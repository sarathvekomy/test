<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "view-transaction";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/my-sales/transacations/view-transactions.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			ViewTransactionsHandler.initPageLinks();
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="transaction-page-container"></div>

