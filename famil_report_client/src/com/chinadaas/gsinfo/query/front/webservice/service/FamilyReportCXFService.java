package com.chinadaas.gsinfo.query.front.webservice.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
@WebService(targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/")
public interface FamilyReportCXFService {
	@WebMethod
	@WebResult String getFamilyXml(@WebParam(name="encodeString",targetNamespace = "http://service.webservice.front.query.gsinfo.chinadaas.com/") String encodeString);
	
	
	 
}
