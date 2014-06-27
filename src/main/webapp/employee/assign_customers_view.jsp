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
 		      <tr ><td>S.No</td><td>Business Name</td><td>Locality</td><td>Contact Number</td></tr>
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
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
			<% } %>
		
</div>