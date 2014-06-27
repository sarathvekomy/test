/**
 * com.vekomy.vbooks.activities.DayBookActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 05-Sep-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.DayBookAllowancesAdapter;
import com.vekomy.vbooks.adapters.DayBookProductsAdapter;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookAllowances;
import com.vekomy.vbooks.database.ImagesDao;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.listener.VLocationListener;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class DayBookActivity  extends ProgressActivity implements OnClickListener,OnItemSelectedListener 
{
	View        llAllowancesPageView;
	View        llDaybookAmountProductView;
	View        llDaybookPreView;

	ListView 	lvAllowancesList;
	DayBookAllowancesAdapter allowanceAdapter;
	
	TextView 	lblDayBookSalesExecutive;
	TextView 	lblDayBookOpeningBal;
	TextView 	lblDayBookDate;
	TextView 	lblTotalAllowanceAmt;
	
	EditText	txtDayBookVehincalNo;
	EditText	txtstartReading;		
	EditText	txtDriverName;
	
	EditText    txDBReportingManager;
	CheckBox    cbDBReturnToFactory;
	
	EditText	txtAmount;
	EditText	txtEndReading;
	
	EditText    txtAllowancesRemarks;
	
	Button		btnAddAllowance;
	Button		btnBack;
	Button		btnPreview;
	Button		btnDBAllowancesClose;	
	Button		btnDBPaymentClose;
	Spinner 	cmbExpensesType;
	Spinner     cmbOffloadingBusinessNames;
	
	ListView 	lvDaybookProducts;
	ListView 	lvDayBookfinalPreview;
	
	DayBookProductsAdapter productsAdapter;
	DayBookProductsAdapter productsPreviewAdapter;
	
	// 2 Payments and Products page
	
	TextView	lblCustomerTotalPayable;
	TextView	lblCustomerTotalRecived;
	TextView	lblCustomerTotCredit;
	TextView	lblCustomerTotCreditAdv;
	TextView	lblAmountToBank;
	EditText	txtAmtToFactory;
	
	// Daybook Final View Page

	EditText 	txDBVehicalNoPreview;
	TextView 	lblDBTotalPayablePreview;
	TextView 	lblDBTotalreceviedPreview;
	EditText 	txDBstartReadingPreview;
	EditText 	txDBendReadingPreview;
	TextView 	lblDBamtToabankPreview;
	EditText 	txDBAmtToFactoryPreview;
	EditText 	txDBDriverNamePreview;
	TextView 	lblDBbalPreview;
	TextView 	lblDBCloseingBalPreview;
	
	TextView 	lblSalesExecName;

	TextView 	lblOpeningBalanceView;
	TextView 	lblDaybookDatePreview;
	TextView 	lblDBTotalAllowancebalPreview;
	Button		btnPreviewBack;
	Button		btnSave;
	
	// *********************************************************** //
	DayBook 	dayBook;
	Context 	context;	
	SalesDao 	salesDao;
	ImagesDao	imagesDao;
	String 		dayId;
	String 		salesRefId;
	Dialog 		allowanceShowDialog;
	Dialog 		allowanceAddDialog;
	String      allotment_type;
	String      selected_expenses;
	Map<String, String> bNamewithCnt;
	Float		totalrecivedAmt;
	long 		previous_reading;
	static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	Bitmap 		mImageBitmap;
	LocationManager locationManager;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES 	= 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES 			= 1000; // in Milliseconds
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.daybook_view);
			this.context = this;
			
			// *******************   location capture  *******************    
			try
			{
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(	LocationManager.GPS_PROVIDER,
													MINIMUM_TIME_BETWEEN_UPDATES,	
													MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
													new VLocationListener());
			}
			catch(SecurityException e){}
			catch(Exception e){}
			// *******************   location capture  *******************
			
			llAllowancesPageView		=	findViewById(R.id.llAllowancesPage);
			llDaybookAmountProductView  =	findViewById(R.id.llDaybookAmountProductView);
			llDaybookPreView			=	findViewById(R.id.llDaybookPreView);
	
			// ---------------  AllowancesPageView ----------------
			lblDayBookSalesExecutive    =	(TextView)findViewById(R.id.lblDayBookSalesExecutive);
			lblDayBookOpeningBal        =   (TextView)findViewById(R.id.lblDayBookOpeningBal);
			lblDayBookDate              =	(TextView)findViewById(R.id.lblDayBookDate);
			lblTotalAllowanceAmt		=	(TextView)findViewById(R.id.lblTotalAllowanceAmt);
			
			txtDayBookVehincalNo		=   (EditText)findViewById(R.id.txtDayBookVehincalNo);
			txtstartReading         	=   (EditText)findViewById(R.id.txtstartReading);
			txtDriverName        		=   (EditText)findViewById(R.id.txtDriverName);
			txtEndReading				=   (EditText)findViewById(R.id.txtEndReading);
			btnDBAllowancesClose		=	(Button)findViewById(R.id.btnDBAllowancesClose);
			btnDBAllowancesClose.setOnClickListener(this);
			cbDBReturnToFactory         =   (CheckBox)findViewById(R.id.cbDBReturnToFactory);
			cbDBReturnToFactory.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						txDBReportingManager.setEnabled(true);
						txtAmtToFactory.setEnabled(true);
					}
					else
					{
						txDBReportingManager.setText("");
						txtAmtToFactory.setText("");
						txDBReportingManager.setEnabled(false);
						txtAmtToFactory.setEnabled(false);
					}
				}
			});
			
			txDBReportingManager        =   (EditText)findViewById(R.id.txDBReportingManager);			
			lvAllowancesList			=	(ListView) findViewById(R.id.lvAllowancesList);
			
			// ---------------  Products PageView ----------------
			lvDaybookProducts			=	(ListView) findViewById(R.id.lvDaybookProducts);
			lvDayBookfinalPreview		=	(ListView) findViewById(R.id.lvDayBookfinalPreview);
			
			lblCustomerTotalPayable		=	(TextView)findViewById(R.id.lblCustomerTotalPayable);
			lblCustomerTotalRecived		=	(TextView)findViewById(R.id.lblCustomerTotalRecived);
			lblCustomerTotCredit		=	(TextView)findViewById(R.id.lblCustomerTotCredit);
			lblCustomerTotCreditAdv		=	(TextView)findViewById(R.id.lblCustomerTotCreditAdv);
			lblAmountToBank				=	(TextView)findViewById(R.id.lblAmountToBank);
			txtAmtToFactory				=	(EditText)findViewById(R.id.txtAmtToFactory);
			btnDBPaymentClose			=	(Button)findViewById(R.id.btnDBPaymentClose);
			btnDBPaymentClose.setOnClickListener(this);
			
			// ---------------  Final Preview PageView ----------------
			txDBVehicalNoPreview 		= 	(EditText) findViewById(R.id.txDBVehicalNoPreview);
			lblDBTotalPayablePreview 	= 	(TextView) findViewById(R.id.lblDBTotalPayablePreview);
			lblDBTotalreceviedPreview 	= 	(TextView) findViewById(R.id.lblDBTotalreceviedPreview);
			txDBendReadingPreview 		= 	(EditText) findViewById(R.id.txDBendReadingPreview);
			lblDBamtToabankPreview 		= 	(TextView) findViewById(R.id.lblDBamtToabankPreview);
			txDBAmtToFactoryPreview 	= 	(EditText) findViewById(R.id.txDBAmtToFactoryPreview);
			txDBDriverNamePreview 		= 	(EditText) findViewById(R.id.txDBDriverNamePreview);
			lblDBTotalAllowancebalPreview = (TextView) findViewById(R.id.lblDBTotalAllowancePreview);
			lblDBCloseingBalPreview 	= 	(TextView) findViewById(R.id.lblDBCloseingBalPreview);
			txDBstartReadingPreview 	= 	(EditText) findViewById(R.id.txDBstartReadingPreview);
	
			lblSalesExecName 			= 	(TextView) findViewById(R.id.lblSalesExecName);
			lblOpeningBalanceView 		= 	(TextView) findViewById(R.id.lblOpeningBalanceView);
			lblDaybookDatePreview 		= 	(TextView) findViewById(R.id.lblDaybookDatePreview);
			btnPreviewBack				= 	(Button)	 findViewById(R.id.btnPreviewBack);
			btnSave						= 	(Button)	 findViewById(R.id.btnSave);
			btnPreviewBack.setOnClickListener(this);
			btnSave.setOnClickListener(this);
			
			userID						=	Utils.getData(getApplicationContext(), 	Constants.USER_ID, Constants.NA);
			dayId 						=	Utils.getData(VbookApp.getInstance(), 	userID + Constants.CYCLE_ID, Constants.NA);	
			orgID						=	Utils.getData(VbookApp.getInstance(), 	Constants.ORG_ID, "0");
			allotment_type				=	Utils.getData(VbookApp.getInstance(),	Constants.ALLOTMENT_TYPE,Constants.NA);

			salesDao 					= 	new SalesDao();
			imagesDao					=	new ImagesDao();
			btnBack						=	(Button)findViewById(R.id.btnBack);
			btnBack.setOnClickListener(this);
			btnPreview					=	(Button)findViewById(R.id.btnPreview);
			btnPreview.setOnClickListener(this);
			((Button)findViewById(R.id.btnNext)).setOnClickListener(this);
			((Button)findViewById(R.id.btnVehicleInfoSave)).setOnClickListener(this);
			bNamewithCnt = new HashMap<String, String>();
			bNamewithCnt.put(Constants.NA, "0");
			
			if(getIntent().getExtras().getString(Constants.MODE).equalsIgnoreCase(Constants.VIEW + Constants.DB))
			{
				salesRefId	=	getIntent().getExtras().getString(Constants.SALES_REF_ID);
				btnPreviewBack.setText(getResources().getString(R.string.ok));
				btnSave.setText(getResources().getString(R.string.show_allowance));			
				new LoadDayBook(true).execute(salesRefId);
			}
			else if(getIntent().getExtras().getString(Constants.MODE).equalsIgnoreCase(Constants.MODIFY + Constants.DB))
			{
				salesRefId	=	getIntent().getExtras().getString(Constants.SALES_REF_ID);
				btnPreviewBack.setText(getResources().getString(R.string.update));
				btnSave.setText(getResources().getString(R.string.show_allowance));
				txDBVehicalNoPreview.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {}				
					@Override	
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
					@Override
					public void afterTextChanged(Editable entertext){
						dayBook.setVehicleNo(entertext.toString());
					}
				});
				txDBDriverNamePreview.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {}				
					@Override	
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
					@Override
					public void afterTextChanged(Editable entertext) {
						dayBook.setDriverName(entertext.toString());
					}
				});
				
				/*txDBstartReadingPreview.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {}				
					@Override	
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
					@Override
					public void afterTextChanged(Editable entertext) {
						if(Integer.parseInt(txDBendReadingPreview.getText().toString().trim().isEmpty()?"0":txDBendReadingPreview.getText().toString()) < Integer.parseInt(entertext.toString().trim().isEmpty()?"0":entertext.toString())){
							txDBstartReadingPreview.setError("Start Reading is Always Less Than End Reading");
							if(txDBstartReadingPreview.getText().toString().length() > 0)
								txDBstartReadingPreview.setText(txDBstartReadingPreview.getText().toString().substring(0, entertext.length()-1));
						}
						dayBook.setStartReading(txDBstartReadingPreview.getText().toString());
					}
				});*/
				txDBendReadingPreview.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {}				
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
					@Override
					public void afterTextChanged(Editable entertext) {
						if(Integer.parseInt(txDBstartReadingPreview.getText().toString().trim().isEmpty()?"0":txDBstartReadingPreview.getText().toString()) > Integer.parseInt(entertext.toString().trim().isEmpty()?"0":entertext.toString())){
							txDBendReadingPreview.setError("End Reading is Always GraterThan Start Reading");
							if(txDBendReadingPreview.getText().toString().length() > 0)
								txDBendReadingPreview.setText(txDBendReadingPreview.getText().toString().substring(0, entertext.length()-1));
						}
						dayBook.setEndReading(txDBendReadingPreview.getText().toString());
					}
				});
				
				txDBAmtToFactoryPreview.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {}				
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
					@Override
					public void afterTextChanged(Editable entertext) {
						Float fEnterAmt			= entertext.toString().trim().isEmpty()?0:Utils.getNumberFromMillion(entertext.toString().trim());
						Float maxamtToFactory 	= dayBook.getOpeningBal() + dayBook.getTotalReceivedFromCust()-dayBook.getTotalAllowances();
						if(maxamtToFactory < fEnterAmt)
						{
							txDBAmtToFactoryPreview.setText(txDBAmtToFactoryPreview.getText().toString().substring(0, entertext.length()-1));
							txDBAmtToFactoryPreview.setError("Amount to factory should Not Exceed this much amount " + Utils.currencyInMillion(maxamtToFactory)); 
							txDBAmtToFactoryPreview.requestFocus();
							return;
						}
						dayBook.setAmtToFactory(entertext.toString().trim().isEmpty()?0:Utils.getNumberFromMillion(entertext.toString()));
						String closeing_bal = Utils.currencyInMillion((dayBook.getOpeningBal()+ dayBook.getTotalReceivedFromCust())-dayBook.getTotalAllowances()-dayBook.getAmtToFactory());
						lblDBCloseingBalPreview.setText(closeing_bal);
						dayBook.setCloseingBal(Utils.getNumberFromMillion(closeing_bal));
					}
				});
				new LoadDayBook(false).execute(salesRefId);
			}
			else // new
			{
				btnAddAllowance				= (Button)findViewById(R.id.btnAddAllowance);
				btnAddAllowance.setOnClickListener(this);
				
				if(allotment_type.equalsIgnoreCase("daily"))
				{
					txDBReportingManager.setEnabled(true);
					cbDBReturnToFactory.setChecked(true);
					cbDBReturnToFactory.setClickable(false);
				}
				else
				{
					txDBReportingManager.setEnabled(false);
					txtAmtToFactory.setEnabled(false);
				}
				new LoadDayBookAllowances().execute();
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
			case R.id.btnNext:
				if(!prepareProductScreeen())
					return;
				new LoadDayBookProducts().execute();
				break;
			case R.id.btnBack:
				showAllowanceScreeen();
				break;
			case R.id.btnPreviewBack:
				if(btnPreviewBack.getText().equals(getResources().getString(R.string.back)))
					showProductScreeen();
				else if(btnPreviewBack.getText().equals(getResources().getString(R.string.update)))
				{
					final Dialog descriptionDialog = new Dialog(DayBookActivity.this,R.style.customDialogStyle);
					descriptionDialog.setCancelable(false);
					descriptionDialog.setContentView(R.layout.remarks_display_dialog);								
					descriptionDialog.findViewById(R.id.txtDescription).setEnabled(false);
					((EditText)descriptionDialog.findViewById(R.id.txtDescription)).setText(dayBook.getRemarks());
					Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
					Cancel.setOnClickListener(new OnClickListener(){@Override public void onClick(View v) {descriptionDialog.dismiss();}});							
					Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);
					btnSave.setOnClickListener(new OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							String resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtnewDescription)).getText().toString();
							dayBook.setRemarks(resultMsg);
							// save Day with  CR Request
							new DayBookSave(true).execute();
							descriptionDialog.dismiss();
						}
					});
					descriptionDialog.show();
				}
				else
					finish();
				break;
			case R.id.btnPreview:
				if(!preparePreviewScreeen())
					return;
				showPreviewScreeen(true);
				break;
			case R.id.btnSave:
				if(btnSave.getText().equals(getResources().getString(R.string.save)))
				{
					final Dialog descriptionDialog = new Dialog(DayBookActivity.this,R.style.customDialogStyle);
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
							dayBook.setRemarks(resultMsg);
							// Save Day Book here
							new DayBookSave(false).execute();
							descriptionDialog.dismiss();
						}
					});
					descriptionDialog.show();					
				}
				else
					// Show day Book with CR Request show custom Dialog here
					showAllowanceDialog();
				break;
			case R.id.btnAllowncesOk:
				dayBook.setAmtTobank(0);
				dayBook.setTotalAllowances(0);
				for (int i = 0; i < dayBook.getAllowancesList().size(); i++) 
				{
					if(dayBook.getAllowancesList().get(i).getAllowancesType().equalsIgnoreCase("Amount To Bank"))
					{
						dayBook.setAmtTobank(dayBook.getAmtTobank() + dayBook.getAllowancesList().get(i).getAmt());
						dayBook.setTotalAllowances(dayBook.getTotalAllowances() + dayBook.getAllowancesList().get(i).getAmt());
					}
					else
						dayBook.setTotalAllowances(dayBook.getTotalAllowances() + dayBook.getAllowancesList().get(i).getAmt());
				}
				String closeing_bal = String.valueOf((dayBook.getOpeningBal()+ dayBook.getTotalReceivedFromCust()) - dayBook.getTotalAllowances() - dayBook.getAmtToFactory());
				dayBook.setCloseingBal(Float.parseFloat(closeing_bal));
				lblDBCloseingBalPreview.setText(Utils.currencyInMillion(dayBook.getCloseingBal()));
				lblDBamtToabankPreview.setText(Utils.currencyInMillion(dayBook.getAmtTobank()));
				lblDBTotalAllowancebalPreview.setText(Utils.currencyInMillion(dayBook.getTotalAllowances()- dayBook.getAmtTobank()));
				allowanceShowDialog.dismiss();
				allowanceShowDialog = null;
				break;
			case R.id.btnAddAllowance:
				showAddallowance();
				 break;
			case R.id.btnVehicleInfoSave:
				dayBook.setVehicleNo(txtDayBookVehincalNo.getText().toString());
				dayBook.setDriverName(txtDriverName.getText().toString());
				dayBook.setStartReading(txtstartReading.getText().toString());
				if(salesDao.insertDayBookEntry(VbookApp.getDbInstance(), dayBook,dayId)!=-1)
					Utils.displayDialog(context,null,"Vechicle Details Saved Successfully.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				else
					Utils.displayDialog(context,null,"Vechicle Details Failed.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				break;
			case R.id.btnDBAllowancesClose:
			case R.id.btnDBPaymentClose:
				finish();
				break;
			}// switch
		}// try
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void showAddallowance()
	{
		try
		{			
			if(allowanceAdapter.getCount() == 0)
			{
				if(txtDayBookVehincalNo.getText().toString().equals(""))
				{
					txtDayBookVehincalNo.setError("Enter VehicalNo");
					txtDayBookVehincalNo.requestFocus();
					return;
				}
				if(txtstartReading.getText().toString().equals(""))
				{
					txtstartReading.setError("Enter StartReading");
					txtstartReading.requestFocus();
					return;
				}
				if(txtDriverName.getText().toString().equals(""))
				{
					txtDriverName.setError("Enter Driver Name");
					txtDriverName.requestFocus();
					return;
				}
				txtDayBookVehincalNo.setError(null);
				txtstartReading.setError(null);
				txtDriverName.setError(null);
				if(dayBook.getVehicleNo() == null ||dayBook.getVehicleNo().isEmpty() || dayBook.getDriverName() == null || dayBook.getDriverName().isEmpty() || dayBook.getStartReading() == null || dayBook.getStartReading().isEmpty())
				{
					dayBook.setVehicleNo(txtDayBookVehincalNo.getText().toString());
					dayBook.setDriverName(txtDriverName.getText().toString());
					dayBook.setStartReading(txtstartReading.getText().toString());
					salesDao.insertDayBookEntry(VbookApp.getDbInstance(), dayBook,dayId); 
				}
			}
			getDisplayDialog();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}// showAddallowance

	public void showAllowanceDialog()
	{
		try
		{
			allowanceShowDialog = new Dialog(DayBookActivity.this,R.style.customDialogStyle);
			allowanceShowDialog.setTitle(getResources().getString(R.string.allowances_details));			
			allowanceShowDialog.setCancelable(false);
			allowanceShowDialog.setContentView(R.layout.allowances_list_view_dialog);
			ListView  lvAllowance_display =(ListView) allowanceShowDialog.findViewById(R.id.listAllownacesShow);				
			lvAllowance_display.setAdapter(allowanceAdapter);
			((Button) allowanceShowDialog.findViewById(R.id.btnAllowncesOk)).setOnClickListener(DayBookActivity.this);
			allowanceShowDialog.show();
			Window window = allowanceShowDialog.getWindow();
			window.setLayout(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	public void showAllowanceScreeen()
	{
		try
		{
			llDaybookAmountProductView.setVisibility(View.GONE);
			llDaybookPreView.setVisibility(View.GONE);
			llAllowancesPageView.setVisibility(View.VISIBLE);
			
			lblDayBookSalesExecutive.setText(userID);
			lblDayBookOpeningBal.setText(Utils.currencyInMillion(dayBook.getOpeningBal()));
			try
			{
				String CycleId =  dayId.substring(0,8);
				Date date = Utils.ReportDate2serverDate(CycleId);
				if (date != null) 
				{
				    lblDayBookDate.setText(dateformat.format(date)); 
				}
			}
			catch (Exception e)
			{
				String year=dayId.substring(5,9);
				String month=dayId.substring(9,11);
				String day=dayId.substring(11,13);
				lblDayBookDate.setText(day +":"+month+":"+year);
			}
			dayBook.setTotalAllowances(0);
			
			if(dayBook.getVehicleNo().isEmpty())
			{
				txtDayBookVehincalNo.setText(dayBook.getVehicleNo());
				txtstartReading.setText(dayBook.getStartReading());
				txtDriverName.setText(dayBook.getDriverName());
			}
			else
			{
				txtDayBookVehincalNo.setText(dayBook.getVehicleNo());
				txtstartReading.setText(dayBook.getStartReading());
				txtDriverName.setText(dayBook.getDriverName());
				txtDayBookVehincalNo.setEnabled(false);
				txtstartReading.setEnabled(false);
				txtDriverName.setEnabled(false);
				findViewById(R.id.btnVehicleInfoSave).setEnabled(false);
			}
			allotment_type=Utils.getData(this,Constants.ALLOTMENT_TYPE,"");
			
			if(allotment_type.equalsIgnoreCase("daily"))
			{
				cbDBReturnToFactory.setVisibility(View.FOCUS_BACKWARD);
				txDBReportingManager.setText(dayBook.getReportingManager().isEmpty()?"":dayBook.getReportingManager());
			}
			else
			{
				cbDBReturnToFactory.setClickable(true);
				txDBReportingManager.setText(dayBook.getReportingManager().isEmpty()?"":dayBook.getReportingManager());
			}
			
			for (int i = 0; i < dayBook.getAllowancesList().size(); i++)
			{
				if(dayBook.getAllowancesList().get(i).getAllowancesType().equalsIgnoreCase("Amount To Bank"))
				{
					dayBook.setAmtTobank(dayBook.getAmtTobank() + dayBook.getAllowancesList().get(i).getAmt());
					dayBook.setTotalAllowances(dayBook.getTotalAllowances() + dayBook.getAllowancesList().get(i).getAmt());
				}
				else
					dayBook.setTotalAllowances(dayBook.getTotalAllowances() + dayBook.getAllowancesList().get(i).getAmt());
			}
			lblTotalAllowanceAmt.setText(Utils.currencyInMillion(dayBook.getTotalAllowances()));
			dayBook.setOrgId(orgID);
			
			if(dayBook.getAllowancesList() == null)
				allowanceAdapter = new DayBookAllowancesAdapter(DayBookActivity.this,R.layout.daybook_allowances_row,new ArrayList<DayBookAllowances>(),false);
			else
				allowanceAdapter = new DayBookAllowancesAdapter(DayBookActivity.this,R.layout.daybook_allowances_row,dayBook.getAllowancesList(),false);
			lvAllowancesList.setAdapter(allowanceAdapter);
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
			llAllowancesPageView.setVisibility(View.GONE);
			llDaybookPreView.setVisibility(View.GONE);
			llDaybookAmountProductView.setVisibility(View.VISIBLE);
	
			dayBook.setTotalAllowances(Utils.getNumberFromMillion(lblTotalAllowanceAmt.getText().toString()));
			lblCustomerTotalPayable.setText(Utils.currencyInMillion(dayBook.getTotalpayableFromCust()));
			lblCustomerTotalRecived.setText(Utils.currencyInMillion(dayBook.getTotalReceivedFromCust()));
			float totalcredit = dayBook.getTotalpayableFromCust() - dayBook.getTotalReceivedFromCust();
			if(totalcredit < 0)
			{
				lblCustomerTotCreditAdv.setText(getResources().getString(R.string.adv));
				lblCustomerTotCredit.setText(Utils.currencyInMillion(-1*totalcredit));
			}
			else
			{
				lblCustomerTotCreditAdv.setText("");
				lblCustomerTotCredit.setText(Utils.currencyInMillion(totalcredit));
			}		
			lblAmountToBank.setText(Utils.currencyInMillion(dayBook.getAmtTobank()));
			//if(cbDBReturnToFactory.isChecked())
			//	txtAmtToFactory.setEnabled(true);
			//else
			//	txtAmtToFactory.setEnabled(f);
			
			if(dayBook.getProductsList()!=null)
			{
				productsAdapter = new DayBookProductsAdapter(context, R.layout.daybook_products_row,dayBook.getProductsList(),!cbDBReturnToFactory.isChecked());
				lvDaybookProducts.setAdapter(productsAdapter);
			}
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
			llAllowancesPageView.setVisibility(View.GONE);
			llDaybookAmountProductView.setVisibility(View.GONE);
			llDaybookPreView.setVisibility(View.VISIBLE);
			txDBstartReadingPreview.setEnabled(false);
			if(isViewMode)
			{
				txDBVehicalNoPreview.setEnabled(false);
				//txDBstartReadingPreview.setEnabled(false);
				txDBendReadingPreview.setEnabled(false);
				txDBDriverNamePreview.setEnabled(false);
				txDBAmtToFactoryPreview.setEnabled(false);
			}
			else
			{
				txDBVehicalNoPreview.setBackgroundResource(R.drawable.txt_box_boarder);
				//txDBstartReadingPreview.setBackgroundResource(R.drawable.txt_box_boarder);
				txDBendReadingPreview.setBackgroundResource(R.drawable.txt_box_boarder);
				txDBDriverNamePreview.setBackgroundResource(R.drawable.txt_box_boarder);
				txDBAmtToFactoryPreview.setBackgroundResource(R.drawable.txt_box_boarder);
			}
			txDBVehicalNoPreview.setText(dayBook.getVehicleNo());
			txDBDriverNamePreview.setText(dayBook.getDriverName());
			txDBendReadingPreview.setText(dayBook.getEndReading());
			txDBstartReadingPreview.setText(dayBook.getStartReading());
			lblDBTotalPayablePreview.setText(Utils.currencyInMillion(dayBook.getTotalpayableFromCust()));
			lblDBTotalreceviedPreview.setText(Utils.currencyInMillion(dayBook.getTotalReceivedFromCust()));
			lblDBamtToabankPreview.setText(Utils.currencyInMillion(dayBook.getAmtTobank()));
			lblSalesExecName.setText(dayBook.getSaleExeName());
			lblOpeningBalanceView.setText(Utils.currencyInMillion(dayBook.getOpeningBal()));
			lblDaybookDatePreview.setText(dayBook.getCreatedOn());
			lblDBTotalAllowancebalPreview.setText(Utils.currencyInMillion(dayBook.getTotalAllowances()- dayBook.getAmtTobank()));
			txDBAmtToFactoryPreview.setText(Utils.currencyInMillion(dayBook.getAmtToFactory()));
			float closeing_bal=(dayBook.getOpeningBal()+dayBook.getTotalReceivedFromCust())-dayBook.getTotalAllowances()-dayBook.getAmtToFactory();
			dayBook.setCloseingBal(closeing_bal);
			lblDBCloseingBalPreview.setText(Utils.currencyInMillion(closeing_bal));
			if(dayBook.getProductsList() != null && dayBook.getProductsList().size()!=0)
			{
				if(isViewMode)
					productsPreviewAdapter = new DayBookProductsAdapter(context, R.layout.daybook_products_row,dayBook.getProductsList(),isViewMode);
				else
					productsPreviewAdapter = new DayBookProductsAdapter(context, R.layout.daybook_products_row,dayBook.getProductsList(),!dayBook.getIsreturn());				
				lvDayBookfinalPreview.setAdapter(productsPreviewAdapter);
			}
			if(allowanceAdapter == null)
				allowanceAdapter = new DayBookAllowancesAdapter(DayBookActivity.this,R.layout.daybook_allowances_row,dayBook.getAllowancesList(),true);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	public boolean prepareProductScreeen()
	{
		try
		{		
			if(txtDayBookVehincalNo.getText().toString().equals(""))
			{
				txtDayBookVehincalNo.setError("Enter VehicalNo");
				txtDayBookVehincalNo.requestFocus();
				return false;
			}
			if(txtstartReading.getText().toString().equals(""))
			{
				txtstartReading.setError("Enter StartReading");
				txtstartReading.requestFocus();
				return false;
			}
			if(txtDriverName.getText().toString().equals(""))
			{
				txtDriverName.setError("Enter Driver Name");
				txtDriverName.requestFocus();
				return false;
			}
			if(cbDBReturnToFactory.isChecked() && txDBReportingManager.getText().toString().equals(""))
			{
				txDBReportingManager.setError("Enter Reporting Manager Name");
				txDBReportingManager.requestFocus();
				return false;
			}
			txtDayBookVehincalNo.setError(null);
			txtstartReading.setError(null);
			txtDriverName.setError(null);
			txDBReportingManager.setError(null);
			dayBook.setVehicleNo(txtDayBookVehincalNo.getText().toString());
			dayBook.setDriverName(txtDriverName.getText().toString());
			dayBook.setStartReading(txtstartReading.getText().toString());
			dayBook.setReportingManager(txDBReportingManager.getText().toString().trim());
			salesDao.saveReportMgrDayBook(VbookApp.getDbInstance(),dayBook);
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	public boolean preparePreviewScreeen()
	{
		try
		{		
			if(txtEndReading.getText().toString().equals(""))
			{
				txtEndReading.setError("Enter Ending Reading");
				txtEndReading.requestFocus();
				return false;
			}
			long endReading 	=	Long.parseLong(txtEndReading.getText().toString());
			long startReading	=	Long.parseLong(txtstartReading.getText().toString());
			previous_reading 	= 	salesDao.getleatestMeteReading(VbookApp.getDbInstance(),userID, dayId,"Vehicle Fuel Expenses");
			
			if(endReading <= startReading || endReading <= previous_reading)
			{
				txtEndReading.setError("Ending Reading must heigher Than Start Reading.");
				txtEndReading.requestFocus();
				return false;
			}			
			// Need Validation and Amount To factory
			Float result 		= dayBook.getOpeningBal() + dayBook.getTotalReceivedFromCust() - dayBook.getTotalAllowances();
			Float amtToFactory 	= Float.parseFloat(txtAmtToFactory.getText().toString().trim().equals("")?"0":txtAmtToFactory.getText().toString().trim());
			if(result < amtToFactory)
			{
				txtAmtToFactory.setError("Amount should Not Exceed Closing Balance " + Utils.currencyInMillion(result)); 
				txtAmtToFactory.requestFocus();
				return false;
			}	
			txtEndReading.setError(null);
			txtAmtToFactory.setError(null);
			dayBook.setEndReading(txtEndReading.getText().toString().trim());
			dayBook.setAmtToFactory(amtToFactory);
			dayBook.setCreatedOn(dateformat.format(new Date()));
			dayBook.setIsreturn(cbDBReturnToFactory.isChecked());
			return true;
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	public void OnbtnRemoveClick(View v) 
	{
		try
		{		
			final DayBookAllowances itemToRemove = (DayBookAllowances) v.getTag();		
			AlertDialog.Builder adb = new AlertDialog.Builder(DayBookActivity.this);
			adb.setTitle("ExpenseList");
			adb.setMessage("Do you want To Delete :"+"\n" + itemToRemove.getAllowancesType() + "  For Amount  :"+ Utils.currencyInMillion(itemToRemove.getAmt()));
			android.content.DialogInterface.OnClickListener ol1=new android.content.DialogInterface.OnClickListener() 
			{			
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{	
					salesDao.deleteDayBookAllowance(VbookApp.getDbInstance(), itemToRemove);
					allowanceAdapter.remove(itemToRemove);
					String prepareImgId = dayBook.getDayBookid() + "_" + itemToRemove.getRowid();
					imagesDao.removeBillImage(VbookApp.getDbInstance(), prepareImgId);
					lblTotalAllowanceAmt.setText( Utils.currencyInMillion(lblTotalAllowanceAmt.getText().toString().isEmpty()?0:Utils.getNumberFromMillion(lblTotalAllowanceAmt.getText().toString())- itemToRemove.getAmt()));
				}
			};
			adb.setPositiveButton("Ok", ol1);
			adb.setNegativeButton("Cancel", null);
			adb.setCancelable(false);
			adb.create().show();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	public void OnbtnBillViewClick(View v) 
	{
		try
		{		
			final DayBookAllowances itemToShow = (DayBookAllowances) v.getTag();
			if(itemToShow.getReserved() == null || itemToShow.getReserved().isEmpty())
			{
				Utils.displayDialog(context,null,"no Bill preview available.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				return;
			}
			String prepareImgId = dayBook.getDayBookid() + "_" + itemToShow.getRowid();
			Intent intent = new Intent(DayBookActivity.this,BillViewActivity.class);
			intent.putExtra(Constants.BILL_IMG_ID, prepareImgId);
			startActivity(intent);			
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	protected void getDisplayDialog() 
	{
		try
		{
	        // recyle unused bitmaps
			if (mImageBitmap != null)
			{
				mImageBitmap.recycle();
				mImageBitmap = null;
			}
			allowanceAddDialog = new Dialog(DayBookActivity.this,R.style.customDialogStyle);
			allowanceAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			allowanceAddDialog.setContentView(R.layout.allowances_dialog_show);
			allowanceAddDialog.setCancelable(false);
			
			txtAllowancesRemarks        =   (EditText) 	allowanceAddDialog.findViewById(R.id.txtAllowancesRemarks);
		    cmbExpensesType             =   (Spinner) 	allowanceAddDialog.findViewById(R.id.cmbExpensesType);
			cmbOffloadingBusinessNames  =   (Spinner) 	allowanceAddDialog.findViewById(R.id.cmbOffloadingBusinessNames);
			txtAmount                   =   (EditText) 	allowanceAddDialog.findViewById(R.id.txtAmount);
			cmbExpensesType.setOnItemSelectedListener(this);

			Button btncancel		=	(Button) allowanceAddDialog.findViewById(R.id.btnFuelCancel);
			Button btnSave			=	(Button) allowanceAddDialog.findViewById(R.id.btnFuelSave);
			Button btnscan  		=   (Button) allowanceAddDialog.findViewById(R.id.btnSmartScan);
			
			btncancel.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					allowanceAddDialog.dismiss();
					allowanceAddDialog = null;
				}
			});
			
			btnSave.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					String txAllownaceAmt     =    txtAmount.getText().toString().trim();
					String txAllowanceRemarks =    txtAllowancesRemarks.getText().toString();
					
					if(selected_expenses.equalsIgnoreCase("--Select Allowances--"))
					{
						Utils.displayDialog(context,null,"Select Expense Type",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
						return;
					}
					if(txtAmount.getText().toString().equals(""))
					{
						txtAmount.setError("Enter Amount");
						txtAmount.requestFocus();
						return;
					}
					
					if(selected_expenses.equalsIgnoreCase("Vehicle Fuel Expenses"))
					{
						if(txtAllowancesRemarks.getText().toString().equals(""))
						{
							txtAllowancesRemarks.setError("Enter Remarks");
							txtAllowancesRemarks.requestFocus();
							return;
						}
						saveFuelExpense(txAllownaceAmt,txAllowanceRemarks);
						
					}
					else if(selected_expenses.equalsIgnoreCase("OffLoading Charges"))
					{
						if(txtAmount.getText().toString().equals(""))
						{
							txtAmount.setError("Enter Amount");
							txtAmount.requestFocus();
							return;
						}
						saveOffloadCharge(txAllownaceAmt);
					}
					else if(selected_expenses.equalsIgnoreCase("Amount to Bank"))
					{
						if(txtAllowancesRemarks.getText().toString().equals(""))
						{
							txtAllowancesRemarks.setError("Enter Remarks");
							txtAllowancesRemarks.requestFocus();
							return;
						}
						saveAllownaces(selected_expenses,txAllownaceAmt,txAllowanceRemarks,allowanceAddDialog);
					}
					else if(selected_expenses.equalsIgnoreCase("Miscellaneous Expenses"))
					{
						if(txtAllowancesRemarks.getText().toString().equals(""))
						{
							txtAllowancesRemarks.setError("Enter Remarks");
							txtAllowancesRemarks.requestFocus();
							return;
						}
						saveAllownaces(selected_expenses,txAllownaceAmt,txAllowanceRemarks,allowanceAddDialog);
					}
					else
					{
						saveAllownaces(selected_expenses,txAllownaceAmt,txAllowanceRemarks,allowanceAddDialog);
					}
				
				}
			});
			
			btnscan.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					try
					{						
						Intent imageCapture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			            startActivityForResult(imageCapture, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);						
					}catch(Exception e){
						e.printStackTrace();	
					}
				}
			});
			allowanceAddDialog.show();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		try
		{
			if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK ) 
			{
				try 
				{
			        // recyle unused bitmaps
					if (mImageBitmap != null) 
						mImageBitmap.recycle();					
					Bundle extras = data.getExtras();
					if(extras!=null)
					{
						mImageBitmap = (Bitmap) extras.get("data");
						if(allowanceAddDialog != null && mImageBitmap != null)
						{
							((ImageView)allowanceAddDialog.findViewById(R.id.imgViewPreview)).setImageBitmap(mImageBitmap);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}// catch (IOException e) {
				//	e.printStackTrace();					
				//} finally {
					//if (stream != null)
					//	try {
					//		stream.close();
			        // } catch (IOException e) {
			        //   e.printStackTrace();
			        //  }
				//}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void saveOffloadCharge(String allowanceAmt)
	{
		try
		{
			String selected_businessName = cmbOffloadingBusinessNames.getSelectedItem().toString().trim();
			if(selected_businessName.equals(Constants.NA))
			{
				Utils.displayDialog(context,null,"NA is not a Business Name",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				return;
			}
			
			long offloading = salesDao.getOffloadingCount(VbookApp.getDbInstance(),userID, dayId,cmbExpensesType.getSelectedItem().toString().trim(),selected_businessName);
			String totalCnt = bNamewithCnt.get(selected_businessName);
			if(Integer.parseInt(totalCnt) > offloading)
			{
				saveAllownaces(selected_expenses,allowanceAmt,selected_businessName,allowanceAddDialog);
			}
			else
				Utils.displayDialog(context,null,"Sorry!You dont have DeliveryNotes\nFor Selected BusinessName",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	public void saveFuelExpense(String txtAllowanceAmt,String remarks)
	{
		try
		{
			previous_reading = salesDao.getleatestMeteReading(VbookApp.getDbInstance(),userID, dayId,cmbExpensesType.getSelectedItem().toString().trim());
			
			if(!(Long.parseLong(dayBook.getStartReading())<=Long.parseLong(txtAllowancesRemarks.getText().toString().trim())))
			{
				txtAllowancesRemarks.setError("Entered Reading is Less Than ("+dayBook.getStartReading()+") ");
				txtAllowancesRemarks.setText("");
				return;
			}
				
			if(((previous_reading==0)?0:previous_reading)<Long.parseLong(txtAllowancesRemarks.getText().toString().trim()))
			{
				saveAllownaces(selected_expenses,txtAllowanceAmt,remarks,allowanceAddDialog);
			}
			else
			{
				txtAllowancesRemarks.setError("Entered Reading is Less Than :("+previous_reading+")");
				txtAllowancesRemarks.setText("");
			}
		}
		catch(Exception e)
		{
			//Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void saveAllownaces(String expenseType,String expAmount,String remarks,Dialog dialog_type) 
	{
		try
		{		
			DayBookAllowances allowance = new DayBookAllowances();
			allowance.setAllowancesType(expenseType);
			allowance.setAmt(Utils.getNumberFromMillion(expAmount));
			allowance.setRemarks(remarks);
			long rowid;
			float result = dayBook.getOpeningBal() + totalrecivedAmt;
			if(!(result > Utils.getNumberFromMillion(lblTotalAllowanceAmt.getText().toString())+allowance.getAmt()))
			{
				Utils.displayDialog(context,null,"Sorry!Your Balance is  :"+Utils.currencyInMillion(result-Utils.getNumberFromMillion(lblTotalAllowanceAmt.getText().toString())),Utils.DIALOG_OK,"Ok",null,null,null,null,null);
				dialog_type.dismiss();
				return;	
			}
			String id = dayBook.getDayBookid() + "_";
			allowance.setReserved(id);
			if(allowance.getAllowancesType().equalsIgnoreCase(getResources().getString(R.string.exptypebank)))
				rowid = salesDao.insertDayBookAllowances(VbookApp.getDbInstance(), allowance, userID, dayBook.getDayBookid(),true);
			else
				rowid = salesDao.insertDayBookAllowances(VbookApp.getDbInstance(), allowance, userID, dayBook.getDayBookid(),false);
			
			lblTotalAllowanceAmt.setText(Utils.currencyInMillion(Utils.getNumberFromMillion(lblTotalAllowanceAmt.getText().toString())+allowance.getAmt()));
			allowance.setRowid(rowid);
			allowanceAdapter.insert(allowance, 0);
			if(mImageBitmap!=null) 			// save image here
			{
				id = dayBook.getDayBookid() + "_" + rowid;
				imagesDao.saveBillImage(VbookApp.getDbInstance(), id, mImageBitmap);
				allowance.setReserved(id);
			}
			dialog_type.dismiss();
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	// ***************************************
	// 			Private classes 
	// ***************************************
	class LoadDayBook extends AsyncTask<String, Void, DayBook>
	{
		boolean isViewMode;
		public LoadDayBook(boolean isViewMode)
		{
			this.isViewMode = isViewMode;
		}
		protected void onPreExecute(){showProgressDialog("Loading Day Book Information..... Please wait...");}
		@Override
		protected DayBook doInBackground(String... params)
		{  
			try
			{
				dayBook = salesDao.readDayBook(VbookApp.getDbInstance(),params[0], userID);
				return dayBook; 
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(DayBook result)
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
	}// LoadDayBook
	
	
	class LoadDayBookAllowances extends AsyncTask<Void, Void, DayBook>
	{
		protected void onPreExecute(){showProgressDialog("Loading Day Book Information..... Please wait...");}
		@Override
		protected DayBook doInBackground(Void... params)
		{
			try
			{
				dayBook = salesDao.readDayBookEntries(VbookApp.getDbInstance(), userID, dayId);
				return dayBook;
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return new DayBook();
		}
		protected void onPostExecute(DayBook result)
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
				if(result!=null)
				{
					totalrecivedAmt 			= salesDao.getTotalReciviedAmt(VbookApp.getDbInstance(), userID, dayId);
					float[] tmp 				= salesDao.getPreviousBusinessAmts(VbookApp.getDbInstance(), userID,dayBook.getDayBookid());
					totalrecivedAmt-=tmp[1];

					showAllowanceScreeen();
				}
				else
					Utils.displayDialog(context,null,"Your Allotement has Not be Done.",Utils.DIALOG_OK,"Ok",okclick,null,null,null,null);
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
	}// LoadDayBookAllowances
	
	class LoadDayBookProducts extends AsyncTask<Void, Void, DayBook>
	{
		protected void onPreExecute(){showProgressDialog("Loading Day Book Information..... Please wait...");}
		@Override
		protected DayBook doInBackground(Void... params)
		{  
			try
			{
				float totalsaleAmt 		= salesDao.getTotalSaleAmt(VbookApp.getDbInstance(), userID, dayId);
				float[] tmp 			= salesDao.getPreviousBusinessAmts(VbookApp.getDbInstance(), userID,dayBook.getDayBookid());
				//float totalrecivedAmt1 	= salesDao.getTotalReciviedAmt(VbookApp.getDbInstance(), userID, dayId);
				float bankDepositAmt 	= salesDao.getBankDepositAmt(VbookApp.getDbInstance(), userID, dayBook.getDayBookid());
				dayBook.setTotalpayableFromCust(totalsaleAmt - tmp[0]);
				dayBook.setTotalReceivedFromCust(totalrecivedAmt);
				dayBook.setAmtTobank(bankDepositAmt);
				dayBook.setProductsList(salesDao.readDayBookproducts(VbookApp.getDbInstance(), userID, dayId));
				return dayBook;
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(DayBook result)
		{
			try
			{
				dismissProgressDialog();
				showProductScreeen();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}
	}// LoadDayBookProducts	
	
	public void disPlayfinallyResult(Long result)
	{
		try
		{
			android.content.DialogInterface.OnClickListener okclick = new android.content.DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface arg0, int arg1)
				{
					finish();
				}
			};
			if(result != null && result != -1)
			{
				if(dayBook.getIsreturn())
				{
					Utils.saveData(context, dayBook.getSaleExeName() + Constants.IS_CYCLE_CLOSED, "YES");
				}
				salesDao.markAlltrxns(VbookApp.getDbInstance(),dayBook.getSaleExeName(),dayId,dayBook.getDayBookid());
				Utils.displayDialog(context,null,"Day Book Saved Sucessfully.",Utils.DIALOG_OK,"Ok",okclick,null,null,null,null);
			}
			else
				Utils.displayDialog(context,null,"Some Error happened.",Utils.DIALOG_OK,"Ok",null,null,null,null,null);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}// disPlayfinallyResult
	
	class DayBookSave extends AsyncTask<Void, Void, Long>
	{
		boolean isChangeReq;
		public DayBookSave(boolean isChangeReq)
		{
			this.isChangeReq = isChangeReq;
		}
		protected void onPreExecute(){showProgressDialog("Saving Day Book ..... Please wait...");}
		@Override
		protected Long doInBackground(Void... params)
		{  
			try
			{
				return salesDao.saveDayBook(VbookApp.getDbInstance(), dayBook, dayId,isChangeReq);
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return null;
		}
		protected void onPostExecute(Long result)
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
	}// DayBookSave

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
	{
		try
		{
			selected_expenses = parent.getSelectedItem().toString();
			txtAllowancesRemarks.setText("");
			txtAmount.setText("");
			if(selected_expenses.equalsIgnoreCase("Vehicle Fuel Expenses"))
			{
				cmbOffloadingBusinessNames.setVisibility(View.GONE);
				int maxLength=10;
				txtAllowancesRemarks.setInputType(InputType.TYPE_CLASS_NUMBER);
				txtAllowancesRemarks.setHint("Enter Meter Reading");
				InputFilter[] filter=new InputFilter[1];
				filter[0]= new InputFilter.LengthFilter(maxLength);
				txtAllowancesRemarks.setFilters(filter);
			}
			else if(selected_expenses.equalsIgnoreCase("Miscellaneous Expenses"))
			{
				cmbOffloadingBusinessNames.setVisibility(View.GONE);
				txtAllowancesRemarks.setInputType(InputType.TYPE_CLASS_TEXT);
				txtAllowancesRemarks.setHint("Enter Purpose");
			}
			else if(selected_expenses.equalsIgnoreCase("Amount To Bank"))
			{
				cmbOffloadingBusinessNames.setVisibility(View.GONE);
				txtAllowancesRemarks.setInputType(InputType.TYPE_CLASS_TEXT);
				txtAllowancesRemarks.setHint("Bank Name");
			}
			else if(selected_expenses.equalsIgnoreCase("OffLoading Charges"))
			{
				cmbOffloadingBusinessNames.setVisibility(View.VISIBLE);
				txtAllowancesRemarks.setVisibility(View.GONE); 
				List<String[]>  businessNames	=	salesDao.getBnamesforOffLoadingCharges(VbookApp.getDbInstance(), userID,dayId);
				for(String[] s:businessNames)
					bNamewithCnt.put(s[0], s[1]);				
				cmbOffloadingBusinessNames.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,new ArrayList<String>(bNamewithCnt.keySet())));
			}
			else
			{
				cmbOffloadingBusinessNames.setVisibility(View.GONE);
				txtAllowancesRemarks.setInputType(InputType.TYPE_CLASS_TEXT);
				txtAllowancesRemarks.setHint("Remaks");
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	protected void showCurrentLocation() 
	{
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) 
		{
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(), location.getLatitude());
			Toast.makeText(DayBookActivity.this, message,Toast.LENGTH_LONG).show();
		}
	}
}