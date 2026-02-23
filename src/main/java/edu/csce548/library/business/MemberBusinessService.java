package edu.csce548.library.business;

import edu.csce548.library.dao.MemberDAO;
import edu.csce548.library.model.Member;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for members. Exposes all data layer (DAO) CRUD operations.
 */
public class MemberBusinessService {
    private final MemberDAO dao = new MemberDAO();

    public Member addMember(Member member) throws SQLException {
        return dao.create(member);
    }

    public Member getMemberById(int memberId) throws SQLException {
        return dao.read(memberId);
    }

    public List<Member> getAllMembers() throws SQLException {
        return dao.readAll();
    }

    public Member updateMember(Member member) throws SQLException {
        return dao.update(member);
    }

    public boolean removeMember(int memberId) throws SQLException {
        return dao.delete(memberId);
    }
}
