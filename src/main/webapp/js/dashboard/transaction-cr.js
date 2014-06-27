var DashboardDeliveryNoteCRHandler ={
		initDeliveryNoteCROnLoad: function(){
			var paramString='action=search-delivery-note-change-request-dashboard';
			$.post('deliveryNoteCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-delivery-note-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="delivery-note-change-request-row"  align="'+data[loop].id+'" style="height: 90px;">';
								} else {
									rowstr = '<a href="#"><div id="delivery-note-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 90px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].businessName + '</div>' +
								'<span class="property">'+Msg.DELIVERY_NOTE_INVOICE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].invoiceName + '</span><br/>' +
								'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
								'</div>' +
								'</div>' +
								'<div class="green-result-col-2">'+
								'<div class="result-body">' +'<span class="property">'+Msg.DELIVERY_NOTE_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
								'</div>' +'<span class="property">'+Msg.DELIVERY_NOTE_BALANCE_LABEL +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].balance) + '</span>' +
								'</div>' +
								'</a>';
						$('#search-delivery-note-results-list').append(rowstr);
					};
					$('#search-delivery-note-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-delivery-note-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Delivery Note CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.delivery-note-change-request').css( "width", "250px" );
			    	$('.sales-return-change-request').css( "width", "250px" );
			    	$('.day-book-change-request').css( "width", "245px" );
			    	$('.journal-change-request').css( "width", "245px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.delivery-note-change-request').css( "width", "210px" );
			    	$('.sales-return-change-request').css( "width", "205px" );
			    	$('.day-book-change-request').css( "width", "200px" );
			    	$('.journal-change-request').css( "width", "210px" );
			    	$('.search-row-results').css( "width", "200px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				}
				setTimeout(function() {
					$('#customer-change-request-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},	
};
$('#delivery-note-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#delivery-note-dashboard-view-container').html(data);  
	        	$("#delivery-note-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 550,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var thisButton = $(this);
	    				$('#delivery-note-approval-decline-container').html('Are You Sure Want To Approved Delivery Note Change Request ?');
	    				var dialogOpts = {
						     		   height : 200,
						     		   width : 500,
				            	   buttons : {
						     		   Yes : function() {
						     			  var statusParameter='Approved';
						    				 $.post('deliveryNoteCr.json', 'deliverNoteCRId='+id+'&action=approve-delivery-note-cr'+'&status='+statusParameter,
						    						 function(obj) {
						    					 DashboardDeliveryNoteCRHandler.initDeliveryNoteCROnLoad();
						    						$("#delivery-note-dashboard-view-dialog").dialog('close');
						    					});
						    				 $("#delivery-note-approval-decline-dialog").dialog('close');
						     		   },
						     		   No : function() {
						     			  $("#delivery-note-approval-decline-dialog").dialog('close');
    						     		 }
						     		 },
				            	};
				            $("#delivery-note-approval-decline-dialog").dialog(dialogOpts);
	    				},
	    		      Decline: function() {
	    		    	  $('#delivery-note-approval-decline-container').html('Are You Sure Want To Decline Delivery Note Change Request ?');
		    				var dialogOpts = {
							     		   height : 200,
							     		   width : 500,
					            	   buttons : {
							     		   Yes : function() {
							     			  var statusParameter='Decline';
						    		    	  $.post('deliveryNoteCr.json', 'deliverNoteCRId='+id+'&action=approve-delivery-note-cr'+'&status='+statusParameter,
							    						 function(obj) {
						    		    		  DashboardDeliveryNoteCRHandler.initDeliveryNoteCROnLoad();
							    						$("#delivery-note-dashboard-view-dialog").dialog('close');
							    					});
							    				 $("#delivery-note-approval-decline-dialog").dialog('close');
							     		   },
							     		   No : function() {
							     			  $("#delivery-note-approval-decline-dialog").dialog('close');
	    						     		 }
							     		 },
					            	};
					            $("#delivery-note-approval-decline-dialog").dialog(dialogOpts);
	    		          },
	    		         Cancel: function(){
	    		        	 $("#delivery-note-dashboard-view-dialog").dialog('close');
	    		         } 
	    				
	    			},
	    			close: function() {
	    				$('#delivery-note-dashboard-view-container').html('');
	    			}
	    		});
	        	//onclick of invoice number displaying delivery note view page before Delivery Note CR
	        	$('#change-request-dn-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		var deliveryNote=$(this).attr('align');
	        		$.post('deliveryNoteCr.json', 'action=get-delivery-note-id'+'&invoiceNumber='+invoiceNumber+'&deliveryNoteId='+deliveryNote,
	        				function(obj){
	        					var deliveryNoteId = obj.result.data;
	        					if(deliveryNoteId != 0){
	        						 $.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_previous_cr_view.jsp','id=' + deliveryNoteId,
	        						           function(data) {
	        						            $('#delivery-note-container').html(data);
	        						            var dialogOpts = {
	  	        						     		   height : 500,
	  	        						     		   width : 1020,
	        						            		buttons : {
	    	        						     		    Close : function() {
	    	        						     		     $(this).dialog('close');
	    	        						     		    }
	    	        						     		 },
	        						            	};
	        						            $("#delivery-note-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        	});
	    });
});
var DashboardSalesReturnCRHandler ={
		initSalesReturnCROnLoad: function(){
			var paramString='action=search-sales-return-change-request-dashboard';
			$.post('salesReturnCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-sales-return-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="sales-return-change-request-row"  align="'+data[loop].id+'" style="height: 70px;">';
								} else {
									rowstr = '<a href="#"><div id="sales-return-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 70px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].businessName + '</div>' +
								'<span class="property">'+Msg.SALES_RETURNS_CREATED_DATE +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
								'</div>' +
								'</div>' +
								'<div class="green-result-col-2">'+
								'<div class="result-body">' +
								'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].total) + '</span>' +
								'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
								'</div>' +
								'</a>';
						$('#search-sales-return-change-request-results-list').append(rowstr);
					};
					$('#search-sales-return-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-sales-return-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Sales Return CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
};
$('#sales-return-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$('#sales-return-change-request-dashboard-view-container').data('');
	$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#sales-return-change-request-dashboard-view-container').html(data);  
	        	//to display calculated cost and grand total with changed damaged and resalable qty
	        		var sum = 0;
	        		$('tr').each(function(index, value) {
	        			if(index == 0){
	        				return;
	        			}
						var productName = $(this).find('#productName').html();
						var batchNumber =$(this).find('#batchNumber').html();
						var damaged =$(this).find('#damaged').html();
						var resalable = $(this).find('#resalable').html();
						var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
						var damagedCost = currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
						var totalResalableCost = resalableCost * resalable;
						var totalDamagedCost = damagedCost * damaged;
						$(this).find('#totalCost').html((totalResalableCost + totalDamagedCost).toFixed(2));
						var totalCost =(totalResalableCost + totalDamagedCost);
						if(isNaN(totalCost)) {
					       	  totalCost = 0.00;
					       }
						if(resalableCost < 0 || damagedCost < 0){
							$('#sales-return-approval-decline-dialog').dialog('close');
							$('#sales-return-container').html('Cost must be positive');
		     			  		$("#sales-return-dialog").dialog({
		     			  			autoOpen: true,
		     			  			title:'Warning',
		     		    			height: 200,
		     		    			width: 350,
		     		    			modal: true,
		     		    			buttons: {
		     		    				Ok: function() {
		     		    					$(this).dialog('close');
		     		    				},
		     		    			},
		     			  		});
						    return false;
						}else{
							 $(this).find('#totalCost').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
						}
					     
					      sum += totalCost;
					      $('#grandTotal').html(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
		        	});
	        		//changed cost and grand total on blur event of product table
	        		$('table').live("blur",function(){
	        		var sum = 0;
	        		$('tr').each(function(index, value) {
	        			if(index == 0){
	        				return;
	        			}
						var productName = $(this).find('#productName').html();
						var batchNumber =$(this).find('#batchNumber').html();
						var damaged =$(this).find('#damaged').html();
						var resalable = $(this).find('#resalable').html();
						var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
						var damagedCost = currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
						var totalResalableCost = resalableCost * resalable;
						var totalDamagedCost = damagedCost * damaged;
					//	$(this).find('#totalCost').html((totalResalableCost + totalDamagedCost).toFixed(2));
						var totalCost =(totalResalableCost + totalDamagedCost);
						if(isNaN(totalCost)) {
					       	  totalCost = 0.00;
					       }
						if(resalableCost < 0 || damagedCost < 0){
							$('#sales-return-approval-decline-dialog').dialog('close');
							$('#sales-return-container').html('Cost must be positive');
		     			  		$("#sales-return-dialog").dialog({
		     			  			autoOpen: true,
		     			  			title:'Warning',
		     		    			height: 200,
		     		    			width: 350,
		     		    			modal: true,
		     		    			buttons: {
		     		    				Ok: function() {
		     		    					$(this).dialog('close');
		     		    				},
		     		    			},
		     			  		});
						    return false;
						}else{
							 $(this).find('#totalCost').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
						}
					     
					      sum += totalCost;
					      $('#grandTotal').html(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
		        	});
	        	})
	        	$("#sales-return-change-request-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 420,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    				//Approved for Sales Return Change Request in Txn CR
	    			Approve: function() {
	    				//var thisButton = $(this);
	    				 $("#sales-return-CR-approval-decline-container").html('Are You Sure Want To Approve Sales Return Change Request?');
				            var dialogOpts = {
					     		   height : 200,
					     		   width : 500,
					     		   title:'Sales Return Change Request',
				            		buttons : {
						     		    Yes : function() {
						     		    	//check validation for sold qty in SR CR for android app
						     		    	var approvedListOfObjects='';
						     		    	var result;
						     		    	var businessName;
						     		    	 var totalQuantity;
						     		    	 var invoiceNumber;
						     		    	 var resultMessage;
						     		    	var grandTotal =  currencyHandler.convertStringPatternToFloat($('#grandTotal').html());
						     			  	$('tr').each(function(index, value) {
						     			  		if(index == 0){
						     			  			return;
						     			  		}
												var productName = $(this).find('#productName').html();
												var batchNumber =$(this).find('#batchNumber').html();
												var damaged =currencyHandler.convertStringPatternToFloat($(this).find('#damaged').html());
												var resalable =currencyHandler.convertStringPatternToFloat($(this).find('#resalable').html());
												 totalQuantity = damaged + resalable;
												var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
												var damagedCost =currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
												var totalResalableCost = resalableCost * resalable;
												 businessName = $('#businessName').text();
												var totalDamagedCost = damagedCost * damaged;
												if($(this).find('#totalCost').html() != null){
												var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#totalCost').html());
												}
												 invoicenumber = $('#invoiceNo').text();
												if(resalableCost < 0 || damagedCost < 0){
													$('#sales-return-approval-decline-dialog').dialog('close');
													$('#sales-return-container').html('Cost must be positive');
								     			  		$("#sales-return-dialog").dialog({
								     			  			autoOpen: true,
								     			  			title:'Warning',
								     		    			height: 200,
								     		    			width: 350,
								     		    			modal: true,
								     		    			buttons: {
								     		    				Ok: function() {
								     		    					$(this).dialog('close');
								     		    				},
								     		    			},
								     			  		});
												    return false;
												}else if(damaged != "" || resalable != ""){
													
													if(damaged == ""){
														if(resalableCost == ""){
															result = false;
														}
														
													}else if(resalable == ""){
														if(damagedCost == ""){
															result = false;
														}
													}else{
														if(resalableCost == "" || damagedCost == ""){
															result = false;
														}
													}
												}
												if(result != false){
													if(index == 0) {
														approvedListOfObjects +=damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
													} else {
														approvedListOfObjects +=','+damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
													}
												}
											//	if(invoiceNumber.contains("#")) {
													if(totalQuantity != 0){
														$.ajax({type: "POST",
															url:'salesReturn.json', 
															data:  'action=get-quantity-sold-for-approvals-cr&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName+'&totalQuantity='+totalQuantity+'&invoiceNo='+invoicenumber,
															async : false,
															success: function(data){
															  qtySold = data.result.data; 
														 }
														  });
													}
										//		}
											});
						     			   if(resultMessage != undefined){
												 $('#sales-return-approval-decline-dialog').dialog('close');
						     			  			$('#sales-return-container').html(''+resultMessage +'');
						     						$('#sales-return-dialog').dialog({
										    			height: 200,
										    			width : 500,
										    			overflow :'hidden',
										    			buttons: {
										    				OK : function(){
										    					$(this).dialog('close');
										    				}
										    			},
													});
						     						result = false;
						     						return;
											 }else //if(invoiceNumber.contains("#")) {
												 if(parseInt(totalQuantity) > parseInt(qtySold)) {
													 $('#sales-return-CR-approval-decline-dialog').dialog('close');
													//	$('#sales-return-dialog').dialog("close");
							     			  			$('#sales-return-container').html('You Can Not Exceed Sold Quantity('+qtySold +')');
							     						$('#sales-return-dialog').dialog({
											    			height: 200,
											    			width : 500,
											    			overflow :'hidden',
											    			buttons: {
											    				OK : function(){
											    					$(this).dialog('close');
											    				}
											    			},
														});
							     						result = false;
							     						return;
													  }else{
														  if(result != false){
								     			  		var statusParameter='Approved';
									    				$.post('salesReturnCr.json', 'salesReturnCRId='+id+'&action=approve-sales-return-cr'+'&status='+statusParameter+'&listOfSalesReturnObjects='+approvedListOfObjects+'&totalQuantity='+totalQuantity,
									    						 function(obj) {
									    					DashboardSalesReturnCRHandler.initSalesReturnCROnLoad();
									    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
									    					});
									    				$("#sales-return-CR-approval-decline-dialog").dialog('close');
								     			  	}else{
								     			  		$("#sales-return-CR-approval-decline-dialog").dialog('close');
								     			  		$('#sales-return-CR-approval-error-container').html('Please Enter Cost for Return Values');
								     			  		$("#sales-return-CR-approval-error-dialog").dialog({
								     			  			autoOpen: true,
								     			  			title:'Warning',
								     		    			height: 200,
								     		    			width: 350,
								     		    			modal: true,
								     		    			buttons: {
								     		    				Ok: function() {
								     		    					$(this).dialog('close');
								     		    				},
								     		    			},
								     			  		});
															return;
								     			  	}
								     		    }
											// }
						     		    },
						     		   No : function() {
						     			  $("#sales-return-CR-approval-decline-dialog").dialog('close');
							     	   }
						     		 },
				            	};
				            $("#sales-return-CR-approval-decline-dialog").dialog(dialogOpts);
	    				},
	    				//Decline for Sales Return Change Request in Txn CR
	    		      Decline: function() {
	    		    	  //var thisButton = $(this);
		    				 $("#sales-return-CR-approval-decline-container").html('Are You Sure Want To Decline Sales Return Change Request?');
					            var dialogOpts = {
						     		   height : 200,
						     		   width : 500,
					            		buttons : {
							     		    Yes : function() {
							     		    	var declinedListOfObjects="";
							     		    	var statusParameter='Decline';
							    		    	  $.post('salesReturnCr.json', 'salesReturnCRId='+id+'&action=approve-sales-return-cr'+'&status='+statusParameter+'&listOfSalesReturnObjects='+declinedListOfObjects,
								    						 function(obj) {
							    		    		  DashboardSalesReturnCRHandler.initSalesReturnCROnLoad();
								    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
								    					});
							    				$("#sales-return-CR-approval-decline-dialog").dialog('close');
							     		    },
							     		   No : function() {
							     			  $("#sales-return-CR-approval-decline-dialog").dialog('close');
								     	   }
							     		 },
					            	};
					            $("#sales-return-CR-approval-decline-dialog").dialog(dialogOpts);
	    		          },
	    		        Cancel: function(){
	    		        	//var thisButton = $(this);
	    		        	$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
	    		        	$('#sales-return-change-request-dashboard-view-container').html('');  
	    		        }   
	    			},
	    			close: function() {
	    				$('#sales-return-change-request-dashboard-view-container').html('');
	    			}
	    		});
	        	$('#change-request-salesreturn-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		var salesReturnId = $(this).attr('align');
	        		$.post('salesReturnCr.json', 'action=get-sales-return-id'+'&invoiceNumber='+invoiceNumber+'&salesReturnId='+salesReturnId,
	        				function(obj){
	        					var salesReturnId = obj.result.data;
	        					if(salesReturnId != 0){
	        						 $.post('my-sales/transactions/change-transactions/sales_return_change_transaction_previous_cr_view.jsp','id=' + salesReturnId,
	        						           function(data) {
	        						            $('#sales-return-container').html(data);
	        						            var dialogOpts = {
		  	        						     		   height : 420,
		  	        						     		   width : 1030,
		        						            		buttons : {
		    	        						     		    Close : function() {
		    	        						     		     $("#sales-return-dialog").dialog('close');
		    	        						     		     $('#sales-return-container').html('');
		    	        						     		    }
		    	        						     		 },
		        						            	};
		        						            $("#sales-return-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        	});
	        });
});
var DashboardDayBookCRHandler ={
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		initDayBookCROnLoad: function(){
			var paramString='action=search-day-book-change-request-dashboard';
			$.post('dayBookCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-day-book-change-request-results-list').html('');
						if(data != undefined ) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashboardDayBookCRHandler.formatDate(data[loop].createdOn);
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="day-book-change-request-row"  align="'+data[loop].id+'" style="height: 50px;">';
								} else {
									rowstr = '<a href="#"><div id="day-book-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 50px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1">'+
								'<div class="result-body">' +
					   			'<div class="result-title">'+ data[loop].salesExecutive + '</div>' +
					   			'<div class="result-body">' +'<span class="property">'+Msg.DAY_BOOK_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + dateFormat + '</span>' +
					   			'</div>' +
					   			'</div>' +
					   			'</div>' +
								'</a>';
						$('#search-day-book-change-request-results-list').append(rowstr);
					};
					$('#search-day-book-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-day-book-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Day Book CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
};
$('#day-book-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/day_book_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#day-book-dashboard-view-container').html(data);  
	        	$("#day-book-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 600,
	    			width: 900,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				//var thisButton = $(this);
	    				 $("#daybook-cr-approval-decline-container").html('Are You Sure Want To Approved Day Book Change Request?');
				            var dialogOpts = {
					     		   height : 200,
					     		   width : 500,
				            		buttons : {
						     		    Yes : function() {
						     		    	var statusParameter='Approved';
						    				$.post('dayBookCr.json', 'dayBookCrId='+id+'&action=approve-day-book-cr'+'&status='+statusParameter,
						    						 function(obj) {
						    					DashboardDayBookCRHandler.initDayBookCROnLoad();
						    						$("#day-book-dashboard-view-dialog").dialog('close');
						    					});
						    				$("#daybook-cr-approval-decline-dialog").dialog('close');
						     		    },
						     		   No : function() {
						     			  $("#daybook-cr-approval-decline-dialog").dialog('close');
							     	   }
						     		 },
				            	};
				            $("#daybook-cr-approval-decline-dialog").dialog(dialogOpts);
	    				},
	    		      Decline: function() {
	    		    	  $("#daybook-cr-approval-decline-container").html('Are You Sure Want To Decline Day Book Change Request?');
				            var dialogOpts = {
					     		   height : 200,
					     		   width : 500,
				            		buttons : {
						     		    Yes : function() {
						     		    	var statusParameter='Decline';
						    		    	  $.post('dayBookCr.json', 'dayBookCrId='+id+'&action=approve-day-book-cr'+'&status='+statusParameter,
							    						 function(obj) {
						    		    		  DashboardDayBookCRHandler.initDayBookCROnLoad();
							    						$("#day-book-dashboard-view-dialog").dialog('close');
							    					});
						    				$("#daybook-cr-approval-decline-dialog").dialog('close');
						     		    },
						     		   No : function() {
						     			  $("#daybook-cr-approval-decline-dialog").dialog('close');
							     	   }
						     		 },
				            	};
				            $("#daybook-cr-approval-decline-dialog").dialog(dialogOpts);
	    		          },
	    			 Cancel: function(){
		    		        	//var thisButton = $(this);
		    		        	$("#day-book-dashboard-view-dialog").dialog('close');
		    		        }   
	    			},
	    			close: function() {
	    				$('#day-book-dashboard-view-container').html('');
	    			}
	    		});
	        	//onclick of daybook number displaying day book view page before Day Book CR
	        	$('#change-request-daybook-number').click(function(){
	        		var dayBookNo=$(this).attr('class');
	        		var dayBookId=$(this).attr('align');
	        		$.post('dayBookCr.json', 'action=get-daybook-id'+'&dayBookNumber='+dayBookNo+'&dayBookId='+dayBookId,
	        				function(obj){
	        					var previousDayBookId = obj.result.data;
	        					if(previousDayBookId != 0){
	        						 $.post('my-sales/transactions/change-transactions/day_book_view_transaction_cr.jsp','id=' + previousDayBookId,
	        						           function(data) {
	        						            $('#day-book-container').html(data);
	        						            var dialogOpts = {
	              						     		   height : 650,
	              						     		   width : 900,
	        						            		buttons : {
	                						     		    Close : function() {
	                						     		     $(this).dialog('close');
	                						     		    }
	                						     		 },
	        						            	};
	        						            $("#day-book-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        });
	});
	
});
var DashboardJournalCRHandler ={
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		initJournalsCROnLoad: function(){
			var paramString='action=get-all-journals-CR-for-dashboard';
			$.post('journalCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-journal-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashboardJournalCRHandler.formatDate(data[loop].createdOn);
								var amount=data[loop].amount;
								if(amount.indexOf(",1") !== -1){
								var formatAmount=amount.replace(",1","");
								var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}else{
									var formatAmount=amount.replace(",0","");
									var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="journal-change-request-row" align="'+data[loop].id+'" style="height: 80px;">';
								} else {
									rowstr = '<a href="#"><div id="journal-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 80px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1" style="width: 340px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].businessName + '</div>';
								rowstr +='<span class="property">'+Msg.JOURNAL_TYPE+':'+' </span><span class="property-value">' + data[loop].journalType + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_CREATED_DATE +':'+' </span><span class="property-value">' + dateFormat + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_AMOUNT +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span><br/>';
								rowstr +='<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#search-journal-change-request-results-list').append(rowstr);
					};
					$('#search-journal-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-journal-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Journal CR Available.</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
};
$('#journal-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/journal_dashboard_cr_view.jsp', 'id='+id,
	        function(data){
	        	$('#journal-dashboard-cr-view-container').html(data);  
	        	$("#journal-dashboard-cr-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 450,
	    			width: 1040,
	    			modal: true,
	    			buttons: {
	    				Approved: function() {
		    				 $("#journal-cr-approval-decline-container").html('Are You Sure Want To Approved Journal Change Request?');
					            var dialogOpts = {
						     		   height : 200,
						     		   width : 500,
					            		buttons : {
							     		    Yes : function() {
							     		    	var statusParameter='Approved';
							    				$.post('journalCr.json', 'journalId='+id+'&action=approve-journal-cr'+'&status='+statusParameter,
							    						 function(obj) {
							    					DashboardJournalCRHandler.initJournalsCROnLoad();
							    						$("#journal-dashboard-cr-view-dialog").dialog('close');
							    					});
							    				$("#journal-cr-approval-decline-dialog").dialog('close');
							     		    },
							     		   No : function() {
							     			  $("#journal-cr-approval-decline-dialog").dialog('close');
								     	   }
							     		 },
					            	};
					            $("#journal-cr-approval-decline-dialog").dialog(dialogOpts);
		    				},
		    		      Decline: function() {
		    		    	  $("#journal-cr-approval-decline-container").html('Are You Sure Want To Approved Journal Change Request?');
					            var dialogOpts = {
						     		   height : 200,
						     		   width : 500,
					            		buttons : {
							     		    Yes : function() {
							     		    	var statusParameter='Decline';
							    		    	  $.post('journalCr.json', 'journalId='+id+'&action=approve-journal-cr'+'&status='+statusParameter,
								    						 function(obj) {
							    		    		  DashboardJournalCRHandler.initJournalsCROnLoad();
								    						$("#journal-dashboard-cr-view-dialog").dialog('close');
								    					});
							    				$("#journal-cr-approval-decline-dialog").dialog('close');
							     		    },
							     		   No : function() {
							     			  $("#journal-cr-approval-decline-dialog").dialog('close');
								     	   }
							     		 },
					            	};
					            $("#journal-cr-approval-decline-dialog").dialog(dialogOpts);
		    		          },
		    		          Cancel: function(){
		    		        	  $("#journal-dashboard-cr-view-dialog").dialog('close');
		    		          }
	    			},
	    			close: function() {
	    				$('#journal-dashboard-cr-view-container').html('');
	    			}
	    		});
	        	$('#change-request-journal-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		var journalId=$(this).attr('align');
	        		if(journalId != 0){
	        		$.post('journalCr.json', 'action=get-journal-id'+'&invoiceNumber='+invoiceNumber+'&journalId='+journalId,
	        				function(obj){
	        					var journalId = obj.result.data;
	        					if(journalId != 0){
	        						 $.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp','id=' + journalId,
	        						           function(data) {
	        						            $('#journal-container').html(data);
	        						            var dialogOpts = {
	  	        						     		   height : 400,
	  	        						     		   width : 1020,
	        						            		buttons : {
	    	        						     		    Close : function() {
	    	        						     		     $(this).dialog('close');
	    	        						     		    }
	    	        						     		 },
	        						            	};
	        						            $("#journal-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        		}else{
	        			//not able to fetch original invoice based on invoice number in CR'd invoice
	        		}
	        	});
	        });
	 });
