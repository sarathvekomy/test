/**
 * com.vekomy.vbooks.mysales.cr.dao.DayBookCrDao.java
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
import com.vekomy.vbooks.hibernate.model.VbCashDayBook;
import com.vekomy.vbooks.hibernate.model.VbCashDayBookCr;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesResult;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.mysales.command.DayBookViewResult;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookAmountCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookProductsCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookVehicleDetailsCommand;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;
/**
 * This dao class is responsible to perform operations on Day Book change request in sales
 * module.
 * 
 * @author Ankit
 * 
 */

public class DayBookCrDao extends MysalesCrBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
private static final Logger _logger = LoggerFactory.getLogger(DayBookCrDao.class);
/**
 * This method is responsible to store day book CR details into DB.
 * 
 * @param dayBookBasicInfoCommand - {@link ChangeRequestDayBookBasicInfoCommand}
 * @param dayBookAllowancesCommand - {@link ChangeRequestDayBookAllowancesCommand}
 * @param dayBookAmountCommand - {@link ChangeRequestDayBookAmountCommand}
 * @param productsDetailsCRCommand - {@link ChangeRequestDayBookProductsCommand}
 * @param dayBookProductsCommand - {@link List}
 * @param dayBookVehicleCommand - {@link ChangeRequestDayBookVehicleDetailsCommand}
 * @param dayBookNumber - {@link String}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @param isReturn - {@link String}
 * @return isSaved - {@link Boolean}
 * @throws DataAccessException - {@link DataAccessException}
 * 
 */
@SuppressWarnings("unchecked")
public synchronized void saveDayBook(ChangeRequestDayBookBasicInfoCommand dayBookBasicInfoCommand,
		ChangeRequestDayBookAllowancesCommand dayBookAllowancesCommand, 
		ChangeRequestDayBookAmountCommand dayBookAmountCommand,
		ChangeRequestDayBookProductsCommand productsDetailsCRCommand,
		List<ChangeRequestDayBookProductsCommand> dayBookProductsCommand,
		ChangeRequestDayBookVehicleDetailsCommand dayBookVehicleCommand,String dayBookNumber,
		VbOrganization organization, String userName, String isReturn) throws DataAccessException {
	
	Boolean returnToFactory=Boolean.FALSE;
	Session session = null;
	Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbDayBookChangeRequest vbDayBookChangeRequest = null;
			VbDayBookChangeRequestAmount vbDayBookChangeRequestAmount = null;
			Date date = new Date();
			returnToFactory = Boolean.parseBoolean(isReturn);
			if (returnToFactory == null) {
				returnToFactory = Boolean.TRUE;
			}
			// fetch original Day book Details based on day book number
			VbDayBook originalDayBook = (VbDayBook) session.createCriteria(VbDayBook.class)
					.add(Restrictions.eq("dayBookNo", dayBookNumber))
					.add(Restrictions.eq("salesExecutive", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (originalDayBook != null) {
				vbDayBookChangeRequest = new VbDayBookChangeRequest();
				String originalReportingManager = originalDayBook.getReportingManager();
				String changedReportingManager = dayBookBasicInfoCommand.getReportingManager();
				String originalDayBookRemarks = originalDayBook.getDayBookRemarks();
				String changedDayBookRemarks = dayBookBasicInfoCommand.getDayBookCRRemarks();
				String originalDayBookVehicleEndingReading = Float.toString(originalDayBook.getEndingReading());
				String changedDayBookVehicleEndingReading = dayBookVehicleCommand.getEndingReading();
				String originalDayBookVehicleRemarks = originalDayBook.getVehicleDetailsRemarks();
				String changedDayBookVehicleRemarks = dayBookVehicleCommand.getDayBookVehicleRemarks();
				vbDayBookChangeRequest.setVbSalesBook(originalDayBook.getVbSalesBook());
				vbDayBookChangeRequest.setCreatedBy(userName);
				vbDayBookChangeRequest.setCreatedOn(date);
				vbDayBookChangeRequest.setModifiedOn(date);
				vbDayBookChangeRequest.setSalesExecutive(userName);
				vbDayBookChangeRequest.setFlag(new Integer(1));
				vbDayBookChangeRequest.setDayBookNo(dayBookNumber);
				vbDayBookChangeRequest.setCrDescription(dayBookVehicleCommand.getDescription());
				if (returnToFactory != null) {
					vbDayBookChangeRequest.setIsReturn(returnToFactory);
				} else {
					vbDayBookChangeRequest.setIsReturn(Boolean.FALSE);
				}
				if (originalReportingManager.equals(changedReportingManager)) {
					vbDayBookChangeRequest.setReportingManager(originalReportingManager.concat(",0"));
				} else {
					vbDayBookChangeRequest.setReportingManager(changedReportingManager.concat(",1"));
				}
				if (originalDayBookRemarks == "null") {
				} else {
					if (originalDayBookRemarks.equals(changedDayBookRemarks)) {
						vbDayBookChangeRequest.setDayBookRemarks(originalDayBookRemarks.concat(",0"));
					} else {
						vbDayBookChangeRequest.setDayBookRemarks(changedDayBookRemarks.concat(",1"));
					}
				}
				if (originalDayBookVehicleEndingReading.replace(".0", "").equals(changedDayBookVehicleEndingReading)) {
					vbDayBookChangeRequest.setEndingReading(originalDayBookVehicleEndingReading.concat(",0"));
				} else {
					vbDayBookChangeRequest.setEndingReading(changedDayBookVehicleEndingReading.concat(",1"));
				}
				vbDayBookChangeRequest.setVehicleNo(dayBookVehicleCommand.getVehicleNo().concat(",0"));
				vbDayBookChangeRequest.setDriverName(dayBookVehicleCommand.getDriverName().concat(",0"));
				vbDayBookChangeRequest.setStartingReading(dayBookVehicleCommand.getStartingReading().concat(",0"));
				if (originalDayBookVehicleRemarks == "null"	|| originalDayBookVehicleRemarks == null) {
				} else {
					if (originalDayBookVehicleRemarks.equals(changedDayBookVehicleRemarks)) {
						vbDayBookChangeRequest.setVehicleDetailsRemarks(originalDayBookVehicleRemarks.concat(",0"));
					} else {
						vbDayBookChangeRequest.setVehicleDetailsRemarks(changedDayBookVehicleRemarks.concat(",1"));
					}
				}
				vbDayBookChangeRequest.setVbOrganization(organization);
				vbDayBookChangeRequest.setStatus(CRStatus.PENDING.name());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting vbDayBookChangeRequest: {}",	vbDayBookChangeRequest);
				}
				session.saveOrUpdate(vbDayBookChangeRequest);
			}
			// fetch Day Book original Amount Details and compared with Day Book
			// changed amount and allowances details
			String originalDayBookAmountToFactory = null;
			String originalDayBookAmountToBank = null;
			String originalDayBookSEClosingBalance = null;
			String changedDayBookAmountToFactory = null;
			String changedDayBookAmountToBank = null;
			String changedDayBookSEClosingBalance = null;
			String originalDayBookAmountRemarks = null;
			String changedDayBookAmountRemarks = null;
			// day book allowances fields
			String originalExecutiveAllowances = null;
			String originalDriverAllowances = null;
			String originalVehicleFuelExpences = null;
			String originalOffloadingCharges = null;
			String originalVehicleMaintenanceExpences = null;
			String originalMiscellaneousExpenses = null;
			String originalDealerPartyExpences = null;
			String originalMunicipalCityCouncil = null;
			String originalTotalAllowances = null;
			String originalAllowancesRemarks = null;

			String changedExecutiveAllowances = null;
			String changedDriverAllowances = null;
			String changedVehicleFuelExpences = null;
			String changedOffloadingCharges = null;
			String changedVehicleMaintenanceExpences = null;
			String changedMiscellaneousExpenses = null;
			String changedDealerPartyExpences = null;
			String changedMunicipalCityCouncil = null;
			String changedTotalAllowances = null;
			String changedAllowancesRemarks = null;
			List<VbDayBookAmount> originalDayBookAmountsDetails = new ArrayList<VbDayBookAmount>(originalDayBook.getVbDayBookAmounts());
			for (VbDayBookAmount originalDayBookAmount : originalDayBookAmountsDetails) {
				originalDayBookAmountToFactory = Float.toString(originalDayBookAmount.getAmountToFactory());
				originalDayBookAmountToBank = Float.toString(originalDayBookAmount.getAmountToBank());
				originalDayBookSEClosingBalance = Float.toString(originalDayBookAmount.getClosingBalance());
				originalDayBookAmountRemarks = originalDayBookAmount.getAmountsRemarks();
				changedDayBookAmountToFactory = dayBookAmountCommand.getAmountToFactory();
				changedDayBookAmountToBank = dayBookAmountCommand.getAmountToBank();
				changedDayBookSEClosingBalance = dayBookAmountCommand.getClosingBalance();
				changedDayBookAmountRemarks = dayBookAmountCommand.getAmountsCRRemarks();
				vbDayBookChangeRequestAmount = new VbDayBookChangeRequestAmount();
				if (vbDayBookChangeRequestAmount != null) {
					if (originalDayBookAmountToBank.replace(".0", "").equals(changedDayBookAmountToBank)) {
						vbDayBookChangeRequestAmount.setAmountToBank(originalDayBookAmountToBank.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setAmountToBank(changedDayBookAmountToBank.concat(",1"));
					}
					if (originalDayBookAmountToFactory.replace(".0", "").equals(changedDayBookAmountToFactory)) {
						vbDayBookChangeRequestAmount.setAmountToFactory(originalDayBookAmountToFactory.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setAmountToFactory(dayBookAmountCommand.getAmountToFactory().concat(",1"));
					}
					if (originalDayBookSEClosingBalance.replace(".0", "").equals(changedDayBookSEClosingBalance)) {
						vbDayBookChangeRequestAmount.setClosingBalance(originalDayBookSEClosingBalance.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setClosingBalance(changedDayBookSEClosingBalance.concat(",1"));
					}
					if (originalDayBookAmountRemarks == "null") {
					} else {
						if (originalDayBookAmountRemarks.equals(changedDayBookAmountRemarks)) {
							vbDayBookChangeRequestAmount.setAmountsRemarks(originalDayBookAmountRemarks.concat(",0"));
						} else {
							vbDayBookChangeRequestAmount.setAmountsRemarks(changedDayBookAmountRemarks.concat(",1"));
						}
					}
					vbDayBookChangeRequestAmount.setCustomerTotalCredit(dayBookAmountCommand.getCustomerTotalCredit());
					vbDayBookChangeRequestAmount.setCustomerTotalPayable(dayBookAmountCommand.getCustomerTotalPayable());
					vbDayBookChangeRequestAmount.setCustomerTotalReceived(dayBookAmountCommand.getCustomerTotalReceived());

					originalExecutiveAllowances = Float.toString(originalDayBookAmount.getExecutiveAllowances());
					originalDriverAllowances = Float.toString(originalDayBookAmount.getDriverAllowances());
					originalVehicleFuelExpences = Float.toString(originalDayBookAmount.getVehicleFuelExpenses());
					originalOffloadingCharges = Float.toString(originalDayBookAmount.getOffloadingLoadingCharges());
					originalVehicleMaintenanceExpences = Float.toString(originalDayBookAmount.getVehicleMaintenanceExpenses());
					originalMiscellaneousExpenses = Float.toString(originalDayBookAmount.getMiscellaneousExpenses());
					originalDealerPartyExpences = Float.toString(originalDayBookAmount.getDealerPartyExpenses());
					originalMunicipalCityCouncil = Float.toString(originalDayBookAmount.getMunicipalCityCouncil());
					originalTotalAllowances = Float.toString(originalDayBookAmount.getTotalAllowances());
					originalAllowancesRemarks = originalDayBookAmount.getAllowancesRemarks();

					changedExecutiveAllowances = dayBookAllowancesCommand.getExecutiveAllowances();
					changedDriverAllowances = dayBookAllowancesCommand.getDriverAllowances();
					changedVehicleFuelExpences = dayBookAllowancesCommand.getVehicleFuelExpenses();
					changedOffloadingCharges = dayBookAllowancesCommand.getOffloadingLoadingCharges();
					changedVehicleMaintenanceExpences = dayBookAllowancesCommand.getVehicleMaintenanceExpenses();
					changedMiscellaneousExpenses = dayBookAllowancesCommand.getMiscellaneousExpenses();
					changedDealerPartyExpences = dayBookAllowancesCommand.getDealerPartyExpenses();
					changedMunicipalCityCouncil = dayBookAllowancesCommand.getMunicipalCityCouncil();
					changedTotalAllowances = dayBookAllowancesCommand.getTotalAllowances();
					changedAllowancesRemarks = dayBookAllowancesCommand.getAllowancesCRRemarks();

					// Day book Allowances
					if (originalExecutiveAllowances.replace(".0", "").equals(changedExecutiveAllowances)) {
						vbDayBookChangeRequestAmount.setExecutiveAllowances(originalExecutiveAllowances.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setExecutiveAllowances(changedExecutiveAllowances.concat(",1"));
					}

					if (originalDriverAllowances.replace(".0", "").equals(changedDriverAllowances)) {
						vbDayBookChangeRequestAmount.setDriverAllowances(originalDriverAllowances.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setDriverAllowances(changedDriverAllowances.concat(",1"));
					}

					if (originalVehicleFuelExpences.replace(".0", "").equals(changedVehicleFuelExpences)) {
						vbDayBookChangeRequestAmount.setVehicleFuelExpenses(originalVehicleFuelExpences.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setVehicleFuelExpenses(changedVehicleFuelExpences.concat(",1"));
					}

					if (originalOffloadingCharges.replace(".0", "").equals(changedOffloadingCharges)) {
						vbDayBookChangeRequestAmount.setOffloadingLoadingCharges(originalOffloadingCharges.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setOffloadingLoadingCharges(changedOffloadingCharges.concat(",1"));
					}

					if (originalVehicleMaintenanceExpences.replace(".0", "").equals(changedVehicleMaintenanceExpences)) {
						vbDayBookChangeRequestAmount.setVehicleMaintenanceExpenses(originalVehicleMaintenanceExpences.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setVehicleMaintenanceExpenses(changedVehicleMaintenanceExpences.concat(",1"));
					}

					if (originalMiscellaneousExpenses.replace(".0", "").equals(changedMiscellaneousExpenses)) {
						vbDayBookChangeRequestAmount.setMiscellaneousExpenses(originalMiscellaneousExpenses.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setMiscellaneousExpenses(changedMiscellaneousExpenses.concat(",1"));
					}

					if (originalDealerPartyExpences.replace(".0", "").equals(changedDealerPartyExpences)) {
						vbDayBookChangeRequestAmount.setDealerPartyExpenses(originalDealerPartyExpences.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setDealerPartyExpenses(changedDealerPartyExpences.concat(",1"));
					}

					if (originalMunicipalCityCouncil.replace(".0", "").equals(changedMunicipalCityCouncil)) {
						vbDayBookChangeRequestAmount.setMunicipalCityCouncil(originalMunicipalCityCouncil.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setMunicipalCityCouncil(changedMunicipalCityCouncil.concat(",1"));
					}

					if (originalTotalAllowances.replace(".0", "").equals(changedTotalAllowances)) {
						vbDayBookChangeRequestAmount.setTotalAllowances(originalTotalAllowances.concat(",0"));
					} else {
						vbDayBookChangeRequestAmount.setTotalAllowances(changedTotalAllowances.concat(",1"));
					}

					if (originalAllowancesRemarks == "null") {
					} else {
						if (originalAllowancesRemarks.equals(changedAllowancesRemarks)) {
							vbDayBookChangeRequestAmount.setAllowancesRemarks(originalAllowancesRemarks.concat(",0"));
						} else {
							vbDayBookChangeRequestAmount.setAllowancesRemarks(changedAllowancesRemarks.concat(",1"));
						}
					}
					vbDayBookChangeRequestAmount.setVbDayBookChangeRequest(vbDayBookChangeRequest);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbDayBookChangeRequestAmount: {}", vbDayBookChangeRequestAmount);
					}
					session.saveOrUpdate(vbDayBookChangeRequestAmount);
				}
			}
			// fetch Day Book original Products Details and compared with Day
			// Book changed products details
			String originalProductToFactory = null;
			String changedProductToFactory = null;
			String originalProductRemarks = null;
			String changedProductRemarks = null;
			String originalClosingStock = null;
			String changedClosingStock = null;
			String originalProductName = null;
			String originalBatchNumber = null;
			List<VbDayBookProducts> originalDayBookProductsDetails = new ArrayList<VbDayBookProducts>(originalDayBook.getVbDayBookProductses());
			if (dayBookProductsCommand != null) {
				VbDayBookChangeRequestProducts vbDayBookChangeRequestProducts = null;
				for (ChangeRequestDayBookProductsCommand products : dayBookProductsCommand) {
					for (VbDayBookProducts originalProducts : originalDayBookProductsDetails) {
						originalProductName = originalProducts.getProductName();
						originalBatchNumber = originalProducts.getBatchNumber();
						originalProductToFactory = Integer.toString(originalProducts.getProductsToFactory());
						originalProductRemarks = originalProducts.getProductsRemarks();
						changedProductToFactory = products.getProductsToFactory();
						changedProductRemarks = products.getProductsCRRemarks();
						originalClosingStock = Integer.toString(originalProducts.getClosingStock());
						changedClosingStock = products.getClosingStock();
						vbDayBookChangeRequestProducts = new VbDayBookChangeRequestProducts();
						if (vbDayBookChangeRequestProducts != null) {
							if (originalProductName.equalsIgnoreCase(products.getProductName()) && originalBatchNumber.equalsIgnoreCase(products.getBatchNumber())) {
								if (originalProductToFactory.equals(changedProductToFactory)) {
									vbDayBookChangeRequestProducts.setProductsToFactory(originalProductToFactory.concat(",0"));
									vbDayBookChangeRequestProducts.setClosingStock(originalClosingStock.concat(",0"));
								} else {
									vbDayBookChangeRequestProducts.setProductsToFactory(changedProductToFactory.concat(",1"));
									vbDayBookChangeRequestProducts.setClosingStock(changedClosingStock.concat(",1"));
								}
								if (originalProductRemarks == "null") {
								} else {
									if (originalProductRemarks.equals(changedProductRemarks)) {
										vbDayBookChangeRequestProducts.setProductRemarks(originalProductRemarks.concat(",0"));
									} else {
										vbDayBookChangeRequestProducts.setProductRemarks(changedProductRemarks.concat(",1"));
									}
								}
								vbDayBookChangeRequestProducts.setOpeningStock(products.getOpeningStock().concat(",0"));
								vbDayBookChangeRequestProducts.setProductName(products.getProductName().concat(",0"));
								vbDayBookChangeRequestProducts.setProductsToCustomer(products.getProductsToCustomer().concat(",0"));
								vbDayBookChangeRequestProducts.setBatchNumber(products.getBatchNumber().concat(",0"));

								vbDayBookChangeRequestProducts.setVbDayBookChangeRequest(vbDayBookChangeRequest);
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting vbDayBookChangeRequestProducts: {}", vbDayBookChangeRequestProducts);
								}
								session.saveOrUpdate(vbDayBookChangeRequestProducts);
							}// if
						}// /if condition
					}// inner foreach
				}// outer foreach
			}
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, ENotificationTypes.DB_TXN_CR.name(),
					CRStatus.PENDING.name(), null, null);
			txn.commit();
		} catch(HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String errorMessage = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMessage);
			}
			throw new DataAccessException(errorMessage);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
/**
 * This method is used to get all the day books on criteria .
 * 
 * @param vbOrganization - {@link VbOrganization}
 * @param username - {@link String}
 * @return resultList - {@link ChangeRequestDayBookResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<ChangeRequestDayBookResult> getDayBookChangeTransaction(VbOrganization organization, String username) throws DataAccessException {
	Session session = this.getSession();
	Date date=new Date();
	Criteria criteria = session.createCriteria(VbDayBook.class);
	criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)));
	criteria.add(Restrictions.eq("createdBy", username));
	if (organization != null) {
		criteria.add(Restrictions.eq("vbOrganization", organization));
	}
	//criteria.add(Expression.eq("flag", new Integer(1)));
	criteria.addOrder(Order.desc("createdOn"));
	List<VbDayBook> vbDayBook = criteria.list();
	session.close();
	if(!vbDayBook.isEmpty()) {
		List<ChangeRequestDayBookResult> resultList=prepareResultList(vbDayBook);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultList: {}", resultList);
		}
		return resultList;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}

/**
 * This method is used to get the Day Book Based on daybook Id.
 * 
 * @param dayBookId - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return vbDayBook - {@link VbDayBook}
 * @throws DataAccessException - {@link DataAccessException}
 */
public VbDayBook getDayBook(Integer dayBookId,VbOrganization organization, String userName) throws DataAccessException {
	Session session = this.getSession();
	VbDayBook vbDayBook = (VbDayBook) session.get(VbDayBook.class, dayBookId);
	session.close();
	if(vbDayBook != null) {
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbDayBook: {}", vbDayBook);
		}
		return vbDayBook;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isErrorEnabled()) {
			_logger.error(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}
/**
 * This method is used to get original day books details for dayBook edit page on particular id for view.
 * 
 * @param id - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return resultList - {@link DayBookViewResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public DayBookViewResult getOriginalDayBookData(Integer id,VbOrganization organization, String userName) throws DataAccessException {
	Session session = this.getSession();
	List<VbDayBookAmount> amountList = null;
	DayBookViewResult result = null;
	VbDayBookAmount amount = null;
	VbDayBook vbDayBook = (VbDayBook) session.get(VbDayBook.class, id);
	if (vbDayBook != null) {
		Float startingReading = vbDayBook.getStartingReading();
		Float endingReading = vbDayBook.getEndingReading();
		Date currentDate=new Date();
		String vehicleNo = vbDayBook.getVehicleNo();
		String driverName = vbDayBook.getDriverName();
		Date createdDate = vbDayBook.getCreatedOn();
		String fullName = getEmployeeFullName(vbDayBook.getSalesExecutive(), organization);
		amountList = new ArrayList<VbDayBookAmount>(vbDayBook.getVbDayBookAmounts());
		amount = amountList.get(0);
		result = new DayBookViewResult();
		result.setDayBookNo(StringUtil.format(vbDayBook.getDayBookNo()));
		result.setSalesExecutive(fullName);
		result.setIsReturn(vbDayBook.getIsReturn());
		result.setAllotmentType(vbDayBook.getVbSalesBook().getAllotmentType());
		result.setCreatedDate(DateUtils.format(createdDate));
		result.setCurrentDate(DateUtils.format(currentDate));
		result.setOpeningBalance(StringUtil.currencyFormat(vbDayBook.getVbSalesBook().getOpeningBalance()));
		result.setReportingManager(StringUtil.format(vbDayBook.getReportingManager()));
		result.setStartingReading(startingReading);
		result.setEndingReading(endingReading);
		result.setDriverName(StringUtil.format(driverName));
		result.setVehicleNo(StringUtil.format(vehicleNo));
		
		result.setTotalPayable(StringUtil.currencyFormat(amount.getCustomerTotalPayable()));
		result.setTotalRecieved(StringUtil.currencyFormat(amount.getCustomerTotalReceived()));
		result.setAmountToFactory(StringUtil.currencyFormat(amount.getAmountToFactory()));
		result.setBalance(StringUtil.currencyFormat(amount.getCustomerTotalCredit()));
				
		result.setBasicInfoRemarks(StringUtil.format(vbDayBook.getDayBookRemarks()));
		result.setVehicleDetailRemarks(StringUtil.format(vbDayBook.getVehicleDetailsRemarks()));
		result.setAllowancesRemarks(StringUtil.format(amount.getAllowancesRemarks()));
		result.setAmountRemarks(StringUtil.format(amount.getAmountsRemarks()));		
			
		result.setId(vbDayBook.getId());
		result.setSalesBookId(vbDayBook.getVbSalesBook().getId());
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookViewResult: {}", result);
		}
		return result;
	} else {
		session.close();
		String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if(_logger.isWarnEnabled()) {
			_logger.warn(message);
		}
		throw new DataAccessException(message);
	}
}
/**
 * This method is responsible to get the advance value from {@link VbSalesBook}.
 * 
 * @param dayBookId - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @return advance - {@link Float}
 */
public Float getAdvance(Integer dayBookId, VbOrganization organization) {
	Session session = this.getSession();
	VbDayBook vbDayBook=(VbDayBook)session.get(VbDayBook.class, dayBookId);
	Float advance = (Float) session.createCriteria(VbSalesBook.class)
			.setProjection(Projections.property("openingBalance"))
			.add(Restrictions.eq("salesExecutive", vbDayBook.getSalesExecutive()))
			.add(Restrictions.eq("vbOrganization", organization))
			.add(Restrictions.eq("flag", new Integer(0)))
			.uniqueResult();
	session.close();
	if(advance == null) {
		advance = new Float("0.00");
	}
	return advance;
}

/**
 * This method is responsible to get the Sales Executive type for checking Day Book CR for Daily or Non Daily value from {@link VbSalesBook}.
 * 
 * @param dayBookId - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @return advance - {@link Float}
 * @throws DataAccessException - {@link DataAccessException}
 */
public VbSalesBook getSalesExecutiveType(Integer dayBookId, VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	VbDayBook vbDayBook = (VbDayBook)session.get(VbDayBook.class, dayBookId);
	if(vbDayBook != null) {
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", vbDayBook.getSalesExecutive()))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(0)))
				.uniqueResult();
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Fetching sales executive type based on the daybook id");
		}
		return vbSalesBook;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isErrorEnabled()) {
			_logger.error(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}

/**
 * This method is responsible to prepare {@link DayBookResult} list.
 * 
 * @param dayBookList - {@link List}
 * @return - {@link List}
 * 
 */
private List<ChangeRequestDayBookResult> prepareResultList(List<VbDayBook> dayBookList) {
	List<ChangeRequestDayBookResult> dayBookResultList = new ArrayList<ChangeRequestDayBookResult>();
	ChangeRequestDayBookResult dayBook = null;
	for(VbDayBook result:dayBookList){
		dayBook=new ChangeRequestDayBookResult();
		dayBook.setId(result.getId());
		dayBook.setCreatedOn(result.getCreatedOn());
		dayBook.setSalesExecutive(result.getSalesExecutive());
		dayBookResultList.add(dayBook);
	}
	if (_logger.isDebugEnabled()) {
		_logger.debug("dayBookResultList: {}", dayBookResultList);
	}
	return dayBookResultList;
}
/**
 * This method is used to get the grid data into day book page based on the salesExecutive and the organization.
 * 
 * @param salesExecutive - {@link String}
 * @param dayBookId - {@link Integer}
 * @param userName - {@link String}
 * @param organization - {@link VbOrganization}
 * @return List - {@link ChangeRequestDayBookResult}
 * @throws DataAccessException  - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<ChangeRequestDayBookResult> getGridData(String salesExecutive,
		Integer dayBookId, String userName, VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	List<ChangeRequestDayBookResult> dayBookResultList = null;
	// if SE is non-daily load SE data with flag=0 not with flag=1 from vbSalesBook
	VbSalesBook vbSalesBook = getVbSalesBookNoFlag(session, userName,	organization);
	if (vbSalesBook.getAllotmentType().equalsIgnoreCase("non-daily")) {
		vbSalesBook = getVbSalesBookNonDailySEFlag(session, userName,	organization);
	}
	List<VbDayBookProducts> productList = session.createCriteria(VbDayBookProducts.class)
			.createAlias("vbDayBook", "vb")
			.add(Restrictions.eq("vb.id", dayBookId))
			.add(Restrictions.eq("vb.vbOrganization", organization))
			.addOrder(Order.asc("productName")).list();
	if (!productList.isEmpty()) {
		Integer returnQty = null;
		ChangeRequestDayBookResult dayBookResult = null;
		dayBookResultList = new ArrayList<ChangeRequestDayBookResult>();
        String productRemarks=null;
		for (VbDayBookProducts vbDayBookProducts : productList) {
			dayBookResult = new ChangeRequestDayBookResult();
			returnQty = (Integer) session.createQuery("SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb "
									+ "WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND "
									+ "vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName AND vb.batchNumber = :batchNumber AND vb.vbSalesReturn.flag = :flag GROUP BY vb.productName, vb.batchNumber")
					.setParameter("vbOrganization", organization)
					.setParameter("vbSalesBook", vbSalesBook)
					.setParameter("productName", vbDayBookProducts.getProductName())
					.setParameter("batchNumber", vbDayBookProducts.getBatchNumber())
					.setParameter("flag", new Integer(1))
					.uniqueResult();

			if (returnQty != null) {
				dayBookResult.setReturnQty(StringUtil.format(returnQty));
			} else {
				dayBookResult.setReturnQty(StringUtil.format(new Integer(0)));
			}
			dayBookResult.setProductName(vbDayBookProducts.getProductName());
			dayBookResult.setBatchNumber(vbDayBookProducts.getBatchNumber());
			dayBookResult.setOpeningStock(StringUtil.format(vbDayBookProducts.getOpeningStock()));
			dayBookResult.setProductsToCustomer(StringUtil.format(vbDayBookProducts.getProductsToCustomer()));
			dayBookResult.setProductsToFactory(StringUtil.format(vbDayBookProducts.getProductsToFactory()));
			dayBookResult.setClosingStock(StringUtil.format(vbDayBookProducts.getClosingStock()));
			productRemarks = vbDayBookProducts.getProductsRemarks();
			dayBookResult.setProductsRemarks(productRemarks);
			dayBookResultList.add(dayBookResult);
		}
		session.close();
			
		if (_logger.isDebugEnabled()) {
			_logger.debug("DayBookResultList: {}", dayBookResultList);
		}
		return dayBookResultList;
	} else {
		session.close();
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}


/**
 * This method is used to get the opening balance based on the sales
 * executive.
 * 
 * @param salesExecutive - {@link String}
 * @param organization - {@link VbOrganization}
 * @return openingBal - {@link Float}
 */
public Float getOpeningBalance(String salesExecutive , VbOrganization organization) {
	Session session = this.getSession();
	Query query = session.createQuery("SELECT vb.openingBalance FROM VbSalesBook as vb WHERE vb.salesExecutive = :salesExecutive AND vb.vbOrganization = :vbOrganization AND vb.createdOn IN (SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb)");
	query.setParameter("salesExecutive", salesExecutive);
	query.setParameter("vbOrganization", organization);
	Float openingBal = getSingleResultOrNull(query);
	session.close();
	if(openingBal == null) {
		openingBal = new Float("0.00");
	}
	if (_logger.isDebugEnabled()) {
		_logger.debug("Opening Balance : {}", openingBal);
	}
	return openingBal;
}
/**
 * This Method is Responsible to Check whether The Searched result is
 * Applicable for SE CR or not.
 * 
 * @param dayBookId - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @return isAvailable - {@link String}
 */
public String validateSEDayBookChangeRequest(Integer dayBookId,VbOrganization organization) {
	String isAvailable = "y";
	Session session = this.getSession();
	Date date = new Date();
	VbDayBookChangeRequest changeRequest = null;
	VbDayBook vbDayBook = (VbDayBook)session.get(VbDayBook.class, dayBookId);
	if(vbDayBook != null) {
	 changeRequest = (VbDayBookChangeRequest)session.createCriteria(VbDayBookChangeRequest.class)
			.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
			.add(Restrictions.eq("flag", new Integer(1)))
			.add(Restrictions.eq("createdBy",vbDayBook.getCreatedBy()))
			.add(Expression.eq("vbOrganization", organization)).uniqueResult();
   }
	if(changeRequest != null) {
		VbDayBookChangeRequest changeRequestDayBook =(VbDayBookChangeRequest)session.createCriteria(VbDayBookChangeRequest.class)
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.add(Expression.eq("dayBookNo", vbDayBook.getDayBookNo()))
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
		if(changeRequestDayBook !=null) {
			isAvailable = "y";
		} else {
			isAvailable = "n";
		}
	} else {
		isAvailable = "n";
	}
	session.close();
	return isAvailable;
}
/**
 * This method is responsible to get the daybook CR Results.
 * @param username - {@link String}
 * @param organization - {@link VbOrganization}
 * @return resultList - {@link ChangeRequestDayBookResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<ChangeRequestDayBookResult> getDayBookCrResults(String username, VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	List<VbDayBookChangeRequest> vbDayBookChangeRequest = session.createCriteria(VbDayBookChangeRequest.class)
			.add(Restrictions.eq("status", CRStatus.PENDING.name()))
			.add(Restrictions.eq("vbOrganization", organization))
			.addOrder(Order.asc("createdOn"))
			.list();
	session.close();
	if(!vbDayBookChangeRequest.isEmpty()) {
		List<ChangeRequestDayBookResult> resultList=prepareDayBookCrResultList(vbDayBookChangeRequest);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultList: {}", resultList);
		}
		return resultList;
	} else {
		session.close();
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}
/**
 * This method is responsible to prepare {@link DayBookResult} list.
 * 
 * @param dayBookList - {@link List}
 * @return - {@link List}
 * 
 */
private List<ChangeRequestDayBookResult> prepareDayBookCrResultList(List<VbDayBookChangeRequest> dayBookList){
	List<ChangeRequestDayBookResult> dayBookResultList = new ArrayList<ChangeRequestDayBookResult>();
	ChangeRequestDayBookResult dayBook = null;
	for(VbDayBookChangeRequest result:dayBookList){
		dayBook=new ChangeRequestDayBookResult();
		dayBook.setId(result.getId());
		dayBook.setCreatedOn(result.getCreatedOn());
		dayBook.setSalesExecutive(result.getSalesExecutive());
		dayBookResultList.add(dayBook);
	}
	if (_logger.isDebugEnabled()) {
		_logger.debug("dayBookResultList: {}", dayBookResultList);
	}
	return dayBookResultList;
}

/**
 * This method is used to get day books CR on particular DayBook CR Id .
 * 
 * @param dayBookCRId - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @return vbDayBookChangeRequest - {@link VbDayBookChangeRequest}
 * @throws DataAccessException - {@link DataAccessException}
 */
/*public VbDayBookChangeRequest getDayBookOnCRId(Integer dayBookCRId , VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	VbDayBookChangeRequest vbDayBookChangeRequest = (VbDayBookChangeRequest) session.createCriteria(VbDayBookChangeRequest.class)
			.add(Restrictions.eq("id", dayBookCRId))
			.add(Restrictions.eq("vbOrganization", organization))
			.uniqueResult();
	//session.close();
	if(vbDayBookChangeRequest != null) {
		if(_logger.isDebugEnabled()) {
			_logger.debug("Fetching the Daybook based on the daybook cr id");
		}
		return vbDayBookChangeRequest;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}*/
/**
 * This method is used to get day books change request data on particular id for dashboard result and history view.
 * 
 * @param id - {@link Integer}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return resultList - {@link DayBookViewResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<DayBookViewResult> getDayBookChangeRequestOnId(Integer id,VbOrganization organization, String userName) throws DataAccessException {
	List<VbDayBookChangeRequestProducts> productList = null;
	List<VbDayBookChangeRequestAmount> amountList = null;
	List<DayBookViewResult> resultList = null;
	VbDayBookChangeRequestAmount amount = null;
	Session session = this.getSession();
	VbDayBookChangeRequest vbDayBookChangeRequest = (VbDayBookChangeRequest) session.get(VbDayBookChangeRequest.class, id);
	if (vbDayBookChangeRequest != null) {
		
		//String salesExecutiveStartDate=getSalesExecutiveStartDate(vbDayBook.getSalesExecutive(), organization);
		String startingReading = vbDayBookChangeRequest.getStartingReading();
		String endingReading = vbDayBookChangeRequest.getEndingReading();
		String vehicleNo = vbDayBookChangeRequest.getVehicleNo();
		String remarks = vbDayBookChangeRequest.getDayBookRemarks();
		String driverName = vbDayBookChangeRequest.getDriverName();
		Date createdDate = vbDayBookChangeRequest.getCreatedOn();
		String fullName = getEmployeeFullName(vbDayBookChangeRequest.getSalesExecutive(), organization);
		productList = new ArrayList<VbDayBookChangeRequestProducts>(vbDayBookChangeRequest.getVbDayBookChangeRequestProductses());
		amountList = new ArrayList<VbDayBookChangeRequestAmount>(vbDayBookChangeRequest.getVbDayBookChangeRequestAmounts());
		amount = amountList.get(0);
		resultList = new ArrayList<DayBookViewResult>();
		if (!productList.isEmpty()) {
			String productName = null;
			String batchNumber = null;
			DayBookViewResult result = null;
			for (VbDayBookChangeRequestProducts product : productList) {
				result = new DayBookViewResult();
				result.setDayBookNo(StringUtil.format(vbDayBookChangeRequest.getDayBookNo()));
				result.setId(vbDayBookChangeRequest.getId());
				result.setSalesExecutive(fullName);
				result.setIsReturn(vbDayBookChangeRequest.getIsReturn());
				result.setReportingManager(StringUtil.format(vbDayBookChangeRequest.getReportingManager()));
				result.setCreatedDate(DateUtils.format(createdDate));
				result.setOpeningBalance(StringUtil.currencyFormat(vbDayBookChangeRequest.getVbSalesBook().getOpeningBalance()));
				result.setTotalExpenses(amount.getTotalAllowances());
				result.setTotalPayable(amount.getCustomerTotalPayable());
				result.setTotalRecieved(amount.getCustomerTotalReceived());
				result.setBalance(amount.getCustomerTotalCredit());
				result.setAmountToBank(amount.getAmountToBank());
				result.setAmountToFactory(amount.getAmountToFactory());
				result.setClosingBalance(amount.getClosingBalance());
				result.setCrDescription(vbDayBookChangeRequest.getCrDescription());
				productName = product.getProductName();
				batchNumber = product.getBatchNumber();
				result.setProduct(productName);
				result.setBatchNumber(batchNumber);
				result.setCrOpeningStock(product.getOpeningStock());
				result.setCrProductsToCustomer(product.getProductsToCustomer());
				result.setCrProductsToFactory(product.getProductsToFactory());
				result.setCrClosingStock(product.getClosingStock());
				
				result.setCrStartingReading(startingReading);
				result.setCrEndingReading(endingReading);
				result.setDriverName(StringUtil.format(driverName));
				result.setRemarks(StringUtil.format(remarks));
				result.setVehicleNo(StringUtil.format(vehicleNo));
				
				result.setBasicInfoRemarks(StringUtil.format(vbDayBookChangeRequest.getDayBookRemarks()));
				result.setVehicleDetailRemarks(StringUtil.format(vbDayBookChangeRequest.getVehicleDetailsRemarks()));
				result.setAllowancesRemarks(StringUtil.format(amount.getAllowancesRemarks()));
				result.setAmountRemarks(StringUtil.format(amount.getAmountsRemarks()));
				result.setProductsRemarks(StringUtil.format(product.getProductRemarks()));
				
				resultList.add(result);
			}
		}
		//Collections.sort(resultList);
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} records found.", resultList.size());
		}
		return resultList;
	} else {
		session.close();
		String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if(_logger.isWarnEnabled()) {
			_logger.warn(message);
		}
		throw new DataAccessException(message);
	}
}
/**
 * This method is responsible for Approving Day Book CR.
 * 
 * @param dayBookCrId - {@link Integer}
 * @param status - {@link String}
 * @param vbOrganization - {@link VbOrganization}
 * @param userName - {@link String}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public void getApprovedDayBookCR(Integer dayBookCrId, String status, VbOrganization vbOrganization,String userName) throws DataAccessException {
	Session session = this.getSession();
	VbDayBookChangeRequest vbDayBookChangeRequest = (VbDayBookChangeRequest) session.get(VbDayBookChangeRequest.class, dayBookCrId);
	if(vbDayBookChangeRequest!=null) {
		Transaction txn = null;
		try {
			txn = session.beginTransaction();
			vbDayBookChangeRequest.setModifiedBy(userName);
			vbDayBookChangeRequest.setModifiedOn(new Date());
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				//update DayBook Tables from flag=1 to flag=0
				VbDayBook vbDayBook=(VbDayBook)session.createCriteria(VbDayBook.class)
						.add(Restrictions.eq("dayBookNo", vbDayBookChangeRequest.getDayBookNo()))
						.add(Restrictions.eq("salesExecutive", vbDayBookChangeRequest.getSalesExecutive()))
						.add(Restrictions.eq("createdBy", vbDayBookChangeRequest.getCreatedBy()))
						.add(Expression.eq("vbOrganization", vbOrganization))
						.uniqueResult();
				if(vbDayBook != null) {
					//fetching DayBook CR data to update original day Book Data
					List<VbDayBookChangeRequestAmount> amountSet= new ArrayList<VbDayBookChangeRequestAmount>(vbDayBookChangeRequest.getVbDayBookChangeRequestAmounts());
					List<VbDayBookChangeRequestProducts> productsSet = new ArrayList<VbDayBookChangeRequestProducts>(vbDayBookChangeRequest.getVbDayBookChangeRequestProductses());
					//fetching original Day Book Data to maintain histroy in day book CR tables
					List<VbDayBookAmount> originalDayBookAmount= new ArrayList<VbDayBookAmount>(vbDayBook.getVbDayBookAmounts());
					List<VbDayBookProducts> originalProductSet = new ArrayList<VbDayBookProducts>(vbDayBook.getVbDayBookProductses());
					VbSalesBook vbSalesBook = getVbSalesBook(session, vbDayBook.getCreatedBy(), vbOrganization);
					if (vbSalesBook == null) {
						vbSalesBook = getVbSalesBookNoFlag(session, vbDayBookChangeRequest.getCreatedBy(), vbOrganization);
					}
					 //persist vb_day_book,vb_day_book_products,vb_day_book_amounts with Day Book CR data with flag=0
					persistOriginalDayBookDetails(session,vbDayBook,vbSalesBook,originalProductSet,originalDayBookAmount,vbOrganization,userName);
					 //persist vb_day_book,vb_day_book_products,vb_day_book_amounts with Day Book CR data with flag=1
					updateOriginalDayBookDetails(session,vbDayBook,vbDayBookChangeRequest,vbSalesBook,productsSet,amountSet,vbOrganization,userName);
					//update SE salesBook details (closing balance,and closing product qty based on CR'd data if CR has been made for SE daily and non-daily case.			     
				  //check SE allotment type
					if(vbSalesBook.getAllotmentType().equalsIgnoreCase("daily")) {
						//update vbSalesBook abd vbSalesBookProduct based on CR'd Data
						for(VbDayBookAmount updatedAmount : originalDayBookAmount){
						    vbSalesBook.setClosingBalance(updatedAmount.getClosingBalance());
						    vbSalesBook.setModifiedOn(new Date());
						    vbSalesBook.setModifiedBy(userName);
						}
						if(_logger.isDebugEnabled()){
							_logger.debug("VbSalesBook updated for daily SE.",vbSalesBook);
						}
						session.update(vbSalesBook);
						//update vbSalesBookProduct based on changed product in Day book CR for daily SE
						List<VbDayBookProducts> productsList = new ArrayList<VbDayBookProducts>(vbDayBook.getVbDayBookProductses());
						List<VbSalesBookProducts> productsSalesList = new ArrayList<VbSalesBookProducts>(vbSalesBook.getVbSalesBookProductses());
							for (int i = 0; i < productsList.size(); i++) {
								VbDayBookProducts products = productsList.get(i);
								for (int j = 0; j < productsSalesList.size(); j++) {
									VbSalesBookProducts salesBookProducts = productsSalesList.get(i);
									salesBookProducts.setQtyClosingBalance(products.getClosingStock());
									if (_logger.isDebugEnabled()) {
										_logger.debug("Updating VbsalesBookProducts for daily SE.",	salesBookProducts);
									}
									session.update(salesBookProducts);
								}
							}
					} else {
						//update closing balance of SE for non-daily type and products will genarate as system alloted.
						VbSalesBook nonDailySEflag=getVbSalesBookNoFlag(session, vbSalesBook.getSalesExecutive(), vbOrganization);
						for(VbDayBookAmount updatedAmount : originalDayBookAmount){
							nonDailySEflag.setClosingBalance(updatedAmount.getClosingBalance());
							nonDailySEflag.setOpeningBalance(updatedAmount.getClosingBalance());
							nonDailySEflag.setModifiedOn(new Date());
							nonDailySEflag.setModifiedBy(userName);
						}
						if(_logger.isDebugEnabled()){
							_logger.debug("VbSalesBook updated for non-daily SE.",nonDailySEflag);
						}
						session.update(nonDailySEflag);
					}
					//update vb_cash_day_book by changed Cash Day Book Details in Cash_Day_Book_CR details
					List<VbCashDayBookCr> cashBookDayBookCRList=new ArrayList<VbCashDayBookCr>();
					if(vbSalesBook.getAllotmentType().equalsIgnoreCase("non-daily")) {
						vbSalesBook=getVbSalesBookNonDailySEFlag(session,vbSalesBook.getSalesExecutive(),vbOrganization);
					}
					cashBookDayBookCRList=session.createCriteria(VbCashDayBookCr.class)
							.add(Restrictions.eq("vbSalesBook", vbSalesBook))
							.add(Restrictions.eq("vbOrganization", vbOrganization))
							.add(Restrictions.eq("createdBy", vbDayBookChangeRequest.getCreatedBy())).list();
					if(cashBookDayBookCRList != null) {
						for(VbCashDayBookCr cashDayBookCR : cashBookDayBookCRList) {
							VbCashDayBook cashDayBook=(VbCashDayBook) session.get(VbCashDayBook.class, cashDayBookCR.getVbCashDayBook().getId());
							if(cashDayBook.getDayBookType().equalsIgnoreCase(cashDayBookCR.getDayBookType())) {
							cashDayBook.setValueOne(cashDayBookCR.getValueOne());
							cashDayBook.setValueThree(cashDayBookCR.getValueThree());
							session.update(cashDayBook);
							}
						}
						if(_logger.isDebugEnabled()) {
							_logger.debug("Cash Day Book Updated Successfully by Cash Day Book CR data.",cashBookDayBookCRList);
						}
					} else {
						if(_logger.isDebugEnabled()) {
							_logger.debug("Day Book Cash CR not found.",cashBookDayBookCRList);
						}
					}
				} else {
					if(_logger.isDebugEnabled()) {
						_logger.debug("Day Book not found with Day Book CR id.",vbDayBook);
					}
				}
				vbDayBookChangeRequest.setStatus(CRStatus.APPROVED.name());
			} else {
				vbDayBookChangeRequest.setStatus(CRStatus.DECLINE.name());
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating vbDayBookChangeRequest for approved.");
			}
			session.update(vbDayBookChangeRequest);
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, vbDayBookChangeRequest.getSalesExecutive(), vbOrganization, 
					ENotificationTypes.DB_TXN_CR.name(), status, vbDayBookChangeRequest.getDayBookNo(), null);
			txn.commit();
		} catch(HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String errorMessage = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMessage);
			}
			throw new DataAccessException(errorMessage);
		} finally {
			if (session != null) {
				session.close();
			}
		}
    } else {
    	if (session != null) {
			session.close();
		}
    	String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if (_logger.isErrorEnabled()) {
			_logger.error(errorMsg);
		}
		throw new DataAccessException(errorMsg);
    }
}
/**This method is responsible for persisting original dayBook Details before updating all details by DayBook CR to CR tables with flag=0
 * 
 * @param vbDayBook - {@link VbDayBook}
 * @param vbSalesBook - {@link VbSalesBook}
 * @param originalProductSet - {@link VbDayBookProducts}
 * @param originalDayBookAmount - {@link VbDayBookAmount}
 * @param vbOrganization - {@link VbOrganization}
 * @param userName - {@link String}
 */
private void persistOriginalDayBookDetails(Session session,VbDayBook vbDayBook,VbSalesBook vbSalesBook,List<VbDayBookProducts> originalProductSet,List<VbDayBookAmount> originalDayBookAmount,
		VbOrganization vbOrganization, String userName) {
	Date date=new Date();
	VbDayBookChangeRequest dayBookCR = new VbDayBookChangeRequest();
	dayBookCR.setVbSalesBook(vbSalesBook);
	dayBookCR.setCreatedBy(vbDayBook.getCreatedBy());
	dayBookCR.setCreatedOn(date);
	dayBookCR.setModifiedOn(date);
	dayBookCR.setModifiedBy(userName);
	dayBookCR.setSalesExecutive(vbDayBook.getCreatedBy());
	dayBookCR.setReportingManager(vbDayBook.getReportingManager());
	dayBookCR.setVehicleDetailsRemarks(vbDayBook.getVehicleDetailsRemarks());
	dayBookCR.setVehicleNo(vbDayBook.getVehicleNo());
	dayBookCR.setDriverName(vbDayBook.getDriverName());
	dayBookCR.setStartingReading(Float.toString(vbDayBook.getStartingReading()));
	dayBookCR.setEndingReading(Float.toString(vbDayBook.getEndingReading()));
	dayBookCR.setDayBookRemarks(vbDayBook.getDayBookRemarks());
	dayBookCR.setIsReturn(vbDayBook.getIsReturn());
	dayBookCR.setFlag(new Integer(0));
	dayBookCR.setStatus(CRStatus.APPROVED.name());
	dayBookCR.setDayBookNo(vbDayBook.getDayBookNo());
	dayBookCR.setVbOrganization(vbOrganization);

	if (_logger.isDebugEnabled()) {
		_logger.debug("Persisting Day Book Change Rquest with Original Day Book data");
	}
	session.save(dayBookCR);
	//persist vb_day_book_products
	
    for(VbDayBookProducts products : originalProductSet) {
    	VbDayBookChangeRequestProducts dayBookCRProducts = new VbDayBookChangeRequestProducts();
		dayBookCRProducts.setProductName(products.getProductName());
		dayBookCRProducts.setBatchNumber(products.getBatchNumber());
		dayBookCRProducts.setOpeningStock(Integer.toString(products.getOpeningStock()));
        dayBookCRProducts.setProductsToCustomer(Integer.toString(products.getProductsToCustomer()));
        dayBookCRProducts.setProductsToFactory(Integer.toString(products.getProductsToFactory()));
        dayBookCRProducts.setClosingStock(Float.toString(products.getClosingStock()));
        dayBookCRProducts.setProductRemarks(products.getProductsRemarks());
        dayBookCRProducts.setVbDayBookChangeRequest(dayBookCR);
        if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting dayBookCRProducts: {}", dayBookCRProducts);
		}
		session.save(dayBookCRProducts);
    }
	
	for(VbDayBookAmount originalAmounts : originalDayBookAmount){
		VbDayBookChangeRequestAmount dayBookCRAmount = new VbDayBookChangeRequestAmount();
		dayBookCRAmount.setExecutiveAllowances(Float.toString(originalAmounts.getExecutiveAllowances()));
		dayBookCRAmount.setDriverAllowances(Float.toString(originalAmounts.getDriverAllowances()));
		dayBookCRAmount.setVehicleFuelExpenses(Float.toString(originalAmounts.getVehicleFuelExpenses()));
		//dayBookCRAmount.setVehicleMeterReading(Float.toString(originalAmounts.getVehicleMeterReading()));
		dayBookCRAmount.setVehicleMaintenanceExpenses(Float.toString(originalAmounts.getVehicleMaintenanceExpenses()));
		dayBookCRAmount.setMiscellaneousExpenses(Float.toString(originalAmounts.getMiscellaneousExpenses()));
		dayBookCRAmount.setDealerPartyExpenses(Float.toString(originalAmounts.getDealerPartyExpenses()));
		dayBookCRAmount.setMunicipalCityCouncil(Float.toString(originalAmounts.getMunicipalCityCouncil()));
		dayBookCRAmount.setTotalAllowances(Float.toString(originalAmounts.getTotalAllowances()));
		dayBookCRAmount.setAllowancesRemarks(originalAmounts.getAllowancesRemarks());
		//customer credits
		dayBookCRAmount.setAmountToBank(Float.toString(originalAmounts.getAmountToBank()));
		dayBookCRAmount.setAmountToFactory(Float.toString(originalAmounts.getAmountToFactory()));
		dayBookCRAmount.setClosingBalance(Float.toString(originalAmounts.getClosingBalance()));
		dayBookCRAmount.setAmountsRemarks(originalAmounts.getAmountsRemarks());
		dayBookCRAmount.setCustomerTotalCredit(Float.toString(originalAmounts.getCustomerTotalCredit()));
		dayBookCRAmount.setCustomerTotalPayable(Float.toString(originalAmounts.getCustomerTotalPayable()));
		dayBookCRAmount.setCustomerTotalReceived(Float.toString(originalAmounts.getCustomerTotalReceived()));
		dayBookCRAmount.setVbDayBookChangeRequest(dayBookCR);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting dayBookCRAmount: {}", dayBookCRAmount);
		}
		session.save(dayBookCRAmount);
	}
}

/**This method is responsible for updating Day Book original details(daybook,product,amounts) to vb_day_book,vb_day_book_products,vb_day_book_amount with flag=1
 * 
 * @param vbDayBook - {@link VbDayBook}
 * @param vbDayBookChangeRequest - {@link VbDayBookChangeRequest}
 * @param vbSalesBook - {@link VbSalesBook}
 * @param productsSet - {@link VbDayBookChangeRequestProducts}
 * @param amountSet - {@link VbDayBookChangeRequestAmount}
 * @param vbOrganization - {@link VbOrganization}
 * @param userName - {@link String}
 */
@SuppressWarnings("unchecked")
private void updateOriginalDayBookDetails(Session session,VbDayBook vbDayBook,VbDayBookChangeRequest vbDayBookChangeRequest, VbSalesBook vbSalesBook,List<VbDayBookChangeRequestProducts> productsSet,List<VbDayBookChangeRequestAmount> amountSet,
		VbOrganization vbOrganization, String userName) {
	String vehicleNo = vbDayBookChangeRequest.getVehicleNo();
	String reportingManager = vbDayBookChangeRequest.getReportingManager();
	String driverName = vbDayBookChangeRequest.getDriverName();
	String startingReading = vbDayBookChangeRequest.getStartingReading();
	String endingReading = vbDayBookChangeRequest.getEndingReading();
	String vehicleDetailsRemarks = vbDayBookChangeRequest.getVehicleDetailsRemarks();
	String dayBookRemarks = vbDayBookChangeRequest.getDayBookRemarks();
	//update Day book CR Vehicle Details
	if(vehicleNo.contains(",1")) {
		vbDayBook.setVehicleNo(vehicleNo.replace(",1", ""));
	}
	if(reportingManager.contains(",1")) {
		vbDayBook.setReportingManager(reportingManager.replace(",1", ""));
	}
	if(driverName.contains(",1")) {
		vbDayBook.setDriverName(driverName.replace(",1", ""));
	}
	if(startingReading.contains(",1")) {
		vbDayBook.setStartingReading(Float.parseFloat(startingReading.replace(",1", "")));
	}
	if(endingReading.contains(",1")) {
		vbDayBook.setEndingReading(Float.parseFloat(endingReading.replace(",1", "")));
	}
	if(vehicleDetailsRemarks != null) {
	if(vehicleDetailsRemarks.contains(",1")) {
		vbDayBook.setVehicleDetailsRemarks(vehicleDetailsRemarks.replace(",1", ""));
	}
	}
	if(dayBookRemarks.contains(",1")) {
		vbDayBook.setDayBookRemarks(dayBookRemarks.replace(",1", ""));
	}
	if(_logger.isDebugEnabled()) {
		_logger.debug("Updating Day Book", vbDayBook);
	}
	session.update(vbDayBook);
	
	//update VbDayBookProducts from VbDayBookCRProducts
	List<VbDayBookProducts> productsList = new ArrayList<VbDayBookProducts>(vbDayBook.getVbDayBookProductses());
	for(int i = 0;i < productsSet.size();i++) {
		VbDayBookChangeRequestProducts changedProducts=productsSet.get(i);
		for(int j=0;j<productsList.size();j++) {
			VbDayBookProducts products=productsList.get(i);
			//if(products.getProductName().equals(changedProductName) && (products.getBatchNumber().equals(changedBatchNumber))){
				if(changedProducts.getProductsToFactory().contains(",1")) {
				 products.setProductsToFactory(Integer.parseInt(changedProducts.getProductsToFactory().replace(",1", "")));
			 }
			 if(changedProducts.getClosingStock().contains(",1")) {
				 products.setClosingStock(Integer.parseInt(changedProducts.getClosingStock().replace(",1", "")));
			 }
			 if(_logger.isDebugEnabled()) {
					_logger.debug("Updating VBDayBookProducts.",products);
				}
			   session.update(products);
			  // break;
			}
	    }
	//update DayBook Allowances from VbDayBookAllowances CR Details
	List<VbDayBookAmount> amountList= new ArrayList<VbDayBookAmount>(vbDayBook.getVbDayBookAmounts());
	//updating vbDeliveryNotePayments
	VbDayBookAmount amount=null;
	String executiveAllowances = null;
	String offloadingCharges = null;
	String driverAllowances = null;
	String vehicleFuelExpenses = null;
	String vehicleMaintenanceExpenses = null;
	String miscellaneousExpenses = null;
	String dealerPartyExpenses = null;
	String muncipalCityCouncil = null;
	String totalAllowances = null;
	String amountToFactory = null;
	String amountToBank = null;
	String closingBalance = null;
	for(int i=0;i<amountSet.size();i++) {
		VbDayBookChangeRequestAmount changedAmounts = amountSet.get(i);
		for(int j=0;j<amountList.size();j++) {
			amount=amountList.get(i);
			executiveAllowances = changedAmounts.getExecutiveAllowances();
			offloadingCharges = changedAmounts.getOffloadingLoadingCharges();
			driverAllowances = changedAmounts.getDriverAllowances();
			vehicleFuelExpenses = changedAmounts.getVehicleFuelExpenses();
			vehicleMaintenanceExpenses = changedAmounts.getVehicleMaintenanceExpenses();
			miscellaneousExpenses = changedAmounts.getMiscellaneousExpenses();
			dealerPartyExpenses = changedAmounts.getDealerPartyExpenses();
			muncipalCityCouncil = changedAmounts.getMunicipalCityCouncil();
			totalAllowances = changedAmounts.getTotalAllowances();
			amountToFactory = changedAmounts.getAmountToFactory();
			amountToBank = changedAmounts.getAmountToBank();
			closingBalance = changedAmounts.getClosingBalance();
			if(executiveAllowances.contains(",1")){
				amount.setExecutiveAllowances(Float.parseFloat(executiveAllowances.replace(",1", "")));
			 }
			if(offloadingCharges.contains(",1")){
				amount.setOffloadingLoadingCharges(Float.parseFloat(offloadingCharges.replace(",1", "")));
			 }
			if(driverAllowances.contains(",1")){
				amount.setDriverAllowances(Float.parseFloat(driverAllowances.replace(",1", "")));
			 }
			if(vehicleFuelExpenses.contains(",1")){
				amount.setVehicleFuelExpenses(Float.parseFloat(vehicleFuelExpenses.replace(",1", "")));
			 }
			if(vehicleMaintenanceExpenses.contains(",1")){
				amount.setVehicleMaintenanceExpenses(Float.parseFloat(vehicleMaintenanceExpenses.replace(",1", "")));
			 }
			if(miscellaneousExpenses.contains(",1")){
				amount.setMiscellaneousExpenses(Float.parseFloat(miscellaneousExpenses.replace(",1", "")));
			 }
			if(dealerPartyExpenses.contains(",1")){
				amount.setDealerPartyExpenses(Float.parseFloat(dealerPartyExpenses.replace(",1", "")));
			 }
			if(muncipalCityCouncil.contains(",1")){
				amount.setMunicipalCityCouncil(Float.parseFloat(muncipalCityCouncil.replace(",1", "")));
			 }
			if(totalAllowances.contains(",1")){
				amount.setTotalAllowances(Float.parseFloat(totalAllowances.replace(",1", "")));
			 }
			//updating vb day book amount details
			if(amountToFactory.contains(",1")){
				amount.setAmountToFactory(Float.parseFloat(amountToFactory.replace(",1", "")));
			 }
			if(amountToBank.contains(",1")){
				amount.setAmountToBank(Float.parseFloat(amountToBank.replace(",1", "")));
			 }
			if(closingBalance.contains(",1")){
				amount.setClosingBalance(Float.parseFloat(closingBalance.replace(",1", "")));
			 }
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating VbDaybookAmount.",amount);
			}
			session.update(amount);
		}
	}
}

/** This method is responsible to get {@link VbDayBook } based on
 * {@link VbOrganization} and dayBookId.
 * 
 * @param dayBookId -{@link Integer}
 * @param organization -{@link VbOrganization}
 * @return isdayBookCRExpired -{@link String}
 * @throws ParseException - {@link ParseException}
 */
public String fetchDayBookCreationTime(Integer dayBookId,VbOrganization organization) throws ParseException {
	Session session = this.getSession();
	String isdayBookCRExpired = "";
	//HH converts hour in 24 hours format (0-23), day calculation
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	Date currentTime = format.parse(DateUtils.formatDateWithTimestamp(new Date()));
	VbDayBook vbDayBook = (VbDayBook)session.get(VbDayBook.class, dayBookId);
	if(vbDayBook != null) {
		 Date dayBookCreationTime = vbDayBook.getCreatedOn();
		 String creationTime = DateUtils.formatDateWithTimestamp(dayBookCreationTime);
		 Date dayBookCreation = format.parse(creationTime);
		 Integer totalMin = calculateMinDiff(currentTime, dayBookCreation);
		 if(totalMin > 10) {
			 isdayBookCRExpired = "y";
		 } else {
			 isdayBookCRExpired = "n";
		 }
	 }
	 session.close();
	return isdayBookCRExpired;
}
/**
 * This method is responsible to get {@link VbDayBook} based on
 * {@link VbOrganization} and userName and dayBookNo.
 * 
 * @param organization -{@link VbOrganization}
 * @param userName -{@link String}
 * @param dayBookNo -{@link String}
 * @return dayBookId -{@link Integer}
 * @throws DataAccessException  - {@link DataAccessException}
 * 
 */
public Integer getDayBookBasedOnDayBookNo(Integer dayBookId,VbOrganization organization,
		String dayBookNo, String userName) throws DataAccessException {
	Session session = this.getSession();
	Integer originaldayBookId = null;
	VbDayBook dayBook=(VbDayBook)session.createCriteria(VbDayBook.class)
				.add(Restrictions.eq("dayBookNo", dayBookNo))
				.add(Restrictions.eq("vbOrganization", organization)).uniqueResult();
	if(dayBook != null) {
		originaldayBookId = dayBook.getId();
	}
	session.close();
	if(originaldayBookId != null) {
		return originaldayBookId;
	 } else {
		String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		if(_logger.isErrorEnabled()) {
			_logger.error(message);
		}
		throw new DataAccessException(message);
	}
}
/**This method is responsible for getting Approved,Declined,Pending count of Day Book.
 * 
 * @param organization -{@link VbOrganization}
 * @param userName - {@link String}
 * @return historyResults -{@link MySalesHistoryResult} 
 * @throws DataAccessException - {@link DataAccessException}
 */
public List<MySalesHistoryResult> getDayBookTransactionHistory(VbOrganization organization, String userName) throws DataAccessException {
	Session session = this.getSession();
	//counts for Approved,Declined,Pending Day Book CR
	@SuppressWarnings("unchecked")
	List<Object[]> resultList = session.createCriteria(VbDayBookChangeRequest.class)
			.setProjection(Projections.projectionList()
					.add(Projections.property("dayBookNo"))
					.add(Projections.property("status"))
					.add(Projections.rowCount())
					.add(Projections.groupProperty("status")))
				.add(Restrictions.like("dayBookNo", "DB", MatchMode.ANYWHERE))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		      session.close();
		      if (!resultList.isEmpty()) {
					MySalesHistoryResult historyResult = null;
					List<MySalesHistoryResult> historyResults=new ArrayList<MySalesHistoryResult>();
					for (Object[] objects : resultList) {
						historyResult = new MySalesHistoryResult();
						String dayBookNumber = (String) objects[0];
					     String dayBookStatus = (String) objects[1];
					     Integer count = (Integer) objects[2];
					     if(CRStatus.APPROVED.name().equals(dayBookStatus)){
								historyResult.setDeliveryNoteTransactionType(dayBookNumber);
								historyResult.setDeliveryNoteTxnStatus(dayBookStatus);
								historyResult.setApprovedDNCount(count);
							}
							if(CRStatus.DECLINE.name().equals(dayBookStatus)){
								historyResult.setDeliveryNoteTransactionType(dayBookNumber);
								historyResult.setDeliveryNoteTxnStatus(dayBookStatus);
								historyResult.setDeclinedDNCount(count);
							}
							if(CRStatus.PENDING.name().equals(dayBookStatus)){
								historyResult.setDeliveryNoteTransactionType(dayBookNumber);
								historyResult.setDeliveryNoteTxnStatus(dayBookStatus);
								historyResult.setPendingDNCount(count);
							}
				     historyResults.add(historyResult);
					}
					if(_logger.isDebugEnabled()) {
						_logger.debug("{} records found.", historyResults.size());
					}
					return historyResults;
		      } else {
					String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
					
					if(_logger.isWarnEnabled()){
						_logger.warn(errorMsg);
					}
					throw new DataAccessException(errorMsg);
				}
}
/**This method is responsible to fetch invoices from {@link VbDayBookChangeRequest} based on status and invoiceNumber 
 * 
 * @param dayBookNo - {@link String}
 * @param status - {@link String}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return invoiceHistoryResults - {@link MySalesInvoicesHistoryResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<MySalesInvoicesHistoryResult> getDayBookInvoicesTransactionHistory(String dayBookNo, String status, VbOrganization organization,
		String userName) throws DataAccessException {
	Session session = this.getSession();
	List<Object[]> resultList = session.createCriteria(VbDayBookChangeRequest.class)
			.setProjection(Projections.projectionList()
					.add(Projections.property("id"))
					.add(Projections.property("dayBookNo"))
					.add(Projections.property("status"))
					.add(Projections.property("createdBy"))
					.add(Projections.property("createdOn"))
					.add(Projections.property("modifiedBy"))
					.add(Projections.property("modifiedOn")))
			.add(Restrictions.like("dayBookNo", dayBookNo, MatchMode.ANYWHERE))
			.add(Restrictions.eq("status", status))
			.add(Restrictions.eq("flag", new Integer(1)))
			.add(Restrictions.eq("vbOrganization", organization))
			.list();
	session.close();
	if (!resultList.isEmpty()) {
		Integer dayBookId = null;
		String dayBookNumber = null;
		String dayBookStatus = null;
		String createdBy = null;
		Date createdOn = null;
		String modifiedBy = null;
		Date modifiedOn = null;
		MySalesInvoicesHistoryResult invoiceHistoryResult = null;
		List<MySalesInvoicesHistoryResult> invoiceHistoryResults = new ArrayList<MySalesInvoicesHistoryResult>();
		for (Object[] objects : resultList) {
			 dayBookId = (Integer) objects[0];
		      dayBookNumber = (String) objects[1];
		      dayBookStatus = (String) objects[2];
		      createdBy = (String) objects[3];
		      createdOn=(Date) objects[4];
		      modifiedBy = (String) objects[5];
		      modifiedOn = (Date) objects[6];
		      
		    invoiceHistoryResult = new MySalesInvoicesHistoryResult();
			invoiceHistoryResult.setInvoiceNumber(dayBookNumber);
			invoiceHistoryResult.setRequestedBy(createdBy);
			invoiceHistoryResult.setRequestedDate(createdOn);
			invoiceHistoryResult.setModifiedBy(modifiedBy);
			invoiceHistoryResult.setModifiedDate(modifiedOn);
			invoiceHistoryResult.setStatus(dayBookStatus);
			invoiceHistoryResult.setId(dayBookId);
			
			invoiceHistoryResults.add(invoiceHistoryResult);
		  }
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} records found.", invoiceHistoryResults.size());
		}
		return invoiceHistoryResults;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if(_logger.isWarnEnabled()){
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}
/**
 * This Method Is To Retrieve Existed Starting Reading Or Meter Reading If,Exists
 * 
 * @param dayBookType - {@link String}
 * @param userName - {@link String}
 * @param organization - {@link VbOrganization}
 * @return existedStartReading - {@link String}
 */
public String getExistedStartReading(String dayBookType, String userName,
		VbOrganization organization) {
	Session session = this.getSession();
	VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
	if(salesBook.getAllotmentType().equalsIgnoreCase("non-daily")){
		salesBook = getVbSalesBookNonDailySEFlag(session, userName, organization);
	}
	String existedStartReading = null;
	String meterReading = (String) session.createCriteria(VbCashDayBookCr.class)
			.add(Restrictions.eq("vbSalesBook", salesBook))
			.add(Restrictions.eq("vbOrganization", organization))
			.setProjection(Projections.max("valueThree"))
			.add(Restrictions.eq("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES))
			.uniqueResult();
	if (meterReading == null) {
		String sql = "select vbtb.valueThree FROM VbCashDayBookCr vbtb WHERE vbtb.vbOrganization=:organization AND vbtb.vbSalesBook =:salesBook AND vbtb.dayBookType =:dayBookType AND"
				+ " vbtb.createdOn=(select MAX(vb.createdOn) FROM VbCashDayBookCr vb WHERE vb.vbOrganization=:organization AND vb.vbSalesBook =:salesBook AND vb.dayBookType =:dayBookType)";
		existedStartReading = (String) session.createQuery(sql)
				.setParameter("salesBook", salesBook)
				.setParameter("organization", organization)
				.setParameter("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS)
				.uniqueResult();

	} else {
		existedStartReading = meterReading;
	}
    session.close(); 
    
    if(_logger.isDebugEnabled()) {
    	_logger.debug("existed starting reading for daybook is :", existedStartReading);
    }
	return existedStartReading;
}

/**
 * This method is responsible to get the total payments for that day from - {@link VbDeliveryNotePayments}
 * 
 * @param userName - {@link String}
 * @param organization - {@link VbOrganization}
 * @return availableAmount - {@link Float}
 */
@SuppressWarnings("unchecked")
public Float getPresentPayment(String userName, VbOrganization organization) {
	Session session = this.getSession();
	Float availableAmount = new Float(0);
	Float amount = new Float(0);
	VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
	Float openingBalance = salesBook.getOpeningBalance();
	Float totalPayment = (Float) session.createCriteria(VbDeliveryNotePayments.class)
			.createAlias("vbDeliveryNote", "deliveryNote")
			.setProjection(Projections.sum("presentPayment"))
			.add(Restrictions.eq("deliveryNote.vbSalesBook", salesBook))
			.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
			.add(Restrictions.eq("deliveryNote.flag", new Integer(1)))
			.add(Restrictions.between("deliveryNote.createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
			.uniqueResult();
	List<String> amountToBankList = session.createCriteria(VbCashDayBook.class)
			.setProjection(Projections.property("valueOne"))
			.add(Restrictions.eq("vbSalesBook", salesBook))
			.add(Restrictions.eq("vbOrganization", organization))
			.add(Restrictions.ne("dayBookType", OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS))
			.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
			.list();
	if(totalPayment == null){
		totalPayment = new Float(0);
	}
	if(!(amountToBankList.isEmpty())) {
		for (String amountSpent : amountToBankList) {
			amount += Float.parseFloat(amountSpent);
		}
		availableAmount = (totalPayment + openingBalance) - amount;
	} else {
		availableAmount = totalPayment + openingBalance;
	}
		
	session.close();
	
	if(_logger.isDebugEnabled()) {
		_logger.debug("Available amount To spend : {}", availableAmount);
	}
	return availableAmount;
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
public List<DayBookAllowancesResult> getAllowances(String userName, VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	Date date = new Date();
	VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
	if(salesBook.getAllotmentType().equalsIgnoreCase("non-daily")){
		salesBook = getVbSalesBookNonDailySEFlag(session, userName, organization);
	}
	List<VbCashDayBookCr> cashDayBooks = session.createCriteria(VbCashDayBookCr.class)
			.add(Restrictions.eq("vbSalesBook", salesBook))
			.add(Restrictions.eq("vbOrganization", organization))
			.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
			.list();
	if(!(cashDayBooks.isEmpty())) {
		String dayBookType = null;
		String valueOne = null;
		String valueThree = null;
		Float amountToBank = new Float(0);
		DayBookAllowancesResult allowancesResult = null;
		List<DayBookAllowancesResult> resultList = new ArrayList<DayBookAllowancesResult>();
		for (VbCashDayBookCr vbCashDayBook : cashDayBooks) {
			allowancesResult = new DayBookAllowancesResult();
			dayBookType = vbCashDayBook.getDayBookType();
			valueOne = vbCashDayBook.getValueOne();
			valueThree = vbCashDayBook.getValueThree();
			allowancesResult.setDayBookType(dayBookType);
			if(OrganizationUtils.DAY_BOOK_AMOUNT.equalsIgnoreCase(dayBookType)) {
				amountToBank += Float.parseFloat(valueOne);
				allowancesResult.setAmountToBank(StringUtil.currencyFormat(amountToBank));
				allowancesResult.setReasonAmountToBank(valueThree);
			} else if(OrganizationUtils.DAY_BOOK_VEHICLE_DETAILS.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setVehicleNo(valueOne);
				allowancesResult.setDriverName(vbCashDayBook.getValueTwo());
				allowancesResult.setStartingReading(valueThree);
			} else if(OrganizationUtils.DAY_BOOK_DRIVER_ALLOWANCES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setDriverAllowances(valueOne);
			} else if(OrganizationUtils.DAY_BOOK_DEALER_PARTY_EXPENSES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setDealerPartyExpenses(valueOne);
				allowancesResult.setReasonDealerPartyExpenses(valueThree);
			} else if(OrganizationUtils.DAY_BOOK_EXECUTIVE_ALLOWANCES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setExecutiveAllowances(valueOne);
			} else if(OrganizationUtils.DAY_BOOK_MISCELLANEOUS_EXPENSES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setMiscellaneousExpenses(valueOne);
				allowancesResult.setReasonMiscellaneousExpenses(valueThree);
			} else if(OrganizationUtils.DAY_BOOK_MUNCIPAL_CITY_COUNCIL.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setMunicipalCityCouncil(valueOne);
			} else if(OrganizationUtils.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setVehicleMaintenanceExpenses(valueOne);
			} else if(OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setOffLoadingCharges(valueOne);
			} else if(OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(dayBookType)) {
				allowancesResult.setVehicleFuelExpenses(valueOne);
			}
			resultList.add(allowancesResult);
		}
			
		if(_logger.isInfoEnabled()) {
			_logger.info("{} results have been found for {}", resultList.size(), userName);
		}
		session.close();
		return resultList;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		if(_logger.isWarnEnabled()){
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}

/**This method is responsible for updating changed cash day book allowances in day book CR to vb_day_book_cr table.
 * 
 * @param cashDayBookId - {@link Integer}
 * @param valueOne - {@link String}
 * @param valueThree - {@link String}
 * @throws DataAccessException - {@link DataAccessException}
 */
public void updateChangedAllowance(Integer cashDayBookId, String valueOne,String valueThree) throws DataAccessException {
	Session session = this.getSession();
	VbCashDayBookCr cashDayBook = (VbCashDayBookCr) session.get(VbCashDayBookCr.class, cashDayBookId);
	if (cashDayBook != null) {
		Transaction tx = session.beginTransaction();
		cashDayBook.setValueOne(valueOne);
		cashDayBook.setValueThree(valueThree);
		cashDayBook.setModifiedOn(new Date());
		session.update(cashDayBook);
		tx.commit();
		session.close();
	} else {
		session.close();
		String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);

		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}
/**
 * This method is used to get the Vehicle Fuel Expenses
 * 
 * @param userName - {@link String}
 * @param organization - {@link VbOrganization}
 * @param dayBookType - {@link String}
 * @return resultsList - {@link DayBookAllowancesResult}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public List<DayBookAllowancesResult> getExecutiveAllowances(String userName, VbOrganization organization, String dayBookType) throws DataAccessException {
	Session session = this.getSession();
	VbSalesBook salesBook = getVbSalesBookNoFlag(session, userName, organization);
	if(salesBook.getAllotmentType().equalsIgnoreCase("non-daily")){
		salesBook = getVbSalesBookNonDailySEFlag(session, userName, organization);
	}
	List<VbCashDayBookCr> list = session.createCriteria(VbCashDayBookCr.class)
			.add(Restrictions.eq("vbSalesBook", salesBook))
			.add(Restrictions.eq("vbOrganization", organization))
			.add(Restrictions.eq("dayBookType", dayBookType))
			.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
			.list();
	session.close();
	if(!list.isEmpty()) {
		DayBookAllowancesResult allowancesResult = null;
		List<DayBookAllowancesResult> resultsList = new ArrayList<DayBookAllowancesResult>();
		String valueOne = null;
		String valueThree = null;
		for (VbCashDayBookCr vbCashDayBook : list) {
			valueOne = vbCashDayBook.getValueOne();
			valueThree = vbCashDayBook.getValueThree();
			allowancesResult = new DayBookAllowancesResult();
			allowancesResult.setId(vbCashDayBook.getId());
			allowancesResult.setSalesBookId(vbCashDayBook.getVbSalesBook().getId());
			allowancesResult.setCreatedOn(DateUtils.formatDateWithTimestamp(vbCashDayBook.getCreatedOn()));
			if(dayBookType.equals(OrganizationUtils.DAY_BOOK_VEHICLE_FUEL_EXPENSES)){
				allowancesResult.setVehicleFuelExpenses(valueOne);
				allowancesResult.setVehicleMeterReading(valueThree);
			}else if(dayBookType.equals(OrganizationUtils.DAY_BOOK_OFFLOADING_CHARGES)){
				allowancesResult.setOffLoadingCharges(valueOne);
				allowancesResult.setBusinessName(valueThree);
			}else {
				allowancesResult.setExecutiveAllowances(valueOne);
				allowancesResult.setRemarks(valueThree);
			}
			resultsList.add(allowancesResult);
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("available executive allowances for today are {}", resultsList.size());
		}
		return resultsList;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}
/**
 * This method is responsible to get {@link VbSalesBook} based on
 * {@link VbOrganization} and userName only for non-daily SE for making CR's.
 * 
 * @param session - {@link Session}
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return salesBook - {@link VbSalesBook}
 * 
 */
public VbSalesBook getVbSalesBookNonDailySEFlag(Session session,  String userName, VbOrganization organization) {
	Query query = session.createQuery("FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutiveName1 AND vb.flag = :flag")
			.setParameter("vbOrganization1", organization)
			.setParameter("salesExecutiveName1", userName)
	        .setParameter("flag", new Integer(0));
	VbSalesBook salesBook = getSingleResultOrNull(query);
	
	return salesBook;
}
/**This method is responsible for persisting original cash day book details to cash day book CR.
 * 
 * @param salesBookId - {@link Integer}
 * @param username - {@link String}
 * @param organization - {@link VbOrganization}
 * @throws DataAccessException - {@link DataAccessException}
 */
@SuppressWarnings("unchecked")
public void persistOriginalCashDayBookDetails(Integer salesBookId,String username, VbOrganization organization) throws DataAccessException {
	Session session = this.getSession();
	VbSalesBook salesBook=(VbSalesBook)session.get(VbSalesBook.class, salesBookId);
	List<VbCashDayBook> list = session.createCriteria(VbCashDayBook.class)
			.add(Restrictions.eq("vbSalesBook", salesBook))
			.add(Restrictions.eq("vbOrganization", organization))
			.add(Restrictions.eq("createdBy", username))
			.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
			.list();
	if(!list.isEmpty()) {
		VbCashDayBookCr dayBookCr=null;
		Transaction txn=session.beginTransaction();
		for (VbCashDayBook vbCashDayBook : list) {
			dayBookCr=new VbCashDayBookCr();
			dayBookCr.setId(vbCashDayBook.getId());
			dayBookCr.setDayBookType(vbCashDayBook.getDayBookType());
			dayBookCr.setValueOne(vbCashDayBook.getValueOne());
			dayBookCr.setValueTwo(vbCashDayBook.getValueTwo());
			dayBookCr.setValueThree(vbCashDayBook.getValueThree());
			dayBookCr.setCreatedBy(vbCashDayBook.getCreatedBy());
			dayBookCr.setCreatedOn(vbCashDayBook.getCreatedOn());
			dayBookCr.setModifiedBy(vbCashDayBook.getModifiedBy());
			dayBookCr.setModifiedOn(vbCashDayBook.getModifiedOn());
			dayBookCr.setVbCashDayBook(vbCashDayBook);
			dayBookCr.setVbSalesBook(vbCashDayBook.getVbSalesBook());
			dayBookCr.setVbOrganization(vbCashDayBook.getVbOrganization());
			session.saveOrUpdate(dayBookCr);
		}
		txn.commit();
		session.close();
		if(_logger.isDebugEnabled()) {
			_logger.debug("original Cash Day Book Persisted in Cash Day Book CR {}", dayBookCr);
		}
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

		if (_logger.isWarnEnabled()) {
			_logger.warn(errorMsg);
		}
		throw new DataAccessException(errorMsg);
	}
}

}
