/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.library.rest;

import com.library.dal.BookRepository;
import com.library.dal.DbConnection;
import com.library.dal.OperationResult;
import com.library.dto.Book;
import com.library.dto.SharedBook;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Path("/borrowed-books")
public class BorrowedBookResource {

    private final DbConnection conn = new DbConnection();
    private final BookRepository repo = new BookRepository();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response borrowBook(@QueryParam("sharedBookId") Long bookId, @Context SecurityContext sc) {

        Long userId = Long.parseLong(sc.getUserPrincipal().getName());
        OperationResult result = repo.insertBorrowedBook(bookId, userId, conn);

        if (result == OperationResult.SUCCESS) {
            return Response.status(Response.Status.CREATED)
                    .entity(null)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } else {
            return Response.status(Response.Status.fromStatusCode(500))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }

    }

    @OPTIONS
    @Path("/")
    public Response getOptions() {

        return Response.status(Response.Status.OK)
                .header("Allow", "POST,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*").build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksBorrowedByUser(@Context SecurityContext sc) {
        Long userId = Long.parseLong(sc.getUserPrincipal().getName());

        BookRepository br = new BookRepository();

        ArrayList<SharedBook> books = br.getBorrowedBooksByUserId(userId, conn);

        Response response = Response.status(Response.Status.OK)
                .entity(books)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;
    }

}
