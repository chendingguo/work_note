package com.airsupply.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.airsupply.util.MyUtil;
import com.chinadaas.gsinfo.hbase.data.EntBaseinfo;
import com.chinadaas.gsinfo.hbase.util.HBaseUtil;
import com.chinadaas.gsinfo.hbase.util.TableSuffixTracker;
import com.common.utils.StringUtils;

public class BaseInfoDaoImpl {

	private static final String TABLE_NAME = "ENTERPRISEBASEINFOCOLLECT";
	// 字段分隔符
	private static final String DELIM = "\u0001";
	private static final byte[] TABLE_FAMILY = Bytes.toBytes("f");
	private static final byte[] ALL_QUALIFIER = Bytes.toBytes("A");
	private static final byte[] QUALIFIER = new byte[] { 'A' };
	public static final Log logger = LogFactory.getLog(BaseInfoDaoImpl.class);
	private HConnection connection;
	private static BaseInfoDaoImpl queryDao = null;

	public static BaseInfoDaoImpl getInstance() {
		if (queryDao == null||queryDao.connection==null) {
			queryDao = new BaseInfoDaoImpl();
			Configuration conf = getDefaultConfiguration();
			conf.setInt("hbase.client.keyvalue.maxsize", -1);
			try {
				queryDao.connection = HConnectionManager.createConnection(conf);
			} catch (ZooKeeperConnectionException e) {
				logger.error(e);
			}

		}
		return queryDao;
	}

	/**
	 * 拼接pripid
	 * 
	 * @param r
	 * @return
	 */
	public static String composite(Result r) {
		StringBuilder sb = new StringBuilder();
		sb.append(Bytes.toString(r.getRow()));
		sb.append(DELIM);
		sb.append(Bytes.toString(r.getValue(TABLE_FAMILY, ALL_QUALIFIER)));
		return sb.toString();
	}

	public EntBaseinfo getEntBaseInfo(String extNodeNum, String pripid) {
		EntBaseinfo baseInfo = new EntBaseinfo();
		ResultScanner rs = null;
		String startRow = StringUtils.connect(DELIM, extNodeNum, pripid);
		byte[] startRowBytes = Bytes.toBytes(startRow);
		byte[] stopRowBytes = HBaseUtil.incrementBytes(startRowBytes);
		try {
			String suffix =  MyUtil.getConfigValue("app.properties", "hbase.table.data.postfix.current");
		
			// HTableInterface table = getTable(TABLE_NAME);
			HTableInterface table = connection.getTable(TABLE_NAME + "_" + suffix);
			Scan scan = new Scan();
			scan.setStartRow(startRowBytes);
			scan.setStopRow(stopRowBytes);
			QualifierFilter ff = new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(
					QUALIFIER));
			scan.setFilter(ff);
			rs = table.getScanner(scan);
			for (Result r : rs) {
				String valueStr = composite(r);
				baseInfo.parseFromString(valueStr);
			}

		} catch (Exception e) {
			baseInfo = null;
			logger.error(e);
		}
		return baseInfo;
	}

	public static Configuration getDefaultConfiguration() {
		return HBaseConfiguration.create();
	}
}
