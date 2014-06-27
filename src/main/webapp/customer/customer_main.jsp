<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="customer-search";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/customer/customer.js"></script>
	<script type="text/javascript" src="js/customer/customer-validate.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			CustomerHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

	
					<div id="error-message" title="Error Dialog"></div>

					<div class="customer-page-container">
					</div>
