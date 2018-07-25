/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.dal;

import com.library.dto.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author IuraPC
 */
public class UserRepository implements IUserRepository {

    private final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id= ?";
    private final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email= ?";
    private final String INSERT_INTO = "INSERT users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

    public User getUserById(long id, DbConnection conn) {
        User user = null;

        try {
            PreparedStatement ps = conn.getPreparedStatement(SELECT_USER_BY_ID);
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                user = getUserFromRs(rs);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public User getUserByEmail(String email, DbConnection conn) {
        User user = null;

        try {
            PreparedStatement ps = conn.getPreparedStatement(SELECT_USER_BY_EMAIL);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs != null && rs.next()) {
                user = getUserFromRs(rs);

            }
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public OperationResult insertUser(User user, DbConnection conn) {
        try {
            PreparedStatement ps = conn.getPreparedStatement(INSERT_INTO);

            ps.setString(1, user.FirstName);
            ps.setString(2, user.LastName);
            ps.setString(3, user.Email);
            ps.setString(4, user.PasswordHash);

            boolean result = ps.execute();

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return OperationResult.SUCCESS;

    }

    private User getUserFromRs(ResultSet rs) {

        User user = null;
        try {
            user = new User();
            user.Id = rs.getInt("id");
            user.FirstName = rs.getString("first_name");
            user.LastName = rs.getString("last_name");
            user.Email = rs.getString("email");
            user.PasswordHash = rs.getString("password");
        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;

    }

}
