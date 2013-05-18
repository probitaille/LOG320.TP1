package file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	private static FileHelper instance;
	
	private FileHelper() {
	}
	
	public static FileHelper getInstance() {
		if (instance == null) {
			instance = new FileHelper();
		}
		
		return instance;
	}
	
	public String readTextFile(String path) {
		/*String text = "";
		File file = new File(path);
		
	    try {
	    	Scanner scanner =  new Scanner(file);
	    	while (scanner.hasNextLine()) {
	    		text += scanner.nextLine() + "\r\n";
	    	}      
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    
		return text;*/
		
		StringBuffer fileData = new StringBuffer();
        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));

	        char[] buf = new char[1024];
	        int numRead=0;
	        while((numRead=reader.read(buf)) != -1){
	            String readData = String.valueOf(buf, 0, numRead);
	            fileData.append(readData);
	        }
	        reader.close();
        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileData.toString();
	}
	
	public byte[] readByteFile(String path) {
		File file = new File(path);
		byte[] encodedFileContent = new byte[(int)file.length()];
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(encodedFileContent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encodedFileContent;
	}
	
	public void writeToTextFile(String path, String text) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			out.write(text);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeToByteFile(String path, byte[] header, byte[] text) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
			bos.write(header);
			bos.write(text);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
