<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
	User user = (User) SecurityContextHolder.getContext()
	.getAuthentication().getPrincipal();

	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	User loggedInUser = (User) SecurityContextHolder.getContext()
	.getAuthentication().getPrincipal();

	OrganizationDao organizationDao = (OrganizationDao) hibernateContext
	.getBean("organizationDao");
%>
<div id="allot-stock-form-container" title="Allot Stock">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-add-icon"></div>
			<div class="page-title employee-add-title"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">

		<!-- start of sales-book-form -->

		<form id="allot-stock-form" style="height: 280px;">

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 140px;">
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_SALES_EXECUTIVE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="salesExecutive"
								id="salesExecutive">
						</div>
					</div>
					<div id="sales-executive-name-suggestions"
						class="sales-executive-name-suggestions"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_PERVIOUS_CLOSING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input 	name="closingBalance" id="previousClosingBalance" readonly="readonly" style="background:none; border:none;">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_ADVANCE_LABEL)%></div>
						<div class="input-field">
							<input class="constrained" name="advance" id="advance">
						</div>
						<span id="advanceValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="advance_pop" class="helppop" style="display: block;" aria-hidden="false;">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Advance can contain only float type value and should not exceed by 10 digits.</p></div>
                            </div> 
					</div>
					<div class="form-row">
						<div class="label" style="float: left;"><%=Msg.get(MsgEnum.ACCOUNT_ALLOTMENT_TYPE_LABEL)%></div>
						<div class="input-field">
							<select name="allotmentType" id="allotmentType" class="mandatory">
								<option value="daily">Daily</option>
								<%
									for(String allotmentType: DropDownUtil.getDropDown(DropDownUtil.ALLOTMENT_TYPE).keySet()) {
								%>
								<option value="<%=allotmentType%>"><%=DropDownUtil.getDropDown(DropDownUtil.ALLOTMENT_TYPE, allotmentType)%></option>
								<%
									}
								%>
							</select>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 120px;"></div>
				<div class="fieldset" style="height: 140px;">
					<div class="form-row" style="margin-top: 10px;">
						<div class="label" style="width: 125px;"><%=Msg.get(MsgEnum.ACCOUNTS_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="createdOn" id="createdOn" readonly="readonly" style="background: none; border: none;" value="<%=DateUtils.format(new java.util.Date())%>">
						</div>
					</div>
					<div class="form-row" style="height: 10px;">
						<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_OPENING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory constrained"
								name="openingBalance" id="openingBalance" readonly='readonly'>
						</div>
						<span id="openingBalanceValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="openingBalance_pop" class="helppop" style="display: block;" aria-hidden="false;">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Opening Balance can contain only float type value and should not exceed by 10 digits.</p></div>
                            </div> 
					</div>
					<div class="form-row" style="height: 10px; margin-top: 20px;">
						<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_CLOSING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input name="closingBalance" id="closingBalance"  readonly="readonly" style="background:none; border:none;">
						</div>
					</div>
					
				</div>
				<div id="page-buttons" class="page-buttons">
					<input name="action" value="update-stock" type="hidden"
						id="accountsAction">
				</div>

				<div id="search-results-container1"
					class="ui-container search-results-container" style="float: left;">
					<div class="ui-content grid-data">
						<div id="products-search-results-list" class="green-results-list"
							style="height: 180px; width: 830px; float: left; overflow: initial; overflow-x: hidden; overflow-y: hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
			</div>

		</form>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 230px; margin-top: 80px;">
			<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
			<div id="button-update" class="ui-btn btn-update"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>

	</div>
</div>
<div id="allot-stock-edit-dialog" style="display: none; overflow-y: hidden;"
	title="Edit Allotment">
	<div id="allot-stock-edit-container"></div>
</div>
<script type="text/javascript">
/* 	$(document).ready(function() {
		$(".datepicker").datepicker({
			maxDate : 0,
			buttonImageOnly : false,
			dateFormat : 'dd-mm-yy',
			changeMonth : true,
			changeYear : true
		});
		SalesBookHandler.initAllotStockSelection();
	}); */
	SalesBookHandler.initSalesExecutiveName();
	SalesBookHandler.loadGridByDate();
	$('#products-search-results-list').hide();
	$('.helppop').hide();
</script>
