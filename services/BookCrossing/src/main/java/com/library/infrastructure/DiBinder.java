/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.infrastructure;

import com.library.dal.IUserRepository;
import com.library.dal.UserRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author IuraPC
 */
public class DiBinder extends AbstractBinder {

    @Override
    protected void configure() {

        bind(UserRepository.class).to(IUserRepository.class);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
