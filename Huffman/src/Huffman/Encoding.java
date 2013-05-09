package Huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.TreeMap;
import Tree.*;
import Utilities.FileReaderWriter;
import Utilities.MapValueComparator;

public class Encoding {
	public void encrypt(String path)
	{
		HashMap<Byte, Integer> freqDic = FileReaderWriter.readFile(path);
		MapValueComparator mvc = new MapValueComparator(freqDic);
		TreeMap<Byte, Integer> sortedFreqDic = new TreeMap<Byte, Integer>(mvc);
		sortedFreqDic.putAll(freqDic);
		Tree tree = createTree(sortedFreqDic);
		HashMap<String, String> codestr = createTreeCode(tree, ""); // TODO return overwrite le dernier codestr
		encodeDocument(path, codestr);
	}
	
	private Tree createTree(TreeMap<Byte, Integer> sortedFreqDic)
	{
		ArrayDeque<Tree> tree = new ArrayDeque<Tree>();
		
	    for(Map.Entry<Byte, Integer> entry : sortedFreqDic.entrySet())
	      	  tree.offerLast(new Leaf((char) (int) entry.getKey(), entry.getValue()));

	    while (tree.size() > 1)
	    {
	    	Tree child1 = tree.poll();
	    	Tree child2 = tree.poll();
	    	tree.offerLast(new Node(child1, child2));
        }
	        
        return tree.element();
	}
	
	// TODO return overwrite le dernier codestr
	private HashMap<String, String> createTreeCode(Tree tree, String code)
	{
		HashMap<String, String> codestr = new HashMap<String, String>();
		
		if (tree instanceof Node)
		{
			Node node = (Node) tree;
			if (node.getLeftBranch() != null)
				createTreeCode(node.getLeftBranch(), code + "0");
			if (node.getRightBranch() != null)
				createTreeCode(node.getRightBranch(), code + "1");
		}
		if (tree instanceof Leaf)
		{
			Leaf leaf = (Leaf) tree;
			codestr.put(String.valueOf(leaf.getCharacter()), code);
		}
		
		return codestr;
	}
	
	private byte[] encodeDocument(String path, HashMap<String, String> codestr)
	{
		String str = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(path));		
			int content;
			while ((content = fis.read()) != -1) {
				str += codestr.get((char) content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str.getBytes();
	}
}
