package com.vekomy.vbooks.restservices;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;

/**
 * @author Bhavan
 * 
 */
public class RestTemplateFactory
{
	
	protected static final String TAG = RestTemplateFactory.class.getSimpleName();
	
	private static RestTemplate instance = null;
	
	protected RestTemplateFactory()
	{
		// Exists only to defeat instantiation.
	}

	public static RestTemplate getInstance(Context context)
	{
		//if (instance == null)
		{

			HttpComponentsClientHttpRequestFactory fact = new HttpComponentsClientHttpRequestFactory();
			
			// protected HttpUriRequest createHttpRequest(HttpMethod httpMethod, URI uri)
			// { HttpUriRequest uriRequest = super.createHttpRequest(httpMethod, uri); // Add request headers
			// uriRequest.addHeader("Content-Type",MediaType.APPLICATION_JSON_VALUE);
			// return uriRequest; }
			// public ClientHttpRequest createRequest(URI uri,HttpMethod httpMethod) throws IOException 
			// { if (Config.DEBUG) {
			// Log.d(Constant.LOG_TAG, uri.getPath()); } return
			// super.createRequest(uri, httpMethod); } };
			fact.setReadTimeout(1000);
			ResponseErrorHandler errorHandler = new ResponseErrorHandler()
			{
				public boolean hasError(ClientHttpResponse resp) throws IOException
				{
					HttpStatus status = resp.getStatusCode();
					if (HttpStatus.CREATED.equals(status) || HttpStatus.OK.equals(status))
					{
						return false;
					} 
					else
					{
						Log.d(TAG, "response: " + resp.getBody());
						return true;		
					}
				}

				public void handleError(ClientHttpResponse resp) throws IOException
				{
					throw new HttpClientErrorException(resp.getStatusCode());
				}
			};

			instance = new RestTemplate(fact);
			//instance = new RestTemplate();

			FormHttpMessageConverter formConverter = new FormHttpMessageConverter();
			formConverter.setCharset(Charset.forName("UTF8"));
			instance.getMessageConverters().add(formConverter);
			//instance.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			instance.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
			instance.setErrorHandler(errorHandler);
		}
		return instance;
	}
}

