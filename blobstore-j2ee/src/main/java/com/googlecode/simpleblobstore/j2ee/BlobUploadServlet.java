package com.googlecode.simpleblobstore.j2ee;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class BlobUploadServlet extends HttpServlet {

	private Logger log = Logger.getLogger(BlobUploadServlet.class.getName());
	@Inject
	BlobService blobService;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("BLOBUPLOADSERVLET");
		Collection<Part> parts = request.getParts();
		for (Part part:parts) {
			log.info("Part name: " + part.getName());
		}
		
	}
}
