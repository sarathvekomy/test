<script type="text/javascript" src="js/dashboard/transaction-cr.js"></script>
<script type="text/javascript" src="js/dashboard/mysales-transaction-cr-history.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
	<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
		 <div id="delivery-note-change-request-container" class="ui-content dashboard-icons-bg delivery-note-change-request" style="float: left; width:210px; height: 220px; margin-left: 5px;">
							 <div class="dashboard-title" style="width: 210px;"><a id="delivery-note-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Delivery Note</a></div>
								<div id="search-delivery-note-results-list" class="delivery-note-change-request"  style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="sales-return-change-request-container" class="ui-content dashboard-icons-bg sales-return-change-request" style="float: left; margin-left: 3px; width:210px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;"><a id="sales-return-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Sales Return</a></div>
								<div id="search-sales-return-change-request-results-list" class="sales-return-change-request"  style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="day-book-change-request-container" class="ui-content dashboard-icons-bg day-book-change-request" style="float: left; margin-left: 3px; width:195px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;"><a id="day-book-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Day Book</a></div>
								<div id="search-day-book-change-request-results-list" class="day-book-change-request"  style="height: 180px; width:195px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				   <div id="journal-change-request-container" class="ui-content dashboard-icons-bg journal-change-request" style="float: left; width:210px; margin-left: 5px; margin-top: 5px; height: 220px;">
							 <div class="dashboard-title" style="width: 210px;"><a id="journal-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Journal</a></div>
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
<div id="sales-return-CR-approval-decline-dialog" style="display: none" title="Sales Return Change Request">
					<div id="sales-return-CR-approval-decline-container"></div>
</div>
<div id="sales-return-CR-approval-error-dialog" style="display: none">
					<div id="sales-return-CR-approval-error-container"></div>
</div>
<div id="delivery-note-approval-decline-dialog" style="display: none" title="Delivery Note Change Request">
					<div id="delivery-note-approval-decline-container"></div>
</div>
<div id="journal-cr-approval-decline-dialog" style="display: none" title="Journal Change Request">
					<div id="journal-cr-approval-decline-container"></div>
</div>
<div id="daybook-cr-approval-decline-dialog" style="display: none" title="Day Book Change Request">
					<div id="daybook-cr-approval-decline-container"></div>
</div>
<div id="day-book-dialog" style="display: none" title="Day Book View">
					<div id="day-book-container" style="width:200px;height: 200px"></div>
</div>
<!-- dialog-box and container for MySales-transaction-history -->
<!-- Delivery_Note_History_Transaction Counts -->
<div id="delivery-note-history-transaction-dialog" style="display: none" title="Delivery Note Transaction History">
					<div id="delivery-note-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Delivery_Note_History_Transaction Invoices -->
<div id="delivery-note-invoice-history-transaction-dialog" style="display: none" title="Delivery Note Invoice Transaction History">
					<div id="delivery-note-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Delivery_Note_History_Transaction Invoices Views dialog and container -->
<div id="delivery-note-invoices-view-histroy-dialog-1" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-2" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-3" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-4" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-5" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div>

<div id="delivery-note-invoices-view-histroy-dialog-6" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-7" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-8" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-9" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-10" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div>

<div id="delivery-note-invoices-view-histroy-dialog-11" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-12" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-13" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-14" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-15" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div>

<div id="delivery-note-invoices-view-histroy-dialog-16" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-17" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-18" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-19" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-20" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div>

<div id="delivery-note-invoices-view-histroy-dialog-21" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-22" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-23" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-24" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-25" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
</div>

<div id="delivery-note-invoices-view-histroy-dialog-26" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-27" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-28" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-29" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
</div>
<div id="delivery-note-invoices-view-histroy-dialog-30" style="display: none" title="Delivery Note Invoice Transaction History View">
					<div id="delivery-note-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
</div>


<!-- Sales_Return_History_Transaction Counts -->
<div id="sales-return-history-transaction-dialog" style="display: none" title="Sales Return Transaction History">
					<div id="sales-return-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Sales_Return_History_Transaction Invoices -->
<div id="sales-return-invoice-history-transaction-dialog" style="display: none" title="Sales Return Invoice Transaction History">
					<div id="sales-return-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Sales_Return_History_Transaction Invoices Views dialog and container -->
<div id="sales-return-invoices-view-histroy-dialog-1" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-2" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-3" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-4" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-5" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div>

<div id="sales-return-invoices-view-histroy-dialog-6" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-7" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-8" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-9" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-10" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div>

<div id="sales-return-invoices-view-histroy-dialog-11" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-12" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-13" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-14" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-15" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div>

<div id="sales-return-invoices-view-histroy-dialog-16" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-17" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-18" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-19" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-20" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div>

<div id="sales-return-invoices-view-histroy-dialog-21" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-22" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-23" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-24" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-25" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
</div>

<div id="sales-return-invoices-view-histroy-dialog-26" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-27" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-28" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-29" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-invoices-view-histroy-dialog-30" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="sales-return-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
</div>
<!-- Journal_History_Transaction Counts -->
<div id="journal-history-transaction-dialog" style="display: none" title="Journal Transaction CR Types">
					<div id="journal-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Journal_History_Transaction journal types Counts -->
<div id="CR-specific-journal-history-transaction-dialog" style="display: none" title="Journal Transaction History">
					<div id="CR-specific-journal-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Journal_History_Transaction Invoices -->
<div id="journal-invoice-history-transaction-dialog" style="display: none" title="Journal Invoice Transaction History">
					<div id="journal-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Journal_History_Transaction Invoices Views dialog and container -->
 <div id="journal-invoices-view-histroy-dialog-1" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-2" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-3" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-4" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-5" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div> 



<div id="journal-invoices-view-histroy-dialog-6" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-7" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-8" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-9" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-10" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div> 



<div id="journal-invoices-view-histroy-dialog-11" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-12" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-13" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-14" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-15" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div> 


<div id="journal-invoices-view-histroy-dialog-16" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-17" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-18" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-19" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-20" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div> 

<div id="journal-invoices-view-histroy-dialog-21" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-22" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-23" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-24" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-25" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
</div> 

<div id="journal-invoices-view-histroy-dialog-26" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-27" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-28" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-29" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
</div>
<div id="journal-invoices-view-histroy-dialog-30" style="display: none" title="Journal Invoice Transaction History View">
					<div id="journal-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
</div> 

<!-- Day_Book_History_Transaction Counts -->
<div id="day-book-history-transaction-dialog" style="display: none" title="Day Book Transaction History">
					<div id="day-book-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Day_Book_History_Transaction Invoices -->
<div id="day-book-invoice-history-transaction-dialog" style="display: none" title="Day Book Invoice Transaction History">
					<div id="day-book-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Day_Book_History_Transaction Invoices Views dialog and container -->
<div id="day-book-invoices-view-histroy-dialog-1" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-2" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-3" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-4" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-5" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div>

<div id="day-book-invoices-view-histroy-dialog-6" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-7" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-8" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-9" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-10" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div>
 
 
 <div id="day-book-invoices-view-histroy-dialog-11" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-12" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-13" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-14" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-15" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div>

<div id="day-book-invoices-view-histroy-dialog-16" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-17" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-18" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-19" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="day-book-invoices-view-histroy-dialog-20" style="display: none" title="Day Book Invoice Transaction History View">
					<div id="day-book-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div>
<script type="text/javascript">
DashboardDeliveryNoteCRHandler.initDeliveryNoteCROnLoad();
DashboardSalesReturnCRHandler.initSalesReturnCROnLoad();
DashboardDayBookCRHandler.initDayBookCROnLoad(); 
DashboardJournalCRHandler.initJournalsCROnLoad();
	 $(document).ready(function() {
		 deliveryNoteCRHistoryTrackerHandler.deliveryNoteHistoryTrackerTransaction();
		 SalesReturnHistoryTransactionHandler.SalesReturnHistoryTransaction();
		 JournalCRHistoryTransactionHandler.JournalHistoryTransaction();
		 DayBookCRHistoryTrackerHandler.DayBookHistoryTrackerFunction();
	}); 
</script>			
