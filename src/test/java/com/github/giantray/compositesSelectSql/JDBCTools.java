package com.github.giantray.compositesSelectSql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCTools {

	/***
	 * release connection
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void releaseDB(ResultSet resultSet, Statement statement, Connection connection) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * get connection
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {

		String user = "root";
		String password = "aa";
		String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF8";
		String driverClass = "com.mysql.jdbc.Driver";

		Class.forName(driverClass);

		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}
}
