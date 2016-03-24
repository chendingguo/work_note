package com.chinadaas.batch;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.chinadaas.gsinfo.query.front.webservice.service.EntInfoQueryService;
import com.chinadaas.util.ClientTool;

public class CallbleOperatorThread implements Callable<String> {
	private static Logger log = Logger.getLogger(CallbleOperatorThread.class);
	private int retryNumber = 0;
	String retryMaxNumberStr = ClientTool.getProperty("monitor.add.retry.max.number");
	
	
	// 企业名称
	public String entName;
	// 组织机构代码
	public String entOrg;

	public String batchFile;

	public CallbleOperatorThread(String entName, String entOrg, String batchFile) {
		this.entName = entName;
		this.entOrg = entOrg;
		this.batchFile = batchFile;

	}

	public String addMonitorOrder() {
		//System.out.println("|-----------------add new monitor order:" + entName);
		String errorCode ="";
		EntInfoClient entInfoClient=new EntInfoClient();
		EntInfoQueryService entInfoService = entInfoClient.getEntInfoService();
		String queryXml = getMonitorOrderAddParameterXml();
		String encodeQueryXml = ClientTool.encryPostDataXml(queryXml);
		try {
			String resultXml = entInfoService.postMonitorOrderAdd(encodeQueryXml);
			String decodeResult = ClientTool.decryptResult(resultXml);
			 errorCode = getErrorCode(decodeResult);
			if (!errorCode.isEmpty()) {
				String outFilePath = ClientTool.getProperty("monitor.add.out.file.path");
				String content = entName + "," + errorCode;
				ClientTool.saveFile(outFilePath + "/" + batchFile, content + "\n");

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
		return entName+"#"+errorCode;
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
	public String call() throws Exception {
		return addMonitorOrder();
	}

}
