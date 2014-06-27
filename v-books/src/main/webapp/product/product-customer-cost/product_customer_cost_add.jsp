<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<div id="product-customer-cost-form-container" title="Product Customer Cost">
	<div class="green-title-bar">
		<div class="green-title-bar2">
				<div class="page-icon employee-search-icon"></div>
							<div class="page-title employee-search-title"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="product-customer-cost-add-form" style="height: 375px;">
			 <div class="fieldset-row" style="margin-top: 0px;">
			   <div class="fieldset" style="height: 120px;">
				   <div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CUSTOMER_NAME_LABEL)%>
					</div>
					<div class="input-field">
						<input class="mandatory constrained" name="businessName" id="businessName">
					</div>
					<span id="businessNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
				    <div id="businessNameValid_pop" class="helppop" style="display: block;" aria-hidden="false;">
                        <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Business Name can contain letters, numbers and space.</p></div>
                    </div> 
                     <div id="businessNameEmpty_pop" class="helppop" style="display: block;" aria-hidden="false;">
                        <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Business Name cannot be empty</p></div>
                    </div> 
                     <div id="businessNameIncorrect_pop" class="helppop" style="display: block;" aria-hidden="false;">
                        <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;">Please select available businessname</p></div>
                    </div>
				  </div>
				<div id="business-name-suggestions" class="business-name-suggestions" style="z-index: 10;">
				</div>
				<div class="separator" style="height: 20px;"></div>
		        <div class="form-row"> 
					 <div id="search-results-list" class="green-results-list" style="height: 340px; width: 697px; overflow: hidden;">
					 </div>
				 </div>  
				 <div id="page-buttons" class="page-buttons">
					<input name="action" value="save-product-customer-cost" type="hidden"
						id="productCustomerCostAction">
			   </div>
			</div>
		</div>
		</form>
		<div id="product-preview-container" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next" style="display: none"></div>
			<div id="button-save-product-customer-cost" class="ui-btn btn-save"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel-product-customer-cost" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<div id="product-customer-cost-add-dialog" style="display: none; overflow-y: hidden;"
	title="Add Product Customer Cost">
	<div id="product-customer-cost-add-container"></div>
</div>
<script type="text/javascript">
    ProductCustomerCostHandler.setTableGrid();
	ProductCustomerCostHandler.initAddButtons();
	ProductCustomerCostHandler.load();
	$('.helppop').hide();
</script>
