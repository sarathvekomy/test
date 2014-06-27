<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="sales-search-form-container" class="ui-container" title="Search stock">
					<div class="ui-content form-panel form-panel-border">												
						<form id="sales-return-search-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_BUSINESS_NAME_LABEL)%></div>
										<div class="input-field"><input name="businessName" id="businessName" ></div>
									</div>
									
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_INVOICE_NAME_LABEL)%></div>
										 <div class="input-field"><input name="invoiceName" id="invoiceName" ></div>
									</div>	
								</div>
								<div class="separator" style="height: 40px;"></div>								
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_CREATED_DATE)%> </div>
									<div class="input-field"><input class="datepicker" id="createdOn" name="createdOn" readonly="readonly"></div>
									</div>
								</div>
								<input type="hidden" id="createdBy" class="createdBy" name="createdBy" value="<%=user.getName()%>">
							</div>						 
							<input name="action" value="search-sales-return" type="hidden" id="salesReturnAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-sales-return" class="ui-btn btn-search"></div>
							<div id="action-clear" class="ui-btn btn-clear"></div>
							
						</div>
					</div>
				</div>

				<div id="sales-return-view-dialog" style="display: none" title="Sales returns">
					<div id="sales-return-view-container" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 315px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
				$(document).ready(function() {
					SalesReturnsHandler.initSearchSalesReturnOnLoad();

					 $(".datepicker").datepicker({
					       maxDate: 0,
					       buttonImageOnly : false,
					       dateFormat : 'dd-mm-yy',
					       changeMonth : true,
					       changeYear : true
					      
					      });
					   
					 
					 });
				SalesReturnsHandler.initSearchSalesReturn(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
				</script>									