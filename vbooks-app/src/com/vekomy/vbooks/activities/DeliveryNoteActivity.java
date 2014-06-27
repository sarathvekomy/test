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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.client.RestClientException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.ProductsAdapter;
import com.vekomy.vbooks.adapters.ProductsPreviewAdapter;
import com.vekomy.vbooks.adapters.SalesReturnAdapter;
import com.vekomy.vbooks.adapters.SalesReturnPreviewAdapter;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteProduct;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnProduct;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class DeliveryNoteActivity extends ProgressActivity implements OnClickListener {
	
	private ListView 	mlvProductlist;
	private ListView 	mlvProductsPreview;
	
	ProductsAdapter 			mProductsAdapter;
	ProductsPreviewAdapter 		mProductsPreviewAdapter;
	SalesReturnAdapter			mSaleReturnProductsAdapter;
	SalesReturnPreviewAdapter   mSaleReturnPreviewAdapter;

	// ================================== Views ============================//
	private View		productsView;
	private View 		productsBottomView;
	private View 		salesReturnBottomView;
	private View		paymentsView;
	private View 		paymentsBottomView;
	private View 		previewView;
	private View 		previewPaymentBottom;
	private View 		previewBottomView;
	private View 		chequeView;
	// ================================== Views ============================//
	private Button 		mbtnPayment;
	private Button 		btnPreview;
	// ========================== INVOICE Payments ============================// 
	private TextView	lblTotalCost;
	private TextView	lblOldCredit;
	private TextView	lbladvance;
	private TextView	lblPresentPayable;
	private TextView	lblTotalPayable;
	private TextView	lblTotoalPayableAdv;
	private TextView	lblRemaingBal;	
	private EditText	txPaymentNow;
	private TextView	lblBalAdv;
	private Spinner 	cmbPaymentType;
	private EditText	txChequeNo;
	private EditText	txBankName;
	private EditText	txBranchName;
	private EditText	txBranchLoc;
	// ========================== INVOICE Payments ============================//
	
	// ========================== INVOICE PreView ============================// 
	private TextView	lblInvoiceNoPreView;
	private TextView	lblDatePreView;
	private TextView	lblInvoiceNamePreView;
	private TextView	lblBusinessNamePreView;
	private TextView	lblPreviousBalPreView;	
	private EditText	txPresentPayPreView;
	private TextView	lblTatalPayablePreView;
	private TextView	lblTatalPayablePreViewAdv;
	private TextView	lblPresentPayablePreView;
	private TextView	lblBalPreView;
	private TextView	lblBalPreViewAdv;
	private Spinner		cmbPaymentTypePreView;
	private EditText	txBankNamePreView;
	private EditText	txChequeNoPreView;
	private EditText	txBranchAndLocPreView;
	// =============================== INVOICE PreView ======================//

	// ================================== Dialog ============================//
	private Dialog      dialog;
	private EditText    txtProductName;
	private EditText    txtBatchNo;
	// ================================== Dialog ============================//

	SalesDao			salesDao;
	DeliveryNote		deliveryNote;
	SalesReturn			deliveryNoteSR;
	String 				custId="";
	String 				salesRefId;
	/**
	 * The Context
	 */
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.sales_delivery_note_view);
			this.context 	=	this;
			userID 			=	Utils.getData(this, Constants.USER_ID, Constants.NA);
			orgID			=	Utils.getData(this, Constants.ORG_ID, "0");
			custId			=	getIntent().getExtras().getString("CUSTID") ;
			
			mlvProductlist 	= (ListView) findViewById(R.id.lvProductlist);
			mlvProductlist.setClickable(true);
			mlvProductsPreview = (ListView) findViewById(R.id.lvProductPreview);
			
			// all Buttons setOnClickListener
			mbtnPayment = (Button) findViewById(R.id.btnPayment);
			mbtnPayment.setOnClickListener(this);
			btnPreview = (Button) findViewById(R.id.btnPreview);
			btnPreview.setOnClickListener(this);
			findViewById(R.id.btnBack).setOnClickListener(this);		
			
			findViewById(R.id.btnPreviewBack).setOnClickListener(this);
			findViewById(R.id.btnSave).setOnClickListener(this);
			findViewById(R.id.btnSRPreview).setOnClickListener(this);
			findViewById(R.id.btnSRAddProduct).setOnClickListener(this);
	
			// =============================== Views ============================//
			productsView 		=	findViewById(R.id.llProductsView);
			productsBottomView 	=	findViewById(R.id.llProductsBottom);
			salesReturnBottomView= 	findViewById(R.id.llSalesReturnBottom);
	
			paymentsView 		=	findViewById(R.id.llPaymentView);
			paymentsBottomView 	=	findViewById(R.id.llPaymentsBottom);
	
			previewView			=	findViewById(R.id.llPreview);
			previewPaymentBottom=	findViewById(R.id.llPreviewPaymentBottom);
			previewBottomView	= 	findViewById(R.id.llPreviwBottom);
			
			chequeView			= 	findViewById(R.id.trChequeInfoView);
			cmbPaymentType		=	(Spinner) findViewById(R.id.cmbPaymentType);
			
			// =============================== Product View ======================//
			findViewById(R.id.btnDNProductsBack).setOnClickListener(this);
			findViewById(R.id.btnSRProductsBack).setOnClickListener(this);
	
			// =============================== Payments View ======================//
			lblTotalCost		=	(TextView) 	findViewById(R.id.lblTotalPrice);
			lblOldCredit		=	(TextView) 	findViewById(R.id.lblpreviouscredit);
			lbladvance			=	(TextView) 	findViewById(R.id.lbladvance);
			lblPresentPayable	=	(TextView) 	findViewById(R.id.lblPresentPayable);
			lblTotalPayable		=	(TextView) 	findViewById(R.id.lblTotalPayment);
			lblRemaingBal		=	(TextView) 	findViewById(R.id.lblRemaingBal);
			txPaymentNow		=	(EditText) 	findViewById(R.id.txPaymentnow);	
			cmbPaymentType 		= 	(Spinner)	findViewById(R.id.cmbPaymentType);
			txChequeNo			=	(EditText) 	findViewById(R.id.txChequeNo);
			txBankName			=	(EditText) 	findViewById(R.id.txBankName);
			txBranchName		=	(EditText) 	findViewById(R.id.txBranchName);
			txBranchLoc			=	(EditText) 	findViewById(R.id.txBranchLoc);
			lblTotoalPayableAdv =	(TextView) 	findViewById(R.id.lblTotoalPayableAdv);
			lblBalAdv			=	(TextView) 	findViewById(R.id.lblBalAdv);
			findViewById(R.id.btnPaymentsBack).setOnClickListener(this);
			
			String  paymentsTypes 	= Utils.getData(getApplicationContext(), Constants.PAYMENTS_TYPE, "Cash");
			String [] paymentsTypesArry  = paymentsTypes.split(",");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,paymentsTypesArry);
			cmbPaymentType.setAdapter(adapter);
			
			//	
			//  ********************  INVOICE PREVIEW ***********************************
			//
			lblInvoiceNoPreView		=	(TextView) 	findViewById(R.id.lblInvoiceNo);
			lblDatePreView			=	(TextView) 	findViewById(R.id.lblCurrentDate);
			lblInvoiceNamePreView	=	(TextView) 	findViewById(R.id.lblInvoiceName);
			lblBusinessNamePreView	=	(TextView) 	findViewById(R.id.lblBusinessName);
			lblPreviousBalPreView	=	(TextView) 	findViewById(R.id.lblPreviousBalPreView);	
			txPresentPayPreView		=	(EditText) 	findViewById(R.id.txPresentPayPreView);
			lblTatalPayablePreView	=	(TextView) 	findViewById(R.id.lblTatalPayablePreView);
			lblTatalPayablePreViewAdv=	(TextView) 	findViewById(R.id.lblTatalPayablePreViewAdv);
			lblPresentPayablePreView=	(TextView) 	findViewById(R.id.lblPresentPayablePreView);
			lblBalPreView			=	(TextView) 	findViewById(R.id.lblBalPreView);
			lblBalPreViewAdv		=	(TextView) 	findViewById(R.id.lblBalPreViewAdv);
			cmbPaymentTypePreView	=	(Spinner) 	findViewById(R.id.cmbPaymentTypePreView);
			
			txChequeNoPreView		=	(EditText) findViewById(R.id.txChequeNoPreView);
			txBankNamePreView		=	(EditText) findViewById(R.id.txBankNamePreView);
			txBranchAndLocPreView	=	(EditText) findViewById(R.id.txBranchAndLocPreView);
			//  ********************  INVOICE PREVIEW ***********************************
			salesDao =  new SalesDao();		
	
			lblTatalPayablePreView.addTextChangedListener(new TextWatcher()
			{	@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
				
				@Override
				public void afterTextChanged(Editable entertext) {
					if(mProductsPreviewAdapter!=null && !mProductsPreviewAdapter.isViewMode)
						deliveryNote.setTotalPayable(mProductsPreviewAdapter.totalProductsPayable);
					mProductsPreviewAdapter.deliveryNoteProducts = deliveryNote.getProducts();
					if(entertext.toString().startsWith("-"))
					{
						if(entertext.length()!=0)
							lblTatalPayablePreView.setText(entertext.subSequence(1, entertext.length()));
						lblTatalPayablePreViewAdv.setText(getResources().getString(R.string.adv));
					}
					else
					{
						lblTatalPayablePreViewAdv.setText("");
					}
					float presentPayable	= deliveryNote.getTotalPayable() - (deliveryNote.getPreviousCredit()!=0?deliveryNote.getPreviousCredit():-deliveryNote.getPresentAdvance()); 
					lblPresentPayablePreView.setText(Utils.currencyInMillion(presentPayable));
					float presentPayment 	=	txPresentPayPreView.getText().toString().isEmpty()?0:Utils.getNumberFromMillion(txPresentPayPreView.getText().toString());
					deliveryNote.setBalance(deliveryNote.getTotalPayable() - presentPayment);
					lblBalPreView.setText(Utils.currencyInMillion(deliveryNote.getBalance()));
				}
			});
			lblBalPreView.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
				@Override
				public void afterTextChanged(Editable s) {
					if(s.toString().startsWith("-"))
					{
						if(s.length()!=0)
							lblBalPreView.setText(s.subSequence(1, s.length()));
						lblBalPreViewAdv.setText(getResources().getString(R.string.adv));
					}
					else
					{
						lblBalPreViewAdv.setText("");
					}
				}
			});	
			
			if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.NEW))
			{
				if(getIntent().getExtras().getString(Constants.MODE).equals(Constants.NEW + Constants.SR ))
				{
					deliveryNoteSR = new SalesReturn();
					deliveryNoteSR.setBusinessName(getIntent().getExtras().getString("BNAME"));
					deliveryNoteSR.setInvoiceName(getIntent().getExtras().getString("INAME"));
					deliveryNoteSR.setProducts(new ArrayList<SalesReturnProduct>());
					deliveryNoteSR.setCreatedBy(userID);
					deliveryNoteSR.setOrganizationId(Integer.parseInt(orgID));				
					showSalesReturnProductScreeen();
					new LoadSoldProductList().execute();
				}
				else // DN or Payments
				{
					deliveryNote = new DeliveryNote();
					deliveryNote.setBusinessName(getIntent().getExtras().getString("BNAME"));
					deliveryNote.setInvoiceName(getIntent().getExtras().getString("INAME"));
					deliveryNote.setPreviousCredit(Float.parseFloat((String)(getIntent().getExtras().getString("CAMT")==null?0:getIntent().getExtras().getString("CAMT"))));
					deliveryNote.setPresentAdvance(Float.parseFloat((String)(getIntent().getExtras().getString("AAMT")==null?0:getIntent().getExtras().getString("AAMT"))));
					deliveryNote.setTotalPayable(deliveryNote.getPreviousCredit() - deliveryNote.getPresentAdvance());
					deliveryNote.setPresentPayable(0f);
					deliveryNote.setBalance(deliveryNote.getTotalPayable());
					deliveryNote.setProducts(new ArrayList<DeliveryNoteProduct>());
					deliveryNote.setCreatedBy(userID);
					deliveryNote.setOrganizationId(Integer.parseInt(orgID));
					
					txPaymentNow.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
						@Override
						public void afterTextChanged(Editable s) {
							float presentPayment = s.toString().trim().isEmpty()?0:Utils.getNumberFromMillion(s.toString().trim());
							float remaingBal 	 = deliveryNote.getTotalPayable() - presentPayment;
							deliveryNote.setBalance(remaingBal);
							deliveryNote.setPresentPayment(presentPayment);
							lblRemaingBal.setText(Utils.currencyInMillion(remaingBal));
						}
					});
					lblTotalPayable.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
						@Override
						public void afterTextChanged(Editable s) {
							if(s.toString().startsWith("-"))
							{
								if(s.length()!=0)
									lblTotalPayable.setText(s.subSequence(1, s.length()));
								lblTotoalPayableAdv.setText(getResources().getString(R.string.adv));
							}
							else
							{
								lblTotoalPayableAdv.setText("");
								lblTatalPayablePreViewAdv.setText("");
							}						
						}
					});
					lblRemaingBal.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
						@Override
						public void afterTextChanged(Editable s) {
							if(s.toString().startsWith("-"))
							{
								if(s.length()!=0)
								{
									lblRemaingBal.setText(s.subSequence(1, s.length()));
								}
								lblBalAdv.setText(getResources().getString(R.string.adv));
							}
							else
							{
								lblBalAdv.setText("");
								lblBalPreViewAdv.setText("");
							}
						}
					});
					cmbPaymentType.setOnItemSelectedListener(new OnItemSelectedListener() 
					{
						final LinearLayout cheqe_row=(LinearLayout) findViewById(R.id.dynamicRow);
						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
							String selected_item = cmbPaymentType.getSelectedItem().toString().trim();
							if(selected_item.equalsIgnoreCase("Cheque")){
								txChequeNo.setText("");
								txBankName.setText("");
								txBranchName.setText("");
								txBranchLoc.setText("");
								cheqe_row.setVisibility(View.VISIBLE);
							}
							else{
								cheqe_row.setVisibility(View.GONE);
								txChequeNo.setText("");
								txBankName.setText("");
								txBranchName.setText("");
								txBranchLoc.setText("");
								txChequeNo.setError(null);
								txBankName.setError(null);
								txBranchName.setError(null);
								txBranchLoc.setError(null);
							}
								
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {}					
					});
					if(getIntent().getExtras().getString(Constants.MODE).equals(Constants.NEW + Constants.PY))
					{
						findViewById(R.id.btnBack).setVisibility(View.GONE);
						showPaymentScreeen();
					}
					else
					{					
						showProductScreeen();
						new LoadProductList().execute();					
					}
				}
			}
			else if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.VIEW))
			{
				salesRefId	=	getIntent().getExtras().getString(Constants.SALES_REF_ID);
				((Button)findViewById(R.id.btnPreviewBack)).setText(getResources().getString(R.string.ok));
				((Button)findViewById(R.id.btnSave)).setText(getResources().getString(R.string.cancel));
				if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.VIEW + Constants.DN))
				{
					//((Button)findViewById(R.id.btnPreviewBack)).setText(getResources().getString(R.string.ok));
					//((Button)findViewById(R.id.btnSave)).setText(getResources().getString(R.string.cancel));
					new LoadDeliveryNote(true).execute(salesRefId);
				}
				else if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.VIEW + Constants.SR))
				{
					//((Button)findViewById(R.id.btnSRPreview)).setText(getResources().getString(R.string.ok));
					//((Button)findViewById(R.id.btnSRAddProduct)).setText(getResources().getString(R.string.cancel));
					new LoadSalesReturnDN(true).execute(salesRefId);
				}
			}
			else if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.MODIFY))
			{
				salesRefId	=	getIntent().getExtras().getString(Constants.SALES_REF_ID);
				((Button)findViewById(R.id.btnPreviewBack)).setText(getResources().getString(R.string.update));
				((Button)findViewById(R.id.btnSave)).setText(getResources().getString(R.string.cancel));
				if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.MODIFY + Constants.DN))
				{
					//((Button)findViewById(R.id.btnPreviewBack)).setText(getResources().getString(R.string.update));
					//((Button)findViewById(R.id.btnSave)).setText(getResources().getString(R.string.cancel));
					
					cmbPaymentTypePreView.setOnItemSelectedListener(new OnItemSelectedListener() 
					{
						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
							String selected_item = cmbPaymentTypePreView.getSelectedItem().toString().trim();
							if(selected_item.equalsIgnoreCase("Cheque")){
								chequeView.setVisibility(View.VISIBLE);
							}else {
								txChequeNoPreView.setText("");
								txBankNamePreView.setText("");
								txBranchAndLocPreView.setText("");
								txChequeNoPreView.setError(null);
								txBankNamePreView.setError(null);
								txBranchAndLocPreView.setError(null);
								chequeView.setVisibility(View.GONE);
							}						
							deliveryNote.setPaymentType(selected_item);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {}					
					});
					
					txPresentPayPreView.addTextChangedListener(new TextWatcher()
					{	@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
						
						@Override
						public void afterTextChanged(Editable entertext) {
							//float totalPayable 		=	lblTatalPayablePreView.getText().toString().isEmpty()?0:Float.parseFloat(lblTatalPayablePreView.getText().toString());
							float presentPayment 	=	entertext.toString().isEmpty()?0:Utils.getNumberFromMillion(entertext.toString());
							deliveryNote.setBalance(deliveryNote.getTotalPayable() - presentPayment);
							lblBalPreView.setText(Utils.currencyInMillion(deliveryNote.getBalance()));
							deliveryNote.setPresentPayment(presentPayment);
						}
					});			
					new LoadDeliveryNote(false).execute(salesRefId);
				}
				else if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.MODIFY + Constants.SR))
				{
					//((Button)findViewById(R.id.btnSRPreview)).setText(getResources().getString(R.string.update));
					//((Button)findViewById(R.id.btnSRAddProduct)).setText(getResources().getString(R.string.cancel));
					new LoadSalesReturnDN(false).execute(salesRefId);
				}	
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	/** 
	 * 
	 */
	@Override
	public void onClick(View v) 
	{
		try
		{		
			switch (v.getId()) 
			{
				case R.id.btnDNProductsBack:
				case R.id.btnSRProductsBack:
				case R.id.btnPaymentsBack:
					finish();
					break;
				case R.id.btnPayment:					
					float presentPayable = lblTotalCost.getText().toString().trim().isEmpty()?0:Utils.getNumberFromMillion(lblTotalCost.getText().toString().trim());
					if(presentPayable == 0)
					{
						Integer prodQty = new Integer(0);
						Integer bonusQty = new Integer(0);
						DeliveryNoteProduct product;
						for (int i = 0; i < mProductsAdapter.mProductList.size(); i++) {
							product = mProductsAdapter.mProductList.get(i);
							mProductsAdapter.mProductList.get(i).getBonusQty();
							//if(product.getProductQty()!=0 || product.getBonusQty() != 0){
								deliveryNote.getProducts().add(product);		
							//}
						}
						List<DeliveryNoteProduct> products = deliveryNote.getProducts();
						for (DeliveryNoteProduct deliveryNoteProduct : products) {
							prodQty = prodQty + deliveryNoteProduct.getProductQty();
							bonusQty = bonusQty + deliveryNoteProduct.getBonusQty();
						}
						if(bonusQty == 0){
							Utils.displayDialog(context,null,"Please select one of Product.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
							return;
						}
					}
					deliveryNote.setPresentPayable(presentPayable);
					float totalPayable =  presentPayable + deliveryNote.getPreviousCredit() - deliveryNote.getPresentAdvance();
					deliveryNote.setTotalPayable(totalPayable);
					float presentPayment = txPaymentNow.getText().toString().trim().isEmpty()?0:Float.parseFloat(txPaymentNow.getText().toString().trim());
					deliveryNote.setBalance(totalPayable-presentPayment);
					showPaymentScreeen();
					break;
				case R.id.btnBack:
					showProductScreeen();
					break;
				case R.id.btnPreview:
					if(!preparePreviewScreeen())
						return;
					showPreviewScreeen(true);
					break;
				case R.id.btnPreviewBack:
					if( ((Button)findViewById(R.id.btnPreviewBack)).getText().equals(getResources().getString(R.string.ok)))
						finish();
					else if( ((Button)findViewById(R.id.btnPreviewBack)).getText().equals(getResources().getString(R.string.update)))
					{
						if((Integer)btnPreview.getTag()==1)
						{
							if(cmbPaymentTypePreView.getSelectedItem().toString().trim().equalsIgnoreCase("CHEQUE"))
							{
								if(txChequeNoPreView.getText().toString().trim().equals("")){
									txChequeNoPreView.setError("Enter ChequeNo");
									txChequeNoPreView.requestFocus();
									return;
								}else if(txBankNamePreView.getText().toString().trim().equals("")){
									txBankNamePreView.setError("Enter Bank Name");
									txBankNamePreView.requestFocus();
									return;
								}
								else if(txBranchAndLocPreView.getText().toString().trim().equals("")){
									txBranchAndLocPreView.setError("Enter Branch Name");
									txBranchAndLocPreView.requestFocus();
									return;
								}
								deliveryNote.setChequeNo(txChequeNoPreView.getText().toString().trim());
								deliveryNote.setBankName(txBankNamePreView.getText().toString().trim());
								deliveryNote.setBranchName(txBranchAndLocPreView.getText().toString().trim());
								//deliveryNote.setBranchLocation(branchLocation);
							}
							final Dialog descriptionDialog = new Dialog(DeliveryNoteActivity.this,R.style.customDialogStyle);
							descriptionDialog.setCancelable(false);
							descriptionDialog.setContentView(R.layout.remarks_display_dialog);								
							descriptionDialog.findViewById(R.id.txtDescription).setEnabled(false);
							((EditText)descriptionDialog.findViewById(R.id.txtDescription)).setText(deliveryNote.getRemarks());
							Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
							Cancel.setOnClickListener(new OnClickListener(){@Override public void onClick(View v) {descriptionDialog.dismiss();}});							
							Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);
							btnSave.setOnClickListener(new OnClickListener() 
							{
								@Override
								public void onClick(View v) 
								{
									String resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtnewDescription)).getText().toString();
									deliveryNote.setRemarks(resultMsg);
									new SaveDeliveryNote(true,false).execute();
									descriptionDialog.dismiss();
								}
							});
							descriptionDialog.show();
						}
						else
						{
							final Dialog descriptionDialog = new Dialog(DeliveryNoteActivity.this,R.style.customDialogStyle);
							descriptionDialog.setCancelable(false);
							descriptionDialog.setContentView(R.layout.remarks_display_dialog);								
							descriptionDialog.findViewById(R.id.txtDescription).setEnabled(false);
							((EditText)descriptionDialog.findViewById(R.id.txtDescription)).setText(deliveryNoteSR.getRemarks());
							Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
							Cancel.setOnClickListener(new OnClickListener(){@Override public void onClick(View v) {descriptionDialog.dismiss();}});							
							Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);
							btnSave.setOnClickListener(new OnClickListener() 
							{
								@Override
								public void onClick(View v) 
								{
									String resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtnewDescription)).getText().toString();
									deliveryNoteSR.setRemarks(resultMsg);
									new SaveDeliveryNote(true,true).execute();
									descriptionDialog.dismiss();
								}
							});
							descriptionDialog.show();
						}
					}									
					else
					{
						if((Integer)btnPreview.getTag()==1)
							showPaymentScreeen();
						else
							showSalesReturnProductScreeen();
					}
					break;
				case R.id.btnSave:
					if( ((Button)findViewById(R.id.btnSave)).getText().equals(getResources().getString(R.string.cancel)))
						finish();
					else 
						if((Integer)btnPreview.getTag()==1)
						{
							final Dialog descriptionDialog = new Dialog(DeliveryNoteActivity.this,R.style.customDialogStyle);
							descriptionDialog.setCancelable(false);
							descriptionDialog.setContentView(R.layout.remarks_display_dialog);								
							descriptionDialog.findViewById(R.id.viewnewDescription).setVisibility(View.GONE);
							Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
							Cancel.setOnClickListener(new OnClickListener(){@Override public void onClick(View v) {descriptionDialog.dismiss();}});							
							Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);
							btnSave.setOnClickListener(new OnClickListener() 
							{
								@Override
								public void onClick(View v) 
								{
									String resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtDescription)).getText().toString();
									deliveryNote.setRemarks(resultMsg);
									new SaveDeliveryNote(false,false).execute();
									descriptionDialog.dismiss();
								}
							});
							descriptionDialog.show();
						}
						else
						{
							final Dialog descriptionDialog = new Dialog(DeliveryNoteActivity.this,R.style.customDialogStyle);
							descriptionDialog.setCancelable(false);
							descriptionDialog.setContentView(R.layout.remarks_display_dialog);								
							descriptionDialog.findViewById(R.id.viewnewDescription).setVisibility(View.GONE);
							Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
							Cancel.setOnClickListener(new OnClickListener(){@Override public void onClick(View v) {descriptionDialog.dismiss();}});							
							Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);
							btnSave.setOnClickListener(new OnClickListener() 
							{
								@Override
								public void onClick(View v) 
								{
									String resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtDescription)).getText().toString();
									deliveryNoteSR.setRemarks(resultMsg);
									new SaveDeliveryNote(false,true).execute();	
									descriptionDialog.dismiss();
								}
							});
							descriptionDialog.show();
						}
					break;
				case R.id.btnSRPreview:
					if( ((Button)findViewById(R.id.btnSRPreview)).getText().equals(getResources().getString(R.string.ok)))
						finish();
					//else if( ((Button)findViewById(R.id.btnSRPreview)).getText().equals(getResources().getString(R.string.back)))
					//	showSalesReturnProductScreeen();
					//else if( ((Button)findViewById(R.id.btnSRPreview)).getText().equals(getResources().getString(R.string.update)))
					//{
					//	new SaveDeliveryNote(true,true).execute();
					//}
					else
					{
						((Button)findViewById(R.id.btnSRPreview)).requestFocus();
						if(!prepareSRPreviewScreeen())
							return;
						showSalesReturnPreviewScreeen(true);
					}
					break;
				case R.id.btnSRAddProduct:
					if( ((Button)findViewById(R.id.btnSRAddProduct)).getText().equals(getResources().getString(R.string.cancel)))
						finish();
					//else if( ((Button)findViewById(R.id.btnSRAddProduct)).getText().equals(getResources().getString(R.string.save)))
					//{
					//	new SaveDeliveryNote(false,true).execute();
					//}				
					else
					{
						// call cust dialog  box here					
						dialog	=	new Dialog(DeliveryNoteActivity.this,R.style.customDialogStyle);
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setCancelable(false);
						dialog.setContentView(R.layout.add_sold_products);
						txtProductName	=	(EditText) dialog.findViewById(R.id.txtSoldProductName);
						txtBatchNo		=	(EditText) dialog.findViewById(R.id.txtSoldBatchNo);
						((Button) dialog.findViewById(R.id.btnDialogSoldProductCancel)).setOnClickListener(DeliveryNoteActivity.this);
						((Button) dialog.findViewById(R.id.btnDialogSoldProductAdd)).setOnClickListener(DeliveryNoteActivity.this);
						dialog.show();					
					}
					break;				
				case R.id.btnDialogSoldProductAdd:
					if(txtProductName.getText().toString().trim().equals("")){
						txtProductName.setError("Enter ProductName");
						return;
					}
					if(txtBatchNo.getText().toString().trim().equals("")){
						txtBatchNo.setError("Enter BatchNo");
						return;
					}
					//Add the Details To The ListView From Hear					
					SalesReturnProduct product = new SalesReturnProduct();					
					product.setProductName(txtProductName.getText().toString().trim());
					product.setBatchNumber(txtBatchNo.getText().toString().trim());
					product.setResaleQty(0);
					product.setDamagedQty(0);
					product.setTotalQty(0);
					mSaleReturnProductsAdapter.mProductList.add(product);
					mSaleReturnProductsAdapter.refresh();
					dialog.dismiss();
					break;
				case R.id.btnDialogSoldProductCancel:
					dialog.dismiss();
					break;
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	public void showProductScreeen()
	{
		try
		{
			previewView.setVisibility(View.GONE);
			previewPaymentBottom.setVisibility(View.GONE);
			previewBottomView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.GONE);
			paymentsBottomView.setVisibility(View.GONE);
			salesReturnBottomView.setVisibility(View.GONE);
			productsView.setVisibility(View.VISIBLE);
			productsBottomView.setVisibility(View.VISIBLE);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	public void showPaymentScreeen()
	{
		try
		{
			productsView.setVisibility(View.GONE);
			productsBottomView.setVisibility(View.GONE);
			salesReturnBottomView.setVisibility(View.GONE);
			previewView.setVisibility(View.GONE);
			previewPaymentBottom.setVisibility(View.GONE);
			previewBottomView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.VISIBLE);
			paymentsBottomView.setVisibility(View.VISIBLE);
			
			lblOldCredit.setText(Utils.currencyInMillion(deliveryNote.getPreviousCredit()));
			lbladvance.setText(Utils.currencyInMillion(deliveryNote.getPresentAdvance()));
			lblPresentPayable.setText(Utils.currencyInMillion(deliveryNote.getPresentPayable()));			
			lblTotalPayable.setText(Utils.currencyInMillion(deliveryNote.getTotalPayable()));
			lblRemaingBal.setText(Utils.currencyInMillion(deliveryNote.getBalance()));
			txPaymentNow.setText("");
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void showPreviewScreeen(boolean isViewMode)
	{
		try
		{
			productsView.setVisibility(View.GONE);
			productsBottomView.setVisibility(View.GONE);
			salesReturnBottomView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.GONE);
			paymentsBottomView.setVisibility(View.GONE);
			previewView.setVisibility(View.VISIBLE);
			previewPaymentBottom.setVisibility(View.VISIBLE);
			previewBottomView.setVisibility(View.VISIBLE);
			btnPreview.setTag(1);

			((TextView)findViewById(R.id.lblCreatedBy)).setText(deliveryNote.getCreatedBy());
			lblInvoiceNoPreView.setText(deliveryNote.getInvoiceNo());
			lblDatePreView.setText(deliveryNote.getCreatedOn());
			lblInvoiceNamePreView.setText(deliveryNote.getInvoiceName());
			lblBusinessNamePreView.setText(deliveryNote.getBusinessName());
			if(deliveryNote.getPresentAdvance() > 0)
			{
				((TextView)findViewById(R.id.lblPreviousBal)).setText(getResources().getString(R.string.previous_advance));
				lblPreviousBalPreView.setText(Utils.currencyInMillion(deliveryNote.getPresentAdvance()));
			}
			else
			{
				((TextView)findViewById(R.id.lblPreviousBal)).setText(getResources().getString(R.string.previous_credit));
				lblPreviousBalPreView.setText(Utils.currencyInMillion(deliveryNote.getPreviousCredit()));
			}
			txPresentPayPreView.setText(Utils.currencyInMillion(deliveryNote.getPresentPayment()));
			lblTatalPayablePreView.setText(Utils.currencyInMillion(deliveryNote.getTotalPayable()));
			lblPresentPayablePreView.setText(Utils.currencyInMillion(deliveryNote.getPresentPayable()));
			lblBalPreView.setText(Utils.currencyInMillion(deliveryNote.getBalance()));
			
			txChequeNoPreView.setText(deliveryNote.getChequeNo());
			txBankNamePreView.setText(deliveryNote.getBankName());
			txBranchAndLocPreView.setText(deliveryNote.getBranchName()+ (deliveryNote.getBranchLocation().isEmpty()?"":"/"+deliveryNote.getBranchLocation()));
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,new String[]{deliveryNote.getPaymentType()});
			cmbPaymentTypePreView.setAdapter(adapter);
			
			if(cmbPaymentTypePreView.getSelectedItem().toString().equalsIgnoreCase("Cheque"))			
				chequeView.setVisibility(View.VISIBLE);	
			else
				chequeView.setVisibility(View.GONE);		

			if(isViewMode)
			{
				txPresentPayPreView.setEnabled(false);
				cmbPaymentTypePreView.setEnabled(false);
				txBankNamePreView.setEnabled(false);
				txChequeNoPreView.setEnabled(false);
				txBranchAndLocPreView.setEnabled(false);
				if(deliveryNote.getProducts() != null && deliveryNote.getProducts().size()!=0)
				{
					if(mProductsPreviewAdapter == null)
					{
						// productsViewList
						mProductsPreviewAdapter = new ProductsPreviewAdapter(context, R.layout.sales_delivery_note_preview_row,new ArrayList<DeliveryNoteProduct>(),true,lblTatalPayablePreView,deliveryNote.getTotalPayable());
						mlvProductsPreview.setAdapter(mProductsPreviewAdapter);
					}
					mProductsPreviewAdapter.deliveryNoteProducts.clear();
					mProductsPreviewAdapter.deliveryNoteProducts.addAll(deliveryNote.getProducts());
					mProductsPreviewAdapter.refresh();
				}
				else
				{
					findViewById(R.id.trListViewHeader).setVisibility(View.GONE);
				}			
			}
			else
			{
				String  paymentsTypes 	= Utils.getData(getApplicationContext(), Constants.PAYMENTS_TYPE, "Cash");
				String [] paymentsTypesArry  = paymentsTypes.split(",");
				adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,paymentsTypesArry);
				cmbPaymentTypePreView.setAdapter(adapter);
				
				for (int j = 0; j < cmbPaymentTypePreView.getCount(); j++) {
					if(((String) cmbPaymentTypePreView.getItemAtPosition(j)).equalsIgnoreCase(deliveryNote.getPaymentType()))
						cmbPaymentTypePreView.setSelection(j);		
				}
				
				txPresentPayPreView.setEnabled(true);
				cmbPaymentTypePreView.setEnabled(true);
				txBankNamePreView.setEnabled(true);
				txChequeNoPreView.setEnabled(true);
				txBranchAndLocPreView.setEnabled(true);
				txPresentPayPreView.setBackgroundResource(R.drawable.grid_border_yellow);
				txBankNamePreView.setBackgroundResource(R.drawable.grid_border_yellow);
				txChequeNoPreView.setBackgroundResource(R.drawable.grid_border_yellow);
				txBranchAndLocPreView.setBackgroundResource(R.drawable.grid_border_yellow);
				cmbPaymentTypePreView.setBackgroundResource(R.drawable.grid_border_yellow);
				float oldBal = deliveryNote.getPreviousCredit()!=0?deliveryNote.getPreviousCredit():-deliveryNote.getPresentAdvance();
				if(deliveryNote.getProducts() != null && deliveryNote.getProducts().size()!=0)
				{
					mProductsPreviewAdapter = new ProductsPreviewAdapter(context, R.layout.sales_delivery_note_preview_row,deliveryNote.getProducts(),false,lblTatalPayablePreView,oldBal);
					mlvProductsPreview.setAdapter(mProductsPreviewAdapter);
				}
				else
				{
					findViewById(R.id.trListViewHeader).setVisibility(View.GONE);
				}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		

	}// showPreviewScreeen
	public void showSalesReturnProductScreeen()
	{
		try
		{
			previewView.setVisibility(View.GONE);
			previewPaymentBottom.setVisibility(View.GONE);
			previewBottomView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.GONE);
			paymentsBottomView.setVisibility(View.GONE);
			productsBottomView.setVisibility(View.GONE);
			
			productsView.setVisibility(View.VISIBLE);
			salesReturnBottomView.setVisibility(View.VISIBLE);

			//((Button)findViewById(R.id.btnSRPreview)).setText(getResources().getString(R.string.preview));
			//((Button)findViewById(R.id.btnSRAddProduct)).setText(getResources().getString(R.string.addSRProduct));
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	public void showSalesReturnPreviewScreeen(boolean isViewMode)
	{
		try
		{
			productsView.setVisibility(View.GONE);
			productsBottomView.setVisibility(View.GONE);
			salesReturnBottomView.setVisibility(View.GONE);
			paymentsView.setVisibility(View.GONE);
			paymentsBottomView.setVisibility(View.GONE);
			previewView.setVisibility(View.VISIBLE);
			previewPaymentBottom.setVisibility(View.GONE);
			previewBottomView.setVisibility(View.VISIBLE);
			findViewById(R.id.trListViewHeader).setVisibility(View.GONE);
			findViewById(R.id.trListViewSRHeader).setVisibility(View.VISIBLE);
			btnPreview.setTag(0);

			lblInvoiceNoPreView.setText(deliveryNoteSR.getInvoiceNo());
			lblDatePreView.setText(deliveryNoteSR.getCreatedOn());
			lblInvoiceNamePreView.setText(deliveryNoteSR.getInvoiceName());
			lblBusinessNamePreView.setText(deliveryNoteSR.getBusinessName());
			
			if(deliveryNoteSR.getProducts() != null && deliveryNoteSR.getProducts().size()!=0)
			{
				if(mSaleReturnPreviewAdapter == null)
				{
					if(isViewMode)
						mSaleReturnPreviewAdapter = new SalesReturnPreviewAdapter(context, R.layout.sales_returns_products_preview_row,new ArrayList<SalesReturnProduct>(),true);
					else
						mSaleReturnPreviewAdapter = new SalesReturnPreviewAdapter(context, R.layout.sales_returns_products_preview_row,new ArrayList<SalesReturnProduct>(),false);
					mlvProductsPreview.setAdapter(mSaleReturnPreviewAdapter);
				}
				mSaleReturnPreviewAdapter.mProductList.clear();
				for (int i = 0; i < deliveryNoteSR.getProducts().size(); i++) {
					if(deliveryNoteSR.getProducts().get(i).getTotalQty() > 0)
						mSaleReturnPreviewAdapter.mProductList.add(deliveryNoteSR.getProducts().get(i));
				}
				if(mSaleReturnPreviewAdapter.mProductList.size()>0)
					mSaleReturnPreviewAdapter.refresh();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	public void disPlayfinallyResult(Response result)
	{
		try
		{
			android.content.DialogInterface.OnClickListener okclick = new android.content.DialogInterface.OnClickListener()	
			{ public void onClick(DialogInterface arg0, int arg1) {	finish(); }	};
			
			if(result!=null)
				Utils.displayDialog(context,null,result.getMessage(),Utils.DIALOG_OK,"Ok",okclick,null,null,null,null);
			else
				Utils.displayDialog(context,null,"Some Error happened at server side.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	public boolean preparePreviewScreeen()
	{
		try
		{
			if(cmbPaymentType.getSelectedItem().toString().trim().equalsIgnoreCase("CHEQUE"))
			{
				if(txChequeNo.getText().toString().trim().equals(""))
				{
					txChequeNo.setError("Enter ChequeNo");
					txChequeNo.requestFocus();
					return false;
				}
				else if(txBankName.getText().toString().trim().equals(""))
				{
					txBankName.setError("Enter Bank Name");
					txBankName.requestFocus();
					return false;
				}
				else if(txBranchName.getText().toString().trim().equals(""))
				{
					txBranchName.setError("Enter Branch Name");
					txBranchName.requestFocus();
					return false;
				}			
			}
			if(!(deliveryNote.getPresentPayment()==0) && cmbPaymentType.getSelectedItem().toString().trim().equals(Constants.NA))
			{
				Utils.displayDialog(context,null,"Please Select Payment Type",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				return false;
			}
			else if((deliveryNote.getPresentPayment()==0) && !(cmbPaymentType.getSelectedItem().toString().trim().equalsIgnoreCase(Constants.NA)))
			{
				Utils.displayDialog(context,null,"Please Enter The Amount",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				txPresentPayPreView.requestFocus();
				return false;
			}
			if(getIntent().getExtras().getString(Constants.MODE).equals(Constants.NEW + Constants.PY))
			{
				if(deliveryNote.getPresentPayment()==0)
				{
					Utils.displayDialog(context,null,"Please Enter The Amount",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
					txPresentPayPreView.requestFocus();
					return false;
				}
			}
			deliveryNote.setChequeNo(txChequeNo.getText().toString().trim());
			deliveryNote.setBankName(txBankName.getText().toString().trim());
			deliveryNote.setBranchName(txBranchName.getText().toString().trim());
			deliveryNote.setBranchLocation(txBranchLoc.getText().toString().trim().isEmpty()?"":txBranchLoc.getText().toString().trim());
			deliveryNote.setInvoiceNo(String.valueOf(userID.charAt(0)).toUpperCase() + String.valueOf(deliveryNote.getBusinessName().charAt(0)).toUpperCase() +  Utils.getUniqueIDfromDate());
			deliveryNote.setCreatedOn(dateformat.format(new Date()));
			//deliveryNote.setPresentPayment(Float.parseFloat(txPaymentNow.getText().toString().trim().isEmpty()?"0.0":txPaymentNow.getText().toString().trim()));
			//deliveryNote.setBalance(Float.parseFloat(lblRemaingBal.getText().toString()));
			deliveryNote.setPaymentType(cmbPaymentType.getSelectedItem().toString().trim());
			if(mProductsAdapter != null)
			{
				DeliveryNoteProduct product;
				deliveryNote.getProducts().clear();
				for (int i = 0; i < mProductsAdapter.mProductList.size(); i++) {
					product = mProductsAdapter.mProductList.get(i);
					if(product.getProductQty()!=0 || product.getBonusQty() != 0){
						deliveryNote.getProducts().add(product);		
					}
				}
			}
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	public boolean prepareSRPreviewScreeen()
	{
		try
		{
			deliveryNoteSR.setInvoiceNo(String.valueOf(userID.charAt(0)).toUpperCase() + String.valueOf(deliveryNoteSR.getBusinessName().charAt(0)).toUpperCase() +  Utils.getUniqueIDfromDate());
			deliveryNoteSR.setCreatedOn(dateformat.format(new Date()));
			
			if(mSaleReturnProductsAdapter != null)
			{
				deliveryNoteSR.getProducts().clear();
				SalesReturnProduct product;
				mSaleReturnProductsAdapter.refresh();
				for (int i = 0; i < mSaleReturnProductsAdapter.mProductList.size(); i++) {
					product = mSaleReturnProductsAdapter.mProductList.get(i);
					if(product.getTotalQty() > 0){
						deliveryNoteSR.getProducts().add(product);
					}
				}
			}
			if(deliveryNoteSR.getProducts().size() == 0 )
			{
				Utils.displayDialog(DeliveryNoteActivity.this, null, "Please Select one of Product.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
				return false;
			}			
			//((Button)findViewById(R.id.btnSRPreview)).setText(getResources().getString(R.string.back));
			//((Button)findViewById(R.id.btnSRAddProduct)).setText(getResources().getString(R.string.save));
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	// ***************************************
	// Private classes http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	// ***************************************
	class LoadDeliveryNote extends AsyncTask<String, Void, DeliveryNote>
	{
		boolean isViewMode;
		public LoadDeliveryNote(boolean isViewMode)
		{
			this.isViewMode = isViewMode;
		}
		protected void onPreExecute(){showProgressDialog("Loading Delivery Note Information..... Please wait...");}
		@Override
		protected DeliveryNote doInBackground(String... params)
		{  
			try
			{
				deliveryNote = salesDao.readDeliveryNote(VbookApp.getDbInstance(), params[0], Utils.getData(VbookApp.getInstance(), Constants.USER_ID, "0"),isViewMode);
				return deliveryNote;
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(DeliveryNote result)
		{
			try
			{
				dismissProgressDialog();
				if(result!=null)
				{
					showPreviewScreeen(isViewMode);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
	}// LoadDeliveryNote
	
	class LoadSalesReturnDN extends AsyncTask<String, Void, SalesReturn>
	{
		boolean isViewMode;
		public LoadSalesReturnDN(boolean isViewMode)
		{
			this.isViewMode = isViewMode;
		}
		protected void onPreExecute(){showProgressDialog("Loading Sales Returns Information..... Please wait...");}
		@Override
		protected SalesReturn doInBackground(String... params)
		{  
			try
			{
				deliveryNoteSR = salesDao.readSalesReturn(VbookApp.getDbInstance(), params[0], Utils.getData(VbookApp.getInstance(), Constants.USER_ID, "0"));
				return deliveryNoteSR;
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(SalesReturn result)
		{
			try
			{
				dismissProgressDialog();
				if(result!=null)
				{
					showSalesReturnPreviewScreeen(isViewMode);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}// LoadSalesReturnDN
	
	class LoadProductList extends AsyncTask<Void, Void, List<DeliveryNoteProduct>>
	{
		final String url = getString(R.string.BASE_URL) + "productlist?uname={uname}&orgID={orgID}&bname={bname}";
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of allocated Products ..... Please wait...");
		}
		
		@Override
		protected List<DeliveryNoteProduct> doInBackground(Void... params)
		{
			try
			{				
				String dayid = Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID, Constants.NA);
				return salesDao.readAllocatedproducts(VbookApp.getDbInstance(),userID,dayid,custId);
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return null;
		}

		protected void onPostExecute(List<DeliveryNoteProduct> result)
		{
			try
			{
				dismissProgressDialog();
				if(mProductsAdapter!= null && mProductsAdapter.mProductList.size() == 0){
					mbtnPayment.setEnabled(false);
					Utils.displayDialog(context,null,"No Products Allotted.",Utils.DIALOG_OK,"Ok",new DialogInterface.OnClickListener(){
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					      	finish();
					    }
					}, null,null,null, null);
					return;
				}
					
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	}
	
	class LoadSoldProductList extends AsyncTask<Void, Void, List<SalesReturnProduct>>
	{
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of Sold Products ..... Please wait...");
		}
		
		@Override
		protected List<SalesReturnProduct> doInBackground(Void... params)
		{
			try
			{
				return salesDao.readsoldproducts(VbookApp.getDbInstance(),userID,deliveryNoteSR.getBusinessName());
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

		protected void onPostExecute(List<SalesReturnProduct> result)
		{
			try
			{
				dismissProgressDialog();
				if(result!=null)
				{
					mSaleReturnProductsAdapter = new SalesReturnAdapter(context, R.layout.sales_returns_products_preview_row,result,false);
					mlvProductlist.setAdapter(mSaleReturnProductsAdapter);
				}
				if(mProductsAdapter!= null && mProductsAdapter.mProductList.size() == 0)
					mbtnPayment.setEnabled(false);
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}

	}// LoadSoldProductList
	
	class SaveDeliveryNote extends AsyncTask<Void, Void, Response>
	{
		boolean isChangeReq;
		boolean isSaleReturn;
		public SaveDeliveryNote(boolean isChangeReq,boolean isSaleReturn)
		{
			this.isChangeReq 	= isChangeReq;
			this.isSaleReturn 	= isSaleReturn;
		}
		protected void onPreExecute()
		{
			if(isSaleReturn)
				showProgressDialog("SalesReturn  Saveing..... Please wait...");
			else
				showProgressDialog("Delivery  Saveing..... Please wait...");
		}
		
		@Override
		protected Response doInBackground(Void... params)
		{
			try
			{
				if(isSaleReturn)
					salesDao.saveSalesReturn(VbookApp.getDbInstance(),deliveryNoteSR,isChangeReq);
				else
					salesDao.saveDeliveryNote(VbookApp.getDbInstance(),deliveryNote,isChangeReq);
				
				Response result = new Response();
				result.setMessage("Sucessfully saved.");
				return result;
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return null;
		}
		protected void onPostExecute(Response result)
		{
			try
			{
				dismissProgressDialog();			
				disPlayfinallyResult(result);
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}// SaveDeliveryNote
}