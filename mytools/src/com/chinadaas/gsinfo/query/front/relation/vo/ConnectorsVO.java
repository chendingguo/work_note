package com.chinadaas.gsinfo.query.front.relation.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("connectors")
public class ConnectorsVO {

	@XStreamAsAttribute
	String stdThickness;
	@XStreamImplicit(itemFieldName = "connector")
	private List<ConnectorVO> connectorList;
	
	
    
	public String getStdThickness() {
		return stdThickness;
	}
	public void setStdThickness(String stdThickness) {
		this.stdThickness = stdThickness;
	}
	public List<ConnectorVO> getConnectorList() {
		return connectorList;
	}
	public void setConnectorList(List<ConnectorVO> connectorList) {
		this.connectorList = connectorList;
	}
	
	

	

}
