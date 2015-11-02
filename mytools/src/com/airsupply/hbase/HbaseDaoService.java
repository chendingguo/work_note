package com.airsupply.hbase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.chinadaas.gsinfo.core.utils.DateUtils;
import com.chinadaas.gsinfo.hbase.data.SearchResult;
import com.common.utils.StringUtils;

public class HbaseDaoService {

	// 声明静态配置
	static Configuration conf = null;

	static HConnection conn = null;
	private static final byte[] TABLE_FAMILY = Bytes.toBytes("f");
	private static final byte[] ALL_QUALIFIER = Bytes.toBytes("A");
	
	static String dateStr = "_20150908";
	public static String entIndexTableName="ENTERPRISEBASEINFOCOLLECT_ENTNAME"+dateStr;
	public static String  entTableName = "ENTERPRISEBASEINFOCOLLECT" + dateStr;
	public static String tableName = "E_INV_INVESTMENT" + dateStr;
	public static String result = "result";
	public static String personTable = "E_PRI_PERSON" + dateStr;
	public static String gtBaseInfo = "E_GT_BASEINFO" + dateStr;
	public static String userPushTable="USER_PUSH";
	
	public static HbaseDaoService hbaseDaoService=new HbaseDaoService();
	public static HbaseDaoService getInstance(){
		if(conn==null){
			setConfig();
			try {
				conn = HConnectionManager.createConnection(conf);
			} catch (ZooKeeperConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hbaseDaoService;
	
	}

	/*
	 * 创建表
	 * 
	 * @tableName 表名
	 * 
	 * @family 列族列表
	 */
	public static void creatTable(String tableName, String[] family) throws Exception {

		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.139.129");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor desc = new HTableDescriptor();
		for (int i = 0; i < family.length; i++) {
			desc.addFamily(new HColumnDescriptor(family[i]));
		}
		if (admin.tableExists(tableName)) {
			System.out.println("table Exists!");
			deleteTable("blog");
		}
		admin.createTable(desc);
		System.out.println("create table Success!");

	}

	/*
	 * 为表添加数据（适合知道有多少列族的固定表）
	 * 
	 * @rowKey rowKey
	 * 
	 * @tableName 表名
	 * 
	 * @column1 第一个列族列表
	 * 
	 * @value1 第一个列的值的列表
	 * 
	 * @column2 第二个列族列表
	 * 
	 * @value2 第二个列的值的列表
	 */
	public static void addData(String rowKey, String tableName, String[] column1, String[] value1,
			String[] column2, String[] value2) throws IOException {

		Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey
		HTableInterface htable = conn.getTable(tableName);
		HColumnDescriptor[] columnFamilies = htable.getTableDescriptor() // 获取所有的列族
				.getColumnFamilies();

		for (int i = 0; i < columnFamilies.length; i++) {
			String familyName = columnFamilies[i].getNameAsString(); // 获取列族名
			if (familyName.equals("article")) { // article列族put数据
				for (int j = 0; j < column1.length; j++) {
					put.add(Bytes.toBytes(familyName), Bytes.toBytes(column1[j]),
							Bytes.toBytes(value1[j]));
				}
			}
			if (familyName.equals("author")) { // author列族put数据
				for (int j = 0; j < column2.length; j++) {
					put.add(Bytes.toBytes(familyName), Bytes.toBytes(column2[j]),
							Bytes.toBytes(value2[j]));
				}
			}
		}
		htable.put(put);
		// System.out.println("add data Success!");
	}

	/*
	 * 根据rwokey查询
	 * 
	 * @rowKey rowKey
	 * 
	 * @tableName 表名
	 */
	public static Result getResult(String tableName, String rowKey) throws IOException {

		Get get = new Get(Bytes.toBytes(rowKey));
		HTable table = new HTable(conf, Bytes.toBytes(tableName));// 获取表
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		return result;
	}

	/*
	 * 查询表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static void getResultByColumn(String tableName, String rowKey, String familyName,
			String columnName) throws IOException {

		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
	}

	/*
	 * 更新表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 * 
	 * @value 更新后的值
	 */
	public static void updateTable(String tableName, String rowKey, String familyName,
			String columnName, String value) throws IOException {

		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Put put = new Put(Bytes.toBytes(rowKey));
		put.add(Bytes.toBytes(familyName), Bytes.toBytes(columnName), Bytes.toBytes(value));
		table.put(put);
		System.out.println("update table Success!");
	}

	/*
	 * 查询某列数据的多个版本
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void getResultByVersion(String tableName, String rowKey, String familyName,
			String columnName) throws IOException {

		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
		get.setMaxVersions(5);
		Result result = table.get(get);
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
		}
		/*
		 * List<?> results = table.get(get).list(); Iterator<?> it =
		 * results.iterator(); while (it.hasNext()) {
		 * System.out.println(it.next().toString()); }
		 */
	}

	/*
	 * 删除指定的列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 * 
	 * @familyName 列族名
	 * 
	 * @columnName 列名
	 */
	public static void deleteColumn(String tableName, String rowKey, String falilyName,
			String columnName) throws IOException {

		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete deleteColumn = new Delete(Bytes.toBytes(rowKey));
		deleteColumn.deleteColumns(Bytes.toBytes(falilyName), Bytes.toBytes(columnName));
		table.delete(deleteColumn);
		System.out.println(falilyName + ":" + columnName + "is deleted!");
	}

	/*
	 * 删除指定的列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static void deleteAllColumn(String tableName, String rowKey) throws IOException {

		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete deleteAll = new Delete(Bytes.toBytes(rowKey));
		table.delete(deleteAll);
		System.out.println("all columns are deleted!");
	}

	/*
	 * 删除表
	 * 
	 * @tableName 表名
	 */
	public static void deleteTable(String tableName) throws IOException {

		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println(tableName + "is deleted!");
	}

	/**
	 * init config
	 */

	public static void setConfig() {

		conf = HBaseConfiguration.create();
	  // conf.set("hbase.zookeeper.quorum", "192.168.202.101");
	}

	public static void createTable(String tableName) {
		long start = System.currentTimeMillis();
		try {
			HTableInterface htable = conn.getTable(tableName);
			List<Put> putList = new ArrayList<Put>();
			HColumnDescriptor[] columnFamilies = htable.getTableDescriptor().getColumnFamilies();

			String[] family = { "article", "author" };
			creatTable(tableName, family);
			String[] column1 = { "title", "content", "tag" };
			String[] column2 = { "name", "nickname" };
			for (int i = 1; i <= 1000000; i++) {
				DecimalFormat format = new DecimalFormat("00000000");
				String rowKey = format.format(i);
				// System.out.println("==| insert"+rowKey);
				String[] valueOfArticle = { "title" + i, "content" + i, "tag" + i };
				String[] valueOfAuthor = { "name" + i, "nickname" + i };
				// addData(rowKey, tableName, column1, valueOfArticle, column2,
				// valueOfAuthor);
				Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey

				for (int colIndex = 0; colIndex < columnFamilies.length; colIndex++) {
					String familyName = columnFamilies[colIndex].getNameAsString(); // 获取列族名

					if (familyName.equals("article")) { // article列族put数据
						for (int k = 0; k < column1.length; k++) {
							put.add(Bytes.toBytes(familyName), Bytes.toBytes(column1[k]),
									Bytes.toBytes(valueOfArticle[k]));
						}
					}
					if (familyName.equals("author")) { // author列族put数据
						for (int k = 0; k < column2.length; k++) {
							put.add(Bytes.toBytes(familyName), Bytes.toBytes(column2[k]),
									Bytes.toBytes(valueOfAuthor[k]));
						}
					}
				}
				putList.add(put);
				if (i % 10000 == 0) {

					htable.put(putList);
					putList = new ArrayList<Put>();
				}

			}
			long end = System.currentTimeMillis();
			long used = end - start;
			System.out.println("data insert  finished used " + used + " ms");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static void searchByRowKey(String tableName, String rowKey) {
		try {
			HTable table = new HTable(conf, Bytes.toBytes(tableName));
			Get g = new Get(Bytes.toBytes(rowKey));
			Result rs = table.get(g);
			for (KeyValue kv : rs.list()) {
				System.out.println("family:" + Bytes.toString(kv.getFamily()));
				System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
				System.out.println("value:" + Bytes.toString(kv.getValue()));
				System.out.println("Timestamp:" + kv.getTimestamp());
				System.out.println("-------------------------------------------");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/***********************
	 * 将rowkey及value拼接成一个完整的字符串
	 * 
	 * @param r
	 * @return
	 * 
	 * @author wangyoucai@chinadaas.com
	 */
	public static String composite(Result r) {
		StringBuilder sb = new StringBuilder();
		sb.append(Bytes.toString(r.getRow()));
		String DELIM = "\u0001";
		sb.append(DELIM);
		sb.append(Bytes.toString(r.getValue(TABLE_FAMILY, ALL_QUALIFIER)));
		return sb.toString();
	}

	/*
	 * 遍历查询hbase表
	 * 
	 * @tableName 表名
	 */
	public String getResultScann(String tableName,String startRow) throws IOException {
		String pripid="";
		int count = 0;
		Scan scan = new Scan();
		ResultScanner rs = null;
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		try {
			String split = StringUtils.S001;
			 
			 scan.setStartRow(Bytes.toBytes(startRow));
			 scan.setStopRow(Bytes.toBytes(startRow + "X"));
			 QualifierFilter ff = new QualifierFilter(
			 CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("A")));
			 scan.setFilter(ff);
			rs = table.getScanner(scan);

			for (Result r : rs) {

				count++;
				for (KeyValue kv : r.list()) {
					String rowKey = Bytes.toString(kv.getRow());
					//
					 System.out.println("row:" + Bytes.toString(kv.getRow()));
					 System.out.println("family:"
					 + Bytes.toString(kv.getFamily()));
					 System.out.println("qualifier:"
					 + Bytes.toString(kv.getQualifier()));
					 System.out
					 .println("value:" + Bytes.toString(kv.getValue()));
					 pripid=Bytes.toString(kv.getValue());
//					SearchResult result = new SearchResult();
//					result.parseFromBytes((kv.getValue()));
//					if (result != null && result.getEntBaseInfo() != null) {
//						saveFile("D:\\gsworkspace\\out_file\\order.txt", result.getEntBaseInfo()
//								.toString() + "\n");
//						System.out.println(result.getEntBaseInfo().getNAME());
//					}
				}
				int max = 10;
				if (count > max) {
					System.out.println("execute end search " + max + " results!");
					return pripid;
				}
			}
		} finally {
			rs.close();
		}
		return pripid;
	}
	

	public static boolean saveFile(String path, String content) {

		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
}