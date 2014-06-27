/**
 * com.vekomy.vbooks.activities.JournalActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 02-Sep-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class JournalActivity  extends ProgressActivity implements OnClickListener,OnItemSelectedListener {
	
	TextView    businessName;
	TextView    invoiceName;
	TextView    currentDate;
	TextView	lblJNInVoiceNo;
	Button                 	btnJrSave;
	Button                 	btnJrCancel;
	List<String> 			journelList;
	//List<String> 			invoiceNoList;
	String 					business_name;
	
	EditText				txtJNAmount;
	EditText				txtDescription;
	Spinner					cmbJournelType;
	SalesDao	saleDao;
	Journal		journal;
	String[]	journalTypes;
	String 		salesRefId;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.add_journels);
			userID				=	Utils.getData(getApplicationContext(), Constants.USER_ID, Constants.NA);
			businessName        =	(TextView) findViewById(R.id.lblJNBusinessName);
			currentDate         =	(TextView) findViewById(R.id.lblJNDate);
			invoiceName         =	(TextView) findViewById(R.id.lblJNInVoiceName);
			lblJNInVoiceNo		=	(TextView) findViewById(R.id.lblJNInVoiceNo);
			btnJrSave           =   (Button)   findViewById(R.id.btnJournelSave);
			btnJrCancel         =   (Button)   findViewById(R.id.btnJournelCancel);
			txtJNAmount			=	(EditText) findViewById(R.id.txtJNAmount);
			txtDescription		=	(EditText) findViewById(R.id.txtDescription);		
			cmbJournelType 		=	(Spinner)findViewById(R.id.cmbJournalType);
			saleDao    			=   new SalesDao();
			btnJrSave.setOnClickListener(this);
			btnJrCancel.setOnClickListener(this);
			
			if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.VIEW))
			{
				salesRefId = getIntent().getExtras().getString(Constants.SALES_REF_ID);
				new LoadJournal(true).execute(salesRefId);
				((Button)findViewById(R.id.btnJournelSave)).setText(getResources().getString(R.string.ok));
			}
			else if(getIntent().getExtras().getString(Constants.MODE).startsWith(Constants.MODIFY))
			{
				salesRefId = getIntent().getExtras().getString(Constants.SALES_REF_ID);
				new LoadJournal(false).execute(salesRefId);
				((Button)findViewById(R.id.btnJournelSave)).setText(getResources().getString(R.string.update));
			}
			else
			{
				business_name		=	getIntent().getExtras().getString(Constants.P_BNAME).toString();
				userID 	=	Utils.getData(this, Constants.USER_ID, Constants.NA);
				orgID	=	Utils.getData(this, Constants.ORG_ID, "0");
				
				journal =	new Journal();
				journal.setBusinessName(business_name);			
				journal.setInvoiceName(getIntent().getExtras().getString(Constants.P_INAME).toString());
				journal.setCreatedOn(dateformat.format(new Date()));
				journal.setOrganizationId(Integer.parseInt(orgID));
				journal.setCreatedBy(userID);
				journal.setAmount(0);
				journal.setDescription("");
				journal.setJournalType("");
				showPreviewScreeen(false);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}

	@Override
	public void onClick(View v) 
	{
		try
		{		
			switch (v.getId()) 
			{
				case R.id.btnJournelSave:
					if( ((Button)findViewById(R.id.btnJournelSave)).getText().equals(getResources().getString(R.string.ok)))
						finish();
					else
					{
						if(!preparePreviewScreeen())
							return;
						if( ((Button)findViewById(R.id.btnJournelSave)).getText().equals(getResources().getString(R.string.update)))
							new JournalSaveTask(true).execute();
						else
							new JournalSaveTask(false).execute();
					}
					break;
				case R.id.btnJournelCancel:
					finish();
					break;
				default:
					break;
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) 
	{
		try
		{
			if(position==0)
				return;
			String tempInvoiceformat = String.valueOf(userID.charAt(0)).toUpperCase() + String.valueOf(business_name.charAt(0)).toUpperCase() +  Utils.getUniqueIDfromDate();
			//lblJNInVoiceNo.setText(invoiceNoList.get(position) +"/"+ tempInvoiceformat);
			lblJNInVoiceNo.setText(tempInvoiceformat);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	public boolean preparePreviewScreeen()
	{
		try
		{
			if(txtJNAmount.getText().toString().isEmpty() || Utils.getNumberFromMillion(txtJNAmount.getText().toString()) == 0)
			{
				txtJNAmount.setError("Plz Enter Amount.");
				return false;
			}			
			if(txtDescription.getText().toString().trim().length()==0)
			{
				txtDescription.setError("Plz Enter Description.");
				return false;
			}
			if(cmbJournelType.getSelectedItem().toString().equals("-- JOURNAL TYPE --"))
			{
				((TextView)findViewById(R.id.lblJournalTypeErr)).setError("Select JOURNAL TYPE");
				return false;
			}
			txtJNAmount.setError(null);
			txtDescription.setError(null);
			((TextView)findViewById(R.id.lblJournalTypeErr)).setError(null);
			journal.setJournalType(cmbJournelType.getSelectedItem().toString().trim());
			journal.setInvoiceNo(lblJNInVoiceNo.getText().toString());
			journal.setAmount(Utils.getNumberFromMillion(txtJNAmount.getText().toString().trim()));
			journal.setDescription(txtDescription.getText().toString().trim());
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	
	public void showPreviewScreeen(boolean isViewMode)
	{
		try
		{
			if(isViewMode)
			{
				txtJNAmount.setEnabled(false);
				txtDescription.setEnabled(false);
			}
			else
			{
				txtJNAmount.setEnabled(true);
				txtDescription.setEnabled(true);			
			}
			currentDate.setText(journal.getCreatedOn());
			lblJNInVoiceNo.setText(journal.getInvoiceNo());
			invoiceName.setText(journal.getInvoiceName());
			businessName.setText(journal.getBusinessName());
			txtJNAmount.setText(Utils.currencyInMillion(journal.getAmount()));
			txtDescription.setText(journal.getDescription());
			ArrayAdapter<String> adapter;
			
			if(journal.getJournalType().isEmpty())
			{
				String  journelTypes = Utils.getData(getApplicationContext(), Constants.JOURNALS_TYPE, Constants.NA);
				String [] journelTypesArray  = journelTypes.split(",");
				journelList 	=	new ArrayList<String>();
				//invoiceNoList	=	new ArrayList<String>();
				journelList.add(0,"-- JOURNAL TYPE --");
				//invoiceNoList.add(0,"");
				//String[] journelInfo;  
				for (int i = 0; i < journelTypesArray.length; i++) {
					//journelInfo = journelTypesArray[i].split("\\|");
					//if(journelInfo.length==2)
					//{
						journelList.add(journelTypesArray[i]);
						//journelList.add(journelInfo[0]);
						//invoiceNoList.add(journelInfo[1]);
					//}
				}
				adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,journelList);
				cmbJournelType.setOnItemSelectedListener(this);
			}
			else
			{
				adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,new String[]{journal.getJournalType()});	
			}
			
			cmbJournelType.setAdapter(adapter);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	class LoadJournal extends AsyncTask<String, Void, Journal>
	{
		boolean isViewMode;
		public LoadJournal(boolean isViewMode)
		{
			this.isViewMode = isViewMode;
		}
		protected void onPreExecute(){showProgressDialog("Loading Journal Information..... Please wait...");}
		@Override
		protected Journal doInBackground(String... params)
		{  
			try
			{
				journal = saleDao.readJournal(VbookApp.getDbInstance(), params[0], Utils.getData(VbookApp.getInstance(), Constants.USER_ID, "0"));
				return journal;
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(Journal result)
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
	}// LoadJournal
	
	class JournalSaveTask extends AsyncTask<Void, Void, Long>
	{
		boolean isChangeReq;
		public JournalSaveTask(boolean isChangeReq)
		{
			this.isChangeReq = isChangeReq;
		}
		protected void onPreExecute()
		{
			showProgressDialog("Saveing Journal...");
		}
		
		@Override
		protected Long doInBackground(Void... params)
		{
			try
			{
				if(isChangeReq)
				{
					//if( saleDao.changeToCRTrxn(VbookApp.getDbInstance(),salesRefId,userID) == 0){
					//	// Need to error here	
					//}
				}
				
				return saleDao.saveJournal(VbookApp.getDbInstance(), journal,isChangeReq);
			} 
			catch (Exception scx)
			{
				scx.printStackTrace();
				Log.e(TAG, scx.getLocalizedMessage(), scx);
				
			}
			return null;
		}

		protected void onPostExecute(Long result)
		{
			try
			{
				dismissProgressDialog();
				android.content.DialogInterface.OnClickListener okclick = new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						finish();
					}
				};
				if(result!=null && result!= -1)
				{
					Utils.displayDialog(JournalActivity.this,null,"Journal Saved Successfully.",Utils.DIALOG_OK,"Ok",okclick,null,null,null,null);
				}
				else
				{
					Utils.displayDialog(JournalActivity.this,null,"Some Error happened Journal is Not saved.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
	}
}