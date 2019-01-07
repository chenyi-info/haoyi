package com.hy.otw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/** 
 * 微众请求工具类 
 * 
 * @author <a href="mailto:mohy@dtds.com.cn">莫海涌</a> 
 * @company 深圳动态网络科技有限公司 版权所有 (c) 2016 
 * @version 2017年10月30日 
 */
public class HttpsUtils {

	private final static Logger logger = Logger.getLogger(HttpsUtils.class);

	private static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	static void trustAllHttpsCertificates(SSLSocketFactory ssLSocketFactory) throws Exception {
		//		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		//		javax.net.ssl.TrustManager tm = new miTM();
		//		trustAllCerts[0] = tm;
		//		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		//		sc.init(null, trustAllCerts, null);
		//      javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(ssLSocketFactory);
	}

	private static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException {
			return;
		}
	}

	/**
	 * 读取url内容
	 *
	 * @param strurl
	 * @param params
	 * @param connectTimeout 毫秒
	 * @param readTimeout 毫秒
	 * @return
	 */
	public static String doGet(String strurl, Map<String, String> params, int connectTimeout, int readTimeout, boolean isEncodeParams, SSLSocketFactory ssLSocketFactory) {
		return doGet(strurl, params, null, connectTimeout, readTimeout, isEncodeParams, ssLSocketFactory);
	}

	/**
	 * 读取url内容
	 *
	 * @param strurl
	 * @param params
	 * @param connectTimeout 毫秒
	 * @param readTimeout 毫秒
	 * @return
	 */
	public static String doGet(String strurl, Map<String, String> params, int connectTimeout, int readTimeout, SSLSocketFactory ssLSocketFactory) {
		return doGet(strurl, params, null, connectTimeout, readTimeout, true, ssLSocketFactory);
	}

	/**
	 * 读取url内容
	 *
	 * @param strurl
	 * @param params
	 * @param connectTimeout 毫秒
	 * @param readTimeout 毫秒
	 * @return
	 */
	public static String doGet(String strurl, SSLSocketFactory ssLSocketFactory) {
		return doGet(strurl, null, null, 5000, 5000, true, ssLSocketFactory);
	}

	/**
	 * 读取url内容
	 *
	 * @param strurl
	 * @param params
	 * @param host
	 * @param connectTimeout毫秒
	 * @param readTimeout毫秒
	 * @return
	 */
	public static String doGet(String strurl, Map<String, String> params, String host, int connectTimeout, int readTimeout, boolean isEncodeParams, SSLSocketFactory ssLSocketFactory) {
		StringBuilder respContent = new StringBuilder();
		BufferedReader in = null;
		HttpsURLConnection conn = null;

		try {
			trustAllHttpsCertificates(ssLSocketFactory);
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			String param = params == null ? "" : toQueryString(params, isEncodeParams);

			String urlStr = strurl + (param.length() > 0 ? "?" : "") + param;
			logger.info("开始读取外部资源,url=" + urlStr);
			URL url = new URL(urlStr);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			if (host != null) {
				conn.setRequestProperty("Host", host);
			}
			//不等于200时直接抛出异常，不支持重定向
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("error ResponseCode[" + conn.getResponseCode() + "], message[" + conn.getResponseMessage()
						+ "] for url[" + strurl + "] with GET method");
			}
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String str;
			while ((str = in.readLine()) != null) {
				if (respContent.length() > 0) {
					respContent.append('\n');
				}
				respContent.append(str);
			}
		} catch (MalformedURLException e) {
			logger.error(MessageFormat.format("读取URL内容异常,URL={0},msg={1}", new Object[] { strurl, e.getMessage() }));
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(MessageFormat.format("读取URL内容异常,URL={0},msg={1}", new Object[] { strurl, e.getMessage() }));
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(MessageFormat.format("读取URL内容异常,URL={0},msg={1}", new Object[] { strurl, e.getMessage() }));
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
				}
			}
		}
		return respContent.toString();
	}

	/**
	 * @param params
	 * @param param
	 */
	public static String toQueryString(Map<String, String> params, boolean isEncodeParams) {
		StringBuilder param = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			if (param.length() > 0) {
				param.append('&');
			}
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			try {
				if (isEncodeParams) {
					value = URLEncoder.encode(value, "UTF-8");
				}
				param.append(key).append('=').append(value);
			} catch (UnsupportedEncodingException e) {
				logger.info(e.getMessage(), e);
			}
		}
		return param.toString();
	}

	/**
	 * 发送post请求
	 * @desc 增加超时时间避免socket阻塞不抛异常
	 */
	public static String doPost(final String url, final String data, final CloseableHttpClient httpClient, String charSet) throws Exception {
		//1.设置请求参数
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(data, charSet));

		//方式1
		//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);// 请求超时
		//httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);// 读取超时

		//方式2
		//setConnectTimeout：设置连接超时时间，单位毫秒。   java.net.SocketTimeoutException: connect timed out 最长默认1.5s
		//setConnectionRequestTimeout：设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
		//setSocketTimeout：请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。  java.net.SocketTimeoutException: Read timed out
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000)
				.setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000)
				.setStaleConnectionCheckEnabled(true)
				.build();
		httpPost.setConfig(requestConfig);

		logger.info("【HttpsUtils.httpPost有设置超时时长-请求参数】：url=" + url + "|data=" + data);
		CloseableHttpResponse response = httpClient.execute(httpPost);

		//2.处理返回结果
		String resultStr = "";
		try {
			final HttpEntity entity = response.getEntity();
			final String contentType = ContentType.getOrDefault(entity).getCharset().name();
			final InputStream is = entity.getContent();
			resultStr = read(is, contentType);
			logger.info("【HttpsUtils.httpPost有设置超时时长-返回结果】：url=" + url + "|data=" + data + "|resultStr=" + resultStr);
			EntityUtils.consume(entity);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		//3.返回结果
		return resultStr;
	}

	/**
	 * 发送post请求，带文本数据.
	 * @param url 请求地址
	 * @param data 文本数据
	 * @param httpClient httpclient
	 * @return post返回数据
	 * @throws Exception 异常
	 */
	public static String doPost(final String url, final String data, final CloseableHttpClient httpClient) throws Exception {
		return doPost(url, data, httpClient, "UTF-8");
	}

	/**
	 * 读取流.
	 * @param is 输入流.
	 * @param charSet 编码
	 * @return 读取文本
	 * @throws IOException io异常
	 */
	private static String read(InputStream is, String charSet) throws IOException {
		if (StringUtils.isEmpty(charSet)) {
			charSet = "utf-8";
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(is, charSet));
		String s;
		StringBuffer sb = new StringBuffer();
		while ((s = in.readLine()) != null) {
			sb.append(s + "\n");
		}
		in.close();
		return sb.toString();
	}

	/**************上传文件**************/
	public static String doPostFile(String url, File file, final CloseableHttpClient httpClient) throws IOException {
		String rsp = null;
		try {
			final HttpPost httpPost = new HttpPost(url);
			//httpPost.setHeader("Content-Type", "multipart/form-data");

			//			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			//			builder.addBinaryBody("uploadFile", inputStream, ContentType.create("multipart/form-data"), "text01.txt");
			//			StringBody stringBody = new StringBody("12", ContentType.MULTIPART_FORM_DATA);
			//			builder.addPart("id", stringBody);
			//			HttpEntity entity = builder.build();

			MultipartEntity mutiEntity = new MultipartEntity();
			mutiEntity.addPart("file", new FileBody(file));
			httpPost.setEntity(mutiEntity);

			//FileEntity entity = new FileEntity(file, "multipart/form-data");
			//httpPost.setEntity(entity);

			CloseableHttpResponse response = httpClient.execute(httpPost);
			final HttpEntity responseEntiy = response.getEntity();
			//final String contentType = ContentType.getOrDefault(responseEntiy).getCharset().name();
			final InputStream is = responseEntiy.getContent();
			rsp = read(is, "utf-8");
			EntityUtils.consume(responseEntiy);
		} catch (IOException e) {
			logger.error(url, e);
			throw e;
		}

		return rsp;
	}

	private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
		entry.append(fieldValue);
		return entry.toString().getBytes(charset);
	}

	private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\";filename=\"");
		entry.append(fileName);
		entry.append("\"\r\nContent-Type:");
		entry.append(mimeType);
		entry.append("\r\n\r\n");
		return entry.toString().getBytes(charset);
	}
}
