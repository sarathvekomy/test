/**
 * com.vekomy.vbooks.app.dao.SalesReturnDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.app.dao.base.BaseDao;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnProduct;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.utils.ApplicationConstants;
import com.vekomy.vbooks.app.utils.CRStatus;
import com.vekomy.vbooks.app.utils.DateUtils;
import com.vekomy.vbooks.app.utils.ENotificationTypes;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts;

/**
 * @author Sudhakar
 * 
 */
public class SalesReturnDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnDao.class);
	
	/**
	 * This method is responsible to persist {@link VbSalesReturn} details.
	 * 
	 * @param request - {@link SalesReturnRequest}
	 * @return response - {@link Response}
	 */
	public Response saveSalesReturn(SalesReturn request) {
		Response response = null;
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			VbOrganization organization = getOrganization(session, request.getOrganizationId());
			Date date = new Date();
			String salesExecutive = request.getCreatedBy();
			VbSalesReturn salesReturn = new VbSalesReturn();
			salesReturn.setCreatedBy(salesExecutive);
			salesReturn.setCreatedOn(date);
			salesReturn.setModifiedOn(date);
			salesReturn.setVbOrganization(organization);
			salesReturn.setStatus(CRStatus.PENDING.name());
			salesReturn.setFlag(new Integer(1));
			VbSalesBook salesBook = getVbSalesBook(session, salesExecutive, organization);
			salesReturn.setVbSalesBook(salesBook);
			String businessName = request.getBusinessName();
			String invoiceNo = new StringBuffer(generateInvoiceNo(organization)).append("#").append(request.getInvoiceNo()).toString();
			salesReturn.setBusinessName(businessName);
			salesReturn.setInvoiceName(request.getInvoiceName());
			salesReturn.setInvoiceNo(invoiceNo);
			salesReturn.setRemarks(request.getRemarks());
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSalesReturn.");
			}
			session.save(salesReturn);
				
			List<SalesReturnProduct> productList = request.getProducts();
			//Persisting SalesReturnProdducts
			VbSalesReturnProducts instanceReturnProducts = null;
			for (SalesReturnProduct prodcutRequest : productList) {
				instanceReturnProducts = new VbSalesReturnProducts();
				instanceReturnProducts.setProductName(prodcutRequest.getProductName());
				instanceReturnProducts.setBatchNumber(prodcutRequest.getBatchNumber());
				instanceReturnProducts.setResalable(prodcutRequest.getResaleQty());
				instanceReturnProducts.setDamaged(prodcutRequest.getDamagedQty());
				instanceReturnProducts.setTotalQty(prodcutRequest.getResaleQty()+prodcutRequest.getDamagedQty());
				instanceReturnProducts.setVbSalesReturn(salesReturn);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbSalesReturnProducts.");
				}
				session.save(instanceReturnProducts);
			}
			
			// Persisting systemNotifications.
			saveSystemNotification(session, salesExecutive, salesExecutive, organization, 
					ENotificationTypes.SALES_RETURN.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
			
			// Preparing response.
			response = new Response();
			response.setMessage("success");
			response.setStatusCode(new Integer(200));
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Response: {}", response);
			}
			return response;
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			// Preparing response.
			response = new Response();
			response.setMessage("fail");
			response.setStatusCode(new Integer(500));
						
			if(_logger.isDebugEnabled()) {
				_logger.debug("Response: {}", response);
			}
			return response;
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to generate new invoice no for the {@link VbSalesReturn}
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * 
	 */
	public String generateInvoiceNo(VbOrganization organization) {
		Session session = this.getSession();
		String seperator = ApplicationConstants.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		Query query = session.createQuery(
				"SELECT vb.invoiceNo FROM VbSalesReturn vb WHERE vb.createdOn IN (SELECT MAX(vbsr.createdOn) FROM VbSalesReturn vbsr " +
				"WHERE vbsr.vbOrganization = :vbOrganization)")
				.setParameter("vbOrganization", organization);
		String latestInvoiceNo = getSingleResultOrNull(query);
		
		VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("txnType", ApplicationConstants.TXN_TYPE_SALES_RETURN))
				.uniqueResult();
		StringBuffer generatedInvoiceNo = new StringBuffer();
		String txnTypeSalesReturn = ApplicationConstants.TXN_TYPE_SALES_RETURN;
		if (latestInvoiceNo == null) {
			if(invoiceNoPeriod != null) {
				generatedInvoiceNo.append(organization.getOrganizationCode())
				.append(seperator).append(txnTypeSalesReturn)
				.append(seperator).append(formattedDate)
				.append(seperator).append(invoiceNoPeriod.getStartValue());
			} else {
				generatedInvoiceNo.append(organization.getOrganizationCode())
				.append(seperator).append(txnTypeSalesReturn)
				.append(seperator).append(formattedDate)
				.append(seperator).append("00001");
			}
		} else {
			String[] salesReturnInvoiceNo = latestInvoiceNo.split("#");
			String[] latestInvoiceNoArray = salesReturnInvoiceNo[0].split("/");
			latestInvoiceNo = latestInvoiceNoArray[3];
			Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
			String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
			generatedInvoiceNo.append(organization.getOrganizationCode())
			.append(seperator).append(txnTypeSalesReturn)
			.append(seperator).append(formattedDate)
			.append(seperator).append(newInvoiceNo);
		}
		session.close();
		
		if(_logger.isDebugEnabled()){
			_logger.debug("Generated sales return invoiceNo: {}", generatedInvoiceNo.toString());
		}
		return generatedInvoiceNo.toString();
	}
}
