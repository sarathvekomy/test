
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	VbCustomer vbCustomer = null;
	VbCustomerDetail customerDetail = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		vbCustomer = (CustomerCommand) session
				.getAttribute("customer_basic");
		customerDetail = (VbCustomerDetail) session
				.getAttribute("customer-detail");
	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			CustomerDao customerDao = (CustomerDao) hibernateContext
					.getBean("customerDao");
			if (customerDao != null) {
				int id = Integer.parseInt(request.getParameter("id"));
				vbCustomer = customerDao.getCustomer(id , user.getOrganization());
				Iterator<VbCustomerDetail> iterator = vbCustomer.getVbCustomerDetails().iterator();
				if (iterator.hasNext()) {
					customerDetail = iterator.next();
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
<%@page import="java.util.StringTokenizer"%><div
	id="customer-add-form-container" title="Create Customer">
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
<div class="table-field" style="height: 75px;">
						<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Business Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(vbCustomer.getBusinessName())%></span>
				</div>
			</div>
		
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(vbCustomer.getCustomerName())%></span>
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
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(vbCustomer.getInvoiceName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,vbCustomer.getGender().toString())%></span>
				</div>
			</div>	
			</div></div></div>

			<%
				if (customerDetail != null) {
			%>
			<div class="table-field" style="height: 150px;">
						<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Contact Number</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getMobile())%></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(customerDetail.getEmail())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getDirectLine())%></span>
				</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label"style="height:40px;">Alternate Mobile No</span>
				</div>
			</div>
			<div class="display-boxes" >
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getAlternateMobile())%></span>
				</div>
			</div>	
			
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getAddressLine1())%></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getAddressLine2())%></span>
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
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getLocality())%></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">LandMark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getLandmark())%></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Region</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getRegion())%></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getCity())%></span>
				</div>
			</div>
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">ZipCode</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerDetail.getZipcode())%></span>
				</div>
			</div>	
			</div></div></div>
			<%} %>
			<%
				if (vbCustomer != null) {
			%>
		 <div class="table-field" style="margin-top:25px;">
		<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Credit Limit</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.currencyFormat(vbCustomer.getCreditLimit())%></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.quantityFormat(vbCustomer.getCreditOverdueDays())%></span>
				</div>
			</div>			
			</div></div></div>  
		<%
		}
			%>				
	