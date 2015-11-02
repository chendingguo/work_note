package com.airsupply.hdfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.common.utils.StringUtils;

public class FileUtils {
	static Configuration conf=null;
	public static void readFile() throws IOException{
		conf=HBaseConfiguration.create();
	    String compareFilePath="/hive1/user/hive/warehouse/enterprisebaseinfocollect_20150510_compare";
		
		FileSystem hdfs = FileSystem.get(conf);
		
		
		List<String> paths=getHDFSPaths(compareFilePath,conf);
		for(String path:paths){
			Path partPath = new Path(path);
			FSDataInputStream dis = hdfs.open(partPath);
			BufferedReader in=new BufferedReader(new InputStreamReader(dis), 1024*1024*10);
			String value=null;
			int index=0;
			while((value=in.readLine())!=null){//逐行读取
				System.out.println(value);
				String[] values=value.split("\t", -1);
				String oldKey=values[0];
				String newKey=values[2];
				System.out.println(oldKey);
				index++;
				if (index>1){
					return ;
				}
				
				
			}
			
		}
	
		
	}
	
	public static List<String> getHDFSPaths( String path,Configuration conf) throws IOException{
		List<String> result =new ArrayList<String>();
		FileSystem fs = FileSystem.get(URI.create(path), conf);

		FileStatus fileList[] = fs.listStatus(new Path(path.toLowerCase()));
		for(FileStatus fi : fileList){
			if(!fi.isDirectory()){
				result.add(fi.getPath().toString());
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		try {
			readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
