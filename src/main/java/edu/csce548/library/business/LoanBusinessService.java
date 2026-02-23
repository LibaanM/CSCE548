package edu.csce548.library.business;

import edu.csce548.library.dao.LoanDAO;
import edu.csce548.library.model.Loan;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for loans. Exposes all data layer (DAO) CRUD operations
 * plus read-by-member and read-by-status.
 */
public class LoanBusinessService {
    private final LoanDAO dao = new LoanDAO();

    public Loan addLoan(Loan loan) throws SQLException {
        return dao.create(loan);
    }

    public Loan getLoanById(int loanId) throws SQLException {
        return dao.read(loanId);
    }

    public List<Loan> getAllLoans() throws SQLException {
        return dao.readAll();
    }

    public List<Loan> getLoansByMember(int memberId) throws SQLException {
        return dao.readByMember(memberId);
    }

    public List<Loan> getLoansByStatus(String status) throws SQLException {
        return dao.readByStatus(status);
    }

    public Loan updateLoan(Loan loan) throws SQLException {
        return dao.update(loan);
    }

    public boolean removeLoan(int loanId) throws SQLException {
        return dao.delete(loanId);
    }
}
