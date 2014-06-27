var DashboardSalesReturnApprovalsHandler ={
		//sales Return Approval onload
		initSalesReturnOnLoad: function(){
			var paramString='action=search-sales-return-dashboard';
			$.post('salesReturn.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-sales-returns-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="sales-return-row"  align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 60px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="sales-return-row" class="green-result-row search-row-results" align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 60px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr = rowstr +'<div class="green-result-col-1 search-row-results" style="width: 340px;">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].createdBy + '</div>' +
								'<span class="property">'+Msg.CUSTOMER_BUSINESS_NAME+':'+' </span><span class="property-value">' + data[loop].businessName + '</span><br/>' +
								'<span class="property">'+Msg.SALES_RETURNS_CREATED_DATE+':'+' </span><span class="property-value">' + data[loop].date + '</span><br/>' +
								'<span class="property">'+Msg.CUSTOMER_INVOICE_NAME +':'+' </span><span class="property-value">' + data[loop].invoiceName + '</span>' +
								'</div>' +
								'</div>'+
								'</a>';
						$('#search-sales-returns-results-list').append(rowstr);
					};
					$('#search-sales-returns-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-sales-returns-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Sales Return Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
};
$('#sales-return-row').live('click',function(){
	var id = $(this).attr('align');
	var resultList='null';
	var invoiceNumber = $(this).attr('comp');
	$('#sales-return-change-request-dashboard-view-container').data('');
	$.post('salesReturnCr.json', 'action=check-Transaction-Sales-Return'+'&invoiceNumber='+invoiceNumber,
	        function(data){
		var existsId=data.result.data;
		//if Pending transaction sales return is present in Transaction CR for Aproval sales return based on invoiceNo.
	    if(existsId != 0){
	    	  $('#check-transaction-sales-return-view-container').html('There is an Transaction CR related to the Invoice.');
			  $("#check-transaction-sales-return-view-dialog").dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						//if click open Transaction Sales Return CR dialog should open.
						Open: function() {
							$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_results_view.jsp', 'id='+existsId,
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
												$(this).find('#totalCost').html((totalResalableCost + totalDamagedCost).toFixed(2));
											}
											var totalCost =(totalResalableCost + totalDamagedCost);
											if(isNaN(totalCost)) {
										       	  totalCost = 0.00;
										       }
										      $(this).find('#totalCost').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
										      sum += totalCost;
										      $('#grandTotal').html(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
							        	});
							        	$("table").live("blur",function(){
							        		var sum = 0;
							        		$('tr').each(function(index, value) {
							        			if(index == 0){
					     		        			return;
					     		        			}
												var productName = $(this).find('#productName').html();
												var batchNumber =$(this).find('#batchNumber').html();
												var damaged =currencyHandler.convertStringPatternToFloat($(this).find('#damaged').html());
												var resalable =currencyHandler.convertStringPatternToFloat($(this).find('#resalable').html());
												var totalQuantity = $(this).find('#totalQuantity').html();
												var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
												var damagedCost =currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
												var totalResalableCost = resalableCost * resalable;
												var totalDamagedCost = damagedCost * damaged;
												$(this).find('#totalCost').html((totalResalableCost + totalDamagedCost).toFixed(2));
												var totalCost =(totalResalableCost + totalDamagedCost);
												if(isNaN(totalCost)) {
											       	  totalCost = 0.00;
											       }
											      $(this).find('#totalCost').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
											      sum += totalCost;
											      $('#grandTotal').html(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
								        	});
							        	})
							        	
							        	//click on invoice number display previous Sales Return
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
							        	//end of invoice number click
							        	$("#sales-return-change-request-dashboard-view-dialog").dialog({
							    			autoOpen: true,
							    			height: 485,
							    			width: 1000,
							    			modal: true,
							    			buttons: {
							    				//for txn Sales return CR clicked respective Approval SR dialog should opened.
							    			Approved: function() {
							    				var thisButton = $(this);
							    				$('#sales-return-approval-decline-container').html('Are You Sure Want To Approve Sales Return Change Request ?');
							    				 var dialogOpts = {
		  	        						     		   height : 200,
		  	        						     		   width : 500,
		  	        						     		   title:'Sales Return Approval',
		        						            		buttons : {
		        						            			//onclick of Yes Sales Return CR Approved and respective Approval Sales Return should opened.
		    	        						     		   Yes : function() {
		    	        						     			   var qtyList = []; 
		    	        						     			  var listOfObjects='';
		    	        						     			  var qtySold;
		    	        						     			 var totalQuantity;
		    	        						     			 var result;
		    	        						     			var businessName;
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
		    	      												totalQuantity = $(this).find('#totalQuantity').html();
		    	      												var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
		    	      												var damagedCost =currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
		    	      												var totalResalableCost = resalableCost * resalable;
		    	      												var totalDamagedCost = damagedCost * damaged;
		    	      												 businessName = $('#businessName').text();
		    	      												if($(this).find('#totalCost').html() != null){
		    	      													var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#totalCost').html());
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
		    	      														listOfObjects +=damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
		    	      													} else {
		    	      														listOfObjects +=','+damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
		    	      													}
		    	    												}
		    	    												if(totalQuantity != 0){
		    	    													$.ajax({type: "POST",
			    															url:'salesReturn.json', 
			    															data:  'action=get-quantity-sold-for-approvals&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName+'&salesReturnId='+id,
			    															async : false,
			    															success: function(data){
			    															  qtySold = data.result.data; 
			    														 }
			    														  });
		    	    												}
		    	    												qtyList.push(qtySold);
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
															 }else if(parseInt(totalQuantity) > parseInt(qtySold)) {
																$('#sales-return-approval-decline-dialog').dialog('close');
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
		    	      						     			  	var statusParameter='APPROVED';
											    				$.post('salesReturnCr.json', 'salesReturnCRId='+existsId+'&action=approve-sales-return-cr'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects,
											    						 function(obj) {
											    					//DashboardSalesReturnApprovalsHandler.initSalesReturnCROnLoad();
											    					DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
											    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
											    					});
											    				$("#sales-return-approval-decline-dialog").dialog('close');
		    	      						     			  	}else{
		    	      						     			  	$('#sales-return-approval-decline-container').html('Please Enter Cost for Return Values');
		    							     			  		$("#sales-return-approval-decline-dialog").dialog({
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
		    	        						     		    },
		    	        						     		    //onclick of No Approved pop-up will close
		    	        						     		   No : function() {
		    	        						     			  $("#sales-return-approval-decline-dialog").dialog('close');
			    	        						     		 }
		    	        						     		 },
		    	        						     		 
		        						            	};
		        						            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
							    			},
							    				//if decline click Approval SR dialog should opened.
							    		      Decline: function() {
							    		    	  var thisButton = $(this);
								    				$('#sales-return-approval-decline-container').html('Are You Sure Want To Decline Sales Return Change Request ?');
								    				 var dialogOpts = {
								    						   height : 200,
			  	        						     		   width : 500,
			        						            		buttons : {
			        						            			//onclick of Yes Sales Return CR Declined and respective Approval Sales Return should opened.
			        						            			Yes: function() {
			        						            				var listOfObjects="";
																		var statusParameter='DECLINE';
													    		    	  $.post('salesReturnCr.json', 'salesReturnCRId='+existsId+'&action=approve-sales-return-cr'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects,
														    						 function(obj) {
														    					    //after CR decline display respective Approval SR page to do further operation
														    					    $.post('salesReturnCr.json', 'action=get-sales-return-status&id='+id,
															    					        function(data){
														    					    	var approvalSalesReturn=data.result.data;
														    					    	if(approvalSalesReturn.status == 'PENDING'){
														    					    $.post('my-sales/transactions/change-transactions/sales_return_approval_page.jsp', 'id='+id,
															    					        function(data){
														    					    	$('#sales-return-dashboard-view-container').html(data); 
														    				        	$("#sales-return-dashboard-view-dialog").dialog({
														    				    			autoOpen: true,
														    				    			height: 485,
														    				    			width: 1000,
														    				    			modal: true,
														    				    			buttons: {
														    				    				Approved: function() {
														    					    				$('#sales-return-approval-decline-container').html('Are You Sure Want To Approve Sales Return ?');
														    					    				var dialogOpts = {
														    										     		   height : 200,
														    										     		   width : 500,
														    										     		   title:'Sales Returns',
														    								            	   buttons : {
														    										     		   Yes : function() {
														    				  								     	   var result;
														    										     			  var listOfObjects='';
														    										     			  var qtySold;
														    										     			  var totalQuantity;
														    										     			  var resultMessage;
														    										     			 var grandTotal =  currencyHandler.convertStringPatternToFloat( $('#grandTotal').html());
														    										     			       $('tr').each(function(index, value) {
														    										     			    	  if(index == 0){
														    										     		        			return;
														    										     		        			}
														    																var productName = $(this).find('#productName').html();
														    																var batchNumber =$(this).find('#batchNumber').html();
														    																var damaged =currencyHandler.convertStringPatternToNumber($(this).find('#damaged').html());
														    																var resalable =currencyHandler.convertStringPatternToNumber($(this).find('#resalable').html());
														    																 totalQuantity = currencyHandler.convertStringPatternToNumber($(this).find('#totalQuantity').html());
														    																var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
														    																var businessName = $('#businessName').text();
														    																var damagedCost =currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
														    																var totalResalableCost = resalableCost * resalable;
														    																var totalDamagedCost = damagedCost * damaged;
														    																if($(this).find('#totalCost').html() != null){
																    	      													var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#totalCost').html());
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
														    												     			  		result = false;
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
														    																
														    																if(totalQuantity != 0){
														    																	$.ajax({type: "POST",
															    																	url:'salesReturn.json', 
															    																	data:  'action=get-quantity-sold-for-approvals&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName,
															    																	async : false,
															    																	success: function(data){
															    																	  qtySold = data.result.data; 
															    																 }
															    																  });	
														    																}
														    																
														    																//alert(parseInt(totalQuantity) > parseInt(qtySold))
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
														    																 }else if(parseInt(totalQuantity) > parseInt(qtySold)) {
														    																	$('#sales-return-approval-decline-dialog').dialog('close');
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
														    																  }
														    																if(result != false){
														    																	if(index == 0) {
														    																		listOfObjects +=damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
														    																	} else {
														    																		listOfObjects +=','+damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
														    																	}
														    																}
														    														     				
														    										     			  	});
														    										     			       if(result != false){
														    										     			    		var statusParameter='APPROVED';
														    												    				$.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects,
														    												    						 function(obj) {
														    												    					DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
														    												    					    $("#sales-return-approval-decline-dialog").dialog('close');
														    												    						$("#sales-return-dashboard-view-dialog").dialog('close');
														    												    				});
														    												    				 $("#sales-return-approval-decline-dialog").dialog('close');
														    												    				 $("#sales-return-dashboard-view-dialog").dialog('close');
														    										     			    	   
														    										     			       }else{
														    										     			    	  $('#sales-return-approval-decline-container').html('Please Enter Cost for Return Values');
														    											     			  		$("#sales-return-approval-decline-dialog").dialog({
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
														    										     		  },
														    										     		 No : function() {
														    										     			  $("#sales-return-approval-decline-dialog").dialog('close');
														    				    						     		 }
														    								            	   },
														    										     			}
														    										     		  
														    								            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
														    					    				},
														    					    				 Decline: function() {
														    						    		    	  $('#sales-return-approval-decline-container').html('Are You Sure Want To Decline Sales Return ?');
														    							    				var dialogOpts = {
														    												     		   height : 200,
														    												     		   width : 500,
														    										            	   buttons : {
														    												     		   Yes : function() {
														    												     			  var listOfObjects="";
														    												     			 var statusParameter='DECLINE';
														    											    		    	  $.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects,
														    												    						 function(obj) {
														    											    		    		  DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
														    												    					    $("#sales-return-approval-decline-dialog").dialog('close');
														    												    						$("#sales-return-dashboard-view-dialog").dialog('close');
														    												    					});
														    											    		    	  $("#sales-return-approval-decline-dialog").dialog('close');
														    											    		    	  $("#sales-return-dashboard-view-dialog").dialog('close');
														    												     		   },
														    												     		   No : function() {
														    												     			  $("#sales-return-approval-decline-dialog").dialog('close');
														    						    						     		 }
														    												     		 },
														    										            	};
														    										            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
														    						    		          },
														    				    		      Cancel: function() {
														    				    		    	  $("#sales-return-dashboard-view-dialog").dialog('close');
														    								      }
														    				    			},
														    				    			close: function() {
														    				    				$('#sales-return-dashboard-view-container').html('');
														    				    			}
														    				    		});
															    					        });
														    					    	}else{
														    					    	
														    					    	}
														    					    });
														    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
														    					});
													    		    	  //DashboardSalesReturnApprovalsHandler.initSalesReturnCROnLoad();
													    		    	  $("#sales-return-approval-decline-dialog").dialog('close');
																	},
																	//Decline pop-up should closed.
														            No: function() {
														            	$("#sales-return-approval-decline-dialog").dialog('close');
															      }
			    	        						     		 },
			        						            	};
			        						            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
							    		          },
							    		          
							    		          //cancel button
							    				Cancel: function(){
							    					var thisButton = $(this);
							    					 $("#sales-return-change-request-dashboard-view-dialog").dialog('close');
							    				    }
							    			},
							    			close: function() {
							    				$('#sales-return-change-request-dashboard-view-container').html('');
							    			}
							    		});
							        });
							$("#check-transaction-sales-return-view-dialog").dialog('close');
						},
						//if cancel redirect to Approval dashboard page.
			            Cancel: function() {
			            	$('.dashboard-page-container').load('dashboard/approvals/dashboard_main_approvals_view.jsp');
							$("#check-transaction-sales-return-view-dialog").dialog('close');
				      }
					},
					close: function() {
						$(this).dialog('close');
					}
	          });
	    }else{
	    	var cost;
	    	var grandTotal;
	    	  var qtyList = []; 
	    	//if not, display Approval Sales return and Approved and declined
	    	$.post('my-sales/transactions/change-transactions/sales_return_approval_page.jsp', 'id='+id,
	        function(data){
	        	$('#sales-return-dashboard-view-container').html(data);  
	        	$('table').live("blur",function(){
	        		var sum = 0;
	        		$('tr').each(function(index, value) {
	        			if(index == 0){
	        			return;
	        			}
						var productName = $(this).find('#productName').html();
						var batchNumber =$(this).find('#batchNumber').html();
						var damaged = currencyHandler.convertStringPatternToNumber($(this).find('#damaged').html());
						var resalable = currencyHandler.convertStringPatternToNumber($(this).find('#resalable').html());
						var resalableCost = currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
						if(resalableCost == 0) {
							$(this).find('#resalableCost').val("0.00");
						}
						var damagedCost = currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
						if(damagedCost == 0) {
							$(this).find('#damagedCost').val("0.00");
						}
						var totalResalableCost = resalableCost * resalable;
						var totalDamagedCost = damagedCost * damaged;
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
							$(this).find('#totalCost').html((totalResalableCost + totalDamagedCost).toFixed(2));
						}
						var totalCost =(totalResalableCost + totalDamagedCost);
						if(isNaN(totalCost)) {
					       	  totalCost = 0.00;
					       }
					      //$(this).find('#totalCost').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
					      sum += totalCost;
					      $('#grandTotal').html(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
		        	});
	        	});
	        	$("#sales-return-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 485,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    				Approved: function() {
		    				$('#sales-return-approval-decline-container').html('Are You Sure Want To Approve Sales Return ?');
		    				var dialogOpts = {
							     		   height : 200,
							     		   width : 500,
							     		   title:'Sales Returns',
					            	   buttons : {
							     		   Yes : function() {
							     			   var result;
							     			   var resultSold;
							     			  var listOfObjects='';
							     			  var qtySold;
							     			  var resultMessage ;
							     			  var totalQuantity;
							     			       $('tr').each(function(index, value) {
							     			    	  if(index == 0){
							     		        			return;
							     		        			}
							     			    	 var businessName = $('#businessName').text();
													var productName = $(this).find('#productName').html();
													var batchNumber =$(this).find('#batchNumber').html();
													var damaged =currencyHandler.convertStringPatternToNumber($(this).find('#damaged').html());
													var resalable =currencyHandler.convertStringPatternToNumber($(this).find('#resalable').html());
													 totalQuantity = currencyHandler.convertStringPatternToNumber($(this).find('#totalQuantity').html());
													var resalableCost =currencyHandler.convertStringPatternToFloat($(this).find('#resalableCost').val());
													var damagedCost =currencyHandler.convertStringPatternToFloat($(this).find('#damagedCost').val());
													var totalResalableCost = resalableCost * resalable;
													var totalDamagedCost = damagedCost * damaged;
													var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#totalCost').html());
													var grandTotal =  currencyHandler.convertStringPatternToFloat( $('#grandTotal').html());
													if(totalQuantity != 0){
														$.ajax({type: "POST",
															url:'salesReturn.json', 
															data:  'action=get-quantity-sold-for-approvals&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName+'&salesReturnId='+id,
															async : false,
															success: function(data){
															  qtySold = data.result.data; 
															  resultMessage = data.result.message;
														 }
														  });
													}
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
													 }else if(parseInt(totalQuantity) > parseInt(qtySold)) {
														$('#sales-return-approval-decline-dialog').dialog('close');
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
									     			  		result = false;
													    return false;
													}else if(damaged != "" || resalable != ""){
														if(damaged == ""){
															if(resalableCost == ""){
																resalableCost = 0;
															}
														}else if(resalable == ""){
															if(damagedCost == ""){
																damagedCost = 0;
															}
														}
													}
														if(index == 0) {
															listOfObjects +=damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
														} else {
															listOfObjects +=','+damaged+'|'+resalable+'|'+damagedCost+'|'+resalableCost+'|'+totalResalableCost+'|'+totalDamagedCost+'|'+totalQuantity+'|'+totalCost+'|'+batchNumber+'|'+productName+'|'+grandTotal
														}
							     			  	});
							     			       grandTotal = currencyHandler.convertStringPatternToFloat( $('#grandTotal').html());
							     			      if(result != false){
							     			       if(parseFloat(grandTotal) != 0 && resultSold != false){
							     			    		  var statusParameter='APPROVED';
										    				$.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects, function(obj) {
										    					DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
										    					    $("#sales-return-approval-decline-dialog").dialog('close');
										    						$("#sales-return-dashboard-view-dialog").dialog('close');
										    				});
										    				 $("#sales-return-approval-decline-dialog").dialog('close');
										    				 $("#sales-return-dashboard-view-dialog").dialog('close');
							     			    	   }
							     			       else{
							     			    	  $('#sales-return-approval-decline-container').html('Please Enter Cost for Return Values');
								     			  		$("#sales-return-approval-decline-dialog").dialog({
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
							     			    		  },
							     		 No : function() {
							     			  $("#sales-return-approval-decline-dialog").dialog('close');
	    						     		 }
					            	   },
							     			}
							     		  
					            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
		    				},
	    		      Decline: function() {
	    		    	  $('#sales-return-approval-decline-container').html('Are You Sure Want To Decline Sales Return ?');
		    				var dialogOpts = {
							     		   height : 200,
							     		   width : 500,
					            	   buttons : {
							     		   Yes : function() {
							     			  var listOfObjects="";
							     			 var statusParameter='DECLINE';
						    		    	  $.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter+'&listOfSalesReturnObjects='+listOfObjects, function(obj) {
						    		    		  DashboardSalesReturnApprovalsHandler.initSalesReturnOnLoad();
							    					    $("#sales-return-approval-decline-dialog").dialog('close');
							    						$("#sales-return-dashboard-view-dialog").dialog('close');
							    					});
						    		    	  $("#sales-return-approval-decline-dialog").dialog('close');
						    		    	  $("#sales-return-dashboard-view-dialog").dialog('close');
							     		   },
							     		   No : function() {
							     			  $("#sales-return-approval-decline-dialog").dialog('close');
	    						     		 }
							     		 },
					            	};
					            $("#sales-return-approval-decline-dialog").dialog(dialogOpts);
	    		          },
	    		      Cancel: function() {
	    		    	  $("#sales-return-dashboard-view-dialog").dialog('close');
					      }
	    			},
	    			close: function() {
	    				$('#sales-return-dashboard-view-container').html('');
	    			}
	    		});
	        });
	    }
	  });
	 });
var DashboardJournalApprovalsHandler={
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		//Journals Approvals Onload
		initJournalsOnLoad: function(){
			var paramString='action=get-all-journals-for-dashboard';
			$.post('journal.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-journal-add-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashboardJournalApprovalsHandler.formatDate(data[loop].createdOn);
								var amount=data[loop].amount;
								if(amount.indexOf(",1") !== -1){
								var formatAmount=amount.replace(",1","");
								var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}else{
									var formatAmount=amount.replace(",0","");
									var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="journal-add-row" amount="'+data[loop].amount+'" align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 70px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="journal-add-row" class="green-result-row search-row-results" align="'+data[loop].id+'" amount="'+data[loop].amount+'" comp="'+data[loop].invoiceNo+'"  style="height: 70px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1 search-row-results" style="width: 210px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].businessName + '</div>';
								rowstr +='<span class="property">'+Msg.JOURNAL_TYPE+':'+' </span><span class="property-value">' + data[loop].journalType + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_CREATED_DATE +':'+' </span><span class="property-value">' + dateFormat + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_AMOUNT +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#search-journal-add-results-list').append(rowstr);
					};
					$('#search-journal-add-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-journal-add-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Journals Available.</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
};
$('#journal-add-row').live('click',function(){
	var id = $(this).attr('align');
	var invoiceNumber=$(this).attr('comp');
	var amount=$(this).attr('amount');
	$.post('journalCr.json', 'action=check-Transaction-journal'+'&invoiceNumber='+invoiceNumber,
	        function(data){
		var existsId=data.result.data;
		//if Pending transaction journal is present in Transaction CR for Aproval journal based on invoiceNo.
	    if(existsId != 0){
	    	 $('#check-transaction-journal-view-container').html('There is an Transaction CR related to the Invoice.');
			  $("#check-transaction-journal-view-dialog").dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						//if click open Transaction journal CR dialog should open.
						Open: function() {
							$.post('my-sales/transactions/change-transactions/journal_dashboard_cr_view.jsp', 'id='+existsId,
							        function(data){
							        	$('#journal-dashboard-cr-view-container').html(data);  
							        	//onclick of journal invoice number previous journal should opened
							        	$('#change-request-journal-Invoice-number').click(function(){
							        		var invoiceNumber=$(this).attr('class');
							        		var journalId=$(this).attr('align');
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
							        	});
							        	//end of journal invoice number click
							        	$("#journal-dashboard-cr-view-dialog").dialog({
							    			autoOpen: true,
							    			height: 400,
							    			width: 1040,
							    			modal: true,
							    			buttons: {
							    				//for txn journal CR clicked respective Approval journal dialog should opened.
							    				Approved: function() {
								    				var thisButton = $(this);
								    				$('#journal-approval-decline-container').html('Are You Sure Want To Approved Journal Change Request ?');
								    				 var dialogOpts = {
			  	        						     		   height : 200,
			  	        						     		   width : 500,
			        						            		buttons : {
			        						            			//onclick of Yes Journal CR Approved and respective Approval Sales Return should opened.
			    	        						     		   Yes : function() {
			    	        						     			  var statusParameter='Approved';
			    	  							    				$.post('journalCr.json', 'journalId='+existsId+'&action=approve-journal-cr'+'&status='+statusParameter,
			    	  							    						 function(obj) {
			    	  							    					   // DashbookHandler.initJournalsCROnLoad();
			    	  							    					DashboardJournalApprovalsHandler.initJournalsOnLoad();
												    					    $("#journal-dashboard-cr-view-dialog").dialog('close');
												    					    	});
													    				$("#journal-approval-decline-dialog").dialog('close');
													    				 $("#journal-dashboard-cr-view-dialog").dialog('close');
			    	        						     		    },
			    	        						     		    //onclick of No Approved pop-up will close
			    	        						     		   No : function() {
			    	        						     			  $("#journal-approval-decline-dialog").dialog('close');
				    	        						     		 }
			    	        						     		 },
			    	        						     		 
			        						            	};
			        						            $("#journal-approval-decline-dialog").dialog(dialogOpts);
								    			},
								    			//for txn journal CR  decline clicked respective Approval journal dialog should opened.
								    			 Decline: function() {
									    				$('#journal-approval-decline-container').html('Are You Sure Want To Decline Journal Change Request ?');
									    				 var dialogOpts = {
									    						   height : 200,
				  	        						     		   width : 500,
				        						            		buttons : {
				        						            			//onclick of Yes Sales Return CR Declined and respective Approval Sales Return should opened.
				        						            			Yes: function() {
				        						            				var statusParameter='Decline';
				        						            				$.post('journalCr.json', 'journalId='+existsId+'&action=approve-journal-cr'+'&status='+statusParameter,
				       								    						 function(obj) {
				       								    					    //if CR decline display respective Approval page
				       								    					 $.post('journalCr.json', 'action=get-journal-status&id='+id,
														    					        function(data){
													    					    	var approvalJournal=data.result.data;
													    					    	if(approvalJournal.status == 'PENDING'){
				       								    					    $.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp', 'id='+id,
				       								    				    	        function(data){
				       								    				    	        	$('#journal-dashboard-view-container').html(data);  
				       								    				    	        	$("#journal-dashboard-view-dialog").dialog({
				       								    				    	    			autoOpen: true,
				       								    				    	    			height: 400,
				       								    				    	    			width: 1040,
				       								    				    	    			modal: true,
				       								    				    	    			buttons: {
															    				    			Approved: function() {
															    				    				var thisButton = $(this);
															    				    				$('#journal-approval-decline-container').html('Are You Sure Want To Approve Journal ?');
															    				    				var dialogOpts = {
															    									     		   height : 200,
															    									     		   width : 500,
															    							            	   buttons : {
															    									     		   Yes : function() {
															    									     			  var statusParameter='Approved';
															    									     			 $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter+'&amount='+amount,
																	    				    	    						 function(obj) {
															    									     				DashboardJournalApprovalsHandler.initJournalsOnLoad();
																	    				    	    						$("#journal-dashboard-view-dialog").dialog('close');
																	    				    	    					});
															    									     			$("#journal-approval-decline-dialog").dialog('close');
															    									     		   },
															    									     		   No : function() {
															    									     			  $("#journal-approval-decline-dialog").dialog('close');
															    			    						     		 }
															    									     		 },
															    							            	};
															    							            $("#journal-approval-decline-dialog").dialog(dialogOpts);
															    				    				},
															    				    		      Decline: function() {
															    				    		    	  $('#journal-approval-decline-container').html('Are You Sure Want To Decline Journal ?');
															    					    				var dialogOpts = {
															    										     		   height : 200,
															    										     		   width : 500,
															    								            	   buttons : {
															    										     		   Yes : function() {
															    										     			  var statusParameter='Decline';
																		    				    	    		    	  $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter+'&amount='+amount,
																		    				    		    						 function(obj) {
																		    				    	    		    		  DashboardJournalApprovalsHandler.initJournalsOnLoad();
																		    				    		    						$("#journal-dashboard-view-dialog").dialog('close');
																		    				    		    					});
																		    				    	    		    	  $("#journal-approval-decline-dialog").dialog('close');
															    										     		   },
															    										     		   No : function() {
															    										     			  $("#journal-approval-decline-dialog").dialog('close');
															    				    						     		 }
															    										     		 },
															    								            	};
															    								            $("#journal-approval-decline-dialog").dialog(dialogOpts);
															    				    		          },
															    				    		      Cancel: function() {
															    				    		    	  $("#journal-dashboard-view-dialog").dialog('close');
															    								      }
															    				    			},
															    				    			close: function() {
															    				    				$('#journal-dashboard-view-container').html('');
															    				    			}
															    				    		});
																    					        });
													    					    	 }else{
														    					    		//alert("previous SR approved");
														    					    	}
													    					    	DashboardJournalApprovalsHandler.initJournalsOnLoad();
														    					});
				       								    					 //DashbookHandler.initJournalsCROnLoad();
															    			 $("#journal-dashboard-cr-view-dialog").dialog('close');
															    		});
														    		    	  $("#journal-approval-decline-dialog").dialog('close');
														    		    	  $("#journal-dashboard-cr-view-dialog").dialog('close');
																		},
																		//Decline pop-up should closed.
															            No: function() {
															            	$("#journal-approval-decline-dialog").dialog('close');
																      }
				    	        						     		 },
				        						            	};
				        						            $("#journal-approval-decline-dialog").dialog(dialogOpts);
								    		          },
								    		          
								    		          //cancel button
								    				Cancel: function(){
								    					var thisButton = $(this);
								    					$("#journal-dashboard-cr-view-dialog").dialog('close');
								    				    }	
							    			},
							    			close: function() {
							    				$('#journal-dashboard-cr-view-container').html('');
							    			}
							    		});
							        }
							    );
							$("#check-transaction-journal-view-dialog").dialog('close');
						},
						//if cancel redirect to Approval dashboard page.
			            Cancel: function() {
			            	$('.dashboard-page-container').load('dashboard/approvals/dashboard_main_approvals_view.jsp');
							$("#check-transaction-journal-view-dialog").dialog('close');
				      }
					},
					close: function() {
						$(this).dialog('close');
					}
	          });
			//if not, display Approval journal and Approved and declined
	    }else{
	    	$.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp', 'id='+id,
	    	        function(data){
	    	        	$('#journal-dashboard-view-container').html(data);  
	    	        	$("#journal-dashboard-view-dialog").dialog({
	    	    			autoOpen: true,
	    	    			height: 400,
	    	    			width: 1040,
	    	    			modal: true,
	    	    			buttons: {
	    	    				Approved: function() {
	    		    				$('#journal-approval-decline-container').html('Are You Sure Want To Approve Journal ?');
	    		    				var dialogOpts = {
	    							     		   height : 200,
	    							     		   width : 500,
	    					            	   buttons : {
	    							     		   Yes : function() {
	    							     			  var statusParameter='Approved';
	    							     				 $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter+'&amount='+amount,
	    					    	    						 function(obj) {
	    							     					DashboardJournalApprovalsHandler.initJournalsOnLoad();
	    					    	    						$("#journal-dashboard-view-dialog").dialog('close');
	    					    	    					});
	    					    	    				$("#journal-approval-decline-dialog").dialog('close');
	    					    	    				$("#journal-dashboard-view-dialog").dialog('close');
	    					    	    			  },
	    							     		   No : function() {
	    							     			  $("#journal-approval-decline-dialog").dialog('close');
	    	    						     		 }
	    							     		 },
	    					            	};
	    					            $("#journal-approval-decline-dialog").dialog(dialogOpts);
	    		    				},
	    		    		      Decline: function() {
	    		    		    	  $('#journal-approval-decline-container').html('Are You Sure Want To Decline Journal ?');
	    			    				var dialogOpts = {
	    								     		   height : 200,
	    								     		   width : 500,
	    						            	   buttons : {
	    								     		   Yes : function() {
	    								     			  var statusParameter='Decline';
	    								     			  var amount=0.0;
	    					    	    		    	  $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter+'&amount='+amount,
	    					    		    						 function(obj) {
	    					    	    		    		  DashboardJournalApprovalsHandler.initJournalsOnLoad();
	    					    		    						$("#journal-dashboard-view-dialog").dialog('close');
	    					    		    					});
	    					    	    		    	  $("#journal-approval-decline-dialog").dialog('close');
	    					    	    		    	  $("#journal-dashboard-view-dialog").dialog('close');
	    								     		   },
	    								     		   No : function() {
	    								     			  $("#journal-approval-decline-dialog").dialog('close');
	    		    						     		 }
	    								     		 },
	    						            	};
	    						            $("#journal-approval-decline-dialog").dialog(dialogOpts);
	    		    		          },
	    		    		      Cancel: function() {
	    		    		    	  $("#journal-dashboard-view-dialog").dialog('close');
	    						   }
	    	    			},
	    	    			close: function() {
	    	    				$('#journal-dashboard-view-container').html('');
	    	    			}
	    	    		});
	    	        });
	       }
	  });
	 });
var DashboardCustomerCRApprovalsHandler={
		count:1,
		//Change Request Dashboard for Customer,Sales Return,SE Transaction CR(Delivery Note,Sales Return,Day Book,Journal)
		initSearchCustomerChangeRequestOnLoad: function(){
			var paramString='action=search-cr-onload';
			$.post('customerCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#customer-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="change-request-row" align="'+data[loop].id+'" comp="'+data[loop].businessName+'" style="height: 100px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="change-request-row" class="green-result-row search-row-results" align="'+data[loop].id+'" comp="'+data[loop].businessName+'"  style="height: 100px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1 search-row-results" style="width: 210px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].createdBy + '</div>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_BUSINESS_NAME +':'+' </span><span class="property-value" id="customerBusinessName">' + data[loop].businessName + '</span><br/>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_LOCALITY+':'+' </span><span class="property-value">' + data[loop].locality + '</span><br/>';
								rowstr +='<span class="property">'+Msg.DATE+':'+' </span><span class="property-value">' + DashboardJournalApprovalsHandler.formatDate(data[loop].createdDate) + '</span><br/>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_CR_TYPE +':'+' </span><span class="property-value">' + data[loop].crType + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#customer-change-request-results-list').append(rowstr);
					};
					$('#customer-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#customer-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.customer-change-request').css( "width", "250px" );
			    	$('.sales-return-approval').css( "width", "250px" );
			    	$('.journal-approval').css( "width", "245px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.customer-change-request').css( "width", "210px" );
			    	$('.sales-return-approval').css( "width", "205px" );
			    	$('.journal-approval').css( "width", "200px" );
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
		
		validateCustomerChangeRequestCredits: function(){
			var result=true;
			if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10){
				$('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#creditLimit").focus(function(event){
					$('#creditLimitValid').empty();
					 $('#creditLimit_pop').show();
				});
				$("#creditLimit").blur(function(event){
					 $('#creditLimit_pop').empty();
					 if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10){
						 $('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#creditLimit_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			if(/^[0-9]+$/.test($('#creditOverdueDays').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditOverdueDays').val())).toString().length > 3){
				$('#creditOverdueDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#creditOverdueDays").focus(function(event){
					$('#creditOverdueDaysValid').empty();
					 $('#creditOverdueDays_pop').show();
				});
				$("#creditOverdueDays").blur(function(event){
					 $('#creditOverdueDays_pop').empty();
					 if(/^[0-9]+$/.test($('#creditOverdueDays').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditOverdueDays').val())).toString().length > 3){
						 $('#creditOverdueDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#creditOverdueDays_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			return result;
		},
};
$('#change-request-row').live('click',function(){
	var count=1;
	var id = $(this).attr('align');
	//var businessName=$('#customerBusinessName').html();
	var businessName = $(this).attr('comp');
	$.post('customerCr.json', 'action=get-customer-change-request-data&id='+id+'&businessName='+businessName, function(obj) {
		var result = obj.result.data;
		
		$.post('customer/customer_change_request_dashboard_approved.jsp', 'id='+id+'&businessName='+businessName, function(data) {
		        	$('#customer-change-request-view-container').html(data);
		        	$('.customerName').text(result.customerName);
		        	$('.crType').text(result.crType);
		        	if('M' == result.gender){
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
		        	$("#customer-change-request-view-dialog").dialog({
		    			autoOpen: true,
		    			height: 485,
		    			width: 700,
		    			modal: true,
		    			buttons: {
		    			Approved: function() {
		    				var thisButton = $(this);
		    				$('#customer-CR-approval-decline-container').html('Are You Sure Want To Approved Customer Change Request ?');
		    				var dialogOpts = {
							     		   height : 200,
							     		   width : 500,
					            	   buttons : {
							     		   Yes : function() {
							     			   var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
							    			   var creditOverdueDays=$('#creditOverdueDays').val();
							    				var resultSuccess=true;
							    				var resultFailure=false;
							    				if(DashboardCustomerCRApprovalsHandler.validateCustomerChangeRequestCredits()==false){
							    					 $("#customer-CR-approval-decline-dialog").dialog('close');
							    					return resultSuccess;
							    				}else{
							    					$.post('customerCr.json', 'action=validate-duplicate-customer-business-name&customerCRId='+id,
								    						 function(obj) {
							    						var data = obj.result.data;
							    						if(data == 'false'){
							    							 $("#customer-CR-approval-decline-dialog").dialog('close');
							    							$('#new-customer-cr-businessName-duplication-container').html('New Customer CR Business Name Already Exists');
	    							     			  		$("#new-customer-cr-businessName-duplication-dialog").dialog({
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
	    	      						     			  	}else{
							    				var statusParameter='Approved';
							    				$.post('customerCr.json', 'id='+id+'&action=approve-customer-cr'+'&creditLimit='+creditLimit+'&creditOverdueDays='+creditOverdueDays+'&status='+statusParameter,
							    						 function(obj) {
							    					DashboardCustomerCRApprovalsHandler.initSearchCustomerChangeRequestOnLoad();
							    						$("#customer-change-request-view-dialog").dialog('close');
							    					});
							    				 $("#customer-CR-approval-decline-dialog").dialog('close');
							    						}
							    				   });
							    		      }
							     		   },
							     		   No : function() {
							     			  $("#customer-CR-approval-decline-dialog").dialog('close');
	    						     		 }
							     		 },
					            	};
					            $("#customer-CR-approval-decline-dialog").dialog(dialogOpts);
		    				},
		    		      Decline: function() {
		    		    	  $('#customer-CR-approval-decline-container').html('Are You Sure Want To Decline Customer Change Request ?');
			    				var dialogOpts = {
								     		   height : 200,
								     		   width : 500,
						            	   buttons : {
								     		   Yes : function() {
								     			   var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
								     			   var creditOverdueDays=$('#creditOverdueDays').val();
								     			   if(creditOverdueDays == "" || creditOverdueDays == undefined){
								     				  creditOverdueDays=0;
								     			   }
								     			  var statusParameter='Decline';
							    		    	  $.post('customerCr.json', 'id='+id+'&action=approve-customer-cr'+'&creditLimit='+creditLimit+'&creditOverdueDays='+creditOverdueDays+'&status='+statusParameter,
								    						 function(obj) {
							    		    		  DashboardCustomerCRApprovalsHandler.initSearchCustomerChangeRequestOnLoad();
								    						$("#customer-change-request-view-dialog").dialog('close');
								    					});
								    				 $("#customer-CR-approval-decline-dialog").dialog('close');
								     		   },
								     		   No : function() {
								     			  $("#customer-CR-approval-decline-dialog").dialog('close');
		    						     		 }
								     		 },
						            	};
						            $("#customer-CR-approval-decline-dialog").dialog(dialogOpts);
		    		          },
		    		          Cancel: function(){
		    		        	  $("#customer-change-request-view-dialog").dialog('close');
		    		          }
		    			},
		    			close: function() {
		    				$('#customer-change-request-view-container').html('');
		    			}
		    		});
		        	
		        	//onclick of Business Name displaying All Customer CR view page for that businessName upto New Customer View Page. 
		        	$('#customer-change-request-business-name').live('click',function(){
		        		var businessName=$(this).attr('class');
		        		var customerCRId=$(this).attr('align');
		        		$.post('customerCr.json', 'action=get-previous-customer-cr-id'+'&businessName='+businessName+'&customerCRId='+customerCRId,
		        				function(obj){
		        					var previousCRId = obj.result.data;
		        					if(previousCRId != 0){
		        						$.post('customerCr.json', 'action=get-customer-change-request-data&id='+previousCRId+'&businessName='+businessName, function(obj) {
		        							var result = obj.result.data;
		        							
		        							 $.post('customer/customer_change_request_previous_cr_view.jsp','id=' + previousCRId,
			        						           function(data) {
			        						            $('#customer-cr-previous-view-container-'+count).html(data);
			        						            $('.customerName').text(result.customerName);
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
			        						        	$('.creditLimit').text(parseFloat(result.creditLimit).toFixed(2));
			        						        	$('.creditOverdueDays').text(result.creditOverdueDays);
			        						            var dialogOpts = {
			  	        						     		   height : 510,
			  	        						     		   width : 800,
			        						            		buttons : {
			    	        						     		    Close : function() {
			    	        						     		     $(this).dialog('close');
			    	        						     		    }
			    	        						     		 },
			        						            	};
			        						            $("#customer-cr-previous-view-dialog-"+count).dialog(dialogOpts);
			        						 });
		        						});
		        						 count=count+1;
		        				}else{
		        					$('#customer-cr-business-name').css('font-weight','normal');
		          					$('#customer-cr-business-name').css('color','#00f');
		          					$('#customer-cr-previous-business-name').css('font-weight','normal');
		          					$('#customer-cr-previous-business-name').css('color','#00f');
		        				}
		        					DashboardCustomerCRApprovalsHandler.count=DashboardCustomerCRApprovalsHandler.count+1;
		        		});
		        	});
		        });
	});
});


