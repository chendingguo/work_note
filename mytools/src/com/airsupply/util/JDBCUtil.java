package com.airsupply.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	static Connection conn = null;

	public static Connection getConn() {
		if (conn == null) {
			String url = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.url");
			String userName = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.username");
			String userPassword = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.password");

			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url, userName, userPassword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
}
