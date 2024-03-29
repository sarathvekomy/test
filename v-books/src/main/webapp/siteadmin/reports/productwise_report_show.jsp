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
<div id="reports-add-form-container" title="Product Wise Report" style="overflow: auto;">
	 <div class="green-title-bar">
		<div class="green-title-bar">
				<div class="green-title-bar2">
				</div>
			</div> 
	</div> 
	<div class="ui-content form-panel full-content">
		<form id="product-wise-report-form" style="height: 150px">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
				<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.ORGANIZATION_LABEL)%></div>
							<div class="input-field">
								<select name="organization" id="organization" class="mandatory"
									constraints='{"fieldLabel":"Organization Type"}'>
									<option value=-1>Select</option>
								</select>
							</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PRODUCT_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<select name="productName" id="productName">
								<option value="-1" selected="selected">Select</option>
							</select>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
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
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_START_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="startDate" id="startDate" value="<%= DateUtils.format(new java.util.Date()) %>">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.REPORT_END_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="endDate" id="endDate" value="<%= DateUtils.format(new java.util.Date()) %>">
						</div>
					</div>
				</div>
			</div>
			  <div class="form-row">
				<div class="input-field">
					<input id = "reportFormat" name="reportFormat" type="hidden">
				</div>
			</div>  
		</form>
		<div style="float: left;width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 130px; margin-top: 10px; position: relative;">
			<button id="action-show" class="ui-btn btn-show">Show</button>
			<button id="action-pdf" class="ui-btn btn-print">Download PDF</button>
			<button id="action-xls" class="ui-btn btn-print">Download CSV</button>
			<button id="action-clear" class="ui-btn btn-clear">Clear</button>
			<button id="action-cancel" class="ui-btn btn-cancel">Cancel</button>
		</div>
		<!-- <div class="fieldset-row" style="margin-top: 100px;">
			<div id="report-container"></div>
		</div> -->
		<div id="report-show-dialog" style="display: none; overflow-y: hidden;"
			title="Product Wise Report">
			<div id="report-container"></div>
		</div>
	</div>

</div>
</div>

<script type="text/javascript">
	
	$(document).ready(function() {
		AdminProductWiseReportHandler.load();
		AdminProductWiseReportHandler.registorReportShowEvents();
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
