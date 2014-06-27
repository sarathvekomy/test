/**
 * com.vekomy.vbooks.product.action.ProductCustomerCostAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.product.action;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.product.command.ProductCustomerCostCommand;
import com.vekomy.vbooks.product.command.ProductCustomerCostResult;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.product.dao.ProductCustomerCostDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This action class is responsible to process product customer cost request.
 * 
 * @author Ankit
 * 
 */
public class ProductCustomerCostAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProductCustomerCostAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) {
		try {
			VbOrganization organization = getOrganization();
			String userName = getUsername();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			ResultSuccess resultSuccess = new ResultSuccess();
			ProductCustomerCostCommand productCustomerCostCommand = null;
			ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) getDao();
			if (form instanceof ProductCustomerCostCommand) {
				productCustomerCostCommand = (ProductCustomerCostCommand) form;
				String action = productCustomerCostCommand.getAction();
				if ("save-product-customer-cost".equals(action)) {
					productCustomerCostDao.saveProductCustomerCost(productCustomerCostCommand, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("search-product-customer-cost".equals(action)) {
					HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
					filter.put("businessName", new SearchFilterData("businessName", productCustomerCostCommand.getBusinessName(), SearchFilterData.TYPE_STRING_STR));
					filter.put("productName", new SearchFilterData("productNumber", productCustomerCostCommand.getProductName(), SearchFilterData.TYPE_STRING_STR));
					List<ProductCustomerCostResult> list = productCustomerCostDao.searchProductCustomerCost(productCustomerCostCommand, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("update-product-customer-cost".equals(action)) {
					productCustomerCostDao.updateProductCustomerCost(productCustomerCostCommand, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("get-product-list".equals(action)) {
					List<ProductResult> productList = productCustomerCostDao.getProductList(organization);
					
					resultSuccess.setData(productList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-business-name".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<VbCustomer> businessNames = productCustomerCostDao.getBusinessName(businessName, organization);
					
					resultSuccess.setData(businessNames);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("delete-product-customer-cost".equals(action)) {
					productCustomerCostDao.deleteProductCustomerCost(productCustomerCostCommand, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if ("get-product-cost-by-business-name".equals(action)) {
					List<ProductResult> vbProductCustomerCost = productCustomerCostDao.getProductCustomerCostByBusinessName(productCustomerCostCommand, organization);
					
					resultSuccess.setData(vbProductCustomerCost);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
}
