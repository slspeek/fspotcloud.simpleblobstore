package com.googlecode.simpleblobstore.j2ee;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class BlobUploadServlet extends HttpServlet {

	private Logger log = Logger.getLogger(BlobUploadServlet.class.getName());
	@Inject
	J2eeBlobService blobService;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String successUrl = request.getParameter("next");
		if (ServletFileUpload.isMultipartContent(request)) {
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Configure a repository (to ensure a secure temp location is used)
			ServletContext servletContext = this.getServletConfig()
					.getServletContext();
			File repository = (File) servletContext
					.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			Map<String, List<String>> result = Maps.newHashMap();
			try {
				// Parse the request
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem i : items) {
					if (!i.isFormField()) {
						byte[] data;
						data = IOUtils.toByteArray(i.getInputStream());
						BlobKey key = blobService.save("", data);
						List<String> l = Lists.newArrayList();
						l.add(key.getKeyString());
						result.put(i.getFieldName(), l);
					}
				}
				request.setAttribute(J2eeBlobService.UPLOADS_ATTRIBUTE, result);
				RequestDispatcher dispatcher = request.getRequestDispatcher(successUrl);
				dispatcher.forward(request, response);
			} catch (FileUploadException e) {
				throw new ServletException(e);
			}
		}

	}
}
