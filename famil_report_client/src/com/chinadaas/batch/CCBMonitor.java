package com.chinadaas.batch;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.chinadaas.util.ClientTool;
/**
 * @deprecated

* <p>Description: </p>

* @author airsupply

* @date 2015年12月3日

* @version 1.0
 */
public class CCBMonitor {
	private static Logger log = Logger.getLogger(OperatorThread.class);
	public static void executeBatchAddMonitor() {
		long start = System.currentTimeMillis();
		String threadNumberStr = ClientTool.getProperty("monitor.add.thread.number");
		int threadNumber = Integer.parseInt(threadNumberStr);
		ExecutorService cachedThreadPool = Executors.newFixedThreadPool(threadNumber);

		String filePath = ClientTool.getProperty("monitor.add.input.file.path");
		File input = new File(filePath);
		
		for (File file : input.listFiles()) {
		
			List<String> inputRecords = ClientTool.readFileLineList(file.getAbsolutePath());
			for (String record : inputRecords) {
				if (record.split(",").length == 1) {
					String entName = record.split(",")[0].replaceAll("\"", "");
					// String entOrg = record.split(",")[1].replaceAll("\"",
					// "");
					System.out.println("|-- start " + entName);
					OperatorThread ot = new OperatorThread(entName.trim(), "",file.getName());
					cachedThreadPool.execute(ot);
				}
			}

			cachedThreadPool.shutdown();
			
			boolean isEnd=false;
			while (!isEnd) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (cachedThreadPool.isTerminated()) {
					long end = System.currentTimeMillis();
					long used = (end - start) / 1000;
					System.out.println(file.getName() + " 中的企业插入结束！Used" + used + "S");
					isEnd=true;
				}

			}
		}
	}

	public static void initWork() {
		String outFilePath = ClientTool.getProperty("monitor.add.out.file.path");
		File file = new File(outFilePath);
		for(File f:file.listFiles()){
			file.delete();
		}
	

	}

	public static void main(String[] args) {
		initWork();

		executeBatchAddMonitor();
	}

}
