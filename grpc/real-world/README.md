# gRPC Real-world — Order Processing Pipeline

Demonstrates **server-side streaming RPC** with a realistic order-processing scenario. The client places a single order (unary call) and then streams live status updates as the order moves through the pipeline.

## Structure

```
real-world/
  proto/    order.proto              — service + message definitions
  server/   grpc-realworld-server    — Spring Boot gRPC server (port 9091)
  client/   grpc-realworld-client    — Java CLI client (CommandLineRunner)
```

## Proto Contract

```protobuf
service OrderService {
    // Unary: place an order
    rpc PlaceOrder (PlaceOrderRequest) returns (OrderConfirmation);

    // Server streaming: track order status updates
    rpc TrackOrder (TrackOrderRequest) returns (stream OrderStatus);
}
```

## Concepts demonstrated

| Concept | Where |
|---------|-------|
| Unary RPC | `PlaceOrder` — one request, one response |
| Server-streaming RPC | `TrackOrder` — one request, multiple responses pushed over time |
| `ConcurrentHashMap` order store | `OrderServiceImpl` — in-memory order registry |
| Async `StreamObserver` client | `GrpcRealworldClientApplication` — non-blocking stream handler |
| `CountDownLatch` | Client waits for stream completion before exiting |

## Prerequisites

| Tool  | Version |
|-------|---------|
| Java  | 21      |
| Maven | 3.8+    |

> **Note:** The `protobuf-maven-plugin` downloads `protoc` and `protoc-gen-grpc-java` automatically from Maven Central on first compile. No manual protoc installation is needed.

## Running

### 1. Start the server

```bash
cd grpc/real-world/server
mvn spring-boot:run
```

Server starts on **port 9091**.

### 2. Run the client (in a separate terminal)

```bash
cd grpc/real-world/client
mvn spring-boot:run
```

### Expected output

```
=== gRPC Order Processing Client ===

--- Placing Order ---
Order ID : A3F7C1B2
Message  : Order accepted for 2x Wireless Headphones. Thank you, Alice!

--- Tracking Order A3F7C1B2 ---
[14:23:01.042] RECEIVED                  Order received and queued for processing
[14:23:02.051] INVENTORY_CHECKED         Items confirmed in stock
[14:23:03.057] PAYMENT_PROCESSED         Payment authorized and captured
[14:23:04.063] DISPATCHED                Package handed to courier
[14:23:05.068] DELIVERED                 Package delivered successfully

=== Order tracking complete ===
```

## Building a Fat JAR

```bash
# Server
cd grpc/real-world/server
mvn package
java -jar target/grpc-realworld-server-0.0.1-SNAPSHOT.jar

# Client
cd grpc/real-world/client
mvn package
java -jar target/grpc-realworld-client-0.0.1-SNAPSHOT.jar
```

## How server streaming works

1. The client calls `asyncStub.trackOrder(request, streamObserver)` — this returns immediately.
2. The server's `trackOrder` method sends five `OrderStatus` messages via `responseObserver.onNext()` with a 1-second pause between each.
3. Each message triggers `onNext()` on the client's `StreamObserver`, which prints the status line.
4. After all five messages, the server calls `onCompleted()` — the client's `onCompleted()` fires, counting down the latch and allowing the JVM to exit.
