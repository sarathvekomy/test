<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="day-book-search-form-container" class="ui-container" title="Search stock">
					<div class="ui-content form-panel form-panel-border">												
						<form id="day-book-search-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%></div>
										<div class="input-field">
									<input name="salesExecutive" id="salesExecutive"></div>
									</div>
									<div id="sales-executive-name-suggestions" class="sales-executive-name-suggestions" style="height: 80px;"></div>
								</div>
								<div class="separator" style="height: 40px;"></div>								
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CREATED_DATE_LABEL)%> </div>
									<div class="input-field"><input class="datepicker" id="createdOn" name="createdOn" readonly="readonly"></div>
									</div>
								</div>
								
							</div>						 
							<input name="action" value="search-day-book" type="hidden" id="dayBookAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-day-book" class="ui-btn btn-search">Search</div>
							<div id="action-clear" class="ui-btn btn-clear">Clear</div>
							
						</div>
					</div>
				</div>

				<div id="day-book-view-dialog" style="display: none" title="Day Book">
					<div id="day-book-view-container" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 335px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				
				<script type="text/javascript">
				$(document).ready(function() {
					DayBookViewHandler.initSearchDayBookOnLoad();
					 $(".datepicker").datepicker({
					       maxDate: 0,
					       buttonImageOnly : false,
					       dateFormat : 'dd-mm-yy',
					       changeMonth : true,
					       changeYear : true
					      
					      });
					   
					 
					 });
				 //SalesBookHandler.initSalesExecutiveName();
				 DayBookViewHandler.initSearchDayBook(<%=user.getRoles().contains("ROLE_MANAGEMENT")%>);
				</script>									