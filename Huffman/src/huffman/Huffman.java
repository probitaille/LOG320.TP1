package huffman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import huffman.Node;
import huffman.MapValueComparator;

public class Huffman
{
	public void encrypt(String path)
	{
		TreeSet<Node> tree;
		HashMap<Byte, Integer> freqDic = new HashMap<Byte, Integer>();
		freqDic = readFile(path);
		MapValueComparator mvc = new MapValueComparator(freqDic);
		TreeMap<Byte, Integer> sortedFreqDic = new TreeMap<Byte, Integer>(mvc);
		sortedFreqDic.putAll(freqDic);
		tree = buildTree(sortedFreqDic);
		tree.first().printPath("");
	}
	
	public void decrypt(String path)
	{
		
	}

    private TreeSet<Node> buildTree(TreeMap<Byte, Integer> sortedFreqDic)
    {
        TreeSet<Node> tree = new TreeSet<Node>();
        for(Map.Entry<Byte, Integer> entry : sortedFreqDic.entrySet())
        	  tree.add(new huffman.Node(entry.getKey(), entry.getValue(), null, null));

        while (tree.size() > 1)
        {
            Node childNode1 = (Node) tree.first();
            tree.remove(childNode1);
            Node childNode2 = (Node) tree.first();
            tree.remove(childNode2);

            Node parent = new Node(childNode1, childNode2);
            tree.add(parent);
        }
        
        return tree;
    }
    
    //reading file based on
    //http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
	private static HashMap<Byte, Integer> readFile(String path)
	{
		HashMap<Byte, Integer> freqDic = new HashMap<Byte, Integer>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileChannel ch = fis.getChannel( );
		ByteBuffer bb = null;
		try {
			bb = ByteBuffer.allocateDirect((int)ch.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nRead;
		try {
			while ((nRead=ch.read(bb)) != -1)
			{
				bb.position(0);
				bb.limit(nRead);
				while (bb.hasRemaining())
				{
					byte temp = bb.get();
					if (freqDic.containsKey(temp))
						freqDic.put(temp, freqDic.get(temp) + 1);
					else
						freqDic.put(temp, 1);
				}
				bb.clear();
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freqDic;
	}
	
	private static void writeFile(String path, byte[] data)
	{
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(path);
			fos.write(data, 0, data.length);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
