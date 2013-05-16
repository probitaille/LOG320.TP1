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
		
		String path = "C:\\Users\\Samuel\\Desktop\\carotte\\rambo.txt";
		
		//int i = 112;
		//Character v = (char)i;
		//System.out.println(v);
		
		//System.out.println(String.format("%08d", Integer.parseInt("11111")));
		//System.out.println("00000000".substring("11111".length())+"11111");
		
		//System.out.println(((int)(byte)112) & 0xFF);
		
		CompressHelper.getInstance().CompressFile(path);
		CompressHelper.getInstance().DecompressFile(path + ".huf");
		
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time : " + (endTime - startTime) + " milliseconds");
	}

}
