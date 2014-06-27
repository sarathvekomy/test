package com.vekomy.vbooks.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class CalendarUtil {

	public static String MONTH = "MON";
	public static int START_MONTH = Calendar.JUNE;
	public static LinkedHashMap<Integer, String> months = new LinkedHashMap<Integer, String>();
	public static int[] academicYears = {2011,2012};
	public static ArrayList<Term> terms = new ArrayList<Term>();
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	static {
		CalendarUtil.load();
	}

	/**
	 * 
	 */
	public CalendarUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void load() {
		months.put(Calendar.JANUARY, "January");
		months.put(Calendar.FEBRUARY, "February");
		months.put(Calendar.MARCH, "March");
		months.put(Calendar.APRIL, "April");
		months.put(Calendar.MAY, "May");
		months.put(Calendar.JUNE, "June");
		months.put(Calendar.JULY, "July");
		months.put(Calendar.AUGUST, "August");
		months.put(Calendar.SEPTEMBER, "September");
		months.put(Calendar.OCTOBER, "October");
		months.put(Calendar.NOVEMBER, "November");
		months.put(Calendar.DECEMBER, "December");
		
		terms.add(new Term(1, "Term 1"));
		terms.add(new Term(2, "Term 2"));
		terms.add(new Term(3, "Term 3"));
		terms.add(new Term(4, "Final Assessment"));
	}
	
	public static String getMonthDisplayString(String month) {
	   return months.get(Integer.parseInt(month));
	}

	public static ArrayList<Month> getAcademicMonths() {
		//LinkedHashMap<String, String> academicMonths = new LinkedHashMap<String, String>();
		ArrayList<Month> academicMonths = new ArrayList<Month>();
		int currentMonth =Calendar.getInstance().get(Calendar.MONTH);
		boolean start = false;
		boolean end = false;
		int academicYear = academicYears[0];
		while (!end) {
			for (int month : months.keySet()) {
				if (CalendarUtil.START_MONTH == month) {
					start = true;
				}
				if (start && !end) {
					//academicMonths.put("" + month, months.get(month));
					academicMonths.add(new Month(month, months.get(month), academicYear));
					if(currentMonth == month) {
						end = true;
					}
					if (Calendar.DECEMBER == month) {
						academicYear = academicYears[1];
					}
				}
			}
		}

		return academicMonths;
	}
	
	public static Term getLatestTerm() {
		return terms.get(0);
	}
	
	public static class Month {
		
		private int month;
		private String monthLabel;
		private int year;
		
		public Month(int month, String monthLabel, int year) {
	        this.month = month;
	        this.monthLabel = monthLabel;
	        this.year = year;
        }

		/**
         * @return the month
         */
        public int getMonth() {
        	return month;
        }

		/**
         * @param month the month to set
         */
        public void setMonth(int month) {
        	this.month = month;
        }

		/**
         * @return the monthLabel
         */
        public String getMonthLabel() {
        	return monthLabel;
        }

		/**
         * @param monthLabel the monthLabel to set
         */
        public void setMonthLabel(String monthLabel) {
        	this.monthLabel = monthLabel;
        }

		/**
         * @return the year
         */
        public int getYear() {
        	return year;
        }

		/**
         * @param year the year to set
         */
        public void setYear(int year) {
        	this.year = year;
        }
		
	}

	public static class Term {
		private String termLabel;
		private int term;
		
		public Term(int temr, String termLabel) {
	        this.term = term;
	        this.termLabel = termLabel;
        }

		public String getTermLabel() {
			return termLabel;
		}

		public void setTermLabel(String termLabel) {
			this.termLabel = termLabel;
		}

		public int getTerm() {
			return term;
		}

		public void setTerm(int term) {
			this.term = term;
		}
	}
	
}
