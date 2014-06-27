/**
 * com.vekomy.vbooks.VekomyClientApplication.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks;

import com.vekomy.vbooks.database.DatabaseHelper;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * The main Application class of the application as it is specified in the
 * <application> tag of AndroidManifest.xml file.
 * 
 * @author NKR
 * 
 */
public class VekomyClientApplication extends Application {

	/**
	 * Tag constant to log the information.
	 */
	private static final String LOG_TAG = "VekomyClientApplication";

	/**
	 * It launches the StbClientLauncher. Uses Intent.FLAG_ACTIVITY_NEW_TASK to
	 * start a service.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "VekomyClientApplication onCreate");
		setupDatabase();
		Intent intent = new Intent(Intent.CATEGORY_HOME);
		intent.setClass(this, VekomyClientLauncher.class);
		// Used to start a new service
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startService(intent);

	}

	/**
	 * Setting up database
	 */
	public void setupDatabase() {
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		// SQLiteDatabase db = dbHelper.getWritableDatabase();
	}
}
