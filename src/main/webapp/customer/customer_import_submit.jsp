<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="java.util.ArrayList"%>
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
List<Integer> faultRows=new ArrayList<Integer>();
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		FileUploadUtil.createEvaluator(workBook);
		out.println(request.getParameter("file"));
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		CustomerDao customerDao = (CustomerDao) hibernateContext.getBean("customerDao");
		VbCustomer customer = null;
		VbCustomerDetail customerDetail =null;
		CustomerCommand customerCommand=null;
		String businessName = null;
		VbOrganization organization = user.getOrganization();
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		int mandatoryFields[] = {1,2,3,4,5,13,15};
		int customerFields[] ={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
		while (rows.hasNext()) {
			Boolean isRowWrongValidated=Boolean.FALSE;
			row = (HSSFRow) rows.next();
			if(row.getPhysicalNumberOfCells() > 1){
				
				if(row.getRowNum()>=0 && row.getRowNum()<=4)
					continue;
				for (int i = 0; i < customerFields.length; i++) {
					Boolean isValidate=FileUploadUtil.validateCustomerXls(row.getRowNum(),row.getCell(customerFields[i]),customerFields[i]);
					if(isValidate){
						isRowWrongValidated = Boolean.TRUE;
					}
				}
				if(isRowWrongValidated){
					faultRows.add(row.getRowNum()+1);
				}else{
					businessName = FileUploadUtil.readCell(row.getCell(1));
					Boolean isExists = customerDao.isBusinessNameExists(businessName, organization);
					if(!isExists) {
						customerCommand = new CustomerCommand();
						customerDetail = new VbCustomerDetail();
					

						customerCommand.setBusinessName(businessName);
						customerCommand.setInvoiceName(FileUploadUtil.readCell(row.getCell(2)));
						customerCommand.setCustomerName(FileUploadUtil.readCell(row.getCell(3)));
						customerCommand.setGender((FileUploadUtil.readCell(row.getCell(4))).charAt(0));
						customerCommand.setMobile(FileUploadUtil.readCell(row.getCell(5)));
						customerDetail.setEmail(FileUploadUtil.readCell(row.getCell(6))); 
						if(FileUploadUtil.readCell((row.getCell(7))).equals("")){
							customerCommand.setCreditLimit(null);
						}else{
							customerCommand.setCreditLimit(Float.parseFloat(FileUploadUtil.readCell((row.getCell(7)))));
						}
						if(FileUploadUtil.readCell(row.getCell(8)).equals("")){
							customerCommand.setCreditOverdueDays(null);
						}else{
							customerCommand.setCreditOverdueDays(Integer.parseInt(FileUploadUtil.readCell(row.getCell(8))));
						}
						customerDetail.setDirectLine(FileUploadUtil.readCell(row.getCell(9)));
						customerCommand.setAlternateMobile(FileUploadUtil.readCell(row.getCell(10)));
						customerDetail.setAddressLine1(FileUploadUtil.readCell(row.getCell(11)));
						customerDetail.setAddressLine2(FileUploadUtil.readCell(row.getCell(12)));
						customerDetail.setLocality(FileUploadUtil.readCell(row.getCell(13)));
						customerDetail.setLandmark(FileUploadUtil.readCell(row.getCell(14)));
						customerDetail.setRegion(FileUploadUtil.readCell(row.getCell(15)));
						customerDetail.setCity(FileUploadUtil.readCell(row.getCell(16)));
						customerDetail.setState(FileUploadUtil.readCell(row.getCell(17)));
						customerDetail.setZipcode(FileUploadUtil.readCell(row.getCell(18)));
						customerDao.saveCustomer(customerCommand, customerDetail, organization, user.getName());
					}
				}
				
			}
			
		}
		if(faultRows.isEmpty()){
			response.sendRedirect("../index.jsp?module=customer");
		}else{
			HttpSession importsession = request.getSession();
			String faultRowNumbers="";
			for(int i=0;i<faultRows.size();i++){
				if(faultRowNumbers.equals("")){
					faultRowNumbers +=faultRows.get(i);
				}else{
					faultRowNumbers +=","+faultRows.get(i);	
				}
			}
				importsession.setAttribute("failureMsg","Invalid entries found in the following excel rows:<BR>"+faultRowNumbers);
				response.sendRedirect("../index.jsp?module=customer&pageLink=customer-import");
		}
		
	} catch (Exception e) {
		out.print(e);
		e.printStackTrace();
		HttpSession importsession = request.getSession();
		importsession.setAttribute("failureMsg", "Errors found while processing the Import functionality, Please provide the file with  valid content!");
		response.sendRedirect("../index.jsp?module=customer&pageLink=customer-import");
	}
%>
