package com.googlecode.simpleblobstore.gae;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.simpleblobstore.BlobInfo;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

public class GaeBlobService implements BlobService {

	public static final int MILLION = 1000000;
	@Inject
	BlobstoreService gaeBlobService;
	@Inject
	BlobInfoFactory infoFactory;
	
	@Override
	public void delete(BlobKey blobKey) {
		com.google.appengine.api.blobstore.BlobKey appengineKey = getGaeBlobKey(blobKey);
		gaeBlobService.delete(appengineKey);
	}

	private com.google.appengine.api.blobstore.BlobKey getGaeBlobKey(BlobKey key) {
		return new com.google.appengine.api.blobstore.BlobKey(
				key.getKeyString());
	}

	private BlobKey getBlobKey(com.google.appengine.api.blobstore.BlobKey key) {
		if (key != null) {
			return new BlobKey(key.getKeyString());
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public BlobInfo getInfo(BlobKey key) {
		com.google.appengine.api.blobstore.BlobInfo gaeInfo = infoFactory
				.loadBlobInfo(getGaeBlobKey(key));
		if (gaeInfo != null) {
			BlobInfo info = new BlobInfo(gaeInfo.getContentType(),
					gaeInfo.getSize());
			return info;
		} else {
			return null;
		}
	}

	@Override
	public Map<String, List<BlobKey>> getUploads(HttpServletRequest request) {
		Map<String, List<com.google.appengine.api.blobstore.BlobKey>> gaeResult = null;
		try {
			gaeResult = gaeBlobService.getUploads(request);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		Map<String, List<BlobKey>> result = Maps.newHashMap();
		if (gaeResult != null) {

			for (String key : gaeResult.keySet()) {
				List<BlobKey> blobs = Lists.newArrayList();
				for (com.google.appengine.api.blobstore.BlobKey bk : gaeResult
						.get(key)) {
					blobs.add(getBlobKey(bk));
				}
				result.put(key, blobs);

			}
		}
		return result;
	}

	@Override
	public String createUploadUrl(String successUrl) {
		return gaeBlobService.createUploadUrl(successUrl);
	}

	@Override
	public void serve(BlobKey blobKey, HttpServletResponse response)
			throws IOException {
		gaeBlobService.serve(getGaeBlobKey(blobKey), response);
	}
}
