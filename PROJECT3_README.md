# CSCE 548 – Project 3: Front End for Library Services

This project provides a **web client** that invokes all GET methods of the Library API (Project 2) for every table and report endpoint.

## What the client does

The client calls **all** of the following:

### Get single record (by ID)

- **Categories:** `GET /api/categories/:id`
- **Authors:** `GET /api/authors/:id`
- **Members:** `GET /api/members/:id`
- **Books:** `GET /api/books/:id`
- **Loans:** `GET /api/loans/:id`

### Get all records

- **Categories:** `GET /api/categories`
- **Authors:** `GET /api/authors`
- **Members:** `GET /api/members`
- **Books:** `GET /api/books`
- **Loans:** `GET /api/loans`

### Get subset of records (where the API supports it)

- **Loans by member:** `GET /api/loans/member/:memberId`
- **Loans by status:** `GET /api/loans/status/:status`

### Reports / query endpoints

- **All loans with details:** `GET /api/loans/with-details`
- **Loan details by ID:** `GET /api/loans/:id/details`
- **Member loan summary:** `GET /api/members/:id/summary`
- **Book popularity stats:** `GET /api/books/popularity`
- **Record counts:** `GET /api/records/counts`

## How to run and host the client

### Option A: Same server (recommended for testing)

The Library API server serves the web client and the API on the same port.

1. **Start PostgreSQL** and set `DB_*` environment variables (same as Project 2), e.g.:
   ```bash
   export DB_PASSWORD=postgres
   ```

2. **Start the server** (from the project root):
   ```bash
   mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
   ```
   Or: `./run_project2_server.sh`

3. **Open in a browser:**  
   **http://localhost:7000** (or http://localhost:PORT if the console shows a different port)

   The root URL redirects to the client. The API base URL in the page defaults to `http://localhost:7000`; you can change it if your server runs on another host/port.

### Option B: Standalone static hosting

If you want to serve only the client from another host (e.g. another port or a static host):

1. Serve the `client-web` folder with any static file server, for example:
   ```bash
   npx serve client-web
   ```
   Or with Python:
   ```bash
   cd client-web && python3 -m http.server 3000
   ```

2. Start the Library API server separately (see Option A, step 2).

3. Open the URL shown by your static server (e.g. http://localhost:3000). Set **API base URL** in the page to your API (e.g. `http://localhost:7000`) and click **Save**.  
   CORS is enabled on the API so the browser can call it from this origin.

## Testing and screenshots

1. Start the API server (and the client as in Option A or B).
2. In the browser:
   - **Get all** for each section (Categories, Authors, Members, Books, Loans) and confirm JSON in the Response area.
   - **Get by ID** for each section using a valid ID (e.g. 1) and confirm a single record.
   - For **Loans**, use **Get by member** (member ID) and **Get by status** (e.g. `active` or `returned`).
   - Under **Reports & queries**, try: **All loans with details**, **Loan details by ID**, **Member loan summary**, **Book popularity stats**, **Record counts**.
3. Take **screenshots** showing:
   - The client UI with at least one “Get all” result (e.g. categories or books).
   - At least one “Get by ID” result.
   - At least one subset or report (e.g. loans by member or record counts).

Include these screenshots in your submission to prove the client works.

## Project layout

| Path | Purpose |
|------|--------|
| `client-web/` | Standalone web client (HTML, CSS, JS) for use with any static server. |
| `src/main/resources/public/` | Same client files, packaged so the Library server can serve them at `/`. |
| `src/main/java/.../api/LibraryServer.java` | CORS enabled; serves static files from `/public` and redirects `/` to the client. |

## Requirements checklist (Project 3)

- [x] Client calls **all** “get” methods: get single, get all, get subset (for all tables that support it).
- [x] Client covers **all** tables: Categories, Authors, Members, Books, Loans, plus report/query endpoints.
- [x] Client can be **hosted** (same server as API or separate static host).
- [ ] **Test** the client and take **screenshots** for your submission.
