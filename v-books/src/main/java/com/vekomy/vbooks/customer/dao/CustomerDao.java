/**
 * com.vekomy.vbooks.customer.dao.CustomerDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand;
import com.vekomy.vbooks.customer.command.CustomerCommand;
import com.vekomy.vbooks.customer.command.CustomerResult;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * @author Sudhakar
 * 
 * 
 */
public class CustomerDao extends BaseDao {

	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**
	 * This method is responsible for saving customer basic and additional
	 * details.
	 * 
	 * @param customer
	 * @param customerDetail
	 * @param customerCreditCommand
	 * @param organization
	 * @param createdBy
	 * @return isSaved
	 */
	public Boolean saveCustomer(CustomerCommand customer,
			VbCustomerDetail customerDetail, VbOrganization organization,
			String createdBy) {
		_logger.debug("CustomerCommand: {}", customer);
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbCustomer vbCustomer = new VbCustomer();
		Boolean isSaved = Boolean.FALSE;
		Date createdDate = new Date();
		if (vbCustomer != null) {
			vbCustomer.setCustomerName(customer.getCustomerName());
			vbCustomer.setBusinessName(customer.getBusinessName());
			vbCustomer.setInvoiceName(customer.getInvoiceName());
			vbCustomer.setGender(customer.getGender());
			vbCustomer.setCreditLimit(customer.getCreditLimit());
			vbCustomer.setCreditOverdueDays(customer.getCreditOverdueDays());
			vbCustomer.setCreatedBy(createdBy);
			vbCustomer.setCreatedOn(createdDate);
			vbCustomer.setModifiedOn(createdDate);
			vbCustomer.setVbOrganization(organization);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting customer: {}", vbCustomer);
			}
			session.save(vbCustomer);
			isSaved = Boolean.TRUE;
		}
		VbCustomerDetail vbCustomerDetail = new VbCustomerDetail();
		if (vbCustomerDetail != null) {
			vbCustomerDetail.setAddressLine1(customerDetail.getAddressLine1());
			String addressLine2 = customerDetail.getAddressLine2();
			if (!StringUtils.isEmpty(addressLine2)) {
				vbCustomerDetail.setAddressLine2(addressLine2);
			}
			String alternateMobile = customerDetail.getAlternateMobile();
			if (!StringUtils.isEmpty(alternateMobile)) {
				vbCustomerDetail.setAlternateMobile(alternateMobile);
			}
			vbCustomerDetail.setCity(customerDetail.getCity());
			String landMark = customerDetail.getLandmark();
			if (!StringUtils.isEmpty(landMark)) {
				vbCustomerDetail.setLandmark(landMark);
			}
			vbCustomerDetail.setLocality(customerDetail.getLocality());
			vbCustomerDetail.setMobile(customer.getMobile());
			vbCustomerDetail.setZipcode(customerDetail.getZipcode());
			if(customer.getEmail()!=null){
				vbCustomerDetail.setEmail(customer.getEmail());
			}else if(customerDetail.getEmail()!=null){
				vbCustomerDetail.setEmail(customerDetail.getEmail());
			}
			String directLine = customerDetail.getDirectLine();
			if (!StringUtils.isEmpty(directLine)) {
				vbCustomerDetail.setDirectLine(directLine);
			}
			vbCustomerDetail.setState(customerDetail.getState());
			vbCustomerDetail.setRegion(customerDetail.getRegion());
			vbCustomerDetail.setVbCustomer(vbCustomer);
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting CustomerDetail: {}", vbCustomerDetail);
			}

			session.save(vbCustomerDetail);
			isSaved = Boolean.TRUE;
		}

		txn.commit();
		session.close();
		return isSaved;
	}


	/**
	 * This method is responsible to perform search on customer module.
	 * 
	 * @param customerCommand
	 * @return customerDetailsList
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public List<CustomerResult> searchCustomer(CustomerCommand customerCommand, VbOrganization vbOrganization) {

		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerCommand: {}", customerCommand);
		}
		Session session = this.getSession();
		VbCustomerDetail vbCustomerDetail = null;
		Criteria criteria = session.createCriteria(VbCustomer.class);
		if (customerCommand != null) {
			String customerName = customerCommand.getCustomerName();
			String businessName = customerCommand.getBusinessName();
			String invoiceName = customerCommand.getInvoiceName();
			String locality = customerCommand.getLocality();
			Criteria crit=session.createCriteria(VbCustomerDetail.class).createAlias("vbCustomer","customer");
			if(!locality.isEmpty()){
				List<Integer> localities=crit.setProjection(Projections.property("customer.id"))
						                     .add(Restrictions.like("locality", locality, MatchMode.START).ignoreCase())
			                                 .list();
				if(!localities.isEmpty()){
					criteria.add(Restrictions.in("id", localities));
				}
			}
			if (!customerName.isEmpty()) {
				criteria.add(Expression.like("customerName", customerName, MatchMode.START).ignoreCase());
			}
			if (!businessName.isEmpty()) {
				criteria.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase());
			}
			if (!invoiceName.isEmpty()) {
				criteria.add(Expression.like("invoiceName", invoiceName, MatchMode.START).ignoreCase());
			}
		}
		
		if (vbOrganization != null) {
			criteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		criteria.addOrder(Order.asc("businessName"));
		List<VbCustomer> customerList = criteria.list();
		List<CustomerResult> customerDetailsList = new ArrayList<CustomerResult>();
		CustomerResult customerResult = null;
		for (VbCustomer vbCustomer : customerList) {
			customerResult = new CustomerResult();
			customerResult.setId(vbCustomer.getId());
			customerResult.setBusinessName(vbCustomer.getBusinessName());
			customerResult.setVbCustomer(vbCustomer);
			vbCustomerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
					.createAlias("vbCustomer", "customer")
					.add(Expression.eq("customer.id", vbCustomer.getId()))
					.add(Expression.eq("customer.vbOrganization", vbOrganization))
					.uniqueResult();
			customerResult.setMobile(vbCustomerDetail.getMobile());
			customerResult.setLocality(vbCustomerDetail.getLocality());
			if(vbCustomerDetail.getAlternateMobile()!= null){
				customerResult.setAlternateMobile(vbCustomerDetail.getAlternateMobile());
			}
			if(vbCustomerDetail.getEmail()!=null){
				customerResult.setEmail(vbCustomerDetail.getEmail());
			}
			customerResult.setCreditLimit(vbCustomer.getCreditLimit());
			customerResult.setCreditOverdueDays(vbCustomer.getCreditOverdueDays());
			customerDetailsList.add(customerResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerDetailsList: {}", customerDetailsList);
		}
		return customerDetailsList;
	}

	/**
	 * This Method Is Responsible to retrieve the Customer Records Based On Given Id
	 * 
	 * @param id
	 * @param organization
	 * @return instance
	 */
	public VbCustomer getCustomer(int id , VbOrganization organization) {
		Session session = this.getSession();
		VbCustomer instance = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("id", id))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Vbcustomer: {}", instance);
		}
		return instance;
	}

	/**
	 * This Method Is Responisble For Deleting Customer Based on Given Id
	 * 
	 * @param customerCommand
	 * @return
	 */
	public Boolean deleteCustomer(CustomerCommand customerCommand, VbOrganization organization) {
		Boolean isDeleted = Boolean.FALSE;
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbCustomerDetail vbCustomerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.id", customerCommand.getId()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		if (vbCustomerDetail != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Deleting VbCustomerDetail: {}", vbCustomerDetail);
			}
			session.delete(vbCustomerDetail);
			isDeleted = Boolean.TRUE;
		}
		
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, customerCommand.getId());
		if (vbCustomer != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Deleting VbCustomer: {}", vbCustomer);
			}
			session.delete(vbCustomer);
			isDeleted = Boolean.TRUE;
		}
		
		tx.commit();
		session.close();
		return isDeleted;
	}

	/**
	 * This method is responsible to update customer.
	 * 
	 * @param customerCommand
	 * @param organization
	 * @param userName
	 * 
	 */
	public void editCustomer(CustomerCommand customerCommand,
			VbCustomerDetail vbCustomerDetail2, VbOrganization organization,
			String userName) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		VbCustomer vbCustomer = (VbCustomer) session.get(VbCustomer.class, customerCommand.getId());

		if (vbCustomer != null) {
			vbCustomer.setCustomerName(customerCommand.getCustomerName());
			vbCustomer.setBusinessName(customerCommand.getBusinessName());
			vbCustomer.setInvoiceName(customerCommand.getInvoiceName());
			vbCustomer.setModifiedBy(userName);
			vbCustomer.setCreatedBy(userName);
			Date modifiedDate = new Date();
			vbCustomer.setModifiedOn(modifiedDate);
			vbCustomer.setVbOrganization(organization);
			vbCustomer.setGender(customerCommand.getGender());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomer: {}", vbCustomer);
			}
			session.saveOrUpdate(vbCustomer);
		} else {
			if (_logger.isErrorEnabled()) {
				_logger.error("Customer Not Found to updated.");
			}
		}

		VbCustomerDetail vbCustomerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.id", customerCommand.getId()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		if (vbCustomerDetail != null) {
			vbCustomerDetail.setVbCustomer(vbCustomer);
			vbCustomerDetail.setAddressLine1(vbCustomerDetail2.getAddressLine1());
			vbCustomerDetail.setAddressLine2(vbCustomerDetail2.getAddressLine2());
			vbCustomerDetail.setAlternateMobile(vbCustomerDetail2.getAlternateMobile());
			vbCustomerDetail.setCity(vbCustomerDetail2.getCity());
			vbCustomerDetail.setLandmark(vbCustomerDetail2.getLandmark());
			vbCustomerDetail.setLocality(vbCustomerDetail2.getLocality());
			vbCustomerDetail.setZipcode(vbCustomerDetail2.getZipcode());
			vbCustomerDetail.setState(vbCustomerDetail2.getState());
			vbCustomerDetail.setEmail(customerCommand.getEmail());
			vbCustomerDetail.setDirectLine(vbCustomerDetail2.getDirectLine());
			vbCustomerDetail.setRegion(vbCustomerDetail2.getRegion());
			vbCustomerDetail.setMobile(customerCommand.getMobile());

			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbCustomerDetail: {}", vbCustomerDetail);
			}
			session.saveOrUpdate(vbCustomerDetail);
		}
		tx.commit();
		session.close();
	}

	/**
	 * This Method is Responsible to Check whether The Entered BusinessName is
	 * Duplicate Or Not
	 * 
	 * @param customerCommand
	 * @return available
	 */
	@SuppressWarnings({"unchecked" })
	public String validateBusinessName(CustomerCommand customerCommand, VbOrganization organization) {
		String isAvailable = "y";
		Session session = this.getSession();
		List<VbCustomer> customerList = session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", customerCommand.getBusinessName()))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		if(!customerList.isEmpty()){
			isAvailable = "n";
		}
		session.close();
		return isAvailable;
	}

	public VbCustomerDetail getCustomerDetails(CustomerChangeRequestCommand customerCrCommand, VbOrganization organization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("CustomerChangeRequestCommand: {}", customerCrCommand);
		}
		Session session = this.getSession();
		VbCustomerDetail customerDetail = (VbCustomerDetail) session.createCriteria(VbCustomerDetail.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.businessName", customerCrCommand.getBusinessName()))
				.add(Expression.eq("customer.customerName", customerCrCommand.getCustomerName()))
				.add(Expression.eq("customer.gender", customerCrCommand.getGender()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		if(customerDetail == null){
			return null;
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("customerDetail: {}", customerDetail);
		}
		return customerDetail;
	}


	/**
	 * This Method Is To Retrieve The FullName Based On Given businessName.
	 * @param businessName
	 * @return
	 */
	public String getFullname(String businessName, VbOrganization organization) {
		Session session=this.getSession();
		String customerName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("customerName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(customerName == null){
			if (_logger.isErrorEnabled()) {
				_logger.error("Records not found for FullName: {}", customerName);
			}
			return null;
			
		}
		
		if (_logger.isErrorEnabled()) {
			_logger.error("Retrived CustomerName For businessName is: {}", customerName);
		}
		return customerName;
		
	}

	/**This ethod Is Responsible For Displaying all BusinessNames In ascendingOrder.
	 * @param businessName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomer> getBusinessName(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(VbCustomer.class)
				.add(Expression.eq("vbOrganization", organization))
				.addOrder(Order.asc("businessName"));
		List<VbCustomer> businessNameList = criteria.list();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} business names avialable for the organization: {}", businessNameList.size(), organization.getName());
		}
		return businessNameList;
	}

	
	public VbCustomer getVbCustomer(String businessName, VbOrganization organization) {
		Session session = this.getSession();
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization)).uniqueResult();
		return customer;
	}
}