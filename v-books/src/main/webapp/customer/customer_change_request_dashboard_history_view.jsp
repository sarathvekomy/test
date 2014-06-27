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
					<span class="property-value businessName" Style="padding-left: 2px;"></span>
				</div>
			</div>
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
					<span class="property-value alternateNumber" Style="padding-left: 2px;"></span>
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
			<div class="table-field" style="height: 90px; width: 340px;">
				   <div class="main-table" style="margin-left: 0px;">
		                  <div class="inner-table">
		     <div class="display-boxes-colored">
		     <div>
				<span class="span-label"><%=Msg.get(MsgEnum.CREDIT_LIMIT)%>
				</span>
			</div>
			</div>
			<div class="display-boxes">
				 <div>
					<span class="property-value" Style="padding-left: 2px;">
					<input style="width: 145px;height: 15px; border: none;" name="creditLimit" id="creditLimit">
					</span>
				</div> 
				<span id="creditLimitValid" style="float: right; position: absolute; margin-left: 220px; margin-top: -20px"></span>
				<div id="creditLimit_pop" class="helppop" style="display: block; margin-left: 220px; margin-top: -10px;" aria-hidden="false;">
                   <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 200px;"><p style="display: inline;"> Credit Limit should contain floating point value and should not exceed by 10 digits.</p></div>
                </div> 
			</div>	
			<div class="display-boxes-colored">
		     <div>
				<span class="span-label"><%=Msg.get(MsgEnum.CREDIT_OVERDUE_DAYS)%>
				</span>
			</div>
			</div>
			<div class="display-boxes">
				 <div>
					<span class="property-value" Style="padding-left: 2px;">
					<input style="width: 145px;height: 15px; border: none;" name="creditOverdueDays" id="creditOverdueDays">
					</span>
				</div> 
				<span id="creditOverdueDaysValid" style="float: right; position: absolute; margin-left: 220px; margin-top: -10px"></span>
				<div id="creditOverdueDays_pop" class="helppop" style="display: block; margin-left: 220px; margin-top: -10px;" aria-hidden="false;">
                   <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 200px;"><p style="display: inline;"> Credit Overdue Days can contain 3 digit number.</p></div>
                </div> 
			</div>	
			</div>
      </div>
</div>
 <script type="text/javascript">
 $('#creditLimit').blur(function(){
	 var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
	 var formatCreditLimit=currencyHandler.convertFloatToStringPattern(creditLimit.toFixed(2));
	 $('#creditLimit').val(formatCreditLimit);
 }); 
    $('.helppop').hide();
</script>           