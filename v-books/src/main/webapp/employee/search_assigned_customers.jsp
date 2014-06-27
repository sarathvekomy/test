<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="employee-search-form-container" class="ui-container"
	title="Search Employee">
	<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-search-icon"></div>
			<div class="page-title employee-search-title"></div>
		</div>
	</div>

	<div class="ui-content form-panel form-panel-border">
		<form name="form2" id="search-form" style="height: 60px;">
			<div class="fieldset-row" style="height: 40px; margin-top: 10px;">
				<div class="fieldset" style="height: 20px;">
					<div class="form-row">
						<div class="label">Employee Names</div>
						<div class="input-field">
							<input name="userName" id="userName">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 20px;">
					<div class="form-row">
						<div class="label">Business Names</div>
						<div class="input-field">
							<input id="businessName" name="businessName"/>
						</div>
					</div>
				</div>
				<!-- <div class="separator" style="height: 60px;"></div> -->
			</div>
			<input name="action" value="search-assigned-customers" type="hidden"
				id="employeeAction">
		</form>
		<div id="search-buttons" class="search-buttons">
			<div id="action-search-assigned-customer" class="ui-btn btn-search"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
		</div>
	</div>
</div>
<div id="employee-view-dialog" style="display: none;"
	title="Employee Details">
	<div id="employee-view-container"></div>
</div>
<div id="employee-delete-dialog" style="display: none;"
	title="Employee Details">
	<div id="employee-delete-container"></div>
</div>

<div id="search-results-container2"
	class="ui-container search-results-container">

	<div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: 315px;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div>
<script type="text/javascript">
	assignCustomerHandler.load();
	assignCustomerHandler.initSearchAssignedCustomer();
	$(document).ready(function(){
		assignCustomerHandler.searchonload();
	})
</script>
