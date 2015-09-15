package com.github.giantray.compositesSelectSql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare the field operator when it convert to sql foramt
 * 
 * @author lizeyang
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlOper {

	/**
	 * operator
	 *
	 */
	public enum Oper {
		/**
		 * means "="
		 */
		EQUAL,
		/**
		 * means "like"
		 */
		LIKE,
		/**
		 * means ">"
		 */
		GREATER,
		/**
		 * means ">="
		 */
		GREATEREQUAL,
/**
		 *  means "<"
		 */
		LESS,
		/**
		 * means "<="
		 */
		LESSEQUAL

	};

	Oper value();
}
