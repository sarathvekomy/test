<%@page import="com.vekomy.vbooks.product.command.ProductCustomerCostViewResult"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductCustomerCostDao"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Formatter"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currencyFormat = user.getOrganization().getCurrencyFormat();
	List<ProductCustomerCostViewResult> productCustomerCostList=null;
	ProductCustomerCostViewResult productCustomerCost=null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) hibernateContext
				.getBean("productCustomerCostDao");
		if (productCustomerCostDao != null) {
			String businessName = request.getParameter("name");
		 productCustomerCostList = productCustomerCostDao.getProductCustomerCost(businessName , user.getOrganization());
		 if(productCustomerCostList != null){
			 productCustomerCost=productCustomerCostList.get(0);
		 }
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
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
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getBusinessName())%></span>
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
					<span class="property-value-view" ><%=StringUtil.format(productCustomerCost.getInvoiceName())%></span>
				</div>
			</div>
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Customer Name</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view"><%=StringUtil.format(productCustomerCost.getCustomerName())%></span>
				</div>
			</div>	
			<div class="display-boxes-view-colored">
				<div>
					<span class="span-label">Gender</span>
				</div>
			</div>
			<div class="display-boxes-view">
				<div>
					<span class="property-value-view" ><%=DropDownUtil.getDropDown(DropDownUtil.GENDER,productCustomerCost.getGender().toString())%></span>
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
					<span class="property-value-view"><%=DateUtils.format(productCustomerCost.getCreatedOn())%></span>
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
					<span class="property-value-view"><%=DateUtils.format(productCustomerCost.getModifiedOn())%></span>
				</div>
			</div>		
	</div>
</div> 
<div style="width:965px; float:left;">
 			<table  border="1" width="100%">
 		      <tr><td>S.No</td><td>Product Category</td><td>Product Name</td><td>Batch No</td><td>Product Cost (<%=currencyFormat %>)</td><td>Selling Price (<%=currencyFormat %>)</td><td>Description</td></tr>
 					<% 
			int count=0;
 		    Boolean alternateProduct=false;
 			for(int i=0;i<productCustomerCostList.size();i++){
 				count++;
 				ProductCustomerCostViewResult productCost=new ProductCustomerCostViewResult();
 				 productCost=productCustomerCostList.get(i);
				if(alternateProduct){
					%><tr style="background:#eaeaea;"><%}
					else{%>
						<tr style="background:#bababa;">
						<%} %>
 					<% alternateProduct= !alternateProduct;%>
 					<td><%=count%></td>
 					 <td style="word-wrap:break-word;"><%=StringUtil.format(productCost.getProductCategory())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(productCost.getProduct())%></td>
 	    		    <td style="word-wrap:break-word;"><%=StringUtil.format(productCost.getBatchNumber())%></td>
 	    		     <td align="right"><%=StringUtil.currencyFormat(productCost.getCostPerQuantity())%></td>
 	    		      <td align="right"><%=StringUtil.currencyFormat(productCost.getCost())%></td>
 	    		      <td style="word-wrap:break-word;"><%=StringUtil.format(productCost.getDescription())%></td>
 	    		    </tr>
 					<% }%>
 						  
 					</table>
 					</div>	