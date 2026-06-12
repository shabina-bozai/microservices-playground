# gRPC Examples

> 🔜 Coming soon

## What is gRPC?

gRPC is a high-performance communication framework developed by Google. Instead of JSON over HTTP, it uses **Protocol Buffers** (binary format) over HTTP/2. You define a `.proto` contract file and both server and client generate code from it.

gRPC is ideal when:
- Services talk **service-to-service** (not browser-to-server directly)
- You need **maximum performance** — binary is faster and smaller than JSON
- You want a **strict typed contract** shared between teams
- You need **streaming** — one request, continuous response

## REST vs gRPC

```
REST:
  - Text (JSON) over HTTP/1.1
  - No shared contract — docs can drift from reality
  - Request → Response only

gRPC:
  - Binary (Protobuf) over HTTP/2
  - .proto file IS the contract — server and client generated from it
  - Unary, server-streaming, client-streaming, bidirectional streaming
```

## The .proto file

```proto
service Calculator {
  rpc Add (AddRequest) returns (AddResponse);
}

message AddRequest {
  int32 a = 1;
  int32 b = 2;
}

message AddResponse {
  int32 result = 1;
}
```

Both the Spring Boot server and the client generate code from this file. If the contract changes, compilation fails — no silent mismatches.

---

## Examples

| | Simple | Real-world |
|---|---|---|
| **What** | Calculator service | Order processing pipeline |
| **Concepts** | Unary RPC, Protobuf, codegen | Server-streaming, multiple services |
| **Status** | 🔜 Coming soon | 🔜 Coming soon |

---

## Simple → Calculator Service *(coming soon)*

Add, subtract, multiply. One request, one response (unary RPC). Focuses on the proto → codegen → implement flow.

## Real-world → Order Pipeline *(coming soon)*

Place an order and stream back status updates (accepted → inventory checked → payment processed → shipped). Uses server-side streaming RPC.
