<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequestDetails"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerChangeRequestDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerChangeRequest"%>
<%@page import="com.vekomy.vbooks.customer.dao.CustomerDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomer"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Formatter"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
VbCustomerChangeRequest vbCustomerCr=null;
String currency=user.getOrganization().getCurrencyFormat();
    int id=0;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		CustomerChangeRequestDao customerCrDao = (CustomerChangeRequestDao) hibernateContext
				.getBean("customerCrDao");
		if(customerCrDao!=null){
			 id=Integer.parseInt(request.getParameter("id"));
			vbCustomerCr=customerCrDao.getCustomerCr(id, user.getOrganization());
		}
	} catch (Exception exx) {
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
			<%String businessName=StringUtil.format(vbCustomerCr.getBusinessName());%>
			<%if(vbCustomerCr.getCrType() == Boolean.TRUE) {%>
			<div class="display-boxes">
				<div>
			<a id="customer-change-request-business-name" href="#" class="<%=businessName%>" align="<%=vbCustomerCr.getId() %>" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
				<span id="customer-cr-previous-business-name" class="property-value" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;"><%=businessName%></span>
		    </a>
				</div>
			</div>
			<%}else{ %>
			<div class="display-boxes">
				<div>
				<a id="customer-change-request-business-name" href="#" class="<%=businessName%>" align="<%=vbCustomerCr.getId() %>" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
				<span class="property-value" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;"><%=StringUtil.format(vbCustomerCr.getBusinessName())%></span>
				</a>
				</div>
			</div>
			<%} %>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value customerName" Style="padding-left: 2px;"></span>
				</div></div>
			<div>
			</div>
			</div></div>
			<div class="main-table">
		<div class="inner-table">
		<div class="display-boxes-colored">
				<div>
					<span class="span-label">Change Request Type </span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value crType" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value gender" Style="padding-left: 2px;"></span>
				</div>
			</div>	
					
			</div></div></div>

<div class="clearfloat"></div>
	<div class="table-field" style="height: 150px;">
						<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Contact Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value contactNumber" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value email" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value directLine" Style="padding-left: 2px;"></span>
				</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value alternateMobile" Style="padding-left: 2px;"></span>
				</div>
			</div>	
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value address1" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Region</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value region" Style="padding-left: 2px;"></span>
				</div>
			</div>
			</div>	</div>
			<div class="main-table">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Address2</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value address2" Style="padding-left: 2px;"></span>
				</div>
			</div> 
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value locality" Style="padding-left: 2px;"></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Land Mark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value landMark" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value city" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value state" Style="padding-left: 2px;"></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Zip Code</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value zipCode" Style="padding-left: 2px;"></span>
				</div>
			</div>	
			</div></div></div>

<div class="main-table" style="margin-left: 20px;">
		<div class="inner-table">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Invoice Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value invoiceName" Style="padding-left: 2px;"></span>
				</div>
			</div> 
			</div>
			</div>
<div class="clearfloat"></div>
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
					<span class="property-value creditLimit" Style="padding-left: 2px;"></span>
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
					<span class="property-value creditOverdueDays" Style="padding-left: 2px;"></span>
				</div>
			</div>			
			</div></div></div>  
 <script type="text/javascript">
 $('#creditLimit').blur(function(){
	 var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
	 var formatCreditLimit=currencyHandler.convertFloatToStringPattern(creditLimit.toFixed(2));
	 $('#creditLimit').val(formatCreditLimit);
 }); 
    $('.helppop').hide();
</script>           