package com.vekomy.vbooks.product.command;
/**
 * This command class is responsible for display product search result and product customer cost Search results.
 * 
 * @author ankit
 * 
 */
public class ProductResult  {
	/**
	 * Integer variable hold id.
	 */
	private Integer id;
	/**
	 * String variable holds Product Category
	 */
	private String productCategory;
	/**
	 * String variable holds product name.
	 */
	private String productName;
	/**
	 * String variables holds batch number.
	 */
	private String batchNumber;
	/**
	 * String variable holds description.
	 */
	private String description;
	/**
	 * String variable holds cost per quantity.
	 */
	private String costPerQuantity;
	/**
	 * Float variable holds product cost.
	 */
	private String cost;
	/**
	 * @return product id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param product id
	 *            the product id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @param product category
	 *            the product category to set
	 */
	public String getProductCategory() {
		return productCategory;
	}
	/**
	 * @return product category
	 */
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	/**
	 * @return product name
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param product name
	 *            the product name to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return batch number
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batch number
	 *            the batch number to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	/**
	 * @return product description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description
	 *            the product description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return cost per quantity
	 */
	public String getCostPerQuantity() {
		return costPerQuantity;
	}
	/**
	 * @param cost per quantity
	 *            the cost per quantity to set
	 */
	public void setCostPerQuantity(String costPerQuantity) {
		this.costPerQuantity = costPerQuantity;
	}
	/**
	 * @return product customer cost
	 */
	public String getCost() {
		return cost;
	}
	/**
	 * @param product customer cost
	 *            the product customer cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}
public String toString(){
	return new StringBuffer("[productName :").append(getProductName())
			.append("productCategory :").append(getProductCategory())
			.append("batchNumber :").append(getBatchNumber())
			.append("description :").append(getDescription())
			.append("costPerQuantity :").append(getCostPerQuantity())
			.append("cost :").append(getCost())
			.append("]").toString();
}

}
