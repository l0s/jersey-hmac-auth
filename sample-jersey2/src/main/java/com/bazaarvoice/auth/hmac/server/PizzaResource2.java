package com.bazaarvoice.auth.hmac.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.security.Principal;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jersey 2.x HMAC-authenticated REST resource
 *
 * @author Carlos Macasaet
 */
@Path("/pizza")
@Produces(APPLICATION_JSON)
public class PizzaResource2 {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PizzaResource2() {
        logger.info("Resource is ready");
    }

    @POST
    public void bakePizza(@HmacAuth final Principal principal) {
        /*
         * FIXME principal being provided after this method is called...
         * c.b.auth.hmac.server.PizzaResource2 - Resource is ready
         * c.b.auth.hmac.server.PizzaResource2 - Baking a pizza for null.
         * c.b.a.hmac.server.PrincipalFactory - Providing principal
         */
        logger.info("Baking a pizza for {}.", principal);
    }

}