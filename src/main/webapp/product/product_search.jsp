<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="product-search-form-container" class="ui-container"
	title="Search Product">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div  id="action-add-arrived-quantity" style="float:right; cursor: pointer;">Arrived Quantity</div> 
			<div  id="action-add-damaged-quantity" style="float:right; cursor: pointer;">Damaged Quantity</div> 
			<div  id="action-add-product" style="float: right; cursor: pointer;">Add Product</div>
		</div>
	</div>
	<div class="ui-content form-panel form-panel-border">
		<form id="product-search-form">
			<div class="fieldset-row" style="height: 45px; margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%></div>
						<div class="input-field">
							<input name="productName" id="productName">
						</div>
					</div>

				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row" style="margin-bottom: 30px;">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%>
						</div>
						<div class="input-field" id="bNumber">
							<input name="batchNumber" id="batchNumber">
						</div>

					</div>
					<input name="action" value="search-product" type="hidden"
						id="productAction">

				</div>
			</div>
		</form>
		<div id="search-buttons" class="search-buttons">
			<div id="action-search-product-list" class="ui-btn btn-search">Search</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
		</div>
	</div>
</div>

<div id="product-view-dialog" style="display: none;"
	title=" View Product">
	<div id="product-view-container"></div>
</div>
<div id="product-add-dialog" style="display: none; overflow-y: hidden;"
	title="Add Product">
	<div id="product-add-container"></div>
</div>
<div id="product-edit-dialog" style="display: none; overflow-y: hidden;"
	title="Edit Product">
	<div id="product-edit-container"></div>
</div>
<div id="product-delete-dialog" style="display: none; overflow-y: hidden;"
	title="Delete Product">
	<div id="product-delete-container"></div>
</div>
<div id="product-add-arrived-quantity-dialog" style="display: none;"
	title="Add Arrived Quantity">
	<div id="product-add-arrived-quantity-container"></div>
</div>
<div id="product-add-damaged-quantity-dialog" style="display: none;"
	title="Add Damaged Quantity">
	<div id="product-add-damaged-quantity-container"></div>
</div>
<div id="search-results-container2"
	class="ui-container search-results-container">
    <div class="ui-content">
		<div id="search-results-list" class="green-results-list" style="overflow-x: hidden;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div> 
<script type="text/javascript">
$(document).ready(function() {
	ProductHandler.initSearchProductListOnLoad();	
});
   ProductHandler.initFunctionButtons();
	ProductHandler.initSearchProductList(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
</script>

