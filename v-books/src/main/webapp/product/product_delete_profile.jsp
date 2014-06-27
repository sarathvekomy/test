<%@page import="com.vekomy.vbooks.product.dao.ProductDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProduct"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Formatter"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String currencyFormat = user.getOrganization().getCurrencyFormat();
	VbProduct vbProduct = null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		ProductDao productDao = (ProductDao) hibernateContext.getBean("productDao");
		if (productDao != null) {
			int id = Integer.parseInt(request.getParameter("id"));
			vbProduct = productDao.getProduct(id , user.getOrganization());
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
<div id="product-add-form-container" title="View Product">
	<%
		if (vbProduct != null) {
	%>
	<div class="main-table"
		Style="width: 550px;margin-left:20px;">
		<div class="inner-table" Style="width: 550px;">
		<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Product Category</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getProductCategory())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Product Brand</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getBrand())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Product Model</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getModel())%></span>
				</div>
			</div>
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Product Name</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getProductName())%></span>
				</div>
			</div>	
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Batch Number</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getBatchNumber())%></span>
				</div>
			</div>	
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Cost per Quantity (<%=currencyFormat %>)</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.currencyFormat(vbProduct.getCostPerQuantity())%></span>
				</div>
			</div>	
			<div class="display-boxes-product-view-colored">
				<div>
					<span class="span-label">Description</span>
				</div>
			</div>
			<div class="display-boxes-product-view">
				<div>
					<span class="property-value" Style="padding-left: 10px;"><%=StringUtil.format(vbProduct.getDescription())%></span>
				</div>
			</div>	
	</div>
	<%} %>
</div>
							</div>
							
