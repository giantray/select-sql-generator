package com.github.giantray.compositesSelectSql;

import java.lang.reflect.Field;
import java.util.Date;

import com.github.giantray.compositesSelectSql.SqlLimit.Limit;
import com.github.giantray.compositesSelectSql.SqlOper.Oper;
import com.github.giantray.compositesSelectSql.SqlOrderType.Order;

/**
 * generate select sql by model with certain annotation
 * 
 * @author lizeyang
 *
 */
public class SqlGenerator {

	/**
	 * generate select sql by query(which has SqlGenerator annotation model)
	 * 
	 * @param query
	 * @return
	 */
	public static final SelectSql getSelectWhere(Object query) {
		SqlStr sqlStr = new SqlStr();

		// the whole method is design by Reflection
		Class<?> clazz = query.getClass();

		Field[] fields = clazz.getDeclaredFields();

		if (clazz.isAnnotationPresent(SqlTable.class)) {
			sqlStr.setTable(clazz.getAnnotation(SqlTable.class).value());
		}

		if (sqlStr.getTable() == null || sqlStr.getTable().trim().length() == 0) {
			throw new IllegalArgumentException("the para'class should has SqlTable annotation at the class name ");
		}

		try {
			for (Field field : fields) {
				field.setAccessible(true);

				// join th where condition
				if (field.isAnnotationPresent(SqlOper.class)) {
					setWhere(query, sqlStr, field);
				}
				// join the limit condition
				else if (field.isAnnotationPresent(SqlLimit.class)) {
					setLimit(query, sqlStr, field);
				}
				// join the oder type
				else if (field.isAnnotationPresent(SqlOrderType.class)) {
					setOrderType(query, sqlStr, field);
				}
				// join the order col
				else if (field.isAnnotationPresent(SqlOrderCol.class)) {
					setOrderCol(query, sqlStr, field);
				}

			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("reflection para is invalid");
		}

		return getSelectSql(sqlStr);
	}

	/**
	 * set order col.
	 * 
	 * @param o
	 * @param sqlStr
	 * @param field
	 * @throws IllegalAccessException
	 */
	private static void setOrderCol(Object o, SqlStr sqlStr, Field field) throws IllegalAccessException {
		String col = (String) field.get(o);
		if (!isBlank(col)) {
			sqlStr.setOrderCol((String) field.get(o));
		}
	}

	/**
	 * set order type(desc or asc)
	 * 
	 * @param o
	 * @param sqlStr
	 * @param field
	 * @throws IllegalAccessException
	 */
	private static void setOrderType(Object o, SqlStr sqlStr, Field field) throws IllegalAccessException {
		Object fieldVal = field.get(o);
		if (fieldVal instanceof String) {
			String order = (String) fieldVal;
			if (null != order) {
				if ("desc".equalsIgnoreCase(order.trim())) {
					sqlStr.setOrderType(" desc ");
				} else if ("asc".equalsIgnoreCase(order.trim())) {
					sqlStr.setOrderType(" asc ");
				}
			}
		} else if (fieldVal instanceof Order) {
			Order order = (Order) fieldVal;
			if (null != order) {
				if (order.equals(Order.ASC)) {
					sqlStr.setOrderType(" asc ");
				} else if (order.equals(Order.DESC)) {
					sqlStr.setOrderType(" desc ");
				}
			}
		} else {
			throw new ClassCastException("order type should be a string type ");
		}

	}

	/**
	 * set limit(eg.sql may has 'limt 0,1')
	 * 
	 * @param o
	 * @param sqlStr
	 * @param field
	 * @throws IllegalAccessException
	 */
	private static void setLimit(Object o, SqlStr sqlStr, Field field) throws IllegalAccessException {
		SqlLimit limitPara = field.getAnnotation(SqlLimit.class);
		if (Limit.START == limitPara.value()) {
			sqlStr.setStart((Integer) field.get(o));
		} else if (Limit.SIZE == limitPara.value()) {
			sqlStr.setSize((Integer) field.get(o));
		}
	}

	private static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * set where condition
	 * 
	 * @param o
	 * @param sqlStr
	 * @param field
	 * @throws IllegalAccessException
	 */
	private static void setWhere(Object o, SqlStr sqlStr, Field field) throws IllegalAccessException {

		String where = sqlStr.getWhere();

		SqlOper oper = (SqlOper) field.getAnnotation(SqlOper.class);
		Object fieldValue = field.get(o);
		String fieldName = toLowerCase(field.toString().substring(field.toString().lastIndexOf(".") + 1));
		// if the field value is null(which mean the code dose not set value to
		// it).then ignore the field
		if (null == fieldValue) {
			return;
		}
		if (fieldValue instanceof String && isBlank((String) fieldValue)) {
			return;
		}

		if (StringUtil.isNotBlank(where)) {
			where += " and ";
		}

		if (fieldValue instanceof String) {

			fieldValue = String.valueOf(fieldValue).replaceAll("([';])+|(--)+", "");
		}

		// format the value
		if (Oper.LIKE != oper.value()) {
			if (fieldValue instanceof String) {
				fieldValue = "'" + fieldValue + "'";
			} else if (fieldValue instanceof Date) {
				fieldValue = "'" + TimeUtil.dateToStr((Date) fieldValue, TimeUtil.TYPE_YYYY_MM_DD_HH_MM_SS) + "'";
			}
		}

		// add operator
		if (Oper.EQUAL == oper.value()) {
			where += fieldName + " = " + fieldValue + " ";
		} else if (Oper.LIKE == oper.value()) {
			where += fieldName + " like '" + fieldValue + "'";
		} else if (Oper.GREATER == oper.value()) {
			where += fieldName + " > " + fieldValue + " ";
		} else if (Oper.GREATEREQUAL == oper.value()) {
			where += fieldName + " >= " + fieldValue + " ";
		} else if (Oper.LESS == oper.value()) {
			where += fieldName + " < " + fieldValue + " ";
		} else if (Oper.LESSEQUAL == oper.value()) {
			where += fieldName + " <= " + fieldValue + " ";
		}

		sqlStr.setWhere(where);
	}

	protected static SelectSql getSelectSql(SqlStr str) {
		SelectSql sql = new SelectSql();
		Integer start = str.getStart();
		Integer size = str.getSize();
		String limit = str.getLimit();
		String table = str.getTable();
		String orderCol = str.getOrderCol();
		String order = str.getOrder();
		String orderType = str.getOrderType();
		String where = str.getWhere();

		if ((start == null || start <= 0) && (size == null || size <= 0)) {
			// 参数为空或者为0，不使用limit
			limit = "";
		} else {
			limit = " limit " + start + "," + size;
		}

		if (null != orderCol && orderCol.trim().length() != 0) {
			order = " order by " + orderCol + " " + orderType;
		}

		if (StringUtil.isNotBlank(where)) {
			where = " where " + where + " ";
		}

		sql.setSelectAll("select * from " + table + where + order + limit);
		sql.setSelectCount("select  count(1) from " + table + where);
		sql.setLimit(limit);
		sql.setOrder(order);
		sql.setWhere(where);

		return sql;
	}

	/**
	 * conver camelCase to lower_case
	 * 
	 * @param s
	 * @return
	 */
	private static String toLowerCase(String s) {
		if (null == s) {
			return null;
		}

		String regex = "([a-z0-9])([A-Z])";
		String replacement = "$1_$2";

		return s.replaceAll(regex, replacement).toLowerCase(); // prints
	}

	public static class SqlStr {
		String where = "";
		String limit = "";
		String order = "";
		String orderCol = "";
		String orderType = "";
		String table = "";
		Integer start = 0;
		Integer size = 0;

		public String getWhere() {
			return where;
		}

		public void setWhere(String where) {
			this.where = where;
		}

		public String getLimit() {
			return limit;
		}

		public void setLimit(String limit) {
			this.limit = limit;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

		public String getOrderCol() {
			return orderCol;
		}

		public void setOrderCol(String orderCol) {
			this.orderCol = orderCol;
		}

		public String getOrderType() {
			return orderType;
		}

		public void setOrderType(String orderType) {
			this.orderType = orderType;
		}

		public String getTable() {
			return table;
		}

		public void setTable(String table) {
			this.table = table;
		}

		public Integer getStart() {
			return start;
		}

		public void setStart(Integer start) {
			this.start = start;
		}

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

	}
}
