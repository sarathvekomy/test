<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<div id="deliverynote-add-form-container" title="Delivery Note">
	<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="deliverynote-form" style="height: 360px;">
			<div class="add-student-tabs">
				<div class="step-selected" style="width: 230px;">
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_LABEL)%>
					</div>
				</div>
				<div class="step-no-select">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PREVIEW_LABEL)%>
					</div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 70px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="businessName" id="businessName">
						</div>
					</div>
					<div id="business-name-suggestions" class="business-name-suggestions"></div>
					<a href="#" id="addCustomer" style="margin-left: 135px; text-decoration: none;">New Customer?</a>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class ="mandatory" name="invoiceName" id="invoiceName">
						</div>
						<span id="invoiceNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
									<div id="iname_pop" class="helppop" style="display: block;margin-top:-5px;" aria-hidden="false">
                <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;margin-top:2px;"><p><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME) %></p></div>
            </div>
					</div>
				</div>
				<div class="separator" style="height: 70px;"></div>
				<div class="fieldset" style="height: 70px;">
					<div class="form-row" style="height: 50px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_DATE_LABEL)%></div>
						<div class="input-field">
							<input name="invoiceDate" id="invoiceDate" value='<%=DateUtils.format(new Date())%>' class="read-only" readonly="readonly">
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="forPayments" id="forPayments" type="checkbox">
						</div>
						<div class="label" style="float: left; margin-left: -5px"><%=Msg.get(MsgEnum.DELIVERY_NOTE_FOR_PAYMENTS_LABEL)%>
						</div>
					</div>
				</div>
			</div>
			<div class="dn-filler"></div>
			<div class="fieldset-row gridDisplay"
				style="margin-top: 60px; display: none;">
				<div class="fieldset" style="height: 200px; width: 699px;">
					<div class="report-header"
						style="width: 699px; height: 40px; overflow-x: hidden; overflow-y: hidden;">
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRODUCT_NAME_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_AVAILABLE_QUANTITY_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRODUCT_COST_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRODUCT_QUANTITY_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BONUS_QUANTITY_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BONUS_REASON_LABEL)%></div>
						<div class="report-header-column2 centered" style="width: 85px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_TOTAL_COST_LABEL)%></div>
					</div>
					<div id="search-results-list" class="green-results-list"
						style="height: 150px; width: 699px; overflow-x: hidden;"></div>
				</div>
				<div class="fieldset" style="height: 50px; float: right;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_TOTAL_COST_LABEL)%>
						</div>
						<div class="input-field-float">
							<input
								style="background-color: inherit; border: 0 none; font-weight: bold;"
								name="totalCost" id="totalCost" readonly="readonly" value="0.00">
						</div>
					</div>
					<div id="page-buttons" class="page-buttons">
						<input name="action" value="save-deliverynote-info" type="hidden" id="deliverynoteAction">
					</div>
				</div>
			</div>
		</form>

		<form id="deliverynote-product-form" name="form1"
			style="height: 340px; display: none;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 230px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-selected">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_LABEL)%></div>
				</div>
				<div class="step-no-select">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PREVIOUS_CREDIT_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="previousCredit"
								id="previousCredit" readonly='readonly'>
						</div>
					</div>
				</div>
				<div class="separator delivery-note-separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px; width: 320px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_ADVANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="presentAdvance"
								id="presentAdvance" readonly='readonly'>
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_PAYABLE_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="presentPayable"
								id="presentPayable" readonly='readonly' value="0.00">
						</div>
					</div>
				</div>
				<div class="separator delivery-note-separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px; width: 320px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_PAYMENT_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="presentPayment"
								id="presentPayment">
						</div>
					</div>

				</div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height:70px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_TOTAL_PAYABLE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="totalPayable" id="totalPayable"
								readonly='readonly' value="0.00">
						</div>
					</div>

				</div>
				<div class="separator delivery-note-separator"
					style="height:70px;"></div>
				<div class="fieldset" style="height: 70px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="balance" id="balance"
								readonly='readonly'>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select name="paymentType" id="paymentType"
								class="mandatory"
								constraints='{"fieldLabel":"Payment Type","mustSelect":"true"}'>
							</select>
						</div>
					</div>
				</div>
				<div class="fieldset"style="height: 30px;">
				<div class="form-row"id="bankname">
				     <div class="label">Bank Name</div>
				    <div class="input-field">
				          <input  name="bankName" id="bankName"/>
				     </div>
				</div>
				<div class="form-row"id="branchname">
				     <div class="label">Branch Name</div>
				    <div class="input-field">
				          <input name="branchName" id="branchName"/>
				     </div>
				</div>
				</div>
				<div class="separator delivery-note-separator"
					style="height: 5px;"></div>
					 <div class="fieldset"style="height:20px;">
				<div class="form-row"id="chequeno">
				     <div class="label">Cheque No</div>
				    <div class="input-field">
				          <input name="chequeNo" id="chequeNo"/>
				     </div>
				</div>
				</div> 
						</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="save-product-info" type="hidden"
								id="deliverynoteAction">
							<input name="businessName1" type="hidden" id="businessName1">						
						</div>
					</div>
				</div>
			</div>
		</form>
		<div id="deliverynote-preview-container" class="delivery-note-preview" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next"></div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
	DeliveryNoteHandler.customerChangeRequest();
	DeliveryNoteHandler.initDeliveryNotePageSelection();
	DeliveryNoteHandler.initAddButtons();
	DeliveryNoteHandler.load();
	UserHandler.initCheckUsername();
	UserHandler.initCheckPassword();
	$(document).ready(function() {
		$('.helppop').hide();
			$('#bankname').hide();
			$('#branchname').hide();
			$('#chequeno').hide();
			SystemDefaultsHandler.getPaymentTypes();
	});

		
</script>

