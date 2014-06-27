<%@page import="org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator"%>
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
<%@page import="java.util.*"%>
<%
List<Integer> faultRows=new ArrayList<Integer>();
	try {
		String businessName=null;
		Integer damagedQuantityDefault=0;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		ProductDao productDao = (ProductDao) hibernateContext.getBean("productDao");
		HSSFWorkbook workBook = new HSSFWorkbook(FileUploadUtil.getFile(request));
		FileUploadUtil.createEvaluator(workBook);
			ProductCommand productCommand = null;
			List<ProductCommand> arrivedQuantityList = new ArrayList<ProductCommand>();
			VbOrganization organization = user.getOrganization();
			HSSFSheet sheet = workBook.getSheetAt(1);
			Iterator<Row> rows = sheet.rowIterator();
			HSSFRow row = (HSSFRow) rows.next();
			int rowNumber=row.getRowNum();
			boolean emptyFound=false;
			int mandatoryFields[] = {1,2,3};
			while (rows.hasNext()) {
				Boolean isRowWrongValidated=Boolean.FALSE;
				row = (HSSFRow) rows.next();
				if(row.getPhysicalNumberOfCells() > 1){
					if(row.getRowNum() >=0 && row.getRowNum() <=4){
						continue;
					}
					String productName=FileUploadUtil.readCell(row.getCell(1));
					String batchNumber=FileUploadUtil.readCell(row.getCell(2));
					for (int i = 0; i < mandatoryFields.length; i++) {
						Boolean isValidate=FileUploadUtil.validateProductArrivedQtyXls(row.getRowNum(),row.getCell(mandatoryFields[i]),mandatoryFields[i]);
						if(isValidate){
							isRowWrongValidated = Boolean.TRUE;
						}
					}
						if(isRowWrongValidated){
							faultRows.add(row.getRowNum()+1);
						}else{
							boolean isProductExists = productDao.isProductExists(productName, batchNumber, organization);
							if(isProductExists){
								productCommand = new ProductCommand();
								productCommand.setProductName(productName);
								productCommand.setBatchNumber(batchNumber);
								productCommand.setQunatityArrived(Integer.parseInt(FileUploadUtil.readCell(row.getCell(3))));
								productCommand.setDamagedQuantity(damagedQuantityDefault);
								arrivedQuantityList.add(productCommand);
							}else{
								faultRows.add(row.getRowNum()+1);
							}
							
						}
				}
			}
			productDao.addArrivedQuantity(arrivedQuantityList, user.getName(), user.getOrganization());
			if(faultRows.isEmpty()){
				response.sendRedirect("../index.jsp?module=product");
			}else{
				HttpSession importsession1 = request.getSession();
				String faultRowNumbers="";
				for(int i=0;i<faultRows.size();i++){
					if(faultRowNumbers.equals("")){
						faultRowNumbers +=faultRows.get(i);
					}else{
						faultRowNumbers +=","+faultRows.get(i);	
					}
				}
					importsession1.setAttribute("fail","Invalid entries found in the following excel rows:<BR>"+faultRowNumbers);
					response.sendRedirect("../index.jsp?module=product&pageLink=product-import");
			}
		
	} catch (Exception e) {
		HttpSession importsession1 = request.getSession();
		importsession1.setAttribute("fail", "Errors found while processing the Import functionality, Please provide the file with  valid content!");
		response.sendRedirect("../index.jsp?module=product&pageLink=product-import");
	}
%>
