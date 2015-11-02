package com.chinadaas.gsinfo.query.front.relation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.chinadaas.gsinfo.query.front.relation.vo.ChartVO;
import com.chinadaas.gsinfo.query.front.relation.vo.ConnectorVO;
import com.chinadaas.gsinfo.query.front.relation.vo.SetVO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FamilyReportUtil {
	static XStream xStream = new XStream(new DomDriver("utf8"));

	public static ChartVO getChartVO(String xml) {
		xStream.processAnnotations(ChartVO.class);
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("chart_test.xml");
		ChartVO chart = (ChartVO) xStream.fromXML(input);

		// re construct the connector
		List<ConnectorVO> connectorList = chart.getConnectors()
				.getConnectorList();
		for (int i = 0; i < connectorList.size(); i++) {

			reConstructConnector(connectorList.get(i));

		}

		return chart;

	}

	public static String readFile(InputStream input) {

		BufferedReader reader = null;
		String content = "";
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(input,
					"UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				content += tempString;

			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	/**
	 * 
	 * @param connector
	 * @return
	 */
	public static ConnectorVO reConstructConnector(ConnectorVO connector) {
		String trips = connector.getTrips();
		trips = trips.replaceAll("[{]", "").replaceAll("[}]", "");

		String[] keyValuArray = trips.split(",");
		for (String item : keyValuArray) {

			String[] arr = item.split("=");
			if (arr.length == 2) {
				String key = arr[0].trim();
				String value = arr[1].trim();
				if (key.equals("conprop")) {
					connector.setConprop(value);
				} else if (key.equals("invType")) {
					connector.setInvType(value);
				} else if (key.equals("postion")) {
					connector.setPostion(value);
				} else if (key.equals("tripsCny")) {
					connector.setTripsCny(value);
				} else if (key.equals("subconam")) {
					connector.setSubconam(value);
				} else if (key.equals("tripsScaleStr")) {
					connector.setTripsScaleStr(value);
				}
			}
		}
		return connector;

	}

	public List<LinkedHashMap<String, String>> getEntperiseInfo(ChartVO chart) {
		String[] cols = { "id", "name", "esdate", "tooltext", "regno", "",
				"industryphy", "entStatus", "regcap" };
		String[] colsNames = { "id", "名称", "企业成立日期", "节点描述", "工商注册号", "",
				"企业所属行业门类", "企业状态", "企业注册资金" };
		List<LinkedHashMap<String, String>> entList = new ArrayList<LinkedHashMap<String, String>>();
		List<SetVO> setList = chart.getDataset().getSetList();
		try {
			for (SetVO set : setList) {
				if (set.getRegno() != null && !set.getRegno().isEmpty()) {
					LinkedHashMap<String, String> propsMap = new LinkedHashMap<String, String>();
					for (String key : cols) {
						String methodName = "get"
								+ key.substring(0, 1).toUpperCase()
								+ key.substring(1);
						Method method = chart.getClass().getMethod(methodName);
						String value = "";
						if (method.invoke(chart) != null) {
							value = String.valueOf(method.invoke(chart));
						}
						propsMap.put(key, value);
					}

					entList.add(propsMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return entList;

	}
	
	
	public static void exportExcelFile(){
		
	}

}
