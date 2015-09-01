package com.googlecode.simpleblobstore.j2ee;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class BlobEntity implements Blob {

    @Id
    @GeneratedValue(generator = "increment")
    //@GeneratedValue(strategy = GenerationType.IDENTITY) //Derby with Hibernate does not want this, I know
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Lob
    private byte[] data;
    @Basic
    private String mimeType;
   
    public long getSize() {
        return data.length;
    }

    public BlobEntity() {
    }

    public BlobEntity(byte[] data, String mimeType) {
        this.data = data;
        this.mimeType = mimeType;
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
        return new ToStringBuilder(this).append("id", id)
        		.append("size", getSize()).append("mime", mimeType).toString();
    }
}
