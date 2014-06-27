/**
 * com.vekomy.vbooks.activities.LoginActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.GregorianCalendar;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.vekomy.validation.Rule;
import com.vekomy.validation.Validator;
import com.vekomy.validation.Validator.ValidationListener;
import com.vekomy.validation.annotations.Required;
import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.LoginRequest;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.restservices.RestTemplateFactory;
import com.vekomy.vbooks.service.ScheduleReciver;
import com.vekomy.vbooks.utils.Utils;


/**
 * @author Nkr
 *
 */
public class LoginActivity extends ProgressActivity implements OnClickListener,ValidationListener
{
	
	/**
	 * Tag constant to log the information.
	 */
	protected static final String TAG = LoginActivity.class.getSimpleName();
	
	/**
	 * The Context
	 */
	private Context context;

	/**
	 * The validator
	 */
	private Validator validator;

	/**
	 * The username
	 */
	@Required(order = 1, message = "Username is required")
	private EditText username;

	/**
	 * The password
	 */
	@Required(order = 2, message = "Password is required")
	private EditText password;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			// remove title
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.login_screen);
			this.context = this;
			
			// Retrieving the fields
			username = (EditText) findViewById(R.id.txUsername);
			password = (EditText) findViewById(R.id.txpaswd);			
			final Button btnLogin = (Button) findViewById(R.id.btnLogin);
			validator = new Validator(this);
			validator.setValidationListener(this);
			btnLogin.setOnClickListener(this);					
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void onClick(View view)
	{
		try
		{		
			switch (view.getId())
			{
				case R.id.btnLogin:
				{
					//displayPasswordDialog();
					validator.validate();
				}
				break;
			}// switch
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}// onClick
	

	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			// Handle the back button
			if (keyCode == KeyEvent.KEYCODE_BACK)
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

	// ***************************************
	// Private methods
	// ***************************************
	private void displayResponse(final LoginResponse user)
	{
		try
		{
			if (user != null)
			{
				if(user.getStatusCode()==200)
				{
					PackageInfo pInfo = null;
					 try 
					 {
						 pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
					 } catch (NameNotFoundException e) {}
					 catch (Exception e) {}
					String	versionName = pInfo!=null?pInfo.versionName:null;
					//Integer versionCode = pInfo.versionCode;
					
					if(versionName !=null && versionName.compareToIgnoreCase(user.getVersionName()) < 0)
					{
						android.content.DialogInterface.OnClickListener onYesclick = new android.content.DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog,int which) {
								new DownloadFile().execute();
								return;
							}
						};
						android.content.DialogInterface.OnClickListener onNoclick = new android.content.DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog,int which) {
								onSuccessLogin(user);
							}
						};
						if(user.isForceupdate())
						{
							Utils.displayDialog(this, null, "New version of vbooks found,update Now.", Utils.DIALOG_OK, "Ok", onYesclick, null, null, null, null);
							return;
						}
						else
						{
							Utils.displayDialog(this, null, "New Version Of vbooks has found do you want to update", Utils.DIALOG_YES_NO, "Yes", onYesclick,"No", onNoclick, null, null);
							return;
						}
					}
					onSuccessLogin(user);
				}
				else if(user.getStatusCode()==1)
				{
					Toast.makeText(this, "Given user ID not Authorized for this Application", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Plz.... Enter Valid UserName and Password", Toast.LENGTH_SHORT).show();
					username.getText().clear();
					password.getText().clear();
					
				}
			} 
			else
			{
				String user_id = Utils.getData(getApplicationContext(), Constants.USER_ID, Constants.NA);
				String user_pwd= Utils.getData(getApplicationContext(), Constants.USER_PWD, Constants.NA);
				if(user_id.equals(username.getText().toString().trim()) && user_pwd.equals(password.getText().toString().trim()))
				{
					Intent alaramIntent = new Intent(getApplicationContext(),ScheduleReciver.class);
					Long time = new GregorianCalendar().getTimeInMillis()+10*1000;
					AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
					manager.setRepeating(AlarmManager.RTC_WAKEUP, time, 300000,  PendingIntent.getBroadcast(getApplicationContext(), 1, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT));
					password.getText().clear();
					Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
					System.gc();
					password.getText().clear();
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Plz.... Enter Valid UserName and Password", Toast.LENGTH_SHORT).show();
					password.getText().clear();
				}
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	void onSuccessLogin(LoginResponse user)
	{
		Utils.saveData(this, Constants.USER_ID, user.getUserName());
		Utils.saveData(this, Constants.ORG_ID, String.valueOf(user.getOrganizationId()));
		Utils.saveData(this, Constants.PAYMENTS_TYPE, Constants.NA + "," + user.getPaymentTypes());
		Utils.saveData(this, Constants.JOURNALS_TYPE, user.getJournalTypes());
		if(user.getAllotmentType().trim().length()!=0)
			Utils.saveData(this, Constants.ALLOTMENT_TYPE,user.getAllotmentType());					
		Utils.saveData(this, Constants.GRANDED_DAYS,user.getGrandedDays());
		if(user.isFirstTimeLogin())
		{
			Utils.displayPasswordChangeDialog(LoginActivity.this,password.getText().toString().trim());
			password.getText().clear();
		}
		else
		{
			Utils.saveData(this, Constants.USER_PWD, password.getText().toString().trim());
			Intent alaramIntent = new Intent(getApplicationContext(),ScheduleReciver.class);
			Long time = new GregorianCalendar().getTimeInMillis()+10*1000;
			AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
			manager.setRepeating(AlarmManager.RTC_WAKEUP, time, 300000,  PendingIntent.getBroadcast(getApplicationContext(), 1, alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT));						
			Toast.makeText(getApplicationContext(), "Services Was started Successfully.",Toast.LENGTH_LONG).show();
			password.getText().clear();
			Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
			System.gc();
			startActivity(intent);
		}
	}
	public void InstallApplication(boolean result)
	{
		try
		{
			if(result)
			{
				boolean isNonPlayAppAllowed = false;
				if (Build.VERSION.SDK_INT < 17) 
				{
					isNonPlayAppAllowed = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0) == 1;
			    } 
				else 
				{
			    	isNonPlayAppAllowed = Settings.Global.getInt(this.getContentResolver(), Settings.Global.INSTALL_NON_MARKET_APPS, 0) == 1;
			    }
				//if(isNonPlayAppAllowed)
				{
			        Uri packageURI = Uri.parse(Constants.PACKGENAME);
			        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, packageURI);
			        intent.setDataAndType(Uri.fromFile(new File(Constants.DOWNLOAD_PATH + Constants.APKNAME)),Constants.ANDROID_PACKAGE);
			        startActivity(intent);			       
				}
			}
			else
			{
				Utils.displayDialog(this,null,"Downloading New Version Failed.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.getMessage());
			Toast.makeText(this, "Error : Install Application Failed.", Toast.LENGTH_LONG).show();
		}		
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class LoginTask extends AsyncTask<Void, Void, LoginResponse>
	{
		protected void onPreExecute()
		{
			showProgressDialog("Login.... to Sales Execute A/c. Please wait...");
		}
		
		@Override
		protected LoginResponse doInBackground(Void... params)
		{
			try
			{
				RestTemplate restTemplate = RestTemplateFactory.getInstance(getApplicationContext());
				if (restTemplate == null)
				{
					showProgressDialog("Network Connection Error...");
					dismissProgressDialog();
					Utils.displayDialog(context,null,"Internet is Not Reachable or Signals not available",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
					return null;
				}
				final String url = getString(R.string.BASE_URL) + "LoginAuth";
				Log.d(TAG, url);
				LoginRequest login = new LoginRequest();
				login.setUserName(username.getText().toString().trim());
				login.setPassword(password.getText().toString().trim());
				//MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				//map.add("userName", username.getText().toString().trim());
				//map.add("password",password.getText().toString().trim());
				
				PackageInfo pInfo = null;
				 try 
				 {
					 pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				 } catch (NameNotFoundException e) {}
				 catch (Exception e) {}
				String	versionName = pInfo!=null?pInfo.versionName:"";
				login.setVersion(versionName);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				LoginResponse response = restTemplate.postForObject(url, new HttpEntity<LoginRequest>(login, headers), LoginResponse.class);
				if (response != null)	
				{ 
					return response;
				}
			} 
			catch (RestClientException  e)
			{
				e.printStackTrace();
				Log.e(TAG, e.getLocalizedMessage(), e);
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return null;
		}

		protected void onPostExecute(LoginResponse result)
		{
			try
			{
				dismissProgressDialog();
				displayResponse(result);			
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	private class DownloadFile  extends AsyncTask<Void, Integer, Boolean>
	{
		final String f_url = getString(R.string.APP_DOWNLOAD_URL) + Constants.APKNAME;

		public DownloadFile()
		{
		}	    	
		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			showDownlodingProgressBar();
		}
	 
	    /**
	     * Downloading file in background thread
	     * */
	    @Override
	    protected Boolean doInBackground(Void... params) 
	    {
	        try 
	        {
	        	int count;
	        	URL url = new URL(f_url);
	        	HttpURLConnection conection = (HttpURLConnection) url.openConnection();
	            conection.setRequestMethod("GET");
	            conection.setDoOutput(true);                         
	            conection.connect(); // Connection Complete here.!
	            
	            // Output stream
	            File file = new File(Constants.DOWNLOAD_PATH); // PATH = /mnt/sdcard/download/
	            //File file = new File(context.getFilesDir().getPath().toString()+File.separator+"appDownload/");
	            
	            if (!file.exists()) 
	            {
	                file.mkdirs();
	            }
	            
	            InputStream input = conection.getInputStream(); // Get from Server and Catch In Input Stream Object.
	            // this will be useful so that you can show a tipical 0-100% progress bar
	            int lenghtOfFile = conection.getContentLength(); 	            

	            File outputFile = new File(file, Constants.APKNAME);
	            FileOutputStream output = new FileOutputStream(outputFile);           
	            
	            byte data[] = new byte[1024];
	            long total = 0;
	 
	            while ((count = input.read(data)) != -1) 
	            {
	            	total += count;
	            	// publishing the progress....
	                // After this onProgressUpdate will be called
	            	publishProgress((int)((total*100)/lenghtOfFile));
	                // writing data to file
	            	output.write(data, 0, count);
	            }
	 
	            // flushing output
	            output.flush();
	            // closing streams
	            output.close();
	            input.close();
	            return true;
	        }
	        catch (ParseException e) {
	        	//			e.printStackTrace();
	        	Log.e(TAG, e.getMessage());
	        } catch (ClientProtocolException e) {
	        	//			e.printStackTrace();
	        	Log.e(TAG, e.getMessage());
	        } catch (IOException e) {
	        	//			e.printStackTrace();
	        	Log.e(TAG, e.getMessage());
	        } 
	        catch (Exception e) {
	        	Log.e("Error: ", e.getMessage());
	        }          
	        return false;
	    }
	 
	    /**
	     * Updating progress bar
	     * */
	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        // setting progress percentage
	    	showProgressBar(progress[0]);        
	   }
	 
		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * dismiss the dialog after the file was downloaded
		 * **/
		@Override
		protected void onPostExecute(Boolean result) 
		{			
			dismissProgressDialog();
			InstallApplication(result);
		}
		
	}

	@Override
	public void preValidation() {
	}

	@Override
	public void onValidationSuccess() {
		new LoginTask().execute();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText || failedView instanceof RadioGroup) {
			failedView.requestFocus();
			((TextView) failedView).setError(message);
		}
		else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onValidationCancelled() {}
}