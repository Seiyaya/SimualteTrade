package com.seiyaya.common.http;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * http相关工具类
 * 
 * @author Seiyaya
 *
 */
@Data
@Slf4j
public class HttpUtils {

	private static final String ENCODING = "UTF-8";

	private PoolingHttpClientConnectionManager cm = null;

	private static HttpUtils httpUtils = new HttpUtils();
	
	private HttpUtils() {
		init();
	}
	
	public static HttpUtils getHttpUtils() {
		return httpUtils;
	}
	
	public void init() {
		log.info("初始化httpUtils");
		LayeredConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			log.error("", e);
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
		cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		cm.setMaxTotal(200);
		cm.setDefaultMaxPerRoute(20);
	}

	public CloseableHttpClient getHttpClient() {
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		return httpClient;
	}

	public String sendGet(String url) {
		return sendGet(url, null, ENCODING);
	}

	public String sendGet(String url, Map<String, Object> params) {
		return sendGet(url, params, ENCODING);
	}

	public String sendGet(String url, Map<String, Object> params, String encoding) {
		log.info("请求方式->get 请求的url->{} 请求的参数->{}", url, params);
		String resultJson = null;
		CloseableHttpClient client = getHttpClient();
		CloseableHttpResponse response = null;
		HttpGet httpGet = new HttpGet();
		httpGet.addHeader("Cookie",
				"xq_a_token=bb7610f538b447ebadd6a374cdff374206f350dd; xq_a_token.sig=XglA1uiAYkfyfKlhbuJdRhRTTM4; xq_r_token=c946fa437eff0f7b251c2a7babd12b5c9e5a1851; xq_r_token.sig=jW7KrLgtGYffUvfG3DfPexDR8RQ; _ga=GA1.2.1133658.1538990044; _gid=GA1.2.1240074852.1538990044; Hm_lvt_1db88642e346389874251b5a1eded6e3=1538990044; u=671538990045610; device_id=561e497c5e923875a0be9767d38b4637; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1538990057");
		try {
			URIBuilder builder = new URIBuilder(url);
			if (params != null) {
				for (String key : params.keySet()) {
					builder.addParameter(key, params.get(key).toString());
				}
			}
			URI uri = builder.build();
			httpGet.setURI(uri);
			log.info("real url ->{}",httpGet.getURI().toString());
			response = client.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				resultJson = EntityUtils.toString(response.getEntity(), encoding);
			} else {
				log.error("响应失败，状态码：" + status);
			}
		} catch (Exception e) {
			log.error("发送get请求失败", e);
			throw new RuntimeException(e);
		} finally {
			httpGet.releaseConnection();
			if(response!=null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error("",e);
				}
			}
		}
		return resultJson;
	}

	public String getElementText(String url, Map<String, Object> params, String className) {
		String result = sendGet(url, params, ENCODING);
		return Jsoup.parse(result).getElementsByClass(className).get(0).text();
	}
	
	public static void main(String[] args) {
		System.getProperties().keySet().forEach((key)->{
			log.info("{} --> {}",key,System.getProperties().get(key));
		});;
	}
}
