package mytools;

import java.util.List;

import org.junit.Test;

import com.chinadaas.gsinfo.query.front.relation.FamilyTreeFactory;
import com.chinadaas.gsinfo.query.front.relation.FamilyTreeNode;

public class TestFamilyNode {
	@Test
	public void testCreateNode() {
		FamilyTreeFactory factory = new FamilyTreeFactory();
		// 刘毅武
		String id = "173001631";
		
		//大连毅都集团有限公司
		id="205000725";
		
		//养牛人
		//id="321000714";
		FamilyTreeNode fnode = factory.createFamilyTree(id);
		fnode.traverse();

		System.out.println("=============first level=================");
		List<FamilyTreeNode> nodeListOne = fnode.getNodeListByLevel(1);
		printNodeList(nodeListOne);
		
		System.out.println("=============second level=================");
		List<FamilyTreeNode> nodeListTwo = fnode.getNodeListByLevel(2);
		printNodeList(nodeListTwo);

	}
	
	public static void printNodeList(List<FamilyTreeNode> nodeList){
		for (FamilyTreeNode node : nodeList) {
			System.out.println(node.getNodeName());
		}
	}

}
