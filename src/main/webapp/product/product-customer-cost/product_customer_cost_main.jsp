<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="product-search-customer-cost";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>
	
	<script type="text/javascript" src="js/product/product-customer-cost.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			ProductCustomerCostHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

	
					<div id="error-message" title="Error Dialog"></div>

					<div class="product-page-container">
					</div>