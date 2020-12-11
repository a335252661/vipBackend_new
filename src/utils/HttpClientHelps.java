package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.*;
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

public class HttpClientHelps {



	public static String dopatchnew(String pathUrl, String data){
		OutputStreamWriter out = null;
		BufferedReader br = null;
		String result = "";
		try {
			URL url = new URL(pathUrl);
			//打开和url之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String plainCredentials="id:password";
			String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));
			conn.setRequestProperty("Authorization", "Basic " + base64Credentials);
			//请求方式
			conn.setRequestMethod("POST");
			//conn.setRequestMethod("GET");
			conn.setConnectTimeout(10000);// 设置连接主机超时时间（毫秒）
			conn.setReadTimeout(10000);// 设置主机读取数据超时
			//设置通用的请求属性
			conn.setRequestProperty("x-http-method-override", "PATCH");
			//设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			//DoOutput设置是否向httpUrlConnection输出，DoInput设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			conn.setDoOutput(true);
			conn.setDoInput(true);

			/**
			 * 下面的三句代码，就是调用第三方http接口
			 */
			//获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			//发送请求参数即数据
			out.write(data);
			//flush输出流的缓冲
			out.flush();

			/**
			 * 下面的代码相当于，获取调用第三方http接口后返回的结果
			 */
			//获取URLConnection对象对应的输入流
			InputStream is = conn.getInputStream();
			//构造一个字符流缓存
			br = new BufferedReader(new InputStreamReader(is));
			String str = "";
			while ((str = br.readLine()) != null){
				result += str;
			}
			//关闭流
			is.close();
			//断开连接，disconnect是在底层tcp socket链接空闲时才切断，如果正在被其他线程使用就不切断。
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (out != null){
					out.close();
				}
				if (br != null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	public static void HttpClientPost(String url , String date) {
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
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * post请求,发送json数据
	 *
	 * @param url
	 * @param json
	 * @return
	 */
	public static JSONObject doJsonPost(String url, String json) {
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
			//	e.printStackTrace();
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
		String url="http://10.145.220.57:8063/acct/acctdatabill/billInvoice/189013560472/87115123992";
		com.alibaba.fastjson.JSONObject js= new com.alibaba.fastjson.JSONObject();
		js.put("payMethod",12);
//		HttpClientHelps.sendPatch(url, js.toJSONString());
		HttpClientHelps.insertAndReturn(url, js.toJSONString());




//		HttpClientHelps.insertAndReturn(url, js.toJSONString());
//		Map dataMap = new HashMap();
//		dataMap.put("balanceType","99");
//		dataMap.put("requestTime",new Date());
//		dataMap.put("destinationAttr",6);//分账
//		dataMap.put("destinationAttrDetail",1);
//		dataMap.put("destinationExpTime","20991231");
//		dataMap.put("destinationId","ACMB8310100000089665");
//		dataMap.put("objType","5BA"); //分账
//		dataMap.put("operSystem","03");//(必填)发起操作系统(01:老UVC,02:付费易,03:空中充值,04:CRM(现金充值),05:电子代办,06:网厅,07:银联卡(IVR接入),11:CBS(余额转移))
//		dataMap.put("organCode",898904);//(必填)网点编号
//		dataMap.put("prolongDays",0);//(必填)充值延长有效期（天数）
//		dataMap.put("rechargeAmount","20000");//(必填)充值金额，单位为分
//		dataMap.put("rechargeFlag",0);
//		dataMap.put("rechargeFlowAmount",null);//流量
//		dataMap.put("rechargeUnit",0);
//		dataMap.put("requestAmount",0);
//		dataMap.put("secChannelCode","20");//(必填)二级渠道代码
//		dataMap.put("tmpSn","seq11020304");//(必填)充值交易流水号(可同REQUEST_SEQ)
//		dataMap.put("staffId",244033247435L);
//		dataMap.put("noBillFlag",0);
//		dataMap.put("payForm",0);
//		dataMap.put("payChunnel",0);
//		dataMap.put("channelId","0");
//		dataMap.put("rechargeAccountType",0);
//
//		String url = "http://10.145.221.42:8263/acct/acctbizbalance/acctAsyncRecharge";
//		HttpClientHelps.HttpClientPost(url, JSONObject.toJSONString(dataMap));

	}

}
