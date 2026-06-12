# gRPC Simple — Calculator Service

> 🔜 Coming soon

## What we'll build

A calculator service (add, subtract, multiply) using unary gRPC — one request in, one response out. Focuses entirely on the proto → codegen → implement → call workflow.

## Planned stack

- **Proto:** `calculator.proto`
- **Server:** Spring Boot + `grpc-spring-boot-starter`
- **Client:** Node.js script (or React via Envoy proxy)

## Proto contract planned

```proto
syntax = "proto3";

service Calculator {
  rpc Add      (CalcRequest) returns (CalcResponse);
  rpc Subtract (CalcRequest) returns (CalcResponse);
  rpc Multiply (CalcRequest) returns (CalcResponse);
}

message CalcRequest {
  double a = 1;
  double b = 2;
}

message CalcResponse {
  double result = 1;
}
```
