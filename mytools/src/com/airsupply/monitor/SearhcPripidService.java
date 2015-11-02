package com.airsupply.monitor;

import java.io.IOException;
import java.util.List;

import com.airsupply.hbase.HbaseDaoService;
import com.airsupply.util.MyUtil;

public class SearhcPripidService {
	public static String entNameListFile = "D:\\gsworkspace\\input_file\\ent_name.txt";
	public static String  ent_pripidFile="D:\\gsworkspace\\out_file\\ent_pripid.txt";

	public static void main(String[] args) {
		HbaseDaoService hbaseDaoService = HbaseDaoService.getInstance();
		List<String> entNameList = MyUtil.readFileLineList(entNameListFile);
		for (String entName : entNameList) {
			try {
				String pripid = hbaseDaoService.getResultScann(HbaseDaoService.entIndexTableName,
						entName);
				if(pripid.split("\u0001").length==3){
					String[] pripidArray=pripid.split("\u0001");
					pripid=pripidArray[0]+","+pripidArray[1]+","+pripidArray[2];
				}
				String content=entName+","+pripid;
				MyUtil.saveFile(ent_pripidFile, content);
				System.out.println(entName + ":" + pripid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
