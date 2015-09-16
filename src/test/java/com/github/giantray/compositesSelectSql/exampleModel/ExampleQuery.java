package com.github.giantray.compositesSelectSql.exampleModel;

import java.util.Date;

import com.github.giantray.compositesSelectSql.SqlLimit;
import com.github.giantray.compositesSelectSql.SqlLimit.Limit;
import com.github.giantray.compositesSelectSql.SqlOper;
import com.github.giantray.compositesSelectSql.SqlOper.Oper;
import com.github.giantray.compositesSelectSql.SqlOrderCol;
import com.github.giantray.compositesSelectSql.SqlOrderType;
import com.github.giantray.compositesSelectSql.SqlOrderType.Order;
import com.github.giantray.compositesSelectSql.SqlTable;

@SqlTable("example")
public class ExampleQuery {

	@SqlOper(Oper.LIKE)
	private String name;
	@SqlOper(Oper.EQUAL)
	private String sexuality;
	@SqlOper(Oper.GREATER)
	private Integer score;
	@SqlOper(Oper.LESSEQUAL)
	private Date createAt;

	@SqlLimit(Limit.START)
	private int start;
	@SqlLimit(Limit.SIZE)
	private int size;
	@SqlOrderType()
	private Order order;
	@SqlOrderCol()
	private String orderCol;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSexuality() {
		return sexuality;
	}

	public void setSexuality(String sexuality) {
		this.sexuality = sexuality;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getOrderCol() {
		return orderCol;
	}

	public void setOrderCol(String orderCol) {
		this.orderCol = orderCol;
	}

}
