package com.chinadaas.gsinfo.query.front.relation.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("dataset")
public class DataSetVO {

	@XStreamAsAttribute
	String seriesname;
	@XStreamImplicit(itemFieldName = "set")
	private List<SetVO> setList;

	public String getSeriesname() {
		return seriesname;
	}

	public void setSeriesname(String seriesname) {
		this.seriesname = seriesname;
	}

	public List<SetVO> getSetList() {
		return setList;
	}

	public void setSetList(List<SetVO> setList) {
		this.setList = setList;
	}

}
