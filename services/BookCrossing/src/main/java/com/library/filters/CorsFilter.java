/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.filters;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import org.apache.logging.log4j.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author IuraPC
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    static final Logger log = LogManager.getLogger(AuthorizationFilter.class.getName());
    String signKey = "lksjhdflsdflsjdf";
    static final String AUTHENTICATION_SCHEME = "Bearer";
    static final String REALM = "Example";

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {

        String authHeader = crc.getHeaderString("Authorization");
        log.debug("Got request with next auth data : " + ((authHeader != null) ? authHeader : ""));

        //TODO: Make proper injection of secured and non-secured resources
        if (!crc.getUriInfo().getAbsolutePath().getPath().contains("auth-token")) {
            try {
                //TODO: Add check for Bearer JWT type of authentication

                //Get user and pass it in security context
                String subject = Jwts.parser()
                        .setSigningKey(signKey)
                        .parseClaimsJws(authHeader.split(" ")[1])
                        .getBody().getSubject();

                crc.setSecurityContext(new SecurityContext() {

                    public Principal getUserPrincipal() {
                        return new Principal() {
                            @Override
                            public String getName() {
                                return subject;
                            }
                        };
                    }

                    @Override
                    public boolean isUserInRole(String string) {
                        //TODO: replace if / when we will have userRoles
                        return true;
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean isSecure() {
                        return uriInfo.getAbsolutePath().toString().startsWith("https");
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return AUTHENTICATION_SCHEME;
                    }

                });

            } catch (SignatureException e) {
                crc.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + "realm=\"" + REALM + "\"")
                        .build());

            } catch (Exception e) {
                crc.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + "realm=\"" + REALM + "\"")
                        .build());
                log.error(e);

            }
        }
    }

}
