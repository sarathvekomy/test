/**
 * com.vekomy.vbooks.app.sales.dao.DeliveryNoteDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 6, 2013
 */
package com.vekomy.vbooks.app.sales.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.base.BaseDao;
import com.vekomy.vbooks.app.hibernate.model.VbCustomer;
import com.vekomy.vbooks.app.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.app.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.app.hibernate.model.VbEmployee;
import com.vekomy.vbooks.app.hibernate.model.VbEmployeeCustomer;
import com.vekomy.vbooks.app.hibernate.model.VbOrganization;
import com.vekomy.vbooks.app.response.DeliveryNoteResponse;

/**
 * @author Sudhakar
 *
 */
public class DeliveryNoteDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteDao.class);
	
	/**
	 * This method is responsible to get all the business names, which are assigned to corresponding sales executive.
	 * 
	 * @param userName - {@link String}
	 * @param organizationId - {@link Integer}
	 * @return responseList {@link List} 
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryNoteResponse> getBusinessName(String userName, Integer organizationId) {
		Session session = this.getSession();
		List<DeliveryNoteResponse> responseList = new ArrayList<DeliveryNoteResponse>();
		VbOrganization organization = (VbOrganization) session.get(VbOrganization.class, organizationId);
		if(organization != null) {
			VbEmployee employee = (VbEmployee) session.createCriteria(VbEmployee.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			if(employee != null) {
				List<VbCustomer> customerList = session.createCriteria(VbEmployeeCustomer.class)
						.setProjection(Projections.property("vbCustomer"))
						.add(Restrictions.eq("vbEmployee", employee))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				
				DeliveryNoteResponse response = null;
				Float creditAmount;
				Float advanceAmount;
				String businessName = null;
				String invoiceName = null;
				for (VbCustomer customer : customerList) {
					businessName = customer.getBusinessName();
					businessName = customer.getInvoiceName();
					// For Customer Credit Info
					creditAmount = (Float) session.createCriteria(VbCustomerCreditInfo.class)
							.setProjection(Projections.sum("due"))
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("vbOrganization", organization))
							.uniqueResult();
					
					if(creditAmount == null) {
						creditAmount = new Float("0.00");
					}
					// For Customer Advance info
					advanceAmount = (Float) session.createCriteria(VbCustomerAdvanceInfo.class)
							.setProjection(Projections.sum("balance"))
							.add(Restrictions.eq("businessName", businessName))
							.add(Restrictions.eq("vbOrganization", organization))
							.uniqueResult();
					
					if(advanceAmount == null) {
						advanceAmount = new Float("0.00");
					}
					
					// Preparing response 
					response = new DeliveryNoteResponse();
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
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("{} business names have been assigned to {}", responseList.size(), userName);
		}
		return responseList;
	}
}
