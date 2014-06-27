package com.vekomy.vbooks.product.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbProduct;
import com.vekomy.vbooks.hibernate.model.VbProductCustomerCost;
import com.vekomy.vbooks.hibernate.model.VbProductInventoryTransaction;
import com.vekomy.vbooks.product.command.ProductCommand;
import com.vekomy.vbooks.product.command.ProductResult;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
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
	 * Static variable for size of the page
	 */
	private static final Integer pageSize = 100;
	/**
	 * Integer variable holds defaultWareHouseQuantity.
	 */
	private static final Integer defaultWareHouseQuantity = 0;

	/**
	 * This method is responsible for persist operation on {@link VbProduct}.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return isSaved - {@link Boolean}
	 * @throws DataAccessException
	 */
	public void saveProduct(final ProductCommand productCommand, final String userName,
			final VbOrganization vbOrganization) throws DataAccessException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession();
			transaction = session.beginTransaction();
			final Date createdDate = new Date();
			final VbProduct instance = new VbProduct();
			instance.setProductCategory(productCommand.getProductCategory());
			instance.setBrand(productCommand.getBrand());
			instance.setModel(productCommand.getModel());
			instance.setProductName(productCommand.getProductName());
			instance.setBatchNumber(productCommand.getBatchNumber());
			instance.setDescription(productCommand.getDescription());
			instance.setCostPerQuantity(productCommand.getCostPerQuantity());
			instance.setAvailableQuantity(0);
			instance.setQuantityAtWarehouse(0);
			instance.setVbOrganization(vbOrganization);
			instance.setCreatedBy(userName);
			instance.setCreatedOn(createdDate);
			instance.setModifiedOn(createdDate);
			instance.setState(OrganizationUtils.PRODUCT_ENABLED);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbProduct");
			}
			session.save(instance);
			transaction.commit();
		} catch (final Exception exception) {
			if(transaction != null) {
				transaction.rollback();
			}
			final String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			if (_logger.isErrorEnabled()) {
				_logger.error(errorMsg);
			}
			throw new DataAccessException(errorMsg);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to check whether product exists or not.
	 * 
	 * @param productName - {@link String}
	 * @param batchNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return isProductExists - {@link Boolean}
	 */
	public Boolean isProductExists(final String productName, final String batchNumber, final VbOrganization organization) {
		Boolean isProductExists = Boolean.FALSE;
		final Session session = this.getSession();
		final VbProduct vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Restrictions.eq("productName", productName))
				.add(Restrictions.eq("batchNumber", batchNumber))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(vbProduct != null) {
			isProductExists = Boolean.TRUE;
		}
		return isProductExists;
	}

	/**
	 * This Method is Responsible to Check whether The Entered Product Name is
	 * Duplicate Or Not.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @return isAvailable - {@link String}
	 */
	public String validateProductName(final ProductCommand productCommand, final VbOrganization organization) {
		String isAvailable = "y";
		final Session session = this.getSession();
		final VbProduct product = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("productName", productCommand.getProductName()))
				.add(Expression.eq("batchNumber", productCommand.getBatchNumber()))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if (product != null) {
			isAvailable = "n";
		}
		return isAvailable;
	}
	/**
	 * This Method is Responsible to Check whether The Entered Product Name is
	 * Duplicate Or Not.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @return isAvailable - {@link String}
	 */
	public String validateEditProductName(final ProductCommand productCommand, final VbOrganization organization) {
		String isAvailable = "y";
		final Session session = this.getSession();
		final VbProduct product = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Expression.eq("productName", productCommand.getProductName()))
				.add(Expression.eq("batchNumber", productCommand.getBatchNumber()))
				.add(Expression.eq("vbOrganization", organization))
				.add(Restrictions.ne("id",productCommand.getId()))
				.uniqueResult();
		session.close();
		
		if (product != null) {
			isAvailable = "n";
		}
		return isAvailable;
	}
	
	/**
	 * This method is responsible for update product details in to db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param vbOrganization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void editProduct(final ProductCommand productCommand, final String userName, final VbOrganization vbOrganization) throws DataAccessException {
		final Session session = this.getSession();
		final VbProduct productInstance = (VbProduct) session.get(VbProduct.class, productCommand.getId());
		if (productInstance != null) {
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				productInstance.setProductCategory(productCommand.getProductCategory());
				productInstance.setBrand(productCommand.getBrand());
				productInstance.setModel(productCommand.getModel());
				productInstance.setProductName(productCommand.getProductName());
				productInstance.setBatchNumber(productCommand.getBatchNumber());
				productInstance.setDescription(productCommand.getDescription());
				productInstance.setCostPerQuantity(productCommand.getCostPerQuantity());
				productInstance.setQuantityAtWarehouse(productInstance.getQuantityAtWarehouse());
				productInstance.setAvailableQuantity(productInstance.getAvailableQuantity());
				productInstance.setVbOrganization(vbOrganization);
				productInstance.setCreatedBy(userName);
				productInstance.setModifiedOn(new Date());

				if (_logger.isDebugEnabled()) {
					_logger.debug("Updating VbProduct");
				}
				session.update(productInstance);
				transaction.commit();
			} catch (final HibernateException exception) {
				if(transaction != null) {
					transaction.rollback();
				}
				final String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
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
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for delete product details in to db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void deleteProduct(final ProductCommand productCommand,
			final VbOrganization organization) throws DataAccessException {
		final Session session = this.getSession();
		final VbProduct vbProduct = (VbProduct) session.get(VbProduct.class, productCommand.getId());
		if (vbProduct != null) {
			final VbProductCustomerCost vbProductCustomerCost = (VbProductCustomerCost) session.createCriteria(VbProductCustomerCost.class)
					.add(Expression.eq("vbProduct", vbProduct))
					.add(Expression.eq("vbOrganization", organization))
					.uniqueResult();
			Transaction transaction = null;
			try {
				transaction = session.beginTransaction();
				if (vbProductCustomerCost != null) {
					if (_logger.isDebugEnabled()) {
						_logger.debug("Deleting vbProductCustomerCost");
					}
					session.delete(vbProductCustomerCost);
				}
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Deleting VbProduct");
				}
				session.delete(vbProduct);
				transaction.commit();
			} catch (final HibernateException exception) {
				if(transaction != null) {
					transaction.rollback();
				}
				final String message = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
				
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
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for search product details from db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param vbOrganization - {@link VbOrganization}
	 * @return productDetailList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ProductResult> searchProduct(final ProductCommand productCommand, final VbOrganization vbOrganization) throws DataAccessException {
		final Session session = this.getSession();
		final Criteria searchCriteria = session.createCriteria(VbProduct.class);
		if (productCommand != null) {
			final String productName = productCommand.getProductName();
			final String batchNumber = productCommand.getBatchNumber();
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
		searchCriteria.addOrder(Order.desc("state"));
		searchCriteria.addOrder(Order.asc("productName"));
		final List<VbProduct> productList = searchCriteria.list();
		session.close();

		if(!productList.isEmpty()) {
			ProductResult productResult = null;
			final List<ProductResult> productDetailList = new ArrayList<ProductResult>();
			for (final VbProduct product : productList) {
				productResult = new ProductResult();
				productResult.setId(product.getId());
				productResult.setProductCategory(product.getProductCategory());
				productResult.setProductName(product.getProductName());
				productResult.setBatchNumber(product.getBatchNumber());
				productResult.setDescription(product.getDescription());
				productResult.setCostPerQuantity(StringUtil.currencyFormat(product.getCostPerQuantity()));
				productResult.setDescription(product.getDescription());
				productResult.setState(product.getState());
				productDetailList.add(productResult);
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", productDetailList.size());
			}
			return productDetailList;
		} else {
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible to get {@link VbProduct} from database.
	 * 
	 * @param id - {@link Integer}
	 * @return instance - {@link VbProduct}
	 */
	public VbProduct getProduct(final int id, final VbOrganization organization) {
		final Session session = this.getSession();
		final VbProduct instance = (VbProduct) session.get(VbProduct.class, id);
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
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbProduct> getProductList(final VbOrganization vbOrganization) throws DataAccessException {
		final Session session = this.getSession();
		final List<VbProduct> productsList = session.createCriteria(VbProduct.class)
				.add(Restrictions.eq("vbOrganization", vbOrganization))
				.add(Restrictions.eq("state", OrganizationUtils.PRODUCT_ENABLED))
				.addOrder(Order.asc("productName"))
				.list();
		session.close();
		
		if(!productsList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", productsList.size());
			}
			return productsList;
		} else {
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for getting product name based on product id.
	 * 
	 * @param id - {@link Integer}
	 * @return productName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getProductNameById(final int id) throws DataAccessException {
		final Session session = this.getSession();
		final VbProduct product = (VbProduct) session.get(VbProduct.class, id);
		session.close();
		
		if(product != null) {
			final String productName = product.getProductName();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} is the product associated with id {}", productName, id);
			}
			return productName;
		} else {
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for getting product transaction details from
	 * db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @return productInventoryTransaction - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<VbProductInventoryTransaction> getProductTransactionList(
			final ProductCommand productCommand, final VbOrganization organization) throws DataAccessException {
		final Session session = this.getSession();
		final Criteria criteria = session.createCriteria(VbProductInventoryTransaction.class);
		if (productCommand != null) {
			final String productName = productCommand.getProductName();
			if (!productName.isEmpty()) {
				criteria.add(Restrictions.like("productName", productName, MatchMode.START).ignoreCase());
			}
			
			final String batchNo = productCommand.getBatchNumber();
			if (!batchNo.isEmpty()) {
				criteria.add(Restrictions.like("batchNumber", batchNo, MatchMode.START).ignoreCase());
			}
			
			final String txnType = productCommand.getTransactionType();
			if (!txnType.isEmpty()) {
				criteria.add(Restrictions.like("quantityType", txnType, MatchMode.START).ignoreCase());
			}
			
			final Date createdOn = productCommand.getCreatedOn();
			if (createdOn!= null) {
				criteria.add(Restrictions.between("createdOn", 	DateUtils.getStartTimeStamp(createdOn), 
						DateUtils.getEndTimeStamp(createdOn)));
			}
		}
		criteria.add(Expression.eq("vbOrganization", organization));
		criteria.setFirstResult(pageSize * (productCommand.getPageNumber() - 1));
		criteria.setMaxResults(pageSize);
		criteria.addOrder(Order.desc("createdOn"));
		final List<VbProductInventoryTransaction> productInventoryTransactionList = criteria.list();
		session.close();
		
		if(!productInventoryTransactionList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found.", productInventoryTransactionList.size());
			}
			return productInventoryTransactionList;
		} else {
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for add all product arrived quantity in db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void addArrivedQuantity(final List<ProductCommand> productCommandList,
			final String userName, final VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession();
			transaction = session.beginTransaction();
			final Date date = new Date();
			VbProduct vbProduct = null;
			String productName = null;
			String batchNumber = null;
			Integer availableQuantity = new Integer(0);
			Integer qtyArrived = new Integer(0);
			VbProductInventoryTransaction inventoryTransaction = null;
			for (final ProductCommand command : productCommandList) {
				productName = command.getProductName();
				batchNumber = command.getBatchNumber();
				qtyArrived = command.getQunatityArrived();
				vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();

				if (vbProduct != null) {
					availableQuantity =  qtyArrived + vbProduct.getAvailableQuantity();
					vbProduct.setAvailableQuantity(availableQuantity);
					vbProduct.setModifiedOn(date);
					vbProduct.setModifiedBy(userName);
					vbProduct.setQuantityAtWarehouse(qtyArrived + vbProduct.getQuantityAtWarehouse());
					
					if (_logger.isDebugEnabled()) {
						_logger.debug("Updating VbProduct");
					}
					session.update(vbProduct);
					
					inventoryTransaction = new VbProductInventoryTransaction();
					if (qtyArrived > 0) {
						inventoryTransaction.setProductName(productName);
						inventoryTransaction.setBatchNumber(batchNumber);
						inventoryTransaction.setCreatedBy(userName);
						inventoryTransaction.setCreatedOn(date);
						inventoryTransaction.setModifiedOn(date);
						inventoryTransaction.setVbOrganization(organization);
						inventoryTransaction.setQuantityType(OrganizationUtils.PRODUCT_INVENTORY_TXN_TYPE_ARRIVED);
						inventoryTransaction.setQuantity(qtyArrived);
					}

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting productInventoryTransaction");
					}
					session.save(inventoryTransaction);
				}
			}// foreach loop
			transaction.commit();
		} catch (final HibernateException exception) {
			if(transaction != null) {
				transaction.rollback();
			}
			final String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
				_logger.error(message);
			}
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible for add all product arrived quantity in db.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void addDamagedQuantity(final List<ProductCommand> productCommandList,
			final String userName, final VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = this.getSession();
			transaction = session.beginTransaction();
			final Date date = new Date();
			VbProduct vbProduct = null;
			String productName = null;
			String batchNumber = null;
			Integer availableQty = new Integer(0);
			Integer damagedQty = new Integer(0);
			Integer remainingQty = new Integer(0);
			VbProductInventoryTransaction inventoryTransaction = null;
			for (final ProductCommand command : productCommandList) {
				productName = command.getProductName();
				batchNumber = command.getBatchNumber();
				vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
						.add(Expression.eq("productName", productName))
						.add(Expression.eq("batchNumber", batchNumber))
						.add(Expression.eq("vbOrganization", organization))
						.uniqueResult();
				if (vbProduct != null) {
					availableQty = vbProduct.getAvailableQuantity();
					damagedQty = command.getDamagedQuantity();
					if (availableQty >= damagedQty) {
						remainingQty = availableQty - damagedQty;
						vbProduct.setModifiedBy(userName);
						vbProduct.setModifiedOn(date);
						vbProduct.setAvailableQuantity(remainingQty);
						vbProduct.setQuantityAtWarehouse(vbProduct.getQuantityAtWarehouse() - damagedQty);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Updating VbProduct.");
						}
						session.update(vbProduct);
						
						inventoryTransaction = new VbProductInventoryTransaction();
						if (damagedQty > 0) {
							inventoryTransaction.setProductName(productName);
							inventoryTransaction.setBatchNumber(batchNumber);
							inventoryTransaction.setCreatedBy(userName);
							inventoryTransaction.setCreatedOn(date);
							inventoryTransaction.setModifiedOn(date);
							inventoryTransaction.setVbOrganization(organization);
							inventoryTransaction.setQuantityType(OrganizationUtils.PRODUCT_INVENTORY_TXN_TYPE_DAMAGED);
							inventoryTransaction.setQuantity(damagedQty);
						}

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting productInventoryTransaction.");
						}
						session.save(inventoryTransaction);
					}
				}
			}// foreach loop
			transaction.commit();
		} catch (final HibernateException exception) {
			final String message = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			if(_logger.isErrorEnabled()) {
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
	 * This method is responsible for getting count of records from table.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return countTransactionRecords - {@link Integer}
	 */
	public Integer getRecordsCount(final VbOrganization organization) {
		final Session session = this.getSession();
		final Integer countTransactionRecords = (Integer) session.createCriteria(VbProductInventoryTransaction.class)
				.setProjection(Projections.rowCount())
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", countTransactionRecords);
		}
		return countTransactionRecords;
	}

	/**
	 * This method is responsible for getting count of records from table.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @return countTransactionRecords - {@link Integer}
	 */
	public Integer getRecordsCountForCriteria(final ProductCommand productCommand,
			final VbOrganization organization) {
		final Session session = this.getSession();
		final Criteria criteria = session.createCriteria(VbProductInventoryTransaction.class);
		if (productCommand != null) {
			final String productName = productCommand.getProductName();
			if (!productName.isEmpty()) {
				criteria.add(Restrictions.like("productName", productName, MatchMode.START).ignoreCase());
			}
			final String batchNo = productCommand.getBatchNumber();
			if (!batchNo.isEmpty()) {
				criteria.add(Restrictions.like("batchNumber", batchNo, MatchMode.START).ignoreCase());
			}
			final String txnType = productCommand.getTransactionType();
			if (!txnType.isEmpty()) {
				criteria.add(Restrictions.like("quantityType", txnType, MatchMode.START).ignoreCase());
			}
			final Date createdOn = productCommand.getCreatedOn();
			if (createdOn != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(createdOn),
						DateUtils.getEndTimeStamp(createdOn)));
			}
		}
		criteria.add(Expression.eq("vbOrganization", organization));
		final Integer count = criteria.list().size();
		session.close();
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("{} records have been found.", count);
		}
		return count;
	}

	/**
	 * This method is responsible for modifying status of product.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param productStatusParam - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public List<ProductResult> modifyProductStatus(final ProductCommand productCommand,
			final String productStatusParam, final VbOrganization organization) throws DataAccessException {
		final Session session = this.getSession();
		final ProductCommand productCmdObj = null;
		List<ProductResult> productListWithModifiedStatus = new ArrayList<ProductResult>();
		final VbProduct vbProduct = (VbProduct) session.get(VbProduct.class, productCommand.getId());
		
		if (vbProduct != null) {
			final Transaction tx = session.beginTransaction();
			if (OrganizationUtils.PRODUCT_DISABLED.equalsIgnoreCase(productStatusParam)) {
				vbProduct.setState(OrganizationUtils.PRODUCT_DISABLED);
			} else if (OrganizationUtils.PRODUCT_ENABLED.equalsIgnoreCase(productStatusParam)) {
				vbProduct.setState(OrganizationUtils.PRODUCT_ENABLED);
			}
			session.update(vbProduct);
			tx.commit();
			session.close();
			productListWithModifiedStatus=searchProduct(productCmdObj,organization);
			return productListWithModifiedStatus;
		} else {
			session.close();
			final String message = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			if(_logger.isWarnEnabled()) {
				_logger.warn(message);
			}
			throw new DataAccessException(message);
		}
	}

	/**
	 * This method is responsible for checking product in warehouse before
	 * disabling that product.
	 * 
	 * @param productCommand - {@link ProductCommand}
	 * @param organization - {@link VbOrganization}
	 * @return isContains - {@link Boolean}
	 */
	public Boolean checkProductWarehouseQuantity(final ProductCommand productCommand, final VbOrganization organization) {
		Boolean isContains = Boolean.FALSE;
		final Session session = this.getSession();
		final VbProduct vbProduct = (VbProduct) session.createCriteria(VbProduct.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("id", productCommand.getId()))
				.add(Restrictions.eq("quantityAtWarehouse", defaultWareHouseQuantity))
				.uniqueResult();
		session.close();
		
		if (vbProduct != null) {
			isContains = Boolean.TRUE;
		}
		return isContains;
	}
}