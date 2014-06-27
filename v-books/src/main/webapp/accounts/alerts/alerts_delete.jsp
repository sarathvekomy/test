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
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
VbSystemAlerts vbSystemAlerts =null;
VbUserDefinedAlerts vbUserDefinedAlerts = null;
int id = Integer.parseInt(request.getParameter("id"));
String category = request.getParameter("category");
ApplicationContext hibernateContext = WebApplicationContextUtils
.getWebApplicationContext(request.getSession().getServletContext());
AlertsDao alertsDao = (AlertsDao) hibernateContext.getBean("alertsDao");
if(category.equals("System Alerts")){
	vbSystemAlerts = alertsDao.getSystemAlertsData(id);
}else if(category.equals("User Defined Alerts")){
	vbUserDefinedAlerts = alertsDao.getUserAlertsData(id);
}
	
%>

<div id="" title="Edit Alerts"></div>
<div class="ui-content form-panel full-content">
<%if(vbSystemAlerts!=null){ %>
	<div class="main-table"
		Style="width: 550px;margin-left:20px;">
		<div class="inner-table" Style="width: 550px;">
		<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbSystemAlerts.getAlertName())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=category%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbSystemAlerts.getVbAlertType().getAlertType())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbSystemAlerts.getDescription())%></span>
				</div>
			</div>	
	</div>
</div>
<%}else if(vbUserDefinedAlerts!=null){ %>
	<div class="main-table"
		Style="width: 550px;margin-left:20px;">
		<div class="inner-table" Style="width: 550px;">
		<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbUserDefinedAlerts.getAlertName())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=category%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbUserDefinedAlerts.getVbAlertType().getAlertType())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%></span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbUserDefinedAlerts.getDescription())%></span>
				</div>
			</div>	
	</div>
</div>
<%} %>
</div>
<script type="text/javascript">
</script>
