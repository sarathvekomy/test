<%@page import="com.vekomy.vbooks.employee.dao.EmployeeCustomerDao"%>
<%@page
	import="com.vekomy.vbooks.employee.command.EmployeeCustomersCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployeeCustomer"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbCustomerDetail"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.text.DecimalFormat"%>


<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.vekomy.vbooks.security.User" %>

<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
%>
<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>
<%
	EmployeeCustomersCommand employeeCustomersCommand = new EmployeeCustomersCommand();
	VbEmployeeCustomer vbEmployeeCustomer = null;
	String date=null;
	String salesExecutive=null;
	List list=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
	user.getOrganization();

	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	String locality=request.getParameter("locality");
	employeeCustomersCommand.setLocality(locality);
	String loc=employeeCustomersCommand.getLocality();
	if (viewType != null && viewType.equals("preview")) {
		preview = true;

	} else {
		try {
			ApplicationContext hibernateContext = WebApplicationContextUtils
					.getWebApplicationContext(request.getSession()
							.getServletContext());
			EmployeeCustomerDao employeeCustomerDao = (EmployeeCustomerDao) hibernateContext
					.getBean("employeeCustomerDao");
			if (employeeCustomerDao != null) {
				int id = Integer.parseInt(request.getParameter("id"));
                date=request.getParameter("date");
                salesExecutive=request.getParameter("salesExecutive");
				list = employeeCustomerDao.getEmployeeCustomersForView(id, user.getOrganization());
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
<%@page import="java.util.StringTokenizer"%>
	<%=pageTitle%>
	<%
		if (list != null) {
			VbCustomerDetail customer1=(VbCustomerDetail)list.get(0);
	%>
	
	
				<div class="cont-left">
					<span ><%=StringUtil.format(salesExecutive)%></span>

				</div>
				
			
			
				<div class="cont-right">
					<span><%=StringUtil.format(date)%></span>

				</div>
	<%} %>
	<%
		if (list != null) {
	%>
 			<div style="width:500px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Business Name</td><td>Locality</td><td>Contact Number</td><td>Delete</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(int i=0;i<list.size();i++){
				VbCustomerDetail customer=(VbCustomerDetail)list.get(i);
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:500px;"><%}
					else{%>
						<tr style="background:#bababa;width:500px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(customer.getVbCustomer().getBusinessName())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(customer.getLocality())%></td>
 	    		      <td align="right"><%=customer.getMobile()%></td>
 	    		      <td align="right"><input type="checkbox" name="delete" value=<%=customer.getId()%>></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
			<% } %>
	
	
		<%-- <% if (list != null) { %>
			<div class="invoice-main-table" style="width: 513px !important;border-top:solid 1px gray;margin:25px;">
		<div class="inner-table" style="width: 525px !important;">
			<div class="invoice-boxes-colored" style="width: 40px;border-left:solid 1px gray;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Business Name</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 135px;">
				<div>
					<span class="span-label">Locality</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 136px;">
				<div>
					<span class="span-label">Contact No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 52px;">
				<div>
					<span class="span-label">Delete</span>
				</div>
			</div>
		
			<% 
			int count=0;
			for(int i=0;i<list.size();i++){
				VbCustomerDetail customer=(VbCustomerDetail)list.get(i);
				count++;
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			assignCustomerHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width: 513px !important;">
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 40px;border-left:solid 1px gray;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<input type="hidden" value=<%=customer.getId()%>>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(customer.getVbCustomer().getBusinessName())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 135px;">
					<div>
						<span class="property"><%=StringUtil.format(customer.getLocality())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 135px; ">
					<div>
						<span class="property-right-float"><%=customer.getMobile()%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 40px; border-right:none !important;">
					<div>
						<span class="property-right-float"><input type="checkbox" name="delete" value=<%=customer.getId()%>></span>
					</div>
				</div>
			</div>
			<%
				}
			%>
			</div>
			<%
				}
			%> --%>
</div>
