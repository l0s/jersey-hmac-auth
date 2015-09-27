package com.bazaarvoice.auth.hmac.server;

import static com.bazaarvoice.auth.hmac.common.Credentials.builder;
import static com.google.common.base.Preconditions.checkArgument;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.apache.commons.lang.Validate.notNull;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;

import com.bazaarvoice.auth.hmac.common.Credentials.CredentialsBuilder;
import com.bazaarvoice.auth.hmac.common.Version;

/**
 * {@link Factory} for creating a {@link Principal} from the request.
 *
 * @see Authenticator
 * @author Carlos Macasaet
 */
public class PrincipalFactory
        extends AbstractContainerRequestValueFactory<Principal> {

    private final Authenticator<Principal> authenticator;

    /**
     * @param authenticator the application's credential authenticator (required)
     */
    @Inject
    public PrincipalFactory(final Authenticator<Principal> authenticator) {
        notNull(authenticator, "authenticator cannot be null");
        this.authenticator = authenticator;
    }

    public Principal provide() {
        final ContainerRequest request = getContainerRequest();
        final UriInfo uriInfo = request.getUriInfo();
        final URI requestUri = uriInfo.getRequestUri();

        final MultivaluedMap<? super String, ? extends String> queryParameters = uriInfo
                .getQueryParameters();
        final List<? extends String> apiKeys = queryParameters.get("apiKey");
        checkArgument(!apiKeys.isEmpty(), "apiKey is required");

        final CredentialsBuilder builder = builder();
        builder.withApiKey(!apiKeys.isEmpty() ? apiKeys.get(0) : null);
        builder.withSignature(request.getHeaderString("X-Auth-Signature"));
        builder.withTimestamp(request.getHeaderString("X-Auth-Timestamp"));
        builder.withVersion(
                Version.fromValue(request.getHeaderString("X-Auth-Version")));
        builder.withMethod(request.getMethod());
        builder.withPath(requestUri.getPath() + "?" + requestUri.getQuery());

        final Principal retval = getAuthenticator()
                .authenticate(builder.build());
        if (retval == null) {
            throw new NotAuthorizedException(status(UNAUTHORIZED).build());
        }
        return retval;
    }

    protected Authenticator<Principal> getAuthenticator() {
        return authenticator;
    }

}