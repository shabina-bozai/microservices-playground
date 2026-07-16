# REST Simple — Todo CRUD API

A minimal full-stack app demonstrating REST fundamentals: all five HTTP verbs, status codes, JPA persistence, and an H2 in-memory database.

---

## Tech Stack

| Layer    | Technology              |
|----------|-------------------------|
| Runtime  | Java 21                 |
| Backend  | Spring Boot 3.3.5       |
| Database | H2 (in-memory)          |
| ORM      | Spring Data JPA         |
| Frontend | React 18                |

---

## Project Structure

```
rest/simple/
├── server/
│   ├── pom.xml
│   └── src/main/java/com/demo/rest/simple/
│       ├── RestSimpleApplication.java
│       ├── model/Todo.java
│       ├── repository/TodoRepository.java
│       └── controller/TodoController.java
│   └── src/main/resources/application.properties
└── client/
    ├── package.json
    ├── .env
    ├── public/index.html
    └── src/
        ├── index.js
        └── App.js
```

---

## How to Run

### Backend (port 8081)

```bash
cd rest/simple/server
mvn spring-boot:run
```

### Frontend (port 3001)

```bash
cd rest/simple/client
npm install
npm start
```

Open http://localhost:3001 in your browser. The React app proxies `/api` requests to `http://localhost:8081`.

The H2 console is available at http://localhost:8081/h2-console (JDBC URL: `jdbc:h2:mem:tododb`).

---

## API Endpoints

| Method | Endpoint         | Description              | Success Status |
|--------|------------------|--------------------------|----------------|
| GET    | /api/todos       | List all todos           | 200 OK         |
| GET    | /api/todos/{id}  | Get a single todo        | 200 OK / 404   |
| POST   | /api/todos       | Create a new todo        | 201 Created    |
| PUT    | /api/todos/{id}  | Replace a todo           | 200 OK / 404   |
| DELETE | /api/todos/{id}  | Delete a todo            | 204 No Content |

### Example Request

```bash
curl -X POST http://localhost:8081/api/todos \
  -H "Content-Type: application/json" \
  -d '{"title": "Learn REST"}'
```

---

## Concepts Covered

- **REST verbs** — GET, POST, PUT, DELETE mapped to CRUD operations
- **HTTP status codes** — 200, 201, 204, 404 used semantically
- **Spring Data JPA** — repository pattern with zero boilerplate
- **H2 in-memory database** — zero-config persistence for demos
- **React state management** — fetching, creating, updating, deleting via `fetch()`
