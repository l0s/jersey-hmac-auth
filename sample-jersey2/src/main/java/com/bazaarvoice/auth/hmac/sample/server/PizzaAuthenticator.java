package com.bazaarvoice.auth.hmac.sample.server;

import java.security.Principal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.bazaarvoice.auth.hmac.common.Credentials;
import com.bazaarvoice.auth.hmac.server.Authenticator;

/**
 * Dummy {@link Authenticator} implementation that just checks for the apiKey "fred-api-key".
 *
 * @author Carlos Macasaet
 */
public class PizzaAuthenticator implements Authenticator<Principal> {

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

    public Principal authenticate(final Credentials credentials) {
        if ("fred-api-key".equals(credentials.getApiKey())) {
            return fred;
        }
        return null;
    }

}