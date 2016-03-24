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

/**
 * 插入oracle 监控 订单操作
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author airsupply
 * 
 * @date 2015年11月2日
 * 
 * @version 1.0
 */
public class AddNewMonitroOrder {
	/**
	 * 数据文件
	 */
	static File excelFile;
	/**
	 * 需要删除的订单号
	 */
	static String[] delOrderNo = null;

	public static void insertToMonitor() {
		String url = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.url");
		String userName = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.username");
		String userPassword = MyUtil.getConfigValue("jdbc.properties", "jdbc.system.password");
		String driver= MyUtil.getConfigValue("jdbc.properties","jdbc.driver");
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, userPassword);
			stmt = conn.createStatement();// 提交sql语句,创建一个Statement对象来将SQL语句发送到数据库
			// ************************删除订单数据***********************************************

			// for (int i = 0; i < delOrderNo.length; i++) {
			// String deleteOrder = "delete  from t_order where order_no='" +
			// delOrderNo[i] + "'";
			// String deleteOrderMon =
			// "delete  from t_order_mon where order_no='" + delOrderNo[i]
			// + "'";
			// System.out.println("确认要执行删除语句吗?：");
			// System.out.println(deleteOrder);
			// System.out.println(deleteOrderMon);
			// Scanner sc = new Scanner(System.in);
			// String confirm = sc.nextLine();
			// if (confirm.equals("yes")) {
			// stmt.execute(deleteOrder);
			// stmt.execute(deleteOrderMon);
			// }
			// }

			// ****************************谨慎操作**********************************************

			InputStream in;
			in = new FileInputStream(excelFile);
			List<String> sqlList = ExcelReader.getSqlList(in);
			for (String sql : sqlList) {
				System.out.println("exccute :" + sql);
				stmt.execute(sql);
			}
			System.out.println(" insert success!");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭数据库，结束进程
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		delOrderNo = new String[] { "955940000000000000001", "955940000000000000002" };
		// String filePath = "D:\\gsworkspace\\input_file\\table-1115-1119.xls";
		//String filePath = "D:\\gsworkspace\\input_file\\table-1108-1112.xls";
		String filePath = "D:\\gsworkspace\\input_file\\pro.xls";
		// String filePath = "D:\\gsworkspace\\input_file\\table-1025-1029.xls";
		//String filePath = "D:\\gsworkspace\\input_file\\table-1018-1022.xls";
		excelFile = new File(filePath);
		insertToMonitor();
	}

}
