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
	VbOrganization vbOrganization = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
	                .getSession().getServletContext());
	    	OrganizationDao organizationDao = (OrganizationDao) hibernateContext.getBean("organizationDao");
			if (organizationDao != null) {
				int id = Integer.parseInt(request.getParameter("id"));
				vbOrganization = organizationDao.getOrganization(id);
			}
		} catch (Exception exx) {
			exx.printStackTrace();
		}
%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%><div id="organization-create-form-container" title="Delete Organization">
	<%=pageTitle%>
	<%
		if (preview == true) {
	%>
	<%
		}
	%>
<div class="main-table" Style="width: 330px;margin-left: 10px;">
		<div class="inner-table" Style="width: 330px;">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Main Branch</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getMainBranch())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organization Code</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getOrganizationCode())%></span>

				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Organization Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getName())%></span>

				</div>
			</div>
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Branch Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getBranchName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getDescription())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address Line1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getAddressLine1())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address Line2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getAddressLine2())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Full Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getFullName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getLocality())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Land Mark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getLandmark())%></span>

				</div>
			</div>
			<div class="display-boxes-colored">

				<div>
					<span class="span-label">City</span>
				</div>
			</div>

			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getCity())%></span>
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
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">

				<div>
					<span class="span-label">Country</span>
				</div>
			</div>

			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getCountry())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Zipcode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getZipcode())%></span>
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
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getCurrencyFormat())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone 1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getPhone1())%></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone 2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getPhone2())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Phone 3</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=StringUtil.format(vbOrganization.getPhone3())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email Id</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"
						Style="padding-left: 10px;"><%=vbOrganization.getEmail()%></span>
				</div>
			</div>
		
				<input name="id" value="<%=vbOrganization.getId()%>" type="hidden">
			</div>
		</div>
			</div>


		
