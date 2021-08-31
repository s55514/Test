package Test01;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import util.DepositDesUtil;
import util.DesUtil;
import util.LogWriteUtil;
import util.ReadFileUtil;
import util.jsonUtil;

public class ReadTest {

	DesUtil desUtil = new DesUtil();
	LogWriteUtil logWriteUtil = new LogWriteUtil();
	jsonUtil jsonUtil = new jsonUtil();
	SimpleDateFormat createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 读取日志（双鱼登录）耗时
	@Test
	public void readFileTest1() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\双鱼登录失败\\26";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains(":jsonStr:")) {
					int index = strArray.indexOf(str);
					String[] ss = str.split(":jsonStr:");

					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						String suiji = ss[0].substring(ss[0].indexOf("] ") + 2);

						String data = jsons.getString("data");
						String rep = str.substring(str.indexOf("[") + 1, str.indexOf("]"));

						Date repTime = createTime.parse(rep);
						Date resTime = null;
						String userName = "";
						String passWord = "";
						String type = "未知";
						String msg = "";
						String code = "";
						long sec = 0;
						if (jsonUtil.isJson(data)) {
							JSONObject json = JSONObject.parseObject(data);
							userName = json.getString("username");
							passWord = json.getString("userpwd");

							// 是否走短信验证登录
							boolean isMessageLogin = !("".equals(json.getString("captcha"))
									|| json.getString("captcha") == null)
									&& !("".equals(json.getString("mobile")) || json.getString("mobile") == null);

							// 0 || 3 登录 1 验证登录状态 2 人脸信息验证
							if ("0".equals(json.getString("type")) || "3".equals(json.getString("type"))) {
								if (isMessageLogin) {
									type = "短信验证登录";
								} else {
									type = "账号正常登录";
								}

							} else if ("1".equals(json.getString("type"))) {
								type = "验证登录状态";
							} else if ("2".equals(json.getString("type"))) {
								type = "人脸信息验证";
							}
							try {
								userName = DepositDesUtil.usernameDecrypt(userName);
								passWord = DepositDesUtil.userpwdDecrypt(passWord, userName);
							} catch (Exception e) {
								userName = "";
								passWord = "";
							}
						}

						for (int j = index; j < strArray.size(); j++) {
							String strj = strArray.get(j);
							if (strj.contains(":result:")
									&& strj.substring(strj.indexOf("] ") + 2, strj.indexOf(":result:")).equals(suiji)) {

								resTime = createTime.parse(strj.substring(strj.indexOf("[") + 1, strj.indexOf("]")));

								String[] result = strj.split(":result:");

								if (jsonUtil.isJson(result[1])) {
									JSONObject results = JSONObject.parseObject(result[1]);
									msg = results.getString("msg");
									code = results.getString("code");
								}

								break;
							}
						}
						if (repTime != null && resTime != null) {
							sec = resTime.getTime() - repTime.getTime();

						}
//                        System.out.println(createTime.format(repTime) + "\t" + userName + "\t" + passWord + "\t" + msg + "\t" + code + "\t" + sec);
						logWriteUtil.write("双鱼登录07-", rep + "\t" + suiji + "\t" + type + "\t" + userName + "\t"
								+ passWord + "\t" + msg + "\t" + code + "\t" + sec);

					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 读取日志（空铁人脸核验解封）
	@Test
	public void readFileTest2() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\人脸";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains(":jsonStr:")) {
					int index = strArray.indexOf(str);
					String[] ss = str.split(":jsonStr:");

					String rep = str.substring(str.indexOf("[") + 1, str.indexOf("]"));

					Date repTime = createTime.parse(rep);
					Date resTime = null;
					String userName = "";
					String passWord = "";
					String partnerid = "";
					String msg = "";
					String code = "";
					long sec = 0;
					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						String suiji = ss[0].substring(ss[0].indexOf("] ") + 2);
						partnerid = jsons.getString("partnerid");

						userName = jsons.getString("userName");
						passWord = jsons.getString("passWord");
						passWord = DesUtil.decrypt(passWord, "u0FKpzhZa1x1DeWn");

						for (int j = index; j < strArray.size(); j++) {
							String strj = strArray.get(j);
							if (strj.contains(":result:")
									&& strj.substring(strj.indexOf("] ") + 2, strj.indexOf(":result:")).equals(suiji)) {

								resTime = createTime.parse(strj.substring(strj.indexOf("[") + 1, strj.indexOf("]")));

								String[] result = strj.split(":result:");

								if (jsonUtil.isJson(result[1])) {
									JSONObject results = JSONObject.parseObject(result[1]);
									msg = results.getString("msg");
									code = results.getString("code");
								}

								break;
							}
						}
						if (repTime != null && resTime != null) {
							sec = resTime.getTime() - repTime.getTime();

						}
//                        System.out.println(createTime.format(repTime) + "\t" + userName + "\t" + passWord + "\t" + msg + "\t" + code + "\t" + sec);
						logWriteUtil.write("空铁人脸解封07-", rep + "\t" + suiji + "\t" + partnerid + "\t" + userName + "\t"
								+ passWord + "\t" + msg + "\t" + code + "\t" + sec);

					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 国际卡
	@Test
	public void readFileTest3() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\新建文件夹 (2)\\09";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains("卡号输入错误卡号") && str.contains(":返回信息：")) {

					System.out.println(str.toString());
//						logWriteUtil.write("空铁人脸解封0700-",str);

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// 读取日志同程 退票改签占座
	@Test
	public void readFileTest4() {
		LogWriteUtil logWriteUtil = new LogWriteUtil();
		String fileName = "C:\\Users\\Administrator\\Desktop\\改签\\18";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);

			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains("parm:data=")
						&& (str.contains("tongcheng_train_hs") || str.contains("tongcheng_train_new"))) {
					String[] ss = str.split("parm:data=");

					String[] sss = ss[1].split(":timeStamp:");
					JSONObject jsons = JSONObject.parseObject(sss[0]);
					if (jsons.containsKey("returntickets")) {
						JSONObject jsonss = null;
						if (jsons.getJSONArray("returntickets").size() > 0) {
							jsonss = (JSONObject) jsons.getJSONArray("returntickets").get(0);
						}
						if (jsonss != null) {
							if ((jsonss.getString("returnfailid").equals("")
									|| jsonss.getString("returnfailid") == null)
									&& jsons.getString("returntype").equals("1")) {
								logWriteUtil.write("tc退票成功25-", ss[0].substring(1, 20) + "\t"
										+ jsons.getString("apiorderid") + "\t" + jsonss.getString("returnfailmsg"));
								success++;
							} else if (jsons.getString("returntype").equals("1")) {
								logWriteUtil.write("tc退票失败25-",
										ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t"
												+ URLDecoder.decode(jsonss.getString("returnfailmsg"), "UTF-8") + "\t"
												+ jsonss.getString("returnfailid"));

							} else {
								logWriteUtil.write("tc退票其他25-",
										ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t"
												+ URLDecoder.decode(jsonss.getString("returnfailmsg"), "UTF-8"));
							}
						} else if (jsons.getString("returntype").equals("1")) {
							logWriteUtil.write("tc退票失败25-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t"
											+ URLDecoder.decode(jsonss.getString("returnfailmsg"), "UTF-8") + "\t"
											+ jsonss.getString("returnfailid"));

						} else {
							logWriteUtil.write("tc退票其他25-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid"));
						}
					}

				} else if (str.contains("backjson=")) {
					String[] ss = str.split("backjson=");

					JSONObject json = JSONObject.parseObject(ss[1]); // &&(json.toString().contains("43003") ||
																		// json.toString().contains("12306响应异常 "))
					if (!json.getString("code").equals("100") && !json.getString("msg").equals("改签占座成功")) {
						logWriteUtil.write("tc改签失败25-", json.getString("orderid") + "\t" + json.getString("msg") + "\t"
								+ json.getString("code"));

					}

				} else if (str.contains("?data=")) {
					String[] ss = str.split("data=");

					JSONObject json = JSONObject.parseObject(URLDecoder.decode(ss[1], "UTF-8"));
//                    logWriteUtil.write("tc占座失败25-",  URLDecoder.decode(ss[1],"UTF-8"));

					if (!json.getString("code").equals("100") && !json.getString("msg").equals("处理或操作成功")
							&& (json.toString().contains("43003") || json.toString().contains("12306响应异常 "))) {
						logWriteUtil.write("tc占座失败封禁25-", json.getString("orderid") + "\t" + json.getString("msg")
								+ "\t" + json.getString("code"));

					}

//                    else if(!json.getString("code").equals("100") && !json.getString("msg").equals("处理或操作成功") ){
//                        logWriteUtil.write("tc占座失败25-",  json.getString("orderid")+ "\t" + json.getString("msg")+ "\t" + json.getString("code"));
//
//                    }

				}

			}

			System.out.println("退票请求：" + num);
			System.out.println("退票成功：" + success);
			System.out.println("===============***************=================");

		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	// 读取日志拼夕夕退票
	@Test
	public void readFileTest5() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\ddd\\拼多多";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();

		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("result:") && str.contains("退票请求已接收")) {
					String[] ss = str.split("result:");

					JSONObject jsons = JSONObject.parseObject(ss[1]);
					logWriteUtil.write("pdd退票请求-", ss[0].substring(1, 20) + "\t" + jsons.getString("orderid"));
					num++;

				} else if (str.contains("pdd.train.callback.refund.confirm第1次--->request:")
						&& str.contains("pdd.train.callback.refund.confirm")) {
					String[] ss = str.split("request:");

					JSONObject jsons = JSONObject.parseObject(ss[1]);

					if (jsons.getString("msg").equals("请求成功") && jsons.getInteger("code") == 0
							&& jsons.getInteger("refund_type") == 1) {
						logWriteUtil.write("pdd退票成功-", ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id")
								+ "\t" + jsons.getString("msg"));
						success++;
					} else if (jsons.getInteger("refund_type") == 1) {
						if (jsons.getString("msg").contains("请先人脸识别后再来登录") || jsons.getString("msg").contains("人脸核验")) {
							renlian++;
						}
						logWriteUtil.write("pdd退票失败-", ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id")
								+ "\t" + jsons.getString("msg"));
					} else {
						logWriteUtil.write("pdd退票其他-", ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id")
								+ "\t" + jsons.getString("msg"));

					}

				}
			}

			System.out.println("拼多多退票请求：" + num);
			System.out.println("拼多多退票成功：" + success);
			System.out.println("拼多多人脸核验：" + renlian);
			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	// 读取日志同程退票
	@Test
	public void readFileTest6() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\退票成功率专用\\双鱼\\16";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				String returnfailmsg = "";
				if (str.contains("parm:data=")) {
					String[] ss = str.split("parm:data=");

					if (ss.length >= 2 && !ss[1].equals("")) {

						String[] sss = ss[1].split(":timeStamp:");
						JSONObject jsons = JSONObject.parseObject(sss[0]);
						String returnmsg = "";
						if (jsons.containsKey("returnmsg") && jsons.getString("returnmsg") != null) {
							returnmsg = URLDecoder.decode(jsons.getString("returnmsg"), "UTF-8");

						}
						if (jsons.getBooleanValue("returnstate") == true && jsons.getInteger("returntype") == 1) {
							logWriteUtil.write("sy退票成功23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
							success++;
						} else if (jsons.getInteger("returntype") == 1) {
							if (returnmsg.contains("人脸核验") || returnmsg.contains("人脸识别")) {
								renlian++;
							}
							logWriteUtil.write("kt退票失败23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
						} else {
							logWriteUtil.write("sy退票其他23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);

						}

					}

				} else if (str.contains("退票请求已接收") && str.contains("result:")) {
					String[] ss = str.split("result:");

					JSONObject json = JSONObject.parseObject(ss[1]);
					num++;
					logWriteUtil.write("tc退票请求-", ss[0].substring(1, 20) + "\t" + json.getString("orderid"));

				}

			}

			System.out.println("同程退票请求：" + num);
			System.out.println("同程退票成功：" + success);
			System.out.println("同程人脸核验：" + renlian);
			System.out.println("===============***************=================");

		} catch (

		Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	@Test
	public void readFileTestXC() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\退票成功率专用\\携程\\111";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				String returnfailmsg = "";

				if (str.contains("parm:data=") && str.contains("xiecheng_zs")) {
					String[] ss = str.split("parm:data=");

					if (ss.length >= 2 && !ss[1].equals("")) {

						String[] sss = ss[1].split(":timeStamp:");
						JSONObject jsons = JSONObject.parseObject(sss[0]);
						String returnmsg = "";
						if (jsons.containsKey("returnmsg") && jsons.getString("returnmsg") != null) {
							returnmsg = URLDecoder.decode(jsons.getString("returnmsg"), "UTF-8");

						}
						if (jsons.getBooleanValue("returnstate") == true && jsons.getInteger("returntype") == 1) {
							logWriteUtil.write("sy退票成功23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
							success++;
						} else if (jsons.getInteger("returntype") == 1) {
							if (returnmsg.contains("人脸核验") || returnmsg.contains("人脸识别")) {
								renlian++;
							}
							logWriteUtil.write("kt退票失败23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
						} else {
							logWriteUtil.write("sy退票其他23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);

						}

					}

				} else if (str.contains("\"退票请求已接收\"") && str.contains("result:")) {
					String[] ss = str.split("result:");
					if (fileName.endsWith("请求")) {

						logWriteUtil.write("退票请求-", str);
					} else if (fileName.endsWith("111")) {
						JSONObject json = JSONObject.parseObject(ss[1]);

						logWriteUtil.write("xc退票请求-", ss[0].substring(1, 20) + "\t" + json.getString("orderid"));

					}

				}

				/*
				 * else if (str.contains("\"xiecheng_zs"\") && str.contains("jsonStr:")) {
				 * String[] ss = str.split("result:"); if (fileName.endsWith("请求")) {
				 * 
				 * logWriteUtil.write("退票请求-", str); } else if (fileName.endsWith("111")) {
				 * JSONObject json = JSONObject.parseObject(ss[1]);
				 * 
				 * logWriteUtil.write("xc退票请求-", ss[0].substring(1, 20) + "\t" +
				 * json.getString("orderid"));
				 * 
				 * }
				 * 
				 * }
				 */

			}

			System.out.println("携程退票成功：" + success);
			System.out.println("携程人脸核验：" + renlian);
			System.out.println("===============***************=================");

		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	// 支付同步接口问题
	@Test
	public void readFileTest7() {
		String fileName = "C:\\Users\\Administrator\\Desktop\\顶顶顶顶";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();

		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////

	// 读取日志（双鱼取消耗时统计） sz /data/*/bin/D:/userlog/202105/07/*/t同程火车票接口_4.7取消火车票订单.log
	@Test
	public void readFileTest08() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\取消耗时";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains(":jsonStr:")) {
					int index = strArray.indexOf(str);
					String[] ss = str.split(":jsonStr:");

					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						String suiji = ss[0].substring(ss[0].indexOf("] ") + 2);

						String rep = str.substring(str.indexOf("[") + 1, str.indexOf("] "));
						Date repTime = createTime.parse(rep);
						Date resTime = null;
						String orderid = jsons.getString("orderid");
						String msg = "";
						String code = "";

						long sec = 0;

						for (int j = index; j < strArray.size(); j++) {
							String strj = strArray.get(j);
							if (strj.contains(":result:")
									&& strj.substring(strj.indexOf("] ") + 2, strj.indexOf(":result:")).equals(suiji)) {

								resTime = createTime.parse(strj.substring(strj.indexOf("[") + 1, strj.indexOf("] ")));

								String[] result = strj.split(":result:");

								if (jsonUtil.isJson(result[1])) {
									JSONObject results = JSONObject.parseObject(result[1]);
									msg = results.getString("msg");
									code = results.getString("code");

								}

								break;
							}
						}
						if (repTime != null && resTime != null) {
							sec = resTime.getTime() - repTime.getTime();

						}
//	                        System.out.println(createTime.format(repTime) + "\t" + userName + "\t" + passWord + "\t" + msg + "\t" + code + "\t" + sec);
						logWriteUtil.write("双鱼占座取消28-",
								rep + "\t" + suiji + "\t" + orderid + "\t" + msg + "\t" + code + "\t" + sec);

					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}
	}
	
	
	
	//// 读取日志（双鱼改签占座同步响应耗时统计） sz /data/*/bin/D:/userlog/202107/17/*/t同程火车票接口_4.12请求改签.log
	@Test
	public void readFileTestTCGQ() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\改签接口\\18";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains(":jsonStr:")) {
					int index = strArray.indexOf(str);
					String[] ss = str.split(":jsonStr:");

					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						String suiji = ss[0].substring(ss[0].indexOf("] ") + 2);

						String rep = str.substring(str.indexOf("[") + 1, str.indexOf("] "));
						Date repTime = createTime.parse(rep);
						Date resTime = null;
						String orderid = jsons.getString("orderid");
						String msg = "";
						String code = "";

						long sec = 0;

						for (int j = index; j < strArray.size(); j++) {
							String strj = strArray.get(j);
							if (strj.contains(":result:")
									&& strj.substring(strj.indexOf("] ") + 2, strj.indexOf(":result:")).equals(suiji)) {

								resTime = createTime.parse(strj.substring(strj.indexOf("[") + 1, strj.indexOf("] ")));

								String[] result = strj.split(":result:");

								if (jsonUtil.isJson(result[1])) {
									JSONObject results = JSONObject.parseObject(result[1]);
									msg = results.getString("msg");
									code = results.getString("code");

								}

								break;
							}
						}
						if (repTime != null && resTime != null) {
							sec = resTime.getTime() - repTime.getTime();

						}
//	                        System.out.println(createTime.format(repTime) + "\t" + userName + "\t" + passWord + "\t" + msg + "\t" + code + "\t" + sec);
						logWriteUtil.write("双鱼请求改签同步耗时28-",
								rep + "\t" + suiji + "\t" + orderid + "\t" + msg + "\t" + code + "\t" + sec);

					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}
	}
	
	
	
	

	////////////////////////////////////////////////////////////////////////

	// 高铁占座失败原因及code 拉的日志名为GaoTieCallBack_GaoTieCallBack.log
	@Test
	public void readFileTest09() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\高铁\\占座\\09";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains("seatdata=")) {
					// [2021-01-10 11:59:54.512]
					// http://jt.rsscc.com/trainspr/dom/afterSupplierGetSeat.action? seatdata=
					// {"supplierOrderId":"T2101107744D9D00095804DD7091D00FEE98D876B60","order12306":"E530273775","reqtime":"20210110115954","use_supplier_account":"0","arrive_date":"2021-01-10","sign":"ef2ff9602d70e06392041c6174498b87","ticket_entrance":"","pay_limit_time":"2021-01-10
					// 12:29:54.33","gtgjOrderId":"D60110115903578147","refund_online":0,"ticket_price_all":39.0,"paperlessCheckin":"1","supplier":"hthy","ticketInfo":[{"seat_type":"O","seat_type_name":"%E4%BA%8C%E7%AD%89%E5%BA%A7","coach_name":"04","seat_name":"09F%E5%8F%B7","ticket_price":"39.00","sub_order_12306":"E530273775104009F","ticket_id":"D601101159035781470"}],"reqtoken":"890999050605398041","status":"SUCCESS"}
					// --->0--->SUCCESS
					String[] sss = str.split("seatdata=");
					String ss = sss[1];
					if (ss.contains("--->0")) {
						String[] ssss = ss.split("--->0");
						JSONObject jsons = JSONObject.parseObject(ssss[0]);
						if (!jsons.getString("status").equals("SUCCESS")) {
							logWriteUtil.write("高铁占座失败_",
									sss[0].substring(1, 20) + "	" + jsons.getString("gtgjOrderId") + "	"
											+ jsons.getString("fail_code") + "	"
											+ URLDecoder.decode(jsons.getString("fail_msg"))); // URLDecoder.decode
						}

					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}
	}

	// 高铁改签失败原因及code 待完善
	@Test
	public void readFileTest10() {
		String path = "";
		File f = new File(path);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("backjson=")) {
					String[] ss = str.split("backjson=");
					// ss[1];
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////

	// 拼多多改签失败原因及code
	@Test
	public void readFileTest11() {
		String path = "C:\\Users\\Administrator\\Desktop\\品多多改签\\10";
		File f = new File(path);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("pdd.train.callback.change.reserve第1次--->request:")) {
					String[] ss = str.split("request:");
					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						if (jsons.getInteger("code") != 0 && !("请求成功").equals(jsons.getString("msg"))) {
							logWriteUtil.write("pdd改签占座失败_",
									ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id") + "\t"
											+ jsons.getString("msg") + "\t" + jsons.getInteger("code"));
						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 拼多多占座(含成功和失败所有订单)原因及code 拼多多_CallBack.log
	// 101.37.17.54 sz
	// /data/tomcat-7.0.59-KongTieCallbackServlet_19232/bin/D:/userlog/202105/17/*/拼多多_CallBack.log
	// 101.37.33.4 sz
	// /data/6.0.5-tomcat-7.0.59_callbackServlet_19222/bin/D:/userlog/202105/17/*/拼多多_CallBack.log
	@Test
	public void readFileTest12() {
		String path = "C:\\Users\\Administrator\\Desktop\\拼\\17";
		File f = new File(path);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("pdd.train.callback.reserve第1次--->request:")) {
					String[] ss = str.split("request:");
					if (jsonUtil.isJson(ss[1])) {
						JSONObject jsons = JSONObject.parseObject(ss[1]);
						// if(jsons.getString("msg").contains("未支付的车票")) {
						logWriteUtil.write("pdd占座_", ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id")
								+ "\t" + jsons.getString("msg") + "\t" + jsons.getString("code"));
						// }
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 拼多多占座请求 （接口）
	@Test
	public void readFileTest13() {
		int count = 0;
		int count1 = 0;
		String path = "C:\\Users\\Administrator\\Desktop\\拼接口\\15";
		File f = new File(path);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("pdd.train.create.reserve")) {

					// count++;

					String suiji = str.substring(str.indexOf("]") + 2, str.indexOf("-->channel"));
					// System.out.println(suiji);
					for (int j = i + 1; j < strArray.size(); j++) {
						String strj = strArray.get(j);
						if (strj.contains(suiji + ":响应-->")) {

							// count1++;

							String[] ss = strj.split("响应-->");
							if (jsonUtil.isJson(ss[1])) {
								JSONObject jsons = JSONObject.parseObject(ss[1]);
								// if(!("请求成功").equals(jsons.getString("error_msg"))) {
								logWriteUtil.write("pdd请求_",
										ss[0].substring(1, 20) + "\t" + jsons.getString("pdd_order_id") + "\t"
												+ jsons.getString("error_msg") + "\t" + jsons.getString("error_code"));
								break;
								// }
							}
						}
					}

				}
			}

			// System.out.println(count);
			// System.out.println(count1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 天猪退票请求数（根据日志统计 未去重） 39.98.185.190 聚石塔 "returnticket-空铁无忧网票务" 退票
	// "msg_type":"3" 线上退
	// sz
	// /data/taobao/1.0.2_30001_taobao_jianting_order_tomcat7.0-new-new/D:/userlog/202105/18/*/淘宝TMC链接词.log
	@Test
	public void readFileTest14() {
		int count = 0;
		String path = "C:\\Users\\Administrator\\Desktop\\退票成功率专用\\天猪";
		File f = new File(path);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				if (str.contains("\"returnticket-空铁无忧网票务\",\"msg_type\":\"3\"")) {
					count++;
					String[] ss = str.split("淘宝TMC获取记录成功:");
					JSONObject jss = JSON.parseObject(ss[1]);
					logWriteUtil.write("淘宝退票请求_", jss.getString("main_biz_order_id"));
				}
			}
			System.out.println(count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////////
	// 空铁退票失败原因及code 待修改为 上边同程已修改
	@Test
	public void readFileTest15() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\空铁退票\\20";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);
				String returnfailmsg = "";
				if (str.contains("backurl:") && str.contains("parm:data=")) {
					String[] ss = str.split("parm:data=");

					String[] sss = ss[1].split(":timeStamp:");
					JSONObject jsons = JSONObject.parseObject(sss[0]);
					if (jsons.containsKey("returntickets")) {
						JSONObject jsonss = null;
						if (jsons.getJSONArray("returntickets").size() > 0) {
							jsonss = (JSONObject) jsons.getJSONArray("returntickets").get(0);
						}
						if (jsonss != null) {
							if (jsonss.getString("returnfailmsg") != null) {
								returnfailmsg = URLDecoder.decode(jsonss.getString("returnfailmsg"), "UTF-8");
							}
							if ((jsonss.getString("returnfailid").equals("")
									|| jsonss.getString("returnfailid") == null)
									&& jsons.getString("returntype").equals("1")) {
								logWriteUtil.write("tc退票成功-", ss[0].substring(1, 20) + "\t"
										+ jsons.getString("apiorderid") + "\t" + returnfailmsg);
								success++;
							} else if (jsons.getString("returntype").equals("1")) {
								if (returnfailmsg.contains("人脸核验")) {
									renlian++;
								}
								logWriteUtil.write("tc退票失败-",
										ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t"
												+ jsonss.getString("returnfailid") + "\t" + returnfailmsg);

							} else {
								logWriteUtil.write("tc退票其他-", ss[0].substring(1, 20) + "\t"
										+ jsons.getString("apiorderid") + "\t" + returnfailmsg);
							}
						} else if (jsons.getString("returntype").equals("1")) {
							logWriteUtil.write("tc退票失败-", ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid")
									+ "\t" + jsonss.getString("returnfailid") + "\t" + returnfailmsg);

						} else {
							logWriteUtil.write("tc退票其他-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid"));
						}

					}

				} else if (str.contains("退票请求已接收") && str.contains("result:")) {
					String[] ss = str.split("result:");

					JSONObject json = JSONObject.parseObject(ss[1]);
					num++;
					logWriteUtil.write("tc退票请求-", ss[0].substring(1, 20) + "\t" + json.getString("orderid"));

				}

			}

			System.out.println("同程退票请求：" + num);
			System.out.println("同程退票成功：" + success);
			System.out.println("同程人脸核验：" + renlian);
			System.out.println("===============***************=================");

		} catch (

		Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 读取日志退票前置核验接口

	// 读取日志退票前置核验接口
	@org.junit.Test
	public void readFileTest16() {

		ReadFileUtil readFileUtil = new ReadFileUtil();
		File f = new File("F:\\空铁\\退票前置接口\\t同程火车票接口_退票前置核验接口\\210525");

		File f2 = new File("F:\\空铁\\退票前置接口\\退票前置核验接口\\210525");
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);

			ArrayList<String> strArray2 = readFileUtil.readFromTextFile(f2);
			int redisIndex = 0;
			for (int i = 0; i < strArray.size(); i++) {
				String stri = strArray.get(i);
				if (stri.contains("-->json:")) {

					String[] jsons = stri.split("-->json:");

					String strisuiji = jsons[0].substring(jsons[0].indexOf("] ") + 2);// 接口请求随机数
					JSONObject jsoni = JSONObject.parseObject(jsons[1]);

					String orderid = jsoni.getString("orderid");// 接口单号
					String partnerid = jsoni.getString("partnerid");// 采购

					String reps = jsons[0].substring(jsons[0].indexOf("[") + 1, jsons[0].indexOf("] "));
					Date repTime = createTime.parse(reps); // 接口请求时间
					Date appRepTime = null; // 请求app时间
					Date appResTime = null; // rep响应时间
					Date RescgTime = null; // 异步回调采购时间
					String appRepTimes = ""; // 请求app时间
					String appResTimes = ""; // rep响应时间
					String RescgTimes = ""; // 异步回调采购时间
					String repUrl = ""; // 请求rep地址
					String repType = ""; // 请求rep途径 1 PC 2 APP
					String repResult = ""; // rep响应
					String redisResult = ""; // redis数据
					String jkcgResult = "";// 异步回调采购数据
					long sec = 0; // 请求rep耗时
					long jksec = 0; // 接口耗时

					int index = 0; //

					for (int j = (redisIndex - 200 < 0 ? 0 : redisIndex - 200); j < strArray2.size(); j++) {
						String strj = strArray2.get(j);
						String strjsuiji = "";
						if (strj.contains("-->")) {
							String s = strj.split("-->")[0];
							strjsuiji = s.substring(s.indexOf("] ") + 2);

						}
						if (strisuiji.equals(strjsuiji) && strj.contains("-->redis:")) {
							redisResult = strj.split("-->redis:")[1];
							if (!"null".equals(redisResult) && jsonUtil.isJson(redisResult)) {
								JSONObject repJson = JSONObject.parseObject(redisResult);
								redisResult = "msg: " + repJson.getString("msg") + " code: "
										+ repJson.getString("code");
							}
						}
						if (strisuiji.equals(strjsuiji) && strj.contains("-->redis状态标记转换:")) {

							redisIndex = strArray2.indexOf(strj);

							if ("1".equals(strj.split("-->redis状态标记转换:")[1])) {
								repType = "PC";
							} else if ("2".equals(strj.split("-->redis状态标记转换:")[1])) {
								repType = "APP";
							} else {
								repType = "未知";
							}
						}
						if (strisuiji.equals(strjsuiji)
								&& (strj.contains("requestApp") || strj.contains("requestWeb"))) {
							appRepTimes = strj.substring(strj.indexOf("[") + 1, strj.indexOf("] "));
							appRepTime = createTime.parse(appRepTimes);
							if (strj.contains("requestWeb")) {
								repUrl = strj.substring(strj.indexOf("requestWeb:") + 11, strj.indexOf(":param"));
							}
						}

						if (strisuiji.equals(strjsuiji) && strj.contains("-->repResult:")) {
							if ((strj.split("-->repResult:")).length >= 2
									&& !(strj.split("-->repResult:")[1]).equals("")) {
								repResult = strj.split("-->repResult:")[1];
							}
							index = strArray2.indexOf(strj);
							if (jsonUtil.isJson(repResult)) {
								JSONObject repJson = JSONObject.parseObject(repResult);
								if ("APP".equals(repType)) {
									if (repJson.getJSONObject("repData").getBooleanValue("success")) {
										repResult = "查询成功";
									} else {
										repResult = "查询失败：" + repJson.getJSONObject("repData").getString("msg");
									}
								} else if ("PC".equals(repType)) {
									if (repJson.getBooleanValue("success") && repJson.containsKey("data")) {
										repResult = "查询成功";
									} else {
										repResult = "查询失败：" + repJson.getString("msg");
									}
								}
							}

							appResTimes = strj.substring(strj.indexOf("[") + 1, strj.indexOf("] "));
							appResTime = createTime.parse(appResTimes);
						} else if (strisuiji.equals(strjsuiji) && strj.contains("-->resArray")) {

							index = strArray2.indexOf(strj);

							appResTimes = strj.substring(strj.indexOf("[") + 1, strj.indexOf("] "));
							appResTime = createTime.parse(appResTimes);
						}

						if (strisuiji.equals(strjsuiji) && strj.contains("-->异步回调采购")) {
							strj = strj.replace("?", "parm");
							jkcgResult = strj.split("parm")[1];
							if (jsonUtil.isJson(jkcgResult)) {
								JSONObject repJson = JSONObject.parseObject(jkcgResult);
								jkcgResult = "msg: " + repJson.getString("msg") + " code: " + repJson.getString("code");
							}

							RescgTimes = strj.substring(strj.indexOf("[") + 1, strj.indexOf("] "));
							RescgTime = createTime.parse(RescgTimes);
						}
						if (appRepTime != null && appResTime != null) {
							sec = appResTime.getTime() - appRepTime.getTime();

						}
						if (repTime != null && RescgTime != null) {
							jksec = RescgTime.getTime() - repTime.getTime();
							break;
						} else if (index != 0 && j == index + 500) {
							break;
						}

					}
					logWriteUtil.write("kt退票前置核验接口25-",
							reps + "\t" + orderid + "\t" + strisuiji + "\t" + partnerid + "\t" + repType + "\t" + repUrl
									+ "\t" + appRepTimes + "\t" + appResTimes + "\t" + sec + "\t" + RescgTimes + "\t"
									+ jksec + "\t" + redisResult + "\t" + repResult + "\t" + jkcgResult);

				}
			}

		} catch (Exception e) {
			System.out.println("**************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}
		System.out.println("===============***************=================");

	}

	///////////////////////////////////////////////////////////////////////////
	//新 携程退票请求数统计
	
	
	@Test
	public void readFileTestXCc2() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\退票成功率专用\\携程\\携程退票请求";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				String returnfailmsg = "";

				if (str.contains("parm:data=") && str.contains("xiecheng_zs")) {
					String[] ss = str.split("parm:data=");

					if (ss.length >= 2 && !ss[1].equals("")) {

						String[] sss = ss[1].split(":timeStamp:");
						JSONObject jsons = JSONObject.parseObject(sss[0]);
						String returnmsg = "";
						if (jsons.containsKey("returnmsg") && jsons.getString("returnmsg") != null) {
							returnmsg = URLDecoder.decode(jsons.getString("returnmsg"), "UTF-8");

						}
						if (jsons.getBooleanValue("returnstate") == true && jsons.getInteger("returntype") == 1) {
							logWriteUtil.write("sy退票成功23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
							success++;
						} else if (jsons.getInteger("returntype") == 1) {
							if (returnmsg.contains("人脸核验") || returnmsg.contains("人脸识别")) {
								renlian++;
							}
							logWriteUtil.write("kt退票失败23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
						} else {
							logWriteUtil.write("sy退票其他23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);

						}

					}

				} else /*
						 * if (str.contains("\"退票请求已接收\"") && str.contains("result:")) { String[] ss =
						 * str.split("result:"); if (fileName.endsWith("请求")) {
						 * 
						 * logWriteUtil.write("退票请求-", str); } else if (fileName.endsWith("111")) {
						 * JSONObject json = JSONObject.parseObject(ss[1]);
						 * 
						 * logWriteUtil.write("xc退票请求-", ss[0].substring(1, 20) + "\t" +
						 * json.getString("orderid"));
						 * 
						 * }
						 * 
						 * } else
						 */ if (str.contains("xiecheng_zs") && str.contains("jsonStr:")
						&& str.contains("return_ticket")) {
					String[] ss = str.split(":jsonStr:");
					int index = strArray.indexOf(str);
					String suiji = ss[0].substring(ss[0].indexOf("] ") + 2);
					if (jsonUtil.isJson(ss[1])) {
						JSONObject json = JSONObject.parseObject(ss[1]);
						JSONArray tickets = json.getJSONArray("tickets");
						for (int j = index; j < strArray.size(); j++) {
							String strj = strArray.get(j);
							if (strj.contains(":result:") && strj.contains("退票请求已接收")
									&& strj.substring(strj.indexOf("] ") + 2, strj.indexOf(":result:")).equals(suiji)) {
								num += tickets.size();
								break;
							}

						}
						logWriteUtil.write("xc退票请求-", ss[0].substring(1, 20) + "\t" + json.getString("orderid"));

					}


				}

			}
			System.out.println("携程退票成功：" + num);

			System.out.println("携程退票成功：" + success);
			System.out.println("携程人脸核验：" + renlian);
			System.out.println("===============***************=================");

		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}
	
	
	
	
	@Test
	public void readFileTestXCc3() {

		LogWriteUtil logWriteUtil = new LogWriteUtil();
		jsonUtil jsonUtil = new jsonUtil();

		String fileName = "C:\\Users\\Administrator\\Desktop\\退票成功率专用\\携程\\111";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		int num = 0;
		int success = 0;
		int renlian = 0;
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				String returnfailmsg = "";

				if (str.contains("parm:data=") && str.contains("xiecheng_zs")) {
					String[] ss = str.split("parm:data=");

					if (ss.length >= 2 && !ss[1].equals("")) {

						String[] sss = ss[1].split(":timeStamp:");
						JSONObject jsons = JSONObject.parseObject(sss[0]);
						String returnmsg = "";
						if (jsons.containsKey("returnmsg") && jsons.getString("returnmsg") != null) {
							returnmsg = URLDecoder.decode(jsons.getString("returnmsg"), "UTF-8");

						}
						if (jsons.getBooleanValue("returnstate") == true && jsons.getInteger("returntype") == 1) {
							logWriteUtil.write("sy退票成功23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
							success++;
						} else if (jsons.getInteger("returntype") == 1) {
							if (returnmsg.contains("人脸核验") || returnmsg.contains("人脸识别")) {
								renlian++;
							}
							logWriteUtil.write("kt退票失败23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);
						} else {
							logWriteUtil.write("sy退票其他23-",
									ss[0].substring(1, 20) + "\t" + jsons.getString("apiorderid") + "\t" + returnmsg);

						}

					}

				} else /*
						 * if (str.contains("\"退票请求已接收\"") && str.contains("result:")) { String[] ss =
						 * str.split("result:"); if (fileName.endsWith("请求")) {
						 * 
						 * logWriteUtil.write("退票请求-", str); } else if (fileName.endsWith("111")) {
						 * JSONObject json = JSONObject.parseObject(ss[1]);
						 * 
						 * logWriteUtil.write("xc退票请求-", ss[0].substring(1, 20) + "\t" +
						 * json.getString("orderid"));
						 * 
						 * }
						 * 
						 * } else
						 */ if (str.contains("xiecheng_zs") && str.contains("jsonStr:")
						&& str.contains("return_ticket")) {
							 String[]ss = str.split(":jsonStr");
							 String suiji = ss[0].substring(ss[0].indexOf("] "));
						 }

			}
			System.out.println("携程退票成功：" + num);

			System.out.println("携程退票成功：" + success);
			System.out.println("携程人脸核验：" + renlian);
			System.out.println("===============***************=================");

		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}

	}
	

	
	
	//美团下单占座统计             日志名：  MeiTuan_先占座_回调.log
	@Test
	public void readFileTestMeiTuan17() {

		String fileName = "C:\\Users\\Administrator\\Desktop\\2223\\23\\占座";
		File f = new File(fileName);
		ReadFileUtil readFileUtil = new ReadFileUtil();
		try {
			ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
			//
			for (int i = 0; i < strArray.size(); i++) {
				String str = strArray.get(i);

				if (str.contains("url:") && str.contains("美团回调参数：")) {
					String[] sss = str.split("美团回调参数：");
					if(sss.length >1) {
					String ss = sss[1];
					if (jsonUtil.isJson(ss)) {
						JSONObject jsons = JSONObject.parseObject(ss);
						if (!jsons.getString("bookingSuccess").equals("true") && !jsons.getString("code").equals("0") ) {
							logWriteUtil.write("美团下单占座失败_",
									sss[0].substring(1, 20) + "	" + jsons.getString("orderId") + "	"
											+ jsons.getString("code")
											);
						}

					}
					}

				}

			}

			System.out.println("===============***************=================");
		} catch (Exception e) {
			System.out.println("***************");
			logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
		}
	}

	//美团改签占座统计(待修改   1请求参数未加密-->)      日志名：  t同程火车票接口_4.12.改签占座回调.log
		@Test
		public void readFileTestMeiTuan18() {

			String fileName = "C:\\Users\\Administrator\\Desktop\\2223\\23\\改签";
			File f = new File(fileName);
			ReadFileUtil readFileUtil = new ReadFileUtil();
			try {
				ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
				//
				for (int i = 0; i < strArray.size(); i++) {
					String str = strArray.get(i);

					if (str.contains("callBackUrl:") && str.contains("http://agentgateway-train.meituan.com")) {
						// [2021-01-10 11:59:54.512]
						// http://jt.rsscc.com/trainspr/dom/afterSupplierGetSeat.action? seatdata=
						// {"supplierOrderId":"T2101107744D9D00095804DD7091D00FEE98D876B60","order12306":"E530273775","reqtime":"20210110115954","use_supplier_account":"0","arrive_date":"2021-01-10","sign":"ef2ff9602d70e06392041c6174498b87","ticket_entrance":"","pay_limit_time":"2021-01-10
						// 12:29:54.33","gtgjOrderId":"D60110115903578147","refund_online":0,"ticket_price_all":39.0,"paperlessCheckin":"1","supplier":"hthy","ticketInfo":[{"seat_type":"O","seat_type_name":"%E4%BA%8C%E7%AD%89%E5%BA%A7","coach_name":"04","seat_name":"09F%E5%8F%B7","ticket_price":"39.00","sub_order_12306":"E530273775104009F","ticket_id":"D601101159035781470"}],"reqtoken":"890999050605398041","status":"SUCCESS"}
						// --->0--->SUCCESS
						String[] sss = str.split("backjson=");
						if(sss.length >1) {
						String ss = sss[1];
						if (jsonUtil.isJson(ss)) {
							JSONObject jsons = JSONObject.parseObject(ss);
							if (!jsons.getString("success").equals("true") && !jsons.getString("msg").equals("改签占座成功")) {
								logWriteUtil.write("美团改签占座失败_",
										sss[0].substring(1, 20) + "	" + jsons.getString("orderid") + "	"
												+ jsons.getString("code") + "	"
												+ jsons.getString("msg")); // URLDecoder.decode
							}

						}
						}

					}

				}

				System.out.println("===============***************=================");
			} catch (Exception e) {
				System.out.println("***************");
				logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
			}
		}

	
		//空铁下单占座失败原因及code
		//          /data/tomcat-7.0.59-KongTieCallbackServlet_19232/bin/D:/userlog/202107/11 
		//          sz */t同程火车票接口_1.1申请分配座位席别回调.log
		@Test
		public void readFileTestMeiT19() {

			String fileName = "C:\\Users\\Administrator\\Desktop\\盛威\\3\\333";
			File f = new File(fileName);
			ReadFileUtil readFileUtil = new ReadFileUtil();
			try {
				ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
				//
				for (int i = 0; i < strArray.size(); i++) {
					String str = strArray.get(i);
					if(str.contains("?data=") ) {    //&& str.contains("fenbeitong.com") 分贝通需要下边加不等于占座成功
						String[] ss = str.split("data=");
	
						JSONObject json = JSONObject.parseObject(ss[1]);
						String msgg = URLDecoder.decode(json.getString("msg"), "UTF-8"); 
						if(!json.getString("code").equals("100") && json.getString("orderid").startsWith("66") ) {
																														//URLDecoder.decode(json.getString("msg"), "UTF-8")
	                      logWriteUtil.write("tc占座失败-", ss[0].substring(1,20) + "\t" + json.getString("orderid")+ "\t" + msgg + "\t" + json.getString("code"));
						}
					}
				}

				System.out.println("===============***************=================");
			} catch (Exception e) {
				System.out.println("***************");
				logWriteUtil.write("Exception_readFileTest", logWriteUtil.getTrace(e));
			}
		}

		
		
	    // 车次查询sign加密
	    // sign数字签名=md5(partnerid+method+reqtime+md5(key))，其中key 由我方分配
	    @Test
	    public void MD5Test1() {
	    	
	        // MD5Util.MD5(userName + method + reqTime +
	        // MD5Util.MD5("hNSdaVvhaM3ZzToNb0KcrM6kyzgMdd1c"))
	        // 采购商用户名
	        String partnerid = "hxht_zs";
	        // 请求方法
	        String method = "train_query";
	        // 请求时间
	        String reqtime = "20210819111645";
	        // 加密key
	        String key = desUtil.MD5("qOl7TTwlbaL39DznSDRp0BpKI8kstAeH", false);
	        System.out.println("加密后key：" + key);
	        // 加密签名
	        String sign = desUtil.MD5(partnerid + method + reqtime + key, false);   //正常加密使用
	        //String sign = desUtil.MD5(partnerid + reqtime + key, false);    //停运加密使用
	        System.out.println("加密后sign：" + sign);

	    }
	
	    
	    
	    
	 // 拼多多-云库 占座请求 （查接口拦截）
		@Test
		public void readFileTest20() {
			int count = 0;
			int count1 = 0;
			String path = "C:\\Users\\Administrator\\Desktop\\蜜蜂\\占座";
			File f = new File(path);
			ReadFileUtil readFileUtil = new ReadFileUtil();
			try {
				ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
				for (int i = 0; i < strArray.size(); i++) {
					String str = strArray.get(i);
					if (str.contains("航天接口响应:")) {

						// count++;

						//String suiji = str.substring(str.indexOf("]") + 2, str.indexOf("-->channel"));
						// System.out.println(suiji);
						//for (int j = i + 1; j < strArray.size(); j++) {
							//String strj = strArray.get(j);
							//if (strj.contains(suiji + ":响应-->")) {

								// count1++;

								String[] ss = str.split("航天接口响应:");
								if (jsonUtil.isJson(ss[1])) {
									JSONObject jsons = JSONObject.parseObject(ss[1]);
									 if(!("创建订单成功").equals(jsons.getString("msg"))) {
									logWriteUtil.write("pdd请求_",
											ss[0].substring(1, 20) + "\t" + jsons.getString("orderid") + "\t"
													+ jsons.getString("msg") + "\t" + jsons.getString("code"));
									//break;
									// }
								}
							}
						}

					}
				

				// System.out.println(count);
				// System.out.println(count1);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		// 拼多多-云库 出票请求 （查接口拦截）
				@Test
				public void readFileTest21() {
					int count = 0;
					int count1 = 0;
					String path = "C:\\Users\\Administrator\\Desktop\\蜜蜂\\出票";
					File f = new File(path);
					ReadFileUtil readFileUtil = new ReadFileUtil();
					try {
						ArrayList<String> strArray = readFileUtil.readFromTextFile(f);
						for (int i = 0; i < strArray.size(); i++) {
							String str = strArray.get(i);
							if (str.contains("航天接口响应:")) {

								// count++;

								//String suiji = str.substring(str.indexOf("]") + 2, str.indexOf("-->channel"));
								// System.out.println(suiji);
								//for (int j = i + 1; j < strArray.size(); j++) {
									//String strj = strArray.get(j);
									//if (strj.contains(suiji + ":响应-->")) {

										// count1++;

										String[] ss = str.split("航天接口响应:");
										if (jsonUtil.isJson(ss[1])) {
											JSONObject jsons = JSONObject.parseObject(ss[1]);
											 if(!("出票请求已接收").equals(jsons.getString("msg"))) {
											logWriteUtil.write("pdd请求_",
													ss[0].substring(1, 20) + "\t" + jsons.getString("orderid") + "\t"
															+ jsons.getString("msg") + "\t" + jsons.getString("code"));
											//break;
											// }
										}
									}
								}

							}
						

						// System.out.println(count);
						// System.out.println(count1);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
		
		
		
		
		
		
		
	
}
