/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.dal;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IuraPC
 */
public class DbConnection {

    private Connection conn;

    public DbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_crossing", "dev", "h9tbTzx6U2Ne");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_crossing", "root", "admin");
            conn.setAutoCommit(true);
        } catch (Exception ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException {

        return conn.prepareStatement(query);

    }

    public void flush() {
        try {
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close() {

        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
