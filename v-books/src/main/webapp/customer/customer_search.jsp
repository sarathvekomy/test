<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="customer-search-form-container" class="ui-container" title="Search Customer">
					<div class="green-title-bar">
						<div class="green-title-bar2">
						<div class="page-icon employee-search-icon"></div>
							<div class="page-title customer-search-title"></div>
						</div>
					</div>

					<div class="ui-content form-panel form-panel-border">												
						<form id="customer-search-form">
							<div class="fieldset-row" style="height: 60px; margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_FULL_NAME)%></div>
										 <div class="input-field"><input name="customerName" id="customerName" ></div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAME)%></div>
										 <div class="input-field"><input name="businessName" id="businessName" ></div>
									</div>
								</div>
								<div class="separator" style="height: 60px;"></div>								
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_INVOICE_NAME)%> </div>
										<div class="input-field"><input name="invoiceName" id="invoiceName" ></div>
									</div>
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_DETAILS_LOCALITY)%></div>
										 <div class="input-field"><input name="locality" id="locality" ></div>
									</div>
								</div>
							</div>						 
							<input name="action" value="search-customer" type="hidden" id="customerAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-customer" class="ui-btn btn-search"></div>
							<div id="action-clear" class="ui-btn btn-clear"></div><!--
							<div id="action-cancel" class="ui-btn btn-cancel"></div>
						--></div>
					</div>
				</div>

				<div id="customer-view-dialog" style="display: none;" title="Customer Details">
					<div id="customer-view-container"></div>
				</div>
					<div id="customer-delete-dialog" style="display: none;" title="Customer Details">
					<div id="customer-delete-container"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 315px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
				CustomerHandler.initSearchCustomer(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
				$(document).ready(function() {
					CustomerHandler.customerSearch();

					});
				</script>									