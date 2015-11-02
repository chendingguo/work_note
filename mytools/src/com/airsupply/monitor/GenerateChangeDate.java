package com.airsupply.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import com.airsupply.util.MyUtil;
import com.chinadaas.gsinfo.hbase.data.AdministrativePenalty;
import com.chinadaas.gsinfo.hbase.data.CourtAnnouncement;
import com.chinadaas.gsinfo.hbase.data.MonSharesFrost;
import com.chinadaas.gsinfo.hbase.data.MonSharesImpawn;
import com.chinadaas.gsinfo.hbase.data.PunishBreakRecord;
import com.chinadaas.gsinfo.hbase.data.TableConstant.TABLE;
import com.chinadaas.gsinfo.hbase.data.adapter.table.Basic;
import com.chinadaas.gsinfo.hbase.data.adapter.table.Person;
import com.chinadaas.gsinfo.hbase.data.adapter.table.ShareHolder;
/**
 * 按监控数据格式拼结变更项

* <p>Description: </p>

* @author airsupply

* @date 2015年11月2日

* @version 1.0
 */
public class GenerateChangeDate {

	public static final String S001 = "\u0001";
	public static final String S002 = "\u0002";
	public static final String S003 = "\u0003";
	public static final String changeMonitorFile = "monitor_change.properties";
	@SuppressWarnings("rawtypes")
	static Class[] classes = { Basic.class, ShareHolder.class, Person.class,
			PunishBreakRecord.class, CourtAnnouncement.class, AdministrativePenalty.class,
			MonSharesFrost.class, MonSharesImpawn.class };
	static String[] classNames = { TABLE.ENTERPRISEBASEINFOCOLLECT.toString(),
			TABLE.E_INV_INVESTMENT.toString(), TABLE.E_PRI_PERSON.toString(),
			TABLE.dis_sxbzxr.toString(), TABLE.fygg.toString(), TABLE.xzcf.toString(),
			TABLE.frost.toString(), TABLE.impawn.toString() };
	static String[] classChinessNames = { "企业基本信息", "投资", "人员", "失信人", "法院公告", "处罚信息", "股权冻结信",
			"股权出质" };

	public static void main(String[] args) {
		 generatePropertyFile();

		createMonitorDataString();

	}

	@SuppressWarnings("rawtypes")
	public static String createMonitorDataString() {
		// ENTERPRISEBASEINFOCOLLECT,E_GT_BASEINFO,E_INV_INVESTMENT,E_PRI_PERSON,E_GT_PERSON
		Properties changeProperties = new Properties();
		InputStream in = GenerateChangeDate.class.getClassLoader().getResourceAsStream(
				changeMonitorFile);
		try {
			changeProperties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		for (Class cls : classes) {
			String className = classNames[i];
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				String propertyKey = className + "." + fieldName;

				String propertyValue = changeProperties.getProperty(propertyKey);
				

				if (propertyValue != null && !propertyValue.isEmpty()
						&& propertyValue.split(",").length == 2) {
					String[] values = propertyValue.split(",");
				//	String sequenceId = RandomTools.getRandom32String(28);
					String sequenceId ="630000201506151311747041";
					stringBuilder.append(className);
					stringBuilder.append(S001);
					stringBuilder.append(field.getName().replaceFirst("daas", ""));
					stringBuilder.append(S001);
					stringBuilder.append(values[0].trim());
					stringBuilder.append(S001);
					stringBuilder.append(values[1].trim());
					stringBuilder.append(S001);

					String type = "UPDATE";
					if (values[0].trim().isEmpty() && !values[1].trim().isEmpty()) {
						type = "INSERT";
					} else if (!values[0].trim().isEmpty() && values[1].trim().isEmpty()) {
						type = "DELETE";
					}

					stringBuilder.append(type);
					stringBuilder.append(S001);
					stringBuilder.append(sequenceId);
					stringBuilder.append(S002);
				}
			}
			
			i++;

		}
		stringBuilder.delete(stringBuilder.lastIndexOf(S002), stringBuilder.length());
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();

	}

	@SuppressWarnings("rawtypes")
	public static void generatePropertyFile() {

		StringBuilder propBuilder = new StringBuilder();
		int i = 0;
		for (Class cls : classes) {
			propBuilder.append(" #----" + classChinessNames[i] + " " + "旧值 ," + "新值\n");
			String className = classNames[i];
			Field[] fields = cls.getDeclaredFields();

			for (Field field : fields) {
				String fieldName = field.getName();
				String propertyKey = className + "." + fieldName + "=";
				String newValue;
				String oldValue;
			
				if(fieldName.indexOf("DATE")>-1){
					 oldValue="2015-01-01";
					 newValue="2015-02-02";
				}else{
					 oldValue=fieldName+"_1";
					 newValue=fieldName+"_2";
				}
				//新增变更
				if(i>2){
					oldValue=" ";
				}
				propertyKey+=oldValue+","+newValue;
				propBuilder.append(propertyKey + "\n");

			}
			i++;
		}
		String path = "D:\\gsworkspace\\out_file" + "\\" + changeMonitorFile;
		MyUtil.saveFile(path, propBuilder.toString());
		System.out.println("save file success!");

	}
}
