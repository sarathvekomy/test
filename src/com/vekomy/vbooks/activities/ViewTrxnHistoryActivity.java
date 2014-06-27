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

/**
 * @author satish
 *
 */
public class ViewTrxnHistoryActivity extends ProgressActivity implements OnItemClickListener,OnItemSelectedListener
{
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
			lvTrxnHistory     =      (ListView) findViewById(R.id.listViewTrxnHistory);
			lvTrxnHistory.setClickable(true);
			lvTrxnHistory.setOnItemClickListener(this);						
			userID 		=	Utils.getData(getApplicationContext(), Constants.USER_ID, Constants.NA);
			dayId		=	Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID,Constants.NA);
			cmbTrxnType	=   (Spinner) findViewById(R.id.spinnerTrxnType);
			cmbTrxnDate =   (Spinner) findViewById(R.id.spinnerTrxnDate);
			cmbTrxnName =   (Spinner) findViewById(R.id.spinnerTrxnBN);
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,new String[]{Constants.VIEW_TRANSACTION,Constants.DELIVERY_NOTE,Constants.SALES_RETURN,Constants.JOURNAL,Constants.DAY_BOOK});
			cmbTrxnType.setAdapter(adapter);		
			cmbTrxnType.setOnItemSelectedListener(this);
			cmbTrxnDate.setOnItemSelectedListener(this);
			cmbTrxnName.setOnItemSelectedListener(this);
			new LoadTrxns().execute();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	private class LoadTrxns extends AsyncTask<Void, Void,List<TrxnHistory>>
	{
		@Override
		protected void onPreExecute()
		{
			showProgressDialog("Loding .... Transaction History.");
		}
		@Override
		protected List<TrxnHistory> doInBackground(Void... params) 
		{			
			return new SalesDao().getAllReport(VbookApp.getDbInstance(), userID);
		}
		@Override
		protected void onPostExecute(List<TrxnHistory> result) 
		{
			try
			{
				super.onPostExecute(result);			
				dismissProgressDialog();
				Set<String>  dateSet 	=	new TreeSet<String>();
				Set<String>  nameSet	=	new TreeSet<String>();
				List<String> dateList	=	new ArrayList<String>();
				List<String> nameList	=	new ArrayList<String>();
				dateList.add(Constants.VIEW_DATE);
				nameList.add(Constants.VIEW_NAME);
				if(result!=null)
				{
					for(int i = 0; i< result.size();i++)
					{
						dateSet.add(result.get(i).getDateID());
						//nameSet.add(result.get(i).getName());
					}
					int granded_days = Integer.parseInt(Utils.getData(ViewTrxnHistoryActivity.this, Constants.GRANDED_DAYS,"2"));
					String [] dataArray = dateSet.toArray(new String[dateSet.size()]);  
					//String [] nameArray = nameSet.toArray(new String[nameSet.size()]);
					for(int j = (dateSet.size()-granded_days < 0)?0:dateSet.size()-granded_days; j < dateSet.size();j++)
					{
						dateList.add(dataArray[j]);
						//nameList.add(nameArray[j]);
					}
					List<TrxnHistory> trxns = new ArrayList<TrxnHistory>();
					for(int i = 0; i< result.size();i++)
					{
						if(dateList.contains(result.get(i).getDateID()))
						{
							trxns.add(result.get(i));
							if(!result.get(i).getName().isEmpty())
								nameSet.add(result.get(i).getName());
						}
					}
					nameList.addAll(nameSet);
					trxnHistoryAdapter = new TrxnHistoryAdapter(context,R.layout.trxn_history_row,trxns);
					lvTrxnHistory.setAdapter(trxnHistoryAdapter);

				}
				cmbTrxnDate.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,dateList));
				cmbTrxnName.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,nameList));
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
				Utils.displayDialog(ViewTrxnHistoryActivity.this, null, "This Transaction is Not Approved from Management.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
				return;
			}
			Intent intent = null;		
			if(sales_ref_id.startsWith(Constants.DN))
			{
				intent  = new Intent(getApplicationContext(),DeliveryNoteActivity.class);
				intent.putExtra(Constants.MODE, Constants.VIEW + Constants.DN);
			}
			else if(sales_ref_id.startsWith(Constants.SR))
			{
				intent = new Intent(getApplicationContext(),DeliveryNoteActivity.class);			
				intent.putExtra(Constants.MODE, Constants.VIEW + Constants.SR);
			}
			else if(sales_ref_id.startsWith(Constants.JN))
			{
				intent = new Intent(getApplicationContext(),JournalActivity.class);			
				intent.putExtra(Constants.MODE, Constants.VIEW + Constants.JN);
			}		
			else
			{
				intent  = new Intent(getApplicationContext(),DayBookActivity.class);
				intent.putExtra("MODE", Constants.VIEW + Constants.DB);
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
}