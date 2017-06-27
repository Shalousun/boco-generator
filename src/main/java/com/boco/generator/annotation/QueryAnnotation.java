package com.boco.generator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field in a standard java bean
 * which should be as a query field in SQL statement
 *
 * @author sunyu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface QueryAnnotation {
    boolean isQuery() default true;
}
