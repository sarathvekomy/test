<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
				<div id="delivery-note-search-form-container" class="ui-container" title="Search stock">
					<%-- <div class="green-title-bar">
						<div class="green-title-bar2">
							<div class="page-icon employee-search-icon"></div>
							<div class="page-title employee-search-title"></div>
							<div class="exp-coll expand-icon" id="employee-exp-coll"></div>
							<div class="favorites-bg">
									<div class="favorite-icon" id="favorite-icon" pageLink="employee-search" favTitle="Search Employee"></div>
									<div class="help-icon" id="help-icon" link="<%=OrganizationUtils.HELP_URL%>employee/employee-search.html"></div>
							</div>
						</div>
					</div> --%>

					<div class="ui-content form-panel form-panel-border">												
						<form id="delivery-note-search-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
										<div class="input-field"><input name="businessName" id="businessName" ></div>
									</div>
									
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME_LABEL)%></div>
										 <div class="input-field"><input name="invoiceName" id="invoiceName" ></div>
									</div>	
								</div>
								<div class="separator" style="height: 40px;"></div>								
								<div class="fieldset" style="height: 40px;">
								<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_CREATED_DATE_LABEL)%> </div>
									<div class="input-field"><input class="datepicker" id="createdOn" name="createdOn" readonly="readonly"></div>
									</div>
								</div>
								<input type="hidden" id="createdBy" class="createdBy" name="createdBy" value="<%=user.getName()%>">
							</div>						 
							<input name="action" value="search-delivery-note" type="hidden" id="deliveryNoteAction">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search-delivery-note" class="ui-btn btn-search"></div>
							<div id="action-clear" class="ui-btn btn-clear"></div>
							
						</div>
					</div>
				</div>

				
				<div id="delivery-note-view-dialog" style="display: none" title="Delivery Note">
					<div id="delivery-note-view-container" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="search-results-container2" class="ui-container search-results-container">

					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 315px;"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>

				
				<script type="text/javascript">
				$(document).ready(function() {
					DeliveryNoteViewHandler.initSearchDeliveryNoteOnLoad();
					 $(".datepicker").datepicker({
					       maxDate: 0,
					       buttonImageOnly : false,
					       dateFormat : 'dd-mm-yy',
					       changeMonth : true,
					       changeYear : true
					      
					      });
					   
					 
					 });
				DeliveryNoteViewHandler.initSearchDeliveryNote();
				</script>									
