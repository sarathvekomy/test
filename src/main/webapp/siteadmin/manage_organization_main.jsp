<%
	String pageLink = request.getParameter("pageLink");
	if(pageLink==null || "".equals(pageLink)) {
		pageLink ="organization-list";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

	<script type="text/javascript" src="js/siteadmin/siteadmin_organization.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			OrganizationHandler.pageLinks();
			$('#<%=pageLink%>').click();
			setTimeout(function(){
				$('#school-results-list').jScrollPane({showArrows:true});
			},300);
		});
	</script>



	
	<div id="error-message" title="Error Dialog"></div>
	<div class="organization-page-container"></div>

