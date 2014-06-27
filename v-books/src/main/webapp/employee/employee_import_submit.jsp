
<%@page import="com.vekomy.vbooks.hibernate.model.VbLogin"%>
<%@page import="com.vekomy.vbooks.employee.command.EmployeeCommand"%>
<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.util.FileUploadUtil"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

<%@page import="com.vekomy.vbooks.employee.command.EmployeeCommand"%>
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
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
				.getSession().getServletContext());
		EmployeeDao EmployeeDao = (EmployeeDao) hibernateContext.getBean("employeeDao");
//		SiteAccountsDao siteAccountsDao = (SiteAccountsDao) hibernateContext .getBean("siteAccountsDao");
		EmployeeCommand vbEmployee = null;
		VbEmployeeDetail vbEmployeeDetail =null;
		VbEmployeeAddress vbEmployeeAddress=null;
		VbLogin vbLogin=null;
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		int mandatoryFields[] = { 0, 1, 2, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 16, 18, 19, 20, 21};
		while (rows.hasNext()) {
			row = (HSSFRow) rows.next();
			if(row.getCell(0)==null||row.getCell(0).toString().length()==0) break;
			for (int i = 0; i < mandatoryFields.length; i++) {
				if (FileUploadUtil.readCell(row.getCell(mandatoryFields[i])) == null || FileUploadUtil.readCell(row.getCell(mandatoryFields[i])).length() <= 0) {
					emptyFound=true;
					break;
				}
			}
			vbEmployee = new EmployeeCommand();
			vbLogin=new VbLogin();
			vbEmployeeDetail = new VbEmployeeDetail();
			vbEmployeeAddress=new VbEmployeeAddress();
			
			vbEmployee.setUsername(FileUploadUtil.readCell(row.getCell(0)).toLowerCase());
			vbEmployee.setPassword(FileUploadUtil.readCell(row.getCell(1)));
			
			
			vbEmployee.setFirstName(FileUploadUtil.readCell(row.getCell(2)));
			vbEmployee.setMiddleName(FileUploadUtil.readCell(row.getCell(3)));
			vbEmployee.setLastName(FileUploadUtil.readCell(row.getCell(4)));
			vbEmployee.setEmployeeType(FileUploadUtil.readCell(row.getCell(5)));
			vbEmployee.setEmployeeNumber(null);
			vbEmployee.setEmployeeEmail(FileUploadUtil.readCell(row.getCell(6)));
			vbEmployee.setGender((FileUploadUtil.readCell(row.getCell(7))).charAt(0));
			
			
			vbEmployeeDetail.setMobile(FileUploadUtil.readCell(row.getCell(8)));
			vbEmployeeDetail.setDirectLine(FileUploadUtil.readCell(row.getCell(10)));
			vbEmployeeDetail.setAlternateMobile(FileUploadUtil.readCell(row.getCell(9)));
			
			vbEmployeeDetail.setBloodGroup(FileUploadUtil.readCell(row.getCell(11)));
			vbEmployeeDetail.setPassportNumber(FileUploadUtil.readCell(row.getCell(13)));
			vbEmployeeDetail.setNationality(FileUploadUtil.readCell(row.getCell(12)));
			
			vbEmployeeAddress.setAddressLine1(FileUploadUtil.readCell(row.getCell(14)));
			vbEmployeeAddress.setAddressLine2(FileUploadUtil.readCell(row.getCell(15)));
			vbEmployeeAddress.setLocality(FileUploadUtil.readCell(row.getCell(16)));
			vbEmployeeAddress.setLandmark(FileUploadUtil.readCell(row.getCell(17)));
			vbEmployeeAddress.setCity(FileUploadUtil.readCell(row.getCell(18)));
			vbEmployeeAddress.setState(FileUploadUtil.readCell(row.getCell(19)));
			vbEmployeeAddress.setZipcode(FileUploadUtil.readCell(row.getCell(20)));
			vbEmployeeAddress.setAddressType(OrganizationUtils.getAddressType(FileUploadUtil.readCell(row.getCell(21))));
			
			EmployeeDao.saveEmployee(vbEmployee, vbEmployeeDetail,vbEmployeeAddress, user.getName(), user.getOrganization());

		}
		out.println(rows.toString().length()+"records are updated");
		response.sendRedirect("../index.jsp?module=employee");
	} catch (Exception e) {
		out.print(e);
		e.printStackTrace();
	}
%>
