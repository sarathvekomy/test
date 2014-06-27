var TransactionHandler = {
	initPageLinks : function() {
		$('#add-transaction').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/delivery_note_add.jsp'
		});
		$('#add-sales-returns').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/sales_returns_add.jsp'
		});
		$('#customer-wise-report').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/reports/customerwise_report_show.jsp'
		});
		$('#product-wise-report').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/reports/productwise_report_show.jsp'
		});
		$('#add-journal').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/add_journals.jsp'
		});
		$('#my-alerts').pageLink({
			container : '.transaction-page-container',
			url : 'accounts/alerts/my_alert_history.jsp'
		})

	}
};

var DeliveryNoteHandler = {
		theme: "",
		expanded: true,
	dataForProduct : '',
	deliverynoteSteps : [ '#deliverynote-form', '#deliverynote-product-form' ],
	deliverynoteUrl : [ 'deliveryNote.json', 'deliveryNote.json' ],
	deliverynoteStepCount : 0,
	initDeliveryNotePageSelection: function(){
	   PageHandler.hidePageSelection();	
	},
	customerChangeRequest : function() {
		$('#addCustomer').click(function() {
			PageHandler.pageSelectionHidden =false;
			PageHandler.hidePageSelection();
			$('.transaction-page-container').load('customer/customer_change_request_add.jsp');
		});
	},
	initAddButtons : function() {
		DeliveryNoteHandler.deliverynoteStepCount = 0;
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			};
		$('#action-clear').click(function() {
			var previousCredit = $('#previousCredit').val();
			var presentPayable = $('#presentPayable').val();
			var totalPayable = $('#totalPayable').val();
			var advance = $('#presentAdvance').val();
			var balance = $('#balance').val();
			var paymentType = $('#paymentType').val();
			$('#paymentTypeValid').empty();
			$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).clear();
						$('#previousCredit').val(previousCredit);
						$('#presentPayable').val(presentPayable);
						$('#totalPayable').val(totalPayable);
						 $('#presentAdvance').val(advance);
						 $('#balance').val(balance);
						 if(paymentType == 'NA'){
							 $('#paymentType').val(paymentType).attr('selected');
						 }
						$(this).dialog('close');
					},
				Cancel: function() {
					$(this).dialog('close');
			}
				}
			});
		});
		
		$('#button-next').click(function() {
			var totalCost=$('#totalCost').val();
			var resultSuccess=true;
			var resultFailure=false;
			if(DeliveryNoteHandler.deliverynoteStepCount==0){
				if(ValidateDeliveryNoteHandler.validatein()==false){
					return resultSuccess;
				}
				if($('#forPayments').is(':checked') == false){
					$.each($('.report-body'), function(index, value) {
						if(currencyHandler.convertStringPatternToFloat($(this).find('.productQuantity').val()) <0 ||currencyHandler.convertStringPatternToFloat($(this).find('.bonus').val())<0){
							showMessage({title:'Error', msg:'Product And bonus quantiies must have positive values'});
								event.preventDefault();
							return false;
							
						}
					});
					if(totalCost == 0){
						var isBonusQty = false;
						$.each($('.report-body'), function(index, value) {
							if($(this).find('.productQuantity').val() <0 ||$(this).find('.bonus').val()<0){
								showMessage({title:'Error', msg:'Product And bonus quantiies must have positive values'});
									event.preventDefault();
								return false;
								
							}
							var bonusQty = currencyHandler.convertStringPatternToNumber($(this).find('.bonus').val());
							if(bonusQty != '' && bonusQty != 0) {
								isBonusQty = true;
							}
						});
						if(!isBonusQty) {
							showMessage({title:'Error', msg:'Please Enter Product Quantity'});
							return false;
						}
					}
				}else{
					$("#presentPayable").val(0);
				}
			}
			if(DeliveryNoteHandler.deliverynoteStepCount == 1){
				if(ValidateDeliveryNoteHandler.validatePaymnetFields()==false){
					return resultSuccess;
				}
				if($('#paymentType').val().toLowerCase() == "cheque"){
					if(ValidateDeliveryNoteHandler.validateChequeDetails()==false){
						return resultSuccess;
					}
				}
			}
			if(DeliveryNoteHandler.deliverynoteStepCount == 2){
				if ($(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).validate() == false)
					return;
			}
			
			$('.delivery-note-separator').css('width',"80px");
			var paramString = $(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).serializeArray();
			var presentPayableVal = $("#presentPayable").val();
			//var totalCost=$('#totalCost').val();
			var previousCreditVal = $("#previousCredit").val();
			var presentAdvanceVal =$("#presentAdvance").val();
			var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(presentPayableVal)) + parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));
			if(totalPayableVal < 0){
				var totalPayable=Math.abs(totalPayableVal).toFixed(2);
				$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayable)+' '+'('+Msg.ADVANCE_AMOUNT+')');
			}else{
				$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal.toFixed(2)));
			}
			var presentPaymentVal =$("#presentPayment").val();
			var totalPayable = $('#totalPayable').val();
			var balance=$('#balance').val();
			var chequeNo = $('#chequeNo').val();
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
			    if(formData.value === totalPayable){ 
			    	formData.value = currencyHandler.convertStringPatternToFloat(totalPayable);
			    }
			    if(formData.value === presentPaymentVal){ 
			    	formData.value = currencyHandler.convertStringPatternToFloat(presentPaymentVal);
			    }
			    if(formData.value === balance){ 
			    	formData.value = currencyHandler.convertStringPatternToFloat(balance);
			    }
			}); 
			
			// To stop next button clickable
			var businessName = $('#businessName').val();
			$('#businessName1').val(businessName);
			$.post('deliveryNote.json', 'action=check-businessname-availability&businessName='+businessName, function(obj) {
				var isExists = obj.result.data;
				if(isExists == 'true'){
					$.post('deliveryNote.json', 'action=is-day-book-closed', function(obj) {
						var isDayBookClosed = obj.result.data;
						// Checking whether day book closed or not 
						if(isDayBookClosed == 'false') {
							var sendFormData = $.param(paramString);
							$.ajax({type : "POST",
									url : 'deliveryNote.json',
									data : sendFormData,
									success : function(data) {
										$('#error-message').html('');
										$('#error-message').hide();
										$(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).hide();
										$(DeliveryNoteHandler.deliverynoteSteps[++DeliveryNoteHandler.deliverynoteStepCount]).show();
										if(PageHandler.expanded){
											PageHandler.pageSelectionHidden =false;
											PageHandler.hidePageSelection();
										}
										if (DeliveryNoteHandler.deliverynoteStepCount == DeliveryNoteHandler.deliverynoteSteps.length) {
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
											$.post('my-sales/transactions/delivery_note_view.jsp', 'viewType=preview', function(data) {
												 $('#deliverynote-preview-container').css({'height' : '350px'});
													$('#deliverynote-preview-container').html(data);
													$('#deliverynote-preview-container').show();
													DeliveryNoteHandler.expanded=false;
													$('#ps-exp-col').click(function() {
														if(DeliveryNoteHandler.deliverynoteStepCount==DeliveryNoteHandler.deliverynoteSteps.length){
															 if(!PageHandler.expanded) {
															    	$('#deliverynote-preview-container').css({'height' : '350px'});
																	$('#deliverynote-preview-container').html(data);
																	$('#deliverynote-preview-container').show();
																	DeliveryNoteHandler.expanded=false;
															    }
															    else{
															    	$('#deliverynote-preview-container').css({'height' : '350px'});
																	$('#deliverynote-preview-container').html(data);
																	$('#deliverynote-preview-container').show();
																	DeliveryNoteHandler.expanded=true;
															    }
														}
													   
													});
											});
										}
										if (DeliveryNoteHandler.deliverynoteStepCount > 0) {
											$('#button-prev').show();
											$('.page-buttons').css('margin-left', '150px');
											
											var businessName = $("#businessName").val();
											var invoiceName = $("#invoiceName").val();
											if($('#forPayments').is(':checked')){
												$('.green-results-list').hide();
												$.post('deliveryNote.json', 'action=go-for-payments&businessName=' +businessName +'&invoiceName=' +invoiceName +'&forPayments=' +forPayments , function(obj) {
													/*var previousCredits=$('#previousCredit').val();
										        	var advance=$('#presentAdvance').val();
										        	var formatTotalPayable=(currencyHandler.convertStringPatternToFloat(previousCredits) - currencyHandler.convertStringPatternToFloat(advance)).toFixed(2);
										        	//var advanceTotalPayable=Math.abs(formatTotalPayable).toFixed(2);
													//$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(advanceTotalPayable)+' '+'('+Msg.ADVANCE_AMOUNT+')');
										        	$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(formatTotalPayable));*/
										        	$('#presentPayableLabel').hide();
												});
											} else {
												var listOfObjects = '';
												var sum = 0;
													$('div#delivery-note-grid').each(function(index, value) {
												    	  var id = $(this).find('div').attr('id');
												          var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.productQuantity').val());
												          var bonusQty = currencyHandler.convertStringPatternToNumber($(this).find('.bonus').val());
												          var bonusReason = encodeURIComponent($(this).find('.bonusReason').val());
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
												    	$.post('deliveryNote.json','action=save-product-data&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName,function(obj) {
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
						} else {
							showMessage({title:'Warning', msg:'Your daybook have been closed for the day.'});
						    return false;
						}
					});
				} else {
					showMessage({title:'Warning', msg:'Business name not available to you.'});
				    return false;
				}
			});
			
		});
		$("#add-transaction").click(function(){
			PageHandler.hidePageSelection();
			  $('.page-link-strip').hide();
				$('.module-title').hide();
				$('.page-selection').animate( { width:"55px"}, 0,function(){});
				$('.page-container').animate( { width:"835px"}, 0);
				var thisTheme = PageHandler.theme;
				$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
		});
		$('#button-prev').click(function() {
			$('#action-clear').show();
			if(PageHandler.expanded){
				PageHandler.pageSelectionHidden =false;
				PageHandler.hidePageSelection();
			}
			if (DeliveryNoteHandler.deliverynoteStepCount == DeliveryNoteHandler.deliverynoteSteps.length) {
								$('#button-next').show();
								$('#button-save').hide();
								$('#button-update').hide();
								$('#deliverynote-preview-container').html('');
								$('#deliverynote-preview-container').hide();
								$('.page-buttons').css('margin-left', '150px');
							}
							$(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount])
									.hide();
							$(DeliveryNoteHandler.deliverynoteSteps[--DeliveryNoteHandler.deliverynoteStepCount])
									.show();
							if (DeliveryNoteHandler.deliverynoteStepCount > 0) {
								$('#button-prev').show();
								 $('.page-buttons').css('margin-left', '240px');
								//DeliveryNoteHandler.expanded=true;
							} else {
								if(PageHandler.expanded){
									PageHandler.pageSelectionHidden =false;
									PageHandler.hidePageSelection();
									DeliveryNoteHandler.expanded=true;
									    $('#search-results-list' ).css( "width", "830px" );
								        $('.report-header' ).css( "width", "830px" );
								    	$('.report-body' ).css( "width", "830px" );
										$('.report-header-column2' ).css( "width", "100px" );
										$('.report-body-column2' ).css( "width", "100px" );
								}
								$('#button-prev').hide();
								$('.page-buttons').css('margin-left', '240px');
							}
						});
		$('#button-save').click(function() {
			var thisButton = $(this);
			if($('#forPayments').is(':checked')){
				var paramString = 'action=save-payment-delivery-note';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$('.page-content').ajaxSavingLoader();
				$.post('deliveryNote.json', paramString, function(obj) {
					$.loadAnimation.end();
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
			} else {
				var paramString = 'action=save-deliverynote';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$('.page-content').ajaxSavingLoader();
				$.post('deliveryNote.json', paramString, function(obj) {
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
                                                       // var invoiceNumber=$('#').val();
														var paramString="action=remove-delivery-note-session-data";
											        	$.post('deliveryNote.json',paramString,function(obj){
											        		$('.task-page-container').html('');
											    			var container ='.transaction-page-container';
											    			var url = "my-sales/transactions/delivery_note_add.jsp";
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
		$('#button-update').click(function() {
			var thisButton = $(this);
			var paramString = $('#department-edit-form').serialize();
			$.post('deliveryNote.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.deliverynote-page-container',
					data : obj.result.message
				});
			});
		});

	},
	load : function() {
		$('#presentPayment').change(function() {
			$('#paymentTypeValid').empty();
			if($('#forPayments').is(':checked')){
				var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
				var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
				$('#presentPayment').val(formatPresentPayment);
				var presentAdvanceVal = currencyHandler.convertStringPatternToFloat($("#presentAdvance").val());
				var previousCreditVal = currencyHandler.convertStringPatternToFloat($("#previousCredit").val());
				var totalPayableVal = parseFloat(previousCreditVal) - parseFloat(presentAdvanceVal);
				if(totalPayableVal < 0){
					var totalPayable=Math.abs(totalPayableVal).toFixed(2);
					$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayable)+' '+'('+Msg.ADVANCE_AMOUNT+')');
				}else{
					$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal.toFixed(2)));
				}
				       var totalBalance=currencyHandler.convertFloatToStringPattern((totalPayableVal - presentPayment).toFixed(2));
				       var formatTotalBalance=currencyHandler.convertStringPatternToFloat(totalBalance);
						if(formatTotalBalance > 0){
							var balance=formatTotalBalance.toFixed(2);
							$('#balance').val(currencyHandler.convertFloatToStringPattern(balance));
						}else{
							var advanceBalance=Math.abs(formatTotalBalance).toFixed(2);
							$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
						}
			} else{
			var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
			var presentAdvanceVal = currencyHandler.convertStringPatternToFloat($("#presentAdvance").val());
			var previousCreditVal = currencyHandler.convertStringPatternToFloat($("#previousCredit").val());
			var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
			$('#presentPayment').val(formatPresentPayment);
			var presentPayableVal = currencyHandler.convertStringPatternToFloat($("#presentPayable").val());
			var totalPayableVal = parseFloat(presentPayableVal) + parseFloat(previousCreditVal) - parseFloat(presentAdvanceVal);
			if(totalPayableVal < 0){
				var totalPayable=Math.abs(totalPayableVal).toFixed(2);
				$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayable)+' '+'('+Msg.ADVANCE_AMOUNT+')');
				var totalBalance=(totalPayableVal - presentPayment).toFixed(2);
				if(totalBalance < 0) {
					var advanceBalance=Math.abs(totalBalance).toFixed(2);
					$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
				} else{
				$('#balance').val(totalBalance.toFixed(2));
				}
			}else{
				//var totalPayableVal = currencyHandler.convertStringPatternToFloat($("#totalPayable").val());
				$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal.toFixed(2)));
				var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
				var totalBalance=currencyHandler.convertStringPatternToFloat((totalPayableVal - presentPaymentVal).toFixed(2));
				if(totalBalance < 0){
					var advanceBalance=Math.abs(totalBalance).toFixed(2);
					$('#balance').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.ADVANCE_AMOUNT+')');
				}else{
				$('#balance').val(currencyHandler.convertFloatToStringPattern(totalBalance.toFixed(2)));
				}
			}     
		 }
			var presentPaymentVal = currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
			if(presentPaymentVal == 0) {
				DeliveryNoteHandler.getPaymentTypesWithOutPayment();
			} else {
				DeliveryNoteHandler.getPaymentTypes();
			}
	});
		$('#businessName').click(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			DeliveryNoteHandler.suggestBusinessName(thisInput);
		});
		$('#businessName').keyup(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			DeliveryNoteHandler.suggestBusinessName(thisInput);
		});
		$('#businessName').focusout(function() {
			$('#business-name-suggestions').animate({
				display : 'none'
			}, 1000, function() {
				$('#business-name-suggestions').hide();
			});
		});
		
		$('#forPayments').change(function(){
	        if(this.checked){
	        	DeliveryNoteHandler.getPaymentTypes();
	        	var paramString="action=remove-delivery-note-product-from-session";
	        	$.post('deliveryNote.json',paramString,function(obj){
	        	});
	            $('.gridDisplay').hide();
	            $('#presentPayable').hide();
	            $('#presentPayableLabel').hide();
	            $("#presentPayment").val("");
		        $('#balance').val("");
		        $('#totalCost').val(0.00.toFixed(2));
	        }else{
	        	$('#presentPayment').val(0.00.toFixed(2));
				DeliveryNoteHandler.getPaymentTypesWithOutPayment();
	        	 $('.green-results-list').html('');
	        	 DeliveryNoteHandler.loadGrid();
	        	 $('#presentPayable').show();
		         $('#presentPayableLabel').show();
		     //    $("#presentPayment").val("");
		         $('#balance').val("");
		         $('#totalCost').val(0.00.toFixed(2));
	        	}
	    }); 
		/*$('#businessName').change(function() {
			DeliveryNoteHandler.getInvoiceName();
		});*/
		$('#paymentType').change(function(){
			$('#bankNameValid').empty();
			$('#chequeNoValid').empty();
			$('#bankLocationValid').empty();
			$('#branchnameValid').empty();
			$('#chequeNo').html('');
			$('#bankName').html('');
			$('#bankLocation').val('');
			$('#branchName').html('');
			if($('#paymentType').val().toLowerCase()=='cheque'){
				$('#bankname').show();
				$('#bankName').attr('class','mandatory');
				$('#branchname').show();
				$('#chequeno').show();
				$('#chequeNo').attr('class','mandatory');
				$('#location').show();
				$('#bankLocation').attr('class','mandatory');
			}else{
				$('#bankname').hide().find("input").val("");
				$('#bankName').removeAttr('class');
				$('#branchname').hide().find("input").val("");
				$('#chequeno').hide().find("input").val("");
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
			    	  var availableQty = currencyHandler.convertStringPatternToFloat($(this).find('#row6').html());
			          var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.productQuantity').val());
			          var bonusQty = currencyHandler.convertStringPatternToNumber($(this).find('.bonus').val());
			          var bonusReason = $(this).find('.bonusReason').val();
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
			          var productName = $(this).find('#row1').html();
			          var productCost = $(this).find('#row4').html();
			          var totalCost = currencyHandler.convertStringPatternToFloat(productCost) *  parseInt(productQty);
			          if(isNaN(totalCost)) {
			        	  totalCost = 0.00;
			          }
			          if(currencyHandler.convertStringPatternToFloat($(this).find('.productQuantity').val()) <0 ||currencyHandler.convertStringPatternToFloat($(this).find('.bonus').val())<0){
							showMessage({title:'Error', msg:'Product And bonus quantiies must have positive values'});
								event.preventDefault();
							return false;
							
						}else{
							 $(this).find('#row5').html(currencyHandler.convertFloatToStringPattern(totalCost.toFixed(2)));
					          sum += totalCost;
					          $('#totalCost').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
						}
			          $('#presentPayable').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
			          $(this).find('.productQuantity').val(currencyHandler.convertNumberToStringPattern(productQty));
			          $(this).find('.bonus').val(currencyHandler.convertNumberToStringPattern(bonusQty));
			});
		});
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
	/*validateChequeDetails : function(){
		var bankNameLen = $('#bankName').val().length;
		var branchNameLen = $('#branchName').val().length;
		var presentPaymentLen = $('#presentPayment').val().length;
		var result =true;
		if(/^[a-zA-Z\s]+$/.test($('#bankName').val())==false||$('#bankName').val().length == 0||$('#bankName').val().charAt(0)==" "||$('#bankName').val().charAt(bankNameLen-1)==" "){
			$('#bankNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#bankName").focus(function(event){
				$('#bankNameValid').empty();
				 $('#bankName_pop').show();
			});
			$("#bankName").blur(function(event){
				 $('#bankName_pop').hide();
				 if(/^[a-zA-Z.\s]+$/.test($('#bankName').val())==false||$('#bankName').val().length== 0||$('#bankName').val().charAt(0)==" "||$('#bankName').val().charAt(citylen-1)==" "){
					 $('#bankNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#bankName").focus(function(event){
						 $('#bankNameValid').empty();
						 $('#bankName_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		if(/^[a-zA-Z.\s]+$/.test($('#branchName').val())==false && $('#branchName').val().length> 0||$('#branchName').val().charAt(0)==" "||$('#branchName').val().charAt(branchName-1)==" "){
			$('#branchnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#branchName").focus(function(event){
				$('#branchnameValid').empty();
				 $('#branchname_pop').show();
			});
			$("#branchName").blur(function(event){
				 $('#branchname_pop').hide();
				 if(/^[a-zA-Z.\s]+$/.test($('#branchName').val())==false&&$('#branchName').val().length>0||$('#branchName').val().charAt(0)==" "||$('#branchName').val().charAt(citylen-1)==" "){
					 $('#branchnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#branchName").focus(function(event){
						 $('#branchnameValid').empty();
						 $('#branchname_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		var bankLocationLen = $('#bankLocation').val().length;
		if(/^[a-zA-Z.\s]+$/.test($('#bankLocation').val())==false|| $('#bankLocation').val().length == 0||$('#bankLocation').val().charAt(0)==" "||$('#bankLocation').val().charAt(bankLocation-1)==" "){
			$('#bankLocationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#bankLocation").focus(function(event){
				$('#bankLocationValid').empty();
				 $('#bankLocation_pop').show();
			});
			$("#bankLocation").blur(function(event){
				 $('#bankLocation_pop').hide();
				 if(/^[a-zA-Z.\s]+$/.test($('#bankLocation').val())==false || $('#bankLocation').val().length ==0||$('#bankLocation').val().charAt(0)==" "||$('#branchName').val().charAt(citylen-1)==" "){
					 $('#bankLocationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#bankLocation").focus(function(event){
						 $('#bankLocationValid').empty();
						 $('#bankLocation_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		var chequeLen = $('#chequeNo').val().length;
		if(/^[0-9a-zA-Z\s]+$/.test($('#chequeNo').val())==false|| $('#chequeNo').val().length == 0||$('#chequeNo').val().charAt(0)==" "||$('#chequeNo').val().charAt(chequeLen-1)==" "){
			$('#chequeNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#chequeNo").focus(function(event){
				$('#chequeNoValid').empty();
				 $('#chequeNo_pop').show();
			});
			$("#chequeNo").blur(function(event){
				 $('#chequeNo_pop').hide();
				 if(/^[0-9a-zA-Z\s]+$/.test($('#chequeNo').val())==false || $('#chequeNo').val().length ==0||$('#chequeNo').val().charAt(0)==" "||$('#chequeNo').val().charAt(chequeNo-1)==" "){
					 $('#chequeNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#chequeNo").focus(function(event){
						 $('#chequeNoValid').empty();
						 $('#chequeNo_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length == 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(bankNameLen-1)==" "){
			$('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#presentPayment").focus(function(event){
				$('#presentPaymentValid').empty();
				 $('#presentPayment_pop').show();
			});
			$("#presentPayment").blur(function(event){
				 $('#presentPayment_pop').hide();
				 if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length== 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(citylen-1)==" "){
					 $('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#presentPayment").focus(function(event){
						 $('#presentPaymenteValid').empty();
						 $('#presentPayment_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		return result;
	},
	validatePaymnetFields : function(){
		var result = true;
		if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length == 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt($('#presentPayment').val().length -1)==" "){
			$('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    // $('#city').focus();
			$("#presentPayment").focus(function(event){
				$('#presentPaymentValid').empty();
				 $('#presentPayment_pop').show();
			});
			$("#presentPayment").blur(function(event){
				 $('#presentPayment_pop').hide();
				 if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length== 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(citylen-1)==" "){
					 $('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#presentPayment").focus(function(event){
						 $('#presentPaymenteValid').empty();
						 $('#presentPayment_pop').show();
					 });
					
				 }
			});
			result = false;
		}
		if(currencyHandler.convertStringPatternToFloat($('#presentPayment').val()) > 0){
			if($('#paymentType').val() == 'NA'||$('#paymentType').val() == '-1'||$('#paymentType').val() == ''){
				$('#paymentTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#paymentType").focus(function(event){
					$('#paymentTypeValid').empty();
					 $('#paymentType_pop').show();
				});
				$("#paymentType").blur(function(event){
					$('#paymentType_pop').hide();
					if($('#presentPayment').val() > 0){
					if($('#paymentType').val() == 'NA'||$('#paymentType').val() == '-1'||$('#paymentType').val() == ''){
						 $('#paymentTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#paymentType").focus(function(event){
							 $('#paymentTypeValid').empty();
							 $('#paymentType_pop').show();
						 });
					 }
					}else{
						 $('#paymentTypeValid').empty();
					}
				});
				result = false;
			}
		}
		return result;
	},*/
	suggestBusinessName : function(thisInput) {
		var suggestionsDiv = $('#business-name-suggestions');
		var val = $('#businessName').val();
		$.post('deliveryNote.json','action=get-business-name&businessNameVal=' + val,function(obj) {
							$.loadAnimation.end();
							suggestionsDiv.html('');
							var data = obj.result.data;
							if (data != undefined) {
								var htmlStr = '<div>';
								for ( var loop = 0; loop < data.length; loop = loop + 1) {
									htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'+ data[loop]+ '</a></li>';
								}
								htmlStr += '</div>';
								suggestionsDiv.append(htmlStr);
							} else {
								suggestionsDiv.append('<div id="">'	+ 'No Business Names' + '</div>');
							}
							suggestionsDiv.css('left', thisInput.position().left);
							suggestionsDiv.css('top', thisInput.position().top + 25);
							suggestionsDiv.show();
							$('.select-teacher').click(function() {
								if(PageHandler.expanded){
									PageHandler.pageSelectionHidden =false;
									PageHandler.hidePageSelection();
								}
										thisInput.val($(this).html());
										thisInput.attr('businessName', $(this).attr('businessName'));
										var businessName = $(this).attr('businessName');
										$('#businessName').attr('value', businessName);
										suggestionsDiv.hide();
										var businessName = $('#businessName').val();
										//fetching the invoice name based on the business name.
										$.post('deliveryNote.json','action=get-customer-data&businessName='+businessName, function(obj) {
											$('#invoiceName').val(obj.result.data.invoiceName);
											$('#previousCredit').val(currencyHandler.convertFloatToStringPattern(obj.result.data.customerCredit));
											$('#presentAdvance').val(currencyHandler.convertFloatToStringPattern(obj.result.data.customerAdvance));
										});
								 
										//checking for payments.
										var forPayments = $('#forPayments').val();
										if($('#forPayments').is(':checked')){
											$('.gridDisplay').hide();
										} else {
											 DeliveryNoteHandler.loadGrid();
											}
									});
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
	/*len: function(count){
		$('#row-'+count).each(function(index) {
	        var maxHeight = 0;
	        $(this).children().each(function(index) {
	            if($(this).height() > maxHeight) 
	                maxHeight = $(this).height();
	        });
	    });    
	},*/
	//loading the grid when Accounts Payable Option is not selectd. 
	loadGrid: function() {
        var businessName = $('#businessName').val();
        $('.green-results-list').html('');
			$.post('deliveryNote.json', 'action=get-grid-data&businessName=' +businessName, function(obj) {
				var data = obj.result.data;
				if(data != undefined){
					if(data.length>0) {
						var rowstr = '';
						var count=1;
						for(var x=0;x<data.length;x=x+1) {
							var rowstr='<div class="ui-content report-content" id = "delivery-note-grid">';
							rowstr+='<div id="delivery-note-row-'+count+'" class="report-body" style="width: 699px; height: auto; overflow: hidden;">';
							rowstr+='<div id="row1" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].productName+'</div>'
							rowstr+='<div id="row8" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
							rowstr+='<div id="row6" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 85px;text-align:right !important;">'+data[x].availableQuantity+'</div>';
							rowstr+='<div id="row4" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 85px; text-align:right !important;">'+data[x].productCost+'</div>';
							rowstr+='<div id="row2" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px">'+'<input type = "text" style="margin-top:-3px; text-align:right !important; width:70px;" class="productQuantity">'+'</div>';
							rowstr+='<div id="row3" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px">'+'<input type = "text" style="margin-top:-3px;text-align:right;width:70px;" class="bonus" >'+'</div>';
							rowstr+='<div id="row7" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px;text-align:right;">'+'<textarea rows="2" cols="2"  style="height: 41px; width: 85px; border: none; resize: none;" class="bonusReason"></textarea>'+'</div>';
							rowstr+='<div id="row5" class="report-body-column2 right-aligned totalCost sameHeight" style="height: inherit; width: 85px;text-align:right;">'+data[x].totalCost+'</div>';
							rowstr+='</div>';
							rowstr+='</div>';
							$('.green-results-list').append(rowstr);
							if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
									$('#delivery-note-row-'+count).each(function(index) {
								        $(this).children().height(100);
								    });    
								   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
											$('#delivery-note-row-'+count).each(function(index) {
										        $(this).children().height(70);
										    });    
									}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
										$('#delivery-note-row-'+count).each(function(index) {
									        $(this).children().height(55);
									    });   
								   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
									   $('#delivery-note-row-'+count).each(function(index) {
									        $(this).children().height(50);
									    });   
						           }
						           else{
						        	   $('#delivery-note-row-'+count).each(function(index) {
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
						$( '.report-body-column2' ).css( "width", "90px" );
						$( '.returnQty' ).css( "width", "90px" );		
				        $('.gridDisplay').show();
					}else{
					$( '#search-results-list' ).css( "width", "830px" );
					$( '.report-header' ).css( "width", "830px" );
			    	$( '.report-body' ).css( "width", "830px" );
					$( '.report-header-column2' ).css( "width", "90px" );
					$( '.report-body-column2' ).css( "width", "90px" );
					$( '.returnQty' ).css( "width", "90px" );		
			        $('.gridDisplay').show();
					}
					$('#ps-exp-col').click(function(){
						   if(PageHandler.expanded){
							   $( '#search-results-list' ).css( "width", "699px" );
							    $( '.report-header' ).css( "width", "699px" );
						    	$( '.report-body' ).css( "width", "699px" );
								$( '.report-header-column2' ).css( "width", "70px" );
								$( '.report-body-column2' ).css( "width", "70px" );
								$( '.bonusReason' ).css( "width", "70px" );
						   }else{
							   $( '#search-results-list' ).css( "width", "830px" );
							   $( '.report-header' ).css( "width", "830px" );
						    	$( '.report-body' ).css( "width", "830px" );
								$( '.report-header-column2' ).css( "width", "90px" );
								$( '.report-body-column2' ).css( "width", "90px" );
								$( '.bonusReason' ).css( "width", "90px" );
						   }
					   });
					$('.green-results-list').show();
			}else {
				$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
				$('.gridDisplay').show();
			}
		});
	},
		
};

var SalesReturnsHandler = {
		salesreturnsSteps : [ '#sales-returns-form'],
		salesreturnsUrl : [ 'salesReturn.json'],
		salesreturnsStepCount : 0,
		expanded: true,
		count: 1,
		initAddButtons : function() {
			SalesReturnsHandler.salesreturnsStepCount = 0;
			$.fn.clear = function() {
				  return this.each(function() {
				    var type = this.type, tag = this.tagName.toLowerCase();
				    if (tag == 'form')
				      return $(':input',this).clear();
				    if (type == 'text' || type == 'password' || tag == 'textarea')
				      this.value = '';
				    else if (tag == 'select')
				      this.selectedIndex = 0;
				  });
				};
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$(SalesReturnsHandler.salesreturnsSteps[SalesReturnsHandler.salesreturnsStepCount]).clear();
							$(this).dialog('close');
						},
					Cancel: function() {
						$(this).dialog('close');
				}
					}
				});
			
			});
			$('#button-save').click(function() {
				var thisButton = $(this);
				var paramString = 'action=save-sales-returns';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$('.page-content').ajaxSavingLoader();
				$.post('salesReturn.json', paramString, function(obj) {
					$.loadAnimation.end();
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
				
		});
			$('#button-prev').click(function() {
						$('#action-clear').show();
						if(PageHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
						}
						if (SalesReturnsHandler.salesreturnsStepCount == SalesReturnsHandler.salesreturnsSteps.length) {
							$('#button-next').show();
							$('#button-save').hide();
							$('#button-update').hide();
							$('#sales-return-preview-container').html('');
							$('#sales-return-preview-container').hide();
							$('.page-buttons').css('margin-left', '150px');
						}
						$(SalesReturnsHandler.salesreturnsSteps[SalesReturnsHandler.salesreturnsStepCount]).hide();
						$(SalesReturnsHandler.salesreturnsSteps[--SalesReturnsHandler.salesreturnsStepCount]).show();
						if (SalesReturnsHandler.salesreturnsStepCount > 0) {
							$('#button-prev').show();
							$('.page-buttons').css('margin-left', '150px');
						} else {
							$('#button-prev').hide();
							$('.page-buttons').css('margin-left', '240px');
						}
						
					});
			$('#button-next').click(function(){
				var resultSuccess=true;
				var resultFailure=false;
				/*if(SalesReturnsHandler.salesreturnsStepCount==0){
					if(SalesReturnsHandler.validatein()==false){
						return resultSuccess;
					}
				}*/
				if(PageHandler.expanded){
					PageHandler.pageSelectionHidden =false;
					PageHandler.hidePageSelection();
				}
				if($(SalesReturnsHandler.salesreturnsSteps[SalesReturnsHandler.salesreturnsStepCount]).validate()==false) return;
				var businessName = $('#businessName').val();
				$.post('salesReturn.json', 'action=check-businessname-availability&businessName='+businessName, function(obj) {
					var isExists = obj.result.data;
					if(isExists == 'true') {
						$.post('salesReturn.json', 'action=is-day-book-closed', function(obj){
							var isDayBookClosed = obj.result.data;
							if(isDayBookClosed == 'false'){
								var thisButton = $(this);
								var listOfObjects = '';
								var invoiceName = $('#invoiceName').val();
								var remarks = $('#salesReturnRemarks').val();
								var qtySold;
								var grandTotalCost =currencyHandler.convertStringPatternToFloat($('#grandTotalCost').val());
									totalQuantity = 0;
								$('div#sales-row').each(function(index, value) {
									var id = $(this).find('div').attr('id');
									var productName = $(this).find('#row1').html();
									var batchNumber = $(this).find('#row2').html();
									var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
									var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
									var returnQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
									totalQuantity = totalQuantity + Number(damaged)+Number(resalable);
									if(damaged < 0 || resalable < 0){
										showMessage({title:'Warning', msg:'Damaged and resalable quanities must have postive values'});
									    evt.preventDefault();
									    return false;
									}
									if(damaged != 0 || resalable != 0){
									$.ajax({type: "POST",
											url:'salesReturn.json', 
											data:  'action=get-quantity-sold&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName,
											async : false,
											success: function(data){
											  qtySold = data.result.data; 
										 }
										  });
									}
									if(parseInt(returnQty) > parseInt(qtySold)) {
										  showMessage({title:'Warning', msg:'Please enter valid quantities.'});
										  evt.stopPropagation();
										  return false;
									  } else {
									  if(damaged != "" || resalable != ""){
												if(index == 0) {
													listOfObjects +=damaged+'|'+resalable+'|'+returnQty+'|'+batchNumber+'|'+productName
												} else {
													listOfObjects +='?'+damaged+'|'+resalable+'|'+returnQty+'|'+batchNumber+'|'+productName
												}
											}
									  }
									return totalQuantity;
								});
								if(totalQuantity == 0){
									 showMessage({title:'Warning', msg:'Please enter damaged or resalable quantity.'});
									  evt.stopPropagation();
									  return false;
								} 
								var paramString = 'action=save-sales-return-products&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&grandTotalCost=' +grandTotalCost+'&remarks='+remarks;
									if(listOfObjects != '') {
										$.ajax({type : "POST",
											url : 'salesReturn.json',
											data : paramString,
											success : function(data) {
												$(SalesReturnsHandler.salesreturnsSteps[SalesReturnsHandler.salesreturnsStepCount])	.hide();
												$(SalesReturnsHandler.salesreturnsSteps[++SalesReturnsHandler.salesreturnsStepCount]).show();
												$('#button-next').hide();
												$('#action-clear').hide();
												$('#button-save').show();
												$('#button-prev').show();
												$.post('my-sales/transactions/sales_return_data_preview.jsp', 'viewType=preview', function(data){
													$('#sales-return-preview-container').css({'height' : '350px'});
													$('#sales-return-preview-container').html(data);
													$('#sales-return-preview-container').show();
												});
											},
									});
									}
									/*showMessage({title:'Warning', msg:'Please enter damaged or resalable quantity.'});
								    return false;*/
							} else {
								showMessage({title:'Warning', msg:'Your daybook have been closed for the day.'});
							    return false;
							}
						//}
						});
				
					} else {
						showMessage({title:'Warning', msg:'Business name not available to you.'});
					    return false;
					}
				});
				
			});
			/*$("#add-sales-returns").click(function(){
				$('.transaction-page-container').load('my-sales/transactions/sales_returns_add.jsp');
				PageHandler.hidePageSelection();
				  $('.page-link-strip').hide();
					$('.module-title').hide();
					$('.page-selection').animate( { width:"55px"}, 0,function(){});
					$('.page-container').animate( { width:"835px"}, 0);
					var thisTheme = PageHandler.theme;
					$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			});*/
		    $('#action-cancel').click(function() {
				$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
				$("#error-message").dialog({	
					resizable : false,
					height : 140,
					title : "<span class='ui-dlg-confirm'>Confirm</span>",
					modal : true,
					buttons : {
						'Ok' : function() {
							var paramString="action=remove-sales-return-session-data";
				        	$.post('salesReturn.json',paramString,function(obj){
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/sales_returns_add.jsp";
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
checkLength: function(len,number) {
	$('.invoice-boxes-'+number).css({'height' : 'inherit'});
	if(len>45){
		$('#row-'+number).css({'height' : '35px'});
	}
	else
	$('#row-'+number).css({'height' : '40px'});
	
},
		load:function() {
			
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesReturnsHandler.suggestBusinessName(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesReturnsHandler.suggestBusinessName(thisInput);
			});
			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			
			$('#productName').change(function() {
				var productName=$("#productName").val()
				var businessName = $("#businessName").val()
				$.post('salesReturn.json','action=get-product-cost&productNameVal='+productName+'&businessNameVal='+businessName, function(obj) {
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
					  if(damaged != 0 || resalable != 0){
						  $.ajax({type: "POST",
								url:'salesReturn.json', 
								data:  'action=get-quantity-sold&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName,
								async : false,
								success: function(data){
								  qtySold = data.result.data; 
							    }
						  });
					  }
					  $(this).find('.returnQty').val(currencyHandler.convertNumberToStringPattern(returnQty));
					  $(this).find('.returnQty').attr('readonly','readonly');
				      $(this).find('.damaged').val(currencyHandler.convertNumberToStringPattern(damaged));
					  $(this).find('.resalable').val(currencyHandler.convertNumberToStringPattern(resalable));
					  if(parseInt(returnQty) > parseInt(qtySold)) {
						  showMessage({title:'Warning', msg:'You Can Not Exceed Sold Quantity ('+qtySold+')'});
						  event.preventDefault();
					  } 
				  });
				});
		},
		suggestBusinessName : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var val = $('#businessName').val();
			$.post('salesReturn.json','action=get-business-name&businessNameVal=' + val,function(obj) {
				$.loadAnimation.end();
				suggestionsDiv.html('');
				var data = obj.result.data;
				if (data != undefined) {
					var htmlStr = '<div>';
					for ( var loop = 0; loop < data.length; loop = loop + 1) {
						htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">' + data[loop] + '</a></li>';
					}
					htmlStr += '</div>';
					suggestionsDiv.append(htmlStr);
				} else {
					suggestionsDiv.append('<div id="">' + obj.result.message + '</div>');
				}
				suggestionsDiv.css('left', thisInput.position().left);
				suggestionsDiv.css('top', thisInput.position().top + 25);
				suggestionsDiv.show();
				$('.select-teacher').click(function() {
					if(PageHandler.expanded){
						PageHandler.pageSelectionHidden =false;
						PageHandler.hidePageSelection();
					}
				thisInput.val($(this).html());
				thisInput.attr('businessName', $(this).attr('businessName'));
				$('#businessName').attr('value', $(this).attr('businessName'));
				suggestionsDiv.hide();
				var businessName = $('#businessName').val();
				$('.green-results-list1').html('');
				$.post('salesReturn.json', 'action=get-grid-data&businessName='+businessName, function(obj) {
					var data = obj.result.data;
					if(data !== undefined){
						if(data.length>0) {
							$('.success-msg').hide();
							var count=1;
							for(var x=0;x<data.length;x=x+1) {
								var rowstr='<div class="ui-content report-content" id="sales-row">';
								rowstr=rowstr+'<div id="sales-return-row-'+count+'" class="report-body" style="width: 697px; overflow: hidden;">';
								rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;" >'+data[x].productName+'</div>';
								rowstr=rowstr+'<div id="row2" class="report-body-column2 centered" style="height: inherit; width: 85px ;word-wrap: break-word;">'+data[x].batchNumber+'</div>';
								rowstr=rowstr+'<div id="row6" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;width:70px; text-align:right !important;" id="damaged" class="damaged" >' + '</div>';
								rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;width:70px;text-align:right !important;" id="resalable" class="resalable" >'+'</div>';
								rowstr=rowstr+'<div id="row4" class="report-body-column2 right-aligned" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text"  id="returnQty" class="returnQty" style = "margin-top:-3px;text-align:right !important; size: 4px; border: none;" readonly="readonly">'+'</div>';
								rowstr=rowstr+'</div></div>';
								$( '#returnQty' ).css( "width", "85px" );
							$('.green-results-list1').append(rowstr);
							if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
									$('#sales-return-row-'+count).each(function(index) {
								        $(this).children().height(100);
								    });    
								   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
											$('#sales-return-row-'+count).each(function(index) {
										        $(this).children().height(68);
										    });    
									}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
										$('#sales-return-row-'+count).each(function(index) {
									        $(this).children().height(55);
									    });   
								   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
									   $('#sales-return-row-'+count).each(function(index) {
									        $(this).children().height(50);
									    });   
						           }
						           else{
						        	   $('#sales-return-row-'+count).each(function(index) {
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
							$( '.report-header-column2' ).css( "width", "150px" );
							$( '.report-body-column2' ).css( "width", "150px" );
							$( '.returnQty' ).css( "width", "140px" );		
					        $('.gridDisplay').show();
						}else{
						$( '#search-results-list' ).css( "width", "830px" );
						$( '.report-header' ).css( "width", "830px" );
				    	$( '.report-body' ).css( "width", "830px" );
						$( '.report-header-column2' ).css( "width", "150px" );
						$( '.report-body-column2' ).css( "width", "150px" );
						$( '.returnQty' ).css( "width", "140px" );		
				        $('.gridDisplay').show();
						}
						$('#ps-exp-col').click(function(){
							   if(PageHandler.expanded){
								   $( '#search-results-list' ).css( "width", "699px" );
								    $( '.report-header' ).css( "width", "699px" );
							    	$( '.report-body' ).css( "width", "699px" );
									$( '.report-header-column2' ).css( "width", "125px" );
									$( '.report-body-column2' ).css( "width", "125px" );
									$( '.returnQty' ).css( "width", "100px" );
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
			$.post('salesReturn.json','action=get-customer-invoice-name&businessName='+businessName, function(obj) {
					$('#invoiceName').val(obj.result.data);
				});
			 });
		   });
		},
	};

var JournalHandler = {
		journalSteps : [ '#journal-form'],
		journalsUrl : [ 'journal.json'],
		journalStepCount : 0,
		count: 1,
		expanded: true,
		initAddButtons : function() {
			JournalHandler.journalStepCount = 0;
			$.fn.clear = function() {
				  return this.each(function() {
				    var type = this.type, tag = this.tagName.toLowerCase();
				    if (tag == 'form')
				      return $(':input',this).clear();
				    if (type == 'text' || type == 'password' || tag == 'textarea')
				      this.value = '';
				    else if (tag == 'select')
				      this.selectedIndex = 0;
				  });
				};
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('form').clear();
							$(this).dialog('close');
						},
					Cancel: function() {
						$(this).dialog('close');
				}
					}
				});
			
			});
			$('#amount').blur(function() {
				var journalAmount=$('#amount').val();
				var formatJournalAmount=currencyHandler.convertStringPatternToFloat(journalAmount);
				if(formatJournalAmount >10000000000000 && formatJournalAmount != 10000000000000){
					$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#amount").focus(function(event){
						$('#amountValid').empty();
						 $('#amount_len_pop').show();
					});
					$("#amount").blur(function(event){
						 $('#amount_len_pop').hide();
						 if(formatJournalAmount >10000000000000  && formatJournalAmount != 10000000000000){
							 $('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#amount").focus(function(event){
								 $('#amountValid').empty();
								 if($('#amount').val().length==0){
										 $('#amount_len_pop').show();
								 }
								});
						 }else{
							 $('#amount_len_pop').hide();
								$('#amountValid').empty();
						 }
					});
					
				}else{
					var formatAmount=currencyHandler.convertFloatToStringPattern(formatJournalAmount.toFixed(2));
					$('#amount').val(formatAmount);
				}
			});
			$('#button-save').click(function() {
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$('.page-content').ajaxSavingLoader();
				if(ValidateJournalHandler.validateJournals()==false){
					 $.loadAnimation.end();
					return true;
				}
				var thisButton = $(this);
				var paramString = $('#add-journal-form').serializeArray();
				
				var amount=$('#amount').val();
				//access form searialized value and modified
				$.each(paramString, function(i, formData) {
				    if(formData.value === amount){ 
				    	formData.value = currencyHandler.convertStringPatternToFloat(amount);
				    }
				});
				var sendFormData=$.param(paramString);
				$.post('journal.json', sendFormData, function(obj) {
					 $.loadAnimation.end();
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
				
		});
			/*$("#add-journal").click(function(){
				$('.transaction-page-container').load('my-sales/transactions/add_journals.jsp');
				PageHandler.hidePageSelection();
				  $('.page-link-strip').hide();
					$('.module-title').hide();
					$('.page-selection').animate( { width:"55px"}, 0,function(){});
					$('.page-container').animate( { width:"835px"}, 0);
					var thisTheme = PageHandler.theme;
					$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			});*/
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
			    			var url = "my-sales/transactions/add_journals.jsp";
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
	/*validateJournals : function(){
		var result = true;
		if($('#businessName').val().length==0){
			$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#businessName").focus(function(event){
				$('#businessNameValid').empty();
				 $('#businessName_pop').show();
			});
			$("#businessName").blur(function(event){
				 $('#businessName_pop').hide();
				 if($('#businessName').val().length==0){
					 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#businessName").focus(function(event){
						 $('#businessNameValid').empty();
						 if($('#businessName').val().length==0){
							 $('#businessName_pop').show();
						 }
						});
				 }else{
					 $('#businessName_pop').hide();
				 }
			});
			result =false; 
		}else{
			 $('#businessName_pop').hide();
		}
		if( $('#journalType').val()=='-1'||$('#journalType').val()==null){
			$('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#journalType").focus(function(event){
				$('#typeValid').empty();
				 $('#Type_pop').show();
			});
			$("#journalType").blur(function(event){
				 $('#Type_pop').hide();
				 if($('#journalType').val() == '-1'||$('#journalType').val()==null){
					 $('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#journalType").focus(function(event){
						 $('#typeValid').empty();
						 if($('#journalType').val() == '-1'){
						 $('#Type_pop').show();
					 }
						});
				 }else{
					 $('#Type_pop').hide();
				 }
			});
			result =false; 
		}
		if($('#description').val()==""){
			$('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#description").focus(function(event){
				$('#descValid').empty();
				 $('#desc_pop').show();
			});
			$("#description").blur(function(event){
				 $('#desc_pop').hide();
				 if($('#description').val().length==0){
					 $('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#description").focus(function(event){
						 $('#descValid').empty();
						 if($('#description').val().length==0){
							 $('#desc_pop').show();
						 }
						});
				 }else{
					 $('#desc_pop').hide();
				 }
			});
			result =false; 
		}
		if($('#amount').val().length == 0){
			$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#amount").focus(function(event){
				$('#amountValid').empty();
				 $('#amount_pop').show();
			});
			$("#amount").blur(function(event){
				 $('#amount_pop').hide();
				 if(/^[0-9,.]+$/.test($('#amount').val())==false||$('#amount').val().charAt(0)==" "){
					 $('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#amount").focus(function(event){
						 $('#amountValid').empty();
						 if($('#amount').val().length==0){
								 $('#amount_pop').show();
						 }
						});
				 }else{
					 $('#amount_pop').hide();
				 }
			});
			result =false; 
		}
		return result;
	},*/
	loadData:function(){
		$('#journalType').change(function() {
			var journalType = $('#journalType').val();
			$.post('journal.json', 'action=generate-invoice-no&journalType='+journalType, function(obj) {
				$('#invoiceNo').val(obj.result.data);
			});
		});
		
		$('#journalType').click(function() {
			$.post('journal.json', 'action=is-daybook-closed', function(obj) {
				var isDaybookClosed = obj.result.data;
				if(isDaybookClosed == 'true') {
					showMessage({title:'Warning', msg:'Your daybook have been closed for the day.'});
				    return false;
				}
			});
		});
		
		$('#businessName').click(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			JournalHandler.suggestBusinessName(thisInput);
		});
		$('#businessName').keyup(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			JournalHandler.suggestBusinessName(thisInput);
		});
		$('#businessName').focusout(function() {
			$('#business-name-suggestions').animate({
				display : 'none'
			}, 1000, function() {
				$('#business-name-suggestions').hide();
			});
		});
		
	},
	
	suggestBusinessName : function(thisInput) {
		var suggestionsDiv = $('#business-name-suggestions');
		var val = $('#businessName').val();
		$.post('journal.json','action=get-business-name&businessNameVal=' + val,function(obj) {
			$.loadAnimation.end();
			suggestionsDiv.html('');
			var data = obj.result.data;
			if (data != undefined) {
				var htmlStr = '<div>';
				for ( var loop = 0; loop < data.length; loop = loop + 1) {
					htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'+ data[loop]+ '</a></li>';
				}
			htmlStr += '</div>';
			suggestionsDiv.append(htmlStr);
			} else {
				suggestionsDiv.append('<div id="">'	+ obj.result.message + '</div>');
			}
			suggestionsDiv.css('left', thisInput.position().left);
			suggestionsDiv.css('top', thisInput.position().top + 25);
			suggestionsDiv.show();
			$('.select-teacher').click(function() {
				if(PageHandler.expanded){
					PageHandler.pageSelectionHidden =false;
					PageHandler.hidePageSelection();
			}
			thisInput.val($(this).html());
			thisInput.attr('businessName', $(this).attr('businessName'));
			var businessName = $(this).attr('businessName');
			$('#businessName').attr('value', businessName);
			suggestionsDiv.hide();
			var businessName = $('#businessName').val();
			//fetching the invoice name based on the business name.
			$.post('journal.json','action=get-invoice-name-for-journal&businessName='+businessName, function(obj) {
				$('#invoiceName').val(obj.result.data);
			});
		 });
	  });
	},
};

var CustomerCrHandler = {
		status: true,
		flag:true,
		initPageLinks : function() {
			$('#add-change-request').pageLink({
				container : '.customer-page-container',
				url : 'customer/customer_change_request_add.jsp'
			});
			$('#search-change-request-details').pageLink({
				container : '.customer-page-container',
				url : 'customer/customer_change_request_search.jsp'
			});
		
		},
		
		crSteps:['#customer-change-request-form','#customers-change-request-detail-form'],
		crUrl : [ 'customerCr.json', 'customerCr.json' ],
		crStepCount : 0,
		
		initAddCrButtons : function(){
			CustomerCrHandler.crStepCount = 0;
			$.fn.clear = function() {
				  return this.each(function() {
				    var type = this.type, tag = this.tagName.toLowerCase();
				    if (tag == 'form')
				      return $(':input',this).clear();
				    if (type == 'text' || type == 'password' || tag == 'textarea')
				      this.value = '';
				    /*
					 * else if (type == 'checkbox' || type == 'radio')
					 * this.checked =true;
					 */
				    else if (tag == 'select')
				      this.selectedIndex = 0;
				  });
				};
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('#businessName').val('');
							$('#customerName').val('');
							$('#businessNameValid').html('');
							if($('#crType').val() == "true"){
								$('#crType option[value=true]').remove();
								$("#crType option[value=true]").text("New Customer");
								$("#crType option[value=true]").val("false");
								$('#crType').append( $('<option></option').text('Existed Customer').val('true'));
							}
							$(this).dialog('close');
						},
				Cancel: function() {
					$(this).dialog('close');
				}
					}
				});
			});
			// clear button for customer credit form
			$('#action-clear-customer-credit').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('form').clearForm();
							$(this).dialog('close');
						},
				Cancel: function() {
					$(this).dialog('close');
				}
					}
				});
			});
			$('#button-cr-next').click(function() {
				
				var success = true;
				$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).find('.mandatory').each(function() {
					
					if($(this).val()=='' || ($(this).val()=='-1') &&
							($(this).get(0).tagName=='select'||$(this).get(0).tagName=='SELECT')){
						// showMessage({title:'Error', msg:'Red marked fields
						// are mandatory'});
						return;
						success = false;
						
					}
				});
				var thisButton = $(this);
				var resultSuccess=true;
				var resultFailure=false;
				if(CustomerCrHandler.crStepCount == 0){
				if(CustomerCrHandler.validateApprovedOrDeclineCustomerCR() == false){
					return resultSuccess;
				   }
				}
				if(ValidateCustomerChangerequest.validateCustomerCr()==false){
					return resultSuccess;
				}
				else if (CustomerCrHandler.crStepCount == 1) {
					if(ValidateCustomerChangerequest.validateCustomerCrStepOne()==false){
						return resultSuccess;
					}
				}
			// if($(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).validate()==false)
			// return;
						var paramString = $(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).serialize();
						$.ajax({	type : "POST",
							url : 'customerCr.json',
							data : paramString,
							success : function(obj) {
								var res=obj.result.data;
								var crType=$('#crType').val();
								if(res !=null){
								$('#mobile').val(res.mobile);
								$('#locality').val(res.locality);
								$('#region').val(res.region);
								if(res.landmark != 'null'){
									$('#landmark').val(res.landmark);
									
								}else{
									$('#landmark').val();
								}
								if(res.alternateMobile != 'null'){
									$('#alternateMobile').val(res.alternateMobile);
								}else{
									$('#alternateMobile').val();
								}
								if(res.directLine != 'null'){
									$('#directLine').val(res.directLine);
								}else{
									$('#directLine').val();
								}
								$('#invoiceName').val(res.vbCustomer.invoiceName);
								if(res.city != 'null'){
									$('#city').val(res.city);
									
								}else{
									$('#city').val();
								}
								if(res.state != 'null'){
									$('#state').val(res.state);
								}else{
									$('#state').val();
								}
								if(res.addressLine1 != 'null'){
									$('#addressLine1').val(res.addressLine1);
									
								}else{
									$('#addressLine1').val();
								}
								if(res.email != 'null'){
									$('#Email').val(res.email);
									
								}else{
									$('#Email').val();
								}
								if(res.addressLine2 != 'null'){
									$('#addressLine2').val(res.addressLine2);
								}else{
									$('#addressLine2').val();
								}
								if(res.zipcode != 'null'){
									$('#zipcode').val(res.zipcode);
								}else{
									$('#zipcode').val();
								}
								
								}else if(crType=="true" && CustomerCrHandler.crStepCount==0){
									 showMessage({title:'Warning', msg:'No User Exists With Given Details'});
								       event.preventDefault();	
								       return;
								}
								$('#error-message').html('');
								$('#error-message').hide();
								$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).hide();
								$(CustomerCrHandler.crSteps[++CustomerCrHandler.crStepCount]).show();
								if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length) {
									if(!PageHandler.expanded){
										PageHandler.hidePageSelection();// this
																		// is to
																		// enlarge
																		// preview
																		// container
																		// on
																		// loading
																		// page
									}
									else{
										
										PageHandler.pageSelectionHidden =false;
										PageHandler.hidePageSelection();
									}
									$('#button-cr-next').hide();
									$('#action-clear').hide();
									$('#button-cr-save').show();
									$.post('customer/customer_change_request_view.jsp','viewType=preview',function(data) {
										 $('#customer-preview-container').css({'height' : '350px'});
											$('#customer-preview-container').html(data);
											$('.table-field').css({"width":"800px"});
											$('.main-table').css({"width":"400px"});
											$('.inner-table').css({"width":"400px"});
											$('.display-boxes-colored').css({"width":"140px"});
											$('.display-boxes').css({"width":"255px"});
											$('#customer-preview-container').show();
											CustomerCrHandler.expanded=false;
										$('#ps-exp-col').click(function() {
												if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length)
												{
												if(!PageHandler.expanded) {
											    	$('#customer-preview-container').css({'height' : '350px'});
													$('#customer-preview-container').html(data);
													$('.table-field').css({"width":"800px"});
													$('.main-table').css({"width":"400px"});
													$('.inner-table').css({"width":"400px"});
													$('.display-boxes-colored').css({"width":"140px"});
													$('.display-boxes').css({"width":"255px"});
													$('#customer-preview-container').show();
													CustomerCrHandler.expanded=false;
											    }
											    else{
											    	$('#customer-preview-container').css({'height' : '350px'});
													$('#customer-preview-container').html(data);
													$('.table-field').css({"width":"662px"});
													$('.main-table').css({"width":"330px"});
													$('.inner-table').css({"width":"330px"});
													$('.display-boxes-colored').css({"width":"125px"});
													$('.display-boxes').css({"width":"200px"});
													$('#customer-preview-container').show();
													CustomerCrHandler.expanded=true;
											    }
												
												}
										   
										});		});
								}
								
								if (CustomerCrHandler.crStepCount > 0) {
									$('#button-cr-prev').show();
									$('.page-buttons').css(
											'margin-left', '150px');

								} else {
									$('#button-cr-prev').hide();
									$('.page-buttons').css(
											'margin-left', '200px');
								}
							},
							error : function(data) {
								$('#error-message').html(data.responseText);
								$('#error-message').dialog();
								$('#error-message').show();
							}
						});
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
											$('.task-page-container').html('');
							    			var container ='.transaction-page-container';
							    			var url = "my-sales/transactions/delivery_note_add.jsp";
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
					
		$('#button-cr-prev')
		.click(
				function() {
					$('#action-clear').show();
					if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length) {
						if(!CustomerCrHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
							CustomerCrHandler.expanded=true;
						}

						$('#button-cr-next').show();
						$('#button-cr-save').hide();
						$('#button-update').hide();
						$('#customer-preview-container').html('');
						$('#customer-preview-container').hide();
						$('.page-buttons').css('margin-left', '150px');
					}
					$(
							CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount])
							.hide();
					$(
							CustomerCrHandler.crSteps[--CustomerCrHandler.crStepCount])
							.show();
					if (CustomerCrHandler.crStepCount > 0) {
						$('#button-cr-prev').show();
						$('.page-buttons').css('margin-left', '150px');
					} else {
						$('#button-cr-prev').hide();
						$('.page-buttons').css('margin-left', '240px');
					}
				});
		$('#button-cr-save').click(function() {
			var thisButton = $(this);
			var paramString = 'action=save-customer-cr';
			$('.page-content').ajaxSavingLoader();
			$.post('customerCr.json', paramString, function(obj) {
				$.loadAnimation.end();
				$(this).successMessage({
					container : '.transaction-page-container',
					data : obj.result.message
				});
			});
		});
		$('#business-name-suggestions').click(function (){
	        var customerBusinessName = $('#businessName').val();
	        $.post('customerCr.json', 'action=check-customer-cr-type&customerBusinessName='+customerBusinessName,
			        function(obj){
	        	    var isApproved = obj.result.data;
	        	    if(isApproved == "y"){
	        	    	//allow customer to make CR
	        	    }else{
	        	    	 showMessage({title:'Warning', msg:"Customer Changed request Not Approved Till Now."});
					       event.preventDefault();	
					       return;
	        	    }
	        	    }
		       );
	      }); 
		},
initSearchCrOnLoad: function() {
	var paramString='action=search-cr-onload';
	$.post('customerCr.json', paramString,
	function(obj){
		var data = obj.result.data;
		$('#search-results-list').html('');
				if(data != undefined) {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate"style="height:70px;">';
						} else {
							rowstr = '<div class="green-result-row"style="height:70px;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1">'+
						'<div class="result-title">' + data[loop].businessName  + '</div>' +
						'<div class="result-body">' +
						'<span class="property">'+ Msg.CUSTOMER_CR_TYPE +'</span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].crType +'</span>' +
						
						'</div>' +'<span class="property">'+Msg.CUSTOMER_EMAIL+' </span><span class="property-value">' + fmt(data[loop].email) + '</span>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + fmt(data[loop].mobile) + '</span><br/>' +
						'<span class="property">'+'</span>'+
						'<span class="property">'+Msg.CUSTOMER_ALTERNATE_NUMBER+' </span><span class="property-value">' + fmt(data[loop].alternateMobile) + '</span><br/>' +
						'</div>' +
						'</div>' +						
						'<div class="green-result-col-action"style="height:57px;">' + 
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="Approve Cr Details"></div>';
				
						rowstr = rowstr + '<div id="'+data[loop].id+'" class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
						'</div>' +
						'</div>';
				$('#search-results-list').append(rowstr);
			};
			CustomerCrHandler.initCrSearchResultButtons ();
			$('#search-results-list').jScrollPane({showArrows:true});
				}
				 else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
					}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
			});
},
initSearchCrCustomer : function(role) {
$('#action-search-customer-cr').click(function(){
	var thisButton = $(this);
	var paramString = $('#customer-cr-search-form').serialize();
	$('#search-results-list').ajaxLoader();
	$.post('customerCr.json', paramString, function(obj) {
		var data = obj.result.data;
   		$('#search-results-list').html('');
        if(data!=undefined){
		var alternate = false;
		for(var loop=0;loop<data.length;loop=loop+1){
			if(alternate) {
				var rowstr = '<div class="green-result-row alternate"style="height:70px;">';
			} else {
				rowstr = '<div class="green-result-row"style="height:70px;">';
			}
			alternate = !alternate;
		rowstr = rowstr + '<div class="green-result-col-1">'+
		'<div class="result-title">' + data[loop].businessName  + '</div>' +
		'<div class="result-body">' +
		'<span class="property">'+ Msg.CUSTOMER_CR_TYPE +'</span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].crType +'</span>' +
		
		'</div>' +'<span class="property">'+Msg.CUSTOMER_EMAIL+' </span><span class="property-value" style="font: bold 14px arial;">' + fmt(data[loop].email) + '</span>' +
		'</div>' +
		'<div class="green-result-col-2">'+
		'<div class="result-body">' +
		'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + fmt(data[loop].mobile) + '</span><br/>' +
		'<span class="property">'+'</span>'+
		'<span class="property">'+Msg.CUSTOMER_ALTERNATE_NUMBER+' </span><span class="property-value" style="font: bold 14px arial;">' + fmt(data[loop].alternateMobile) + '</span><br/>' +
    	'</div>' +
		'</div>' +
		'<div class="green-result-col-action"style="height:57px;">' + 
		'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Customer Details"></div>';
         $('#search-results-list').append(rowstr);
		};
		CustomerCrHandler.initCrSearchResultButtons();
		$('#search-results-list').jScrollPane({showArrows:true});
	} else {
		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
	}
		$.loadAnimation.end();
		setTimeout(function(){
			$('#search-results-list').jScrollPane({showArrows:true});
		},0);
	});
});
},
initCrSearchResultButtons : function(){
	$('.btn-view').click(function() {
		var id = $(this).attr('id');
		$.post('customerCr.json', 'action=get-customer-change-request-credit-data&id='+id, function(obj) {
			var result = obj.result.data;
			$.post('customer/customer_change_request_credit.jsp', 'id='+id,
			        function(data){
			        	$('#customer-view-container').html(data);  
			        	
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
				        	$('.alternateNumber').text(result.alternateMobile);
				        	$('.address1').text(result.addressLine1);
				        	$('.region').text(result.region);
				        	$('.address2').text(result.addressLine2);
				        	$('.locality').text(result.locality);
				        	$('.landMark').text(result.landmark);
				        	$('.city').text(result.city);
				        	$('.state').text(result.state);
				        	$('.zipCode').text(result.zipcode);
			        	$("#customer-view-dialog").dialog({
			    			autoOpen: true,
			    			height: 485,
			    			width: 640,
			    			modal: true,
			    			buttons: {
			    		      Close: function() {
			    			      $(this).dialog('close');
			    		          }
			    			},
			    			close: function() {
			    				$('#customer-view-container').html('');
			    			}
			    		});
			        }
			    );
		});
  });
},
initCheckNewCustomerCRBusinessName: function() {
	$("#businessName").focus(function(event){
		$('#businessNameValid').empty();
	});

	$("#businessName").blur(function(event){
		if($('#businessName').val()==''){
		}else
			{
			var businessname=$('#businessName').val();
			var paramString='businessName='+businessname+'&action=validate-new-customer-CR-businessName';
			$.post('customerCr.json',paramString,
			        function(data){
	            var delay = function() {
	            	CustomerCrHandler.AjaxSucceeded(data);
	            	};
	            	setTimeout(delay, 0);
			});
			
			}
	});
},
AjaxSucceeded: function(data1) {
    if (data1.result.data == "true") {
    	CustomerCrHandler.flag=true;
    	 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> ");
    	 $("#businessName").focus(function() {
    		 $('#businessnamevalid_pop').hide();
    	 });
    }else {
        $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>");
        CustomerCrHandler.flag=false;
               $("#businessName").focus(function() {
            	   if($('#businessName').val()!="") {
            	   $('#businessName_pop').hide();
               	 $('#businessnamevalid_pop').show();
               	 $('#businessNameValid').empty();
            	   }
               });
        $("#businessName").blur(function(event){
        	var end=$('#businessName').val().length;
        	 $('#businessnamevalid_pop').hide();
        });
    }
    
},

load:function() {
	$('#crType').change(function() {
		var crType = $('#crType').val();
		if(crType == 'false') {
			$('.transaction-page-container').load('customer/customer_change_request_add.jsp');
		} else {
			$('#businessName').val('');
			$('#customerName').val('');
			$('#businessNameValid').hide();
		}
	});
	
	$('#businessName').click(function() {
		$('#businessnamevalid_pop').hide();
		var crtype=$('#crType').val();
		var thisInput = $(this);
		if(crtype=="true"){
		$('#business-name-suggestions').show();
		CustomerCrHandler.suggestBusinessName(thisInput);
	}
	});
	$('#businessName').keyup(function() {
		var thisInput = $(this);
		$('#business-name-suggestions').animate({
			display : 'none'
		}, 500, function() {
			$('#business-name-suggestions').hide();
		});
		var thisInput = $(this);
		var crtype=$('#crType').val();
		if(crtype=="true"){
		$('#business-name-suggestions').show();
		CustomerCrHandler.suggestBusinessName(thisInput);
		var businessName = $('#businessName').val();
		$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
			$('#customerName').val(obj.result.data.customerName);
		});
	    }
	});
	$('#businessName').focusout(function() {
		$('#business-name-suggestions').animate({
			display : 'none'
		}, 1000, function() {
			$('#business-name-suggestions').hide();
		});
	});
},
validateApprovedOrDeclineCustomerCR: function(){
	var customerBusinessName = $('#businessName').val();
		$.ajax
		    ({
		        type: "POST",
		        url: 'customerCr.json',
		        data: 'action=check-customer-cr-type&customerBusinessName='+customerBusinessName,
		        async: false,
		        success:function(obj){
		        	 var isApproved = obj.result.data;
		        	  if(isApproved == "y"){
		        	    	//allow customer to make CR if CR is Approved,Decline
		        		  CustomerCrHandler.status=true;
		        	      }else{
		        	    	  //do not allow customer to make CR before Approval,Decline ie. CR i Pending state.
		        	    	  CustomerCrHandler.status=false;
		        	      }
		        },
		       error:function (request, status, error) {alert(status);}
		   });
		return CustomerCrHandler.status;
},
/*validateCustomerCr:function(){
	var result=true;
	var end=$('#businessName').val().length;
	if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			$('#businessnamelen_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessName_pop').show();
		});
	
	$("#businessName").blur(function(event){
		 $('#businessName_pop').hide();
		 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
			 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#businessName").focus(function(event){
				 $('#businessNameValid').empty();
				 $('#businessName_pop').show();
			 });
			 
		 }else{
			// $('#cityValid').html("<img
			// src='"+THEMES_URL+"images/available.gif' alt=''>");
		 }
	});
	result=false;
	}
	if($('#businessName').val().length>200){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			 //$('#businessName_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessnamelen_pop').show();
		});
		$("#businessName").blur(function(event){
			$('#businessnamelen_pop').hide();
			if($('#businessName').val().length>200){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessNameValid').empty();
					 $('#businessnamelen_pop').show();
				});
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessnamelen_pop').hide();
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
				});
			
			$("#businessName").blur(function(event){
				 $('#businessName_pop').hide();
				 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" " ){
					 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#businessName").focus(function(event){
						 $('#businessNameValid').empty();
						 $('#businessName_pop').show();
					 });
					 
				 }else{
					// $('#cityValid').html("<img
					// src='"+THEMES_URL+"images/available.gif' alt=''>");
				 }
			});
			result=false;
			}
		});
		result=false;
	}

	var cname= $('#customerName').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			 $('#cnamelen_pop').hide();
			$('#customerNameValid').empty();
			 $('#cname_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cname_pop').hide();
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
					 $('#cname_pop').show();
				 });
				 
			 }else{
				 $('#customerNameValid').empty();
			 }
		});
		result=false;
	}
	if($('#customerName').val().length>200){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			$('#cname_pop').hide();
			$('#customerNameValid').empty();
			 $('#cnamelen_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cnamelen_pop').hide();
			 if($('#customerName').val().length>200){
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
				 $('#cnamelen_pop').show();
				 });
			 }if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
					$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#customerName").focus(function(event){
						 $('#cnamelen_pop').hide();
						$('#customerNameValid').empty();
						 $('#cname_pop').show();
					});
					$("#customerName").blur(function(event){
						 $('#cname_pop').hide();
							 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0){
							 $("#customerName").focus(function(event){
								 $('#customerNameValid').empty();
								 $('#cname_pop').show();
							 });
							 
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	return result;
},
validateCustomerCrStepOne: function(){
	var result=true;
	var invEnd =$('#invoiceName').val().length;
	if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#inamelen_pop').hide();
			$('#invoiceNameValid').empty();
			 $('#iname_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#iname_pop').hide();
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#iname_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	if($('#invoiceName').val().length>200){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#invoiceNameValid').empty();
			 $('#inamelen_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#inamelen_pop').hide();
			 if($('#invoiceName').val().length>200){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#inamelen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
					$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#invoiceName").focus(function(event){
						$('#inamelen_pop').hide();
						$('#invoiceNameValid').empty();
						 $('#iname_pop').show();
					});
					$("#invoiceName").blur(function(event){
						 $('#iname_pop').hide();
						 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
							 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#invoiceName").focus(function(event){
								 $('#invoiceNameValid').empty();
							 $('#iname_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	
	var mobile=$('#mobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			 $('#mobilelen_pop').hide();
			$('#mobileValid').empty();
			 $('#mobile_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobile_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobileValid').empty();
					 $('#mobile_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#mobile').val().length>60){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			$('#mobile_pop').hide();
			$('#mobileValid').empty();
			 $('#mobilelen_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobilelen_pop').hide();
			 if($('#mobile').val().length>60){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobile_pop').hide();
					 $('#mobileValid').empty();
				 $('#mobilelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
					$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#mobile").focus(function(event){
						 $('#mobilelen_pop').hide();
						$('#mobileValid').empty();
						 $('#mobile_pop').show();
					});
					$("#mobile").blur(function(event){
						 $('#mobile_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
							 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#mobile").focus(function(event){
								 $('#mobileValid').empty();
								 $('#mobile_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var elen= $('#Email').val().length;
	var Email = $('#Email').val();
	if($('#Email').ValidateEmailAddr(Email) == false&&  $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			 $('#emaillen_pop').hide();
			$('#emailValid').empty();
			 $('#email_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#email_pop').hide();
			 if($('#Email').ValidateEmailAddr($('#Email').val()) == false&& $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#Email').val().length>100){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			$('#email_pop').hide();
			$('#emailValid').empty();
			 $('#emaillen_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#emaillen_pop').hide();
			 if($('#Email').val().length>100){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#email_pop').hide();
					 $('#emailValid').empty();
				 $('#emaillen_pop').show();
				 });
			 }if($('#Email').ValidateEmailAddr(Email) == false&& $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
					$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#Email").focus(function(event){
						 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
					$("#Email").blur(function(event){
						 $('#email_pop').hide();
						 if($('#Email').ValidateEmailAddr(Email) == false&& $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
							 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#Email").focus(function(event){
								 $('#emaillen_pop').hide();
								 $('#emailValid').empty();
								 $('#email_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
	}
	if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#inamelen_pop').hide();
			$('#invoiceNameValid').empty();
			 $('#iname_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#iname_pop').hide();
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#iname_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	var dlen =$("#directLine").val().length
	if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#directLineValid').empty();
			 $('#dline_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dline_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#directLineValid').empty();
					 $('#dline_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#directLine').val().length>60){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#dline_pop').hide();
			$('#directLineValid').empty();
			 $('#dlinelen_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dlinelen_pop').hide();
			 if($('#directLine').val().length>60){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#dline_pop').hide();
					 $('#directLineValid').empty();
				 $('#dlinelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
					$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#directLine").focus(function(event){
						 $('#dlinelen_pop').hide();
						$('#directLineValid').empty();
						 $('#dline_pop').show();
					});
					$("#directLine").blur(function(event){
						 $('#dline_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
							 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#directLine").focus(function(event){
								 $('#directLineValid').empty();
								 $('#dline_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var almlen =$('#alternateMobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#altmobileValid').empty();
			 $('#alternate_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternate_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#altmobileValid').empty();
					 $('#alternate_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#alternateMobile').val().length>60){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#alternate_pop').hide();
			$('#altmobileValid').empty();
			 $('#alternatelen_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternatelen_pop').hide();
			 if($('#alternateMobile').val().length>60){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#alternate_pop').hide();
					 $('#altmobileValid').empty();
				 $('#alternatelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
					$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#alternateMobile").focus(function(event){
						 $('#alternatelen_pop').hide();
						$('#altmobileValid').empty();
						 $('#alternate_pop').show();
					});
					$("#alternateMobile").blur(function(event){
						 $('#alternate_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
							 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#alternateMobile").focus(function(event){
								 $('#altmobileValid').empty();
								 $('#alternate_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var reglen=$('#region').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
		$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#region").focus(function(event){
			$('#regionValid').empty();
			 $('#region_pop').show();
		});
		$("#region").blur(function(event){
			 $('#region_pop').hide();
			 if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
				 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#region").focus(function(event){
					 $('#regionValid').empty();
					 $('#region_pop').show();
				 });
				
			 }if($('#region').val().length>60){
					$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#region").focus(function(event){
						$('#region_pop').hide();	
						$('#regionValid').empty();
						 $('#regionlen_pop').show();
					});
					$("#region").blur(function(event){
						 $('#regionlen_pop').hide();
						 if($('#region').val().length>60){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionValid').empty();
							 $('#regionlen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false || ($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionlen_pop').hide();
								 $('#regionValid').empty();
								 $('#region_pop').show();
							 });
							 $("#region").blur(function(event){
								 $('#region_pop').hide();
								 if(/^[0-9]+$/.test($('#region').val())==false||($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
									 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#region").focus(function(event){
										 $('#regionlen_pop').hide();
										 $('#regionValid').empty();
										 $('#region_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#regionValid').empty();
						 }
					});
					result=false;
			 }
		});
		result=false;
	}
	var addlen=$('#addressLine1').val().length ;
	if($('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1_pop').hide();
			 if($('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#addressLine1').val().length>200){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1len_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1len_pop').hide();
			 if($('#addressLin1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1len_pop').show();
					});
			 }else{
			 }
		});
	}
	var add2len=$('#addressLine2').val().length;
	if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
		 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		 $("#addressLine2").focus(function(event){
				$('#address2Valid').empty();
				 $('#addressLine2_pop').show();
			});
		 $("#addressLine2").blur(function(event){
		 $('#addressLine2_pop').hide();
			if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
			 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#addressLine2").focus(function(event){
					$('#address2Valid').empty();
					 $('#addressLine2_pop').show();
				});
		 }else{
		 }
	});
		 result=false;
	 }
	var lmlen=$('#landmark').val().length;
	if(($('#landmark').val()).length > 0){
	if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmark_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmark_pop').hide();
			 if(($('#landmark').val()).length > 0){
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
					 $('#landmark_pop').show();
				 });
				
			 }
			 else{
				 $('#landmarkValid').empty();
			 }
			 }
		});
		result=false;
	}else {
		 $('#landmarkValid').empty();
	}
}
	if($('#landmark').val().length>60){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmarklen_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmarklen_pop').hide();
			 if($('#landmark').val().length>60){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
				 $('#landmarklen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false ||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
					$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#landmark").focus(function(event){
						$('#landmarklen_pop').hide();
						$('#landmarkValid').empty();
						 $('#landmark_pop').show();
					});
					$("#landmark").blur(function(event){
						 $('#landmark_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false ||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
							 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#landmark").focus(function(event){
								 $('#landmarkValid').empty();
							 $('#landmark_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var loclen=$('#locality').val().length ;
	if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#locality_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#locality_pop').hide();
			 if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
					 $('#locality_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}	if($('#locality').val().length>60){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#localitylen_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#localitylen_pop').hide();
			 if($('#locality').val().length>60){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
				 $('#localitylen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
					$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#locality").focus(function(event){
						$('#localitylen_pop').hide();
						$('#localityValid').empty();
						 $('#locality_pop').show();
					});
					$("#locality").blur(function(event){
						 $('#locality_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
							 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#locality").focus(function(event){
								 $('#localityValid').empty();
							 $('#locality_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var citylen=$('#city').val().length;
	if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
		// $('#city_pop').show();
		$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	    // $('#city').focus();
		$("#city").focus(function(event){
			$('#cityValid').empty();
			 $('#city_pop').show();
		});
		$("#city").blur(function(event){
			 $('#city_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0 ||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
				 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#city").focus(function(event){
					 $('#cityValid').empty();
					 $('#city_pop').show();
				 });
				
			 }if($('#city').val().length>50){
					$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#city").focus(function(event){
						$('#city_pop').hide();	
						$('#cityValid').empty();
						 $('#citylen_pop').show();
					});
					$("#city").blur(function(event){
						 $('#citylen_pop').hide();
						 if($('#city').val().length>60){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#cityValid').empty();
							 $('#citylen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false&&$('#city').val().length>0 ||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#citylen_pop').hide();
								 $('#cityValid').empty();
								 $('#city_pop').show();
							 });
							 $("#city").blur(function(event){
								 $('#city_pop').hide();
								 if(/^[0-9]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
									 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#city").focus(function(event){
										 $('#citylen_pop').hide();
										 $('#cityValid').empty();
										 $('#city_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#cityValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length>0||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
		$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#state").focus(function(event){
			$('#stateValid').empty();
			 $('#state_pop').show();
		});
		$("#state").blur(function(event){
			 $('#state_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
				 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#state").focus(function(event){
						$('#stateValid').empty();
						 $('#state_pop').show();
					});
			 }
			 if($('#state').val().length>60){
					$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#state").focus(function(event){
						$('#state_pop').hide();	
						$('#stateValid').empty();
						 $('#statelen_pop').show();
					});
					$("#state").blur(function(event){
						 $('#statelen_pop').hide();
						 if($('#state').val().length>60){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#stateValid').empty();
							 $('#statelen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false &&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#statelen_pop').hide();
								 $('#stateValid').empty();
								 $('#state_pop').show();
							 });
							 $("#state").blur(function(event){
								 $('#state_pop').hide();
								 if(/^[0-9]+$/.test($('#state').val())==false&&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
									 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#state").focus(function(event){
										 $('#statelen_pop').hide();
										 $('#stateValid').empty();
										 $('#state_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#stateValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	var zipcodelen=$('#zipcode').val().length;
	if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" " ){
		// $('#pincode_pop').show();
		$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#zipcode").focus(function(event){
			$('#zipcodeValid').empty();
			 $('#pincode_pop').show();
		});
		$("#zipcode").blur(function(event){
			 $('#pincode_pop').hide();
				if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
				 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#zipcode").focus(function(event){
					 $('#zipcodeValid').empty();
					 $('#pincode_pop').show();
				 });
				
			 }else{
				 // $('#pincodeValid').html("<img
					// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#zipcode').val().length>10){
			$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincode_pop').hide();	
				$('#zipcodeValid').empty();
				 $('#pincodelen_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincodelen_pop').hide();
				 if($('#zipcode').val().length>10){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#zipcodeValid').empty();
					 $('#pincodelen_pop').show();
					 });
				 } if(/^[0-9]+$/.test($('#zipcode').val())==false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#pincodelen_pop').hide();
						 $('#zipcodeValid').empty();
						 $('#pincode_pop').show();
					 });
					 $("#zipcode").blur(function(event){
						 $('#pincode_pop').hide();
						 if(/^[0-9]+$/.test($('#zipcode').val())==false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
							 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#zipcode").focus(function(event){
								 $('#pincodelen_pop').hide();
								 $('#zipcodeValid').empty();
								 $('#pincode_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
				 }
				 else{
					 $('#zipcodeValid').empty();
				 }
			});
			result=false;
			
	}
	return result;
},*/
suggestBusinessName : function(thisInput) {
	var suggestionsDiv = $('#business-name-suggestions');
	var val = $('#businessName').val();
	$.post('customer.json','action=get-SE-assigned-business-name&businessNameVal=' + val,function(obj) {
						$.loadAnimation.end();
						$('#businessnamevalid_pop').hide();
						suggestionsDiv.html('');
						var data = obj.result.data;
						if (data.length > 0) {
							var htmlStr = '<div>';
							for ( var loop = 0; loop < data.length; loop = loop + 1) {
								htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
										+ data[loop].businessName
										+ '</a></li>';
							}
							htmlStr += '</div>';
							suggestionsDiv.append(htmlStr);
						} else {
							suggestionsDiv.append('<div id="">'
									+ 'No Business Names' + '</div>');
						}
						suggestionsDiv.css('left',thisInput.position().left);
						suggestionsDiv.css('top',thisInput.position().top + 25);
						suggestionsDiv.show();
						$('.select-teacher').click(
								function() {
									thisInput.val($(this).html());
									thisInput.attr('businessName', $(this)
											.attr('businessName'));
									$('#businessName').attr('value',$(this).attr('businessName'));
									$('#businessnamevalid_pop').hide();
									suggestionsDiv.hide();
									var businessName = $('#businessName').val();
									$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
										$('#customerName').val(obj.result.data.customerName);
										var gender = obj.result.data.gender;
										if(gender == 'M'){
											$('#male').attr('checked','checked');
										}else{
											$('#female').attr('checked','checked');
										}
									
								});
						

								});
					});
	
       },

};
