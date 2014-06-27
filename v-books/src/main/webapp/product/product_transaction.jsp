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
	<div class="ui-content form-panel form-panel-border">
		<form id="product-transaction-search-form">
			<div class="fieldset-row" style="height: 45px; margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%></div>
						<div class="input-field">
							<input name="productName" id="productName" type="text">
						</div>
					</div>
					<div class="form-row" style="margin-bottom: 30px;">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%>
						</div>
						<div class="input-field" id="bNumber">
							<input name="batchNumber" id="batchNumber" type="text">
						</div>

					</div>

				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row" >
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CREATED_DATE_LABEL)%>
						</div>
						<div class="input-field" id="bNumber">
							<input name="date" id="date" class="datepicker" type="text">
						</div>

					</div>
					<div class="form-row">
						<div class="label">Transaction Type
						</div>
						<div class="input-field" id="bNumber">
							<select class="mandatory" name="type" id="type">
							            <option>Select</option>
										<option value="Arrived">Arrived</option>
										<option value="Damaged">Damaged</option>
										<option value="Allotted">Allotted</option>
										</select>
						</div>

					</div>
					<input name="action" value="show-product-history" type="hidden"
						id="productAction">

				</div>
			</div>
		</form>
		<div id="search-buttons" class="search-buttons" style="margin-top:20px;">
			<div id="action-search-product-transactions" class="ui-btn btn-search">Search</div>
			<div id="action-transaction-clear" class="ui-btn btn-clear">Clear</div>
		</div>
	</div>
</div>


<div id="search-results-container2"
	class="ui-container search-results-container">
    <div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: auto; overflow: hidden; float:left;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div> 
<script type="text/javascript">
$(document).ready(function() {
	ProductHandler.showTransactionsOnLoad();	
	 $(".datepicker").datepicker({
	       maxDate: 0,
	       buttonImageOnly : false,
	       dateFormat : 'dd-mm-yy',
	       changeMonth : true,
	       changeYear : true
	      
	      });
});
ProductHandler.showTransactions();
</script>

