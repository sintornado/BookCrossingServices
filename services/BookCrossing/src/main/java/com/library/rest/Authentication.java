/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.rest;

import com.library.dal.DbConnection;
import com.library.dal.IUserRepository;
import com.library.dto.AuthenticationResponse;
import com.library.dto.User;
import com.library.dto.UserCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author IuraPC
 */
@Singleton
@Path("auth-token")
public class Authentication {

    @Inject
    IUserRepository userRepo;

    DbConnection dbConn = new DbConnection();

    //TODO : provide proper injection here
    String signKey = "lksjhdflsdflsjdf";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials uc) {

        Response response = null;
        User user = userRepo.getUserByEmail(uc.UserName, dbConn);

        if (user != null && user.PasswordHash.equals(uc.Password)) {

            String compactJws = Jwts.builder()
                    .setSubject(Long.toString(user.Id))
                    .signWith(SignatureAlgorithm.HS512, signKey)
                    .compact();

            response = Response.status(Response.Status.OK)
                    .header("Access-Control-Allow-Origin", "*")
                    .entity(new AuthenticationResponse(compactJws))
                    .build();
        } else {
            response = Response.status(Response.Status.FORBIDDEN)
                    .build();
        }

        return response;

    }

}
