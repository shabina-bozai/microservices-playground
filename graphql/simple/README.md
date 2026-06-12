# GraphQL Simple — Book Library

> 🔜 Coming soon

## What we'll build

A book library where you can query books by author and add new books — covering the two core GraphQL operations: Query and Mutation.

## Planned stack

- **Server:** Spring Boot + Spring for GraphQL
- **Client:** React + Apollo Client

## Schema planned

```graphql
type Book {
  id: ID!
  title: String!
  author: Author!
}

type Author {
  id: ID!
  name: String!
  books: [Book!]!
}

type Query {
  books: [Book!]!
  book(id: ID!): Book
  authors: [Author!]!
}

type Mutation {
  addBook(title: String!, authorId: ID!): Book!
}
```
