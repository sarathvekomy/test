<%@page import="com.vekomy.vbooks.mysales.dao.DayBookDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.DayBookCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBook"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookAmount"%>
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
	VbDayBookProducts vbDayBookProducts = null;
	VbDayBook vbDayBook=null;
	VbEmployee vbEmployee=null;
	List<VbDayBookProducts> list=new ArrayList<VbDayBookProducts>();
	Float openingBalance=0.0f;
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
		list=dayBookDao.getDayBookOnId(id , user.getOrganization());
		vbDayBook=list.get(0).getVbDayBook();
	}
	if(vbDayBook != null){
		openingBalance=dayBookDao.getOpeningBalance(vbDayBook.getSalesExecutive(), user.getOrganization());
	}
	if(vbDayBook != null){
		vbEmployee=dayBookDao.getSalesExecutiveFullName(vbDayBook.getSalesExecutive(),user.getOrganization());
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
<%Set<VbDayBookAmount> amount=(Set<VbDayBookAmount>)vbDayBook.getVbDayBookAmounts();
List<VbDayBookAmount> amountList=new ArrayList<VbDayBookAmount>(amount);

	VbDayBookAmount amounts=(VbDayBookAmount)amountList.get(0);


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
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=DateUtils.format(vbDayBook.getCreatedOn())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom: none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Opening Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.floatFormat(openingBalance)%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Total Expenses</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getTotalAllowances())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;height: auto;margin-bottom:20px;">
		<div class="left-align">
			<div class="number-lable" style="width: 150px;">
				<span class="span-label">Reason For Expenses</span>
			</div>
			<div class="number" style="margin-left:140px;margin-top:-18px;">
				<div class="input-field-preview">
							<%=StringUtil.format(amounts.getMiscellaneousExpenses())%>
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
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getCustomerTotalPayable())%></span>
			</div>
		</div>
		<div class="center-align">
			<div class="number-lable">
				<span class="span-label">Total recieved</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getCustomerTotalReceived())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getCustomerTotalCredit())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;">
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Amount To Bank</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getAmountToBank())%></span>
			</div>
		</div>
		<div class="center-align">
			<div class="number-lable">
				<span class="span-label">Amount To Factory</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getAmountToFactory())%></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Closing Balance</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(amounts.getClosingBalance())%></span>
			</div>
		</div>
		<div class="left-align">
			<div class="number-lable" style="width: 170px;">
				<span class="span-label">Reason For Amount To Bank</span>
			</div>
			<div class="number" style="margin-left: 180px;">
				<div class="input-field-preview" style="width: 620px;">
							<%=StringUtil.format(amounts.getReasonAmountToBank())%>
						</div>
			</div>
		</div>
	</div>
	
		<%
if(list != null){
	 %>

	<div class="invoice-main-table" style="width:820px;">
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
			for(int i=0;i<list.size();i++){
				VbDayBookProducts dayBookProducts=list.get(i);
							
					count++;
		%>
		  <%
		String product=StringUtil.format(dayBookProducts.getProductName());
		if(product.length()> 22){
			int len=product.length();
			%> 
			<input id="length-<%=count%>" type="hidden" value=<%=len%>>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesBookHandler.expandByLength($('#length-<%=count%>').val(),$('#num-<%=count%>').val());
			</script>
			<%
			}
		%> 
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			SalesBookHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width:820px;">
				<div class="invoice-boxes" Style="width: 80px;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(dayBookProducts.getProductName())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getOpeningStock())%></span>
					</div>
				</div>


				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToCustomer())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getProductsToFactory())%></span>
					</div>
				</div>
				<div class="invoice-boxes" Style="width: 145px;border-right:none;">
					<div>
						<span class="property-right-float"><%=StringUtil.quantityFormat(dayBookProducts.getClosingStock())%></span>
					</div>
				</div>
			</div>
			<%
				}
			%>
			
		</div>
	</div>
			<%
				}
			%>
			<%if(vbDayBook != null){ %>
			<div class="first-row" style="width:820px;border-bottom:none;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Vehicle Number</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.format(vbDayBook.getVehicleNo()) %></span>
			</div>
		</div>
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Starting Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(vbDayBook.getStartingReading()) %></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:820px;border-bottom:none;">
	<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Driver Name</span>
			</div>
			<div class="number">
				<span class="property-value" Style="padding-left: 10px;padding-top: 3px;"><%=str%><%=StringUtil.format(vbDayBook.getDriverName())%></span>
			</div>
		</div>
		
		<div class="right-align">
			<div class="number-lable">
				<span class="span-label">Ending Reading</span>
			</div>
			<div class="number-view"><span class="appostaphie"><%=str%></span>
				<span class="span-payment"><%=StringUtil.currencyFormat(vbDayBook.getEndingReading())%></span>
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
							<%=StringUtil.format(vbDayBook.getRemarks())%>
						</div>
			</div>
		</div>
		<div style="float: right;margin-right: 100px;margin-bottom: -50px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	</div>
	<%} %>
	</div>