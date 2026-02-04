package edu.csce548.library.dao;

import edu.csce548.library.DatabaseConnection;
import edu.csce548.library.model.Member;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    
    public Member create(Member member) throws SQLException {
        String sql = "INSERT INTO members (first_name, last_name, email, phone, address, membership_date, membership_type) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING member_id, first_name, last_name, email, phone, address, membership_date, membership_type, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getAddress());
            stmt.setDate(6, Date.valueOf(member.getMembershipDate()));
            stmt.setString(7, member.getMembershipType());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Member created = new Member();
                    created.setMemberId(rs.getInt("member_id"));
                    created.setFirstName(rs.getString("first_name"));
                    created.setLastName(rs.getString("last_name"));
                    created.setEmail(rs.getString("email"));
                    created.setPhone(rs.getString("phone"));
                    created.setAddress(rs.getString("address"));
                    created.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                    created.setMembershipType(rs.getString("membership_type"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) created.setCreatedAt(ts.toLocalDateTime());
                    return created;
                }
            }
        }
        throw new SQLException("Failed to create member");
    }
    
    public Member read(Integer memberId) throws SQLException {
        String sql = "SELECT member_id, first_name, last_name, email, phone, address, membership_date, membership_type, created_at FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberId(rs.getInt("member_id"));
                    member.setFirstName(rs.getString("first_name"));
                    member.setLastName(rs.getString("last_name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));
                    member.setAddress(rs.getString("address"));
                    member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                    member.setMembershipType(rs.getString("membership_type"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) member.setCreatedAt(ts.toLocalDateTime());
                    return member;
                }
            }
        }
        return null;
    }
    
    public List<Member> readAll() throws SQLException {
        String sql = "SELECT member_id, first_name, last_name, email, phone, address, membership_date, membership_type, created_at FROM members ORDER BY last_name, first_name";
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                member.setMembershipType(rs.getString("membership_type"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) member.setCreatedAt(ts.toLocalDateTime());
                members.add(member);
            }
        }
        return members;
    }
    
    public Member update(Member member) throws SQLException {
        String sql = "UPDATE members SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, membership_date = ?, membership_type = ? WHERE member_id = ? RETURNING member_id, first_name, last_name, email, phone, address, membership_date, membership_type, created_at";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getAddress());
            stmt.setDate(6, Date.valueOf(member.getMembershipDate()));
            stmt.setString(7, member.getMembershipType());
            stmt.setInt(8, member.getMemberId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Member updated = new Member();
                    updated.setMemberId(rs.getInt("member_id"));
                    updated.setFirstName(rs.getString("first_name"));
                    updated.setLastName(rs.getString("last_name"));
                    updated.setEmail(rs.getString("email"));
                    updated.setPhone(rs.getString("phone"));
                    updated.setAddress(rs.getString("address"));
                    updated.setMembershipDate(rs.getDate("membership_date").toLocalDate());
                    updated.setMembershipType(rs.getString("membership_type"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) updated.setCreatedAt(ts.toLocalDateTime());
                    return updated;
                }
            }
        }
        throw new SQLException("Member not found");
    }
    
    public boolean delete(Integer memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            return stmt.executeUpdate() > 0;
        }
    }
}

