<%@page import="java.util.Date"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>

<div id="system-defined-alerts-add-form-container"
	title="User Defined Alert" >
	<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="user-defined-alerts-form" name="form1"style="height: 300px">
			<div class="add-student-tabs">
			<div class="step-selected">
				<div class="tabs-title" style="padding-left:0px;">Alerts Configuration
				</div>
			</div>
			<div class="step-no-select">
				<div class="step-selected-corner"></div>
				<div class="tabs-title">Alerts Notification
				</div>
			</div>
			<div class="step-no-select">
				<div class="step-no-select-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%>
				</div>
			</div>
			<div class="step-no-select-corner"></div>
		</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
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
							<textarea class="mandatory" rows="3" cols="40" name="description"
								id="alertDescription" style="resize: none;"></textarea>
						</div>
					</div>
					<div class="separator" style="height:40px; margin-left: 60px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%>
						</div>
						<div class="input-field">
							<select class="mandatory" name="alertType" id="alertType">
							</select>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px; margin-left: 60px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label">Created Date
						</div>
						<div class="input-field">
							<input class="remove-inputfield-style mandatory"  readonly="readonly"   name="createdDate" id="createdDate" value="<%=DateUtils.format(new Date())%>">
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 30px;margin-top: 50px">
					<div class="ui-container search-results-container" id="mySales-search-result-list" style="display: none;">
				 	 <div class="label" style="float: left; margin-left: 20px;">My Sales Types:</div>
					<div class="ui-content">
					<div id="mysales-search-results-list" class="green-results-list"
						style="height: 125px; width: 200px; float: left; margin-left: 15px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
					</div>
					</div>
					<div class="ui-container search-results-container" id="salesTypes-search-result-list" style="display: none;">
				 	 <div class="label"  style="float: left; margin-left: 245px;margin-top: -160px">My Sales Pages:</div>
					<div class="ui-content">
					<div id="salesTypes-search-results-list"
						style="height: 125px; width: 200px; float: left; margin-left:250px; margin-top:-125px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
					</div>
					</div>
						
					<div class="form-row" id="trendingAp" style="display: none;">
						<div class="label">Amount percentage</div>
						<div class="input-field">
							<input name="amountPersentage" id="amountPercentage">
						</div>
					</div>
					<div class="form-row" id="trendingPp" style="display: none;">
						<div class="label">Product percentage</div>
						<div class="input-field">
							<input name="productPercentage" id="productPercentage">
						</div>
					</div>
					<div class="form-row" id="excescashRow" style="display: none;">
						<div class="label">Amount</div>
						<div class="input-field">
							<input name="amount" id="amount">
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="user-defined-alerts-form"
								type="hidden" id="alertsAction">
						</div>
					</div>
				</div>
				</div>
		</form>
		<form id="user-defined-alerts-form-notifications" name="form1" style="height: 300px;display: none">
		<div class="add-student-tabs">
			<div class="step-no-select">
				<div class="tabs-title" style="padding-left:0px;">Alerts Configuration</div>
			</div>
			<div class="step-selected">
				<div class="step-no-select-corner"></div>
				<div class="tabs-title" >Alerts Notification</div>
			</div>
			<div class="step-no-select">
				<div class="step-selected-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%></div>
			</div>
			<div class="step-no-select-corner"></div>
		</div>
			 <div class="fieldset-row" style="margin-top: 10px;">	
				<div class="fieldset" style="height: 100px;">
					<div class="separator" style="height: 10px; margin-left: 60px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%>
						</div>
						<div class="input-field">
							<textarea rows="3" cols="40" name="description"id="alertDescription" style="resize: none;"></textarea>
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="save-user-defined-alert-notification" type="hidden" id="alertsAction">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px; margin-left: 60px;"></div>
				<div class="fieldset" style="height: 100px;"></div>
			</div> 
			<div class="label" style="float: left; margin-left: 20px;"><%=Msg.get(MsgEnum.ALERT_NOTIFICATION_LABEL)%></div>
			<div class="label" style="float: left; margin-left: 105px;"><%=Msg.get(MsgEnum.ALERT_GROUP_LABEL)%></div>
			<div class="label" style="float: left; margin-left: 110px;"><%=Msg.get(MsgEnum.ALERT_USERS_LABEL)%></div>
			<div id="notification-search-results-container"
				class="ui-container search-results-container">
				<div class="ui-content">
					<div id="notification-search-results-list" class="green-results-list" name="notificationType"
						style="height: 125px; width: 200px; float: left; margin-left: 15px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
			<div id="groups-search-results-container"
				class="ui-container search-results-container">

				<div class="ui-content" >
					<div id="groups-search-results-list" class="green-results-list" name="group"
						style="height: 125px; width: 200px; float: left; margin-left: 30px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
			<div id="users-search-results-container"
				class="ui-container search-results-container">
				<div class="ui-content">
					<div id="users-search-results-list" class="green-results-list" name="userName"
						style="height: 125px; width: 200px; float: left; margin-left: 30px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
					<div class="green-footer-bar"></div>
				</div>
			</div>
		</form>

        <div id="alerts-preview-container" style="display: none;"></div>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"style="margin-left: 200px; margin-top:70px;">
		<div id="action-assign" class="ui-btn assign-customers-multiple-btn" style="display: none"></div>
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next"></div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
</div>

<script type="text/javascript">
	UserDefinedAlertsHandler.load();
	SystemDefinedAlertsHandler.configuredNotifications();
	$(document).ready(function() {
		UserDefinedAlertsHandler.getAlertTypes();
		UserDefinedAlertsHandler.initAddButtons();
		$('#salesTypes-search-result-list').hide();
	});
</script>
