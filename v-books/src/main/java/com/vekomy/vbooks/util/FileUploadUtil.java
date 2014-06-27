package com.vekomy.vbooks.util;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

@SuppressWarnings("deprecation")
public class FileUploadUtil {

	public static POIFSFileSystem getFile(HttpServletRequest request) throws Exception {
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(100000);
		fu.setSizeThreshold(1024);
		fu.setRepositoryPath(System.getProperty("java.io.tmpdir"));
		List fileList = fu.parseRequest(request);
		InputStream uploadedFileStream = null;
		String uploadedFileName = null; // name of file on user's computer
		for (Iterator i = fileList.iterator(); i.hasNext();) {
			FileItem fi = (FileItem) i.next();
			if (fi.isFormField()) {
				String key = fi.getFieldName();
				String val = fi.getString();
				// out.println("Form parameter " + key + "=" + val + "<br/>");
			} else {
				if (fi.getSize() < 1) {
					throw new Exception("No file was uplaoded");
				}
				// out.print(fi.getSize() + "<br/>");
				// uploadedFileName = fi.getName();
				// out.print(uploadedFileName + "<br/>");
				uploadedFileStream = fi.getInputStream();
				// out.print(uploadedFileStream + "<br/>");
			}
		}

		return new POIFSFileSystem(uploadedFileStream);
	}

	public static String readCell(HSSFCell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: {
			return "" + ((long) cell.getNumericCellValue());
		}
		case HSSFCell.CELL_TYPE_STRING: {
			// cell type string.
			HSSFRichTextString richTextString = cell.getRichStringCellValue();
			return richTextString.getString();
		}
		default: {
			// types other than String and Numeric.
			return "";
		}
		}

	}

	
	
	
}
