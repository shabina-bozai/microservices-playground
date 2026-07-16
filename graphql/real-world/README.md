# GraphQL Real-world — Social Feed

A real-world GraphQL demo using Spring Boot and React. Shows deeply nested queries, pagination, mutations (like, comment, post), and field-level resolvers — the kind of data graph where GraphQL shines over REST.

## Stack

- **Server**: Java 21, Spring Boot 3.3.5, Spring for GraphQL, port 8084
- **Client**: React 18, Apollo Client 3, port 3004

## Features

- Paginated social feed (posts with author, comments, like count)
- Like a post (mutation, updates count live)
- Add a comment to any post (expand/collapse per post)
- Create a new post
- Load more pagination (appends next page to feed)
- GraphiQL playground at `http://localhost:8084/graphiql`

## Project Structure

```
real-world/
├── server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/demo/graphql/realworld/
│       │   ├── GraphqlRealworldApplication.java
│       │   ├── model/
│       │   │   ├── User.java
│       │   │   ├── Post.java
│       │   │   ├── Comment.java
│       │   │   └── FeedPage.java
│       │   ├── repository/
│       │   │   └── FeedRepository.java
│       │   └── controller/
│       │       └── FeedController.java
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

Server starts at http://localhost:8084
GraphiQL at http://localhost:8084/graphiql

### Client

```bash
cd client
npm install
npm start
```

Client starts at http://localhost:3004

## GraphQL Schema

```graphql
type Query {
    feed(page: Int = 0, size: Int = 5): FeedPage!
    post(id: ID!): Post
}

type Mutation {
    createPost(content: String!, authorId: ID!): Post!
    addComment(postId: ID!, content: String!, authorId: ID!): Comment!
    likePost(postId: ID!): Post!
}

type FeedPage {
    posts: [Post!]!
    page: Int!
    size: Int!
    hasMore: Boolean!
}

type Post {
    id: ID!
    content: String!
    author: User!
    comments: [Comment!]!
    likesCount: Int!
    createdAt: String!
}

type Comment {
    id: ID!
    content: String!
    author: User!
    createdAt: String!
}

type User {
    id: ID!
    username: String!
    avatarUrl: String!
}
```

## Example Queries

```graphql
# Get first page of feed
query {
  feed(page: 0, size: 5) {
    posts {
      id
      content
      likesCount
      author { username }
      comments {
        content
        author { username }
      }
    }
    hasMore
  }
}

# Like a post
mutation {
  likePost(postId: "1") {
    id
    likesCount
  }
}

# Add a comment
mutation {
  addComment(postId: "2", content: "Great post!", authorId: "1") {
    id
    content
    author { username }
  }
}

# Create a new post
mutation {
  createPost(content: "Hello GraphQL world!", authorId: "2") {
    id
    content
    author { username }
  }
}
```

## Key Concepts

- **Nested resolvers** — `Post.author`, `Post.comments`, `Comment.author` each resolved by a `@SchemaMapping` method
- **Pagination** — offset-based (`page` + `size`) returning a `FeedPage` wrapper with `hasMore`
- **Mutations** — createPost, addComment, likePost all mutate in-memory state
- In-memory storage seeded with 3 users, 6 posts, and 10 comments on startup
