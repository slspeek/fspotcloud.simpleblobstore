package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.simpleblobstore.BlobInfo;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class InfoServlet extends HttpServlet {

	private Logger log = Logger.getLogger(InfoServlet.class.getName());
	@Inject
	BlobService blobService;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("INFOSERVLET");
		BlobKey blobKey = new BlobKey(request.getParameter("id"));
		BlobInfo info = blobService.getInfo(blobKey);
		String output = String.valueOf(info.getLength()) + ":" + info.getMimeType();
		OutputStream out = response.getOutputStream();
		PrintWriter p = new PrintWriter(out);
		p.write(output);
		p.close();
		out.close();
	}
}
