# WebSocket Examples

## What is WebSocket?

A normal HTTP request is like sending a letter — you ask, the server replies, and the connection closes. WebSocket is like a phone call — once connected, both sides can talk anytime without re-dialing.

WebSocket is ideal when:
- The server needs to **push data** to the client without the client asking (live prices, notifications)
- You need **low latency** two-way communication (chat, multiplayer games)
- Frequent small updates would make polling too expensive

## Protocol: STOMP over WebSocket

Raw WebSocket is just a pipe — no routing, no pub/sub. **STOMP** (Simple Text Oriented Messaging Protocol) adds structure on top:

```
Client subscribes to:  /topic/public
Client publishes to:   /app/chat.send
Server routes and broadcasts back to all subscribers
```

Spring Boot's `@EnableWebSocketMessageBroker` sets this up with minimal config.

---

## Examples

| | Simple | Real-world |
|---|---|---|
| **What** | Chat app | Stock price ticker |
| **Concept** | Connect, subscribe, publish | Server-push streaming |
| **Server pushes?** | Only on user message | Yes — on a schedule |
| **Status** | ✅ Done | 🔜 Coming soon |

---

## Simple → [Chat App](simple/)

Two browser tabs, one room, instant messaging. Covers all the fundamentals:
- WebSocket handshake
- STOMP subscriptions
- Publishing and receiving messages

## Real-world → [Stock Ticker](real-world/) *(coming soon)*

Server pushes live price updates every second to all connected clients — no client request needed. Adds:
- `@Scheduled` server-side broadcasting
- Multiple topic channels (one per stock symbol)
- Disconnect/reconnect handling on the frontend
