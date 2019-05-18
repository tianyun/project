package com.tian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tian.WeiCurl;

public class WeiboList extends HttpServlet {
	 
	  private String message;

	  public void init() throws ServletException
	  {
	      // 执行必需的初始化
	      message = "Hello World";
	  }

	  public void doGet(HttpServletRequest request,
	                    HttpServletResponse response)
	            throws ServletException, IOException
	  {
	      // 设置响应内容类型
		  response.setContentType("application/json;charset=utf-8");

	      //获取微博信息
	      
	      String URL = "https://m.weibo.cn/api/container/getIndex?uid=1827861545&luicode=10000011&lfid=100103type%3D1%26q%3D%E5%8D%BF%E5%9B%BD%E5%8D%BF%E5%9F%8E&type=uid&value=1827861545&containerid=1076031827861545";
	      WeiCurl weiCurl = new WeiCurl();
		String resultStr = weiCurl.httpGet(URL);
		List<String> reList= weiCurl.processInfo(resultStr);	      
	    
		JSONObject data = new JSONObject();
		for (int i = 0; i < reList.size(); i++) {
			data.put("data"+i, reList.get(i));
		}
		System.out.println(data.toString());	      
		response.getWriter().write(data.toString());
	  }
	  
	  public void destroy()
	  {
	      // 什么也不做
	  }
	}
