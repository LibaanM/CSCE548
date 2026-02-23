package edu.csce548.library.business;

import edu.csce548.library.service.QueryService;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Business layer for library query operations (reports and details).
 * Exposes QueryService methods so all data access goes through the business layer.
 */
public class LibraryQueryBusinessService {
    private final QueryService queryService = new QueryService();

    public QueryService.LoanDetails getLoanDetails(int loanId) throws SQLException {
        return queryService.getLoanDetails(loanId);
    }

    public List<Map<String, Object>> getAllLoansWithDetails() throws SQLException {
        return queryService.getAllLoansWithDetails();
    }

    public Map<String, Object> getMemberLoanSummary(int memberId) throws SQLException {
        return queryService.getMemberLoanSummary(memberId);
    }

    public List<Map<String, Object>> getBookPopularityStats() throws SQLException {
        return queryService.getBookPopularityStats();
    }

    public Map<String, Long> getAllRecordCounts() throws SQLException {
        return queryService.getAllRecordsCount();
    }
}
