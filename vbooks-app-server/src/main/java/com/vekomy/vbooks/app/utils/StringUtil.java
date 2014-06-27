package com.vekomy.vbooks.app.utils;

import java.text.DecimalFormat;

public class StringUtil {
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00");
	private static final DecimalFormat currencyFormat = new DecimalFormat("###,###,###.00");
	private static final DecimalFormat quantityFormat = new DecimalFormat("###,###,###");
	private static final DecimalFormat invoiceNoFormat = new DecimalFormat("0000");
	
	public StringUtil(){
	}
	
	public static String format(String var){
		if(var==null || var.equals("null")||var == "0"){
			return "";
		}
		else return var;
	}
	public static String format(long var){
		if(var==0){
			return "";
		}
		else return ""+var;
	}

	public static String format(Integer var) {
		if (var == null || var == 0) {
			return "0";
		} else
			return "" + var;
	}

	public static String format(Float var) {
		if (var == null || var == 0) {
			return "0";
		} else
			return "" + var;
	}

	public static String floatFormat(Float value){
		if(value == null ||value == 0){
			return "0.00";
		} else {
			return decimalFormat.format(value);
		}
	}
	public static String currencyFormat(Float value){
		if(value == null ||value == 0){
			return "0.00";
		} else {
			return currencyFormat.format(value);
		}
	}
	public static String quantityFormat(Integer value){
		if(value == null ||value == 0){
			return "0";
		} else {
			return quantityFormat.format(value);
		}
	}
	
	public static String booleanFormat(boolean value){
		if(value){
			return "Existing Customer";
		} else {
			return "New Customer";
		}
	}
	public static String nullFormat(Integer value){
		if(value==null||value == 0){
			return "";
		} else {
			return quantityFormat.format(value);
		}
	}
	public static String nullFormat(String value){
		
		if(value.equals("0")||value == null||value.equals("null") ){
			return "";
		}
		else return value;
	}
	
	public static String invoiceNoFormat(Integer value) {
		if(null == value || 0 == value) {
			return " ";
		} else {
			return invoiceNoFormat.format(value);
		}
	}
}
