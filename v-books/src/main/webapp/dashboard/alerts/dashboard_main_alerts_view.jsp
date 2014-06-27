<script type="text/javascript" src="js/dashboard/dashboard.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
	<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
	  <div id="alerts-container" class="ui-content dashboard-icons-bg system-alerts" style="float: left; width:210px; height: 220px;  margin-left: 5px;">
							 <div class="dashboard-title" style="width: 210px;">Alerts</div>
								<div id="search-alerts-results-list" class="system-alerts"  style="height: 180px; width:220px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div> 
	 </div>
</div>
<script type="text/javascript">
DashbookHandler.initAlertOnLoad();
	 $(document).ready(function() {
		 //DashbookHandler.initDashbookPageSelection();
	}); 
</script>		