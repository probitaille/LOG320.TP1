package Huffman;

import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.TreeMap;
import Tree.*;
import Utilities.FileReaderWriter;
import Utilities.MapValueComparator;

public class Encoding {
	private static HashMap<String, String> codestr = new HashMap<String, String>();
	private static String txtstr = "";
	
	public void encrypt(String path)
	{ 
		Object[] o = FileReaderWriter.readFile(path);
		@SuppressWarnings("unchecked")
		HashMap<Byte, Integer> freqDic = (HashMap<Byte, Integer>) o[0];
		txtstr = (String) o[1];
		MapValueComparator mvc = new MapValueComparator(freqDic);
		TreeMap<Byte, Integer> sortedFreqDic = new TreeMap<Byte, Integer>(mvc);
		sortedFreqDic.putAll(freqDic);
		Tree tree = createTree(sortedFreqDic);
		createTreeCode(tree, "");
		byte[] encStr = encodeDocument(txtstr);
		FileReaderWriter.writeFileBin(path + ".bin", encStr, tree);
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
	
	private void createTreeCode(Tree tree, String code)
	{	
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
	}
	
	private byte[] encodeDocument(String path)
	{
		char[] c = path.toCharArray();
		String str = "";
		for(char content : c)
		{
			str += codestr.get(String.valueOf((char) content));
		}
		return str.getBytes();
	}
}
