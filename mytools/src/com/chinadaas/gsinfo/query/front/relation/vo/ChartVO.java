package com.chinadaas.gsinfo.query.front.relation.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("chart")
public class ChartVO {

	@XStreamAsAttribute
	String xAxisMinValue;
	@XStreamAsAttribute
	String yAxisMaxValue;
	@XStreamAsAttribute
	String baseFontSize;
	@XStreamAsAttribute
	String palette;
	@XStreamAsAttribute
	String is3D;
	@XStreamAsAttribute
	String showFormBtn;
	@XStreamAsAttribute
	String xAxisMaxValue;
	@XStreamAsAttribute
	String baseFont;
	@XStreamAsAttribute
	String canvasBorderThickness;

	@XStreamAlias("dataset")
	private DataSetVO dataset;
	
	@XStreamAlias("connectors")
	private ConnectorsVO connectors;

	public String getxAxisMinValue() {
		return xAxisMinValue;
	}

	public void setxAxisMinValue(String xAxisMinValue) {
		this.xAxisMinValue = xAxisMinValue;
	}

	public String getyAxisMaxValue() {
		return yAxisMaxValue;
	}

	public void setyAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = yAxisMaxValue;
	}

	public String getBaseFontSize() {
		return baseFontSize;
	}

	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}

	public String getPalette() {
		return palette;
	}

	public void setPalette(String palette) {
		this.palette = palette;
	}

	public String getIs3D() {
		return is3D;
	}

	public void setIs3D(String is3d) {
		is3D = is3d;
	}

	public String getShowFormBtn() {
		return showFormBtn;
	}

	public void setShowFormBtn(String showFormBtn) {
		this.showFormBtn = showFormBtn;
	}

	public String getxAxisMaxValue() {
		return xAxisMaxValue;
	}

	public void setxAxisMaxValue(String xAxisMaxValue) {
		this.xAxisMaxValue = xAxisMaxValue;
	}

	public String getBaseFont() {
		return baseFont;
	}

	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}

	public String getCanvasBorderThickness() {
		return canvasBorderThickness;
	}

	public void setCanvasBorderThickness(String canvasBorderThickness) {
		this.canvasBorderThickness = canvasBorderThickness;
	}

	public DataSetVO getDataset() {
		return dataset;
	}

	public void setDataset(DataSetVO dataset) {
		this.dataset = dataset;
	}

	public ConnectorsVO getConnectors() {
		return connectors;
	}

	public void setConnectors(ConnectorsVO connectors) {
		this.connectors = connectors;
	}
	
	

}
