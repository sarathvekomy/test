/**
 * com.vekomy.vbooks.mysales.cr.dao.JournalCrDao.java
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
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesJournalHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestJournalCommand;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This dao class is responsible to perform operations on Journal change request
 * in sales module.
 * 
 * @author Ankit
 * 
 */

public class JournalCrDao extends MysalesCrBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalCrDao.class);

	/**
	 * This method is used to get all the Journals on current date .
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return resultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getJournalChangeTransaction(VbOrganization organization, String username) throws DataAccessException {
		Date date = new Date();
		Session session = this.getSession();
		List<VbJournal> journalList = session.createCriteria(VbJournal.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
				.add(Restrictions.eq("createdBy", username))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(1)))
				.addOrder(Order.desc("createdOn")).list();
		session.close();
		
		if(!journalList.isEmpty()) {
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", journalList.size());
			}
			return journalList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is Responsible to Check whether The Searched result is
	 * Applicable for SE CR or not.
	 * 
	 * @param journalId - {@link Integer}
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable - {@link String}
	 */
	public String validateSEJournalChangeRequest(String invoiceNo, Integer journalId, VbOrganization organization) {
		String isAvailable = "n";
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal) session.get(VbJournal.class, journalId);
		Date date = new Date();
		if (vbJournal != null) {
			VbJournalChangeRequest vbJournalChangeRequest = (VbJournalChangeRequest) session.createCriteria(VbJournalChangeRequest.class)
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("invoiceNo", invoiceNo))
					.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			
			if (vbJournalChangeRequest != null) {
				isAvailable = "y";
			} 
		}
		session.close();
		
		return isAvailable;
	}

	/**
	 * This method is used to get the Journal Based on Sales Return Id.
	 * 
	 * @param journalId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbJournal - {@link VbJournal}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbJournal getJournalById(Integer journalId, VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal) session.get(VbJournal.class, journalId);
		session.close();
		if(vbJournal != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("vbJournal: {}", vbJournal);
			}
			return vbJournal;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to persist {@link VbJournalChangeRequest} into
	 * database.
	 * 
	 * @param journalCommand - {@link ChangeRequestJournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void saveJournalCR(ChangeRequestJournalCommand journalCommand,
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			String businessName = null;
			String invoiceNo = null;
			// fetch original Journal based on Journal invoice number and journal type
			VbJournal originalJournalCommand = (VbJournal) session.createCriteria(VbJournal.class)
					.add(Restrictions.eq("invoiceNo", journalCommand.getInvoiceNo()))
					.add(Restrictions.eq("journalType", journalCommand.getJournalType()))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("createdBy", userName))
					.add(Restrictions.eq("flag", new Integer(1)))
					.uniqueResult();
			if (originalJournalCommand != null) {
				VbJournalChangeRequest vbJournalInstance = new VbJournalChangeRequest();
				businessName = journalCommand.getBusinessName();
				invoiceNo = journalCommand.getInvoiceNo();
				String originalJournalAmount = Float.toString(originalJournalCommand.getAmount());
				String changedJournalAmount = journalCommand.getAmount();
				if (originalJournalAmount.equals(changedJournalAmount)) {
					vbJournalInstance.setAmount(originalJournalAmount.concat(",0"));
				} else {
					vbJournalInstance.setAmount(changedJournalAmount.concat(",1"));
				}
				String originalJournalInvoiceName = originalJournalCommand.getInvoiceName();
				String changedJournalInvoiceName = journalCommand.getInvoiceName();
				if (originalJournalInvoiceName.equals(changedJournalInvoiceName)) {
					vbJournalInstance.setInvoiceName(originalJournalInvoiceName.concat(",0"));
				} else {
					vbJournalInstance.setInvoiceName(changedJournalInvoiceName.concat(",1"));
				}
				String originalJournalDescription = originalJournalCommand.getDescription();
				String changedJournalDescription = journalCommand.getDescription();
				if (originalJournalDescription == null || changedJournalDescription == null) {
					vbJournalInstance.setDescription(",0");
				} else {
					if (originalJournalDescription.equals(changedJournalDescription)) {
						vbJournalInstance.setDescription(originalJournalDescription.concat(",0"));
					} else {
						vbJournalInstance.setDescription(changedJournalDescription.concat(",1"));
					}
				}
				vbJournalInstance.setBusinessName(businessName);
				vbJournalInstance.setCrDescription(journalCommand.getCrDescription());
				vbJournalInstance.setCreatedBy(userName);
				vbJournalInstance.setCreatedOn(date);
				vbJournalInstance.setModifiedOn(date);
				vbJournalInstance.setInvoiceNo(invoiceNo);
				vbJournalInstance.setStatus(CRStatus.PENDING.name());
				vbJournalInstance.setJournalType(journalCommand.getJournalType());
				vbJournalInstance.setVbOrganization(organization);
				vbJournalInstance.setVbSalesBook(getVbSalesBookNoFlag(session, userName, organization));

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbJournalChangeRequest");
				}
				session.save(vbJournalInstance);
			}
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, ENotificationTypes.JOURNAL_TXN_CR.name(),
					CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible for getting all pending journals CR from
	 * Change Request table.
	 * 
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbJournalChangeRequestList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournalChangeRequest> getJournalCrResults(String username, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbJournalChangeRequest> journalChangeRequestList = session.createCriteria(VbJournalChangeRequest.class)
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn"))
				.list();
		session.close();

		if(!journalChangeRequestList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", journalChangeRequestList.size());
			}
			return journalChangeRequestList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to get {@link VbJournalChangeRequest} based on
	 * journal id and userName.
	 * 
	 * @param journalCRId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbJournalChangeRequest - {@link VbJournalChangeRequest}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbJournalChangeRequest getJournalCRById(Integer journalCRId,	VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbJournalChangeRequest vbJournalChangeRequest = (VbJournalChangeRequest) session.get(VbJournalChangeRequest.class, journalCRId);
		session.close();
		if(vbJournalChangeRequest != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("VbJournalChangeRequest: {}", vbJournalChangeRequest);
			}
			return vbJournalChangeRequest;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to approve the raised
	 * {@link VbJournalChangeRequest} requests means updating
	 * {@link VbCustomerCreditInfo} if the business name have any credit or
	 * saving the amount in {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param id - {@link Integer}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isApproved - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Boolean approveOrDeclineJournalCR(Integer id, String status, VbOrganization organization, String userName) throws DataAccessException {
		Boolean isApproved = Boolean.FALSE;
		Date date = new Date();
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbJournal vbJournal = null;
		VbJournalChangeRequest journalChangeRequest = (VbJournalChangeRequest) session.get(VbJournalChangeRequest.class, id);
		if (journalChangeRequest != null) {
			try {
				journalChangeRequest.setModifiedBy(userName);
				journalChangeRequest.setModifiedOn(date);
				if (CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
					String businessName = journalChangeRequest.getBusinessName();
					String invoiceNo = journalChangeRequest.getInvoiceNo();
					vbJournal = (VbJournal) session.createCriteria(VbJournal.class)
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("invoiceNo", invoiceNo))
							.add(Restrictions.eq("createdBy",	journalChangeRequest.getCreatedBy()))
							.add(Restrictions.eq("flag", new Integer(1)))
							.add(Restrictions.eq("status", CRStatus.APPROVED.name()))
							.add(Restrictions.eq("vbOrganization", organization))
							.uniqueResult();
					// if Approval Sales Return is Approved
					if (vbJournal != null) {
						// check Journal amount txn with debit and credit Txn tables
						// revert back all amount changes from credit info,advance
						// info use of debit txn and credit txn
						VbCustomerCreditInfo creditInfoRecord = null;
						VbCustomerDebitTransaction customerDebitTxn = (VbCustomerDebitTransaction) session.createCriteria(VbCustomerDebitTransaction.class)
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("debitTo", invoiceNo))
								.add(Restrictions.eq("flag", new Integer(1)))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (customerDebitTxn != null) {
							Float debitedAmount = customerDebitTxn.getAmount();
							String creditFrom = customerDebitTxn.getCreditFrom();
							creditInfoRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
									.add(Restrictions.eq("businessName",	businessName))
									.add(Restrictions.eq("invoiceNo", creditFrom))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();
							if (creditInfoRecord != null) {
								creditInfoRecord.setDue(debitedAmount+ creditInfoRecord.getDue());
								session.update(creditInfoRecord);

								if (_logger.isDebugEnabled()) {
									_logger.debug("Updating VbCustomerCreditInfo.");
								}
							}
							customerDebitTxn.setModifiedBy(userName);
							customerDebitTxn.setModifiedOn(new Date());
							customerDebitTxn.setFlag(new Integer(0));

							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerDebitTransaction.");
							}
							session.update(customerDebitTxn);
						}
						// Based on Approval Invoice Number Check Customer Credit
						// Txn and update Customer Advance Info table
						VbCustomerCreditTransaction customerCreditTxn = (VbCustomerCreditTransaction) session.createCriteria(VbCustomerCreditTransaction.class)
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("creditFrom", invoiceNo))
								.add(Restrictions.eq("flag", new Integer(1)))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (customerCreditTxn != null) {
							float creditedAmount = customerCreditTxn.getAmount();
							String debitTo = customerCreditTxn.getDebitTo();
							VbCustomerAdvanceInfo advanceInfoRecord = (VbCustomerAdvanceInfo) session.createCriteria(VbCustomerAdvanceInfo.class)
									.add(Restrictions.eq("businessName",	businessName))
									.add(Restrictions.eq("invoiceNo", debitTo))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();

							if (advanceInfoRecord != null) {
								if ((advanceInfoRecord.getBalance() - creditedAmount) >= 0) {
									advanceInfoRecord.setBalance(advanceInfoRecord.getBalance()	- creditedAmount);
									advanceInfoRecord.setAdvance(advanceInfoRecord.getAdvance()	- creditedAmount);
								} else {
									Float amount = -(advanceInfoRecord.getBalance() - creditedAmount);
									VbCustomerCreditTransaction creditTrx = new VbCustomerCreditTransaction();
									advanceInfoRecord.setBalance(new Float(0.00));
									advanceInfoRecord.setAdvance(advanceInfoRecord.getAdvance()	- creditedAmount);

									VbCustomerCreditInfo creditInfoData = new VbCustomerCreditInfo();
									String customerCreditInvoiceNo = generateCustomerCreditInfoInvoiceNumber(organization);
									creditInfoData.setDue(amount);
									creditInfoData.setBalance(amount);
									creditInfoData.setBusinessName(businessName);
									creditInfoData.setInvoiceNo(customerCreditInvoiceNo);
									creditInfoData.setCreatedBy(userName);
									creditInfoData.setCreatedOn(date);
									creditInfoData.setCreditFrom(invoiceNo);
									creditInfoData.setModifiedOn(date);
									creditInfoData.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("persisting VbCustomerCreditInfo.");
									}
									session.save(creditInfoData);

									// persisting VbCustomerCreditTxn
									creditTrx.setCreditFrom(invoiceNo);
									creditTrx.setBusinessName(businessName);
									creditTrx.setDebitTo(customerCreditInvoiceNo);
									creditTrx.setAmount(amount);
									creditTrx.setCreatedBy(userName);
									creditTrx.setCreatedOn(date);
									creditTrx.setModifiedOn(date);
									creditTrx.setFlag(new Integer(1));
									creditTrx.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("Persisting VbCustomerCreditTransaction.");
									}
									session.save(creditTrx);
								}

								if (_logger.isDebugEnabled()) {
									_logger.debug("Updating VbCustomerAdvanceInfo.");
								}
								session.update(advanceInfoRecord);
							}

							// check same Customer Credit debitTo in Customer
							// Credit Info
							creditInfoRecord = (VbCustomerCreditInfo) session.createCriteria(VbCustomerCreditInfo.class)
									.add(Restrictions.eq("businessName",	businessName))
									.add(Restrictions.eq("invoiceNo", debitTo))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();
							if (creditInfoRecord != null) {
								// condition for reverting -ve value to credit info due.
								if ((creditInfoRecord.getDue() - creditedAmount) >= 0) {
									creditInfoRecord.setDue(creditInfoRecord.getDue() - creditedAmount);
									creditInfoRecord.setBalance(creditInfoRecord.getBalance() - creditedAmount);
								} else {
									Float advance = -(creditInfoRecord.getDue() - creditedAmount);
									VbCustomerCreditTransaction creditTrx = new VbCustomerCreditTransaction();
									creditInfoRecord.setDue(new Float(0.00));
									creditInfoRecord.setBalance(creditInfoRecord.getBalance() - creditedAmount);

									VbCustomerAdvanceInfo advanceInfoData = new VbCustomerAdvanceInfo();
									advanceInfoData.setAdvance(advance);
									advanceInfoData.setBalance(advance);
									advanceInfoData.setBusinessName(businessName);
									String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(organization);
									advanceInfoData.setInvoiceNo(advanceInfoInvoiceNumber);
									advanceInfoData.setCreatedBy(userName);
									advanceInfoData.setCreatedOn(date);
									advanceInfoData.setCreditFrom(invoiceNo);
									advanceInfoData.setModifiedOn(date);
									advanceInfoData.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("persisting VbCustomerAdvanceInfo.");
									}
									session.save(advanceInfoData);

									// persisting VbCustomerCreditTxn
									creditTrx.setCreditFrom(invoiceNo);
									creditTrx.setBusinessName(businessName);
									creditTrx.setDebitTo(advanceInfoInvoiceNumber);
									creditTrx.setAmount(advance);
									creditTrx.setCreatedBy(userName);
									creditTrx.setCreatedOn(date);
									creditTrx.setModifiedOn(date);
									creditTrx.setFlag(new Integer(1));
									creditTrx.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("Persisting VbCustomerCreditTransaction.");
									}
									session.save(creditTrx);
								}

								if (_logger.isDebugEnabled()) {
									_logger.debug("Updating VbCustomerCreditInfo.");
								}
								session.update(creditInfoRecord);
							}
							customerCreditTxn.setModifiedBy(userName);
							customerCreditTxn.setModifiedOn(new Date());
							customerCreditTxn.setFlag(new Integer(0));
							session.update(customerCreditTxn);

							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerCreditTransaction.");
							}
						}
						// update journal main tables set flag=0 and status as
						// Approved if CR is Approved
						vbJournal.setFlag(new Integer(0));
						vbJournal.setModifiedBy(userName);
						vbJournal.setStatus(CRStatus.APPROVED.name());

						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbJournal.");
						}
						session.update(vbJournal);

						// persist new Journal entry with Journal CR data
						VbJournal vbJournalinstance = new VbJournal();
						String amount = journalChangeRequest.getAmount();
						if (amount.contains(",1")) {
							vbJournalinstance.setAmount(Float.parseFloat(amount.replace(",1", "")));
						} else {
							vbJournalinstance.setAmount(Float.parseFloat(amount.replace(",0", "")));
						}

						String invoiceName = journalChangeRequest.getInvoiceName();
						if (invoiceName.contains(",1")) {
							vbJournalinstance.setInvoiceName(invoiceName.replace(",1", ""));
						} else {
							vbJournalinstance.setInvoiceName(invoiceName.replace(",0", ""));
						}

						String description = journalChangeRequest.getDescription();
						if (description.contains(",1")) {
							vbJournalinstance.setDescription(description.replace(",1", ""));
						} else {
							vbJournalinstance.setDescription(description.replace(",0", ""));
						}

						vbJournalinstance.setBusinessName(businessName);
						vbJournalinstance.setCreatedBy(journalChangeRequest.getCreatedBy());
						vbJournalinstance.setModifiedBy(userName);
						vbJournalinstance.setCreatedOn(date);
						vbJournalinstance.setModifiedOn(date);
						vbJournalinstance.setFlag(new Integer(1));
						vbJournalinstance.setInvoiceNo(invoiceNo);
						vbJournalinstance.setStatus(CRStatus.APPROVED.name());
						vbJournalinstance.setJournalType(journalChangeRequest.getJournalType());
						vbJournalinstance.setVbOrganization(organization);
						vbJournalinstance.setVbSalesBook(journalChangeRequest.getVbSalesBook());

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbJournal");
						}
						session.save(vbJournalinstance);
						// end Journal persistance

						// update credit info and advance info if amount is
						// changed in journal CR
						// update customer credits as if dues are there else
						// save as advance
						Float journalAmount = vbJournalinstance.getAmount();
						String journalBusinessName = vbJournal.getBusinessName();
						if (journalAmount > 0) {
							Query customerInfoQuery = null;
							while (journalAmount != 0) {
								customerInfoQuery = session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
														+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
														+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization "
														+ "AND vbc.due > :due)")
										.setParameter("businessName", journalBusinessName)
										.setParameter("vbOrganization",	organization)
										.setParameter("due", new Float("0"));
								VbCustomerCreditInfo creditInfo = getSingleResultOrNull(customerInfoQuery);

								if (creditInfo != null) {
									// persisting Customer debit Txn
									VbCustomerDebitTransaction approvedDebitTxn = new VbCustomerDebitTransaction();
									creditInfo.setModifiedBy(userName);
									creditInfo.setModifiedOn(new Date());
									String existingInvoiceNo = creditInfo.getDebitTo();
									String newInvoiceNo = vbJournal.getInvoiceNo();
									if (existingInvoiceNo != null) {
										creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(newInvoiceNo));
									} else {
										creditInfo.setDebitTo(newInvoiceNo);
									}
									Float existingDue = creditInfo.getDue();
									if (journalAmount < existingDue) {
										creditInfo.setDue(existingDue - journalAmount);
										approvedDebitTxn.setAmount(journalAmount);
										journalAmount = new Float(0.0);
									} else {
										journalAmount = journalAmount - existingDue;
										creditInfo.setDue(new Float("0.00"));
										approvedDebitTxn.setAmount(existingDue);
									}
									// persisting Customer Debit Txn for Payback his credit amount.
									approvedDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
									approvedDebitTxn.setBusinessName(vbJournal.getBusinessName());
									approvedDebitTxn.setDebitTo(vbJournal.getInvoiceNo());
									approvedDebitTxn.setCreatedBy(userName);
									approvedDebitTxn.setCreatedOn(new Date());
									approvedDebitTxn.setModifiedOn(new Date());
									approvedDebitTxn.setFlag(new Integer(1));
									approvedDebitTxn.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("Updating VbCustomerCreditInfo");
									}
									session.update(creditInfo);

									if (_logger.isDebugEnabled()) {
										_logger.debug("Updating VbCustomerDebitTransaction.");
									}
									session.save(approvedDebitTxn);
									// updating credit amount of Customer Credit Txn
								} else {
									// saveCustomerAdvanceInfo(session, businessName, deliveryNote.getInvoiceNo(), organization, presentPayment, userName);
									VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
									VbCustomerCreditTransaction approvedCreditTxn = new VbCustomerCreditTransaction();
									if (journalAmount < 0) {
										Float newBalance = -(journalAmount);
										advanceInfo.setAdvance(newBalance);
										advanceInfo.setBalance(newBalance);
										approvedCreditTxn.setAmount(newBalance);
									} else {
										advanceInfo.setAdvance(journalAmount);
										advanceInfo.setBalance(journalAmount);
										approvedCreditTxn.setAmount(journalAmount);
									}
									advanceInfo.setBusinessName(businessName);
									String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(organization);
									advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
									advanceInfo.setCreatedBy(userName);
									advanceInfo.setCreatedOn(date);
									advanceInfo.setModifiedOn(date);
									advanceInfo.setCreditFrom(vbJournal.getInvoiceNo());
									advanceInfo.setVbOrganization(organization);

									// persisting Customer Credit txn case of extra amount
									approvedCreditTxn.setBusinessName(businessName);
									approvedCreditTxn.setCreditFrom(vbJournal.getInvoiceNo());
									approvedCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
									approvedCreditTxn.setCreatedBy(userName);
									approvedCreditTxn.setCreatedOn(date);
									approvedCreditTxn.setModifiedOn(date);
									approvedCreditTxn.setFlag(new Integer(1));
									approvedCreditTxn.setVbOrganization(organization);

									if (_logger.isDebugEnabled()) {
										_logger.debug("Persisting VbCustomerAdvanceInfo");
									}
									session.save(advanceInfo);
									session.save(approvedCreditTxn);
									// }
									journalAmount = new Float("0");
								}
							}
						}
						// if Approval Journal is still pending
					} else {
						// update Journal main tables with Journal CR tables
						// get VbSalesReturn based on Sales Return Change Request Invoice Number
						vbJournal = (VbJournal) session.createCriteria(VbJournal.class)
								.add(Restrictions.eq("vbOrganization", organization))
								.add(Restrictions.eq("createdBy",	journalChangeRequest.getCreatedBy()))
								.add(Restrictions.eq("status", CRStatus.PENDING.name()))
								.add(Restrictions.eq("invoiceNo",	journalChangeRequest.getInvoiceNo()))
								.uniqueResult();

						if (vbJournal != null) {
							// update journal main tables set flag=0 and status as
							// Approved if CR is Approved
							vbJournal.setFlag(new Integer(0));
							vbJournal.setStatus(CRStatus.APPROVED.name());
							vbJournal.setModifiedBy(userName);
							session.update(vbJournal);
							// persist new Journal entry with Journal CR data
							VbJournal vbJournalInstance = new VbJournal();
							String amount = journalChangeRequest.getAmount();
							if (amount.contains(",1")) {
								vbJournalInstance.setAmount(Float.parseFloat(amount.replace(",1", "")));
							} else {
								vbJournalInstance.setAmount(Float.parseFloat(amount.replace(",0", "")));
							}
							vbJournalInstance.setBusinessName(businessName);
							String invoiceNumber = journalChangeRequest.getInvoiceName();
							if (invoiceNumber.contains(",1")) {
								vbJournalInstance.setInvoiceName(invoiceNumber.replace(",1", ""));
							} else {
								vbJournalInstance.setInvoiceName(invoiceNumber.replace(",0", ""));
							}
							String description = journalChangeRequest.getDescription();
							if (description.contains(",1")) {
								vbJournalInstance.setDescription(description.replace(",1", ""));
							} else {
								vbJournalInstance.setDescription(description.replace(",0", ""));
							}
							vbJournalInstance.setCreatedBy(journalChangeRequest.getCreatedBy());
							vbJournalInstance.setModifiedBy(userName);
							vbJournalInstance.setCreatedOn(date);
							vbJournalInstance.setModifiedOn(date);
							vbJournalInstance.setFlag(new Integer(1));
							vbJournalInstance.setInvoiceNo(journalChangeRequest.getInvoiceNo());
							vbJournalInstance.setStatus(CRStatus.APPROVED.name());
							vbJournalInstance.setJournalType(journalChangeRequest.getJournalType());
							vbJournalInstance.setVbOrganization(organization);
							vbJournalInstance.setVbSalesBook(journalChangeRequest.getVbSalesBook());

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbJournal");
							}
							session.save(vbJournalInstance);
							// end Journal persistance
							// update credit info and advance info if amount is
							// changed in journal CR
							// update customer credits as if dues are there else
							// save as advance
							Float journalAmount = vbJournalInstance.getAmount();
							String journalBusinessName = vbJournal.getBusinessName();
							if (journalAmount > 0) {
								Query customerInfoQuery = null;
								while (journalAmount != 0) {
									customerInfoQuery = session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
															+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
															+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
											.setParameter("businessName", journalBusinessName)
											.setParameter("vbOrganization", organization)
											.setParameter("due", new Float("0"));

									VbCustomerCreditInfo creditInfo = getSingleResultOrNull(customerInfoQuery);
									if (creditInfo != null) {
										// persisting Customer debit Txn
										VbCustomerDebitTransaction approvedDebitTxn = new VbCustomerDebitTransaction();
										creditInfo.setModifiedBy(userName);
										creditInfo.setModifiedOn(new Date());
										String existingInvoiceNo = creditInfo.getDebitTo();
										String newInvoiceNo = vbJournal.getInvoiceNo();
										if (existingInvoiceNo != null) {
											creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(newInvoiceNo));
										} else {
											creditInfo.setDebitTo(newInvoiceNo);
										}
										Float existingDue = creditInfo.getDue();
										if (journalAmount < existingDue) {
											creditInfo.setDue(existingDue - journalAmount);
											approvedDebitTxn.setAmount(journalAmount);
											journalAmount = new Float(0.0);
										} else {
											journalAmount = journalAmount - existingDue;
											creditInfo.setDue(new Float("0.00"));
											approvedDebitTxn.setAmount(existingDue);
										}
										// persisting Customer Debit Txn for
										// Payback
										// his credit amount.
										approvedDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
										approvedDebitTxn.setBusinessName(vbJournal.getBusinessName());
										approvedDebitTxn.setDebitTo(vbJournal.getInvoiceNo());
										approvedDebitTxn.setCreatedBy(userName);
										approvedDebitTxn.setCreatedOn(new Date());
										approvedDebitTxn.setModifiedOn(new Date());
										approvedDebitTxn.setFlag(new Integer(1));
										approvedDebitTxn.setVbOrganization(organization);

										if (_logger.isDebugEnabled()) {
											_logger.debug("Updating VbCustomerDebitTransaction.");
										}
										session.save(approvedDebitTxn);

										if (_logger.isDebugEnabled()) {
											_logger.debug("Updating VbCustomerCreditInfo.");
										}
										session.update(creditInfo);
										// updating credit amount of Customer Credit Txn
									} else {
										// saveCustomerAdvanceInfo(session, businessName, deliveryNote.getInvoiceNo(), organization, presentPayment, userName);
										VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
										VbCustomerCreditTransaction approvedCreditTxn = new VbCustomerCreditTransaction();
										if (journalAmount < 0) {
											Float newBalance = -(journalAmount);
											advanceInfo.setAdvance(newBalance);
											advanceInfo.setBalance(newBalance);
											approvedCreditTxn.setAmount(newBalance);
										} else {
											advanceInfo.setAdvance(journalAmount);
											advanceInfo.setBalance(journalAmount);
											approvedCreditTxn.setAmount(journalAmount);
										}
										advanceInfo.setBusinessName(businessName);
										String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(organization);
										advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
										advanceInfo.setCreatedBy(userName);
										advanceInfo.setCreatedOn(date);
										advanceInfo.setModifiedOn(date);
										advanceInfo.setCreditFrom(vbJournal.getInvoiceNo());
										advanceInfo.setVbOrganization(organization);

										// persisting Customer Credit txn case of extra amount
										approvedCreditTxn.setBusinessName(businessName);
										approvedCreditTxn.setCreditFrom(vbJournal.getInvoiceNo());
										approvedCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
										approvedCreditTxn.setCreatedBy(userName);
										approvedCreditTxn.setCreatedOn(date);
										approvedCreditTxn.setModifiedOn(date);
										approvedCreditTxn.setFlag(new Integer(1));
										approvedCreditTxn.setVbOrganization(organization);

										if (_logger.isDebugEnabled()) {
											_logger.debug("Persisting VbCustomerAdvanceInfo.");
										}
										session.save(advanceInfo);
										session.save(approvedCreditTxn);
										// }
										journalAmount = new Float("0");
									}
								}
							}

						}
					}
					journalChangeRequest.setStatus(CRStatus.APPROVED.name());
					isApproved = Boolean.TRUE;
				} else {
					journalChangeRequest.setStatus(CRStatus.DECLINE.name());
					isApproved = Boolean.FALSE;
				}
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbJournalChangeRequest: {}", journalChangeRequest);
				}
				session.update(journalChangeRequest);

				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, journalChangeRequest.getVbSalesBook().getSalesExecutive(), organization,
						ENotificationTypes.JOURNAL_TXN_CR.name(), status,
						journalChangeRequest.getInvoiceNo(), journalChangeRequest.getBusinessName());
				txn.commit();
				return isApproved;
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
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName -{@link String}
	 * @param invoiceNumber -{@link String}
	 * @param journalId -{@link Integer}
	 * @return deliveryNoteId -{@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Integer getJournalBasedOnInvoiceNo(VbOrganization organization, String invoiceNumber, Integer journalId, String userName) throws DataAccessException {
		Session session = this.getSession();
		Integer maxId = new Integer(0);
		Query query = session.createQuery("SELECT MAX(vbj.id) FROM VbJournal vbj where vbj.invoiceNo LIKE :invoiceNo " +
						"AND vbj.flag= :flag AND vbj.id <= :journalId AND vbj.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(1))
				.setParameter("journalId", journalId)
				.setParameter("vbOrganization", organization);
		Integer journalMaxId = getSingleResultOrNull(query);
		session.close();
		
		if (journalMaxId != null) {
			maxId = journalMaxId;
		} 
		return maxId;
	}

	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * {@link VbOrganization} and invoiceNo and journalId.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param journalId -{@link Integer}
	 * @param invoiceNumber -{@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * @throws ParseException - {@link ParseException}
	 * 
	 */
	public String fetchJournalCreationTime(Integer journalId, String invoiceNumber, VbOrganization organization) throws ParseException {
		Session session = this.getSession();
		String isJournalCRExpired = "n";
		Date currentDateTime = new Date();
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime = format.parse(d2);
		VbJournal vbJournal = (VbJournal) session.createCriteria(VbJournal.class)
				.add(Restrictions.eq("id", journalId))
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if (vbJournal != null) {
			Date journalCreationTime = vbJournal.getCreatedOn();
			String creationTime = DateUtils.formatDateWithTimestamp(journalCreationTime);
			Date journalCreation = format.parse(creationTime);
			Integer totalMin = calculateMinDiff(currentTime, journalCreation);
			if (totalMin > 10) {
				isJournalCRExpired = "y";
			} else {
				isJournalCRExpired = "n";
			}
		}
		
		return isJournalCRExpired;
	}

	/**
	 * This Method is Responsible to Check whether The Transaction Journal for
	 * given Invoice Number is Available or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return salesReturnCRId - {@link Integer}
	 */
	public Integer checkTransactionJournal(String invoiceNumber, VbOrganization organization) {
		Integer journalCRId;
		Session session = this.getSession();
		VbJournalChangeRequest vbJournalChangeRequest = (VbJournalChangeRequest) session.createCriteria(VbJournalChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.uniqueResult();
		session.close();
		if (vbJournalChangeRequest != null) {
			journalCRId = vbJournalChangeRequest.getId();
		} else {
			journalCRId = new Integer(0);
		}
		
		return journalCRId;
	}

	/**
	 * This method is responsible for fetching all Txn for Journal based on
	 * Journal Type and respective invoice pattern
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults - {@link MySalesInvoicesHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesJournalHistoryResult> getconfiguredJournalTypesCRWithInvoicePattern(VbOrganization organization, String userName)
			throws DataAccessException {
		// approved,decline,pending status count of journal
		Session session = this.getSession();
		List<VbJournalTypes> journalTypes = (List<VbJournalTypes>) session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdBy", userName))
				.list();
		if (!journalTypes.isEmpty()) {
			String journalType = null;
			Integer totalCount = null;
			List<Object[]> resultList = null;
			List<MySalesJournalHistoryResult> historyResults = new ArrayList<MySalesJournalHistoryResult>();
			for (VbJournalTypes journalsType : journalTypes) {
				resultList = session.createCriteria(VbJournalChangeRequest.class)
						.setProjection(Projections.projectionList()
										.add(Projections.property("journalType"))
										.add(Projections.rowCount())
										.add(Projections.groupProperty("journalType")))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();

				if (!resultList.isEmpty()) {
					MySalesJournalHistoryResult historyResult = null;
					for (Object[] objects : resultList) {
						historyResult = new MySalesJournalHistoryResult();
						journalType = (String) objects[0];
						totalCount = (Integer) objects[1];
						if (journalsType.getJournalType().equalsIgnoreCase(journalType)) {
							historyResult.setJournalTransactionType(journalType);
							historyResult.setJournalInvoicePattern(journalsType.getInvoiceNo());
							historyResult.setTotalJournalTypeCount(totalCount);
							historyResults.add(historyResult);
						}
					}
				} else {
					// for those journal type which are configured but not made any txn
					MySalesJournalHistoryResult historyResult = new MySalesJournalHistoryResult();
					historyResult.setJournalTransactionType(journalsType.getJournalType());
					historyResult.setJournalInvoicePattern(journalsType.getInvoiceNo());
					historyResult.setTotalJournalTypeCount(new Integer(0));
					historyResults.add(historyResult);
				}
			}
			session.close();
			return historyResults;
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
	 * This method is responsible for getting Approved,Declined,Pending count of
	 * Journal.
	 * 
	 * @param journalTxnType -{@link String}
	 * @param invoicePattern -{@link String}
	 * @param organization -{@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults -{@link MySalesHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public List<MySalesHistoryResult> getJournalTransactionHistory(String journalTxnType, String invoicePattern, VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		// approved,decline,pending status count of journal
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = session.createCriteria(VbJournalChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.rowCount())
								.add(Projections.groupProperty("status")))
				.add(Restrictions.like("invoiceNo", invoicePattern, MatchMode.ANYWHERE))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		if (!resultList.isEmpty()) {
			MySalesHistoryResult historyResult = null;
			List<MySalesHistoryResult> historyResults = new ArrayList<MySalesHistoryResult>();
			for (Object[] objects : resultList) {
				historyResult = new MySalesHistoryResult();
				String journalStatus = (String) objects[1];
				Integer count = (Integer) objects[2];
				if (CRStatus.APPROVED.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setApprovedDNCount(count);
				} else if (CRStatus.DECLINE.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setDeclinedDNCount(count);
				} else if (CRStatus.PENDING.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setPendingDNCount(count);
				}
				historyResults.add(historyResult);
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResults.size());
			}
			return historyResults;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible for getting sales return to fetch status based
	 * on sales return id.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param journalId - {@link Integer}
	 * @param userName - {@link String}
	 * @return vbJournal - {@link VbJournal}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbJournal getJournalStatus(VbOrganization organization, Integer journalId, String userName) throws DataAccessException {
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal) session.get(VbJournal.class, journalId);
		session.close();
		
		if (vbJournal != null) {
			return vbJournal;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to fetch invoices from
	 * {@link VbJournalChangeRequest} based on status and invoiceNumber
	 * 
	 * @param invoiceNumber - {@link String}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return invoiceHistoryResults - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesInvoicesHistoryResult> getJournalInvoicesTransactionHistory(String invoiceNumber, String status, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		List<Object[]> resultList = session.createCriteria(VbJournalChangeRequest.class)
				.setProjection(Projections.projectionList()
								.add(Projections.property("id"))
								.add(Projections.property("invoiceNo"))
								.add(Projections.property("status"))
								.add(Projections.property("createdBy"))
								.add(Projections.property("createdOn"))
								.add(Projections.property("modifiedBy"))
								.add(Projections.property("modifiedOn")))
				.add(Restrictions.like("invoiceNo", invoiceNumber, MatchMode.ANYWHERE))
				.add(Restrictions.eq("status", status))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();

		if (!resultList.isEmpty()) {
			Integer journalId = null;
			String journalInvNo = null;
			String JOURNALstatus = null;
			String createdBy = null;
			Date createdOn = null;
			String modifiedBy = null;
			Date modifiedOn = null;
			MySalesInvoicesHistoryResult invoiceHistoryResult = null;
			List<MySalesInvoicesHistoryResult> invoiceHistoryResults = new ArrayList<MySalesInvoicesHistoryResult>();
			for (Object[] objects : resultList) {
				journalId = (Integer) objects[0];
				journalInvNo = (String) objects[1];
				JOURNALstatus = (String) objects[2];
				createdBy = (String) objects[3];
				createdOn = (Date) objects[4];
				modifiedBy = (String) objects[5];
				modifiedOn = (Date) objects[6];

				invoiceHistoryResult = new MySalesInvoicesHistoryResult();
				invoiceHistoryResult.setInvoiceNumber(journalInvNo);
				invoiceHistoryResult.setRequestedBy(createdBy);
				invoiceHistoryResult.setRequestedDate(createdOn);
				invoiceHistoryResult.setModifiedBy(modifiedBy);
				invoiceHistoryResult.setModifiedDate(modifiedOn);
				invoiceHistoryResult.setStatus(JOURNALstatus);
				invoiceHistoryResult.setId(journalId);

				invoiceHistoryResults.add(invoiceHistoryResult);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", invoiceHistoryResults.size());
			}
			return invoiceHistoryResults;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}

	// get original sales return based on sales return CR invoice number and
	// organization id and if status is Approved.
	/**
	 * This method is responsible to fetch journal from {@link VbJournal} based
	 * on status and invoiceNumber
	 * 
	 * @param journalInvoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbJournal - {@link VbJournal}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public VbJournal getOriginalJournal(String journalInvoiceNumber, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal) session.createCriteria(VbJournal.class)
				.add(Restrictions.eq("invoiceNo", journalInvoiceNumber))
				.add(Restrictions.eq("flag", new Integer(1)))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if (vbJournal != null) {
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbJournal: {}", vbJournal);
			}
			return vbJournal;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if (_logger.isWarnEnabled()) {
				_logger.warn(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		}
	}
}
