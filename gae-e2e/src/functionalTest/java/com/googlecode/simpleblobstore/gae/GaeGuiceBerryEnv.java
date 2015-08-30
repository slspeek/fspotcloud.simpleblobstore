package com.googlecode.simpleblobstore.gae;

import com.google.guiceberry.GuiceBerryModule;

public class GaeGuiceBerryEnv extends GuiceBerryModule {

	@Override
	protected void configure() {
		super.configure();
		install(new GaeSimpleBlobstoreModule());
	}
}
