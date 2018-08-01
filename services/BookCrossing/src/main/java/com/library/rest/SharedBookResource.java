/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.rest;

import com.library.dal.BookRepository;
import com.library.dal.DbConnection;
import com.library.dal.OperationResult;
import com.library.dto.SharedBook;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author IuraPC
 */
@Path("/shared-books")
public class SharedBookResource {

    @Path("/borrowed-books")
    public class BorrowedBookResource {

        private final DbConnection conn = new DbConnection();
        private final BookRepository repo = new BookRepository();

        @GET
        @Consumes(MediaType.APPLICATION_JSON)
        public Response borrowBook(@QueryParam("bookId") Long bookId, @Context SecurityContext sc) {

            ArrayList<SharedBook> books = repo.getSharedBooksByBookId(bookId, conn);

            Response response = Response.status(Response.Status.OK)
                    .entity(books)
                    .header("Access-Control-Allow-Origin", "*").build();

            return response;

        }

    }
}
