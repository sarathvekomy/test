<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<div id="manage-user-add-form-container" title="Create User">
	<div class="ui-content form-panel full-content">
		<form name="manage-user-form" id="manage-user-form"
			style="height: 300px;">
			<div class="add-student-tabs">
				<div class="step-selected">
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div>
				</div>
				<div class="step-no-select">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="height: 350px; margin-top: 5px;">
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%>
						</div>
						<div class="input-field" id="uName">
							<input class="mandatory constrained" name="username" id="username" constraints='{"fieldLabel":"userName","userNameValidate":"true"}'/>
						</div>
						<span id="uValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<span id="unValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="username_pop" class="helppop" style="display: block;" aria-hidden="false"> 
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_USERNAME)%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 30px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PASSWORD)%>
						</div>
						<div class="input-field" id="pName">
							<input class="mandatory" name="password" id="password" type="password"/>
						</div>
						<span id="pValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<span id="pwValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="password_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_PWD)%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="fieldset" style="height: 50px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_FULL_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory " name="fullName" id="fullName"/>
						</div>
						<span id="fnameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="fname_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_FIRST_NAME)%></p>
							</div>
						</div>
						<div id="dbfname_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_FIRST_NAME)%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="fieldset" style="height: 50px;">
					<div class="label"></div>
					<div class="input-field"></div>
				</div>
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_MOBILE)%></div>
						<div class="input-field">
							<input class="mandatory" name="mobile" id="mobile"/>
						</div>
						<span id="mobileValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="mobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE)%></p>
							</div>
						</div>
						<div id="dbmobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ALTERNATE_MOBILE)%></div>
						<div class="input-field">
							<input name="alternateMobile" id="alternateMobile"/>
						</div>
						<span id="alternateValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="alternate_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE)%></p>
							</div>
						</div>
						<div id="dbalternate_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE)%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 80px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_DIRECT_LINE)%></div>
						<div class="input-field">
							<input class="mandatory" name="directLine" id="directLine"/>
						</div>
						<span id="dlineValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dline_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Direct Line can contains numbers, spaces, hyphen(-) and curly braces(()).</p>
							</div>
						</div>
						<div id="dbdline_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Direct Line can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_EID_LABLE)%></div>
						<div class="input-field">
							<input class="mandatory" name="employeeEmail" id="employeeEmail"/>
						</div>
						<span id="emailValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="email_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_EMAIL)%></p>
							</div>
						</div>
						<div id="dbemail_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_EMAIL)%></p>
							</div>
						</div>
					</div>
				</div>

				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_BLOOD_GROUP_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="bloodGroup" id="bloodGroup"/>
						</div>
						<span id="bgroupValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="bgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_BLOOD_GROUP)%></p>
							</div>
						</div>
						<div id="dbbgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_BLOOD_GROUP)%></p>
							</div>
						</div>
					</div>
					<div class="form-row" style="height: 40px;">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_LABLE)%></div>
						<div class="input-field" style="width: 43px;">
							<input id="male" type="radio" name="gender" class="gender " checked="checked" value="M" style="width: 20px; border: 0 !important;" />
							<span class="male"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_MALE_LABLE)%></span>
						</div>
						<div class="input-field" style="width: 43px;">
							<input id="female" type="radio" name="gender" class="gender " value="F" style="width: 20px; border: 0 !important;" />
							<span class="female"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_FEMALE_LABLE)%></span>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PASSPORT_NUMBER)%></div>
						<div class="input-field">
							<input class="mandatory" name="passportNumber" id="passPortNumber"/>
						</div>
						<span id="passportValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="passport_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_PASSPORT_NUMBER)%></p>
							</div>
						</div>
						<div id="dbpassport_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_PASSPORT_NUMBER)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_NATIONALITY_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="nationality" id="nationality"/>
						</div>
						<span id="nationValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="nation_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_NATIONALITY)%></p>
							</div>
						</div>
						<div id="dbnation_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_NATIONALITY)%></p>
							</div>
						</div>
					</div>
					<div id="page-buttons" class="page-buttons">
						<input name="action" value="manage-user-basic" type="hidden" id="manageUserAction">
					</div>
				</div>
			</div>
		</form>
		<form id="manage-user-address-form"
			style="height: 280px; display: none;">
			<div class="add-student-tabs">
				<div class="step-no-select">
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-selected">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="height: 350px; margin-top: 5px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE1_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="addressLine1" id="addressLine1"/>
						</div>
						<span id="addressLine1Valid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="addressLine1_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ADDRESS_LINE1)%></p>
							</div>
						</div>
						<div id="dbaddressLine1_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_ADDRESS_LINE1)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE2_LABEL)%>
						</div>
						<div class="input-field">
							<input name="addressLine2" id="addressLine2"/>
						</div>
						<span id="addressLine2Valid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dbaddressLine2_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ADDRESS_LINE2)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LOCALITY_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="locality" id="locality"/>
						</div>
						<span id="localityValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="locality_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_LOCALITY)%></p>
							</div>
						</div>
						<div id="dblocality_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_LOCALITY)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LAND_MARK_LABEL)%></div>
						<div class="input-field">
							<input name="landmark" id="landmark"/>
						</div>
						<span id="landmarkValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="landmark_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_LAND_MARK)%></p>
							</div>
						</div>
						<div id="dblandmark_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_LANDMARK)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_LABEL)%></div>
						<div class="input-field">
							<select multiple="multiple" name="organizations" style="width: 200px;" id="organizationType"
								class="mandatory constrained" constraints='{"fieldLabel":"Organization Type","mustSelect":"true"}'>
							</select>
						</div>
						<span id="organizationTypeValid"
							style="float: left; position: absolute; margin-left: 60px; margin-top: 5px"></span>
						<div id="organizationType_pop" class="helppop"
							style="display: block; margin-left: 340px;" aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ORGANIZATION )%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_CITY_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="city" id="city">
						</div>
						<span id="cityValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="city_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_CITY)%></p>
							</div>
						</div>
						<div id="dbcity_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_CITY)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_STATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="state" id="state">
						</div>
						<span id="stateValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="state_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_STATE)%></p>
							</div>
						</div>
						<div id="dbstate_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_STATE)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PIN_CODE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="zipcode" id="zipcode">
						</div>
						<span id="pincodeValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="pincode_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ZIPCODE)%></p>
							</div>
						</div>
					</div>
					<div class="form-row" style="height: 40px;">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select class="mandatory" name="addressType" id="addressType">
								<option value="-1">Select</option>
								<%
									for(String addressType: DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE).keySet()) {
								%>
								<option value="<%=addressType%>"><%=DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE, addressType)%></option>
								<%
									}
								%>
							</select>
						</div>
						<span id="addressTypeValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="addressType_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ADDRESS_TYPE)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="manage-user-address" type="hidden"
								id="manageUserAction">
						</div>
					</div>
				</div>
			</div>
		</form>
		<div id="manage-user-preview-container" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 225px; margin-top: 60px;" align="center";>
			<button id="button-prev" class="ui-btn btn-prev"
				style="display: none">Previous</button>
			<button id="button-next" class="ui-btn btn-next">Next</button>
			<button id="button-save" class="ui-btn btn-save"
				style="display: none;">Save</button>
			<button id="action-clear" class="ui-btn btn-clear">Clear</button>
			<button id="action-cancel" class="ui-btn btn-cancel">Cancel</button>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('.helppop').hide();
		UserHandler.initValidateUserName();
		ManageUserHandler.getOrganizations();
		ManageUserHandler.initAddButtons();
	});
</script>
