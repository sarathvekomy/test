/**
 * com.vekomy.vbooks.app.dao.DayBookDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookAllowances;
import com.vekomy.vbooks.app.request.DayBookProduct;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.DateUtils;
import com.vekomy.vbooks.hibernate.model.VbCashDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;

/**
 * @author Sudhakar
 * 
 */
public class DayBookDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DayBookDao.class);
	
	/**
	 * This method is responsible to persist {@link VbDayBook}.
	 *  
	 * @param request - {@link DayBookRequest}
	 * @param salesExecutive - {@link String}
	 * @param organizationId - {@link Integer}
	 * @return response - {@link Response}
	 */
	public Response saveDayBook(DayBook request, String salesExecutive, Integer organizationId) {
		Session session = null;
		Transaction txn = null;
		Response response = null;
		try {
			VbDayBookProducts dayBookProducts = null;
			VbSalesBook salesBook = null;
			VbProduct vbProduct = null;
			VbSalesBook vbSaBook1 = null;
			Date date = new Date();
			session = this.getSession();
			VbOrganization organization = getOrganization(session, organizationId);
			VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
			txn = session.beginTransaction();
			Boolean returnToFactory = request.getIsreturn();
			if(returnToFactory == null) {
				returnToFactory = Boolean.TRUE;
			}
			
			// Persisting VbDayBook
			VbDayBook dayBook = new VbDayBook();
			if(vbSalesBook != null) {
				dayBook.setVbSalesBook(vbSalesBook);
			}
			if (dayBook != null) {
				dayBook.setCreatedBy(salesExecutive);
				dayBook.setCreatedOn(date);
				dayBook.setModifiedOn(date);
				dayBook.setSalesExecutive(salesExecutive);
				if(returnToFactory != null) {
					dayBook.setIsReturn(returnToFactory);
				} else {
					dayBook.setIsReturn(Boolean.FALSE);
				}
				dayBook.setReportingManager(request.getReportingManager());
				dayBook.setVehicleNo(request.getVehicleNo());
				dayBook.setDriverName(request.getDriverName());
				dayBook.setEndingReading(Float.valueOf(request.getEndReading()));
				dayBook.setStartingReading(Float.valueOf(request.getStartReading()));
				dayBook.setDayBookRemarks(request.getRemarks()==null?"":request.getRemarks());
				//dayBook.setVehicleDetailsRemarks(request.getRemarks());
				String dayBookNo = new StringBuffer(generateDayBookNo(organization)).append("#")
						.append(request.getDayBookid()).toString();
				dayBook.setDayBookNo(dayBookNo);
				dayBook.setVbOrganization(organization);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting dayBook: {}", dayBook);
				}
				session.save(dayBook);
			}
			
			// Persisting VbDayBookAmount
			VbDayBookAmount dayBookAmount = new VbDayBookAmount();
			if (dayBookAmount != null) {
				dayBookAmount.setAmountToBank(request.getAmtTobank());
				dayBookAmount.setAmountToFactory(request.getAmtToFactory());
				dayBookAmount.setClosingBalance(request.getCloseingBal());
				Float customerTotalPayable = request.getTotalpayableFromCust();
				Float customerTotalReceived = request.getTotalReceivedFromCust();
				Float customerTotalCredit = customerTotalPayable - customerTotalReceived;
				if(customerTotalCredit < 0) {
					dayBookAmount.setCustomerTotalCredit(-(customerTotalCredit));
				} else {
					dayBookAmount.setCustomerTotalCredit(customerTotalCredit);
				}
				dayBookAmount.setCustomerTotalPayable(customerTotalPayable);
				dayBookAmount.setCustomerTotalReceived(customerTotalReceived);
				dayBookAmount.setTotalAllowances(request.getTotalAllowances());
				
				List<DayBookAllowances> allowancesRequests = request.getAllowancesList();
				Float dealerPartyExpenses = new Float(0);
				Float driverAllowances = new Float(0);
				Float executiveAllowances = new Float(0);
				Float miscellaneousExpenses = new Float(0);
				Float municipalCityCouncil = new Float(0);
				Float offloadingLoadingCharges = new Float(0);
				Float vehicleFuelExpenses = new Float(0);
				Float vehicleMaintenanceExpenses = new Float(0);
				Float totalAllowances = new Float(0);
				VbCashDayBook cashDayBook = null;
				String allowanceType = null;
				for (DayBookAllowances dayBookAllowancesRequest : allowancesRequests) {
					// Persisting VbCashDayBook
					cashDayBook = new VbCashDayBook();
					cashDayBook.setCreatedBy(salesExecutive);
					cashDayBook.setCreatedOn(date);
					cashDayBook.setModifiedOn(date);
					cashDayBook.setVbSalesBook(vbSalesBook);
					cashDayBook.setVbOrganization(organization);
					allowanceType = dayBookAllowancesRequest.getAllowancesType();
					cashDayBook.setDayBookType(allowanceType);
					Float amount = dayBookAllowancesRequest.getAmt();
					if(!(ApplicationConstants.DAY_BOOK_AMOUNT_TO_BANK.equalsIgnoreCase(allowanceType))) {
						totalAllowances += amount;
					}
					
					cashDayBook.setValueOne(String.valueOf(amount));
					if(ApplicationConstants.DAY_BOOK_OFFLOADING_CHARGES.equalsIgnoreCase(allowanceType)) {
						offloadingLoadingCharges += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(allowanceType)) {
						vehicleFuelExpenses += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_DEALER_PARTY_EXPENSES.equalsIgnoreCase(allowanceType)){
						dealerPartyExpenses += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_DRIVER_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
						driverAllowances += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_EXECUTIVE_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
						executiveAllowances += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_MISCELLANEOUS_EXPENSES.equalsIgnoreCase(allowanceType)) {
						miscellaneousExpenses += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_MUNCIPAL_CITY_COUNCIL.equalsIgnoreCase(allowanceType)) {
						municipalCityCouncil += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					} else if (ApplicationConstants.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES.equalsIgnoreCase(allowanceType)) {
						vehicleMaintenanceExpenses += amount;
						cashDayBook.setValueThree(dayBookAllowancesRequest.getRemarks());
					}
					
					if(_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCashDayBook: {}", cashDayBook);
					}
					session.save(cashDayBook);
				}
				
				dayBookAmount.setDealerPartyExpenses(dealerPartyExpenses);
	 			dayBookAmount.setDriverAllowances(driverAllowances);
				dayBookAmount.setExecutiveAllowances(executiveAllowances);
				dayBookAmount.setMiscellaneousExpenses(miscellaneousExpenses);
				dayBookAmount.setMunicipalCityCouncil(municipalCityCouncil);
				dayBookAmount.setOffloadingLoadingCharges(offloadingLoadingCharges);
				dayBookAmount.setTotalAllowances(totalAllowances);
				dayBookAmount.setVehicleFuelExpenses(vehicleFuelExpenses);
				dayBookAmount.setVehicleMaintenanceExpenses(vehicleMaintenanceExpenses);
				dayBookAmount.setVbDayBook(dayBook);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting dayBookAmount: {}", dayBookAmount);
				}
				session.save(dayBookAmount);
			}
			
			// Automatic allocation of products for non-daily sales executive. 
			if(!returnToFactory) {
				vbSaBook1 = new VbSalesBook();
				String createdBy = (String) session.createQuery("SELECT vb.createdBy FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 " +
						"AND vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN (SELECT MAX(vbs.createdOn) " +
						"FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2)")
						.setParameter("salesExecutive1", salesExecutive)
						.setParameter("vbOrganization1", organization)
						.setParameter("salesExecutive2", salesExecutive)
						.setParameter("vbOrganization2", organization)
						.uniqueResult();
				Integer cycleId = getCycleID(session, organization, salesExecutive);
				vbSaBook1.setCycleId(cycleId);
				vbSaBook1.setCreatedBy(createdBy);
				vbSaBook1.setCreatedOn(date);
				vbSaBook1.setModifiedOn(date);
				vbSaBook1.setAllotmentType("non-daily");
				vbSaBook1.setFlag(new Integer(1));
				vbSaBook1.setSalesExecutive(salesExecutive);
				vbSaBook1.setVbOrganization(organization);
				vbSaBook1.setAdvance(new Float(0));
				vbSaBook1.setSalesBookNo(generateSalesBookNo(organization));
				Float closingBalance = request.getCloseingBal();
				vbSaBook1.setClosingBalance(closingBalance);
				vbSaBook1.setOpeningBalance(closingBalance);
				
				if(_logger.isDebugEnabled()){
					_logger.debug("Automatic allocation of salesBook: {}", vbSaBook1);
				}
				session.save(vbSaBook1);
			}
			List<DayBookProduct> productsRequests = request.getProductsList();		
			if (productsRequests != null) {
				String productName = null;
				String batchNumber = null;
				Integer qtyClosingBalance;
				Integer qtyOpeningBalance;
				VbSalesBookProducts salesBookProducts = null;
				for (DayBookProduct products : productsRequests) {
					dayBookProducts = new VbDayBookProducts();
					productName = products.getProductName();
					batchNumber = products.getBatchNumber();
					qtyClosingBalance = products.getCloseingStockQty();
					qtyOpeningBalance = products.getOpeningStockQty();
					if (dayBookProducts != null) {
						dayBookProducts.setClosingStock(qtyClosingBalance);
						dayBookProducts.setOpeningStock(qtyOpeningBalance);
						dayBookProducts.setProductName(productName);
						dayBookProducts.setProductsToCustomer(products.getSoldStockQty());
						dayBookProducts.setProductsToFactory(products.getReturnFactoryStockQty());
						dayBookProducts.setBatchNumber(batchNumber);
						//dayBookProducts.setProductsRemarks(products.getProductsRemarks());
						dayBookProducts.setVbDayBook(dayBook);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting dayBookProducts: {}", dayBookProducts);
						}
						session.save(dayBookProducts);
					}

					// Updating Sales book by the end of the day(Generating Day book).
					VbSalesBookProducts salesBookProduct = (VbSalesBookProducts) session.createCriteria(VbSalesBookProducts.class)
							.createAlias("vbSalesBook", "salesBook")
							.add(Restrictions.eq("vbSalesBook", vbSalesBook))
							.add(Restrictions.eq("salesBook.vbOrganization", organization))
							.add(Restrictions.eq("productName", productName))
							.add(Restrictions.eq("batchNumber", batchNumber))
							.add(Restrictions.eq("salesBook.flag", new Integer(1)))
							.uniqueResult();
					if (salesBookProduct != null) {
						// Updating VbProduct
						vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
								.add(Restrictions.eq("productName", productName))
								.add(Restrictions.eq("batchNumber", batchNumber))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						
						Integer qtySold = products.getSoldStockQty();
						Integer qtyToFactory = products.getReturnFactoryStockQty();
						if(qtyToFactory == null) {
							qtyToFactory = 0;
						}
						if(vbProduct != null){
							vbProduct.setAvailableQuantity(vbProduct.getAvailableQuantity() + qtyToFactory);
							vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() - qtySold);
							vbProduct.setState(vbProduct.getState());
							vbProduct.setModifiedBy(salesExecutive);
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
					salesBook.setClosingBalance(request.getCloseingBal());
					salesBook.setModifiedBy(salesExecutive);
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
						.setParameter("salesExecutive", salesExecutive)
						.setParameter("salesExecutiveName", salesExecutive)
						.setParameter("vbOrganization2", organization);
				salesBook = getSingleResultOrNull(query);
				if (salesBook != null) {
					salesBook.setClosingBalance(request.getCloseingBal());
					salesBook.setFlag(new Integer(0));
					if(_logger.isDebugEnabled()){
						_logger.debug("Updating VbSalesBook: {}", salesBook);
					}
					session.update(salesBook);
				}
			}
			txn.commit();
			
			// Preparing response.
			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			// Preparing response.
			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
						
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to get the unique no for {@link VbDayBook}.
	 * 
	 * @param organization- {@link VbOrganization}
	 * @return generatedDayBookNo - {@link String}
	 */
	private String generateDayBookNo(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = ApplicationConstants.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		//For fetching latest invoice no.
		Query query = session.createQuery(
				"SELECT vb.dayBookNo FROM VbDayBook vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDayBook vbdn "
				+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.dayBookNo LIKE :dayBookNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("dayBookNo", "%DB%");
		String latestDayBookNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_DAY_BOOK))
				.uniqueResult();
		
		StringBuffer generatedDayBookNo = new StringBuffer();
		if (latestDayBookNo == null) {
			if(invoiceNoPeriod != null) {
				generatedDayBookNo.append(organization.getOrganizationCode())
				.append(seperator).append(ApplicationConstants.TXN_TYPE_DAY_BOOK)
				.append(seperator).append(formattedDate).append(seperator)
				.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedDayBookNo.append(organization.getOrganizationCode())
				.append(seperator).append(ApplicationConstants.TXN_TYPE_DAY_BOOK)
				.append(seperator).append(formattedDate).append(seperator).append("0001");
			}
		} else {
			String[] dayBookNumber = latestDayBookNo.split("#");
			String[] latestDayBookNoArray = dayBookNumber[0].split(seperator);
			latestDayBookNo = latestDayBookNoArray[3];
			Integer dayBookNo = Integer.parseInt(latestDayBookNo);
			String newDayBookNo = resetInvoiceNo(session, invoiceNoPeriod, organization, dayBookNo);
			generatedDayBookNo.append(organization.getOrganizationCode())
			.append(seperator).append(ApplicationConstants.TXN_TYPE_DAY_BOOK)
			.append(seperator).append(formattedDate).append(seperator).append(newDayBookNo);
		}
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated daybookNo is {}", generatedDayBookNo.toString());
		}
		return generatedDayBookNo.toString();
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
						"AND vb.salesExecutive = :salesExecutive1 AND vb.createdOn IN (SELECT MAX(vbs.createdOn) " +
						"FROM VbSalesBook vbs WHERE vbs.vbOrganization = :vbOrganization2 AND vbs.salesExecutive = :salesExecutive2)")
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutive1", userName)
				.setParameter("vbOrganization2", organization)
				.setParameter("salesExecutive2", userName).uniqueResult();
		return cycleId;
	}
}
