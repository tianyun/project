package com.tian.servlet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.WeiCurl;

@Controller
public class WeiboList extends HttpServlet {
	protected static final Logger LOGGER = Logger.getLogger(WeiboList.class);

	SimpleDateFormat df;
	WeiCurl weiCurl = new WeiCurl();
	public void init() throws ServletException {
		// 执行必需的初始化
	}

	@RequestMapping(value = "/WeiboList.do", method = { RequestMethod.GET })
	public void getList(HttpServletResponse response) throws IOException {
		System.out.println("get request");
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");

		// 获取微博地址
		String name = weiCurl.getProperties(WeiboList.class.getResource("/").getPath()+"weiboURL.properties", "weiboURL");
		String uid = weiCurl.getWeiboUid(name);
		System.out.println(uid);

		String containerid = weiCurl.getWeiboPro(uid);
		String URL = "https://m.weibo.cn/api/container/getIndex?type=uid&value="+uid+"&containerid="+containerid;
		System.out.println(URL);
		String resultStr = weiCurl.httpGet(URL);
		List<JSONObject> reList = weiCurl.processInfo(resultStr);

		JSONArray data = new JSONArray();
		for (int i = 0; i < reList.size(); i++) {
			data.add(reList.get(i));

		}
		response.getWriter().write(data.toString());
	}

	@RequestMapping(value = "/getMail.do", method = { RequestMethod.GET })
	public void getMail(HttpServletResponse response) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");
		JSONObject reJson = new JSONObject();
		// 获取邮箱
		try {
			InputStream inStream = WeiCurl.class.getResourceAsStream("/mail.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			String mailURL = prop.getProperty("mail_1");
			LOGGER.info("获取参数 mailURL ："+mailURL);
			reJson.put("mail", mailURL);
			response.getWriter().write(reJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/getWeiboID.do", method = { RequestMethod.GET })
	public void getWeiboID(HttpServletResponse response) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");
		JSONObject reJson = new JSONObject();
		// 获取邮箱
		try {
			InputStream inStream = WeiCurl.class.getResourceAsStream("/weiboURL.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			String weiboName = prop.getProperty("weiboURL");
			LOGGER.info("获取参数 weiboName ："+weiboName);
			reJson.put("weiboName", weiboName);
			response.getWriter().write(reJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/editWeiboId.do", method = { RequestMethod.GET })
	public void editWeiboId(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");
		// 获取邮箱
		String weiboName = request.getParameter("weiboName");
		LOGGER.info("获取参数 weiboName：" + weiboName);
		JSONObject reJson = new JSONObject();
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(WeiCurl.class.getResource("/").getPath() + "weiboURL.properties"));
			OutputStream fos = new FileOutputStream(WeiCurl.class.getResource("/").getPath() + "/weiboURL.properties");
			props.setProperty("weiboURL", weiboName);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			props.store(fos, "Update " + weiboName + " " + df.format(new Date()));
			// LOGGER.info(props.getProperty("mail_1"));
			reJson.put("status", "ok");
			response.getWriter().write(reJson.toString());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/editMail.do", method = { RequestMethod.GET })
	public void editMail(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");
		// 获取邮箱
		String mail = request.getParameter("mail");
		LOGGER.info("获取参数mail：" + mail);
		JSONObject reJson = new JSONObject();
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(WeiCurl.class.getResource("/").getPath() + "mail.properties"));
			OutputStream fos = new FileOutputStream(WeiCurl.class.getResource("/").getPath() + "/mail.properties");
			props.setProperty("mail_1", mail);
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			props.store(fos, "Update " + mail + " " + df.format(new Date()));
			// LOGGER.info(props.getProperty("mail_1"));
			reJson.put("status", "ok");
			response.getWriter().write(reJson.toString());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {
		// 什么也不做
	}
}
