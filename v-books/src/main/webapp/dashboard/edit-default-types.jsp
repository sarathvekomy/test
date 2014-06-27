<%@page import="com.vekomy.vbooks.organization.dao.SyStemDefaultsDao"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.hibernate.model.*"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
	VbRole vbRole = null;
	VbPaymentTypes vbPaymentTypes = null;
	VbAddressTypes vbAddressTypes = null;
	VbJournalTypes vbJournalTypes = null;
	String type = request.getParameter("type");
	int id = Integer.parseInt(request.getParameter("id"));

	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		SyStemDefaultsDao syStemDefaultsDao = (SyStemDefaultsDao) hibernateContext
				.getBean("systemDefaultsDao");
		if (syStemDefaultsDao != null) {
			if (type.equals("EmployeeType")) {
				vbRole = (VbRole) syStemDefaultsDao
						.getEmployeetypeById(id);
			} else if (type.equals("Address Type")) {
				vbAddressTypes = (VbAddressTypes) syStemDefaultsDao
						.getAddTypeById(id);
			} else if (type.equals("Payment Type")) {
				vbPaymentTypes = (VbPaymentTypes) syStemDefaultsDao
						.getPayTypeById(id);
			}else if(type.equals("Journal Type")){
				vbJournalTypes =(VbJournalTypes) syStemDefaultsDao.getVbJournalTypeByid(id);
			}
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="employee-search-form-container" class="ui-container"
	title="Add Default Types">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-search-icon"></div>
			<div class="page-title employee-search-title"></div>
		</div>
	</div>

	<div class="ui-content form-panel form-panel-border"
		style="height: 150px;">
		<form name="form2" id="employee-search-form">
			<%
				if (type.equals("EmployeeType")) {
			%>
			<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES)%></div>
						<div class="input-field">
							<select class="mandatory" name="types" id="types">
								<option value="-1">Select</option>
								<option value="Employee Type" selected="selected">EmployeeType</option>
								<option value="Payment Type">Payment Type</option>
								<option value="Address Type">Address Type</option>
								<option value="Address Type">Journal Type</option>
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
					<div class="label"><%=Msg.get(MsgEnum.DESCRIPTION)%></div>
					<div class="input-field">
						<textarea name="description" id="description" cols="16"rows="2"style="overflow-x:hidden;resize:none; width: 145px;"><%=vbRole.getDescription()%></textarea>
					</div>
				</div>
					
				</div>
			<div class="separator" style="height: 80px;"></div>
			<div class="fieldset" style="height: 80px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DEFAULT_VALUE)%></div>
					<div class="input-field">
						<input class="mandatory" name="value" id="value" value="<%=vbRole.getRoleName()%>">
					</div>
				</div>
				<div class="seperator" style="height: 40px;"></div>
			</div>
			</div>
	<%
		} else if (type.equals("Address Type")) {
	%>
	<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
		<div class="fieldset" style="height: 120px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES)%></div>
				<div class="input-field">
					<select name="types" id="types">
						<option value="-1">Select</option>
						<option value="Employee Type">EmployeeType</option>
						<option value="Payment Type">Payment Type</option>
						<option value="Address Type" selected="selected">Address
							Type</option>
							<option value="Address Type" selected="selected">Journal
							Type</option>
					</select>
				</div>
				<div id="Type_pop" class="helppop" style="display: block;"
					aria-hidden="false">
					<div id="namehelp" class="helpctr"
						style="float: left; margin-left: 3px;">
						<p>Choose One Type</p>
					</div>
				</div>
			</div>
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DESCRIPTION)%></div>
				<div class="input-field">
					<textarea name="description" id="description" cols="16"rows="2"style="overflow-x:hidden;resize:none; width: 145px;"><%=vbAddressTypes.getDescription()%></textarea>
				</div>
				<div id="desc_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Description can have only letters, spaces.</p>
				</div>
			</div>
			</div>
		</div>
		<div class="separator" style="height: 80px;"></div>
		<div class="fieldset" style="height: 80px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_VALUE)%></div>
				<div class="input-field">
					<input class="mandatory" name="value" id="value"
						value="<%=vbAddressTypes.getAddressType()%>">
				</div>
			</div>
			<div id="value_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Value Name can have only letters, Spaces.</p>
				</div>
			</div>
			<div class="seperator" style="height: 40px;"></div>
		</div>
	</div>
	<%
		} else if (type.equals("Payment Type")) {
	%>
	<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
		<div class="fieldset" style="height: 120px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES)%></div>
				<div class="input-field">
					<select name="types" id="types">
						<option value="-1">Select</option>
						<option value="Employee Type">EmployeeType</option>
						<option value="Payment Type" selected="selected">Payment
							Type</option>
						<option value="Address Type">Address Type</option>
							<option value="Address Type">Journal Type</option>
					</select>
				</div>
				<div id="Type_pop" class="helppop" style="display: block;"
					aria-hidden="false">
					<div id="namehelp" class="helpctr"
						style="float: left; margin-left: 3px;">
						<p>Choose One Type</p>
					</div>
				</div>
				<span id="typeValid"
					style="float: left; position: absolute; margin-left: -20px; margin-top: 5px"></span>
			</div>
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DESCRIPTION)%></div>
				<div class="input-field">
					<textarea name="description" id="description"cols="16"rows="2" style="resize:none; width: 145px;"><%=vbPaymentTypes.getDescription()%></textarea>
					
				</div>
			</div>
			<div id="desc_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Description can have only letters, spaces.</p>
				</div>
			</div>
			<span id="descValid"
				style="float: left; position: absolute; margin-left: 285px; margin-top: 5px"></span>
		</div>
		<div class="separator" style="height: 80px;"></div>
		<div class="fieldset" style="height: 80px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_VALUE)%></div>
				<div class="input-field" class="mandatory">
					<input name="value" id="value" class="mandatory"
						value="<%=vbPaymentTypes.getPaymentType()%>">
				</div>
			</div>
			<div id="value_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Value name can have only letters, spaces.</p>
				</div>
			</div>
			 <div id="payvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Payment type  already exists</p>
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
							<p>Employee type is already exists</p>
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
				style="float: left; position: absolute; margin-left: 285px; margin-top: 5px"></span>
			<div class="seperator" style="height: 40px;"></div>
		</div>
	</div>
	<%
		}
		 else if (type.equals("Journal Type")) {
	%>
	<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
		<div class="fieldset" style="height: 120px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES)%></div>
				<div class="input-field">
					<select name="types" id="types">
						<option value="-1">Select</option>
						<option value="Employee Type">EmployeeType</option>
						<option value="Journal Type" selected="selected">Journal
							Type</option>
						<option value="Address Type">Address Type</option>
					</select>
				</div>
				<div id="Type_pop" class="helppop" style="display: block;"
					aria-hidden="false">
					<div id="namehelp" class="helpctr"
						style="float: left; margin-left: 3px;">
						<p>Choose one type</p>
					</div>
				</div>
				<span id="typeValid"
					style="float: left; position: absolute; margin-left: -20px; margin-top: 5px"></span>
			</div>
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DESCRIPTION)%></div>
				<div class="input-field">
					<textarea name="description" id="description"cols="16"rows="2" style="resize:none; width: 145px;"><%=vbJournalTypes.getDescription()%></textarea>
					
				</div>
			
			<div id="desc_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Description can have only letters, Spaces.</p>
				</div>
			</div>
			<span id="descValid"
				style="float: left; position: absolute; margin-left: 285px; margin-top: 5px"></span>
		</div>
		</div>
		<div class="separator" style="height: 80px;"></div>
		<div class="fieldset" style="height: 80px;">
			<div class="form-row">
				<div class="label"><%=Msg.get(MsgEnum.DEFAULT_VALUE)%></div>
				<div class="input-field" class="mandatory">
					<input name="value" id="value" class="mandatory"
						value="<%=vbJournalTypes.getJournalType()%>">
				</div>
			</div>
			<div id="value_pop" class="helppop" style="display: block;"
				aria-hidden="false">
				<div id="namehelp" class="helpctr"
					style="float: left; margin-left: 3px;">
					<p>Value Name can have only letters, spaces.</p>
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
							<p>Address type  already exists</p>
						</div>
					    </div>
					    <div id="empvalue_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Employee type is already exists</p>
						</div>
					    </div>
					     <div id="valuelen_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Value should not exceed 30 characters.</p>
						</div>
					    </div>
					     <div id="valuesp_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Spaces should not be accepted as suffix or prefix </p>
						</div>
					    </div>
			<span id="valueValid"
				style="float: left; position: absolute; margin-left: 285px; margin-top: 5px"></span>
				<div class="form-row" id="invoiceNum">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_INVOICE_NO)%>
						</div>
						<div class="input-field">
							<input class ="mandatory" name="invoiceNo"id="invoiceNo" value="<%=vbJournalTypes.getInvoiceNo()%>">
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
			<div class="seperator" style="height: 40px;"></div>
		</div>
	</div>
	<%
		}
	%>
	<input type="hidden" name="id" id="idVal" value="<%=id%>">
	</form>
	<div id="page-buttons" class="page-buttons"
		style="margin-left: 200px; margin-top: 15px;">
		<div id="button-update" class="ui-btn btn-update"></div>
		<div id="action-clear" class="ui-btn btn-clear"></div>
		<div id="action-cancel" class="ui-btn btn-cancel"></div>
	</div>
</div>
</div>
<div id="employee-view-dialog" style="display: none;"
	title="Employee Details">
	<div id="employee-view-container"></div>
</div>
<div id="search-results-container2"
	class="ui-container search-results-container">

	<div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: 315px;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div>
<script>
	DashbookHandler.initAddButtons();
	$(document).ready(function() {
		$('.helppop').hide();
	});
</script>
