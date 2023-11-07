package com.unzer.payment.util;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Integration annotation is used to filter integration tests (slow tests that communicate with PAPI).
 * <p>
 * See more:
 * <a href="https://junit.org/junit5/docs/current/user-guide/#writing-tests-meta-annotations">JUnit 5 | Meta annotations</a>
 * <a href="https://junit.org/junit5/docs/current/user-guide/#running-tests-build-maven-filter-tags">JUnit 5 | Filtering by Tags</a>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Tag("integration")
public @interface Integration {
}
