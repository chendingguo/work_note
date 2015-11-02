package com.chinadaas.batch;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.chinadaas.gsinfo.query.front.webservice.service.EntInfoQueryService;
import com.chinadaas.util.ClientTool;
import com.ibm.wsdl.util.StringUtils;
/**
 * @deprecated

* <p>Description: </p>

* @author airsupply

* @date 2015年12月3日

* @version 1.0
 */
public class OperatorThread implements Runnable {
	private static Logger log = Logger.getLogger(OperatorThread.class);
	private int retryNumber = 0;
	String retryMaxNumberStr = ClientTool.getProperty("monitor.add.retry.max.number");
	EntInfoClient entInfoClient = new EntInfoClient();
	EntInfoQueryService entInfoService;
	// 企业名称
	public String entName;
	// 组织机构代码
	public String entOrg;

	public String batchFile;
	public OperatorThread(String entName, String entOrg,String batchFile) {

		this.entName = entName;
		this.entOrg = entOrg;
		this.batchFile=batchFile;

	}

	public void addMonitorOrder() {
		System.out.println("|-----------------add new monitor order:" + entName);
		
		entInfoService = entInfoClient.getEntInfoService();
		String queryXml = getMonitorOrderAddParameterXml();
		String encodeQueryXml = ClientTool.encryPostDataXml(queryXml);
		try {
			String resultXml = entInfoService.postMonitorOrderAdd(encodeQueryXml);
			String decodeResult = ClientTool.decryptResult(resultXml);
			String errorCode = getErrorCode(decodeResult);
			if (!errorCode.isEmpty()) {
				String outFilePath = ClientTool.getProperty("monitor.add.out.file.path");
				String content = entName + ":" + errorCode;
				ClientTool.saveFile(outFilePath+"/"+batchFile, content + "\n");

			}
		} catch (Exception e) {
			
			if (retryNumber < Integer.parseInt(retryMaxNumberStr)) {
				addMonitorOrder();
				retryNumber++;

			} else {
				log.error(entName + " retry " + retryNumber + " times failed");
				log.error(ClientTool.formatStackTrace(e));
			}

		}

	}

	public static String getErrorCode(String decodeResult) {
		String errorCode = "";
		Document doc;
		try {
			doc = DocumentHelper.parseText(decodeResult);
			@SuppressWarnings("unchecked")
			List<Node> nodes = doc.selectNodes("//ERRORCODE");
			if (nodes != null && nodes.size() > 0) {
				errorCode = nodes.get(0).getText();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return errorCode;

	}

	/**
	 * 监控新加订单
	 * 
	 * @return
	 */
	private String getMonitorOrderAddParameterXml() {
		String uid = ClientTool.getProperty("monitor.add.parameter.uid");
		String password = ClientTool.getProperty("monitor.add.parameter.password");
		String keyType = ClientTool.getProperty("monitor.add.parameter.keytype");
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element orderElement = doc.addElement("DATA").addElement("ORDER");
		orderElement.addElement("UID").setText(uid);
		orderElement.addElement("PASSWORD").setText(password);
		orderElement.addElement("KEY").setText(entName);
		orderElement.addElement("KEYTYPE").setText(keyType);
		return doc.asXML();
	}

	@Override
	public void run() {
		addMonitorOrder();
	}

}
