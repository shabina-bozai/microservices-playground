# Microservices Playground

A hands-on collection of API and communication pattern examples using **Java Spring Boot** (backend) and **React** (frontend).

Each protocol has a `simple/` example to learn the concept and a `real-world/` example that shows how it's used in practice.

---

## Examples

| Protocol | Simple | Real-world | Status |
|---|---|---|---|
| WebSocket | [Chat app](websocket/simple/) | [Stock ticker](websocket/real-world/) | ✅ Simple done |
| REST | [Todo CRUD API](rest/simple/) | [Product catalog](rest/real-world/) | 🔜 Coming soon |
| GraphQL | [Book library](graphql/simple/) | [Social feed](graphql/real-world/) | 🔜 Coming soon |
| gRPC | [Calculator service](grpc/simple/) | [Order pipeline](grpc/real-world/) | 🔜 Coming soon |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.3 |
| Frontend | React 18 |
| Build | Maven (server), npm (client) |

---

## How to navigate

Each example is fully self-contained — it has its own `server/`, `client/`, and `README.md`. You can run any example independently without touching the others.

```
microservices-playground/
├── websocket/
│   ├── simple/       ← Start here for WebSocket
│   └── real-world/
├── rest/
│   ├── simple/       ← Start here for REST
│   └── real-world/
├── graphql/
│   ├── simple/       ← Start here for GraphQL
│   └── real-world/
└── grpc/
    ├── simple/       ← Start here for gRPC
    └── real-world/
```

---

## Prerequisites

- Java 21+
- Maven 3.6+
- Node.js 18+

Each example's README has its own run instructions.
