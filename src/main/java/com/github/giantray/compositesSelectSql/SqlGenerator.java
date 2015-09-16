package com.github.giantray.compositesSelectSql;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
	 * @throws SQLException
	 */
	public static final SelectSql getSelectWhere(Object query) {
		SqlTempStr sqlStr = new SqlTempStr();

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
	private static void setOrderCol(Object o, SqlTempStr sqlStr, Field field) throws IllegalAccessException {
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
	private static void setOrderType(Object o, SqlTempStr sqlStr, Field field) throws IllegalAccessException {
		Object fieldVal = field.get(o);
		if (null != fieldVal) {
			if (fieldVal instanceof String) {
				String order = (String) fieldVal;
				if ("desc".equalsIgnoreCase(order.trim())) {
					sqlStr.setOrderType(" desc ");
				} else if ("asc".equalsIgnoreCase(order.trim())) {
					sqlStr.setOrderType(" asc ");
				}
			} else if (fieldVal instanceof Order) {
				Order order = (Order) fieldVal;
				if (order.equals(Order.ASC)) {
					sqlStr.setOrderType(" asc ");
				} else if (order.equals(Order.DESC)) {
					sqlStr.setOrderType(" desc ");
				}
			} else {
				throw new ClassCastException("order type should be a string type ");
			}
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
	private static void setLimit(Object o, SqlTempStr sqlStr, Field field) throws IllegalAccessException {
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
	 * @throws SQLException
	 */
	private static void setWhere(Object o, SqlTempStr sqlStr, Field field) throws IllegalAccessException {

		String where = sqlStr.getWhere();
		String whereForpreparedstatement = sqlStr.getWhereForPreparedstatement();

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

		sqlStr.getWhereParaListForPreparedstatement().add(fieldValue);

		if (StringUtil.isNotBlank(where)) {
			where += " and ";
			whereForpreparedstatement += " and ";
		}

		// add operator
		String operStr = "";
		if (Oper.EQUAL == oper.value()) {
			operStr = "=";
		} else if (Oper.LIKE == oper.value()) {
			operStr = "like";
		} else if (Oper.GREATER == oper.value()) {
			operStr = ">";
		} else if (Oper.GREATEREQUAL == oper.value()) {
			operStr = ">=";
		} else if (Oper.LESS == oper.value()) {
			operStr = "<";
		} else if (Oper.LESSEQUAL == oper.value()) {
			operStr = "<=";
		}

		where += fieldName + " " + operStr + " " + getFieldValueForWhere(oper, fieldValue) + " ";
		whereForpreparedstatement += fieldName + " " + operStr + " ? ";

		sqlStr.setWhere(where);
		sqlStr.setWhereForPreparedstatement(whereForpreparedstatement);

	}

	public static void setPreparedstatementPara(PreparedStatement preparedstatement, List<Object> paras)
			throws SQLException {
		if (null != preparedstatement && null != paras && paras.size() > 0) {

			for (int i = 0; i < paras.size(); i++) {
				Object fieldValue = paras.get(i);
				int setIndex = i + 1;
				if (fieldValue == String.class) {
					preparedstatement.setString(setIndex, (String) fieldValue);
				} else if (fieldValue instanceof Integer) {
					preparedstatement.setInt(setIndex, (Integer) fieldValue);
				} else if (fieldValue instanceof Long) {
					preparedstatement.setLong(setIndex, (Long) fieldValue);
				} else if (fieldValue instanceof Float) {
					preparedstatement.setFloat(setIndex, (Float) fieldValue);
				} else if (fieldValue instanceof Double) {
					preparedstatement.setDouble(setIndex, (Double) fieldValue);
				} else if (fieldValue instanceof Boolean) {
					preparedstatement.setBoolean(setIndex, (Boolean) fieldValue);
				} else if (fieldValue instanceof Date) {
					if (fieldValue instanceof Date) {
						fieldValue = TimeUtil.dateToStr((Date) fieldValue, TimeUtil.TYPE_YYYY_MM_DD_HH_MM_SS);
					}
					preparedstatement.setString(setIndex, (String) fieldValue);
				} else if (fieldValue instanceof Timestamp) {
					preparedstatement.setTimestamp(setIndex, (java.sql.Timestamp) fieldValue);
				} else {
					preparedstatement.setObject(setIndex, fieldValue);
				}
			}

		}

	}

	private static Object getFieldValueForWhere(SqlOper oper, Object fieldValue) {
		if (fieldValue instanceof Date) {
			fieldValue = TimeUtil.dateToStr((Date) fieldValue, TimeUtil.TYPE_YYYY_MM_DD_HH_MM_SS);
		}

		if (fieldValue instanceof String) {
			fieldValue = String.valueOf(fieldValue).replaceAll("([';])+|(--)+", "");
		}

		if (Oper.LIKE == oper.value()) {
			fieldValue = "'" + fieldValue + "'";
		} else {
			if (fieldValue instanceof String || fieldValue instanceof Date) {
				fieldValue = "'" + fieldValue + "'";
			}
		}
		return fieldValue;
	}

	protected static SelectSql getSelectSql(SqlTempStr str) {
		SelectSql sql = new SelectSql();
		Integer start = str.getStart();
		Integer size = str.getSize();
		String limit = str.getLimit();
		String table = str.getTable();
		String orderCol = str.getOrderCol();
		String order = str.getOrder();
		String orderType = str.getOrderType();
		String where = str.getWhere();
		String whereForPreparedstatement = str.getWhereForPreparedstatement();

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
			whereForPreparedstatement = " where " + whereForPreparedstatement + " ";
		}

		sql.setSelectAll("select * from " + table + where + order + limit);
		sql.setSelectAllForPreparedstatement("select * from " + table + whereForPreparedstatement + order + limit);
		sql.setSelectCount("select  count(1) from " + table + where);
		sql.setSelectCountForPreparedstatement("select  count(1) from " + table + whereForPreparedstatement);
		sql.setLimit(limit);
		sql.setOrder(order);
		sql.setWhere(where);
		sql.setWhereParaListForPreparedstatement(str.getWhereParaListForPreparedstatement());

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

	}
}
