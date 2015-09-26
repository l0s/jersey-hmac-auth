package com.bazaarvoice.auth.hmac.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;

/**
 * Annotation to indicate that a given resource should be authenticated
 *
 * @author Carlos Macasaet
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface HmacProtect {
}