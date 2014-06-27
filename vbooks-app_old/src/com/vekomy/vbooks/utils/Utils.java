/**
 * com.vekomy.vbooks.helpers.AddCustomerHelper.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 3, 2013
 */
package com.vekomy.vbooks.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * @author koteswararao
 * 
 */
public class Utils
{
	public static final int DIALOG_OK = 0;
	public static final int DIALOG_YES_NO = 1;
	public static final int DIALOG_YES_NO_CANCEL = 2;

	//public	static DateFormat Reportdateformat 	= new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
	public	static DateFormat dateformat 		=	new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	public	static DateFormat Reportdateformat 	= 	new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
	
	private static final SimpleDateFormat vbookDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// yyMMdd
	
	//formatting time to have AM/PM text using 'a' format
	private static final SimpleDateFormat vbookAppDateformat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");// yyMMdd
	
	
	public static final String DIALOG_TITLE = "V book";

	public static void displayDialog(Context obj, Drawable icon, String msg, int dialogType, String pvBtn,
			DialogInterface.OnClickListener pvlistener, String nvBtn, DialogInterface.OnClickListener nvlistener, String ntBtn,
			DialogInterface.OnClickListener ntlistener)
	{
		try
		{
			AlertDialog.Builder adb = new AlertDialog.Builder(obj);
			adb.setTitle(DIALOG_TITLE);
			// adb.setIcon(icon);
			adb.setMessage(msg);
			adb.setCancelable(false);
			switch (dialogType)
			{
			case DIALOG_YES_NO_CANCEL:
				// Setting Netural "Cancel" Button
				adb.setNeutralButton(ntBtn, ntlistener);
			case DIALOG_YES_NO:
				// Setting Negative "NO" Button
				adb.setNegativeButton(nvBtn, nvlistener);
			case DIALOG_OK:
				// Setting OK Button
				adb.setPositiveButton(pvBtn, pvlistener);
				break;
			}// switch
				// Showing Alert Message
			adb.show();
		}
		catch(Exception e)
		{
			Toast.makeText(obj,"Error : at Dialog Display", Toast.LENGTH_LONG).show();
		}

	}

	public static Bitmap getBmpFromRes(Resources res, int id)
	{
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1; // sample size value depends upon your image
										// so you have to calculate it on run time.
			options.inPurgeable = true; // IMP
			return BitmapFactory.decodeResource(res, id, options);
		}
		catch(Exception e)
		{
			//Toast.makeText(obj,"Error : at getBmpFromRes", Toast.LENGTH_LONG).show();
			return null;
		}		
	}
	
	public static boolean checkNetworkStatus(Context context)
    {
		try
		{
	        ConnectivityManager conMgr 	=  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkinfo		=	conMgr.getActiveNetworkInfo();
	        
	        //if(nf != null && nf.isConnectedOrConnecting()==true )
	        if( networkinfo != null && networkinfo.isConnected() == true )
	        {
	        	// Return a human-readable name describe the type of the network, for example "WIFI" or "MOBILE".
	        	Toast.makeText(context,"Your Device connected Internet Through " + networkinfo.getTypeName() , Toast.LENGTH_SHORT).show();
	        	return true;
	        }
	        Toast.makeText(context,"Your Device not having any internet access.", Toast.LENGTH_SHORT).show();
	        return false;
		}
		catch(Exception e)
		{
			Toast.makeText(context,"Error : checking Internet Connection.", Toast.LENGTH_SHORT).show();
			return false;
		}		
    }
	
	public static Date serverDate2ReportDate(Date serverDate)
	{
		try
		{
			//Date tmp = Reportdateformat.parse(dateformat.format(serverDate));
			return DateFormat.getDateTimeInstance().parse(serverDate.toString());
			//return Reportdateformat.parse(dateformat.format(serverDate));
			//Date v_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(serverDate.toString());
			
			//return Reportdateformat.parse(Reportdateformat.format(v_date));
		} 
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}		
		return null;
	}
	
	public static Date ReportDate2serverDate(String reportDate)
	{
		try
		{
			//String strtmp = dateformat.format(serverDate);
			//System.out.println(strtmp);
			//Date tmp = dateformat.parse(reportDate);
			//System.out.println(tmp.toString());
			return dateformat.parse(reportDate);
			//return DateFormat.getDateTimeInstance().parse(serverDate.toString());
			//return Reportdateformat.parse(dateformat.format(serverDate));
			//Date v_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(serverDate.toString());
			
			//return Reportdateformat.parse(Reportdateformat.format(v_date));
		} 
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}		
		return null;
	}
	
	public static int dip(Context context, int pixels) {
		   float scale = context.getResources().getDisplayMetrics().density;
		   return (int) (pixels * scale + 0.5f);
		}
	


	/**
     * 
	 * Retrieves the current date/time. Date/Time format: yyyy-MM-dd HH:mm:ss
	 *
	 * @return     Current date/time
	 * @exception  None
	 * @since      3.0
	 *
	 */
	public static Date getCurrentTime()
	{
		try
		{
			return vbookDateformat.parse(vbookDateformat.format(new Date()));
		}catch (ParseException e)
		{
		}
		return null;

	}// getCurrentTime
	
	/**
	 * day of month (1 - 31)
	 * month of year (0 - 11)
	 * year - 1900
	 * @param date
	 * @return
	 */
	public static String getUniqueIDfromDate()
	{
		try
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(getCurrentTime());
			return cal.get(Calendar.YEAR) + String.format("%02d",(cal.get(Calendar.MONTH)+1)) + String.format("%02d",cal.get(Calendar.DATE)) + 
			    		((cal.get(Calendar.AM_PM) == Calendar.AM) ? "0":"1") +  String.format("%02d",cal.get(Calendar.HOUR)) + String.format("%02d",cal.get(Calendar.MINUTE)) + String.format("%02d",cal.get(Calendar.SECOND));
		}
		catch (Exception e)
		{
			
		}
		return "";
	}
}
