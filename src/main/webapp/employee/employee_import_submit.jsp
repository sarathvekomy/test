
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbAddressTypes"%>
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
List<Integer> faultRows=new ArrayList<Integer>();
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		FileUploadUtil.createEvaluator(workBook);
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
				.getSession().getServletContext());
		EmployeeDao employeeDao = (EmployeeDao) hibernateContext.getBean("employeeDao");
//		SiteAccountsDao siteAccountsDao = (SiteAccountsDao) hibernateContext .getBean("siteAccountsDao");
		EmployeeCommand vbEmployee = null;
		VbEmployeeDetail vbEmployeeDetail =null;
		VbEmployeeAddress vbEmployeeAddress=null;
		VbLogin vbLogin=null;
		Integer grantedDays=null;
		String userName = null;
		VbOrganization organization = user.getOrganization();
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		List<String> addressTypesList=employeeDao.getAddressTypes(organization);
		if(! addressTypesList.isEmpty()){
			FileUploadUtil.readAddressTypes(addressTypesList);
			int mandatoryFields[] = {1,2,3,5,6,7,8,9,12,13,14,15,17,19,20,21,22};
			while (rows.hasNext()) {
				Boolean isRowWrongValidated=Boolean.FALSE;
				row = (HSSFRow) rows.next();
				if(row.getPhysicalNumberOfCells() > 1){
					//Not to consider first 5 rows of a sheet
					if(row.getRowNum()>=0 && row.getRowNum()<=4)
						continue;
					for (int i = 0; i < mandatoryFields.length; i++) {
						Boolean isValidate=FileUploadUtil.validateEmployeeXls(row.getRowNum(),row.getCell(mandatoryFields[i]),mandatoryFields[i]);
						if(isValidate){
							isRowWrongValidated = Boolean.TRUE;
						}
					}
					//Checking condition if validating a row is failed
					if(isRowWrongValidated){
						faultRows.add(row.getRowNum()+1);
					}
					else{
						userName = new StringBuffer(organization.getUsernamePrefix()).append(".")
								.append(FileUploadUtil.readCell(row.getCell(1)).toLowerCase()).toString();
						Boolean isExist = employeeDao.isEmployeeExist(userName, organization);
						if(!isExist) {
							vbEmployee = new EmployeeCommand();
							vbEmployeeDetail = new VbEmployeeDetail();
							vbEmployeeAddress=new VbEmployeeAddress();
							
							vbEmployee.setUsername(FileUploadUtil.readCell(row.getCell(1)).toLowerCase());
							vbEmployee.setPassword(FileUploadUtil.readCell(row.getCell(2)));
							vbEmployee.setOrgPrefix(organization.getUsernamePrefix().concat("."));
							vbEmployee.setFirstName(FileUploadUtil.readCell(row.getCell(3)));
							vbEmployee.setMiddleName(FileUploadUtil.readCell(row.getCell(4)));
							vbEmployee.setLastName(FileUploadUtil.readCell(row.getCell(5)));
							String employeeType=FileUploadUtil.readCell(row.getCell(6));
							vbEmployee.setEmployeeType(employeeType);
							if(employeeType.equalsIgnoreCase("SLE")){
								grantedDays=new Integer(2);
							}
							vbEmployee.setGrantedDays(grantedDays);
							vbEmployee.setEmployeeNumber(null);
							vbEmployee.setEmployeeEmail(FileUploadUtil.readCell(row.getCell(7)));
							vbEmployee.setGender((FileUploadUtil.readCell(row.getCell(8))).charAt(0));
							
							
							vbEmployeeDetail.setMobile(FileUploadUtil.readCell(row.getCell(9)));
							vbEmployeeDetail.setDirectLine(FileUploadUtil.readCell(row.getCell(11)));
							vbEmployeeDetail.setAlternateMobile(FileUploadUtil.readCell(row.getCell(10)));
							
							vbEmployeeDetail.setBloodGroup(FileUploadUtil.readCell(row.getCell(12)));
							vbEmployeeDetail.setPassportNumber(FileUploadUtil.readCell(row.getCell(13)));
							vbEmployeeDetail.setNationality(FileUploadUtil.readCell(row.getCell(14)));
							
							vbEmployeeAddress.setAddressLine1(FileUploadUtil.readCell(row.getCell(15)));
							vbEmployeeAddress.setAddressLine2(FileUploadUtil.readCell(row.getCell(16)));
							vbEmployeeAddress.setLocality(FileUploadUtil.readCell(row.getCell(17)));
							vbEmployeeAddress.setLandmark(FileUploadUtil.readCell(row.getCell(18)));
							vbEmployeeAddress.setCity(FileUploadUtil.readCell(row.getCell(19)));
							vbEmployeeAddress.setState(FileUploadUtil.readCell(row.getCell(20)));
							vbEmployeeAddress.setZipcode(FileUploadUtil.readCell(row.getCell(21)));
							vbEmployeeAddress.setAddressType(FileUploadUtil.readCell(row.getCell(22)));
							
							employeeDao.saveEmployee(vbEmployee, vbEmployeeDetail,vbEmployeeAddress, user.getName(), organization);
						}
						
					}
				}
			}
			//condition to check whether validating rows were failed
			if(faultRows.isEmpty()){
				response.sendRedirect("../index.jsp?module=employee");
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
				response.sendRedirect("../index.jsp?module=employee&pageLink=employee-import");
			}
			
		}else{
			//Throwing an exception when address types are not configured
			throw new Exception(Msg.get(MsgEnum.WARNING_NO_ADDRESS_TYPES_CONFIGURED));
		}
		
	} catch (Exception e) {
		HttpSession importsession = request.getSession();
		importsession.setAttribute("failureMsg", e.getMessage());
		response.sendRedirect("../index.jsp?module=employee&pageLink=employee-import");
	}
%>
