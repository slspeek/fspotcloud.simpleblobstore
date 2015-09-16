package com.googlecode.simpleblobstore.client;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private final Logger log = Logger.getLogger(BlobstoreClient.class.getName());

	private final String urlBase;

	public BlobstoreClient(String urlBase) {
		super();
		this.urlBase = urlBase;
	}

	public Map<String, List<BlobKey>> upload(Map<String, byte[]> dataMap) throws BlobstoreClientException {
		Map<String, List<BlobKey>> result = null;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		for (Map.Entry<String, byte[]> entry : dataMap.entrySet()) {
			String key = entry.getKey();
			byte[] data = entry.getValue();
			File targetFile;
			try {
				targetFile = File.createTempFile("bs-object", "", null);
				Files.write(data, targetFile);
			} catch (IOException e) {
				throw new BlobstoreClientException("Temp file could not be written", e);
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
			try {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new BlobstoreClientException("createurl call failed: " + response.getStatusLine(), null);
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
			response = httpclient.execute(httppost);
			try {
				if (response.getStatusLine().getStatusCode() != 200) {
					throw new BlobstoreClientException("upload call failed: " + response.getStatusLine(), null);
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = SerializationUtils.deserialize(EntityUtils
							.toByteArray(resEntity));
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			throw new BlobstoreClientException("General network exception", e);
		} catch (IOException e) {
			throw new BlobstoreClientException("General IO exception", e);
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				throw new BlobstoreClientException("Failed to close httpclient", e);
			}
		}
		return result;
	}

}
