package com.sinergy.chronosync.config.policy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition for enforcing CRUD policy.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnforcePolicy {

	Class<?> entity();

	CrudOperation operation();
}
