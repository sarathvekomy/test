<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.employee.command.EmployeeCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
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
	VbEmployee employee = null;
	VbEmployeeDetail employeeDetail = null;
	VbEmployeeAddress employeeAddress = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		employee = (EmployeeCommand) session
				.getAttribute("employee-basic");
		employeeDetail = (VbEmployeeDetail) session
				.getAttribute("employee-detail");
		employeeAddress = (VbEmployeeAddress) session
				.getAttribute("employee-address");

	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			EmployeeDao employeeDao = (EmployeeDao) hibernateContext
					.getBean("employeeDao");
			if (employeeDao != null) {
				int id = Integer.parseInt(request.getParameter("id"));
				employee = employeeDao.getEmployee(id,
						user.getOrganization());
				Iterator<VbEmployeeDetail> iterator = employee
						.getVbEmployeeDetails().iterator();
				if (iterator.hasNext()) {
					employeeDetail = iterator.next();
				}
				Iterator<VbEmployeeAddress> iterator_address = employee
						.getVbEmployeeAddresses().iterator();
				if (iterator_address.hasNext()) {
					employeeAddress = iterator_address.next();
				}

			}
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
<div class="add-student-tabs">
	<div class="step-no-select">
		<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_BASIC_INFO_LABEL)%>
		</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDITIONAL_INFO_LABEL)%></div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.EMPLOYEE_ADDRESS_LABEL)%></div>
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
<div class="table-field">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">User Name</span>
				</div>
			</div>
			
			<div class="display-boxes">
				<%
				if (employee.getUsername().startsWith(
						employee.getVbOrganization().getUsernamePrefix()
								.concat("."))) {
				%>
				<div>
					<span class="property-value" Style="padding-left: 2px;">
					<%=StringUtil.format(employee.getUsername())%></span>
				</div>
				
			<%
				} else {
			%>
			<div>
				<span class="property-value" Style="padding-left: 2px;">
					<%=StringUtil.format(employee.getVbOrganization().getUsernamePrefix().concat(".")
						.concat(employee.getUsername()))%></span>
			</div>
			<%
				}
			%>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">First Name</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getFirstName())%></span>
			</div>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">Middle Name</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getMiddleName())%></span>
			</div>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">Last Name</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getLastName())%></span>
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
				<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER, employee
					.getGender().toString())%></span>
			</div>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">Email Id</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getEmployeeEmail())%></span>
			</div>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">Employee Type</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(DropDownUtil.getDropDown(
					DropDownUtil.EMPLOYEE_TYPE, employee.getEmployeeType()))%></span>
			</div>
		</div>
		<div class="display-boxes-colored">
			<div>
				<span class="span-label">Employee Number</span>
			</div>
		</div>
		<div class="display-boxes">
			<div>
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getEmployeeNumber())%></span>
			</div>
		</div>
	</div>
</div>
</div>

<%
	if (employeeDetail != null) {
%>

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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getMobile())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getDirectLine())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getAlternateMobile())%></span>
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
				<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getBloodGroup())%></span>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Passport Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getPassportNumber())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Nationality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeDetail.getNationality())%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<%
	}
%>
<%
	if (employeeAddress != null) {
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getAddressLine1())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getAddressLine2())%></span>
				</div>
			</div>

			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getLocality())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Landmark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getLandmark())%></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getCity())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">ZipCode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getZipcode())%></span>
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
						employeeAddress.getAddressType()))%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<%
	}
%>

<%
	if (StringUtil.format(
			DropDownUtil.getDropDown(DropDownUtil.EMPLOYEE_TYPE,
					employee.getEmployeeType())).equalsIgnoreCase(
			"Sales Executive")) {
%>
<div class="table-field" style="height: 25px; margin-top: 40px;">
	<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Granted Days</span>
				</div>
			</div>
			<div class="display-boxes right-aligned">
				<div>
					<span class="property-value right-aligned"
						Style="padding-left: 2px;"><%=StringUtil.quantityFormat(employee.getGrantedDays())%></span>
				</div>
			</div>
		</div>
	</div>
</div>

<%
	}
%>
