package com.googlecode.simpleblobstore.testserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Singleton;
import com.googlecode.simpleblobstore.BlobKey;
import com.googlecode.simpleblobstore.BlobService;

@SuppressWarnings("serial")
@Singleton
public class J2eeAfterUploadServlet extends HttpServlet {

	private Logger log = Logger.getLogger(J2eeAfterUploadServlet.class.getName()); 
    @Inject BlobService blobService;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	log.info("UPLOADSERVLET");
//    	Enumeration headerNames = request.getHeaderNames();
//    	while(headerNames.hasMoreElements()) {
//    	  String headerName = (String)headerNames.nextElement();
//    	  System.out.println(headerName + " : " + request.getHeader(headerName));
//    	}
//    	
        Map<String, List<BlobKey>> uploads = blobService.getUploads(request);
        log.info("Uploads " + uploads);
        OutputStream out = response.getOutputStream();
        PrintWriter p = new PrintWriter(out);
        List<BlobKey> keyList = uploads.get("bin");
        BlobKey key = keyList.get(0);
        p.write(key.getKeyString());
        p.close();
        out.close();
    }
}