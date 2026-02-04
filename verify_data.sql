-- Verification script to check data counts
-- This script verifies that we have 50+ rows in the database

SELECT 
    'Book Categories' as table_name, 
    COUNT(*) as row_count 
FROM book_categories
UNION ALL
SELECT 
    'Authors', 
    COUNT(*) 
FROM authors
UNION ALL
SELECT 
    'Members', 
    COUNT(*) 
FROM members
UNION ALL
SELECT 
    'Books', 
    COUNT(*) 
FROM books
UNION ALL
SELECT 
    'Loans', 
    COUNT(*) 
FROM loans
ORDER BY table_name;

-- Show total loans count (should be 50+)
SELECT 
    COUNT(*) as total_loans,
    CASE 
        WHEN COUNT(*) >= 50 THEN '✓ Requirement met (50+ rows)'
        ELSE '✗ Requirement not met (less than 50 rows)'
    END as status
FROM loans;

-- Show sample loans
SELECT 
    loan_id,
    member_id,
    book_id,
    loan_date,
    due_date,
    status
FROM loans
ORDER BY loan_id
LIMIT 10;
