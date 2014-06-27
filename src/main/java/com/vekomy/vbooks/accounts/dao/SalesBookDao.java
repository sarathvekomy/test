/**
 * com.vekomy.vbooks.accounts.dao.SalesBookDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 24, 2013
 */
package com.vekomy.vbooks.accounts.dao;

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

import com.vekomy.vbooks.accounts.command.AllotStockCommand;
import com.vekomy.vbooks.accounts.command.AllotStockResult;
import com.vekomy.vbooks.accounts.command.SalesBookResult;
import com.vekomy.vbooks.accounts.command.SalesCommand;
import com.vekomy.vbooks.accounts.command.SalesResult;
import com.vekomy.vbooks.accounts.command.SalesReturnCommand;
import com.vekomy.vbooks.accounts.command.SalesReturnResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookAllotments;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * @author vinay
 * 
 * 
 */
public class SalesBookDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesBookDao.class);
	
	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesReturnsList - {@link List}
	 * @throws DataAccessException  - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings({ "unchecked"})
	public List<SalesReturnResult> getSalesReturns(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbSalesReturn> salesReturnsList = session.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn"))
				.list();
		session.close();
		
		if(!salesReturnsList.isEmpty()) {
			List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
			SalesReturnResult result = null;
			Float total;
			for (VbSalesReturn vbSalesReturn : salesReturnsList) {
				result = new SalesReturnResult();
				total = getSalesReturnProducts(vbSalesReturn);
				result.setCreatedBy(vbSalesReturn.getCreatedBy());
				result.setDate(DateUtils.format(vbSalesReturn.getCreatedOn()));
				result.setTotalCost(StringUtil.floatFormat(total));
				result.setId(vbSalesReturn.getId());
				result.setBusinessName(vbSalesReturn.getBusinessName());
				salesResultList.add(result);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", salesResultList.size());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve sum of total cost of based on
	 * salesReturnId {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturn - {@link VbSalesReturn}
	 * @return sumTotal - {@link Float}
	 * 
	 */
	private Float getSalesReturnProducts(VbSalesReturn salesReturn) {
		Session session = this.getSession();
		Float sumTotal = (Float) session.createCriteria(VbSalesReturnProducts.class)
				.setProjection(Projections.sum("totalCost"))
				.add(Expression.eq("vbSalesReturn", salesReturn))
				.uniqueResult();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}

	/**
	 * This method is responsible to retrieve salesReturnProducts details along
	 * with salesReturn {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesReturn - {@link VbSalesReturn}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbSalesReturn getSalesReturnProductsCost(Integer salesReturnId,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesReturn salesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		session.close();
		
		if (salesReturn != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("VbSalesReturn: {}", salesReturn);
			}
			return salesReturn;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from
	 * DB on criteria.
	 * 
	 * @param salesReturnCommand -{@link SalesReturnCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesReturnsList - {@link List}
	 * @throws DataAccessException  - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturnsCriteria(
			SalesReturnCommand salesReturnCommand, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbSalesReturn.class);
		if (salesReturnCommand != null) {
			String businessName = salesReturnCommand.getBusinessName();
			String invoiceName = salesReturnCommand.getInvoiceName();
			Date createdOn = salesReturnCommand.getCreatedOn();
			String createdBy = salesReturnCommand.getCreatedBy();
			if (!createdBy.isEmpty()) {
				criteria.add(Expression.like("createdBy", createdBy, MatchMode.START).ignoreCase());
			}
			if (!businessName.isEmpty()) {
				criteria.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase());
			}
			if (!invoiceName.isEmpty()) {
				criteria.add(Expression.like("invoiceName", invoiceName, MatchMode.START).ignoreCase());
			}
			if (createdOn != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(createdOn), DateUtils.getEndTimeStamp(createdOn)));
			}
		}
		if (organization != null) {
			criteria.add(Expression.eq("vbOrganization", organization));
		}
		criteria.addOrder(Order.desc("createdOn"));
		List<VbSalesReturn> criteriaList = criteria.list();
		session.close();
		if(!criteriaList.isEmpty()) {
			List<SalesReturnResult> salesReturnList = new ArrayList<SalesReturnResult>();
			SalesReturnResult salesResult = null;
			Float total;
			for (VbSalesReturn vbSalesReturn : criteriaList) {
				salesResult = new SalesReturnResult();
				total = getSalesReturnProducts(vbSalesReturn);
				salesResult.setCreatedBy(vbSalesReturn.getCreatedBy());
				salesResult.setDate(DateUtils.format(vbSalesReturn.getCreatedOn()));
				salesResult.setId(vbSalesReturn.getId());
				salesResult.setTotalCost(StringUtil.floatFormat(total));
				salesResult.setBusinessName(vbSalesReturn.getBusinessName());
				salesReturnList.add(salesResult);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} salesReturn records found", salesReturnList.size());
			}
			return salesReturnList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSales} from DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SalesResult> getSales(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<SalesResult> salesResultList = new ArrayList<SalesResult>();
		List<VbSalesBook> salesList = session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.desc("createdOn")).list();
		session.close();
		
		if(!salesList.isEmpty()) {
			for (VbSalesBook vbSalesBook : salesList) {
				SalesResult result = new SalesResult();
				result.setSalesExecutive(vbSalesBook.getSalesExecutive());
				result.setDate(DateUtils.format(vbSalesBook.getCreatedOn()));
				result.setBalanceOpening(StringUtil.floatFormat(vbSalesBook.getOpeningBalance()));
				result.setBalanceClosing(StringUtil.floatFormat(vbSalesBook.getClosingBalance()));
				result.setId(vbSalesBook.getId());
				salesResultList.add(result);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} salesReturn records found", salesResultList.size());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesBook} from
	 * DB on criteria.
	 * 
	 * @param salesCommand - {{@link SalesCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SalesResult> getSalesOnCriteria(SalesCommand salesCommand,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbSalesBook.class);
		if (salesCommand != null) {
			String salesExecutive = salesCommand.getSalesExecutive();
			Date createdOn = salesCommand.getCreatedOn();
			if (!salesExecutive.isEmpty()) {
				criteria.add(Expression.like("salesExecutive", salesExecutive, MatchMode.START).ignoreCase());
			}
			if (createdOn != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(createdOn), DateUtils.getEndTimeStamp(createdOn)));
			}
		}
		criteria.addOrder(Order.desc("createdOn"));
		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		List<VbSalesBook> criteriaList = criteria.list();
		session.close();
		if(!criteriaList.isEmpty()) {
			List<SalesResult> salesResultList = new ArrayList<SalesResult>();
			SalesResult result = null;
			for (VbSalesBook vbSalesBook : criteriaList) {
				result = new SalesResult();
				result.setSalesExecutive(vbSalesBook.getSalesExecutive());
				result.setDate(DateUtils.format(vbSalesBook.getCreatedOn()));
				result.setBalanceOpening(StringUtil.floatFormat(vbSalesBook.getOpeningBalance()));
				result.setBalanceClosing(StringUtil.floatFormat(vbSalesBook.getClosingBalance()));
				result.setId(vbSalesBook.getId());
				salesResultList.add(result);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} sales records found.", salesResultList.size());
			}
			return salesResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to retrieve the Allotments based on the sales id.
	 * @param salesId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return list - {@link SalesBookResult}
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SalesBookResult> getAllotments(Integer salesId,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		List<SalesBookResult> allotmentsList = new ArrayList<SalesBookResult>();
		try {
			Integer previousClosingStockDefault = 0;
			Float previousClosingBalance = (float) 0.0;
			List<VbSalesReturnProducts> returnProducts = new ArrayList<VbSalesReturnProducts>();
			List<VbSalesBookProducts> previousProducts = new ArrayList<VbSalesBookProducts>();
			VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
					.add(Restrictions.eq("id", salesId))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			List<Integer> salesReturnId = session.createCriteria(VbSalesReturn.class)
					.setProjection(Projections.property("id"))
					.add(Restrictions.eq("vbSalesBook.id", salesId))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.list();
			if (!salesReturnId.isEmpty()) {
				returnProducts = session.createCriteria(VbSalesReturnProducts.class)
						.add(Restrictions.in("vbSalesReturn.id", salesReturnId))
						.list();
			}
			if (vbSalesBook != null) {
				String salesExecutive = vbSalesBook.getSalesExecutive();
				Float openingBalance = vbSalesBook.getOpeningBalance();
				Float closingBalance = vbSalesBook.getClosingBalance();
				Float advance = vbSalesBook.getAdvance();
				Date createdDate = vbSalesBook.getCreatedOn();
				VbSalesBook salesBookPrevious = (VbSalesBook) session.createQuery(
						"From VbSalesBook sb where sb.salesExecutive=:executive AND sb.vbOrganization=:organization AND "
						+ "sb.id =(select MAX(salesBook.id) from VbSalesBook salesBook where salesBook.salesExecutive=:executive AND "
						+ "salesBook.vbOrganization=:organization AND salesBook.id < :salesId)")
						.setParameter("executive", salesExecutive)
						.setParameter("organization", vbOrganization)
						.setParameter("salesId", salesId).uniqueResult();
				if (salesBookPrevious != null) {
					previousClosingBalance = salesBookPrevious.getClosingBalance();
					previousProducts = new ArrayList<VbSalesBookProducts>(salesBookPrevious.getVbSalesBookProductses());
				}
				List<VbSalesBookProducts> productsList = new ArrayList<VbSalesBookProducts>(vbSalesBook.getVbSalesBookProductses());
				for (VbSalesBookProducts product : productsList) {
					Integer returnQty = 0;
					SalesBookResult result = new SalesBookResult();
					String productName = product.getProductName();
					String batchNumber = product.getBatchNumber();
					Integer qtyAllotted = product.getQtyAllotted();
					result.setProduct(product.getProductName());
					result.setBatchNumber(product.getBatchNumber());
					result.setQtyClosingBalance(product.getQtyClosingBalance());
					result.setQtyAllotted(qtyAllotted);
					result.setQtyOpeningBalance(product.getQtyOpeningBalance());
					result.setQtySold(product.getQtySold());
					result.setQtyToFactory(product.getQtyToFactory());
					result.setOpeningBalance(openingBalance);
					result.setClosingBalance(closingBalance);
					result.setAdvance(advance);
					result.setSalesExecutive(salesExecutive);
					result.setCreatedDate(createdDate);
					result.setPreviousClosingBalance(previousClosingBalance);
					if (!previousProducts.isEmpty()) {
						for (int i = 0; i < previousProducts.size(); i++) {
							Integer previousClosingStock = 0;
							VbSalesBookProducts previousProduct = previousProducts.get(i);
							if (previousProduct.getProductName().equals(productName)
									&& previousProduct.getBatchNumber().equals((batchNumber))) {
								previousClosingStock = previousProduct.getQtyClosingBalance();
								Integer qtyOpeningBalance = previousClosingStock + qtyAllotted;
								result.setPreviousClosingStock(previousClosingStock);
								result.setQtyOpeningBalance(qtyOpeningBalance);
								break;
							} else {
								result.setPreviousClosingStock(previousClosingStock);
								Integer qtyOpeningBalance = previousClosingStock + qtyAllotted;
								result.setQtyOpeningBalance(qtyOpeningBalance);
							}
						}
					} else {
						result.setPreviousClosingStock(previousClosingStockDefault);
					}
					if (!returnProducts.isEmpty()) {
						for (int j = 0; j < returnProducts.size(); j++) {
							VbSalesReturnProducts productsReturn = returnProducts.get(j);
							if (productsReturn.getProductName().equals(productName)
									&& productsReturn.getBatchNumber().equals(batchNumber)) {
								returnQty = productsReturn.getTotalQty();
								result.setReturnQty(returnQty);
								break;
							} else {
								result.setReturnQty(returnQty);
							}
						}
					} else {
						result.setReturnQty(returnQty);
					}
					allotmentsList.add(result);
				}
				Collections.sort(allotmentsList);
				session.close();
			}

		} catch (HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return allotmentsList;
	}


	/**
	 * This method is responsible to retrieve all the {@link VbSalesBook} from DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesExecutiveDetailsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> getSalesExecutives(
			VbOrganization vbOrganization, String salesExecutiveName)
			throws DataAccessException {
		Session session = this.getSession();
		List<String> salesExecutiveList = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.add(Expression.like("username", salesExecutiveName, MatchMode.START))
				.list();
		
		AllotStockResult allotStockResult = null;
		VbSalesBook vbSalesBook = null;
		if (!salesExecutiveList.isEmpty()) {
			List<AllotStockResult> salesExecutiveDetailsList = new ArrayList<AllotStockResult>();
			for (String salesExecutive : salesExecutiveList) {
				VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
						.add(Restrictions.eq("vbOrganization", vbOrganization))
						.add(Restrictions.eq("enabled", "1"))
						.add(Restrictions.eq("username", salesExecutive))
						.uniqueResult();
				if(login != null) {
					allotStockResult = new AllotStockResult();
					allotStockResult.setSalesExecutive(salesExecutive);
					vbSalesBook = (VbSalesBook) session.createQuery(
							"FROM VbSalesBook vb WHERE vb.createdOn IN "
							+ "(SELECT MAX(vbs.createdOn) FROM  VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive "
							+ "AND vbs.vbOrganization = :vbOrganization AND vbs.flag = :flag)")
							.setParameter("salesExecutive", salesExecutive)
							.setParameter("vbOrganization", vbOrganization)
							.setParameter("flag", new Integer(1)).uniqueResult();
					if (vbSalesBook == null) {
						allotStockResult.setAdvance(new Float(0));
						allotStockResult.setOpeningBalance(new Float(0));
					} else {
						allotStockResult.setAdvance(vbSalesBook.getAdvance());
						allotStockResult.setOpeningBalance(vbSalesBook.getOpeningBalance());
					}
					salesExecutiveDetailsList.add(allotStockResult);
				}
			}
			session.close();
			
			if(!salesExecutiveDetailsList.isEmpty()) {
				if (_logger.isDebugEnabled()) {
					_logger.debug("{} salesExecutives found.", salesExecutiveDetailsList.size());
				}
				
				return salesExecutiveDetailsList;
			} else {
				throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
			}
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible for fetching the closingBalance based on the salesExecutive from DB.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return closingBalance - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Float getClosingBalance(String salesExecutive,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Query query = session.createQuery(
				"SELECT sb.closingBalance FROM VbSalesBook as sb WHERE sb.salesExecutive = :salesExecutive and sb.vbOrganization = :vbOrganization "
				+ "AND sb.createdOn IN (SELECT MAX(vb.createdOn) FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName)")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", salesExecutive);
		Float closingBalance = getSingleResultOrNull(query);
		session.close();
		
		if (closingBalance != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} is the previous Closing Balance for the sales executive: {}.", closingBalance, salesExecutive);
			}
			return closingBalance;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesBookProduct} from DB.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return productDetailsList - {@link List<AllotStockResult>}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> getProductDetails(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbProduct> productsList = session.createCriteria(VbProduct.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
				.list();
		if(!productsList.isEmpty()) {
			AllotStockResult allotStockResult = null;
			List<AllotStockResult> productDetailsList = new ArrayList<AllotStockResult>();
			for (VbProduct product : productsList) {
				allotStockResult = new AllotStockResult();
				allotStockResult.setProductName(product.getProductName());
				if(product.getAvailableQuantity() == null)	{
					allotStockResult.setAvailableQuantity(new Integer(0));
				}else {
					allotStockResult.setAvailableQuantity(product.getAvailableQuantity());
				}
				Integer initialValue = new Integer(0);
				allotStockResult.setReturnQty(initialValue);
				allotStockResult.setPreviousQtyClosingStock(initialValue);
				allotStockResult.setBatchNumber(product.getBatchNumber());
				allotStockResult.setQtyOpeningBalance(new Integer(0));
				allotStockResult.setQtyAllotted(new Integer(0));
				allotStockResult.setClosingStock(new Integer(0));
				productDetailsList.add(allotStockResult);
			}
			session.close();
			
			if (_logger.isInfoEnabled()) {
				_logger.info("{} products found.", productDetailsList.size());
			}
			return productDetailsList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.PRODUCTS_NOT_CONFIGURED_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible for saving {@link VbSalesBookProduct} in DB and saveOrUpdate the {@link VbSalesBook} in DB.
	 * 
	 * @param allotStockCommand -{@link AllotStockCommand}
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSaved - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	public synchronized void saveOrUpdateAllotStock(
			List<AllotStockCommand> allotStockList, String username,
			VbOrganization organization, String salesBookNo) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			VbSalesBook vbSalesBook = null;
			VbSalesBookAllotments vbSalesBookAllotments = null;
			VbProduct vbProduct = null;
			VbSalesBookProducts vbSalesBookProduct = null;
			Float advance = new Float(0);
			String salesExecutive = null;
			Float closingBalance = new Float(0);
			Float openingBalance = new Float(0);
			String allotmentType = null;
			txn = session.beginTransaction();
			Integer count = new Integer(0);
			Date date = new Date();
			Integer cycleId;
			String productName = null;
			String batchNumber = null;
			Integer qtyAllotted = null;
			Integer qtyClosingBalance = null;
			String remarks = null;
			Query query = null;
			Integer existingProdTxnQty = null;
			Integer latestAllotment = null;
			VbProductInventoryTransaction productInventoryTransaction = null;
			Integer existingAllottedQty = new Integer(0);
			for (AllotStockCommand allotStockCommand : allotStockList) {
				salesExecutive = allotStockCommand.getSalesExecutive();
				advance = allotStockCommand.getAdvance();
				closingBalance = allotStockCommand.getClosingBalance();
				openingBalance = allotStockCommand.getOpeningBalance();
				allotmentType = allotStockCommand.getAllotmentType();
				vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
				if(vbSalesBook != null && count == 0) {
					vbSalesBook.setClosingBalance(closingBalance);
					vbSalesBook.setAllotmentType(allotmentType);
					cycleId = vbSalesBook.getCycleId();
					vbSalesBook.setCycleId(cycleId);
					vbSalesBook.setAdvance(advance);
					vbSalesBook.setOpeningBalance(openingBalance);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating SalesBook.");
					}
					session.update(vbSalesBook);
					++count;
					// Persisting System Notifications.
					saveSystemNotificationForAndroid(session, username, salesExecutive, organization, 
							ENotificationTypes.ALLOTMENT_UPDATE.name(), "N/A", salesBookNo, "N/A");
				}else if(vbSalesBook == null) {
					if(count == 0) {
						vbSalesBook = new VbSalesBook();
						cycleId = getCycleID(session, organization, salesExecutive);
						if(cycleId == null) {
							cycleId = new Integer(1);
						}
						vbSalesBook.setCycleId(++cycleId);
						vbSalesBook.setAdvance(advance);
						vbSalesBook.setClosingBalance(closingBalance);
						vbSalesBook.setCreatedBy(username);
						vbSalesBook.setOpeningBalance(openingBalance);
						vbSalesBook.setSalesExecutive(salesExecutive);
						vbSalesBook.setVbOrganization(organization);
						vbSalesBook.setAllotmentType(allotmentType);
						vbSalesBook.setCreatedOn(date);
						vbSalesBook.setModifiedOn(date);
						vbSalesBook.setFlag(new Integer(1));
						vbSalesBook.setSalesBookNo(salesBookNo);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting SalesBook.");
						}
						session.save(vbSalesBook);
						++count;
					}
					
					// Persisting System Notifications.
					saveSystemNotificationForAndroid(session, username, salesExecutive, organization, 
							ENotificationTypes.ALLOTTED.name(), "N/A", salesBookNo, "N/A");
				}
				
				
				productName = allotStockCommand.getProductName();
				batchNumber = allotStockCommand.getBatchNumber();
				vbSalesBookProduct = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
						.createAlias("vbSalesBook", "salesBook")
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("salesBook.vbOrganization", organization))
						.add(Expression.eq("vbSalesBook", vbSalesBook))
						.add(Expression.eq("salesBook.flag", new Integer(1)))
						.uniqueResult();
				qtyAllotted = allotStockCommand.getQtyAllotted();
				qtyClosingBalance = allotStockCommand.getQtyClosingBalance();
				remarks = allotStockCommand.getRemarks();
				// Updating existing VbSalesBookProduct.
				if(vbSalesBookProduct != null){
					existingAllottedQty = vbSalesBookProduct.getQtyAllotted();
					Integer newAllotedQty;
					if(existingAllottedQty.intValue() == qtyAllotted.intValue()) {
						newAllotedQty = qtyAllotted;
					} else {
						newAllotedQty = qtyAllotted + existingAllottedQty;
					}
					vbSalesBookProduct.setRecentAllotment(qtyAllotted);
					vbSalesBookProduct.setQtyAllotted(newAllotedQty);
					vbSalesBookProduct.setQtyOpeningBalance(allotStockCommand.getQtyOpeningBalance());
					vbSalesBookProduct.setQtyClosingBalance(qtyClosingBalance);
					vbSalesBookProduct.setRemarks(remarks);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbSalesBookProducts.");
					}
					session.update(vbSalesBookProduct);
					
					/*____________________________________________________________________________*/
					 // code for Intermediate table vb_sales_book_allottments to get data for Factory wise product report and Product Report
					vbSalesBookAllotments = new VbSalesBookAllotments();
					vbSalesBookAllotments.setVbSalesBook(vbSalesBook);
					vbSalesBookAllotments.setProductName(productName);
					vbSalesBookAllotments.setBatchNumber(batchNumber);
					vbSalesBookAllotments.setQtyAllotted(qtyAllotted);
					vbSalesBookAllotments.setCreatedOn(date);
					vbSalesBookAllotments.setModifiedOn(date);
					vbSalesBookAllotments.setCreatedBy(username);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbSalesBookAllotments.");
					}
					// checking condition for only modified product qty allotment in vb_sales_book
					if(existingAllottedQty.intValue() == qtyAllotted.intValue()) {
					} else {
						session.save(vbSalesBookAllotments);
					}
					/*____________________________________________________________________________*/
					
					
					// Updating product history, if sales executive is active.
					if(qtyAllotted != 0) {
						query = session.createQuery("FROM VbProductInventoryTransaction vb WHERE vb.productName = :productName1 " +
								"AND vb.batchNumber = :batchNumber1 AND vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutive1 " +
								"AND vb.createdOn IN (SELECT MAX(vbp.createdOn) FROM VbProductInventoryTransaction vbp WHERE vbp.productName = :productName2 " +
								"AND vbp.batchNumber = :batchNumber2 AND vbp.vbOrganization = :vbOrganization2 AND vbp.salesExecutive = :salesExecutive2)")
								.setParameter("productName1", productName)
								.setParameter("batchNumber1", batchNumber)
								.setParameter("vbOrganization1", organization)
								.setParameter("salesExecutive1", salesExecutive)
								.setParameter("productName2", productName)
								.setParameter("batchNumber2", batchNumber)
								.setParameter("vbOrganization2", organization)
								.setParameter("salesExecutive2", salesExecutive);
						// need to put null check for below object
						productInventoryTransaction = getSingleResultOrNull(query);
						existingProdTxnQty = productInventoryTransaction.getQuantity();
						latestAllotment = allotStockCommand.getQtyAllotted();
						if(productInventoryTransaction != null && existingProdTxnQty.intValue() != latestAllotment.intValue()){
							productInventoryTransaction.setQuantity(existingProdTxnQty + latestAllotment);
							productInventoryTransaction.setModifiedOn(date);
							productInventoryTransaction.setModifiedBy(username);
							
							if(_logger.isDebugEnabled()){	
								_logger.debug("Updating productInventoryTransaction.");
							}
							session.update(productInventoryTransaction);
						}
					}
				} else {
					vbSalesBookProduct = new VbSalesBookProducts();
					vbSalesBookProduct.setProductName(productName);
					vbSalesBookProduct.setRecentAllotment(qtyAllotted);
					vbSalesBookProduct.setQtyAllotted(qtyAllotted);
					vbSalesBookProduct.setQtyOpeningBalance(allotStockCommand.getQtyOpeningBalance());
					vbSalesBookProduct.setQtyClosingBalance(qtyClosingBalance);
					vbSalesBookProduct.setVbSalesBook(vbSalesBook);
					vbSalesBookProduct.setRemarks(remarks);
					vbSalesBookProduct.setBatchNumber(batchNumber);
					if(allotStockCommand.getQtySold() == null) {
						vbSalesBookProduct.setQtySold(new Integer(0));
					}
					if(allotStockCommand.getQtyToFactory() == null) {
						vbSalesBookProduct.setQtyToFactory(new Integer(0));
					}
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting salesBookProduct.");
					}
					session.save(vbSalesBookProduct);
					
					/*____________________________________________________________________________*/
					 // code for Intermediate table vb_sales_book_allottments to get data for Factory wise product report and Product Report
					vbSalesBookAllotments = new VbSalesBookAllotments();
					vbSalesBookAllotments.setVbSalesBook(vbSalesBook);
					vbSalesBookAllotments.setProductName(productName);
					vbSalesBookAllotments.setBatchNumber(batchNumber);
					vbSalesBookAllotments.setQtyAllotted(qtyAllotted);
					vbSalesBookAllotments.setCreatedOn(date);
					vbSalesBookAllotments.setModifiedOn(date);
					vbSalesBookAllotments.setCreatedBy(username);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbSalesBookAllotments.");
					}
					session.save(vbSalesBookAllotments);
					/*____________________________________________________________________________*/
					
					// Persisting product history for new Allotment.
					if(qtyAllotted != 0) {
						productInventoryTransaction = new VbProductInventoryTransaction(); 
						productInventoryTransaction.setQuantity(allotStockCommand.getQtyAllotted());
						productInventoryTransaction.setSalesExecutive(salesExecutive);
						productInventoryTransaction.setProductName(productName);
						productInventoryTransaction.setBatchNumber(batchNumber);
						productInventoryTransaction.setCreatedBy(username);
						productInventoryTransaction.setCreatedOn(date);
						productInventoryTransaction.setModifiedOn(date);
						productInventoryTransaction.setVbOrganization(organization);
						productInventoryTransaction.setQuantityType(OrganizationUtils.PRODUCT_INVENTORY_TXN_TYPE_ALLOTTED);
						
						if(_logger.isDebugEnabled()){	
							_logger.debug("Saving productInventoryTransaction.");
						}
						session.save(productInventoryTransaction);
					}
				}
				
				// Updating AvailableQuantity in VbProduct.
				vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				
				if(vbProduct != null){
					Integer availableQty;
					if(existingAllottedQty.intValue() == qtyAllotted.intValue()) {
						availableQty = vbProduct.getAvailableQuantity();
					} else {
						availableQty = vbProduct.getAvailableQuantity() - allotStockCommand.getQtyAllotted();
					}
					vbProduct.setAvailableQuantity(availableQty);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating availableQuantity.");
					}
					session.update(vbProduct);
				}
			}
			txn.commit();
		} catch(HibernateException exception) {
			System.out.println("Exception :" + exception);
			if(txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		}  finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible for getting cycleId based on salesExecutive.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return cycleId - {@link Integer}
	 * 
	 */
	private Integer getCycleID(Session session, VbOrganization organization, String userName) {
		Integer cycleId = (Integer) session.createQuery("SELECT vb.cycleId FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutive1 AND " +
				"vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.vbOrganization = :vbOrganization2 AND vbs.salesExecutive = :salesExecutive2)")
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutive1", userName)
				.setParameter("vbOrganization2", organization)
				.setParameter("salesExecutive2", userName)
				.uniqueResult();
		return cycleId;
	}
	
	
	/**
	 * This method is responsible for checking the salesExecutive is existing or not if existing get all the previous details from db.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return productDetailsList - {@link List}.
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> checkSalesExecutive(String salesExecutive,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<AllotStockResult> productDetailsList = new ArrayList<AllotStockResult>();
		AllotStockResult allotStockResult=null;
		String productName = null;
		String batchNumber = null;
		VbSalesBook salesBook = null;
		Integer qtyClosingBal;
		Integer qtyAllotted;
		Integer qtyOpeningBal;
		String remarks = null;
		//getting salesBook details based on the salesExecutive for existing sales executive.
		salesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("salesExecutive", salesExecutive))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("flag", new Integer(1)))
				.uniqueResult();
		if(salesBook != null){
			   Float previousClosingBalance = salesBook.getClosingBalance();
			   Float openingBalance = salesBook.getOpeningBalance();
			   Float advance = salesBook.getAdvance();
			   String allotmentType = salesBook.getAllotmentType();
			   VbSalesBookProducts salesBookProducts = null;
			   //getting product details based on salesExecutiveId.
			   List<VbProduct> productsList = session.createCriteria(VbProduct.class)
					   .add(Expression.eq("vbOrganization", organization))
					   .add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
					   .list();
			   for(VbProduct products:productsList){
				productName = products.getProductName();
				batchNumber = products.getBatchNumber();
				//Getting the products from VbSalesBookProducts to fetch the stock related info.
				salesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
					     .createAlias("vbSalesBook", "salesBook")
					     .add(Expression.eq("vbSalesBook", salesBook))
					     .add(Expression.eq("salesBook.vbOrganization", organization))
					     .add(Restrictions.eq("productName", productName))
					     .add(Restrictions.eq("batchNumber", batchNumber))
					     .uniqueResult();
			    allotStockResult = new AllotStockResult();
			    allotStockResult.setAvailableQuantity(products.getAvailableQuantity());
			    allotStockResult.setPreviousClosingBalance(previousClosingBalance);
			    allotStockResult.setClosingBalance(new Float(0));
			    allotStockResult.setOpeningBalance(openingBalance);
			    allotStockResult.setAdvance(advance);
			    allotStockResult.setAllotmentType(allotmentType);
			    allotStockResult.setFlag("1");
			    if(salesBookProducts != null) {
			    	qtyClosingBal = salesBookProducts.getQtyClosingBalance();
			    	qtyAllotted = salesBookProducts.getQtyAllotted();
			    	qtyOpeningBal = salesBookProducts.getQtyOpeningBalance();
			    	remarks = salesBookProducts.getRemarks();
			    	allotStockResult.setId(salesBookProducts.getId());
			    } else {
			    	qtyClosingBal = new Integer(0);
			    	qtyAllotted = new Integer(0);
			    	qtyOpeningBal = new Integer(0);
			    	remarks = "";
			    }
			    allotStockResult.setProductName(productName);
			    allotStockResult.setBatchNumber(batchNumber);
			    allotStockResult.setPreviousQtyClosingStock(qtyClosingBal);
			    allotStockResult.setQtyAllotted(qtyAllotted);
			    allotStockResult.setQtyOpeningBalance(qtyOpeningBal);
			    allotStockResult.setRemarks(remarks);
			    allotStockResult.setClosingStock(new Integer(0));
			    productDetailsList.add(allotStockResult);
			   }
		}else{
			// If day book closed for sales executives.
			List<VbProduct> productList = session.createCriteria(VbProduct.class)
					   .add(Expression.eq("vbOrganization", organization))
					   .add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
					   .list();
			// For sales book.
			salesBook = (VbSalesBook) session.createQuery(
					"FROM VbSalesBook as sb WHERE sb.createdOn IN " +
					"(SELECT MAX(vb.createdOn) FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive " +
					"AND vb.vbOrganization = :vbOrganization)")
					.setParameter("salesExecutive",salesExecutive)
					.setParameter("vbOrganization", organization)
					.uniqueResult();
			if(salesBook != null && productList != null && productList.size() > 0) {
				Float closingBal = salesBook.getClosingBalance();
				for(VbProduct product:productList){
					productName = product.getProductName();
					batchNumber = product.getBatchNumber();
					allotStockResult = new AllotStockResult();
					allotStockResult.setPreviousClosingBalance(closingBal);
					allotStockResult.setClosingBalance(new Float(0));
					allotStockResult.setAdvance(new Float(0.00));
					allotStockResult.setOpeningBalance(closingBal);
					allotStockResult.setProductName(productName);
					allotStockResult.setBatchNumber(batchNumber);
					allotStockResult.setQtyAllotted(new Integer(0));
					allotStockResult.setClosingStock(new Integer(0));
					allotStockResult.setFlag("0");
					allotStockResult.setAvailableQuantity(product.getAvailableQuantity());
					// For Qty Closing balance.
					qtyClosingBal = (Integer) session.createCriteria(VbSalesBookProducts.class)
						.createAlias("vbSalesBook", "salesBook")
						.setProjection(Projections.property("qtyClosingBalance"))
						.add(Restrictions.eq("productName", productName))
						.add(Restrictions.eq("batchNumber", batchNumber))
						.add(Restrictions.eq("salesBook.vbOrganization", organization))
						.add(Restrictions.eq("vbSalesBook", salesBook))
						.uniqueResult();
					if(qtyClosingBal != null) {
						allotStockResult.setPreviousQtyClosingStock(qtyClosingBal);
						allotStockResult.setQtyOpeningBalance(qtyClosingBal);
					} else {
						allotStockResult.setPreviousQtyClosingStock(new Integer(0));
						allotStockResult.setQtyOpeningBalance(new Integer(0));
					}
					productDetailsList.add(allotStockResult);
				}
			} else {
				// For new Sales executives.
				session.close();
				throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
			}
		}
		session.close();
		return productDetailsList;
	}
	
	/**
	 * This method is responsible to get the closing balance for the sales executive based on {@link Date}.
	 * 
	 * @param inputDate - {@link Date}
	 * @param organization - {@link VbOrganization}
	 * @param salesExecutive - {@link String}
	 * @return clossingBalance - {@link Float}
	 * 
	 */
	public Float getclosingBalance(Date inputDate, VbOrganization organization,String salesExecutive) {
		Session session = this.getSession();
		Date startDate = DateUtils.getStartTimeStamp(inputDate);
		Float clossingBalance = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("closingBalance"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.between("createdOn", startDate, DateUtils.getEndTimeStamp(inputDate)))
				.uniqueResult();
		session.close();
		if(clossingBalance == null) {
			clossingBalance = new Float("0.00");
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("{} is the closing balance of {} on {}", clossingBalance, salesExecutive, startDate);
		}
		return clossingBalance;
	}
	
	/**
	 * This method is responsible to check whether the sales executive is available or not.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 * 
	 */
	public Boolean isSalesExecutiveAvailable(String salesExecutive, VbOrganization organization) {
		Boolean isAvailable = Boolean.FALSE;
		Session session = this.getSession();
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(vbEmployee != null) {
			isAvailable = Boolean.TRUE;
		}
		return isAvailable;
		
	}
	
	/**
	 * This method is responsible to get {@link VbSalesBookProducts} from database.
	 * 
	 * @param id - {@link Integer}
	 * @return vbSalesBookProducts - {@link VbSalesBookProducts}
	 */
	public VbSalesBookProducts getSalesBookProduct(int id , VbOrganization organization) {
		Session session = this.getSession();
		VbSalesBookProducts vbSalesBookProducts = (VbSalesBookProducts) session.get(VbSalesBookProducts.class, id);
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbSalesBookProducts: {}", vbSalesBookProducts);
		}
		return vbSalesBookProducts;
	}
	
	/**
	 * This method is responsible to get the available qty for particular product.
	 * 
	 * @param productName - {@link String}
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return availableQty - {@link Integer}
	 */
	public Integer getAvailableQty(String productName, String batchNumber, VbOrganization organization) {
		Session session = this.getSession();
		Integer availableQty = (Integer) session.createCriteria(VbProduct.class)
				.setProjection(Projections.property("availableQuantity"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} is the available quantity for {} with batch number {} ",availableQty, productName, batchNumber);
		}
		return availableQty;
	}

	/**
	 * This method is responsible to get the recent allotment based on the id.
	 * 
	 * @param id - {@link Integer}
	 * @return recentAllotment - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public Integer getRecentAllotmentById(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBookProducts vbSalesBookProducts = (VbSalesBookProducts) session.get(VbSalesBookProducts.class, id);
		session.close();
		if(vbSalesBookProducts != null) {
			Integer recentAllotment = vbSalesBookProducts.getRecentAllotment();
			return recentAllotment;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
		
	}
	 /**
	  * This method is responsible for updating the allot stock.
	  * 
	  * @param allotStockCommand - {@link AllotStockCommand}
	  * @param username - {@link String}
	  * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	  */
	public void editAllotStock(Integer id, Integer currentAllotment,
			Integer qtyOpeningBalance, String remarks, String username,
			VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbSalesBookProducts vbSalesBookProducts = (VbSalesBookProducts) session.get(VbSalesBookProducts.class, id);
			if (vbSalesBookProducts != null) {
				VbSalesBook vbSalesBook = vbSalesBookProducts.getVbSalesBook();
				Date date = new Date();
				if (vbSalesBook != null) {
					vbSalesBook.setModifiedBy(username);
					vbSalesBook.setModifiedOn(date);
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbSalesBook: {}", vbSalesBook);
					}
					session.update(vbSalesBook);
				}

				// Updating product available qty.
				VbProduct product = (VbProduct) session
						.createCriteria(VbProduct.class)
						.add(Restrictions.eq("productName",	vbSalesBookProducts.getProductName()))
						.add(Restrictions.eq("batchNumber",	vbSalesBookProducts.getBatchNumber()))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				Integer previousRecentAllotment = new Integer(0);
				Integer allotmentDiff = new Integer(0);
				if (product != null) {
					Integer allomentDifference;
					Integer availableQty;
					previousRecentAllotment = vbSalesBookProducts.getRecentAllotment();
					if (previousRecentAllotment < currentAllotment) {
						allomentDifference = currentAllotment - previousRecentAllotment;
						availableQty = product.getAvailableQuantity() - allomentDifference;
					} else {
						allomentDifference = previousRecentAllotment - currentAllotment;
						availableQty = product.getAvailableQuantity() + allomentDifference;
					}
					product.setModifiedBy(username);
					product.setModifiedOn(date);
					product.setAvailableQuantity(availableQty);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbProduct");
					}
					session.update(product);
				}
				//Updating VbSalesBookProducts with recent allotment.
				Integer previousAllotment = vbSalesBookProducts.getQtyAllotted();
				if(previousRecentAllotment < currentAllotment) {
					allotmentDiff = currentAllotment - previousRecentAllotment;
					vbSalesBookProducts.setQtyAllotted(previousAllotment + allotmentDiff);
				} else {
					allotmentDiff = previousRecentAllotment - currentAllotment;
					vbSalesBookProducts.setQtyAllotted(previousAllotment - allotmentDiff);
				}
				vbSalesBookProducts.setRecentAllotment(currentAllotment);
				vbSalesBookProducts.setQtyOpeningBalance(qtyOpeningBalance);
				vbSalesBookProducts.setRemarks(remarks);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbSalesBookProducts: {}", vbSalesBookProducts);
				}
				session.update(vbSalesBookProducts);
			} else {
				throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
			}
			txn.commit();
		} catch(HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

		/**
		 * This method is responsible to get the existing allotment based on the sales executive.
		 * 
		 * @param productName - {@link String}
		 * @param batchNumber - {@link String}
		 * @param salesExecutive - {@link String}
		 * @param organization - {@link VbOrganization}
		 * @return existingQtyAllotted - {@link Integer}
		 * @throws DataAccessException - {@link DataAccessException}
		 */
	public Integer getExistingAllotment(String productName, String batchNumber,
			String salesExecutive, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		Integer existingQtyAllotted = null;
		VbSalesBook vbSalesBook = getVbSalesBookForExistingAllotment(session, organization, salesExecutive);
		if (vbSalesBook != null) {
			existingQtyAllotted = (Integer) session
					.createCriteria(VbSalesBookProducts.class)
					.setProjection(Projections.property("qtyAllotted"))
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.eq("vbSalesBook", vbSalesBook))
					.uniqueResult();
			session.close();
			return existingQtyAllotted;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
		
		/**
		 * This method is responsible to get the {@link VbSalesBook} object for sales executive.
		 * 
		 * @param session - {@link Session}
		 * @param organization - {@link VbOrganization}
		 * @param salesExecutive - {@link String}
		 * @return vbSalesBook - {@link VbSalesBook}
		 */
		private VbSalesBook getVbSalesBookForExistingAllotment(Session session, VbOrganization organization, String salesExecutive) {
		VbSalesBook vbSalesBook = (VbSalesBook) session.createQuery(
				"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn "
				+ "IN(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2 " +
				"AND vbs.flag = :flag)")
				.setParameter("salesExecutive1", salesExecutive)
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutive2", salesExecutive)
				.setParameter("vbOrganization2", organization)
				.setParameter("flag", new Integer(1))
				.uniqueResult();
			return vbSalesBook;
		}
}
