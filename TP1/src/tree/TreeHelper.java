package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TreeHelper {
	private static TreeHelper instance;
	private static HashMap<Character, String> codestr;
	
	private TreeHelper() {
		codestr = new HashMap<Character, String>();
	}
	
	public static TreeHelper getInstance() {
		if (instance == null) {
			instance = new TreeHelper();
		}
		
		return instance;
	}
	
	public Tree createTree(TreeMap<Character, Integer> sortedFrequencyTable) {
		ArrayList<Tree> tree = new ArrayList<Tree>();
		
		for (Map.Entry<Character, Integer> entry : sortedFrequencyTable.entrySet()) {
			tree.add(new Leaf(entry.getKey(), entry.getValue()));
		}
		
		while (tree.size() > 1) {
			Node node = new Node(tree.get(tree.size() - 1), tree.get(tree.size() - 2));
			tree.remove(tree.size() - 1);
			tree.remove(tree.size() - 1);
			
			int i = 0;
			boolean hasFoundIndex = false;
			
			while (i != tree.size() && !hasFoundIndex) {
				if (tree.get(i).getFrequency() < node.getFrequency()) {
					hasFoundIndex = true;
				} else {
					++i;
				}
			}
			
			tree.add(i, node);
		}
		
		return tree.get(0);
			
		/*ArrayDeque<Tree> tree = new ArrayDeque<Tree>();
		
	    for(Map.Entry<Character, Integer> entry : sortedFrequencyTable.entrySet())
	      	  tree.offerLast(new Leaf(entry.getKey(), entry.getValue()));

	    while (tree.size() > 1)
	    {
	    	Tree child1 = tree.poll();
	    	Tree child2 = tree.poll();
	    	tree.offerLast(new Node(child1, child2));
        }
	    
        return tree.element();*/
	}
	
	public HashMap<Character, String> getCharCodes(Tree tree) {
		String code = "";
		
		createTreeCode(tree, code, 1);
		
		return codestr;
	}
	
	private void createTreeCode(Tree tree, String code, int treeLevel) {
		if (tree instanceof Node)
		{
			Node node = (Node) tree;
			
			if (node.getLeftBranch() != null) {
				createTreeCode(node.getLeftBranch(), code + "0", treeLevel + 1);
			}
			
			if (node.getRightBranch() != null) {
				createTreeCode(node.getRightBranch(), code + "1", treeLevel + 1);
			}
		}
		if (tree instanceof Leaf)
		{
			if (treeLevel == 1) {
				code = "0";
			}
			
			Leaf leaf = (Leaf) tree;
			codestr.put(leaf.getCharacter(), code);
		}
	}
}
