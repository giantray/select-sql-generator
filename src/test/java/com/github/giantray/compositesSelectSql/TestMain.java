package com.github.giantray.compositesSelectSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		query.setSexuality("male ' or 1=1");
		query.setSize(0);
		query.setStart(5);

		SelectSql sql = SqlGenerator.getSelectWhere(query);
		System.out.println(sql.getSelectAll());
		System.out.println(sql.getSelectCount());
		System.out.println(sql.getSelectAllForPreparedstatement());
		System.out.println(sql.getSelectCountForPreparedstatement());
	}

	public void testSQLInjection2() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			ExampleQuery query = new ExampleQuery();

			query.setCreateAt(new Date(System.currentTimeMillis()));
			query.setSexuality("test");

			SelectSql sql = SqlGenerator.getSelectWhere(query);
			System.out.println(sql.getSelectAllForPreparedstatement());
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql.getSelectAllForPreparedstatement());

			SqlGenerator.setPreparedstatementPara(preparedStatement, sql.getWhereParaListForPreparedstatement());

			resultSet = preparedStatement.executeQuery();

			int i = 0;
			while (resultSet.next()) {
				i++;
			}
			System.out.println(i);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}
	}
}
