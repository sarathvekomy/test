<%@page import="com.vekomy.vbooks.accounts.command.SalesReturnResult"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnViewResult"%>
<%@page import="com.vekomy.vbooks.mysales.dao.SalesReturnDao"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
    SalesReturnViewResult salesReturn=null;
    List<SalesReturnViewResult> salesReturnList=null;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	SalesReturnDao salesReturnDao = (SalesReturnDao)hibernateContext
	.getBean("salesReturnDao");
	if(salesReturnDao!=null)
	{
		int salesReturnId=Integer.parseInt(request.getParameter("id"));
		salesReturnList=salesReturnDao.getSalesReturnProductsDetails(salesReturnId , user.getOrganization());
		salesReturn=salesReturnList.get(0);
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
		<div class="tabs-title"><%=Msg.get(MsgEnum.SALES_RETURNS_LABEL)%>
		</div>
	</div>
</div>
<%
	}
%>
<%
	String str=": ";
%>
<div class="outline" style="margin-left:20px;">
<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getInvoiceNo())%></span>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=salesReturn.getCreatedDate()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getBusinessName())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getInvoiceName())%></span>
			</div>
		</div>
	</div>
	
	
	
	
	
	 <%
		if (salesReturnList != null) {
	%>
 			<div style="width:960px; float:left;">
 			<table border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Damaged</td><td>Resalable</td><td>Total Quantity</td><td>Unit Cost (<%=currency%>)</td>
 		     <td>Damaged Cost (<%=currency%>)</td><td>Resalable Cost (<%=currency%>)</td><td>Total Cost (<%=currency%>)</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(int i=0;i<salesReturnList.size();i++){
 			  SalesReturnViewResult product=salesReturnList.get(i);
			count++;%>
			
			 <% if(alternateProduct){
					%><tr class="row" id="row-<%=count%>" style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr class="row" id="row-<%=count%>" style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;" ><%=StringUtil.format(product.getProduct())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getBatchNumber())%></td>
 	    		     <td align="right" ><%=StringUtil.quantityFormat(product.getDamaged())%></td>
 	    		      <td align="right" ><%=StringUtil.quantityFormat(product.getResalable())%></td>
 	    		      <td align="right" ><%=StringUtil.quantityFormat(product.getTotalQty())%></td>
 	    		      <td align="right" ><%=StringUtil.format(product.getProductCost())%></td>
 	    		      <td align="right"><input type="text" readonly="readonly"  style="text-align:right !important;" value="<%=product.getDamageCost()%>"></td>
 	    		      <td align="right"><input type="text" readonly="readonly"  style="text-align:right !important;" value="<%=product.getResaleCost()%>"></td>
						<td  align="right"><%=product.getProductTotalCost()%></td>
 	    		      
 	    		    </tr>
			</div>
		          
 					<% }%>
 						  
 					</table>
 					</div>	
			<% } %>
	 <div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height:90px;">
			<div class="separator" style="height: 60px; width: 80px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 300px;">
				<div class="payment-span-label" style="width:100px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(salesReturn.getSalesExecutive())%></span>
			</div>
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 280px; margin-top: -41px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(salesReturn.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 380px;"></div>
		<div class="paymentset" style="margin-left: 18px;height:100px;">
		<div class="form-row">
				<div class="payment-span-label">Total</div>
				<div class="input-field"><span class="appostaphie"><%=str%><%=salesReturn.getProductsGrandTotal()%></span>
					<span class="span-payment"></span>
				</div>
			</div>
		</div>
<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
<div class="left-align">
			<div class="number-lable" style="margin-left: 2px;">
				<span class="span-label">Sales Return Remarks</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getRemarks())%></span>
			</div>
</div>
		<div style="float: right;margin-right: 110px;margin-bottom: 2px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	</div> 
</div>
