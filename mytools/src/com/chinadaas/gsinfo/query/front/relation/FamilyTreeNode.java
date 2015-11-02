package com.chinadaas.gsinfo.query.front.relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FamilyTreeNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private String parentId;
	private String selfId;
	protected String nodeName;
	protected Object obj;
	protected FamilyTreeNode parentNode;
	protected List<FamilyTreeNode> childList;
	// -1 ,0,1,2
	protected int nodeLevel;
	// 0 :企业　1:人
	protected String nodeType;

	public FamilyTreeNode() {
		initChildList();
	}

	public FamilyTreeNode(FamilyTreeNode parentNode) {
		this.getParentNode();
		initChildList();
	}

	public boolean isLeaf() {
		if (childList == null) {
			return true;
		} else {
			if (childList.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}
	}

	/* 插入一个child节点到当前节点中 */
	public void addChildNode(FamilyTreeNode treeNode) {
		initChildList();
		childList.add(treeNode);
	}

	public void initChildList() {
		if (childList == null)
			childList = new ArrayList<FamilyTreeNode>();
	}

	public boolean isValidTree() {
		return true;
	}

	/* 返回当前节点的父辈节点集合 */
	public List<FamilyTreeNode> getElders() {
		List<FamilyTreeNode> elderList = new ArrayList<FamilyTreeNode>();
		FamilyTreeNode parentNode = this.getParentNode();
		if (parentNode == null) {
			return elderList;
		} else {
			elderList.add(parentNode);
			elderList.addAll(parentNode.getElders());
			return elderList;
		}
	}

	/* 返回当前节点的晚辈集合 */
	public List<FamilyTreeNode> getJuniors() {
		List<FamilyTreeNode> juniorList = new ArrayList<FamilyTreeNode>();
		List<FamilyTreeNode> childList = this.getChildList();
		if (childList == null) {
			return juniorList;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				FamilyTreeNode junior = childList.get(i);
				juniorList.add(junior);
				juniorList.addAll(junior.getJuniors());
			}
			return juniorList;
		}
	}

	/* 返回当前节点的孩子集合 */
	public List<FamilyTreeNode> getChildList() {
		return childList;
	}

	/* 删除节点和它下面的晚辈 */
	public void deleteNode() {
		FamilyTreeNode parentNode = this.getParentNode();
		String id = this.getSelfId();

		if (parentNode != null) {
			parentNode.deleteChildNode(id);
		}
	}

	/* 删除当前节点的某个子节点 */
	public void deleteChildNode(String childId) {
		List<FamilyTreeNode> childList = this.getChildList();
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			FamilyTreeNode child = childList.get(i);
			if (child.getSelfId().equals(childId)) {
				childList.remove(i);
				return;
			}
		}
	}

	/* 动态的插入一个新的节点到当前树中 */
	public boolean insertJuniorNode(FamilyTreeNode treeNode) {
		String juniorParentId = treeNode.getParentId();
		if (this.parentId.equals(juniorParentId)) {
			addChildNode(treeNode);
			return true;
		} else {
			List<FamilyTreeNode> childList = this.getChildList();
			int childNumber = childList.size();
			boolean insertFlag;

			for (int i = 0; i < childNumber; i++) {
				FamilyTreeNode childNode = childList.get(i);
				insertFlag = childNode.insertJuniorNode(treeNode);
				if (insertFlag == true)
					return true;
			}
			return false;
		}
	}

	/* 找到一颗树中某个节点 */
	public FamilyTreeNode findTreeNodeById(String id) {
		if (this.selfId.equals(id))
			return this;
		if (childList.isEmpty() || childList == null) {
			return null;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				FamilyTreeNode child = childList.get(i);
				FamilyTreeNode resultNode = child.findTreeNodeById(id);
				if (resultNode != null) {
					return resultNode;
				}
			}
			return null;
		}
	}

	/* 遍历一棵树，层次遍历 */
	public void traverse() {
		if (selfId == null)
			return;
		print(this);
		if (childList == null || childList.isEmpty())
			return;
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			FamilyTreeNode child = childList.get(i);
			child.traverse();
		}
	}

	public void print(String content) {
		System.out.println(content);
	}

	public void print(FamilyTreeNode fNode) {
		System.out.println("========================");
		System.out.println("level:" + fNode.nodeLevel);
		System.out.println("parentId:" + fNode.parentId);
		System.out.println("selfId:" + fNode.selfId);

		String beanStr = BeanUtil.buildString(fNode.getObj());

		System.out.println(beanStr);

	}
 
	/**
	 * 按级别返回node list
	 * @param level 
	 * @return
	 */
	public List<FamilyTreeNode> getNodeListByLevel(int level) {
		List<FamilyTreeNode> nodeList = new ArrayList<FamilyTreeNode>();
		List<FamilyTreeNode> childList = this.getChildList();
		if (childList == null) {
			return nodeList;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				FamilyTreeNode junior = childList.get(i);
				if (junior.nodeLevel == level) {
					nodeList.add(junior);
				}
				nodeList.addAll(junior.getNodeListByLevel(level));
			}
			return nodeList;
		}

	}

	public void print(int content) {
		System.out.println(String.valueOf(content));
	}

	public void setChildList(List<FamilyTreeNode> childList) {
		this.childList = childList;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	public FamilyTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(FamilyTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public int getNodeLevel() {
		return nodeLevel;
	}

	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}

}