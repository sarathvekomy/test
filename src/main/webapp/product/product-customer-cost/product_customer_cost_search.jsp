<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>

<div id="product-customer-cost-search-form-container" class="ui-container"
	title="Search Product Customer Cost">
	<!--<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-search-icon"></div>
			<div class="page-title product-search-title"></div>
		</div>
	</div>-->
	<div class="ui-content form-panel form-panel-border">
		<form id="product-customer-cost-search-form">
			<div class="fieldset-row" style="height: 60px; margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CUSTOMER_NAME_LABEL)%></div>
						<div class="input-field">
							<input name="businessName" id="businessName">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row" style="margin-bottom: 30px;">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input name="productName" id="productName">
						</div>
					</div>
					<input name="action" value="search-product-customer-cost" type="hidden"
						id="productCustomerCostAction">
				</div>
			</div>
		</form>
		<div id="search-buttons" class="search-buttons">
			<div id="action-search-product" class="ui-btn btn-search">Search</div>
			<div id="action-search-clear" class="ui-btn btn-clear">Clear</div>
		</div>
	</div>
</div>
 <div id="product-customer-cost-view-dialog" style="display: none;" title="Product Customer Cost Details">
	<div id="product-customer-cost-view-container"></div>
</div> 
<div id="product-customer-cost-delete-dialog" style="display: none;" title="Product Customer Cost Details">
	<div id="product-customer-cost-delete-container"></div>
</div> 
<div id="search-results-container2"
	class="ui-container search-results-container">
	<div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: 300px;"></div>
		 <div class="green-footer-bar"></div> 
	</div>
</div>

<script type="text/javascript">
 ProductCustomerCostHandler.initProductCustomerCostSearchResultOnLoad();
 ProductCustomerCostHandler.initSearchProduct(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>); 
</script>



