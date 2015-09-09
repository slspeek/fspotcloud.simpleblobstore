package com.googlecode.simpleblobstore.testserver;

import com.google.inject.servlet.ServletModule;
import com.googlecode.simpleblobstore.DefaultCreateUrlServlet;

public class TestAppModule extends ServletModule {
		@Override
		protected void configureServlets() {
			super.configureServlets();
			serve("/createurl").with(CreateUrlServlet.class);
			serve("/serve").with(ServeBlobServlet.class);
			serve("/delete").with(DeleteServlet.class);
			serve("/info").with(InfoServlet.class);
			serve("/defaultcreateurl").with(DefaultCreateUrlServlet.class);
		}
	}
