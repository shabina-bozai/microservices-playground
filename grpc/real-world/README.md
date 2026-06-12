# gRPC Real-world — Order Processing Pipeline

> 🔜 Coming soon

## What we'll build

Place an order and receive a **stream of status updates** as it moves through the pipeline — inventory check, payment, fulfilment, shipping. Demonstrates server-side streaming RPC.

## Concepts this adds over the simple example

- Server-streaming RPC — one request, multiple responses over time
- Multiple `.proto` files (order, inventory, payment)
- Inter-service communication — Order service calls Inventory and Payment services via gRPC
- gRPC-Web + Envoy proxy to reach the browser from React

## Planned stack

- **Server:** Spring Boot + `grpc-spring-boot-starter`
- **Client:** React + gRPC-Web + Envoy proxy sidecar
