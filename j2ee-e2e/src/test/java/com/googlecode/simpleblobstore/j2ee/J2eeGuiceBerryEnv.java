package com.googlecode.simpleblobstore.j2ee;

import com.google.guiceberry.GuiceBerryModule;

public class J2eeGuiceBerryEnv extends GuiceBerryModule {

    @Override
    protected void configure() {
        super.configure();
        bind(String.class).toInstance("http://localhost:9050/j2ee-e2e/");
    }
}
