package com.tian;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WeiCurl {

	public static void main(String[] args) {
		
		
		String URL = "https://m.weibo.cn/api/container/getIndex?uid=1827861545&luicode=10000011&lfid=100103type%3D1%26q%3D%E5%8D%BF%E5%9B%BD%E5%8D%BF%E5%9F%8E&type=uid&value=1827861545&containerid=1076031827861545";
		
		String resultStr = httpGet(URL);
		String res = processInfo(resultStr);

	}
	


	public static String processInfo(String str) {
		
		JSONObject json = JSON.parseObject(str);
		JSONObject data = (JSONObject)json.getJSONObject("data");
		JSONArray arr = (JSONArray)data.getJSONArray("cards");
		for (int i = 0; i < arr.size(); i++) {
			System.out.println("==========================");
			JSONObject tempJson = ((JSONObject)arr.get(i)).getJSONObject("mblog");
			String tempStr = tempJson.getString("raw_text");
			if(tempStr == null) {
				String tempTxt = tempJson.getString("text");
		        Pattern p = Pattern.compile("<([\\s\\S]*?)>");  //匹配数字的正则表达式
		        Matcher matcher = p.matcher(tempTxt);
		        // 把字母替换成 0
		        String qx_new = matcher.replaceAll("");
		        
				System.out.println(qx_new);
			}else {
				System.out.println(tempStr);
			}
		}
		System.out.println(arr.size());
		return null;
	}
	
	
	/**
	 * 向指定URL发送GET方法的请求 获取微博信息并取得需要信息
	 * 
	 * @param url 发送请求的UR 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String httpGet(String URL) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(URL);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result +=line+"";

			}

		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
}
