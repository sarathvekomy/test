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
<div id="system-defined-alerts-add-form-container"
	title="System Defined Alert" style="overflow: auto;">
	<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="system-defined-alerts-form" name="form1" style="height: 350px">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory" name="alertName" id="alertName">
						</div>
					</div>
					
					<div class="separator" style="height: 10px; margin-left: 60px;"></div>
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
				<div class="separator" style="height: 100px; margin-left: 60px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%>
						</div>
						<div class="input-field">
							<select class="mandatory" name="alertType" id="alertType">
								<option value="-1">Select</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="separator" style="height: 140px; margin-left: 60px;"></div>
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
						style="height: 125px; width: 200px; float: left; margin-left: 30px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
			<div id="users-search-results-container"
				class="ui-container search-results-container">
				<div class="ui-content">
					<div id="users-search-results-list" class="green-results-list"
						style="height: 125px; width: 200px; float: left; margin-left: 30px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
		</form>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 10px;">
			<div id="action-assign" class="ui-btn assign-customers-multiple-btn"></div>
			<div id="button-save" class="ui-btn btn-save"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>

</div>

<script type="text/javascript">
SystemDefinedAlertsHandler.load();
SystemDefinedAlertsHandler.configuredNotifications();
SystemDefinedAlertsHandler.initAddButtons();
</script>
