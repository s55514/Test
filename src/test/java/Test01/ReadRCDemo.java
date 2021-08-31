package Test01;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.LogWriteUtil;
import util.ReadFileUtil;
import util.SendPostandGet;
import util.jsonUtil;

public class ReadRCDemo {
    LogWriteUtil logWriteUtil = new LogWriteUtil();
    jsonUtil jsonUtil = new jsonUtil();
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // 读取操作记录
    //空铁：https://www.51kongtie.net/train!ajaxgettrainorderrc.action?trainorderid=343815175
    //双鱼：http://tcmanager.hangtian123.com//train!ajaxgettrainorderrc.action?trainorderid=286205644
    @org.junit.Test
    public void readFileTest() {

        String fileName = "";
        String Recordurl = "";
        //cookie使用对应后台的cookie，后台每次重新登录需要更改
        String cookie = "";
        //  1  空铁    2  双鱼
        String type = "1";

        if ("1".equals(type)) {

            fileName = "C:\\Users\\Administrator\\Desktop\\操作记录\\dd.txt";
            Recordurl = "https://www.51kongtie.net/train!ajaxgettrainorderrc.action";
            cookie = "JSESSIONID=8FF4847ED629D5EEFC64F21D8AD6EAD9";
        } else if ("2".equals(type)) {

            fileName = "F:\\双鱼\\订单\\订单id.txt";
            Recordurl = "http://tcmanager.hangtian123.com//train!ajaxgettrainorderrc.action";
            cookie = "JSESSIONID=3A6D7AA03C5A0C1E17E55C1A1137EE87";
        }

        File f = new File(fileName);
        ReadFileUtil readFileUtil = new ReadFileUtil();
        try {
            ArrayList<String> strArray = readFileUtil.readFromTextFile(f);

            for (int i = 0; i < strArray.size(); i++) {
                String orderid = strArray.get(i);
                String createtime = "";
                String creatuser = "";
                String content = "";

                sb.append("&trainorderid=").append(orderid);

                String result = SendPostandGet.submitPost(Recordurl + "?" + sb.toString(), "utf-8", cookie, 60000);
                sb.delete(0, sb.length());

                System.out.println(result);
                JSONArray resultArray = JSONObject.parseArray(result);

                for (int j = 0; j < resultArray.size(); j++) {
                    createtime = resultArray.getJSONObject(j).getString("CREATETIME");
                    creatuser = resultArray.getJSONObject(j).getString("CREATEUSER");
                    content = resultArray.getJSONObject(j).getString("CONTENT");

                    System.out.println(sb.append(orderid).append("\t").append(createtime).append("\t").append(creatuser).append("\t").append(content));
                    sb.delete(0, sb.length());
                }
                
   
            }


        } catch (Exception e) {
            System.out.println("***************");
            logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
        }


    }
    
    
    
    
    //统计数据查订单用
    @org.junit.Test
    public void readFileTest1() {

        String fileName = "";
        String recordurl = "";
        //cookie使用对应后台的cookie，后台每次重新登录需要更改
        String cookie = "";
        //  1  空铁    2  双鱼
        String type = "2";

        if ("1".equals(type)) {

            fileName = "F:\\空铁\\订单\\订单id.txt";
            recordurl = "https://www.51kongtie.net/train!ajaxgettrainorderrc.action";
            cookie = "cod=10854; JSESSIONID=FAF3070E33B9F595B21851A7E1A06E7C";
        } else if ("2".equals(type)) {

            fileName = "F:\\双鱼\\订单\\订单id.txt";
            recordurl = "http://tcmanager.hangtian123.com//train!ajaxgettrainorderrc.action";
            cookie = "JSESSIONID=E6D4077E970DE1155016EA355B5E2427";
        }

        File f = new File(fileName);
        ReadFileUtil readFileUtil = new ReadFileUtil();
        try {
            ArrayList<String> strArray = readFileUtil.readFromTextFile(f);

            for (int i = 0; i < strArray.size(); i++) {
                String orderid = strArray.get(i);
                String createtime = "";
                String creatuser = "";
                String content = "";
                String status = "";

                sb.append("&trainorderid=").append(orderid);

                String result = SendPostandGet.submitPost(recordurl + "?" + sb.toString(), "utf-8", cookie, 60000);
                sb.delete(0, sb.length());

                System.out.println(result);
                JSONArray resultArray = JSONObject.parseArray(result);

                for (int j = 0; j < resultArray.size(); j++) {
                    creatuser = resultArray.getJSONObject(j).getString("CREATEUSER");
                    content = resultArray.getJSONObject(j).getString("CONTENT");
                    status = resultArray.getJSONObject(j).getString("STATUS");

                    if (("12306".equals(creatuser) || "重复订单判断".equals(creatuser) || "下单消费者".equals(creatuser)) && "2".equals(status)) {
                        if ("下单消费者".equals(creatuser)) {
                            content = content.substring(content.indexOf("拒单【") + 3, content.indexOf("】"));
                        }
                        sb.append(orderid).append("\t").append(content);
                        System.out.println(sb.toString());

//                        logWriteUtil.write("订单失败原因", sb.toString());
                        sb.delete(0, sb.length());
                    }

                }

            }


        } catch (Exception e) {
            System.out.println("***************");
            logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
        }


    }
    
    
    
    
    

}
