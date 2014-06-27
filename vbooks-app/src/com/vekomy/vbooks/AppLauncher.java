/**
 * com.vekomy.vbooks.VekomyClientLauncher.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks;

import com.vekomy.vbooks.activities.SplashScreenActivity;
import com.vekomy.vbooks.service.LauncherService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

/**
 * This class starts the SplashScreenActivity and it gets invoked by the
 * VekomyClientApplication when application starts up. It is associated with Intent
 * as HOME, when home button is pressed it displays the Launch option.
 * 
 * @author NKR
 * 
 */
public class AppLauncher extends Activity
{

	/**
	 * Tag constant to log the information.
	 */
	private static final String LOG_TAG = "vbook-app Launcher";
	
	private ServiceConnection launcherServiceConnection;
	
	private LauncherService launcherService;

	/**
	 * Called when the activity is first created. This internally starts
	 * SplashScreenActivity.
	 */
	@Override
	public void onCreate(Bundle bundle) 
	{
		super.onCreate(bundle);
		Log.d(LOG_TAG, "vbook-app onCreate");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// Starts the SplashScreen Activity when home launcher is invoked
		//connectToLauncherService();
		startActivity(new Intent(this, SplashScreenActivity.class));
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		disconnectFromLauncherService();
	}

	/**
	 * Connect to the background service.
	 */
	private void connectToLauncherService() 
	{
		launcherServiceConnection = new ServiceConnection() 
		{
			public void onServiceConnected(ComponentName name, IBinder service) 
			{
				Log.d(LOG_TAG, "onServiceConnected");
				launcherService = ((LauncherService.LocalBinder) service).getService();
			}
			public void onServiceDisconnected(ComponentName name) {
				Log.d(LOG_TAG, "onServiceDisconnected");
				launcherService = null;
			}
		};
		if (launcherServiceConnection != null) 
		{
			Intent intent = new Intent(AppLauncher.this, LauncherService.class);
			bindService(intent, launcherServiceConnection, BIND_AUTO_CREATE);
		}
	}

	/**
	 * Close the connection to the background service.
	 */
	private synchronized void disconnectFromLauncherService() 
	{
		if (launcherServiceConnection != null) 
		{
			unbindService(launcherServiceConnection);
			launcherServiceConnection = null;
		}
		launcherService = null;
	}

}
