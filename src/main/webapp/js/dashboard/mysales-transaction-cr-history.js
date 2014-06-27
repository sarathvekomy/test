var deliveryNoteCRHistoryTrackerHandler={ 
//Delivery Note History Tracker function
deliveryNoteHistoryTrackerTransaction: function(){
$('#delivery-note-txn-history').live('click',function(){
var deliveryNote='<div class="outline" style="margin-left: 10px; width: 398px; min-height: 0px;">';
 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 400px;">'+
	'<div class="inner-table" style="width: 500px;">'+
		'<div class="invoice-boxes-colored" style="width: 80px;">'+
			'<div>'+
				'<span class="span-label">S.No</span>'+
			'</div>'+
		'</div>'+
		'<div class="invoice-boxes-colored"  style="width: 110px;"><div>'+
				'<span class="span-label">Type</span>'+
			'</div>'+
		'</div>'+
		'<div class="invoice-boxes-colored" Style="width: 110px;">'+
			'<div>'+
				'<span class="span-label">Status</span>'+
			'</div>'+
		'</div>'+
		'<div class="invoice-boxes-colored" Style="width: 96px;">'+
			'<div>'+
				'<span class="span-label">Count</span>'+
			'</div>'+
		'</div>';
 //first row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-1">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">1</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Delivery Note</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Approved</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-approved-count" Style="width: 96px; align: center;">'+
				'<div>'+
					'<span class="property-right-float" Style="align: center;"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
			//second row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-2">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">2</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Delivery Note</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Declined</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-decline-count" Style="width: 96px;">'+
				'<div>'+
					'<span class="property-right-float"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
			//Third row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-3">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">3</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Delivery Note</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Pending</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-pending-count" Style="width: 96px;">'+
				'<div>'+
					'<span class="property-right-float"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
			//fourth row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-4">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">4</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Cash Collection</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Approved</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-collection-approved-count" Style="width: 96px;">'+
				'<div>'+
					'<span class="property-right-float"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
			//fifth row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-5">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">5</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Cash Collection</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Declined</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-collection-decline-count" Style="width: 96px;">'+
				'<div>'+
					'<span class="property-right-float"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
			//sixth row
			deliveryNote= deliveryNote +'<div class="result-row" style="width:400px;" id="row-6">'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">6</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Cash Collection</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
				'<div>'+
					'<span class="property">Pending</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" id="delivery-note-collection-pending-count" Style="width: 96px;">'+
				'<div>'+
					'<span class="property-right-float"></span>'+
				'</div>'+
			'</div>'
		+'</div>';
//end rows	    					
	deliveryNote=deliveryNote+ '</div>'+
    '</div>';
    
$('#delivery-note-history-transaction-container').html(deliveryNote);
$("#delivery-note-history-transaction-dialog").dialog('open');
$.post('deliveryNoteCr.json','action=get-delivery-note-history-transaction',function(obj){
//result class will get all approved,decline,pending CR for DeliveryNote and Cash Collection
   var data=obj.result.data;
   var str1="DN";
    					var count=0;
    		        for(var loop=0;loop < data.length;loop=loop+1) {
    					count++;
    					if(data[loop].deliveryNoteTransactionType.indexOf(str1) != -1){
    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
    							if(data[loop].approvedDNCount == undefined){
    							$('.result-row').find('#delivery-note-approved-count').html(0);
		    					}else{
		    						$('.result-row').find('#delivery-note-approved-count').html('<a id="delivery-note-approved" href="#" count='+ data[loop].approvedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
		    					}
    						}
    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
    							if(data[loop].declinedDNCount == undefined){
	    							$('.result-row').find('#delivery-note-decline-count').html(0);
			    					}else{
			    						$('.result-row').find('#delivery-note-decline-count').html('<a id="delivery-note-decline" href="#" count='+ data[loop].declinedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
			    					}
    							
		    					}
	    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
	    						if(data[loop].pendingDNCount == undefined){
	    							$('.result-row').find('#delivery-note-pending-count').html(0);
			    					}else{
			    						$('.result-row').find('#delivery-note-pending-count').html('<a id="delivery-note-pending" href="#" count='+ data[loop].pendingDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
			    					}					
		    					}
    					}else{
    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
    							if(data[loop].approvedCOLLECTIONCount == undefined){
	    							$('.result-row').find('#delivery-note-collection-approved-count').html(0);
			    					}else{
			    						$('.result-row').find('#delivery-note-collection-approved-count').html('<a id="delivery-note-collection-approved" href="#" count='+ data[loop].approvedCOLLECTIONCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].approvedCOLLECTIONCount + '</a>');
			    					}	
		    					} 
    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
    							if(data[loop].declinedCOLLECTIONCount == undefined){
	    							$('.result-row').find('#delivery-note-collection-decline-count').html(0);
			    					}else{
			    						$('.result-row').find('#delivery-note-collection-decline-count').html('<a id="delivery-note-collection-decline" href="#" count='+ data[loop].declinedCOLLECTIONCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedCOLLECTIONCount + '</a>');
			    					}	
		    					}
	    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
	    						if(data[loop].pendingCOLLECTIONCount == undefined){
	    							$('.result-row').find('#delivery-note-collection-pending-count').html(0);
			    					}else{
			    						$('.result-row').find('#delivery-note-collection-pending-count').html('<a id="delivery-note-collection-pending" href="#" count='+ data[loop].pendingCOLLECTIONCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingCOLLECTIONCount + '</a>');
			    					}	
		    					}
    					}
    }
});
});//click
//counts dialog
$("#delivery-note-history-transaction-dialog").dialog({
   autoOpen : false,
   height : 400,
   width : 500,
   modal : true,
   buttons : {
    Close : function() {
     $(this).dialog('close');
    }
   },
   close : function() {
    $('#delivery-note-history-transaction-container').html('');
   }
  });
//delivery_note approved,decline,pending counts click
$('#delivery-note-approved').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var count=$(this).attr('count');
var str="DN";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 777px;">'+
    			'<div class="inner-table" style="width: 782px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 if(data[0].status == "APPROVED"){
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
    					'<div>'+
    						'<span class="span-label">Approved By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Approved Date</span>'+
    					'</div>'+
    				'</div>';
    		 }else{
    			 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored">'+
					'<div>'+
						'<span class="span-label">Declined By</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 130px;">'+
					'<div>'+
						'<span class="span-label">Declined Date</span>'+
					'</div>'+
				'</div>';
    		 }
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+


    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 520,
	   width : 900,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 550,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
//Delivery Note decline
$('#delivery-note-decline').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var count=$(this).attr('count');
var str="DN";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 777px;">'+
    			'<div class="inner-table" style="width: 782px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 if(data[0].status == "APPROVED"){
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
    					'<div>'+
    						'<span class="span-label">Approved By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Approved Date</span>'+
    					'</div>'+
    				'</div>';
    		 }else{
    			 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored">'+
					'<div>'+
						'<span class="span-label">Declined By</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
					'<div>'+
						'<span class="span-label">Declined Date</span>'+
					'</div>'+
				'</div>';
    		 }
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+


    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//DN invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 520,
	   width : 900,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//DN decline invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 550,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
//DN Pending Count
$('#delivery-note-pending').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var str="DN";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 478px; min-height: 0px">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 480px;">'+
    			'<div class="inner-table" style="width: 782px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//DN pending invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 400,
	   width : 550,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//DN pending invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 520,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
//delivery_note_cash_collection approved,decline,pending counts click
$('#delivery-note-collection-approved').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var count=$(this).attr('count');
var str="COLLECTIONS";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 850px;min-height: 0px">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 852px;">'+
    			'<div class="inner-table" style="width: 852px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" style="width: 220px;"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 if(data[0].status == "APPROVED"){
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
    					'<div>'+
    						'<span class="span-label">Approved By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Approved Date</span>'+
    					'</div>'+
    				'</div>';
    		 }else{
    			 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored">'+
					'<div>'+
						'<span class="span-label">Declined By</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
					'<div>'+
						'<span class="span-label">Declined Date</span>'+
					'</div>'+
				'</div>';
    		 }
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 220px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+


    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//DN invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 520,
	   width : 1020,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//DN decline invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 550,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
$('#delivery-note-collection-decline').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var count=$(this).attr('count');
var str="COLLECTIONS";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 850px;min-height: 0px">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 852px;">'+
    			'<div class="inner-table" style="width: 852px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" style="width: 220px;"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 if(data[0].status == "APPROVED"){
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
    					'<div>'+
    						'<span class="span-label">Approved By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Approved Date</span>'+
    					'</div>'+
    				'</div>';
    		 }else{
    			 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored">'+
					'<div>'+
						'<span class="span-label">Declined By</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
					'<div>'+
						'<span class="span-label">Declined Date</span>'+
					'</div>'+
				'</div>';
    		 }
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 220px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+


    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//DN invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 520,
	   width : 1020,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//DN decline invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 550,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
$('#delivery-note-collection-pending').live('click',function(){
var invoiceNumber = $(this).attr('align');
var status=$(this).attr('comp');
var count=$(this).attr('count');
var str="COLLECTIONS";
if(invoiceNumber.indexOf(str) != -1){
$.post('deliveryNoteCr.json','action=get-delivery-note-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
		function(obj){
	//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
	   var data=obj.result.data;
		   var deliveryNote='<div class="outline" style="margin-left: 10px; width: 553px; min-height: 0px;">';
    		 deliveryNote= deliveryNote + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 555px;">'+
    			'<div class="inner-table" style="width: 555px;">'+
    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
    					'<div>'+
    						'<span class="span-label">S.No</span>'+
    					'</div>'+
    				'</div>';
    		 
    		 deliveryNote= deliveryNote +'<div class="invoice-boxes-colored" style="width: 220px;"><div>'+
    						'<span class="span-label">Invoice Number</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
    					'<div>'+
    						'<span class="span-label">Requested By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Requested Date</span>'+
    					'</div>'+
    				'</div>';
    		 var count=0;
    					for(var i=0;i<data.length;i++)
    					{
    					count++;
    					deliveryNote= deliveryNote +'<div class="result-row" id="row-<%=count%>">'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
    						'<div>'+
    							'<span class="property">'+count+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 220px;">'+
    						'<div>'+
    							'<span class="property">'+
    							'<a id="delivery-note-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].invoiceNumber
		    					 +'</a>'
    						     +'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
    						'<div>'+
    							'<span class="property">'+data[i].requestedBy+'</span>'+
    						'</div>'+
    					'</div>'+
    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
    						'<div>'+
    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
    						'</div>'+
    					'</div>'+
    				'</div>';
    }//for
    					deliveryNote=deliveryNote+ '</div>'+
			    		'</div>'+
			    		'</div>';
       $('#delivery-note-invoice-history-transaction-container').html(deliveryNote);
	   $("#delivery-note-invoice-history-transaction-dialog").dialog('open');
});
 }//if
//DN pending invoices dialog
$("#delivery-note-invoice-history-transaction-dialog").dialog({
	   autoOpen : false,
	   height : 400,
	   width : 600,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#delivery-note-invoice-history-transaction-container').html('');
	   }
	  });
//DN pending invoices click
var $anchor = $('.result-row').find('a#delivery-note-invoice-number-histroy');
$anchor.live('click',function(){
	var invoiceNumber=$(this).attr('class');
	var deliveryNoteId=$(this).attr('align');
	var count=$(this).attr('count');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_history_view.jsp','id=' + deliveryNoteId,
	           function(data) {
	            $('#delivery-note-invoices-view-histroy-container-'+count).html(data);
	            var dialogOpts = {
				     		   height : 520,
				     		   width : 1020,
	            		buttons : {
				     		    Close : function() {
				     		     $(this).dialog('close');
				     		    }
				     		 },
	            	};
	            $("#delivery-note-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
	            count=count+1;
	 });
});
});
},
};
var SalesReturnHistoryTransactionHandler={
	//Sales Return History Tracker function
	SalesReturnHistoryTransaction: function(){
		$('#sales-return-txn-history').live('click',function(){
			var salesReturn='<div class="outline" style="margin-left: 10px; width: 288px; min-height: 0px;">';
			salesReturn= salesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 290px;">'+
 			'<div class="inner-table" style="width: 290px;">'+
 				'<div class="invoice-boxes-colored" style="width: 80px;">'+
 					'<div>'+
 						'<span class="span-label">S.No</span>'+
 					'</div>'+
 				'</div>'+
 				'<div class="invoice-boxes-colored" Style="width: 110px;">'+
 					'<div>'+
 						'<span class="span-label">Status</span>'+
 					'</div>'+
 				'</div>'+
 				'<div class="invoice-boxes-colored" Style="width: 96px;">'+
 					'<div>'+
 						'<span class="span-label">Count</span>'+
 					'</div>'+
 				'</div>';
 		 //first row
			salesReturn= salesReturn +'<div class="result-row" style="width:290px;" id="row-1">'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 						'<div>'+
 							'<span class="property">1</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
 						'<div>'+
 							'<span class="property">Approved</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="sales-return-approved-count" Style="width: 96px; align: center;">'+
 						'<div>'+
 							'<span class="property-right-float" Style="align: center;"></span>'+
 						'</div>'+
 					'</div>'
 				+'</div>';
 					//second row
			salesReturn= salesReturn +'<div class="result-row" style="width:290px;" id="row-2">'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 						'<div>'+
 							'<span class="property">2</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
 						'<div>'+
 							'<span class="property">Decline</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="sales-return-decline-count" Style="width: 96px;">'+
 						'<div>'+
 							'<span class="property-right-float"></span>'+
 						'</div>'+
 					'</div>'
 				+'</div>';
 					//Third row
			salesReturn= salesReturn +'<div class="result-row" style="width:290px;" id="row-3">'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 						'<div>'+
 							'<span class="property">3</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
 						'<div>'+
 							'<span class="property">Pending</span>'+
 						'</div>'+
 					'</div>'+
 					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="sales-return-pending-count" Style="width: 96px;">'+
 						'<div>'+
 							'<span class="property-right-float"></span>'+
 						'</div>'+
 					'</div>'
 				+'</div>';
			
			
			salesReturn=salesReturn+ '</div>'+
 	    		    '</div>';
 	    		    
 	$('#sales-return-history-transaction-container').html(salesReturn);
     $("#sales-return-history-transaction-dialog").dialog('open');
 			$.post('salesReturnCr.json','action=get-sales-return-history-transaction',function(obj){
 				//result class will get all approved,decline,pending CR for SalesReturn
 		    	   var data=obj.result.data;
 				    					var count=0;
 				    		        for(var loop=0;loop < data.length;loop=loop+1) {
 				    					count++;
 				    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
 				    							if(data[loop].approvedDNCount == undefined){
 				    							$('.result-row').find('#sales-return-approved-count').html(0);
 						    					}else{
 						    						$('.result-row').find('#sales-return-approved-count').html('<a id="sales-return-approved" href="#" count='+ data[loop].approvedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
 						    					}
 				    						}
 				    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
 				    							if(data[loop].declinedDNCount == undefined){
 					    							$('.result-row').find('#sales-return-decline-count').html(0);
 							    					}else{
 							    						$('.result-row').find('#sales-return-decline-count').html('<a id="sales-return-decline" href="#" count='+ data[loop].declinedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
 							    					}
 				    							
 						    					}
 					    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
 					    						if(data[loop].pendingDNCount == undefined){
 					    							$('.result-row').find('#sales-return-pending-count').html(0);
 							    					}else{
 							    						$('.result-row').find('#sales-return-pending-count').html('<a id="sales-return-pending" href="#" count='+ data[loop].pendingDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
 							    					}					
 						    					}
 			                }
 			});
 		});//click
 			//counts dialog
 			$("#sales-return-history-transaction-dialog").dialog({
 				   autoOpen : false,
 				   height : 300,
 				   width : 350,
 				   modal : true,
 				   buttons : {
 				    Close : function() {
 				     $(this).dialog('close');
 				    }
 				   },
 				   close : function() {
 				    $('#sales-return-history-transaction-container').html('');
 				   }
 				 });
 			//sales_return approved,decline,pending counts click
 			$('#sales-return-approved').live('click',function(){
 				var invoiceNumber = $(this).attr('align');
 				var status=$(this).attr('comp');
 				var count=$(this).attr('count');
 				var str="SR";
 				if(invoiceNumber.indexOf(str) != -1){
 				$.post('salesReturnCr.json','action=get-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
 						function(obj){
 					//result class will display all CR invoices for SalesReturn based on respective status(Approved,Decline,Pending)
 			    	   var data=obj.result.data;
 			    		   var salesReturn='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
 			    		   salesReturn= salesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 777px;">'+
 				    			'<div class="inner-table" style="width: 782px;">'+
 				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">S.No</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 
 			    		   salesReturn= salesReturn +'<div class="invoice-boxes-colored"><div>'+
 				    						'<span class="span-label">Invoice Number</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested By</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested Date</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 if(data[0].status == "APPROVED"){
 				    			 salesReturn= salesReturn +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Approved By</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Approved Date</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 }else{
 				    			 salesReturn= salesReturn +'<div class="invoice-boxes-colored">'+
 			    					'<div>'+
 			    						'<span class="span-label">Declined By</span>'+
 			    					'</div>'+
 			    				'</div>'+
 			    				'<div class="invoice-boxes-colored" Style="width: 130px;">'+
 			    					'<div>'+
 			    						'<span class="span-label">Declined Date</span>'+
 			    					'</div>'+
 			    				'</div>';
 				    		 }
 				    		 var count=0;
 				    					for(var i=0;i<data.length;i++)
 				    					{
 				    					count++;
 				    					salesReturn= salesReturn +'<div class="result-row" id="row-<%=count%>">'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+count+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+
 				    							'<a id="sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
 						    					 data[i].invoiceNumber
 						    					 +'</a>'
 				    						     +'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+data[i].requestedBy+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
 				    						'</div>'+
 				    					'</div>'+


 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    				'</div>';
 				    }//for
 				    					salesReturn=salesReturn+ '</div>'+
 							    		'</div>'+
 							    		'</div>';
 				       $('#sales-return-invoice-history-transaction-container').html(salesReturn);
 			    	   $("#sales-return-invoice-history-transaction-dialog").dialog('open');
 				});
 				 }//if
 				//invoices dialog
 				$("#sales-return-invoice-history-transaction-dialog").dialog({
 					   autoOpen : false,
 					   height : 520,
 					   width : 900,
 					   modal : true,
 					   buttons : {
 					    Close : function() {
 					     $(this).dialog('close');
 					    }
 					   },
 					   close : function() {
 					    $('#sales-return-invoice-history-transaction-container').html('');
 					   }
 					  });
 				//invoices click
 				var $anchor = $('.result-row').find('a#sales-return-invoice-number-histroy');
 			    $anchor.live('click',function(){
 			    	var invoiceNumber=$(this).attr('class');
 			    	var salesReturnId=$(this).attr('align');
 			    	var count=$(this).attr('count');
 			    	$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_history_view.jsp','id=' + salesReturnId,
 					           function(data) {
 					            $('#sales-return-invoices-view-histroy-container-'+count).html(data);
 					            var dialogOpts = {
      						     		   height : 550,
      						     		   width : 1020,
 					            		buttons : {
      						     		    Close : function() {
      						     		     $(this).dialog('close');
      						     		    }
      						     		 },
 					            	};
 					            $("#sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
 					           count=count+1;
 					 });
 				});
 			});
 			//Sales Return decline
 			$('#sales-return-decline').live('click',function(){
 				var invoiceNumber = $(this).attr('align');
 				var status=$(this).attr('comp');
 				var count=$(this).attr('count');
 				var str="SR";
 				if(invoiceNumber.indexOf(str) != -1){
 				$.post('salesReturnCr.json','action=get-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
 						function(obj){
 					//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
 			    	   var data=obj.result.data;
 			    		   var salesReturn='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
 			    		   salesReturn= salesReturn + '<div class="invoice-main-table" style="overflow:hidden;margin: 0px; width: 777px;">'+
 				    			'<div class="inner-table" style="width: 782px;">'+
 				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">S.No</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 
 			    		   salesReturn= salesReturn +'<div class="invoice-boxes-colored"><div>'+
 				    						'<span class="span-label">Invoice Number</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested By</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested Date</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 if(data[0].status == "APPROVED"){
 				    			 salesReturn= salesReturn +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Approved By</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Approved Date</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 }else{
 				    			 salesReturn= salesReturn +'<div class="invoice-boxes-colored">'+
 			    					'<div>'+
 			    						'<span class="span-label">Declined By</span>'+
 			    					'</div>'+
 			    				'</div>'+
 			    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 			    					'<div>'+
 			    						'<span class="span-label">Declined Date</span>'+
 			    					'</div>'+
 			    				'</div>';
 				    		 }
 				    		 var count=0;
 				    					for(var i=0;i<data.length;i++)
 				    					{
 				    					count++;
 				    					salesReturn= salesReturn +'<div class="result-row" id="row-<%=count%>">'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+count+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+
 				    							'<a id="sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
 						    					 data[i].invoiceNumber
 						    					 +'</a>'
 				    						     +'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+data[i].requestedBy+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
 				    						'</div>'+
 				    					'</div>'+


 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    				'</div>';
 				    }//for
 				    					salesReturn=salesReturn+ '</div>'+
 							    		'</div>'+
 							    		'</div>';
 				    					 $('#sales-return-invoice-history-transaction-container').html(salesReturn);
 				    			    	   $("#sales-return-invoice-history-transaction-dialog").dialog('open');
 				});
 				 }//if
 				//SR invoices dialog
 				$("#sales-return-invoice-history-transaction-dialog").dialog({
 					   autoOpen : false,
 					   height : 520,
 					   width : 900,
 					   modal : true,
 					   buttons : {
 					    Close : function() {
 					     $(this).dialog('close');
 					    }
 					   },
 					   close : function() {
 					    $('#sales-return-invoice-history-transaction-container').html('');
 					   }
 					  });
 				//DN decline invoices click
 				var $anchor = $('.result-row').find('a#sales-return-invoice-number-histroy');
 			    $anchor.live('click',function(){
 			    	var invoiceNumber=$(this).attr('class');
 			    	var salesReturnId=$(this).attr('align');
 			    	var count=$(this).attr('count');
 			    	$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_history_view.jsp','id=' + salesReturnId,
 					           function(data) {
 					            $('#sales-return-invoices-view-histroy-container-'+count).html(data);
 					            var dialogOpts = {
      						     		   height : 550,
      						     		   width : 1020,
 					            		buttons : {
      						     		    Close : function() {
      						     		     $(this).dialog('close');
      						     		    }
      						     		 },
 					            	};
 					            $("#sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
 					           count=count+1;
 					 });
 				});
 			});
 			//Sales Return Pending Count
 			$('#sales-return-pending').live('click',function(){
 				var invoiceNumber = $(this).attr('align');
 				var status=$(this).attr('comp');
 				var str="SR";
 				if(invoiceNumber.indexOf(str) != -1){
 				$.post('salesReturnCr.json','action=get-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
 						function(obj){
 					//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
 			    	   var data=obj.result.data;
 			    		   var salesReturn='<div class="outline" style="margin-left: 10px; width: 478px; min-height: 0px;">';
 			    		   salesReturn= salesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 480px;">'+
 				    			'<div class="inner-table" style="width: 782px;">'+
 				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">S.No</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 
 			    		   salesReturn= salesReturn +'<div class="invoice-boxes-colored"><div>'+
 				    						'<span class="span-label">Invoice Number</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested By</span>'+
 				    					'</div>'+
 				    				'</div>'+
 				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
 				    					'<div>'+
 				    						'<span class="span-label">Requested Date</span>'+
 				    					'</div>'+
 				    				'</div>';
 				    		 var count=0;
 				    					for(var i=0;i<data.length;i++)
 				    					{
 				    					count++;
 				    					salesReturn= salesReturn +'<div class="result-row" id="row-<%=count%>">'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+count+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+
 				    							'<a id="sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
 						    					 data[i].invoiceNumber
 						    					 +'</a>'
 				    						     +'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
 				    						'<div>'+
 				    							'<span class="property">'+data[i].requestedBy+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
 				    						'<div>'+
 				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
 				    						'</div>'+
 				    					'</div>'+
 				    				'</div>';
 				    }//for
 				    					salesReturn=salesReturn+ '</div>'+
 							    		'</div>'+
 							    		'</div>';
 				    					$('#sales-return-invoice-history-transaction-container').html(salesReturn);
 				    			    	   $("#sales-return-invoice-history-transaction-dialog").dialog('open');
 				});
 				 }//if
 				//SR pending invoices dialog
 				$("#sales-return-invoice-history-transaction-dialog").dialog({
 					   autoOpen : false,
 					   height : 400,
 					   width : 550,
 					   modal : true,
 					   buttons : {
 					    Close : function() {
 					     $(this).dialog('close');
 					    }
 					   },
 					   close : function() {
 					    $('#sales-return-invoice-history-transaction-container').html('');
 					   }
 					  });
 				//SR pending invoices click
 				var $anchor = $('.result-row').find('a#sales-return-invoice-number-histroy');
 			    $anchor.live('click',function(){
 			    	var invoiceNumber=$(this).attr('class');
 			    	var salesReturnId=$(this).attr('align');
 			    	var count=$(this).attr('count');
 			    	$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_history_view.jsp','id=' + salesReturnId,
 					           function(data) {
 					            $('#sales-return-invoices-view-histroy-container-'+count).html(data);
 					            var dialogOpts = {
      						     		   height : 550,
      						     		   width : 1020,
 					            		buttons : {
      						     		    Close : function() {
      						     		     $(this).dialog('close');
      						     		    }
      						     		 },
 					            	};
 					            $("#sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
 					           count=count+1;
 					 });
 				});
 			});
	},
	};

var JournalCRHistoryTransactionHandler={
//Journal History Tracker function
JournalHistoryTransaction: function(){
//approval journal header click
$('#journal-txn-history').live('click',function(){
$.post('journalCr.json','action=get-configured-journal-types-invoice-pattern',function(obj){
	//result class will get all journal type,with invoice pattern from vb_journal_types
	   var data=obj.result.data;
	   var journalCR='<div class="outline" style="margin-left: 10px; width: 412px;  min-height: 0px;">';
	   journalCR= journalCR + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 412px;">'+
			'<div class="inner-table" style="width: 417px;">'+
				'<div class="invoice-boxes-colored" style="width: 80px;">'+
					'<div>'+
						'<span class="span-label">S.No</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
					'<div>'+
						'<span class="span-label">Journal Type</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
					'<div>'+
						'<span class="span-label">Invoice Pattern</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 80px;">'+
				'<div>'+
					'<span class="span-label">Count</span>'+
				'</div>'+
			'</div>';
		+'</div>';
		journalCR=journalCR+ '</div>'+
	    		    '</div>';
		 var count=0;
			for(var i=0;i<data.length;i++)
			{
			count++;
			var defaultCount=0;
			journalCR= journalCR +'<div class="result-row" Style="width: 414px;" border-right: solid 0px !important; id="row-<%=count%>" >'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property">'+count+'</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				'<div>'+
					'<span class="property">'+data[i].journalTransactionType+'</span>'+
				'</div>'+
			'</div>'+
			'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
				'<div>'+
					'<span class="property">'+data[i].journalInvoicePattern+'</span>'+
				'</div>'+
			'</div>';
			//get sum up of all Approved,Declined,pending for specific journal_type based on invoice pattern of that journal type 
			if(data[i].totalJournalTypeCount == undefined){
				journalCR= journalCR +'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property-right-float" Style="padding-right: 5px;">'+ defaultCount +'</span>'+
				'</div>'+
			'</div>'+
		   '</div>';
			}else{
				journalCR= journalCR +'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				'<div>'+
					'<span class="property-right-float">'+
					'<a id="total-journal-txn-count-histroy" count="'+count+'" href="#" class="'+ data[i].journalInvoicePattern +'" align="'+ data[i].journalTransactionType +'" style="color:#000; font-weight:bold; padding-right:5px; outline:none;text-decoration:none !important;">'+
					 data[i].totalJournalTypeCount
					 +'</a>'
				     +'</span>'+
				'</div>'+
			'</div>'+
		'</div>';
			}
   }//for
			$('#journal-history-transaction-container').html(journalCR);
	        $("#journal-history-transaction-dialog").dialog('open');
});
});//click
	//counts dialog
	$("#journal-history-transaction-dialog").dialog({
		   autoOpen : false,
		   height : 400,
		   width : 500,
		   modal : true,
		   buttons : {
		    Close : function() {
		     $(this).dialog('close');
		    }
		   },
		   close : function() {
		    $('#journal-history-transaction-container').html('');
		   }
		 });
	
	//design for Approved,pending,count status of specific journal counts
    //approved,pending,decline journal design
		//Journal Type with respective invoice number click
		$('#total-journal-txn-count-histroy').live('click',function(){
			var journalTxnType = $(this).attr('align');
			var invoicePattern=$(this).attr('class');
			$.post('journalCr.json','action=get-specific-journal-type-count&journalTxnType='+journalTxnType+'&invoicePattern='+invoicePattern,
					function(obj){
				var journalCR='<div class="outline" style="margin-left: 10px; width: 288px;  min-height: 0px;">';
				journalCR= journalCR + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 290px;">'+
	    			'<div class="inner-table" style="width: 290px;">'+
	    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
	    					'<div>'+
	    						'<span class="span-label">S.No</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 110px;">'+
	    					'<div>'+
	    						'<span class="span-label">Status</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 96px;">'+
	    					'<div>'+
	    						'<span class="span-label">Count</span>'+
	    					'</div>'+
	    				'</div>';
	    		 //first row
				journalCR= journalCR +'<div class="result-row" style="width:290px;" id="row-1">'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">1</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
	    						'<div>'+
	    							'<span class="property">Approved</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-journal-approved-count" Style="width: 96px; align: center;">'+
	    						'<div>'+
	    							'<span class="property-right-float" Style="align: center;"></span>'+
	    						'</div>'+
	    					'</div>'
	    				+'</div>';
	    					//second row
				journalCR= journalCR +'<div class="result-row" style="width:290px;" id="row-2">'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">2</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
	    						'<div>'+
	    							'<span class="property">Decline</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-journal-decline-count" Style="width: 96px;">'+
	    						'<div>'+
	    							'<span class="property-right-float"></span>'+
	    						'</div>'+
	    					'</div>'
	    				+'</div>';
	    					//Third row
				journalCR= journalCR +'<div class="result-row" style="width:290px;" id="row-3">'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">3</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
	    						'<div>'+
	    							'<span class="property">Pending</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-journal-pending-count" Style="width: 96px;">'+
	    						'<div>'+
	    							'<span class="property-right-float"></span>'+
	    						'</div>'+
	    					'</div>'
	    				+'</div>';
				
				journalCR=journalCR+ '</div>'+
	    	    		    '</div>';
				   $('#CR-specific-journal-history-transaction-container').html(journalCR);
    		       $("#CR-specific-journal-history-transaction-dialog").dialog('open');
				//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    	   var count=0;
    		        for(var loop=0;loop < data.length;loop=loop+1) {
    					count++;
    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
    							if(data[loop].approvedDNCount == undefined){
	    							$('.result-row').find('#approval-journal-approved-count').html(0);
			    					}else{
			    						$('.result-row').find('#approval-journal-approved-count').html('<a id="CR-journal-approved" href="#" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
			    					}
    						}
    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
    							if(data[loop].declinedDNCount == undefined){
	    							$('.result-row').find('#approval-journal-decline-count').html(0);
			    					}else{
			    						$('.result-row').find('#approval-journal-decline-count').html('<a id="CR-journal-decline" href="#" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
			    					}
		    					}
    							
	    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
	    						if(data[loop].pendingDNCount == undefined){
	    							$('.result-row').find('#approval-journal-pending-count').html(0);
			    					}else{
			    						$('.result-row').find('#approval-journal-pending-count').html('<a id="CR-journal-pending" href="#" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
			    					}	
		    					}
                       }//for loop
                 });
			});
		//invoices dialog
		$("#CR-specific-journal-history-transaction-dialog").dialog({
			   autoOpen : false,
			   height : 300,
			   width : 350,
			   modal : true,
			   buttons : {
			    Close : function() {
			     $(this).dialog('close');
			    }
			   },
			   close : function() {
			    $('#CR-specific-journal-history-transaction-container').html('');
			   }
		});
		
//journal CR approved,decline,pending counts click for specific Journal type based on invoice pattern
$('#CR-journal-approved').live('click',function(){
	var invoiceNumber = $(this).attr('align');
	var status=$(this).attr('class');
	var count=$(this).attr('count');
	$.post('journalCr.json','action=get-journal-invoices-history-transaction&invoiceNumber='+invoiceNumber+'&status='+status,
			function(obj){
		//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
    	   var data=obj.result.data;
    		   var journal='<div class="outline" style="margin-left: 10px; width: 860px; min-height: 0px;">';
    		   journal= journal + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 861px;">'+
	    			'<div class="inner-table" style="width: 861px;">'+
	    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
	    					'<div>'+
	    						'<span class="span-label">S.No</span>'+
	    					'</div>'+
	    				'</div>';
	    		 
    		   journal= journal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
	    						'<span class="span-label">Invoice Number</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested By</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested Date</span>'+
	    					'</div>'+
	    				'</div>';
	    		 if(data[0].status == "APPROVED"){
	    			 journal= journal +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
	    					'<div>'+
	    						'<span class="span-label">Approved By</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
	    					'<div>'+
	    						'<span class="span-label">Approved Date</span>'+
	    					'</div>'+
	    				'</div>';
	    		 }else{
	    			 journal= journal +'<div class="invoice-boxes-colored">'+
    					'<div>'+
    						'<span class="span-label">Declined By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 130px;">'+
    					'<div>'+
    						'<span class="span-label">Declined Date</span>'+
    					'</div>'+
    				'</div>';
	    		 }
	    		 var count=0;
	    					for(var i=0;i<data.length;i++)
	    					{
	    					count++;
	    					journal= journal +'<div class="result-row" id="row-<%=count%>">'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">'+count+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
	    						'<div>'+
	    							'<span class="property">'+
	    							'<a id="journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
			    					 data[i].invoiceNumber
			    					 +'</a>'
	    						     +'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
	    						'<div>'+
	    							'<span class="property">'+data[i].requestedBy+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
	    						'</div>'+
	    					'</div>'+


	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    				'</div>';
	    }//for
	    					journal=journal+ '</div>'+
				    		'</div>'+
				    		'</div>';
	       $('#journal-invoice-history-transaction-container').html(journal);
    	   $("#journal-invoice-history-transaction-dialog").dialog('open');
	});
	// }//if
	//invoices dialog
	$("#journal-invoice-history-transaction-dialog").dialog({
		   autoOpen : false,
		   height : 520,
		   width : 900,
		   modal : true,
		   buttons : {
		    Close : function() {
		     $(this).dialog('close');
		    }
		   },
		   close : function() {
		    $('#journal-invoice-history-transaction-container').html('');
		   }
		  });
	//invoices click
	var $anchor = $('.result-row').find('a#journal-invoice-number-histroy');
    $anchor.live('click',function(){
    	var invoiceNumber=$(this).attr('class');
    	var journalId=$(this).attr('align');
    	var count=$(this).attr('count');
    	$.post('my-sales/transactions/change-transactions/journal_dashboard_CR_txn_history_view.jsp','id=' + journalId,
		           function(data) {
		            $('#journal-invoices-view-histroy-container-'+count).html(data);
		            var dialogOpts = {
					     		   height : 400,
					     		   width : 1020,
		            		buttons : {
					     		    Close : function() {
					     		     $(this).dialog('close');
					     		    }
					     		 },
		            	};
		            $("#journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
		            count=count+1;
		 });
	});
// });
});
//Journal decline
$('#CR-journal-decline').live('click',function(){
	var invoiceNumber = $(this).attr('align');
	var status=$(this).attr('class');
	var count=$(this).attr('count');
	$.post('journalCr.json','action=get-journal-invoices-history-transaction&invoiceNumber='+invoiceNumber+'&status='+status,
			function(obj){
		//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
    	   var data=obj.result.data;
    		   var journal='<div class="outline" style="margin-left: 10px; width: 860px; min-height: 0px;">';
    		   journal= journal + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 861px;">'+
	    			'<div class="inner-table" style="width: 861px;">'+
	    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
	    					'<div>'+
	    						'<span class="span-label">S.No</span>'+
	    					'</div>'+
	    				'</div>';
	    		 
    		   journal= journal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
	    						'<span class="span-label">Invoice Number</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested By</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested Date</span>'+
	    					'</div>'+
	    				'</div>';
	    		 if(data[0].status == "APPROVED"){
	    			 journal= journal +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
	    					'<div>'+
	    						'<span class="span-label">Approved By</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
	    					'<div>'+
	    						'<span class="span-label">Approved Date</span>'+
	    					'</div>'+
	    				'</div>';
	    		 }else{
	    			 journal= journal +'<div class="invoice-boxes-colored">'+
    					'<div>'+
    						'<span class="span-label">Declined By</span>'+
    					'</div>'+
    				'</div>'+
    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
    					'<div>'+
    						'<span class="span-label">Declined Date</span>'+
    					'</div>'+
    				'</div>';
	    		 }
	    		 var count=0;
	    					for(var i=0;i<data.length;i++)
	    					{
	    					count++;
	    					journal= journal +'<div class="result-row" id="row-<%=count%>">'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">'+count+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
	    						'<div>'+
	    							'<span class="property">'+
	    							'<a id="journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
			    					 data[i].invoiceNumber
			    					 +'</a>'
	    						     +'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
	    						'<div>'+
	    							'<span class="property">'+data[i].requestedBy+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
	    						'</div>'+
	    					'</div>'+


	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    				'</div>';
	    }//for
	    					journal=journal+ '</div>'+
				    		'</div>'+
				    		'</div>';
	    					   $('#journal-invoice-history-transaction-container').html(journal);
	    			    	   $("#journal-invoice-history-transaction-dialog").dialog('open');
	});
	// }//if
	//Journal invoices dialog
	$("#journal-invoice-history-transaction-dialog").dialog({
		   autoOpen : false,
		   height : 520,
		   width : 900,
		   modal : true,
		   buttons : {
		    Close : function() {
		     $(this).dialog('close');
		    }
		   },
		   close : function() {
		    $('#journal-invoice-history-transaction-container').html('');
		   }
		  });
	//Journal decline invoices click
	var $anchor = $('.result-row').find('a#journal-invoice-number-histroy');
    $anchor.live('click',function(){
    	var invoiceNumber=$(this).attr('class');
    	var journalId=$(this).attr('align');
    	var count=$(this).attr('count');
    	$.post('my-sales/transactions/change-transactions/journal_dashboard_CR_txn_history_view.jsp','id=' + journalId,
		           function(data) {
		            $('#journal-invoices-view-histroy-container-'+count).html(data);
		            var dialogOpts = {
					     		   height : 400,
					     		   width : 1020,
		            		buttons : {
					     		    Close : function() {
					     		     $(this).dialog('close');
					     		    }
					     		 },
		            	};
		            $("#journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
		            count=count+1;
		 });
	});
});
//Journal Pending Count
$('#CR-journal-pending').live('click',function(){
	var invoiceNumber = $(this).attr('align');
	var status=$(this).attr('class');
	$.post('journalCr.json','action=get-journal-invoices-history-transaction&invoiceNumber='+invoiceNumber+'&status='+status,
			function(obj){
		//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
    	   var data=obj.result.data;
    		   var journal='<div class="outline" style="margin-left: 10px; width: 563px;min-height: 0px;">';
    		   journal= journal + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 565px;">'+
	    			'<div class="inner-table" style="width: 565px;">'+
	    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
	    					'<div>'+
	    						'<span class="span-label">S.No</span>'+
	    					'</div>'+
	    				'</div>';
	    		 
    		   journal= journal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
	    						'<span class="span-label">Invoice Number</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested By</span>'+
	    					'</div>'+
	    				'</div>'+
	    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
	    					'<div>'+
	    						'<span class="span-label">Requested Date</span>'+
	    					'</div>'+
	    				'</div>';
	    		 var count=0;
	    					for(var i=0;i<data.length;i++)
	    					{
	    					count++;
	    					journal= journal +'<div class="result-row" id="row-<%=count%>">'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
	    						'<div>'+
	    							'<span class="property">'+count+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
	    						'<div>'+
	    							'<span class="property">'+
	    							'<a id="journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
			    					 data[i].invoiceNumber
			    					 +'</a>'
	    						     +'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
	    						'<div>'+
	    							'<span class="property">'+data[i].requestedBy+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
	    						'<div>'+
	    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
	    						'</div>'+
	    					'</div>'+
	    				'</div>';
	    }//for
	    					journal=journal+ '</div>'+
				    		'</div>'+
				    		'</div>';
	    					$('#journal-invoice-history-transaction-container').html(journal);
	    			    	   $("#journal-invoice-history-transaction-dialog").dialog('open');
	});
	// }//if
	//Journal invoices dialog
	$("#journal-invoice-history-transaction-dialog").dialog({
		   autoOpen : false,
		   height : 400,
		   width : 600,
		   modal : true,
		   buttons : {
		    Close : function() {
		     $(this).dialog('close');
		    }
		   },
		   close : function() {
		    $('#journal-invoice-history-transaction-container').html('');
		   }
		  });
	//Journal pending invoices click
	var $anchor = $('.result-row').find('a#journal-invoice-number-histroy');
    $anchor.live('click',function(){
    	var invoiceNumber=$(this).attr('class');
    	var journalId=$(this).attr('align');
    	var count=$(this).attr('count');
    	$.post('my-sales/transactions/change-transactions/journal_dashboard_CR_txn_history_view.jsp','id=' + journalId,
		           function(data) {
		            $('#journal-invoices-view-histroy-container-'+count).html(data);
		            var dialogOpts = {
					     		   height : 400,
					     		   width : 1020,
		            		buttons : {
					     		    Close : function() {
					     		     $(this).dialog('close');
					     		    }
					     		 },
		            	};
		            $("#journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
		            count=count+1;
		 });
	});
});
},//End of Journal History Tracker
};

var DayBookCRHistoryTrackerHandler={
//Day Book History Tracker
DayBookHistoryTrackerFunction: function(){
	$('#day-book-txn-history').live('click',function(){
		var dayBook='<div class="outline" style="margin-left: 10px; width: 288px; min-height: 0px;">';
		dayBook= dayBook + '<div class="invoice-main-table" style="overflow:hidden;margin: 0px; width: 290px;">'+
			'<div class="inner-table" style="width: 290px;">'+
				'<div class="invoice-boxes-colored" style="width: 80px;">'+
					'<div>'+
						'<span class="span-label">S.No</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 110px;">'+
					'<div>'+
						'<span class="span-label">Status</span>'+
					'</div>'+
				'</div>'+
				'<div class="invoice-boxes-colored" Style="width: 96px;">'+
					'<div>'+
						'<span class="span-label">Count</span>'+
					'</div>'+
				'</div>';
		 //first row
		dayBook= dayBook +'<div class="result-row" style="width:290px;" id="row-1">'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
						'<div>'+
							'<span class="property">1</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
						'<div>'+
							'<span class="property">Approved</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="day-book-approved-count" Style="width: 96px; align: center;">'+
						'<div>'+
							'<span class="property-right-float" Style="align: center;"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
					//second row
		dayBook= dayBook +'<div class="result-row" style="width:290px;" id="row-2">'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
						'<div>'+
							'<span class="property">2</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
						'<div>'+
							'<span class="property">Decline</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="day-book-decline-count" Style="width: 96px;">'+
						'<div>'+
							'<span class="property-right-float"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
					//Third row
		dayBook= dayBook +'<div class="result-row" style="width:290px;" id="row-3">'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
						'<div>'+
							'<span class="property">3</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
						'<div>'+
							'<span class="property">Pending</span>'+
						'</div>'+
					'</div>'+
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="day-book-pending-count" Style="width: 96px;">'+
						'<div>'+
							'<span class="property-right-float"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
		
		dayBook=dayBook+ '</div>'+
	    		    '</div>';
	    		    
	$('#day-book-history-transaction-container').html(dayBook);
    $("#day-book-history-transaction-dialog").dialog('open');
			$.post('dayBookCr.json','action=get-day-book-history-transaction',function(obj){
				//result class will get all approved,decline,pending CR for Day Book
		    	   var data=obj.result.data;
				    					var count=0;
				    		        for(var loop=0;loop < data.length;loop=loop+1) {
				    					count++;
				    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
				    							if(data[loop].approvedDNCount == undefined){
				    							$('.result-row').find('#day-book-approved-count').html(0);
						    					}else{
						    						$('.result-row').find('#day-book-approved-count').html('<a id="day-book-approved" href="#" count='+ data[loop].approvedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
						    					}
				    						}
				    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
				    							if(data[loop].declinedDNCount == undefined){
					    							$('.result-row').find('#day-book-decline-count').html(0);
							    					}else{
							    						$('.result-row').find('#day-book-decline-count').html('<a id="day-book-decline" href="#" count='+ data[loop].declinedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
							    					}
				    							
						    					}
					    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
					    						if(data[loop].pendingDNCount == undefined){
					    							$('.result-row').find('#day-book-pending-count').html(0);
							    					}else{
							    						$('.result-row').find('#day-book-pending-count').html('<a id="day-book-pending" href="#" count='+ data[loop].pendingDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
							    					}					
						    					}
			                }
			});
		});//click
			//counts dialog
			$("#day-book-history-transaction-dialog").dialog({
				   autoOpen : false,
				   height : 350,
				   width : 350,
				   modal : true,
				   buttons : {
				    Close : function() {
				     $(this).dialog('close');
				    }
				   },
				   close : function() {
				    $('#day-book-history-transaction-container').html('');
				   }
				 });
			//day_book approved,decline,pending counts click
			$('#day-book-approved').live('click',function(){
				var dayBookNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var count=$(this).attr('count');
				var str="DB";
				if(dayBookNumber.indexOf(str) != -1){
				$.post('dayBookCr.json','action=get-day-book-invoices-history-transaction&dayBookNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for DayBook based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var dayBook='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
			    		   dayBook= dayBook + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 777px;">'+
				    			'<div class="inner-table" style="width: 782px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   dayBook= dayBook +'<div class="invoice-boxes-colored"><div>'+
				    						'<span class="span-label">Invoice Number</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested By</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested Date</span>'+
				    					'</div>'+
				    				'</div>';
				    		 if(data[0].status == "APPROVED"){
				    			 dayBook= dayBook +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
				    					'<div>'+
				    						'<span class="span-label">Approved By</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
				    					'<div>'+
				    						'<span class="span-label">Approved Date</span>'+
				    					'</div>'+
				    				'</div>';
				    		 }else{
				    			 dayBook= dayBook +'<div class="invoice-boxes-colored">'+
			    					'<div>'+
			    						'<span class="span-label">Declined By</span>'+
			    					'</div>'+
			    				'</div>'+
			    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
			    					'<div>'+
			    						'<span class="span-label">Declined Date</span>'+
			    					'</div>'+
			    				'</div>';
				    		 }
				    		 var count=0;
				    					for(var i=0;i<data.length;i++)
				    					{
				    					count++;
				    					dayBook= dayBook +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="day-book-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
						    					 data[i].invoiceNumber
						    					 +'</a>'
				    						     +'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
				    						'<div>'+
				    							'<span class="property">'+data[i].requestedBy+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
				    						'</div>'+
				    					'</div>'+


				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    				'</div>';
				    }//for
				    					dayBook=dayBook+ '</div>'+
							    		'</div>'+
							    		'</div>';
				       $('#day-book-invoice-history-transaction-container').html(dayBook);
			    	   $("#day-book-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#day-book-invoice-history-transaction-dialog").dialog({
					   autoOpen : false,
					   height : 520,
					   width : 900,
					   modal : true,
					   buttons : {
					    Close : function() {
					     $(this).dialog('close');
					    }
					   },
					   close : function() {
					    $('#day-book-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#day-book-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var dayBookNumber=$(this).attr('class');
			    	var dayBookId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/day_book_change_transaction_history_view.jsp','id=' + dayBookId,
					           function(data) {
					            $('#day-book-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 600,
     						     		   width : 900,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#day-book-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
				});
			});
			//Day Book decline
			$('#day-book-decline').live('click',function(){
				var dayBookNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var count=$(this).attr('count');
				var str="DB";
				if(dayBookNumber.indexOf(str) != -1){
				$.post('dayBookCr.json','action=get-day-book-invoices-history-transaction&dayBookNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for DayBook based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var dayBook='<div class="outline" style="margin-left: 10px; width: 775px;min-height: 0px;">';
			    		   dayBook= dayBook + '<div class="invoice-main-table" style="overflow:hidden;margin:0px; width: 777px;">'+
				    			'<div class="inner-table" style="width: 782px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   dayBook= dayBook +'<div class="invoice-boxes-colored"><div>'+
				    						'<span class="span-label">Invoice Number</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested By</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested Date</span>'+
				    					'</div>'+
				    				'</div>';
				    		 if(data[0].status == "APPROVED"){
				    			 dayBook= dayBook +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
				    					'<div>'+
				    						'<span class="span-label">Approved By</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
				    					'<div>'+
				    						'<span class="span-label">Approved Date</span>'+
				    					'</div>'+
				    				'</div>';
				    		 }else{
				    			 dayBook= dayBook +'<div class="invoice-boxes-colored">'+
			    					'<div>'+
			    						'<span class="span-label">Declined By</span>'+
			    					'</div>'+
			    				'</div>'+
			    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
			    					'<div>'+
			    						'<span class="span-label">Declined Date</span>'+
			    					'</div>'+
			    				'</div>';
				    		 }
				    		 var count=0;
				    					for(var i=0;i<data.length;i++)
				    					{
				    					count++;
				    					dayBook= dayBook +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="day-book-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
						    					 data[i].invoiceNumber
						    					 +'</a>'
				    						     +'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
				    						'<div>'+
				    							'<span class="property">'+data[i].requestedBy+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
				    						'</div>'+
				    					'</div>'+


				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].modifiedBy+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].modifiedDate+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    				'</div>';
				    }//for
				    					dayBook=dayBook+ '</div>'+
							    		'</div>'+
							    		'</div>';
				       $('#day-book-invoice-history-transaction-container').html(dayBook);
			    	   $("#day-book-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#day-book-invoice-history-transaction-dialog").dialog({
					   autoOpen : false,
					   height : 520,
					   width : 900,
					   modal : true,
					   buttons : {
					    Close : function() {
					     $(this).dialog('close');
					    }
					   },
					   close : function() {
					    $('#day-book-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#day-book-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var dayBookNumber=$(this).attr('class');
			    	var dayBookId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/day_book_change_transaction_history_view.jsp','id=' + dayBookId,
					           function(data) {
					            $('#day-book-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 600,
     						     		   width : 900,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#day-book-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
				});
			});
			//DN Pending Count
			$('#day-book-pending').live('click',function(){
				var dayBookNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var str="DB";
				if(dayBookNumber.indexOf(str) != -1){
				$.post('dayBookCr.json','action=get-day-book-invoices-history-transaction&dayBookNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for DayBook based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var dayBook='<div class="outline" style="margin-left: 10px; width: 478px; min-height: 0px;">';
			    		   dayBook= dayBook + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 480px;">'+
				    			'<div class="inner-table" style="width: 782px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   dayBook= dayBook +'<div class="invoice-boxes-colored"><div>'+
				    						'<span class="span-label">Invoice Number</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 100px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested By</span>'+
				    					'</div>'+
				    				'</div>'+
				    				'<div class="invoice-boxes-colored" Style="width: 150px;">'+
				    					'<div>'+
				    						'<span class="span-label">Requested Date</span>'+
				    					'</div>'+
				    				'</div>';
				    		 var count=0;
				    					for(var i=0;i<data.length;i++)
				    					{
				    					count++;
				    					dayBook= dayBook +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="day-book-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
						    					 data[i].invoiceNumber
						    					 +'</a>'
				    						     +'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
				    						'<div>'+
				    							'<span class="property">'+data[i].requestedBy+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
				    						'<div>'+
				    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    				'</div>';
				    }//for
				    					dayBook=dayBook+ '</div>'+
							    		'</div>'+
							    		'</div>';
				    					$('#day-book-invoice-history-transaction-container').html(dayBook);
				    			    	   $("#day-book-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#day-book-invoice-history-transaction-dialog").dialog({
					   autoOpen : false,
					   height : 400,
					   width : 600,
					   modal : true,
					   buttons : {
					    Close : function() {
					     $(this).dialog('close');
					    }
					   },
					   close : function() {
					    $('#day-book-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#day-book-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var dayBookNumber=$(this).attr('class');
			    	var dayBookId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/day_book_change_transaction_history_view.jsp','id=' + dayBookId,
					           function(data) {
					            $('#day-book-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 600,
     						     		   width : 900,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#day-book-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
				});
			});
},
};