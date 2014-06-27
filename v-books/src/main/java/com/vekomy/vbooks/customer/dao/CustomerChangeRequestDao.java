/**
 * com.vekomy.vbooks.customer.controller.CustomerChangeRequestController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 9, 2013
 */
package com.vekomy.vbooks.customer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequestDetails;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.util.CRStatus;

/**
 * This Class Is Responsible To Perform Database-Related Operations.
 * 
 * @author priyanka
 *
 */
public class CustomerChangeRequestDao extends BaseDao {
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**
	 * This Method is Responsible for Retrieving The CustomerDetails In Case Of CustomerChangeRequest.
	 * 
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @return customerDetail - {@link VbCustomerDetail}
	 */
	public VbCustomerDetail getCustomerDetails(
			CustomerChangeRequestCommand customerCrCommand) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerChangeRequestCommand: {}", customerCrCommand);
		}
		Session session = this.getSession();
		VbCustomerDetail customerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.businessName", customerCrCommand.getBusinessName()))
				.add(Expression.eq("customer.customerName", customerCrCommand.getCustomerName()))
				.add(Expression.eq("customer.gender", customerCrCommand.getGender()))
				.uniqueResult();;
		if(customerDetail == null){
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found.");
			}
			return null;
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("customerDetail: {}", customerDetail);
		}
		return customerDetail;
	}
	
	/**
	 * This Method is Responsible For Saving CustomerChangeRequest Details.
	 * 
	 * @param customer - {@link CustomerChangeRequestCommand}
	 * @param customerChangeRequestCommand - {@link CustomerChangeRequestCommand}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return isSaved - {@link Boolean}
	 */
	public Boolean saveCustomerCrDetails(CustomerChangeRequestCommand customer,
			CustomerChangeRequestCommand customerChangeRequestCommand,
			VbOrganization organization, String username) {
		_logger.debug("CustomerCommand: {}", customer);
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbCustomerChangeRequest vbCustomer = new VbCustomerChangeRequest();
		Boolean isSaved = Boolean.FALSE;
		Date createdDate = new Date();
		if (vbCustomer != null) {
			vbCustomer.setBusinessName(customer.getBusinessName());
			vbCustomer.setCrType(customer.getCrType());
			vbCustomer.setGender(customer.getGender());
			vbCustomer.setVbOrganization(organization);
			vbCustomer.setCreatedOn(createdDate);
			vbCustomer.setCreatedBy(username);
			vbCustomer.setModifiedOn(new Date());
			vbCustomer.setStatus(CRStatus.PENDING.name());
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting customer: {}", vbCustomer);
			}
			session.save(vbCustomer);
			isSaved = Boolean.TRUE;
		}
		VbCustomerChangeRequestDetails vbCustomerCrDetail = new VbCustomerChangeRequestDetails();
		if (vbCustomerCrDetail != null) {
			vbCustomerCrDetail.setCustomerName(customer.getCustomerName());

			vbCustomerCrDetail.setInvoiceName(customerChangeRequestCommand.getInvoiceName());
			vbCustomerCrDetail.setAddressLine1(customerChangeRequestCommand.getAddressLine1());
			String addressLine2 = customerChangeRequestCommand.getAddressLine2();
			if (!StringUtils.isEmpty(addressLine2)) {
				vbCustomerCrDetail.setAddressLine2(addressLine2);
			}
			vbCustomerCrDetail.setCity(customerChangeRequestCommand.getCity());
			String landMark = customerChangeRequestCommand.getLandmark();
			if (!StringUtils.isEmpty(landMark)) {
				vbCustomerCrDetail.setLandmark(landMark);
			}
			vbCustomerCrDetail.setLocality(customerChangeRequestCommand.getLocality());
			vbCustomerCrDetail.setMobile(customerChangeRequestCommand.getMobile());
			vbCustomerCrDetail.setZipcode(customerChangeRequestCommand.getZipcode());
			vbCustomerCrDetail.setEmail(customerChangeRequestCommand.getEmail());
			if (!StringUtils.isEmpty(customerChangeRequestCommand.getDirectLine())) {
				vbCustomerCrDetail.setDirectLine(customerChangeRequestCommand.getDirectLine());
			}
			if(!StringUtils.isEmpty(customerChangeRequestCommand.getRegion())){
				vbCustomerCrDetail.setRegion(customerChangeRequestCommand.getRegion());
			}
			if(!StringUtils.isEmpty(customerChangeRequestCommand.getAlternateMobile())){
				vbCustomerCrDetail.setAlternateMobile(customerChangeRequestCommand.getAlternateMobile());
			}
			vbCustomerCrDetail.setState(customerChangeRequestCommand.getState());
			vbCustomerCrDetail.setAddressType(customerChangeRequestCommand.getAddressType());
			vbCustomerCrDetail.setCreatedBy(customerChangeRequestCommand.getCreatedBy());
			vbCustomerCrDetail.setCreatedDate(createdDate);
			vbCustomerCrDetail.setModifiedDate(createdDate);
			vbCustomerCrDetail.setModifiedBy(username);
			vbCustomerCrDetail.setVbCustomerChangeRequest(vbCustomer);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting CustomerDetail: {}", vbCustomerCrDetail);
			}

			session.save(vbCustomerCrDetail);
			isSaved = Boolean.TRUE;
		}
		txn.commit();
		session.close();
		return isSaved;

	}


	/**
	 * This Method Is Responsible to display All Records From VbCustomerChangerequestTable On Page Load.
	 * 
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @return customerDetailsList - {@link List}
	 */ 
	@SuppressWarnings({ "unchecked" })
	public List<CustomerResult> getCrDetails(CustomerChangeRequestCommand customerCrCommand, VbOrganization organization) {
		Session session=this.getSession();
		CustomerResult customerResult = null;
		List<VbCustomerChangeRequestDetails> crDetailsList = session.createCriteria(VbCustomerChangeRequestDetails.class)
				.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
				.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
				.add(Expression.eq("customerChangeRequest.status", CRStatus.PENDING.name()))
				.list();
		List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
		for (VbCustomerChangeRequestDetails customerChangeRequestDetails : crDetailsList) {
			VbCustomerChangeRequest crRequest = customerChangeRequestDetails.getVbCustomerChangeRequest();
			customerResult = new CustomerResult();
			customerResult.setId(crRequest.getId());
			customerResult.setBusinessName(crRequest.getBusinessName());
			customerResult.setCreatedBy(crRequest.getCreatedBy());
			customerResult.setLocality(customerChangeRequestDetails.getLocality());
			Boolean crType = crRequest.getCrType();
			if(crType == Boolean.TRUE){
				customerResult.setCrType("Existed Customer");
			} else {
				customerResult.setCrType("New Customer");
			}
			customerResult.setMobile(customerChangeRequestDetails.getMobile());
			String alternateMobile = customerChangeRequestDetails.getAlternateMobile(); 
			if(!StringUtils.isEmpty(alternateMobile)){
				customerResult.setAlternateMobile(alternateMobile);
			}
			String email = customerChangeRequestDetails.getEmail();
			if(!StringUtils.isEmpty(email)){
				customerResult.setEmail(email);
			}
			customerDetailsList.add(customerResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerDetailsList: {}", customerDetailsList);
		}
		return customerDetailsList;
		
	}


/**
 * This Method is To Approve The Customer Based On crType.and To Remove Data From Temp Tables.
 * 
 * @param id 
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
 */
public void approveOrDeclineCustomerCr(int id, String status, VbOrganization organization, String userName,CustomerChangeRequestCommand customerCrCommand) {
		Session session=this.getSession();
		Transaction txn = session.beginTransaction();
		String creditLimit=customerCrCommand.getCreditLimit();
		String creditOverdues=customerCrCommand.getCreditOverdueDays();
		Date date =new Date();
		VbCustomerChangeRequestDetails changeRequestDetails = (VbCustomerChangeRequestDetails) session.createCriteria(VbCustomerChangeRequestDetails.class)
				.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
				.add(Expression.eq("customerChangeRequest.id", id))
				.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
				.uniqueResult();
		VbCustomerChangeRequest customerChangeRequest = changeRequestDetails.getVbCustomerChangeRequest();
		if(customerChangeRequest != null) {
			customerChangeRequest.setModifiedBy(userName);
			customerChangeRequest.setModifiedOn(date);
			
			if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
				customerChangeRequest.setStatus(CRStatus.APPROVED.name());
				
			    if(customerChangeRequest.getCrType() == Boolean.TRUE){
			    	// Updating existing customer's CR
			    	String businessName=customerChangeRequest.getBusinessName();
			    	VbCustomerDetail customerDetails = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
			    			.createAlias("vbCustomer", "customer")
			    			.add(Expression.eq("customer.businessName", businessName))
			    			.add(Expression.eq("customer.vbOrganization", organization))
			    			.uniqueResult();
			    	VbCustomer customer = customerDetails.getVbCustomer();
			    	if(customerDetails!=null){
			    		customer.setInvoiceName(changeRequestDetails.getInvoiceName());
			    		customer.setCustomerName(changeRequestDetails.getCustomerName());
			    		customer.setVbOrganization(organization);
		                customer.setBusinessName(customerChangeRequest.getBusinessName());
		                customer.setCreditLimit(Float.parseFloat(creditLimit));
		                customer.setCreditOverdueDays(Integer.parseInt(creditOverdues));
		                customer.setModifiedOn(date);
		                customer.setCreatedOn(date);
			    		customerDetails.setAddressLine1(changeRequestDetails.getAddressLine1());
			    		customerDetails.setAddressLine2(changeRequestDetails.getAddressLine2());
			    		customerDetails.setCity(changeRequestDetails.getCity());
			    		customerDetails.setEmail(changeRequestDetails.getEmail());
			    		if(changeRequestDetails.getLandmark()!=null){
			    			customerDetails.setLandmark(changeRequestDetails.getLandmark());
			    		}if(changeRequestDetails.getAlternateMobile()!=null){
			    			customerDetails.setAlternateMobile(changeRequestDetails.getAlternateMobile());
			    		}
			    		customerDetails.setLocality(changeRequestDetails.getLocality());
			    		customerDetails.setMobile(changeRequestDetails.getMobile());
			    		customerDetails.setZipcode(changeRequestDetails.getZipcode());
			    		customerDetails.setDirectLine(changeRequestDetails.getDirectLine());
			    		customerDetails.setState(changeRequestDetails.getState());
			    		
			    		if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbCustomer: {}", customer);
						}
						session.update(customer);
						session.update(customerDetails);
						session.update(customerChangeRequest);
					} 
			    } else if(customerChangeRequest.getCrType() == Boolean.FALSE){
			    	// Adding new customer's CR
			    	VbCustomerDetail customerDetails=new VbCustomerDetail();
			    	VbCustomer customer = new VbCustomer();
			    	if(customerDetails!=null){
			    		customer.setBusinessName(customerChangeRequest.getBusinessName());
			    		customer.setInvoiceName(changeRequestDetails.getInvoiceName());
			    		customer.setCustomerName(changeRequestDetails.getCustomerName());
			    		customer.setGender(customerChangeRequest.getGender());
			    		customer.setCreatedBy(userName);
			    		customer.setModifiedBy(userName);
			    		customer.setCreatedOn(date);
			    		customer.setModifiedOn(date);
			    		customer.setVbOrganization(organization);
			    		customerDetails.setAddressLine1(changeRequestDetails.getAddressLine1());
			    		customerDetails.setAddressLine2(changeRequestDetails.getAddressLine2());
			    		customerDetails.setAlternateMobile(changeRequestDetails.getAlternateMobile());
			    		customerDetails.setCity(changeRequestDetails.getCity());
			    		customerDetails.setEmail(changeRequestDetails.getEmail());
			    		customerDetails.setRegion(changeRequestDetails.getRegion());
			    		customerDetails.setLandmark(changeRequestDetails.getLandmark());
			    		customerDetails.setLocality(changeRequestDetails.getLocality());
			    		customerDetails.setMobile(changeRequestDetails.getMobile());
			    		customerDetails.setZipcode(changeRequestDetails.getZipcode());
			    		customerDetails.setState(changeRequestDetails.getState());
			    		customerDetails.setVbCustomer(customer);
			    		customer.setCreditLimit(Float.parseFloat(creditLimit));
			    		customer.setCreditOverdueDays(Integer.parseInt(creditOverdues));
			    		if (_logger.isDebugEnabled()) {
							_logger.debug("Saving VbCustomer: {}", customer);
						}
						session.save(customer);
						session.save(customerDetails);
						session.update(customerChangeRequest);
			    }
			    
			    // Assigning customers to Sales executive after approved.
			    VbEmployeeCustomer employeeCustomer = new VbEmployeeCustomer();
			    if(employeeCustomer != null) {
			    	VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
			    			.add(Restrictions.eq("username", customerChangeRequest.getCreatedBy()))
			    			.add(Restrictions.eq("vbOrganization", organization))
			    			.uniqueResult();
			    	employeeCustomer.setVbCustomer(customer);
			    	employeeCustomer.setVbEmployee(employee);
			    	employeeCustomer.setCreatedBy(userName);
			    	employeeCustomer.setCreatedOn(date);
			    	employeeCustomer.setModifiedOn(date);
			    	employeeCustomer.setVbOrganization(organization);
			    	
			    	if(_logger.isDebugEnabled()){
			    		_logger.debug("Persisting VbEmployeeCustomer: {}", employeeCustomer);
			    	}
			    	session.save(employeeCustomer);
			    }
			}
		} else {
			customerChangeRequest.setStatus(CRStatus.DECLINE.name());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating customerChangeRequest: {}", customerChangeRequest);
			}
			session.update(customerChangeRequest);
		}
	}
		
	txn.commit();
    session.close();
 }

	/**
	 * This Method Is Responsible To Search Records Based On The Given Criteria.
	 * @param customerCommand - {@link CustomerChangeRequestCommand}
	 * @return customerDetailsList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerResult> searchCustomerCr(CustomerChangeRequestCommand customerCommand, VbOrganization organization) {
		Session session = this.getSession();
		VbCustomerChangeRequestDetails vbCustomerDetail = null;
		Criteria criteria = session.createCriteria(VbCustomerChangeRequest.class);
		Criteria criteria1=session.createCriteria(VbCustomerChangeRequestDetails.class);
		if (customerCommand != null) {
			String customerName = customerCommand.getCustomerName();
			String businessName = customerCommand.getBusinessName();
			String invoiceName = customerCommand.getInvoiceName();
			if (!customerName.isEmpty()) {
				criteria1.add(Expression.like("customerName", customerName, MatchMode.START).ignoreCase());
			}
			if (!businessName.isEmpty()) {
				criteria.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase());
			}
			if (!invoiceName.isEmpty()) {
				criteria1.add(Expression.like("invoiceName", invoiceName, MatchMode.START).ignoreCase());
			}
		}
		criteria1.addOrder(Order.asc("customerName"));
		List<VbCustomerChangeRequest> customerList = criteria.list();
		List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
		CustomerResult customerResult = null;
		for (VbCustomerChangeRequest customerChangeRequest : customerList) {
			customerResult = new CustomerResult();
			customerResult.setId(customerChangeRequest.getId());
			customerResult.setBusinessName(customerChangeRequest.getBusinessName());
			Boolean crType = customerChangeRequest.getCrType();
			if(crType == Boolean.TRUE){
				customerResult.setCrType("Existed Customer");
			} else {
				customerResult.setCrType("New Customer");
			}
			vbCustomerDetail = (VbCustomerChangeRequestDetails) session.createCriteria(VbCustomerChangeRequestDetails.class)
					.createAlias("vbCustomerChangeRequest", "customerChangeRequest")
					.add(Expression.eq("customerChangeRequest.id", customerChangeRequest.getId()))
					.add(Expression.eq("customerChangeRequest.vbOrganization", organization))
					.uniqueResult();
			customerResult.setMobile(vbCustomerDetail.getMobile());
			if(vbCustomerDetail.getAlternateMobile()!= null){
				customerResult.setAlternateMobile(vbCustomerDetail.getAlternateMobile());
			}
			customerResult.setCreatedBy(customerChangeRequest.getCreatedBy());
			if(vbCustomerDetail.getEmail()!=null){
			customerResult.setEmail(vbCustomerDetail.getEmail());
			}
			customerDetailsList.add(customerResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerDetailsList: {}", customerDetailsList);
		}
		return customerDetailsList;
		
	}
	
	/**This Method is Responsible To retrieve Records Based On the Given Id.
	 * 
	 * @param id
	 * @return instance - {@link VbCustomerChangeRequest}
	 */
	public VbCustomerChangeRequest getCustomerCr(int id , VbOrganization vbOrganization) {
		Session session = this.getSession();
		VbCustomerChangeRequest instance = (VbCustomerChangeRequest) session.createCriteria(VbCustomerChangeRequest.class)
				.add(Expression.eq("id", id))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Vbcustomer: {}", instance);
		}
		return instance;
	}

	/**
	 * This method is responsible for checking unique customer change request type to avoid duplication.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @param customerCrCommand - {@link CustomerChangeRequestCommand}
	 * @return instance - {@link VbCustomerChangeRequest}
	 */
	public VbCustomerChangeRequest checkCustomerCr(VbOrganization vbOrganization,CustomerChangeRequestCommand customerCrCommand) {
		Session session = this.getSession();
		VbCustomerChangeRequest instance = (VbCustomerChangeRequest) session.createCriteria(VbCustomerChangeRequest.class)
				.add(Expression.eq("businessName", customerCrCommand.getBusinessName()))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbCustomerChangeRequest: {}", instance);
		}
		return instance;
		
	}

	/**
	 * This method is responsible to get {@link VbCustomerChangeRequest} based on id.
	 * 
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * 
	 */
	public VbCustomerChangeRequest getVbCustomerChangeRequestById(Integer id) {
		Session session = this.getSession();
		VbCustomerChangeRequest vbCustomerChangeRequest = (VbCustomerChangeRequest) session.get(VbCustomerChangeRequest.class, id);
		session.close();
		return vbCustomerChangeRequest;
	}


}
