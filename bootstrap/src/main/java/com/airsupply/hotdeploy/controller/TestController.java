package com.airsupply.hotdeploy.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.airsupply.model.*;

@Controller
@RequestMapping(value = "/test")
public class TestController {
	private static Log log=LogFactory.getLog(TestController.class);
	@RequestMapping(value = "/sayHello", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sayHello(HttpServletRequest request) throws IOException {
		String serviceName=request.getParameter("serviceName");
		Class<?> clazz;
		try {
			clazz = Class.forName("com.airsupply.model."+serviceName);
			ServiceInterface service = (ServiceInterface) clazz.newInstance();
			return service.getInfo();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
		} catch (InstantiationException e) {
			log.error(e.getMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
		}
		return "no service is found:" +serviceName;

	}

}
