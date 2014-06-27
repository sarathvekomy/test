<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="add-user";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/siteadmin/user/user.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			ManageUserHandler.initPageLinks();
			$('#<%=pageLink%>').click();
		});
	</script>



	
	<div id="error-message" title="Error Dialog"></div>
	<div class="manage-user-page-container"></div>

