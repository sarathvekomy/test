
<%@page import="com.vekomy.vbooks.mysales.dao.SalesReturnDao"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturn"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
%>
<%
	DecimalFormat df = new DecimalFormat("#0.00");
%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
    VbSalesReturnProducts salesReturnProducts=null;
    VbSalesReturn salesReturn=null;
	boolean flag = false;
	boolean preview = false;
	List<SalesReturnCommand> salesReturnList=new ArrayList<SalesReturnCommand>();
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		salesReturnList=(List<SalesReturnCommand>)session.getAttribute("save-sales-return-products");

	} else {
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	SalesReturnDao salesReturnDao = (SalesReturnDao) hibernateContext
			.getBean("salesReturnDao");
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
	<div class="step-no-select">
		<div class="tabs-title"><%=Msg.get(MsgEnum.SALES_RETURNS_LABEL)%>
		</div>
	</div>
	<div class="step-selected">
				<div class="step-no-select-corner"></div>
				<div class="tabs-title"><%=Msg.get(MsgEnum.CUSTOMER_PREVIEW_LABEL)%>
				</div>
			</div>
			<div class="step-selected-corner"></div>
</div>
<%
	}
%>
<%VbSalesReturn sale=salesReturnList.get(0); %>
<% SalesReturnCommand salesCommand=salesReturnList.get(0);
String str=": ";
%>
<div class="outline">
<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(sale.getInvoiceNo())%></span>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=DateUtils.format(new Date())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(sale.getBusinessName())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(sale.getInvoiceName())%></span>
			</div>
		</div>
	</div>
<%if(!salesReturnList.isEmpty())
{
%>
<div  class="invoice-main-table" >
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
		<div class="invoice-boxes-colored" style="width:80px;">
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
				<span class="span-label">Cost</span>
			</div>
		</div>
		<div class="invoice-boxes-colored" style="border-right: none;
width: 153px;">
			<div>
				<span class="span-label">Total Cost</span>
			</div>
		</div>
		<%
		int count=0;
		float total=0;
		for(SalesReturnCommand command:salesReturnList){
			count++;
 
			%> 
			<%
			if((command.getProductName()).length()>45){
				int len=(command.getProductName()).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>>
			<input id="number-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesReturnsHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val());
			</script>
			<%
			}
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DeliveryNoteHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>"
			>
		<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width:80px;">
			<div>
				<span class="property"><%=count%></span>
			</div>
		</div>
		<div class="invoice-boxes invoice-boxes-<%=count%>"   style="width: 200px;">
			<div>
				<span class="property"><%=StringUtil.format(command.getProductName())%></span>
			</div>
		</div>
		<div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 145px;">
			<div>
				<span class="property"><%=StringUtil.format(command.getBatchNumber())%></span>
			</div>
		</div>
         <div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 80px;">
			<div>
				<span class="property-right-float" ><%=StringUtil.quantityFormat(command.getDamaged())%></span>
			</div>
		</div>
		
		<div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 80px;">
			<div>
				<span class="property-right-float" ><%=StringUtil.quantityFormat(command.getResalable())%></span>
			</div>
		</div>
		<div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 90px;">
			<div>
				<span class="property-right-float" ><%=StringUtil.quantityFormat(command.getTotalQty())%></span>
			</div>
		</div>
		<div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 125px;">
			<div>
				<span class="property-right-float"><%=StringUtil.currencyFormat(command.getCost())%></span>
			</div>
		</div>
		<div class="invoice-boxes invoice-boxes-<%=count%>"  Style="width: 140px;border-right: none;">
			<div>
				<span class="property-right-float"><%=StringUtil.currencyFormat(command.getTotalCost())%></span>
			</div>
		</div>

	</div>
		<%
			}
		%>
		
		</div></div>
		
		<div class="paymentset-row" style="margin-top: 10px;">
								<div class="paymentset">
									<div class="separator" style="height: 60px;width:80px;"></div>	
									<div class="form-row" style="margin-left:2px;width:300px;"> 
									<div class="payment-span-label">Sales Executive</div>
									 <span style="color: #1C8CF5;font:12px arial;"><%=StringUtil.format(user.getVbEmployee().getFirstName())%><%="   "%><%=StringUtil.format(user.getVbEmployee().getLastName())%></span>
									 </div>
									 <div class="separator" style="height: 10px;width:60px;"></div>	
									 <div class="form-row" style="width:300px;margin-left:280px;margin-top:-43px;"> 
									<div class="payment-span-label" style="margin-left:40px;width:80px;">Customer</div>
									<span style="color: #1C8CF5;font:12px arial;"><%=StringUtil.format(sale.getBusinessName())%></span></div>
								</div>
								<div class="separator" style="height: 100px;width:380px;"></div>								
								<div class="paymentset"style="margin-left:30px;height:100px;">
								 <div class="form-row">
								<div class="payment-span-label" style="width: 115px;">Total</div>
										 <div class="input-field"><span class="appostaphie"><%=str%></span>
										<span id="total" class="span-payment"><%=StringUtil.currencyFormat(salesCommand.getGrandTotalCost())%></span></div>
									</div>
								</div>
								<div style="float: right;margin-right: 110px;margin-bottom: 20px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
							</div>			
		
		</div><%} %>
