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
	DayBookBasicInfoCommand dayBookBasicInfoCommand = null;
	DayBookAllowancesCommand dayBookAllowancesCommand=null;
	DayBookAmountCommand dayBookAmountCommand=null;
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
	<div class="step-no-select"style="width:140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"style="width:120px;"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
	<div class="first-row" style="width: 820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Sales Executive</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(user.getVbEmployee().getFirstName())%><%="   "%><%=StringUtil.format(user.getVbEmployee().getLastName())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Date</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=DateUtils.format(new Date())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Opening Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getOpeningBalance())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Total Expenses</span>
			</div>
		<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAllowancesCommand.getTotalAllowances())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;height: auto;">
		<div class="left-align">
			<div class="number-lable" style="width: 220px;">
				<span class="span-label">Reason For miscellaneous Expenses</span>
			</div>
			<div class="number" style="margin-left:230px;margin-top:-16px;">
				<div class="input-field-preview" style="width:590px;">
							<%=StringUtil.format(dayBookAllowancesCommand.getReasonMiscellaneousExpenses())%>
						</div>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Total Payable</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalPayable())%></span>
			</div>
		</div>
		<div class="center-align">
			<div class="number-lable">
				<span class="span-label">Total recieved</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalReceived())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getCustomerTotalCredit())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount To Bank</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getAmountToBank())%></span>
			</div>
		</div>
		<div class="center-align" >
			<div class="number-lable">
				<span class="span-label">Amount To Factory</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getAmountToFactory())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookAmountCommand.getClosingBalance())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label">Reason For Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 180px;">
				<div class="input-field-preview" style="width: 620px;">
							<%=StringUtil.format(dayBookAmountCommand.getReasonAmountToBank())%>
						</div>
			</div>
		</div>
	</div>
	<%
	if(dayBookProducts != null){
	%>
	<div class="fieldset-row" style="margin-top:20px;">
			<div class="fieldset" style="height:auto; width: 699px;">
					<div class="report-header" style="width: 820px; height: 40px;">
						<div class="report-header-column2 centered" style="width: 170px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCT_NAME)%></div>
						<div class="report-header-column2 centered" style="width: 120px;"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_STOCK)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.ACCOUNTS_RETURN_QTY)%></div>
						<div class="report-header-column2 centered" style="width: 125px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_CUSTOMER)%></div>
						<div class="report-header-column2 centered" style="width: 125px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_FACTORY)%></div>
						<div class="report-header-column2 centered" style="width: 110px;"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_STOCK)%></div>
					</div>
					<div id="search-results-list" class="green-results-list"
						style="height:auto; width: 820px; overflow: hidden;border: none;">
							<%
							int count=0;
				for(DayBookProductsCommand vbProducts : dayBookProducts){
					count++;
		%>
		  <%
		String product=StringUtil.format(vbProducts.getProductName());
		if(product.length()> 22){
			int len=product.length();
			%> 
			<input id="length-<%=count%>" type="hidden" value=<%=len%>>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DayBookHandler.checkLength($('#length-<%=count%>').val(),$('#num-<%=count%>').val());
			</script>
			<%
			}
		%> 
		<div class="result-row" style="width: 820px;height:auto;border-right: none;border-bottom: solid 1px #9BCCF2" id="row-<%=count%>">
			<div class="invoice-boxes-grid" Style="width: 182px;">
				<div>
					<span class="property"><%=StringUtil.format(vbProducts.getProductName())%></span>
				</div>
			</div>
			<div class="invoice-boxes-grid" Style="width: 130px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(vbProducts.getOpeningStock())%></span>
				</div>
			</div>
			<div class="invoice-boxes-grid" Style="width: 110px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(vbProducts.getReturnQty())%></span>
				</div>
			</div>
			<div class="invoice-boxes-grid" Style="width: 135px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(vbProducts.getProductsToCustomer())%></span>
				</div>
			</div>
			<div class="invoice-boxes-grid" Style="width: 135px;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(vbProducts.getProductsToFactory())%></span>
				</div>
			</div>
			<div class="invoice-boxes-grid" style="width: 120px;border-right: none;">
				<div>
					<span class="property-right-float"><%=StringUtil.quantityFormat(vbProducts.getClosingStock())%></span>
				</div>
			</div>
		</div>
							<%} %>
						</div>
						
				</div>
				
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
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.format(dayBookVehicleDetailsCommand.getVehicleNo()) %></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookVehicleDetailsCommand.getStartingReading()) %></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(dayBookVehicleDetailsCommand.getDriverName())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(dayBookVehicleDetailsCommand.getEndingReading())%></span>
			</div>
		</div>
		
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Remarks</span>
			</div>
			<div class="number" style="margin-left: 120px;">
				<div class="input-field-preview">
							<%=StringUtil.format(dayBookVehicleDetailsCommand.getRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 110px;margin-bottom: -50px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	</div>
	<%} %>
	</div>
