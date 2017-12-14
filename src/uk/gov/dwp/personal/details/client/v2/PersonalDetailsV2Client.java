package uk.gov.dwp.personal.details.client.v2;

import uk.gov.dwp.personal.details.type.PersonalDetailsId;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v2/personal-details")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PersonalDetailsV2Client {

    @GET
    @Path("{personalDetailsId}")
    PersonalDetailsV2Response findById(@PathParam("personalDetailsId") PersonalDetailsId personalDetailsId);

    @POST
    void create(CreatePersonalDetailsV2Request createPersonalDetailsV2Request);

    @PUT
    void update(UpdatePersonalDetailsV2Request updatePersonalDetailsV2Request);

    @DELETE
    @Path("{personalDetailsId}")
    void delete(@PathParam("personalDetailsId") PersonalDetailsId personalDetailsId);
}
