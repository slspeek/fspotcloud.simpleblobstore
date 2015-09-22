package com.googlecode.simpleblobstore.client;

import java.util.List;
import java.util.Map;

import com.googlecode.simpleblobstore.BlobKey;

public interface BlobstoreClient {

	Map<String, List<BlobKey>> upload(Map<String, byte[]> dataMap)
			throws BlobstoreClientException;

}