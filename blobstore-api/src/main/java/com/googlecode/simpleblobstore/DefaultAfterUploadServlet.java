package com.googlecode.simpleblobstore;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SerializationUtils;

import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class DefaultAfterUploadServlet extends HttpServlet {

	@Inject
	BlobService blobService;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, List<BlobKey>> uploads = blobService.getUploads(request);
		OutputStream out = response.getOutputStream();
		SerializationUtils.serialize((Serializable) uploads, out);
		out.close();
	}
}