package com.bazaarvoice.auth.hmac.server;

import static com.bazaarvoice.auth.hmac.common.Credentials.builder;
import static com.google.common.base.Preconditions.checkArgument;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.apache.commons.lang.Validate.notNull;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.bazaarvoice.auth.hmac.common.Credentials.CredentialsBuilder;
import com.bazaarvoice.auth.hmac.common.Version;

/**
 * Jersey 2.x filter to implement HMAC-256 authentication
 *
 * @author Carlos Macasaet
 */
@HmacProtect
public class AuthenticatorFilter implements ContainerRequestFilter {

    private final Authenticator<Principal> authenticator;

    @Inject
    public AuthenticatorFilter(final Authenticator<Principal> authenticator) {
        notNull(authenticator, "authenticator cannot be null");
        this.authenticator = authenticator;
    }

    public void filter(final ContainerRequestContext context)
        throws IOException {
        final UriInfo uriInfo = context.getUriInfo();
        final URI requestUri = uriInfo.getRequestUri();

        final MultivaluedMap<? super String, ? extends String> queryParameters = uriInfo.getQueryParameters();
        final List<? extends String> apiKeys = queryParameters.get("apiKey");
        checkArgument(!apiKeys.isEmpty(), "apiKey is required");

        final CredentialsBuilder builder = builder();
        builder.withApiKey(!apiKeys.isEmpty() ? apiKeys.get(0) : null);
        builder.withSignature(context.getHeaderString("X-Auth-Signature"));
        builder.withTimestamp(context.getHeaderString("X-Auth-Timestamp"));
        builder.withVersion(
                Version.fromValue(context.getHeaderString("X-Auth-Version")));
        builder.withMethod(context.getMethod());
        builder.withPath(requestUri.getPath() + "?" + requestUri.getQuery());

        final Principal principal = getAuthenticator()
                .authenticate(builder.build());
        if (principal == null) {
            final ResponseBuilder statusBuilder = status(UNAUTHORIZED);
            context.abortWith(statusBuilder.build());
        }
        final SecurityContext delegateContext = context.getSecurityContext();
        final SecurityContext securityContext = new SecurityContext() {
            public boolean isUserInRole(final String role) {
                return delegateContext != null ? delegateContext.isUserInRole(role) : false;
            }
            public boolean isSecure() {
                return delegateContext != null ? delegateContext.isSecure() : null;
            }
            public Principal getUserPrincipal() {
                return principal;
            }
            public String getAuthenticationScheme() {
                return DIGEST_AUTH;
            }
        };
        context.setSecurityContext(securityContext);
    }

    protected Authenticator<Principal> getAuthenticator() {
        return authenticator;
    }

}