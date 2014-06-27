<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNote"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments"%>
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
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    VbDeliveryNote vbDeliveryNote = null;
    VbDeliveryNoteProducts vbDeliveryNoteProducts = null;
    VbDeliveryNotePayments vbDeliveryNotePayments = null; 
    Float grandTotal= new Float(0);
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext
				.getBean("changeTransactionDao");
		if (changeTransactionDao != null) {
			int deliveryNoteId = Integer.parseInt(request.getParameter("id"));
			vbDeliveryNote = changeTransactionDao.getDeliveryNote(deliveryNoteId , user.getOrganization(),user.getName());
			Iterator deliveryNotePaymentsIterator = vbDeliveryNote.getVbDeliveryNotePaymentses().iterator();
			if (deliveryNotePaymentsIterator.hasNext()) {
				vbDeliveryNotePayments = (VbDeliveryNotePayments) deliveryNotePaymentsIterator.next();
			}
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
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
				<div class="form-row" style="height: 50px;">
						<div class="label"><%=Msg.get(MsgEnum.INVOICE_NUMBER_LABEL)%></div>
						<div class="input-field">
							<input  name="invoiceNumber" id="invoiceNumber" value="<%=vbDeliveryNote.getInvoiceNo()%>" readonly="readonly" class="read-only">
						</div>
					</div>
					<div class="form-row" style="height: 50px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="businessName" id="businessName" value="<%=vbDeliveryNote.getBusinessName()%>" readonly="readonly" class="read-only">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 70px;"></div>
				<div class="fieldset" style="height: 70px;">
					<div class="form-row" style="height: 50px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_DATE_LABEL)%></div>
						<div class="input-field">
							<input name="invoiceDate" id="invoiceDate" value='<%=DateUtils.format(new Date())%>' disabled="true">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input name="invoiceName" id="invoiceName" value="<%=vbDeliveryNote.getInvoiceName()%>">
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
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PREVIOUS_CREDIT_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="previousCredit"
								id="previousCredit" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getPreviousCredit())%>">
						</div>
					</div>
				</div>
				<div class="separator delivery-note-separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 40px; width: 320px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_ADVANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="presentAdvance"
								id="presentAdvance" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getPresentAdvance())%>">
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_PAYABLE_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="presentPayable"
								id="presentPayable" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getPresentPayable())%>">
						</div>
					</div>
				</div>
				<div class="separator delivery-note-separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 40px; width: 320px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRESENT_PAYMENT_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory" name="presentPayment"
								id="presentPayment" value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getPresentPayment())%>">
						</div>
					</div>
				</div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height:40px;">
					<div class="form-row" style="height: 40px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_TOTAL_PAYABLE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="totalPayable" id="totalPayable"
								readonly='readonly' value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getTotalPayable())%>">
						</div>
					</div>
					<div class="form-row" style="height:40px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select name="paymentType" id="paymentType"
								class="mandatory constrained"
								constraints='{"fieldLabel":"Payment Type","mustSelect":"true"}'>
								<option><%=vbDeliveryNotePayments.getPaymentType()%></option>
							</select>
						</div>
					</div>
				</div>
				<div class="separator delivery-note-separator"
					style="height:40px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row" style="height:40px;">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="balance" id="balance"
								readonly='readonly' value="<%=StringUtil.currencyFormat(vbDeliveryNotePayments.getBalance())%>">
						</div>
					</div>
				</div>
				<div class="form-row" style="height: 40px; margin-top: 30px;">
						<div class="label"><%=Msg.get(MsgEnum.CHANGE_REQUEST_DESCRIPTION)%></div>
						<div class="input-field">
							<textarea name="description" id="description"rows="4"cols="60"></textarea>
						</div>
				</div>
				</div>
				<div class="fieldset-row" style="margin-top: 20px;">
				<div class="fieldset"style="height: 30px; margin-top: 110px; margin-left: -280px;">
				<div class="form-row"id="branchname">
				     <div class="label">Branch Name</div>
				    <div class="input-field">
				          <input name="branchName" id="branchName"/>
				     </div>
				</div>
				<div class="form-row"id="bankname">
				     <div class="label">Bank Name</div>
				    <div class="input-field">
				          <input  name="bankName" id="bankName"/>
				     </div>
				</div>
				</div>
				<div class="separator delivery-note-separator"
					style="height: 5px;"></div>
			<div class="fieldset"style="height:20px; margin-top: 110px;">
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
								<input name="deliveryNoteId" value="<%=vbDeliveryNote.getId()%>" type="hidden"
								id="deliveryNoteId">
								<input name="salesId" value="<%=vbDeliveryNote.getVbSalesBook().getId()%>" type="hidden"
								id="salesId">
								<input name="paymentTypeValue" value="<%=vbDeliveryNotePayments.getPaymentType()%>" type="hidden"
								id="paymentTypeValue">
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
TransactionChangeRequestHandler.initDeliveryNotePageSelection();
TransactionChangeRequestHandler.initDeliveryNoteAddButtons();
TransactionChangeRequestHandler.deliveryNoteLoad();
	UserHandler.initCheckUsername();
	UserHandler.initCheckPassword();
	$(document).ready(function() {
		$('#bankname').hide();
		$('#branchname').hide();
		$('#chequeno').hide();
		  $('#deliverynote-form').find('input').live('change paste', function (event) {
		    //this now refers to the "changed" element
		    var name  = this.name,
		        value = this.value,
		        id    = this.id,
		        dObj  = {};//create object to pass as data parameter to AJAX request
		    //set the key as the name of the input, and the value as the value of the input
		    dObj[id] = value;
		    TransactionChangeRequestHandler.changedDeliveryNoteBasicInfoFormValues(id,value);
		    //$('#'+id).addClass('changed');
		});
		  $('#deliverynote-product-form').find('input,select').live('change paste', function (event) {
			    //this now refers to the "changed" element
			    var name  = this.name,
			        value = this.value,
			        id    = this.id,
			        dObj  = {};//create object to pass as data parameter to AJAX request
			    //set the key as the name of the input, and the value as the value of the input
			    dObj[id] = value;
			    TransactionChangeRequestHandler.changedDeliveryNoteBasicInfoFormValues(id,currencyHandler.convertStringPatternToFloat(value));
			    //$('#'+id).addClass('changed');
			});
	});
		
</script>

