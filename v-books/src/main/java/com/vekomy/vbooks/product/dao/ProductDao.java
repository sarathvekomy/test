package com.vekomy.vbooks.product.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.product.command.ProductCommand;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible for performing product save and search
 * operation.
 * 
 * @author ankit
 * 
 */
public class ProductDao extends BaseDao {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProductDao.class);
	/**
	 * Decimal format for two decimal points for a float values.
	 */
	public static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
	
	/**
	 * Static variable for size of the page
	 */
	private static int pageSize = 100;

	/**
	 * This method is responsible for saving {@link vbOrganization},username and product details in to db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return isSaved - {@link Boolean}
	 */
	public boolean saveProduct(ProductCommand productCommand, String userName,
			VbOrganization vbOrganization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("productCommand: {}", productCommand);
			_logger.debug("vbOrganization: {}", vbOrganization);
		}
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		boolean isSaved = false;
		VbProduct instance = new VbProduct();
		if (instance != null) {
			instance.setProductCategory(productCommand.getProductCategory());
			instance.setBrand(productCommand.getBrand());
			instance.setModel(productCommand.getModel());
			instance.setProductName(productCommand.getProductName());
			instance.setBatchNumber(productCommand.getBatchNumber());
			instance.setDescription(productCommand.getDescription());
			instance.setCostPerQuantity(productCommand.getCostPerQuantity());
			instance.setAvailableQuantity(0);
			instance.setQuantityArrived(0);
			instance.setQuantityAtWarehouse(0);
			instance.setTotalQuantity(0);
			instance.setVbOrganization(vbOrganization);
			instance.setCreatedBy(userName);
			Date createdDate = new Date();
			instance.setCreatedOn(createdDate);
			instance.setModifiedOn(createdDate);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbProduct: {}", instance);
			}
			session.save(instance);
			isSaved = true;
		}
		transaction.commit();
		session.close();
		return isSaved;
	}
	
	/**
	 * This Method is Responsible to Check whether The Entered Product Name is
	 * Duplicate Or Not
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @return isAvailable - {@link String}
	 */
	@SuppressWarnings({"unchecked" })
	public String validateProductName(ProductCommand productCommand, VbOrganization organization) {
		String isAvailable = "y";
		Session session = this.getSession();
		List<VbProduct> productList = session.createCriteria(VbProduct.class)
				.add(Expression.eq("productName", productCommand.getProductName()))
				.add(Expression.eq("batchNumber", productCommand.getBatchNumber()))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		if(!productList.isEmpty()){
			isAvailable = "n";
		}
		session.close();
		return isAvailable;
	}

	/**
	 * This method is responsible for update product details in to db.
	 * 
	 * @param productCommand -  {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * 
	 */
	public void editProduct(ProductCommand productCommand, String userName,
			VbOrganization vbOrganization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("productCommand: {}", productCommand);
			_logger.debug("vbOrganization: {}", vbOrganization);
		}
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		VbProduct vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("id", productCommand.getId()))
				.add(Expression.eq("vbOrganization", vbOrganization))
				.uniqueResult();
		if (vbProduct != null) {
			vbProduct.setProductCategory(productCommand.getProductCategory());
			vbProduct.setBrand(productCommand.getBrand());
			vbProduct.setModel(productCommand.getModel());
			vbProduct.setProductName(productCommand.getProductName());
			vbProduct.setBatchNumber(productCommand.getBatchNumber());
			vbProduct.setDescription(productCommand.getDescription());
			vbProduct.setCostPerQuantity(productCommand.getCostPerQuantity());
			vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse());
			vbProduct.setQuantityArrived(vbProduct.getQuantityArrived());
			vbProduct.setAvailableQuantity(vbProduct.getAvailableQuantity());
			vbProduct.setTotalQuantity(vbProduct.getTotalQuantity());
			vbProduct.setVbOrganization(vbOrganization);
			vbProduct.setCreatedBy(userName);
			Date createdDate = new Date();
			vbProduct.setCreatedOn(createdDate);
			vbProduct.setModifiedOn(createdDate);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating VbProduct: {}", vbProduct);
			}
			session.saveOrUpdate(vbProduct);
		} else {
			if (_logger.isErrorEnabled()) {
				_logger.error("Product Not Found to updated.");
			}
		}
		transaction.commit();
		session.close();
	}

	/**
	 * This method is responsible for delete product details in to db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * 
	 */
	public void deleteProduct(ProductCommand productCommand , VbOrganization organization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("productCommand: {}", productCommand);
		}
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		VbProduct vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("id", productCommand.getId()))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		
		if(vbProduct != null){
			VbProductCustomerCost vbProductCustomerCost = (VbProductCustomerCost) session.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbProduct", "product")
					.add(Expression.eq("product.id", vbProduct.getId()))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			if(vbProductCustomerCost != null){
				if(_logger.isDebugEnabled()){
					_logger.debug("Deleting vbProductCustomerCost: {}", vbProductCustomerCost);
				}
				session.delete(vbProductCustomerCost);
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("Deleting VbProduct: {}", vbProduct);
			}
			session.delete(vbProduct);
		}
		transaction.commit();
		session.close();
	}

	/**
	 * This method is responsible for search product details from db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return productDetailList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> searchProduct(ProductCommand productCommand,
			VbOrganization vbOrganization) {

		if (_logger.isDebugEnabled()) {
			_logger.debug("productCommand: {}", productCommand);
		}
		Session session = this.getSession();
		Criteria searchCriteria = session.createCriteria(VbProduct.class);
		if (productCommand != null) {
			String productName = productCommand.getProductName();
			String batchNumber = productCommand.getBatchNumber();
			if ((productName != null) && (productName.length() > 0)) {
				searchCriteria.add(Expression.like("productName", productName, MatchMode.START).ignoreCase());
			}
			if ((batchNumber != null) && (batchNumber.length() > 0)) {
				searchCriteria.add(Expression.like("batchNumber", batchNumber, MatchMode.START).ignoreCase());
			}
		}
		if (vbOrganization != null) {
			searchCriteria.add(Expression.eq("vbOrganization", vbOrganization));
		}
		searchCriteria.addOrder(Order.asc("productName"));
		List<VbProduct> productList = searchCriteria.list();
		List<ProductResult> productDetailList = new ArrayList<ProductResult>();
		ProductResult productResult = null;
		for (VbProduct product : productList) {
			productResult = new ProductResult();
			productResult.setId(product.getId());
			productResult.setProductCategory(product.getProductCategory());
			productResult.setProductName(product.getProductName());
			productResult.setBatchNumber(product.getBatchNumber());
			productResult.setDescription(product.getDescription());
			productResult.setCostPerQuantity(StringUtil.currencyFormat(product.getCostPerQuantity()));
			productResult.setDescription(product.getDescription());
			productDetailList.add(productResult);
		}
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("productDetailList: {}", productDetailList);
		}
		return productDetailList;
	}

	/**
	 * This method is responsible to get {@link VbProduct} from database.
	 * 
	 * @param id - {@link Integer}
	 * @return instance - {@link VbProduct}
	 */
	public VbProduct getProduct(int id , VbOrganization organization) {
		Session session = this.getSession();
		VbProduct instance = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("id", id))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbProduct: {}", instance);
		}
		return instance;
	}

	/**
	 * This method is responsible to retrieve all the product list from the DB.
	 * 
	 * @param vbOrganization - {@link VbOrganization}
	 * @return productsList - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbProduct> getProductList(VbOrganization vbOrganization) {
		Session session = this.getSession();
		List<VbProduct> productsList = session.createCriteria(VbProduct.class)
				.add(Expression.eq("vbOrganization", vbOrganization))
				.addOrder(Order.asc("productName"))
				.list();
		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("No. of products available are: {}",  productsList.size());
		}
		return productsList;
	}

	/**
	 * This method is responsible for getting product name based on product id.
	 * 
	 * @param id - {@link Integer}
	 * @return productName - {@link String}
	 */
	public String getProductNameById(int id){
		Session session = this.getSession();
		VbProduct instance = (VbProduct) session.get(VbProduct.class, id);
		String productName = instance.getProductName();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbProduct: {}", instance);
		}
		return productName;
	}
	
	/**
	 * This method is responsible for getting product transaction details from db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @return productInventoryTransaction - {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<VbProductInventoryTransaction> getProductTransactionList(ProductCommand productCommand , VbOrganization organization) {
		Session session = this.getSession();
		List<VbProductInventoryTransaction> productInventoryTransaction=new ArrayList<VbProductInventoryTransaction>();
		Criteria criteria = session.createCriteria(VbProductInventoryTransaction.class);
		criteria.add(Expression.eq("vbOrganization", organization));
		criteria.setFirstResult(pageSize*(productCommand.getPageNumber()-1));
		criteria.setMaxResults(pageSize);		
		criteria.addOrder(Order.desc("createdOn"));
	    productInventoryTransaction=criteria.list();
		session.close();
		if(productInventoryTransaction.isEmpty()){
			if(_logger.isErrorEnabled()){
				_logger.error("ProductInventoryTransactions not found.");
			}
			return null;
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbProductInventoryTransaction: {}", productInventoryTransaction);
		}
		return productInventoryTransaction;
	}

	/**This method is responsible for add all product arrived quantity in db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * 
	 */
	public void addArrivedQuantity(ProductCommand productCommand,String userName, VbOrganization organization) {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		Date date = new Date();
		VbProduct vbProduct=new VbProduct();
		String productName = productCommand.getProductName();
		String batchNumber = productCommand.getBatchNumber();
		vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("productName", productName))
				.add(Expression.eq("batchNumber", batchNumber))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		
		Integer availableQuantity = 0;
		if(vbProduct != null){
			availableQuantity = productCommand.getQuantityArrived() + vbProduct.getAvailableQuantity();
		    vbProduct.setProductName(productName);
		    vbProduct.setQuantityArrived(productCommand.getQuantityArrived());
		    vbProduct.setAvailableQuantity(availableQuantity);
		    vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() + productCommand.getQuantityArrived());
		    session.update(vbProduct);
		    if (_logger.isDebugEnabled()) {
		    	_logger.debug("Updating VbProduct: {}", vbProduct);
			}
		}
		
		VbProductInventoryTransaction inventoryTransaction = new VbProductInventoryTransaction();
		if(inventoryTransaction != null) {
			inventoryTransaction=new VbProductInventoryTransaction();
			inventoryTransaction.setProductName(productCommand.getProductName());
			inventoryTransaction.setBatchNumber(productCommand.getBatchNumber());
			inventoryTransaction.setCreatedBy(userName);
			inventoryTransaction.setCreatedOn(date);
			inventoryTransaction.setModifiedOn(date);
			inventoryTransaction.setInwardsQty(productCommand.getQuantityArrived());
			inventoryTransaction.setVbOrganization(organization);
			inventoryTransaction.setOutwardsQty(0);
				
			if(_logger.isDebugEnabled()){
				_logger.debug("Persisting productInventoryTransaction: {}", inventoryTransaction);
			}
			session.save(inventoryTransaction);
		}
		
		transaction.commit();
		session.close();
	}

	/** This method is responsible for getting count of records from table.
	 *  
	 *  @param organization - {@link VbOrganization}
	 * @return countTransactionRecords - {@link Integer}
	 */
	public Integer getRecordsCount(VbOrganization organization) {
		Session session = this.getSession();
		Integer countTransactionRecords = session.createCriteria(VbProductInventoryTransaction.class)
				.add(Expression.eq("vbOrganization", organization))
				.list().size();
		 if (_logger.isDebugEnabled()) {
				_logger.debug("countTransactionRecords: {}", countTransactionRecords);
			}
		return countTransactionRecords;
	}

}