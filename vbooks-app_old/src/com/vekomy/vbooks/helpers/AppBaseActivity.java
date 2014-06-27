/**
 * com.vekomy.vbooks.activities.AppBaseActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.helpers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Activity class which closes all the activities. Used for logout operation
 * 
 * @author NKR
 * 
 */
public abstract class AppBaseActivity extends Activity {

	/**
	 * The intent to finish all activities
	 */
	public static final String FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION = "com.vekomy.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";

	/**
	 * The Broadcast Receiver
	 */
	private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();

	/**
	 * The Intent filter
	 */
	public static final IntentFilter INTENT_FILTER = createIntentFilter();

	/**
	 * Creates FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION intent filter
	 * 
	 * @return
	 */
	private static IntentFilter createIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION);
		return filter;
	}

	/**
	 * Registers receiver
	 */
	protected void registerBaseActivityReceiver() {
		registerReceiver(baseActivityReceiver, INTENT_FILTER);
	}

	/**
	 * Unregisters receiver
	 */
	protected void unRegisterBaseActivityReceiver() {
		unregisterReceiver(baseActivityReceiver);
	}

	/**
	 * The BroadcastReceiver for FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION
	 * 
	 * 
	 */
	public class BaseActivityReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction()
					.equals(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION)) {
				finish();
			}
		}
	}

	/**
	 * Closes all the activities by sending a broadcast
	 */
	protected void closeAllActivities() {
		sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION));
	}
}