<script type="text/javascript" src="js/dashboard/dashboard.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
		<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
					<div id="change-request-container" class="ui-content dashboard-icons-bg customer-change-request" style="margin-left: 5px; float: left; width: 210px; height: 220px;">
							        <div class="dashboard-title" style="width: 250px;">Customer Change Request</div>
									<div id="customer-change-request-results-list" class="customer-change-request" style="height: 180px; width:210px; overflow-x: hidden;"></div>
											<div class="green-footer-bar"></div>
					</div>
					<div id="sales-returns-container" class="ui-content dashboard-icons-bg sales-return-approval" style="float: left; margin-left: 3px; width:205px; height: 220px;">
							 <div class="dashboard-title" style="width: 250px;">Sales Returns</div>
								<div id="search-sales-returns-results-list" class="sales-return-approval" style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="journal-add-container" class="ui-content dashboard-icons-bg journal-approval" style="float: left; margin-left: 3px; width:200px; height: 220px;">
							 <div class="dashboard-title" style="width: 250px;">Journal</div>
								<div id="search-journal-add-results-list" class="journal-approval" style="height: 180px; width:200px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
		</div>
</div>
<div id="customer-change-request-view-dialog" style="display: none; overflow-y: hidden;" title="Customer Change Request Details">
					<div id="customer-change-request-view-container"></div>
</div>
<div id="sales-return-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Sales Return Details">
					<div id="sales-return-dashboard-view-container"></div>
</div>
<div id="journal-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Add Journal Details">
					<div id="journal-dashboard-view-container"></div>
</div>
<div id="check-transaction-sales-return-view-dialog" style="display: none; overflow-x: hidden;" title="Check Transaction Sales Return">
					<div id="check-transaction-sales-return-view-container"></div>
</div>
<div id="sales-return-change-request-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Sales Return Change Request Details">
					<div id="sales-return-change-request-dashboard-view-container"></div>
</div>
<div id="journal-dashboard-cr-view-dialog" style="display: none; overflow-x: hidden;" title="Journal CR Details">
					<div id="journal-dashboard-cr-view-container"></div>
</div>
<div id="check-transaction-journal-view-dialog" style="display: none; overflow-x: hidden;" title="Check Transaction Journal">
					<div id="check-transaction-journal-view-container"></div>
</div>
<script type="text/javascript">
	 DashbookHandler.initSearchCustomerChangeRequestOnLoad();
	 DashbookHandler.initSalesReturnOnLoad();
	 DashbookHandler.initJournalsOnLoad();
	 $(document).ready(function() {
		 //DashbookHandler.initDashbookPageSelection();
	}); 
</script>									 					
							