/**
 * com.vekomy.vbooks.mysales.dao.DayBookDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 22, 2013
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

import com.vekomy.vbooks.hibernate.model.VbAlertCategory;
import com.vekomy.vbooks.hibernate.model.VbAlertType;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbExcessCash;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.DayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.DayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.DayBookCommand;
import com.vekomy.vbooks.mysales.command.DayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * @author Sudhakar
 * 
 * 
 */
public class DayBookDao extends MySalesBaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DayBookDao.class);
	
	/**
	 * String variable holds DAILY_SALES_EXECUTIVE.
	 */
	private static final String DAILY_SALES_EXECUTIVE = "Daily";
	

	/**
	 * This method is responsible to store day book details into DB.
	 * 
	 * @param dayBookAllowancesCommand
	 * @param dayBookAmountCommand 
	 * @param expenses
	 * @param dayBookProductsCommand
	 * @param dayBookVehicleCommand 
	 * @param organization
	 * @param userName
	 * @return isSaved
	 * 
	 */
	public synchronized Boolean saveDayBook(DayBookBasicInfoCommand dayBookBasicInfoCommand,
			DayBookAllowancesCommand dayBookAllowancesCommand, DayBookAmountCommand dayBookAmountCommand,
			List<DayBookProductsCommand> dayBookProductsCommand, DayBookVehicleDetailsCommand dayBookVehicleCommand,
			VbOrganization organization, String userName, String isReturn) {
		Boolean isSaved = Boolean.FALSE;
		VbDayBookProducts dayBookProducts = null;
		VbSalesBook salesBook = null;
		VbProduct vbProduct = null;
		VbSalesBook vbSaBook1 = null;
		Date date = new Date();
		Session session = this.getSession();
		VbSalesBook vbSalesBook = getVbSalesBook(session, userName, organization);
		Transaction txn = session.beginTransaction();
		Boolean returnToFactory = Boolean.parseBoolean(isReturn);
		if(returnToFactory == null) {
			returnToFactory = Boolean.TRUE;
		}
		VbDayBook dayBook = new VbDayBook();
		if(vbSalesBook != null) {
			dayBook.setVbSalesBook(vbSalesBook);
		}
		if (dayBook != null) {
			dayBook.setCreatedBy(userName);
			dayBook.setCreatedOn(date);
			dayBook.setModifiedOn(date);
			dayBook.setSalesExecutive(userName);
			if(returnToFactory != null) {
				dayBook.setIsReturn(returnToFactory);
			} else {
				dayBook.setIsReturn(Boolean.FALSE);
			}
			
			dayBook.setVehicleNo(dayBookVehicleCommand.getVehicleNo());
			dayBook.setDriverName(dayBookVehicleCommand.getDriverName());
			dayBook.setEndingReading(dayBookVehicleCommand.getEndingReading());
			dayBook.setStartingReading(dayBookVehicleCommand.getStartingReading());
			dayBook.setRemarks(dayBookVehicleCommand.getRemarks());
			dayBook.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting dayBook: {}", dayBook);
			}
			session.save(dayBook);
			isSaved = Boolean.TRUE;
		}

		VbDayBookAmount dayBookAmount = new VbDayBookAmount();
		if (dayBookAmount != null) {
			dayBookAmount.setAmountToBank(dayBookAmountCommand.getAmountToBank());
			dayBookAmount.setReasonAmountToBank(dayBookAmountCommand.getReasonAmountToBank());
			dayBookAmount.setAmountToFactory(dayBookAmountCommand.getAmountToFactory());
			dayBookAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance());
			dayBookAmount.setCustomerTotalCredit(dayBookAmountCommand.getCustomerTotalCredit());
			dayBookAmount.setCustomerTotalPayable(dayBookAmountCommand.getCustomerTotalPayable());
			dayBookAmount.setCustomerTotalReceived(dayBookAmountCommand.getCustomerTotalReceived());
			
			dayBookAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances());
			dayBookAmount.setDealerPartyExpenses(dayBookAllowancesCommand.getDealerPartyExpenses());
			dayBookAmount.setDriverAllowances(dayBookAllowancesCommand.getDriverAllowances());
			dayBookAmount.setExecutiveAllowances(dayBookAllowancesCommand.getExecutiveAllowances());
			dayBookAmount.setMiscellaneousExpenses(dayBookAllowancesCommand.getMiscellaneousExpenses());
			dayBookAmount.setReasonMiscellaneousExpenses(dayBookAllowancesCommand.getReasonMiscellaneousExpenses());
			dayBookAmount.setMunicipalCityCouncil(dayBookAllowancesCommand.getMunicipalCityCouncil());
			dayBookAmount.setOffloadingLoadingCharges(dayBookAllowancesCommand.getOffloadingLoadingCharges());
			dayBookAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances());
			dayBookAmount.setVehicleFuelExpenses(dayBookAllowancesCommand.getVehicleFuelExpenses());
			dayBookAmount.setVehicleMeterReading(dayBookAllowancesCommand.getVehicleMeterReading());
			dayBookAmount.setVehicleMaintenanceExpenses(dayBookAllowancesCommand.getVehicleMaintenanceExpenses());
			dayBookAmount.setVbDayBook(dayBook);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting dayBookAmount: {}", dayBookAmount);
			}
			session.save(dayBookAmount);
			isSaved = Boolean.TRUE;
		}
		
		// Automatic allocation of products for non-daily sales executive. 
		if(!returnToFactory) {
			vbSaBook1 = new VbSalesBook();
			String createdBy = (String) session.createQuery("SELECT vb.createdBy FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 " +
					"AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN (SELECT MAX(vbs.createdOn) " +
					"FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2)")
					.setParameter("salesExecutive1", userName)
					.setParameter("vbOrganization1", organization)
					.setParameter("salesExecutive2", userName)
					.setParameter("vbOrganization2", organization)
					.uniqueResult();
			Integer cycleId = getCycleID(session, organization, userName);
			vbSaBook1.setCycleId(cycleId);
			vbSaBook1.setCreatedBy(createdBy);
			vbSaBook1.setCreatedOn(date);
			vbSaBook1.setModifiedOn(date);
			vbSaBook1.setAllotmentType("non-daily");
			vbSaBook1.setFlag(new Integer(1));
			vbSaBook1.setSalesExecutive(userName);
			vbSaBook1.setVbOrganization(organization);
			vbSaBook1.setAdvance(new Float(0));
			Float closingBalance = dayBookAmountCommand.getClosingBalance();
			vbSaBook1.setClosingBalance(closingBalance);
			vbSaBook1.setOpeningBalance(closingBalance);
			if(_logger.isDebugEnabled()){
				_logger.debug("Automatic allocation of salesBook: {}", vbSaBook1);
			}
			session.save(vbSaBook1);
		}
				
		if (dayBookProductsCommand != null) {
			String productName = null;
			String batchNumber = null;
			Integer qtyClosingBalance;
			Integer qtyOpeningBalance;
			VbSalesBookProducts salesBookProducts = null;
			for (DayBookProductsCommand products : dayBookProductsCommand) {
				dayBookProducts = new VbDayBookProducts();
				productName = products.getProductName();
				batchNumber = products.getBatchNumber();
				qtyClosingBalance = products.getClosingStock();
				qtyOpeningBalance = products.getOpeningStock();
				if (dayBookProducts != null) {
					dayBookProducts.setClosingStock(qtyClosingBalance);
					dayBookProducts.setOpeningStock(qtyOpeningBalance);
					dayBookProducts.setProductName(productName);
					dayBookProducts.setProductsToCustomer(products.getProductsToCustomer());
					dayBookProducts.setProductsToFactory(products.getProductsToFactory());
					dayBookProducts.setBatchNumber(batchNumber);
					dayBookProducts.setVbDayBook(dayBook);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting dayBookProducts: {}", dayBookProducts);
					}
					session.save(dayBookProducts);
					isSaved = Boolean.TRUE;
				}

				// Updating Sales book by the end of the day(Generating Day book).
				VbSalesBookProducts salesBookProduct = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
						.createAlias("vbSalesBook", "salesBook")
						.add(Expression.eq("vbSalesBook", vbSalesBook))
						.add(Expression.eq("salesBook.vbOrganization", organization))
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("salesBook.flag", new Integer(1)))
						.uniqueResult();
				if (salesBookProduct != null) {
					// Updating VbProduct
					vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
							.add(Expression.eq("productName", productName))
							.add(Expression.eq("batchNumber", batchNumber))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
					
					Integer qtySold = products.getProductsToCustomer();
					Integer qtyToFactory = products.getProductsToFactory();
					if(qtyToFactory == null) {
						qtyToFactory = 0;
					}
					if(vbProduct != null){
						vbProduct.setAvailableQuantity(vbProduct.getAvailableQuantity() + qtyToFactory);
						vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() - qtySold);
						vbProduct.setModifiedBy(userName);
						vbProduct.setModifiedOn(new Date());
					}
					if(_logger.isDebugEnabled()){
						_logger.debug("Updating VbProduct: {}", vbProduct);
					}
					
					salesBookProduct.setQtySold(qtySold);
					salesBookProduct.setQtyToFactory(qtyToFactory);
					salesBookProduct.setQtyClosingBalance(qtyClosingBalance);
					salesBook = salesBookProduct.getVbSalesBook();
					if(_logger.isDebugEnabled()){
						_logger.debug("Updating VbSalesBookProduct: {}", salesBookProduct);
					}
					session.update(salesBookProduct);
				}
				
				// Automatic allocation of products for non-daily sales executive. 
				if(!returnToFactory){
					salesBookProducts = new VbSalesBookProducts();
					salesBookProducts.setBatchNumber(batchNumber);
					salesBookProducts.setProductName(productName);
					salesBookProducts.setQtyAllotted(new Integer(0));
					salesBookProducts.setQtyClosingBalance(qtyClosingBalance);
					salesBookProducts.setQtyOpeningBalance(qtyOpeningBalance);
					salesBookProducts.setVbSalesBook(vbSaBook1);
					salesBookProducts.setRemarks("System allotment");
					
					if(_logger.isDebugEnabled()){
						_logger.debug("Automatic allocation of salesBookProduct: {}", salesBookProducts);
					}
					session.save(salesBookProducts);
				}
			}
			if (salesBook != null) {
				salesBook.setClosingBalance(dayBookAmountCommand.getClosingBalance());
				salesBook.setModifiedBy(userName);
				salesBook.setModifiedOn(date);
				salesBook.setFlag(new Integer(0));
				if(_logger.isDebugEnabled()){
					_logger.debug("Updating VbSalesBook: {}", salesBook);
				}
				session.update(salesBook);
			}
		} else {
			if (_logger.isErrorEnabled()) {
				_logger.error("Products not configured.");
			}
			Query query = session.createQuery("FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutive AND " +
					"vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization2)")
					.setParameter("vbOrganization1", organization)
					.setParameter("salesExecutive", userName)
					.setParameter("salesExecutiveName", userName)
					.setParameter("vbOrganization2", organization);
			salesBook = getSingleResultOrNull(query);
			
			if (salesBook != null) {
				salesBook.setClosingBalance(dayBookAmountCommand.getClosingBalance());
				salesBook.setFlag(new Integer(0));
				if(_logger.isDebugEnabled()){
					_logger.debug("Updating VbSalesBook: {}", salesBook);
				}
				session.update(salesBook);
			}
		}
		txn.commit();
		session.close();
		return isSaved;
	}

	/**
	 * This method is used to get the SUM of presentPayable, SUM of present
	 * Payment based on the sales executive from Delivery Note Payments.
	 * 
	 * @param salesExecutiveName
	 * @param organization
	 * @return DayBookResult
	 */
	public DayBookResult getCustomerTotalPayable(String salesExecutiveName , VbOrganization organization) {
		DayBookResult dayBookResult = new DayBookResult();
		Float customerTotalPayable = null;
		Float customerTotalPayment = null;
		Session session = this.getSession();
		VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutiveName, organization);
		if(vbSalesBook != null) {
			customerTotalPayable = (Float) session.createCriteria(VbDeliveryNotePayments.class)
					.createAlias("vbDeliveryNote", "deliveryNote")
					.setProjection(Projections.sum("presentPayable"))
					.add(Expression.eq("deliveryNote.createdBy", salesExecutiveName))
					.add(Expression.eq("deliveryNote.vbOrganization", organization))
					.add(Expression.eq("deliveryNote.vbSalesBook", vbSalesBook))
					.uniqueResult();
					
			dayBookResult.setPresentPayable(StringUtil.floatFormat(customerTotalPayable));
			
			customerTotalPayment = (Float) session.createCriteria(VbDeliveryNotePayments.class)
					.createAlias("vbDeliveryNote", "deliveryNote")
					.setProjection(Projections.sum("presentPayment"))
					.add(Expression.eq("deliveryNote.createdBy", salesExecutiveName))
					.add(Expression.eq("deliveryNote.vbOrganization", organization))
					.add(Expression.eq("deliveryNote.vbSalesBook", vbSalesBook))
					.uniqueResult();
			
			dayBookResult.setPresentPayment(StringUtil.floatFormat(customerTotalPayment));
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookResult List size is : {}", dayBookResult);
		}
		session.close();
		return dayBookResult;
	}

	/**
	 * This method is used to get the opening balance based on the sales
	 * executive.
	 * 
	 * @param salesExecutive
	 * @param organization
	 * @return Float
	 */
	public Float getOpeningBalance(String salesExecutive , VbOrganization organization) {
		Float openingBal = null;
		Session session = this.getSession();
		Query query = session.createQuery("SELECT vb.openingBalance FROM VbSalesBook as vb " +
						"WHERE vb.salesExecutive = :salesExecutive1 AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN " +
						"(SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb WHERE vbsb.salesExecutive = :salesExecutive2 AND vbsb.vbOrganization = :vbOrganization2)");
		query.setParameter("salesExecutive1", salesExecutive);
		query.setParameter("vbOrganization1", organization);
		query.setParameter("salesExecutive2", salesExecutive);
		query.setParameter("vbOrganization2", organization);
		openingBal = getSingleResultOrNull(query);
		if(openingBal == null) {
			   openingBal = new Float("0.00");
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("Opening Balance : {}", openingBal);
		}
		session.close();
		return openingBal;
	}

	/**
	 * This method is responsible to prepare {@link DayBookResult} list.
	 * 
	 * @param dayBookList - {@link List}
	 * @return - {@link List}
	 * 
	 */
	private List<DayBookResult> prepareResultList(List<VbDayBook> dayBookList){
		List<DayBookResult> dayBookResultList = new ArrayList<DayBookResult>();
		DayBookResult dayBook = null;
		for(VbDayBook result:dayBookList){
			dayBook=new DayBookResult();
			dayBook.setId(result.getId());
			dayBook.setCreatedOn(result.getCreatedOn());
			dayBook.setSalesExecutive(result.getSalesExecutive());
			dayBookResultList.add(dayBook);
		}
		return dayBookResultList;
	}
	/**
	 * This method is used to get all the day books on criteria .
	 * 
	 * @param dayBookCommand
	 * @param vbOrganization
	 * @return resultList
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookResult> getDayBook(DayBookCommand dayBookCommand, VbOrganization vbOrganization, String userName) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookCommand: {}", dayBookCommand);
		}
		List<VbDayBook> vbDayBook = new ArrayList<VbDayBook>();
		List<DayBookResult> resultList = new ArrayList<DayBookResult>();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbDayBook.class).createAlias("vbSalesBook","vb");
		VbSalesBook salesBook=getSalesBook(session, vbOrganization, userName);
		if(salesBook != null){
			if(salesBook.getAllotmentType().equals(DAILY_SALES_EXECUTIVE)){
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
			}
			else{
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds = session.createCriteria(VbSalesBook.class)
						.setProjection(Projections.property("id"))
						.add(Restrictions.eq("flag", new Integer(0)))
						.add(Restrictions.eq("allotmentType","non-daily"))
						.add(Restrictions.between("createdOn",createdDate,DateUtils.getEndTimeStamp(new Date())))
						.list();
				if(listIds.size()>0){
					criteria.add(Expression.in("vb.id",listIds));
				}
			}
		}else{
			criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date())));
		}
		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		if (dayBookCommand != null) {
			if (!dayBookCommand.getSalesExecutive().isEmpty()) {
				criteria.add(Expression.eq("salesExecutive", dayBookCommand.getSalesExecutive()));
			}
			if (dayBookCommand.getCreatedOn() != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(dayBookCommand.getCreatedOn()), DateUtils.getEndTimeStamp(dayBookCommand.getCreatedOn())));
			}
		}
		criteria.addOrder(Order.asc("createdOn"));
		vbDayBook = criteria.list();
		session.close();
		resultList=prepareResultList(vbDayBook);
		return resultList;
	}


	/**
	 * This method is used to get the grid data into day book page based on the salesExecutive and the organization.
	 * 
	 * @param salesExecutive
	 * @param organization
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookResult> getGridData(String salesExecutive , VbOrganization organization) {
		List<DayBookResult> dayBookResultList = null;
		Session session = this.getSession();
		List<VbSalesBookProducts> productList = null;
		VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
		if(vbSalesBook != null) {
			productList = session.createQuery("FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.salesExecutive = :salesExecutive AND " +
					"vb.vbSalesBook.vbOrganization = :vbOrganization AND vb.vbSalesBook = :vbSalesBook GROUP BY vb.productName, vb.batchNumber")
					.setParameter("salesExecutive", salesExecutive)
					.setParameter("vbOrganization", organization)
					.setParameter("vbSalesBook", vbSalesBook)
					.list();
		}
		if (productList != null) {
			DayBookResult dayBookResult = null;
			Integer productQty = 0;
			Integer returnQty = null;
			dayBookResultList = new ArrayList<DayBookResult>();
			for (VbSalesBookProducts vbSalesBookProduct : productList) {
				dayBookResult = new DayBookResult();
				String productName = vbSalesBookProduct.getProductName();
				String batchNumber = vbSalesBookProduct.getBatchNumber();
				Query productQtyQuery = session.createQuery(
								"SELECT SUM(vb.productQty) + SUM(vb.bonusQty) FROM VbDeliveryNoteProducts vb WHERE vb.productName = :productName AND vb.batchNumber = :batchNumber" +
								" AND vb.vbDeliveryNote.vbOrganization = :vbOrganization AND vb.vbDeliveryNote.vbSalesBook = :vbSalesBook GROUP BY vb.vbDeliveryNote.createdBy")
						.setParameter("productName", productName)
						.setParameter("batchNumber", batchNumber)
						.setParameter("vbOrganization", organization)
						.setParameter("vbSalesBook", vbSalesBook);
				
				productQty = getSingleResultOrNull(productQtyQuery);
				Integer openingStock = vbSalesBookProduct.getQtyClosingBalance() + vbSalesBookProduct.getQtyAllotted();
				if(productQty != null) {
					dayBookResult.setProductsToCustomer(productQty);
				} else {
					dayBookResult.setProductsToCustomer(new Integer(0));
				}
				
				returnQty = (Integer) session.createQuery("SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb " +
				         "WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND " +
				         "vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName AND vb.batchNumber = :batchNumber GROUP BY vb.productName, vb.batchNumber")
				         .setParameter("vbOrganization", organization)
				         .setParameter("vbSalesBook", vbSalesBook)
				         .setParameter("productName", productName)
				         .setParameter("batchNumber", batchNumber)
				         .uniqueResult();
				
				if(returnQty != null) {
					dayBookResult.setReturnQty(returnQty);
				} else {
					dayBookResult.setReturnQty(new Integer(0));
				}
				dayBookResult.setProductName(productName);
				dayBookResult.setBatchNumber(batchNumber);
				dayBookResult.setOpeningStock(openingStock);
				dayBookResult.setClosingStock(openingStock - dayBookResult.getProductsToCustomer());
				dayBookResultList.add(dayBookResult);
			}
		} else {
			_logger.error("No records found for result");
		}

		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookResultList: {}", dayBookResultList);
		}

		return dayBookResultList;
	}

	/**
	 * This method is used to get day books on particular id .
	 * 
	 * @param id
	 * @param organization
	 * @return vbDayBook
	 */
	@SuppressWarnings("unchecked")
	public List<VbDayBookProducts> getDayBookOnId(Integer id , VbOrganization organization) {
		Session session = this.getSession();
		VbDayBook vbDayBook = (VbDayBook) session.get(VbDayBook.class, id);
		List<VbDayBookProducts> list= session.createCriteria(VbDayBookProducts.class)
				.createAlias("vbDayBook","dayBook")
				.add(Restrictions.eq("dayBook.vbOrganization", organization))
				.add(Restrictions.eq("vbDayBook",vbDayBook))
				.addOrder(Order.asc("productName"))
				.addOrder(Order.asc("batchNumber"))
				.list();
		session.close();
		return list;

	}
	/**
	 * This method is used to get the salesbook instance.
	 * 
	 * @param session
	 * @param userName
	 * @param organization
	 * @return VbSalesBook
	 */
	private VbSalesBook getVbSalesBook(Session session, String userName, VbOrganization organization) {
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("salesExecutive", userName))
				.add(Expression.eq("flag", new Integer(1)))
				.uniqueResult();
		return vbSalesBook;
	}
	@SuppressWarnings("unchecked")
	public List<String> getAllSalesExecutives(VbOrganization organization,String executiveName) {
		Session session = this.getSession();
		List<String> salesExecutives = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.like("username", executiveName, MatchMode.START))
				.addOrder(Order.asc("username"))
				.list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Employees: {}", salesExecutives);
		}
		return salesExecutives;
	}
	/**
	 * This method is responsible to get {@link VbEmployee} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return employee - {@link VbEmployee}
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
	 * This method is responsible to get allotment type from {@link VbSalesBook}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return flag - {@link Boolean}
	 * 
	 */
	public Boolean getAllotmentType(String userName, VbOrganization organization) {
		Boolean flag = Boolean.FALSE;
		Session session = this.getSession();
		String allotmentType = (String) session.createQuery("SELECT vb.allotmentType FROM VbSalesBook vb WHERE vb.createdOn IN" +
				"(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive AND vbs.vbOrganization = :vbOrganization)")
				.setParameter("salesExecutive", userName)
				.setParameter("vbOrganization", organization)
				.uniqueResult();
		
		if(DAILY_SALES_EXECUTIVE.equalsIgnoreCase(allotmentType)) {
			flag = Boolean.TRUE;
		} 
		session.close();
		return flag;
	}
	
	/**
	 * This method is used to check the day book is closed or not on particular date for particular sales executive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isDayBookClosed - {@link Boolean}
	 */
	public Boolean isDayBookClosed(String salesExecutive, VbOrganization organization) {
		Boolean isDayBookClosed = Boolean.FALSE;
		Session session = this.getSession();
		Date currentDate = new Date();
		VbDayBook vbDayBook = (VbDayBook) session.createCriteria(VbDayBook.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(currentDate), DateUtils.getEndTimeStamp(currentDate)))
				.uniqueResult();
		if(vbDayBook != null) {
			isDayBookClosed = Boolean.TRUE;
		}
		session.close();
		return isDayBookClosed;
	}

	/**
	 * This method is responsible to get the advance value from {@link VbSalesBook}.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return advance - {@link Float}
	 */
	public Long getAllotStockOpeningBalance(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		Long openingBal;
		Float openingBalance = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("openingBalance"))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		if(openingBalance == null) {
			openingBal = new Long("0.00");
		} else {
			openingBal = openingBalance.longValue();
		}
		session.close();
		return openingBal;
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
		Query query = session
				.createQuery(
						"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization AND (vb.createdOn,vb.cycleId) IN (SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE" +
						" vbs.salesExecutive = :salesExecutiveName AND vbs.vbOrganization = :vbOrganization )")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}
	
	/**
	 * This method is responsible to get createdBy from {@link VbSalesBook}.
	 * 
	 * @param salesExecutive - {@link String} 
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getcreatedBy(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		String createdBy = (String) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("createdBy"))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{}'s allotment have been done by {}", salesExecutive, createdBy);
		}
		return createdBy;
	}
	
	public Float getAdvance(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		Float advance = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("advance"))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		session.close();
		
		if(advance == null) {
			advance = new Float(0);
		}
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} is the advance of {}", advance, salesExecutive);
		}
		return advance;
		
	}
	
	/**
	 * This method is responsible to get configured excess amount from {@link VbExcessCash}.
	 * 
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return excessAmount - {@link Float}
	 * 
	 */
	public Float getExcessAmount(String alertType, VbOrganization organization) {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = (VbAlertCategory) session.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory", OrganizationUtils.USER_DEFINED_ALERT))
				.uniqueResult();
		Float excessAmount = null;
		if(vbAlertCategory != null) {
			VbAlertType vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
					.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
					.add(Restrictions.eq("alertType", alertType))
					.uniqueResult();
			if(vbAlertType != null) {
				VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.createCriteria(VbUserDefinedAlerts.class)
						.add(Restrictions.eq("vbAlertType", vbAlertType))
						.add(Restrictions.eq("vbOrganization", organization))
						.add(Restrictions.eq("activeInactive", Boolean.TRUE))
						.uniqueResult();
				if(vbUserDefinedAlerts != null) {
					excessAmount = (Float) session.createCriteria(VbExcessCash.class)
							.setProjection(Projections.property("amount"))
							.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
							.uniqueResult();
				}
			}
		}
		session.close();
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("");
		}
		return excessAmount;
	}
}
