package com.airsupply.monitor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
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

import com.airsupply.monitor.GenerateChangeDate;
import com.airsupply.util.MyUtil;
import com.chinadaas.gsinfo.hbase.data.EntBaseinfo;
import com.common.utils.StringUtils;

/**
 * hbase操作类
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
public class MonitorHbaseOperation {

	// 声明静态配置
	static Configuration conf = null;

	static HConnection conn = null;

	// static HTable table;

	/*
	 * 创建表
	 * 
	 * @tableName 表名
	 * 
	 * @family 列族列表
	 */
	public static void creatTable(String tableName, String[] family) throws Exception {
		@SuppressWarnings("resource")
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
	public static Result getResult(String tableName, String rowKey, String qualifierName)
			throws IOException {

		Get get = new Get(Bytes.toBytes(rowKey));
		QualifierFilter filter = new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(qualifierName)));
		get.setFilter(filter);
		@SuppressWarnings("resource")
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

		@SuppressWarnings("resource")
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

		@SuppressWarnings("resource")
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

		@SuppressWarnings("resource")
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

		@SuppressWarnings("resource")
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

		@SuppressWarnings("resource")
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		System.out.println(tableName + "is deleted!");
	}

	// 删除一条(行)数据
	public static void delRow(String tableName, String row) throws Exception {
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Delete del = new Delete(Bytes.toBytes(row));
		table.delete(del);
	}

	/**
	 * init config
	 */

	public static void setConfig() {
		conf = HBaseConfiguration.create();
		// conf.set("hbase.zookeeper.quorum",
		// "192.168.202.101,192.168.202.103,192.168.202.102");
		// conf.set("zookeeper.znode.parent", "/hbase1");
		// conf.setInt("hbase.zookeeper.property.clientPort", 2181);
		conf.set("hbase.rpc.timeout", "600000");

		// conf.set("hbase.zookeeper.quorum", "192.168.139.129");
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
			e.printStackTrace();
		}

	}

	/**
	 * 查询表中的某一列
	 * 
	 * @tableName 表名
	 * 
	 * @rowKey rowKey
	 */
	public static byte[] getResultByColumn(String tableName, String rowKey, String familyName,
			String columnName) throws IOException {

		@SuppressWarnings("resource")
		HTable table = new HTable(conf, Bytes.toBytes(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); // 获取指定列族和列修饰符对应的列
		Result result = table.get(get);
		byte[] value = null;
		for (KeyValue kv : result.list()) {
			System.out.println("family:" + Bytes.toString(kv.getFamily()));
			System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
			System.out.println("value:" + Bytes.toString(kv.getValue()));
			System.out.println("Timestamp:" + kv.getTimestamp());
			System.out.println("-------------------------------------------");
			if (Bytes.toString(kv.getQualifier()).equals(columnName)) {
				value = kv.getValue();

			}

		}

		return value;
	}

	/*
	 * 遍历查询hbase表
	 * 
	 * @tableName 表名
	 */
	public static void getResultScann(String tableName) throws IOException {
		Scan scan = new Scan();
		ResultScanner rs = null;
		HTableInterface table = conn.getTable(tableName);
		try {
			// String split = StringUtils.S001;
			// String startRow = StringUtils.connect(split, "894", "440000",
			// "0016367", "20140319");
			// // System.out.println(startRow);
			// scan.setStartRow(Bytes.toBytes(startRow));
			// scan.setStopRow(Bytes.toBytes(startRow));
			// QualifierFilter ff = new QualifierFilter(
			// CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("A")));
			// scan.setFilter(ff);
			rs = table.getScanner(scan);
			int count = 0;
			for (Result r : rs) {
				count++;
				for (KeyValue kv : r.list()) {
					System.out.println("row:" + Bytes.toString(kv.getRow()));
					System.out.println("family:" + Bytes.toString(kv.getFamily()));
					System.out.println("qualifier:" + Bytes.toString(kv.getQualifier()));
					System.out.println("value:" + Bytes.toString(kv.getValue()));

					System.out.println("timestamp:" + kv.getTimestamp());
					System.out.println("-------------------------------------------");

				}
				if (count > 10) {
					return;
				}

			}
		} finally {
			rs.close();
		}
	}

	/**
	 * 向表中插入一条新数据
	 * 
	 * @param tableName
	 *            表名
	 * @param row
	 *            行键key
	 * @param columnFamily
	 *            列族
	 * @param column
	 *            列名
	 * @param data
	 *            要插入的数据
	 * @throws IOException
	 */
	public static void putData(String tableName, String row, String columnFamily, String column,
			byte[] data) throws IOException {
		HTableInterface table = conn.getTable(tableName);
		Put put = new Put(Bytes.toBytes(row));

		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), data);
		table.put(put);
		table.flushCommits();
		System.out.println("PUT:" + row + "','" + columnFamily + ":" + column + "','"
				+ Bytes.toString(data) + "'");
	}

	public static void getEntBaseInfo() {
		BaseInfoDaoImpl baseInfoDao = BaseInfoDaoImpl.getInstance();
		EntBaseinfo baseInfo = baseInfoDao.getEntBaseInfo("630000", "630000000011953071100058");
		System.out.println("entName" + baseInfo.getENTNAME());

	}

	public static void putToUserPush() throws IOException {
		String dateStr = "_20150908";
		String entIndexTableName = "ENTERPRISEBASEINFOCOLLECT_ENTNAME" + dateStr;
		String entTableName = "ENTERPRISEBASEINFOCOLLECT" + dateStr;
		String tableName = "E_INV_INVESTMENT" + dateStr;
		String personTable = "E_PRI_PERSON" + dateStr;
		String resultTable = "RESULT";
		String userPushTable = "USER_PUSH";
		String alertLogTable = "ALTER_LOG";

		// a column change value
		// 厦门用户61147
		String newUrerId = "61477";
		String newExtNo = "";
		String newPripid = "";
		//String[] newLstChangeDates = { "20151115", "20151116", "20151117", "20151118", "20151119" };
		//String[] newLstChangeDates = { "20151108", "20151109", "20151110", "20151111", "20151112" };
		//String[] newLstChangeDates = { "20151101", "20151102", "20151103", "20151104", "20151105" };
		//String[] newLstChangeDates = { "20151025", "20151026", "20151027", "20151028", "20151029" };
		String[] newLstChangeDates = { "20151018", "20151019", "20151020", "20151021", "20151022" };
		String split = StringUtils.S001;
		// xielei's account
		String rowKey = StringUtils.connect(split, "894", "440000", "0016367", "20140319");
		String pripidInputFile = MyUtil.getConfigValue("app.properties", "ent.pripid.file");
		System.out.println(pripidInputFile);
		List<String> pripidList = MyUtil.readFileLineList(pripidInputFile);
		// --------------------------------遍历userpush-----------------
		Scan scan = new Scan();
		ResultScanner rs = null;
		HTableInterface table = conn.getTable(userPushTable);

		try {
			
			rs = table.getScanner(scan);
			int i = 0;
			for (Result r : rs) {

				EntBaseinfo entBaseinfo = new EntBaseinfo();
				byte[] eBytes = null;
				byte[] aBytes = null;
				for (KeyValue kv : r.list()) {
					String qualifier = Bytes.toString(kv.getQualifier());
					if ("A".equals(qualifier)) {
						aBytes = kv.getValue();
					} else if ("E".equals(qualifier)) {
						eBytes = kv.getValue();
					}
					if (eBytes == null) {
						eBytes=getResultByColumn(userPushTable, rowKey, "f", "E");
					}
					entBaseinfo.parseFromBytes(eBytes);
				}

				String pripidStr = pripidList.get(i).trim();
				newExtNo = pripidStr.split("#")[0];
				newPripid = pripidStr.split("#")[1];
				String entName = pripidStr.split("#")[2];
				String newKey = StringUtils.connect(split, newUrerId, newExtNo, newPripid,
						newLstChangeDates[i % 5]);

				// column----------------------------------------------------"E"
				entBaseinfo.setENTNAME(entName);
				putData(userPushTable, newKey, "f", "E", entBaseinfo.getBytes());

				// column----------------------------------------------------"A"
               if(aBytes==null){
            	   aBytes = getResultByColumn(userPushTable, rowKey, "f", "A");
               }
				
				System.out.println(Bytes.toString(aBytes));
//				String fullChangedValue = GenerateChangeDate.createMonitorDataString();
				// //造全量数据
				// if(i==0){
				// // aValue=Bytes.toBytes(fullChangedValue);
				//
				// }
				putData(userPushTable, newKey, "f", "A", aBytes);

				i++;
				if (i >= 100) {
					return;
				}

			}
		} finally {
			rs.close();
		}
	}

	public static void main(String[] args) throws Exception {
		setConfig();
		conn = HConnectionManager.createConnection(conf);
		putToUserPush();

	}
}

