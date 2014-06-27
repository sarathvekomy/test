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
	<!--<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>-->
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
							<input class ="mandatory" name="invoiceName" id="invoiceName" tabindex="-1">
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
							<input name="invoiceDate" id="invoiceDate" value='<%=DateUtils.format(new Date())%>' class="read-only" readonly="readonly" tabindex="-1">
						</div>
					</div>
                    
					<div class="form-row">
                    
						<div class="input-field" style="width:35px !important;">
							<input name="forPayments" id="forPayments" type="checkbox" style="width:28px !important; border: 0 !important;">
						</div>
						<div class="label" style="float: left; margin-left: -5px; margin-top:3px;"><%=Msg.get(MsgEnum.DELIVERY_NOTE_FOR_PAYMENTS_LABEL)%>
						</div>
					</div>
				</div>
			</div>
			<div class="dn-filler"></div>
			<div class="fieldset-row gridDisplay"
				style="margin-top: 60px; display: none;">
				<div class="fieldset" style="height: 200px; width: 699px;">
					<div class="report-header" style="width: 699px; height: 30px;line-height:12px; overflow-x: hidden; overflow-y: hidden;">
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
								name="totalCost" id="totalCost" readonly="readonly" value="0.00" tabindex="-1">
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
							<input class="mandatory read-only" name="presentAdvance" id="presentAdvance" readonly='readonly'>
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row" id="presentPayableLabel">
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
							<input class="mandatory" name="presentPayment"id="presentPayment" value="0.00">
						</div>
						<span id="presentPaymentValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="presentPayment_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_PRESENT_PAYMENT) %></p>
						</div>
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
							<input class="mandatory read-only" name="totalPayable" id="totalPayable" readonly='readonly' value="0.00">
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
							<select name="paymentType" id="paymentType"	class="mandatory" constraints='{"fieldLabel":"Payment Type","mustSelect":"true"}'>
							</select>
						</div>
						<span id="paymentTypeValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="paymentType_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_PAYMENT_TYPE) %></p>
						</div>
					</div>
					</div>
				</div>
				<div class="fieldset"style="height: 10px;">
				<div class="form-row"id="bankname">
				     <div class="label">Bank Name</div>
				    <div class="input-field">
				          <input  name="bankName" id="bankName"/>
				     </div>
				     	<span id="bankNameValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="bankName_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_BANK_NAME) %></p>
						</div>
					</div>
				</div>
				</div>
				 <div class="separator delivery-note-separator"	style="height:30px;"></div>
				<div class="fieldset"style="height: 30px;">
				<div class="form-row"id="chequeno">
				     <div class="label">Cheque No</div>
				    <div class="input-field">
				          <input name="chequeNo" id="chequeNo"/>
				     </div>
				     <span id="chequeNoValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="chequeNo_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_CHEQUE_NO) %></p>
						</div>
					</div>
				</div> 
				</div>
				<div class="separator delivery-note-separator"	style="height:30px;"></div>
				<div class="fieldset"style="height:20px;">
				<div class="form-row"id="branchname">
				     <div class="label">Branch Name</div>
				    <div class="input-field">
				          <input name="branchName" id="branchName"/>
				     </div>
				     <span id="branchnameValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="branchname_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_BRANCH_NAME)%></p>
						</div>
					</div>
				</div>
				</div>
				<div class="separator delivery-note-separator"	style="height:30px;"></div>
				<div class="fieldset"style="height:20px;">
				<div class="form-row"id="location">
				     <div class="label">Bank Location</div>
				    <div class="input-field">
				          <input name="bankLocation" id="bankLocation"/>
				     </div>
				     <span id="bankLocationValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="bankLocation_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.DAY_BOOK_VALIDATE_BANK_LOCATION) %></p>
						</div>
					</div>
				</div>
				
				</div> 
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="save-product-info" type="hidden"id="deliverynoteAction">
							<input name="businessName1" type="hidden" id="businessName1">						
						</div>
					</div>
				</div>
			</div>
		</form>
		<div id="deliverynote-preview-container" class="delivery-note-preview" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 240px; margin-top: 40px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none">Previous</div>
			<div id="button-next" class="ui-btn btn-next">Next</div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;">Save</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	DeliveryNoteHandler.customerChangeRequest();
	DeliveryNoteHandler.initDeliveryNotePageSelection();
	DeliveryNoteHandler.initAddButtons();
	DeliveryNoteHandler.load();
	$(document).ready(function() {
		$('.helppop').hide();
			$('#bankname').hide();
			$('#branchname').hide();
			$('#chequeno').hide();
			$('#location').hide();
			if($('#forPayments').is(':checked')){
				
			}else{
				DeliveryNoteHandler.getPaymentTypesWithOutPayment();
				$('#presentPayment').val("0.00");
			}
				
			
	});

		
</script>

