<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>

<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.*"%>

<div id="customer-add-form-container" title="Create Customer">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-add-icon"></div>
			<div class="page-title employee-add-title"></div>
			<!-- <div class="favorites-bg">
				<div class="favorite-icon" id="favorite-icon"
					pageLink="employee-add" favTitle="Add Employee"></div>
				<div class="help-icon" id="help-icon-customer"></div>
			</div> -->
		</div>
	</div>
</div>
<div class="ui-content form-panel full-content">
	<form id="customer-form" style="height: 280px;">
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
				<div class="form-row" style="margin-bottom: 30px;">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAME)%>
					</div>
					<div class="input-field" id="uName">
						<input class="mandatory constrained" name="businessName"
							id="businessName" 
							constraints='{"fieldLabel":"businessName","charsOnly":"true"}'>
					</div>
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
				<div class="form-row" style="margin-bottom:30px;margin-left:50px;">
									<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_INVOICE_NAME)%></div>
									<div class="input-field" ><input class="mandatory" name="invoiceName" id="invoiceName"></div>
									<span id="invoiceNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
									<div id="iname_pop" class="helppop" style="display: block;margin-top:-5px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_INVOICE_NAME) %></p></div>
            </div>
            <div id="inamelen_pop" class="helppop" style="display: block;margin-top:-5px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.INVOICE_NAME_VALIDATION) %></p></div>
            </div>
			</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height:40px;">
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
								<p><%=Msg.get(MsgEnum.VALIDATE_CUSTOMER_NAME) %></p>
							</div>
						</div>
						<div id="cnamelen_pop" class="helppop" style="display: block;margin-top:-10px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;margin-top:2px;">
								<p><%=Msg.get(MsgEnum.CUSTOMER_NAME_VALIDATION) %></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label" ><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_LABLE)%></div>
											<div class="input-field" style="width: 43px;">
												<input id="male" type="radio" name="gender" class="gender " checked="checked" value="M" style="width:20px;" /><span class="male"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_MALE_LABLE)%></span>
											</div>
											<div class="input-field" style="width: 43px;">
												<input id="female" type="radio" name="gender" class="gender " value="F" style="width:20px;"/><span class="female"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_FEMALE_LABLE)%></span>
											</div>
										</div>
				</div>
			</div>
			 <div class="separator" style="height:60px; margin-left:40px;"></div>
			<div class="fieldset-row" style="margin-top:20px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_MOBILE)%></div>
						<div class="input-field">
							<input class="mandatory constrained" 
							constraints='{"fieldLabel":"mobile","numbersOnly":"true"}'
							name="mobile" id="mobile"> <span id="mobileValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="mobile_pop" class="helppop" style="display: block;float:left;margin-left:140px;margin-top:-25px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"style="float: left; margin-left: 3px;margin-top:2px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_MOBILE) %></p>
						</div>
					</div>
					<div id="mobilelen_pop" class="helppop" style="display: block;float:left;margin-left:140px;margin-top:-25px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"style="float: left; margin-left: 3px;margin-top:2px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_MOBILE_NUMBER) %></p>
						</div>
					</div>
						</div>
					</div>
				</div>
			<div class="fieldset" style="height:60px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_EMAIL)%></div>
					<div class="input-field">
						<input  name="email" id="Email"
							constraints='{"fieldLabel":"Email Id","email":"true"}'> <span
							id="emailValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -25px"></span>
					<div id="email_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:2px;"">
							<p><%=Msg.get(MsgEnum.VALIDATE_EMAIL) %></p>
						</div>
					</div>
					<div id="emaillen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:2px;"">
							<p><%=Msg.get(MsgEnum.VALIDATE_MAIL) %></p>
						</div>
					</div>
				</div>
			</div> 
			</div>
			<div class="separator" style="height:10px;"></div>
			<div class="fieldset" style="height: 20px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CREDIT_LIMIT)%>
					</div>
					<div class="input-field">
						<input 
							constraints='{"fieldLabel":"creditLimit","numbersOnly":"true"}'
							name="creditLimit" id="creditLimit"> <span
							id="creditLimitValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="creditlimit_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						    aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:2px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CREDIT_LIMIT) %></p>
						</div>
					</div>
					<div id="creditlimitlen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:2px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CREDIT_LIMIT_CHAR) %></p>
						</div>
					</div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 80px; width:60px;"></div>
			<div class="fieldset" style="height: 20px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CREDIT_OVERDUE_DAYS)%>
					</div>
					<div class="input-field">
						<input 	name="creditOverdueDays" id="creditOverdueDays"> <span
							id="overduesValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="overdues_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:-5px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CREDIT_OVER_DUE_DAYS) %></p>
						</div>
					</div>
					<div id="overdueslen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;margin-top:-5px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CRESIT_OVER_DUE_DAYS_CHAR) %></p>
						</div>
					</div>
					</div>
				</div>
			</div>
		<div id="page-buttons" class="page-buttons">
			<input name="action" value="save-basic" type="hidden"
				id="customerAction">
		</div>
		<div class="fieldset-row"></div>
	</form>
	<form id="customers-detail-form" style="height: 280px; display: none;">
		<div class="add-student-tabs">
			<div class="step-no-select">
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_BASIC_INFO_LABEL)%>
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

		<div class="fieldset-row" style="margin-top: 10px;">
			<div class="fieldset" style="height: 10px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_DIRECT_LINE)%></div>
					<div class="input-field">
						<input name="directLine" id="directLine" 
							constraints='{"fieldLabel":"creditOverDueDays","numbersOnly":"true"}'>
						<span id="directLineValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								<div id="dline_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:-5px;"><p><%=Msg.get(MsgEnum.VALIDATE_DIRECT_LINE) %></p></div>
                </div>
                <div id="dlinelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:-5px;"><p><%=Msg.get(MsgEnum.VALIDATE_DIRECT_LINE_CHAR) %></p></div>
                </div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 60px;"></div>
			<div class="fieldset" style="height: 10px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ALTERNATE_MOBILE)%>
					</div>
					<div class="input-field">
						<input name="alternateMobile" id="alternateMobile" >
						<span id="altmobileValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="alternate_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ALTERNATE_MOBILE) %></p></div>
            </div>
            <div id="alternatelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ALTERNATE_MOBILE_CHAR) %></p></div>
            </div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 60px;"></div>
			<div class="fieldset" style="height: 10px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ADDRESS_LINE_1)%></div>
					<div class="input-field">
						<input name="addressLine1" id="addressLine1"
							maxlength="45"> <span id="addressLine1Valid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="addressLine1_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ADDRESS1) %></p></div>
            </div>
            <div id="addressLine1len_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ADDRESS1_CHAR) %></p></div>
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
							<div id="addressLine2_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ADDRESS2) %></p></div>
            </div>
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
								<div id="locality_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_LOCALITY) %></p></div>
            </div> 
            <div id="localitylen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_LOCALITY_CHAR) %></p></div>
            </div> 
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_LANDMARK)%></div>
					<div class="input-field">
						<input name="landmark" id="landmark"> <span
							id="landmarkValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="landmark_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_LANDMARK) %></p></div>
            </div>
            <div id="landmarklen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_LANDMARK_CHAR) %></p></div>
            </div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 80px;"></div>
			<div class="fieldset" style="height: 60px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_REGION)%>
					</div>
					<div class="input-field">
						<input class="mandatory constrained" name="region" id="region"
							maxlength="45"
							constraints='{"fieldLabel":"region","charsOnly":"true"}'>
						<span id="regionValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							 <div id="region_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_REGION) %></p></div>
					</div>
					 <div id="regionlen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_REGION_CHAR) %></p></div>
					</div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_STATE)%>
					</div>
					<div class="input-field">
						<input  name="state" id="state"
							
							constraints='{"fieldLabel":"state","charsOnly":"true"}'>
						<span id="stateValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							 <div id="state_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_STATE) %></p></div>
            </div>
            <div id="statelen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;"  aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_STATE_CHAR) %></p></div>
            </div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_CITY)%>
					</div>
					<div class="input-field">
						<input  name="city" id="city"
							maxlength="45" constraints='{"fieldLabel":"city","charsOnly":"true"}'> <span
							id="cityValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
							<div id="city_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_CITY) %></p></div>
            </div>
            <div id="citylen_pop" class="helppop" style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_CITY_CHARS) %></p></div>
            </div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_PINCODE)%>
					</div>
					<div class="input-field">
						<input 
							name="zipcode"
							id="zipcode"> <span id="zipcodeValid"
							style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								 <div id="pincode_pop" class="helppop"style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ZIPCODE) %></p></div>
            </div>
             <div id="pincodelen_pop" class="helppop"style="display: block;margin-top:-25px;float:left;margin-left:140px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.VALIDATE_ZIPCODE_CHARS ) %></p></div>
            </div>
					</div>
				</div>
				<div class="form-row">
					<div class="input-field">
						<input name="action" value="save-detail" type="hidden"
							id="customerAction">
					</div>
				</div>
			</div>
		</div>
	</form>
	<div id="customer-preview-container" style="display: none;"></div>
	<div id="page-buttons" class="page-buttons"
		style="margin-left: 200px; margin-top:10px;">
		<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
		<div id="button-next" class="ui-btn btn-next"></div>
		<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
		<div id="action-clear" class="ui-btn btn-clear"></div>
		<div id="action-cancel" class="ui-btn btn-cancel"></div>
	</div>
</div>
<script type="text/javascript">
	CustomerHandler.initAddButtons();
	CustomerHandler.initCheckBusinessName();
	CustomerHandler.initBubble();
	$(document).ready(function() {
		$('.helppop').hide();
	});
</script>
