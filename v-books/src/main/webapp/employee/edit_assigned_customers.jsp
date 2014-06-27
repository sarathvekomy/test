<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="com.vekomy.vbooks.employee.dao.EmployeeCustomerDao"%>
<%@page
	import="com.vekomy.vbooks.employee.command.EmployeeCustomersCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>


<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.List"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>


<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>
<%
	EmployeeCustomersCommand employeeCustomersCommand = new EmployeeCustomersCommand();
	VbEmployeeCustomer vbEmployeeCustomer = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	String userName=request.getParameter("userName");
	employeeCustomersCommand.setUserName(userName);
	String uname=employeeCustomersCommand.getUserName();
	String locality=request.getParameter("locality");
	employeeCustomersCommand.setLocality(locality);
	String loc=employeeCustomersCommand.getLocality();
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	user.getOrganization();
     int id=Integer.parseInt(request.getParameter("id"));
	/* try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		EmployeeCustomerDao employeeCustomerDao = (EmployeeCustomerDao) hibernateContext
				.getBean("employeeCustomerDao");
		if (employeeCustomerDao != null) {
			List<String> businessNames = employeeCustomerDao.getBusinessNamesByEmployee(userName, user.getOrganization(), id);
			for(String businessName :businessNames){
				
			}
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	} */
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
		<div class="tabs-title">Basic Info</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title">Additional Info</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title">Address</div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title">Preview</div>
	</div>
	<div class="step-selected-corner"></div>
</div>
<%
		}
	%>

<div class="ui-content form-panel form-panel-border">
	<form name="form2" id="employee-customer-search-form">
		<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
			<div class="fieldset" style="height: 40px;">
				<div class="form-row">
					<div class="label">Employee Name</div>
					<div class="input-field">
						<input name="userName" id="uName" value="<%=uname%>">
					</div>
				</div>
			<div class="seperator" style="height: 40px;"></div>
			<div class="form-row">
				<div class="label">Localities</div>
				<div class="input-field">
					<input name="localities" id="localities" value="<%=loc%>">
				</div>
			</div>
			</div>
		<div class="separator" style="height: 80px;"></div>
	<div class="fieldset" style="height: 40px;">
			<div class="form-row">
				<div class="label">Customers</div>
				
				<div class="input-field">
					<select id="businessNames" name="businessName" multiple="multiple">
						<option></option>
					</select>
				</div>
			</div>
		</div>
		</div>
		<input type="hidden" name="id" value="<%=id %>" />
		<!-- <input type="hidden" name="action" value="get-customernames-by-id"/> -->
</form>
<div id="search-buttons" class="search-buttons">
	<div id="btn-update" class="ui-btn btn-update"></div>
</div>

</div>
<script type="text/javascript">
	$(document).ready(function() {
		assignCustomerHandler.getCustomerNames();
	});
	
</script>
