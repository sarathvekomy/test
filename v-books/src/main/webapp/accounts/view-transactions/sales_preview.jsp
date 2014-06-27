<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBookProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBook"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
%>
<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>
<%
	
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    VbSalesBook vbSalesBook=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	SalesBookDao salesBookDao = (SalesBookDao)hibernateContext
			.getBean("accountsSalesBookDao");
	if(salesBookDao!=null)
	{
		int salesId=Integer.parseInt(request.getParameter("id"));
		vbSalesBook=salesBookDao.getSalesProduct(salesId , user.getOrganization());
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
		<div class="tabs-title"><%=Msg.get(MsgEnum.SALES_LABEL)%>
		</div>
	</div>
</div>
<%
	}
%>
          <div class="head">
				<div class="cont-left">
					<span class="property-value-view"><%=StringUtil.format(vbSalesBook.getSalesExecutive())%></span>
				</div>
			
			
			
				<div class="cont-right">
					<span class="property-value-view"><%=dateFormat.format(vbSalesBook.getCreatedOn())%></span>
				</div>
			</div>
			
<div class="main-table"
		style="width: 330px;margin-left:320px;">
		<div class="inner-table" style="width: 330px;">
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Advance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(vbSalesBook.getAdvance())%></span>
				</div>
			</div>	
				
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Opening Balance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(vbSalesBook.getOpeningBalance())%></span>
				</div>
			</div>	
			<div class="display-boxes-colored">
				<div>
					<span class="span-label">Closing Balance</span>
				</div>
			</div>
			<div class="display-boxes">
				<div>
					<span class="property-value-view"><%=StringUtil.currencyFormat(vbSalesBook.getClosingBalance())%></span>
				</div>
			</div>	
			</div></div>
			<% Set<VbSalesBookProducts> productsSet =vbSalesBook.getVbSalesBookProductses() ;
			%> 
			<% if (!productsSet.isEmpty()) { %>
			<div class="invoice-main-table" style="width: 1088px;border-top:solid 1px gray;margin-left:10px;">
		<div class="inner-table" style="width: 1115px;">
			<div class="invoice-boxes-colored" style="width: 40px;border-left:solid 1px gray;">
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
					<span class="span-label">Product Batch</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 100px;">
				<div>
					<span class="span-label">Quantity Allotted</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="width:160px;">
				<div>
					<span class="span-label">Quantity Opening Balance</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 95px;">
				<div>
					<span class="span-label">Quantity Sold</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 120px;">
				<div>
					<span class="span-label">Quantity To Factory</span>
				</div>
			</div>
			<div class="invoice-boxes-colored"
				style="border-right: solid 1px gray; width: 159px;">
				<div>
					<span class="span-label">Quantity Closing Balance</span>
				</div>
			</div>
			<div class="invoice-boxes-colored"
				style="border-right: solid 1px gray; width: 159px;">
				<div>
					<span class="span-label">Remarks</span>
				</div>
			</div>
			<% 
			int count=0;
			for(VbSalesBookProducts product : productsSet){
				count++;
			%>
			
			<%
			 String batchNo=StringUtil.format(product.getBatchNumber());
				if((product.getProductName()).length()>20||batchNo.length()>15){
					int len=(product.getProductName()).length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>>
				<input id="batch-<%=count%>" type="hidden" value=<%=batchNo%>>
			<script type="text/javascript">
			SalesBookHandler.checkLengthForAllotment($('#length-<%=count%>').val(),$('#number-<%=count%>').val(),$('#batch-<%=count%>').val());
			</script>
			<%
				}
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesBookHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width: 1087px;">
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 40px;border-left:solid 1px gray;">
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
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getQtyAllotted())%></span>
					</div>
				</div>


				<div class="invoice-boxes invoice-boxes-<%=count%>" style="width:160px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getQtyOpeningBalance())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 95px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getQtySold())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 120px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getQtyToFactory())%></span>
					</div>
				</div>

				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 159px; border-right: solid 1px gray;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(product.getQtyClosingBalance())%></span>
					</div>

				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 159px; border-right: none;">
					<div>
						<span class="property-right-float"><%=StringUtil.format(product.getRemarks())%></span>
					</div>

				</div>
			</div>
			<%
				}
			%>
			
		</div>
	</div>
<% } %>