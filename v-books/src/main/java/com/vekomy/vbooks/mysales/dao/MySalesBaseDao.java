/**
 * com.vekomy.vbooks.mysales.dao.MySalesBaseDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jun 26, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbDayBook;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

/**
 * This base class is responsible to provide common methods. 
 * 
 * @author Sudhakar
 *
 * 
 */
public class MySalesBaseDao extends BaseDao {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(MySalesBaseDao.class);
	
	/**
	 * This method is responsible to check either the business name available
	 * to the sales executive or not.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isExists - {@link Boolean}
	 * 
	 */
	public Boolean checkBusinessName(String salesExecutive, String businessName, VbOrganization organization){
		Boolean isExists = Boolean.FALSE;
		Session session = this.getSession();
		VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
				.add(Restrictions.eq("username", salesExecutive))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		VbCustomer customer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("businessName", businessName))
				.uniqueResult();
		
		if(employee != null && customer != null) {
			VbEmployeeCustomer employeeCustomer = (VbEmployeeCustomer) session.createCriteria(VbEmployeeCustomer.class)
					.add(Restrictions.eq("vbEmployee", employee))
					.add(Restrictions.eq("vbCustomer", customer))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if(employeeCustomer != null) {
				isExists = Boolean.TRUE;
				if(_logger.isDebugEnabled()) {
					_logger.debug("{} is not available to {}.", businessName, salesExecutive);
				}
			}
		}
		session.close();
		return isExists;
	}
	
	/**
	 * This method is responsible to check whether {@link VbDayBook} have been closed or not.
	 * 
	 * @param salesExecutive - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isDayBookClosed - {@link Boolean}
	 * 
	 */
	public Boolean isDayBookClosed(String salesExecutive, VbOrganization organization){
		Boolean isDayBookClosed = Boolean.FALSE;
		Session session = this.getSession();
		Integer flag = (Integer) session.createQuery("SELECT vb.flag FROM VbSalesBook vb WHERE vb.salesExecutive = :salesExecutive1 AND " +
				"vb.vbOrganization = :vbOrganization1 AND vb.createdOn IN(SELECT MAX(vbs.createdOn) FROM VbSalesBook vbs " +
				"WHERE vbs.salesExecutive = :salesExecutive2 AND vbs.vbOrganization = :vbOrganization2)")
				.setParameter("salesExecutive1", salesExecutive)
				.setParameter("vbOrganization1", organization)
				.setParameter("salesExecutive2", salesExecutive)
				.setParameter("vbOrganization2", organization)
				.uniqueResult();
		if(flag != null && flag == 0) {
			isDayBookClosed = Boolean.TRUE;
			if(_logger.isDebugEnabled()) {
				_logger.debug("{}'s daybook have been closed.", salesExecutive);
			}
		}
		session.close();
		return isDayBookClosed;
	}
	
	/**
	 * This method is used to get the business names.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return businessNameList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getBusinessName(String businessName,
			VbOrganization organization, String userName) {
		Session session = this.getSession();
		VbEmployee vbemployee = (VbEmployee) session
				.createCriteria(VbEmployee.class)
				.add(Expression.eq("username", userName))
				.add(Expression.eq("employeeType", "SLE")).uniqueResult();
		List<String> businessNameList = session
				.createCriteria(VbEmployeeCustomer.class)
				.createAlias("vbCustomer", "customer")
				.setProjection(Projections.distinct(Projections.property("customer.businessName")))
				.add(Expression.eq("vbEmployee", vbemployee))
				.add(Expression.eq("vbOrganization", organization))
				.add(Expression.like("customer.businessName", businessName,MatchMode.START).ignoreCase())
				.addOrder(Order.asc("customer.businessName")).list();

		if (_logger.isDebugEnabled()) {
			_logger.debug("Business Name List: {}", businessNameList);
		}
		session.close();
		return businessNameList;
	}

}
