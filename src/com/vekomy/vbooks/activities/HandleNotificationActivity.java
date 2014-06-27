/**
 * com.vekomy.vbooks.activities.HandleNotificationActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 03-Oct-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.activities;

import com.vekomy.vbooks.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author nkr
 *
 */
public class HandleNotificationActivity  extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.handle_notification_activity);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}