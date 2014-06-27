/**
 * com.vekomy.vbooks.mysales.cr.action.DayBookCrAction.java
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
import com.vekomy.vbooks.mysales.command.DayBookAllowancesResult;
import com.vekomy.vbooks.mysales.command.DayBookViewResult;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookAmountCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookProductsCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.cr.dao.DayBookCrDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
/**
 * This action class is responsible to process the Day Book CR activity.
 * 
 * @author ankit
 * 
 */

public class DayBookCrAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DayBookCrAction.class);
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
		try{
			ChangeRequestDayBookBasicInfoCommand dayBookBasicInfoCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			DayBookCrDao dayBookCrDao = (DayBookCrDao) getDao();
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			String persistSuccessMessage = Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE);
			if (form instanceof ChangeRequestDayBookBasicInfoCommand) {
				dayBookBasicInfoCommand = (ChangeRequestDayBookBasicInfoCommand) form;
				String action = dayBookBasicInfoCommand.getAction();
				if ("daybook-basic-info".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("daybook-basic-info", dayBookBasicInfoCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if("change-daybook-transaction-request".equals(action)) {
		        	List<ChangeRequestDayBookResult> dayBook = dayBookCrDao.getDayBookChangeTransaction( organization, userName);
		        	
					resultSuccess.setData(dayBook);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
		        } else if ("get-day-book-grid-data".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
		        	String salesExecutive=request.getParameter("salesExecutive");
					session = request.getSession(Boolean.TRUE);
					List<ChangeRequestDayBookResult> dayBookResultList = dayBookCrDao.getGridData(salesExecutive,dayBookId,userName , organization);
					
					resultSuccess.setData(dayBookResultList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-opening-balance".equals(action)) {
					Float openingBalance = dayBookCrDao.getOpeningBalance(userName , organization);
					
					resultSuccess.setData(openingBalance);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				}else if ("get-original-day-book-data".equals(action)) {
					Integer dayBookId=Integer.valueOf(request.getParameter("id"));
					DayBookViewResult originalDayBookResult  = dayBookCrDao.getOriginalDayBookData(dayBookId,organization , userName);
					
					resultSuccess.setData(originalDayBookResult);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				}
				else if ("save-day-book-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String listOfObjects = request.getParameter("listOfProductObjects");
					String productsCRRemarks=request.getParameter("productsRemarks");
					String rowSteps[] = listOfObjects.split(",");
					List<ChangeRequestDayBookProductsCommand> dayBookList = new ArrayList<ChangeRequestDayBookProductsCommand>();
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							ChangeRequestDayBookProductsCommand dayBookProduct = new ChangeRequestDayBookProductsCommand();
							String rowData[] = rowSteps[rowStep].split("\\|");
							String productName = rowData[0].trim();
							String openingStock = rowData[1].trim();
							String productToCustomer = rowData[2].trim();
							String productToFactory = rowData[3].trim();
							String closingStock = rowData[4].trim();
							String returnQty = rowData[5].trim();
							String batchNumber = rowData[6].trim();
							if (!StringUtils.isEmpty(productName)) {
								dayBookProduct.setProductName(productName);
							}
							if (!StringUtils.isEmpty(openingStock)) {
								dayBookProduct.setOpeningStock(openingStock);
							}
							if (!StringUtils.isEmpty(productToCustomer)) {
								dayBookProduct.setProductsToCustomer(productToCustomer);
							}
							if (!StringUtils.isEmpty(productToFactory)) {
								dayBookProduct.setProductsToFactory(productToFactory);
							}
							if (!StringUtils.isEmpty(closingStock)) {
								dayBookProduct.setClosingStock(closingStock);
							}
							if(!StringUtils.isEmpty(returnQty)){
								dayBookProduct.setReturnQty(returnQty);
							}
							if(!StringUtils.isEmpty(batchNumber)){
								dayBookProduct.setBatchNumber(batchNumber);
							}
							dayBookProduct.setProductsCRRemarks(productsCRRemarks);
							dayBookList.add(dayBookProduct);
							session.setAttribute("daybook-product-data", dayBookList);
						}
					}
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("save-daybook".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String dayBookNumber=request.getParameter("dayBookNo");
					dayBookCrDao.saveDayBook((ChangeRequestDayBookBasicInfoCommand)session.getAttribute("daybook-basic-info"), 
							(ChangeRequestDayBookAllowancesCommand)session.getAttribute("daybook-allowances"),
							(ChangeRequestDayBookAmountCommand) session.getAttribute("daybook-amount"),
							(ChangeRequestDayBookProductsCommand) session.getAttribute("daybook-products"), 
							(List<ChangeRequestDayBookProductsCommand>) session.getAttribute("daybook-product-data")
							,(ChangeRequestDayBookVehicleDetailsCommand)session.getAttribute("vehicle-details"),dayBookNumber,
							organization, userName, request.getParameter("isReturn"));
					
					// Firing alerts.
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DAY_BOOK, Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_RAISED_MESSAGE));
					_logger.info("Firing an alert for sales executive DayBook Change Request");
					
					// Removing data from session.
					removeDayBookFromSession(session);
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("validate-SE-DB-change-request".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
		        	String isAvailable = dayBookCrDao.validateSEDayBookChangeRequest(dayBookId, organization);
		        	
					resultSuccess.setData(isAvailable);
					resultSuccess.setMessage("SE with Change Request for Day Book Validated Successfully.");
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("search-day-book-change-request-dashboard".equals(action)) {
					List<ChangeRequestDayBookResult> list=dayBookCrDao.getDayBookCrResults(userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("approve-day-book-cr".equals(action)) {
					Integer dayBookCrId = Integer.parseInt(request.getParameter("dayBookCrId"));
					String status = request.getParameter("status");
					dayBookCrDao.getApprovedDayBookCR(dayBookCrId, status, organization, userName);
					
					// For Alerts.
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = dayBookCrDao.getCreatedBy(dayBookCrId, OrganizationUtils.CR_TYPE_DAY_BOOK);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DAY_BOOK, description);
					
					_logger.info("DayBook sales executive change request have been {}", status);
					
					resultSuccess.setMessage(description);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-day-book-creation-time".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
		        	String dayBookCreationTime = dayBookCrDao.fetchDayBookCreationTime(dayBookId,organization);
		        	
					resultSuccess.setData(dayBookCreationTime);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-daybook-id".equals(action)) {
			        String dayBookNo=request.getParameter("dayBookNumber");
			        Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
			        Integer originalDaybookId = dayBookCrDao.getDayBookBasedOnDayBookNo(dayBookId,organization, dayBookNo,userName);
			        
					resultSuccess.setData(originalDaybookId);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-day-book-history-transaction".equals(action)) {
					List<MySalesHistoryResult> dayBookHistory=dayBookCrDao.getDayBookTransactionHistory(organization,userName);
						
					resultSuccess.setData(dayBookHistory);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-day-book-invoices-history-transaction".equals(action)) {
			        String dayBookNumber=request.getParameter("dayBookNumber");
			        String status=request.getParameter("status");
					List<MySalesInvoicesHistoryResult> dayBookInvoicesHistory=dayBookCrDao.getDayBookInvoicesTransactionHistory(dayBookNumber,status,organization,userName);
					
					resultSuccess.setData(dayBookInvoicesHistory);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-starting-reading".equals(action)) {
					String dayBookType = request.getParameter("dayBookType");
					Integer salesBookId=Integer.valueOf(request.getParameter("salesBookId"));
					String startReading = dayBookCrDao.getExistedStartReading(salesBookId,dayBookType,userName,getOrganization());
					
					resultSuccess.setData(startReading);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-allowances".equals(action)) {
					Integer salesBookId=Integer.valueOf(request.getParameter("salesBookId"));
					List<DayBookAllowancesResult> allowancesResultList = dayBookCrDao.getAllowances(salesBookId,userName, organization);
					
					resultSuccess.setData(allowancesResultList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-allowances-details".equals(action)) {
					String dayBookType = request.getParameter("dayBookType");
					Integer salesBookId=Integer.valueOf(request.getParameter("salesBookId"));
					List<DayBookAllowancesResult> allowancesResultList = dayBookCrDao.getExecutiveAllowances(salesBookId,userName, organization,dayBookType);
					
					resultSuccess.setData(allowancesResultList);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("update-changed-allowance".equals(action)) {
					Integer cashDayBookId = Integer.parseInt(request.getParameter("id"));
					String valueOne = request.getParameter("valueOne");
					String valueThree = request.getParameter("valueThree");
					dayBookCrDao.updateChangedAllowance(cashDayBookId, valueOne, valueThree);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-original-day-book-cash-details".equals(action)) {
					Integer salesBookId=Integer.valueOf(request.getParameter("salesBookId"));
					dayBookCrDao.persistOriginalCashDayBookDetails(salesBookId, getUsername(), organization);
					
					resultSuccess.setMessage(persistSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("check-available-deposit-amount".equals(action)){
					Float amount = dayBookCrDao.getPresentPayment(userName, organization);
					
					resultSuccess.setData(amount);
					resultSuccess.setMessage(resultSuccessMessage);
					resultSuccess.setStatus(resultSuccessStatus);
				}
				//remove day book CR session data on click of cancel button click
				else if("remove-day-book-cr-session-data".equals(action)){
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					removeDayBookFromSession(session);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			} else if(form instanceof ChangeRequestDayBookAllowancesCommand) {
				ChangeRequestDayBookAllowancesCommand dayBookAllowancesCommand = (ChangeRequestDayBookAllowancesCommand) form;
				session.setAttribute("daybook-allowances", dayBookAllowancesCommand);
				
				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			} else if(form instanceof ChangeRequestDayBookAmountCommand) {
				ChangeRequestDayBookAmountCommand dayBookAmountCommand = (ChangeRequestDayBookAmountCommand) form;
				session.setAttribute("daybook-amount", dayBookAmountCommand);
				
				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			} else if(form instanceof ChangeRequestDayBookProductsCommand) {
				ChangeRequestDayBookProductsCommand dayBookProductsCommand = (ChangeRequestDayBookProductsCommand) form;
				session.setAttribute("daybook-products", dayBookProductsCommand);
				
				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			} else if(form instanceof ChangeRequestDayBookVehicleDetailsCommand){
				ChangeRequestDayBookVehicleDetailsCommand dayBookVehicleDetailsCommand = (ChangeRequestDayBookVehicleDetailsCommand) form;
				session.setAttribute("vehicle-details", dayBookVehicleDetailsCommand);
				
				resultSuccess.setMessage(persistSuccessMessage);
				resultSuccess.setStatus(resultSuccessStatus);
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		} catch (ParseException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}
	/**
	 * Removing data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeDayBookFromSession(HttpSession session) {
		session.removeAttribute("daybook-basic-info");
		session.removeAttribute("daybook-amount");
		session.removeAttribute("daybook-allowances");
		session.removeAttribute("daybook-products");
		session.removeAttribute("daybook-product-data");
		session.removeAttribute("vehicle-details");
	}

}
