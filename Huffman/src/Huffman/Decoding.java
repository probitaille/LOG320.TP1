package Huffman;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Tree.Leaf;
import Tree.Node;
import Tree.Tree;

public class Decoding {

	public void decrypt(String path)
	{
		//read file and separate header/payload
		ObjectInputStream ois;
		BufferedInputStream bis;
		FileInputStream fis;
		Tree tree = null;
		String text = "";
		Character code = null;
		
		try {
			fis = new FileInputStream(path);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			
			tree = (Tree)ois.readObject();
			
			try {
				while ((code = (char)ois.readByte()) != null) {
					text += code;
					
				}
			} catch (EOFException e) {
				System.out.println("End of file reached.");
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Node node = (Node) tree;
		System.out.println(node.getFrequency());
		
		System.out.println(text);
		
		String decodedText = replaceBytes(tree, text);
		
		System.out.println(decodedText);
		
		//write text to file
		writeDecodedTextToFile(path + ".txt", decodedText);
	}
	
	private String replaceBytes(Tree tree, String text)
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
		
		String decodedText = "";
		
		/*if (tree instanceof Node)
		{
			Node node = (Node) tree;
			if (text.charAt(0) == '0')
				decodedCharacter = replaceBytes(node.getLeftBranch(), text.substring(1));
			if (text.charAt(0) == '1')
				decodedCharacter = replaceBytes(node.getRightBranch(), text.substring(1));
		}
		if (tree instanceof Leaf)
		{
			Leaf leaf = (Leaf) tree;
			decodedCharacter = leaf.getCharacter();
		}*/
		
		Tree currentTree = tree;
		
		for (int i = 0; i != text.length(); ++i) {
			if (currentTree instanceof Node) {
				if (text.charAt(i) == '0')
					currentTree = ((Node)currentTree).getLeftBranch();
				else if (text.charAt(i) == '1')
					currentTree = ((Node)currentTree).getRightBranch();
			}
			
			if (currentTree instanceof Leaf) {
				decodedText += ((Leaf)currentTree).getCharacter();
				currentTree = tree;
			}
		}
		
		return decodedText;
	}
	
	public void writeDecodedTextToFile(String path, String text) {
		ObjectOutputStream fos;
		try {
			fos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			fos.write(text.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
