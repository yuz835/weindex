package ca.weindex.web.helper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonHelper {
	public static JSONObject get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte b;
			while ((b = (byte) is.read()) != -1) {
				os.write(b);
			}
			is.close();
			byte[] bt = os.toByteArray();
			String content = new String(bt);
			System.out.println(content);
			JSONObject obj = JSON.parseObject(content);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
		return null;
	}

	public static JSONObject post(String url) {
		return post(url, null);
	}

	public static JSONObject post(String url, Map<String, Object> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			if (params != null && !params.isEmpty()) {
				HttpParams hp = new BasicHttpParams();
				Set<Entry<String, Object>> set = params.entrySet();
				for (Entry<String, Object> e : set) {
					hp.setParameter(e.getKey(), e.getValue());
				}
				post.setParams(hp);
			}
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			byte b;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			while ((b = (byte) is.read()) != -1) {
				os.write(b);
			}
			is.close();
			byte[] bt = os.toByteArray();
			String content = new String(bt);
			System.out.println(content);
			JSONObject obj = JSON.parseObject(content);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}
	
	public static JSONObject post(String url, Map<String, String> params, String fileParamName, String fileName, byte[] fileContent) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		//post.setHeader("Content-Type", "multipart/form-data");
		try {
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			ByteArrayBody  fileBody = new ByteArrayBody(fileContent, "application/octet-stream", fileName);			
			entity.addPart(fileParamName, fileBody);
			
			Set<Entry<String, String>> set = params.entrySet();
			for (Entry<String, String> e : set) {
				entity.addPart(e.getKey(), new StringBody(e.getValue()));
			}

			post.setEntity(entity);
			HttpResponse response = httpclient.execute(post);
			HttpEntity httpEntity = response.getEntity();
			InputStream is = httpEntity.getContent();
			byte b;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			while ((b = (byte) is.read()) != -1) {
				os.write(b);
			}
			is.close();
			byte[] bt = os.toByteArray();
			String content = new String(bt);
			System.out.println(content);
			JSONObject obj = JSON.parseObject(content);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return null;
	}
	
	
}
