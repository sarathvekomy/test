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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
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
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.DeliveryNoteCollectionReportResult;
import com.vekomy.vbooks.reports.result.DeliveryNoteReportResult;
import com.vekomy.vbooks.reports.result.DynamicReportResult;
import com.vekomy.vbooks.reports.result.JournalReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveExpenditureReportResult;
import com.vekomy.vbooks.reports.result.SalesExecutiveSalesWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnQtyReportResult;
import com.vekomy.vbooks.reports.result.SalesReturnReportResult;
import com.vekomy.vbooks.reports.result.SalesWiseReportResult;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.StringUtil;

/**
 * 
 * @author Swarupa.
 * @param <K>
 * 
 */
public class ReportsDao<K,V> extends BaseDao{

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ReportsDao.class);
	
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbProductInventoryTransaction vb WHERE vb.createdOn BETWEEN :startDate AND :endDate");
		if(productName != null && !productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbOrganization = :vbOrganization");
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		ArrayList<DeliveryNoteReportResult> reportResultList = new ArrayList<DeliveryNoteReportResult>();
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNotePayments vb WHERE" +
				" vb.vbDeliveryNote.createdBy = :createdBy AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate AND vb.vbDeliveryNote.invoiceNo LIKE :invoiceNo");
		if(!("".equals(businessName))) {
			queryString.append(" AND vb.vbDeliveryNote.businessName LIKE :businessName");
			addBusinessName = Boolean.TRUE;
		}
		queryString.append(" AND vb.vbDeliveryNote.flag = :flag");
		query = session.createQuery(queryString.toString())
				.setParameter("createdBy", salesWiseReportCommand.getSalesExecutive())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null && reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
	 * This method is responsible for getting all the sales executive names from vbproduct.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllSalesExecutives(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> salesExecutiveList = session.createCriteria(VbEmployee.class)
		.setProjection(Projections.property("username"))
		.add(Restrictions.eq("vbOrganization", organization))
		.add(Restrictions.eq("employeeType", "SLE"))
		.list();
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
		if(vbemployee != null) {
			businessNameList = session.createCriteria(VbEmployeeCustomer.class)
					.createAlias("vbCustomer", "customer")
					.setProjection(Projections.distinct(Projections.property("customer.businessName")))
					.add(Restrictions.eq("vbEmployee", vbemployee))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.like("customer.businessName", businessName,MatchMode.START).ignoreCase())
					.addOrder(Order.asc("customer.businessName")).list();
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("SELECT vb.dayBookType, SUM(vb.valueOne), vb.valueThree, vb.createdOn FROM VbCashDayBook vb WHERE vb.createdBy = :createdBy AND vb.createdOn BETWEEN :startDate AND :endDate");
		if(allowanceType != null && !allowanceType.equalsIgnoreCase("-1")) {
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		} else if(reportType != null &&  reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
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
/*
	public List<DeliveryNoteReportResult> getDnData(VbOrganization organization) {
		Session session = this.getSession();
		DeliveryNoteReportResult deliveryNoteReportResult = null;
		List<DeliveryNoteReportResult>dnList = new ArrayList<>();
		List<VbDeliveryNoteProducts> deliveryNoteProducts = (List<VbDeliveryNoteProducts>) session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote","vbDeliveryNote")
				.add(Restrictions.eq("vbDeliveryNote.vbOrganization", organization))
				.list();
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : deliveryNoteProducts) {
			deliveryNoteReportResult = new DeliveryNoteReportResult();
			deliveryNoteReportResult.setProductName(vbDeliveryNoteProducts.getProductName());
			deliveryNoteReportResult.setBusinessName(vbDeliveryNoteProducts.getVbDeliveryNote().getBusinessName());
			dnList.add(deliveryNoteReportResult);
		}
		List<DeliveryNoteReportResult>finalList = new ArrayList<DeliveryNoteReportResult>(new HashSet<DeliveryNoteReportResult>(dnList));
		return finalList;
		
				
	}

	public List<DynamicReportResult> getDnByProducts(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		List<DynamicReportResult>dynamicList = new ArrayList<>();
		DynamicReportResult dynamicReportResult = null;
		List<VbDeliveryNoteProducts>dnData = session.createCriteria(VbDeliveryNoteProducts.class)
				.add(Restrictions.eq("productName", salesWiseReportCommand.getProductName()))
				.list();
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : dnData) {
			dynamicReportResult = new DynamicReportResult();
			dynamicReportResult.setProductName(vbDeliveryNoteProducts.getProductName());
			dynamicReportResult.setBatchNumber(vbDeliveryNoteProducts.getBatchNumber());
			//dynamicReportResult.se(vbDeliveryNoteProducts.getVbDeliveryNote().getCreatedOn());
			dynamicReportResult.setQtySold(vbDeliveryNoteProducts.getProductQty());
			dynamicList.add(dynamicReportResult);
			
		}
		return dynamicList;
	}
	public List<DynamicReportResult> getDnByCustomers(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		List<DynamicReportResult>dynamicList = new ArrayList<>();
		DynamicReportResult dynamicReportResult =  new DynamicReportResult();
		List<VbDeliveryNoteProducts>dnData = (List<VbDeliveryNoteProducts>)session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "vbDeliveryNote")
				.add(Restrictions.eq("vbDeliveryNote.businessName", salesWiseReportCommand.getBusinessName()))
				.list();
		List<VbDeliveryNotePayments>paymentsData = (List<VbDeliveryNotePayments>)session.createCriteria(VbDeliveryNotePayments.class)
				.createAlias("vbDeliveryNote", "vbDeliveryNote")
				.add(Restrictions.eq("vbDeliveryNote.businessName", salesWiseReportCommand.getBusinessName()))
				.list();
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : dnData) {
			dynamicReportResult.setProductName(vbDeliveryNoteProducts.getProductName());
			dynamicReportResult.setBatchNumber(vbDeliveryNoteProducts.getBatchNumber());
			//dynamicReportResult.setCreatedOn(vbDeliveryNoteProducts.getVbDeliveryNote().getCreatedOn());
			dynamicReportResult.setBusinessName(vbDeliveryNoteProducts.getVbDeliveryNote().getBusinessName());
			dynamicReportResult.setQtySold(vbDeliveryNoteProducts.getProductQty());
			dynamicList.add(dynamicReportResult);
		}
		for (VbDeliveryNotePayments vbDeliveryNotePayments : paymentsData) {
				//dynamicReportResult.setBalance(vbDeliveryNotePayments.getBalance());
				dynamicReportResult.setAdvanceAmount(vbDeliveryNotePayments.getPresentAdvance());
		}
		return dynamicList;
	}*/

	public List<DynamicReportResult> getDnByProductsAndCustomers(
			ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		List<DynamicReportResult>dynamicList = new ArrayList<>();
		DynamicReportResult dynamicReportResult =  new DynamicReportResult();
		List<VbDeliveryNoteProducts>dnData = (List<VbDeliveryNoteProducts>)session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "vbDeliveryNote")
				.add(Restrictions.eq("vbDeliveryNote.businessName", salesWiseReportCommand.getBusinessName()))
				.add(Restrictions.eq("productName", salesWiseReportCommand.getProductName()))
				.list();
		List<VbDeliveryNotePayments>paymentsData = (List<VbDeliveryNotePayments>)session.createCriteria(VbDeliveryNotePayments.class)
				.createAlias("vbDeliveryNote", "vbDeliveryNote")
				.add(Restrictions.eq("vbDeliveryNote.businessName", salesWiseReportCommand.getBusinessName()))
				.list();
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : dnData) {
			dynamicReportResult.setProductName(vbDeliveryNoteProducts.getProductName());
			dynamicReportResult.setBatchNumber(vbDeliveryNoteProducts.getBatchNumber());
			dynamicReportResult.setBusinessName(vbDeliveryNoteProducts.getVbDeliveryNote().getBusinessName());
			//dynamicReportResult.setQtySold(vbDeliveryNoteProducts.getProductQty());
			dynamicList.add(dynamicReportResult);
		}
		for (VbDeliveryNotePayments vbDeliveryNotePayments : paymentsData) {
				//dynamicReportResult.setCreditAmount(vbDeliveryNotePayments.getBalance());
				//dynamicReportResult.setAdvanceAmount(vbDeliveryNotePayments.getPresentAdvance());
		}
		return dynamicList;
	}

	/**
	 * @param criteria
	 * @param inputFields
	 * @param outPutFields
	 * @param organization
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	public List<DynamicReportResult> getDynamicDnData(Map<String, String> criteriaMap,
			String inputFields, String outPutFields, VbOrganization organization) throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, ParseException {
		List<DynamicReportResult>resultList = new ArrayList<>();
		DynamicReportResult dynamicReportResult = null;
		Boolean isdnPaymentsInput = Boolean.FALSE;
		Boolean isDnProductsInput = Boolean.FALSE;
		Boolean isDnInput = Boolean.FALSE;
		Boolean isdnPaymentsOp = Boolean.FALSE;
		Boolean isDnProductsOp = Boolean.FALSE;
		Boolean isDnOp = Boolean.FALSE;
		Boolean isLocality = Boolean.FALSE;
		@SuppressWarnings("rawtypes")
		Class tableName1 = null ;
		Class<?>tableName2 = null;
		String[] inputs = inputFields.split(",");
		String [] outputs = outPutFields.split(",");
		Criteria dnProductsCriteria = null;
		Session session = this.getSession();
		Map<String,Class<?>>dnInputs = new HashMap<>();
		Map<String,Class<?>>dnPayInputs = new HashMap<>();
		Map<String,Class<?>>dnProdInputs = new HashMap<>();
		List<String> dnPayOutputs = new ArrayList<>() ;
		List<String> dnoutputs = new ArrayList<>() ;
		List<String>dnProdOutPuts = new ArrayList<>()  ;
		List<VbDeliveryNotePayments>resultData = new ArrayList<>();
		String[] criterias =null;
		String criteriaMapVal = null;
		List<VbDeliveryNoteProducts>resultProdData = new ArrayList<>();
		List<VbDeliveryNote>dnresultData = new ArrayList<>();
		String custLocality = null;
		Field[] deliveryNoteFields = VbDeliveryNote.class.getDeclaredFields();
		Field[] deliveryNoteProducts = VbDeliveryNoteProducts.class.getDeclaredFields();
		Field[] deliveryNotePayments = VbDeliveryNotePayments.class.getDeclaredFields();
		List<VbCustomerDetail>customerDetails = null;
		
		for (Field field : deliveryNoteProducts) {
			for (String inputField : inputs) {
				if(inputField.equals(field.getName())){
					dnProdInputs.put(inputField,field.getType());
					isDnProductsInput = Boolean.TRUE;
				}
			}
		}
		for (Field field : deliveryNotePayments) {
			for (String inputField : inputs) {
				if(inputField.equals(field.getName())){
					dnPayInputs.put(inputField,field.getType());
					isdnPaymentsInput = Boolean.TRUE;
				}
			}
		}
		for (Field field : deliveryNoteFields) {
			for (String inputField : inputs) {
				if(inputField.equals(field.getName())){
					dnInputs.put(inputField, field.getType());
					isDnInput = Boolean.TRUE;
				}
			}
		}
		for (String outputField : outputs) {
			for (Field field : deliveryNoteFields) {
					if(outputField.equals(field.getName())){
						dnoutputs.add(outputField);
						isDnOp = Boolean.TRUE;
					}
				}
			}
		for (String outputField : outputs) {
			for (Field field : deliveryNoteProducts) {
					if(outputField.equals(field.getName())){
						dnProdOutPuts.add(outputField);
						isDnProductsOp = Boolean.TRUE;
					}
				}
			}
			for (String outputField : outputs) {
			for (Field field : deliveryNotePayments) {
					if(outputField.equals(field.getName())){
						dnPayOutputs.add(outputField);
						isdnPaymentsOp = Boolean.TRUE;
					}
				}
			}
			for (String outputField : outputs) {
				if("locality".equals(outputField)){
					isLocality = Boolean.TRUE;
				}
			}
		//To retrieve locality as it doesn't belongs to any of these 3 DN tables,we need to get it seperately
			if(isDnInput == Boolean.TRUE){
				for(Map.Entry<String, Class<?>> entry : dnInputs.entrySet()){
					criteriaMapVal = criteriaMap.get(entry.getKey());
					criterias = criteriaMapVal.split(",");
					/*if(entry.getKey().equals("businessName")){
						custLocality = getCustomerLocality(criterias[1],session,organization);
					}*/
			}
		}
			if(isLocality == Boolean.TRUE){
				 customerDetails = (List<VbCustomerDetail>) getLocalities(session,organization);
			}
		if((isdnPaymentsInput == Boolean.TRUE&&isDnProductsInput == Boolean.TRUE)||(isdnPaymentsOp == Boolean.TRUE && isDnProductsOp == Boolean.TRUE)
				||(isdnPaymentsInput == Boolean.TRUE&&isDnProductsOp == Boolean.TRUE)||(isdnPaymentsOp == Boolean.TRUE&&isDnProductsInput == Boolean.TRUE)){
			tableName1 = VbDeliveryNotePayments.class;
			tableName2 = VbDeliveryNoteProducts.class;
		}else{
			if(isdnPaymentsInput == Boolean.TRUE||isdnPaymentsOp == Boolean.TRUE){
				tableName1  = VbDeliveryNotePayments.class;
			}else if((isdnPaymentsInput == Boolean.TRUE&&isDnOp==Boolean.TRUE)||(isdnPaymentsOp == Boolean.TRUE&&isDnInput == Boolean.TRUE) || (isdnPaymentsInput == Boolean.TRUE&&isdnPaymentsOp == Boolean.TRUE)){
				tableName1  = VbDeliveryNotePayments.class;
			}else if(isDnProductsInput == Boolean.TRUE||isDnProductsOp == Boolean.TRUE){
				tableName1  = VbDeliveryNoteProducts.class;
			}else if((isDnProductsInput == Boolean.TRUE&&isDnOp==Boolean.TRUE)||(isDnProductsOp == Boolean.TRUE&&isDnInput == Boolean.TRUE) ||(isDnProductsInput == Boolean.TRUE&&isDnProductsOp == Boolean.TRUE)){
				tableName1  = VbDeliveryNoteProducts.class;
			}else if((isDnOp==Boolean.TRUE&&isDnInput == Boolean.TRUE)||isDnInput == Boolean.TRUE||isDnOp == Boolean.TRUE){
				tableName1  = VbDeliveryNote.class;
			}
		}
		if(tableName2!=null){
			 dnProductsCriteria = session.createCriteria(tableName2);
		}
		//<<<<<<<<<<<<<<<<<--------Criteria Formation Starts from here------->>>>>>>>>>>>>>>>>>>>>>> 
		Criteria criteria = session.createCriteria(tableName1);
		if(tableName1.equals(com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments.class)||tableName1.equals(com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts.class)){
			if(isDnInput == Boolean.TRUE || isDnOp == Boolean.TRUE){
				criteria.createAlias("vbDeliveryNote", "vbDeliveryNote");
				if(tableName2 != null){
					dnProductsCriteria.createAlias("vbDeliveryNote", "vbDeliveryNote");
				}
				//.add(Restrictions.eq("vbDeliveryNote.vbOrgnaization", organization));
			}
		}
		//Adding Criteria for DeliveryNoteInputs if exist with combination of DnPayments table or DnProductsTable
		if((isdnPaymentsOp == Boolean.TRUE&&isDnInput == Boolean.TRUE)||(isdnPaymentsInput == Boolean.TRUE&&isDnInput==Boolean.TRUE)||(isDnProductsOp == Boolean.TRUE&&isDnInput == Boolean.TRUE)||(isDnProductsInput == Boolean.TRUE&&isDnInput == Boolean.TRUE)){
			for(Map.Entry<String, Class<?>> entry : dnInputs.entrySet()){
				criteriaMapVal = criteriaMap.get(entry.getKey());
				criterias = criteriaMapVal.split(",");
				String fieldName = "vbDeliveryNote."+entry.getKey();
				if(criterias.length !=0){
					criteria = prepareCriteriaForResult(criteria, entry.getValue(), criterias,fieldName);
					
				}
				if(tableName2 !=null){
					if(criterias.length !=0){
						dnProductsCriteria = prepareCriteriaForResult(dnProductsCriteria, entry.getValue(), criterias,fieldName);
					}
				}
				//criteria.add(Restrictions.eq(entry.getKey(), criteriaMap.get(entry.getKey())));
			}
		}
		//Executes when combination of tables occured
		if(tableName1 != null&&tableName2!=null){
			if(isdnPaymentsInput == Boolean.TRUE||isdnPaymentsOp == Boolean.TRUE){
				for(Map.Entry<String, Class<?>> entry : dnPayInputs.entrySet()){
					//To Add Restrictions based on the inputs
					criteriaMapVal = criteriaMap.get(entry.getKey());
					criterias = criteriaMapVal.split(",");
					if(criterias.length !=0){
						criteria = prepareCriteriaForResult(criteria,entry.getValue(),criterias,entry.getKey());
					}
				}
				resultData = criteria.list();
			}if(isDnProductsInput == Boolean.TRUE||isDnProductsOp == Boolean.TRUE){
				for(Map.Entry<String, Class<?>> entry : dnProdInputs.entrySet()){
					//To Add Restrictions based on the inputs
					criteriaMapVal = criteriaMap.get(entry.getKey());
					criterias = criteriaMapVal.split(",");
					if(criterias.length !=0){
						dnProductsCriteria = prepareCriteriaForResult(dnProductsCriteria,entry.getValue(),criterias,entry.getKey());
					}
					
				}
				resultProdData = dnProductsCriteria.list();
				for(VbDeliveryNotePayments dnPayResult :resultData){
					for(VbDeliveryNoteProducts productResult :resultProdData){
						if(productResult.getVbDeliveryNote()!=null){
							if(productResult.getVbDeliveryNote().equals(dnPayResult.getVbDeliveryNote())){
								 dynamicReportResult =new DynamicReportResult();
									if(isDnOp == Boolean.TRUE){
										for(String resultField : dnoutputs){
											//For setting data to setter method for the given Property/field Name
											String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
												if(isLocality == Boolean.TRUE){
													for(VbCustomerDetail customerDetail : customerDetails){
														if(productResult.getVbDeliveryNote().getBusinessName().equals(customerDetail.getVbCustomer().getBusinessName())){
															dynamicReportResult.setLocality(customerDetail.getLocality());
														}
													}
													dynamicReportResult.setShowLocality(true);
												}
											VbDeliveryNote vbDeliveryNote = dnPayResult.getVbDeliveryNote();
											if("createdOn".equals(resultField)){
												PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, DateUtils.format(vbDeliveryNote.getCreatedOn()));
											}else{
												PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(vbDeliveryNote, resultField));
											}
											PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
											
										}
									}
									if(isdnPaymentsOp == Boolean.TRUE){
										for(String resultField : dnPayOutputs){
											//For setting data to setter method for the given Property/field Name
											dnPayResult.getVbDeliveryNote().getVbDeliveryNotePaymentses();
											String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
											PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
											PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(dnPayResult, resultField));
										}
									}
									if(isDnProductsOp == Boolean.TRUE){
										for(String resultField : dnProdOutPuts){
											//For setting data to setter method for the given Property/field Name
											String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
											PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
											PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(productResult, resultField));
										}
									}
									resultList.add(dynamicReportResult);
							}
						}
					}
				}
			}
		}else{
			//To add restrictions for matched Table
			if(tableName1.equals(com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments.class)||com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments.class.equals(tableName2)){
				//Iterating over DnPayments inputs to add given criteria
				if(isdnPaymentsInput == Boolean.TRUE){
					for(Map.Entry<String, Class<?>> entry : dnPayInputs.entrySet()){
						//To Add Restrictions based on the inputs
						criteriaMapVal = criteriaMap.get(entry.getKey());
						criterias = criteriaMapVal.split(",");
						if(criterias.length !=0){
							criteria = prepareCriteriaForResult(criteria,entry.getValue(),criterias,entry.getKey());
						}
						
					}
				}
				resultData = criteria.list();
				for(VbDeliveryNotePayments result :resultData){
					dynamicReportResult =new DynamicReportResult();
					if(isDnOp == Boolean.TRUE){
						for(String resultField : dnoutputs){
							//For setting data to setter method for the given Property/field Name
							VbDeliveryNote vbDeliveryNote = result.getVbDeliveryNote();
							String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
						/*	if("locality".equals(resultField)){
									dynamicReportResult.setLocality(custLocality);
							}*/
							if(isLocality == Boolean.TRUE){
								for(VbCustomerDetail customerDetail : customerDetails){
									if(vbDeliveryNote.getBusinessName().equals(customerDetail.getVbCustomer().getBusinessName())){
										dynamicReportResult.setLocality(customerDetail.getLocality());
									}
								}
								dynamicReportResult.setShowLocality(true);
							}
							if("createdOn".equals(resultField)){
								PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, DateUtils.format(vbDeliveryNote.getCreatedOn()));
							}else{
								PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(vbDeliveryNote, resultField));
							}
							PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
						}
					}
					if(isdnPaymentsOp == Boolean.TRUE){
						for(String resultField : dnPayOutputs){
							//For setting data to setter method for the given Property/field Name
							String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
							PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
							PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(result, resultField));
						}
					}
					resultList.add(dynamicReportResult);
				}
				
				/*for(VbDeliveryNotePayments result :resultData){
					
				}*/
			}else if(tableName1.equals(com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts.class)){
				if(isDnProductsInput == Boolean.TRUE){
					for(Map.Entry<String, Class<?>> entry : dnProdInputs.entrySet()){
						//To Add Restrictions based on the inputs
						criteriaMapVal = criteriaMap.get(entry.getKey());
						criterias = criteriaMapVal.split(",");
						criteria = prepareCriteriaForResult(criteria,entry.getValue(),criterias,entry.getKey());
						if(criterias.length !=0){
							criteria = prepareCriteriaForResult(criteria,entry.getValue(),criterias,entry.getKey());
						}
						
					}
				}
				resultProdData = criteria.list();
				for(VbDeliveryNoteProducts result :resultProdData){
					dynamicReportResult =new DynamicReportResult();
					if(isDnOp == Boolean.TRUE){
						for(String resultField : dnoutputs){
							//For setting data to setter method for the given Property/field Name
							VbDeliveryNote vbDeliveryNote = result.getVbDeliveryNote();
							String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
							if(isLocality == Boolean.TRUE){
								for(VbCustomerDetail customerDetail : customerDetails){
									if(vbDeliveryNote.getBusinessName().equals(customerDetail.getVbCustomer().getBusinessName())){
										dynamicReportResult.setLocality(customerDetail.getLocality());
									}
								}
							}
							if("createdOn".equals(resultField)){
								PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, DateUtils.format(vbDeliveryNote.getCreatedOn()));
							}else{
								PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(vbDeliveryNote, resultField));
							}
							PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
						
							
						//	resultList.add(dynamicReportResult);
						}
					}
					if(isDnProductsOp == Boolean.TRUE){
						for(String resultField : dnProdOutPuts){
							//For setting data to setter method for the given Property/field Name
							String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
							PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
							PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(result, resultField));
							
						}
					}
					if(isLocality == Boolean.TRUE){
						dynamicReportResult.setLocality(custLocality);
						dynamicReportResult.setShowLocality(true);
					}
					resultList.add(dynamicReportResult);
				}
			}else if(tableName1.equals(com.vekomy.vbooks.hibernate.model.VbDeliveryNote.class)){
				if(isDnInput == Boolean.TRUE){
					for(Map.Entry<String, Class<?>> entry : dnInputs.entrySet()){
						//To Add Restrictions based on the inputs
						criteriaMapVal = criteriaMap.get(entry.getKey());
						criterias = criteriaMapVal.split(",");
						/*if(entry.getKey().equals("businessName")){
							custLocality = getCustomerLocality(criterias[1],session,organization);
						}*/
						if(criterias.length !=0){
							criteria = prepareCriteriaForResult(criteria,entry.getValue(),criterias,entry.getKey());
						}
					}
				}
				dnresultData = criteria.list();
				for(VbDeliveryNote result :dnresultData){
					dynamicReportResult = new DynamicReportResult();
					for(String resultField : dnoutputs){
						//For setting data to setter method for the given Property/field Name
						if(isLocality == Boolean.TRUE){
							for(VbCustomerDetail customerDetail : customerDetails){
								if(result.getBusinessName().equals(customerDetail.getVbCustomer().getBusinessName())){
									dynamicReportResult.setLocality(customerDetail.getLocality());
								}
							}
							dynamicReportResult.setShowLocality(true);
						}
						String booleanField = "show"+Character.toUpperCase(resultField.charAt(0))+resultField.substring(1); 
						if("createdOn".equals(resultField)){
							PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, DateUtils.format(result.getCreatedOn()));
						}else{
							PropertyUtils.setSimpleProperty(dynamicReportResult, resultField, PropertyUtils.getProperty(result, resultField));
						}
						PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
						
						
					}
					resultList.add(dynamicReportResult);
				}
			}
		}
		
		return resultList;
	}

	private String getCustomerLocality(String businessName, Session session, VbOrganization organization) {
		String locality =(String) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Restrictions.eq("customer.businessName",businessName))
				.setProjection(Projections.distinct(Projections.property("locality")))
				.add(Restrictions.eq("customer.vbOrganization", organization))
				.add(Restrictions.eq("customer.state",OrganizationUtils.CUSTOMER_ENABLED)).uniqueResult();
		return locality;
	}

	//Adds Restrictions dynamically
	private Criteria prepareCriteriaForResult(Criteria criteria, Class<?> value,String[] criterias, String fiedlName) throws ParseException {
		if((float.class.isAssignableFrom(value))||Float.class.isAssignableFrom(value)){
			criteria = addFloatCriteria(criteria, criterias[0],Float.valueOf(criterias[1]),fiedlName);
		}else if((Integer.class.isAssignableFrom(value))||int.class.isAssignableFrom(value)){
			criteria = addIntCriteria(criteria, criterias[0],Integer.parseInt(criterias[1]),fiedlName);
		}else if(String.class.isAssignableFrom(value)){
			criteria = addStringCriteria(criteria,  criterias[0], String.valueOf(criterias[1]),fiedlName);
		}else if(Date.class.isAssignableFrom(value)){
			Date date = DateUtils.parse(criterias[1]);
			criteria = addDateCriteria(criteria,  criterias[0],DateUtils.getEndTimeStamp(date),fiedlName);
		}
		return criteria;
		
	}

	private Criteria addDateCriteria(Criteria criteria, String criteriaOperator,
			Date date, String fiedlName) {
		if(criteriaOperator.equals("<")){
			 criteria.add(Restrictions.lt(fiedlName,date));
		 }else if(criteriaOperator.equals(">")){
			 criteria.add(Restrictions.gt(fiedlName,date));
		 }else if(criteriaOperator.equals("!=")){
			 criteria.add(Restrictions.ne(fiedlName,date));
		 }else if(criteriaOperator.equals("==")){
			 criteria.add(Restrictions.eq(fiedlName,date));
		 }
		return criteria;
	}

	private Criteria addIntCriteria(Criteria criteria, String criteriaOperator,
			Integer criteriaValue, String fiedlName) {
		if(criteriaOperator.equals("<")){
			 criteria.add(Restrictions.lt(fiedlName,criteriaValue));
		 }else if(criteriaOperator.equals(">")){
			 criteria.add(Restrictions.gt(fiedlName,criteriaValue));
		 }else if(criteriaOperator.equals("!=")){
			 criteria.add(Restrictions.ne(fiedlName,criteriaValue));
		 }else if(criteriaOperator.equals("==")){
			 criteria.add(Restrictions.eq(fiedlName,criteriaValue));
		 }
		return criteria;
	}

	private Criteria addFloatCriteria(Criteria criteria, String criteriaOperator, Float criteriaValue, String fiedlName) {
		 if(criteriaOperator.equals("<")){
			 criteria.add(Restrictions.lt(fiedlName,criteriaValue));
		 }else  if(criteriaOperator.equals(">")){
			 criteria.add(Restrictions.gt(fiedlName,criteriaValue));
		 }else  if(criteriaOperator.equals("==")){
			 criteria.add(Restrictions.eq(fiedlName,criteriaValue));
		 }else  if(criteriaOperator.equals("!=")){
			 criteria.add(Restrictions.ne(fiedlName,criteriaValue));
		 }
		return criteria;
	}
	private Criteria addStringCriteria(Criteria criteria, String criteriaOperator,
			String criteriaVal, String fiedlName) {
		if(criteriaOperator.equals("<")){
			 criteria.add(Restrictions.lt(fiedlName,criteriaVal));
		 }else if(criteriaOperator.equals(">")){
			 criteria.add(Restrictions.gt(fiedlName,criteriaVal));
		 }else if(criteriaOperator.equals("!=")){
			 criteria.add(Restrictions.ne(fiedlName,criteriaVal));
		 }else if(criteriaOperator.equals("==")){
			 criteria.add(Restrictions.eq(fiedlName,criteriaVal));
		 }else if(criteriaOperator.equals("LIKE")){
			 criteria.add(Restrictions.ilike(fiedlName, criteriaVal, MatchMode.START));
		 }
		return criteria;
	}

	public List<DynamicReportResult> getDynamicSrData(
			Map<String, String> criteriaMap, String inputFields,
			String outPutFields, VbOrganization organization) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {
		
		//Declarations and all..
		Boolean isSrInput = Boolean.FALSE;
		Boolean isSrProdInput = Boolean.FALSE;
		Boolean isSrOp = Boolean.FALSE;
		Boolean isSrProdOp = Boolean.FALSE;
		String[] inputs = inputFields.split(",");
		String [] outputs = outPutFields.split(",");
		Map<String,Class<?>>srInputs = new HashMap<>();
		Map<String,Class<?>>srProdInputs = new HashMap<>();
		List<String>srOuputs = new ArrayList<>();
		List<String>srProdOutputs = new ArrayList<>();
		Field[] srFields = VbSalesReturn.class.getDeclaredFields();
		List<DynamicReportResult>srResultList = new ArrayList<>();
		DynamicReportResult dynamicReportResult = null;
		String criteriaMapVal = null;
		String[] criterias = null;
		Boolean isLocality = Boolean.TRUE;
		List<VbSalesReturn>srResult = null;
		List<VbSalesReturnProducts>srProdResult = null;
		Field[] srProductsFields = VbSalesReturnProducts.class.getDeclaredFields();
		Class<?> tableName = null;
		List<VbCustomerDetail>customerDetails = null;
		
		Session session = this.getSession();
		//Filtering input & outputs
		for(Field tableField : srFields){
			for(String input : inputs){
				if(tableField.getName().equals(input)){
					srInputs.put(input, tableField.getType());
					isSrInput =Boolean.TRUE;
				}
			}
			for(String output : outputs){
				if(tableField.getName().equals(output)){
					srOuputs.add(output);
					isSrOp = Boolean.TRUE;
				}
			}
		}
		for(Field tableField : srProductsFields){
			for(String input : inputs){
				if(tableField.getName().equals(input)){
					isSrProdInput = Boolean.TRUE;
					srProdInputs.put(input, tableField.getType());
				}
			}
			for(String output : outputs){
				if(tableField.getName().equals(output)){
					isSrProdOp = Boolean.TRUE;
					srProdOutputs.add(output);
				}
			}
		}
		if(isLocality == Boolean.TRUE){
			List<VbCustomerDetail> customerDetail = (List<VbCustomerDetail>) getLocalities(session,organization);
		}
		//Forming Criteria for result data..
		 if((isSrProdInput == Boolean.TRUE && isSrInput == Boolean.TRUE)||(isSrProdInput == Boolean.TRUE&&isSrOp==Boolean.TRUE)||(isSrProdOp == Boolean.TRUE&&isSrInput==Boolean.TRUE)
				||(isSrProdOp == Boolean.TRUE&&isSrOp == Boolean.TRUE)){
			tableName = VbSalesReturnProducts.class;
		}else if(isSrInput == Boolean.TRUE || isSrOp == Boolean.TRUE){
				 tableName = VbSalesReturn.class;
			}
		Criteria criteria = session.createCriteria(tableName);
				
		if(tableName.equals(com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts.class)){
			criteria.createAlias("vbSalesReturn", "vbSalesReturn")
			.add(Restrictions.eq("vbSalesReturn.vbOrganization", organization));
		}else{
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		if(tableName.equals(com.vekomy.vbooks.hibernate.model.VbSalesReturn.class)){
			if(isSrInput == Boolean.TRUE){
				for(Entry<String, Class<?>> entry : srInputs.entrySet()){
					criteriaMapVal = criteriaMap.get(entry.getKey());
					criterias = criteriaMapVal.split(",");
					String fieldName = entry.getKey();
					if(criterias.length !=0){
						criteria = prepareCriteriaForResult(criteria, entry.getValue(), criterias,fieldName);
					}
				}
			}
			srResult = criteria.list();
			
			if(isSrOp == Boolean.TRUE){
				for(VbSalesReturn salesReturn : srResult){
					dynamicReportResult =new DynamicReportResult();
					for(String output : srOuputs){
						String booleanField = "show"+Character.toUpperCase(output.charAt(0))+output.substring(1); 
						PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField,true);
						if("createdOn".equals(output)){
							PropertyUtils.setSimpleProperty(dynamicReportResult, output, DateUtils.format(salesReturn.getCreatedOn()));
						}else{
							PropertyUtils.setSimpleProperty(dynamicReportResult, output, PropertyUtils.getProperty(salesReturn,output));
						}
						
					}
					if(isLocality == Boolean.TRUE){
						customerDetails = getLocalities(session, organization);
						for (VbCustomerDetail vbCustomerDetail : customerDetails) {
							if(salesReturn.getBusinessName().equals(vbCustomerDetail.getVbCustomer().getBusinessName())){
								dynamicReportResult.setLocality(vbCustomerDetail.getLocality());
							}
						}
					}
					srResultList.add(dynamicReportResult);
				}
			}
		}else if(tableName.equals(com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts.class)){
			for(Entry<String, Class<?>> entry : srProdInputs.entrySet()){
				criteriaMapVal = criteriaMap.get(entry.getKey());
				criterias = criteriaMapVal.split(",");
				String fieldName = entry.getKey();
				if(criterias.length !=0){
					criteria = prepareCriteriaForResult(criteria, entry.getValue(), criterias,fieldName);
				}
			}
			if(isSrInput == Boolean.TRUE){
				for(Entry<String, Class<?>> entry : srInputs.entrySet()){
					criteriaMapVal = criteriaMap.get(entry.getKey());
					criterias = criteriaMapVal.split(",");
					String fieldName = "vbSalesReturn."+entry.getKey();
					if(criterias.length !=0){
						criteria = prepareCriteriaForResult(criteria, entry.getValue(), criterias,fieldName);
					}
				}
			}
			srProdResult = criteria.list();
			for(VbSalesReturnProducts srProdResultData : srProdResult){
				dynamicReportResult =new DynamicReportResult();
				if(isSrOp == Boolean.TRUE){
					for(String srOpField : srOuputs){
						VbSalesReturn salesReturn = srProdResultData.getVbSalesReturn();
						String booleanField = "show"+Character.toUpperCase(srOpField.charAt(0))+srOpField.substring(1); 
						PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
						if("createdOn".equals(srOpField)){
							PropertyUtils.setSimpleProperty(dynamicReportResult, srOpField, DateUtils.format(salesReturn.getCreatedOn()));
						}else{
							PropertyUtils.setSimpleProperty(dynamicReportResult, srOpField, PropertyUtils.getProperty(salesReturn, srOpField));
						}
						
					}
				}
				if(isSrProdOp == Boolean.TRUE){
					for(String srProdOp : srProdOutputs){
						String booleanField = "show"+Character.toUpperCase(srProdOp.charAt(0))+srProdOp.substring(1); 
						PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
						PropertyUtils.setSimpleProperty(dynamicReportResult, srProdOp, PropertyUtils.getProperty(srProdResultData, srProdOp));
					}
				}
				srResultList.add(dynamicReportResult);
			}
		}
		
		return srResultList;
	}

	private List<VbCustomerDetail> getLocalities(Session session,
			VbOrganization organization) {
		List<VbCustomerDetail> customerDetails = (List<VbCustomerDetail>) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer","vbCustomer")
				.add(Restrictions.eq("vbCustomer.vbOrganization", organization))
				.list();
		return customerDetails;
	}

	/**
	 * This Method is Responsible to retrieve journal data dynamically
	 * 
	 * @param criteriaMap
	 * @param inputFields
	 * @param outPutFields
	 * @param organization
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public List<DynamicReportResult> getDynamicJournalData(
			Map<String, String> criteriaMap, String inputFields,
			String outPutFields, VbOrganization organization) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {
		
		String[] outputs = outPutFields.split(",");
		String[] inputs =inputFields.split(",");
		Map<String, Class<?>>inputMap = new HashMap<>();
		String criteriaMapVal =null;
		String[] criterias = null;
		List<DynamicReportResult>resultList = new ArrayList<>();
		DynamicReportResult dynamicReportResult = null;
		Session session = this.getSession();
		List<VbJournal>journalData = null;
		Field[] journalFields = VbJournal.class.getDeclaredFields();
		for(String inputName :inputs){
			for(Field tableField :journalFields){
				if(tableField.getName().equals(inputName)){
					inputMap.put(inputName, tableField.getType());
				}
			}
		}
		Criteria criteria = session.createCriteria(VbJournal.class)
				.add(Restrictions.eq("vbOrganization", organization));
		for(Entry<String, Class<?>>jouInputs : inputMap.entrySet()){
			criteriaMapVal = criteriaMap.get(jouInputs.getKey());
			criterias = criteriaMapVal.split(",");
			String fieldName = jouInputs.getKey();
			if(criterias.length !=0){
				criteria = prepareCriteriaForResult(criteria, jouInputs.getValue(), criterias,fieldName);
			}
		}
		journalData = criteria.list();
		for(VbJournal journal : journalData){
			dynamicReportResult = new DynamicReportResult();
			for(String reqField : outputs){
				if("locality".equals(reqField)){
						List<VbCustomerDetail> customerDetails = getLocalities(session, organization);
						for (VbCustomerDetail vbCustomerDetail : customerDetails) {
							if(journal.getBusinessName().equals(vbCustomerDetail.getVbCustomer().getBusinessName())){
								dynamicReportResult.setLocality(vbCustomerDetail.getLocality());
							}
						}
				}
				String booleanField = "show"+Character.toUpperCase(reqField.charAt(0))+reqField.substring(1); 
				PropertyUtils.setSimpleProperty(dynamicReportResult, booleanField, true);
				if("createdOn".equals(reqField)){
					PropertyUtils.setSimpleProperty(dynamicReportResult, reqField, DateUtils.format(journal.getCreatedOn()));
				}else{
					PropertyUtils.setSimpleProperty(dynamicReportResult, reqField, PropertyUtils.getProperty(journal, reqField));
				}
				
			}
			resultList.add(dynamicReportResult);
		}
		return resultList;
	}

	public List<DynamicReportResult> getCommonData(
			Map<String, String> criteriaMap, String inputFields,
			String outPutFields, VbOrganization organization) {
		String[] inputs = inputFields.split(",");
		String[] outputs = outPutFields.split(",");
		List<String>custOps = null;
		List<String>prodOps = null;
		List<String>dnOps = null;
		List<String>dnPayOps = null;
		List<String>dnProdOps = null;
		List<String>cashDayBookOp = null;
		Map<String,Class<?>>custIpMap = new HashMap<>();
		Map<String, Class<?>>prodMap = new HashMap<>();
		Map<String, Class<?>>dnMap = new HashMap<>();
		Map<String,Class<?>>dnPayMap = new HashMap<>();
		Map<String, Class<?>>dnProdMap = new HashMap<>();
		Map<String,Class<?>>cashDbMap = new HashMap<>();
		
		return null;
	}

}
