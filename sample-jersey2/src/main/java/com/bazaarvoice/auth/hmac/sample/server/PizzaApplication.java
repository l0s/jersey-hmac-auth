package com.bazaarvoice.auth.hmac.sample.server;

import static org.slf4j.bridge.SLF4JBridgeHandler.install;
import static org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger;

import java.security.Principal;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.server.Authenticator;
import com.bazaarvoice.auth.hmac.server.HmacAuthFeature;

/**
 * Jersey 2.x JAX-RS application that demonstrates HMAC authentication.
 *
 * @author Carlos Macasaet
 */
public class PizzaApplication extends ResourceConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Binder pizzaApplicationBinder = new AbstractBinder() {
        protected void configure() {
            bind(PizzaAuthenticator.class).to(new TypeLiteral<Authenticator<Principal>>() {});
        }
    };

    public PizzaApplication() {
        removeHandlersForRootLogger();
        install();

        logger.info("Registering features and resources");
        register(HmacAuthFeature.class);
        register(pizzaApplicationBinder);
        register(PizzaResource2.class);
    }

}