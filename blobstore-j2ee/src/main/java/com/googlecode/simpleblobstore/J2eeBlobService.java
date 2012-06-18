package com.googlecode.simpleblobstore;

import javax.inject.Inject;

public class J2eeBlobService implements BlobService {


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
    public byte[] fetchData(BlobKey key) {
        Blob record = blobDao.find(Long.valueOf(key.getKeyString()));
        byte[] data = null;
        if (record != null) {
            data = record.getData();
        }
        return data;
    }
}
