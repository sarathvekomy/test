<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<div id="reports-add-form-container" title="Sales Return Wise Report"
	style="overflow: auto;">
	<div class="ui-content form-panel full-content">
		<form id="sales-return-wise-report-form" style="height: 150px;">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 150px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_SALES_EXECUTIVE_LABEL)%>
						</div>
						<div class="input-field">
							<input name="salesExecutive" id="salesExecutive" class="mandatory">
						</div>
						<span id="salesExecutiveValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="salesExecutive_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.SALES_EXECUTIVE_NAME_NOT_AVAILABLE)%></p>
							</div>
						</div>
					</div>
					<div id="salesexecutive-name-suggestions"
						class="business-name-suggestions"></div>
						<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input name="businessName" id="businessName">
						</div>
						<span id="businessNameValid"
							style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="businessName_pop" class="helppop" style="display: block;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 3px;">
								<p><%=Msg.get(MsgEnum.BUSINESS_NAME_NOT_AVAILABLE)%></p>
							</div>
						</div>
					</div>
					<div id="business-name-suggestions" class="business-name-suggestions"></div>
					
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRODUCT_NAME_LABEL)%></div>
						<div class="input-field">
							<select name="productName" id="productName">
								<option value="ALL">ALL</option>
							</select>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%></div>
						<div class="input-field">
							<select name="batchNumber" id="batchNumber">
								<option value="ALL">ALL</option>
							</select>
						</div>
					</div>
					
				</div>
				<div class="separator" style="height: 150px;"></div>
				<div class="fieldset" style="height: 150px;">
				<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_CR_STATUS_LABEL)%>
						</div>
						<div class="input-field">
							<select name="crStatus" id="crStatus">
								<option value="-1">ALL</option>
								<%													
													for(String crStatus: DropDownUtil.getDropDown(DropDownUtil.CRSTATUS_TYPE).keySet()) {%>
								<option value="<%=crStatus %>"><%=DropDownUtil.getDropDown(DropDownUtil.CRSTATUS_TYPE, crStatus) %></option>
								<%} %>
							</select>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_RESALABLE_QTY_LABEL)%></div>
						<div class="input-field" style="width: 140px;">
						<select style="width: 40px;" id="resalableOperator" name="resalableOperator">
							<option value="-1">Select</option>
							<%													
													for(String operatorType: DropDownUtil.getDropDown(DropDownUtil.OPERATOR_TYPE).keySet()) {%>
								<option value="<%=operatorType %>"><%=DropDownUtil.getDropDown(DropDownUtil.OPERATOR_TYPE, operatorType) %></option>
								<%} %>
						</select>
							 <input style="width: 90px; display:none;" name="resalableQty" id="resalableQty">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_DAMAGED_QTY_LABEL)%></div>
						<div class="input-field">
							<select style="width: 40px;" id="damagedOperator" name="damagedOperator">
							<option value="-1">Select</option>
							<%													
													for(String operatorType: DropDownUtil.getDropDown(DropDownUtil.OPERATOR_TYPE).keySet()) {%>
								<option value="<%=operatorType %>"><%=DropDownUtil.getDropDown(DropDownUtil.OPERATOR_TYPE, operatorType) %></option>
								<%} %>
						</select>
							 <input style="width: 90px; display:none;" name="damagedQty" id="damagedQty">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select name="reportType" id="reportType">
								<option value="-1">Select</option>
								<%													
													for(String reportType: DropDownUtil.getDropDown(DropDownUtil.REPORT_TYPE).keySet()) {%>
								<option value="<%=reportType %>"><%=DropDownUtil.getDropDown(DropDownUtil.REPORT_TYPE, reportType) %></option>
								<%} %>
							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
				<div class="form-row">
						<div class="label" id="startDateLbl"><%=Msg.get(MsgEnum.REPORT_START_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="startDate" id="startDate"
								value="<%= DateUtils.format(new java.util.Date()) %>">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label" id="endDateLbl"><%=Msg.get(MsgEnum.REPORT_END_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="endDate" id="endDate"
								value="<%= DateUtils.format(new java.util.Date()) %>">
						</div>
					</div>
				</div>
			</div>
			<div class="form-row">
				<div class="input-field">
					<input id="reportFormat" name="reportFormat" type="hidden">
					<%-- <input id = "organization" name="organization" type="hidden" value = "<%=user.getOrganization()%>"> --%>
				</div>
			</div>
		</form>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 130px; float:left; position: relative;">
			<div id="sales-return-action-show" class="ui-btn btn-show">Show</div>
			<div id="sales-return-action-pdf" class="ui-btn btn-print">Download PDF</div>
			<div id="sales-return-action-xls" class="ui-btn btn-print">Download CSV</div>
			<div id="sales-return-action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="sales-return-action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
		<div id="sales-return-wise-report-show-dialog" style="display: none; overflow-y: hidden;"
			title="Sales Return Wise Report">
			<div id="sales-return-report-container"></div>
		</div>
	</div>

</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('.helppop').hide();
		SalesReturnWiseReportHandler.load();
		SalesReturnWiseReportHandler.registorReportShowEvents();
		var dates = $("#startDate,#endDate" ).datepicker({
	        defaultDate: "+1w",
	        dateFormat : 'dd-mm-yy',
	        changeMonth: true,
	        numberOfMonths: 1,
			changeMonth: true,
		    changeYear : true,
			numberOfMonths: 1,
			maxDate: 0,

	        onSelect: function( selectedDate ) {
	        	if($('#reportType').val() != "Weekly"){
	        		 if(this.id == "startDate") {
			        		var option = "minDate",
			    	                instance = $( this ).data( "datepicker" ),
			    	                date = $.datepicker.parseDate(
			    	                    instance.settings.dateFormat ||
			    	                    $.datepicker._defaults.dateFormat,
			    	                    selectedDate, instance.settings );
			    	            dates.not( this ).datepicker( "option", option, date );
			        	} else {
			        			var startDate = $('#startDate').val();
								var endDate = $('#endDate').val();
								var parseStartDate = $.datepicker.parseDate( 'dd-mm-yy', startDate);
								var parseEndDate = $.datepicker.parseDate( 'dd-mm-yy', endDate );
								if(parseStartDate > parseEndDate) {
									$('#startDate').val(selectedDate);
								}
						}
	        	}
	        	
	        	if($('#reportType').val() == "Weekly"){
	        		 if(this.id == "startDate") {
	        			 var startDate = $('#startDate').val();
			        		var parseStartDate = $.datepicker.parseDate( 'dd-mm-yy', startDate);
			        		parseStartDate.setDate(parseStartDate.getDate()+7);
			        		 var date = new Date(parseStartDate),
			        	        mnth = ("0" + (date.getMonth()+1)).slice(-2),
			        	        day  = ("0" + date.getDate()).slice(-2);
			        	    var parsedEndDate = [ day, mnth, date.getFullYear() ].join("-");
			        	    $("#endDate").datepicker( "setDate",  parsedEndDate.toString());
	        		 }else{
	        			 var endDate = $('#endDate').val();
			        		var parseEndDate = $.datepicker.parseDate( 'dd-mm-yy', endDate);
			        		parseEndDate.setDate(parseEndDate.getDate()-7);
			        		 var date = new Date(parseEndDate),
			        	        mnth = ("0" + (date.getMonth()+1)).slice(-2),
			        	        day  = ("0" + date.getDate()).slice(-2);
			        	    var parsedStartDate = [ day, mnth, date.getFullYear() ].join("-");
			        	    $("#startDate").datepicker( "setDate",  parsedStartDate.toString());
	        		 }
	        		
				 }
	        },
	        beforeShow : function(input, inst){
	        	 if($('#reportType').val() == "Monthly"){
					 $(inst.dpDiv).addClass('calendar-off');  
				 }else{
					 $(inst.dpDiv).removeClass('calendar-off');  
				 }
	        },
	        onClose: function(dateText, inst) {
				 if($('#reportType').val() == "Monthly"){
					 var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
					 var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
					 $(this).datepicker('setDate', new Date(year, month, 1));
				 }else{
						 $('#startDate').datepicker({
							 altFormat : "dd-mm-yy",
							 onSelect: function(dateText, inst){
								 var option = "minDate",
			    	                instance = $( this ).data( "datepicker" ),
			    	                date = $.datepicker.parseDate(
			    	                    instance.settings.altFormat ||
			    	                    $.datepicker._defaults.altFormat,
			    	                    selectedDate, instance.settings );
			    	            $( this ).datepicker( "option", option, date );
							  }
						 });
				 }
			 }
	    }); 
	});
</script>
<style>
.calendar-off table.ui-datepicker-calendar {
    display:none !important;
}
</style>
