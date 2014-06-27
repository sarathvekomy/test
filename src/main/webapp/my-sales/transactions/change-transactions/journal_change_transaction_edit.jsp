<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbJournal"%>
<%@page import="com.vekomy.vbooks.mysales.cr.dao.JournalCrDao"%>
<%
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currencyFormat = user.getOrganization().getCurrencyFormat();
%>
<div id="add-journal-form-container" title="Allot Stock">
	<div class="ui-content form-panel full-content">
		<form id="edit-journal-form" style="height: 200px;">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 160px;">
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_TYPE)%></div>
						<div class="input-field">
							<select class="mandatory" name="journalType" id="journalType">
							</select>
						</div>
						<div id="Type_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Choose One Type</p>
						</div>
					    </div>
						 <span id="typeValid"style="float: left; position: absolute; margin-left: -20px; margin-top: 5px"></span>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_INVOICE_NO)%></div>
						<div class="input-field">
							<textarea  readonly="readonly" name="invoiceNo" id="invoiceNo" style="height:auto !important; min-height:30px; border:none; margin-bottom:5px; width:150px !important" ></textarea>
						</div>
					</div>
					<div class="separator" style="height:20px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAMES)%></div>
						<div class="input-field">
							<input class="mandatory "name="businessName" id="businessName" readonly="readonly">
						</div>
						<div id="business-name-suggestions"class="business-name-suggestions" style="z-index: 10;"></div>
						<span id="businessNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<span id="businessNameValid"
						style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="businessName_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Select One BusinessName</p>
						</div>
					</div>
					</div>
					<div class="form-row" style="height: 10px;">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_DESCRIPTION_LABEL)%></div>
						<div class="input-field">
							<textarea class="mandatory" name="description" id="description" rows="5" cols="52"></textarea>
						</div>
						<span id="descValid"
						style="float: left; position: absolute; margin-left:270px; margin-top: 5px"></span>
					<div id="desc_pop" class="helppop" style="display: block;margin-left:555px;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left:5px;">
							<p>You Must Enter Description</p>
						</div>
					</div>
					</div>
					<div class="form-row" style="height: 10px; margin-top: 100px;">
						<div class="label"><%=Msg.get(MsgEnum.CHANGE_REQUEST_DESCRIPTION)%></div>
						<div class="input-field">
							<textarea name="CrDescription" id="crDescription" rows="5" cols="52"></textarea>
						</div>
					</div>
					<div class="separator" style="height:100px;"></div>
				</div>
				<div class="separator" style="height: 160px;"></div>
				<div class="fieldset" style="height: 140px;">
				<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label" style="width: 125px;"><%=Msg.get(MsgEnum.ACCOUNTS_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker read-only" name="createdOn" id="createdOn" readonly="readonly">
						</div>
					</div>
					<div class="form-row">
						<div class="label" style="float: left;"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%> (<%=currencyFormat%>)</div>
						<div class="input-field">
							<input class="mandatory" name="amount" id="amount">
						</div>
						<span id="amountValid"
						style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					<div id="amount_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Amount Can Contain Only Numbers</p>
						</div>
					</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class ="mandatory" name="invoiceName" id="invoiceName">
						</div>
					</div>
				</div>
				<div class="input-field">
							<input type="hidden" name="journalId" id="journalId">
						</div>
				<div id="page-buttons" class="page-buttons">
					<input name="action" value="save-journal-change-request" type="hidden" id="action">
				</div>

				<div id="search-results-container1"
					class="ui-container search-results-container" style="float: left;">
					<div class="ui-content grid-data">
						<div class="green-footer-bar"></div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="input-field">
								<input name="originalInvoiceName"  type="hidden" id="originalInvoiceName">
								<input name="originalJournalAmount"  type="hidden" id="originalJournalAmount">
								<input name="originalJournalDescription"  type="hidden" id="originalJournalDescription">
						</div>
					</div>
				</div>
			</div>
		</form>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 230px; margin-top: 140px;">
			<div id="button-save" class="ui-btn btn-save">Save</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	TransactionChangeRequestHandler.initAddJournalCRButtons();
	$('.helppop').hide();
});
</script>
