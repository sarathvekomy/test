/**
 * com.globems.stbclient.SplashScreenActivity.java
 *
 * This is proprietary work of Globems Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Globems Technologies.
 *
 * Author: NKR
 * Created : 12-Feb-2013
 */
package com.vekomy.vbooks.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.vekomy.vbooks.R;

/**
 * Activity class which displays the splash image for the specified duration and
 * starts off the MainActivity.
 * 
 * @author NKR
 * 
 */
public class SplashScreenActivity extends Activity {

	/**
	 * Tag constant to log the information.
	 */
	private static final String LOG_TAG = "SplashScreenActivity";

	/**
	 * Time duration for splash image to appear on the UI
	 */
	protected int _splashTime = 5000;

	/**
	 * Creating a thread for splash
	 */
	private Thread splashTread;
	
    //private DatabaseHelper databaseHelper;

	/**
	 * Called when the activity is first created. It uses splash.xml as its
	 * layout.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.d(LOG_TAG, "onCreate");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		//databaseHelper = new DatabaseHelper(this);
		final SplashScreenActivity sPlashScreen = this;

		// thread for displaying the SplashScreen
		splashTread = new Thread() 
		{
			@SuppressWarnings("finally")
			@Override
			public void run() 
			{
				try 
				{
					synchronized (this) 
					{
						wait(_splashTime);
					}

				} catch (InterruptedException e) {
				} finally 
				{
					// indicates activity is completed
					finish();
					
					// After the elapsed time starts the MainActivity
					final Intent i = new Intent();
					//databaseHelper.clearAuthKey();
					//String authKey = databaseHelper.getAuthKey();
/*					if(authKey == null || authKey.equalsIgnoreCase("")){
						i.setClass(sPlashScreen, LoginActivity.class);
					}else{
						i.setClass(sPlashScreen, MainActivity.class);
					}*/
					i.setClass(sPlashScreen, LoginActivity.class);
					startActivity(i);
					return;
				}
			}
		};
		// Starting the thread
		splashTread.start();
	}

	/**
	 * Handling touch event
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		try
		{
			if (event.getAction() == MotionEvent.ACTION_DOWN) 
			{
				synchronized (splashTread){splashTread.notifyAll();}
			}
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
}
