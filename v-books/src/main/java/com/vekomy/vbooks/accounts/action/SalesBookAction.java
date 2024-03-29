package com.vekomy.vbooks.accounts.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.accounts.command.AllotStockCommand;
import com.vekomy.vbooks.accounts.command.AllotStockResult;
import com.vekomy.vbooks.accounts.command.SalesCommand;
import com.vekomy.vbooks.accounts.command.SalesResult;
import com.vekomy.vbooks.accounts.command.SalesReturnCommand;
import com.vekomy.vbooks.accounts.command.SalesReturnResult;
import com.vekomy.vbooks.accounts.dao.SalesBookDao;
import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to perform accounts module sales book
 * related operations.
 * 
 * @author vinay
 */
public class SalesBookAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SalesBookAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */

	@Override
	public IResult process(Object form) {
		try {
			SalesReturnCommand salesReturnCommand = null;
			SalesBookDao salesBookDao = (SalesBookDao) getDao();
			SalesCommand salesCommand = null;
			AllotStockCommand allotStockCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			String action = null;
			if (form instanceof SalesReturnCommand) {
				salesReturnCommand = (SalesReturnCommand) form;
				action = salesReturnCommand.getAction();
				if ("search-sales-return-data-onload".equals(action)) {
					List<SalesReturnResult> list = salesBookDao.getSalesReturns(organization);
					
					resultSuccess.setData(list);
				} else if ("search-sales-return".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("createdBy", new SearchFilterData("createdBy", salesReturnCommand.getCreatedBy(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("businessName", new SearchFilterData("businessName", salesReturnCommand.getBusinessName(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("invoiceName", new SearchFilterData("invoiceName", salesReturnCommand.getInvoiceName(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn", salesReturnCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					List<SalesReturnResult> list = salesBookDao.getSalesReturnsCriteria(salesReturnCommand, organization);
					
					resultSuccess.setData(list);
				}
				resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				resultSuccess.setStatus(resultSuccessStatus);
			} else if (form instanceof SalesCommand) {
				salesCommand = (SalesCommand) form;
				action = salesCommand.getAction();
				if ("search-sales-data-onload".equals(action)) {
					List<SalesResult> list = salesBookDao.getSales(organization);
					
					resultSuccess.setData(list);
				} else if ("search-sales".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("salesExecutive", new SearchFilterData("salesExecutive", salesCommand.getSalesExecutive(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn", salesCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					List<SalesResult> list = salesBookDao.getSalesOnCriteria(salesCommand, organization);
					
					resultSuccess.setData(list);
				}
				resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				resultSuccess.setStatus(resultSuccessStatus);
			} else if(form instanceof AllotStockCommand) {
				allotStockCommand = (AllotStockCommand) form;
				action = allotStockCommand.getAction();
				if("search-sales-data".equals(action)) {
					String salesExecutive = request.getParameter("salesExecutiveNameVal");
					List<AllotStockResult> list = salesBookDao.getSalesExecutives(organization, salesExecutive);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-closing-balance".equals(action)) {
					String salesExecutive = request.getParameter("salesExecutiveVal");
					Float closingBalance = salesBookDao.getClosingBalance(salesExecutive, organization);
					
					resultSuccess.setData(closingBalance);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("get-product-details".equals(action)) {
					List<AllotStockResult> list = salesBookDao.getProductDetails(organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if("update-stock".equals(action)) {
					String listOfObjects = request.getParameter("listOfObjs");
					String salesExecutive = request.getParameter("salesExecutive");
					String previousClosingBal = request.getParameter("previousClosingBal");
					String advance = request.getParameter("advance");
					String salesBookNo = salesBookDao.generateSalesBookNo(organization);
					String openingBal = request.getParameter("openingBal");
					String allotmentType = request.getParameter("allotmentType");
					String rowSteps[] = listOfObjects.split("\\?");
					List<AllotStockCommand> allotStockList = new ArrayList<AllotStockCommand>();
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
						allotStockCommand = new AllotStockCommand();
						allotStockCommand.setSalesExecutive(salesExecutive);
						allotStockCommand.setClosingBalance(Float.parseFloat(previousClosingBal));
						allotStockCommand.setAdvance(Float.parseFloat(advance));
						allotStockCommand.setOpeningBalance(Float.parseFloat(openingBal));
						allotStockCommand.setAllotmentType(allotmentType);
						String rowData[] = rowSteps[rowStep].split("\\|");
						String productName = rowData[0].trim();
						String remarks = rowData[1].trim();
						String qtyClosingBalance = rowData[2].trim();
						String qtyAllotted = rowData[3].trim();
						String qtyOpeningBalance = rowData[4].trim();
						String batchNumber = rowData[5];
						if(!StringUtils.isEmpty(productName)) {
							allotStockCommand.setProductName(productName);
						}
						if(!StringUtils.isEmpty(batchNumber)) {
							allotStockCommand.setBatchNumber(batchNumber);
						}
						if(!StringUtils.isEmpty(qtyClosingBalance)) {
							allotStockCommand.setQtyClosingBalance(Integer.parseInt(qtyClosingBalance));
						}
						if(!StringUtils.isEmpty(qtyAllotted)) {
							allotStockCommand.setQtyAllotted(Integer.parseInt(qtyAllotted));
						}
						if(!StringUtils.isEmpty(qtyOpeningBalance)) {
							allotStockCommand.setQtyOpeningBalance(Integer.parseInt(qtyOpeningBalance));
						}
						if(!StringUtils.isEmpty(remarks)) {
							allotStockCommand.setRemarks(remarks);
						}
						allotStockList.add(allotStockCommand);
						}
					}
					salesBookDao.saveOrUpdateAllotStock(allotStockList, userName, organization, salesBookNo);
					
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
					
					// For Alerts
					AlertManager.getInstance().fireSystemAlert(organization, userName, salesExecutive, OrganizationUtils.ALERT_TYPE_STOCK_ALLOTMENT, 
							Msg.get(MsgEnum.ALERT_TYPE_STOCK_ALLOTMENT_MESSAGE));
					_logger.info("Firing alert for allotment.");
				} else if ("show-hide-buttons".equals(action)) {
					String salesExecutive =request.getParameter("salesExecutive");
					List<AllotStockResult> list = salesBookDao.checkSalesExecutive(salesExecutive, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("is-sales-executive-available".equals(action)) {
					String salesExecutive = request.getParameter("salesExecutive");
					Boolean isAvailable = salesBookDao.isSalesExecutiveAvailable(salesExecutive, organization);
					
					resultSuccess.setData(isAvailable);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("edit-allot-stock".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					Integer qtyAllotted = Integer.parseInt(request.getParameter("qtyAllotted"));
					Integer qtyOpeningBalance = Integer.parseInt(request.getParameter("qtyOpeningBalance"));
					String remarks = request.getParameter("remarks");
					salesBookDao.editAllotStock(id, qtyAllotted, qtyOpeningBalance, remarks, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("get-existing-allotment".equals(action)) {
					String productName = request.getParameter("productName");
					String batchNumber = request.getParameter("batchNumber");
					String salesExecutive = request.getParameter("salesExecutive");
					Integer existedQtyAllotted = salesBookDao.getExistingAllotment(productName, batchNumber, salesExecutive, organization);
					
					resultSuccess.setData(existedQtyAllotted);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-recent-allotment".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("id"));
					Integer recentAllotment = salesBookDao.getRecentAllotmentById(id);
					
					resultSuccess.setData(recentAllotment);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch(DataAccessException exception) {
			ResultError resultError =  getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		} 
	}
}
