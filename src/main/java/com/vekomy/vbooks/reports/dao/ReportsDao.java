/**
 * com.vekomy.vbooks.mysales.dao.ReportsDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: May 28, 2013
 */
package com.vekomy.vbooks.reports.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAssignOrganizations;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.result.CustomerProductSalesReportResult;
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.DeliveryNoteCollectionReportResult;
import com.vekomy.vbooks.reports.result.DeliveryNoteReportResult;
import com.vekomy.vbooks.reports.result.FactoryProductWiseReportResult;
import com.vekomy.vbooks.reports.result.JournalReportResult;
import com.vekomy.vbooks.reports.result.ProductReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseSalesReportResult;
import com.vekomy.vbooks.reports.result.SLECustomerWiseSalesResult;
import com.vekomy.vbooks.reports.result.SLEProductReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnQtyReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnReportResult;
import com.vekomy.vbooks.reports.result.SalesWiseReportResult;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.StringUtil;

/**
 * 
 * @author Swarupa.
 * 
 */
public class ReportsDao extends BaseDao{

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ReportsDao.class);
	//private Integer dispatchStock;
	
	/**
	 * This method is responsible for fetching every customer information about products.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 * @throws ParseException - {@link ParseException}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerWiseReportResult> getCustomerWiseData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		Boolean addProductName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String businessName = reportsCommand.getBusinessName();
		String reportType = reportsCommand.getReportType();
		if(reportType != null) {
			
		}
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNoteProducts vb WHERE vb.vbDeliveryNote.businessName = :businessName AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate");
		if(productName!= null && !productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbDeliveryNote.vbOrganization = :organization");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("businessName", businessName);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		query.setParameter("organization", organization);
		
		List<VbDeliveryNoteProducts> productsList = query.list();
		ArrayList<CustomerWiseReportResult> resultList = new ArrayList<CustomerWiseReportResult>();	
		CustomerWiseReportResult reportResult = null;
		for (VbDeliveryNoteProducts products : productsList) {
			reportResult = new CustomerWiseReportResult();
			reportResult.setProductName(products.getProductName());
			reportResult.setBatchNumber(products.getBatchNumber());
			reportResult.setProductCost(products.getProductCost());
			reportResult.setBusinessName(products.getVbDeliveryNote().getBusinessName());
			reportResult.setInvoice_no(products.getVbDeliveryNote().getInvoiceNo());
			reportResult.setProductQty(products.getProductQty());
			reportResult.setTotalCost(products.getTotalCost());
			reportResult.setCreatedOn(DateUtils.format(products.getVbDeliveryNote().getCreatedOn()));
		
			resultList.add(reportResult);
		}
			
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} available for :", resultList.size());
		}
		return resultList;
	}
	
	/**
	 * This method is responsible for fetching all product information.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductWiseReportResult> getProductWiseReportData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		Boolean addProductName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String reportType = reportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbProductInventoryTransaction vb WHERE vb.createdOn BETWEEN :startDate AND :endDate");
		if(productName != null && !productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbOrganization = :vbOrganization");
		queryString.append(" ORDER BY vb.createdOn ASC");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		query.setParameter("vbOrganization", organization);
		List<VbProductInventoryTransaction> productsList = query.list();
		session.close();
		
		List<ProductWiseReportResult> productList = new ArrayList<ProductWiseReportResult>();
		ProductWiseReportResult reportResult = null;
		for (VbProductInventoryTransaction products : productsList) {
			reportResult = new ProductWiseReportResult();
			reportResult.setProductName(products.getProductName());
			reportResult.setBatchNumber(products.getBatchNumber());
		    reportResult.setQuantityType(products.getQuantityType());
		    if(products.getQuantityType().equals("Arrived")){
		    	reportResult.setQuantityType("Production");
		    }else if(products.getQuantityType().equals("Allotted")){
		    	reportResult.setQuantityType("Dispatch");
		    }
			reportResult.setQuantity(products.getQuantity());
			reportResult.setCreatedOn(DateUtils.format(products.getCreatedOn()));
			productList.add(reportResult);
		}
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", productList.size());
		}
		return productList;
	}
	
	/**
	 * This method is responsible for fetching all Sales information.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link SalesWiseReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<SalesWiseReportResult> getSalesWiseReportData(ReportsCommand reportsCommand) {
		Session session = this.getSession();
		String businessName = reportsCommand.getBusinessName();
		String salesExecutiveName = reportsCommand.getSalesExecutive();
		String reportSummaryResults = "";
		String reportTypeData = reportsCommand.getReportType();
		Boolean addBusinessName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = reportsCommand.getSalesExecutive();
		String reportType = reportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNoteProducts  vb WHERE vb.vbDeliveryNote.createdBy = :salesExecutive AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbDeliveryNote.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		}
		queryString.append("  Group By vb.vbDeliveryNote.businessName");
		Query query = session.createQuery(queryString.toString())
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		List<VbDeliveryNoteProducts> salesExecList = query.list();
		List<SalesWiseReportResult> salesExecutiveList = new ArrayList<SalesWiseReportResult>();
		SalesWiseReportResult reportResult = null;
		
		for (VbDeliveryNoteProducts executive : salesExecList) {
			reportResult = new SalesWiseReportResult();
			reportResult.setProductName(executive.getProductName());
			reportResult.setBatchNumber(executive.getBatchNumber());
			reportResult.setProductQty(executive.getProductQty());
			reportResult.setBonusQty(executive.getBonusQty());
			reportResult.setProductCost(StringUtil.floatFormat(executive.getProductCost()));
			reportResult.setTotalCost(StringUtil.floatFormat(executive.getTotalCost()));
			reportResult.setCreatedOn(DateUtils.format(executive.getVbDeliveryNote().getCreatedOn()));
			reportResult.setBusinessName(executive.getVbDeliveryNote().getBusinessName());
			reportResult.setSalesExecutive(salesExecutiveName);
			if(reportTypeData.equalsIgnoreCase("select")){
				reportResult.setReportType(reportSummaryResults);
			}else{
				reportResult.setReportType(reportTypeData);
			}
			reportResult.setStartDate(DateUtils.format(startDate));
			reportResult.setEndDate(DateUtils.format(endDate));
			salesExecutiveList.add(reportResult);
		}
		
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} available for :", salesExecutiveList.size());
		}
		return salesExecutiveList;
	}
	
	/**
	 * This method is responsible for getting the first sub report data.
	 * 
	 * @param salesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResultList - {@link DeliveryNoteReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteReportResult> getFirstSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		String businessName = salesWiseReportCommand.getBusinessName();
		Boolean addBusinessName = Boolean.FALSE;
		Query query = null;
		Date startDate = DateUtils.getStartTimeStamp(salesWiseReportCommand.getStartDate());
		Date eDate = salesWiseReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String reportType = salesWiseReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		String removeStartDateTimeStamp = DateUtils.format1(startDate);
		String removeEndDateTimeStamp = DateUtils.format1(endDate);
		
		ArrayList<DeliveryNoteReportResult> reportResultList = new ArrayList<DeliveryNoteReportResult>();
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNotePayments vb WHERE" + 
				" vb.vbDeliveryNote.createdBy = :createdBy AND SUBSTRING(vb.vbDeliveryNote.createdOn,1,10) BETWEEN :startDate AND :endDate AND vb.vbDeliveryNote.invoiceNo LIKE :invoiceNo");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbDeliveryNote.businessName LIKE :businessName");
			addBusinessName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbDeliveryNote.flag = :flag");
		query = session.createQuery(queryString.toString())
				.setParameter("createdBy", salesWiseReportCommand.getSalesExecutive())
				.setParameter("startDate", removeStartDateTimeStamp)
				.setParameter("endDate", removeEndDateTimeStamp)
				.setParameter("invoiceNo", "%DN%");
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		query.setParameter("flag", new Integer(1));
		List<VbDeliveryNotePayments> paymentList = query.list();
		DeliveryNoteReportResult  reportResult = null;
		VbDeliveryNote deliveryNote = null;
		for (VbDeliveryNotePayments vbDeliveryNotePayments : paymentList) {
			reportResult = new DeliveryNoteReportResult();
			Float presentPayable = vbDeliveryNotePayments.getPresentPayable();
			Float presentPayment = vbDeliveryNotePayments.getPresentPayment();
			deliveryNote = vbDeliveryNotePayments.getVbDeliveryNote();
			reportResult.setCreatedOn(deliveryNote.getCreatedOn());
			reportResult.setInvoiceNo(deliveryNote.getInvoiceNo());
			reportResult.setPresentPayable(presentPayable);
			reportResult.setPresentPayment(presentPayment);
			reportResult.setBalance(presentPayable - presentPayment);
			reportResult.setType("Delivery Note");
			reportResult.setBusinessName(deliveryNote.getBusinessName());
			reportResultList.add(reportResult);
		}
		session.close();
		return reportResultList;
	}

	/**
	 * This method is responsible for getting the second sub report data.
	 * 
	 * @param salesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResultList - {@link SalesReturnReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnReportResult> getSecondSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		String businessName = salesWiseReportCommand.getBusinessName();
		Boolean addBusinessName = Boolean.FALSE;
		Query query = null;
		Date startDate = DateUtils.getStartTimeStamp(salesWiseReportCommand.getStartDate());
		Date eDate = salesWiseReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String reportType = salesWiseReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		ArrayList<SalesReturnReportResult> reportResultList = new ArrayList<SalesReturnReportResult>();
		StringBuffer queryString = new StringBuffer("FROM VbSalesReturnProducts vb WHERE" +
				" vb.vbSalesReturn.createdBy = :createdBy AND vb.vbSalesReturn.createdOn BETWEEN :startDate AND :endDate");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbSalesReturn.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbSalesReturn.flag = :flag AND vb.vbSalesReturn.status = :status");
		query = session.createQuery(queryString.toString())
				.setParameter("createdBy", salesWiseReportCommand.getSalesExecutive())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		query.setParameter("flag", new Integer(1));
		query.setParameter("status", "APPROVED");
		List<VbSalesReturnProducts> salesReturnList = query.list();
		SalesReturnReportResult  reportResult = null;
		VbSalesReturn salesReturn = null;
		for (VbSalesReturnProducts vbSalesReturnProducts : salesReturnList) {
			reportResult = new SalesReturnReportResult();
			salesReturn = vbSalesReturnProducts.getVbSalesReturn();
			reportResult.setCreatedOn(salesReturn.getCreatedOn());
			reportResult.setInvoiceNo(salesReturn.getInvoiceNo());
			reportResult.setTotalCost(vbSalesReturnProducts.getTotalCost());
			reportResult.setType("Sales Return");
			reportResult.setBusinessName(salesReturn.getBusinessName());
			reportResultList.add(reportResult);
		}
		session.close();
		return reportResultList;
		
	}
	
	/**
	 * This method is responsible for getting the third sub report data.
	 * 
	 * @param salesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResultList - {@link DeliveryNoteReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteCollectionReportResult> getThirdSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		String businessName = salesWiseReportCommand.getBusinessName();
		Boolean addBusinessName = Boolean.FALSE;
		Query query = null;
		Date startDate = DateUtils.getStartTimeStamp(salesWiseReportCommand.getStartDate());
		Date eDate = salesWiseReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String reportType = salesWiseReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		ArrayList<DeliveryNoteCollectionReportResult> reportResultList = new ArrayList<DeliveryNoteCollectionReportResult>();
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNotePayments vb WHERE" +
				" vb.vbDeliveryNote.createdBy = :createdBy AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate AND vb.vbDeliveryNote.invoiceNo LIKE :invoiceNo");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbDeliveryNote.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		}
		query = session.createQuery(queryString.toString())
				.setParameter("createdBy", salesWiseReportCommand.getSalesExecutive())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("invoiceNo", "%COLLECTIONS%");
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		List<VbDeliveryNotePayments> paymentList = query.list();
		DeliveryNoteCollectionReportResult  reportResult = null;
		VbDeliveryNote deliveryNote = null;
		for (VbDeliveryNotePayments vbDeliveryNotePayments : paymentList) {
			reportResult = new DeliveryNoteCollectionReportResult();
			Float presentPayment = vbDeliveryNotePayments.getPresentPayment();
			deliveryNote = vbDeliveryNotePayments.getVbDeliveryNote();
			reportResult.setCreatedOn(deliveryNote.getCreatedOn());
			reportResult.setInvoiceNo(deliveryNote.getInvoiceNo());
			reportResult.setPresentPayment(presentPayment);
			reportResult.setType("Cash Collections");
			reportResult.setBusinessName(deliveryNote.getBusinessName());
			reportResultList.add(reportResult);
		}
		session.close();
		return reportResultList;
		
	}
	
	
	/**
	 * This method is responsible for getting the third sub report data.
	 * 
	 * @param salesWiseReportCommand - {@link ReportsCommand}
	 * @return reportResultList - {@link DeliveryNoteReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<JournalReportResult> getFourthSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		String businessName = salesWiseReportCommand.getBusinessName();
		Boolean addBusinessName = Boolean.FALSE;
		Query query = null;
		Date startDate = DateUtils.getStartTimeStamp(salesWiseReportCommand.getStartDate());
		Date eDate = salesWiseReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String reportType = salesWiseReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		ArrayList<JournalReportResult> reportResultList = new ArrayList<JournalReportResult>();
		StringBuffer queryString = new StringBuffer("FROM VbJournal vb WHERE" +
				" vb.createdBy = :createdBy AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		}
		queryString.append(" AND vb.flag = :flag AND vb.status = :status");
		query = session.createQuery(queryString.toString())
				.setParameter("createdBy", salesWiseReportCommand.getSalesExecutive())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		query.setParameter("flag", new Integer(1));
		query.setParameter("status", "APPROVED");
		List<VbJournal> journalList = query.list();
		JournalReportResult  reportResult = null;
		for (VbJournal vbJournal : journalList) {
			reportResult = new JournalReportResult();
			reportResult.setCreatedOn(vbJournal.getCreatedOn());
			reportResult.setInvoiceNo(vbJournal.getInvoiceNo());
			reportResult.setTotalAmount(vbJournal.getAmount());
			reportResult.setJournalType(vbJournal.getJournalType());
			reportResult.setType("Journals");
			reportResult.setBusinessName(vbJournal.getBusinessName());
			reportResultList.add(reportResult);
		}
		session.close();
		return reportResultList;
		
	}


	/**
	 * This method is used to get the businessnames based on the vborganization.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllBusinessNames(String businessName , VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> businessNameList = session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("businessName"))
			    .add(Expression.eq("vbOrganization", organization))
			    .add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("businessName"))
			    .list();
		session.close();
		
		if(!businessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Business Name List: {}", businessNameList);
			}
			return businessNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is used to get the customer names based on the vborganization.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	/*@SuppressWarnings("unchecked")
	public List<String> getAllCustomerNames(String customerName , VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> businessNameList = session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("businessName"))
			    .add(Expression.eq("vbOrganization", organization))
			    .add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("businessName"))
			    .list();
		session.close();
		
		if(!businessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Business Name List: {}", businessNameList);
			}
			return businessNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}*/
	
	/**
	 * This method is used to get the customer names based on the vborganization.
	 * 
	 * @param customerName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	/*@SuppressWarnings("unchecked")
	public List<String> getAllCustomerNames(String customerName , VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> customerNameList = session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("customerName"))
			    .add(Expression.eq("vbOrganization", organization))
			    .add(Expression.like("customerName", customerName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("customerName"))
			    .list();
		session.close();
		
		if(!customerNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Business Name List: {}", customerNameList);
			}
			return customerNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}*/
	
	
	/**
	 * This method is used to get the region names based on the vborganization.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllRegionNames(String businessName , VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> regionNamesList = session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.property("region"))
			    .add(Expression.eq("customer.vbOrganization", organization))
			    .add(Expression.like("region", businessName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("region"))
			    .list();
		session.close();
		
		if(!regionNamesList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Region Name List: {}", regionNamesList);
			}
			return regionNamesList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is used to get the productNameList based on the businessName from
	 * VbDeliveryNote table.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return String - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCustomerProductName(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> productNameList = session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.distinct(Projections.property("productName")))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.list();
		session.close();
		
		if(!productNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Product Names associated with businessName are: {}", productNameList);
			}
			return productNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for productName: {}", productNameList);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	
	/**
	 * This method is used to get the customer region name based on the customerName from
	 * VbCustomer table for SLE Customer Wise Sales new Report.
	 * 
	 * @param customerName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return String - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public String getCustomerRegionName(String customerName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomerDetail vbCustomerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.customerName", customerName))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		session.close();
		String customerRegionName = null;
		if(vbCustomerDetail != null) {
			customerRegionName = vbCustomerDetail.getRegion();
			if (_logger.isDebugEnabled()) {
				_logger.debug("Region Name associated with Customer Name is: {}", customerRegionName);
			}
			return customerRegionName;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for customerName: {}", customerName);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible for getting all the product names from vbproduct.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	public List<String> getAllProductNames(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		List<String> productNameList = session.createQuery("SELECT vb.productName FROM VbProduct vb WHERE vb.vbOrganization = :organization GROUP BY vb.productName")
				.setParameter("organization", organization).list();
		session.close();
		if(!productNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Product Names are: {}", productNameList);
			}
			return productNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for productName: {}", productNameList);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible for getting all the sales executive names from VbEmployee.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllSalesExecutives(String businessName,VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> salesExecutiveList = null;
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization)).uniqueResult();
				//.add(Restrictions.eq("employeeType", "SLE")).uniqueResult();
		if(vbCustomer != null){
			salesExecutiveList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbEmployee", "employee")
					.setProjection(Projections.distinct(Projections.property("employee.username")))
					.add(Restrictions.eq("vbCustomer", vbCustomer))
					.add(Restrictions.eq("vbOrganization", organization))
					//.add(Restrictions.like("employee.username", businessName,MatchMode.START).ignoreCase())
					.addOrder(Order.asc("employee.username")).list();
		}else{
		 salesExecutiveList = session.createCriteria(VbEmployee.class)
		.setProjection(Projections.property("username"))
		.add(Restrictions.eq("vbOrganization", organization))
		.add(Restrictions.eq("employeeType", "SLE"))
		.list();
		}
		session.close();
		if(!salesExecutiveList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Sales Executives are: {}", salesExecutiveList);
			}
			return salesExecutiveList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for salesExecutive: {}", salesExecutiveList);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible for getting all the sales executive names from VbEmployee if customer name is not null for SLE Customer Sales Wise New Report.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllSalesExecutivesBasedOnCustomerName(String customerName,VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> salesExecutiveList = null;
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("customerName", customerName))
				.add(Restrictions.eq("vbOrganization", organization)).uniqueResult();
				//.add(Restrictions.eq("employeeType", "SLE")).uniqueResult();
		if(vbCustomer != null){
			salesExecutiveList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbEmployee", "employee")
					.setProjection(Projections.distinct(Projections.property("employee.username")))
					.add(Restrictions.eq("vbCustomer", vbCustomer))
					.add(Restrictions.eq("vbOrganization", organization))
					//.add(Restrictions.like("employee.username", businessName,MatchMode.START).ignoreCase())
					.addOrder(Order.asc("employee.username")).list();
		}else{
		 salesExecutiveList = session.createCriteria(VbEmployee.class)
		.setProjection(Projections.property("username"))
		.add(Restrictions.eq("vbOrganization", organization))
		.add(Restrictions.eq("employeeType", "SLE"))
		.list();
		}
		session.close();
		if(!salesExecutiveList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Sales Executives are: {}", salesExecutiveList);
			}
			return salesExecutiveList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for salesExecutive: {}", salesExecutiveList);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	/**
	 * This method is responsible for getting the assigned organizations for management user.
	 * 
	 * @param userName - {@link String}
	 * @return organizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllAssignedOrganizations(String userName) throws DataAccessException {
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		List<String> organizationList = null;
		if(employee != null) {
			organizationList = session.createCriteria(VbAssignOrganizations.class)
					.createAlias("vbOrganization", "organization")
					.setProjection(Projections.property("organization.name"))
					.add(Restrictions.eq("vbEmployee", employee))
					.list();
		}
		session.close();
		if(!organizationList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} organizations have been assigned for {}", organizationList.size(), userName);
			}
			return organizationList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for salesExecutive: {}", organizationList);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	/**
	 * This method is responsible for getting  all the  organizations .
	 * 
	 * @return organizationsList - {@link List}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllOrganizations() throws DataAccessException {
		Session session = this.getSession();
		List<String> organizationsList = session.createCriteria(VbOrganization.class)
				.setProjection(Projections.property("name"))
				.add(Restrictions.ne("id", 1)).list();
		session.close();
		if(!organizationsList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} organizations {}", organizationsList);
			}
			return organizationsList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found: {}", organizationsList);
			}
			throw new DataAccessException(errorMsg);
		}
		
	}

	/**
	 * This method is used to get {@link VbOrganization} based on organization name.
	 * 
	 * @param organizationName - {@link String}
	 * @return organization - {@link VbOrganization}
	 */
	public VbOrganization getOrganization(String organizationName) {
		Session session = this.getSession();
		VbOrganization organization = (VbOrganization) session.createCriteria(VbOrganization.class)
				.add(Restrictions.eq("name", organizationName))
				.uniqueResult();
		session.close();
		
		if(organization == null) {
			return null;
		}
		return organization;
	}

	/**
	 * This method is responsible to get the businessnames associated to particular sales executive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return businessNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllBusinessaNamesBasedSalesExecutive(String businessName, String salesExecutive, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> businessNameList = null;
		VbEmployee vbemployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("employeeType", "SLE")).uniqueResult();
		//if user wants to select all business names based on SLE
		if(vbemployee != null) {
			businessNameList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.distinct(Projections.property("customer.businessName")))
					.add(Restrictions.eq("vbEmployee", vbemployee))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.like("customer.businessName", businessName,MatchMode.START).ignoreCase())
					.addOrder(Order.asc("customer.businessName")).list();
			//else user wants to select SLE based on Business name selection
		}else{
			businessNameList = getAllBusinessNames(businessName,organization);
		}
		session.close();
		if(!businessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} business name have been assigned to {}",
						businessNameList.size(), salesExecutive);
			}
			return businessNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names have been assigned to: {}", salesExecutive);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to get the customer names associated to particular sales executive for SLE Customer wise sales report.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return businessNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllCustomerNamesBasedSalesExecutive(String businessName, String salesExecutive, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> customerBusinessNameList = null;
		VbEmployee vbemployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("employeeType", "SLE")).uniqueResult();
		//if user wants to select all business names based on SLE
		if(vbemployee != null) {
			customerBusinessNameList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.distinct(Projections.property("customer.businessName")))
					.add(Restrictions.eq("vbEmployee", vbemployee))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.like("customer.businessName", businessName,MatchMode.START).ignoreCase())
					.addOrder(Order.asc("customer.businessName")).list();
			//else user wants to select SLE based on Business name selection
		}else{
			customerBusinessNameList = getAllBusinessNames(businessName,organization);
		}
		session.close();
		if(!customerBusinessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} customer business name have been assigned to {}",
						customerBusinessNameList.size(), salesExecutive);
			}
			return customerBusinessNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names have been assigned to: {}", salesExecutive);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to get the region names associated to particular customer for SLE Customer wise sales report.
	 * 
	 * @param customerName - {@link String}
	 * @param salesExecutiveVal 
	 * @param organization - {@link VbOrganization}
	 * @return regionNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllRegionNamesBasedOnCustomerNameOrSalesExecutive(String businessName, String region, String salesExecutiveVal, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> regionNameList = new ArrayList<String>();
		VbCustomerDetail customerDetail = null;
		List<String> sleCustomerRegionsList =  new ArrayList<String>();
		
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutiveVal))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		//if user wants to get region for selected customer
		if(vbCustomer != null) {
			regionNameList = session.createCriteria(VbCustomerDetail.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.distinct(Projections.property("region")))
					.add(Restrictions.eq("customer.businessName", vbCustomer.getBusinessName()))
					.add(Restrictions.eq("customer.vbOrganization", organization))
					.addOrder(Order.asc("region")).list();
		}else if(vbEmployee != null){
			// Retrieve all assigned customer region names for this sales executive
			List<VbEmployeeCustomer> assignedCustomerList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbEmployee","employee")
					.add(Restrictions.eq("employee.id", vbEmployee.getId()))
					.add(Restrictions.eq("employee.vbOrganization", organization))
					.list();
			// if duplicate regions for assigned customer for this sle use distinct clause
			for(VbEmployeeCustomer employeeCustomer : assignedCustomerList){
				customerDetail = (VbCustomerDetail)session.createCriteria(VbCustomerDetail.class)
						.createAlias("vbCustomer", "customer")
						.add(Restrictions.eq("customer.businessName", employeeCustomer.getVbCustomer().getBusinessName()))
						.add(Restrictions.eq("customer.vbOrganization", organization))
						.uniqueResult();
				// region names list for all assigned customer for sales executive (remove duplicate region for multiple customer)
				if(!regionNameList.contains(customerDetail.getRegion())){
				regionNameList.add(customerDetail.getRegion());
				}
			}
		}
		//else user wants to get all region names registered with this system
		else{
			regionNameList = getAllRegionNames(businessName,organization);
		}
		session.close();
		if(!regionNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} customer name is belong to {}",
						regionNameList.size(), businessName);
			}
			return regionNameList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Region Names have been assigned to: {}", businessName);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to check the given business name availability.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailability - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean checkBusinessNameAvailability(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean isAvailability = Boolean.FALSE;
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				//.add(Restrictions.like("businessName", businessName, MatchMode.START))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(vbCustomer != null) {
			isAvailability = Boolean.TRUE;
			return isAvailability;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names available with : {}", businessName);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to check the given customer name availability.
	 * 
	 * @param customerName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailability - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean checkCustomerNameAvailability(String customerName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean isAvailability = Boolean.FALSE;
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("customerName", customerName))
				//.add(Restrictions.like("businessName", businessName, MatchMode.START))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(vbCustomer != null) {
			isAvailability = Boolean.TRUE;
			return isAvailability;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names available with : {}", customerName);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is used to check the businessname availability for particular salesexecutive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailability - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean checkBusinessNameAvailabilityBasedOnSalesExecutive(String salesExecutive, String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean isAvailability = Boolean.FALSE;
		VbEmployeeCustomer vbEmployeeCustomer = null;
		VbEmployee vbemployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("employeeType", "SLE")).uniqueResult();
		if(vbemployee != null) {
			vbEmployeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.add(Restrictions.eq("vbEmployee", vbemployee))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("customer.businessName", businessName))
					.uniqueResult();
							
		}
		session.close();
		if(vbEmployeeCustomer != null) {
			isAvailability = Boolean.TRUE;
			return isAvailability;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names available with : {}", salesExecutive);
			}
			throw new DataAccessException(errorMsg);
		}
	}
	/**
	 * This method is used to check the sales executive availability.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailability - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Boolean checkSalesExecutiveAvailability(String salesExecutive, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Boolean isAvailability = Boolean.FALSE;
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("employeeType", "SLE"))
				.add(Restrictions.eq("username", salesExecutive))
				.uniqueResult();
		session.close();
		if(vbEmployee != null) {
			isAvailability = Boolean.TRUE;
			return isAvailability;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error("No Business Names available with : {}", salesExecutive);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 *  This method is responsible for getting the batch numbers based on the product.
	 * 
	 * @param productName - {@link String}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return batchNumberList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBatchNumberAssociatedWithProduct(String productName,	String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> batchNumberList = session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.distinct(Projections.property("batchNumber")))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.list();
		session.close();
		
		if(!batchNumberList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("BatchNumbers associated with productName are: {}", batchNumberList);
			}
			return batchNumberList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for productName: {}", batchNumberList);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get the Sales Return Quantity related data.
	 * 
	 * @param salesReturnReportsCommand - {@link ReportsCommand}
	 * @param organization - {@link VbOrganization}
	 * @return resultList - {@link SalesReturnQtyReportResult}
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnQtyReportResult> getSalesReturnReportsData(ReportsCommand salesReturnReportsCommand,
			VbOrganization organization) {
		Session session = this.getSession();
		Boolean addBusinessName = Boolean.FALSE;
		Boolean addProductName = Boolean.FALSE;
		Boolean addBatchNumber = Boolean.FALSE;
		Boolean addStatus = Boolean.FALSE;
		Boolean addResalableQuantity = Boolean.FALSE;
		Boolean addDamagedQuantity = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(salesReturnReportsCommand.getStartDate());
		Date eDate = salesReturnReportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = salesReturnReportsCommand.getSalesExecutive();
		String businessName = salesReturnReportsCommand.getBusinessName();
		String productName = salesReturnReportsCommand.getProductName();
		String batchNumber = salesReturnReportsCommand.getBatchNumber();
		String status = salesReturnReportsCommand.getCrStatus();
		String resalableOperator = salesReturnReportsCommand.getResalableOperator();
		Integer resalableQty = salesReturnReportsCommand.getResalableQty();
		String damagedOperator = salesReturnReportsCommand.getDamagedOperator();
		Integer damagedQty = salesReturnReportsCommand.getDamagedQty();
		String reportType = salesReturnReportsCommand.getReportType();
		
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbSalesReturnProducts vb WHERE vb.vbSalesReturn.createdBy = :salesExecutive");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbSalesReturn.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		}
		if(!("ALL".equals(productName))) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = Boolean.TRUE;
		}
		if(!("ALL".equals(batchNumber))) {
			queryString.append(" AND vb.batchNumber = :batchNumber");
			addBatchNumber = Boolean.TRUE;
		}
		if(!("-1".equals(status))) {
			queryString.append(" AND vb.vbSalesReturn.status = :status");
			addStatus = Boolean.TRUE;
		} 
		if(!("-1").equals(resalableOperator)) {
			queryString.append(" AND vb.resalable " + resalableOperator +" :resalableQty");
			addResalableQuantity = Boolean.TRUE;
		}
		if(!("-1".equals(damagedOperator))) {
			queryString.append(" AND vb.damaged " + damagedOperator +" :damagedQty");
			addDamagedQuantity = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbSalesReturn.vbOrganization = :vbOrganization AND vb.vbSalesReturn.createdOn BETWEEN :startDate AND :endDate AND vb.vbSalesReturn.flag = :flag");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("salesExecutive", salesExecutive);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		}
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		if(addBatchNumber) {
			query.setParameter("batchNumber", batchNumber);
		}
		if(addStatus) {
			query.setParameter("status", status);
		}
		if(addResalableQuantity) {
			query.setParameter("resalableQty", resalableQty);
		}
		if(addDamagedQuantity) {
			query.setParameter("damagedQty", damagedQty);
		}
		query.setParameter("vbOrganization", organization);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("flag", new Integer(1));
		List<VbSalesReturnProducts> productsList = query.list();
		List<SalesReturnQtyReportResult> resultList = new ArrayList<SalesReturnQtyReportResult>();
		SalesReturnQtyReportResult reportResult = null;
		VbSalesReturn salesReturn = null;
		for (VbSalesReturnProducts vbSalesReturnProduct : productsList) {
			reportResult = new SalesReturnQtyReportResult();
			salesReturn = vbSalesReturnProduct.getVbSalesReturn();
			reportResult.setCreatedBy(salesReturn.getCreatedBy());
			reportResult.setBusinessName(salesReturn.getBusinessName());
			reportResult.setProductName(vbSalesReturnProduct.getProductName());
			reportResult.setBatchNumber(vbSalesReturnProduct.getBatchNumber());
			reportResult.setStatus(salesReturn.getStatus());
			reportResult.setResalableQty(vbSalesReturnProduct.getResalable());
			reportResult.setDamagedQty(vbSalesReturnProduct.getDamaged());
			reportResult.setApprovedBy(salesReturn.getModifiedBy());
			reportResult.setApprovedDate(salesReturn.getModifiedOn());
			reportResult.setCreatedDate(salesReturn.getCreatedOn());
			reportResult.setTotalQty(vbSalesReturnProduct.getTotalQty());
			reportResult.setInvoiceNo(salesReturn.getInvoiceNo());
			resultList.add(reportResult);
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", productsList.size());
		}
		return resultList;
	}

	/**
	 * @param salesExecutiveExpenditureReportsCommand 
	 * @param organization
	 * @return
	 */
	public List<SalesExecutiveExpenditureReportResult> getSalesExecutiveExpenditureReportsData(ReportsCommand salesExecutiveExpenditureReportsCommand,
			VbOrganization organization) {

		Session session = this.getSession();
		Boolean addAllowanceType = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(salesExecutiveExpenditureReportsCommand.getStartDate());
		Date eDate = salesExecutiveExpenditureReportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = salesExecutiveExpenditureReportsCommand.getSalesExecutive();
		String allowanceType = salesExecutiveExpenditureReportsCommand.getAllowanceType();
		String reportType = salesExecutiveExpenditureReportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("SELECT vb.dayBookType, SUM(vb.valueOne), vb.valueThree, vb.createdOn FROM VbCashDayBook vb WHERE vb.createdBy = :createdBy AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(allowanceType != null && !allowanceType.equalsIgnoreCase("all")) {
			queryString.append(" AND vb.dayBookType = :dayBookType");
			addAllowanceType = Boolean.TRUE;
		} else {
			queryString.append(" AND vb.dayBookType != :dayBookType1 AND vb.dayBookType != :dayBookType2 AND vb.dayBookType != :dayBookType3");
		}
		queryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY vb.dayBookType,vb.createdOn,vb.valueThree  ORDER BY vb.createdOn, vb.dayBookType");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("createdBy", salesExecutive);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addAllowanceType) {
			query.setParameter("dayBookType", allowanceType);
		} else {
			query.setParameter("dayBookType1", "Vehicle Details");
			query.setParameter("dayBookType2", "Deposit Amount");
			query.setParameter("dayBookType3", "Amount to Bank");
		}
		query.setParameter("vbOrganization", organization);
		List<Object[]> allowanceTypeList = query.list();
		session.close();
		
		List<SalesExecutiveExpenditureReportResult> expenditureList = new ArrayList<SalesExecutiveExpenditureReportResult>();
		SalesExecutiveExpenditureReportResult reportResult = null;
		for (Object[] allowance : allowanceTypeList) {
			reportResult = new SalesExecutiveExpenditureReportResult();
			reportResult.setExpensesType((String)allowance[0]);
			reportResult.setAmount(Float.parseFloat((allowance[1].toString())));
			reportResult.setDetails((String)allowance[2]);
			reportResult.setCreatedOn((Date)allowance[3]);
			
			expenditureList.add(reportResult);
			
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", expenditureList.size());
		}
		return expenditureList;
	
	}

	/**
	 * @param salesExecutiveSalesWiseReportsCommand
	 * @param organization
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SalesExecutiveSalesWiseReportResult> getSalesExecutiveSalesWiseReportsData(ReportsCommand salesExecutiveSalesWiseReportsCommand,
			VbOrganization organization) {
		
		Session session = this.getSession();
		Boolean addBusinessName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(salesExecutiveSalesWiseReportsCommand.getStartDate());
		Date eDate = salesExecutiveSalesWiseReportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = salesExecutiveSalesWiseReportsCommand.getSalesExecutive();
		String businessName = salesExecutiveSalesWiseReportsCommand.getBusinessName();
		String reportType = salesExecutiveSalesWiseReportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNote vb WHERE vb.createdBy = :createdBy AND vb.flag = :flag AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(businessName != null && !businessName.equalsIgnoreCase("")) {
			queryString.append(" AND vb.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		} 
		queryString.append(" AND vb.vbOrganization = :vbOrganization ORDER BY vb.createdOn ASC");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("createdBy", salesExecutive);
		query.setParameter("flag", new Integer(1));
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		} 
		query.setParameter("vbOrganization", organization);
		List<VbDeliveryNote> deliveryNoteList = query.list();
		
		List<SalesExecutiveSalesWiseReportResult> deliveryNoteReportResultList = new ArrayList<SalesExecutiveSalesWiseReportResult>();
		SalesExecutiveSalesWiseReportResult reportResult = null;
		VbDeliveryNotePayments vbDeliveryNotePayments = null;
		List<VbDeliveryNoteProducts> vbDeliveryNoteProductsList = null;
		Float presentPayable = new Float(0);
		Float previousCredit = new Float(0);
		Float total = new Float(0);
		Float totalReceived = new Float(0);
		String businessNameVal = null;
		String locality = null;
		for (VbDeliveryNote deliveryNote : deliveryNoteList) {
			businessNameVal = deliveryNote.getBusinessName();
			vbDeliveryNotePayments = new ArrayList<VbDeliveryNotePayments>(deliveryNote.getVbDeliveryNotePaymentses()).get(0);
			vbDeliveryNoteProductsList = new ArrayList<VbDeliveryNoteProducts>(deliveryNote.getVbDeliveryNoteProductses());
			presentPayable = vbDeliveryNotePayments.getPresentPayable();
			previousCredit = vbDeliveryNotePayments.getPreviousCredit();
			total = presentPayable + previousCredit;
			totalReceived = vbDeliveryNotePayments.getPresentPayment();
			locality = (String) session.createCriteria(VbCustomerDetail.class)
					.setProjection(Projections.property("locality"))
					.createAlias("vbCustomer", "customer")
					.add(Restrictions.eq("customer.businessName", businessNameVal))
					.add(Restrictions.eq("customer.vbOrganization", organization))
					.uniqueResult();
			if(!vbDeliveryNoteProductsList.isEmpty()) {
				for (VbDeliveryNoteProducts vbDeliveryNoteProducts : vbDeliveryNoteProductsList) {
					reportResult = new SalesExecutiveSalesWiseReportResult();
					
					reportResult.setCreatedOn(DateUtils.format(deliveryNote.getCreatedOn()));
					reportResult.setBusinessName(businessNameVal);
					reportResult.setLocality(locality);
					reportResult.setSoldValue(presentPayable);
					reportResult.setOldBalance(previousCredit);
					reportResult.setTotal(total);
					reportResult.setTotalReceived(totalReceived);
					reportResult.setCredit(total - totalReceived);
					reportResult.setProductName(vbDeliveryNoteProducts.getProductName());
					reportResult.setProductQty(vbDeliveryNoteProducts.getProductQty());
					reportResult.setInvoiceNo(deliveryNote.getInvoiceNo());
					
					deliveryNoteReportResultList.add(reportResult);
				}
			} else {
				reportResult = new SalesExecutiveSalesWiseReportResult();
				
				reportResult.setCreatedOn(DateUtils.format(deliveryNote.getCreatedOn()));
				reportResult.setBusinessName(businessNameVal);
				reportResult.setLocality(locality);
				reportResult.setSoldValue(presentPayable);
				reportResult.setOldBalance(previousCredit);
				reportResult.setTotal(total);
				reportResult.setTotalReceived(totalReceived);
				reportResult.setCredit(total - totalReceived);
				reportResult.setProductName("NA");
				reportResult.setProductQty(new Integer(0));
				reportResult.setInvoiceNo(deliveryNote.getInvoiceNo());
				
				deliveryNoteReportResultList.add(reportResult);
			}
		}
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", deliveryNoteReportResultList.size());
		}
		return deliveryNoteReportResultList;
	}
	
	/**
	 * @param salesExecutiveSalesWiseReportsCommand
	 * @param organization
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VbDeliveryNotePayments> getSalesExecutiveSalesWiseSubReportsData(ReportsCommand salesExecutiveSalesWiseReportsCommand,
			VbOrganization organization) {
		Session session = this.getSession();
		Boolean addBusinessName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(salesExecutiveSalesWiseReportsCommand.getStartDate());
		Date eDate = salesExecutiveSalesWiseReportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = salesExecutiveSalesWiseReportsCommand.getSalesExecutive();
		String businessName = salesExecutiveSalesWiseReportsCommand.getBusinessName();
		String reportType = salesExecutiveSalesWiseReportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getEndTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getEndTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNote vb WHERE vb.createdBy = :createdBy AND vb.flag = :flag AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(businessName != null && !businessName.equalsIgnoreCase("")) {
			queryString.append(" AND vb.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		} 
		queryString.append(" AND vb.vbOrganization = :vbOrganization ORDER BY vb.createdOn ASC");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("createdBy", salesExecutive);
		query.setParameter("flag", new Integer(1));
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		} 
		query.setParameter("vbOrganization", organization);
		List<VbDeliveryNote> deliveryNoteList = query.list();
		
		List<VbDeliveryNotePayments> reportResultList = new ArrayList<VbDeliveryNotePayments>();
		for (VbDeliveryNote deliveryNote : deliveryNoteList) {
			reportResultList.add(new ArrayList<VbDeliveryNotePayments>(deliveryNote.getVbDeliveryNotePaymentses()).get(0));
		}
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", reportResultList);
		}
		return reportResultList;
	}
	
	// Dao Methods for New Reports Criteria
	
	/**
	 * This method is responsible for fetching all Factory Product Wise Report Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<FactoryProductWiseReportResult> getFactoryProductWiseReportData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		List<FactoryProductWiseReportResult> reportResultList = new ArrayList<FactoryProductWiseReportResult>();
		FactoryProductWiseReportResult reportResult = null;
		String quantityTypeProduction = "Arrived";
		String noDispatchNote = "NA";
		String noSalesExecutive = "NA";
		
		StringBuffer productionQueryString = new StringBuffer();
		Query productionQuery = null; 
		List productsList = null;
		String productionCreatedOn =null;
		Integer production = new Integer(0);
		Object[] productionObjectArray = null;
		
		StringBuffer salesBookQueryString = new StringBuffer();
 		Query salesBookQuery = null;
 		List salesBookList = null;
 		Object[] salesBookProductObjectArray = null;
 		String salesBookCreatedOn = null;
 		String dispatchNote = null;
		String salesExecutiveName = null;
		Integer dispatch = new Integer(0);
		Integer returnToFactory = new Integer(0);
 		
 		StringBuffer productsToFactoryQueryString = new StringBuffer();
 		Query productsToFactoryQuery = null;
 		List productsToFactoryList = null;
 		Object[] productsToFactoryObjectArray = null;
 		String returnSalesExecutiveName = null;
		String returnDispatchNote = null;
		String returnSalesBookCreatedOn = null;
		Integer returnProductsToFactory = new Integer(0);
		
		String productionCreatedOnEndTimeStamp = null;
		String salesBookCreatedOnEndTimeStamp = null;
		Integer dateComparision = null;
 		
 		
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
 		String reportType = reportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		
		String removeStartDateTimeStamp = DateUtils.format1(startDate);
		String removeEndDateTimeStamp = DateUtils.format1(endDate);
		
		
		// Query for retrieve arrived product(productionCreatedOn,arrivedQty) from warehouse to factory.
		productionQueryString = new StringBuffer("SELECT SUBSTRING(vb.createdOn,1,10), SUM(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType AND SUBSTRING(vb.createdOn,1,10) BETWEEN :startDate AND :endDate");
		productionQueryString.append(" AND vb.productName LIKE :productName");
		productionQueryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY SUBSTRING(vb.createdOn,1,10)");
		productionQuery = session.createQuery(productionQueryString.toString());
		productionQuery.setParameter("startDate", removeStartDateTimeStamp);
		productionQuery.setParameter("endDate", removeEndDateTimeStamp);
		productionQuery.setParameter("productName", productName);
		productionQuery.setParameter("quantityType", quantityTypeProduction);
		productionQuery.setParameter("vbOrganization", organization);
 		productsList = productionQuery.list();
 		
 	    // Query for Retrieve SalesBook(salesExecutive,salesBookNumber,salesBookCreatedOn,allottedQty) details for Arrived Products.
		salesBookQueryString = new StringBuffer("SELECT vs.salesExecutive,vs.salesBookNo,SUBSTRING(vsa.createdOn,1,10),SUM(vsa.qtyAllotted) FROM VbSalesBook vs,VbSalesBookAllotments vsa WHERE SUBSTRING(vsa.createdOn,1,10) BETWEEN :startDate AND :endDate");
		salesBookQueryString.append(" AND vsa.productName = :productName");
		salesBookQueryString.append(" AND vs.vbOrganization = :vbOrganization");
		salesBookQueryString.append(" AND vsa.qtyAllotted != 0");
		salesBookQueryString.append(" AND vs.id = vsa.vbSalesBook GROUP BY vs.salesExecutive,vs.salesBookNo, SUBSTRING (vsa.createdOn,1,10) ORDER BY  vs.createdOn");
		salesBookQuery = session.createQuery(salesBookQueryString.toString());
		salesBookQuery.setParameter("startDate", removeStartDateTimeStamp);
		salesBookQuery.setParameter("endDate", removeEndDateTimeStamp);
		salesBookQuery.setParameter("productName", productName);
		salesBookQuery.setParameter("vbOrganization", organization);
		salesBookList = salesBookQuery.list();
		
		// Query for calculating Products To Factory Qty for same (salesExecutive,salesBookNo.,createdOn,productsToFactory) of Products from customer
		productsToFactoryQueryString = new StringBuffer("SELECT vs.salesExecutive,vs.salesBookNo,SUBSTRING(vdb.createdOn,1,10),SUM(vdbp.productsToFactory) FROM VbSalesBook vs,VbDayBook vdb,VbDayBookProducts vdbp WHERE vdbp.productName LIKE :productName ");
		productsToFactoryQueryString.append("AND SUBSTRING(vdb.createdOn,1,10) BETWEEN :startDate AND :endDate ");
		productsToFactoryQueryString.append("AND vs.id=vdb.vbSalesBook ");
		productsToFactoryQueryString.append("AND vdb.id=vdbp.vbDayBook ");
		productsToFactoryQueryString.append("AND vs.vbOrganization = :vbOrganization ");
		productsToFactoryQueryString.append("AND vdbp.productsToFactory != 0 GROUP BY vs.salesExecutive,vs.salesBookNo,SUBSTRING(vdb.createdOn,1,10) ORDER BY SUBSTRING(vdb.createdOn,1,10)");
		productsToFactoryQuery = session.createQuery(productsToFactoryQueryString.toString());
		productsToFactoryQuery.setParameter("startDate", removeStartDateTimeStamp);
		productsToFactoryQuery.setParameter("endDate", removeEndDateTimeStamp);
		productsToFactoryQuery.setParameter("productName", productName);
		productsToFactoryQuery.setParameter("vbOrganization", organization);
		productsToFactoryList = productsToFactoryQuery.list();
	
		Iterator productionIterator = productsList.iterator();
		Iterator salesBookIterator = salesBookList.iterator();
		Iterator productsToFactoryIterator = productsToFactoryList.iterator();
		
		String productionDate = null;
		String salesBookDate = null;
		String returnToFactoryDate = null;
		List<String> productionDispatchReturnDateList = new ArrayList<String>();
		if(!productsList.isEmpty()){
 			while(productionIterator.hasNext()){
 				productionObjectArray = (Object[])productionIterator.next();
 				productionDate = (String)productionObjectArray[0];
 				productionDispatchReturnDateList.add(productionDate);
 		}
 	}
 		if(!salesBookList.isEmpty()){
 			while(salesBookIterator.hasNext()){
 				salesBookProductObjectArray = (Object[])salesBookIterator.next();
 				salesBookDate = (String)salesBookProductObjectArray[2];
 				productionDispatchReturnDateList.add(salesBookDate);
 		}
 	}
 		if(!productsToFactoryList.isEmpty()){
 			while(productsToFactoryIterator.hasNext()){
 				productsToFactoryObjectArray = (Object[])productsToFactoryIterator.next();
 				returnToFactoryDate = (String)productsToFactoryObjectArray[2];
 				productionDispatchReturnDateList.add(returnToFactoryDate);
 		}
 	}
 		
 		// convert list to set for removal of all duplicate elements(dates) from list
 		Set<String> productionDispatchReturnDateSet = new HashSet<String>(productionDispatchReturnDateList);
		
 		StringBuffer returnToFactoryQueryString = new StringBuffer();
 		Query returnToFactoryQuery = null;
 		List returnToFactoryList = null;
 		Integer dispatchQty = new Integer(0);
 		List<String> salesExecutiveList = new ArrayList<String>();
 		List<String> dispatchNoteList = new ArrayList<String>();
 		
 		Integer returnToFactoryListSize = new Integer(0);
 		Integer productionListSize = new Integer(0);
 		Integer salesBookListSize = new Integer(0);
		if(!productsList.isEmpty() || !salesBookList.isEmpty() || !productsToFactoryList.isEmpty()){
			returnToFactoryListSize = productsToFactoryList.size();
			productionListSize = productsList.size();
			salesBookListSize = salesBookList.size();
			
			for(String productionAllottedReturnDate : productionDispatchReturnDateSet){
			reportResult = new FactoryProductWiseReportResult();
			
			// check production for this product in specific date
			boolean	isFound = false;
				if(!productsList.isEmpty()){
	 				   for(Object productionObject : productsList){
	 				   productionObjectArray = (Object[])productionObject;
	 				   productionCreatedOn = (String)productionObjectArray[0];
	 				   production = (Integer)productionObjectArray[1];
		 			   
		 			   if(productionAllottedReturnDate.equalsIgnoreCase(productionCreatedOn)){
		 				 reportResult.setCreatedOn(productionAllottedReturnDate);
		 				 reportResult.setProduction(production);
			 			 reportResult.setSalesExecutive(noSalesExecutive);
			 			 reportResult.setDispatchNote(noDispatchNote);
			 			 reportResult.setDispatch(new Integer(0));
			 			 reportResult.setSalesReturnQty(0);
			 			 isFound = true;
		 				 break;
		 			   }else{
		 				  // product name is not arrived with specify date
		 				 reportResult.setCreatedOn("");
		 				 reportResult.setProduction(new Integer(0));
		 			   }
	 				}
	 			}else{
	 				reportResult.setProduction(new Integer(0));
	 			}
 		// check Dispatch for this product in specific date
 			if(!salesBookList.isEmpty()){
 				   for(Object salesBookObject : salesBookList){
 					  FactoryProductWiseReportResult salesBookReportResult = new FactoryProductWiseReportResult();
 					    salesBookProductObjectArray = (Object[]) salesBookObject;
 						salesExecutiveName = (String)salesBookProductObjectArray[0];
 						dispatchNote = (String)salesBookProductObjectArray[1];
 						salesBookCreatedOn = (String)salesBookProductObjectArray[2];
 						dispatch = (Integer)salesBookProductObjectArray[3];
	 			   if(productionAllottedReturnDate.equalsIgnoreCase(salesBookCreatedOn)){
	 				// production arrived Quantity
	 				salesBookReportResult.setCreatedOn(productionAllottedReturnDate);   
	 				salesBookReportResult.setSalesExecutive(salesExecutiveName);
	 				salesBookReportResult.setDispatchNote(dispatchNote);
	 				salesBookReportResult.setDispatch(dispatch);
	 				if(productionCreatedOn != null){
	 				if(productionCreatedOn.equalsIgnoreCase(salesBookCreatedOn)){
	 				    salesBookReportResult.setProduction(production);
	 				}else{
	 					salesBookReportResult.setProduction(new Integer(0));
	 				}
	 				}else{
	 					salesBookReportResult.setProduction(new Integer(0));
	 				}
	 			// check Return To factory for this product in specific date
	 	 			if(!productsToFactoryList.isEmpty()){
	 	 				   for(Object returnToFactoryObject : productsToFactoryList){
	 	 					    productsToFactoryObjectArray = (Object[]) returnToFactoryObject;
	 	 					    returnSalesExecutiveName = (String)productsToFactoryObjectArray[0];
	 	 						returnDispatchNote = (String)productsToFactoryObjectArray[1];
	 	 						returnSalesBookCreatedOn = (String)productsToFactoryObjectArray[2];
	 	 						returnProductsToFactory = (Integer)productsToFactoryObjectArray[3]; 
	 	 				   		
	 		 			   if(productionAllottedReturnDate.equalsIgnoreCase(returnSalesBookCreatedOn) && returnSalesExecutiveName.equalsIgnoreCase(salesExecutiveName) && returnDispatchNote.equalsIgnoreCase(dispatchNote)){
	 		 				 salesBookReportResult.setSalesReturnQty(returnProductsToFactory);
	 		 			   }else{
		 		 				 Integer salesReturnQty = salesBookReportResult.getSalesReturnQty();
		 		 				 if(salesReturnQty == null){
		 		 					salesBookReportResult.setSalesReturnQty(new Integer(0)); 
		 		 				 }else{
		 		 					salesBookReportResult.setSalesReturnQty(salesReturnQty); 
		 		 				 }
		 		 			   }
	 	 				 }
	 	 			}else{
	 	 				salesBookReportResult.setSalesReturnQty(new Integer(0));
	 	 			} 
	 			   }
	 			  if(productionAllottedReturnDate.equalsIgnoreCase(salesBookCreatedOn)) {
	 			  reportResultList.add(salesBookReportResult);  
	 			  }
 				   }
 			}else{
 				reportResult.setSalesExecutive(noSalesExecutive);
 				reportResult.setDispatchNote(noDispatchNote);
 				reportResult.setDispatch(new Integer(0));
 				reportResult.setCreatedOn("");
 			}
 			 
			 if(productsToFactoryList.isEmpty()){
 				reportResult.setSalesReturnQty(new Integer(0));
 			}
			
			 if(isFound) reportResultList.add(reportResult);
		}
			// Query for check only if product is returned no arrived no dispatch.
			   for(Object salesBookObjects : salesBookList){
				salesBookProductObjectArray = (Object[]) salesBookObjects;
				salesExecutiveName = (String)salesBookProductObjectArray[0];
				dispatchNote = (String)salesBookProductObjectArray[1];
				salesBookCreatedOn = (String)salesBookProductObjectArray[2];
				dispatch = (Integer)salesBookProductObjectArray[3];
				salesExecutiveList.add(salesExecutiveName);
				dispatchNoteList.add(dispatchNote);
			 }
			 for(Object returnToFactoryObject : productsToFactoryList){
				    productsToFactoryObjectArray = (Object[]) returnToFactoryObject;
				    returnSalesExecutiveName = (String)productsToFactoryObjectArray[0];
					returnDispatchNote = (String)productsToFactoryObjectArray[1];
					returnSalesBookCreatedOn = (String)productsToFactoryObjectArray[2];
					returnProductsToFactory = (Integer)productsToFactoryObjectArray[3]; 
				
				if(!salesExecutiveList.contains(returnSalesExecutiveName) && !dispatchNoteList.contains(returnDispatchNote)){
					FactoryProductWiseReportResult returnToFactoryResult = new FactoryProductWiseReportResult();
					if(productionCreatedOn != null){
		 				if(productionCreatedOn.equalsIgnoreCase(returnSalesBookCreatedOn)){
		 					returnToFactoryResult.setProduction(production);
		 				}else{
		 					returnToFactoryResult.setProduction(new Integer(0));
		 				}
		 				}else{
		 					returnToFactoryResult.setProduction(new Integer(0));
		 				}
	 				 returnToFactoryResult.setCreatedOn(returnSalesBookCreatedOn);
	 				 returnToFactoryResult.setDispatch(new Integer(0));
	 				 returnToFactoryResult.setDispatchNote(returnDispatchNote);
	 				 returnToFactoryResult.setSalesExecutive(returnSalesExecutiveName);
	 				 returnToFactoryResult.setSalesReturnQty(returnProductsToFactory);
	 				 reportResultList.add(returnToFactoryResult); 
				}else if(!salesExecutiveList.contains(returnSalesExecutiveName) || !dispatchNoteList.contains(returnDispatchNote)){
					FactoryProductWiseReportResult returnToFactoryResult = new FactoryProductWiseReportResult();
					if(productionCreatedOn != null){
		 				if(productionCreatedOn.equalsIgnoreCase(salesBookCreatedOn)){
		 					returnToFactoryResult.setProduction(production);
		 				}else{
		 					returnToFactoryResult.setProduction(new Integer(0));
		 				}
		 				}else{
		 					returnToFactoryResult.setProduction(new Integer(0));
		 				}
	 				 returnToFactoryResult.setCreatedOn(returnSalesBookCreatedOn);
	 				 returnToFactoryResult.setDispatch(new Integer(0));
	 				 returnToFactoryResult.setDispatchNote(returnDispatchNote);
	 				 returnToFactoryResult.setSalesExecutive(returnSalesExecutiveName);
	 				 returnToFactoryResult.setSalesReturnQty(returnProductsToFactory);
	 				 reportResultList.add(returnToFactoryResult); 
				}
		   }
		}else{
			// no matching records found
		}
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", reportResultList);
		}
		 Collections.sort(reportResultList, new Comparator<FactoryProductWiseReportResult>() {
		    @Override
		    public int compare(final FactoryProductWiseReportResult o1, final FactoryProductWiseReportResult o2) {
		        return o1.getCreatedOn().compareTo(o2.getCreatedOn());
		    }});
		 
		 return reportResultList;
	}
	/**
	 * This method is responsible for fetching all Factory Product Wise Report Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<FactoryProductWiseReportResult> getFactoryProductWiseStockReportData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		List<FactoryProductWiseReportResult> productList = new ArrayList<FactoryProductWiseReportResult>();
		FactoryProductWiseReportResult reportResult = null;
		String quantityTypeProduction = "Arrived";
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String reportType = reportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getEndTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getEndTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		
		String removeStartDateTimeStamp = DateUtils.format1(startDate);
		String removeEndDateTimeStamp = DateUtils.format1(endDate);
		
		// Variable Declarations
		StringBuffer productionSummarySumQueryString = new StringBuffer();
		Query productionSummarySumQuery = null;
		List productsSummarySumList = null;
		Iterator productionSummarySumIterator = null;
		Object[] productionSummarySumObjArray = null;
		String productionSummarySumProductName = null;
		Integer productionSummarySumQty = new Integer(0);
		Integer productArrivedQtySum = new Integer(0);
		
		
		StringBuffer productionQtyQueryString = new StringBuffer();
		Query productionQtyQuery = null;
		List productionQtyList = null;
		Iterator productionQtyIterator = null;
		Object[] productionQtyObjectArray = null;
		String produitionQtyProductName = null;
		Integer productionQty = new Integer(0);
		Integer productionQtyNotNull = new Integer(0);
		
		StringBuffer salesBookAllottedReturnQueryString = new StringBuffer();
		Query salesBookAllottedReturnQuery = null;
		List salesBookAllottedReturnList = null;
		Iterator salesBookAllottedIterator = null;
		Object[] salesBookAllottedReturnObjectArray = null;
		String salesBookAllottedReturnProductName = null;
		Integer salesBookQtyAllotted = new Integer(0);
		//Integer salesBookReturnToFactory = new Integer(0);
		Integer salesBookQtyAllottedNotNull = new Integer(0);
		//Integer salesBookReturnToFactoryNotNull = new Integer(0);
		
		
		StringBuffer salesBookAllottmentsQueryString = new StringBuffer();
		Query salesBookAllottmentsQuery = null;
		List salesBookAllottmentsList = null;
		Iterator salesBookAllottmentsIterator = null;
		Object[] salesBookAllottmentsObjectArray = null;
		String salesBookAllottmentsProductName = null;
		Integer salesBookAllotementsAllottted = new Integer(0);
		Integer salesBookAllotementsAllotttedNotNull = new Integer(0);
		
		StringBuffer salesBookReturnToFactoryQueryString = new StringBuffer();
		Query salesBookReturnToFactoryQuery = null;
		List salesBookReturnToFactoryList = null;
		Iterator salesBookReturnToFactoryIterator = null;
		Object[] salesBookReturnToFactoryObjectArray = null;
		String salesBookReturnToFactoryProductName = null;
		Integer salesBookReturnToFactory = new Integer(0);
		Integer salesBookReturnToFactoryNotNull = new Integer(0);
		
		
		
		Integer openingStockResultSum = new Integer(0);
		Integer openingStockPositiveSum = new Integer(0);
		
		
		//query for retrieving production sum for Factory wise product report summary.
		productionSummarySumQueryString = new StringBuffer("SELECT vb.productName,SUM(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType AND SUBSTRING(vb.createdOn,1,10) BETWEEN :startDate AND :endDate");
		productionSummarySumQueryString.append(" AND vb.productName = :productName");
		productionSummarySumQueryString.append(" AND vb.vbOrganization = :vbOrganization");
		productionSummarySumQuery = session.createQuery(productionSummarySumQueryString.toString());
		productionSummarySumQuery.setParameter("startDate", removeStartDateTimeStamp);
		productionSummarySumQuery.setParameter("endDate", removeEndDateTimeStamp);
		productionSummarySumQuery.setParameter("productName", productName);
		productionSummarySumQuery.setParameter("quantityType", quantityTypeProduction);
		productionSummarySumQuery.setParameter("vbOrganization", organization);
		productsSummarySumList = productionSummarySumQuery.list();
	      
		productionSummarySumIterator  = productsSummarySumList.iterator();
	      
	      if(!productsSummarySumList.isEmpty()){
	    	  while (productionSummarySumIterator.hasNext()){
	    		  productionSummarySumObjArray = (Object[]) productionSummarySumIterator.next();
	    		  productionSummarySumProductName = (String)productionSummarySumObjArray[0];
	    		  productionSummarySumQty = (Integer)productionSummarySumObjArray[1];
	    		  
	  			  if(productionSummarySumQty != null){
	  		    	  productArrivedQtySum = productionSummarySumQty;
	  		    }else{
	  		    	 productArrivedQtySum = new Integer(0);
	  		    }
	  		}
	     }else{
	    	 productArrivedQtySum = new Integer(0);
	     }
		
	      // Query for Retrieving Arrived Qty of Product on previous specific Date.
	      /*productionQtyQueryString  = new StringBuffer("SELECT vb.productName,SUM(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType AND vb.productName = :productName");
	      productionQtyQueryString.append(" AND SUBSTRING(vb.createdOn,1,10) < :startDate");
	      productionQtyQueryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY vb.productName");
	      productionQtyQuery = session.createQuery(productionQtyQueryString.toString());
	      productionQtyQuery.setParameter("startDate", removeStartDateTimeStamp);
	      productionQtyQuery.setParameter("quantityType", quantityTypeProduction);
	      productionQtyQuery.setParameter("productName", productName);
	      productionQtyQuery.setParameter("vbOrganization", organization);
	      productionQtyList = productionQtyQuery.list();*/
	      
	      productionQtyQueryString  = new StringBuffer("SELECT vb.productName,SUM(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType AND vb.productName = :productName");
	      productionQtyQueryString.append(" AND vb.createdOn < :startDate");
	      productionQtyQueryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY vb.productName");
	      productionQtyQuery = session.createQuery(productionQtyQueryString.toString());
	      productionQtyQuery.setParameter("startDate", removeStartDateTimeStamp);
	      productionQtyQuery.setParameter("quantityType", quantityTypeProduction);
	      productionQtyQuery.setParameter("productName", productName);
	      productionQtyQuery.setParameter("vbOrganization", organization);
	      productionQtyList = productionQtyQuery.list();
		
		
		  // query for Retrieving QtyAllotted and QtyToFactory from vb_sales_book and vb_sales_book_products on previous specific date.
	      /*salesBookAllottedReturnQueryString = new StringBuffer("SELECT vsp.productName,SUM(vsp.qtyAllotted),SUM(vsp.qtyToFactory) FROM VbSalesBook vs,VbSalesBookProducts vsp WHERE SUBSTRING(vs.createdOn,1,10) < :startDate");
	      salesBookAllottedReturnQueryString.append(" AND vsp.productName LIKE :productName");
	      salesBookAllottedReturnQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
	      salesBookAllottedReturnQueryString.append("AND vs.id = vsp.vbSalesBook GROUP BY vsp.productName ORDER BY vsp.productName");
	      salesBookAllottedReturnQuery = session.createQuery(salesBookAllottedReturnQueryString.toString());
	      salesBookAllottedReturnQuery.setParameter("startDate", removeStartDateTimeStamp);
	      salesBookAllottedReturnQuery.setParameter("productName", productName);
	      salesBookAllottedReturnQuery.setParameter("vbOrganization", organization);
	      salesBookAllottedReturnList = salesBookAllottedReturnQuery.list();*/
	      
	      
	      //query for Retrieving QtyAllotted from vb_sales_book and vb_sales_book_products on previous specific date.
	      salesBookAllottedReturnQueryString = new StringBuffer("SELECT vsa.productName,SUM(vsa.qtyAllotted) FROM VbSalesBook vs,VbSalesBookAllotments vsa WHERE vs.createdOn < :startDate");
	      salesBookAllottedReturnQueryString.append(" AND vsa.productName LIKE :productName");
	      salesBookAllottedReturnQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
	      salesBookAllottedReturnQueryString.append("AND vs.id = vsa.vbSalesBook GROUP BY vsa.productName ORDER BY vsa.productName");
	      salesBookAllottedReturnQuery = session.createQuery(salesBookAllottedReturnQueryString.toString());
	      salesBookAllottedReturnQuery.setParameter("startDate", removeStartDateTimeStamp);
	      salesBookAllottedReturnQuery.setParameter("productName", productName);
	      salesBookAllottedReturnQuery.setParameter("vbOrganization", organization);
	      salesBookAllottedReturnList = salesBookAllottedReturnQuery.list();

		  //query for QtyToFactory from vb_day_book and vb_day_book_products on previous specific data.
	      salesBookReturnToFactoryQueryString = new StringBuffer("Select vbp.productName,SUM(vbp.productsToFactory) FROM VbDayBook vb,VbDayBookProducts vbp WHERE vb.createdOn < :startDate");
          salesBookReturnToFactoryQueryString.append(" AND vbp.productName LIKE :productName");
          salesBookReturnToFactoryQueryString.append(" AND vb.vbOrganization = :vbOrganization ");
          salesBookReturnToFactoryQueryString.append("AND vb.id = vbp.vbDayBook GROUP BY vbp.productName ORDER BY vbp.productName");
          salesBookReturnToFactoryQuery = session.createQuery(salesBookReturnToFactoryQueryString.toString());
          salesBookReturnToFactoryQuery.setParameter("startDate", removeStartDateTimeStamp);
          salesBookReturnToFactoryQuery.setParameter("productName", productName);
          salesBookReturnToFactoryQuery.setParameter("vbOrganization", organization);
          salesBookReturnToFactoryList = salesBookReturnToFactoryQuery.list();

		  
		  
	      /*salesBookAllottmentsQueryString = new StringBuffer("SELECT vsa.productName,SUM(vsa.qtyAllotted) FROM VbSalesBook vs,VbSalesBookAllotments vsa WHERE vsa.productName = :productName");
		  if(reportType.equalsIgnoreCase("Daily")){
			  salesBookAllottmentsQueryString.append(" AND SUBSTRING(vsa.createdOn,1,10) < :startDate ");
		  }else{
			  salesBookAllottmentsQueryString.append(" AND SUBSTRING(vsa.createdOn,1,10) BETWEEN :startDate AND :endDate");
		 }
		  salesBookAllottmentsQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
		  salesBookAllottmentsQueryString.append("AND vs.id=vsa.vbSalesBook GROUP BY vsa.productName");
		  salesBookAllottmentsQuery = session.createQuery(salesBookAllottmentsQueryString.toString());*/
	      
	      salesBookAllottmentsQueryString = new StringBuffer("SELECT vsa.productName,SUM(vsa.qtyAllotted) FROM VbSalesBook vs,VbSalesBookAllotments vsa WHERE vsa.productName = :productName");
//		  if(reportType.equalsIgnoreCase("Daily")){
			  salesBookAllottmentsQueryString.append(" AND vs.createdOn < :startDate AND vsa.createdOn > :startDate");
//		  }else{
//			  salesBookAllottmentsQueryString.append(" AND SUBSTRING(vsa.createdOn,1,10) BETWEEN :startDate AND :endDate");
//		 }
		  salesBookAllottmentsQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
		  salesBookAllottmentsQueryString.append("AND vs.id=vsa.vbSalesBook GROUP BY vsa.productName");
		  salesBookAllottmentsQuery = session.createQuery(salesBookAllottmentsQueryString.toString());
//		  if(reportType.equalsIgnoreCase("Daily")){
			  salesBookAllottmentsQuery.setParameter("startDate", removeStartDateTimeStamp);
//		  }else{
//			  salesBookAllottmentsQuery.setParameter("startDate", removeStartDateTimeStamp);
//			  salesBookAllottmentsQuery.setParameter("endDate", removeEndDateTimeStamp);
//		  }
		  salesBookAllottmentsQuery.setParameter("productName", productName);
		  salesBookAllottmentsQuery.setParameter("vbOrganization", organization);
		  salesBookAllottmentsList = salesBookAllottmentsQuery.list();
		
		
		  productionQtyIterator = productionQtyList.iterator();
		  salesBookAllottedIterator = salesBookAllottedReturnList.iterator();
		  salesBookAllottmentsIterator = salesBookAllottmentsList.iterator();
		  salesBookReturnToFactoryIterator = salesBookReturnToFactoryList.iterator();
		
		if(!productionQtyList.isEmpty() && !salesBookAllottedReturnList.isEmpty()){
		while (productionQtyIterator.hasNext() || salesBookAllottedIterator.hasNext() || salesBookAllottmentsIterator.hasNext() || salesBookReturnToFactoryIterator.hasNext())
		{
			 reportResult = new FactoryProductWiseReportResult();
			 
			// if production sum is greater than zero then production sum should display in summary.
			 if(productArrivedQtySum > 0){
					reportResult.setProduction(productArrivedQtySum);
			}else{
				reportResult.setProduction(new Integer(0));
			}
			 // opening stock calculation for product
			 productionQtyObjectArray = (Object[]) productionQtyIterator.next();
			 produitionQtyProductName = (String)productionQtyObjectArray[0];
			 productionQty = (Integer)productionQtyObjectArray[1];
			 
			 if(productionQty != null){
				 productionQtyNotNull = productionQty;
			 }else{
				 productionQtyNotNull = new Integer(0);
			 }
			 
			 salesBookAllottedReturnObjectArray = (Object[]) salesBookAllottedIterator.next();
			 salesBookAllottedReturnProductName = (String)salesBookAllottedReturnObjectArray[0];
			 salesBookQtyAllotted = (Integer)salesBookAllottedReturnObjectArray[1];
			 //salesBookReturnToFactory = (Integer)salesBookAllottedReturnObjectArray[2];
			 
			 if(salesBookQtyAllotted != null){
				 salesBookQtyAllottedNotNull = salesBookQtyAllotted;
			 }else{
				 salesBookQtyAllottedNotNull = new Integer(0);
			 }	
			 
			 /*if(salesBookReturnToFactory != null){
				 salesBookReturnToFactoryNotNull = salesBookReturnToFactory;
			 }else{
				 salesBookReturnToFactoryNotNull = new Integer(0);
			 }*/
			 if(salesBookAllottmentsIterator.hasNext()){
				 
			 salesBookAllottmentsObjectArray = (Object[]) salesBookAllottmentsIterator.next();
			 salesBookAllottmentsProductName = (String)salesBookAllottmentsObjectArray[0];
			 salesBookAllotementsAllottted = (Integer)salesBookAllottmentsObjectArray[1];
			 }
			 
			 if(salesBookAllotementsAllottted != null){
				 salesBookAllotementsAllotttedNotNull = salesBookAllotementsAllottted;
			 }else{
				 salesBookAllotementsAllotttedNotNull = new Integer(0);
			 }
			 
			 if(salesBookReturnToFactoryIterator.hasNext()){
			 
				salesBookReturnToFactoryObjectArray = (Object[]) salesBookReturnToFactoryIterator.next();
				salesBookReturnToFactoryProductName = (String)salesBookReturnToFactoryObjectArray[0];
				salesBookReturnToFactory = (Integer)salesBookReturnToFactoryObjectArray[1];
			 }
			if(salesBookReturnToFactory != null){
				 salesBookReturnToFactoryNotNull = salesBookReturnToFactory;
			 }else{
				 salesBookReturnToFactoryNotNull = new Integer(0);
			 }
			
			 openingStockResultSum  = productionQtyNotNull - salesBookQtyAllotted + salesBookReturnToFactory + salesBookAllotementsAllottted;
				if(openingStockResultSum < 0){
					openingStockPositiveSum = -(openingStockResultSum);
				}else{
					openingStockPositiveSum = openingStockResultSum;
				}
				reportResult.setOpeningStock(openingStockPositiveSum);
				reportResult.setProduction(productArrivedQtySum);
				productList.add(reportResult);
		}
	}else{
		if(productArrivedQtySum > 0){
			 reportResult = new FactoryProductWiseReportResult();
			 reportResult.setProduction(productArrivedQtySum);
			 reportResult.setOpeningStock(new Integer(0));
			 productList.add(reportResult);
		}
	}
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} products have been found.", productList);
		}
		return productList;
	}
	
	/**
	 * This method is responsible for fetching all  Product Wise Sales Report Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductWiseSalesReportResult> getProductWiseSalesReportData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		Boolean addProductName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String reportType = reportsCommand.getReportType();
		String salesExecutiveName = reportsCommand.getSalesExecutive();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNote vb WHERE vb.invoiceNo like :invoiceNumber AND vb.createdBy = :created_by AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(productName != null && !productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbOrganization = :vbOrganization");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("created_by", salesExecutiveName);
		query.setParameter("invoiceNumber", "%DN%");
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		query.setParameter("vbOrganization", organization);
		List<VbDeliveryNote> deliveryNoteList = query.list();
	
		
		List<ProductWiseSalesReportResult> reportResultList = new ArrayList<ProductWiseSalesReportResult>();
		ProductWiseSalesReportResult reportResult = null;
		VbDeliveryNote deliveryNoteInstance = null;
		List<VbDeliveryNoteProducts> deliveryNoteProducts = null;
		List<VbDeliveryNotePayments> deliveryNotePayments = null;
		for (VbDeliveryNote deliveryNote : deliveryNoteList) {
			deliveryNoteInstance = (VbDeliveryNote) session.get(VbDeliveryNote.class, deliveryNote.getId()); 
			deliveryNoteProducts = new ArrayList<VbDeliveryNoteProducts>(deliveryNoteInstance.getVbDeliveryNoteProductses());
			deliveryNotePayments = new ArrayList<VbDeliveryNotePayments>(deliveryNoteInstance.getVbDeliveryNotePaymentses());
			Float totalSalesValue = new Float(0.0);
			Float spotCollection = new Float(0.0);
			Float currentSalesBalance = new Float(0.0);
			for(VbDeliveryNotePayments payments : deliveryNotePayments){
				totalSalesValue = payments.getPresentPayable();
				spotCollection = payments.getPresentPayment();
				currentSalesBalance = payments.getBalance();
			}
			for(VbDeliveryNoteProducts products : deliveryNoteProducts){
				reportResult = new ProductWiseSalesReportResult();
				reportResult.setInvoiceNumber(deliveryNote.getInvoiceNo());
				reportResult.setProductName(products.getProductName());
				reportResult.setSalesQty(products.getProductQty());
				reportResult.setSalesAmount(products.getTotalCost());
				reportResult.setTotalSalesValue(totalSalesValue);
				reportResult.setSpotCollection(spotCollection);
				reportResult.setCurrentSalesBalance(totalSalesValue - spotCollection);
				reportResultList.add(reportResult);
			}
		}
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", reportResultList.size());
		}
		return reportResultList;
	}

	/**
	 * This method is responsible for fetching all  Product Wise Sales Sub Report Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbDeliveryNotePayments> getProductWiseSalesSubReportData(ReportsCommand productWiseSalesReportCommand,
			VbOrganization organization) {
		Session session = this.getSession();
		Boolean addBusinessName = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(productWiseSalesReportCommand.getStartDate());
		Date eDate = productWiseSalesReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = productWiseSalesReportCommand.getSalesExecutive();
		String businessName = productWiseSalesReportCommand.getBusinessName();
		String reportType = productWiseSalesReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNote vb WHERE vb.createdBy = :createdBy AND vb.flag = :flag AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(businessName != null && !businessName.equalsIgnoreCase("")) {
			queryString.append(" AND vb.businessName = :businessName");
			addBusinessName = Boolean.TRUE;
		} 
		queryString.append(" AND vb.vbOrganization = :vbOrganization ORDER BY vb.createdOn ASC");
		Query query = session.createQuery(queryString.toString());
		query.setParameter("createdBy", salesExecutive);
		query.setParameter("flag", new Integer(1));
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addBusinessName) {
			query.setParameter("businessName", businessName);
		} 
		query.setParameter("vbOrganization", organization);
		List<VbDeliveryNote> deliveryNoteList = query.list();
		
		List<VbDeliveryNotePayments> reportResultList = new ArrayList<VbDeliveryNotePayments>();
		for (VbDeliveryNote deliveryNote : deliveryNoteList) {
			reportResultList.add(new ArrayList<VbDeliveryNotePayments>(deliveryNote.getVbDeliveryNotePaymentses()).get(0));
		}
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", reportResultList);
		}
		return reportResultList;
	}
	/**
	 * This method is responsible for fetching all SalesExecutive Customer Wise Sales Related Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<SLECustomerWiseSalesResult> getSLECustomerWiseSalesReportData(ReportsCommand sleCustomerWiseSalesReportCommand, VbOrganization organization) {
		Session session = this.getSession();
		List<SLECustomerWiseSalesResult> salesExecutiveSalesResultList = new ArrayList<SLECustomerWiseSalesResult>();
		SLECustomerWiseSalesResult salesExecutiveSalesResult = null;
		Boolean addSalesExecutive = Boolean.FALSE;
		Boolean addCustomer = Boolean.FALSE; 
		Boolean addRegion = Boolean.FALSE; 
		Date startDate = DateUtils.getStartTimeStamp(sleCustomerWiseSalesReportCommand.getStartDate());
		Date eDate = sleCustomerWiseSalesReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String customerName = null;
		String salesExecutive = sleCustomerWiseSalesReportCommand.getSalesExecutive();
		String reportType = sleCustomerWiseSalesReportCommand.getReportType();
		String customerBusinessName = sleCustomerWiseSalesReportCommand.getBusinessName();
		String regionName = sleCustomerWiseSalesReportCommand.getRegion();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		// Query for Calculating Sales Executive Sales Value,Spot Collection,Cash Collection
		// Retrieve customer name based on business name
		if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {		
			VbCustomer vbCustomer = (VbCustomer)session.createCriteria(VbCustomer.class)
							.add(Expression.eq("businessName", customerBusinessName))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
					if(vbCustomer != null){
						customerName = vbCustomer.getCustomerName();
					}else{
						if(_logger.isDebugEnabled()) {
							_logger.debug("{} Customer Name not Found for Business name", customerName);
						}
					}
			}
		// To display report only for selected region for sales executive 
		String customerRegionBusinessName = null;
		String customerRegionName = null;
		VbCustomer customerDetailsCustomerObject = null;
		VbCustomerDetail vbCustomerDetail = null;
		VbCustomer customerDetailsCustomer = null;
		// when multiple customer belong to same region for same selected Sales Executive
		List<String> customerBusinessNamesList = new ArrayList<String>();
		VbEmployee vbEmployee = null;
		if(regionName != null && !regionName.equalsIgnoreCase("") || customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {		
			//if more than one customer belongs to same region
			// filter customers assigned to selected sales executive and then for each customer(vbCustomer) set the business name based on region(vbCustomerDetail) 
			vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
					.add(Restrictions.eq("username", salesExecutive))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			
			if(vbEmployee != null){
				// Retrieve all assigned customer region names for this sales executive
				List<VbEmployeeCustomer> assignedCustomerList = session.createCriteria(VbEmployeeCustomer.class)
						.createAlias("vbEmployee","employee")
						.add(Restrictions.eq("employee.id", vbEmployee.getId()))
						.add(Restrictions.eq("employee.vbOrganization", organization))
						.list();
				if(!assignedCustomerList.isEmpty()){
				for(VbEmployeeCustomer employeeCustomer : assignedCustomerList){
					vbCustomerDetail = (VbCustomerDetail)session.createCriteria(VbCustomerDetail.class)
							.createAlias("vbCustomer", "customer")
							.add(Restrictions.eq("customer.businessName", employeeCustomer.getVbCustomer().getBusinessName()))
							.add(Restrictions.eq("customer.vbOrganization", organization))
							.uniqueResult();
					customerRegionName = vbCustomerDetail.getRegion();
					customerDetailsCustomerObject = (VbCustomer) session.get(VbCustomer.class,vbCustomerDetail.getVbCustomer().getId());
					// if we want details based on SLE , customer and region
					if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
						if(customerDetailsCustomerObject.getBusinessName().equalsIgnoreCase(customerBusinessName))
						customerRegionBusinessName = customerDetailsCustomerObject.getBusinessName();
					} 
					// if we want details only SLE , region (belongs to multiple customer)
					// check with SLE how many assigned customer business names and customer.businessName with region
					else if(regionName != null && !regionName.equalsIgnoreCase("")) {	
					 if(customerRegionName.equalsIgnoreCase(regionName)){
						 //customerRegionBusinessName = customerDetailsCustomerObject.getBusinessName();
						 customerBusinessNamesList.add(customerDetailsCustomerObject.getBusinessName());
					 }
					}
				}
			  }else{
					if(_logger.isDebugEnabled()) {
						_logger.debug("{} No Assigned Customer Found", salesExecutive);
					}
				}
			}else{
				if(_logger.isDebugEnabled()) {
					_logger.debug("{} Employee Name not Found", salesExecutive);
				}
			}
		}
		List<Object> deliveryNoteInvoiceQueryList = null;
		StringBuffer deliveryNoteInvoiceQueryString = new StringBuffer();
		Query deliveryNoteInvoiceQuery = null;
		
		List<Object> deliveryNoteInvoiceRegionQueryLists = new ArrayList<Object>();
		List<Object> deliveryNoteInvoiceRegionQueryList = null;
		
		if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("") && customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("") && regionName != null && !regionName.equalsIgnoreCase("")){
			deliveryNoteInvoiceQueryString = new StringBuffer("SELECT vdn.businessName,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn BETWEEN :startDate AND :endDate ");
			if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.createdBy LIKE :createdBy");
				addSalesExecutive = Boolean.TRUE;
			} 
			if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
				addCustomer = Boolean.TRUE;
			} 
			if(regionName != null && !regionName.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
				addRegion = Boolean.TRUE;
			} 
			deliveryNoteInvoiceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
			deliveryNoteInvoiceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.businessName");
			deliveryNoteInvoiceQuery = session.createQuery(deliveryNoteInvoiceQueryString.toString());
			deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
			deliveryNoteInvoiceQuery.setParameter("startDate", startDate);
			deliveryNoteInvoiceQuery.setParameter("endDate", endDate);
			if(addSalesExecutive) {
				deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
			} 
			if(addCustomer) {
				deliveryNoteInvoiceQuery.setParameter("businessName", customerBusinessName);
			} 
			if(addRegion) {
				deliveryNoteInvoiceQuery.setParameter("businessName", customerRegionBusinessName);
			} 
			deliveryNoteInvoiceQuery.setParameter("vbOrganization", organization);
		    deliveryNoteInvoiceQueryList = deliveryNoteInvoiceQuery.list();
		// when user will select only sales executive and region, should display all customer belongs to that region with all txn.
		}else if(regionName != null && !regionName.equalsIgnoreCase("") && salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
			for(String customerRegionBusinessNames : customerBusinessNamesList){
			deliveryNoteInvoiceQueryString = new StringBuffer("SELECT vdn.businessName,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn BETWEEN :startDate AND :endDate ");
			if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.createdBy LIKE :createdBy");
				addSalesExecutive = Boolean.TRUE;
			} 
			if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
				addCustomer = Boolean.TRUE;
			} 
			if(regionName != null && !regionName.equalsIgnoreCase("")) {
				deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
				addRegion = Boolean.TRUE;
			} 
			deliveryNoteInvoiceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
			deliveryNoteInvoiceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.businessName");
			deliveryNoteInvoiceQuery = session.createQuery(deliveryNoteInvoiceQueryString.toString());
			deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
			deliveryNoteInvoiceQuery.setParameter("startDate", startDate);
			deliveryNoteInvoiceQuery.setParameter("endDate", endDate);
			if(addSalesExecutive) {
				deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
			} 
			if(addCustomer) {
				deliveryNoteInvoiceQuery.setParameter("businessName", customerBusinessName);
			} 
			if(addRegion) {
				deliveryNoteInvoiceQuery.setParameter("businessName", customerRegionBusinessNames);
			} 
			deliveryNoteInvoiceQuery.setParameter("vbOrganization", organization);
			deliveryNoteInvoiceRegionQueryList = deliveryNoteInvoiceQuery.list();
			deliveryNoteInvoiceRegionQueryLists.addAll(deliveryNoteInvoiceRegionQueryList);
			}
		}else if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("") && customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")){
				deliveryNoteInvoiceQueryString = new StringBuffer("SELECT vdn.businessName,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn BETWEEN :startDate AND :endDate ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.createdBy LIKE :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
					addCustomer = Boolean.TRUE;
				} 
				if(regionName != null && !regionName.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
					addRegion = Boolean.TRUE;
				} 
				deliveryNoteInvoiceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
				deliveryNoteInvoiceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.businessName");
				deliveryNoteInvoiceQuery = session.createQuery(deliveryNoteInvoiceQueryString.toString());
				deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
				deliveryNoteInvoiceQuery.setParameter("startDate", startDate);
				deliveryNoteInvoiceQuery.setParameter("endDate", endDate);
				if(addSalesExecutive) {
					deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
				} 
				if(addCustomer) {
					deliveryNoteInvoiceQuery.setParameter("businessName", customerBusinessName);
				} 
				if(addRegion) {
					deliveryNoteInvoiceQuery.setParameter("businessName", customerRegionBusinessName);
				} 
				deliveryNoteInvoiceQuery.setParameter("vbOrganization", organization);
			    deliveryNoteInvoiceQueryList = deliveryNoteInvoiceQuery.list();
			}else{
				deliveryNoteInvoiceQueryString = new StringBuffer("SELECT vdn.businessName,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn BETWEEN :startDate AND :endDate ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.createdBy LIKE :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
					addCustomer = Boolean.TRUE;
				} 
				if(regionName != null && !regionName.equalsIgnoreCase("")) {
					deliveryNoteInvoiceQueryString.append(" AND vdn.businessName = :businessName");
					addRegion = Boolean.TRUE;
				} 
				deliveryNoteInvoiceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
				deliveryNoteInvoiceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.businessName");
				deliveryNoteInvoiceQuery = session.createQuery(deliveryNoteInvoiceQueryString.toString());
				deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
				deliveryNoteInvoiceQuery.setParameter("startDate", startDate);
				deliveryNoteInvoiceQuery.setParameter("endDate", endDate);
				if(addSalesExecutive) {
					deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
				} 
				if(addCustomer) {
					deliveryNoteInvoiceQuery.setParameter("businessName", customerBusinessName);
				} 
				if(addRegion) {
					deliveryNoteInvoiceQuery.setParameter("businessName", customerRegionBusinessName);
				} 
				deliveryNoteInvoiceQuery.setParameter("vbOrganization", organization);
			    deliveryNoteInvoiceQueryList = deliveryNoteInvoiceQuery.list();
			}
		
		
		//variable declaration
		StringBuffer salesReturnInvoiceQueryString = null;
		Query salesReturnInvoiceQuery = null;
		List<Object> salesReturnInvoiceQueryList = null;
		
		StringBuffer journalDiscountInvoiceQueryString = null; 
		Query journalDiscountInvoiceQuery = null; 
		List<Object> journalDiscountInvoiceQueryList = null;
		
		Object[] deliveryNoteInvoiceObjectArray = null;
		Object[] salesReturnInvoiceObjectArray = null;
		Object[] journalInvoiceObjectArray = null;
		
		Float salesValue = new Float(0.0);
		Float spotCollection = new Float(0.0);
		Float cashCollection = new Float (0.0);
		String customerBusinessNames = null;
		Float salesReturnValue = new Float(0.0);
		Float discountValue = new Float(0.0);
		String customerNames = null;
		
		if(deliveryNoteInvoiceRegionQueryLists.isEmpty()){
		if(!deliveryNoteInvoiceQueryList.isEmpty()){
			for(Object deliveryNoteInvoiceObject : deliveryNoteInvoiceQueryList){
				salesExecutiveSalesResult = new SLECustomerWiseSalesResult();
				deliveryNoteInvoiceObjectArray = (Object[])deliveryNoteInvoiceObject;
				customerBusinessNames = (String) deliveryNoteInvoiceObjectArray[0];
				// if business name selected by user
				if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
					salesExecutiveSalesResult.setCustomerName(customerName);
				}
				// else based on business name retrieve customer name
				else{
					VbCustomer vbCustomer = (VbCustomer)session.createCriteria(VbCustomer.class)
							.add(Expression.eq("businessName", customerBusinessNames))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
					if(vbCustomer != null){
						customerNames = vbCustomer.getCustomerName();
					}else{
						if(_logger.isDebugEnabled()) {
							_logger.debug("{} Customer Name not Found for Business name", customerBusinessName);
						}
					}
					salesExecutiveSalesResult.setCustomerName(customerNames);
				}
				salesValue = (Float)deliveryNoteInvoiceObjectArray[1];
				spotCollection = (Float)deliveryNoteInvoiceObjectArray[2];
				cashCollection = (Float)deliveryNoteInvoiceObjectArray[3];
				
				salesExecutiveSalesResult.setSalesValue(salesValue);
				salesExecutiveSalesResult.setSpotCollection(spotCollection);
				salesExecutiveSalesResult.setCashCollectionAmount(cashCollection);
				
				// Query for Sales Return to get Product Grand Total
				salesReturnInvoiceQueryString = new StringBuffer("SELECT vsr.businessName,SUM(vsr.productsGrandTotal) FROM VbSalesReturn vsr WHERE vsr.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					salesReturnInvoiceQueryString.append(" AND vsr.createdBy = :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				salesReturnInvoiceQueryString.append(" AND vsr.businessName = :businessName ");
				salesReturnInvoiceQueryString.append(" AND vsr.createdOn BETWEEN :startDate AND :endDate ");
				salesReturnInvoiceQueryString.append(" AND vsr.vbOrganization = :vbOrganization GROUP BY vsr.businessName");
				salesReturnInvoiceQuery = session.createQuery(salesReturnInvoiceQueryString.toString());
				salesReturnInvoiceQuery.setParameter("startDate", startDate);
				salesReturnInvoiceQuery.setParameter("endDate", endDate);
				salesReturnInvoiceQuery.setParameter("businessName", customerBusinessNames);
				salesReturnInvoiceQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					salesReturnInvoiceQuery.setParameter("createdBy", salesExecutive);
				} 
				
				salesReturnInvoiceQueryList = salesReturnInvoiceQuery.list();
				
				if(!salesReturnInvoiceQueryList.isEmpty()){
				for(Object salesReturnInvoiceObject : salesReturnInvoiceQueryList){
					salesReturnInvoiceObjectArray = (Object[])salesReturnInvoiceObject;
					salesReturnValue = (Float)salesReturnInvoiceObjectArray[1];
					salesExecutiveSalesResult.setSalesReturnValue(salesReturnValue);
				}
				}else{
					salesExecutiveSalesResult.setSalesReturnValue(new Float(0.0));
					if(_logger.isDebugEnabled()) {
		 				_logger.debug("{} Sales Executive Sales Return Value Not Found.", salesReturnInvoiceQueryList);
		 			}
				}
				
				// Query Returning Journal Discount Amount for Sales Executive Opening Balance Calculation
				
				journalDiscountInvoiceQueryString = new StringBuffer("SELECT vj.businessName,SUM(vj.amount) FROM VbJournal vj WHERE vj.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					journalDiscountInvoiceQueryString.append(" AND vj.createdBy = :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				journalDiscountInvoiceQueryString.append(" AND vj.createdOn BETWEEN :startDate AND :endDate ");
				journalDiscountInvoiceQueryString.append(" AND vj.businessName = :businessName  ");
				journalDiscountInvoiceQueryString.append(" AND vj.vbOrganization = :vbOrganization GROUP BY vj.businessName");
				journalDiscountInvoiceQuery = session.createQuery(journalDiscountInvoiceQueryString.toString());
				journalDiscountInvoiceQuery.setParameter("startDate", startDate);
				journalDiscountInvoiceQuery.setParameter("endDate", endDate);
				journalDiscountInvoiceQuery.setParameter("businessName", customerBusinessNames);
				journalDiscountInvoiceQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					journalDiscountInvoiceQuery.setParameter("createdBy", salesExecutive);
				} 
				journalDiscountInvoiceQueryList = journalDiscountInvoiceQuery.list();
				
				if(!journalDiscountInvoiceQueryList.isEmpty()){
					for(Object journalDiscountInvoiceObject : journalDiscountInvoiceQueryList){
						journalInvoiceObjectArray = (Object[])journalDiscountInvoiceObject;
						discountValue = (Float)journalInvoiceObjectArray[1];
						salesExecutiveSalesResult.setDiscountValue(discountValue);
					}
				}else{
					salesExecutiveSalesResult.setDiscountValue(new Float(0.0));
					if(_logger.isDebugEnabled()) {
		 				_logger.debug("{} Sales Executive Journal Amount Value Not Found.", journalDiscountInvoiceQueryList);
		 			}
				}
			// method for calculating salesExecutive Opening Balance before start Date by (Opening Balance = Opening Balance Part - Sales Return - Discount) 
			   Float salesExecutiveOpeningBalance = calculateSalesExecutiveCustomerOpeningBalance(session,customerBusinessNames,salesExecutive,startDate,organization); 
			   salesExecutiveSalesResult.setOpeningBalance(salesExecutiveOpeningBalance);
				//salesExecutiveSalesResult.setOpeningBalance(new Float(0.0));
				salesExecutiveSalesResultList.add(salesExecutiveSalesResult);
			}
			session.close();
			if(_logger.isDebugEnabled()) {
 				_logger.debug("{} Sales Executive Found.", salesExecutiveSalesResultList.size());
 			}
			return salesExecutiveSalesResultList;
		}
		}
		// if for one sales executive multiple customer with same region
		else{
			if(!deliveryNoteInvoiceRegionQueryLists.isEmpty()){
				for(Object deliveryNoteInvoiceObject : deliveryNoteInvoiceRegionQueryLists){
					salesExecutiveSalesResult = new SLECustomerWiseSalesResult();
					deliveryNoteInvoiceObjectArray = (Object[])deliveryNoteInvoiceObject;
					customerBusinessNames = (String) deliveryNoteInvoiceObjectArray[0];
					// if business name selected by user
					if(customerBusinessName != null && !customerBusinessName.equalsIgnoreCase("")) {
						salesExecutiveSalesResult.setCustomerName(customerName);
					}
					// else based on business name retrieve customer name
					else{
						VbCustomer vbCustomer = (VbCustomer)session.createCriteria(VbCustomer.class)
								.add(Expression.eq("businessName", customerBusinessNames))
								.add(Expression.eq("vbOrganization", organization))
								.uniqueResult();
						if(vbCustomer != null){
							customerNames = vbCustomer.getCustomerName();
						}else{
							if(_logger.isDebugEnabled()) {
								_logger.debug("{} Customer Name not Found for Business name", customerBusinessName);
							}
						}
						salesExecutiveSalesResult.setCustomerName(customerNames);
					}
					salesValue = (Float)deliveryNoteInvoiceObjectArray[1];
					spotCollection = (Float)deliveryNoteInvoiceObjectArray[2];
					cashCollection = (Float)deliveryNoteInvoiceObjectArray[3];
					
					salesExecutiveSalesResult.setSalesValue(salesValue);
					salesExecutiveSalesResult.setSpotCollection(spotCollection);
					salesExecutiveSalesResult.setCashCollectionAmount(cashCollection);
					
					// Query for Sales Return to get Product Grand Total
					salesReturnInvoiceQueryString = new StringBuffer("SELECT vsr.businessName,SUM(vsr.productsGrandTotal) FROM VbSalesReturn vsr WHERE vsr.status LIKE 'APPROVED' ");
					if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
						salesReturnInvoiceQueryString.append(" AND vsr.createdBy = :createdBy");
						addSalesExecutive = Boolean.TRUE;
					} 
					salesReturnInvoiceQueryString.append(" AND vsr.businessName = :businessName ");
					salesReturnInvoiceQueryString.append(" AND vsr.createdOn BETWEEN :startDate AND :endDate ");
					salesReturnInvoiceQueryString.append(" AND vsr.vbOrganization = :vbOrganization GROUP BY vsr.businessName");
					salesReturnInvoiceQuery = session.createQuery(salesReturnInvoiceQueryString.toString());
					salesReturnInvoiceQuery.setParameter("startDate", startDate);
					salesReturnInvoiceQuery.setParameter("endDate", endDate);
					salesReturnInvoiceQuery.setParameter("businessName", customerBusinessNames);
					salesReturnInvoiceQuery.setParameter("vbOrganization", organization);
					if(addSalesExecutive) {
						salesReturnInvoiceQuery.setParameter("createdBy", salesExecutive);
					} 
					
					salesReturnInvoiceQueryList = salesReturnInvoiceQuery.list();
					
					if(!salesReturnInvoiceQueryList.isEmpty()){
					for(Object salesReturnInvoiceObject : salesReturnInvoiceQueryList){
						salesReturnInvoiceObjectArray = (Object[])salesReturnInvoiceObject;
						salesReturnValue = (Float)salesReturnInvoiceObjectArray[1];
						salesExecutiveSalesResult.setSalesReturnValue(salesReturnValue);
					}
					}else{
						salesExecutiveSalesResult.setSalesReturnValue(new Float(0.0));
						if(_logger.isDebugEnabled()) {
			 				_logger.debug("{} Sales Executive Sales Return Value Not Found.", salesReturnInvoiceQueryList);
			 			}
					}
					
					// Query Returning Journal Discount Amount for Sales Executive Opening Balance Calculation
					
					journalDiscountInvoiceQueryString = new StringBuffer("SELECT vj.businessName,SUM(vj.amount) FROM VbJournal vj WHERE vj.status LIKE 'APPROVED' ");
					if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
						journalDiscountInvoiceQueryString.append(" AND vj.createdBy = :createdBy");
						addSalesExecutive = Boolean.TRUE;
					} 
					journalDiscountInvoiceQueryString.append(" AND vj.createdOn BETWEEN :startDate AND :endDate ");
					journalDiscountInvoiceQueryString.append(" AND vj.businessName = :businessName  ");
					journalDiscountInvoiceQueryString.append(" AND vj.vbOrganization = :vbOrganization GROUP BY vj.businessName");
					journalDiscountInvoiceQuery = session.createQuery(journalDiscountInvoiceQueryString.toString());
					journalDiscountInvoiceQuery.setParameter("startDate", startDate);
					journalDiscountInvoiceQuery.setParameter("endDate", endDate);
					journalDiscountInvoiceQuery.setParameter("businessName", customerBusinessNames);
					journalDiscountInvoiceQuery.setParameter("vbOrganization", organization);
					if(addSalesExecutive) {
						journalDiscountInvoiceQuery.setParameter("createdBy", salesExecutive);
					} 
					journalDiscountInvoiceQueryList = journalDiscountInvoiceQuery.list();
					
					if(!journalDiscountInvoiceQueryList.isEmpty()){
						for(Object journalDiscountInvoiceObject : journalDiscountInvoiceQueryList){
							journalInvoiceObjectArray = (Object[])journalDiscountInvoiceObject;
							discountValue = (Float)journalInvoiceObjectArray[1];
							salesExecutiveSalesResult.setDiscountValue(discountValue);
						}
					}else{
						salesExecutiveSalesResult.setDiscountValue(new Float(0.0));
						if(_logger.isDebugEnabled()) {
			 				_logger.debug("{} Sales Executive Journal Amount Value Not Found.", journalDiscountInvoiceQueryList);
			 			}
					}
				// method for calculating salesExecutive Opening Balance before start Date by (Opening Balance = Opening Balance Part - Sales Return - Discount) 
				   Float salesExecutiveOpeningBalance = calculateSalesExecutiveCustomerOpeningBalance(session,customerBusinessNames,salesExecutive,startDate,organization); 
				   salesExecutiveSalesResult.setOpeningBalance(salesExecutiveOpeningBalance);
					//salesExecutiveSalesResult.setOpeningBalance(new Float(0.0));
					salesExecutiveSalesResultList.add(salesExecutiveSalesResult);
				}
				session.close();
				if(_logger.isDebugEnabled()) {
	 				_logger.debug("{} Sales Executive Found.", salesExecutiveSalesResultList.size());
	 			}
				return salesExecutiveSalesResultList;
		}
	}	
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} Sales Executive  Not Found.", deliveryNoteInvoiceQueryList);
			}
		return salesExecutiveSalesResultList;
	}
	/**
	 * This method is responsible for calculating opening balance for Sales Executive Customer Wise Sales Report.
	 * 
	 * @param session - {@link Session}
	 * @param customerBusinessName - {@link String}
	 * @param reportsCommand - {@link ReportsCommand}
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	private Float calculateSalesExecutiveCustomerOpeningBalance(Session session, String customerBusinessName,String salesExecutive,  Date startDate,VbOrganization organization) {
		//variable declaration
				Object[] openingBalanceObjectArray = null;
				Object[] salesReturnObjectArray = null;
				Object[] journalObjectArray = null;
				Float openingBalancePart = new Float(0.0);
				Float salesReturnProductsGrandTotal = new Float(0.0);
				Float journalAmount = new Float(0.0);
				Float salesExecutiveOpeningBalance = new Float(0.0);
				Boolean addSalesExecutive = Boolean.FALSE;
				StringBuffer sleOpeningBalanceQueryString = null;
				Query sleOpeningBalanceQuery = null;
				List<Object> sleOpeningBalanceQueryList = null;
				StringBuffer salesReturnQueryString = null;
				Query salesReturnQuery = null;
				List<Object> salesReturnQueryList = null;
				StringBuffer journalDiscountQueryString = null;
				Query journalDiscountQuery = null;
				List<Object> journalDiscountQueryList = null;
				String businessName = null;
				Float salesValue = new Float(0.0);
				Float spotCollection = new Float(0.0);
				Float cashCollection = new Float(0.0);
				
				sleOpeningBalanceQueryString = new StringBuffer("SELECT vdn.businessName,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn < :startDate ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					sleOpeningBalanceQueryString.append(" AND vdn.createdBy LIKE :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				sleOpeningBalanceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
				sleOpeningBalanceQueryString.append(" AND vdn.businessName = :businessName ");
				sleOpeningBalanceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.businessName");
				sleOpeningBalanceQuery = session.createQuery(sleOpeningBalanceQueryString.toString());
				//sleOpeningBalanceQuery.setParameter("createdBy", salesExecutive);
				sleOpeningBalanceQuery.setParameter("startDate", startDate);
				if(addSalesExecutive) {
					sleOpeningBalanceQuery.setParameter("createdBy", salesExecutive);
				} 
				sleOpeningBalanceQuery.setParameter("businessName", customerBusinessName);
				sleOpeningBalanceQuery.setParameter("vbOrganization", organization);
				sleOpeningBalanceQueryList = sleOpeningBalanceQuery.list();
				
				// Query for Returning Sales Return for calculating SLE Opening Balance
				salesReturnQueryString = new StringBuffer("SELECT vsr.createdBy,SUM(vsr.productsGrandTotal) FROM VbSalesReturn vsr WHERE vsr.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					salesReturnQueryString.append(" AND vsr.createdBy LIKE :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				salesReturnQueryString.append(" AND vsr.createdOn < :startDate ");
				salesReturnQueryString.append(" AND vsr.businessName = :businessName ");
				salesReturnQueryString.append(" AND vsr.vbOrganization = :vbOrganization GROUP BY vsr.createdBy");
				salesReturnQuery = session.createQuery(salesReturnQueryString.toString());
				salesReturnQuery.setParameter("startDate", startDate);
				salesReturnQuery.setParameter("businessName", customerBusinessName);
				salesReturnQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					salesReturnQuery.setParameter("createdBy", salesExecutive);
				} 
				salesReturnQueryList = salesReturnQuery.list();
				
				// Query Returning Journal Discount Amount for Sales Executive Opening Balance Calculation
				
				journalDiscountQueryString = new StringBuffer("SELECT vj.createdBy,SUM(vj.amount) FROM VbJournal vj WHERE vj.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					journalDiscountQueryString.append(" AND vj.createdBy LIKE :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				journalDiscountQueryString.append(" AND vj.createdOn < :startDate ");
				journalDiscountQueryString.append(" AND vj.businessName = :businessName ");
				journalDiscountQueryString.append(" AND vj.vbOrganization = :vbOrganization GROUP BY vj.createdBy");
				journalDiscountQuery = session.createQuery(journalDiscountQueryString.toString());
				journalDiscountQuery.setParameter("startDate", startDate);
				journalDiscountQuery.setParameter("businessName", customerBusinessName);
				journalDiscountQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					journalDiscountQuery.setParameter("createdBy", salesExecutive);
				} 
				journalDiscountQueryList = journalDiscountQuery.list();
				
				
				if(!sleOpeningBalanceQueryList.isEmpty()){
		            for(Object openingBalanceObject : sleOpeningBalanceQueryList){
		            	openingBalanceObjectArray = (Object[])openingBalanceObject;
		            	businessName = (String)openingBalanceObjectArray[0];
		            	salesValue = (Float)openingBalanceObjectArray[1];
		            	spotCollection = (Float)openingBalanceObjectArray[2];
		            	cashCollection = (Float)openingBalanceObjectArray[3];
		            	
		            if(!salesReturnQueryList.isEmpty()){	
		               for(Object salesReturnObject : salesReturnQueryList){
		            	   salesReturnObjectArray  = (Object[])salesReturnObject;
		            	   salesReturnProductsGrandTotal = (Float)salesReturnObjectArray[1];
		               }
		            }else{
		            	salesReturnProductsGrandTotal = new Float(0.0);
		    			if(_logger.isDebugEnabled()) {
		     				_logger.debug("{} Sales Executive sales return Grand total Not Found.", salesReturnQueryList);
		     			}
		            }
		            if(!journalDiscountQueryList.isEmpty()){
		               for(Object journalObject : journalDiscountQueryList){
		            	   journalObjectArray = (Object[])journalObject;
		            	   journalAmount = (Float)journalObjectArray[1];
		               }
		            }else{
		            	journalAmount = new Float(0.0);
		    			if(_logger.isDebugEnabled()) {
		     				_logger.debug("{} Sales Executive Journal Amount Not Found.", journalDiscountQueryList);
		     			}
		            }
		            salesExecutiveOpeningBalance = salesValue - spotCollection - cashCollection - salesReturnProductsGrandTotal - journalAmount;
		            }
				}else{
					salesExecutiveOpeningBalance = salesValue - spotCollection - cashCollection - salesReturnProductsGrandTotal - journalAmount;
					if(_logger.isDebugEnabled()) {
		 				_logger.debug("{} Sales Executive Opening Balance Not Found.", sleOpeningBalanceQueryList);
		 			}
				}
				return salesExecutiveOpeningBalance;
	}

	/**
	 * This method is responsible for fetching all New Sales Executive Sales Wise Related Data.
	 * 
	 * @param salesExecutiveSalesReportCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 */
	public List<SalesExecutiveSalesReportResult> getSalesExecutiveSalesWiseReportData(ReportsCommand salesExecutiveSalesReportCommand,
			VbOrganization organization) {
		Session session = this.getSession();
		List<SalesExecutiveSalesReportResult> salesExecutiveSalesResultList = new ArrayList<SalesExecutiveSalesReportResult>();
		SalesExecutiveSalesReportResult salesExecutiveSalesResult = null;
		Boolean addSalesExecutive = Boolean.FALSE;
		Date startDate = DateUtils.getStartTimeStamp(salesExecutiveSalesReportCommand.getStartDate());
		Date eDate = salesExecutiveSalesReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = salesExecutiveSalesReportCommand.getSalesExecutive();
		String reportType = salesExecutiveSalesReportCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		// Query for Calculating Sales Executive Sales Value,Spot Collection,Cash Collection
		
		StringBuffer deliveryNoteInvoiceQueryString = new StringBuffer("SELECT vdn.createdBy,SUM(vdnp.presentPayable),SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end),SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn BETWEEN :startDate AND :endDate ");
		if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
			deliveryNoteInvoiceQueryString.append(" AND vdn.createdBy = :createdBy");
			addSalesExecutive = Boolean.TRUE;
		} 
		deliveryNoteInvoiceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
		deliveryNoteInvoiceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.createdBy");
		Query deliveryNoteInvoiceQuery = session.createQuery(deliveryNoteInvoiceQueryString.toString());
		//deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
		deliveryNoteInvoiceQuery.setParameter("startDate", startDate);
		deliveryNoteInvoiceQuery.setParameter("endDate", endDate);
		if(addSalesExecutive) {
			deliveryNoteInvoiceQuery.setParameter("createdBy", salesExecutive);
		} 
		deliveryNoteInvoiceQuery.setParameter("vbOrganization", organization);
		List<Object> deliveryNoteInvoiceQueryList = deliveryNoteInvoiceQuery.list();
		
		//variable declaration
		StringBuffer salesReturnInvoiceQueryString = null;
		Query salesReturnInvoiceQuery = null;
		List<Object> salesReturnInvoiceQueryList = null;
		
		StringBuffer journalDiscountInvoiceQueryString = null; 
		Query journalDiscountInvoiceQuery = null; 
		List<Object> journalDiscountInvoiceQueryList = null;
		
		Object[] deliveryNoteInvoiceObjectArray = null;
		Object[] salesReturnInvoiceObjectArray = null;
		Object[] journalInvoiceObjectArray = null;
		
		Float salesValue = new Float(0.0);
		Float spotCollection = new Float(0.0);
		Float cashCollection = new Float (0.0);
		String salesExecutiveNames = null;
		Float salesReturnValue = new Float(0.0);
		Float discountValue = new Float(0.0);
		
		if(!deliveryNoteInvoiceQueryList.isEmpty()){
			for(Object deliveryNoteInvoiceObject : deliveryNoteInvoiceQueryList){
				salesExecutiveSalesResult = new SalesExecutiveSalesReportResult();
				deliveryNoteInvoiceObjectArray = (Object[])deliveryNoteInvoiceObject;
				
				salesExecutiveNames = (String) deliveryNoteInvoiceObjectArray[0];
				salesValue = (Float)deliveryNoteInvoiceObjectArray[1];
				spotCollection = (Float)deliveryNoteInvoiceObjectArray[2];
				cashCollection = (Float)deliveryNoteInvoiceObjectArray[3];
				
				salesExecutiveSalesResult.setSalesExecutive(salesExecutiveNames);
				salesExecutiveSalesResult.setSalesValue(salesValue);
				salesExecutiveSalesResult.setSpotCollection(spotCollection);
				salesExecutiveSalesResult.setCashCollectionAmount(cashCollection);
				
				// Query for Sales Return to get Product Grand Total
				salesReturnInvoiceQueryString = new StringBuffer("SELECT vsr.createdBy,SUM(vsr.productsGrandTotal) FROM VbSalesReturn vsr WHERE vsr.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					salesReturnInvoiceQueryString.append(" AND vsr.createdBy = :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				salesReturnInvoiceQueryString.append(" AND vsr.createdOn BETWEEN :startDate AND :endDate ");
				salesReturnInvoiceQueryString.append(" AND vsr.vbOrganization = :vbOrganization GROUP BY vsr.createdBy");
				salesReturnInvoiceQuery = session.createQuery(salesReturnInvoiceQueryString.toString());
				salesReturnInvoiceQuery.setParameter("startDate", startDate);
				salesReturnInvoiceQuery.setParameter("endDate", endDate);
				salesReturnInvoiceQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					salesReturnInvoiceQuery.setParameter("createdBy", salesExecutiveNames);
				} 
				salesReturnInvoiceQueryList = salesReturnInvoiceQuery.list();
				
				if(!salesReturnInvoiceQueryList.isEmpty()){
				for(Object salesReturnInvoiceObject : salesReturnInvoiceQueryList){
					salesReturnInvoiceObjectArray = (Object[])salesReturnInvoiceObject;
					salesReturnValue = (Float)salesReturnInvoiceObjectArray[1];
					salesExecutiveSalesResult.setSalesReturnValue(salesReturnValue);
				}
				}else{
					salesExecutiveSalesResult.setSalesReturnValue(new Float(0.0));
					if(_logger.isDebugEnabled()) {
		 				_logger.debug("{} Sales Executive Sales Return Value Not Found.", salesReturnInvoiceQueryList);
		 			}
				}
				
				// Query Returning Journal Discount Amount for Sales Executive Opening Balance Calculation
				
				journalDiscountInvoiceQueryString = new StringBuffer("SELECT vj.createdBy,SUM(vj.amount) FROM VbJournal vj WHERE vj.status LIKE 'APPROVED' ");
				if(salesExecutive != null && !salesExecutive.equalsIgnoreCase("")) {
					journalDiscountInvoiceQueryString.append(" AND vj.createdBy = :createdBy");
					addSalesExecutive = Boolean.TRUE;
				} 
				journalDiscountInvoiceQueryString.append(" AND vj.createdOn BETWEEN :startDate AND :endDate ");
				journalDiscountInvoiceQueryString.append(" AND vj.vbOrganization = :vbOrganization GROUP BY vj.createdBy");
				journalDiscountInvoiceQuery = session.createQuery(journalDiscountInvoiceQueryString.toString());
				journalDiscountInvoiceQuery.setParameter("startDate", startDate);
				journalDiscountInvoiceQuery.setParameter("endDate", endDate);
				journalDiscountInvoiceQuery.setParameter("vbOrganization", organization);
				if(addSalesExecutive) {
					journalDiscountInvoiceQuery.setParameter("createdBy", salesExecutiveNames);
				} 
				journalDiscountInvoiceQueryList = journalDiscountInvoiceQuery.list();
				
				if(!journalDiscountInvoiceQueryList.isEmpty()){
					for(Object journalDiscountInvoiceObject : journalDiscountInvoiceQueryList){
						journalInvoiceObjectArray = (Object[])journalDiscountInvoiceObject;
						discountValue = (Float)journalInvoiceObjectArray[1];
						salesExecutiveSalesResult.setDiscountValue(discountValue);
					}
				}else{
					salesExecutiveSalesResult.setDiscountValue(new Float(0.0));
					if(_logger.isDebugEnabled()) {
		 				_logger.debug("{} Sales Executive Journal Amount Value Not Found.", journalDiscountInvoiceQueryList);
		 			}
				}
			// method for calculating salesExecutive Opening Balance before start Date by (Opening Balance = Opening Balance Part - Sales Return - Discount) 
			   Float salesExecutiveOpeningBalance = calculateSalesExecutiveOpeningBalance(session,salesExecutiveNames,startDate,organization); 
			   salesExecutiveSalesResult.setOpeningBalance(salesExecutiveOpeningBalance);
			   salesExecutiveSalesResultList.add(salesExecutiveSalesResult);
			}
			session.close();
			if(_logger.isDebugEnabled()) {
 				_logger.debug("{} Sales Executive Found.", salesExecutiveSalesResultList.size());
 			}
			return salesExecutiveSalesResultList;
		}else{
			if(_logger.isDebugEnabled()) {
 				_logger.debug("{} Sales Executive  Not Found.", deliveryNoteInvoiceQueryList);
 			}
			return salesExecutiveSalesResultList;
		}
	}
	/**
	 * This method is responsible for Calculating Opening Balance of Sales executive.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 * @throws DataAccessException 
	 */
	private Float calculateSalesExecutiveOpeningBalance(Session session,String salesExecutiveNames, Date startDate,VbOrganization organization) {
		//variable declaration
		Object[] openingBalanceObjectArray = null;
		Object[] salesReturnObjectArray = null;
		Object[] journalObjectArray = null;
		Float openingBalancePart = new Float(0.0);
		Float salesReturnProductsGrandTotal = new Float(0.0);
		Float journalAmount = new Float(0.0);
		Float salesExecutiveOpeningBalance = new Float(0.0);
		Boolean addSalesExecutive = Boolean.FALSE;
		StringBuffer sleOpeningBalanceQueryString = null;
		Query sleOpeningBalanceQuery = null;
		List<Object> sleOpeningBalanceQueryList = null;
		StringBuffer salesReturnQueryString = null;
		Query salesReturnQuery = null;
		List<Object> salesReturnQueryList = null;
		StringBuffer journalDiscountQueryString = null;
		Query journalDiscountQuery = null;
		List<Object> journalDiscountQueryList = null;
		
		sleOpeningBalanceQueryString = new StringBuffer("SELECT vdn.createdBy,SUM(CASE when vdn.invoiceNo LIKE '%/DN/%' THEN vdnp.presentPayment else 0 end)+SUM(CASE when vdn.invoiceNo LIKE '%/COLLECTIONS/%' then vdnp.presentPayment else 0 end) FROM VbDeliveryNote vdn,VbDeliveryNotePayments vdnp WHERE vdn.createdOn < :startDate ");
		if(salesExecutiveNames != null && !salesExecutiveNames.equalsIgnoreCase("")) {
			sleOpeningBalanceQueryString.append(" AND vdn.createdBy = :createdBy");
			addSalesExecutive = Boolean.TRUE;
		} 
		sleOpeningBalanceQueryString.append(" AND vdn.vbOrganization = :vbOrganization ");
		sleOpeningBalanceQueryString.append(" AND vdn.id = vdnp.vbDeliveryNote GROUP BY vdn.createdBy");
		sleOpeningBalanceQuery = session.createQuery(sleOpeningBalanceQueryString.toString());
		sleOpeningBalanceQuery.setParameter("createdBy", salesExecutiveNames);
		sleOpeningBalanceQuery.setParameter("startDate", startDate);
		if(addSalesExecutive) {
			sleOpeningBalanceQuery.setParameter("createdBy", salesExecutiveNames);
		} 
		sleOpeningBalanceQuery.setParameter("vbOrganization", organization);
		sleOpeningBalanceQueryList = sleOpeningBalanceQuery.list();
		
		// Query for Returning Sales Return for calculating SLE Opening Balance
		salesReturnQueryString = new StringBuffer("SELECT vsr.createdBy,SUM(vsr.productsGrandTotal) FROM VbSalesReturn vsr WHERE vsr.status LIKE 'APPROVED' ");
		if(salesExecutiveNames != null && !salesExecutiveNames.equalsIgnoreCase("")) {
			salesReturnQueryString.append(" AND vsr.createdBy = :createdBy");
			addSalesExecutive = Boolean.TRUE;
		} 
		salesReturnQueryString.append(" AND vsr.createdOn < :startDate ");
		salesReturnQueryString.append(" AND vsr.vbOrganization = :vbOrganization GROUP BY vsr.createdBy");
		salesReturnQuery = session.createQuery(salesReturnQueryString.toString());
		salesReturnQuery.setParameter("startDate", startDate);
		salesReturnQuery.setParameter("vbOrganization", organization);
		if(addSalesExecutive) {
			salesReturnQuery.setParameter("createdBy", salesExecutiveNames);
		} 
		salesReturnQueryList = salesReturnQuery.list();
		
		// Query Returning Journal Discount Amount for Sales Executive Opening Balance Calculation
		
		journalDiscountQueryString = new StringBuffer("SELECT vj.createdBy,SUM(vj.amount) FROM VbJournal vj WHERE vj.status LIKE 'APPROVED' ");
		if(salesExecutiveNames != null && !salesExecutiveNames.equalsIgnoreCase("")) {
			journalDiscountQueryString.append(" AND vj.createdBy = :createdBy");
			addSalesExecutive = Boolean.TRUE;
		} 
		journalDiscountQueryString.append(" AND vj.createdOn < :startDate ");
		journalDiscountQueryString.append(" AND vj.vbOrganization = :vbOrganization GROUP BY vj.createdBy");
		journalDiscountQuery = session.createQuery(journalDiscountQueryString.toString());
		journalDiscountQuery.setParameter("startDate", startDate);
		journalDiscountQuery.setParameter("vbOrganization", organization);
		if(addSalesExecutive) {
			journalDiscountQuery.setParameter("createdBy", salesExecutiveNames);
		} 
		journalDiscountQueryList = journalDiscountQuery.list();
		
		
		if(!sleOpeningBalanceQueryList.isEmpty()){
            for(Object openingBalanceObject : sleOpeningBalanceQueryList){
            	openingBalanceObjectArray = (Object[])openingBalanceObject;
            	openingBalancePart = (Float)openingBalanceObjectArray[1];
            	
            if(!salesReturnQueryList.isEmpty()){	
               for(Object salesReturnObject : salesReturnQueryList){
            	   salesReturnObjectArray  = (Object[])salesReturnObject;
            	   salesReturnProductsGrandTotal = (Float)salesReturnObjectArray[1];
               }
            }else{
            	salesReturnProductsGrandTotal = new Float(0.0);
    			if(_logger.isDebugEnabled()) {
     				_logger.debug("{} Sales Executive sales return Grand total Not Found.", salesReturnQueryList);
     			}
            }
            if(!journalDiscountQueryList.isEmpty()){
               for(Object journalObject : journalDiscountQueryList){
            	   journalObjectArray = (Object[])journalObject;
            	   journalAmount = (Float)journalObjectArray[1];
               }
            }else{
            	journalAmount = new Float(0.0);
    			if(_logger.isDebugEnabled()) {
     				_logger.debug("{} Sales Executive Journal Amount Not Found.", journalDiscountQueryList);
     			}
            }
               salesExecutiveOpeningBalance = openingBalancePart - salesReturnProductsGrandTotal - journalAmount;
            }
		}else{
			openingBalancePart = new Float(0.0);
			salesExecutiveOpeningBalance = openingBalancePart - salesReturnProductsGrandTotal - journalAmount;
			if(_logger.isDebugEnabled()) {
 				_logger.debug("{} Sales Executive Opening Balance Not Found.", sleOpeningBalanceQueryList);
 			}
		}
		return salesExecutiveOpeningBalance;
	}

	/**
	 * This method is responsible for fetching all New Product Report Data.
	 * 
	 * @param reportsCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<ProductReportResult> getProductReportWiseReportData(ReportsCommand reportsCommand, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<ProductReportResult> productReportResultList = new ArrayList<ProductReportResult>();
		ProductReportResult productReporteportResult = null;
		String quantityTypeProduction = "Arrived";
		String quantityTypeDispatch = "Allotted";
		String txnProductName = "";
		
		Integer openingStockSum = new Integer(0);
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
 		String reportType = reportsCommand.getReportType();
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		//variable Initialization
		StringBuffer salesBookDispatchReturnQueryString = new StringBuffer();
		StringBuffer salesReturnQueryString = new StringBuffer();
		List salesBookList = null;
		List salesReturnList = null;
		String productName = null;
		String salesBookProductName = null;
		Integer productionQty = new Integer(0);
		Integer dispatchQty = new Integer(0);
		Integer salesReturnQty = new Integer(0);
		String salesReturnProductName = null;
		
		Object[] productionObjectProductNameArray = null;
		Object[] salesBookObjectProductNameArray = null;
		Object[] salesReturnObjectProductNameArray = null;
		
		String removeStartDateTimeStamp = DateUtils.format1(startDate);
		String removeEndDateTimeStamp = DateUtils.format1(endDate);
		
		//query for productName and Arrived Quantity
		    StringBuffer productionQtyQueryString = new StringBuffer("SELECT vb.productName, sum(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType AND SUBSTRING(vb.createdOn,1,10) BETWEEN :startDate AND :endDate");
		    productionQtyQueryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY vb.productName ORDER BY vb.productName");
			Query productionQtyQuery = session.createQuery(productionQtyQueryString.toString());
			productionQtyQuery.setParameter("startDate", removeStartDateTimeStamp);
			productionQtyQuery.setParameter("endDate", removeEndDateTimeStamp);
			productionQtyQuery.setParameter("quantityType", quantityTypeProduction);
			productionQtyQuery.setParameter("vbOrganization", organization);
	 		List productsList = productionQtyQuery.list();
	 		
	 		//query for Dispatch and Return qty for product
	 		salesBookDispatchReturnQueryString = new StringBuffer("SELECT vsa.productName,sum(vsa.qtyAllotted) FROM VbSalesBookAllotments vsa,VbSalesBook vs WHERE SUBSTRING(vsa.createdOn,1,10) BETWEEN :startDate AND :endDate");
	 		salesBookDispatchReturnQueryString.append(" AND vs.id=vsa.vbSalesBook ");
	 		salesBookDispatchReturnQueryString.append(" AND vs.vbOrganization = :vbOrganization GROUP BY vsa.productName ORDER BY vsa.productName");
			Query salesBookDispatchReturnQuery = session.createQuery(salesBookDispatchReturnQueryString.toString());
			salesBookDispatchReturnQuery.setParameter("startDate", removeStartDateTimeStamp);
			salesBookDispatchReturnQuery.setParameter("endDate", removeEndDateTimeStamp);
			salesBookDispatchReturnQuery.setParameter("vbOrganization", organization);
			salesBookList = salesBookDispatchReturnQuery.list();
			
			// Query for Sales Return from vb_day_book_products table
			salesReturnQueryString = new StringBuffer("SELECT vdbp.productName,sum(vdbp.productsToFactory) FROM VbDayBook vdb, VbDayBookProducts vdbp WHERE vdb.id=vdbp.vbDayBook AND SUBSTRING(vdb.createdOn,1,10) BETWEEN :startDate AND :endDate");
			salesReturnQueryString.append(" AND vdb.vbOrganization = :vbOrganization GROUP BY vdbp.productName ORDER BY vdbp.productName");
			Query salesReturnQuery = session.createQuery(salesReturnQueryString.toString());
			salesReturnQuery.setParameter("startDate", removeStartDateTimeStamp);
			salesReturnQuery.setParameter("endDate", removeEndDateTimeStamp);
			salesReturnQuery.setParameter("vbOrganization", organization);
			salesReturnList = salesReturnQuery.list();
			
	 		Iterator productionIterator = productsList.iterator();
	 		Iterator salesBookIterator = salesBookList.iterator();
	 		Iterator salesReturnIterator = salesReturnList.iterator();
	 		
	 		Object[] productionObjectArray = null;
	 		Object[] dispatchArray = null;
	 		Object[] salesReturnArray = null;
	 		
	 		// iterate each above list and keep all product names in string list.
	 		List<String> arrivedAllottedProductNameList = new ArrayList<String>();
	 		if(!productsList.isEmpty()){
	 			while(productionIterator.hasNext()){
	 				productionObjectProductNameArray = (Object[])productionIterator.next();
	 				arrivedAllottedProductNameList.add((String)productionObjectProductNameArray[0]);
	 		}
	 	}
	 		if(!salesBookList.isEmpty()){
	 			while(salesBookIterator.hasNext()){
	 				salesBookObjectProductNameArray = (Object[])salesBookIterator.next();
	 				arrivedAllottedProductNameList.add((String)salesBookObjectProductNameArray[0]);
	 		}
	 	}
	 		if(!salesReturnList.isEmpty()){
	 			while(salesReturnIterator.hasNext()){
	 				salesReturnObjectProductNameArray = (Object[])salesReturnIterator.next();
	 				arrivedAllottedProductNameList.add((String)salesReturnObjectProductNameArray[0]);
	 		}
	 	}
	 		
	 		// convert list to set for removal of all duplicate elements from list
	 		Set<String> arrivedAllottedProductNameSet = new HashSet<String>(arrivedAllottedProductNameList);
	 		
	 		// if all list size is zero, product should not display in product report (No Matching Records Found)
	 		if(!productsList.isEmpty() || !salesBookList.isEmpty() || !salesReturnList.isEmpty()){
	 			// if at least product is arrived or dispatched
	 			
	 		for(String arrivedAllottedReturnProductName : arrivedAllottedProductNameSet)	{
	 			productReporteportResult = new ProductReportResult(); 
	 			
	 			// check productName in productionList
	 			if(!productsList.isEmpty()){
	 				
	 				   for(Object productionObject : productsList){
	 				   productionObjectArray = (Object[])productionObject;
	 				   productName = (String) productionObjectArray[0];
		 			   productionQty = (Integer) productionObjectArray[1];
		 			   
		 			   if(arrivedAllottedReturnProductName.equalsIgnoreCase(productName)){
		 				 productReporteportResult.setProductName(arrivedAllottedReturnProductName);
		 				 productReporteportResult.setProduction(productionQty);
		 				 break;
		 			   }else{
		 				  // product name is not arrived with specify date
		 				  productReporteportResult.setProductName(arrivedAllottedReturnProductName);
			 			  productReporteportResult.setProduction(new Integer(0));
		 			   }
	 				  }
	 			
	 			}else{
	 			// product list is empty (No product Arrived on specify date)
	 				productReporteportResult.setProductName(arrivedAllottedReturnProductName);
	 				productReporteportResult.setProduction(new Integer(0));
	 			}
	 			
	 			
	 		// check productName in salesBookList
	 			if(!salesBookList.isEmpty()){
	 				
	 				   for(Object salesBookObject : salesBookList){
	 					  dispatchArray = (Object[])salesBookObject;
	 					  salesBookProductName = (String) dispatchArray[0];
	 					  dispatchQty = (Integer) dispatchArray[1];
		 			   
		 			   if(arrivedAllottedReturnProductName.equalsIgnoreCase(salesBookProductName)){
		 				productReporteportResult.setProductName(arrivedAllottedReturnProductName);
		 				productReporteportResult.setDispatch(dispatchQty);
		 				break;
		 			   }else{
		 				  // product name is not dispatched with specify date
		 				 productReporteportResult.setProductName(arrivedAllottedReturnProductName);
		 				 productReporteportResult.setDispatch(new Integer(0));
		 			   }
	 				  }
	 				
	 			}else{
	 				 productReporteportResult.setProductName(arrivedAllottedReturnProductName);
	 				 productReporteportResult.setDispatch(new Integer(0));
	 				// product list is empty (No product dispatched on specify date)
	 			}
	 			
	 		// check productName in salesBookList
	 			if(!salesReturnList.isEmpty()){
	 				
	 				   for(Object salesReturnObject : salesReturnList){
	 					    salesReturnArray = (Object[])salesReturnObject;
							salesReturnProductName = (String)salesReturnArray[0];
							salesReturnQty = (Integer)salesReturnArray[1];
		 			   
		 			   if(arrivedAllottedReturnProductName.equalsIgnoreCase(salesReturnProductName)){
		 				 productReporteportResult.setProductName(arrivedAllottedReturnProductName);
		 				 productReporteportResult.setSalesReturnQty(salesReturnQty);
		 				 break;
		 			   }else{
		 				  // product name is not returned with specify date
		 				productReporteportResult.setProductName(arrivedAllottedReturnProductName);
		 				productReporteportResult.setSalesReturnQty(new Integer(0));
		 			   }
	 				  }
		
	 			}else{
	 				productReporteportResult.setProductName(arrivedAllottedReturnProductName);
	 				productReporteportResult.setSalesReturnQty(new Integer(0));
	 				// product list is empty (No product returned on specify date)
	 			}
	 			
	 			// Calculate Opening Stock for Individual product based on specify date
	 			openingStockSum = calculateOpeningStock(session,arrivedAllottedReturnProductName,startDate,endDate,reportType,quantityTypeProduction,organization,quantityTypeDispatch);
	 			productReporteportResult.setOpeningStock(openingStockSum);
				productReportResultList.add(productReporteportResult);
	 		 }// outer for loop	 		
	 		}// outer if loop	 
	 		session.close();

 			if(_logger.isDebugEnabled()) {
 				_logger.debug("{} records have been found.", productReportResultList.size());
 			}
 			return productReportResultList;
	}
	/**
	 * This method is responsible for calculation opening stock for all products.
	 * 
	 * @param session - {@link Session}
	 * @param startDate - {@link Date}
	 * @param quantityTypeProduction - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param quantityTypeDispatch - {@link String}
	 * @return openingStockSum - {@link Integer}
	 */
private Integer calculateOpeningStock(Session session,String productName,Date startDate,Date endDate,String reportType,String quantityTypeProduction, VbOrganization organization,String quantityTypeDispatch) {
	String removeStartDateTimeStamp = DateUtils.format1(startDate);
	String removeEndDateTimeStamp = DateUtils.format1(endDate);
	// query for calculating opening stock (Production + Dispatch - Return) for products
	StringBuffer openingStockQueryString = new StringBuffer("SELECT vb.productName,sum(vb.quantity) FROM VbProductInventoryTransaction vb WHERE vb.quantityType = :quantityType");
	openingStockQueryString.append(" AND vb.productName LIKE :productName");
	openingStockQueryString.append(" AND SUBSTRING(vb.createdOn,1,10) < :startDate");
	openingStockQueryString.append(" AND vb.vbOrganization = :vbOrganization GROUP BY vb.productName ORDER BY vb.productName");
	Query openingStockQuery = session.createQuery(openingStockQueryString.toString());
	openingStockQuery.setParameter("productName", productName);
	openingStockQuery.setParameter("startDate", removeStartDateTimeStamp);
	openingStockQuery.setParameter("quantityType", quantityTypeProduction);
	openingStockQuery.setParameter("vbOrganization", organization);
	List openingStockList = openingStockQuery.list();
	
	// query for production (Product Arrived Quantity)
	StringBuffer dispatchReturnQueryString = new StringBuffer("SELECT vsp.productName,SUM(vsp.qtyAllotted),SUM(vsp.qtyToFactory) FROM VbSalesBook vs,VbSalesBookProducts vsp WHERE SUBSTRING(vs.createdOn,1,10) < :startDate");
	dispatchReturnQueryString.append(" AND vsp.productName LIKE :productName");
	dispatchReturnQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
	dispatchReturnQueryString.append("AND vs.id = vsp.vbSalesBook GROUP BY vsp.productName ORDER BY vsp.productName");
	Query dispatchReturnQuery = session.createQuery(dispatchReturnQueryString.toString());
	dispatchReturnQuery.setParameter("startDate", removeStartDateTimeStamp);
	dispatchReturnQuery.setParameter("productName", productName);
	dispatchReturnQuery.setParameter("vbOrganization", organization);
	List dispatchReturnList = dispatchReturnQuery.list();
	
	
	// query for salesBook recent allottments from vb_sales_book_allottments.
		StringBuffer salesBookAllottmentsQueryString = new StringBuffer("SELECT vsa.productName,SUM(vsa.qtyAllotted) FROM VbSalesBook vs,VbSalesBookAllotments vsa WHERE SUBSTRING(vs.createdOn,1,10) < :startDate ");
		if(reportType.equalsIgnoreCase("Daily")){
			salesBookAllottmentsQueryString.append("AND SUBSTRING(vsa.createdOn,1,10) LIKE :startDate");
		}else{
			salesBookAllottmentsQueryString.append("AND SUBSTRING(vsa.createdOn,1,10) BETWEEN :startDate AND :endDate");
		}
		salesBookAllottmentsQueryString.append(" AND vsa.productName LIKE :productName");
		salesBookAllottmentsQueryString.append(" AND vs.vbOrganization = :vbOrganization ");
		salesBookAllottmentsQueryString.append("AND vs.id = vsa.vbSalesBook GROUP BY vsa.productName ORDER BY vsa.productName");
		Query salesBookAllottmentsQuery = session.createQuery(salesBookAllottmentsQueryString.toString());
		if(reportType.equalsIgnoreCase("Daily")){
			salesBookAllottmentsQuery.setParameter("startDate", removeStartDateTimeStamp);
		}else{
			salesBookAllottmentsQuery.setParameter("startDate", removeStartDateTimeStamp);
			salesBookAllottmentsQuery.setParameter("endDate", removeEndDateTimeStamp);
		}
		salesBookAllottmentsQuery.setParameter("productName", productName);
		salesBookAllottmentsQuery.setParameter("vbOrganization", organization);
		List salesBookAllottmentsList = salesBookAllottmentsQuery.list();
	
	
	Object[] productionObjectArray = null;
	Object[] dispatchReturnObjectAtrray = null;
	Integer productionStock = new Integer(0);
	Integer productionStockNotNull = new Integer(0);
	Integer dispatchStock = new Integer(0);
	Integer returnStock = new Integer(0);
	Integer returnStockNotNull = new Integer(0);
	Integer dispatchStockNotNull = new Integer(0);
	Integer openingStockSum = new Integer(0);
	Integer openingStockPositiveSum = new Integer(0);
	String productionProductName = null;
	String dispatchProductName = null;
	String salesBookProductName = null;
	Object[] salesBookAllottmentsObjectArray = null;
	Integer recentAllottments = new Integer(0);
	Integer recentAllottmentsNotNull = new Integer(0);
	
	Integer calculatePreviousQtyAllotted = new Integer(0);
	//calculate OpeningStock (Production - Dispatch - Return)
	
	Iterator openingStockIterator = openingStockList.iterator();
	Iterator dispatchReturnIterator = dispatchReturnList.iterator();
	Iterator salesBookAllottmentsIterator = salesBookAllottmentsList.iterator();
	
	
	if(!openingStockList.isEmpty() || !dispatchReturnList.isEmpty() || !salesBookAllottmentsList.isEmpty()){
		while(openingStockIterator.hasNext() || dispatchReturnIterator.hasNext() || salesBookAllottmentsIterator.hasNext()){
			if(!openingStockList.isEmpty() && !dispatchReturnList.isEmpty()){
				if(openingStockIterator.hasNext()){
					productionObjectArray = (Object[])openingStockIterator.next();
					productionProductName = (String)productionObjectArray[0];
					productionStock = (Integer)productionObjectArray[1];
				  if(productionStock == null){
					  productionStockNotNull = new Integer(0);
					}else{
						productionStockNotNull = productionStock;
					}
				}
				if(dispatchReturnIterator.hasNext()){
					dispatchReturnObjectAtrray = (Object[])dispatchReturnIterator.next();
					dispatchProductName = (String)dispatchReturnObjectAtrray[0];
					dispatchStock = (Integer)dispatchReturnObjectAtrray[1];
					returnStock = (Integer)dispatchReturnObjectAtrray[2];
					 if(dispatchStock == null){
						 dispatchStockNotNull = new Integer(0);
						}else{
						 dispatchStockNotNull = dispatchStock;
						}
					 if(returnStock == null){
						 returnStockNotNull = new Integer(0);
						}else{
							returnStockNotNull = returnStock;
						}
				}
				if(salesBookAllottmentsIterator.hasNext()){
					salesBookAllottmentsObjectArray = (Object[])salesBookAllottmentsIterator.next();
					salesBookProductName = (String)salesBookAllottmentsObjectArray[0];
					recentAllottments = (Integer)salesBookAllottmentsObjectArray[1];
					 if(recentAllottments == null){
						 recentAllottmentsNotNull = new Integer(0);
						}else{
							recentAllottmentsNotNull = recentAllottments;
						}
				}
					calculatePreviousQtyAllotted = dispatchStockNotNull - recentAllottmentsNotNull;
				
				openingStockSum = productionStockNotNull - calculatePreviousQtyAllotted +  returnStockNotNull;
				if(openingStockSum < 0){
					openingStockPositiveSum = -(openingStockSum);
				}else{
					openingStockPositiveSum = openingStockSum;
				}
				
			}else if(!openingStockList.isEmpty()){
				if(openingStockIterator.hasNext()){
					productionObjectArray = (Object[])openingStockIterator.next();
					productionProductName = (String)productionObjectArray[0];
					productionStock = (Integer)productionObjectArray[1];
				  if(productionStock == null){
					  productionStockNotNull = new Integer(0);
					}else{
						productionStockNotNull = productionStock;
					}
				}
				dispatchStockNotNull = new Integer(0);
				returnStockNotNull = new Integer(0);
				openingStockSum = productionStockNotNull - dispatchStockNotNull +  returnStockNotNull;
				if(openingStockSum < 0){
					openingStockPositiveSum = -(openingStockSum);
				}else{
					openingStockPositiveSum = openingStockSum;
				}
				
			}else if(!dispatchReturnList.isEmpty()){
				if(dispatchReturnIterator.hasNext()){
					dispatchReturnObjectAtrray = (Object[])dispatchReturnIterator.next();
					dispatchStock = (Integer)dispatchReturnObjectAtrray[1];
					returnStock = (Integer)dispatchReturnObjectAtrray[2];
					 if(dispatchStock == null){
						 dispatchStockNotNull = new Integer(0);
						}else{
						 dispatchStockNotNull = dispatchStock;
						}
					 if(returnStock == null){
						 returnStockNotNull = new Integer(0);
						}else{
							returnStockNotNull = returnStock;
						}
				}
				if(salesBookAllottmentsIterator.hasNext()){
					salesBookAllottmentsObjectArray = (Object[])salesBookAllottmentsIterator.next();
					salesBookProductName = (String)salesBookAllottmentsObjectArray[0];
					recentAllottments = (Integer)salesBookAllottmentsObjectArray[1];
					 if(recentAllottments == null){
						 recentAllottmentsNotNull = new Integer(0);
						}else{
							recentAllottmentsNotNull = recentAllottments;
						}
				}
				calculatePreviousQtyAllotted = dispatchStockNotNull - recentAllottmentsNotNull;
				productionStockNotNull = new Integer(0);
				openingStockSum = productionStockNotNull - calculatePreviousQtyAllotted +  returnStockNotNull;
				if(openingStockSum < 0){
					openingStockPositiveSum = -(openingStockSum);
				}else{
					openingStockPositiveSum = openingStockSum;
				}
			}
		}
}else{
	openingStockPositiveSum = new Integer(0);
}
	if(_logger.isDebugEnabled()) {
		_logger.debug("{} openingStockSum have been found.", openingStockPositiveSum);
	}
	return openingStockPositiveSum;

}
/**
 * This method is responsible for fetching all Customer Product Sales Report Data.
 * 
 * @param customerProductSalesWiseReportCommand - {@link ReportsCommand}
 * @return List - {@link List}
 * @throws DataAccessException 
 */
	public List<CustomerProductSalesReportResult> getCustomerProductSalesReportData(ReportsCommand customerProductSalesWiseReportCommand,VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<CustomerProductSalesReportResult> customerProductResultList = new ArrayList<CustomerProductSalesReportResult>();
		CustomerProductSalesReportResult customerProductReportResult = null;
		String customerBusinessName = null;
		
		customerBusinessName = customerProductSalesWiseReportCommand.getBusinessName();
		String reportType = customerProductSalesWiseReportCommand.getReportType();
		Date startDate = DateUtils.getStartTimeStamp(customerProductSalesWiseReportCommand.getStartDate());
		Date eDate = customerProductSalesWiseReportCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		if(reportType != null && reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		} else if(reportType != null && reportType.equals("Weekly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getDateAfterSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			startDate = DateUtils.getStartTimeStamp(startDate);
			endDate = DateUtils.getAfterThirtyDays(startDate);
		}
		// Query for getting Delivery Note Products Data
		StringBuffer deliveryNoteProductsQueryString = new StringBuffer("SELECT vdp.productName,SUM(vdp.productQty),vdp.productCost,SUM(vdp.totalCost) FROM VbDeliveryNoteProducts vdp WHERE vdp.vbDeliveryNote IN");
		deliveryNoteProductsQueryString.append("(SELECT vd.id FROM VbDeliveryNote vd WHERE vd.businessName LIKE :customerBusinessName ");
		deliveryNoteProductsQueryString.append(" AND vd.createdOn BETWEEN :startDate AND :endDate ");
		deliveryNoteProductsQueryString.append(" AND vd.vbOrganization = :vbOrganization) GROUP BY vdp.productName,vdp.productCost");
		
		Query deliveryNoteProductsQuery = session.createQuery(deliveryNoteProductsQueryString.toString());
		deliveryNoteProductsQuery.setParameter("startDate", startDate);
		deliveryNoteProductsQuery.setParameter("endDate", endDate);
		deliveryNoteProductsQuery.setParameter("customerBusinessName", customerBusinessName);
		deliveryNoteProductsQuery.setParameter("vbOrganization", organization);
		List deliveryNoteProductList = deliveryNoteProductsQuery.list();
		
		//variable Declarations
		String productName = null;
		Integer productQty = new Integer(0);
		Float salesPrice = new Float(0.0);
		Float totalSalesValue = new Float(0.0);
		StringBuffer salesReturnQueryString = new StringBuffer();
		Query salesReturnQuery = null;
		List salesReturnProductList = null;
		Object[] salesReturnObjectArray = null;
		Object[] deliveryNoteObjectArray = null;
		
		Integer salesReturnQty = new Integer(0);
		Float salesReturnValue = new Float(0.0);
		
		if(!deliveryNoteProductList.isEmpty()){
		for(Object deleiveryNoteObject : deliveryNoteProductList){
			customerProductReportResult = new CustomerProductSalesReportResult();
			deliveryNoteObjectArray = (Object[])deleiveryNoteObject;
			productName = (String) deliveryNoteObjectArray[0];
			productQty = (Integer) deliveryNoteObjectArray[1];
			salesPrice = (Float) deliveryNoteObjectArray[2];
			totalSalesValue = (Float) deliveryNoteObjectArray[3];
		    
			customerProductReportResult.setProductName(productName);
			customerProductReportResult.setQuantity(productQty);
			customerProductReportResult.setSellingPrice(salesPrice);
			customerProductReportResult.setSalesValue(totalSalesValue);
			
			//Retrieve SalesReturn Qty and SalesReturn Value based on sold ProductName
			salesReturnQueryString = new StringBuffer("SELECT vsrp.productName,SUM(vsrp.totalQty),SUM(vsrp.totalCost) FROM VbSalesReturnProducts vsrp WHERE vsrp.productName = :productName AND  vsrp.vbSalesReturn IN");
			salesReturnQueryString.append("(SELECT vsr.id FROM VbSalesReturn vsr WHERE vsr.businessName LIKE :customerBusinessName ");
			salesReturnQueryString.append("AND vsr.createdOn BETWEEN :startDate AND :endDate ");
			salesReturnQueryString.append(" AND vsr.vbOrganization = :vbOrganization) GROUP BY vsrp.productName ");
			 
			salesReturnQuery = session.createQuery(salesReturnQueryString.toString());
			salesReturnQuery.setParameter("startDate", startDate);
			salesReturnQuery.setParameter("productName", productName);
			salesReturnQuery.setParameter("endDate", endDate);
			salesReturnQuery.setParameter("customerBusinessName", customerBusinessName);
			salesReturnQuery.setParameter("vbOrganization", organization);
			salesReturnProductList = salesReturnQuery.list();
			
			if(!salesReturnProductList.isEmpty()){
			for(Object salesReturnObject : salesReturnProductList){
				salesReturnObjectArray = (Object[])salesReturnObject;
				
				salesReturnQty = (Integer) salesReturnObjectArray[1];
				salesReturnValue = (Float) salesReturnObjectArray[2];
				if(salesReturnQty == null) salesReturnQty = 0;
				if(salesReturnValue == null) salesReturnValue = 0F;
				customerProductReportResult.setSalesReturnQty(salesReturnQty);
				customerProductReportResult.setSalesReturnValue(salesReturnValue);
			}
		}else{
			customerProductReportResult.setSalesReturnQty(0);
			customerProductReportResult.setSalesReturnValue(0F);
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} Sales Return Result List not found", salesReturnProductList.size());
			}
		}
			customerProductResultList.add(customerProductReportResult);
		}
	
		}else{
		/*String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isErrorEnabled()) {
			_logger.error("Records not found for Delivery Note Products: {}", deliveryNoteProductList);
		}
		throw new DataAccessException(errorMsg);*/
	}
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} Customer Product Sales Product Result List", customerProductResultList.size());
		}
		      return customerProductResultList;
	}

	/**
	 * This method is responsible for fetching all Sales Executive Product Sales Report Data.
	 * 
	 * @param sleProductReportSalesWiseReportCommand - {@link ReportsCommand}
	 * @return List - {@link List}
	 * @throws DataAccessException 
	 */
public List<SLEProductReportResult> getSLEProductReportSalesReportData(ReportsCommand sleProductReportSalesWiseReportCommand,VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	List<SLEProductReportResult> sleProductSalesResultList = new ArrayList<SLEProductReportResult>();
	SLEProductReportResult sleProductSalesReportResult = null;
	
	String salesExecutive = sleProductReportSalesWiseReportCommand.getSalesExecutive();
	String reportType = sleProductReportSalesWiseReportCommand.getReportType();
	Date startDate = DateUtils.getStartTimeStamp(sleProductReportSalesWiseReportCommand.getStartDate());
	Date eDate = sleProductReportSalesWiseReportCommand.getEndDate();
	Date endDate = null;
	if(eDate != null) {
		endDate = DateUtils.getEndTimeStamp(eDate);
	}
	if(reportType != null && reportType.equals("Daily")) {
		 endDate = DateUtils.getEndTimeStamp(startDate);
	} else if(reportType != null && reportType.equals("Weekly")) {
		startDate = DateUtils.getStartTimeStamp(startDate);
		endDate = DateUtils.getDateAfterSevenDays(startDate);
	} else if(reportType != null &&  reportType.equals("Monthly")) {
		startDate = DateUtils.getStartTimeStamp(startDate);
		endDate = DateUtils.getAfterThirtyDays(startDate);
	}
	
	//variable Declarations
	String productName = null;
	Integer openingStock = new Integer(0);
	
	StringBuffer qtyAllotedAndSoldString = new StringBuffer();
	StringBuffer salesReturnQueryString = new StringBuffer();
	
	Query qtyAllotedAndSoldQuery = null;
	Query salesReturnQuery = null;
	
	List qtyAllotedAndSoldQueryList = null;
	List salesReturnProductList = null;
	
	Object[] qtyAllotedAndSoldObjectArray = null;
	Object[] sleProductSalesObjectArray = null;
	Object[] salesReturnObjectArray = null;
	
	Integer qtyAllotted = new Integer(0);
	Integer qtySold = new Integer(0);
	Integer resalableQty = new Integer(0);
	Integer damagedQty = new Integer(0);
	StringBuffer sleOpeningBalanceQueryString = new StringBuffer();
	Query sleOpeningBalanceQuery= null;
	List sleOpeningBalanceList = null;
	StringBuffer innerSalesBookQueryString = new StringBuffer();
	Query innerSalesBookQuery = null;
	List innerSalesBookQueryList = null;
	Integer innerQueryLimit = new Integer(1);
	Integer salesBookId = new Integer(0);
	String batchNumber = null;
	String salesReturnProductName = null;
	String salesReturnBatchNumber = null;
	
	//Retrieve sold and allotted products from vb_sales_book and vb_sales_book_products
	
			qtyAllotedAndSoldString = new StringBuffer("SELECT vsp.productName,vsp.batchNumber,SUM(vsp.qtyAllotted),SUM(vsp.qtySold) FROM VbSalesBookProducts vsp WHERE  vsp.vbSalesBook IN");
			qtyAllotedAndSoldString.append("(SELECT vs.id FROM VbSalesBook vs WHERE vs.salesExecutive LIKE :salesExecutiveName ");
			qtyAllotedAndSoldString.append("AND vs.createdOn BETWEEN :startDate AND :endDate ");
			qtyAllotedAndSoldString.append(" AND vs.vbOrganization = :vbOrganization) GROUP BY vsp.productName ");
			 
			qtyAllotedAndSoldQuery = session.createQuery(qtyAllotedAndSoldString.toString());
			qtyAllotedAndSoldQuery.setParameter("startDate", startDate);
			qtyAllotedAndSoldQuery.setParameter("endDate", endDate);
			qtyAllotedAndSoldQuery.setParameter("salesExecutiveName", salesExecutive);
			qtyAllotedAndSoldQuery.setParameter("vbOrganization", organization);
			qtyAllotedAndSoldQueryList = qtyAllotedAndSoldQuery.list();
	
		if(!qtyAllotedAndSoldQueryList.isEmpty()){
		for(Object qtyAllotedAndSoldObject : qtyAllotedAndSoldQueryList){
			qtyAllotedAndSoldObjectArray = (Object[])qtyAllotedAndSoldObject;
			sleProductSalesReportResult = new SLEProductReportResult();
			productName = (String) qtyAllotedAndSoldObjectArray[0];
			batchNumber = (String)qtyAllotedAndSoldObjectArray[1];
			qtyAllotted = (Integer) qtyAllotedAndSoldObjectArray[2];
			qtySold = (Integer) qtyAllotedAndSoldObjectArray[3];
			
			sleProductSalesReportResult.setProductName(productName);
			sleProductSalesReportResult.setRecievedQty(qtyAllotted);
			sleProductSalesReportResult.setSalesQty(qtySold);
		
			
			// Query for Retrieve Sales Return and Damaged Return from vb_sales_return and vb_sales_return_products
			salesReturnQueryString = new StringBuffer("SELECT vsrp.productName,vsrp.batchNumber,SUM(vsrp.damaged),SUM(vsrp.resalable) FROM  VbSalesReturn vsr, VbSalesReturnProducts vsrp WHERE vsr.status LIKE :statusParameter AND vsrp.productName = :productName AND vsrp.batchNumber = :batchNumber AND vsr.vbSalesBook IN ");
			salesReturnQueryString.append("(SELECT vs.id FROM VbSalesBook vs WHERE vs.salesExecutive LIKE :salesExecutiveName ");
			salesReturnQueryString.append("AND vs.createdOn BETWEEN :startDate AND :endDate ");
			salesReturnQueryString.append("AND vs.vbOrganization = :vbOrganization) AND vsr.id=vsrp.vbSalesReturn GROUP BY vsrp.productName ");
			salesReturnQuery = session.createQuery(salesReturnQueryString.toString());
			salesReturnQuery.setParameter("startDate", startDate);
			salesReturnQuery.setParameter("endDate", endDate);
			salesReturnQuery.setParameter("salesExecutiveName", salesExecutive);
			salesReturnQuery.setParameter("vbOrganization", organization);
			salesReturnQuery.setParameter("productName", productName);
			salesReturnQuery.setParameter("statusParameter", "APPROVED");
			salesReturnQuery.setParameter("batchNumber", batchNumber);
			salesReturnProductList = salesReturnQuery.list();
			
			
			if(!salesReturnProductList.isEmpty()){
			for(Object salesReturnObject : salesReturnProductList){
				salesReturnObjectArray = (Object[])salesReturnObject;
				
				salesReturnProductName = (String)salesReturnObjectArray[0];
				salesReturnBatchNumber = (String)salesReturnObjectArray[1];
				damagedQty = (Integer) salesReturnObjectArray[2];
				resalableQty = (Integer) salesReturnObjectArray[3];
				
				sleProductSalesReportResult.setDamagedQty(damagedQty);
				sleProductSalesReportResult.setResalableQty(resalableQty);
			}
		}else{
			sleProductSalesReportResult.setDamagedQty(new Integer(0));
			sleProductSalesReportResult.setResalableQty(new Integer(0));
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} Sales Return Result List not found", salesReturnProductList.size());
			}
		}
			innerSalesBookQueryString = new StringBuffer("SELECT Max(vs.id) FROM VbSalesBook vs WHERE vs.salesExecutive LIKE :salesExecutiveName ");
			innerSalesBookQueryString.append(" AND vs.createdOn < :startDate ");
			innerSalesBookQueryString.append(" AND vs.vbOrganization = :vbOrganization");
			
			innerSalesBookQuery = session.createQuery(innerSalesBookQueryString.toString());
			innerSalesBookQuery.setMaxResults(innerQueryLimit);
			innerSalesBookQuery.setParameter("startDate", startDate);
			innerSalesBookQuery.setParameter("salesExecutiveName", salesExecutive);
			innerSalesBookQuery.setParameter("vbOrganization", organization);
			innerSalesBookQueryList = innerSalesBookQuery.list();
			
			for(Object salesBookIdInnerQuery : innerSalesBookQueryList){
				 salesBookId = (Integer)salesBookIdInnerQuery;
			}
			
			
			// Query for getting OpeningBalance and ProductName for SalesExecutive
			sleOpeningBalanceQueryString = new StringBuffer("SELECT vsp.productName,vsp.qtyClosingBalance FROM VbSalesBookProducts vsp WHERE  vsp.vbSalesBook LIKE :salesBookId AND vsp.productName = :productName GROUP BY vsp.productName ");
			
			//Getting issues with query as vs.createdOn LIKE :startDate
			sleOpeningBalanceQuery = session.createQuery(sleOpeningBalanceQueryString.toString());
			sleOpeningBalanceQuery.setParameter("productName", productName);
			sleOpeningBalanceQuery.setParameter("salesBookId", salesBookId);
			sleOpeningBalanceList = sleOpeningBalanceQuery.list();
		
			if(!sleOpeningBalanceList.isEmpty()){
				for(Object sleProductSalesObject : sleOpeningBalanceList){
					sleProductSalesObjectArray = (Object[])sleProductSalesObject;
					openingStock = (Integer) sleProductSalesObjectArray[1];
				    
					sleProductSalesReportResult.setOpeningStock(openingStock);
				}
			}else{
				sleProductSalesReportResult.setOpeningStock(new Integer(0));
			}
			sleProductSalesResultList.add(sleProductSalesReportResult);
		}
	}else{
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} Sales Executive Product Name,SoldQty,AllottedQty Not found", qtyAllotedAndSoldQueryList);
		}
	}
	session.close();
	if(_logger.isDebugEnabled()) {
		_logger.debug("{} Sales Executive Product List", sleProductSalesResultList);
	}
	      return sleProductSalesResultList;
}
}
