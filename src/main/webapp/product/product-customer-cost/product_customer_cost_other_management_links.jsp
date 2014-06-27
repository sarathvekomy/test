<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="product-search-customer-cost";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>
	
	<script type="text/javascript" src="js/product/product-customer-cost-other-users.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			ProductCustomerCostOtherUserHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

<ul>
	<li><a id="product-search-customer-cost">Search Product	Customer Cost</a></li>
</ul>