package com.googlecode.simpleblobstore.gae;


import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.simpleblobstore.BlobInfo;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class GaeBlobService implements BlobService {

    public static final int MILLION = 1000000;
    @Inject
    BlobstoreService gaeBlobService;
    @Inject
    BlobInfoFactory infoFactory;
    @Inject
    FileService fileService;

    @Override
    public void delete(BlobKey blobKey) {
        com.google.appengine.api.blobstore.BlobKey appengineKey = getGaeBlobKey(blobKey);
        gaeBlobService.delete(appengineKey);
    }

    private com.google.appengine.api.blobstore.BlobKey getGaeBlobKey(BlobKey key) {
        return new com.google.appengine.api.blobstore.BlobKey(key.getKeyString());
    }

    private BlobKey getBlobKey(com.google.appengine.api.blobstore.BlobKey key) {
        return new BlobKey(key.getKeyString());
    }
    @Override
    public BlobKey save(String mimeType, byte[] data) {

        try {
            AppEngineFile file = fileService.createNewBlobFile(mimeType);
            // Open a channel to write to it
            boolean lock = true;
            FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
            ByteBuffer buf = ByteBuffer.wrap(data);
            writeChannel.write(buf);
            writeChannel.closeFinally();

            com.google.appengine.api.blobstore.BlobKey blobKey = fileService.getBlobKey(file);
            return new BlobKey(blobKey.getKeyString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BlobInfo getInfo(BlobKey key) {
        com.google.appengine.api.blobstore.BlobInfo gaeInfo = infoFactory.loadBlobInfo(getGaeBlobKey(key));
        if (gaeInfo != null) {
            BlobInfo info = new BlobInfo(gaeInfo.getContentType(), gaeInfo.getSize());
            return info;
        } else {
            return null;
        }
    }

    @Override
    public byte[] fetchData(BlobKey key) {
        BlobInfo info = getInfo(key);
        if (info != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            long length = info.getLength();
            long offset = 0;
            long remainingLength = length;
            while (remainingLength > 0) {
                long numberOfBytesWeWillRead = Math.min(remainingLength, MILLION);
                byte[] result = gaeBlobService.fetchData(getGaeBlobKey(key), offset, offset + numberOfBytesWeWillRead - 1);
                try {
                    outputStream.write(result);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                remainingLength -= numberOfBytesWeWillRead;
                offset += numberOfBytesWeWillRead;
            }
            return outputStream.toByteArray();
        } else {
            return null;
        }
    }

	@Override
	public Map<String, List<BlobKey>> getUploads(HttpServletRequest request) {
		Map<String, List<com.google.appengine.api.blobstore.BlobKey>> gaeResult = gaeBlobService.getUploads(request);
		Map<String, List<BlobKey>> result = Maps.newHashMap();
		for (String key: gaeResult.keySet()) {
			List<BlobKey> blobs = Lists.newArrayList();
			for (com.google.appengine.api.blobstore.BlobKey bk: gaeResult.get(key)) {
				blobs.add(getBlobKey(bk));
			}
			result.put(key,blobs);
			
		}
		return result;
	}

	@Override
	public String createUploadUrl(String successUrl) {
		return gaeBlobService.createUploadUrl(successUrl);
	}
}
