package com.googlecode.simpleblobstore.client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.io.Files;
import com.googlecode.simpleblobstore.BlobKey;

public class BlobstoreClient {

	private final String urlBase;

	public BlobstoreClient(String urlBase) {
		super();
		this.urlBase = urlBase;
	}

	public Map<String, List<BlobKey>> upload(Map<String, byte[]> dataMap) {
		Map<String, List<BlobKey>> result = null;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Map.Entry<String, byte[]> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			byte[] data = entry.getValue();
			File targetFile;
			try {
				targetFile = File.createTempFile("bs-test-object", ".jpg", null);
				Files.write(data, targetFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			//ByteArrayBody bin = new ByteArrayBody(data, "");
			FileBody bin = new FileBody(targetFile);
			builder.addPart(key, bin);
		}
		String uploadUrl = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(urlBase + "defaultcreateurl" + "?success=%2Fdefaultupload");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			System.out.println(httpGet.getRequestLine());
			try {
				System.out
						.println("---------------CREATEURL-CALL-------------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != 200) {
					return result;
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					uploadUrl = EntityUtils.toString(resEntity);
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
			HttpPost httppost = new HttpPost(uploadUrl);

			HttpEntity reqEntity = builder.build();

			httppost.setEntity(reqEntity);
			System.out
					.println("executing request " + httppost.getRequestLine());
			response = httpclient.execute(httppost);
			try {
				System.out.println("---------UPLOAD-----------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != 200) {
					return result;
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = SerializationUtils.deserialize(EntityUtils
							.toByteArray(resEntity));
					System.out.println("Returning:  " + result);
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

}
