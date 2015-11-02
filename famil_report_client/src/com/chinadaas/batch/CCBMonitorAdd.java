package com.chinadaas.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.chinadaas.util.ClientTool;

public class CCBMonitorAdd {
	private static Logger log = Logger.getLogger(OperatorThread.class);

	public static void executeBatchAddMonitor() {
		long start = System.currentTimeMillis();
		String threadNumberStr = ClientTool.getProperty("monitor.add.thread.number");
		int threadNumber = Integer.parseInt(threadNumberStr);
		String filePath = ClientTool.getProperty("monitor.add.input.file.path");
		File input = new File(filePath);

		for (File file : input.listFiles()) {

			System.out.println("|-- start " + file.getName());
			List<Future<String>> results = new ArrayList<Future<String>>();
			ExecutorService excutor = Executors.newFixedThreadPool(threadNumber);
			List<String> inputRecords = ClientTool.readFileLineList(file.getAbsolutePath());
			for (String record : inputRecords) {
				if (record.split(",").length == 1) {
					String entName = record.split(",")[0].replaceAll("\"", "");
					// String entOrg = record.split(",")[1].replaceAll("\"",
					// "");
					// System.out.println("|-- start " + entName);
					CallbleOperatorThread ot = new CallbleOperatorThread(entName.trim(), "",
							file.getName());
					results.add(excutor.submit(ot));
				}
			}

			for (Future<String> fs : results) {
				try {
					String str = String.valueOf(fs.get());
					if (!str.isEmpty() && str.split(":").length == 2) {
						String errorCode = str.split(":")[1];
						if (!errorCode.isEmpty()) {
							log.error("ERROR ENT:" + str);
						}
					}
				} catch (InterruptedException e) {
					log.error(e);
				} catch (ExecutionException e) {
					log.error(e);
				}
			}
			long end = System.currentTimeMillis();
			long used = (end - start) / 1000;
			excutor.shutdown();
			log.error(file.getName() + " 中的企业插入结束！Used" + used + "S");
		}
		long end = System.currentTimeMillis();
		long used = (end - start) / 1000;
		log.error("共用时" + used + "S");
		System.out.println("共用时" + used + "S");
	}

	public static void initWork() {
		String outFilePath = ClientTool.getProperty("monitor.add.out.file.path");
		File file = new File(outFilePath);
		for (File f : file.listFiles()) {
			f.delete();
		}

	}

	public static void main(String[] args) {
		initWork();
		executeBatchAddMonitor();
	}

}
