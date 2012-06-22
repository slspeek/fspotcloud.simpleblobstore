package com.googlecode.simpleblobstore.j2ee;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class BlobEntity implements Blob {

    @Id
    @GeneratedValue(generator = "increment")
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //Derby with Hibernate do not want this, I know
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Lob
    private byte[] data;
    @Basic
    private String mimeType;
    @Basic
    private long size;

    public long getSize() {
        return size;
    }

    public BlobEntity() {
    }

    public BlobEntity(byte[] data, String mimeType) {
        this.data = data;
        this.mimeType = mimeType;
        this.size = data.length;
    }

    @Override
    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("BlobEntity");
        sb.append("{id=").append(id);
        sb.append(", mimeType='").append(mimeType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
