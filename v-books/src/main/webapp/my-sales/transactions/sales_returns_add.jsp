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


<div id="sales-returns-add-form-container" title="Sales Returns">
	 <div class="green-title-bar">
		<div class="green-title-bar">
				<div class="green-title-bar2">
				</div>
			</div> 
	</div> 
	<div class="ui-content form-panel full-content">
		<form id="sales-returns-form" style="height: 350px">
		<div class="add-student-tabs">
			<div class="step-selected">
				<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.SALES_RETURNS_LABEL)%>
				</div>
			</div>
			<div class="step-no-select">
				<div class="step-selected-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%>
				</div>
			</div>
			<div class="step-no-select-corner"></div>
		</div> 
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="businessName"
								id="businessName">
						</div>
					</div>
					<div id="business-name-suggestions"
						class="business-name-suggestions"></div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_INVOICE_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="invoiceName"
								id="invoiceName"
								constraints='{"fieldLabel":"invoiceName","charsNumsOnly":"true"}'>
						</div>
						<span id="invoiceNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
									<div id="iname_pop" class="helppop" style="display: block;margin-top:-5px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME) %></p></div>
            </div>
					</div>
				</div>
			</div>
			<div class="fieldset-row gridDisplay"
				style="margin-top: 10px; display: none;">
				<div class="fieldset" style="height: 200px; width: 699px;">
					<div class="report-header" style="width: 699px; height: 40px;">
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_PRODUCT_NAME_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_BATCH_NUMBER_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_DAMAGED_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_RESALABLE)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_RETURN_QUANTITY)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_COST)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.SALES_RETURNS_TOTAL_COST)%></div>
					</div>
					<div id="search-results-list" class="green-results-list1"
						style="height: 150px; width: 699px; overflow-x: hidden;"></div>
				</div>
				<div class="fieldset" style="height: 50px; float:right;">
					 <div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_TOTAL_COST)%>
						</div>
						<div class="input-field-float"><input style="background-color:inherit;  border:0 none; font-weight: bold;" name="grandTotalCost" id="grandTotalCost" readonly="readonly" value = "0.00"></div>
					</div> 
					<input name="action" value="save-sales-returns" type="hidden"
						id="salesReturnAction">
				</div>
			</div>
		</form>
		<div id="sales-return-preview-container" style="overflow: auto">
		</div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
			<div id="button-next" class="ui-btn btn-next"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>

</div>
</div>

<script type="text/javascript">
	SalesReturnsHandler.initAddButtons();
	UserHandler.initCheckUsername();
	UserHandler.initCheckPassword();
	SalesReturnsHandler.load();
	$(document).ready(function() {
		$('.helppop').hide();
	});
	if(PageHandler.expanded){
		PageHandler.pageSelectionHidden=false;
		PageHandler.hidePageSelection();
	}
</script>
