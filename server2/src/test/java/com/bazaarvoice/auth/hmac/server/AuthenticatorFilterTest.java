package com.bazaarvoice.auth.hmac.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import com.bazaarvoice.auth.hmac.common.Credentials;
import com.google.common.collect.ImmutableMultimap;

/**
 * Test class for {@link AuthenticatorFilter}.
 *
 * @author Carlos Macasaet
 */
public class AuthenticatorFilterTest {

    @Mock
    private Authenticator<Principal> authenticator;
    private AuthenticatorFilter filter;

    @Before
    public void setUp() {
        initMocks(this);

        filter = new AuthenticatorFilter(authenticator);
    }

    @Test
    public final void verifyFilterAbortsRequest() throws IOException, URISyntaxException {
        // given
        final String apiKey = "invalidApiKey";
        final UriInfo info = mockUriInfo(apiKey);

        final ContainerRequestContext context = mock(ContainerRequestContext.class);
        given(context.getUriInfo()).willReturn(info);
        given(context.getMethod()).willReturn("POST");
        given(context.getHeaderString("X-Auth-Signature")).willReturn("invalidSignature");
        given(context.getHeaderString("X-Auth-Timestamp")).willReturn("invalidTimestamp");
        given(context.getHeaderString("X-Auth-Version")).willReturn("1");

        given(authenticator.authenticate(any(Credentials.class))).willReturn(null);

        // when
        filter.filter(context);

        // then
        final ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(context).abortWith(responseCaptor.capture());
        final Response response = responseCaptor.getValue();
        assertEquals(401, response.getStatus());
    }

    @Test
    public final void verifyFilterSetsPrincipal() throws IOException, URISyntaxException {
        // given
        final UriInfo info = mockUriInfo("validApiKey");

        final ContainerRequestContext context = mock(ContainerRequestContext.class);
        given(context.getUriInfo()).willReturn(info);
        given(context.getMethod()).willReturn("POST");
        given(context.getHeaderString("X-Auth-Signature")).willReturn("validSignature");
        given(context.getHeaderString("X-Auth-Timestamp")).willReturn("validTimestamp");
        given(context.getHeaderString("X-Auth-Version")).willReturn("1");

        final Principal principal = mock(Principal.class);
        given(principal.getName()).willReturn("authenticatedUser");
        given(authenticator.authenticate(any(Credentials.class))).willReturn(principal);

        // when
        filter.filter(context);

        // then
        final ArgumentCaptor<SecurityContext> securityContextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
        verify(context).setSecurityContext(securityContextCaptor.capture());
        final SecurityContext securityContext = securityContextCaptor.getValue();
        assertEquals(principal, securityContext.getUserPrincipal());
    }

    protected UriInfo mockUriInfo(final String apiKey)
        throws URISyntaxException {
        ImmutableMultimap.of("apiKey", apiKey);
        final URI uri = new URI("http://www.example.com/path/to/resource?apiKey=" + apiKey);
        final MultivaluedMap<String, String> queryParameters = new MultivaluedStringMap();
        queryParameters.putSingle("apiKey", apiKey);

        final UriInfo retval = mock(UriInfo.class);
        given(retval.getRequestUri()).willReturn(uri);
        given(retval.getQueryParameters()).willReturn(queryParameters);
        return retval;
    }

}