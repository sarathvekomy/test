package com.vekomy.vbooks.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.TrxnHistoryAdapter;
import com.vekomy.vbooks.app.response.TrxnHistory;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.utils.Utils;

public class ModifyTrxnHistoryActivity extends ProgressActivity implements OnItemClickListener,OnItemSelectedListener {

	Context 			context;
	ListView    		lvTrxnHistory;
	TrxnHistoryAdapter  trxnHistoryAdapter;		
	Spinner            	cmbTrxnType;
	Spinner            	cmbTrxnDate;
	Spinner            	cmbTrxnName;
	String				dayId;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.view_transactions_view_display);
			this.context=this;							
			userID 				=	Utils.getData(getApplicationContext(), Constants.USER_ID, Constants.NA);
			dayId				=	Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID, Constants.NA);
			lvTrxnHistory     	=      (ListView) findViewById(R.id.listViewTrxnHistory);
			lvTrxnHistory.setClickable(true);
			lvTrxnHistory.setOnItemClickListener(this);		
			cmbTrxnType         =      (Spinner) findViewById(R.id.spinnerTrxnType);
			cmbTrxnDate         =      (Spinner) findViewById(R.id.spinnerTrxnDate);
			cmbTrxnName         =      (Spinner) findViewById(R.id.spinnerTrxnBN);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,new String[]{Constants.VIEW_TRANSACTION,Constants.DELIVERY_NOTE,Constants.SALES_RETURN,Constants.JOURNAL,Constants.DAY_BOOK});
			cmbTrxnType.setAdapter(adapter);		
			cmbTrxnType.setOnItemSelectedListener(this);
			cmbTrxnDate.setOnItemSelectedListener(this);
			cmbTrxnName.setOnItemSelectedListener(this);
			new LoadTrxns(false).execute();
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
			new LoadTrxns(true).execute();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	private class LoadTrxns extends AsyncTask<Void, Void,List<TrxnHistory>>
	{
		boolean isOnRestart;
		public LoadTrxns(boolean isOnRestart)
		{
			this.isOnRestart = isOnRestart;
		}
		@Override
		protected void onPreExecute()
		{
			showProgressDialog("Loding .... Transaction History.");
		}
		@Override
		protected List<TrxnHistory> doInBackground(Void... params) {			
			return new SalesDao().getAllReportsToday(VbookApp.getDbInstance(), userID,dayId);
		}
		@Override
		protected void onPostExecute(List<TrxnHistory> result) 
		{
			try
			{			
				super.onPostExecute(result);			
				dismissProgressDialog();
				if(isOnRestart)
				{
					trxnHistoryAdapter.reloadAdapter(result);
					cmbTrxnType.setSelection(0);
					cmbTrxnDate.setSelection(0);
					cmbTrxnName.setSelection(0);
					refreshListView();
				}
				else
				{
					Set<String>  dateSet 	=	new TreeSet<String>();
					Set<String>  nameSet	=	new TreeSet<String>();
					List<String>			 dateList	=	new ArrayList<String>();
					List<String>			 nameList	=	new ArrayList<String>();
					dateList.add(Constants.VIEW_DATE);
					nameList.add(Constants.VIEW_NAME);
					if(result!=null)
					{
						trxnHistoryAdapter = new TrxnHistoryAdapter(context,R.layout.trxn_history_row,result);
						lvTrxnHistory.setAdapter(trxnHistoryAdapter);
						for(int i=0; i< trxnHistoryAdapter.alltrxnViewList.size();i++){
						    dateSet.add(trxnHistoryAdapter.alltrxnViewList.get(i).getDateID());
							if(!trxnHistoryAdapter.alltrxnViewList.get(i).getName().isEmpty())
								nameSet.add(trxnHistoryAdapter.alltrxnViewList.get(i).getName());
						}
						dateList.addAll(dateSet);
						nameList.addAll(nameSet);
					}
					cmbTrxnDate.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,dateList));
					cmbTrxnName.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,nameList));
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}				
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	{
		try
		{
			String sales_ref_id = trxnHistoryAdapter.getItem(position).getId();
			if(sales_ref_id.endsWith(Constants.CR))
			{
				Utils.displayDialog(ModifyTrxnHistoryActivity.this, null, "This Transaction is Not Approved from Management.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
				return;
			}		
			for (int i = 0; i < trxnHistoryAdapter.alltrxnViewList.size(); i++) {
				if(trxnHistoryAdapter.alltrxnViewList.get(i).getId().equalsIgnoreCase(sales_ref_id+Constants.CR))
				{
					Utils.displayDialog(ModifyTrxnHistoryActivity.this, null, "This Transaction is under Approvel.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
					return;
				}
			}
			Intent intent = null;		
			String bName=trxnHistoryAdapter.alltrxnViewList.get(position).getName();
			if(sales_ref_id.startsWith(Constants.DN))
			{
				intent  = new Intent(getApplicationContext(),DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.MODIFY + Constants.DN);
				intent.putExtra(Constants.P_CUSTID, bName);
			}
			else if(sales_ref_id.startsWith(Constants.SR))
			{
				intent = new Intent(getApplicationContext(),DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.MODIFY + Constants.SR);
				intent.putExtra(Constants.P_CUSTID, bName);
			}
			else if(sales_ref_id.startsWith(Constants.JN))
			{
				intent = new Intent(getApplicationContext(),JournalActivity.class);			
				intent.putExtra(Constants.MODE, Constants.MODIFY + Constants.JN);
				intent.putExtra(Constants.P_CUSTID, bName);
			}		
			else
			{
				intent  = new Intent(getApplicationContext(),DayBookActivity.class);
				intent.putExtra(Constants.MODE, Constants.MODIFY + Constants.DB);
			}
			intent.putExtra(Constants.SALES_REF_ID,sales_ref_id );
			startActivity(intent);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
		try
		{		
			if(cmbTrxnType.getSelectedItem() == null || cmbTrxnDate.getSelectedItem() == null || cmbTrxnName.getSelectedItem() == null)
				return;
			trxnHistoryAdapter.filter(cmbTrxnType.getSelectedItem().toString(), cmbTrxnDate.getSelectedItem().toString(), cmbTrxnName.getSelectedItem().toString());
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	public void refreshListView()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				public void run()
				{
					trxnHistoryAdapter.notifyDataSetChanged();
					System.gc();
				}
			});
		} catch (Exception ex)
		{
		}
	}
}
