/**
 * com.vekomy.vbooks.activities.ProductsActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 23-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VBookApp;
import com.vekomy.vbooks.adapters.ProductsAdapter;
import com.vekomy.vbooks.adapters.ProductsPreviewAdapter;
import com.vekomy.vbooks.app.request.DeliveryNoteRequest;
import com.vekomy.vbooks.app.response.DeliveryNoteResponse;
import com.vekomy.vbooks.app.response.ProductListResponse;
import com.vekomy.vbooks.helpers.ProductsView;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.restservices.RestTemplateFactory;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class ProductsActivity extends ProgressActivity implements OnClickListener{
	
	private ProductsAdapter 		mProductsAdapter;	
	private ProductsPreviewAdapter 	mProductsPreviewAdapter;
	private ListView 	mlvProductlist;
	private ListView 	mlvProductsPreview;
	
	private Button 		mbtnPayment;
	
	private String 		businessName;
	private String 		invoiceName;
	private String 		creditAmt;
	private String 		advanceAmt;
	
	private View		productsView;	
	private View		paymentsView;	
	private View 		previewView;
	
	private View 		paymentsBottomView;
	private View 		productsBottomView;
	private View 		previewBottomView;
	
	private TextView	lblTotalCost;
	
	TextView			lblOldCredit;
	TextView			lbladvance;
	TextView			lblPresentPay;
	TextView			lblTotalPayable;
	TextView			lblRemaingBal;	
	EditText			txPaymentNow;

	/**
	 * The Context
	 */
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_note_view);
		this.context = this;
		lblTotalCost	=  (TextView) findViewById(R.id.lblTotalPrice);
		mlvProductlist = (ListView) findViewById(R.id.lvProductlist);
		mlvProductsPreview = (ListView) findViewById(R.id.lvProductPreview);
		mbtnPayment = (Button) findViewById(R.id.btnPayment);
		mbtnPayment.setOnClickListener(this);
		findViewById(R.id.btnBack).setOnClickListener(this);		
		findViewById(R.id.btnPreview).setOnClickListener(this);
		findViewById(R.id.btnPreviewBack).setOnClickListener(this);
		
		businessName 	=	getIntent().getExtras().getString("BNAME") ;
		invoiceName		=	getIntent().getExtras().getString("INAME") ;
		creditAmt		=	getIntent().getExtras().getString("CAMT") ;	
		advanceAmt		=	getIntent().getExtras().getString("AAMT") ;
		
		productsView 	=	findViewById(R.id.hsProductsView);
		paymentsView 	=	findViewById(R.id.llPaymentView);
		previewView		=	findViewById(R.id.llPreview);	
		paymentsBottomView 	= findViewById(R.id.llPaymentsBottom);
		productsBottomView 	= findViewById(R.id.llProductsBottom);
		previewBottomView	= findViewById(R.id.llPreviwBottom);
		
		lblOldCredit		=	(TextView) findViewById(R.id.lblpreviouscredit);
		lbladvance			=	(TextView) findViewById(R.id.lbladvance);
		lblPresentPay		=	(TextView) findViewById(R.id.lblPresentPay);
		lblTotalPayable		=	(TextView) findViewById(R.id.lblTotalPayment);
		lblRemaingBal		=	(TextView) findViewById(R.id.lblRemaingBal);
		txPaymentNow		=	(EditText) findViewById(R.id.txPaymentnow);	
		lblOldCredit.setText(getIntent().getExtras().getString("CAMT"));
		lbladvance.setText(getIntent().getExtras().getString("AAMT"));
		lblPresentPay.setText("0.0");
		
		if(getIntent().getExtras().getString("MODE").equals("PAYMENT"))
		{
			productsView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.VISIBLE);
			productsBottomView.setVisibility(View.GONE);
			paymentsBottomView.setVisibility(View.VISIBLE);
			findViewById(R.id.btnBack).setVisibility(View.GONE);
			float totalPayable =  Float.parseFloat(creditAmt.isEmpty()?"0.0":creditAmt) - Float.parseFloat(advanceAmt.isEmpty()?"0.0":advanceAmt);
			lblTotalPayable.setText(String.valueOf(totalPayable));
		}
		else
		{ 
			new ProductListTask().execute();			
		}
		txPaymentNow.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Double remaingBal = (Double) (lblTotalPayable.getText().toString().trim().isEmpty()?0.0:Double.parseDouble(lblTotalPayable.getText().toString().trim())) -
									(Double) (txPaymentNow.getText().toString().trim().isEmpty()?0.0:Double.parseDouble(txPaymentNow.getText().toString().trim()));
				lblRemaingBal.setText(String.valueOf(BigDecimal.valueOf(remaingBal).toPlainString()));
			}
		});
		final Spinner cmbPaymentType = (Spinner)findViewById(R.id.spinnerPaymentType);
		cmbPaymentType.setOnItemSelectedListener(new OnItemSelectedListener() {
			final TableRow cheqe_row=(TableRow) findViewById(R.id.dynamicRow);
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				String selected_item = cmbPaymentType.getSelectedItem().toString().trim();
				if(selected_item.equalsIgnoreCase("Cash")){
					cheqe_row.setVisibility(View.GONE);
				}else if(selected_item.equalsIgnoreCase("Cheque")){
					cheqe_row.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}					
		});		
		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.btnPayment:
				productsView.setVisibility(View.GONE);
				paymentsView.setVisibility(View.VISIBLE);
				productsBottomView.setVisibility(View.GONE);
				paymentsBottomView.setVisibility(View.VISIBLE);
				String totalCost = lblTotalCost.getText().toString().trim().isEmpty()?"0.0":lblTotalCost.getText().toString().trim();
				lblPresentPay.setText(totalCost);
				float totalPayable =  Float.parseFloat(totalCost) + Float.parseFloat(creditAmt.isEmpty()?"0.0":creditAmt) - Float.parseFloat(advanceAmt.isEmpty()?"0.0":advanceAmt);
				lblTotalPayable.setText(String.valueOf(totalPayable));
				lblRemaingBal.setText(String.valueOf(totalPayable));
				if(mProductsPreviewAdapter == null)
				{
					mProductsPreviewAdapter = new ProductsPreviewAdapter(context, R.layout.delivery_note_preview_row,new ArrayList<ProductsView>());				
					mlvProductsPreview.setAdapter(mProductsPreviewAdapter);
				}
				mProductsPreviewAdapter.deliveryNoteProducts.clear();
				ProductsView product;
				for (int i = 0; i < mProductsAdapter.mProductList.size(); i++) {
					product = mProductsAdapter.mProductList.get(i); 
					Log.d("tag", String.valueOf(product.enteredQty));
					Log.d("tag", String.valueOf(product.bonusEnteredQty));
					if(product.enteredQty!=0 || product.bonusEnteredQty != 0){
						mProductsPreviewAdapter.deliveryNoteProducts.add(product);		
					}
				}
				mProductsPreviewAdapter.refresh();
				break;
			case R.id.btnBack:
				paymentsView.setVisibility(View.GONE);
				productsView.setVisibility(View.VISIBLE);
				paymentsBottomView.setVisibility(View.GONE);
				productsBottomView.setVisibility(View.VISIBLE);
				txPaymentNow.setText("0");
				break;
			case R.id.btnPreview:
				previewBottomView.setVisibility(View.VISIBLE);
				previewView.setVisibility(View.VISIBLE);
				productsView.setVisibility(View.GONE);
				productsBottomView.setVisibility(View.GONE);
				paymentsView.setVisibility(View.GONE);				
				paymentsBottomView.setVisibility(View.GONE);
				//new DeliveryNoteTask().execute();
				break;
			case R.id.btnPreviewBack:
				previewBottomView.setVisibility(View.GONE);
				previewView.setVisibility(View.GONE);
				productsView.setVisibility(View.GONE);
				productsBottomView.setVisibility(View.GONE);
				paymentsView.setVisibility(View.VISIBLE);				
				paymentsBottomView.setVisibility(View.VISIBLE);
		}
	}
	public void disPlayResult(ProductListResponse result)
	{
		mlvProductlist.setClickable(true);
		if(result!=null)
		{			
			ProductsView deliveryNoteProduct;
			List<ProductsView> productsViewList = new ArrayList<ProductsView>();
			for (int i = 0; i < result.getProductList().size(); i++) {
				deliveryNoteProduct = new ProductsView(result.getProductList().get(i));
				productsViewList.add(deliveryNoteProduct);
			}
			mProductsAdapter = new ProductsAdapter(context, R.layout.delivery_note_product_row,productsViewList,lblTotalCost);
			mlvProductlist.setAdapter(mProductsAdapter);
		}
		if(mProductsAdapter.mProductList.size() == 0)
			mbtnPayment.setEnabled(false);
	}
	// ***************************************
	// Private classes http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	// ***************************************
	class ProductListTask extends AsyncTask<Void, Void, ProductListResponse>
	{
		final String url = getString(R.string.BASE_URL) + "productlist?uname={uname}&orgID={orgID}&bname={bname}";
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of allocated Products ..... Please wait...");
		}
		
		@Override
		protected ProductListResponse doInBackground(Void... params)
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
			    ResponseEntity<ProductListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(new HttpHeaders()), ProductListResponse.class,
			    		VBookApp.userID,VBookApp.orgID,businessName);			    
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

		protected void onPostExecute(ProductListResponse result)
		{
			dismissProgressDialog();
			disPlayResult(result);			
		}

	}
	
	class DeliveryNoteTask extends AsyncTask<Void, Void, DeliveryNoteResponse>
	{
		final String url = getString(R.string.BASE_URL) + "deliveryNote";
		protected void onPreExecute()
		{
			showProgressDialog("Delivery Note Saveing..... Please wait...");
		}
		
		@Override
		protected DeliveryNoteResponse doInBackground(Void... params)
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
			     
				DeliveryNoteRequest deliveryNote = new DeliveryNoteRequest();
				deliveryNote.setCreatedBy(VBookApp.userID);
				deliveryNote.setOrganizationId(VBookApp.orgID);
				deliveryNote.setBusinessName(businessName);
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);			    
			    // The POST request.
			    return restTemplate.postForObject(url, new HttpEntity(deliveryNote, headers), DeliveryNoteResponse.class);
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

		protected void onPostExecute(DeliveryNoteResponse result)
		{
			dismissProgressDialog();
			Utils.displayDialog(context,null,result.getMessage(),Utils.DIALOG_OK,"Ok",null,null,null,null,null);
		}

	}
}

