package com.demo.grpc.simple;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GrpcSimpleClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GrpcSimpleClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub stub =
                CalculatorServiceGrpc.newBlockingStub(channel);

        System.out.println("=== gRPC Calculator Client ===");
        System.out.println();

        // Add: 10 + 5
        CalcRequest addRequest = CalcRequest.newBuilder().setA(10).setB(5).build();
        CalcResponse addResponse = stub.add(addRequest);
        System.out.println("ADD:      " + addResponse.getOperation());

        // Subtract: 10 - 3
        CalcRequest subRequest = CalcRequest.newBuilder().setA(10).setB(3).build();
        CalcResponse subResponse = stub.subtract(subRequest);
        System.out.println("SUBTRACT: " + subResponse.getOperation());

        // Multiply: 6 * 7
        CalcRequest mulRequest = CalcRequest.newBuilder().setA(6).setB(7).build();
        CalcResponse mulResponse = stub.multiply(mulRequest);
        System.out.println("MULTIPLY: " + mulResponse.getOperation());

        // Divide: 22 / 7
        CalcRequest divRequest = CalcRequest.newBuilder().setA(22).setB(7).build();
        try {
            CalcResponse divResponse = stub.divide(divRequest);
            System.out.printf("DIVIDE:   %s%n", divResponse.getOperation());
        } catch (StatusRuntimeException e) {
            System.out.println("DIVIDE error: " + e.getStatus().getDescription());
        }

        // Demonstrate divide by zero error handling
        System.out.println();
        System.out.println("Attempting division by zero...");
        CalcRequest divByZero = CalcRequest.newBuilder().setA(10).setB(0).build();
        try {
            stub.divide(divByZero);
        } catch (StatusRuntimeException e) {
            System.out.println("Expected error: " + e.getStatus().getDescription());
        }

        System.out.println();
        System.out.println("=== Done ===");

        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
