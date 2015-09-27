package com.bazaarvoice.auth.hmac.server;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.common.Credentials;

public class PizzaAuthenticator
        extends AbstractCachingAuthenticator<Principal> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Principal fred = new Principal() {
        public String getName() {
            return "fred";
        }
    };

    public PizzaAuthenticator() {
        super(5l, 1l, MINUTES, 1000l);
        logger.info("Authenticator is ready");
    }

    protected Principal loadPrincipal(Credentials credentials) {
        logger.info("Loading principal for: {}", credentials);
        if ("fred-api-key".equals(credentials.getApiKey())) {
            return fred;
        }
        return null;
    }

    protected String getSecretKeyFromPrincipal(Principal principal) {
        logger.info("Fetching shared secret for: {}", principal);
        if (principal != null) {
            if ("fred".equals(principal.getName())) {
                return "fred-secret-key";
            }
        }
        return null;
    }

}