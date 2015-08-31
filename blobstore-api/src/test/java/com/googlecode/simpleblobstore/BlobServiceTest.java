package com.googlecode.simpleblobstore;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.io.Files;
import com.google.guiceberry.junit4.GuiceBerryRule;

@RunWith(Parameterized.class)
public class BlobServiceTest {

	@Rule
	public GuiceBerryRule guiceBerry = new GuiceBerryRule(
			PlaceHolderGuiceBerryEnv.class);

	@Inject private String URLBASE;
	final private byte[] data;

	public BlobServiceTest(byte[] data) {
		this.data = data;
	}

	private BlobKey saveInBlobstore(byte[] data) throws IOException {
		File targetFile = File.createTempFile("bs-test-object", ".jpg", null);
		Files.write(data, targetFile);
		String uploadUrl = "";
		BlobKey result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(URLBASE + "createurl");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out
						.println("---------------CREATEURL-CALL-------------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != 200) {
					Assert.fail();
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
			FileBody bin = new FileBody(targetFile);
			StringBody comment = new StringBody("A binary file of some kind",
					ContentType.TEXT_PLAIN);
			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart("bin", bin).addPart("comment", comment).build();
			httppost.setEntity(reqEntity);
			System.out
					.println("executing request " + httppost.getRequestLine());
			response = httpclient.execute(httppost);
			try {
				System.out.println("---------UPLOAD-----------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != 200) {
					Assert.fail();
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = new BlobKey(EntityUtils.toString(resEntity));
					System.out.println("Returning:  " + result);
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

	@Test
	public void testGetInfo() throws Exception {
		BlobKey key = saveInBlobstore(data);
		assertEquals(data.length, getInfo(key).getLength());
	}
	
	private BlobInfo getInfo(BlobKey key) throws IOException {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(URLBASE + "info?id="
					+ key.getKeyString());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out
						.println("---------------INFO-CALL-------------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						result = EntityUtils.toString(resEntity);
					}
					EntityUtils.consume(resEntity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		String mimeType = result.split(":")[1];
		long length = Long.valueOf(result.split(":")[0]);
		BlobInfo b = new BlobInfo(mimeType, length);
		return b;
	}

	@Test
	public void testSaveAndLoad() throws Exception {
		BlobKey key = saveInBlobstore(data);
		byte[] retrieved = null;
		retrieved = loadBytes(key);
		assertNotNull(retrieved);
		assertEquals(data.length, retrieved.length);
		assertTrue(Arrays.equals(data, retrieved));
	}

	@Test
	public void testDelete() throws Exception {
		BlobKey key = saveInBlobstore(data);
		byte[] retrieved = null;

		retrieved = loadBytes(key);
		assertNotNull(retrieved);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpGet = new HttpPost(URLBASE + "delete?id="
					+ key.getKeyString());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out
						.println("---------------DELETE-CALL-------------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() != 200) {
					Assert.fail();
				}
			} finally {
				response.close();
			}

		} finally {
			httpclient.close();
		}
		retrieved = loadBytes(key);
		assertNull(retrieved);
	}

	private byte[] loadBytes(BlobKey key) throws IOException {
		byte[] result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(URLBASE + "serve?id="
					+ key.getKeyString());
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out
						.println("---------------SERVEBLOB-CALL-------------------");
				System.out.println(response.getStatusLine());
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity resEntity = response.getEntity();
					if (resEntity != null) {
						result = EntityUtils.toByteArray(resEntity);
					}
					EntityUtils.consume(resEntity);
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
		return result;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> configs() {
		List<Object[]> result = newArrayList(randomBytesArray(100),
				randomBytesArray(1000), randomBytesArray(10000),
				randomBytesArray(2000000));
		return result;
	}

	private static Object[] randomBytesArray(int length) {
		byte[] result = new byte[length];
		(new Random()).nextBytes(result);
		return new Object[] { result };
	}
}
