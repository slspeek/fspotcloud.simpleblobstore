package com.googlecode.simpleblobstore.j2ee;

import com.google.inject.servlet.ServletModule;
import com.googlecode.simpleblobstore.BlobService;

public class J2eeSimpleBlobstoreModule extends ServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		serve("/_ah/upload").with(BlobUploadServlet.class);
		bind(BlobDao.class).to(BlobDaoImpl.class);
		bind(BlobService.class).to(J2eeBlobService.class);
		bind(String.class).annotatedWith(ServerAddress.class).toProvider(ServerAddressProvider.class);
	}
}
