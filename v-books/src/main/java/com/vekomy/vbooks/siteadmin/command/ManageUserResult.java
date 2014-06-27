 /**
 * ${package_name}.${file_name}
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: ${date}
 */
package com.vekomy.vbooks.siteadmin.command;

/**
 * @author priyanka
 *
 */
public class ManageUserResult extends ManageUserBasicCommand {

	/**
	 * long variable holds serialVersionUID.
	 */
	private static final long serialVersionUID = 7897824763159017084L;

	/**
	 * String variable holds addressLine1
	 */
	private String addressLine1;
	
	/**
	 * String variable holds addressLine2
	 */
	private String addressLine2;
	
	/**
	 * String variable holds locality
	 */
	private String locality;
	
	/**
	 * String variable holds landmark
	 */
	private String landmark;

	/**
	 * String variable holds city
	 */
	private String city;
	
	/**
	 * String variable holds state
	 */
	private String state;
	
	/**
	 * String variable holds zipcode
	 */
	private String zipcode;
	
	/**
	 * String variable holds addressType
	 */
	private String addressType;
	
	/**
	 * int variable holds id
	 */
	private int id;
	
	/**
	 * String variable holds disabledLoginUserName
	 */
	private String disabledLoginUserName;

	public String getDisabledLoginUserName() {
		return disabledLoginUserName;
	}

	public void setDisabledLoginUserName(String disabledLoginUserName) {
		this.disabledLoginUserName = disabledLoginUserName;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return the landmark
	 */
	public String getLandmark() {
		return landmark;
	}

	/**
	 * @param landmark the landmark to set
	 */
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	
}
