<%@page import="com.vekomy.vbooks.product.dao.ProductCustomerCostDao"%>
<%@page
	import="com.vekomy.vbooks.product.command.ProductCustomerCostCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProductCustomerCost"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Formatter"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
%>
<%
	DecimalFormat decimalFormat = new DecimalFormat("#0.00");
%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currencyFormat = user.getOrganization().getCurrencyFormat();
	VbProductCustomerCost vbProductCustomerCost = null;
	List<VbProductCustomerCost> productCustomerCostList = new ArrayList<VbProductCustomerCost>();
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) hibernateContext
				.getBean("productCustomerCostDao");
		if (productCustomerCostDao != null) {
			String businessName = request.getParameter("businessName");
			productCustomerCostList = productCustomerCostDao
					.getProductCustomerCost(businessName , user.getOrganization());
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
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%
	VbProductCustomerCost productCustomerCost = productCustomerCostList
			.get(0);
%>
<div class="main-table"
		Style="width: 400px;margin-left:45px;">
		<div class="inner-table" Style="width: 400px;">
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Business Name</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getVbCustomer().getBusinessName())%></span>
				</div>
			</div>
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Invoice
						Name</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getVbCustomer().getInvoiceName())%></span>
				</div>
			</div>
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getVbCustomer().getCustomerName())%></span>
				</div>
			</div>	
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,productCustomerCost.getVbCustomer().getGender().toString())%></span>
				</div>
			</div>	
	</div>
</div>
<div class="main-table" Style="width: 400px;">
		<div class="inner-table" Style="width: 400px;">
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Created By</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getCreatedBy())%></span>
				</div>
			</div>		
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Created Date</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=dateFormat.format(productCustomerCost.getVbCustomer().getCreatedOn())%></span>
				</div>
			</div>		
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Modified By</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getModifiedBy())%></span>
				</div>
			</div>		
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Modified Date</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=dateFormat.format(productCustomerCost.getModifiedOn())%></span>
				</div>
			</div>		
	</div>
</div> 
<div class="invoice-main-table" style="overflow:hidden;border-top:solid 1px gray;width: 880px;">
		<div class="inner-table" style="width: 880px;">
			<div class="invoice-boxes-colored" style="width: 40px;border-left:solid 1px gray;">
				<div>
					<span class="span-label">S.No</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Product Category</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 150px;">
				<div>
					<span class="span-label">Product Name</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 122px;">
				<div>
					<span class="span-label">Batch Number</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 120px;">
				<div>
					<span class="span-label">Product Cost (<%=currencyFormat %>)</span>
				</div>
			</div>
			<div class="invoice-boxes-colored">
				<div>
					<span class="span-label">Selling Price (<%=currencyFormat %>)</span>
				</div>
			</div>
			<div class="invoice-boxes-colored" Style="width: 150px;">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<% 
			int count=0;
		for(int i=0;i<productCustomerCostList.size();i++){
			count++;
			VbProductCustomerCost productCost=new VbProductCustomerCost();
			 productCost=productCustomerCostList.get(i);
		
		%>
			<%
				String productName=StringUtil.format(productCost.getVbProduct().getProductName());
			 String category=StringUtil.format(productCost.getVbProduct().getProductCategory());
			 String batch=StringUtil.format(productCost.getVbProduct().getBatchNumber());
			 String description=StringUtil.format(productCost.getVbProduct().getDescription());
				if(productName.length()>20||category.length()>25||batch.length()>15||description.length()>20){
					int len=productName.length();
			%>
			<input id="length-<%=count%>" type="hidden" value=<%=len%>> <input
				id="number-<%=count%>" type="hidden" value=<%=count%>> <input
				id="category-<%=count%>" type="hidden" value=<%=category.length()%>>
				<input id="batch-<%=count%>" type="hidden" value=<%=batch.length()%>>
			<script type="text/javascript">
			ProductCustomerCostHandler.checkLength($('#length-<%=count%>').val(),$('#number-<%=count%>').val(),$('#bonus-<%=count%>').val(),$('#batch-<%=count%>').val());
			</script>
			<%
				}
			%>
			<input id="num-<%=count%>" type="hidden" value=<%=count%>>
			<script type="text/javascript">
			ProductCustomerCostHandler.addColor($('#num-<%=count%>').val());
			</script>
			<div class="result-row" id="row-<%=count%>" style="width:880px;">
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 40px;border-left:solid 1px gray;">
					<div>
						<span class="property"><%=count%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property"><%=StringUtil.format(productCost.getVbProduct().getProductCategory())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">
					<div>
						<span class="property"><%=StringUtil.format(productCost.getVbProduct().getProductName())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 122px;">
					<div>
						<span class="property"><%=StringUtil.format(productCost.getVbProduct().getBatchNumber())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 120px;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(productCost.getVbProduct().getCostPerQuantity())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">
					<div>
						<span class="property-right-float"><%=StringUtil.currencyFormat(productCost.getCost())%></span>
					</div>
				</div>
				<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">
					<div>
						<span class="property"><%=StringUtil.format(productCost.getVbProduct().getDescription())%></span>
					</div>
				</div>
			</div>
			<%
				}
			%>
			
		</div>
	</div>
