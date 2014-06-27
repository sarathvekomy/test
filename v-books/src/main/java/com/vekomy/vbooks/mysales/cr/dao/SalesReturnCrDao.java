/**
 * com.vekomy.vbooks.mysales.cr.dao.SalesRetunCrDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: September 26, 2013
 */
package com.vekomy.vbooks.mysales.cr.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.SalesReturnViewResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnsResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform operations on Sales Return change
 * request in sales module.
 * 
 * @author Ankit
 * 
 */

public class SalesReturnCrDao extends MysalesCrBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnCrDao.class); // NOPMD by ankit on 23/10/13 5:17 PM

	/**
	 * This method is responsible to persist the sales returns change request
	 * data.
	 * 
	 * @param salesReturnCommands - {@link List}
	 * @param originalSRInvoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized void saveSalesReturns(
			List<ChangeRequestSalesReturnCommand> salesReturnCommands,
			String originalSRInvoiceNumber, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbSalesReturnChangeRequest instanceSalesReturn = new VbSalesReturnChangeRequest();
			if (instanceSalesReturn != null) {
				instanceSalesReturn.setCreatedBy(userName);
				Date date = new Date();
				instanceSalesReturn.setCreatedOn(date);
				instanceSalesReturn.setModifiedOn(date);
				instanceSalesReturn.setVbOrganization(organization);
				instanceSalesReturn.setStatus(CRStatus.PENDING.name());
				VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
				instanceSalesReturn.setVbSalesBook(salesBook);
				String businessName = null;
				String originalInvoiceName = null;
				String changedInvoiceName = null;
				String originalRemarks = null;
				String changedRemarks = null;
				String invoiceNo = null;
				// fetch original SR based on invoice Number
				VbSalesReturn originalSalesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
						.add(Restrictions.eq("invoiceNo", originalSRInvoiceNumber))
						.add(Restrictions.eq("createdBy", userName))
						.add(Restrictions.eq("flag", new Integer(1)))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				if (originalSalesReturn != null) {
					ChangeRequestSalesReturnCommand basicSRChangedDetails = salesReturnCommands.get(0);
					businessName = basicSRChangedDetails.getBusinessName();
					invoiceNo = basicSRChangedDetails.getInvoiceNo();
					originalInvoiceName = originalSalesReturn.getInvoiceName();
					changedInvoiceName = basicSRChangedDetails.getInvoiceName();
					originalRemarks = originalSalesReturn.getRemarks();
					changedRemarks = basicSRChangedDetails.getRemarks();
					instanceSalesReturn.setCrDescription(basicSRChangedDetails.getCrDescription());
					instanceSalesReturn.setBusinessName(businessName);
					instanceSalesReturn.setInvoiceNo(invoiceNo);
					if (originalRemarks == null || changedRemarks == null) {
						instanceSalesReturn.setRemarks(",0");
					} else {
						if (originalRemarks.equals(changedRemarks)) {
							instanceSalesReturn.setRemarks(originalRemarks.concat(",0"));
						} else {
							instanceSalesReturn.setRemarks(changedRemarks.concat(",1"));
						}
					}
					
					if (originalInvoiceName.equals(changedInvoiceName)) {
						instanceSalesReturn.setInvoiceName(originalInvoiceName.concat(",0"));
					} else {
						instanceSalesReturn.setInvoiceName(changedInvoiceName.concat(",1"));
					}
					instanceSalesReturn.setProductsGrandTotal("0.00");
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSalesReturnChangeRequest.");
					}
					session.save(instanceSalesReturn);
				}
				// persist SR products details
				List<VbSalesReturnProducts> originalSalesReturnProducts = new ArrayList<VbSalesReturnProducts>(
						originalSalesReturn.getVbSalesReturnProductses());
				String productName = null;
				String batchNumber = null;
				String originalDamagedQty = null;
				String originalResalableQty = null;
				String originalTotalQty = null;
				String changedDamagedQty = null;
				String changedResalableQty = null;
				String changedTotalQty = null;
				VbSalesReturnChangeRequestProducts instanceReturnProducts = null;
				for (ChangeRequestSalesReturnCommand changedSRProducts : salesReturnCommands) {
					for (VbSalesReturnProducts originalSRProducts : originalSalesReturnProducts) {
						productName = originalSRProducts.getProductName();
						batchNumber = originalSRProducts.getBatchNumber();
						originalDamagedQty = Integer.toString(originalSRProducts.getDamaged());
						originalResalableQty = Integer.toString(originalSRProducts.getResalable());
						originalTotalQty = Integer.toString(originalSRProducts.getTotalQty());
						changedDamagedQty = changedSRProducts.getDamaged();
						changedResalableQty = changedSRProducts.getResalable();
						changedTotalQty = changedSRProducts.getTotalQty();
						if (productName.equalsIgnoreCase(changedSRProducts.getProductName())
								&& batchNumber.equalsIgnoreCase(changedSRProducts.getBatchNumber())) {
							instanceReturnProducts = new VbSalesReturnChangeRequestProducts();
							if (originalDamagedQty.equals(changedDamagedQty)) {
								instanceReturnProducts.setDamaged(originalDamagedQty.concat(",0"));
								instanceReturnProducts.setTotalQty(originalTotalQty.concat(",0"));
							} else {
								instanceReturnProducts.setDamaged(changedDamagedQty.concat(",1"));
								instanceReturnProducts.setTotalQty(changedTotalQty.concat(",1"));
							}
							
							if (originalResalableQty.equals(changedResalableQty)) {
								instanceReturnProducts.setResalable(originalResalableQty.concat(",0"));
								instanceReturnProducts.setTotalQty(originalTotalQty.concat(",0"));
							} else {
								instanceReturnProducts.setResalable(changedResalableQty.concat(",1"));
								instanceReturnProducts.setTotalQty(changedTotalQty.concat(",1"));
							}
							instanceReturnProducts.setResalableCost("0.00");
							instanceReturnProducts.setDamagedCost("0.00");
							instanceReturnProducts.setTotalCost("0.00");
							instanceReturnProducts.setProductName(productName.concat(",0"));
							instanceReturnProducts.setBatchNumber(batchNumber.concat(",0"));
							instanceReturnProducts.setVbSalesReturnChangeRequest(instanceSalesReturn);
							
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSalesReturnProducts.");
							}
							session.save(instanceReturnProducts);
						}// if
					}// inner foreach
				}// outer foreach

				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, userName,
						organization, ENotificationTypes.SR_TXN_CR.name(),
						CRStatus.PENDING.name(), invoiceNo, businessName);
			}
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to fetch invoices from
	 * {@link VbDayBookChangeRequest} based on status and invoiceNumber
	 * 
	 * @param businessName - {@link String}
	 * @param salesReturnInvoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return invoicesalesReturnResults - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnViewResult> getOriginalSalesReturnDetails(
			String businessName, String salesReturnInvoiceNumber,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn salesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
				.add(Restrictions.eq("invoiceNo", salesReturnInvoiceNumber))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if (salesReturn != null) {
			List<VbSalesReturnProducts> salesReturnProductList = new ArrayList<VbSalesReturnProducts>(
					salesReturn.getVbSalesReturnProductses());
			List<SalesReturnViewResult> invoicesalesReturnResultsList = new ArrayList<SalesReturnViewResult>();
			if (!salesReturnProductList.isEmpty()) {
				Float productCost = new Float(0);
				SalesReturnViewResult result = null;
				String productName = null;
				String batchnumber = null;
				for (VbSalesReturnProducts product : salesReturnProductList) {
					result = new SalesReturnViewResult();
					result.setResaleCost(product.getResalableCost().toString());
					result.setDamageCost(product.getDamagedCost().toString());
					result.setId(product.getVbSalesReturn().getId());
					productName = product.getProductName();
					batchnumber = product.getBatchNumber();
					productCost = (Float) session.createCriteria(VbProductCustomerCost.class)
							.createAlias("vbProduct", "product")
							.createAlias("vbCustomer", "customer")
							.setProjection(Projections.property("cost"))
							.add(Restrictions.eq("product.productName", productName))
							.add(Restrictions.eq("product.batchNumber", batchnumber))
							.add(Restrictions.eq("product.vbOrganization", organization))
							.add(Restrictions.eq("customer.businessName", businessName))
							.add(Restrictions.eq("customer.vbOrganization", organization))
							.uniqueResult();
					if (productCost == null || productCost == 0) {
						productCost = (Float) session.createCriteria(VbProduct.class)
								.setProjection(Projections.property("costPerQuantity"))
								.add(Restrictions.eq("productName", productName))
								.add(Restrictions.eq("batchNumber", batchnumber))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
					}
					result.setProductCost(StringUtil.currencyFormat(productCost));
					
					invoicesalesReturnResultsList.add(result);
				}
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", invoicesalesReturnResultsList.size());
			}
			return invoicesalesReturnResultsList;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is Responsible to Check whether The Searched result is
	 * Applicable for SE CR or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link String}
	 */
	public String validateSESalesReturnChangeRequest(String invoiceNumber,
			VbOrganization organization) {
		String isAvailable = "n";
		Session session = this.getSession();
		VbSalesReturnChangeRequest changeRequest = (VbSalesReturnChangeRequest) session
				.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		 if(changeRequest != null) {
			VbSalesReturnChangeRequest changeRequestDayBook = (VbSalesReturnChangeRequest) session
					.createCriteria(VbSalesReturnChangeRequest.class)
					.add(Restrictions.eq("invoiceNo", invoiceNumber))
					.add(Restrictions.eq("status", CRStatus.PENDING.name()))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (changeRequestDayBook != null) {
				isAvailable = "y";
			} 
		}
		session.close();
		
		return isAvailable;
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from
	 * DB.
	 * 
	 * @param username - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestSalesReturnResult> getSalesReturnsOnLoad(
			String username, VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		Date date = new Date();
		List<VbSalesReturn> salesReturnsList = session.createCriteria(VbSalesReturn.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
				.add(Restrictions.eq("createdBy", username))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.addOrder(Order.desc("createdOn"))
				.list();
		session.close();
		
		if (!salesReturnsList.isEmpty()) {
			Float total = new Float(0);
			List<ChangeRequestSalesReturnResult> salesResultList = new ArrayList<ChangeRequestSalesReturnResult>();
			ChangeRequestSalesReturnResult result = null;
			for (VbSalesReturn salesReturn : salesReturnsList) {
				result = new ChangeRequestSalesReturnResult();
				result.setCreatedBy(salesReturn.getCreatedBy());
				result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
				total = getSalesReturnTotalCost(salesReturn.getId(), vbOrganization, username);
				result.setTotal(StringUtil.floatFormat(total));
				result.setId(salesReturn.getId());
				result.setBusinessName(salesReturn.getBusinessName());
				result.setInvoiceNo(salesReturn.getInvoiceNo());
				result.setInvoiceName(salesReturn.getInvoiceName());
				
				salesResultList.add(result);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", salesReturnsList.size());
			}
			return salesResultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to retrieve sum of total cost of provided
	 * salesReturnId {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return sumTotal - {@link Float}
	 * 
	 */
	public Float getSalesReturnTotalCost(Integer salesReturnId,
			VbOrganization organization, String userName) {
		Session session = this.getSession();
		Float sumTotal = (Float) session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.setProjection(Projections.sum("totalCost"))
				.add(Restrictions.eq("salesReturn.id", salesReturnId))
				.add(Restrictions.eq("salesReturn.vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}

	/**
	 * This method is used to get the Sales Return Based on Sales Return Id.
	 * 
	 * @param SalesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbSalesReturn - {@link vbSalesReturn}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbSalesReturn getSalesReturn(Integer salesReturnId,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		session.close();
		
		if(vbSalesReturn != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("vbSalesReturn: {}", vbSalesReturn);
			}
			return vbSalesReturn;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to provide the data for sales returns grid.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param businessName - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return salesReturnsResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestSalesReturnsResult> getSalesReturnGridData(
			Integer salesReturnId, String businessName, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbSalesReturnProducts> vbSalesReturnProductsList = session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.add(Restrictions.eq("salesReturn.id", salesReturnId))
				.addOrder(Order.asc("productName"))
				.list();
		session.close();
		
		if (!vbSalesReturnProductsList.isEmpty()) {
			ChangeRequestSalesReturnsResult result = null;
			List<ChangeRequestSalesReturnsResult> salesReturnsResultList = new ArrayList<ChangeRequestSalesReturnsResult>();
			for (VbSalesReturnProducts product : vbSalesReturnProductsList) {
				result = new ChangeRequestSalesReturnsResult();
				result.setProductName(product.getProductName());
				result.setBatchNumber(product.getBatchNumber());
				result.setdamagedQty(StringUtil.format(product.getDamaged()));
				result.setResaleableQty(StringUtil.format(product.getResalable()));
				result.setTotalQty(StringUtil.format(product.getTotalQty()));
				result.setResalableCost(StringUtil.currencyFormat(product.getResalableCost()));
				result.setDamagedCost(StringUtil.currencyFormat(product.getDamagedCost()));
				result.setTotalCost(StringUtil.currencyFormat(product.getTotalCost()));
				
				salesReturnsResultList.add(result);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", salesReturnsResultList.size());
			}
			return salesReturnsResultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for getting SR change request
	 * 
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestSalesReturnResult> getSalesReturnCrResults(
			String username, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbSalesReturnChangeRequest> salesReturnsCrList = session.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn"))
				.list();
		
		if (!salesReturnsCrList.isEmpty()) {
			String total = null;
			ChangeRequestSalesReturnResult result = null;
			List<ChangeRequestSalesReturnResult> salesResultList = new ArrayList<ChangeRequestSalesReturnResult>();
			for (VbSalesReturnChangeRequest salesReturnCr : salesReturnsCrList) {
				result = new ChangeRequestSalesReturnResult();
				result.setCreatedBy(salesReturnCr.getCreatedBy());
				result.setDate(DateUtils.format(salesReturnCr.getCreatedOn()));
				total = getSalesReturnProductsCrCost(session, salesReturnCr.getId(), organization, username);
				result.setTotal(StringUtil.floatFormat(Float.parseFloat(total)));
				result.setId(salesReturnCr.getId());
				result.setBusinessName(salesReturnCr.getBusinessName());
				result.setInvoiceNo(salesReturnCr.getInvoiceNo());
				result.setInvoiceName(salesReturnCr.getInvoiceName());
				
				salesResultList.add(result);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", salesResultList.size());
			}
			session.close();
			return salesResultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to retrieve sum of total cost of provided
	 * salesReturnId {@link VbSalesReturnChangeRequestProducts} from DB.
	 * 
	 * @param session - {@link Session}
	 * @param salesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return sumTotal - {@link String}
	 */
	private String getSalesReturnProductsCrCost(Session session, Integer salesReturnId,
			VbOrganization organization, String userName) {
		String sumTotal = (String) session.createCriteria(VbSalesReturnChangeRequestProducts.class)
				.createAlias("vbSalesReturnChangeRequest", "vbSalesReturnCr")
				.setProjection(Projections.sum("totalCost"))
				.add(Restrictions.eq("vbSalesReturnCr.id", salesReturnId))
				.add(Restrictions.eq("vbSalesReturnCr.vbOrganization", organization))
				.uniqueResult();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}

	/**
	 * This method is responsible to retrieve sales returns CR based on Sales
	 * Return ID {@link VbSalesReturnChangeRequest} from DB.
	 * 
	 * @param salesReturnCRId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return vbSalesReturn - {@link VbSalesReturn}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnViewResult> getSalesReturnProductsDetails(Integer salesReturnCRId, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session.get(VbSalesReturnChangeRequest.class, salesReturnCRId);
		List<SalesReturnViewResult> invoicesalesReturnResults=new ArrayList<SalesReturnViewResult>();
		VbSalesReturn salesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("invoiceNo", vbSalesReturnChangeRequest.getInvoiceNo()))
				.add(Expression.eq("flag", new Integer(1)))
				.add(Expression.eq("businessName", vbSalesReturnChangeRequest.getBusinessName()))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if(salesReturn != null){
			String productName=null;
			String batchNumber=null;
			String businessName=null;
			String fullName=null;
			List<VbSalesReturnProducts> salesReturnProductList=new ArrayList<VbSalesReturnProducts>(salesReturn.getVbSalesReturnProductses());
			List<VbSalesReturnChangeRequestProducts> salesReturnCRProductList=new ArrayList<VbSalesReturnChangeRequestProducts>(vbSalesReturnChangeRequest.getVbSalesReturnChangeRequestProductses());
			if(!salesReturnProductList.isEmpty()){
			Float productCost = new Float(0);
			fullName=getEmployeeFullName(vbSalesReturnChangeRequest.getCreatedBy(),organization);
			for(int i=0;i<salesReturnCRProductList.size();i++){
				businessName=vbSalesReturnChangeRequest.getBusinessName();
				VbSalesReturnProducts product=salesReturnProductList.get(i);
				SalesReturnViewResult result = new SalesReturnViewResult();
				productName=product.getProductName();
				batchNumber=product.getBatchNumber();
				result.setProductsGrandTotal(StringUtil.currencyFormat(salesReturn.getProductsGrandTotal()).toString());
				result.setBusinessName(businessName);
				result.setSalesExecutive(fullName);
				result.setInvoiceName(vbSalesReturnChangeRequest.getInvoiceName());
				result.setInvoiceNo(vbSalesReturnChangeRequest.getInvoiceNo());
				result.setCreatedOn(vbSalesReturnChangeRequest.getCreatedOn());
				result.setRemarks(vbSalesReturnChangeRequest.getRemarks());
				result.setCrDescription(vbSalesReturnChangeRequest.getCrDescription());
				result.setProductName(product.getProductName());
				result.setCreatedBy(vbSalesReturnChangeRequest.getCreatedBy());
				result.setBatchNumber(product.getBatchNumber());
				VbSalesReturnChangeRequestProducts products = (VbSalesReturnChangeRequestProducts) session.createCriteria(VbSalesReturnChangeRequestProducts.class)
						.createAlias("vbSalesReturnChangeRequest", "changeRequest")
						.add(Expression.eq("productName", product.getProductName().concat(",0")))
						.add(Expression.eq("batchNumber", product.getBatchNumber().concat(",0")))
						.add(Expression.eq("changeRequest.id", vbSalesReturnChangeRequest.getId()))
						.add(Expression.eq("changeRequest.vbOrganization", organization))
						.add(Expression.eq("changeRequest.businessName", vbSalesReturnChangeRequest.getBusinessName()))
						.uniqueResult();
				//vbSalesReturnProducts 
				if(products != null){
					result.setDamagedQty(products.getDamaged());
					result.setResaleQty(products.getResalable());
					result.setTotalProductQty(products.getTotalQty());
				}
				result.setResaleCost(StringUtil.currencyFormat(product.getResalableCost()).toString());
				result.setDamageCost(StringUtil.currencyFormat(product.getDamagedCost()).toString());
				result.setProductTotalCost(StringUtil.currencyFormat(product.getTotalCost()).toString());
				result.setId(product.getVbSalesReturn().getId());
				productCost = (Float) session.createCriteria(VbProductCustomerCost.class)
						.createAlias("vbProduct", "product")
						.createAlias("vbCustomer", "customer")
						.setProjection(Projections.property("cost"))
						.add(Expression.eq("product.productName", productName))
						.add(Expression.eq("product.batchNumber", batchNumber))
						.add(Expression.eq("product.vbOrganization", organization))
						.add(Expression.eq("customer.businessName", vbSalesReturnChangeRequest.getBusinessName()))
						.add(Expression.eq("customer.vbOrganization", organization))
						.uniqueResult();
				if (productCost == null || productCost == 0) {
					productCost = (Float) session.createCriteria(VbProduct.class)
							.setProjection(Projections.property("costPerQuantity"))
							.add(Expression.eq("productName", productName))
							.add(Expression.eq("batchNumber", batchNumber))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
				} 
				result.setProductCost(StringUtil.currencyFormat(productCost));	
				invoicesalesReturnResults.add(result);
			}
			if (_logger.isInfoEnabled()) {
				_logger.info("{} Original Sales Return Product List: {}", invoicesalesReturnResults);
			}
		}
		session.close();
		return invoicesalesReturnResults;
		}
		else{
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This method is responsible for Approving Sales Return CR.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param salesReturnId - {@link Integer}
	 * @param salesReturnList {@link List}
	 * @param status - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void getApprovedSalesReturnCR(VbOrganization vbOrganization,
			String userName, String status, Integer salesReturnId,
			List<ChangeRequestSalesReturnCommand> salesReturnList) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session.get(VbSalesReturnChangeRequest.class, salesReturnId);
		if (vbSalesReturnChangeRequest != null) {
			Transaction txn = null;
			try {
				txn = session.beginTransaction();
				VbSalesReturn vbSalesReturn = null;
				VbSalesBook salesBook = null;
				Float grandTotal = new Float(0.00);
				Date date = new Date();
				String businessName = vbSalesReturnChangeRequest.getBusinessName();
				String invoiceNo = vbSalesReturnChangeRequest.getInvoiceNo();
				if (CRStatus.APPROVED.name().equalsIgnoreCase(status) && salesReturnList != null) {
					vbSalesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("invoiceNo", invoiceNo))
							.add(Restrictions.eq("status", CRStatus.APPROVED.name()))
							.add(Restrictions.eq("flag", new Integer(1)))
							.add(Restrictions.eq("vbOrganization", vbOrganization))
							.uniqueResult();
					// if Approval Sales Return is Approved
					if (vbSalesReturn != null) {
						//reverting back debit txn amount based on SR CR invoice number
						revertDebitTransactionAmount(session,businessName,invoiceNo,vbOrganization,userName);
						//reverting back credit txn amount based on SR CR invoice number
						revertCreditTransactionAmount(session,businessName,invoiceNo,vbOrganization,userName);
						
						// update vb_sales_return_change_request and vb_sales_return_change_request_product with SR CR Changed data
						updateSalesReturnChangeRequest(session, userName, vbSalesReturnChangeRequest, salesReturnList, status);
						// update previous sales return get VbSalesReturn based on Sales Return Change Request Invoice Number
						List<VbSalesReturnChangeRequestProducts> productsSet = new ArrayList<VbSalesReturnChangeRequestProducts>(
								vbSalesReturnChangeRequest.getVbSalesReturnChangeRequestProductses());
						// persist vb_sales_return_change_request and vb_sales_return_change_request details to vb_sales_reurn and vb_sales_return_products
						persistSalesReturnCRDetails(session, userName, vbSalesReturnChangeRequest, productsSet, vbOrganization);
						// considered Approval Sales Return CR as new Sales Return CR

						salesBook = getVbSalesBookNoFlag(session, vbSalesReturnChangeRequest.getCreatedBy(), vbOrganization);
						List<VbSalesReturnProducts> approvedProductsList = new ArrayList<VbSalesReturnProducts>(vbSalesReturn.getVbSalesReturnProductses());
						// update SalesBook Products with Approved Sales Return Resalable Qty
						updateSalesBookProductsSRCRResalableQty(session,salesBook,approvedProductsList,salesReturnList,vbOrganization,userName);
						
						vbSalesReturn.setFlag(new Integer(0));
						vbSalesReturn.setModifiedBy(userName);
						session.update(vbSalesReturn);
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating vbSalesReturn." , vbSalesReturn);
						}
						if (vbSalesReturnChangeRequest.getProductsGrandTotal().contains(",1")) {
							grandTotal = Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",1", ""));
						} else {
							grandTotal = Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",0", ""));
						}
						String salesReturnBusinessName = vbSalesReturn.getBusinessName();
						// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
						updateProductsAndPreviousCredits(session, grandTotal, salesReturnBusinessName, 
								 vbOrganization, userName, invoiceNo);
						
						// if Approval Sales Return is still pending
					} else {
						// update VbSalesReturn based on Change Request details and get VbSalesReturn based on Sales Return Change Request Invoice Number
						vbSalesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
								.add(Restrictions.eq("vbOrganization", vbOrganization))
								.add(Restrictions.eq("createdBy", vbSalesReturnChangeRequest.getCreatedBy()))
								.add(Restrictions.eq("status", CRStatus.PENDING.name()))
								.add(Restrictions.eq("flag", new Integer(1)))
								.add(Restrictions.eq("invoiceNo", invoiceNo))
								.uniqueResult();
						if (vbSalesReturn != null) {
							vbSalesReturn.setFlag(new Integer(0));
							vbSalesReturn.setModifiedBy(userName);
							vbSalesReturn.setStatus(CRStatus.APPROVED.name());
							session.update(vbSalesReturn);
							
							if(_logger.isDebugEnabled()) {
								_logger.debug("Updating VbSalesReturn.");
							}
							// update vb_sales_return_change_request and vb_sales_return_change_request_product with SR CR Changed data
							updateSalesReturnChangeRequest(session, userName, vbSalesReturnChangeRequest, salesReturnList, status);
							// Persist VbSalesReturn, VbSalesReturnProducts based on VbSalesReturnChangeRequestProducts via reference as invoiceNumber
							List<VbSalesReturnChangeRequestProducts> productsSet = new ArrayList<VbSalesReturnChangeRequestProducts>(
									vbSalesReturnChangeRequest.getVbSalesReturnChangeRequestProductses());
							// persist vb_sales_return_change_request and vb_sales_return_change_request details to vb_sales_reurn and vb_sales_return_products
							persistSalesReturnCRDetails(session, userName, vbSalesReturnChangeRequest, productsSet, vbOrganization);
							
							// Updating Sales_Return_Product with Sales Return Resalable Qty.
							VbSalesBookProducts vbSalesBookProducts = null;
							Integer resalableProductQty = null;
							salesBook = getVbSalesBookNoFlag(session, vbSalesReturnChangeRequest.getCreatedBy(), vbOrganization);
							for (ChangeRequestSalesReturnCommand command : salesReturnList) {
								vbSalesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
										.add(Restrictions.eq("vbSalesBook", salesBook))
										.add(Restrictions.eq("productName", command.getProductName()))
										.add(Restrictions.eq("batchNumber", command.getBatchNumber()))
										.uniqueResult();
								resalableProductQty = Integer.parseInt(command.getResalable());
								if (vbSalesBookProducts != null && resalableProductQty != null) {
									vbSalesBookProducts.setQtyClosingBalance(vbSalesBookProducts.getQtyClosingBalance() + resalableProductQty);
									vbSalesBookProducts.setQtyOpeningBalance(vbSalesBookProducts.getQtyOpeningBalance() + resalableProductQty);
									
									if (_logger.isDebugEnabled()) {
										_logger.debug("Updating vbSalesBookProducts.");
									}
									session.update(vbSalesBookProducts);
								}
							}
							if (vbSalesReturnChangeRequest.getProductsGrandTotal().contains(",1")) {
								grandTotal = Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",1", ""));
							} else {
								grandTotal = Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",0", ""));
							}
							String salesReturnBusinessName = vbSalesReturn.getBusinessName();
							// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
							updateProductsAndPreviousCredits(session, grandTotal, salesReturnBusinessName, 
									 vbOrganization, userName, invoiceNo);
						}
					}
					vbSalesReturnChangeRequest.setStatus(CRStatus.APPROVED.name());
				} else {
					vbSalesReturnChangeRequest.setStatus(CRStatus.DECLINE.name());
				}
				
				vbSalesReturnChangeRequest.setModifiedBy(userName);
				vbSalesReturnChangeRequest.setModifiedOn(date);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating vbSalesReturnChangeRequest for approved.");
				}
				session.update(vbSalesReturnChangeRequest);

				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, vbSalesReturnChangeRequest.getVbSalesBook().getSalesExecutive(), 
						vbOrganization, ENotificationTypes.SR_TXN_CR.name(), status, invoiceNo, businessName);
				txn.commit();
			} catch (HibernateException exception) {
				if(txn != null) {
					txn.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				if(_logger.isErrorEnabled()){
					_logger.error(message);
				}
				throw new DataAccessException(message);
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()){
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for update previous credits if exists for businessName based on SR CR returned amount(grand total)
	 * 
	 * @param session - {@link Session}
	 * @param grandTotal - {@link Float} 
	 * @param salesReturnBusinessName - {@link String} 
	 * @param vbOrganization - {@link VbOrganization} 
	 * @param userName - {@link String} 
	 * @param invoiceNo - {@link String} 
	 * @throws DataAccessException 
	 */
	private void updateProductsAndPreviousCredits(Session session,
			Float grandTotal, String salesReturnBusinessName,
			VbOrganization vbOrganization, String userName, String invoiceNo) throws DataAccessException {
		Float grandTotalCost = grandTotal;
		VbCustomerDebitTransaction approvedDebitTxn = null;
		String newInvoiceNo = invoiceNo;
		Date date = new Date();
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
		if (grandTotalCost > 0) {
			Query customerInfoQuery = null;
			while (grandTotal != 0) {
				customerInfoQuery = session.createQuery(
								"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
								+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
								+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
						.setParameter("businessName", salesReturnBusinessName)
						.setParameter("vbOrganization", vbOrganization)
						.setParameter("due", new Float("0"));
				VbCustomerCreditInfo creditInfo = getSingleResultOrNull(customerInfoQuery);
				
				if (creditInfo != null) {
					// persisting Customer debit Txn
					approvedDebitTxn = new VbCustomerDebitTransaction();
					creditInfo.setModifiedBy(userName);
					creditInfo.setModifiedOn(date);
					String existingInvoiceNo = creditInfo.getDebitTo();
					newInvoiceNo = invoiceNo;
					if (existingInvoiceNo != null) {
						creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(newInvoiceNo));
					} else {
						creditInfo.setDebitTo(newInvoiceNo);
					}
					Float existingDue = creditInfo.getDue();
					if (grandTotal < existingDue) {
						creditInfo.setDue(existingDue - grandTotal);
						approvedDebitTxn.setAmount(grandTotal);
						grandTotal = new Float(0.0);
					} else {
						grandTotal = grandTotal - existingDue;
						creditInfo.setDue(new Float("0.00"));
						approvedDebitTxn.setAmount(existingDue);
					}
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo.");
					}
					session.update(creditInfo);
					
					// persisting Customer Debit Txn for Payback his credit amount.
					approvedDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
					approvedDebitTxn.setBusinessName(salesReturnBusinessName);
					approvedDebitTxn.setDebitTo(invoiceNo);
					approvedDebitTxn.setCreatedBy(userName);
					approvedDebitTxn.setCreatedOn(date);
					approvedDebitTxn.setModifiedOn(date);
					approvedDebitTxn.setFlag(new Integer(1));
					approvedDebitTxn.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerDebitTransaction.");
					}
					session.save(approvedDebitTxn);
				} else {
					saveCustomerAdvanceInfo(session, salesReturnBusinessName, newInvoiceNo, vbOrganization, grandTotalCost, userName,invoiceNo);
					grandTotal = new Float("0");
				}
			}
		}
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
	}

	/**
	 * This method is responsible for update advanceInfo amount if credit not exist for businessName based on SR CR grandTotal
	 * 
	 * @param session - {@link Session}
	 * @param newInvoiceNo - {@link String}
	 * @param grandTotalCost - {@link Float} 
	 * @param salesReturnBusinessName - {@link String} 
	 * @param vbOrganization - {@link VbOrganization} 
	 * @param userName - {@link String} 
	 * @param invoiceNo - {@link String} 
	 * @throws DataAccessException 
	 */
	private void saveCustomerAdvanceInfo(Session session,String salesReturnBusinessName, String newInvoiceNo,
			VbOrganization vbOrganization, Float grandTotalCost,
			String userName, String invoiceNo) throws DataAccessException {
		VbCustomerCreditTransaction approvedCreditTxn = null;
		VbCustomerAdvanceInfo advanceInfo = null;
		Date date = new Date();
		advanceInfo = new VbCustomerAdvanceInfo();
		approvedCreditTxn = new VbCustomerCreditTransaction();
		if (grandTotalCost < 0) {
			Float newBalance = -(grandTotalCost);
			advanceInfo.setAdvance(newBalance);
			advanceInfo.setBalance(newBalance);
			approvedCreditTxn.setAmount(newBalance);
		} else {
			advanceInfo.setAdvance(grandTotalCost);
			advanceInfo.setBalance(grandTotalCost);
			approvedCreditTxn.setAmount(grandTotalCost);
		}
		advanceInfo.setBusinessName(salesReturnBusinessName);
		String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(vbOrganization);
		advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
		advanceInfo.setCreatedBy(userName);
		advanceInfo.setCreatedOn(date);
		advanceInfo.setModifiedOn(date);
		advanceInfo.setCreditFrom(newInvoiceNo);
		advanceInfo.setVbOrganization(vbOrganization);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerAdvanceInfo.");
		}
		session.save(advanceInfo);
		
		// persisting Customer Credit txn case of extra amount
		approvedCreditTxn.setBusinessName(salesReturnBusinessName);
		approvedCreditTxn.setCreditFrom(newInvoiceNo);
		approvedCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
		approvedCreditTxn.setCreatedBy(userName);
		approvedCreditTxn.setCreatedOn(date);
		approvedCreditTxn.setModifiedOn(date);
		approvedCreditTxn.setFlag(new Integer(1));
		approvedCreditTxn.setVbOrganization(vbOrganization);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerCreditTransaction.");
		}
		session.save(approvedCreditTxn);
	}

	/**
	 * This method is responsible for revert back and update vbSalesBookProducts opening and closing stock based on SR CR changed resalable Qty
	 * 
	 * @param session - {@link Session}
	 * @param salesBook - {@link VbSalesBook}
	 * @param approvedProductsList - {@link List}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException 
	 */
	private void updateSalesBookProductsSRCRResalableQty(Session session,VbSalesBook salesBook,List<VbSalesReturnProducts> approvedProductsList,List<ChangeRequestSalesReturnCommand> salesReturnList, 
			VbOrganization vbOrganization, String userName) throws DataAccessException {
		VbSalesBookProducts vbSalesBookProducts = null;	
		String productName = null;
		String batchNumber = null;
		Integer originalResalableQty = new Integer(0);
		Integer qtyClosing = new Integer(0);
		Integer qtyOpening = new Integer(0); 
		for (VbSalesReturnProducts salesReturnProduct : approvedProductsList) {
			productName = salesReturnProduct.getProductName();
			batchNumber = salesReturnProduct.getBatchNumber();
			originalResalableQty = salesReturnProduct.getResalable();
				vbSalesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
						.add(Restrictions.eq("vbSalesBook", salesBook))
						.add(Restrictions.eq("productName", productName))
						.add(Restrictions.eq("batchNumber", batchNumber))
						.uniqueResult();
				if (vbSalesBookProducts != null) {
					qtyClosing = vbSalesBookProducts.getQtyClosingBalance();
					qtyOpening = vbSalesBookProducts.getQtyOpeningBalance();
					vbSalesBookProducts.setQtyClosingBalance(qtyClosing - originalResalableQty);
					vbSalesBookProducts.setQtyOpeningBalance(qtyOpening - originalResalableQty);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating vbSalesBookProducts." , vbSalesBookProducts);
					}
					session.update(vbSalesBookProducts);
				}
			}
		// update SalesBook Products with CR'd Sales Return Resalable Qty
		String changedProductName = null;
		String changedBatchNumber = null;
		Integer changedResalableProductQty = new Integer(0);
		Integer closingQty = new Integer(0);
		Integer openingQty = new Integer(0);
		for (ChangeRequestSalesReturnCommand salesReturnCRCommand : salesReturnList) {
			changedProductName = salesReturnCRCommand.getProductName();
			changedBatchNumber = salesReturnCRCommand.getBatchNumber();
			changedResalableProductQty = Integer.parseInt(salesReturnCRCommand.getResalable());
			vbSalesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
					.add(Restrictions.eq("vbSalesBook", salesBook))
					.add(Restrictions.eq("productName", changedProductName))
					.add(Restrictions.eq("batchNumber", changedBatchNumber))
					.uniqueResult();
			
			if (vbSalesBookProducts != null && changedResalableProductQty != null) {
				closingQty = vbSalesBookProducts.getQtyClosingBalance();
				openingQty = vbSalesBookProducts.getQtyOpeningBalance();		
				vbSalesBookProducts.setQtyClosingBalance(closingQty + changedResalableProductQty);
				vbSalesBookProducts.setQtyOpeningBalance(openingQty	+ changedResalableProductQty);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating vbSalesBookProducts." , vbSalesBookProducts);
				}
				session.update(vbSalesBookProducts);
			}
		}
	}

	/** 
	 * This method is responsible for reverting back credit txn data based on SR CR invoice number
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException 
	 */
	private void revertCreditTransactionAmount(Session session,String businessName, String invoiceNo,
			VbOrganization vbOrganization, String userName) throws DataAccessException {
		VbCustomerCreditTransaction customerCreditTxn = null;
		VbCustomerCreditInfo creditInfoRecord = null;
		VbCustomerAdvanceInfo advanceInfoRecord = null;
		Date date = new Date();
		Float creditedAmount = new Float(0.00);
		String debitTo = null;
		Float defaultBalance = new Float(0.00);
		customerCreditTxn = (VbCustomerCreditTransaction) session.createCriteria(VbCustomerCreditTransaction.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("creditFrom", invoiceNo))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		
		if (customerCreditTxn != null) {
			 creditedAmount = customerCreditTxn.getAmount();
			 debitTo = customerCreditTxn.getDebitTo();
			advanceInfoRecord = (VbCustomerAdvanceInfo) session.createCriteria(VbCustomerAdvanceInfo.class)
					.add(Restrictions.eq("businessName", businessName))
					.add(Restrictions.eq("invoiceNo", debitTo))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			
			VbCustomerCreditInfo creditInfoData = null;
			VbCustomerCreditTransaction creditTrx = null;
			if (advanceInfoRecord != null) {
				Float advanceInfoRevertAmount = advanceInfoRecord.getBalance() - creditedAmount;
				if (advanceInfoRevertAmount >= 0) {
					advanceInfoRecord.setBalance(advanceInfoRevertAmount);
					advanceInfoRecord.setAdvance(advanceInfoRevertAmount);
				} else {
					Float revertAmount = -(advanceInfoRevertAmount);
					creditTrx = new VbCustomerCreditTransaction();
					advanceInfoRecord.setBalance(defaultBalance);
					advanceInfoRecord.setAdvance(advanceInfoRevertAmount);

					creditInfoData = new VbCustomerCreditInfo();
					creditInfoData.setDue(revertAmount);
					creditInfoData.setBalance(revertAmount);
					creditInfoData.setBusinessName(businessName);
					String customerCreditInvoiceNo = generateCustomerCreditInfoInvoiceNumber(vbOrganization);
					creditInfoData.setInvoiceNo(customerCreditInvoiceNo);
					creditInfoData.setCreatedBy(userName);
					creditInfoData.setCreatedOn(date);
					creditInfoData.setCreditFrom(invoiceNo);
					creditInfoData.setModifiedOn(date);
					creditInfoData.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("persisting VbCustomerCreditInfo." , creditInfoData);
					}
					session.save(creditInfoData);
					// persisting VbCustomerCreditTxn
					creditTrx.setCreditFrom(invoiceNo);
					creditTrx.setBusinessName(businessName);
					creditTrx.setDebitTo(customerCreditInvoiceNo);
					creditTrx.setAmount(revertAmount);
					creditTrx.setCreatedBy(userName);
					creditTrx.setCreatedOn(date);
					creditTrx.setModifiedOn(date);
					creditTrx.setFlag(new Integer(1));
					creditTrx.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCustomerCreditTransaction." , creditTrx);
					}
					session.save(creditTrx);
				}
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerAdvanceInfo." , advanceInfoRecord);
				}
				session.update(advanceInfoRecord);
			} 
			// check same Customer Credit debitTo in Customer Credit Info
			creditInfoRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
					.add(Restrictions.eq("businessName", businessName))
					.add(Restrictions.eq("invoiceNo", debitTo))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			
			VbCustomerAdvanceInfo advanceInfoData = null;
			VbCustomerCreditTransaction creditIndoCreditTrx = null;
			if (creditInfoRecord != null) {
				// condition for reverting -ve value to credit info due.
				Float creditTxnRevertAmount = creditInfoRecord.getDue() - creditedAmount;
				if (creditTxnRevertAmount >= 0) {
					creditInfoRecord.setDue(creditTxnRevertAmount);
					creditInfoRecord.setBalance(creditTxnRevertAmount);
				} else {
					Float revertAdvance = -(creditTxnRevertAmount);
					creditIndoCreditTrx = new VbCustomerCreditTransaction();
					creditInfoRecord.setDue(defaultBalance);
					creditInfoRecord.setBalance(creditInfoRecord.getBalance() - creditedAmount);

					advanceInfoData = new VbCustomerAdvanceInfo();
					advanceInfoData.setAdvance(revertAdvance);
					advanceInfoData.setBalance(revertAdvance);
					advanceInfoData.setBusinessName(businessName);
					String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(vbOrganization);
					advanceInfoData.setInvoiceNo(advanceInfoInvoiceNumber);
					advanceInfoData.setCreatedBy(userName);
					advanceInfoData.setCreatedOn(date);
					advanceInfoData.setCreditFrom(invoiceNo);
					advanceInfoData.setModifiedOn(date);
					advanceInfoData.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("persisting VbCustomerAdvanceInfo." , advanceInfoData);
					}
					session.save(advanceInfoData);
					
					// persisting VbCustomerCreditTxn
					creditIndoCreditTrx.setCreditFrom(invoiceNo);
					creditIndoCreditTrx.setBusinessName(businessName);
					creditIndoCreditTrx.setDebitTo(advanceInfoInvoiceNumber);
					creditIndoCreditTrx.setAmount(revertAdvance);
					creditIndoCreditTrx.setCreatedBy(userName);
					creditIndoCreditTrx.setCreatedOn(date);
					creditIndoCreditTrx.setModifiedOn(date);
					creditIndoCreditTrx.setFlag(new Integer(1));
					creditIndoCreditTrx.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCustomerCreditTransaction." , creditIndoCreditTrx);
					}
					session.save(creditIndoCreditTrx);
				}
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerCreditInfo.");
				}
				session.update(creditInfoRecord);
			} 
			customerCreditTxn.setModifiedBy(userName);
			customerCreditTxn.setModifiedOn(date);
			customerCreditTxn.setFlag(new Integer(0));
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomerCreditTransaction." , customerCreditTxn);
			}
			session.update(customerCreditTxn);
		} 
	}

	/**
	 * This method is responsible for revert back debit txn amount based on SR CR approval invoice number.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException 
	 */
	private void revertDebitTransactionAmount(Session session,String businessName, String invoiceNo,
			     VbOrganization vbOrganization, String userName) throws DataAccessException {
		// Based on Approval Invoice Number Check Customer Debit Txn and update Customer Credit Info table.
		VbCustomerCreditInfo creditInfoRecord = null;
		VbCustomerDebitTransaction customerDebitTxn = null;
		Float debitedAmount = new Float(0.00);
		String creditFrom = null;
		customerDebitTxn = (VbCustomerDebitTransaction) session.createCriteria(VbCustomerDebitTransaction.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("debitTo", invoiceNo))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (customerDebitTxn != null) {
			 debitedAmount = customerDebitTxn.getAmount();
			 creditFrom = customerDebitTxn.getCreditFrom();
			creditInfoRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
					.add(Restrictions.eq("businessName", businessName))
					.add(Restrictions.eq("invoiceNo", creditFrom))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			
			if (creditInfoRecord != null) {
				Float revertAmount = debitedAmount + creditInfoRecord.getDue();
				creditInfoRecord.setDue(revertAmount);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerCreditInfo." , creditInfoRecord);
				}
				session.update(creditInfoRecord);
			} 
			customerDebitTxn.setModifiedBy(userName);
			customerDebitTxn.setModifiedOn(new Date());
			customerDebitTxn.setFlag(new Integer(0));
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Fetching VbCustomerDebitTransaction." , customerDebitTxn);
			}
			session.update(customerDebitTxn);
		} 
	}

	/**
	 * This method is responsible for persisting Sales Return CR Details to
	 * vb_sales-return table with flag=1
	 * 
	 * @param session
	 * @param userName
	 * @param vbSalesReturnChangeRequest
	 * @param productsSet
	 * @throws DataAccessException 
	 */
	private void persistSalesReturnCRDetails(Session session, String userName,
			VbSalesReturnChangeRequest vbSalesReturnChangeRequest,
			List<VbSalesReturnChangeRequestProducts> productsSet,
			VbOrganization vbOrganization) throws DataAccessException {
		// Persist VbSalesReturn based on Change Request details
		VbSalesReturn instanceSalesReturn = new VbSalesReturn();
		Date date = new Date();
		instanceSalesReturn.setCreatedBy(vbSalesReturnChangeRequest.getCreatedBy());
		instanceSalesReturn.setModifiedBy(userName);
		instanceSalesReturn.setCreatedOn(date);
		instanceSalesReturn.setModifiedOn(date);
		instanceSalesReturn.setVbOrganization(vbOrganization);
		instanceSalesReturn.setStatus(CRStatus.APPROVED.name());
		VbSalesBook salesBook = getVbSalesBook(session, vbSalesReturnChangeRequest.getCreatedBy(), vbOrganization);
		if (salesBook == null) {
			salesBook = getVbSalesBookNoFlag(session, vbSalesReturnChangeRequest.getCreatedBy(), vbOrganization);
		}
		instanceSalesReturn.setVbSalesBook(salesBook);
		instanceSalesReturn.setBusinessName(vbSalesReturnChangeRequest.getBusinessName());
		instanceSalesReturn.setInvoiceNo(vbSalesReturnChangeRequest.getInvoiceNo());
		if (vbSalesReturnChangeRequest.getInvoiceName().contains(",1")) {
			instanceSalesReturn.setInvoiceName(vbSalesReturnChangeRequest.getInvoiceName().replace(",1", ""));
		} else {
			instanceSalesReturn.setInvoiceName(vbSalesReturnChangeRequest.getInvoiceName().replace(",0", ""));
		}
		if (vbSalesReturnChangeRequest.getProductsGrandTotal().contains(",1")) {
			instanceSalesReturn.setProductsGrandTotal(Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",1", "")));
		} else {
			instanceSalesReturn.setProductsGrandTotal(Float.parseFloat(vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",0", "")));
		}
		instanceSalesReturn.setFlag(new Integer(1));
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbSalesReturn.");
		}
		session.save(instanceSalesReturn);
		
		// persisting vbSalesReturnProducts based on CR SalesReturnProducts
		for (VbSalesReturnChangeRequestProducts changedProducts : productsSet) {
			VbSalesReturnProducts instanceReturnProducts = new VbSalesReturnProducts();
			instanceReturnProducts.setProductName(changedProducts.getProductName().replace(",0", ""));
			instanceReturnProducts.setBatchNumber(changedProducts.getBatchNumber().replace(",0", ""));
			instanceReturnProducts.setVbSalesReturn(instanceSalesReturn);
			if (changedProducts.getDamaged().contains(",1")) {
				instanceReturnProducts.setDamaged(Integer.parseInt(changedProducts.getDamaged().replace(",1", "")));
			} else {
				instanceReturnProducts.setDamaged(Integer.parseInt(changedProducts.getDamaged().replace(",0", "")));
			}
			if (changedProducts.getResalable().contains(",1")) {
				instanceReturnProducts.setResalable(Integer.parseInt(changedProducts.getResalable().replace(",1", "")));
			} else {
				instanceReturnProducts.setResalable(Integer.parseInt(changedProducts.getResalable().replace(",0", "")));
			}
			if (changedProducts.getTotalQty().contains(",1")) {
				instanceReturnProducts.setTotalQty(Integer.parseInt(changedProducts.getTotalQty().replace(",1", "")));
			} else {
				instanceReturnProducts.setTotalQty(Integer.parseInt(changedProducts.getTotalQty().replace(",0", "")));
			}
			if (changedProducts.getTotalCost().contains(",1")) {
				instanceReturnProducts.setTotalCost(Integer.parseInt(changedProducts.getTotalCost().replace(",1", "")));
			} else {
				instanceReturnProducts.setTotalCost(Integer.parseInt(changedProducts.getTotalCost().replace(",0", "")));
			}
			if (changedProducts.getResalableCost().contains(",1")) {
				instanceReturnProducts.setResalableCost(Float.parseFloat(changedProducts.getResalableCost().replace(",1", "")));
			} else {
				instanceReturnProducts.setResalableCost(Float.parseFloat(changedProducts.getResalableCost().replace(",0", "")));
			}
			if (changedProducts.getDamagedCost().contains(",1")) {
				instanceReturnProducts.setDamagedCost(Float.parseFloat(changedProducts.getDamagedCost().replace(",1", "")));
			} else {
				instanceReturnProducts.setDamagedCost(Float.parseFloat(changedProducts.getDamagedCost().replace(",0", "")));
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSalesReturnProducts.");
			}
			session.save(instanceReturnProducts);
		}
	}

	/**
	 * This method is responsible for updating vb_sales_retrn_change_request
	 * table with damaged_cost,resalable_cost,total_cost details SR CR.
	 * 
	 * @param session
	 * @param userName
	 * @param vbSalesReturn
	 * @param salesReturnCommandList
	 * @param status
	 * @throws DataAccessException 
	 */
	private void updateSalesReturnChangeRequest(Session session,
			String userName, VbSalesReturnChangeRequest vbSalesReturn,
			List<ChangeRequestSalesReturnCommand> salesReturnCommandList,
			String status) throws DataAccessException {
		Float grandTotal = new Float(0);
		VbSalesReturnChangeRequestProducts vbSalesReturnCRProducts = null;
		for (ChangeRequestSalesReturnCommand salesReturnCommand : salesReturnCommandList) {
			grandTotal = Float.parseFloat(salesReturnCommand.getGrandTotalCost());
			vbSalesReturnCRProducts = (VbSalesReturnChangeRequestProducts) session.createCriteria(VbSalesReturnChangeRequestProducts.class)
					.add(Restrictions.eq("vbSalesReturnChangeRequest", vbSalesReturn))
					.add(Restrictions.eq("productName", salesReturnCommand.getProductName().concat(",0")))
					.add(Restrictions.eq("batchNumber", salesReturnCommand.getBatchNumber().concat(",0")))
					.uniqueResult();
			
			if (vbSalesReturnCRProducts != null) {
				vbSalesReturnCRProducts.setResalableCost(salesReturnCommand.getResalableCost().concat(",1"));
				vbSalesReturnCRProducts.setDamagedCost(salesReturnCommand.getDamagedCost().concat(",1"));
				vbSalesReturnCRProducts.setTotalCost(salesReturnCommand.getTotalCost().concat(",1"));
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbSalesReturnChangeRequestProducts.");
			}
			session.update(vbSalesReturnCRProducts);
		}
		vbSalesReturn.setModifiedBy(userName);
		vbSalesReturn.setModifiedOn(new Date());
		vbSalesReturn.setProductsGrandTotal(Float.toString(grandTotal).concat(",1"));
		vbSalesReturn.setStatus(status);
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Updating VbSalesReturnChangeRequest.");
		}
		session.update(vbSalesReturn);
	}

	/**
	 * This method is responsible to get {@link VbSalesReturn} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param invoiceNumber - {@link String}
	 * @param salesReturnId - {@link Integer}
	 * @param userName - {@link String}
	 * @return srId - {@link String}
	 */
	public Integer getSalesReturnBasedOnInvoiceNo(VbOrganization organization,
			String invoiceNumber, Integer salesReturnId, String userName) {
		Session session = this.getSession();
		Integer srId = new Integer(0);
		Query query = session.createQuery(
				"SELECT MAX(vbs.id) FROM VbSalesReturn vbs where vbs.invoiceNo LIKE :invoiceNo AND " +
				"vbs.flag= :flag AND vbs.id <= :salesReturnId AND vbs.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(1))
				.setParameter("salesReturnId", salesReturnId)
				.setParameter("vbOrganization", organization);
		      srId = getSingleResultOrNull(query);
		if (srId == null) {
			VbSalesReturn salesReturn = (VbSalesReturn) session
					.createCriteria(VbSalesReturn.class)
					.add(Restrictions.eq("invoiceNo", invoiceNumber))
					.add(Restrictions.eq("flag", new Integer(1)))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			srId = salesReturn.getId();
		}
		session.close();
		
		return srId;
	}

	/**
	 * This method is responsible to get {@link VbSalesReturn} based on
	 * {@link VbOrganization} and invoiceNo and salesReturnId.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param salesReturnId -{@link Integer}
	 * @param invoiceNumber -{@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * @throws ParseException
	 * 
	 */
	public String fetchSalesReturnCreationTime(Integer salesReturnId,
			String invoiceNumber, VbOrganization organization)
			throws ParseException {
		Session session = this.getSession();
		String isSalesReturnCRExpired = "n";
		Date salesReturnCreationTime;
		Date currentDateTime = new Date();
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime = format.parse(d2);
		VbSalesReturn vbSalesReturn = null;
		vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		if (vbSalesReturn != null) {
			salesReturnCreationTime = vbSalesReturn.getCreatedOn();
			String creationTime = DateUtils.formatDateWithTimestamp(salesReturnCreationTime);
			Date salesCreation = format.parse(creationTime);
			Integer totalMin = calculateMinDiff(currentTime, salesCreation);
			if (totalMin > 10) {
				isSalesReturnCRExpired = "y";
			}
		}
		session.close();
		return isSalesReturnCRExpired;
	}

	/**
	 * This method is responsible for getting sales return to fetch status based
	 * on sales return id.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param salesReturnId - {@link Integer}
	 * @param userName - {@link String}
	 * @return salesReturn - {@link VbSalesReturn}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbSalesReturn getSalesReturnStatus(VbOrganization organization,
			Integer salesReturnId, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn salesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		session.close();
		
		if (salesReturn != null) {
			return salesReturn;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()){
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This Method is Responsible to Check whether The Transaction Sales Return
	 * for given Invoice Number is Available or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return salesReturnCRId - {@link Integer}
	 */
	public Integer checkTransactionSalesReturn(String invoiceNumber,
			VbOrganization organization) {
		Integer salesReturnCRId = new Integer(0);
		Session session = this.getSession();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session
				.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.uniqueResult();
		session.close();
		
		if (vbSalesReturnChangeRequest != null) {
			salesReturnCRId = vbSalesReturnChangeRequest.getId();
		}
		return salesReturnCRId;
	}

	/**
	 * This method is responsible for getting Approved,Declined,Pending count of
	 * Sales Return Products.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults -{@link MySalesHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesHistoryResult> getSalesReturnTransactionHistory(
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		List<MySalesHistoryResult> historyResults = new ArrayList<MySalesHistoryResult>();
		// approved,decline,pending status count of sales return
		List<Object[]> resultList = session.createCriteria(VbSalesReturnChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.rowCount())
								.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("invoiceNo", "SR", MatchMode.ANYWHERE))
				.list();
		session.close();
		if (!resultList.isEmpty()) {
			MySalesHistoryResult historyResult = null;
			for (Object[] objects : resultList) {
				historyResult = new MySalesHistoryResult();
				String salesReturnType = (String) objects[0];
				String salesReturnStatus = (String) objects[1];
				Integer count = (Integer) objects[2];
				if (CRStatus.APPROVED.name().equals(salesReturnStatus)) {
					historyResult.setDeliveryNoteTransactionType(salesReturnType);
					historyResult.setDeliveryNoteTxnStatus(salesReturnStatus);
					historyResult.setApprovedDNCount(count);
				} else if (CRStatus.DECLINE.name().equals(salesReturnStatus)) {
					historyResult.setDeliveryNoteTransactionType(salesReturnType);
					historyResult.setDeliveryNoteTxnStatus(salesReturnStatus);
					historyResult.setDeclinedDNCount(count);
				} else if (CRStatus.PENDING.name().equals(salesReturnStatus)) {
					historyResult.setDeliveryNoteTransactionType(salesReturnType);
					historyResult.setDeliveryNoteTxnStatus(salesReturnStatus);
					historyResult.setPendingDNCount(count);
				}
				historyResults.add(historyResult);
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResults.size());
			}
			return historyResults;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to fetch invoices from
	 * {@link VbSalesReturnChangeRequest} based on status and invoiceNumber
	 * 
	 * @param invoiceNumber - {@link String}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return invoiceHistoryResults - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesInvoicesHistoryResult> getSalesReturnInvoicesTransactionHistory(
			String invoiceNumber, String status, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		List<Object[]> resultList = session.createCriteria(VbSalesReturnChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.property("createdBy"))
								.add(Projections.property("createdOn"))
								.add(Projections.property("modifiedBy"))
								.add(Projections.property("modifiedOn")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("status", status))
				.add(Restrictions.like("invoiceNo", invoiceNumber, MatchMode.ANYWHERE)).list();
		session.close();
		if (!resultList.isEmpty()) {
			Date createdOn = null;
			Date modifiedOn = null;
			String createdBy = null;
			String modifiedBy = null;
			Integer salesReturnId = null;
			String salesReturnInvNo = null;
			String salesReturnstatus = null;
			MySalesInvoicesHistoryResult invoiceHistoryResult = null;
			List<MySalesInvoicesHistoryResult> invoiceHistoryResults = new ArrayList<MySalesInvoicesHistoryResult>();
			for (Object[] objects : resultList) {
				salesReturnId = (Integer) objects[0];
				salesReturnInvNo = (String) objects[1];
				salesReturnstatus = (String) objects[2];
				createdBy = (String) objects[3];
				createdOn = (Date) objects[4];
				modifiedBy = (String) objects[5];
				modifiedOn = (Date) objects[6];

				invoiceHistoryResult = new MySalesInvoicesHistoryResult();
				invoiceHistoryResult.setInvoiceNumber(salesReturnInvNo);
				invoiceHistoryResult.setRequestedBy(createdBy);
				invoiceHistoryResult.setRequestedDate(createdOn);
				invoiceHistoryResult.setModifiedBy(modifiedBy);
				invoiceHistoryResult.setModifiedDate(modifiedOn);
				invoiceHistoryResult.setStatus(salesReturnstatus);
				invoiceHistoryResult.setId(salesReturnId);

				invoiceHistoryResults.add(invoiceHistoryResult);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", invoiceHistoryResults.size());
			}
			return invoiceHistoryResults;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for getting the {@link Integer} based on the
	 * product name.
	 * 
	 * @param username - {@link String}
	 * @param productName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return qtySold - {@link Integer}
	 */
	public Integer getQtySold(String businessName, String productName,
			String batchNumber, VbOrganization organization) {
		Session session = this.getSession();
		Integer qtySold = (Integer) session
				.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.sum("productQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.flag", new Integer(1)))
				.uniqueResult();

		if (qtySold == null) {
			qtySold = new Integer(0);
		}
		Integer previousReturnQty = (Integer) session
				.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.setProjection(Projections.sum("totalQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("salesReturn.vbOrganization", organization))
				.add(Restrictions.eq("salesReturn.businessName", businessName))
				.add(Restrictions.eq("salesReturn.flag", new Integer(1)))
				.uniqueResult();
		
		if (previousReturnQty == null) {
			previousReturnQty = new Integer(0);
		}
		session.close();
		Integer avaialableQty = qtySold - previousReturnQty;
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} is the sold Quantity for product {} ", avaialableQty, productName);
		}
		return avaialableQty;
	}
}