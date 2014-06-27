/**
 * com.vekomy.vbooks.mysales.action.DeliveryNoteAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 17, 2013
 */
package com.vekomy.vbooks.mysales.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteCustomerResult;
import com.vekomy.vbooks.mysales.command.DeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.DeliveryNoteResult;
import com.vekomy.vbooks.mysales.command.ProductResult;
import com.vekomy.vbooks.mysales.dao.DeliveryNoteDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This actions class is responsible to perform delivery note activities.
 * 
 * @author Swarupa
 * 
 * 
 */
public class DeliveryNoteAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteAction.class);
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IResult process(Object form) throws Exception {
		DeliveryNoteCommand deliveryNoteCommand = null;
		DeliveryNoteProductCommand deliveryNoteProducts = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		DeliveryNoteDao deliveryNoteDao = (DeliveryNoteDao) getDao();
		String userName = getUsername();
		VbOrganization organization = getOrganization();
		if (form instanceof DeliveryNoteCommand) {
			deliveryNoteCommand = (DeliveryNoteCommand) form;
			_logger.debug("deliveryNoteCommand: {}", deliveryNoteCommand);
			String action = deliveryNoteCommand.getAction();
			if ("save-deliverynote-info".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("deliverynote-info", deliveryNoteCommand);
				resultSuccess.setMessage("Saved Successfully");
			} else if ("save-deliverynote".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				deliveryNoteDao.saveDeliveryNote((DeliveryNoteProductCommand) session.getAttribute("delivery-note-product"),
						(List<DeliveryNoteCommand>) session.getAttribute("product-data"), userName, organization);

				// removing the session data.
				removeDeliveryNoteDataFromSession();
				resultSuccess.setMessage("Saved Successfully");
			} else if ("lookup-delivery-note".equals(action)) {
				List<String> productNamesList = null;
				productNamesList = deliveryNoteDao.getProductNames(organization);
				if (productNamesList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(productNamesList);
				}
			} else if ("get-product-cost".equals(action)) {
				String customerName = request.getParameter("customerNameVal");
				String productName = request.getParameter("productNameVal");
				String batchNumber = request.getParameter("batchNumberVal");
				Float productCost = deliveryNoteDao.getProductCost(productName, batchNumber, customerName, organization);
				if (productCost == null) {
					resultSuccess.setMessage("No Records Found");
					resultSuccess.setData(0.0);
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(productCost);
				}
			} else if ("get-business-name".equals(action)) {
				String businessName = request.getParameter("businessNameVal");
				List<String> businessNames = deliveryNoteDao.getBusinessName(businessName , organization, userName);
				if (businessNames.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(businessNames);
				}
			} else if ("get-grid-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String businessName = request.getParameter("businessName");
				List<ProductResult> productResultList = deliveryNoteDao.getGridData(userName, businessName, organization);
				session.setAttribute("grid-data", productResultList);
				resultSuccess.setMessage("Fetched Records Successfully");
				resultSuccess.setData(productResultList);
			} else if ("save-product-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String listOfObjects = request.getParameter("listOfProductObjects");
				String businessName = request.getParameter("businessName");
				String invoiceName = request.getParameter("invoiceName");
				String rowSteps[] = listOfObjects.split(",");
				String invoiceNo = deliveryNoteDao.generateInvoiceNo(organization);
				List<DeliveryNoteCommand> deliveryNoteList = new ArrayList<DeliveryNoteCommand>();
				for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
					if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
						deliveryNoteCommand = new DeliveryNoteCommand();
						String rowData[] = rowSteps[rowStep].split("\\|");
						String productQuantity = rowData[0].trim();
						String batchNumber = rowData[1].trim();
						String bonus = rowData[2].trim();
						String bonusReason = rowData[3].trim();
						String productName = rowData[4].trim();
						String productCost = rowData[5].trim();
						if (!StringUtils.isEmpty(productQuantity)) {
							deliveryNoteCommand.setProductQuantity(Integer.parseInt(productQuantity));
						}
						if (!StringUtils.isEmpty(productName)) {
							deliveryNoteCommand.setProductName(productName);
						}
						if (!StringUtils.isEmpty(productCost)) {
							deliveryNoteCommand.setProductCost(Float.parseFloat(productCost));
						}
						if (!StringUtils.isEmpty(bonus)) {
							deliveryNoteCommand.setBonusQuantity(Integer.parseInt(bonus));
						}
						if (!StringUtils.isEmpty(bonusReason)) {
							deliveryNoteCommand.setBonusReason(bonusReason);
						}
						if (!StringUtils.isEmpty(batchNumber)) {
							deliveryNoteCommand.setBatchNumer(batchNumber);
						}
						deliveryNoteCommand.setBusinessName(businessName);
						deliveryNoteCommand.setInvoiceName(invoiceName);
						deliveryNoteCommand.setInvoiceNo(invoiceNo);
						deliveryNoteList.add(deliveryNoteCommand);
						session.setAttribute("product-data", deliveryNoteList);
						resultSuccess.setMessage("Saved Successfully");
					}
				}
			} else if ("get-customer-data".equals(action)) {
				String businessName = request.getParameter("businessName");
				DeliveryNoteCustomerResult deliveryNoteCustomerResult = deliveryNoteDao.getCustomerData(businessName, organization);
				if (deliveryNoteCustomerResult == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(deliveryNoteCustomerResult);
				}
			} else if ("search-delivery-note".equals(action)) {
				HashMap hashMap = new HashMap();
				hashMap.put("createdBy", new SearchFilterData("createdBy", deliveryNoteCommand.getCreatedBy(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("businessName", new SearchFilterData("businessName", deliveryNoteCommand.getBusinessName(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("invoiceName", new SearchFilterData("invoiceName",deliveryNoteCommand.getInvoiceName(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("createdOn", new SearchFilterData("createdOn",deliveryNoteCommand.getCreatedOn(),SearchFilterData.TYPE_DATE_STR));
				List<DeliveryNoteResult> list = deliveryNoteDao.getDeliveryNoteResultsOnCriteria(deliveryNoteCommand, organization, userName);
				if (list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");

				}
			} else if ("go-for-payments".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				DeliveryNoteCommand command = new DeliveryNoteCommand();
				command.setBusinessName(request.getParameter("businessName"));
				command.setInvoiceName(request.getParameter("invoiceName"));
				command.setForPayments(request.getParameter("forPayments"));
				command.setInvoiceNo(deliveryNoteDao.generateInvoiceNoForPayments(organization));
				session.setAttribute("go-for-payments", command);
				resultSuccess.setMessage("Saved Successfully");
			} else if ("save-payment-delivery-note".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				deliveryNoteDao.savePayments((DeliveryNoteCommand)session.getAttribute("go-for-payments"), 
						(DeliveryNoteProductCommand)session.getAttribute("delivery-note-product"), 
						organization, userName);
				resultSuccess.setMessage("Saved Successfully");
				
				//Deleting data from session.
				removeDataFromSession();
			} else if ("check-businessname-availability".equals(action)) {
				String businessName = request.getParameter("businessName");
				Boolean isExists = deliveryNoteDao.checkBusinessName(userName, businessName, organization); 
				resultSuccess.setData(isExists);
				resultSuccess.setMessage("Successfully fetched data");
			} else if ("is-day-book-closed".equals(action)) {
				Boolean isDayBookClosed = deliveryNoteDao.isDayBookClosed(userName, organization);
				resultSuccess.setData(isDayBookClosed);
				resultSuccess.setMessage("Successfully fetched data");
			}
		} else if (form instanceof DeliveryNoteProductCommand) {
			deliveryNoteProducts = (DeliveryNoteProductCommand) form;
			session = request.getSession(Boolean.TRUE);
			session.setAttribute("delivery-note-product", deliveryNoteProducts);
			resultSuccess.setMessage("Saved Successfully");
			
			// Firing alert for Credit Limit and OverDueDays.
			String businessName = deliveryNoteProducts.getBusinessName1();
			VbCustomer customer = deliveryNoteDao.getCustomer(businessName, getOrganization());
			Float creditLimit = customer.getCreditLimit();
			Integer creditOverDueDays = customer.getCreditOverdueDays();
			if(customer != null && creditOverDueDays != null) {
				int daysDifference = DateUtils.getDifferenceDays(new Date(), customer.getCreatedOn());
				if(daysDifference > creditOverDueDays && creditLimit < deliveryNoteProducts.getBalance()) {
					String toRecipient = deliveryNoteDao.getCreatedBy(getUsername(), getOrganization());
					AlertManager.getInstance().fireSystemAlert(organization, userName, toRecipient, OrganizationUtils.ALERT_TYPE_CREDIT_OVERDUE, 
							businessName.concat(Msg.get(MsgEnum.ALERT_TYPE_CREDIT_OVERDUE_MESSAGE)));
				}
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

	/**
	 * This method is responsible to remove data from session.
	 * 
	 */
	private void removeDeliveryNoteDataFromSession() {
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("deliverynote-info");
		session.removeAttribute("delivery-note-product");
		session.removeAttribute("product-data");
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 */
	private void removeDataFromSession(){
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("go-for-payments");
		session.removeAttribute("delivery-note-product");
	}
}
