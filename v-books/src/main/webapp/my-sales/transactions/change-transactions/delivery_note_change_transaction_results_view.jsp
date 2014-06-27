<%@page import="com.vekomy.vbooks.hibernate.BaseDao"%>
<%@page import="com.vekomy.vbooks.spring.action.BaseAction"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
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
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
VbDeliveryNoteChangeRequestProducts vbDeliveryNoteChangeRequestProducts = null;
VbDeliveryNoteChangeRequestPayments vbDeliveryNoteChangeRequestPayments = null;
	VbDeliveryNoteChangeRequest vbDeliveryNoteChangeRequest = null;
	VbEmployee vbEmployee=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	String employee_subjects = "";
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
		.getWebApplicationContext(request.getSession()
		.getServletContext());
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext.getBean("changeTransactionDao");
		if (changeTransactionDao != null) {
	Integer id = Integer.valueOf(request.getParameter("id"));
	vbDeliveryNoteChangeRequest = changeTransactionDao.getDeliveryNoteChangeRequest(id , user.getOrganization());
	if(vbDeliveryNoteChangeRequest != null){
		vbEmployee=changeTransactionDao.getSalesExecutiveFullName(vbDeliveryNoteChangeRequest.getCreatedBy(), user.getOrganization());
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
			<%String invoiceNumber=StringUtil.format(vbDeliveryNoteChangeRequest.getInvoiceNo());%>
				<span class="property-value"><%=str%>
				<a id="change-request-dn-Invoice-number" href="#" class="<%=invoiceNumber%>" style="text-decoration: none;">
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
				<span class="property-value"><%=str%><%=DateUtils.format(vbDeliveryNoteChangeRequest.getCreatedOn())%></span>
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
if(vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestProductses() != null){
	 %>
	<%
		Set<VbDeliveryNoteChangeRequestProducts> productsSet = vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestProductses();
		Object[] arraySet=productsSet.toArray();
	%>
	<%
		if (productsSet.size() > 0) {
	%>

	<div class="invoice-main-table" style="overflow:hidden;">
		<div class="inner-table" style="width: 1000px;">
			<div class="invoice-boxes-colored" style="width: 80px;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Product Name</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 100px;">
				<div>
					<span class="span-label">Batch No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 100px;">
				<div>
					<span class="span-label">Product Quantity</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Product Cost (<%=currency%>)</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 95px;">
				<div>
					<span class="span-label">Bonus Quantity</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Bonus Reason</span>
				</div>
			</div>
			<div class="invoice-boxes-colored"
				style="border-right: solid 1px gray; width: 143px;">
				<div>
					<span class="span-label">Total Cost (<%=currency%>)</span>
				</div>
			</div>
			<%
				int count=0;
					for(int i=0;i<arraySet.length;i++)
				
				{
				count++;
				VbDeliveryNoteChangeRequestProducts product=(VbDeliveryNoteChangeRequestProducts)arraySet[i];
			%>
			<%
				String reason=StringUtil.format(product.getBonusReason().replace(",1", ""));
			    String batchNo=StringUtil.format(product.getBatchNumber().replace(",0", ""));
				if((product.getProductName().replace(",0", "")).length()>25||reason.length()>25||batchNo.length()>8){
					int len=(product.getProductName().replace(",0", "")).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>> <input
				id="bonus-<%=count%>" type="hidden" value=<%=reason.length()%>>
				<input id="batch-<%=count%>" type="hidden" value=<%=batchNo%>>
			<script type="text/javascript">
			DashbookHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val(),$('#bonus-<%=count%>').val(),$('#batch-<%=count%>').val());
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
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getProductName().replace(",0", ""))%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getBatchNumber().replace(",0", ""))%></span>
					</div>
				</div>
				<%
				if(product.getProductQty().contains(",1")){
					String productQty=product.getProductQty().replace(",1", "");
					String formatProductQty=StringUtil.quantityFormat(Integer.parseInt(productQty));				
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=formatProductQty%></span>
					</div>
				</div>
               <%}else {
            	   String productQty=product.getProductQty().replace(",0", "");
					String formatProductQty=StringUtil.quantityFormat(Integer.parseInt(productQty));	
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
					<div>
						<span class="property-right-float"><%=formatProductQty%></span>
					</div>
				</div>
				<%} %>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(Float.parseFloat(product.getProductCost().replace(",0", "")))%></span>
					</div>
				</div>
				
				<%
				if(product.getBonusQty().equals("")){
				   if(product.getBonusQty().contains(",1")){
					String bonusQty=product.getBonusQty().replace(",1", "");
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=bonusQty%></span>
					</div>
				</div>
				<%}else {
					String bonusQty=product.getBonusQty().replace(",0", "");
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float"><%=bonusQty%></span>
					</div>
				</div>
				<%} 
				}else{
				 if(product.getBonusQty().contains(",1")){
					String bonusQty=product.getBonusQty().replace(",1", "");
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=bonusQty%></span>
					</div>
				</div>
				<%}else {
					String bonusQty=product.getBonusQty().replace(",0", "");
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float"><%=bonusQty%></span>
					</div>
				</div>
				<%}  
				 }%>
				 <% 
				if(product.getBonusReason().contains(",1")){
					String bonusReason=product.getBonusReason().replace(",1", "");
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property" style="font-size: 12px; color: #6600FF;"><%=StringUtil.format(bonusReason)%></span>
					</div>
				</div>
				<%}else {
					String bonusReason=product.getBonusReason().replace(",0", "");
				%>
               <div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(bonusReason)%></span>
					</div>
				</div>
				<%} %>
				
				<%
				if(product.getTotalCost().contains(",1")){
					String totalCost=product.getTotalCost().replace(",1", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));				
					%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 143px; border-right: none;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=formatTotalCost%></span>
					</div>
				</div>
				<%}else {
					String totalCost=product.getTotalCost().replace(",0", "");
					String formatTotalCost=StringUtil.currencyFormat(Float.parseFloat(totalCost));		
				%>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 143px; border-right: none;">
					<div>
						<span class="property-right-float"><%=formatTotalCost%></span>
					</div>
				</div>
				<%} %>
			</div>
			<%
				}
			%>
			
		</div>
	</div>
	<%
				}
			%>
			<%
				}
			%>

	<%
		Set<VbDeliveryNoteChangeRequestPayments> paymentSet = vbDeliveryNoteChangeRequest.getVbDeliveryNoteChangeRequestPaymentses();
	%>
	<%
		if (paymentSet != null) {
	%>
	<%
		for (VbDeliveryNoteChangeRequestPayments productPayments : paymentSet) {
	%>
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 150px;">
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment
					Type</div>
				<div class="input-field" style="margin-left: 7px;">
					<span style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(productPayments
							.getPaymentType())%></span>
				</div>
			</div>
			<%
			if(! StringUtil.format(productPayments.getBankName()).isEmpty()) {%>
			<%if( StringUtil.format(productPayments.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%></span>
				</div>
			</div>
			<%} %>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Bank Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(productPayments.getBankName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(productPayments.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(productPayments.getBranchName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(productPayments.getChequeNo()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Cheque No</div>
				<div class="input-field" style="margin-left: 7px;"><span
						style="color: #1C8CF5; font: 12px arial;"><%=str%><%=StringUtil.format(productPayments.getChequeNo())%></span>
				</div>
			</div>
			<div class="separator" style="height: 30px; width: 260px;"></div>
			<%} %>
			<%if(StringUtil.format(productPayments.getBankName()).isEmpty()||StringUtil.format(productPayments.getBranchName()).isEmpty()||StringUtil.format(productPayments.getChequeNo()).isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label" style="width: 105px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbEmployee.getFirstName())%><%="   "%><%=StringUtil.format(vbEmployee.getLastName())%></span>
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
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(productPayments.getPresentPayable()))%></span>
				</div>
			</div>
			 <div class="form-row">
				<div class="payment-span-label">Present Advance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(productPayments
							.getPresentAdvance()))%></span>
				</div>
			</div> 
			<div class="form-row">
				<div class="payment-span-label">Previous Credit</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(productPayments
							.getPreviousCredit()))%></span>
				</div>
			</div>
			<%
				if(productPayments.getTotalPayable().contains(",1")){
					String totalPayable=productPayments.getTotalPayable().replace(",1", "");
					String formatTotalPayable=StringUtil.currencyFormat(Float.parseFloat(totalPayable));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatTotalPayable%></span>
				</div>
			</div>
			<%}else {
				String totalPayable=productPayments.getTotalPayable().replace(",0", "");
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
				if(productPayments.getPresentPayment().contains(",1")){
					String presentPayment=productPayments.getPresentPayment().replace(",1", "");
					String formatPresentPayment=StringUtil.currencyFormat(Float.parseFloat(presentPayment));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatPresentPayment%></span>
				</div>
			</div>
			<%}else {
				String presentPayment=productPayments.getPresentPayment().replace(",0", "");
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
				if(productPayments.getBalance().contains(",1")){
					String balance=productPayments.getBalance().replace(",1", "");
					String formatBalance=StringUtil.currencyFormat(Float.parseFloat(balance));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatBalance%></span>
				</div>
			</div>
			<%}else {
				String balance=productPayments.getBalance().replace(",0", "");
				String formatBalance=StringUtil.currencyFormat(Float.parseFloat(balance));		
				%>
				<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatBalance%></span>
				</div>
			</div>
			<%} %>
			<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
	</div>
	<%
		}
	%>
	<%
		}
	%>
<div class="first-row">
		<div class="left-align">
			<div class="number-lable" style="margin-left: -13px;">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class="number">
				<span class="property-value" style="width: 200px;"><%=str%><%=StringUtil.format(vbDeliveryNoteChangeRequest.getCrDescription())%></span>
			</div>
		</div>
</div>
</div>
