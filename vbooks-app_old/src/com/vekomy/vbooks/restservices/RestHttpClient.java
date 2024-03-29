/**
 * 
 */
package com.vekomy.vbooks.restservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

/**
 * @author koteswararao
 * 
 */
public class RestHttpClient {
	
	public static final String TAG = "RestClient";
	private boolean authentication;
	private ArrayList<NameValuePair> headers;

	private String jsonBody;
	private String message;

	private ArrayList<NameValuePair> params;
	private String response;
	private int responseCode;

	private String url;

	// HTTP Basic Authentication
	private String username;
	private String password;

	protected Context context;

	public RestHttpClient(String url) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	// Be warned that this is sent in clear text, don't use basic auth unless
	// you have to.
	public void addBasicAuthentication(String user, String pass) {
		authentication = true;
		username = user;
		password = pass;
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void execute(RequestMethod method) throws Exception {
		switch (method) {
		case GET: {
			HttpGet request = new HttpGet(url + addGetParams());
			request = (HttpGet) addHeaderParams(request);
			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);
			request = (HttpPost) addHeaderParams(request);
			request = (HttpPost) addBodyParams(request);
			executeRequest(request, url);
			break;
		}
		case PUT: {
			HttpPut request = new HttpPut(url);
			request = (HttpPut) addHeaderParams(request);
			request = (HttpPut) addBodyParams(request);
			executeRequest(request, url);
			break;
		}
		case DELETE: {
			HttpDelete request = new HttpDelete(url);
			request = (HttpDelete) addHeaderParams(request);
			executeRequest(request, url);
		}
		}
	}

	private HttpUriRequest addHeaderParams(HttpUriRequest request)
			throws Exception {
		for (NameValuePair h : headers) {
			request.addHeader(h.getName(), h.getValue());
		}

		if (authentication) {

			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
					username, password);
			request.addHeader(new BasicScheme().authenticate(creds, request));
		}

		return request;
	}

	private HttpUriRequest addBodyParams(HttpUriRequest request)
			throws Exception {
		if (jsonBody != null) {
			request.addHeader("Content-Type", "application/json");
			if (request instanceof HttpPost)
				((HttpPost) request).setEntity(new StringEntity(jsonBody,
						"UTF-8"));
			else if (request instanceof HttpPut)
				((HttpPut) request).setEntity(new StringEntity(jsonBody,
						"UTF-8"));

		} else if (!params.isEmpty()) {
			if (request instanceof HttpPost)
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			else if (request instanceof HttpPut)
				((HttpPut) request).setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
		}
		return request;
	}

	private String addGetParams() throws Exception {
		// Using StringBuffer append for better performance.
		StringBuffer combinedParams = new StringBuffer();
		if (!params.isEmpty()) {
			combinedParams.append("?");
			for (NameValuePair p : params) {
				combinedParams.append((combinedParams.length() > 1 ? "&" : "")
						+ p.getName() + "="
						+ URLEncoder.encode(p.getValue(), "UTF-8"));
			}
		}
		return combinedParams.toString();
	}

	public String getErrorMessage() {
		return message;
	}

	public String getResponse() {
		return response;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setContext(Context ctx) {
		context = ctx;
	}

	public void setJSONString(String data) {
		jsonBody = data;
	}

	private void executeRequest(HttpUriRequest request, String url) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();

		// Setting 30 second timeouts
		HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
		HttpConnectionParams.setSoTimeout(params, 30 * 1000);

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}	
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}

/*
public void sendHttpPost() throws ClientProtocolException, IOException{
HttpPost httpPostRequest = new HttpPost(url + buildParams());

// add headers
Iterator&gt; it = headers.entrySet().iterator();
while (it.hasNext()) {
	Entry header = it.next();
	httpPostRequest.addHeader(header.getKey(), header.getValue());
}

HttpClient client = new DefaultHttpClient();
HttpResponse resp;

resp = client.execute(httpPostRequest);

this.respCode = resp.getStatusLine().getStatusCode();
Log.i(TAG, "response code: " + getResponseCode());
this.responsePhrase = resp.getStatusLine().getReasonPhrase();
Log.i(TAG, "error msg: " + getErrorMsg());
HttpEntity entity = resp.getEntity();

if (entity != null){
	InputStream is = entity.getContent();
	//Header contentEncoding = resp.getFirstHeader("Content-encoding");
	//Log.i(TAG, "endoding" + contentEncoding.getValue());
	response = convertStreamToString(is);
	//response = response.substring(1,response.length()-1);
	//response = "{" + response + "}";
	Log.i(TAG, "response: " + response);
	is.close();
}
}

public void callWebService(int method) throws IllegalStateException, ClientProtocolException, IOException {
switch (method) {
case GET:
	// build correct url
	HttpGet request = new HttpGet(url + buildParams());

	// add headers
	Iterator&gt; it = headers.entrySet().iterator();
	while (it.hasNext()) {
		Entry header = it.next();
		request.addHeader(header.getKey(), header.getValue());
	}

	// exec request
	HttpClient client = new DefaultHttpClient();
	HttpResponse resp;

	resp = client.execute(request);
	this.respCode = resp.getStatusLine().getStatusCode();
	this.responsePhrase = resp.getStatusLine().getReasonPhrase();

	HttpEntity entity = resp.getEntity();

	if (entity != null) {
		InputStream is = entity.getContent();
		response = this.convertStreamToString(is);
		response = response.substring(1,response.length()-1);
		response = "{" + response + "}";
		Log.i(TAG, "response: " + response);
		is.close();
	}

	break;
case POST:
	sendHttpPost();
}
}

public void createObjectJson() throws JSONException{
this.recvObj = new JSONArray(this.response);
Log.i(TAG, "json object" + recvObj.toString() + "/json object");
}
*/