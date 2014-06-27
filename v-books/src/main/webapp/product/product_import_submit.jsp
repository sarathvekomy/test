<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductDao"%>
<%@page import="com.vekomy.vbooks.util.FileUploadUtil"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProduct"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRichTextString"%>
<%@page import="org.apache.poi.ss.usermodel.Cell"%>
<%@page import="org.apache.poi.ss.usermodel.Row"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.List"%>
<%
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ProductDao ProductDao = (ProductDao) hibernateContext.getBean("productDao");
		ProductCommand productCommand = null;
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		int mandatoryFields[] = { 0, 1,2,4};
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			if(row.getCell(0)==null||row.getCell(0).toString().length()==0) break;
			for (int i = 0; i < mandatoryFields.length; i++) {
				if (FileUploadUtil.readCell(row.getCell(mandatoryFields[i])) == null || FileUploadUtil.readCell(row.getCell(mandatoryFields[i])).length() <= 0) {
					emptyFound=true;
					break;
				}
			}
			productCommand = new ProductCommand();
			productCommand.setProductCategory(FileUploadUtil.readCell(row.getCell(0)));
			String brand=FileUploadUtil.readCell(row.getCell(1));
			String model=FileUploadUtil.readCell(row.getCell(2));
			productCommand.setBrand(brand);
			productCommand.setModel(model);
			productCommand.setProductName(brand+ " "+model); 
			productCommand.setBatchNumber(FileUploadUtil.readCell(row.getCell(3)));
			productCommand.setDescription(FileUploadUtil.readCell(row.getCell(4)));
			productCommand.setCostPerQuantity(Float.parseFloat(FileUploadUtil.readCell(row.getCell(5))));
			ProductDao.saveProduct(productCommand, user.getName(), user.getOrganization());
		}
		response.sendRedirect("../index.jsp?module=product");
	} catch (Exception e) {
		out.print(e);
		e.printStackTrace();
	}
%>
