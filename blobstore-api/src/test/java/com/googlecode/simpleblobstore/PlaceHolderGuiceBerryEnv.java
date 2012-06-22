package com.googlecode.simpleblobstore;

import com.google.guiceberry.GuiceBerryModule;

public class PlaceHolderGuiceBerryEnv extends GuiceBerryModule {

    @Override
    protected void configure() {
        super.configure();
        throw new IllegalStateException("Please override GuiceBerryEnv.");
    }
}
