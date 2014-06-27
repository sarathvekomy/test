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
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.SalesReturnCommand;
import com.vekomy.vbooks.mysales.command.SalesReturnResult;
import com.vekomy.vbooks.mysales.command.SalesReturnViewResult;
import com.vekomy.vbooks.mysales.command.SalesReturnsResult;
import com.vekomy.vbooks.mysales.dao.SalesReturnDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

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
	
	/**
	 * String variable holds SAVE_SALES_RETURNS_PRODUCTS.
	 */
	private static final String SAVE_SALES_RETURNS_PRODUCTS = "save-sales-return-products";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IResult process(Object form) {
		try {
			ResultSuccess resultSuccess = new ResultSuccess();
			SalesReturnCommand salesReturnCommand = null;
			SalesReturnDao salesReturnDao = (SalesReturnDao) getDao();
			if (form instanceof SalesReturnCommand) {
				String userName = getUsername();
				VbOrganization organization = getOrganization();
				salesReturnCommand = (SalesReturnCommand) form;
				String action = salesReturnCommand.getAction();
				String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
				String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
				HttpSession session = request.getSession(Boolean.TRUE);
				if ("save-sales-returns".equals(action)) {
					salesReturnDao.saveSalesReturns((List<SalesReturnCommand>) session.getAttribute(SAVE_SALES_RETURNS_PRODUCTS), organization, userName);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
					
					// Removing session data.
					removeDataFromSession(session);
					
					//For Alerts.
					String defaultToRecipient = salesReturnDao.getCreatedBy(userName, organization);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES, 
							OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS, OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN,
							Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURN_RAISED_MESSAGE));
					
					_logger.info("Firing an alert for SalesReturn Request");
				} else if ("save-sales-return-products".equals(action)) {
					String listOfObjects = request.getParameter("listOfProductObjects");
					String businessName = request.getParameter("businessName");
					String invoiceName = request.getParameter("invoiceName");
					String remarks = request.getParameter("remarks");
					String grandTotalCost = request.getParameter("grandTotalCost");
					String rowSteps[] = listOfObjects.split("\\?");
					List<SalesReturnCommand> salesReturnList = new ArrayList<SalesReturnCommand>();
					SalesReturnCommand command = null;
					String invoiceNo = (String) session.getAttribute("invoiceNo");
					if(invoiceNo == null) {
						invoiceNo = salesReturnDao.generateInvoiceNo(organization);
						session.setAttribute("invoiceNo", invoiceNo);
					}
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							command = new SalesReturnCommand();
							String rowData[] = rowSteps[rowStep].split("\\|");
							String damaged = rowData[0].trim();
							String resalable = rowData[1].trim();
							String returnQty = rowData[2].trim();
							String batchNumber = rowData[3].trim();
							String productName = rowData[4].trim();
							if(!StringUtils.isEmpty(damaged)){
								command.setDamaged(Integer.parseInt(damaged));
							}
							if(!StringUtils.isEmpty(resalable)){
								command.setResalable(Integer.parseInt(resalable));
							}
							if (!StringUtils.isEmpty(returnQty)) {
								command.setTotalQty(Integer.parseInt(returnQty));
							}
							command.setProductName(productName);
							command.setBatchNumber(batchNumber);
							command.setBusinessName(businessName);
							command.setInvoiceName(invoiceName);
							command.setRemarks(remarks);
							command.setGrandTotalCost(Float.parseFloat(grandTotalCost));
							command.setInvoiceNo(invoiceNo);
							salesReturnList.add(command);
						}
					}
					session.setAttribute(SAVE_SALES_RETURNS_PRODUCTS, salesReturnList);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-sales-return".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("createdBy", new SearchFilterData("createdBy", salesReturnCommand.getCreatedBy(),SearchFilterData.TYPE_STRING_STR));
					hashMap.put("businessName", new SearchFilterData("businessName", salesReturnCommand.getBusinessName(),SearchFilterData.TYPE_STRING_STR));
					hashMap.put("invoiceName", new SearchFilterData("invoiceName",salesReturnCommand.getInvoiceName(),SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn",salesReturnCommand.getCreatedOn(),SearchFilterData.TYPE_DATE_STR));
					List<SalesReturnResult> list = salesReturnDao.getSalesReturns(salesReturnCommand, userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-sales-return-data-onload".equals(action)) {
					List<SalesReturnResult> list=salesReturnDao.getSalesReturnsOnLoad(userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-sales-return-dashboard".equals(action)) {
					List<SalesReturnResult> list = salesReturnDao.getSalesReturnsDashboard(organization, userName);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-customer-invoice-name".equals(action)) {
					String businessName = request.getParameter("businessName");
					String invoiceName = salesReturnDao.getInvoiceName(businessName , organization);
					
					resultSuccess.setData(invoiceName);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("sales-return-approval".equals(action)) {
					SalesReturnCommand command = null;
					List<SalesReturnCommand> salesReturnList = new ArrayList<SalesReturnCommand>();
					Integer salesReturnId = Integer.parseInt(request.getParameter("salesReturnId"));
					String status = request.getParameter("status");
					String grandTotal = null;
					String listOfObjects = request.getParameter("listOfSalesReturnObjects");
					if(listOfObjects != null){
						String rowSteps[] = listOfObjects.split(",");
						String damagedCost = null;
						String resalableCost = null;
						String totalResalableCost = null;
						String totalDamagedCost = null;
						String totalCost = null;
						String[] rowData = null;
						String damaged = null;
						String resalable = null;
						String returnQty = null;
						String batchNumber = null;
						String productName = null;
						for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
							if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
								command = new SalesReturnCommand();
								rowData = rowSteps[rowStep].split("\\|");
								damaged = rowData[0].trim();
								resalable = rowData[1].trim();
								damagedCost = rowData[2].trim();
								resalableCost = rowData[3].trim();
								totalResalableCost = rowData[4].trim();
								totalDamagedCost = rowData[5].trim();
								returnQty = rowData[6].trim();
								totalCost = rowData[7].trim();
								batchNumber = rowData[8].trim();
								productName = rowData[9].trim();
								grandTotal =rowData[10].trim();
								if(!StringUtils.isEmpty(damaged)){
									command.setDamaged(Integer.parseInt(damaged));
								}
								if(!StringUtils.isEmpty(resalable)){
									command.setResalable(Integer.parseInt(resalable));
								}
								if(!StringUtils.isEmpty(damagedCost)){
									command.setDamagedCost(Float.parseFloat(damagedCost));
								}
								if(!StringUtils.isEmpty(resalableCost)){
									command.setResalableCost(Float.parseFloat(resalableCost));
								}
								if(!StringUtils.isEmpty(totalResalableCost)){
									command.setTotalResalableCost(Float.parseFloat(totalResalableCost));
								}
								if(!StringUtils.isEmpty(totalDamagedCost)){
									command.setTotalDamagedCost(Float.parseFloat(totalDamagedCost));
								}
								if (!StringUtils.isEmpty(returnQty)) {
									command.setTotalQty(Integer.parseInt(returnQty));
								}
								if(!StringUtils.isEmpty(totalCost)){
									command.setTotalCost(Float.parseFloat(totalCost));
								}
								if(!StringUtils.isEmpty(grandTotal)){
									command.setGrandTotalCost(Float.parseFloat(grandTotal));
								}
								command.setProductName(productName);
								command.setBatchNumber(batchNumber);
								salesReturnList.add(command);
							}
						}
					}
					salesReturnDao.approveOrDeclineSalesReturn(salesReturnId, status, organization, userName, grandTotal, salesReturnList);
					
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
					
					resultSuccess.setMessage(description);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-business-name".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNames = salesReturnDao.getBusinessName(businessName , organization , userName);
					
					resultSuccess.setData(businessNames);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-grid-data".equals(action)) {
					String businessName = request.getParameter("businessName");
					List<SalesReturnsResult> salesReturnsResults = salesReturnDao.getGridData(businessName , organization, userName);
					
					resultSuccess.setData(salesReturnsResults);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("check-businessname-availability".equals(action)) {
					String businessName = request.getParameter("businessName");
					Boolean isExists = salesReturnDao.checkBusinessName(userName, businessName, organization); 
					
					resultSuccess.setData(isExists);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-quantity-sold".equals(action)) {
					String businessName = request.getParameter("businessName");
					String productName = request.getParameter("productName");
					String batchNumber = request.getParameter("batchNumber");
					Integer openingStock = salesReturnDao.getQtySold(businessName, productName, batchNumber, organization);
					
					resultSuccess.setData(openingStock);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				}else if("get-quantity-sold-for-approvals".equals(action)){
					String businessName = request.getParameter("businessName");
					String productName = request.getParameter("productName");
					String batchNumber = request.getParameter("batchNumber");
					String requestMode = null ;
					Integer salesReturnId =0;
					if(request.getParameter("salesReturnId") == null ){
						requestMode = request.getParameter("mode");
					}else{
						 salesReturnId = Integer.parseInt(request.getParameter("salesReturnId"));
					}
					Integer openingStock = salesReturnDao.getQtySoldForApprovals(businessName, productName, batchNumber, organization,salesReturnId);
					
					resultSuccess.setData(openingStock);
					resultSuccess.setStatus(resultSuccessStatus);
					if(openingStock < 0){
						resultSuccess.setMessage("No Product is available with the given Product Name :".concat(productName).concat(" And Batch Number :").concat(batchNumber));	
					}
				}else if("get-quantity-sold-for-approvals-cr".equals(action)){

					String businessName = request.getParameter("businessName");
					String productName = request.getParameter("productName");
					String batchNumber = request.getParameter("batchNumber");
					Integer salesReturnId =0;
					String invoiceNo = request.getParameter("invoiceNo");
					Integer totalQuantity = Integer.parseInt(request.getParameter("totalQuantity"));
					Integer openingStock = salesReturnDao.getQtySoldForCrApprovals(businessName, productName, batchNumber, organization,salesReturnId,invoiceNo,totalQuantity);
					
					resultSuccess.setData(openingStock);
					resultSuccess.setStatus(resultSuccessStatus);
					if(openingStock < 0){
						resultSuccess.setMessage("No Product is available with the given Product Name :".concat(productName).concat(" And Batch Number :").concat(batchNumber));	
					}
				
				}
				else if ("is-day-book-closed".equals(action)) {
					Boolean isDayBookClosed = salesReturnDao.isDayBookClosed(userName, organization);
					
					resultSuccess.setData(isDayBookClosed);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-for-view".equals(action)) {
					List<SalesReturnViewResult> salesReturnList = salesReturnDao.getSalesReturnProductsDetails(salesReturnCommand.getId(), organization);
					
					resultSuccess.setData(salesReturnList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-previous-sales-return-id".equals(action)) {
		        	String invoiceNumber = request.getParameter("invoiceNumber");
		        	Integer salesReturnId = Integer.valueOf(request.getParameter("salesReturnId"));
		        	Integer result = salesReturnDao.getPreviousSalesReturnInvoiceNumber(salesReturnId, invoiceNumber, organization, userName);
		        	
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessMessage);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-approval-sales-return-history-transaction".equals(action)) {
					List<MySalesHistoryResult> salesReturnHistory = salesReturnDao.getSalesReturnTransactionHistory(organization, userName);
					
					resultSuccess.setData(salesReturnHistory);
					resultSuccess.setStatus(resultSuccessMessage);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-approval-sales-return-invoices-history-transaction".equals(action)) {
		        	String invoiceNumber = request.getParameter("invoiceNumber");
		        	String status = request.getParameter("status");
					List<MySalesInvoicesHistoryResult> journalInvoicesHistory = salesReturnDao.getSalesReturnInvoicesTransactionHistory(invoiceNumber, status, organization, userName);
					
					resultSuccess.setData(journalInvoicesHistory);
					resultSuccess.setStatus(resultSuccessMessage);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} 
				//removing data from session on click of cancel button in SR  add page
				else if ("remove-sales-return-session-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					removeDataFromSession(session);
					resultSuccess.setStatus(resultSuccessMessage);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError =  getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * Removing data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeDataFromSession(HttpSession session) {
		session.removeAttribute(SAVE_SALES_RETURNS_PRODUCTS);
		session.removeAttribute("invoiceNo");
	}
}