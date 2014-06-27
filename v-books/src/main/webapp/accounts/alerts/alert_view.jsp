<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>

				<div id="alert-view-form-container" class="ui-container" title="View Alert">
					<!--<div class="green-title-bar">
						<div class="green-title-bar2">
						</div>
					</div>-->
					<div class="ui-content form-panel form-panel-border">												
						<form id="alert-view-form">
							<div class="fieldset-row" style="height: 60px;margin-top: 10px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.ALERT_CATEGORY)%></div>
										<div class="input-field">
											<select name="alertCategory" id="alertCategory" >
												<option value = -1>Select </option>
											</select>
										</div>
									</div>
								</div>
								<div class="separator" style="height: 60px;"></div>								
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
										<div class="label"><%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%> </div>
										<div class="input-field">
										<select name="alertType" id="alertType" >
											<option value = -1>Select </option>
										</select>
										</div>
									</div>
								</div>
							</div>						 
							<input name="action" value="view-alerts" type="hidden">
						</form>
						<div id="search-buttons" class="search-buttons">
							<div id="action-search" class="ui-btn btn-search">Search</div>
							<div id="action-clear" class="ui-btn btn-clear">Clear</div>
						</div>
					</div>
				</div>
				<div id="search-results-container2" class="ui-container search-results-container" style="overflow: hidden">
					<div class="ui-content">
						<div id="search-results-list" class="green-results-list" style="height: 300px;overflow-x : auto; overflow-y: hidden"></div>
						<div class="green-footer-bar"></div>
					</div>
				</div>
				<div id="alert-delete-dialog" style="display: none; overflow: hidden;" title="Delete Alert Details">
					<div id="alert-delete-container"></div>
				</div>
				<div id="alert-view-dialog" style="display: none; overflow: hidden;" title="View Alert Details">
					<div id="alert-view-container"></div>
				</div>
				<script type="text/javascript">
					$(document).ready(function() {
						AlertsHandler.initViewAlertsOnload();
						PageHandler.hidePageSelection();
						AlertsHandler.getAlertCategory();
						});
				</script>									