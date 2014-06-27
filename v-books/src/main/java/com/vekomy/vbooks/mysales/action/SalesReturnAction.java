/**
 * com.vekomy.vbooks.mysales.action.SalesReturnAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.mysales.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.SalesReturnCommand;
import com.vekomy.vbooks.mysales.command.SalesReturnResult;
import com.vekomy.vbooks.mysales.command.SalesReturnsResult;
import com.vekomy.vbooks.mysales.dao.SalesReturnDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This action class is responsible to process sales returns requests.
 * 
 * @author Sudhakar
 * 
 */
public class SalesReturnAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		SalesReturnCommand salesReturnCommand = null;
		
		SalesReturnDao salesReturnDao = (SalesReturnDao) getDao();
		if (form instanceof SalesReturnCommand) {
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			salesReturnCommand = (SalesReturnCommand) form;
			String action = salesReturnCommand.getAction();
			HttpSession session = request.getSession();
			if ("save-sales-returns".equals(action)) {
				salesReturnDao.saveSalesReturns((List<SalesReturnCommand>) session.getAttribute("save-sales-return-products"), 
						organization, userName);
				resultSuccess.setMessage("Saved Successfully");
				
				//For Alerts.
				String defaultToRecipient = salesReturnDao.getCreatedBy(userName, organization);
				AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES, 
						OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS, OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN,
						Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURN_RAISED_MESSAGE));
				_logger.info("Firing an alert for SalesReturn Request");
				
				// Removing session data.
				session.removeAttribute("save-sales-return-products");
			} else if ("save-sales-return-products".equals(action)) {
				String listOfObjects = request.getParameter("listOfProductObjects");
				String businessName = request.getParameter("businessName");
				String invoiceName = request.getParameter("invoiceName");
				String grandTotalCost = request.getParameter("grandTotalCost");
				String rowSteps[] = listOfObjects.split(",");
				List<SalesReturnCommand> salesReturnList = new ArrayList<SalesReturnCommand>();
				SalesReturnCommand command = null;
				String invoiceNo = salesReturnDao.generateInvoiceNo(getOrganization());
				for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
					if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
						command = new SalesReturnCommand();
						String rowData[] = rowSteps[rowStep].split("\\|");
						String damaged = rowData[0].trim();
						String resalable = rowData[1].trim();
						String cost = rowData[2].trim();
						String returnQty = rowData[3].trim();
						String totalCost = rowData[4].trim();
						String batchNumber = rowData[5].trim();
						String productName = rowData[6].trim();
						if(!StringUtils.isEmpty(damaged)){
							command.setDamaged(Integer.parseInt(damaged));
						}
						
						if(!StringUtils.isEmpty(resalable)){
							command.setResalable(Integer.parseInt(resalable));
						}
						
						if(!StringUtils.isEmpty(cost)){
							command.setCost(Float.parseFloat(cost));
						}
						
						if (!StringUtils.isEmpty(returnQty)) {
							command.setTotalQty(Integer.parseInt(returnQty));
						}
						
						if(!StringUtils.isEmpty(totalCost)){
							command.setTotalCost(Float.parseFloat(totalCost));
						}
						
						command.setProductName(productName);
						command.setBatchNumber(batchNumber);
						command.setBusinessName(businessName);
						command.setInvoiceName(invoiceName);
						command.setGrandTotalCost(Float.parseFloat(grandTotalCost));
						command.setInvoiceNo(invoiceNo);
						salesReturnList.add(command);
					}
				}
				session.setAttribute("save-sales-return-products", salesReturnList);
			} else if ("search-sales-return".equals(action)) {
				HashMap hashMap = new HashMap();
				hashMap.put("createdBy", new SearchFilterData("createdBy", salesReturnCommand.getCreatedBy(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("businessName", new SearchFilterData("businessName", salesReturnCommand.getBusinessName(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("invoiceName", new SearchFilterData("invoiceName",salesReturnCommand.getInvoiceName(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("createdOn", new SearchFilterData("createdOn",salesReturnCommand.getCreatedOn(),SearchFilterData.TYPE_DATE_STR));
				List list=salesReturnDao.getSalesReturns(salesReturnCommand, userName, organization);
				if(list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
				} else if ("search-sales-return-data-onload".equals(action)) {
				List list=salesReturnDao.getSalesReturnsOnLoad(userName, organization);
				if(list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			}else if ("search-sales-return-dashboard".equals(action)) {
				List<SalesReturnResult> list=salesReturnDao.getSalesReturnsDashboard(organization);
				if(list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			} else if ("get-customer-invoice-name".equals(action)) {
				String businessName = request.getParameter("businessName");
				String invoiceName = salesReturnDao.getInvoiceName(businessName , organization);
				if (invoiceName == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(invoiceName);
				}
			}  else if ("sales-return-approval".equals(action)) {
					Integer salesReturnId = Integer.parseInt(request.getParameter("salesReturnId"));
					String status = request.getParameter("status");
					salesReturnDao.approveOrDeclineSalesReturn(salesReturnId, status, organization, userName);
					
					//For Alerts.
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURN_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURN_DECLINED_MESSAGE);
					}
					String defaultToRecipient = salesReturnDao.getCreatedBy(salesReturnId);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES, 
							OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS, OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN, description);
					
					_logger.info("Firing an alert for SalesReturn Request");
					
					resultSuccess.setMessage("SalesReturn Products Approved Successfully");
			} else if ("get-business-name".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNames = salesReturnDao.getBusinessName(businessName , organization , userName);
					if (businessNames.isEmpty()) {
						resultSuccess.setMessage("No Records Found");
					} else {
						resultSuccess.setMessage("Fetched Records Successfully");
						resultSuccess.setData(businessNames);
					}
			} else if ("get-grid-data".equals(action)) {
				String businessName = request.getParameter("businessName");
				List<SalesReturnsResult> salesReturnsResults = salesReturnDao.getGridData(businessName , organization, userName);
				if (salesReturnsResults == null) {
					resultSuccess.setMessage("No Products available");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(salesReturnsResults);
				}
			} else if ("check-businessname-availability".equals(action)) {
				String businessName = request.getParameter("businessName");
				Boolean isExists = salesReturnDao.checkBusinessName(userName, businessName, organization); 
				resultSuccess.setData(isExists);
				resultSuccess.setMessage("Successfully fetched data");
			} else if ("get-quantity-sold".equals(action)) {
				String businessName = request.getParameter("businessName");
				String productName = request.getParameter("productName");
				String batchNumber = request.getParameter("batchNumber");
				Integer openingStock = salesReturnDao.getQtySold(businessName, productName, batchNumber, organization); 
				resultSuccess.setData(openingStock);
				resultSuccess.setMessage("Successfully fetched data");
			} else if ("is-day-book-closed".equals(action)) {
				Boolean isDayBookClosed = salesReturnDao.isDayBookClosed(userName, organization);
				resultSuccess.setData(isDayBookClosed);
				resultSuccess.setMessage("Successfully fetched data");
			}
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("ResultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

}
