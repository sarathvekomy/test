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
                            <div class="ui-btn" id="action-add-school" style="float:right;">Add Organization</div> 
							
						</div>
					</div>
					
					<div class="ui-content form-panel form-panel-border">												
						<form id="organization-search-form">
							<div class="fieldset-row" style="height: 40px;margin-top: 10px;">
								<div class="fieldset" style="height: 20px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_COUNTRY_LABEL)%></div>
										<div class="input-field"><input name="country" id="country" ></div>
									</div>
									
								</div>
								<div class="separator" style="height: 40px;"></div>								
								<div class="fieldset" style="height: 20px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_MAIN_BRANCH_TYPE_LABEL)%></div>
										<div class="input-field">
											<select name="mainBranch" id="mainBranch">
												<option value="All">All</option> 
												<%													
													for(String mainBranch: DropDownUtil.getDropDown(DropDownUtil.BRANCH_TYPE).keySet()) {%>
												<option value="<%=mainBranch %>"><%=DropDownUtil.getDropDown(DropDownUtil.BRANCH_TYPE, mainBranch) %></option>
												<%} %>
											</select> 
										</div>
									</div>
								</div>
							</div>						 
							<input name="action" value="search-organization" type="hidden" id="organizationAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<button id="action-search-organization" class="ui-btn btn-search"> Search</button>
							<button id="action-clear" class="ui-btn btn-clear">Clear</button>
						</div>
					</div>
				</div>
				<div id="organization-view-dialog" style="display: none;" title="Organization Details">
					<div id="organization-view-container"></div>
				</div>
					<div id="organization-delete-dialog" style="display: none;" title="Organization Details">
					<div id="organization-delete-container"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="school-results-list" class="green-results-list" style="height: 325px; width: 698px; overflow:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				<div id="organization-list-dialog" style="display: none; overflow-y: hidden;" title="Organization List">
	                 <div id="organization-list-container"></div>
               </div>
				
				<script type="text/javascript">
					<%-- OrganizationHandler.initSearchEmployee(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>); --%>
					OrganizationHandler.initSearchOrganization(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
					$(document).ready(function() {
						//OrganizationHandler.initEmployeePageSelection();
						OrganizationHandler.initsearchOrganizationOnload();
						OrganizationHandler.initOrganizationListButtons();
						
						});
				</script>				
