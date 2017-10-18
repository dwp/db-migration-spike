package uk.gov.dwp.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/personal-details")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PersonalDetailsClient {

    @GET
    @Path("{personalDetailsId}")
    PersonalDetails findById(@PathParam("personalDetailsId") PersonalDetailsId personalDetailsId);

    @POST
    void create(PersonalDetails personalDetails);

    @PUT
    void update(PersonalDetails personalDetails);

    @DELETE
    @Path("{personalDetailsId}")
    void delete(PersonalDetailsId personalDetailsId);
}
