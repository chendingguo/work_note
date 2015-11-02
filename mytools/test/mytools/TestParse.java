package mytools;

import java.io.InputStream;

import com.chinadaas.gsinfo.query.front.relation.FamilyReportUtil;
import com.chinadaas.gsinfo.query.front.relation.vo.ChartVO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TestParse {
	static XStream xStream = new XStream(new DomDriver("utf8"));
	public static void main(String[] args) {
		xStream.processAnnotations(ChartVO.class);
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("chart_test.xml");
		String xml=FamilyReportUtil.readFile(input);
		ChartVO chart=FamilyReportUtil.getChartVO(xml);
		System.out.println(chart.getDataset().getSetList().get(1).getCertNo());
		for(int i=0;i<30;i++){
			System.out.println(chart.getConnectors().getConnectorList().get(i).getInvType());
		}
		
	}
	
	
	

}
