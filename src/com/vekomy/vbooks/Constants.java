/**
 * com.vekomy.vbooks.Constants.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 06-Aug-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks;

import java.io.File;

import android.os.Environment;


/**
 * @author nkr
 *
 */
public class Constants 
{
	public static final String 	APP_VERSION_CODE				= "1.0";
	public static final String 	APP_VERSION_NAME				= "1.0";
	public static final int 	APP_VERSION_MISMATCH			= 	1;
	
	public static final String 	APP_SUCCESS_STRING				= 	"SUCCESS";
	public static final String 	APP_FAILED_STRING				= 	"FAILED";
	public static final int 	APP_SUCCESS						= 	0;
	public static final int 	APP_FAILED						= 	99;
	public static final int 	APP_AUTHENTIC_FAILED			=	100;
	public static final int 	APP_AUTHORIZE_FAILED			=	101;
	
	public static final String	PREFS_NAME 						=	"VBOOK";
	//public static final String	DAY_ID 							=	"DAY_ID";
	public static final String	USER_ID 						=	"SALES_EMP_ID";
	public static final String	USER_PWD 						=	"SALES_EMP_PWD";
	public static final String	MSG 							=	"MSG";
	public static final String	ORG_ID 							=	"ORG_ID";
	public static final String	ALLOTMENT_TYPE					=	"ALLOTMENT_TYPE";
	public static final String	PAYMENTS_TYPE					=	"PAYMENTS_TYPE";
	public static final String	JOURNALS_TYPE					=	"JOURNALS_TYPE";
	public static final String	GRANDED_DAYS					=	"GRANDED_DAYS";
	
	public static final String	P_CUSTID 						=	"CUSTID";
	public static final String	P_BNAME 						=	"BNAME";
	public static final String	P_INAME 						=	"INAME";
	public static final String	P_CAMT 							=	"CAMT";
	public static final String	P_AAMT 							=	"AAMT";
	
	public static final String	VIEW 							=	"V_";
	public static final String	MODIFY 							=	"M_";	
	public static final String	NEW 							=	"N_";
	public static final String	MODE							=	"MODE";
	
	public static final String	DELIVERY_NOTE					=	"DELIVERY NOTE";
	public static final String	SALES_RETURN 					=	"SALES RETURN";
	public static final String	JOURNAL 						=	"JOURNAL";
	public static final String	DAY_BOOK 						=	"DAY BOOK";
	public static final String	CYCLE_ID 						=	"CYCLE_ID";
	public static final String	IS_CYCLE_CLOSED 				=	"IS_CYCLE_CLOSED";
	
	
	public static final String	DN 								=	"DN_";
	public static final String	PY 								=	"PY_";
	public static final String	SR 								=	"SR_";
	public static final String	JN 								=	"JN_";
	public static final String	DB 								=	"DB_";
	public static final String	CR 								=	"_CR";
	
	public static final String	VIEW_TRANSACTION				=	"-- TRANSACTIONS --";
	public static final String	VIEW_DATE 						=	"-- DATE --";
	public static final String	VIEW_NAME 						=	"-- NAME --";
	public static final String	NA 								=	"-- NA --";
	public static final String	SALES_BOOK_SEPARATOR			=	"/";
	
	public static final String	SALES_REF_ID					=	"SALES_REF_ID";
	
	public static final String	BILL_IMG_ID						=	"BILL_IMG";
	
	public static final String	URL				           		=	"URL";	
	public static final String	APKNAME							=	"vbooks-app-release.apk";
	public static final String	DOWNLOAD_PATH					=	Environment.getExternalStorageDirectory().getPath().toString()+File.separator+"appDownload/";
	
	public final static String ANDROID_PACKAGE 				= 	"application/vnd.android.package-archive";
	public final static String PACKGENAME					= 	"package:com.vekomy.vbooks";
}
