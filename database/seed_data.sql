-- CSCE 548 - Project 1
-- Test Data for Library Management System Database
-- This script inserts 50+ rows of test data

-- Insert book categories
INSERT INTO book_categories (category_name, description) VALUES
('Fiction', 'Novels and fictional stories'),
('Non-Fiction', 'Factual and informational books'),
('Science Fiction', 'Speculative fiction with scientific elements'),
('Mystery', 'Detective stories and crime novels'),
('Biography', 'Life stories of real people'),
('History', 'Historical accounts and analysis'),
('Technology', 'Books about computers, programming, and tech'),
('Philosophy', 'Works on fundamental questions and ideas');

-- Insert authors
INSERT INTO authors (first_name, last_name, birth_date, nationality, biography) VALUES
('George', 'Orwell', '1903-06-25', 'British', 'English novelist and essayist, known for 1984 and Animal Farm'),
('J.K.', 'Rowling', '1965-07-31', 'British', 'Author of the Harry Potter series'),
('Isaac', 'Asimov', '1920-01-02', 'American', 'Science fiction writer and professor of biochemistry'),
('Agatha', 'Christie', '1890-09-15', 'British', 'Mystery writer, creator of Hercule Poirot'),
('Stephen', 'King', '1947-09-21', 'American', 'Horror and suspense novelist'),
('Jane', 'Austen', '1775-12-16', 'British', 'Novelist known for social commentary'),
('Ernest', 'Hemingway', '1899-07-21', 'American', 'Nobel Prize-winning novelist and journalist'),
('Mark', 'Twain', '1835-11-30', 'American', 'Humorist and novelist, author of Huckleberry Finn'),
('Toni', 'Morrison', '1931-02-18', 'American', 'Nobel Prize-winning novelist and editor'),
('Maya', 'Angelou', '1928-04-04', 'American', 'Poet, memoirist, and civil rights activist');

-- Insert members
INSERT INTO members (first_name, last_name, email, phone, address, membership_date, membership_type) VALUES
('Alice', 'Johnson', 'alice.johnson@email.com', '555-0101', '123 Main St, City, State', '2023-01-15', 'Premium'),
('Bob', 'Smith', 'bob.smith@email.com', '555-0102', '456 Oak Ave, City, State', '2023-02-20', 'Standard'),
('Carol', 'Williams', 'carol.williams@email.com', '555-0103', '789 Pine Rd, City, State', '2023-03-10', 'Standard'),
('David', 'Brown', 'david.brown@email.com', '555-0104', '321 Elm St, City, State', '2023-04-05', 'Premium'),
('Emma', 'Davis', 'emma.davis@email.com', '555-0105', '654 Maple Dr, City, State', '2023-05-12', 'Standard'),
('Frank', 'Miller', 'frank.miller@email.com', '555-0106', '987 Cedar Ln, City, State', '2023-06-18', 'Standard'),
('Grace', 'Wilson', 'grace.wilson@email.com', '555-0107', '147 Birch Way, City, State', '2023-07-22', 'Premium'),
('Henry', 'Moore', 'henry.moore@email.com', '555-0108', '258 Spruce Ct, City, State', '2023-08-30', 'Standard');

-- Insert books
INSERT INTO books (title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description) VALUES
('1984', 1, 1, '978-0-452-28423-4', 1949, 'Secker & Warburg', 5, 3, 'Dystopian novel about totalitarian surveillance'),
('Animal Farm', 1, 1, '978-0-452-28424-1', 1945, 'Secker & Warburg', 4, 2, 'Allegorical novella about farm animals'),
('Harry Potter and the Philosopher''s Stone', 2, 1, '978-0-7475-3269-6', 1997, 'Bloomsbury', 8, 5, 'First book in the Harry Potter series'),
('Harry Potter and the Chamber of Secrets', 2, 1, '978-0-7475-3849-0', 1998, 'Bloomsbury', 7, 4, 'Second book in the Harry Potter series'),
('Foundation', 3, 3, '978-0-553-29335-7', 1951, 'Gnome Press', 6, 3, 'First book in the Foundation series'),
('I, Robot', 3, 3, '978-0-553-29438-5', 1950, 'Gnome Press', 5, 2, 'Collection of science fiction short stories'),
('Murder on the Orient Express', 4, 4, '978-0-06-269366-2', 1934, 'Collins Crime Club', 4, 1, 'Hercule Poirot mystery novel'),
('The Murder of Roger Ackroyd', 4, 4, '978-0-06-207402-8', 1926, 'Collins Crime Club', 3, 2, 'Famous mystery novel'),
('The Shining', 5, 1, '978-0-385-12167-5', 1977, 'Doubleday', 5, 3, 'Horror novel about a haunted hotel'),
('It', 5, 1, '978-0-670-81302-5', 1986, 'Viking Press', 6, 4, 'Horror novel about a shape-shifting entity'),
('Pride and Prejudice', 6, 1, '978-0-14-143951-8', 1813, 'T. Egerton', 4, 2, 'Romantic novel of manners'),
('Sense and Sensibility', 6, 1, '978-0-14-143966-2', 1811, 'T. Egerton', 3, 1, 'Novel about two sisters'),
('The Old Man and the Sea', 7, 1, '978-0-684-80122-3', 1952, 'Scribner', 5, 3, 'Short novel about an aging fisherman'),
('For Whom the Bell Tolls', 7, 1, '978-0-684-83048-3', 1940, 'Scribner', 4, 2, 'Novel set during the Spanish Civil War'),
('The Adventures of Huckleberry Finn', 8, 1, '978-0-14-243717-9', 1884, 'Chatto & Windus', 5, 3, 'Adventure novel about a boy and escaped slave'),
('The Adventures of Tom Sawyer', 8, 1, '978-0-14-303956-3', 1876, 'American Publishing Company', 4, 2, 'Novel about a young boy growing up'),
('Beloved', 9, 1, '978-1-4000-3341-3', 1987, 'Alfred A. Knopf', 4, 2, 'Novel about slavery and its aftermath'),
('The Bluest Eye', 9, 1, '978-0-375-41128-3', 1970, 'Holt, Rinehart and Winston', 3, 1, 'Novel about a young African-American girl'),
('I Know Why the Caged Bird Sings', 10, 2, '978-0-345-40071-5', 1969, 'Random House', 5, 3, 'Autobiography about growing up in the South');

-- Insert loans (this will give us 50+ total rows)
INSERT INTO loans (member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes) VALUES
-- Member 1 (Alice) loans
(1, 1, '2024-01-10', '2024-01-24', '2024-01-22', 0.00, 'Returned', 'Returned on time'),
(1, 3, '2024-01-15', '2024-01-29', NULL, 0.00, 'Active', 'Currently reading'),
(1, 5, '2024-02-01', '2024-02-15', '2024-02-14', 0.00, 'Returned', 'Enjoyed the book'),
(1, 7, '2024-02-20', '2024-03-06', NULL, 0.00, 'Active', 'Mystery novel'),
-- Member 2 (Bob) loans
(2, 2, '2024-01-12', '2024-01-26', '2024-01-25', 0.00, 'Returned', 'Good read'),
(2, 4, '2024-01-28', '2024-02-11', '2024-02-10', 0.00, 'Returned', 'Loved it'),
(2, 6, '2024-02-05', '2024-02-19', NULL, 0.00, 'Active', 'Science fiction'),
(2, 9, '2024-02-15', '2024-03-01', NULL, 2.50, 'Overdue', 'Late return'),
(2, 11, '2024-03-01', '2024-03-15', NULL, 0.00, 'Active', 'Classic literature'),
-- Member 3 (Carol) loans
(3, 1, '2024-01-20', '2024-02-03', '2024-02-02', 0.00, 'Returned', 'Thought-provoking'),
(3, 8, '2024-02-10', '2024-02-24', '2024-02-23', 0.00, 'Returned', 'Great mystery'),
(3, 12, '2024-02-25', '2024-03-11', NULL, 0.00, 'Active', 'Jane Austen novel'),
(3, 15, '2024-03-05', '2024-03-19', NULL, 0.00, 'Active', 'Adventure story'),
-- Member 4 (David) loans
(4, 3, '2024-01-18', '2024-02-01', '2024-01-31', 0.00, 'Returned', 'Fantasy favorite'),
(4, 5, '2024-02-08', '2024-02-22', '2024-02-21', 0.00, 'Returned', 'Sci-fi classic'),
(4, 10, '2024-02-18', '2024-03-04', NULL, 0.00, 'Active', 'Horror novel'),
(4, 13, '2024-03-02', '2024-03-16', NULL, 0.00, 'Active', 'Hemingway classic'),
(4, 17, '2024-03-08', '2024-03-22', NULL, 0.00, 'Active', 'Toni Morrison'),
-- Member 5 (Emma) loans
(5, 4, '2024-01-25', '2024-02-08', '2024-02-07', 0.00, 'Returned', 'Harry Potter series'),
(5, 7, '2024-02-12', '2024-02-26', '2024-02-25', 0.00, 'Returned', 'Mystery solved'),
(5, 11, '2024-02-28', '2024-03-14', NULL, 0.00, 'Active', 'Pride and Prejudice'),
(5, 14, '2024-03-06', '2024-03-20', NULL, 0.00, 'Active', 'Spanish Civil War'),
(5, 18, '2024-03-10', '2024-03-24', NULL, 0.00, 'Active', 'The Bluest Eye'),
-- Member 6 (Frank) loans
(6, 2, '2024-01-30', '2024-02-13', '2024-02-12', 0.00, 'Returned', 'Animal Farm'),
(6, 6, '2024-02-15', '2024-03-01', NULL, 1.25, 'Overdue', 'Overdue by 2 days'),
(6, 9, '2024-02-25', '2024-03-11', NULL, 0.00, 'Active', 'The Shining'),
(6, 12, '2024-03-03', '2024-03-17', NULL, 0.00, 'Active', 'Sense and Sensibility'),
(6, 16, '2024-03-09', '2024-03-23', NULL, 0.00, 'Active', 'Tom Sawyer'),
-- Member 7 (Grace) loans
(7, 1, '2024-02-05', '2024-02-19', '2024-02-18', 0.00, 'Returned', '1984'),
(7, 5, '2024-02-20', '2024-03-06', NULL, 0.00, 'Active', 'Foundation'),
(7, 8, '2024-03-01', '2024-03-15', NULL, 0.00, 'Active', 'Murder mystery'),
(7, 13, '2024-03-07', '2024-03-21', NULL, 0.00, 'Active', 'Old Man and the Sea'),
(7, 19, '2024-03-11', '2024-03-25', NULL, 0.00, 'Active', 'Maya Angelou'),
-- Member 8 (Henry) loans
(8, 3, '2024-02-10', '2024-02-24', '2024-02-23', 0.00, 'Returned', 'Harry Potter'),
(8, 7, '2024-02-22', '2024-03-08', NULL, 0.00, 'Active', 'Orient Express'),
(8, 10, '2024-03-04', '2024-03-18', NULL, 0.00, 'Active', 'It'),
(8, 15, '2024-03-08', '2024-03-22', NULL, 0.00, 'Active', 'Huckleberry Finn'),
(8, 19, '2024-03-12', '2024-03-26', NULL, 0.00, 'Active', 'Maya Angelou memoir'),
-- Additional loans to reach 50+
(1, 9, '2024-03-01', '2024-03-15', NULL, 0.00, 'Active', 'Horror genre'),
(2, 13, '2024-03-05', '2024-03-19', NULL, 0.00, 'Active', 'Hemingway'),
(3, 6, '2024-03-08', '2024-03-22', NULL, 0.00, 'Active', 'I Robot'),
(4, 1, '2024-03-10', '2024-03-24', NULL, 0.00, 'Active', '1984 again'),
(5, 5, '2024-03-12', '2024-03-26', NULL, 0.00, 'Active', 'Foundation series'),
(6, 3, '2024-03-14', '2024-03-28', NULL, 0.00, 'Active', 'Harry Potter'),
(7, 11, '2024-03-15', '2024-03-29', NULL, 0.00, 'Active', 'Pride and Prejudice'),
(8, 2, '2024-03-16', '2024-03-30', NULL, 0.00, 'Active', 'Animal Farm'),
(1, 14, '2024-03-18', '2024-04-01', NULL, 0.00, 'Active', 'For Whom the Bell Tolls'),
(2, 16, '2024-03-19', '2024-04-02', NULL, 0.00, 'Active', 'Tom Sawyer'),
(3, 19, '2024-03-20', '2024-04-03', NULL, 0.00, 'Active', 'Maya Angelou memoir'),
(4, 4, '2024-03-21', '2024-04-04', NULL, 0.00, 'Active', 'Chamber of Secrets'),
(5, 8, '2024-03-22', '2024-04-05', NULL, 0.00, 'Active', 'Roger Ackroyd'),
(6, 17, '2024-03-23', '2024-04-06', NULL, 0.00, 'Active', 'Beloved'),
(7, 18, '2024-03-24', '2024-04-07', NULL, 0.00, 'Active', 'The Bluest Eye'),
(8, 19, '2024-03-25', '2024-04-08', NULL, 0.00, 'Active', 'Maya Angelou');
