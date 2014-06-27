<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequest"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookChangeRequestAmount"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.text.DecimalFormat"%>
<%@page
	import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currency=user.getOrganization().getCurrencyFormat();
	DayBookCommand dayBookCommand = null;
	VbDayBookChangeRequestProducts vbDayBookChangeRequestProducts = null;
	VbDayBookChangeRequest vbDayBookChangeRequest=null;
	VbEmployee vbEmployee=null;
	Float openingBalance=0.0f;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
		try {
	ApplicationContext hibernateContext = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession()
					.getServletContext());
	ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext
			.getBean("changeTransactionDao");
	if(changeTransactionDao!=null){
		int dayBookCRId=Integer.parseInt(request.getParameter("id"));
		vbDayBookChangeRequest=changeTransactionDao.getDayBookOnCRId(dayBookCRId, user.getOrganization());
		if(vbDayBookChangeRequest != null){
			openingBalance=changeTransactionDao.getOpeningBalanceDayBookCR(vbDayBookChangeRequest.getSalesExecutive(), user.getOrganization());
		}
		if(vbDayBookChangeRequest != null){
			vbEmployee=changeTransactionDao.getSalesExecutiveFullName(vbDayBookChangeRequest.getSalesExecutive(),user.getOrganization());
		}
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
<%Set<VbDayBookChangeRequestAmount> amount=(Set<VbDayBookChangeRequestAmount>)vbDayBookChangeRequest.getVbDayBookChangeRequestAmounts();
List<VbDayBookChangeRequestAmount> list=new ArrayList<VbDayBookChangeRequestAmount>(amount);

VbDayBookChangeRequestAmount amounts=(VbDayBookChangeRequestAmount)list.get(0);


%>
<%String str=": "; %>
<div class="outline" style="width: 820px; margin-left: 10px;">
	<div class="first-row" style="width: 820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Sales Executive</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(vbEmployee.getFirstName())%><%="   "%><%=StringUtil.format(vbEmployee.getLastName())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Date</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=DateUtils.format(vbDayBookChangeRequest.getCreatedOn())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Opening Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(openingBalance)%></span>
			</div>
		</div>
		<%
				if(amounts.getTotalAllowances().contains(",1")){
					String totalAllowances=amounts.getTotalAllowances().replace(",1", "");
					String formatTotalAllowances=StringUtil.currencyFormat(Float.parseFloat(totalAllowances));				
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Total Expenses</span>
			</div>
		<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatTotalAllowances%></span>
			</div>
		</div>
		<%}else{ 
			String totalAllowances=amounts.getTotalAllowances().replace(",0", "");
			String formatTotalAllowances=StringUtil.currencyFormat(Float.parseFloat(totalAllowances));	
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Total Expenses</span>
			</div>
		<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatTotalAllowances%></span>
			</div>
		</div>
		<%} %>
	</div>
	
	
	<div class="first-row" style="width:820px;height: auto;margin-bottom:20px;">
	<%
				if(amounts.getReasonMiscellaneousExpenses().contains(",1")){
					String miscellaneousExpenses=amounts.getReasonMiscellaneousExpenses().replace(",1", "");
					String formatMiscellaneousExpenses=StringUtil.format(miscellaneousExpenses);				
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 150px;">
				<span class="span-label">Reason For Expenses</span>
			</div>
			<div class="number" style="margin-left:140px;margin-top:-18px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;">
							<%=formatMiscellaneousExpenses%>
						</div>
			</div>
		</div>
		<%}else{ 
			String miscellaneousExpenses=amounts.getReasonMiscellaneousExpenses().replace(",0", "");
			String formatMiscellaneousExpenses=StringUtil.format(miscellaneousExpenses);		
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 150px;">
				<span class="span-label">Reason For Expenses</span>
			</div>
			<div class="number" style="margin-left:140px;margin-top:-18px;">
				<div class="input-field-preview">
							<%=formatMiscellaneousExpenses%>
						</div>
			</div>
		</div>
		<%} %>
	</div>
	<div class="first-row" style="width:820px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Total Payable</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(amounts.getCustomerTotalPayable()))%></span>
			</div>
		</div>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable">
				<span class="span-label">Total recieved</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(amounts.getCustomerTotalReceived()))%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(Float.parseFloat(amounts.getCustomerTotalCredit()))%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<%
				if(amounts.getAmountToBank().contains(",1")){
					String amountToBank=amounts.getAmountToBank().replace(",1", "");
					String formatAmountToBank=StringUtil.currencyFormat(Float.parseFloat(amountToBank));				
		%>
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount To Bank</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatAmountToBank%></span>
			</div>
		</div>
		<%}else{ 
			String amountToBank=amounts.getAmountToBank().replace(",0", "");
			String formatAmountToBank=StringUtil.currencyFormat(Float.parseFloat(amountToBank));
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount To Bank</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatAmountToBank%></span>
			</div>
		</div>
		<%} %>
		<%
				if(amounts.getAmountToFactory().contains(",1")){
					String amountToFactory=amounts.getAmountToFactory().replace(",1", "");
					String formatAmountToFactory=StringUtil.currencyFormat(Float.parseFloat(amountToFactory));				
		%>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable">
				<span class="span-label">Amount To Factory</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatAmountToFactory%></span>
			</div>
		</div>
		<%}else{ 
			String amountToFactory=amounts.getAmountToFactory().replace(",0", "");
			String formatAmountToFactory=StringUtil.currencyFormat(Float.parseFloat(amountToFactory));
		%>
		<div class="center-align" style="margin-left:260px;margin-top:-25px;">
			<div class="number-lable">
				<span class="span-label">Amount To Factory</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatAmountToFactory%></span>
			</div>
		</div>
		<%} %>
		
		<%
				if(amounts.getClosingBalance().contains(",1")){
					String closingBalance=amounts.getClosingBalance().replace(",1", "");
					String formatClosingBalance=StringUtil.currencyFormat(Float.parseFloat(closingBalance));				
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatClosingBalance%></span>
			</div>
		</div>
		<%}else{ 
			String closingBalance=amounts.getClosingBalance().replace(",0", "");
			String formatClosingBalance=StringUtil.currencyFormat(Float.parseFloat(closingBalance));
		%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatClosingBalance%></span>
			</div>
		</div>
		<%} %>
		
		<%
				if(amounts.getReasonAmountToBank().contains(",1")){
					String reasonAmountToBank=amounts.getReasonAmountToBank().replace(",1", "");
					String formatReasonAmountToBank=StringUtil.format(reasonAmountToBank);				
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label">Reason For Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 180px;">
				<div class="input-field-preview" style="width: 620px;font-size: 12px; color: #6600FF;">
							<%=formatReasonAmountToBank%>
						</div>
			</div>
		</div>
		<%}else{ 
			String reasonAmountToBank=amounts.getReasonAmountToBank().replace(",0", "");
			String formatReasonAmountToBank=StringUtil.format(reasonAmountToBank);	
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label">Reason For Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 180px;">
				<div class="input-field-preview" style="width: 620px;">
							<%=formatReasonAmountToBank%>
						</div>
			</div>
		</div>
		<%} %>
	</div>
	
		<%
if(vbDayBookChangeRequest.getVbDayBookChangeRequestProductses() != null){
	 %>
	<%
	Set<VbDayBookChangeRequestProducts> set=vbDayBookChangeRequest.getVbDayBookChangeRequestProductses();
	List<VbDayBookChangeRequestProducts> productList=new ArrayList<VbDayBookChangeRequestProducts>(set);
	if(productList != null){
	%>

	<div class="invoice-main-table" style="width:820px;overflow: hidden;">
		<div class="inner-table" style="width: 820px;border-top:solid 1px gray;">
			<div class="invoice-boxes-colored" style="width: 80px;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCT_NAME)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_STOCK)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_CUSTOMER)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_FACTORY)%></span>
				</div>
			</div>
			<div class="invoice-boxes-colored" style="border-right:none;width:155px;">
				<div>
					<span class="span-label"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_STOCK)%></span>
				</div>
			</div>
			<%
							int count=0;
							for(VbDayBookChangeRequestProducts dayBookProducts:productList)
							{
					count++;
		%>
		  <%
		String product=StringUtil.format(dayBookProducts.getProductName().replace(",0", ""));
		if(product.length()> 22){
			int len=product.length();
			%> 
			<input id="length-<%=count%>" type="hidden" value=<%=len%>>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DashbookHandler.checkLength($('#length-<%=count%>').val(),$('#num-<%=count%>').val());
			</script>
			<%
			}
		%> 
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			DashbookHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width:820px;">
				<div class="invoice-boxes" Style="width: 80px;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(dayBookProducts.getProductName().replace(",0", ""))%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(Integer.parseInt(dayBookProducts.getOpeningStock().replace(",0", "")))%></span>
					</div>
				</div>


				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(Integer.parseInt(dayBookProducts.getProductsToCustomer().replace(",0", "")))%></span>
					</div>
				</div>
				<%
				if(dayBookProducts.getProductsToFactory().contains(",1"))
				{
					String productsToFactory=dayBookProducts.getProductsToFactory().replace(",1", "");
					String formatProductsToFactory=StringUtil.quantityFormat(Integer.parseInt(productsToFactory));
				
				%>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=formatProductsToFactory%></span>
					</div>
				</div>
				<%}else{ 
					String productsToFactory=dayBookProducts.getProductsToFactory().replace(",0", "");
					String formatProductsToFactory=StringUtil.quantityFormat(Integer.parseInt(productsToFactory));
		       %>
		        <div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=formatProductsToFactory%></span>
					</div>
				</div>
				<%} %>
				<%
				if(dayBookProducts.getClosingStock().contains(",1"))
				{
					String closingStock=dayBookProducts.getClosingStock().replace(",1", "");
					String formatClosingStock=StringUtil.quantityFormat(Integer.parseInt(closingStock));
				
				%>
				<div class="invoice-boxes" Style="width: 145px;border-right:none;">
					<div>
						<span class="property-right-float" style="font-size: 12px; color: #6600FF;"><%=formatClosingStock%></span>
					</div>
				</div>
				<%}else{ 
					String closingStock=dayBookProducts.getClosingStock().replace(",0", "");
					String formatClosingStock=StringUtil.quantityFormat(Integer.parseInt(closingStock));
		       %>
		       <div class="invoice-boxes" Style="width: 145px;border-right:none;">
					<div>
						<span class="property-right-float"><%=formatClosingStock%></span>
					</div>
				</div>
				<%} %>
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
				}
			%>
			<%if(vbDayBookChangeRequest != null){ %>
			<div class="first-row" style="width:820px;border-bottom:none;">
			<%
				if(vbDayBookChangeRequest.getVehicleNo().contains(",1"))
				{
					String vehicleNo=vbDayBookChangeRequest.getVehicleNo().replace(",1", "");
					String formatVehicleNo=StringUtil.format(vehicleNo);
				%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Vehicle Number</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatVehicleNo%></span>
			</div>
		</div>
		<%}else{ 
			String vehicleNo=vbDayBookChangeRequest.getVehicleNo().replace(",0", "");
			String formatVehicleNo=StringUtil.format(vehicleNo);
		       %>
		       <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Vehicle Number</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatVehicleNo%></span>
			</div>
		</div>
		<%} %>
		<%
				if(vbDayBookChangeRequest.getDriverName().contains(",1"))
				{
					String driverName=vbDayBookChangeRequest.getDriverName().replace(",1", "");
					String formatDriverName=StringUtil.format(driverName);
				%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;font-size: 12px; color: #6600FF;"><%=str%><%=formatDriverName%></span>
			</div>
		</div>
		<%}else{ 
			String driverName=vbDayBookChangeRequest.getDriverName().replace(",0", "");
			String formatDriverName=StringUtil.format(driverName);
		       %>
		    <div class="right-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=formatDriverName%></span>
			</div>
		</div>   
		<%} %>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
	<%
				if(vbDayBookChangeRequest.getStartingReading().contains(",1"))
				{
					String startingReading=vbDayBookChangeRequest.getStartingReading().replace(",1", "");
					String formatStartingReading=StringUtil.currencyFormat(Float.parseFloat(startingReading));
				%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatStartingReading%></span>
			</div>
		</div>
		<%}else{ 
			String startingReading=vbDayBookChangeRequest.getStartingReading().replace(",0", "");
			String formatStartingReading=StringUtil.currencyFormat(Float.parseFloat(startingReading));
		       %>
		       <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatStartingReading%></span>
			</div>
		</div>
		<%} %>
		<%
				if(vbDayBookChangeRequest.getEndingReading().contains(",1"))
				{
					String endingReading=vbDayBookChangeRequest.getEndingReading().replace(",1", "");
					String formatEndingReading=StringUtil.currencyFormat(Float.parseFloat(endingReading));
				%>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatEndingReading%></span>
			</div>
			<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
		<%}else{ 
			String endingReading=vbDayBookChangeRequest.getEndingReading().replace(",0", "");
			String formatEndingReading=StringUtil.currencyFormat(Float.parseFloat(endingReading));
		       %>
		       <div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=formatEndingReading%></span>
			</div>
			<span style="color:red;">*</span><span style="color: gray;">All the amounts are in  <%=currency%></span>
		</div>
		<%} %>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px;">
	<%
				if(vbDayBookChangeRequest.getRemarks().contains(",1"))
				{
					String remarks=vbDayBookChangeRequest.getRemarks().replace(",1", "");
					String formatRemarks=StringUtil.format(remarks);
				%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Remarks</span>
			</div>
			<div class="number" style="margin-left: 120px;">
				<div class="input-field-preview" style="font-size: 12px; color: #6600FF;"><%=str%><%=formatRemarks%></div>
			</div>
		</div>
		<%}else{ 
			String remarks=vbDayBookChangeRequest.getRemarks().replace(",0", "");
			String formatRemarks=StringUtil.format(remarks);
		       %>
		       <div class="left-align">
			<div class="number-lable">
				<span class="span-label">Remarks</span>
			</div>
			<div class="number" style="margin-left: 120px;">
				<div class="input-field-preview"><%=str%><%=formatRemarks%></div>
			</div>
		</div>
		<%} %>
	</div>
	<%} %>
	<div class="first-row" style="width: 820px;">
	<div class="left-align">
			<div class="number-lable" style="margin-left: -13px;">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class="number" style="margin-left: 120px;">
				<div class="input-field-preview"><%=str%><%=StringUtil.format(vbDayBookChangeRequest.getCrDescription())%></div>
			</div>
		</div>
		</div>
	</div>