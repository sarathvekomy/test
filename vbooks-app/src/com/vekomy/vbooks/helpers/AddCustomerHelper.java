/**
 * com.vekomy.vbooks.helpers.AddCustomerHelper.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 3, 2013
 */
package com.vekomy.vbooks.helpers;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.vekomy.validation.annotations.Email;
import com.vekomy.validation.annotations.Required;
import com.vekomy.vbooks.R;


/**
 * Helper class for adding a new customer.
 * 
 * @author NKR
 * 
 */
public class AddCustomerHelper extends ProgressActivity {

	@Required(order = 1, message = "Business name is required")
	protected EditText businessName;

	protected RadioButton male;
	protected RadioButton female;

	@Required(order = 2, message = "Customer name is required")
	protected EditText customerName;
	
	@Required(order = 3, message = "Invoice name is required")
	protected EditText invoiceName;
	
	@Required(order = 4, message = "Contact Number is required")
	protected EditText contactNumber;

	/*@Required(order = 5, message = "Email Address is required")
	@Email(order = 6, message = "Enter valid email")*/
	protected EditText emailAddress;

	

	protected EditText alternateContactNumber;

	/*@Required(order = 7, message = "Address is required")*/
	protected EditText address1;

	protected EditText address2;

	@Required(order = 5, message = "Locality is required")
	protected EditText locality;

	protected EditText landmark;
	
	protected EditText directLine;
	@Required(order = 6, message = "Region is required")
	protected EditText region;
	
	/*@Required(order = 9, message = "City is required")*/
	protected EditText city;

	/*@Required(order = 10, message = "State is required")*/
	protected EditText state;

	protected EditText zipcode;
	/*@Required(order = 7, message = "AddressType required")
	protected EditText addressType;*/
	
	/*@Required(order = 1, message = "Business name is required")
	protected EditText businessName1;

	protected RadioButton male1;
	protected RadioButton female1;

	@Required(order = 2, message = "Customer name is required")
	protected EditText customerName1;
	
	
	@Required(order = 3, message = "Invoice name is required")
	protected EditText invoiceName1;
	@Required(order = 4, message = "Contact Number is required")
	protected EditText contactNumber1;

	/*@Required(order = 4, message = "Email Address is required")
	@Email(order = 5, message = "Enter valid email")
	protected EditText emailAddress1;

	

	protected EditText alternateContactNumber1;

	/*@Required(order = 7, message = "Address is required")
	protected EditText address11;

	protected EditText address21;

	@Required(order = 5, message = "Locality is required")
	protected EditText locality1;

	protected EditText landmark1;
	
	protected EditText directLine1;
	@Required(order = 6, message = "Locality is required")
	protected EditText region1;
	
	/*@Required(order = 9, message = "City is required")
	protected EditText city1;

	/*@Required(order = 10, message = "State is required")
	protected EditText state1;

	protected EditText zipcode1;
	
	protected EditText addressType1;*/


	/**
	 * Initializes the EditText fields
	 */
	protected void initializeFields() {
		businessName = (EditText) findViewById(R.id.txtBusinessName);
		male = (RadioButton) findViewById(R.id.rbtnMale);
		female = (RadioButton) findViewById(R.id.rbtnfemale);
		customerName = (EditText) findViewById(R.id.txtCustomerName);
		contactNumber = (EditText) findViewById(R.id.txtcontactNo);
		city = (EditText) findViewById(R.id.txtCity);
		directLine = (EditText) findViewById(R.id.txtDirectLine);
		region	= (EditText) findViewById(R.id.txtRegion);
		state = (EditText) findViewById(R.id.txtState);
		invoiceName = (EditText) findViewById(R.id.txtInvoiceNo);
		landmark = (EditText) findViewById(R.id.txtLandMark);
		alternateContactNumber = (EditText) findViewById(R.id.txtAltContactNum);
		locality = (EditText) findViewById(R.id.txtLocality);
		emailAddress = (EditText) findViewById(R.id.txtEmailAddress);
		zipcode = (EditText) findViewById(R.id.txtZipCode);
		address1 = (EditText) findViewById(R.id.txtAddress1);
		address2 = (EditText) findViewById(R.id.txtAddress2);
		//addressType = (EditText) findViewById(R.id.txtAddressType);
	}

	/**
	 * Initializes the EditText fields
	 */
	/*protected void initializeFields(View view) {
		businessName1 = (EditText) view.findViewById(R.id.txtBusinessName);
		male1 = (RadioButton) view.findViewById(R.id.rbtnMale);
		female1 = (RadioButton) view.findViewById(R.id.rbtnfemale);
		customerName1 = (EditText) view.findViewById(R.id.txtCustomerName);
		contactNumber1 = (EditText) view.findViewById(R.id.txtcontactNo);
		city1 = (EditText) view.findViewById(R.id.txtCity);
		directLine1 = (EditText) view.findViewById(R.id.txtDirectLine);
		region1	= (EditText) view.findViewById(R.id.txtRegion);
		state1 = (EditText) view.findViewById(R.id.txtState);
		invoiceName1 = (EditText) view.findViewById(R.id.txtInvoiceNo);
		landmark1 = (EditText) view.findViewById(R.id.txtLandMark);
		alternateContactNumber1 = (EditText) view.findViewById(R.id.txtAltContactNum);
		locality1 = (EditText) view.findViewById(R.id.txtLocality);
		emailAddress1 = (EditText) view.findViewById(R.id.txtEmailAddress);
		zipcode1 = (EditText) view.findViewById(R.id.txtZipCode);
		address11 = (EditText) view.findViewById(R.id.txtAddress1);
		address21 = (EditText) view.findViewById(R.id.txtAddress2);
		addressType1 = (EditText) view.findViewById(R.id.txtAddressType);
	}*/
	/**
	 * Clearing data from fields
	 */
	protected void clearFieldData() {

		businessName.setText("");
		male.setChecked(true);
		customerName.setText("");
		contactNumber.setText("");
		city.setText("");
		directLine.setText("");
		region.setText("");
		state.setText("");
		invoiceName.setText("");
		landmark.setText("");
		alternateContactNumber.setText("");
		locality.setText("");
		emailAddress.setText("");
		zipcode.setText("");
		address1.setText("");
		address2.setText("");
		//addressType.setText("");
		businessName.setError(null);
		customerName.setError(null);
		contactNumber.setError(null);
		region.setError(null);
		invoiceName.setError(null);
		locality.setError(null);

	}

}
