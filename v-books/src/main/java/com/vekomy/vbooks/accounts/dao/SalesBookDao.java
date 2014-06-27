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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
import com.vekomy.vbooks.accounts.command.SalesCommand;
import com.vekomy.vbooks.accounts.command.SalesResult;
import com.vekomy.vbooks.accounts.command.SalesReturnCommand;
import com.vekomy.vbooks.accounts.command.SalesReturnResult;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.util.DateUtils;
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
	 * 
	 */
	@SuppressWarnings({ "unchecked"})
	public List<SalesReturnResult> getSalesReturns(VbOrganization organization) {
		Session session = this.getSession();
		List<VbSalesReturn> salesReturnsList = session.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn")).list();
		List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
		SalesReturnResult result = null;
		for (VbSalesReturn vbSalesReturn : salesReturnsList) {
			result = new SalesReturnResult();
			Float total = getSalesReturnProducts(vbSalesReturn.getId(), organization);
			result.setCreatedBy(vbSalesReturn.getCreatedBy());
			result.setDate(DateUtils.format(vbSalesReturn.getCreatedOn()));
			result.setTotalCost(StringUtil.floatFormat(total));
			result.setId(vbSalesReturn.getId());
			result.setBusinessName(vbSalesReturn.getBusinessName());
			salesResultList.add(result);
		}
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} sales returns records found.", salesReturnsList.size());
		}
		return salesResultList;
	}

	/**
	 * This method is responsible to retrieve sum of total cost of based on
	 * salesReturnId {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return sumTotal - {@link Float}
	 * 
	 */
	private Float getSalesReturnProducts(Integer salesReturnId, VbOrganization organization) {
		Session session = this.getSession();
		Float sumTotal = (Float) session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "salesReturn")
				.setProjection(Projections.sum("totalCost"))
				.add(Expression.eq("salesReturn.id", salesReturnId))
				.add(Expression.eq("salesReturn.vbOrganization", organization))
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
	 * 
	 */
	public VbSalesReturn getSalesReturnProductsCost(Integer salesReturnId, VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbSalesReturn salesReturn = (VbSalesReturn) session.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("id", salesReturnId))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if(salesReturn == null){
			if(_logger.isErrorEnabled()){
				_logger.error("Records not found with id: {}", salesReturnId);
			}
			return null;
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("VbSalesReturn: {}", salesReturn);
		}
		return salesReturn;
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from
	 * DB on criteria.
	 * 
	 * @param salesReturnCommand -{@link SalesReturnCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesReturnsList 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturnsCriteria(SalesReturnCommand salesReturnCommand, VbOrganization organization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("SalesReturnCommand: {}", salesReturnCommand);
		}
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbSalesReturn.class);
		if (salesReturnCommand != null) {
			String businessName = salesReturnCommand.getBusinessName();
			String invoiceName = salesReturnCommand.getInvoiceName();
			Date createdOn = salesReturnCommand.getCreatedOn();
            String createdBy=salesReturnCommand.getCreatedBy();
            if(!createdBy.isEmpty()) {
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
		criteria.addOrder(Order.asc("createdOn"));
		List<VbSalesReturn> criteriaList = criteria.list();
		List<SalesReturnResult> salesReturnList = new ArrayList<SalesReturnResult>();
		SalesReturnResult salesResult = null;
		for (VbSalesReturn vbSalesReturn : criteriaList) {
			salesResult = new SalesReturnResult();
			Float total = getSalesReturnProducts(vbSalesReturn.getId(), organization);
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
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSales} from DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SalesResult> getSales(VbOrganization organization) {
		Session session = this.getSession();
		List<SalesResult> salesResultList = new ArrayList<SalesResult>();
		List<VbSalesBook> salesList = session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn")).list();
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
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesBook} from
	 * DB on criteria.
	 * 
	 * @param salesCommand - {{@link SalesCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	public List<SalesResult> getSalesOnCriteria(SalesCommand salesCommand, VbOrganization vbOrganization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("SalesCommand: {}", salesCommand);
		}
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
		criteria.addOrder(Order.asc("createdOn"));
		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		List<VbSalesBook> criteriaList = criteria.list();
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
		
		if(_logger.isDebugEnabled()){
			_logger.debug("{} sales records found.", salesResultList.size());
		}
		return salesResultList;
	}

	/**
	 * This method is responsible to retrieve the salesProducts based on the sales id.
	 * @param salesId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return vbSalesBook - {@link VbSalesBook}
	 */
	public VbSalesBook getSalesProduct(Integer salesId , VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("id", salesId))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbSalesBook: {}", vbSalesBook);
		}
		return vbSalesBook;

	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesBook} from DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesExecutiveDetailsList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> getSalesExecutives(VbOrganization vbOrganization, String salesExecutiveName) {
		Session session = this.getSession();
		List<AllotStockResult> salesExecutiveDetailsList = new ArrayList<AllotStockResult>();
		List<String> salesExecutiveList = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.add(Expression.like("username", salesExecutiveName, MatchMode.START))
				.list();
		AllotStockResult allotStockResult = null;
		VbSalesBook vbSalesBook = null;
		if(!salesExecutiveList.isEmpty()){
			for (String salesExecutive : salesExecutiveList) {
				allotStockResult = new AllotStockResult();
				allotStockResult.setSalesExecutive(salesExecutive);
				vbSalesBook = (VbSalesBook) session.createQuery("FROM VbSalesBook vb WHERE vb.createdOn IN " +
						"(SELECT MAX(vbs.createdOn) FROM  VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive " +
						"AND vbs.vbOrganization = :vbOrganization AND vbs.flag = :flag)")
						.setParameter("salesExecutive", salesExecutive)
						.setParameter("vbOrganization", vbOrganization)
						.setParameter("flag", new Integer(1))
						.uniqueResult();
				if (vbSalesBook == null) {
					allotStockResult.setAdvance(new Float(0));
					allotStockResult.setOpeningBalance(new Float(0));
				} else {
					allotStockResult.setAdvance(vbSalesBook.getAdvance());
					allotStockResult.setOpeningBalance(vbSalesBook.getOpeningBalance());
				}
				salesExecutiveDetailsList.add(allotStockResult);
			}
			session.close();
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} salesExecutives found.", salesExecutiveDetailsList.size());
			}
			return salesExecutiveDetailsList;
		} else {
			if(_logger.isErrorEnabled()){
				_logger.error("No employee found as sales executive.");
			}
			return null;
		}
	}

	/**
	 * This method is responsible for fetching the closingBalance based on the salesExecutive from DB.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return closingBalance - {@link Float}
	 */
	public Float getClosingBalance(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		Query query = session.createQuery(
				"SELECT sb.closingBalance FROM VbSalesBook as sb WHERE sb.salesExecutive = :salesExecutive and sb.vbOrganization = :vbOrganization " +
				"AND sb.createdOn IN (SELECT MAX(vb.createdOn) FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName)")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", salesExecutive);
		Float closingBalance = getSingleResultOrNull(query);
		session.close();
		if (closingBalance == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for salesExecutive: {}", closingBalance);
			}
			return null;
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} is the previous Closing Balance for the sales executive: {}.", closingBalance, salesExecutive);
		}
		return closingBalance;
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesBookProduct} from DB.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return productDetailsList - {@link List<AllotStockResult>}
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> getProductDetails(VbOrganization organization) {
		Session session = this.getSession();
		List<AllotStockResult> productDetailsList = new ArrayList<AllotStockResult>();
		List<VbProduct> productsList = session.createCriteria(VbProduct.class)
				.add(Expression.eq("vbOrganization", organization))
				.list();
		AllotStockResult allotStockResult = null;
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
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} products found.", productDetailsList.size());
		}
		return productDetailsList;
	}
	
	/**
	 * This method is responsible for saving {@link VbSalesBookProduct} in DB and saveOrUpdate the {@link VbSalesBook} in DB.
	 * 
	 * @param allotStockCommand -{@link AllotStockCommand}
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isSaved - {@link Boolean}
	 */
	public synchronized Boolean saveOrUpdateAllotStock(List<AllotStockCommand> allotStockList,String username,VbOrganization organization) {
		Session session = this.getSession();
		VbSalesBook vbSalesBook = null;
		VbProduct vbProduct = null;
		VbSalesBookProducts vbSalesBookProduct = null;
		Boolean isSaved = Boolean.FALSE;
		Float advance = new Float(0);
		Transaction txn = session.beginTransaction();
		Integer count = new Integer(0);
		for (AllotStockCommand allotStockCommand : allotStockList) {
			String salesExecutive = allotStockCommand.getSalesExecutive();
			advance = allotStockCommand.getAdvance();
			Float closingBalance = allotStockCommand.getClosingBalance();
			Float openingBalance = allotStockCommand.getOpeningBalance();
			String allotmentType = allotStockCommand.getAllotmentType();
			vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
					.add(Expression.eq("salesExecutive", salesExecutive))
					.add(Expression.eq("vbOrganization", organization))
					.add(Expression.eq("flag", new Integer(1)))
					.uniqueResult();
			if(vbSalesBook != null && count == 0) {
				vbSalesBook.setClosingBalance(closingBalance);
				vbSalesBook.setAllotmentType(allotmentType);
				Integer cycleId = vbSalesBook.getCycleId();
				vbSalesBook.setCycleId(++ cycleId);
				Float previousAdvance = vbSalesBook.getAdvance();
				if(previousAdvance == null) {
					vbSalesBook.setAdvance(advance);
					vbSalesBook.setOpeningBalance(openingBalance);
				} else {
					if(vbSalesBook.getAdvance().floatValue() != advance) {
						vbSalesBook.setAdvance(previousAdvance + advance);
						vbSalesBook.setOpeningBalance(vbSalesBook.getOpeningBalance() + advance);
					}
				}
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating SalesBook: {}", vbSalesBook);
				}
				session.update(vbSalesBook);
				++count;
			}else if(vbSalesBook == null) {
				vbSalesBook = new VbSalesBook();
				vbSalesBook.setCycleId(new Integer(1));
				vbSalesBook.setAdvance(advance);
				vbSalesBook.setClosingBalance(closingBalance);
				vbSalesBook.setCreatedBy(username);
				vbSalesBook.setOpeningBalance(openingBalance);
				vbSalesBook.setSalesExecutive(salesExecutive);
				vbSalesBook.setVbOrganization(organization);
				vbSalesBook.setAllotmentType(allotmentType);
				Date date = new Date();
				vbSalesBook.setCreatedOn(date);
				vbSalesBook.setModifiedOn(date);
				vbSalesBook.setFlag(new Integer(1));
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting SalesBook: {}", vbSalesBook);
				}
				session.save(vbSalesBook);
				isSaved = Boolean.TRUE;
				++count;
			}
			
			String productName = allotStockCommand.getProductName();
			String batchNumber = allotStockCommand.getBatchNumber();
			vbSalesBookProduct = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
					.createAlias("vbSalesBook", "salesBook")
					.add(Expression.eq("productName", productName))
					.add(Expression.eq("batchNumber", batchNumber))
					.add(Expression.eq("salesBook.vbOrganization", organization))
					.add(Expression.eq("vbSalesBook", vbSalesBook))
					.add(Expression.eq("salesBook.flag", new Integer(1)))
					.uniqueResult();
			Integer qtyAllotted = allotStockCommand.getQtyAllotted();
			Integer qtyClosingBalance = allotStockCommand.getQtyClosingBalance();
			String remarks = allotStockCommand.getRemarks();
			// Updating existing VbSalesBookProduct.
			Integer existingAllottedQty = new Integer(0);
			if(vbSalesBookProduct != null){
				existingAllottedQty = vbSalesBookProduct.getQtyAllotted();
				Integer newAllotedQty;
				if(existingAllottedQty.intValue() == qtyAllotted.intValue()) {
					newAllotedQty = qtyAllotted;
				} else {
					newAllotedQty = qtyAllotted + existingAllottedQty;
				}
				vbSalesBookProduct.setQtyAllotted(newAllotedQty);
				vbSalesBookProduct.setQtyOpeningBalance(newAllotedQty);
				vbSalesBookProduct.setQtyClosingBalance(qtyClosingBalance);
				vbSalesBookProduct.setRemarks(remarks);
				vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating salesBookProduct: {}", vbSalesBookProduct);
				}
				session.update(vbSalesBookProduct);
				isSaved = Boolean.TRUE;
			} else {
				VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
				Integer qtyClosingBal = (Integer) session.createQuery(
						"SELECT vb.qtyClosingBalance FROM VbSalesBookProducts vb WHERE " +
						"vb.productName = :productName AND vb.batchNumber = :batchNumber AND vb.vbSalesBook = :vbSalesBook")
						.setParameter("productName", productName)
						.setParameter("batchNumber", batchNumber)
						.setParameter("vbSalesBook", salesBook)
						.uniqueResult();
				vbSalesBookProduct = new VbSalesBookProducts();
				vbSalesBookProduct.setProductName(productName);
				vbSalesBookProduct.setQtyAllotted(qtyAllotted);
				if(qtyClosingBal == null) {
					vbSalesBookProduct.setQtyOpeningBalance(qtyAllotted);
				} else{
					vbSalesBookProduct.setQtyOpeningBalance(qtyAllotted + qtyClosingBal);
				}
				vbSalesBookProduct.setQtyClosingBalance(qtyClosingBalance);
				vbSalesBookProduct.setVbSalesBook(vbSalesBook);
				vbSalesBookProduct.setRemarks(remarks);
				vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				vbSalesBookProduct.setBatchNumber(vbProduct.getBatchNumber());
				if(allotStockCommand.getQtySold() == null) {
					vbSalesBookProduct.setQtySold(new Integer(0));
				}
				if(allotStockCommand.getQtyToFactory() == null) {
					vbSalesBookProduct.setQtyToFactory(new Integer(0));
				}
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting salesBookProduct: {}", vbSalesBookProduct);
				}
				session.save(vbSalesBookProduct);
				isSaved = Boolean.TRUE;
			}
			
			// Updating AvailableQuantity in VbProduct.
			if(vbProduct != null){
				Integer availableQty;
				if(existingAllottedQty.intValue() == qtyAllotted.intValue()) {
					availableQty = vbProduct.getAvailableQuantity();
				} else {
					availableQty = vbProduct.getAvailableQuantity() - allotStockCommand.getQtyAllotted();
				}
				vbProduct.setAvailableQuantity(availableQty);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating availableQuantity{}", vbProduct);
				}
				session.update(vbProduct);
				isSaved = Boolean.TRUE;
			}
			
			VbProductInventoryTransaction productInventoryTransaction = new VbProductInventoryTransaction(); 
			if(productInventoryTransaction != null){
				Date date = new Date();
				productInventoryTransaction.setOutwardsQty(allotStockCommand.getQtyAllotted());
				productInventoryTransaction.setSalesExecutive(salesExecutive);
				productInventoryTransaction.setProductName(productName);
				productInventoryTransaction.setBatchNumber(batchNumber);
				productInventoryTransaction.setCreatedBy(username);
				productInventoryTransaction.setCreatedOn(date);
				productInventoryTransaction.setModifiedOn(date);
				productInventoryTransaction.setVbOrganization(organization);
				productInventoryTransaction.setInwardsQty(new Integer(0));
				
				if(_logger.isDebugEnabled()){	
					_logger.debug("Saving productInventoryTransaction: {}", productInventoryTransaction);
				}
				session.save(productInventoryTransaction);
				isSaved = Boolean.TRUE;
			}
		}
		
		txn.commit();
		session.close();
		return isSaved;
	}
	
	/**
	 * This method is responsible for checking the salesExecutive is existing or not if existing get all the previous details from db.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return productDetailsList - {@link List}.
	 */
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> checkSalesExecutive(String salesExecutive, VbOrganization organization)  {
		Session session = this.getSession();
		List<AllotStockResult> productDetailsList = new ArrayList<AllotStockResult>();
		AllotStockResult allotStockResult=null;
		Integer returnQty = null;
		String productName = null;
		String batchNumber = null;
		VbSalesBook salesBook = null;
		Integer qtyClosingBal;
		Integer qtyAllotted;
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
			    allotStockResult=new AllotStockResult();
			    allotStockResult.setAvailableQuantity(products.getAvailableQuantity());
			    allotStockResult.setPreviousClosingBalance(previousClosingBalance);
			    allotStockResult.setClosingBalance(new Float(0));
			    allotStockResult.setOpeningBalance(openingBalance);
			    allotStockResult.setAdvance(advance);
			    allotStockResult.setAllotmentType(allotmentType);
			    allotStockResult.setFlag("1");
			    // For return Qty.
			    returnQty = (Integer) session.createQuery("SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb " +
			    		"WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND " +
			    		"vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName AND vb.batchNumber = :batchNumber GROUP BY vb.productName, vb.batchNumber")
			    		.setParameter("vbOrganization", organization)
			    		.setParameter("vbSalesBook", salesBook)
			    		.setParameter("productName", productName)
				        .setParameter("batchNumber", batchNumber)
			    		.uniqueResult();
			    
			    if(returnQty != null) {
			     allotStockResult.setReturnQty(returnQty);
			    } else {
			     allotStockResult.setReturnQty(new Integer(0));
			    }
			    if(salesBookProducts != null) {
			    	qtyClosingBal = salesBookProducts.getQtyClosingBalance();
			    	qtyAllotted = salesBookProducts.getQtyAllotted();
			    	remarks = salesBookProducts.getRemarks();
			    } else {
			    	qtyClosingBal = new Integer(0);
			    	qtyAllotted = new Integer(0);
			    	remarks = "";
			    }
			    allotStockResult.setProductName(productName);
			    allotStockResult.setBatchNumber(batchNumber);
			    allotStockResult.setPreviousQtyClosingStock(qtyClosingBal);
			    allotStockResult.setQtyAllotted(qtyAllotted);
			    allotStockResult.setQtyOpeningBalance(qtyClosingBal + qtyAllotted);
			    allotStockResult.setRemarks(remarks);
			    allotStockResult.setClosingStock(new Integer(0));
			   // allotStockResult.setId(salesBookProducts.getId());
			    productDetailsList.add(allotStockResult);
			   }
		}else{
			// If day book closed for sales executives.
			List<VbProduct> productList = session.createCriteria(VbProduct.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
			// For sales book.
			salesBook = (VbSalesBook) session.createQuery(
					"FROM VbSalesBook as sb WHERE sb.createdOn IN " +
					"(SELECT MAX(vb.createdOn) FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive AND vb.vbOrganization = :vbOrganization)")
					.setParameter("salesExecutive",salesExecutive)
					.setParameter("vbOrganization", organization)
					.uniqueResult();
			if(salesBook != null && productList != null && productList.size() > 0) {
				Float closingBal = salesBook.getClosingBalance();
				for(VbProduct product:productList){
					productName = product.getProductName();
					batchNumber = product.getBatchNumber();
					allotStockResult=new AllotStockResult();
					allotStockResult.setPreviousClosingBalance(closingBal);
					allotStockResult.setClosingBalance(new Float(0));
					allotStockResult.setAdvance(new Float(0.00));
					allotStockResult.setOpeningBalance(closingBal);
				
					// For product return qty.
					returnQty = (Integer) session.createQuery("SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb " +
				             "WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND " +
				             "vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName AND vb.batchNumber = :batchNumber GROUP BY vb.productName, vb.batchNumber")
				             .setParameter("vbOrganization", organization)
				             .setParameter("vbSalesBook", salesBook)
				             .setParameter("productName", productName)
				             .setParameter("batchNumber", batchNumber)
				             .uniqueResult();
				
					if(returnQty != null) {
						allotStockResult.setReturnQty(returnQty);
					} else {
						allotStockResult.setReturnQty(new Integer(0));
					}
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
				return null;
			}
		}
			return productDetailsList;
	}
	
	@SuppressWarnings("unchecked")
	public List<AllotStockResult> getGridByDate(String salesExecName, String selectedDate, VbOrganization organization) throws ParseException {
		Session session = this.getSession();
		Date createdOn = DateUtils.parse(selectedDate);
		Date startDate = DateUtils.getStartTimeStamp(createdOn);
		Date endDate = DateUtils.getEndTimeStamp(createdOn);
		List<VbSalesBookProducts> productList = session.createQuery("FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.vbOrganization = :vbOrganization " +
				"AND vb.vbSalesBook.salesExecutive = :salesExecutive AND vb.vbSalesBook.createdOn <= :createdOn GROUP BY vb.productName,vb.batchNumber")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutive", salesExecName)
				.setParameter("createdOn", endDate)
				.list();
		List<AllotStockResult> allotStockResults = new ArrayList<AllotStockResult>();
		AllotStockResult allotStockResult = null;
		VbSalesBook salesBook = null;
		Float previousClosingBal;
		String productName = null;
		String batchNumber = null;
		Integer returnQty;
		Integer availableQty;
		Float closingBalanceByDate;
		Integer previousQtyClosingStock;
		Integer allottedQty;
		VbSalesBookProducts salesBookProducts;
		for (VbSalesBookProducts vbSalesBookProducts : productList) {
			allotStockResult = new AllotStockResult();
			productName = vbSalesBookProducts.getProductName();
			batchNumber = vbSalesBookProducts.getBatchNumber();
			previousQtyClosingStock = vbSalesBookProducts.getQtyClosingBalance();
			// Filling the form fields.
			salesBook = vbSalesBookProducts.getVbSalesBook();
			allotStockResult.setAdvance(salesBook.getAdvance());
			allotStockResult.setAllotmentType(salesBook.getAllotmentType());
			closingBalanceByDate = (Float) session.createCriteria(VbSalesBook.class)
					.setProjection(Projections.property("closingBalance"))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("salesExecutive", salesExecName))
					.add(Restrictions.between("createdOn", startDate, endDate))
					.uniqueResult();
			if(closingBalanceByDate == null) {
				closingBalanceByDate = new Float(0);
			}
			allotStockResult.setClosingBalance(closingBalanceByDate);
			previousClosingBal = (Float) session.createCriteria(VbSalesBook.class)
					.setProjection(Projections.sum("closingBalance"))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("salesExecutive", salesExecName))
					.add(Restrictions.lt("createdOn", startDate))
					.uniqueResult();
			if(previousClosingBal == null) {
				previousClosingBal = new Float(0);
			}
			allotStockResult.setPreviousClosingBalance(previousClosingBal);
			allotStockResult.setOpeningBalance(salesBook.getOpeningBalance());
			
			// Filling grid.
			allotStockResult.setProductName(productName);
			allotStockResult.setBatchNumber(batchNumber);
			allotStockResult.setPreviousQtyClosingStock(previousQtyClosingStock);
			allotStockResult.setRemarks(vbSalesBookProducts.getRemarks());
			salesBookProducts = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
					.createAlias("vbSalesBook", "salesBook")
					.add(Restrictions.eq("vbSalesBook", salesBook))
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.eq("salesBook.vbOrganization", organization))
					.add(Restrictions.eq("salesBook.salesExecutive", salesExecName))
					.add(Restrictions.between("salesBook.createdOn", startDate, endDate))
					.uniqueResult();
			allottedQty = salesBookProducts.getQtyAllotted();
			allotStockResult.setClosingStock(salesBookProducts.getQtyClosingBalance());
			allotStockResult.setQtyAllotted(allottedQty);
			allotStockResult.setQtyOpeningBalance(previousQtyClosingStock + allottedQty);
			returnQty = (Integer) session.createCriteria(VbSalesReturnProducts.class)
					.createAlias("vbSalesReturn", "salesReturn")
					.setProjection(Projections.property("totalQty"))
					.add(Restrictions.eq("salesReturn.vbOrganization", organization))
					.add(Restrictions.eq("salesReturn.createdBy", salesExecName))
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.lt("salesReturn.createdOn", endDate))
					.uniqueResult();
			if(returnQty == null) {
				returnQty = new Integer(0);
			} 
			allotStockResult.setReturnQty(returnQty);
			
			availableQty = (Integer) session.createCriteria(VbProduct.class)
					.setProjection(Projections.property("availableQuantity"))
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.between("createdOn", startDate, endDate))
					.uniqueResult();
			if(availableQty == null) {
				availableQty = new Integer(0);
			}
			allotStockResult.setAvailableQuantity(availableQty);
			allotStockResults.add(allotStockResult);
			
		}
				
		return allotStockResults;
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
		if(vbEmployee != null) {
			isAvailable = Boolean.TRUE;
		}
		return isAvailable;
		
	}
	
	/**
	 * This method is responsible to give {@link VbSalesBook} base on max {@link Date}.
	 *  
	 * @param session - {@link Session}
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getVbSalesBook(Session session, String salesExecutive, VbOrganization organization) {
		VbSalesBook salesBook = (VbSalesBook) session.createQuery("FROM VbSalesBook vb WHERE vb.createdOn " +
				"IN(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive AND vbs.vbOrganization = :vbOrganization)")
						.setParameter("salesExecutive", salesExecutive)
						.setParameter("vbOrganization", organization)
						.uniqueResult();
		return salesBook;
	}

	/**
	 * This method is responsible to get the previous opening balance for particular sales executive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return previousOpeningBal - {@link Float}
	 */
	public Float getPreviousOpeningBalance(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		Float previousOpeningBal = (Float) session.createQuery("SELECT vb.openingBalance FROM VbSalesBook vb " +
				"WHERE vb.salesExecutive = :salesExecutive1 AND vb.vbOrganization = :vbOrganization1 AND " +
				"vb.createdOn IN(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2)")
				.setParameter("vbOrganization1", organization)
				.setParameter("vbOrganization2", organization)
				.setParameter("salesExecutive1", salesExecutive)
				.setParameter("salesExecutive2", salesExecutive)
				.uniqueResult();
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Previous Opening Balance For {} is {}",salesExecutive,previousOpeningBal);
		}
		return previousOpeningBal;
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
			_logger.debug("{} is the available quantity for {} with batch number {} ",availableQty,productName,batchNumber);
		}
		return availableQty;
	}

	 /**
	  * This method is responsible for updating the allot stock.
	  * 
	  * @param allotStockCommand - {@link AllotStockCommand}
	  * @param username - {@link String}
	  * @param organization - {@link VbOrganization}
	  */
	 public void editAllotStock(AllotStockCommand allotStockCommand, String username, VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbSalesBookProducts vbSalesBookProducts = (VbSalesBookProducts) session.get(VbSalesBookProducts.class, allotStockCommand.getId());
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
			Integer currentAllotment = allotStockCommand.getQtyAllotted();
			Integer previousAllotment = vbSalesBookProducts.getQtyAllotted();

			// Updating product available qty.
			VbProduct product = (VbProduct) session
					.createCriteria(VbProduct.class)
					.add(Restrictions.eq("productName",	vbSalesBookProducts.getProductName()))
					.add(Restrictions.eq("batchNumber",	vbSalesBookProducts.getBatchNumber()))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (product != null) {
				Integer allomentDifference;
				Integer availableQty;
				if (previousAllotment < currentAllotment) {
					allomentDifference = currentAllotment - previousAllotment;
					availableQty = product.getAvailableQuantity() - allomentDifference;
				} else {
					allomentDifference = previousAllotment - currentAllotment;
					availableQty = product.getAvailableQuantity() + allomentDifference;
				}
				product.setModifiedBy(username);
				product.setModifiedOn(date);
				product.setAvailableQuantity(availableQty);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbProduct: {}", product);
				}
				session.update(product);
			}

			vbSalesBookProducts.setQtyAllotted(currentAllotment);
			vbSalesBookProducts.setQtyOpeningBalance(allotStockCommand.getQtyOpeningBalance());
			vbSalesBookProducts.setRemarks(allotStockCommand.getRemarks());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbSalesBookProducts: {}",
						vbSalesBookProducts);
			}
			session.update(vbSalesBookProducts);
		} else {
			if (_logger.isErrorEnabled()) {
				_logger.error("Products Not Found To Update");
			}
		}
		txn.commit();
		session.close();
	}

}
