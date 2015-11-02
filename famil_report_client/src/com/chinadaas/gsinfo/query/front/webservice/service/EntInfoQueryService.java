package com.chinadaas.gsinfo.query.front.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
@WebService(targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")
public interface EntInfoQueryService {
	@WebMethod
	@WebResult String queryEntInfoByKey(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/") String encodeString);
	
	@WebMethod
	@WebResult String queryEntInfoForXml(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")  String encodeString);
	
	@WebMethod
	@WebResult String queryDisHonestyForXml(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")  String encodeString);
	
	@WebMethod
	@WebResult String queryPerInfoForXml(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String postMonitorOrderAdd(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String postChangeOrderlistQuery(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String postChangeOrderlistQueryByUnread(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String postChangeDetailsQuery(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String postMonitorOrderDel(@WebParam String encodeString);
	
	@WebMethod
	@WebResult String queryEntInfoForLikeQuery(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")  String encodeString);
	
	@WebMethod
	@WebResult String queryEntInfoForSimRateLikeQuery(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")  String encodeString);
	
	@WebMethod
	@WebResult String queryPersonByCerNoForXml(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")  String encodeString);
	
	 
}
