/**
 * com.vekomy.vbooks.product.dao.ProductCustomerCostDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 15, 2013
 */
package com.vekomy.vbooks.product.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.product.command.ProductCustomerCostCommand;
import com.vekomy.vbooks.product.command.ProductCustomerCostResult;
import com.vekomy.vbooks.product.command.ProductCustomerCostViewResult;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This class is responsible for perform product customer cost request.
 * 
 * @author Ankit
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
	 * @throws DataAccessException 
	 */
	public void saveProductCustomerCost(
			ProductCustomerCostCommand productCustomerCostCommand,
			String createdBy, VbOrganization organization)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			String businessName = productCustomerCostCommand.getBusinessName();
			VbCustomer vbCustomer = getCustomer(session, businessName, organization);
			VbProduct vbProduct = getProduct(session, productCustomerCostCommand.getProductName(), 
					productCustomerCostCommand.getBatchNumber(), organization);
			VbProductCustomerCost instanceProductCustomerCost = (VbProductCustomerCost) session
					.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbProduct", "product")
					.createAlias("vbCustomer", "customer")
					.add(Expression.eq("vbProduct", vbProduct))
					.add(Expression.eq("vbCustomer", vbCustomer))
					.add(Expression.eq("product.vbOrganization", organization))
					.add(Expression.eq("customer.vbOrganization", organization))
					.uniqueResult();
			if (instanceProductCustomerCost != null && businessName.equals(instanceProductCustomerCost.getVbCustomer().getBusinessName())) {
				instanceProductCustomerCost.setCreatedBy(createdBy);
				instanceProductCustomerCost.setCreatedOn(date);
				instanceProductCustomerCost.setModifiedOn(date);
				instanceProductCustomerCost.setVbCustomer(vbCustomer);
				instanceProductCustomerCost.setVbProduct(vbProduct);
				instanceProductCustomerCost.setCost(productCustomerCostCommand.getCost());
				instanceProductCustomerCost.setVbOrganization(organization);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbProductCustomerCost.");
				}
				session.update(instanceProductCustomerCost);
			} else {
				VbProductCustomerCost vbProductCustomerCost = new VbProductCustomerCost();
				vbProductCustomerCost.setCreatedBy(createdBy);
				vbProductCustomerCost.setCreatedOn(date);
				vbProductCustomerCost.setModifiedOn(date);
				vbProductCustomerCost.setVbCustomer(vbCustomer);
				vbProductCustomerCost.setVbProduct(vbProduct);
				vbProductCustomerCost.setCost(productCustomerCostCommand.getCost());
				vbProductCustomerCost.setVbOrganization(organization);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbProductCustomerCost.");
				}
				session.save(vbProductCustomerCost);
			}
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is get {@link VbCustomer} instance.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbCustomer - {@link VbCustomer}
	 */
	private VbCustomer getCustomer(Session session, String businessName, VbOrganization organization) {
		VbCustomer vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		
		return vbCustomer;
	}
	
	/**
	 * This method is get {@link VbProduct} instance.
	 *  
	 * @param session - {@link Session}
	 * @param productName - {@link String}
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbProduct - {@link VbProduct}
	 */
	private VbProduct getProduct(Session session, String productName,
			String batchNumber, VbOrganization organization) {
		VbProduct vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("productName", productName))
				.add(Expression.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();

		return vbProduct;
	}
	
	/**
	 * This method is responsible to check whether business name exist or not.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isExists - {@link Boolean}
	 */
	public Boolean isCustomerExist(String businessName, VbOrganization organization) {
		Boolean isExists = Boolean.FALSE;
		Session session = this.getSession();
		VbCustomer customer = getCustomer(session, businessName, organization);
		session.close();
		
		if(customer != null) {
			isExists = Boolean.TRUE;
		}
		return isExists;
	}
	
	/**
	 * This method is responsible to check whether product name with the batch
	 * number exist or not.
	 * 
	 * @param productName - {@link String} 
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isExists - {@link Boolean}
	 */
	public Boolean isProductExist(String productName, String batchNumber, VbOrganization organization) {
		Boolean isExists = Boolean.FALSE;
		Session session = this.getSession();
		VbProduct product = getProduct(session, productName, batchNumber, organization);
		session.close();
		
		if(product != null) {
			isExists = Boolean.TRUE;
		}
		return isExists;
	}
	
	/**
	 * This method is responsible for search product customer cost details from
	 * database.
	 * 
	 * @param productCommand - {@link ProductCustomerCostCommand}
	 * @param organization - {@link VbOrganization}
	 * @return productDetailList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings({ "unchecked" })
	public List<ProductCustomerCostResult> searchProductCustomerCost(
			ProductCustomerCostCommand productCustomerCostCommand,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String businessName = productCustomerCostCommand.getBusinessName();
		String productName = productCustomerCostCommand.getProductName();
		 new ArrayList<ProductCustomerCostResult>();
		Criteria criteria = session.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.createAlias("vbProduct", "product")
				.add(Expression.eq("customer.vbOrganization", organization));
		if (!businessName.isEmpty()) {
			criteria.add(Expression.like("customer.businessName", '%' + businessName + '%'));
		}
		if (!productName.isEmpty()) {
			criteria.add(Expression.like("product.productName",	'%' + productName + '%'));
		}
		List<VbProductCustomerCost> costList = criteria.list();
		session.close();
		
		if(!costList.isEmpty()) {
			List<ProductCustomerCostResult> productDetails = makeResult(costList);
			List<ProductCustomerCostResult> resultList = new ArrayList<ProductCustomerCostResult>(
					new HashSet<ProductCustomerCostResult>(productDetails));

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", resultList.size());
			}
			Collections.sort(resultList);
			return resultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for performing result using list
	 * 
	 * @param productCustomerCosts - {@link List}
	 * @return productDetails - {@link List}
	 */
	private List<ProductCustomerCostResult> makeResult(
			List<VbProductCustomerCost> productCustomerCosts) {
		ProductCustomerCostResult productCustomerCostResult = null;
		List<ProductCustomerCostResult> productDetails = new ArrayList<ProductCustomerCostResult>();
		List<VbProductCustomerCost> listWithoutDuplicates = new ArrayList<VbProductCustomerCost>(
				new HashSet<VbProductCustomerCost>(productCustomerCosts));
		for (VbProductCustomerCost vbProductCustomerCost : listWithoutDuplicates) {
			productCustomerCostResult = new ProductCustomerCostResult();
			productCustomerCostResult.setBusinessName(vbProductCustomerCost.getVbCustomer().getBusinessName());
			productCustomerCostResult.setCustomerState(vbProductCustomerCost.getVbCustomer().getState());
			productCustomerCostResult.setId(vbProductCustomerCost.getVbCustomer().getId());
			productCustomerCostResult.setCreatedDate(vbProductCustomerCost.getCreatedOn());

			productDetails.add(productCustomerCostResult);
		}
		return productDetails;
	}

	/**
	 * This method is responsible for deleting product customer cost from
	 * database.
	 * 
	 * @param productCustomerCostCommand
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void deleteProductCustomerCost(
			ProductCustomerCostCommand productCustomerCostCommand,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbProductCustomerCost> vbProductCustomerCostList = (List<VbProductCustomerCost>) session.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.id", productCustomerCostCommand.getId()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.list();
		if(!vbProductCustomerCostList.isEmpty()) {
			Transaction txn = session.beginTransaction();
			try {
				for (VbProductCustomerCost vbProductCustomerCost : vbProductCustomerCostList) {
					session.delete(vbProductCustomerCost);
				}
				if (_logger.isDebugEnabled()) {
					_logger.debug("Deleting VbProductCustomerCost");
				}
				txn.commit();
			} catch (HibernateException exception) {
				if (txn != null) {
					txn.rollback();
				}
				String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

				if (_logger.isErrorEnabled()) {
					_logger.error(message);
				}
				throw new DataAccessException(message);
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to retrieve all the product from the DB.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return productNameList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getProductList(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbProduct> productList = session.createCriteria(VbProduct.class)
				.add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
				.add(Restrictions.eq("vbOrganization", organization))
				.addOrder(Order.asc("productName"))
				.list();
		session.close();
		
		if (!(productList.isEmpty())) {
			ProductResult productResult = null;
			List<ProductResult> productResultList = new ArrayList<ProductResult>();
			Integer count = new Integer(0);
			for (VbProduct vbProduct : productList) {
				productResult = new ProductResult();
				productResult.setId(++count);
				productResult.setProductName(vbProduct.getProductName());
				productResult.setBatchNumber(vbProduct.getBatchNumber());
				productResult.setCostPerQuantity(StringUtil.currencyFormat(vbProduct.getCostPerQuantity()));
				
				productResultList.add(productResult);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} products have been found.", productList.size());
			}
			return productResultList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to get {@link VbProductCustomerCost} from
	 * database.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return resultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductCustomerCostViewResult> getProductCustomerCost(
			String businessName, VbOrganization organization) throws DataAccessException {
		ProductCustomerCostViewResult result = null;
		Session session = this.getSession();
		List<VbProductCustomerCost> productCustomerCostList = session.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.add(Expression.eq("customer.businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.list();
		
		if (!productCustomerCostList.isEmpty()) {
			VbCustomer customer = null;
			VbProduct product = null;
			List<ProductCustomerCostViewResult> resultList = new ArrayList<ProductCustomerCostViewResult>();
			for (VbProductCustomerCost productCustomerCost : productCustomerCostList) {
				customer = productCustomerCost.getVbCustomer();
				product = productCustomerCost.getVbProduct();
				result = new ProductCustomerCostViewResult();
				result.setBusinessName(customer.getBusinessName());
				result.setInvoiceName(customer.getInvoiceName());
				result.setCustomerName(customer.getCustomerName());
				result.setGender(customer.getGender());
				result.setCreatedOn(customer.getCreatedOn());
				result.setCost(productCustomerCost.getCost());
				result.setProductCategory(product.getProductCategory());
				result.setProduct(product.getProductName());
				result.setBatchNumber(product.getBatchNumber());
				result.setCostPerQuantity(product.getCostPerQuantity());
				result.setDescription(product.getDescription());
				result.setModifiedBy(productCustomerCost.getModifiedBy());
				result.setModifiedOn(productCustomerCost.getModifiedOn());
				result.setCreatedBy(productCustomerCost.getCreatedBy());
				resultList.add(result);
			}
			session.close();
			Collections.sort(resultList);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", resultList.size());
			}
			return resultList;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for getting product customer cost based on
	 * customer business name entered by user.
	 * 
	 * @param productCustomerCostCommand
	 * @param organization
	 * @return
	 * @throws DataAccessException 
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> getProductCustomerCostByBusinessName(
			ProductCustomerCostCommand productCustomerCostCommand,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbCustomer vbCustomer = null;
		Integer count = new Integer(0);
		String businessName = productCustomerCostCommand.getBusinessName();
		vbCustomer = (VbCustomer) session.createCriteria(VbCustomer.class)
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		
		if (vbCustomer != null) {
			List<VbProductCustomerCost> vbProductCustomerCostList = session.createCriteria(VbProductCustomerCost.class)
					.createAlias("vbCustomer", "customer")
					.createAlias("vbProduct", "product")
					.add(Expression.eq("customer.id", vbCustomer.getId()))
					.add(Expression.eq("vbOrganization", organization))
					.list();
			VbProduct product = null;
			ProductResult productCustomerCostResult = null;
			List<ProductResult> productCustomerCostResultList = new ArrayList<ProductResult>();
			for (VbProductCustomerCost vbProductCustomerCost : vbProductCustomerCostList) {
				productCustomerCostResult = new ProductResult();
				product = vbProductCustomerCost.getVbProduct();
				productCustomerCostResult.setCost(StringUtil.currencyFormat(vbProductCustomerCost.getCost()));
				productCustomerCostResult.setProductName(product.getProductName());
				productCustomerCostResult.setBatchNumber(product.getBatchNumber());
				productCustomerCostResult.setCostPerQuantity(StringUtil.currencyFormat(product.getCostPerQuantity()));
				productCustomerCostResult.setId(++count);
				
				productCustomerCostResultList.add(productCustomerCostResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", productCustomerCostResultList.size());
			}
			return productCustomerCostResultList;
		} else {
			session.close();
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This Method Is Responsible To Update ProductCustomerCost.
	 * 
	 * @param productCustomerCostCommand
	 * @param username
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateProductCustomerCost(
			ProductCustomerCostCommand productCustomerCostCommand,
			String username, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		String businessName = productCustomerCostCommand.getBusinessName();
		List<VbProductCustomerCost> productCustomerCostsList = session.createCriteria(VbProductCustomerCost.class)
				.createAlias("vbCustomer", "customer")
				.createAlias("vbProduct", "product")
				.add(Expression.eq("customer.businessName", businessName))
				.add(Expression.eq("product.productName", productCustomerCostCommand.getProductName()))
				.add(Expression.eq("product.batchNumber", productCustomerCostCommand.getBatchNumber()))
				.add(Expression.eq("customer.vbOrganization", organization))
				.list();
		
		if(!productCustomerCostsList.isEmpty()) {
			try {
				for (VbProductCustomerCost vbProductCustomerCost : productCustomerCostsList) {
					vbProductCustomerCost.setCost(productCustomerCostCommand.getCost());
					vbProductCustomerCost.setModifiedBy(username);
					vbProductCustomerCost.setModifiedOn(new Date());
					session.update(vbProductCustomerCost);
				}
				txn.commit();
			} catch (HibernateException exception) {
				String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				if(_logger.isErrorEnabled()) {
					_logger.error(message);
				}
				throw new DataAccessException(message);
			} finally {
				if(session != null) {
					session.close();
				}
			}
		} else {
			if(session != null) {
				session.close();
			}
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is used to get the business names based on the {@link VbOrganization}.
	 * 
	 * @param businessName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return List - {@link List}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public List<VbCustomer> getBusinessName(String businessName,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbCustomer> businessNameList = session.createCriteria(VbCustomer.class)
				.add(Expression.eq("vbOrganization", organization))
				.add(Restrictions.eq("state", OrganizationUtils.CUSTOMER_ENABLED))
				.add(Expression.like("businessName", businessName, MatchMode.START).ignoreCase())
				.addOrder(Order.asc("businessName"))
				.list();
		session.close();
		
		if(!businessNameList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", businessNameList.size());
			}
			return businessNameList;
		} else {
			String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}
}
