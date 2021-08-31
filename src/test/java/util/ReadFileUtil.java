package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ReadFileUtil {

	InputStreamReader reader = null;
	BufferedReader br = null;
	
	// 读取文件
	public ArrayList<String> readFromTextFile(File filename) throws IOException {

		return scan(filename);
	}

	// 递归调用读取文件
	public ArrayList<String> scan(File f) throws IOException {
		ArrayList<String> strArray = new ArrayList<String>();
		if (f != null) {
			if (f.isDirectory()) {
				File[] fileArray = f.listFiles();
				if (fileArray != null) {
					for (int i = 0; i < fileArray.length; i++) {
						// 递归调用
						strArray.addAll(scan(fileArray[i]));
					}
				}
			} else {
				try {
					reader = new InputStreamReader(new FileInputStream(f),"UTF-8");
					br = new BufferedReader(reader);
					String line = br.readLine();
					
					while (line != null) {
						strArray.add(line);
						line = br.readLine();
					}
				} catch (FileNotFoundException e) {
					throw new FileNotFoundException();
				}finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				return strArray;
			}
		}
		return strArray;
	}

}
