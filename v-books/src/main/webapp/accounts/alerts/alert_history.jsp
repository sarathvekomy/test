<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>

<div id="alert-history-form-container" class="ui-container"
	title="Alert History">
	<!--<div class="green-title-bar">
		<div class="green-title-bar2"></div>
	</div>-->

	<div class="ui-content form-panel form-panel-border">
		<form id="alert-history-form">
			<div class="fieldset-row" style="height: 60px; margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%></div>
						<div class="input-field">
							<select name="alertCategory" id="alertCategory">
								<option value=-1>Select</option>
							</select>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select name="alertType" id="alertType">
								<option value=-1>Select</option>
							</select>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input name="alertName" id="alertName">
						</div>
					</div>
				</div>
			</div>

			<div class="fieldset-row" style="height: 60px; margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_START_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="startDate" id="startDate"
								value="<%=DateUtils.format(new java.util.Date())%>">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 60px;"></div>
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_END_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="endDate" id="endDate"
								value="<%=DateUtils.format(new java.util.Date())%>">
						</div>
					</div>
				</div>
			</div>
			<input name="action" value="alerts-history" type="hidden">
		</form>
		<div id="search-buttons" class="search-buttons">
			<div id="action-search-history" class="ui-btn btn-search">Search</div>
			<div id="history-clear" class="ui-btn btn-clear">Clear</div>
		</div>
	</div>
</div>

<div id="search-results-container2"
	class="ui-container search-results-container">

	<div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: 270px; overflow-x: auto;overflow-y: hidden;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
						AlertsHistoryHandler.initAlertHistoryOnload();
						AlertsHandler.getAlertCategory();

						var dates = $("#startDate, #endDate").datepicker({
											defaultDate : "+1w",
											dateFormat : 'dd-mm-yy',
											changeMonth : true,
											numberOfMonths : 1,
											changeMonth : true,
											changeYear : true,
											numberOfMonths : 1,
											maxDate : 0,
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
