<%@page import="com.vekomy.vbooks.siteadmin.dao.ManageUserDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbLogin"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>

<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ListIterator"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	VbEmployee vbEmployee = null;
	VbEmployeeDetail vbEmployeeDetail = null;
	VbEmployeeAddress vbEmployeeAddress =null;
	 List<String> organizationsList = new ArrayList<String>();
		VbEmployeeAddress employeeAddress = null;
		VbOrganization vbOrganization = null;
		String organizationName = null;
	Object[] subjectsList = null;
	int id = Integer.parseInt(request.getParameter("id"));
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ManageUserDao manageUserDao = (ManageUserDao) hibernateContext
				.getBean("manageUserDao");
		if (manageUserDao != null) {
			vbEmployee = manageUserDao.getUser(id);
			Iterator iterator = vbEmployee.getVbEmployeeDetails()
					.iterator();

			if (iterator.hasNext()) {
				vbEmployeeDetail = (VbEmployeeDetail) iterator.next();
			}
			Iterator addressIterator = vbEmployee
					.getVbEmployeeAddresses().iterator();
			if (addressIterator.hasNext()) {
				vbEmployeeAddress = (VbEmployeeAddress) addressIterator
						.next();
			}
			if (vbEmployeeDetail == null) {
				vbEmployeeDetail = new VbEmployeeDetail();
			}
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="employee-add-form-container" title="Edit User">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-add-icon"></div>
			<div class="page-title employee-add-title"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
	<form name="manage-user-form" id="manage-user-form" style="height: 300px;">
			<div class="add-student-tabs">
				<div class="step-selected">
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%>
					</div>
				</div>
				<div class="step-no-select">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%>
					</div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<%-- <input type="hidden" name="id" value="<%=vbEmployee.getId()%>"> --%>
			
			<div class="fieldset-row" style="height: 350px; margin-top: 5px;">
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_FULL_NAME_LABEL)%></div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployee.getFirstName()) %> <%=StringUtil.format(vbEmployee.getLastName()) %>" class="mandatory " name="fullName" id="fullName">
						</div>
						<span id="fnameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="fname_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_FIRST_NAME) %></p>
							</div>
						</div>
						<div id="dbfname_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_FIRST_NAME) %></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_MOBILE)%></div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeDetail.getMobile()) %>" class="mandatory" name="mobile" id="mobile">
						</div>
						<span id="mobileValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="mobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE) %></p>
							</div>
						</div>
						<div id="dbmobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE) %></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ALTERNATE_MOBILE)%></div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeDetail.getAlternateMobile()) %>" name="alternateMobile" id="alternateMobile">
						</div>
						<span id="alternateMobileValid"	style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="mobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE) %></p>
							</div>
						</div>
						<div id="dbmobile_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE) %></p>
							</div>
						</div>
					</div>
				</div>
					<div class="separator" style="height: 120px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_DIRECT_LINE)%></div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeDetail.getDirectLine()) %>" class="mandatory" name="directLine" id="directLine">
						</div>
						<span id="dlineValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dline_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Direct Line can contains numbers, spaces, hyphen(-) and
									curly braces(()).</p>
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
							<input  value="<%=StringUtil.format(vbEmployee.getEmployeeEmail()) %>" class="mandatory" name="employeeEmail" id="employeeEmail">
						</div>
						<span id="emailValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="email_pop" class="helppop" style="display: block;" saria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_EMAIL) %></p>
							</div>
						</div>
						<div id="dbemail_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_EMAIL) %></p>
							</div>
						</div>
					</div>
				</div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_BLOOD_GROUP_LABEL)%>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeDetail.getBloodGroup()) %>" class="mandatory" name="bloodGroup" id="bloodGroup">
						</div>
						<span id="bgroupValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="bgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_BLOOD_GROUP) %></p>
							</div>
						</div>
						<div id="dbbgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_BLOOD_GROUP) %></p>
							</div>
						</div>
					</div>
					<div class="form-row" style="height: 40px;">
						<%if(vbEmployee.getGender().equals('M'))
											{
						%><div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_LABLE)%></div>
											
												<div class="input-field" style="width: 43px;">
												<input id="male" type="radio" name="gender" class="gender " checked="checked" value="M" style="width:20px;" /><span class="male"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_MALE_LABLE)%></span>
											</div>
											<div class="input-field" style="width: 43px;">
												<input id="female" type="radio" name="gender" class="gender " value="F" style="width:20px;"/><span class="female"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_FEMALE_LABLE)%></span>
											</div>
											<%
											}
											%>
											<%if(vbEmployee.getGender().equals('F'))
											{
												 %>
												 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_LABLE)%></div>
												<div class="input-field" style="width: 43px;">
												<input id="male" type="radio" name="gender" class="gender "  value="M" style="width:20px;" /><span class="male"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_MALE_LABLE)%></span>
											</div>
											<div class="input-field" style="width: 43px;">
												<input id="female" type="radio" name="gender" class="gender " checked="checked" value="F" style="width:20px;"/><span class="female"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_FEMALE_LABLE)%></span>
											</div>
											<%
											}
											%>
					</div>
				</div>
				<div class="separator" style="height: 100px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PASSPORT_NUMBER)%>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeDetail.getPassportNumber()) %>" class="mandatory" name="passportNumber"
								id="passPortNumber">
						</div>
						<span id="passportValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="passport_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_PASSPORT_NUMBER) %></p>
							</div>
						</div>
						<div id="dbpassport_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_PASSPORT_NUMBER) %></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_NATIONALITY_LABEL)%></div>
						<div class="input-field">
							<input value="<%=vbEmployeeDetail.getNationality() %>" class="mandatory" name="nationality" id="nationality">
						</div>
						<span id="nationValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="nation_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_NATIONALITY) %></p>
							</div>
						</div>
						<div id="dbnation_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_NATIONALITY) %></p>
							</div>
						</div>
					</div>
					<div id="page-buttons" class="page-buttons">
						<input name="action" value="manage-user-basic" type="hidden" id="manageUserAction">
					</div>
				</div>
				
					<div class="fieldset" style="height: 50px;">
					<div class="form-row" style="display: none">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%>
						</div>
						<div class="input-field" id="uName">
							<input value="<%=vbEmployee.getUsername() %>" class="mandatory constrained" name="username"	id="username"
								constraints='{"fieldLabel":"userName","userNameValidate":"true"}'>
						</div>
						<span id="uValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<span id="unValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="username_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_USERNAME) %></p>
							</div>
						</div>
					</div>
				</div>
			<div class="fieldset" style="height: 50px;">
					<div class="form-row" style="display: none">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PASSWORD)%>
						</div>
						<div class="input-field" id="pName">
							<input value="Admin@123" class="mandatory" name="password" id="password" type="password">
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
			</div>
			<input type="hidden" name="id" value="<%=id%>">
		</form>
		<form name="form2" id="manage-user-address-form" style="height: 250px; display: none;">
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
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 150px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE1_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getAddressLine1())%>" class="mandatory" name="addressLine1" id="addressLine1">
						</div>
						<span id="addressLine1Valid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="addressLine1_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Address Line1 Required.</p>
							</div>
						</div>
						<div id="dbaddressLine1_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Address Line1 can not accepts more than 200 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE2_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getAddressLine2())%>" name="addressLine2" id="addressLine2">
						</div>
						<span id="addressLine2Valid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dbaddressLine2_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Address Line2 can not accepts more than 200 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LOCALITY_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getLocality())%>" class="mandatory" name="locality" id="locality">
						</div>
						<span id="localityValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="locality_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Locality can contains letters, numbers and spaces only.</p>
							</div>
						</div>
						<div id="dblocality_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Locality can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LAND_MARK_LABEL) %></div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getLandmark())%>" name="landmark" id="landmark">
						</div>
						<span id="landmarkValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="landmark_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Landmark can contains letters, numbers, spaces,
									dashes(-), periods(.), commas(,), hash(#), slash(/) only.</p>
							</div>
						</div>
						<div id="dblandmark_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Landmark can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_LABEL)%></div>
						<div class="input-field">
							<select multiple="multiple" name="organizations" style="width:200px;" id="organizationType" class="mandatory constrained" 
							constraints='{"fieldLabel":"Organization Type","mustSelect":"true"}'>
							</select>
						</div>
						<span id="organizationTypeValid" style="float: left; position: absolute; margin-left: 60px; margin-top: 5px"></span>
						<div id="organizationType_pop" class="helppop" style="display: block; margin-left: 340px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_ORGANIZATION ) %></p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 150px;"></div>
				<div class="fieldset" style="height: 150px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_CITY_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getCity())%>" class="mandatory" name="city" id="city">
						</div>
						<div class="input-field">
							<input class="mandatory" name="city" id="city">
						</div>
						<span id="cityValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="city_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>City accepts alphabetics only.</p>
							</div>
						</div>
						<div id="dbcity_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>City can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_STATE_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getState())%>"	class="mandatory" name="state" id="state">
						</div>
						<span id="stateValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="state_pop" class="helppop" style="display: block;"	aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>State accepts alphabetics only.</p>
							</div>
						</div>
						<div id="dbstate_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>State can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PIN_CODE_LABEL) %>
						</div>
						<div class="input-field">
							<input value="<%=StringUtil.format(vbEmployeeAddress.getZipcode())%>" class="mandatory" name="zipcode" id="zipcode">
						</div>
						<span id="pincodeValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="pincode_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Zipcode accepts numbers,letters, dashes(-),spaces only and should not exceed 9 digits.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_TYPE_LABEL) %>
						</div>
						<div class="input-field">
							<select class="mandatory" name="addressType" id="addressType">
								<option value="<%=vbEmployeeAddress.getAddressType()%>"><%=DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE).get(vbEmployeeAddress.getAddressType())%></option>
												<%													
													for(String addressType: DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE).keySet()) {
														if(addressType.equalsIgnoreCase(vbEmployeeAddress.getAddressType()))
															continue;
													%>
													
												<option value="<%=addressType %>"><%=DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE, addressType) %></option>
												<%} %>
							</select>
						</div>
						<span id="addressTypeValid"	style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="addressType_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Please Select Address Type.</p>
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
		<div id="page-buttons" class="page-buttons" style="margin-left: 200px; margin-top:10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next"></div>
			<div id="button-update-employee" class="ui-btn btn-update"
				style="display: none;"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>
	
</div>
<script type="text/javascript">
$(document).ready(function() {
	$('.helppop').hide();
	ManageUserHandler.initAddButtons();
});
</script>

								