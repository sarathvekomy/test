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
	<!--<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>-->
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
						<span id="alertNameValid"style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
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
					<div class="separator" style="height: 10px; margin-left: 60px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%>
						</div>
						<div class="input-field">
							<textarea class="mandatory" rows="3" cols="40" name="description"
								id="alertDescription" style="resize: none;"></textarea>
						</div>
						<span id="descriptionValid"style="float: left; position: absolute; margin-left:155px; margin-top:5px"></span>
					<div id="desc_pop" class="helppop" style="display: block;margin-left: 438px;"aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 12px;">
							<p>You Must Enter Description</p>
						</div>
					</div>
					</div>
					<div class="separator" style="height:40px; margin-left: 60px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%>
						</div>
						<div class="input-field">
							<select class="alerttype mandatory" name="alertType" id="alertType">
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
							<div id="mysales-search-results-list" class="green-results-list"style="height: 125px; width: 200px; float: left; margin-left: 15px; overflow-x: hidden; border-radius: 5px; background: white;"></div>
							<div class="green-footer-bar">
							</div>
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
						<span id="amountPercentValid"style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="amountP_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Amount percentage can contain only numbers and should not be greater than 100</p>
						</div>
					</div>
					</div>
					<div class="form-row" id="trendingPp" style="display: none;">
						<div class="label">Product percentage</div>
						<div class="input-field">
							<input name="productPercentage" id="productPercentage">
						</div>
						<span id="productPercentValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="product_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Product percentage can contain only numbers and should not be greater than 100</p>
						</div>
					</div>
					</div>
					<div class="form-row" id="excescashRow" style="display: none;">
						<div class="label">Amount</div>
						<div class="input-field">
							<input name="amount" id="amount">
						</div>
						<span id="amountValid" style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="amount_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p>Amount can contain only numbers</p>
						</div>
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
							<textarea rows="3" cols="40" name="description"id="alertDescription" ></textarea>
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
		
			<div class="separator" style="height: 140px;"></div>
			<div style="float:left; width:800px;position:relative;">
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
		</div>
		</form>

        <div id="alerts-preview-container" style="display: none;"></div>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"style="margin-left: 200px; margin-top:100px;">
		<div id="action-assign" class="ui-btn assign-customers-multiple-btn" style="display: none">Assign</div>
			<div id="button-prev" class="ui-btn btn-prev" style="display: none">Prev</div>
			<div id="button-next" class="ui-btn btn-next">Next</div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;">Save</div>
			<div id="button-update" class="ui-btn btn-update"
			style="display: none;">Update</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
</div>

<script type="text/javascript">
 	UserDefinedAlertsHandler.load();
	SystemDefinedAlertsHandler.configuredNotifications();
	UserDefinedAlertsHandler.getAlertTypes();
	UserDefinedAlertsHandler.initAddButtons(); 
	$(document).ready(function() {
		$('#salesTypes-search-result-list').hide();
			$('.helppop').hide();
	});
</script>
