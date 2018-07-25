/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.dal;

import com.library.dto.User;

/**
 *
 * @author IuraPC
 */
public interface IUserRepository {

    User getUserByEmail(String email, DbConnection conn);

}
