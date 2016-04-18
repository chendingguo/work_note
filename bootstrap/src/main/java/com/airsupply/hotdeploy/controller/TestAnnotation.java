package com.airsupply.hotdeploy.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class TestAnnotation {
	
	 public static void main(String[] args) {
		 Annotation[]  annotatios= Test.class.getAnnotations();{
			 for(Annotation a :annotatios){
				 MethodInfo methodInfo=(MethodInfo)a;
				 System.out.println(methodInfo.comment());
			 }
			 
		 }
	        for (Method method: Test.class.getMethods()) {
	            if (method.isAnnotationPresent(MethodInfo.class)) {
	                for (Annotation annotation:method.getAnnotations()) {
	                    System.out.println(annotation + " in method:"+ method);
	                }

	                MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);

	                System.out.println(methodInfo.comment());
	            }
	        }
	    }

}
