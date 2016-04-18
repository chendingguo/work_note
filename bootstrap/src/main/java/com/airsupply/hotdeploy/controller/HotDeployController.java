package com.airsupply.hotdeploy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/hotdeploy")
public class HotDeployController {
	@RequestMapping(value = "/uploadComponentFile", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uploadComponentFile(
			@RequestParam MultipartFile[] componentFiles,
			HttpServletRequest request) throws IOException {
		// 如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
		// 如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
		// 并且上传多个文件时，前台表单中的所有<input
		// type="file"/>的name都应该是myfiles，否则参数里的myfiles无法获取到所有上传的文件
		for (MultipartFile myfile : componentFiles) {
			if (myfile.isEmpty()) {
				System.out.println("文件未上传");
			} else {
				System.out.println("文件长度: " + myfile.getSize());
				System.out.println("文件类型: " + myfile.getContentType());
				System.out.println("文件名称: " + myfile.getName());
				System.out.println("文件原名: " + myfile.getOriginalFilename());
				System.out.println("========================================");
				String realPath = request.getSession().getServletContext()
						.getRealPath("/WEB-INF/upload");
				FileUtils.copyInputStreamToFile(myfile.getInputStream(),
						new File(realPath, myfile.getOriginalFilename()));
			}
		}
		return "success";
	}

	@RequestMapping(value = "/listComponentNames", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public List<String> listComponentNames(HttpServletRequest request)
			throws IOException {
		// TODO:File path
		List<String> componentFileList = new ArrayList<String>();
		File f = new File("D:\\lcworkspace\\output");
		File[] files = f.listFiles();
		for (File file : files) {
			componentFileList.add(file.getName());

		}

		return componentFileList;
	}
	
	@RequestMapping(value = "/showComponentInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showComponentInfo(String componentName,HttpServletRequest request)
			 {
		File componentFile = new File("D:\\lcworkspace\\output\\"+componentName);
		SAXReader reader = new SAXReader();              
	       try {
			Document   document = reader.read(componentFile);
			
			Element root=document.getRootElement();
			List<Element> elements=root.selectNodes("//quantum:plugin");
			for(Element e:elements){
				Attribute pluginIdAttr=e.attribute("id");
				System.out.println("***"+pluginIdAttr.getValue());
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}  
		return "return"+componentName;
	}

}
