package com.chinadaas.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.chinadaas.command.RandomTools;
import com.chinadaas.gsinfo.query.front.webservice.service.FamilyReportCXFService;
import com.chinadaas.rsa.RSAEncryption;
import com.chinadaas.util.ClientTool;
import com.longcredit.common.Encryption.Authcode;

public class FamilyReportClient {
	private static Logger log = Logger.getLogger(FamilyReportClient.class);

	public static void main(String[] args) {
		getFamilyXml();
	}

	private static void getFamilyXml() {
		try {
			String url = ClientTool.getProperty("service.url");
			JaxWsProxyFactoryBean j = new JaxWsProxyFactoryBean();
			j.setAddress(url);
			j.setServiceClass(FamilyReportCXFService.class);
			FamilyReportCXFService familyReportService = (FamilyReportCXFService) j.create();
			String queryXml = getParameterXml();
			log.error("request xml :" + queryXml);
			System.out.println(queryXml);
			String encodeQueryXml = ClientTool.encryPostDataXml(queryXml);
			String resultXml = familyReportService.getFamilyXml(encodeQueryXml);
			String decodeResult =ClientTool.decryptResult(resultXml);
			File file=new File(ClientTool.getConfigPath());
			String path = file.getParentFile().getAbsolutePath() + File.separator+"result.xml";

			Document doc = DocumentHelper.parseText(decodeResult);
			Node errorCodeNode = doc.selectSingleNode("//ERRORCODE");
			String errorCode = errorCodeNode.getText();
			if (errorCode != null &&!errorCode.isEmpty()) {
				log.error("failed! " + doc.asXML());
				System.out.println("failed! " + doc.asXML());
			} else {
				ClientTool.writeXmlFile(path, DocumentHelper.parseText(decodeResult));
				log.error("success!请在 " + path + "查看返回结果");
				System.out.println("success!请在 " + path + "查看返回结果");
			}

		} catch (Exception e) {
			log.error(ClientTool.formatStackTrace(e));
			e.printStackTrace();
			System.out.println("failed");
		}

	}

	
	

	

	private static String getParameterXml() {
		String uid = ClientTool.getProperty("post.parameter.uid");
		String password = ClientTool.getProperty("post.parameter.password");
		String key = ClientTool.getProperty("post.parameter.key");
		String relationtype = ClientTool.getProperty("post.parameter.relationtype");
		String familytype = ClientTool.getProperty("post.parameter.familytype");
		String nodeexclude = ClientTool.getProperty("post.parameter.nodeexclude");
		String nodeexcludeamount = ClientTool.getProperty("post.parameter.nodeexcludeamount");
		String relationexcludeamount = ClientTool
				.getProperty("post.parameter.relationexcludeamount");
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element orderElement = doc.addElement("DATA").addElement("ORDER");
		orderElement.addElement("UID").setText(uid);
		orderElement.addElement("PASSWORD").setText(password);
		orderElement.addElement("KEY").setText(key);
		orderElement.addElement("RELATIONTYPE").setText(relationtype);
		orderElement.addElement("FAMILYTYPE").setText(familytype);
		orderElement.addElement("NODEEXCLUDE").setText(nodeexclude);
		orderElement.addElement("NODEEXCLUDEAMOUNT").setText(nodeexcludeamount);
		orderElement.addElement("RELATIONEXCLUDEAMOUNT").setText(relationexcludeamount);
		return doc.asXML();
	}

}
