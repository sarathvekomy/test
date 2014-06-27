<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBookProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBook"%>
<%@page import="com.vekomy.vbooks.accounts.command.SalesBookResult"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	boolean preview = false;
	List<SalesBookResult> list=null;
	SalesBookResult salesBook=null;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	SalesBookDao salesBookDao = (SalesBookDao)hibernateContext
			.getBean("accountsSalesBookDao");
	if(salesBookDao!=null)
	{
		int salesId=Integer.parseInt(request.getParameter("id"));
		list=salesBookDao.getAllotments(salesId , user.getOrganization());
		if(list != null){
			salesBook=list.get(0);
		}
	}
		} catch (Exception exx) {
	exx.printStackTrace();
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
		<div class="tabs-title"><%=Msg.get(MsgEnum.SALES_LABEL)%>
		</div>
	</div>
</div>
<%
	}
%>
          <div class="head">
				<div class="cont-left">
					<span class="property-value-view"><%=StringUtil.format(salesBook.getSalesExecutive())%></span>
				</div>
			
			
			
				<div class="cont-right">
					<span class="property-value-view"><%=DateUtils.format(salesBook.getCreatedDate())%></span>
				</div>
			</div>
			
<div class="main-table"
		style="width: 330px;margin-left:320px;">
		<div class="inner-table" style="width: 330px;">
		    <div class="display-boxes-colored" style="height:35px;">
				<div>
					<span class="span-label">Previous Closing Balance</span>
				</div>
			</div>
			<div class="display-boxes" style="height:35px;">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(salesBook.getPreviousClosingBalance())%></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Advance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(salesBook.getAdvance())%></span>
				</div>
			</div>	
				
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Opening Balance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(salesBook.getOpeningBalance())%></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Closing Balance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(salesBook.getClosingBalance())%></span>
				</div>
			</div>	
			</div></div>
			
			<% if (list != null) { %>
			
 			<div style="width:965px; float:left;">
 			<table  border="1" width="100%">
 		      <tr><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Previous Closing Stock</td><td>Quantity Allotted</td><td>Quantity Opening Balance</td><td>Quantity Sold</td>
 		     <td>Return Quantity</td><td>Quantity To Factory</td><td>Quantity Closing Balance</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
			for(int i=0;i<list.size();i++)
			{
				SalesBookResult product=list.get(i);
				count++;
				if(alternateProduct){
					%><tr style="background:#eaeaea;"><%}
					else{%>
						<tr style="background:#bababa;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getProduct())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getBatchNumber())%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(product.getPreviousClosingStock())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(product.getQtyAllotted())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(product.getQtyOpeningBalance()) %></td>
 	    		       <td align="right"><%=StringUtil.quantityFormat(product.getQtySold())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(product.getReturnQty())%></td>
 	    		        <td align="right"><%=StringUtil.quantityFormat(product.getQtyToFactory())%></td>
 	    		        <td align="right"><%=StringUtil.quantityFormat(product.getQtyClosingBalance()) %></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
			<% } %>