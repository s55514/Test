package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SendPostandGet {

    public static String submitGet(String strUrl, String codetype, int timeout) throws Exception {
        URLConnection connection = null;
        BufferedReader reader = null;
        String str = null;
        try {
            URL url = new URL(strUrl);
            connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type", "application/json");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), codetype));

            StringBuffer linebuff = new StringBuffer("");
            String lines;
            while ((lines = reader.readLine()) != null) {
                linebuff.append(lines);
            }
            str = linebuff.toString();

        } catch (Exception var16) {
            throw var16;

        } finally {
            try {
                reader.close();

            } catch (Exception var15) {
                var15.printStackTrace();


                return str;
            }

        }
        return str;
    }

    public static String submitPost(String strUrl, String codetype, String cookie,int timeout) throws Exception {
        URLConnection connection = null;
        BufferedReader reader = null;
        String str = null;
        try {
            URL url = new URL(strUrl);
            connection = url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Cookie", cookie);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), codetype));

            StringBuffer linebuff = new StringBuffer("");
            String lines;
            while ((lines = reader.readLine()) != null) {
                linebuff.append(lines);
            }
            str = linebuff.toString();

        } catch (Exception var16) {
            throw var16;

        } finally {
            try {
                reader.close();

            } catch (Exception var15) {
                var15.printStackTrace();


                return str;
            }

        }
        return str;
    }
}
