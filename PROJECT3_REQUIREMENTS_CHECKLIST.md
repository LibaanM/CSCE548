# Project 3 – Requirements Compliance Checklist

This document maps each assignment requirement to the codebase for submission.

---

## 1. AI-generated client

> **Requirement:** Use ChatGPT (or other AI tool) to generate code for a web, mobile, or desktop client application that invokes the services created in Project 2.

| Item | Status | Location |
|------|--------|----------|
| Web client generated (HTML/CSS/JS) | ✓ | `client-web/index.html`, `client-web/styles.css`, `client-web/app.js` |
| Client invokes Project 2 REST API | ✓ | `app.js` – `apiGet()`, fetch to `/api/*` |

**Status:** Web client is present and calls the Library API.

---

## 2. Call ALL “get” methods for ALL tables

> **Requirement:** At the very least, the client must call ALL “get” methods (get single record, get all records, get some subset of records) for ALL tables in the database.

### Get single record (by ID)

| Table | Endpoint | Client action |
|-------|----------|---------------|
| Categories | GET /api/categories/:id | “Get by ID” in Categories card |
| Authors | GET /api/authors/:id | “Get by ID” in Authors card |
| Members | GET /api/members/:id | “Get by ID” in Members card |
| Books | GET /api/books/:id | “Get by ID” in Books card |
| Loans | GET /api/loans/:id | “Get by ID” in Loans card |

### Get all records

| Table | Endpoint | Client action |
|-------|----------|---------------|
| Categories | GET /api/categories | “Get all” in Categories |
| Authors | GET /api/authors | “Get all” in Authors |
| Members | GET /api/members | “Get all” in Members |
| Books | GET /api/books | “Get all” in Books |
| Loans | GET /api/loans | “Get all” in Loans |

### Get subset of records (where supported)

| Table | Endpoint | Client action |
|-------|----------|---------------|
| Loans | GET /api/loans/member/:memberId | “By member” in Loans card |
| Loans | GET /api/loans/status/:status | “By status” in Loans card |

### Reports / query GET endpoints

| Endpoint | Client action |
|----------|---------------|
| GET /api/loans/with-details | “All loans with details” |
| GET /api/loans/:id/details | “Loan details” (with Loan ID) |
| GET /api/members/:id/summary | “Member summary” (with Member ID) |
| GET /api/books/popularity | “Book popularity” |
| GET /api/records/counts | “Record counts” |

**Status:** All required GET methods for all tables and report endpoints are callable from the client.

---

## 3. Host the client

> **Requirement:** Host the client in an appropriate environment (website, desktop, mobile device, or emulator).

| Option | How | Where documented |
|--------|-----|-------------------|
| Same server | Run `LibraryServer`; open http://localhost:7000 | `PROJECT3_README.md`, `README.md` |
| Static host | `npx serve client-web` or any static server; set API base URL | `PROJECT3_README.md` |
| CORS | API allows cross-origin requests from the client | `LibraryServer.java` (CORS + static files) |

**Status:** Client can be hosted via the API server or any static host.

---

## 4. Test the functionality

> **Requirement:** Test the functionality of the client.

| What to do | How |
|------------|-----|
| Start server | `mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer` (PostgreSQL + DB_* set) |
| Open client | http://localhost:7000 (or port shown in console) |
| Test Get all | Click “Get all” for Categories, Authors, Members, Books, Loans; confirm tables render |
| Test Get by ID | Enter a valid ID (e.g. 1), click “Get by ID” for each resource; confirm one row or one record |
| Test subset | Loans: “By member” with a member ID, “By status” with e.g. `active` |
| Test reports | “All loans with details”, “Loan details” (Loan ID), “Member summary” (Member ID), “Book popularity”, “Record counts” |
| Test errors | Use invalid ID (e.g. 99999) or non-existent loan; confirm server error message is shown |

**Status:** You need to run through these tests before submission.

---

## 5. Screenshots

> **Requirement:** Take screenshots to prove the client works.

| Screenshot | Suggested content |
|------------|--------------------|
| 1. Get all | Client UI with “Get all” for one resource (e.g. Categories) and the result table visible |
| 2. Get by ID | “Get by ID” with an ID and the single-record result |
| 3. Subset or report | e.g. “Loans by member” or “Record counts” or “Loan details” with result visible |
| 4. (Optional) Error | One 404 or error response showing the server error message in the UI |

Include these in your submission (e.g. PDF or doc with captions, or in a `screenshots/` folder referenced in the write-up).

**Status:** You need to take and submit these screenshots.

---

## Summary

| Requirement | Met in code? | Your action |
|-------------|--------------|-------------|
| 1. AI-generated client invoking Project 2 services | ✓ | None |
| 2. All get methods (single, all, subset) for all tables | ✓ | None |
| 3. Host client in appropriate environment | ✓ | None |
| 4. Test functionality | ✓ (client supports it) | Run tests before submitting |
| 5. Screenshots proving client works | — | Take and include screenshots |

**To aim for full credit:** Run the server, exercise the client (get all, get by ID, subset, reports), take the screenshots, and include them in your submission with a short note on how to run and host the client.
