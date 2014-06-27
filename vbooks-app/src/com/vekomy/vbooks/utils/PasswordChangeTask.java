/**
 * com.vekomy.vbooks.utils.PasswordChangeTask.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 09-Oct-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.PasswordChangeRequest;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.restservices.RestTemplateFactory;

/**
 * @author nkr
 *
 */
public class PasswordChangeTask extends AsyncTask<PasswordChangeRequest, Void, Response>
{
	Context context;
	Dialog	dialog;
	PasswordChangeRequest request;
	PasswordChangeTask(Context context,Dialog	dialog)
	{
		this.context 	=	context;
		this.dialog		=	dialog;
	}
	protected void onPreExecute()
	{
		((ProgressActivity) context).showProgressDialog("Password Changing Please wait...");
	}
	
	@Override
	protected Response doInBackground(PasswordChangeRequest... params)
	{
		try
		{
			RestTemplate restTemplate = RestTemplateFactory.getInstance(context);
			if (restTemplate == null)
			{
				((ProgressActivity) context).showProgressDialog("Network Connection Error...");
				((ProgressActivity) context).dismissProgressDialog();
				Utils.displayDialog(context,null,"Internet is Not Reachable or Signals not available",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				return null;
			}
			request = params[0];
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			final String url = context.getString(R.string.BASE_URL) + "passwordchange";
			return restTemplate.postForObject(url, new HttpEntity<PasswordChangeRequest>(request, headers), Response.class);
		} 
		catch (RestClientException  e)
		{
			e.printStackTrace();
		} 
		catch (Exception scx)
		{
			scx.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Response response)
	{
		try
		{
			((ProgressActivity) context).dismissProgressDialog();
			if (response != null)
			{
				if(response.getStatusCode()==200)
				{
					Utils.displayDialog(context,null,"Password change successfully.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
					Utils.saveData(context, Constants.USER_PWD,request.getNewPassword());
				}
				else
					Utils.displayDialog(context,null,response.getMessage(),Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				dialog.dismiss();
			}
			else
			{
				Utils.displayDialog(context,null,"Password change Failed,Due to some Network Error.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
