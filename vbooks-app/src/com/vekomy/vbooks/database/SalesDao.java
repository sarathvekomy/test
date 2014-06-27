/**
 * 
 */
package com.vekomy.vbooks.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookAllowances;
import com.vekomy.vbooks.app.request.DayBookProduct;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteProduct;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnProduct;
import com.vekomy.vbooks.app.response.AllocatedProduct;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.app.response.TrxnHistory;
import com.vekomy.vbooks.helpers.AppBaseActivity;
import com.vekomy.vbooks.utils.Utils;


/**
 * @author koteswararao
 * 
 */
public class SalesDao
{
	private static String LOG_TAG = "SalesDao";
	
	public final static String COL_ROW_ID 			= "rowid";
	
	public final static String COL_TRXN_SALE_EMP_ID = "SID";
	public final static String COL_TRXN_DAY_ID 		= "DAY_ID";
	public final static String COL_TRXN_UID 		= "UID";
	public final static String COL_TRXN_FROM 		= "TRXN_FROM";
	public final static String COL_TRXN_TYPE 		= "TRXN_TYPE";
	public final static String COL_TRXN_PRODUCT		= "PRODUCT";
	public final static String COL_TRXN_BATCH_NO	= "BATCH_NO";
	public final static String COL_TRXN_QTY 		= "QTY";
	public final static String COL_TRXN_BONUS_QTY 	= "BONUS_QTY";
	public final static String COL_TRXN_REMARK		= "REMARK";
	public final static String COL_TRXN_COST 		= "COST";
	public final static String COL_TRXN_AMT 		= "AMT";
	public final static String COL_TRXN_TO 			= "TRXN_TO";
	public final static String COL_TRXN_FLOW 		= "FLOW";
	public final static String COL_TRXN_ISCOMMIT	= "ISCOMMIT";
	public final static String COL_TRXN_SALE_REF_ID = "SALE_REF_ID";
	public final static String COL_TRXN_ISAPPROVED 	= "ISAPPROVED"; // approved
	//public final static String COL_TRXN_CREATED_ON 	= "CREATED_ON";
	
	public final static String COL_HELP_ID 			= "ID";
	public final static String COL_HELP_DAY_ID 		= "DAY_ID";
	public final static String COL_HELP_ORG_ID 		= "ORG_ID";
	public final static String COL_HELP_INV_NO 		= "INV_NO";
	public final static String COL_HELP_CREATEON	= "CREATEON";
	public final static String COL_HELP_BUSINESS 	= "BUSINESS";
	public final static String COL_HELP_INV_NAME 	= "INV_NAME";
	public final static String COL_HELP_CREATEBY 	= "CREATEBY";
	public final static String COL_HELP_PRV_BALANCE = "PRV_BALANCE";
	public final static String COL_HELP_PAYMENT_TYPE= "PAYMENT_TYPE";
	public final static String COL_HELP_CHKNO		= "CHKNO";
	public final static String COL_HELP_BANK		= "BANK";
	public final static String COL_HELP_BANK_LOC	= "BANK_LOC";
	public final static String COL_HELP_BRANCH 		= "BRANCH";
	public final static String COL_HELP_REPORT_MGR	= "REPORT_MGR";
	public final static String COL_HELP_DESCRIPTION = "DESCRIPTION";
	public final static String COL_HELP_ISCLOSED 	= "ISCLOSED";
	public final static String COL_HELP_ISRETURN 	= "ISRETURN";
	
	public final static String COL_PD_ID 			= "ID";
	public final static String COL_PD_NAME 			= "NAME";
	public final static String COL_PD_BATCHNO 		= "BATCH_NO";
	

	private String[] TRXN_ALL =	{
										COL_TRXN_SALE_EMP_ID,
										COL_TRXN_DAY_ID,
										COL_TRXN_FROM,
										COL_TRXN_TYPE,
										COL_TRXN_PRODUCT,
										COL_TRXN_QTY,
										COL_TRXN_BONUS_QTY,
										COL_TRXN_REMARK,
										COL_TRXN_COST,
										COL_TRXN_AMT,
										COL_TRXN_TO, 
										COL_TRXN_FLOW,
										COL_TRXN_ISCOMMIT,
										COL_TRXN_SALE_REF_ID,
										COL_TRXN_ISAPPROVED
//										COL_TRXN_CREATED_ON
								};
	
	private String[] HELP_ALL =	{
										COL_HELP_ID,
										COL_HELP_ORG_ID,
										COL_HELP_INV_NO,
										COL_HELP_CREATEON,
										COL_HELP_BUSINESS,
										COL_HELP_INV_NAME,
										COL_HELP_CREATEBY,
										COL_HELP_PRV_BALANCE,
										COL_HELP_PAYMENT_TYPE,
										COL_HELP_CHKNO,
										COL_HELP_BANK,
										COL_HELP_BANK_LOC,
										COL_HELP_BRANCH,
										COL_HELP_DESCRIPTION,
										COL_HELP_ISCLOSED
								};	
	enum PRODUCT_TYPE 
	{
		PAYMENT("PAYMENT"), BF("BF"), PRODUCT("PRD"),JOURNAL("JOURNAL"),CF("CF"),EXP("EXP"),BANK("BANK");
	 
		private String productCode;
	 
		private PRODUCT_TYPE(String productCode) {
			this.productCode = productCode;
		}

		public String getProductCode() {
			return productCode;
		}
	}
	enum FLOW 
	{
		IN(false), OUT(true);
	 
		private boolean flow;
		
		private FLOW(boolean flow) {
			this.flow = flow;
		}		
		public boolean getFlow() {
			return flow;
		}
	}
	
	enum ISCOMMIT
	{
		NO(false), YES(true);
	 
		private boolean isCommit;
		
		private ISCOMMIT(boolean isCommit) {
			this.isCommit = isCommit;
		}
		
		public boolean getCommit() {
			return isCommit;
		}
	}
	
	enum ISAPPROVED
	{
		NO(false), YES(true);
	 
		private boolean isApproved;
		
		private ISAPPROVED(boolean isApproved) {
			this.isApproved = isApproved;
		}
		
		public boolean getApproved() {
			return isApproved;
		}
	}
	
	public SalesDao()
	{
		
	}
	public SalesDao(Context context)
	{
		try
		{
			//dbHelper = new DBHelper(context);
			//open();
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
	}

	public void open() throws SQLException
	{
		try
		{
			//database = dbHelper.getWritableDatabase();
			//database = dbHelper.getReadableDatabase();
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
	}

	public void close()
	{
		try
		{
			//dbHelper.close();
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
	}
	/**
	 * 
	 * @param deloveryNote
	 * @return
	 */
	public long saveDeliveryNote(SQLiteDatabase db,DeliveryNote deloveryNote,boolean isChangeReq)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH
			String sale_ref_id;
			if(isChangeReq)
				sale_ref_id	=	Constants.DN + deloveryNote.getInvoiceNo() + Constants.CR;
			else
				sale_ref_id	=	Constants.DN + deloveryNote.getInvoiceNo();
			String dayid	=	Utils.getData(VbookApp.getInstance(),deloveryNote.getCreatedBy() +Constants.CYCLE_ID, Utils.getUniqueIDfromDate().substring(0,8));
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ID, sale_ref_id);
			values.put(COL_HELP_DAY_ID, dayid);
			values.put(COL_HELP_ORG_ID, deloveryNote.getOrganizationId());
			values.put(COL_HELP_INV_NO, deloveryNote.getInvoiceNo());
			values.put(COL_HELP_CREATEON, deloveryNote.getCreatedOn());
			values.put(COL_HELP_BUSINESS,deloveryNote.getBusinessName());
			values.put(COL_HELP_INV_NAME, deloveryNote.getInvoiceName());
			values.put(COL_HELP_CREATEBY, deloveryNote.getCreatedBy());
			values.put(COL_HELP_PRV_BALANCE, (deloveryNote.getPresentAdvance()==0)?deloveryNote.getPreviousCredit():-deloveryNote.getPresentAdvance());
			values.put(COL_HELP_PAYMENT_TYPE, deloveryNote.getPaymentType());
			values.put(COL_HELP_CHKNO, deloveryNote.getChequeNo());
			values.put(COL_HELP_BANK, deloveryNote.getBankName());
			values.put(COL_HELP_BRANCH, deloveryNote.getBranchName());
			values.put(COL_HELP_BANK_LOC,deloveryNote.getBranchLocation());
			values.put(COL_HELP_DESCRIPTION,deloveryNote.getRemarks()==null?"":deloveryNote.getRemarks());
			//values.put(COL_DN_ISCLOSED, 1);
			if(db.insert(DBHelper.TBL_SALE_HELP, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			// SID | DAY_ID | FROM | PRODUCT | BATCH_NO | QTY | BONUS_QTY | BONUS_REMARKS | COST | AMT | TO | FLOW | ISCOMMIT | SALE_REF_ID | ISAPPROVED
			List<DeliveryNoteProduct> products = deloveryNote.getProducts();
			String salesId 		= deloveryNote.getCreatedBy();
			String businesName 	= deloveryNote.getBusinessName();
			// Products insertions...............
			if(products!=null)
			{
				for (int index = 0; index < products.size(); index++) 
				{
					values.clear();
					values.put(COL_TRXN_SALE_EMP_ID, salesId);
					values.put(COL_TRXN_DAY_ID, dayid);
					values.put(COL_TRXN_FROM, salesId);
					values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PRODUCT.getProductCode());				
					values.put(COL_TRXN_PRODUCT, products.get(index).getProductName());
					values.put(COL_TRXN_BATCH_NO, products.get(index).getBatchNumber());
					values.put(COL_TRXN_QTY, products.get(index).getProductQty());
					values.put(COL_TRXN_BONUS_QTY, products.get(index).getBonusQty());
					values.put(COL_TRXN_REMARK, products.get(index).getBonusReason());
					values.put(COL_TRXN_COST, products.get(index).getProductCost());
					values.put(COL_TRXN_AMT, products.get(index).getTotalCost());
					values.put(COL_TRXN_TO, businesName);
					values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
					values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
					values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
					if(isChangeReq)
						values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
					else
						values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
					if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
					{
						// need To handle any Error occurred here ........................
						Log.d("DB", "insertion failed");
						return -1;
					}
				}
			}
			// Payment insertions 
			values.clear();
			values.put(COL_TRXN_SALE_EMP_ID, salesId);
			values.put(COL_TRXN_DAY_ID, dayid);
			values.put(COL_TRXN_FROM, businesName);
			values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PAYMENT.getProductCode());
			values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.PAYMENT.getProductCode());
			values.put(COL_TRXN_BATCH_NO, "");
			values.put(COL_TRXN_QTY, 1);
			values.put(COL_TRXN_BONUS_QTY, 0);
			values.put(COL_TRXN_REMARK, "");
			values.put(COL_TRXN_COST,deloveryNote.getPresentPayment());
			values.put(COL_TRXN_AMT, deloveryNote.getPresentPayment());
			values.put(COL_TRXN_TO, salesId);
			values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
			values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
			values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
			if(isChangeReq)
				values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
			else
				values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
			
			if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			return 0;	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return -1;
	}
	
	public long saveSalesReturn(SQLiteDatabase db,SalesReturn salesreturn,boolean isChangeReq)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH		
			String sale_ref_id;
			if(isChangeReq)
				sale_ref_id	=	Constants.SR + salesreturn.getInvoiceNo() + Constants.CR;	
			else
				sale_ref_id	=	Constants.SR + salesreturn.getInvoiceNo();
			String dayid	= 	Utils.getData(VbookApp.getInstance(),salesreturn.getCreatedBy() + Constants.CYCLE_ID, Utils.getUniqueIDfromDate().substring(0,8));
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ID, sale_ref_id);
			values.put(COL_HELP_DAY_ID, dayid);
			values.put(COL_HELP_ORG_ID, salesreturn.getOrganizationId());
			values.put(COL_HELP_INV_NO, salesreturn.getInvoiceNo());
			values.put(COL_HELP_CREATEON, salesreturn.getCreatedOn());
			values.put(COL_HELP_BUSINESS,salesreturn.getBusinessName());
			values.put(COL_HELP_INV_NAME, salesreturn.getInvoiceName());
			values.put(COL_HELP_CREATEBY, salesreturn.getCreatedBy());
			values.put(COL_HELP_PRV_BALANCE, 0);
			values.put(COL_HELP_PAYMENT_TYPE, "ADV");
			values.put(COL_HELP_DESCRIPTION,salesreturn.getRemarks()==null?"":salesreturn.getRemarks());
			//values.put(COL_DN_ISCLOSED, 1);
			//values.put(COL_DN_CHKNO, deloveryNote.getChequeNo());
			//values.put(COL_DN_BANK, deloveryNote.getBankName());
			//values.put(COL_DN_BRANCH, deloveryNote.getBranchName()); 
			if(db.insert(DBHelper.TBL_SALE_HELP, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			// SID | DAY_ID | FROM | PRODUCT | BATCH_NO | QTY | BONUS_QTY | BONUS_REMARKS | COST | AMT | TO | FLOW | ISCOMMIT | SALE_REF_ID | ISAPPROVED
			List<SalesReturnProduct> products = salesreturn.getProducts();
			String salesId 		= salesreturn.getCreatedBy();
			String businesName 	= salesreturn.getBusinessName();
			// Products insertions...............
			if(products!=null)
			{
				for (int index = 0; index < products.size(); index++) 
				{
					values.clear();
					values.put(COL_TRXN_SALE_EMP_ID, salesId);
					values.put(COL_TRXN_DAY_ID, dayid);
					values.put(COL_TRXN_FROM, businesName);
					values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PRODUCT.getProductCode());
					values.put(COL_TRXN_PRODUCT, products.get(index).getProductName());
					values.put(COL_TRXN_BATCH_NO, products.get(index).getBatchNumber());
					values.put(COL_TRXN_QTY, products.get(index).getResaleQty());
					values.put(COL_TRXN_BONUS_QTY, products.get(index).getDamagedQty());
					values.put(COL_TRXN_REMARK, products.get(index).getBonusReason());
					values.put(COL_TRXN_COST, 0);
					values.put(COL_TRXN_AMT, 0);
					values.put(COL_TRXN_TO, salesId);
					values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
					values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
					values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
					//if(isChangeReq)
					values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
					//else
					//	values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
					if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
					{
						// need To handle any Error occurred here ........................
						Log.d("DB", "insertion failed");
						return -1;
					}
				}
			}	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
		// Payment insertions 
		return 0;
	}
	public long saveJournal(SQLiteDatabase db,Journal journal,boolean isChangeReq)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH
			String sale_ref_id;
			if(isChangeReq)
				sale_ref_id	=	Constants.JN + journal.getInvoiceNo() + Constants.CR;	
			else
				sale_ref_id	=	Constants.JN + journal.getInvoiceNo();
			
			String dayid	=	Utils.getData(VbookApp.getInstance(),journal.getCreatedBy() + Constants.CYCLE_ID, Utils.getUniqueIDfromDate().substring(0,8));
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ID, sale_ref_id);
			values.put(COL_HELP_DAY_ID, dayid);
			values.put(COL_HELP_ORG_ID, journal.getOrganizationId());
			values.put(COL_HELP_INV_NO, journal.getInvoiceNo());
			values.put(COL_HELP_CREATEON, journal.getCreatedOn());
			values.put(COL_HELP_BUSINESS,journal.getBusinessName());
			values.put(COL_HELP_INV_NAME, journal.getInvoiceName());
			values.put(COL_HELP_CREATEBY, journal.getCreatedBy());
			values.put(COL_HELP_PRV_BALANCE, journal.getAmount());
			values.put(COL_HELP_PAYMENT_TYPE, journal.getJournalType()); 
			//values.put(COL_DN_ISCLOSED, 1);
			if(db.insert(DBHelper.TBL_SALE_HELP, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			// SID | DAY_ID | FROM | PRODUCT | BATCH_NO | QTY | BONUS_QTY | BONUS_REMARKS | COST | AMT | TO | FLOW | ISCOMMIT | SALE_REF_ID | ISAPPROVED
			String salesId 		= journal.getCreatedBy();
			String businesName 	= journal.getBusinessName();
			values.clear();
			values.put(COL_TRXN_SALE_EMP_ID, salesId);
			values.put(COL_TRXN_DAY_ID, dayid);
			values.put(COL_TRXN_FROM, businesName);
			values.put(COL_TRXN_TYPE, PRODUCT_TYPE.JOURNAL.getProductCode());
			values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.JOURNAL.getProductCode());
			values.put(COL_TRXN_BATCH_NO, "");
			values.put(COL_TRXN_QTY, 1);
			values.put(COL_TRXN_BONUS_QTY, 0);
			values.put(COL_TRXN_REMARK, journal.getDescription());
			values.put(COL_TRXN_COST,journal.getAmount());
			values.put(COL_TRXN_AMT, journal.getAmount());
			values.put(COL_TRXN_TO, Constants.ORG_ID);
			values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
			values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
			values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
			values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
			if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
		return 0;
	}
	public long saveDayBook(SQLiteDatabase db,DayBook daybook,String dayid,boolean isChangeReq)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH		
			String sale_ref_id;
			int result;
			if(isChangeReq)
				sale_ref_id	=	daybook.getDayBookid() + Constants.CR;
			else
				sale_ref_id	=	Constants.DB + daybook.getDayBookid();
			
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ID, sale_ref_id);
			values.put(COL_HELP_DAY_ID,dayid);
			values.put(COL_HELP_ORG_ID, Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, "0"));
			values.put(COL_HELP_INV_NO, daybook.getDayBookid());
			values.put(COL_HELP_CREATEON, daybook.getCreatedOn());
			values.put(COL_HELP_BUSINESS,Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, "0"));
			values.put(COL_HELP_INV_NAME, daybook.getDayBookid());
			values.put(COL_HELP_CREATEBY, daybook.getSaleExeName());
			values.put(COL_HELP_PRV_BALANCE, daybook.getCloseingBal());
			values.put(COL_HELP_PAYMENT_TYPE, "DAYBOOK"); 
			values.put(COL_HELP_CHKNO, daybook.getVehicleNo());
			values.put(COL_HELP_BANK, daybook.getDriverName());
			values.put(COL_HELP_BRANCH, daybook.getStartReading());
			values.put(COL_HELP_BANK_LOC, daybook.getEndReading());
			values.put(COL_HELP_REPORT_MGR, daybook.getReportingManager());
			values.put(COL_HELP_ISRETURN, daybook.getIsreturn());
			values.put(COL_HELP_DESCRIPTION,daybook.getRemarks());
			
			if(isChangeReq)
			{
				values.put(COL_HELP_ISCLOSED, 1);
				if(db.insertWithOnConflict(DBHelper.TBL_SALE_HELP, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
				{
					// need To handle any Error occurred here ........................
					Log.d("DB", "insertion failed");
					return -1;
				}
			}
			else
			{
				result = db.update(DBHelper.TBL_SALE_HELP,values,COL_HELP_ID + " = \'" + daybook.getDayBookid() + "\'", null);
				if(result < 1)
				{
					// need To handle any Error occurred here ........................
					Log.d("DB", "Update failed");
					return -1;
				}				
			}

			String salesId 		= daybook.getSaleExeName();
			// SID | DAY_ID | FROM | PRODUCT | BATCH_NO | QTY | BONUS_QTY | BONUS_REMARKS | COST | AMT | TO | FLOW | ISCOMMIT | SALE_REF_ID | ISAPPROVED
			List<DayBookProduct> products = daybook.getProductsList();
			// Products insertions...............
			String temp = "";
			if(products!=null)
			{
				for (int index = 0; index < products.size(); index++) 
				{
					values.clear();
					values.put(COL_TRXN_SALE_EMP_ID, salesId);
					values.put(COL_TRXN_DAY_ID, dayid);
					values.put(COL_TRXN_FROM, salesId);
					values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PRODUCT.getProductCode());				
					values.put(COL_TRXN_PRODUCT, products.get(index).getProductName());
					values.put(COL_TRXN_BATCH_NO, products.get(index).getBatchNumber());
					values.put(COL_TRXN_QTY, products.get(index).getReturnFactoryStockQty());
					if(daybook.getIsreturn())
					{
						values.put(COL_TRXN_BONUS_QTY, products.get(index).getCloseingStockQty());
						temp = String.valueOf(products.get(index).getOpeningStockQty()) 	+ "|" +
								   String.valueOf(products.get(index).getSalesReturnStockQty())	+ "|" +
								   String.valueOf(products.get(index).getSoldStockQty());
					}
					else
					{
						values.put(COL_TRXN_BONUS_QTY, 0);
						temp = String.valueOf(products.get(index).getOpeningStockQty()) 	+ "|" +
								   String.valueOf(products.get(index).getSalesReturnStockQty())	+ "|" +
								   String.valueOf(products.get(index).getSoldStockQty())		+ "|" +
								   String.valueOf(products.get(index).getCloseingStockQty());
					}
					values.put(COL_TRXN_REMARK, temp);
					values.put(COL_TRXN_COST, 0);
					values.put(COL_TRXN_AMT, 0);
					values.put(COL_TRXN_TO, Constants.ORG_ID);
					values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
					values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
					values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
					if(isChangeReq)
						values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
					else
						values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
					if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
					{
						// need To handle any Error occurred here ........................
						Log.d("DB", "insertion failed");
						return -1;
					}
				}
			}
			// insert Modify Allowances list here
			if(isChangeReq)
			{
				List<DayBookAllowances> allowancesList = daybook.getAllowancesList();
				if(allowancesList!=null)
				{
					for (int index = 0; index < allowancesList.size(); index++) 
					{
						values.clear();
						values.put(COL_TRXN_SALE_EMP_ID, salesId);
						values.put(COL_TRXN_DAY_ID, dayid);
						values.put(COL_TRXN_FROM, salesId);
						values.put(COL_TRXN_UID,"");
						values.put(COL_TRXN_TO,Constants.ORG_ID);
						values.put(COL_TRXN_PRODUCT, allowancesList.get(index).getAllowancesType());
						if(allowancesList.get(index).getAllowancesType().equalsIgnoreCase("Amount To Bank"))
							values.put(COL_TRXN_TYPE, PRODUCT_TYPE.BANK.getProductCode());
						else
							values.put(COL_TRXN_TYPE, PRODUCT_TYPE.EXP.getProductCode());
						values.put(COL_TRXN_BATCH_NO,"");
						values.put(COL_TRXN_QTY, 1);
						values.put(COL_TRXN_BONUS_QTY, 0);
						values.put(COL_TRXN_REMARK, allowancesList.get(index).getRemarks());
						values.put(COL_TRXN_COST, allowancesList.get(index).getAmt());
						values.put(COL_TRXN_AMT, allowancesList.get(index).getAmt());		 
						values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
						values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
						values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
						values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
						long pos = db.insert(DBHelper.TBL_SALE_TRXN, null, values);
						if(pos == -1)
						{
							// need To handle any Error occurred here ........................
							Log.d("DB", "insertion failed");
							return -1;
						}
					}
				}
			}
			else
			{
				values.clear();
				// update opening Bal Ref iD
				values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
				// 	update All allowances Ref IDs
				db.update(DBHelper.TBL_SALE_TRXN, values, 
													COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
													COL_TRXN_FROM 			+ " =? AND (" +
													COL_TRXN_TYPE 			+ " =? OR "  +
													COL_TRXN_TYPE 			+ " =? ) AND " +
													COL_TRXN_TO 			+ " =? AND " +												
													COL_TRXN_DAY_ID 		+ " =? AND " +
													COL_TRXN_SALE_REF_ID 	+ " =? ",
													new String[]{
																	salesId,
																	salesId,
																	PRODUCT_TYPE.EXP.getProductCode(),
																	PRODUCT_TYPE.BANK.getProductCode(),
																	Constants.ORG_ID, 
																	dayid,
																	daybook.getDayBookid()
																});
			}
			values.clear();
			
			if(daybook.getIsreturn())
			{
				// Payment insertions 
				values.put(COL_TRXN_SALE_EMP_ID, salesId);
				values.put(COL_TRXN_DAY_ID, dayid);
				values.put(COL_TRXN_FROM, salesId);
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PAYMENT.getProductCode());
				values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.PAYMENT.getProductCode());
				values.put(COL_TRXN_BATCH_NO, "");
				values.put(COL_TRXN_QTY, 1);
				values.put(COL_TRXN_BONUS_QTY, 0);
				values.put(COL_TRXN_REMARK, "");
				values.put(COL_TRXN_COST,daybook.getAmtToFactory());
				values.put(COL_TRXN_AMT,daybook.getAmtToFactory());
				values.put(COL_TRXN_TO, Constants.ORG_ID);
				values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
				values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
				values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
				values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
				if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
				{
					// need To handle any Error occurred here ........................
					Log.d("DB", "insertion failed");
					return -1;
				}	
			}
			else
			{
		    	CustomerAmountInfo saleExecutive = new CustomerAmountInfo();
		    	Float amt = daybook.getCloseingBal();
		    	if(amt > 0)
		    	{
		    		saleExecutive.setCreditAmount(amt);
		    		saleExecutive.setAdvanceAmount(0f);
		    	}
		    	else
		    	{
		    		saleExecutive.setAdvanceAmount(amt);
		    		saleExecutive.setCreditAmount(0f);
		    	}
		    	// here we insert customer Total Payable and Total Received.
		    	saleExecutive.setInvoiceName(getTotalSaleAmt(VbookApp.getDbInstance(),daybook.getSaleExeName(), dayid)+ "|" +getTotalReciviedAmt(VbookApp.getDbInstance(),daybook.getSaleExeName(), dayid));
		    	saleExecutive.setBusinessName(daybook.getSaleExeName());
		    	insertcustomerBF(VbookApp.getDbforServices(),saleExecutive,daybook.getSaleExeName(),true,Constants.NA);
			}
			values.clear();
			if(isChangeReq)
			{
		    	CustomerAmountInfo saleExecutive = new CustomerAmountInfo();
		    	Float amt = daybook.getOpeningBal();
		    	if(amt > 0)
		    	{
		    		saleExecutive.setCreditAmount(amt);
		    		saleExecutive.setAdvanceAmount(0f);
		    	}
		    	else
		    	{
		    		saleExecutive.setAdvanceAmount(amt);
		    		saleExecutive.setCreditAmount(0f);
		    	}
		    	saleExecutive.setUid(daybook.getSaleExeName());
		    	Cursor cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
								new String[]{
												COL_TRXN_REMARK
											},
											COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
											COL_TRXN_TYPE 			+ " =? AND " +
											COL_TRXN_SALE_REF_ID	+ " =? ",
								new String[]
										{ 
		    								salesId,
											PRODUCT_TYPE.CF.getProductCode(),
											sale_ref_id.substring(0, sale_ref_id.length()-3)
										},null, null,null,null);
		    	cursor.moveToFirst();
		    	String remarks = "0|0";
		    	while (!cursor.isAfterLast())
		    	{
		    		remarks = cursor.getString(0);
		    		cursor.moveToNext();
		    	}
		    	// 	Make sure to close the cursor
		    	cursor.close();		    	
		    	saleExecutive.setInvoiceName(remarks==null?"0|0":remarks);
		    	saleExecutive.setBusinessName(daybook.getSaleExeName());
		    	insertcustomerBF(VbookApp.getDbforServices(),saleExecutive,daybook.getSaleExeName(),true,sale_ref_id);
			}
			else
			{
				// update opening Bal Ref iD
				values.put(COL_TRXN_SALE_REF_ID, sale_ref_id);
				result = db.update(DBHelper.TBL_SALE_TRXN, values, 
													COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
													COL_TRXN_FROM 			+ " =? AND " +
													COL_TRXN_TYPE 			+ " =? AND " +
													COL_TRXN_TO 			+ " =? AND " +
													COL_TRXN_DAY_ID 		+ " =? AND " +
													COL_TRXN_SALE_REF_ID 	+ " =? ",
										new String[]{ 
														salesId,
														Constants.ORG_ID,
														PRODUCT_TYPE.CF.getProductCode(),
														salesId,
														dayid,
														daybook.getDayBookid()
													});
				if(result==0) // if closing Balance is negative
				{
					result = db.update(DBHelper.TBL_SALE_TRXN, values, 
													COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
													COL_TRXN_FROM 			+ " =? AND " +
													COL_TRXN_TYPE 			+ " =? AND " +
													COL_TRXN_TO 			+ " =? AND " +
													COL_TRXN_DAY_ID 		+ " =? AND " +
													COL_TRXN_SALE_REF_ID 	+ " =? ",
										new String[]{ 
														salesId,
														salesId,
														PRODUCT_TYPE.CF.getProductCode(),
														Constants.ORG_ID,
														dayid,
														daybook.getDayBookid()
													});					
				}
			}
			return 0;
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}// saveDayBook
	
	public void saveReportMgrDayBook(SQLiteDatabase db,DayBook dayBookentry)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_HELP_REPORT_MGR, dayBookentry.getReportingManager());
			db.update(DBHelper.TBL_SALE_HELP, values, COL_HELP_ID + " = " + dayBookentry.getDayBookid(), null);
		}
		catch(Exception e)
		{
		}
	}
	public long insertDayBookEntry(SQLiteDatabase db,DayBook dayBookentry,String dayid)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ID, dayBookentry.getDayBookid());
			values.put(COL_HELP_DAY_ID, dayid);
			values.put(COL_HELP_ORG_ID, Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, "0"));
			values.put(COL_HELP_INV_NO, dayid);
			values.put(COL_HELP_CREATEON, AppBaseActivity.dateformat.format(new Date()));
			values.put(COL_HELP_BUSINESS,Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, "0"));
			values.put(COL_HELP_INV_NAME, dayBookentry.getDayBookid());
			values.put(COL_HELP_CREATEBY, Utils.getData(VbookApp.getInstance(), Constants.USER_ID, "0"));
			values.put(COL_HELP_PRV_BALANCE, dayBookentry.getOpeningBal());
			values.put(COL_HELP_PAYMENT_TYPE, "DAYBOOK"); 
			values.put(COL_HELP_CHKNO, dayBookentry.getVehicleNo());
			values.put(COL_HELP_BANK, dayBookentry.getDriverName());
			values.put(COL_HELP_BRANCH, dayBookentry.getStartReading());
			values.put(COL_HELP_BANK_LOC, dayBookentry.getEndReading());
			if(db.insertWithOnConflict(DBHelper.TBL_SALE_HELP, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
			}
			return 0;	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	public long insertDayBookAllowances(SQLiteDatabase db,DayBookAllowances dayBookAllowance,String salesId,String sale_ref_id,boolean isBankDeposit)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH		
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_SALE_EMP_ID,salesId);
			values.put(COL_TRXN_DAY_ID, Utils.getData(VbookApp.getInstance(), salesId + Constants.CYCLE_ID,Constants.NA));
			values.put(COL_TRXN_UID,dayBookAllowance.getReserved()==null?"":dayBookAllowance.getReserved());
			values.put(COL_TRXN_FROM,salesId );
			values.put(COL_TRXN_TO,Constants.ORG_ID);
			if(isBankDeposit)
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.BANK.getProductCode());
			else
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.EXP.getProductCode());
			
			values.put(COL_TRXN_PRODUCT, dayBookAllowance.getAllowancesType());
			values.put(COL_TRXN_BATCH_NO,"");
			values.put(COL_TRXN_QTY, 1);
			values.put(COL_TRXN_BONUS_QTY, 0);
			values.put(COL_TRXN_REMARK, dayBookAllowance.getRemarks());
			//values.put(COL_TRXN_COST, product.getProductCost());
			values.put(COL_TRXN_COST, dayBookAllowance.getAmt());
			values.put(COL_TRXN_AMT, dayBookAllowance.getAmt());		 
			values.put(COL_TRXN_FLOW, FLOW.OUT.getFlow());
			values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.NO.getCommit());
			values.put(COL_TRXN_SALE_REF_ID,sale_ref_id);
			values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
			long pos = db.insert(DBHelper.TBL_SALE_TRXN, null, values);
			if(pos == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			return pos;	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	
	public int deleteDayBookAllowance(SQLiteDatabase db,DayBookAllowances dayBookAllowance)
	{
		try
		{
			return db.delete(DBHelper.TBL_SALE_TRXN, COL_ROW_ID + " = " + dayBookAllowance.getRowid(), null);
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		return -1;
	}
	public int updateDayBookAllowance(SQLiteDatabase db,DayBookAllowances dayBookAllowance)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_REMARK, dayBookAllowance.getRemarks());
			values.put(COL_TRXN_COST, dayBookAllowance.getAmt());
			values.put(COL_TRXN_AMT, dayBookAllowance.getAmt());
			return db.update(DBHelper.TBL_SALE_TRXN, values, COL_ROW_ID + " = " + dayBookAllowance.getRowid(), null); 
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		return -1;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DeliveryNote readDeliveryNote(SQLiteDatabase db,String saleRefId,String userid,boolean isViewMode)
	{
		DeliveryNote deliveryNote = new DeliveryNote();
		try
		{
			// read Products
			Cursor cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
												new String[]{	COL_TRXN_PRODUCT,
																COL_TRXN_BATCH_NO,
																COL_TRXN_QTY,
																COL_TRXN_BONUS_QTY,
																COL_TRXN_REMARK,
																COL_TRXN_COST,
																COL_TRXN_AMT
															},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
												COL_TRXN_FROM 			+ " =? AND " +
												COL_TRXN_TYPE 			+ " =? AND " +
												//COL_TRXN_ISAPPROVED		+ " = 1 AND "+
												COL_TRXN_SALE_REF_ID 	+ " =? ",
												new String[]{userid,userid,PRODUCT_TYPE.PRODUCT.getProductCode(),saleRefId},
												null, null,null,null);
			cursor.moveToFirst(); 
			List<DeliveryNoteProduct> products = new ArrayList<DeliveryNoteProduct>();
			DeliveryNoteProduct product;
			float presentPayable = 0;
			while (!cursor.isAfterLast())
			{
				product = new DeliveryNoteProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				product.setProductQty(cursor.getInt(2));
				product.setBonusQty(cursor.getInt(3));
				product.setBonusReason(cursor.getString(4));
				product.setProductCost(cursor.getFloat(5));
				product.setTotalCost(cursor.getFloat(6));
				if(!isViewMode)
				{
					int npresentAvailable = getAvailableproductsQty(db,userid,Utils.getData(VbookApp.getInstance(), userid + Constants.CYCLE_ID, Constants.NA),product.getProductName(),product.getBatchNumber());
					product.setAvailableQty(npresentAvailable + product.getProductQty() + product.getBonusQty());
				}
				presentPayable+=cursor.getFloat(6);
				products.add(product);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			deliveryNote.setProducts(products);
			
			cursor = db.query(DBHelper.TBL_SALE_HELP, HELP_ALL, COL_HELP_ID + " =? ",new String[]{saleRefId},null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				deliveryNote.setOrganizationId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_HELP_ORG_ID))));
				deliveryNote.setInvoiceNo(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NO)));
				deliveryNote.setCreatedOn(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEON)));
				deliveryNote.setBusinessName(cursor.getString(cursor.getColumnIndex(COL_HELP_BUSINESS)));
				deliveryNote.setInvoiceName(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NAME)));
				deliveryNote.setCreatedBy(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEBY)));
				float preBalance = cursor.getFloat(cursor.getColumnIndex(COL_HELP_PRV_BALANCE));
				if(preBalance < 0)
				{
					deliveryNote.setPresentAdvance(-1*preBalance);
					deliveryNote.setPreviousCredit(0);
				}
				else
				{
					deliveryNote.setPresentAdvance(0);
					deliveryNote.setPreviousCredit(preBalance);
				}
				deliveryNote.setPaymentType(cursor.getString(cursor.getColumnIndex(COL_HELP_PAYMENT_TYPE)));
				deliveryNote.setChequeNo(cursor.getString(cursor.getColumnIndex(COL_HELP_CHKNO)));
				deliveryNote.setBankName(cursor.getString(cursor.getColumnIndex(COL_HELP_BANK)));
				deliveryNote.setBranchName(cursor.getString(cursor.getColumnIndex(COL_HELP_BRANCH)));
				deliveryNote.setBranchLocation(cursor.getString(cursor.getColumnIndex(COL_HELP_BANK_LOC)));
				deliveryNote.setRemarks(cursor.getString(cursor.getColumnIndex(COL_HELP_DESCRIPTION)));
				
				deliveryNote.setPresentPayable(presentPayable);
				deliveryNote.setTotalPayable(presentPayable + (preBalance));
				
				// read payments info
				Cursor inCursor = db.query(DBHelper.TBL_SALE_TRXN,new String[]{"COALESCE(SUM("+COL_TRXN_AMT+"),0)"},
													COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
													COL_TRXN_TO 			+ " =? AND " +
													COL_TRXN_TYPE 			+ " =? AND " +
													//COL_TRXN_ISAPPROVED		+ " = 1 AND "+
													COL_TRXN_SALE_REF_ID 	+ " =? ",
													new String[]{userid,userid,PRODUCT_TYPE.PAYMENT.getProductCode(),saleRefId},
													null, null,null,null);
				inCursor.moveToFirst();
				float presentPayment = 0;
				while (!inCursor.isAfterLast())
				{
					presentPayment+=inCursor.getFloat(0);
					inCursor.moveToNext();
				}
				deliveryNote.setPresentPayment(presentPayment);
				deliveryNote.setBalance(deliveryNote.getTotalPayable()-presentPayment);
				cursor.moveToNext();
			}	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return deliveryNote;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public SalesReturn readSalesReturn(SQLiteDatabase db,String saleRefId,String userid)
	{
		SalesReturn salesreturn = new SalesReturn();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, HELP_ALL, COL_HELP_ID + " =? ",new String[]{saleRefId},null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				salesreturn.setOrganizationId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_HELP_ORG_ID))));
				salesreturn.setInvoiceNo(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NO)));
				salesreturn.setCreatedOn(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEON)));
				salesreturn.setBusinessName(cursor.getString(cursor.getColumnIndex(COL_HELP_BUSINESS)));
				salesreturn.setInvoiceName(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NAME)));
				salesreturn.setCreatedBy(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEBY)));
				salesreturn.setRemarks(cursor.getString(cursor.getColumnIndex(COL_HELP_DESCRIPTION)));
				cursor.moveToNext();
			}
			cursor.close();
			
			// read Products
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
												new String[]{	COL_TRXN_PRODUCT,
																COL_TRXN_BATCH_NO,
																COL_TRXN_QTY,
																COL_TRXN_BONUS_QTY,
																COL_TRXN_REMARK,
																COL_TRXN_AMT,
																COL_TRXN_ISAPPROVED
															},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " 	+
												COL_TRXN_FROM 			+ " =? AND " 	+
												COL_TRXN_TYPE 			+ " =? AND " 	+
												COL_TRXN_TO				+ " =? AND " 	+
												COL_TRXN_SALE_REF_ID 	+ " =? ",
												new String[]{userid,salesreturn.getBusinessName(),PRODUCT_TYPE.PRODUCT.getProductCode(),userid,saleRefId},
												null, null,null,null);
			cursor.moveToFirst(); 
			List<SalesReturnProduct> products = new ArrayList<SalesReturnProduct>();
			SalesReturnProduct product;
			while (!cursor.isAfterLast())
			{
				product = new SalesReturnProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				product.setResaleQty(cursor.getInt(2));
				product.setDamagedQty(cursor.getInt(3));
				product.setBonusReason(cursor.getString(4));
				product.setTotalQty( product.getResaleQty() + product.getDamagedQty() );
				products.add(product);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			salesreturn.setProducts(products);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return salesreturn;
	}	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public DayBook readDayBook(SQLiteDatabase db,String saleRefId,String userid)
	{
		DayBook dayBook = new DayBook();
		try
		{
			dayBook.setSaleExeName(userid);
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP,
								new String[]{
												COL_HELP_CHKNO,
												COL_HELP_BANK,
												COL_HELP_BRANCH,
												COL_HELP_BANK_LOC,
												COL_HELP_CREATEON,
												COL_HELP_INV_NO,
												COL_HELP_ORG_ID,
												COL_HELP_REPORT_MGR,
												COL_HELP_ISRETURN,
												COL_HELP_PRV_BALANCE,
												COL_HELP_DESCRIPTION,
												COL_HELP_DAY_ID
											},
											COL_HELP_ID + " =? ",
								new String[]{
												saleRefId
											},null, null,null,null);
			cursor.moveToFirst();
			String cycle_id="";
			while (!cursor.isAfterLast())
			{
				dayBook.setDayBookid(saleRefId);
				dayBook.setVehicleNo(cursor.getString(0));
				dayBook.setDriverName(cursor.getString(1));
				dayBook.setStartReading(cursor.getString(2));
				dayBook.setEndReading(cursor.getString(3));
				dayBook.setCreatedOn(cursor.getString(4));				
				dayBook.setOrgId(cursor.getString(6));
				dayBook.setReportingManager(cursor.getString(7));
				dayBook.setIsreturn(cursor.getInt(8)==0?false:true);
				dayBook.setCloseingBal(cursor.getFloat(9));
				dayBook.setRemarks(cursor.getString(10));
				cycle_id = cursor.getString(11);
				cursor.moveToNext();
			}
			cursor.close();
			dayBook.setAmtTobank(getBankDepositAmt(db, userid, saleRefId));
			dayBook.setTotalAllowances(getTotalAllowancesAmt(db,userid,saleRefId));
			dayBook.setTotalpayableFromCust(getTotalSaleAmt(db, userid, cycle_id));
			dayBook.setTotalReceivedFromCust(getTotalReciviedAmt(db, userid, cycle_id));
			// read Products
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
												new String[]{	COL_TRXN_PRODUCT,
																COL_TRXN_BATCH_NO,
																COL_TRXN_QTY,
																COL_TRXN_BONUS_QTY,
																COL_TRXN_REMARK
															},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
												COL_TRXN_FROM 			+ " =? AND " +
												COL_TRXN_TYPE 			+ " =? AND " +
												//COL_TRXN_ISAPPROVED		+ " =1 AND " +
												COL_TRXN_SALE_REF_ID 	+ " =? ",
												new String[]{userid,userid,PRODUCT_TYPE.PRODUCT.getProductCode(),saleRefId},
												null, null,null,null);
			cursor.moveToFirst(); 
			List<DayBookProduct> products  = new ArrayList<DayBookProduct>();
			DayBookProduct product;
			while (!cursor.isAfterLast())
			{
				product = new DayBookProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				product.setReturnFactoryStockQty(cursor.getInt(2));
				//product.setCloseingStockQty(cursor.getInt(3));
				product.setCloseingStockTemp(cursor.getInt(3));
				String tmp = cursor.getString(4);
				String[] strArry = tmp.split("\\|");
				if(strArry.length == 4)
				{
					product.setOpeningStockQty(Integer.parseInt(strArry[0]));
					product.setSalesReturnStockQty(Integer.parseInt(strArry[1]));
					product.setSoldStockQty(Integer.parseInt(strArry[2]));
					product.setCloseingStockQty(Integer.parseInt(strArry[3]));
				}
				else if(strArry.length == 3)
				{
					product.setOpeningStockQty(Integer.parseInt(strArry[0]));
					product.setSalesReturnStockQty(Integer.parseInt(strArry[1]));
					product.setSoldStockQty(Integer.parseInt(strArry[2]));
					product.setCloseingStockQty(cursor.getInt(3));
				}
				else
				{
					product.setOpeningStockQty(0);
					product.setSalesReturnStockQty(0);
					product.setSoldStockQty(0);
					product.setCloseingStockQty(0);
				}
				products.add(product);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();		
			dayBook.setProductsList(products);
			
			// read Payments
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, new String[]{COL_TRXN_AMT},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
												COL_TRXN_FROM 			+ " =? AND " +
												COL_TRXN_TYPE 			+ " =? AND " +
												COL_TRXN_TO 			+ " =? AND " +
												//COL_TRXN_ISAPPROVED		+ " =1 AND " +
												COL_TRXN_SALE_REF_ID 	+ " =? ",
												new String[]{userid,userid,PRODUCT_TYPE.PAYMENT.getProductCode(),Constants.ORG_ID,saleRefId},
												null, null,null,null);
			cursor.moveToFirst(); 
			while (!cursor.isAfterLast())
			{
				dayBook.setAmtToFactory(cursor.getFloat(0));
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();		
			// read opening Bal
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
								new String[]{ 
												COL_TRXN_AMT,
												COL_TRXN_REMARK
											},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
												COL_TRXN_FROM 			+ " =? AND " +
												COL_TRXN_TYPE 			+ " =? AND " +
												COL_TRXN_TO 			+ " =? AND " +
												COL_TRXN_SALE_REF_ID 	+ " =? ",
								new String[]{ 
												userid,
												Constants.ORG_ID,
												PRODUCT_TYPE.CF.getProductCode(),
												userid,
												saleRefId 
											},
											null, null,null,null);
			cursor.moveToFirst();
			String tmp = "";
			float openingBal = 0;
			while (!cursor.isAfterLast())
			{
				openingBal += cursor.getFloat(0);
				tmp 		= cursor.getString(1);
				cursor.moveToNext();
			}
			if(tmp!=null)
			{
				String[] strArry = tmp.split("\\|");
				if(strArry.length == 2)
				{
					dayBook.setTotalpayableFromCust(dayBook.getTotalpayableFromCust() 	- Float.parseFloat(strArry[0]));
					dayBook.setTotalReceivedFromCust(dayBook.getTotalReceivedFromCust() - Float.parseFloat(strArry[1]));
				}	
			}		
			// Make sure to close the cursor
			cursor.close();
			dayBook.setOpeningBal(openingBal);
			List<DayBookAllowances> allowancesList = new ArrayList<DayBookAllowances>();
			// read Allowances
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
								new String[]
										{
											COL_TRXN_PRODUCT,
											COL_TRXN_AMT,
											COL_TRXN_REMARK,
											COL_ROW_ID,
											COL_TRXN_UID
											},
										COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
										COL_TRXN_FROM 			+ " =? AND (" +
										COL_TRXN_TYPE 			+ " =? OR "  +
										COL_TRXN_TYPE 			+ " =? ) AND " +
										COL_TRXN_TO 			+ " =? AND " +
										COL_TRXN_SALE_REF_ID 	+ " =? ",
								new String[]
										{ 
											userid,
											userid,PRODUCT_TYPE.EXP.getProductCode(),
											PRODUCT_TYPE.BANK.getProductCode(),
											Constants.ORG_ID,
											saleRefId 
										},
					null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				DayBookAllowances allowance = new DayBookAllowances();
				allowance.setAllowancesType(cursor.getString(0));
				allowance.setAmt(cursor.getFloat(1));
				allowance.setRemarks(cursor.getString(2));
				allowance.setRowid(cursor.getLong(3));
				String id = cursor.getString(4);
				if(id!=null && id.endsWith("_"))
				{
					allowance.setReserved(id + allowance.getRowid());	
				}
				else if(id ==null || id.isEmpty())
				{
					allowance.setReserved("");	
				}
				else
				{
					allowance.setReserved(id);
				}
				allowancesList.add(allowance);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			dayBook.setAllowancesList(allowancesList);	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return dayBook;
	}
	
	public DayBook readDayBookEntries(SQLiteDatabase db,String userid,String dayId)
	{
		DayBook dayBookEntries = new DayBook();
		try
		{
			dayBookEntries.setSaleExeName(userid);
			
			// read opening Bal
			Cursor cursor = db.query( DBHelper.TBL_SALE_TRXN,
												new String[]{
																COL_TRXN_AMT,
																COL_TRXN_SALE_REF_ID
															},
																COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
																COL_TRXN_FROM 			+ " =? AND " +
																COL_TRXN_TYPE 			+ " =? AND " +
																COL_TRXN_TO 			+ " =? AND " +
																COL_TRXN_DAY_ID 		+ " =? AND " +
																COL_TRXN_SALE_REF_ID	+ " NOT LIKE \'" + Constants.DB + "%\' AND "+
																COL_TRXN_SALE_REF_ID	+ " != \'" + Constants.NA + "\'",
												new String[]{
																userid,
																Constants.ORG_ID,
																PRODUCT_TYPE.CF.getProductCode(),
																userid,
																dayId 
															},null, null,null,null);
			cursor.moveToFirst();
			float openingBal = 0;
			String ref_id="";
			List<DayBookAllowances> allowancesList = new ArrayList<DayBookAllowances>();

			// if Day book Not opened.
			if(cursor.getCount() == 0)
			{
				// Make sure to close the cursor
				cursor.close();
				// check for empty opening Bal Record in Data Base
				cursor = db.query( DBHelper.TBL_SALE_TRXN,
						new String[]{
										COL_TRXN_AMT,
									},
										COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
										COL_TRXN_FROM 			+ " =? AND " +
										COL_TRXN_TYPE 			+ " =? AND " +
										COL_TRXN_TO 			+ " =? AND " +
										COL_TRXN_DAY_ID 		+ " =? AND " +
										COL_TRXN_SALE_REF_ID	+ " = \'" + Constants.NA + "\'",
						new String[]{
										userid,
										Constants.ORG_ID,
										PRODUCT_TYPE.CF.getProductCode(),
										userid,
										dayId 
									},null, null,null,null);
				cursor.moveToFirst();
				if(cursor.getCount() == 0)
				{
					cursor.close();
					return null;
				}
				else
				{
					while (!cursor.isAfterLast())
					{
						openingBal +=	cursor.getFloat(0);
						cursor.moveToNext();
					}
					// Make sure to close the cursor
					cursor.close();
					dayBookEntries.setOpeningBal(openingBal);					
					ref_id = String.valueOf(userid.charAt(0)).toUpperCase() + Utils.getUniqueIDfromDate();
					dayBookEntries.setDayBookid(ref_id);
					insertDayBookEntry(db, dayBookEntries, dayId);
					ContentValues values = new ContentValues();
					// update opening Bal id and all allowance 
					values.put(COL_TRXN_SALE_REF_ID, ref_id);
					db.update(DBHelper.TBL_SALE_TRXN, values, 
										COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
										COL_TRXN_FROM 			+ " =? AND " +
										COL_TRXN_TYPE 			+ " =? AND " +
										COL_TRXN_TO 			+ " =? AND " +
										COL_TRXN_DAY_ID 		+ " =? AND " +
										COL_TRXN_SALE_REF_ID	+ " = \'" + Constants.NA + "\'",
							new String[]{
											userid,
											Constants.ORG_ID,
											PRODUCT_TYPE.CF.getProductCode(),
											userid,
											dayId 
										});
					dayBookEntries.setAllowancesList(allowancesList);
				}
			}
			else // Day book already opened or Allotment Done
			{
				while (!cursor.isAfterLast())
				{
					openingBal +=	cursor.getFloat(0);
					ref_id 		= 	cursor.getString(1);
					cursor.moveToNext();
				}
				// Make sure to close the cursor
				cursor.close();
				dayBookEntries.setOpeningBal(openingBal);
				
				// read all existing Allowances
				cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
											new String[]{
															COL_TRXN_PRODUCT,
															COL_TRXN_AMT,
															COL_TRXN_REMARK,
															COL_ROW_ID,
															COL_TRXN_UID
														},
											COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
											COL_TRXN_FROM 			+ " =? AND ( " +
											COL_TRXN_TYPE 			+ " =? OR "  +
											COL_TRXN_TYPE 			+ " =? ) AND " +
											COL_TRXN_TO 			+ " =? AND " +
											COL_TRXN_DAY_ID 		+ " =? AND " +
											COL_TRXN_SALE_REF_ID	+ " =? ",
											new String[]{ 
															userid,
															userid,
															PRODUCT_TYPE.EXP.getProductCode(),
															PRODUCT_TYPE.BANK.getProductCode(),
															Constants.ORG_ID,
															dayId,
															ref_id
														},null, null,null,null);
				cursor.moveToFirst();
				while (!cursor.isAfterLast())
				{
					DayBookAllowances allowance = new DayBookAllowances();
					allowance.setAllowancesType(cursor.getString(0));
					allowance.setAmt(cursor.getFloat(1));
					allowance.setRemarks(cursor.getString(2));
					allowance.setRowid(cursor.getLong(3));
					String id = cursor.getString(4);
					if(id!=null && id.endsWith("_"))
					{
						allowance.setReserved(id + allowance.getRowid());	
					}
					else if(id ==null || id.isEmpty())
					{
						allowance.setReserved("");	
					}
					else
					{
						allowance.setReserved(id);
					}					
					allowancesList.add(allowance);
					cursor.moveToNext();
				}
				// Make sure to close the cursor
				cursor.close();
				dayBookEntries.setAllowancesList(allowancesList);
				
				// read vehicle Details 
				cursor = db.query(DBHelper.TBL_SALE_HELP,
						new String[]{
										COL_HELP_ID,
										COL_HELP_CHKNO,
										COL_HELP_BANK,
										COL_HELP_BRANCH,
										COL_HELP_BANK_LOC,
										COL_HELP_CREATEON,
										COL_HELP_ORG_ID,
										COL_HELP_REPORT_MGR,
										COL_HELP_ISRETURN
									},
									COL_HELP_CREATEBY + " =? AND " + COL_HELP_INV_NO + " =? ",
						new String[]{
										userid,
										dayId
									},null, null,null,null);
				cursor.moveToFirst();
				while (!cursor.isAfterLast())
				{
					dayBookEntries.setDayBookid(cursor.getString(0));
					dayBookEntries.setVehicleNo(cursor.getString(1));
					dayBookEntries.setDriverName(cursor.getString(2));
					dayBookEntries.setStartReading(cursor.getString(3));
					dayBookEntries.setEndReading(cursor.getString(4));
					dayBookEntries.setCreatedOn(cursor.getString(5));
					dayBookEntries.setOrgId(cursor.getString(6));
					dayBookEntries.setReportingManager(cursor.getString(7));
					dayBookEntries.setIsreturn(cursor.getInt(8)==0?false:true);
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		
/*		if(dayBookEntries.getDayBookid() != null && dayBookEntries.getDayBookid().length() !=0)
		{
			cursor = db.query(DBHelper.TBL_SALE_HELP,
					new String[]{
									COL_DN_CHKNO,
									COL_DN_BANK,
									COL_DN_BRANCH,
									COL_DN_BANK_LOC
								},
								COL_DN_ID + " =? ",
					new String[]{
									dayBookEntries.getDayBookid()
								},null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				dayBookEntries.setVehicleNo(cursor.getString(0));
				dayBookEntries.setDriverName(cursor.getString(1));
				dayBookEntries.setStartReading(cursor.getString(2));
				dayBookEntries.setEndReading(cursor.getString(3));
				cursor.moveToNext();
			}
			cursor.close();
			dayBookEntries.setTotalAllowances(getTotalAllowancesAmt(db,userid,dayId));
		}
		else
		{
			cursor = db.query(DBHelper.TBL_SALE_HELP,
						new String[]{
										COL_DN_CHKNO,
										COL_DN_BANK,
										COL_DN_BRANCH,
										COL_DN_BANK_LOC,
										COL_DN_ID
									},
									COL_DN_INV_NO 	+ 	" =? AND " + 
									COL_DN_ID		+	" LIKE \'" + Constants.DB + "%\'",
						new String[]{
										dayId
									},null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				dayBookEntries.setVehicleNo(cursor.getString(0));
				dayBookEntries.setDriverName(cursor.getString(1));
				dayBookEntries.setStartReading(cursor.getString(2));
				dayBookEntries.setEndReading(cursor.getString(3));
				dayBookEntries.setDayBookid(cursor.getString(4));
				cursor.moveToNext();
			}
			cursor.close();
			if(dayBookEntries.getDayBookid() == null || dayBookEntries.getDayBookid().length() ==0)
				dayBookEntries.setDayBookid(Constants.DB + String.valueOf(userid.charAt(0)).toUpperCase() + Utils.getUniqueIDfromDate());
		}	*/	
		return dayBookEntries;
	}
	
	public List<DayBookProduct> readDayBookproducts(SQLiteDatabase db,String salesId,String dayId)
	{
		List<DayBookProduct> productlist = new ArrayList<DayBookProduct>();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_TRXN,
					new String[]{
									COL_TRXN_PRODUCT,
									COL_TRXN_BATCH_NO,
									COL_TRXN_QTY 
								},
					COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
					COL_TRXN_DAY_ID 		+ " =? AND " + 
					COL_TRXN_FROM 			+ " =? AND " + 
					COL_TRXN_TO 			+ " =? AND " +
					COL_TRXN_TYPE 			+ " =?",
					new String[]{	
									salesId,
									dayId,
									Constants.ORG_ID,
									salesId,
									PRODUCT_TYPE.PRODUCT.getProductCode()
								}, null, null,null,null);
			cursor.moveToFirst();
			DayBookProduct product;
			while (!cursor.isAfterLast())
			{
				product = new DayBookProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				product.setOpeningStockQty(cursor.getInt(2));
				int[] qtyArry = getSoldProductsQty(db,salesId,dayId,product.getProductName(),product.getBatchNumber());
				product.setSoldStockQty(qtyArry[0]);
				product.setSalesReturnStockQty(qtyArry[1]);
				product.setReturnFactoryStockQty(0);
				product.setCloseingStockQty(cursor.getInt(2) + qtyArry[2] - qtyArry[0]);
				product.setCloseingStockTemp(cursor.getInt(2) + qtyArry[2] - qtyArry[0]);
				productlist.add(product);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return productlist;		
	}
	
	public int[] getSoldProductsQty(SQLiteDatabase db,String salesId,String dayId,String productName,String BatchNo)
	{
		int[] soldQty = new int[3];
		try
		{
			Cursor cursor = db.rawQuery("SELECT " +
					"(SELECT COALESCE(SUM("+COL_TRXN_QTY +"),0) + COALESCE(SUM("+COL_TRXN_BONUS_QTY + "),0) FROM SALE_TRXN WHERE " +
						COL_TRXN_SALE_EMP_ID+ "=\'" + salesId 	    + "\' AND " +
						COL_TRXN_FROM 		+"=\'" 	+ salesId 	 	+ "\' AND " +
						COL_TRXN_PRODUCT	+"=\'" 	+ productName 	+ "\' AND " +
						COL_TRXN_BATCH_NO	+"=\'" 	+ BatchNo 	 	+ "\' AND " +
						COL_TRXN_DAY_ID 	+"=\'" 	+ dayId 		+ "\' AND " +
						COL_TRXN_ISAPPROVED	+ "="  	+ 1			 	+ " AND "   +
						COL_TRXN_TYPE 		+"=\'"+ PRODUCT_TYPE.PRODUCT.getProductCode() +"\')" +
				   ",(SELECT COALESCE(SUM("+COL_TRXN_QTY +"),0) + COALESCE(SUM("+COL_TRXN_BONUS_QTY + "),0) FROM SALE_TRXN WHERE " +
				   		COL_TRXN_SALE_EMP_ID+ "=\'" + salesId 	    + "\' AND " +
						COL_TRXN_TO 		+"=\'" 	+ salesId 	 	+ "\' AND " +
						COL_TRXN_FROM 		+"!=\'"	+ Constants.ORG_ID+ "\' AND " +
						COL_TRXN_PRODUCT	+"=\'" 	+ productName 	+ "\' AND " +
						COL_TRXN_BATCH_NO	+"=\'" 	+ BatchNo 	 	+ "\' AND " +
						COL_TRXN_DAY_ID 	+"=\'" 	+ dayId 		+ "\' AND " +
						COL_TRXN_TYPE 		+"=\'" 	+ PRODUCT_TYPE.PRODUCT.getProductCode() +"\')AS A " +
					",(SELECT COALESCE(SUM("+COL_TRXN_QTY +"),0) + COALESCE(SUM("+COL_TRXN_BONUS_QTY + "),0) FROM SALE_TRXN WHERE " +
						COL_TRXN_SALE_EMP_ID+ "=\'" + salesId 	    + "\' AND " +
						COL_TRXN_TO 		+"=\'" 	+ salesId 	 	+ "\' AND " +
						COL_TRXN_FROM 		+"!=\'"	+ Constants.ORG_ID+ "\' AND " +
						COL_TRXN_PRODUCT	+"=\'" 	+ productName 	+ "\' AND " +
						COL_TRXN_BATCH_NO	+"=\'" 	+ BatchNo 	 	+ "\' AND " +
						COL_TRXN_DAY_ID 	+"=\'" 	+ dayId 		+ "\' AND " +
						COL_TRXN_ISAPPROVED	+ "="  	+ 1			 	+ " AND "   +
						COL_TRXN_TYPE 	+"=\'" 		+ PRODUCT_TYPE.PRODUCT.getProductCode() +"\')AS B",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				soldQty[0] = cursor.getInt(0);
				soldQty[1] = cursor.getInt(1);
				soldQty[2] = cursor.getInt(2);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return soldQty;
	}
	
	public int isDayBookClosed(SQLiteDatabase db,String userid,String dayId)
	{
		try
		{
			//Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, 
			//		new String[]{
			//						COL_HELP_ID
			//					},
			//					COL_HELP_CREATEBY + " =? AND " + COL_HELP_INV_NO + " =? ",
			//		new String[]{
			//						userid,
			//						dayId
			//					},null, null,null,null);
			//cursor.moveToFirst();
			//while (!cursor.isAfterLast())
			//{
			//	if(cursor.getString(0).startsWith(Constants.DB))
			//	{
			//		cursor.close();
			//		return 1;
			//	}
			//	cursor.moveToNext();
			//}
			//cursor.close();
			return Utils.getData(VbookApp.getInstance(), userid + Constants.IS_CYCLE_CLOSED, "NO").equalsIgnoreCase("YES")?1:0;
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}

		
		// read opening Bal
		/*cursor = db.query( 	DBHelper.TBL_SALE_TRXN,
													new String[]{ 
																	COL_TRXN_AMT
																},
																	COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
																	COL_TRXN_FROM 			+ " =? AND " +
																	COL_TRXN_TYPE 			+ " =? AND " +
																	COL_TRXN_TO 			+ " =? AND " +
																	COL_TRXN_DAY_ID 		+ " =? ",
													new String[]{ 
																	userid,
																	Constants.ORG_ID,
																	PRODUCT_TYPE.CF.getProductCode(),
																	userid,
																	dayId 
																},null, null,null,null);
		cursor.moveToFirst();
		float openingBal = 0;
		while (!cursor.isAfterLast())
		{
			openingBal+=cursor.getFloat(0);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		
		ContentValues values = new ContentValues();
		values.put(COL_DN_ID, String.valueOf(userid.charAt(0)).toUpperCase() + Utils.getUniqueIDfromDate());
		values.put(COL_DN_ORG_ID, Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, Constants.NA));
		values.put(COL_DN_INV_NO, dayId);
		values.put(COL_DN_CREATEON, AppBaseActivity.dateformat.format(new Date()));
		values.put(COL_BUSINESS,Utils.getData(VbookApp.getInstance(), Constants.ORG_ID, Constants.NA));
		values.put(COL_DN_INV_NAME, Constants.DB + String.valueOf(userid.charAt(0)).toUpperCase() + Utils.getUniqueIDfromDate());
		values.put(COL_DN_CREATEBY, userid);
		values.put(COL_DN_PRV_BALANCE, openingBal);
		values.put(COL_DN_PAYMENT_TYPE, "DAYBOOK"); 
		values.put(COL_DN_CHKNO, "");
		values.put(COL_DN_BANK, "");
		values.put(COL_DN_BRANCH, "");
		values.put(COL_DN_BANK_LOC, "");
		if(db.insert(DBHelper.TBL_SALE_HELP, null, values) == -1)
		{
			// need To handle any Error occurred here ........................
			Log.d("DB", "insertion failed");
		}*/
		return 0;		
	}

	public Journal readJournal(SQLiteDatabase db,String saleRefId,String userid)
	{
		Journal journal = new Journal();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, HELP_ALL, COL_HELP_ID + " =? ",new String[]{saleRefId},null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				journal.setOrganizationId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COL_HELP_ORG_ID))));
				journal.setJournalType(cursor.getString(cursor.getColumnIndex(COL_HELP_PAYMENT_TYPE)));
				journal.setInvoiceNo(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NO)));
				journal.setCreatedOn(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEON)));
				journal.setBusinessName(cursor.getString(cursor.getColumnIndex(COL_HELP_BUSINESS)));
				journal.setInvoiceName(cursor.getString(cursor.getColumnIndex(COL_HELP_INV_NAME)));
				journal.setCreatedBy(cursor.getString(cursor.getColumnIndex(COL_HELP_CREATEBY)));
				cursor.moveToNext();
			}
			cursor.close();
			
			// read Products
			cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
												new String[]{	COL_TRXN_AMT,
																COL_TRXN_REMARK,
																COL_TRXN_ISAPPROVED
															},
												COL_TRXN_SALE_EMP_ID 	+ " =? AND " 	+
												COL_TRXN_FROM 			+ " =? AND " 	+
												COL_TRXN_TYPE 			+ " =? AND " 	+
												COL_TRXN_TO				+ " =? AND " 	+
												COL_TRXN_SALE_REF_ID 	+ " =? ",
												new String[]{userid,journal.getBusinessName(),PRODUCT_TYPE.JOURNAL.getProductCode(),Constants.ORG_ID,saleRefId},
												null, null,null,null);
			cursor.moveToFirst(); 
			while (!cursor.isAfterLast())
			{
				journal.setAmount(cursor.getFloat(0));
				journal.setDescription(cursor.getString(1));
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return journal;
	}
	
	public List<String[]> readPendingIds(SQLiteDatabase db)
	{
		List<String[]> saleRefIds = new ArrayList<String[]>();
		try
		{
			Cursor cursor = db.query(true,DBHelper.TBL_SALE_TRXN, 
					new String[]{
									COL_TRXN_SALE_REF_ID,
									COL_TRXN_SALE_EMP_ID
								},
								COL_TRXN_ISCOMMIT + " =?",
								new String[]{"0"}, null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				String[] tempinfo = new String[2];
				tempinfo[0] = cursor.getString(0);
				tempinfo[1] = cursor.getString(1);
				saleRefIds.add(tempinfo);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return saleRefIds;
	}
	public long insertcustomerBF(SQLiteDatabase db,CustomerAmountInfo customerInfo,String salesId,boolean issalesExecutive,String ref_id)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH		
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_SALE_EMP_ID,salesId);
			values.put(COL_TRXN_DAY_ID, Utils.getData(VbookApp.getInstance(), salesId + Constants.CYCLE_ID,Constants.NA));
			values.put(COL_TRXN_UID,customerInfo.getUid());
			Float amt = customerInfo.getAdvanceAmount();
			if(amt > 0)
			{
				values.put(COL_TRXN_FROM, customerInfo.getBusinessName());
				values.put(COL_TRXN_TO, Constants.ORG_ID);
				amt = customerInfo.getAdvanceAmount();
			}
			else
			{
				values.put(COL_TRXN_FROM, Constants.ORG_ID);
				values.put(COL_TRXN_TO, customerInfo.getBusinessName());			
				amt = customerInfo.getCreditAmount();
			}
			if(issalesExecutive)
			{
				values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.CF.getProductCode());
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.CF.getProductCode());
			}
			else
			{
				values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.BF.getProductCode());
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.BF.getProductCode());
			}		
			values.put(COL_TRXN_BATCH_NO,"");
			values.put(COL_TRXN_QTY, 1);
			values.put(COL_TRXN_BONUS_QTY, 0);
			values.put(COL_TRXN_REMARK, customerInfo.getInvoiceName());
			values.put(COL_TRXN_COST, amt);
			values.put(COL_TRXN_AMT, amt);		 
			values.put(COL_TRXN_FLOW, FLOW.IN.getFlow());
			values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.YES.getCommit());
			values.put(COL_TRXN_SALE_REF_ID, ref_id);
			values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
			
			Cursor cursor = db.query(DBHelper.TBL_SALE_TRXN, 
					new String[]{
									COL_TRXN_SALE_REF_ID
								},
								COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
								COL_TRXN_DAY_ID 	 	+ " =? AND " +
								COL_TRXN_UID 	 		+ " =? AND " +
								COL_TRXN_FROM 	 	+ " =? AND " +
								COL_TRXN_TO 	 + " =? AND " +
								COL_TRXN_PRODUCT 	 + " =? AND " +
								COL_TRXN_TYPE 	 + " =? AND " +
								COL_TRXN_AMT 	 + " =? ",
					new String[]{
								values.getAsString(COL_TRXN_SALE_EMP_ID),
								values.getAsString(COL_TRXN_DAY_ID),
								values.getAsString(COL_TRXN_UID),
								values.getAsString(COL_TRXN_FROM),
								values.getAsString(COL_TRXN_TO),
								values.getAsString(COL_TRXN_PRODUCT),
								values.getAsString(COL_TRXN_TYPE),
								values.getAsString(COL_TRXN_AMT)
					}, null, null,null,null);
			boolean isExists = false;
			if(cursor!=null)
			{
				cursor.moveToFirst();
				while (!cursor.isAfterLast())
				{
					isExists = true;
					cursor.moveToNext();
				}
				// Make sure to close the cursor
				cursor.close();
			}
			if(!isExists)
			{
				if(db.insertWithOnConflict(DBHelper.TBL_SALE_TRXN, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
				{
					// need To handle any Error occurred here ........................
					Log.d("DB", "insertion failed");
					return -1;
				}	
			}
			return 0;	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	public long deassigncustomer(SQLiteDatabase db,String businessNames,String saleEmpId)
	{
		int result = 0;
		try
		{
			//result = db.delete(DBHelper.TBL_SALE_TRXN, 
			//				"(" + COL_TRXN_FROM + " IN (\'"+ businessNames + "\')" +
			//			" OR " + 
			//				COL_TRXN_TO + " IN (\'"+ businessNames + "\')) AND " + COL_TRXN_SALE_EMP_ID + " = \'"+ saleEmpId + "\'", null);
			List<String> businessNamesList = Arrays.asList(businessNames.split(","));
			
			for (int index = 0; index < businessNamesList.size(); index++) 
			{
				result = db.delete(DBHelper.TBL_SALE_TRXN,
						"(" + 
								COL_TRXN_FROM + " = \'" + businessNamesList.get(index) + "\'"  +
						" OR "  +
							  	COL_TRXN_TO   + " = \'" + businessNamesList.get(index) +
					    "\')" 	+
					    " AND "	+ 
					    		COL_TRXN_SALE_EMP_ID + " = \'"+ saleEmpId + "\'"		+
					    " AND "	+ 
					    		COL_TRXN_TYPE 	 	 + " = \'"+ PRODUCT_TYPE.BF.getProductCode() + "\'"
					,null);
			}
	    		
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			result = -1;
		}
		return result == 0?-1:result;
	}
	public long updateBForCF(SQLiteDatabase db,CustomerAmountInfo customerInfo,String salesId,boolean issalesExecutive,String ref_id)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH		
			ContentValues values = new ContentValues();
			Float amt = customerInfo.getAdvanceAmount();
			if(issalesExecutive)
			{
				values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.CF.getProductCode());
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.CF.getProductCode());
			}
			else
			{
				values.put(COL_TRXN_PRODUCT, PRODUCT_TYPE.BF.getProductCode());
				values.put(COL_TRXN_TYPE, PRODUCT_TYPE.BF.getProductCode());
			}
			String dayid 	= Utils.getData(VbookApp.getInstance(), salesId + Constants.CYCLE_ID,Constants.NA);
			// credit or Debit check
			if(amt > 0)
			{
				amt = customerInfo.getAdvanceAmount();
				values.put(COL_TRXN_REMARK, customerInfo.getInvoiceName());
				values.put(COL_TRXN_COST, amt);
				values.put(COL_TRXN_AMT, amt);
				
				int result		= db.update(DBHelper.TBL_SALE_TRXN, values, 
														COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
														COL_TRXN_DAY_ID 		+ " =? AND " +
														COL_TRXN_FROM 			+ " =? AND " +
														COL_TRXN_TO 			+ " =? AND " +
														COL_TRXN_UID 			+ " =? ",
										new String[] {
														salesId,
														dayid,
														customerInfo.getBusinessName(),
														Constants.ORG_ID,
														customerInfo.getUid(),
													});
	
				if(result > 0)
					return 0;
				else
					return insertcustomerBF(db,customerInfo,salesId,issalesExecutive,ref_id);
			}
			else
			{
				amt = customerInfo.getCreditAmount();
				values.put(COL_TRXN_REMARK, customerInfo.getInvoiceName());
				values.put(COL_TRXN_COST, amt);
				values.put(COL_TRXN_AMT, amt);
				
				int result		= db.update(DBHelper.TBL_SALE_TRXN, values, 
														COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
														COL_TRXN_DAY_ID 		+ " =? AND " +
														COL_TRXN_FROM 			+ " =? AND " +
														COL_TRXN_TO 			+ " =? AND " +
														COL_TRXN_UID 			+ " =? ",
										new String[] {
														salesId,
														dayid,
														Constants.ORG_ID,
														customerInfo.getBusinessName(),
														customerInfo.getUid(),
													});
	
				if(result > 0)
					return 0;
				else
					return insertcustomerBF(db,customerInfo,salesId,issalesExecutive,ref_id);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	public long insertproductsQty(SQLiteDatabase db,AllocatedProduct product,String salesId)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH
			product.setProductCost(product.getProductCost()==null?0:product.getProductCost());
			
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_SALE_EMP_ID,salesId);
			values.put(COL_TRXN_DAY_ID, Utils.getData(VbookApp.getInstance(), salesId + Constants.CYCLE_ID,Constants.NA));
			values.put(COL_TRXN_UID,product.getUid());
			values.put(COL_TRXN_FROM,Constants.ORG_ID );
			values.put(COL_TRXN_TO,salesId);
			values.put(COL_TRXN_TYPE, PRODUCT_TYPE.PRODUCT.getProductCode());
			values.put(COL_TRXN_PRODUCT, product.getProductName());
			values.put(COL_TRXN_BATCH_NO,product.getBatchNumber());
			values.put(COL_TRXN_QTY, product.getAvailableQty());
			values.put(COL_TRXN_BONUS_QTY, 0);
			values.put(COL_TRXN_REMARK, "");
			values.put(COL_TRXN_COST, product.getProductCost());
			//values.put(COL_TRXN_COST, 1000);
			values.put(COL_TRXN_AMT, product.getAvailableQty()*product.getProductCost());		 
			values.put(COL_TRXN_FLOW, FLOW.IN.getFlow());
			values.put(COL_TRXN_ISCOMMIT, ISCOMMIT.YES.getCommit());
			values.put(COL_TRXN_SALE_REF_ID, "Link Custmer table");
			values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.YES.getApproved());
			if(db.insert(DBHelper.TBL_SALE_TRXN, null, values) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
				return -1;
			}
			return 0;	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	
	public long updateproductsQty(SQLiteDatabase db,AllocatedProduct product,String salesId)
	{
		try
		{
			// ID | ORG_ID | INV_NO | CREATEON | BUSINESS | INV_NAME | CREATEBY | BALANCE | PAYMENT_TYPE | CHKNO | BANK | BRANCH
			product.setProductCost(product.getProductCost()==null?0:product.getProductCost());
			
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_QTY, product.getAvailableQty());
			values.put(COL_TRXN_COST, product.getProductCost());
			values.put(COL_TRXN_AMT, product.getAvailableQty()*product.getProductCost());
			String dayid = Utils.getData(VbookApp.getInstance(), salesId + Constants.CYCLE_ID,Constants.NA);
			int result = db.update(DBHelper.TBL_SALE_TRXN, values, 
								COL_TRXN_PRODUCT 		+ " =? AND " +
								COL_TRXN_BATCH_NO 		+ " =? AND " +
								COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
								COL_TRXN_FROM 			+ " =? AND " +
								COL_TRXN_TYPE 			+ " =? AND " +
								COL_TRXN_TO 			+ " =? AND " +
								COL_TRXN_DAY_ID 		+ " =? AND " +
								COL_TRXN_UID 			+ " =? ",
								new String[] { 
												product.getProductName(),
												product.getBatchNumber(),
												salesId,
												Constants.ORG_ID,
												PRODUCT_TYPE.PRODUCT.getProductCode(),
												salesId,
												dayid,
												product.getUid()
											});
			
			if(result > 0)
				return 0;
			else
				return insertproductsQty(db,product,salesId);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			return -1;
		}
	}
	

	/*public int changeToCRTrxn(SQLiteDatabase db,String saleRefId,String userId)
	{
		ContentValues values = new ContentValues();
		values.put(COL_DN_ID, saleRefId + Constants.CR);
		int result = db.update(DBHelper.TBL_SALE_HELP, values,COL_DN_ID 	+ " =? AND " + COL_DN_CREATEBY	+ " =?",new String[]{ saleRefId,userId});
		if(result <= 0)
			return 0;
		
		values.clear();
		values.put(COL_TRXN_SALE_REF_ID, saleRefId + Constants.CR);
		//values.put(COL_TRXN_ISAPPROVED, ISAPPROVED.NO.getApproved());
		result =db.update(DBHelper.TBL_SALE_TRXN, values,COL_TRXN_SALE_REF_ID 	+ " =? AND " + COL_TRXN_SALE_EMP_ID	+ " =?",new String[]{ saleRefId,userId});
		if(result <= 0)
			return 0;
		return result;
	}*/
	
	public List<CustomerAmountInfo> readAssiginCustomers(SQLiteDatabase db,String salesId,String dayId)
	{
		List<CustomerAmountInfo> custInfoList = new ArrayList<CustomerAmountInfo>();
		try
		{
			Cursor cursor = db.query(true,DBHelper.TBL_SALE_TRXN, new String[]{COL_TRXN_FROM,COL_TRXN_REMARK,COL_TRXN_UID},
					COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
					COL_TRXN_DAY_ID 		+ " =? AND " + 
					COL_TRXN_TO 			+ " =? AND " + 
					COL_TRXN_PRODUCT 		+ " =?",
					new String[]{	
									salesId,
									dayId,
									Constants.ORG_ID,
									PRODUCT_TYPE.BF.getProductCode()
								}, null, null,null,null);
			cursor.moveToFirst();
			CustomerAmountInfo customer;
			while (!cursor.isAfterLast())
			{
				customer = new CustomerAmountInfo();
				customer.setBusinessName(cursor.getString(0));
				customer.setInvoiceName(cursor.getString(1));
				Float bal = getCustomerBal(db,salesId,dayId,customer.getBusinessName());
				if(bal>= 0)
				{
					customer.setAdvanceAmount(bal);
					customer.setCreditAmount(0f);
				}
				else
				{
					customer.setCreditAmount(-1*bal);
					customer.setAdvanceAmount(0f);
				}
				customer.setUid(cursor.getString(2));
				custInfoList.add(customer);
				cursor.moveToNext();
			}
			cursor.close();
			cursor = db.query(true,DBHelper.TBL_SALE_TRXN, new String[]{COL_TRXN_TO,COL_TRXN_REMARK,COL_TRXN_UID},
					COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
					COL_TRXN_DAY_ID 		+ " =? AND " + 
					COL_TRXN_FROM 			+ " =? AND " + 
					COL_TRXN_PRODUCT 		+ " =?",
					new String[]{	salesId,
									dayId,
									Constants.ORG_ID,
									PRODUCT_TYPE.BF.getProductCode()
								}, null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				customer = new CustomerAmountInfo();
				customer.setBusinessName(cursor.getString(0));
				customer.setInvoiceName(cursor.getString(1));
				Float bal = getCustomerBal(db,salesId,dayId,customer.getBusinessName());
				if(bal>=0)
				{
					customer.setAdvanceAmount(bal);
					customer.setCreditAmount(0f);
				}
				else
				{
					customer.setCreditAmount(-1*bal);
					customer.setAdvanceAmount(0f);
				}
				customer.setUid(cursor.getString(2));
				custInfoList.add(customer);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return custInfoList;
	}
	
	public float getCustomerBal(SQLiteDatabase db,String salesId,String dayId,String businessName)
	{
		//Cursor cursor = VbookApp.db.rawQuery("SELECT (SELECT COALESCE(SUM(AMT),0) FROM SALE_TRXN WHERE TRXN_FROM ='BN A' AND ISAPPROVED = 1 and DAY_ID = '20130826 ') - (SELECT COALESCE(SUM(AMT),0) FROM SALE_TRXN WHERE TRXN_TO ='BN A' AND ISAPPROVED = 1 and DAY_ID = '20130826' ) AS A", null);
		//Cursor cursor = VbookApp.db.query(DBHelper.TBL_SALE_TRXN, 
		//new String[]{"(SELECT COALESCE(SUM(AMT),0) FROM SALE_TRXN WHERE TRXN_FROM =\'"+ businessName + "\' AND ISAPPROVED = 1 and DAY_ID =\'" + dayId +"\')" +
		//		  " - (SELECT COALESCE(SUM(AMT),0) FROM SALE_TRXN WHERE TRXN_TO   =\'"+ businessName + "\' AND ISAPPROVED = 1 and DAY_ID =\'" + dayId +"\') AS A"},
		//		  null,null, null, null,null,null);
		//		+ " = 1 AND " +
		float balance = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT " +
					"(SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
					COL_TRXN_SALE_EMP_ID+ "=\'"+ salesId 	  + "\' AND " +
					COL_TRXN_FROM 		+ "=\'"+ businessName + "\' AND " +
					COL_TRXN_ISAPPROVED + "="  + 1 			  +"	AND " +
					COL_TRXN_DAY_ID		+ "=\'"+ dayId 		   +"\')" +
		  		" - (SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
		  			COL_TRXN_SALE_EMP_ID+ "=\'"+ salesId 	  + "\' AND " +
		  			COL_TRXN_TO 		+ "=\'"+ businessName + "\' AND " +
		  			COL_TRXN_ISAPPROVED + "="  + 1 			  +"	AND " +
		  			COL_TRXN_DAY_ID		+ "=\'"+ dayId 		   +"\') AS A",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				balance = cursor.getFloat(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return balance;
	}
	
	public float getTotalSaleAmt(SQLiteDatabase db,String salesId,String cycle_id)
	{
		float balance = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
					COL_TRXN_SALE_EMP_ID+ "=\'" + salesId 	  	+ "\' AND " +
					COL_TRXN_FROM		+ "=\'" + salesId 		+ "\' AND " +
					COL_TRXN_TYPE 		+ "=\'" + PRODUCT_TYPE.PRODUCT.getProductCode() + "\' AND " +
					COL_TRXN_ISAPPROVED + "="   + 1 			+" AND " +
					COL_TRXN_DAY_ID		+ "=\'" + cycle_id	  	+"\'",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				balance = cursor.getFloat(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return balance;
	}
	
	public float getTotalReciviedAmt(SQLiteDatabase db,String salesId,String cycle_id)
	{
		float balance = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
					COL_TRXN_SALE_EMP_ID+ 	"=\'"	+ salesId 	  	+ "\' AND " +
					COL_TRXN_TO			+ 	"=\'" 	+ salesId 		+ 	"\' AND " +
					COL_TRXN_TYPE 		+	" =\'"	+ PRODUCT_TYPE.PAYMENT.getProductCode() 	+	"\' AND " +
					COL_TRXN_ISAPPROVED + 	"=" 	+ 1 			+" AND " +
					COL_TRXN_DAY_ID		+ 	"=\'"	+ cycle_id	  	+"\'",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				balance = cursor.getFloat(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return balance;
	}	
	public float getBankDepositAmt(SQLiteDatabase db,String salesId,String sales_ref_id)
	{
		float balance = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
					COL_TRXN_SALE_EMP_ID+ 	" =\'" + salesId 	  						+ 	"\' AND " +
					COL_TRXN_FROM		+ 	" =\'" + salesId 							+ 	"\' AND " +
					COL_TRXN_TYPE 		+	" =\'"+ PRODUCT_TYPE.BANK.getProductCode() 	+	"\' AND " +
					//COL_TRXN_ISAPPROVED + "=" + 1 			  +" AND " +
					COL_TRXN_SALE_REF_ID	+ "=\'"	  + sales_ref_id  +"\'",null);
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast())
			{
				balance = cursor.getFloat(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return balance;
	}
	
	public float getTotalAllowancesAmt(SQLiteDatabase db,String salesId,String sales_ref_id)
	{
		float balance = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT COALESCE(SUM(" + COL_TRXN_AMT +"),0) FROM SALE_TRXN WHERE " +
					COL_TRXN_SALE_EMP_ID+ 	" = \'" + salesId 	  						+ 	"\' AND " +
					COL_TRXN_FROM		+ 	" = \'" + salesId 							+ 	"\' AND (" +
					COL_TRXN_TYPE 		+	" = \'" + PRODUCT_TYPE.BANK.getProductCode()+	"\' OR  " +
					COL_TRXN_TYPE 		+	" = \'" + PRODUCT_TYPE.EXP.getProductCode() +	"\' ) AND " +
					//COL_TRXN_ISAPPROVED + "=" + 1 			  +" AND " +
					COL_TRXN_SALE_REF_ID+ "=\'"	  + sales_ref_id 		  +"\'",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				balance = cursor.getFloat(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return balance;
	}
	
	public List<DeliveryNoteProduct> readAllocatedproducts(SQLiteDatabase db,String salesId,String dayId,String custId)
	{
		List<DeliveryNoteProduct> productlist = new ArrayList<DeliveryNoteProduct>();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_TRXN,
					new String[]{
									COL_TRXN_PRODUCT,
									COL_TRXN_BATCH_NO,
									COL_TRXN_COST,
									COL_TRXN_UID
								},
								COL_TRXN_SALE_EMP_ID 	+ " =? AND " +	
								COL_TRXN_DAY_ID 		+ " =? AND " +	
								COL_TRXN_FROM 			+ " =? AND " +
								COL_TRXN_TO 			+ " =? AND " +
								COL_TRXN_TYPE 			+ " =?",
					new String[]{	
									salesId,
									dayId,
									Constants.ORG_ID,
									salesId,
									PRODUCT_TYPE.PRODUCT.getProductCode()
								}, null, null,null,null);
			cursor.moveToFirst();			
			DeliveryNoteProduct product;
			while (!cursor.isAfterLast())
			{
				product = new DeliveryNoteProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				product.setAvailableQty(getAvailableproductsQty(db,salesId,dayId,product.getProductName(),product.getBatchNumber()));
				float cost = getProductCostforCustomer(db,custId,cursor.getString(3));
				product.setProductCost(cost==0?cursor.getFloat(2):cost);
				productlist.add(product);
				cursor.moveToNext();
			}
			cursor.close();	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return productlist;		
	}
	
	public int getAvailableproductsQty(SQLiteDatabase db,String salesId,String dayId,String productName,String BatchNo)
	{
		int availableQty = 0;
		try
		{
			Cursor cursor = db.rawQuery("SELECT " +
					//"(SELECT COALESCE(SUM(" + COL_TRXN_QTY +"),0) + COALESCE(SUM("+COL_TRXN_BONUS_QTY + "),0) FROM SALE_TRXN WHERE " + 	
					"(SELECT COALESCE(SUM(" + COL_TRXN_QTY +"),0) FROM SALE_TRXN WHERE " +
						//COL_TRXN_FROM 	+"=\'"  + Constants.ORG_ID 	+ "\' AND " +
						COL_TRXN_SALE_EMP_ID+" = \'" + salesId 	  		+ "\' AND " +
						COL_TRXN_PRODUCT 	+" = \'" + productName 		+ "\' AND " +
						COL_TRXN_BATCH_NO	+" = \'" + BatchNo 			+ "\' AND " +
						COL_TRXN_ISAPPROVED	+" = "	 + 1 			  	+ " AND " 	+
						COL_TRXN_DAY_ID  	+" = \'" + dayId 			+ "\' AND " +
						COL_TRXN_TO 	 	+" = \'" + salesId 			+ "\')" 	+
		  		" -  (SELECT COALESCE(SUM(" + COL_TRXN_QTY + "),0) + COALESCE(SUM("+COL_TRXN_BONUS_QTY + "),0) FROM SALE_TRXN WHERE " +
		  				COL_TRXN_SALE_EMP_ID+" = \'" + salesId 	  		+ "\' AND " +
		  				COL_TRXN_FROM 		+" = \'" + salesId 		    + "\' AND " +
						COL_TRXN_PRODUCT 	+" = \'" + productName		+ "\' AND " +
						COL_TRXN_BATCH_NO	+" = \'" + BatchNo 			+ "\' AND " +
						COL_TRXN_ISAPPROVED	+" = "+ 1 			  		+	" AND " +
						COL_TRXN_DAY_ID  	+" = \'" + dayId +"\') AS A",null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				availableQty = cursor.getInt(0);
				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return availableQty;
		
	}
	
	public List<SalesReturnProduct> readsoldproducts(SQLiteDatabase db,String salesId,String businessName)
	{
		List<SalesReturnProduct> productlist = new ArrayList<SalesReturnProduct>();
		try
		{
			Cursor cursor = db.query(true,DBHelper.TBL_SALE_TRXN, 
					new String[]{	
									COL_TRXN_PRODUCT,
									COL_TRXN_BATCH_NO,
								},
								COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
								COL_TRXN_FROM 			+ " =? AND " +
								COL_TRXN_TYPE 			+ " =? AND " +
								COL_TRXN_ISAPPROVED+ "="+ 1	+" AND " +
								COL_TRXN_TO 			+ " =? ",
					new String[]{
									salesId,
									salesId,
									PRODUCT_TYPE.PRODUCT.getProductCode(),
									businessName
								},
								null, null,null,null);
			cursor.moveToFirst();
			SalesReturnProduct product;
			while (!cursor.isAfterLast())
			{
				product = new SalesReturnProduct();
				product.setProductName(cursor.getString(0));
				product.setBatchNumber(cursor.getString(1));
				productlist.add(product);
				cursor.moveToNext();
			}
			cursor.close();	
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return productlist;		
	}
	
	public float getProductCostforCustomer(SQLiteDatabase db,String custId,String productId)
	{
		float cost = 0;
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_COSTS, 
					new String[]{
									CustomerDao.COL_COST
								},
					CustomerDao.COL_COST_CUSTID 	+ " =? AND " +
					CustomerDao.COL_COST_PRODUCT_ID + " =? ",
					new String[]{	
									custId,
									productId
								}, null, null,null,null);
				cursor.moveToFirst();
				while (!cursor.isAfterLast())
				{
					cost = cursor.getFloat(0);
					cursor.moveToNext();
				}
				cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return cost;
		
	}
	public int setcommited(SQLiteDatabase db,String saleRefId)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_ISCOMMIT, true);
			return db.update(DBHelper.TBL_SALE_TRXN,values,COL_TRXN_SALE_REF_ID + " = \'" + saleRefId + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return -1;
	}	
	public List<TrxnHistory> getAllReport(SQLiteDatabase db,String sid)
	{
		List<TrxnHistory> trxns = new ArrayList<TrxnHistory>();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, 
							new String[]{
											SalesDao.COL_HELP_ID,
											SalesDao.COL_HELP_INV_NO,
											SalesDao.COL_HELP_INV_NAME,
											SalesDao.COL_HELP_CREATEON,
											SalesDao.COL_HELP_BUSINESS,
											SalesDao.COL_HELP_PRV_BALANCE
										},
										SalesDao.COL_HELP_CREATEBY 	+ " =? AND "+
										"SUBSTR("+ SalesDao.COL_HELP_ID +",3,1) = \'_\'",
										new String[]{
														sid,
													},										
							null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				TrxnHistory trxn=new TrxnHistory();
				trxn.setId(cursor.getString(0));
				trxn.setInvoiceNo(cursor.getString(1));
				trxn.setInvoiceName(cursor.getString(2));
				trxn.setDate(cursor.getString(3));
				if(trxn.getId().startsWith(Constants.DB))
					trxn.setName("");
				else
					trxn.setName(cursor.getString(4));
				
				if(trxn.getId().startsWith(Constants.DN))
				{
					Cursor inCursor = db.query(DBHelper.TBL_SALE_TRXN,
							new String[]{
											"COALESCE(SUM("+COL_TRXN_AMT+"),0)"
										},
										COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
										COL_TRXN_FROM 			+ " =? AND " +
										COL_TRXN_TYPE 			+ " =? AND " +
										COL_TRXN_SALE_REF_ID 	+ " =? ",
							new String[]{
											sid,
											sid,
											PRODUCT_TYPE.PRODUCT.getProductCode(),
											trxn.getId()
										},
							null, null,null,null);
					inCursor.moveToFirst();
					float presentPayment = 0;
					while (!inCursor.isAfterLast())
					{
						presentPayment+=inCursor.getFloat(0);
						inCursor.moveToNext();
					}
					trxn.setBal(String.valueOf(presentPayment));	
				}
				else
				{
					trxn.setBal(String.valueOf(cursor.getFloat(5)));
				}			
				try
				{
					Date date = AppBaseActivity.dateformat.parse(cursor.getString(3));
					if (date != null) 
					{
					    Calendar cal = Calendar.getInstance();
					    cal.setTime(date);
					    trxn.setDateID(cal.get(Calendar.YEAR) + " / "+ String.format("%02d",(cal.get(Calendar.MONTH)+1)) + " / "+String.format("%02d",cal.get(Calendar.DATE))); 
					}
				}
				catch (Exception e)
				{
					String year=cursor.getString(0).substring(5,9);
					String month=cursor.getString(0).substring(9,11);
					String day=cursor.getString(0).substring(11,13);
					trxn.setDateID(year +" / "+month+" / "+day);
				}
				trxns.add(trxn);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		return trxns;
	}
	
	public List<TrxnHistory> getAllReportsToday(SQLiteDatabase db,String sid,String dayId )
	{
		List<TrxnHistory> trxns = new ArrayList<TrxnHistory>();
		
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, 
					new String[]{
									SalesDao.COL_HELP_ID,
									SalesDao.COL_HELP_INV_NO,
									SalesDao.COL_HELP_INV_NAME,
									SalesDao.COL_HELP_CREATEON,
									SalesDao.COL_HELP_BUSINESS,
									SalesDao.COL_HELP_PRV_BALANCE
								},
								SalesDao.COL_HELP_CREATEBY 	+ " =? AND "+
								SalesDao.COL_HELP_DAY_ID 	+ " =? AND "+
								SalesDao.COL_HELP_ISCLOSED 	+ " !=1 AND "+
								"SUBSTR("+ SalesDao.COL_HELP_ID +",3,1) = \'_\'",
					new String[]{
									sid,
									dayId
								},								
					null, null, null, null);
			
			cursor.moveToFirst();
			Date date;
			while (!cursor.isAfterLast())
			{
				TrxnHistory trxn=new TrxnHistory();
				trxn.setId(cursor.getString(0));
				trxn.setInvoiceNo(cursor.getString(1));
				trxn.setInvoiceName(cursor.getString(2));
				trxn.setDate(cursor.getString(3));
				if(trxn.getId().startsWith(Constants.DB))
					trxn.setName("");
				else
					trxn.setName(cursor.getString(4));
				
				if(trxn.getId().startsWith(Constants.DN))
				{
					Cursor inCursor = db.query(DBHelper.TBL_SALE_TRXN,
							new String[]{
											"COALESCE(SUM("+COL_TRXN_AMT+"),0)"
										},
										COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
										COL_TRXN_FROM 			+ " =? AND " +
										COL_TRXN_TYPE 			+ " =? AND " +
										COL_TRXN_SALE_REF_ID 	+ " =? ",
							new String[]{
											sid,
											sid,
											PRODUCT_TYPE.PRODUCT.getProductCode(),
											trxn.getId()
										},
										null, null,null,null);
					inCursor.moveToFirst();
					float presentPayment = 0;
					while (!inCursor.isAfterLast())
					{
						presentPayment+=inCursor.getFloat(0);
						inCursor.moveToNext();
					}
					trxn.setBal(String.valueOf(presentPayment));
				}
				else
				{
					trxn.setBal(String.valueOf(cursor.getFloat(5)));
				}				
				try
				{
					date = AppBaseActivity.dateformat.parse(cursor.getString(3));
					if (date != null) 
					{
					    Calendar cal = Calendar.getInstance();
					    cal.setTime(date);
					    trxn.setDateID(cal.get(Calendar.YEAR) + " / "+ String.format("%02d",(cal.get(Calendar.MONTH)+1)) + " / "+String.format("%02d",cal.get(Calendar.DATE))); 
					}
				}
				catch (Exception e)
				{
					String year=cursor.getString(0).substring(5,9);
					String month=cursor.getString(0).substring(9,11);
					String day=cursor.getString(0).substring(11,13);
					trxn.setDateID(year +" / "+month+" / "+day);	
				}
				trxns.add(trxn);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		} catch (Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		return trxns;
	}
	
	public List<String[]> getBnamesforOffLoadingCharges(SQLiteDatabase db,String userID, String dayId)
	{
		List<String[]>  businessNames = new ArrayList<String[]>();
		try
		{
			
			Cursor cursor = db.query(DBHelper.TBL_SALE_HELP, 
					new String[]{
									SalesDao.COL_HELP_BUSINESS,
									"COUNT(*)"
								},
								SalesDao.COL_HELP_DAY_ID +	" =? AND "+
								SalesDao.COL_HELP_ID 	 +	" LIKE \'DN_%\'",
					new String[]{
									dayId
								},								
					SalesDao.COL_HELP_BUSINESS, null, null, null);
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				String[] tempinfo = new String[2];
				tempinfo[0] = cursor.getString(0);
				tempinfo[1] = cursor.getString(1);
				businessNames.add(tempinfo);				
				cursor.moveToNext();
			}
			//
			// Make sure to close the cursor
			cursor.close();
			return businessNames;
		}catch(Exception e){
			Log.d(LOG_TAG, e.getMessage());
		}
		return businessNames;
	}
	
	public long getleatestMeteReading(SQLiteDatabase db,String userID, String dayBookid, String exptype) 
	{
		long leatestMeteReading = 0;
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_TRXN, 
					new String[]{
									"MAX(" + SalesDao.COL_TRXN_REMARK + ")",
								},
								COL_TRXN_TYPE		+ " =? AND " +
								COL_TRXN_PRODUCT 	+ " =? AND " +
								COL_TRXN_DAY_ID     + " =? AND " +
								COL_TRXN_SALE_EMP_ID+ " =? ",
					new String[]{
									PRODUCT_TYPE.EXP.getProductCode(),
									exptype,
									dayBookid,
									userID
								},								
					null, null, null, null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				leatestMeteReading += cursor.getLong(0);
				cursor.moveToNext();
			}
			cursor.close();
			return leatestMeteReading;
		}
		catch(Exception e){
			Log.d(LOG_TAG, e.getMessage());
			
		}
		return leatestMeteReading;
	}
	
	public long trxnApproved(SQLiteDatabase db,String saleRefId)
	{
		int result = 0;
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_ISAPPROVED, true);		
			result = db.update(DBHelper.TBL_SALE_TRXN,values,COL_TRXN_SALE_REF_ID + " = \'" + saleRefId + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			result = -1;
		}
		return result == 0?-1:result;
	}
	public long trxnDeclined(SQLiteDatabase db,String saleRefId)
	{
		int result = 0;
		try
		{
			result = db.delete(DBHelper.TBL_SALE_TRXN,COL_TRXN_SALE_REF_ID + " = \'" + saleRefId + "\'", null);			
			result = db.delete(DBHelper.TBL_SALE_HELP,COL_HELP_ID + " = \'" + saleRefId + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			result = -1;
		}
		return result == 0?-1:result;
	}
	public long changeRequestApproved(SQLiteDatabase db,String new_saleRefId,String old_saleRefId)
	{
		int result = 0;

		try
		{
			// delete all old transactions 
			db.delete(DBHelper.TBL_SALE_TRXN,COL_TRXN_SALE_REF_ID + " = \'" + old_saleRefId + "\'", null);
			db.delete(DBHelper.TBL_SALE_HELP,COL_HELP_ID + " = \'" + old_saleRefId + "\'", null);
			
			ContentValues values = new ContentValues();
			values.put(COL_TRXN_ISAPPROVED, true);
			values.put(COL_TRXN_SALE_REF_ID, old_saleRefId);
			result = db.update(DBHelper.TBL_SALE_TRXN,values,COL_TRXN_SALE_REF_ID + " = \'" + new_saleRefId + "\'", null);
			values.clear();
			values.put(COL_HELP_ID, old_saleRefId);
			result = db.update(DBHelper.TBL_SALE_HELP,values,COL_HELP_ID + " = \'" + new_saleRefId + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			result = -1;
		}
		return result == 0?-1:result;
	}
	public long changeRequestDeclined(SQLiteDatabase db,String new_saleRefId)
	{
		int result = 0;

		try
		{
			result = db.delete(DBHelper.TBL_SALE_TRXN,COL_TRXN_SALE_REF_ID + " = \'" + new_saleRefId + "\'", null);
			result = db.delete(DBHelper.TBL_SALE_HELP,COL_HELP_ID + " = \'" + new_saleRefId + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return result == 0?-1:result;
	}
	public long getOffloadingCount(SQLiteDatabase db,String userID, String dayBookid, String exptype,String remark) 
	{
		long offloadingCount = 0;
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_SALE_TRXN, 
					new String[]{
									"COUNT(" + SalesDao.COL_TRXN_REMARK + ")",
								},
								COL_TRXN_TYPE		+ " =? AND " +
								COL_TRXN_PRODUCT 	+ " =? AND " +
								COL_TRXN_DAY_ID     + " =? AND " +
								COL_TRXN_SALE_EMP_ID+ " =?  AND " +
								COL_TRXN_REMARK     +"=?",
					new String[]{
									PRODUCT_TYPE.EXP.getProductCode(),
									exptype,
									dayBookid,
									userID,
									remark
								},								
					null, null, null, null);
			
			cursor.moveToFirst();
			while(!cursor.isAfterLast())
			{
				offloadingCount += cursor.getLong(0);
				cursor.moveToNext();
			}
			cursor.close();
			return offloadingCount;
		}
		catch(Exception e){
			Log.d(LOG_TAG, e.getMessage());
			
		}
		return offloadingCount;
	}
	public float[] getPreviousBusinessAmts(SQLiteDatabase db,String salesId, String dayBookid)
	{
		float[] result = new float[2];
		try
		{
			Cursor cursor = db.query( 	DBHelper.TBL_SALE_TRXN, 
								new String[]
								{
									COL_TRXN_REMARK
								},
									COL_TRXN_SALE_EMP_ID 	+ " =? AND " +
									COL_TRXN_TYPE 			+ " =? AND " +
									COL_TRXN_SALE_REF_ID 	+ " =? ",
								new String[]
								{
									salesId,
									PRODUCT_TYPE.CF.getProductCode(),
									dayBookid
								},
								null, null,null,null);
			cursor.moveToFirst();
			String tmp = "";
			while (!cursor.isAfterLast())
			{
				tmp = cursor.getString(0);
				cursor.moveToNext();
			}
			if(tmp!=null)
			{
				String[] strArry = tmp.split("\\|");
				if(strArry.length == 2)
				{
					result[0] = Float.parseFloat(strArry[0]); 
					result[1] = Float.parseFloat(strArry[1]);
				}	
				else
				{
					result[0] = 0; 
					result[1] = 0;
				}
			}		
			else
			{
				result[0] = 0; 
				result[1] = 0;
			}
			return result;
		}
		catch(Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		result[0] = 0;
		result[1] = 0;
		return result;
	}
	
	public int markAlltrxns(SQLiteDatabase db,String salesId,String dayid,String dayBookid)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_HELP_ISCLOSED, 1);
			return db.update(DBHelper.TBL_SALE_HELP, values, 
											COL_HELP_CREATEBY 	+ " =? AND " +
											COL_HELP_DAY_ID 	+ " =? AND " +
											COL_HELP_ID			+ "!= ? ",
								new String[]{ 
												salesId,
												dayid,
												Constants.DB + dayBookid
											});
		}
		catch(Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
		return -1;
	}
}
