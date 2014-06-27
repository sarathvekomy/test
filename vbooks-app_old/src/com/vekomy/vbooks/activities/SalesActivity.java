/**
 * com.vekomy.vbooks.activities.SalesActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks.activities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.validation.Rule;
import com.vekomy.validation.Validator;
import com.vekomy.validation.Validator.ValidationListener;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VBookApp;
import com.vekomy.vbooks.activities.ProductsActivity.ProductListTask;
import com.vekomy.vbooks.adapters.CustmourAdapter;
import com.vekomy.vbooks.adapters.ProductsAdapter;
import com.vekomy.vbooks.app.request.CustomerCrRequest;
import com.vekomy.vbooks.app.request.DeliveryNoteProducts;
import com.vekomy.vbooks.app.response.CustmourListResponse;
import com.vekomy.vbooks.app.response.CustomerCrResponse;
import com.vekomy.vbooks.app.response.DeliveryNoteResponse;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.app.response.ProductListResponse;
import com.vekomy.vbooks.helpers.AddCustomerHelper;
import com.vekomy.vbooks.helpers.ProductParcelable;
import com.vekomy.vbooks.restservices.RestTemplateFactory;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author koteswararao
 *
 */
public class SalesActivity extends AddCustomerHelper implements OnClickListener,OnTabChangeListener,ValidationListener{
	
	private ListView mlvCustlist;
	private CustmourAdapter mCustAdapter;
	private Button 	mBtnDisplay;
	
	private View 	modifyCustmourView;
	private View 	custmourListView;
	/**
	 * The validator
	 */
	private Validator validator;
	/**
	 * The Context
	 */
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_module);
		this.context = this;
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec invoice = tabHost.newTabSpec("Invoice");
		invoice.setIndicator("Invoice", getResources().getDrawable(R.drawable.invoices));
		invoice.setContent(R.id.rlDeliveryNoteView);
		
		TabSpec paymentCollection = tabHost.newTabSpec("PaymentCollection");
		paymentCollection.setIndicator("Payment Collection", getResources().getDrawable(R.drawable.paymentcollection));
		paymentCollection.setContent(R.id.rlDeliveryNoteView);

		TabSpec stockReturns = tabHost.newTabSpec("StockReturns");
		stockReturns.setIndicator("Stock Returns", getResources().getDrawable(R.drawable.stockreturn));
		stockReturns.setContent(R.id.rlDeliveryNoteView);

		TabSpec modifyCustomerInfo = tabHost.newTabSpec("ModifyCustomerInfo");
		modifyCustomerInfo.setIndicator("Modify Customer Info", getResources().getDrawable(R.drawable.modify_customer));
		modifyCustomerInfo.setContent(R.id.rlDeliveryNoteView);

		TabSpec newCustomer = tabHost.newTabSpec("NewCustomer");
		newCustomer.setIndicator("New Customer", getResources().getDrawable(R.drawable.add_customer));
		newCustomer.setContent(R.id.llAddCustView);

		tabHost.addTab(invoice);
		tabHost.addTab(paymentCollection);
		tabHost.addTab(stockReturns);
		tabHost.addTab(modifyCustomerInfo);
		tabHost.addTab(newCustomer);		
		tabHost.setOnTabChangedListener(this);
		tabHost.setCurrentTab(0);
		mBtnDisplay = (Button) findViewById(R.id.btnDisplay);
		mBtnDisplay.setOnClickListener(this);
		new CustomerListTask().execute();
		mlvCustlist = (ListView) findViewById(R.id.lvcustlist);
		initializeFields();
		validator = new Validator(this);
		validator.setValidationListener(this);
		findViewById(R.id.btnSubmit).setOnClickListener(this);
		findViewById(R.id.btnClear).setOnClickListener(this);
		custmourListView 	= findViewById(R.id.rlDeliveryNoteView);
		modifyCustmourView	= findViewById(R.id.llModifyCustView);
	}
	
	// ***************************************
	// Private classes http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	// ***************************************
	class CustomerListTask extends AsyncTask<Void, Void, CustmourListResponse>
	{
		final String url = getString(R.string.BASE_URL) + "customerlist?uname={uname}&orgID={orgID}";
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of Assign customer  ..... Please wait...");
		}
		
		@Override
		protected CustmourListResponse doInBackground(Void... params)
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
				Log.d(TAG, url);				
			    ResponseEntity<CustmourListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(new HttpHeaders()), CustmourListResponse.class,
			    		VBookApp.userID,VBookApp.orgID);			    
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

		protected void onPostExecute(CustmourListResponse result)
		{
			dismissProgressDialog();
			if(result!=null)
			{
				mCustAdapter = new CustmourAdapter(context, R.layout.customer_list_row,result.getCustmourList());
				mlvCustlist.setAdapter(mCustAdapter);				
			}
			if(mCustAdapter.mCustInfoList.size()==0)
				mBtnDisplay.setEnabled(false);
		}

	}	
	// ***************************************
	// Private classes http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	// ***************************************
	class CustomerCRTask extends AsyncTask<Void, Void, CustomerCrResponse>
	{
		final String url = getString(R.string.BASE_URL) + "customerlist?uname={uname}&orgID={orgID}";
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of Assign customer  ..... Please wait...");
		}
		
		@Override
		protected CustomerCrResponse doInBackground(Void... params)
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
				final String url = getString(R.string.BASE_URL) + "custReq";
				Log.d(TAG, url);
				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				map.add("businessName", businessName.getText().toString().trim());
				map.add("customerName",customerName.getText().toString().trim());
				map.add("gender",String.valueOf(male.isChecked()==true?'M':'F'));
				map.add("invoiceName",invoiceName.getText().toString().trim());
				map.add("addressLine1",address1.getText().toString().trim());
				map.add("addressLine2",address2.getText().toString().trim());
				map.add("region",region.getText().toString().trim());
				map.add("locality",locality.getText().toString().trim());
				map.add("landmark",landmark.getText().toString().trim());
				map.add("city",city.getText().toString().trim());
				map.add("state",state.getText().toString().trim());
				map.add("zipcode",zipcode.getText().toString().trim());
				map.add("addressType",addressType.getText().toString().trim());
				map.add("mobile",contactNumber.getText().toString().trim());
				map.add("alternateMobile",alternateContactNumber.getText().toString().trim());
				map.add("email",emailAddress.getText().toString().trim());
				map.add("directLine",directLine.getText().toString().trim());
				map.add("salesExecutive",VBookApp.userID);
				map.add("organizationId",String.valueOf(VBookApp.orgID));
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);				
			    ResponseEntity<CustomerCrResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CustomerCrResponse.class);
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

		protected void onPostExecute(CustomerCrResponse result)
		{
			dismissProgressDialog();
			if(result!=null)
			{				
				Utils.displayDialog(context,null,result.getMessage(),Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				clearFieldData();
			}else
			{
				Utils.displayDialog(context,null,"Custmour change Request Failed.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);	
			}
			
		}

	}
	public void onClick(View v)
	{
		try
		{
			switch (v.getId())
			{
				case R.id.btnDisplay:
				{
					if(mCustAdapter.mSelectedPosition==-1)
					{
						Utils.displayDialog(context,null,"Please select one of custmour.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
						return;
					}
					if(mBtnDisplay.getText().equals("Modify Custmour"))
					{
						LinearLayout modifyCustLayout = (LinearLayout)findViewById(R.id.ModifyCust); 
						getLayoutInflater().inflate(R.layout.add_custmour_view, modifyCustLayout, true);
						modifyCustmourView.setVisibility(View.VISIBLE);
						custmourListView.setVisibility(View.GONE);
					}
					else if(mBtnDisplay.getText().equals("Sales return"))
					{
						
					}
					else if(mBtnDisplay.getText().equals("Payments"))
					{
						DeliveryNoteResponse deliveryNote = mCustAdapter.getItem(mCustAdapter.mSelectedPosition);
						Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
						intent.putExtra("MODE", "PAYMENT");
						intent.putExtra("BNAME", deliveryNote.getBusinessName());
						intent.putExtra("INAME", deliveryNote.getInvoiceName());
						intent.putExtra("CAMT", String.valueOf(deliveryNote.getCreditAmount()));
						intent.putExtra("AAMT", String.valueOf(deliveryNote.getAdvanceAmount()));
						startActivity(intent);						
						
					}
					else
					{
						DeliveryNoteResponse deliveryNote = mCustAdapter.getItem(mCustAdapter.mSelectedPosition);
						Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
						intent.putExtra("MODE", "INVOICE");
						intent.putExtra("BNAME", deliveryNote.getBusinessName());
						intent.putExtra("INAME", deliveryNote.getInvoiceName());
						intent.putExtra("CAMT", String.valueOf(deliveryNote.getCreditAmount()));
						intent.putExtra("AAMT", String.valueOf(deliveryNote.getAdvanceAmount()));
						startActivity(intent);						
					}
					Toast.makeText(getApplicationContext(), "selected Custmour is " + mCustAdapter.mSelectedPosition, Toast.LENGTH_LONG).show();
				}break;
				case R.id.btnSubmit:
				{
					validator.validate();
				}break;
				case R.id.btnClear:
				{
					clearFieldData();
				}break;
			}
		} catch (Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid selection", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onTabChanged(String tabId) 
	{
        if (tabId.equals("Invoice")) 
        {
        	mBtnDisplay.setText("Generate invoice");
        	modifyCustmourView.setVisibility(View.GONE);
        	custmourListView.setVisibility(View.VISIBLE);
        }
        else if (tabId.equals("PaymentCollection"))
        {
        	mBtnDisplay.setText("Payments");
        	modifyCustmourView.setVisibility(View.GONE);
        	custmourListView.setVisibility(View.VISIBLE);
        }
        else if (tabId.equals("StockReturns"))        	
        {
        	mBtnDisplay.setText("Sales return");
        	modifyCustmourView.setVisibility(View.GONE);
        	custmourListView.setVisibility(View.VISIBLE);
        }
        else if (tabId.equals("ModifyCustomerInfo"))
        {
        	mBtnDisplay.setText("Modify Custmour");
        }
        else
        {
        	modifyCustmourView.setVisibility(View.GONE);
        }
	}
	@Override
	public void preValidation() {		
	}
	@Override
	public void onValidationSuccess() {
		
		final Dialog dialog=new Dialog(context,R.style.customDialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog.setTitle("Customer Details");
		dialog.setContentView(R.layout.customer_preview);
		dialog.setCancelable(false);
		Button btnSave=(Button) dialog.findViewById(R.id.btnSave);
		Button btnCancel=(Button) dialog.findViewById(R.id.btnCancel);
		((TextView)dialog.findViewById(R.id.lblCustomerName)).setText(businessName.getText().toString());
		((TextView)dialog.findViewById(R.id.lblContactNo)).setText(contactNumber.getText().toString());
		((TextView)dialog.findViewById(R.id.lblInvoiceName)).setText(invoiceName.getText().toString());
		((TextView)dialog.findViewById(R.id.lblAddress)).setText(address1.getText().toString());
		((TextView)dialog.findViewById(R.id.lblCity)).setText(locality.getText().toString());
		((TextView)dialog.findViewById(R.id.lblState)).setText(city.getText().toString());
		btnCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			//Toast.makeText(getApplicationContext(), "Details Saved Successfuly",Toast.LENGTH_LONG).show();
				
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new CustomerCRTask().execute();
			//Toast.makeText(getApplicationContext(), "Details Saved Successfuly",Toast.LENGTH_LONG).show();
				
			}
		});
		dialog.show();
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
