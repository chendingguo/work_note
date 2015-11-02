package com.chinadaas.gsinfo.query.front.relation;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class FamilyTreeFactory {
	/**
	 * 人:0
	 */
	String typePerson = "0";
	/**
	 * 企业:1
	 */
	String typeEnterprise = "1";
	Document document;

	public FamilyTreeFactory() {
		init();
	}

	public void init() {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(this.getClass().getClassLoader()
					.getResourceAsStream("chart.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public FamilyTreeNode createFamilyTree(String id) {
		FamilyTreeNode fRootNode = new FamilyTreeNode();
		int level = FamilyTreeLevelEnum.levelZero.getLevel();
		//获得当前节点属性
		StringBuilder xpathBuilder = new StringBuilder();
		xpathBuilder.append("//set");
		xpathBuilder.append("[@id='").append(id).append("']");
		String xPath = xpathBuilder.toString();
		List<Element> elements = document.selectNodes(xPath);
		if(elements!=null&&elements.size()==1){
			fRootNode =getSetNode(elements.get(0),level);
		}
		
		createNagativeLevelNode(fRootNode, id);
		createPositiveLevelNode(fRootNode, id, 1);
		return fRootNode;
	}

	/**
	 * 组装层级为-1的节点数据
	 */
	@SuppressWarnings("unchecked")
	public void createNagativeLevelNode(FamilyTreeNode fRootNode, String nodeId) {
		StringBuilder xpathBuilder = new StringBuilder();
		xpathBuilder.append("//connector");
		xpathBuilder.append("[@to='").append(nodeId).append("']");
		String xPath = xpathBuilder.toString();
		List<Element> connectorElements = document.selectNodes(xPath);
		for (Element connElement : connectorElements) {
			String fromId = connElement.attributeValue("from");
			// 获取节点属性
			xpathBuilder = new StringBuilder();
			xpathBuilder.append("//set");
			xpathBuilder.append("[@id='").append(fromId).append("']");
			xPath = xpathBuilder.toString();
			List<Element> setElements = document.selectNodes(xPath);
			for (Element setElement : setElements) {
				int level = FamilyTreeLevelEnum.levelNagativeOne.getLevel();
				FamilyTreeNode setNode = getSetNode(setElement, level);
				fRootNode.addChildNode(setNode);
			}

		}

	}

	@SuppressWarnings("unchecked")
	public void createPositiveLevelNode(FamilyTreeNode node,
			String nodeId, int level) {
		if (level > 2) {
			return;
		}
		StringBuilder xpathBuilder = new StringBuilder();
		xpathBuilder.append("//connector");
		xpathBuilder.append("[@from='").append(nodeId).append("']");
		String xPath = xpathBuilder.toString();
		List<Element> connectorElements = document.selectNodes(xPath);
		for (Element connElement : connectorElements) {
			String toId = connElement.attributeValue("to");
			// 获取节点属性
			xpathBuilder = new StringBuilder();
			xpathBuilder.append("//set");
			xpathBuilder.append("[@id='").append(toId).append("']");
			xPath = xpathBuilder.toString();
			List<Element> setElements = document.selectNodes(xPath);
			for (Element setElement : setElements) {

				FamilyTreeNode setNode = getSetNode(setElement, level);
				setNode.setParentId(nodeId);
				node.addChildNode(setNode);
				// 递归
				String id = setElement.attributeValue("id");
				int nextLevel=level+1;
				createPositiveLevelNode(setNode, id, nextLevel);
				
			}

		}

	}

	/**
	 * 组装Set节点的node
	 * 
	 * @param setElement
	 * @param level
	 * @return
	 */
	public FamilyTreeNode getSetNode(Element setElement, int level) {
		FamilyTreeNode node = new FamilyTreeNode();
		// 企业节点
		if (setElement.attributeValue("shape").equalsIgnoreCase("RECTANGLE")) {
			String id = setElement.attributeValue("id");
			String entName = setElement.attributeValue("name");
			EnterpriseBaseInfo entInfo = new EnterpriseBaseInfo();
			String regno = setElement.attributeValue("regno");
			String esdate = setElement.attributeValue("esdate");
			String invType = setElement.attributeValue("invType");
			String pripid = setElement.attributeValue("pripid");
			String regcap = setElement.attributeValue("regcap");
			String entStatus = setElement.attributeValue("entStatus");
			String industryphy = setElement.attributeValue("industryphy");
			entInfo.setDaasID(id);
			entInfo.setDaasENTNAME(entName);
			entInfo.setDaasREGNO(regno);
			entInfo.setDaasESDATE(esdate);
			entInfo.setDaasINVTYPE(invType);
			entInfo.setDaasPRIPID(pripid);
			entInfo.setDaasENTSTATUS(entStatus);
			entInfo.setDaasREGCAP(regcap);
			entInfo.setDaasENTSTATUS(entStatus);
			entInfo.setDaasINDUSTRYPHY(industryphy);
			node.setNodeLevel(level);
			node.setSelfId(id);
			node.setNodeName(entName);
			node.setObj(entInfo);
			node.setNodeType(typeEnterprise);
		} else if (setElement.attributeValue("shape")
				.equalsIgnoreCase("CIRCLE")) {
			ManagerInfo managerInfo = new ManagerInfo();
			String cerNo = setElement.attributeValue("certNo");
			String name = setElement.attributeValue("name");
			String id = setElement.attributeValue("id");
			String cerType = setElement.attributeValue("cerType");
			managerInfo.setDaasID(id);
			managerInfo.setDaasCERNO(cerNo);
			managerInfo.setDaasCERTYPE(cerType);
			managerInfo.setDaasNAME(name);
			node.setNodeLevel(level);
			node.setSelfId(id);
			node.setObj(managerInfo);
			node.setNodeName(name);
			node.setNodeType(typePerson);
		}
		return node;

	}
	
	

}
