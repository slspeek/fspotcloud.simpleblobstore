package com.googlecode.simpleblobstore.j2ee;

import com.google.inject.AbstractModule;
import com.googlecode.simpleblobstore.BlobService;

public class J2eeSimpleBlobstoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BlobDao.class).to(BlobDaoImpl.class);
        bind(BlobService.class).to(J2eeBlobService.class);
    }
}
