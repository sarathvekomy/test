<%@page import="com.vekomy.vbooks.mysales.cr.dao.SalesReturnCrDao"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.accounts.command.SalesReturnResult"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnViewResult"%>
<%@page import="com.vekomy.vbooks.accounts.command.SalesReturnCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequestProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturnChangeRequest"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
    SalesReturnViewResult salesReturn=null;
    List<SalesReturnViewResult> salesReturnList=null;
    VbEmployee vbEmployee=null;
    Integer salesReturnId=0;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	SalesReturnCrDao salesReturnCrDao = (SalesReturnCrDao)hibernateContext.getBean("salesReturnCrDao");
	if(salesReturnCrDao!=null)
	{
		int salesReturnCRId=Integer.parseInt(request.getParameter("id"));
		salesReturnList=salesReturnCrDao.getSalesReturnProductsDetails(salesReturnCRId , user.getOrganization());
		salesReturn=salesReturnList.get(0);		
		if(salesReturn != null){
			salesReturnId=salesReturn.getId();	
		}else{
			salesReturnId=new Integer(0);
		}
	}
		} catch (Exception exx) {
	exx.printStackTrace();
	}
%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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
<div class="outline">
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
				<span class="property-value"><%=str%><%=DateUtils.format(salesReturn.getCreatedOn())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name <%=str%></span>
			</div>
			<div class="number">
				<span class="property-value" id ="businessName"><%=StringUtil.format(salesReturn.getBusinessName())%></span>
			</div>
		</div>
		
		<%
		if(salesReturn.getInvoiceName().contains(",1")){
		%><div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value" style="font-size: 12px; color: #6600FF;"><%=str%><%=StringUtil.format(salesReturn.getInvoiceName().replace(",1", ""))%></span>
			</div>
		</div>
		<%
		}else{
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getInvoiceName().replace(",0", ""))%></span>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<%
		if (!salesReturnList.isEmpty()) {
	%>
 			<div style="width:960px; float:left;">
 			<table border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Damaged</td><td>Resalable</td><td>Total Quantity</td><td>Unit Cost (<%=currency%>)</td>
 		     <td>Damaged Cost (<%=currency%>)</td><td>Resalable Cost (<%=currency%>)</td><td>Total Cost (<%=currency%>)</td></tr>
 					<% 
 		    Boolean alternateProduct=false;
				int count=0;
					for(int i=0;i<salesReturnList.size();i++){
						SalesReturnViewResult product=salesReturnList.get(i);
						count++;
			%>
			<% if(alternateProduct){
					%><tr class="row" id="row-<%=count%>" style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr class="row" id="row-<%=count%>" style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
						<td><%=count%></td>
						<td  id="productName"><%=StringUtil.format(product.getProductName().replace(",0", ""))%></td>
						<td  id="batchNumber"><%=StringUtil.format(product.getBatchNumber().replace(",0", ""))%></td>
				<%
				if(product.getDamagedQty().contains(",1")){
					String damaged=product.getDamagedQty().replace(",1", "");
					String formatDamaged=StringUtil.quantityFormat(Integer.parseInt(damaged));				
					%>
						<td  id="damaged" style="font-size: 12px; color: #6600FF;"><%=formatDamaged%></td>
				<%}else {
					String damaged=product.getDamagedQty().replace(",0", "");
					String formatDamaged=StringUtil.quantityFormat(Integer.parseInt(damaged));
				%>
						<td  id="damaged"><%=formatDamaged%></td>
				<%} %>
				
				<%
				if(product.getResaleQty().contains(",1")){
					String resalable=product.getResaleQty().replace(",1", "");
					String formatResalable=StringUtil.quantityFormat(Integer.parseInt(resalable));	
				%>
						<td id="resalable" style="font-size: 12px; color: #6600FF;"><%=formatResalable%></td>
				<%}else {
					String resalable=product.getResaleQty().replace(",0", "");
					String formatResalable=StringUtil.quantityFormat(Integer.parseInt(resalable));	
				%>
						<td  id="resalable"><%=formatResalable%></td>
				<%} %>
				
				<%
				if(product.getTotalProductQty().contains(",1")){
					String totalQty=product.getTotalProductQty().replace(",1", "");
					String formatTotalQty=StringUtil.quantityFormat(Integer.parseInt(totalQty));	
				%>
						<td  style="font-size: 12px;color: #6600FF;" id="totalQuantity"><%=formatTotalQty%></td>
				<%}else {
					String totalQty=product.getTotalProductQty().replace(",0", "");
					String formatTotalQty=StringUtil.quantityFormat(Integer.parseInt(totalQty));	
				%>
						<td id="totalQuantity"><%=formatTotalQty%></td>
				<%} %>
				
						  <td id="productCost"><%=product.getProductCost()%></td>  
					<td>
						<input type="text" id="damagedCost" class="property-right-float" value="<%=product.getDamageCost() %>" style="text-align:right !important;"/>
					</td>
					<td>
						<input type="text" id="resalableCost" class="property-right-float" value="<%=product.getResaleCost()%>" style="text-align:right !important;"/>
					</td>
						<td id="totalCost"><%=product.getProductTotalCost()%></td>
				</tr>
			<%
				}
			%>
			</table>
			</div>
	<%
				}
			%>
	
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 100px;">
			<div class="separator" style="height: 60px; width: 80px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 300px;">
				<div class="payment-span-label" style="width:100px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=str%><%=salesReturn.getSalesExecutive()%></span>
			</div>
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 280px; margin-top: -41px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(salesReturn.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 380px;"></div>
		<div class="paymentset" style="margin-left: 18px; height: 100px;">
			<div class="form-row">
				<div class="payment-span-label">Total</div>
				<div class="input-field"><span class="appostaphie" id="grandTotal"><%=str%><%=salesReturn.getProductsGrandTotal()%></span>
					<span id="total" class="span-payment" style="color: #6600FF; font-size: 12px;"></span>
				</div>
			</div>
				<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
	</div>
<%if(salesReturn.getRemarks().contains(",1")){ 
		  String remarks=salesReturn.getRemarks().replace(",1", "");
		  String formatRemarks=StringUtil.format(remarks);
		%>
			<div class="form-row">
				<div class="payment-span-label">Sales Return Remarks</div>
				<div class="input-field">
					<span class="property-value" style="font-size: 12px;color: #6600FF;"><%=str%><%=formatRemarks %></span>
				</div>
			</div>
			<%}else{ 
				String remarks=salesReturn.getRemarks().replace(",0", "");
				  String formatRemarks=StringUtil.format(remarks);
			%>
			<div class="form-row">
				<div class="payment-span-label">Sales Return Remarks</div>
				<div class="input-field">
					<span  class="property-value"><%=str%><%=formatRemarks %></span>
				</div>
			</div>
			<%} %>
		<div style="float: right;margin-right: 110px;"></div>
		<div class="left-align">
			<div class="number-lable" style="margin-left: 2px;">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class=input-field>
				<span class="property-value"><%=str%><%=StringUtil.format(salesReturn.getCrDescription())%></span>
			</div>
       </div>
</div>

