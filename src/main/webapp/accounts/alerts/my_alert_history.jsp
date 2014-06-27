<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<div id="search-results-container2" class="ui-container search-results-container">
	<div class="ui-content">
		<div id="search-results-list" class="green-results-list" style="height: 465px; overflow-x: auto;overflow-y: hidden;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		AlertsHistoryHandler.initMyAlertHistoryOnload();
	});
</script>
