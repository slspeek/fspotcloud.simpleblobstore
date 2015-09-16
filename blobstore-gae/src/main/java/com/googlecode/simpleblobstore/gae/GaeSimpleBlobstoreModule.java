package com.googlecode.simpleblobstore.gae;

import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.inject.AbstractModule;
import com.googlecode.simpleblobstore.BlobService;

public class GaeSimpleBlobstoreModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(BlobstoreService.class).toInstance(
				BlobstoreServiceFactory.getBlobstoreService());
		bind(BlobInfoFactory.class).toInstance(new BlobInfoFactory());
		bind(BlobService.class).to(GaeBlobService.class);
	}
}
