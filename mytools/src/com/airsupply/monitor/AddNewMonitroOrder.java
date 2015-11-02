package com.airsupply.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import com.airsupply.util.ExcelReader;
import com.airsupply.util.MyUtil;

public class AddNewMonitroOrder {
	static File f ;

	public static void insertToMonitor() {
		String url = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.url");
		String userName = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.username");
		String userPassword = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.password");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, userName, userPassword);
			stmt = conn.createStatement();// 提交sql语句,创建一个Statement对象来将SQL语句发送到数据库
			// ====================================================================================
			String deleteOrder = "delete  from t_order where order_no='200000000000000000001'";
			String deleteOrderMon = "delete  from t_order_mon where order_no='200000000000000000001'";

			System.out.println("确认要执行删除语句吗?：");
			System.out.println(deleteOrder);
			System.out.println(deleteOrderMon);
			Scanner sc = new Scanner(System.in);
			String confirm = sc.nextLine();
			if (confirm.equals("yes")) {
				stmt.execute(deleteOrder);
				stmt.execute(deleteOrderMon);
			}
			// =====================================================================================

			InputStream in;
			in = new FileInputStream(f);
			List<String> sqlList = ExcelReader.getSqlList(in);
			for (String sql : sqlList) {
				System.out.println("exccute :" + sql);
				stmt.execute(sql);
			}
			rs = stmt.executeQuery("select *  from t_order where order_no='200000000000000000001'");
			while (rs.next()) {// 使当前记录指针定位到记录集的第一条记录
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭数据库，结束进程
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String filePath="D:\\gsworkspace\\out_file\\t_data_batch.xls";
		f=new File(filePath);
		insertToMonitor();
	}

}
