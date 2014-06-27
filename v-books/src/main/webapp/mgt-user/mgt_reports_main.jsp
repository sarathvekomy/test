<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "product-wise-report";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/mgt-user/reports/reports.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
		    ReportsHandler.initPageLinks();
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="report-page-container"></div>
 