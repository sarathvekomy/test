/**
 * com.vekomy.vbooks.alerts.command.TrendingAlertResult.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 23, 2013
 */
package com.vekomy.vbooks.alerts.command;

/**
 * @author Sudhakar
 * 
 * 
 */
public class TrendingAlertResult {
	/**
	 * Integer variable holds yesterdaySoldQty.
	 */
	private Integer yesterdaySoldQty;
	/**
	 * Integer variable holds todaySoldQty.
	 */
	private Integer todaySoldQty;
	/**
	 * Float variable holds yesterdaysAmount.
	 */
	private Float yesterdaysAmount;
	/**
	 * Float variable holds todaysAmount.
	 */
	private Float todaysAmount;

	/**
	 * @return the yesterdaySoldQty
	 */
	public Integer getYesterdaySoldQty() {
		return yesterdaySoldQty;
	}

	/**
	 * @param yesterdaySoldQty
	 *            the yesterdaySoldQty to set
	 */
	public void setYesterdaySoldQty(Integer yesterdaySoldQty) {
		this.yesterdaySoldQty = yesterdaySoldQty;
	}

	/**
	 * @return the todaySoldQty
	 */
	public Integer getTodaySoldQty() {
		return todaySoldQty;
	}

	/**
	 * @param todaySoldQty
	 *            the todaySoldQty to set
	 */
	public void setTodaySoldQty(Integer todaySoldQty) {
		this.todaySoldQty = todaySoldQty;
	}

	/**
	 * @return the yesterdaysAmount
	 */
	public Float getYesterdaysAmount() {
		return yesterdaysAmount;
	}

	/**
	 * @param yesterdaysAmount
	 *            the yesterdaysAmount to set
	 */
	public void setYesterdaysAmount(Float yesterdaysAmount) {
		this.yesterdaysAmount = yesterdaysAmount;
	}

	/**
	 * @return the todaysAmount
	 */
	public Float getTodaysAmount() {
		return todaysAmount;
	}

	/**
	 * @param todaysAmount
	 *            the todaysAmount to set
	 */
	public void setTodaysAmount(Float todaysAmount) {
		this.todaysAmount = todaysAmount;
	}

}
