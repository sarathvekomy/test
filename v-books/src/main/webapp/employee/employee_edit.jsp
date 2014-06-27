<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbLogin"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ListIterator"%>
<div id="employee-add-form-container" title="Create Employee">
						<!--<div class="green-title-bar">
							<div class="green-title-bar2">						
								<div class="page-icon employee-add-icon"></div>
								<div class="page-title employee-add-title"></div>
							</div>
						</div>-->
						<div class="ui-content form-panel full-content">	
										
						<form id="employee-form" style="height: 335px;">
							<div class="add-student-tabs">
								<div class="step-selected"><div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%> </div></div>
								<div class="step-no-select"><div class="step-selected-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDITIONAL_INFO_LABEL)%> </div></div>
								<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%> </div></div>
								<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%> </div></div>
								<div class="step-no-select-corner"></div>
							</div>
						<input type="hidden" name="id"id="id">
							<div class="fieldset-row" style="margin-top: 10px;">
								<div class="form-row" style="margin-bottom:30px;">
									<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%> </div>
									<div class="input-field" id="uName"><input class="remove-inputfield-style mandatory"  readonly="readonly" class="mandatory constrained" constraints='{"fieldLabel":"Username","maxSize":"30"}' name="username" id="userName" ></div>
									
								</div>
								<div class="form-row" style="margin-bottom:30px; margin-left: 10px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_EMPLOYEE_NO_LABEL)%></div>
										 <div class="input-field"><input  class="remove-inputfield-style mandatory" style =" background-color:inherit;border :0 none ;font-weight:bold;" readonly="readonly"  class="mandatory constrained" constraints='{"fieldLabel":"Employee Number","maxSize":"10","numbersOnly":"true"}' name="employeeNumber" id="employeeNumber" ></div>
									</div>
								</div>
							</div>
							<div class="fieldset-row" style="margin-top: 10px;">
								<div class="fieldset" style="height: 120px;">
									<div class="form-row">
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_FIRST_NAME_LABEL)%></div>
										 <div class="input-field"><input  class="mandatory" name="firstName" id="firstName" ></div>
										  <span id="fnameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="fname_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>First Name accepts alphabetics only.</p>
                								</div>
           									</div>
           									<div id="dbfname_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>First Name can not accepts more than 35 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_MIDDLE_NAME_LABEL)%> </div>
										 <div class="input-field"><input  name="middleName" id="middleName" ></div>
										 <span id="mnameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="mname_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Middle Name can contains letters, numbers, periods (.) and at the rate (@).</p>
                								</div>
            								</div>
            								<div id="dbmname_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Middle Name can not accepts more than 35 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LAST_NAME_LABEL)%></div>
										 <div class="input-field"><input  class="mandatory" name="lastName" id="lastName" ></div>
										 <span id="lnameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="lname_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Last Name accepts alphabetics only.</p>
                								</div>
            								</div>
            								<div id="dblname_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Last Name can not accepts more than 35 characters.</p>
												</div>
											</div>
									</div>
									<!-- Adding one more column to enter SE granted_days to display his view_transaction -->
					
					<div class="form-row" id='salesExecutive_granted_days'>
						<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_GRANTED_DAYS_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="grantedDays" id="grantedDays">
						</div>
						<span id="txnViewDayValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="txnViewDayValid_pop" class="helppop" style="display: block;"	aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Granted Days accepts positive Numbers only.</p>
							</div>
						</div>
						<div id="dbtxnViewDayValid_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Granted Days can not accepts more than 3 digits.</p>
							</div>
						</div>
						<div id="dbtxnMaxMinDays_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Granted Days should not exceed from 365 days and not less than 1 day.</p>
							</div>
						</div>
					</div>
					<!-- ------------- -->
								    <div>
								    </div>
								</div>
								<div class="separator" style="height: 120px;"></div>								
								<div class="fieldset" style="height: 120px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_EID_LABLE)%></div>
										 <div class="input-field"><input class="mandatory constrained" constraints='{"fieldLabel":"Email","email":"true"}' name="employeeEmail" id="employeeEmail"  class="constrained" constraints='{"fieldLabel":"Employee","email":"true"}'></div>
										  <span id="emailValid"  style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											 <div id="email_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Please enter a valid email address Eg: john@xyz.com.</p>
                								</div>
            								</div>
            								<div id="dbemail_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Email can not accepts more than 100 characters.</p>
												</div>
											</div>
									</div>
									 <div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_EMPLOYEE_TYPE_LABEL)%></div>
										<div class="input-field"> 
											<select name="employeeType" id="employeeType" class="mandatory constrained" constraints='{"fieldLabel":"Employee Type","mustSelect":"true"}'>
												<option value="-1">Select</option>
									<%
									for(String employeeType: DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE).keySet()) {
								%>
									<option value="<%=employeeType%>"><%=DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE, employeeType)%></option>
									<%
									}
								%>
											</select> 
										</div>
										 <span id="employeeTypeValid"  style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
										 <div id="employeeType_pop" class="helppop" style="display: block;" aria-hidden="false">
                							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                								<p>Please Select Employee Type.</p>
                							</div>
            							</div>
									</div> 
									<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_LABLE)%></div>
							<div class="input-field" style="width: 43px;">
								<input id="male" type="radio" name="gender" class="gender " value="M" style="width: 20px;" /><span
									class="male"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_MALE_LABLE)%></span>
							</div>
							<div class="input-field" style="width: 43px;">
								<input id="female" type="radio" name="gender" class="gender "
									value="F" style="width: 20px;" /><span class="female"><%=Msg.get(MsgEnum.EMPLOYEE_GENDER_FEMALE_LABLE)%></span>
							</div>
						</div>
									<div id="page-buttons" class="page-buttons"><input name="action" value="save-basic" type="hidden" id="employeeAction"></div>
								</div>	
							</div>
							<div class="fieldset-row">
							</div>
						</form>
						
						<form id="employee-detail-form" style="height:335px; display:none;">
							<div class="add-student-tabs">
								<div class="step-no-select"><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%> </div></div>
								<div class="step-selected"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDITIONAL_INFO_LABEL)%></div></div>
								<div class="step-no-select"><div class="step-selected-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div></div>
								<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div></div>
								<div class="step-no-select-corner"></div>
							</div>					
						
							<div class="fieldset-row" style="margin-top: 10px;">
								<div class="fieldset" style="height: 150px;">
									<div class="form-row">
										 <div class="label">Mobile Number</div>
										 <div class="input-field"><input class="mandatory constrained" constraints='{"fieldLabel":"Mobile Number","numbersOnly":"true"}' name="mobile" id="mobile" ></div>
										 <span id="mobileValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="mobile_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Mobile Number can contains numbers, spaces, hyphen(-) and curly braces(()). </p>
                								</div>
           									</div>
           									<div id="dbmobile_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr"	style="float: left; margin-left: 3px;">
													<p>Mobile Number can not accepts more than 60 characters.</p>
												</div>
											</div>
									</div> 
									<div class="form-row"> 
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_DIRECT_LINE)%></div>
										 <div class="input-field"><input class="constrained" constraints='{"fieldLabel":"Residence Phone","residence":"true"}'  name="directLine" id="directLine" ></div>
										 <span id="dlineValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="dline_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Direct Line  can contains numbers, spaces, hyphen(-) and curly braces(()).</p>
                								</div>
            								</div>
            								<div id="dbdline_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Direct Line can not accepts more than 60 characters.</p>
												</div>
											</div>
									</div>
							 	<div class="form-row"> 
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ALTERNATE_MOBILE)%></div>
										 <div class="input-field"><input  class="constrained" constraints='{"fieldLabel":"Residence Phone","residence":"true"}'  name="alternateMobile" id="alternateMobile" ></div>
										 <span id="alternateValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="alternate_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Alternate Mobile  can contains numbers, spaces, hyphen(-) and curly braces(()).</p>
                								</div>
            								</div>
            								<div id="dbalternate_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Alternate Mobile can not accepts more than 60 characters.</p>
												</div>
											</div>
									</div> 
								</div>
								<div class="separator" style="height: 150px;"></div>								
								<div class="fieldset" style="height: 150px;">
								 <div class="form-row">
										 <div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_BLOOD_GROUP_LABEL)%> </div>
										 <div class="input-field"><input class="mandatory" name="bloodGroup" id="bloodGroup" ></div>
										 <span id="bgroupValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="bgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
               	 								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
               	 									<p>Please Enter the valid Blood Group Eg:A+.</p>
               	 								</div>
            								</div>
            								<div id="dbbgroup_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Blood Group can not accepts more than 5 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PASSPORT_NUMBER)%> </div>
										<div class="input-field"><input class="mandatory" name="passportNumber" id="passPortNumber" ></div>
										<span id="passportValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="passport_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Passport Number can contains numbers and letters only.</p>
                								</div>
            								</div>
            								<div id="dbpassport_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Passport Number can not accepts more than 50 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_NATIONALITY_LABEL)%></div>
										<div class="input-field"><input  class="mandatory" name="nationality" id="nationality" ></div>
										<span id="nationValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="nation_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Nationality accepts alphabetics only.</p>
                								</div>
            								</div>
            								<div id="dbnation_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Nationality can not accepts more than 20 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										<div class="input-field"><input name="action" value="save-detail" type="hidden" id="employeeAction"></div>
									</div>
								</div>
							</div>						
						</form>	
						<form id="employee-address-form" style="height:335px; display:none;">
							<div class="add-student-tabs">
								<div class="step-no-select"><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%> </div></div>
								<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDITIONAL_INFO_LABEL)%></div></div>
								<div class="step-selected"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div></div>
								<div class="step-no-select"><div class="step-selected-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div></div>
								<div class="step-no-select-corner"></div>
							</div>
						
							<div class="fieldset-row" style="margin-top: 10px;">
								<div class="fieldset" style="height: 150px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE1_LABEL) %> </div>
										<div class="input-field"><input  class="mandatory" name="addressLine1" id="addressLine1" ></div>
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
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LANE2_LABEL) %> </div>
										<div class="input-field"><input  name="addressLine2" id="addressLine2" ></div>
										<span id="addressLine2Valid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="dbaddressLine2_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Address Line2 can not accepts more than 200 characters.</p>
												</div>
											</div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LOCALITY_LABEL) %> </div>
										<div class="input-field"><input  class="mandatory" name="locality" id="locality" ></div>
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
										<div class="input-field"><input name="landmark" id="landmark" ></div>
										<span id="landmarkValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<div id="landmark_pop" class="helppop" style="display: block;" aria-hidden="false">
                								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                									<p>Landmark can contains letters, numbers, spaces, dashes(-), periods(.), commas(,), hash(#), slash(/) only.</p>
                								</div>
            								</div>
            								<div id="dblandmark_pop" class="helppop" style="display: block;" aria-hidden="false">
												<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
													<p>Landmark can not accepts more than 60 characters.</p>
												</div>
											</div>
									</div>
								</div>
						<div class="separator" style="height: 150px;"></div>
						<div class="fieldset" style="height: 150px;">
							<div class="form-row">
								<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_CITY_LABEL) %> </div>
								<div class="input-field"><input  class="mandatory" name="city" id="city" ></div>
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
								<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_STATE_LABEL) %> </div>
								<div class="input-field"><input class="mandatory" name="state" id="state" ></div>
								<span id="stateValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
									<div id="state_pop" class="helppop" style="display: block;" aria-hidden="false">
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
								<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_PIN_CODE_LABEL) %> </div>
								<div class="input-field"><input class="mandatory"  name="zipcode" id="zipcode" ></div>
								<span id="pincodeValid"  style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
									<div id="pincode_pop" class="helppop" style="display: block;" aria-hidden="false">
                						<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
                							<p>Zipcode accepts numbers,letters, dashes(-),spaces only and should not exceed 9 digits.</p>
                						</div>
            						</div>
							</div>
							<div class="form-row">
								<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_TYPE_LABEL) %> </div>
								<div class="input-field">
							<select class="mandatory" name="addressType" id="addressTypeEdit">
							</select>
						</div>
								<span id="addressTypeValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							<div id="addressType_pop" class="helppop" style="display: block;" aria-hidden="false">
								<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
									<p>Please Select Address Type.</p>
								</div>
							</div>
							</div>
							<div class="form-row">
								<div class="input-field"><input name="action" value="save-address" type="hidden" id="employeeAction"></div>
							</div>
						</div>
					</div>						
						</form>
															
						<div id="employee-preview-container" style="display:none;"></div>
						<div id="page-buttons" class="page-buttons" style="margin-left: 200px; margin-top: 50px;" align="center">
							<div id="button-prev" class="ui-btn btn-prev" style="display: none">Previous</div>
							<div id="button-next" class="ui-btn btn-next">Next</div>
							<div id="button-update-employee" class="ui-btn btn-update" style="display: none;">Update</div>
							<div id="action-clear" class="ui-btn btn-clear">Clear</div>							
							<div id="action-cancel-edit" class="ui-btn btn-cancel">Cancel</div>
						</div>	
					</div>
					</div>
					<script type="text/javascript">
				
						EmployeeHandler.initAddButtons();
						UserHandler.initCheckUsername();
						UserHandler.initCheckPassword();
						$(document).ready(function() {
							$('.helppop').hide();
							//EmployeeHandler.AddEmployeeType();
							$('#employeeType').change(function () {
					            var text = $(this).find('option').filter(':selected').text();
					            if(text == "Sales Executive"){
					            	$('#salesExecutive_granted_days').show();
					            	//$('#grantedDays').val(2);
					            	
					            }else{
					            	$('#salesExecutive_granted_days').hide();
					            }
					    });
							});
					</script>									 					

