package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

public class LogWriteUtil {
	// 打印日志
	public void  write(String fileNameHead, Object object) {
		write(fileNameHead,object,false);

	}

	// 打印日志
	public void write(String fileNameHead, Object object,boolean isConsole) {
		Calendar cd = Calendar.getInstance();// 日志文件时间
		int year = cd.get(Calendar.YEAR);
		String logFilePathName = null;
		String month = addZero(cd.get(Calendar.MONTH) + 1);
		String day = addZero(cd.get(Calendar.DAY_OF_MONTH));
		String hour = addZero(cd.get(Calendar.HOUR_OF_DAY));
		String min = addZero(cd.get(Calendar.MINUTE));
		String sec = addZero(cd.get(Calendar.SECOND));
		String mill = addZero(cd.get(Calendar.MILLISECOND));
		String path = "D:/userlog/" + year + month + "/" + day + "/" + hour;
		try {
			File fileParentDir = new File(path);// 判断log目录是否存在
			if (!fileParentDir.exists()) {
				System.out.println("创建目录：" + path);
				fileParentDir.mkdirs();
			}
			// 判断当前存在的log文件，如果大于200M，新建日志
			for (int i = 0; i < 10; i++) {
				if (fileNameHead == null || fileNameHead.equals("")) {
					logFilePathName = path + "/" + year + month + day + ".log";// 日志文件名
				} else if(fileNameHead.contains(".log")||fileNameHead.contains(".txt")) {
					logFilePathName = path + "/" + fileNameHead;// 日志文件名
				}else {
					logFilePathName = path + "/" + fileNameHead + i + ".log";// 日志文件名

				}
				File logFile = new File(logFilePathName);
				if (logFile.exists() && logFile.isFile()) {
					if (logFile.length() > 209715200) {
						continue;
					} else {
						break;
					}
				} else {
					logFile.createNewFile();
					break;
				}
			}
			PrintWriter printWriter = new PrintWriter(new FileOutputStream(logFilePathName, true));// 紧接文件尾写入日志字符串
			String time = "[" + year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec + "." + mill + "] ";
			printWriter.println(object);
			if (isConsole) {
				System.out.println(object);
			}
			printWriter.flush();
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String addZero(int i) {
		if (i < 10) {
			String tmpString = "0" + i;
			return tmpString;
		} else {
			return String.valueOf(i);
		}
	}

	// 获取异常详细信息
	public String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
}
