/**
 * com.vekomy.vbooks.hibernate.BaseDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 17, 2013
 */
package com.vekomy.vbooks.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSystemNotifications;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This base class is responsible to perform basic CRUD operations.
 * 
 * @author Sudhakar
 * 
 */
public class BaseDao extends HibernateDaoSupport implements IHibernateDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(BaseDao.class);
	
	/**
	 * This method is responsible to save the model, if model is not in the DB.
	 * Other wise it will update the model.
	 * 
	 * @param model 
	 * @throws DataAccessException 
	 */
	public <T> void saveOrUpdate(T model) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			session.saveOrUpdate(model);
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
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
	 * This method is responsible to update the existing model.
	 *
	 * @param model
	 * @throws DataAccessException 
	 */
	public <T> void update(T model) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			session.update(model);
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to delete from DB.
	 *
	 * @param model
	 * @throws DataAccessException 
	 */
	public <T> void detete(T model) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			session.delete(model);
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method takes {@link Query} as input and gives generic entity.
	 * 
	 * @param query - {@link Query}
	 * @return T - Generic type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSingleResultOrNull(Query query) {
		query.setMaxResults(1);
		List<?> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return (T) list.get(0);
	}
	
	/**
	 * This method is responsible to generate employee number for the new {@link VbEmployee}.
	 * 
	 * @param session - {@link Session}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param vbEmployee - {@link EmployeeCommand}
	 * @return generatedEmployeeNo - [{@link String}
	 * 
	 */
	public String generateEmployeeNo(Session session, VbOrganization vbOrganization, String employeeType){
		Query query = session.createQuery(
				"FROM VbEmployee vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.id IN" +
				"(SELECT MAX(vbe.id) FROM VbEmployee vbe WHERE vbe.vbOrganization = :vbOrganization2 AND vbe.employeeType like :empType)")
				.setParameter("vbOrganization1", vbOrganization)
				.setParameter("vbOrganization2", vbOrganization)
		        .setParameter("empType", employeeType);
		VbEmployee employee = (VbEmployee) query.uniqueResult();
		String generatedEmployeeNo = null;
		if(employee == null) {
			generatedEmployeeNo = vbOrganization.getOrganizationCode().concat(employeeType).concat("1");
		} else{
			String employeeNo = employee.getEmployeeNumber();
			Integer length = employeeType.length();
			employeeNo = employeeNo.substring(employeeNo.indexOf(employee.getEmployeeType())+length, employeeNo.length());
			Integer empNnumber = Integer.parseInt(employeeNo);
			Integer employeeNumber = ++ empNnumber;
			generatedEmployeeNo = vbOrganization.getOrganizationCode().concat(employeeType).concat(employeeNumber.toString());
		}
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Genereated employeeNo: {}", generatedEmployeeNo);
		}
		return generatedEmployeeNo;
	}
	
	/**
	 * This method is responsible to reset invoice no based on configuration.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param latestValue - {@link Integer}
	 * @return newValue- {@link String}
	 */
	public String resetInvoiceNo(Session session,
			VbInvoiceNoPeriod invoiceNoPeriod, VbOrganization organization,
			Integer latestValue) {
		Integer newValue = null;
		if (invoiceNoPeriod != null) {
			Integer daysDifference = DateUtils.getDifferenceDays(new Date(), invoiceNoPeriod.getModifiedOn());
			if (daysDifference == Integer.valueOf(invoiceNoPeriod.getPeriod())) {
				newValue = Integer.valueOf(invoiceNoPeriod.getStartValue());

				// Updating modifiedOn field for next reset.
				Transaction txn = session.beginTransaction();
				invoiceNoPeriod.setModifiedOn(new Date());
				txn.commit();
			} else {
				newValue = ++latestValue;
			}
		} else {
			newValue = ++latestValue;
		}

		return StringUtil.invoiceNoFormat(newValue);
	}
	
	/**
	 * This method is used to get the salesbook instance.
	 * 
	 * @param session - {@link Session}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return VbSalesBook - {@link VbSalesBook}
	 */
	public VbSalesBook getVbSalesBook(Session session, String userName, VbOrganization organization) {
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.eq("salesExecutive", userName))
				.add(Expression.eq("flag", new Integer(1)))
				.uniqueResult();
		return vbSalesBook;
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
	public VbSalesBook getVbSalesBookNoFlag(Session session,  String userName, VbOrganization organization) {
		Query query = session.createQuery("FROM VbSalesBook vb WHERE vb.vbOrganization = :vbOrganization1 AND vb.salesExecutive = :salesExecutiveName1 AND"
						+ " vb.createdOn IN (SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs WHERE " +
						"vbs.vbOrganization = :vbOrganization2 AND vbs.salesExecutive = :salesExecutiveName2)")
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutiveName1", userName)
				.setParameter("vbOrganization2", organization)
				.setParameter("salesExecutiveName2", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		
		return salesBook;
	}
	
	/**
	 * This method is responsible to get the Unique no for {@link VbSalesBook}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedSalesBookNo - {@link String}
	 */
	public String generateSalesBookNo(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		//For fetching latest invoice no.
		Query query = session.createQuery(
						"SELECT vb.salesBookNo FROM VbSalesBook vb WHERE vb.createdOn IN (SELECT MAX(vbsb.createdOn) FROM VbSalesBook vbsb "
						+ "WHERE vbsb.vbOrganization = :vbOrganization AND vbsb.salesBookNo LIKE :salesBookNo)")
				.setParameter("vbOrganization", organization)
				.setParameter("salesBookNo", "%SB%");
		String latestSalesBookNo = getSingleResultOrNull(query);
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_SALES_BOOK))
				.uniqueResult();
		StringBuffer generatedSalesBookNo = new StringBuffer();
		if (latestSalesBookNo == null) {
			if(invoiceNoPeriod != null) {
				generatedSalesBookNo.append(organization.getOrganizationCode()).append(seperator).append(OrganizationUtils.TXN_TYPE_SALES_BOOK).append(seperator).append(formattedDate).append(seperator).append(invoiceNoPeriod.getStartValue());
			} else {
				generatedSalesBookNo.append(organization.getOrganizationCode()).append(seperator).append(OrganizationUtils.TXN_TYPE_SALES_BOOK).append(seperator).append(formattedDate).append(seperator).append("0001");
			}
		} else {
			String[] latestSalesBookNoArray = latestSalesBookNo.split(seperator);
			latestSalesBookNo = latestSalesBookNoArray[3];
			Integer salesBookNo = Integer.parseInt(latestSalesBookNo);
			String newSalesBookNo = resetInvoiceNo(session, invoiceNoPeriod, organization, salesBookNo);
			generatedSalesBookNo.append(organization.getOrganizationCode()).append(seperator).append(OrganizationUtils.TXN_TYPE_SALES_BOOK).append(seperator).append(formattedDate).append(seperator).append(newSalesBookNo);
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Generated daybookNo is {}", generatedSalesBookNo.toString());
		}
		session.close();
		return generatedSalesBookNo.toString();
	}
	
	
	/**
	 * This method is responsible to persist {@link VbSystemNotifications}.
	 * This method is completely dedicated to Android Application for alert Notifications.
	 * 
	 * @param session - {@link Session}
	 * @param userName - {@link String}
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param notificationType - {@link String}
	 * @param notificationStatus - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param businessName - {@link String}
	 */
	public void saveSystemNotificationForAndroid(Session session,
			String userName, String salesExecutive,
			VbOrganization organization, String notificationType,
			String notificationStatus, String invoiceNo, String businessName) {
		Date createdDate = new Date();
		VbSystemNotifications systemNotifications = new VbSystemNotifications();
		systemNotifications.setCreatedBy(userName);
		systemNotifications.setCreatedOn(createdDate);
		systemNotifications.setModifiedOn(createdDate);
		
		if (businessName != null) {
			systemNotifications.setBusinessName(businessName);
		}
		
		if (notificationStatus != null) {
			systemNotifications.setNotificationStatus(notificationStatus);
		}
		
		if (invoiceNo != null) {
			systemNotifications.setInvoiceNo(invoiceNo);
		}
		
		systemNotifications.setNotificationType(notificationType);
		systemNotifications.setFlag(new Integer(1));
		systemNotifications.setVbOrganization(organization);
		VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
		if (salesBook == null) {
			salesBook = getVbSalesBookNoFlag(session, salesExecutive, organization);
		}
		
		if(salesBook != null) {
			systemNotifications.setSalesId(salesBook.getId());
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbSystemNotifications.");
		}
		session.save(systemNotifications);
	}
}
