/**
 * com.vekomy.vbooks.activities.ProgressActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.helpers;

import android.app.ProgressDialog;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Shows the progress dialog.
 * 
 * @author NKR
 * 
 */
public abstract class ProgressActivity extends AppBaseActivity 
{

	protected static final String TAG = ProgressActivity.class.getSimpleName();

	/**
	 * The progress dialog
	 */
	private ProgressDialog progressDialog;

	private ProgressDialog progressBar;

	private boolean destroyed = false;

	/**
	 * Called when the activity is destroyed
	 */
	@Override
	protected void onDestroy() 
	{
		try
		{
			super.onDestroy();
			destroyed = true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Shows the progress dialog with the default data
	 */
	public void showLoadingProgressDialog() 
	{
		try
		{
			this.showProgressDialog("Loading. Please wait...");
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		
	}
	public void showDownlodingProgressBar()
	{
		try { this.showProgressBar(0);}catch(Exception e){}
	}
	/**
	 * Shows the progress dialog with the specified message
	 * @param message
	 */
	public void showProgressDialog(CharSequence message) 
	{
		try
		{
			if (progressDialog == null) 
			{
				progressDialog = new ProgressDialog(this);
				progressDialog.setIndeterminate(true);
			}
			progressDialog.setMessage(message);
			progressDialog.show();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void showProgressBar(Integer... progress)
	{
		try
		{			
			if (progressBar == null)
			{
				progressBar = new ProgressDialog(this);
				progressBar.setCancelable(false);
				progressBar.setProgress(0);
				progressBar.setMax(100);
				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressBar.setMessage("Downloading.... Please wait...");
				progressBar.show();
			}
			progressBar.setProgress(progress[0]);
		}catch(Exception e){}
	}
	/**
	 * Dismisses the progress dialog
	 */
	public void dismissProgressDialog() 
	{
		try
		{
			if (progressDialog != null && !destroyed)	{progressDialog.dismiss();}
			if (progressBar != null && !destroyed)	{progressBar.dismiss();}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			// Handle the back button
			if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH)
			{
				return true;
			} 
			else
			{
				return super.onKeyDown(keyCode, event);
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return true;
	}
}
