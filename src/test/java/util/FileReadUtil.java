package util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gychqyl@126.com
 * @version V1.0
 * @Description: TODO
 * @date 2019年8月8日 上午9:28:25
 */
public class FileReadUtil {

    public static List<String> readLines(String filePath, String enCode) {
        List<String> lines = new ArrayList<String>();
        File file = new File(filePath);
        if (file.exists()) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                String line = br.readLine();
                while (line != null) {
                    lines.add(line);
                    line = br.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return lines;
    }

    public static String replaceLine(String line) {
        if (line == null || "".equals(line)) {
            return line;
        }
        if (line.contains("\\")) {
            line = line.replaceAll("\\\\", "=~=~=~=~=~=");
        }
        line = line.replaceAll("\"", "\\\\\"");
        line = line.replaceAll("=~=~=~=~=~=", "\\\\\\\\");
        return line;
    }

    public static StringBuilder converFileToJava(String filePath) {
        List<String> lines = readLines(filePath, "UTF-8");
        StringBuilder builder = new StringBuilder();
        builder.append("StringBuilder builder = new StringBuilder();");
        for (String line : lines) {
            line = replaceLine(line);
            builder.append("builder.append(\"" + line + "\\r\\n" + "\");\n");
        }
        return builder;
    }

    public static void writeFile(String text, String file) {
        PrintWriter fw = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fw = new PrintWriter(fos);
            fw.println(text);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<String> readDir(String filePath, String enCode) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                lines.addAll(readDir(f.getPath(), enCode));
            }
        } else {
            lines.addAll(readLines(filePath, enCode));
        }
        return lines;
    }

    public static void syso(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                syso(f.getPath());
            }
        } else {
            if (!file.getName().equals("count.log")) {
                System.out.println(file.getPath());
                file.delete();
            }
        }
    }

}
