package edu.csce548.library.dao;

import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.model.BookCategory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookCategoryDAO {
    
    public BookCategory create(BookCategory category) throws SQLException {
        String sql = "INSERT INTO book_categories (category_name, description) VALUES (?, ?) RETURNING category_id, category_name, description, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookCategory created = new BookCategory();
                    created.setCategoryId(rs.getInt("category_id"));
                    created.setCategoryName(rs.getString("category_name"));
                    created.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) created.setCreatedAt(ts.toLocalDateTime());
                    return created;
                }
            }
        }
        throw new SQLException("Failed to create book category");
    }
    
    public BookCategory read(Integer categoryId) throws SQLException {
        String sql = "SELECT category_id, category_name, description, created_at FROM book_categories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookCategory category = new BookCategory();
                    category.setCategoryId(rs.getInt("category_id"));
                    category.setCategoryName(rs.getString("category_name"));
                    category.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) category.setCreatedAt(ts.toLocalDateTime());
                    return category;
                }
            }
        }
        return null;
    }
    
    public List<BookCategory> readAll() throws SQLException {
        String sql = "SELECT category_id, category_name, description, created_at FROM book_categories ORDER BY category_name";
        List<BookCategory> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BookCategory category = new BookCategory();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) category.setCreatedAt(ts.toLocalDateTime());
                categories.add(category);
            }
        }
        return categories;
    }
    
    public BookCategory update(BookCategory category) throws SQLException {
        String sql = "UPDATE book_categories SET category_name = ?, description = ? WHERE category_id = ? RETURNING category_id, category_name, description, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getCategoryId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BookCategory updated = new BookCategory();
                    updated.setCategoryId(rs.getInt("category_id"));
                    updated.setCategoryName(rs.getString("category_name"));
                    updated.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) updated.setCreatedAt(ts.toLocalDateTime());
                    return updated;
                }
            }
        }
        throw new SQLException("Category not found");
    }
    
    public boolean delete(Integer categoryId) throws SQLException {
        String sql = "DELETE FROM book_categories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate() > 0;
        }
    }
}

