<script type="text/javascript" src="js/dashboard/dashboard.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
	<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
		 <div id="delivery-note-change-request-container" class="ui-content dashboard-icons-bg delivery-note-change-request" style="float: left; width:210px; height: 220px; margin-left: 5px;">
							 <div class="dashboard-title" style="width: 210px;">Delivery Note</div>
								<div id="search-delivery-note-results-list" class="delivery-note-change-request"  style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="sales-return-change-request-container" class="ui-content dashboard-icons-bg sales-return-change-request" style="float: left; margin-left: 3px; width:210px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;">Sales Return</div>
								<div id="search-sales-return-change-request-results-list" class="sales-return-change-request"  style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="day-book-change-request-container" class="ui-content dashboard-icons-bg day-book-change-request" style="float: left; margin-left: 3px; width:195px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;">Day Book</div>
								<div id="search-day-book-change-request-results-list" class="day-book-change-request"  style="height: 180px; width:195px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				   <div id="journal-change-request-container" class="ui-content dashboard-icons-bg journal-change-request" style="float: left; width:210px; margin-left: 5px; margin-top: 5px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;">Journal CR</div>
								<div id="search-journal-change-request-results-list" class="journal-change-request"  style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div> 
		  </div>
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
<div id="journal-dashboard-cr-view-dialog" style="display: none; overflow-x: hidden;" title="Journal CR Details">
					<div id="journal-dashboard-cr-view-container"></div>
</div>
<div id="delivery-note-dialog" style="display: none" title="Delivery Note View">
					<div id="delivery-note-container" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-dialog" style="display: none" title="Sales Return View">
					<div id="sales-return-container" style="width:200px;height: 200px"></div>
</div>
<div id="journal-dialog" style="display: none" title="Journal View">
					<div id="journal-container" style="width:200px;height: 200px"></div>
</div>
<script type="text/javascript">
	 DashbookHandler.initDeliveryNoteCROnLoad();
	 DashbookHandler.initSalesReturnCROnLoad();
	 DashbookHandler.initDayBookCROnLoad(); 
	 DashbookHandler.initJournalsCROnLoad();
	 $(document).ready(function() {
	}); 
</script>			
