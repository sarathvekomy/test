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

import java.text.ParseException;
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

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.exception.ProcessingException;
import com.vekomy.vbooks.hibernate.model.VbAlertCategory;
import com.vekomy.vbooks.hibernate.model.VbAlertType;
import com.vekomy.vbooks.hibernate.model.VbCashDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbExcessCash;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesResult;
import com.vekomy.vbooks.mysales.command.DayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.DayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.DayBookCommand;
import com.vekomy.vbooks.mysales.command.DayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.command.DayBookViewResult;
import com.vekomy.vbooks.mysales.command.TempDayBookCommand;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * @author Sudhakar
 * 
 */
public class DayBookDao extends MySalesBaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DayBookDao.class);

	/**
	 * This method is responsible to store day book details into DB.
	 * 
	 * @param dayBookAllowancesCommand - {@link DayBookAllowancesCommand}
	 * @param dayBookAmountCommand - {@link DayBookAmountCommand}
	 * @param expenses - {@link String}
	 * @param dayBookProductsCommand - {@link DayBookProductsCommand}
	 * @param dayBookVehicleCommand - {@link DayBookVehicleDetailsCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isSaved - {@link Boolean}
	 * @throws ProcessingException - {@link ProcessingException}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public synchronized void saveDayBook(DayBookBasicInfoCommand dayBookBasicInfoCommand,
			DayBookAllowancesCommand dayBookAllowancesCommand,
			DayBookAmountCommand dayBookAmountCommand,
			List<DayBookProductsCommand> dayBookProductsCommand,
			DayBookVehicleDetailsCommand dayBookVehicleCommand,
			VbOrganization organization, String userName, String isReturn,
			String dayBookNo) throws ProcessingException, DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbDayBookProducts dayBookProducts = null;
			VbSalesBook salesBook = null;
			VbProduct vbProduct = null;
			VbSalesBook systemAllotmentSalesBook = null;
			Date date = new Date();
			VbSalesBook vbSalesBook = getVbSalesBook(session, userName, organization);
			Boolean returnToFactory = Boolean.parseBoolean(isReturn);
			if (returnToFactory == null) {
				returnToFactory = Boolean.TRUE;
			}
			
			// DayBook Basic Info.
			VbDayBook dayBook = new VbDayBook();
			if (vbSalesBook != null) {
				dayBook.setVbSalesBook(vbSalesBook);
			}
			dayBook.setCreatedBy(userName);
			dayBook.setCreatedOn(date);
			dayBook.setModifiedOn(date);
			dayBook.setSalesExecutive(userName);
			if (returnToFactory != null) {
				dayBook.setIsReturn(returnToFactory);
			} else {
				dayBook.setIsReturn(Boolean.FALSE);
			}
			dayBook.setReportingManager(dayBookBasicInfoCommand.getReportingManagerName());
			dayBook.setVehicleNo(dayBookVehicleCommand.getVehicleNo());
			dayBook.setDriverName(dayBookVehicleCommand.getDriverName());
			dayBook.setEndingReading(dayBookVehicleCommand.getEndingReading());
			dayBook.setStartingReading(dayBookVehicleCommand.getStartingReading());
			dayBook.setDayBookRemarks(dayBookBasicInfoCommand.getDayBookRemarks());
			dayBook.setVehicleDetailsRemarks(dayBookVehicleCommand.getRemarks());
			dayBook.setDayBookNo(dayBookNo);
			dayBook.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting dayBook");
			}
			session.save(dayBook);
			
			// For Day Book Amounts.
			VbDayBookAmount dayBookAmount = new VbDayBookAmount();
			dayBookAmount.setAmountToBank(dayBookAmountCommand.getAmountToBank());
			dayBookAmount.setAmountToFactory(dayBookAmountCommand.getAmountToFactory());
			dayBookAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance());
			dayBookAmount.setCustomerTotalCredit(dayBookAmountCommand.getCustomerTotalCredit());
			dayBookAmount.setCustomerTotalPayable(dayBookAmountCommand.getCustomerTotalPayable());
			dayBookAmount.setCustomerTotalReceived(dayBookAmountCommand.getCustomerTotalReceived());
			dayBookAmount.setAmountsRemarks(dayBookAmountCommand.getAmountsRemarks());
			dayBookAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances());
			dayBookAmount.setDealerPartyExpenses(dayBookAllowancesCommand.getDealerPartyExpenses());
			dayBookAmount.setReasonDealerPartyExpenses(dayBookAllowancesCommand.getReasonDealerPartyExpenses());
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
			dayBookAmount.setAllowancesRemarks(dayBookAllowancesCommand.getAllowancesRemarks());
			dayBookAmount.setVbDayBook(dayBook);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting dayBookAmount: {}", dayBookAmount);
			}
			session.save(dayBookAmount);

			// Automatic allocation of products for non-daily sales executive.
			if (!returnToFactory) {
				systemAllotmentSalesBook = new VbSalesBook();
				String createdBy = (String) session.createQuery(
								"SELECT vb.createdBy FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 "
								+ "AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN (SELECT MAX(vbs.createdOn) "
								+ "FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2)")
						.setParameter("salesExecutive1", userName)
						.setParameter("vbOrganization1", organization)
						.setParameter("salesExecutive2", userName)
						.setParameter("vbOrganization2", organization)
						.uniqueResult();
				
				Integer cycleId = getCycleID(session, organization, userName);
				systemAllotmentSalesBook.setCycleId(cycleId);
				systemAllotmentSalesBook.setCreatedBy(createdBy);
				systemAllotmentSalesBook.setCreatedOn(date);
				systemAllotmentSalesBook.setModifiedOn(date);
				systemAllotmentSalesBook.setAllotmentType(OrganizationUtils.ALLOTMENT_TYPE_NON_DAILY);
				systemAllotmentSalesBook.setFlag(new Integer(1));
				systemAllotmentSalesBook.setSalesExecutive(userName);
				systemAllotmentSalesBook.setVbOrganization(organization);
				systemAllotmentSalesBook.setAdvance(new Float(0));
				systemAllotmentSalesBook.setSalesBookNo(generateSalesBookNo(organization));
				Float closingBalance = dayBookAmountCommand.getClosingBalance();
				systemAllotmentSalesBook.setClosingBalance(closingBalance);
				systemAllotmentSalesBook.setOpeningBalance(closingBalance);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Automatic allocation of salesBook");
				}
				session.save(systemAllotmentSalesBook);
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
						dayBookProducts.setProductsRemarks(products.getProductsRemarks());
						dayBookProducts.setVbDayBook(dayBook);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting dayBookProducts");
						}
						session.save(dayBookProducts);
					}

					// Updating Sales book by the end of the day(Generating Day Book).
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
						Integer qtySold = products.getProductsToCustomer();
						Integer qtyToFactory = products.getProductsToFactory();
						if (qtyToFactory == null) {
							qtyToFactory = 0;
						}
						vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
								.add(Expression.eq("productName", productName))
								.add(Expression.eq("batchNumber", batchNumber))
								.add(Expression.eq("vbOrganization", organization))
								.uniqueResult();
						
						if (vbProduct != null) {
							vbProduct.setAvailableQuantity(vbProduct.getAvailableQuantity() + qtyToFactory);
							vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() - qtySold);
							vbProduct.setState(vbProduct.getState());
							vbProduct.setModifiedBy(userName);
							vbProduct.setModifiedOn(new Date());
							
							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbProduct");
							}
							session.update(vbProduct);
						}

						salesBookProduct.setQtySold(qtySold);
						salesBookProduct.setQtyToFactory(qtyToFactory);
						salesBookProduct.setQtyClosingBalance(qtyClosingBalance);
						salesBook = salesBookProduct.getVbSalesBook();
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbSalesBookProduct");
						}
						session.update(salesBookProduct);
					}

					// Automatic allocation of products for non-daily sales executive.
					if (!returnToFactory) {
						salesBookProducts = new VbSalesBookProducts();
						salesBookProducts.setBatchNumber(batchNumber);
						salesBookProducts.setProductName(productName);
						salesBookProducts.setQtyAllotted(new Integer(0));
						salesBookProducts.setQtyClosingBalance(qtyClosingBalance);
						salesBookProducts.setQtyOpeningBalance(qtyOpeningBalance);
						salesBookProducts.setVbSalesBook(systemAllotmentSalesBook);
						salesBookProducts.setRemarks(OrganizationUtils.SYSTEM_ALLOTMENT);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Automatic allocation of salesBookProduct");
						}
						session.save(salesBookProducts);

						// For Alerts
						AlertManager.getInstance().fireSystemAlert(organization, systemAllotmentSalesBook.getCreatedBy(), userName,
										OrganizationUtils.ALERT_TYPE_STOCK_ALLOTMENT, Msg.get(MsgEnum.ALERT_TYPE_STOCK_ALLOTMENT_MESSAGE));
						_logger.info("Firing alert for allotment.");
					}
				}
				if (salesBook != null) {
					salesBook.setClosingBalance(dayBookAmountCommand.getClosingBalance());
					salesBook.setModifiedBy(userName);
					salesBook.setModifiedOn(date);
					salesBook.setFlag(new Integer(0));
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbSalesBook");
					}
					session.update(salesBook);
				}
			} else {
				Query query = session.createQuery(
								"FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutive AND "
								+ "vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName "
								+ "AND vb.vbOrganization = :vbOrganization2)")
						.setParameter("vbOrganization1", organization)
						.setParameter("salesExecutive", userName)
						.setParameter("salesExecutiveName", userName)
						.setParameter("vbOrganization2", organization);
				salesBook = getSingleResultOrNull(query);

				if (salesBook != null) {
					salesBook.setClosingBalance(dayBookAmountCommand.getClosingBalance());
					salesBook.setFlag(new Integer(0));
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbSalesBook");
					}
					session.update(salesBook);
				}
			}
			txn.commit();
		} catch (HibernateException exception) {
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
	 * This method is used to get the SUM of presentPayable, SUM of present
	 * Payment based on the sales executive from Delivery Note Payments.
	 * 
	 * @param salesExecutiveName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return DayBookResult - {@link DayBookResult}
	 */
	public DayBookResult getCustomerTotalPayable(String salesExecutiveName,
			VbOrganization organization) {
		DayBookResult dayBookResult = new DayBookResult();
		Float customerTotalPayable = null;
		Float customerTotalPayment = null;
		Session session = this.getSession();
		VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutiveName, organization);
		if (vbSalesBook != null) {
			customerTotalPayable = (Float) session.createCriteria(VbDeliveryNotePayments.class)
					.createAlias("vbDeliveryNote", "deliveryNote")
					.setProjection(Projections.sum("presentPayable"))
					.add(Expression.eq("deliveryNote.createdBy", salesExecutiveName))
					.add(Expression.eq("deliveryNote.vbOrganization", organization))
					.add(Expression.eq("deliveryNote.vbSalesBook", vbSalesBook))
					.add(Expression.eq("deliveryNote.flag", new Integer(1)))
					.uniqueResult();
			dayBookResult.setPresentPayable(StringUtil.floatFormat(customerTotalPayable));
			
			customerTotalPayment = (Float) session.createCriteria(VbDeliveryNotePayments.class)
					.createAlias("vbDeliveryNote", "deliveryNote")
					.setProjection(Projections.sum("presentPayment"))
					.add(Expression.eq("deliveryNote.createdBy", salesExecutiveName))
					.add(Expression.eq("deliveryNote.vbOrganization", organization))
					.add(Expression.eq("deliveryNote.vbSalesBook", vbSalesBook))
					.add(Expression.eq("deliveryNote.flag", new Integer(1)))
					.uniqueResult();

			dayBookResult.setPresentPayment(StringUtil.floatFormat(customerTotalPayment));
		}
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookResult: {}", dayBookResult);
		}
		return dayBookResult;
	}

	/**
	 * This method is used to get the opening balance based on the sales
	 * executive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return openingBal - {@link Float}
	 */
	public Float getOpeningBalance(String salesExecutive,
			VbOrganization organization) {
		Float openingBal = null;
		Session session = this.getSession();
		Query query = session.createQuery("SELECT vb.openingBalance FROM VbSalesBook as vb "
						+ "WHERE vb.salesExecutive = :salesExecutive1 AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN "
						+ "(SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb WHERE vbsb.salesExecutive = :salesExecutive2 AND " +
						"vbsb.vbOrganization = :vbOrganization2)")
			.setParameter("salesExecutive1", salesExecutive)
			.setParameter("vbOrganization1", organization)
			.setParameter("salesExecutive2", salesExecutive)
			.setParameter("vbOrganization2", organization);
		openingBal = getSingleResultOrNull(query);
		session.close();
		
		if (openingBal == null) {
			openingBal = new Float("0.00");
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Opening Balance : {}", openingBal);
		}
		return openingBal;
	}

	/**
	 * This method is responsible to prepare {@link DayBookResult} list.
	 * 
	 * @param dayBookList - {@link List}
	 * @return dayBookResultList - {@link List}
	 * 
	 */
	private List<DayBookResult> prepareResultList(List<VbDayBook> dayBookList) {
		List<DayBookResult> dayBookResultList = null;
		if(!dayBookList.isEmpty()) {
			DayBookResult dayBook = null;
			dayBookResultList = new ArrayList<DayBookResult>();
			for (VbDayBook result : dayBookList) {
				dayBook = new DayBookResult();
				dayBook.setId(result.getId());
				dayBook.setCreatedOn(result.getCreatedOn());
				dayBook.setSalesExecutive(result.getSalesExecutive());
				dayBook.setInvoiceNo(result.getDayBookNo());
				dayBookResultList.add(dayBook);
			}
		}
		return dayBookResultList;
	}

	/**
	 * This method is used to get all the day books on criteria .
	 * 
	 * @param dayBookCommand - {@link DayBookCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return resultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookResult> getDayBook(DayBookCommand dayBookCommand,
			VbOrganization vbOrganization, String userName) throws DataAccessException {
		Integer grantedDays = null;
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbDayBook.class).createAlias("vbSalesBook", "vb");
		// for display of search txn of SE get granted_days from vb_employee table for specific SLE.
		grantedDays = (Integer) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("grantedDays"))
				.add(Expression.eq("username", userName))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (grantedDays != null) {
			grantedDays = new Integer(0);
		} 
		VbSalesBook salesBook = getSalesBook(session, vbOrganization, userName);
		if (salesBook != null) {
			if (salesBook.getAllotmentType().equals(OrganizationUtils.ALLOTMENT_TYPE_DAILY)) {
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date(), grantedDays)));
			} else {
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds = session.createCriteria(VbSalesBook.class)
						.setProjection(Projections.property("id"))
						.add(Restrictions.eq("flag", new Integer(0)))
						.add(Restrictions.eq("allotmentType", OrganizationUtils.ALLOTMENT_TYPE_NON_DAILY))
						.add(Restrictions.between("createdOn", createdDate, DateUtils.getEndTimeStamp(new Date()))).list();
				if (listIds.size() > 0) {
					criteria.add(Expression.in("vb.id", listIds));
				}
			}
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
		criteria.addOrder(Order.desc("createdOn"));
		List<DayBookResult> resultList = prepareResultList(criteria.list());
		session.close();
		
		if(resultList != null && !resultList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", resultList.size());
			}
			return resultList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is used to get the grid data into day book page based on the
	 * salesExecutive and the organization.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return dayBookResultList - {@link DayBookResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookResult> getGridData(String salesExecutive,
			VbOrganization organization) throws DataAccessException {
		List<DayBookResult> dayBookResultList = null;
		Session session = this.getSession();
		VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
		List<VbSalesBookProducts> productList = session.createQuery(
				"FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.salesExecutive = :salesExecutive AND " +
				"vb.vbSalesBook.vbOrganization = :vbOrganization AND vb.vbSalesBook = :vbSalesBook GROUP BY vb.productName, vb.batchNumber")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("vbSalesBook", vbSalesBook)
				.list();
		
		if (!productList.isEmpty()) {
			DayBookResult dayBookResult = null;
			Query productQtyQuery = null;
			Integer productQty = null;
			Integer returnQty = null;
			String productName = null;
			String batchNumber = null;
			Integer openingStock = null;
			dayBookResultList = new ArrayList<DayBookResult>();
			for (VbSalesBookProducts vbSalesBookProduct : productList) {
				dayBookResult = new DayBookResult();
				productName = vbSalesBookProduct.getProductName();
				batchNumber = vbSalesBookProduct.getBatchNumber();
				productQtyQuery = session.createQuery(
								"SELECT SUM(vb.productQty) + SUM(vb.bonusQty) FROM VbDeliveryNoteProducts vb WHERE " +
								"vb.productName = :productName AND vb.batchNumber = :batchNumber"
								+ " AND vb.vbDeliveryNote.vbOrganization = :vbOrganization AND " +
								"vb.vbDeliveryNote.vbSalesBook = :vbSalesBook AND vb.vbDeliveryNote.flag = :flag  " +
								"GROUP BY vb.vbDeliveryNote.createdBy")
						.setParameter("productName", productName)
						.setParameter("batchNumber", batchNumber)
						.setParameter("vbOrganization", organization)
						.setParameter("vbSalesBook", vbSalesBook)
						.setParameter("flag", new Integer(1));

				productQty = getSingleResultOrNull(productQtyQuery);
				openingStock = vbSalesBookProduct.getQtyClosingBalance() + vbSalesBookProduct.getQtyAllotted();
				if (productQty != null) {
					dayBookResult.setProductsToCustomer(productQty);
				} else {
					dayBookResult.setProductsToCustomer(new Integer(0));
				}

				returnQty = (Integer) session.createQuery(
								"SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb "
								+ "WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND "
								+ "vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName " +
								"AND vb.batchNumber = :batchNumber AND vb.vbSalesReturn.flag = :flag GROUP BY vb.productName, vb.batchNumber")
						.setParameter("vbOrganization", organization)
						.setParameter("vbSalesBook", vbSalesBook)
						.setParameter("productName", productName)
						.setParameter("batchNumber", batchNumber)
						.setParameter("flag", new Integer(1)).uniqueResult();
				if (returnQty != null) {
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
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", dayBookResultList.size());
			}
			return dayBookResultList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is used to get day books on particular id for view.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return resultList - {@link DayBookViewResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookViewResult> getDayBookOnId(Integer id,
			VbOrganization organization, String userName) throws DataAccessException {
		List<VbDayBookProducts> productList = null;
		List<VbDayBookAmount> amountList = null;
		List<DayBookViewResult> resultList = null;
		VbDayBookAmount amount = null;
		Session session = this.getSession();
		VbDayBook vbDayBook = (VbDayBook) session.get(VbDayBook.class, id);
		if (vbDayBook != null) {
			List<String> amountsList = (List<String>) session.createCriteria(VbCashDayBook.class)
					.add(Restrictions.eq("vbSalesBook", vbDayBook.getVbSalesBook()))
					.add(Restrictions.eq("vbOrganization", organization))
					.setProjection(Projections.property("valueOne"))
					.add(Restrictions.eq("dayBookType", OrganizationUtils.DAY_BOOK_AMOUNT))
					.list();
			
			List<VbSalesReturn> listSalesReturn = new ArrayList<VbSalesReturn>(vbDayBook.getVbSalesBook().getVbSalesReturns());
			List<Object[]> objList = null;
			if (!listSalesReturn.isEmpty()) {
				objList = session.createCriteria(VbSalesReturnProducts.class)
						.setProjection(Projections.projectionList()
								.add(Projections.property("productName"))
								.add(Projections.property("batchNumber"))
								.add(Projections.property("totalQty")))
						.add(Restrictions.in("vbSalesReturn", listSalesReturn))
						.list();
			}
			String salesExecutiveStartDate=getSalesExecutiveStartDate(vbDayBook.getSalesExecutive(), organization);
			Float startingReading = vbDayBook.getStartingReading();
			Float endingReading = vbDayBook.getEndingReading();
			String vehicleNo = vbDayBook.getVehicleNo();
			String remarks = vbDayBook.getDayBookRemarks();
			String driverName = vbDayBook.getDriverName();
			Date createdDate = vbDayBook.getCreatedOn();
			String fullName = getEmployeeFullName(vbDayBook.getSalesExecutive(), organization);
			productList = new ArrayList<VbDayBookProducts>(vbDayBook.getVbDayBookProductses());
			amountList = new ArrayList<VbDayBookAmount>(vbDayBook.getVbDayBookAmounts());
			amount = amountList.get(0);
			resultList = new ArrayList<DayBookViewResult>();
			if (!productList.isEmpty()) {
				Integer returnQty = 0;
				String productName = null;
				String batchNumber = null;
				String salesReturnProductName = null;
				String salesReturnBatchNumber = null;
				Integer salesReturnTotaQty = null;
				for (VbDayBookProducts product : productList) {
					DayBookViewResult result = new DayBookViewResult();
					productName = product.getProductName();
					batchNumber = product.getBatchNumber();
					if (objList != null) {
						for (Object[] objects : objList) {
							salesReturnProductName = (String) objects[0];
							salesReturnBatchNumber = (String) objects[1];
							salesReturnTotaQty = (Integer) objects[2];
							if (productName.equals(salesReturnProductName) && batchNumber.equals(salesReturnBatchNumber)) {
								returnQty = salesReturnTotaQty;
								break;
							}
						}
					}
					result.setStartDate(salesExecutiveStartDate);
					result.setProduct(product.getProductName());
					result.setBatchNumber(product.getBatchNumber());
					result.setOpeningStock(product.getOpeningStock());
					result.setProductsToCustomer(product.getProductsToCustomer());
					result.setProductsToFactory(product.getProductsToFactory());
					result.setClosingStock(product.getClosingStock());
					result.setTotalExpenses(StringUtil.currencyFormat(amount.getTotalAllowances()));
					result.setExecutiveAllowances(StringUtil.currencyFormat(amount.getExecutiveAllowances()));
					result.setDriverAllowances(StringUtil.currencyFormat(amount.getDriverAllowances()));
					result.setMiscellaneousExpenses(StringUtil.currencyFormat(amount.getMiscellaneousExpenses()));
					result.setMunicipalCityCouncil(StringUtil.currencyFormat(amount.getMunicipalCityCouncil()));
					result.setVehicleFuelExpenses(StringUtil.currencyFormat(amount.getVehicleFuelExpenses()));
					result.setVehicleMaintenanceExpenses(StringUtil.currencyFormat(amount.getVehicleMaintenanceExpenses()));
					result.setOffloadingLoadingCharges(StringUtil.currencyFormat(amount.getOffloadingLoadingCharges()));
					result.setDealerPartyExpenses(StringUtil.currencyFormat(amount.getDealerPartyExpenses()));
					result.setReasonMiscellaneousExpenses(StringUtil.format(amount.getReasonMiscellaneousExpenses()));
					result.setTotalPayable(StringUtil.currencyFormat(amount.getCustomerTotalPayable()));
					result.setTotalRecieved(StringUtil.currencyFormat(amount.getCustomerTotalReceived()));
					result.setBalance(StringUtil.currencyFormat(amount.getCustomerTotalCredit()));
					result.setAmountToBank(StringUtil.currencyFormat(amount.getAmountToBank()));
					result.setAmountToFactory(StringUtil.currencyFormat(amount.getAmountToFactory()));
					result.setClosingBalance(StringUtil.currencyFormat(amount.getClosingBalance()));
					result.setReasonAmountToBank(StringUtil.format(amount.getAmountsRemarks()));
					result.setOpeningBalance(StringUtil.currencyFormat(vbDayBook.getVbSalesBook().getOpeningBalance()));
					result.setSalesExecutive(fullName);
					result.setReturnQty(returnQty);
					result.setCreatedDate(DateUtils.format(createdDate));
					result.setStartingReading(startingReading);
					result.setEndingReading(endingReading);
					result.setDriverName(StringUtil.format(driverName));
					result.setRemarks(StringUtil.format(remarks));
					result.setVehicleNo(StringUtil.format(vehicleNo));
					result.setId(vbDayBook.getId());
					result.setIsReturn(vbDayBook.getIsReturn());
					result.setReportingManager(StringUtil.format(vbDayBook.getReportingManager()));
					result.setDayBookNo(StringUtil.format(vbDayBook.getDayBookNo()));
					result.setReportingManager(vbDayBook.getReportingManager());
					result.setBasicInfoRemarks(StringUtil.format(vbDayBook.getDayBookRemarks()));
					result.setVehicleDetailRemarks(StringUtil.format(vbDayBook.getVehicleDetailsRemarks()));
					result.setAllowancesRemarks(StringUtil.format(amount.getAllowancesRemarks()));
					result.setAmountRemarks(StringUtil.format(amount.getAmountsRemarks()));
					result.setProductsRemarks(StringUtil.format(product.getProductsRemarks()));
					if (!amountsList.isEmpty()) {
						result.setDepositedAmounts(amountsList);
					}
					resultList.add(result);
				}
			}
			Collections.sort(resultList);
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", resultList.size());
			}
			return resultList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to get all the sales executives associated to the particular organization.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param executiveName - {@link String}
	 * @return salesExecutives - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllSalesExecutives(VbOrganization organization,
			String executiveName) throws DataAccessException {
		Session session = this.getSession();
		List<String> salesExecutives = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.like("username", executiveName, MatchMode.START))
				.addOrder(Order.asc("username"))
				.list();
		session.close();
		
		if(!salesExecutives.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", salesExecutives.size());
			}
			return salesExecutives;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
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
		Boolean isDaily = Boolean.FALSE;
		Session session = this.getSession();
		String allotmentType = (String) session.createQuery(
						"SELECT vb.allotmentType FROM VbSalesBook vb WHERE vb.createdOn IN"
						+ "(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive " +
						"AND vbs.vbOrganization = :vbOrganization)")
				.setParameter("salesExecutive", userName)
				.setParameter("vbOrganization", organization)
				.uniqueResult();
		session.close();
		
		if (OrganizationUtils.ALLOTMENT_TYPE_DAILY.equalsIgnoreCase(allotmentType)) {
			isDaily = Boolean.TRUE;
		}
		return isDaily;
	}

	/**
	 * This method is used to check the day book is closed or not on particular
	 * date for particular sales executive.
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
		session.close();
		
		if (vbDayBook != null) {
			isDayBookClosed = Boolean.TRUE;
		}
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
		session.close();
		
		if (openingBalance == null) {
			openingBal = new Long(0);
		} else {
			openingBal = openingBalance.longValue();
		}
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
		Integer cycleId = (Integer) session.createQuery(
						"SELECT vb.cycleId FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 " +
						"AND vb.salesExecutive = :salesExecutive1 AND vb.createdOn IN " +
						"(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.vbOrganization = :vbOrganization2 " +
						"AND vbs.salesExecutive = :salesExecutive2)")
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
		Query query = session.createQuery(
						"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization " +
						"AND (vb.createdOn,vb.cycleId) IN (SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE" +
						" vbs.salesExecutive = :salesExecutiveName AND vbs.vbOrganization = :vbOrganization )")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}

	/**
	 * This method is responsible to get advance amount of SLE.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return advance - {@link Float}
	 */
	public Float getAdvance(String salesExecutive, VbOrganization organization) {
		Session session = this.getSession();
		Float advance = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("advance"))
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.uniqueResult();
		session.close();

		if (advance == null) {
			advance = new Float(0);
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} is the advance of {}", advance, salesExecutive);
		}
		return advance;
	}

	/**
	 * This method is responsible to get configured excess amount from
	 * {@link VbExcessCash}.
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
		if (vbAlertCategory != null) {
			VbAlertType vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
					.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
					.add(Restrictions.eq("alertType", alertType))
					.uniqueResult();
			
			if (vbAlertType != null) {
				VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.createCriteria(VbUserDefinedAlerts.class)
						.add(Restrictions.eq("vbAlertType", vbAlertType))
						.add(Restrictions.eq("vbOrganization", organization))
						.add(Restrictions.eq("activeInactive", Boolean.TRUE))
						.uniqueResult();
				
				if (vbUserDefinedAlerts != null) {
					excessAmount = (Float) session.createCriteria(VbExcessCash.class)
							.setProjection(Projections.property("amount"))
							.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
							.uniqueResult();
				}
			}
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("excessAmount: {}", excessAmount);
		}
		return excessAmount;
	}

	/**
	 * This method is responsible to get the unique no for {@link VbDayBook}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedDayBookNo - {@link String}
	 */
	public String generateDayBookNo(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		// For fetching latest invoice no.
		Query query = session.createQuery(
						"SELECT vb.dayBookNo FROM VbDayBook vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDayBook vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.dayBookNo LIKE :dayBookNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("dayBookNo", "%DB%");
		String latestDayBookNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_DAY_BOOK))
				.uniqueResult();

		StringBuffer generatedDayBookNo = new StringBuffer();
		if (latestDayBookNo == null) {
			if (invoiceNoPeriod != null) {
				generatedDayBookNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DAY_BOOK)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedDayBookNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DAY_BOOK)
						.append(seperator).append(formattedDate)
						.append(seperator).append("0001");
			}

		} else {
			String[] previousDayBookNo = latestDayBookNo.split("#");
			String[] latestDayBookNoArray = previousDayBookNo[0].split(seperator);
			latestDayBookNo = latestDayBookNoArray[3];
			Integer dayBookNo = Integer.parseInt(latestDayBookNo);
			String newDayBookNo = resetInvoiceNo(session, invoiceNoPeriod, organization, dayBookNo);
			generatedDayBookNo.append(organization.getOrganizationCode())
					.append(seperator)
					.append(OrganizationUtils.TXN_TYPE_DAY_BOOK)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newDayBookNo);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated daybookNo is {}", generatedDayBookNo.toString());
		}
		return generatedDayBookNo.toString();
	}

	/**
	 * This method is used to save the day book temporarily.
	 * 
	 * @param tempDayBookCommand - {@link TempDayBookCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveTempDayBook(TempDayBookCommand tempDayBookCommand,
			String userName, VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			VbCashDayBook vbCashDayBook = new VbCashDayBook();
			String dayBookType = tempDayBookCommand.getDayBookType();
			String allowancesType = tempDayBookCommand.getAllowanceType();
			vbCashDayBook.setCreatedBy(userName);
			vbCashDayBook.setCreatedOn(new Date());
			vbCashDayBook.setModifiedOn(new Date());
			vbCashDayBook.setVbOrganization(organization);
			vbCashDayBook.setVbSalesBook(salesBook);
			if (OrganizationUtils.DAY_BOOK_AMOUNT.equalsIgnoreCase(dayBookType)) {
				vbCashDayBook.setDayBookType(dayBookType);
				vbCashDayBook.setValueOne(tempDayBookCommand.getAmountToBank());
				vbCashDayBook.setValueThree(tempDayBookCommand.getRemarks());
			} else if (OrganizationUtils.DAY_BOOK_ALLOWANCES.equalsIgnoreCase(dayBookType)) {
				vbCashDayBook.setDayBookType(allowancesType);
				vbCashDayBook.setValueOne(tempDayBookCommand.getAllowancesAmount());
				if (OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES.equals(allowancesType)) {
					vbCashDayBook.setValueThree(tempDayBookCommand.getBusinessName());
				} else if (OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(allowancesType)) {
					vbCashDayBook.setValueThree(tempDayBookCommand.getMeterReading());
				} else {
					vbCashDayBook.setValueThree(tempDayBookCommand.getRemarks());
				}
			} else if (OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS.equalsIgnoreCase(dayBookType)) {
				vbCashDayBook.setDayBookType(dayBookType);
				vbCashDayBook.setValueOne(tempDayBookCommand.getVehicleNo());
				vbCashDayBook.setValueTwo(tempDayBookCommand.getDriverName());
				vbCashDayBook.setValueThree(tempDayBookCommand.getStartingReading());
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCashDayBook");
			}
			session.save(vbCashDayBook);
			txn.commit();
		} catch (HibernateException exception) {
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
	 * This method is responsible for getting all the allowances.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return resultList - {@link DayBookAllowancesResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookAllowancesResult> getAllowances(String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		DayBookAllowancesResult allowancesResult = null;
		List<VbCashDayBook> cashDayBooks = session.createCriteria(VbCashDayBook.class)
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.list();
		session.close();
		
		if (!cashDayBooks.isEmpty()) {
			String dayBookType = null;
			String valueOne = null;
			Float amountToBank = new Float(0);
			List<DayBookAllowancesResult> resultList = new ArrayList<DayBookAllowancesResult>();
			for (VbCashDayBook vbCashDayBook : cashDayBooks) {
				allowancesResult = new DayBookAllowancesResult();
				dayBookType = vbCashDayBook.getDayBookType();
				valueOne = vbCashDayBook.getValueOne();
				allowancesResult.setDayBookType(dayBookType);
				if (OrganizationUtils.DAY_BOOK_AMOUNT.equalsIgnoreCase(dayBookType)) {
					amountToBank += Float.parseFloat(valueOne);
					allowancesResult.setAmountToBank(StringUtil.currencyFormat(amountToBank));
					allowancesResult.setReasonAmountToBank(vbCashDayBook.getValueThree());
				} else if (OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setVehicleNo(valueOne);
					allowancesResult.setDriverName(vbCashDayBook.getValueTwo());
					allowancesResult.setStartingReading(vbCashDayBook.getValueThree());
				} else if (OrganizationUtils.DAY_BOOK_DRIVER_ALLOWANCES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setDriverAllowances(valueOne);
				} else if (OrganizationUtils.DAY_BOOK_DEALER_PARTY_EXPENSES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setDealerPartyExpenses(valueOne);
					allowancesResult.setReasonDealerPartyExpenses(vbCashDayBook.getValueThree());
				} else if (OrganizationUtils.DAY_BOOK_EXECUTIVE_ALLOWANCES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setExecutiveAllowances(valueOne);
				} else if (OrganizationUtils.DAY_BOOK_MISCELLANEOUS_EXPENSES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setMiscellaneousExpenses(valueOne);
					allowancesResult.setReasonMiscellaneousExpenses(vbCashDayBook.getValueThree());
				} else if (OrganizationUtils.DAY_BOOK_MUNCIPAL_CITY_COUNCIL.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setMunicipalCityCouncil(valueOne);
				} else if (OrganizationUtils.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setVehicleMaintenanceExpenses(valueOne);
				} else if (OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setOffLoadingCharges(valueOne);
				} else if (OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(dayBookType)) {
					allowancesResult.setVehicleFuelExpenses(valueOne);
				}
				resultList.add(allowancesResult);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} results have been found for {}", resultList.size(), userName);
			}
			return resultList;
		} else {
			if(session != null) {
				session.close();
			}
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is used to get the Vehicle Fuel Expenses
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param dayBookType - {@link String}
	 * @return resultsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DayBookAllowancesResult> getExecutiveAllowances(String userName, VbOrganization organization, String dayBookType) throws DataAccessException {
		Session session = this.getSession();
		List<DayBookAllowancesResult> resultsList = null;
		DayBookAllowancesResult allowancesResult = null;
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		List<VbCashDayBook> resultList = session.createCriteria(VbCashDayBook.class)
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("dayBookType", dayBookType))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.list();
		session.close();
		
		if(!resultList.isEmpty()) {
			resultsList = new ArrayList<DayBookAllowancesResult>();
			for (VbCashDayBook vbCashDayBook : resultList) {
				allowancesResult = new DayBookAllowancesResult();
				allowancesResult.setId(vbCashDayBook.getId());
				allowancesResult.setCreatedOn(DateUtils.formatDateWithTimestamp(vbCashDayBook.getModifiedOn()));
				if (dayBookType.equals(OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES)) {
					allowancesResult.setVehicleFuelExpenses(vbCashDayBook.getValueOne());
					allowancesResult.setVehicleMeterReading(vbCashDayBook.getValueThree());
				} else if (dayBookType.equals(OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES)) {
					allowancesResult.setOffLoadingCharges(vbCashDayBook.getValueOne());
					allowancesResult.setBusinessName(vbCashDayBook.getValueThree());
				} else {
					allowancesResult.setExecutiveAllowances(vbCashDayBook.getValueOne());
					allowancesResult.setRemarks(vbCashDayBook.getValueThree());
				}
				resultsList.add(allowancesResult);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("available executive allowances for today are {}", resultsList.size());
			}
			return resultsList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method Is To Retrieve Existed Starting Reading Or Meter Reading if exists.
	 * 
	 * @param dayBookType - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return existedStartReading - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public String getExistedStartReading(String dayBookType, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		String existedStartReading = null;
		String meterReading = (String) session.createCriteria(VbCashDayBook.class)
				.setProjection(Projections.max("valueThree"))
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.uniqueResult();
		
		if (meterReading == null) {
			String sql = "select vbtb.valueThree FROM VbCashDayBook vbtb WHERE vbtb.vbOrganization=:organization " +
					"AND vbtb.vbSalesBook =:salesBook AND vbtb.dayBookType =:dayBookType AND"
					+ " vbtb.createdOn=(select MAX(vb.createdOn) FROM VbCashDayBook vb WHERE vb.vbOrganization=:organization " +
					"AND vb.vbSalesBook =:salesBook AND vb.dayBookType =:dayBookType)";
			existedStartReading = (String) session.createQuery(sql)
					.setParameter("salesBook", salesBook)
					.setParameter("organization", organization)
					.setParameter("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS)
					.uniqueResult();
			session.close();
			
			return existedStartReading;

		} else {
			session.close();
			return meterReading;
		}
	}

	/**
	 * This method is used to get the business names.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return businessNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessNames(String businessName,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		List<String> businessNameList = session.createCriteria(VbDeliveryNote.class)
				.setProjection(Projections.distinct(Projections.property("businessName")))
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.addOrder(Order.asc("businessName"))
				.list();
		session.close();
		
		if(!businessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} business name have been assigned to {}", businessNameList.size(), userName);
			}
			return businessNameList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method Is to check whether Vehicle Details Are Already entered Or
	 * not
	 * 
	 * @param userName - String
	 * @param organization - {@link VbOrganization}
	 * @return dayBookAllowancesResultsList - {@link DayBookAllowancesResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public List<DayBookAllowancesResult> getExistedVehicleDetails(
			String userName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String dayBookType = OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS;
		List<VbCashDayBook> vehicleDetailsList = getExistedCashDayBookData(session, userName, organization, dayBookType);
		session.close();
		
		if (vehicleDetailsList != null && !vehicleDetailsList.isEmpty()) {
			DayBookAllowancesResult dayBookAllowancesResult = null;
			List<DayBookAllowancesResult> dayBookAllowancesResultsList = new ArrayList<DayBookAllowancesResult>();
			for (VbCashDayBook vbCashDayBook : vehicleDetailsList) {
				dayBookAllowancesResult = new DayBookAllowancesResult();
				dayBookAllowancesResult.setDriverName(vbCashDayBook.getValueTwo());
				dayBookAllowancesResult.setVehicleNo(vbCashDayBook.getValueOne());
				dayBookAllowancesResult.setStartingReading(vbCashDayBook.getValueThree());
				dayBookAllowancesResult.setId(vbCashDayBook.getId());
				
				dayBookAllowancesResultsList.add(dayBookAllowancesResult);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", dayBookAllowancesResultsList.size());
			}
			return dayBookAllowancesResultsList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method is responsible to Update Specified record in Vehicle Details
	 * 
	 * @param id - {@link Integer}
	 * @param vehicleNo - {@link String}
	 * @param driverName - {@link String}
	 * @param startingReading - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void updateVehicleDetails(Integer id, String vehicleNo,
			String driverName, String startingReading) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook vbCashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		
		if (vbCashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			vbCashDayBook.setValueOne(vehicleNo);
			vbCashDayBook.setValueTwo(driverName);
			vbCashDayBook.setValueThree(startingReading);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCashDayBook");
			}
			session.update(vbCashDayBook);
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method is responsible to Delete Specified record in Vehicle Details.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void deleteVehicleDetails(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook cashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		if (cashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(cashDayBook);
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible for get previous day book based on current
	 * dayBookNumber,flag=0,and previousDayBookId
	 * 
	 * @param dayBookId - {@link Integer}
	 * @param dayBookNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return deliveryNoteMaxId - {@link Integer}
	 */
	public Integer getPreviousDayBookNumber(Integer dayBookCRId,
			String dayBookNumber, VbOrganization organization, String username) {
		Session session = this.getSession();
		Integer daybookId = new Integer(0);
		VbDayBookChangeRequest dayBookCR = (VbDayBookChangeRequest) session.createCriteria(VbDayBookChangeRequest.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.add(Expression.eq("dayBookNo", dayBookNumber))
				.add(Expression.eq("createdBy", username))
				.add(Expression.eq("flag", new Integer(1)))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		
		if (dayBookCR != null) {
			Query query = session.createQuery(
							"SELECT MAX(vbd.id) FROM VbDayBookChangeRequest vbd WHERE vbd.dayBookNo LIKE :dayBookNo " +
							"AND vbd.flag= :flag AND vbd.id < :dayBookId AND vbd.vbOrganization = :vbOrganization")
					.setParameter("dayBookNo", dayBookNumber)
					.setParameter("flag", new Integer(1))
					.setParameter("dayBookId", dayBookCR.getId())
					.setParameter("vbOrganization", organization);
			Integer dayBookMaxId = getSingleResultOrNull(query);
			
			if (dayBookMaxId != null) {
				daybookId = dayBookMaxId;
			}
		}
		session.close();
		
		return daybookId;
	}

	/**
	 * This method is responsible to get deposited amount. 
	 * 
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return allowancesResultsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public List<DayBookAllowancesResult> getDepositedAmounts(String userName,
			VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		List<VbCashDayBook> existedAmountsList = getExistedCashDayBookData(session, userName, vbOrganization, OrganizationUtils.DAY_BOOK_AMOUNT);
		session.close();
		
		if(existedAmountsList != null && !existedAmountsList.isEmpty()) {
			DayBookAllowancesResult dayBookAllowancesResult = null;
			List<DayBookAllowancesResult> allowancesResultsList = new ArrayList<DayBookAllowancesResult>();
			for (VbCashDayBook vbCashDayBook : existedAmountsList) {
				dayBookAllowancesResult = new DayBookAllowancesResult();
				dayBookAllowancesResult.setAmountToBank(vbCashDayBook.getValueOne());
				dayBookAllowancesResult.setRemarks(vbCashDayBook.getValueThree());
				dayBookAllowancesResult.setId(vbCashDayBook.getId());
				allowancesResultsList.add(dayBookAllowancesResult);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", allowancesResultsList.size());
			}
			return allowancesResultsList;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to get existing {@link VbCashDayBook}s.
	 * 
	 * @param session - {@link Session}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param dayBookType - {@link String}
	 * @return existedCashDayBookList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	private List<VbCashDayBook> getExistedCashDayBookData(Session session,
			String userName, VbOrganization vbOrganization, String dayBookType) {
		VbSalesBook salesBook = getVbSalesBook(session, userName, vbOrganization);
		List<VbCashDayBook> existedCashDayBookList = (List<VbCashDayBook>) session.createCriteria(VbCashDayBook.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.add(Restrictions.eq("dayBookType", dayBookType))
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.list();
		
		return existedCashDayBookList;
	}

	/**
	 * This Method is responsible to Update Specified record in Deposited
	 * Amounts
	 * 
	 * @param id - {@link Integer}
	 * @param amountToBank - {@link String}
	 * @param remarks - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void updateDepositedAmounts(Integer id, String amountToBank, String remarks) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook vbCashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		
		if (vbCashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			vbCashDayBook.setValueOne(amountToBank);
			vbCashDayBook.setValueThree(remarks);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCashDayBook");
			}
			session.update(vbCashDayBook);
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method is responsible to Delete Specified record in Deposited
	 * Amounts.
	 * 
	 * @param id - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	public void deleteDepositedAmounts(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook vbCashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		if (vbCashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(vbCashDayBook);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Deleting VbCashDayBook");
			}
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This Method is Responsible To check The Status of Vehicle Details .
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isAvailable - {@link Boolean}
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkVehicleDetailsExistance(VbOrganization organization, String userName) {
		Session session = this.getSession();
		Boolean isAvailable = Boolean.FALSE;
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		List<VbCashDayBook> tempDayBook = (List<VbCashDayBook>) session.createCriteria(VbCashDayBook.class)
				.createAlias("vbSalesBook", "salesBook")
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.add(Restrictions.eq("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS))
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();

		if (!tempDayBook.isEmpty()) {
			isAvailable = Boolean.TRUE;
		}

		return isAvailable;
	}

	/**
	 * This method is responsible to get the total payments for that day from -
	 * {@link VbDeliveryNotePayments}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return availableAmount - {@link Float}
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	public Float getPresentPayment(String userName, VbOrganization organization) throws ParseException {
		Session session = this.getSession();
		Float availableAmount = new Float(0);
		Float amount = new Float(0);
		VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
		Float totalPayment = new Float(0);
		Float openingBalance = salesBook.getOpeningBalance();
		Criteria criteria = session.createCriteria(VbDeliveryNotePayments.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.setProjection(Projections.sum("presentPayment"))
				.add(Restrictions.eq("deliveryNote.vbSalesBook", salesBook))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.add(Restrictions.eq("deliveryNote.flag", new Integer(1)));
		if(OrganizationUtils.ALLOTMENT_TYPE_NON_DAILY.equals(salesBook.getAllotmentType())) {
			Date startDate = DateUtils.parse(getSalesExecutiveStartDate(salesBook.getSalesExecutive(), organization));
			criteria.add(Restrictions.between("deliveryNote.createdOn", DateUtils.getStartTimeStamp(startDate), DateUtils.getEndTimeStamp(new Date())));
		}
		totalPayment = (Float) criteria.uniqueResult();
		
		List<String> amountToBankList = session.createCriteria(VbCashDayBook.class)
				.setProjection(Projections.property("valueOne"))
				.add(Restrictions.eq("vbSalesBook", salesBook))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.ne("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.list();
		session.close();
		if(totalPayment == null){
			totalPayment = new Float(0);
		}
		if (!(amountToBankList.isEmpty())) {
			for (String amountSpent : amountToBankList) {
				amount += Float.parseFloat(amountSpent);
			}
			availableAmount = (totalPayment + openingBalance) - amount;
		} else {
			availableAmount = totalPayment + openingBalance;
		}


		if (_logger.isDebugEnabled()) {
			_logger.debug("Available amount To spend : {}", availableAmount);
		}
		return availableAmount;
	}

	/**
	 * This Method is To Retrieve Vehicle Fuel Expenses From VbCash Day Book.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return dayBookAllowancesResultsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public List<DayBookAllowancesResult> getVehicleFuelExpenses(VbOrganization organization, String userName) throws DataAccessException {
		List<DayBookAllowancesResult> dayBookAllowancesResultsList = new ArrayList<DayBookAllowancesResult>();
		String dayBookType = OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES;
		Session session = this.getSession();
		List<VbCashDayBook> cashDayBooksList = getExistedCashDayBookData(session, userName, organization, dayBookType);
		session.close();
		
		if(cashDayBooksList != null && !cashDayBooksList.isEmpty()) {
			for (VbCashDayBook vbCashDayBook : cashDayBooksList) {
				DayBookAllowancesResult allowancesResult = new DayBookAllowancesResult();
				allowancesResult.setVehicleMeterReading(vbCashDayBook.getValueThree());
				allowancesResult.setAllowancesAmount(vbCashDayBook.getValueOne());
				allowancesResult.setId(vbCashDayBook.getId());
				dayBookAllowancesResultsList.add(allowancesResult);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", dayBookAllowancesResultsList.size());
			}
			return dayBookAllowancesResultsList;
		}  else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to update {@link VbCashDayBook}.
	 * 
	 * @param id - {@link Integer}
	 * @param allowancesAmount - {@link String}
	 * @param meterReading - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void updateCashDayBook(Integer id, String valueOne, String valueThree) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook vbCashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		if (vbCashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			vbCashDayBook.setValueOne(valueOne);
			vbCashDayBook.setValueThree(valueThree);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCashDayBook");
			}
			session.update(vbCashDayBook);
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to delete {@link VbCashDayBook}.
	 * 
	 * @param id - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void deleteCashDayBook(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbCashDayBook vbCashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		if (vbCashDayBook != null) {
			Transaction transaction = session.beginTransaction();
			session.delete(vbCashDayBook);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Deleting VbCashDayBook");
			}
			transaction.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to get the sales executive's start
	 * {@link Date}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createDate - {@link Date}
	 */
	public String getSalesExecutiveStartDate(String userName,
			VbOrganization organization) {
		Session session = this.getSession();
		String createDate = null;
		VbSalesBook vbSalesBook = getVbSalesBook(session, userName, organization);
		if (vbSalesBook == null) {
			vbSalesBook = getVbSalesBookNoFlag(session, userName, organization);
		}
		VbSalesBook salesBook = (VbSalesBook) session.createQuery(
						"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 "
						+ "AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN "
						+ "(SELECT MIN(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 "
						+ "AND vbs.vbOrganization = :vbOrganization2 AND vbs.cycleId = :cycleId)")
				.setParameter("salesExecutive1", userName)
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutive2", userName)
				.setParameter("vbOrganization2", organization)
				.setParameter("cycleId", vbSalesBook.getCycleId())
				.uniqueResult();
		session.close();
		
		if (salesBook != null) {
			createDate = DateUtils.format(salesBook.getCreatedOn());
		} else {
			createDate =DateUtils.format(vbSalesBook.getCreatedOn());
		}

		return createDate;
	}

	/**
	 * This method is responsible to get createdBy of {@link VbSalesBook}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link String}
	 */
	public String getCretedBy(String userName, VbOrganization organization) {
		Session session = this.getSession();
		String createdBy = null;
		VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
		session.close();

		if (salesBook != null) {
			createdBy = salesBook.getCreatedBy();
		}
		return createdBy;
	}

	/**
	 * This method is responsible to update the vehicle fuel expenses -
	 * {@link VbCashDayBook}.
	 * 
	 * @param id - {@link Integer}
	 * @param valueOne - {@link String}
	 * @param valueThree - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void editAllowance(Integer id, String valueOne, String valueThree)
			throws DataAccessException {
		Session session = this.getSession();
		
		VbCashDayBook cashDayBook = (VbCashDayBook) session.get(VbCashDayBook.class, id);
		if (cashDayBook != null) {
			Transaction tx = session.beginTransaction();
			cashDayBook.setValueOne(valueOne);
			cashDayBook.setValueThree(valueThree);
			session.update(cashDayBook);
			tx.commit();
			session.close();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to check availability of off loading charges.
	 * 
	 * @param businessName - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link Boolean}
	 */
	public Boolean checkForOffloadingCharges(String businessName,
			String userName, VbOrganization organization) {
		Boolean isAvailable = Boolean.FALSE;
		Session session = this.getSession();
		Integer deliveryNoteCount = (Integer) session
				.createCriteria(VbDeliveryNote.class)
				.setProjection(Projections.count("businessName"))
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date()))).uniqueResult();
		if (deliveryNoteCount != 0) {
			Integer cashDayBookCount = (Integer) session
					.createCriteria(VbCashDayBook.class)
					.setProjection(Projections.count("id"))
					.add(Restrictions.eq("valueThree", businessName))
					.add(Restrictions.eq("dayBookType",
							OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
					.uniqueResult();
			if (deliveryNoteCount.intValue() != cashDayBookCount.intValue()) {
				isAvailable = Boolean.TRUE;
			} else {
				isAvailable = Boolean.FALSE;
			}
		} else {
			isAvailable = Boolean.FALSE;
		}
		session.close();
		return isAvailable;
	}
}