<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="journal-search-form-container" class="ui-container"
	title="Search stock">
	<div class="ui-content form-panel form-panel-border">
		<form id="journal-search-form">
			<div class="fieldset-row" style="height: 60px; margin-top: 10px;">
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.JOURNAL_TYPE)%></div>
						<div class="input-field">
								<select name="journalType" id="journalType">
								<option title="select" selected="selected"></option>
								</select>
						</div>
					</div>
					<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.SALES_RETURNS_BUSINESS_NAME_LABEL)%></div>
										<div class="input-field"><input name="businessName" id="businessName" class="clear"></div>
									</div>
				</div>
				<div class="separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 40px;">
					<div class="form-row">
						<div class="label" style="width: 125px;"><%=Msg.get(MsgEnum.ACCOUNTS_DATE_LABEL)%>
						</div>
						<div class="input-field">
							<input class="datepicker" name="createdOn" id="createdOn" class="clear" readonly="readonly">
						</div>
					</div>
				</div>

			</div>
			<input name="action" value="search-journals" type="hidden"
				id="">
		</form>
		<div id="search-buttons" class="search-buttons">
			<div id="action-search-journals" class="ui-btn btn-search">Search</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>

		</div>
	</div>
</div>

<div id="journals-view-dialog" style="display: none" title="Journal">
	<div id="journals-view-container" style="width: 200px; height: 200px"></div>
</div>

<div id="search-results-container2"
	class="ui-container search-results-container">

	<div class="ui-content">
		<div id="search-results-list" class="green-results-list"
			style="height: 335px;"></div>
		<div class="green-footer-bar"></div>
	</div>
</div>

<script type="text/javascript">
$(document).ready(function() {
	SystemDefaultsHandler.getJournalTypes();
	JournalViewHandler.searchJournalOnLoad();
	 $(".datepicker").datepicker({
	       maxDate: 0,
	       buttonImageOnly : false,
	       dateFormat : 'dd-mm-yy',
	       changeMonth : true,
	       changeYear : true
	      
	      });
	   
	 
	 });
JournalViewHandler.searchJournal();
</script>
