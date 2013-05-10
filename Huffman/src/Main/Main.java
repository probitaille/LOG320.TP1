package Main;

import Huffman.Encoding;
import Huffman.Decoding;

public class Main {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		//temp var
		args = new String[2];
		args[0] = "-e";
		args[1] = "C:\\Users\\Marc-Andre\\Desktop\\test.txt";
		//--------
		if (args[0] == "-e")
		{
			System.out.println("Encrypt : " + args[1]);
			new Encoding().encrypt(args[1]);
		} 
		else if (args[0] == "-d")
		{
			System.out.println("Decrypt : " + args[1]);
			new Decoding().decrypt(args[1]);
		}
		else
		{
			System.out.println("Usage : \n\t-e <path>\tEncrypt file\n\t-d <path>\tDecrypt file");
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Execution time : " + (endTime - startTime) + " milliseconds");
	}
}
