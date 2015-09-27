package com.bazaarvoice.auth.hmac.sample.server;

import java.security.Principal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.common.Credentials;
import com.bazaarvoice.auth.hmac.server.Authenticator;

public class PizzaAuthenticator implements Authenticator<Principal> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Principal fred = new Principal() {
        public String getName() {
            return "fred";
        }

        public String toString() {
            final ToStringBuilder builder = new ToStringBuilder(this);
            builder.append("name", getName());
            return builder.toString();
        }
    };

    public PizzaAuthenticator() {
        logger.info("Authenticator is ready");
    }

    public Principal authenticate(Credentials credentials) {
        logger.info("Loading principal for: {}", credentials);
        if ("fred-api-key".equals(credentials.getApiKey())) {
            return fred;
        }
        return null;
    }

}