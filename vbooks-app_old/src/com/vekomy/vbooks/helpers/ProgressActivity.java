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

/**
 * Shows the progress dialog.
 * 
 * @author NKR
 * 
 */
public abstract class ProgressActivity extends AppBaseActivity {

	protected static final String TAG = ProgressActivity.class
			.getSimpleName();

	/**
	 * The progress dialog
	 */
	private ProgressDialog progressDialog;

	private boolean destroyed = false;

	/**
	 * Called when the activity is destroyed
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
	}

	/**
	 * Shows the progress dialog with the default data
	 */
	public void showLoadingProgressDialog() {
		this.showProgressDialog("Loading. Please wait...");
	}

	/**
	 * Shows the progress dialog with the specified message
	 * @param message
	 */
	public void showProgressDialog(CharSequence message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
		}

		progressDialog.setMessage(message);
		progressDialog.show();
	}

	/**
	 * Dismisses the progress dialog
	 */
	public void dismissProgressDialog() {
		if (progressDialog != null && !destroyed) {
			progressDialog.dismiss();
		}
	}

}
