package edu.csce548.library.business;

import edu.csce548.library.dao.BookCategoryDAO;
import edu.csce548.library.model.BookCategory;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for book categories. Exposes all data layer (DAO) CRUD operations.
 * Services and clients should use this layer instead of calling the DAO directly.
 */
public class BookCategoryBusinessService {
    private final BookCategoryDAO dao = new BookCategoryDAO();

    public BookCategory addCategory(BookCategory category) throws SQLException {
        return dao.create(category);
    }

    public BookCategory getCategoryById(int categoryId) throws SQLException {
        return dao.read(categoryId);
    }

    public List<BookCategory> getAllCategories() throws SQLException {
        return dao.readAll();
    }

    public BookCategory updateCategory(BookCategory category) throws SQLException {
        return dao.update(category);
    }

    public boolean removeCategory(int categoryId) throws SQLException {
        return dao.delete(categoryId);
    }
}
