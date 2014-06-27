<%@page import="com.vekomy.vbooks.alerts.command.AlertsResult"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts"%>
<%@page import="com.vekomy.vbooks.alerts.dao.AlertsDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSystemAlerts"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>

<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
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
<%@page import="com.vekomy.vbooks.util.*"%>
<div id="" title="Delete Alerts"></div>
<div class="ui-content form-panel full-content">
	<div class="main-table" Style="width: 550px; margin-left: 20px;">
		<div class="inner-table" Style="width: 550px;">
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label" ><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value alertNameVal" Style="padding-left: 10px;"id="alertName"></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value alertCategoryVal" Style="padding-left: 10px;"id = "alertCategory"></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label" ><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value descriptionVal" Style="padding-left: 10px;" id ="description"></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label" >Notification Type</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value notificationTypeVal" Style="padding-left: 10px;" id ="notificationType"></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label" >Role Name</span>
				</div>
			</div>
			<div class="display-boxes-product-view" >
				<div>
					<span class="property-value roleVal" Style="padding-left: 10px;"id="role"></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"  >Users</span>
				</div>
			</div>
			<div class="display-boxes-product-view" >
				<div>
					<span class="property-value userNameVal" Style="padding-left: 10px;"id="userName"></span>
				</div>
			</div>
		</div>
	</div> 
	</div>
