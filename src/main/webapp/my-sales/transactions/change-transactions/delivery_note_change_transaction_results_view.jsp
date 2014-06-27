<%@page import="com.vekomy.vbooks.hibernate.BaseDao"%>
<%@page import="com.vekomy.vbooks.spring.action.BaseAction"%>
<%@page import="com.vekomy.vbooks.mysales.cr.dao.DeliveryNoteCrDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequest"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestPayments"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteChangeRequestProducts"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
DeliveryNoteViewResult vbDeliveryNoteChangeRequest = null;
List<DeliveryNoteViewResult> resultList=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
		.getWebApplicationContext(request.getSession()
		.getServletContext());
		DeliveryNoteCrDao deliveryNoteCrDao = (DeliveryNoteCrDao) hibernateContext.getBean("deliveryNoteCrDao");
		if (deliveryNoteCrDao != null) {
	Integer id = Integer.valueOf(request.getParameter("id"));
	resultList = deliveryNoteCrDao.getDeliveryNoteChangeRequestResult(id , user.getOrganization());
	vbDeliveryNoteChangeRequest=resultList.get(0);
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
<%
String str=": ";
String advance= "(Adv)"; 
%>

<div class="outline" style="margin-left: 10px;">
	<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
			<%String invoiceNumber=StringUtil.format(vbDeliveryNoteChangeRequest.getInvoiceNo());%>
				<span class="property-value"><%=str%>
				<a id="change-request-dn-Invoice-number" href="#" class="<%=invoiceNumber%>" align="<%=vbDeliveryNoteChangeRequest.getId() %>" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
				<%=invoiceNumber %>
				</a>
				</span>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=vbDeliveryNoteChangeRequest.getCreatedDate()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbDeliveryNoteChangeRequest.getBusinessName())%></span>
			</div>
		</div>
		<%
		if(vbDeliveryNoteChangeRequest.getInvoiceName().contains(",1")){
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value" style="font-size: 12px; color: #6600FF;"><%=str%><%=StringUtil.format(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",1", ""))%></span>
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
				<span class="property-value"><%=str%><%=StringUtil.format(vbDeliveryNoteChangeRequest.getInvoiceName().replace(",0", ""))%></span>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<%
	if(vbDeliveryNoteChangeRequest.getInvoiceNo().contains("/DN/")){
	%>
   <div style="width:960px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Product Quantity</td><td>Product Cost (<%=currency%>)</td><td>Bonus Quantity</td><td>Bonus Reason</td>
 		     <td>Total Cost (<%=currency%>)</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
		    for(DeliveryNoteViewResult product : resultList){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getProduct().replace(",0", ""))%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getBatchNumber().replace(",0", ""))%></td>
 	    		     <%
				if(product.getQtyProduct().contains(",1")){
					String productQty=product.getQtyProduct().replace(",1", "");
					String formatProductQty=StringUtil.quantityFormat(Integer.parseInt(productQty));				
					%>
					 <td align="right" style="font-size: 12px; color: #6600FF;"><%=formatProductQty%></td>
               <%}else {
            	   String productQty=product.getQtyProduct().replace(",0", "");
					String formatProductQty=StringUtil.quantityFormat(Integer.parseInt(productQty));	
				%>
				 <td align="right"><%=formatProductQty%></td>
				<%} %>
				<%
				  String productCost=product.getCostProduct().replace(",0", "");
				  String formatProductCost=StringUtil.currencyFormat(Float.parseFloat(productCost));	
				%>
				<td align="right"><%=formatProductCost%></td>
 	    		    <%
				if(product.getQtyBonus().equals("")){
				   if(product.getQtyBonus().contains(",1")){
					String bonusQty=product.getQtyBonus().replace(",1", "");
					%>
				 <td align="right" style="font-size: 12px; color: #6600FF;"><%=bonusQty%></td>
				<%}else {
					String bonusQty=product.getQtyBonus().replace(",0", "");
				%>
				<td align="right"><%=bonusQty%></td>
				<%} 
				}else{
				 if(product.getQtyBonus().contains(",1")){
					String bonusQty=product.getQtyBonus().replace(",1", "");
					%>
				 <td align="right" style="font-size: 12px; color: #6600FF;"><%=bonusQty%></td>
				<%}else {
					String bonusQty=product.getQtyBonus().replace(",0", "");
				%>
				
				 <td align="right"><%=bonusQty%></td>
				<%}  
				 }%>
				  <% 
				if(product.getBonusReason().contains(",1")){
					String bonusReason=product.getBonusReason().replace(",1", "");
					%>
				 <td align="right"style="font-size: 12px; color: #6600FF;"><%=bonusReason%></td>
				<%}else {
					String bonusReason=product.getBonusReason().replace(",0", "");
				%>
                <td align="right"><%=bonusReason%></td>
				<%} %>
				
				<%
				if(product.getTotalCostProduct().contains(",1")){
					String totalCost=product.getTotalCostProduct().replace(",1", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));				
					%>
				<td align="right" style="font-size: 12px; color: #6600FF;"><%=formatTotalCost%></td>
				<%}else {
					String totalCost=product.getTotalCostProduct().replace(",0", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));		
				%>
				<td align="right"><%=formatTotalCost%></td>
				<%} %>
 	    		    </tr>
 					<% }%>
 					</table>
 					</div>	
	        <%
				}
			%>
			<%if(resultList != null){ %>
	<% String paymentType=vbDeliveryNoteChangeRequest.getPaymentType(); %>
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 150px;">
		<%if(vbDeliveryNoteChangeRequest.getPaymentType().contains(",1")){
			 paymentType=paymentType.replace(",1", "");
		%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment Type</div>
				<div class="input-field" style="margin-left: 7px;">
					<span style="font-size: 12px; color: #6600FF;"><%=str%><%=paymentType%></span>
				</div>
			</div>
			<%}else{ 
				 paymentType=paymentType.replace(",0", "");
			%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment Type</div>
				<div class="input-field" style="margin-left: 7px;">
					<span class="property-value"><%=str%><%=paymentType%></span>
				</div>
			</div>
			<%} %>
			<%if(paymentType.equalsIgnoreCase("Cheque")){  %>
			<%
			if(! StringUtil.format(vbDeliveryNoteChangeRequest.getBankName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Bank Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=vbDeliveryNoteChangeRequest.getBankName()%></span>
				</div>
			</div>
			<%}%>
			<%if(! StringUtil.format(vbDeliveryNoteChangeRequest.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=vbDeliveryNoteChangeRequest.getBranchName()%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(vbDeliveryNoteChangeRequest.getBankLocation()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Cheque No.</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=vbDeliveryNoteChangeRequest.getChequeNo()%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(vbDeliveryNoteChangeRequest.getBankLocation()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Bank Location</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=vbDeliveryNoteChangeRequest.getBankLocation()%></span>
				</div>
			</div>
			<%} }%>
			<div class="separator" style="height: 15px; width: 260px;"></div>
			<%if(vbDeliveryNoteChangeRequest.getBankName().isEmpty() || vbDeliveryNoteChangeRequest.getBranchName().isEmpty() || vbDeliveryNoteChangeRequest.getChequeNo().isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label" style="width: 105px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=vbDeliveryNoteChangeRequest.getSalesExecutive()%></span>
			</div> 
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 240px; margin-top: -40px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbDeliveryNoteChangeRequest
							.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 400px;"></div>
		<div class="paymentset">
			<%
				if(vbDeliveryNoteChangeRequest.getPresentPayable().contains(",1")){
					String presentPayable=vbDeliveryNoteChangeRequest.getPresentPayable().replace(",1", "");
					String formatPresentPayable=StringUtil.currencyFormat(Float.parseFloat(presentPayable));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatPresentPayable%></span>
				</div>
			</div>
			<%}else{ 
				String presentPayable=vbDeliveryNoteChangeRequest.getPresentPayable().replace(",0", "");
				String formatPresentPayable=StringUtil.currencyFormat(Float.parseFloat(presentPayable));		
			%>
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatPresentPayable%></span>
				</div>
			</div>
			<%} %>
			 <div class="form-row">
				<div class="payment-span-label">Present Advance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(vbDeliveryNoteChangeRequest
							.getPresentAdvance()))%></span>
				</div>
			</div> 
			<div class="form-row">
				<div class="payment-span-label">Previous Credit</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(vbDeliveryNoteChangeRequest
							.getPreviousCredit()))%></span>
				</div>
			</div>
			<%
				if(vbDeliveryNoteChangeRequest.getTotalPayable().contains(",1")){
					String totalPayable=vbDeliveryNoteChangeRequest.getTotalPayable().replace(",1", "");
					String formatTotalPayable=StringUtil.currencyFormat(Float.parseFloat(totalPayable));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatTotalPayable%></span>
				</div>
			</div>
			<%}else {
				String totalPayable=vbDeliveryNoteChangeRequest.getTotalPayable().replace(",0", "");
				String formatTotalPayable=StringUtil.currencyFormat(Float.parseFloat(totalPayable));		
				%>
				<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatTotalPayable%></span>
				</div>
			</div>
				<%} %>
				
				<%
				if(vbDeliveryNoteChangeRequest.getPresentPayment().contains(",1")){
					String presentPayment=vbDeliveryNoteChangeRequest.getPresentPayment().replace(",1", "");
					String formatPresentPayment=StringUtil.currencyFormat(Float.parseFloat(presentPayment));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatPresentPayment%></span>
				</div>
			</div>
			<%}else {
				String presentPayment=vbDeliveryNoteChangeRequest.getPresentPayment().replace(",0", "");
				String formatPresentPayment=StringUtil.currencyFormat(Float.parseFloat(presentPayment));		
				%>
				<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatPresentPayment%></span>
				</div>
			</div>
					<%} %>
					
					<%
				if(vbDeliveryNoteChangeRequest.getBalance().contains(",1")){
					String balance=vbDeliveryNoteChangeRequest.getBalance().replace(",1", "");
					String formatBalance=StringUtil.currencyFormat(Float.parseFloat(balance));				
					%>
			<%
			float totalPayable = new Float(0.00);
			if(vbDeliveryNoteChangeRequest.getTotalPayable().contains(",1")){
				totalPayable=Float.parseFloat(vbDeliveryNoteChangeRequest.getTotalPayable().replace(",1", ""));
			}else{
				totalPayable=Float.parseFloat(vbDeliveryNoteChangeRequest.getTotalPayable().replace(",0", ""));
			}
			float presentPayment = new Float(0.00);
			if(vbDeliveryNoteChangeRequest.getPresentPayment().contains(",1")){
				presentPayment=Float.parseFloat(vbDeliveryNoteChangeRequest.getPresentPayment().replace(",1", ""));
			}else{
				presentPayment=Float.parseFloat(vbDeliveryNoteChangeRequest.getPresentPayment().replace(",0", ""));
			}
			 float advBalance=(totalPayable - presentPayment);
			%> 
			<%if(advBalance < 0.0){ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=StringUtil.currencyFormat(Math.abs(Float.parseFloat(StringUtil.floatFormat(advBalance))))%> <%=advance %></span>
				</div>
			</div>
			<%}else{ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatBalance%></span>
				</div>
			</div>
			<%} %>
			
			<%}else {
				String balance=vbDeliveryNoteChangeRequest.getBalance().replace(",0", "");
				String formatBalance=StringUtil.currencyFormat(Float.parseFloat(balance));		
				%>
				<%
				float totalPayable = new Float(0.00);
				if(vbDeliveryNoteChangeRequest.getTotalPayable().contains(",1")){
					totalPayable=Float.parseFloat(vbDeliveryNoteChangeRequest.getTotalPayable().replace(",1", ""));
				}else{
					totalPayable=Float.parseFloat(vbDeliveryNoteChangeRequest.getTotalPayable().replace(",0", ""));
				}
				float presentPayment = new Float(0.00);
				if(vbDeliveryNoteChangeRequest.getPresentPayment().contains(",1")){
					presentPayment=Float.parseFloat(vbDeliveryNoteChangeRequest.getPresentPayment().replace(",1", ""));
				}else{
					presentPayment=Float.parseFloat(vbDeliveryNoteChangeRequest.getPresentPayment().replace(",0", ""));
				}
			 float advBalance=(totalPayable - presentPayment);
			%> 
			<%if(advBalance < 0.0){ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Math.abs(Float.parseFloat(StringUtil.floatFormat(advBalance))))%> <%=advance %></span>
				</div>
			</div>
			<%}else{ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatBalance%></span>
				</div>
			</div>
			<%} %>
			<%} %>
			<%} %>
			<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
	</div>
<div class="first-row">
		<div class="left-align">
			<div class="number-lable" style="margin-left: 2px ;margin-top: -15px">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class="number">
				<span class="property-value" style="width: 200px;"><%=str%><%=vbDeliveryNoteChangeRequest.getCrDescription()%></span>
			</div>
		</div>
</div>
</div>
