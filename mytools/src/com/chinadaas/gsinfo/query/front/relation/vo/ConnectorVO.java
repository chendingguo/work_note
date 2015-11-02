package com.chinadaas.gsinfo.query.front.relation.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("set")
public class ConnectorVO {
	@XStreamAsAttribute
	private String color;
	@XStreamAsAttribute
	private String trips;
	@XStreamAsAttribute
	private String from;
	@XStreamAsAttribute
	private String to;

	// trips='{conprop=0.0, invType=C1, postion=执行董事, regcap=2000.00,
	// tripsCny=null, subconam=0.00, tripsScaleStr=}'
	//-----------------------------------
	private String conprop;
	@XStreamOmitField
	private String invType;
	@XStreamOmitField
	private String postion;
	@XStreamOmitField
	private String tripsCny;
	@XStreamOmitField
	private String subconam;
	@XStreamOmitField
	private String tripsScaleStr;
	//------------------------------------

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTrips() {
		return trips;
	}

	public void setTrips(String trips) {
		this.trips = trips;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getConprop() {
		return conprop;
	}

	public void setConprop(String conprop) {
		this.conprop = conprop;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public String getPostion() {
		return postion;
	}

	public void setPostion(String postion) {
		this.postion = postion;
	}

	public String getTripsCny() {
		return tripsCny;
	}

	public void setTripsCny(String tripsCny) {
		this.tripsCny = tripsCny;
	}

	public String getSubconam() {
		return subconam;
	}

	public void setSubconam(String subconam) {
		this.subconam = subconam;
	}

	public String getTripsScaleStr() {
		return tripsScaleStr;
	}

	public void setTripsScaleStr(String tripsScaleStr) {
		this.tripsScaleStr = tripsScaleStr;
	}

}
