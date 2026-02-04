package edu.csce548.library.dao;

import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.model.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    
    public Book create(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING book_id, title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getCategoryId());
            stmt.setString(4, book.getIsbn());
            if (book.getPublicationYear() != null) {
                stmt.setInt(5, book.getPublicationYear());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, book.getPublisher());
            stmt.setInt(7, book.getTotalCopies());
            stmt.setInt(8, book.getAvailableCopies());
            stmt.setString(9, book.getDescription());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book created = new Book();
                    created.setBookId(rs.getInt("book_id"));
                    created.setTitle(rs.getString("title"));
                    created.setAuthorId(rs.getInt("author_id"));
                    created.setCategoryId(rs.getInt("category_id"));
                    created.setIsbn(rs.getString("isbn"));
                    Integer pubYear = rs.getInt("publication_year");
                    if (!rs.wasNull()) created.setPublicationYear(pubYear);
                    created.setPublisher(rs.getString("publisher"));
                    created.setTotalCopies(rs.getInt("total_copies"));
                    created.setAvailableCopies(rs.getInt("available_copies"));
                    created.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) created.setCreatedAt(ts.toLocalDateTime());
                    return created;
                }
            }
        }
        throw new SQLException("Failed to create book");
    }
    
    public Book read(Integer bookId) throws SQLException {
        String sql = "SELECT book_id, title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description, created_at FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthorId(rs.getInt("author_id"));
                    book.setCategoryId(rs.getInt("category_id"));
                    book.setIsbn(rs.getString("isbn"));
                    Integer pubYear = rs.getInt("publication_year");
                    if (!rs.wasNull()) book.setPublicationYear(pubYear);
                    book.setPublisher(rs.getString("publisher"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    book.setAvailableCopies(rs.getInt("available_copies"));
                    book.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) book.setCreatedAt(ts.toLocalDateTime());
                    return book;
                }
            }
        }
        return null;
    }
    
    public List<Book> readAll() throws SQLException {
        String sql = "SELECT book_id, title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description, created_at FROM books ORDER BY title";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setIsbn(rs.getString("isbn"));
                Integer pubYear = rs.getInt("publication_year");
                if (!rs.wasNull()) book.setPublicationYear(pubYear);
                book.setPublisher(rs.getString("publisher"));
                book.setTotalCopies(rs.getInt("total_copies"));
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) book.setCreatedAt(ts.toLocalDateTime());
                books.add(book);
            }
        }
        return books;
    }
    
    public Book update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author_id = ?, category_id = ?, isbn = ?, publication_year = ?, publisher = ?, total_copies = ?, available_copies = ?, description = ? WHERE book_id = ? RETURNING book_id, title, author_id, category_id, isbn, publication_year, publisher, total_copies, available_copies, description, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getCategoryId());
            stmt.setString(4, book.getIsbn());
            if (book.getPublicationYear() != null) {
                stmt.setInt(5, book.getPublicationYear());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setString(6, book.getPublisher());
            stmt.setInt(7, book.getTotalCopies());
            stmt.setInt(8, book.getAvailableCopies());
            stmt.setString(9, book.getDescription());
            stmt.setInt(10, book.getBookId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book updated = new Book();
                    updated.setBookId(rs.getInt("book_id"));
                    updated.setTitle(rs.getString("title"));
                    updated.setAuthorId(rs.getInt("author_id"));
                    updated.setCategoryId(rs.getInt("category_id"));
                    updated.setIsbn(rs.getString("isbn"));
                    Integer pubYear = rs.getInt("publication_year");
                    if (!rs.wasNull()) updated.setPublicationYear(pubYear);
                    updated.setPublisher(rs.getString("publisher"));
                    updated.setTotalCopies(rs.getInt("total_copies"));
                    updated.setAvailableCopies(rs.getInt("available_copies"));
                    updated.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) updated.setCreatedAt(ts.toLocalDateTime());
                    return updated;
                }
            }
        }
        throw new SQLException("Book not found");
    }
    
    public boolean delete(Integer bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            return stmt.executeUpdate() > 0;
        }
    }
}

