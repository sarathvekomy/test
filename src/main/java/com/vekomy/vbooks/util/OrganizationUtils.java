package com.vekomy.vbooks.util;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.security.User;

public class OrganizationUtils {

	public static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public static final int TOTAL_PERIODS = 8;
	public static final int EMPLOYEE_TYP_MANAGEMENT = 1;
	public static final int EMPLOYEE_TYP_ACOUNTANT = 2;
	public static final int EMPLOYEE_TYP_SALES_EXECUTIVE = 3;
	public static final String PARENT_DEFAULT_PASSWORD = "welcome";
	public static final String ROLE_SUPER_MANAGEMENT = "Super Management";
	public static final int SUPER_MANAGEMENT = 7;
	public static final String MANAGEMENT_USER = "Management User";
	public static final String GROUPHEAD = "ROLE_GROUPHEAD";
	public static final int SUPER_MANAGEMENT_EMPLOYEE_NUMBER = 1;
	public static final String SUPER_MANAGEMENT_LAST_NAME = "";
	public static final int MANAGEMENT_EMPLOYEE_NUMBER = 1;
	public static final String MANAGEMENT_LAST_NAME = "";
	public static final String PARENT_LOGIN_PREFIX = "parent_";
	public static final int ROLE_USER = 1;
	public static final String ROLE_USER_STR = "ROLE_USER";
	public static final int ROLE_SITEADMIN = 2;
	public static final int ROLE_MANAGEMENT = 3;
	public static final String ROLE_MANAGEMENT_STR = "ROLE_MANAGEMENT";
	public static final String ROLE_MANAGEMENT_VALUE = "Management";
	public static final int ROLE_ACCOUNTANT = 4;
	public static final String ROLE_ACOUNTANT_STR = "ROLE_ACCOUNTANT";
	public static final String ROLE_ACOUNTANT_VALUE = "Accountant";
	public static final int ROLE_SALESEXECUTIVE = 5;
	public static final String ROLE_SALESEXECUTIVE_STR = "ROLE_SALESEXECUTIVE";
	public static final String ROLE_SALESEXECUTIVE_VALUE = "Sales Executive";
	public static final int MAXIMUM_SMSES_PER_SCHOOL = 100000;
    public static final String THEMES_URL = "";
	public static final String HELP_URL = "";
	public static final String PICTURE_FOLDER = File.separator + "usr" + File.separator + "pic_store" + File.separator;
	public static final char FIRST_TIME_LOGIN_YES = 'Y';
	public static final char FIRST_TIME_LOGIN_NO = 'N';
	public static final char LOGIN_ENABLED = '1';
	public static final String LOGIN_ENABLED_STRING = "Enabled";
	public static final char LOGIN_DISABLED = '0';
	public static final String LOGIN_DISABLED_STRING = "Disabled";
	public static final int TIME_INTERVAL_BETWEEN_BULK_SMS_IN_SECOUNDS = 1;
	public static final int SMS_LIMIT_AT_ONCE = 15;
	public static final String SMS = "SMS";
	public static final String LIM = "LIM";
	public static final int THUMBNAIL_IMAGE_HIEGHT = 60;
	public static final int THUMBNAIL_IMAGE_WIDTH = 70;
	public static final int PREVIEW_IMAGE_HIEGHT = 124;
	public static final int PREVIEW_IMAGE_WIDTH = 100;
	public static final String PASSWORD_CHANGED_MESSAGE = "Password changed successfully";
	public static final String PASSWORD_NOTIFICATION = "Password Notification";
	public static final String NEW_PASSWORD_PREFIX = "Your New password is : ";
	public static final String ASSESSMENT_NOTIFICATION_SUBJECT = "Assessment Notification";
	public static final String ASSESSMENT_NOTIFICATION_MESSAGE = "Assessment Entered";
	public static final String ASSESSMENT_NOTIFICATION_SMS = "Assessment Entered";
	public static final boolean STUDENT_DELETE_YES = true;
	public static final boolean STUDENT_DELETE_NO = false;
	public static final String ADDRESS_TYPE_PRIMARY = "primary";
	public static final String PRINT_HEADER_TYPE = "phd";
	public static final String PRINT_HEADER_KEY = "def";
	public static final String PRINT_HEADER_STR = "<div class=\"play-slip\"><div class=\"school-header-name\"><div class=\"school-logo\">|</div><div class=\"school-name\">|<div class=\"clearfloat\"></div>|</div><div class=\"print-btn-bg\">|</div></div></div>";
	// Alerts ---> START
	public static final String SYSTEM_ALERT = "System Alerts";
	public static final String USER_DEFINED_ALERT = "User Defined Alerts";
	public static final String ALERT_TYPE_SYSTEM_DEFAULTS = "System Defaults";
	public static final String ALERT_TYPE_CREDIT_OVERDUE = "Credit Overdue";
	public static final String ALERT_TYPE_STOCK_ALLOTMENT = "Stock Allotment";
	public static final String ALERT_TYPE_DAY_BOOK_CLOSURE = "Day Book closure";
	public static final String ALERT_TYPE_WRONG_PWD_ENTRY = "Wrong login for 5 times";
	public static final String ALERT_TYPE_ALLOWANCE_OVER_LIMIT = "Allowance Overlimit";
	public static final String ALERT_TYPE_MY_SALES = "My Sales";
	public static final String ALERT_TYPE_TRENDING = "Trending";
	public static final String ALERT_TYPE_EXCESS_AMOUNT = "Excess Amount";
	public static final String EMAIL_RECEIPIENT_TYPE_TO = "to";
	public static final String EMAIL_RECEIPIENT_TYPE_CC = "cc";
	public static final String EMAIL_RECEIPIENT_TYPE_BCC = "bcc";
	public static final String ALERT_NOTIFICATION_TYPE_IN_SYSTEM_ALERT = "In System Alert";
	public static final String ALERT_NOTIFICATION_TYPE_SMS = "SMS";
	public static final String ALERT_NOTIFICATION_TYPE_EMAIL = "Emails";
	public static final String ALERT_MYSALES_TYPE_APPROVALS = "Approvals";
	public static final String ALERT_MYSALES_TYPE_TRANSACTION_CR = "Transaction CR";
	public static final String ALERT_MYSALES_PAGE_NEW_CUSTOMER_CR = "New Customer CR";
	public static final String ALERT_MYSALES_PAGE_EXISTING_CUSTOMER_CR = "Existing Customer CR";
	public static final String ALERT_MYSALES_PAGE_SALES_RETURN = "Sales Return";
	public static final String ALERT_MYSALES_PAGE_JOURNAL = "Journal";
	public static final String ALERT_MYSALES_PAGE_DELIVERY_NOTE = "Delivery Note";
	public static final String ALERT_MYSALES_PAGE_DAY_BOOK = "Day Book";
	public static final Integer WRONG_PASSWORD_COUNT_LIMIT = 5;
	public static final String STATUS_APPROVED = "Approved";
	public static final String STATUS_DECLINED = "Decline";
	public static final String CR_TYPE_DELIVERY_NOTE = "Delivery Note";
	public static final String CR_TYPE_SALES_RETURNS = "Sales Returns";
	public static final String CR_TYPE_JOURNAL = "Journal";
	public static final String CR_TYPE_DAY_BOOK = "Day Book";
	public static final String ALERT_TYPE_DAY_BOOK_HAVING_CLOSING_STOCK = "Having Closing Stock And Balance";
	public static final String RESULT_STATUS_SUCCESS = "success";
	public static final String RESULT_STATUS_FAILURE = "fail";
	public static final String DES_ALGORITHM = "DES";
	public static final String SECRET_PASSWORD = "secretpassword";
	public static final String SEPERATOR = "/";
	public static final String MONTHLY = "Monthly";
	public static final String QUARTERLY = "Quarterly";
	public static final String HALF_YEARLY = "Half Yearly";
	public static final String YEARLY = "Yearly";
	public static final String PRODUCT_ENABLED="Enabled";
	public static final String CUSTOMER_ENABLED="Enabled";
	public static final String PRODUCT_DISABLED="Disabled";
	public static final String CUSTOMER_DISABLED="Disabled";
	public static final String PRODUCT_INVENTORY_TXN_TYPE_ALLOTTED = "Allotted";
	public static final String PRODUCT_INVENTORY_TXN_TYPE_ARRIVED = "Arrived";
	public static final String PRODUCT_INVENTORY_TXN_TYPE_DAMAGED = "Damaged";
	public static final String TXN_TYPE_DELIVERY_NOTE = "DN";
	public static final String TXN_TYPE_DELIVERY_NOTE_COLLECTIONS = "COLLECTIONS";
	public static final String TXN_TYPE_SALES_RETURN = "SR";
	public static final String TXN_TYPE_JOURNAL = "Journal";
	public static final String TXN_TYPE_DAY_BOOK = "DB";
	public static final String TXN_TYPE_SALES_BOOK = "SB";
	public static final String TXN_TYPE_CUSTOMER_CREDIT = "CR";
	public static final String TXN_TYPE_ADVANCE = "AD";
	public static final String DAY_BOOK_AMOUNT = "Deposit Amount";
	public static final String DAY_BOOK_VEHICLE_DETAILS = "Vehicle Details";
	public static final String DAY_BOOK_ALLOWANCES = "Allowances";
	public static final String DAY_BOOK_EXECUTIVE_ALLOWANCES = "Executive Allowances";
	public static final String DAY_BOOK_DRIVER_ALLOWANCES = "Driver Allowances";
	public static final String DAY_BOOK_OFFLOADING_CHARGES = "Offloading Charges";
	public static final String DAY_BOOK_DEALER_PARTY_EXPENSES = "Dealer Party Expenses";
	public static final String DAY_BOOK_VEHICLE_FUEL_EXPENSES = "Vehicle Fuel Expenses";
	public static final String DAY_BOOK_MUNCIPAL_CITY_COUNCIL = "Municipal City Council";
	public static final String DAY_BOOK_MISCELLANEOUS_EXPENSES = "Miscellaneous Expenses";
	public static final String DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES = "Vehicle Maintenance Expenses";
	public static final String ALLOTMENT_TYPE_DAILY = "Daily";
	public static final String ALLOTMENT_TYPE_NON_DAILY = "Non-Daily";
	public static final String SYSTEM_ALLOTMENT = "System allotment";
	// Alerts ---> END
	public static final String DISABLED_USER_ERROR_MSG = "User de-activated Please Contact your admin";
	public static final String INVALID_USERNAME_PASSWORD= "Invalid username/password.";
	public static final Integer WARNING_PASSWORD_COUNT= 4;
	public static final String WARNING_PASSWORD_COUNT_MESSAGE= "Warning ! Exceeding wrong password count limit";
	public static String getModules() {
		Iterator<GrantedAuthority> roleIterator = SecurityContextHolder.getContext().getAuthentication()
		        .getAuthorities().iterator();
		// basic logic
		while (roleIterator.hasNext()) {
			GrantedAuthority authority = (GrantedAuthority) roleIterator.next();
			if (authority.getAuthority().equals("ROLE_SITEADMIN")) {
				return "{siteadmin:true}";
			}
			if (authority.getAuthority().equals("ROLE_SUPER_MANAGEMENT")) {
				return "{employee:true, siteadmin:true, customer:true, product:true, accounts:true, mysales:true, reports:true}";
			}
			if (authority.getAuthority().equals("ROLE_MANAGEMENT")) {
				return "{employee:true, siteadmin:true, customer:true, product:true, accounts:true, mysales:true, reports:true}";
			}
			if (authority.getAuthority().equals("ROLE_ACCOUNTANT")) {
				return "{employee:true, product:true, accounts:true, customer:true, reports:true}";
			}
			if(authority.getAuthority().equals("ROLE_SALESEXECUTIVE")) {
				return "{mysales:true}";
			}
			if(authority.getAuthority().equals("ROLE_GROUPHEAD")) {
				return "{grouphead:true}";
			}
		}

		return "[]";
	}

	public static String getThemeCSS(User user) {
		String theme = "";
		if (user.getTheme() == null || "".equals(user.getTheme())) {
			theme = "default";
		} else {
			theme = user.getTheme();
		}
		return "css/" + theme + "/" + theme + ".css";
	}

	public static String getImageUrl(User user, String image) {
		String theme = "";
		if (user.getTheme() == null || "".equals(user.getTheme())) {
			theme = "default";
		} else {
			theme = user.getTheme();
		}
		return "images/" + theme + "/" + image;
	}

	public static String getFavoriteUrl(User user, String favoriteLink) {
		String favorite = "";
		// if(user.getFavorite()==null || "".equals(user.getFavorite())) {
		// favorite = "";
		// } else {
		// favorite = user.getFavorite();
		// }
		return "index.jsp?" + favorite;

	}

	public static String getModuleImageUrl(User user, String image) {
		String theme = "";
		if (user.getTheme() == null || "".equals(user.getTheme())) {
			theme = "default";
		} else {
			theme = user.getTheme();
		}
		return "images/" + theme + "/pages/" + image;
	}

	public static String getEmployeeType(String employeeTypeString) {
		String employeeType = "3";
		if (employeeTypeString.startsWith("admin") || employeeTypeString.startsWith("Admin")) {
			employeeType = "2";
		} else if (employeeTypeString.startsWith("man") || employeeTypeString.startsWith("Man")) {
			employeeType = "1";
		}
		return employeeType;
	}

	public static String getAddressType(String employeeTypeString) {
		String employeeType = "2";
		if (employeeTypeString.startsWith("primary") || employeeTypeString.startsWith("Primary")) {
			employeeType = "1";
		}
		return employeeType;
	}

	public static String generatePassword() {
		return "";
	}

	public static String getFormatedServerDate() {
		return simpleDateTimeFormat.format(new Date()).toString();
	}

	public static String validateRemainderDate(String dueDate, String remainderDate) {
		String isRemainderValid = "Yes";
		 
		
		    int dueDateStatus = simpleDateFormat.parse(dueDate, new ParsePosition(0)).compareTo(
		        simpleDateFormat.parse(simpleDateFormat.format(new Date()).toString(), new ParsePosition(0)));
		if (dueDateStatus < 0) {
			/* due date can not be before this date */
			return "DUEDATE_BEFORE";

		} else if (dueDateStatus == 0) {
			/* due date can not be on this day */
			return "DUEDATE_ONDATE";
		} else {
		    if(remainderDate.equals("")) return isRemainderValid;
			/* continue to check remainder date... */
			if (simpleDateFormat.parse(remainderDate, new ParsePosition(0)).before(
			        simpleDateFormat.parse(simpleDateFormat.format(new Date()).toString(), new ParsePosition(0)))) {
				isRemainderValid = "REMAINDERDATE_BEFORE";
			} else {
				int remainderDateStatus = simpleDateFormat.parse(remainderDate, new ParsePosition(0)).compareTo(
				        simpleDateFormat.parse(dueDate, new ParsePosition(0)));
				if (remainderDateStatus > 0)
					isRemainderValid = "REMAINDERDATE_AFTER_DUEDATE";
				else if (remainderDateStatus == 0)
					isRemainderValid = "REMAINDERDATE_ONDATE_DUEDATE";
				else {
				}
			}
		}
		return isRemainderValid;
		
	}

	/*
	 * public static boolean validateGeneratedDate(String dueDate) {
	 * 
	 * boolean canGenerate = true; if(dueDate != null && dueDate != "") { int
	 * status = simpleDateFormat.parse(simpleDateFormat.format(new
	 * Date()).toString(), new
	 * ParsePosition(0)).compareTo(simpleDateFormat.parse(dueDate,new
	 * ParsePosition(0)));
	 * 
	 * if(status < 0) canGenerate = false;//before else if(status > 0) ;//after
	 * else canGenerate = false;//on date } return canGenerate; }
	 */

	public static String validateGeneratedDate(String dueDate) {

		String canContinueGenerate = "YES";
		if (dueDate != null && dueDate != "") {
			int status = simpleDateFormat.parse(simpleDateFormat.format(new Date()).toString(), new ParsePosition(0))
			        .compareTo(simpleDateFormat.parse(dueDate, new ParsePosition(0)));

			if (status < 0)
				canContinueGenerate = "BEFORE";
			else if (status > 0)
				canContinueGenerate = "AFTER";
			else
				canContinueGenerate = "ONDATE";
		}
		return canContinueGenerate;
	}

	public static boolean validateNumberFormat(String numberString) {
		int fromIndex = numberString.length() - 10;
		numberString = numberString.substring(fromIndex);
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(numberString);
		return matcher.find();

	}

	public static boolean compareWithCurrentDate(String date) {
		Date d = null;
		Date currentDate = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			d = (Date) dateFormat.parse(date);
			currentDate = new Date();
		} catch (Exception e) {

		}
		return d.before(currentDate);
	}

	public static int compareToCurrentDate(String date) {
		Date d = null;
		Date currentDate = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
			d = (Date) dateFormat.parse(date);
			currentDate = new Date();
		} catch (Exception e) {

		}
		return d.compareTo(currentDate);

	}
	
	public static String getEmployeeTypeByRole(String group) {
		String employeeType = null;
		if ("Sales Executive".equals(group)) {
			employeeType = "SLE";
		} else if ("Management".equals(group)) {
			employeeType = "MGT";
		} else if ("Accountant".equals(group)) {
			employeeType = "ACC";
		} else if("Super Management".equals(group)) {
			employeeType = "Super Management";
		}

		return employeeType;
	}
	
	public static String getEmployeePicturesDirectory() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return PICTURE_FOLDER + user.getOrganization().getId() + File.separator + "employee" + File.separator;
	}
	public static String getProductPicturesDirectory() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return PICTURE_FOLDER + user.getOrganization().getId() + File.separator + "product" + File.separator;
	}

	public static String getStudentPicturesDirectory() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return PICTURE_FOLDER + user.getOrganization().getId() + File.separator + "corporate" + File.separator;
	}
	
	public static String getOrganizationLogoDirectory() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return PICTURE_FOLDER + user.getOrganization().getId() + File.separator + "logo" + File.separator;
	}

    public static String getOrganizationLogoDirectory(VbOrganization vbOrganization) {
        return PICTURE_FOLDER + vbOrganization.getId() + File.separator + "logo" + File.separator;
    }

	public static String createPrintHeaderDefault(VbOrganization vbOrganization) {
        String defaultPrintHeader = "";
        StringBuffer path=new StringBuffer();
        path.append("../servlet/schoolLogo?");
        if(System.getProperty("os.name").startsWith("Windows")){
            path.append((OrganizationUtils.getOrganizationLogoDirectory(vbOrganization)+vbOrganization.getId()+"-original.jpg").replace('/', '\\'));
        }else{
            path.append((OrganizationUtils.getOrganizationLogoDirectory(vbOrganization)+vbOrganization.getId()+"-original.jpg"));
        }
        
        defaultPrintHeader = "<div style=\"height: 70px;\">" +
        		"<table style=\"border-collapse: collapse; margin: 0pt auto; border-bottom: 1px solid;\">" +
        		"<tbody>" +
        		"<tr style=\"height: 60px; margin: 0pt auto;\">" +
        		"<td style=\"text-align: center; font: 14px Arial; padding-left: 5px; width: 20px;\">" +
        		"<img border=\"0\" src=\"" +path.toString()+
        		"\" width=\"100\" alt=\"LOGO\"></img>" +
        		"</td>" +
        		"<td style=\"font: 14px Arial; padding-left: 10px; width: 400px;\">" +
        		    vbOrganization.getName()+"-"+vbOrganization.getBranchName()+"<br>"+
        		    vbOrganization.getAddressLine1()+","+vbOrganization.getZipcode()+"<br>"+
        		    "Ph: "+vbOrganization.getPhone1()+","+vbOrganization.getPhone2()+
        		"</td>"+
        		"</tr>" +
        		"</tbody>" +
        		"</table>" +
        		"</div>"; 
        return defaultPrintHeader;
    }
	

}
