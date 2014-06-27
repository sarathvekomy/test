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
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="system-defined-alerts-add-form-container" class = "form1"
	title="System Defined Alert" style="overflow: auto;">
	<!--<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>-->
	<div class="ui-content form-panel full-content">
		<form id="system-defined-alerts-form" name="form1" style="height: 350px">
			<div class="fieldset-row" style="margin-top: 10px; width:100%; float:left; margin-bottom:15px;">
				<div class="fieldset" style="height: 100px; ">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="alertName" id="alertName">
						</div>
						<span id="alertNameValid"
						style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="alertName_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_ALERT_NAME) %></p>
						</div>
					</div>
					<div id="alertNamespaces_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_ALERT_NAME_SPACES) %></p>
						</div>
					</div>
					</div>
					
					<div class="separator" style="height: 10px; "></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%>
						</div>
						<div class="input-field">
							<textarea rows="3" cols="40" name="description"
								id="alertDescription" style="resize: none;"></textarea>
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="save-system-defined-alert"
								type="hidden" id="alertsAction">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px; "></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select class="mandatory" name="alertType" id="alertType">
								<option value="-1">Select</option>
							</select>
						</div>
						<span id="alertTypeValid"
						style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="alertType_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_ALERT_TYPE) %></p>
						</div>
					</div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 140px;"></div>
			<div class="label" style="float: left; margin-left: 20px;"><%=Msg.get(MsgEnum.ALERT_NOTIFICATION_LABEL)%></div>
			<div class="label" style="float: left; margin-left: 105px;"><%=Msg.get(MsgEnum.ALERT_GROUP_LABEL)%></div>
			<div class="label" style="float: left; margin-left: 110px;"><%=Msg.get(MsgEnum.ALERT_USERS_LABEL)%></div>
			<div id="notification-search-results-container"
				class="ui-container search-results-container">

				<div class="ui-content">
					<div id="notification-search-results-list" class="green-results-list"
						style="height: 125px; width: 200px; float: left; margin-left: 15px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
			<div id="groups-search-results-container"
				class="ui-container search-results-container">

				<div class="ui-content">
					<div id="groups-search-results-list" class="green-results-list"
						style="height: 125px; width: 200px; float: left; margin-left: 20px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
			<div id="users-search-results-container"
				class="ui-container search-results-container">
				<div class="ui-content">
					<div id="users-search-results-list" class="green-results-list"
						style="height: 125px; width: 200px; float: left; margin-left: 20px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
		</form>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 40px;">
			
			<div id="button-save" class="ui-btn btn-save">Save</div>
			<div id="button-update" class="ui-btn btn-update"
			style="display: none;">Update</div>
            <div id="action-assign" class="ui-btn assign-customers-multiple-btn">Assign</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>

</div>

<script type="text/javascript">
SystemDefinedAlertsHandler.load();
SystemDefinedAlertsHandler.configuredNotifications();
SystemDefinedAlertsHandler.initAddButtons();
$(document).ready(function() {
	$('.helppop').hide();
});
</script>
