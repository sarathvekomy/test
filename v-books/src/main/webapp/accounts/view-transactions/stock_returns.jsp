<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="stock-search-form-container" class="ui-container" title="Search stock">
					<!--<div class="green-title-bar">
						<div class="green-title-bar2">
							<div class="page-icon employee-search-icon"></div>
							<div class="page-title employee-search-title"></div>
						</div>
					</div>-->

					<div class="ui-content form-panel form-panel-border">												
						<form id="sales-return-search-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.STOCK_RETURNS_BUSINESS_NAME_LABEL)%></div>
										<div class="input-field"><input name="businessName" id="businessName" ></div>
									</div>
									
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.STOCK_RETURNS_INVOICE_NAME_LABEL)%></div>
										 <div class="input-field"><input name="invoiceName" id="invoiceName" ></div>
									</div>	
								</div>
								<div class="separator" style="height: 40px;"></div>								
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%></div>
										<div class="input-field"><input  name="createdBy" id="salesExecutive" ></div>
									</div>
									<div id="sales-executive-name-suggestions" class="sales-executive-name-suggestions" style="height: 80px;"></div>
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.STOCK_RETURNS_DATE_LABEL)%> </div>
									<div class="input-field"><input class="datepicker" id="createdOn" name="createdOn" readonly="readonly"></div>
									</div>
								</div>
								
							</div>						 
							<input name="action" value="search-sales-return" type="hidden" id="accountsAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-sales-return" class="ui-btn btn-search">Search</div>
							<div id="action-clear" class="ui-btn btn-clear">Clear</div>
							
						</div>
					</div>
				</div>

				<div id="accounts-view-dialog" style="display: none;" title="Sales returns">
					<div id="accounts-view-container" style="width:200px;height:200px"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 335px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
				$(document).ready(function() {
					SalesReturnViewHandler.initSearchStockReturnOnLoad();
					//SalesBookHandler.showPageSelection();
					   $(".datepicker").datepicker({
						   
					       maxDate: 0,
					       buttonImageOnly : false,
					       dateFormat : 'dd-mm-yy',
					       changeMonth : true,
					       changeYear : true
					      
					      });
					   
					 
					 });
				//SalesBookHandler.initSalesExecutiveName();
				SalesReturnViewHandler.initSearchStockReturn(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
				</script>									