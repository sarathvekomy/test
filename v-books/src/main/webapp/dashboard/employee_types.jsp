<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="employee-search-form-container" class="ui-container"
	title="Add Default Types">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-search-icon"></div>
			<div class="page-title employee-search-title"></div>
		</div>
	</div>

<div class="ui-content form-panel form-panel-border"style="height: 170px;">												
						<form name="form2"id="employee-search-form">
							<div class="fieldset-row" style="height: 80px;margin-top: 10px;">
						<div class="fieldset" style="height:120px;">
						<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES) %></div>
										<div class="input-field"><select class="mandatory" name="types" id="types">
										<option value="-1">Select</option>
										<option value="Employee Type">Employee Type</option>
										<option value="Payment Type">Payment Type</option>
										<option value="Address Type">Address Type</option>
										<option value="Journal Type">Journal Type</option>
										</select></div>
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
						       <div class="label"><%=Msg.get(MsgEnum.DESCRIPTION) %></div>
						           <div class="input-field">
							          <textarea name="description" id="description" cols="15"rows="2"style="resize:none;"></textarea>
						           </div>
					          </div>
					           <div id="desc_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Description  can have only letters, Spaces.</p>
						</div>
					    </div>
					          <span id="descValid"
							style="float: left; position: absolute; margin-left: -2px; margin-top:35px"></span>
						</div>
								<div class="separator" style="height:80px;"></div>								
								<div class="fieldset" style="height:80px;">
								<div class="form-row">
						       <div class="label"><%=Msg.get(MsgEnum.DEFAULT_VALUE) %></div>
						           <div class="input-field">
							          <input class="mandatory" name="value" id="value">
						           </div>
					          </div>
					   <div class="form-row" id="invoiceNum">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_INVOICE_NO)%>
						</div>
						<div class="input-field">
							<input name="invoiceNo"id="invoiceNo">
						</div>
						  <div id="invoiceNo_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>InvoiceNo  can have only alphabates and no spaces at prefix and suffix</p>
						</div>
					    </div>
					          <span id="invoiceNoValid"
							style="float: left; position: absolute; margin-left:5px; margin-top: 5px"></span>
					</div>
					       <div id="value_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Value name can have only letters, spaces</p>
						</div>
					    </div>
					    <div id="valuesp_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Spaces should not be accepted as suffix or prefix </p>
						</div>
					    </div>
					      <div id="payvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Payment type already exists</p>
						</div>
					    </div>
					    <div id="addvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Address type already exists</p>
						</div>
					    </div>
					    <div id="empvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Employee type already exists</p>
						</div>
					    </div>
					    <div id="journalvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Journal type already exists</p>
						</div>
					    </div>
					     <div id="valuelen_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Value should not exceed 30 characters.</p>
						</div>
					    </div>
					          <span id="valueValid"
							style="float: left; position: absolute; margin-left: 285px; margin-top: 5px;"></span>
					       <div class="seperator"style="height:40px;"></div>
								</div>
							</div>	
		</form>
			<div id="page-buttons" class="page-buttons"
		style="margin-left: 200px; margin-top:50px;">
			<div id="button-save" class="ui-btn btn-save"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
		<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<div id="employee-view-dialog" style="display: none;"
	title="Employee Details">
	<div id="employee-view-container"></div>
</div>
<div id="search-results-container2" class="ui-container search-results-container" >
<div class="ui-content" style="height: 250px;overflow-y:auto;">
					<div class="pay-ui ui-content">
						<div id="pay-search-results-list" class="green-results-list" style="border:0px none;margin-top:auto;height: auto;overflow-x:hidden;overflow-y:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
					<div class="emp-ui ui-content">
						<div id="emp-search-results-list" class="green-results-list" style="border:0px none;margin-top:auto;height: auto;overflow-x:hidden;overflow-y:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
					<div class="add-ui ui-content">
						<div id="add-search-results-list" class="green-results-list" style="border:0px none;margin-top:auto;height: auto;overflow-x:hidden;overflow-y:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
					<div class="journal-ui ui-content">
						<div id="journal-search-results-list" class="green-results-list" style="border:0px none;margin-top:auto;height: auto;overflow-x:hidden;overflow-y:hidden;"></div>
						<div class="green-footer-bar"></div>
					</div>
					
</div>
</div>
<script type="text/javascript">
DashbookHandler.initAddButtons();
	$(document).ready(function() {
		$('.helppop').hide();
		$('#invoiceNum').hide();
	});
DashbookHandler.getAllExistedTypes();
</script>