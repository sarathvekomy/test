<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<div id="organization-search-form-container" class="ui-container" title="Search Organization">
					<div class="green-title-bar">
						<div class="green-title-bar2">
							<div class="page-icon employee-search-icon"></div>
							<div class="page-title employee-search-title"></div>
							<div class="ui-btn" id="action-add-school" style="float:right;"></div> 
						</div>
					</div>

					<div class="ui-content form-panel form-panel-border">												
						<form id="user-search-form">
							<div class="fieldset-row" style="height: 20px;margin-top: 10px;">
								<div class="fieldset" style="height: 20px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%></div>
										<div class="input-field"><input name="username" id="username" ></div>
									</div>
								</div>
								<div class="separator" style="height: 20px;"></div>								
								<div class="fieldset" style="height: 20px;">
									<div class="form-row">
						<div class="label">Full Name</div>
						<div class="input-field">
							<input  name="firstName" id="firstName">
						</div>
					</div>
								</div>
							</div>		
							<div class="fieldset-row" style="height: 20px; margin-top: 10px;">
								<div class="fieldset" style="height: 20px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_ORGANIZATION_NAME_LABEL)%></div>
										<div class="input-field"><input name="organizationName" id="organizationName" ></div>
									</div>
								</div>
								</div>				 
							<input name="action" value="search-user" type="hidden" id="manageUserAction">
						</form>
						<div id="search-buttons" class="search-buttons" style="margin-top: 10px;">
							<div id="action-search" class="ui-btn btn-search"></div>
							<div id="action-clear" class="ui-btn btn-clear"></div>
						</div>
					</div>
				</div>
				<div id="user-view-dialog" style="display: none;" title="User Details">
					<div id="user-view-container"></div>
				</div>
					<div id="user-delete-dialog" style="display: none;" title="User Details">
					<div id="user-delete-container"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">
					<div class="ui-content">
						<div id="green-results-list" class="green-results-list" style="height: 315px; overflow:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
					<%-- OrganizationHandler.initSearchEmployee(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>); --%>
					//OrganizationHandler.initSearchOrganization(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
					$(document).ready(function() {
						//OrganizationHandler.initsearchOrganizationOnload();
						ManageUserHandler.initAddButtons();
						ManageUserHandler.search();
						
						});
				</script>				
