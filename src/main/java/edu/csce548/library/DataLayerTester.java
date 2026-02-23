package edu.csce548.library;

import edu.csce548.library.dao.*;
import edu.csce548.library.model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Simple console tester for the data layer (DAOs).
 * Exercises CRUD on the database directly without the business or service layer.
 *
 * Prerequisite: PostgreSQL running, DB_* env vars set (or defaults), database and schema created.
 *
 * Run: mvn exec:java -Dexec.mainClass=edu.csce548.library.DataLayerTester
 */
public class DataLayerTester {
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

        System.out.println("=== Data Layer Tester (DAOs) ===\n");

        try {
            DatabaseConnection.initialize(host, database, user, password, port);

            testBookCategoryDAO();
            testAuthorDAO();
            testMemberDAO();
            testBookDAO();
            testLoanDAO();

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

    private static void testBookCategoryDAO() throws SQLException {
        System.out.println("--- BookCategoryDAO ---");
        BookCategoryDAO dao = new BookCategoryDAO();

        BookCategory c = new BookCategory("DL-Test-Cat", "Data layer test");
        BookCategory created = dao.create(c);
        if (created == null || created.getCategoryId() == null) {
            fail("create returned null or no id");
        } else {
            ok("create -> id=" + created.getCategoryId());
        }

        BookCategory read = dao.read(created.getCategoryId());
        if (read == null || !"DL-Test-Cat".equals(read.getCategoryName())) {
            fail("read after create");
        } else {
            ok("read after create");
        }

        created.setCategoryName("DL-Test-Updated");
        BookCategory updated = dao.update(created);
        if (updated == null || !"DL-Test-Updated".equals(updated.getCategoryName())) {
            fail("update");
        } else {
            ok("update");
        }

        read = dao.read(created.getCategoryId());
        if (read == null || !"DL-Test-Updated".equals(read.getCategoryName())) {
            fail("read after update");
        } else {
            ok("read after update");
        }

        boolean deleted = dao.delete(created.getCategoryId());
        if (!deleted) {
            fail("delete");
        } else {
            ok("delete");
        }

        if (dao.read(created.getCategoryId()) != null) {
            fail("read after delete should be null");
        } else {
            ok("read after delete -> null");
        }

        List<BookCategory> all = dao.readAll();
        if (all == null) fail("readAll"); else ok("readAll -> " + all.size() + " categories");
    }

    private static void testAuthorDAO() throws SQLException {
        System.out.println("\n--- AuthorDAO ---");
        AuthorDAO dao = new AuthorDAO();

        Author a = new Author("DataLayer", "Tester", LocalDate.of(1990, 1, 1), "US", "Test author");
        Author created = dao.create(a);
        if (created == null || created.getAuthorId() == null) {
            fail("create");
        } else {
            ok("create -> id=" + created.getAuthorId());
        }

        Author read = dao.read(created.getAuthorId());
        if (read == null) fail("read"); else ok("read");

        created.setLastName("Updated");
        dao.update(created);
        read = dao.read(created.getAuthorId());
        if (read == null || !"Updated".equals(read.getLastName())) fail("update"); else ok("update");

        dao.delete(created.getAuthorId());
        if (dao.read(created.getAuthorId()) != null) fail("delete"); else ok("delete");

        List<Author> all = dao.readAll();
        if (all == null) fail("readAll"); else ok("readAll -> " + all.size() + " authors");
    }

    private static void testMemberDAO() throws SQLException {
        System.out.println("\n--- MemberDAO ---");
        MemberDAO dao = new MemberDAO();

        Member m = new Member("DL", "Tester", "dltest@test.local", null, null,
                LocalDate.now(), "Standard");
        Member created = dao.create(m);
        if (created == null || created.getMemberId() == null) {
            fail("create");
        } else {
            ok("create -> id=" + created.getMemberId());
        }

        if (dao.read(created.getMemberId()) == null) fail("read"); else ok("read");
        created.setEmail("updated@test.local");
        dao.update(created);
        if (dao.read(created.getMemberId()).getEmail() == null || !dao.read(created.getMemberId()).getEmail().contains("updated")) fail("update"); else ok("update");
        dao.delete(created.getMemberId());
        if (dao.read(created.getMemberId()) != null) fail("delete"); else ok("delete");

        List<Member> all = dao.readAll();
        if (all == null) fail("readAll"); else ok("readAll -> " + all.size() + " members");
    }

    private static void testBookDAO() throws SQLException {
        System.out.println("\n--- BookDAO ---");
        BookCategoryDAO catDao = new BookCategoryDAO();
        AuthorDAO authorDao = new AuthorDAO();
        BookDAO dao = new BookDAO();

        List<BookCategory> cats = catDao.readAll();
        List<Author> authors = authorDao.readAll();
        if (cats.isEmpty() || authors.isEmpty()) {
            System.out.println("  Skip full CRUD (need existing category and author). Testing readAll only.");
            List<Book> all = dao.readAll();
            if (all != null) ok("readAll -> " + all.size() + " books");
            else fail("readAll");
            return;
        }

        Book b = new Book("DL Test Book", authors.get(0).getAuthorId(), cats.get(0).getCategoryId(),
                null, 2024, null, 1, 1, null);
        Book created = dao.create(b);
        if (created == null || created.getBookId() == null) {
            fail("create");
        } else {
            ok("create -> id=" + created.getBookId());
        }

        if (dao.read(created.getBookId()) == null) fail("read"); else ok("read");
        created.setTitle("DL Test Updated");
        dao.update(created);
        if (dao.read(created.getBookId()) == null || !"DL Test Updated".equals(dao.read(created.getBookId()).getTitle())) fail("update"); else ok("update");
        dao.delete(created.getBookId());
        if (dao.read(created.getBookId()) != null) fail("delete"); else ok("delete");

        List<Book> all = dao.readAll();
        if (all == null) fail("readAll"); else ok("readAll -> " + all.size() + " books");
    }

    private static void testLoanDAO() throws SQLException {
        System.out.println("\n--- LoanDAO ---");
        LoanDAO dao = new LoanDAO();
        List<Loan> all = dao.readAll();
        if (all != null) ok("readAll -> " + all.size() + " loans");
        else fail("readAll");

        if (!all.isEmpty()) {
            Loan first = dao.read(all.get(0).getLoanId());
            if (first != null) ok("read by id");
            else fail("read by id");
        }
    }
}
