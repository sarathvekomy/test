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
VbCustomer customer=null;
VbCustomerChangeRequest vbCustomerCr=null;
VbCustomerChangeRequestDetails customerCrDetails=null;
    int id=0;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		CustomerDao customerDao = (CustomerDao) hibernateContext
				.getBean("customerDao");
		CustomerChangeRequestDao customerCrDao = (CustomerChangeRequestDao) hibernateContext
				.getBean("customerCrDao");
		if(customerCrDao!=null){
			 id=Integer.parseInt(request.getParameter("id"));
			vbCustomerCr=customerCrDao.getCustomerCr(id, user.getOrganization());
			 Iterator<VbCustomerChangeRequestDetails> iterator = vbCustomerCr.getVbCustomerChangeRequestDetailses().iterator();

				if (iterator.hasNext()) {
					customerCrDetails = iterator.next();
				}
		}
		if (customerDao != null) {
			String businessName=request.getParameter("businessName");
			 customer = customerDao.getVbCustomer(businessName, user.getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(vbCustomerCr.getBusinessName())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getCustomerName())%></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.booleanFormat(vbCustomerCr.getCrType())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,vbCustomerCr.getGender().toString())%></span>
				</div>
			</div>	
					
			</div></div></div>

<div class="clearfloat"></div>
<%
	if (customerCrDetails != null) {
%>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getMobile())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Email</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getEmail())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Direct Line</span>
				</div>
			</div>
			<div class="display-boxes">
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getDirectLine())%></span>
				</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Alternate Number</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getAlternateMobile())%></span>
				</div>
			</div>	
			 <div class="display-boxes-colored">
				<div>
					<span class="span-label">Address1</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getAddressLine1())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Region</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getRegion())%></span>
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
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getAddressLine2())%></span>
				</div>
			</div> 
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getLocality())%></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Land Mark</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getLandmark())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">City</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getCity())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">State</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=StringUtil.format(customerCrDetails.getState())%></span>
				</div>
			</div>
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Zip Code</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value" Style="padding-left: 2px;"><%=customerCrDetails.getZipcode()%></span>
				</div>
			</div>	
			</div></div></div>
<%
	}
%>
<div class="clearfloat"></div>
	<%if(customer!=null){ %>
                  <form id="editCustomerCredit">
			<div class="table-field" style="height: 90px; width: 340px;">
				   <div class="main-table" style="margin-left: 130px;">
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
					<input style="width: 145px;height: 15px; border: none;" name="creditLimit" id="creditLimit"
					value="<%=StringUtil.currencyFormat(customer.getCreditLimit())%>">
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
					<input style="width: 145px;height: 15px; border: none;" name="creditOverdueDays" id="creditOverdueDays" 
			        value="<%=StringUtil.quantityFormat(customer.getCreditOverdueDays())%>">
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
		<input name="action" value="edit-customer-credit" type="hidden"id="cusomerAction">
		<input name="id" value="<%=id%>" type="hidden" id="id">
        </form> 
    <%
		}else{
	%>
	  <form id="editCustomerCredit">
			<div class="table-field" style="height: 90px; width: 340px;">
				   <div class="main-table" style="margin-left: 130px;">
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
				<span id="creditLimitValid" style="float: right; position: absolute; margin-left: 220px; margin-top: -10px"></span>
				<div id="creditLimit_pop" class="helppop" style="display: block; margin-left: 220px; margin-top: -10px;" aria-hidden="false;">
                   <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 200px;"><p style="display: inline;">  Credit Limit should contain floating point value and should not exceed by 10 digits.</p></div>
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
					<span class="property-value" Style="padding-left: 2px;"><input style="width: 145px;height: 15px; border: none;" name="creditOverdueDays" id="creditOverdueDays" >
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
		<input name="action" value="edit-customer-credit" type="hidden"id="cusomerAction">
		<input name="id" value="<%=id%>" type="hidden" id="id">
        </form> 
 <%} %> 		     
 <script type="text/javascript">
 $('#creditLimit').blur(function(){
	 var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
	 var formatCreditLimit=currencyHandler.convertFloatToStringPattern(creditLimit.toFixed(2));
	 $('#creditLimit').val(formatCreditLimit);
 });
    $('.helppop').hide();
</script>            
