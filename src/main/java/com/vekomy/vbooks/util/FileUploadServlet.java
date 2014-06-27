/**
 * ccom.vekomy.vbooks.util.FileUploadServlet.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Aug 17, 2013
 */
package com.vekomy.vbooks.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This class is responsible to upload the customer documents.
 * 
 * @author Swarupa
 *
 */
public class FileUploadServlet extends HttpServlet {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = -3208409086358916855L;
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(FileUploadServlet.class);
	/**
	 * String variable holds BUSINESS_NAME.
	 */
	private static final String BUSINESS_NAME = "businessName";
	/**
	 * String variable holds ORGANIZATION_ID.
	 */
	private static final String ORGANIZATION_ID = "organizationId";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> fileItems = upload.parseRequest(request);
				String businessName = null;
				String organizationName = null;
				String fieldName = null;
				String fileName = null;
				String targetDirectory = null;
				File uploadedFile = null;
				for (FileItem item : fileItems) {
					if(item.isFormField()) {
						fieldName = item.getFieldName();
						if(BUSINESS_NAME.equalsIgnoreCase(fieldName)) {
							businessName = item.getString();
						} else if(ORGANIZATION_ID.equalsIgnoreCase(fieldName)){
							organizationName = item.getString();
						}
					} else {
						fileName = item.getName();
						targetDirectory = new StringBuffer(DirectoryUtil.getInstance().getDestinationPath(organizationName, businessName))
									.append(File.separator).append(fileName).toString();
						uploadedFile = new File(targetDirectory);
						item.write(uploadedFile);
					}
				}
				response.sendRedirect("index.jsp?module=customer");
			} catch (FileUploadException exception) {
				if(_logger.isErrorEnabled()) {
					_logger.error(Msg.get(MsgEnum.UPLOAD_FAILURE_MESSAGE));
				}
			} catch (Exception exception) {
				if(_logger.isErrorEnabled()) {
					_logger.error(Msg.get(MsgEnum.UPLOAD_FAILURE_MESSAGE));
				}
			}
		}
	}
}