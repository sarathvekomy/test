package com.vekomy.vbooks.app.response;

import com.vekomy.vbooks.app.response.Response;

public class TrxnHistory extends Response 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String invoiceNo;
	private String invoiceName;
	private String date;
	private String name;
	private String bal;
	private String dateID;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public String getDateID() {
		return dateID;
	}
	public void setDateID(String dateID) {
		this.dateID = dateID;
	}
}
