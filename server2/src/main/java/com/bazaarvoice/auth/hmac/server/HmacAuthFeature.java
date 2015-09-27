package com.bazaarvoice.auth.hmac.server;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

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
                    .in(Singleton.class);
            bind(PrincipalValueFactoryProvider.class)
                    .to(PrincipalValueFactoryProvider.class)
                    .in(Singleton.class);
            bind(PrincipalInjectionResolver.class)
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