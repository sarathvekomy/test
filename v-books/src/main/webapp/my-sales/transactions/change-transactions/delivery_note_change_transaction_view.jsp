<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.mysales.cr.dao.DeliveryNoteCrDao"%>
<%@page import="com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteProductCommand"%>
<%@page import="com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteCommand"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Date"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
ChangeRequestDeliveryNoteCommand deliveryNote = null;
String currency=user.getOrganization().getCurrencyFormat();
ChangeRequestDeliveryNoteProductCommand deliveryNoteProduct = null;
ChangeRequestDeliveryNoteCommand command=null;
	String value=null;
	String invoice=null;
	List<ChangeRequestDeliveryNoteCommand> list=new ArrayList<ChangeRequestDeliveryNoteCommand>();
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		deliveryNote = (ChangeRequestDeliveryNoteCommand) session.getAttribute("deliverynote-info");
		deliveryNoteProduct = (ChangeRequestDeliveryNoteProductCommand) session.getAttribute("delivery-note-product");
		 //  list=(List<ChangeRequestDeliveryNoteCommand>)session.getAttribute("product-data");
		if(session.getAttribute("product-data") != null){	 
		      list=(List<ChangeRequestDeliveryNoteCommand>)session.getAttribute("product-data");
		      if(list != null){
					for(ChangeRequestDeliveryNoteCommand deliveryNoteCommand : list){
						value=deliveryNoteCommand.getInvoiceNo();
						invoice=deliveryNoteCommand.getInvoiceName();
					}
		      }
		    }
		      else{
			  command=(ChangeRequestDeliveryNoteCommand)session.getAttribute("go-for-payments");
			  if(command != null){
				  value=command.getInvoiceNo();
				  invoice=command.getInvoiceName();
			  }
			  }
	} else {
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	DeliveryNoteCrDao deliveryNoteCrDao = (DeliveryNoteCrDao) hibernateContext.getBean("deliveryNoteCrDao");
		} catch (Exception ex) {
	ex.printStackTrace();
		}
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
	<div class="step-no-select" style="width: 230px;">
		<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INFO_LABEL)%>
		</div>
	</div>
	<div class="step-no-select">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_LABEL)%></div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.DELIVERY_NOTE_PREVIEW_LABEL)%></div>
	</div>
	<div class="step-selected-corner"></div>
</div>
<%
	}
%>
<%String str=": "; %>
<%String advance= "(Adv)"; %>
<div class="outline" style="width: 820px; margin-left: 10px;">
	<div class="first-row" style="width: 820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=value%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left: 110px;">
				<span class="property-value"><%=str%><%=DateUtils.format(new Date())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(deliveryNote.getBusinessName())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(deliveryNote.getInvoiceName())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Description</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(deliveryNoteProduct.getDescription())%></span>
			</div>
		</div>
	</div>
		<%
		if (list.size() !=0) {
	%>
 			<div style="width:821px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Product Quantity</td><td>Product Cost (<%=currency%>)</td><td>Bonus Quantity</td><td>Bonus Reason</td>
 		     <td>Total Cost (<%=currency%>)</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
		    for(ChangeRequestDeliveryNoteCommand deliveryNoteCommand : list){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(deliveryNoteCommand
							.getProductName())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(deliveryNoteCommand.getBatchNumer())%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(Integer.parseInt(deliveryNoteCommand.getProductQuantity()))%></td>
 	    		      <td align="right"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteCommand.getProductCost()))%></td>
 	    		     <%if(deliveryNoteCommand.getBonusQuantity().equals("")) {%>
 	    		      <td align="right"><%=StringUtil.format(deliveryNoteCommand.getBonusQuantity())%></td>
 	    		     <%}else{ %>
 	    		      <td align="right"><%=StringUtil.quantityFormat(Integer.parseInt(deliveryNoteCommand.getBonusQuantity()))%></td>
 	    		      <%} %>
 	    		     
 	    		     
 	    		      <td align="right"><%=StringUtil.format(deliveryNoteCommand.getBonusReason())%></td>
 	    		      <td align="right"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteCommand.getProductCost()) * Integer.parseInt(deliveryNoteCommand.getProductQuantity()))%></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
			<% } %>
	
	<%
		if (deliveryNoteProduct != null) {
	%>
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 150px;">
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Payment
					Type</div>
			<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(deliveryNoteProduct
						.getPaymentType())%></span>
				</div>
			</div>
			<%if(deliveryNoteProduct.getPaymentType().toLowerCase().equalsIgnoreCase("Cheque")){ %>
			<%if(! StringUtil.format(deliveryNoteProduct.getBankName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Bank Name</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(deliveryNoteProduct.getBankName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(deliveryNoteProduct.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(deliveryNoteProduct.getBranchName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(deliveryNoteProduct.getChequeNo()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Cheque No</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(deliveryNoteProduct.getChequeNo())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(deliveryNoteProduct.getBankLocation()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Bank Location</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(deliveryNoteProduct.getBankLocation())%></span>
				</div>
			</div>
			<%} }%>
			<%if(StringUtil.format(deliveryNoteProduct.getBankName()).isEmpty()||StringUtil.format(deliveryNoteProduct.getChequeNo()).isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
			<div class="separator" style="height: 30px; width: 260px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(user.getVbEmployee().getFirstName())%><%="   "%><%=StringUtil.format(user.getVbEmployee().getLastName())%></span>
			</div>
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 260px; margin-top: -40px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(deliveryNote.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 260px;"></div>
		<div class="paymentset">
		<%
		if (list != null && list.size() != 0) {
	    %>
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getPresentPayable()))%></span>
				</div>
			</div>
			<%} %>
			<div class="form-row">
				<div class="payment-span-label">Present Advance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getPresentAdvance()))%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Previous Credit</div>
					<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getPreviousCredit()))%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getTotalPayable()))%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
					<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getPresentPayment()))%></span>
				</div>
			</div>
			<%if(list == null){ %>
			 <%
			 float totalPayable=Float.parseFloat(deliveryNoteProduct.getTotalPayable());
			float presentPayment=Float.parseFloat(deliveryNoteProduct.getPresentPayment());
			 float balance=(totalPayable - presentPayment);
			%> 
			<%
			if(balance < 0.0){
			%>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Math.abs(Float.parseFloat(deliveryNoteProduct.getBalance())))%> <%=advance %></span>
				</div>
			</div>
			
			<%}else{ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct.getBalance()))%></span>
				</div>
			</div> 
			<%}
			}else{ %>
			 <%
			float totalPayable=Float.parseFloat(deliveryNoteProduct.getTotalPayable());
			float presentPayment=Float.parseFloat(deliveryNoteProduct.getPresentPayment());
			float balance=(totalPayable - presentPayment);
			%> 
			<%
			if(balance < 0.0){
			%>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Math.abs(Float.parseFloat(deliveryNoteProduct.getBalance())))%> <%=advance %></span>
				</div>
			</div>
			
			<%}else{ %>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct.getBalance()))%></span>
				</div>
			</div>
			<%} 
			}%>
		</div>
	</div>
	<%
		}
	%>
</div>