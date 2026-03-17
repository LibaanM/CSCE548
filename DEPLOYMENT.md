# Library Management System — Deployment Guide

This document explains how to go from a copy of the repository to a fully running system: database, backend (data, business, and service layers), and front end (web client). It is written for someone who has never seen the project before.

---

## 1. Prerequisites

Install and have available:

| Prerequisite | Purpose | How to check |
|--------------|---------|--------------|
| **Java 11 or higher** | Run backend and API | `java -version` |
| **Maven 3.6+** | Build and run the project | `mvn -version` |
| **PostgreSQL 12+** | Database for all data | `psql --version` (optional); ensure the server is running |

**Optional:** A code IDE (e.g. IntelliJ, VS Code) for editing; a browser for the web client.

**Settings:** No special IDE settings are required. The project uses Maven; point your IDE at the project root and use the existing `pom.xml`.

---

## 2. Get the Code

**Option A — Clone (if you use Git):**

```bash
git clone <repository-url>
cd CSCE548
```

**Option B — Download ZIP:**

1. On GitHub, click **Code** → **Download ZIP**.
2. Unzip the file (e.g. to `CSCE548` or `CSCE548-main`).
3. Open a terminal and go into that folder:
   ```bash
   cd /path/to/CSCE548
   ```

---

## 3. Configure the Database

### 3.1 Create the database

Using a PostgreSQL user that can create databases (e.g. `postgres`):

```bash
createdb -U postgres library_management
```

Or inside `psql`:

```sql
CREATE DATABASE library_management;
\q
```

### 3.2 Create tables and load seed data

From the **project root** (the folder that contains `database/` and `pom.xml`):

```bash
psql -U postgres -d library_management -f database/schema.sql
psql -U postgres -d library_management -f database/seed_data.sql
```

Replace `postgres` with your PostgreSQL username if different.

### 3.3 Verify the database

```bash
psql -U postgres -d library_management -f verify_data.sql
```

You should see counts and sample data (e.g. 50+ loans). If you see errors, re-run the schema and seed steps.

---

## 4. Set Environment Variables

The application and API server read database settings from the environment. Set these **before** building or running (e.g. in your terminal or in your IDE run configuration):

| Variable | Meaning | Example |
|----------|---------|---------|
| `DB_HOST` | Database host | `localhost` |
| `DB_NAME` | Database name | `library_management` |
| `DB_USER` | PostgreSQL user | `postgres` (or your user) |
| `DB_PASSWORD` | User password | Your password (leave unset if none) |
| `DB_PORT` | PostgreSQL port | `5432` |

**Example (Unix-like shell):**

```bash
export DB_HOST=localhost
export DB_NAME=library_management
export DB_USER=postgres
export DB_PASSWORD=your_password
export DB_PORT=5432
```

On Windows (Command Prompt): use `set DB_NAME=library_management` etc.  
On Windows (PowerShell): use `$env:DB_NAME="library_management"` etc.

---

## 5. Build the Project

From the **project root**:

```bash
mvn clean compile
```

You should see `BUILD SUCCESS`. If you see compilation errors, fix them (or check Java and Maven versions).

---

## 6. Host the Back End (Service, Business, and Data Layers)

The back end is a single process: the **API server**. It hosts the service layer (REST API), which uses the business layer, which uses the data layer (DAOs) and the database.

### 6.1 Start the API server

From the project root, with `DB_*` variables set:

```bash
mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
```

Or use the helper script:

```bash
./run_project3_client.sh
```

The server will:

- Bind to port **7000** (or the next free port if 7000 is in use).
- Print the URL in the console, e.g. `http://localhost:7000`.
- Serve the **web client** at that same URL (same origin), so the browser can call the API without CORS issues.

Leave this terminal running.

### 6.2 Check that the back end is up

- Open a browser and go to: `http://localhost:7000` (or the port printed). You should see the Library API Client page.
- Or run: `curl -s -o /dev/null -w "%{http_code}" http://localhost:7000/api/categories`  
  You should see `200`.

If the server fails to start, check:

- PostgreSQL is running.
- `DB_*` variables are set correctly.
- Port 7000 (or the one you set via `PORT`) is not in use.

---

## 7. Host the Front End (Client)

The front end is **served by the same API server** when you run `LibraryServer`. There is no separate client server for local use.

- **Local:** Open `http://localhost:7000` (or the URL printed by the server). The page loads the API base URL from the current origin, so no extra configuration is needed.
- **Standalone client (e.g. for a different host):** The files in `client-web/` (and under `src/main/resources/public/`) can be served by any static file server (e.g. `npx serve client-web`). In that case, set the “API base URL” in the client UI to the URL of your deployed API (e.g. `https://your-api.railway.app`).

---

## 8. Verify End-to-End (Full System Test)

1. **Back end and client:** Server running; browser at `http://localhost:7000`.
2. **Get data:** In the client, use “Get all” for Categories (or any resource). You should see a table of records.
3. **Create:** In Categories, fill “Create” (e.g. Name: `Test`, Description: `Test category`), click **Create**. You should see a 201 response and the new record (with an ID).
4. **Database check:** In another terminal:
   ```bash
   psql -U postgres -d library_management -c "SELECT * FROM book_categories ORDER BY category_id DESC LIMIT 3;"
   ```
   You should see the new row.
5. **Update:** In the client, under “Update,” enter the new category’s ID, click **Load**, change the name/description, click **Update**. Confirm the response and check the same row in the database again.
6. **Get one:** Use “Get by ID” with that ID; you should see the updated record.
7. **Delete (optional):** Under “Delete,” enter the ID and click **Delete**. Confirm 204 or success, then run the same `SELECT` in `psql` and confirm the row is gone.

Repeating similar steps for Authors, Members, Books, and Loans (and the Reports & queries section) completes a full system test. Take screenshots of the client and of database query results for your submission.

---

## 9. Hosting on a Platform (Optional)

- **Back end:** Build with `mvn package`, then run the JAR:
  ```bash
  java -cp target/*.jar edu.csce548.library.api.LibraryServer
  ```
  Set `PORT` and all `DB_*` variables in the platform’s environment. Point the platform’s “web” process to this command.
- **Database:** Use a hosted PostgreSQL (e.g. Railway, Render, Neon) and set `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, and `DB_PORT` to that instance.
- **Front end:** Either let the same server serve the client (as locally) or host the contents of `client-web/` (or `src/main/resources/public/`) on a static host and set the client’s “API base URL” to your deployed API URL.

---

## 10. Summary Checklist

- [ ] Prerequisites installed (Java 11+, Maven 3.6+, PostgreSQL 12+).
- [ ] Code obtained (clone or download ZIP) and opened in project root.
- [ ] Database created; schema and seed data loaded; `verify_data.sql` succeeds.
- [ ] `DB_*` environment variables set.
- [ ] `mvn clean compile` succeeds.
- [ ] API server started (`mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer` or `./run_project3_client.sh`).
- [ ] Browser opens `http://localhost:<port>` and shows the client.
- [ ] “Get all” and “Get by ID” work for at least one resource.
- [ ] Create (insert) works; new row visible in database.
- [ ] Update works; change visible in database and in “Get by ID.”
- [ ] Delete works (optional); row removed in database.

If all of the above pass, the system is deployed and ready for full system testing and screenshots.
