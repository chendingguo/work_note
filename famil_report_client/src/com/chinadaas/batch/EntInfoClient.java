package com.chinadaas.batch;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;

import com.chinadaas.gsinfo.query.front.webservice.service.EntInfoQueryService;
import com.chinadaas.util.ClientTool;

public class EntInfoClient {
	// TODO:经常超时
	private static Logger log = Logger.getLogger(EntInfoClient.class);

	private static ThreadLocal<EntInfoQueryService> entInfoServiceThreadLocal = new ThreadLocal<EntInfoQueryService>() {
		public EntInfoQueryService initialValue() {
			String url = ClientTool.getProperty("monitor.service.url");
			JaxWsProxyFactoryBean j = new JaxWsProxyFactoryBean();
			j.setAddress(url);
			j.setServiceClass(EntInfoQueryService.class);
			EntInfoQueryService entInfoService = (EntInfoQueryService) j.create();
			entInfoServiceThreadLocal.set(entInfoService);
			return entInfoServiceThreadLocal.get();
		}
	};

	public EntInfoQueryService getEntInfoService() {
		return entInfoServiceThreadLocal.get();

	}

}
