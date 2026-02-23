package edu.csce548.library.business;

import edu.csce548.library.dao.AuthorDAO;
import edu.csce548.library.model.Author;
import java.sql.SQLException;
import java.util.List;

/**
 * Business layer for authors. Exposes all data layer (DAO) CRUD operations.
 */
public class AuthorBusinessService {
    private final AuthorDAO dao = new AuthorDAO();

    public Author addAuthor(Author author) throws SQLException {
        return dao.create(author);
    }

    public Author getAuthorById(int authorId) throws SQLException {
        return dao.read(authorId);
    }

    public List<Author> getAllAuthors() throws SQLException {
        return dao.readAll();
    }

    public Author updateAuthor(Author author) throws SQLException {
        return dao.update(author);
    }

    public boolean removeAuthor(int authorId) throws SQLException {
        return dao.delete(authorId);
    }
}
