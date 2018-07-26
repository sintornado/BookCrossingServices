package com.library.rest;

import com.library.dal.BookRepository;
import com.library.dal.DbConnection;
import com.library.dal.OperationResult;
import com.library.dto.Book;
import com.library.dto.SharedBook;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.OPTIONS;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author Dima
 */
@Path("/books")
public class BookResource {

    private final DbConnection conn = new DbConnection();
    private final BookRepository repo = new BookRepository();

//    @GET
//    @Path("/all")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllBooks() {
//
//        BookRepository br = new BookRepository();
//
//        ArrayList<Book> books = br.getAllBooks(conn);
//
//        Response response = Response.status(Response.Status.OK)
//                .entity(books)
//                .header("Access-Control-Allow-Origin", "*").build();
//
//        return response;
//    }
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

        ArrayList<Book> books = br.getBooksByTitle(author, conn);

        Response response = Response.status(Response.Status.OK)
                .entity(books)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertBook(Book book, @Context SecurityContext sc) {

        Long userId = Long.parseLong(sc.getUserPrincipal().getName());
        OperationResult result = repo.insertSharedBook(book, userId, conn);

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

    @OPTIONS
    @Path("/")
    public Response geOptions() {

        return Response.status(Response.Status.OK)
                .header("Allow", "POST,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*").build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksByUserId(@Context SecurityContext sc) {
        Long userId = Long.parseLong(sc.getUserPrincipal().getName());

        BookRepository br = new BookRepository();

        ArrayList<SharedBook> books = br.getBooksByUserId(userId, conn);

        Response response = Response.status(Response.Status.OK)
                .entity(books)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;
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
