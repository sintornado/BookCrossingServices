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
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
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
public class CorsFilter implements ContainerResponseFilter {

    static final Logger log = LogManager.getLogger(CorsFilter.class.getName());
    String signKey = "lksjhdflsdflsjdf";
    static final String AUTHENTICATION_SCHEME = "Bearer";
    static final String REALM = "Example";

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) throws IOException {
        crc1.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        crc1.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Authorization");

    }

}
