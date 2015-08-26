package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class BlobServlet extends HttpServlet {
	 private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	    @Override
	    public void doGet(HttpServletRequest req, HttpServletResponse res)
	        throws IOException {
	            BlobKey blobKey = new BlobKey(req.getParameter("id"));
	            blobstoreService.serve(blobKey, res);
	        }
}
