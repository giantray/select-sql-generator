package com.github.giantray.compositesSelectSql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare if generate sql should has desc or asc order
 * 
 * @author lizeyang
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlOrderType {

	public enum Order {
		/**
		 * When a group of things is listed or arranged in descending order,
		 * each thing is smaller or less important than the thing before it.
		 */
		DESC,
		/**
		 * If a group of things is arranged in ascending order, each thing is
		 * bigger, greater, or more important than the thing before it.
		 */
		ASC
	};

}
