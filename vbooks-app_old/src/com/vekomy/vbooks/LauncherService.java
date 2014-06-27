/**
 * com.vekomy.vbooks.LauncherService.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Launcher service to do background tasks
 * 
 * @author NKR
 * 
 */
public class LauncherService extends Service {

	private static final String LOG_TAG = "LauncherService";

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		LauncherService getService() {
			return LauncherService.this;
		}
	}

	private final IBinder binder = new LocalBinder();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOG_TAG, "LauncherService onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

}