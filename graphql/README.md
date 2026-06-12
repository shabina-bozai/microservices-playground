# GraphQL Examples

> 🔜 Coming soon

## What is GraphQL?

GraphQL is a query language for APIs. Instead of hitting multiple REST endpoints and getting fixed response shapes, the client asks for **exactly the fields it needs** in a single request.

GraphQL is ideal when:
- The client needs **flexible, nested data** in one request
- Different clients (mobile vs web) need **different shapes** of the same data
- You want to avoid **over-fetching** (getting fields you don't need) and **under-fetching** (needing multiple requests)

## REST vs GraphQL

```
REST — fixed shape, multiple round trips:
  GET /users/1          → { id, name, email, address, ... }
  GET /users/1/posts    → [{ id, title, body, ... }]

GraphQL — you define the shape, one request:
  query {
    user(id: 1) {
      name
      posts { title }
    }
  }
```

---

## Examples

| | Simple | Real-world |
|---|---|---|
| **What** | Book library | Social feed |
| **Concepts** | Query, Mutation, Schema | Nested queries, pagination, N+1 fix |
| **Status** | 🔜 Coming soon | 🔜 Coming soon |

---

## Simple → Book Library *(coming soon)*

Query books and authors, add new books. Covers schema definition, queries, and mutations.

## Real-world → Social Feed *(coming soon)*

Posts with comments, likes, and authors — deeply nested data that would require many REST calls but one GraphQL query. Also covers the N+1 query problem and how DataLoader solves it.
