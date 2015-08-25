package com.googlecode.simpleblobstore;

import com.google.common.io.Files;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.guiceberry.junit4.GuiceBerryRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BlobServiceTest {

	@Rule
	public GuiceBerryRule guiceBerry = new GuiceBerryRule(
			PlaceHolderGuiceBerryEnv.class);

	@Inject
	BlobService service;

	final private byte[] data;

	public BlobServiceTest(byte[] data) {
		this.data = data;
	}

	@Test
	public void testDelete() throws Exception {
		BlobKey key = saveInBlobstore(data);
		byte[] retrieved = null;

		retrieved = loadBytes(key);
		assertNotNull(retrieved);

		service.delete(key);
		retrieved = loadBytes(key);
		assertNull(retrieved);
	}

	private BlobKey saveInBlobstore(byte[] data) throws IOException {
		File targetFile = File.createTempFile("bs-test-object", "jpg", null);
		Files.write(data, targetFile);
		String uploadUrl = service.createUploadUrl("/test-upload");
		  CloseableHttpClient httpclient = HttpClients.createDefault();
	        try {
	        	System.out.println("upload url: " + uploadUrl);
	            HttpPost httppost = new HttpPost(uploadUrl);

	            FileBody bin = new FileBody(targetFile);
	            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

	            HttpEntity reqEntity = MultipartEntityBuilder.create()
	                    .addPart("bin", bin)
	                    .addPart("comment", comment)
	                    .build();


	            httppost.setEntity(reqEntity);

	            System.out.println("executing request " + httppost.getRequestLine());
	            CloseableHttpResponse response = httpclient.execute(httppost);
	            try {
	                System.out.println("----------------------------------------");
	                System.out.println(response.getStatusLine());
	                HttpEntity resEntity = response.getEntity();
	                if (resEntity != null) {
	                    System.out.println("Response content length: " + resEntity.getContentLength());
	                }
	                EntityUtils.consume(resEntity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
		
		return null;
	}

	private byte[] loadBytes(BlobKey key) {
		return service.fetchData(key);
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

	public void testGetInfo() throws Exception {
		BlobKey key = saveInBlobstore(data);
		assertEquals(data.length, service.getInfo(key).getLength());
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
