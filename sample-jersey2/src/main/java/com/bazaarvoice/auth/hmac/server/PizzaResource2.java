package com.bazaarvoice.auth.hmac.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.commons.lang.Validate.notNull;

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
        notNull(principal, "principal should not be null");
        logger.info("Baking a pizza for {}.", principal.getName());
    }

}