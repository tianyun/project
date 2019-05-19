package com.tian.servlet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.WeiCurl;

@Controller
public class WeiboList extends HttpServlet {

	private String message;
	SimpleDateFormat df;
	
	public void init() throws ServletException {
		// 执行必需的初始化
		message = "Hello World";
	}

	@RequestMapping(value = "/WeiboList.do", method = { RequestMethod.GET })
	public void getList(HttpServletResponse response) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");

		// 获取微博信息
		String URL = "https://m.weibo.cn/api/container/getIndex?uid=1827861545&luicode=10000011&lfid=100103type%3D1%26q%3D%E5%8D%BF%E5%9B%BD%E5%8D%BF%E5%9F%8E&type=uid&value=1827861545&containerid=1076031827861545";
		WeiCurl weiCurl = new WeiCurl();
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
		//获取邮箱
		try {
			InputStream inStream = WeiCurl.class.getResourceAsStream("/mail.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			String mailURL = prop.getProperty("mail_1");
			System.out.println(mailURL);
			reJson.put("mail", mailURL);
			response.getWriter().write(reJson.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/editMail.do", method = { RequestMethod.GET })
	public void editMail(HttpServletResponse response, HttpServletRequest request) throws IOException {
		// 设置响应内容类型
		response.setContentType("application/json;charset=utf-8");
		//获取邮箱
        String mail=request.getParameter("mail");
        System.out.println("获取参数mail："+mail);
		JSONObject reJson = new JSONObject();
		Properties props=new Properties();
        try {
			props.load(new FileInputStream(WeiCurl.class.getResource("/").getPath()+"mail.properties"));
			OutputStream fos = new FileOutputStream(WeiCurl.class.getResource("/").getPath()+"/mail.properties");          
	        props.setProperty("mail_1", mail);
	        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        props.store(fos, "Update "+mail+" "+df.format(new Date()));
	        //System.out.println(props.getProperty("mail_1"));
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
