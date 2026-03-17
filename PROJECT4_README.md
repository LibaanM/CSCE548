# CSCE 548 — Project 4: Full System Test & Deployment

Project 4 completes the AI-generated n-tier application with **full system testing** and **deployment documentation**. All functionality from Projects 1–3 must work to receive full credit.

---

## What’s Included

### 1. Full CRUD in the Web Client

The web client now supports **insert, update, get, and delete** for all main resources:

| Resource   | Create (POST) | Update (PUT) | Get (GET) | Delete (optional) |
|-----------|----------------|--------------|-----------|--------------------|
| Categories | ✓             | ✓            | ✓         | ✓                 |
| Authors   | ✓             | ✓            | ✓         | ✓                 |
| Members   | ✓             | ✓            | ✓         | ✓                 |
| Books     | ✓             | ✓            | ✓         | ✓                 |
| Loans     | ✓             | ✓            | ✓         | ✓                 |

- **Create:** Fill the “Create” fields in each card and click **Create**.
- **Update:** Enter an ID in “Update,” click **Load** to fetch the record, edit the fields, then click **Update**.
- **Delete:** Enter an ID in “Delete” and click **Delete**.

Reports & queries (loans with details, book popularity, record counts, etc.) are unchanged and still available.

### 2. Deployment Document

**[DEPLOYMENT.md](DEPLOYMENT.md)** is the full deployment guide. It covers:

- Prerequisites (Java, Maven, PostgreSQL) and how to check them  
- Getting the code (clone or download ZIP)  
- Creating the database and loading schema + seed data  
- Setting `DB_*` environment variables  
- Building with Maven  
- Hosting the back end (single API server that hosts service, business, and data layers)  
- Hosting the front end (served by the same server locally)  
- **Full system test:** create → verify in DB → update → verify → get by ID → delete → verify  
- Optional platform hosting  
- A short checklist to confirm “it works”

Use **DEPLOYMENT.md** to go from “Download ZIP” to “cool, that works!”

### 3. GitHub README

The main **[README.md](README.md)** has been updated with:

- Table of contents including **Deployment** and **Project 3 & 4**
- A **Deployment** section that points to DEPLOYMENT.md for full instructions
- Overview of the web client’s full CRUD (create, update, get, delete)
- Quick Start, project summaries, and troubleshooting

---

## Running a Full System Test (Screenshots)

1. **Start the system**  
   Follow [DEPLOYMENT.md](DEPLOYMENT.md) through §6 so the API server is running and you can open the client at `http://localhost:<port>`.

2. **Create**  
   In the client, create a new item (e.g. a category or author). Take a **screenshot of the client** showing the success response and a **screenshot of the database** (e.g. `psql -c "SELECT * FROM book_categories ORDER BY category_id DESC LIMIT 1;"`) showing the new row.

3. **Update**  
   Use “Update” (Load → edit → Update) for that item. Take a **screenshot of the client** and a **screenshot of the database** showing the updated row.

4. **Get**  
   Use “Get all” and “Get by ID” for that resource. Take **screenshots of the client** showing the results.

5. **Delete (optional)**  
   Use “Delete” for that item. Take a **screenshot of the client** and a **screenshot of the database** (or query results) showing the row is gone.

Repeat for other resources (authors, members, books, loans) as needed to demonstrate full functionality. DEPLOYMENT.md §8 gives a compact verification flow.

---

## Quick Commands

```bash
# Database (one-time)
createdb -U postgres library_management
psql -U postgres -d library_management -f database/schema.sql
psql -U postgres -d library_management -f database/seed_data.sql

# Environment
export DB_HOST=localhost DB_NAME=library_management DB_USER=postgres DB_PASSWORD=your_password DB_PORT=5432

# Build & run API + web client
mvn clean compile
./run_project3_client.sh
# Open the URL printed (e.g. http://localhost:7000)
```

---

*Project 4 — Full system test and deployment (CSCE 548)*
