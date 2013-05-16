package file;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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
		String text = "";
		Path pathObject = Paths.get(path);
		
	    try (Scanner scanner =  new Scanner(pathObject)) {
	    	while (scanner.hasNextLine()) {
	    		text += scanner.nextLine() + "\r\n";
	    	}      
	    } catch (IOException e) {
			e.printStackTrace();
		}
		
	    System.out.println(text);
	    
		return text;
	}
	
	public void writeToTextFile(String text) {
		
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
