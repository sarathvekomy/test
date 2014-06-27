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

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.CustomerAdapter;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author koteswararao
 *
 */
public class SalesActivity extends ProgressActivity implements OnTabChangeListener
{
	
	private ListView mlvCustlist;
	private CustomerAdapter mCustAdapter;
	//private Button 	mBtnDisplay;	
	//private View 	custmourListView;
	private SalesDao salesDao;
	TabHost mTabHost;
	int mSelectedPosition =-1;
	ImageView imgListView;
	/**
	 * The Context
	 */
	LinearLayout llCustomerList;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.sales_module);
			this.context = this;
			userID 	=	Utils.getData(this, Constants.USER_ID, Constants.NA);
			orgID	=	Utils.getData(this, Constants.ORG_ID, "0");
	
			mlvCustlist = (ListView) findViewById(R.id.lvcustlist);
			imgListView = (ImageView) findViewById(R.id.imgListView);
			//mlvCustlist.setOnItemClickListener(this);
			new CustomerListTask().execute();
			
			mTabHost = (TabHost) findViewById(R.id.tabHost);
			mTabHost.setup();
	
			addTab(mTabHost,R.id.lvcustlist,"Invoice",R.drawable.tab_invoice);
			addTab(mTabHost,R.id.lvcustlist,"PaymentCollection",R.drawable.tab_payments);

			addTab(mTabHost,R.id.lvcustlist,"SalesReturn",R.drawable.tab_sales_return);
			addTab(mTabHost,R.id.lvcustlist,"AddJournal",R.drawable.tab_journal);
			mTabHost.setCurrentTab(2);
			/*TabSpec invoice = mTabHost.newTabSpec("Invoice");
			invoice.setIndicator("", getResources().getDrawable(R.drawable.invoice_selection));
			invoice.setContent(R.id.rlDeliveryNoteView);
		
			
			TabSpec paymentCollection = mTabHost.newTabSpec("PaymentCollection");
			paymentCollection.setIndicator("", getResources().getDrawable(R.drawable.payment_selection));
			paymentCollection.setContent(R.id.rlDeliveryNoteView);
	
			TabSpec stockReturns = mTabHost.newTabSpec("StockReturns");
			stockReturns.setIndicator("", getResources().getDrawable(R.drawable.sales_return_selection));
			stockReturns.setContent(R.id.rlDeliveryNoteView);
			
			TabSpec addJournel = mTabHost.newTabSpec("AddJounel");
			addJournel.setIndicator("", getResources().getDrawable(R.drawable.journel_selection));
			addJournel.setContent(R.id.rlDeliveryNoteView);
	
			mTabHost.addTab(invoice);
			mTabHost.addTab(paymentCollection);
			mTabHost.addTab(stockReturns);
			mTabHost.addTab(addJournel);*/
	
			mTabHost.setOnTabChangedListener(this);
			
			//mBtnDisplay = (Button) findViewById(R.id.btnDisplay);
			//mBtnDisplay.setOnClickListener(this);
			//custmourListView 	= findViewById(R.id.rlDeliveryNoteView);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	@Override
	protected void onRestart() 
	{
		try
		{
			super.onRestart();
			if(mCustAdapter!=null && mSelectedPosition !=-1 && mSelectedPosition < mCustAdapter.getCount() )
			{
				CustomerAmountInfo customer = mCustAdapter.getItem(mSelectedPosition);
				float balance = 0;
	
				if(salesDao != null)
				{
					String dayid = Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID, Constants.NA);
					
					balance = salesDao.getCustomerBal(VbookApp.getDbInstance(),userID, dayid, customer.getBusinessName());
					
					if(balance>=0)
					{
						customer.setAdvanceAmount(balance);
						customer.setCreditAmount(0f);
					}
					else
					{
						customer.setCreditAmount(-1*balance);
						customer.setAdvanceAmount(0f);
					}
					// replace here
					mCustAdapter.replace(mSelectedPosition,customer);
					refreshListView();
					//View row = mlvCustlist.getChildAt(mSelectedPosition);
					//if(row!=null)
					//	row.setBackgroundResource(R.drawable.grid_border_yellow);
				}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	public void refreshListView()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					mCustAdapter.notifyDataSetChanged();
					System.gc();
				}
			});
		} catch (Exception ex)
		{
		}
	}

	// ***************************************
	// Private classes http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	// ***************************************
	class CustomerListTask extends AsyncTask<Void, Void, List<CustomerAmountInfo>>
	{
		final String url = getString(R.string.BASE_URL) + "customerlist?uname={uname}&orgID={orgID}";
		protected void onPreExecute()
		{
			showProgressDialog("Getting List of Assign customer  ..... Please wait...");
		}
		
		@Override
		protected List<CustomerAmountInfo> doInBackground(Void... params)
		{
			try
			{
				salesDao = new SalesDao();
				String dayid = Utils.getData(VbookApp.getInstance(), userID +Constants.CYCLE_ID, Constants.NA);
				return salesDao.readAssiginCustomers(VbookApp.getDbInstance(),userID,dayid);
				
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
					mCustAdapter = new CustomerAdapter(context,R.layout.sales_customer_list_row,result);
					mlvCustlist.setAdapter(mCustAdapter);
					//if(mCustAdapter.mCustInfoList.size()==0)
						//mBtnDisplay.setEnabled(false);
				}
				dismissProgressDialog();
				mTabHost.setCurrentTab(0);
				android.content.DialogInterface.OnClickListener okclick = new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						finish();
					}
				};
				if(result.size()==0){
					Utils.displayDialog(context,null,"Present no customers assigned for you.",Utils.DIALOG_OK,"Ok",okclick,null,null,null,null);
					return;
				}
				
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}	
	
	public void OnbtnNextClick(View v) 
	{
		try
		{
			//
			// deselect process
			// 
			View row = mlvCustlist.getChildAt(mSelectedPosition);
			if(row!=null)
				row.setBackgroundResource(R.drawable.grid_border_blue);
			row = mlvCustlist.getSelectedView();
			if(row!=null)
				row.setBackgroundResource(R.drawable.grid_border_blue);
			
			//
			// selection process
			//
			CustomerAmountInfo customerInfo =  (CustomerAmountInfo) v.getTag();
			row = (View) v.getParent();			
			if(row!=null)
				row.setBackgroundResource(R.drawable.grid_border_yellow);						
			mSelectedPosition = customerInfo.getPos();
			
			for (int i = 0; i < mCustAdapter.mCustInfoList.size(); i++) {
                if (i == mSelectedPosition)    
                	mCustAdapter.mBtnRadioArry[i] = 1;
                else
                	mCustAdapter.mBtnRadioArry[i] = 0;
            }
			if(mTabHost.getCurrentTab() == 0)
			{
				Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.NEW + Constants.DN);
				intent.putExtra(Constants.P_CUSTID, customerInfo.getUid());
				intent.putExtra(Constants.P_BNAME, customerInfo.getBusinessName());
				intent.putExtra(Constants.P_INAME, customerInfo.getInvoiceName());
				intent.putExtra(Constants.P_CAMT, String.valueOf(customerInfo.getCreditAmount()));
				intent.putExtra(Constants.P_AAMT, String.valueOf(customerInfo.getAdvanceAmount()));
				startActivity(intent);	
			}
			else if(mTabHost.getCurrentTab() == 1)
			{
				Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.NEW + Constants.PY);
				intent.putExtra(Constants.P_CUSTID, customerInfo.getUid());
				intent.putExtra(Constants.P_BNAME, customerInfo.getBusinessName());
				intent.putExtra(Constants.P_INAME, customerInfo.getInvoiceName());
				intent.putExtra(Constants.P_CAMT, String.valueOf(customerInfo.getCreditAmount()));
				intent.putExtra(Constants.P_AAMT, String.valueOf(customerInfo.getAdvanceAmount()));
				startActivity(intent);	
			}
			else if(mTabHost.getCurrentTab() == 2)
			{
				Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.NEW + Constants.SR);
				intent.putExtra(Constants.P_BNAME, customerInfo.getBusinessName());
				intent.putExtra(Constants.P_INAME, customerInfo.getInvoiceName());
				intent.putExtra(Constants.P_CAMT, String.valueOf(customerInfo.getCreditAmount()));
				intent.putExtra(Constants.P_AAMT, String.valueOf(customerInfo.getAdvanceAmount()));
				startActivity(intent);	
			}
			else if(mTabHost.getCurrentTab() == 3)
			{
				Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
				intent.putExtra(Constants.MODE, Constants.NEW + Constants.JN);
				intent.putExtra(Constants.P_BNAME, customerInfo.getBusinessName());
				intent.putExtra(Constants.P_INAME, customerInfo.getInvoiceName());
				startActivity(intent);
			}
		} catch (Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	/*public void onClick(View v)
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
					CustomerAmountInfo deliveryNote = mCustAdapter.getItem(mCustAdapter.mSelectedPosition);
					if(mBtnDisplay.getText().equals("Sales return"))
					{
						Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
						intent.putExtra(Constants.MODE, Constants.NEW + Constants.SR);
						intent.putExtra(Constants.P_BNAME, deliveryNote.getBusinessName());
						intent.putExtra(Constants.P_INAME, deliveryNote.getInvoiceName());
						intent.putExtra(Constants.P_CAMT, String.valueOf(deliveryNote.getCreditAmount()));
						intent.putExtra(Constants.P_AAMT, String.valueOf(deliveryNote.getAdvanceAmount()));
						startActivity(intent);	
					}
					else if(mBtnDisplay.getText().equals("Payments"))
					{
						Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
						intent.putExtra(Constants.MODE, Constants.NEW + Constants.PY);
						intent.putExtra(Constants.P_CUSTID, deliveryNote.getUid());
						intent.putExtra(Constants.P_BNAME, deliveryNote.getBusinessName());
						intent.putExtra(Constants.P_INAME, deliveryNote.getInvoiceName());
						intent.putExtra(Constants.P_CAMT, String.valueOf(deliveryNote.getCreditAmount()));
						intent.putExtra(Constants.P_AAMT, String.valueOf(deliveryNote.getAdvanceAmount()));
						startActivity(intent);						
						
					}else if(mBtnDisplay.getText().equals("AddJounel")){
						Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
						intent.putExtra(Constants.MODE, Constants.NEW + Constants.JN);
						intent.putExtra(Constants.P_BNAME, deliveryNote.getBusinessName());
						intent.putExtra(Constants.P_INAME, deliveryNote.getInvoiceName());
						startActivity(intent);	
					}
					else
					{
						Intent intent = new Intent(getApplicationContext(), DeliveryNoteActivity.class);
						intent.putExtra(Constants.MODE, Constants.NEW + Constants.DN);
						intent.putExtra(Constants.P_CUSTID, deliveryNote.getUid());
						intent.putExtra(Constants.P_BNAME, deliveryNote.getBusinessName());
						intent.putExtra(Constants.P_INAME, deliveryNote.getInvoiceName());
						intent.putExtra(Constants.P_CAMT, String.valueOf(deliveryNote.getCreditAmount()));
						intent.putExtra(Constants.P_AAMT, String.valueOf(deliveryNote.getAdvanceAmount()));
						startActivity(intent);						
					}
					//Toast.makeText(getApplicationContext(), "selected Custmour is " + mCustAdapter.mSelectedPosition, Toast.LENGTH_LONG).show();
				}break;
			}
		} catch (Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid selection", Toast.LENGTH_SHORT).show();
		}
	}*/
	@Override
	public void onTabChanged(String tabId) 
	{
		try
		{		
	        if (tabId.equals("Invoice")) 
	        {
	        	//mBtnDisplay.setText("Generate invoice");
	        	//custmourListView.setVisibility(View.VISIBLE);
	        	//mlvCustlist.setBackgroundResource(R.drawable.invoice_bg);
	        	imgListView.setImageBitmap(Utils.getBmpFromRes(SalesActivity.this.getResources(),R.drawable.invoice_bg));
	        }
	        else if (tabId.equals("PaymentCollection"))
	        {
	        	//mBtnDisplay.setText("Payments");
	        	//custmourListView.setVisibility(View.VISIBLE);
	        	//mlvCustlist.setBackgroundResource(R.drawable.payment_collection_bg);
	        	imgListView.setImageBitmap(Utils.getBmpFromRes(SalesActivity.this.getResources(),R.drawable.payment_collection_bg));
	        }
	        else if (tabId.equals("SalesReturn"))        	
	        {
	        	//mBtnDisplay.setText("Sales return");
	        	//custmourListView.setVisibility(View.VISIBLE);
	        	//mlvCustlist.setBackgroundResource(R.drawable.sales_return_bg);
	        	imgListView.setImageBitmap(Utils.getBmpFromRes(SalesActivity.this.getResources(),R.drawable.sales_return_bg));
	        }else if(tabId.equals("AddJournal")){
	        	
	        	//mBtnDisplay.setText("AddJounel");
	        	//custmourListView.setVisibility(View.VISIBLE);
	        	//mlvCustlist.setBackgroundResource(R.drawable.journal_bg);
	        	imgListView.setImageBitmap(Utils.getBmpFromRes(SalesActivity.this.getResources(),R.drawable.journal_bg));
	        }
	        System.gc();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	private void addTab(TabHost tabHost,int viewId,String labelId, int drawableId)
	{
		try
		{
			//TabSpec invoice = mTabHost.newTabSpec("Invoice");
			//invoice.setIndicator("", getResources().getDrawable(R.drawable.invoice_selection));
			//invoice.setContent(R.id.rlDeliveryNoteView);
			//Intent intent = new Intent(this, c);
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
}