<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.alerts.dao.AlertsDao"%>
<%@page import="com.vekomy.vbooks.alerts.command.AlertsCommand"%>
<%@page import="com.vekomy.vbooks.alerts.action.AlertsAction"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbUserDefinedAlertsNotifications"%>

<%@page	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%
User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
VbUserDefinedAlerts	userDefinedAlerts =null;
String salesTypePage=null;
AlertsCommand alertsCommand = null;
String notificationType = null;
String group = null;
String userNames = null;
VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
	 
		alertsCommand = (AlertsCommand)session.getAttribute("save-user-defined");
		 salesTypePage = (String)session.getAttribute("sales-types");
		
		notificationType = (String)session.getAttribute("notification-types");
		group = (String) session.getAttribute("group");
		userNames = (String) session.getAttribute("userNames");
	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			AlertsDao alertsDao =(AlertsDao) hibernateContext.getBean("alertsDao");
		}
		catch (Exception exx) {
			exx.printStackTrace();
		}
	}
%>
<div class="add-student-tabs">
	<div class="step-no-select">
		<div class="tabs-title"style="padding-left:0px;">Alerts Configuration
		</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title">Alerts Notification</div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%></div>
	</div>
	<div class="step-selected-corner"></div>
</div>
 <div class="table-field" style="height: 65px;">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alert Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=alertsCommand.getAlertName()%></span>
				</div>
			</div>
		
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getDescription())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alert Type</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getAlertType())%></span>
					</div>
			</div>
					<%if (alertsCommand.getAlertType().equals("My Sales")){ %>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">My Sales Type</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getAlertMySales())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">My Sales Page Type</span>
				</div>
			</div>
			<div class="display-boxes">
						<%if(salesTypePage !=null) {%>
			
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=salesTypePage%></span>
				</div>
				<%} %>
			</div>
					
					<%}else if (alertsCommand.getAlertType().equals("Trending")){ %>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Product Percentage</span>
				</div>
			</div>
			<div class="display-boxes">
			<%if(alertsCommand.getProductPercentage()!=null){ %>
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getProductPercentage())%></span>
				</div>
				<%} %>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Amount Percentage</span>
				</div>
			</div>
			<div class="display-boxes">
			<% if(alertsCommand.getAmountPercentage()!=null){ %>
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getAmountPercentage())%></span>
				</div>
				<%} %>
			</div>
			<%}else if(alertsCommand.getAlertType().equals("Excess Amount")){ %>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Amount</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(alertsCommand.getAmount())%></span>
				</div>
			</div>
			<%} %>
			</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Notification</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=notificationType%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Groups</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=group%></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Users</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=userNames%></span>
				</div>
			</div>	
			</div>
			</div>
</div> 

