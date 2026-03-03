-- Fix duplicate authors (same first_name, last_name, birth_date) by keeping one author_id
-- and pointing all books to that author, then deleting the duplicate author rows.
-- Run this if you see the same author with different author_ids (e.g. Maya Angelou as 10 and 20).
--
-- Usage: psql -d library_management -f fix_duplicate_authors.sql

BEGIN;

-- 1. Point all books that reference a "duplicate" author to the canonical author (smallest id in the group).
WITH dup_groups AS (
  SELECT first_name, last_name, birth_date, MIN(author_id) AS keep_id
  FROM authors
  GROUP BY first_name, last_name, birth_date
  HAVING COUNT(*) > 1
),
mapping AS (
  SELECT a.author_id AS old_id, d.keep_id AS new_id
  FROM authors a
  JOIN dup_groups d
    ON a.first_name = d.first_name AND a.last_name = d.last_name AND a.birth_date = d.birth_date
  WHERE a.author_id <> d.keep_id
)
UPDATE books b
SET author_id = m.new_id
FROM mapping m
WHERE b.author_id = m.old_id;

-- 2. Delete the duplicate author rows (we kept the one with the smallest author_id).
WITH dup_groups AS (
  SELECT first_name, last_name, birth_date, MIN(author_id) AS keep_id
  FROM authors
  GROUP BY first_name, last_name, birth_date
  HAVING COUNT(*) > 1
),
to_remove AS (
  SELECT a.author_id
  FROM authors a
  JOIN dup_groups d
    ON a.first_name = d.first_name AND a.last_name = d.last_name AND a.birth_date = d.birth_date
  WHERE a.author_id <> d.keep_id
)
DELETE FROM authors
WHERE author_id IN (SELECT author_id FROM to_remove);

COMMIT;

-- Optional: add a unique constraint so the same author can't be inserted twice.
-- Run this after the fix above if you want to prevent future duplicates:
-- ALTER TABLE authors ADD CONSTRAINT uq_author_identity UNIQUE (first_name, last_name, birth_date);
