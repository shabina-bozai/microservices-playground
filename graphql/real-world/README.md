# GraphQL Real-world — Social Feed

> 🔜 Coming soon

## What we'll build

A social feed with posts, comments, likes, and author profiles — the kind of nested data where GraphQL shines over REST.

## Concepts this adds over the simple example

- Deeply nested queries (post → comments → author → avatar)
- Pagination with cursors
- The N+1 query problem and how **DataLoader** (batching) solves it
- Fragments for reusable query parts

## Planned stack

- **Server:** Spring Boot + Spring for GraphQL + DataLoader
- **Client:** React + Apollo Client + infinite scroll
