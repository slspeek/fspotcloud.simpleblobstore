package com.googlecode.simpleblobstore;

import javax.servlet.http.HttpServletRequest;

/**
 * User: steven
 * Date: 18-6-12
 * Time: 20:48
 * 
 */
public interface BlobService {

    void delete(BlobKey key);

    @Deprecated
    BlobKey save(String mimeType, byte[] data);

    BlobInfo getInfo(BlobKey key);

    byte[] fetchData(BlobKey key);
    
    java.util.Map<java.lang.String,java.util.List<BlobKey>> getUploads(HttpServletRequest request);
        
    String createUploadUrl(String successUrl);
}
