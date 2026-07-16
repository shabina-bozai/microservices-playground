package com.demo.grpc.simple.service;

import com.demo.grpc.simple.CalcRequest;
import com.demo.grpc.simple.CalcResponse;
import com.demo.grpc.simple.CalculatorServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void add(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        double result = request.getA() + request.getB();
        CalcResponse response = CalcResponse.newBuilder()
                .setResult(result)
                .setOperation(request.getA() + " + " + request.getB() + " = " + result)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void subtract(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        double result = request.getA() - request.getB();
        CalcResponse response = CalcResponse.newBuilder()
                .setResult(result)
                .setOperation(request.getA() + " - " + request.getB() + " = " + result)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void multiply(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        double result = request.getA() * request.getB();
        CalcResponse response = CalcResponse.newBuilder()
                .setResult(result)
                .setOperation(request.getA() + " * " + request.getB() + " = " + result)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void divide(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        if (request.getB() == 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Division by zero is not allowed")
                    .asRuntimeException());
            return;
        }
        double result = request.getA() / request.getB();
        CalcResponse response = CalcResponse.newBuilder()
                .setResult(result)
                .setOperation(request.getA() + " / " + request.getB() + " = " + result)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
