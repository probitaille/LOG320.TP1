package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class FileReaderWriter {
    //reading file based on
    //http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
	public static HashMap<Byte, Integer> readFile(String path)
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
	
	public static void writeFile(String path, byte[] data)
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
