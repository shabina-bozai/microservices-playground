# WebSocket Real-world — Stock Price Ticker

> 🔜 Coming soon

## What we'll build

A live stock price dashboard where the **server pushes price updates every second** to all connected clients — no polling, no client requests.

## Concepts this adds over the simple example

- `@Scheduled` broadcasting — server initiates messages on a timer
- Multiple channels — each stock symbol gets its own `/topic/stock.{symbol}`
- Client selects which symbols to watch (subscribe/unsubscribe dynamically)
- Reconnection logic — auto-reconnect if the connection drops

## Planned stack

- **Server:** Spring Boot + `@Scheduled` + SimpMessagingTemplate
- **Client:** React + Recharts (live line chart)
