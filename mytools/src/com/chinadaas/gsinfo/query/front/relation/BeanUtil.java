package com.chinadaas.gsinfo.query.front.relation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanUtil {
	public static Map<String, String> confMap;

	public static String buildString(Object obj) {
		if (obj == null) {
			return "object is null";
		}
		StringBuilder builder = new StringBuilder();
		Method[] methods = obj.getClass().getMethods();
		builder.append("........................\n");

		for (Method method : methods) {
			String methodName = method.getName();
			if (method.getName().startsWith("get")
					&& !methodName.equals("getClass")
					&& (methodName.indexOf("NAME") > -1 || methodName
							.indexOf("ID") > -1)) {
				try {
					builder.append(method.getName()).append(":")
							.append(method.invoke(obj)).append("\n");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

		}

		return builder.toString();

	}

}
