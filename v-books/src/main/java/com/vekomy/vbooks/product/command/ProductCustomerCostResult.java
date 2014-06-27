package com.vekomy.vbooks.product.command;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is responsible for display products search results.
 * 
 * @author Vinay
 * 
 * 
 */
public class ProductCustomerCostResult  implements Serializable,Comparable<ProductCustomerCostResult>{
	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 319201037108414443L;

	/**
	 * String variable holds BusinessName.
	 */
	private String businessName;

	/**
	 * String variable holds product name.
	 */
	private String productName;
	
	/**
	 * String variable holds product batch number.
	 */
	private String batchNumber;
	
	/**
	 * Float variable holds product cost.
	 */
	private String cost;
	/**
	 * Integer variable hold id.
	 */
	private Integer id;
	/**
	 * String variable holds product name.
	 */
	/**
	 * Date variable holds createdDate
	 */
	private Date createdDate;
	/**
	 * float variable holds costPerQuantity
	 */
	private float costPerQuantity;
	
	/**
	 * String variable holds customerState
	 */
	private String customerState ;
	/**
	 * 
	 * @return Id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the Id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *        the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}
	/**
	 * @return the businessName
	 */
	public String getBusinessName() {
		return businessName;
	}
    
	/**
	 * @param businessName
	 *             the businessName to set
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return the product name
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName
	 *            the product name to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the batch number
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
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the costPerQuantity
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
	 * @return the customerState
	 */
	public String getCustomerState() {
		return customerState;
	}
	/**
	 * @param customerState the customerState to set
	 */
	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}
	
	public boolean equals(Object o){
		if(o instanceof ProductCustomerCostResult){
			ProductCustomerCostResult result=(ProductCustomerCostResult) o;
			if(this.businessName.equals(result.businessName)){
				return true;
			}
		}
				return false;
		
		
	}
	public int hashCode(){
		return this.businessName.hashCode();
	}
	
	//To sort search result based on customer state(disabled or enabled)
	@Override
	public int compareTo(ProductCustomerCostResult o) {
		if(o.customerState.equalsIgnoreCase("Enabled")){
			return 1;
		}else
		return -1;
	}
	
}
