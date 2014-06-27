/**
 * com.vekomy.vbooks.BootReceiver.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: nafees
 * Created: Jun 8, 2013
 */
package com.vekomy.vbooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Boot receiver to launch service after device boots up
 * 
 * @see manifest: android.intent.action.BOOT_COMPLETED
 * 
 * @author nafees
 * 
 */
public class BootReceiver extends BroadcastReceiver {
	private static final String LOG_CAT = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_CAT, "bootReciever.onReciever");
		try {
			context.startService(new Intent(context, LauncherService.class));
		} catch (Exception e) {
			Log.e(LOG_CAT, "onReceive", e);
		}
	}
}