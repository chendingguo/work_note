package com.airsupply.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class JsonCompareUtil {
	  
	    public static  void JsonObject2HashMap(JSONObject jo, List<Map<?, ?>> rstList) {  
	        for (Iterator<String> keys = jo.keys(); keys.hasNext();) {  
	            try {  
	                String key1 = keys.next();  
	                System.out.println("key1---" + key1 + "------" + jo.get(key1)  
	                        + (jo.get(key1) instanceof JSONObject) + jo.get(key1)  
	                        + (jo.get(key1) instanceof JSONArray));  
	                if (jo.get(key1) instanceof JSONObject) {  
	  
	                    JsonObject2HashMap((JSONObject) jo.get(key1), rstList);  
	                    continue;  
	                }  
	                if (jo.get(key1) instanceof JSONArray) {  
	                    JsonArray2HashMap((JSONArray) jo.get(key1), rstList);  
	                    continue;  
	                }  
	                System.out.println("key1:" + key1 + "----------jo.get(key1):"  
	                        + jo.get(key1));  
	                json2HashMap(key1, jo.get(key1), rstList);  
	  
	            } catch (JSONException e) {  
	                e.printStackTrace();  
	            }  
	  
	        }  
	  
	    }  
	    public static  void JsonArray2HashMap(JSONArray joArr,  
	            List<Map<?, ?>> rstList) {  
	        for (int i = 0; i < joArr.size(); i++) {  
	            try {  
	                if (joArr.get(i) instanceof JSONObject) {  
	  
	                    JsonObject2HashMap((JSONObject) joArr.get(i), rstList);  
	                    continue;  
	                }  
	                if (joArr.get(i) instanceof JSONArray) {  
	  
	                    JsonArray2HashMap((JSONArray) joArr.get(i), rstList);  
	                    continue;  
	                }  
	                System.out.println("Excepton~~~~~");  
	  
	            } catch (JSONException e) {  
	                e.printStackTrace();  
	            }  
	  
	        }  
	  
	    }  
	  
	    public static  void json2HashMap(String key, Object value,  
	            List<Map<?, ?>> rstList) {  
	        HashMap<String, Object> map = new HashMap<String, Object>();  
	        map.put(key, value);  
	        rstList.add(map);  
	    }  
	    
	    public static void main(String[] args) {
	    	String jsonstr = "{\"data\":{\"a\":[{\"a1\":\"aa1\",\"b1\":\"b1\"},{\"a2\":\"aa2\",\"b2\":\"bb2\"}]}}";
	    	String jsonstr2 = "{\"data\":{\"a\":[{\"a1\":\"aa1\",\"b1\":\"b1\"},{\"a2\":\"aa2\",\"b2\":\"bb2\"}]}}";
	    	JSONObject j=JSONObject.fromObject(jsonstr);
	    	List<Map<?, ?>> rstList=new ArrayList<Map<?,?>>();
	    	JsonObject2HashMap(j, rstList);
	    	
	    	JSONObject j2=JSONObject.fromObject(jsonstr2);
	    	List<Map<?, ?>> rstList2=new ArrayList<Map<?,?>>();
	    	JsonObject2HashMap(j2, rstList2);
	    	System.out.println("----------");
	    	System.out.println(rstList);
	    	System.out.println(rstList2);
	    	
	    	System.out.println(rstList.equals(rstList2));
		}
	}  


