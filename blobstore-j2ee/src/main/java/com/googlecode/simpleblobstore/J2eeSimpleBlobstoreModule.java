package com.googlecode.simpleblobstore;

import com.google.inject.AbstractModule;

public class J2eeSimpleBlobstoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BlobDao.class).to(BlobDaoImpl.class);
        bind(BlobService.class).to(J2eeBlobService.class);
    }
}
