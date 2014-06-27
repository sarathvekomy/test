<script type="text/javascript" src="js/dashboard/dashboard.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
		<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
					<div id="change-request-container" class="ui-content dashboard-icons-bg" style="margin-left: 5px; width: 250px; height: 140px;">
							        <div class="dashboard-title" style="width: 250px;">Customer Change Request</div>
									<div id="customer-change-request-results-list" style="height: 120px; width:250px; overflow-x: hidden;"></div>
											<div class="green-footer-bar"></div>
						</div>
					<div id="sales-returns-container" class="ui-content dashboard-icons-bg" style="float: right; width:250px; height: 140px; margin-top: -165px; margin-right: 278px;">
							 <div class="dashboard-title" style="width: 250px;">Sales Returns</div>
								<div id="search-sales-returns-results-list"  style="height: 120px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="journal-add-container" class="ui-content dashboard-icons-bg" style="float: right; width:245px; height: 140px; margin-top: 5px; margin-right: 5px; margin-top: -165px;">
							 <div class="dashboard-title" style="width: 250px;"> Add Journal</div>
								<div id="search-journal-add-results-list"  style="height: 120px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				   <div id="delivery-note-change-request-container" class="ui-content dashboard-icons-bg" style="float: left; width:250px; height: 140px; margin-top: 5px; margin-left: 5px;">
							 <div class="dashboard-title" style="width: 250px;">Delivery Note</div>
								<div id="search-delivery-note-results-list"  style="height: 120px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="sales-return-change-request-container" class="ui-content dashboard-icons-bg" style="float: right; width:250px; height: 140px; margin-top: 5px; margin-right: 278px;">
							 <div class="dashboard-title" style="width: 250px;">Sales Return</div>
								<div id="search-sales-return-change-request-results-list"  style="height: 120px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="day-book-change-request-container" class="ui-content dashboard-icons-bg" style="float: right; width:245px; height: 140px; margin-top: 5px; margin-right: -546px;">
							 <div class="dashboard-title" style="width: 250px;">Day Book</div>
								<div id="search-day-book-change-request-results-list"  style="height: 120px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				   <div id="journal-change-request-container" class="ui-content dashboard-icons-bg" style="float: right; width:250px; height: 130px; margin-top: 175px; margin-left: -546px;">
							 <div class="dashboard-title" style="width: 250px;">Journal CR</div>
								<div id="search-journal-change-request-results-list"  style="height: 110px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div> 
				    <div id="alerts-container" class="ui-content dashboard-icons-bg" style="float: right; width:250px; height: 130px; margin-top: 5px; margin-left: -300px; margin-right: 275px;"">
							 <div class="dashboard-title" style="width: 250px;">Alerts</div>
								<div id="search-alerts-results-list"  style="height: 110px; width:250px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div> 
				    <div id="journal-change-request-container" class="ui-content dashboard-icons-bg" style="float: right; width:245px; height: 130px; margin-top: 5px; margin-left: -100px; margin-right: -245px;"">
							 <div class="dashboard-title" style="width: 250px;"></div>
								<div id="search-journal-change-request-results-list"  style="height: 110px; width:250px; overflow-x: hidden;"></div>
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
<div id="delivery-note-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Delivery Note Change Request Details">
					<div id="delivery-note-dashboard-view-container"></div>
</div>
<div id="sales-return-change-request-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Sales Return Change Request Details">
					<div id="sales-return-change-request-dashboard-view-container"></div>
</div>
<div id="day-book-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Day Book Change Request Details">
					<div id="day-book-dashboard-view-container"></div>
</div>
<div id="journal-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Add Journal Details">
					<div id="journal-dashboard-view-container"></div>
</div>
<div id="journal-dashboard-cr-view-dialog" style="display: none; overflow-x: hidden;" title="Journal CR Details">
					<div id="journal-dashboard-cr-view-container"></div>
</div>
<script type="text/javascript">
	 DashbookHandler.initSearchCustomerChangeRequestOnLoad();
	 DashbookHandler.initSalesReturnOnLoad();
	 DashbookHandler.initDeliveryNoteCROnLoad();
	 DashbookHandler.initSalesReturnCROnLoad();
	 DashbookHandler.initDayBookCROnLoad();
	 DashbookHandler.initJournalsOnLoad();
	 DashbookHandler.initJournalsCROnLoad();
	 $(document).ready(function() {
		 DashbookHandler.initDashbookPageSelection();
	});
</script>									 					
							