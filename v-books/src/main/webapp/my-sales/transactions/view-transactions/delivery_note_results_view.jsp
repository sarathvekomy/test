<%@page import="com.vekomy.vbooks.hibernate.BaseDao"%>
<%@page import="com.vekomy.vbooks.spring.action.BaseAction"%>
<%@page import="com.vekomy.vbooks.mysales.dao.DeliveryNoteDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult"%>
<%@page
	import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts"%>
	<%@page
	import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page
	import="com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNote"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
	DeliveryNoteViewResult vbDeliveryNote = null;
	List<DeliveryNoteViewResult> resultList=null;
	List<VbDeliveryNoteProducts> list=new ArrayList<VbDeliveryNoteProducts>();
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
		.getWebApplicationContext(request.getSession()
		.getServletContext());
		DeliveryNoteDao deliveryNoteDao = (DeliveryNoteDao) hibernateContext
		.getBean("deliveryNoteDao");
		if (deliveryNoteDao != null) {
	int id = Integer.parseInt(request.getParameter("id"));
	resultList = deliveryNoteDao.getDeliveryNoteProducts(id , user.getOrganization());
	vbDeliveryNote=resultList.get(0);
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
%>
<div class="outline" style="margin-left: 10px;">
	<div class="first-row">
	 <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
			<%String invoiceNumber=StringUtil.format(vbDeliveryNote.getInvoiceNo());%>
			<%Integer deliveryNoteId=vbDeliveryNote.getId();%>
				<span class="property-value"><%=str%>
				<a id="change-request-dn-Invoice-number" href="#" class="<%=invoiceNumber%>" align="<%= deliveryNoteId %>"  style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
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
				<span class="property-value"><%=str%><%=vbDeliveryNote.getCreatedDate()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbDeliveryNote.getBusinessName())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbDeliveryNote.getInvoiceName())%></span>
			</div>
		</div>
	</div>
	<%
	DeliveryNoteViewResult productList=resultList.get(0);
	if(vbDeliveryNote.getInvoiceNo().contains("/DN/")){
	 %>
	 <div style="width:960px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Product Cost (<%=currency%>)</td><td>Product Quantity</td><td>Bonus Quantity</td><td>Bonus Reason</td>
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
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getProduct())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(product.getBatchNumber())%></td>
 	    		     <td align="right"><%=product.getCostProduct()%></td>
 	    		     <td align="right"><%=StringUtil.format(product.getQtyProduct())%></td>
 	    		     <td align="right"><%=product.getQtyBonus()%></td>
 	    		      <td align="right"><%=StringUtil.format(product.getBonusReason())%></td>
				    <td align="right"><%=product.getTotalCostProduct()%></td>
 	    		    </tr>
 					<% }%>
 					</table>
 					</div>	
			<%
				}
			%>

	<%
		if (resultList != null) {
	%>
	
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 150px;">
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment
					Type</div>
				<div class="input-field" style="margin-left: 7px;">
					<span style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(vbDeliveryNote
							.getPaymentType())%></span>
				</div>
			</div>
			<%
			if(! StringUtil.format(vbDeliveryNote.getBankName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Bank Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(vbDeliveryNote.getBankName())%></span>
				</div>
			</div>
			<%if( StringUtil.format(vbDeliveryNote.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%></span>
				</div>
			</div>
			<%} %>
			<%} %>
			<%if(! StringUtil.format(vbDeliveryNote.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(vbDeliveryNote.getBranchName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(vbDeliveryNote.getChequeNo()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Cheque No</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(vbDeliveryNote.getChequeNo())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(vbDeliveryNote.getBankLocation()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Bank Location</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(vbDeliveryNote.getBankLocation())%></span>
				</div>
			</div>
			<div class="separator" style="height: 10px; width: 260px;"></div>
			<%} %>
			<%if(StringUtil.format(vbDeliveryNote.getBankName()).isEmpty()||StringUtil.format(vbDeliveryNote.getBranchName()).isEmpty()||StringUtil.format(vbDeliveryNote.getChequeNo()).isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
			<div class="separator" style="height: 10px; width: 260px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label" style="width: 105px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbDeliveryNote.getSalesExecutive())%></span>
			</div> 
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 240px; margin-top: -40px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbDeliveryNote
							.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 400px;"></div>
		<div class="paymentset">
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getPresentPayable()%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Present Advance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getPresentAdvance()%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Previous Credit</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getPreviousCredit()%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getTotalPayable()%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getPresentPayment()%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=vbDeliveryNote
							.getBalance()%></span>
				</div>
			</div>
			<div style="float: left;margin-right:10px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
		</div>
	</div>
	<%
		}
	%>
</div>
