<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%User user = (User) SecurityContextHolder.getContext()
.getAuthentication().getPrincipal();
String currency = user.getOrganization().getCurrencyFormat(); %>
	<div class="table-field" style="height: 65px;">
		<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Business Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="bName"></span>
				</div>
			</div>
		
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;" id="cName"></span>
				</div></div>
			<div>
			</div>
			</div></div>
			<div class="main-table">
		<div class="inner-table">
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Invoice Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="iName"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;" id="gender"></span>
				</div>
			</div>	
			</div>
			</div>
			</div>

		<div class="table-field" style="height: 150px;" >
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Contact Number</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;" id="mobile"></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="mail"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value" Style="padding-left: 2px;" id="directLine"></span>
				</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label"style="height:40px;">Alternate Mobile No</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="alternateNumber"></span>
				</div>
			</div>	
			
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="address1"></span>
				</div>
			</div>
			 <div class="display-boxes-colored" >
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="address2"></span>
				</div>
			</div>
			</div>	</div>
			<div class="main-table">
		<div class="inner-table">
			 <div class="display-boxes-colored" >
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="loc"></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">LandMark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="landMark"></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Region</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="region"></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="city"></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="state"></span>
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
			</div></div></div>
		 <div class="table-field" style="margin-top:15px;">
		<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Credit Limit (<%=currency%>)</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="creditLimit"></span>
				</div>
			</div>	
			</div></div>
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Credit Overdue Days</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="creditOverdueDays"></span>
				</div>
			</div>			
			</div>
			</div>
			</div>  		
	
