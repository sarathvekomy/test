<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "transaction-details";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/my-sales/transacations/transacations.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			TransactionChangeRequestHandler.initPageLinks();
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="transaction-page-container"></div>

