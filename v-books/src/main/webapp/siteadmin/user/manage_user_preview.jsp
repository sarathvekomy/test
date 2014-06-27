<%@page import="com.vekomy.vbooks.siteadmin.dao.ManageUserDao"%>
<%@page import="com.vekomy.vbooks.siteadmin.command.ManageUserCommand"%>
<%@page
	import="com.vekomy.vbooks.siteadmin.command.ManageUserBasicCommand"%>
<%@page
	import="com.vekomy.vbooks.siteadmin.command.ManageUserAddressCommand"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
	ManageUserCommand manageUserCommand = null;
	ManageUserBasicCommand manageUserBasicCommand = null;
	ManageUserAddressCommand manageUserAddressCommand = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		manageUserBasicCommand = (ManageUserBasicCommand) session
				.getAttribute("manage-user-basic");
		manageUserAddressCommand = (ManageUserAddressCommand) session
				.getAttribute("manage-user-address");

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
<div class="add-student-tabs" style="height: 20px;">
		<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%></div></div>
		<div class="step-no-select"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div></div>
		<div class="step-selected"><div class="step-no-select-corner"></div><div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_PREVIEW_LABEL)%></div></div>
		<div class="step-selected-corner"></div>
	</div>
<%
	}
%>
<div class="table-field">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">User Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getUsername())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Full Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getFullName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organizations</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
					.getOrganizations())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,
					manageUserBasicCommand.getGender().toString())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email Id</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand
					.getEmployeeEmail())%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="table-field" style="height: 75px;">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Mobile Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getMobile())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getDirectLine())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand
					.getAlternateMobile())%></span>
				</div>
			</div>
		</div>
	</div>
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Blood Group</span>
				</div>
			</div>
			<div class="display-boxes">
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getBloodGroup())%></span>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Passport Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand
					.getPassportNumber())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Nationality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserBasicCommand.getNationality())%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<%-- <%} %> --%>
<%
	if (manageUserAddressCommand != null) {
%>
<div class="table-field" style="height: 75px;">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
						.getAddressLine1())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
						.getAddressLine2())%></span>
				</div>
			</div>

			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
						.getLocality())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Landmark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
						.getLandmark())%></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand.getCity())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">ZipCode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(manageUserAddressCommand
						.getZipcode())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address Type</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(DropDownUtil.getDropDown(
						DropDownUtil.ADDRESS_TYPE,
						manageUserAddressCommand.getAddressType()))%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<%
	}
%>

