/**
 * com.vekomy.vbooks.mysales.cr.dao.DeliveryNoteCrDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: September 26, 2013
 */
package com.vekomy.vbooks.mysales.cr.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestPayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestProductResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform operations on Delivery Note change
 * request in sales module.
 * 
 * @author Ankit
 * 
 */
public class DeliveryNoteCrDao extends MysalesCrBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteCrDao.class);

	/**
	 * This method is responsible to persist the {@link VbDeliveryNoteChangeRequest},
	 * {@link VbDeliveryNoteChangeRequestPayments} and {@link VbDeliveryNoteChangeRequestProducts}.
	 * 
	 * @param products - {@link ChangeRequestDeliveryNoteProductCommand}
	 * @param deliveryNoteList - {@link List}
	 * @param deliveryNoteOriginalList - {@link List}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void saveDeliveryNote(
			ChangeRequestDeliveryNoteProductCommand products,
			List<ChangeRequestDeliveryNoteCommand> deliveryNoteList,
			String originalDNInvoiceNumber, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			String businessName = null;
			String invoiceNo = null;
			VbDeliveryNoteChangeRequest instanceNote = new VbDeliveryNoteChangeRequest();
			VbDeliveryNoteChangeRequestProducts instanceProducts = null;
			List<VbDeliveryNotePayments> originalDNPaymentsList = null;
			instanceNote.setCreatedOn(date);
			instanceNote.setCreatedBy(userName);
			instanceNote.setModifiedOn(date);
			instanceNote.setVbOrganization(organization);
			instanceNote.setCrDescription(products.getDescription());
			instanceNote.setStatus(CRStatus.PENDING.name());
			VbSalesBook salesBook = getSalesBook(session, organization, userName);
			instanceNote.setVbSalesBook(salesBook);
			// based on original DN invoice number get respective DN ,DN products,DN payments details.
			VbDeliveryNote originalDeliveryNoteCommand = (VbDeliveryNote) session.createCriteria(VbDeliveryNote.class)
					.add(Restrictions.eq("invoiceNo", originalDNInvoiceNumber))
					.add(Restrictions.eq("createdBy", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("flag", new Integer(1)))
					.uniqueResult();
			if (originalDeliveryNoteCommand != null) {
				// persisting changed DN basic Details
				ChangeRequestDeliveryNoteCommand changeDNBasic = deliveryNoteList.get(0);
				businessName = changeDNBasic.getBusinessName();
				invoiceNo = changeDNBasic.getInvoiceNo();
				instanceNote.setBusinessName(businessName);
				instanceNote.setInvoiceNo(invoiceNo);
				String originalInvoiceName = originalDeliveryNoteCommand.getInvoiceName();
				String modifiedInvoiceName = changeDNBasic.getInvoiceName();
				if (originalInvoiceName.equals(modifiedInvoiceName)) {
					instanceNote.setInvoiceName(originalInvoiceName.concat(",0"));
				} else {
					instanceNote.setInvoiceName(modifiedInvoiceName.concat(",1"));
				}

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteChangeRequest.");
				}
				session.save(instanceNote);
			}
			List<VbDeliveryNoteProducts> originalDNProductsList = new ArrayList<VbDeliveryNoteProducts>(
					originalDeliveryNoteCommand.getVbDeliveryNoteProductses());
			String originalProductQty = null;
			String originalBonusQty = null;
			String originalBonusReason = null;
			String changedProductQty = null;
			String changedBonusQty = null;
			String changedBonusReason = null;
			String productCost = null;
			Float totalCost = new Float(0);
			String productQty = null;
			String originalProductName = null;
			String modifiedProductName = null;
			String originalBatchNo = null;
			String modifiedBatchNo = null;
			for (ChangeRequestDeliveryNoteCommand changedDNproduct : deliveryNoteList) {
				for (VbDeliveryNoteProducts originalDNProduct : originalDNProductsList) {
					originalProductName = originalDNProduct.getProductName();
					modifiedProductName = changedDNproduct.getProductName();
					originalBatchNo = originalDNProduct.getBatchNumber();
					modifiedBatchNo = changedDNproduct.getBatchNumer();
					if (originalProductName.equals(modifiedProductName) && originalBatchNo.equals(modifiedBatchNo)) {
						originalProductQty = Integer.toString(originalDNProduct.getProductQty());
						originalBonusQty = Integer.toString(originalDNProduct.getBonusQty());
						originalBonusReason = originalDNProduct.getBonusReason();
						changedProductQty = changedDNproduct.getProductQuantity();
						changedBonusQty = changedDNproduct.getBonusQuantity();
						changedBonusReason = changedDNproduct.getBonusReason();

						instanceProducts = new VbDeliveryNoteChangeRequestProducts();
						// check current row product quantity
						if (originalProductQty.equals(changedProductQty)) {
							productCost = Float.toString(originalDNProduct.getProductCost());
							productQty = Integer.toString(originalDNProduct.getProductQty());
							instanceProducts.setProductQty(productQty.concat(",0"));
							totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
							instanceProducts.setTotalCost(totalCost.toString().concat(",0"));
						} else {
							productCost = changedDNproduct.getProductCost();
							productQty = changedDNproduct.getProductQuantity();
							instanceProducts.setProductQty(productQty.concat(",1"));
							totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
							instanceProducts.setTotalCost(totalCost.toString().concat(",1"));
						}
						// check current row bonus quantity
						if (originalBonusQty.equals(changedBonusQty)) {
							if (originalBonusQty.equals("") || changedBonusQty.equals("")) {
								instanceProducts.setBonusQty(originalBonusQty.concat(",0"));
							} else {
								instanceProducts.setBonusQty(originalBonusQty.concat(",0"));
							}
						} else {
							if (originalBonusQty.equals("")	|| changedBonusQty.equals("")) {
								instanceProducts.setBonusQty(changedBonusQty.concat(",1"));
							} else {
								instanceProducts.setBonusQty(changedBonusQty.concat(",1"));
							}
						}
						// check current row bonus reason
						if (originalBonusReason == null && changedBonusReason == null) {
							instanceProducts.setBonusReason(",0");
						} else if(originalBonusReason != null && changedBonusReason == null){
							instanceProducts.setBonusReason(",0");
						}else if(originalBonusReason == null && changedBonusReason != null){
							instanceProducts.setBonusReason(changedBonusReason.concat(",1"));
						}
						else if(originalBonusReason != null && changedBonusReason != null) {
							if (originalBonusReason.equals(changedBonusReason)) {
								instanceProducts.setBonusReason(originalBonusReason.concat(",0"));
							} else {
								instanceProducts.setBonusReason(changedBonusReason.concat(",1"));
							}
						}
						productCost = Float.toString(originalDNProduct.getProductCost());
						instanceProducts.setProductCost(productCost.concat(",0"));
						instanceProducts.setBatchNumber(originalDNProduct.getBatchNumber().concat(",0"));
						instanceProducts.setProductName(originalDNProduct.getProductName().concat(",0"));
						instanceProducts.setVbDeliveryNoteChangeRequest(instanceNote);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbDeliveryNoteChangeRequestProducts.");
						}
						session.save(instanceProducts);
					}
				}
			}
			originalDNPaymentsList = new ArrayList<VbDeliveryNotePayments>(originalDeliveryNoteCommand.getVbDeliveryNotePaymentses());
			VbDeliveryNoteChangeRequestPayments instancePayments = new VbDeliveryNoteChangeRequestPayments();
			String originalPresentPayment = null;
			String changedPresentPayment = null;
			String originalPaymentType = null;
			String changedPaymentType = null;
			String changedBalance = null;
			String changedTotalPayable = null;
			String originalPresentPayable = null;
			String changedPresentPayable = null;
			for (VbDeliveryNotePayments originalDNPayments : originalDNPaymentsList) {
				originalPresentPayment = Float.toString(originalDNPayments.getPresentPayment());
				changedPresentPayment = products.getPresentPayment();
				originalPaymentType = originalDNPayments.getPaymentType();
				changedBalance = products.getBalance();
				changedTotalPayable = products.getTotalPayable();
				originalPresentPayable = Float.toString(originalDNPayments.getPresentPayable());
				changedPresentPayable = products.getPresentPayable();
				changedPaymentType = products.getPaymentType();
				if (originalPresentPayment.replace(".0", "").equals(changedPresentPayment)) {
					instancePayments.setPresentPayment(originalPresentPayment.concat(",0"));
					instancePayments.setBalance(changedBalance.concat(",0"));
					instancePayments.setTotalPayable(changedTotalPayable.concat(",0"));
				} else {
					instancePayments.setPresentPayment(changedPresentPayment.concat(",1"));
					instancePayments.setBalance(changedBalance.concat(",1"));
					instancePayments.setTotalPayable(changedTotalPayable.concat(",1"));
				}
				if (originalPaymentType.equalsIgnoreCase(changedPaymentType)) {
					instancePayments.setPaymentType(originalPaymentType.concat(",0"));
				} else {
					instancePayments.setPaymentType(changedPaymentType.concat(",1"));
				}
				if (originalPresentPayable.replace(".0", "").equals(changedPresentPayable)) {
					instancePayments.setPresentPayable(originalPresentPayable.concat(",0"));
				} else {
					instancePayments.setBalance(changedBalance.concat(",1"));
					instancePayments.setTotalPayable(changedTotalPayable.concat(",1"));
					instancePayments.setPresentPayable(changedPresentPayable.concat(",1"));
				}
				instancePayments.setPresentAdvance(products.getPresentAdvance());
				instancePayments.setPreviousCredit(products.getPreviousCredit());
				instancePayments.setBankName(products.getBankName());
				instancePayments.setChequeNo(products.getChequeNo());
				instancePayments.setBranchName(products.getBranchName());
				instancePayments.setBankLocation(products.getBankLocation());
				instancePayments.setVbDeliveryNoteChangeRequest(instanceNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteChangeRequestPayments.");
				}
				session.save(instancePayments);
			}
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, ENotificationTypes.DN_TXN_CR.name(),
					CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to persist {@link VbDeliveryNoteChangeRequest}
	 * , update {@link VbCustomerAdvanceInfo} and {@link VbCustomerCreditInfo}.
	 * 
	 * @param command - {@link ChangeRequestDeliveryNoteCommand}
	 * @param deliveryNoteProductCommand - {@link ChangeRequestDeliveryNoteProductCommand}
	 * @param deliveryNotePayments - {@link VbDeliveryNoteChangeRequestPayments}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void savePayments(ChangeRequestDeliveryNoteCommand command,
			String originalDNInvoiceNumber,
			ChangeRequestDeliveryNoteProductCommand deliveryNoteProductCommand,
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbDeliveryNoteChangeRequest deliveryNote = new VbDeliveryNoteChangeRequest();
			VbSalesBook salesBook = getSalesBook(session, organization,	userName);
			String businessName = command.getBusinessName();
			String invoiceNo = command.getInvoiceNo();
			// based on original DN invoice number get respective DN ,DN payments details.
			VbDeliveryNote originalDeliveryNoteCommand = (VbDeliveryNote) session.createCriteria(VbDeliveryNote.class)
					.add(Restrictions.eq("invoiceNo", originalDNInvoiceNumber))
					.add(Restrictions.eq("createdBy", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if (originalDeliveryNoteCommand != null) {
				if (originalDeliveryNoteCommand.getInvoiceName().equals(command.getInvoiceName())) {
					deliveryNote.setInvoiceName(command.getInvoiceName().concat(",0"));
				} else {
					deliveryNote.setInvoiceName(command.getInvoiceName().concat(",1"));
				}
			}
			deliveryNote.setBusinessName(businessName);
			deliveryNote.setCreatedBy(userName);
			Date date = new Date();
			deliveryNote.setCreatedOn(date);
			deliveryNote.setModifiedOn(date);
			deliveryNote.setCrDescription(deliveryNoteProductCommand.getDescription());
			deliveryNote.setInvoiceNo(invoiceNo);
			deliveryNote.setStatus(CRStatus.PENDING.name());
			deliveryNote.setVbSalesBook(salesBook);
			deliveryNote.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting deliveryNoteCR.");
			}
			session.save(deliveryNote);

			List<VbDeliveryNotePayments> originalPayments = new ArrayList<VbDeliveryNotePayments>(originalDeliveryNoteCommand.getVbDeliveryNotePaymentses());
			String changedPresentPayment = null;
			String originalPresentPayment = null;
			String originalPaymentType = null;
			String changedPaymentType = null;
			String changedBalance = null;
			String changedTotalPayable = null;
			VbDeliveryNoteChangeRequestPayments deliveryNotePayments = null;
			for (VbDeliveryNotePayments payments : originalPayments) {
				changedPresentPayment = deliveryNoteProductCommand.getPresentPayment();
				originalPresentPayment = Float.toString(payments.getPresentPayment());
				originalPaymentType = payments.getPaymentType();
				changedPaymentType = deliveryNoteProductCommand.getPaymentType();
				changedBalance = deliveryNoteProductCommand.getBalance();
				changedTotalPayable = deliveryNoteProductCommand.getTotalPayable();
				deliveryNotePayments = new VbDeliveryNoteChangeRequestPayments();
				if (originalPresentPayment.replace(".0", "").equals(changedPresentPayment)) {
					deliveryNotePayments.setPresentPayment(originalPresentPayment.concat(",0"));
					deliveryNotePayments.setBalance(changedBalance.concat(",0"));
					deliveryNotePayments.setTotalPayable(changedTotalPayable.concat(",0"));
				} else {
					deliveryNotePayments.setPresentPayment(changedPresentPayment.concat(",1"));
					deliveryNotePayments.setBalance(changedBalance.concat(",1"));
					deliveryNotePayments.setTotalPayable(changedTotalPayable.concat(",1"));
				}
				if (originalPaymentType.equalsIgnoreCase(changedPaymentType)) {
					deliveryNotePayments.setPaymentType(originalPaymentType.concat(",0"));
				} else {
					deliveryNotePayments.setPaymentType(changedPaymentType.concat(",1"));
				}
				deliveryNotePayments.setPresentPayable(deliveryNoteProductCommand.getPresentPayable().concat(",0"));
				deliveryNotePayments.setBankName(deliveryNoteProductCommand.getBankName());
				deliveryNotePayments.setBranchName(deliveryNoteProductCommand.getBranchName());
				deliveryNotePayments.setChequeNo(deliveryNoteProductCommand.getChequeNo());
				deliveryNotePayments.setBankLocation(deliveryNoteProductCommand.getBankLocation());
				deliveryNotePayments.setPresentAdvance(deliveryNoteProductCommand.getPresentAdvance());
				deliveryNotePayments.setPreviousCredit(deliveryNoteProductCommand.getPreviousCredit());
				deliveryNotePayments.setVbDeliveryNoteChangeRequest(deliveryNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting deliveryNotePayments.");
				}
				session.save(deliveryNotePayments);
			}
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, ENotificationTypes.DN_TXN_CR.name(),
					CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}


	/**
	 * This method is responsible for getting Delivery Note Cr details from temp
	 * tables.
	 * 
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return deliveryNoteList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDeliveryNoteResult> getDeliveryNoteCrResults(
			String username, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbDeliveryNoteChangeRequest> deliveryNoteChangeRequestList = session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn"))
				.list();
		
		if(!deliveryNoteChangeRequestList.isEmpty()) {
			ChangeRequestDeliveryNoteResult deliveryNoteResult = null;
			List<ChangeRequestDeliveryNoteResult> deliveryNoteList = new ArrayList<ChangeRequestDeliveryNoteResult>();
			for (VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest : deliveryNoteChangeRequestList) {
				deliveryNoteResult = new ChangeRequestDeliveryNoteResult();
				deliveryNoteResult.setBusinessName(vbDeliveryNoteChangeRequest.getBusinessName());
				if (vbDeliveryNoteChangeRequest.getInvoiceName().contains(",1")) {
					deliveryNoteResult.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",1", ""));
				} else {
					deliveryNoteResult.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",0", ""));
				}
				deliveryNoteResult.setInvoiceNo(vbDeliveryNoteChangeRequest.getInvoiceNo());
				deliveryNoteResult.setDate(DateUtils.format(vbDeliveryNoteChangeRequest.getCreatedOn()));
				Set<VbDeliveryNoteChangeRequestPayments> paymentSet = vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestPaymentses();
				/*VbDeliveryNoteChangeRequestPayments payments= (VbDeliveryNoteChangeRequestPayments) session.createCriteria(VbDeliveryNoteChangeRequestPayments.class)
						.createAlias("vbDeliveryNoteChangeRequest", "vbDeliveryNoteChangeRequest")
						.add(Restrictions.eq("vbDeliveryNoteChangeRequest.id",vbDeliveryNoteChangeRequest.getId()))
						.uniqueResult();*/
				for (VbDeliveryNoteChangeRequestPayments payments : paymentSet) {
					if (payments.getBalance().contains(",1")) {
						deliveryNoteResult.setBalance(StringUtil.currencyFormat(Float.parseFloat(payments.getBalance().replace(",1", ""))));
					} else {
						deliveryNoteResult.setBalance(StringUtil.currencyFormat(Float.parseFloat(payments.getBalance().replace(",0", ""))));
					}
				}
				deliveryNoteResult.setId(vbDeliveryNoteChangeRequest.getId());
				deliveryNoteResult.setCreatedBy(vbDeliveryNoteChangeRequest.getCreatedBy());
				
				deliveryNoteList.add(deliveryNoteResult);
			}
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", deliveryNoteList.size());
			}
			return deliveryNoteList;
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
	 * This method is responsible for Get Delivery Note Results List of Current
	 * Date.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return deliveryNoteList - {@link List}
	 * @throws ParseException - {@link ParseException}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDeliveryNoteResult> getDeliveryNoteResultsOnCriteria(
			VbOrganization organization, String username)
			throws DataAccessException {
		Session session = this.getSession();
		List<VbDeliveryNote> deliveryNotesList = session.createCriteria(VbDeliveryNote.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(new Date()), DateUtils.getEndTimeStamp(new Date())))
				.add(Restrictions.eq("createdBy", username))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.addOrder(Order.desc("createdOn"))
				.list();

		if (!deliveryNotesList.isEmpty()) {
			ChangeRequestDeliveryNoteResult deliveryNoteResult = null;
			List<ChangeRequestDeliveryNoteResult> deliveryNoteList = new ArrayList<ChangeRequestDeliveryNoteResult>();
			for (VbDeliveryNote vbDeliveryNote : deliveryNotesList) {
				deliveryNoteResult = new ChangeRequestDeliveryNoteResult();
				deliveryNoteResult.setBusinessName(vbDeliveryNote.getBusinessName());
				deliveryNoteResult.setInvoiceName(vbDeliveryNote.getInvoiceName());
				deliveryNoteResult.setInvoiceNo(vbDeliveryNote.getInvoiceNo());
				deliveryNoteResult.setDate(DateUtils.format(vbDeliveryNote.getCreatedOn()));
				Set<VbDeliveryNotePayments> paymentSet = vbDeliveryNote.getVbDeliveryNotePaymentses();
				for (VbDeliveryNotePayments payments : paymentSet) {
					deliveryNoteResult.setBalance(StringUtil.currencyFormat(payments.getBalance()));
				}
				deliveryNoteResult.setId(vbDeliveryNote.getId());
				
				deliveryNoteList.add(deliveryNoteResult);
			}
			session.close();

			if (_logger.isInfoEnabled()) {
				_logger.info("{} records have been found.", deliveryNoteList.size());
			}
			return deliveryNoteList;
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
	 * @return isAvailable - {@link String}
	 */
	public String validateSEChangeRequest(String invoiceNumber,
			VbOrganization organization) {
		String isAvailable = "n";
		Session session = this.getSession();
		VbDeliveryNoteChangeRequest changeRequest = (VbDeliveryNoteChangeRequest) session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if (changeRequest != null) {
			VbDeliveryNoteChangeRequest changeRequestDayBook = (VbDeliveryNoteChangeRequest) session.createCriteria(VbDeliveryNoteChangeRequest.class)
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
	 * This method is used to get the original delivery note details based on
	 * the delivery note id for DN CR edit page.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return resultList - {@link DeliveryNoteViewResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public DeliveryNoteViewResult getOriginalDeliveryNoteDetails(
			Integer id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Float grandTotal=new Float(0.00);
		VbDeliveryNote vbDeliveryNote = (VbDeliveryNote) session.get(VbDeliveryNote.class, id);
		if (vbDeliveryNote != null) {
			String invoiceNo = vbDeliveryNote.getInvoiceNo();
			String invoiceName = vbDeliveryNote.getInvoiceName();
			String businessName = vbDeliveryNote.getBusinessName();
			Date createdDate = vbDeliveryNote.getCreatedOn();
			Integer salesBookId = vbDeliveryNote.getVbSalesBook().getId();
			Integer deliveryNoteId = vbDeliveryNote.getId();
			List<VbDeliveryNotePayments> deliveryNotePaymentsList = new ArrayList<VbDeliveryNotePayments>(vbDeliveryNote.getVbDeliveryNotePaymentses());
			VbDeliveryNotePayments payments = deliveryNotePaymentsList.get(0);
			String fullName = getEmployeeFullName(vbDeliveryNote.getVbSalesBook().getSalesExecutive(), organization);
			grandTotal=getGrandTotal(id,organization);
					
			DeliveryNoteViewResult result = new DeliveryNoteViewResult();
			result.setSalesBookId(salesBookId);
			result.setGrandTotal(StringUtil.currencyFormat(grandTotal));
			result.setInvoiceNo(StringUtil.format(invoiceNo));
			result.setInvoiceName(StringUtil.format(invoiceName));
			result.setId(deliveryNoteId);
			result.setBusinessName(StringUtil.format(businessName));
			result.setCreatedDate(DateUtils.format(createdDate));
			result.setSalesExecutive(StringUtil.format(fullName));
			result.setPaymentType(StringUtil.format(payments.getPaymentType()));
			result.setBankName(StringUtil.format(payments.getBankName()));
			result.setBranchName(StringUtil.format(payments.getBranchName()));
			result.setChequeNo(StringUtil.format(payments.getChequeNo()));
			result.setBankLocation(StringUtil.format(payments.getBankLocation()));
			result.setPresentPayable(StringUtil.currencyFormat(payments.getPresentPayable()));
			result.setPresentAdvance(StringUtil.currencyFormat(payments.getPresentAdvance()));
			result.setPreviousCredit(StringUtil.currencyFormat(payments.getPreviousCredit()));
			result.setTotalPayable(StringUtil.currencyFormat(payments.getTotalPayable()));
			result.setPresentPayment(StringUtil.currencyFormat(payments.getPresentPayment()));
			result.setBalance(StringUtil.currencyFormat(payments.getBalance()));
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("DeliveryNoteViewResult: {}", result);
			}
			return result;
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
	 * This method is used to get the data for existed Business Name from
	 * VbDelivery Note Products.
	 * 
	 * @param salesId - {@link Integer}
	 * @param deliveryNoteId - {@link Integer}
	 * @param salesExecutive - {@link String}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return productResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestProductResult> getExistedBusinessNameGridData(
			Integer salesId, Integer deliveryNoteId, String salesExecutive,
			String businessName, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		List<VbDeliveryNoteProducts> productList = session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.add(Restrictions.eq("deliveryNote.businessName", businessName))
				.add(Restrictions.eq("deliveryNote.id", deliveryNoteId))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.addOrder(Order.asc("productName"))
				.list();
		if (!productList.isEmpty()) {
			ChangeRequestProductResult productResult = null;
			List<ChangeRequestProductResult> productResultList = new ArrayList<ChangeRequestProductResult>();
			String productName = null;
			String batchNo = null;
			for (VbDeliveryNoteProducts vbDeliveryNoteProducts : productList) {
				productName = vbDeliveryNoteProducts.getProductName();
				batchNo = vbDeliveryNoteProducts.getBatchNumber();
				productResult = new ChangeRequestProductResult();
				productResult.setProductName(productName);
				productResult.setBatchNumber(batchNo);
				productResult.setProductCost(StringUtil.currencyFormat(vbDeliveryNoteProducts.getProductCost()));
				productResult.setProductQty(StringUtil.format(vbDeliveryNoteProducts.getProductQty()));
				productResult.setTotalCost(StringUtil.currencyFormat(vbDeliveryNoteProducts.getTotalCost()));
				productResult.setBonus(vbDeliveryNoteProducts.getBonusQty());
				productResult.setBonusReason(StringUtil.format(vbDeliveryNoteProducts.getBonusReason()));
				List<VbSalesBookProducts> salesBookProductsList = session.createCriteria(VbSalesBookProducts.class)
						.createAlias("vbSalesBook", "salesBook")
						.add(Restrictions.eq("salesBook.id", salesId))
						.add(Restrictions.eq("productName", productName))
						.add(Restrictions.eq("batchNumber", batchNo))
						.add(Restrictions.eq("salesBook.vbOrganization", organization))
						.list();
				for (VbSalesBookProducts vbSalesBookProducts : salesBookProductsList) {
					VbSalesBook salesBook = getSalesBook(session, organization, salesExecutive);
					Query qtyQuery = session.createQuery(
									"SELECT SUM(vb.productQty) + SUM(vb.bonusQty) FROM VbDeliveryNoteProducts vb "
									+ "WHERE vb.productName = :productName AND vb.batchNumber = :batchNumber AND " +
									"vb.vbDeliveryNote.vbOrganization = :vbOrganization AND vb.vbDeliveryNote.vbSalesBook = :vbSalesBook AND vb.vbDeliveryNote.flag = :flag")
							.setParameter("productName", vbDeliveryNoteProducts.getProductName())
							.setParameter("batchNumber", vbDeliveryNoteProducts.getBatchNumber())
							.setParameter("vbOrganization", organization)
							.setParameter("vbSalesBook", salesBook)
							.setParameter("flag", new Integer(1));
					Integer totalQty = getSingleResultOrNull(qtyQuery);
					Integer allottedQty = vbSalesBookProducts.getQtyAllotted();
					Integer availabelQty = vbSalesBookProducts.getQtyClosingBalance() + allottedQty;
					if (totalQty == null) {
						productResult.setAvailableQuantity(StringUtil.quantityFormat(availabelQty));
					} else {
						productResult.setAvailableQuantity(StringUtil.quantityFormat(availabelQty - totalQty));
					}
				}
				productResultList.add(productResult);
			}
			session.close();

			if (_logger.isInfoEnabled()) {
				_logger.info("{} products have been found.", productResultList.size());
			}
			return productResultList;
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
	 * This method is responsible to get the Grand Total based on the delivery
	 * Note Id.
	 * 
	 * @param deliveryNoteId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return grandTotal - {@link Float}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Float getGrandTotal(Integer deliveryNoteId,
			VbOrganization organization) {
		Session session = this.getSession();
		Float grandTotal = new Float(0);
		List<VbDeliveryNoteProducts> productList = session.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "deliveryNote")
				.add(Restrictions.eq("deliveryNote.id", deliveryNoteId))
				.add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				.addOrder(Order.asc("productName"))
				.addOrder(Order.asc("batchNumber"))
				.list();
		session.close();
		
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : productList) {
			grandTotal += vbDeliveryNoteProducts.getTotalCost();
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Grand Total is : {}", grandTotal);
		}
		return grandTotal;
	}

	/**
	 * This method is used to get the delivery note CR Results based on the
	 * delivery note CR id for dashboard DN result and history page.
	 * 
	 * @param id - {@link Integer}
	 * @return resultList - {@link DeliveryNoteViewResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteViewResult> getDeliveryNoteChangeRequestResult(
			Integer id, VbOrganization organization) throws DataAccessException {
		VbDeliveryNoteChangeRequestPayments changedPayments = null;
		Session session = this.getSession();
		VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, id);
		if (vbDeliveryNoteChangeRequest != null) {
			String invoiceNo = vbDeliveryNoteChangeRequest.getInvoiceNo();
			String crDescription = vbDeliveryNoteChangeRequest.getCrDescription();
			String invoiceName = vbDeliveryNoteChangeRequest.getInvoiceName();
			String businessName = vbDeliveryNoteChangeRequest.getBusinessName();
			Date createdDate = vbDeliveryNoteChangeRequest.getCreatedOn();
			Integer deliveryNoteId = vbDeliveryNoteChangeRequest.getId();
			List<VbDeliveryNoteChangeRequestProducts> deliveryNoteProductsList = new ArrayList<VbDeliveryNoteChangeRequestProducts>(
					vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestProductses());
			List<VbDeliveryNoteChangeRequestPayments> deliveryNotePaymentsList = new ArrayList<VbDeliveryNoteChangeRequestPayments>(
					vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestPaymentses());
			changedPayments = deliveryNotePaymentsList.get(0);
			String fullName = getEmployeeFullName(vbDeliveryNoteChangeRequest
					.getVbSalesBook().getSalesExecutive(), organization);
			List<DeliveryNoteViewResult> resultList = new ArrayList<DeliveryNoteViewResult>();
			DeliveryNoteViewResult result = null;
			if (!deliveryNoteProductsList.isEmpty()) {
				for (VbDeliveryNoteChangeRequestProducts product : deliveryNoteProductsList) {
					result = new DeliveryNoteViewResult();
					result.setProduct(product.getProductName());
					result.setBatchNumber(product.getBatchNumber());
					result.setQtyProduct(product.getProductQty());
					result.setCostProduct(product.getProductCost());
					result.setQtyBonus(product.getBonusQty());
					result.setBonusReason(product.getBonusReason());
					result.setTotalCostProduct(product.getTotalCost());
					result.setInvoiceNo(StringUtil.format(invoiceNo));
					result.setCrDescription(crDescription);
					result.setInvoiceName(StringUtil.format(invoiceName));
					result.setId(deliveryNoteId);
					result.setBusinessName(StringUtil.format(businessName));
					result.setCreatedDate(DateUtils.format(createdDate));
					result.setSalesExecutive(StringUtil.format(fullName));
					result.setPaymentType(StringUtil.format(changedPayments.getPaymentType()));
					result.setBankName(StringUtil.format(changedPayments.getBankName()));
					result.setBranchName(StringUtil.format(changedPayments.getBranchName()));
					result.setChequeNo(StringUtil.format(changedPayments.getChequeNo()));
					result.setBankLocation(StringUtil.format(changedPayments.getBankLocation()));
					result.setPresentPayable(changedPayments.getPresentPayable());
					result.setPresentAdvance(changedPayments.getPresentAdvance());
					result.setPreviousCredit(changedPayments.getPreviousCredit());
					result.setTotalPayable(changedPayments.getTotalPayable());
					result.setPresentPayment(changedPayments.getPresentPayment());
					result.setBalance(changedPayments.getBalance());
					
					resultList.add(result);
				}
				Collections.sort(resultList);
			} else {
				result = new DeliveryNoteViewResult();
				result.setInvoiceNo(StringUtil.format(invoiceNo));
				result.setInvoiceName(StringUtil.format(invoiceName));
				result.setBusinessName(StringUtil.format(businessName));
				result.setBusinessName(businessName);
				result.setId(deliveryNoteId);
				result.setCrDescription(crDescription);
				result.setCreatedDate(DateUtils.format(createdDate));
				result.setSalesExecutive(StringUtil.format(fullName));
				result.setPaymentType(StringUtil.format(changedPayments.getPaymentType()));
				result.setBankName(StringUtil.format(changedPayments.getBankName()));
				result.setBranchName(StringUtil.format(changedPayments.getBranchName()));
				result.setChequeNo(StringUtil.format(changedPayments.getChequeNo()));
				result.setBankLocation(StringUtil.format(changedPayments.getBankLocation()));
				result.setPresentPayable(changedPayments.getPresentPayable());
				result.setPresentAdvance(changedPayments.getPresentAdvance());
				result.setPreviousCredit(changedPayments.getPreviousCredit());
				result.setTotalPayable(changedPayments.getTotalPayable());
				result.setPresentPayment(changedPayments.getPresentPayment());
				result.setBalance(changedPayments.getBalance());
				
				resultList.add(result);
			}
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", resultList.size());
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
	 * This method is responsible for Approving Delivery Note CR.
	 * 
	 * @param deliverNoteCRId - {@link Integer}
	 * @param status - {@link String}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void getApprovedDeliveyNoteCR(Integer deliverNoteCRId,
			String status, VbOrganization vbOrganization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, deliverNoteCRId);
		if (vbDeliveryNoteChangeRequest != null) {
			Transaction txn = null;
			try {
				Date date = new Date();
				txn = session.beginTransaction();
				String changeRequestInvoiceNumber = vbDeliveryNoteChangeRequest.getInvoiceNo();
				String createdBy = vbDeliveryNoteChangeRequest.getCreatedBy();
				String businessName = vbDeliveryNoteChangeRequest.getBusinessName();
				VbDeliveryNote vbDeliveryNote = null;
				List<VbDeliveryNoteChangeRequestProducts> changedProductsSet = null;
				List<VbDeliveryNoteChangeRequestPayments> changedPaymentsSet = null;
				if (CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
					//perform debit txn amount reverting back for unique invoice operation
					revertDebitTxnAmount(session,businessName,changeRequestInvoiceNumber,vbOrganization,userName);
					//perform credit txn amount reverting back operation
					Float advCompaensateBalance = revertCreditTxnAmount(session,businessName,changeRequestInvoiceNumber,userName,vbOrganization);
					
					// updating New CR Data vbDeliveryNote,vbDeliveryNotePayments,vbCustomerCreditInfo,vbCustomerAdvanceInfo,Credit Txn, Debit Txn for Approval Invoice Number
					vbDeliveryNote = (VbDeliveryNote) session.createCriteria(VbDeliveryNote.class)
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("vbOrganization", vbOrganization))
							.add(Restrictions.eq("createdBy", createdBy))
							.add(Restrictions.eq("invoiceNo", changeRequestInvoiceNumber))
							.add(Restrictions.eq("flag", new Integer(1)))
							.uniqueResult();
					if (vbDeliveryNote != null) {
						// update VbDeliveryNoteProducts,vbDeliveryNotePayments based on VbDeliveryChangeRequestProducts,vbDeliveryNoteChangeRequestPayments via reference as invoiceNumber
						changedProductsSet = new ArrayList<VbDeliveryNoteChangeRequestProducts>(vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestProductses());
						changedPaymentsSet = new ArrayList<VbDeliveryNoteChangeRequestPayments>(vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestPaymentses());
						VbSalesBook salesBook = getVbSalesBook(session, vbDeliveryNote.getCreatedBy(), vbOrganization);
						if (salesBook == null) {
							salesBook = getVbSalesBookNoFlag(session, vbDeliveryNote.getCreatedBy(), vbOrganization);
						}
						// Check CR is Payment Collection Or Delivery Note Product Sold
						if (vbDeliveryNoteChangeRequest.getInvoiceNo().contains("COLLECTION")) {
							// updating vbDeliveryNote
							vbDeliveryNote.setFlag(new Integer(0));
							session.update(vbDeliveryNote);
							// persist vb_delivery_note with changed payments
							// collection
							VbDeliveryNotePayments instanceCollectionPayments = new VbDeliveryNotePayments();
							persistVbDeliveryNotePaymentsCollection(session, salesBook, vbDeliveryNote, instanceCollectionPayments,
									vbDeliveryNoteChangeRequest, changedPaymentsSet, userName, vbOrganization);
							// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
							updateCustomerCreditInfo(session, vbDeliveryNote, instanceCollectionPayments, vbOrganization, userName);
							// Updating temp tables(vb_customer_credit_info andvb_customer_advance_info) ----> END.
						} else {
							// Persisting New CR Data vbDeliveryNote,vbDeliveryNoteProduct,vbDeliveryNotePayments,vbCustomerCreditInfo,vbCustomerAdvanceInfo,Credit
							// update previous DN with flag=0
							vbDeliveryNote.setFlag(new Integer(0));
							session.update(vbDeliveryNote);

							VbDeliveryNotePayments instancePayments = new VbDeliveryNotePayments();
							// persist vbDeliveryNote,VbDeliveryNoteProducts,vbDeliveryNotePayments based on VbDeliveryChangeRequestProducts,vbDeliveryNoteChangeRequestPayments
							// via reference as invoiceNumber with flag=1 with DeliveryNote CR data
							persistVbDeliveryNoteProductSold(session, salesBook, vbDeliveryNote, instancePayments, vbDeliveryNoteChangeRequest,
									changedProductsSet, changedPaymentsSet, userName, vbOrganization);
							// updating customer credits info
							// Txn,Debit Txn for Approval Invoice Number
							VbCustomerCreditTransaction creditTxn = new VbCustomerCreditTransaction();
							VbCustomerCreditInfo creditInfo = new VbCustomerCreditInfo();
							if (creditInfo != null) {
								creditInfo.setBusinessName(businessName);
								String customerCreditInvoiceNo = generateCustomerCreditInfoInvoiceNumber(vbOrganization);
								creditInfo.setInvoiceNo(customerCreditInvoiceNo);
								creditInfo.setCreditFrom(vbDeliveryNoteChangeRequest.getInvoiceNo());
								creditInfo.setCreatedBy(userName);
								creditInfo.setCreatedOn(date);
								creditInfo.setModifiedOn(date);
								Float balance = instancePayments.getPresentPayable() - instancePayments.getPresentPayment();
								Float advance = instancePayments.getPresentAdvance();
								// persist customer credit Txn basic information
								creditTxn.setCreditFrom(vbDeliveryNote.getInvoiceNo());
								creditTxn.setBusinessName(businessName);
								creditTxn.setDebitTo(customerCreditInvoiceNo);
								creditTxn.setCreatedBy(userName);
								creditTxn.setModifiedBy(userName);
								creditTxn.setCreatedOn(date);
								creditTxn.setFlag(new Integer(1));
								creditTxn.setModifiedOn(date);
								if (advance < balance) {
									Float remainingBalance = balance - advance;
									creditInfo.setBalance(remainingBalance);
									creditInfo.setDue(remainingBalance - advCompaensateBalance);
									// persist customer credits Txn amount
									creditTxn.setAmount(remainingBalance);
									creditTxn.setVbOrganization(vbOrganization);
									creditInfo.setVbOrganization(vbOrganization);
									session.save(creditInfo);
									session.save(creditTxn);

									// For updating advanceInfo.
									while (advance != 0) {
										VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, vbOrganization);
										VbCustomerDebitTransaction customerDebitTxnRecord = new VbCustomerDebitTransaction();
										if (advanceInfo != null) {
											float advBalance = advanceInfo.getBalance();
											if (advance > advBalance) {
												advance = advance - advBalance;
												customerDebitTxnRecord.setAmount(advBalance);
												advanceInfo.setBalance(new Float(0));
											} else {
												advanceInfo.setBalance(advBalance - advance);
												customerDebitTxnRecord.setAmount(advBalance);
												advance = new Float(0);
											}
											// customerDebitTxnRecord.setAmount(advanceInfo.getBalance());
											advanceInfo.setModifiedBy(userName);
											advanceInfo.setModifiedOn(date);
											advanceInfo.setDebitTo(vbDeliveryNote.getInvoiceName());
											
											if (_logger.isDebugEnabled()) {
												_logger.debug("Updating advanceInfo: {}",advanceInfo);
											}
											session.update(advanceInfo);
											
											customerDebitTxnRecord.setCreditFrom(advanceInfo.getInvoiceNo());
											customerDebitTxnRecord.setBusinessName(businessName);
											customerDebitTxnRecord.setDebitTo(vbDeliveryNoteChangeRequest.getInvoiceNo());
											customerDebitTxnRecord.setCreatedBy(userName);
											customerDebitTxnRecord.setCreatedOn(date);
											customerDebitTxnRecord.setFlag(new Integer(1));
											customerDebitTxnRecord.setModifiedOn(date);
											customerDebitTxnRecord.setVbOrganization(vbOrganization);
											
											if (_logger.isDebugEnabled()) {
												_logger.debug("Persisting VbCustomerDebitTransaction.");
											}
											session.save(customerDebitTxnRecord);
										}
									}// while
								} else {
									Float remainingBalance = advance - balance;
									creditInfo.setDue(new Float("0"));
									VbCustomerCreditInfo vbCustomerCreditInfo = null;
									while (remainingBalance != 0) {
										vbCustomerCreditInfo = getPreviousCreditByBusinessName(session, businessName, vbOrganization);
										VbCustomerDebitTransaction vbDebitTransaction = null;
										if (vbCustomerCreditInfo != null) {
											vbDebitTransaction = new VbCustomerDebitTransaction();
											vbCustomerCreditInfo.setModifiedBy(userName);
											vbCustomerCreditInfo.setModifiedOn(date);
											Float due = vbCustomerCreditInfo.getDue();
											if (due < remainingBalance) {
												remainingBalance = remainingBalance - due;
												vbDebitTransaction.setAmount(remainingBalance);
												vbCustomerCreditInfo.setDue(new Float(0));
											} else {
												vbCustomerCreditInfo.setDue(due - remainingBalance);
												vbDebitTransaction.setAmount(remainingBalance);
												remainingBalance = new Float(0);
											}
											vbCustomerCreditInfo.setDebitTo(vbDeliveryNote.getInvoiceName());
											
											if (_logger.isDebugEnabled()) {
												_logger.debug("Updating VbCustomerCreditInfo.");
											}
											session.update(vbCustomerCreditInfo);
											
											// persist customer credit Txn basic information
											vbDebitTransaction.setCreditFrom(vbCustomerCreditInfo.getInvoiceNo());
											vbDebitTransaction.setDebitTo(vbDeliveryNote.getInvoiceNo());
											vbDebitTransaction.setBusinessName(businessName);
											vbDebitTransaction.setCreatedBy(userName);
											vbDebitTransaction.setCreatedOn(date);
											vbDebitTransaction.setFlag(new Integer(1));
											vbDebitTransaction.setModifiedOn(date);
											vbDebitTransaction.setVbOrganization(vbOrganization);

											
											if (_logger.isDebugEnabled()) {
												_logger.debug("Persisting VbDebitTransaction.");
											}
											session.save(vbDebitTransaction);
										} else {
											VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, vbOrganization);
											VbCustomerDebitTransaction vbDebitTransactionAdvance = null;
											if (advanceInfo != null) {
												// Updating existing advance.
												vbDebitTransactionAdvance = new VbCustomerDebitTransaction();
												Float previousAdvance = advanceInfo.getBalance();
												if (previousAdvance == remainingBalance) {
													advanceInfo.setBalance(new Float(0));
												} else {
													remainingBalance = remainingBalance - previousAdvance;
													if (remainingBalance < 0) {
														remainingBalance = -(remainingBalance);
														vbDebitTransactionAdvance.setAmount(previousAdvance);
														advanceInfo.setBalance(previousAdvance - remainingBalance);
													} else {
														vbDebitTransactionAdvance.setAmount(previousAdvance);
														advanceInfo.setBalance(previousAdvance + remainingBalance);
													}
												}
												advanceInfo.setModifiedBy(userName);
												advanceInfo.setModifiedOn(date);
												advanceInfo.setDebitTo(vbDeliveryNote.getInvoiceNo());
												
												if (_logger.isDebugEnabled()) {
													_logger.debug("Updating VbCustomerAdvanceInfo.");
												}
												session.update(advanceInfo);
												
												// persist customer credit Txn basic information
												vbDebitTransactionAdvance.setCreditFrom(customerCreditInvoiceNo);
												vbDebitTransactionAdvance.setBusinessName(businessName);
												vbDebitTransactionAdvance.setDebitTo(vbDeliveryNote.getInvoiceNo());
												vbDebitTransactionAdvance.setCreatedBy(userName);
												vbDebitTransactionAdvance.setCreatedOn(date);
												vbDebitTransactionAdvance.setModifiedOn(date);
												vbDebitTransactionAdvance.setFlag(new Integer(1));
												vbDebitTransactionAdvance.setVbOrganization(vbOrganization);

												if (_logger.isDebugEnabled()) {
													_logger.debug("Persisting VbDebitTransaction.");
												}
												session.save(vbDebitTransactionAdvance);
												
											} else {
												VbCustomerCreditTransaction creditTransaction = new VbCustomerCreditTransaction();
												// If there is no advance for businessName creating new record.
												advanceInfo = new VbCustomerAdvanceInfo();
												advanceInfo.setAdvance(remainingBalance);
												advanceInfo.setBalance(remainingBalance);
												advanceInfo.setBusinessName(businessName);
												String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(vbOrganization);
												advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
												advanceInfo.setCreatedBy(userName);
												advanceInfo.setCreatedOn(date);
												advanceInfo.setCreditFrom(vbDeliveryNote.getInvoiceNo());
												advanceInfo.setModifiedOn(date);
												advanceInfo.setVbOrganization(vbOrganization);
												
												if (_logger.isDebugEnabled()) {
													_logger.debug("Persisting VbCustomerCreditTransaction.");
												}
												session.save(advanceInfo);
												
												// persisting Customer credit Txn for new Advance Info.
												creditTransaction.setCreditFrom(vbDeliveryNote.getInvoiceNo());
												creditTransaction.setBusinessName(businessName);
												creditTransaction.setDebitTo(advanceInfoInvoiceNumber);
												creditTransaction.setAmount(remainingBalance);
												creditTransaction.setCreatedBy(userName);
												creditTransaction.setCreatedOn(date);
												creditTransaction.setModifiedOn(date);
												creditTransaction.setFlag(new Integer(1));
												creditTransaction.setVbOrganization(vbOrganization);
												
												if (_logger.isDebugEnabled()) {
													_logger.debug("Persisting VbCustomerAdvanceInfo.");
												}
												session.save(creditTransaction);
											}
											// Updating remaining balance.
											remainingBalance = new Float(0);
										}
									}
								}
							}
						}
					} 
					vbDeliveryNoteChangeRequest.setStatus(CRStatus.APPROVED.name());
				} else {
					vbDeliveryNoteChangeRequest.setStatus(CRStatus.DECLINE.name());
				}

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating vbDeliveryNoteChangeRequest");
				}
				vbDeliveryNoteChangeRequest.setModifiedBy(userName);
				vbDeliveryNoteChangeRequest.setModifiedOn(new Date());
				session.update(vbDeliveryNoteChangeRequest);

				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, vbDeliveryNoteChangeRequest.getVbSalesBook()
								.getSalesExecutive(), vbOrganization, ENotificationTypes.DN_TXN_CR.name(), status,
						vbDeliveryNoteChangeRequest.getInvoiceNo(), businessName);
				txn.commit();
			} catch (HibernateException exception) {
				if(txn != null) {
					txn.rollback();
				}
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				if(_logger.isErrorEnabled()) {
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

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**This method is responsible for reverting credit txn amounts based on DN CR invoiceNumber as debitTo
	 * 
	 * @param session session - {@link Session}
	 * @param businessName  - {@link String}
	 * @param changeRequestInvoiceNumber  - {@link String}
	 * @param userName  - {@link String}
	 * @param vbOrganization  - {@link VbOrganization}
	 * @return revertedAmount - {@link Float}
	 */
	private Float revertCreditTxnAmount(Session session, String businessName,
			String changeRequestInvoiceNumber, String userName,
			VbOrganization vbOrganization) {
		//credit txn revert back operation
		VbCustomerCreditTransaction customerCreditTxn = null;
		VbCustomerCreditInfo creditTxnCreditRecord = null;
		VbCustomerAdvanceInfo creditTxnAdvanceRecord = null;
		VbCustomerAdvanceInfo advanceInfoData = null;
		VbCustomerCreditInfo creditInfoData = null;
		//fetch credit txn record based on DN CR approval Invoice Number
		Float creditedAmount = new Float(0.00);
		Float creditedDue = new Float(0.00);
		float revertedAmount = new Float(0.00);
		Float advCompaensateBalance = new Float(0.00);
		Date date = new Date();
		String debitTo = null;
		customerCreditTxn = (VbCustomerCreditTransaction) session
				.createCriteria(VbCustomerCreditTransaction.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("creditFrom", changeRequestInvoiceNumber))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (customerCreditTxn != null) {
			Float creditTxnAdvanceAmount = new Float(0.00);
			creditedAmount = customerCreditTxn.getAmount();
			debitTo = customerCreditTxn.getDebitTo();
			VbCustomerCreditTransaction advanceInfoCreditTrx = null;
			creditTxnAdvanceRecord = (VbCustomerAdvanceInfo) session
					.createCriteria(VbCustomerAdvanceInfo.class)
					.add(Restrictions.eq("businessName", businessName))
					.add(Restrictions.eq("invoiceNo", debitTo))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			if (creditTxnAdvanceRecord != null) {
				Float advanceBalance = creditTxnAdvanceRecord.getBalance();
				creditTxnAdvanceAmount = advanceBalance - creditedAmount;
				if (creditTxnAdvanceAmount >= 0) {
					creditTxnAdvanceRecord.setBalance(creditTxnAdvanceAmount);
					creditTxnAdvanceRecord.setAdvance(creditTxnAdvanceAmount);
				} else {
					Float advance = creditTxnAdvanceRecord.getAdvance();
					advanceInfoCreditTrx = new VbCustomerCreditTransaction();
					creditTxnAdvanceRecord.setBalance(new Float(0.00));
					creditTxnAdvanceRecord.setAdvance(advance - creditedAmount);

					creditInfoData = new VbCustomerCreditInfo();
					Float amount = -(creditTxnAdvanceAmount);
					creditInfoData.setDue(amount);
					creditInfoData.setBalance(amount);
					creditInfoData.setBusinessName(businessName);
					String customerCreditInvoiceNo = generateCustomerCreditInfoInvoiceNumber(vbOrganization);
					creditInfoData.setInvoiceNo(customerCreditInvoiceNo);
					creditInfoData.setCreatedBy(userName);
					creditInfoData.setCreatedOn(date);
					creditInfoData.setCreditFrom(changeRequestInvoiceNumber);
					creditInfoData.setModifiedOn(date);
					creditInfoData.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("persisting VbCustomerCreditInfo.", creditInfoData);
					}
					session.save(creditInfoData);
					
					// persisting VbCustomerCreditTxn
					advanceInfoCreditTrx.setCreditFrom(changeRequestInvoiceNumber);
					advanceInfoCreditTrx.setBusinessName(businessName);
					advanceInfoCreditTrx.setDebitTo(customerCreditInvoiceNo);
					advanceInfoCreditTrx.setAmount(amount);
					advanceInfoCreditTrx.setCreatedBy(userName);
					advanceInfoCreditTrx.setCreatedOn(date);
					advanceInfoCreditTrx.setModifiedOn(date);
					advanceInfoCreditTrx.setFlag(new Integer(1));
					advanceInfoCreditTrx.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCustomerCreditTransaction.", advanceInfoCreditTrx);
					}
					session.save(advanceInfoCreditTrx);
				}
				session.update(creditTxnAdvanceRecord);
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerAdvanceInfo.");
				}
			} 
			// check same Customer Credit debitTo in Customer Credit Info
			creditTxnCreditRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
					.add(Restrictions.eq("businessName", businessName))
					.add(Restrictions.eq("invoiceNo", debitTo))
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.uniqueResult();
			
			if (creditTxnCreditRecord != null) {
				creditedDue = creditTxnCreditRecord.getDue();
				Float creditBalance = creditTxnCreditRecord.getBalance() - creditedAmount;
				VbCustomerCreditTransaction creditInfoCreditTrx = null;
				// condition for reverting -ve value to credit info due.
				revertedAmount = creditedDue - creditedAmount;
				if (revertedAmount >= 0) {
					creditTxnCreditRecord.setDue(revertedAmount);
					creditTxnCreditRecord.setBalance(revertedAmount);
				} else {
					advanceInfoData = new VbCustomerAdvanceInfo();
					creditInfoCreditTrx = new VbCustomerCreditTransaction();
					creditTxnCreditRecord.setDue(new Float(0.00));
					creditTxnCreditRecord.setBalance(creditBalance);
					float formatRevertAmount = -(revertedAmount);
					advanceInfoData.setAdvance(formatRevertAmount);
					advanceInfoData.setBalance(formatRevertAmount);
					advanceInfoData.setBusinessName(businessName);
					String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(vbOrganization);
					advanceInfoData.setInvoiceNo(advanceInfoInvoiceNumber);
					advanceInfoData.setCreatedBy(userName);
					advanceInfoData.setCreatedOn(date);
					advanceInfoData.setCreditFrom(changeRequestInvoiceNumber);
					advanceInfoData.setModifiedOn(date);
					advanceInfoData.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("persisting VbCustomerAdvanceInfo." , advanceInfoData);
					}
					session.save(advanceInfoData);
					
					// persisting VbCustomerCreditTxn
					creditInfoCreditTrx.setCreditFrom(changeRequestInvoiceNumber);
					creditInfoCreditTrx.setBusinessName(businessName);
					creditInfoCreditTrx.setDebitTo(advanceInfoInvoiceNumber);
					creditInfoCreditTrx.setAmount(-(revertedAmount));
					creditInfoCreditTrx.setCreatedBy(userName);
					creditInfoCreditTrx.setCreatedOn(date);
					creditInfoCreditTrx.setModifiedOn(date);
					creditInfoCreditTrx.setFlag(new Integer(1));
					creditInfoCreditTrx.setVbOrganization(vbOrganization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCustomerCreditTransaction." , creditInfoCreditTrx);
					}
					session.save(creditInfoCreditTrx);
				}
				session.update(creditTxnCreditRecord);
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerCreditInfo." , creditTxnCreditRecord);
				}
			} 
			customerCreditTxn.setModifiedBy(userName);
			customerCreditTxn.setModifiedOn(new Date());
			customerCreditTxn.setFlag(new Integer(0));
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomerCreditTransaction." , customerCreditTxn);
			}
			session.update(customerCreditTxn);
		} 
		// after reverting back if there is some amount left compensate with credit amounts
		if (revertedAmount != 0 || revertedAmount < 0) {
			VbCustomerAdvanceInfo advanceInfoRevert = getPreviousAdvanceByBusinessName(session, businessName, vbOrganization);
			VbCustomerDebitTransaction customerDebitTxnRecord = new VbCustomerDebitTransaction();
			if (advanceInfoRevert != null) {
				advCompaensateBalance = advanceInfoRevert.getBalance();
				advanceInfoRevert.setBalance(new Float(0.00));
				advanceInfoRevert.setAdvance(new Float(0.00));
				customerDebitTxnRecord.setAmount(advCompaensateBalance);
				advanceInfoRevert.setModifiedBy(userName);
				advanceInfoRevert.setModifiedOn(date);
				advanceInfoRevert.setDebitTo(changeRequestInvoiceNumber);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating advanceInfo.");
				}
				session.update(advanceInfoRevert);
				
				customerDebitTxnRecord.setCreditFrom(advanceInfoRevert.getInvoiceNo());
				customerDebitTxnRecord.setBusinessName(businessName);
				customerDebitTxnRecord.setDebitTo(changeRequestInvoiceNumber);
				customerDebitTxnRecord.setCreatedBy(userName);
				customerDebitTxnRecord.setCreatedOn(date);
				customerDebitTxnRecord.setFlag(new Integer(1));
				customerDebitTxnRecord.setModifiedOn(date);
				customerDebitTxnRecord.setVbOrganization(vbOrganization);
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbCustomerDebitTransaction.");
				}
				session.save(customerDebitTxnRecord);
			}
		}
		return advCompaensateBalance;
	}

	/**This method is responsible for revert back debit txn amount to respective creditInfo and advanceInfo based on creitForm invoiceNumber
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param changeRequestInvoiceNumber - {@link String}
 	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 */
	@SuppressWarnings("unchecked")
	private void revertDebitTxnAmount(Session session, String businessName,
			String changeRequestInvoiceNumber, VbOrganization vbOrganization,String userName) {
		//debit txn revert back operation
		List<VbCustomerDebitTransaction> customerDebitTxn = null;
		VbCustomerCreditInfo debitTxnCreditRecord = null;
		VbCustomerAdvanceInfo debitTxnAdvanceRecord = null;
		//fetch debit txn record based on DN CR approval invoice number
		customerDebitTxn = (List<VbCustomerDebitTransaction>) session.createCriteria(VbCustomerDebitTransaction.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("debitTo", changeRequestInvoiceNumber))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.list();

		if (!customerDebitTxn.isEmpty()) {
			Float debitedAmount = new Float(0.00);
			String creditFrom = null;
			Float debitTxnCreditDueAmount = new Float(0.00);
			Float debitTxnAdvanceBalanceAmount = new Float(0.00);
			// latest change to revert back more than two records from debit txn table
			for (VbCustomerDebitTransaction debitTxn : customerDebitTxn) {
				debitedAmount = debitTxn.getAmount();
				creditFrom = debitTxn.getCreditFrom();
				debitTxnCreditRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
						.add(Restrictions.eq("businessName", businessName))
						.add(Restrictions.eq("invoiceNo", creditFrom))
						.add(Restrictions.eq("vbOrganization", vbOrganization))
						.uniqueResult();
				
				if (debitTxnCreditRecord != null) {
					debitTxnCreditDueAmount=debitedAmount + debitTxnCreditRecord.getDue();
					debitTxnCreditRecord.setDue(debitTxnCreditDueAmount);
					session.update(debitTxnCreditRecord);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo.");
					}
				} 
				debitTxnAdvanceRecord = (VbCustomerAdvanceInfo) session.createCriteria(VbCustomerAdvanceInfo.class)
						.add(Restrictions.eq("businessName", businessName))
						.add(Restrictions.eq("invoiceNo", creditFrom))
						.add(Restrictions.eq("vbOrganization", vbOrganization))
						.uniqueResult();
				if (debitTxnAdvanceRecord != null) {
					debitTxnAdvanceBalanceAmount=debitedAmount + debitTxnAdvanceRecord.getBalance();
					debitTxnAdvanceRecord.setBalance(debitTxnAdvanceBalanceAmount);
					session.update(debitTxnAdvanceRecord);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerAdvanceInfo.");
					}
				} 
				debitTxn.setModifiedBy(userName);
				debitTxn.setModifiedOn(new Date());
				debitTxn.setFlag(new Integer(0));
				session.update(debitTxn);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbCustomerDebitTransaction.");
				}
			}
		} 
	}

	/**
	 * This method is responsible for persisting
	 * vb_delivery_note,vb_deliver_note_products,vb_delivery_note_payments if
	 * CR'd Delivery note is Products Sold not payments collection.
	 * 
	 * @param session -{@link Session}
	 * @param salesBook -{@link VbSalesBook}
	 * @param vbDeliveryNote -{@link VbDeliveryNote}
	 * @param vbDeliveryNoteChangeRequest -{@link VbDeliveryNoteChangeRequest}
	 * @param changedProductsSet -{@link List<VbDeliveryNoteChangeRequestProducts>}
	 * @param changedPaymentsSet -{@link List<VbDeliveryNoteChangeRequestPayments>}
	 * @param userName -{@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 */
	private void persistVbDeliveryNoteProductSold(Session session,
			VbSalesBook salesBook, VbDeliveryNote vbDeliveryNote,
			VbDeliveryNotePayments instancePayments,
			VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest,
			List<VbDeliveryNoteChangeRequestProducts> changedProductsSet,
			List<VbDeliveryNoteChangeRequestPayments> changedPaymentsSet,
			String userName, VbOrganization vbOrganization) {
		Date date = new Date();
		VbDeliveryNote deliveryNote = new VbDeliveryNote();
		VbDeliveryNoteProducts deliveryNoteProducts = null;
		if (deliveryNote != null) {
			deliveryNote.setCreatedOn(date);
			deliveryNote.setCreatedBy(vbDeliveryNote.getCreatedBy());
			deliveryNote.setModifiedOn(date);
			deliveryNote.setVbOrganization(vbOrganization);
			deliveryNote.setFlag(new Integer(1));

			deliveryNote.setVbSalesBook(salesBook);
			deliveryNote.setBusinessName(vbDeliveryNoteChangeRequest.getBusinessName());
			if (vbDeliveryNoteChangeRequest.getInvoiceName().contains(",1")) {
				deliveryNote.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",1", ""));
			} else {
				deliveryNote.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",0", ""));
			}
			deliveryNote.setInvoiceNo(vbDeliveryNoteChangeRequest.getInvoiceNo());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNote.");
			}
			session.save(deliveryNote);

			// persisting vbDeliveryNoteProducts Changed in DN CR
			for (VbDeliveryNoteChangeRequestProducts deliveryNoteChangedProduct : changedProductsSet) {
				deliveryNoteProducts = new VbDeliveryNoteProducts();
				Float productCost = new Float(0.0);
				Integer productQty = new Integer(0);
				if (deliveryNoteChangedProduct.getProductCost().contains(",1")) {
					productCost = Float.parseFloat(deliveryNoteChangedProduct.getProductCost().replace(",1", ""));
					deliveryNoteProducts.setProductCost(productCost);
				} else {
					productCost = Float.parseFloat(deliveryNoteChangedProduct.getProductCost().replace(",0", ""));
					deliveryNoteProducts.setProductCost(productCost);
				}
				String productName = deliveryNoteChangedProduct.getProductName().replace(",0", "");
				deliveryNoteProducts.setProductName(productName);
				deliveryNoteProducts.setBatchNumber(deliveryNoteChangedProduct.getBatchNumber().replace(",0", ""));

				if (deliveryNoteChangedProduct.getProductQty().contains(",1")) {
					productQty = Integer.parseInt(deliveryNoteChangedProduct.getProductQty().replace(",1", ""));
					deliveryNoteProducts.setProductQty(productQty);
				} else {
					productQty = Integer.parseInt(deliveryNoteChangedProduct.getProductQty().replace(",0", ""));
					deliveryNoteProducts.setProductQty(productQty);
				}
				Float totalCost = productCost * productQty;
				deliveryNoteProducts.setTotalCost(totalCost);
				// bonusQty and bonusReason optional fields
				if (deliveryNoteChangedProduct.getBonusQty().contains(",1")) {
					if (deliveryNoteChangedProduct.getBonusQty().replace(",1", "").equals("")) {
						deliveryNoteProducts.setBonusQty(new Integer(0));
					} else {
						Integer bonusQty = Integer.parseInt(deliveryNoteChangedProduct.getBonusQty().replace(",1", ""));
						deliveryNoteProducts.setBonusQty(bonusQty);
					}
				} else {
					if (deliveryNoteChangedProduct.getBonusQty().contains(",0")) {
						deliveryNoteProducts.setBonusQty(new Integer(0));
					} else {
						Integer bonusQty = Integer.parseInt(deliveryNoteChangedProduct.getBonusQty().replace(",0", ""));
						deliveryNoteProducts.setBonusQty(bonusQty);
					}
				}
				if (deliveryNoteChangedProduct.getBonusReason().contains(",1")) {
					deliveryNoteProducts.setBonusReason(deliveryNoteChangedProduct.getBonusReason().replace(",1", ""));
				} else {
					deliveryNoteProducts.setBonusReason(deliveryNoteChangedProduct.getBonusReason().replace(",0", ""));
				}
				deliveryNoteProducts.setVbDeliveryNote(deliveryNote);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteProducts.");
				}
				session.save(deliveryNoteProducts);
			}
		}
		// persisting vbDeliveryNotePayments Changed in DN CR
		for (VbDeliveryNoteChangeRequestPayments deliveryNoteChangedPayments : changedPaymentsSet) {
			if (deliveryNote != null) {
				if (deliveryNoteChangedPayments.getBalance().contains(",1")) {
					instancePayments.setBalance(Float.parseFloat(deliveryNoteChangedPayments.getBalance().replace(",1", "")));
				} else {
					instancePayments.setBalance(Float.parseFloat(deliveryNoteChangedPayments.getBalance().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getTotalPayable().contains(",1")) {
					instancePayments.setTotalPayable(Float.parseFloat(deliveryNoteChangedPayments.getTotalPayable().replace(",1", "")));
				} else {
					instancePayments.setTotalPayable(Float.parseFloat(deliveryNoteChangedPayments.getTotalPayable().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getPresentPayment().contains(",1")) {
					instancePayments.setPresentPayment(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayment().replace(",1", "")));
				} else {
					instancePayments.setPresentPayment(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayment().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getPaymentType().contains(",1")) {
					instancePayments.setPaymentType(deliveryNoteChangedPayments.getPaymentType().replace(",1", ""));
				} else {
					instancePayments.setPaymentType(deliveryNoteChangedPayments.getPaymentType().replace(",0", ""));
				}
				if (deliveryNoteChangedPayments.getPresentPayable().contains(",1")) {
					instancePayments.setPresentPayable(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayable().replace(",1", "")));
				} else {
					instancePayments.setPresentPayable(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayable().replace(",0", "")));
				}
				instancePayments.setPresentAdvance(Float.parseFloat(deliveryNoteChangedPayments.getPresentAdvance()));
				instancePayments.setPreviousCredit(Float.parseFloat(deliveryNoteChangedPayments.getPreviousCredit()));
				instancePayments.setBankName(deliveryNoteChangedPayments.getBankName());
				instancePayments.setChequeNo(deliveryNoteChangedPayments.getChequeNo());
				instancePayments.setBranchName(deliveryNoteChangedPayments.getBranchName());
				instancePayments.setBankLocation(deliveryNoteChangedPayments.getBankLocation());
				instancePayments.setVbDeliveryNote(deliveryNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNotePayments.");
				}
				session.save(instancePayments);
			}
		}
	}

	/**
	 * This method is responsible for persisting delivery note CR payments
	 * collection data into vb_deliver_note and vb_delivery_note_payments table.
	 * 
	 * @param session -{@link Session}
	 * @param salesBook -{@link VbSalesBook}
	 * @param vbDeliveryNote -{@link VbDeliveryNote}
	 * @param vbDeliveryNoteChangeRequest -{@link VbDeliveryNoteChangeRequest}
	 * @param changedPaymentsSet -{@link List<VbDeliveryNoteChangeRequestPayments>}
	 * @param userName -{@link String}
	 * @param vbOrganization -{@link VbOrganization}
	 */
	private void persistVbDeliveryNotePaymentsCollection(Session session,
			VbSalesBook salesBook, VbDeliveryNote vbDeliveryNote,
			VbDeliveryNotePayments instancePayments,
			VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest,
			List<VbDeliveryNoteChangeRequestPayments> changedPaymentsSet,
			String userName, VbOrganization vbOrganization) {
		VbDeliveryNote deliveryNote = new VbDeliveryNote();
		Date date = new Date();
		if (deliveryNote != null) {
			deliveryNote.setCreatedOn(date);
			deliveryNote.setCreatedBy(vbDeliveryNote.getCreatedBy());
			deliveryNote.setModifiedOn(date);
			deliveryNote.setVbOrganization(vbOrganization);
			deliveryNote.setFlag(new Integer(1));
			deliveryNote.setVbSalesBook(salesBook);
			deliveryNote.setBusinessName(vbDeliveryNoteChangeRequest.getBusinessName());
			if (vbDeliveryNoteChangeRequest.getInvoiceName().contains(",1")) {
				deliveryNote.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",1", ""));
			} else {
				deliveryNote.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",0", ""));
			}
			deliveryNote.setInvoiceNo(vbDeliveryNoteChangeRequest.getInvoiceNo());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNote.");
			}
			session.save(deliveryNote);
		}

		// persist vb_delivery_note_payments Delivery_note payment collection data.
		for (VbDeliveryNoteChangeRequestPayments deliveryNoteChangedPayments : changedPaymentsSet) {
			if (deliveryNote != null) {
				if (deliveryNoteChangedPayments.getBalance().contains(",1")) {
					instancePayments.setBalance(Float.parseFloat(deliveryNoteChangedPayments.getBalance().replace(",1", "")));
				} else {
					instancePayments.setBalance(Float.parseFloat(deliveryNoteChangedPayments.getBalance().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getTotalPayable().contains(",1")) {
					instancePayments.setTotalPayable(Float.parseFloat(deliveryNoteChangedPayments.getTotalPayable().replace(",1", "")));
				} else {
					instancePayments.setTotalPayable(Float.parseFloat(deliveryNoteChangedPayments.getTotalPayable().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getPresentPayment().contains(",1")) {
					instancePayments.setPresentPayment(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayment().replace(",1", "")));
				} else {
					instancePayments.setPresentPayment(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayment().replace(",0", "")));
				}
				if (deliveryNoteChangedPayments.getPaymentType().contains(",1")) {
					instancePayments.setPaymentType(deliveryNoteChangedPayments.getPaymentType().replace(",1", ""));
				} else {
					instancePayments.setPaymentType(deliveryNoteChangedPayments.getPaymentType().replace(",0", ""));
				}
				if (deliveryNoteChangedPayments.getPresentPayable().contains(",1")) {
					instancePayments.setPresentPayable(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayable().replace(",1", "")));
				} else {
					instancePayments.setPresentPayable(Float.parseFloat(deliveryNoteChangedPayments.getPresentPayable().replace(",0", "")));
				}
				instancePayments.setPresentAdvance(Float.parseFloat(deliveryNoteChangedPayments.getPresentAdvance()));
				instancePayments.setPreviousCredit(Float.parseFloat(deliveryNoteChangedPayments.getPreviousCredit()));
				instancePayments.setBankName(deliveryNoteChangedPayments.getBankName());
				instancePayments.setChequeNo(deliveryNoteChangedPayments.getChequeNo());
				instancePayments.setBranchName(deliveryNoteChangedPayments.getBranchName());
				instancePayments.setBankLocation(deliveryNoteChangedPayments.getBankLocation());
				instancePayments.setVbDeliveryNote(deliveryNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNotePayments.");
				}
				session.save(instancePayments);
			}
		}
	}

	/**
	 * This method is responsible to update {@link VbCustomerAdvanceInfo} and
	 * {@link VbCustomerCreditInfo}.
	 * 
	 * @param session - {@link Session}
	 * @param command - {@link DeliveryNoteCommand}
	 * @param productCommand - {@link DeliveryNoteProductCommand}
	 * @param organization - {@link VbOrganization}
	 * 
	 */
	private void updateCustomerCreditInfo(Session session,
			VbDeliveryNote deliveryNote, VbDeliveryNotePayments payments,
			VbOrganization organization, String userName) {
		Float presentPayment = payments.getPresentPayment();
		VbCustomerDebitTransaction customerDebitTxn = null;
		String businessName = deliveryNote.getBusinessName();
		if (presentPayment > 0) {
			Query customerInfoQuery = null;
			while (presentPayment != 0) {
				customerInfoQuery = session.createQuery(
								"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
								+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
								+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
						.setParameter("businessName", deliveryNote.getBusinessName())
						.setParameter("vbOrganization", organization)
						.setParameter("due", new Float("0"));

				VbCustomerCreditInfo creditInfo = getSingleResultOrNull(customerInfoQuery);
				if (creditInfo != null) {
					// persisting Customer debit Txn
					customerDebitTxn = new VbCustomerDebitTransaction();
					creditInfo.setModifiedBy(userName);
					creditInfo.setModifiedOn(new Date());
					String existingInvoiceNo = creditInfo.getDebitTo();
					String newInvoiceNo = deliveryNote.getInvoiceNo();
					if (existingInvoiceNo != null) {
						creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(newInvoiceNo));
					} else {
						creditInfo.setDebitTo(newInvoiceNo);
					}
					Float existingDue = creditInfo.getDue();
					if (presentPayment < existingDue) {
						creditInfo.setDue(existingDue - presentPayment);
						customerDebitTxn.setAmount(presentPayment);
						presentPayment = new Float(0.0);
					} else {
						presentPayment = presentPayment - existingDue;
						creditInfo.setDue(new Float("0.00"));
						customerDebitTxn.setAmount(existingDue);
					}
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo.");
					}
					session.update(creditInfo);
					
					// persisting Customer Debit Txn for Payback his credit amount.
					customerDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
					customerDebitTxn.setBusinessName(deliveryNote.getBusinessName());
					customerDebitTxn.setDebitTo(deliveryNote.getInvoiceNo());
					customerDebitTxn.setCreatedBy(userName);
					customerDebitTxn.setCreatedOn(new Date());
					customerDebitTxn.setModifiedOn(new Date());
					customerDebitTxn.setFlag(new Integer(1));
					customerDebitTxn.setVbOrganization(organization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerDebitTransaction.");
					}
					session.save(customerDebitTxn);
					// updating credit amount of Customer Credit Txn
				} else {
					saveCustomerAdvanceInfo(session, businessName, deliveryNote.getInvoiceNo(), organization, presentPayment, userName);
					presentPayment = new Float("0");
				}
			}
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
	 * 
	 */
	private void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance,
			String userName) {
		Date date = new Date();
		VbCustomerCreditTransaction customerCreditTxn = null;
		VbCustomerAdvanceInfo advanceInfo = null;
		advanceInfo = new VbCustomerAdvanceInfo();
		customerCreditTxn = new VbCustomerCreditTransaction();
		if (balance < 0) {
			Float newBalance = -(balance);
			advanceInfo.setAdvance(newBalance);
			advanceInfo.setBalance(newBalance);
			customerCreditTxn.setAmount(newBalance);
		} else {
			advanceInfo.setAdvance(balance);
			advanceInfo.setBalance(balance);
			customerCreditTxn.setAmount(balance);
		}
		advanceInfo.setBusinessName(businessName);
		String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(organization);
		advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
		advanceInfo.setCreatedBy(userName);
		advanceInfo.setCreatedOn(date);
		advanceInfo.setModifiedOn(date);
		advanceInfo.setCreditFrom(invoiceNo);
		advanceInfo.setVbOrganization(organization);
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerAdvanceInfo.");
		}
		session.save(advanceInfo);
		
		// persisting Customer Credit txn case of extra amount
		customerCreditTxn.setBusinessName(businessName);
		customerCreditTxn.setCreditFrom(invoiceNo);
		customerCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
		customerCreditTxn.setCreatedBy(userName);
		customerCreditTxn.setCreatedOn(date);
		customerCreditTxn.setModifiedOn(date);
		customerCreditTxn.setFlag(new Integer(1));
		customerCreditTxn.setVbOrganization(organization);

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbCustomerCreditTransaction.");
		}
		session.save(customerCreditTxn);
		// }
	}

	/**
	 * This method is responsible to get {@link VbCustomerAdvanceInfo} based on
	 * minimum of createdOn.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customerAdvanceInfo - {@link VbCustomerAdvanceInfo}
	 * 
	 */
	private VbCustomerAdvanceInfo getPreviousAdvanceByBusinessName(
			Session session, String businessName, VbOrganization organization) {
		VbCustomerAdvanceInfo customerAdvanceInfo = (VbCustomerAdvanceInfo) session.createQuery(
						"FROM VbCustomerAdvanceInfo vb WHERE vb.businessName = :businessName AND vb.vbOrganization = :vbOrganization AND vb.createdOn IN ("
						+ "SELECT MIN(vbc.createdOn) FROM VbCustomerAdvanceInfo vbc WHERE vbc.businessName = :businessName AND "
						+ "vbc.vbOrganization = :vbOrganization AND vbc.balance > :balance)")
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.setParameter("balance", new Float(0))
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.uniqueResult();
		
		return customerAdvanceInfo;
	}
	
	/**
	 * This method is responsible to get {@link VbCustomerCreditInfo} based on
	 * minimum of createdOn.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customerCreditInfo - {@link VbCustomerCreditInfo}
	 * 
	 */
	private VbCustomerCreditInfo getPreviousCreditByBusinessName(
			Session session, String businessName, VbOrganization organization) {
		VbCustomerCreditInfo customerCreditInfo = (VbCustomerCreditInfo) session.createQuery(
						"FROM VbCustomerCreditInfo vb WHERE vb.businessName = :businessName AND vb.vbOrganization = :vbOrganization AND vb.createdOn IN ("
						+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE vbc.businessName = :businessName AND "
						+ "vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.setParameter("due", new Float(0))
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.uniqueResult();
		
		return customerCreditInfo;
	}

	/**
	 * This method is responsible to get {@link VbDeliveryNote} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName -{@link String}
	 * @param invoiceNumber -{@link String}
	 * @return deliveryNoteId -{@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Integer getDeliveryNoteBasedOnInvoiceNo(VbOrganization organization,
			Integer deliveryNoteId, String invoiceNumber, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		Integer originalDeliveryNoteId = new Integer(0);
		Query query = session.createQuery(
						"SELECT MAX(vbd.id) FROM VbDeliveryNote vbd where vbd.invoiceNo LIKE :invoiceNo AND " +
						"vbd.flag= :flag AND vbd.id <= :deliveryNoteId AND vbd.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(1))
				.setParameter("deliveryNoteId", deliveryNoteId)
				.setParameter("vbOrganization", organization);
		Integer deliveryNoteMaxId = getSingleResultOrNull(query);
		if (deliveryNoteMaxId == null) {
			VbDeliveryNote deliveryNote = (VbDeliveryNote) session
					.createCriteria(VbDeliveryNote.class)
					.add(Restrictions.eq("invoiceNo", invoiceNumber))
					.add(Restrictions.eq("flag", new Integer(1)))
					// .add(Restrictions.eq("id", deliveryNoteId))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			originalDeliveryNoteId = deliveryNote.getId();
		} else {
			originalDeliveryNoteId = deliveryNoteMaxId;
		}
		session.close();
		return originalDeliveryNoteId;
	}

	/**
	 * This method is responsible to get {@link VbDeliveryNote} based on
	 * {@link VbOrganization} and invoiceNo and deliveryNoteId.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param deliveryNoteId - {@link Integer}
	 * @param invoiceNumber - {@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * 
	 */
	public String fetchDeliveryNoteCreationTime(Integer deliveryNoteId,
			String invoiceNumber, VbOrganization organization) {
		String isDeliveryNoteCRExpired = "n";
		Session session = this.getSession();
		VbDeliveryNote vbDeliveryNote = (VbDeliveryNote) session
				.createCriteria(VbDeliveryNote.class)
				.add(Restrictions.eq("id", deliveryNoteId))
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (vbDeliveryNote != null) {
			Integer totalMin = DateUtils.getDifferenceDays(new Date(), vbDeliveryNote.getCreatedOn());
			if (totalMin > 10) {
				isDeliveryNoteCRExpired = "y";
			} 
		} 
		return isDeliveryNoteCRExpired;
	}

	/**
	 * This method is responsible to get {@link VbSalesBook} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session - {@link Session}
	 * @param organization  - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getSalesBook(Session session,
			VbOrganization organization, String userName) {
		Query query = session.createQuery(
						"FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization AND vb.salesExecutive = :salesExecutive AND "
						+ "vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName)")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutive", userName)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}

	/**
	 * This method is responsible for getting Approved,Declined,Pending count of
	 * Delivery Note Products and Cash Collection
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName - {@link String}
	 * @return deliveryNoteHistory -{@link DeliveryNoteHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesHistoryResult> getDeliveryNoteTransactionHistory(
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		List<MySalesHistoryResult> historyResults = new ArrayList<MySalesHistoryResult>();
		// approved,decline,pending status count of delivery note
		List<Object[]> deliveryNoteProductSoldResultList = session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.rowCount())
								.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("invoiceNo", "DN", MatchMode.ANYWHERE))
				.list();
		if (!deliveryNoteProductSoldResultList.isEmpty()) {
			MySalesHistoryResult historyResult = null;
			String deliveryNoteType = null;
			String deliveryNoteStatus = null;
			Integer count = null;
			for (Object[] objects : deliveryNoteProductSoldResultList) {
				historyResult = new MySalesHistoryResult();
				deliveryNoteType = (String) objects[0];
				deliveryNoteStatus = (String) objects[1];
				count = (Integer) objects[2];
				if (deliveryNoteType.contains("DN")) {
					if (CRStatus.APPROVED.name().equalsIgnoreCase(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setApprovedDNCount(count);
					} else if (CRStatus.DECLINE.name().equalsIgnoreCase(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setDeclinedDNCount(count);
					} else if (CRStatus.PENDING.name().equalsIgnoreCase(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setPendingDNCount(count);
					}
				}
				historyResults.add(historyResult);
			}
		}
		// approved,decline,pending status count of delivery note cash
		// collection
		List<Object[]> deliveryNoteCollectionResultList = session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.rowCount())
								.add(Projections.groupProperty("status")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("invoiceNo", "COLLECTIONS", MatchMode.ANYWHERE))
				.list();
		if (!deliveryNoteCollectionResultList.isEmpty()) {
			MySalesHistoryResult historyResult = null;
			for (Object[] objects : deliveryNoteCollectionResultList) {
				historyResult = new MySalesHistoryResult();
				String deliveryNoteType = (String) objects[0];
				String deliveryNoteStatus = (String) objects[1];
				Integer count = (Integer) objects[2];
				if (deliveryNoteType.contains("COLLECTIONS")) {
					if (CRStatus.APPROVED.name().equals(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setApprovedCOLLECTIONCount(count);
					} else if (CRStatus.DECLINE.name().equals(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setDeclinedCOLLECTIONCount(count);
					} else if (CRStatus.PENDING.name().equals(deliveryNoteStatus)) {
						historyResult.setDeliveryNoteTransactionType(deliveryNoteType);
						historyResult.setDeliveryNoteTxnStatus(deliveryNoteStatus);
						historyResult.setPendingCOLLECTIONCount(count);
					}
				}
				historyResults.add(historyResult);
			}
		}
		session.close();
		if (!historyResults.isEmpty()) {
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
	 * {@link VbDeliveryNoteChangeRequest} based on status and invoiceNumber
	 * 
	 * @param invoiceNumber
	 * @param status
	 * @param organization
	 * @param userName
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesInvoicesHistoryResult> getDeliveryNoteInvoicesTransactionHistory(
			String invoiceNumber, String status, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		List<Object[]> resultList = session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.property("createdBy"))
								.add(Projections.property("createdOn"))
								.add(Projections.property("modifiedBy"))
								.add(Projections.property("modifiedOn")))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.like("invoiceNo", invoiceNumber, MatchMode.ANYWHERE))
				.add(Restrictions.eq("status", status))
				.list();
		session.close();
		if (!resultList.isEmpty()) {
			Integer deliveryNoteId = null;
			String deliveryNoteInvoiceNo = null;
			String dNstatus = null;
			String createdBy = null;
			Date createdOn = null;
			String modifiedBy = null;
			Date modifiedOn = null;
			MySalesInvoicesHistoryResult invoiceHistoryResult = null;
			List<MySalesInvoicesHistoryResult> invoiceHistoryResults = new ArrayList<MySalesInvoicesHistoryResult>();
			for (Object[] objects : resultList) {
				deliveryNoteId = (Integer) objects[0];
				deliveryNoteInvoiceNo = (String) objects[1];
				dNstatus = (String) objects[2];
				createdBy = (String) objects[3];
				createdOn = (Date) objects[4];
				modifiedBy = (String) objects[5];
				modifiedOn = (Date) objects[6];

				invoiceHistoryResult = new MySalesInvoicesHistoryResult();
				invoiceHistoryResult.setInvoiceNumber(deliveryNoteInvoiceNo);
				invoiceHistoryResult.setRequestedBy(createdBy);
				invoiceHistoryResult.setRequestedDate(createdOn);
				invoiceHistoryResult.setModifiedBy(modifiedBy);
				invoiceHistoryResult.setModifiedDate(modifiedOn);
				invoiceHistoryResult.setStatus(dNstatus);
				invoiceHistoryResult.setId(deliveryNoteId);

				invoiceHistoryResults.add(invoiceHistoryResult);
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", invoiceHistoryResults.size());
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
}
