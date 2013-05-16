package compressing;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.Arrays;

import tree.Leaf;
import tree.Node;
import tree.Tree;
import tree.TreeHelper;

import map.MapValueComparator;
import file.FileHelper;

public class CompressHelper {
	
	private static CompressHelper instance;
	private int paddingBits;
	private static final int HEADER_SIZE = 257;
	
	private CompressHelper() {
	}
	
	public static CompressHelper getInstance() {
		if (instance == null) {
			instance = new CompressHelper();
		}
		
		return instance;
	}
	
	public void CompressFile(String path) {
		String text = FileHelper.getInstance().readTextFile(path);
		TreeMap<Character, Integer> sortedFrequencyTable = this.getSortedFrequencyTable(text);
		System.out.println(sortedFrequencyTable);
		Tree tree = TreeHelper.getInstance().createTree(sortedFrequencyTable);
		HashMap<Character, String> charCodes = TreeHelper.getInstance().getCharCodes(tree);
		byte[] encodedText = encodeText(text, charCodes);
		byte[] encodedHeader = encodeHeader(sortedFrequencyTable);
		System.out.println("Text : " + encodedText.length);
		System.out.println("Header : " + encodedHeader.length);
		FileHelper.getInstance().writeToByteFile(path + ".huf", encodedHeader, encodedText);
	}
	
	public void DecompressFile(String path) {
		//byte[] fileContent = FileHelper.getInstance()
		
		File file = new File(path);
		byte[] fileBArray = new byte[(int)file.length()];
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(fileBArray);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("All : " + fileBArray.length);
		
		TreeMap<Character, Integer> sortedFrequencyTable = decodeHeader(fileBArray);
		Tree tree = TreeHelper.getInstance().createTree(sortedFrequencyTable);
		String text = decodeText(fileBArray, tree);
		System.out.println(text);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path + ".txt"));
			out.write(text);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private TreeMap<Character, Integer> getSortedFrequencyTable(String text) {
		HashMap<Character, Integer> frequencyTable = new HashMap<Character, Integer>();
		char[] textChars = text.toCharArray();
		
		for (int i = 0; i != textChars.length; ++i) {
			if (frequencyTable.containsKey(textChars[i]))
				frequencyTable.put(textChars[i], frequencyTable.get(textChars[i]) + 1);
			else
				frequencyTable.put(textChars[i], 1);
		}
		
		MapValueComparator mapValueComparator = new MapValueComparator(frequencyTable);
		TreeMap<Character, Integer> sortedFrequencyTable = new TreeMap<Character, Integer>(mapValueComparator);
		sortedFrequencyTable.putAll(frequencyTable);
		
		return sortedFrequencyTable;
	}
	
	private byte[] encodeText(String text, HashMap<Character, String> charCodes) {
		/*byte[] encodedText = new byte[text.length()];
		
		for (int i = 0; i != text.length(); ++i) {
			System.out.println(charCodes.get(text.charAt(i)));
			encodedText[i] = (byte)Integer.parseInt(charCodes.get(text.charAt(i)), 2);
		}*/
		
		//byte[] encodedText = null;
		/*String encodedTextString = "";
		for (int i = 0; i != text.length(); ++i) {
			System.out.println(charCodes.get(text.charAt(i)));
			encodedTextString += charCodes.get(text.charAt(i));
		}
		
		byte[] encodedText = {(byte)Integer.parseInt(encodedTextString, 2)};
		
		return encodedText;*/
		
		String encodedTextBits = "";
		
		for (int i = 0; i != text.length(); ++i) {
			encodedTextBits += charCodes.get(text.charAt(i));
		}
		
		System.out.println(encodedTextBits);
		
		ArrayList<String> stringBits = new ArrayList<String>();
		String currentByte = "";
		
		for (int i = 0; i != encodedTextBits.length(); ++i) {
			currentByte += encodedTextBits.charAt(i);
			
			if (currentByte.length() == 8 || i == encodedTextBits.length() - 1) {
				stringBits.add(currentByte);
				this.paddingBits = 8 - currentByte.length();
				currentByte = "";
			}
		}
		
		byte[] encodedTextBytes = new byte[stringBits.size()];
		
		for (int i = 0; i != stringBits.size(); ++i) {
			encodedTextBytes[i] = (byte)Integer.parseInt(stringBits.get(i), 2);
		}
		
		return encodedTextBytes;
	}
	
	private String decodeText(byte[] encodedTextBytes, Tree tree) {
		StringBuilder sbEncodedText = new StringBuilder();
		StringBuilder sbText = new StringBuilder();
		String padString = "00000000";
		String encodedTextPart = "";
		String encodedText = "";
		String text = "";
		
		for (int i = HEADER_SIZE; i != encodedTextBytes.length; ++i) {
			encodedTextPart = Integer.toBinaryString(encodedTextBytes[i] & 0xFF);
			if (i != encodedTextBytes.length - 1) {
				sbEncodedText.append(padString.substring(encodedTextPart.length()));
			} else {
				sbEncodedText.append(padString.substring(encodedTextPart.length()+this.paddingBits));
			}
			sbEncodedText.append(encodedTextPart);
		}
		
		encodedText = sbEncodedText.toString();
		System.out.println(encodedText);
		
		if (tree instanceof Node) {
			Node currentNode = (Node)tree;
			
			for (int i = 0; i != encodedText.length(); ++i) {
				if (encodedText.charAt(i) == '0') {
					if (currentNode.getLeftBranch() instanceof Node) {
						currentNode = (Node)currentNode.getLeftBranch();
					} else if (currentNode.getLeftBranch() instanceof Leaf) {
						sbText.append(((Leaf)currentNode.getLeftBranch()).getCharacter());
						currentNode = (Node)tree;
					}
				} else if (encodedText.charAt(i) == '1') {
					if (currentNode.getRightBranch() instanceof Node) {
						currentNode = (Node)currentNode.getRightBranch();
					} else if (currentNode.getRightBranch() instanceof Leaf) {
						sbText.append(((Leaf)currentNode.getRightBranch()).getCharacter());
						currentNode = (Node)tree;
					}
				}
				
			}
		} else if (tree instanceof Leaf) {
			Leaf currentLeaf = (Leaf)tree;
			
			for (int i = 0; i != encodedText.length(); ++i) {
				sbText.append(currentLeaf.getCharacter());
			}
		}
		
		text = sbText.toString();
		System.out.println(text);
		
		return text;
	}
	
	private byte[] encodeHeader(TreeMap<Character, Integer> sortedFrequencyTable) {
		int[] headerInt = new int[HEADER_SIZE-1];
		Arrays.fill(headerInt, 0);
		
		for (Map.Entry<Character, Integer> entry : sortedFrequencyTable.entrySet()) {
			headerInt[(byte)(char)entry.getKey()] = entry.getValue();
		}
		
		System.out.println("-----------------------------------------\r\n");
		
		for (int i = 0; i != headerInt.length; ++i) {
			if (headerInt[i] != 0) {
				System.out.print(headerInt[i] + " ");
			}
		}
		
		byte[] headerBytes = new byte[HEADER_SIZE];
		
		for (int i = 0; i != HEADER_SIZE - 1; ++i) {
			headerBytes[i] = (byte)headerInt[i];
		}
		
		headerBytes[HEADER_SIZE-1] = (byte)this.paddingBits;
		
		return headerBytes;
	}
	
	private TreeMap<Character, Integer> decodeHeader(byte[] encodedHeader) {
		HashMap<Character, Integer> frequencyTable = new HashMap<Character, Integer>();
		int value = 0;
		
		for (int i = 0; i != HEADER_SIZE - 1; ++i) {
			if ((value = encodedHeader[i] & 0xFF) != 0) {
				frequencyTable.put((char)i, value);
			}
		}
		
		MapValueComparator mapValueComparator = new MapValueComparator(frequencyTable);
		TreeMap<Character, Integer> sortedFrequencyTable = new TreeMap<Character, Integer>(mapValueComparator);
		sortedFrequencyTable.putAll(frequencyTable);
		
		System.out.println(sortedFrequencyTable);
		
		this.paddingBits = encodedHeader[HEADER_SIZE - 1] & 0xFF;
		
		return sortedFrequencyTable;
	}
	
	//http://stackoverflow.com/questions/10572398/how-can-i-easily-compress-and-decompress-strings-to-from-byte-arrays
	/*private byte[] compress(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(text.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }
	
	private String decompress(byte[] bytes) {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }*/
}
