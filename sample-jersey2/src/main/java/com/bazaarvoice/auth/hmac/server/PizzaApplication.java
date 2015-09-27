package com.bazaarvoice.auth.hmac.server;

import static org.slf4j.bridge.SLF4JBridgeHandler.install;
import static org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jersey 2.x JAX-RS application that demonstrates HMAC authentication.
 *
 * @author Carlos Macasaet
 */
public class PizzaApplication extends ResourceConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PizzaApplication() {
        removeHandlersForRootLogger();
        install();

        logger.info("Registering features and resources");
        register(HmacAuthFeature.class);
        packages(getClass().getPackage().getName());
    }

}