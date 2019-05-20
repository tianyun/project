package com.tian;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WeiCurl {
	protected static final Logger LOGGER = Logger.getLogger(WeiCurl.class);

	public static void main(String[] args) {
		String filePath = "";
		if(args.length>0) {
			filePath = args[0];
		}else {
			filePath = WeiCurl.class.getResource("/").getPath();
		}
		WeiCurl weiCurl = new WeiCurl();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 获取微博地址
		String name = weiCurl.getProperties(filePath+"weiboURL.properties", "weiboURL");
		String uid = weiCurl.getWeiboUid(name);
		String containerid = weiCurl.getWeiboPro(uid);
		String URL = "https://m.weibo.cn/api/container/getIndex?type=uid&value="+uid+"&containerid="+containerid;
		
		// 获取微博内容并处理
		String resultStr = weiCurl.httpGet(URL);
		List<JSONObject> res = weiCurl.processInfo(resultStr);
		JSONObject mailJSON = res.get(0);

		// 判断是否有更新
		String weiboContent = weiCurl.getProperties(filePath+"weiboContent.properties", "content");
		try {
			if (weiboContent.equals(mailJSON.getString("content"))) {
				// 重新写入content
				Properties props = new Properties();
				props.load(new FileInputStream(filePath + "weiboContent.properties"));
				OutputStream fos = new FileOutputStream(
						filePath + "/weiboContent.properties");
				props.store(fos, "Update date: " + " " + df.format(new Date()));
				LOGGER.info(filePath+ "/weiboContent.properties  时间更新重新写入完成");
				fos.close();
			} else {
				// 获取邮箱
				String mail = weiCurl.getProperties(filePath+"mail.properties", "mail_1");
				// 发送邮件
				String content = "内容：" + mailJSON.getString("content") + "\n时间：" + mailJSON.getString("date");
				weiCurl.sendMail(mail, content);
				LOGGER.info("邮件 "+mail+" 发送成功"+df.format(new Date()));

				// 重新写入content
				Properties props = new Properties();
				props.load(new FileInputStream(filePath + "weiboContent.properties"));
				OutputStream fos = new FileOutputStream(
						filePath + "/weiboContent.properties");
				props.setProperty("date", mailJSON.getString("date"));
				props.setProperty("content", mailJSON.getString("content"));
				props.store(fos, "Update " + mail + " " + df.format(new Date()));
				LOGGER.info(filePath+ "/weiboContent.properties 内容重新写入完成"+df.format(new Date()));
				fos.close();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 处理返回的微博信息
	 * 
	 * @param str
	 * @return
	 */
	public List<JSONObject> processInfo(String str) {

		List<JSONObject> reList = new ArrayList<JSONObject>();
		JSONObject json = JSON.parseObject(str);
		JSONObject data = (JSONObject) json.getJSONObject("data");
		JSONArray arr = (JSONArray) data.getJSONArray("cards");
		for (int i = 0; i < arr.size(); i++) {
			try {
				JSONObject tempJson = ((JSONObject) arr.get(i)).getJSONObject("mblog");
				
				String tempStr ="";
				JSONObject tempdata = new JSONObject();
				String tempTxt = tempJson.getString("text");
				Pattern p = Pattern.compile("<([\\s\\S]*?)>"); // 匹配数字的正则表达式
				Matcher matcher = p.matcher(tempTxt);
				// 把字母替换成 空
				tempStr = matcher.replaceAll("");
				tempdata.put("content", tempStr);
				tempdata.put("date", tempJson.getString("created_at"));
				//LOGGER.info("=======================");
				//LOGGER.info(tempdata.getString("date"));
				reList.add(tempdata);
				tempdata = null;
			} catch (Exception e) {
				e.printStackTrace();
				continue;
				// TODO: handle exception
			}
			
		}
		LOGGER.info("=======================");

		LOGGER.info("从 processInfo 处理完，获取微博信息条数："+arr.size());
		return reList;
	}

	/**
	 * 向指定URL发送GET方法的请求 获取微博信息并取得需要信息
	 * 
	 * @param url 发送请求的UR 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public String httpGet(String URL) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(URL);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line + "";

			}

		} catch (Exception e) {
			LOGGER.info("发送GET请求出现异常！" + e);
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

	/**
	 * 通过微博名获取uid
	 * https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D1%26q%3D湖南大学&page_type=searchall
	 * @param name
	 * @return
	 */
	public String getWeiboUid(String name) {
		String url = "https://m.weibo.cn/api/container/getIndex?containerid=100103type%3D1%26q%3D"+name+"&page_type=searchall";
		String result = httpGet(url);
		JSONObject json = JSON.parseObject(result);
		JSONArray cards = (JSONArray) ((JSONObject) json.getJSONObject("data")).getJSONArray("cards");
		JSONArray card_group =((JSONObject)cards.get(0)).getJSONArray("card_group");
		JSONObject tar = (JSONObject)card_group.get(0);

		return tar.getJSONObject("user").getString("id");
	}
	
	/**
	 * 通过uid 返回containerid
	 * https://m.weibo.cn/api/container/getIndex?type=uid&value=2712377910&containerid=1005052712377910
	 * @param uid
	 * @return
	 */
	public String getWeiboPro(String uid) {
		String url = "https://m.weibo.cn/api/container/getIndex?type=uid&value="+uid+"&containerid=100505"+uid;
		String result = httpGet(url);
		JSONObject json = JSON.parseObject(result);
		JSONObject data = (JSONObject) json.getJSONObject("data").getJSONObject("tabsInfo");
		JSONArray arr = (JSONArray) data.getJSONArray("tabs");
		
		String containerid = ((JSONObject)arr.get(1)).getString("containerid");
		return containerid;
	}
	
	
	/**
	 * 发送邮件 
	 * 
	 * @param mail
	 * @param content
	 * @throws Exception
	 */
	public void sendMail(String mail, String content) throws Exception {
		Properties properties = new Properties();
		properties.setProperty("mail.host", "smtp.qq.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		LOGGER.info("邮箱配置完成");

		// 建立两点之间的链接
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("814405826@qq.com", "vzvvsurcvbmdbbaf");
			}
		});
		LOGGER.info("邮箱之间链接建立");
		// 创建邮件对象
		Message message = new MimeMessage(session);
		// 设置发件人
		try {
			message.setFrom(new InternetAddress("814405826@qq.com"));
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateNowStr = sdf.format(d);

			// 设置收件人
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));// 收件人
			// 设置主题
			message.setSubject("微博提醒");
			// 设置邮件正文 第二个参数是邮件发送的类型

			message.setContent("我的卿卿，你的卿卿，发微博啦 <br><br>" + content+"<br><br><br>"+dateNowStr, "text/html;charset=UTF-8");
			// 发送一封邮件
			Transport transport = session.getTransport();
			transport.connect("814405826@qq.com", "tianyunfclw123");
			Transport.send(message);
			LOGGER.info("===========================\n邮件发送成功");
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {

		}

	}

	/**
	 * 
	 * 从properties 获取配置文件
	 * 
	 * @param file 需要带后缀
	 * @param key
	 * @return
	 */
	public String getProperties(String file, String key) {

		Properties props = new Properties();
		try {
			props.load(new FileInputStream(file));
			return props.getProperty(key);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
