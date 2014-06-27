/**
 * com.vekomy.vbooks.mysales.action.DayBookAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 22, 2013
 */
package com.vekomy.vbooks.mysales.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.exception.ProcessingException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.DayBookAllowancesResult;
import com.vekomy.vbooks.mysales.command.DayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.DayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.DayBookCommand;
import com.vekomy.vbooks.mysales.command.DayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.command.DayBookViewResult;
import com.vekomy.vbooks.mysales.command.TempDayBookCommand;
import com.vekomy.vbooks.mysales.dao.DayBookDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to process the day book activity.
 * 
 * @author Sudhakar
 * 
 */
public class DayBookAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DayBookAction.class);
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
			DayBookCommand dayBookCommand = null;
			DayBookDao dayBookDao = (DayBookDao) getDao();
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			String persistSuccessMessage = Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE);
			String updateSuccessMessage = Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE);
			String deleteSuccessMessage = Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE);
			session = request.getSession(Boolean.TRUE);
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			if(form instanceof DayBookBasicInfoCommand) {
				DayBookBasicInfoCommand dayBookBasicInfoCommand = (DayBookBasicInfoCommand)form;
				String action = dayBookBasicInfoCommand.getAction();
				if("daybook-basic-info".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String createdDate =dayBookDao.getSalesExecutiveStartDate(userName,getOrganization());
					dayBookBasicInfoCommand.setStartDate(DateUtils.parse(createdDate));
					session.setAttribute("daybook-basic-info", dayBookBasicInfoCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if ("get-sales-executive-name".equals(action)) {
					String salesExecutiveName = userName;
					
					resultSuccess.setData(salesExecutiveName);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-all-sales-executive-names".equals(action)) {
					String executiveName = request.getParameter("salesExecutive");
					List <String> salesExecutiveList = dayBookDao.getAllSalesExecutives(organization, executiveName);
					
					resultSuccess.setData(salesExecutiveList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-existed-vehicle-details".equals(action)){
					List<DayBookAllowancesResult> existedVehicleRecordList = dayBookDao.getExistedVehicleDetails(userName, organization);
					
					resultSuccess.setData(existedVehicleRecordList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("save-day-book-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String productsRemarks = request.getParameter("productsRemarks");
					String listOfObjects = request.getParameter("listOfProductObjects");
					String rowSteps[] = listOfObjects.split("\\?");
					List<DayBookProductsCommand> dayBookList = new ArrayList<DayBookProductsCommand>();
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							DayBookProductsCommand dayBookProduct = new DayBookProductsCommand();
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
								dayBookProduct.setOpeningStock(Integer.parseInt(openingStock));
							}
							if (!StringUtils.isEmpty(productToCustomer)) {
								dayBookProduct.setProductsToCustomer(Integer.parseInt(productToCustomer));
							}
							if (!StringUtils.isEmpty(productToFactory)) {
								dayBookProduct.setProductsToFactory(Integer.parseInt(productToFactory));
							}
							if (!StringUtils.isEmpty(closingStock)) {
								dayBookProduct.setClosingStock(Integer.parseInt(closingStock));
							}
							if(!StringUtils.isEmpty(returnQty)){
								dayBookProduct.setReturnQty(Integer.parseInt(returnQty));
							}
							if(!StringUtils.isEmpty(batchNumber)){
								dayBookProduct.setBatchNumber(batchNumber);
							}
							dayBookProduct.setProductsRemarks(productsRemarks);
							dayBookList.add(dayBookProduct);
						}
					}
					session.setAttribute("daybook-product-data", dayBookList);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if ("save-daybook".equals(action)) {
					Boolean isClosingStockAvailable = Boolean.FALSE;
					String description = Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_HAVING_CLOSING_STOCK_MESSAGE);
					String dayBookNo = dayBookDao.generateDayBookNo(organization);
					DayBookBasicInfoCommand basicInfoCommand = (DayBookBasicInfoCommand)session.getAttribute("daybook-basic-info");
					DayBookAmountCommand amountCommand = (DayBookAmountCommand) session.getAttribute("daybook-amount");
					List<DayBookProductsCommand> productsCommandList = (List<DayBookProductsCommand>) session.getAttribute("daybook-product-data");
					dayBookDao.saveDayBook(basicInfoCommand, (DayBookAllowancesCommand)session.getAttribute("daybook-allowances"), amountCommand,
							productsCommandList, (DayBookVehicleDetailsCommand)session.getAttribute("vehicle-details"),
							organization, userName, request.getParameter("isReturn"), dayBookNo);
					
					// Firing alert, for DayBook closure.
					String	toRecipient = dayBookDao.getCretedBy(userName, organization);
					AlertManager.getInstance().fireSystemAlert(organization, userName, toRecipient, 
							OrganizationUtils.ALERT_TYPE_DAY_BOOK_CLOSURE, userName.concat(" ").concat(Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CLOSURE_MESSAGE)));
					
					for (DayBookProductsCommand dayBookProductsCommand : productsCommandList) {
						Integer closingStock = dayBookProductsCommand.getClosingStock();
						if(closingStock != 0) {
							isClosingStockAvailable = Boolean.TRUE;
						}
					}
					Float closingBalance = amountCommand.getClosingBalance();
					if(closingBalance != 0.0 || isClosingStockAvailable) {
						AlertManager.getInstance().fireSystemAlert(organization, userName, toRecipient, 
								OrganizationUtils.ALERT_TYPE_DAY_BOOK_HAVING_CLOSING_STOCK, userName.concat(" ").concat(description));
						
						if(_logger.isInfoEnabled()) {
							_logger.info(description);
						}
					}
					// Removing data from session.
					removeDayBookFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if ("save-cancel".equals(action)) {
					removeDayBookFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.CANCEL_SUCCESS_MESSAGE));
				} else if("check-day-book".equals(action)) {
					String salesExecutive = request.getParameter("salesExecutive");
					Boolean isDayBookClosed = dayBookDao.isDayBookClosed(salesExecutive, organization);
					
					resultSuccess.setData(isDayBookClosed);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-allowances".equals(action)) {
					List<DayBookAllowancesResult> allowancesResultList = dayBookDao.getAllowances(userName, organization);
					
					resultSuccess.setData(allowancesResultList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-allowances-details".equals(action)) {
					String dayBookType = request.getParameter("dayBookType");
					List<DayBookAllowancesResult> allowancesResultList = dayBookDao.getExecutiveAllowances(userName, organization,dayBookType);
					
					resultSuccess.setData(allowancesResultList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-starting-reading".equals(action)){
					String dayBookType = request.getParameter("dayBookType");
					String startReading = dayBookDao.getExistedStartReading(dayBookType,userName,getOrganization());
					
					resultSuccess.setData(startReading);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-business-name".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNames = dayBookDao.getBusinessNames(businessName , organization, userName);
					
					resultSuccess.setData(businessNames);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("delete-vehicle-details".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.deleteVehicleDetails(id);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(deleteSuccessMessage);
				} else if("get-deposited-amounts".equals(action)){
					List<DayBookAllowancesResult> amountsList = dayBookDao.getDepositedAmounts(userName, organization);
					
					resultSuccess.setData(amountsList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-previous-day-book-id".equals(action)) {
		        	String dayBookNumber=request.getParameter("dayBookNumber");
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
		        	Integer result = dayBookDao.getPreviousDayBookNumber(dayBookId, dayBookNumber, organization,userName);
		        	
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("update-amounts".equals(action)){
					String amountToBank = request.getParameter("bankAmount");
					String remarks = request.getParameter("remarks");
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.updateDepositedAmounts(id,amountToBank,remarks);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				} else if("delete-amount-details".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.deleteDepositedAmounts(id);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(deleteSuccessMessage);
				} else if("check-vehicle-details-availability".equals(action)){
					Boolean isAvailable = dayBookDao.checkVehicleDetailsExistance(organization, userName);
					
					resultSuccess.setData(isAvailable);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-existed-vehicle-fuel-expenses".equals(action)){
					List<DayBookAllowancesResult> vehicleFuelExpenses = dayBookDao.getVehicleFuelExpenses(organization, userName);
					
					resultSuccess.setData(vehicleFuelExpenses);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("delete-vehicle-fuel-expenses".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.deleteCashDayBook(id);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(deleteSuccessMessage);
				} else if("check-available-deposit-amount".equals(action)){
					Float amount = dayBookDao.getPresentPayment(userName, organization);
					
					resultSuccess.setData(amount);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("update-offloading-charges".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					String allowancesAmount = request.getParameter("allowancesAmount");
					String businessName = request.getParameter("businessName");
					dayBookDao.updateCashDayBook(id, allowancesAmount, businessName);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				} else if("delete-offloading-charges".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.deleteCashDayBook(id);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(deleteSuccessMessage);
				} else if("get-expenses-details".equals(action)){
					String dayBookType = request.getParameter("dayBookType");
					List<DayBookAllowancesResult>existedExpenses = dayBookDao.getExecutiveAllowances(userName, organization, dayBookType);
					
					resultSuccess.setData(existedExpenses);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("update-expenses-data".equals(action)){
					String allowancesAmount = request.getParameter("allowancesAmount");
					String remarks = request.getParameter("remarks");
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.updateCashDayBook(id, allowancesAmount, remarks);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				} else if("delete-expenses-data".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					dayBookDao.deleteCashDayBook(id);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(deleteSuccessMessage);
				} else if ("edit-allowance".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					String valueOne = request.getParameter("valueOne");
					String valueThree = request.getParameter("valueThree");
					dayBookDao.editAllowance(id, valueOne, valueThree);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				} else if ("is-offloading-charge-available".equals(action)) {
					String businessName = request.getParameter("businessName");
					Boolean isAvailable = dayBookDao.checkForOffloadingCharges(businessName, userName, organization);
					
					resultSuccess.setData(isAvailable);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-dayBook-data".equals(action)) {
					DayBookResult result = dayBookDao.getDayBookData(userName, organization);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("remove-day-book-session-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					removeDayBookFromSession(session);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			} else if(form instanceof DayBookCommand){
				dayBookCommand = (DayBookCommand) form;
				String action=dayBookCommand.getAction();
				if ("search-day-book".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("salesExecutive", new SearchFilterData("salesExecutive", dayBookCommand.getSalesExecutive(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn", dayBookCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					List<DayBookResult> dayBook = dayBookDao.getDayBook(dayBookCommand, organization, userName);
					if (dayBook.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(dayBook);
						resultSuccess.setMessage("Search Successfull");
					}
				}
				else if("get-day-book-for-view".equals(action)){
					List<DayBookViewResult> dayBookReturnList = dayBookDao.getDayBookOnId(dayBookCommand.getId(), organization,userName);
					resultSuccess.setData(dayBookReturnList);
					resultSuccess.setMessage("Successfully fetched data");
				}
			} else if(form instanceof DayBookAllowancesCommand) {
				DayBookAllowancesCommand dayBookAllowancesCommand = (DayBookAllowancesCommand) form;
				session.setAttribute("daybook-allowances", dayBookAllowancesCommand);
				
				// Firing alert, if Total Allowances greater than Advance.
				Float advance = dayBookDao.getAdvance(userName, organization);
				if(dayBookAllowancesCommand.getTotalAllowances() > advance) {
					AlertManager.getInstance().fireSystemAlert(organization, userName, userName, 
							OrganizationUtils.ALERT_TYPE_ALLOWANCE_OVER_LIMIT, userName.concat(Msg.get(MsgEnum.ALERT_TYPE_ALLOWANCE_OVER_LIMIT_MESSAGE)));
					
					_logger.info("Alert have been fired for allowances over limit.");
				}
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(persistSuccessMessage);
			} else if(form instanceof DayBookAmountCommand) {
				DayBookAmountCommand dayBookAmountCommand = (DayBookAmountCommand) form;
				float openingBalance = dayBookDao.getOpeningBalance(userName, organization);
				dayBookAmountCommand.setOpeningBalance(openingBalance);
				session.setAttribute("daybook-amount", dayBookAmountCommand);
				
				// For User Defined Alert - Excess Amount.
				String alertType = OrganizationUtils.ALERT_TYPE_EXCESS_AMOUNT;
				Float configuredExcessAmount = dayBookDao.getExcessAmount(alertType, organization);
				if (configuredExcessAmount != null && configuredExcessAmount < dayBookAmountCommand.getClosingBalance()) {
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, organization.getSuperUserName(), alertType,
							null, null, Msg.get(MsgEnum.ALERT_TYPE_EXCESS_AMOUNT_MESSAGE));
					
					_logger.info("Firing alert for alert type: {}", alertType);
				}
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(persistSuccessMessage);
			} else if(form instanceof DayBookProductsCommand) {
				DayBookProductsCommand dayBookProductsCommand = (DayBookProductsCommand) form;
				session.setAttribute("daybook-products", dayBookProductsCommand);
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(persistSuccessMessage);
			} else if(form instanceof DayBookVehicleDetailsCommand){
				DayBookVehicleDetailsCommand dayBookVehicleDetailsCommand = (DayBookVehicleDetailsCommand) form;
				session.setAttribute("vehicle-details", dayBookVehicleDetailsCommand);
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(persistSuccessMessage);
			} 
			else if(form instanceof TempDayBookCommand) {
				TempDayBookCommand tempDayBookCommand = (TempDayBookCommand) form;
				String action = tempDayBookCommand.getAction();
				if("temp-day-book-save".equals(action)){
					dayBookDao.saveTempDayBook(tempDayBookCommand, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(persistSuccessMessage);
				} else if("update-vehicle-fuel-expenses".equals(action)){
					Integer id =Integer.parseInt(request.getParameter("id"));
					dayBookDao.updateCashDayBook(id,tempDayBookCommand.getAllowancesAmount(),tempDayBookCommand.getMeterReading());
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				} else if("update-vehicle-details".equals(action)){
					Integer id =Integer.parseInt(request.getParameter("id"));
					dayBookDao.updateVehicleDetails(id,tempDayBookCommand.getVehicleNo(),tempDayBookCommand.getDriverName()
							,tempDayBookCommand.getStartingReading());
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(updateSuccessMessage);
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch(ProcessingException exception) {
			ResultError resultError =  getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		} catch(DataAccessException exception) {
			ResultError resultError =  getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		} catch (ParseException exception) {
			ResultError resultError =  getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
		
		
	}

	/**
	 * Removing data from session.
	 * 
	 */
	private void removeDayBookFromSession(HttpSession session) {
		session.removeAttribute("daybook-amount");
		session.removeAttribute("daybook-allowances");
		session.removeAttribute("daybook-product-data");
		session.removeAttribute("vehicle-details");
	}
}
