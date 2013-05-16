package main;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import compressing.CompressHelper;

public class TP1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		String path = "C:\\Users\\Samuel\\Desktop\\carotte\\rambo2.txt";
		
		//CompressHelper.getInstance().CompressFile(path);
		CompressHelper.getInstance().DecompressFile(path + ".huf");
		
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time : " + (endTime - startTime) + " milliseconds");
	}

}
