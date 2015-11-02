package com.chinadaas.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.chinadaas.command.RandomTools;
import com.chinadaas.command.tools.ClsSystem;
import com.chinadaas.rsa.RSAEncryption;
import com.chinadaas.test.EntMonitorClient;
import com.longcredit.common.Encryption.Authcode;

public class ClientTool {
	static Logger log = Logger.getLogger(EntMonitorClient.class);
	static Properties properties = new Properties();
	static {
		String path;
		try {
			path = URLDecoder.decode(ClsSystem.getClassesPath(), "utf-8") + "config.properties";
			InputStream inStream = new FileInputStream(path);
			properties.load(inStream);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeXmlFile(String path, Document document) throws IOException {

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("gbk");
		XMLWriter writer = new XMLWriter(new FileWriter(path), format);
		writer.write(document);
		writer.close();
	}

	/** 格式化异常信息 ,将异常信息转乘成字符串 */
	public static String formatStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		try {
			PrintWriter p = new PrintWriter(sw);
			t.printStackTrace(p);
		} catch (Exception ex) {
		}
		return sw.toString();
	}

	public static String getConfigPath() {
		String path = null;
		try {
			path = URLDecoder.decode(ClsSystem.getClassesPath(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String encryPostDataXml(String queryXml) {
		try {
			String xmlkey = RandomTools.GenerateRandom();
			Authcode authcode = new Authcode();
			// des加密
			String encryptXmlDataValue = null;
			try {
				encryptXmlDataValue = authcode.AuthcodeEncode(URLEncoder.encode(queryXml, "UTF-8"),
						xmlkey);
			} catch (UnsupportedEncodingException e) {
				log.error(ClientTool.formatStackTrace(e));
				e.printStackTrace();
			}
			if (encryptXmlDataValue == null) {
				// DES加密失败\
				return null;
			}
			// RSA加密
			String encryptXmlDataKey = RSAEncryption.PublicEncrypt(xmlkey);
			if (encryptXmlDataKey == null) {
				// RSA加密失败\
				log.error("RSA加密失败");
				return null;
			}
			Document doc = DocumentHelper.createDocument();
			doc.setXMLEncoding("UTF-8");
			Element data = doc.addElement("DATA");
			Element keyElement = data.addElement("KEY");
			keyElement.setText(encryptXmlDataKey);
			Element valueElement = data.addElement("VALUE");
			valueElement.setText(encryptXmlDataValue);

			return data.asXML();
		} catch (Exception e) {
			System.out.println("参数加密失败");
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> getParametersFromXml(String xmlStr, String parentNodeName) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		Document doc;
		try {
			doc = DocumentHelper.parseText(xmlStr);
			List<Element> orderList = doc.selectNodes("//" + parentNodeName);
			List<Element> elementList = orderList.get(0).elements();
			for (Element element : elementList) {
				parameterMap.put(element.getName(), element.getText().trim());
			}
		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return parameterMap;
	}

	public static String decryptResult(String xml) {

		Map<String, String> map = getParametersFromXml(xml, "DATA");
		String desKey = map.get("KEY");
		String value = map.get("VALUE");
		// RSA解密
		// String desDecryptKey = RSAEncryption.PrivateDecrypt(desKey);
		// 正式环境中 返回结果私钥加密,此处公钥解密
		String desDecryptKey = RSAEncryption.PublicDecrypt(desKey);
		// des解密
		String decryptXmlStr = new Authcode().AuthcodeDecode(value, desDecryptKey);
		try {
			decryptXmlStr = URLDecoder.decode(decryptXmlStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return decryptXmlStr;

	}

	public static List<String> readFileLineList(String Path) {
		List<String> lineList = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
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
