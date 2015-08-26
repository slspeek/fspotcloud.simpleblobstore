package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Guice;
import com.google.inject.Singleton;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;
import com.googlecode.simpleblobstore.gae.GaeSimpleBlobstoreModule;

@SuppressWarnings("serial")
@Singleton
public class UploadServlet extends HttpServlet {

	private Logger log = Logger.getLogger(UploadServlet.class.getName()); 
    BlobService blobService = Guice.createInjector(new GaeSimpleBlobstoreModule()).getInstance(BlobService.class);
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("UPLOADSERVLET");
        Map<String, List<BlobKey>> uploads = blobService.getUploads(request);
        log.info("Uploads " + uploads);
        OutputStream out = response.getOutputStream();
        PrintWriter p = new PrintWriter(out);
        p.write(uploads.toString());
//        p.write("Foo!");
        p.close();
        out.close();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request, response);
    }

   
}