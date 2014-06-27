
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.vekomy.vbooks.organization.command.OrganizationCommand"%>
<%@page import="com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand"%>
<%@page import="com.vekomy.vbooks.siteadmin.dao.SiteAdminDao"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="com.vekomy.vbooks.util.FileUploadUtil"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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
		SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
		OrganizationDao orgDao = (OrganizationDao) hibernateContext.getBean("organizationDao");
//		SiteAccountsDao siteAccountsDao = (SiteAccountsDao) hibernateContext .getBean("siteAccountsDao");
		OrganizationCommand organizationUser = null;
		OrganizationCommand organizationCmdObj = null;
		OrganizationSuperUserCommand organizationSupercmd =null;
		VbOrganization organization = new VbOrganization();
		//VbOrganization organization = user.getOrganization();
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
			int mandatoryFields[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
			while (rows.hasNext()) {
				Boolean isRowWrongValidated=Boolean.FALSE;
				row = (HSSFRow) rows.next();
				if(row.getPhysicalNumberOfCells() > 1){
					//Not to consider first 5 rows of a sheet
					if(row.getRowNum()>=0 && row.getRowNum()<=4)
						continue;
					for (int i = 0; i < mandatoryFields.length; i++) {
						Boolean isValidate=FileUploadUtil.validateOrganizationXls(row.getRowNum(),row.getCell(mandatoryFields[i]),mandatoryFields[i]);
						if(isValidate){
							isRowWrongValidated = Boolean.TRUE;
						}
					}
					//Checking condition if validating a row is failed
					if(isRowWrongValidated){
						faultRows.add(row.getRowNum()+1);
					}
					else{
						String isMainBranch=FileUploadUtil.readCell(row.getCell(1));
						organizationUser = new OrganizationCommand();
						organizationCmdObj = new OrganizationCommand();
						organizationSupercmd = new OrganizationSuperUserCommand();
						if("No".equalsIgnoreCase(isMainBranch)){
							String mainBranchName=FileUploadUtil.readCell(row.getCell(2));
							Boolean isMainBranchAvailable =siteadminDao.checkMainBranch(mainBranchName);
							organizationUser.setMainBranch(isMainBranch);
							if(isMainBranchAvailable){
								organizationUser.setMainBranchName(FileUploadUtil.readCell(row.getCell(2)));
							
							}else{
								faultRows.add(row.getRowNum()+1);
								continue;
							}
						}else{
							organizationUser.setMainBranch("Yes");
						}
						String orgCode=FileUploadUtil.readCell(row.getCell(3));
						organizationCmdObj.setOrganizationCode(orgCode);
						String result=orgDao.validateOrganizationCode(organizationCmdObj);
						if(result.equalsIgnoreCase("y")){
							String orgUserPrefix=FileUploadUtil.readCell(row.getCell(11));
							Boolean isOrgPrefixExists=orgDao.checkUeserPrefixAvailabilty(orgUserPrefix,null);
							if(! isOrgPrefixExists){
								organizationUser.setOrganizationCode(FileUploadUtil.readCell(row.getCell(3)));
								organizationUser.setName(FileUploadUtil.readCell(row.getCell(4)));
								organizationUser.setBranchName(FileUploadUtil.readCell(row.getCell(5)));
								organizationUser.setCurrencyFormat(FileUploadUtil.readCell(row.getCell(6)));
								organizationUser.setAddressLine1(FileUploadUtil.readCell(row.getCell(7)));
								organizationUser.setAddressLine2(FileUploadUtil.readCell(row.getCell(8)));
								organizationUser.setLocality(FileUploadUtil.readCell(row.getCell(9)));
								organizationUser.setLandmark(FileUploadUtil.readCell(row.getCell(10)));
								organizationUser.setUsernamePrefix(FileUploadUtil.readCell(row.getCell(11)));
								organizationUser.setCity(FileUploadUtil.readCell(row.getCell(12)));
								organizationUser.setState(FileUploadUtil.readCell(row.getCell(13)));
								organizationUser.setCountry(FileUploadUtil.readCell(row.getCell(14)));
								organizationUser.setZipcode(FileUploadUtil.readCell(row.getCell(15)));
								organizationUser.setPhone1(FileUploadUtil.readCell(row.getCell(16)));
								organizationUser.setPhone2(FileUploadUtil.readCell(row.getCell(17)));
								organizationUser.setDescription(FileUploadUtil.readCell(row.getCell(18)));
								organizationSupercmd.setSuperUsername(FileUploadUtil.readCell(row.getCell(19)));
								organizationSupercmd.setPassword(FileUploadUtil.readCell(row.getCell(21)));
								organizationSupercmd.setFullName(FileUploadUtil.readCell(row.getCell(20)));
								organizationSupercmd.setMobile(FileUploadUtil.readCell(row.getCell(22)));
								organizationSupercmd.setAlternateMobile(FileUploadUtil.readCell(row.getCell(23)));
								organizationSupercmd.setEmail(FileUploadUtil.readCell(row.getCell(24)));
											
								siteadminDao.saveOrganization(organizationUser, organizationSupercmd,user.getName());
							
							}else{
								faultRows.add(row.getRowNum()+1);
								continue;
							}
						}else{
							faultRows.add(row.getRowNum()+1);
							continue;
						}
							//organizationUser.setMainBranchName(FileUploadUtil.readCell(row.getCell(2)));
					}
				}
			}
			//condition to check whether validating rows were failed
			if(faultRows.isEmpty()){
				response.sendRedirect("../index.jsp?module=siteadmin");
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
				response.sendRedirect("../index.jsp?module=siteadmin&pageLink=import-organization");
			}
		
	} catch (Exception e) {
		HttpSession importsession = request.getSession();
		importsession.setAttribute("failureMsg", e.getMessage());
		response.sendRedirect("../index.jsp?module=siteadmin&pageLink=import-organization");
	}
%>
