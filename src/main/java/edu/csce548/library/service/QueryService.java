package edu.csce548.library.service;

import edu.csce548.library.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryService {
    
    public static class LoanDetails {
        public Map<String, Object> loan;
        public Map<String, Object> member;
        public Map<String, Object> book;
        public Map<String, Object> author;
        public Map<String, Object> category;
        
        public LoanDetails() {
            this.loan = new HashMap<>();
            this.member = new HashMap<>();
            this.book = new HashMap<>();
            this.author = new HashMap<>();
            this.category = new HashMap<>();
        }
    }
    
    public LoanDetails getLoanDetails(Integer loanId) throws SQLException {
        LoanDetails details = new LoanDetails();
        String sql = "SELECT l.loan_id, l.loan_date, l.due_date, l.return_date, l.fine_amount, l.status, l.notes, " +
                     "m.member_id, m.first_name as member_first_name, m.last_name as member_last_name, m.email, m.membership_type, " +
                     "b.book_id, b.title, b.isbn, b.publication_year, b.available_copies, " +
                     "a.author_id, a.first_name as author_first_name, a.last_name as author_last_name, " +
                     "c.category_id, c.category_name " +
                     "FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "JOIN authors a ON b.author_id = a.author_id " +
                     "JOIN book_categories c ON b.category_id = c.category_id " +
                     "WHERE l.loan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details.loan.put("loan_id", rs.getInt("loan_id"));
                    details.loan.put("loan_date", rs.getDate("loan_date"));
                    details.loan.put("due_date", rs.getDate("due_date"));
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) details.loan.put("return_date", returnDate);
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) details.loan.put("fine_amount", fine);
                    details.loan.put("status", rs.getString("status"));
                    details.loan.put("notes", rs.getString("notes"));
                    
                    details.member.put("member_id", rs.getInt("member_id"));
                    details.member.put("first_name", rs.getString("member_first_name"));
                    details.member.put("last_name", rs.getString("member_last_name"));
                    details.member.put("email", rs.getString("email"));
                    details.member.put("membership_type", rs.getString("membership_type"));
                    
                    details.book.put("book_id", rs.getInt("book_id"));
                    details.book.put("title", rs.getString("title"));
                    details.book.put("isbn", rs.getString("isbn"));
                    Integer pubYear = rs.getInt("publication_year");
                    if (!rs.wasNull()) details.book.put("publication_year", pubYear);
                    details.book.put("available_copies", rs.getInt("available_copies"));
                    
                    details.author.put("author_id", rs.getInt("author_id"));
                    details.author.put("first_name", rs.getString("author_first_name"));
                    details.author.put("last_name", rs.getString("author_last_name"));
                    
                    details.category.put("category_id", rs.getInt("category_id"));
                    details.category.put("category_name", rs.getString("category_name"));
                } else {
                    return null;
                }
            }
        }
        return details;
    }
    
    public List<Map<String, Object>> getAllLoansWithDetails() throws SQLException {
        String sql = "SELECT l.loan_id, l.loan_date, l.due_date, l.return_date, l.fine_amount, l.status, " +
                     "m.member_id, m.first_name as member_first_name, m.last_name as member_last_name, " +
                     "b.book_id, b.title, " +
                     "a.first_name as author_first_name, a.last_name as author_last_name " +
                     "FROM loans l " +
                     "JOIN members m ON l.member_id = m.member_id " +
                     "JOIN books b ON l.book_id = b.book_id " +
                     "JOIN authors a ON b.author_id = a.author_id " +
                     "ORDER BY l.loan_date DESC, l.loan_id DESC";
        List<Map<String, Object>> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> loan = new HashMap<>();
                loan.put("loan_id", rs.getInt("loan_id"));
                loan.put("loan_date", rs.getDate("loan_date"));
                loan.put("due_date", rs.getDate("due_date"));
                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) loan.put("return_date", returnDate);
                BigDecimal fine = rs.getBigDecimal("fine_amount");
                if (fine != null) loan.put("fine_amount", fine);
                loan.put("status", rs.getString("status"));
                loan.put("member_id", rs.getInt("member_id"));
                loan.put("member_name", rs.getString("member_first_name") + " " + rs.getString("member_last_name"));
                loan.put("book_id", rs.getInt("book_id"));
                loan.put("book_title", rs.getString("title"));
                loan.put("author_name", rs.getString("author_first_name") + " " + rs.getString("author_last_name"));
                loans.add(loan);
            }
        }
        return loans;
    }
    
    public Map<String, Object> getMemberLoanSummary(Integer memberId) throws SQLException {
        String sql = "SELECT m.member_id, m.first_name, m.last_name, m.email, m.membership_type, " +
                     "COUNT(DISTINCT l.loan_id) as total_loans, " +
                     "COUNT(DISTINCT CASE WHEN l.status = 'Active' THEN l.loan_id END) as active_loans, " +
                     "COUNT(DISTINCT CASE WHEN l.status = 'Returned' THEN l.loan_id END) as returned_loans, " +
                     "SUM(l.fine_amount) as total_fines, " +
                     "MIN(l.loan_date) as first_loan_date, " +
                     "MAX(l.loan_date) as last_loan_date " +
                     "FROM members m " +
                     "LEFT JOIN loans l ON m.member_id = l.member_id " +
                     "WHERE m.member_id = ? " +
                     "GROUP BY m.member_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("member_id", rs.getInt("member_id"));
                    summary.put("first_name", rs.getString("first_name"));
                    summary.put("last_name", rs.getString("last_name"));
                    summary.put("email", rs.getString("email"));
                    summary.put("membership_type", rs.getString("membership_type"));
                    summary.put("total_loans", rs.getLong("total_loans"));
                    summary.put("active_loans", rs.getLong("active_loans"));
                    summary.put("returned_loans", rs.getLong("returned_loans"));
                    BigDecimal totalFines = rs.getBigDecimal("total_fines");
                    if (totalFines != null) summary.put("total_fines", totalFines);
                    Date firstDate = rs.getDate("first_loan_date");
                    if (firstDate != null) summary.put("first_loan_date", firstDate);
                    Date lastDate = rs.getDate("last_loan_date");
                    if (lastDate != null) summary.put("last_loan_date", lastDate);
                    return summary;
                }
            }
        }
        return null;
    }
    
    public List<Map<String, Object>> getBookPopularityStats() throws SQLException {
        String sql = "SELECT b.book_id, b.title, a.first_name as author_first_name, a.last_name as author_last_name, " +
                     "c.category_name, b.available_copies, b.total_copies, " +
                     "COUNT(l.loan_id) as loan_count, " +
                     "COUNT(DISTINCT l.member_id) as unique_borrowers " +
                     "FROM books b " +
                     "JOIN authors a ON b.author_id = a.author_id " +
                     "JOIN book_categories c ON b.category_id = c.category_id " +
                     "LEFT JOIN loans l ON b.book_id = l.book_id " +
                     "GROUP BY b.book_id, a.author_id, c.category_id " +
                     "ORDER BY loan_count DESC, b.title";
        List<Map<String, Object>> stats = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("book_id", rs.getInt("book_id"));
                stat.put("title", rs.getString("title"));
                stat.put("author_name", rs.getString("author_first_name") + " " + rs.getString("author_last_name"));
                stat.put("category_name", rs.getString("category_name"));
                stat.put("available_copies", rs.getInt("available_copies"));
                stat.put("total_copies", rs.getInt("total_copies"));
                stat.put("loan_count", rs.getLong("loan_count"));
                stat.put("unique_borrowers", rs.getLong("unique_borrowers"));
                stats.add(stat);
            }
        }
        return stats;
    }
    
    public Map<String, Long> getAllRecordsCount() throws SQLException {
        String sql = "SELECT (SELECT COUNT(*) FROM book_categories) as categories_count, " +
                     "(SELECT COUNT(*) FROM authors) as authors_count, " +
                     "(SELECT COUNT(*) FROM members) as members_count, " +
                     "(SELECT COUNT(*) FROM books) as books_count, " +
                     "(SELECT COUNT(*) FROM loans) as loans_count";
        Map<String, Long> counts = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                counts.put("categories_count", rs.getLong("categories_count"));
                counts.put("authors_count", rs.getLong("authors_count"));
                counts.put("members_count", rs.getLong("members_count"));
                counts.put("books_count", rs.getLong("books_count"));
                counts.put("loans_count", rs.getLong("loans_count"));
            }
        }
        return counts;
    }
}

