package com.airsupply.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.chinadaas.gsinfo.core.utils.StringUtils;

/**
 * 操作Excel表格的功能类
 */
public class ExcelReader {
	

	public static List<String> getSqlList(InputStream in) {
		List<String> sqlList=new ArrayList<String>();
		try {
			HSSFWorkbook wbs = new HSSFWorkbook(in);

			for (int i = 0; i < wbs.getNumberOfSheets(); i++) {
				HSSFSheet childSheet = wbs.getSheetAt(i);

				StringBuilder sqlFieldBuilder = new StringBuilder();
				sqlFieldBuilder.append("insert into ").append(childSheet.getSheetName());
				sqlFieldBuilder.append("(");
				// head
				HSSFRow headRow = childSheet.getRow(0);
				String[] heads = new String[headRow.getLastCellNum()];
				for (int k = 0; k < headRow.getLastCellNum(); k++) {
					HSSFCell cell = headRow.getCell(k);
					if (null != cell) {

						heads[k] = cell.getStringCellValue();
						sqlFieldBuilder.append(heads[k]).append(",");
					} else {
						System.out.print("head can't be null");
					}
				}
				sqlFieldBuilder.delete(sqlFieldBuilder.lastIndexOf(","), sqlFieldBuilder.length());
				sqlFieldBuilder.append(")");
				// type
				HSSFRow typeRow = childSheet.getRow(1);
				String[] types = new String[typeRow.getLastCellNum() + 1];
				for (int k = 0; k < typeRow.getLastCellNum(); k++) {
					HSSFCell cell = typeRow.getCell(k);
					if (null != cell) {
						types[k] = cell.getStringCellValue();
					} else {
						System.out.print("types can't be null");
					}
				}
			
				for (int j = 2; j < childSheet.getLastRowNum() + 1; j++) {
					StringBuilder sqlValueBuilder = new StringBuilder();
					sqlValueBuilder.append(" VALUES (");
					HSSFRow row = childSheet.getRow(j);
					if (null != row) {
						for (int k = 0; k <= heads.length - 1; k++) {
							HSSFCell cell = row.getCell(k);
							String value;
							if (null != cell) {
								value = cell.getStringCellValue();
								if (types[k].equalsIgnoreCase("Date")) {
									value = "to_date('" + value + "'," + "'yyyy/mm/dd hh24:mi:ss')";
								}else if(heads[k].equalsIgnoreCase("DESC_INFO")){
									value= "'"+value.replaceAll("#", StringUtils.S001)+ "'";
								}else {
								
									value = "'" + value + "'";
								}

							} else {
								value = "''";
							}

							sqlValueBuilder.append(value).append(",");
						}
						sqlValueBuilder.delete(sqlValueBuilder.lastIndexOf(","),
								sqlValueBuilder.length() + 1);
						sqlValueBuilder.append(")");
						String sql = sqlFieldBuilder.toString() + sqlValueBuilder.toString();
						sqlList.add(sql);
						System.out.println(sql);
					}
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sqlList;
	}

	public static void main(String[] args) {
		File f = new File("D:\\gsworkspace\\out_file\\table.xls");
		InputStream in;
		try {
			in = new FileInputStream(f);
			getSqlList(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
