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
import io.javalin.http.staticfiles.Location;
import java.awt.Desktop;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(jsonMapper);
            // Allow web client (Project 3) to call API from any origin (browser "Failed to fetch" fix)
            config.bundledPlugins.enableCors(cors -> cors.addRule(rule -> rule.anyHost()));
            // Serve Project 3 web client from / (index.html, styles.css, app.js from classpath public/)
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });
        }).start(port);

        // Manual CORS headers so browser allows fetch() from any origin (backup for plugin)
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Accept");
        });
        app.options("/*", ctx -> ctx.status(204));

        // Return detailed error JSON for any unhandled exception (e.g. during JSON serialization)
        app.exception(Exception.class, (e, ctx) -> {
            Map<String, Object> err = new HashMap<>();
            err.put("message", e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            err.put("detail", e.toString());
            ctx.status(500).json(err);
        });

        // Redirect root to web client (Project 3)
        app.get("/", ctx -> ctx.redirect("/index.html"));

        // ----- Book Categories -----
        app.get("/api/categories", ctx -> {
            List<BookCategory> list = categoryService.getAllCategories();
            ctx.json(list);
        });
        app.get("/api/categories/{id}", ctx -> {
            String id = ctx.pathParam("id");
            BookCategory c = categoryService.getCategoryById(Integer.parseInt(id));
            if (c == null) notFound(ctx, "category", id); else ctx.json(c);
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
            ctx.json(deduplicateAuthors(authorService.getAllAuthors()));
        });
        app.get("/api/authors/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Author a = authorService.getAuthorById(Integer.parseInt(id));
            if (a == null) notFound(ctx, "author", id); else ctx.json(a);
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
            String id = ctx.pathParam("id");
            Member m = memberService.getMemberById(Integer.parseInt(id));
            if (m == null) notFound(ctx, "member", id); else ctx.json(m);
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
        app.get("/api/books/popularity", ctx -> {
            try {
                ctx.json(queryService.getBookPopularityStats());
            } catch (Exception e) {
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Failed to get book popularity stats");
                err.put("detail", e.getMessage());
                ctx.status(500).json(err);
            }
        });
        app.get("/api/books/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Book b = bookService.getBookById(Integer.parseInt(id));
            if (b == null) notFound(ctx, "book", id); else ctx.json(b);
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
        app.get("/api/loans/with-details", ctx -> {
            try {
                ctx.json(queryService.getAllLoansWithDetails());
            } catch (Exception e) {
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Failed to get loans with details");
                err.put("detail", e.getMessage());
                ctx.status(500).json(err);
            }
        });
        app.get("/api/loans/member/{memberId}", ctx -> {
            ctx.json(loanService.getLoansByMember(Integer.parseInt(ctx.pathParam("memberId"))));
        });
        app.get("/api/loans/status/{status}", ctx -> {
            ctx.json(loanService.getLoansByStatus(ctx.pathParam("status")));
        });
        app.get("/api/loans/{id}/details", ctx -> {
            String id = ctx.pathParam("id");
            QueryService.LoanDetails d = queryService.getLoanDetails(Integer.parseInt(id));
            if (d == null) notFound(ctx, "loan", id); else ctx.json(toDetailsMap(d));
        });
        app.get("/api/loans/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Loan l = loanService.getLoanById(Integer.parseInt(id));
            if (l == null) notFound(ctx, "loan", id); else ctx.json(l);
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
        app.get("/api/members/{id}/summary", ctx -> {
            String id = ctx.pathParam("id");
            Map<String, Object> s = queryService.getMemberLoanSummary(Integer.parseInt(id));
            if (s == null) notFound(ctx, "member", id); else ctx.json(s);
        });
        app.get("/api/records/counts", ctx -> {
            ctx.json(queryService.getAllRecordCounts());
        });

        System.out.println("Library API server running at http://localhost:" + port);
        System.out.println("Open http://localhost:" + port + " in your browser for the Project 3 web client (API base URL will use this port automatically).");
        if (port != preferredPort) {
            System.out.println("(Port " + preferredPort + " was in use; using " + port + " instead.)");
        }
        openBrowserWhenReady(port);
    }

    /** Opens the default browser to the web client after a short delay so the server is ready. */
    private static void openBrowserWhenReady(int port) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1500);
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(URI.create("http://localhost:" + port));
                    }
                }
            } catch (Exception e) {
                // Ignore (e.g. headless environment); user can open the URL manually
            }
        });
        t.setDaemon(true);
        t.start();
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

    /** Deduplicate authors by (firstName, lastName, birthDate), keeping the one with the smallest authorId. */
    private static List<Author> deduplicateAuthors(List<Author> authors) {
        if (authors == null || authors.isEmpty()) return authors;
        Map<String, Author> byIdentity = new LinkedHashMap<>();
        for (Author a : authors) {
            String key = a.getFirstName() + "|" + a.getLastName() + "|" +
                (a.getBirthDate() == null ? "" : a.getBirthDate().toString());
            Author existing = byIdentity.get(key);
            if (existing == null || (a.getAuthorId() != null && existing.getAuthorId() != null && a.getAuthorId() < existing.getAuthorId())) {
                byIdentity.put(key, a);
            }
        }
        return new ArrayList<>(byIdentity.values());
    }

    /** Send 404 with a JSON body so the client can display the error message. */
    private static void notFound(io.javalin.http.Context ctx, String resource, String id) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", resource + " not found");
        body.put("detail", "No " + resource + " with id " + id);
        ctx.status(404).json(body);
    }
}
