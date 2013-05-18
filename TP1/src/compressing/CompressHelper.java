package compressing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
	private int encodedFileTextBeginIndex;
	
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
		
		Tree tree = TreeHelper.getInstance().createTree(sortedFrequencyTable);
		
		HashMap<Character, String> charCodes = TreeHelper.getInstance().getCharCodes(tree);
		
		byte[] encodedText = encodeText(text, charCodes);
		
		byte[] encodedHeader = encodeHeader(sortedFrequencyTable);
		
		FileHelper.getInstance().writeToByteFile(path + ".huf", encodedHeader, encodedText);
	}
	
	public void DecompressFile(String path) {
		byte[] encodedFileContent = FileHelper.getInstance().readByteFile(path);
		TreeMap<Character, Integer> sortedFrequencyTable = decodeHeader(encodedFileContent);
		Tree tree = TreeHelper.getInstance().createTree(sortedFrequencyTable);
		String text = decodeText(encodedFileContent, tree);
		FileHelper.getInstance().writeToTextFile(path + ".txt", text);
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
		String encodedTextBits = "";
		StringBuffer sbEncodedTextBits = new StringBuffer();
		
		for (int i = 0; i != text.length(); ++i) {
			sbEncodedTextBits.append(charCodes.get(text.charAt(i)));
		}
		encodedTextBits = sbEncodedTextBits.toString();
		
		ArrayList<String> stringBits = new ArrayList<String>();
		StringBuffer sbEncodedTextBytes = new StringBuffer();
		
		for (int i = 0; i != encodedTextBits.length(); ++i) {
			sbEncodedTextBytes.append(encodedTextBits.charAt(i));
			
			if (sbEncodedTextBytes.length() == 8 || i == encodedTextBits.length() - 1) {
				stringBits.add(sbEncodedTextBytes.toString());
				this.paddingBits = 8 - sbEncodedTextBytes.length();
				sbEncodedTextBytes.delete(0, sbEncodedTextBytes.length());
			}
		}
		
		byte[] encodedTextBytes = new byte[stringBits.size()];
		
		for (int i = 0; i != stringBits.size(); ++i) {
			encodedTextBytes[i] = (byte)Integer.parseInt(stringBits.get(i), 2);
		}
		
		return encodedTextBytes;
	}
	
	private String decodeText(byte[] encodedFileContent, Tree tree) {
		StringBuilder sbEncodedText = new StringBuilder();
		StringBuilder sbText = new StringBuilder();
		String padString = "00000000";
		String encodedTextPart = "";
		String encodedText = "";
		String text = "";
		
		for (int i = this.encodedFileTextBeginIndex; i != encodedFileContent.length; ++i) {
			encodedTextPart = Integer.toBinaryString(encodedFileContent[i] & 0xFF);
			if (i != encodedFileContent.length - 1) {
				sbEncodedText.append(padString.substring(encodedTextPart.length()));
			} else {
				sbEncodedText.append(padString.substring(encodedTextPart.length()+this.paddingBits));
			}
			sbEncodedText.append(encodedTextPart);
		}
		
		encodedText = sbEncodedText.toString();
		
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
		
		return text;
	}
	
	private byte[] encodeHeader(TreeMap<Character, Integer> sortedFrequencyTable) {
		int[] headerInt = new int[256];
		Arrays.fill(headerInt, 0);
		
		for (Map.Entry<Character, Integer> entry : sortedFrequencyTable.entrySet()) {
			headerInt[(byte)(char)entry.getKey()] = entry.getValue();
		}
		
		String charInFileBits = "";
		String padString = "00000000000000000000000000000000";
		StringBuilder sbCharInFileBits = new StringBuilder();
		StringBuilder sbCharFrequencyInFileBits = new StringBuilder();
		String frequencyBits = "";
		
		for (int i = 0; i != headerInt.length; ++i) {
			if (headerInt[i] == 0) {
				sbCharInFileBits.append('0');
			} else {
				sbCharInFileBits.append('1');
				frequencyBits = Integer.toBinaryString(headerInt[i]);
				sbCharFrequencyInFileBits.append(padString.substring(frequencyBits.length()));
				sbCharFrequencyInFileBits.append(frequencyBits);
			}
		}
		
		charInFileBits = sbCharInFileBits.append(sbCharFrequencyInFileBits.toString()).toString();
		
		ArrayList<String> stringBits = new ArrayList<String>();
		StringBuffer sbEncodedTextBytes = new StringBuffer();
		
		for (int i = 0; i != charInFileBits.length(); ++i) {
			sbEncodedTextBytes.append(charInFileBits.charAt(i));
			
			if (sbEncodedTextBytes.length() == 8 || i == charInFileBits.length() - 1) {
				stringBits.add(sbEncodedTextBytes.toString());
				sbEncodedTextBytes.delete(0, sbEncodedTextBytes.length());
			}
		}
		
		byte[] headerBytes = new byte[stringBits.size() + 1];
		
		for (int i = 0; i != stringBits.size(); ++i) {
			headerBytes[i + 1] = (byte)Integer.parseInt(stringBits.get(i), 2);
		}
		
		headerBytes[0] = (byte)this.paddingBits;
		
		return headerBytes;
	}
	
	private TreeMap<Character, Integer> decodeHeader(byte[] encodedFileContent) {
		HashMap<Character, Integer> frequencyTable = new HashMap<Character, Integer>();
		this.paddingBits = encodedFileContent[0] & 0xFF;
		
		StringBuilder sbEncodedHeader = new StringBuilder();
		String padString = "00000000";
		String encodedHeaderPart = "";
		String encodedHeader = "";
		
		for (int i = 1; i != 33; ++i) {
			encodedHeaderPart = Integer.toBinaryString(encodedFileContent[i] & 0xFF);
			sbEncodedHeader.append(padString.substring(encodedHeaderPart.length()));
			sbEncodedHeader.append(encodedHeaderPart);
		}
		
		encodedHeader = sbEncodedHeader.toString();
		
		int[] frequencies = new int[256];
		Arrays.fill(frequencies, 0);
		int currentIndex = 33;
		
		
		for (int i = 0; i != encodedHeader.length(); ++i) {
			if (encodedHeader.charAt(i) == '1') {
				StringBuilder sbEncodedFrequencies = new StringBuilder();
				String frequencyPart = "";
				frequencyPart = Integer.toBinaryString(encodedFileContent[currentIndex] & 0xFF);
				sbEncodedFrequencies.append(padString.substring(frequencyPart.length()));
				sbEncodedFrequencies.append(frequencyPart);
				frequencyPart = Integer.toBinaryString(encodedFileContent[currentIndex+1] & 0xFF);
				sbEncodedFrequencies.append(padString.substring(frequencyPart.length()));
				sbEncodedFrequencies.append(frequencyPart);
				frequencyPart = Integer.toBinaryString(encodedFileContent[currentIndex+2] & 0xFF);
				sbEncodedFrequencies.append(padString.substring(frequencyPart.length()));
				sbEncodedFrequencies.append(frequencyPart);
				frequencyPart = Integer.toBinaryString(encodedFileContent[currentIndex+3] & 0xFF);
				sbEncodedFrequencies.append(padString.substring(frequencyPart.length()));
				sbEncodedFrequencies.append(frequencyPart);
				currentIndex += 4;
				
				frequencyTable.put((char)i, Integer.parseInt(sbEncodedFrequencies.toString(), 2));
			}
		}
		
		this.encodedFileTextBeginIndex = currentIndex;
		
		MapValueComparator mapValueComparator = new MapValueComparator(frequencyTable);
		TreeMap<Character, Integer> sortedFrequencyTable = new TreeMap<Character, Integer>(mapValueComparator);
		sortedFrequencyTable.putAll(frequencyTable);
		
		return sortedFrequencyTable;
	}
	
}
