
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.dal;

import com.library.dto.Book;
import com.library.dto.SharedBook;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IuraPC
 */
public class BookRepository {

    private final String SELECT_BOOK_BY_TITLE = "SELECT * FROM books WHERE title = ?";
    private final String SELECT_BOOK_BY_AUTHOR = "SELECT * FROM books WHERE author = ?";
    private final String SELECT_BOOK_BY_AUTHOR_AND_TITLE = "SELECT * FROM books WHERE author = ? AND title = ?";
    private final String INSERT_INTO_BOOK = "INSERT books (title, author, genre) VALUES (?, ?, ?)";
    private final String INSERT_INTO_SHARED_BOOK = "INSERT shared_books (user_id, book_id) VALUES (?, ?)";
    private final String INSERT_INTO_BORROWED_BOOK = "INSERT borrowed_books (user_id, book_id) VALUES (?, ?)";
    private final String SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE id = ?";
    private final String SELECT_BOOKS_BY_USERID = "SELECT b.id, sb.id, user_id, book_id, title, author,year_publish, genre"
            + "  FROM books as b JOIN shared_books as sb on b.id = sb.book_id WHERE sb.user_id = ?";

    private final String SELECT_BORROWED_BOOKS_BY_USERID = "SELECT b.id, sb.id, sb.user_id as user_id, sb.book_id as book_id, title, author,year_publish, genre"
            + "  FROM books as b JOIN shared_books as sbb on b.id = sbb.book_id "
            + "  JOIN borrowed_books as sb on sb.book_id = sbb.id WHERE sb.user_id = ?";

    public ArrayList<Book> getBooksByTitle(String title, DbConnection conn) {

        return getBooksByCriteria(new String[]{title}, SELECT_BOOK_BY_TITLE, conn);

    }

    public ArrayList<Book> getBooksByAuthor(String author, DbConnection conn) {

        return getBooksByCriteria(new String[]{author}, SELECT_BOOK_BY_AUTHOR, conn);

    }

    public ArrayList<Book> getBooksByAuthorAndTitle(String author, String title, DbConnection conn) {
        return getBooksByCriteria(new String[]{author, title}, SELECT_BOOK_BY_AUTHOR_AND_TITLE, conn);
    }

    public ArrayList<SharedBook> getBooksByUserId(Long userId, DbConnection conn) {
        return getBooksByCriteria(userId, SELECT_BOOKS_BY_USERID, conn);

    }

    public ArrayList<SharedBook> getBorrowedBooksByUserId(Long userId, DbConnection conn) {
        return getBooksByCriteria(userId, SELECT_BORROWED_BOOKS_BY_USERID, conn);
    }

    public Book getBookById(long id, DbConnection conn) {

        Book book = null;

        try {
            PreparedStatement ps = conn.getPreparedStatement(SELECT_BOOK_BY_ID);

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                book = getBookFromRs(rs);
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return book;

    }

    public OperationResult insertSharedBook(Book book, long userId, DbConnection conn) {

        ArrayList<Book> books = getBooksByAuthorAndTitle(book.Author, book.Title, conn);
        Long bookId = -1L;
        if (books == null || books.size() < 1) {
            insertBook(book, conn);
            books = getBooksByAuthorAndTitle(book.Author, book.Title, conn);
        }
        bookId = books.get(0).Id;
        insertBookUsingQuery(INSERT_INTO_SHARED_BOOK, bookId, userId, conn);
        return OperationResult.SUCCESS;
    }

    public OperationResult insertBorrowedBook(Long sharedBookId, long userId, DbConnection conn) {

        //TODO: check whether record exists, it should be done by DB constraint
        insertBookUsingQuery(INSERT_INTO_BORROWED_BOOK, sharedBookId, userId, conn);
        return OperationResult.SUCCESS;
    }

    private void insertBook(Book book, DbConnection conn) {
        try {
            PreparedStatement ps = conn.getPreparedStatement(INSERT_INTO_BOOK);

            ps.setString(1, book.Title);
            ps.setString(2, book.Author);
            ps.setString(3, book.Genre);
//            ps.setString(4, book.Year);

            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void insertBookUsingQuery(String query, long bookId, long userId, DbConnection conn) {
        try {
            PreparedStatement ps = conn.getPreparedStatement(query);

            ps.setLong(1, userId);
            ps.setLong(2, bookId);

            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<Book> getBooksByCriteria(String[] criteria, String query, DbConnection conn) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            PreparedStatement ps = conn.getPreparedStatement(query);

            for (int i = 1; i <= criteria.length; i++) {
                ps.setString(i, criteria[i - 1]);
            }

            // ps.setString(1, criteria);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(getBookFromRs(rs));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return books;
    }

    private ArrayList<SharedBook> getBooksByCriteria(Long criteria, String query, DbConnection conn) {

        ArrayList<SharedBook> books = new ArrayList<>();

        try {
            PreparedStatement ps = conn.getPreparedStatement(query);

            ps.setLong(1, criteria);

            // ps.setString(1, criteria);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(getSharedBookFromRs(rs));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return books;
    }

    private Book getBookFromRs(ResultSet rs) {

        Book book = null;
        try {
            book = new Book();
            book.Id = rs.getLong("id");
            book.Author = rs.getString("author");
            book.Title = rs.getString("title");
            book.Genre = rs.getString("genre");

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return book;
    }

    private SharedBook getSharedBookFromRs(ResultSet rs) {

        SharedBook book = null;
        try {
            book = new SharedBook();
            book.book = new Book();
            book.Id = rs.getLong("sb.id");
            book.UserId = rs.getLong("user_id");
            book.BookId = rs.getLong("book_id");
            book.book.Id = rs.getLong("b.id");
            book.book.Author = rs.getString("author");
            book.book.Title = rs.getString("title");
            book.book.Genre = rs.getString("genre");

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return book;
    }

}
