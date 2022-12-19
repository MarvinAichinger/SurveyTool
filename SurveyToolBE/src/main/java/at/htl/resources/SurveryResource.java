package at.htl.resources;

import at.htl.controller.SurveyController;
import at.htl.model.Survey;
import at.htl.websocket.SurveySocketServer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("api/survey")
public class SurveryResource {

    @Inject
    SurveyController controller;

    @Inject
    SurveySocketServer socket;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSurvey(Survey survey) {
        try {
            if (!survey.getQuestion().isEmpty() && !survey.getResult().isEmpty()) {
                controller.setSurvey(survey);
                socket.broadcast();
                return Response.status(Response.Status.CREATED).build();
            }else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/vote/{option}")
    public Response vote(@PathParam("option") String option) {
        if (controller.vote(option)) {
            socket.broadcast();
            return Response.accepted().build();
        }else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


}
