package com.googlecode.simpleblobstore;

import com.google.guiceberry.GuiceBerryModule;
import com.googlecode.simplejpadao.EntityModule;

public class J2eeGuiceBerryEnv extends GuiceBerryModule {

    @Override
    protected void configure() {
        super.configure();
        install(new J2eeSimpleBlobstoreModule());
        install(new EntityModule("derby"));
    }
}
