package com.googlecode.simpleblobstore;

import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.TestWrapper;
import com.googlecode.simpleblobstore.gae.GaeSimpleBlobstoreModule;

public class GaeGuiceBerryEnv extends GuiceBerryModule {

    @Override
    protected void configure() {
        super.configure();
        install(new GaeSimpleBlobstoreModule());
        bind(TestWrapper.class).to(GaeLocalDatastoreHelperTestWrapper.class);
    }
}
