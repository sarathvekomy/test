<%@page import="com.vekomy.vbooks.mysales.cr.dao.DayBookCrDao"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookViewResult"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
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
	DayBookCrDao dayBookCrDao = (DayBookCrDao) hibernateContext
			.getBean("dayBookCrDao");
	if(dayBookCrDao!=null){
		int dayBookCRId=Integer.parseInt(request.getParameter("id"));
		dayBookResultList=dayBookCrDao.getDayBookChangeRequestOnId(dayBookCRId, user.getOrganization(),user.getName());
		viewResult=dayBookResultList.get(0);
	}
	
		} catch (Exception ex) {
	ex.printStackTrace();
		}
%>
<%String str=": "; %>
<div class="outline" style="width: 820px; margin-left: 10px;">
	<div class="first-row" style="width: 820px;">
	     <div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:160px !important;">Day Book No.</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%>
			<%String dayBookNo=StringUtil.format(viewResult.getDayBookNo());%></span>
			<a id="change-request-daybook-number" href="#" class="<%=dayBookNo%>" align="<%=viewResult.getId() %>" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
				<span class="property-value" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;"><%=dayBookNo%></span>
		    </a>
			</div>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:160px !important;">Sales Executive</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getSalesExecutive()%></span>
			</div>
			</div>
		</div>
		<%if(viewResult.getIsReturn() != false){ %>
		<% if(viewResult.getReportingManager().contains(",1")){%>
		 <div class="right-align">
			<div class="number-lable" style="width: 170px;margin-top: -20px;">
				<span class="span-label" style="width:160px !important;">Reporting Manager Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=viewResult.getReportingManager().replace(",1", "")%></span>
			</div>
			</div>
		</div>
		<%}else{ %>
		<div class="right-align">
			<div class="number-lable" style="width: 170px; margin-top: -20px;">
				<span class="span-label" style="width:160px !important;">Reporting Manager Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getReportingManager().replace(",0", "")%></span>
			</div>
			</div>
		</div>
		<%} %>
		<%} %>
		<div class="right-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getCreatedDate()%></span>
			</div>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:160px !important;">Opening Balance</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=viewResult.getOpeningBalance()%></span>
			</div>
			</div>
		</div>
		<%
				if(viewResult.getTotalExpenses().contains(",1")){
					String totalAllowances=viewResult.getTotalExpenses().replace(",1", "");
					String formatTotalAllowances=StringUtil.currencyFormat(Float.parseFloat(totalAllowances));				
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:160px !important;">Total Expenses</span>
			</div>
		<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatTotalAllowances%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String totalAllowances=viewResult.getTotalExpenses().replace(",0", "");
			String formatTotalAllowances=StringUtil.currencyFormat(Float.parseFloat(totalAllowances));	
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:160px !important;">Total Expenses</span>
			</div>
		<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatTotalAllowances%></span>
			</div>
		</div>
		<%} %>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Total Payable</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%>
				<%=StringUtil.currencyFormat(Float.parseFloat(viewResult.getTotalPayable()))%></span>
			</div>
			</div>
		</div>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable" >
				<span class="span-label" style="width:150px !important;">Total recieved</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.currencyFormat(Float.parseFloat(viewResult.getTotalRecieved()))%></span>
			</div>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Balance</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.currencyFormat(Float.parseFloat(viewResult.getBalance()))%></span>
			</div>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<%
				if(viewResult.getAmountToBank().contains(",1")){
					String amountToBank=viewResult.getAmountToBank().replace(",1", "");
					String formatAmountToBank=StringUtil.currencyFormat(Float.parseFloat(amountToBank));				
		%>
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatAmountToBank%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String amountToBank=viewResult.getAmountToBank().replace(",0", "");
			String formatAmountToBank=StringUtil.currencyFormat(Float.parseFloat(amountToBank));
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatAmountToBank%></span>
			</div>
			</div>
		</div>
		<%} %>
		<%if(viewResult.getIsReturn() != false){ %>
		<%
				if(viewResult.getAmountToFactory().contains(",1")){
					String amountToFactory=viewResult.getAmountToFactory().replace(",1", "");
					String formatAmountToFactory=StringUtil.currencyFormat(Float.parseFloat(amountToFactory));				
		%>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Amount To Factory</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatAmountToFactory%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String amountToFactory=viewResult.getAmountToFactory().replace(",0", "");
			String formatAmountToFactory=StringUtil.currencyFormat(Float.parseFloat(amountToFactory));
		%>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Amount To Factory</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatAmountToFactory%></span>
			</div>
			</div>
		</div>
		<%} %>
		<%} %>
		
		<%
				if(viewResult.getClosingBalance().contains(",1")){
					String closingBalance=viewResult.getClosingBalance().replace(",1", "");
					String formatClosingBalance=StringUtil.currencyFormat(Float.parseFloat(closingBalance));				
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Closing Balance</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatClosingBalance%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String closingBalance=viewResult.getClosingBalance().replace(",0", "");
			String formatClosingBalance=StringUtil.currencyFormat(Float.parseFloat(closingBalance));
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Closing Balance</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatClosingBalance%></span>
			</div>
			</div>
		</div>
		<%} %>
		
	</div>
	
	<%
	if(dayBookResultList != null){
	%>
     <div style="width:821px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Opening Stock</td><td>Products To Customer</td><td>Products To Factory</td>
 		     <td>Closing Stock</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(DayBookViewResult dayBookProducts:dayBookResultList){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(dayBookProducts.getProduct().replace(",0", ""))%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(dayBookProducts.getBatchNumber().replace(",0", ""))%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(Integer.parseInt(dayBookProducts.getCrOpeningStock().replace(",0", "")))%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(Integer.parseInt(dayBookProducts.getCrProductsToCustomer().replace(",0", "")))%></td>
 	    		      <%
				if(dayBookProducts.getCrProductsToFactory().contains(",1"))
				{
					String productsToFactory=dayBookProducts.getCrProductsToFactory().replace(",1", "");
					String formatProductsToFactory=StringUtil.quantityFormat(Integer.parseInt(productsToFactory));
				%>
				<td align="right" style="font-size: 12px; color: #6600FF;"><%=formatProductsToFactory%></td>
				<%}else{ 
					String productsToFactory=dayBookProducts.getCrProductsToFactory().replace(",0", "");
					String formatProductsToFactory=StringUtil.quantityFormat(Integer.parseInt(productsToFactory));
		       %>
		       <td align="right"><%=formatProductsToFactory%></td>
				<%} %>
				<%
				if(dayBookProducts.getCrClosingStock().contains(",1"))
				{
					String closingStock=dayBookProducts.getCrClosingStock().replace(",1", "");
					String formatClosingStock=StringUtil.quantityFormat(Integer.parseInt(closingStock));
				%>
				<td align="right" style="font-size: 12px; color: #6600FF;"><%=formatClosingStock%></td>
				<%}else{ 
					String closingStock=dayBookProducts.getCrClosingStock().replace(",0", "");
					String formatClosingStock=StringUtil.quantityFormat(Integer.parseInt(closingStock));
		       %>
		       <td align="right"><%=formatClosingStock%></td>
				<%} %>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
	        <%
				}
			%>
			<%if(viewResult != null){ %>
			<div class="first-row" style="width:820px;border-bottom:none;">
			<%
				if(viewResult.getVehicleNo().contains(",1"))
				{
					String vehicleNo=viewResult.getVehicleNo().replace(",1", "");
					String formatVehicleNo=StringUtil.format(vehicleNo);
				%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Vehicle Number</span>
			</div>
		<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value"  Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatVehicleNo%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String vehicleNo=viewResult.getVehicleNo().replace(",0", "");
			String formatVehicleNo=StringUtil.format(vehicleNo);
		       %>
		       <div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Vehicle Number</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatVehicleNo%></span>
			</div>
			</div>
		</div>
		<%} %>
		<%
				if(viewResult.getDriverName().contains(",1"))
				{
					String driverName=viewResult.getDriverName().replace(",1", "");
					String formatDriverName=StringUtil.format(driverName);
				%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Driver Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=str%><%=formatDriverName%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String driverName=viewResult.getDriverName().replace(",0", "");
			String formatDriverName=StringUtil.format(driverName);
		       %>
		    <div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Driver Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatDriverName%></span>
			</div>
		</div>   
		<%} %>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
	<%
				if(viewResult.getCrStartingReading().contains(",1"))
				{
					String startingReading=viewResult.getCrStartingReading().replace(",1", "");
					String formatStartingReading=StringUtil.currencyFormat(Float.parseFloat(startingReading));
				%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Starting Reading</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatStartingReading%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String startingReading=viewResult.getCrStartingReading().replace(",0", "");
			String formatStartingReading=StringUtil.currencyFormat(Float.parseFloat(startingReading));
		       %>
		       <div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Starting Reading</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatStartingReading%></span>
			</div>
			</div>
		</div>
		<%} %>
		<%
				if(viewResult.getCrEndingReading().contains(",1"))
				{
					String endingReading=viewResult.getCrEndingReading().replace(",1", "");
					String formatEndingReading=StringUtil.currencyFormat(Float.parseFloat(endingReading));
				%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Ending Reading</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%><%=formatEndingReading%></span>
			</div>
			</div>
		</div>
		<%}else{ 
			String endingReading=viewResult.getCrEndingReading().replace(",0", "");
			String formatEndingReading=StringUtil.currencyFormat(Float.parseFloat(endingReading));
		       %>
		       <div class="right-align">
			<div class="number-lable">
				<span class="span-label" style="width:150px !important;">Ending Reading</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatEndingReading%></span>
			</div>
			</div>
		</div>
		<%} %>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px; line-height:20px;">
	<% if(viewResult.getBasicInfoRemarks().contains(",1"))
				{
					String remarks=viewResult.getBasicInfoRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
	%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Basic Info Remarks </span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px; font-size: 12px; color: #6600FF;"><%=str%>
							<%=StringUtil.nullFormat(formatRemarks)%></span>
							</div>
						</div>
			</div>
		</div>
		<%}else{ 
			String remarks=viewResult.getBasicInfoRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		      <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Basic Info Remarks </span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%>
							<%=StringUtil.nullFormat(formatRemarks)%></span>
							</div>
						</div>
			</div>
		</div>
		<%} %>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<%if(viewResult.getVehicleDetailRemarks() != null){ %>
		<% if(viewResult.getVehicleDetailRemarks().contains(",1"))
				{
					String remarks=viewResult.getVehicleDetailRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
	    %>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:160px !important;">Vehicle Details Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%}else{ 
			String remarks=viewResult.getVehicleDetailRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		      <div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:160px !important;">Vehicle Details Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%} %>
		<%}else{ %>
		 <div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:160px !important;">Vehicle Details Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
						</div>
			</div>
		</div>
		<%} %>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<% if(viewResult.getAmountRemarks().contains(",1"))
				{
					String remarks=viewResult.getAmountRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
	%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%}else{ 
			String remarks=viewResult.getAmountRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		      <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%} %>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<% if(viewResult.getAllowancesRemarks().contains(",1"))
				{
					String remarks=viewResult.getAllowancesRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
	%>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label">Allowances Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%}else{ 
			String remarks=viewResult.getAllowancesRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		     <div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label">Allowances Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%} %>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<% if(viewResult.getProductsRemarks().contains(",1"))
				{
					String remarks=viewResult.getProductsRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
	%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Product Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%}else{ 
			String remarks=viewResult.getProductsRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		    <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Product Remarks :</span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							<%=formatRemarks%>
						</div>
			</div>
		</div>
		<%} %>
		
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	<%} %>
	
	<div class="first-row" style="width: 820px;">
	<div class="left-align">
			<div class="number-lable" style="margin-left: 0px;">
				<span class="span-label" style="width:160px !important;">Change Request Description</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(viewResult.getCrDescription())%></div>
			</div>
			</div>
		</div>
		</div>
		</div>
	</div>