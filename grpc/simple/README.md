# gRPC Simple — Calculator Service

A minimal gRPC demo with a four-operation calculator. Demonstrates **unary RPCs** (one request, one response) using Spring Boot and the `grpc-spring-boot-starter`.

## Structure

```
simple/
  proto/    calculator.proto          — service + message definitions
  server/   grpc-simple-server        — Spring Boot gRPC server (port 9090)
  client/   grpc-simple-client        — Java CLI client (CommandLineRunner)
```

## Proto Contract

```protobuf
service CalculatorService {
    rpc Add      (CalcRequest) returns (CalcResponse);
    rpc Subtract (CalcRequest) returns (CalcResponse);
    rpc Multiply (CalcRequest) returns (CalcResponse);
    rpc Divide   (CalcRequest) returns (CalcResponse);
}
```

## Prerequisites

| Tool  | Version |
|-------|---------|
| Java  | 21      |
| Maven | 3.8+    |

> **Note:** The `protobuf-maven-plugin` downloads `protoc` and `protoc-gen-grpc-java` automatically from Maven Central on first compile. No manual protoc installation is needed.

## Running

### 1. Start the server

```bash
cd grpc/simple/server
mvn spring-boot:run
```

Server starts on **port 9090**. You should see:
```
gRPC Server started, listening on port 9090
```

### 2. Run the client (in a separate terminal)

```bash
cd grpc/simple/client
mvn spring-boot:run
```

### Expected output

```
=== gRPC Calculator Client ===

ADD:      10.0 + 5.0 = 15.0
SUBTRACT: 10.0 - 3.0 = 7.0
MULTIPLY: 6.0 * 7.0 = 42.0
DIVIDE:   22.0 / 7.0 = 3.142857142857143

Attempting division by zero...
Expected error: Division by zero is not allowed

=== Done ===
```

## Key Concepts

- **`@GRpcService`** — marks a class as a gRPC service implementation; the starter auto-registers it.
- **`CalculatorServiceImplBase`** — generated base class; override each RPC method.
- **`StreamObserver`** — gRPC's callback interface: call `onNext()` with the response then `onCompleted()`.
- **`StatusRuntimeException`** — returned to the client when `onError()` is called with a `Status`.
- **`ManagedChannel`** + **blocking stub** — simplest client pattern; blocks the calling thread until the response arrives.

## Building a Fat JAR

```bash
# Server
cd grpc/simple/server
mvn package
java -jar target/grpc-simple-server-0.0.1-SNAPSHOT.jar

# Client
cd grpc/simple/client
mvn package
java -jar target/grpc-simple-client-0.0.1-SNAPSHOT.jar
```
