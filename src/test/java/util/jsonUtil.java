package util;

import com.alibaba.fastjson.JSONObject;


public class jsonUtil {

	public boolean isJson(String data) {

		if (data.equals("") || data == null) {
			return false;
		}
		
		try {
			JSONObject.parse(data);   
		} catch (Exception e) {
			return false;
		}
		return true;

	}
	
	public boolean isJsonarray(String data) {

		if (data.equals("") || data == null) {
			return false;
		}
		
		try {
			JSONObject.parseArray(data);   
		} catch (Exception e) {
			return false;
		}
		return true;

	}
}
