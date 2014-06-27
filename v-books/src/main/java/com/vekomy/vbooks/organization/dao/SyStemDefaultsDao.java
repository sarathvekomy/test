/**
 * com.vekomy.vbooks.organization.dao.SyStemDefaultsDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.organization.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.vekomy.vbooks.customer.dao.CustomerDao;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbProductCategories;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.organization.command.SystemDefaultsResult;
import com.vekomy.vbooks.util.DropDownUtil;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author priyanka
 * 
 */
public class SyStemDefaultsDao extends BaseDao {
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(CustomerDao.class);

	/**
	 * This Method Is Responsible For Adding PaymentTypes
	 * 
	 * @param val
	 * @param type
	 * @param description
	 * @param userName
	 * @param organization
	 * @throws DataAccessException 
	 */
	public void addPaymentTypes(String val, String description,
			String userName, VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			VbPaymentTypes vbPaymentTypes = new VbPaymentTypes();
			vbPaymentTypes.setPaymentType(val);
			vbPaymentTypes.setCreatedBy(userName);
			vbPaymentTypes.setCreatedOn(createdOn);
			vbPaymentTypes.setModifiedBy(userName);
			vbPaymentTypes.setDescription(description);
			vbPaymentTypes.setModifiedOn(createdOn);
			vbPaymentTypes.setVbOrganization(organization);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbPaymentTypes");
			}
			session.save(vbPaymentTypes);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This Method Is To Retrieve All Existed PaymentTypes
	 * 
	 * @param organization
	 * @return paymentTypeList
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPaymentTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> paymentTypeList = session.createCriteria(VbPaymentTypes.class)
				.setProjection(Projections.property("paymentType"))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("paymentType"))
				.list();
		session.close();
		
		if(!paymentTypeList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", paymentTypeList.size());
			}
			return paymentTypeList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This Method is Responsible For Retrieving All Existed Employee Types
	 * 
	 * @param organization
	 * @return employeeTypes
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllEmployeeTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> employeeTypeList = session.createCriteria(VbRole.class)
				.setProjection(Projections.property("description"))
				.add(Restrictions.ne("description", OrganizationUtils.ROLE_USER))
				.add(Restrictions.ne("description", OrganizationUtils.ROLE_SITEADMIN))
				.add(Restrictions.ne("description", OrganizationUtils.ROLE_SUPER_MANAGEMENT))
				.addOrder(Order.asc("description"))
				.list();
		session.close();
		
		if(!employeeTypeList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", employeeTypeList.size());
			}
			return employeeTypeList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible for Retrieving All Existing Address Types
	 * 
	 * @param organization
	 * @return addressTypeList
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<VbAddressTypes> getAllAddressTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbAddressTypes> addressTypeList = session.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		
		if(!addressTypeList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", addressTypeList.size());
			}
			return addressTypeList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is To Add AddressType Values
	 * 
	 * @param val
	 * @param description
	 * @param organization
	 * @param username
	 * @throws DataAccessException 
	 */
	public void addAddressTypes(String val, String description,
			VbOrganization organization, String username) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			VbAddressTypes addressTypes = new VbAddressTypes();
			addressTypes.setCreatedBy(username);
			addressTypes.setCreatedOn(createdOn);
			addressTypes.setDescription(description);
			addressTypes.setAddressType(val);
			addressTypes.setVbOrganization(organization);
			addressTypes.setModifiedBy(username);
			addressTypes.setModifiedOn(createdOn);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbAddressTypes");
			}
			session.save(addressTypes);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This Method Is to Add EmployeeTypes
	 * 
	 * @param value
	 * @param description
	 * @param organization
	 * @param username
	 * @throws DataAccessException 
	 */
	public void addemployeeTypes(String value, String description,
			VbOrganization organization, String username) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbRole vbRole = new VbRole();
			vbRole.setDescription(description);
			vbRole.setRoleName(value);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbRole");
			}
			session.save(vbRole);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This Method Is To delete PaymentTypes Based on Given Id
	 * 
	 * @param id
	 * @param organization
	 * @throws DataAccessException 
	 */
	public void deletePaymentTypes(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbPaymentTypes vbPaymentTypes = (VbPaymentTypes) session
				.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("paymentType", value))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if (vbPaymentTypes != null) {
			Transaction txn = session.beginTransaction();
			session.delete(vbPaymentTypes);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is To Delete EmployeeType Based On THe Given id
	 * 
	 * @param desc
	 * @param organization
	 * @throws DataAccessException 
	 */
	public void deleteEmployeeTypes(String desc, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
				.add(Restrictions.eq("description", desc)).uniqueResult();
		
		if (vbRole != null) {
			Transaction txn = session.beginTransaction();
			session.delete(vbRole);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible To AddressTypes
	 * 
	 * @param id
	 * @param organization
	 * @throws DataAccessException 
	 */
	public void deleteAddressTypes(String description,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbAddressTypes vbAddressTypes = (VbAddressTypes) session
				.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("addressType", description))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if (vbAddressTypes != null) {
			Transaction txn = session.beginTransaction();
			session.delete(vbAddressTypes);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is To Retrieve EmployeeType Based On Given Id
	 * 
	 * @param id
	 * @return role
	 * @throws DataAccessException 
	 */
	public VbRole getEmployeetypeById(int id) throws DataAccessException {
		Session session = this.getSession();
		VbRole role = (VbRole) session.get(VbRole.class, id);
		session.close();
		
		if (role != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving EmployeeType by Id: {}", role);
			}
			return role;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is To Retrieve AddressTypes Based On Given Id
	 * 
	 * @param id
	 * @return vbAddressTypes
	 * @throws DataAccessException 
	 */
	public VbAddressTypes getAddTypeById(int id) throws DataAccessException {
		Session session = this.getSession();
		VbAddressTypes vbAddressTypes = (VbAddressTypes) session.get(VbAddressTypes.class, id);
		session.close();
		
		if (vbAddressTypes != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving AddressTypes by Id: {}", vbAddressTypes);
			}
			return vbAddressTypes;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}

	}

	/**
	 * This Method Is To Retrieve PaymentTypes Based On Given Id
	 * 
	 * @param id
	 * @return vbPaymentTypes
	 * @throws DataAccessException 
	 */
	public VbPaymentTypes getPayTypeById(int id) throws DataAccessException {
		Session session = this.getSession();
		VbPaymentTypes vbPaymentTypes = (VbPaymentTypes) session.get(VbPaymentTypes.class, id);
		session.close();
		
		if (vbPaymentTypes != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving paymentTypes by Id: {}", vbPaymentTypes);
			}
			return vbPaymentTypes;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}

	}

	/**
	 * This Method is Responsible To update EmployeeTypes Based On The Given id
	 * 
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @throws DataAccessException 
	 */
	public void updateEmployeeTypes(Integer id, VbOrganization organization,
			String value, String description) throws DataAccessException {
		Session session = this.getSession();
		VbRole vbRole = (VbRole) session.get(VbRole.class, id);
		
		if (vbRole != null) {
			Transaction txn = session.beginTransaction();
			vbRole.setRoleName(value);
			vbRole.setDescription(description);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbRole");
			}
			session.update(vbRole);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is Responsible to Update AddressTypes Based On Given id
	 * 
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @param username
	 * @throws DataAccessException 
	 */
	public void updateAddressTypes(Integer id, VbOrganization organization,
			String value, String description, String username) throws DataAccessException {
		Session session = this.getSession();
		VbAddressTypes vbAddressTypes = (VbAddressTypes) session.get(VbAddressTypes.class, id);
		
		if (vbAddressTypes != null) {
			Transaction txn = session.beginTransaction();
			vbAddressTypes.setAddressType(value);
			vbAddressTypes.setDescription(description);
			vbAddressTypes.setCreatedBy(username);
			vbAddressTypes.setModifiedBy(username);
			vbAddressTypes.setModifiedOn(new Date());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbAddressTypes");
			}
			session.update(vbAddressTypes);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible For Updating Payment Types
	 * 
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @param username
	 * @throws DataAccessException 
	 */
	public void updatePayTypes(Integer id, VbOrganization organization,
			String value, String description, String username) throws DataAccessException {
		Session session = this.getSession();
		VbPaymentTypes vbPaymentTypes = (VbPaymentTypes) session.get(VbPaymentTypes.class, id);
		
		if (vbPaymentTypes != null) {
			Transaction txn = session.beginTransaction();
			vbPaymentTypes.setPaymentType(value);
			vbPaymentTypes.setDescription(description);
			vbPaymentTypes.setCreatedBy(username);
			vbPaymentTypes.setModifiedBy(username);
			vbPaymentTypes.setModifiedOn(new Date());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbPaymentTypes");
			}
			session.update(vbPaymentTypes);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		

	}

	/**
	 * This Method is Responsible For Retrieving Employee id From vbRole Based
	 * on Description
	 * 
	 * @param description
	 * @param organization
	 * @return id
	 * @throws DataAccessException 
	 */
	public Integer getEmpId(String description, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbRole.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("description", description))
				.uniqueResult();
		session.close();
		
		if(id != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Role Id: {}", id);
			}
			return id;
		} else {
			String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible for validating whether the given AddressType
	 * is Already Present Or not
	 * 
	 * @param value
	 * @param organization
	 * @return {@link VbAddressTypes}
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<VbAddressTypes> validateAddressType(String value,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbAddressTypes> vbAddressTypesList = (List<VbAddressTypes>) session.createCriteria(VbAddressTypes.class)
				.add(Restrictions.eq("addressType", value).ignoreCase())
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if(!vbAddressTypesList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", vbAddressTypesList.size());
			}
			return vbAddressTypesList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This Method Is Responsible for validating whether the given PaymentType
	 * is Already Present Or not
	 * 
	 * @param value
	 * @param organization
	 * @return {@link VbPaymentTypes}
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<VbPaymentTypes> validatePaymentType(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbPaymentTypes> vbPaymentTypes = (List<VbPaymentTypes>) session
				.createCriteria(VbPaymentTypes.class)
				.add(Restrictions.eq("paymentType", value).ignoreCase())
				.add(Restrictions.eq("vbOrganization", organization)).list();
		session.close();
		
		if(!vbPaymentTypes.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("validating paymentTypes by Id: {}", vbPaymentTypes);
			}
			return vbPaymentTypes;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible for validating whether the given employeetype
	 * is Already Present Or not
	 * 
	 * @param value
	 * @param organization
	 * @return {@link VbRole}
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<VbRole> validateEmployeeType(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbRole> vbRoleList = (List<VbRole>) session.createCriteria(VbRole.class)
				.add(Restrictions.eq("description", value).ignoreCase())
				.list();
		session.close();
		
		if(!vbRoleList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", vbRoleList.size());
			}
			return vbRoleList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible To Retrieve AddressTypes
	 * 
	 * @param organization
	 * @return addressTypes
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<EmployeeResult> getCrAddressTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		EmployeeResult employeeResult = new EmployeeResult();
		EmployeeResult employeeResult2 = null;
		List<EmployeeResult> employeeResults = new ArrayList<EmployeeResult>();
		List<String> addressTypesList = session.createCriteria(VbAddressTypes.class)
				.setProjection(Projections.property("addressType"))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("addressType"))
				.list();
		session.close();
		
		for (String addressType : addressTypesList) {
			employeeResult2 = new EmployeeResult();
			employeeResult2.setAddressType(addressType);
			employeeResults.add(employeeResult2);
		}
		employeeResult.setUserName(organization.getUsernamePrefix());
		employeeResults.add(employeeResult);
		if(!employeeResults.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", addressTypesList.size());
			}
			return employeeResults;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		

	}

	/**
	 * This Method Is To Retrieve Particular Id Based On AddressType
	 * 
	 * @param value
	 * @param organization
	 * @return
	 * @throws DataAccessException 
	 */
	public int getAddressTypeId(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbAddressTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("addressType", value))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(id != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("AddressType Id: {}", id);
			}
			return id;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is To Retrieve Particular Id Based On PaymentType
	 * 
	 * @param value
	 * @param organization
	 * @return
	 * @throws DataAccessException 
	 */
	public int getPaymentTypeId(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbPaymentTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("paymentType", value))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(id != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("PaymentType Id: {}", id);
			}
			return id;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for retrieving all existed journal types.
	 * 
	 * @param organization
	 * @return journalTypeList
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<String> getJournalTypes(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> journalTypeList = (List<String>) session.createCriteria(VbJournalTypes.class)
				.setProjection(Projections.property("journalType"))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("journalType"))
				.list();
		session.close();
		
		if(!journalTypeList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", journalTypeList.size());
			}
			return journalTypeList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for saving journal typews 
	 * 
	 * @param value
	 * @param description
	 * @param organization
	 * @param username
	 * @param invoiceNo
	 * @throws DataAccessException
	 */
	public void addJournalTypes(String value, String description,
			VbOrganization organization, String username, String invoiceNo) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date createdOn = new Date();
			VbJournalTypes vbJournalTypes = new VbJournalTypes();
			vbJournalTypes.setDescription(description);
			vbJournalTypes.setJournalType(value);
			vbJournalTypes.setInvoiceNo(invoiceNo.toUpperCase());
			vbJournalTypes.setVbOrganization(organization);
			vbJournalTypes.setModifiedOn(createdOn);
			vbJournalTypes.setModifiedBy(username);
			vbJournalTypes.setCreatedBy(username);
			vbJournalTypes.setCreatedOn(createdOn);
			session.save(vbJournalTypes);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbJournalTypes");
			}
			session.save(vbJournalTypes);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible for retrieving given record id from journal
	 * 
	 * @param value
	 * @param organization
	 * @return id
	 * @throws DataAccessException
	 */
	public Integer getJournalId(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer id = (Integer) session.createCriteria(VbJournalTypes.class)
				.setProjection(Projections.property("id"))
				.add(Restrictions.eq("journalType", value))
				.uniqueResult();
		session.close();
		
		if(id != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving JournalType Id: {}", id);
			}
			return id;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is reponsible for retrieving journal type for specified id 
	 * 
	 * @param id
	 * @return vbJournalTypes
	 * @throws DataAccessException
	 */
	public VbJournalTypes getVbJournalTypeByid(int id) throws DataAccessException {
		Session session = this.getSession();
		VbJournalTypes vbJournalTypes = (VbJournalTypes) session.get(VbJournalTypes.class, id);
		session.close();
		
		if (vbJournalTypes != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Retrieving paymentTypes: {}", vbJournalTypes);
			}
			return vbJournalTypes;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}

	}

	/**
	 * This method is responsible for updating journal types of specific record
	 * 
	 * @param id
	 * @param organization
	 * @param value
	 * @param description
	 * @param invoiceNo
	 * @throws DataAccessException
	 */
	public void updateJournalTypes(Integer id, VbOrganization organization,
			String value, String description, String invoiceNo) throws DataAccessException {
		Session session = this.getSession();
		VbJournalTypes vbJournalTypes = (VbJournalTypes) session.get(VbJournalTypes.class, id);
		
		if (vbJournalTypes != null) {
			Transaction txn = session.beginTransaction();
			vbJournalTypes.setJournalType(value);
			vbJournalTypes.setDescription(description);
			vbJournalTypes.setInvoiceNo(invoiceNo.toUpperCase());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Updating VbJournalTypes");
			}
			session.update(vbJournalTypes);
			txn.commit();
			session.close();
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
	 * This method is responsible to delete specified record
	 * 
	 * @param value
	 * @param organization
	 * @throws DataAccessException
	 */
	public void deleteJournalTypes(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbJournalTypes vbJournalTypes = (VbJournalTypes) session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("journalType", value))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		if (vbJournalTypes != null) {
			Transaction txn = session.beginTransaction();
			session.delete(vbJournalTypes);
			txn.commit();
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This method is responsible to check the given journal type is existed already or not
	 * 
	 * @param value
	 * @param organization
	 * @return vbJournalTypesList
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournalTypes> validateJournalTypes(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbJournalTypes> vbJournalTypesList = (List<VbJournalTypes>) session
				.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("journalType", value).ignoreCase())
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if(!vbJournalTypesList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", vbJournalTypesList.size());
			}
			return vbJournalTypesList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
		
	}

	/**
	 * This method is responsible to persist {@link VbInvoiceNoPeriod}.
	 * 
	 * @param value - {@link String}
	 * @param description - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @param invoiceNoPeriod - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void addInvoicePeriod(String value, String description,
			VbOrganization organization, String username, String invoiceNoPeriod) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			Date createdOn = new Date();
			txn = session.beginTransaction();
			VbInvoiceNoPeriod vbInvoiceNoPeriod = null;
			for (String txnType : DropDownUtil.getDropDown(DropDownUtil.TXN_TYPES).keySet()) {
				vbInvoiceNoPeriod = new VbInvoiceNoPeriod();
				vbInvoiceNoPeriod.setDescription(description);
				vbInvoiceNoPeriod.setStartValue(value);
				vbInvoiceNoPeriod.setPeriod(invoiceNoPeriod);
				vbInvoiceNoPeriod.setVbOrganization(organization);
				vbInvoiceNoPeriod.setModifiedOn(createdOn);
				vbInvoiceNoPeriod.setModifiedBy(username);
				vbInvoiceNoPeriod.setCreatedBy(username);
				vbInvoiceNoPeriod.setCreatedOn(createdOn);
				vbInvoiceNoPeriod.setTxnType(txnType);
				session.save(vbInvoiceNoPeriod);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbInvoiceNoPeriod");
			}
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param vbOrganization
	 * @return systemDefaultsResult
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	public SystemDefaultsResult getExistedInvoiceNumbers(VbOrganization vbOrganization) throws DataAccessException {
		Session session = this.getSession();
		VbInvoiceNoPeriod vbInvoiceNoPeriod = null;
		List<VbInvoiceNoPeriod> invoiceNumbersList = (List<VbInvoiceNoPeriod>) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.list();
		session.close();
		
		if (!invoiceNumbersList.isEmpty()) {
			SystemDefaultsResult systemDefaultsResult = new SystemDefaultsResult();
			vbInvoiceNoPeriod = invoiceNumbersList.get(0);
			systemDefaultsResult.setValue(vbInvoiceNoPeriod.getStartValue());
			systemDefaultsResult.setPeriod(vbInvoiceNoPeriod.getPeriod());
			systemDefaultsResult.setId(vbInvoiceNoPeriod.getId());
			systemDefaultsResult.setDescription(vbInvoiceNoPeriod.getDescription());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("SystemDefaultsResult: {}", systemDefaultsResult);
			}
			return systemDefaultsResult;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to check whether the given Invoice number is already exited or not
	 * 
	 * @param organization
	 * @return isAvailable
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkInvoiceNumberAvailability(VbOrganization organization) {
		Session session = this.getSession();
		Boolean isAvailable = Boolean.FALSE;
		List<VbInvoiceNoPeriod> vbInvoiceNoPeriod = (List<VbInvoiceNoPeriod>) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if (vbInvoiceNoPeriod.size() != 0) {
			isAvailable = Boolean.TRUE;
		} 
		return isAvailable;
	}

	/**
	 * This Method Is Responsible To Delete The Specified InvoiceNo
	 * 
	 * @param id
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public void deleteInvoiceNumber(Integer id) throws DataAccessException {
		Session session = this.getSession();
		VbInvoiceNoPeriod vbInvoiceNoPeriod = (VbInvoiceNoPeriod) session.get(VbInvoiceNoPeriod.class, id);
		if (vbInvoiceNoPeriod != null) {
			List<VbInvoiceNoPeriod> invoiceNoList = (List<VbInvoiceNoPeriod>) session.createCriteria(VbInvoiceNoPeriod.class)
					.add(Restrictions.eq("vbOrganization", vbInvoiceNoPeriod.getVbOrganization()))
					.list();
			if (!invoiceNoList.isEmpty()) {
				Transaction transaction = session.beginTransaction();
				for (VbInvoiceNoPeriod invoiceNoPeriod : invoiceNoList) {
					session.delete(invoiceNoPeriod);
				}
				if(_logger.isDebugEnabled()) {
					_logger.debug("Deleting VbInvoiceNoPeriod");
				}
				transaction.commit();
			}
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible To Update The Specified InvoiceNo
	 * 
	 * @param id
	 * @param value
	 * @param description
	 * @param period
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public void updateInvoiceNumber(Integer id, String value,
			String description, String period) throws DataAccessException {
		Session session = this.getSession();
		VbInvoiceNoPeriod vbInvoiceNoPeriod = (VbInvoiceNoPeriod) session.get(VbInvoiceNoPeriod.class, id);
		if (vbInvoiceNoPeriod != null) {
			List<VbInvoiceNoPeriod> invoiceNoList = (List<VbInvoiceNoPeriod>) session.createCriteria(VbInvoiceNoPeriod.class)
					.add(Restrictions.eq("vbOrganization", vbInvoiceNoPeriod.getVbOrganization()))
					.list();
			if (!invoiceNoList.isEmpty()) {
				Transaction transaction = session.beginTransaction();
				for (VbInvoiceNoPeriod invoiceNoPeriod : invoiceNoList) {
					invoiceNoPeriod.setDescription(description);
					invoiceNoPeriod.setStartValue(value);
					invoiceNoPeriod.setPeriod(period);
					
					session.update(invoiceNoPeriod);
				}
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Updating VbInvoiceNoPeriod");
				}
				transaction.commit();
			}
			session.close();
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method is responsible for checking that whether the given invoice
	 * pattern is already exists or not.
	 * 
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isAvailable
	 */
	public Boolean checkInvoiceNumber(String invoiceNo, VbOrganization organization) {
		Session session = this.getSession();
		Boolean isAvailable = Boolean.FALSE;
		VbJournalTypes journalTypes = (VbJournalTypes ) session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("invoiceNo", invoiceNo.toUpperCase()))
				.uniqueResult();
		session.close();
		
		if (journalTypes != null) {
			isAvailable = Boolean.TRUE;
		} 
		return isAvailable;
	}

	/**
	 *  This method is responsible to save the given product category
	 *  
	 * @param productCategory
	 * @param description
	 * @param organization
	 * @param userName
	 * @throws DataAccessException
	 */
	public void saveProductCategories(String productCategory,
			String description, VbOrganization organization, String userName) throws DataAccessException {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		try{
			
		Date date = new Date();
		VbProductCategories vbProductCategories = new VbProductCategories();
		vbProductCategories.setProductCategory(productCategory);
		vbProductCategories.setDescription(description);
		vbProductCategories.setVbOrganization(organization);
		vbProductCategories.setCreatedOn(date);
		vbProductCategories.setCreatedBy(userName);
		vbProductCategories.setModifiedBy(userName);
		vbProductCategories.setModifiedOn(date);
		if(_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbPaymentTypes");
		}
		session.save(vbProductCategories);
		transaction.commit();
	} catch (HibernateException exception) {
		if(transaction != null) {
			transaction.rollback();
		}
		String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
		
		if(_logger.isErrorEnabled()) {
			_logger.error(message);
		}
		throw new DataAccessException(message);
	} finally {
		if(session != null) {
			session.close();
		}
	}
	}

	/**
	 * This Method is responsible for retrieving all the existed product categories
	 * 
	 * @return {@link SystemDefaultsResult}
	 */
	@SuppressWarnings("unchecked")
	public List<SystemDefaultsResult> getExistedProductCategories() {
		Session session = this.getSession();
		SystemDefaultsResult systemDefaultsResult = null;
		List<SystemDefaultsResult> productCategories = new ArrayList<SystemDefaultsResult>();
		
		List<VbProductCategories> productCategoriesList = session.createCriteria(VbProductCategories.class)
			//	.setProjection(Projections.property("productCategory"))
				.list();
		for (VbProductCategories vbProductCategories : productCategoriesList) {
			systemDefaultsResult = new SystemDefaultsResult();
			systemDefaultsResult.setValue(vbProductCategories.getProductCategory());
			systemDefaultsResult.setDescription(vbProductCategories.getDescription());
			systemDefaultsResult.setId(vbProductCategories.getId());
			productCategories.add(systemDefaultsResult);
		}
		return productCategories;
	}

	/**
	 * This method is responsible for updating the VbProductCategories.
	 * 
	 * @param id
	 * @param productCategory
	 * @param description
	 * @param userName
	 */
	public void updateProductCategories(Integer id, String productCategory,
			String description,String userName) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		Date date = new Date();
		VbProductCategories vbProductCategories = (VbProductCategories) session.get(VbProductCategories.class,id);
		if(vbProductCategories != null){
			vbProductCategories.setProductCategory(productCategory);
			vbProductCategories.setDescription(description);
			vbProductCategories.setCreatedBy(userName);
			vbProductCategories.setModifiedBy(userName);
			vbProductCategories.setCreatedOn(date);
			vbProductCategories.setModifiedOn(date);
			session.update(vbProductCategories);
			transaction.commit();
			session.close();
		}
		
	}

	/**
	 * This method is responsible to delete the specified product category
	 * 
	 * @param id
	 */
	public void deleteProductCategories(Integer id) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		
		VbProductCategories vbProductCategories = (VbProductCategories) session.get(VbProductCategories.class, id);
		if(vbProductCategories != null){
			session.delete(vbProductCategories);
			transaction.commit();
			session.close();
			
		}
		
	}

	/**
	 * This method is responsible to check whethe the given product category is available or not 
	 * 
	 * @param prodCategory
	 * @param organization
	 * @return
	 */
	public Boolean checkProductCategoryExistance(String prodCategory,
			VbOrganization organization) {
		Session session = this.getSession();
		Boolean isProdCategoryAvailable = Boolean.TRUE;
		VbProductCategories vbProductCategories = (VbProductCategories) session.createCriteria(VbProductCategories.class)
				.add(Restrictions.eq("productCategory", prodCategory).ignoreCase())
				.add(Restrictions.eq("vbOrganization",organization))
				.uniqueResult();
		if(vbProductCategories != null){
			isProdCategoryAvailable = Boolean.FALSE;
		}
		return isProdCategoryAvailable;
	}
}