<%@page import="com.vekomy.vbooks.mysales.command.DayBookVehicleDetailsCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookAmountCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookAllowancesCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookBasicInfoCommand"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.vekomy.vbooks.mysales.dao.DeliveryNoteDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookCommand"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookProductsCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBook"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
String reportingManager =null;
	DayBookBasicInfoCommand dayBookBasicInfoCommand = null;
	DayBookAllowancesCommand dayBookAllowancesCommand=null;
	DayBookAmountCommand dayBookAmountCommand=null;
	DayBookProductsCommand dayBookProductsCommand = null;
	List<DayBookProductsCommand> dayBookProducts=new ArrayList<DayBookProductsCommand>();
	DayBookVehicleDetailsCommand dayBookVehicleDetailsCommand=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	if (viewType != null && viewType.equals("preview")) {
		preview = true;
		dayBookBasicInfoCommand = (DayBookBasicInfoCommand) session.getAttribute("daybook-basic-info");
		dayBookAllowancesCommand = (DayBookAllowancesCommand) session.getAttribute("daybook-allowances");
		dayBookAmountCommand=(DayBookAmountCommand)session.getAttribute("daybook-amount");
		dayBookVehicleDetailsCommand=(DayBookVehicleDetailsCommand)session.getAttribute("vehicle-details");
		dayBookProducts=(List<DayBookProductsCommand>)session.getAttribute("daybook-product-data");
		if(dayBookProducts.size() > 0){
			dayBookProductsCommand = dayBookProducts.get(0);
		}
	} else {
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	DeliveryNoteDao deliverNoteDao = (DeliveryNoteDao) hibernateContext
			.getBean("dayBookDao");
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
	<div class="step-no-select" style="width:100px;">
		<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
		</div>
	</div>
	<div class="step-no-select"style="width:140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"style="width:120px;"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
	<div class="step-no-select" style="width: 120px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
	<div class="step-no-select"style="width:120px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
	</div>
	<div class="step-no-select"style="width:100px;">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title" style="width: 280px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
	</div>
	<div class="step-selected">
		<div class="step-no-select-corner"></div>
		<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
	</div>
	<div class="step-selected-corner"></div>
</div>
<%
	}		
%>
<%String str=": "; %>
<div class="outline" style="width: 820px; margin-left: 10px;">
<%if(!dayBookBasicInfoCommand.getReportingManagerName().equals("0")){ %>
<div class="first-row" style="width: 820px;" id ="reportingManager">
<div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label" style="width:156px !important;">Reporting Manager Name</span>
			</div>
			<div class="number" style="margin-left: 50px;">
				<div class="number">
				<span class="property-value" Style="padding-left: 3px;padding-top: 3px;"><%=str%><%=dayBookBasicInfoCommand.getReportingManagerName()%></span>
			</div>
			</div>
</div>
		</div>
		<%} %>
		
	<div class="first-row" style="width: 820px;">
		<div class="left-align">
			<div class="number-lable" style="">
				<span class="span-label">Sales Executive</span>
			</div>
			<div class="number" style="margin-left:153px;">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(user.getName())%></span>
			</div>
		</div>
		<div class="align">
			<div class="number-lable">
				<span class="span-label">Start Date</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 25px;padding-top: 3px;"><%=str%><%=DateUtils.format(dayBookBasicInfoCommand.getStartDate())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Created Date</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=DateUtils.format(new Date())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:121px !important;">Basic Info Remarks </span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
						<span class="appostaphie" style=" float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;">	<%=StringUtil.format(dayBookBasicInfoCommand.getDayBookRemarks())%>
						</div>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align" style="width:125px !important;">
			<div class="number-lable">
				<span class="span-label">Opening Balance</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:53px; float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getOpeningBalance())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label"><a href="#" id="totalExpenses" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">Total Expenses</a></span>
			</div>
		<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:11px;"><%=str%></span>
				<span class="span-payment" style="float:none !important;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getTotalAllowances())%></span>
			</div>
		</div>
		<div class="all-expenses" style="display:none; line-height:26px; " title="Expenses">
					<div style="float:left; width:255px;color:#808080;">Driver Allowances</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getDriverAllowances())%></div>
					<div style="float:left; width:255px;color:#808080;">Executive Allowances</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getExecutiveAllowances())%></div>
					<div style="float:left; width:255px;color:#808080;">Vehicle Fuel Expenses</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getVehicleFuelExpenses())%></div>
					<div style="float:left; width:255px;color:#808080;">Vehicle Maintainance Expenses</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getVehicleMaintenanceExpenses())%></div>
					<div style="float:left; width:255px;color:#808080;">Offloading Loading Charges</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getOffloadingLoadingCharges())%></div>
					<div style="float:left; width:255px;color:#808080;">Dealer Party Expenses</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getDealerPartyExpenses())%></div>
					<div style="float:left; width:255px;color:#808080;">Municipality Council</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getMunicipalCityCouncil())%></div>
					<div style="float:left; width:255px;color:#808080;">Miscellaneous Expenses</div>
					<div style="color:#1C8CF5;"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getMiscellaneousExpenses())%></div>
					</div>
	</div>
	<div class="first-row" style="width:820px;height: auto;">
		<div class="left-align" style="width:125px !important;">
			<div class="number-lable">
				<span class="span-label" style="width:122px !important;">Allowance Remarks </span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							: &nbsp;<%=StringUtil.format(dayBookAllowancesCommand.getAllowancesRemarks())%>
						</div>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Total Payable</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:53px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalPayable())%></span>
			</div>
		</div>
		<div class="center-align">
			<div class="number-lable">
				<span class="span-label">Total recieved</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:53px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalReceived())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Balance</span>
			</div>
			<div class="number-view" style="width:275px;"> <span class="appostaphie" style="margin-left:12px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalCredit())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<div class="left-align">
			<div class="number-lable" style="width:170px !important;">
		<span class="span-label" style="width:112px !important;"><a href="#" id="amountToBankLink" onclick="DayBookHandler.getAmountToBankData()" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">Amount Deposited To Bank</a></span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:53px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getAmountToBank())%></span>
			</div>
		</div>
		<div class="center-align" >
			<div class="number-lable">
				<span class="span-label" style="width:110px !important;"id= "amountToFactory">Amount To Factory</span>
			</div>
			<div class="number-view" style="width:275px;" id="amountToFactoryVal"><span class="appostaphie" style="margin-left:53px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getAmountToFactory())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance Of Sales Executive</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:12px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.currencyFormat(dayBookAmountCommand.getClosingBalance())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:110px !important;">Amount Remarks </span>
			</div>
			<div class="number" style="margin-left: 163px;">
				<div class="input-field-preview">
							: &nbsp;<%=StringUtil.format(dayBookAmountCommand.getAmountsRemarks())%>
						</div>
			</div>
		</div>
	</div>
	
	
	
	
	<%
	if(dayBookProducts != null && !dayBookProducts.isEmpty()){
	%>
 			<div style="width:821px; float:left;">
 			<table  border="1" width="100%";>
 		      <tr ><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Opening Stock</td><td>Return Quantity</td><td>Products To Customer</td><td>Products To Factory</td>
 		     <td>Closing Stock</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 		   for(DayBookProductsCommand vbProducts : dayBookProducts){
			count++;
		            if(alternateProduct){
					%><tr style="background:#eaeaea;width:821px;"><%}
					else{%>
						<tr style="background:#bababa;width:821px;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(vbProducts
							.getProductName())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(vbProducts.getBatchNumber())%></td>
 	    		     <td align="right"><%=StringUtil.quantityFormat(vbProducts.getOpeningStock())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(vbProducts.getReturnQty())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(vbProducts.getProductsToCustomer())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(vbProducts.getProductsToFactory())%></td>
 	    		      <td align="right"><%=StringUtil.quantityFormat(vbProducts.getClosingStock())%></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	
	
	<div class="fieldset-row" style="margin-top:20px;">
	<%if(dayBookProductsCommand != null){ %>
			<div class="fieldset" style="height:auto; width: 699px;">
					<div id="search-results-list" class="green-results-list"
						style="height:auto; width: 820px; overflow: hidden;border: none;">
						</div>
						<div class="left-align">
			<div class="number-lable">
				<span class="span-label" style="width:110px !important;">Product Remarks </span>
			</div>
			<div class="number" style="margin-left: 143px;">
				<div class="input-field-preview">
							: &nbsp;<%=StringUtil.nullFormat(dayBookProductsCommand.getProductsRemarks())%>
						</div>
			</div>
		</div>
				</div>
					<%} %>
				
				<div class="fieldset" style="height:auto;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-products" type="hidden" id="dayBookAction">
						</div>
					</div>
				</div>
			</div>
			<%} %>
			<%if(dayBookVehicleDetailsCommand != null){ %>
			<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Vehicle Number</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:33px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:3px !important;"><%=StringUtil.format(dayBookVehicleDetailsCommand.getVehicleNo()) %></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:50px; float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:10px !important;"><%=StringUtil.currencyFormat(dayBookVehicleDetailsCommand.getStartingReading()) %></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number" style="margin-left:133px !important;">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(dayBookVehicleDetailsCommand.getDriverName())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view" style="width:275px;"><span class="appostaphie" style="margin-left:50px;float:left;"><%=str%></span>
				<span class="span-payment" style="float:left !important; margin-left:10px !important;"><%=StringUtil.currencyFormat(dayBookVehicleDetailsCommand.getEndingReading())%></span>
			</div>
		</div>
		
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px; line-height:20px;">
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"></div>
		<div class="left-align">
			<div class="number-lable" style="width:160px !important;">
				<span class="span-label" style="width:150px !important;">Vehicle Details Remarks :</span>
			</div>
			<div class="number" style="margin-left:152px !important; margin-top:-13px !important;">
				<div class="input-field-preview">
							<%=StringUtil.format(dayBookVehicleDetailsCommand.getRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	<%} %>
	</div>
	</div>
	<script>
	$('#totalExpenses').click(function(){
		$('.all-expenses').dialog({
			height: 400,
			width: 400,
			modal:true,
			buttons: {
				Close: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				$(this).dialog('close');
			}
		});
	})
	$(document).ready(function(){
		if($('#isReturn').is(':checked')) {
					$('span#amountToFactory').show();
					$('#amountToFactoryVal').show();
			}else{
					$('span#amountToFactory').hide();
					$('#amountToFactoryVal').hide();
			}	
	})
													
	</script>