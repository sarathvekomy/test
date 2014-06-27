<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteProductCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteCommand"%>
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
ChangeRequestDeliveryNoteProductCommand deliveryNoteProduct = null;
ChangeRequestDeliveryNoteCommand command=null;
	String value=null;
	String invoice=null;
	List<ChangeRequestDeliveryNoteCommand> list=new ArrayList();
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		deliveryNote = (ChangeRequestDeliveryNoteCommand) session.getAttribute("deliverynote-info");
		deliveryNoteProduct = (ChangeRequestDeliveryNoteProductCommand) session.getAttribute("delivery-note-product");
	  list=(List<ChangeRequestDeliveryNoteCommand>)session.getAttribute("product-data");
	  command=(ChangeRequestDeliveryNoteCommand)session.getAttribute("go-for-payments");
	  if(list != null){
				for(ChangeRequestDeliveryNoteCommand deliveryNoteCommand : list){
					value=deliveryNoteCommand.getInvoiceNo();
					invoice=deliveryNoteCommand.getInvoiceName();
				}
	  }
	  else{
		 value= command.getInvoiceNo();
		 invoice=command.getInvoiceName();
	  }
	} else {
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
	.getWebApplicationContext(request.getSession()
			.getServletContext());
	ChangeTransactionDao deliverNoteDao = (ChangeTransactionDao) hibernateContext.getBean("changeTransactionDao");
		} catch (Exception ex) {
	ex.printStackTrace();
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
		if (list != null) {
	%>

	<div class="invoice-main-table" style="width: 820px;overflow:hidden;">
		<div class="invoice-boxes-colored" style="width: 40px;">
			<div>
				<span class="span-label">S.No</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 155px;">
			<div>
				<span class="span-label">Product Name</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 70px;">
			<div>
				<span class="span-label">Batch No</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 100px;">
			<div>
				<span class="span-label">Product Quantity</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 110px;">
			<div>
				<span class="span-label">Product Cost</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 90px;">
			<div>
				<span class="span-label">Bonus Quantity</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="width: 120px;">
			<div>
				<span class="span-label">Bonus Reason</span>
			</div>
		</div>
		<div class="invoice-boxes-colored"
			style="border-right: none; width: 128px;">
			<div>
				<span class="span-label">Total Cost</span>
			</div>
		</div>
		<%
			int count=0;
				for(ChangeRequestDeliveryNoteCommand deliveryNoteCommand : list){
			count++;
		%>
		<%
			String reason=StringUtil.format(deliveryNoteCommand.getBonusReason());
		    String batchNo=StringUtil.format(deliveryNoteCommand.getBatchNumer());
			if((deliveryNoteCommand.getProductName()).length()>25||reason.length()>25||batchNo.length()>8){
				int len=(deliveryNoteCommand.getProductName()).length();
		%>
		<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
			id="number-<%=count%>" type="hidden" value=<%=count%>> <input
			id="bonus-<%=count%>" type="hidden" value=<%=reason.length()%>>
			<input id="batch-<%=count%>" type="hidden" value=<%=batchNo%>>
		<script type="text/javascript">
		TransactionChangeRequestHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val(),$('#bonus-<%=count%>').val(),$('#batch-<%=count%>').val());
			</script>
		<%
			}
		%>
		<input id="num-<%=count%>" type="hidden" value=<%=count%>>
		<script type="text/javascript">
		TransactionChangeRequestHandler.addColor($('#num-<%=count%>').val());
		</script>
		<div class="result-row" style="width: 820px;" id="row-<%=count%>">
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 40px;">
				<div>
					<span class="property"><%=count%></span>
				</div>
			</div>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 155px;">
				<div>
					<span class="property"><%=StringUtil.format(deliveryNoteCommand
							.getProductName())%></span>
				</div>
			</div>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 70px;">
				<div>
					<span class="property"><%=StringUtil.format(deliveryNoteCommand
							.getBatchNumer())%></span>
				</div>
			</div>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(Integer.parseInt(deliveryNoteCommand.getProductQuantity()))%></span>
				</div>
			</div>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">
				<div>
					<span class="property-right-float"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteCommand.getProductCost()))%></span>
				</div>
			</div>
			<%if(deliveryNoteCommand.getBonusQuantity().equals("")) {%>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 90px;">
				<div>
					<span class="property-right-float"><%=StringUtil.format(deliveryNoteCommand.getBonusQuantity())%></span>
				</div>
			</div>
			<%}else{ %>
			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 90px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(Integer.parseInt(deliveryNoteCommand.getBonusQuantity()))%></span>
				</div>
			</div>
			<%} %>
			<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 120px;">
				<div>
					<span class="property"><%=StringUtil.format(deliveryNoteCommand
							.getBonusReason())%></span>
				</div>
			</div>

			<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 116px; border-right: none;">
				<div>
					<span class="property-right-float"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteCommand.getProductCost()) * Integer.parseInt(deliveryNoteCommand.getProductQuantity()))%></span>
				</div>
			</div>
		</div>
		<%
			}
		%>
		</div>
		<%
			}
		%> 
	
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
			<%if(! StringUtil.format(deliveryNoteProduct.getBankName()).isEmpty()) {%>
			<%if( StringUtil.format(deliveryNoteProduct.getBranchName()).isEmpty()) {%>
			<div class="form-row">
				<div class="payment-span-label" style="width: 90px;margin-left: 2px;">Branch Name</div>
				<div class="input-field" style="margin-left: 7px;">
				<span class="span-payment" style="float:left;"><%=str%></span>
				</div>
			</div>
			<%} %>
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
			<div class="separator" style="height: 30px; width: 260px;"></div>
			<%} %>
			<%if(StringUtil.format(deliveryNoteProduct.getBankName()).isEmpty()||StringUtil.format(deliveryNoteProduct.getChequeNo()).isEmpty()) {%>
			<div class="separator" style="height: 120px; width: 260px;"></div>
			<%} %>
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
			<div class="form-row">
				<div class="payment-span-label">Present Payable</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getPresentPayable()))%></span>
				</div>
			</div>
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
			<div class="form-row">
				<div class="payment-span-label">Balance</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(deliveryNoteProduct
						.getBalance()))%></span>
				</div>
			</div>
		</div>
	</div>
	<%
		}
	%>
</div>