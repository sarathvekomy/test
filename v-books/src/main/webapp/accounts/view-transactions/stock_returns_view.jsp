
<%@page import="com.vekomy.vbooks.mysales.dao.SalesReturnDao"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.accounts.command.SalesReturnCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturnProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesReturn"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page
	import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
    VbSalesReturnProducts salesReturnProducts=null;
    VbSalesReturn salesReturn=null;
    VbEmployee vbEmployee=null;
    List<VbSalesReturnProducts> list=new ArrayList<VbSalesReturnProducts>();
	boolean flag = false;
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
		list=salesReturnDao.getSalesReturnProductsDetails(salesReturnId , user.getOrganization());
		salesReturn=list.get(0).getVbSalesReturn();
	}
	if(salesReturn != null){
		vbEmployee=salesReturnDao.getSalesExecutiveFullName(salesReturn.getCreatedBy(),user.getOrganization());
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
			<div class="number">
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
		if (!list.isEmpty()) {
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
					for(int i=0;i<list.size();i++){
						count++;
						VbSalesReturnProducts product=list.get(i);
					float cost=product.getTotalCost();
					total=total+cost;
			%>
			<%
				if((product.getProductName()).length()>45){
					int len=(product.getProductName()).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesBookHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val());
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
				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width: 200px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getProductName())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(product.getBatchNumber())%></span>
					</div>
				</div>
                <div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getDamaged())%></span>
					</div>
				</div>

				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getResalable())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 90px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getTotalQty())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 125px;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(product.getCost())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 140px; border-right: none;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(product.getTotalCost())%></span>
					</div>
				</div>

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
		<div class="paymentset" style="height:90px;">
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
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(salesReturn.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 380px;"></div>
		<div class="paymentset" style="margin-left: 18px;height:100px;">
			<div class="form-row">
				<div class="payment-span-label">Total</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span id="total" class="span-payment"><%=StringUtil.currencyFormat(salesReturn.getProductsGrandTotal()) %></span>
				</div>
			</div>
			
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: 2px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	</div>

</div>
