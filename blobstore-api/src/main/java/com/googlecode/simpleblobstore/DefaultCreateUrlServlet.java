package com.googlecode.simpleblobstore;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class DefaultCreateUrlServlet extends HttpServlet {

	@Inject
	BlobService blobService;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String postUploadUrl = request.getParameter("success");
		String uploadUrl = blobService.createUploadUrl(postUploadUrl);
		OutputStream out = response.getOutputStream();
		PrintWriter p = new PrintWriter(out);
		p.write(uploadUrl);
		p.close();
		out.close();
	}
}