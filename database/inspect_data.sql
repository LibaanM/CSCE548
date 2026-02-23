-- CSCE 548 - Inspect current data in library_management
-- Run after testers or API client to see what's in the tables.
-- PostgreSQL: psql -U <user> -d library_management -f database/inspect_data.sql

\echo '=== book_categories ==='
SELECT category_id, category_name, LEFT(COALESCE(description, ''), 40) AS description_preview, created_at
FROM book_categories
ORDER BY category_id;

\echo ''
\echo '=== authors ==='
SELECT author_id, first_name, last_name, nationality, birth_date, created_at
FROM authors
ORDER BY author_id;

\echo ''
\echo '=== members ==='
SELECT member_id, first_name, last_name, email, membership_type, membership_date, created_at
FROM members
ORDER BY member_id;

\echo ''
\echo '=== books ==='
SELECT book_id, title, author_id, category_id, total_copies, available_copies, created_at
FROM books
ORDER BY book_id;

\echo ''
\echo '=== loans ==='
SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, status, fine_amount, created_at
FROM loans
ORDER BY loan_id DESC
LIMIT 30;

\echo ''
\echo '=== row counts ==='
SELECT
  (SELECT COUNT(*) FROM book_categories) AS categories,
  (SELECT COUNT(*) FROM authors) AS authors,
  (SELECT COUNT(*) FROM members) AS members,
  (SELECT COUNT(*) FROM books) AS books,
  (SELECT COUNT(*) FROM loans) AS loans;
