<%@page import="com.vekomy.vbooks.siteadmin.command.OrganizationResult"%>
<%@page import="com.vekomy.vbooks.siteadmin.dao.SiteAdminDao"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
	                .getSession().getServletContext());
	    	OrganizationDao organizationDao = (OrganizationDao) hibernateContext.getBean("organizationDao");
			if (organizationDao != null) {
				//int id = Integer.parseInt(request.getParameter("id"));
			//	organizationResult = organizationDao.getOrganizationViewResult(id);
			}
		} catch (Exception exx) {
			exx.printStackTrace();
		}
%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
	<%=pageTitle%>
	<%
		if (preview == true) {
	%>
	<%
		}
	%>
	<div class="table-field" style="height: 75px;">
						<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored" style="height:20px;">
				<div>
					<span class="span-label">Organization Name</span>
				</div>
			</div>
			<div class="display-boxes" style="height:20px;">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="name"></span>
				</div>
			</div>
		
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organization Code</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;" id="organizationCode"></span>
				</div></div>
				<div class="display-boxes-colored">
				<div>
					<span class="span-label">Main Branch</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="mainBranchVal"></span>
				</div>
			</div>
				<div class="display-boxes-colored">
				<div>
					<span class="span-label">Main Branch Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="mainBranchName"></span>
				</div>
			</div>
			</div></div>
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored" >
				<div>
					<span class="span-label">User Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="username"></span>
				</div>
			</div>
			<div class="display-boxes-colored" >
				<div>
					<span class="span-label">Full Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="fullName"></span>
				</div>
			</div>
			<div class="display-boxes-colored" style="height:45px;">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<div class="display-boxes" style="height:45px;">
				<div>
					<span class="property-value" Style="padding-left: 2px;" id="description"></span>
				</div>
			</div>	
			</div></div></div>
			
			
			
			<div class="table-field" style="height: 150px; margin-top: 30px;">
						<div class="main-table" style=" margin-top: 10px;">
		<div class="inner-table">
		 <div class="display-boxes-colored">
				<div>
					<span class="span-label">username prefix</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="usernamePrefix"></span>
				</div>
			</div>
		 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="addressLine1"></span>
				</div>
			</div>
			 <div class="display-boxes-colored" >
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="addressLine2"></span>
				</div>
			</div>
			<div class="display-boxes-colored" >
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"id="locality"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">LandMark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word" id="landMark"></span>
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
					<span class="span-label">Country</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="countryVal"></span>
				</div>
			</div>	
			</div></div>
			
			<div class="main-table">
		<div class="inner-table">
			
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
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Currency Format</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="currencyFormat"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone1</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="phone1"></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"id="phone2"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value" Style="padding-left: 2px;"id="mobile"></span>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Mobile</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value" Style="padding-left: 2px;"id="alternateMobile"></span>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label"style="height:40px;">Email</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="email"></span>
				</div>
			</div>	
			<!-- //%if(organizationResult.getMainBranch().equals("N")) {%> -->
			<div class="display-boxes-colored">
				<div>
					<span class="span-label"style="height:40px;">Branch Name</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"id="branchName"></span>
				</div>
			</div>	
			<%-- <%}else{ %>
			
			<%} %> --%>
			</div></div>
