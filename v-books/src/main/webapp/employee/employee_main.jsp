<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="employee-search";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/employee/employee.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
		    EmployeeHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>

	
					<div id="error-message" title="Error Dialog"></div>

					<div class="employee-page-container">
					</div>

