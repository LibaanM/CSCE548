package edu.csce548.library.dao;

import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.model.Author;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    
    public Author create(Author author) throws SQLException {
        String sql = "INSERT INTO authors (first_name, last_name, birth_date, nationality, biography) VALUES (?, ?, ?, ?, ?) RETURNING author_id, first_name, last_name, birth_date, nationality, biography, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            if (author.getBirthDate() != null) {
                stmt.setDate(3, Date.valueOf(author.getBirthDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, author.getNationality());
            stmt.setString(5, author.getBiography());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Author created = new Author();
                    created.setAuthorId(rs.getInt("author_id"));
                    created.setFirstName(rs.getString("first_name"));
                    created.setLastName(rs.getString("last_name"));
                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) created.setBirthDate(birthDate.toLocalDate());
                    created.setNationality(rs.getString("nationality"));
                    created.setBiography(rs.getString("biography"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) created.setCreatedAt(ts.toLocalDateTime());
                    return created;
                }
            }
        }
        throw new SQLException("Failed to create author");
    }
    
    public Author read(Integer authorId) throws SQLException {
        String sql = "SELECT author_id, first_name, last_name, birth_date, nationality, biography, created_at FROM authors WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Author author = new Author();
                    author.setAuthorId(rs.getInt("author_id"));
                    author.setFirstName(rs.getString("first_name"));
                    author.setLastName(rs.getString("last_name"));
                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) author.setBirthDate(birthDate.toLocalDate());
                    author.setNationality(rs.getString("nationality"));
                    author.setBiography(rs.getString("biography"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) author.setCreatedAt(ts.toLocalDateTime());
                    return author;
                }
            }
        }
        return null;
    }
    
    public List<Author> readAll() throws SQLException {
        String sql = "SELECT author_id, first_name, last_name, birth_date, nationality, biography, created_at FROM authors ORDER BY last_name, first_name";
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Author author = new Author();
                author.setAuthorId(rs.getInt("author_id"));
                author.setFirstName(rs.getString("first_name"));
                author.setLastName(rs.getString("last_name"));
                Date birthDate = rs.getDate("birth_date");
                if (birthDate != null) author.setBirthDate(birthDate.toLocalDate());
                author.setNationality(rs.getString("nationality"));
                author.setBiography(rs.getString("biography"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) author.setCreatedAt(ts.toLocalDateTime());
                authors.add(author);
            }
        }
        return authors;
    }
    
    public Author update(Author author) throws SQLException {
        String sql = "UPDATE authors SET first_name = ?, last_name = ?, birth_date = ?, nationality = ?, biography = ? WHERE author_id = ? RETURNING author_id, first_name, last_name, birth_date, nationality, biography, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            if (author.getBirthDate() != null) {
                stmt.setDate(3, Date.valueOf(author.getBirthDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setString(4, author.getNationality());
            stmt.setString(5, author.getBiography());
            stmt.setInt(6, author.getAuthorId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Author updated = new Author();
                    updated.setAuthorId(rs.getInt("author_id"));
                    updated.setFirstName(rs.getString("first_name"));
                    updated.setLastName(rs.getString("last_name"));
                    Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) updated.setBirthDate(birthDate.toLocalDate());
                    updated.setNationality(rs.getString("nationality"));
                    updated.setBiography(rs.getString("biography"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) updated.setCreatedAt(ts.toLocalDateTime());
                    return updated;
                }
            }
        }
        throw new SQLException("Author not found");
    }
    
    public boolean delete(Integer authorId) throws SQLException {
        String sql = "DELETE FROM authors WHERE author_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            return stmt.executeUpdate() > 0;
        }
    }
}

