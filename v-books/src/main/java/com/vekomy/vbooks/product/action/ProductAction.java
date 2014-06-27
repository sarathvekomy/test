package com.vekomy.vbooks.product.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.product.command.ProductCommand;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.product.dao.ProductDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This action class is responsible for performing product add and search
 * operation.
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
	public IResult process(Object form) {
		try {
			VbOrganization organization = getOrganization();
			String userName = getUsername();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			ProductCommand productCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			if (form instanceof ProductCommand) {
				productCommand = (ProductCommand) form;
				String action = productCommand.getAction();
				ProductDao productDao = (ProductDao) getDao();
				if ("save-product".equals(action)) {
					productDao.saveProduct(productCommand, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("search-product".equals(action)) {
					HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
					filter.put("productName", new SearchFilterData("productName", productCommand.getProductName(), SearchFilterData.TYPE_STRING_STR));
					filter.put("batchNumber", new SearchFilterData("batchNumber", productCommand.getBatchNumber(), SearchFilterData.TYPE_STRING_STR));
					List<ProductResult> list = productDao.searchProduct(productCommand, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("validate-productName".equals(action)) {
					String data = productDao.validateProductName(productCommand, organization);
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
				else if ("validate-edit-productName".equals(action)) {
					String data = productDao.validateEditProductName(productCommand, organization);
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}else if ("edit-product".equals(action)) {
					productDao.editProduct(productCommand, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("delete-product".equals(action)) {
					productDao.deleteProduct(productCommand, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if ("modify-product-status".equals(action)) {
					String productStatusParam = request.getParameter("productStatusParam");
					List<ProductResult> productList=productDao.modifyProductStatus(productCommand, productStatusParam, organization);
					resultSuccess.setData(productList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("check-products-in-warehouse".equals(action)) {
					Boolean result = productDao.checkProductWarehouseQuantity(productCommand, organization);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("add-arrived-quantity".equals(action)) {
					String listOfObjects = request.getParameter("listOfProductObjects");
					String rowSteps[] = listOfObjects.split(",");
					List<ProductCommand> arrivedQuantityList = new ArrayList<ProductCommand>();
					ProductCommand arrivedProductCommand = null;
					String[] rowData = null;
					String productName = null;
					String batchNumber = null;
					String arrivedQuantity = null;
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep]	&& rowSteps[rowStep].length() > 0) {
							arrivedProductCommand = new ProductCommand();
							rowData = rowSteps[rowStep].split("\\|");
							productName = rowData[0].trim();
							batchNumber = rowData[1].trim();
							arrivedQuantity = rowData[2].trim();
							if (!StringUtils.isEmpty(arrivedQuantity)) {
								arrivedProductCommand.setQunatityArrived(Integer.parseInt(arrivedQuantity));
							}
							if (!StringUtils.isEmpty(productName)) {
								arrivedProductCommand.setProductName(productName);
							}
							if (!StringUtils.isEmpty(batchNumber)) {
								arrivedProductCommand.setBatchNumber(batchNumber);
							}
							arrivedQuantityList.add(arrivedProductCommand);
						}
					}
					productDao.addArrivedQuantity(arrivedQuantityList, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("add-damaged-quantity".equals(action)) {
					String listOfObjects = request.getParameter("listOfProductObjects");
					String rowSteps[] = listOfObjects.split(",");
					List<ProductCommand> damagedQuantityList = new ArrayList<ProductCommand>();
					ProductCommand damagedProductCommand = null;
					String[] rowData = null;
					String productName = null;
					String batchNumber = null;
					String damagedQuantity = null;
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							damagedProductCommand = new ProductCommand();
							rowData = rowSteps[rowStep].split("\\|");
							productName = rowData[0].trim();
							batchNumber = rowData[1].trim();
							damagedQuantity = rowData[2].trim();
							if (!StringUtils.isEmpty(damagedQuantity)) {
								damagedProductCommand.setDamagedQuantity(Integer.parseInt(damagedQuantity));
							}
							if (!StringUtils.isEmpty(productName)) {
								damagedProductCommand.setProductName(productName);
							}
							if (!StringUtils.isEmpty(batchNumber)) {
								damagedProductCommand.setBatchNumber(batchNumber);
							}
							damagedQuantityList.add(damagedProductCommand);
						}
					}
					productDao.addDamagedQuantity(damagedQuantityList, userName, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if ("get-records-count".equals(action)) {
					Integer recordsCount = productDao.getRecordsCount(organization);
					
					resultSuccess.setData(recordsCount);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-records-count-for-criteria".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("productName", new SearchFilterData("productName", productCommand.getProductName(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("batchNumber", new SearchFilterData("batchNumber", productCommand.getBatchNumber(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("transactionType", new SearchFilterData("transactionType", productCommand.getTransactionType(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn", productCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					Integer recordsCount = productDao.getRecordsCountForCriteria(productCommand, organization);
					
					resultSuccess.setData(recordsCount);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("show-product-history".equals(action)) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("productName", new SearchFilterData("productName", productCommand.getProductName(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("batchNumber", new SearchFilterData("batchNumber", productCommand.getBatchNumber(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("transactionType", new SearchFilterData("transactionType", productCommand.getTransactionType(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn", productCommand.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					List<VbProductInventoryTransaction> productTransactionList = productDao.getProductTransactionList(productCommand, organization);
					
					resultSuccess.setData(productTransactionList);
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