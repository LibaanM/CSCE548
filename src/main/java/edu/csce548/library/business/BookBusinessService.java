package edu.csce548.library.business;

import edu.csce548.library.dao.BookDAO;
import edu.csce548.library.model.Book;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for books. Exposes all data layer (DAO) CRUD operations.
 */
public class BookBusinessService {
    private final BookDAO dao = new BookDAO();

    public Book addBook(Book book) throws SQLException {
        return dao.create(book);
    }

    public Book getBookById(int bookId) throws SQLException {
        return dao.read(bookId);
    }

    public List<Book> getAllBooks() throws SQLException {
        return dao.readAll();
    }

    public Book updateBook(Book book) throws SQLException {
        return dao.update(book);
    }

    public boolean removeBook(int bookId) throws SQLException {
        return dao.delete(bookId);
    }
}
