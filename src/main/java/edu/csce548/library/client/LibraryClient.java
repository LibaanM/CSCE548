package edu.csce548.library.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Console client to test the Library REST API (Project 2).
 * Demonstrates full CRUD: Create (POST), Read (GET), Update (PUT), Delete (DELETE).
 *
 * Prerequisite: Start the API server first:
 *   mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer
 *
 * Then run this client:
 *   mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient
 *
 * Optional: set BASE_URL env var (default http://localhost:7000)
 */
public class LibraryClient {
    private static final String BASE_URL = System.getenv().getOrDefault("BASE_URL", "http://localhost:7000");
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        System.out.println("=== Library API Client (Project 2 - Test CRUD) ===");
        System.out.println("Base URL: " + BASE_URL);
        System.out.println();

        try {
            // Quick check that server is reachable before running CRUD
            if (!checkServerReachable()) {
                System.err.println("\n>>> Server not reachable at " + BASE_URL);
                System.err.println("    Start the API server first in another terminal:");
                System.err.println("    ./run_project2_server.sh");
                System.err.println("    or: mvn exec:java -Dexec.mainClass=edu.csce548.library.api.LibraryServer");
                System.err.println("    If you use a different port (e.g. 7001), run:");
                System.err.println("    BASE_URL=http://localhost:7001 mvn exec:java -Dexec.mainClass=edu.csce548.library.client.LibraryClient");
                System.exit(1);
            }
            // Test on Book Categories: Create -> Get -> Update -> Get -> Delete -> Get (404)
            testCategoryCrud();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("\nAll steps completed successfully.");
    }

    private static void testCategoryCrud() throws Exception {
        System.out.println("--- Testing Book Category CRUD ---");

        // 1. CREATE (POST)
        System.out.println("1. POST /api/categories (Create)");
        String createBody = "{\"categoryName\":\"Client-Test-Category\",\"description\":\"Created by console client\"}";
        HttpResponse<String> createResp = send("POST", "/api/categories", createBody);
        if (createResp.statusCode() != 201) {
            throw new RuntimeException("Create failed: " + createResp.statusCode() + " " + createResp.body());
        }
        int id = extractId(createResp.body());
        System.out.println("   Created category with id: " + id);
        System.out.println("   Response: " + createResp.body());
        System.out.println();

        // 2. READ (GET) after create
        System.out.println("2. GET /api/categories/" + id + " (Read after create)");
        HttpResponse<String> get1 = send("GET", "/api/categories/" + id, null);
        if (get1.statusCode() != 200) {
            throw new RuntimeException("Get after create failed: " + get1.statusCode());
        }
        System.out.println("   " + get1.body());
        System.out.println();

        // 3. UPDATE (PUT)
        System.out.println("3. PUT /api/categories/" + id + " (Update)");
        String updateBody = "{\"categoryId\":" + id + ",\"categoryName\":\"Client-Test-Updated\",\"description\":\"Updated by client\"}";
        HttpResponse<String> updateResp = send("PUT", "/api/categories/" + id, updateBody);
        if (updateResp.statusCode() != 200) {
            throw new RuntimeException("Update failed: " + updateResp.statusCode() + " " + updateResp.body());
        }
        System.out.println("   " + updateResp.body());
        System.out.println();

        // 4. READ (GET) after update
        System.out.println("4. GET /api/categories/" + id + " (Read after update)");
        HttpResponse<String> get2 = send("GET", "/api/categories/" + id, null);
        if (get2.statusCode() != 200) {
            throw new RuntimeException("Get after update failed: " + get2.statusCode());
        }
        if (!get2.body().contains("Client-Test-Updated")) {
            throw new RuntimeException("Update did not persist: " + get2.body());
        }
        System.out.println("   " + get2.body());
        System.out.println();

        // 5. DELETE
        System.out.println("5. DELETE /api/categories/" + id + " (Delete)");
        HttpResponse<String> delResp = send("DELETE", "/api/categories/" + id, null);
        if (delResp.statusCode() != 204) {
            throw new RuntimeException("Delete failed: " + delResp.statusCode() + " " + delResp.body());
        }
        System.out.println("   Deleted (204 No Content)");
        System.out.println();

        // 6. READ (GET) after delete -> expect 404
        System.out.println("6. GET /api/categories/" + id + " (Read after delete - expect 404)");
        HttpResponse<String> get3 = send("GET", "/api/categories/" + id, null);
        if (get3.statusCode() != 404) {
            throw new RuntimeException("Expected 404 after delete, got: " + get3.statusCode());
        }
        System.out.println("   Got 404 Not Found as expected.");
    }

    /** Returns true if the API server responds at BASE_URL. */
    private static boolean checkServerReachable() {
        try {
            HttpResponse<String> r = send("GET", "/api/categories", null);
            return true; // any response means server is up
        } catch (java.net.ConnectException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static HttpResponse<String> send(String method, String path, String body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json");
        switch (method) {
            case "GET":
                builder.GET();
                break;
            case "POST":
                builder.POST(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
                break;
            case "PUT":
                builder.PUT(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
                break;
            case "DELETE":
                builder.DELETE();
                break;
            default:
                throw new IllegalArgumentException(method);
        }
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static int extractId(String json) {
        // Simple extract "categoryId":123 or "authorId":123 etc.
        int i = json.indexOf("\"categoryId\"");
        if (i < 0) i = json.indexOf("\"authorId\"");
        if (i < 0) i = json.indexOf("\"memberId\"");
        if (i < 0) i = json.indexOf("\"bookId\"");
        if (i < 0) i = json.indexOf("\"loanId\"");
        if (i < 0) throw new RuntimeException("Could not find id in: " + json);
        int colon = json.indexOf(":", i);
        int start = colon + 1;
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\t')) start++;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return Integer.parseInt(json.substring(start, end));
    }
}
