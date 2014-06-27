<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "my-alerts";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/my-sales/alerts/my-alerts.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			MyAlertsHandler.initPageLinks();
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="my-alerts-page-container"></div>

