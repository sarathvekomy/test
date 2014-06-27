/**
 * com.vekomy.vbooks.app.base.BaseDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 6, 2013
 */
package com.vekomy.vbooks.app.base;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Sudhakar
 *
 */
public class BaseDao extends HibernateDaoSupport {
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
}
