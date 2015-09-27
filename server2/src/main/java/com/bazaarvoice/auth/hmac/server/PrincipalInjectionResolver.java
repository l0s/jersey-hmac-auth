package com.bazaarvoice.auth.hmac.server;

import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.glassfish.hk2.api.InjectionResolver InjectionResolver} for
 * injecting a {@link java.security.Principal Principal} wherever the
 * {@link HmacAuth} annotation is specified.
 *
 * @author Carlos Macasaet
 */
public class PrincipalInjectionResolver extends ParamInjectionResolver<HmacAuth> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public PrincipalInjectionResolver() {
        super(PrincipalValueFactoryProvider.class);
        logger.debug("PrincipalInjectionResolver is ready");
    }

    public boolean isMethodParameterIndicator() {
        return true;
    }

}