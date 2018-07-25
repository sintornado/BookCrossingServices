/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import org.apache.logging.log4j.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author IuraPC
 */
@Provider
public class AuthorizationFilter implements ContainerRequestFilter {

    static final Logger log = LogManager.getLogger(AuthorizationFilter.class.getName());
    String signKey = "lksjhdflsdflsjdf";

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {

        String authHeader = crc.getHeaderString("Authorization");
        log.debug("Got request with next auth data : " + ((authHeader != null) ? authHeader : ""));

        //TODO: Make proper injection of secured and non-secured resources
        if (!crc.getUriInfo().getAbsolutePath().getPath().contains("auth-token")) {
            try {
                Jwts.parser().setSigningKey(signKey).parseClaimsJws(authHeader);
            } catch (SignatureException e) {

            }
        }
    }

}
