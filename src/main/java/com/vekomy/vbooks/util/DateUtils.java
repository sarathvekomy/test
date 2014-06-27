/**
 * com.vekomy.vbooks.util.DateUtils.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 9, 2013
 */
package com.vekomy.vbooks.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This utility class is responsible to parse different {@link Date} formats.
 * 
 * @author Sudhakar
 * 
 */
public class DateUtils {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DateUtils.class);
	/**
	 * Calendar variable holds calendar.
	 */
	private static Calendar calendar = Calendar.getInstance();
	/**
	 * SimpleDateFormat variable holds DDMMYYYY_FORMAT.
	 */
	private static final SimpleDateFormat DDMMYYYY_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	/**
	 * SimpleDateFormat variable holds YYYYMMDD_FORMATE.
	 */
	private static final SimpleDateFormat YYYYMMDD_FORMATE = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * SimpleDateFormat variable holds DDMMYYYY_HHMMSS_FORMATE.
	 */
	private static final SimpleDateFormat DDMMYYYY_HHMMSS_FORMATE = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	/**
	 * SimpleDateFormat variable holds MMMYY_FORMAT.
	 */
	private static final SimpleDateFormat MMMYY_FORMAT = new SimpleDateFormat("MMMyy");
	/**
	 * SimpleDateFormat variable holds MM_FORMAT.
	 */
	private static final SimpleDateFormat MM_FORMAT = new SimpleDateFormat("MM");
	/**
	 * SimpleDateFormat variable holds DDMMM_FORMAT.
	 */
	private static final SimpleDateFormat DDMMM_FORMAT = new SimpleDateFormat("ddMMM");
	/**
	 * SimpleDateFormat variable holds YYYYMMDDHHMMSS_FORMAT
	 */
	private static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * This method is responsible to return Minimum time stamp as 00:00:00 of
	 * given date.
	 * 
	 * @param createdDate - {@link Date}
	 * @return startTime - {@link Date}
	 * 
	 */
	public static Date getStartTimeStamp(Date createdDate) {
		Date startTime;
		if(createdDate == null) {
			startTime = null;
		} else {
			calendar.setTime(createdDate);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startTime = calendar.getTime();
		}
		
		return startTime;
	}

	/**
	 * This method is responsible to return Maximum time stamp as 23:59:59 of
	 * given date.
	 * 
	 * @param createdDate - {@link Date}
	 * @return endTime - {@link Date}
	 * 
	 */
	public static Date getEndTimeStamp(Date createdDate) {
		Date endTime;
		if(createdDate == null) {
			endTime = null;
		} else {
			calendar.setTime(createdDate);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			endTime = calendar.getTime();
		}
		return endTime;
	}
	

	/**
	 * This method is responsible to return 2days before date for
	 * given date.
	 * 
	 * @param createdDate - {@link Date}
	 * @return twodaysTime - {@link Date}
	 * 
	 */
	public static Date getBeforeTwoDays(Date todayDate,Integer grantedViewDays) {
		Date twodaysTime;
		if(todayDate == null) {
			twodaysTime = null;
		} else {
			calendar.setTime(todayDate);
			calendar.add(Calendar.DATE, -grantedViewDays); 
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			twodaysTime = calendar.getTime();
		}
		return twodaysTime;
	}
	
	/**
	 * This method is responsible to return 2days before date for
	 * given date.
	 * 
	 * @param createdDate - {@link Date}
	 * @return twodaysTime - {@link Date}
	 * 
	 */
	public static Date getBeforeTwoDays(Date todayDate) {
		Date twodaysTime;
		if(todayDate == null) {
			twodaysTime = null;
		} else {
			calendar.setTime(todayDate);
			calendar.add(Calendar.DATE, -2); 
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			twodaysTime = calendar.getTime();
		}
		return twodaysTime;
	}
	
	/**
	 * This method is responsible to parse the {@link Date}, which includes time stamp.
	 * 
	 * @param dateWithTimestamp - {@link Date}
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String format(Date dateWithTimestamp){
		String formatedDate = null;
		if(dateWithTimestamp == null){
			formatedDate = "";
		}else {
			formatedDate = DDMMYYYY_FORMAT.format(dateWithTimestamp);
		}
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to parse the {@link Date}, which includes time stamp.
	 * 
	 * @param dateWithTimestamp - {@link Date}
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String format1(Date date){
		String formatedDate = null;
		if(date == null){
			formatedDate = "";
		} else {
			formatedDate = YYYYMMDD_FORMATE.format(date);
		}
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to parse the {@link Date}, which includes time stamp.
	 * 
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String downloadFormat(Date date){
		String formatedDate = "";
		if(date != null) {
			formatedDate = YYYYMMDDHHMMSS_FORMAT.format(date);
		}
				
		return formatedDate;
	}
	
	/**
	 * This method is responsible to parse the {@link Date}, which includes time stamp.
	 * 
	 * @param date - {@link Date}
	 * @return formatedDate - {@link String}
	 */
	public static String format2(Date date){
		String formatedDate = null;
		if(date == null){
			formatedDate = "";
		} else {
			formatedDate = MMMYY_FORMAT.format(date);
		}
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to parse {@link Date} in dd-mm-yyyy format.
	 * 
	 * @param date - {@link Date}
	 * @return formatedDate - {@link Date}
	 * @throws ParseException - {@link ParseException}
	 * 
	 */
	public static Date parse(String date) throws ParseException {
		Date formatedDate = null;
		if(date != null) {
			formatedDate = DDMMYYYY_FORMAT.parse(date);
		} 
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to generate seven days before {@link Date} from system date.
	 * 
	 * @param currentDate - {@link Date}
	 * @return previousDate - {@link Date}
	 * 
	 */
	public static Date getDateAfterSevenDays(Date currentDate) {
		Date previousDate = null;
		if(currentDate != null) {
			previousDate = DateUtils.getStartTimeStamp(new Date(currentDate.getTime() + 7 * 24 * 3600 * 1000));
		} 
		
		return previousDate;
	}
	/**
	 * This method is responsible to generate thirty days before {@link Date} from system date.
	 * 
	 * @param currentDate - {@link Date}
	 * @return previousDate - {@link Date}
	 * 
	 */
	public static Date getAfterThirtyDays(Date currentDate) {
		Date previousDate = null;
		if(currentDate != null) {
			calendar.setTime(currentDate);
			calendar.add(Calendar.DATE, +30);
			previousDate = calendar.getTime();
		} 
		
		return previousDate;
	}
	
	/**
	 * This method is responsible to generate thirty days before {@link Date} from system date.
	 * 
	 * @param currentDate - {@link Date}
	 * @return yesterdayDate - {@link Date}
	 * 
	 */
	public static Date getYestersDayDate(Date currentDate) {
		Date yesterdayDate = null;
		if(currentDate != null) {
			calendar.setTime(currentDate);
			calendar.add(Calendar.DATE, -1);
			yesterdayDate = calendar.getTime();
		} 
		
		return yesterdayDate;
	}
	
	/**
	 * This method is responsible to  parse {@link Date}  to include time stamp as dd-MM-yyyy HH:mm:ss.
	 * 
	 * @param date - {@link Date}
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String formatDateWithTimestamp(Date date) {
		String formatedDate = null;
		if(date != null) {
			formatedDate = DDMMYYYY_HHMMSS_FORMATE.format(date);
		} 
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to get the difference between two {@link Date}s.
	 * 
	 * @param newDate - {@link Date}
	 * @param oldDate - {@link Date}
	 * @return days - {@link Integer}
	 * 
	 */
	public static Integer getDifferenceDays(Date newDate, Date oldDate) {
		Integer days = new Integer(0);
		if(newDate != null || oldDate != null) {
			days = (int) ((newDate.getTime() - oldDate.getTime())/(1000 * 60 * 60 * 24));
		}  
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Difference B/W given two days are {}", days);
		}
		return days;
	}
	
	/**
	 * This method is responsible to  parse {@link Date}  to include time stamp as MM.
	 * 
	 * @param date - {@link Date}
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String getMonth(Date date) {
		String formatedDate = null;
		if(date != null){
			formatedDate = MM_FORMAT.format(date);
		} 
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to  parse {@link Date}  to include time stamp as dd-MMM.
	 * 
	 * @param date - {@link Date}
	 * @return formatedDate - {@link String}
	 * 
	 */
	public static String getDayMonth(Date date) {
		String formatedDate = null;
		if(date == null){
			formatedDate = DDMMM_FORMAT.format(date);
		} 
		
		return formatedDate;
	}
	
	/**
	 * This method is responsible to  get quarterly {@link Date}.
	 * 
	 * @param date - {@link Date}
	 * @return yesterdayDate - {@link String}
	 * 
	 */
	public static Date getQuarterMonth(Date date) {
		Date yesterdayDate = null;
		if(date != null) {
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, 3);
			yesterdayDate = calendar.getTime();
		} 
		
		return yesterdayDate;
	}
}
