package com.googlecode.simpleblobstore;

public class BlobKey {

    private final String keyString;

    public String getKeyString() {
        return keyString;
    }

    public BlobKey(String keyString) {
        this.keyString = keyString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlobKey)) return false;

        BlobKey blobKey = (BlobKey) o;

        if (keyString != null ? !keyString.equals(blobKey.keyString) : blobKey.keyString != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return keyString != null ? keyString.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("BlobKey");
        sb.append("{keyString='").append(keyString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
