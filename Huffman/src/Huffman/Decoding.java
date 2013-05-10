package Huffman;

import Tree.Leaf;
import Tree.Node;
import Tree.Tree;

public class Decoding {

	public void decrypt(String path)
	{
		//read file and separate header/payload
		replaceBytes(null, "");
		//write text to file
	}
	
	private void replaceBytes(Tree tree, String code)
	{
		//TODO il faut parcourir l'arbre pour trouver si le chemin que nous faison
		// correspond a un leaf ou non, si oui on remplace par la lettre sinon on
		// continue d'iterer dans la liste de byte[]
		// le header du fichier contient l'arbre sous forme de Tree et le payload le contenu sous forme de byte[]
		// les deux sections sont separes par ;
		
		//           A
		//         0/ \1
		//         B   C
		//       0/ \1  1
		//       D   E
		//      00   01
		
		// 0010101
		// 0  -> node
		// 00 -> leaf -> D
		// 1  -> leaf -> C
		// 0  -> node
		// 01 -> leaf -> E
		// 0  -> node
		// 01 -> leaf -> E
		// 0010101 -> DCEE
		
		/*if (tree instanceof Node)
		{
			Node node = (Node) tree;
			if (code.charAt(code.length() - 1) == '0')
				replaceBytes(node.getLeftBranch(), code + "0");
			if (code.charAt(code.length() - 1) == '1')
				replaceBytes(node.getRightBranch(), code + "1");
		}
		if (tree instanceof Leaf)
		{
			Leaf leaf = (Leaf) tree;
		}*/
	}
}
