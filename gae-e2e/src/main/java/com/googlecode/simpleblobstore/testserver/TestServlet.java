package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class TestServlet extends HttpServlet {

	private Logger log = Logger.getLogger(TestServlet.class.getName());
	@Inject
	BlobService blobService;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("TESTSERVLET");
		OutputStream out = response.getOutputStream();
		PrintWriter p = new PrintWriter(out);
		p.write(outputHTML());
		p.close();
		out.close();
	}

	private String outputHTML() {
		String result = blobService.createUploadUrl("/upload");
		return result;
	}
}
