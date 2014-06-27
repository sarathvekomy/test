<%@page import="com.vekomy.vbooks.siteadmin.dao.SiteAdminDao"%>
<%@page
	import="com.vekomy.vbooks.organization.command.OrganizationCommand"%>
<%@page
	import="com.vekomy.vbooks.organization.command.OrganizationSuperUserCommand"%>
<%@page import="com.vekomy.vbooks.organization.*"%>
<%@page import="com.vekomy.vbooks.siteadmin.*"%>
<%@page import="com.vekomy.vbooks.hibernate.model.*"%>
<%@page import="com.vekomy.vbooks.hibernate.model.*"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	/* String currency=user.getOrganization().getCurrencyFormat(); */
	OrganizationCommand organizationCommand = null;
	OrganizationSuperUserCommand organizationSuperUserCommand = null;
	VbCustomerDetail customerDetail = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		organizationCommand = (OrganizationCommand) session.getAttribute("save-basic");
		organizationSuperUserCommand = (OrganizationSuperUserCommand) session.getAttribute("save-detail");
	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			SiteAdminDao siteAdminDao = (SiteAdminDao) hibernateContext.getBean("siteadminDao");
		} catch (Exception exx) {
			exx.printStackTrace();
		}
	}
%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%=pageTitle%>
<%
	if (preview == true) {
%>
<div class="add-student-tabs" style="height: 20px; margin-top: 2px;">
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.ORGANIZATION_TAB)%></div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.ORGANIZATION_SUPER_USER)%></div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div>
	</div>
	<div class="step-selected-corner"></div>
</div>
<%
	}
%>

<div class="table-field" style="height: 75px;">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Main Branch</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=organizationCommand.getMainBranch()%></span>
				</div>
			</div>

			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Main Branch Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=StringUtil.format(organizationCommand.getMainBranchName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Currency Format</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=StringUtil.format(organizationCommand.getCurrencyFormat())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organization Code</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=organizationCommand.getOrganizationCode()%></span>
				</div>
			</div>

			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organization Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=StringUtil.format(organizationCommand.getName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Branch Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=StringUtil.format(organizationCommand.getBranchName())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getAddressLine1())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getAddressLine2())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getLocality())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">LandMark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getLandmark())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationCommand.getPhone1())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getCity())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Country</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationCommand.getCountry())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">ZipCode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=organizationCommand.getZipcode()%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word;"><%=StringUtil.format(organizationCommand.getPhone2())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">UserName</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationSuperUserCommand.getSuperUsername())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">UserNamePrefix</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationCommand.getUsernamePrefix())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Full Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 2px; word-wrap: break-word"><%=StringUtil.format(organizationSuperUserCommand
					.getFullName())%></span>
				</div>
			</div>

			<div class="display-boxes-colored">
				<div>
					<span class="span-label" style="height: 40px;">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationSuperUserCommand.getEmail())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label" style="height: 40px;">AlternateMobile</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationSuperUserCommand
					.getAlternateMobile())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored" style="height: 70px;">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<div class="display-boxes" style="height: 70px;">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationCommand.getDescription())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label" style="height: 40px;">Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(organizationSuperUserCommand.getMobile())%></span>
				</div>
			</div>
		</div>
	</div>
</div>
