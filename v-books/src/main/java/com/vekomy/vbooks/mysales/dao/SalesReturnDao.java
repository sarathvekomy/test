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

import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.mysales.command.SalesReturnCommand;
import com.vekomy.vbooks.mysales.command.SalesReturnResult;
import com.vekomy.vbooks.mysales.command.SalesReturnsResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
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
	 * 
	 */
	public synchronized Boolean saveSalesReturns(
			List<SalesReturnCommand> salesReturnCommands,
			VbOrganization organization, String userName) {
		Boolean isSaved = Boolean.FALSE;
		Session session = this.getSession();
		VbSalesReturn instanceSalesReturn = new VbSalesReturn();
		if (instanceSalesReturn != null) {
			instanceSalesReturn.setCreatedBy(userName);
			Date date = new Date();
			instanceSalesReturn.setCreatedOn(date);
			instanceSalesReturn.setModifiedOn(date);
			instanceSalesReturn.setVbOrganization(organization);
			instanceSalesReturn.setStatus(CRStatus.PENDING.name());
			VbSalesBook salesBook = getSalesBook(session, organization, userName);
			if (salesBook != null) {
				instanceSalesReturn.setVbSalesBook(salesBook);
			}
			String businessName = null;
			String invoiceName = null;
			for (SalesReturnCommand command : salesReturnCommands) {
				Transaction txn = session.beginTransaction();
				businessName = command.getBusinessName();
				invoiceName = command.getInvoiceName();
				instanceSalesReturn.setBusinessName(businessName);
				instanceSalesReturn.setInvoiceName(invoiceName);
				instanceSalesReturn.setInvoiceNo(command.getInvoiceNo());
				instanceSalesReturn.setProductsGrandTotal(command.getGrandTotalCost());
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbSalesReturn: {}", instanceSalesReturn);
				}
				session.save(instanceSalesReturn);
				isSaved = Boolean.TRUE;

				String productName = command.getProductName();
				String batchNumber = command.getBatchNumber();
				VbSalesReturnProducts instanceReturnProducts = new VbSalesReturnProducts();
				if (instanceReturnProducts != null) {
					instanceReturnProducts.setCost(command.getCost());
					instanceReturnProducts.setProductName(productName);
					instanceReturnProducts.setBatchNumber(batchNumber);
					instanceReturnProducts.setDamaged(command.getDamaged());
					instanceReturnProducts.setResalable(command.getResalable());
					instanceReturnProducts.setTotalQty(command.getTotalQty());
					instanceReturnProducts.setTotalCost(command.getTotalCost());
					instanceReturnProducts.setVbSalesReturn(instanceSalesReturn);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSalesReturnProducts: {}", instanceReturnProducts);
					}
					session.save(instanceReturnProducts);
					isSaved = Boolean.TRUE;
				}
				txn.commit();
			}
		}
		session.close();
		return isSaved;
	}
	
	/**
	 * This method is responsible to update {@link VbProduct}, {@link VbCustomerCreditInfo} 
	 * and {@link VbCustomerAdvanceInfo} after returning the products.
	 * 
	 * @param command - {@link SalesReturnCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * 
	 */
	private void updateProductsAndPreviousCredits(Session session, List<SalesReturnCommand> commands, VbOrganization organization, String userName){
		Transaction txn = session.beginTransaction();
		
		// Updating VbProduct with the return Qty.
		VbProduct vbProduct = null;
		Float grandTotalCost = new Float(0.0);
		String businessName = null;
		for (SalesReturnCommand command : commands) {
			vbProduct = (VbProduct) session
					.createCriteria(VbProduct.class)
					.add(Expression.eq("productName", command.getProductName()))
					.add(Expression.eq("batchNumber", command.getBatchNumber()))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			Integer resalableProductQty = command.getResalable();
			if (vbProduct != null && resalableProductQty != null) {
				vbProduct.setAvailableQuantity(vbProduct.getAvailableQuantity() + resalableProductQty);
				vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() + resalableProductQty);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbProduct: {}", vbProduct);
				}
				session.update(vbProduct);
			}
			grandTotalCost = command.getGrandTotalCost();
			businessName = command.getBusinessName();
		}
		
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
		if (grandTotalCost > 0) {
			Query query = null;
			while (grandTotalCost != 0) {
				query = session.createQuery(
				"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
						+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
						+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
						.setParameter("businessName", businessName)
						.setParameter("vbOrganization", organization)
						.setParameter("due", new Float("0"));

				VbCustomerCreditInfo creditInfo = getSingleResultOrNull(query);
				String newInvoiceNo = generateInvoiceNo(organization);
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
					grandTotalCost = new Float(0.0);
				} else {
					grandTotalCost = grandTotalCost - existingDue;
					creditInfo.setDue(new Float("0.00"));
				}
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerCreditInfo: {}", creditInfo);
				}
				session.update(creditInfo);
				} else {
					saveCustomerAdvanceInfo(session, businessName, newInvoiceNo, organization, grandTotalCost, userName);
					grandTotalCost = new Float("0");
				}
			}
		}
	txn.commit();
	// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
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
		Query query = session.createQuery(
				"SELECT vb.invoiceNo FROM VbSalesReturn vb WHERE vb.createdOn IN (SELECT MAX(vbsr.createdOn) FROM VbSalesReturn vbsr " +
				"WHERE vbsr.vbOrganization = :vbOrganization)")
				.setParameter("vbOrganization", organization);
		String latestInvoiceNo = getSingleResultOrNull(query);
		String generatedInvoiceNo = null;
		if (latestInvoiceNo == null) {
			generatedInvoiceNo = organization.getOrganizationCode().concat("SR").concat("1");
		} else {
			latestInvoiceNo = latestInvoiceNo.substring(latestInvoiceNo.indexOf("SR") + 2, latestInvoiceNo.length());
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			Integer newInvoiceNo = ++invoiceNo;
			generatedInvoiceNo = organization.getOrganizationCode().concat("SR").concat(newInvoiceNo.toString());
		}
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Generated sales return invoiceNo: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo;
	}
	

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from
	 * DB on criteria.
	 * 
	 * @param salesReturnCommand - {@link SalesReturnCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturns(
			SalesReturnCommand salesReturnCommand, String userName,
			VbOrganization organization) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbSalesReturn.class).createAlias("vbSalesBook","vb");
		VbSalesBook salesBook=getSalesBookForSearch(session,organization, userName);
		if(salesBook != null){
			if(salesBook.getAllotmentType().equals(DAILY_SALES_EXECUTIVE)){
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
			}else{
				
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds=session.createCriteria(VbSalesBook.class)
						              .setProjection(Projections.property("id"))
						              .add(Restrictions.between("createdOn",createdDate, DateUtils.getEndTimeStamp(new Date())))
						              .list();
				criteria.add(Restrictions.in("vb.id",listIds));
			}
		}else{
			criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
		}
		if (organization != null) {
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
		if (salesReturnCommand != null) {
			if(! salesReturnCommand.getCreatedBy().isEmpty()){
				criteria.add(Restrictions.like("createdBy", salesReturnCommand.getCreatedBy(), MatchMode.START).ignoreCase());
			}
			if (!salesReturnCommand.getBusinessName().isEmpty()) {
				criteria.add(Restrictions.like("businessName", salesReturnCommand.getBusinessName(), MatchMode.START).ignoreCase());
			}
			if (!salesReturnCommand.getInvoiceName().isEmpty()) {
				criteria.add(Restrictions.like("invoiceName", salesReturnCommand.getInvoiceName(), MatchMode.START).ignoreCase());
			}
			if (salesReturnCommand.getCreatedOn() != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(salesReturnCommand.getCreatedOn()), DateUtils.getEndTimeStamp(salesReturnCommand.getCreatedOn())));
			}
		}
		criteria.addOrder(Order.asc("createdOn"));
		List<SalesReturnResult> salesResultList = new ArrayList<SalesReturnResult>();
		List<VbSalesReturn> criteriaList = criteria.list();
		for (VbSalesReturn salesReturn : criteriaList) {
			SalesReturnResult result = new SalesReturnResult();
			Float total = getSalesReturnProducts(salesReturn.getId(), organization, userName);
			result.setCreatedBy(salesReturn.getCreatedBy());
			result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
			result.setTotal(StringUtil.floatFormat(total));
			result.setId(salesReturn.getId());
			result.setBusinessName(salesReturn.getBusinessName());
			result.setStatus(salesReturn.getStatus());
			salesResultList.add(result);
		}
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("salesResultList: {}", salesResultList);
		}
		return salesResultList;

	}

	/**
	 * This method is responsible to retrieve sum of total cost of provided
	 * salesReturnId {@link VbSalesReturnProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @return sumTotal - {@link Float}
	 * 
	 */
	public Float getSalesReturnProducts(Integer salesReturnId,
			VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbSalesBook salesBook = getSalesBook(session, organization, userName);
		Float sumTotal = (float) 0;
		if (salesBook != null) {
			sumTotal = (Float) session.createCriteria(VbSalesReturnProducts.class)
					.createAlias("vbSalesReturn", "salesReturn")
					.setProjection(Projections.sum("totalCost"))
					.add(Expression.eq("salesReturn.id", salesReturnId))
					.add(Expression.eq("salesReturn.vbSalesBook", salesBook))
					.add(Expression.eq("salesReturn.vbOrganization", organization)).uniqueResult();
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}

	/**
	 * This method is responsible to retrieve all the {@link VbSalesReturn} from DB.
	 * 
	 * @param username - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnResult> getSalesReturnsOnLoad(String username,
			VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbSalesBook salesBook = getSalesBook(session, vbOrganization, username);
		List<VbSalesReturn> salesReturnsList = null;
		if (salesBook != null) {
			salesReturnsList = session.createCriteria(VbSalesReturn.class)
					.add(Expression.eq("createdBy", username))
					.add(Expression.eq("vbSalesBook", salesBook))
					.add(Expression.eq("vbOrganization", vbOrganization))
					.addOrder(Order.asc("createdOn")).list();
		}

		List<SalesReturnResult> salesResultList = null;
		if (salesReturnsList != null) {
			salesResultList = new ArrayList<SalesReturnResult>();
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
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("salesReturnsList: {}", salesReturnsList);
		}
		return salesResultList;

	}

	/**
	 * This method is responsible to retrieve sales returns based on return Id
	 * {@link VbSalesReturn} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return vbSalesReturn - {@link VbSalesReturn}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbSalesReturnProducts> getSalesReturnProductsDetails(Integer salesReturnId,
			VbOrganization organization) {
		Session session = this.getSession();
		VbSalesReturn vbSalesReturn=(VbSalesReturn) session.get(VbSalesReturn.class,salesReturnId);
		List<VbSalesReturnProducts> salesReturnProducts=session.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn","salesReturn")
				.add(Restrictions.eq("salesReturn.vbOrganization",organization))
				.add(Restrictions.eq("vbSalesReturn", vbSalesReturn))
				.addOrder(Order.asc("productName"))
				.addOrder(Order.asc("batchNumber"))
				.list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbSalesReturn: {}", salesReturnProducts);
		}
		return salesReturnProducts;

	}

	/**
	 * This method is used to get the invoiceName based on the businessName from
	 * VbCustomer table.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return invoiceName - {@link String}
	 */
	public String getInvoiceName(String businessName,
			VbOrganization organization) {
		Session session = this.getSession();
		String invoiceName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("invoiceName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (invoiceName == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for invoiceName: {}", invoiceName);
			}
			return null;
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("Invoice Name associated with businessName is: {}", invoiceName);
		}
		return invoiceName;
	}


	/**
	 * This method is responsible to provide the data for sales returns grid.
	 * 
	 * @param businessName - {@link String}
	 * @return salesReturnsResultList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SalesReturnsResult> getGridData(String businessName,
			VbOrganization organization, String userName) {
		Session session = this.getSession();
		SalesReturnsResult result = null;
		Float productCost;
		String productName = null;
		List<SalesReturnsResult> salesReturnsResultList = null;
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

			if (products != null) {
				salesReturnsResultList = new ArrayList<SalesReturnsResult>();
				for (VbDeliveryNoteProducts product : products) {
					result = new SalesReturnsResult();
					productName = product.getProductName();
					String batchNumber = product.getBatchNumber();
					result.setProductName(productName);

					productCost = (Float) session
							.createCriteria(VbProductCustomerCost.class)
							.createAlias("vbProduct", "product")
							.createAlias("vbCustomer", "customer")
							.setProjection(Projections.property("cost"))
					.add(Expression.eq("product.productName", productName))
					.add(Expression.eq("product.batchNumber", batchNumber))
					.add(Expression.eq("product.vbOrganization", organization))
					.add(Expression.eq("customer.businessName", businessName))
					.add(Expression.eq("customer.vbOrganization", organization))
					.uniqueResult();

					Float costperQuantity = (Float) session
							.createCriteria(VbProduct.class)
							.setProjection(Projections.property("costPerQuantity"))
							.add(Expression.eq("productName", productName))
							.add(Expression.eq("batchNumber", batchNumber))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();

					result.setBatchNumber(product.getBatchNumber());
					if (productCost == null || productCost == 0) {
						result.setCost(StringUtil.currencyFormat(costperQuantity));
					} else {
						result.setCost(StringUtil.currencyFormat(productCost));
					}
					result.setTotalCost("0.00");
					salesReturnsResultList.add(result);
				}
			}
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("salesReturnsResultList: {}", salesReturnsResultList);
		}
		return salesReturnsResultList;
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
	private VbSalesBook getSalesBook(Session session, VbOrganization organization, String userName) {
		Query query = session.createQuery(
						"FROM VbSalesBook vb WHERE  "
								+ "vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE " +
								"vb.vbOrganization = :vbOrganization AND vbs.salesExecutive = :salesExecutiveName)")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		
		return salesBook;
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
	 * 
	 */
	public void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance, String userName) {
		VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
		if (balance < 0) {
			advanceInfo.setAdvance(-(balance));
			advanceInfo.setBalance(-(balance));
		} else {
			advanceInfo.setAdvance(balance);
			advanceInfo.setBalance(balance);
		}
		advanceInfo.setBusinessName(businessName);
		advanceInfo.setCreatedBy(userName);
		Date date = new Date();
		advanceInfo.setCreatedOn(date);
		advanceInfo.setModifiedOn(date);
		advanceInfo.setCreditFrom(invoiceNo);
		advanceInfo.setVbOrganization(organization);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
		}
		session.save(advanceInfo);
	}

	/**This method is responsible for getting sales return details from Database.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return salesResultList - {@link List}
	 */
	@SuppressWarnings({"unchecked" })
	public List<SalesReturnResult> getSalesReturnsDashboard(VbOrganization vbOrganization) {
		Session session = this.getSession();
		
		 List<SalesReturnResult> salesResultList = null;
		 List<VbSalesReturn> salesReturnsList = null;
		salesReturnsList = session.createCriteria(VbSalesReturn.class)
					          .add(Restrictions.eq("vbOrganization", vbOrganization))
					          .add(Restrictions.eq("status", CRStatus.PENDING.name()))
					          .addOrder(Order.asc("createdOn")).list();
		if (salesReturnsList != null) {
			salesResultList = new ArrayList<SalesReturnResult>();
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
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("salesReturnsList: {}", salesReturnsList);
		}
		return salesResultList;
	}

	/**
	 * This method is responsible for fetching all sales return product list from db.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 */
	@SuppressWarnings("unchecked")
	public void approveOrDeclineSalesReturn(Integer salesReturnId, String status, VbOrganization vbOrganization, String userName) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbSalesReturn vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, salesReturnId);
		SalesReturnCommand salesReturnCommand=null;
		if(vbSalesReturn!=null){
			vbSalesReturn.setModifiedBy(userName);
			vbSalesReturn.setModifiedOn(new Date());
			
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				List<VbSalesReturnProducts> salesReturnProductList= session.createCriteria(VbSalesReturnProducts.class)
						.createAlias("vbSalesReturn", "salesReturn")
						.add(Restrictions.eq("vbSalesReturn", vbSalesReturn))
						.add(Restrictions.eq("salesReturn.vbOrganization", vbOrganization))
						.list();
				List<SalesReturnCommand> salesReturnCommandList = new ArrayList<SalesReturnCommand>();
				for(VbSalesReturnProducts vbSalesReturnProducts : salesReturnProductList){
				salesReturnCommand=new SalesReturnCommand();
				salesReturnCommand.setInvoiceNo(vbSalesReturn.getInvoiceNo());
				salesReturnCommand.setBusinessName(vbSalesReturn.getBusinessName());
				salesReturnCommand.setInvoiceName(vbSalesReturn.getInvoiceName());
				salesReturnCommand.setGrandTotalCost(vbSalesReturn.getProductsGrandTotal());
				salesReturnCommand.setProductName(vbSalesReturnProducts.getProductName());
				salesReturnCommand.setBatchNumber(vbSalesReturnProducts.getBatchNumber());
				salesReturnCommand.setResalable(vbSalesReturnProducts.getResalable());
				salesReturnCommandList.add(salesReturnCommand);
			    }
				//call updateProductsAndPreviousCredits to update return products and customer credit info.
				updateProductsAndPreviousCredits(session, salesReturnCommandList, vbOrganization, userName);
				
				vbSalesReturn.setStatus(CRStatus.APPROVED.name());
			} else {
				vbSalesReturn.setStatus(CRStatus.DECLINE.name());
			}
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating vbSalesReturn for approved.");
			}
			session.update(vbSalesReturn);
	    }
		txn.commit();
		session.close();
	}
	
	/**
	 * This method is responsible to get {@link VbEmployee} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session
	 * @param organization
	 * @param userName
	 * @return employee
	 * 
	 */
	public VbEmployee getSalesExecutiveFullName(String userName,VbOrganization organization) {
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("username", userName))
				.uniqueResult();
		session.close();
		
		return employee;
	}

	/**
	 * This method is responsible for getting the {@link Integer} based on the product name.
	 * 
	 * @param username - {@link String}
	 * @param productName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return - {@link Integer}
	 */
	public Integer getQtySold(String businessName, String productName,	String batchNumber, VbOrganization organization) {
		Session session = this.getSession();
		Integer qtySold = (Integer) session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.sum("productQty"))
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.uniqueResult();
		if(qtySold == null) {
			qtySold = new Integer(0);
		}
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} is the sold Quantity for product {} ",qtySold, productName);
		}
		return qtySold;
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
		Query query = session
				.createQuery(
						"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization AND (vb.createdOn,vb.cycleId) IN (SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName AND vbs.vbOrganization = :vbOrganization )")
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
		if(salesReturn != null) {
			createdBy = salesReturn.getCreatedBy();
		}
		
		return createdBy;
	}
}
