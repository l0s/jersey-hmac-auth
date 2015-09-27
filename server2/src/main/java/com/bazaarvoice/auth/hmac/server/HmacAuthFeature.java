package com.bazaarvoice.auth.hmac.server;

import java.security.Principal;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

/**
 * JAX-RS {@link Feature} to enable HMAC authentication on methods with the
 * {@link HmacAuth} annotation.
 *
 * @author Carlos Macasaet
 */
public class HmacAuthFeature implements Feature {

    private final Binder binder = new AbstractBinder() {
        protected void configure() {
            bind(PrincipalFactory.class)
                    .to(PrincipalFactory.class)
                    .to(new TypeLiteral<Factory<Principal>>() {})
                    .in(Singleton.class);
            bind(PrincipalValueFactoryProvider.class)
                    .to(AbstractValueFactoryProvider.class)
                    .to(ValueFactoryProvider.class)
                    .in(Singleton.class);
            bind(PrincipalInjectionResolver.class)
                    .to(new TypeLiteral<ParamInjectionResolver<HmacAuth>>() {})
                    .to(new TypeLiteral<InjectionResolver<HmacAuth>>() {})
                    .in(Singleton.class);
        }
    };

    public boolean configure(final FeatureContext context) {
        context.register(getBinder());
        return true;
    }

    protected Binder getBinder() {
        return binder;
    }

}