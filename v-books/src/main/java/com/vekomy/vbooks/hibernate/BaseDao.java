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

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbOrganization;

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
	 */
	public <T> void saveOrUpdate(T model) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		session.saveOrUpdate(model);
		txn.commit();
		session.close();
	}
	
	/**
	 * This method is responsible to update the existing model.
	 *
	 * @param model
	 */
	public <T> void update(T model) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		session.update(model);
		txn.commit();
		session.close();
	}
	
	/**
	 * This method is responsible to delete from DB.
	 *
	 * @param model
	 */
	public <T> void detete(T model) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		session.delete(model);
		txn.commit();
		session.close();
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
}
