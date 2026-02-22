package edu.csce548.library;

import edu.csce548.library.dao.*;
import edu.csce548.library.model.*;
import edu.csce548.library.service.QueryService;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BookCategoryDAO categoryDAO = new BookCategoryDAO();
    private static AuthorDAO authorDAO = new AuthorDAO();
    private static MemberDAO memberDAO = new MemberDAO();
    private static BookDAO bookDAO = new BookDAO();
    private static LoanDAO loanDAO = new LoanDAO();
    private static QueryService queryService = new QueryService();
    
    public static void main(String[] args) {
        String host = System.getenv("DB_HOST");
        if (host == null) host = "localhost";
        
        String database = System.getenv("DB_NAME");
        if (database == null) database = "library_management";
        
        String user = System.getenv("DB_USER");
        if (user == null) user = System.getProperty("user.name");
        
        String password = System.getenv("DB_PASSWORD");
        
        String portStr = System.getenv("DB_PORT");
        int port = (portStr != null) ? Integer.parseInt(portStr) : 5432;
        
        try {
            System.out.println("Initializing database connection...");
            DatabaseConnection.initialize(host, database, user, password, port);
            System.out.println("Connected to database successfully!");
            
            while (true) {
                showMenu();
                String choice = scanner.nextLine().trim();
                
                if (choice.equals("0")) {
                    System.out.println("\nExiting application. Goodbye!");
                    break;
                }
                
                try {
                    switch (choice) {
                        case "1": displayAllBookCategories(); break;
                        case "2": displayAllAuthors(); break;
                        case "3": displayAllMembers(); break;
                        case "4": displayAllBooks(); break;
                        case "5": displayAllLoans(); break;
                        case "6": displayLoansWithDetails(); break;
                        case "7": displayLoanDetails(); break;
                        case "8": displayMemberSummary(); break;
                        case "9": displayBookPopularityStats(); break;
                        case "10": displayRecordCounts(); break;
                        case "11": manageBookCategories(); break;
                        case "12": manageAuthors(); break;
                        case "13": manageMembers(); break;
                        case "14": manageBooks(); break;
                        case "15": manageLoans(); break;
                        default: System.out.println("\nInvalid choice. Please try again.");
                    }
                } catch (Exception e) {
                    System.out.println("\nError: " + e.getMessage());
                    e.printStackTrace();
                }
                
                if (!choice.equals("0")) {
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                }
            }
        } catch (Exception e) {
            System.err.println("\nFatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            DatabaseConnection.close();
        }
    }
    
    private static void printSeparator() {
        System.out.println("\n" + "=".repeat(80) + "\n");
    }
    
    private static void printHeader(String title) {
        printSeparator();
        System.out.println("  " + title);
        printSeparator();
    }
    
    private static void showMenu() {
        printSeparator();
        System.out.println("  LIBRARY MANAGEMENT SYSTEM - MAIN MENU");
        printSeparator();
        System.out.println("1.  Display all book categories");
        System.out.println("2.  Display all authors");
        System.out.println("3.  Display all members");
        System.out.println("4.  Display all books");
        System.out.println("5.  Display all loans");
        System.out.println("6.  Display loans with details (joins)");
        System.out.println("7.  Display detailed loan information");
        System.out.println("8.  Display member loan summary");
        System.out.println("9.  Display book popularity statistics");
        System.out.println("10. Display database record counts");
        System.out.println("11. Manage book categories (CRUD)");
        System.out.println("12. Manage authors (CRUD)");
        System.out.println("13. Manage members (CRUD)");
        System.out.println("14. Manage books (CRUD)");
        System.out.println("15. Manage loans (CRUD)");
        System.out.println("0.  Exit");
        printSeparator();
        System.out.print("Enter your choice: ");
    }
    
    private static void displayAllBookCategories() {
        printHeader("ALL BOOK CATEGORIES");
        try {
            List<BookCategory> categories = categoryDAO.readAll();
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }
            System.out.printf("%-5s %-25s %-50s%n", "ID", "Category Name", "Description");
            System.out.println("-".repeat(85));
            for (BookCategory cat : categories) {
                String desc = (cat.getDescription() != null && cat.getDescription().length() > 50) 
                    ? cat.getDescription().substring(0, 47) + "..." 
                    : (cat.getDescription() != null ? cat.getDescription() : "N/A");
                System.out.printf("%-5d %-25s %-50s%n", cat.getCategoryId(), cat.getCategoryName(), desc);
            }
            System.out.println("\nTotal categories: " + categories.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayAllAuthors() {
        printHeader("ALL AUTHORS");
        try {
            List<Author> authors = authorDAO.readAll();
            if (authors.isEmpty()) {
                System.out.println("No authors found.");
                return;
            }
            System.out.printf("%-5s %-30s %-15s %-12s %-30s%n", "ID", "Name", "Nationality", "Birth Date", "Biography");
            System.out.println("-".repeat(100));
            for (Author author : authors) {
                String name = author.getFirstName() + " " + author.getLastName();
                String nationality = (author.getNationality() != null) ? author.getNationality() : "N/A";
                String birthDate = (author.getBirthDate() != null) ? author.getBirthDate().toString() : "N/A";
                String bio = (author.getBiography() != null && author.getBiography().length() > 30) 
                    ? author.getBiography().substring(0, 27) + "..." 
                    : (author.getBiography() != null ? author.getBiography() : "N/A");
                System.out.printf("%-5d %-30s %-15s %-12s %-30s%n", author.getAuthorId(), name, nationality, birthDate, bio);
            }
            System.out.println("\nTotal authors: " + authors.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayAllMembers() {
        printHeader("ALL MEMBERS");
        try {
            List<Member> members = memberDAO.readAll();
            if (members.isEmpty()) {
                System.out.println("No members found.");
                return;
            }
            System.out.printf("%-5s %-25s %-30s %-15s %-15s%n", "ID", "Name", "Email", "Phone", "Membership Type");
            System.out.println("-".repeat(95));
            for (Member member : members) {
                String name = member.getFirstName() + " " + member.getLastName();
                String phone = (member.getPhone() != null) ? member.getPhone() : "N/A";
                System.out.printf("%-5d %-25s %-30s %-15s %-15s%n",
                    member.getMemberId(), name, member.getEmail(), phone, member.getMembershipType());
            }
            System.out.println("\nTotal members: " + members.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayAllBooks() {
        printHeader("ALL BOOKS");
        try {
            List<Book> books = bookDAO.readAll();
            if (books.isEmpty()) {
                System.out.println("No books found.");
                return;
            }
            System.out.printf("%-5s %-40s %-12s %-12s %-8s %-8s%n", "ID", "Title", "Author ID", "Category ID", "Total", "Available");
            System.out.println("-".repeat(95));
            for (Book book : books) {
                String title = (book.getTitle().length() > 40) ? book.getTitle().substring(0, 37) + "..." : book.getTitle();
                System.out.printf("%-5d %-40s %-12d %-12d %-8d %-8d%n",
                    book.getBookId(), title, book.getAuthorId(), book.getCategoryId(), 
                    book.getTotalCopies(), book.getAvailableCopies());
            }
            System.out.println("\nTotal books: " + books.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayAllLoans() {
        printHeader("ALL LOANS");
        try {
            List<Loan> loans = loanDAO.readAll();
            if (loans.isEmpty()) {
                System.out.println("No loans found.");
                return;
            }
            System.out.printf("%-5s %-10s %-10s %-12s %-12s %-12s %-10s %-15s%n", "ID", "Member ID", "Book ID", "Loan Date", "Due Date", "Return Date", "Fine", "Status");
            System.out.println("-".repeat(100));
            for (Loan loan : loans) {
                String returnDate = (loan.getReturnDate() != null) ? loan.getReturnDate().toString() : "N/A";
                String fine = (loan.getFineAmount() != null) ? String.format("$%.2f", loan.getFineAmount()) : "$0.00";
                System.out.printf("%-5d %-10d %-10d %-12s %-12s %-12s %-10s %-15s%n",
                    loan.getLoanId(), loan.getMemberId(), loan.getBookId(), 
                    loan.getLoanDate().toString(), loan.getDueDate().toString(), returnDate, fine, loan.getStatus());
            }
            System.out.println("\nTotal loans: " + loans.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayLoansWithDetails() {
        printHeader("LOANS WITH DETAILS");
        try {
            List<Map<String, Object>> loans = queryService.getAllLoansWithDetails();
            if (loans.isEmpty()) {
                System.out.println("No loans found.");
                return;
            }
            System.out.printf("%-8s %-12s %-12s %-25s %-40s %-25s %-10s%n", "Loan ID", "Loan Date", "Due Date", "Member", "Book Title", "Author", "Status");
            System.out.println("-".repeat(135));
            for (Map<String, Object> loan : loans) {
                String bookTitle = loan.get("book_title").toString();
                if (bookTitle.length() > 40) bookTitle = bookTitle.substring(0, 37) + "...";
                System.out.printf("%-8d %-12s %-12s %-25s %-40s %-25s %-10s%n",
                    loan.get("loan_id"), ((Date)loan.get("loan_date")).toString(), 
                    ((Date)loan.get("due_date")).toString(), loan.get("member_name"), 
                    bookTitle, loan.get("author_name"), loan.get("status"));
            }
            System.out.println("\nTotal loans: " + loans.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayLoanDetails() {
        printHeader("LOAN DETAILS");
        try {
            System.out.print("Enter loan ID: ");
            String input = scanner.nextLine().trim();
            int loanId = Integer.parseInt(input);
            
            QueryService.LoanDetails details = queryService.getLoanDetails(loanId);
            if (details == null) {
                System.out.println("Loan " + loanId + " not found.");
                return;
            }
            
            System.out.println("\nLoan Information:");
            System.out.println("  Loan ID: " + details.loan.get("loan_id"));
            System.out.println("  Loan Date: " + details.loan.get("loan_date"));
            System.out.println("  Due Date: " + details.loan.get("due_date"));
            System.out.println("  Return Date: " + (details.loan.get("return_date") != null ? details.loan.get("return_date") : "Not returned"));
            System.out.println("  Fine Amount: " + (details.loan.get("fine_amount") != null ? "$" + details.loan.get("fine_amount") : "$0.00"));
            System.out.println("  Status: " + details.loan.get("status"));
            System.out.println("  Notes: " + (details.loan.get("notes") != null ? details.loan.get("notes") : "N/A"));
            
            System.out.println("\nMember Information:");
            System.out.println("  Name: " + details.member.get("first_name") + " " + details.member.get("last_name"));
            System.out.println("  Email: " + details.member.get("email"));
            System.out.println("  Membership Type: " + details.member.get("membership_type"));
            
            System.out.println("\nBook Information:");
            System.out.println("  Title: " + details.book.get("title"));
            System.out.println("  ISBN: " + (details.book.get("isbn") != null ? details.book.get("isbn") : "N/A"));
            System.out.println("  Publication Year: " + (details.book.get("publication_year") != null ? details.book.get("publication_year") : "N/A"));
            System.out.println("  Available Copies: " + details.book.get("available_copies"));
            
            System.out.println("\nAuthor Information:");
            System.out.println("  Name: " + details.author.get("first_name") + " " + details.author.get("last_name"));
            
            System.out.println("\nCategory Information:");
            System.out.println("  Category: " + details.category.get("category_name"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid loan ID.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayMemberSummary() {
        printHeader("MEMBER LOAN SUMMARY");
        try {
            System.out.print("Enter member ID: ");
            String input = scanner.nextLine().trim();
            int memberId = Integer.parseInt(input);
            
            Map<String, Object> summary = queryService.getMemberLoanSummary(memberId);
            if (summary == null) {
                System.out.println("Member " + memberId + " not found.");
                return;
            }
            
            System.out.println("\nMember: " + summary.get("first_name") + " " + summary.get("last_name"));
            System.out.println("Email: " + summary.get("email"));
            System.out.println("Membership Type: " + summary.get("membership_type"));
            System.out.println("Total Loans: " + summary.get("total_loans"));
            System.out.println("Active Loans: " + summary.get("active_loans"));
            System.out.println("Returned Loans: " + summary.get("returned_loans"));
            System.out.println("Total Fines: " + (summary.get("total_fines") != null ? "$" + summary.get("total_fines") : "$0.00"));
            System.out.println("First Loan Date: " + (summary.get("first_loan_date") != null ? summary.get("first_loan_date") : "N/A"));
            System.out.println("Last Loan Date: " + (summary.get("last_loan_date") != null ? summary.get("last_loan_date") : "N/A"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid member ID.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayBookPopularityStats() {
        printHeader("BOOK POPULARITY STATISTICS");
        try {
            List<Map<String, Object>> stats = queryService.getBookPopularityStats();
            if (stats.isEmpty()) {
                System.out.println("No book statistics found.");
                return;
            }
            System.out.printf("%-40s %-30s %-20s %-8s %-8s %-12s %-15s%n", "Book Title", "Author", "Category", "Available", "Total", "Loan Count", "Unique Borrowers");
            System.out.println("-".repeat(140));
            for (Map<String, Object> stat : stats) {
                String title = stat.get("title").toString();
                if (title.length() > 40) title = title.substring(0, 37) + "...";
                String author = stat.get("author_name").toString();
                if (author.length() > 30) author = author.substring(0, 27) + "...";
                System.out.printf("%-40s %-30s %-20s %-8d %-8d %-12d %-15d%n",
                    title, author, stat.get("category_name"), 
                    stat.get("available_copies"), stat.get("total_copies"),
                    stat.get("loan_count"), stat.get("unique_borrowers"));
            }
            System.out.println("\nTotal books: " + stats.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void displayRecordCounts() {
        printHeader("DATABASE RECORD COUNTS");
        try {
            Map<String, Long> counts = queryService.getAllRecordsCount();
            System.out.println("Book Categories: " + counts.get("categories_count"));
            System.out.println("Authors: " + counts.get("authors_count"));
            System.out.println("Members: " + counts.get("members_count"));
            System.out.println("Books: " + counts.get("books_count"));
            System.out.println("Loans: " + counts.get("loans_count"));
            long total = counts.values().stream().mapToLong(Long::longValue).sum();
            System.out.println("\nTotal Records: " + total);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- CRUD: Book Categories ----------
    private static void manageBookCategories() {
        while (true) {
            printHeader("MANAGE BOOK CATEGORIES");
            System.out.println("1. List all categories");
            System.out.println("2. Add category (Create)");
            System.out.println("3. Update category");
            System.out.println("4. Delete category");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            try {
                if ("0".equals(ch)) break;
                if ("1".equals(ch)) { displayAllBookCategories(); continue; }
                if ("2".equals(ch)) { createBookCategory(); continue; }
                if ("3".equals(ch)) { updateBookCategory(); continue; }
                if ("4".equals(ch)) { deleteBookCategory(); continue; }
                System.out.println("Invalid choice.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createBookCategory() throws SQLException {
        System.out.print("Category name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) { System.out.println("Name required."); return; }
        System.out.print("Description (optional): ");
        String desc = scanner.nextLine().trim();
        BookCategory cat = new BookCategory(name, desc.isEmpty() ? null : desc);
        BookCategory created = categoryDAO.create(cat);
        System.out.println("Created category ID: " + created.getCategoryId());
    }

    private static void updateBookCategory() throws SQLException {
        System.out.print("Category ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        BookCategory existing = categoryDAO.read(id);
        if (existing == null) { System.out.println("Category not found."); return; }
        System.out.print("New name [current: " + existing.getCategoryName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) existing.setCategoryName(name);
        System.out.print("New description (optional): ");
        String desc = scanner.nextLine().trim();
        existing.setDescription(desc.isEmpty() ? null : desc);
        categoryDAO.update(existing);
        System.out.println("Category updated.");
    }

    private static void deleteBookCategory() throws SQLException {
        System.out.print("Category ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = categoryDAO.delete(id);
        System.out.println(ok ? "Category deleted." : "Category not found or in use.");
    }

    // ---------- CRUD: Authors ----------
    private static void manageAuthors() {
        while (true) {
            printHeader("MANAGE AUTHORS");
            System.out.println("1. List all authors");
            System.out.println("2. Add author (Create)");
            System.out.println("3. Update author");
            System.out.println("4. Delete author");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            try {
                if ("0".equals(ch)) break;
                if ("1".equals(ch)) { displayAllAuthors(); continue; }
                if ("2".equals(ch)) { createAuthor(); continue; }
                if ("3".equals(ch)) { updateAuthor(); continue; }
                if ("4".equals(ch)) { deleteAuthor(); continue; }
                System.out.println("Invalid choice.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createAuthor() throws SQLException {
        System.out.print("First name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String last = scanner.nextLine().trim();
        if (first.isEmpty() || last.isEmpty()) { System.out.println("First and last name required."); return; }
        System.out.print("Birth date (YYYY-MM-DD, optional): ");
        String bd = scanner.nextLine().trim();
        LocalDate birthDate = bd.isEmpty() ? null : LocalDate.parse(bd);
        System.out.print("Nationality (optional): ");
        String nat = scanner.nextLine().trim();
        System.out.print("Biography (optional): ");
        String bio = scanner.nextLine().trim();
        Author a = new Author(first, last, birthDate, nat.isEmpty() ? null : nat, bio.isEmpty() ? null : bio);
        Author created = authorDAO.create(a);
        System.out.println("Created author ID: " + created.getAuthorId());
    }

    private static void updateAuthor() throws SQLException {
        System.out.print("Author ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Author existing = authorDAO.read(id);
        if (existing == null) { System.out.println("Author not found."); return; }
        System.out.print("First name [current: " + existing.getFirstName() + "]: ");
        String first = scanner.nextLine().trim();
        if (!first.isEmpty()) existing.setFirstName(first);
        System.out.print("Last name [current: " + existing.getLastName() + "]: ");
        String last = scanner.nextLine().trim();
        if (!last.isEmpty()) existing.setLastName(last);
        System.out.print("Birth date YYYY-MM-DD (optional, blank to keep): ");
        String bd = scanner.nextLine().trim();
        if (!bd.isEmpty()) existing.setBirthDate(LocalDate.parse(bd));
        System.out.print("Nationality (optional): ");
        existing.setNationality(scanner.nextLine().trim());
        System.out.print("Biography (optional): ");
        existing.setBiography(scanner.nextLine().trim());
        authorDAO.update(existing);
        System.out.println("Author updated.");
    }

    private static void deleteAuthor() throws SQLException {
        System.out.print("Author ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = authorDAO.delete(id);
        System.out.println(ok ? "Author deleted." : "Author not found or has books.");
    }

    // ---------- CRUD: Members ----------
    private static void manageMembers() {
        while (true) {
            printHeader("MANAGE MEMBERS");
            System.out.println("1. List all members");
            System.out.println("2. Add member (Create)");
            System.out.println("3. Update member");
            System.out.println("4. Delete member");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            try {
                if ("0".equals(ch)) break;
                if ("1".equals(ch)) { displayAllMembers(); continue; }
                if ("2".equals(ch)) { createMember(); continue; }
                if ("3".equals(ch)) { updateMember(); continue; }
                if ("4".equals(ch)) { deleteMember(); continue; }
                System.out.println("Invalid choice.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createMember() throws SQLException {
        System.out.print("First name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String last = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (first.isEmpty() || last.isEmpty() || email.isEmpty()) { System.out.println("First name, last name, and email required."); return; }
        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine().trim();
        System.out.print("Address (optional): ");
        String address = scanner.nextLine().trim();
        System.out.print("Membership date (YYYY-MM-DD): ");
        LocalDate memDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Membership type [Standard]: ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) type = "Standard";
        Member m = new Member(first, last, email, phone.isEmpty() ? null : phone, address.isEmpty() ? null : address, memDate, type);
        Member created = memberDAO.create(m);
        System.out.println("Created member ID: " + created.getMemberId());
    }

    private static void updateMember() throws SQLException {
        System.out.print("Member ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Member existing = memberDAO.read(id);
        if (existing == null) { System.out.println("Member not found."); return; }
        System.out.print("First name [current: " + existing.getFirstName() + "]: ");
        String first = scanner.nextLine().trim();
        if (!first.isEmpty()) existing.setFirstName(first);
        System.out.print("Last name [current: " + existing.getLastName() + "]: ");
        String last = scanner.nextLine().trim();
        if (!last.isEmpty()) existing.setLastName(last);
        System.out.print("Email [current: " + existing.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) existing.setEmail(email);
        System.out.print("Phone (optional): ");
        existing.setPhone(scanner.nextLine().trim());
        System.out.print("Address (optional): ");
        existing.setAddress(scanner.nextLine().trim());
        System.out.print("Membership type [current: " + existing.getMembershipType() + "]: ");
        String type = scanner.nextLine().trim();
        if (!type.isEmpty()) existing.setMembershipType(type);
        memberDAO.update(existing);
        System.out.println("Member updated.");
    }

    private static void deleteMember() throws SQLException {
        System.out.print("Member ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = memberDAO.delete(id);
        System.out.println(ok ? "Member deleted." : "Member not found or has loans.");
    }

    // ---------- CRUD: Books ----------
    private static void manageBooks() {
        while (true) {
            printHeader("MANAGE BOOKS");
            System.out.println("1. List all books");
            System.out.println("2. Add book (Create)");
            System.out.println("3. Update book");
            System.out.println("4. Delete book");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            try {
                if ("0".equals(ch)) break;
                if ("1".equals(ch)) { displayAllBooks(); continue; }
                if ("2".equals(ch)) { createBook(); continue; }
                if ("3".equals(ch)) { updateBook(); continue; }
                if ("4".equals(ch)) { deleteBook(); continue; }
                System.out.println("Invalid choice.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createBook() throws SQLException {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) { System.out.println("Title required."); return; }
        System.out.print("Author ID: ");
        int authorId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("ISBN (optional): ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Publication year (optional): ");
        String py = scanner.nextLine().trim();
        Integer pubYear = py.isEmpty() ? null : Integer.parseInt(py);
        System.out.print("Publisher (optional): ");
        String publisher = scanner.nextLine().trim();
        System.out.print("Total copies [1]: ");
        String tc = scanner.nextLine().trim();
        int total = tc.isEmpty() ? 1 : Integer.parseInt(tc);
        System.out.print("Description (optional): ");
        String desc = scanner.nextLine().trim();
        Book b = new Book(title, authorId, categoryId, isbn.isEmpty() ? null : isbn, pubYear, publisher.isEmpty() ? null : publisher, total, total, desc.isEmpty() ? null : desc);
        Book created = bookDAO.create(b);
        System.out.println("Created book ID: " + created.getBookId());
    }

    private static void updateBook() throws SQLException {
        System.out.print("Book ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Book existing = bookDAO.read(id);
        if (existing == null) { System.out.println("Book not found."); return; }
        System.out.print("Title [current: " + existing.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) existing.setTitle(title);
        System.out.print("Author ID [current: " + existing.getAuthorId() + "]: ");
        String aid = scanner.nextLine().trim();
        if (!aid.isEmpty()) existing.setAuthorId(Integer.parseInt(aid));
        System.out.print("Category ID [current: " + existing.getCategoryId() + "]: ");
        String cid = scanner.nextLine().trim();
        if (!cid.isEmpty()) existing.setCategoryId(Integer.parseInt(cid));
        System.out.print("Total copies [current: " + existing.getTotalCopies() + "]: ");
        String tc = scanner.nextLine().trim();
        if (!tc.isEmpty()) existing.setTotalCopies(Integer.parseInt(tc));
        System.out.print("Available copies [current: " + existing.getAvailableCopies() + "]: ");
        String ac = scanner.nextLine().trim();
        if (!ac.isEmpty()) existing.setAvailableCopies(Integer.parseInt(ac));
        bookDAO.update(existing);
        System.out.println("Book updated.");
    }

    private static void deleteBook() throws SQLException {
        System.out.print("Book ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = bookDAO.delete(id);
        System.out.println(ok ? "Book deleted." : "Book not found or has loans.");
    }

    // ---------- CRUD: Loans ----------
    private static void manageLoans() {
        while (true) {
            printHeader("MANAGE LOANS");
            System.out.println("1. List all loans");
            System.out.println("2. Add loan (Create)");
            System.out.println("3. Update loan (e.g. return book)");
            System.out.println("4. Delete loan");
            System.out.println("0. Back to main menu");
            System.out.print("Choice: ");
            String ch = scanner.nextLine().trim();
            try {
                if ("0".equals(ch)) break;
                if ("1".equals(ch)) { displayAllLoans(); continue; }
                if ("2".equals(ch)) { createLoan(); continue; }
                if ("3".equals(ch)) { updateLoan(); continue; }
                if ("4".equals(ch)) { deleteLoan(); continue; }
                System.out.println("Invalid choice.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createLoan() throws SQLException {
        System.out.print("Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Book ID: ");
        int bookId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Loan date (YYYY-MM-DD): ");
        LocalDate loanDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Due date (YYYY-MM-DD): ");
        LocalDate dueDate = LocalDate.parse(scanner.nextLine().trim());
        System.out.print("Status [Active]: ");
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) status = "Active";
        Loan loan = new Loan(memberId, bookId, loanDate, dueDate, null, BigDecimal.ZERO, status, null);
        Loan created = loanDAO.create(loan);
        System.out.println("Created loan ID: " + created.getLoanId());
    }

    private static void updateLoan() throws SQLException {
        System.out.print("Loan ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        Loan existing = loanDAO.read(id);
        if (existing == null) { System.out.println("Loan not found."); return; }
        System.out.print("Return date YYYY-MM-DD (optional, for return): ");
        String rd = scanner.nextLine().trim();
        if (!rd.isEmpty()) existing.setReturnDate(LocalDate.parse(rd));
        System.out.print("Fine amount (optional): ");
        String fine = scanner.nextLine().trim();
        if (!fine.isEmpty()) existing.setFineAmount(new BigDecimal(fine));
        System.out.print("Status [current: " + existing.getStatus() + "]: ");
        String status = scanner.nextLine().trim();
        if (!status.isEmpty()) existing.setStatus(status);
        System.out.print("Notes (optional): ");
        existing.setNotes(scanner.nextLine().trim());
        loanDAO.update(existing);
        System.out.println("Loan updated.");
    }

    private static void deleteLoan() throws SQLException {
        System.out.print("Loan ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean ok = loanDAO.delete(id);
        System.out.println(ok ? "Loan deleted." : "Loan not found.");
    }
}

