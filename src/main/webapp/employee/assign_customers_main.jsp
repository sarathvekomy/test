<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="search-assigned-customers";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/employee/assigncustomers.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			assignCustomerHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

	
					<div id="error-message" title="Error Dialog"></div>

					<div class="employee-page-container">
					</div>
