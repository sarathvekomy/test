/**
 * com.vekomy.vbooks.app.dao.TransactionChangeRequestDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Sep 23, 2013
 */
package com.vekomy.vbooks.app.dao;


import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookAllowances;
import com.vekomy.vbooks.app.request.DayBookCR;
import com.vekomy.vbooks.app.request.DayBookProduct;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteCR;
import com.vekomy.vbooks.app.request.DeliveryNoteProduct;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.app.request.JournalCR;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnCR;
import com.vekomy.vbooks.app.request.SalesReturnProduct;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.CRStatus;
import com.vekomy.vbooks.app.utils.ENotificationTypes;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestPayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequestProducts;

/**
 * @author Sudhakar
 *
 */
public class TransactionChangeRequestDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(TransactionChangeRequestDao.class);
	
	/**
	 * This method is responsible to persist CR for delivery note.
	 * 
	 * @param request - {@link DeliveryNoteCR}
	 * @return - response - {@link Response}
	 */
	public Response saveDeliveryNoteCR(DeliveryNoteCR request) {
		Response response = new Response();
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			String userName = request.getSalesExecutive();
			DeliveryNote originalRequest = request.getOldDeliveryNote();
			DeliveryNote modifiedRequest = request.getNewDeliveryNote();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			
			// DeliveryNote Basic Info.
			VbDeliveryNoteChangeRequest instance = new VbDeliveryNoteChangeRequest();
			String businessName = originalRequest.getBusinessName();
			instance.setBusinessName(businessName);
			instance.setCreatedBy(userName);
			instance.setCreatedOn(createdOn);
			String invoiceNo = (String) session.createCriteria(VbDeliveryNote.class)
					.setProjection(Projections.property("invoiceNo"))
					.add(Restrictions.like("invoiceNo", originalRequest.getInvoiceNo(), MatchMode.END))
					.uniqueResult();
			instance.setInvoiceNo(invoiceNo);
			instance.setModifiedOn(createdOn);
			instance.setStatus(CRStatus.PENDING.name());
			instance.setVbOrganization(organization);
			instance.setVbSalesBook(salesBook);
			instance.setCrDescription(modifiedRequest.getRemarks());
			
			String originalInvoiceName = originalRequest.getInvoiceName();
			String modifiedInvoiceName = modifiedRequest.getInvoiceName();
			if(originalInvoiceName.equals(modifiedInvoiceName)) {
				instance.setInvoiceName(originalInvoiceName.concat(",0"));
			} else {
				instance.setInvoiceName(modifiedInvoiceName.concat(",1"));
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNoteChangeRequest");
			}
			session.save(instance);
			
			// DeliveryNote Payments.
			VbDeliveryNoteChangeRequestPayments paymentsInstance = new VbDeliveryNoteChangeRequestPayments();
			paymentsInstance.setBankName(originalRequest.getBankName());
			paymentsInstance.setBranchName(originalRequest.getBranchName());
			paymentsInstance.setChequeNo(originalRequest.getChequeNo());
			paymentsInstance.setBankLocation(originalRequest.getBranchLocation());
			paymentsInstance.setPaymentType(originalRequest.getPaymentType());
			paymentsInstance.setPresentAdvance(String.valueOf(originalRequest.getPresentAdvance()));
			paymentsInstance.setPreviousCredit(String.valueOf(originalRequest.getPreviousCredit()));
			paymentsInstance.setVbDeliveryNoteChangeRequest(instance);
			
			Float originalPresentPayable = originalRequest.getPresentPayable();
			Float modifiedPresentPayable = modifiedRequest.getPresentPayable();
			if(originalPresentPayable.equals(modifiedPresentPayable)) {
				paymentsInstance.setPresentPayable(String.valueOf(originalPresentPayable).concat(",0"));
				//paymentsInstance.setBalance(String.valueOf(originalRequest.getBalance()).concat(",0"));
				Float originalTotalPayableValue = originalRequest.getTotalPayable();
				if(originalTotalPayableValue < 0) {
					paymentsInstance.setTotalPayable(String.valueOf(-(originalTotalPayableValue)).concat(",0"));
				} else {
					paymentsInstance.setTotalPayable(String.valueOf(originalTotalPayableValue).concat(",0"));
				}
			} else {
				paymentsInstance.setPresentPayable(String.valueOf(modifiedPresentPayable).concat(",1"));
				//paymentsInstance.setBalance(String.valueOf(modifiedRequest.getBalance()).concat(",1"));
				Float modifiedTotalPayableValue = modifiedRequest.getTotalPayable();
				if(modifiedTotalPayableValue < 0) {
					paymentsInstance.setTotalPayable(String.valueOf(-(modifiedTotalPayableValue)).concat(",1"));
				} else {
					paymentsInstance.setTotalPayable(String.valueOf(modifiedTotalPayableValue).concat(",1"));
				}
			}
			
			Float originalPresentPayment = originalRequest.getPresentPayment();
			Float modifiedPresentPayment = modifiedRequest.getPresentPayment();
			if (originalPresentPayment.equals(modifiedPresentPayment)) {
				paymentsInstance.setPresentPayment(String.valueOf(originalPresentPayment).concat(",0"));
				//paymentsInstance.setBalance(String.valueOf(originalRequest.getBalance()).concat(",0"));
				//paymentsInstance.setTotalPayable(String.valueOf(originalRequest.getTotalPayable()).concat(",0"));
			} else {
				paymentsInstance.setPresentPayment(String.valueOf(modifiedPresentPayment).concat(",1"));
				//paymentsInstance.setBalance(String.valueOf(modifiedRequest.getBalance()).concat(",1"));
				//paymentsInstance.setTotalPayable(String.valueOf(modifiedRequest.getTotalPayable()).concat(",1"));
			}
			
			originalPresentPayment = originalRequest.getBalance();
			modifiedPresentPayment = modifiedRequest.getBalance();
			if (originalPresentPayment.equals(modifiedPresentPayment)) {
				Float originalBalanceValue = originalRequest.getBalance();
				if(originalBalanceValue < 0) {
					paymentsInstance.setBalance(String.valueOf(-(originalBalanceValue)).concat(",0"));
				} else {
					paymentsInstance.setBalance(String.valueOf(originalBalanceValue).concat(",0"));
				}
			} else {
				Float modifiedBalanceValue = modifiedRequest.getBalance();
				if(modifiedBalanceValue < 0) {
					paymentsInstance.setBalance(String.valueOf(-(modifiedBalanceValue)).concat(",1"));
				} else {
					paymentsInstance.setBalance(String.valueOf(modifiedBalanceValue).concat(",1"));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNoteChangeRequestPayments");
			}
			session.save(paymentsInstance);
			
			// DeliveryNote Products.
			List<DeliveryNoteProduct> originalProducts = originalRequest.getProducts();
			List<DeliveryNoteProduct> modifiedProducts = modifiedRequest.getProducts();
			String originalProductName = null;
			String modifiedProductName = null;
			String originalBatchNo = null;
			String modifiedBatchNo = null;
			Integer originalProductQty = null;
			Integer modifiedProductQty = null;
			Integer originalBonusQty = null;
			Integer modifiedBonusQty = null;
			String originalBonusReason = "";
			String modifiedBonusReason = "";
			VbDeliveryNoteChangeRequestProducts productInstance = null;
			for (DeliveryNoteProduct originalProduct : originalProducts) {
				for (DeliveryNoteProduct modifiedProduct : modifiedProducts) {
					originalProductName = originalProduct.getProductName();
					originalBatchNo = originalProduct.getBatchNumber();
					modifiedProductName = modifiedProduct.getProductName();
					modifiedBatchNo = modifiedProduct.getBatchNumber();
					
					productInstance = new VbDeliveryNoteChangeRequestProducts();
					productInstance.setVbDeliveryNoteChangeRequest(instance);
					productInstance.setProductName(originalProductName.concat(",0"));
					productInstance.setBatchNumber(originalBatchNo.concat(",0"));
					productInstance.setProductCost(String.valueOf(originalProduct.getProductCost()).concat(",0"));
					if((originalProductName.equals(modifiedProductName) && (originalBatchNo.equals(modifiedBatchNo)))) {
						originalProductQty = originalProduct.getProductQty();
						modifiedProductQty = modifiedProduct.getProductQty();
						originalBonusQty = originalProduct.getBonusQty();
						modifiedBonusQty = modifiedProduct.getBonusQty();
						originalBonusReason = originalProduct.getBonusReason()==null?"":originalProduct.getBonusReason();
						modifiedBonusReason = modifiedProduct.getBonusReason()==null?"":modifiedProduct.getBonusReason();
						
						if (originalProductQty.equals(modifiedProductQty)) {
							productInstance.setProductQty(String.valueOf(originalProductQty).concat(",0"));
							productInstance.setTotalCost(String.valueOf(originalProduct.getTotalCost()).concat(",0"));
						} else {
							productInstance.setProductQty(String.valueOf(modifiedProductQty).concat(",1"));
							productInstance.setTotalCost(String.valueOf(modifiedProduct.getTotalCost()).concat(",1"));
						}
						
						if(originalBonusQty.equals(modifiedBonusQty)) {
							productInstance.setBonusQty(String.valueOf(originalBonusQty).concat(",0"));
						} else {
							productInstance.setBonusQty(String.valueOf(modifiedBonusQty).concat(",1"));
						}
						
						if(originalBonusReason.equals(modifiedBonusReason)) {
							productInstance.setBonusReason(originalBonusReason.concat(",0"));
						} else {
							productInstance.setBonusReason(originalBonusReason.concat(",1"));
						}
						
						if(_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbDeliveryNoteChangeRequestProducts");
						}
						session.save(productInstance);
					}
				}
			}
			
			// Persisting VbSystemNotifications.
			saveSystemNotification(session, userName, userName, organization, 
					ENotificationTypes.DN_TXN_CR.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
			
			// Preparing Success Response.
			response.setStatusCode(new Integer(200));
			response.setMessage("success");
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Success response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			// Preparing Failure Response.
			response.setStatusCode(new Integer(500));
			response.setMessage("fail");
			
			if(_logger.isErrorEnabled()) {
				_logger.error("Error response: {}", response);
			}
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to persist CR for Journal.
	 * 
	 * @param request - {@link JournalCR}
	 * @return response - {@link Response}
	 */
	public Response saveJournalCR(JournalCR request) {
		Response response = new Response();
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			String userName = request.getSalesExecutive();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			Journal originalRequest = request.getOldJournal();
			Journal modifiedRequest = request.getNewJournal();
			
			// Populating and Persisting VbJournalChangeRequest.
			VbJournalChangeRequest journalInstance = new VbJournalChangeRequest();
			String businessName = originalRequest.getBusinessName();
			journalInstance.setBusinessName(businessName);
			journalInstance.setCrDescription(modifiedRequest.getDescription());
			journalInstance.setCreatedBy(userName);
			journalInstance.setCreatedOn(createdOn);
			journalInstance.setModifiedOn(createdOn);
			String invoiceNo = (String) session.createCriteria(VbJournal.class)
					.setProjection(Projections.property("invoiceNo"))
					.add(Restrictions.like("invoiceNo", originalRequest.getInvoiceNo(), MatchMode.END))
					.uniqueResult();
			journalInstance.setInvoiceNo(invoiceNo);
			journalInstance.setStatus(CRStatus.PENDING.name());
			journalInstance.setJournalType(originalRequest.getJournalType());
			journalInstance.setVbOrganization(organization);
			journalInstance.setVbSalesBook(salesBook);
			
			Float originalAmount = originalRequest.getAmount();
			Float modifiedAmount = modifiedRequest.getAmount();
			String originalInvoiceName = originalRequest.getInvoiceName();
			String modifiedInvoiceName = modifiedRequest.getInvoiceName();
			String origianlDescription = originalRequest.getDescription();
			String modifiedDescription = modifiedRequest.getDescription();
			
			if(originalAmount.equals(modifiedAmount)){
				journalInstance.setAmount(String.valueOf(originalAmount).concat(",0"));
			} else{
				journalInstance.setAmount(String.valueOf(modifiedAmount).concat(",1"));
			}
			
			if(originalInvoiceName.equals(modifiedInvoiceName)){
				journalInstance.setInvoiceName(originalInvoiceName.concat(",0"));
			} else{
				journalInstance.setInvoiceName(modifiedInvoiceName.concat(",1"));
			}
			
			if(origianlDescription.equals(modifiedDescription)){
				journalInstance.setDescription(origianlDescription.concat(",0"));
			} else{
				journalInstance.setDescription(modifiedDescription.concat(",1"));
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbJournalChangeRequest");
			}
			session.save(journalInstance);
			
			// Persisting VbSystemNotifications.
			saveSystemNotification(session, userName, userName, organization, 
					ENotificationTypes.JOURNAL_TXN_CR.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
			
			// Preparing Success Response.
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			
			if(_logger.isErrorEnabled()) {
				_logger.error("Success response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			// Preparing Failure Response.
			response.setStatusCode(new Integer(500));
			response.setMessage("fail");
			
			if(_logger.isErrorEnabled()) {
				_logger.error("Error response: {}", response);
			}
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to persist CR for Day Book.
	 * 
	 * @param request - {@link DayBookCR}
	 * @return response - {@link Response}
	 */
	public Response saveDayBookCr(DayBookCR request) {
		Response response = new Response();
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			String userName = request.getSalesExecutive();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			if(salesBook == null) {
				salesBook = getVbSalesBookNoFlag(session, userName, organization);
			}
			DayBook originalRequest = request.getOldDayBook();
			DayBook modifiedRequest = request.getNewDayBook();
			
			Boolean returnToFactory = originalRequest.getIsreturn();
			if(returnToFactory == null) {
				returnToFactory = Boolean.TRUE;
			}
			
			// Persisting Basic Info and Vehicle Details.
			String dayBookNo = (String) session.createCriteria(VbDayBook.class)
					.setProjection(Projections.property("dayBookNo"))
					.add(Restrictions.like("dayBookNo", originalRequest.getDayBookid(), MatchMode.END))
					.uniqueResult();
			VbDayBookChangeRequest dayBookinstance = new VbDayBookChangeRequest();
			dayBookinstance.setCreatedOn(createdOn);
			dayBookinstance.setCreatedBy(userName);
			dayBookinstance.setModifiedOn(createdOn);
			dayBookinstance.setDayBookNo(dayBookNo);
			dayBookinstance.setFlag(new Integer(1));
			dayBookinstance.setIsReturn(returnToFactory);
			dayBookinstance.setSalesExecutive(userName);
			dayBookinstance.setStatus(CRStatus.PENDING.name());
			dayBookinstance.setVbOrganization(organization);
			dayBookinstance.setVbSalesBook(salesBook);
			
			String originalReportingManager = originalRequest.getReportingManager();
			String modifiedReportingManager = modifiedRequest.getReportingManager();
			if(originalReportingManager.equals(modifiedReportingManager)){
				dayBookinstance.setReportingManager(originalReportingManager.concat(",0"));
			}else{
				dayBookinstance.setReportingManager(modifiedReportingManager.concat(",1"));
			}
			
			String originalVehicleNo = originalRequest.getVehicleNo();
			String modifiedVehicleNo = modifiedRequest.getVehicleNo();
			if(originalVehicleNo.equals(modifiedVehicleNo)){
				dayBookinstance.setVehicleNo(originalVehicleNo.concat(",0"));
			}else{
				dayBookinstance.setVehicleNo(modifiedVehicleNo.concat(",1"));
			}
			
			String originalDriverName = originalRequest.getDriverName();
			String modifiedDriverName = modifiedRequest.getDriverName();
			if(originalDriverName.equals(modifiedDriverName)){
				dayBookinstance.setDriverName(originalDriverName.concat(",0"));
			}else{
				dayBookinstance.setDriverName(modifiedDriverName.concat(",1"));
			}
			
			String originalEndingReading = originalRequest.getEndReading();
			String modifiedEndingReading = modifiedRequest.getEndReading();
			if(originalEndingReading.equals(modifiedEndingReading)){
				dayBookinstance.setEndingReading(originalEndingReading.concat(",0"));
			}else{
				dayBookinstance.setEndingReading(modifiedEndingReading);
			}
			
			String originalStartingReading = originalRequest.getStartReading();
			String modifiedStartingReading = modifiedRequest.getStartReading();
			if(originalStartingReading.equals(modifiedStartingReading)){
				dayBookinstance.setStartingReading(originalStartingReading.concat(",0"));
			}else{
				dayBookinstance.setStartingReading(modifiedStartingReading.concat(",1"));
			}
			
			String originalremarks = originalRequest.getRemarks()==null?"":originalRequest.getRemarks();
			String modifiedremarks = modifiedRequest.getRemarks()==null?"":modifiedRequest.getRemarks();

			if(originalremarks.equals(modifiedremarks)){
				dayBookinstance.setDayBookRemarks(originalremarks.concat(",0"));
			}else{
				dayBookinstance.setDayBookRemarks(modifiedremarks.concat(",1"));
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbDayBookChangeRequest");
			}
			session.save(dayBookinstance);
			
			// Persisting VbDayBookChangeRequestAmount.
			VbDayBookChangeRequestAmount amountsInstance = new VbDayBookChangeRequestAmount();
			amountsInstance.setVbDayBookChangeRequest(dayBookinstance);
			
			Float originalAmountToBank = originalRequest.getAmtTobank();
			Float modifiedAmountToBank = modifiedRequest.getAmtTobank();
			if(originalAmountToBank.equals(modifiedAmountToBank)){
				amountsInstance.setAmountToBank(String.valueOf(originalAmountToBank).concat(",0"));
				//amountsInstance.setClosingBalance(String.valueOf(originalRequest.getCloseingBal()).concat(",0"));
			}else{
				amountsInstance.setAmountToBank(String.valueOf(modifiedAmountToBank).concat(",1"));
				//amountsInstance.setClosingBalance(String.valueOf(modifiedRequest.getCloseingBal()).concat(",1"));
			}
			
			Float originalAmountToFactory = originalRequest.getAmtToFactory();
			Float modifiedAmountToFactory = modifiedRequest.getAmtToFactory();
			if(originalAmountToFactory.equals(modifiedAmountToFactory)){
				amountsInstance.setAmountToFactory(String.valueOf(originalAmountToFactory).concat(",0"));
			}else{
				amountsInstance.setAmountToFactory(String.valueOf(modifiedAmountToFactory).concat(",1"));
			}
			
			Float originalClosingBalance = originalRequest.getCloseingBal();
			Float modifiedClosingBalance = modifiedRequest.getCloseingBal();
			if(originalClosingBalance.equals(modifiedClosingBalance)){
				amountsInstance.setClosingBalance(String.valueOf(originalClosingBalance).concat(",0"));
			}else{
				amountsInstance.setClosingBalance(String.valueOf(modifiedClosingBalance).concat(",1"));
			}
			Float customerTotalPayable = originalRequest.getTotalpayableFromCust();
			Float customerTotalReceived = originalRequest.getTotalReceivedFromCust();
			
			Float customerTotalCredit = customerTotalPayable - customerTotalReceived;
			if(customerTotalCredit < 0) {
				amountsInstance.setCustomerTotalCredit(String.valueOf(-1*customerTotalCredit));
			} else {
				amountsInstance.setCustomerTotalCredit(String.valueOf(customerTotalCredit));
			}
			
			amountsInstance.setCustomerTotalPayable(String.valueOf(customerTotalPayable));
			amountsInstance.setCustomerTotalReceived(String.valueOf(customerTotalReceived));

			Float originaltotalAllowances = originalRequest.getTotalAllowances();
			Float modifiedtotalAllowances = modifiedRequest.getTotalAllowances();
			if(originaltotalAllowances.equals(modifiedtotalAllowances)){
				amountsInstance.setTotalAllowances(String.valueOf(originaltotalAllowances).concat(",0"));
			}else{
				amountsInstance.setTotalAllowances(String.valueOf(modifiedtotalAllowances).concat(",1"));
			}

			// Day book Allowances
			List<DayBookAllowances> allowancesList = originalRequest.getAllowancesList();
			String allowanceType 			=	null;
			
			Float old_offloadingAmount 			=	new Float(0);
			Float old_vehicleFuelAmount 		=	new Float(0);
			Float old_dealerPartyAmount 		=	new Float(0);
			Float old_driverAllowancesAmount 	=	new Float(0);
			Float old_executiveAllowancesAmount =	new Float(0);
			Float old_miscellaneousExpensesAmount= new Float(0);
			Float old_municipalCityCouncilAmount=	new Float(0);
			Float old_vehicleMaintenanceAmount 	= new Float(0);

			for (DayBookAllowances dayBookAllowances : allowancesList) {
				allowanceType = dayBookAllowances.getAllowancesType();
				if(ApplicationConstants.DAY_BOOK_OFFLOADING_CHARGES.equalsIgnoreCase(allowanceType)) {
					old_offloadingAmount += dayBookAllowances.getAmt();
					//amountsInstance.setOffloadingLoadingCharges(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(allowanceType)) {
					old_vehicleFuelAmount += dayBookAllowances.getAmt();
					//amountsInstance.setVehicleFuelExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_DEALER_PARTY_EXPENSES.equalsIgnoreCase(allowanceType)){
					old_dealerPartyAmount += dayBookAllowances.getAmt();
					//amountsInstance.setDealerPartyExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_DRIVER_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
					old_driverAllowancesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setDriverAllowances(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_EXECUTIVE_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
					old_executiveAllowancesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setExecutiveAllowances(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_MISCELLANEOUS_EXPENSES.equalsIgnoreCase(allowanceType)) {
					old_miscellaneousExpensesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setMiscellaneousExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_MUNCIPAL_CITY_COUNCIL.equalsIgnoreCase(allowanceType)) {
					old_municipalCityCouncilAmount += dayBookAllowances.getAmt();
					//amountsInstance.setMunicipalCityCouncil(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES.equalsIgnoreCase(allowanceType)) {
					old_vehicleMaintenanceAmount += dayBookAllowances.getAmt();
					//amountsInstance.setVehicleMaintenanceExpenses(String.valueOf(amount).concat(",0"));
				}
				//amountsInstance.setClosingBalance(String.valueOf(amount));
			}
			
			Float new_offloadingAmount 			=	new Float(0);
			Float new_vehicleFuelAmount 		=	new Float(0);
			Float new_dealerPartyAmount 		=	new Float(0);
			Float new_driverAllowancesAmount 	=	new Float(0);
			Float new_executiveAllowancesAmount =	new Float(0);
			Float new_miscellaneousExpensesAmount= new Float(0);
			Float new_municipalCityCouncilAmount=	new Float(0);
			Float new_vehicleMaintenanceAmount 	= new Float(0);
			
			allowancesList = modifiedRequest.getAllowancesList();
			for (DayBookAllowances dayBookAllowances : allowancesList) {
				allowanceType = dayBookAllowances.getAllowancesType();
				if(ApplicationConstants.DAY_BOOK_OFFLOADING_CHARGES.equalsIgnoreCase(allowanceType)) {
					new_offloadingAmount += dayBookAllowances.getAmt();
					//amountsInstance.setOffloadingLoadingCharges(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_VEHICLE_FUEL_EXPENSES.equalsIgnoreCase(allowanceType)) {
					new_vehicleFuelAmount += dayBookAllowances.getAmt();
					//amountsInstance.setVehicleFuelExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_DEALER_PARTY_EXPENSES.equalsIgnoreCase(allowanceType)){
					new_dealerPartyAmount += dayBookAllowances.getAmt();
					//amountsInstance.setDealerPartyExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_DRIVER_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
					new_driverAllowancesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setDriverAllowances(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_EXECUTIVE_ALLOWANCES.equalsIgnoreCase(allowanceType)) {
					new_executiveAllowancesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setExecutiveAllowances(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_MISCELLANEOUS_EXPENSES.equalsIgnoreCase(allowanceType)) {
					new_miscellaneousExpensesAmount += dayBookAllowances.getAmt();
					//amountsInstance.setMiscellaneousExpenses(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_MUNCIPAL_CITY_COUNCIL.equalsIgnoreCase(allowanceType)) {
					new_municipalCityCouncilAmount += dayBookAllowances.getAmt();
					//amountsInstance.setMunicipalCityCouncil(String.valueOf(amount).concat(",0"));
				} else if (ApplicationConstants.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES.equalsIgnoreCase(allowanceType)) {
					new_vehicleMaintenanceAmount += dayBookAllowances.getAmt();
					//amountsInstance.setVehicleMaintenanceExpenses(String.valueOf(amount).concat(",0"));
				}
				//amountsInstance.setClosingBalance(String.valueOf(amount));
			}

			if(old_offloadingAmount.equals(new_offloadingAmount)){
				amountsInstance.setOffloadingLoadingCharges(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setOffloadingLoadingCharges(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_vehicleFuelAmount.equals(new_vehicleFuelAmount)){
				amountsInstance.setVehicleFuelExpenses(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setVehicleFuelExpenses(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_dealerPartyAmount.equals(new_dealerPartyAmount)){
				amountsInstance.setDealerPartyExpenses(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setDealerPartyExpenses(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_driverAllowancesAmount.equals(new_driverAllowancesAmount)){
				amountsInstance.setDriverAllowances(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setDriverAllowances(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_executiveAllowancesAmount.equals(new_executiveAllowancesAmount)){
				amountsInstance.setExecutiveAllowances(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setExecutiveAllowances(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_miscellaneousExpensesAmount.equals(new_miscellaneousExpensesAmount)){
				amountsInstance.setMiscellaneousExpenses(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setMiscellaneousExpenses(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_municipalCityCouncilAmount.equals(new_municipalCityCouncilAmount)){
				amountsInstance.setMunicipalCityCouncil(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setMunicipalCityCouncil(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if(old_vehicleMaintenanceAmount.equals(new_vehicleMaintenanceAmount)){
				amountsInstance.setVehicleMaintenanceExpenses(String.valueOf(old_offloadingAmount).concat(",0"));
			}else{
				amountsInstance.setVehicleMaintenanceExpenses(String.valueOf(new_offloadingAmount).concat(",1"));
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDayBookChangeRequestAmount");
			}
			session.save(amountsInstance);
			
			List<DayBookProduct> originalProducts = originalRequest.getProductsList();
			List<DayBookProduct> modifiedProducts = modifiedRequest.getProductsList();
			String originalProductName = null;
			String modifiedProductName = null;
			String originalBatchNo = null;
			String modifiedBatchNo = null;
			Integer originalProductsToFactory = null;
			Integer modifiedProductsToFactory = null;
			VbDayBookChangeRequestProducts productsInstance = null;
			for (DayBookProduct originalProduct : originalProducts) {
				for (DayBookProduct modifiedProduct : modifiedProducts) {
					originalProductName = originalProduct.getProductName();
					modifiedProductName = modifiedProduct.getProductName();
					originalBatchNo = originalProduct.getBatchNumber();
					modifiedBatchNo = modifiedProduct.getBatchNumber();
					if(originalProductName.equals(modifiedProductName) && originalBatchNo.equals(modifiedBatchNo)) {
						productsInstance = new VbDayBookChangeRequestProducts();
						productsInstance.setOpeningStock(String.valueOf(originalProduct.getOpeningStockQty()).concat(",0"));
						productsInstance.setProductName(originalProductName.concat(",0"));
						productsInstance.setProductsToCustomer(String.valueOf(originalProduct.getSoldStockQty()).concat(",0"));
						productsInstance.setBatchNumber(originalBatchNo.concat(",0"));
						
						originalProductsToFactory = originalProduct.getReturnFactoryStockQty();
						modifiedProductsToFactory = modifiedProduct.getReturnFactoryStockQty();
						if(originalProductsToFactory.equals(modifiedProductsToFactory)){
							productsInstance.setProductsToFactory(String.valueOf(originalProductsToFactory).concat(",0"));
							productsInstance.setClosingStock(String.valueOf(originalProduct.getCloseingStockQty()).concat(",0"));
						} else {
							productsInstance.setProductsToFactory(String.valueOf(originalProductsToFactory).concat(",1"));
							productsInstance.setClosingStock(String.valueOf(modifiedProduct.getCloseingStockQty()).concat(",1"));
						}
						
						productsInstance.setVbDayBookChangeRequest(dayBookinstance); 
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbDayBookChangeRequestProducts");
						}
						session.save(productsInstance);
					}
				}
			}
			
			// Persisting VbSystemNotifications
			saveSystemNotification(session, userName, userName, organization, 
					ENotificationTypes.DB_TXN_CR.name(), CRStatus.PENDING.name(), dayBookinstance.getDayBookNo(), null);
			txn.commit();
			
			// Preparing Success Response.
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
					
			if(_logger.isErrorEnabled()) {
				_logger.error("Success response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			// Preparing Failure Response.
			response.setStatusCode(new Integer(500));
			response.setMessage("fail");

			if (_logger.isErrorEnabled()) {
				_logger.error("Error response: {}", response);
			}
			return response;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	public Response saveSalesReturnCR(SalesReturnCR request) {
		Response response = new Response();
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			String userName = request.getSalesExecutive();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			SalesReturn originalRequest = request.getOldSalesReturn();
			SalesReturn modifiedRequest = request.getNewSalesReturn();
			
			// Persisting Basic Info.
			VbSalesReturnChangeRequest instance = new VbSalesReturnChangeRequest();
			String businessName = originalRequest.getBusinessName();
			String invoiceNo = (String) session.createCriteria(VbSalesReturn.class)
					.setProjection(Projections.property("invoiceNo"))
					.add(Restrictions.like("invoiceNo", originalRequest.getInvoiceNo(), MatchMode.END))
					.uniqueResult();
			instance.setBusinessName(businessName);
			instance.setInvoiceNo(invoiceNo);
			instance.setModifiedOn(createdOn);
			instance.setCreatedBy(userName);
			instance.setCreatedOn(createdOn);
			instance.setStatus(CRStatus.PENDING.name());
			instance.setVbOrganization(organization);
			instance.setVbSalesBook(salesBook);
			
			String originalRemarks = originalRequest.getRemarks()==null?"":originalRequest.getRemarks();
			String modifiedRemarks = modifiedRequest.getRemarks()==null?"":modifiedRequest.getRemarks();
			if(originalRemarks.equals(modifiedRemarks)){
				instance.setRemarks(originalRemarks.concat(",0"));
			} else{
				instance.setRemarks(modifiedRemarks.concat(",1"));
			}
			String originalInvaoiceName = originalRequest.getInvoiceName();
			String modifiedInvaoiceName = modifiedRequest.getInvoiceName();
			if(originalInvaoiceName.equals(modifiedInvaoiceName)){
				instance.setInvoiceName(originalInvaoiceName.concat(",0"));
			} else{
				instance.setInvoiceName(modifiedInvaoiceName.concat(",1"));
			}
			instance.setProductsGrandTotal("0.00".concat(",0"));
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSalesReturnChangeRequest");
			}
			session.save(instance);
			
			// Sales Returns Products.
			List<SalesReturnProduct> originalProducts = originalRequest.getProducts();
			List<SalesReturnProduct> modifiedProducts = modifiedRequest.getProducts();
			String originalProductName = null;
			String modifiedProductName = null;
			String originalBatchNo = null;
			String modifiedBatchNo = null;
			Integer origianlDamagedQty = null;
			Integer modifiedDamagedQty = null;
			Integer origianlResalableQty = null;
			Integer modifiedResalableQty = null;
			VbSalesReturnChangeRequestProducts productsInstance = null;
			for (SalesReturnProduct originalProduct : originalProducts) {
				for (SalesReturnProduct modifiedProduct : modifiedProducts) {
					originalProductName = originalProduct.getProductName();
					modifiedProductName = modifiedProduct.getProductName();
					originalBatchNo = originalProduct.getBatchNumber();
					modifiedBatchNo = modifiedProduct.getBatchNumber();
					if(originalProductName.equals(modifiedProductName) && originalBatchNo.equals(modifiedBatchNo)) {
						productsInstance = new VbSalesReturnChangeRequestProducts();
						
						origianlDamagedQty = originalProduct.getDamagedQty();
						modifiedDamagedQty = modifiedProduct.getDamagedQty();
						if(origianlDamagedQty.equals(modifiedDamagedQty)){
							productsInstance.setDamaged(String.valueOf(origianlDamagedQty).concat(",0"));
							productsInstance.setTotalQty(String.valueOf(originalProduct.getTotalQty()).concat(",0"));
						}else{
							productsInstance.setDamaged(String.valueOf(modifiedDamagedQty).concat(",1"));
							productsInstance.setTotalQty(String.valueOf(modifiedProduct.getTotalQty()).concat(",1"));
						}
						origianlResalableQty = originalProduct.getResaleQty();
						modifiedResalableQty = modifiedProduct.getResaleQty();
						if(origianlResalableQty.equals(modifiedResalableQty)){
							productsInstance.setResalable(String.valueOf(origianlResalableQty).concat(",0"));
							productsInstance.setTotalQty(String.valueOf(originalProduct.getTotalQty()).concat(",0"));
						}else{
							productsInstance.setResalable(String.valueOf(modifiedResalableQty).concat(",1"));
							productsInstance.setTotalQty(String.valueOf(modifiedProduct.getTotalQty()).concat(",1"));
						}
						productsInstance.setResalableCost("0.00");
						productsInstance.setDamagedCost("0.00");
						productsInstance.setTotalCost("0.00".concat(",0"));
						productsInstance.setProductName(originalProductName.concat(",0"));
						productsInstance.setBatchNumber(originalBatchNo.concat(",0"));
						productsInstance.setVbSalesReturnChangeRequest(instance);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbSalesReturnProducts");
						}
						session.save(productsInstance);
					}
				}
			}
			
			// Persisting VbSystemNotifications.
			saveSystemNotification(session, userName, userName, organization, 
					ENotificationTypes.SR_TXN_CR.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
			
			// Preparing Success Response.
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
								
			if(_logger.isErrorEnabled()) {
				_logger.error("Success response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			
			// Preparing Failure Response.
			response.setStatusCode(new Integer(500));
			response.setMessage("fail");

			if (_logger.isErrorEnabled()) {
				_logger.error("Error response: {}", response);
			}
			return response;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}

