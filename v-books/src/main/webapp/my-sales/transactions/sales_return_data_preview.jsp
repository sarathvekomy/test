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
<div class="outline" style="overflow:hidden !important; width:800px !important;">
<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(sale.getInvoiceNo())%></span>
			</div>
		</div>
		<div class="right-align" style="width: 355px !important;">
				<div class="number-lable" style="margin-left:-70px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:30px;">
				<span class="property-value"><%=str%><%=DateUtils.format(new Date())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:800px !important;">
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
	 <%
	if(!salesReturnList.isEmpty()) {
	%>
 			<div style="width:800px; float:left;">
 			<table  border="1" width="100%">
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Damaged</td><td>Resalable</td><td>Total Quantity</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(SalesReturnCommand command:salesReturnList){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:800px;"><%}
					else{%>
						<tr style="background:#bababa;width:800px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(command.getProductName())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(command.getBatchNumber())%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(command.getDamaged())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(command.getResalable())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(command.getTotalQty())%></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
		<div class="paymentset-row" style="margin-top: 10px;">
								<div class="paymentset">
									<div class="separator" style="height: 10px;width:80px;"></div>	
									<div class="first-row" style="width:799px;border-bottom:none;height: auto; word-wrap:break-word; min-height:30px;margin-bottom:50px;">
										<div class="left-align">
											<div class="number-lable">
												<span class="span-label">Remarks</span>
											</div>
											<div class="number" style="">
												<span style="color: #1C8CF5;font:12px arial;"><%=StringUtil.format(sale.getRemarks())%><%="   "%></span>
											</div>
										</div>
										
									</div> 
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
							</div>	
		</div><%} %></div>
