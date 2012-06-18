package com.googlecode.simpleblobstore;

/**
 * Created with IntelliJ IDEA.
 * User: steven
 * Date: 18-6-12
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public class BlobInfo {

    private final String mimeType;
    private final long length;

    public BlobInfo(String mimeType, long length) {
        this.mimeType = mimeType;
        this.length = length;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getLength() {
        return length;
    }
}
