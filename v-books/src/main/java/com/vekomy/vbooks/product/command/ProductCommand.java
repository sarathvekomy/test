package com.vekomy.vbooks.product.command;
import com.vekomy.vbooks.hibernate.model.VbProduct;

/**
 * This command class is a intermediate class for {@link VbProduct}.
 * 
 * @author ankit
 * 
 */
public class ProductCommand extends VbProduct {
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 3099790963874861436L;
	
	public ProductCommand() {
		
	}
	/**
	 * @param id
	 */
	public ProductCommand(int id){
		this.id=id;
	}
	/**
	 * Integer variable hold id.
	 */
	private Integer id;
	/**
	 * String variable holds action.
	 */
	public String action;
	
	/**
	 * Integer variable holds page number
	 */
	public Integer pageNumber;
	/**
	 * String variable holds productName.
	 */
	private String productName;
	/**
	 * String variable holds batchNumber.
	 */
	private String batchNumber;
	/**
	 * String variable holds transactionType.
	 */
	private String transactionType;
	/**
	 * String variable holds productCategory.
	 */
    private String productCategory;
    /**
	 * String variable holds brand.
	 */
	private String brand;
	/**
	 * String variable holds model.
	 */
	private String model;
	/**
	 * String variable holds description.
	 */
	private String description;
	/**
	 * float variable holds costPerQuantity.
	 */
	private float costPerQuantity;
	/**
	 * Integer variable holds qunatityArrived.
	 */
	private Integer qunatityArrived;
	/**
	 * Integer variable holds damagedQuantity.
	 */
	private Integer damagedQuantity;
	
	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param Id
	 *            the product id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	

	/**
	 * @return the action
	 */
	public String getAction() {

		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return page number
	 */
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber the page number to set
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	/**
	 * @return productCategory
	 */
	public String getProductCategory() {
		return productCategory;
	}
	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	/**
	 * @return brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return costPerQuantity
	 */
	public float getCostPerQuantity() {
		return costPerQuantity;
	}
	/**
	 * @param costPerQuantity the costPerQuantity to set
	 */
	public void setCostPerQuantity(float costPerQuantity) {
		this.costPerQuantity = costPerQuantity;
	}
	/**
	 * @return qunatityArrived
	 */
	public Integer getQunatityArrived() {
		return qunatityArrived;
	}
	/**
	 * @param qunatityArrived the qunatityArrived to set
	 */
	public void setQunatityArrived(Integer qunatityArrived) {
		this.qunatityArrived = qunatityArrived;
	}
	/**
	 * @return damagedQuantity
	 */
	public Integer getDamagedQuantity() {
		return damagedQuantity;
	}
	/**
	 * @param damagedQuantity the damagedQuantity to set
	 */
	public void setDamagedQuantity(Integer damagedQuantity) {
		this.damagedQuantity = damagedQuantity;
	}
}
