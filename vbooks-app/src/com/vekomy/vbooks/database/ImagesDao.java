/**
 * com.vekomy.vbooks.database.ImagesDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 07-Nov-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.vekomy.vbooks.VbookApp;

/**
 * @author nkr
 *
 */
public class ImagesDao 
{
	private static String LOG_TAG = "ImagesDao";
	
	public final static String COL_IMG_ID 				= "ID";
	public final static String COL_IMG_IMAGE 			= "IMAGE";

	private String[] IMG_ALL =	{
										COL_IMG_ID,
										COL_IMG_IMAGE
								};
	
	public ImagesDao()
	{
		
	}
	public ImagesDao(Context context)
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
		
	/**
	 * 
	 * @param deloveryNote
	 * @return
	 */
	public long saveBillImage(SQLiteDatabase db,String id,Bitmap billImg)
	{
		try
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			billImg.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte imageInByte[] = stream.toByteArray();
			ContentValues values = new ContentValues();
			values.put(COL_IMG_ID, 		id);
			values.put(COL_IMG_IMAGE, 	imageInByte);			
			if(db.insert(DBHelper.TBL_BILL_IMGS, null, values) == -1)
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
	
	public Bitmap readBillImage(SQLiteDatabase db,String id)
	{
		try
		{
			Cursor cursor = db.query(	DBHelper.TBL_BILL_IMGS, 
					new String[]
								{	
									COL_IMG_IMAGE
								},
								COL_IMG_ID 	+ " =? ",
								new String[] 
								{
									id
								},
								null, null,null,null);
			if (cursor != null)
				cursor.moveToFirst();
			ByteArrayInputStream imageStream = null;
			while (!cursor.isAfterLast())
			{
				byte[] outImage = cursor.getBlob(0);
				imageStream = new ByteArrayInputStream(outImage);
				cursor.moveToNext();
			}
			cursor.close();
			if(imageStream!=null)
				return BitmapFactory.decodeStream(imageStream);
		}
		catch(Exception e)
		{
			//Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	public long removeBillImage(SQLiteDatabase db,String id)
	{
		int result = 0;
		try
		{
			result = db.delete(DBHelper.TBL_BILL_IMGS,COL_IMG_ID + " = \'" + id + "\'", null);			
		}
		catch(Exception e)
		{
			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			result = -1;
		}
		return result == 0?-1:result;
	}
}
