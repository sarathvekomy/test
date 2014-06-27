package com.vekomy.vbooks.product.action;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.product.command.ProductCustomerCostCommand;
import com.vekomy.vbooks.product.command.ProductCustomerCostResult;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.product.dao.ProductCustomerCostDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

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
	private static final Logger _logger = LoggerFactory
			.getLogger(ProductCustomerCostAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */

	@Override
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		ProductCustomerCostCommand productCustomerCostCommand = null;
		ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) getDao();
		if (form instanceof ProductCustomerCostCommand) {
			productCustomerCostCommand = (ProductCustomerCostCommand) form;
			if ("save-product-customer-cost".equals(productCustomerCostCommand
					.getAction())) {
				productCustomerCostDao.saveProductCustomerCost(
						productCustomerCostCommand, getUsername(),
						getOrganization());
				resultSuccess.setMessage("Saved Successfully");
			} else if ("search-product-customer-cost"
					.equals(productCustomerCostCommand.getAction())) {
				HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
				filter.put("businessName", new SearchFilterData("businessName",
						productCustomerCostCommand.getBusinessName(),
						SearchFilterData.TYPE_STRING_STR));
				filter.put("productName", new SearchFilterData("productNumber",
						productCustomerCostCommand.getProductName(),
						SearchFilterData.TYPE_STRING_STR));
				List<ProductCustomerCostResult> list = productCustomerCostDao
						.searchProductCustomerCost(productCustomerCostCommand,
								getOrganization());
				if (list.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfully");
				}
			} else if ("update-product-customer-cost".equals(productCustomerCostCommand.getAction())) {
				productCustomerCostDao.updateProductCustomerCost(productCustomerCostCommand, getUsername(),getOrganization());
				resultSuccess.setMessage("Updated Successfully");
			} else if ("get-product-list".equals(productCustomerCostCommand
					.getAction())) {
				List<ProductResult> productList = productCustomerCostDao
						.getProductList(getOrganization());
				if (productList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(productList);
				}
			} else if ("get-business-name".equals(productCustomerCostCommand.getAction())) {
				String businessName = request.getParameter("businessNameVal");
				List<VbCustomer> businessNames = productCustomerCostDao.getBusinessName(businessName , getOrganization());
				if (businessNames.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(businessNames);
				}
			}else if ("delete-product-customer-cost"
					.equals(productCustomerCostCommand.getAction())) {
				productCustomerCostDao = (ProductCustomerCostDao) getDao();
				productCustomerCostDao.deleteProductCustomerCost(
						productCustomerCostCommand, getOrganization());
				resultSuccess.setMessage("Deleted Successfully");
			}
			else if ("get-product-cost-by-business-name"
					.equals(productCustomerCostCommand.getAction())) {
				productCustomerCostDao = (ProductCustomerCostDao) getDao();
				List<ProductResult> vbProductCustomerCost = productCustomerCostDao
						.getProductCustomerCostByBusinessName(
								productCustomerCostCommand , getOrganization());
				if (vbProductCustomerCost.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(vbProductCustomerCost);
				}
			}

		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("ResultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}
}
