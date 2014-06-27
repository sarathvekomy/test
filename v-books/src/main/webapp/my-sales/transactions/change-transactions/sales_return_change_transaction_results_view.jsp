<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
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
VbSalesReturnChangeRequestProducts vbSalesReturnChangeRequestProducts=null;
    VbSalesReturnChangeRequest vbSalesReturnChangeRequest=null;
    VbEmployee vbEmployee=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao)hibernateContext.getBean("changeTransactionDao");
	if(changeTransactionDao!=null)
	{
		int salesReturnCRId=Integer.parseInt(request.getParameter("id"));
		vbSalesReturnChangeRequest=changeTransactionDao.getSalesReturnProductsDetails(salesReturnCRId , user.getOrganization());
			}
	if(vbSalesReturnChangeRequest != null){
		vbEmployee=changeTransactionDao.getSalesExecutiveFullNameSalesReturn(vbSalesReturnChangeRequest.getCreatedBy(),user.getOrganization());
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
<div class="outline" style="margin-left:20px;">
<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
			<%String invoiceNumber=StringUtil.format(vbSalesReturnChangeRequest.getInvoiceNo());%>
			<a id="change-request-salesreturn-Invoice-number" href="#" class="<%=invoiceNumber%>" style="text-decoration: none;">
				<span class="property-value"><%=str%><%=invoiceNumber %></span>
			</a>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=DateUtils.format(vbSalesReturnChangeRequest.getCreatedOn())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbSalesReturnChangeRequest.getBusinessName())%></span>
			</div>
		</div>
		
		<%
		if(vbSalesReturnChangeRequest.getInvoiceName().contains(",1")){
		%><div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value" style="font-size: 12px; color: #6600FF;"><%=str%><%=StringUtil.format(vbSalesReturnChangeRequest.getInvoiceName().replace(",1", ""))%></span>
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
				<span class="property-value"><%=str%><%=StringUtil.format(vbSalesReturnChangeRequest.getInvoiceName().replace(",0", ""))%></span>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<%
		Set<VbSalesReturnChangeRequestProducts> productsSet = vbSalesReturnChangeRequest.getVbSalesReturnChangeRequestProductses();
	%>
	<%
		if (!productsSet.isEmpty()) {
	%>

	<div class="invoice-main-table">
		<div class="inner-table" style="width: 960px;">
			<div class="invoice-boxes-colored" style="width: 80px;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="width: 200px;">
				<div>
					<span class="span-label">Product Name</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Batch No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="width:80px;">
				<div>
					<span class="span-label">Damaged</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="width: 80px;">
				<div>
					<span class="span-label">Resalable</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 90px;">
				<div>
					<span class="span-label">Total quantity</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 125px;">
				<div>
					<span class="span-label">Cost (<%=currency%>)</span>
				</div>
			</div>
			<div class="invoice-boxes-colored"
				style="border-right: none; width: 153px;">
				<div>
					<span class="span-label">Total Cost (<%=currency%>)</span>
				</div>
			</div>
			<%
				int count=0;
					float total=0;
					float cost=0;
					for(VbSalesReturnChangeRequestProducts product : productsSet){
				    count++;
				    if(product.getTotalCost().contains(",1")){
				    	String productcost=product.getTotalCost().replace(",1","");
					  cost=Float.parseFloat(productcost);
				    }else{
				    	String productcost=product.getTotalCost().replace(",0","");
						  cost=Float.parseFloat(productcost);
				    }
					total=total+cost;
			%>
			<%
				if((product.getProductName()).length()>45){
					int len=(product.getProductName()).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DashbookHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val());
			</script>
			<%
				}
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DashbookHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>">
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<%
				if(product.getProductName().contains(",1")){
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 200px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=StringUtil.format(product.getProductName().replace(",1", ""))%></span>
					</div>
				</div>
				<%}else {%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 200px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getProductName().replace(",0", ""))%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getBatchNumber().contains(",1")){
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 145px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=StringUtil.format(product.getBatchNumber().replace(",1", ""))%></span>
					</div>
				</div>
				<%}else {%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getBatchNumber().replace(",0", ""))%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getDamaged().contains(",1")){
					String damaged=product.getDamaged().replace(",1", "");
					String formatDamaged=StringUtil.quantityFormat(Integer.parseInt(damaged));				
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 80px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=formatDamaged%></span>
					</div>
				</div>
				<%}else {
					String damaged=product.getDamaged().replace(",0", "");
					String formatDamaged=StringUtil.quantityFormat(Integer.parseInt(damaged));
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 80px;">
					<div>
						<span class="property"><%=formatDamaged%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getResalable().contains(",1")){
					String resalable=product.getResalable().replace(",1", "");
					String formatResalable=StringUtil.quantityFormat(Integer.parseInt(resalable));	
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 80px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=formatResalable%></span>
					</div>
				</div>
				<%}else {
					String resalable=product.getResalable().replace(",0", "");
					String formatResalable=StringUtil.quantityFormat(Integer.parseInt(resalable));	
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 80px;">
					<div>
						<span class="property"><%=formatResalable%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getTotalQty().contains(",1")){
					String totalQty=product.getTotalQty().replace(",1", "");
					String formatTotalQty=StringUtil.quantityFormat(Integer.parseInt(totalQty));	
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 90px;">
					<div>
						<span class="property" style="font-size: 12px;color: #6600FF;"><%=formatTotalQty%></span>
					</div>
				</div>
				<%}else {
					String totalQty=product.getTotalQty().replace(",0", "");
					String formatTotalQty=StringUtil.quantityFormat(Integer.parseInt(totalQty));	
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 90px;">
					<div>
						<span class="property"><%=formatTotalQty%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getCost().contains(",1")){
					String productcost=product.getCost().replace(",1", "");
					String formatCost=StringUtil.currencyFormat(Float.parseFloat(productcost));
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 125px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=formatCost%></span>
					</div>
				</div>
				<%}else {
					String productcost=product.getCost().replace(",0", "");
					String formatCost=StringUtil.currencyFormat(Float.parseFloat(productcost));
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 125px;">
					<div>
						<span class="property"><%=formatCost%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getTotalCost().contains(",1")){
					String totalCost=product.getTotalCost().replace(",1", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 140px; border-right: none;">
					<div>
						<span class="property" style="font-size: 12px;color: #6600FF;"><%=formatTotalCost%></span>
					</div>
				</div>
				<%}else {
					String totalCost=product.getTotalCost().replace(",0", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 140px; border-right: none;">
					<div>
						<span class="property"><%=formatTotalCost%></span>
					</div>
				</div>
				<%} %>
			</div>
			<%
				}
			%>
			<%
				}
			%>
		</div>
	</div>

	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 100px;">
			<div class="separator" style="height: 60px; width: 80px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 300px;">
				<div class="payment-span-label" style="width:100px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbEmployee.getFirstName())%><%="   "%><%=StringUtil.format(vbEmployee.getLastName())%></span>
			</div>
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 280px; margin-top: -41px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbSalesReturnChangeRequest.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 380px;"></div>
		<div class="paymentset" style="margin-left: 18px; height: 100px;">
		<%if(vbSalesReturnChangeRequest.getProductsGrandTotal().contains(",1")){ 
		  String grandTotal=vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",1", "");
		  String formatGrandTotal=StringUtil.currencyFormat(Float.parseFloat(grandTotal));
		%>
			<div class="form-row">
				<div class="payment-span-label">Total</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span id="total" class="span-payment" style="color: #6600FF; font-size: 12px;"><%=formatGrandTotal%></span>
				</div>
			</div>
			<%}else{ 
				String grandTotal=vbSalesReturnChangeRequest.getProductsGrandTotal().replace(",0", "");
				String formatGrandTotal=StringUtil.currencyFormat(Float.parseFloat(grandTotal));
			%>
			<div class="form-row">
				<div class="payment-span-label">Total</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span id="total" class="span-payment"><%=formatGrandTotal%></span>
				</div>
			</div>
			<%} %>
				<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
	</div>
<div class="first-row">
<div class="left-align">
			<div class="number-lable" style="margin-left: -13px;">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbSalesReturnChangeRequest.getCrDescription())%></span>
			</div>
</div>
</div>
</div>
