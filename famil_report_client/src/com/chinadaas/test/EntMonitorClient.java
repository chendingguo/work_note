package com.chinadaas.test;

import java.io.File;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import com.chinadaas.gsinfo.query.front.webservice.service.EntInfoQueryService;
import com.chinadaas.util.ClientTool;
public class EntMonitorClient {
	private static Logger log = Logger.getLogger(EntMonitorClient.class);
	static EntInfoQueryService entInfoService;

	public static void main(String[] args) {
		String url = ClientTool.getProperty("monitor.service.url");
		JaxWsProxyFactoryBean j = new JaxWsProxyFactoryBean();
		j.setAddress(url);
		j.setServiceClass(EntInfoQueryService.class);
		entInfoService = (EntInfoQueryService) j.create();
		addMonitorOrder();
	}

	public static void addMonitorOrder() {
		System.out.println("------------------------------------------------add new monitor order");
		String queryXml = getMonitorOrderAddParameterXml();
		String encodeQueryXml = ClientTool.encryPostDataXml(queryXml);
		String resultXml = entInfoService.postMonitorOrderAdd(encodeQueryXml);
		String decodeResult = ClientTool.decryptResult(resultXml);
		System.out.println(decodeResult);
	}

	private static void getMonitorDetail() {
		System.out.println("------------------------------------------------get monitor detail");
		try {

			String queryXml = getMonitorDetailQueryParameterXml();
			log.error("request xml :" + queryXml);
			System.out.println(queryXml);
			String encodeQueryXml = ClientTool.encryPostDataXml(queryXml);
			String resultXml = entInfoService.postChangeDetailsQuery(encodeQueryXml);
			String decodeResult = ClientTool.decryptResult(resultXml);
			File file = new File(ClientTool.getConfigPath());
			String path = file.getParentFile().getAbsolutePath() + File.separator
					+ "monitor-result.xml";

			Document doc = DocumentHelper.parseText(decodeResult);
			Node errorCodeNode = doc.selectSingleNode("//ERRORCODE");
			String errorCode = errorCodeNode.getText();
			if (errorCode != null && !errorCode.isEmpty()) {
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

	

	/**
	 * 监控新加订单
	 * 
	 * @return
	 */
	private static String getMonitorOrderAddParameterXml() {
		String uid = ClientTool.getProperty("monitor.add.parameter.uid");
		String password = ClientTool.getProperty("monitor.add.parameter.password");
		String orderNo = ClientTool.getProperty("monitor.add.parameter.key");
		String changeNo = ClientTool.getProperty("monitor.add.parameter.keytype");

		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element orderElement = doc.addElement("DATA").addElement("ORDER");
		orderElement.addElement("UID").setText(uid);
		orderElement.addElement("PASSWORD").setText(password);
		orderElement.addElement("KEY").setText(orderNo);
		orderElement.addElement("KEYTYPE").setText(changeNo);
		return doc.asXML();
	}

	/**
	 * 监控详情查询参数
	 * 
	 * @return
	 */
	private static String getMonitorDetailQueryParameterXml() {
		String uid = ClientTool.getProperty("monitor.parameter.uid");
		String orderNo = ClientTool.getProperty("monitor.parameter.orderno");
		String changeNo = ClientTool.getProperty("monitor.parameter.changeno");

		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element orderElement = doc.addElement("DATA").addElement("ORDER");
		orderElement.addElement("UID").setText(uid);
		orderElement.addElement("ORDERNO").setText(orderNo);
		orderElement.addElement("CHANGENO").setText(changeNo);
		return doc.asXML();
	}

}
