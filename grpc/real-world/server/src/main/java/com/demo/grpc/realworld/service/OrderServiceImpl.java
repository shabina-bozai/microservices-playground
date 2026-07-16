package com.demo.grpc.realworld.service;

import com.demo.grpc.realworld.OrderConfirmation;
import com.demo.grpc.realworld.OrderServiceGrpc;
import com.demo.grpc.realworld.OrderStatus;
import com.demo.grpc.realworld.PlaceOrderRequest;
import com.demo.grpc.realworld.TrackOrderRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@GRpcService
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private final ConcurrentHashMap<String, PlaceOrderRequest> orderStore = new ConcurrentHashMap<>();

    @Override
    public void placeOrder(PlaceOrderRequest request, StreamObserver<OrderConfirmation> responseObserver) {
        String orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        orderStore.put(orderId, request);

        System.out.printf("Order placed: id=%s, product=%s, qty=%d, customer=%s%n",
                orderId, request.getProductName(), request.getQuantity(), request.getCustomerName());

        OrderConfirmation confirmation = OrderConfirmation.newBuilder()
                .setOrderId(orderId)
                .setMessage("Order accepted for " + request.getQuantity() + "x " + request.getProductName()
                        + ". Thank you, " + request.getCustomerName() + "!")
                .build();

        responseObserver.onNext(confirmation);
        responseObserver.onCompleted();
    }

    @Override
    public void trackOrder(TrackOrderRequest request, StreamObserver<OrderStatus> responseObserver) {
        String orderId = request.getOrderId();

        if (!orderStore.containsKey(orderId)) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Order not found: " + orderId)
                    .asRuntimeException());
            return;
        }

        String[][] stages = {
            {"RECEIVED",            "Order received and queued for processing"},
            {"INVENTORY_CHECKED",   "Items confirmed in stock"},
            {"PAYMENT_PROCESSED",   "Payment authorized and captured"},
            {"DISPATCHED",          "Package handed to courier"},
            {"DELIVERED",           "Package delivered successfully"}
        };

        try {
            for (String[] stage : stages) {
                OrderStatus status = OrderStatus.newBuilder()
                        .setOrderId(orderId)
                        .setStatus(stage[0])
                        .setDetail(stage[1])
                        .setTimestamp(System.currentTimeMillis())
                        .build();
                responseObserver.onNext(status);
                Thread.sleep(1000);
            }
            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Stream interrupted")
                    .asRuntimeException());
        }
    }
}
