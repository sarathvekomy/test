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
<%@page import="java.util.ArrayList"%>
<%
List<Integer> faultRows=new ArrayList<Integer>();
	try {
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		FileUploadUtil.createEvaluator(workBook);
		HSSFSheet sheet = workBook.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ProductDao productDao = (ProductDao) hibernateContext.getBean("productDao");
		ProductCommand productCommand = null;
		HSSFRow row = (HSSFRow) rows.next();
		boolean emptyFound=false;
		int mandatoryFields[] = {1,2,3,4,6};
		while (rows.hasNext()) {
			Boolean isRowWrongValidated=Boolean.FALSE;
			row = (HSSFRow) rows.next();
			if(row.getRowNum()>=0 && row.getRowNum()<=4)
				continue;
			for (int i = 0; i < mandatoryFields.length; i++) {
				Boolean isValidate=FileUploadUtil.validateProductXls(row.getRowNum(),row.getCell(mandatoryFields[i]),mandatoryFields[i]);
				if(isValidate){
					isRowWrongValidated = Boolean.TRUE;
				}
			}
			if(isRowWrongValidated){
				faultRows.add(row.getRowNum()+1);
			}else{
				VbOrganization organization = user.getOrganization();
				String brand=FileUploadUtil.readProductCell(row.getCell(2));
				String model=FileUploadUtil.readProductCell(row.getCell(3));
				String productName = brand + " " + model;
				String batchNumber = FileUploadUtil.readProductCell(row.getCell(4));
				Boolean isProductExists = productDao.isProductExists(productName, batchNumber, organization);
				if(!isProductExists) {
					productCommand = new ProductCommand();
					productCommand.setProductCategory(FileUploadUtil.readProductCell(row.getCell(1)));
					productCommand.setBrand(brand);
					productCommand.setModel(model);
					productCommand.setProductName(productName); 
					productCommand.setBatchNumber(batchNumber);
					productCommand.setDescription(FileUploadUtil.readProductCell(row.getCell(5)));
					productCommand.setCostPerQuantity(Float.parseFloat(FileUploadUtil.readProductCell(row.getCell(6))));
					
					productDao.saveProduct(productCommand, user.getName(), organization);
				}
			}
		}
		if(faultRows.isEmpty()){
			response.sendRedirect("../index.jsp?module=product");
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
				response.sendRedirect("../index.jsp?module=product&pageLink=product-import");
		}
	} catch (Exception e) {
		HttpSession importsession = request.getSession();
		importsession.setAttribute("failureMsg", "Errors found while processing the Import functionality, Please provide the file with  valid content!");
		response.sendRedirect("../index.jsp?module=product&pageLink=product-import");
	}
%>
