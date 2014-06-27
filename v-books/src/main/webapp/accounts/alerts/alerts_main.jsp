
<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "my-alerts";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/accounts/alerts/alerts.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			AlertsHandler.initPageLinks();			
			
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="alerts-page-container"></div>

