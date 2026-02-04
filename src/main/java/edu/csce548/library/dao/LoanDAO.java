package edu.csce548.library.dao;

import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.model.Loan;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    
    public Loan create(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loan.getMemberId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            if (loan.getReturnDate() != null) {
                stmt.setDate(5, Date.valueOf(loan.getReturnDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            if (loan.getFineAmount() != null) {
                stmt.setBigDecimal(6, loan.getFineAmount());
            } else {
                stmt.setBigDecimal(6, BigDecimal.ZERO);
            }
            stmt.setString(7, loan.getStatus());
            stmt.setString(8, loan.getNotes());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Loan created = new Loan();
                    created.setLoanId(rs.getInt("loan_id"));
                    created.setMemberId(rs.getInt("member_id"));
                    created.setBookId(rs.getInt("book_id"));
                    created.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    created.setDueDate(rs.getDate("due_date").toLocalDate());
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) created.setReturnDate(returnDate.toLocalDate());
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) created.setFineAmount(fine);
                    created.setStatus(rs.getString("status"));
                    created.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) created.setCreatedAt(ts.toLocalDateTime());
                    return created;
                }
            }
        }
        throw new SQLException("Failed to create loan");
    }
    
    public Loan read(Integer loanId) throws SQLException {
        String sql = "SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at FROM loans WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setMemberId(rs.getInt("member_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    loan.setDueDate(rs.getDate("due_date").toLocalDate());
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) loan.setReturnDate(returnDate.toLocalDate());
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) loan.setFineAmount(fine);
                    loan.setStatus(rs.getString("status"));
                    loan.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) loan.setCreatedAt(ts.toLocalDateTime());
                    return loan;
                }
            }
        }
        return null;
    }
    
    public List<Loan> readAll() throws SQLException {
        String sql = "SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at FROM loans ORDER BY loan_date DESC, loan_id DESC";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setMemberId(rs.getInt("member_id"));
                loan.setBookId(rs.getInt("book_id"));
                loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                loan.setDueDate(rs.getDate("due_date").toLocalDate());
                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) loan.setReturnDate(returnDate.toLocalDate());
                BigDecimal fine = rs.getBigDecimal("fine_amount");
                if (fine != null) loan.setFineAmount(fine);
                loan.setStatus(rs.getString("status"));
                loan.setNotes(rs.getString("notes"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) loan.setCreatedAt(ts.toLocalDateTime());
                loans.add(loan);
            }
        }
        return loans;
    }
    
    public List<Loan> readByMember(Integer memberId) throws SQLException {
        String sql = "SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at FROM loans WHERE member_id = ? ORDER BY loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setMemberId(rs.getInt("member_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    loan.setDueDate(rs.getDate("due_date").toLocalDate());
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) loan.setReturnDate(returnDate.toLocalDate());
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) loan.setFineAmount(fine);
                    loan.setStatus(rs.getString("status"));
                    loan.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) loan.setCreatedAt(ts.toLocalDateTime());
                    loans.add(loan);
                }
            }
        }
        return loans;
    }
    
    public List<Loan> readByStatus(String status) throws SQLException {
        String sql = "SELECT loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at FROM loans WHERE status = ? ORDER BY loan_date DESC";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setMemberId(rs.getInt("member_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    loan.setDueDate(rs.getDate("due_date").toLocalDate());
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) loan.setReturnDate(returnDate.toLocalDate());
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) loan.setFineAmount(fine);
                    loan.setStatus(rs.getString("status"));
                    loan.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) loan.setCreatedAt(ts.toLocalDateTime());
                    loans.add(loan);
                }
            }
        }
        return loans;
    }
    
    public Loan update(Loan loan) throws SQLException {
        String sql = "UPDATE loans SET member_id = ?, book_id = ?, loan_date = ?, due_date = ?, return_date = ?, fine_amount = ?, status = ?, notes = ? WHERE loan_id = ? RETURNING loan_id, member_id, book_id, loan_date, due_date, return_date, fine_amount, status, notes, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loan.getMemberId());
            stmt.setInt(2, loan.getBookId());
            stmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            stmt.setDate(4, Date.valueOf(loan.getDueDate()));
            if (loan.getReturnDate() != null) {
                stmt.setDate(5, Date.valueOf(loan.getReturnDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            if (loan.getFineAmount() != null) {
                stmt.setBigDecimal(6, loan.getFineAmount());
            } else {
                stmt.setBigDecimal(6, BigDecimal.ZERO);
            }
            stmt.setString(7, loan.getStatus());
            stmt.setString(8, loan.getNotes());
            stmt.setInt(9, loan.getLoanId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Loan updated = new Loan();
                    updated.setLoanId(rs.getInt("loan_id"));
                    updated.setMemberId(rs.getInt("member_id"));
                    updated.setBookId(rs.getInt("book_id"));
                    updated.setLoanDate(rs.getDate("loan_date").toLocalDate());
                    updated.setDueDate(rs.getDate("due_date").toLocalDate());
                    Date returnDate = rs.getDate("return_date");
                    if (returnDate != null) updated.setReturnDate(returnDate.toLocalDate());
                    BigDecimal fine = rs.getBigDecimal("fine_amount");
                    if (fine != null) updated.setFineAmount(fine);
                    updated.setStatus(rs.getString("status"));
                    updated.setNotes(rs.getString("notes"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) updated.setCreatedAt(ts.toLocalDateTime());
                    return updated;
                }
            }
        }
        throw new SQLException("Loan not found");
    }
    
    public boolean delete(Integer loanId) throws SQLException {
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            return stmt.executeUpdate() > 0;
        }
    }
}

