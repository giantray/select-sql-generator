package com.github.giantray.compositesSelectSql;

import java.util.Date;

import org.junit.Test;

import com.github.giantray.compositesSelectSql.SqlOrderType.Order;
import com.github.giantray.compositesSelectSql.exampleModel.ExampleQuery;

/**
 * @author lizeyang
 */
public class TestMain {

	@Test
	public void testGenSql() {
		ExampleQuery query = new ExampleQuery();
		query.setName("li");
		query.setCreateAt(new Date(System.currentTimeMillis()));
		query.setOrder(Order.DESC);
		query.setOrderCol("name");
		query.setScore(60);
		query.setSexuality("");
		// query.setSize(0);
		// query.setStart(5);

		SelectSql sql = SqlGenerator.getSelectWhere(query);
		System.out.println(sql.getSelectAll());
		System.out.println(sql.getSelectCount());
	}

}
