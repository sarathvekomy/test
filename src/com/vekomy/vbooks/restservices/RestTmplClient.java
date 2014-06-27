/**
 * 
 */
package com.vekomy.vbooks.restservices;

import java.nio.charset.Charset;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import org.springframework.http.client.ClientHttpResponse;

/**
 * @author koteswararao
 *
 */
public class RestTmplClient {
	
	protected static final String TAG = RestTmplClient.class.getSimpleName();
	
	private static RestTemplate instance = null;
	
	protected RestTmplClient(){
		// Exists only to defeat instantiation.
	}

	public static RestTemplate getInstance(Context context)
	{
		// connection cheking...................
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				int i = 0;
				for (i=0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						break;
					}
				}
				if(i== info.length)
				{
					//android.content.DialogInterface.OnClickListener onclicklist = new android.content.DialogInterface.OnClickListener() 
					//{						
					//	public void onClick(DialogInterface arg0, int arg1) 
					//	{							
					//	}
					//};
					//Utils.displayDialog(context,null,"Internet is Not Reachable or Signals not available",Utils.DIALOG_OK,"Ok",onclicklist,null,null,null,null);
					return null;
				}
			}
			else
			{
				//android.content.DialogInterface.OnClickListener onclicklist = new android.content.DialogInterface.OnClickListener() 
				//{						
				//	public void onClick(DialogInterface arg0, int arg1) 
				//	{							
				//	}
				//};
				//Utils.displayDialog(context,null,"Internet is Not Reachable or Signals not available",Utils.DIALOG_OK,"Ok",onclicklist,null,null,null,null);
				return null;
			}
		}
		
		if (instance == null)
		{

			//HttpComponentsClientHttpRequestFactory fact = new HttpComponentsClientHttpRequestFactory();
			
			// protected HttpUriRequest createHttpRequest(HttpMethod httpMethod, URI uri)
			// { HttpUriRequest uriRequest = super.createHttpRequest(httpMethod, uri); // Add request headers
			// uriRequest.addHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
			// return uriRequest; }
			// public ClientHttpRequest createRequest(URI uri,HttpMethod httpMethod) throws IOException 
			// { if (Config.DEBUG) {
			// Log.d(Constant.LOG_TAG, uri.getPath()); } return
			// super.createRequest(uri, httpMethod); } };

			/*ResponseErrorHandler errorHandler = new ResponseErrorHandler()
			{
				public boolean hasError(ClientHttpResponse resp) throws IOException
				{
					org.springframework.http.HttpStatus status = resp.getStatusCode();
					if (HttpStatus.CREATED.equals(status) || HttpStatus.OK.equals(status))
					{
						return false;
					} 
					else
					{
						Log.d(TAG, "response: " + resp.getBody());
						return true;		}
				}

				public void handleError(ClientHttpResponse resp) throws IOException
				{
					throw new HttpClientErrorException(resp.getStatusCode());
				}
			};*/

			//instance = new RestTemplate(fact);
			instance = new RestTemplate();

			FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
			formConverter.setCharset(Charset.forName("UTF8"));
			instance.getMessageConverters().add(formConverter);
			instance.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			//instance.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			//instance.setErrorHandler(errorHandler);
		}
		return instance;
	}
}
