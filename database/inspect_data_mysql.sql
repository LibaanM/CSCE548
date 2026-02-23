-- CSCE 548 - Inspect current data (MySQL)
-- Run in MySQL Workbench or: mysql -u <user> -p library_management < database/inspect_data_mysql.sql

SELECT '=== book_categories ===' AS '';
SELECT category_id, category_name, LEFT(COALESCE(description, ''), 40) AS description_preview, created_at
FROM book_categories
ORDER BY category_id;

SELECT '=== authors ===' AS '';
SELECT author_id, first_name, last_name, nationality, birth_date, created_at
FROM authors
ORDER BY author_id;

SELECT '=== members ===' AS '';
SELECT member_id, first_name, last_name, email, membership_type, membership_date, created_at
FROM members
ORDER BY member_id;

SELECT '=== books ===' AS '';
SELECT book_id, title, author_id, category_id, total_copies, available_copies, created_at
FROM books
ORDER BY book_id;

SELECT '=== loans (latest 30) ===' AS '';
SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, status, fine_amount, created_at
FROM loans
ORDER BY loan_id DESC
LIMIT 30;

SELECT '=== row counts ===' AS '';
SELECT
  (SELECT COUNT(*) FROM book_categories) AS categories,
  (SELECT COUNT(*) FROM authors) AS authors,
  (SELECT COUNT(*) FROM members) AS members,
  (SELECT COUNT(*) FROM books) AS books,
  (SELECT COUNT(*) FROM loans) AS loans;
