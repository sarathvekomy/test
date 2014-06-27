var TransactionChangeRequestHandler = {
		expanded: true,
		dataForProduct : '',
		deliverynoteSteps : [ '#deliverynote-form', '#deliverynote-product-form' ],
		deliverynoteUrl : [ 'deliveryNote.json', 'deliveryNote.json' ],
		deliverynoteStepCount : 0,
		deliverynotePaymentSteps : [ '#deliverynote-payment-form'],
		deliverynotePaymentUrl : [ 'deliveryNote.json'],
		deliverynotePaymentStepCount : 0,
		salesreturnsSteps : [ '#sales-returns-form'],
		salesreturnsUrl : [ 'salesReturn.json'],
		salesreturnsStepCount : 0,
		dayBookSteps : [  '#day-book-basic-info-form' ,'#day-book-vehicle-details-form', '#day-book-allowances-form', '#day-book-amount-form','#day-book-product-form' ],
		dayBookUrl : [ 'dayBook.json' , 'dayBook.json', 'dayBook.json','dayBook.json' ,'dayBook.json'],
		dayBookStepCount : 0,
		journalCRSteps : [ '#journal-form'],
		journalsCRUrl : [ 'journal.json'],
		journalCRStepCount : 0,
		array : '',
		deliveryNoteArray : '',
		deliveryNoteOldFormValuesArray : '',
		dayBookArray: '',
		prevVal: 0,
		initPageLinks : function() {
					$('#transaction-details').pageLink({
						container : '.transaction-page-container',
						url : 'my-sales/transactions/change-transactions/change_transactions_search.jsp'
					});
				},
	initTransactionChangeSearchButtons: function () {
		$('#transactionRequestTypes').click(function(){
			var transactionRequestTypes = $(this).val();
			//Drop-down selection based on txn type
			if(transactionRequestTypes == "deliveryNote"){
			    $.post('deliveryNoteCr.json','action=change-dn-transaction-request',
			          function(obj) {
			       var data = obj.result.data;
			       $('#change-transaction-search-results-list').html('');
			       if (data!=null) {
				        var alternate = false;
				        for(var loop=0;loop<data.length;loop=loop+1) {
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate">';
							} else {
								rowstr = '<div class="green-result-row">';
							}
							alternate = !alternate;
							rowstr = rowstr + '<div class="green-result-col-1">'+
							'<div class="result-body">' +
							'<div class="result-title">' + data[loop].businessName + '</div>' +
							'<span class="property">'+Msg.DELIVERY_NOTE_INVOICE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].invoiceName + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2">'+
							'<div class="result-body">' +'<span class="property">'+Msg.DELIVERY_NOTE_CREATED_DATE_LABEL +':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].date + '</span>' +
							'</div>' +'<span class="property">'+Msg.DELIVERY_NOTE_BALANCE_LABEL +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].balance) + '</span>' +
							'</div>' +
							'<div class="green-result-col-action">' + 
							'<div id="'+data[loop].id+'" align="'+data[loop].invoiceNo+'" class="ui-btn edit-icon" title="Edit Delivery Note"></div>';
							'</div>' +
							'</div>';
					$('#change-transaction-search-results-list').append(rowstr);
				};
				TransactionChangeRequestHandler.initDNSearchResultButtons();
				        $('#change-transaction-search-results-list').jScrollPane({
				         showArrows : true
				        });
				       } else {
				        $('#change-transaction-search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Delivery Notes found</div></div></div>');
				       }
				       $.loadAnimation.end();
				       setTimeout(function() {
				        $('#change-transaction-search-results-list').jScrollPane({
				         showArrows : true
				        });
				}, 0);
			});
			}else if(transactionRequestTypes == "salesReturn"){
				$.post('salesReturnCr.json','action=change-sales-transaction-request',
				          function(obj) {
				       var data = obj.result.data;
				       $('#change-transaction-search-results-list').html('');
				       if (data!=null) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<div class="green-result-row alternate">';
								} else {
									rowstr = '<div class="green-result-row">';
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
								'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST +':'+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].total) + '</span>' +
								'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
								'</div>' +
								'<div class="green-result-col-action">' + 
								'<div id="'+data[loop].id+'" align="'+data[loop].invoiceNo+'" class="ui-btn edit-icon" title="Edit Sales Returns"></div>';
								'</div>' +
								'</div>';
						$('#change-transaction-search-results-list').append(rowstr);
					};
					TransactionChangeRequestHandler.initSalesReturnSearchResultButtons();
					$('#change-transaction-search-results-list').jScrollPane({showArrows:true});
						}else {
					        $('#change-transaction-search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Sales Returns found</div></div></div>');
					       }
					       $.loadAnimation.end();
					       setTimeout(function() {
					        $('#change-transaction-search-results-list').jScrollPane({
					         showArrows : true
					        });
					}, 300);
				});
			}else if(transactionRequestTypes == "dayBook"){
				$.post('dayBookCr.json','action=change-daybook-transaction-request',
				          function(obj) {
				       var data = obj.result.data;
				       $('#change-transaction-search-results-list').html('');
				       if(data!=null)  {
				   		var alternate = false;
				   		for(var loop=0;loop<data.length;loop=loop+1) {
				   			var dateFormat=TransactionChangeRequestHandler.formatDate(data[loop].createdOn);
				   			if(alternate) {
				   				var rowstr = '<div class="green-result-row alternate">';
				   			} else {
				   				rowstr = '<div class="green-result-row">';
				   			}
				   			alternate = !alternate;
				   			rowstr = rowstr + '<div class="green-result-col-1">'+
				   			'<div class="result-body">' +
				   			'<div class="result-title">'+ '</div>' +
				   			
				   			'<span class="property">'+Msg.DAY_BOOK_SALES_EXECUTIVE +':'+' </span><span class="property-value"style="font: bold 13px arial;width: 300px;" >' + data[loop].salesExecutive + '</span>' +
				   			'</div>' +
				   			'</div>' +
				   			'<div class="green-result-col-2">'+
				   			'<div class="result-body">' +'<span class="property">'+Msg.DAY_BOOK_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + dateFormat + '</span>' +
				   			'</div>' +
				   			'</div>' +
				   			'<div class="green-result-col-action">' + 
				   			'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Day Book"></div>';
				   			'</div>' +
				   			'</div>';
				   	$('#change-transaction-search-results-list').append(rowstr);
				   };
				   TransactionChangeRequestHandler.initDayBookResultButtons();
				     $('#change-transaction-search-results-list').jScrollPane({
				            showArrows : true
				           });
				   	} else {
				   		$('#change-transaction-search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Day Books found</div></div></div>');
				   	}
				   	$.loadAnimation.end();
				   	 setTimeout(function() {
				   	        $('#change-transaction-search-results-list').jScrollPane({
				   	         showArrows : true
				   	        });
				   	       }, 300);
				});
			}else if(transactionRequestTypes == "jounal"){
				$.post('journalCr.json','action=change-journal-transaction-request',
				          function(obj) {
				       var data = obj.result.data;
				       $('#change-transaction-search-results-list').html('');
				       if(data != undefined)  {
				   		var alternate = false;
				   		for(var loop=0;loop<data.length;loop=loop+1) {
				   			var dateFormat=TransactionChangeRequestHandler.formatDate(data[loop].createdOn);
				   			var amnt=data[loop].amount;
				        	var parsedAmount=parseFloat(Math.round(amnt * 100) / 100).toFixed(2);
				   			if(alternate) {
				   				var rowstr = '<div class="green-result-row alternate">';
				   			} else {
				   				rowstr = '<div class="green-result-row">';
				   			}
				   			alternate = !alternate;
				   			rowstr = rowstr + '<div class="green-result-col-1">'+
							'<div class="result-body">' +
							'<div class="result-title">' + data[loop].businessName + '</div>' +
							'<span class="property">'+Msg.JOURNAL_TYPE +':'+' </span><span class="property-value">' + data[loop].journalType + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2">'+
							'<div class="result-body">' +'<span class="property">'+Msg.JOURNAL_CREATED_DATE+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + dateFormat + '</span>' +
							'</div>' +'<span class="property">'+Msg.JOURNAL_AMOUNT +' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span>' +
							'</div>' +
							'<div class="green-result-col-action">' + 
				   			'<div id="'+data[loop].id+'" align="'+data[loop].invoiceNo+'" class="ui-btn edit-icon" title="Edit Journal"></div>';
				   			'</div>' +
							'</div>';
				   	$('#change-transaction-search-results-list').append(rowstr); 
				   };
				   TransactionChangeRequestHandler.initJournalResultButtons();
				     $('#change-transaction-search-results-list').jScrollPane({
				            showArrows : true
				           });
				   	} else {
				   		$('#change-transaction-search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Journals found</div></div></div>');
				   	}
				   	$.loadAnimation.end();
				   	 setTimeout(function() {
				   	        $('#change-transaction-search-results-list').jScrollPane({
				   	         showArrows : true
				   	        });
				   	       }, 300);
				});
			}
		});
		$('#action-clear').click(function() {
			$('#change-transactions-request-search-form').clearForm();
			$('#change-transaction-search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
			setTimeout(function(){
				$('#change-transaction-search-results-list').jScrollPane({showArrows:true});
			},0);
		});
		/*$('#transaction-details').click(function() {
			$('.transaction-page-container').load('my-sales/transactions/change-transactions/change_transactions_search.jsp');
		});*/
	},
	//search result edit buttons click
	initDNSearchResultButtons : function() {
		  $('.edit-icon').click(function() {
		    var id = $(this).attr('id');
		    var invoiceNumber = $(this).attr('align');
		    /*$.post('changeTransaction.json','action=get-delivery-note-creation-time&invoiceNumber='+invoiceNumber+'&deliveryNoteId='+id,
				    function(data){
		    	 var createdOn=data.result.data;
		    	 if(createdOn == 'n'){*/
		    $.post('deliveryNoteCr.json','action=validate-SE-DN-change-request&invoiceNumber='+invoiceNumber,
				    function(data){
		    var exists=data.result.data;
		    if(exists == 'y'){
		    	 showMessage({title:'Warning', msg:'Transaction CR for Delivery Note has not been Approved.'});
			       return;
		    }else{
		    	if (invoiceNumber.indexOf('COLLECTIONS') > -1) {
		    		var paramString="action=remove-delivery-note-product-from-session";
		        	$.post('deliveryNoteCr.json',paramString,function(obj){
		        	});
		        	$.post('deliveryNoteCr.json','action=get-original-delivery-note-data&id='+id,function(obj){
		    			var result = obj.result.data;
		    		$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_collect_amount_edit.jsp', 'id='+id,
					        function(data){
							$('.transaction-page-container').html(data);
							$("textarea#invoiceNumber").html(result.invoiceNo);
							$('#businessName').val(result.businessName);
							$('#invoiceDate').val(result.createdDate);
							$('#invoiceName').val(result.invoiceName);
							$('#previousCredit').val(result.previousCredit);
							$('#presentAdvance').val(result.presentAdvance);
							$('#presentPayable').val(result.presentPayable);
							$('#presentPayment').val(result.presentPayment);
							$('#totalPayable').val(result.totalPayable);
							$('#balance').val(result.balance);
							
							$('#bankName').val(result.bankName);
							$('#chequeNo').val(result.chequeNo);
							$('#branchName').val(result.branchName);
							$('#bankLocation').val(result.bankLocation);
							
							$('#deliveryNoteId').val(id);
							$('#originalInvoiceName').val(result.invoiceName);
							$('#salesId').val(result.salesBookId);
							$('#originalPresentPayment').val(result.presentPayment);
							$('#originalBalance').val(result.balance);
							$('#paymentTypeValue').val(result.paymentType);
							 SystemDefaultsHandler.getPaymentTypes();
							 if(result.paymentType.toLowerCase() == "cheque"){
									$('#bankname').show();
									$('#chequeno').show();
									$('#location').show(); 
									$('#branchname').show(); 
									$('#bankName').val(result.bankName);
									$('#chequeNo').val(result.chequeNo);
									$('#branchName').val(result.branchName);
									$('#bankLocation').val(result.bankLocation);
									$('#bankName').addClass('mandatory');
									$('#chequeNo').addClass('mandatory');
									$('#bankLocation').addClass('mandatory');
									$('#bankName_pop').hide();
									$('#branchname_pop').hide();
									$('#chequeNo_pop').hide(); 
									$('#bankLocation_pop').hide(); 
								} else{
											$('#bankname').hide();
											$('#chequeno').hide();
											$('#location').hide(); 
											$('#branchname').hide(); 
								}
					     });
		        	});
		    		} else {
		    			$.post('deliveryNoteCr.json','action=get-original-delivery-note-data&id='+id,function(obj){
			    			var result = obj.result.data;
		    			$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_edit.jsp', 'id='+id,
		    			        function(data){
		    					$('.transaction-page-container').html(data);
		    					$("textarea#invoiceNumber").html(result.invoiceNo);
								$('#businessName').val(result.businessName);
								$('#invoiceDate').val(result.createdDate);
								$('#invoiceName').val(result.invoiceName);
								$('#totalCost').val(result.grandTotal);
								$('#previousCredit').val(result.previousCredit);
								$('#presentAdvance').val(result.presentAdvance);
								$('#presentPayable').val(result.presentPayable);
								$('#presentPayment').val(result.presentPayment);
								$('#totalPayable').val(result.totalPayable);
								$('#balance').val(result.balance);
								
								$('#deliveryNoteId').val(id);
								$('#originalInvoiceName').val(result.invoiceName);
								$('#salesId').val(result.salesBookId);
								$('#originalTotalCost').val(result.grandTotal);
								$('#originalPresentPayment').val(result.presentPayment);
								$('#originalBalance').val(result.balance);
								$('#paymentTypeValue').val(result.paymentType);
								 SystemDefaultsHandler.getPaymentTypes();
								 if(result.paymentType.toLowerCase() == "cheque"){
										$('#bankname').show();
										$('#chequeno').show();
										$('#location').show(); 
										$('#branchname').show(); 
										$('#bankName').val(result.bankName);
										$('#chequeNo').val(result.chequeNo);
										$('#branchName').val(result.branchName);
										$('#bankLocation').val(result.bankLocation);
										$('#bankName').addClass('mandatory');
										$('#chequeNo').addClass('mandatory');
										$('#bankLocation').addClass('mandatory');
										$('#bankName_pop').hide();
										$('#branchname_pop').hide();
										$('#chequeNo_pop').hide(); 
										$('#bankLocation_pop').hide(); 
									} else{
												$('#bankname').hide();
												$('#chequeno').hide();
												$('#location').hide(); 
												$('#branchname').hide(); 
									}
								TransactionChangeRequestHandler.loadProductGridBasedOnBusinesName(id,result.businessName,result.salesBookId);
		    			        });
		    			});
		    		}
			
		        }
		    });
		    	/*}else{
		    		 showMessage({title:'Warning', msg:'Transaction CR for Delivery Note has been Expired.'});
				       return;
		    	 }*/
		    });
		 // });
		 },
		 initSalesReturnSearchResultButtons : function() {
		 $('.edit-icon').click(function() {
				var id = $(this).attr('id');
				var invoiceNumber = $(this).attr('align');
				/*$.post('changeTransaction.json','action=get-sales-return-creation-time&invoiceNumber='+invoiceNumber+'&salesReturnId='+id,
					    function(data){
			    	 var createdOn=data.result.data;
			    	 if(createdOn == 'n'){*/
			    $.post('salesReturnCr.json','action=validate-SE-SR-change-request&invoiceNumber='+invoiceNumber,
					    function(data){
			    var exists=data.result.data;
			    if(exists == 'y'){
			    	 showMessage({title:'Warning', msg:'Transaction CR for Sales Return has not been Approved.'});
				       return;
			    }else{
			    		$.post('salesReturnCr.json','action=get-original-sales-return-data&id='+id,function(obj){
			    			var result = obj.result.data;
			    			$.post('my-sales/transactions/change-transactions/sales_returns_change_transaction_edit.jsp', 'id='+id,
			    					function(data){
			    				            $('.transaction-page-container').html(data);
			    							$("#invoiceNumber").val(result.invoiceNo);
			    							$('#businessName').val(result.businessName);
			    							$('textarea#remarks').html(result.remarks);
			    							$('#invoiceName').val(result.invoiceName);
			    							$('#salesReturnId').val(id);
			    							$('#originalInvoiceName').val(result.invoiceName);
			    							$('#originalRemarks').val(result.remarks);
			    							TransactionChangeRequestHandler.loadSalesReturnProductGridBasedOnBusinesName(id,result.businessName);
			    			        });
			    		     });
			           }
			    });
			    	 /*}else{
			    		 showMessage({title:'Warning', msg:'Transaction CR for Sales Return has been Expired.'});
					       return;
			    	 }*/
			});
		 //});
		 },
		 initDayBookResultButtons : function() {
			 $('.edit-icon').click(function() {
					var id = $(this).attr('id');
					/*$.post('dayBookCr.json','action=get-day-book-creation-time&dayBookId='+id,
						    function(data){
				    	 var createdOn=data.result.data;
				    	 if(createdOn == 'n'){*/
					$.post('dayBookCr.json','action=validate-SE-DB-change-request&dayBookId='+id,
						    function(data){
				    var exists=data.result.data;
				    if(exists == 'y'){
				    	 showMessage({title:'Warning', msg:'Transaction CR for Day Book has not been Approved.'});
					       return;
				    }else{
				    	$.post('dayBookCr.json','action=get-original-day-book-data&id='+id,function(obj){
			    			var result = obj.result.data;
							//persist original cash data into CR table
							TransactionChangeRequestHandler.persistOriginalCashDayBookDataCR(result.salesBookId);
					$.post('my-sales/transactions/change-transactions/daybook_change_transaction_edit.jsp', 'id='+id,
				        function(data){
						$('.transaction-page-container').html(data);
						$("#dayBookNo").val(result.dayBookNo);
						$('#salesExecutive').val(result.salesExecutive);
						if(result.allotmentType == 'daily'){
							$('#isReturn').val(result.isReturn);
						}else{
							$('#isReturn').val('');
						}
						$('#startDate').val(result.createdDate);
						$('#currentDate').val(result.currentDate);
						$('#allotStockAdvance').val(result.openingBalance);
						$('#allotStockAdvance').val(result.openingBalance);
						if(result.allotmentType == 'Daily'){
							$('#reportingManager').val(result.reportingManager);
						}else{
							$('#reportingManager').val('');
						}
						if(result.basicInfoRemarks != 'null' || result.basicInfoRemarks != '' || result.basicInfoRemarks != '0'){
							$('textarea#dayBookCRRemarks').html(result.basicInfoRemarks);
						}else{
							$('textarea#dayBookCRRemarks').html();
						}
						$('#dayBookNo').val(result.dayBookNo);
						$('#vehicleNo').val(result.vehicleNo);
						$('#driverName').val(result.driverName);
						$('#startReading').val(result.startingReading);
						$('#endingReading').val(result.endingReading);
						if(result.vehicleDetailRemarks != 'null' || result.vehicleDetailRemarks != '' || result.vehicleDetailRemarks != '0'){
							$('textarea#dayBookVehicleRemarks').html(result.vehicleDetailRemarks);
						}else{
							$('textarea#dayBookVehicleRemarks').html();
						}
						if(result.allowancesRemarks != 'null' || result.allowancesRemarks != '' || result.allowancesRemarks != '0'){
							$('textarea#allowancesCRRemarks').html(result.allowancesRemarks);
						}else{
							$('textarea#allowancesCRRemarks').html();
						}
						$('#customerTotalPayable').val(result.totalPayable);
						$('#customerTotalReceived').val(result.totalRecieved);
						var totalCustomerCredit = currencyHandler.convertStringPatternToFloat(result.balance);
						if(totalCustomerCredit < 0.0){
							var advanceBalance=Math.abs(totalCustomerCredit).toFixed(2);
							$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.DayBook_ADVANCE_AMOUNT+')');
						}else{
							$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(totalCustomerCredit.toFixed(2)));
						}
						if(result.allotmentType == 'Daily'){
							$('#amountToFactory').val(result.amountToFactory);
						}else{
							$('#amountToFactory').val('');
						}
						if(result.amountRemarks != 'null' || result.amountRemarks != '' || result.amountRemarks != '0'){
							$('textarea#amountsCRRemarks').html(result.amountRemarks);
						}else{
							$('textarea#amountsCRRemarks').html();
						}
						//products remarks
						$('#dayBookId').val(id);
						$('#salesBookId').val(result.salesBookId);
						//hidden fields data to display in clear button click
						$('#originalAmountToFactory').val(result.amountToFactory);
						$('#originalReportingManagerName').val(result.reportingManager);
						$('#originalDayBookCRRemarks').val(result.basicInfoRemarks);
						$('#originalEndingReading').val(result.endingReading);
						$('#originalDayBookVehicleCRRemarks').val(result.vehicleDetailRemarks);
						$('#originalDayBookCRAllowancesRemarks').val(result.allowancesRemarks);
						$('#originalDayBookCRAmountsRemarks').val(result.amountRemarks);
						//set isReturn based on SE allotment type
						TransactionChangeRequestHandler.checkSEAllotmentType(result.isReturn);
						//populates original day book product data
						TransactionChangeRequestHandler.LoadDayBookProductGridData(id,result.salesExecutive);
				     });
				 });
			}
		});
				    	 /*}else{
				    		 showMessage({title:'Warning', msg:'Transaction CR for Day Book has been Expired.'});
						       return;
				    	 }*/
			//});
		});
		 },
		 initJournalResultButtons : function() {
			 $('.edit-icon').click(function() {
					var id = $(this).attr('id');
					var invoiceNumber = $(this).attr('align');
					/*$.post('changeTransaction.json','action=get-journal-creation-time&invoiceNumber='+invoiceNumber+'&journalId='+id,
						    function(data){
				    	 var createdOn=data.result.data;
				    	 if(createdOn == 'n'){*/
					$.post('journalCr.json','action=validate-SE-Journal-change-request&journalId='+id+'&invoiceNumber='+invoiceNumber,
						    function(data){
				    var exists=data.result.data;
				    if(exists == 'y'){
				    	 showMessage({title:'Warning', msg:'Transaction CR for Journal has not been Approved.'});
					       return;
				    }else{
				    	$.post('journalCr.json','action=get-original-journal-data&id='+id,function(obj){
			    			var result = obj.result.data;
			    			$.post('my-sales/transactions/change-transactions/journal_change_transaction_edit.jsp', 'id='+id,
							        function(data){
			    				            $('.transaction-page-container').html(data);
			    				            var select = $("#journalType");
			    				            select.append($("<option>").val(result.journalType).text(result.journalType));
			    							$('#businessName').val(result.businessName);
			    							$('textarea#invoiceNo').html(result.invoiceNo);
			    							$('#description').html(result.description);
			    							$('#amount').val(currencyHandler.convertFloatToStringPattern(result.amount));
			    							$('#invoiceName').val(result.invoiceName);
			    							$('#journalId').val(id);
			    							$('#createdOn').val(TransactionChangeRequestHandler.formatDate(result.createdOn));
			    							//hidden field data
			    							$('#originalInvoiceName').val(result.invoiceName);
			    							$('#originalJournalAmount').val(currencyHandler.convertFloatToStringPattern(result.amount));
			    							$('#originalJournalDescription').val(result.description);
			    			        });
			    		     });
				       }
				});
				    	/*}else{
				    		 showMessage({title:'Warning', msg:'Transaction CR for Journal has been Expired.'});
						       return;
				    	 }*/
			});
			 //});
		 },
		//Function to format date to DD/MM/YYYY
			formatDate:function(inputFormat){
				var str=inputFormat.split(/[" "]/);
				dt=new Date(str[0]);
				return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
			},
			//loading the grid of delivery note with existing details.
	        loadProductGridBasedOnBusinesName : function(deliveryNoteId,businessName,salesId){
		        $('.green-results-list').html('');
					$.post('deliveryNoteCr.json', 'action=get-existed-grid-data&businessName=' +businessName+'&deliveryNoteId='+deliveryNoteId+'&salesId='+salesId, function(obj) {
						var data = obj.result.data;
						if(data != undefined){
							if(data.length>0) {
								var count=1;
								for(var x=0;x<data.length;x=x+1) {
									var rowstr ='<div class="ui-content report-content" id = "delivery-note-grid">';
									rowstr=rowstr+'<div id="delivery-note-cr-row-'+count+'" class="report-body" style="width: 699px;height: auto; overflow: hidden;">';
									rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>'
									rowstr=rowstr+'<div id="row8" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
									rowstr=rowstr+'<div id="row6" class="report-body-column2 right-aligned" style="height: inherit; width: 0px; display: none;text-align: right;">'+currencyHandler.convertNumberToStringPattern(data[x].availableQuantity)+'</div>';
									rowstr=rowstr+'<div id="row4" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; text-align: right;">'+data[x].productCost+'</div>';
									rowstr=rowstr+'<div id="row2" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; text-align: right;">'+'<input type = "text" style="text-align: right; width:100px;" id="productQty" class="productQuantity" size=9px value='+currencyHandler.convertNumberToStringPattern(data[x].productQty)+'></div>';
									rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; text-align: right;">'+'<input type = "text" id="productBonus" style="text-align: right;width:100px;"  class="bonus" size=9px value='+currencyHandler.convertNumberToStringPattern(data[x].bonus)+'></div>';
									rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 100px;">'+'<textarea rows="2" cols="2" id="productBonusReason"  style="height: 41px; width: 100px; border: none; resize: none;"  class="bonusReason">'+data[x].bonusReason+'</textarea>'+'</div>';
									rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned totalCost" style="height: inherit; width: 150px; text-align: right;">'+data[x].totalCost+'</div>';
									rowstr=rowstr+'</div></div>';
									$('.green-results-list').append(rowstr);
									if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
											$('#delivery-note-cr-row-'+count).each(function(index) {
										        $(this).children().height(100);
										    });    
										   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
													$('#delivery-note-cr-row-'+count).each(function(index) {
												        $(this).children().height(70);
												    });    
											}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
												$('#delivery-note-cr-row-'+count).each(function(index) {
											        $(this).children().height(55);
											    });   
										   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
											   $('#delivery-note-cr-row-'+count).each(function(index) {
											        $(this).children().height(50);
											    });   
								           }
								           else{
								        	   $('#delivery-note-cr-row-'+count).each(function(index) {
											        $(this).children().height(40);
											    });   
										   }
									count=count+1;	
								}
							} else {
							$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
						}
							if(PageHandler.expanded){
								
								$( '#search-results-list' ).css( "width", "830px" );
								$( '.report-header' ).css( "width", "830px" );
						    	$( '.report-body' ).css( "width", "830px" );
								$( '.report-header-column2' ).css( "width", "90px" );
								$( '.totalCost' ).css( "width", "120px" );
								$( '.returnQty' ).css( "width", "90px" );		
						        $('.gridDisplay').show();
							}else{
							$( '#search-results-list' ).css( "width", "830px" );
							$( '.report-header' ).css( "width", "830px" );
					    	$( '.report-body' ).css( "width", "830px" );
							$( '.report-header-column2' ).css( "width", "100px" );
							$( '.report-body-column2' ).css( "width", "100px" );
							$( '.report-body-column2' ).css( "width", "100px" );
							$( '.totalCost' ).css( "width", "150px" );
							$( '.returnQty' ).css( "width", "95px" );		
					        $('.gridDisplay').show();
							}
							$('#ps-exp-col').click(function(){
								   if(PageHandler.expanded){
									   $( '#search-results-list' ).css( "width", "699px" );
									    $( '.report-header' ).css( "width", "699px" );
								    	$( '.report-body' ).css( "width", "699px" );
										$( '.report-header-column2' ).css( "width", "85px" );
										$( '.report-body-column2' ).css( "width", "85px" );
										$( '.totalCost' ).css( "width", "120px" );
										$( '.bonusReason' ).css( "width", "80px" );
								   }else{
									   $( '#search-results-list' ).css( "width", "830px" );
									   $( '.report-header' ).css( "width", "830px" );
								    	$( '.report-body' ).css( "width", "830px" );
										$( '.report-header-column2' ).css( "width", "100px" );
										$( '.report-body-column2' ).css( "width", "100px" );
										$( '.totalCost' ).css( "width", "150px" );
										$( '.bonusReason' ).css( "width", "95px" );
								   }
							   });
					}else {
						$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
						$('.gridDisplay').show();
					}
				});
		     
			},
			//delivery-note step-by-step Next-Previous-Save button functionality..
				initDeliveryNotePageSelection: function(){
				   PageHandler.hidePageSelection();	
				},
				initDeliveryNoteAddButtons : function() {
					TransactionChangeRequestHandler.deliverynoteStepCount = 0;
					$('#action-clear').click(function() {
						$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
						$("#error-message").dialog({
							resizable: false,
							height:140,
							title: "<span class='ui-dlg-confirm'>Confirm</span>",
							modal: true,
							buttons: {
								'Ok' : function() {
									var deliveryNoteId=$("#deliveryNoteId").val();
									var businessName=$('#businessName').val();
									var salesBookId=$('#salesId').val();
									var originalInvoiceName=$('#originalInvoiceName').val();
									if(TransactionChangeRequestHandler.deliverynoteStepCount == 0){
										var invoiceNumber= $("#invoiceNumber").val();
										if(invoiceNumber.indexOf('COLLECTIONS') > -1){
									     $('#invoiceName').val(originalInvoiceName);
									}else{
										$('#invoiceName').val(originalInvoiceName);
										TransactionChangeRequestHandler.loadProductGridBasedOnBusinesName(deliveryNoteId,businessName,salesBookId);
										var originalTotalCost = $('#originalTotalCost').val();
										$('#totalCost').val(originalTotalCost);
										$('#presentPayable').val(originalTotalCost);
									}
									}
									if(TransactionChangeRequestHandler.deliverynoteStepCount == 1){
										//modified code for clear button in DNCR payment screen
										//original payment,balance and payment type should changed
										var originalPresentPayment=$('#originalPresentPayment').val();
										var originalBalance=$('#originalBalance').val();
										var originalPaymentType=$('#paymentTypeValue').val();
										$('#presentPayment').val(originalPresentPayment);
										var invoiceNumber = $("#invoiceNumber").val();
										if(invoiceNumber.indexOf('COLLECTIONS') > -1){
										//set adv label or credit in balance if original default balance is advance 
										var previousCreditVal = $("#previousCredit").val();
										var presentAdvanceVal =$("#presentAdvance").val();
										var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));		
									    var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
											var totalBalance=currencyHandler.convertFloatToStringPattern((totalPayableVal - presentPaymentVal).toFixed(2));
											var formatTotalBalance=currencyHandler.convertStringPatternToFloat(totalBalance);
											if(formatTotalBalance > 0){
												$('#balance').val(currencyHandler.convertFloatToStringPattern(formatTotalBalance.toFixed(2)));
											}else{
												var advanceBalance=Math.abs(formatTotalBalance).toFixed(2);
												$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
											}
										}else{
											var presentPayableVal = $("#presentPayable").val();
											var previousCreditVal = $("#previousCredit").val();
											var presentAdvanceVal =$("#presentAdvance").val();
											var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(presentPayableVal)) + parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));
													var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
													var totalBalance=parseFloat(totalPayableVal) - parseFloat(presentPaymentVal);
													if(totalBalance > 0){
														$('#balance').val(currencyHandler.convertFloatToStringPattern(totalBalance.toFixed(2)));
													}else{
														var advanceBalance=Math.abs(totalBalance).toFixed(2);
														$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
													}
										}
										$('#description').val('');
										//display default payment type with respective fields
										$('#paymentType').val(originalPaymentType).attr('selected');
										if(originalPaymentType.toLowerCase() == "cash"){
											$('#bankname').hide();
											$('#branchname').hide();
											$('#chequeno').hide();
											$('#location').hide();
										}else{
											$('#bankname').show();
											$('#branchname').show();
											$('#chequeno').show();
											$('#location').show();
											$('#bankName').attr('class','mandatory');
											$('#chequeNo').attr('class','mandatory');
											$('#bankLocation').attr('class','mandatory');
										}
									}
									$(this).dialog('close');
								},
								Cancel: function() {
									$(this).dialog('close');
								}
							}
						});
					    return false;
						
						});		
					$('#button-next').click(function() {
						if ($(TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount]).validate() == false)
							return;
						if(TransactionChangeRequestHandler.deliverynoteStepCount==0){
							if($('#paymentTypeValue').val().toLowerCase() == "cheque"){
								$('#bankName').addClass('mandatory');
								$('#chequeNo').addClass('mandatory');
								$('#bankLocation').addClass('mandatory');
							}
							if(ValidateChangeTransaction.validatein()==false){
								return resultSuccess;
							}
							var invoiceNumber= $("#invoiceNumber").val();
							var totalCost=$('#totalCost').val();
							if(invoiceNumber.indexOf('DN') > -1){
								if(totalCost == 0){
									showMessage({title:'Error', msg:'Please Enter Product Quantity'});
									return false;
								}
							}
						}
						if(TransactionChangeRequestHandler.deliverynoteStepCount == 1){
							if(ValidateChangeTransaction.validatePaymnetFields()==false){
								return resultSuccess;
							}
							if($('#paymentTypeValue').val().toLowerCase() == "cheque"){
								if(ValidateChangeTransaction.validateChequeDetails()==false){
									return resultSuccess;
								}
							}
						}
						//display default payment type in DN CR by overriding drop-down list options
						if(TransactionChangeRequestHandler.deliverynoteStepCount == 0){
							var paymentType=$('#paymentTypeValue').val();
							if(paymentType == "NA"){
								$("#paymentType option[value=-1]").text(paymentType);
								$("#paymentType option[value=-1]").val(paymentType);
								$('select option').each(function(){
									if(paymentType != $(this).attr('value')){
										$('#paymentType option[value="'+$(this).attr('value')+'"]').remove();
									}
								});
							}else{
							if(!$(this).hasClass("clicked-once")){
							$('select option').each(function(){
								if(paymentType == $(this).attr('value')){
									$('#paymentType option[value="'+$(this).attr('value')+'"]').remove();
									$("#paymentType option[value=-1]").text(paymentType);
									$("#paymentType option[value=-1]").val(paymentType);
								}
							});
							}
							}
							}
						$(this).addClass('clicked-once');
						$('.delivery-note-separator').css('width',"80px");
						var paramString = $(TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount]).serializeArray();
						//paramString.push({name: 'presentPaymentChangedValue', value: presentPaymentChangedValue});
						var businessName = $('#businessName').val();
						var totalCost=$('#totalCost').val();
						var presentPayableVal = $("#presentPayable").val();
						var previousCreditVal = $("#previousCredit").val();
						var presentAdvanceVal =$("#presentAdvance").val();
						var totalPayableVal1 = parseFloat(currencyHandler.convertStringPatternToFloat(presentPayableVal)) + parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));
						if(totalPayableVal1 < 0){
							var totalPayable=Math.abs(totalPayableVal1).toFixed(2);
							$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayable)+' '+'('+Msg.ADVANCE_AMOUNT+')');
						}else{
							$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal1.toFixed(2)));
						}
						var totalPayableVal = $("#totalPayable").val();
						var presentPaymentVal =currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
						var formatBalance=parseFloat(totalPayableVal1) - parseFloat(presentPaymentVal);
						if(formatBalance < 0){
							var totalBalance=Math.abs(formatBalance).toFixed(2);
							$('#balance').val(currencyHandler.convertFloatToStringPattern(totalBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
						}else{
							$('#balance').val(currencyHandler.convertFloatToStringPattern(formatBalance.toFixed(2)));
						}
						
						var balance =$("#balance").val();
						var formatPresentPaymentValue = presentPaymentVal.toFixed(2);
						var modifiedPresentPayment=currencyHandler.convertFloatToStringPattern(formatPresentPaymentValue);
						//access form searialized value and modified
						$.each(paramString, function(i, formData) {
						    if(formData.value === totalCost){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(totalCost);
						    }
						    if(formData.value === presentPayableVal){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(presentPayableVal);
						    }
						   if(formData.value === previousCreditVal){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(previousCreditVal);
						    }
						    if(formData.value === presentAdvanceVal){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(presentAdvanceVal);
						    }
						    if(formData.value === totalPayableVal){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(totalPayableVal);
						    }
						    if(formData.value === modifiedPresentPayment){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(modifiedPresentPayment);
						    }
						    if(formData.value === balance){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(balance);
						    }
						}); 
						
						// To stop next button clickable
						var totalCost = $('#totalCost').val();
						/*if(totalCost != '0.00') {*/
							//convert serialized array to url-encoded string
							var sendFormData = $.param(paramString);
								$.ajax({type : "POST",
										url : 'deliveryNoteCr.json',
										data : sendFormData,
										success : function(data) {
											$('#error-message').html('');
											$('#error-message').hide();
											$(TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount]).hide();
											$(TransactionChangeRequestHandler.deliverynoteSteps[++TransactionChangeRequestHandler.deliverynoteStepCount]).show();
											if(PageHandler.expanded){
												PageHandler.pageSelectionHidden =false;
												PageHandler.hidePageSelection();
											}
											if (TransactionChangeRequestHandler.deliverynoteStepCount == TransactionChangeRequestHandler.deliverynoteSteps.length) {
												if(!PageHandler.expanded){
													PageHandler.hidePageSelection();//this is to enlarge preview container on loading page
												}
												else{
													PageHandler.pageSelectionHidden =false;
													PageHandler.hidePageSelection();
												}
												$('#button-next').hide();
												$('#action-clear').hide();
												$('#button-save').show();
												$('#button-prev').show();
												$('#button-update').show();
												$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_view.jsp', 'viewType=preview', function(data) {
													 $('#deliverynote-preview-container').css({'height' : '350px'});
														$('#deliverynote-preview-container').html(data);
														$('#deliverynote-preview-container').show();
														TransactionChangeRequestHandler.expanded=false;
														$('#ps-exp-col').click(function() {
															if(TransactionChangeRequestHandler.deliverynoteStepCount==TransactionChangeRequestHandler.deliverynoteSteps.length){
																 if(!PageHandler.expanded) {
																    	$('#deliverynote-preview-container').css({'height' : '350px'});
																		$('#deliverynote-preview-container').html(data);
																		$('#deliverynote-preview-container').show();
																		TransactionChangeRequestHandler.expanded=false;
																    }
																    else{
																    	$('#deliverynote-preview-container').css({'height' : '350px'});
																		$('#deliverynote-preview-container').html(data);
																		$('#deliverynote-preview-container').show();
																		TransactionChangeRequestHandler.expanded=true;
																    }
															}
														   
														});
												});
											}
											if (TransactionChangeRequestHandler.deliverynoteStepCount > 0) {
												$('#button-prev').show();
												$('.page-buttons').css('margin-left', '150px');
												var invoiceNumber= $("#invoiceNumber").val();
												var businessName = $("#businessName").val();
												var invoiceName = $("#invoiceName").val();
												if(invoiceNumber.indexOf('COLLECTIONS') > -1){
													$.post('deliveryNoteCr.json', 'action=go-for-payments&businessName=' +businessName +'&invoiceName=' +invoiceName +'&invoiceNumber='+invoiceNumber, function(obj) {
														$('#presentPay').hide();
													});
												} else {
													var listOfObjects = '';
													var sum = 0;
														$('div#delivery-note-grid').each(function(index, value) {
													    	  var id = $(this).find('div').attr('id');
													          var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.productQuantity').val());
													          var bonusQty = currencyHandler.convertStringPatternToNumber($(this).find('.bonus').val());
													          var bonusReason = $(this).find('.bonusReason').val();
													          var productName = $(this).find('#row1').html();
													          var productCost = currencyHandler.convertStringPatternToFloat($(this).find('#row4').html());
													          var batchNumber = $(this).find('#row8').html();
													          if(productQty != "" || bonusQty != "") {
													        	  if(index == 0){
													        		  listOfObjects +=productQty+'|'+batchNumber+'|'+bonusQty+'|'+bonusReason+'|'+productName+'|'+productCost;
													        	  }else{
													        		  listOfObjects +='?'+productQty+'|'+batchNumber+'|'+bonusQty+'|'+bonusReason+'|'+productName+'|'+productCost;
													        	  }
													          }
													    });
														
													    	$.post('deliveryNoteCr.json','action=save-product-data&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&invoiceNumber='+invoiceNumber,function(obj) {
													    	});
												}
											} else {
												$('#button-prev').hide();
												$('.page-buttons').css('margin-left', '200px');
											}
									 },
									 error : function(data) {
										 $('#error-message').html(data.responseText);
										 $('#error-message').dialog();
										 $('#error-message').show();
									 }
								 });
						//}
						
					});
					$('#button-prev').click(function() {
						$('#action-clear').show();
						if(PageHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
						}
						if (TransactionChangeRequestHandler.deliverynoteStepCount == TransactionChangeRequestHandler.deliverynoteSteps.length) {
											$('#button-next').show();
											$('#button-save').hide();
											$('#button-update').hide();
											$('#deliverynote-preview-container').html('');
											$('#deliverynote-preview-container').hide();
											$('.page-buttons').css('margin-left', '150px');
										}
										$(
												TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount])
												.hide();
										$(
												TransactionChangeRequestHandler.deliverynoteSteps[--TransactionChangeRequestHandler.deliverynoteStepCount])
												.show();
										if (TransactionChangeRequestHandler.deliverynoteStepCount > 0) {
											$('#button-prev').show();
											$('.page-buttons').css('margin-left', '150px');
											//DeliveryNoteHandler.expanded=true;
										} else {
											if(PageHandler.expanded){
												PageHandler.pageSelectionHidden =false;
												PageHandler.hidePageSelection();
												TransactionChangeRequestHandler.expanded=true;
												    $( '#search-results-list' ).css( "width", "830px" );
											        $( '.report-header' ).css( "width", "830px" );
											    	$( '.report-body' ).css( "width", "830px" );
													$( '.report-header-column2' ).css( "width", "100px" );
													$( '.report-body-column2' ).css( "width", "100px" );
											}
											$('#button-prev').hide();
											$('.page-buttons').css('margin-left', '240px');
										}
									});
					$('#button-save').click(function() {
						var thisButton = $(this);
						var invoiceNumber=$('#invoiceNumber').val();
						if(invoiceNumber.indexOf('COLLECTIONS') > -1){
							var paramString = 'action=save-payment-delivery-note&invoiceNumber='+invoiceNumber;
							PageHandler.expanded=false;
							pageSelctionButton.click();
							$('.page-content').ajaxSavingLoader();
							$.post('deliveryNoteCr.json', paramString, function(obj) {
								$.loadAnimation.end();
								$(this).successMessage({
									container : '.transaction-page-container',
									data : obj.result.message
								});
							});
						} else {
							var paramString = 'action=save-deliverynote&invoiceNumber='+invoiceNumber;
							PageHandler.expanded=false;
							pageSelctionButton.click();
							$('.page-content').ajaxSavingLoader();
							$.post('deliveryNoteCr.json', paramString, function(obj) {
								$.loadAnimation.end();
								$(this).successMessage({
									container : '.transaction-page-container',
									data : obj.result.message
								});
							});
						}
					});
					$('#action-cancel').click(function() {
										$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
										$("#error-message").dialog(
														{
															resizable : false,
															height : 140,
															title : "<span class='ui-dlg-confirm'>Confirm</span>",
															modal : true,
															buttons : {
																'Ok' : function() {
																	var invoiceNumber=$('#invoiceNumber').val();
																	$.post('deliveryNoteCr.json', 'action=remove-delivery-note-cr-session-data&invoiceNumber='+invoiceNumber, 
																			function(obj) {
																		$('.task-page-container').html('');
														    			var container ='.transaction-page-container';
														    			var url = "my-sales/transactions/change-transactions/change_transactions_search.jsp";
														    			$(container).load(url);
																		});
																	$(this).dialog('close');
																},
																Cancel : function() {
																	$(this).dialog('close');
																}
															}
														});
										return false;
									});
				},
				deliveryNoteLoad : function() {
					$('#presentPayment').change(function() {
						var invoiceNumber = $("#invoiceNumber").val();
						if(invoiceNumber.indexOf('COLLECTIONS') > -1){
							var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
							var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
							$('#presentPayment').val(formatPresentPayment);
							var previousCreditVal = $("#previousCredit").val();
							var presentAdvanceVal =$("#presentAdvance").val();
							var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));		
							       var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
									var totalBalance=currencyHandler.convertFloatToStringPattern((totalPayableVal - presentPaymentVal).toFixed(2));
									var formatTotalBalance=currencyHandler.convertStringPatternToFloat(totalBalance);
									if(formatTotalBalance > 0){
										$('#balance').val(currencyHandler.convertFloatToStringPattern(formatTotalBalance.toFixed(2)));
									}else{
										var advanceBalance=Math.abs(formatTotalBalance).toFixed(2);
										$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
									}
						}else{
						var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
						var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
						$('#presentPayment').val(formatPresentPayment);
						var presentPayableVal = $("#presentPayable").val();
						var previousCreditVal = $("#previousCredit").val();
						var presentAdvanceVal =$("#presentAdvance").val();
						var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(presentPayableVal)) + parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));
								var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
								var totalBalance=parseFloat(totalPayableVal) - parseFloat(presentPaymentVal);
								if(totalBalance > 0){
									$('#balance').val(currencyHandler.convertFloatToStringPattern(totalBalance.toFixed(2)));
								}else{
									var advanceBalance=Math.abs(totalBalance).toFixed(2);
									$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
								}
					}
						if($('#paymentTypeValue').val() == 'NA'){
							var presentPaymentVal = currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
							if(presentPaymentVal == 0) {
								TransactionChangeRequestHandler.getPaymentTypesWithOutPayment();
							} else {
								TransactionChangeRequestHandler.getPaymentTypes();
							}
						  }
					});
					$('#paymentType').change(function(){ 
						if($('#paymentType').val().toLowerCase()=='cheque'){
							$('#bankName_pop').hide();
							$('#branchname_pop').hide();
							$('#chequeNo_pop').hide(); 
							$('#bankLocation_pop').hide(); 
							$('#bankname').show();
							$('#bankName').attr('class','mandatory');
							$('#branchname').show();
							$('#chequeno').show();
							$('#chequeNo').attr('class','mandatory');
							$('#location').show();
							$('#bankLocation').attr('class','mandatory');
						}else{
							$('#bankname').hide();
							$('#bankName').removeAttr('class');
							$('#branchname').hide();
							$('#chequeno').hide();
							$('#chequeNo').removeAttr('class');
							$('#location').hide();
							$('#bankLocation').removeAttr('class');
						}
					});
					$('.green-results-list').live("blur",function(){
						var businessName = $("#businessName").val();
						var invoiceName = $("#invoiceName").val();
						var listOfObjects = '';
						var sum = 0;
						    $.each($('.report-body'), function(index, value) {
						    	  var availableQty = currencyHandler.convertStringPatternToNumber($(this).find('#row6').html());
						    	  var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.productQuantity').val());
						    	  var bonusQty = currencyHandler.convertStringPatternToNumber($(this).find('.bonus').val());
						          var bonusReason = $(this).find('.bonusReason').val();
						          var productName = $(this).find('#row1').html();
						          var productCost = $(this).find('#row4').html();
						          if(parseInt(productQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
								       return;
						          }
						          if(parseInt(bonusQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
								       return;
						          }
						          if(parseInt(bonusQty) + parseInt(productQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
								       return;
						          }
						          var totalCost = currencyHandler.convertStringPatternToFloat(productCost) *  parseInt(productQty);
						          if(isNaN(totalCost)) {
						        	  totalCost = 0.00;
						          }
						          $(this).find('#row5').html(currencyHandler.convertFloatToStringPattern(totalCost.toFixed(2)));
						          sum += totalCost;
						          $('#totalCost').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
						          $('#presentPayable').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
						          $(this).find('.productQuantity').val(currencyHandler.convertNumberToStringPattern(productQty));
						          $(this).find('.bonus').val(currencyHandler.convertNumberToStringPattern(bonusQty));
						});
					});
					//save original payments data(invoice Name,present Payments) in to session to match data in Collection DN CR
				},
				getPaymentTypesWithOutPayment : function(){
					$('#paymentType').empty();
					$('#paymentType').append( $('<option></option').text('NA').val('NA'));
					$('#bankname').hide().find("input").val("");
					$('#bankName').removeAttr('class');
					$('#branchname').hide().find("input").val("");
					$('#chequeno').hide().find("input").val("");
					$('#chequeNo').removeAttr('class');
					$('#location').hide();
					$('#bankLocation').removeAttr('class');
				},
				getPaymentTypes : function() {
					$.post('default.json','action=get-all-payment-types',function(obj){
						var result =obj.result.data;
						if(result.length>0){
							$('#paymentType').empty();
							var select= 'select'
							$('#paymentType').append( $('<option></option').text('select').val('-1'));
							for(var loop=0;loop<result.length;loop = loop+1){
								$('#paymentType').append( $('<option value='+ result[loop] +'></option').text(result[loop]));			
						};
						}
					});
				},
				checkLength: function(len,number,bonus,batchNo) {
					if(len>25||bonus>25||batchNo>8){
						$('#row-'+number).css({'height' : '30px'});
						$('.invoice-boxes-'+number).css({'height' : 'inherit'});
						if(len>=45||bonus>=45||batchNo>16){
							$('#row-'+number).css({'height' : '45px'});
						}
						if(len>=55||bonus>=55){
							$('#row-'+number).css({'height' : '60px'});
						}
						if(len>=70||bonus>=70){
							$('#row-'+number).css({'height' : '70px'});
						}
					}
				},
				addColor: function(number) {
					if(number%2 !=0){
						$('#row-'+number).css({'background-color' : 'LightGray'});
					}
					else
					$('#row-'+number).css({'background-color' : 'FloralWhite'});
					
				},
				//step-by-step Next-save of Sales Return
				//load the grid of Sales return with existing details.
				loadSalesReturnProductGridBasedOnBusinesName : function(salesReturnId,businessName){
					$('.green-results-list1').html('');
					$.post('salesReturnCr.json', 'action=get-sales-return-grid-data&businessName=' +businessName+'&salesReturnId='+salesReturnId, function(obj) {
						var data = obj.result.data;
						if(data !== undefined){
							if(data.length>0) {
								$('.success-msg').hide();
								var count=1;
								for(var x=0;x<data.length;x=x+1) {
									var rowstr ='<div class="ui-content report-content" id="sales-row">';
									rowstr=rowstr+'<div id="sales-return-cr-row-'+count+'" class="report-body" style="width: 697px;height: 30px; overflow: hidden;">';
									rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 120px; word-wrap: break-word;">'+data[x].productName+'</div>';
									rowstr=rowstr+'<div id="row2" class="report-body-column2 centered" style="height: inherit; width: 120px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
									rowstr=rowstr+'<div id="row6" class="report-body-column2 centered" style="height: inherit; width: 120px; word-wrap: break-word;">'+'<input type = "text" style="text-align:right;" id="damaged" class="damaged" size=4px value='+currencyHandler.convertNumberToStringPattern(data[x].damagedQty) +'>' + '</div>';
									rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 120px; word-wrap: break-word;">'+'<input type = "text" style="text-align:right;" id="resalable" class="resalable" size=4px value='+currencyHandler.convertNumberToStringPattern(data[x].resaleableQty) +'>'+'</div>';
									rowstr=rowstr+'<div id="row3" class="report-body-column2 centered" style="height: inherit; width: 120px; word-wrap: break-word;">'+'<input type = "text" id="returnQty" class="returnQty" style = "size: 4px; border: none;text-align:right;" value='+ currencyHandler.convertNumberToStringPattern(data[x].totalQty) +'>'+'</div>';
									rowstr=rowstr+'<div>'+'<input type = "hidden" id="totalQty" class="totalQty"  value='+ currencyHandler.convertNumberToStringPattern(data[x].totalQty) +'>'+'</div>';
									rowstr=rowstr+'</div></div>';
									$( '#returnQty' ).css( "width", "85px" );
									$('.green-results-list1').append(rowstr);
									if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
											$('#sales-return-cr-row-'+count).each(function(index) {
										        $(this).children().height(100);
										    });    
										   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
													$('#sales-return-cr-row-'+count).each(function(index) {
												        $(this).children().height(70);
												    });    
											}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
												$('#sales-return-cr-row-'+count).each(function(index) {
											        $(this).children().height(55);
											    });   
										   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
											   $('#sales-return-cr-row-'+count).each(function(index) {
											        $(this).children().height(50);
											    });   
								           }
								           else{
								        	   $('#sales-return-cr-row-'+count).each(function(index) {
											        $(this).children().height(40);
											    });   
										   }
									count=count+1;	
									
									}
							} else {
							$('.green-results-list1').html('<div class="success-msg">No Products Available.</div>');
						}
							if(PageHandler.expanded){
								$( '#search-results-list' ).css( "width", "830px" );
								$( '.report-header' ).css( "width", "830px" );
						    	$( '.report-body' ).css( "width", "830px" );
								$( '.report-header-column2' ).css( "width", "120px" );
								$( '.report-body-column2' ).css( "width", "120px" );
								$( '.returnQty' ).css( "width", "120px" );		
						        $('.gridDisplay').show();
							}else{
							$( '#search-results-list' ).css( "width", "830px" );
							$( '.report-header' ).css( "width", "830px" );
					    	$( '.report-body' ).css( "width", "830px" );
							$( '.report-header-column2' ).css( "width", "150px" );
							$( '.report-body-column2' ).css( "width", "150px" );
							$( '.returnQty' ).css( "width", "150px" );		
					        $('.gridDisplay').show();
							}
							$('#ps-exp-col').click(function(){
								   if(PageHandler.expanded){
									   $( '#search-results-list' ).css( "width", "699px" );
									    $( '.report-header' ).css( "width", "699px" );
								    	$( '.report-body' ).css( "width", "699px" );
										$( '.report-header-column2' ).css( "width", "120px" );
										$( '.report-body-column2' ).css( "width", "120px" );
										$( '.returnQty' ).css( "width", "120px" );
								   }else{
									   $( '#search-results-list' ).css( "width", "830px" );
									   $( '.report-header' ).css( "width", "830px" );
								    	$( '.report-body' ).css( "width", "830px" );
										$( '.report-header-column2' ).css( "width", "150px" );
										$( '.report-body-column2' ).css( "width", "150px" );
										$( '.returnQty' ).css( "width", "150px" );
								   }
							   });
					}else {
						$('.green-results-list1').html('<div class="success-msg">No Products Available.</div>');
						$('.gridDisplay').show();
					}
				});
				},
				initSalesReturnAddButtons : function() {
					TransactionChangeRequestHandler.salesreturnsStepCount = 0;
					$('#action-clear').click(function() {
						$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
						$("#error-message").dialog({
							resizable: false,
							height:140,
							title: "<span class='ui-dlg-confirm'>Confirm</span>",
							modal: true,
							buttons: {
								'Ok' : function() {
									var originalInvoiceName=$('#originalInvoiceName').val();
									var originalRemarks=$('#originalRemarks').val();
									var salesReturnId=$('#salesReturnId').val();
									var salesReturnBusinessName=$('#businessName').val();
									$('#invoiceName').val(originalInvoiceName);
									$('#remarks').val(originalRemarks);
									$('#description').val('');
									TransactionChangeRequestHandler.loadSalesReturnProductGridBasedOnBusinesName(salesReturnId,salesReturnBusinessName);
									$(this).dialog('close');
								},
								Cancel: function() {
									$(this).dialog('close');
								}
							}
						});
					    return false;
					});
					$('#button-save').click(function() {
						var thisButton = $(this);
						var invoiceNumber= $('#invoiceNumber').val();
						var paramString = 'action=save-sales-returns&invoiceNumber='+invoiceNumber;
						PageHandler.expanded=false;
						pageSelctionButton.click();
						$('.page-content').ajaxSavingLoader();
						$.post('salesReturnCr.json', paramString, function(obj) {
							$.loadAnimation.end();
							$(this).successMessage({
								container : '.transaction-page-container',
								data : obj.result.message
							});
						});
				});
					$('#button-next').click(function(){
						var qtySold;
						if(PageHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
						}
						if($(TransactionChangeRequestHandler.salesreturnsSteps[TransactionChangeRequestHandler.salesreturnsStepCount]).validate()==false) return;
						var thisButton = $(this);
						var listOfObjects='';
						var invoiceNumber= $('#invoiceNumber').val();
						var businessName = $('#businessName').val();
						var invoiceName = $('#invoiceName').val();
						var crDescription = $('#description').val();
						var remarks=$('#remarks').val();
						$('div#sales-row').each(function(index, value){
							 var previousTotalQty = $("#totalQty").val();
							var id = $(this).find('div').attr('id');
							var productName = $(this).find('#row1').html();
							var batchNumber = $(this).find('#row2').html();
							var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
							var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
							var returnQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
							$.ajax({type: "POST",
								url:'salesReturnCr.json', 
								data:  'action=get-quantity-sold&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName,
								async : false,
								success: function(data){
								  qtySold = parseInt(data.result.data) + parseInt(previousTotalQty); 
							 }
							  });
							 if(parseInt(returnQty) > parseInt(qtySold)) {
								  showMessage({title:'Warning', msg:'Please enter valid quantities.'});
							      return false;
							  } else {
								  if(damaged != "" || resalable != ""){
										if(index == 0) {
											listOfObjects +=damaged+'|'+resalable+'|'+returnQty+'|'+batchNumber+'|'+productName
										} else {
											listOfObjects +=','+damaged+'|'+resalable+'|'+returnQty+'|'+batchNumber+'|'+productName
										}
									}
							  }
						});
						var paramString = 'action=save-sales-return-products&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&invoiceNumber='+invoiceNumber+'&description='+crDescription+'&remarks='+remarks;
						if(listOfObjects != '') {
							$.ajax({type : "POST",
								url : 'salesReturnCr.json',
								data : paramString,
								success : function(data) {
									$(TransactionChangeRequestHandler.salesreturnsSteps[TransactionChangeRequestHandler.salesreturnsStepCount])	.hide();
									$(TransactionChangeRequestHandler.salesreturnsSteps[++TransactionChangeRequestHandler.salesreturnsStepCount]).show();
									$('#button-next').hide();
									$('#action-clear').hide();
									$('#button-save').show();
									$('#button-prev').show();
									$.post('my-sales/transactions/change-transactions/sales_return_change_request_preview.jsp', 'viewType=preview', function(data){
										$('#sales-return-preview-container').css({'height' : '350px'});
										$('#sales-return-preview-container').html(data);
										$('#sales-return-preview-container').show();
										
									});
									
								},
						});
						}
					});
					$('#button-prev').click(function() {
						$('#action-clear').show();
						if(PageHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
						}
						if (TransactionChangeRequestHandler.salesreturnsStepCount == TransactionChangeRequestHandler.salesreturnsSteps.length) {
							$('#button-next').show();
							$('#button-save').hide();
							$('#button-update').hide();
							$('#sales-return-preview-container').html('');
							$('#sales-return-preview-container').hide();
							$('.page-buttons').css('margin-left', '150px');
						}
						$(TransactionChangeRequestHandler.salesreturnsSteps[TransactionChangeRequestHandler.salesreturnsStepCount]).hide();
						$(TransactionChangeRequestHandler.salesreturnsSteps[--TransactionChangeRequestHandler.salesreturnsStepCount]).show();
						if (TransactionChangeRequestHandler.salesreturnsStepCount > 0) {
							$('#button-prev').show();
							$('.page-buttons').css('margin-left', '150px');
						} else {
							$('#button-prev').hide();
							$('.page-buttons').css('margin-left', '240px');
						}
						
					});
				    $('#action-cancel').click(function() {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({	
							resizable : false,
							height : 140,
							title : "<span class='ui-dlg-confirm'>Confirm</span>",
							modal : true,
							buttons : {
								'Ok' : function() {
									$.post('salesReturnCr.json', 'action=remove-sales-return-cr-session-data', 
											function(obj) {
									$('.task-page-container').html('');
									var container ='.transaction-page-container';
					    			var url = "my-sales/transactions/change-transactions/change_transactions_search.jsp";
					    			$(container).load(url);
									});
					    			$(this).dialog('close');
								},
								Cancel : function() {
									$(this).dialog('close');
								}
							}
						});
						return false;
				});
				   
		},
          salesReturnLoad:function() {
			$('#productName').change(function() {
				var productName=$("#productName").val()
				var businessName = $("#businessName").val()
				$.post('salesReturnCr.json','action=get-product-cost&productNameVal='+productName+'&businessNameVal='+businessName, function(obj) {
					$('#cost').val(obj.result.data);
				});
			});
			
			$('#totalCost').focus(function() {
				var returnQty=$("#returnQty").val();
				var cost=$("#cost").val();
					$('#totalCost').val((returnQty * cost).toFixed(2));
				});
			
			$('.green-results-list1').live("blur",function(){
				var sum = 0;
				var businessName = $('#businessName').val();
				  $.each($('.report-body'), function(index, value) {
					  var previousTotalQty = $("#totalQty").val();
					  var productName = $(this).find('#row1').html();
					  var batchNumber = $(this).find('#row2').html();
					  var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
					  var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
						if(damaged == "") {
							damaged = 0;
						}
						if(resalable == "") {
							resalable = 0;
						}
						var returnQty = parseInt(damaged) + parseInt(resalable);
						$.ajax({type: "POST",
							url:'salesReturnCr.json', 
							data:  'action=get-quantity-sold&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName,
							async : false,
							success: function(data){
							  qtySold = parseInt(data.result.data) + parseInt(previousTotalQty); 
						    }
					  });
					  $(this).find('.returnQty').val(currencyHandler.convertNumberToStringPattern(returnQty));
					  $(this).find('.returnQty').attr('readonly','readonly');
					  var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
			          $(this).find('.damaged').val(currencyHandler.convertNumberToStringPattern(damaged));
					  $(this).find('.resalable').val(currencyHandler.convertNumberToStringPattern(resalable));
					  if(parseInt(returnQty) > parseInt(qtySold)) {
						  showMessage({title:'Warning', msg:'You Can Not Exceed Sold Quantity ('+qtySold+')'});
						  event.preventDefault();
					  } 
				  });
				});
		},
		//day book step-by-step js function.
		dayBookInitAddButtons : function() {
			TransactionChangeRequestHandler.dayBookStepCount = 0;
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							if(TransactionChangeRequestHandler.dayBookStepCount == 0){
								var originalReportingManager=$('#originalReportingManagerName').val();
								var originalDayBookRemarks=$('#originalDayBookCRRemarks').val();
								$('#reportingManager').val(originalReportingManager);
								$('textarea#dayBookCRRemarks').val(originalDayBookRemarks);
							}
							if(TransactionChangeRequestHandler.dayBookStepCount == 1){
								var originalEndingReading=$('#originalEndingReading').val();
								var originalDayBookCrVehicleRemarks=$('#originalDayBookVehicleCRRemarks').val();
								$('#endingReading').val(originalEndingReading);
								$('textarea#dayBookVehicleRemarks').val(originalDayBookCrVehicleRemarks);
								$('#description').val('');
							}
							if(TransactionChangeRequestHandler.dayBookStepCount == 2){
								var originalDayBookCRAllowancesRemarks=$('#originalDayBookCRAllowancesRemarks').val();
								$('#allowancesCRRemarks').val(originalDayBookCRAllowancesRemarks);
							}
							if(TransactionChangeRequestHandler.dayBookStepCount == 3){
								//on click of clear in amount step of day book CR defualt (original) amountToFactory and Closing balance should display.
								var originalAmountToFactory=$('#originalAmountToFactory').val();
								var changedAmountToFactory=$('#amountToFactory').val();
								var amountToFactoryDiff=parseFloat(originalAmountToFactory) - parseFloat(changedAmountToFactory);
								if(amountToFactoryDiff > 0){
									var closingBalance=currencyHandler.convertStringPatternToFloat($('#closingBalance').val());
									var originalClosingBalance=currencyHandler.convertFloatToStringPattern(parseFloat(closingBalance) - parseFloat(amountToFactoryDiff));
									var formatClosingBalance=currencyHandler.convertStringPatternToFloat(originalClosingBalance);
									$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(formatClosingBalance.toFixed(2)));
								}else{
									var closingBalance=currencyHandler.convertStringPatternToFloat($('#closingBalance').val());
									var originalClosingBalance=currencyHandler.convertFloatToStringPattern(parseFloat(closingBalance) - parseFloat(amountToFactoryDiff));
									var formatClosingBalance=currencyHandler.convertStringPatternToFloat(originalClosingBalance);
									$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(formatClosingBalance.toFixed(2)));
								}
								$('#amountToFactory').val(originalAmountToFactory);
								var originalDayBookCRAmountsRemarks=$('#originalDayBookCRAmountsRemarks').val();
								$('#amountsCRRemarks').val(originalDayBookCRAmountsRemarks);
							}
							if(TransactionChangeRequestHandler.dayBookStepCount == 4){
								var dayBookId=$('#dayBookId').val();
								var salesExecutive=$('#salesExecutive').val();
								//populates original day book product data
								TransactionChangeRequestHandler.LoadDayBookProductGridData(dayBookId,salesExecutive);
								var originalDayBookCRProductRemarks=$('#originalDayBookCRProductRemarks').val();
								$('#productsCRRemarks').val(originalDayBookCRProductRemarks);
							}
							$(this).dialog('close');
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			    return false;
			});
			$('#button-next').click(function() {
						var success = true;
						var resultSuccess =true;
						if(TransactionChangeRequestHandler.dayBookStepCount == 0){
							if($('#reportingManager').is(":visible")){
					    	if(ValidateChangeTransaction.validateReportingManager() == false){
					    		return true;
					    	}
							}
						}
						if(TransactionChangeRequestHandler.dayBookStepCount == 1){
							if(ValidateChangeTransaction.validateReading()==false){
								return resultSuccess;
							}
							}
						if(TransactionChangeRequestHandler.dayBookStepCount == 2){
							TransactionChangeRequestHandler.calculateClosingBalance();
							if(ValidateChangeTransaction.validateAllowances()== false){
								return resultSuccess;
							}
							}
						if(TransactionChangeRequestHandler.dayBookStepCount == 3){
							if($('#amountToFactory').is(":visible")){
							if(ValidateChangeTransaction.validateAmounts() == false){
								return true;
							}
							}
						}
						if(TransactionChangeRequestHandler.dayBookStepCount == 4){
							$('div#day-book-grid').each(function(index, value) {
								var productName = $(this).find('#row1').html();
					        	  var openingStock = currencyHandler.convertStringPatternToNumber($(this).find('#row2').html());
					        	  var productToCustomer = currencyHandler.convertStringPatternToNumber($(this).find('#row3').html());
					        	  var productToFactory = currencyHandler.convertStringPatternToNumber($(this).find('.productToFactory').val());
					        	  var closingStock =parseInt(openingStock) - (parseInt(productToCustomer) + parseInt(productToFactory)) ;
					        	  if( closingStock < 0){
										//$(this).find('#row5').html(currencyHandler.convertNumberToStringPattern(openingStock));
										showMessage({title:'Error', msg:'Products to factory should not be greater than closing stock'});
										flag = false;
										event.preventDefault();
										return false;
									}else{
										flag = true;
									}
										
								});
								if(flag == false)
								 return false;
						}
						
						//Ending of totalAllowances value setting.
						var paramString = $(TransactionChangeRequestHandler.dayBookSteps[TransactionChangeRequestHandler.dayBookStepCount]).serializeArray();
						//paramString.push({name: 'isReturn', value: $('#isReturn').val()});
						var execAllowances=$('#executiveAllowances').val();
						var driverAllowances=$('#driverAllowances').val();
						var vehicleFuelAllowances=$('#vehicleFuelExpenses').val();
						var vehicleMeterReading=$('#vehicleMeterReading').val();
						var offloadingLoadingCharges=$('#offloadingLoadingCharges').val();
						var vehicleMaintenanceExpenses=$('#vehicleMaintenanceExpenses').val();
						var miscellaneousExpenses=$('#miscellaneousExpenses').val();
						var dealerPartyExpenses=$('#dealerPartyExpenses').val();
						var municipalCityCouncil=$('#municipalCityCouncil').val();
						var totalAllowances=$('#totalAllowances').val();
						var customerTotalPayable=$('#customerTotalPayable').val();
					    var customerTotalReceived=$('#customerTotalReceived').val();
					    var customerTotalCredit=$('#customerTotalCredit').val();
					    var amountToBank=$('#amountToBank').val();
					    var amountToFactory=$('#amountToFactory').val();
						var closingBalance=$('#closingBalance').val();
						var startReading=$('#startReading').val();
						var endingReading=$('#endingReading').val();
						var openingBalance=$('#allotStockAdvance').val();
						//access form searialized value and modified
						$.each(paramString, function(i, formData) {
							if(formData.name ==='reasonAmountToBank' || formData.name === 'reasonMiscellaneousExpenses' || formData.name === 'remarks'){ 
						    	formData.value = currencyHandler.formatStringValue(formData.value);
						    }
						    if(formData.value === execAllowances){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(execAllowances);
						    }
						    if(formData.value === driverAllowances){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(driverAllowances);
						    }
						    if(formData.value === vehicleFuelAllowances){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(vehicleFuelAllowances);
						    }
						    if(formData.value === offloadingLoadingCharges){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(offloadingLoadingCharges);
						    }
						    if(formData.value === vehicleMaintenanceExpenses){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(vehicleMaintenanceExpenses);
						    }
						    if(formData.value === miscellaneousExpenses){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(miscellaneousExpenses);
						    }
						    if(formData.value === dealerPartyExpenses){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(dealerPartyExpenses);
						    }
						    if(formData.value === municipalCityCouncil){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(municipalCityCouncil);
						    }
						    if(formData.value === totalAllowances){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(totalAllowances);
						    }
						    
						    if(formData.value === customerTotalPayable){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(customerTotalPayable);
						    }
						    if(formData.value === customerTotalReceived){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(customerTotalReceived);
						    }
						    if(formData.value === customerTotalCredit){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(customerTotalCredit);
						    }
						    if(formData.value === amountToBank){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(amountToBank);
						    }
						    if(formData.value === amountToFactory){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(amountToFactory);
						    }
						    if(formData.value === closingBalance){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(closingBalance);
						    }
						    
						    if(formData.value === startReading){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(startReading);
						    }
						    if(formData.value === endingReading){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(endingReading);
						    }
						    if(formData.value === openingBalance){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(openingBalance);
						    }
						});    
						//convert serialized array to url-encoded string
						/*var salesExecutive = $('#salesExecutive').val();
						$.post('changeTransaction.json','action=check-day-book&salesExecutive='+salesExecutive,
								function(obj) {
							var data = obj.result.data;*/
							/*if(data == "false") {*/
								var sendFormData = $.param(paramString);
						        $.ajax({
											type : "POST",
											url : 'dayBookCr.json',
											data : sendFormData,
											success : function(data) {
												$('#error-message').html('');
												$('#error-message').hide();
												$(TransactionChangeRequestHandler.dayBookSteps[TransactionChangeRequestHandler.dayBookStepCount]).hide();
												$(TransactionChangeRequestHandler.dayBookSteps[++TransactionChangeRequestHandler.dayBookStepCount]).show();
												if (TransactionChangeRequestHandler.dayBookStepCount == TransactionChangeRequestHandler.dayBookSteps.length) {
													if(!PageHandler.expanded){
														PageHandler.hidePageSelection();//this is to enlarge preview container on loading page
													}else{
														PageHandler.pageSelectionHidden =false;
														PageHandler.hidePageSelection();
													}
													$('#button-next').hide();
													$('#action-clear').hide();
													$('#button-save').show();
													$('#button-update').show();
													$.post('my-sales/transactions/change-transactions/day_book_change_transaction_preview.jsp',
																	'viewType=preview',
																	function(data) {
														$('#day-book-preview-container').css({'height' : '350px'});
														$('#day-book-preview-container').html(data);
														$('.table-field').css({"width":"800px"});
														$('.main-table').css({"width":"400px"});
														$('.inner-table').css({"width":"400px"});
														$('.display-boxes-colored').css({"width":"140px"});
														$('.display-boxes').css({"width":"255px"});
														$('#day-book-preview-container').show();
														TransactionChangeRequestHandler.expanded=false;
														$('#ps-exp-col').click(function() {
														if(TransactionChangeRequestHandler.dayBookStepCount == TransactionChangeRequestHandler.dayBookSteps.length){
															if(!PageHandler.expanded) {
														    	$('#day-book-preview-container').css({'height' : '350px'});
																$('#day-book-preview-container').html(data);
																$('.table-field').css({"width":"800px"});
																$('.main-table').css({"width":"400px"});
																$('.inner-table').css({"width":"400px"});
																$('.display-boxes-colored').css({"width":"140px"});
																$('.display-boxes').css({"width":"255px"});
																$('#day-book-preview-container').show();
																TransactionChangeRequestHandler.expanded=false;
														    }
														    else{
														    	$('#day-book-preview-container').css({'height' : '350px'});
																$('#day-book-preview-container').html(data);
																$('.table-field').css({"width":"662px"});
																$('.main-table').css({"width":"330px"});
																$('.inner-table').css({"width":"330px"});
																$('.display-boxes-colored').css({"width":"125px"});
																$('.display-boxes').css({"width":"200px"});
																$('#day-book-preview-container').show();
																TransactionChangeRequestHandler.expanded=true;
														    }
														}
														});
													});
												}
												if (TransactionChangeRequestHandler.dayBookStepCount > 0) {
													if(TransactionChangeRequestHandler.dayBookStepCount == 2){
														//Populating the allowances.
														TransactionChangeRequestHandler.populateAllowances();
													}
													//29-10-2013 enhancement to save product data only for specific step
													if(TransactionChangeRequestHandler.dayBookStepCount == 5){
														var listOfObjects = '';
														var isDaily = $('#isReturn').val();
														var productsRemarks = $('#productsCRRemarks').val();
														$('div#day-book-grid').each(function(index, value) {
															 var id = $(this).find('div').attr('id');
															//restricting productToFactory if he is non-daily employee.
															  if(isDaily != "false") {
																  $(this).find('.productToFactory').removeAttr('readonly');
															  } else {
																  $(this).find('.productToFactory').val('');
																  $(this).find('.productToFactory').attr('readonly','readonly');
															  }
												        	  var productName = $(this).find('#row1').html();
												        	  var batchNumber = $(this).find('#row7').html();
												        	  var openingStock = currencyHandler.convertStringPatternToNumber($(this).find('#row2').html());
												        	  var productToCustomer = currencyHandler.convertStringPatternToNumber($(this).find('#row3').html());
												        	  var productToFactory = currencyHandler.convertStringPatternToNumber($(this).find('.productToFactory').val());
												        	  var closingStock = currencyHandler.convertStringPatternToNumber($(this).find('#row5').html());
												        	  var returnQty = currencyHandler.convertStringPatternToNumber($(this).find('#row6').html());
														        	  if(index == 0){
														        		  listOfObjects +=productName+'|'+openingStock+'|'+productToCustomer+'|'+productToFactory+'|'+closingStock+'|'+returnQty+'|'+batchNumber;
														        	  }else{
														        		  listOfObjects +=','+productName+'|'+openingStock+'|'+productToCustomer+'|'+productToFactory+'|'+closingStock+'|'+returnQty+'|'+batchNumber;
														        	  }
													    });
												          $.post('dayBookCr.json','action=save-day-book-data&listOfProductObjects=' + listOfObjects+'&productsRemarks='+productsRemarks,function(obj) {
												          });
														PageHandler.hidePageSelection();
														  $('.page-link-strip').hide();
															$('.module-title').hide();
															$('.page-selection').animate( { width:"55px"}, 0,function(){});
															$('.page-container').animate( { width:"835px"}, 0);
															var thisTheme = PageHandler.theme;
															$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
													}
													$('#button-prev').show();
													$('.page-buttons').css('margin-left', '225px');
													
													if (TransactionChangeRequestHandler.dayBookStepCount == 4) {
														var isDaily = $('#isReturn').val();
														$('div#day-book-grid').each(function(index, value) {
															 var id = $(this).find('div').attr('id');
															//restricting productToFactory if he is non-daily employee.
															  if(isDaily != "false") {
																  $(this).find('.productToFactory').removeAttr('readonly');
															  } else {
																  $(this).find('.productToFactory').val('');
																  $(this).find('.productToFactory').attr('readonly','readonly');
															  }
														});
														PageHandler.hidePageSelection();
														  $('.page-link-strip').hide();
															$('.module-title').hide();
															$('.page-selection').animate( { width:"55px"}, 0,function(){});
															$('.page-container').animate( { width:"835px"}, 0);
															var thisTheme = PageHandler.theme;
															$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
													
															$('#ps-exp-col').click(function(){
																   if(PageHandler.expanded){
																	   $( '#search-results-list' ).css( "width", "697px" );
																	    $( '.report-header' ).css( "width", "697px" );
																    	$( '.report-body' ).css( "width", "697px" );
																		$( '.report-header-column2' ).css( "width", "80px" );
																		$( '.report-body-column2' ).css( "width", "80px" );
																		$( '.productToCustomer' ).css( "width", "95px" );
																		$( '.productToFactories' ).css( "width", "95px" );
																		TransactionChangeRequestHandler.expanded=false;
																   }else{
																	   $( '#search-results-list' ).css( "width", "830px" );
																	   $( '.report-header' ).css( "width", "830px" );
																    	$( '.report-body' ).css( "width", "830px" );
																		$( '.report-header-column2' ).css( "width", "100px" );
																		$( '.report-body-column2' ).css( "width", "100px" );
																		$( '.productToCustomer' ).css( "width", "100px" );
																		$( '.productToFatcory' ).css( "width", "1000px" );
																		TransactionChangeRequestHandler.expanded=true;
																   }
															   });
													}
												} else {
													$('#button-prev').hide();
													$('.page-buttons').css('margin-left', '200px');
												}
											},
										});
					});
			$('#button-prev').click(function() {
						$('#action-clear').show();
						if (TransactionChangeRequestHandler.dayBookStepCount == TransactionChangeRequestHandler.dayBookSteps.length) {
							$('#button-next').show();
							$('#button-save').hide();
							$('#button-update').hide();
							$('#day-book-preview-container').html('');
							$('#day-book-preview-container').hide();
							$('.page-buttons').css('margin-left', '150px');
						}
						$(TransactionChangeRequestHandler.dayBookSteps[TransactionChangeRequestHandler.dayBookStepCount]).hide();
						$(TransactionChangeRequestHandler.dayBookSteps[--TransactionChangeRequestHandler.dayBookStepCount]).show();
						if (TransactionChangeRequestHandler.dayBookStepCount > 0) {
							$('#button-prev').show();
							$('.page-buttons').css('margin-left', '150px');
							if (TransactionChangeRequestHandler.dayBookStepCount == 3) {
								$('.page-selection').animate( { width:"183px"}, 0,function(){
									$('.page-link-strip').show();
									$('.module-title').show();
								});
								$('.page-container').animate( { width:"702px"}, 0);
							}
							if (TransactionChangeRequestHandler.dayBookStepCount == 4) {
								PageHandler.hidePageSelection();
								  $('.page-link-strip').hide();
									$('.module-title').hide();
									$('.page-selection').animate( { width:"55px"}, 0,function(){});
									$('.page-container').animate( { width:"835px"}, 0);
									var thisTheme = PageHandler.theme;
									$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
							}
							if (TransactionChangeRequestHandler.dayBookStepCount == 2) {
								$('.page-selection').animate( { width:"183px"}, 0,function(){
									$('.page-link-strip').show();
									$('.module-title').show();
								});
								$('.page-container').animate( { width:"702px"}, 0);
							}
						} else {
							$('.page-selection').animate( { width:"183px"}, 0,function(){
								$('.page-link-strip').show();
								$('.module-title').show();
							});
							$('.page-container').animate( { width:"702px"}, 0);
							$('#button-prev').hide();
							$('.page-buttons').css('margin-left', '240px');
						}
					});
			$('#button-save').click(function() {
				var thisButton = $(this);
				var dayBookNo=$('#dayBookNo').val();
				var paramString = 'action=save-daybook&dayBookNo='+dayBookNo;
				PageHandler.expanded=false;
				pageSelctionButton.click();
				var isReturn = $('#isReturn').val();
				$('.page-content').ajaxSavingLoader();
				$.post('dayBookCr.json', paramString+'&isReturn='+isReturn, function(obj) {
					$.loadAnimation.end();
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
			});
			$('#action-cancel').click(function() {
			    $('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$.post('dayBookCr.json', 'action=remove-day-book-cr-session-data', 
									function(obj) {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/change-transactions/change_transactions_search.jsp";
			    			$(container).load(url);
							});
			    			$(this).dialog('close');
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			    return false;
			});
			$('#amountToFactory').blur(function() {
				var amountToFactory=currencyHandler.convertStringPatternToFloat($('#amountToFactory').val());
				var fomatAmountToFactory=currencyHandler.convertFloatToStringPattern(amountToFactory.toFixed(2));
				$('#amountToFactory').val(fomatAmountToFactory);
				var openingBalance =$('#allotStockAdvance').val();
				var totalAllowances = currencyHandler.convertStringPatternToFloat($('#totalAllowances').val());
				var customerTotalReceived = currencyHandler.convertStringPatternToFloat($('#customerTotalReceived').val());
				var amountToBank = currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val())
				var amounttoFactory = $('#amountToFactory').val();
				var sum = amountToBank + closingBalnce + amounttoFactory;
				var validResult = (currencyHandler.convertStringPatternToFloat(openingBalance)+customerTotalReceived)-(amountToBank+totalAllowances);
				if($('#isReturn').is(':checked')) {
			  if(parseFloat(validResult - amounttoFactory) < 0){
				showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
					result = false;
				}
				}
			});
			$('#amountToFactory').change(function() {
				TransactionChangeRequestHandler.calculateClosingBalance();
			});
		},
		LoadDayBookProductGridData: function(dayBookId,salesExecutive){
			 $('.green-results-list').html('');
			$.post('dayBookCr.json', 'action=get-day-book-grid-data&dayBookId='+dayBookId+'&salesExecutive='+salesExecutive, function(obj) {
				var data = obj.result.data;
				if(data != undefined){
					if(data.length>0) {
						var count=1;
						var productRemarks;
						for(var x=0;x<data.length;x=x+1) {
							var rowstr='<div class="ui-content report-content" id="day-book-grid">';
							rowstr=rowstr+'<div id="day-book-cr-row-'+count+'" class="report-body" style="width: 830px;height: auto; overflow: hidden;">';
							rowstr=rowstr+'<div id = "row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>';
							rowstr=rowstr+'<div id = "row7" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
							rowstr=rowstr+'<div id = "row2" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;text-align: right;">'+currencyHandler.convertNumberToStringPattern(data[x].openingStock)+'</div>';
							rowstr=rowstr+'<div id = "row6" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word; text-align: right;">'+currencyHandler.convertNumberToStringPattern(data[x].returnQty)+'</div>';
							rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned productToCustomer" style="height: inherit; width: 100px; word-wrap: break-word; text-align: right;">'+currencyHandler.convertNumberToStringPattern(data[x].productsToCustomer)+'</div>';
							rowstr=rowstr+'<div id="row4" class="report-body-column2 centered productToFactories" style="height: inherit; width: 100px; word-wrap: break-word; text-align: right;">'+'<input type = "text" style="margin-top:-3px; text-align: right;" class="productToFactory" id="productsToFactory" size=6px value='+currencyHandler.convertNumberToStringPattern(data[x].productsToFactory)+'>'+'</div>';
							rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word; text-align: right;">'+currencyHandler.convertNumberToStringPattern(data[x].closingStock)+'</div>';
							rowstr=rowstr+'</div></div>';
							$('.green-results-list').append(rowstr);
							if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
									$('#day-book-cr-row-'+count).each(function(index) {
								        $(this).children().height(100);
								    });    
								   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
											$('#day-book-cr-row-'+count).each(function(index) {
										        $(this).children().height(70);
										    });    
									}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
										$('#day-book-cr-row-'+count).each(function(index) {
									        $(this).children().height(55);
									    });   
								   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
									   $('#day-book-cr-row-'+count).each(function(index) {
									        $(this).children().height(50);
									    });   
						           }
						           else{
						        	   $('#day-book-cr-row-'+count).each(function(index) {
									        $(this).children().height(40);
									    });   
								   }
							count=count+1;	
							productRemarks = data[x].productsRemarks;
							}
						if(productRemarks != 'null'){
						$('textarea#productsCRRemarks').html(productRemarks);
						}else{
							$('textarea#productsCRRemarks').html('');
						}
						$('#originalDayBookCRProductRemarks').val(productRemarks);
					} else {
					$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
				}
				}else {
				$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
				$('.gridDisplay').show();
			}
		});
		},
		calculateClosingBalance : function() {
				var openingBalance = currencyHandler.convertStringPatternToFloat($('#allotStockAdvance').val());
				var amountToBank = currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var amountToFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val());
				if(amountToBank == "") {
					amountToBank = 0.00;
				}
				if(amountToFactory == "") {
					amountToFactory = 0.00;
				}
				var customerTotalReceived = currencyHandler.convertStringPatternToFloat($('#customerTotalReceived').val());
				var totalAllowances = currencyHandler.convertStringPatternToFloat($('#totalAllowances').val());
				var value1 = parseFloat(openingBalance) + parseFloat(customerTotalReceived);
				var value2 = parseFloat(totalAllowances) + parseFloat(amountToBank) + parseFloat(amountToFactory);
				var closingBalanceVal = value1 - value2;
				if(isNaN(closingBalanceVal)) {
					closingBalanceVal = 0.00;
				}
				if($('#isReturn').is(':checked')) {
					if(value1 - (amountToFactory + parseFloat(amountToBank + totalAllowances)) < 0){
						var validResult = (openingBalance + customerTotalReceived)-(amountToBank + totalAllowances);
						showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
					}else{
						$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceVal.toFixed(2)));
					}
				} else {
					$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceVal.toFixed(2)));
				}
		},
		checkSEAllotmentType: function(isDaily){
				if(isDaily == 'true') {
					$('#presentDate').hide();
					$('#reportingManagerLabel').show();
					$('#reportingManager').attr('class','mandatory');
					$('#isReturn').attr('checked', 'checked');
					$('#isReturn').attr("value", true);
					$('#isReturn').attr("disabled", true);
				} else {
					$('#amountToFactory').hide();
					$('.amountToFactory').hide();
					$('#presentDate').show();
					$('#reportingManagerLabel').hide();
					$('#isReturn').removeAttr("disabled");
					$('#isReturn').attr("value", false);
				}
				$('#isReturn').change(function() {
					if($('#isReturn').is(':checked')) {
						$('#reportingManagerLabel').show();
						$('#reportingManager').attr('class','mandatory');
						$('#reportingManager').val("");
						$('#amountToFactory').show();
						$('.amountToFactory').show();
						$('#isReturn').attr("value", true);
					} else {
						$('#reportingManagerLabel').hide();
						$('#reportingManager').removeAttr('class','mandatory');
						$('#amountToFactory').hide();
						$('#amountToFactory').val("");
						$('#reportingManager').val("");
						$('.amountToFactory').hide();
						$('#isReturn').attr("value", false);
					}
				});
		},
		dayBookLoad:function() {
			var titleVal;
			$('#totalAllowances').focus(function() {
				var executiveAllowances = currencyHandler.convertStringPatternToFloat($('#executiveAllowances').val());
				if(executiveAllowances == "") {
					executiveAllowances = 0;
				}
				var driverAllowances = currencyHandler.convertStringPatternToFloat($('#driverAllowances').val());
				if(driverAllowances == "") {
					driverAllowances = 0;
				}
				var vehicleFuelExpenses = currencyHandler.convertStringPatternToFloat($('#vehicleFuelExpenses').val());
				if(vehicleFuelExpenses == "") {
					vehicleFuelExpenses = 0;
				}
				var vehicleMaintenanceExpenses = currencyHandler.convertStringPatternToFloat($('#vehicleMaintenanceExpenses').val());
				if(vehicleMaintenanceExpenses == "") {
					vehicleMaintenanceExpenses = 0;
				}
				var offloadingLoadingCharges = currencyHandler.convertStringPatternToFloat($('#offloadingLoadingCharges').val());
				if(offloadingLoadingCharges == "") {
					offloadingLoadingCharges = 0;
				}
				var dealerPartyExpenses = currencyHandler.convertStringPatternToFloat($('#dealerPartyExpenses').val());
				if(dealerPartyExpenses == "") {
					dealerPartyExpenses = 0;
				}
				var muncipalCityCouncil = currencyHandler.convertStringPatternToFloat($('#municipalCityCouncil').val());
				if(muncipalCityCouncil == "") {
					muncipalCityCouncil = 0;
				}
				var miscellaneousExpenses = currencyHandler.convertStringPatternToFloat($('#miscellaneousExpenses').val());
				if(miscellaneousExpenses == "") {
					miscellaneousExpenses = 0;
				}
				var totalAllowancesVal = parseFloat(executiveAllowances)+parseFloat(driverAllowances)+parseFloat(vehicleFuelExpenses)+parseFloat(vehicleMaintenanceExpenses)+
				parseFloat(offloadingLoadingCharges)+parseFloat(dealerPartyExpenses)+parseFloat(muncipalCityCouncil)+parseFloat(miscellaneousExpenses);
				$('#totalAllowances').val(currencyHandler.convertFloatToStringPattern(totalAllowancesVal.toFixed(2)));
			});
			
			$('.green-results-list').live("blur",function(){
				var listOfObjects = '';
		          $.each($('.report-body'), function(index, value) {
		        	  var productName = $(this).find('#row1').html();
		        	  var openingStock = currencyHandler.convertStringPatternToNumber($(this).find('#row2').html());
		        	  var productToCustomer = currencyHandler.convertStringPatternToNumber($(this).find('#row3').html());
		        	  var productToFactory = currencyHandler.convertStringPatternToNumber($(this).find('.productToFactory').val());
		        	  if(productToFactory == "") {
		        		  productToFactory = 0;
		        	  }
		        	  var closingStock =parseInt(openingStock) - (parseInt(productToCustomer) + parseInt(productToFactory)) ;
						if(isNaN(closingStock)) {
							closingStock = 0;
				          }
						if(closingStock < 0 ){
							showMessage({title:'Error', msg:'Products to factory should not be greater than closing stock'});
							check = false;
							return check;
						}else{
				         $(this).find('#row5').html(currencyHandler.convertNumberToStringPattern(closingStock));
				         $(this).find('.productToFactory').val(currencyHandler.convertNumberToStringPattern(productToFactory));
				         check = true;
						 return check;
						}
			    });
			});
			
			//adding Cash Day Book CR functionality in Cash Day Book CR
			
			$('#executiveAllowancesCRLink').click(function() {
				var dayBookType = "Executive Allowances";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#driverAllowancesCRLink').click(function(){
				var dayBookType = "Driver Allowances";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#vehicleFuelExpensesCRLink').click(function(){
				var dayBookType = "Vehicle Fuel Expenses";
				titleVal = dayBookType+" Details";
				var salesBookId = $('#salesBookId').val();
				var d = $("#cr-executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				$.post('dayBookCr.json','action=get-allowances-details&dayBookType='+dayBookType+'&salesBookId='+salesBookId,function(obj){
					var result = obj.result.data;
					$("#cr-executive-allowances-view-dialog").dialog('open');
					if(result.length != 0){
						var allowancesTrack="";
						allowancesTrack +='<div class="report-header" style="width: 730px; height: 30px;">'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 200px;">'+ Msg.DAY_BOOK_METER_READING+'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
						'</div>';
			            $('#cr-executive-allowances-view-container').append(allowancesTrack);
			            $('#cr-executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 730px; overflow-y:initial;"></div>'); 
						for(var loop=1; loop<=result.length; loop=loop+1) {
							 var vehicleExpenses = currencyHandler.convertStringPatternToFloat(result[loop-1].vehicleFuelExpenses);
							var allowancesTrackRows ='<div class="ui-content report-content">'+
							'<div id='+ loop+' class="report-body" style="width: 730px; height: 30px; overflow: hidden; line-height:20px;">'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row1"  style="height: inherit; width: 100px; word-wrap: break-word;">' + '<input class = "col1 read-only" type = "text" style="width:100px;" readonly="readonly" value='+ loop+ '></div>' +
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern(vehicleExpenses.toFixed(2))+'></div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ result[loop-1].createdOn +'</div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="height: inherit; width: 200px; word-wrap: break-word;">'+'<input class = "col4 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+ result[loop-1].vehicleMeterReading+'></div>'+
							'<a id="'+result[loop-1].id+'" onclick="TransactionChangeRequestHandler.allowanceEdit(\'' +result[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px;"></a>'+
							'<a id="'+result[loop-1].id+'" onclick="TransactionChangeRequestHandler.changedPersistAllowancesDayBookCR(\'' +result[loop-1].id+'\',\'' +dayBookType+'\',\'' +result[loop-1].salesBookId+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px; display:none;"></a>'+
							'</div>'+
							'</div>';
							$('.grid-content').append(allowancesTrackRows);
						}
					}
					 else {
						 $('#cr-executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
						}
				});
			
			});
			$('#offloadingChargesCRLink').click(function(){
				var dayBookType = "Offloading Charges";
				titleVal = dayBookType+" Details";
				var salesBookId = $('#salesBookId').val();
				var d = $("#cr-executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				$.post('dayBookCr.json','action=get-allowances-details&dayBookType='+dayBookType+'&salesBookId='+salesBookId, function(obj){
					var result = obj.result.data;
					$("#cr-executive-allowances-view-dialog").dialog('open');
					if(result.length != 0){
						var allowancesTrack="";
						allowancesTrack +='<div class="report-header" style="width: 830px; height: 30px;">'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 300px;">'+ Msg.DAY_BOOK_BUSINESS_NAME+'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
						'</div>';
			            $('#cr-executive-allowances-view-container').append(allowancesTrack);
			            $('#cr-executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
						for(var loop=1; loop<=result.length; loop=loop+1) {
							var offloadingCharges = currencyHandler.convertStringPatternToFloat(result[loop-1].offLoadingCharges );
							var allowancesTrackRows ='<div class="ui-content report-content">'+
							'<div id='+loop+' class="report-body" style="width: 830px; height: 30px; overflow: hidden; line-height:20px;">'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 100px; word-wrap: break-word;">' +  loop  + '</div>' +
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern( offloadingCharges.toFixed(2))+'></div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ result[loop-1].createdOn +'</div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="height: inherit; width: 300px; word-wrap: break-word;">'+'<input class = "col4 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+result[loop-1].businessName+'></div>'+
							'<a id="'+result[loop-1].id+'" onclick="TransactionChangeRequestHandler.allowanceEdit(\'' +result[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px;"></a>'+
							'<a id="'+result[loop-1].id+'" onclick="TransactionChangeRequestHandler.changedPersistAllowancesDayBookCR(\'' +result[loop-1].id+'\',\'' +dayBookType+'\',\'' +result[loop-1].salesBookId+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px; display:none;"></a>'+
							'</div>'+
							'</div>';
							$('.grid-content').append(allowancesTrackRows);
						}
					}
					 else {
						 $('#cr-executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
						}
				});
			});
			$('#vehicleMaintenanceExpensesCRLink').click(function(){
				var dayBookType = "Vehicle Maintenance Expenses";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#municipalCityCouncilCRLink').click(function(){
				var dayBookType = "Municipal City Council";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#miscellaneousExpensesCRLink').click(function(){
				var dayBookType = "Miscellaneous Expenses";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#dealerPartyExpensesCRLink').click(function(){
				var dayBookType = "Dealer Party Expenses";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$('#amountToBankCRLink').click(function(){
				var dayBookType = "Deposit Amount";
				TransactionChangeRequestHandler.trackAllowanceData(dayBookType);
			});
			$("#cr-executive-allowances-view-dialog").dialog({
				autoOpen: false,
				height: 455,
				width: 850,
				title :titleVal,
				modal: true,
				buttons: {
					Close: function() {
						//Populating the allowances.
						TransactionChangeRequestHandler.populateAllowances();
						$(this).dialog('close');
					}
				},
				close: function() {
					$('#cr-executive-allowances-view-container').html('');
				}
			});
		},
		persistOriginalCashDayBookDataCR : function(salesBookId){
			$.post('dayBookCr.json', 'action=get-original-day-book-cash-details&salesBookId='+salesBookId, function(obj) {
			});
		},
		//display in preview page onclick of link
		getAmountToBankData : function(){
			var dayBookType = "Deposit Amount";
			var salesBookId = $('#salesBookId').val();
			titleVal = dayBookType+" Details";
			$.post('dayBookCr.json', 'action=get-allowances-details&dayBookType='+dayBookType+'&salesBookId='+salesBookId, function(obj) {
				var data = obj.result.data;
				var d = $("#cr-executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				if(data !='') {
					var allowancesTrack="";
					allowancesTrack +='<div class="report-header" style="width: 830px; height: 30px;">'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 300px;id="remarks"">' +Msg.DAY_BOOK_REMARKS +'</div>'+
					'</div>';
					$('#cr-executive-allowances-view-container').html('');
					$('#cr-executive-allowances-view-container').append(allowancesTrack);
					$('#cr-executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
					for(var loop=1; loop<=data.length; loop=loop+1) {
						if( data[loop-1].remarks == "null"){
							 data[loop-1].remarks ="";
						}
						var allowancesAmount =currencyHandler.convertStringPatternToFloat( data[loop-1].executiveAllowances);
						var allowancesTrackRows ='<div class="ui-content report-content">'+
						'<div id='+loop+' class="report-body" style="width: 830px; height: 30px; overflow: hidden; line-height:20px;">'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 100px; word-wrap: break-word;">' +  loop  + '</div>' +
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" id =" amount " type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern(allowancesAmount.toFixed(2))+'></div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ data[loop-1].createdOn +'</div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="height: inherit; width: 300px; word-wrap: break-word;" id="remarksVal">'+'<textarea rows="1" cols="2"  class = "col4 read-only" style="height: 41px; width: 85px; resize: none;" readonly="readonly">'+data[loop-1].remarks+'</textarea></div>'+
						'</div>'+
						'</div>';
						$('.grid-content').append(allowancesTrackRows);
					}
				} else {
					$('#cr-executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
				}
			});
		return titleVal;
	    },
		trackAllowanceData : function(dayBookType){
			titleVal = dayBookType+" Details";
			var salesBookId = $('#salesBookId').val();
			$.post('dayBookCr.json', 'action=get-allowances-details&dayBookType='+dayBookType+'&salesBookId='+salesBookId, function(obj) {
				var data = obj.result.data;
				//$("#cr-executive-allowances-view-dialog").dialog('open');
				var d = $("#cr-executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				if(data !='') {
					var allowancesTrack="";
					allowancesTrack +='<div class="report-header" style="width: 830px; height: 30px;">'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 300px;id="remarks"">' +Msg.DAY_BOOK_REMARKS +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
					'</div>';
		            $('#cr-executive-allowances-view-container').append(allowancesTrack);
		            $('#cr-executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
					for(var loop=1; loop<=data.length; loop=loop+1) {
						if( data[loop-1].remarks == "null"){
							 data[loop-1].remarks ="";
						}
						var allowancesAmount =currencyHandler.convertStringPatternToFloat( data[loop-1].executiveAllowances);
						var allowancesTrackRows ='<div class="ui-content report-content">'+
						'<div id='+loop+' class="report-body" style="width: 830px; height: 30px; overflow: hidden; line-height:20px;">'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 100px; word-wrap: break-word;">' +  loop  + '</div>' +
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern(allowancesAmount.toFixed(2))+'></div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ data[loop-1].createdOn +'</div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="width: 301px;border-right: 1px solid #9BCCF2; word-wrap: break-word;" id="remarksVal">'+'<textarea rows="1" cols="2"  class = "col4 read-only" style="min-height: 15px;margin-top:-6px; overflow-y:auto ;width: 285px; resize: none;" readonly="readonly">'+data[loop-1].remarks+'</textarea></div>'+
						'<a id="'+data[loop-1].id+'" onclick="TransactionChangeRequestHandler.allowanceEdit(\'' +data[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px;"></a>'+
						'<a id="'+data[loop-1].id+'" onclick="TransactionChangeRequestHandler.changedPersistAllowancesDayBookCR(\'' +data[loop-1].id+'\',\'' +dayBookType+'\',\'' +data[loop-1].salesBookId+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px; display:none;"></a>'+
						'</div>'+
						'</div>';
						$('.grid-content').append(allowancesTrackRows);
					}
				} else {
					$('#cr-executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
				}
			});
			return titleVal;
		},
		populateAllowances : function() {
			var salesBookId = $('#salesBookId').val();
			$.post('dayBookCr.json', 'action=get-allowances'+'&salesBookId='+salesBookId, function(obj) {
				var data = obj.result.data;
				var executiveAllowancesVal = "0.00";
				var driverAllowancesVal = "0.00";
				var vehicleFuelExpensesVal = "0.00";
				var vehicleMaintenanceExpensesVal = "0.00";
				var offloadingLoadingChargesVal = "0.00";
				var dealerPartyExpensesVal = "0.00";
				var miscellaneousExpensesVal = "0.00";
				var municipalCityCouncilVal = "0.00";
				if(data != undefined) {
					for(var i=0; i < data.length; i++) {
						if(data[i].dayBookType == "Deposit Amount") {
							$('#amountToBank').val(data[i].amountToBank);
							$('#reasonAmountToBank').val(data[i].reasonAmountToBank);
							TransactionChangeRequestHandler.calculateClosingBalance();
						} else if(data[i].dayBookType == "Vehicle Details") {
							$('#vehicleNo').val(data[i].vehicleNo);
							$('#driverName').val(data[i].driverName);
							$('#startReading').val(data[i].startingReading);
						} else if("Executive Allowances" == data[i].dayBookType){
							if(data[i].executiveAllowances == undefined) {
								executiveAllowancesVal = executiveAllowancesVal;
							} else {
								executiveAllowancesVal = currencyHandler.convertStringPatternToFloat(executiveAllowancesVal) + currencyHandler.convertStringPatternToFloat(data[i].executiveAllowances);
								executiveAllowancesVal = currencyHandler.convertFloatToStringPattern(executiveAllowancesVal);
							}
						} else if("Driver Allowances" == data[i].dayBookType) {
							if(data[i].driverAllowances == undefined) {
								driverAllowancesVal = driverAllowancesVal;
							} else {
								driverAllowancesVal = currencyHandler.convertStringPatternToFloat(driverAllowancesVal) + currencyHandler.convertStringPatternToFloat(data[i].driverAllowances);
								driverAllowancesVal = currencyHandler.convertFloatToStringPattern(driverAllowancesVal);
							}
						} else if("Offloading Charges" == data[i].dayBookType) {
							if(data[i].offLoadingCharges == undefined) {
								offloadingLoadingChargesVal = offloadingLoadingChargesVal;
							} else {
								offloadingLoadingChargesVal = currencyHandler.convertStringPatternToFloat(offloadingLoadingChargesVal) + currencyHandler.convertStringPatternToFloat(data[i].offLoadingCharges);
								offloadingLoadingChargesVal = currencyHandler.convertFloatToStringPattern(offloadingLoadingChargesVal);
							}
						} else if("Vehicle Maintenance Expenses" == data[i].dayBookType) {
							if(data[i].vehicleMaintenanceExpenses == undefined) {
								vehicleMaintenanceExpensesVal = vehicleMaintenanceExpensesVal;
							} else {
								vehicleMaintenanceExpensesVal = currencyHandler.convertStringPatternToFloat(vehicleMaintenanceExpensesVal) + currencyHandler.convertStringPatternToFloat(data[i].vehicleMaintenanceExpenses);
								vehicleMaintenanceExpensesVal = currencyHandler.convertFloatToStringPattern(vehicleMaintenanceExpensesVal);
							}
						} else if("Miscellaneous Expenses" == data[i].dayBookType) {
							if(data[i].miscellaneousExpenses == undefined) {
								miscellaneousExpensesVal = miscellaneousExpensesVal;
							} else {
								miscellaneousExpensesVal = currencyHandler.convertStringPatternToFloat(miscellaneousExpensesVal) + currencyHandler.convertStringPatternToFloat(data[i].miscellaneousExpenses);
								miscellaneousExpensesVal = currencyHandler.convertFloatToStringPattern(miscellaneousExpensesVal);
								$('#reasonMiscellaneousExpenses').val(data[i].reasonMiscellaneousExpenses);
							}
						} else if("Vehicle Fuel Expenses" == data[i].dayBookType) {
							if(data[i].vehicleFuelExpenses == undefined) {
								vehicleFuelExpensesVal = vehicleFuelExpensesVal;
							} else {
								vehicleFuelExpensesVal = currencyHandler.convertStringPatternToFloat(vehicleFuelExpensesVal) + currencyHandler.convertStringPatternToFloat(data[i].vehicleFuelExpenses);
								vehicleFuelExpensesVal = currencyHandler.convertFloatToStringPattern(vehicleFuelExpensesVal);
							}
						} else if("Dealer Party Expenses" == data[i].dayBookType) {
							if(data[i].dealerPartyExpenses == undefined) {
								dealerPartyExpensesVal = dealerPartyExpensesVal;
							} else {
								dealerPartyExpensesVal = currencyHandler.convertStringPatternToFloat(dealerPartyExpensesVal) + currencyHandler.convertStringPatternToFloat(data[i].dealerPartyExpenses);
								dealerPartyExpensesVal = currencyHandler.convertFloatToStringPattern(dealerPartyExpensesVal);
								$('#reasonDealerPartyExpenses').val(data[i].reasonDealerPartyExpenses);
							}
						} else if("Municipal City Council" == data[i].dayBookType) {
							if(data[i].municipalCityCouncil == undefined) {
								municipalCityCouncilVal = municipalCityCouncilVal;
							} else {
								municipalCityCouncilVal = currencyHandler.convertStringPatternToFloat(municipalCityCouncilVal) + currencyHandler.convertStringPatternToFloat(data[i].municipalCityCouncil);
								municipalCityCouncilVal = currencyHandler.convertFloatToStringPattern(municipalCityCouncilVal);
							}
						}
							$('#executiveAllowances').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(executiveAllowancesVal).toFixed(2)));
							$('#driverAllowances').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(driverAllowancesVal).toFixed(2)));
							$('#vehicleFuelExpenses').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(vehicleFuelExpensesVal).toFixed(2)));
							$('#offloadingLoadingCharges').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(offloadingLoadingChargesVal).toFixed(2)));
							$('#vehicleMaintenanceExpenses').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(vehicleMaintenanceExpensesVal).toFixed(2)));
							$('#miscellaneousExpenses').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(miscellaneousExpensesVal).toFixed(2)));
							$('#dealerPartyExpenses').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(dealerPartyExpensesVal).toFixed(2)));
							$('#municipalCityCouncil').val(currencyHandler.convertFloatToStringPattern(
									currencyHandler.convertStringPatternToFloat(municipalCityCouncilVal).toFixed(2)));
						}
				}
				var exAllowances = currencyHandler.convertStringPatternToFloat($('#executiveAllowances').val());
				var drAllowances = currencyHandler.convertStringPatternToFloat($('#driverAllowances').val());
				var veAllowances = currencyHandler.convertStringPatternToFloat($('#vehicleFuelExpenses').val());
				var offCharges = currencyHandler.convertStringPatternToFloat($('#offloadingLoadingCharges').val());
				var veMaintainCharges = currencyHandler.convertStringPatternToFloat($('#vehicleMaintenanceExpenses').val());
				var misExpenses = currencyHandler.convertStringPatternToFloat($('#miscellaneousExpenses').val());
				var dealerExpenses = currencyHandler.convertStringPatternToFloat($('#dealerPartyExpenses').val());
				var muncipalExpenses = currencyHandler.convertStringPatternToFloat($('#municipalCityCouncil').val());
				var totalAllowancesVal = exAllowances+drAllowances+veAllowances+veMaintainCharges+offCharges+dealerExpenses+muncipalExpenses+misExpenses;
				$('#totalAllowances').val(currencyHandler.convertFloatToStringPattern(totalAllowancesVal.toFixed(2)));
			});
		},
		//Allowances edit related functionality.
		allowanceEdit : function(id) {
			var mainId = $('#'+id).parent().attr('id');
			$('#'+mainId).children('.row2').children('.col2').removeAttr('readonly');
			if($('#cr-executive-allowances-view-dialog').dialog("option", "title") != "Offloading Charges Details"){
				$('#'+mainId).children('.row4').children('.col4').removeClass('read-only');
				$('#'+mainId).children('.row4').children('.col4').removeAttr('readonly');
			}
			$('#'+mainId).children('.row2').children('.col2').removeClass('read-only');
			$('#'+mainId).children('.row5').hide();
			$('#'+mainId).children('.row6').show();
			var valueOne = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row2').children('.col2').val());
			$('#'+mainId).children('.row2').children('.col2').val(currencyHandler.convertFloatToStringPattern(valueOne.toFixed(2)));
			TransactionChangeRequestHandler.checkAmountAvailability(id,valueOne);
		},
		
		checkAmountAvailability : function(id,valueOne){
			var amountToFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val());
			var mainId = $('#'+id).parent().attr('id');
			var check;
			var currentValue ;
			prevVal = valueOne;
			$('#'+mainId).children('.row2').children('.col2').change(function(){
				currentValue = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row2').children('.col2').val());
				 $.ajax({type: "POST",
				url:'dayBookCr.json', 
				data: 'action=check-available-deposit-amount',
				async : false,
				success: function(data){
					amount = data.result.data;
					if($('#closingBalance').val() == 0){
						if( currentValue > Number(amount)){
							if($('#cr-executive-allowances-view-dialog').dialog("option", "title") == "Deposit Amount Details"){
								showMessage({title:'Error',msg:'You Can Deposit Upto ' +parseFloat(amount).toFixed(2)+' Only'});
							}else{
								showMessage({title:'Error',msg:'You Can Enter Upto ' +parseFloat(amount).toFixed(2)+' Only'});
							}
						}
					}else{
						 if(currentValue > Number(amount)+Number(prevVal)- Number(amountToFactory)){
								var resVal = Number(amount) + Number(prevVal) - Number(amountToFactory);
							if($('#cr-executive-allowances-view-dialog').dialog("option", "title") == "Deposit Amount Details"){
								showMessage({title:'Error',msg:'You Can Deposit '+resVal+' Only'});
							}else{
								showMessage({title:'Error',msg:'You Can Deposit '+resVal+' Only'});
							}
						}
					  }
			       }
				});
			
			});

		}, 
		
		//Allowances update related functionality.
		changedPersistAllowancesDayBookCR : function(id,dayBookType,salesBookId) {
			var amountToFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val());
			var mainId = $('#'+id).parent().attr('id');
				var valueOne = $('#'+mainId).children('.row2').children('.col2').val();
				var formatValueOne=currencyHandler.convertStringPatternToFloat(valueOne);
				var valueThree;
				if ("Vehicle Fuel Expenses" == dayBookType) {
					valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
				} else {
					valueThree = $('#'+mainId).children('.row4').children('.col4').val();
				}
				if($('#cr-executive-allowances-view-dialog').dialog("option", "title") == "Vehicle Fuel Expenses Details"){
					var startingReading ;
					var endingReading;
					var nextMainId = Number(mainId)+Number(1);
						var Sno = $('#'+mainId).children('.row1').children('.col1').val();
					if(Sno == 1){
							startingReading = $('#startReading').val();
							endingReading = $('#'+nextMainId).children('.row4').children('.col4').val();
							if(typeof endingReading == 'undefined' || endingReading == undefined){
								endingReading = $('#endingReading').val();
							}
					}else{
							valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
							startingReading = currencyHandler.convertStringPatternToFloat($('#'+Number(mainId-1)).children('.row4').children('.col4').val());
							endingReading =$('#'+nextMainId).children('.row4').children('.col4').val();
							if(typeof endingReading == 'undefined' || endingReading == undefined){
								endingReading = $('#endingReading').val();
							}
					}
					if(TransactionChangeRequestHandler.validateMeterreading(valueThree,id) == false){
							showMessage({title:'Message', msg:'Meter Reading should greater than '+ startingReading +'  And should be less than  '+ endingReading +''});
							event.preventDefault();
							return false;
					}
				}
				
				$.ajax({type: "POST",
					url:'dayBookCr.json', 
					data: 'action=check-available-deposit-amount',
					async : false,
					success: function(data){
						amount = data.result.data;
						if($('#closingBalance').val() == 0){
							if(valueOne > Number(amount)+Number(prevVal)){
								var resVal = Number(amount)+Number(prevVal);
								if($('#cr-executive-allowances-view-dialog').dialog("option", "title") == "Deposit Amount Details"){
									showMessage({title:'Error',msg:'You Can Deposit '+resVal+' Only'});
								}else{
									showMessage({title:'Error',msg:'You Can Deposit '+resVal+' Only'});
								}
							}else{
								TransactionChangeRequestHandler.updateAllowances(id,formatValueOne,valueThree,dayBookType,salesBookId);
							}
						}else{
							 if(valueOne > Number(amount)+Number(prevVal)- Number(amountToFactory)){
									var resVal = Number(amount) + Number(prevVal) - Number(amountToFactory);
								if($('#cr-executive-allowances-view-dialog').dialog("option", "title") == "Deposit Amount Details"){
									showMessage({title:'Error',msg:'You Can Deposit '+resVal+' Only'});
								}else{
									showMessage({title:'Error',msg:'You Can Enter Upto ' +resVal+' Only'});
								}
							}else{
								TransactionChangeRequestHandler.updateAllowances(id,formatValueOne,valueThree,dayBookType,salesBookId);
							}
						}
				   }
					});
				
		},
		validateMeterreading : function(meterReading,id){
			var prevMeterReading = meterReading
			var currentMeterReading = $('#'+mainId).children('.row4').children('.col4').val(); 
			var mainId = $('#'+id).parent().attr('id');
			var res = $('#'+id).parent().attr('class');
			$('.col4').change(function(){
				
				
			});
			
			var startingReading ;
			var endingReading;
			var nextMainId = Number(mainId)+Number(1);
				var Sno = $('#'+mainId).children('.row1').children('.col1').val();
			if(Sno == 1){
				startingReading = $('#startReading').val();
				endingReading =$('#'+nextMainId).children('.row4').children('.col4').val();
			}else{
				valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
				startingReading = currencyHandler.convertStringPatternToFloat($('#'+Number(mainId-1)).children('.row4').children('.col4').val());
				if(typeof endingReading != 'undefined'){
					endingReading = currencyHandler.convertStringPatternToFloat($('#'+nextMainId).children('.row4').children('.col4').val());
				}else{
					endingReading = $('#endingReading').val();
				}
			}
			var check;
			if(Number(meterReading) < Number(startingReading) || Number(meterReading) > Number(endingReading) ||Number(meterReading) ==$('#'+nextMainId).children('.row4').children('.col4').val()||Number(meterReading) == Number(startingReading)){
				check = false;
			}else{
				check = true;
			}
			return check;
		},
		
		updateAllowances : function(id,formatValueOne,valueThree,dayBookType,salesBookId){
			var mainId = $('#'+id).parent().attr('id');
			$.post('dayBookCr.json', 'action=update-changed-allowance&id='+id+'&valueOne='+formatValueOne+'&valueThree='+valueThree, function(obj) {
				$('#'+mainId).children('.row2').children('.col2').attr('readonly', 'readonly');
				$('#'+mainId).children('.row4').children('.col4').attr('readonly', 'readonly');
				$('#'+mainId).children('.row2').children('.col2').addClass('read-only');
				$('#'+mainId).children('.row4').children('.col4').addClass('read-only');
				$('#'+mainId).children('.row5').show();
				$('#'+mainId).children('.row6').hide();
			});
		},
		
		//Journal step-by-step
		initAddJournalCRButtons : function(){
			TransactionChangeRequestHandler.journalCRStepCount = 0;
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							var originalJournalAmount=$('#originalJournalAmount').val();
							var originalInvoiceName=$('#originalInvoiceName').val();
							var originalJournalDescription=$('#originalJournalDescription').val();
							$('#description').val(originalJournalDescription);
							$('#amount').val(currencyHandler.convertFloatToStringPattern(originalJournalAmount));
							$('#invoiceName').val(originalInvoiceName);
							$('#crDescription').val('');
							$(this).dialog('close');
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			    return false;
			});
			$('#button-save').click(function() {
				if(ValidateChangeTransaction.validateJournals()==false){
					return true;
				}
				var thisButton = $(this);
				var paramString = $('#edit-journal-form').serializeArray();
				var amount=$('#amount').val();
				var formatAmount=currencyHandler.convertStringPatternToFloat(amount);
				$.each(paramString, function(i, formData) {
				    if(formData.value === amount){ 
				    	formData.value = currencyHandler.convertStringPatternToFloat(amount);
				    }
				});
				var sendFormData=$.param(paramString);
				var journalType=$('#journalType').val();
				var invoiceNo=$('#invoiceNo').val();
				var businessName=$('#businessName').val();
				var description=$('#description').val();
				var crDescription=$('#crDescription').val();
				var invoiceName=$('#invoiceName').val();
				var createdOn=$('#createdOn').val();
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$('.page-content').ajaxSavingLoader();
				$.post('journalCr.json', sendFormData+'&businessName='+businessName+'&invoiceNo='+invoiceNo+'&journalType='+journalType+'&amount='+formatAmount+'&invoiceName='+invoiceName+'&description='+description+'&crDescription='+crDescription, function(obj) {
					 $.loadAnimation.end();
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
		});
			$('#amount').blur(function() {
				var journalAmount=currencyHandler.convertStringPatternToFloat($('#amount').val());
				var formatJournalAmount=currencyHandler.convertFloatToStringPattern(journalAmount.toFixed(2));
				$('#amount').val(formatJournalAmount);
			});
		    $('#action-cancel').click(function() {
				$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
				$("#error-message").dialog({	
					resizable : false,
					height : 140,
					title : "<span class='ui-dlg-confirm'>Confirm</span>",
					modal : true,
					buttons : {
						'Ok' : function() {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/change-transactions/change_transactions_search.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						Cancel : function() {
							$(this).dialog('close');
						}
					}
				});
				return false;
		});
	},
  };