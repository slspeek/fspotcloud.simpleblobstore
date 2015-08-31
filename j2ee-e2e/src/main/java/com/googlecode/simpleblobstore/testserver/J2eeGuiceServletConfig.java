/*
 * Copyright 2010-2012 Steven L. Speek.
 * This program is free software; you can redistribute it
                and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free
                Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is
                distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied
                warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public
                License for more details.
 * You should have received a copy of the GNU General Public License
 * along
                with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330,
                Boston, MA 02111-1307, USA.
 *
 */

package com.googlecode.simpleblobstore.testserver;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.simpleblobstore.j2ee.J2eeSimpleBlobstoreModule;
import com.googlecode.simplejpadao.EntityModule;

public class J2eeGuiceServletConfig extends GuiceServletContextListener {
	@Override
	protected Injector getInjector() {
		Injector i = Guice.createInjector(new J2eeSimpleBlobstoreModule(),
				new EntityModule("derby"), new TestServletModule());
		return i;
	}

	private class TestServletModule extends ServletModule {
		@Override
		protected void configureServlets() {
			super.configureServlets();
			Logger.getAnonymousLogger().info("In module.");
			serve("/createurl").with(CreateUrlServlet.class);
			serve("/serve").with(ServeBlobServlet.class);
			serve("/delete").with(DeleteServlet.class);
			serve("/info").with(InfoServlet.class);
		}
	}
}
