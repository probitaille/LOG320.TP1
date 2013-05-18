package main;

import compressing.CompressHelper;

public class TP1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		if (args.length == 2)
		{
			String path = args[1];
			if (args[0].equals("-e"))
				CompressHelper.getInstance().CompressFile(path);
			if (args[0].equals("-d"))
				CompressHelper.getInstance().DecompressFile(path);
		}
		else
		{
			System.out.println("Usage : \n\t-e <path>\tEncrypt file\n\t-d <path>\tDecrypt file");
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time : " + (endTime - startTime) + " milliseconds");
	}

}
