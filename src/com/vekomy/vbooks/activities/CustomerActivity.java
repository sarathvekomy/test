/**
 * com.vekomy.vbooks.activities.CustmourActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 29-Jul-2013
 *
 * @author nkr
 *
 *
 */

package com.vekomy.vbooks.activities;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.validation.Rule;
import com.vekomy.validation.Validator;
import com.vekomy.validation.Validator.ValidationListener;
import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.CustomerModifyAdapter;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.app.response.CustomerInfo;
import com.vekomy.vbooks.database.CustomerDao;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.AddCustomerHelper;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 * 
 */
public class CustomerActivity extends AddCustomerHelper implements	OnClickListener, OnItemClickListener,OnTabChangeListener, ValidationListener 
{
	/**
	 * The Context
	 */
	private Context context;

	/**
	 * The validator
	 */
	private Validator validator;
	TabHost mtabHost;
	private ListView 				mlvModifyCustlist;
	private CustomerModifyAdapter 	mCustAdapter;
	private View 					customerListView;
	private View 					customerAddView;
	private String					mBusinessName;
	private String					mRef_id;
	CustomerDao						mcustomerDao;
	
	Button btnsubmit,btnclear;
	int mSelectedPosition =-1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.custmours_module);
			this.context = this;
			userID 			=	Utils.getData(this, Constants.USER_ID, Constants.NA);
			orgID			=	Utils.getData(this, Constants.ORG_ID, "0");
			mcustomerDao	=	new CustomerDao();
			mtabHost 		= (TabHost) findViewById(android.R.id.tabhost);
			mtabHost.setup();
			
			customerListView=	findViewById(R.id.llcustomerListView);
			customerAddView	=	findViewById(R.id.llcustomerAddView);
			addTab(mtabHost,R.id.llcustomerListView,"Modify Customer",R.drawable.modify_cust);
			addTab(mtabHost,R.id.llcustomerAddView,"NewCustomer",R.drawable.new_cust);
			mtabHost.setOnTabChangedListener(this);
			validator = new Validator(this);
			initializeFields();
			validator.setValidationListener(this);
			btnsubmit=(Button) findViewById(R.id.btnSubmit);
			btnsubmit.setOnClickListener(this);
			btnclear=(Button) findViewById(R.id.btnClear);
			btnclear.setOnClickListener(this);
			((Button) findViewById(R.id.btnCancel)).setOnClickListener(this);
			new LoadCustomerListTask().execute();
			mlvModifyCustlist = (ListView) findViewById(R.id.lvModifyCustlist);
			mlvModifyCustlist.setOnItemClickListener(this);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	public void OnbtnNextClick(View v)
	{
		try
		{
			mBusinessName 		= ((CustomerAmountInfo) v.getTag()).getBusinessName();
			View row 			= (View) v.getParent();									
			mSelectedPosition 	= ((CustomerAmountInfo) v.getTag()).getPos();
			for (int i = 0; i < mCustAdapter.mCustInfoList.size(); i++) 
			{
				row = mlvModifyCustlist.getChildAt(i);
				if(row==null)
					continue;
				
				if (i == mSelectedPosition)
				{
					row.setBackgroundResource(R.drawable.grid_border_yellow);
				}
				else
				{
					row.setBackgroundResource(R.drawable.bluegradient);					
				}
			}
			new LoadCustomerTask().execute(mBusinessName);			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void addTab(TabHost tabHost,int viewId,String labelId, int drawableId)
	{
		try
		{
			TabHost.TabSpec spec = tabHost.newTabSpec(labelId);	
			View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, tabHost.getTabWidget(), false);
			TextView title = (TextView) tabIndicator.findViewById(R.id.title);
			title.setText(labelId);
			
			ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
			icon.setImageResource(drawableId);
	
			spec.setIndicator(tabIndicator);
			spec.setContent(viewId);
			tabHost.addTab(spec);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	

	@Override
	public void onTabChanged(String tabId) {
		try
		{
			if(mtabHost.getCurrentTab()==0)
			{
				customerListView.setVisibility(View.VISIBLE);
				businessName.setEnabled(false);
			}
			else
			{
				clearFieldData();
				customerAddView.setVisibility(View.VISIBLE);
				businessName.setEnabled(true);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}			
	}

	@Override
	public void onClick(View v) {
		try
		{
			switch (v.getId())
			{
				case R.id.btnSubmit:
				{
					validator.validate();
				}
				break;
				case R.id.btnClear:
				{
					clearFieldData();
				}
				break;
				case R.id.btnCancel:
				{
					if(mtabHost.getCurrentTab()==0) // Modify Tab
					{
						customerAddView.setVisibility(View.GONE);
						customerListView.setVisibility(View.VISIBLE);
					}
					else
						finish();
						
				}
				break;
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	// ***************************************
	class LoadCustomerListTask extends AsyncTask<Void, Void, List<CustomerAmountInfo>>
	{
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of Assign customer  ..... Please wait...");
		}
		@Override
		protected List<CustomerAmountInfo> doInBackground(Void... params)
		{
			try
			{
				String dayid = Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID, Constants.NA);
				return new SalesDao().readAssiginCustomers(VbookApp.getDbInstance(),userID,dayid);
				
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return null;
		}

		protected void onPostExecute(List<CustomerAmountInfo> result)
		{
			try
			{
				if(result!=null)
				{
					mCustAdapter = new CustomerModifyAdapter(context, R.layout.customer_list_modify_row,result);
					mlvModifyCustlist.setAdapter(mCustAdapter);				
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
			dismissProgressDialog();
		}
	}
	
	class LoadCustomerTask extends AsyncTask<String, Void, CustomerInfo>
	{
		protected void onPreExecute()
		{
			showProgressDialog("Getting Selected customer  ..... Please wait...");
		}
		@Override
		protected CustomerInfo doInBackground(String... params)
		{
			try
			{
				return mcustomerDao.readCustomer(VbookApp.getDbInstance(),params[0]);
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return null;
		}

		protected void onPostExecute(CustomerInfo customer)
		{
			try
			{
				dismissProgressDialog();
				if(customer!=null)
				{
					if(!customer.getIsApproved())
					{
						Utils.displayDialog(context,null,"Customer is under Approval.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
						customerAddView.setVisibility(View.GONE);
						customerListView.setVisibility(View.VISIBLE);
						return;
					}
					mRef_id = customer.getRefID();
					businessName.setText((customer.getBusinessName() == null || customer.getBusinessName().equals("null")) ?"":customer.getBusinessName());
					customerName.setText((customer.getCustomerName() == null || customer.getCustomerName().equals("null"))?"":customer.getCustomerName());
					if(Character.toUpperCase(customer.getGender())=='M')
					{
						male.setChecked(true);	
					}
					else
					{
						female.setChecked(true);
					}
					invoiceName.setText((customer.getInvoiceName() == null 	|| customer.getInvoiceName().equals("null"))?"":customer.getInvoiceName());
					address1.setText((customer.getAddressLine1() == null 	|| customer.getAddressLine1().equals("null"))?"":customer.getAddressLine1());
					address2.setText((customer.getAddressLine2()==null 		|| customer.getAddressLine2().equals("null"))?"":customer.getAddressLine2());
					region.setText((customer.getRegion() == null 			|| customer.getRegion().equals("null"))?"":customer.getRegion());
					locality.setText((customer.getLocality() == null 		|| customer.getLocality().equals("null"))?"":customer.getLocality());
					landmark.setText((customer.getLandmark() == null 		|| customer.getLandmark().equals("null"))?"":customer.getLandmark());
					city.setText((customer.getCity() == null 				|| customer.getCity().equals("null"))?"":customer.getCity());
					state.setText((customer.getState() == null 				|| customer.getState().equals("null"))?"":customer.getState());
					zipcode.setText((customer.getZipcode() == null 			|| customer.getZipcode().equals("null"))?"":customer.getZipcode());
				//	addressType.setText((customer.getAddressType() == null 	|| customer.getAddressType().equals("null"))?"":customer.getAddressType());
					contactNumber.setText((customer.getMobile() == null 	|| customer.getMobile().equals("null"))?"":customer.getMobile());
					alternateContactNumber.setText((customer.getAlternateMobile() == null || customer.getAlternateMobile().equals("null"))?"":customer.getAlternateMobile());
					emailAddress.setText((customer.getEmail() == null 		|| customer.getEmail().equals("null"))?"":customer.getEmail());
					directLine.setText((customer.getDirectLine() == null 	|| customer.getDirectLine().equals("null"))?"":customer.getDirectLine());
					customerListView.setVisibility(View.GONE);
					customerAddView.setVisibility(View.VISIBLE);
				}
				else
				{
					Utils.displayDialog(context,null,"Customer Info not Loaded,try again after some time..",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
	}

	class SaveCustomer extends AsyncTask<CustomerInfo, Void, Integer>
	{
		protected void onPreExecute()
		{
			showProgressDialog("Saveing customer info ..... Please wait...");
		}
		@Override
		protected Integer doInBackground(CustomerInfo... customer)
		{
			try
			{
				return mcustomerDao.insertOrUpdateCustomer(VbookApp.getDbInstance(), userID, customer[0],false);
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
			}
			return 0;
		}

		protected void onPostExecute(Integer result)
		{		
			try
			{
				dismissProgressDialog();
				if (result != -1) {
					if(mtabHost.getCurrentTab()==0)
					{
						Utils.displayDialog(context, null, "Customer Info Updated successfully.",	Utils.DIALOG_OK, "Ok", null, null, null, null, null);
						customerAddView.setVisibility(View.GONE);
						customerListView.setVisibility(View.VISIBLE);
					}
					else
						Utils.displayDialog(context, null, "Customer Info Saved successfully.",	Utils.DIALOG_OK, "Ok", null, null, null, null, null);
					clearFieldData();
					
				} else {
					Utils.displayDialog(context, null,"Customer Info Saved Failed.", Utils.DIALOG_OK,"Ok", null, null, null, null, null);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}

	}
	@Override
	public void preValidation() {
	}
	@Override
	public void onValidationSuccess() {
		try
		{
			final Dialog dialog = new Dialog(context, R.style.customDialogStyle);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.customer_preview);
			dialog.setCancelable(false);
			Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
			Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
			
			
				((TextView) dialog.findViewById(R.id.lblCustomerName)).setText(customerName.getText().toString());
				((TextView) dialog.findViewById(R.id.lblContactNo)).setText(contactNumber.getText().toString());
				((TextView) dialog.findViewById(R.id.lblInvoiceName)).setText(invoiceName.getText().toString());
				((TextView) dialog.findViewById(R.id.lblLocality)).setText(locality.getText().toString());
				((TextView) dialog.findViewById(R.id.lblRegion)).setText(region.getText().toString());
				((TextView) dialog.findViewById(R.id.lblBusinessName)).setText(businessName.getText().toString());
			
			
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) 
				{
					dialog.dismiss();
				}
			});
			btnSave.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					CustomerInfo request = new  CustomerInfo();
					request.setSalesExecutive(userID);
					request.setOrganizationId(Integer.parseInt(orgID));
					if(mtabHost.getCurrentTab() == 0) // Modified 
					{
						mcustomerDao.setunApproved(VbookApp.getDbInstance(),mRef_id);
						request.setRefID(mRef_id + Constants.CR);
						request.setCrType(true);
					}
					else
					{
						request.setRefID(String.valueOf(userID.charAt(0)).toUpperCase() + Utils.getUniqueIDfromDate()+ Constants.CR);
						request.setCrType(false);
					}
					request.setBusinessName(businessName.getText().toString().trim());
					request.setCustomerName(customerName.getText().toString().trim());
					request.setGender(male.isChecked() == true ? 'M' : 'F');
					request.setInvoiceName(invoiceName.getText().toString().trim());
					request.setAddressLine1(address1.getText().toString().trim());
					request.setAddressLine2(address2.getText().toString().trim());
					request.setRegion(region.getText().toString().trim());
					request.setLocality(locality.getText().toString().trim());
					request.setLandmark(landmark.getText().toString().trim());
					request.setCity(city.getText().toString().trim());
					request.setState(state.getText().toString().trim());
					request.setZipcode(zipcode.getText().toString().trim());
					//request.setAddressType(addressType.getText().toString().trim());
					request.setMobile(contactNumber.getText().toString().trim());
					request.setAlternateMobile(alternateContactNumber.getText().toString().trim());
					request.setEmail(emailAddress.getText().toString().trim());
					request.setDirectLine(directLine.getText().toString().trim());
					new SaveCustomer().execute(request);
				}
			});
			dialog.show();
		}	
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText || failedView instanceof RadioGroup) {
			failedView.requestFocus();
			((TextView) failedView).setError(message);
		} else {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onValidationCancelled() {
	}	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(getApplicationContext(), "Selected one is"+arg2, Toast.LENGTH_SHORT).show();
	}
}
