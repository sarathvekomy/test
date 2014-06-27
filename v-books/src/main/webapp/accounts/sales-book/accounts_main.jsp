
<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "allot-stock";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/accounts/sales-book/accounts.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			SalesBookHandler.initPageLinks();			
			
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="accounts-page-container"></div>

