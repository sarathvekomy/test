//Approvals Sales Return History Tracker
var ApprovalSalesReturnHistoryHandler = {
ApprovalsSalesReturnHistoryTrackerFunction: function(){
	//approval sales return header click
	$('#sales-return-approval-txn-history').live('click',function(){
		var approvalSalesReturn='<div class="outline" style="margin-left: 10px; width: 288px;  min-height: 90px;">';
		approvalSalesReturn= approvalSalesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 290px;">'+
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
		approvalSalesReturn= approvalSalesReturn +'<div class="result-row" style="width:292px;" id="row-1">'+
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
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-sales-return-approved-count" Style="width: 96px; align: center;">'+
						'<div>'+
							'<span class="property-right-float" Style="align: center;"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
					//second row
		approvalSalesReturn= approvalSalesReturn +'<div class="result-row" style="width:292px;" id="row-2">'+
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
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-sales-return-decline-count" Style="width: 96px;">'+
						'<div>'+
							'<span class="property-right-float"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
					//Third row
		approvalSalesReturn= approvalSalesReturn +'<div class="result-row" style="width:292px;" id="row-3">'+
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
					'<div class="invoice-boxes invoice-boxes-<%=count%>" id="approval-sales-return-pending-count" Style="width: 96px;">'+
						'<div>'+
							'<span class="property-right-float"></span>'+
						'</div>'+
					'</div>'
				+'</div>';
		approvalSalesReturn=approvalSalesReturn+ '</div>'+
	    		    '</div>';
	    		    
	$('#approval-sales-return-history-transaction-container').html(approvalSalesReturn);
    $("#approval-sales-return-history-transaction-dialog").dialog('open');
			$.post('salesReturn.json','action=get-approval-sales-return-history-transaction',function(obj){
				//result class will get all approved,decline,pending CR for Approval SalesReturn
		    	   var data=obj.result.data;
		    	   var defaultValue=0;
		    	   if(data.length <= 0){
							$('.result-row').find('#approval-sales-return-approved-count').html('<div style="color:#000; font-weight:normal; outline:none;text-decoration:none !important;">'+ defaultValue + '</div>');
   							$('.result-row').find('#approval-sales-return-decline-count').html('<div style="color:#000; font-weight:normal; outline:none;text-decoration:none !important;">'+ defaultValue + '</div>');
   							$('.result-row').find('#approval-sales-return-pending-count').html('<div style="color:#000; font-weight:normal; outline:none;text-decoration:none !important;">'+ defaultValue + '</div>');
		    	   }else{
		    		   var count=0;
	    		        for(var loop=0;loop < data.length;loop=loop+1) {
	    					count++;
	    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
	    							if(data[loop].approvedDNCount == undefined){
	    							$('.result-row').find('#approval-sales-return-approved-count').html('<div style="color:#000; font-weight:normal; outline:none;text-decoration:none !important;">'+ defaultValue + '</div>');
			    					}else{
			    						$('.result-row').find('#approval-sales-return-approved-count').html('<a id="approval-sales-return-approved" href="#" count='+ data[loop].approvedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
			    					}
	    						}
	    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
	    							if(data[loop].declinedDNCount == undefined){
		    							$('.result-row').find('#approval-sales-return-decline-count').html(0);
				    					}else{
				    						$('.result-row').find('#approval-sales-return-decline-count').html('<a id="approval-sales-return-decline" href="#" count='+ data[loop].declinedDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
				    					}
	    							
			    					}
		    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
		    						if(data[loop].pendingDNCount == undefined){
		    							$('.result-row').find('#approval-sales-return-pending-count').html(0);
				    					}else{
				    						$('.result-row').find('#approval-sales-return-pending-count').html('<a id="approval-sales-return-pending" href="#" count='+ data[loop].pendingDNCount +' comp='+ data[loop].deliveryNoteTxnStatus +' align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
				    					}					
			    					}
                           }
		    	   }

			});
		});//click
			//counts dialog
			$("#approval-sales-return-history-transaction-dialog").dialog({
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
				    $('#approval-sales-return-history-transaction-container').html('');
				   }
				 });
			//Approval Sales Return approved,decline,pending counts click
			$('#approval-sales-return-approved').live('click',function(){
				var invoiceNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var count=$(this).attr('count');
				var str="SR";
				if(invoiceNumber.indexOf(str) != -1){
				$.post('salesReturn.json','action=get-approval-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var approvalSalesReturn='<div class="outline" style="margin-left: 10px; width: 860px;min-height:0px;">';
			    		   approvalSalesReturn= approvalSalesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 861px;">'+
				    			'<div class="inner-table" style="width: 861px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
				    		 if(data[0].status == "Approved"){
				    			 approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
				    			 approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored">'+
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
				    					approvalSalesReturn= approvalSalesReturn +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="approval-sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
				    					approvalSalesReturn=approvalSalesReturn+ '</div>'+
							    		'</div>'+
							    		'</div>';
				       $('#approval-sales-return-invoice-history-transaction-container').html(approvalSalesReturn);
			    	   $("#approval-sales-return-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#approval-sales-return-invoice-history-transaction-dialog").dialog({
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
					    $('#approval-sales-return-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#approval-sales-return-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var invoiceNumber=$(this).attr('class');
			    	var salesReturnId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/sales_return_dashboard_approval_history_view.jsp','id=' + salesReturnId,
					           function(data) {
					            $('#approval-sales-return-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 550,
     						     		   width : 1020,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#approval-sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
			 });
			});
			//Approval Sales Return  decline
			$('#approval-sales-return-decline').live('click',function(){
				var invoiceNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var count=$(this).attr('count');
				var str="SR";
				if(invoiceNumber.indexOf(str) != -1){
				$.post('salesReturn.json','action=get-approval-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for DeliveryNote based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var approvalSalesReturn='<div class="outline" style="margin-left: 10px; width: 860px; min-height:0px;">';
			    		   approvalSalesReturn = approvalSalesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 861px;">'+
				    			'<div class="inner-table" style="width: 861px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
				    			 approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
				    			 approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored">'+
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
				    					approvalSalesReturn= approvalSalesReturn +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="approval-sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
				    					approvalSalesReturn=approvalSalesReturn+ '</div>'+
							    		'</div>'+
							    		'</div>';
				    					$('#approval-sales-return-invoice-history-transaction-container').html(approvalSalesReturn);
				    			    	$("#approval-sales-return-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#approval-sales-return-invoice-history-transaction-dialog").dialog({
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
					    $('#approval-sales-return-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#approval-sales-return-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var invoiceNumber=$(this).attr('class');
			    	var salesReturnId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/sales_return_dashboard_approval_history_view.jsp','id=' + salesReturnId,
					           function(data) {
					            $('#approval-sales-return-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 550,
     						     		   width : 1020,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#approval-sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
			 });
			});
			//Approval Sales Return Pending Count
			$('#approval-sales-return-pending').live('click',function(){
				var invoiceNumber = $(this).attr('align');
				var status=$(this).attr('comp');
				var str="SR";
				if(invoiceNumber.indexOf(str) != -1){
				$.post('salesReturn.json','action=get-approval-sales-return-invoices-history-transaction&invoiceNumber='+str+'&status='+status,
						function(obj){
					//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
			    	   var data=obj.result.data;
			    		   var approvalSalesReturn='<div class="outline" style="margin-left: 10px; width: 562px; min-height: 0px;">';
			    		   approvalSalesReturn= approvalSalesReturn + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 565px;">'+
				    			'<div class="inner-table" style="width: 565px;">'+
				    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
				    					'<div>'+
				    						'<span class="span-label">S.No</span>'+
				    					'</div>'+
				    				'</div>';
				    		 
			    		   approvalSalesReturn= approvalSalesReturn +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
				    					approvalSalesReturn= approvalSalesReturn +'<div class="result-row" id="row-<%=count%>">'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
				    						'<div>'+
				    							'<span class="property">'+count+'</span>'+
				    						'</div>'+
				    					'</div>'+
				    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
				    						'<div>'+
				    							'<span class="property">'+
				    							'<a id="approval-sales-return-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
				    					approvalSalesReturn=approvalSalesReturn+ '</div>'+
							    		'</div>'+
							    		'</div>';
				    					$('#approval-sales-return-invoice-history-transaction-container').html(approvalSalesReturn);
				    			    	$("#approval-sales-return-invoice-history-transaction-dialog").dialog('open');
				});
				 }//if
				//invoices dialog
				$("#approval-sales-return-invoice-history-transaction-dialog").dialog({
					   autoOpen : false,
					   height : 400,
					   width : 620,
					   modal : true,
					   buttons : {
					    Close : function() {
					     $(this).dialog('close');
					    }
					   },
					   close : function() {
					    $('#approval-sales-return-invoice-history-transaction-container').html('');
					   }
					  });
				//invoices click
				var $anchor = $('.result-row').find('a#approval-sales-return-invoice-number-histroy');
			    $anchor.live('click',function(){
			    	var invoiceNumber=$(this).attr('class');
			    	var salesReturnId=$(this).attr('align');
			    	var count=$(this).attr('count');
			    	$.post('my-sales/transactions/change-transactions/sales_return_dashboard_approval_history_view.jsp','id=' + salesReturnId,
					           function(data) {
					            $('#approval-sales-return-invoices-view-histroy-container-'+count).html(data);
					            var dialogOpts = {
     						     		   height : 550,
     						     		   width : 1020,
					            		buttons : {
     						     		    Close : function() {
     						     		     $(this).dialog('close');
     						     		    }
     						     		 },
					            	};
					            $("#approval-sales-return-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
			 });
   });
},
};
var ApprovalsJournalHistoryTrackerHandler= {
		//Approvals Journal History Tracker
	ApprovalsJournalHistoryTrackerFunction: function(){
		//approval journal header click
		$('#journal-approval-txn-history').live('click',function(){
			$.post('journal.json','action=get-configured-journal-types-invoice-pattern',function(obj){
				//result class will get all journal type,with invoice pattern from vb_journal_types
		    	   var data=obj.result.data;
		    	   var approvalJournal='<div class="outline" style="margin-left: 10px; width: 412px;  min-height: 0px;">';
					approvalJournal= approvalJournal + '<div class="invoice-main-table" style="overflow:hidden;  margin-bottom:0px !important;width: 412px;">'+
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
		    				'<div class="invoice-boxes-colored" Style="width: 80px; ">'+
	    					'<div>'+
	    						'<span class="span-label">Count</span>'+
	    					'</div>'+
	    				'</div>';
					+'</div>';
					approvalJournal=approvalJournal+ '</div>'+
		    	    		    '</div>';
					 var count=0;
						for(var i=0;i<data.length;i++)
						{
						count++;
						approvalJournal= approvalJournal +'<div class="result-row" Style="width: 412px;" id="row-<%=count%>" >'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width:80px;">'+
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
							approvalJournal= approvalJournal +'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px; ">'+
							'<div>'+
								'<span class="property-right-float">'+ 0 +'</span>'+
							'</div>'+
						'</div>'+
					   '</div>';
						}else{
						approvalJournal= approvalJournal +'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 78px;border-right:none !important;">'+
							'<div>'+
								'<span class="property-right-float">'+
								'<a id="total-journal-txn-count-histroy" count="'+count+'" href="#" class="'+ data[i].journalInvoicePattern +'" align="'+ data[i].journalTransactionType +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[i].totalJournalTypeCount
		    					 +'</a>'
							     +'</span>'+
							'</div>'+
						'</div>'+
					'</div>';
						}
	           }//for
						$('#approval-journal-history-transaction-container').html(approvalJournal);
				        $("#approval-journal-history-transaction-dialog").dialog('open');
			});
			});//click
				//counts dialog
				$("#approval-journal-history-transaction-dialog").dialog({
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
					    $('#approval-journal-history-transaction-container').html('');
					   }
					 });
				//design for Approved,pending,count status of specific journal counts
	        //approved,pending,decline journal design
				//Journal Type with respective invoice number click
				$('#total-journal-txn-count-histroy').live('click',function(){
					var journalTxnType = $(this).attr('align');
					var invoicePattern=$(this).attr('class');
					$.post('journal.json','action=get-specific-journal-type-count&journalTxnType='+journalTxnType+'&invoicePattern='+invoicePattern,
							function(obj){
						var approvalJournal='<div class="outline" style="margin-left: 10px; width: 288px;  min-height: 0px;">';
						approvalJournal= approvalJournal + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 290px;">'+
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
						approvalJournal= approvalJournal +'<div class="result-row" style="width:290px;" id="row-1">'+
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
						approvalJournal= approvalJournal +'<div class="result-row" style="width:290px;" id="row-2">'+
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
						approvalJournal= approvalJournal +'<div class="result-row" style="width:290px;" id="row-3">'+
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
						
						approvalJournal=approvalJournal+ '</div>'+
			    	    		    '</div>';
						   $('#approval-specific-journal-history-transaction-container').html(approvalJournal);
		    		       $("#approval-specific-journal-history-transaction-dialog").dialog('open');
						//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
				    	   var data=obj.result.data;
				    	   var count=0;
		    		        for(var loop=0;loop < data.length;loop=loop+1) {
		    					count++;
		    						if(data[loop].deliveryNoteTxnStatus == "APPROVED"){
		    							if(data[loop].approvedDNCount == undefined){
			    							$('.result-row').find('#approval-journal-approved-count').html(0);
					    					}else{
					    						$('.result-row').find('#approval-journal-approved-count').html('<a id="approval-journal-approved" count="'+ data[loop].approvedDNCount +'"  href="#" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedDNCount + '</a>');
					    					}
		    						}
		    						if(data[loop].deliveryNoteTxnStatus == "DECLINE"){
		    							if(data[loop].declinedDNCount == undefined){
			    							$('.result-row').find('#approval-journal-decline-count').html(0);
					    					}else{
					    						$('.result-row').find('#approval-journal-decline-count').html('<a id="approval-journal-decline" href="#" count="'+ data[loop].declinedDNCount +'" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedDNCount + '</a>');
					    					}
				    					}
		    							
			    					if(data[loop].deliveryNoteTxnStatus == "PENDING"){
			    						if(data[loop].pendingDNCount == undefined){
			    							$('.result-row').find('#approval-journal-pending-count').html(0);
					    					}else{
					    						$('.result-row').find('#approval-journal-pending-count').html('<a id="approval-journal-pending" href="#" count="'+ data[loop].pendingDNCount +'" class="'+ data[loop].deliveryNoteTxnStatus +'" align='+ data[loop].deliveryNoteTransactionType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingDNCount + '</a>');
					    					}	
				    					}
	                           }//for loop
	                     });
					});
				//invoices dialog
				$("#approval-specific-journal-history-transaction-dialog").dialog({
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
					    $('#approval-specific-journal-history-transaction-container').html('');
					   }
				});
				//Approvals journal approved,decline,pending counts click based on specific journal type and invoice pattern
				$('#approval-journal-approved').live('click',function(){
					var journalType = $(this).attr('align');
					var status=$(this).attr('class');
					var approvedJournalCount=$(this).attr('count');
					$.post('journal.json','action=get-approval-journal-invoices-history-transaction&journalType='+journalType+'&status='+status,
							function(obj){
						//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
				    	   var data=obj.result.data;
				    		   var approvalJournal='<div class="outline" style="margin-left: 10px; width: 860px; min-height: 0px;">';
				    		   approvalJournal= approvalJournal + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 861px;">'+
					    			'<div class="inner-table" style="width: 861px;">'+
					    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
					    					'<div>'+
					    						'<span class="span-label">S.No</span>'+
					    					'</div>'+
					    				'</div>';
					    		 
				    		   approvalJournal= approvalJournal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
					    			 approvalJournal= approvalJournal +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
					    			 approvalJournal= approvalJournal +'<div class="invoice-boxes-colored">'+
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
					    					approvalJournal= approvalJournal +'<div class="result-row" id="row-<%=count%>">'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
					    						'<div>'+
					    							'<span class="property">'+count+'</span>'+
					    						'</div>'+
					    					'</div>'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
					    						'<div>'+
					    							'<span class="property">'+
					    							'<a id="approval-journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
					    					approvalJournal=approvalJournal+ '</div>'+
								    		'</div>'+
								    		'</div>';
					       $('#approval-journal-invoice-history-transaction-container').html(approvalJournal);
				    	   $("#approval-journal-invoice-history-transaction-dialog").dialog('open');
					});
					// }//if
					//invoices dialog
					$("#approval-journal-invoice-history-transaction-dialog").dialog({
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
						    $('#approval-journal-invoice-history-transaction-container').html('');
						   }
						  });
					//invoices click
					var $anchor = $('.result-row').find('a#approval-journal-invoice-number-histroy');
				    $anchor.live('click',function(){
				    	var invoiceNumber=$(this).attr('class');
				    	var journalId=$(this).attr('align');
				    	var count=$(this).attr('count');
				    	//$("#approvedJournalCount").val(approvedJournalCount);
				    	/*var originalJournal='<div id="approval-journal-invoices-view-histroy-dialog-'+ApprovalsJournalHistoryTrackerHandler.count+'" style="display: none" title="Journal Invoice Transaction History View">'+
						'<div id="approval-journal-invoices-view-histroy-container-'+ApprovalsJournalHistoryTrackerHandler.count+'" style="width:200px;height: 200px"></div>'+
					    '</div>';*/
				    	/*for(var i=1; i<= approvedJournalCount; i++ ){
				    	var originalJournal='<div id="approval-journal-invoices-view-histroy-dialog-'+i+'" style="display: none" title="Journal Invoice Transaction History View">'+
						'<div id="approval-journal-invoices-view-histroy-container-'+i+'" style="width:200px;height: 200px"></div>'+
					    '</div>';
				    	}//for loop
	                 */	    	
				    	$.post('my-sales/transactions/change-transactions/journal_dashboard_approval_history_view.jsp','id=' + journalId,
						           function(data) {
						            $('#approval-journal-invoices-view-histroy-container-'+count).html(data);
						            var dialogOpts = {
	     						     		   height : 400,
	     						     		   width : 1020,
						            		buttons : {
	     						     		    Close : function() {
	     						     		     $(this).dialog('close');
	     						     		    }
	     						     		 },
						            	};
						            $("#approval-journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
						            count=count+1;
						 });
					  });
				});
				
				//Approval Journal decline
				$('#approval-journal-decline').live('click',function(){
					var journalType = $(this).attr('align');
					var status=$(this).attr('class');
					var declinedJournalCount=$(this).attr('count');
					$.post('journal.json','action=get-approval-journal-invoices-history-transaction&journalType='+journalType+'&status='+status,
							function(obj){
						//result class will display all CR invoices for Approval Journal based on respective status(Approved,Decline,Pending)
				    	   var data=obj.result.data;
				    		   var approvalJournal='<div class="outline" style="margin-left: 10px; width: 860px; min-height: 0px;">';
				    		   approvalJournal= approvalJournal + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 861px;">'+
					    			'<div class="inner-table" style="width: 861px;">'+
					    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
					    					'<div>'+
					    						'<span class="span-label">S.No</span>'+
					    					'</div>'+
					    				'</div>';
					    		 
				    		   approvalJournal= approvalJournal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
					    			 approvalJournal= approvalJournal +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
					    			 approvalJournal= approvalJournal +'<div class="invoice-boxes-colored">'+
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
					    					approvalJournal= approvalJournal +'<div class="result-row" id="row-<%=count%>">'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
					    						'<div>'+
					    							'<span class="property">'+count+'</span>'+
					    						'</div>'+
					    					'</div>'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
					    						'<div>'+
					    							'<span class="property">'+
					    							'<a id="approval-journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
					    					approvalJournal=approvalJournal+ '</div>'+
								    		'</div>'+
								    		'</div>';
					    					$('#approval-journal-invoice-history-transaction-container').html(approvalJournal);
					    			    	$("#approval-journal-invoice-history-transaction-dialog").dialog('open');
					});
					// }//if
					//invoices dialog
					$("#approval-journal-invoice-history-transaction-dialog").dialog({
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
						    $('#approval-journal-invoice-history-transaction-container').html('');
						   }
						  });
					//invoices click
					var $anchor = $('.result-row').find('a#approval-journal-invoice-number-histroy');
				    $anchor.live('click',function(){
				    	var invoiceNumber=$(this).attr('class');
				    	var journalId=$(this).attr('align');
				    	var count=$(this).attr('count');
				    	$.post('my-sales/transactions/change-transactions/journal_dashboard_approval_history_view.jsp','id=' + journalId,
						           function(data) {
						            $('#approval-journal-invoices-view-histroy-container-'+count).html(data);
						            var dialogOpts = {
	     						     		   height : 400,
	     						     		   width : 1020,
						            		buttons : {
	     						     		    Close : function() {
	     						     		     $(this).dialog('close');
	     						     		    }
	     						     		 },
						            	};
						            $("#approval-journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
						            count=count+1;
						 });
					  });
		});
				//Approval Journal Pending Count
				$('#approval-journal-pending').live('click',function(){
					var journalType = $(this).attr('align');
					var status=$(this).attr('class');
					var pendingJournalCount=$(this).attr('count');
					$.post('journal.json','action=get-approval-journal-invoices-history-transaction&journalType='+journalType+'&status='+status,
							function(obj){
						//result class will display all CR invoices for Journal based on respective status(Approved,Decline,Pending)
				    	   var data=obj.result.data;
				    		   var approvalJournal='<div class="outline" style="margin-left: 10px; width: 564px; min-height: 0px;">';
				    		   approvalJournal= approvalJournal + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 565px;">'+
					    			'<div class="inner-table" style="width: 565px;">'+
					    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
					    					'<div>'+
					    						'<span class="span-label">S.No</span>'+
					    					'</div>'+
					    				'</div>';
					    		 
				    		   approvalJournal= approvalJournal +'<div class="invoice-boxes-colored" style="width: 230px;"><div>'+
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
					    					approvalJournal= approvalJournal +'<div class="result-row" id="row-<%=count%>">'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
					    						'<div>'+
					    							'<span class="property">'+count+'</span>'+
					    						'</div>'+
					    					'</div>'+
					    					'<div class="invoice-dashboard-boxes invoice-boxes-<%=count%>" Style="width: 230px;">'+
					    						'<div>'+
					    							'<span class="property">'+
					    							'<a id="approval-journal-invoice-number-histroy" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
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
					    					approvalJournal=approvalJournal+ '</div>'+
								    		'</div>'+
								    		'</div>';
					    					$('#approval-journal-invoice-history-transaction-container').html(approvalJournal);
					    			    	$("#approval-journal-invoice-history-transaction-dialog").dialog('open');
					});
					// }//if
					//invoices dialog
					$("#approval-journal-invoice-history-transaction-dialog").dialog({
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
						    $('#approval-journal-invoice-history-transaction-container').html('');
						   }
						  });
					//invoices click
					var $anchor = $('.result-row').find('a#approval-journal-invoice-number-histroy');
				    $anchor.live('click',function(){
				    	var invoiceNumber=$(this).attr('class');
				    	var journalId=$(this).attr('align');
				    	var count=$(this).attr('count');
				    	$.post('my-sales/transactions/change-transactions/journal_dashboard_approval_history_view.jsp','id=' + journalId,
						           function(data) {
						            $('#approval-journal-invoices-view-histroy-container-'+count).html(data);
						            var dialogOpts = {
	     						     		   height : 400,
	     						     		   width : 1020,
						            		buttons : {
	     						     		    Close : function() {
	     						     		     $(this).dialog('close');
	     						     		    }
	     						     		 },
						            	};
						            $("#approval-journal-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
						            count=count+1;
						 });
					  });
				});
	},
	};
var CustomerChangeRequestHistoryTrackerHandler={
		//Customer Change Request History Tracker
		CustomerChangeRequestHistoryTracker: function(){
		$('#customer-change-request-approval-txn-history').live('click',function(){
		var customerCR='<div class="outline" style="margin-left: 10px; width: 399px; min-height: 0px;">';
		customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 400px;">'+
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
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-1">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">1</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">New Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Approved</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-new-approved-count" Style="width: 96px; align: center;">'+
							'<div>'+
								'<span class="property-right-float" Style="align: center;"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
						//second row
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-2">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">2</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">New Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Declined</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-new-decline-count" Style="width: 96px;">'+
							'<div>'+
								'<span class="property-right-float"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
						//Third row
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-3">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">3</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">New Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Pending</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-new-pending-count" Style="width: 96px;">'+
							'<div>'+
								'<span class="property-right-float"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
						//fourth row
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-4">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">4</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Existing Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Approved</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-existing-approved-count" Style="width: 96px;">'+
							'<div>'+
								'<span class="property-right-float"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
						//fifth row
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-5">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">5</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Existing Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Declined</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-existing-decline-count" Style="width: 96px;">'+
							'<div>'+
								'<span class="property-right-float"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
						//sixth row
		customerCR= customerCR +'<div class="result-row" style="width:400px;" id="row-6">'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
							'<div>'+
								'<span class="property">6</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Existing Customers</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 110px;">'+
							'<div>'+
								'<span class="property">Pending</span>'+
							'</div>'+
						'</div>'+
						'<div class="invoice-boxes invoice-boxes-<%=count%>" id="customer-cr-existing-pending-count" Style="width: 96px;">'+
							'<div>'+
								'<span class="property-right-float"></span>'+
							'</div>'+
						'</div>'
					+'</div>';
		//end rows	    					
		customerCR=customerCR+ '</div>'+
			    '</div>';
			    
		$('#approval-customer-cr-history-transaction-container').html(customerCR);
		$("#approval-customer-cr-history-transaction-dialog").dialog('open');
		$.post('customerCr.json','action=get-customer-cr-history',function(obj){
			//result class will get all approved,decline,pending CR for New Customer and Existing Customer
			   var data=obj.result.data;
			   var str1="New Customer";
			    					var count=0;
			    		        for(var loop=0;loop < data.length;loop=loop+1) {
			    					count++;
			    					if(data[loop].customerType.indexOf(str1) != -1){
			    						if(data[loop].customerChangeStatus == "APPROVED"){
			    							if(data[loop].approvedNewCount == undefined){
			    							$('.result-row').find('#customer-cr-new-approved-count').html(0);
					    					}else{
					    						$('.result-row').find('#customer-cr-new-approved-count').html('<a id="customer-cr-new-approved" href="#" count='+ data[loop].approvedNewCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+ data[loop].approvedNewCount + '</a>');
					    					}
			    						}
			    						if(data[loop].customerChangeStatus == "DECLINE"){
			    							if(data[loop].declinedNewCount == undefined){
				    							$('.result-row').find('#customer-cr-new-decline-count').html(0);
						    					}else{
						    						$('.result-row').find('#customer-cr-new-decline-count').html('<a id="customer-cr-new-decline" href="#" count='+ data[loop].declinedNewCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedNewCount + '</a>');
						    					}
					    					}
				    					if(data[loop].customerChangeStatus == "PENDING"){
				    						if(data[loop].pendingNewCount == undefined){
				    							$('.result-row').find('#customer-cr-new-pending-count').html(0);
						    					}else{
						    						$('.result-row').find('#customer-cr-new-pending-count').html('<a id="customer-cr-new-pending" href="#" count='+ data[loop].pendingNewCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingNewCount + '</a>');
						    					}					
					    					}
			    					}else{
			    						if(data[loop].customerChangeStatus == "APPROVED"){
			    							if(data[loop].approvedExistingCount == undefined){
				    							$('.result-row').find('#customer-cr-existing-approved-count').html(0);
						    					}else{
						    						$('.result-row').find('#customer-cr-existing-approved-count').html('<a id="customer-cr-existing-approved" href="#" count='+ data[loop].approvedExistingCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].approvedExistingCount + '</a>');
						    					}	
					    					} 
			    						if(data[loop].customerChangeStatus == "DECLINE"){
			    							if(data[loop].declinedExistingCount == undefined){
				    							$('.result-row').find('#customer-cr-existing-decline-count').html(0);
						    					}else{
						    						$('.result-row').find('#customer-cr-existing-decline-count').html('<a id="customer-cr-existing-decline" href="#" count='+ data[loop].declinedExistingCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].declinedExistingCount + '</a>');
						    					}	
					    					}
				    					if(data[loop].customerChangeStatus == "PENDING"){
				    						if(data[loop].pendingExistingCount == undefined){
				    							$('.result-row').find('#customer-cr-existing-pending-count').html(0);
						    					}else{
						    						$('.result-row').find('#customer-cr-existing-pending-count').html('<a id="customer-cr-existing-pending" href="#" count='+ data[loop].pendingExistingCount +' comp='+ data[loop].customerChangeStatus +' align='+ data[loop].customerType +' style="color:#000; font-weight:bold; align: center; outline:none;text-decoration:none !important;">'+ data[loop].pendingExistingCount + '</a>');
						    					}	
					    					}
			    					}
		        }
		});
		});//click
		//counts dialog
		$("#approval-customer-cr-history-transaction-dialog").dialog({
			   autoOpen : false,
			   height : 400,
			   width : 470,
			   modal : true,
			   buttons : {
			    Close : function() {
			     $(this).dialog('close');
			    }
			   },
			   close : function() {
			    $('#approval-customer-cr-history-transaction-container').html('');
			   }
			  });
		//New Customer CR approved,decline,pending counts click
		$('#customer-cr-new-approved').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="New";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px;width: 777px;">'+
			    			'<div class="inner-table" style="width: 782px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored">'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].businessName +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+


			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
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
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			       $('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
		    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		//New Customer  decline
		$('#customer-cr-new-decline').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="New";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 775px;min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden;margin: 0px; width: 777px;">'+
			    			'<div class="inner-table" style="width: 782px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored">'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].businessName +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+


			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
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
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			       $('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
		    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		//New Customer CR Pending Count
		$('#customer-cr-new-pending').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="New";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 478px;min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin:0px; width: 480px;">'+
			    			'<div class="inner-table" style="width: 782px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    				'</div>';
			    }//for
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			    					$('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
							    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		//customer_cr_existing_customer approved,decline,pending counts click
		$('#customer-cr-existing-approved').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="Existing";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 777px;">'+
			    			'<div class="inner-table" style="width: 782px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored">'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].businessName +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+


			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
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
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			       $('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
		    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		$('#customer-cr-existing-decline').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="Existing";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 775px; min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 777px;">'+
			    			'<div class="inner-table" style="width: 782px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored" Style="width: 145px;">'+
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
			    			 customerCR= customerCR +'<div class="invoice-boxes-colored">'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].businessName +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+


			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 145px;">'+
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
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			       $('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
		    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		$('#customer-cr-existing-pending').live('click',function(){
			var customerType = $(this).attr('align');
			var status=$(this).attr('comp');
			var count=$(this).attr('count');
			var str="Existing";
			if(customerType.indexOf(str) != -1){
			$.post('customerCr.json','action=get-customer-cr-businessNames-history&customerType='+str+'&status='+status,
					function(obj){
				//result class will display all CR BusinessNames for Customer CR based on respective status(Approved,Decline,Pending)
		    	   var data=obj.result.data;
		    		   var customerCR='<div class="outline" style="margin-left: 10px; width: 554px;  min-height: 0px;">';
		    		   customerCR= customerCR + '<div class="invoice-main-table" style="overflow:hidden; margin: 0px; width: 555px;">'+
			    			'<div class="inner-table" style="width: 555px;">'+
			    				'<div class="invoice-boxes-colored" style="width: 80px;">'+
			    					'<div>'+
			    						'<span class="span-label">S.No</span>'+
			    					'</div>'+
			    				'</div>';
			    		 
		    		   customerCR= customerCR +'<div class="invoice-boxes-colored" style="width: 220px;"><div>'+
			    						'<span class="span-label">Business Name</span>'+
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
			    					customerCR= customerCR +'<div class="result-row" id="row-<%=count%>">'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 80px;">'+
			    						'<div>'+
			    							'<span class="property">'+count+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 220px;">'+
			    						'<div>'+
			    							'<span class="property">'+
			    							'<a id="customer-cr-history" count="'+count+'" href="#" class="'+ data[i].invoiceNumber +'" align="'+ data[i].id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					    					 data[i].businessName
					    					 +'</a>'
			    						     +'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 100px;">'+
			    						'<div>'+
			    							'<span class="property">'+data[i].requestedBy+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    					'<div class="invoice-boxes invoice-boxes-<%=count%>" Style="width: 150px;">'+
			    						'<div>'+
			    							'<span class="property-right-float">'+data[i].requestedDate+'</span>'+
			    						'</div>'+
			    					'</div>'+
			    				'</div>';
			    }//for
			    					customerCR=customerCR+ '</div>'+
						    		'</div>'+
						    		'</div>';
			    					$('#approval-customer-cr-invoice-history-transaction-container').html(customerCR);
							    	   $("#approval-customer-cr-invoice-history-transaction-dialog").dialog('open');
			});
			 }//if
			//BusinessNames dialog
			$("#approval-customer-cr-invoice-history-transaction-dialog").dialog({
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
				    $('#approval-customer-cr-invoice-history-transaction-container').html('');
				   }
				  });
			//BusinessNames click
			var $anchor = $('.result-row').find('a#customer-cr-history');
		    $anchor.live('click',function(){
		    	var customerBusinessName=$(this).attr('class');
		    	var customerId=$(this).attr('align');
		    	var count=$(this).attr('count');
		    	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+customerId+'&businessName='+customerBusinessName, function(obj) {
					var result = obj.result.data;
					$.post('customer/customer_change_request_dashboard_history_view.jsp','id=' + customerId,
					           function(data) {
					            $('#approval-customer-cr-invoices-view-histroy-container-'+count).html(data);
					            CustomerChangeRequestHistoryTrackerHandler.setDataToApprovalHistoryPage(result);
					            var dialogOpts = {
								     		   height : 550,
								     		   width : 750,
					            		buttons : {
								     		    Close : function() {
								     		     $(this).dialog('close');
								     		    }
								     		 },
					            	};
					            $("#approval-customer-cr-invoices-view-histroy-dialog-"+count).dialog(dialogOpts);
					            count=count+1;
					 });
		    	});
			});
		});
		},
		
		setDataToApprovalHistoryPage: function(result) {
			 $('.customerName').text(result.customerName);
			 $('.businessName').text(result.businessName);
	        	$('.crType').text(result.crType);
	        	if(result.gender == 'M'){
					$('.gender').text('Male');
				}else{
					$('.gender').text('Female');
				}
	        	$('.contactNumber').text(result.mobile);
	        	$('.email').text(result.email);
	        	$('.directLine').text(result.directLine);
	        	$('.alternateMobile').text(result.alternateMobile);
	        	$('.address1').text(result.addressLine1);
	        	$('.region').text(result.region);
	        	$('.address2').text(result.addressLine2);
	        	$('.locality').text(result.locality);
	        	$('.landMark').text(result.landmark);
	        	$('.city').text(result.city);
	        	$('.state').text(result.state);
	        	$('.zipCode').text(result.zipcode);
	        	$('.invoiceName').text(result.invoiceName);
	        	$('#creditLimit').val(parseFloat(result.creditLimit).toFixed(2));
	        	$('#creditOverdueDays').val(result.creditOverdueDays);
		}
		};