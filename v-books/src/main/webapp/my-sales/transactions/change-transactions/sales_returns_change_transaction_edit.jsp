<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturn"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts"%>
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
<%@page import="com.vekomy.vbooks.util.StringUtil"%>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    VbSalesReturn vbSalesReturn = null;
    VbSalesReturnProducts vbSalesReturnProducts = null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext
				.getBean("changeTransactionDao");
		if (changeTransactionDao != null) {
			int salesReturnId = Integer.parseInt(request.getParameter("id"));
			vbSalesReturn = changeTransactionDao.getSalesReturn(salesReturnId , user.getOrganization(),user.getName());
			Iterator salesReturnProductsIterator = vbSalesReturn.getVbSalesReturnProductses().iterator();
			if (salesReturnProductsIterator.hasNext()) {
				vbSalesReturnProducts = (VbSalesReturnProducts) salesReturnProductsIterator.next();
			}
			if (vbSalesReturnProducts == null) {
				vbSalesReturnProducts = new VbSalesReturnProducts();
			}
			//VbSalesReturn vbSalesReturn = changeTransactionDao.getInvoiceNumberBasedOnBusinessName(vbSalesReturn.getBusinessName(),getUsername(), getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="sales-returns-add-form-container" title="Sales Returns">
	 <div class="green-title-bar">
		<div class="green-title-bar">
				<div class="green-title-bar2">
				</div>
			</div> 
	</div> 
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
							<input  name="invoiceNumber" id="invoiceNumber" value="<%=vbSalesReturn.getInvoiceNo()%>"
								 readonly="readonly" class="read-only">
						</div>
				</div> 
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="businessName"
								id="businessName" value="<%=vbSalesReturn.getBusinessName()%>"
								constraints='{"fieldLabel":"businessName","charsOnly":"true"}'readonly="readonly" class="read-only">
						</div>
					</div>
					<div class="form-row" style="height: 40px;">
						<div class="label"><%=Msg.get(MsgEnum.CHANGE_REQUEST_DESCRIPTION)%></div>
						<div class="input-field">
							<textarea name="description" id="description"rows="3"cols="53"></textarea>
						</div>
				</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_INVOICE_NAME_LABEL)%></div>
						<div class="input-field">
							<input class="mandatory constrained" name="invoiceName"
								id="invoiceName" value="<%=vbSalesReturn.getInvoiceName()%>"
								constraints='{"fieldLabel":"invoiceName","charsNumsOnly":"true"}'>
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row gridDisplay"
				style="margin-top: 30px; display: none;">
				<div class="fieldset" style="height: 200px; width: 699px; margin-top: 60px;">
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
						<div class="input-field-float"><input style="background-color:inherit;  border:0 none; font-weight: bold;" value="<%=StringUtil.currencyFormat(vbSalesReturn.getProductsGrandTotal())%>" name="grandTotalCost" id="grandTotalCost" readonly="readonly" value = "0.00"></div>
					</div> 
					<input name="action" value="save-sales-returns" type="hidden"
						id="salesReturnAction">
						<input name="salesReturnId" value="<%=vbSalesReturn.getId()%>" type="hidden"
								id="salesReturnId">
				</div>
			</div>
		</form>
		<div id="sales-return-preview-container" style="overflow: auto">
		</div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 40px;">
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
TransactionChangeRequestHandler.initSalesReturnAddButtons();
	UserHandler.initCheckUsername();
	UserHandler.initCheckPassword();
	TransactionChangeRequestHandler.salesReturnLoad();
	if(PageHandler.expanded){
		PageHandler.pageSelectionHidden=false;
		PageHandler.hidePageSelection();
	}
	$(document).ready(function() {
		TransactionChangeRequestHandler.loadSalesReturnProductGridBasedOnBusinesName();
		//find all the form elements in your form,
		//then bind an event handler to all of the elements for the `change` event
		  $('#sales-returns-form').find('input').live('change paste', function (event) {
		    //this now refers to the "changed" element
		    var name  = this.name,
		        value = this.value,
		        id    = this.id,
		        dObj  = {};//create object to pass as data parameter to AJAX request
		    //set the key as the name of the input, and the value as the value of the input
		    dObj[id] = value;
		    TransactionChangeRequestHandler.changedFormValues(id,value);
		    //$('#'+id).addClass('changed');
		});
	});
	//TransactionChangeRequestHandler.changedFormValues(listOfObjects);
</script>
