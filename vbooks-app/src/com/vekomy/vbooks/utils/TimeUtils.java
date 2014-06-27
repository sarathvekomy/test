
package com.vekomy.vbooks.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author NKR
 *
 */
public class TimeUtils
{
	
	private static final SimpleDateFormat vBookDateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// yyMMdd
	
	//formatting time to have AM/PM text using 'a' format
	//private static final SimpleDateFormat vBookAppDateformat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");// yyMMdd
	/**
	 * day of month (1 - 31)
	 * month of year (0 - 11)
	 * year - 1900
	 * @param date
	 * @return
	 */
	public static String getDateId( )
	{
		try
		{
			try
			{
				Date date = vBookDateformat.parse(vBookDateformat.format(new Date()));
				if (date != null) 
				{
				    Calendar cal = Calendar.getInstance();
				    cal.setTime(date);
				    return cal.get(Calendar.YEAR) + String.format("%02d",(cal.get(Calendar.MONTH)+1)) + String.format("%02d",cal.get(Calendar.DATE)); 
				    // + ((cal.get(Calendar.AM_PM) == Calendar.AM) ? "0":"1") +  String.format("%02d",cal.get(Calendar.HOUR)) + String.format("%02d",cal.get(Calendar.MINUTE)) + String.format("%02d",cal.get(Calendar.SECOND));
				}
			}catch (ParseException e)
			{
			}

		}
		catch (Exception e)
		{
		}
		return null;
	}
}
