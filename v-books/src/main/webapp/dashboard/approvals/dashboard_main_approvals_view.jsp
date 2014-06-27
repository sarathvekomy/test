<script type="text/javascript" src="js/dashboard/approvals.js"></script>
<script type="text/javascript" src="js/dashboard/mysales-approvals-history.js"></script>
<div id="dashboard-form-container" title="Dashboard" style="height: 500px;">
		<div class="ui-content form-panel full-content" style="margin-top:3px; height: 500px;">	
					<div id="change-request-container" class="ui-content dashboard-icons-bg customer-change-request" style="margin-left: 5px; float: left; width: 210px; height: 220px;">
							        <div class="dashboard-title" style="width: 250px;"><a id="customer-change-request-approval-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Customer CR</a></div>
									<div id="customer-change-request-results-list" class="customer-change-request" style="height: 180px; width:210px; overflow-x: hidden;"></div>
											<div class="green-footer-bar"></div>
					</div>
					<div id="sales-returns-container" class="ui-content dashboard-icons-bg sales-return-approval" style="float: left; margin-left: 3px; width:205px; height: 220px;">
							 <div class="dashboard-title" style="width: 250px;"><a id="sales-return-approval-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Sales Return</a></div>
								<div id="search-sales-returns-results-list" class="sales-return-approval" style="height: 180px; width:210px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
				    <div id="journal-add-container" class="ui-content dashboard-icons-bg journal-approval" style="float: left; margin-left: 3px; width:200px; height: 220px;">
							 <div class="dashboard-title" style="width: 250px;"><a id="journal-approval-txn-history" href="#" style="color:#fff ;font-weight:bold; outline:none;text-decoration:none !important;">Journal</a></div>
								<div id="search-journal-add-results-list" class="journal-approval" style="height: 180px; width:200px; overflow-x: hidden;"></div>
								<div class="green-footer-bar"></div>
				   </div>
		</div>
</div>
<div id="customer-change-request-view-dialog" style="display: none; overflow-y: hidden;" title="Customer Change Request Details">
					<div id="customer-change-request-view-container"></div>
</div>
<div id="sales-return-dialog" style="display: none" title="Sales Return View">
					<div id="sales-return-container" style="width:200px;height: 200px"></div>
</div>
<div id="sales-return-dashboard-view-dialog" style="display: none; overflow-x: hidden;" title="Sales Return Details">
					<div id="sales-return-dashboard-view-container"></div>
</div>
<div id="journal-dialog" style="display: none" title="Journal View">
					<div id="journal-container" style="width:200px;height: 200px"></div>
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
<div id="sales-return-approval-decline-dialog" style="display: none" title="Sales Return">
					<div id="sales-return-approval-decline-container"></div>
</div>
<div id="journal-approval-decline-dialog" style="display: none" title="Journal">
					<div id="journal-approval-decline-container"></div>
</div>
<div id="customer-CR-approval-decline-dialog" style="display: none" title="Customer Change Request">
					<div id="customer-CR-approval-decline-container"></div>
</div>
<!-- dialog-box and container for Mysales-transaction-history -->
<!-- Approval_Sales_Return_History_Transaction Counts -->
<div id="approval-sales-return-history-transaction-dialog" style="display: none" title="Sales Return Transaction History">
					<div id="approval-sales-return-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Sales_Return_History_Transaction Invoices -->
<div id="approval-sales-return-invoice-history-transaction-dialog" style="display: none" title="Sales Return Invoice Transaction History">
					<div id="approval-sales-return-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Sales_Return_History_Transaction Invoices Views dialog and container -->
<div id="approval-sales-return-invoices-view-histroy-dialog-1" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-2" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-3" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-4" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-5" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div>

<div id="approval-sales-return-invoices-view-histroy-dialog-6" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-7" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-8" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-9" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-10" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div>

<div id="approval-sales-return-invoices-view-histroy-dialog-11" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-12" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-13" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-14" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-15" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div>

<div id="approval-sales-return-invoices-view-histroy-dialog-16" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-17" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-18" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-19" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-20" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div>

<div id="approval-sales-return-invoices-view-histroy-dialog-21" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-22" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-23" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-24" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-25" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
</div>

<div id="approval-sales-return-invoices-view-histroy-dialog-26" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-27" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-28" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-29" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
</div>
<div id="approval-sales-return-invoices-view-histroy-dialog-30" style="display: none" title="Sales Return Invoice Transaction History View">
					<div id="approval-sales-return-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
</div>


<!-- dialog-box and container for Mysales-transaction-history -->
<!-- Approval_Journal_History_Transaction Counts -->
<div id="approval-journal-history-transaction-dialog" style="display: none" title="Journal Transaction Types">
					<div id="approval-journal-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Journal_History_Transaction journal types Counts -->
<div id="approval-specific-journal-history-transaction-dialog" style="display: none" title="Journal Transaction History">
					<div id="approval-specific-journal-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Jounal_History_Transaction Invoices -->
<div id="approval-journal-invoice-history-transaction-dialog" style="display: none" title="Journal Invoice Transaction History">
					<div id="approval-journal-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Sales_Return_History_Transaction Invoices Views dialog and container -->
<div id="approval-journal-invoices-view-histroy-dialog-1" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-2" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-3" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-4" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-5" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div> 
  <div id="approval-journal-invoices-view-histroy-dialog-6" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-7" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-8" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-9" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-10" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
</div> 

<div id="approval-journal-invoices-view-histroy-dialog-11" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-12" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-13" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-14" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-15" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
</div> 

<div id="approval-journal-invoices-view-histroy-dialog-16" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-17" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-18" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-19" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-20" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
</div> 

<div id="approval-journal-invoices-view-histroy-dialog-21" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-22" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-23" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-24" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-25" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
</div> 

<div id="approval-journal-invoices-view-histroy-dialog-26" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-27" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-28" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-29" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
</div>
<div id="approval-journal-invoices-view-histroy-dialog-30" style="display: none" title="Journal Invoice Transaction History View">
					<div id="approval-journal-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
</div>   
<!-- dialog-box and container for Customer-CR-transaction-history -->
<!-- Approval_Customer_CR_History_CR Counts -->
<input type="hidden" id="approvedJournalCount"/>
<div id="approval-customer-cr-history-transaction-dialog" style="display: none" title="Customer Change Request History">
					<div id="approval-customer-cr-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Customer_CR_History_Transaction BusinessNames -->
<div id="approval-customer-cr-invoice-history-transaction-dialog" style="display: none" title="Customer Change Request Business Names History">
					<div id="approval-customer-cr-invoice-history-transaction-container" style="width:200px;height: 200px"></div>
</div>
<!-- Approval_Customer_CR_History_Transaction Business Names Views dialog and container -->
<div id="approval-customer-cr-invoices-view-histroy-dialog-1" style="display: none" title="Customer Change Request Business Names History View">
					<div id="approval-customer-cr-invoices-view-histroy-container-1" style="width:200px;height: 200px"></div>
</div>
<div id="approval-customer-cr-invoices-view-histroy-dialog-2" style="display: none" title="Customer Change Request Business Names History View">
					<div id="approval-customer-cr-invoices-view-histroy-container-2" style="width:200px;height: 200px"></div>
</div>
<div id="approval-customer-cr-invoices-view-histroy-dialog-3" style="display: none" title="Customer Change Request Business Names History View">
					<div id="approval-customer-cr-invoices-view-histroy-container-3" style="width:200px;height: 200px"></div>
</div>
<div id="approval-customer-cr-invoices-view-histroy-dialog-4" style="display: none" title="Customer Change Request Business Names History View">
					<div id="approval-customer-cr-invoices-view-histroy-container-4" style="width:200px;height: 200px"></div>
</div>
<div id="approval-customer-cr-invoices-view-histroy-dialog-5" style="display: none" title="Customer Change Request Business Names History View">
					<div id="approval-customer-cr-invoices-view-histroy-container-5" style="width:200px;height: 200px"></div>
</div>
<div id="approval-customer-cr-invoices-view-histroy-dialog-6" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-6" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-7" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-7" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-8" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-8" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-9" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-9" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-10" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-10" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="approval-customer-cr-invoices-view-histroy-dialog-11" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-11" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-12" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-12" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-13" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-13" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-14" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-14" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-15" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-15" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="approval-customer-cr-invoices-view-histroy-dialog-16" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-16" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-17" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-17" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-18" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-18" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-19" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-19" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-20" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-20" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="approval-customer-cr-invoices-view-histroy-dialog-21" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-21" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-22" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-22" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-23" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-23" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-24" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-24" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-25" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-25" style="width:200px;height: 200px"></div>
				</div>
				
				<div id="approval-customer-cr-invoices-view-histroy-dialog-26" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-26" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-27" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-27" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-28" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-28" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-29" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-29" style="width:200px;height: 200px"></div>
				</div>
				<div id="approval-customer-cr-invoices-view-histroy-dialog-30" style="display: none" title="Customer CR View">
					<div id="approval-customer-cr-invoices-view-histroy-container-30" style="width:200px;height: 200px"></div>
				</div>
				
				<!-- for displaying Customer CR previous CR's -->
				<div id="customer-cr-previous-view-dialog-1" style="display: none" title="Customer CR View">
					<div id="customer-cr-previous-view-container-1" style="width:200px;height: 200px"></div>
				</div>
				<div id="customer-cr-previous-view-dialog-2" style="display: none" title="Customer CR View">
					<div id="customer-cr-previous-view-container-2" style="width:200px;height: 200px"></div>
				</div>
				<div id="customer-cr-previous-view-dialog-3" style="display: none" title="Customer CR View">
					<div id="customer-cr-previous-view-container-3" style="width:200px;height: 200px"></div>
				</div>
				<div id="customer-cr-previous-view-dialog-4" style="display: none" title="Customer CR View">
					<div id="customer-cr-previous-view-container-4" style="width:200px;height: 200px"></div>
				</div>
				<div id="customer-cr-previous-view-dialog-5" style="display: none" title="Customer CR View">
					<div id="customer-cr-previous-view-container-5" style="width:200px;height: 200px"></div>
				</div>
				<!-- validating new customer CR from Approval while checking businessName in Customer -->
				<div id="new-customer-cr-businessName-duplication-dialog" style="display: none">
					<div id="new-customer-cr-businessName-duplication-container" style="width: 200px;height: 100px;"></div>
				</div>
				
				
<script type="text/javascript">
DashboardCustomerCRApprovalsHandler.initSearchCustomerChangeRequestOnLoad();
DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
DashboardJournalApprovalsHandler.initJournalsOnLoad();
	 $(document).ready(function() {
		 ApprovalSalesReturnHistoryHandler.ApprovalsSalesReturnHistoryTrackerFunction();
		 ApprovalsJournalHistoryTrackerHandler.ApprovalsJournalHistoryTrackerFunction();
		 CustomerChangeRequestHistoryTrackerHandler.CustomerChangeRequestHistoryTracker();
	}); 
</script>									 					
							