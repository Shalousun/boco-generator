package com.boco.generator.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface Table {
	/**
	 * 
	 * @return
	 */
	String name();
	/**
	 * description of table
	 * @return
	 */
	String desc() default "";
}
