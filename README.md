# Library Management System

**CSCE 548** — A full-stack library management application with a PostgreSQL database, Java backend (data, business, and REST service layers), and a web client.

---

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Deployment](#deployment)
- [Project 1: Data Layer & Console App](#project-1-data-layer--console-app)
- [Project 2: Business Layer, API & Console Client](#project-2-business-layer-api--console-client)
- [Project 3 & 4: Web Client (Full CRUD)](#project-3--4-web-client-full-crud)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)

---

## Overview

| Component | Description |
|-----------|-------------|
| **Database** | PostgreSQL with 5 tables: book_categories, authors, members, books, loans |
| **Data layer** | DAOs with full CRUD (JDBC + HikariCP) |
| **Business layer** | Services wrapping DAOs; no direct data access from API |
| **Service layer** | REST API (Javalin) exposing all resources and report endpoints |
| **Clients** | Console menu app (Project 1), console API client (Project 2), web UI with full CRUD (Projects 3 & 4) |

---

## Prerequisites

- **Java 11+**
- **Maven 3.6+**
- **PostgreSQL 12+**

---

## Quick Start

```bash
# 1. Create database and load schema + seed data
createdb -U postgres library_management
psql -U postgres -d library_management -f database/schema.sql
psql -U postgres -d library_management -f database/seed_data.sql

# 2. Set credentials (required for app and API)
export DB_HOST=localhost
export DB_NAME=library_management
export DB_USER=postgres
export DB_PASSWORD=your_password
export DB_PORT=5432

# 3. Build
mvn clean compile

# 4a. Run console app (Project 1)
mvn exec:java

# 4b. Run API server + web client (Projects 2, 3 & 4)
./run_project3_client.sh
# Then open http://localhost:7000 (or the port printed by the server)
```

---

## Deployment

**Full step-by-step instructions** (from “Download ZIP” or clone to a running system, including database setup, environment variables, build, and how to verify everything works) are in **[DEPLOYMENT.md](DEPLOYMENT.md)**. Use it for:

- Setting up the database and loading schema/seed data  
- Configuring and building the project  
- Hosting the back end (service, business, and data layers)  
- Hosting the front end (web client)  
- Full system test (create, update, get, delete) and screenshots  

The README sections below summarize each project; **DEPLOYMENT.md** is the single source for deployment and verification.

---

## Project 1: Data Layer & Console App

### Database schema

- **book_categories** — Categories for organizing books  
- **authors** — Authors with biographical info  
- **members** — Library members  
- **books** — Collection (linked to authors and categories)  
- **loans** — Loan records (50+ rows in seed data)

**Relations:** `books` → authors, book_categories; `loans` → members, books. All tables use primary keys, foreign keys, check constraints, and indexes.

### Setup (one-time)

```bash
createdb -U postgres library_management
psql -U postgres -d library_management -f database/schema.sql
psql -U postgres -d library_management -f database/seed_data.sql
```

### Run console application

```bash
mvn exec:java
```

Menu options: display categories, authors, members, books, loans; loans with details (JOINs); loan details; member summary; book popularity; record counts.

### Test data layer only

```bash
mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester
```

### Inspect data

- **PostgreSQL:** `psql -U <user> -d library_management -f database/inspect_data.sql`
- **MySQL:** Use `database/inspect_data_mysql.sql` in MySQL Workbench if using MySQL.

---

## Project 2: Business Layer, API & Console Client

Adds a business layer, REST API, and a console client that exercises the API.

### Run all Project 2 tests

```bash
export DB_PASSWORD=postgres   # if needed
./run_project2_submission.sh
```

Runs: compile → data-layer tester → business-layer tester → start API server → console client → sample GET. Pauses for screenshots; logs go to `.project2-submission-logs/`. See `PROJECT2_SUBMISSION.md` for details.

### Run tests individually

| Layer | Command |
|-------|---------|
| Data | `mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester` |
| Business | `mvn exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester` |
| Service | Start server, then `BASE_URL=http://localhost:7000 mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient` |

### Start API server

```bash
./run_project2_server.sh
# or
mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
```

Server listens on **http://localhost:7000** (or next available port if 7000 is in use). Set `PORT=7001` to force a different port.

### REST API summary

| Resource | GET all | GET one | POST | PUT | DELETE |
|----------|---------|---------|------|-----|--------|
| Categories | `/api/categories` | `/api/categories/:id` | ✓ | ✓ | ✓ |
| Authors | `/api/authors` | `/api/authors/:id` | ✓ | ✓ | ✓ |
| Members | `/api/members` | `/api/members/:id` | ✓ | ✓ | ✓ |
| Books | `/api/books` | `/api/books/:id` | ✓ | ✓ | ✓ |
| Loans | `/api/loans` | `/api/loans/:id` | ✓ | ✓ | ✓ |

**Reports:** `GET /api/loans/with-details`, `GET /api/loans/:id/details`, `GET /api/members/:id/summary`, `GET /api/books/popularity`, `GET /api/records/counts`.

### Hosting

- **Local:** Run the server command above; use `http://localhost:7000` as base URL.
- **Cloud:** Package with `mvn package`, run the JAR with `java -cp target/*.jar edu.csce548.library.api.LibraryServer`, and set `PORT` and `DB_*` in your platform’s environment.

---

## Project 3 & 4: Web Client (Full CRUD)

The web UI (Projects 3 & 4) supports **all** operations:

- **Get:** Get all, get by ID, get subset (e.g. loans by member or status), and report endpoints (loans with details, book popularity, record counts).
- **Create (insert):** Create categories, authors, members, books, and loans from the client.
- **Update:** Load a record by ID, edit fields, and send PUT to update.
- **Delete:** Delete by ID (optional but supported for all resources).

### Run

```bash
./run_project3_client.sh
```

Starts the API server and opens the client in your browser (or go to the URL printed by the server). The server serves the client from the same origin, so no CORS or base-URL setup is needed when using the default port.

**Deployment and testing:** See **[DEPLOYMENT.md](DEPLOYMENT.md)** for full setup and full system test (including screenshots). See **PROJECT3_README.md** for endpoint list and hosting options.

---

## Project Structure

```
CSCE548/
├── database/
│   ├── schema.sql              # PostgreSQL schema
│   ├── seed_data.sql           # Test data (50+ loans)
│   ├── inspect_data.sql       # Inspect current data
│   └── fix_duplicate_authors.sql
├── src/main/java/edu/csce548/library/
│   ├── Main.java               # Console app (Project 1)
│   ├── DatabaseConnection.java
│   ├── DataLayerTester.java
│   ├── BusinessLayerTester.java
│   ├── model/                  # POJOs
│   ├── dao/                    # Data access
│   ├── service/
│   │   └── QueryService.java   # JOINs and reports
│   ├── business/               # Business services (Project 2)
│   ├── api/
│   │   └── LibraryServer.java  # REST API (Project 2)
│   └── client/
│       └── LibraryClient.java  # Console API client (Project 2)
├── src/main/resources/public/  # Web client (Project 3)
│   ├── index.html
│   ├── styles.css
│   └── app.js
├── client-web/                 # Standalone copy of web client
├── run_project2_submission.sh
├── run_project2_server.sh
├── run_project2_client.sh
├── run_project3_client.sh
└── pom.xml
```

---

## Dependencies

| Dependency | Purpose |
|------------|---------|
| PostgreSQL JDBC 42.7.x | Database driver |
| HikariCP 5.1.x | Connection pooling |
| Javalin 6.x | REST API |
| Jackson 2.16.x | JSON + Java 8 date/time |
| SLF4J 2.x | Logging |

---

## Troubleshooting

| Issue | Check |
|-------|--------|
| Connection errors | PostgreSQL running; `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` set; database exists |
| Port in use | Set `PORT=7001` (or another port) when starting the server |
| Compilation | `java -version` (11+), `mvn -version`; run `mvn clean compile` |
| Client "Failed to fetch" | Server running; browser opened to the URL the server prints (same port as API) |

---

*CSCE 548 — Library Management System (Projects 1–4)*
