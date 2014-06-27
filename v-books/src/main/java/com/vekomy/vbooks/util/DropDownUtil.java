package com.vekomy.vbooks.util;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author Satish
 *
 * 
 */
public class DropDownUtil {

	private static HashMap<String, HashMap<String, String>> dropDowns = new HashMap<String, HashMap<String, String>>();
	public static String FLAG = "FLAG";
	public static String DAY = "DAY";
	public static String ASSIGNEDTO = "ASSIGNEDTO";
	public static String PRIORITY = "PRI";
	public static String STATUS = "STATUS";
	
	public static String NIRVAHAKACCOUNTPLANS = "NIRVAHAKACCOUNTPLANS";
	public static String GENDER ="GENDER";
	
	public static String POST_APPLIED_FOR = "post_applied_for";
	public static String APPLICANT_STATUS = "applicant_status";
	public static String EMPLOYEE_TYPE = "employee_type";
	public static String ALLOTMENT_TYPE = "allotment_type";
	public static String ADDRESS_TYPE = "address_type";
	public static String PAYMENT_TYPE = "payment_type";
	public static String REPORT_TYPE = "report_type";
	public static String MAIN_BRANCH = "main_branch";
	public static String BRANCH_TYPE = "branch_type";
	
	public static String COMPONENT_RECURRENCE = "component_recurrence";
	
	// public static String MAILCONFIGURE = "MAILCONFIGURE";
	static {
		DropDownUtil.load();
	}

	/**
	 * 
	 */
	public DropDownUtil() {
	}

	public static void load() {

		HashMap<String, String> flags = new HashMap<String, String>();
		flags.put("true", "Yes");
		flags.put("false", "No");
		DropDownUtil.dropDowns.put(DropDownUtil.FLAG, flags);
		HashMap<String, String> days = new HashMap<String, String>();
		days.put("SUN", "Sunday");
		days.put("MON", "Monday");
		days.put("TUE", "Tuesday");
		days.put("WED", "Wednesday");
		days.put("THU", "Thursday");
		days.put("FRI", "Friday");
		days.put("SAT", "Saturday");
		days.put("ALL", "All Days");
		DropDownUtil.dropDowns.put(DropDownUtil.DAY, days);
		HashMap<String, String> priorities = new HashMap<String, String>();
		priorities.put("NL", "Normal");
		priorities.put("LOW", "Low");
		priorities.put("HGH", "High");
		DropDownUtil.dropDowns.put(DropDownUtil.PRIORITY, priorities);
		
		
		HashMap<String, String> addressType = new HashMap<String, String>();
		addressType.put("Primary", "Primary");
		addressType.put("Secondary", "Secondary");
		DropDownUtil.dropDowns.put(DropDownUtil.ADDRESS_TYPE, addressType);
		
		LinkedHashMap<String, String> nirvahakAccountPlans = new LinkedHashMap<String, String>();
		nirvahakAccountPlans.put("PLA", "Platinium");
		nirvahakAccountPlans.put("GLD", "Gold");
		nirvahakAccountPlans.put("SIL", "Silver");
		nirvahakAccountPlans.put("FRE", "Free");
		DropDownUtil.dropDowns.put(DropDownUtil.NIRVAHAKACCOUNTPLANS, nirvahakAccountPlans);

		
		
		HashMap<String, String> gender = new HashMap<String, String>();
		gender.put("M", "Male");
		gender.put("F", "Female");
		DropDownUtil.dropDowns.put(DropDownUtil.GENDER, gender);
		
		
		HashMap<String, String> postType = new HashMap<String, String>();
		postType.put("DIR", "Director");
		postType.put("PRJM", "Project Manager");
		postType.put("TEAL", "Team Leader");
		postType.put("DEV", "Devoloper");
		DropDownUtil.dropDowns.put(DropDownUtil.POST_APPLIED_FOR, postType);
		
	
		HashMap<String, String> applicationStatus = new HashMap<String, String>();
		applicationStatus.put("NEW", "New");
		applicationStatus.put("SEL", "Selected");
		applicationStatus.put("OFF", "Offered");
		applicationStatus.put("HOL", "Hold");
		applicationStatus.put("REJ", "Rejected");
		DropDownUtil.dropDowns.put(DropDownUtil.APPLICANT_STATUS, applicationStatus);

		
		HashMap<String, String> EMPLOYEE_TYPE = new HashMap<String, String>();
		//EMPLOYEE_TYPE.put("SAD", "Site Admin");
		EMPLOYEE_TYPE.put("MGT", "Management");
		EMPLOYEE_TYPE.put("SLE", "Sales Executive");
		EMPLOYEE_TYPE.put("ACC", "Accountant");
		DropDownUtil.dropDowns.put(DropDownUtil.EMPLOYEE_TYPE, EMPLOYEE_TYPE);
		
		HashMap<String, String> MAIN_BRANCH = new HashMap<String, String>();
		MAIN_BRANCH.put("Y", "Yes");
		MAIN_BRANCH.put("N", "No");
		DropDownUtil.dropDowns.put(DropDownUtil.MAIN_BRANCH, MAIN_BRANCH);
		
		HashMap<String, String> BRANCH_TYPE = new HashMap<String, String>();
		BRANCH_TYPE.put("Y", "Main Branch");
		BRANCH_TYPE.put("N", "Sub Branch");
		DropDownUtil.dropDowns.put(DropDownUtil.BRANCH_TYPE, BRANCH_TYPE);
		
		
		
		HashMap<String, String> COMPONENT_RECURRENCE = new HashMap<String, String>();
		COMPONENT_RECURRENCE.put("mon", "Monthly");
		COMPONENT_RECURRENCE.put("term", "Term");
		COMPONENT_RECURRENCE.put("yer", "Yearly");
		DropDownUtil.dropDowns.put(DropDownUtil.COMPONENT_RECURRENCE, COMPONENT_RECURRENCE);
		
		//Adding drop down values for Allotment Type.
		HashMap<String, String> ALLOTMENT_TYPE = new HashMap<String, String>();
		//ALLOTMENT_TYPE.put("daily", "Daily");
		ALLOTMENT_TYPE.put("non-daily", "Non Daily");
		DropDownUtil.dropDowns.put(DropDownUtil.ALLOTMENT_TYPE, ALLOTMENT_TYPE);
		
		//Adding drop down values for Payment Type.
		
		HashMap<String, String> PAYMENT_TYPE = new HashMap<String, String>();
		PAYMENT_TYPE.put("Cash", "Cash");
		PAYMENT_TYPE.put("Cheque", "Cheque");
		DropDownUtil.dropDowns.put(DropDownUtil.PAYMENT_TYPE, PAYMENT_TYPE);

		//Adding dropdown values for Report Type.
		
		HashMap<String, String> REPORT_TYPE = new HashMap<String, String>();
		REPORT_TYPE.put("Daily", "Daily");
		REPORT_TYPE.put("Weekly", "Weekly");
		REPORT_TYPE.put("Monthly", "Monthly");
		DropDownUtil.dropDowns.put(DropDownUtil.REPORT_TYPE, REPORT_TYPE);
		
		
		
		
	}


	public static String getDropDown(String varCode, String key) {
		HashMap<String, String> varMap = DropDownUtil.dropDowns.get(varCode);
		String ret = "";
		if (varMap != null) {
			ret = varMap.get(key);
		}
		if (ret == null || ret.equals("")) {
			ret = key;
		}
		return ret;
	}

	public static HashMap<String, String> getDropDown(String varCode) {
		HashMap<String, String> varMap = DropDownUtil.dropDowns.get(varCode);
		if (varMap == null) {
			varMap = new HashMap<String, String>();
			;
		}
		return varMap;
	}
	

}
