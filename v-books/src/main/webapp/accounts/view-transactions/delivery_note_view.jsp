<%@page import="com.vekomy.vbooks.mysales.dao.DeliveryNoteDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnCommand"%>
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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
	VbDeliveryNoteProducts vbDeliveryNoteProducts = null;
	VbDeliveryNotePayments vbDeliveryNotePayments = null;
	VbDeliveryNote vbDeliveryNote = null;
	VbEmployee vbEmployee=null;
	List<VbDeliveryNoteProducts> list=new ArrayList<VbDeliveryNoteProducts>();
	boolean flag = false;
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
	list = deliveryNoteDao
			.getDeliveryNoteProducts(id , user.getOrganization());
			vbDeliveryNote=list.get(0).getVbDeliveryNote();
		}
		if(vbDeliveryNote != null){
			vbEmployee=deliveryNoteDao.getSalesExecutiveFullName(vbDeliveryNote.getCreatedBy(),user.getOrganization());
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
				<span class="property-value"><%=str%><%=StringUtil.format(vbDeliveryNote.getInvoiceNo())%></span>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=DateUtils.format(vbDeliveryNote.getCreatedOn())%></span>
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
if(list != null){
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
					for(int i=0;i<list.size();i++)
				
				{
				count++;
				VbDeliveryNoteProducts product=list.get(i);
			%>
			<%
				String reason=StringUtil.format(product.getBonusReason());
			 String batchNo=StringUtil.format(product.getBatchNumber());
				if((product.getProductName()).length()>25||reason.length()>25||batchNo.length()>8){
					int len=(product.getProductName()).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>> <input
				id="bonus-<%=count%>" type="hidden" value=<%=reason.length()%>>
				<input id="batch-<%=count%>" type="hidden" value=<%=batchNo%>>
			<script type="text/javascript">
			SalesBookHandler.checkLengthForDeliveryNote($('#length-<%=count%>').val(),$('#number-<%=count%>').val(),$('#bonus-<%=count%>').val(),$('#batch-<%=count%>').val());
			</script>
			<%
				}
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesBookHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>">
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getProductName())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getBatchNumber())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getProductQty())%></span>
					</div>
				</div>


				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(product.getProductCost())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getBonusQty())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getBonusReason())%></span>
					</div>
				</div>

				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 143px; border-right: none;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(product.getTotalCost())%></span>
					</div>

				</div>
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
		Set<VbDeliveryNotePayments> paymentSet = vbDeliveryNote
				.getVbDeliveryNotePaymentses();
	%>
	<%
		if (paymentSet != null) {
	%>
	<%
		for (VbDeliveryNotePayments productPayments : paymentSet) {
	%>
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 150px;">
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment
					Type</div>
				<div class="input-field" style="margin-left: 7px;">
					<span class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(productPayments
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
						class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(productPayments.getBankName())%></span>
				</div>
			</div>
			<%} %>
			<%if(! StringUtil.format(productPayments.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;"><span
						class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(productPayments.getBranchName())%></span>
				</div>
			</div>
			<%} %>
			
			<%if(! StringUtil.format(productPayments.getChequeNo()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left:2px;">Cheque No</div>
				<div class="input-field" style="margin-left: 7px;"><span
						class="span-payment" style="float:left;"><%=str%><%=StringUtil.format(productPayments.getChequeNo())%></span>
				</div>
			</div>
			<div class="separator" style="height: 30px; width: 260px;"></div>
			<%} %>
			<%if(StringUtil.format(productPayments.getBankName()).isEmpty()||StringUtil.format(productPayments.getBranchName()).isEmpty()||StringUtil.format(productPayments.getChequeNo()).isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label" style="width: 105px;">Sales Executive</div>
				<span class="span-payment" style="float:left;"><%=StringUtil.format(vbEmployee.getFirstName())%><%="   "%><%=StringUtil.format(vbEmployee.getLastName())%></span>
			</div>
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 240px; margin-top: -40px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span class="span-payment" style="float:left;"><%=StringUtil.format(vbDeliveryNote
							.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 400px;"></div>
		<div class="paymentset">
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getPresentPayable())%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Present Advance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getPresentAdvance())%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Previous Credit</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getPreviousCredit())%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Total Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getTotalPayable())%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Present Payment</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getPresentPayment())%></span>
				</div>
			</div>
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=StringUtil.currencyFormat(productPayments
							.getBalance())%></span>
				</div>
			</div>
			<div style="float: left;margin-right:10px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
		</div>
	</div>
	<%
		}
	%>
	<%
		}
	%>
</div>
