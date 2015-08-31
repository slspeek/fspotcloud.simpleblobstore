package com.googlecode.simpleblobstore.j2ee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Provider;
import com.googlecode.simpleblobstore.BlobInfo;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

public class J2eeBlobService implements BlobService {

	@javax.inject.Inject
    @ServerAddress
    Provider<String> serverAddressProvider;

	
    @Inject
    BlobDao blobDao;

    @Override
    public void delete(BlobKey key) {
        blobDao.deleteByKey(Long.valueOf(key.getKeyString()));
    }

    @Override
    public BlobKey save(String mimeType, byte[] data) {
        Blob blobRecord = blobDao.newEntity();
        blobRecord.setData(data);
        blobRecord.setMimeType(mimeType);
        blobDao.save(blobRecord);
        BlobKey key = new BlobKey(String.valueOf(blobRecord.getId()));
        return key;
    }

    @Override
    public BlobInfo getInfo(BlobKey key) {
        Blob record = blobDao.find(Long.valueOf(key.getKeyString()));
        BlobInfo info = null;
        if (record != null) {
            info = new BlobInfo(record.getMimeType(), record.getSize());
        }
        return info;
    }

    	@Override
	public Map<String, List<BlobKey>> getUploads(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createUploadUrl(String successUrl) {
		// TODO Auto-generated method stub
		return serverAddressProvider.get() + "/_ah/upload";
	}

	@Override
	public void serve(BlobKey blobKey, HttpServletResponse response)
			throws IOException {
	    Blob record = blobDao.find(Long.valueOf(blobKey.getKeyString()));
        byte[] data = null;
        if (record != null) {
            data = record.getData();
        }
		// TODO Auto-generated method stub
	}
}
