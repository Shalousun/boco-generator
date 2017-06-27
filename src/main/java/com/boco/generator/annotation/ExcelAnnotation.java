package com.boco.generator.annotation;

/**
 * @author sunyu
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a field which should be export into excel and define
 * a name with marked field as export name in excel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface ExcelAnnotation {
	/**
	 * the name of export to excel
	 * 
	 * @return
	 */
	String name();
	/**
	 * 
	 * @return
	 */
	boolean isExport() default true;
	/**
	 * set cell width
	 * 
	 * @return
	 */
	int width() default 4000;
	/**
	 * set number of column
	 * 
	 * @return
	 */
	int columnNum();
	/**
	 * 
	 * @return
	 */
	boolean isSum() default false;

	/**
	 * format time
	 * @return
     */
	String formatTime() default "";
	/**
	 * form easy poi
	 * value replace {a_id,b_id}
	 */
	String[] replace() default {};
}
