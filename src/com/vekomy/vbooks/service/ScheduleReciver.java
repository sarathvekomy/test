/**
 * com.vekomy.vbooks.activities.AlaramReciver.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 22-Aug-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author nkr
 *
 */
public class ScheduleReciver  extends BroadcastReceiver
{

	protected static final String TAG = ScheduleReciver.class.getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) 
	{	
		try
		{
			Log.d("ScheduleReciver", "Schedule Reciver.");
	    	Intent serviceIntent = new Intent(context,SyncService.class);
	    	context.startService(serviceIntent);    	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			Log.e(TAG, ex.getLocalizedMessage(), ex);
		}
	}

}
