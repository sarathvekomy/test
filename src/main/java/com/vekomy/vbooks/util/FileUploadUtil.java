package com.vekomy.vbooks.util;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.vekomy.vbooks.hibernate.model.VbAddressTypes;

public class FileUploadUtil {
	public static HSSFFormulaEvaluator evaluator;
	public static  StringBuilder addressTypesSaperated = new StringBuilder();;

	/* Method to Create Evaluator object to evaluate formulae 
	 * 
	 * 
	 * */
   public static void createEvaluator(HSSFWorkbook workBook){
	evaluator=new HSSFFormulaEvaluator(workBook);
   }
   
	@SuppressWarnings("rawtypes")
	public static POIFSFileSystem getFile(HttpServletRequest request) throws Exception {
		//Removed as it is depricated
		//DiskFileUpload fu = new DiskFileUpload();
		
		DiskFileItemFactory dfi=new DiskFileItemFactory();
	    dfi.setSizeThreshold(1024);
		/*dfi.setRepository(System.getProperty("java.io.tmpdir"));*/
		ServletFileUpload sf=new ServletFileUpload(dfi);
		
		/*fu.setSizeMax(10240000);*/
		/*fu.setSizeThreshold(1024);
		fu.setRepositoryPath(System.getProperty("java.io.tmpdir"));*/
		List fileList = sf.parseRequest(request);
		//List fileList = fu.parseRequest(request);
		InputStream uploadedFileStream = null;
		POIFSFileSystem poiFileSystem=null;
		//String uploadedFileName = null; // name of file on user's computer
		for (Iterator i = fileList.iterator(); i.hasNext();) {
			FileItem fi = (FileItem) i.next();
			if (fi.isFormField()) {
				//String key = fi.getFieldName();
				//String val = fi.getString();
				// out.println("Form parameter " + key + "=" + val + "<br/>");
			} else {
				if (fi.getSize() < 1) {
					throw new Exception("No file was uplaoded");
				}
				// out.print(fi.getSize() + "<br/>");
				// uploadedFileName = fi.getName();
				// out.print(uploadedFileName + "<br/>");
				uploadedFileStream = fi.getInputStream();
				poiFileSystem= new POIFSFileSystem(uploadedFileStream);
				break;
				// out.print(uploadedFileStream + "<br/>");
			}
		}
		uploadedFileStream.close();
		return poiFileSystem;
	}
	/* Method to create a comma saperated address type using a list
	 * to validate employee import entered address types
	 * 
	 * 
	 * */
	@SuppressWarnings("rawtypes")
	public static void readAddressTypes(List addressTypesList){
		String separator = ",";
		for(int i=0;i<addressTypesList.size();i++){
			VbAddressTypes vbAddressType=(VbAddressTypes) addressTypesList.get(i);
			addressTypesSaperated.append(vbAddressType.getAddressType()).append(separator);
		}
	}
	
	public static String readCell(HSSFCell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: {
			return "" + ((long)cell.getNumericCellValue());
		}
		case HSSFCell.CELL_TYPE_FORMULA: {
			HSSFCell hssfCell=evaluator.evaluateInCell(cell);
			return hssfCell.toString();
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
	public static String readProductCell(HSSFCell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC: {
			return "" + (cell.getNumericCellValue());
		}
		case HSSFCell.CELL_TYPE_FORMULA: {
			HSSFCell hssfCell=evaluator.evaluateInCell(cell);
			return hssfCell.toString();
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
	/* Method to validate organization import by each column in every row
	 * specifying each cell with defined column number
	 * 
	 * @param rowNumber - {@link Integer}
	 * @param cell - {@link HSSFCell}
	 *  @param columnNumber - {@link Integer}
	 * @return isOrganizationWrongvalidate - {@link Boolean}
	 * 
	 * */
	public static Boolean validateOrganizationXls(Integer rowNumber,HSSFCell cell,Integer columnNumber) {
		Boolean isOrganizationWrongvalidate=Boolean.FALSE;
		
			String cellValue=readCell(cell);
			switch (columnNumber) {
			case 1: {
				if(! cellValue.matches("Yes|No")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;		
			}
			case 2: {
				break;
			}
			case 3:{
				if(! cellValue.matches("[a-zA-Z0-9-_#@()\\s]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 4:{
				if(! cellValue.matches("[a-zA-Z0-9-_#@()\\s]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 5:{
				if(! cellValue.matches("^[a-zA-Z0-9-_#@()\\s]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 6: {
				if(! cellValue.matches("^[a-zA-Z$@#%^&*]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 7:{
				if(cellValue.length() == 0){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 8: {
				break;
			}
			case 9: {
				if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
					}
				break;
			}
			case 10: {
				if(cellValue.length() > 0){
					if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
						isOrganizationWrongvalidate = Boolean.TRUE;
						}
				}
				break;
			}
			case 11:{
				if( ! cellValue.matches("[a-zA-Z0-9.@]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 12:
			case 13:
			case 14:{
				if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
					}
				break;
			}
			case 15: {
				if(! cellValue.matches("[a-zA-Z0-9-\\s]+") || cellValue.length() > 9){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 16:
			case 22:{
				if(! cellValue.matches("[0-9-+()]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}case 17:{
				if(cellValue.length() > 0){
					if(! cellValue.matches("[0-9-+()]+")){
						isOrganizationWrongvalidate = Boolean.TRUE;
					}
				}
				break;
			}
			case 19: {
				if(! cellValue.matches("^[a-zA-Z0-9._]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			
			case 20: {
				if(! cellValue.matches("^[a-zA-Z ]+")){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 21: {
				Boolean res=cellValue.matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");
				if(! res){
					isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 23: {
				if(cellValue.length() > 0){
					if(! cellValue.matches("^[0-9-+()]+")){
						isOrganizationWrongvalidate = Boolean.TRUE;
					}
				}
				break;
			}
            case 24: {
            	if(! cellValue.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            		isOrganizationWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			default: {
				// types other than String and Numeric.
				break;
			}
			}
	
		return isOrganizationWrongvalidate;
	}
	/* Method to validate employee import by each column in every row
	 * specifying each cell with defined column number
	 * 
	 * @param rowNumber - {@link Integer}
	 * @param cell - {@link HSSFCell}
	 *  @param columnNumber - {@link Integer}
	 *   @param employeeTypeList - {@link List}
	 * @return isEmployeeWrongvalidate - {@link Boolean}
	 * 
	 * */
	public static Boolean validateEmployeeXls(Integer rowNumber,HSSFCell cell,Integer columnNumber) {
		Boolean isEmployeeWrongvalidate=Boolean.FALSE;
		if (cell == null){
			isEmployeeWrongvalidate = Boolean.TRUE;
		}else{
			String cellValue=readCell(cell);
			switch (columnNumber) {
			case 1: {
				if(! cellValue.matches("[a-zA-Z0-9_]+")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;		
			}
			case 2: {
				Boolean res=cellValue.matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");
				if(! res){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 3:
			case 5:{
				if(! cellValue.matches("[a-zA-Z]+")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 6: {
				break;
			}
			case 7: {
				if(! cellValue.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 8: {
				break;
			}
			case 9: {
			if(! cellValue.matches("[0-9-+()]+")){
				isEmployeeWrongvalidate = Boolean.TRUE;
			}
			break;
			}
			case 12: {
				if(! cellValue.matches("^(A|B|AB|O)[+-]$")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 13: {
				if(! cellValue.matches("[a-zA-Z0-9]+")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 14: {
				if(! cellValue.matches("[a-zA-Z\\s]+")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 15: {
				if(cellValue.length() == 0){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 17:
			case 19:{
			if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
				isEmployeeWrongvalidate = Boolean.TRUE;
				}
			break;
			}
			case 20: {
				if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 21: {
				if(! cellValue.matches("[a-zA-Z0-9-\\s]+") || cellValue.length() > 9){
					isEmployeeWrongvalidate = Boolean.TRUE;
				}
				break;
			}
			case 22: {
				if( addressTypesSaperated.indexOf(cellValue) == -1)
					isEmployeeWrongvalidate = Boolean.TRUE;
				break;
			}
			default: {
				// types other than String and Numeric.
				break;
			}
			}
		}
		return isEmployeeWrongvalidate;
	}
		
	/* Method to validate customer import by each column in every row.
	 * specifying each cell with defined column number
	 * @param rowNumber - {@link Integer}
	 * @param cell - {@link HSSFCell}
	 *  @param columnNumber - {@link Integer}
	 * @return isCustomerWrongvalidate - {@link Boolean}
	 * 
	 * */
		public static Boolean validateCustomerXls(Integer rowNumber,HSSFCell cell,Integer columnNumber) {
			Boolean isCustomerWrongvalidate=Boolean.FALSE;
			String cellValue=readCell(cell);
				switch (columnNumber) {
				case 1: {
					if(! cellValue.matches("[a-zA-Z0-9\\s]+") || cellValue.isEmpty())
						isCustomerWrongvalidate = Boolean.TRUE;
					break;		
				}
				case 2: {
					Boolean res=cellValue.matches("[a-zA-Z0-9.\\s]+");
					if(! res || cellValue == null)
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 3:{
					if(! cellValue.matches("[a-zA-Z\\s]+") || cellValue.isEmpty())
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 4:{
					if(cellValue == null)
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 5:{
					if(! cellValue.matches("[0-9-+()]+") || cellValue.isEmpty()){
						isCustomerWrongvalidate = Boolean.TRUE;
					}
					break;
				}
				case 6: {
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
							isCustomerWrongvalidate = Boolean.TRUE;
					}
					break;
				}
				case 7: {
					if(cellValue == null)
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 8: {
					if(! cellValue.matches("[0-9]+") || cellValue.length() > 3)
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 9: 
				case 10:{
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[0-9-+()\\s]+"))
							isCustomerWrongvalidate = Boolean.TRUE;
					}
				break;
				}
				case 11:
				case 12:{
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[a-zA-Z0-9\\s]+")){
							isCustomerWrongvalidate = Boolean.TRUE;
							}
						break;
					}
				break;
				}
				case 13: {
					if(! cellValue.matches("[a-zA-Z0-9\\s]+") || cellValue.isEmpty()){
						isCustomerWrongvalidate = Boolean.TRUE;
					}
					break;
				}
				case 14: {
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[a-zA-Z\\s]+"))
							isCustomerWrongvalidate = Boolean.TRUE;
					}
					break;
				}
				case 15: {
					if(! cellValue.matches("[a-zA-Z\\s]+") || cellValue.isEmpty())
						isCustomerWrongvalidate = Boolean.TRUE;
					break;
				}
				case 16:{
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[a-zA-Z0-9\\s]+"))
							isCustomerWrongvalidate = Boolean.TRUE;
					}
				break;
				}
				case 17: {
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[a-zA-Z0-9\\s]+"))
							isCustomerWrongvalidate = Boolean.TRUE;
					}
					break;
				}
				case 18: {
					if(!cellValue.isEmpty()){
						if(! cellValue.matches("[a-zA-Z0-9-\\s]+") || cellValue.length() > 9)
							isCustomerWrongvalidate = Boolean.TRUE;	
					}
					break;
				}
				default: {
					break;
				}
				}
			
			return isCustomerWrongvalidate;
		

	}
	
		/*Method to validate product import by each column in every row.
		 * specifying each cell with defined column number
		 * @param rowNumber - {@link Integer}
		 * @param cell - {@link HSSFCell}
		 *  @param columnNumber - {@link Integer}
		 * @return isProductWrongvalidate - {@link Boolean}
		 * 
		 * */
			public static Boolean validateProductXls(Integer rowNumber,HSSFCell cell,Integer columnNumber) {
				Boolean isProductWrongvalidate=Boolean.FALSE;
				String cellValue=readProductCell(cell);
				if (cell == null){
					isProductWrongvalidate = Boolean.TRUE;
				}else{
					switch (columnNumber) {
					case 1: {
						if(! cellValue.matches("[a-zA-Z0-9\\s]+"))
							isProductWrongvalidate = Boolean.TRUE;
						break;		
					}
					case 2: 
					case 3:{
						if(! cellValue.matches("[a-zA-Z0-9\\s]+"))
							isProductWrongvalidate = Boolean.TRUE;
						break;
					}
					case 4:{
						if(! cellValue.matches("[a-zA-Z0-9_#\\s]+"))
							isProductWrongvalidate = Boolean.TRUE;
						break;
					}
					case 6:{
						if(! cellValue.matches("[0-9.]+") || cellValue.length() > 10)
							isProductWrongvalidate = Boolean.TRUE;
						break;
					}
					default: {
						break;
					}
					}
				}
				return isProductWrongvalidate;
			

		}
			/* Method to validate product arrived quantity import by each column in every row
			 * specifying each cell with defined column number
			 * 
			 * @param rowNumber - {@link Integer}
			 * @param cell - {@link HSSFCell}
			 *  @param columnNumber - {@link Integer}
			 * @return isProductArrivedWrongvalidate - {@link Boolean}
			 * 
			 * */
			public static Boolean validateProductArrivedQtyXls(Integer rowNumber,HSSFCell cell,Integer columnNumber) {
				Boolean isProductArrivedWrongvalidate=Boolean.FALSE;
				if (cell == null){
					isProductArrivedWrongvalidate = Boolean.TRUE;
				}else{
					String cellValue=readCell(cell);
					switch (columnNumber) {
					case 1: {
						if(! cellValue.matches("[a-zA-Z0-9\\s]+"))
							isProductArrivedWrongvalidate = Boolean.TRUE;
						break;
					}
					case 2: {
						if(! cellValue.matches("[a-zA-Z0-9_#\\s]+"))
							isProductArrivedWrongvalidate = Boolean.TRUE;
						break;
					}
					case 3:{
						if( ! cellValue.matches("([0-9]+,?)+") || cellValue.length() > 10 || cellValue.length() <= 0)
							isProductArrivedWrongvalidate = Boolean.TRUE;
						break;
					}
					
				}
				}
				return isProductArrivedWrongvalidate;
			}
			/* Method to validate product customer cost import row
			 * specifying fourth cell
			 * 
			 * @param cell - {@link HSSFCell}
			 * @return isProductCustCostWrongvalidate - {@link Boolean}
			 * 
			 * */
			public static Boolean validateProductCustomerCostXls(HSSFCell cell) {
				Boolean isProductCustCostWrongvalidate=Boolean.FALSE;
				if (cell == null){
					isProductCustCostWrongvalidate = Boolean.TRUE;
				}else{
					String cellValue=readCell(cell);
					if(! cellValue.matches("[0-9.]+") || cellValue.length() > 10 || cellValue.length() <= 0){
						isProductCustCostWrongvalidate = Boolean.TRUE;
					}
				}
				return isProductCustCostWrongvalidate;
			}
	
}