/**
 * com.vekomy.vbooks.mysales.dao.DeliveryNoteDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 17, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCustomerResult;
import com.vekomy.vbooks.mysales.command.DeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteResult;
import com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult;
import com.vekomy.vbooks.mysales.command.ProductResult;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform operations on deliver note in sales
 * module.
 * 
 * @author Sudhakar
 * 
 */
public class DeliveryNoteDao extends MySalesBaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteDao.class);
	/**
	 * String variable holds DAILY_SALES_EXECUTIVE.
	 */
	private static final String DAILY_SALES_EXECUTIVE = "Daily";

	/**
	 * This method is responsible to persist the {@link VbDeliveryNote},
	 * {@link VbDeliveryNotePayments} and {@link VbDeliveryNoteProducts}.
	 * 
	 * @param products - {@link DeliveryNoteProductCommand}
	 * @param deliveryNoteList - {@link DeliveryNoteCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveDeliveryNote(DeliveryNoteProductCommand products,
			List<DeliveryNoteCommand> deliveryNoteList, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			VbDeliveryNote instanceNote = new VbDeliveryNote();
			VbDeliveryNoteProducts instanceProducts = null;
			if (instanceNote != null) {
				instanceNote.setCreatedOn(date);
				instanceNote.setCreatedBy(userName);
				instanceNote.setModifiedOn(date);
				instanceNote.setVbOrganization(organization);
				instanceNote.setFlag(new Integer(1));
				VbSalesBook salesBook = getSalesBook(session, organization, userName);
				if (salesBook != null) {
					instanceNote.setVbSalesBook(salesBook);
				}
				for (DeliveryNoteCommand deliveryNoteCommand : deliveryNoteList) {
					String businessName = deliveryNoteCommand.getBusinessName();
					instanceNote.setBusinessName(businessName);
					instanceNote.setInvoiceName(deliveryNoteCommand.getInvoiceName());
					instanceNote.setInvoiceNo(deliveryNoteCommand.getInvoiceNo());

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbDeliveryNote: {}", instanceNote);
					}
					session.save(instanceNote);

					instanceProducts = new VbDeliveryNoteProducts();
					Float productCost = deliveryNoteCommand.getProductCost();
					instanceProducts.setProductCost(productCost);
					String productName = deliveryNoteCommand.getProductName();
					instanceProducts.setProductName(productName);
					instanceProducts.setBatchNumber(deliveryNoteCommand.getBatchNumer());
					Integer productQty = deliveryNoteCommand.getProductQuantity();
					instanceProducts.setProductQty(productQty);
					Float totalCost = productCost * productQty;
					instanceProducts.setTotalCost(totalCost);
					instanceProducts.setVbDeliveryNote(instanceNote);
					Integer bonusQty = deliveryNoteCommand.getBonusQuantity();
					instanceProducts.setBonusQty(bonusQty);
					instanceProducts.setBonusReason(deliveryNoteCommand.getBonusReason());

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbDeliveryNoteProducts: {}", instanceProducts);
					}
					session.save(instanceProducts);
				}


			}

			VbDeliveryNotePayments instancePayments = new VbDeliveryNotePayments();
			if (instanceNote != null) {
				instancePayments.setPresentPayable(products.getPresentPayable());
				instancePayments.setPresentPayment(products.getPresentPayment());
				instancePayments.setPaymentType(products.getPaymentType());
				instancePayments.setPresentAdvance(products.getPresentAdvance());
				instancePayments.setPreviousCredit(products.getPreviousCredit());
				instancePayments.setBalance(products.getBalance());
				instancePayments.setTotalPayable(products.getTotalPayable());
				instancePayments.setBankName(products.getBankName());
				instancePayments.setChequeNo(products.getChequeNo());
				instancePayments.setBranchName(products.getBranchName());
				instancePayments.setBankLocation(products.getBankLocation());
				instancePayments.setVbDeliveryNote(instanceNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNotePayments: {}", instancePayments);
				}
				session.save(instancePayments);
			}
			// For Updating previous credits and advance.
			updateCreditsAndAdvance(session, userName, organization, instanceNote, products);
			txn.commit();
			
			if(_logger.isInfoEnabled()) {
				_logger.info("DeliveryNote: {}", Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
			}
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
	 * This method is responsible to update previous credit and advance.
	 * 
	 * @param session - {@link Session}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param instanceNote - {@link VbDeliveryNote}
	 * @param products - {@link DeliveryNoteProductCommand}
	 */
	private void updateCreditsAndAdvance(Session session, String userName,
			VbOrganization organization, VbDeliveryNote instanceNote,
			DeliveryNoteProductCommand products) {
		Date date = new Date();
		VbCustomerCreditInfo customerCreditInfo = new VbCustomerCreditInfo();
		String businessName = instanceNote.getBusinessName();
		String invoiceName = instanceNote.getInvoiceNo();
		customerCreditInfo.setBusinessName(businessName);
		String customerCreditInvoiceNo = generateCustomerCreditInfoInvoiceNumber(organization);
		customerCreditInfo.setInvoiceNo(customerCreditInvoiceNo);
		customerCreditInfo.setCreditFrom(invoiceName);
		customerCreditInfo.setCreatedBy(userName);
		customerCreditInfo.setCreatedOn(date);
		customerCreditInfo.setModifiedOn(date);
		Float balance = products.getPresentPayable() - products.getPresentPayment();
		Float advance = products.getPresentAdvance();

		// persist customer credit Txn basic information
		VbCustomerCreditTransaction customerCreditTxn = new VbCustomerCreditTransaction();
		customerCreditTxn.setCreditFrom(instanceNote.getInvoiceNo());
		customerCreditTxn.setBusinessName(businessName);
		customerCreditTxn.setDebitTo(customerCreditInvoiceNo);
		customerCreditTxn.setFlag(new Integer(1));
		customerCreditTxn.setCreatedBy(userName);
		customerCreditTxn.setCreatedOn(date);
		customerCreditTxn.setModifiedOn(date);
		if (advance <= balance) {
			Float remainingBalance = balance - advance;
			customerCreditInfo.setBalance(remainingBalance);
			customerCreditInfo.setDue(remainingBalance);

			// persist customer credits Txn amount
			customerCreditTxn.setAmount(remainingBalance);
			customerCreditInfo.setVbOrganization(organization);
			customerCreditTxn.setVbOrganization(organization);
			customerCreditTxn.setFlag(new Integer(1));
			session.save(customerCreditInfo);
			session.save(customerCreditTxn);
			// For updating advanceInfo.
			while (advance != 0) {
				VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, organization);
				VbCustomerDebitTransaction customerDebitTxn = new VbCustomerDebitTransaction();
				if (advanceInfo != null) {
					Float advBalance = advanceInfo.getBalance();
					if (advance > advBalance) {
						advance = advance - advBalance;
						customerDebitTxn.setAmount(advBalance);
						advanceInfo.setBalance(new Float(0));
					} else {
						advanceInfo.setBalance(advBalance - advance);
						customerDebitTxn.setAmount(advBalance);
						advance = new Float(0);
					}
					advanceInfo.setBalance(new Float(0));
					advanceInfo.setModifiedBy(userName);
					advanceInfo.setModifiedOn(new Date());
					advanceInfo.setDebitTo(invoiceName);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating advanceInfo");
					}
					session.update(advanceInfo);
					
					customerDebitTxn.setCreditFrom(advanceInfo.getInvoiceNo());
					customerDebitTxn.setBusinessName(businessName);
					customerDebitTxn.setDebitTo(invoiceName);
					customerDebitTxn.setFlag(new Integer(1));
					customerDebitTxn.setCreatedBy(userName);
					customerDebitTxn.setCreatedOn(new Date());
					customerDebitTxn.setModifiedOn(new Date());
					customerDebitTxn.setVbOrganization(organization);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbCustomerDebitTransaction.");
					}
					session.save(customerDebitTxn);
				}
			}// while loop
		} else {
			Float remainingBalance = advance - balance;
			customerCreditInfo.setDue(new Float("0"));
			VbCustomerCreditInfo vbCustomerCreditInfo = null;
			while (remainingBalance != 0) {
				vbCustomerCreditInfo = getPreviousCreditByBusinessName(session, businessName, organization);
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
					vbCustomerCreditInfo.setDebitTo(invoiceName);
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo");
					}
					session.update(vbCustomerCreditInfo);
					
					// persist customer credit Txn basic information
					vbDebitTransaction.setCreditFrom(vbCustomerCreditInfo.getInvoiceNo());
					vbDebitTransaction.setDebitTo(invoiceName);
					vbDebitTransaction.setBusinessName(businessName);
					vbDebitTransaction.setFlag(new Integer(1));
					vbDebitTransaction.setCreatedBy(userName);
					vbDebitTransaction.setCreatedOn(date);
					vbDebitTransaction.setModifiedOn(date);
					vbDebitTransaction.setVbOrganization(organization);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbDebitTransaction");
					}
					session.save(vbDebitTransaction);
				} else {
					VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, organization);
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
						advanceInfo.setDebitTo(invoiceName);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomerAdvanceInfo");
						}
						session.update(advanceInfo);
						
						// persist customer credit Txn basic information
						vbDebitTransactionAdvance.setCreditFrom(customerCreditInvoiceNo);
						vbDebitTransactionAdvance.setBusinessName(businessName);
						vbDebitTransactionAdvance.setDebitTo(invoiceName);
						vbDebitTransactionAdvance.setFlag(new Integer(1));
						vbDebitTransactionAdvance.setCreatedBy(userName);
						vbDebitTransactionAdvance.setCreatedOn(date);
						vbDebitTransactionAdvance.setModifiedOn(date);
						vbDebitTransactionAdvance.setVbOrganization(organization);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting vbDebitTransaction");
						}
						session.save(vbDebitTransactionAdvance);
					} else {
						// If there is no advance for businessName creating new record.
						advanceInfo = new VbCustomerAdvanceInfo();
						advanceInfo.setAdvance(remainingBalance);
						advanceInfo.setBalance(remainingBalance);
						advanceInfo.setBusinessName(businessName);
						String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(organization);
						advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
						advanceInfo.setCreatedBy(userName);
						advanceInfo.setCreatedOn(date);
						advanceInfo.setCreditFrom(invoiceName);
						advanceInfo.setModifiedOn(date);
						advanceInfo.setVbOrganization(organization);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbCustomerAdvanceInfo");
						}
						session.save(advanceInfo);
						
						// persisting Customer credit Txn for new Advance Info.
						VbCustomerCreditTransaction creditTransaction = new VbCustomerCreditTransaction();
						creditTransaction.setCreditFrom(invoiceName);
						creditTransaction.setBusinessName(businessName);
						creditTransaction.setDebitTo(advanceInfoInvoiceNumber);
						creditTransaction.setAmount(remainingBalance);
						creditTransaction.setFlag(new Integer(1));
						creditTransaction.setCreatedBy(userName);
						creditTransaction.setCreatedOn(date);
						creditTransaction.setModifiedOn(date);
						creditTransaction.setVbOrganization(organization);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbCustomerCreditTransaction");
						}
						session.save(creditTransaction);
					}
					// Updating remaining balance.
					remainingBalance = new Float(0);
				}
			}
		}
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
	 * This method is responsible to persist {@link VbDeliveryNote}, update
	 * {@link VbCustomerAdvanceInfo} and {@link VbCustomerCreditInfo}.
	 * 
	 * @param command - {@link DeliveryNoteCommand}
	 * @param deliveryNoteProductCommand - {@link VbDeliveryNotePayments}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isSaved - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void savePayments(DeliveryNoteCommand command,
			DeliveryNoteProductCommand deliveryNoteProductCommand,
			VbOrganization organization, String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();

			VbDeliveryNote deliveryNote = new VbDeliveryNote();
			VbDeliveryNotePayments deliveryNotePayments = new VbDeliveryNotePayments();
			VbSalesBook salesBook = getSalesBook(session, organization, userName);
			String businessName = command.getBusinessName();
			if (deliveryNote != null) {
				deliveryNote.setBusinessName(businessName);
				deliveryNote.setInvoiceName(command.getInvoiceName());
				deliveryNote.setCreatedBy(userName);
				Date date = new Date();
				deliveryNote.setCreatedOn(date);
				deliveryNote.setModifiedOn(date);
				deliveryNote.setInvoiceNo(command.getInvoiceNo());
				deliveryNote.setFlag(new Integer(1));
				deliveryNote.setVbSalesBook(salesBook);
				deliveryNote.setVbOrganization(organization);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting deliveryNote: {}", deliveryNote);
				}
				session.save(deliveryNote);
			}

			Float presentPayment = deliveryNoteProductCommand.getPresentPayment();
			if (deliveryNotePayments != null) {
				deliveryNotePayments.setBalance(deliveryNoteProductCommand.getBalance());
				deliveryNotePayments.setBankName(deliveryNoteProductCommand.getBankName());
				deliveryNotePayments.setBranchName(deliveryNoteProductCommand.getBranchName());
				deliveryNotePayments.setChequeNo(deliveryNoteProductCommand.getChequeNo());
				deliveryNotePayments.setPaymentType(deliveryNoteProductCommand.getPaymentType());
				deliveryNotePayments.setBankLocation(deliveryNoteProductCommand.getBankLocation());
				deliveryNotePayments.setPresentAdvance(deliveryNoteProductCommand.getPresentAdvance());
				deliveryNotePayments.setPresentPayable(deliveryNoteProductCommand.getPresentPayable());
				deliveryNotePayments.setPresentPayment(presentPayment);
				deliveryNotePayments.setPreviousCredit(deliveryNoteProductCommand.getPreviousCredit());
				deliveryNotePayments.setTotalPayable(deliveryNoteProductCommand.getTotalPayable());
				deliveryNotePayments.setVbDeliveryNote(deliveryNote);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting deliveryNotePayments: {}", deliveryNotePayments);
				}
				session.save(deliveryNotePayments);
			}
			// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
			updateCustomerCreditInfo(session, command, deliveryNoteProductCommand, organization, userName);
			// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
			txn.commit();
			
			if(_logger.isInfoEnabled()) {
				_logger.info("DeliveryNote Payments: {}", Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
			}
		} catch(HibernateException exception) {
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
	 * This method is responsible to update {@link VbCustomerAdvanceInfo} and
	 * {@link VbCustomerCreditInfo}.
	 * 
	 * @param session - {@link Session}
	 * @param command - {@link DeliveryNoteCommand}
	 * @param productCommand - {@link DeliveryNoteProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unused")
	private void updateCustomerCreditInfo(Session session,
			DeliveryNoteCommand command,
			DeliveryNoteProductCommand productCommand,
			VbOrganization organization, String userName) throws DataAccessException {
		try {
			Float presentPayment = productCommand.getPresentPayment();
			VbCustomerDebitTransaction customerDebitTxn=null;
			String businessName = command.getBusinessName();
			if (presentPayment > 0) {
				Query customerInfoQuery = null;
				Query customerCreditTxnQuery=null;
				while (presentPayment != 0) {
					customerInfoQuery = session.createQuery(
									"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
									+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
									+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
							.setParameter("businessName", command.getBusinessName())
							.setParameter("vbOrganization", organization)
							.setParameter("due", new Float("0"));

					VbCustomerCreditInfo creditInfo = getSingleResultOrNull(customerInfoQuery);
					if (creditInfo != null) {
						//persisting Customer debit Txn
						customerDebitTxn=new VbCustomerDebitTransaction();
						creditInfo.setModifiedBy(userName);
						creditInfo.setModifiedOn(new Date());
						String existingInvoiceNo = creditInfo.getDebitTo();
						String newInvoiceNo = command.getInvoiceNo();
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
						//persisting Customer Debit Txn for Payback his credit amount.
						customerDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
						customerDebitTxn.setBusinessName(command.getBusinessName());
						customerDebitTxn.setDebitTo(command.getInvoiceNo());
						customerDebitTxn.setFlag(new Integer(1));
						customerDebitTxn.setCreatedBy(userName);
						customerDebitTxn.setCreatedOn(new Date());
						customerDebitTxn.setModifiedOn(new Date());
						customerDebitTxn.setVbOrganization(organization);
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomerCreditInfo: {}", creditInfo);
						}
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomerDebitTransaction: {}", customerDebitTxn);
						}
						session.save(customerDebitTxn);
						session.update(creditInfo);
						//updating credit amount of Customer Credit Txn
						
						if(_logger.isInfoEnabled()) {
							_logger.info("Updating VbCustomerCreditInfo: {}", creditInfo);
						}
						if(_logger.isInfoEnabled()) {
							_logger.info("Updating VbCustomerDebitTransaction: {}", customerDebitTxn);
						}
					} 
					else {
						saveCustomerAdvanceInfo(session, businessName, command.getInvoiceNo(), organization, presentPayment, userName);
						presentPayment = new Float("0");
					}
				}
			}
		} catch(HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to persist {@link VbCustomerAdvanceInfo}, if
	 * there is any balance available after crediting all the {@link VbCustomerCreditInfo} 
	 * based on businessName and {@link VbOrganization} or persisting advance directly.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param balance - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	private void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance, String userName) throws DataAccessException {
		try {
			Date date = new Date();
			VbCustomerCreditTransaction customerCreditTxn = new VbCustomerCreditTransaction();
			VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
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
			String advanceInfoInvoiceNumber=generateCustomerAdvanceInfoInvoiceNumber(organization);
			advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
			advanceInfo.setCreatedBy(userName);
			advanceInfo.setCreatedOn(date);
			advanceInfo.setModifiedOn(date);
			advanceInfo.setCreditFrom(invoiceNo);
			advanceInfo.setVbOrganization(organization);
			
			//persisting Customer Credit txn case of extra amount
			customerCreditTxn.setBusinessName(businessName);
			customerCreditTxn.setCreditFrom(invoiceNo);
			customerCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
			customerCreditTxn.setFlag(new Integer(1));
			customerCreditTxn.setCreatedBy(userName);
			customerCreditTxn.setCreatedOn(date);
			customerCreditTxn.setModifiedOn(date);
			customerCreditTxn.setVbOrganization(organization);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerCreditTransaction: {}", customerCreditTxn);
			}
			session.save(advanceInfo);
			session.save(customerCreditTxn);
			
			if (_logger.isInfoEnabled()) {
				_logger.info("VbCustomerAdvanceInfo: {}", Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("VbCustomerCreditTransaction: {}", Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
			}
		} catch(HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to generate invoice no for {@link VbDeliveryNote}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateInvoiceNo(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		//For fetching latest invoice no.
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%DN%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
				.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_DELIVERY_NOTE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE)
						.append(seperator).append(formattedDate)
						.append(seperator).append("0001");
			}

		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(seperator)
					.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newInvoiceNo.toString());
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated delivery note invoiceNo is {}", generatedInvoiceNo.toString());
		}
		session.close();
		return generatedInvoiceNo.toString();
	}
	
	/**
	 * This method is responsible to generate invoice no for {@link VbDeliveryNote}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return invoiceNoForPayments - {@link String}
	 * 
	 */
	public String generateInvoiceNoForPayments(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
								+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%COLLECTIONS%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS))
				.uniqueResult();
		StringBuffer invoiceNoForPayments = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				invoiceNoForPayments
						.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				invoiceNoForPayments
						.append(organization.getOrganizationCode())
						.append(seperator)
						.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
						.append(seperator).append(formattedDate)
						.append(seperator).append("0001");
			}

		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod,
					organization, invoiceNo);
			invoiceNoForPayments
					.append(organization.getOrganizationCode())
					.append(seperator)
					.append(OrganizationUtils.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newInvoiceNo);
		}
		session.close();
		return invoiceNoForPayments.toString();
	}
	/**
	 * This method is responsible to get all the productnames.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return productNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getProductNames(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> productNameList = session.createCriteria(VbProduct.class)
				.setProjection(Projections.property("productName"))
				.add(Expression.eq("vbOrganization", organization)).list();
		session.close();
		if(!(productNameList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Product Name List size is : {}", productNameList.size());
			}
			return productNameList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to get the productCost based on the
	 * productName,customerName.
	 * 
	 * @param productName - {@link String}
	 * @param customerName - {@link String}
	 * @return productCost - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Float getProductCost(String productName, String batchNumber, String businessName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Float productCost = (Float) session
				.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbProduct", "product")
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.property("cost"))
				.add(Expression.eq("product.productName", productName))
				.add(Expression.eq("product.batchNumber", batchNumber))
				.add(Expression.eq("customer.businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Product Cost for product {} is : {}",productName, productCost);
		}
		if(productCost != null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("Product Cost for product {} is : {}",productName, productCost);
			}
			return productCost;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible to get the previousCredit based on the
	 * customerName.
	 * 
	 * @param businessName - {@link String}
	 * @return customerCredit - {@link String}
	 * 
	 */
	private String getCustomerCredit(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		Float customerCredit = (Float) session.createCriteria(VbCustomerCreditInfo.class)
				.setProjection(Projections.sum("due"))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("businessName", businessName))
				.uniqueResult();
		session.close();

		if (customerCredit == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for customerName: {}",
						customerCredit);
			}
			return "0.00";
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("previous credit value is: {}", customerCredit);
		}
		return StringUtil.floatFormat(customerCredit);
	}

	/**
	 * This method is responsible to get the previousCredit based on the
	 * customerName.
	 * 
	 * @param businessName - {@link String}
	 * @return customerAdvance - {@link String}
	 * 
	 */
	private String getCustomerAdvance(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		Float customerAdvance = (Float) session.createCriteria(VbCustomerAdvanceInfo.class)
				.setProjection(Projections.sum("balance"))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("businessName", businessName))
				.uniqueResult();
		session.close();

		if (customerAdvance == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for customerName: {}", customerAdvance);
			}
			return "0.00";
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Present Advance value is: {}", customerAdvance);
		}
		return StringUtil.floatFormat(customerAdvance);
	}
	/**
	 * This method is used to get the delivery note products based on the
	 * delivery note id.
	 * 
	 * @param id - {@link Integer}
	 * @return resultList - {@link DeliveryNoteViewResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings({ "unchecked" })
	public List<DeliveryNoteViewResult> getDeliveryNoteProducts(Integer id,	VbOrganization organization) throws DataAccessException {
		VbDeliveryNotePayments payments=null;
		Session session = this.getSession();
		VbDeliveryNote vbDeliveryNote=(VbDeliveryNote) session.createCriteria(VbDeliveryNote.class)
				                           .add(Restrictions.eq("id",id))
				                           .add(Restrictions.eq("vbOrganization", organization))
				                           .uniqueResult();
		if(vbDeliveryNote != null){
			String invoiceNo=vbDeliveryNote.getInvoiceNo();
			String invoiceName=vbDeliveryNote.getInvoiceName();
			String businessName=vbDeliveryNote.getBusinessName();
			Date createdDate=vbDeliveryNote.getCreatedOn();
			Integer deliveryNoteId=vbDeliveryNote.getId();
			List<VbDeliveryNoteProducts> deliveryNoteProductsList= new ArrayList<VbDeliveryNoteProducts>(vbDeliveryNote.getVbDeliveryNoteProductses());
			List<VbDeliveryNotePayments> deliveryNotePaymentsList=new ArrayList<VbDeliveryNotePayments>(vbDeliveryNote.getVbDeliveryNotePaymentses());
			payments=deliveryNotePaymentsList.get(0);
			String fullName=getEmployeeFullName(vbDeliveryNote.getVbSalesBook().getSalesExecutive(), organization);
			List<DeliveryNoteViewResult> resultList=new ArrayList<DeliveryNoteViewResult>();
			DeliveryNoteViewResult result = null;
			if( ! deliveryNoteProductsList.isEmpty()) {
				 for(VbDeliveryNoteProducts product:deliveryNoteProductsList){
					 result=new DeliveryNoteViewResult();
					 result.setProduct(product.getProductName());
					 result.setBatchNumber(product.getBatchNumber());
					 result.setQtyProduct(StringUtil.quantityFormat(product.getProductQty()));
					 result.setCostProduct(StringUtil.currencyFormat(product.getProductCost()));
					 result.setQtyBonus(StringUtil.quantityFormat(product.getBonusQty()));
					 result.setBonusReason(StringUtil.format(product.getBonusReason()));
					 result.setTotalCostProduct(StringUtil.currencyFormat(product.getTotalCost()));
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
					 resultList.add(result);
				 }
				 Collections.sort(resultList);
			 } else {
				 result=new DeliveryNoteViewResult();
				 result.setInvoiceNo(StringUtil.format(invoiceNo));
				 result.setInvoiceName(StringUtil.format(invoiceName));
				 result.setBusinessName(StringUtil.format(businessName));
				 result.setBusinessName(businessName);
				 result.setId(deliveryNoteId);
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
				 resultList.add(result);
			 }
			 session.close();
			 
			 if (_logger.isInfoEnabled()) {
					_logger.info("{} Products have been found.", resultList.size());
				}
			 return resultList;
		}
		else{
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is used to get the data for the grid from VbProduct and
	 * associated VbProductCustomerCost table.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return productResultList - {@link ProductResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getGridData(String salesExecutive,
			String businessName, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		List<VbSalesBookProducts> productList = session.createQuery("FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.salesExecutive = :salesExecutive"
								+ " AND vb.vbSalesBook.vbOrganization = :vbOrganization AND vb.vbSalesBook.flag = :flag GROUP BY vb.productName, vb.batchNumber")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("flag", new Integer(1)).list();
		ProductResult productResult = null;
		if (!productList.isEmpty()) {
			List<ProductResult> productResultList = new ArrayList<ProductResult>();
			for (VbSalesBookProducts vbSalesBookProduct : productList) {
				productResult = new ProductResult();
				String productName = vbSalesBookProduct.getProductName();
				String batchNumber = vbSalesBookProduct.getBatchNumber();
				Float productCost = (Float) session.createCriteria(VbProductCustomerCost.class)
						.createAlias("vbCustomer", "customer")
						.createAlias("vbProduct", "product")
						.setProjection(Projections.property("cost"))
						.add(Expression.eq("customer.businessName",	businessName))
						.add(Expression.eq("product.productName", productName))
						.add(Expression.eq("product.batchNumber", batchNumber))
						.add(Expression.eq("customer.vbOrganization", organization)).uniqueResult();
				if (productCost == null) {
					productCost = (Float) session.createCriteria(VbProduct.class)
							.setProjection(Projections.property("costPerQuantity"))
							.add(Expression.eq("productName", productName))
							.add(Expression.eq("batchNumber", batchNumber))
							.add(Expression.eq("vbOrganization", organization))
							.uniqueResult();
				}
				productResult.setProductCost(StringUtil.currencyFormat(productCost));
				productResult.setProductName(productName);

				VbSalesBook salesBook = getSalesBook(session, organization,	salesExecutive);
				Query qtyQuery = session.createQuery("SELECT SUM(vb.productQty) + SUM(vb.bonusQty) FROM VbDeliveryNoteProducts vb "
										+ "WHERE vb.productName = :productName AND vb.batchNumber = :batchNumber AND vb.vbDeliveryNote.vbOrganization = :vbOrganization"
										+ " AND vb.vbDeliveryNote.vbSalesBook = :vbSalesBook AND vb.vbDeliveryNote.flag = :flag")
						.setParameter("productName", productName)
						.setParameter("batchNumber", batchNumber)
						.setParameter("vbOrganization", organization)
						.setParameter("vbSalesBook", salesBook)
						.setParameter("flag", new Integer(1));
				Integer totalQty = getSingleResultOrNull(qtyQuery);
				Integer allottedQty = vbSalesBookProduct.getQtyAllotted();
				if(allottedQty == null){
					allottedQty = new Integer(0);
				}
				Integer availabelQty = vbSalesBookProduct.getQtyClosingBalance() + allottedQty;
				
				if (totalQty == null) {
					productResult.setAvailableQuantity(StringUtil.quantityFormat(availabelQty));
				} else {
					productResult.setAvailableQuantity(StringUtil.quantityFormat(availabelQty - totalQty));
				}
				productResult.setBatchNumber(vbSalesBookProduct.getBatchNumber());
				productResult.setTotalCost("0.00");
				productResultList.add(productResult);
			}
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("productResultList: {}", productResultList);
			}
			return productResultList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	/**
	 * This method is used to get the invoiceName based on the businessName from
	 * VbCustomer table.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return invoiceName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	private String getInvoiceName(String businessName,	VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String invoiceName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("invoiceName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (invoiceName != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Invoice Name associated with businessName is: {}", invoiceName);
			}
			return invoiceName;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}

	}
	/**
	 * This method is used to get all the delivery notes based on criteria.
	 * 
	 * @param deliveryNoteCommand - {@link DeliveryNoteCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return deliveryNoteList - {@link DeliveryNoteResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteResult> getDeliveryNoteResultsOnCriteria(DeliveryNoteCommand deliveryNoteCommand, VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbDeliveryNote.class).createAlias("vbSalesBook","vb");
		//for display of search Txn of SE get granted_days from vb_employee table for specific SE
		VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		VbSalesBook salesBook=getSalesBookForSearch(session,organization, userName);
		if(salesBook != null){
			if("SLE".equalsIgnoreCase(vbEmployee.getEmployeeType()) && salesBook.getAllotmentType().equalsIgnoreCase(DAILY_SALES_EXECUTIVE)){
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date(), vbEmployee.getGrantedDays())));
			}else{
				Date createdDate = salesBook.getCreatedOn();
				List<Integer> listIds = session.createCriteria(VbSalesBook.class)
						.setProjection(Projections.property("id"))
						.add(Restrictions.between("createdOn", createdDate, DateUtils.getEndTimeStamp(new Date()))).list();
				criteria.add(Restrictions.in("vb.id", listIds));
			}
		}
		if (deliveryNoteCommand != null) {
			if(! deliveryNoteCommand.getCreatedBy().isEmpty()){
				criteria.add(Restrictions.like("createdBy", deliveryNoteCommand.getCreatedBy(), MatchMode.START).ignoreCase());
			}
			if (! deliveryNoteCommand.getBusinessName().isEmpty()) {
				criteria.add(Restrictions.like("businessName", deliveryNoteCommand.getBusinessName(), MatchMode.START).ignoreCase());
			}
			if (! deliveryNoteCommand.getInvoiceName().isEmpty()) {
				criteria.add(Restrictions.like("invoiceName", deliveryNoteCommand.getInvoiceName(), MatchMode.START).ignoreCase());
			}
			if (deliveryNoteCommand.getCreatedOn() != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(deliveryNoteCommand.getCreatedOn()), DateUtils.getEndTimeStamp(deliveryNoteCommand.getCreatedOn())));
			}
		}
		if (organization != null) {
			criteria.add(Restrictions.eq("vbOrganization", organization));
		}
		//added criteria to display DN view page as Latest DN after making CR otherwise view page will be original dayBook.
		criteria.add(Restrictions.eq("flag", new Integer(1)));
		criteria.addOrder(Order.desc("createdOn"));
		List<VbDeliveryNote> criteriaList = criteria.list();
		DeliveryNoteResult deliveryNoteResult = null;
		if(!criteriaList.isEmpty()) {
			List<DeliveryNoteResult> deliveryNoteList = new ArrayList<DeliveryNoteResult>();
			for (VbDeliveryNote vbDeliveryNote : criteriaList) {
				deliveryNoteResult = new DeliveryNoteResult();
				deliveryNoteResult.setBusinessName(vbDeliveryNote.getBusinessName());
				deliveryNoteResult.setInvoiceNo(vbDeliveryNote.getInvoiceNo());
				deliveryNoteResult.setDate(DateUtils.format(vbDeliveryNote.getCreatedOn()));
				Set<VbDeliveryNotePayments> paymentSet = vbDeliveryNote.getVbDeliveryNotePaymentses();
				for (VbDeliveryNotePayments payments : paymentSet) {
					deliveryNoteResult.setBalance(StringUtil.floatFormat(payments.getBalance()));
				}
				deliveryNoteResult.setId(vbDeliveryNote.getId());
				deliveryNoteList.add(deliveryNoteResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Available deliverynote list size is: {}", deliveryNoteList.size());
			}
			return deliveryNoteList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
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
		VbSalesBook salesBook = (VbSalesBook) session
				.createCriteria(VbSalesBook.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("salesExecutive", userName))
				.add(Expression.eq("flag", new Integer(1)))
				.uniqueResult();
		
		return salesBook;
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
		Query query = session.createQuery(
						"FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization AND  (vb.createdOn,vb.cycleId) IN (SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName AND vbs.vbOrganization = :vbOrganization)")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		
		return salesBook;
	}

	/**
	 * This method is responsible to get the Invoice Name, Customer Credit, Customer Advance for the Business Name.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return result - {@link DeliveryNoteCustomerResult}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public DeliveryNoteCustomerResult getCustomerData(String businessName, VbOrganization organization) throws DataAccessException {
		String invoiceName = getInvoiceName(businessName, organization);
		String customerAdvance = getCustomerAdvance(businessName, organization);
		String customerCredit = getCustomerCredit(businessName, organization);
		DeliveryNoteCustomerResult result = new DeliveryNoteCustomerResult();
		result.setInvoiceName(invoiceName);
		result.setCustomerAdvance(customerAdvance);
		result.setCustomerCredit(customerCredit);
		if(_logger.isDebugEnabled()) {
			_logger.debug("InvoiceName for configured DN: {}", result.getInvoiceName());
			_logger.debug("Customer Advance for configured DN: {}", result.getCustomerAdvance());
			_logger.debug("Customer Credit for configured DN: {}", result.getCustomerCredit());
		}
		return result;
	}
	
	/**
	 * This method is responsible to get {@link VbCustomer}.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customer - {@link VbCustomer}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbCustomer getCustomer(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if(customer != null) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbCustomer: {}", customer.toString());
			}
			return customer;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to get createdBy of employee.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link VbCustomer}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public String getCreatedBy(String salesExecutive, VbOrganization organization) throws DataAccessException {
		VbEmployee employee = getSalesExecutiveFullName(salesExecutive, organization);
		String createdBy = employee.getCreatedBy();
		
		if(_logger.isInfoEnabled()) {
			_logger.info("DeliveryNote created by: {}", createdBy);
		}
		return createdBy;
	}
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerCreditInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerCreditInfoInvoiceNumber(VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbCustomerCreditInfo vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%CR%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
				.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR).append("0001");
			}

		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo = generatedInvoiceNo
					.append(organization.getOrganizationCode())
					.append(OrganizationUtils.SEPERATOR)
					.append(OrganizationUtils.TXN_TYPE_CUSTOMER_CREDIT)
					.append(OrganizationUtils.SEPERATOR).append(formattedDate)
					.append(OrganizationUtils.SEPERATOR).append(newInvoiceNo);
		}
		session.close();

		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo genertaed for customer credit is: {}", generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}
	
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerAdvanceInfoInvoiceNumber(VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbCustomerAdvanceInfo vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%AD%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
				.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_ADVANCE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_ADVANCE)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_ADVANCE)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR).append("0001");
			}
		} else {
			String[] previousInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(OrganizationUtils.SEPERATOR)
					.append(OrganizationUtils.TXN_TYPE_ADVANCE)
					.append(OrganizationUtils.SEPERATOR).append(formattedDate)
					.append(OrganizationUtils.SEPERATOR).append(newInvoiceNo);
		}
		session.close();

		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo generated for customer advance is: {}", generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}

	/**
	 * This method is responsible for get previous delivery note based on current invoiceNumber,flag=0,and previousDeliveryNoteId
	 *  
	 * @param deliveryNoteId - {@link Integer}
	 * @param invoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return deliveryNoteMaxId - {@link Integer}
	 */
	public Integer getPreviousDeliveryNoteInvoiceNumber(Integer deliveryNoteId,
			String invoiceNumber, VbOrganization organization, String username) {
		Session session = this.getSession();
		Integer deliveryNoteIds = new Integer(0);
		Query query = session.createQuery(
						"SELECT MAX(vbd.id) FROM VbDeliveryNote vbd where vbd.invoiceNo "
						+ "LIKE :invoiceNo AND vbd.flag= :flag AND vbd.id < :deliveryNoteId AND vbd.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(0))
				.setParameter("deliveryNoteId", deliveryNoteId)
				.setParameter("vbOrganization", organization);
		Integer deliveryNoteMaxId = getSingleResultOrNull(query);
		session.close();
		
		if (deliveryNoteMaxId != null) {
			return deliveryNoteMaxId;
		}
		return deliveryNoteIds;
	}
}
