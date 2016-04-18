package com.airsupply.hotdeploy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/httpTest")
public class HttpTestController {
	
	@RequestMapping(value = "/compareResult", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String compareResult(HttpServletRequest request)
			throws IOException {
				return null;
		
	}
	

}
