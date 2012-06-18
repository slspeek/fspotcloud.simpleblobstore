package com.googlecode.simpleblobstore;

/**
 * Created with IntelliJ IDEA.
 * User: steven
 * Date: 18-6-12
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
public interface BlobService {

    void delete(BlobKey key);

    BlobKey save(String mimeType, byte[] data);

    BlobInfo getInfo(BlobKey key);

    byte[] fetchData(BlobKey key);

}
