package com.bazaarvoice.auth.hmac.sample.server;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.security.Principal;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.server.HmacAuth;

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
         * FIXME method is being invoked twice, the first time without the principal
         * INFO  c.b.a.h.sample.server.PizzaResource2 - Resource is ready
         * INFO  c.b.a.h.sample.server.PizzaResource2 - Baking a pizza for null.
         * INFO  c.b.a.hmac.server.PrincipalFactory - Providing principal
         * INFO  c.b.a.h.sample.server.PizzaResource2 - Baking a pizza for com.bazaarvoice.auth.hmac.sample.server.PizzaAuthenticator$1@3e738a74[name=fred].
         */
        logger.info("Baking a pizza for {}.", principal);
    }

}