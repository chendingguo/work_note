package com.chinadaas.gsinfo.query.front.relation.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("set")
public class SetVO {
	@XStreamAsAttribute
	String id;
	@XStreamAsAttribute
	String pripid;
	@XStreamAsAttribute
	String shape;
	@XStreamAsAttribute
	String color;
	@XStreamAsAttribute
	String tooltext;

	// enterprise info
	@XStreamAsAttribute
	String regno;
	@XStreamAsAttribute
	String esdate;
	@XStreamAsAttribute
	String invType;
	@XStreamAsAttribute
	String regcap;
	@XStreamAsAttribute
	String name;
	@XStreamAsAttribute
	String width;

	@XStreamAsAttribute
	String entStatus;
	@XStreamAsAttribute
	String industryphy;

	// person info

	@XStreamAsAttribute
	String certNo;

	@XStreamAsAttribute
	String radius;

	public String getRegno() {
		return regno;
	}

	public void setRegno(String regno) {
		this.regno = regno;
	}

	public String getEsdate() {
		return esdate;
	}

	public void setEsdate(String esdate) {
		this.esdate = esdate;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTooltext() {
		return tooltext;
	}

	public void setTooltext(String tooltext) {
		this.tooltext = tooltext;
	}

	public String getPripid() {
		return pripid;
	}

	public void setPripid(String pripid) {
		this.pripid = pripid;
	}

	public String getRegcap() {
		return regcap;
	}

	public void setRegcap(String regcap) {
		this.regcap = regcap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntStatus() {
		return entStatus;
	}

	public void setEntStatus(String entStatus) {
		this.entStatus = entStatus;
	}

	public String getIndustryphy() {
		return industryphy;
	}

	public void setIndustryphy(String industryphy) {
		this.industryphy = industryphy;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

}
