
<%
	String pageLink = request.getParameter("pageLink");
	if (pageLink == null || "".equals(pageLink)) {
		pageLink = "add-day-book";
	}
%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>

<script type="text/javascript" src="js/my-sales/day-book/day_book.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			ResultHandler.init();
			DayBookHandler.initPageLinks();
			$('#<%=pageLink%>').click();
	});
</script>


<div id="error-message" title="Error Dialog"></div>

<div class="day-book-page-container"></div>

