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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.auth.hmac.common.Credentials.CredentialsBuilder;
import com.bazaarvoice.auth.hmac.common.Version;

/**
 * {@link Factory} for creating a {@link Principal} wherever it is required for a request.
 *
 * @see Authenticator
 * @author Carlos Macasaet
 */
public class PrincipalFactory
        extends AbstractContainerRequestValueFactory<Principal> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Authenticator<Principal> authenticator;

    /**
     * @param authenticator the application's credential authenticator (required)
     */
    @Inject
    public PrincipalFactory(final Authenticator<Principal> authenticator) {
        notNull(authenticator, "authenticator cannot be null");
        this.authenticator = authenticator;
        logger.debug("PrincipalFactory is ready");
    }

    public Principal provide() {
        logger.info( "Providing principal" );
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
            logger.debug( "Unauthorized request" );
            throw new NotAuthorizedException(status(UNAUTHORIZED).build());
        }
        logger.trace( "Authorized request" );
        return retval;
    }

    protected Authenticator<Principal> getAuthenticator() {
        return authenticator;
    }

}