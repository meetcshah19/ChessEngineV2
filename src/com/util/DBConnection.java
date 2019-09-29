package com.util;
import java.sql.*;

public class DBConnection {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://51.158.118.84:33060/account";
	static final String USER = "meet";
	static final String PASS = "meet";

	public static java.sql.Connection getConnection() {

		java.sql.Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			System.out.println("Connected");
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Goodbye!");
		return conn;
	}

	public static void main(String[] args) {
		getConnection();
	}
}
