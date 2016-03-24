package com.chinadaas.batch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.chinadaas.util.ClientTool;

public class SplitFile {
	static String pattern = "000";

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("|--start split file by line ");
		long start = System.currentTimeMillis();
		String filePath = ClientTool.getProperty("monitor.add.entlist.file");
		String maxLineStr = ClientTool.getProperty("monitor.add.input.file.maxline");
		int maxLineNum = Integer.parseInt(maxLineStr);
		String inputPath = ClientTool.getProperty("monitor.add.input.file.path");
		File input = new File(inputPath);
		// delete
		for (File f : input.listFiles()) {
			f.delete();
		}

		BufferedReader reader = null;
		int batchNo = 1;
		int count = 0;
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);

			String lineStr = null;
			DecimalFormat df = new DecimalFormat(pattern);
			String formatBatchNo = df.format(batchNo);
			File batchFile = new File(inputPath + "/" + formatBatchNo + ".txt");
			FileOutputStream fos = new FileOutputStream(batchFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

			System.out.println("|--start write  file   " + batchFile.getName());
			while ((lineStr = reader.readLine()) != null) {
				if (!lineStr.isEmpty()) {
					if (count > 0 && count % maxLineNum == 0) {
						fos.close();
						batchNo++;
						formatBatchNo = df.format(batchNo);
						batchFile = new File(inputPath + "/" + formatBatchNo + ".txt");
						fos = new FileOutputStream(batchFile);
						osw = new OutputStreamWriter(fos, "UTF-8");
						System.out.println("|--start write  file   " + batchFile.getName());

					}
					osw.write(lineStr + "\n");
					osw.flush();
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		long used = (end - start) / 1000;
		System.out.println("共读到" + count + "条记录 Used" + used + "S");

	}
}
