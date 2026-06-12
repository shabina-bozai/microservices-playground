# WebSocket Chat Demo

A real-time chat application built with **Spring Boot** (backend) and **React** (frontend) using the STOMP messaging protocol over WebSocket.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2, Spring WebSocket |
| Frontend | React 18, @stomp/stompjs, SockJS |
| Protocol | WebSocket + STOMP over SockJS |

---

## Project Structure

```
websocket-demo/
├── server/                                         ← Spring Boot backend
│   ├── pom.xml
│   └── src/main/java/com/demo/websocket/
│       ├── WebsocketDemoApplication.java           ← Entry point
│       ├── config/
│       │   └── WebSocketConfig.java                ← STOMP broker + endpoint setup
│       ├── controller/
│       │   └── ChatController.java                 ← Message handlers
│       └── model/
│           └── ChatMessage.java                    ← Message data model
└── client/                                         ← React frontend
    ├── package.json
    └── src/
        ├── index.js                                ← React entry point
        ├── App.js                                  ← WebSocket client + chat UI
        └── App.css                                 ← Styles
```

---

## How It Works

```
User types + clicks Send
        ↓
client.publish(/app/chat.send)
        ↓
Spring Boot ChatController
        ↓
Adds timestamp → broadcasts to /topic/public
        ↓
ALL subscribed browser tabs receive the message
        ↓
React re-renders the chat
```

### Key endpoints

| Direction | Destination | Description |
|---|---|---|
| Client → Server | `/app/chat.send` | Send a chat message |
| Client → Server | `/app/chat.join` | Announce joining the room |
| Server → Client | `/topic/public` | Broadcast channel all clients subscribe to |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 18+

### Run the backend

```bash
cd server
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

### Run the frontend

```bash
cd client
npm install   # Install dependencies (first time only)
npm start     # Start the React development server
```

Frontend starts on `http://localhost:3000`

> **Note:** `npm install` downloads and installs all dependencies listed in `package.json`. This is required before running the frontend for the first time. Dependencies are stored in `node_modules/` and are added to `.gitignore` to keep the repository small.

### Try it out

1. Open `http://localhost:3000` in **two browser tabs**
2. Enter a different username in each tab
3. Send messages — they appear instantly in both tabs

---

## Limitations (demo scope)

This is a learning project. It is intentionally simple and not production-ready.

| Area | Current state | Production would need |
|---|---|---|
| Authentication | No auth — any username accepted | JWT / OAuth2 token on connect |
| Rooms | Single public channel | Per-conversation channels (`/topic/room.{id}`) |
| Message broker | In-memory SimpleBroker | Redis or RabbitMQ for multi-server support |
| Persistence | None — history lost on refresh | Database (Postgres / MongoDB) |
| Scale | Single server instance | Load balancer + external broker + sticky sessions |
| Presence | None | Track online/offline status via connect/disconnect events |

---

## Concepts Covered

- **WebSocket handshake** — upgrading an HTTP connection to a persistent two-way channel
- **STOMP protocol** — a simple messaging layer on top of WebSocket (destinations, subscriptions, frames)
- **SockJS** — fallback transport for environments where WebSocket is blocked
- **Pub/Sub messaging** — clients publish to a destination; all subscribers receive the broadcast
- **Spring `@MessageMapping`** — routes incoming STOMP messages to controller methods (like `@RequestMapping` for REST)
- **React `useState` / `useRef`** — managing live connection state and UI updates without re-renders
