<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="java.util.Date"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="change-transactions-request-form-container" class="ui-container"
	title="Change Transactions Request">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon change-transactions-search-icon"></div>
			<div class="page-title change-transactions-search-title"></div>
		</div>
	</div>

<div class="ui-content form-panel form-panel-border"style="height: 100px;">												
						<form name="form2"id="change-transactions-request-search-form">
							<div class="fieldset-row" style="height: 50px;margin-top: 10px;">
						<div class="fieldset" style="height:30px;">
						<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.TRANSACTION_CHANGE_REQUEST_TYPE) %></div>
										<div class="input-field"><select class="mandatory" name="transactionRequestTypes" id="transactionRequestTypes">
										<option value="-1">Select</option>
										<option value="deliveryNote">Delivery Note</option>
										<option value="salesReturn">Sales Return</option>
										<option value="dayBook">Day Book</option>
										<option value="jounal">Journal</option>
										</select></div>
					<div id="transactionRequestTypes_pop" class="helppop" style="display: block;" aria-hidden="false">
						<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
							<p>Choose Atleast One Transaction Type</p>
						</div>
					    </div>
						 <span id="transactionRequestTypesValid"style="float: left; position: absolute; margin-left: -20px; margin-top: 5px"></span>
						</div>
						</div>
								<div class="separator" style="height:50px;"></div>								
							<div class="fieldset" style="height:50px;">
								<div class="form-row" style="height: 30px;">
						         <div class="label"><%=Msg.get(MsgEnum.TRANSACTION_CHANGE_REQUEST_DATE)%></div>
						         <div class="input-field">
							           <input name="changeTransactionRequestDate" id="changeTransactionRequestDate" value='<%=DateUtils.format(new Date())%>' disabled="true">
						         </div>
					           </div>
								</div>
							</div>	
						<input name="action" value="change-transaction-request" type="hidden" style="margin-bottom: -80px;"
				               id="changeTransactionAction">
		</form>
	</div>
</div>
<div id="search-results-container2"
	class="ui-container search-results-container">
    <div class="ui-content">
		<div id="change-transaction-search-results-list" class="green-results-list"
			style="height: 315px; overflow: visible;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div> 
<script type="text/javascript">
$(document).ready(function() {
	$('.helppop').hide();
});
TransactionChangeRequestHandler.initTransactionChangeSearchButtons();
</script>