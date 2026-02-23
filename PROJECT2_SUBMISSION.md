# Project 2 – Submission Checklist & Screenshots Guide

Use this to **test all generated code thoroughly** and **take screenshots** for your submission.

---

## 1. What to Run (Test Order)

Do these with **PostgreSQL running** and **DB_PASSWORD** (and any other DB_* vars) set.

### A. Data layer

```bash
cd /Users/libaanmohamed/CSCE548
mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester
```

**Screenshot 1:** Terminal showing `=== Data Layer Tester (DAOs) ===`, all `OK` lines, and **Summary: Passed: X, Failed: 0**.

---

### B. Business layer

```bash
mvn exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester
```

**Screenshot 2:** Terminal showing `=== Business Layer Tester ===`, all `OK` lines, and **Summary: Passed: X, Failed: 0**.

---

### C. Service layer (server + client)

**Terminal 1 – start server:**

```bash
./run_project2_server.sh
```

Wait until you see: `Library API server running at http://localhost:XXXX`

**Screenshot 3:** Terminal showing the server started (the “Library API server running…” line and no errors).

**Terminal 2 – run client:**

If the server said a port other than 7000, use it, e.g.:

```bash
BASE_URL=http://localhost:7001 ./run_project2_client.sh
```

Otherwise:

```bash
./run_project2_client.sh
```

**Screenshot 4:** Terminal showing the client output: “Testing Book Category CRUD”, all 6 steps (Create, Get, Update, Get, Delete, Get 404), and **“All steps completed successfully.”**

---

### D. Data retrieval (optional but good for “data retrieval”)

**Option 1 – API in browser or curl**

With the server still running:

- Browser: open `http://localhost:7000/api/categories` (or the port you used).
- Or: `curl http://localhost:7000/api/categories`

**Screenshot 5:** Browser or terminal showing JSON list of categories (or books/loans).

**Option 2 – Inspect database**

```bash
psql -U <your_user> -d library_management -f database/inspect_data.sql
```

**Screenshot 5 (alt):** Terminal showing the result of `inspect_data.sql` (tables and row counts).

---

## 2. Screenshot Summary for Submission

| # | What it shows |
|---|----------------|
| 1 | Data layer tester – all tests passed |
| 2 | Business layer tester – all tests passed |
| 3 | Service layer – server running |
| 4 | Service layer – client CRUD test (all steps, “All steps completed successfully”) |
| 5 | Data retrieval – API response (e.g. /api/categories) or DB inspect script output |

Name files clearly, e.g. `screenshot1_data_layer.png`, `screenshot2_business_layer.png`, etc., and add them to your report or submission doc.

---

## 3. Assignment Requirements vs This Repo

| Requirement | Status | Where |
|-------------|--------|--------|
| Business layer – all DAO CRUD available | Done | `business/*BusinessService.java` |
| Service layer – all business methods available | Done | `api/LibraryServer.java` |
| Host services on a platform + comments on how to host | Done | Comments in `LibraryServer.java`; README “Hosting the Service”. Local = run server; cloud = deploy JAR, set PORT and DB_* |
| Console front end that invokes services (insert, update, delete, get) | Done | `client/LibraryClient.java` – Create → Get → Update → Get → Delete → Get 404 |
| Test all generated code thoroughly | Done | DataLayerTester, BusinessLayerTester, LibraryClient |
| Screenshots for service execution and data retrieval | You do | Follow Section 1–2 above |
| Check in code to GitHub | You do | `git add …`, `git commit`, `git push` |

---

## 4. Ready for 100%?

- **Code:** Yes – business layer, service layer, hosting comments, and console client are in place and tested.
- **Your part:**  
  1. Run the tests above and take the 5 screenshots.  
  2. Push the latest code (and, if required, the screenshot files or a report that includes them) to GitHub.  
  3. If the rubric requires the app to be **deployed** (e.g. Railway/Render), do that and add one screenshot of the live URL; otherwise, local run + README/hosting comments is usually enough.

After you run the tests, take the screenshots, and push to GitHub (and deploy if required), the project is ready for full submission.
