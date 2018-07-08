package com.library.rest;

import com.library.dal.DbConnection;
import com.library.dal.OperationResult;
import com.library.dal.UserRepository;
import com.library.dto.User;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.grizzly.http.HttpContext;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("users")
public class UserResource {

    private static ArrayList<User> listUsers = new ArrayList<>();
    private UserRepository userRepo;
    private DbConnection dbConn;

    public UserResource() {
        dbConn = new DbConnection();
        userRepo = new UserRepository();
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "text/plain" media type.
     *
     * @param id
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") long id) {

        User user = userRepo.getUserById(id, dbConn);

        Response response = Response.status(Response.Status.OK)
                .entity(user)
                .header("Access-Control-Allow-Origin", "*").build();

        return response;

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User update(User user) {

        OperationResult result = userRepo.insertUser(user, dbConn);

        if (result == OperationResult.SUCCESS) {
            return user;
        } else {
            return null;

        }
    }
}
