
<%@page import="com.vekomy.vbooks.employee.dao.EmployeeDao"%>
<%@page import="com.vekomy.vbooks.employee.command.EmployeeCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeAddress"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
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
					<span class="property-value" Style="padding-left: 2px;"id="userName"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">First Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="fName"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Middle Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="middleName"></span>
					
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Last Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="lName"></span>
				</div>
			</div>	
			</div></div>
			<div class="main-table">
		<div class="inner-table">
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="gender"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email Id</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="email"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Employee Type</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="eType"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Employee Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="employeeNumber"></span>
				</div>
			</div>			
			</div>
			</div>
			</div>
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
					<span class="property-value" Style="padding-left: 2px;"id="mobile"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="directLine"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;" id="alternateMobile"></span>
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
					<span class="property-value" Style="padding-left: 2px;"id="bloodGroup"></span>
				</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Passport Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="passportNumber"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Nationality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="nationality"></span>
				</div>
			</div>	
			</div>
		</div>
		</div>

						
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
					<span class="property-value" Style="padding-left: 2px;" id="addressLine1"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="addressLine2"></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="locality"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Landmark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;" id="landMark"></span>
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
					<span class="property-value" Style="padding-left: 2px;" id="city"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="state"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">ZipCode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="zipcode"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address Type</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="addressType"></span>
				</div>
			</div>			
			</div>
			</div>
			</div>
			
			   <div id="grantedDaysField" class="table-field" style="height:25px; margin-top: 40px;">
					   <div class="main-table">
		                       <div class="inner-table">
			<div class="display-boxes-colored" >
				<div>
					<span class="span-label">Granted Days</span>
				</div>
			</div>
			<div class="display-boxes right-aligned">
				<div>
					<span class="property-value right-aligned" Style="padding-left: 2px;" id="grantedDays"></span>
				</div>
			</div>
			</div></div></div>
						
						
					
