package com.bazaarvoice.auth.hmac.server;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.security.Principal;

import com.bazaarvoice.auth.hmac.common.Credentials;

public class PizzaAuthenticator
        extends AbstractCachingAuthenticator<Principal> {

    private final Principal fred = new Principal() {
        public String getName() {
            return "fred";
        }
    };

    public PizzaAuthenticator() {
        super(5l, 1l, MINUTES, 1000l);
    }

    protected Principal loadPrincipal(Credentials credentials) {
        if ("fred-api-key".equals(credentials.getApiKey())) {
            return fred;
        }
        return null;
    }

    protected String getSecretKeyFromPrincipal(Principal principal) {
        if (principal != null) {
            if ("fred".equals(principal.getName())) {
                return "fred-secret-key";
            }
        }
        return null;
    }

}