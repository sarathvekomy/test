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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VBookApp;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.restservices.RestTemplateFactory;
import com.vekomy.vbooks.utils.Utils;

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
			Toast.makeText(getApplicationContext(), "Error : Unable to load Screen your Device Memory full.", Toast.LENGTH_SHORT).show();
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
					validator.validate();
				}
				break;
			}// switch
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid selection", Toast.LENGTH_SHORT).show();
		}		
	}// onClick

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		try
		{
			// Handle the back button
			/*if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				android.content.DialogInterface.OnClickListener onYesclick = new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						LoginActivity.this.finish();
						System.exit(0);
						// Intent intent = new Intent(Intent.ACTION_MAIN);
						// intent.addCategory(Intent.CATEGORY_HOME);
						// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// startActivity(intent);
					}
				};
				// Ask the user if they want to quit
				//Utils.displayDialog(this, null, "Really Exit ?", Utils.DIALOG_YES_NO, "Yes", onYesclick, "No", null, null, null);
				return true;
			} 
			else
			{
				return super.onKeyDown(keyCode, event);
			}*/
			return super.onKeyDown(keyCode, event);
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid key press", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	// ***************************************
	// Private methods
	// ***************************************
	private void displayResponse(LoginResponse user)
	{
		try
		{
			if (user != null)
			{
				if(user.getStatusCode()==0)
				{
					// So login information is correct, we will save the Preference data
					// and redirect to next class / home
					//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					//SharedPreferences.Editor editor = settings.edit();
					//editor.putString("uid", String.valueOf(user.getUserId()));
					//editor.putString("Agent_Pswd", user.getPassword());
					//editor.putBoolean("IsRememberMe", check.isChecked());
					//editor.commit();
					VBookApp.userID =  user.getUserName();
					//VBookApp.password = user.getPassword();
					//VBookApp.Name	= user.getName();
					VBookApp.orgID	=	user.getOrganizationId();
					Toast.makeText(this, "Login to Home screen....", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
					System.gc();
					startActivity(intent);
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
				Toast.makeText(getApplicationContext(), "Some Notwork Error happened.", Toast.LENGTH_LONG).show();
			}
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid key press", Toast.LENGTH_SHORT).show();
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
				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				map.add("userName", username.getText().toString().trim());
				map.add("password",password.getText().toString().trim());
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);				
			    ResponseEntity<LoginResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, LoginResponse.class);
				if (responseEntity != null && responseEntity.hasBody())	
				{ 
					return responseEntity.getBody();
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
			dismissProgressDialog();
			displayResponse(result);
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
	public void onValidationCancelled() {
		
	}
}