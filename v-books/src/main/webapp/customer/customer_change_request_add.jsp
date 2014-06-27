<%@page
	import="com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequestDetails"%>
<%@page
	import="com.vekomy.vbooks.customer.command.CustomerChangeRequestCommand"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<div id="customer-add-form-container" title="Create Customer">
	<!--<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon customer-add-icon"></div>
			<div class="page-title customer-add-title"></div>
		</div>
	</div>-->
</div>
<div class="ui-content form-panel full-content">

	<form id="customer-change-request-form" style="height: 280px;">
		<div class="add-student-tabs">
			<div class="step-selected">
				<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.CUSTOMER_BASIC_INFO_LABEL)%>
				</div>
			</div>
			<div class="step-no-select">
				<div class="step-selected-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_ADDITIONAL_INFO_LABEL)%>
				</div>
			</div>
			<div class="step-no-select">
				<div class="step-no-select-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%>
				</div>
			</div>
			<div class="step-no-select-corner"></div>
		</div>
		<div class="fieldset-row" style="margin-top: 10px;">
			<div class="fieldset" style="height: 60px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_CHANGE_REQUEST_TYPE)%>
					</div>
					<div class="input-field">
						<select class="mandatory" name="crType" id="crType">
							<option value="true">Existed Customer</option>
							<option value="false" selected="selected">New Customer</option>
						</select>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 60px;"></div>
			<div class="fieldset" style="height: 60px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_GENDER)%></div>
					<div class="input-field" style="width: 43px;border:none !important;">
						<input id="male" type="radio" name="gender" class="gender "
							checked="checked" value="M" style="width: 20px;" /><span
							class="male"><%=Msg.get(MsgEnum.CUSTOMER_GENDER_MALE_LABLE)%></span>
					</div>
					<div class="input-field" style="width: 43px;border:none !important;">
						<input id="female" type="radio" name="gender" class="gender "
							value="F" style="width: 20px;" /><span class="female"><%=Msg.get(MsgEnum.CUSTOMER_GENDER_FEMALE_LABLE)%></span>
					</div>
				</div>
			</div>
		</div>
		<div class="fieldset-row" style="margin-top: 10px;">
			<div class="fieldset" style="height: 60px;">
			<div class="form-row" style="margin-bottom: 30px;">
				<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAME)%>
				</div>
				<div class="input-field" id="uName">
					<input class="mandatory constrained" name="businessName"
						id="businessName" 
						constraints='{"fieldLabel":"businessName","charsOnly":"true"}'>
				</div>
				<div id="business-name-suggestions"
					class="business-name-suggestions" style="z-index: 10;"></div>
				<span id="businessNameValid"
						style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
				<div id="businessName_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CUSTOMER_BUSINESS_NAME) %></p>
						</div>
					</div>
					<div id="businessnamevalid_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_BUSINESS_NAME) %></p>
						</div>
					</div>
					<div id="businessnamelen_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.BUSINESS_NAME_VALIDATION) %></p>
						</div>
					</div>
			</div>
			</div>
			<div class="separator" style="height: 60px;"></div>
			 <div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_NAME)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="customerName"
								id="customerName"
								constraints='{"fieldLabel":"customerName","charsOnly":"true"}'>
						</div>
						<span id="customerNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="cname_pop" class="helppop" style="display: block;margin-top:-10px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;margin-top:2px;">
								<p>Customer Name accepts Characters only.</p>
							</div>
						</div>
						<div id="cnamelen_pop" class="helppop" style="display: block;margin-top:-10px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;margin-top:2px;">
								<p>Customer Should Not Exceed 200 characters</p>
							</div>
						</div>
					</div>
		</div>
		<div id="page-buttons" class="page-buttons">
			<input name="action" value="save-basiccr" type="hidden"
				id="customerAction">
		</div>
	</form>
	<form id="customers-change-request-detail-form" name="form2"
		style="height: 280px; display: none;">
		<div class="add-student-tabs">
			<div class="step-no-select">
				<div class="tabs-title" style="padding-left: 10px"><%=Msg.get(MsgEnum.CUSTOMER_BASIC_INFO_LABEL)%>
				</div>
			</div>
			<div class="step-selected">
				<div class="step-no-select-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_ADDITIONAL_INFO_LABEL)%></div>
			</div>
			<div class="step-no-select">
					<div class="step-selected-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%></div>
			</div>
			<div class="step-no-select-corner"></div>
		</div>
		<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_MOBILE)%></div>
					<div class="input-field">
						<input class="mandatory constrained" 
							constraints='{"fieldLabel":"mobile","numbersOnly":"true"}'
							name="mobile" id="mobile"> <span id="mobileValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="mobile_pop" class="helppop"
							style="display: block; float: left; margin-left: 140px; margin-top: -25px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>Mobile Number can contains numbers, spaces, hyphen(-) and
									braces() </p>
							</div>
						</div>
						<div id="mobilelen_pop" class="helppop" style="display: block;float:left;margin-left:140px;margin-top:-25px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"style="float: left; margin-left: 3px;margin-top:2px;">
							<p>Mobile Number Should Not Exceed 60 characters </p>
						</div>
					</div>
					</div>
				</div>	
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ALTERNATE_MOBILE)%>
					</div>
					<div class="input-field">
						<input name="alternateMobile" id="alternateMobile">
						<span id="altmobileValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="alternate_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>Alternate Mobile can contains numbers, spaces, hyphen(-)
									and curly braces(()).</p>
							</div>
						</div>
						 <div id="alternatelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Alternate Mobile Should Not Exceed 80 Digits.</p></div>
            </div>
					</div>
				</div>
			</div>
				<div class="separator" style="height: 60px;"></div>								
				<div class="fieldset" style="height: 40px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_DIRECT_LINE)%></div>
					<div class="input-field">
						<input name="directLine" id="directLine"
							constraints='{"fieldLabel":"DirectLine","numbersOnly":"true"}'>
						<span id="directLineValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="dline_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: -5px;">
								<p>Direct Line can contains numbers, spaces, hyphen(-) and
									curly braces(()) .</p>
							</div>
						</div>
						 <div id="dlinelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:-5px;"><p>Direct Line Should Not Exceed 60 Digits.</p></div>
                </div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_EMAIL)%></div>
					<div class="input-field">
						<input  name="email" id="Email"
							constraints='{"fieldLabel":"Email Id","email":"true"}'> <span
							id="emailValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -25px"></span>
						<div id="email_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;"">
								<p>Enter a valid email address Eg: john@xyz.com.</p>
							</div>
						</div>
						<div id="emaillen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:2px;"">
							<p> email address Should Not Exceed 100 characters.</p>
						</div>
					</div>
					</div>
				</div>
				</div>
			</div>	
				<div class="fieldset-row" style="margin-top: 10px;">
			<div class="fieldset" style="height: 200px;">
	         <div class="separator" style="height:30px;"></div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_INVOICE_NAME)%></div>
					<div class="input-field">
						<input class="mandatory" name="invoiceName" id="invoiceName">
					</div>
					<span id="invoiceNameValid"
						style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					<div id="iname_pop" class="helppop"
						style="display: block; margin-top: -5px;" aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px; margin-top: 2px;">
							<p>Invoice Name can have only letters, numbers, spaces.</p>
						</div>
					</div>
					<div id="inamelen_pop" class="helppop" style="display: block;margin-top:-5px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Invoice Name should Not Exceed 200 characters</p></div>
            </div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ADDRESS_LINE_1)%></div>
					<div class="input-field">
						<input  name="addressLine1" id="addressLine1"> 
						<span id="addressLine1Valid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
            <div id="addressLine1len_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Address1 Should Not Exceed 200 characters</p></div>
            </div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ADDRESS_LINE_2)%>
					</div>
					<div class="input-field">
						<input name="addressLine2" id="addressLine2" >
						<span id="address2Valid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_LOCALITY)%>
					</div>
					<div class="input-field">
						<input class="mandatory constrained" name="locality" id="locality"
							constraints='{"fieldLabel":"locality","charsOnly":"true"}'>
						<span id="localityValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="locality_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>Locality can contains letters, numbers and spaces only.</p>
							</div>
						</div>
						 <div id="localitylen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Locality Should Not Exceed 60 characters.</p></div>
            </div> 
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_LANDMARK)%></div>
					<div class="input-field">
						<input name="landmark" id="landmark" > <span
							id="landmarkValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="landmark_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>Landmark can contains letters, numbers, spaces, dashes(-), periods(.), commas(,), hash(#), slash(/) only.</p>
							</div>
						</div>
						 <div id="landmarklen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Landmark Should Not Exceed 60 characters.</p></div>
            </div>
					</div>
				</div>
			</div>
		</div>
		<div class="separator" style="height: 100px; top: 10px"></div>
		<div class="fieldset-row">
			<div class="fieldset" style="margin-top: 0px;">
				<div class="separator" style="height:30px;"></div>
					<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_REGION)%>
					</div>
					<div class="input-field">
						<input class="mandatory constrained" name="region" id="region"
							
							constraints='{"fieldLabel":"region","charsOnly":"true"}'>
						<span id="regionValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="region_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>Region accepts Characters only .</p>
							</div>
						</div>
						<div id="regionlen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Region Should Not Exceed 60 characters.</p></div>
					</div>
					</div>
				</div> 
				 <div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_CITY)%>
					</div>
					<div class="input-field">
						<input  name="city" id="city"
							
							constraints='{"fieldLabel":"city","charsOnly":"true"}'> <span
							id="cityValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="city_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>City accepts alphabetics only.</p>
							</div>
						</div>
						<div id="citylen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>City Should Not Exceed 60 characters.</p></div>
            </div>
					</div>
				</div> 
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_STATE)%>
					</div>
					<div class="input-field">
						<input name="state" id="state"
							
							constraints='{"fieldLabel":"state","charsOnly":"true"}'>
						<span id="stateValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
						<div id="state_pop" class="helppop"
							style="display: block; margin-top: -25px; float: left; margin-left: 140px;"aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px; margin-top: 2px;">
								<p>State accepts alphabetics only </p>
							</div>
						</div>
						 <div id="statelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>State Should Not Exceed 60 characters.</p></div>
            </div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_PINCODE)%>
					</div>
					<div class="input-field">
						<input name="zipcode"
							id="zipcode"> <span id="zipcodeValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								 <div id="pincode_pop" class="helppop"style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Zipcode accepts numbers,letters, dashes(-),spaces only.</p></div>
            </div>
            <div id="pincodelen_pop" class="helppop"style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p>Zipcode should not exceed 9 digits or Should Not Exceed 9 characters.</p></div>
            </div>
					</div>
				</div>
				        <%-- <div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ADDRESS_TYPE) %>
					</div>
										<div class="input-field"> 
											<select  name="addressType" id="addressType">
						</select> 
										</div>
										 <span id="addressTypeValid"  style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
										 <div id="addressType_pop" class="helppop" style="display: block;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;"><p>Select Address Type.</p></div>
            </div>
									</div> --%>
				<div class="form-row">
					<div class="input-field">
						<input name="action" value="save-crdetail" type="hidden"
							id="customerAction">
					</div>
				</div>
			</div>
		</div>
	</form>
	<div id="customer-preview-container" style="display: none; margin-bottom: -40px;"></div>
	<div id="page-buttons" class="page-buttons"
		style="margin-left: 200px; margin-top:40px">
		<div id="button-cr-prev" class="ui-btn btn-prev" style="display: none">Previous</div>
		<div id="button-cr-next" class="ui-btn btn-next">Next</div>
		<div id="button-cr-save" class="ui-btn btn-save"
			style="display: none;">Save</div>
		<div id="action-clear" class="ui-btn btn-clear">Clear</div>
		<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
	</div>
</div>
<script type="text/javascript">
	CustomerCrHandler.initAddCrButtons();
	CustomerCrHandler.load();
	if($('#crType').val() == "false"){
	CustomerCrHandler.initCheckNewCustomerCRBusinessName();
	}
	$(document).ready(function() {
		$('.helppop').hide();
		SystemDefaultsHandler.getAddressTypes();
	});
</script>