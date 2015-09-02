package com.googlecode.simpleblobstore.j2ee;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Provider;
import com.googlecode.simpleblobstore.BlobInfo;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

public class J2eeBlobService implements BlobService {

	public static final String UPLOADS_ATTRIBUTE = "com.googlecode.simpleblobstore";
	private final Logger log = Logger.getLogger(J2eeBlobService.class.getName());
	@javax.inject.Inject
    @ServerAddress
    Provider<String> serverAddressProvider;
	
    @Inject
    BlobDao blobDao;

    @Override
    public void delete(BlobKey key) {
        blobDao.deleteByKey(Long.valueOf(key.getKeyString()));
    }

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
		@SuppressWarnings("unchecked")
		Map<String, List<String>> uploads = (Map<String, List<String>>) request.getAttribute(UPLOADS_ATTRIBUTE);
		Map<String, List<BlobKey>> result = Maps.newHashMap();
		for (Map.Entry<String, List<String>> entry : uploads.entrySet()) {
			List<BlobKey> keys = Lists.newArrayList();
			for (String key: entry.getValue()) {
				keys.add(new BlobKey(key));
			}
			result.put(entry.getKey(), keys);
 		}
 		return result;
	}

	@Override
	public String createUploadUrl(String successUrl) {
		try {
			successUrl =  URLEncoder.encode(successUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = serverAddressProvider.get() + "/_ah/upload?next=" +successUrl;
		return url;
	}

	@Override
	public void serve(BlobKey blobKey, HttpServletResponse response)
			throws IOException {
	    Blob record = blobDao.find(Long.valueOf(blobKey.getKeyString()));
        byte[] data = null;
        if (record != null) {
            data = record.getData();
            response.setContentType(record.getMimeType());
            response.getOutputStream().write(data);
        } else {
        	response.sendError(404, "blob not found");
        }
	}
}
