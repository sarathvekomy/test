<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<div id="reports-add-form-container" title="Sales Wise Report"
	style="overflow: auto;height:468px !important;background:#fff; width:100%; float:left;">
	<!--<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>-->
	<div class="ui-content form-panel full-content">
		<form id="sales-wise-report-form" style="height: 150px;">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
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
					<div id="salesexecutive-name-suggestions" style="z-index: 10;position: absolute;"
						class="business-name-suggestions"></div>
						<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
						<div class="input-field">
							<input name="businessName" id="businessName" class="mandatory">
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
					<div id="business-name-suggestions" class="business-name-suggestions" style="z-index: 10;position: absolute;"></div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select name="reportType" id="reportType">
								<option value="select">Select</option>
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
						<div class="label"><%=Msg.get(MsgEnum.REPORT_START_DATE_LABEL)%>
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
						<div class="label"><%=Msg.get(MsgEnum.REPORT_END_DATE_LABEL)%>
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
			style="margin-left: 130px; float:left; position: absolute;">
			<div id="sales-action-show" class="ui-btn btn-show">Show</div>
			<div id="sales-action-pdf" class="ui-btn btn-print">Download PDF</div>
			<div id="sales-action-xls" class="ui-btn btn-print">Download CSV</div>
			<div id="sales-action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="sales-action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
		<!-- <div class="fieldset-row" style="margin-top: 100px;">
			<div id="report-container"></div>
		</div> -->
		<div id="report-show-dialog" style="display: none; overflow-y: hidden; overflow-x: scroll;"
			title="Sales Wise Report">
			<div id="report-container"></div>
		</div>
	</div>

</div>
</div>
<script type="text/javascript" src="js/reports/reports.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('.helppop').hide();
		SalesWiseReportHandler.load();
		SalesWiseReportHandler.registorReportShowEvents();
		 var dates = $( "#startDate, #endDate" ).datepicker({
		        defaultDate: "+1w",
		        dateFormat : 'dd-mm-yy',
		        changeMonth: true,
		        numberOfMonths: 1,
				changeMonth: true,
			    changeYear : true,
				numberOfMonths: 1,
				maxDate: 0,

		        onSelect: function( selectedDate ) {
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
						var parseStartDate = $.datepicker.parseDate( 'dd-mm-yy', startDate );
						var parseEndDate = $.datepicker.parseDate( 'dd-mm-yy', endDate );
						if(parseStartDate > parseEndDate) {
							$('#startDate').val(selectedDate);
						}
					}
		        }
		    });
	});
</script>
