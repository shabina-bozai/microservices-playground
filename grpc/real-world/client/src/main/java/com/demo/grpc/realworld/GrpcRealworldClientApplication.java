package com.demo.grpc.realworld;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GrpcRealworldClientApplication implements CommandLineRunner {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void main(String[] args) {
        SpringApplication.run(GrpcRealworldClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        OrderServiceGrpc.OrderServiceBlockingStub blockingStub =
                OrderServiceGrpc.newBlockingStub(channel);
        OrderServiceGrpc.OrderServiceStub asyncStub =
                OrderServiceGrpc.newStub(channel);

        System.out.println("=== gRPC Order Processing Client ===");
        System.out.println();

        // Step 1: Place an order (unary)
        System.out.println("--- Placing Order ---");
        PlaceOrderRequest placeRequest = PlaceOrderRequest.newBuilder()
                .setProductName("Wireless Headphones")
                .setQuantity(2)
                .setCustomerName("Alice")
                .build();

        OrderConfirmation confirmation = blockingStub.placeOrder(placeRequest);
        String orderId = confirmation.getOrderId();
        System.out.println("Order ID : " + orderId);
        System.out.println("Message  : " + confirmation.getMessage());
        System.out.println();

        // Step 2: Track the order (server streaming)
        System.out.println("--- Tracking Order " + orderId + " ---");

        CountDownLatch latch = new CountDownLatch(1);

        asyncStub.trackOrder(
                TrackOrderRequest.newBuilder().setOrderId(orderId).build(),
                new StreamObserver<OrderStatus>() {
                    @Override
                    public void onNext(OrderStatus status) {
                        String time = LocalDateTime
                                .ofInstant(Instant.ofEpochMilli(status.getTimestamp()), ZoneId.systemDefault())
                                .format(FORMATTER);
                        System.out.printf("[%s] %-25s %s%n", time, status.getStatus(), status.getDetail());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Error tracking order: " + t.getMessage());
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println();
                        System.out.println("=== Order tracking complete ===");
                        latch.countDown();
                    }
                });

        // Wait for stream to finish (max 30 seconds)
        latch.await(30, TimeUnit.SECONDS);

        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
