package com.bazaarvoice.auth.hmac.server;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Jersey 2.x JAX-RS REST application that demonstrates HMAC authentication.
 *
 * @author Carlos Macasaet
 */
public class PizzaApplication extends ResourceConfig {

    public PizzaApplication() {
        register(HmacAuthFeature.class);
        register(PizzaResource2.class);
    }

}