/**
 * 
 */
package com.vekomy.vbooks.database;

import java.util.ArrayList;
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
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.app.response.CustomerInfo;
import com.vekomy.vbooks.app.response.CustomerProductsCost;
import com.vekomy.vbooks.utils.Utils;


/**
 * @author koteswararao
 * 
 */
public class CustomerDao
{
	private static String LOG_TAG = "CustomerDao";
	
	//public final static String COL_CUST_ID 				= "ID";
	public final static String COL_CUST_UID 			= "UID";
	public final static String COL_CUST_SALE_EMP_ID 	= "SID";
	public final static String COL_CUST_BUSINESS_NAME 	= "BUSINESS_NAME";
	public final static String COL_CUST_GENDER 			= "GENDER";
	public final static String COL_CUST_NAME 			= "NAME";
	public final static String COL_CUST_INV_NAME 		= "INV_NAME";	
	public final static String COL_CUST_ADDRESS_LINE1 	= "ADDRESS_LINE1";
	public final static String COL_CUST_ADDRESS_LINE2 	= "ADDRESS_LINE2";
	public final static String COL_CUST_REGION 			= "REGION";
	public final static String COL_CUST_LOCALITY		= "LOCALITY";
	public final static String COL_CUST_LANDMARK		= "LANDMARK";
	public final static String COL_CUST_CITY			= "CITY";
	public final static String COL_CUST_STATE			= "STATE";
	public final static String COL_CUST_ZIPCODE			= "ZIPCODE";
	public final static String COL_CUST_MOBILE			= "MOBILE";
	public final static String COL_CUST_ALTMOBILE		= "ALTMOBILE";
	public final static String COL_CUST_EMAIL			= "EMAIL";
	public final static String COL_CUST_DIRECT_LINE		= "DIRECT_LINE";
	public final static String COL_CUST_ISNEW 			= "ISNEW";
	public final static String COL_CUST_ISAPPROVED 		= "ISAPPROVED"; // approved
	public final static String COL_CUST_ISCOMMIT		= "ISCOMMIT";
	
	public final static String COL_COST_CUSTID 		= "CUSTID";
	public final static String COL_COST_PRODUCT_ID 	= "PRODUCT_ID";
	public final static String COL_COST 			= "COST";
	
	private String[] CUST_ALL =	{
										//COL_CUST_ID,
										COL_CUST_UID,
										COL_CUST_SALE_EMP_ID,
										COL_CUST_BUSINESS_NAME,
										COL_CUST_GENDER,
										COL_CUST_NAME,
										COL_CUST_INV_NAME,
										COL_CUST_ADDRESS_LINE1,
										COL_CUST_ADDRESS_LINE2,
										COL_CUST_REGION,
										COL_CUST_LOCALITY,
										COL_CUST_LANDMARK, 
										COL_CUST_CITY,
										COL_CUST_STATE,
										COL_CUST_ZIPCODE,
										COL_CUST_MOBILE,
										COL_CUST_ALTMOBILE,
										COL_CUST_EMAIL,
										COL_CUST_DIRECT_LINE,
										COL_CUST_ISNEW,
										COL_CUST_ISAPPROVED
								};
	
	public CustomerDao()
	{
		
	}
	public CustomerDao(Context context)
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
	public List<String> getNewlyAssignedCustomers(SQLiteDatabase db,List<CustomerAmountInfo> custmourList)
	{
		List<String> assiginBusinessNames = new ArrayList<String>();
		try
		{
			String setOfBusinessString="";
			int i = 0;
			for (i = 0; i < custmourList.size()-1; i++) {
				setOfBusinessString += "\'" + custmourList.get(i).getBusinessName() + "\'" + ",";
				assiginBusinessNames.add(custmourList.get(i).getBusinessName());
			}
			setOfBusinessString += "\'" + custmourList.get(i).getBusinessName() + "\'";
			assiginBusinessNames.add(custmourList.get(i).getBusinessName());
			
			Cursor cursor = db.query( true,	DBHelper.TBL_CUSTOMER, 
					new String[]{	
									COL_CUST_BUSINESS_NAME
								},
								COL_CUST_BUSINESS_NAME  	+ " IN ("+ setOfBusinessString +")",
					null,null, null,null,null);
			cursor.moveToFirst();
			List<String> result = new ArrayList<String>();
			while (!cursor.isAfterLast())
			{
				result.add(cursor.getString(0));
				cursor.moveToNext();
			}
			for (int j = 0; j < result.size(); j++) {
				if(assiginBusinessNames.contains(result.get(j)))
					assiginBusinessNames.remove(result.get(j));
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return assiginBusinessNames;
	}
	public int insertOrUpdateCustomer(SQLiteDatabase db,String salesId,CustomerInfo customer,boolean isSystem)
	{
		try
		{
			ContentValues values = new ContentValues();
			//values.put(COL_CUST_ID,customer.getBusinessName()==null?"":customer.getBusinessName());
			values.put(COL_CUST_UID,customer.getRefID());
			values.put(COL_CUST_SALE_EMP_ID,salesId);
			values.put(COL_CUST_BUSINESS_NAME,customer.getBusinessName()==null?"":customer.getBusinessName());
			values.put(COL_CUST_GENDER,String.valueOf(customer.getGender()==null?'M':customer.getGender()));
			values.put(COL_CUST_NAME,customer.getCustomerName()==null?"":customer.getCustomerName());
			values.put(COL_CUST_INV_NAME,customer.getInvoiceName()==null?"":customer.getInvoiceName());
			values.put(COL_CUST_ADDRESS_LINE1,customer.getAddressLine1()==null?"":customer.getAddressLine1());
			values.put(COL_CUST_ADDRESS_LINE2,customer.getAddressLine2()==null?"":customer.getAddressLine2());
			values.put(COL_CUST_REGION,customer.getRegion()==null?"":customer.getRegion());
			values.put(COL_CUST_LOCALITY,customer.getLocality()==null?"":customer.getLocality());
			values.put(COL_CUST_LANDMARK, customer.getLandmark()==null?"":customer.getLandmark());
			values.put(COL_CUST_CITY,customer.getCity()==null?"":customer.getCity());
			values.put(COL_CUST_STATE,customer.getState()==null?"":customer.getState());
			values.put(COL_CUST_ZIPCODE,customer.getZipcode()==null?"":customer.getZipcode());
			values.put(COL_CUST_MOBILE,customer.getMobile()==null?"":customer.getMobile());
			values.put(COL_CUST_ALTMOBILE,customer.getAlternateMobile()==null?"":customer.getAlternateMobile());
			values.put(COL_CUST_EMAIL,customer.getEmail()==null?"":customer.getEmail());
			values.put(COL_CUST_DIRECT_LINE,customer.getDirectLine()==null?"":customer.getDirectLine());		
			if(isSystem)
			{
				//values.put(COL_CUST_ID,customer.getBusinessName()==null?"":customer.getBusinessName());
				values.put(COL_CUST_ISNEW,0);
				values.put(COL_CUST_ISAPPROVED,1);
				values.put(COL_CUST_ISCOMMIT,1);
			}
			else
			{
				values.put(COL_CUST_ISNEW,customer.getCrType()?0:1);
				values.put(COL_CUST_ISAPPROVED,0);
				values.put(COL_CUST_ISCOMMIT,0);
			}
			if(db.insertWithOnConflict(DBHelper.TBL_CUSTOMER, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
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
	
	public CustomerInfo readCustomer(SQLiteDatabase db,String businessName)
	{
		CustomerInfo customer = null;
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_CUSTOMER,CUST_ALL,
					//COL_CUST_ISAPPROVED + " = 1 AND "+
					COL_CUST_BUSINESS_NAME  	+ " =?",
					new String[]{	
									businessName
								} 
								,null, null,null,null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				customer = readDatafromCursor(cursor);
				cursor.moveToNext();
			}
			// 	Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return customer;
	}
	
	private CustomerInfo readDatafromCursor(Cursor cursor)
	{
		CustomerInfo customer = new CustomerInfo();
		try
		{
			customer.setBusinessName(cursor.getString(cursor.getColumnIndex(COL_CUST_BUSINESS_NAME)));
			customer.setGender(cursor.getString(cursor.getColumnIndex(COL_CUST_GENDER)).charAt(0));
			customer.setCustomerName(cursor.getString(cursor.getColumnIndex(COL_CUST_NAME)));
			customer.setInvoiceName(cursor.getString(cursor.getColumnIndex(COL_CUST_INV_NAME)));
			customer.setAddressLine1(cursor.getString(cursor.getColumnIndex(COL_CUST_ADDRESS_LINE1)));
			customer.setAddressLine2(cursor.getString(cursor.getColumnIndex(COL_CUST_ADDRESS_LINE2)));
			customer.setRegion(cursor.getString(cursor.getColumnIndex(COL_CUST_REGION)));
			customer.setLocality(cursor.getString(cursor.getColumnIndex(COL_CUST_LOCALITY)));
			customer.setLandmark(cursor.getString(cursor.getColumnIndex(COL_CUST_LANDMARK)));
			customer.setCity(cursor.getString(cursor.getColumnIndex(COL_CUST_CITY)));
			customer.setState(cursor.getString(cursor.getColumnIndex(COL_CUST_STATE)));
			customer.setZipcode(cursor.getString(cursor.getColumnIndex(COL_CUST_ZIPCODE)));
			customer.setMobile(cursor.getString(cursor.getColumnIndex(COL_CUST_MOBILE)));
			customer.setAlternateMobile(cursor.getString(cursor.getColumnIndex(COL_CUST_ALTMOBILE)));
			customer.setEmail(cursor.getString(cursor.getColumnIndex(COL_CUST_EMAIL)));
			customer.setDirectLine(cursor.getString(cursor.getColumnIndex(COL_CUST_DIRECT_LINE)));
			customer.setCrType(cursor.getInt(cursor.getColumnIndex(COL_CUST_ISNEW))==1?false:true);
			customer.setIsApproved(cursor.getInt(cursor.getColumnIndex(COL_CUST_ISAPPROVED))==1?true:false);
			customer.setRefID(cursor.getString(cursor.getColumnIndex(COL_CUST_UID)));
		}
		catch(Exception e){}
		return customer;
	}
	public List<CustomerInfo> readCustomerChangeInfo(SQLiteDatabase db,String orgId,String saleExeName)
	{
		List<CustomerInfo> custInfoList = new ArrayList<CustomerInfo>();
		try
		{
			Cursor cursor = db.query(DBHelper.TBL_CUSTOMER,CUST_ALL,
					COL_CUST_ISAPPROVED + 	" = 0 AND " +
					COL_CUST_ISCOMMIT   +	" = 0 AND "  + 
					COL_CUST_UID 		+	" LIKE \'%"+ Constants.CR +"\'",
					null, null,null,null);
			cursor.moveToFirst();
			CustomerInfo customer;
			while (!cursor.isAfterLast())
			{
				customer = readDatafromCursor(cursor);
				customer.setOrganizationId(Integer.parseInt(orgId));
				customer.setSalesExecutive(saleExeName);
				custInfoList.add(customer);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return custInfoList;
	}
	/*public long insertOrUpdateProduts(SQLiteDatabase db,String salesId,CustomerBasicInfoResponse customer)
	{
		ContentValues values = new ContentValues();
		values.put(COL_ID, customer.getUid());
		values.put(COL_SALE_EMP_ID, salesId);
		values.put(COL_BUSINESS_NAME, customer.getBusinessName());
		values.put(COL_INV_NAME, customer.getInvoiceName());
		if(db.insertWithOnConflict(DBHelper.TBL_CUSTOMER, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
		{
			// need To handle any Error occurred here ........................
			Log.d("DB", "insertion failed");
		}
		return 0;
	}*/
	/*
	 * 
	 */
	public int insertCustomerProductCosts(SQLiteDatabase db,List<CustomerProductsCost> custproductsCostList)
	{
		try
		{
			for (int i = 0; i < custproductsCostList.size(); i++) 
			{			
				if(insertOrUpdateCustomerCost(db,custproductsCostList.get(i)) == -1)
				{
					// Error Recevery Here ............
					Log.d("DB", "insertion failed");
				}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return 0;
	}
	
	public long insertOrUpdateCustomerCost(SQLiteDatabase db,CustomerProductsCost customerProductsCost)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_COST_CUSTID, customerProductsCost.getCustId());
			values.put(COL_COST_PRODUCT_ID, customerProductsCost.getProductId());
			values.put(COL_COST, customerProductsCost.getCost());
			if(db.insertWithOnConflict(DBHelper.TBL_COSTS, null, values,SQLiteDatabase.CONFLICT_REPLACE) == -1)
			{
				// need To handle any Error occurred here ........................
				Log.d("DB", "insertion failed");
			}			
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return 0;
	}
	public int setcommited(SQLiteDatabase db,String businessName)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_CUST_ISCOMMIT, true);		
			return db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_BUSINESS_NAME + " = \'" + businessName + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return -1;
	}
	
	public int setunApproved(SQLiteDatabase db,String id)
	{
		try
		{
			ContentValues values = new ContentValues();
			values.put(COL_CUST_ISAPPROVED, 0);		
			return db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_UID + " = \'" + id + "\'", null);
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return -1;
	}
	
	public long changeRequestApproved(SQLiteDatabase db,String new_saleRefId,String old_saleRefId,String bName)
	{
		int result = 0;
		try
		{
			db.delete(DBHelper.TBL_CUSTOMER,COL_CUST_UID + " = \'" + old_saleRefId + "\'", null);
			ContentValues values = new ContentValues();
			values.put(COL_CUST_ISAPPROVED, 1);
			values.put(COL_CUST_UID, old_saleRefId);
			// values.put(COL_CUST_ID, bName);
			result = db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_UID + " = \'" + new_saleRefId + "\'", null);
			if(result==0) // for new customer we will get new unique Id
			{
				result = db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_BUSINESS_NAME + " = \'" + bName + "\'", null);
				if(result==1)
				{
					CustomerInfo customerInfo = readCustomer(db,bName);
					if(customerInfo!=null)
					{
						CustomerAmountInfo customer = new CustomerAmountInfo();
						customer.setUid(old_saleRefId);
						customer.setBusinessName(bName);
						customer.setInvoiceName(customerInfo.getInvoiceName());
						customer.setAdvanceAmount(0f);
						customer.setCreditAmount(0f);
						String saleEmpId 	= Utils.getData(VbookApp.getInstance(), Constants.USER_ID, Constants.NA);
						new SalesDao().insertcustomerBF(VbookApp.getDbforServices(),customer,saleEmpId,false,"");	
					}
				}
			}
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
		return result==0?-1:result;
	}
	public long changeRequestDeclined(SQLiteDatabase db,String new_saleRefId,String old_saleRefId,String bName)
	{
		int result = 0;
		try
		{
			result = db.delete(DBHelper.TBL_CUSTOMER,COL_CUST_UID + " = \'" + new_saleRefId + "\'", null);
			ContentValues values = new ContentValues();
			values.put(COL_CUST_ISAPPROVED, 1);
			result = db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_UID + " = \'" + old_saleRefId + "\'", null);
			if(result==0) // for new customer we will get new unique Id
			{
				result = db.update(DBHelper.TBL_CUSTOMER,values,COL_CUST_BUSINESS_NAME + " = \'" + bName + "\'", null);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return result==0?-1:result;
	}
}
