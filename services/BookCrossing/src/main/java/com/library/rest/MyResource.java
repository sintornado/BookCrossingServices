/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Dima
 */
@Path("myresource")
public class MyResource {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respond(){
        return "My resource response";
        
    }
    
}