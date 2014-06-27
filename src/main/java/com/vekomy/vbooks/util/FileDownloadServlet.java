/**
 * com.vekomy.vbooks.util.FileDownloadServlet.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Sep 7, 2013
 */
package com.vekomy.vbooks.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet is responsible to perform download files.
 * 
 * @author Sudhakar
 * 
 */
public class FileDownloadServlet extends HttpServlet {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(FileDownloadServlet.class);
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -7080152106871511273L;
	/**
	 * Integer variable holds BUFSIZE.
	 */
	private static final Integer BUFSIZE = 4096;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filePath = request.getParameter("filePath");
		File sourceFile = new File(filePath);
		if(sourceFile.exists()) {
			Integer length = 0;
			ServletOutputStream outputStream = response.getOutputStream();
			ServletContext context = getServletConfig().getServletContext();
			// sets response content type
			String mimetype = context.getMimeType(filePath);
			if (mimetype == null) {
				mimetype = "application/octet-stream";
			}
			// sets HTTP header
			response.setContentType(mimetype);
			response.setContentLength((int) sourceFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=\""	+ sourceFile.getName() + "\"");

			byte[] byteBuffer = new byte[BUFSIZE];
			DataInputStream inputStream = new DataInputStream(new FileInputStream(sourceFile));
			
			// reads the file's bytes and writes them to the response stream
			while ((inputStream != null) && ((length = inputStream.read(byteBuffer)) != -1)) {
				outputStream.write(byteBuffer, 0, length);
			}
			inputStream.close();
			outputStream.close();
		} else {
			if(_logger.isErrorEnabled()) {
				_logger.error("{} not available.", filePath);
			}
		}
	}
}
