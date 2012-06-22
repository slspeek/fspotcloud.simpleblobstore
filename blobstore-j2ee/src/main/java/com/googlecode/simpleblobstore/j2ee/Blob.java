package com.googlecode.simpleblobstore.j2ee;

import com.googlecode.simplejpadao.HasKey;

public interface Blob extends HasKey<Long> {

    byte[] getData();

    void setData(byte[] data);

    String getMimeType();

    void setMimeType(String mimeType);

    long getSize();

}
