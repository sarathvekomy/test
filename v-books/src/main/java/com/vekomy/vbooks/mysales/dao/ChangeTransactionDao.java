/**
 * com.vekomy.vbooks.mysales.dao.ChangeTransactionDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: June 18, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.batik.css.engine.value.css2.SrcManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount;
import com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbDayBookProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestPayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequestProducts;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;
import com.vekomy.vbooks.hibernate.model.VbSystemAlertsHistory;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestJournalCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestProductResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnsResult;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform operations on Transaction change request in sales
 * module for different transaction type.
 * 
 * @author Ankit
 * 
 */
public class ChangeTransactionDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
private static final Logger _logger = LoggerFactory.getLogger(ChangeTransactionDao.class);
	/**
	 * This method is responsible to persist the {@link VbDeliveryNoteChangeRequest},
	 * {@link VbDeliveryNoteChangeRequestPayments} and {@link VbDeliveryNoteChangeRequestProducts}.
	 * 
	 * @param products
	 * @param deliveryNoteList
	 * @param userName
	 * @param organization
	 * @return isSaved
	 */
	public Boolean saveDeliveryNote(ChangeRequestDeliveryNoteProductCommand products,String[] formChangedValues,
			List<ChangeRequestDeliveryNoteCommand> deliveryNoteList, String userName,VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Boolean isSaved = Boolean.FALSE;
		Date date = new Date();
		ArrayListMultimap<String,String> changedFormFieldsMap = ArrayListMultimap.create();
		for(final String s : formChangedValues) {
			   final String split[] = s.split(":");
			   changedFormFieldsMap.put(split[0], split[1]);
			}
		VbDeliveryNoteChangeRequest instanceNote = new VbDeliveryNoteChangeRequest();
		VbDeliveryNoteChangeRequestProducts instanceProducts = null;
		if (instanceNote != null) {
			instanceNote.setCreatedOn(date);
			instanceNote.setCreatedBy(userName);
			instanceNote.setModifiedOn(date);
			instanceNote.setVbOrganization(organization);
			instanceNote.setCrDescription(products.getDescription());
			instanceNote.setStatus(CRStatus.PENDING.name());
			VbSalesBook salesBook = getSalesBook(session, organization, userName);
			if (salesBook != null) {
				instanceNote.setVbSalesBook(salesBook);
			}
			List<String> changedProductQty=changedFormFieldsMap.get("productQty");
			List<String> productBonus=changedFormFieldsMap.get("productBonus");
			List<String> productBonusReason=changedFormFieldsMap.get("productBonusReason");
			for (ChangeRequestDeliveryNoteCommand deliveryNoteCommand : deliveryNoteList) {
				String businessName = deliveryNoteCommand.getBusinessName();
				instanceNote.setBusinessName(businessName);
				List<String> customerInvoiceName = changedFormFieldsMap.get("invoiceName");
				if(customerInvoiceName.size()!=0){
				for(String customerInvoiceName1 : customerInvoiceName) {
					if(customerInvoiceName1.equals(deliveryNoteCommand.getInvoiceName())){
						instanceNote.setInvoiceName(deliveryNoteCommand.getInvoiceName().concat(",1"));
					}
				}
				}else{
					instanceNote.setInvoiceName(deliveryNoteCommand.getInvoiceName().concat(",0"));
				}
				instanceNote.setInvoiceNo(deliveryNoteCommand.getInvoiceNo());
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteChangeRequest: {}", instanceNote);
				}
				session.save(instanceNote);

				instanceProducts = new VbDeliveryNoteChangeRequestProducts();
				if(changedProductQty.size()!=0 || productBonus.size()!=0 || productBonusReason.size()!=0) {
				for(String ChangeProductQuantity : changedProductQty) {
				    			if(ChangeProductQuantity.equals(deliveryNoteCommand.getProductQuantity())){
				    				String productCost = deliveryNoteCommand.getProductCost();
									String productQty = deliveryNoteCommand.getProductQuantity();
									instanceProducts.setProductQty(productQty.concat(",1"));
									Float totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
									instanceProducts.setTotalCost(totalCost.toString().concat(",1"));
									instanceProducts.setVbDeliveryNoteChangeRequest(instanceNote);
									if(deliveryNoteCommand.getBonusQuantity().equals("")){
										String bonusQty = deliveryNoteCommand.getBonusQuantity();
										instanceProducts.setBonusQty(bonusQty.toString().concat(",0"));
									}else{
									Integer bonusQty = Integer.parseInt(deliveryNoteCommand.getBonusQuantity());
									instanceProducts.setBonusQty(bonusQty.toString().concat(",0"));
									}
									instanceProducts.setBonusReason(deliveryNoteCommand.getBonusReason().concat(",0"));
								}
				             }
				    			for(String changedProductBonus : productBonus){
				    				 if(changedProductBonus.equals(deliveryNoteCommand.getBonusQuantity())){
										String productCost = deliveryNoteCommand.getProductCost();
										String productQty = deliveryNoteCommand.getProductQuantity();
										instanceProducts.setProductQty(productQty.concat(",0"));
										Float totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
										instanceProducts.setTotalCost(totalCost.toString().concat(",1"));
										instanceProducts.setVbDeliveryNoteChangeRequest(instanceNote);
										if(deliveryNoteCommand.getBonusQuantity().equals("")){
											String bonusQty = deliveryNoteCommand.getBonusQuantity();
											instanceProducts.setBonusQty(bonusQty.toString().concat(",1"));
										}else{
										Integer bonusQty = Integer.parseInt(deliveryNoteCommand.getBonusQuantity());
										instanceProducts.setBonusQty(bonusQty.toString().concat(",1"));
										}
										instanceProducts.setBonusReason(deliveryNoteCommand.getBonusReason().concat(",0"));
									}
				    			}
				    			for(String changedProductBonusReason : productBonusReason){
				    				if(changedProductBonusReason.equals(deliveryNoteCommand.getBonusReason())){
										String productCost = deliveryNoteCommand.getProductCost();
										String productQty = deliveryNoteCommand.getProductQuantity();
										instanceProducts.setProductQty(productQty.concat(",0"));
										Float totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
										instanceProducts.setTotalCost(totalCost.toString().concat(",0"));
										instanceProducts.setVbDeliveryNoteChangeRequest(instanceNote);
										if(deliveryNoteCommand.getBonusQuantity().equals("")){
											String bonusQty = deliveryNoteCommand.getBonusQuantity();
											instanceProducts.setBonusQty(bonusQty.toString().concat(",0"));
										}else{
										Integer bonusQty = Integer.parseInt(deliveryNoteCommand.getBonusQuantity());
										instanceProducts.setBonusQty(bonusQty.toString().concat(",0"));
										}
										instanceProducts.setBonusReason(deliveryNoteCommand.getBonusReason().concat(",1"));
									}
				    			}
				    		}
								else{
									String productCost = deliveryNoteCommand.getProductCost();
									String productQty = deliveryNoteCommand.getProductQuantity();
									instanceProducts.setProductQty(productQty.concat(",0"));
									Float totalCost = Float.parseFloat(productCost) * Integer.parseInt(productQty);
									instanceProducts.setTotalCost(totalCost.toString().concat(",0"));
									Integer bonusQty = Integer.parseInt(deliveryNoteCommand.getBonusQuantity());
									instanceProducts.setBonusQty(bonusQty.toString().concat(",0"));
									instanceProducts.setBonusReason(deliveryNoteCommand.getBonusReason().concat(",0"));
								}
				String productCost = deliveryNoteCommand.getProductCost();
    			instanceProducts.setProductCost(productCost.concat(",0"));
				String productName = deliveryNoteCommand.getProductName();
				instanceProducts.setBatchNumber(deliveryNoteCommand.getBatchNumer().concat(",0"));
				instanceProducts.setProductName(productName.concat(",0"));
				instanceProducts.setVbDeliveryNoteChangeRequest(instanceNote);
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbDeliveryNoteChangeRequestProducts: {}", instanceProducts);
				}
				session.save(instanceProducts);
			}
			isSaved = Boolean.TRUE;
		}

		VbDeliveryNoteChangeRequestPayments instancePayments = new VbDeliveryNoteChangeRequestPayments();
		if (instanceNote != null) {
			List<String> presentPayment = changedFormFieldsMap.get("presentPayment");
			if(presentPayment.size()!=0){
			for(String presentPayment1 : presentPayment) {
				if(presentPayment1.equals(products.getPresentPayment())){
					instancePayments.setPresentPayment(products.getPresentPayment().concat(",1"));
					instancePayments.setBalance(products.getBalance().concat(",1"));
					instancePayments.setTotalPayable(products.getTotalPayable().concat(",1"));
				}
			}
			}else{
				instancePayments.setPresentPayment(products.getPresentPayment().concat(",0"));
				instancePayments.setBalance(products.getBalance().concat(",0"));
				instancePayments.setTotalPayable(products.getTotalPayable().concat(",0"));
			}
			instancePayments.setPresentPayable(products.getPresentPayable());
			instancePayments.setPaymentType(products.getPaymentType());
			instancePayments.setPresentAdvance(products.getPresentAdvance());
			instancePayments.setPreviousCredit(products.getPreviousCredit());
			instancePayments.setBankName(products.getBankName());
			instancePayments.setChequeNo(products.getChequeNo());
			instancePayments.setBranchName(products.getBranchName());
			instancePayments.setVbDeliveryNoteChangeRequest(instanceNote);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNoteChangeRequestPayments: {}", instancePayments);
			}
			session.save(instancePayments);
			isSaved = Boolean.TRUE;
		}
		
		txn.commit();
		session.close();
		return isSaved;
	}
	
	/**
	 * This method is responsible to persist {@link VbDeliveryNoteChangeRequest}, update
	 * {@link VbCustomerAdvanceInfo} and {@link VbCustomerCreditInfo}.
	 * 
	 * @param command
	 *            - {@link ChangeRequestDeliveryNoteCommand}
	 * @param deliveryNoteProductCommand
	 *            - {@link ChangeRequestDeliveryNoteProductCommand}
	 * @param deliveryNotePayments
	 *            - {@link VbDeliveryNoteChangeRequestPayments}
	 * @param organization
	 *            - {@link VbOrganization}
	 * @param userName
	 *            - {@link String}
	 * @return isSaved - {@link Boolean}
	 * 
	 */
	public Boolean savePayments(ChangeRequestDeliveryNoteCommand command,String[] formChangedValues,
			ChangeRequestDeliveryNoteProductCommand deliveryNoteProductCommand,
			VbOrganization organization, String userName) {
		Boolean isSaved = Boolean.TRUE;
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		ArrayListMultimap<String,String> changedFormFieldsMap = ArrayListMultimap.create();
		for(final String s : formChangedValues) {
			   final String split[] = s.split(":");
			   changedFormFieldsMap.put(split[0], split[1]);
			}
		VbDeliveryNoteChangeRequest deliveryNote = new VbDeliveryNoteChangeRequest();
		VbDeliveryNoteChangeRequestPayments deliveryNotePayments = new VbDeliveryNoteChangeRequestPayments();
		VbSalesBook salesBook = getSalesBook(session, organization, userName);
		String businessName = command.getBusinessName();
		if (deliveryNote != null) {
			List<String> customerInvoiceName = changedFormFieldsMap.get("invoiceName");
			if(customerInvoiceName.size()!=0){
			for(String customerInvoiceName1 : customerInvoiceName) {
				if(customerInvoiceName1.equals(command.getInvoiceName())){
					deliveryNote.setInvoiceName(command.getInvoiceName().concat(",1"));
				}
			}
			}else{
				   deliveryNote.setInvoiceName(command.getInvoiceName().concat(",0"));
			}
			deliveryNote.setBusinessName(businessName);
			//deliveryNote.setInvoiceName(command.getInvoiceName());
			deliveryNote.setCreatedBy(userName);
			Date date = new Date();
			deliveryNote.setCreatedOn(date);
			deliveryNote.setModifiedOn(date);
			deliveryNote.setCrDescription(deliveryNoteProductCommand.getDescription());
			deliveryNote.setInvoiceNo(command.getInvoiceNo());
			deliveryNote.setStatus(CRStatus.PENDING.name());
			deliveryNote.setVbSalesBook(salesBook);
			deliveryNote.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting deliveryNote: {}", deliveryNote);
			}
			session.save(deliveryNote);
			isSaved = Boolean.TRUE;
		}

		Float presentPayment = Float.parseFloat(deliveryNoteProductCommand.getPresentPayment());
		if (deliveryNotePayments != null) {
			List<String> presentPayments = changedFormFieldsMap.get("presentPayment");
			if(presentPayments.size()!=0){
			for(String presentPayment1 : presentPayments) {
				if(presentPayment1.equals(deliveryNoteProductCommand.getPresentPayment())){
					deliveryNotePayments.setPresentPayment(deliveryNoteProductCommand.getPresentPayment().concat(",1"));
					deliveryNotePayments.setBalance(deliveryNoteProductCommand.getBalance().concat(",1"));
					deliveryNotePayments.setTotalPayable(deliveryNoteProductCommand.getTotalPayable().concat(",1"));
				}
			}
			}else{
				deliveryNotePayments.setPresentPayment(deliveryNoteProductCommand.getPresentPayment().concat(",0"));
				deliveryNotePayments.setBalance(deliveryNoteProductCommand.getBalance().concat(",0"));
				deliveryNotePayments.setTotalPayable(deliveryNoteProductCommand.getTotalPayable().concat(",0"));
			}
			//deliveryNotePayments.setBalance(deliveryNoteProductCommand.getBalance());
			deliveryNotePayments.setBankName(deliveryNoteProductCommand.getBankName());
			deliveryNotePayments.setBranchName(deliveryNoteProductCommand.getBranchName());
			deliveryNotePayments.setChequeNo(deliveryNoteProductCommand.getChequeNo());
			deliveryNotePayments.setPaymentType(deliveryNoteProductCommand.getPaymentType());
			deliveryNotePayments.setPresentAdvance(deliveryNoteProductCommand.getPresentAdvance());
			deliveryNotePayments.setPresentPayable(deliveryNoteProductCommand.getPresentPayable());
			//deliveryNotePayments.setPresentPayment(presentPayment.toString());
			deliveryNotePayments.setPreviousCredit(deliveryNoteProductCommand.getPreviousCredit());
			//deliveryNotePayments.setTotalPayable(deliveryNoteProductCommand.getTotalPayable());
			deliveryNotePayments.setVbDeliveryNoteChangeRequest(deliveryNote);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting deliveryNotePayments: {}", deliveryNotePayments);
			}
			session.save(deliveryNotePayments);
			isSaved = Boolean.TRUE;
		}
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> START.
		//updateCustomerCreditInfo(session, command, deliveryNoteProductCommand, organization, userName);
		// Updating temp tables(vb_customer_credit_info and vb_customer_advance_info) ----> END.
		txn.commit();
		return isSaved;
	}
	
	
	/** This method is responsible for getting Delivery Note Cr details from temp tables.
	 * 
	 * @param username
	 * @param organization
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDeliveryNoteResult> getDeliveryNoteCrResults(String username,VbOrganization organization) {
		Session session = this.getSession();
		Date date = new Date();
		List<VbDeliveryNoteChangeRequest> deliveryNoteChangeRequestList = null;
		deliveryNoteChangeRequestList = session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn")).list();
		
		List<ChangeRequestDeliveryNoteResult> deliveryNoteList = new ArrayList<ChangeRequestDeliveryNoteResult>();
		ChangeRequestDeliveryNoteResult deliveryNoteResult = null;
		for (VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest : deliveryNoteChangeRequestList) {
			deliveryNoteResult = new ChangeRequestDeliveryNoteResult();
			deliveryNoteResult.setBusinessName(vbDeliveryNoteChangeRequest.getBusinessName());
			if(vbDeliveryNoteChangeRequest.getInvoiceName().contains(",1")){
			deliveryNoteResult.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",1", ""));
			}else{
				deliveryNoteResult.setInvoiceName(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",0", ""));
			}
			deliveryNoteResult.setInvoiceNo(vbDeliveryNoteChangeRequest.getInvoiceNo());
			deliveryNoteResult.setDate(DateUtils.format(vbDeliveryNoteChangeRequest.getCreatedOn()));
			Set<VbDeliveryNoteChangeRequestPayments> paymentSet = vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestPaymentses();
			for (VbDeliveryNoteChangeRequestPayments payments : paymentSet) {
				if(payments.getBalance().contains(",1")){
				deliveryNoteResult.setBalance(StringUtil.currencyFormat(Float.parseFloat(payments.getBalance().replace(",1", ""))));
				}else{
					deliveryNoteResult.setBalance(StringUtil.currencyFormat(Float.parseFloat(payments.getBalance().replace(",0", ""))));
				}
			}
			deliveryNoteResult.setId(vbDeliveryNoteChangeRequest.getId());
			deliveryNoteList.add(deliveryNoteResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("deliveryNoteList: {}", deliveryNoteList);
		}
		return deliveryNoteList;
	}
	/**
	 * This method is responsible for Get Delivery Note Results List of Current Date.
	 * 
	 * @param organization
	 * @param username
	 * @return deliveryNoteList
	 * @throws ParseException
	 */
	public List<ChangeRequestDeliveryNoteResult> getDeliveryNoteResultsOnCriteria(VbOrganization organization, String username) throws ParseException {
		Session session = this.getSession();
		Date date = new Date();
		Criteria criteria = session.createCriteria(VbDeliveryNote.class);
		criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)));
		criteria.add(Expression.eq("createdBy", username));
		if (organization != null) {
			criteria.add(Expression.eq("vbOrganization", organization));
		}
		criteria.addOrder(Order.desc("createdOn"));
		List<VbDeliveryNote> criteriaList = criteria.list();
		List<ChangeRequestDeliveryNoteResult> deliveryNoteList = new ArrayList<ChangeRequestDeliveryNoteResult>();
		ChangeRequestDeliveryNoteResult deliveryNoteResult = null;
		for (VbDeliveryNote vbDeliveryNote : criteriaList) {
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

		if (_logger.isDebugEnabled()) {
			_logger.debug("deliveryNoteList: {}", deliveryNoteList);
		}
		return deliveryNoteList;
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
		String isAvailable = "y";
		Session session = this.getSession();
		VbDeliveryNoteChangeRequest changeRequest =(VbDeliveryNoteChangeRequest)session.createCriteria(VbDeliveryNoteChangeRequest.class)
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
		if(changeRequest == null){	
			isAvailable = "n";
		}else{
			VbDeliveryNoteChangeRequest changeRequestDayBook =(VbDeliveryNoteChangeRequest)session.createCriteria(VbDeliveryNoteChangeRequest.class)
					.add(Expression.eq("invoiceNo", invoiceNumber))
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("vbOrganization", organization)).uniqueResult();
			if(changeRequestDayBook != null){
				isAvailable = "y";
			}else{
				isAvailable = "n";
			}
		}
		session.close();
		return isAvailable;
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
						+ "WHERE vbdn.vbOrganization = :vbOrganization)")
				.setParameter("vbOrganization", organization);
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
			_logger.debug("Generated delivery note invoiceNo: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo;
	}
	/**
	 * This method is used to get the Delivery Note Based on Sales Id.
	 * 
	 * @param deliveryNoteId
	 * @param organization
	 * @param userName
	 * @return vbDeliveryNote
	 */
	@SuppressWarnings("unchecked")
	public VbDeliveryNote getDeliveryNote(Integer deliveryNoteId,VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbDeliveryNote vbDeliveryNote = (VbDeliveryNote) session.get(VbDeliveryNote.class, deliveryNoteId);
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbDeliveryNote: {}", vbDeliveryNote);
		}
		return vbDeliveryNote;
	}
	
	/**
	 * This method is used to get the data for existed Business Name from VbDelivery Note Products.
	 * 
	 * @return List<ProductResult>
	 */
	public List<ChangeRequestProductResult> getExistedBusinessNameGridData(Integer salesId,Integer deliveryNoteId,String salesExecutive,String businessName, VbOrganization organization) {
		List<ChangeRequestProductResult> productResultList = null;
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		List<VbDeliveryNoteProducts> productList = session
				.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "vb")
				.add(Expression.eq("vb.businessName", businessName))
				.add(Expression.eq("vb.id", deliveryNoteId))
				.add(Expression.eq("vb.vbOrganization", organization))
				.addOrder(Order.asc("productName")).list();
		ChangeRequestProductResult productResult = null;
		productResultList = new ArrayList<ChangeRequestProductResult>();
		Float grandTotal = new Float(0);
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : productList) {
			productResult = new ChangeRequestProductResult();
			productResult.setProductName(vbDeliveryNoteProducts.getProductName());
			productResult.setBatchNumber(vbDeliveryNoteProducts.getBatchNumber());
			productResult.setProductCost(StringUtil.currencyFormat(vbDeliveryNoteProducts.getProductCost()));
			productResult.setProductQty(StringUtil.format(vbDeliveryNoteProducts.getProductQty()));
			productResult.setTotalCost(StringUtil.currencyFormat(vbDeliveryNoteProducts.getTotalCost()));
			productResult.setBonus(vbDeliveryNoteProducts.getBonusQty());
			productResult.setBonusReason(StringUtil.format(vbDeliveryNoteProducts.getBonusReason()));
            @SuppressWarnings("unchecked")
			List<VbSalesBookProducts> SalesBookProductsList = session
    				.createCriteria(VbSalesBookProducts.class)
    				.createAlias("vbSalesBook", "vb")
    				.add(Expression.eq("vb.id", salesId))
    				.add(Expression.eq("productName", vbDeliveryNoteProducts.getProductName()))
    				.add(Expression.eq("batchNumber", vbDeliveryNoteProducts.getBatchNumber()))
    				.add(Expression.eq("vb.vbOrganization", organization)).list();
            for (VbSalesBookProducts vbSalesBookProducts : SalesBookProductsList) {
            	productResult.setAvailableQuantity(StringUtil.quantityFormat(vbSalesBookProducts.getQtyAllotted()));
            }
			productResultList.add(productResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("productResultList: {}", productResultList);
		}

		return productResultList;
	}
	
	/**
	 * This method is responsible to get the Grand Total based on the
	 * delivery Note Id.
	 * 
	 * @param deliveryNoteId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return grandTotal - {@link Float}
	 * 
	 */
	public Float getGrandTotal(Integer deliveryNoteId,VbOrganization organization) {
		Session session = this.getSession();
		@SuppressWarnings("unchecked")
		Float grandTotal = new Float(0);
		@SuppressWarnings("unchecked")
		List<VbDeliveryNoteProducts> productList = session
				.createCriteria(VbDeliveryNoteProducts.class)
				.createAlias("vbDeliveryNote", "vb")
				.add(Expression.eq("vb.id", deliveryNoteId))
				.add(Expression.eq("vb.vbOrganization", organization))
				.addOrder(Order.asc("productName")).list();
		for (VbDeliveryNoteProducts vbDeliveryNoteProducts : productList) {
			grandTotal += vbDeliveryNoteProducts.getTotalCost();
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Grand Total is : {}", grandTotal);
		}
		return grandTotal;
	}
	
	/**
	 * This method is used to get the Delivery Note Change Request Based on Delivery Note CR Id.
	 * 
	 * @param deliveryNoteCRId
	 * @param organization
	 * @return vbDeliveryNote
	 */
	@SuppressWarnings("unchecked")
	public VbDeliveryNoteChangeRequest getDeliveryNoteChangeRequest(Integer deliveryNoteCRId,VbOrganization organization) {
		Session session = this.getSession();
		VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, deliveryNoteCRId);
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbDeliveryNoteChangeRequest: {}", vbDeliveryNoteChangeRequest);
		}
		return vbDeliveryNoteChangeRequest;
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
		return employee;
	}
	
	/**
	 * This method is responsible for Approving Delivery Note CR.
	 * 
	 * @param deliverNoteCRId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 */
	public void getApprovedDeliveyNoteCR(Integer deliverNoteCRId, String status, VbOrganization vbOrganization, String userName) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, deliverNoteCRId); 
		if(vbDeliveryNoteChangeRequest!=null){
			vbDeliveryNoteChangeRequest.setModifiedBy(userName);
			vbDeliveryNoteChangeRequest.setModifiedOn(new Date());
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				vbDeliveryNoteChangeRequest.setStatus(CRStatus.APPROVED.name());
			} else {
				vbDeliveryNoteChangeRequest.setStatus(CRStatus.DECLINE.name());
			}
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating vbDeliveryNoteChangeRequest");
			}
			session.update(vbDeliveryNoteChangeRequest);
		}
		txn.commit();
		session.close();
}
	
	/**
	 * This method is responsible to get {@link VbDeliveryNote} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName -{@link String}
	 * @param invoiceNumber -{@link String}
	 * @return deliveryNoteId -{@link Integer}
	 * 
	 */
	public Integer getDeliveryNoteBasedOnInvoiceNo(VbOrganization organization,String invoiceNumber,String userName) {
		Session session=this.getSession();
		Integer deliveryNoteId=0;
		VbDeliveryNote vbDeliveryNote=null;
		 vbDeliveryNote = (VbDeliveryNote) session
				.createCriteria(VbDeliveryNote.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.uniqueResult();
		 if(vbDeliveryNote!=null){
		     deliveryNoteId=vbDeliveryNote.getId();
		 }
		 session.close();
		return deliveryNoteId;
	}
	/**
	 * This method is responsible to get {@link VbDeliveryNote} based on
	 * {@link VbOrganization} and invoiceNo and deliveryNoteId.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param deliveryNoteId -{@link Integer}
	 * @param invoiceNumber -{@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * @throws ParseException 
	 * 
	 */
	public String fetchDeliveryNoteCreationTime(Integer deliveryNoteId,
			String invoiceNumber, VbOrganization organization) throws ParseException {
		Session session=this.getSession();
		String isDeliveryNoteCRExpired="";
		Date deliveryNoteCreationTime;
		Date currentDateTime=new Date();
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime=format.parse(d2);
		VbDeliveryNote vbDeliveryNote=null;
		 vbDeliveryNote = (VbDeliveryNote) session
				.createCriteria(VbDeliveryNote.class)
				.add(Expression.eq("id", deliveryNoteId))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		 if(vbDeliveryNote!=null){
			 deliveryNoteCreationTime=vbDeliveryNote.getCreatedOn();
			 String creationTime= DateUtils.formatDateWithTimestamp(deliveryNoteCreationTime);
			 Date dnCreation=format.parse(creationTime);
			 Integer totalMin=calculateMinDiff(currentTime,dnCreation);
			 if(totalMin > 10){
				 isDeliveryNoteCRExpired="y";
			 }else{
				 isDeliveryNoteCRExpired="n";
			 }
		 }
		 session.close();
		return isDeliveryNoteCRExpired;
	}
	
	/**This method is responsible for calculate minutes difference between Sales creation time and Transaction CR access time.
	 *  
	 * @param currentTime -{@link Date}
	 * @param deliveryNoteCreationTime -{@link Date}
	 * @return totalMinutes -{@link Integer}
	 */
	public Integer calculateMinDiff(Date currentTime,Date deliveryNoteCreationTime){
		//in milliseconds
		long minsDiff=0;
		minsDiff = currentTime.getTime() - deliveryNoteCreationTime.getTime();
	 int minDiff= (int) minsDiff / (60 * 1000) % 60;
	 int hourDiff= (int) minsDiff / (60 * 60 * 1000) % 24;
	 int secondsDiff= (int) minsDiff / 1000 % 60;;
	 int convertHourToMinutes=(int) Math.round(hourDiff * 60);
	 int convertSecondsToMinutes=(int) Math.round(secondsDiff / 60);
	 int totalMinutes=minDiff+convertHourToMinutes+convertSecondsToMinutes;
	 //System.out.println("Minutes Diff:"+totalMinutes);
	 return totalMinutes;
	}
	//end of delivery note
	
	//start of Sales Return
	
	/**
	 * This method is responsible to persist the sales returns change request data.
	 * 
	 * @param salesReturnCommands - {@link List}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isSaved - {@link Boolean}
	 * 
	 */
	public synchronized Boolean saveSalesReturns(List<ChangeRequestSalesReturnCommand> salesReturnCommands,String[] changedFormFields,
			VbOrganization organization, String userName) {
		Boolean isSaved = Boolean.FALSE;
		Boolean emptyFormChangedValuesArray=true;
		Session session = this.getSession();
		ArrayListMultimap<String,String> changedFormFieldsMap = ArrayListMultimap.create();
		if(changedFormFields.length == 0){
			emptyFormChangedValuesArray=true;
		}else{
			emptyFormChangedValuesArray=false;
		for(final String s : changedFormFields) {
			   final String split[] = s.split(":");
			   changedFormFieldsMap.put(split[0], split[1]);
			}
		}
		VbSalesReturnChangeRequest instanceSalesReturn = new VbSalesReturnChangeRequest();
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
			List<String> damagedQuantity=changedFormFieldsMap.get("damaged");;
			List<String> resalableQuantity=resalableQuantity=changedFormFieldsMap.get("resalable");
			for (ChangeRequestSalesReturnCommand command : salesReturnCommands) {
				Transaction txn = session.beginTransaction();
				businessName = command.getBusinessName();
				List<String> customerInvoiceName = changedFormFieldsMap.get("invoiceName");
				if(customerInvoiceName.size()!=0){
				for(String customerInvoiceName1 : customerInvoiceName) {
					if(customerInvoiceName1.equals(command.getInvoiceName())){
						invoiceName = command.getInvoiceName();
						instanceSalesReturn.setInvoiceName(invoiceName.concat(",1"));
					}
				}
				}else{
				invoiceName = command.getInvoiceName();
				instanceSalesReturn.setInvoiceName(invoiceName.concat(",0"));
				}
				instanceSalesReturn.setCrDescription(command.getCrDescription());
				instanceSalesReturn.setBusinessName(businessName);
				instanceSalesReturn.setInvoiceNo(command.getInvoiceNo());
				if(damagedQuantity.size()!=0 || resalableQuantity.size()!=0){
				    	instanceSalesReturn.setProductsGrandTotal(command.getGrandTotalCost().concat(",1"));
				}else{
				instanceSalesReturn.setProductsGrandTotal(command.getGrandTotalCost().concat(",0"));
				}
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbSalesReturnChangeRequest: {}", instanceSalesReturn);
				}
				session.save(instanceSalesReturn);
				isSaved = Boolean.TRUE;

				String productName = command.getProductName();
				String batchNumber = command.getBatchNumber();
				VbSalesReturnChangeRequestProducts instanceReturnProducts = new VbSalesReturnChangeRequestProducts();
				if (instanceReturnProducts != null) {
					if(damagedQuantity.size()!=0 || resalableQuantity.size()!=0){
					for(String damaged : damagedQuantity) {
					if(damaged.equals(command.getDamaged())){
						instanceReturnProducts.setDamaged(command.getDamaged().concat(",1"));
						instanceReturnProducts.setResalable(command.getResalable().concat(",0"));
						instanceReturnProducts.setTotalQty(command.getTotalQty().concat(",1"));
						instanceReturnProducts.setTotalCost(command.getTotalCost().concat(",1"));
					  }
					}
					for(String resalable : resalableQuantity){
						if(resalable.equals(command.getResalable())){
							instanceReturnProducts.setResalable(command.getResalable().concat(",1"));
							instanceReturnProducts.setDamaged(command.getDamaged().concat(",0"));
							instanceReturnProducts.setTotalQty(command.getTotalQty().concat(",1"));
							instanceReturnProducts.setTotalCost(command.getTotalCost().concat(",1"));
						}
					}
				}
					else{
					instanceReturnProducts.setDamaged(command.getDamaged().concat(",0"));
					instanceReturnProducts.setResalable(command.getResalable().concat(",0"));
					instanceReturnProducts.setTotalQty(command.getTotalQty().concat(",0"));
					instanceReturnProducts.setTotalCost(command.getTotalCost().concat(",0"));
					}
					instanceReturnProducts.setCost(command.getCost().concat(",0"));
					instanceReturnProducts.setProductName(productName.concat(",0"));
					instanceReturnProducts.setBatchNumber(batchNumber.concat(",0"));
					instanceReturnProducts.setVbSalesReturnChangeRequest(instanceSalesReturn);
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
	 * This Method is Responsible to Check whether The Searched result is
	 * Applicable for SE CR or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @return isAvailable - {@link String}
	 */
	public String validateSESalesReturnChangeRequest(String invoiceNumber,
			VbOrganization organization) {
		String isAvailable = "y";
		Session session = this.getSession();
		VbSalesReturnChangeRequest changeRequest =(VbSalesReturnChangeRequest)session.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
		if(changeRequest == null){	
			isAvailable = "n";
		}else{
			VbSalesReturnChangeRequest changeRequestDayBook =(VbSalesReturnChangeRequest)session.createCriteria(VbSalesReturnChangeRequest.class)
					.add(Expression.eq("invoiceNo", invoiceNumber))
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("vbOrganization", organization)).uniqueResult();
			if(changeRequestDayBook != null){
				isAvailable = "y";
			}else{
				isAvailable = "n";
			}
		}
		session.close();
		return isAvailable;
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
	public List<ChangeRequestSalesReturnResult> getSalesReturnsOnLoad(String username,
			VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbSalesBook salesBook = getSalesBook(session, vbOrganization, username);
		List<VbSalesReturn> salesReturnsList = null;
		Date date = new Date();
		if (salesBook != null) {
			salesReturnsList = session.createCriteria(VbSalesReturn.class)
					.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
					.add(Expression.eq("createdBy", username))
					/*.add(Expression.eq("vbSalesBook", salesBook))*/
					.add(Expression.eq("vbOrganization", vbOrganization))
					.addOrder(Order.desc("createdOn")).list();
		}
		List<ChangeRequestSalesReturnResult> salesResultList = null;
		if (salesReturnsList != null) {
			salesResultList = new ArrayList<ChangeRequestSalesReturnResult>();
			ChangeRequestSalesReturnResult result = null;
			for (VbSalesReturn salesReturn : salesReturnsList) {
				result = new ChangeRequestSalesReturnResult();
				result.setCreatedBy(salesReturn.getCreatedBy());
				result.setDate(DateUtils.format(salesReturn.getCreatedOn()));
				Float total = getSalesReturnProducts(salesReturn.getId(), vbOrganization, username);
				result.setTotal(StringUtil.floatFormat(total));
				result.setId(salesReturn.getId());
				result.setBusinessName(salesReturn.getBusinessName());
				result.setInvoiceNo(salesReturn.getInvoiceNo());
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
						"FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization AND vb.salesExecutive = :salesExecutive AND "
								+ "vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE vbs.salesExecutive = :salesExecutiveName)")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutive", userName)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
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
					.add(Expression.eq("salesReturn.vbOrganization", organization)).uniqueResult();
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}

	/**
	 * This method is used to get the Sales Return Based on Sales Return Id.
	 * 
	 * @param SalesReturnId  - {@link Integer}
	 * @param organization  - {@link VbOrganization}
	 * @param userName  - {@link String}
	 * @return vbSalesReturn - {@link vbSalesReturn}
	 */
	public VbSalesReturn getSalesReturn(Integer SalesReturnId,VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbSalesReturn vbSalesReturn = (VbSalesReturn) session.get(VbSalesReturn.class, SalesReturnId);
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbSalesReturn: {}", vbSalesReturn);
		}
		return vbSalesReturn;
	}
	
	/**
	 * This method is responsible to provide the data for sales returns grid.
	 * 
	 * @param businessName - {@link String}
	 * @return salesReturnsResultList - {@link List}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestSalesReturnsResult> getSalesReturnGridData(Integer salesReturnId,String businessName,String userName, VbOrganization organization) {
		Session session = this.getSession();
		ChangeRequestSalesReturnsResult result = null;
		Float productCost;
		String productName = null;
		List<VbSalesReturnProducts> vbSalesReturnProducts = session
				.createCriteria(VbSalesReturnProducts.class)
				.createAlias("vbSalesReturn", "vb")
				/*.add(Expression.eq("vb.businessName", businessName))*/
				.add(Expression.eq("vb.id", salesReturnId))
				.add(Expression.eq("vb.vbOrganization", organization))
				.addOrder(Order.asc("productName")).list();
        
		List<ChangeRequestSalesReturnsResult> salesReturnsResultList = null;
		
		if (vbSalesReturnProducts != null) {
			salesReturnsResultList = new ArrayList<ChangeRequestSalesReturnsResult>();
			for (VbSalesReturnProducts product : vbSalesReturnProducts) {
				result = new ChangeRequestSalesReturnsResult();
				result.setProductName(product.getProductName());
				result.setBatchNumber(product.getBatchNumber());
				result.setdamagedQty(StringUtil.format(product.getDamaged()));
				result.setResaleableQty(StringUtil.format(product.getResalable()));
				result.setTotalQty(StringUtil.format(product.getTotalQty()));
				result.setCost(StringUtil.currencyFormat(product.getCost()));
				result.setTotalCost(StringUtil.currencyFormat(product.getTotalCost()));
				salesReturnsResultList.add(result);
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("salesReturnsResultList: {}", salesReturnsResultList);
		}
		return salesReturnsResultList;
	}
	@SuppressWarnings("unchecked")
	public List<ChangeRequestSalesReturnResult> getSalesReturnCrResults(String username,VbOrganization organization) {
		Session session = this.getSession();
		List<VbSalesReturnChangeRequest> salesReturnsCrList = null;
		Date date = new Date();
		salesReturnsCrList = session.createCriteria(VbSalesReturnChangeRequest.class)
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("vbOrganization", organization))
					.addOrder(Order.asc("createdOn")).list();

		List<ChangeRequestSalesReturnResult> salesResultList = null;
		if (salesReturnsCrList != null) {
			salesResultList = new ArrayList<ChangeRequestSalesReturnResult>();
			ChangeRequestSalesReturnResult result = null;
			for (VbSalesReturnChangeRequest salesReturnCr : salesReturnsCrList) {
				result = new ChangeRequestSalesReturnResult();
				result.setCreatedBy(salesReturnCr.getCreatedBy());
				result.setDate(DateUtils.format(salesReturnCr.getCreatedOn()));
				String total = getSalesReturnProductsCr(salesReturnCr.getId(), organization, username);
				result.setTotal(StringUtil.floatFormat(Float.parseFloat(total)));
				result.setId(salesReturnCr.getId());
				result.setBusinessName(salesReturnCr.getBusinessName());
				result.setInvoiceNo(salesReturnCr.getInvoiceNo());
				result.setInvoiceName(salesReturnCr.getInvoiceName());
				salesResultList.add(result);
			}
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("salesResultList: {}", salesResultList);
		}
		return salesResultList;

	}
	/**
	 * This method is responsible to retrieve sum of total cost of provided
	 * salesReturnId {@link VbSalesReturnChangeRequestProducts} from DB.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @return sumTotal - {@link Float}
	 * 
	 */
	public String getSalesReturnProductsCr(Integer salesReturnId,
			VbOrganization organization, String userName) {
		Session session = this.getSession();
		String sumTotal=null;
			sumTotal = (String)session.createCriteria(VbSalesReturnChangeRequestProducts.class)
					.createAlias("vbSalesReturnChangeRequest", "vbSalesReturnCr")
					.setProjection(Projections.sum("totalCost"))
					.add(Expression.eq("vbSalesReturnCr.id", salesReturnId))
					.add(Expression.eq("vbSalesReturnCr.vbOrganization", organization)).uniqueResult();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("sumTotal: {}", sumTotal);
		}
		return sumTotal;
	}
	
	/**
	 * This method is responsible to retrieve sales returns CR based on Sales Return ID
	 * {@link VbSalesReturnChangeRequest} from DB.
	 * 
	 * @param salesReturnCRId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return vbSalesReturn - {@link VbSalesReturn}
	 * 
	 */
	public VbSalesReturnChangeRequest getSalesReturnProductsDetails(Integer salesReturnCRId,
			VbOrganization organization) {
		Session session = this.getSession();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session
				.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Expression.eq("id", salesReturnCRId))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbSalesReturnChangeRequest: {}", vbSalesReturnChangeRequest);
		}
		return vbSalesReturnChangeRequest;

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
	public VbEmployee getSalesExecutiveFullNameSalesReturn(String userName,VbOrganization organization) {
		Session session=this.getSession();
		VbEmployee employee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("username", userName))
				.uniqueResult();
		return employee;
	}
	/**
	 * This method is responsible for Approving Sales Return CR.
	 * 
	 * @param salesReturnId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 */
	public void getApprovedSalesReturnCR(Integer salesReturnId, String status, VbOrganization vbOrganization, String userName) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session.get(VbSalesReturnChangeRequest.class, salesReturnId);
		if(vbSalesReturnChangeRequest!=null){
			vbSalesReturnChangeRequest.setModifiedBy(userName);
			vbSalesReturnChangeRequest.setModifiedOn(new Date());
			
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				vbSalesReturnChangeRequest.setStatus(CRStatus.APPROVED.name());
			} else {
				vbSalesReturnChangeRequest.setStatus(CRStatus.DECLINE.name());
			}
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating vbSalesReturnChangeRequest for approved.");
			}
			session.update(vbSalesReturnChangeRequest);
		}
		txn.commit();
		session.close();
	}
	/**
	 * This method is responsible to get {@link VbSalesReturn} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName -{@link String}
	 * @param invoiceNumber -{@link String}
	 * @return deliveryNoteId -{@link Integer}
	 * 
	 */
	public Integer getSalesReturnBasedOnInvoiceNo(VbOrganization organization,String invoiceNumber,String userName) {
		Session session=this.getSession();
		Integer salesReturnId=0;
		VbSalesReturn vbSalesReturn=null;
		vbSalesReturn = (VbSalesReturn) session
				.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.uniqueResult();
		 if(vbSalesReturn!=null){
			 salesReturnId=vbSalesReturn.getId();
		 }
		return salesReturnId;
	}
	
	/**
	 * This method is responsible to get {@link VbSalesReturn} based on
	 * {@link VbOrganization} and invoiceNo and salesReturnId.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param salesReturnId -{@link Integer}
	 * @param invoiceNumber -{@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * @throws ParseException 
	 * 
	 */
	public String fetchSalesReturnCreationTime(Integer salesReturnId,
			String invoiceNumber, VbOrganization organization) throws ParseException {
		Session session=this.getSession();
		String isSalesReturnCRExpired="";
		Date salesReturnCreationTime;
		Date currentDateTime=new Date();
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime=format.parse(d2);
		VbSalesReturn vbSalesReturn=null;
		vbSalesReturn = (VbSalesReturn) session
				.createCriteria(VbSalesReturn.class)
				.add(Expression.eq("id", salesReturnId))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		 if(vbSalesReturn!=null){
			 salesReturnCreationTime=vbSalesReturn.getCreatedOn();
			 String creationTime= DateUtils.formatDateWithTimestamp(salesReturnCreationTime);
			 Date salesCreation=format.parse(creationTime);
			 Integer totalMin=calculateMinDiff(currentTime,salesCreation);
			 if(totalMin > 10){
				 isSalesReturnCRExpired="y";
			 }else{
				 isSalesReturnCRExpired="n";
			 }
		 }
		 session.close();
		return isSalesReturnCRExpired;
	}
	
	//end of Sales Return
	
	//start of day book
	
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
	public synchronized Boolean saveDayBook(ChangeRequestDayBookBasicInfoCommand dayBookBasicInfoCommand,
			ChangeRequestDayBookAllowancesCommand dayBookAllowancesCommand, ChangeRequestDayBookAmountCommand dayBookAmountCommand,
			List<ChangeRequestDayBookProductsCommand> dayBookProductsCommand, ChangeRequestDayBookVehicleDetailsCommand dayBookVehicleCommand,
			String[] dayBookFormChangedDetails,VbOrganization organization, String userName, String isReturn) {
		Boolean isSaved = Boolean.FALSE;
		VbDayBookChangeRequestProducts vbDayBookChangeRequestProducts = null;
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
		ArrayListMultimap<String,String> changedFormFieldsMap = ArrayListMultimap.create();
		for(final String s : dayBookFormChangedDetails) {
			   final String split[] = s.split(":");
			   changedFormFieldsMap.put(split[0], split[1]);
			}
		VbDayBookChangeRequest vbDayBookChangeRequest = new VbDayBookChangeRequest();
		if(vbSalesBook != null) {
			vbDayBookChangeRequest.setVbSalesBook(vbSalesBook);
		}
		if (vbDayBookChangeRequest != null) {
			vbDayBookChangeRequest.setCreatedBy(userName);
			vbDayBookChangeRequest.setCreatedOn(date);
			vbDayBookChangeRequest.setModifiedOn(date);
			vbDayBookChangeRequest.setSalesExecutive(userName);
			vbDayBookChangeRequest.setCrDescription(dayBookVehicleCommand.getDescription());
			if(returnToFactory != null) {
				vbDayBookChangeRequest.setIsReturn(returnToFactory);
			} else {
				vbDayBookChangeRequest.setIsReturn(Boolean.FALSE);
			}
			List<String> vehicleNumber = changedFormFieldsMap.get("vehicleNo");
			if(vehicleNumber.size()!=0){
			for(String vehicleNumber1 : vehicleNumber) {
				if(vehicleNumber1.equals(dayBookVehicleCommand.getVehicleNo())){
					vbDayBookChangeRequest.setVehicleNo(dayBookVehicleCommand.getVehicleNo().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequest.setVehicleNo(dayBookVehicleCommand.getVehicleNo().concat(",0"));
			}
			
			List<String> driverName = changedFormFieldsMap.get("driverName");
			if(driverName.size()!=0){
			for(String driverName1 : driverName) {
				if(driverName1.equals(dayBookVehicleCommand.getDriverName())){
					vbDayBookChangeRequest.setDriverName(dayBookVehicleCommand.getDriverName().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequest.setDriverName(dayBookVehicleCommand.getDriverName().concat(",0"));
			}
			
			List<String> endingReading = changedFormFieldsMap.get("endingReading");
			if(endingReading.size()!=0){
			for(String endingReading1 : endingReading) {
				if(endingReading1.equals(dayBookVehicleCommand.getEndingReading())){
					vbDayBookChangeRequest.setEndingReading(dayBookVehicleCommand.getEndingReading().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequest.setEndingReading(dayBookVehicleCommand.getEndingReading().concat(",0"));
			}
			
			List<String> startReading = changedFormFieldsMap.get("startReading");
			if(startReading.size()!=0){
			for(String startReading1 : startReading) {
				if(startReading1.equals(dayBookVehicleCommand.getStartingReading())){
					vbDayBookChangeRequest.setStartingReading(dayBookVehicleCommand.getStartingReading().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequest.setStartingReading(dayBookVehicleCommand.getStartingReading().concat(",0"));
			}
			
			List<String> remarks = changedFormFieldsMap.get("remarks");
			if(remarks.size()!=0){
			for(String remarks1 : remarks) {
				if(remarks1.equals(dayBookVehicleCommand.getRemarks())){
					vbDayBookChangeRequest.setRemarks(dayBookVehicleCommand.getRemarks().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequest.setRemarks(dayBookVehicleCommand.getRemarks().concat(",0"));
			}
			vbDayBookChangeRequest.setVbOrganization(organization);
			vbDayBookChangeRequest.setStatus(CRStatus.PENDING.name());
			//vbDayBookChangeRequest.setVbSalesBook(vbSalesBook);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbDayBookChangeRequest: {}", vbDayBookChangeRequest);
			}
			session.save(vbDayBookChangeRequest);
			isSaved = Boolean.TRUE;
		}
		VbDayBookChangeRequestAmount vbDayBookChangeRequestAmount = new VbDayBookChangeRequestAmount();
		if (vbDayBookChangeRequestAmount != null) {
		List<String> amountToBank = changedFormFieldsMap.get("amountToBank");
		if(amountToBank.size()!=0){
		for(String amountToBank1 : amountToBank) {
			if(amountToBank1.equals(dayBookAmountCommand.getAmountToBank())){
				vbDayBookChangeRequestAmount.setAmountToBank(dayBookAmountCommand.getAmountToBank().concat(",1"));
				vbDayBookChangeRequestAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance().concat(",1"));
			}
		}
		}else{
			vbDayBookChangeRequestAmount.setAmountToBank(dayBookAmountCommand.getAmountToBank().concat(",0"));
			vbDayBookChangeRequestAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance().concat(",0"));
		}

		List<String> amountToFactory = changedFormFieldsMap.get("amountToFactory");
		if(amountToFactory.size()!=0){
		for(String amountToFactory1 : amountToFactory) {
			if(amountToFactory1.equals(dayBookAmountCommand.getAmountToFactory())){
				vbDayBookChangeRequestAmount.setAmountToFactory(dayBookAmountCommand.getAmountToFactory().concat(",1"));
				vbDayBookChangeRequestAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance().concat(",1"));
			}
		}
		}else{
			vbDayBookChangeRequestAmount.setAmountToFactory(dayBookAmountCommand.getAmountToFactory().concat(",0"));
			vbDayBookChangeRequestAmount.setClosingBalance(dayBookAmountCommand.getClosingBalance().concat(",0"));
		}
		
		List<String> reasonAmountToBank = changedFormFieldsMap.get("reasonAmountToBank");
		if(reasonAmountToBank.size()!=0){
		for(String reasonAmountToBank1 : reasonAmountToBank) {
			if(reasonAmountToBank1.equals(dayBookAmountCommand.getReasonAmountToBank())){
				vbDayBookChangeRequestAmount.setReasonAmountToBank(dayBookAmountCommand.getReasonAmountToBank().concat(",1"));
			}
		}
		}else{
			vbDayBookChangeRequestAmount.setReasonAmountToBank(dayBookAmountCommand.getReasonAmountToBank().concat(",0"));
		}
			vbDayBookChangeRequestAmount.setCustomerTotalCredit(dayBookAmountCommand.getCustomerTotalCredit());
			vbDayBookChangeRequestAmount.setCustomerTotalPayable(dayBookAmountCommand.getCustomerTotalPayable());
			vbDayBookChangeRequestAmount.setCustomerTotalReceived(dayBookAmountCommand.getCustomerTotalReceived());
			
			//Day book Allowances
			List<String> executiveAllowances = changedFormFieldsMap.get("executiveAllowances");
			if(executiveAllowances.size()!=0){
			for(String executiveAllowances1 : executiveAllowances) {
				if(executiveAllowances1.equals(dayBookAllowancesCommand.getExecutiveAllowances())){
					vbDayBookChangeRequestAmount.setExecutiveAllowances(dayBookAllowancesCommand.getExecutiveAllowances().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setExecutiveAllowances(dayBookAllowancesCommand.getExecutiveAllowances().concat(",0"));
			}
			
			List<String> driverAllowances = changedFormFieldsMap.get("driverAllowances");
			if(driverAllowances.size()!=0){
			for(String driverAllowances1 : driverAllowances) {
				if(changedFormFieldsMap.get("driverAllowances").equals(dayBookAllowancesCommand.getDriverAllowances())){
					vbDayBookChangeRequestAmount.setDriverAllowances(dayBookAllowancesCommand.getDriverAllowances().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setDriverAllowances(dayBookAllowancesCommand.getDriverAllowances().concat(",0"));
			}
			
			List<String> vehicleFuelExpenses = changedFormFieldsMap.get("vehicleFuelExpenses");
			if(vehicleFuelExpenses.size()!=0){
			for(String vehicleFuelExpenses1 : vehicleFuelExpenses) {
				if(vehicleFuelExpenses1.equals(dayBookAllowancesCommand.getVehicleFuelExpenses())){
					vbDayBookChangeRequestAmount.setVehicleFuelExpenses(dayBookAllowancesCommand.getVehicleFuelExpenses().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setVehicleFuelExpenses(dayBookAllowancesCommand.getVehicleFuelExpenses().concat(",0"));
			}
			List<String> offloadingLoadingCharges = changedFormFieldsMap.get("offloadingLoadingCharges");
			if(offloadingLoadingCharges.size()!=0){
			for(String offloadingLoadingCharges1 : offloadingLoadingCharges) {
				if(offloadingLoadingCharges1.equals(dayBookAllowancesCommand.getOffloadingLoadingCharges())){
					vbDayBookChangeRequestAmount.setOffloadingLoadingCharges(dayBookAllowancesCommand.getOffloadingLoadingCharges().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setOffloadingLoadingCharges(dayBookAllowancesCommand.getOffloadingLoadingCharges().concat(",0"));
			}
			
			List<String> vehicleMaintenanceExpenses = changedFormFieldsMap.get("vehicleMaintenanceExpenses");
			if(vehicleMaintenanceExpenses.size()!=0){
			for(String vehicleMaintenanceExpenses1 : vehicleMaintenanceExpenses) {
				if(vehicleMaintenanceExpenses1.equals(dayBookAllowancesCommand.getVehicleMaintenanceExpenses())){
					vbDayBookChangeRequestAmount.setVehicleMaintenanceExpenses(dayBookAllowancesCommand.getVehicleMaintenanceExpenses().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setVehicleMaintenanceExpenses(dayBookAllowancesCommand.getVehicleMaintenanceExpenses().concat(",0"));
			}
			
			List<String> miscellaneousExpenses = changedFormFieldsMap.get("miscellaneousExpenses");
			if(miscellaneousExpenses.size()!=0){
			for(String miscellaneousExpenses1 : miscellaneousExpenses) {
				if(changedFormFieldsMap.get("miscellaneousExpenses").equals(dayBookAllowancesCommand.getMiscellaneousExpenses())){
					vbDayBookChangeRequestAmount.setMiscellaneousExpenses(dayBookAllowancesCommand.getMiscellaneousExpenses().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setMiscellaneousExpenses(dayBookAllowancesCommand.getMiscellaneousExpenses().concat(",0"));
			}
			
			List<String> reasonMiscellaneousExpenses = changedFormFieldsMap.get("reasonMiscellaneousExpenses");
			if(reasonMiscellaneousExpenses.size()!=0){
			for(String reasonMiscellaneousExpenses1 : reasonMiscellaneousExpenses) {
				if(reasonMiscellaneousExpenses1.equals(dayBookAllowancesCommand.getReasonMiscellaneousExpenses())){
					vbDayBookChangeRequestAmount.setReasonMiscellaneousExpenses(dayBookAllowancesCommand.getReasonMiscellaneousExpenses().concat(",1"));
				}
			}
			}else{
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
				vbDayBookChangeRequestAmount.setReasonMiscellaneousExpenses(dayBookAllowancesCommand.getReasonMiscellaneousExpenses().concat(",0"));
			}
			
			List<String> dealerPartyExpenses = changedFormFieldsMap.get("dealerPartyExpenses");
			if(dealerPartyExpenses.size()!=0){
			for(String dealerPartyExpenses1 : dealerPartyExpenses) {
				if(dealerPartyExpenses1.equals(dayBookAllowancesCommand.getDealerPartyExpenses())){
					vbDayBookChangeRequestAmount.setDealerPartyExpenses(dayBookAllowancesCommand.getDealerPartyExpenses().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}
			else{
				vbDayBookChangeRequestAmount.setDealerPartyExpenses(dayBookAllowancesCommand.getDealerPartyExpenses().concat(",0"));
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
			}
			
			List<String> municipalCityCouncil = changedFormFieldsMap.get("municipalCityCouncil");
			if(municipalCityCouncil.size()!=0){
			for(String municipalCityCouncil1 : municipalCityCouncil) {
				if(changedFormFieldsMap.get("municipalCityCouncil").equals(dayBookAllowancesCommand.getMunicipalCityCouncil())){
					vbDayBookChangeRequestAmount.setMunicipalCityCouncil(dayBookAllowancesCommand.getMunicipalCityCouncil().concat(",1"));
					vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",1"));
				}
			}
			}
			else{
				vbDayBookChangeRequestAmount.setMunicipalCityCouncil(dayBookAllowancesCommand.getMunicipalCityCouncil().concat(",0"));
				vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));
			}
			vbDayBookChangeRequestAmount.setVbDayBookChangeRequest(vbDayBookChangeRequest);
			//vbDayBookChangeRequestAmount.setTotalAllowances(dayBookAllowancesCommand.getTotalAllowances().concat(",0"));

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbDayBookChangeRequestAmount: {}", vbDayBookChangeRequestAmount);
			}
			session.save(vbDayBookChangeRequestAmount);
			isSaved = Boolean.TRUE;
		}
		if (dayBookProductsCommand != null) {
			String productName = null;
			String batchNumber = null;
			String qtyClosingBalance;
			String qtyOpeningBalance;
			VbSalesBookProducts salesBookProducts = null;
			VbDayBookChangeRequestProducts VbDayBookChangeRequestProducts=null;
			List<String> changedProductsToFactory=changedFormFieldsMap.get("productsToFactory");
			for (ChangeRequestDayBookProductsCommand products : dayBookProductsCommand) {
				vbDayBookChangeRequestProducts = new VbDayBookChangeRequestProducts();
				if(changedProductsToFactory.size()!=0) {
				for(String changeProductToFactory : changedProductsToFactory) {
				    			if(changeProductToFactory.equals(products.getProductsToFactory())){
				    				vbDayBookChangeRequestProducts.setProductsToFactory(products.getProductsToFactory().concat(",1"));
				    				qtyClosingBalance = products.getClosingStock();
				    				vbDayBookChangeRequestProducts.setClosingStock(qtyClosingBalance.concat(",1"));
				    			}
				            }
				    		}else{
				    			productName = products.getProductName();
								batchNumber = products.getBatchNumber();
								qtyClosingBalance = products.getClosingStock();
								qtyOpeningBalance = products.getOpeningStock();
								if (vbDayBookChangeRequestProducts != null) {
									vbDayBookChangeRequestProducts.setClosingStock(qtyClosingBalance.concat(",0"));
									vbDayBookChangeRequestProducts.setOpeningStock(qtyOpeningBalance.concat(",0"));
									vbDayBookChangeRequestProducts.setProductName(productName.concat(",0"));
									vbDayBookChangeRequestProducts.setProductsToCustomer(products.getProductsToCustomer().concat(",0"));
									vbDayBookChangeRequestProducts.setProductsToFactory(products.getProductsToFactory().concat(",0"));
									vbDayBookChangeRequestProducts.setBatchNumber(batchNumber.concat(",0"));
									vbDayBookChangeRequestProducts.setProductsToFactory(products.getProductsToFactory().concat(",0"));
				    				qtyClosingBalance = products.getClosingStock();
				    				vbDayBookChangeRequestProducts.setClosingStock(qtyClosingBalance.concat(",0"));
				    		}
				    	}
				productName = products.getProductName();
				batchNumber = products.getBatchNumber();
				qtyOpeningBalance = products.getOpeningStock();
				if (vbDayBookChangeRequestProducts != null) {
					vbDayBookChangeRequestProducts.setOpeningStock(qtyOpeningBalance.concat(",0"));
					vbDayBookChangeRequestProducts.setProductName(productName.concat(",0"));
					vbDayBookChangeRequestProducts.setProductsToCustomer(products.getProductsToCustomer().concat(",0"));
					vbDayBookChangeRequestProducts.setBatchNumber(batchNumber.concat(",0"));
					vbDayBookChangeRequestProducts.setVbDayBookChangeRequest(vbDayBookChangeRequest);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting vbDayBookChangeRequestProducts: {}", vbDayBookChangeRequestProducts);
					}
					session.save(vbDayBookChangeRequestProducts);
					isSaved = Boolean.TRUE;
				}
			}
		txn.commit();
		session.close();
		
	}
		return isSaved;
	}

	
	/**
	 * This method is used to get all the day books on criteria .
	 * 
	 * @param vbOrganization
	 * @return resultList
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDayBookResult> getDayBookChangeTransaction(VbOrganization organization, String username) {
		List<VbDayBook> vbDayBook = new ArrayList<VbDayBook>();
		List<ChangeRequestDayBookResult> resultList = new ArrayList<ChangeRequestDayBookResult>();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbDayBook.class);
		Date date=new Date();
		criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)));
		criteria.add(Expression.eq("createdBy", username));
		if (organization != null) {
			criteria.add(Expression.eq("vbOrganization", organization));
		}
		criteria.addOrder(Order.desc("createdOn"));
		vbDayBook = criteria.list();
		resultList=prepareResultList(vbDayBook);
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultList: {}", resultList);
		}
		return resultList;
	}
	
	/**
	 * This method is used to get the Day Book Based on daybook Id.
	 * 
	 * @param dayBookId
	 * @param organization
	 * @param userName
	 * @return vbDeliveryNote
	 */
	public VbDayBook getDayBook(Integer dayBookId,VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbDayBook vbDayBook = (VbDayBook) session.get(VbDayBook.class, dayBookId);
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbDayBook: {}", vbDayBook);
		}
		return vbDayBook;
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
	 */
	public VbSalesBook getSalesExecutiveType(Integer dayBookId, VbOrganization organization) {
		Session session = this.getSession();
		VbDayBook vbDayBook=(VbDayBook)session.get(VbDayBook.class, dayBookId);
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", vbDayBook.getSalesExecutive()))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(0)))
				.uniqueResult();
		return vbSalesBook;
	}
	
	/**
	 * This method is responsible to prepare {@link DayBookResult} list.
	 * 
	 * @param dayBookList - {@link List}
	 * @return - {@link List}
	 * 
	 */
	private List<ChangeRequestDayBookResult> prepareResultList(List<VbDayBook> dayBookList){
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
	 * @param salesExecutive
	 * @param dayBookId
	 * @param userName
	 * @param organization
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDayBookResult> getGridData(String salesExecutive,Integer dayBookId,String userName, VbOrganization organization) {
		List<ChangeRequestDayBookResult> dayBookResultList = null;
		Integer returnQty = null;
		Session session = this.getSession();
		List<VbDayBookProducts> productList = null;
		VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
		productList = session
				.createCriteria(VbDayBookProducts.class)
				.createAlias("vbDayBook", "vb")
				.add(Expression.eq("vb.id", dayBookId))
				.add(Expression.eq("vb.vbOrganization", organization))
				.addOrder(Order.asc("productName")).list();
		if (productList != null) {
			ChangeRequestDayBookResult dayBookResult = null;
			dayBookResultList = new ArrayList<ChangeRequestDayBookResult>();
			
			for (VbDayBookProducts vbDayBookProducts : productList) {
				dayBookResult = new ChangeRequestDayBookResult();
				returnQty = (Integer) session.createQuery("SELECT SUM(vb.totalQty) FROM VbSalesReturnProducts vb " +
				         "WHERE vb.vbSalesReturn.vbOrganization = :vbOrganization AND " +
				         "vb.vbSalesReturn.vbSalesBook = :vbSalesBook AND vb.productName = :productName AND vb.batchNumber = :batchNumber GROUP BY vb.productName, vb.batchNumber")
				         .setParameter("vbOrganization", organization)
				         .setParameter("vbSalesBook", vbSalesBook)
				         .setParameter("productName", vbDayBookProducts.getProductName())
				         .setParameter("batchNumber", vbDayBookProducts.getBatchNumber())
				         .uniqueResult();
				
				if(returnQty != null) {
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
				//.add(Expression.eq("flag", new Integer(1)))
				.uniqueResult();
		return vbSalesBook;
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
		Query query = session
				.createQuery("SELECT vb.openingBalance FROM VbSalesBook as vb WHERE vb.salesExecutive = :salesExecutive AND vb.vbOrganization = :vbOrganization AND vb.createdOn IN (SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb)");
		query.setParameter("salesExecutive", salesExecutive);
		query.setParameter("vbOrganization", organization);
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
		VbDayBookChangeRequest changeRequest=null;
		VbDayBook vbDayBook=(VbDayBook)session.get(VbDayBook.class, dayBookId);
		Date date=new Date();
		if(vbDayBook!=null){
		 changeRequest =(VbDayBookChangeRequest)session.createCriteria(VbDayBookChangeRequest.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
	}
		if(changeRequest != null){
			VbDayBookChangeRequest changeRequestDayBook =(VbDayBookChangeRequest)session.createCriteria(VbDayBookChangeRequest.class)
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
					.add(Expression.eq("vbOrganization", organization)).uniqueResult();
			if(changeRequestDayBook !=null){
				isAvailable="y";
			}else{
				isAvailable="n";
			}
		}else{
			isAvailable="n";
		}
		session.close();
		return isAvailable;
	}
	@SuppressWarnings("unchecked")
	public List<ChangeRequestDayBookResult> getDayBookCrResults(String username,
			VbOrganization organization) {
		List<VbDayBookChangeRequest> vbDayBookChangeRequest = new ArrayList<VbDayBookChangeRequest>();
		List<ChangeRequestDayBookResult> resultList = new ArrayList<ChangeRequestDayBookResult>();
		Session session = this.getSession();
		vbDayBookChangeRequest = session.createCriteria(VbDayBookChangeRequest.class)
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("createdOn")).list();
		resultList=prepareDayBookCrResultList(vbDayBookChangeRequest);
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultList: {}", resultList);
		}
		return resultList;
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
	 * @param id
	 * @param organization
	 * @return vbDayBookChangeRequest
	 */
	public VbDayBookChangeRequest getDayBookOnCRId(Integer dayBookCRId , VbOrganization organization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("Integer: {}", dayBookCRId);
		}
		Session session = this.getSession();
		VbDayBookChangeRequest vbDayBookChangeRequest = (VbDayBookChangeRequest) session.createCriteria(VbDayBookChangeRequest.class)
				.add(Expression.eq("id", dayBookCRId))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		return vbDayBookChangeRequest;

	}
	
	/**
	 * This method is used to get the opening balance based on the sales
	 * executive.
	 * 
	 * @param salesExecutive
	 * @param organization
	 * @return Float
	 */
	public Float getOpeningBalanceDayBookCR(String salesExecutive , VbOrganization organization) {
		Float openingBal = null;
		Session session = this.getSession();
		Query query = session
				.createQuery("SELECT vb.openingBalance FROM VbSalesBook as vb WHERE vb.salesExecutive = :salesExecutive AND vb.vbOrganization = :vbOrganization AND vb.createdOn IN (SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb)");
		query.setParameter("salesExecutive", salesExecutive);
		query.setParameter("vbOrganization", organization);
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
	 * This method is responsible for Approving Day Book CR.
	 * 
	 * @param dayBookCrId - {@link Integer}
	 * @param vbOrganization - {@link VbOrganization}
	 */
	public void getApprovedDayBookCR(Integer dayBookCrId, String status, VbOrganization vbOrganization,String userName) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbDayBookChangeRequest vbDayBookChangeRequest = (VbDayBookChangeRequest) session.get(VbDayBookChangeRequest.class, dayBookCrId);
		if(vbDayBookChangeRequest!=null){
			vbDayBookChangeRequest.setModifiedBy(userName);
			vbDayBookChangeRequest.setModifiedOn(new Date());
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				vbDayBookChangeRequest.setStatus(CRStatus.APPROVED.name());
			} else {
				vbDayBookChangeRequest.setStatus(CRStatus.DECLINE.name());
			}
			
			if(_logger.isDebugEnabled()){
				_logger.debug("Updating vbDayBookChangeRequest for approved.");
			}
			session.update(vbDayBookChangeRequest);
	    }
		txn.commit();
		session.close();
	}
	/** This method is responsible to get {@link VbDayBook } based on
	 * {@link VbOrganization} and dayBookId.
	 * 
	 * @param dayBookId -{@link Integer}
	 * @param organization -{@link VbOrganization}
	 * @return isdayBookCRExpired -{@link String}
	 * @throws ParseException 
	 */
	public String fetchJournalCreationTime(Integer dayBookId,VbOrganization organization) throws ParseException {
		Session session=this.getSession();
		String isdayBookCRExpired="";
		Date dayBookCreationTime;
		Date currentDateTime=new Date();
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime=format.parse(d2);
		VbDayBook vbDayBook=null;
		vbDayBook=(VbDayBook)session.get(VbDayBook.class, dayBookId);
		 if(vbDayBook!=null){
			 dayBookCreationTime=vbDayBook.getCreatedOn();
			 String creationTime= DateUtils.formatDateWithTimestamp(dayBookCreationTime);
			 Date dayBookCreation=format.parse(creationTime);
			 Integer totalMin=calculateMinDiff(currentTime,dayBookCreation);
			 if(totalMin > 10){
				 isdayBookCRExpired="y";
			 }else{
				 isdayBookCRExpired="n";
			 }
		 }
		 session.close();
		return isdayBookCRExpired;
	}

	//end of bay book
	//start of journal CR
	/**
	 * This method is used to get all the Journals on current date .
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return resultList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getJournalChangeTransaction(VbOrganization organization, String username) {
		List<VbJournal> journalList = new ArrayList<VbJournal>();
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbJournal.class);
		Date date=new Date();
		criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)));
		criteria.add(Expression.eq("createdBy", username));
		if (organization != null) {
			criteria.add(Expression.eq("vbOrganization", organization));
		}
		criteria.addOrder(Order.desc("createdOn"));
		journalList = criteria.list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("journalList: {}", journalList);
		}
		return journalList;
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
	public String validateSEJournalChangeRequest(String invoiceNo,Integer journalId,VbOrganization organization) {
		String isAvailable = "y";
		Session session = this.getSession();
		VbJournalChangeRequest changeRequest=null;
		VbJournal vbJournal=(VbJournal)session.get(VbJournal.class, journalId);
		Date date=new Date();
		if(vbJournal!=null){
		 changeRequest =(VbJournalChangeRequest)session.createCriteria(VbJournalChangeRequest.class)
				.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
	}
		if(changeRequest != null){
			VbJournalChangeRequest vbJournalChangeRequest =(VbJournalChangeRequest)session.createCriteria(VbJournalChangeRequest.class)
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("invoiceNo", invoiceNo))
					.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(date), DateUtils.getEndTimeStamp(date)))
					.add(Expression.eq("vbOrganization", organization)).uniqueResult();
			if(vbJournalChangeRequest !=null){
				isAvailable="y";
			}else{
				isAvailable="n";
			}
		}else{
			isAvailable="n";
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
	 * @return vbJournal  - {@link VbJournal}
	 */
	public VbJournal getJournalById(Integer journalId,VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal) session.get(VbJournal.class, journalId);
		if (_logger.isDebugEnabled()) {
			_logger.debug("vbJournal: {}", vbJournal);
		}
		return vbJournal;
	}
	
	/**
	 * This method is responsible to persist {@link VbJournalChangeRequest} into database.
	 * 
	 * @param journalFormEditValues - {@link String[]}
	 *  * @param journalCommand - {@link ChangeRequestJournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * 
	 */
	public void saveJournalCR(ChangeRequestJournalCommand journalCommand,String[] journalFormEditValues, VbOrganization organization, String userName){
		Session session = this.getSession();
		Date date = new Date();
		Transaction txn = session.beginTransaction();
		ArrayListMultimap<String,String> changedFormFieldsMap = ArrayListMultimap.create();
		for(final String s : journalFormEditValues) {
			   final String split[] = s.split(":");
			   changedFormFieldsMap.put(split[0], split[1]);
			}
		VbJournalChangeRequest vbJournalinstance = new VbJournalChangeRequest();
		List<String> customerInvoiceName = changedFormFieldsMap.get("invoiceName");
		if(customerInvoiceName.size()!=0){
		for(String customerInvoiceName1 : customerInvoiceName) {
			if(customerInvoiceName1.equals(journalCommand.getInvoiceName())){
				vbJournalinstance.setInvoiceName(journalCommand.getInvoiceName().concat(",1"));
			}
		}
		}else{
			vbJournalinstance.setInvoiceName(journalCommand.getInvoiceName().concat(",0"));
		}
		List<String> journalAmount = changedFormFieldsMap.get("amount");
		if(journalAmount.size()!=0){
		for(String journalAmount1 : journalAmount) {
			if(journalAmount1.equals(journalCommand.getAmount())){
				vbJournalinstance.setAmount(journalCommand.getAmount().concat(",1"));
			}
		}
		}else{
			vbJournalinstance.setAmount(journalCommand.getAmount().concat(",0"));
		}
		List<String> description = changedFormFieldsMap.get("description");
		if(description.size()!=0){
		for(String description1 : description) {
			if(description1.equals(journalCommand.getDescription())){
				vbJournalinstance.setDescription(journalCommand.getDescription().concat(",1"));
			}
		}
		}else{
			vbJournalinstance.setDescription(journalCommand.getDescription().concat(",0"));
		}
		vbJournalinstance.setBusinessName(journalCommand.getBusinessName());
		vbJournalinstance.setCrDescription(journalCommand.getCrDescription());
		vbJournalinstance.setCreatedBy(userName);
		vbJournalinstance.setCreatedOn(date);
		vbJournalinstance.setModifiedOn(date);
		vbJournalinstance.setInvoiceNo(journalCommand.getInvoiceNo());
		vbJournalinstance.setStatus(CRStatus.PENDING.name());
		vbJournalinstance.setJournalType(journalCommand.getJournalType());
		vbJournalinstance.setVbOrganization(organization);
		vbJournalinstance.setVbSalesBook(getVbSalesBook(organization, userName));		
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbJournal: {}", vbJournalinstance);
		}
		session.save(vbJournalinstance);
		txn.commit();
		session.close();
	}
	/**
	 * This is method is responsible to get {@link VbSalesBook} instance.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param salesExecutive - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getVbSalesBook(VbOrganization organization, String salesExecutive) {
		Session session = this.getSession();
		VbSalesBook salesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("salesExecutive", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("flag", new Integer(0)))
				.uniqueResult();
		
		if(salesBook == null) {
			return null;
		}
		session.close();
		return salesBook;
	}
	
	/**This method is responsible for getting all pending journals CR from Change Request table.
	 * 
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbJournalChangeRequestList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournalChangeRequest> getJournalCrResults(String username,VbOrganization organization) {
		Session session = this.getSession();
		List<VbJournalChangeRequest> vbJournalChangeRequestList = null;
		Date date = new Date();
		vbJournalChangeRequestList = session.createCriteria(VbJournalChangeRequest.class)
					.add(Expression.eq("status", CRStatus.PENDING.name()))
					.add(Expression.eq("vbOrganization", organization))
					.addOrder(Order.asc("createdOn")).list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("vbJournalChangeRequestList: {}", vbJournalChangeRequestList);
		}
		return vbJournalChangeRequestList;

	}
	/**
	 * This method is responsible to get {@link VbJournalChangeRequest} based on
	 * journal id and userName.
	 * 
	 * @param journalCRId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbJournalChangeRequest - {@link VbJournalChangeRequest}
	 * 
	 */
	public VbJournalChangeRequest getJournalCRById(Integer journalCRId, VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbJournalChangeRequest vbJournalChangeRequest=(VbJournalChangeRequest)session.get(VbJournalChangeRequest.class, journalCRId);
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Fetching VbJournalChangeRequest: {}", vbJournalChangeRequest);
		}
		return vbJournalChangeRequest;
	}
	
	/**
	 * This method is responsible to approve the raised {@link VbJournalChangeRequest} requests means
	 * updating {@link VbCustomerCreditInfo} if the business name have any credit
	 * or saving the amount in {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param id - {@link Integer}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isApproved - {@link Boolean}
	 * 
	 */
	public Boolean approveOrDeclineJournalCR(Integer id, String status, VbOrganization organization, String userName) {
		Boolean isApproved = Boolean.FALSE;
		Date date = new Date();
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbJournalChangeRequest journal = (VbJournalChangeRequest) session.get(VbJournalChangeRequest.class, id);
		if(journal != null) {
			journal.setModifiedBy(userName);
			journal.setModifiedOn(date);
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				journal.setStatus(CRStatus.APPROVED.name());
				isApproved = Boolean.TRUE;
			} else {
				journal.setStatus(CRStatus.DECLINE.name());
				isApproved = Boolean.FALSE;
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbJournalChangeRequest: {}", journal);
			}
			session.update(journal);
		}
		txn.commit();
		session.close();
		return isApproved;
	}
	
	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * {@link VbOrganization} and userName and invoiceNo.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param userName -{@link String}
	 * @param invoiceNumber -{@link String}
	 * @return deliveryNoteId -{@link Integer}
	 * 
	 */
	public Integer getJournalBasedOnInvoiceNo(VbOrganization organization,String invoiceNumber,String userName) {
		Session session=this.getSession();
		Integer journalId=0;
		VbJournal vbJournal=null;
		vbJournal = (VbJournal) session
				.createCriteria(VbJournal.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.uniqueResult();
		 if(vbJournal!=null){
			 journalId=vbJournal.getId();
		 }
		return journalId;
	}
	
	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * {@link VbOrganization} and invoiceNo and journalId.
	 * 
	 * @param organization -{@link VbOrganization}
	 * @param journalId -{@link Integer}
	 * @param invoiceNumber -{@link String}
	 * @return isDeliveryNoteCRExpired -{@link String}
	 * @throws ParseException 
	 * 
	 */
	public String fetchJournalCreationTime(Integer journalId,
			String invoiceNumber, VbOrganization organization) throws ParseException {
		Session session=this.getSession();
		String isJournalCRExpired="";
		Date journalCreationTime;
		Date currentDateTime=new Date();
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String d2 = DateUtils.formatDateWithTimestamp(currentDateTime);
		Date currentTime=format.parse(d2);
		VbJournal vbJournal=null;
		vbJournal = (VbJournal) session
				.createCriteria(VbJournal.class)
				.add(Expression.eq("id", journalId))
				.add(Expression.eq("invoiceNo", invoiceNumber))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		 if(vbJournal!=null){
			 journalCreationTime=vbJournal.getCreatedOn();
			 String creationTime= DateUtils.formatDateWithTimestamp(journalCreationTime);
			 Date journalCreation=format.parse(creationTime);
			 Integer totalMin=calculateMinDiff(currentTime,journalCreation);
			 if(totalMin > 10){
				 isJournalCRExpired="y";
			 }else{
				 isJournalCRExpired="n";
			 }
		 }
		 session.close();
		return isJournalCRExpired;
	}
	/**
	 * This method is used to get the alerts for dashboard.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return alertList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbSystemAlertsHistory> getAlertsList(VbOrganization organization) {
		Session session = this.getSession();
		List<VbSystemAlertsHistory> alertList = session.createCriteria(VbSystemAlertsHistory.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("enabled", Boolean.TRUE))
				.setMaxResults(10)
				.list();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Raised alerts list are {}", alertList.size());
		}
		return alertList;
	}
	/**
	 * This Method is Responsible to Check whether The Transaction Sales Return for given Invoice Number is Available or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return salesReturnCRId - {@link Integer}
	 */
	public Integer checkTransactionSalesReturn(String invoiceNumber,VbOrganization organization) {
		String isAvailable = "y";
		Integer salesReturnCRId;
		Session session = this.getSession();
		VbSalesReturnChangeRequest vbSalesReturnChangeRequest = (VbSalesReturnChangeRequest) session.createCriteria(VbSalesReturnChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.uniqueResult();
		if(vbSalesReturnChangeRequest != null){	
			//isAvailable = "y";
			salesReturnCRId=vbSalesReturnChangeRequest.getId();
		}else{
			salesReturnCRId=0;
		}
		session.close();
		return salesReturnCRId;
	}
	
	/**
	 * This Method is Responsible to Check whether The Transaction Journal for given Invoice Number is Available or not.
	 * 
	 * @param invoiceNumber - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return salesReturnCRId - {@link Integer}
	 */
	public Integer checkTransactionJournal(String invoiceNumber,VbOrganization organization) {
		String isAvailable = "y";
		Integer journalCRId;
		Session session = this.getSession();
		VbJournalChangeRequest vbJournalChangeRequest = (VbJournalChangeRequest) session.createCriteria(VbJournalChangeRequest.class)
				.add(Restrictions.eq("invoiceNo", invoiceNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Expression.eq("status", CRStatus.PENDING.name()))
				.uniqueResult();
		if(vbJournalChangeRequest != null){	
			//isAvailable = "y";
			journalCRId=vbJournalChangeRequest.getId();
		}else{
			journalCRId=0;
		}
		session.close();
		return journalCRId;
	}

	/**
	 * This method is responsible to get created by from my-sales change requests
	 * .  
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * 
	 */
	public String getCreatedBy(Integer id, String crType) {
		Session session = this.getSession();
		String createdBy = null;
		if(OrganizationUtils.CR_TYPE_DELIVERY_NOTE.equalsIgnoreCase(crType)) {
			VbDeliveryNoteChangeRequest changeRequest = (VbDeliveryNoteChangeRequest) session.get(VbDeliveryNoteChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_SALES_RETURNS.equalsIgnoreCase(crType)) {
			VbSalesReturnChangeRequest changeRequest = (VbSalesReturnChangeRequest) session.get(VbSalesReturnChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_JOURNAL.equalsIgnoreCase(crType)) {
			VbJournalChangeRequest changeRequest = (VbJournalChangeRequest) session.get(VbJournalChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		} else if (OrganizationUtils.CR_TYPE_DAY_BOOK.equalsIgnoreCase(crType)) {
			VbDayBookChangeRequest changeRequest = (VbDayBookChangeRequest) session.get(VbDayBookChangeRequest.class, id);
			createdBy = changeRequest.getCreatedBy();
		}
		session.close();
		return createdBy;
	}

}

