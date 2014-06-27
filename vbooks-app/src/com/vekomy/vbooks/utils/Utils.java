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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;
import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.PasswordChangeRequest;

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
	
	public	static DecimalFormat millionformat 		= new DecimalFormat("#,###.00");
	private static final SimpleDateFormat vbookDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// yyMMdd
	
	//formatting time to have AM/PM text using 'a' format
	private static final SimpleDateFormat vbookAppDateformat0 = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");// yyMMdd
	
	private static final SimpleDateFormat vbookAppDateformat1 = new SimpleDateFormat("yyyyMMdd");// yyMMdd
	
	
	public static final String DIALOG_TITLE = "V book";
	
	public static final String 	pswdPattern		=	"((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
	public static final Pattern patteren_compile=	Pattern.compile(pswdPattern);

	public static void displayDialog(	Context obj, 
										Drawable icon, 
										String msg, 
										int dialogType, 
										String pvBtn,
										DialogInterface.OnClickListener pvlistener, 
										String nvBtn, 
										DialogInterface.OnClickListener nvlistener, 
										String ntBtn,
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
			Toast.makeText(obj, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public static void displayRemarksDialog(Context obj,final boolean isCR,String remarks,OnClickListener pvlistener)
	{
		try
		{			
			final Dialog descriptionDialog = new Dialog(obj,R.style.customDialogStyle);
			descriptionDialog.setCancelable(false);
			descriptionDialog.setContentView(R.layout.remarks_display_dialog);			
			if(isCR)
				descriptionDialog.findViewById(R.id.viewnewDescription).setEnabled(false);
			else
				descriptionDialog.findViewById(R.id.viewnewDescription).setVisibility(View.GONE);
			Button Cancel	=	(Button) descriptionDialog.findViewById(R.id.btnremarksCancel);
			Cancel.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) {
					descriptionDialog.dismiss();
				}
			});
			((Button) descriptionDialog.findViewById(R.id.btnremarksSave)).setOnClickListener(pvlistener);
			//Button btnSave	=	(Button) descriptionDialog.findViewById(R.id.btnremarksSave);

			/*btnSave.setOnClickListener(pvlistener);
			new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					resultMsg="";
					if(isCR)
						resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtnewDescription)).getText().toString();
					else
						resultMsg = ((EditText) descriptionDialog.findViewById(R.id.txtDescription)).getText().toString();
				}
			});*/
			descriptionDialog.show();
		}
		catch(Exception e)
		{
			Toast.makeText(obj, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
	        	//Toast.makeText(context,"Your Device connected Internet Through " + networkinfo.getTypeName() , Toast.LENGTH_SHORT).show();
	        	return true;
	        }
	        //Toast.makeText(context,"Your Device not having any internet access.", Toast.LENGTH_SHORT).show();
	        return false;
		}
		catch(Exception e)
		{
			//Toast.makeText(context,"Error : checking Internet Connection.", Toast.LENGTH_SHORT).show();
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
			return vbookAppDateformat1.parse(reportDate);
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
	
	public static int dip(Context context, int pixels) 
	{
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
		}
		catch (ParseException e)
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
			return 	cal.get(Calendar.YEAR) 									+ 
					String.format("%02d",(cal.get(Calendar.MONTH)+1)) 		+ 
					String.format("%02d",cal.get(Calendar.DATE)) 			+ 
			    	((cal.get(Calendar.AM_PM) == Calendar.AM) ? "0":"1") 	+  
			    	String.format("%02d",cal.get(Calendar.HOUR)) 			+ 
			    	String.format("%02d",cal.get(Calendar.MINUTE)) 			+ 
			    	String.format("%02d",cal.get(Calendar.SECOND));
		}
		catch (Exception e)
		{
		}
		return "";
	}
	
	public static void saveData(Context con, String variable, String data)
	{
		SharedPreferences prefs = con.getSharedPreferences(Constants.PREFS_NAME , 0);
	    prefs.edit().putString(variable, data).commit();
	}
	
	public static String getData(Context con, String variable, String defaultValue)
	{
		SharedPreferences prefs = con.getSharedPreferences(Constants.PREFS_NAME , 0); 
		return prefs.getString(variable, defaultValue);
	}
	
	public static String currencyInMillion(float amount)
	{
		if(amount==0)
			return "0";
		return millionformat.format(amount);
	}
	
	public static float getNumberFromMillion(String amount)
	{
		try 
		{
			return NumberFormat.getInstance(Locale.US).parse(amount.replaceAll(",", "")).floatValue();
		} catch (ParseException e) {
			return 0;
		}
	}
	public static void passwordValidation(EditText txpwd)
	{
		if (!patteren_compile.matcher(txpwd.getText().toString().trim()).matches()) 
		{
			txpwd.setError("Invalid Password.(min 8 length First Letter must in Upper case one special char)");
			txpwd.setText("");
		}
	}
	
	/**
	 * 
	 */
	public  static void displayPasswordChangeDialog(final Context context,final String currentpswd)
	{
		try
		{
			final Dialog pwdDialog = new Dialog(context,R.style.customDialogStyle);
			pwdDialog.setTitle("Change Password");
			pwdDialog.setCancelable(false);
			pwdDialog.setContentView(R.layout.change_pwd_dialog_show);
			final EditText txOldpwd = (EditText) pwdDialog.findViewById(R.id.txtOldPassword);
			final EditText txtNewPassword = (EditText) pwdDialog.findViewById(R.id.txtNewPassword);
			final EditText txtConfirmPwd = (EditText) pwdDialog.findViewById(R.id.txtConfirmPwd);
			Button btnSavePwd = (Button) pwdDialog.findViewById(R.id.btnSavePwd);
			Button btnCancelPwd = (Button) pwdDialog.findViewById(R.id.btnCancelPwd);

			txOldpwd.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				@Override
				public void onFocusChange(View v, boolean hasFocus) 
				{
					if (!hasFocus) 
					{
						Utils.passwordValidation(txOldpwd);
					}
				}
			});
			txtNewPassword.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				@Override
				public void onFocusChange(View v, boolean hasFocus) 
				{
					if (!hasFocus) 
					{
						Utils.passwordValidation(txtNewPassword);
					}
				}
			});
			txtConfirmPwd.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						Utils.passwordValidation(txtConfirmPwd);
					}
				}
			});
			btnSavePwd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(!(txOldpwd.getText().toString().trim().equals(currentpswd)))
					{
						txOldpwd.setError("wrong old password.");
						return;	
					}
					if (txtNewPassword.getText().toString().trim().equals(""))
					{
						txtNewPassword.setError("New Password is Empty");
						return;
					}
					if (txtConfirmPwd.getText().toString().trim().equals(""))
					{
						txtConfirmPwd.setError("Confirm Password is Empty");
						return;
					}
					if(!txtNewPassword.getText().toString().trim().equals(txtConfirmPwd.getText().toString().trim()))
					{
						txtNewPassword.setError("New password and Confirm does not Mathch.");
						return;
					}
					if(txtNewPassword.getText().toString().trim().equals(txOldpwd.getText().toString().trim()))
					{
						txtNewPassword.setError("Old Password and New Password should not same.");
						return;
					}
					PasswordChangeRequest pswdChangeRequest = new PasswordChangeRequest();
					pswdChangeRequest.setUserName(getData(context, Constants.USER_ID, Constants.NA));
					pswdChangeRequest.setOrganizationId(Integer.parseInt(getData(context, Constants.ORG_ID, "0")));
					pswdChangeRequest.setOldPassword(txOldpwd.getText().toString().trim());
					pswdChangeRequest.setNewPassword(txtNewPassword.getText().toString().trim());
					PasswordChangeTask task = new PasswordChangeTask(context,pwdDialog);
					task.execute(pswdChangeRequest);
					txtConfirmPwd.setError(null);
					txtNewPassword.setError(null);
				}
			});
			btnCancelPwd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pwdDialog.dismiss();
				}
			});
			pwdDialog.show();
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public static void displayNotification(final Context context,String notificationString,String contentText) 
	{
		try
		{
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// Creates an explicit intent for an Activity in your app
			//Intent resultIntent = new Intent(getApplicationContext(),HandleNotificationActivity.class);
			Intent resultIntent = new Intent(Intent.CATEGORY_ALTERNATIVE);
			PendingIntent pendingNotification = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			//PendingIntent resultPendingIntent 	= stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		    NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)  
		    										.setSmallIcon(R.drawable.profile)  
		    										.setTicker(notificationString)
		    										.setDefaults(Notification.DEFAULT_LIGHTS)
		    										.setAutoCancel(true)
		    										.setContentIntent(pendingNotification)
		            								.setContentTitle(notificationString)  
		            								.setContentText(contentText); 
		    
		    Notification notification = builder.build();
		    notification.flags|=notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(0, notification);
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
