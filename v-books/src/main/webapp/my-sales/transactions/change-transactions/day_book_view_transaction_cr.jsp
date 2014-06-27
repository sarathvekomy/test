<%@page import="com.vekomy.vbooks.mysales.dao.DayBookDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookViewResult"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.*"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
	DayBookViewResult viewResult=null;
	List<DayBookViewResult> dayBookResultList=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	DayBookDao dayBookDao = (DayBookDao) hibernateContext
			.getBean("dayBookDao");
	if(dayBookDao!=null){
		int id=Integer.parseInt(request.getParameter("id"));
		dayBookResultList=dayBookDao.getDayBookOnId(id , user.getOrganization(),user.getName());
		viewResult=dayBookResultList.get(0);
	}
	
		} catch (Exception ex) {
	ex.printStackTrace();
		}
%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%String str=": "; %>
<div class="outline" style="width: 820px; margin-left: 10px;">
	<div class="first-row" style="width: 820px;">
	    <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Day Book No.</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getDayBookNo()%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Sales Executive</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getSalesExecutive()%></span>
			</div>
		</div>
		<%if(viewResult.getIsReturn() != false){ %>
		 <div class="right-align">
			<div class="number-lable" style="width: 170px; margin-top: -20px;">
				<span class="span-label" style="width:160px !important;">Reporting Manager Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getReportingManager()%></span>
			</div>
			</div>
		</div>
		<%} %>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Date</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getCreatedDate()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Opening Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getOpeningBalance()%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Total Expenses</span>
			</div>
		<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getTotalExpenses()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Total Payable</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getTotalPayable()%></span>
			</div>
		</div>
		<div class="center-align">
			<div class="number-lable">
				<span class="span-label">Total recieved</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getTotalRecieved()%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getBalance()%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount To Bank</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getAmountToBank()%></span>
			</div>
		</div>
		<%if(viewResult.getIsReturn() != false){ %>
		<div class="center-align" >
			<div class="number-lable">
				<span class="span-label">Amount To Factory</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getAmountToFactory()%></span>
			</div>
		</div>
		<%} %>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=viewResult.getClosingBalance()%></span>
			</div>
		</div>
		<%-- <div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label">Reason For Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 180px;">
				<div class="input-field-preview" style="width: 620px;">
							<%=viewResult.getReasonAmountToBank()%>
						</div>
			</div>
		</div> --%>
	</div>
	
	<%
	if(dayBookResultList != null){
	%>
<div style="width:821px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Opening Stock</td><td>Return Quantity</td><td>Products To Customer</td><td>Products To Factory</td>
 		     <td>Closing Stock</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(DayBookViewResult dayBookProducts : dayBookResultList){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(dayBookProducts.getProduct())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(dayBookProducts.getBatchNumber())%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(dayBookProducts.getOpeningStock())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(dayBookProducts.getReturnQty())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToCustomer())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToFactory())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(dayBookProducts.getClosingStock())%></td>
 	    		    </tr>
 					<% }%>
 					</table>
 					</div>	
	<%-- <div class="invoice-main-table" style="width:820px;overflow: hidden;">
		<div class="inner-table" style="width: 820px;border-top:solid 1px gray;">
			<div class="invoice-boxes-colored" style="width: 40px;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCT_NAME)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 140px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_BATCH_NUMBER)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 100px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_STOCK)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 140px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_CUSTOMER)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 125px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_FACTORY)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="border-right:none;width:124px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_STOCK)%></span>
				</div>
			</div>
			<%
							int count=0;
			for(int i=0;i<dayBookResultList.size();i++){
				DayBookViewResult dayBookProducts=dayBookResultList.get(i);
					count++;
		%>
		  <%
		String product=StringUtil.format(dayBookProducts.getProduct());
		String batchNo=StringUtil.format(dayBookProducts.getBatchNumber());
		if(product.length()> 22||batchNo.length()>20){
			int len=product.length();
			%> 
			<input id="length-<%=count%>" type="hidden" value=<%=len%>>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<input id="batch-<%=count%>" type="hidden" value=<%=batchNo.length()%>>
			<script type="text/javascript">
			DayBookViewHandler.checkLength($('#length-<%=count%>').val(),$('#num-<%=count%>').val(),$('#batch-<%=count%>').val());
			</script>
			<%
			}
		%> 
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DayBookViewHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width:820px;">
				<div class="invoice-boxes" Style="width: 40px;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(dayBookProducts.getProduct())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 140px;">
					<div>
						<span class="property"><%=StringUtil.format(dayBookProducts.getBatchNumber())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 100px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getOpeningStock())%></span>
					</div>
				</div>


				<div class="invoice-boxes" Style="width: 140px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToCustomer())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 125px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToFactory())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 100px;border-right:none;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getClosingStock())%></span>
					</div>
				</div>
			</div>
			<%
				}
			%>
		</div>
	</div> --%>
			<%
				}
			%>
			
			<%if(viewResult != null){ %>
			<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Vehicle Number</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.format(viewResult.getVehicleNo()) %></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(viewResult.getStartingReading()) %></span>
			</div>
		</div>
		
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(viewResult.getDriverName())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(viewResult.getEndingReading())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px; line-height:20px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Basic Info Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=StringUtil.nullFormat(viewResult.getBasicInfoRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:160px !important;">Vehicle Details Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=StringUtil.nullFormat(viewResult.getVehicleDetailRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label">Allowances Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=StringUtil.nullFormat(viewResult.getAllowancesRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=StringUtil.nullFormat(viewResult.getAmountRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Product Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=viewResult.getProductsRemarks()%>
						</div>
			</div>
		</div>
		
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	<%} %>
	</div>
	</div>