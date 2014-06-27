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
import com.vekomy.vbooks.mysales.command.DayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.DayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.DayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.DayBookCommand;
import com.vekomy.vbooks.mysales.command.DayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.DayBookResult;
import com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.dao.DayBookDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IResult process(Object form) throws Exception {
		DayBookCommand dayBookCommand = null;
		DayBookDao dayBookDao = (DayBookDao) getDao();
		ResultSuccess resultSuccess = new ResultSuccess();
		session = request.getSession(Boolean.TRUE);
		String userName = getUsername();
		VbOrganization organization = getOrganization();
		if(form instanceof DayBookBasicInfoCommand) {
			DayBookBasicInfoCommand dayBookBasicInfoCommand = (DayBookBasicInfoCommand)form;
			String action = dayBookBasicInfoCommand.getAction();
			session.setAttribute("daybook-basic-info", dayBookBasicInfoCommand);
			if ("get-sales-executive-name".equals(action)) {
				String salesExecutiveName = userName;
				resultSuccess.setData(salesExecutiveName);
			} else if ("get-all-sales-executive-names".equals(action)) {
				String executiveName = request.getParameter("salesExecutive");
				List <String> salesExecutiveList = dayBookDao.getAllSalesExecutives(organization, executiveName);
				resultSuccess.setData(salesExecutiveList);
				resultSuccess.setMessage("Employee Names Retrieved Successfully");
			} else if ("get-customer-total-payable".equals(action)) {
				DayBookResult dayBookResult = dayBookDao.getCustomerTotalPayable(userName , organization);
				if (dayBookResult == null) {
					if (_logger.isErrorEnabled()) {
						_logger.error("No Data Found.");
					}
					resultSuccess.setMessage("No Data Found.");
				} else {
					resultSuccess.setData(dayBookResult);
					resultSuccess.setMessage("Retrieved Data successfully");
				}
			} else if ("get-opening-balance".equals(action)) {
				Float openingBalance = dayBookDao.getOpeningBalance(userName, organization);
				if (openingBalance == null) {
					if (_logger.isErrorEnabled()) {
						_logger.error("No Data Found.");
					}
					resultSuccess.setMessage("No Data Found.");
					resultSuccess.setData(0.00);
				} else {
					resultSuccess.setData(openingBalance);
					resultSuccess.setMessage("Retrieved Data successfully");
				}
			} else if ("get-grid-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				List<DayBookResult> dayBookResultList = dayBookDao.getGridData(userName , organization);
				resultSuccess.setMessage("Fetched Records Successfully");
				resultSuccess.setData(dayBookResultList);
			} else if ("save-day-book-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String listOfObjects = request.getParameter("listOfProductObjects");
				String rowSteps[] = listOfObjects.split(",");
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
						dayBookList.add(dayBookProduct);
						session.setAttribute("daybook-product-data", dayBookList);
						resultSuccess.setMessage("Saved Successfully");
					}
				}
			} else if ("save-daybook".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				dayBookDao.saveDayBook((DayBookBasicInfoCommand)session.getAttribute("daybook-basic-info"), 
						(DayBookAllowancesCommand)session.getAttribute("daybook-allowances"),(DayBookAmountCommand) session.getAttribute("daybook-amount"),
						(List<DayBookProductsCommand>) session.getAttribute("daybook-product-data"),(DayBookVehicleDetailsCommand)session.getAttribute("vehicle-details"),
						organization, userName, request.getParameter("isReturn"));
				
				// Firing alert, for DayBook closure.
				String toRecipient = dayBookDao.getcreatedBy(userName, organization);
				AlertManager.getInstance().fireSystemAlert(getOrganization(), getUsername(), toRecipient, 
						OrganizationUtils.ALERT_TYPE_DAY_BOOK_CLOSURE, getUsername().concat(Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CLOSURE_MESSAGE)));
				
				// Removing data from session.
				removeDayBookFromSession(session);
				resultSuccess.setMessage("Saved successfully");
			} else if ("save-cancel".equals(action)) {
				removeDayBookFromSession(request.getSession(Boolean.TRUE));
				resultSuccess.setMessage("Save canceled Succesfull");
			}else if ("lookup-allotment-type".equals(action)) {
				Boolean flag = dayBookDao.getAllotmentType(userName, organization);
				resultSuccess.setData(flag);
				resultSuccess.setMessage("Successfully getting allotment type");
			} else if("check-day-book".equals(action)) {
				String salesExecutive = request.getParameter("salesExecutive");
				Boolean isDayBookClosed = dayBookDao.isDayBookClosed(salesExecutive, organization);
				resultSuccess.setData(isDayBookClosed);
				resultSuccess.setMessage("Successfully fetched the records");
			} else if("get-allot-stock-opening-balance".equals(action)) {
				Long advance = dayBookDao.getAllotStockOpeningBalance(userName, organization);
				resultSuccess.setData(advance);
				resultSuccess.setMessage("Successfully fetched the records");
			}
		} else if(form instanceof DayBookCommand){
			dayBookCommand = (DayBookCommand) form;
			String action=dayBookCommand.getAction();
			if ("search-day-book".equals(action)) {
				HashMap hashMap = new HashMap();
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
		} else if(form instanceof DayBookAllowancesCommand) {
			DayBookAllowancesCommand dayBookAllowancesCommand = (DayBookAllowancesCommand) form;
			session.setAttribute("daybook-allowances", dayBookAllowancesCommand);
			
			// Firing alert, if Total Allowances greater than Advance.
			String salesExecutive = getUsername();
			String toRecipient = dayBookDao.getcreatedBy(salesExecutive, organization);
			Float advance = dayBookDao.getAdvance(salesExecutive, organization);
			if(dayBookAllowancesCommand.getTotalAllowances() > advance) {
				AlertManager.getInstance().fireSystemAlert(organization, userName, toRecipient, 
						OrganizationUtils.ALERT_TYPE_ALLOWANCE_OVER_LIMIT, salesExecutive.concat(Msg.get(MsgEnum.ALERT_TYPE_ALLOWANCE_OVER_LIMIT_MESSAGE)));
			}
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
		} else if(form instanceof DayBookProductsCommand) {
			DayBookProductsCommand dayBookProductsCommand = (DayBookProductsCommand) form;
			session.setAttribute("daybook-products", dayBookProductsCommand);
		} else if(form instanceof DayBookVehicleDetailsCommand){
			DayBookVehicleDetailsCommand dayBookVehicleDetailsCommand = (DayBookVehicleDetailsCommand) form;
			session.setAttribute("vehicle-details", dayBookVehicleDetailsCommand);
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
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
