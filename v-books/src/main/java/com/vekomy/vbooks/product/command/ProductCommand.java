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
	
	private static final long serialVersionUID = 1L;
	/**
	 * Integer variable hold id.
	 */
	private Integer id;

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

	public ProductCommand() {

	}

	public ProductCommand(Integer id) {
		this.id = id;
	}

	/**
	 * String variable holds action.
	 */
	public String action;


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
	 * Integer variable holds page number
	 */
	public Integer pageNumber;
	

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

	public String toString() {
		return new StringBuffer("[ action :").append(getAction())
				.append("]").toString();
	}

}
