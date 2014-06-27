package com.vekomy.vbooks.product.action;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.product.command.ProductCommand;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.product.dao.ProductDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * This action class is responsible for performing product add and search operation.
 * 
 * @author ankit
 *
 */
public class ProductAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProductAction.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) throws Exception {

		ProductCommand productCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		if (form instanceof ProductCommand) {
			productCommand = (ProductCommand) form;
			ProductDao productDao = (ProductDao) getDao();
			if ("save-product".equals(productCommand.getAction())) {
				productDao.saveProduct(productCommand, getUsername(), getOrganization());
				resultSuccess.setMessage("Saved Successfully");
			} else if ("search-product".equals(productCommand.getAction())) {
				HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
				filter.put("productName", new SearchFilterData("productName", productCommand.getProductName(), SearchFilterData.TYPE_STRING_STR));
				filter.put("batchNumber", new SearchFilterData("batchNumber", productCommand.getBatchNumber(), SearchFilterData.TYPE_STRING_STR));
				List<ProductResult> list = productDao.searchProduct(productCommand, getOrganization());
			if(list == null){
				resultSuccess.setMessage("No records found");
			} else {
				resultSuccess.setData(list);
				resultSuccess.setMessage("Search Successfully");
			}
			}else if ("validate-productName".equals(productCommand.getAction())) {
				resultSuccess.setData((String) productDao.validateProductName(productCommand, getOrganization()));
				resultSuccess.setMessage("Product Name against Batch Number Validated Successfully.");
			} else if ("edit-product".equals(productCommand.getAction())) {
				productDao.editProduct(productCommand, getUsername(), getOrganization());
				resultSuccess.setMessage("Updated Successfully");
			} else if ("delete-product".equals(productCommand.getAction())) {
				productDao.deleteProduct(productCommand , getOrganization());
				resultSuccess.setMessage("Deleted Successfully");
			} else if ("add-arrived-quantity".equals(productCommand.getAction())) {
				productDao.addArrivedQuantity(productCommand,getUsername(),getOrganization());
				resultSuccess.setMessage("Arrived Quantity Added Successfully");
			}  else if ("get-records-count".equals(productCommand.getAction())) {
				Integer recordsCount=productDao.getRecordsCount(getOrganization());
				if(recordsCount > 0){
					resultSuccess.setData(recordsCount);
					resultSuccess.setMessage("Total Number of Records are:"+recordsCount);
				}else{
					resultSuccess.setMessage("No Records Found");
				}
			} else if ("show-product-history".equals(productCommand.getAction())) {
				List<VbProductInventoryTransaction> productTransactionList=productDao.getProductTransactionList(productCommand , getOrganization());
				if(productTransactionList == null){
					resultSuccess.setMessage("No records found");
				}else{
					resultSuccess.setMessage("Product Transaction Fetched Successfully");
					resultSuccess.setData(productTransactionList);
				}
			} 
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;

	}
}