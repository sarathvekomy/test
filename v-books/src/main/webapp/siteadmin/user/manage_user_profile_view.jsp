
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbAssignOrganizations"%>
<%@page import="com.vekomy.vbooks.siteadmin.dao.ManageUserDao"%>
<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.employee.command.EmployeeCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>


<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	VbEmployee employee = null;
	VbEmployeeDetail employeeDetail = null;
	 List<String> organizationsList = new ArrayList<String>();
	VbEmployeeAddress employeeAddress = null;
	VbOrganization vbOrganization = null;
	String organizationName = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			ManageUserDao manageUerDao = (ManageUserDao) hibernateContext
					.getBean("manageUserDao");
			if (manageUerDao != null) {
				int id = Integer.parseInt(request.getParameter("id"));
				employee = manageUerDao.getUser(id );
				 organizationsList =manageUerDao.getAssignedOrganization(id);
				 for( String organizations :organizationsList){
					 if(organizationName == null){
						 organizationName = organizations;
					 }else{
						 organizationName = organizationName.concat(",").concat(organizations);
					 }
				 }
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
%>	
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getUsername())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Full Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employee.getFirstName())%></span>
				</div>
			</div>
			</div></div>
			<div class="main-table">
		<div class="inner-table">
		<%if(employee.getGender()!=null) {%>
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,employee.getGender().toString())%></span>
				</div>
			</div>
			<%} %>
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
			</div>
			</div>
			</div>
				<%
						if(employeeDetail!=null) {
						%>

		<div class="table-field" style="height:75px;">
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
					<span class="span-label">Organizations</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=organizationName%></span>
				</div>
			</div>
			</div>	</div>
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
			</div></div></div>

						<%} %>
						<%if(employeeAddress!=null) {%>
						<div class="table-field" style="height:75px;">
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
			</div></div>
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(employeeAddress.getCity()) %></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(DropDownUtil.getDropDown(DropDownUtil.ADDRESS_TYPE,employeeAddress.getAddressType()))%></span>
				</div>
			</div>			
			</div>
			</div>
			</div>
				<%}%>	
						
