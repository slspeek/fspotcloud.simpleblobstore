package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class DeleteServlet extends HttpServlet {

	private Logger log = Logger.getLogger(AfterUploadServlet.class.getName());
	@Inject
	BlobService blobService;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("DELETESERVLET");
		BlobKey blobKey = new BlobKey(request.getParameter("id"));
		blobService.delete(blobKey);
	}
}
