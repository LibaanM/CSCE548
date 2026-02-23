# CSCE 548 - Project 1: Library Management System (Java)

A database-driven library management application with a console-based interface, implemented in Java.

## Project Overview

This project implements a Library Management System with:
- **Database**: PostgreSQL with 5 tables (book_categories, authors, members, books, loans)
- **Data Access Layer**: Java DAOs with full CRUD operations using JDBC
- **Console Front End**: Interactive menu system to retrieve and display records

## Database Schema

The database consists of 5 related tables:

1. **book_categories** - Categories for organizing books (Fiction, Non-Fiction, etc.)
2. **authors** - Book authors with biographical information
3. **members** - Library members with membership information
4. **books** - Books in the library collection
5. **loans** - Book loan/borrowing records (50+ rows)

### Relationships

- `books.author_id` → `authors.author_id` (Foreign Key)
- `books.category_id` → `book_categories.category_id` (Foreign Key)
- `loans.member_id` → `members.member_id` (Foreign Key, CASCADE delete)
- `loans.book_id` → `books.book_id` (Foreign Key)

All tables include:
- Primary keys (SERIAL)
- Foreign key constraints
- Data validation constraints (CHECK constraints)
- Indexes for performance
- Timestamps (created_at)

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

## Setup Instructions

### 1. Install Java and Maven

**macOS:**
```bash
brew install openjdk@11 maven
```

**Linux:**
```bash
sudo apt-get install openjdk-11-jdk maven
```

### 2. Create Database

```bash
# Create database
createdb -U postgres library_management

# Or using psql:
psql -U postgres
CREATE DATABASE library_management;
\q
```

### 3. Create Database Schema

```bash
psql -U postgres -d library_management -f database/schema.sql
```

### 4. Load Test Data (50+ rows)

```bash
psql -U postgres -d library_management -f database/seed_data.sql
```

### 5. Verify Data

```bash
psql -U postgres -d library_management -f verify_data.sql
```

Should show 50+ loans (exceeds 50+ requirement).

### 6. Configure Database Connection

Set environment variables:

```bash
export DB_HOST=localhost
export DB_NAME=library_management
export DB_USER=postgres  # or your PostgreSQL username
export DB_PASSWORD=your_password  # leave empty if no password
export DB_PORT=5432
```

## Building and Running

### Build the Project

```bash
mvn clean compile
```

### Run the Application

```bash
mvn exec:java
```

## Application Features

The console application provides 10 menu options:

1. Display all book categories
2. Display all authors
3. Display all members
4. Display all books
5. Display all loans
6. Display loans with details (uses JOINs)
7. Display detailed loan information
8. Display member loan summary
9. Display book popularity statistics
10. Display database record counts

## Project Structure

```
CSCE548/
├── database/
│   ├── schema.sql          # Database schema
│   └── seed_data.sql       # Test data (50+ loans)
├── src/main/java/edu/csce548/library/
│   ├── Main.java                    # Console application
│   ├── DatabaseConnection.java      # Connection pool management
│   ├── model/                       # Data models (POJOs)
│   │   ├── BookCategory.java
│   │   ├── Author.java
│   │   ├── Member.java
│   │   ├── Book.java
│   │   └── Loan.java
│   ├── dao/                         # Data Access Objects
│   │   ├── BookCategoryDAO.java
│   │   ├── AuthorDAO.java
│   │   ├── MemberDAO.java
│   │   ├── BookDAO.java
│   │   └── LoanDAO.java
│   └── service/                     # Query services
│       └── QueryService.java        # Complex queries with JOINs
├── pom.xml                  # Maven configuration
└── README.md                # This file
```

## Data Access Layer

Each DAO includes full CRUD operations:
- `create()` - Insert new record
- `read()` - Read by ID
- `readAll()` - Read all records
- `update()` - Update existing record
- `delete()` - Delete record

### Testing: Data, Business, and Service Layers

| Layer        | Tester                 | What it tests | How to run |
|-------------|------------------------|---------------|------------|
| **Data**    | `DataLayerTester`      | DAOs only (CRUD + readAll) | `mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester` |
| **Business**| `BusinessLayerTester`  | Business services (which use DAOs) | `mvn exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester` |
| **Service** | `LibraryClient`        | REST API (server must be running) | Start server, then `./run_project2_client.sh` or `mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient` |

All testers need PostgreSQL running and `DB_*` env vars set. Data and business testers run standalone; the service-layer test runs against the API server.

**Inspect current data** (after running testers or API):  
- PostgreSQL: `psql -U <user> -d library_management -f database/inspect_data.sql`  
- MySQL: run `database/inspect_data_mysql.sql` in MySQL Workbench (or `mysql ... < database/inspect_data_mysql.sql`).

## Dependencies

- PostgreSQL JDBC Driver (42.7.1)
- HikariCP (5.1.0) - Connection pooling
- SLF4J (2.0.9) - Logging

## Notes

- Database uses proper foreign key relationships with CASCADE deletes
- All tables have primary keys and constraints
- Seed data includes 50+ loan records (exceeds 50+ requirement)
- Application demonstrates both simple queries and complex JOIN queries

## Troubleshooting

**Connection Error:**
- Verify PostgreSQL is running
- Check database credentials (environment variables)
- Ensure database exists

**Compilation Errors:**
- Ensure Java 11+ is installed: `java -version`
- Ensure Maven is installed: `mvn -version`
- Clean and rebuild: `mvn clean compile`

## Author

Created for CSCE 548 - Project 1

---

# CSCE 548 - Project 2: Business Layer, Service Layer, and API Client

Project 2 adds a **business layer**, **REST service layer**, and a **console client** that invokes the services.

## Project 2 Structure

- **Business layer** (`business/`): Wraps all DAO CRUD operations so the service layer does not call the data layer directly.
  - `BookCategoryBusinessService`, `AuthorBusinessService`, `MemberBusinessService`, `BookBusinessService`, `LoanBusinessService`, `LibraryQueryBusinessService`
- **Service layer** (`api/LibraryServer.java`): REST API (Javalin) that exposes every business layer method over HTTP.
- **Console client** (`client/LibraryClient.java`): Simple program that tests the API with full CRUD (Create → Get → Update → Get → Delete → Get 404).

## Running the API Server (Service Layer)

1. **Ensure PostgreSQL is running** and `DB_*` environment variables are set (same as Project 1), e.g.:
   ```bash
   export DB_PASSWORD=postgres   # or your password
   ```
2. **Start the server** (in its own terminal):
   ```bash
   ./run_project2_server.sh
   ```
   Or: `mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer`
   - If you see "Port already in use", pick another port: `PORT=7001 ./run_project2_server.sh`
3. Server listens on **http://localhost:7000** (or the port you set with `PORT`).

## Hosting the Service

- **Local**: Run the command above; base URL is `http://localhost:7000`.
- **Cloud (e.g. Railway, Render, Heroku)**: Package with `mvn package`, then run the JAR with `java -cp target/workout-tracker-1.0-SNAPSHOT.jar edu.csce548.library.api.LibraryServer`. Set `PORT` and `DB_*` in the platform’s environment. Comments in `LibraryServer.java` describe this.

## Running the Console Client (Test CRUD)

1. **Start the API server first** (see above) and leave it running.
2. In **another terminal**:
   ```bash
   ./run_project2_client.sh
   ```
   Or: `mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient`
   - If the server is on a different port: `BASE_URL=http://localhost:7001 ./run_project2_client.sh`
3. If you see "Server not reachable", the server is not running or you need to set `BASE_URL` to the correct port.

The client runs: **POST** (create category) → **GET** (read) → **PUT** (update) → **GET** (read again) → **DELETE** → **GET** (expect 404). This demonstrates full service functionality.

## REST API Endpoints (Summary)

| Resource     | GET all          | GET by ID      | POST (create) | PUT (update)    | DELETE    |
|-------------|------------------|----------------|---------------|-----------------|-----------|
| Categories  | /api/categories  | /api/categories/:id | /api/categories | /api/categories/:id | /api/categories/:id |
| Authors     | /api/authors     | /api/authors/:id    | /api/authors   | /api/authors/:id   | /api/authors/:id    |
| Members     | /api/members     | /api/members/:id    | /api/members  | /api/members/:id   | /api/members/:id    |
| Books       | /api/books       | /api/books/:id      | /api/books    | /api/books/:id     | /api/books/:id      |
| Loans       | /api/loans       | /api/loans/:id      | /api/loans    | /api/loans/:id     | /api/loans/:id      |

Additional: `GET /api/loans/with-details`, `GET /api/loans/:id/details`, `GET /api/members/:id/summary`, `GET /api/books/popularity`, `GET /api/records/counts`.
