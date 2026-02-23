package edu.csce548.library;

import edu.csce548.library.business.*;
import edu.csce548.library.model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Console tester for the business layer.
 * Calls business services (not DAOs or REST API). Exercises the same CRUD flow as DataLayerTester
 * but through the business layer, so it verifies business → data layer integration.
 *
 * Prerequisite: PostgreSQL running, DB_* env vars set, database and schema created.
 *
 * Run: mvn exec:java -Dexec.mainClass=edu.csce548.library.BusinessLayerTester
 */
public class BusinessLayerTester {
    private static int passed = 0;
    private static int failed = 0;

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

        System.out.println("=== Business Layer Tester ===\n");

        try {
            DatabaseConnection.initialize(host, database, user, password, port);

            testBookCategoryBusiness();
            testAuthorBusiness();
            testMemberBusiness();
            testBookBusiness();
            testLoanBusiness();
            testQueryBusiness();

            System.out.println("\n--- Summary ---");
            System.out.println("Passed: " + passed + ", Failed: " + failed);
            if (failed > 0) System.exit(1);
        } catch (Exception e) {
            System.err.println("Fatal: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            DatabaseConnection.close();
        }
    }

    private static void ok(String msg) {
        passed++;
        System.out.println("  OK: " + msg);
    }

    private static void fail(String msg) {
        failed++;
        System.out.println("  FAIL: " + msg);
    }

    private static void testBookCategoryBusiness() throws SQLException {
        System.out.println("--- BookCategoryBusinessService ---");
        BookCategoryBusinessService svc = new BookCategoryBusinessService();

        BookCategory c = new BookCategory("BL-Test-Cat", "Business layer test");
        BookCategory created = svc.addCategory(c);
        if (created == null || created.getCategoryId() == null) {
            fail("addCategory");
        } else {
            ok("addCategory -> id=" + created.getCategoryId());
        }

        BookCategory read = svc.getCategoryById(created.getCategoryId());
        if (read == null || !"BL-Test-Cat".equals(read.getCategoryName())) {
            fail("getCategoryById after add");
        } else {
            ok("getCategoryById");
        }

        created.setCategoryName("BL-Test-Updated");
        svc.updateCategory(created);
        read = svc.getCategoryById(created.getCategoryId());
        if (read == null || !"BL-Test-Updated".equals(read.getCategoryName())) {
            fail("updateCategory");
        } else {
            ok("updateCategory");
        }

        boolean removed = svc.removeCategory(created.getCategoryId());
        if (!removed) fail("removeCategory"); else ok("removeCategory");
        if (svc.getCategoryById(created.getCategoryId()) != null) fail("get after remove"); else ok("get after remove -> null");

        List<BookCategory> all = svc.getAllCategories();
        if (all == null) fail("getAllCategories"); else ok("getAllCategories -> " + all.size());
    }

    private static void testAuthorBusiness() throws SQLException {
        System.out.println("\n--- AuthorBusinessService ---");
        AuthorBusinessService svc = new AuthorBusinessService();

        Author a = new Author("BusinessLayer", "Tester", LocalDate.of(1990, 1, 1), "US", "Test");
        Author created = svc.addAuthor(a);
        if (created == null || created.getAuthorId() == null) {
            fail("addAuthor");
        } else {
            ok("addAuthor -> id=" + created.getAuthorId());
        }

        if (svc.getAuthorById(created.getAuthorId()) == null) fail("getAuthorById"); else ok("getAuthorById");
        created.setLastName("Updated");
        svc.updateAuthor(created);
        if (svc.getAuthorById(created.getAuthorId()) == null || !"Updated".equals(svc.getAuthorById(created.getAuthorId()).getLastName())) fail("updateAuthor"); else ok("updateAuthor");
        svc.removeAuthor(created.getAuthorId());
        if (svc.getAuthorById(created.getAuthorId()) != null) fail("removeAuthor"); else ok("removeAuthor");

        List<Author> all = svc.getAllAuthors();
        if (all == null) fail("getAllAuthors"); else ok("getAllAuthors -> " + all.size());
    }

    private static void testMemberBusiness() throws SQLException {
        System.out.println("\n--- MemberBusinessService ---");
        MemberBusinessService svc = new MemberBusinessService();

        Member m = new Member("BL", "Tester", "bltest@test.local", null, null, LocalDate.now(), "Standard");
        Member created = svc.addMember(m);
        if (created == null || created.getMemberId() == null) {
            fail("addMember");
        } else {
            ok("addMember -> id=" + created.getMemberId());
        }

        if (svc.getMemberById(created.getMemberId()) == null) fail("getMemberById"); else ok("getMemberById");
        created.setEmail("blupdated@test.local");
        svc.updateMember(created);
        if (svc.getMemberById(created.getMemberId()) == null || !svc.getMemberById(created.getMemberId()).getEmail().contains("blupdated")) fail("updateMember"); else ok("updateMember");
        svc.removeMember(created.getMemberId());
        if (svc.getMemberById(created.getMemberId()) != null) fail("removeMember"); else ok("removeMember");

        List<Member> all = svc.getAllMembers();
        if (all == null) fail("getAllMembers"); else ok("getAllMembers -> " + all.size());
    }

    private static void testBookBusiness() throws SQLException {
        System.out.println("\n--- BookBusinessService ---");
        BookCategoryBusinessService catSvc = new BookCategoryBusinessService();
        AuthorBusinessService authorSvc = new AuthorBusinessService();
        BookBusinessService svc = new BookBusinessService();

        List<BookCategory> cats = catSvc.getAllCategories();
        List<Author> authors = authorSvc.getAllAuthors();
        if (cats.isEmpty() || authors.isEmpty()) {
            System.out.println("  Skip full CRUD (need existing category and author). Testing getAllBooks only.");
            List<Book> all = svc.getAllBooks();
            if (all != null) ok("getAllBooks -> " + all.size()); else fail("getAllBooks");
            return;
        }

        Book b = new Book("BL Test Book", authors.get(0).getAuthorId(), cats.get(0).getCategoryId(), null, 2024, null, 1, 1, null);
        Book created = svc.addBook(b);
        if (created == null || created.getBookId() == null) {
            fail("addBook");
        } else {
            ok("addBook -> id=" + created.getBookId());
        }

        if (svc.getBookById(created.getBookId()) == null) fail("getBookById"); else ok("getBookById");
        created.setTitle("BL Test Updated");
        svc.updateBook(created);
        if (svc.getBookById(created.getBookId()) == null || !"BL Test Updated".equals(svc.getBookById(created.getBookId()).getTitle())) fail("updateBook"); else ok("updateBook");
        svc.removeBook(created.getBookId());
        if (svc.getBookById(created.getBookId()) != null) fail("removeBook"); else ok("removeBook");

        List<Book> all = svc.getAllBooks();
        if (all == null) fail("getAllBooks"); else ok("getAllBooks -> " + all.size());
    }

    private static void testLoanBusiness() throws SQLException {
        System.out.println("\n--- LoanBusinessService ---");
        LoanBusinessService svc = new LoanBusinessService();

        List<Loan> all = svc.getAllLoans();
        if (all != null) ok("getAllLoans -> " + all.size()); else fail("getAllLoans");
        if (!all.isEmpty()) {
            Loan one = svc.getLoanById(all.get(0).getLoanId());
            if (one != null) ok("getLoanById"); else fail("getLoanById");
        }
    }

    private static void testQueryBusiness() throws SQLException {
        System.out.println("\n--- LibraryQueryBusinessService ---");
        LibraryQueryBusinessService svc = new LibraryQueryBusinessService();

        List<Map<String, Object>> loansWithDetails = svc.getAllLoansWithDetails();
        if (loansWithDetails != null) ok("getAllLoansWithDetails -> " + loansWithDetails.size()); else fail("getAllLoansWithDetails");

        Map<String, Long> counts = svc.getAllRecordCounts();
        if (counts != null && counts.containsKey("books_count")) ok("getAllRecordCounts"); else fail("getAllRecordCounts");

        List<Map<String, Object>> popularity = svc.getBookPopularityStats();
        if (popularity != null) ok("getBookPopularityStats -> " + popularity.size()); else fail("getBookPopularityStats");
    }
}
