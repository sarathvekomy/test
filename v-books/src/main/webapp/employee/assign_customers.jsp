<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

<%
	User user = (User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
%>
<div id="employee-search-form-container" class="ui-container"
	title="Search Employee">
	<!--<div class="green-title-bar">
		<div class="green-title-bar2">
			<div class="page-icon employee-search-icon"></div>
			<div class="page-title employee-search-title"></div>
		</div>
	</div>-->
<div class="ui-content form-panel form-panel-border">												
						<form name="form2"id="employee-search-form" style="height: 415px;">
						<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
						<div class="fieldset" style="height:60px;">
							<div class="form-row" style="width:350px !important;">
						       <div class="label" style="width:130px !important; margin-right:10px;">Sales Executive Name</div>
						           <div class="input-field">
							          <input  name="userName" id="uName" class="mandatory" readonly="readonly">
						           </div>
						           <span id="employeeNameValid"	style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
								<div id="employeeName_pop" class="helppop" style="display: block;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
								<p>Sales Executive Name Required.</p>
							</div>
						</div>
					          </div>
					       <div id="employee-name-suggestions"class="business-name-suggestions" style="z-index: 999;"></div>
					       </div>
							</div>
							 <div class="label" style="float: left; margin-left: 30px;">Locality:</div>
							 <div class="label" style="float: left; margin-left: 96px;">Business Name:</div>
							 <div class="label" style="float: left; margin-left: 96px;">Selected Business Names:</div>
							 <div id="localities-search-results-container" class="ui-container search-results-container" style="width: 250px;">
							
									<div class="ui-content" >
											<div id="localities-search-results-list" class="green-results-list" style="height: 144px; width: 190px; float: left;margin-left: 30px; overflow-x:hidden; box-shadow:0px 2px 10px #7acfff; border-radius: 10px;border:2px solid #f6f6f6; "></div>
											<div class="green-footer-bar"></div>
									</div>
								</div>
								<div id="business-search-results-container" class="ui-container search-results-container">
									<div class="ui-content" >
											<div id="business-search-results-list" class="green-results-list" style="height: 144px; width: 190px; float: left; margin-left: 30px; overflow-x:hidden; box-shadow:0px 2px 10px #7acfff; border-radius: 10px; cursor:pointer;border:2px solid #f6f6f6;"></div>
											<div class="green-footer-bar"></div>
									</div>
								</div>
								<div id="selected-business-names-container" class="ui-container search-results-container">
									<div class="ui-content" >
											<div id="selected-business-names-list" class="green-results-list" style="height: 144px; width: 190px; float: left; margin-left: 30px; overflow-x:hidden; box-shadow:0px 2px 10px #7acfff; border-radius: 10px;cursor:pointer;border:2px solid #f6f6f6;"></div>
											<div class="green-footer-bar"></div>
									</div>
								</div>
						<input name="action" value="assign-customers" type="hidden" id="employeeAction">
						<div id="search-buttons" class="search-buttons" style="width:240px; margin-left:245px; ">
			<div id="action-assign-customer" class="ui-btn assign-customers-btn"  style="margin-top: 20px;margin-right:0px !important; float:left;">Assign Customers</div>
			<div id="action-assign-multiple-customer" class="ui-btn assign-customers-multiple-btn" style="margin-top: 20px; float:left; margin-left: 20px;">Assign</div>
		</div>
		</form>
		</div>
		
	</div>
<script type="text/javascript">
	assignCustomerHandler.load();
	$('.helppop').hide();
</script>