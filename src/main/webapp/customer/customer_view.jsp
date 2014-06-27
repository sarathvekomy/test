<%@page import="com.vekomy.vbooks.customer.command.CustomerCommand"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
	<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%
User user = (User) SecurityContextHolder.getContext()
.getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
	CustomerCommand customer = null;
	VbCustomerDetail customerDetail = null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		customer = (CustomerCommand) session.getAttribute("customer-basic");
		customerDetail = (VbCustomerDetail) session.getAttribute("customer-detail");

	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			CustomerDao customerDao = (CustomerDao) hibernateContext
					.getBean("customerDao");
		}
		catch (Exception exx) {
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
		<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_BASIC_INFO_LABEL)%>
		</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_ADDITIONAL_INFO_LABEL)%></div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%></div>
	</div>
	<div class="step-selected-corner"></div>
</div>
<%
	}
%>

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
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;" id="businessName"><%=StringUtil.format(customer.getBusinessName())%></span>
				</div>
			</div>
		
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value"  Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(customer.getCustomerName())%></span>
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
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(customer.getInvoiceName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,customer.getGender().toString())%></span>
				</div>
			</div>	
			</div></div></div>

			<%
				if (customerDetail != null) {
			%>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customer.getMobile())%></span>
				</div>
			</div>
			
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word;"><%=StringUtil.format(customer.getEmail())%></span>
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
					<span class="span-label"style="height:40px;width:124px; !important">Alternate Mobile No</span>
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
			
			
			 <div class="display-boxes-colored" >
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;word-wrap:break-word"><%=StringUtil.format(customerDetail.getAddressLine2())%></span>
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
				if (customer != null) {
			%>
		 <div class="table-field" style="margin-top:15px;">
		<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label" style="width:124px; !important">Credit Limit (<%=currency%>)</span>
				</div>
			</div>
			<div class="display-boxes right-aligned">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.currencyFormat(customer.getCreditLimit())%></span>
				</div>
			</div>	
			</div></div>
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label" style="width:130px; !important">Credit Overdue Days</span>
				</div>
			</div>
			<div class="display-boxes right-aligned">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.nullFormat(customer.getCreditOverdueDays())%></span>
				</div>
			</div>			
			</div></div></div>  
		<%} %>				
