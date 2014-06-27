<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.util.FileUploadUtil"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

<%@page import="org.apache.poi.hssf.usermodel.HSSFRichTextString"%>
<%@page import="org.apache.poi.ss.usermodel.Cell"%>
<%@page import="org.apache.poi.ss.usermodel.Row"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.List"%>
<%
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		out.println(request.getParameter("file"));
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		CustomerDao CustomerDao = (CustomerDao) hibernateContext.getBean("customerDao");
		VbCustomer customer = null;
		VbCustomerDetail customerDetail =null;
		CustomerCommand customerCommand=null;
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		int mandatoryFields[] = { 0, 1, 2, 3, 4,12,14};
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			if(row.getCell(0)==null||row.getCell(0).toString().length()==0) break;
			for (int i = 0; i < mandatoryFields.length; i++) {
				if (FileUploadUtil.readCell(row.getCell(mandatoryFields[i])) == null || FileUploadUtil.readCell(row.getCell(mandatoryFields[i])).length() <= 0) {
					emptyFound=true;
					break;
				}
			}
			customerCommand = new CustomerCommand();
			customerDetail = new VbCustomerDetail();
		

			customerCommand.setBusinessName(FileUploadUtil.readCell(row.getCell(0)));
			customerCommand.setInvoiceName(FileUploadUtil.readCell(row.getCell(1)));
			customerCommand.setCustomerName(FileUploadUtil.readCell(row.getCell(2)));
			customerCommand.setGender((FileUploadUtil.readCell(row.getCell(3))).charAt(0));
			customerCommand.setMobile(FileUploadUtil.readCell(row.getCell(4)));
			customerDetail.setEmail(FileUploadUtil.readCell(row.getCell(5))); 
			if(FileUploadUtil.readCell((row.getCell(6))).equals("")){
				customerCommand.setCreditLimit(null);
			}else{
				customerCommand.setCreditLimit(Float.parseFloat(FileUploadUtil.readCell((row.getCell(6)))));
			}
			if(FileUploadUtil.readCell(row.getCell(7)).equals("")){
				customerCommand.setCreditOverdueDays(null);
			}else{
				customerCommand.setCreditOverdueDays(Integer.parseInt(FileUploadUtil.readCell(row.getCell(7))));
			}
			customerDetail.setDirectLine(FileUploadUtil.readCell(row.getCell(8)));
			customerCommand.setAlternateMobile(FileUploadUtil.readCell(row.getCell(9)));
			customerDetail.setAddressLine1(FileUploadUtil.readCell(row.getCell(10)));
			customerDetail.setAddressLine2(FileUploadUtil.readCell(row.getCell(11)));
			customerDetail.setLocality(FileUploadUtil.readCell(row.getCell(12)));
			customerDetail.setLandmark(FileUploadUtil.readCell(row.getCell(13)));
			customerDetail.setRegion(FileUploadUtil.readCell(row.getCell(14)));
			customerDetail.setCity(FileUploadUtil.readCell(row.getCell(15)));
			customerDetail.setState(FileUploadUtil.readCell(row.getCell(16)));
			customerDetail.setZipcode(FileUploadUtil.readCell(row.getCell(17)));
			CustomerDao.saveCustomer(customerCommand, customerDetail, user.getOrganization(), user.getName());
		}
		out.println(rows.toString().length()+"records are updated");
		response.sendRedirect("../index.jsp?module=customer");
	} catch (Exception e) {
		out.print(e);
		e.printStackTrace();
	}
%>
