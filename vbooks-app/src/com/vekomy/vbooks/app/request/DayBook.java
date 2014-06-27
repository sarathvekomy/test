/**
 * com.vekomy.vbooks.app.request.DayBook.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 05-Sep-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.app.request;

import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author nkr
 *
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayBook  extends Request {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4765216898896937580L;
		
	// =============== basic Info =======================
	String	orgId;
	String	dayBookid;
	String	saleExeName;
	String	createdOn;
	String	reportingManager;
	Boolean	isreturn;
	String	dayBookNo;

	// =============== vehicle Info =======================
	String 	vehicleNo;
	String 	driverName;
	String 	startReading;	
	String 	endReading;

	// =============== Payments Info =======================	
	float  	openingBal;
	float  	closeingBal;
	float	totalAllowances;
	float	allowancesSum;
	float	totalpayableFromCust;
	float	totalReceivedFromCust;
	float	amtTobank;
	float	amtToFactory;

	// =============== allowances Info =====================
	List<DayBookAllowances> allowancesList;
	
	// =============== Products Info =======================
	List<DayBookProduct>	productsList;
	
	private String remarks;
	
	public DayBook()
	{
		orgId			=	"";
		dayBookid		=	"";
		saleExeName		=	"";
		createdOn		=	"";
		reportingManager=	"";		
		dayBookNo		=	"";
		// =============== vehicle Info =======================
		vehicleNo		=	"";
		driverName		=	"";
		startReading	=	"";	
		endReading		=	"";

	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDayBookid() {
		return dayBookid;
	}

	public void setDayBookid(String dayBookid) {
		this.dayBookid = dayBookid;
	}

	public String getSaleExeName() {
		return saleExeName;
	}

	public void setSaleExeName(String saleExeName) {
		this.saleExeName = saleExeName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}
	public Boolean getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(Boolean isreturn) {
		this.isreturn = isreturn;
	}

	public String getDayBookNo() {
		return dayBookNo;
	}

	public void setDayBookNo(String dayBookNo) {
		this.dayBookNo = dayBookNo;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getStartReading() {
		return startReading;
	}

	public void setStartReading(String startReading) {
		this.startReading = startReading;
	}

	public String getEndReading() {
		return endReading;
	}

	public void setEndReading(String endReading) {
		this.endReading = endReading;
	}

	public float getOpeningBal() {
		return openingBal;
	}

	public void setOpeningBal(float openingBal) {
		this.openingBal = openingBal;
	}

	public float getCloseingBal() {
		return closeingBal;
	}

	public void setCloseingBal(float closeingBal) {
		this.closeingBal = closeingBal;
	}

	public float getTotalAllowances() {
		return totalAllowances;
	}

	public void setTotalAllowances(float totalAllowances) {
		this.totalAllowances = totalAllowances;
	}

	public float getAllowancesSum() {
		return allowancesSum;
	}

	public void setAllowancesSum(float allowancesSum) {
		this.allowancesSum = allowancesSum;
	}

	public float getTotalpayableFromCust() {
		return totalpayableFromCust;
	}

	public void setTotalpayableFromCust(float totalpayableFromCust) {
		this.totalpayableFromCust = totalpayableFromCust;
	}

	public float getTotalReceivedFromCust() {
		return totalReceivedFromCust;
	}

	public void setTotalReceivedFromCust(float totalReceivedFromCust) {
		this.totalReceivedFromCust = totalReceivedFromCust;
	}

	public float getAmtTobank() {
		return amtTobank;
	}

	public void setAmtTobank(float amtTobank) {
		this.amtTobank = amtTobank;
	}

	public float getAmtToFactory() {
		return amtToFactory;
	}

	public void setAmtToFactory(float amtToFactory) {
		this.amtToFactory = amtToFactory;
	}

	public List<DayBookAllowances> getAllowancesList() {
		return allowancesList;
	}

	public void setAllowancesList(List<DayBookAllowances> allowancesList) {
		this.allowancesList = allowancesList;
	}

	public List<DayBookProduct> getProductsList() {
		return productsList;
	}

	public void setProductsList(List<DayBookProduct> productsList) {
		this.productsList = productsList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
