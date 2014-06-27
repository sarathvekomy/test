<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="employee-search-form-container" class="ui-container" title="Search Employee">
					<div class="ui-content form-panel form-panel-border">												
						<form id="employee-search-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_NAME) %></div>
										<div class="input-field"><input name="firstName" id="firstName" ></div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%> </div>
										<div class="input-field"><input name="username" id="username" ></div>
									</div>
								<%-- 	<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_LAST_NAME_LABEL)%></div>
										 <div class="input-field"><input name="lastName" id="lastName" ></div>
									</div> --%>
								</div>
								<div class="separator" style="height: 60px;"></div>								
								<div class="fieldset" style="height: 40px;">
									<%-- <div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%> </div>
										<div class="input-field"><input name="username" id="username" ></div>
									</div> --%>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_EMPLOYEE_TYPE_LABEL)%></div>
										<div class="input-field">
											<select name="employeeType" id="employeeType" class="mandatory constrained" constraints='{"fieldLabel":"Employee Type","mustSelect":"true"}'>
												<option value="All">All</option> 
												<option value="Super Management">Super Management</option> 
												<%													
													for(String employeeType: DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE).keySet()) {%>
												<option value="<%=employeeType %>"><%=DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE, employeeType) %></option>
												<%} %>
											</select> 
										</div>
									</div>
								</div>
							</div>						 
							<input name="action" value="search-employee" type="hidden" id="employeeAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-employee" class="ui-btn btn-search">Search</div>
							<div id="action-clear" class="ui-btn btn-clear">Clear</div>
						</div>
					</div>
				</div>

				<div id="employee-view-dialog" style="display: none;" title="Employee Details">
					<div id="employee-view-container"></div>
				</div>
				<div id="employee-delete-dialog" style="display: none;" title="Employee Details">
					<div id="employee-delete-container"></div>
				</div>
				<div id="employee-list-dialog" style="display: none; overflow-y: hidden;" title="Employee List">
	                 <div id="employee-list-container"></div>
               </div>
				<div id="employee-login-view-dialog" style="display: none;" title="Employee Login Details">
					<div id="employee-login-view-container"></div>
				</div>
				<div id="employee-reset-password-dialog" style="display: none;" title="Reset Password">
					<div id="employee-reset-password-container"></div>
				</div>
				<div id="search-results-container2" class="ui-container search-results-container">
					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 315px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
					EmployeeHandler.initSearchEmployee(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
					$(document).ready(function() {
				//commented unnecessary calling to page expansion method ,to be removed in the future
						//EmployeeHandler.initEmployeePageSelection();
						EmployeeHandler.initsearchEmployeeOnload();
						});
				</script>									