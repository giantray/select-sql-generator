package com.github.giantray.compositesSelectSql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * declare if generate sql should has start,size.
 * 
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlLimit {

	/**
	 * limit
	 * 
	 * @author lizeyang
	 *
	 */
	public enum Limit {
		/**
		 * declare the field show the start value
		 */
		START,
		/**
		 * declare the field show the size value
		 */
		SIZE
	};

	Limit value();
}
