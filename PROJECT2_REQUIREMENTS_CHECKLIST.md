# Project 2 – Requirements Compliance Checklist

This document maps each assignment requirement to the codebase.

---

## 1. Business layer (AI-generated)

> **Requirement:** Use ChatGPT (or other AI tool) to generate code for a business layer. All methods in the data layer (CRUD operations) must be made available through the business layer, but they can be named however you wish.

| Data layer (DAO / QueryService) | Business layer (exposed as) | Location |
|---------------------------------|----------------------------|----------|
| **BookCategoryDAO:** create, read, readAll, update, delete | addCategory, getCategoryById, getAllCategories, updateCategory, removeCategory | `business/BookCategoryBusinessService.java` |
| **AuthorDAO:** create, read, readAll, update, delete | addAuthor, getAuthorById, getAllAuthors, updateAuthor, removeAuthor | `business/AuthorBusinessService.java` |
| **MemberDAO:** create, read, readAll, update, delete | addMember, getMemberById, getAllMembers, updateMember, removeMember | `business/MemberBusinessService.java` |
| **BookDAO:** create, read, readAll, update, delete | addBook, getBookById, getAllBooks, updateBook, removeBook | `business/BookBusinessService.java` |
| **LoanDAO:** create, read, readAll, readByMember, readByStatus, update, delete | addLoan, getLoanById, getAllLoans, getLoansByMember, getLoansByStatus, updateLoan, removeLoan | `business/LoanBusinessService.java` |
| **QueryService:** getLoanDetails, getAllLoansWithDetails, getMemberLoanSummary, getBookPopularityStats, getAllRecordsCount | getLoanDetails, getAllLoansWithDetails, getMemberLoanSummary, getBookPopularityStats, getAllRecordCounts | `business/LibraryQueryBusinessService.java` |

**Status:** All data-layer methods are exposed through the business layer with custom names.

---

## 2. Service layer (AI-generated)

> **Requirement:** Use ChatGPT (or other AI tool) to generate code for a series of services or microservices that invoke the business layer. All methods of the business layer must be made available through the service layer, but they can be named however you wish.

| Business method | Service (REST) | Endpoint(s) |
|-----------------|----------------|-------------|
| Category CRUD + getAll | REST API | GET/POST /api/categories, GET/PUT/DELETE /api/categories/:id |
| Author CRUD + getAll | REST API | GET/POST /api/authors, GET/PUT/DELETE /api/authors/:id |
| Member CRUD + getAll | REST API | GET/POST /api/members, GET/PUT/DELETE /api/members/:id |
| Book CRUD + getAll | REST API | GET/POST /api/books, GET/PUT/DELETE /api/books/:id |
| Loan CRUD + getAll + by member + by status | REST API | GET/POST /api/loans, GET/PUT/DELETE /api/loans/:id, GET /api/loans/member/:id, GET /api/loans/status/:status |
| getLoanDetails, getAllLoansWithDetails, getMemberLoanSummary, getBookPopularityStats, getAllRecordCounts | REST API | GET /api/loans/:id/details, GET /api/loans/with-details, GET /api/members/:id/summary, GET /api/books/popularity, GET /api/records/counts |

**Status:** All business-layer methods are exposed via the REST service in `api/LibraryServer.java`.

---

## 3. Host services + comments

> **Requirement:** Host those services using a platform of your choosing. Add comments to your code, detailing how to host the service and on which platform.

| Item | Where |
|------|--------|
| How to run locally | `LibraryServer.java` class Javadoc (top of file): "How to run (local)", port 7000, DB_* env vars |
| Platform options | Same Javadoc: "Platform options: You can deploy the packaged JAR to Railway, Render, Heroku, or any JVM host" |
| Env vars for hosting | Same: DB_HOST, DB_NAME, DB_USER, DB_PASSWORD, DB_PORT; PORT for cloud |
| README hosting section | `README.md` – "Hosting the Service" (local + cloud note) |

**Status:** Hosting and platform are described in code comments and in the README.

---

## 4. Console front end to test services

> **Requirement:** Create a simple console-based front end that invokes the services to test them. For example, insert a new object, update that object, and then delete the object. At each step, you can call the Get method. Those 4 steps would prove the full functionality of the services.

| Step | Console client behavior | File |
|------|--------------------------|------|
| Insert | POST /api/categories (create category) | `client/LibraryClient.java` |
| Get after insert | GET /api/categories/:id | same |
| Update | PUT /api/categories/:id | same |
| Get after update | GET /api/categories/:id | same |
| Delete | DELETE /api/categories/:id | same |
| Get after delete | GET /api/categories/:id (expect 404) | same |

**Status:** The console client does insert → get → update → get → delete → get (404), which covers and exceeds the requested 4-step proof.

---

## 5. Test thoroughly + screenshots

> **Requirement:** Test all generated code thoroughly. Take screenshots to demonstrate service execution and data retrieval.

| Item | Where |
|------|--------|
| Data layer tests | `DataLayerTester.java`; run: `mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester` |
| Business layer tests | `BusinessLayerTester.java`; run: `mvn exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester` |
| Service execution | Run server + `LibraryClient`; screenshot server and client output |
| Data retrieval | Screenshot GET /api/categories (or similar) or `database/inspect_data.sql` output |
| Screenshot guide | `PROJECT2_SUBMISSION.md` and `run_project2_submission.sh` |

**Status:** Testers exist for all layers; submission script and doc describe what to run and what to screenshot.

---

## 6. Check in to GitHub

> **Requirement:** Check in the code to your Github repo.

**Status:** You need to commit and push (e.g. `git add .`, `git commit -m "Project 2 complete"`, `git push origin main`).

---

## Summary

| Requirement | Met in code? |
|-------------|----------------|
| 1. Business layer, all data-layer methods exposed | Yes |
| 2. Service layer, all business-layer methods exposed | Yes |
| 3. Host services + comments in code | Yes |
| 4. Console front end (insert, get, update, delete, get) | Yes |
| 5. Test thoroughly + screenshots | Yes (testers + guide; you take screenshots) |
| 6. Check in to Github | You do the push |

The program satisfies the described requirements; completing screenshots and pushing to GitHub finishes the submission.
