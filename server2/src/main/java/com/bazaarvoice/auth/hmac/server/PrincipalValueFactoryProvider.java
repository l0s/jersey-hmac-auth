package com.bazaarvoice.auth.hmac.server;

import static org.apache.commons.lang.Validate.notNull;
import static org.glassfish.jersey.server.model.Parameter.Source.UNKNOWN;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.glassfish.jersey.server.spi.internal.ValueFactoryProvider
 * ValueFactoryProvider} that makes a {@link PrincipalFactory} available to the
 * request if an {@link HmacAuth} annotation is present.
 *
 * @author Carlos Macasaet
 */
public class PrincipalValueFactoryProvider extends AbstractValueFactoryProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PrincipalFactory factory;

    @Inject
    public PrincipalValueFactoryProvider(final MultivaluedParameterExtractorProvider mpep,
            final ServiceLocator locator, final PrincipalFactory factory) {
        super(mpep, locator, UNKNOWN);
        notNull(factory, "factory cannot be null");
        this.factory = factory;
    }

    protected Factory<Principal> createValueFactory(final Parameter parameter) {
        final HmacAuth auth = parameter.getAnnotation(HmacAuth.class);
        if (auth != null) {
            final Class<?> parameterType = parameter.getRawType();
            // TODO support alternate "principal" types
            if (!Principal.class.isAssignableFrom(parameterType)) {
                logger.error(
                    "HmacAuth annotation on parameter not of type Principal, insetad on: {}", parameterType);
                throw new InternalServerErrorException();
            }
            return getFactory();
        }
        return null;
    }

    protected PrincipalFactory getFactory() {
        return factory;
    }

}