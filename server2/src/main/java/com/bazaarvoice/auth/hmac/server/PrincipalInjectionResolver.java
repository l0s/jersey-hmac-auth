package com.bazaarvoice.auth.hmac.server;

import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;

/**
 * {@link org.glassfish.hk2.api.InjectionResolver InjectionResolver} for
 * injecting a {@link java.security.Principal Principal} wherever the
 * {@link HmacAuth} annotation is specified.
 *
 * @author Carlos Macasaet
 */
public class PrincipalInjectionResolver extends ParamInjectionResolver<HmacAuth> {

    public PrincipalInjectionResolver() {
        super(PrincipalValueFactoryProvider.class);
    }

}