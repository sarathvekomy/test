package com.vekomy.vbooks.organization.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.customer.dao.CustomerDao;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;

/**
 * @author priyanka
 *
 */
public class SyStemDefaultsDao extends BaseDao{
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**This Method Is Responsible For Adding PaymentTypes
	 * @param val
	 * @param type
	 * @param description
	 * @param userName
	 * @param organization
	 */
	public void addPaymentTypes(String val, String description, String userName, VbOrganization organization) {
		Session session=this.getSession();
		Transaction txn = session.beginTransaction();
		Date date=new Date();
		VbPaymentTypes vbPaymentTypes = new VbPaymentTypes();
		session.createCriteria(VbPaymentTypes.class);
		vbPaymentTypes.setPaymentType(val);
		vbPaymentTypes.setCreatedBy(userName);
		vbPaymentTypes.setCreatedOn(date);
		vbPaymentTypes.setModifiedBy(userName);
		vbPaymentTypes.setDescription(description);
		vbPaymentTypes.setModifiedOn(date);
		vbPaymentTypes.setVbOrganization(organization);
		session.save(vbPaymentTypes);
		txn.commit();
		session.close();
		
	}

	/**This Method Is To Retrieve All Existed PaymentTypes
	 * @param organization
	 * @return {@link paymentTypeList}
	 */ 
	@SuppressWarnings("unchecked")
	public List<String> getAllPaymentTypes(VbOrganization organization) {
		Session session=this.getSession();
		List<String> paymentType =(List<String>) session
				.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("vbOrganization",organization))
				.setProjection(Projections.property("paymentType"))
				.addOrder(Order.asc("paymentType"))
				.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving PaymentTypes: {}", paymentType);
		}
		return paymentType;
	}

	/**This Method is Responsible For Retrieving All Existed Employee Types
	 * @param organization
	 * @return employeeTypes
	 */
	public List<String> getAllEmployeeTypes(VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<String>employeeTypeList  = (List<String>)session
		.createCriteria(VbRole.class)
		.setProjection(Projections.property("description"))
			.addOrder(Order.asc("description"))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving EmployeeTypes: {}", employeeTypeList);
		}
		return employeeTypeList;
	}

	/**
	 * This Method Is Responsible for Retrieving All Existing Address Types
	 * 
	 * @param organization
	 * @return addressTypeList
	 */
	public List<VbAddressTypes> getAllAddressTypes(VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<VbAddressTypes>addressTypeList= (List<VbAddressTypes>)session
				.createCriteria(VbAddressTypes.class)
		.add(Restrictions.eq("vbOrganization", organization))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving AddressTypes: {}", addressTypeList);
		}
		return addressTypeList;	
	}

	/**
	 * This Method Is To Add AddressType Values
	 * 
	 * @param val
	 * @param description
	 * @param organization
	 * @param username
	 */
	public void addAddressTypes(String val, String description,
			VbOrganization organization, String username) {
		Session session=this.getSession();
		Transaction tx =session.beginTransaction();
		Date date = new Date();
		session.createCriteria(VbAddressTypes.class);
		VbAddressTypes addressTypes = new VbAddressTypes();
		addressTypes.setCreatedBy(username);
		addressTypes.setCreatedOn(date);
		addressTypes.setDescription(description);
		addressTypes.setAddressType(val);
		addressTypes.setVbOrganization(organization);
		addressTypes.setModifiedBy(username);
		addressTypes.setModifiedOn(date);
		if(addressTypes != null){
		session.save(addressTypes);
		}
		tx.commit();
		session.close();
		
	}

	/**
	 * This Method Is to Add EmployeeTypes
	 * 
	 * @param value
	 * @param description
	 * @param organization
	 * @param username
	 */
	public void addemployeeTypes(String value, String description,
			VbOrganization organization, String username) {
		
		Session session=this.getSession();		
		Transaction tx = session.beginTransaction();
		session.createCriteria(VbRole.class);
		VbRole vbRole=new VbRole();
		vbRole.setDescription(description);
		vbRole.setRoleName(value);
		if(vbRole != null){
			session.save(vbRole);
		}
		tx.commit();
		session.close();
		
	}

	/**
	 * This Method Is To delete PaymentTypes Based on Given Id
	 * 
	 * @param id
	 * @param organization
	 */
	public  void deletePaymentTypes(String value, VbOrganization organization) {
		
		Session session = this.getSession();
		Transaction txn = session.beginTransaction(); 
		VbPaymentTypes vbPaymentTypes=
		(VbPaymentTypes) session.createCriteria(VbPaymentTypes.class)
		.add(Restrictions.eq("paymentType",value))
		.add(Restrictions.eq("vbOrganization",organization))
		.uniqueResult();
		if(vbPaymentTypes != null){
			session.delete(vbPaymentTypes);
		}
		txn.commit();
		session.close();
		
	}

	/**
	 * This Method Is To Delete EmployeeType Based On THe Given id
	 * 
	 * @param desc
	 * @param organization
	 */
	public void deleteEmployeeTypes(String desc, VbOrganization organization) {
		Session session =this.getSession();
		Transaction txn = session.beginTransaction();
		VbRole vbRole=(VbRole)session.createCriteria(VbRole.class)
		.add(Restrictions.eq("description",desc))
		.uniqueResult();
		if(vbRole != null){
			session.delete(vbRole);
		}
		txn.commit();
		session.close();
	}

	/**
	 * This Method Is Responsible To AddressTypes
	 * 
	 * @param id
	 * @param organization
	 */
	public void deleteAddressTypes(String description, VbOrganization organization) {
		Session session =this.getSession();
		Transaction txn = session.beginTransaction();
		VbAddressTypes vbAddressTypes=(VbAddressTypes)session.createCriteria(VbAddressTypes.class)
		.add(Restrictions.eq("addressType",description))
		.add(Restrictions.eq("vbOrganization", organization))
		.uniqueResult();
		if(vbAddressTypes != null){
			session.delete(vbAddressTypes);
		}
		txn.commit();
		session.close();
		
	}
	/**
	 * This Method Is To Retrieve EmployeeType Based On Given Id
	 * 
	 * @param id
	 * @return vbRole
	 */
	public VbRole getEmployeetypeById(int id){
		Session session = this.getSession();
		VbRole vbRole = (VbRole)session.get(VbRole.class,id);
		if(vbRole != null){
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving EmployeeType by Id: {}", vbRole);
			}
			return vbRole;
		}else {
			return null;
		}
		
		
	}

	/**
	 * This Method Is To Retrieve AddressTypes Based On Given Id
	 * 
	 * @param id
	 * @return {@link VbAddressTypes}
	 */
	public VbAddressTypes getAddTypeById(int id){
		Session session = this.getSession();
		VbAddressTypes vbAddressTypes = (VbAddressTypes)session.get(VbAddressTypes.class,id);
		if(vbAddressTypes != null){
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving AddressTypes by Id: {}", vbAddressTypes);
			}
			return vbAddressTypes;
		}else {
			return null;
		}
		
	}
	/**
	 * This Method Is To Retrieve PaymentTypes Based On Given Id
	 * 
	 * @param id
	 * @return {@link VbPaymentTypes}
	 */
	public VbPaymentTypes getPayTypeById(int id){
		Session session = this.getSession();
		VbPaymentTypes vbPaymentTypes = (VbPaymentTypes)session.get(VbPaymentTypes.class,id);
		if(vbPaymentTypes != null){
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving paymentTypes by Id: {}", vbPaymentTypes);
			}
			return vbPaymentTypes;
		}else {
			return null;
		}
		
	}
	/*
	 * *This Method is Responsible To update EmployeeTypes Based On The Given id
	 *
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 */
	public void updateEmployeeTypes(int id, VbOrganization organization, String value, String description) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbRole vbRole=(VbRole)session.createCriteria(VbRole.class)
				.add(Restrictions.eq("id",id))
			//	.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbRole !=null){
			vbRole.setRoleName(value);
			vbRole.setDescription(description);
			session.update(vbRole);
		}
		txn.commit();
		session.close();
		
	}

	/**
	 * This Method is Responsible to Update AddressTypes Based On Given id
	 *
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @param username
	 */
	public void updateAddressTypes(int id, VbOrganization organization,
			String value, String description, String username) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		VbAddressTypes vbAddressTypes=(VbAddressTypes)session.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("id",id))
			//	.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbAddressTypes !=null){
			vbAddressTypes.setAddressType(value);
			vbAddressTypes.setDescription(description);
			vbAddressTypes.setCreatedBy(username);
			vbAddressTypes.setCreatedOn(date);
			vbAddressTypes.setModifiedBy(username);
			vbAddressTypes.setModifiedOn(date);
			session.update(vbAddressTypes);
		}
		txn.commit();
		session.close();
		
	}

	/**
	 * This Method Is Responsible For Updating Payment Types
	 * 
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @param username
	 */
	public void updatePayTypes(int id, VbOrganization organization,
			String value, String description, String username) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		Date date = new Date();
		VbPaymentTypes vbPaymentTypes = (VbPaymentTypes)session.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("id",id))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbPaymentTypes !=null){
			vbPaymentTypes.setPaymentType(value);
			vbPaymentTypes.setDescription(description);
			vbPaymentTypes.setCreatedBy(username);
			vbPaymentTypes.setCreatedOn(date);
			vbPaymentTypes.setModifiedBy(username);
			vbPaymentTypes.setModifiedOn(date);
			session.update(vbPaymentTypes);
		}
		txn.commit();
		session.close();
		
	}

	/**
	 * This Method is Responsible For Retrieving Employee id From vbRole Based on Description
	 *
	 * @param description
	 * @param organization
	 * @return id
	 */
	public int getEmpId(String description, VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer)session.createCriteria(VbRole.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("description", description)).uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving Employee Id: {}", id);
		}
		return id;
	}

	/**
	 * This Method Is Responsible for validating whether the given AddressType is Already Present Or not
	 * 
	 * @param value
	 * @param organization
	 * @return {@link VbAddressTypes}
	 */
	public List<VbAddressTypes> validateAddressType(String value,
			VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<VbAddressTypes> vbAddressTypesList =(List<VbAddressTypes>)session.createCriteria(VbAddressTypes.class)
		.add(Restrictions.eq("addressType", value).ignoreCase())
		.add(Restrictions.eq("vbOrganization", organization))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("validating AddressTypes by Id: {}", vbAddressTypesList);
		}
		return vbAddressTypesList;
	}

	/**
	 * This Method Is Responsible for validating whether the given PaymentType is Already Present Or not
	 *
	 * @param value
	 * @param organization
	 * @return {@link VbPaymentTypes}
	 */
	public List<VbPaymentTypes> validatePaymentType(String value,
			VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<VbPaymentTypes> vbPaymentTypes =(List<VbPaymentTypes>)session.createCriteria(VbPaymentTypes.class)
		.add(Restrictions.eq("paymentType", value).ignoreCase())
		.add(Restrictions.eq("vbOrganization", organization))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("validating paymentTypes by Id: {}", vbPaymentTypes);
		}
		return vbPaymentTypes;
	}

	/**
	 * This Method Is Responsible for validating whether the given employeetype is Already Present Or not
	 * 
	 * @param value
	 * @param organization
	 * @return {@link VbRole}
	 */
	public List<VbRole> validateEmployeeType(String value,
			VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<VbRole> vbRole =(List<VbRole>)session.createCriteria(VbRole.class)
		.add(Restrictions.eq("description", value).ignoreCase())
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("validating EmployeeTypes by Id: {}", vbRole);
		}
		return vbRole;
	}

	/**
	 * This Method Is Responsible To Retrieve AddressTypes
	 * 
	 * @param organization
	 * @return addressTypes
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCrAddressTypes(VbOrganization organization) {
		Session session=this.getSession();
	List<String>addressTypes=session.createCriteria(VbAddressTypes.class)
		.add(Restrictions.eq("vbOrganization", organization))
		.setProjection(Projections.property("addressType"))
			.addOrder(Order.asc("addressType"))
		.list();
	return addressTypes;
		
	}

	/**
	 * This Method Is To Retrieve Particular Id Based On AddressType
	 * 
	 * @param value
	 * @param organization
	 * @return
	 */
	public int getAddId(String value, VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer)session.createCriteria(VbAddressTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("addressType", value)).uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving Employee Id: {}", id);
		}
		return id;
	}

	/**
	 *  This Method Is To Retrieve Particular Id Based On PaymentType
	 *  
	 * @param value
	 * @param organization
	 * @return
	 */
	public int getPayId(String value, VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer)session.createCriteria(VbPaymentTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("paymentType", value)).
				add(Restrictions.eq("vbOrganization", organization)).
				uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving PaymentType Id: {}", id);
		}
		return id;
	}

	public List<String> getJournalTypes(VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<String>journalTypeList  = (List<String>)session
		.createCriteria(VbJournalTypes.class)
		.setProjection(Projections.property("journalType"))
		.add(Restrictions.eq("vbOrganization", organization))
			.addOrder(Order.asc("journalType"))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving JournalTypes: {}", journalTypeList);
		}
		return journalTypeList;
	}

	public void addJournalTypes(String value, String description,VbOrganization organization, String username, String invoiceNo) {
		Session session=this.getSession();		
		Date date = new Date();
		Transaction tx = session.beginTransaction();
		session.createCriteria(VbJournalTypes.class);
		VbJournalTypes vbJournalTypes=new VbJournalTypes();
		vbJournalTypes.setDescription(description);
		vbJournalTypes.setJournalType(value);
		vbJournalTypes.setInvoiceNo(invoiceNo.toUpperCase());
		vbJournalTypes.setVbOrganization(organization);
		vbJournalTypes.setModifiedOn(date);
		vbJournalTypes.setModifiedBy(username);
		vbJournalTypes.setCreatedBy(username);
		vbJournalTypes.setCreatedOn(date);
		if(vbJournalTypes != null){
			session.save(vbJournalTypes);
		}
		tx.commit();
		session.close();
	}

	public int getJournalId(String value, VbOrganization organization) {
		Session session = this.getSession();
		Integer id = (Integer)session.createCriteria(VbJournalTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("journalType", value)).uniqueResult();
		if (_logger.isDebugEnabled()) {
			_logger.debug("Retrieving JournalType Id: {}", id);
		}
		return id;
	}
	public VbJournalTypes getVbJournalTypeByid(int id){
		Session session = this.getSession();
		VbJournalTypes vbJournalTypes = (VbJournalTypes)session.get(VbJournalTypes.class,id);
		if(vbJournalTypes != null){
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving paymentTypes by Id: {}", vbJournalTypes);
			}
			return vbJournalTypes;
		}else {
			return null;
		}
		
		
	}

	public void updateJournalTypes(int id, VbOrganization organization,
			String value, String description, String invoiceNo) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbJournalTypes vbJournalTypes=(VbJournalTypes)session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("id",id))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		if(vbJournalTypes !=null){
			vbJournalTypes.setJournalType(value);
			vbJournalTypes.setDescription(description);
			vbJournalTypes.setInvoiceNo(invoiceNo);
			session.update(vbJournalTypes);
		}
		txn.commit();
		session.close();
		
	}

	public void deleteJournalTypes(String value, VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction(); 
		VbJournalTypes vbJournalTypes=
		(VbJournalTypes) session.createCriteria(VbJournalTypes.class)
		.add(Restrictions.eq("journalType",value))
		.add(Restrictions.eq("vbOrganization",organization))
		.uniqueResult();
		if(vbJournalTypes != null){
			session.delete(vbJournalTypes);
		}
		txn.commit();
		session.close();
	}

	public List<VbJournalTypes> validateJournalTypes(String value,
			VbOrganization organization) {
		Session session=this.getSession();
		@SuppressWarnings("unchecked")
		List<VbJournalTypes> vbJournalTypes =(List<VbJournalTypes>)session.createCriteria(VbJournalTypes.class)
		.add(Restrictions.eq("journalType", value).ignoreCase())
		.add(Restrictions.eq("vbOrganization", organization))
		.list();
		if (_logger.isDebugEnabled()) {
			_logger.debug("validating JournalTypes by Id: {}", vbJournalTypes);
		}
		return vbJournalTypes;
	}

}
