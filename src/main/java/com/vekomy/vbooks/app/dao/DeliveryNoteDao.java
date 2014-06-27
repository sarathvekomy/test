/**
 * com.vekomy.vbooks.app.dao.DeliveryNoteDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.request.CustomerCR;
import com.vekomy.vbooks.app.request.CustomersInfoRequest;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteProduct;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.app.response.CustomerInfo;
import com.vekomy.vbooks.app.response.CustomerInfoList;
import com.vekomy.vbooks.app.response.CustomerProductsCost;
import com.vekomy.vbooks.app.response.AllocatedProduct;
import com.vekomy.vbooks.app.response.AllocatedProductList;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.CRStatus;
import com.vekomy.vbooks.app.utils.DateUtils;
import com.vekomy.vbooks.app.utils.ENotificationTypes;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequestDetails;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNote;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments;
import com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSystemNotifications;

/**
 * @author NKR
 * 
 */
public class DeliveryNoteDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteDao.class);

	/**
	 * This method is responsible to get all the business names, which are
	 * assigned to corresponding sales executive.
	 * 
	 * @param userName - {@link String}
	 * @param organizationId - {@link Integer}
	 * @return responseList {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerAmountInfo> getBusinessName(String userName,
			Integer organizationId) {
		Session session = null;
		List<CustomerAmountInfo> responseList = new ArrayList<CustomerAmountInfo>();
		try {
			session = this.getSession();
			VbOrganization organization = getOrganization(session, organizationId);
			if (organization != null) {
				VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("username", userName))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				if (employee != null) {
					List<VbCustomer> customerList = session.createCriteria(VbEmployeeCustomer.class)
							.setProjection(Projections.property("vbCustomer"))
							.add(Restrictions.eq("vbEmployee", employee))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();
					CustomerAmountInfo response = null;
					Double creditAmount;
					Double advanceAmount;
					String businessName = null;
					String invoiceName = null;
					for (VbCustomer customer : customerList) {
						businessName = customer.getBusinessName();
						invoiceName = customer.getInvoiceName();
						// For Customer Credit Info
						creditAmount = (Double) session.createCriteria(VbCustomerCreditInfo.class)
								.setProjection(Projections.sum("due"))
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (creditAmount == null) {
							creditAmount = new Double(0);
						}
						// For Customer Advance info
						advanceAmount = (Double) session.createCriteria(VbCustomerAdvanceInfo.class)
								.setProjection(Projections.sum("balance"))
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (advanceAmount == null) {
							advanceAmount = new Double(0);
						}
						// Preparing response
						response = new CustomerAmountInfo();
						response.setUid(String.valueOf(customer.getId()));
						response.setAdvanceAmount(advanceAmount);
						response.setBusinessName(businessName);
						response.setCreditAmount(creditAmount);
						response.setInvoiceName(invoiceName);
						response.setStatusCode(new Integer(200));
						response.setMessage("Fetching businessNames");
						responseList.add(response);
					}
				}
			}
		} catch (HibernateException exception) {
			_logger.error(exception.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} business names have been assigned to {}", responseList.size(), userName);
		}
		return responseList;
	}
	
	public List<CustomerAmountInfo> getnewlyAssignedBusinessName(CustomersInfoRequest request) {
		Session session = null;
		List<CustomerAmountInfo> responseList = new ArrayList<CustomerAmountInfo>();
		try {
			session = this.getSession();
			VbOrganization organization = getOrganization(session, request.getOrgId());
			Double creditAmount;
			Double advanceAmount = new Double(0);
			CustomerAmountInfo response = null;
			VbCustomer customer = null;
			if (organization != null) {
				for (String businessName : request.getBusinessNames()) {
					customer = (VbCustomer) session.createCriteria(VbCustomer.class)							
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("vbOrganization", organization))
							.uniqueResult();
					if (customer != null) {
						// For Customer Credit Info
						creditAmount = (Double) session.createCriteria(VbCustomerCreditInfo.class)
								.setProjection(Projections.sum("due"))
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (creditAmount == null) {
							creditAmount = new Double(0);
						}	
							// For Customer Advance info
						advanceAmount = (Double) session.createCriteria(VbCustomerAdvanceInfo.class)
								.setProjection(Projections.sum("balance"))
								.add(Restrictions.eq("businessName", businessName))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						if (advanceAmount == null) {
							advanceAmount = new Double(0);
						}
						
						// Preparing response
						response = new CustomerAmountInfo();
						response.setUid(String.valueOf(customer.getId()));
						response.setAdvanceAmount(advanceAmount);
						response.setBusinessName(businessName);
						response.setCreditAmount(creditAmount);
						response.setInvoiceName(customer.getInvoiceName());
						responseList.add(response);
					}
				}
				response.setStatusCode(new Integer(200));
				response.setMessage("Fetching businessNames");
			}
		} catch (HibernateException exception) {
			_logger.error(exception.getMessage());
		} finally {
			if(session != null) {
				session.close();
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} business names have been newly assigned to ", responseList.size());
		}
		return responseList;
	}

	@SuppressWarnings("unchecked")
	public AllocatedProductList getProductList(String salesExecutive,
			Integer organizationId) {
		Session session = this.getSession();
		VbOrganization organization = getOrganization(session, organizationId);
		VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
		List<VbSalesBookProducts> productList = session.createQuery(
						"FROM VbSalesBookProducts vb WHERE vb.vbSalesBook.salesExecutive = :salesExecutive"
						+ " AND vb.vbSalesBook.vbOrganization = :vbOrganization AND vb.vbSalesBook.flag = :flag " 
						+ "GROUP BY vb.productName, vb.batchNumber")
				.setParameter("salesExecutive", salesExecutive)
				.setParameter("vbOrganization", organization)
				.setParameter("flag", new Integer(1)).list();
		Long availableQty;
		Long allottedQty;
		AllocatedProductList salesInfo = new AllocatedProductList();
		salesInfo.setAdvanceAmt(salesBook.getOpeningBalance());
		List<AllocatedProduct> productsList = new ArrayList<AllocatedProduct>();
		AllocatedProduct response = null;
		String productName = null;
		String batchNumber = null;

		for (VbSalesBookProducts vbSalesBookProducts : productList) {
			productName = vbSalesBookProducts.getProductName();
			batchNumber = vbSalesBookProducts.getBatchNumber();
			VbProduct product = (VbProduct) session.createCriteria(VbProduct.class)
					.add(Restrictions.eq("productName", productName))
					.add(Restrictions.eq("batchNumber", batchNumber))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			response = new AllocatedProduct();
			response.setProductName(productName);
			response.setBatchNumber(batchNumber);
			response.setProductCost(product.getCostPerQuantity());
			response.setUid(String.valueOf(product.getId()));
			allottedQty  = vbSalesBookProducts.getQtyAllotted().longValue();
			availableQty = vbSalesBookProducts.getQtyClosingBalance() + allottedQty;
			response.setAvailableQty(availableQty.intValue());
			productsList.add(response);
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} products available for {}", productsList.size(), salesExecutive);
		}
		salesInfo.setStatusCode(new Integer(200));
		salesInfo.setMessage("Product List");
		salesInfo.setProductList(productsList);
		return salesInfo;
	}

	public Response saveCustomerInfo(CustomerInfo request) {
		Session session = null;
		Transaction txn = null;
		Response response = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			// Persisting VbCustomerChangeRequest
			VbCustomerChangeRequest customerChangeRequest = new VbCustomerChangeRequest();
			Date createdDate = new Date();
			String businessName = request.getBusinessName();
			String createdBy = request.getSalesExecutive();
			VbOrganization organization = getOrganization(session,
					request.getOrganizationId());
			VbSalesBook salesBook = getVbSalesBook(session, createdBy, organization);
			customerChangeRequest.setBusinessName(businessName);
			customerChangeRequest.setCrType(request.getCrType());
			customerChangeRequest.setGender(request.getGender());
			customerChangeRequest.setVbOrganization(organization);
			customerChangeRequest.setCreatedOn(createdDate);
			customerChangeRequest.setCreatedBy(createdBy);
			customerChangeRequest.setModifiedOn(createdDate);
			customerChangeRequest.setStatus(CRStatus.PENDING.name());
			customerChangeRequest.setFlag(new Integer(1));

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting customerChangeRequest: {}",
						customerChangeRequest);
			}
			session.save(customerChangeRequest);
			// Persisting VbCustomerChangeRequestDetails
			VbCustomerChangeRequestDetails vbCustomerCrDetail = new VbCustomerChangeRequestDetails();
			vbCustomerCrDetail.setCustomerName(request.getCustomerName());
			vbCustomerCrDetail.setInvoiceName(request.getInvoiceName());
			vbCustomerCrDetail.setAddressLine1(request.getAddressLine1());
			vbCustomerCrDetail.setAddressLine2(request.getAddressLine2());
			vbCustomerCrDetail.setCity(request.getCity());
			vbCustomerCrDetail.setLandmark(request.getLandmark());
			vbCustomerCrDetail.setLocality(request.getLocality());
			vbCustomerCrDetail.setMobile(request.getMobile());
			vbCustomerCrDetail.setZipcode(request.getZipcode());
			vbCustomerCrDetail.setEmail(request.getEmail());
			vbCustomerCrDetail.setDirectLine(request.getDirectLine());
			vbCustomerCrDetail.setRegion(request.getRegion());
			vbCustomerCrDetail.setAlternateMobile(request.getAlternateMobile());
			vbCustomerCrDetail.setState(request.getState());
			vbCustomerCrDetail.setAddressType(request.getAddressType() == null ? ""
					: request.getAddressType());
			vbCustomerCrDetail.setCreatedBy(request.getSalesExecutive());
			vbCustomerCrDetail.setCreatedDate(createdDate);
			vbCustomerCrDetail.setModifiedDate(createdDate);
			vbCustomerCrDetail.setModifiedBy(request.getSalesExecutive());
			vbCustomerCrDetail.setVbCustomerChangeRequest(customerChangeRequest);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbCustomerCrDetail: {}",
						vbCustomerCrDetail);
			}
			session.save(vbCustomerCrDetail);
			// Persisting VbSystemNotifications.
			VbSystemNotifications systemNotifications = new VbSystemNotifications();
			systemNotifications.setBusinessName(businessName);
			systemNotifications.setInvoiceNo(request.getRefID());
			systemNotifications.setCreatedBy(createdBy);
			systemNotifications.setCreatedOn(createdDate);
			systemNotifications.setModifiedOn(createdDate);
			systemNotifications.setNotificationStatus(CRStatus.PENDING.name());
			systemNotifications.setNotificationType(ENotificationTypes.CUSTOMER_CR
					.name());
			systemNotifications.setFlag(new Integer(1));
			systemNotifications.setVbOrganization(organization);
			systemNotifications.setVbSalesBook(salesBook);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSystemNotifications: {}",
						systemNotifications);
			}
			session.save(systemNotifications);
			txn.commit();

			// Preparing response to client.
			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));

			if (_logger.isDebugEnabled()) {
				_logger.debug("response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			response = new Response();
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));
			
			_logger.error("Response: {}", response);
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to get raised
	 * 
	 * @param request - {@link CustomerCR}
	 * @param organizationId - {@link Integer}
	 * @return customerCrResponse - {@link CustomerInfo}
	 */
	public CustomerInfoList getAssignedcustomerInfolist(
			CustomersInfoRequest request) {
		Session session = this.getSession();
		CustomerInfo customerCrResponse = null;
		VbCustomerDetail customerDetail = null;
		VbCustomer customer = null;
		VbOrganization organization = getOrganization(session, request.getOrgId());
		List<String> businessNameList = request.getBusinessNames();
		List<CustomerInfo> CustomerInfoList = new ArrayList<CustomerInfo>();
		for (String businessName : businessNameList) {
			customerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
					.createAlias("vbCustomer", "customer")
					.add(Restrictions.eq("customer.vbOrganization", organization))
					.add(Restrictions.eq("customer.businessName", businessName))
					.uniqueResult();
			customer = customerDetail.getVbCustomer();
			customerCrResponse = new CustomerInfo();
			customerCrResponse.setRefID(String.valueOf(customer.getId()));
			customerCrResponse.setBusinessName(customer.getBusinessName());
			customerCrResponse.setAddressLine1(customerDetail.getAddressLine1());
			customerCrResponse.setAddressLine2(customerDetail.getAddressLine2());
			customerCrResponse.setCity(customerDetail.getCity());
			customerCrResponse.setCustomerName(customer.getCustomerName());
			customerCrResponse.setDirectLine(customerDetail.getDirectLine());
			customerCrResponse.setEmail(customerDetail.getEmail());
			customerCrResponse.setGender(customer.getGender());
			customerCrResponse.setInvoiceName(customer.getInvoiceName());
			customerCrResponse.setLandmark(customerDetail.getLandmark());
			customerCrResponse.setLocality(customerDetail.getLocality());
			customerCrResponse.setRegion(customerDetail.getRegion());
			customerCrResponse.setState(customerDetail.getState());
			customerCrResponse.setZipcode(customerDetail.getZipcode());
			customerCrResponse.setMobile(customerDetail.getMobile());
			customerCrResponse.setAlternateMobile(customerDetail
					.getAlternateMobile());
			customerCrResponse.setEmail(customerDetail.getEmail());
			CustomerInfoList.add(customerCrResponse);
		}
		session.close();
		CustomerInfoList responseList = new CustomerInfoList();
		responseList.setCustomerInfoList(CustomerInfoList);
		responseList.setMessage("success");
		responseList.setStatusCode(new Integer(200));
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", responseList.getCustomerInfoList().size());
		}
		return responseList;
	}

	public Response saveDeliveryNote(DeliveryNote request) {
		Response response = null;
		Session session = null;
		Transaction transaction = null;
		try {
			response = new Response();
			Date date = new Date();
			session = this.getSession();
			transaction = session.beginTransaction();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			VbDeliveryNote vbDeliveryNote = new VbDeliveryNote();
			vbDeliveryNote.setBusinessName(request.getBusinessName());
			vbDeliveryNote.setCreatedBy(request.getCreatedBy());
			vbDeliveryNote.setCreatedOn(date);
			vbDeliveryNote.setModifiedOn(date);
			vbDeliveryNote.setInvoiceName(request.getInvoiceName());
			vbDeliveryNote.setFlag(new Integer(1));
			List<DeliveryNoteProduct> deliveryNoteProductsList = request.getProducts();
			String invoiceNo = null;
			if(!deliveryNoteProductsList.isEmpty()) {
				invoiceNo = generateInvoiceNo(session, organization).concat("#").concat(request.getInvoiceNo());
			} else {
				invoiceNo = generateInvoiceNoForPayments(session, organization).concat("#").concat(request.getInvoiceNo());
			}
			vbDeliveryNote.setInvoiceNo(invoiceNo);
			vbDeliveryNote.setVbOrganization(organization);
			VbSalesBook salesBook = getVbSalesBook(session, request.getCreatedBy(), organization);
			vbDeliveryNote.setVbSalesBook(salesBook);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbDeliveryNote: {}", vbDeliveryNote);
			}
			session.save(vbDeliveryNote);

			VbDeliveryNoteProducts vbDeliveryNoteProducts = null;
			if (deliveryNoteProductsList != null) {
				for (DeliveryNoteProduct deliveryNoteProducts : deliveryNoteProductsList) {
					vbDeliveryNoteProducts = new VbDeliveryNoteProducts();
					vbDeliveryNoteProducts.setBatchNumber(deliveryNoteProducts.getBatchNumber());
					vbDeliveryNoteProducts.setBonusQty(deliveryNoteProducts.getBonusQty());
					vbDeliveryNoteProducts.setBonusReason(deliveryNoteProducts.getBonusReason());
					vbDeliveryNoteProducts.setProductCost(deliveryNoteProducts.getProductCost());
					vbDeliveryNoteProducts.setProductName(deliveryNoteProducts.getProductName());
					vbDeliveryNoteProducts.setProductQty(deliveryNoteProducts.getProductQty());
					vbDeliveryNoteProducts.setTotalCost(deliveryNoteProducts.getTotalCost());
					vbDeliveryNoteProducts.setVbDeliveryNote(vbDeliveryNote);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbDeliveryNoteProducts: {}", vbDeliveryNoteProducts);
					}
					session.save(vbDeliveryNoteProducts);
				}
			}

			VbDeliveryNotePayments vbDeliveryNotePayments = new VbDeliveryNotePayments();
			Float balanceValue = request.getBalance();
			if(balanceValue < 0) {
				vbDeliveryNotePayments.setBalance(-(balanceValue));
			} else {
				vbDeliveryNotePayments.setBalance(balanceValue);
			}
			String paymentType = request.getPaymentType();
			vbDeliveryNotePayments.setPaymentType(paymentType);
			if (ApplicationConstants.PAYMENT_TYPE_CHEQUE.equalsIgnoreCase(paymentType)) {
				vbDeliveryNotePayments.setBankName(request.getBankName());
				vbDeliveryNotePayments.setBranchName(request.getBranchName());
				vbDeliveryNotePayments.setChequeNo(request.getChequeNo());
			}
			vbDeliveryNotePayments.setPresentAdvance(request.getPresentAdvance());
			vbDeliveryNotePayments.setPresentPayable(request.getPresentPayable());
			vbDeliveryNotePayments.setPresentPayment(request.getPresentPayment());
			vbDeliveryNotePayments.setPreviousCredit(request.getPreviousCredit());
			Float totalPayableValue = request.getTotalPayable();
			if(totalPayableValue < 0) {
				vbDeliveryNotePayments.setTotalPayable(-(totalPayableValue));
			} else {
				vbDeliveryNotePayments.setTotalPayable(totalPayableValue);
			}
			vbDeliveryNotePayments.setVbDeliveryNote(vbDeliveryNote);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbDeliveryNotePayments: {}", vbDeliveryNotePayments);
			}
			session.save(vbDeliveryNotePayments);

			// Updating previous credits of the corresponding business name.
			VbCustomerCreditInfo customerCreditInfo = new VbCustomerCreditInfo();
			VbCustomerCreditTransaction customerCreditTxn = new VbCustomerCreditTransaction();
			if (customerCreditInfo != null) {
				String businessName = request.getBusinessName();
				// String invoiceName = request.getInvoiceNo();
				customerCreditInfo.setBusinessName(businessName);
				customerCreditInfo.setCreditFrom(invoiceNo);
				customerCreditInfo.setInvoiceNo(invoiceNo);
				String customerCreditInvoiceNo = new StringBuffer(
						generateCustomerCreditInfoInvoiceNumber(organization))
						.append("#").append(request.getInvoiceNo()).toString();
				customerCreditInfo.setInvoiceNo(customerCreditInvoiceNo);
				customerCreditInfo.setCreatedBy(request.getCreatedBy());
				customerCreditInfo.setCreatedOn(date);
				customerCreditInfo.setModifiedOn(date);
				Float balance = request.getPresentPayable() - request.getPresentPayment();
				Float advance = request.getPresentAdvance();

				// persist customer credit Txn basic information
				customerCreditTxn.setCreditFrom(invoiceNo);
				customerCreditTxn.setBusinessName(businessName);
				customerCreditTxn.setDebitTo(customerCreditInvoiceNo);
				customerCreditTxn.setFlag(new Integer(1));
				customerCreditTxn.setCreatedBy(request.getCreatedBy());
				customerCreditTxn.setCreatedOn(date);
				customerCreditTxn.setModifiedOn(date);
				if (advance < balance) {
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
							float advBalance = advanceInfo.getBalance();
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
							advanceInfo.setModifiedBy(request.getCreatedBy());
							advanceInfo.setModifiedOn(new Date());
							advanceInfo.setDebitTo(invoiceNo);

							customerDebitTxn.setCreditFrom(advanceInfo.getInvoiceNo());
							customerDebitTxn.setBusinessName(businessName);
							customerDebitTxn.setFlag(new Integer(1));
							customerDebitTxn.setCreatedBy(request.getCreatedBy());
							customerDebitTxn.setCreatedOn(new Date());
							customerDebitTxn.setModifiedOn(new Date());
							customerDebitTxn.setVbOrganization(organization);
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbCustomerDebitTransaction: {}", customerDebitTxn);
							}
							session.save(customerDebitTxn);
							
							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating advanceInfo: {}", advanceInfo);
							}
							session.update(advanceInfo);
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
							vbCustomerCreditInfo.setModifiedBy(request.getCreatedBy());
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
							vbCustomerCreditInfo.setDebitTo(invoiceNo);
							
							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerCreditInfo: {}", vbCustomerCreditInfo);
							}
							session.update(vbCustomerCreditInfo);
							
							// persist customer credit Txn basic information
							vbDebitTransaction.setCreditFrom(vbCustomerCreditInfo.getInvoiceNo());
							vbDebitTransaction.setBusinessName(businessName);
							vbDebitTransaction.setFlag(new Integer(1));
							vbDebitTransaction.setCreatedBy(request.getCreatedBy());
							vbDebitTransaction.setCreatedOn(date);
							vbDebitTransaction.setModifiedOn(date);
							vbDebitTransaction.setVbOrganization(organization);

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting vbDebitTransaction: {}", vbDebitTransaction);
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
								advanceInfo.setModifiedBy(request.getCreatedBy());
								advanceInfo.setModifiedOn(date);
								advanceInfo.setDebitTo(invoiceNo);
								
								if (_logger.isDebugEnabled()) {
									_logger.debug("Updating VbCustomerAdvanceInfo: {}", advanceInfo);
								}
								session.update(advanceInfo);
								
								// persist customer credit Txn basic information
								vbDebitTransactionAdvance.setCreditFrom(customerCreditInvoiceNo);
								vbDebitTransactionAdvance.setBusinessName(businessName);
								vbDebitTransactionAdvance.setFlag(new Integer(1));
								vbDebitTransactionAdvance.setCreatedBy(request.getCreatedBy());
								vbDebitTransactionAdvance.setCreatedOn(date);
								vbDebitTransactionAdvance.setModifiedOn(date);
								vbDebitTransactionAdvance.setVbOrganization(organization);

								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting vbDebitTransaction: {}", vbDebitTransactionAdvance);
								}
								session.save(vbDebitTransactionAdvance);
							} else {
								VbCustomerCreditTransaction creditTransaction = new VbCustomerCreditTransaction();
								// If there is no advance for businessName creating
								// new record.
								advanceInfo = new VbCustomerAdvanceInfo();
								advanceInfo.setAdvance(remainingBalance);
								advanceInfo.setBalance(remainingBalance);
								advanceInfo.setBusinessName(businessName);
								String advanceInfoInvoiceNumber = new StringBuffer(
										generateCustomerAdvanceInfoInvoiceNumber(organization))
										.append("#").append(request.getInvoiceNo())
										.toString();
								advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
								advanceInfo.setCreatedBy(request.getCreatedBy());
								advanceInfo.setCreatedOn(date);
								advanceInfo.setCreditFrom(invoiceNo);
								advanceInfo.setModifiedOn(date);
								advanceInfo.setVbOrganization(organization);
								
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
								}
								session.save(advanceInfo);
								
								// persisting Customer credit Txn for new Advance
								// Info.
								creditTransaction.setBusinessName(businessName);
								creditTransaction.setDebitTo(advanceInfoInvoiceNumber);
								creditTransaction.setCreditFrom(invoiceNo);
								creditTransaction.setAmount(remainingBalance);
								creditTransaction.setFlag(new Integer(1));
								creditTransaction.setCreatedBy(request.getCreatedBy());
								creditTransaction.setCreatedOn(date);
								creditTransaction.setModifiedOn(date);
								creditTransaction.setVbOrganization(organization);
								
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting VbCustomerCreditTransaction: {}", creditTransaction);
								}
								session.save(creditTransaction);
							}
							// Updating remaining balance.
							remainingBalance = new Float(0);
						}
					}
				}
			}
			transaction.commit();
			
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			return response;
		} catch (HibernateException exception) {
			exception.printStackTrace();
			if(transaction != null) {
				transaction.rollback();
			}
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to generate invoice no for
	 * {@link VbDeliveryNote}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateInvoiceNo(Session session, VbOrganization organization) {
		String seperator = ApplicationConstants.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		// For fetching latest invoice no.
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
						+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%DN%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
				.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_DELIVERY_NOTE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(seperator)
						.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE)
						.append(seperator).append(formattedDate)
						.append(seperator).append("1");
			}

		} else {
			String[] invoicenumber = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = invoicenumber[0].split(seperator);
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod,
					organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(seperator)
					.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newInvoiceNo.toString());
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated delivery note invoiceNo is {}",
					generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}
	
	/**
	 * This method is responsible to generate invoice no for {@link VbDeliveryNote}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return invoiceNoForPayments - {@link String}
	 * 
	 */
	public String generateInvoiceNoForPayments(Session session, VbOrganization organization) {
		String seperator = ApplicationConstants.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbDeliveryNote vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbDeliveryNote vbdn "
								+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%COLLECTIONS%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS))
				.uniqueResult();
		StringBuffer invoiceNoForPayments = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				invoiceNoForPayments
						.append(organization.getOrganizationCode())
						.append(seperator)
						.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
						.append(seperator).append(formattedDate)
						.append(seperator)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				invoiceNoForPayments
						.append(organization.getOrganizationCode())
						.append(seperator)
						.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
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
					.append(ApplicationConstants.TXN_TYPE_DELIVERY_NOTE_COLLECTIONS)
					.append(seperator).append(formattedDate).append(seperator)
					.append(newInvoiceNo);
		}
		return invoiceNoForPayments.toString();
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
	 * This method is responsible to generate invoice no for
	 * {@link VbCustomerCreditInfo}.
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
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_CUSTOMER_CREDIT))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(ApplicationConstants.SEPERATOR)
						.append(ApplicationConstants.TXN_TYPE_CUSTOMER_CREDIT)
						.append(ApplicationConstants.SEPERATOR)
						.append(formattedDate)
						.append(ApplicationConstants.SEPERATOR)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(ApplicationConstants.SEPERATOR)
						.append(ApplicationConstants.TXN_TYPE_CUSTOMER_CREDIT)
						.append(ApplicationConstants.SEPERATOR)
						.append(formattedDate)
						.append(ApplicationConstants.SEPERATOR).append("1");
			}

		} else {
			String[] creditInfoInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = creditInfoInvoiceNo[0].split(ApplicationConstants.SEPERATOR);
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo = generatedInvoiceNo
					.append(organization.getOrganizationCode())
					.append(ApplicationConstants.SEPERATOR)
					.append(ApplicationConstants.TXN_TYPE_CUSTOMER_CREDIT)
					.append(ApplicationConstants.SEPERATOR)
					.append(formattedDate)
					.append(ApplicationConstants.SEPERATOR)
					.append(newInvoiceNo);
		}
		session.close();

		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo genertaed for customer credit is: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo.toString();
	}

	/**
	 * This method is responsible to generate invoice no for
	 * {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateCustomerAdvanceInfoInvoiceNumber(
			VbOrganization organization) {
		Session session = this.getSession();
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
						"SELECT vb.invoiceNo FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN (SELECT MAX(vbdn.createdOn) FROM VbCustomerAdvanceInfo vbdn "
								+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("invoiceNo", "%AD%");
		String latestInvoiceNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_ADVANCE))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		if (latestInvoiceNo == null) {
			if (invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(ApplicationConstants.SEPERATOR)
						.append(ApplicationConstants.TXN_TYPE_ADVANCE)
						.append(ApplicationConstants.SEPERATOR)
						.append(formattedDate)
						.append(ApplicationConstants.SEPERATOR)
						.append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(ApplicationConstants.SEPERATOR)
						.append(ApplicationConstants.TXN_TYPE_ADVANCE)
						.append(ApplicationConstants.SEPERATOR)
						.append(formattedDate)
						.append(ApplicationConstants.SEPERATOR).append("1");
			}
		} else {
			String[] advanceInfoInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = advanceInfoInvoiceNo[0].split(ApplicationConstants.SEPERATOR);
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
					.append(ApplicationConstants.SEPERATOR)
					.append(ApplicationConstants.TXN_TYPE_ADVANCE)
					.append(ApplicationConstants.SEPERATOR)
					.append(formattedDate)
					.append(ApplicationConstants.SEPERATOR)
					.append(newInvoiceNo);
		}
		session.close();

		if (_logger.isInfoEnabled()) {
			_logger.info("InvoiceNo generated for customer advance is: {}", generatedInvoiceNo);
		}
		return generatedInvoiceNo.toString();
	}

	/**
	 * This method is responsible to get all the product allotted to a sales
	 * executive.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organizationId - {@link Integer}
	 * @return productsResponseList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerProductsCost> getCustomerProductsCost(
			String salesExecutive, Integer organizationId) {
		Session session = this.getSession();
		VbOrganization organization = getOrganization(session, organizationId);
		VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		List<VbEmployeeCustomer> employeeCustomer = session.createCriteria(VbEmployeeCustomer.class)
				.add(Restrictions.eq("vbEmployee", employee))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		Float productCost = null;
		String productName = null;
		String batchNumber = null;
		VbProduct product = null;
		CustomerProductsCost response = null;
		List<CustomerProductsCost> customerProductsCostList = new ArrayList<CustomerProductsCost>();
		for (VbEmployeeCustomer customer : employeeCustomer) {
			List<VbSalesBookProducts> salesBookProducts = session.createCriteria(VbSalesBookProducts.class)
					.add(Restrictions.eq("vbSalesBook", salesBook))
					.list();
			for (VbSalesBookProducts vbSalesBookProducts : salesBookProducts) {
				productName = vbSalesBookProducts.getProductName();
				batchNumber = vbSalesBookProducts.getBatchNumber();
				product = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Restrictions.eq("productName", productName))
						.add(Restrictions.eq("batchNumber", batchNumber))
						.add(Restrictions.eq("vbOrganization", organization))
						.uniqueResult();
				productCost = (Float) session.createCriteria(VbProductCustomerCost.class)
						.add(Restrictions.eq("vbProduct", product))
						.add(Restrictions.eq("vbCustomer", customer.getVbCustomer()))
						.add(Restrictions.eq("vbOrganization", organization))
						.setProjection(Projections.property("cost"))
						.uniqueResult();
				if (productCost == null || productCost == 0) {
					productCost = product.getCostPerQuantity();
				}
				
				response = new CustomerProductsCost();
				response.setCustId(String.valueOf(customer.getVbCustomer().getId()));
				response.setProductId(String.valueOf(product.getId()));
				response.setCost(String.valueOf(productCost));
				
				customerProductsCostList.add(response);
			}
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("{} business names have been found for {}", customerProductsCostList.size(), salesExecutive);
		}
		return customerProductsCostList;
	}
}