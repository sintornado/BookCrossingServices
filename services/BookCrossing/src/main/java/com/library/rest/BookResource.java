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
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Dima
 */
@Path("/books")
public class BookResource {

    private final DbConnection conn = new DbConnection();
    private final BookRepository repo = new BookRepository();

    @GET
    @Path("/title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookByTitle(@PathParam("title") String title) {

        BookRepository br = new BookRepository();

        ArrayList<Book> books = br.getBooksByTitle(title, conn);

        Response response = Response.status(Response.Status.OK)
                .entity(books)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;
    }

    @GET
    @Path("/author/{author}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookByAuthor(@PathParam("author") String author) {

        BookRepository br = new BookRepository();

        ArrayList<Book> books = br.getBooksByAuthor(author, conn);

        Response response = Response.status(Response.Status.OK)
                .entity(books)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;
    }

    @POST
    @Path("/")
    public Response insertBook(Book book) {
        OperationResult result = repo.insertBook(book, conn);

        if (result == OperationResult.SUCCESS) {
            return Response.status(Response.Status.CREATED)
                    .entity(book)
                    .header("Access-Control-Allow-Origin", "*").build();
        } else {
            return Response.status(Response.Status.fromStatusCode(500))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        }

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") long id) {

        Book book = repo.getBookById(id, conn);

        return Response.status(Response.Status.OK)
                .entity(book)
                .header("Access-Control-Allow-Origin", "*").build();

    }

}
