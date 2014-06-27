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
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAssignOrganizations;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.reports.command.ReportsCommand;
import com.vekomy.vbooks.reports.result.CustomerWiseReportResult;
import com.vekomy.vbooks.reports.result.ProductWiseReportResult;
import com.vekomy.vbooks.reports.result.SalesWiseReportResult;
import com.vekomy.vbooks.util.DateUtils;
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
	
	/**
	 * This method is responsible for fetching every customer information about products.
	 * 
	 * @param reportsCommand
	 * @return List
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerWiseReportResult> getCustomerWiseData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		Query query = null;
		ArrayList<CustomerWiseReportResult> resultList = new ArrayList<CustomerWiseReportResult>();
		boolean addProductName = false;
		boolean addBatchNumber = false;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String businessName = reportsCommand.getBusinessName();
		String reportType = reportsCommand.getReportType();
		String batchNumber = null;
		if(reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		}
		else if(reportType.equals("Weekly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		}
		else if(reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbDeliveryNoteProducts vb WHERE vb.vbDeliveryNote.businessName = :businessName AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate");
		if(!productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = true;
			batchNumber = (String) session.createCriteria(VbProduct.class)
					.setProjection(Projections.property("batchNumber"))
					.add(Restrictions.eq("productName", productName))
					.list().get(0);
			queryString.append(" AND vb.batchNumber = :batchNumber");
			addBatchNumber = true;
		}
		queryString.append(" AND vb.vbDeliveryNote.vbOrganization = :organization");
		query = session.createQuery(queryString.toString());
		query.setParameter("businessName", businessName);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		if(addBatchNumber) {
			query.setParameter("batchNumber", batchNumber);
		}
		query.setParameter("organization", organization);
		List<VbDeliveryNoteProducts> productsList = query.list();
		CustomerWiseReportResult reportResult;
		for (VbDeliveryNoteProducts products : productsList) {
			reportResult = new CustomerWiseReportResult();
			reportResult.setProductName(products.getProductName());
			reportResult.setBatchNumber(products.getBatchNumber());
			reportResult.setProductCost(StringUtil.floatFormat(products.getProductCost()));
			reportResult.setBusinessName(products.getVbDeliveryNote().getBusinessName());
			reportResult.setProductQty(products.getProductQty());
			reportResult.setTotalCost(StringUtil.floatFormat(products.getTotalCost()));
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
	 * @param reportsCommand
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<ProductWiseReportResult> getProductWiseReportData(ReportsCommand reportsCommand, VbOrganization organization) {
		Session session = this.getSession();
		Query query = null;
		boolean addProductName = false;
		boolean addBatchNumber = false;
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String productName = reportsCommand.getProductName();
		String reportType = reportsCommand.getReportType();
		String batchNumber = null;
		if(reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		}
		else if(reportType.equals("Weekly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		}
		else if(reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		StringBuffer queryString = new StringBuffer("FROM VbProductInventoryTransaction vb WHERE vb.createdOn BETWEEN :startDate AND :endDate");
		if(!productName.equalsIgnoreCase("ALL")) {
			queryString.append(" AND vb.productName = :productName");
			addProductName = true;
			batchNumber = (String) session.createCriteria(VbProduct.class)
					.setProjection(Projections.property("batchNumber"))
					.add(Restrictions.eq("productName", productName))
					.list().get(0);
			queryString.append(" AND vb.batchNumber = :batchNumber");
			addBatchNumber = true;
		}
		queryString.append(" AND vb.vbOrganization = :vbOrganization");
		query = session.createQuery(queryString.toString());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if(addProductName) {
			query.setParameter("productName", productName);
		}
		if(addBatchNumber) {
			query.setParameter("batchNumber", batchNumber);
		}
		query.setParameter("vbOrganization", organization);
		List<VbProductInventoryTransaction> productsList = query.list();
		List<ProductWiseReportResult> productList = new ArrayList<ProductWiseReportResult>();
		ProductWiseReportResult reportResult = null;
		for (VbProductInventoryTransaction products : productsList) {
			reportResult = new ProductWiseReportResult();
			reportResult.setProductName(products.getProductName());
			reportResult.setBatchNumber(products.getBatchNumber());
			reportResult.setInwardsQty(products.getInwardsQty());
			reportResult.setOutwardsQty(products.getOutwardsQty());
			reportResult.setCreatedOn(DateUtils.format(products.getCreatedOn()));
			productList.add(reportResult);
			
		}
		
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} available for :", productList.size());
		}
		return productList;
	}
	
	/**
	 * This method is responsible for fetching all Sales information.
	 * 
	 * @param reportsCommand
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<SalesWiseReportResult> getSalesWiseReportData(ReportsCommand reportsCommand) {
		Session session = this.getSession();
		Date startDate = DateUtils.getStartTimeStamp(reportsCommand.getStartDate());
		Date eDate = reportsCommand.getEndDate();
		Date endDate = null;
		if(eDate != null) {
			endDate = DateUtils.getEndTimeStamp(eDate);
		}
		String salesExecutive = reportsCommand.getSalesExecutive();
		String reportType = reportsCommand.getReportType();
		if(reportType.equals("Daily")) {
			 endDate = DateUtils.getEndTimeStamp(startDate);
		}
		else if(reportType.equals("Weekly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getDateBeforeSevenDays(startDate);
		}
		else if(reportType.equals("Monthly")) {
			endDate = DateUtils.getEndTimeStamp(startDate);
			startDate = DateUtils.getBeforThirtyDays(startDate);
		}
		Query query = session.createQuery("FROM VbDeliveryNoteProducts vb WHERE vb.vbDeliveryNote.createdBy = :salesExecutive AND vb.vbDeliveryNote.createdOn BETWEEN :startDate AND :endDate")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
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
			salesExecutiveList.add(reportResult);
			
		}
		
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} available for :", salesExecutiveList.size());
		}
		return salesExecutiveList;
	}


	/**
	 * This method is used to get the businessnames based on the vborganization.
	 * 
	 * @param businessName
	 * @param organization
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllBusinessNames(String businessName , VbOrganization organization) {
		Session session = this.getSession();
		List<String> businessNameList = session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("businessName"))
			    .add(Expression.eq("vbOrganization", organization))
			    .add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("businessName"))
			    .list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Business Name List: {}", businessNameList);
		}
		return businessNameList;
	}
	
	/**
	 * This method is used to get the productNameList based on the businessName from
	 * VbDeliveryNote table.
	 * 
	 * @param businessName
	 * @param organization
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCustomerProductName(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		List<String> productNameList = session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.distinct(Projections.property("productName")))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.list();
		session.close();
		
		if (productNameList == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for productName: {}", productNameList);
			}
			return null;
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Product Names associated with businessName are: {}", productNameList);
		}
		return productNameList;
	}
	
	/**
	 * This method is responsible for getting all the product names from vbproduct.
	 * 
	 * @param organization
	 * @return List
	 */
	public List<String> getAllProductNames(VbOrganization organization) {
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		List<String> productNameList = session.createQuery("SELECT vb.productName FROM VbProduct vb WHERE vb.vbOrganization = :organization GROUP BY vb.productName")
				.setParameter("organization", organization).list();
		session.close();
		if (productNameList == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for productName: {}", productNameList);
			}
			return null;
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Product Names are: {}", productNameList);
		}
		return productNameList;
	}
	
	/**
	 * This method is responsible for getting all the sales executive names from vbproduct.
	 * 
	 * @param organization
	 * @return List
	 */
	public List<String> getAllSalesExecutives(VbOrganization organization) {
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		List<String> salesExecutiveList = session.createCriteria(VbEmployee.class)
		.setProjection(Projections.property("username"))
		.add(Restrictions.eq("vbOrganization", organization))
		.add(Restrictions.eq("employeeType", "SLE"))
		.list();
		session.close();
		if (salesExecutiveList == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for salesExecutive: {}", salesExecutiveList);
			}
			return null;
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Sales Executives are: {}", salesExecutiveList);
		}
		return salesExecutiveList;
	}

	@SuppressWarnings("unchecked")
	public List<VbDeliveryNotePayments> getFirstSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		Query query = session.createQuery("FROM VbDeliveryNotePayments");
		List<VbDeliveryNotePayments> paymentList = query.list();
		session.close();
		return paymentList;
	}

	@SuppressWarnings("unchecked")
	public List<VbSalesReturnProducts> getSecondSubReportData(ReportsCommand salesWiseReportCommand) {
		Session session = this.getSession();
		Query query = session.createQuery("FROM VbSalesReturnProducts");
		List<VbSalesReturnProducts> salesReturnList = query.list();
		session.close();
		return salesReturnList;
	}

	/**
	 * This method is responsible for getting the assigned organizations for management user.
	 * 
	 * @param userName - {@link String}
	 * @return organizationList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllAssignedOrganizations(String userName) {
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
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} organizations have been assigned for {}", organizationList.size(), userName);
		}
		return organizationList;
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
}
