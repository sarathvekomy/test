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
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCustomerResult;
import com.vekomy.vbooks.mysales.command.DeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteResult;
import com.vekomy.vbooks.mysales.command.ProductResult;
import com.vekomy.vbooks.util.DateUtils;
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
	 * @param products
	 * @param deliveryNoteList
	 * @param userName
	 * @param organization
	 * @return
	 */
	public Boolean saveDeliveryNote(DeliveryNoteProductCommand products,
			List<DeliveryNoteCommand> deliveryNoteList, String userName,
			VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Boolean isSaved = Boolean.FALSE;
		Date date = new Date();
		VbDeliveryNote instanceNote = new VbDeliveryNote();
		VbCustomerCreditInfo customerCreditInfo = new VbCustomerCreditInfo();
		VbDeliveryNoteProducts instanceProducts = null;
		if (instanceNote != null) {
			instanceNote.setCreatedOn(date);
			instanceNote.setCreatedBy(userName);
			instanceNote.setModifiedOn(date);
			instanceNote.setVbOrganization(organization);
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
				customerCreditInfo.setBusinessName(businessName);

				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteProducts: {}", instanceProducts);
				}
				session.save(instanceProducts);
			}

			isSaved = Boolean.TRUE;

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
			instancePayments.setVbDeliveryNote(instanceNote);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNotePayments: {}", instancePayments);
			}
			session.save(instancePayments);
			isSaved = Boolean.TRUE;
		}
		
		if (customerCreditInfo != null) {
			String businessName = instanceNote.getBusinessName();
			String invoiceName = instanceNote.getInvoiceNo();
			customerCreditInfo.setBusinessName(businessName);
			customerCreditInfo.setCreditFrom(invoiceName);
			customerCreditInfo.setCreatedBy(userName);
			customerCreditInfo.setCreatedOn(date);
			customerCreditInfo.setModifiedOn(date);
			Float balance = products.getPresentPayable() - products.getPresentPayment();
			Float advance = products.getPresentAdvance();
			if (advance < balance) {
				Float remainingBalance = balance - advance;
				customerCreditInfo.setBalance(remainingBalance);
				customerCreditInfo.setDue(remainingBalance);
				
				// For updating advanceInfo.
				VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, organization);
				if (advanceInfo != null) {
					advanceInfo.setBalance(new Float(0));
					advanceInfo.setModifiedBy(userName);
					advanceInfo.setModifiedOn(new Date());
					advanceInfo.setDebitTo(invoiceName);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating advanceInfo: {}", advanceInfo);
					}
					session.update(advanceInfo);
				}
			} else {
				Float remainingBalance = advance - balance;
				customerCreditInfo.setDue(new Float("0"));
				VbCustomerCreditInfo vbCustomerCreditInfo = null;
				while (remainingBalance != 0) {
					vbCustomerCreditInfo = getPreviousCreditByBusinessName(session, businessName, organization);
					if(vbCustomerCreditInfo != null) {
						vbCustomerCreditInfo.setModifiedBy(userName);
						vbCustomerCreditInfo.setModifiedOn(date);
						Float due = vbCustomerCreditInfo.getDue();
						if(due < remainingBalance) {
							remainingBalance = remainingBalance - due;
							vbCustomerCreditInfo.setDue(new Float(0));
						} else {
							vbCustomerCreditInfo.setDue(due - remainingBalance);
							remainingBalance = new Float(0);
						}
						vbCustomerCreditInfo.setDebitTo(invoiceName);
						
						if(_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomerCreditInfo: {}", vbCustomerCreditInfo);
						}
						session.update(vbCustomerCreditInfo);
					} else {
						VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, organization);
						if(advanceInfo != null) {
							// Updating existing advance.
							Float previousAdvance = advanceInfo.getBalance();
							if(previousAdvance == remainingBalance){
								advanceInfo.setBalance(new Float(0));
							} else {
								remainingBalance = remainingBalance - previousAdvance;
								if(remainingBalance < 0) {
									remainingBalance = - (remainingBalance);
									advanceInfo.setBalance(previousAdvance - remainingBalance);
								} else {
									advanceInfo.setBalance(previousAdvance + remainingBalance);
								}
							}
							advanceInfo.setModifiedBy(userName);
							advanceInfo.setModifiedOn(date);
							advanceInfo.setDebitTo(invoiceName);
							
							if(_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerAdvanceInfo: {}", advanceInfo);
							}
							session.update(advanceInfo);
						} else {
							// If there is no advance for businessName creating new record.
							advanceInfo = new VbCustomerAdvanceInfo();
							advanceInfo.setAdvance(remainingBalance);
							advanceInfo.setBalance(remainingBalance);
							advanceInfo.setBusinessName(businessName);
							advanceInfo.setCreatedBy(userName);
							advanceInfo.setCreatedOn(date);
							advanceInfo.setCreditFrom(invoiceName);
							advanceInfo.setModifiedOn(date);
							advanceInfo.setVbOrganization(organization);
							
							if(_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
							}
							session.save(advanceInfo);
						}
						
						// Updating remaining balance.
						remainingBalance = new Float(0);
					}
				}
			}
			customerCreditInfo.setVbOrganization(organization);
			session.save(customerCreditInfo);
			isSaved = Boolean.TRUE;
		}
		
		txn.commit();
		session.close();
		return isSaved;
	}
	
	/**
	 * This method is responsible to get {@link VbCustomerAdvanceInfo} based on minimum of createdOn.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customerAdvanceInfo - {@link VbCustomerAdvanceInfo}
	 * 
	 */
	private VbCustomerAdvanceInfo getPreviousAdvanceByBusinessName(Session session, String businessName, VbOrganization organization) {
		VbCustomerAdvanceInfo customerAdvanceInfo = (VbCustomerAdvanceInfo) session.createQuery("FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN (" +
				"SELECT MIN(vbc.createdOn) FROM VbCustomerAdvanceInfo vbc WHERE vbc.businessName = :businessName AND " +
				"vbc.vbOrganization = :vbOrganization AND vbc.balance > :balance)")
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.setParameter("balance", new Float(0))
				.uniqueResult();
		return customerAdvanceInfo;
	}
	
	/**
	 * This method is responsible to get {@link VbCustomerCreditInfo} based on minimum of createdOn.
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customerCreditInfo - {@link VbCustomerCreditInfo}
	 * 
	 */
	private VbCustomerCreditInfo getPreviousCreditByBusinessName(Session session, String businessName, VbOrganization organization){
		VbCustomerCreditInfo customerCreditInfo = (VbCustomerCreditInfo) session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN (" +
				"SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE vbc.businessName = :businessName AND " +
				"vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
				.setParameter("businessName", businessName)
				.setParameter("vbOrganization", organization)
				.setParameter("due", new Float(0))
				.uniqueResult();
		return customerCreditInfo;
	}
	/**
	 * This method is responsible to persist {@link VbDeliveryNote}, update
	 * {@link VbCustomerAdvanceInfo} and {@link VbCustomerCreditInfo}.
	 * 
	 * @param command
	 *            - {@link DeliveryNoteCommand}
	 * @param deliveryNoteProductCommand
	 *            - {@link VbDeliveryNotePayments}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @param userName
	 *            - {@link String}
	 * @return isSaved - {@link Boolean}
	 * 
	 */
	public Boolean savePayments(DeliveryNoteCommand command,
			DeliveryNoteProductCommand deliveryNoteProductCommand,
			VbOrganization organization, String userName) {
		Boolean isSaved = Boolean.TRUE;
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();

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
			deliveryNote.setVbSalesBook(salesBook);
			deliveryNote.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting deliveryNote: {}", deliveryNote);
			}
			session.save(deliveryNote);
			isSaved = Boolean.TRUE;
		}

		Float presentPayment = deliveryNoteProductCommand.getPresentPayment();
		if (deliveryNotePayments != null) {
			deliveryNotePayments.setBalance(deliveryNoteProductCommand.getBalance());
			deliveryNotePayments.setBankName(deliveryNoteProductCommand.getBankName());
			deliveryNotePayments.setBranchName(deliveryNoteProductCommand.getBranchName());
			deliveryNotePayments.setChequeNo(deliveryNoteProductCommand.getChequeNo());
			deliveryNotePayments.setPaymentType(deliveryNoteProductCommand.getPaymentType());
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
			isSaved = Boolean.TRUE;
		}
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
		updateCustomerCreditInfo(session, command, deliveryNoteProductCommand, organization, userName);
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
		txn.commit();
		session.close();
		return isSaved;
	}

	/**
	 * This method is responsible to update {@link VbCustomerAdvanceInfo} and
	 * {@link VbCustomerCreditInfo}.
	 * 
	 * @param session
	 *            - {@link Session}
	 * @param command
	 *            - {@link DeliveryNoteCommand}
	 * @param productCommand
	 *            - {@link DeliveryNoteProductCommand}
	 * @param organization
	 *            - {@link VbOrganization}
	 * 
	 */
	private void updateCustomerCreditInfo(Session session,
			DeliveryNoteCommand command,
			DeliveryNoteProductCommand productCommand,
			VbOrganization organization, String userName) {
		Float presentPayment = productCommand.getPresentPayment();
		String businessName = command.getBusinessName();
		if (presentPayment > 0) {
			Query query = null;
			while (presentPayment != 0) {
				query = session.createQuery(
								"FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
								+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
								+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
						.setParameter("businessName", command.getBusinessName())
						.setParameter("vbOrganization", organization)
						.setParameter("due", new Float("0"));

				VbCustomerCreditInfo creditInfo = getSingleResultOrNull(query);
				if (creditInfo != null) {
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
						presentPayment = new Float(0.0);
					} else {
						presentPayment = presentPayment - existingDue;
						creditInfo.setDue(new Float("0.00"));
					}
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbCustomerCreditInfo: {}", creditInfo);
					}
					session.update(creditInfo);
				} else {
					saveCustomerAdvanceInfo(session, businessName, command.getInvoiceNo(), organization, presentPayment, userName);
					presentPayment = new Float("0");
				}
			}
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
	 * 
	 */
	private void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance, String userName) {
		Date date = new Date();
		VbCustomerAdvanceInfo advanceInfo = getPreviousAdvanceByBusinessName(session, businessName, organization);
		if(advanceInfo != null) {
			// Updating existing advance.
			if(balance < 0) {
				balance = -(balance);
			}
			advanceInfo.setBalance(advanceInfo.getBalance() + balance);
			advanceInfo.setModifiedBy(userName);
			advanceInfo.setModifiedOn(date);
			advanceInfo.setDebitTo(invoiceNo);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomerAdvanceInfo: {}", advanceInfo);
			}
			session.update(advanceInfo);
		} else {
			advanceInfo = new VbCustomerAdvanceInfo();
			if (balance < 0) {
				Float newBalance = -(balance);
				advanceInfo.setAdvance(newBalance);
				advanceInfo.setBalance(newBalance);
			} else {
				advanceInfo.setAdvance(balance);
				advanceInfo.setBalance(balance);
			}
			advanceInfo.setBusinessName(businessName);
			advanceInfo.setCreatedBy(userName);
			advanceInfo.setCreatedOn(date);
			advanceInfo.setModifiedOn(date);
			advanceInfo.setCreditFrom(invoiceNo);
			advanceInfo.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
			}
			session.save(advanceInfo);
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
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%DN%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		String generatedInvoiceNo = null;
		if (latestInvoiceNo == null) {
			generatedInvoiceNo = organization.getOrganizationCode().concat("DN").concat("1");
		} else {
			latestInvoiceNo = latestInvoiceNo.substring(latestInvoiceNo.indexOf("DN") + 2, latestInvoiceNo.length());
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			Integer newInvoiceNo = ++invoiceNo;
			generatedInvoiceNo = organization.getOrganizationCode().concat("DN").concat(newInvoiceNo.toString());
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated delivery note invoiceNo is {}", generatedInvoiceNo);
		}
		session.close();
		return generatedInvoiceNo;
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
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%COLLECTIONS%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		String invoiceNoForPayments = null;
		if (latestInvoiceNo == null) {
			invoiceNoForPayments = organization.getOrganizationCode().concat("COLLECTIONS").concat("1");
		} else {
			latestInvoiceNo = latestInvoiceNo.substring(latestInvoiceNo.indexOf("COLLECTIONS") + 11, latestInvoiceNo.length());
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			Integer newInvoiceNo = ++invoiceNo;
			invoiceNoForPayments = organization.getOrganizationCode().concat("COLLECTIONS").concat(newInvoiceNo.toString());
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated delivery note invoiceNo for payments is {}", invoiceNoForPayments);
		}
		session.close();
		return invoiceNoForPayments;
	}
	@SuppressWarnings("unchecked")
	public List<String> getProductNames(VbOrganization organization) {
		Session session = this.getSession();
		List<String> productNameList = session.createCriteria(VbProduct.class)
				.setProjection(Projections.property("productName"))
				.add(Expression.eq("vbOrganization", organization)).list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Product Name List size is : {}", productNameList.size());
		}
		return productNameList;
	}

	/**
	 * This method is responsible to get the productCost based on the
	 * productName,customerName.
	 * 
	 * @param productName - {@link String}
	 * @param customerName - {@link String}
	 * @return productCost - {@link Float}
	 * 
	 */
	public Float getProductCost(String productName, String batchNumber, String businessName,
			VbOrganization organization) {
		Float productCost = null;
		Session session = this.getSession();
		productCost = (Float) session
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
			_logger.debug("Product Cost List size is : {}", productCost);
		}
		return productCost;
	}

	/**
	 * This method is responsible to get the previousCredit based on the
	 * customerName.
	 * 
	 * @param customerName
	 * @return Float
	 * 
	 */
	private String getCustomerCredit(String businessName,
			VbOrganization organization) {
		Session session = this.getSession();
		Float customerCredit = (Float) session
				.createCriteria(VbCustomerCreditInfo.class)
				.setProjection(Projections.sum("due"))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("businessName", businessName))
				.uniqueResult();
		session.close();

		if (customerCredit == null) {
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for customerName: {}", customerCredit);
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
	 * @param customerName
	 * @return Float
	 * 
	 */
	private String getCustomerAdvance(String businessName,
			VbOrganization organization) {
		Session session = this.getSession();
		Float customerAdvance = (Float) session
				.createCriteria(VbCustomerAdvanceInfo.class)
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
	 * @param id
	 * @return VbDeliveryNote
	 */
	@SuppressWarnings({ "unchecked" })
	public List<VbDeliveryNoteProducts> getDeliveryNoteProducts(Integer id,
			VbOrganization organization) {
		Session session = this.getSession();
		VbDeliveryNote vbDeliveryNote=(VbDeliveryNote) session.get(VbDeliveryNote.class,id);
		List<VbDeliveryNoteProducts> list=session.createCriteria(VbDeliveryNoteProducts.class)
				         .createAlias("vbDeliveryNote", "deliveryNote")
				         .add(Restrictions.eq("deliveryNote.vbOrganization", organization))
				         .add(Restrictions.eq("vbDeliveryNote", vbDeliveryNote))
				         .addOrder(Order.asc("productName"))
				         .addOrder(Order.asc("batchNumber"))
				         .list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbDeliveryNoteProducts: {}", list);
		}
		return list;
	}

	/**
	 * This method is used to get the data for the grid from VbProduct and
	 * associated VbProductCustomerCost table.
	 * 
	 * @return List<ProductResult>
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getGridData(String salesExecutive,
			String businessName, VbOrganization organization) {
		List<ProductResult> productResultList = null;
		Session session = this.getSession();
		List<VbSalesBookProducts> productList = session.createQuery("FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.salesExecutive = :salesExecutive" +
				" AND vb.vbSalesBook.vbOrganization = :vbOrganization AND vb.vbSalesBook.flag = :flag GROUP BY vb.productName, vb.batchNumber")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("flag", new Integer(1))
				.list();
		ProductResult productResult = null;
		productResultList = new ArrayList<ProductResult>();
		for (VbSalesBookProducts vbSalesBookProduct : productList) {
			productResult = new ProductResult();
			String productName = vbSalesBookProduct.getProductName();
			String batchNumber = vbSalesBookProduct.getBatchNumber();
			Float productCost = (Float) session
					.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbProduct", "product")
					.setProjection(Projections.property("cost"))
					.add(Expression.eq("customer.businessName", businessName))
					.add(Expression.eq("product.productName", productName))
					.add(Expression.eq("product.batchNumber", batchNumber))
					.add(Expression.eq("customer.vbOrganization", organization))
					.uniqueResult();
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

			VbSalesBook salesBook = getSalesBook(session, organization, salesExecutive);
			Query qtyQuery = session.createQuery(
							"SELECT SUM(vb.productQty) + SUM(vb.bonusQty) FROM VbDeliveryNoteProducts vb "
									+ "WHERE vb.productName = :productName AND vb.batchNumber = :batchNumber AND vb.vbDeliveryNote.vbOrganization = :vbOrganization"
									+ " AND vb.vbDeliveryNote.vbSalesBook = :vbSalesBook")
					.setParameter("productName", productName)
					.setParameter("batchNumber", batchNumber)
					.setParameter("vbOrganization", organization)
					.setParameter("vbSalesBook", salesBook);
			Integer totalQty = getSingleResultOrNull(qtyQuery);
			Integer allottedQty = vbSalesBookProduct.getQtyAllotted();
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
	}
	/**
	 * This method is used to get the invoiceName based on the businessName from
	 * VbCustomer table.
	 * 
	 * @param businessName
	 * @return String
	 */
	private String getInvoiceName(String businessName,
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
	 * This method is used to get all the delivery notes based on criteria.
	 * 
	 * @return deliveryNoteList
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteResult> getDeliveryNoteResultsOnCriteria(DeliveryNoteCommand deliveryNoteCommand, VbOrganization organization, String userName) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbDeliveryNote.class).createAlias("vbSalesBook","vb");
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
		criteria.addOrder(Order.asc("createdOn"));
		List<VbDeliveryNote> criteriaList = criteria.list();
		List<DeliveryNoteResult> deliveryNoteList = new ArrayList<DeliveryNoteResult>();
		DeliveryNoteResult deliveryNoteResult = null;
		for (VbDeliveryNote vbDeliveryNote : criteriaList) {
			deliveryNoteResult = new DeliveryNoteResult();
			deliveryNoteResult.setBusinessName(vbDeliveryNote.getBusinessName());
			deliveryNoteResult.setInvoiceName(vbDeliveryNote.getInvoiceName());
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
			_logger.debug("deliveryNoteList: {}", deliveryNoteList);
		}
		return deliveryNoteList;
	}
	/**
	 * This method is responsible to get {@link VbSalesBook} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session
	 * @param organization
	 * @param userName
	 * @return salesBook
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
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("username", userName))
				.uniqueResult();
		session.close();
		
		return employee;
	}

	/**
	 * This method is responsible to get the Invoice Name, Customer Credit, Customer Advance for the Business Name.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return result - {@link DeliveryNoteCustomerResult}
	 */
	public DeliveryNoteCustomerResult getCustomerData(String businessName, VbOrganization organization) {
		String invoiceName = getInvoiceName(businessName, organization);
		String customerAdvance = getCustomerAdvance(businessName, organization);
		String customerCredit = getCustomerCredit(businessName, organization);
		DeliveryNoteCustomerResult result = new DeliveryNoteCustomerResult();
		result.setInvoiceName(invoiceName);
		result.setCustomerAdvance(customerAdvance);
		result.setCustomerCredit(customerCredit);
		if(_logger.isDebugEnabled()) {
			_logger.debug("DeliveryNoteCustomerResult :{}", result);
		}
		return result;
	}
	
	/**
	 * This method is responsible to get {@link VbCustomer}.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return customer - {@link VbCustomer}
	 * 
	 */
	public VbCustomer getCustomer(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("businessName", businessName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		return customer;
	}
	
	/**
	 * This method is responsible to get createdBy of employee.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link VbCustomer}
	 * 
	 */
	public String getCreatedBy(String salesExecutive, VbOrganization organization) {
		String createdBy = null;
		VbEmployee employee = getSalesExecutiveFullName(salesExecutive, organization);
		createdBy = employee.getCreatedBy();
		
		return createdBy;
	}
}
