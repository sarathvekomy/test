<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="default-types";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/dashboard/system_defaults.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			SystemDefaultsHandelr.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

	
					<div id="error-message" title="Error Dialog"></div>

					<div id="cont" class="dashboard-page-container">
					</div>
