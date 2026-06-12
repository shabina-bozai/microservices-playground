# REST Examples

> 🔜 Coming soon

## What is REST?

REST (Representational State Transfer) is the most common API style. Each request is independent — the client asks, the server replies, the connection closes.

REST is ideal when:
- You're exposing **resources** (users, products, orders) with standard CRUD operations
- Responses can be **cached**
- You need broad compatibility — every HTTP client can talk to a REST API

## HTTP verbs mapped to actions

| Verb | Action | Example |
|---|---|---|
| GET | Read | `GET /todos` — fetch all todos |
| POST | Create | `POST /todos` — create a new todo |
| PUT | Replace | `PUT /todos/1` — replace todo #1 |
| PATCH | Partial update | `PATCH /todos/1` — update one field |
| DELETE | Delete | `DELETE /todos/1` — delete todo #1 |

---

## Examples

| | Simple | Real-world |
|---|---|---|
| **What** | Todo CRUD API | Product catalog |
| **Concepts** | GET/POST/PUT/DELETE, status codes | Pagination, filtering, search, relations |
| **Database** | H2 in-memory | Postgres |
| **Status** | 🔜 Coming soon | 🔜 Coming soon |

---

## Simple → Todo CRUD API *(coming soon)*

Classic to-do list. Every HTTP verb, proper status codes, request validation.

## Real-world → Product Catalog *(coming soon)*

E-commerce product listing with search, category filtering, pagination, and image URLs. Connects to a real Postgres database.
