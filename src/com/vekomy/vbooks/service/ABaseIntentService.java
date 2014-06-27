/**
 * com.vekomy.vbooks.service.ABaseIntentService.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 26-Aug-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.service;

import android.app.IntentService;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public abstract class ABaseIntentService  extends IntentService 
{
	public static String 	saleEmpId;
	public static String 	password;
	public static String 	orgId;
	
	public ABaseIntentService() 
	{
		super("SyncService");
	}	
	public ABaseIntentService(String name) 
	{
		super(name);
		saleEmpId 	= Utils.getData(VbookApp.getInstance(), Constants.USER_ID, Constants.NA);
		orgId 		= Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, "0");
	}
}