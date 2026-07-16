# GraphQL Simple — Book Library

A simple GraphQL demo using Spring Boot and React. Demonstrates schema definition, queries, mutations, and nested field resolvers using an in-memory book library.

## Stack

- **Server**: Java 21, Spring Boot 3.3.5, Spring for GraphQL, port 8083
- **Client**: React 18, Apollo Client 3, port 3003

## Features

- Query all books (with nested author resolution)
- Query all authors (with nested books resolution)
- Query a single book or author by ID
- Add a new book (select an existing author)
- Add a new author
- GraphiQL playground at `http://localhost:8083/graphiql`

## Project Structure

```
simple/
├── server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/demo/graphql/simple/
│       │   ├── GraphqlSimpleApplication.java
│       │   ├── model/
│       │   │   ├── Author.java
│       │   │   └── Book.java
│       │   ├── repository/
│       │   │   └── InMemoryRepository.java
│       │   └── controller/
│       │       └── BookController.java
│       └── resources/
│           ├── graphql/schema.graphqls
│           └── application.properties
└── client/
    ├── package.json
    ├── .env
    ├── public/index.html
    └── src/
        ├── index.js
        ├── App.js
        └── App.css
```

## Running

### Server

```bash
cd server
mvn spring-boot:run
```

Server starts at http://localhost:8083
GraphiQL at http://localhost:8083/graphiql

### Client

```bash
cd client
npm install
npm start
```

Client starts at http://localhost:3003

## GraphQL Schema

```graphql
type Query {
    books: [Book!]!
    book(id: ID!): Book
    authors: [Author!]!
    author(id: ID!): Author
}

type Mutation {
    addBook(title: String!, authorId: ID!): Book!
    addAuthor(name: String!): Author!
}

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
```

## Example Queries

```graphql
# Get all books with author names
query {
  books {
    id
    title
    author {
      name
    }
  }
}

# Add a new author
mutation {
  addAuthor(name: "Ursula K. Le Guin") {
    id
    name
  }
}

# Add a new book
mutation {
  addBook(title: "The Left Hand of Darkness", authorId: "4") {
    id
    title
    author {
      name
    }
  }
}
```

## Key Concepts

- **@QueryMapping** — maps a method to a GraphQL Query field by method name
- **@MutationMapping** — maps a method to a GraphQL Mutation field
- **@SchemaMapping** — resolves a nested field on a type (e.g., `Book.author`, `Author.books`)
- **@Argument** — binds a GraphQL argument to a method parameter
- In-memory storage seeded with 3 authors and 6 books on startup
