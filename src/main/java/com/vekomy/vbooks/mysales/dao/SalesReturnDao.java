/**
 * com.vekomy.vbooks.mysales.dao.SalesReturnDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.SalesReturnCommand;
import com.vekomy.vbooks.mysales.command.SalesReturnResult;
import com.vekomy.vbooks.mysales.command.SalesReturnViewResult;
import com.vekomy.vbooks.mysales.command.SalesReturnsResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform CRUD operations on
 * {@link VbSalesReturn}.
 * 
 * @author Sudhakar
 */
public class SalesReturnDao extends MySalesBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnDao.class);
	/**
	 * String variable holds DAILY_SALES_EXECUTIVE.
	 */
	private static final String DAILY_SALES_EXECUTIVE = "Daily";
	/**
	 * This method is responsible to persist the sales returns data.
	 * 
	 * @param salesReturnCommands - {@link List}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isSaved - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public synchronized void saveSalesReturns(
			List<SalesReturnCommand> salesReturnCommands,
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbSalesReturn salesReturn = new VbSalesReturn();
			salesReturn.setCreatedBy(userName);
			Date date = new Date();
			salesReturn.setCreatedOn(date);
			salesReturn.setModifiedOn(date);
			salesReturn.setVbOrganization(organization);
			salesReturn.setStatus(CRStatus.PENDING.name());
			salesReturn.setFlag(new Integer(1));
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			salesReturn.setVbSalesBook(salesBook);
			String businessName = null;
			String invoiceName = null;
			String invoiceNo = null;
			String remarks = null;
			String productName = null;
			String batchNumber = null;
			Boolean flag = Boolean.FALSE;
			for (SalesReturnCommand command : salesReturnCommands) {
				if(!flag) {
					businessName = command.getBusinessName();
					invoiceName = command.getInvoiceName();
					invoiceNo = command.getInvoiceNo();
					remarks = command.getRemarks();
					salesReturn.setBusinessName(businessName);
					salesReturn.setInvoiceName(invoiceName);
					salesReturn.setInvoiceNo(invoiceNo);
					salesReturn.setRemarks(remarks);
					salesReturn.setProductsGrandTotal(new Float(0.00));	
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSalesReturn");
					}
					session.save(salesReturn);
					
					flag = Boolean.TRUE;
				}
				productName = command.getProductName();
				batchNumber = command.getBatchNumber();
				VbSalesReturnProducts instanceReturnProducts = new VbSalesReturnProducts();
				instanceReturnProducts.setProductName(productName);
				instanceReturnProducts.setBatchNumber(batchNumber);
				instanceReturnProducts.setDamaged(command.getDamaged());
				instanceReturnProducts.setResalable(command.getResalable());
				instanceReturnProducts.setTotalQty(command.getTotalQty());
				instanceReturnProducts.setVbSalesReturn(salesReturn);
				instanceReturnProducts.setDamagedCost(new Float(0.00));
				instanceReturnProducts.setResalableCost(new Float(0.00));
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbSalesReturnProducts");
				}
				session.save(instanceReturnProducts);
			}
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, 
					ENotificationTypes.SALES_RETURN.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
		} catch(HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to update {@link VbProduct}, {@link VbCustomerCreditInfo} 
	 * and {@link VbCustomerAdvanceInfo} after returning the products.
	 * 
	 * @param command - {@link SalesReturnCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	private void updateProductsAndPreviousCredits(Session session,
			Float grandTotal, String businessName,
			String salesExecutive, VbOrganization organization,
			String userName, String invoiceNo) throws DataAccessException {
		try {
			// Updating VbProduct with the return Qty.
			Float grandTotalCost = grandTotal;
			// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
			if (grandTotalCost > 0) {
				Query query = null;
				VbCustomerDebitTransaction customerDebitTxn=null;
				while (grandTotalCost != 0) {
					query = session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
							+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
							+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
							.setParameter("businessName", businessName)
							.setParameter("vbOrganization", organization)
							.setParameter("due", new Float("0"));
					VbCustomerCreditInfo creditInfo = getSingleResultOrNull(query);
					
					String newInvoiceNo = generateInvoiceNo(organization);
					customerDebitTxn=new VbCustomerDebitTransaction();
					if (creditInfo != null) {
						creditInfo.setModifiedBy(userName);
						creditInfo.setModifiedOn(new Date());
						String existingInvoiceNo = creditInfo.getDebitTo();
					if (existingInvoiceNo != null) {
						creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(newInvoiceNo));
					} else {
						creditInfo.setDebitTo(newInvoiceNo);
					}
					Float existingDue = creditInfo.getDue();
					if (grandTotalCost < existingDue) {
						creditInfo.setDue(existingDue - grandTotalCost);
						customerDebitTxn.setAmount(grandTotalCost);
						grandTotalCost = new Float(0.0);
					} else {
						grandTotalCost = grandTotalCost - existingDue;
						creditInfo.setDue(new Float("0.00"));
						customerDebitTxn.setAmount(existingDue);
					}

					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo");
					}
					session.update(creditInfo);
					
					//persisting Customer debit Txn
					customerDebitTxn.setBusinessName(businessName);
					customerDebitTxn.setFlag(new Integer(1));
					customerDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
					customerDebitTxn.setDebitTo(invoiceNo);
					customerDebitTxn.setCreatedBy(userName);
					customerDebitTxn.setCreatedOn(new Date());
					customerDebitTxn.setModifiedOn(new Date());
					customerDebitTxn.setVbOrganization(organization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerDebitTransaction");
					}
					session.save(customerDebitTxn);
					} else {
						saveCustomerAdvanceInfo(session, businessName, newInvoiceNo, organization, grandTotalCost, userName,invoiceNo);
						grandTotalCost = new Float("0");
					}
				}
			}
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
		} catch (HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to generate new invoice no for the {@link VbSalesReturn}
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateInvoiceNo(VbOrganization organization){
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbSalesReturn vb WHERE vb.createdOn IN " +
						"(SELECT MAX(vbsr.createdOn) FROM VbSalesReturn vbsr "
						+ "WHERE vbsr.vbOrganization = :vbOrganization)")
				.setParameter("vbOrganization", organization);
		String latestInvoiceNo = getSingleResultOrNull(query);

		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
				.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType",	OrganizationUtils.TXN_TYPE_SALES_RETURN))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_SALES_RETURN)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_SALES_RETURN)
						.append(seperator).append(formattedDate)
						.append(seperator).append("0001");
			}

		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(seperator)
					.append(OrganizationUtils.TXN_TYPE_SALES_RETURN)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newInvoiceNo);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated sales return invoiceNo: {}", generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}
	

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from
	 * DB on criteria.
	 * 
	 * @param salesReturnCommand - {@link SalesReturnCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturns(
			SalesReturnCommand salesReturnCommand, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer grantedDays = 0;
		Criteria criteria = session.createCriteria(VbSalesReturn.class).createAlias("vbSalesBook", "vb");
		VbSalesBook salesBook = getSalesBookForSearch(session, organization, userName);
		//for display of search Txn of SE get granted_days from vb_employee table for specific SE
		VbEmployee vbEmployee=(VbEmployee)session.createCriteria(VbEmployee.class)
					.add(Expression.eq("username", userName))
					.add(Expression.eq("vbOrganization", organization)).uniqueResult();
		if(vbEmployee.getEmployeeType().equalsIgnoreCase("SLE")){
			grantedDays=vbEmployee.getGrantedDays();
		} 
		
		if (salesBook != null) {
			if (salesBook.getAllotmentType().equalsIgnoreCase(DAILY_SALES_EXECUTIVE)) {
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date(),grantedDays)));
			} else {
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds = session.createCriteria(VbSalesBook.class)
						.setProjection(Projections.property("id"))
						.add(Restrictions.between("createdOn", createdDate, DateUtils.getEndTimeStamp(new Date())))
						.list();
				criteria.add(Restrictions.in("vb.id", listIds));
			}
		} 
		if (organization != null) {
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		if (salesReturnCommand != null) {
			if (!salesReturnCommand.getCreatedBy().isEmpty()) {
				criteria.add(Restrictions.like("createdBy", salesReturnCommand.getCreatedBy(), MatchMode.START).ignoreCase());
			}
			if (!salesReturnCommand.getBusinessName().isEmpty()) {
				criteria.add(Restrictions.like("businessName", salesReturnCommand.getBusinessName(), MatchMode.START).ignoreCase());
			}
			if (!salesReturnCommand.getInvoiceName().isEmpty()) {
				criteria.add(Restrictions.like("invoiceName", salesReturnCommand.getInvoiceName(), MatchMode.START).ignoreCase());
			}
			if (salesReturnCommand.getCreatedOn() != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(salesReturnCommand.getCreatedOn()), 
						DateUtils.getEndTimeStamp(salesReturnCommand.getCreatedOn())));
			}
		}
		criteria.add(Restrictions.eq("flag", new Integer(1)));
		criteria.addOrder(Order.desc("createdOn"));
		List<VbSalesReturn> criteriaList = criteria.list();
		session.close();
		
		if(!(criteriaList.isEmpty())) {
			List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
			SalesReturnResult result = null;
			for (VbSalesReturn salesReturn : criteriaList) {
				result = new SalesReturnResult();
				Float total = getSalesReturnProducts(salesReturn.getId(), organization, salesReturn.getCreatedBy());
				result.setCreatedBy(salesReturn.getCreatedBy());
				result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
				result.setTotal(StringUtil.floatFormat(total));
				result.setId(salesReturn.getId());
				result.setBusinessName(salesReturn.getBusinessName());
				result.setStatus(salesReturn.getStatus());
				result.setInvoiceNo(salesReturn.getInvoiceNo());
				salesResultList.add(result);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", salesResultList.size());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve sum of total cost of provided
	 * salesReturnId {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @return sumTotal - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Float getSalesReturnProducts(Integer salesReturnId,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
		if (salesBook != null) {
			Float sumTotal = (Float) session.createCriteria(VbSalesReturnProducts.class)
					.createAlias("vbSalesReturn", "salesReturn")
					.setProjection(Projections.sum("totalCost"))
					.add(Expression.eq("salesReturn.id", salesReturnId))
					.add(Expression.eq("salesReturn.vbSalesBook", salesBook))
					.add(Expression.eq("salesReturn.vbOrganization", organization))
					.uniqueResult();
			session.close();

			return sumTotal;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from DB.
	 * 
	 * @param username - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturnsOnLoad(String username,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = getVbSalesBookNoFlag(session, username, vbOrganization);
		List<VbSalesReturn> salesReturnsList = null;
		if (salesBook != null) {
			salesReturnsList = session.createCriteria(VbSalesReturn.class)
					.add(Expression.eq("createdBy", username))
					.add(Expression.eq("vbSalesBook", salesBook))
					.add(Expression.eq("vbOrganization", vbOrganization))
					.addOrder(Order.asc("createdOn")).list();
		}
		session.close();
		
		if (salesReturnsList != null) {
			List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
			SalesReturnResult result = null;
			for (VbSalesReturn salesReturn : salesReturnsList) {
				result = new SalesReturnResult();
				result.setCreatedBy(salesReturn.getCreatedBy());
				result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
				Float total = getSalesReturnProducts(salesReturn.getId(), vbOrganization, username);
				result.setTotal(StringUtil.floatFormat(total));
				result.setId(salesReturn.getId());
				result.setBusinessName(salesReturn.getBusinessName());
				result.setInvoiceName(salesReturn.getInvoiceName());
				salesResultList.add(result);
			}
			
			if (_logger.isInfoEnabled()) {
				_logger.info("{} results have been found under the organization: {}", salesReturnsList.size(), vbOrganization.getName());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve sales returns based on return Id
	 * {@link VbSalesReturn} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return vbSalesReturn - {@link VbSalesReturn}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnViewResult> getSalesReturnProductsDetails(Integer salesReturnId,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		if(vbSalesReturn != null){
			String invoiceNo = vbSalesReturn.getInvoiceNo();
			String invoiceName = vbSalesReturn.getInvoiceName();
			Date createdDate = vbSalesReturn.getCreatedOn();
			String businessName = vbSalesReturn.getBusinessName();
			String fullName = getEmployeeFullName(vbSalesReturn.getCreatedBy(),organization);
			Float productGrandTotal = vbSalesReturn.getProductsGrandTotal();
			List<VbSalesReturnProducts> salesReturnProducts = new ArrayList<VbSalesReturnProducts>(vbSalesReturn.getVbSalesReturnProductses());
			List<SalesReturnViewResult> resultList = null;
			if( !salesReturnProducts.isEmpty()){
				resultList = new ArrayList<SalesReturnViewResult>();
				Float productCost = new Float(0);
				for(VbSalesReturnProducts product:salesReturnProducts){
					SalesReturnViewResult result = new SalesReturnViewResult();
					result.setStatus(product.getVbSalesReturn().getStatus().toLowerCase());
					result.setInvoiceNo(invoiceNo);
					result.setId(vbSalesReturn.getId());
					result.setInvoiceName(invoiceName);
					result.setCreatedDate(DateUtils.format(createdDate));
					result.setBusinessName(businessName);
					result.setSalesExecutive(fullName);
					result.setProductsGrandTotal(StringUtil.currencyFormat(productGrandTotal));
					result.setProduct(product.getProductName());
					result.setBatchNumber(product.getBatchNumber());
					result.setDamaged(product.getDamaged());
					result.setResalable(product.getResalable());
					result.setTotalQty(product.getTotalQty());
					result.setResaleCost(StringUtil.currencyFormat(product.getResalableCost()));
					result.setDamageCost(StringUtil.currencyFormat(product.getDamagedCost()));
					result.setProductTotalCost(StringUtil.currencyFormat(product.getTotalCost()));
					result.setRemarks(product.getVbSalesReturn().getRemarks());
					productCost = (Float) session.createCriteria(VbProductCustomerCost.class)
							.createAlias("vbProduct", "product")
							.createAlias("vbCustomer", "customer")
							.setProjection(Projections.property("cost"))
							.add(Expression.eq("product.productName", product.getProductName()))
							.add(Expression.eq("product.batchNumber", product.getBatchNumber()))
							.add(Expression.eq("product.vbOrganization", organization))
							.add(Expression.eq("customer.businessName", businessName))
							.add(Expression.eq("customer.vbOrganization", organization))
							.uniqueResult();
					if (productCost == null || productCost == 0) {
						productCost = (Float) session.createCriteria(VbProduct.class)
								.setProjection(Projections.property("costPerQuantity"))
								.add(Expression.eq("productName", product.getProductName()))
								.add(Expression.eq("batchNumber", product.getBatchNumber()))
								.add(Expression.eq("vbOrganization", organization))
								.uniqueResult();
					} 
					result.setProductCost(StringUtil.currencyFormat(productCost));	
					
					resultList.add(result);
				}
				Collections.sort(resultList);
			}
			session.close();
			
			if (_logger.isInfoEnabled()) {
				_logger.info("{} products found for business Name: {}", resultList.size(), businessName);
			}
			return resultList;
		} else{
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is used to get the invoiceName based on the businessName from
	 * VbCustomer table.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return invoiceName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getInvoiceName(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String invoiceName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("invoiceName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (invoiceName != null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("{} is the associated invoice name with businessName {}", invoiceName, businessName);
			}
			return invoiceName;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}


	/**
	 * This method is responsible to provide the data for sales returns grid.
	 * 
	 * @param businessName - {@link String}
	 * @return salesReturnsResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnsResult> getGridData(String businessName,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		if(salesBook != null) {
			List<VbDeliveryNoteProducts> products = session.createQuery(
					"FROM VbDeliveryNoteProducts vb WHERE vb.vbDeliveryNote.vbOrganization = :organization "
					+ "AND vb.vbDeliveryNote.businessName = :businessName GROUP BY vb.productName, vb.batchNumber")
			.setParameter("organization", organization)
			.setParameter("businessName", businessName)
			.list();
			List<SalesReturnsResult> salesReturnsResultList = null;
			if (products != null) {
				SalesReturnsResult result = null;
				String productName = null;
				salesReturnsResultList = new ArrayList<SalesReturnsResult>();
				for (VbDeliveryNoteProducts product : products) {
					result = new SalesReturnsResult();
					productName = product.getProductName();
					result.setProductName(productName);
					result.setBatchNumber(product.getBatchNumber());
					
					salesReturnsResultList.add(result);
				}
			}
			session.close();

			if (_logger.isInfoEnabled()) {
				_logger.info("{} produts found for business name: {}", salesReturnsResultList.size(), businessName);
			}
			return salesReturnsResultList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}


	/**
	 * This method is responsible to persist {@link VbCustomerAdvanceInfo}, if
	 * there is any balance available after crediting all the
	 * {@link VbCustomerCreditInfo} based on businessName and
	 * {@link VbOrganization} or persisting advance directly.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param balance - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance,
			String userName, String approvalInvoiceNo)
			throws DataAccessException {
		try {
			VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
			VbCustomerCreditTransaction customerCreditTxn=new VbCustomerCreditTransaction();
			if (balance < 0) {
				advanceInfo.setAdvance(-(balance));
				advanceInfo.setBalance(-(balance));
				customerCreditTxn.setAmount(-(balance));
			} else {
				advanceInfo.setAdvance(balance);
				advanceInfo.setBalance(balance);
				customerCreditTxn.setAmount(balance);
			}
			String advanceInfoInvoiceNumber=generateCustomerAdvanceInfoInvoiceNumber(organization);
			advanceInfo.setBusinessName(businessName);
			advanceInfo.setCreatedBy(userName);
			Date date = new Date();
			advanceInfo.setCreatedOn(date);
			advanceInfo.setModifiedOn(date);
			advanceInfo.setCreditFrom(approvalInvoiceNo);
			advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
			advanceInfo.setVbOrganization(organization);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
			}
			session.save(advanceInfo);
			
			//persisting Customer Credits Txn for New Advance info for extra amount.
			customerCreditTxn.setCreditFrom(approvalInvoiceNo);
			customerCreditTxn.setBusinessName(businessName);
			customerCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
			customerCreditTxn.setFlag(new Integer(1));
			customerCreditTxn.setCreatedBy(userName);
			customerCreditTxn.setCreatedOn(date);
			customerCreditTxn.setModifiedOn(date);
			customerCreditTxn.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerCreditTransaction: {}", customerCreditTxn);
			}
			session.save(customerCreditTxn);
		} catch (HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		}
		
	}

	/**This method is responsible for getting sales return details from Database.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings({"unchecked" })
	public List<SalesReturnResult> getSalesReturnsDashboard(
			VbOrganization vbOrganization, String username)throws DataAccessException {
		Session session = this.getSession();
		List<VbSalesReturn> salesReturnsList = (List<VbSalesReturn>) session
				.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("vbOrganization", vbOrganization))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.addOrder(Order.asc("createdOn")).list();
		session.close();
		
		if (!(salesReturnsList.isEmpty())) {
			List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
			SalesReturnResult result = null;
			for (VbSalesReturn salesReturn : salesReturnsList) {
				result = new SalesReturnResult();
				result.setCreatedBy(salesReturn.getCreatedBy());
				result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
				result.setId(salesReturn.getId());
				result.setBusinessName(salesReturn.getBusinessName());
				result.setInvoiceName(salesReturn.getInvoiceName());
				result.setInvoiceNo(salesReturn.getInvoiceNo());
				salesResultList.add(result);
			}
			
			if (_logger.isInfoEnabled()) {
				_logger.info("{} results have been found under the organization: {}", salesReturnsList.size(), vbOrganization.getName());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible for fetching all sales return product list from db.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param cost - {@link String}
	 * @param salesReturnList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void approveOrDeclineSalesReturn(Integer salesReturnId,
			String status, VbOrganization organization, String userName,
			String cost, List<SalesReturnCommand> salesReturnList)
			throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		if(vbSalesReturn!=null){
			Transaction txn = null;
			try {
				txn = session.beginTransaction();
				String salesExecutive = vbSalesReturn.getCreatedBy();
				String businessName = vbSalesReturn.getBusinessName();
				// Updating VbSalesReturn and VbSalesReturnProducts.
				Float grandTotal = updateSalesReturn(session, userName, vbSalesReturn, salesReturnList,status, organization);
				//call updateProductsAndPreviousCredits to update return products and customer credit info.
				updateProductsAndPreviousCredits(session, grandTotal, businessName, 
						salesExecutive, organization, userName, vbSalesReturn.getInvoiceNo());
				if(_logger.isDebugEnabled()){
					_logger.debug("Updating vbSalesReturn for approved.");
				}
				session.update(vbSalesReturn);
				
				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, salesExecutive, organization, 
								ENotificationTypes.SALES_RETURN.name(), status, vbSalesReturn.getInvoiceNo(), businessName);
				txn.commit();
			} catch(HibernateException exception) {
				if (txn != null) {
					txn.rollback();
				}
				throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
			} finally {
				if (session != null) {
					session.close();
				}
			}
	    } else {
	    	if (session != null) {
				session.close();
			}
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to update {@link VbSalesReturn}.
	 * 
	 * @param session - {@link Session}
	 * @param userName - {@link String}
	 * @param vbSalesReturn - {@link VbSalesReturn}
	 * @param salesReturnCommandList - {@link List}
	 * @param status - {@link String}
	 */
	private Float updateSalesReturn(Session session, String userName,
			VbSalesReturn vbSalesReturn,
			List<SalesReturnCommand> salesReturnCommandList, String status, VbOrganization organization) {
		Float grandTotal = new Float(0);
		VbSalesReturnProducts vbSalesReturnProducts = null;
		if (CRStatus.APPROVED.name().equalsIgnoreCase(status) && salesReturnCommandList != null) {
			VbSalesBookProducts vbSalesBookProducts = null;
			String productName = null;
			String batchNo = null;
			Integer resalableProductQty = new Integer(0);
			Float totalCost = new Float(0.00);
			VbSalesBook salesBook = getVbSalesBookNoFlag(session, vbSalesReturn.getCreatedBy(), organization);
			for (SalesReturnCommand salesReturnCommand : salesReturnCommandList) {
				grandTotal = salesReturnCommand.getGrandTotalCost();
				totalCost = salesReturnCommand.getTotalCost();
				productName = salesReturnCommand.getProductName();
				batchNo = salesReturnCommand.getBatchNumber();
				resalableProductQty = salesReturnCommand.getResalable();
				if(!new Float(0.00).equals(totalCost)) {
					vbSalesReturnProducts = (VbSalesReturnProducts) session.createCriteria(VbSalesReturnProducts.class)
							.add(Restrictions.eq("vbSalesReturn", vbSalesReturn))
							.add(Restrictions.eq("productName", productName))
							.add(Restrictions.eq("batchNumber", batchNo))
							.uniqueResult();
					if (vbSalesReturnProducts != null) {
						vbSalesReturnProducts.setResalableCost(salesReturnCommand.getResalableCost());
						vbSalesReturnProducts.setDamagedCost(salesReturnCommand.getDamagedCost());
						vbSalesReturnProducts.setTotalCost(salesReturnCommand.getTotalCost());
						session.update(vbSalesReturnProducts);
					}
					
					// Updating VbSalesBookProducts with the return Qty.
					vbSalesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
							.add(Restrictions.eq("vbSalesBook", salesBook))
							.add(Expression.eq("productName", productName))
							.add(Expression.eq("batchNumber", batchNo))
							.uniqueResult();
					if (vbSalesBookProducts != null && resalableProductQty != null) {
						vbSalesBookProducts.setQtyClosingBalance(vbSalesBookProducts.getQtyClosingBalance() + resalableProductQty);
						vbSalesBookProducts.setQtyOpeningBalance(vbSalesBookProducts.getQtyOpeningBalance() + resalableProductQty);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating vbSalesBookProducts.");
						}
						session.update(vbSalesBookProducts);
					}
				}
			}
			vbSalesReturn.setProductsGrandTotal(grandTotal);
			vbSalesReturn.setStatus(status);
		} else if (CRStatus.DECLINE.name().equalsIgnoreCase(status)) {
			vbSalesReturn.setStatus(status);
		}
		
		vbSalesReturn.setModifiedBy(userName);
		vbSalesReturn.setModifiedOn(new Date());
		if(_logger.isDebugEnabled()) {
			_logger.debug("Updating VbSalesReturn.");
		}
		session.update(vbSalesReturn);
		
		return grandTotal;
	}

	/**
	 * This method is responsible for getting the {@link Integer} based on the product name.
	 * 
	 * @param businessName - {@link String}
	 * @param productName - {@link String}
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return avaialableQty - {@link Integer}
	 */
	public Integer getQtySold(String businessName, String productName,
			String batchNumber, VbOrganization organization) {
		Session session = this.getSession();
		Integer qtySold = (Integer) session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.sum("productQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.flag", new Integer(1)))
				.uniqueResult();
		if(qtySold == null) {
			qtySold = new Integer(0);
		}
		
		Integer previousReturnQty = (Integer) session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.setProjection(Projections.sum("totalQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("salesReturn.vbOrganization", organization))
				.add(Restrictions.eq("salesReturn.businessName", businessName))
				.add(Restrictions.ne("salesReturn.status", CRStatus.DECLINE.name()))
				.add(Restrictions.eq("salesReturn.flag", new Integer(1)))
				.uniqueResult();
		if(previousReturnQty == null) {
			previousReturnQty = new Integer(0);
		}
		session.close();
		
		Integer avaialableQty = qtySold - previousReturnQty;
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} is the sold Quantity for product {} ", avaialableQty, productName);
		}
		return avaialableQty;
	}
	
	/**
	 * This method is responsible to get {@link VbSalesBook} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getSalesBookForSearch(Session session, VbOrganization organization, String userName) {
		Query query = session.createQuery(
				"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName AND " +
				"vb.vbOrganization = :vbOrganization AND (vb.createdOn,vb.cycleId) IN " +
				"(SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE " +
				"vbs.salesExecutive = :salesExecutiveName AND vbs.vbOrganization = :vbOrganization )")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}

	/**
	 * This method is responsible to get created user of {@link VbEmployee}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(String userName, VbOrganization organization) {
		Session session = this.getSession();
		String createdBy = (String) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("createdBy"))
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		return createdBy;
	}

	/**
	 * This method is responsible to get created user of {@link VbSalesReturn}.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(Integer salesReturnId) {
		String createdBy = null;
		Session session = this.getSession();
		VbSalesReturn salesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		session.close();
		
		if(salesReturn != null) {
			createdBy = salesReturn.getCreatedBy();
		}
		
		return createdBy;
	}
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerAdvanceInfoInvoiceNumber(
			VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery("SELECT vb.invoiceNo FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN "
							+ "(SELECT MAX(vbdn.createdOn) FROM VbCustomerAdvanceInfo vbdn "
							+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%AD%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_ADVANCE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_ADVANCE)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_ADVANCE)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR).append("0001");
			}

		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(OrganizationUtils.SEPERATOR)
					.append(OrganizationUtils.TXN_TYPE_ADVANCE)
					.append(OrganizationUtils.SEPERATOR).append(formattedDate)
					.append(OrganizationUtils.SEPERATOR).append(newInvoiceNo);
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated Customer Advance Info invoiceNo is {}", generatedInvoiceNo);
		}
		session.close();
		return generatedInvoiceNo.toString();
	}
	
	/**
	 * This method is responsible for get previous sales return based on current
	 * invoiceNumber,flag and previousSalesReturn.
	 * 
	 * @param journalId - {@link Integer}
	 * @param invoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return journalMaxId - {@link Integer}
	 */
	public Integer getPreviousSalesReturnInvoiceNumber(Integer salesReturnId,
			String invoiceNumber, VbOrganization organization, String username) {
		Session session = this.getSession();
		Integer salesReturnIds=new Integer(0);
		Query query = session.createQuery(
				"SELECT MAX(vbs.id) FROM VbSalesReturn vbs where vbs.invoiceNo LIKE :invoiceNo " +
				"AND vbs.flag= :flag AND vbs.id < :salesReturnId AND vbs.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(0))
				.setParameter("salesReturnId", salesReturnId)
				.setParameter("vbOrganization", organization);
		Integer salesReturnMaxId = getSingleResultOrNull(query);
		session.close();
		
		if(salesReturnMaxId != null){
			return salesReturnMaxId;
		}
		return salesReturnIds;
	}

	/**
	 * This method is responsible for getting Approved,Declined,Pending count of Original Sales Return.
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
		List<MySalesHistoryResult> historyResultList = new ArrayList<MySalesHistoryResult>();
		// Approved, Decline, Pending status count of journal
		List<Object[]> resultList = session.createCriteria(VbSalesReturn.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("invoiceNo"))
						.add(Projections.property("status"))
						.add(Projections.rowCount())
						.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("invoiceNo", "SR", MatchMode.ANYWHERE))
				.list();
		session.close();
		
		if(!resultList.isEmpty()) {
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
					historyResultList.add(historyResult);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResultList.size());
			}
			return historyResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to fetch invoices from {@link VbSalesReturn}
	 * based on status and invoiceNumber.
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
		List<Object[]> resultList = session.createCriteria(VbSalesReturn.class)
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
				.add(Restrictions.like("invoiceNo", invoiceNumber, MatchMode.ANYWHERE))
				.list();
		session.close();
		
		if(!resultList.isEmpty()) {
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
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	/**
	 * This method is responsible for getting the {@link Integer} based on the product name wile approving.
	 * @param resultSuccess2 
	 * 
	 * @param businessName - {@link String}
	 * @param productName - {@link String}
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return avaialableQty - {@link Integer}
	 */
	public Integer getQtySoldForApprovals( String businessName,
			String productName, String batchNumber, VbOrganization organization, Integer salesReturnId) {
		Session session = this.getSession();
		Integer qtySold = (Integer) session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.sum("productQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.flag", new Integer(1)))
				.uniqueResult();
		if(qtySold == null) {
			qtySold = new Integer(0);
		}
		Integer previousReturnQty = (Integer) session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.setProjection(Projections.sum("totalQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("salesReturn.vbOrganization", organization))
				.add(Restrictions.eq("salesReturn.businessName", businessName))
				.add(Restrictions.ne("salesReturn.status", CRStatus.DECLINE.name()))
				.add(Restrictions.ne("salesReturn.id",salesReturnId))
				.add(Restrictions.eq("salesReturn.flag", new Integer(1)))
				.uniqueResult();
		if(previousReturnQty == null) {
			previousReturnQty = new Integer(0);
		}
		Integer avaialableQty = qtySold - previousReturnQty;
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} is the sold Quantity for product {} ", avaialableQty, productName);
		}
		if(avaialableQty == 0){
			VbDeliveryNoteProducts vbDeliveryNoteProducts = (VbDeliveryNoteProducts) session.createCriteria(VbDeliveryNoteProducts.class)
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.uniqueResult();
			if(vbDeliveryNoteProducts == null){
				avaialableQty = -1;
			}
		}
		session.close();
		/*if(avaialableQty < 0){
			avaialableQty = new Integer(0);
		}*/
		return avaialableQty;
	
	}

	public Integer getQtySoldForCrApprovals(String businessName,
			String productName, String batchNumber,
			VbOrganization organization, Integer salesReturnId, String invoiceNo, Integer totalQuantity) {
		Session session = this.getSession();
		Integer availableQuantity =new Integer(0);
		Integer qtySold  = new Integer(0);
		 qtySold = (Integer) session.createCriteria(VbDeliveryNoteProducts.class)
					.createAlias("vbDeliveryNote", "deliveryNote")
					.setProjection(Projections.sum("productQty"))
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
					.add(Restrictions.eq("deliveryNote.businessName", businessName))
					.add(Restrictions.eq("deliveryNote.flag", new Integer(1)))
					.uniqueResult();
				availableQuantity = qtySold;
			
			if(qtySold == 0 && availableQuantity == 0){
				availableQuantity = -1;
			}
		return availableQuantity;
		
			
			}
}