package com.bazaarvoice.auth.hmac.sample.server;

import java.security.Principal;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.bazaarvoice.auth.hmac.server.Authenticator;
import com.bazaarvoice.auth.hmac.server.HmacAuthFeature;

/**
 * Jersey 2.x JAX-RS application that demonstrates HMAC authentication.
 *
 * @author Carlos Macasaet
 */
public class PizzaApplication extends ResourceConfig {

    private final Binder pizzaApplicationBinder = new AbstractBinder() {
        protected void configure() {
            bind(PizzaAuthenticator.class).to(new TypeLiteral<Authenticator<Principal>>() {});
        }
    };

    public PizzaApplication() {
        register(HmacAuthFeature.class);
        register(getPizzaApplicationBinder());
        register(PizzaResource2.class);
    }

    protected Binder getPizzaApplicationBinder() {
        return pizzaApplicationBinder;
    }

}