package com.googlecode.simpleblobstore.gae;

import com.google.guiceberry.GuiceBerryModule;
import com.google.guiceberry.TestWrapper;

public class GaeGuiceBerryEnv extends GuiceBerryModule {

    @Override
    protected void configure() {
        super.configure();
        install(new GaeSimpleBlobstoreModule());
        bind(TestWrapper.class).to(GaeLocalBlobstoreHelperTestWrapper.class);
    }
}
