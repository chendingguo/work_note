package com.airsupply.encoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.chinadaas.gsinfo.core.utils.auth.Authcode;
import com.chinadaas.gsinfo.query.front.webservice.utils.RSAEncryption;
import com.chinadaas.gsinfo.query.front.webservice.utils.RandomTools;

public class TestEncode {
	public static void main(String[] args) throws UnsupportedEncodingException {

		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("name", "abcdfdsfdsa");
		System.out.println("----------------------------------");
		System.out.println(encryPostData());
		String decodeStr = decryptResult(encryPostData());
		System.out.println("----------------------------------");
		System.out.println(decodeStr);

	}

	private static Map<String, String> encryPostData() {
		Map<String, String> map = new HashMap<String, String>();
		String xmlkey = RandomTools.GenerateRandom();

		String xmlvalue = GetMonitorDelXmlStr("adaf", "adafdsa");
		Authcode authcode = new Authcode();
		// des加密
		String encryptXmlDataValue = null;
		try {
			encryptXmlDataValue = authcode.AuthcodeEncode(
					URLEncoder.encode(xmlvalue, "UTF-8"), xmlkey);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// RSA加密
		String encryptXmlDataKey = RSAEncryption.PublicEncrypt(xmlkey);
		map.put("key", encryptXmlDataKey);
		map.put("value", encryptXmlDataValue);
		return map;
	}

	private static String decryptResult(Map<String, String> decodeMap) {

		String desKey = decodeMap.get("key");
		String value = decodeMap.get("value");

		// RSA解密
		String desDecryptKey = RSAEncryption.PrivateDecrypt(desKey);
		// desDecryptKey =URLDecoder.decode(desDecryptKey, "UTF-8");

		// des解密
		String decryptXmlStr = new Authcode().AuthcodeDecode(value,
				desDecryptKey);

		try {
			decryptXmlStr = URLDecoder.decode(decryptXmlStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return decryptXmlStr;

	}

	private static String GetMonitorDelXmlStr(String uid, String orderNo) {
		Document doc = DocumentHelper.createDocument();

		doc.setXMLEncoding("UTF-8");
		Element elementRoot = doc.addElement("DATA").addElement("ORDER");
		elementRoot.addElement("UID").setText(uid);
		elementRoot.addElement("KEY").setText(orderNo);

		return doc.asXML();
	}

}
