package com.airsupply.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.chinadaas.gsinfo.query.common.product.dao.entity.GroupCategory;

public class MyUtil {

	public static void writeObjectToFile(File file, Object obj) {

		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(obj);
			objOut.flush();
			objOut.close();
			System.out.println("write object success!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> T readObjectFromFile(File file, Class<T> t) {
		Object temp = null;
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			temp = objIn.readObject();
			objIn.close();
			System.out.println("read object success!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return t.cast(temp);
	}

	public static boolean checkFile(File f) {
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean saveFile(String path, String content) {

		try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static String readFile(String Path) {

		BufferedReader reader = null;
		String content = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				content += tempString;

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	public static List<String> readFileLineList(String Path) {
		List<String> lineList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				lineList.add(tempString);

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lineList;
	}

	public static String[] changeUserRole(String[] ids) {
		if (ids != null && ids.length > 0) {
			ids[0] = "3296";
		}
		return ids;
	}

	


	public static List<GroupCategory> getAddGroupCategorys() {

		List<GroupCategory> gcs = new ArrayList<GroupCategory>();
		GroupCategory groupCategory = new GroupCategory();
		groupCategory.setId("18");
		groupCategory.setFuncCode("ent");
		groupCategory.setGroupCodes("d1");
		groupCategory.setOrderNum(7);
		groupCategory.setState("1");
		gcs.add(groupCategory);
		return gcs;
	}
	
	
	public static String getConfigValue(String fileName,String key) {
		InputStream in=MyUtil.class.getClassLoader().getResourceAsStream(fileName);
		try {
			Properties prop = new Properties();
			prop.load(in);
			return prop.getProperty(key);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	

}
