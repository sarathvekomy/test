<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
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
<%@page import="com.vekomy.vbooks.hibernate.model.VbJournal"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currencyFormat = user.getOrganization().getCurrencyFormat();
    VbJournal vbJournal = null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext
				.getBean("changeTransactionDao");
		if (changeTransactionDao != null) {
			int journalId = Integer.parseInt(request.getParameter("id"));
			vbJournal = changeTransactionDao.getJournalById(journalId , user.getOrganization(),user.getName());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="add-journal-form-container" title="Allot Stock">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-add-icon"></div>
			<div class="page-title employee-add-title"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="edit-journal-form" style="height: 200px;">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 160px;">
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_TYPE)%></div>
						<div class="input-field">
							<select class="mandatory" name="journalType" id="journalType">
							 <option><%=vbJournal.getJournalType()%></option> 
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
							<input class="remove-inputfield-style read-only" readonly="readonly" name="invoiceNo" id="invoiceNo" value="<%=vbJournal.getInvoiceNo()%>">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAMES)%></div>
						<div class="input-field">
							<input class="mandatory "name="businessName" id="businessName" readonly="readonly" value="<%=vbJournal.getBusinessName()%>">
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
							<textarea class="mandatory" name="description" id="description" rows="5" cols="52"><%=vbJournal.getDescription()%></textarea>
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
					<div class="form-row" style="height: 10px; margin-top: 80px;">
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
							<input class="datepicker read-only" name="createdOn" id="createdOn" readonly="readonly" value="<%=DateUtils.format(new java.util.Date())%>">
						</div>
					</div>
					<div class="form-row">
						<div class="label" style="float: left;"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%> (<%=currencyFormat%>)</div>
						<div class="input-field">
							<input class="mandatory" name="amount" id="amount" value="<%=StringUtil.currencyFormat(vbJournal.getAmount())%>">
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
							<input class ="mandatory" name="invoiceName" id="invoiceName" value="<%=vbJournal.getInvoiceName()%>">
						</div>
					</div>
				</div>
				<div id="page-buttons" class="page-buttons">
					<input name="action" value="edit-journal" type="hidden" id="action">
				</div>

				<div id="search-results-container1"
					class="ui-container search-results-container" style="float: left;">
					<div class="ui-content grid-data">
						<div class="green-footer-bar"></div>
					</div>
				</div>
			</div>
		</form>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 230px; margin-top: 100px;">
			<div id="button-save" class="ui-btn btn-save"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	TransactionChangeRequestHandler.initAddJournalCRButtons();
	$('.helppop').hide();
	//find all the form elements in your form,
	//then bind an event handler to all of the elements for the `change` event
	  $('#edit-journal-form').find('input,textarea,select').live('change paste', function (event) {
	    //this now refers to the "changed" element
	    var name  = this.name,
	        value = this.value,
	        id    = this.id,
	        dObj  = {};//create object to pass as data parameter to AJAX request
	    //set the key as the name of the input, and the value as the value of the input
	    dObj[id] = value;
	    TransactionChangeRequestHandler.changedEditJournalBasicInfoFormValues(id,value);
	});
});
	
</script>
