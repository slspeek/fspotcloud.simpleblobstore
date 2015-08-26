package com.googlecode.simpleblobstore;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;

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

	final private byte[] data;

	public BlobServiceTest(byte[] data) {
		this.data = data;
	}

	private BlobKey saveInBlobstore(byte[] data) throws IOException {
		File targetFile = File.createTempFile("bs-test-object", ".jpg", null);
		Files.write(data, targetFile);
		String uploadUrl = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet("http://localhost:8080/test");
			CloseableHttpResponse response = httpclient.execute(httpGet);
			try {
				System.out.println("---------------GET-------------------");
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

			System.out.println("upload url: " + uploadUrl);
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
					String r = EntityUtils.toString(resEntity);
					System.out.println("Response content length: "
							+ resEntity.getContentLength() + " " + r);
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

	@Test
	public void testSaveAndLoad() throws Exception {
		// Thread.sleep(10000);
		BlobKey key = saveInBlobstore(data);
		// byte[] retrieved = null;
		//
		// retrieved = loadBytes(key);
		// assertNotNull(retrieved);
		// assertEquals(data.length, retrieved.length);
		// assertTrue(Arrays.equals(data, retrieved));
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
