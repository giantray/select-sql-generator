package com.github.giantray.compositesSelectSql;

import java.util.ArrayList;
import java.util.List;

public class SqlTempStr {
	String where = "";
	String limit = "";
	String order = "";
	String orderCol = "";
	String orderType = "";
	String table = "";
	Integer start = 0;
	Integer size = 0;
	String whereForPreparedstatement = "";

	List<Object> whereParaListForPreparedstatement = new ArrayList<Object>();

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

	public String getWhereForPreparedstatement() {
		return whereForPreparedstatement;
	}

	public void setWhereForPreparedstatement(String whereForPreparedstatement) {
		this.whereForPreparedstatement = whereForPreparedstatement;
	}

	public List<Object> getWhereParaListForPreparedstatement() {
		return whereParaListForPreparedstatement;
	}

	public void setWhereParaListForPreparedstatement(List<Object> whereParaListForPreparedstatement) {
		this.whereParaListForPreparedstatement = whereParaListForPreparedstatement;
	}

}
