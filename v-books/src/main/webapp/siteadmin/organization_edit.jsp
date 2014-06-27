<%@page import="com.vekomy.vbooks.siteadmin.dao.SiteAdminDao"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="java.util.List"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%
	String mode = request.getParameter("mode");
	boolean edit = true;
	VbOrganization mainBranchName = null;
	String superUser = "";
	if (mode == null || mode.equals("")) {
		edit = false;
	}
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		OrganizationDao organizationDao = (OrganizationDao) hibernateContext.getBean("organizationDao");
		int id = Integer.parseInt((String) request.getParameter("id"));
		request.setAttribute("Id", id);
		SiteAdminDao siteadminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
	} catch (Exception e) {

	}
%>
<link rel="stylesheet" href="css/site.css" type="text/css" />
<link rel="stylesheet" href="css/default/default.css" type="text/css" />
<div id="organization-create-form-container">
	<div class="ui-content form-panel full-content">
		<form id="organization-form" style="height: 360px;">
			<div class="add-student-tabs">
				<div class="step-selected">
					<div class="tabs-title" style="padding-left: 15px;"><%=Msg.get(MsgEnum.ORGANIZATION_TAB)%>
					</div>
				</div>
				<div class="step-no-select" style="width: auto;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.ORGANIZATION_SUPER_USER)%>
					</div>
				</div>
				<div class="step-no-select">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%>
					</div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="height: 280px; margin-top: -5px;">
				<div class="fieldset" style="height: 30px;">
							<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_MAIN_BRANCH_LABEL)%></div>
						<div class="input-field">
							<select class="mandatory" name="mainBranch" id="mainBranch">
								<option value="Y" selected="selected">Yes</option>
								<option value="N">No</option>
							</select>
						</div>
					</div>
					<div class="form-row">
						<div class="label" id='mainBranchLabel'><%=Msg.get(MsgEnum.ORGANIZATION_MAIN_BRANCH_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="mainBranchName"
								id="mainBranchName"  readonly="readonly">
							
						</div>
						<div id="branchMainPop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Please select a Main Branch</p>
							</div>
						</div>
					</div>
					<div id="auto-suggestions" class="auto-suggestions"></div>
				</div>
				<div class="separator" style="height: 20px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ORGANIZATION_CODE_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="organizationCode" id="organizationCode">
						</div>
						<span id="organizationCodeValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="namehelp_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Organization Code can contain letters, numbers, at the
									rate(@), hash(#), curlybraces(()), dashes(-), Underscore(_),
									slash(/) and spaces.</p>
							</div>
						</div>
						<div id="codedb_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Organization Code can not exceed 30 characters.</p>
							</div>
						</div>
						<div id="code_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Organization Code is not available.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ORGANIZATION_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="name" id="name">
						</div>
						<span id="organizationNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="organizationName_pop" class="helppop"
							style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Organization Name can contain letters, numbers, at the
									rate(@), hash(#), curlybraces(()), dashes(-), Underscore(_),
									slash(/) and spaces.</p>
							</div>
						</div>
						<div id="dbName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Organization Name can not accepts more than 400
									characters.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_CURRENCY_FORMAT_LABEL)%></div>
						<div class="input-field">
							<input name="currencyFormat" id="currencyFormat"class="mandatory" >
						</div>
						<span id="currencyValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="currency_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Currency Format Accepts Alphabates Or Special Characters</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 30px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_BRANCH_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="branchName" id="branchName">
						</div>
						<span id="branchNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="branchName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Branch Name can contain letters, numbers, at the rate(@),
									hash(#), curlybraces(()), dashes(-), Underscore(_), slash(/)
									and spaces.</p>
							</div>
						</div>
						<div id="dbBranchName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Branch Name can not accepts more than 400 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_USERNAME_PREFIX_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="usernamePrefix" id="usernamePrefix" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ADDRESS_LINE_1_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="addressLine1" id="addressLine1">
						</div>
						<span id="addressLine1Valid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="addressLine1_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Address Line1 Required.</p>
							</div>
						</div>
						<div id="dbAddressLine1_pop" class="helppop"
							style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Address Line1 can not accepts more than 200 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ADDRESS_LINE_2_LABEL)%></div>
						<div class="input-field">
							<input name="addressLine2" id="addressLine2">
						</div>
						<span id="addressLine2Valid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dbAddressLine2_pop" class="helppop"
							style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Address Line2 can not accepts more than 200 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_LOCALITY_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="locality" id="locality">
						</div>
						<span id="localityValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="locality_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Locality can contains letters, numbers and spaces only.</p>
							</div>
						</div>
						<div id="dbLocality_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Locality can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_LANDMARK_LABEL)%></div>
						<div class="input-field">
							<input name="landmark" id="landmark">
						</div>
						<span id="landmarkValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="landmark_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Landmark can contains letters, numbers, spaces,
									dashes(-), periods(.), commas(,), hash(#), slash(/) only.</p>
							</div>
						</div>
						<div id="dbLandmark_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Landmark can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_CITY_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="city" id="city">
						</div>
						<span id="cityValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="city_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>City accepts alphabetics, spaces( ) and periods(.) only.</p>
							</div>
						</div>
						<div id="dbCity_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>City can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_STATE_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="state" id="state">
						</div>
						<span id="stateValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="state_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>State accepts alphabetics, spaces( ) and periods(.) only.</p>
							</div>
						</div>
						<div id="dbState_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>State can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_COUNTRY_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="country" id="country">
						</div>
						<span id="countryValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="country_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Country accepts alphabetics, spaces( ) and periods(.)
									only.</p>
							</div>
						</div>
						<div id="dbCountry_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Country can not accepts more than 50 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ZIPCODE_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="zipcode" id="zipcode">
						</div>
						<span id="pincodeValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="pincode_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Zipcode accepts numbers,letters, dashes(-),spaces only
									and should not exceed 9 digits.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 130px; margin-left: 100px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_PHONE1_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="phone1" id="phone1">
						</div>
						<span id="phone1Valid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="phone1_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Phone 1 can contains numbers, spaces, hyphen(-) and curly
									braces(()) and should not exceed more than twenty characters.</p>
							</div>
						</div>
						<div id="dbPhone1_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Phone 1 can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_PHONE2_LABEL)%></div>
						<div class="input-field">
							<input name="phone2" id="phone2">
						</div>
						<span id="phone2Valid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="phone2_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Phone 2 can contains numbers, spaces, hyphen(-) and curly
									braces(()) and should not exceed more than twenty characters.</p>
							</div>
						</div>
						<div id="dbPhone2_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Phone 2 can not accepts more than 60 characters.</p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label" style="margin-left: 2px;"><%=Msg.get(MsgEnum.ORGANIZATION_DESCRIPTION_LABEL)%></div>
						<div class="input-field">
							<textarea name="description" id="description" rows="3" cols="76"
								style="margin-left: 10px;"></textarea>
						</div>
						<span id="descriptionValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="dbdescription_pop" class="helppop"
							style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Description can not accepts morethan 200 characters.</p>
							</div>
						</div>
					</div>
				</div>
				<input name="action" value="save-organization" type="hidden"
					id="organizationAction">
			</div>
		</form>

		<form id="organization-superuser-form"
			style="display: none; height: 340px;">
			<div class="add-student-tabs">
				<div class="step-no-select">
					<div class="tabs-title"><%=Msg.get(MsgEnum.ORGANIZATION_TAB)%>
					</div>
				</div>
				<div class="step-selected" style="width: auto;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.ORGANIZATION_SUPER_USER)%></div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="height: 40px; margin-top: -5px;">
				<div class="fieldset" style="height: 20px;">
					<div class="form-row" style="display: none">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_PASSWORD_LABEL)%></div>
						<div class="input-field" id="pName">
							<input class="mandatory constrained"
								constraints='{"fieldLabel":"Password","choose":"true"}'
								name="password" id="password" type="password" value="Admin@123">
						</div>
						<span id="pValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<span id="pwValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="password_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>password should be 8, must contains atleast one
									alphanumeric, capital, small & special characters.</p>
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_FULL_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="fullName" id="fullName">
						</div>
						<span id="fillNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="fullName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Full Name can contain letters, numbers, periods (.) and
									at the rate (@) and spaces.</p>
							</div>
						</div>
						<div id="dbfname_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Full Name can not accepts more than 100 characters.</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px; margin-left: 100px"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_SUPER_USER_NAME_LABEL)%></div>
						<div class="input-field" id="uName">
							<input class="read-only" name="superUsername" readonly="readonly" id="username" >
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="height: 80px; margin-top: -5px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_MOBILE)%></div>
						<div class="input-field">
							<input class="mandatory" name="mobile" id="mobile">
						</div>
						<span id="mobileValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="mobile_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE)%></p>
							</div>
						</div>
						<div id="dbmobile_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE)%></p>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ALTERNATE_MOBILE)%></div>
						<div class="input-field">
							<input name="alternateMobile" id="alternateMobile">
						</div>
						<span id="alternateMobileValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="mobile_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_MOBILE)%></p>
							</div>
						</div>
						<div id="dbmobile_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.MANAGE_USER_DB_MOBILE)%></p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px; margin-left: 100px"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_EMAIL_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="email" id="email">
						</div>
						<span id="emailValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="email_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Please enter a valid email address Eg: john@xyz.com.</p>
							</div>
						</div>
						<div id="dbEmail_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p>Email can not accepts more than 100 characters.</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input name="id"  type="hidden" id="organizationId">
			 <input name="action"value="save-edit-super-organization" type="hidden" id="organizationAction">
		</form>


		<div id="organization-user-preview-container" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 60px;">

			<button id="button-prev" class="ui-btn btn-prev"
				style="display: none">Previous</button>
			<button id="action-update" class="ui-btn btn-update"
				style="display: none;">Update</button>
			<button id="button-next" class="ui-btn btn-next">Next</button>
			<button id="action-clear" class="ui-btn btn-clear">Clear</button>
			<button id="action-cancel" class="ui-btn btn-cancel">Cancel</button>


		</div>
	</div>
</div>
<div id="organization-code-suggestions"
	style="display: none; position: absolute;"
	class="timetable-teacher-suggestions">
	<img id="exposeMask_loading_img" src="images/common/ajax-loader.gif"
		alt="loading.."
		style="z-index: 999; position: absolute; top: 43px; left: 390px;">
</div>

<script type="text/javascript">
	var orgId= "<%=request.getAttribute("Id")%>"
	$(document).ready(function() {
		OrganizationHandler.initOrganizationCreate();
		//	UserHandler.initCheckUsername();
		UserHandler.initCheckPassword();
		OrganizationHandler.initCheckOrgCode(orgId);
		OrganizationHandler.initAdd();
		$('#mainBranch').change(function() {
			if ($(this).val() == 'Y') {
				$('#mainBranchName').val('');
				$('#branchMainPop').hide()
				return;
			} else {
				return;
			}
		});
		$('.helppop').hide();
		$(document).click(function(e) {
			$('.auto-suggestions').hide()
		});
		/* $('#mainBranchName').hide();
		$('#mainBranchLabel').hide(); */
	});
	OrganizationHandler.initOrgNames(orgId);
</script>
