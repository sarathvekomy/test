/**
 * com.vekomy.vbooks.mysales.cr.action.SalesReturnCrAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: September 26, 2013
 */
package com.vekomy.vbooks.mysales.cr.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesReturn;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestSalesReturnsResult;
import com.vekomy.vbooks.mysales.cr.dao.SalesReturnCrDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to process the Sales Return CR activity.
 * 
 * @author ankit
 * 
 */
public class SalesReturnCrAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesReturnCrAction.class);
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

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
			SalesReturnCrDao salesReturnCrDao = (SalesReturnCrDao) getDao();
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			if (form instanceof ChangeRequestSalesReturnCommand) {
				ChangeRequestSalesReturnCommand salesReturnCommand = (ChangeRequestSalesReturnCommand) form;
				String action = salesReturnCommand.getAction();
				if ("change-sales-transaction-request".equals(action)) {
					List<ChangeRequestSalesReturnResult> salesReturnResult = salesReturnCrDao.getSalesReturnsOnLoad(userName, organization);
					
					resultSuccess.setData(salesReturnResult);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-grid-data".equals(action)) {
					// session = request.getSession(Boolean.TRUE);
					String businessName = request.getParameter("businessName");
					Integer salesReturnId = Integer.valueOf(request.getParameter("salesReturnId"));
					List<ChangeRequestSalesReturnsResult> salesReturnsResult = salesReturnCrDao .getSalesReturnGridData(salesReturnId, userName, businessName, organization);
					
					resultSuccess.setData(salesReturnsResult);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-original-sales-return-data".equals(action)){
					int id = Integer.parseInt(request.getParameter("id"));
					VbSalesReturn originalSalesReturn= salesReturnCrDao.getSalesReturn(id, organization, userName);
					
					resultSuccess.setData(originalSalesReturn);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} 
				else if ("get-quantity-sold".equals(action)) {
					String businessName = request.getParameter("businessName");
					String productName = request.getParameter("productName");
					String batchNumber = request.getParameter("batchNumber");
					Integer openingStock = salesReturnCrDao.getQtySold(businessName, productName, batchNumber, organization);
					
					resultSuccess.setData(openingStock);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("save-sales-return-products".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String listOfObjects = request.getParameter("listOfProductObjects");
					String businessName = request.getParameter("businessName");
					String invoiceName = request.getParameter("invoiceName");
					String invoiceNumber = request.getParameter("invoiceNumber");
					String remarks = request.getParameter("remarks");
					String description = request.getParameter("description");
					String rowSteps[] = listOfObjects.split(",");
					List<ChangeRequestSalesReturnCommand> salesReturnList = new ArrayList<ChangeRequestSalesReturnCommand>();
					ChangeRequestSalesReturnCommand command = null;
					String[] rowData = null;
					String damaged = null;
					String resalable = null;
					String returnQty = null;
					String batchNumber = null;
					String productName = null;
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							command = new ChangeRequestSalesReturnCommand();
							rowData = rowSteps[rowStep].split("\\|");
							damaged = rowData[0].trim();
							resalable = rowData[1].trim();
							returnQty = rowData[2].trim();
							batchNumber = rowData[3].trim();
							productName = rowData[4].trim();
							if (!StringUtils.isEmpty(damaged)) {
								command.setDamaged(damaged);
							}
							if (!StringUtils.isEmpty(resalable)) {
								command.setResalable(resalable);
							}
							if (!StringUtils.isEmpty(returnQty)) {
								command.setTotalQty(returnQty);
							}
							command.setProductName(productName);
							command.setBatchNumber(batchNumber);
							command.setBusinessName(businessName);
							command.setInvoiceName(invoiceName);
							command.setRemarks(remarks);
							command.setInvoiceNo(invoiceNumber);
							command.setCrDescription(description);
							
							salesReturnList.add(command);
						}
					}
					session.setAttribute("save-sales-return-products", salesReturnList);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("validate-SE-SR-change-request".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					String data = salesReturnCrDao.validateSESalesReturnChangeRequest(invoiceNumber, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("search-sales-return-change-request-dashboard".equals(action)) {
					List<ChangeRequestSalesReturnResult> list = salesReturnCrDao.getSalesReturnCrResults(userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("save-sales-returns".equals(action)) {
					String originalSRInvoiceNumber = request.getParameter("invoiceNumber");
					salesReturnCrDao.saveSalesReturns((List<ChangeRequestSalesReturnCommand>) session.getAttribute("save-sales-return-products"), originalSRInvoiceNumber, organization, userName);
					
					// Firing alerts.
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES,
									OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN,
									Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_RAISED_MESSAGE));
					_logger.info("Firing an alert for sales executive sales return change request.");

					// Removing session data.
					session.removeAttribute("save-sales-return-products");
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("approve-sales-return-cr".equals(action)) {
					ChangeRequestSalesReturnCommand command = null;
					Integer salesReturnCRId = Integer.parseInt(request.getParameter("salesReturnCRId"));
					String status = request.getParameter("status");
					String listOfObjects = request.getParameter("listOfSalesReturnObjects");
					List<ChangeRequestSalesReturnCommand> salesReturnList = new ArrayList<ChangeRequestSalesReturnCommand>();
					if(listOfObjects != null) {
						String rowSteps[] = listOfObjects.split(",");
						String damagedCost = null;
						String resalableCost = null;
						String totalResalableCost = null;
						String totalDamagedCost = null;
						String totalCost = null;
						String grandTotal = null;
						String[] rowData = null;
						String damaged = null;
						String resalable = null;
						String returnQty = null;
						String batchNumber = null;
						String productName = null;
						for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
							if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
								command = new ChangeRequestSalesReturnCommand();
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
								grandTotal = rowData[10].trim();
								if (!StringUtils.isEmpty(damaged)) {
									command.setDamaged(damaged);
								}
								if (!StringUtils.isEmpty(resalable)) {
									command.setResalable(resalable);
								}
								if (!StringUtils.isEmpty(damagedCost)) {
									command.setDamagedCost(damagedCost);
								}
								if (!StringUtils.isEmpty(resalableCost)) {
									command.setResalableCost(resalableCost);
								}
								if (!StringUtils.isEmpty(totalResalableCost)) {
									command.setTotalResalableCost(Float.parseFloat(totalResalableCost));
								}
								if (!StringUtils.isEmpty(totalDamagedCost)) {
									command.setTotalDamagedCost(Float.parseFloat(totalDamagedCost));
								}
								if (!StringUtils.isEmpty(returnQty)) {
									command.setTotalQty(returnQty);
								}
								if (!StringUtils.isEmpty(totalCost)) {
									command.setTotalCost(totalCost);
								}
								if (!StringUtils.isEmpty(grandTotal)) {
									command.setGrandTotalCost(grandTotal);
								}
								command.setProductName(productName);
								command.setBatchNumber(batchNumber);
								
								salesReturnList.add(command);
							}
						}
					}
					salesReturnCrDao.getApprovedSalesReturnCR(organization, userName, status, salesReturnCRId, salesReturnList);
					
					// For Alerts.
					String description = null;
					if (OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = salesReturnCrDao.getCreatedBy(salesReturnCRId, OrganizationUtils.CR_TYPE_SALES_RETURNS);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES,
									OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN, description);
					_logger.info("Firing an alert for sales executive Sales Return change request.");
					
					resultSuccess.setData(description);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-sales-return-id".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer salesReturnId = Integer.valueOf(request.getParameter("salesReturnId"));
					Integer data = salesReturnCrDao.getSalesReturnBasedOnInvoiceNo(organization, invoiceNumber, salesReturnId, userName);
							
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-status".equals(action)) {
					Integer salesReturnId = Integer.valueOf(request.getParameter("id"));
					VbSalesReturn salesReturn = salesReturnCrDao.getSalesReturnStatus(organization, salesReturnId, userName);
					
					resultSuccess.setData(salesReturn);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-creation-time".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer salesReturnId = Integer.valueOf(request.getParameter("salesReturnId"));
					String data = salesReturnCrDao.fetchSalesReturnCreationTime(salesReturnId, invoiceNumber, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-history-transaction".equals(action)) {
					List<MySalesHistoryResult> salesReturnHistory = salesReturnCrDao.getSalesReturnTransactionHistory(organization, userName);
					
					resultSuccess.setData(salesReturnHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-sales-return-invoices-history-transaction".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					String status = request.getParameter("status");
					List<MySalesInvoicesHistoryResult> salesReturninvoicesHistory = salesReturnCrDao.getSalesReturnInvoicesTransactionHistory(invoiceNumber, status, organization, userName);
					
					resultSuccess.setData(salesReturninvoicesHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("check-Transaction-Sales-Return".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer data = salesReturnCrDao.checkTransactionSalesReturn(invoiceNumber, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				}
				//remove SR CR data on click of cancel in edit page
				else if ("remove-sales-return-cr-session-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					removeSalesReturnDataFromSession(session);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		} catch (ParseException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
	/**
	 * This method is responsible to remove data from session.
	 * 
	 * @param session - {@link HttpSession}
	 * 
	 */
	private void removeSalesReturnDataFromSession(HttpSession session) {
		session.removeAttribute("save-sales-return-products");
	}
}
