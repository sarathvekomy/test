<%@page import="org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCustomerCostCommand"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductCustomerCostDao"%>
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
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%
List<Integer> faultRows=new ArrayList<Integer>();
	try {
		String businessName=null;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) hibernateContext.getBean("productCustomerCostDao");
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		FileUploadUtil.createEvaluator(workBook);
		for(int sheetNo=1;sheetNo<workBook.getNumberOfSheets();sheetNo++){
			ProductCustomerCostCommand productCustomerCostCommand = null;
			HSSFSheet sheet = workBook.getSheetAt(sheetNo);
			Iterator<Row> rows = sheet.rowIterator();
			HSSFRow row = (HSSFRow) rows.next();
			int rowNumber=row.getRowNum();
			if(row.getRowNum() == 0){
				 businessName=FileUploadUtil.readProductCell(row.getCell(2));
			}
			VbOrganization organization = user.getOrganization();
			boolean isBusinessExists = productCustomerCostDao.isCustomerExist(businessName, organization);
			if(isBusinessExists) {
				boolean emptyFound=false;
				int mandatoryFields[] = {1,2,4};
				while (rows.hasNext()) {
					row = (HSSFRow) rows.next();
					if(row.getPhysicalNumberOfCells() > 1){
						if(row.getRowNum()>=0 && row.getRowNum()<=1){
							continue;
						}
				 		String productName=FileUploadUtil.readProductCell(row.getCell(1));
						String batchNumber=FileUploadUtil.readProductCell(row.getCell(2));
						String customerCost=FileUploadUtil.readProductCell(row.getCell(4));
						boolean isProductExists = productCustomerCostDao.isProductExist(productName, batchNumber, organization);
						if(isProductExists) {
							boolean isCostValidate = FileUploadUtil.validateProductCustomerCostXls(row.getCell(4));
							if(isCostValidate){
								faultRows.add(sheetNo+1);
							}else{
								productCustomerCostCommand = new ProductCustomerCostCommand();
								productCustomerCostCommand.setBusinessName(businessName);
								productCustomerCostCommand.setProductName(productName);
								productCustomerCostCommand.setBatchNumber(batchNumber);
								productCustomerCostCommand.setCost(Float.parseFloat(customerCost));
								productCustomerCostDao.saveProductCustomerCost(productCustomerCostCommand, user.getName(), organization);
							}
						}else{
							faultRows.add(sheetNo+1);
						}
					}
				}
			}else{
				faultRows.add(sheetNo+1);
			}
		}
		List<Integer> finalFaultSheets = new ArrayList<Integer>(new HashSet<Integer>(faultRows));
		if(finalFaultSheets.isEmpty()){
			response.sendRedirect("../../index.jsp?module=product&page=productcustomercost");
		}else{
			HttpSession importsession = request.getSession();
			String faultSheetNumbers="";
			for(int i=0;i<finalFaultSheets.size();i++){
				if(faultSheetNumbers.equals("")){
					faultSheetNumbers +=finalFaultSheets.get(i);
				}else{
					faultSheetNumbers +=","+finalFaultSheets.get(i);	
				}
			}
				importsession.setAttribute("failureMsg","Invalid entries found in the following excel sheets:<BR>"+faultSheetNumbers);
				response.sendRedirect("../../index.jsp?module=product&page=productcustomercost&pageLink=import-product-customer-cost");
		}
	} catch (Exception e) {
		HttpSession importsession = request.getSession();
		importsession.setAttribute("failureMsg", "Errors found while processing the Import functionality, Please provide the file with  valid content!");
		response.sendRedirect("../../index.jsp?module=product&page=productcustomercost&pageLink=import-product-customer-cost");
	}
%>
