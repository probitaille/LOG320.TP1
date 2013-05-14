package Utilities;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class FileReaderWriter {
    //reading file based on
    //http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly
	public static Object[] readFile(String path)
	{
		HashMap<Byte, Integer> freqDic = new HashMap<Byte, Integer>();
		FileInputStream fis = null;
		String str = "";
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		FileChannel ch = fis.getChannel( );
		ByteBuffer bb = null;
		try {
			bb = ByteBuffer.allocateDirect((int)ch.size());
		} catch (IOException e) {
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
					str += String.valueOf((char) temp);
					if (freqDic.containsKey(temp))
						freqDic.put(temp, freqDic.get(temp) + 1);
					else
						freqDic.put(temp, 1);
				}
				bb.clear();
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object[] o = new Object[2];
		o[0] = freqDic;
		o[1] = str;
		return o;
	}
	
	public static void writeFileBin(String path, byte[] data, Object header)
	{
		ObjectOutputStream fos;
		try {
			fos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
			fos.writeObject(header);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
