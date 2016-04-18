package com.airsupply.ztree.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.airsupply.ztree.bean.Node;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/ztree")
public class NodesController {

	@RequestMapping(value = "/getNodes", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Node getNodes(HttpServletRequest request) {
		System.out.println("-------parameters-----------");
		System.out.println(
				request.getParameter("id") + "," + request.getParameter("name") + "," + request.getParameter("level"));
		System.out.println("-------parameters-----------");

		if (request.getParameter("level") != null) {
			int level = Integer.parseInt(request.getParameter("level"));
			Node asyncNode = getAsyncNode(level);
			return asyncNode;

		}
		Node root = new Node();
		root.setId("0");
		root.setName("枙子花开dfdf");
		root.setParent(true);
		root.setValue("0");

		// List<Node> childs = new ArrayList<Node>();
		// for (int i = 0; i < 5; i++) {
		// Node child = new Node();
		// child.setId(String.valueOf(i));
		// child.setName("node_" + i);
		// child.setValue(String.valueOf(i));
		// child.setParent(true);
		// childs.add(child);
		//
		// }
		// root.setChildren(childs);
		System.out.println(JSONObject.fromObject(root));

		return root;
	}

	public Node getAsyncNode(int level) {
		Node node = new Node();
		level = level + 1;
		node.setId("L_" + level);
		node.setName("L_" + level);

		List<Node> childs = new ArrayList<Node>();
		int childLevel = level + 1;
		for (int i = 0; i < 5; i++) {

			Node child = new Node();
			String id = "L_" + childLevel + "_" + i;
			String name = "L" + childLevel + "_name" + i;
			child.setId(id);
			child.setName(name);
			child.setValue(id);
			if (childLevel < 6) {
				child.setParent(true);
			}
			childs.add(child);

		}
		node.setChildren(childs);

		return node;

	}

}
