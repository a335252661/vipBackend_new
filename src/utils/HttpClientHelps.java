package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientHelps {

	/**
	 * 将map转换成url
	 *
	 * @param map
	 * @return
	 */
	public static String getUrlParamsByMap(Map<String, Object> map) {
		if (map == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue());
			sb.append("&");
		}
		String s = sb.toString();
		if (s.endsWith("&")) {
			s = StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}

	public static String sendPost(String url , String date) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity( new StringEntity(date,"UTF-8"));
			httpPost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity, "UTF-8");
			System.out.println(str);
			// 关闭
			return str;
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String sendGet(String url) {
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			response = client.execute(httpget);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(result);
		return result;
	}

	public static String sendGet(String url , Map<String , Object> map) {

		HttpClientHelps.getUrlParamsByMap(map);

		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			response = client.execute(httpget);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(result);
		return result;
	}

	/**
	 * post请求,发送json数据
	 *
	 * @param url
	 * @param json
	 * @return
	 */
	public static JSONObject post(String url, String json) {
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json, "UTF-8"); // 中文乱码在此解决
			s.setContentType("application/json");
			post.setEntity(s);
			HttpResponse res = HttpClients.createDefault().execute(post);
			System.out.println(res);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				System.out.println(result);
				response = JSON.parseObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public static String sendPostOrGet(String url, String param) {
		OutputStreamWriter out = null;
		BufferedReader in = null;

		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			// 获取URLConnection对象对应的输出流
			//out = new PrintWriter(conn.getOutputStream());
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			// 发送请求参数
			out.write(param.toString());
			//out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			//System.out.println("发送 POST 请求出现异常！" + e);
				e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}


	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
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

	public static int sendPatch(String url,String jsonParam  /*, Map<String, String> header*/){
		System.out.println("接口请求url===================================");
		System.out.println(url);
		int statusCode = 0;
		org.apache.http.impl.client.CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
		HttpPatch httpPatch = new HttpPatch(url);
		//请求头
//		if (MapUtils.isNotEmpty(header)) {
//			for (Map.Entry<String, String> entry : header.entrySet()) {
//				httpPatch.addHeader(entry.getKey(), entry.getValue());
//			}
//		}
		httpPatch.setHeader("Content-type", "application/json");
		try {
			if (jsonParam != null){
				StringEntity entity = new StringEntity(jsonParam,"UTF-8");
				httpPatch.setEntity(entity);
			}
			//返回状态
			HttpResponse response = httpClient.execute(httpPatch);
			statusCode = response.getStatusLine().getStatusCode();
			//返回内容
			String resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println("接口返回内容===================================");
			System.out.println(resultString);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return statusCode;
	}

	public static <T> String insertAndReturn(String uri,String requestString){
		//commons-httpclient-3.0.jar
		//commons-codec-1.10.jar
		HttpClient httpClient = new HttpClient();
//		PostMethod bankAgentSend = new PostMethod(uri);
//		GetMethod bankAgentSend = new GetMethod(uri);
		PutMethod bankAgentSend = new PutMethod(uri);
		bankAgentSend.addRequestHeader("Content-Type", "application/json");
		bankAgentSend.addRequestHeader("x-http-method-override", "PATCH");
		try{
			RequestEntity entity = new StringRequestEntity(requestString, "application/json", "UTF-8");
			bankAgentSend.setRequestEntity(entity);
			httpClient.executeMethod(bankAgentSend);
			String responseBodyAsString = bankAgentSend.getResponseBodyAsString();
			System.out.println(responseBodyAsString);
			return responseBodyAsString;
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			bankAgentSend.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0L);
		}
		return null;
	}



	public static void main(String[] args) {
//		String url="http://10.145.205.69:9011/Ducc/userDataAuth";
//		String data = "{\"requestTime\":\"1\",\"requestId\":\"1\",\"inputNo\":\"18918582289\",\"queryType\":\"2\"}";
//		String s = HttpClientHelps.sendPost(url, data);
//		System.out.println(s);
//		HttpClientHelps.sendGet("192.168.22.62:32006/test/cld-dev/consumer-demo/echo-rest/22?test=1");
		while (true){
			try { TimeUnit.MILLISECONDS.sleep(300);  } catch (InterruptedException e) { e.printStackTrace(); }
			HttpClientHelps.sendGet("http://192.168.22.62:32006/test/cld-dev/consumer-demo/echo-rest/22?test=1");
		}

	}

}
