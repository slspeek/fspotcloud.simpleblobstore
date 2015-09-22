package com.googlecode.simpleblobstore.j2ee;

import com.google.inject.servlet.ServletModule;

public class J2eeSimpleBlobstoreServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		super.configureServlets();
		serve("/_ah/upload").with(BlobUploadServlet.class);
	}
}
