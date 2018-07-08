/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.dal;

import com.library.dto.Book;
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
    private final String INSERT_INTO = "INSERT books (title, author, genre) VALUES (?, ?, ?)";
    private final String SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE id = ?";

    public ArrayList<Book> getBooksByTitle(String title, DbConnection conn) {

        return getBooksByCriteria(title, SELECT_BOOK_BY_TITLE, conn);

    }

    public ArrayList<Book> getBooksByAuthor(String author, DbConnection conn) {

        return getBooksByCriteria(author, SELECT_BOOK_BY_AUTHOR, conn);

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

    public OperationResult insertBook(Book book, DbConnection conn) {

        try {
            PreparedStatement ps = conn.getPreparedStatement(INSERT_INTO);

            ps.setString(1, book.Title);
            ps.setString(2, book.Author);
            ps.setString(3, book.Genre);

            ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return OperationResult.SUCCESS;

    }

    private ArrayList<Book> getBooksByCriteria(String criteria, String query, DbConnection conn) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            PreparedStatement ps = conn.getPreparedStatement(query);

            ps.setString(1, criteria);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(getBookFromRs(rs));
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

}
