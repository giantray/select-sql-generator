package com.github.giantray.compositesSelectSql;

/**
 * Select SQL generate result set
 */
public class SelectSql {
	/**
	 * sql like 'select * from xxx where xxx order by xxx limit x,x'
	 */
	private String selectAll;
	/**
	 * sql like 'select count(*) from xxx where xxx order by xxx limit x,x'
	 */
	private String selectCount;
	/**
	 * where segment like 'where xxx'
	 */
	private String where;
	/**
	 * limit segment like 'limit x,y'
	 */
	private String limit;
	/**
	 * order segment like 'order by xx desc'
	 */
	private String order;

	/**
	 * 
	 * @return sql like 'select * from xxx where xxx order by xxx limit x,x'
	 */
	public String getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}

	/**
	 * 
	 * @return sql like 'select count(*) from xxx where xxx order by xxx limit
	 *         x,x'
	 */
	public String getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(String selectCount) {
		this.selectCount = selectCount;
	}

	/**
	 * 
	 * @return where segment like 'where xxx'
	 */
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * 
	 * @return limit segment like 'limit x,y'
	 */
	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return order segment like 'order by xx desc'
	 */
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
