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
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IResult process(Object form) throws Exception {
		SalesReturnCommand salesReturnCommand = null;
		SalesBookDao salesBookDao = (SalesBookDao) getDao();
		SalesCommand salesCommand = null;
		AllotStockCommand allotStockCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		if (form instanceof SalesReturnCommand) {
			salesReturnCommand = (SalesReturnCommand) form;
			if ("search-sales-return-data-onload".equals(salesReturnCommand.getAction())) {
				List<SalesReturnResult> list = salesBookDao.getSalesReturns(getOrganization());
				if(list.isEmpty()){
					resultSuccess.setMessage("No Results Found");
				}else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			} else if ("search-sales-return".equals(salesReturnCommand.getAction())) {
				HashMap hashMap = new HashMap();
				hashMap.put("createdBy", new SearchFilterData("createdBy", salesReturnCommand.getCreatedBy(), SearchFilterData.TYPE_STRING_STR));
				hashMap.put("businessName", new SearchFilterData("businessName", salesReturnCommand.getBusinessName(), SearchFilterData.TYPE_STRING_STR));
				hashMap.put("invoiceName", new SearchFilterData("invoiceName", salesReturnCommand.getInvoiceName(), SearchFilterData.TYPE_STRING_STR));
				hashMap.put("createdOn", new SearchFilterData("createdOn", salesReturnCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
				List<SalesReturnResult> list = salesBookDao.getSalesReturnsCriteria(salesReturnCommand, getOrganization());
				if(list.isEmpty()){
					resultSuccess.setMessage("No Results Found");
				}else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			}
		} else if (form instanceof SalesCommand) {
				salesCommand = (SalesCommand) form;
				if ("search-sales-data-onload".equals(salesCommand.getAction())) {
					List<SalesResult> list = salesBookDao.getSales(getOrganization());
					if(list.isEmpty()){
						resultSuccess.setMessage("Records not found");
					}else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
				} else if("search-sales".equals(salesCommand.getAction())) {
					HashMap hashMap = new HashMap();
					hashMap.put("salesExecutive", new SearchFilterData("salesExecutive", salesCommand.getSalesExecutive(),SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn",salesCommand.getCreatedOn(),SearchFilterData.TYPE_DATE_STR));
					List<SalesResult> list = salesBookDao.getSalesOnCriteria(salesCommand, getOrganization());
					if(list.isEmpty()){
						resultSuccess.setMessage("Records not found");
					}else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
			}
		} else if(form instanceof AllotStockCommand) {
			allotStockCommand = (AllotStockCommand) form;
			String action = allotStockCommand.getAction();
			if("search-sales-data".equals(action)) {
				String salesExecutive = request.getParameter("salesExecutiveNameVal");
				List<AllotStockResult> list = salesBookDao.getSalesExecutives(getOrganization(), salesExecutive);
				if (list == null) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if("get-closing-balance".equals(action)) {
				String salesExecutive = request.getParameter("salesExecutiveVal");
				Float closingBalance = salesBookDao.getClosingBalance(salesExecutive, getOrganization());
				if (closingBalance == null) {
					resultSuccess.setMessage("No Records Found");
					resultSuccess.setData(0.00);
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(closingBalance);
				}
			} else if("get-product-details".equals(action)) {
				List<AllotStockResult> list = salesBookDao.getProductDetails(getOrganization());
				if(list.isEmpty()){
					resultSuccess.setMessage("Records not found");
				}else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(list);
				}
			} else if("update-stock".equals(action)) {
				String listOfObjects = request.getParameter("listOfObjs");
				String salesExecutive = request.getParameter("salesExecutive");
				String previousClosingBal = request.getParameter("previousClosingBal");
				String advance = request.getParameter("advance");
				String openingBal = request.getParameter("openingBal");
				String allotmentType = request.getParameter("allotmentType");
				String rowSteps[] = listOfObjects.split(",");
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
					String batchNumber = rowData[4];
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
					if(!StringUtils.isEmpty(remarks)) {
						allotStockCommand.setRemarks(remarks);
					}
					allotStockList.add(allotStockCommand);
					}
				}
				salesBookDao.saveOrUpdateAllotStock(allotStockList,getUsername(),getOrganization());
				resultSuccess.setMessage("Stock Allotted Successfully");
				
				// For Alerts
				AlertManager.getInstance().fireSystemAlert(getOrganization(), getUsername(), salesExecutive, OrganizationUtils.ALERT_TYPE_STOCK_ALLOTMENT, 
						Msg.get(MsgEnum.ALERT_TYPE_STOCK_ALLOTMENT_MESSAGE));
				_logger.info("Firing alert for allotment.");
			} else if ("show-hide-buttons".equals(action)) {
				String salesExecutive =request.getParameter("salesExecutive");
				List<AllotStockResult> list = salesBookDao.checkSalesExecutive(salesExecutive, getOrganization());
				if (list == null){
					resultSuccess.setMessage("Records not found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Fetched Record Successfully");
				}
			} else if("get-grid-by-date".equals(action)) {
				String salesExecutive =request.getParameter("salesExecName");
				String createdOn = request.getParameter("selectedDate");
				List<AllotStockResult> resultList = salesBookDao.getGridByDate(salesExecutive, createdOn, getOrganization());
				if (resultList == null){
					resultSuccess.setMessage("Records not found");
				} else {
					resultSuccess.setData(resultList);
					resultSuccess.setMessage("Fetched Record Successfully");
				}
			} else if ("is-sales-executive-available".equals(action)) {
				String salesExecutive = request.getParameter("salesExecutive");
				Boolean isAvailable = salesBookDao.isSalesExecutiveAvailable(salesExecutive, getOrganization());
				resultSuccess.setData(isAvailable);
				resultSuccess.setMessage("Fetched Record Successfully");
			} else if("get-previous-opening-balance".equals(action)) {
				String salesExecutive = request.getParameter("salesExecutive");
				Float previousOpeningBal = salesBookDao.getPreviousOpeningBalance(salesExecutive, getOrganization());
				if(previousOpeningBal == null) {
					resultSuccess.setData(new Float(0));
					resultSuccess.setMessage("No Previous Opening Balance");
				} else {
					resultSuccess.setData(previousOpeningBal);
					resultSuccess.setMessage("Fetched Records Successfully");
				}
			} else if ("edit-allot-stock".equals(action)) {
				salesBookDao.editAllotStock(allotStockCommand, getUsername(), getOrganization());
				resultSuccess.setMessage("Updated Successfully");
			}
		}
		 if(_logger.isDebugEnabled()){
			 _logger.debug("resultSuccess: {}", resultSuccess);
		 }
		return resultSuccess;
	}
}
