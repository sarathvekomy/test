package com.vekomy.vbooks.product.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.product.command.ProductCustomerCostCommand;
import com.vekomy.vbooks.product.command.ProductCustomerCostResult;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This class is responsible for perform product customer cost request.
 * 
 * @author Ankit
 * 
 * 
 */

public class ProductCustomerCostDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ProductCustomerCostDao.class);

	/**
	 * This method is responsible for save product customer cost details into
	 * db.
	 * 
	 * @param productCustomerCostCommand
	 * @param organization
	 * @param createdBy
	 * @param organization
	 * @return isSaved
	 */
	public Boolean saveProductCustomerCost(ProductCustomerCostCommand productCustomerCostCommand,String createdBy, VbOrganization organization) {
		Boolean isSaved = Boolean.FALSE;
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbCustomer vbCustomer = null;
		VbProduct vbProduct = null;
		if (productCustomerCostCommand != null) {
			String businessName = productCustomerCostCommand.getBusinessName();
			vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
					.add(Expression.eq("businessName", businessName))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();

			if (vbCustomer == null) {
				if (_logger.isErrorEnabled()) {
					_logger.error("No customer available for the Business Name: {}", businessName);
				}
			}
		}
		if (productCustomerCostCommand != null) {
			vbProduct = (VbProduct) session
					.createCriteria(VbProduct.class)
					.add(Expression.eq("productName", productCustomerCostCommand.getProductName()))
					.add(Expression.eq("batchNumber", productCustomerCostCommand.getBatchNumber()))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			if (vbProduct == null) {
				if (_logger.isErrorEnabled()) {
					_logger.error("No Product available for the product Name: {}", vbProduct);
				}
			}
		}
		VbProductCustomerCost instanceProductCustomerCost = null;
		instanceProductCustomerCost = (VbProductCustomerCost) session
				.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbProduct", "product")
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("vbProduct", vbProduct))
				.add(Expression.eq("vbCustomer", vbCustomer))
				.add(Expression.eq("product.vbOrganization", organization))
				.add(Expression.eq("customer.vbOrganization", organization))
				.uniqueResult();
		if (instanceProductCustomerCost != null
				&& instanceProductCustomerCost.getVbCustomer()
						.getBusinessName()
						.equals(productCustomerCostCommand.getBusinessName())) {
			instanceProductCustomerCost.setCreatedBy(createdBy);
			Date date = new Date();
			instanceProductCustomerCost.setCreatedOn(date);
			instanceProductCustomerCost.setModifiedOn(date);
			instanceProductCustomerCost.setVbCustomer(vbCustomer);
			instanceProductCustomerCost.setVbProduct(vbProduct);
			instanceProductCustomerCost.setCost(productCustomerCostCommand.getCost());
			instanceProductCustomerCost.setVbOrganization(organization);
			session.update(instanceProductCustomerCost);
		} else {
			VbProductCustomerCost vbProductCustomerCost = new VbProductCustomerCost();
			vbProductCustomerCost.setCreatedBy(createdBy);
			Date date = new Date();
			vbProductCustomerCost.setCreatedOn(date);
			vbProductCustomerCost.setModifiedOn(date);
			vbProductCustomerCost.setVbCustomer(vbCustomer);
			vbProductCustomerCost.setVbProduct(vbProduct);
			vbProductCustomerCost.setCost(productCustomerCostCommand.getCost());
			vbProductCustomerCost.setVbOrganization(organization);
			session.save(vbProductCustomerCost);
		}

		if (_logger.isDebugEnabled()) {
			_logger.debug("Persisting VbProductCustomerCost: {}",
					instanceProductCustomerCost);
		}
		isSaved = Boolean.TRUE;

		txn.commit();
		session.close();
		return isSaved;
	}

	/**
	 * This method is responsible for search product customer cost details from
	 * database.
	 * 
	 * @param productCommand
	 * @return productDetailList
	 */
	@SuppressWarnings({ "unchecked" })
	public List<ProductCustomerCostResult> searchProductCustomerCost(
			ProductCustomerCostCommand productCustomerCostCommand,
			VbOrganization organization) {
		if (_logger.isDebugEnabled()) {
			_logger.debug("productCustomerCostCommand: {}",
					productCustomerCostCommand);
		}
		Session session = this.getSession();
		String businessName = productCustomerCostCommand.getBusinessName();
		String productName = productCustomerCostCommand.getProductName();
		List<ProductCustomerCostResult> productDetails = new ArrayList<ProductCustomerCostResult>();
		if (productCustomerCostCommand != null) {
            Criteria criteria=session
					.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbProduct", "product")
					.add(Expression.eq("customer.vbOrganization",
							organization));
			if (!businessName.isEmpty()) {
						criteria.add(Expression.like("customer.businessName",
								'%' + businessName + '%'));
			}if (!productName.isEmpty()) {
						criteria.add(Expression.like("product.productName",
								'%' + productName + '%'));
			} 
			productDetails=makeResult(criteria.list());
		}
		List<ProductCustomerCostResult> list = new ArrayList<ProductCustomerCostResult>(
				new HashSet<ProductCustomerCostResult>(productDetails));

		session.close();

		if (_logger.isDebugEnabled()) {
			_logger.debug("productDetails: {}", list);
		}
		
		return list;

	}
	/**
	 * This method is responsible for performing result using list
	 * 
	 * 
	 * @param productCustomerCosts
	 */
	@SuppressWarnings("rawtypes")
	public List makeResult(List productCustomerCosts){
		ProductCustomerCostResult productCustomerCostResult = null;
		List<ProductCustomerCostResult> productDetails = new ArrayList<ProductCustomerCostResult>();
		List<VbProductCustomerCost> listWithoutDuplicates = new ArrayList<VbProductCustomerCost>(
				new HashSet<VbProductCustomerCost>(productCustomerCosts));
		for (int i = 0; i < listWithoutDuplicates.size(); i++) {
			productCustomerCostResult = new ProductCustomerCostResult();
			VbProductCustomerCost customerCost =  listWithoutDuplicates.get(i);
			productCustomerCostResult.setBusinessName(customerCost
					.getVbCustomer().getBusinessName());
			productCustomerCostResult.setId(customerCost
					.getVbCustomer().getId());
			productCustomerCostResult.setCreatedDate(customerCost
					.getCreatedOn());
			productDetails.add(productCustomerCostResult);
		}
		return productDetails;
	}

	/**
	 * This method is responsible for deleting product customer cost from
	 * database.
	 * 
	 * @param productCustomerCostCommand
	 */
	public void deleteProductCustomerCost(ProductCustomerCostCommand productCustomerCostCommand,VbOrganization organization) {
		Session session = this.getSession();
		//VbProductCustomerCost vbProductCustomerCost=(VbProductCustomerCost)session.load(VbProductCustomerCost.class, productCustomerCostCommand.getId());
		Transaction tx = session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<VbProductCustomerCost> vbProductCustomerCostList = (List<VbProductCustomerCost>) session
				.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.id", productCustomerCostCommand.getId()))
				.add(Expression.eq("customer.vbOrganization", organization)).list();
		for (VbProductCustomerCost vbProductCustomerCost  : vbProductCustomerCostList) {
			session.delete(vbProductCustomerCost);
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("Deleting VbProductCustomerCost: {}", vbProductCustomerCostList);
		}
		tx.commit();
		session.close();
	}

	/**
	 * This method is responsible to retrieve all the product from the DB.
	 * 
	 * @return productNameList
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getProductList(VbOrganization organization) {
		List<ProductResult> productResultList = null;
		Session session = this.getSession();
		Criteria searchCriteria = session.createCriteria(VbProduct.class);
		searchCriteria.addOrder(Order.asc("productName"));
		List<VbProduct> productList = searchCriteria.add(Expression.eq("vbOrganization", organization)).list();
		if (!(productList.isEmpty())) {
			ProductResult productResult = null;
			productResultList = new ArrayList<ProductResult>();
			Integer count = 0;
			for (VbProduct vbProduct : productList) {
				productResult = new ProductResult();
				productResult.setId(++count);
				productResult.setProductName(vbProduct.getProductName());
				productResult.setBatchNumber(vbProduct.getBatchNumber());
				productResult.setCostPerQuantity(StringUtil.currencyFormat(vbProduct.getCostPerQuantity()));
				productResultList.add(productResult);
			}
		} else {
			_logger.error("No records found for result");
		}
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("No. of products available are: {}", productList.size());
		}
		return productResultList;
	}

	/**
	 * This method is responsible to get {@link VbProductCustomerCost} from
	 * database.
	 * 
	 * @param productCustomerCostId
	 * @param organization
	 * @return vbProductCustomerCost
	 */
	@SuppressWarnings("unchecked")
	public List<VbProductCustomerCost> getProductCustomerCost(String businessName , VbOrganization organization) {
		Session session=this.getSession();
		//VbProductCustomerCost vbProductCustomerCost=(VbProductCustomerCost)session.load(VbProductCustomerCost.class, this.getI)
		 List<VbProductCustomerCost> productCustomerCostList = session
				.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("VbProductCustomerCost: {}", productCustomerCostList);
		}
		return productCustomerCostList;
	}

	/**
	 * This method is responsible for getting product customer cost based on
	 * customer business name entered by user.
	 * 
	 * @param productCustomerCostCommand
	 * @param organization
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getProductCustomerCostByBusinessName(
			ProductCustomerCostCommand productCustomerCostCommand , VbOrganization organization) {
		List<ProductResult> productCustomerCostResultList = null;
		Session session = this.getSession();
		VbCustomer vbCustomer = null;
		Integer count = 0;
		List<VbProductCustomerCost> vbProductCustomerCostList = null;
		if (productCustomerCostCommand != null) {
			String businessName = productCustomerCostCommand.getBusinessName();
			vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
					.add(Expression.eq("businessName", businessName))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			ProductResult productCustomerCostResult = null;
			if (vbCustomer != null) {
				vbProductCustomerCostList = session
						.createCriteria(VbProductCustomerCost.class)
						.createAlias("vbCustomer", "customer")
						.createAlias("vbProduct", "product")
						.add(Expression.eq("customer.id", vbCustomer.getId()))
						.add(Expression.eq("vbOrganization", organization)).list();
				productCustomerCostResultList = new ArrayList<ProductResult>();
				for (VbProductCustomerCost vbProductCustomerCost : vbProductCustomerCostList) {
					productCustomerCostResult = new ProductResult();
					productCustomerCostResult.setCost(StringUtil.currencyFormat(vbProductCustomerCost.getCost()));
					productCustomerCostResult.setProductName(vbProductCustomerCost.getVbProduct().getProductName());
					productCustomerCostResult.setBatchNumber(vbProductCustomerCost.getVbProduct().getBatchNumber());
					productCustomerCostResult.setCostPerQuantity(StringUtil.currencyFormat(vbProductCustomerCost.getVbProduct().getCostPerQuantity()));
					productCustomerCostResult.setId(++count);
					productCustomerCostResultList.add(productCustomerCostResult);
				}

			} else {
				if (_logger.isErrorEnabled()) {
					_logger.error("No customer available for the businessName: {}", businessName);
				}
			}
		}
		if (_logger.isErrorEnabled()) {
			_logger.error("ProductCustomerCostResultList", productCustomerCostResultList);
		}

		return productCustomerCostResultList;

	}

	/**
	 * This Method Is Responsible To Update ProductCustomerCost.
	 * 
	 * @param productCustomerCostCommand
	 * @param username
	 */
	@SuppressWarnings("unchecked")
	public void updateProductCustomerCost(ProductCustomerCostCommand productCustomerCostCommand,String username, VbOrganization organization) {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		if (productCustomerCostCommand != null) {
			String businessName = productCustomerCostCommand.getBusinessName();
			List<VbProductCustomerCost> productCustomerCosts = session
					.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbProduct", "product")
					.add(Expression.eq("customer.businessName", businessName))
					.add(Expression.eq("product.productName",productCustomerCostCommand.getProductName()))
					.add(Expression.eq("product.batchNumber",productCustomerCostCommand.getBatchNumber()))
					.add(Expression.eq("customer.vbOrganization", organization))
					.list();
			for (VbProductCustomerCost vbProductCustomerCost : productCustomerCosts) {
				vbProductCustomerCost.setCost(productCustomerCostCommand.getCost());
				vbProductCustomerCost.setModifiedBy(username);
				vbProductCustomerCost.setModifiedOn(new Date());
				session.update(vbProductCustomerCost);
			}
			txn.commit();
			session.close();

		}
	}
	
	/**
	 * This method is used to get the businessnames based on the vborganization.
	 * 
	 * @param businessName
	 * @param organization
	 * @param userName
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomer> getBusinessName(String businessName , VbOrganization organization) {
		Session session = this.getSession();
		List<VbCustomer> businessNameList = session.createCriteria(VbCustomer.class)
			    .add(Expression.eq("vbOrganization", organization))
			    .add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
			    .addOrder(Order.asc("businessName"))
			    .list();
			  session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("Business Name List: {}", businessNameList);
		}
		return businessNameList;
	}

	
}
