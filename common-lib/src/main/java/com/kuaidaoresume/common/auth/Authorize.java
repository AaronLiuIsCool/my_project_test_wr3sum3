package com.kuaidaoresume.common.auth;


import java.lang.annotation.*;

/**
 * Authorize annotation.
 *
 * @author Aaron Liu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {
    // allowed consumers
    String[] value();
}
