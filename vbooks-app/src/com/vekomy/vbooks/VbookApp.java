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

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vekomy.vbooks.database.DBHelper;

/**
 * The main Application class of the application as it is specified in the
 * <application> tag of AndroidManifest.xml file.
 * 
 * @author NKR
 * 
 */
public class VbookApp extends Application 
{
	public static String 	USERID;
	public static String 	ORGID;
	/**
	 * Tag constant to log the information.
	 */
	private static final String LOG_TAG = "vbooks-App";

	private static SQLiteDatabase 	dbforServices;
	private static SQLiteDatabase 	db;
	private static DBHelper 		dbHelper;
	
	private static VbookApp instance = null;
	
    public static Context getInstance()
    {
    	try
    	{
    		synchronized (VbookApp.class) {
    			if (instance == null) {
    				return instance.getApplicationContext();
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		//Toast.makeText(this, "Error : Unable to load Screen your Device Memory full.", Toast.LENGTH_SHORT).show();
    	}
		return instance;    	
    }
    
    public static SQLiteDatabase getDbInstance()
    {
        if ( db == null)
        {
        	synchronized (VbookApp.class) 
        	{
                if (db == null) 
                {
            		db = dbHelper.getWritableDatabase();
                }
            }
        }
        return db;
    }
    public static SQLiteDatabase getDbforServices()
    {
        if ( dbforServices == null)
        {
        	synchronized (VbookApp.class) 
        	{
                if (dbforServices == null) 
                {
                	dbforServices = dbHelper.getWritableDatabase();
                }
            }
        }
        return db;
    }
	/**
	 * It launches the StbClientLauncher. Uses Intent.FLAG_ACTIVITY_NEW_TASK to
	 * start a service.
	 */
	@Override
	public void onCreate() 
	{
		super.onCreate();
		instance = this;
		Log.d(LOG_TAG, "Vbook Android Application onCreate");
		setupDatabase();
		Intent intent = new Intent(Intent.CATEGORY_HOME);
		intent.setClass(this, AppLauncher.class);
		// Used to start a new service
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		startService(intent);
	}
	/**
	 * Setting up database
	 */
	public void setupDatabase() 
	{
		//DatabaseHelper dbHelper = new DatabaseHelper(this);
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper = new DBHelper(this);
		db = dbHelper.getWritableDatabase();
	}
}