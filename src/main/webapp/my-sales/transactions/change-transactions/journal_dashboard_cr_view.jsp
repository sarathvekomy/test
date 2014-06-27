<%@page import="com.vekomy.vbooks.hibernate.model.VbJournal"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest"%>
<%@page import="com.vekomy.vbooks.mysales.cr.dao.JournalCrDao"%>
<%@page import="com.vekomy.vbooks.hibernate.BaseDao"%>
<%@page import="com.vekomy.vbooks.spring.action.BaseAction"%>
<%@page import="com.vekomy.vbooks.mysales.dao.JournalDao"%>
<%@page import="com.vekomy.vbooks.mysales.command.SalesReturnCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNoteProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNotePayments"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDeliveryNote"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="com.vekomy.vbooks.security.PasswordEncryption"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currency=user.getOrganization().getCurrencyFormat();
	VbJournalChangeRequest vbJournalChangeRequest=null;
	VbEmployee vbEmployee=null;
	boolean flag = false;
	boolean preview = false;
	String pageTitle = "";
	String viewType = request.getParameter("viewType");
	 VbJournal journal=null;
	 Integer journalId=0;
	String employee_subjects = "";
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		JournalCrDao journalCrDao = (JournalCrDao) hibernateContext
		.getBean("journalCrDao");
		if (journalCrDao != null) {
	int journalCRId = Integer.parseInt(request.getParameter("id"));
	vbJournalChangeRequest = journalCrDao.getJournalCRById(journalCRId , user.getOrganization(),user.getName());
	journal=journalCrDao.getOriginalJournal(vbJournalChangeRequest.getInvoiceNo() , user.getOrganization());
	if(journal != null){
		journalId=journal.getId(); 
	}else{
		journalId=new Integer(0);
	}
	
	if(vbJournalChangeRequest != null){
		vbEmployee=journalCrDao.getSalesExecutiveFullNameSalesReturn(vbJournalChangeRequest.getCreatedBy(),user.getOrganization());
	}
		}
	} catch (Exception exx) {
	}
%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%
	String str=": ";
%>
<div class="outline" style="margin-left: 10px;">
	<div class="first-row">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice No</span>
			</div>
			<div class="number">
			<%String invoiceNumber=StringUtil.format(vbJournalChangeRequest.getInvoiceNo());%>
			<a id="change-request-journal-Invoice-number" href="#" class="<%=invoiceNumber%>" align="<%=journalId%>" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">
				<span class="property-value" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;"><%=str%><%=invoiceNumber%></span>
		    </a>
			</div>
		</div>
		<div class="right-align">
				<div class="number-lable" style="margin-left:-18px;">
				<span class="span-label">Date</span>
			</div>
			<div class="number" style="margin-left:110px;">
				<span class="property-value"><%=str%><%=DateUtils.format(vbJournalChangeRequest.getCreatedOn())%></span>
			</div>
		</div>
	</div>
	<div class="first-row" style="width:960px;">
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Business Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getBusinessName())%></span>
			</div>
		</div>
		<%
		if(vbJournalChangeRequest.getInvoiceName().contains(",1")){
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value" style="font-size: 12px; color: #6600FF;"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getInvoiceName().replace(",1", ""))%></span>
			</div>
		</div>
		<%
		}else{
		%>
		<div class="left-align">
			<div class="number-lable">
				<span class="span-label">Invoice Name</span>
			</div>
			<div class="number">
				<span class="property-value"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getInvoiceName().replace(",0", ""))%></span>
			</div>
		</div>
		<%
		}
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 120px;">
				<span class="span-label">Journal Type</span>
			</div>
			<div class="number" style="margin-left: 140px;">
				<div class="input-field-preview" style="width: 620px;">
							<span class="property-value"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getJournalType())%></span>
						</div>
			</div>
		</div>
		<%
		if(vbJournalChangeRequest.getDescription().contains(",1")){
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 120px;">
				<span class="span-label">Description</span>
			</div>
			<div class="number" style="margin-left: 140px;">
				<div class="input-field-preview" style="width: 620px;">
							<span class="property-value" style="font-size: 12px; color: #6600FF;"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getDescription().replace(",1", ""))%></span>
						</div>
			</div>
		</div>
		<%
		}else{
		%>
		<div class="left-align">
			<div class="number-lable" style="width: 120px;">
				<span class="span-label">Description</span>
			</div>
			<div class="number" style="margin-left: 140px;">
				<div class="input-field-preview" style="width: 620px;">
							<span class="property-value"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getDescription().replace(",0", ""))%></span>
						</div>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<div class="paymentset-row" style="margin-top: 10px;">
		<div class="paymentset" style="height: 100px;">
			<div class="separator" style="height: 30px; width: 260px;"></div>
			<div class="form-row" style="margin-left: 2px; width: 280px;">
				<div class="payment-span-label" style="width: 105px;">Sales Executive</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbEmployee.getFirstName())%><%="   "%><%=StringUtil.format(vbEmployee.getLastName())%></span>
			</div> 
			<div class="separator" style="height: 10px; width: 60px;"></div>
			<div class="form-row"
				style="width: 300px; margin-left: 240px; margin-top: -40px;">
				<div class="payment-span-label"
					style="margin-left: 40px; width: 80px;">Customer</div>
				<span style="color: #1C8CF5; font: 12px arial;"><%=StringUtil.format(vbJournalChangeRequest.getBusinessName())%></span>
			</div>
		</div>
		<div class="separator" style="height: 100px; width: 400px;"></div>
		<div class="paymentset" style="height: 100px;">
		<%
				if(vbJournalChangeRequest.getAmount().contains(",1")){
					String amount=vbJournalChangeRequest.getAmount().replace(",1", "");
					String formatAmount=StringUtil.currencyFormat(Float.parseFloat(amount));				
					%>
			<div class="form-row">
				<div class="payment-span-label">Amount</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment" style="font-size: 12px; color: #6600FF;"><%=formatAmount%></span>
				</div>
			</div>
			<%}else {
				String amount=vbJournalChangeRequest.getAmount().replace(",0", "");
				String formatAmount=StringUtil.currencyFormat(Float.parseFloat(amount));	
				%>
			<div class="form-row">
				<div class="payment-span-label">Amount</div>
				<div class="input-field"><span class="appostaphie"><%=str%></span>
					<span class="span-payment"><%=formatAmount%></span>
				</div>
			</div>
			<%
		   }
		  %>
			</div>
			<div style="float:right;margin-right:120px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  <%=currency%></i></span></div>
	</div>
	<div class="first-row">
	<div class="left-align">
			<div class="number-lable" style="width: 120px; margin-left: 2px;">
				<span class="span-label">Change Request Description</span>
			</div>
			<div class="number" style="margin-left: 100px;">
				<span class="property-value"><%=str%><%=StringUtil.format(vbJournalChangeRequest.getCrDescription())%></span>
			</div>
		</div>
	</div>
</div>
