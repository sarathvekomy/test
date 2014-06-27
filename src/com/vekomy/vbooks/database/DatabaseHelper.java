/**
com.vekomy.vbooks.service.SyncService.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 19-Aug-2013
 *
 * @author nkr
 */
package com.vekomy.vbooks.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database management utility.
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	/**
	 * Tag constant to log the information.
	 */
	private static final String LOG_TAG = "DatabaseHelper";

	private Context context;

	public final static int CURRENT_DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "vbooks_db";

	// AuthKey table
	public final static String AUTH_KEY_TABLE = "AuthKey";

	// Items table
	public final static String ITEMS_TABLE = "items";
	public final static String ID_COLUMN = "id";
	public final static String ACCOUNT_NUMBER_COLUMN = "accountNumber";
	public final static String AUTH_KEY_COLUMN = "authKey";
	// Track id of default items row
	private long itemsRowId;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	/**
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, CURRENT_DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOG_TAG, "create database");
		// create tables
		createTables(db);
	}

	/**
	 * Delete database
	 */
	private void deleteDatabase() {
		context.deleteDatabase(DATABASE_NAME);
	}

	/**
	 * Create row table
	 * 
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) 
	{
		String TABLE_CREATE = "CREATE TABLE " + AUTH_KEY_TABLE + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ACCOUNT_NUMBER_COLUMN + " STRING, "
				+ AUTH_KEY_COLUMN + " INTEGER" + ");";

		db.execSQL(TABLE_CREATE);
		Log.i(LOG_TAG, AUTH_KEY_TABLE + " table was created successfully");
	}


	/**
	 * Upgrade database
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
	 *      int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == newVersion) {
			Log.i(LOG_TAG, "Database upgrade not needed");
			return;
		}
		Log.i(LOG_TAG, "Database needs to be upgraded from version " + oldVersion + " to version " + newVersion);
		boolean success = doUpgrade(db, oldVersion, newVersion);
		if (success) {
			Log.i(LOG_TAG, "Database was updated from version " + oldVersion + " to version " + newVersion);
		} else {
			Log.w(LOG_TAG, "Database was NOT updated from version " + oldVersion + " to version " + newVersion);
			rebuildTables(db);
		}
	}

	/**
	 * Rebuild tables
	 * 
	 * @param db
	 */
	private void rebuildTables(final SQLiteDatabase db) {
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
	private boolean doUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(LOG_TAG, "doUpgrade: " + oldVersion + " to " + newVersion);
		boolean success = true;
		try {
			db.beginTransaction();
			int currentVersion = oldVersion;
			for (int i = currentVersion; i < newVersion; i++) {
				if (currentVersion == 1) {
					upgradeFrom1To2(db);
				}
				currentVersion++;
			}
			db.setVersion(newVersion);
			db.setTransactionSuccessful();
			Log.i(LOG_TAG, "Database upgrade was successful");
		} catch (Exception e) {
			Log.e(LOG_TAG, "Database upgrade was NOT successful", e);
			success = false;
		} finally {
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
	public static boolean clearTables(Context context) {
		boolean success = true;
		Log.i(LOG_TAG, "Clearing all databases");
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			db.beginTransaction();

			Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
			if (cursor.moveToFirst()) {
				String tableName = cursor.getString(0);
				Log.d(LOG_TAG, "clearing " + tableName);
				db.delete(tableName, null, null);
			}
			if (null != cursor)
				cursor.close();
			Log.i(LOG_TAG, "Databased tables cleared");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Database tables NOT cleared", e);
			success = false;
		} finally {
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
	private void upgradeFrom1To2(SQLiteDatabase db) {
	}

	/**
	 * Method insert auth key in database.
	 * @param authKey
	 *
	public void addAuthKey(AuthKey authKey) {
		clearAuthKey();
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(ACCOUNT_NUMBER_COLUMN, authKey.getAccountNumber());
		cv.put(AUTH_KEY_COLUMN, authKey.getAuthKey());
		//cv.put(colDept,2);
		db.insert(AUTH_KEY_TABLE, AUTH_KEY_COLUMN, cv);
		db.close();
	}*/
	/**
	 * get count of rows in auth key.
	 * @return
	 */
	 public int getAuthKeyCount()
	 {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("Select * from "+AUTH_KEY_TABLE, null);
		int x= cur.getCount();
		cur.close();
		return x;
	 }
	 
		/**
		 * get count of rows in auth key.
		 * @return
		 */
	 public String getAuthKey()
	 {
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cur= db.rawQuery("Select * from "+AUTH_KEY_TABLE, null);
		if(cur.moveToFirst()) {
			String auth = cur.getString(2);
			cur.close();
			return auth;
		}
		cur.close();
		return "";
	 }
	 
	/**
	* get count of rows in auth key.
	* @return
	*/
	 public void clearAuthKey()
	 {
		 SQLiteDatabase db=this.getWritableDatabase();
		 db.delete(AUTH_KEY_TABLE, null, null);
		 db.close();
		
	 }
}
