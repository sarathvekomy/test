package com.vekomy.vbooks.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
	private static String LOG_TAG = "DBHelper";

	private Context context;

	public final static int CURRENT_DATABASE_VERSION 	= 1;
	public static final String DATABASE_NAME 			= "vBook_V1.sqlite";


	public final static String TBL_SALE_TRXN 			= "SALE_TRXN";
	
	public final static String TBL_SALE_HELP			= "SALE_HELP";
	
	public final static String TBL_SALE_DB 				= "SALE_DB";
	
	public final static String TBL_CUSTOMER 			= "CUSTOMER";
	
	public final static String TBL_PRODUCTS 			= "PRODUCTS";
	
	public final static String TBL_COSTS 				= "COSTS";
	
	public final static String TBL_BILL_IMGS 			= "BILL_IMGS";
 
	private static final String CREATE_SALE_TRXN		= "CREATE TABLE IF NOT EXISTS " + TBL_SALE_TRXN + " (" +
			SalesDao.COL_TRXN_SALE_EMP_ID 	+ 	" TEXT NOT NULL," +
			SalesDao.COL_TRXN_DAY_ID		+	" VARCHAR(24) NOT NULL,"+
			SalesDao.COL_TRXN_UID			+	" VARCHAR(48),"+
			SalesDao.COL_TRXN_FROM			+ 	" TEXT NOT NULL," +
			SalesDao.COL_TRXN_TYPE			+	" TEXT NOT NULL," +
			SalesDao.COL_TRXN_PRODUCT		+	" TEXT NOT NULL," +
			SalesDao.COL_TRXN_BATCH_NO		+	" TEXT," +
			SalesDao.COL_TRXN_QTY			+	" INT DEFAULT 0," +
			SalesDao.COL_TRXN_BONUS_QTY		+	" INT DEFAULT 0," +
			SalesDao.COL_TRXN_REMARK		+	" TEXT DEFAULT ''," +
			SalesDao.COL_TRXN_COST			+	" FLOAT DEFAULT 0," +
			SalesDao.COL_TRXN_AMT			+	" FLOAT NOT NULL," +
			SalesDao.COL_TRXN_TO			+	" TEXT NOT NULL," +
			SalesDao.COL_TRXN_FLOW			+	" BOOLEAN DEFAULT 0," +
			SalesDao.COL_TRXN_ISCOMMIT		+	" BOOLEAN DEFAULT 0," +
			SalesDao.COL_TRXN_SALE_REF_ID	+	" VARCHAR(255) NOT NULL,"+//FOREIGN KEY(COL_TRXN_SALE_REF_ID) REFERENCES TBL_SALE_DN(COL_DN_ID)"+
			SalesDao.COL_TRXN_ISAPPROVED	+	" BOOLEAN DEFAULT 0 )";
			//SalesDao.COL_TRXN_CREATED_ON	+	" VARCHAR(48) NOT NULL)";

	private static final String CREATE_SALE_HELP	= "CREATE TABLE IF NOT EXISTS " + TBL_SALE_HELP + " ("	+
			SalesDao.COL_HELP_ID 				+ 	" VARCHAR(255) PRIMARY KEY," +
			SalesDao.COL_HELP_DAY_ID			+	" VARCHAR(24) NOT NULL,"+
			SalesDao.COL_HELP_ORG_ID			+	" VARCHAR(24) NOT NULL,"+
			SalesDao.COL_HELP_INV_NO			+ 	" TEXT NOT NULL," +
			SalesDao.COL_HELP_CREATEON			+	" TEXT NOT NULL," +
			SalesDao.COL_HELP_BUSINESS			+	" TEXT NOT NULL," +
			SalesDao.COL_HELP_INV_NAME			+	" TEXT NOT NULL," +
			SalesDao.COL_HELP_CREATEBY			+	" TEXT NOT NULL," +
			SalesDao.COL_HELP_PRV_BALANCE		+	" FLOAT NOT NULL," +
			SalesDao.COL_HELP_PAYMENT_TYPE		+	" VARCHAR(24)NOT NULL," +
			SalesDao.COL_HELP_CHKNO				+	" TEXT DEFAULT ''," +
			SalesDao.COL_HELP_BANK				+	" TEXT DEFAULT '',"+
			SalesDao.COL_HELP_BANK_LOC			+	" TEXT DEFAULT '',"+
			SalesDao.COL_HELP_BRANCH			+	" TEXT DEFAULT ''," +
			SalesDao.COL_HELP_REPORT_MGR		+	" TEXT DEFAULT ''," +
			SalesDao.COL_HELP_DESCRIPTION		+	" TEXT DEFAULT ''," + // 			description
			SalesDao.COL_HELP_ISRETURN			+	" BOOLEAN DEFAULT 0,"+ 
			SalesDao.COL_HELP_ISCLOSED			+	" BOOLEAN DEFAULT 0 )";
		
	private static final String CREATE_CUSTOMER	= "CREATE TABLE IF NOT EXISTS " + TBL_CUSTOMER 	+ " ("	+
			//CustomerDao.COL_CUST_ID 			+ 	" VARCHAR(255) PRIMARY KEY," +			
			CustomerDao.COL_CUST_UID			+	" VARCHAR(48)  PRIMARY KEY," +
			CustomerDao.COL_CUST_SALE_EMP_ID	+	" TEXT NOT NULL,"+
			CustomerDao.COL_CUST_BUSINESS_NAME	+	" TEXT NOT NULL,"+
			CustomerDao.COL_CUST_GENDER			+ 	" BOOLEAN  DEFAULT 1," +
			CustomerDao.COL_CUST_NAME			+	" TEXT," +
			CustomerDao.COL_CUST_INV_NAME		+	" TEXT NOT NULL," +
			CustomerDao.COL_CUST_ADDRESS_LINE1	+	" TEXT DEFAULT ''," +
			CustomerDao.COL_CUST_ADDRESS_LINE2	+	" TEXT DEFAULT ''," +
			CustomerDao.COL_CUST_REGION			+	" TEXT DEFAULT ''," +
			CustomerDao.COL_CUST_LOCALITY		+	" TEXT NOT NULL," + 
			CustomerDao.COL_CUST_LANDMARK		+	" TEXT DEFAULT ''," +
			CustomerDao.COL_CUST_CITY			+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_STATE			+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_ZIPCODE		+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_MOBILE			+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_ALTMOBILE		+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_EMAIL			+	" TEXT DEFAULT '',"+			
			CustomerDao.COL_CUST_DIRECT_LINE	+	" TEXT DEFAULT '',"+
			CustomerDao.COL_CUST_ISNEW			+	" BOOLEAN DEFAULT 0," +
			CustomerDao.COL_CUST_ISCOMMIT		+	" BOOLEAN DEFAULT 0," +
			CustomerDao.COL_CUST_ISAPPROVED		+	" BOOLEAN DEFAULT 0 )";
	
	private static final String CREATE_PRODUCTS	= "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCTS 	+ " (" +
			SalesDao.COL_PD_ID				+	" VARCHAR(255) PRIMARY KEY," +
			SalesDao.COL_PD_NAME			+	" TEXT NOT NULL," +
			SalesDao.COL_PD_BATCHNO			+	" TEXT NOT NULL)";
	
	//private static final String CREATE_COSTS	= "CREATE TABLE IF NOT EXISTS " + TBL_COSTS 	+ " (" +
	//		SalesDao.COL_COST_CUSTID		+	" VARCHAR(255) FOREIGN KEY("+SalesDao.COL_COST_CUSTID		+") REFERENCES "+ TBL_CUSTOMER + "("+CustomerDao.COL_ID +"),"+
	//		SalesDao.COL_COST_PRODUCT_ID	+	" VARCHAR(255) FOREIGN KEY("+SalesDao.COL_COST_PRODUCT_ID	+") REFERENCES "+ TBL_PRODUCTS + "("+SalesDao.COL_PD_ID +"),"+
	//		SalesDao.COL_COST				+	" FLAOT DEFAULT 0";
	
	private static final String CREATE_COSTS	= "CREATE TABLE IF NOT EXISTS " + TBL_COSTS 	+ " (" +
			CustomerDao.COL_COST_CUSTID		+	" VARCHAR(255),"+
			CustomerDao.COL_COST_PRODUCT_ID	+	" VARCHAR(255),"+
			CustomerDao.COL_COST				+	" FLOAT DEFAULT 0,PRIMARY KEY (" + CustomerDao.COL_COST_CUSTID + "," + CustomerDao.COL_COST_PRODUCT_ID +"))";
	
	private static final String CREATE_BILL_IMGS= "CREATE TABLE IF NOT EXISTS " + TBL_BILL_IMGS	+ " (" +
			ImagesDao.COL_IMG_ID			+	" VARCHAR(255) PRIMARY KEY,"+
			ImagesDao.COL_IMG_IMAGE			+	" BLOB )";
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
		this.context = context;
	}

	/**
	 * @param context
	 */
	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			Log.d(LOG_TAG, "create database");
			// create tables
			db.execSQL(CREATE_SALE_TRXN);
			db.execSQL(CREATE_SALE_HELP);
			db.execSQL(CREATE_CUSTOMER);
			db.execSQL(CREATE_PRODUCTS);
			db.execSQL(CREATE_COSTS);
			db.execSQL(CREATE_BILL_IMGS);
			Log.i(LOG_TAG, " tables is created successfully");
		}
		catch(Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}
	}

	/**
	 * Delete database
	 */
	private void deleteDatabase()
	{
		try
		{
			context.deleteDatabase(DATABASE_NAME);
		}
		catch(Exception e)
		{
			Log.d(LOG_TAG, e.getMessage());
		}		
	}

	/**
	 * Initialize the row table with default row
	 */
	private void initializeTable(SQLiteDatabase db)
	{
		try
		{
			/*
			 * ContentValues values = new ContentValues();
			 * values.put(DBHelper.TITLE_COLUMN, "favorites");
			 * values.put(DBHelper.POSITION_COLUMN, 0);
			 * values.put(DBHelper.ROW_TYPE_COLUMN, 1); itemsRowId =
			 * db.insertOrThrow(DBHelper.ROWS_TABLE, DBHelper.TITLE_COLUMN,
			 * values);
			 */
		} catch (Exception e)
		{
			Log.e(LOG_TAG, "initializeTable", e);
		}
	}

	/**
	 * Upgrade database
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (oldVersion == newVersion)
		{
			Log.i(LOG_TAG, "Database upgrade not needed");
			return;
		}
		Log.i(LOG_TAG, "Database needs to be upgraded from version " + oldVersion + " to version " + newVersion);
		boolean success = doUpgrade(db, oldVersion, newVersion);
		if (success)
		{
			Log.i(LOG_TAG, "Database was updated from version " + oldVersion + " to version " + newVersion);
		} else
		{
			Log.w(LOG_TAG, "Database was NOT updated from version " + oldVersion + " to version " + newVersion);
			rebuildTables(db);
		}
	}

	/**
	 * Rebuild tables
	 * 
	 * @param db
	 */
	private void rebuildTables(final SQLiteDatabase db)
	{
		// something very bad happened...
		deleteDatabase();
		System.exit(1);
	}

	/**
	 * Upgrade database tables
	 * 
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 * @return
	 */
	private boolean doUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.i(LOG_TAG, "doUpgrade: " + oldVersion + " to " + newVersion);
		boolean success = true;
		try
		{
			db.beginTransaction();
			int currentVersion = oldVersion;
			for (int i = currentVersion; i < newVersion; i++)
			{
				if (currentVersion == 1)
				{
					upgradeFrom1To2(db);
				}
				currentVersion++;
			}
			db.setVersion(newVersion);
			db.setTransactionSuccessful();
			Log.i(LOG_TAG, "Database upgrade was successful");
		} catch (Exception e)
		{
			Log.e(LOG_TAG, "Database upgrade was NOT successful", e);
			success = false;
		} finally
		{
			db.endTransaction();
		}
		return success;
	}

	/**
	 * Clear database tables
	 * 
	 * @param context
	 * @return
	 */
	public static boolean clearTables(Context context)
	{
		boolean success = true;
		Log.i(LOG_TAG, "Clearing all databases");
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try
		{
			db.beginTransaction();
			Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
			if (cursor.moveToFirst())
			{
				String tableName = cursor.getString(0);
				Log.d(LOG_TAG, "clearing " + tableName);
				db.delete(tableName, null, null);
			}
			if (null != cursor)
				cursor.close();
			Log.i(LOG_TAG, "Databased tables cleared");
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			Log.e(LOG_TAG, "Database tables NOT cleared", e);
			success = false;
		} finally
		{
			db.endTransaction();
			db.close();
		}
		return success;
	}

	/**
	 * Upgrade database from version 1 to version 2
	 * 
	 * @param db
	 */
	private void upgradeFrom1To2(SQLiteDatabase db)
	{
	}
}
