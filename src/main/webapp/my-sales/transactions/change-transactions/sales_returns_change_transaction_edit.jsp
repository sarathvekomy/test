<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<div id="sales-returns-add-form-container" title="Sales Returns">
	<div class="ui-content form-panel full-content">
		<form id="sales-returns-form" style="height: 300px">
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
			<div class="fieldset-row" style="margin-top: 0px;">
				<div class="fieldset" style="height: 60px;">
				 <div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.INVOICE_NUMBER_LABEL)%></div>
						<div class="input-field">
							<input  name="invoiceNumber" id="invoiceNumber" tabindex="-1" readonly="readonly" class="read-only">
						</div>
				</div> 
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="businessName" tabindex="-1"
								id="businessName" constraints='{"fieldLabel":"businessName","charsOnly":"true"}'readonly="readonly" class="read-only">
						</div>
					</div>
					<div class="form-row" style="height: 40px; width:20px;">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 130px; margin-top: -30px;"
						name="remarks" id="remarks" cols="53" rows="2"></textarea>
				</div> 
					<div class="form-row" style="height: 40px; margin-top: 10px;">
						<div class="label"><%=Msg.get(MsgEnum.CHANGE_REQUEST_DESCRIPTION)%></div>
						<div class="input-field">
							<textarea style="float: left; margin-left: -5px;" name="description" id="description" rows="2" cols="53"></textarea>
						</div>
				</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_INVOICE_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="invoiceName"
								id="invoiceName" constraints='{"fieldLabel":"invoiceName","charsNumsOnly":"true"}'>
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row gridDisplay"
				style="margin-top: 80px; display: none;">
				<div class="fieldset" style="height: 150px; width: 699px; margin-top: 80px;">
					<div class="report-header" style="width: 699px; height: 30px;">
						<div class="report-header-column2 centered" style="width: 120px;"><%=Msg.get(MsgEnum.SALES_RETURNS_PRODUCT_NAME_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 120px;"><%=Msg.get(MsgEnum.SALES_RETURNS_BATCH_NUMBER_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 120px;"><%=Msg.get(MsgEnum.SALES_RETURNS_DAMAGED_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 120px;"><%=Msg.get(MsgEnum.SALES_RETURNS_RESALABLE)%></div>
						<div class="report-header-column2 centered" style="width: 120px; line-height:12px;"><%=Msg.get(MsgEnum.SALES_RETURNS_RETURN_QUANTITY)%></div>
					</div>
					<div id="search-results-list" class="green-results-list1"
						style="height: 150px; width: 699px; overflow-x: hidden;"></div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="input-field">
								<input name="salesReturnId"  type="hidden" id="salesReturnId">
								<input name="originalInvoiceName"  type="hidden" id="originalInvoiceName">
								<input name="originalRemarks"  type="hidden" id="originalRemarks">
						</div>
					</div>
				</div>
			</div>
		</form>
		<div id="sales-return-preview-container" style="overflow: auto">
		</div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 80px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none">Prev</div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;">Save</div>
			<div id="button-next" class="ui-btn btn-next">Next</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>

</div>
</div>
<script type="text/javascript">
TransactionChangeRequestHandler.initSalesReturnAddButtons();
TransactionChangeRequestHandler.salesReturnLoad();
	if(PageHandler.expanded){
		PageHandler.pageSelectionHidden=false;
		PageHandler.hidePageSelection();
	}
</script>
