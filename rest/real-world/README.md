# REST Real-world — Product Catalog

A production-style product catalog API with categories, search, filtering, and pagination — the kind of backend behind a real shopping site.

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
rest/real-world/
├── server/
│   ├── pom.xml
│   └── src/main/java/com/demo/rest/realworld/
│       ├── RestRealworldApplication.java
│       ├── DataSeeder.java
│       ├── model/
│       │   ├── Category.java
│       │   └── Product.java
│       ├── repository/
│       │   ├── CategoryRepository.java
│       │   └── ProductRepository.java
│       └── controller/
│           ├── CategoryController.java
│           └── ProductController.java
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

### Backend (port 8082)

```bash
cd rest/real-world/server
mvn spring-boot:run
```

The server seeds 3 categories and 9 products on startup automatically.

### Frontend (port 3002)

```bash
cd rest/real-world/client
npm install
npm start
```

Open http://localhost:3002. The React app proxies `/api` requests to `http://localhost:8082`.

The H2 console is available at http://localhost:8082/h2-console (JDBC URL: `jdbc:h2:mem:catalogdb`).

---

## API Endpoints

### Categories

| Method | Endpoint         | Description        | Status  |
|--------|------------------|--------------------|---------|
| GET    | /api/categories  | List all categories| 200 OK  |

### Products

| Method | Endpoint            | Description                      | Status         |
|--------|---------------------|----------------------------------|----------------|
| GET    | /api/products       | List products (with filters)     | 200 OK         |
| GET    | /api/products/{id}  | Get a single product             | 200 OK / 404   |
| POST   | /api/products       | Create a product                 | 201 Created    |
| PUT    | /api/products/{id}  | Replace a product                | 200 OK / 404   |
| DELETE | /api/products/{id}  | Delete a product                 | 204 No Content |

### Query Parameters for GET /api/products

| Param      | Type    | Description                       |
|------------|---------|-----------------------------------|
| page       | int     | Page number, zero-based (default 0)|
| size       | int     | Page size (default 10)            |
| category   | Long    | Filter by category ID             |
| search     | String  | Filter by name (case-insensitive) |
| minPrice   | Double  | Filter by minimum price           |
| maxPrice   | Double  | Filter by maximum price           |

### Example Requests

```bash
# Get all products, page 2
curl "http://localhost:8082/api/products?page=1&size=9"

# Search within a category
curl "http://localhost:8082/api/products?category=1&search=laptop"

# Price range filter
curl "http://localhost:8082/api/products?minPrice=20&maxPrice=50"
```

---

## Concepts Covered

- **Pagination** — Spring Data `Pageable` / `Page<T>` with `PageRequest.of(page, size)`
- **Filtering** — dynamic query dispatch using derived query methods
- **Relations** — `@ManyToOne` between Product and Category
- **Data seeding** — `CommandLineRunner` populates H2 on startup
- **React sidebar + grid UI** — category sidebar, search/price filters, paginated product grid
- **Query param composition** — `URLSearchParams` builds the API call from UI state
