package edu.csce548.library.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.business.*;
import edu.csce548.library.model.*;
import edu.csce548.library.service.QueryService;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API server for the Library Management System (Project 2 – Service Layer).
 * Exposes all business layer methods as HTTP endpoints.
 *
 * HOSTING:
 * - Local: Run this class (see "How to run" below). The server listens on http://localhost:7000
 * - Platform options: You can deploy the packaged JAR to Railway, Render, Heroku, or any
 *   JVM host. Set environment variables DB_HOST, DB_NAME, DB_USER, DB_PASSWORD, DB_PORT
 *   (default port 5432 for PostgreSQL). For cloud hosts, add a web process that runs:
 *   java -cp <jar> edu.csce548.library.api.LibraryServer
 *   and set PORT from the platform (e.g. Railway provides PORT); this server reads
 *   System.getenv("PORT") and uses it if set, otherwise 7000.
 *
 * How to run (local):
 *   1. Start PostgreSQL and set DB_* env vars (or use defaults).
 *   2. mvn compile exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
 *   3. Or from IDE: run LibraryServer.main().
 *   4. API base URL: http://localhost:7000
 */
public class LibraryServer {
    private static final BookCategoryBusinessService categoryService = new BookCategoryBusinessService();
    private static final AuthorBusinessService authorService = new AuthorBusinessService();
    private static final MemberBusinessService memberService = new MemberBusinessService();
    private static final BookBusinessService bookService = new BookBusinessService();
    private static final LoanBusinessService loanService = new LoanBusinessService();
    private static final LibraryQueryBusinessService queryService = new LibraryQueryBusinessService();

    public static void main(String[] args) {
        String host = System.getenv("DB_HOST");
        if (host == null) host = "localhost";
        String database = System.getenv("DB_NAME");
        if (database == null) database = "library_management";
        String user = System.getenv("DB_USER");
        if (user == null) user = System.getProperty("user.name");
        String password = System.getenv("DB_PASSWORD");
        String portStr = System.getenv("DB_PORT");
        int dbPort = (portStr != null) ? Integer.parseInt(portStr) : 5432;

        DatabaseConnection.initialize(host, database, user, password, dbPort);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JavalinJackson jsonMapper = new JavalinJackson(mapper, false);

        String portEnv = System.getenv("PORT");
        int preferredPort = (portEnv != null && !portEnv.isEmpty()) ? Integer.parseInt(portEnv) : 7000;

        int port = findAvailablePort(preferredPort);
        Javalin app = Javalin.create(config -> config.jsonMapper(jsonMapper)).start(port);

        // ----- Book Categories -----
        app.get("/api/categories", ctx -> {
            List<BookCategory> list = categoryService.getAllCategories();
            ctx.json(list);
        });
        app.get("/api/categories/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            BookCategory c = categoryService.getCategoryById(id);
            if (c == null) ctx.status(404); else ctx.json(c);
        });
        app.post("/api/categories", ctx -> {
            BookCategory body = ctx.bodyAsClass(BookCategory.class);
            BookCategory created = categoryService.addCategory(body);
            ctx.status(201).json(created);
        });
        app.put("/api/categories/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            BookCategory body = ctx.bodyAsClass(BookCategory.class);
            body.setCategoryId(id);
            BookCategory updated = categoryService.updateCategory(body);
            ctx.json(updated);
        });
        app.delete("/api/categories/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean ok = categoryService.removeCategory(id);
            ctx.status(ok ? 204 : 404);
        });

        // ----- Authors -----
        app.get("/api/authors", ctx -> {
            ctx.json(authorService.getAllAuthors());
        });
        app.get("/api/authors/{id}", ctx -> {
            Author a = authorService.getAuthorById(Integer.parseInt(ctx.pathParam("id")));
            if (a == null) ctx.status(404); else ctx.json(a);
        });
        app.post("/api/authors", ctx -> {
            Author body = ctx.bodyAsClass(Author.class);
            ctx.status(201).json(authorService.addAuthor(body));
        });
        app.put("/api/authors/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Author body = ctx.bodyAsClass(Author.class);
            body.setAuthorId(id);
            ctx.json(authorService.updateAuthor(body));
        });
        app.delete("/api/authors/{id}", ctx -> {
            boolean ok = authorService.removeAuthor(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // ----- Members -----
        app.get("/api/members", ctx -> ctx.json(memberService.getAllMembers()));
        app.get("/api/members/{id}", ctx -> {
            Member m = memberService.getMemberById(Integer.parseInt(ctx.pathParam("id")));
            if (m == null) ctx.status(404); else ctx.json(m);
        });
        app.post("/api/members", ctx -> {
            Member body = ctx.bodyAsClass(Member.class);
            ctx.status(201).json(memberService.addMember(body));
        });
        app.put("/api/members/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Member body = ctx.bodyAsClass(Member.class);
            body.setMemberId(id);
            ctx.json(memberService.updateMember(body));
        });
        app.delete("/api/members/{id}", ctx -> {
            boolean ok = memberService.removeMember(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // ----- Books -----
        app.get("/api/books", ctx -> ctx.json(bookService.getAllBooks()));
        app.get("/api/books/{id}", ctx -> {
            Book b = bookService.getBookById(Integer.parseInt(ctx.pathParam("id")));
            if (b == null) ctx.status(404); else ctx.json(b);
        });
        app.post("/api/books", ctx -> {
            Book body = ctx.bodyAsClass(Book.class);
            ctx.status(201).json(bookService.addBook(body));
        });
        app.put("/api/books/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Book body = ctx.bodyAsClass(Book.class);
            body.setBookId(id);
            ctx.json(bookService.updateBook(body));
        });
        app.delete("/api/books/{id}", ctx -> {
            boolean ok = bookService.removeBook(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // ----- Loans -----
        app.get("/api/loans", ctx -> ctx.json(loanService.getAllLoans()));
        app.get("/api/loans/{id}", ctx -> {
            Loan l = loanService.getLoanById(Integer.parseInt(ctx.pathParam("id")));
            if (l == null) ctx.status(404); else ctx.json(l);
        });
        app.get("/api/loans/member/{memberId}", ctx -> {
            ctx.json(loanService.getLoansByMember(Integer.parseInt(ctx.pathParam("memberId"))));
        });
        app.get("/api/loans/status/{status}", ctx -> {
            ctx.json(loanService.getLoansByStatus(ctx.pathParam("status")));
        });
        app.post("/api/loans", ctx -> {
            Loan body = ctx.bodyAsClass(Loan.class);
            ctx.status(201).json(loanService.addLoan(body));
        });
        app.put("/api/loans/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Loan body = ctx.bodyAsClass(Loan.class);
            body.setLoanId(id);
            ctx.json(loanService.updateLoan(body));
        });
        app.delete("/api/loans/{id}", ctx -> {
            boolean ok = loanService.removeLoan(Integer.parseInt(ctx.pathParam("id")));
            ctx.status(ok ? 204 : 404);
        });

        // ----- Query / report endpoints -----
        app.get("/api/loans/with-details", ctx -> {
            ctx.json(queryService.getAllLoansWithDetails());
        });
        app.get("/api/loans/{id}/details", ctx -> {
            QueryService.LoanDetails d = queryService.getLoanDetails(Integer.parseInt(ctx.pathParam("id")));
            if (d == null) ctx.status(404); else ctx.json(toDetailsMap(d));
        });
        app.get("/api/members/{id}/summary", ctx -> {
            Map<String, Object> s = queryService.getMemberLoanSummary(Integer.parseInt(ctx.pathParam("id")));
            if (s == null) ctx.status(404); else ctx.json(s);
        });
        app.get("/api/books/popularity", ctx -> {
            ctx.json(queryService.getBookPopularityStats());
        });
        app.get("/api/records/counts", ctx -> {
            ctx.json(queryService.getAllRecordCounts());
        });

        System.out.println("Library API server running at http://localhost:" + port);
        if (port != preferredPort) {
            System.out.println("(Port " + preferredPort + " was in use. Use BASE_URL=http://localhost:" + port + " when running the client.)");
        }
    }

    /** Find an available port: try preferredPort, then preferredPort+1, +2, ... up to +10. */
    private static int findAvailablePort(int preferredPort) {
        for (int offset = 0; offset <= 10; offset++) {
            int port = preferredPort + offset;
            try (ServerSocket s = new ServerSocket(port)) {
                return port; // port is free; we close the socket and Javalin can bind to it
            } catch (Exception e) {
                continue;
            }
        }
        throw new RuntimeException("No available port between " + preferredPort + " and " + (preferredPort + 10));
    }

    private static Map<String, Object> toDetailsMap(QueryService.LoanDetails d) {
        Map<String, Object> map = new HashMap<>();
        map.put("loan", d.loan);
        map.put("member", d.member);
        map.put("book", d.book);
        map.put("author", d.author);
        map.put("category", d.category);
        return map;
    }
}
