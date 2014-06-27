
var TransactionHandler = {

	initPageLinks : function() {
		$('#view-transaction').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/view-transactions/delivery_note_default_view.jsp'
		});

		$('#add-transaction').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/delivery_note_add.jsp'
		});
		
		$('#add-sales-returns').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/sales_returns_add.jsp'
		});
		
		$('#add-credit-note').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/credit_note_add.jsp'
		});
		
		$('#view-credit-note').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/view-transactions/credit_note_view.jsp'
		});
		
		$('#view-sales-returns').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/view-transactions/sales_returns_view.jsp'
		});
		
		$('#view-day-book').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/view-transactions/day_book_search.jsp'
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
		$('#search-journal').pageLink({
			container : '.transaction-page-container',
			url : 'my-sales/transactions/view-transactions/search_journals.jsp'
		});

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
		
		$('#action-clear').click(function() {
			$(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).clearForm();
		});
		$('#button-next').click(function() {
			var resultSuccess=true;
			var resultFailure=false;
			if(DeliveryNoteHandler.deliverynoteStepCount==0){
				if(DeliveryNoteHandler.validatein()==false){
					return resultSuccess;
				}
			}
			if ($(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).validate() == false)
				return;
			$('.delivery-note-separator').css('width',"80px");
			var paramString = $(DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount]).serializeArray();
			var totalCost=$('#totalCost').val();
			var presentPayableVal = $("#presentPayable").val();
			var previousCreditVal = $("#previousCredit").val();
			var presentAdvanceVal =$("#presentAdvance").val();
			var totalPayableVal = parseFloat(currencyHandler.convertStringPatternToFloat(presentPayableVal)) + parseFloat(currencyHandler.convertStringPatternToFloat(previousCreditVal)) - parseFloat(currencyHandler.convertStringPatternToFloat(presentAdvanceVal));
			$('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal.toFixed(2)));
			var presentPaymentVal =$("#presentPayment").val();
			var totalPayable = $('#totalPayable').val();
			var balance =$("#balance").val();
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
					//convert serialized array to url-encoded string
					
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
												        		  listOfObjects +=','+productQty+'|'+batchNumber+'|'+bonusQty+'|'+bonusReason+'|'+productName+'|'+productCost;
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
							$(
									DeliveryNoteHandler.deliverynoteSteps[DeliveryNoteHandler.deliverynoteStepCount])
									.hide();
							$(
									DeliveryNoteHandler.deliverynoteSteps[--DeliveryNoteHandler.deliverynoteStepCount])
									.show();
							if (DeliveryNoteHandler.deliverynoteStepCount > 0) {
								$('#button-prev').show();
								$('.page-buttons').css('margin-left', '150px');
								//DeliveryNoteHandler.expanded=true;
							} else {
								if(PageHandler.expanded){
									PageHandler.pageSelectionHidden =false;
									PageHandler.hidePageSelection();
									DeliveryNoteHandler.expanded=true;
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
			if($('#forPayments').is(':checked')){
				var paramString = 'action=save-payment-delivery-note';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$.post('deliveryNote.json', paramString, function(obj) {
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
			} else {
				var paramString = 'action=save-deliverynote';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$.post('deliveryNote.json', paramString, function(obj) {
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
			}
		});
		$('#add-transaction').click(function() {
			$('.transaction-page-container').load('my-sales/transactions/delivery_note_add.jsp');
		});
		$('#action-cancel').click(function() {
							$('#error-message').html('You will loose unsaved data.. Clear form?');
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
			var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
			var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
			$('#presentPayment').val(formatPresentPayment);
					var totalPayableVal = currencyHandler.convertStringPatternToFloat($("#totalPayable").val());
					var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
					$('#balance').val(currencyHandler.convertFloatToStringPattern((totalPayableVal - presentPaymentVal).toFixed(2)));
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
	            $('.gridDisplay').hide();
	        }
	        else{
	        	 $('.green-results-list').html('');
	        	 DeliveryNoteHandler.loadGrid();
	        	}
	    }); 
		/*$('#businessName').change(function() {
			DeliveryNoteHandler.getInvoiceName();
		});*/
		$('#paymentType').change(function(){
			if($('#paymentType').val().toLowerCase()=='cheque'){
				$('#bankname').show();
				$('#bankName').attr('class','mandatory');
				$('#branchname').show();
				$('#chequeno').show();
				$('#chequeNo').attr('class','mandatory');
			}else{
				$('#bankname').hide().find("input").val("");
				$('#bankName').removeAttr('class');
				$('#branchname').hide().find("input").val("");
				$('#chequeno').hide().find("input").val("");
				$('#chequeNo').removeAttr('class');
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
			          $(this).find('#row5').html(currencyHandler.convertFloatToStringPattern(totalCost.toFixed(2)));
			          sum += totalCost;
			          $('#totalCost').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
			          $('#presentPayable').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
			          $(this).find('.productQuantity').val(currencyHandler.convertNumberToStringPattern(productQty));
			          $(this).find('.bonus').val(currencyHandler.convertNumberToStringPattern(bonusQty));
			});
		});
	},
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
											$('#previousCredit').val(obj.result.data.customerCredit);
											$('#presentAdvance').val(obj.result.data.customerAdvance);
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
	addColor: function(number) {
		if(number%2 !=0){
			$('#row-'+number).css({'background-color' : 'LightGray'});
		}
		else
		$('#row-'+number).css({'background-color' : 'FloralWhite'});
		
	},
	
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
							rowstr+='<div id="row6" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 85px">'+data[x].availableQuantity+'</div>';
							rowstr+='<div id="row4" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 85px">'+data[x].productCost+'</div>';
							rowstr+='<div id="row2" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px">'+'<input type = "text" style="margin-top:-3px;" class="productQuantity" size=10px>'+'</div>';
							rowstr+='<div id="row3" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px">'+'<input type = "text" style="margin-top:-3px;" class="bonus" size=10px>'+'</div>';
							rowstr+='<div id="row7" class="report-body-column2 centered sameHeight" style="height: inherit; width: 85px">'+'<textarea rows="2" cols="2"  style="height: 41px; width: 85px; border: none; resize: none;" class="bonusReason"></textarea>'+'</div>';
							rowstr+='<div id="row5" class="report-body-column2 right-aligned totalCost sameHeight" style="height: inherit; width: 85px">'+data[x].totalCost+'</div>';
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
		
validatein :function(){
	var result=true;
	var invEnd=$('#invoiceName').val().length;
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
	return result;
},
};

var SalesReturnsHandler = {
		salesreturnsSteps : [ '#sales-returns-form'],
		salesreturnsUrl : [ 'salesReturn.json'],
		salesreturnsStepCount : 0,
		expanded: true,
		initAddButtons : function() {
			SalesReturnsHandler.salesreturnsStepCount = 0;
			$('#action-clear').click(function() {
				$(SalesReturnsHandler.salesreturnsSteps[SalesReturnsHandler.salesreturnsStepCount]).clearForm();
			});
			$('#button-save').click(function() {
				var thisButton = $(this);
				var paramString = 'action=save-sales-returns';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$.post('salesReturn.json', paramString, function(obj) {
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
				if(DeliveryNoteHandler.deliverynoteStepCount==0){
					if(DeliveryNoteHandler.validatein()==false){
						return resultSuccess;
					}
				}
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
								var grandTotalCost =currencyHandler.convertStringPatternToFloat($('#grandTotalCost').val());
								$('div#sales-row').each(function(index, value) {
									var id = $(this).find('div').attr('id');
									var productName = $(this).find('#row1').html();
									var batchNumber = $(this).find('#row2').html();
									var cost = currencyHandler.convertStringPatternToFloat($(this).find('#row3').html());
									var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#row5').html());
									var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
									var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
									var returnQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
									 $.post('salesReturn.json','action=get-quantity-sold&productName='+productName+'&batchNumber='+batchNumber+'&businessName='+businessName, function(obj) {
										 qtySold = obj.result.data; 
									  });
									 if(parseInt(returnQty) > parseInt(qtySold)) {
										  showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
									       return false;
									  } else {
										  if(damaged != "" || resalable != ""){
												if(index == 0) {
													listOfObjects +=damaged+'|'+resalable+'|'+cost+'|'+returnQty+'|'+totalCost+'|'+batchNumber+'|'+productName
												} else {
													listOfObjects +=','+damaged+'|'+resalable+'|'+cost+'|'+returnQty+'|'+totalCost+'|'+batchNumber+'|'+productName
												}
											}
									  }
								});
								var paramString = 'action=save-sales-return-products&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&grandTotalCost=' +grandTotalCost;
								if(grandTotalCost != 0) {
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
								} else {
									showMessage({title:'Warning', msg:'Please enter damaged or resalable quantity.'});
								    return false;
								}
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
			$("#add-sales-returns").click(function(){
				$('.transaction-page-container').load('my-sales/transactions/sales_returns_add.jsp');
				PageHandler.hidePageSelection();
				  $('.page-link-strip').hide();
					$('.module-title').hide();
					$('.page-selection').animate( { width:"55px"}, 0,function(){});
					$('.page-container').animate( { width:"835px"}, 0);
					var thisTheme = PageHandler.theme;
					$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			});
		    $('#action-cancel').click(function() {
				$('#error-message').html('You will loose unsaved data.. Clear form?');
				$("#error-message").dialog({	
					resizable : false,
					height : 140,
					title : "<span class='ui-dlg-confirm'>Confirm</span>",
					modal : true,
					buttons : {
						'Ok' : function() {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/sales_returns_add.jsp";
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
			
			$.post('salesReturn.json','action=lookup-sales-returns', function(obj) {
				var productNameOptions = $("#productName");
				$.each(obj.result.data, function(i, product) {
					productNameOptions.append('<option value="' + product + '" id="' + product+ '" option-code="' + product + '">' + product  + '</option>');
					$('#' + product).data('source', product);
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
				  $.each($('.report-body'), function(index, value) {
					  var productCost = currencyHandler.convertStringPatternToFloat($(this).find('#row3').html());
					  var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
					  var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
						if(damaged == "") {
							damaged = 0;
						}
						if(resalable == "") {
							resalable = 0;
						}
					  var returnQty = parseInt(damaged) + parseInt(resalable);
					  $(this).find('.returnQty').val(currencyHandler.convertNumberToStringPattern(returnQty));
					  $(this).find('.returnQty').attr('readonly','readonly');
				      var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
				      var totalCost = parseFloat(productCost) *  parseInt(productQty);
				      if(isNaN(totalCost)) {
				       	  totalCost = 0.00;
				       }
				      $(this).find('#row5').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
				      sum += totalCost;
			          $('#grandTotalCost').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
			          $(this).find('.damaged').val(currencyHandler.convertNumberToStringPattern(damaged));
					  $(this).find('.resalable').val(currencyHandler.convertNumberToStringPattern(resalable));
					  
					  $('#grandTotalCost').change(function() {
							var grandTotalCost = $('#grandTotalCost').val();
							if(grandTotalCost == '0'){
								$('button-next').hide();
							} else {
								$('button-next').show();
							}
						});
				  });
				});
			
			/*$('#grandTotalCost').change(function() {
				var grandTotalCost = $('#grandTotalCost').val();
				if(grandTotalCost == '0'){
					$('button-next').hide();
				} else {
					$('button-next').show();
				}
			});*/
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
					suggestionsDiv.append('<div id="">' + 'No Business Names' + '</div>');
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
				$.post('salesReturn.json', 'action=get-grid-data&businessName=' +businessName, function(obj) {
					var data = obj.result.data;
					if(data !== undefined){
						if(data.length>0) {
							$('.success-msg').hide();
							var count=1;
							for(var x=0;x<data.length;x=x+1) {
								var rowstr='<div class="ui-content report-content" id="sales-row">';
								rowstr=rowstr+'<div id="sales-return-row-'+count+'" class="report-body" style="width: 697px;height: auto; overflow: hidden;">';
								rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;" >'+data[x].productName+'</div>';
								rowstr=rowstr+'<div id="row2" class="report-body-column2 centered" style="height: inherit; width: 85px ;word-wrap: break-word;">'+data[x].batchNumber+'</div>';
								rowstr=rowstr+'<div id="row6" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;" id="damaged" class="damaged" size=10px>' + '</div>';
								rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;" id="resalable" class="resalable" size=10px>'+'</div>';
								rowstr=rowstr+'<div id="row4" class="report-body-column2 right-aligned" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text"  id="returnQty" class="returnQty" style = "margin-top:-3px; size: 4px; border: none;" readonly="readonly">'+'</div>';
								rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].cost+'</div>';
								rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].totalCost+'</div>';
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
							$( '.report-header-column2' ).css( "width", "100px" );
							$( '.report-body-column2' ).css( "width", "100px" );
							$( '.returnQty' ).css( "width", "100px" );		
					        $('.gridDisplay').show();
						}else{
						$( '#search-results-list' ).css( "width", "830px" );
						$( '.report-header' ).css( "width", "830px" );
				    	$( '.report-body' ).css( "width", "830px" );
						$( '.report-header-column2' ).css( "width", "100px" );
						$( '.report-body-column2' ).css( "width", "100px" );
						$( '.returnQty' ).css( "width", "100px" );		
				        $('.gridDisplay').show();
						}
						$('#ps-exp-col').click(function(){
							   if(PageHandler.expanded){
								   $( '#search-results-list' ).css( "width", "699px" );
								    $( '.report-header' ).css( "width", "699px" );
							    	$( '.report-body' ).css( "width", "699px" );
									$( '.report-header-column2' ).css( "width", "85px" );
									$( '.report-body-column2' ).css( "width", "85px" );
									$( '.returnQty' ).css( "width", "85px" );
							   }else{
								   $( '#search-results-list' ).css( "width", "830px" );
								   $( '.report-header' ).css( "width", "830px" );
							    	$( '.report-body' ).css( "width", "830px" );
									$( '.report-header-column2' ).css( "width", "100px" );
									$( '.report-body-column2' ).css( "width", "100px" );
									$( '.returnQty' ).css( "width", "100px" );
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
		
		//common function for displaying search results
		displaySearchResults:function(data){
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
					'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST +' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].total) + '</span>'+
					'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales Returns"></div>';
					'</div>' +
					'</div>';
			$('#search-results-list').append(rowstr);
		};
		SalesReturnsHandler.initSearchSalesReturnResultButtons();
		$('#search-results-list').jScrollPane({showArrows:true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
			$.loadAnimation.end();
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},300);
			 $('#action-clear')
				.click(
						function() {
							$('form').clearForm();
							$('#search-results-list')
									.html(
											'<tr><td colspan="4">Search Results will be show here</td></tr>');
							setTimeout(function() {
								$('#search-results-list').jScrollPane({
									showArrows : true
								});
							}, 300);
						});
		},
		//View sales return On page load
		initSearchSalesReturnOnLoad: function() {
			var paramString = $('#sales-return-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			$.post('salesReturn.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-results-list').html('');
				SalesReturnsHandler.displaySearchResults(data);
				
			}
			);
			},
			//End of search sales returns on load function
			
		
			initSearchSalesReturn : function(role) {
				//Stock search button click
				$('#action-search-sales-return').click(function() {
					var thisButton = $(this);
					var paramString = $('#sales-return-search-form').serialize();  
					$('#search-results-list').ajaxLoader();
					$.post('salesReturn.json', paramString,
				        function(obj){
					    	var data = obj.result.data;
							$('#search-results-list').html('');
							//for refreshing input fields after search
							$('form').clearForm();
							SalesReturnsHandler.displaySearchResults(data);
				        }
				    );
				});
				//end of search
				
				$('#ps-exp-col').click(function() {
					setTimeout(function() {
						$('#search-results-list').jScrollPaneRemove();
						$('#search-results-list').jScrollPane({
							showArrows : true
						});
					}, 0);
				});
				// button click - cancel
				$('#action-clear')
						.click(
								function() {
									$('#sales-return-search-form').clearForm();
									$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
									setTimeout(function() {
										$('#search-results-list').jScrollPane({
											showArrows : true
										});
									}, 300);
								});
				
				$('#customer-exp-coll').click(function() {
					if($(this).hasClass('expand-icon')) {
						$('#search-results-list').jScrollPaneRemove();
						$('#search-results-list').css('height', '428px');
						$('#search-results-list').jScrollPane({showArrows:true});
					} else if($(this).hasClass('collapse-icon')) {
						$('#search-results-list').jScrollPaneRemove();
						$('#search-results-list').css('height', '328px');
						$('#search-results-list').jScrollPane({showArrows:true});
					}
				});
				
			},
			//End of initSearchSalesReturn
			
initSearchSalesReturnResultButtons : function () {
				$('.btn-view').click(function() {
					var id = $(this).attr('id');
					$.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp', 'id='+id,
				        function(data){
						$('#sales-return-view-container').html(data);
							$("#sales-return-view-dialog").dialog('open');
				        }
			        );
				});
				$("#sales-return-view-dialog").dialog({
					autoOpen: false,
					height: 450,
					width: 1045,
					modal: true,
					buttons: {
						Close: function() {
							$(this).dialog('close');
						}
					},
					close: function() {
						$('#sales-return-view-container').html('');
					}
				});

			},
			//End of initSearchSalesReturnResultButtons
			
	};

var JournalHandler = {
		journalSteps : [ '#journal-form'],
		journalsUrl : [ 'journal.json'],
		journalStepCount : 0,
		expanded: true,
		initAddButtons : function() {
			JournalHandler.journalStepCount = 0;
			$('#action-clear').click(function() {
				$('form').clearForm();
			});
			$('#amount').blur(function() {
				var journalAmount=$('#amount').val();
				var formatJournalAmount=currencyHandler.convertStringPatternToFloat(journalAmount);
				var formatAmount=currencyHandler.convertFloatToStringPattern(formatJournalAmount.toFixed(2));
				$('#amount').val(formatAmount);
			});
			$('#button-save').click(function() {
				if(JournalHandler.validateJournals()==false){
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
				
				PageHandler.expanded=false;
				pageSelctionButton.click();
				$.post('journal.json', sendFormData, function(obj) {
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
				
		});
			$("#add-journal").click(function(){
				$('.transaction-page-container').load('my-sales/transactions/add_journals.jsp');
				PageHandler.hidePageSelection();
				  $('.page-link-strip').hide();
					$('.module-title').hide();
					$('.page-selection').animate( { width:"55px"}, 0,function(){});
					$('.page-container').animate( { width:"835px"}, 0);
					var thisTheme = PageHandler.theme;
					$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			});
		    $('#action-cancel').click(function() {
				$('#error-message').html('You will loose unsaved data.. Clear form?');
				$("#error-message").dialog({	
					resizable : false,
					height : 140,
					title : "<span class='ui-dlg-confirm'>Confirm</span>",
					modal : true,
					buttons : {
						'Ok' : function() {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/sales_returns_add.jsp";
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
	validateJournals : function(){
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
	},
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
										$.post('journal.json','action=get-invoice-name-for-journal&businessName='+businessName, function(obj) {
											$('#invoiceName').val(obj.result.data);
										});
									});
						});
	},
	searchJournalOnLoad:function(){
		 var paramString = $('#journal-search-form').serialize(); 
	    $.post(
	      'journal.json',paramString,
	      function(obj) {
	       var data = obj.result.data;
	       $('#default-results-list').html('');
	       JournalHandler.displayJournalSearchResults(data);

	 });

	},
	 searchJournal : function() {
		  $('#action-search-journals').click(function() {
			  if ($('#journal-search-form').validate()==false){
				  showMessage({title:'Error', msg:'Please Select A Journal Type'});
				  return;
			  }
		   var thisButton = $(this);
		   var paramString = $('#journal-search-form').serialize();  
		   $.post('journal.json', paramString,
		          function(obj){
		        var data = obj.result.data;
		        $('#search-results-list').html('');
		     JournalHandler.displayJournalSearchResults(data);
		     //for refreshing input fields after search
		     $('#businessName').clear();
		     $('#createdOn').clear();
		     

		   });
		  });
		 },
	displayJournalSearchResults:function(data){
		if (data!=null) {
	        var alternate = false;
	        for(var loop=0;loop<data.length;loop=loop+1) {
	        	var dateFormat=DayBookViewHandler.formatDate(data[loop].createdOn);
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
				'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Journal"></div>';
				'</div>' +
				'</div>';
		$('#search-results-list').append(rowstr);
	};
	JournalHandler.initDefaultResultButtons();
	        $('#search-results-list').jScrollPane({
				showArrows : true
			});
	       } else {
	        $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records found</div></div></div>');
	       }
	       $.loadAnimation.end();
	       setTimeout(function() {
	       }, 0);

	  $('#action-clear')
		.click(
				function() {
					$('form').clearForm();
					$('#search-results-list')
							.html(
									'<tr><td colspan="4">Search Results will be show here</td></tr>');
					setTimeout(function() {
						$('#search-results-list').jScrollPane({
							showArrows : true
						});
					}, 0);
				});
		    
	},
	initDefaultResultButtons:function(){
		$('.btn-view')
	    .click(
	      function() {
	       var id = $(this).attr('id');
	       $
	         .post(
	           'my-sales/transactions/view-transactions/journals_view.jsp',
	           'id=' + id,
	           function(data) {
	            $(
	              '#journals-view-container')
	              .html(data);
	            $(
	              "#journals-view-dialog")
	              .dialog('open');
	           });

	      });

	  $("#journals-view-dialog").dialog({
	   autoOpen : false,
	   height : 360,
	   width : 1040,
	   modal : true,
	   buttons : {
	    Close : function() {
	     $(this).dialog('close');
	    }
	   },
	   close : function() {
	    $('#journals-view-container').html('');
	   }
	  });

	 }
};

var DeliveryNoteViewHandler = {
		//Function to display results on load
		  initSearchDeliveryNoteOnLoad : function() {
			  var paramString = $('#delivery-note-search-form').serialize(); 
			  $('#search-results-list').ajaxLoader();
		    $.post(
		      'deliveryNote.json',paramString,
		      function(obj) {
		       var data = obj.result.data;
		       $('#default-results-list').html('');
		       DeliveryNoteViewHandler.displayDeliveryNoteSearchResults(data);

		 });
		  },
		 
		 displayDeliveryNoteSearchResults:function(data){
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
						'</div>' +'<span class="property">'+Msg.DELIVERY_NOTE_BALANCE_LABEL +' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].balance) + '</span>' +
						'</div>' +
						'<div class="green-result-col-action">' + 
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Delivery Note"></div>';
						'</div>' +
						'</div>';
				$('#search-results-list').append(rowstr);
			};
			        DeliveryNoteViewHandler
			          .initDefaultResultButtons();
			        $('#search-results-list').jScrollPane({
						showArrows : true
					});
			       } else {
			        $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records found</div></div></div>');
			       }
			       $.loadAnimation.end();
			       setTimeout(function() {
			       }, 0);

			  $('#action-clear')
				.click(
						function() {
							$('form').clearForm();
							$('#search-results-list')
									.html(
											'<tr><td colspan="4">Search Results will be show here</td></tr>');
							setTimeout(function() {
								$('#search-results-list').jScrollPane({
									showArrows : true
								});
							}, 0);
						});
				    
			},
		 
		 //This function is to search delivery note on criteria
		 initSearchDeliveryNote : function() {
		  $('#action-search-delivery-note').click(function() {
		   var thisButton = $(this);
		   var paramString = $('#delivery-note-search-form').serialize();  
		   $('#search-results-list').ajaxLoader();
		   $.post('deliveryNote.json', paramString,
		          function(obj){
		        var data = obj.result.data;
		     $('#search-results-list').html('');
		     //for refreshing input fields after search
		     $('form').clearForm();
		     DeliveryNoteViewHandler.displayDeliveryNoteSearchResults(data);
		   });
		  });
		 },

		 initDefaultResultButtons : function() {
		  $('.btn-view')
		    .click(
		      function() {
		       var id = $(this).attr('id');
		       $.post(
		           'my-sales/transactions/view-transactions/delivery_note_results_view.jsp',
		           'id=' + id,
		           function(data) {
		            $(
		              '#delivery-note-view-container')
		              .html(data);
		            $(
		              "#delivery-note-view-dialog")
		              .dialog('open');
		           });

		      });

		  $("#delivery-note-view-dialog").dialog({
		   autoOpen : false,
		   height : 550,
		   width : 1020,
		   modal : true,
		   buttons : {
		    Close : function() {
		     $(this).dialog('close');
		    }
		   },
		   close : function() {
		    $('#delivery-note-view-container').html('');
		   }
		  });

		 }

		};
var CustomerCrHandler = {
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
				$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).clear();
			});
			// clear button for customer credit form
			$('#action-clear-customer-credit').click(function() {
				$('form').clearForm();
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
				if(CustomerCrHandler.validateCustomerCr()==false){
					return resultSuccess;
				}
				}else if (CustomerCrHandler.crStepCount == 1) {
					if(CustomerCrHandler.validateCustomerCrStepOne()==false){
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
								if(res.landmark = null){
									$('#landmark').val();
								}else{
									$('#landmark').val(res.landmark);
								}
								if(res.alternateContactNumber= null){
									$('#alternateContactNumber').val();
								}else{
									$('#alternateContactNumber').val(res.alternateContactNumber);
								}
								if(res.directLine = null){
									$('#directLine').val();
								}else{
									$('#directLine').val(res.directLine);
								}
								$('#invoiceName').val(res.vbCustomer.invoiceName);
								if(res.city = null){
									$('#city').val();
								}else{
									$('#city').val(res.city);
								}
								if(res.state = null){
									$('#state').val();
								}else{
									$('#state').val(res.state);
								}
								if(res.addressLine1 = null){
									$('#addressLine1').val();
								}else{
									$('#addressLine1').val(res.addressLine1);
								}
								if(res.email = null){
									$('#Email').val();
								}else{
									$('#Email').val(res.email);
								}
								if(res.addressLine2 = null){
									$('#addressLine2').val();
								}else{
									$('#addressLine2').val(res.addressLine2);
								}
								if(res.zipcode = null){
									$('#zipcode').val();
								}else{
									$('#zipcode').val(res.zipcode);
								}
								
								}else if(crType=="true"&&CustomerCrHandler.crStepCount==0){
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
				$('#error-message').html('You will loose unsaved data.. Clear form?');
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
							    			var url = "customer/customer_change_request_add.jsp";
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
			$.post('customerCr.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.transaction-page-container',
					data : obj.result.message
				});
			});
		});
		$('#add-change-request').click(function() {
			$('.customer-page-container').load('customer/customer_change_request_add.jsp');
		});
		$('#business-name-suggestions').click(function (){
	        var customerBusinessName = $('#businessName').val();
	        $.post('customerCr.json', 'action=check-customer-cr-type&businessName='+customerBusinessName,
			        function(obj){
	        	    var data = obj.result.data;
	        	    if(data == 0){
	        	    	
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
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
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
        if(data!=null){
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
		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
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
		$.post('customer/customer_change_request_credit.jsp', 'id='+id,
	        function(data){
	        	$('#customer-view-container').html(data);  
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
},

load:function() {
	
	$('#businessName').click(function() {
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
		if(crtype=="true"){
		$('#business-name-suggestions').show();
		CustomerCrHandler.suggestBusinessName(thisInput);
	}
		var businessName = $('#businessName').val();
		$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
			
			$('#customerName').val(obj.result.data);
			
		});
	});
	$('#businessName').focusout(function() {
		$('#business-name-suggestions').animate({
			display : 'none'
		}, 1000, function() {
			$('#business-name-suggestions').hide();
		});
	});
},
validateCustomerCr:function(){
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
},
suggestBusinessName : function(thisInput) {
	var suggestionsDiv = $('#business-name-suggestions');
	var val = $('#businessName').val();
	$.post('customer.json','action=get-business-name&businessNameVal=' + val,function(obj) {
						$.loadAnimation.end();
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
									suggestionsDiv.hide();
									var businessName = $('#businessName').val();
									$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
										$('#customerName').val(obj.result.data);
									
								});
						

								});
					});
	
       },

};
var DayBookViewHandler = {
		displaySearchResults:function(data){
			if(data!=null)  {
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1) {
					var dateFormat=DayBookViewHandler.formatDate(data[loop].createdOn);
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
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Day Book"></div>';
					'</div>' +
					'</div>';
			$('#search-results-list').append(rowstr);
		};
		DayBookViewHandler.initDefaultResultButtons();
		  $('#search-results-list').jScrollPane({
		         showArrows : true
		        });
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
			$.loadAnimation.end();
			 setTimeout(function() {
			        $('#search-results-list').jScrollPane({
			         showArrows : true
			        });
			       }, 0);
			 $('#action-clear')
				.click(
						function() {
							$('#createdOn').clearForm();
							$('#search-results-list')
									.html(
											'<tr><td colspan="4">Search Results will be show here</td></tr>');
							setTimeout(function() {
								$('#search-results-list').jScrollPane({
									showArrows : true
								});
							}, 0);
						});
		},
		initSearchDayBookOnLoad: function() {
			$('#search-results-list').ajaxLoader();
			var paramString = $('#day-book-search-form').serialize();
			$.post('dayBook.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-results-list').html('');
				DayBookViewHandler.displaySearchResults(data);
			}
			);
			},
		addColor: function(number) {
			if(number%2 !=0){
				$('#row-'+number).css({'background-color' : 'LightGray'});
			}
			else{
				$('#row-'+number).css({'background-color' : 'FloralWhite'});
			}
		},
		initSearchDayBook:function(role){
			$('#action-search-day-book').click(function() {
				var thisButton = $(this);
				var paramString = $('#day-book-search-form').serialize();
				$('#search-results-list').ajaxLoader();
				$.post('dayBook.json', paramString,
			        function(obj){
				    	var data = obj.result.data;
				    	//for refreshing input field date after search
				    	$('#createdOn').clearForm();
						$('#search-results-list').html('');
						DayBookViewHandler.displaySearchResults(data);
						
			        });
			});
		},
		
		initDefaultResultButtons:function(){
			
			$('.btn-view').click(function() {
				var id = $(this).attr('id');
				$.post('my-sales/day-book/day_book_view.jsp', 'id='+id,
			        function(data){
					$('#day-book-view-container').html(data);
						$("#day-book-view-dialog").dialog('open');
			        }
		        );
			});
			$("#day-book-view-dialog").dialog({
				autoOpen: false,
				height: 550,
				width: 900,
				modal: true,
				buttons: {
					Close: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$('#day-book-view-container').html('');
				}
			});
		},
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		//end of function
		checkLength:function(len,number){
			$('.invoice-boxes-'+number).css({'height' : 'inherit'});
			if(len>22){
				$('#row-'+number).css({'height' : '30px'});
			}
			if(len>40){
				$('#row-'+number).css({'height' : '40px;'});
			}
			if(len>60){
				$('#row-'+number).css({'height' : '50px;'});
			}
			
		},
};
		
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
		dayBookSteps : [  '#day-book-basic-info-form' , '#day-book-allowances-form', '#day-book-amount-form','#day-book-product-form','#day-book-vehicle-details-form' ],
		dayBookUrl : [ 'dayBook.json' , 'dayBook.json', 'dayBook.json','dayBook.json' ,'dayBook.json'],
		dayBookStepCount : 0,
		journalCRSteps : [ '#journal-form'],
		journalsCRUrl : [ 'journal.json'],
		journalCRStepCount : 0,
		array : '',
		deliveryNoteArray : '',
		deliveryNoteOldFormValuesArray : '',
		dayBookArray: '',
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
			    $.post('changeTransaction.json','action=change-dn-transaction-request',
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
							'<div id="'+data[loop].id+'" align="'+data[loop].invoiceNo+'" class="ui-btn edit-icon" title="View Delivery Note"></div>';
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
				$.post('changeTransaction.json','action=change-sales-transaction-request',
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
								'<div id="'+data[loop].id+'" align="'+data[loop].invoiceNo+'" class="ui-btn edit-icon" title="View Sales Returns"></div>';
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
				$.post('changeTransaction.json','action=change-daybook-transaction-request',
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
				$.post('changeTransaction.json','action=change-journal-transaction-request',
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
		$('#transaction-details').click(function() {
			$('.transaction-page-container').load('my-sales/transactions/change-transactions/change_transactions_search.jsp');
		});
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
		    $.post('changeTransaction.json','action=validate-SE-DN-change-request&invoiceNumber='+invoiceNumber,
				    function(data){
		    var exists=data.result.data;
		    if(exists == 'y'){
		    	 showMessage({title:'Warning', msg:'Transaction CR for Delivery Note has not been Approved.'});
			       return;
		    }else{
		    	if (invoiceNumber.indexOf('COLLECTIONS') > -1) {
		    		$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_collect_amount_edit.jsp', 'id='+id,
					        function(data){
							$('.transaction-page-container').html(data);
					        });
		    		} else {
		    			$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_edit.jsp', 'id='+id,
		    			        function(data){
		    					$('.transaction-page-container').html(data);
		    			        });
		    		}
			
		        }
		    });
		    	/* }else{
		    		 showMessage({title:'Warning', msg:'Transaction CR for Delivery Note has been Expired.'});
				       return;
		    	 }
		    });*/
		  });
		 },
		 initSalesReturnSearchResultButtons : function() {
		 $('.edit-icon').click(function() {
				var id = $(this).attr('id');
				var invoiceNumber = $(this).attr('align');
				$.post('changeTransaction.json','action=get-sales-return-creation-time&invoiceNumber='+invoiceNumber+'&salesReturnId='+id,
					    function(data){
			    	 var createdOn=data.result.data;
			    	 if(createdOn == 'n'){
			    $.post('changeTransaction.json','action=validate-SE-SR-change-request&invoiceNumber='+invoiceNumber,
					    function(data){
			    var exists=data.result.data;
			    if(exists == 'y'){
			    	 showMessage({title:'Warning', msg:'Transaction CR for Sales Return has not been Approved.'});
				       return;
			    }else{
				$.post('my-sales/transactions/change-transactions/sales_returns_change_transaction_edit.jsp', 'id='+id,
			        function(data){
					$('.transaction-page-container').html(data);
			        });
			    }
			    });
			    	 }else{
			    		 showMessage({title:'Warning', msg:'Transaction CR for Sales Return has been Expired.'});
					       return;
			    	 }
			});
		 });
		 },
		 initDayBookResultButtons : function() {
			 $('.edit-icon').click(function() {
					var id = $(this).attr('id');
					$.post('changeTransaction.json','action=get-day-book-creation-time&dayBookId='+id,
						    function(data){
				    	 var createdOn=data.result.data;
				    	 if(createdOn == 'n'){
					$.post('changeTransaction.json','action=validate-SE-DB-change-request&dayBookId='+id,
						    function(data){
				    var exists=data.result.data;
				    if(exists == 'y'){
				    	 showMessage({title:'Warning', msg:'Transaction CR for Day Book has not been Approved.'});
					       return;
				    }else{
					$.post('my-sales/transactions/change-transactions/daybook_change_transaction_edit.jsp', 'id='+id,
				        function(data){
						$('.transaction-page-container').html(data);
				        });
				    }
				});
				    	 }else{
				    		 showMessage({title:'Warning', msg:'Transaction CR for Day Book has been Expired.'});
						       return;
				    	 }
			});
		});
		 },
		 initJournalResultButtons : function() {
			 $('.edit-icon').click(function() {
					var id = $(this).attr('id');
					var invoiceNumber = $(this).attr('align');
					$.post('changeTransaction.json','action=get-journal-creation-time&invoiceNumber='+invoiceNumber+'&journalId='+id,
						    function(data){
				    	 var createdOn=data.result.data;
				    	 if(createdOn == 'n'){
					$.post('changeTransaction.json','action=validate-SE-Journal-change-request&journalId='+id+'&invoiceNumber='+invoiceNumber,
						    function(data){
				    var exists=data.result.data;
				    if(exists == 'y'){
				    	 showMessage({title:'Warning', msg:'Transaction CR for Journal has not been Approved.'});
					       return;
				    }else{
					$.post('my-sales/transactions/change-transactions/journal_change_transaction_edit.jsp', 'id='+id,
				        function(data){
						$('.transaction-page-container').html(data);
				        });
				    }
				});
				    	 }else{
				    		 showMessage({title:'Warning', msg:'Transaction CR for Journal has been Expired.'});
						       return;
				    	 }
			});
			 });
		 },
		//Function to format date to DD/MM/YYYY
			formatDate:function(inputFormat){
				var str=inputFormat.split(/[" "]/);
				dt=new Date(str[0]);
				return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
			},
			//loading the grid of delivery note with existing details.
	        loadProductGridBasedOnBusinesName : function(){
		        var businessName = $('#businessName').val();
		        var deliveryNoteId= $('#deliveryNoteId').val();
		        var salesId= $('#salesId').val();
		        $('.green-results-list').html('');
					$.post('changeTransaction.json', 'action=get-existed-grid-data&businessName=' +businessName+'&deliveryNoteId='+deliveryNoteId+'&salesId='+salesId, function(obj) {
						var data = obj.result.data;
						if(data != undefined){
							if(data.length>0) {
								var count=1;
								for(var x=0;x<data.length;x=x+1) {
									var rowstr ='<div class="ui-content report-content" id = "delivery-note-grid">';
									rowstr=rowstr+'<div id="delivery-note-cr-row-'+count+'" class="report-body" style="width: 699px;height: auto; overflow: hidden;">';
									rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>'
									rowstr=rowstr+'<div id="row8" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
									rowstr=rowstr+'<div id="row6" class="report-body-column2 centered" style="height: inherit; width: 0px; display: none;">'+currencyHandler.convertNumberToStringPattern(data[x].availableQuantity)+'</div>';
									rowstr=rowstr+'<div id="row4" class="report-body-column2 centered" style="height: inherit; width: 100px;">'+data[x].productCost+'</div>';
									rowstr=rowstr+'<div id="row2" class="report-body-column2 centered" style="height: inherit; width: 100px;">'+'<input type = "text" id="productQty" class="productQuantity" size=3px value='+currencyHandler.convertNumberToStringPattern(data[x].productQty)+'></div>';
									rowstr=rowstr+'<div id="row3" class="report-body-column2 centered" style="height: inherit; width: 100px;">'+'<input type = "text" id="productBonus"  class="bonus" size=3px value='+currencyHandler.convertNumberToStringPattern(data[x].bonus)+'></div>';
									rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 100px;">'+'<textarea rows="2" cols="2" id="productBonusReason"  style="height: 41px; width: 100px; border: none; resize: none;"  class="bonusReason">'+data[x].bonusReason+'</textarea>'+'</div>';
									rowstr=rowstr+'<div id="row5" class="report-body-column2 centered totalCost" style="height: inherit; width: 90px;">'+data[x].totalCost+'</div>';
									rowstr=rowstr+'</div></div>';
									$('.green-results-list').append(rowstr);
									if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
											$('#delivery-note-cr-row-'+count).each(function(index) {
										        $(this).children().height(100);
										        //$(this).children('#row7').css('height',100);
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
								$( '.report-body-column2' ).css( "width", "90px" );
								$( '.returnQty' ).css( "width", "90px" );		
						        $('.gridDisplay').show();
							}else{
							$( '#search-results-list' ).css( "width", "830px" );
							$( '.report-header' ).css( "width", "830px" );
					    	$( '.report-body' ).css( "width", "830px" );
							$( '.report-header-column2' ).css( "width", "100px" );
							$( '.report-body-column2' ).css( "width", "100px" );
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
										$( '.bonusReason' ).css( "width", "80px" );
								   }else{
									   $( '#search-results-list' ).css( "width", "830px" );
									   $( '.report-header' ).css( "width", "830px" );
								    	$( '.report-body' ).css( "width", "830px" );
										$( '.report-header-column2' ).css( "width", "100px" );
										$( '.report-body-column2' ).css( "width", "100px" );
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
				changedDeliveryNoteBasicInfoFormValues : function(changedFormFieldsId,changedFormFieldsValue){
			         TransactionChangeRequestHandler.deliveryNoteArray+=changedFormFieldsId + ":" + changedFormFieldsValue + ",";
				},
				oldDeliveryNoteBasicInfoFormValues : function(changedFormFieldsId,changedFormFieldsValue){
			         TransactionChangeRequestHandler.deliveryNoteOldFormValuesArray+=changedFormFieldsValue+ ":";
				},
				initDeliveryNoteAddButtons : function() {
					TransactionChangeRequestHandler.deliverynoteStepCount = 0;
					$('#action-clear').click(function() {
						var oldValues = TransactionChangeRequestHandler.deliveryNoteOldFormValuesArray.split(':'); 
						$('#invoiceName').val(oldValues[0]);
						for(var i = 1; i < oldValues.length-2; i++) {
						}
				});		
					$('#button-next').click(function() {
						if ($(TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount]).validate() == false)
							return;
						$('.delivery-note-separator').css('width',"80px");
						var paramString = $(TransactionChangeRequestHandler.deliverynoteSteps[TransactionChangeRequestHandler.deliverynoteStepCount]).serializeArray();
						//paramString.push({name: 'presentPaymentChangedValue', value: presentPaymentChangedValue});
						var totalCost=$('#totalCost').val();
						var presentPayableVal = $("#presentPayable").val();
						var previousCreditVal = $("#previousCredit").val();
						var presentAdvanceVal =$("#presentAdvance").val();
						var totalPayableVal = $("#totalPayable").val();
						var presentPaymentVal =$("#presentPayment").val();
						var balance =$("#balance").val();
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
						    if(formData.value === presentPaymentVal){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(presentPaymentVal);
						    }
						    if(formData.value === balance){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(balance);
						    }
						}); 
						
						// To stop next button clickable
						var totalCost = $('#totalCost').val();
						//if(totalCost != '0.00') {
							//convert serialized array to url-encoded string
							var sendFormData = $.param(paramString);
								$.ajax({type : "POST",
										url : 'changeTransaction.json',
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
													$.post('changeTransaction.json', 'action=go-for-payments&businessName=' +businessName +'&invoiceName=' +invoiceName +'&invoiceNumber='+invoiceNumber+'&formChangedValues='+TransactionChangeRequestHandler.deliveryNoteArray , function(obj) {
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
													        		  listOfObjects +=','+productQty+'|'+batchNumber+'|'+bonusQty+'|'+bonusReason+'|'+productName+'|'+productCost;
													        	  }
													          }
													    });
													    	$.post('changeTransaction.json','action=save-product-data&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&formChangedValues='+TransactionChangeRequestHandler.deliveryNoteArray+'&invoiceNumber='+invoiceNumber,function(obj) {
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
							var paramString = 'action=save-payment-delivery-note';
							PageHandler.expanded=false;
							pageSelctionButton.click();
							$.post('changeTransaction.json', paramString, function(obj) {
								$(this).successMessage({
									container : '.transaction-page-container',
									data : obj.result.message
								});
							});
						} else {
							var paramString = 'action=save-deliverynote';
							PageHandler.expanded=false;
							pageSelctionButton.click();
							$.post('changeTransaction.json', paramString, function(obj) {
								$(this).successMessage({
									container : '.transaction-page-container',
									data : obj.result.message
								});
							});
						}
					});
					$('#action-cancel').click(function() {
										$('#error-message').html('You will loose unsaved data.. Clear form?');
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
													    			var url = "my-sales/transactions/change-transactions/sales_returns_change_transaction_edit.jsp";
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
				deliveryNoteLoad : function() {
					$('#presentPayment').change(function() {
						  var presentPayableVal = currencyHandler.convertStringPatternToFloat($("#presentPayable").val());
						  var previousCreditVal = currencyHandler.convertStringPatternToFloat($("#previousCredit").val());
						  var presentAdvanceVal = currencyHandler.convertStringPatternToFloat($("#presentAdvance").val());
						  var totalPayableVal = parseFloat(presentPayableVal) + parseFloat(previousCreditVal) - parseFloat(presentAdvanceVal);
						  $('#totalPayable').val(currencyHandler.convertFloatToStringPattern(totalPayableVal.toFixed(2)));
					});

					$('#presentPayment').change(function() {
						var presentPayment=currencyHandler.convertStringPatternToFloat($('#presentPayment').val());
						var formatPresentPayment=currencyHandler.convertFloatToStringPattern(presentPayment.toFixed(2));
						$('#presentPayment').val(formatPresentPayment);
								var totalPayableVal = currencyHandler.convertStringPatternToFloat($("#totalPayable").val());
								var presentPaymentVal = currencyHandler.convertStringPatternToFloat($("#presentPayment").val());
								$('#balance').val(currencyHandler.convertFloatToStringPattern((totalPayableVal - presentPaymentVal).toFixed(2)));
							});
					$('#paymentType').change(function(){
						if($('#paymentType').val().toLowerCase()=='cheque'){
							$('#bankname').show();
							$('#bankName').attr('class','mandatory');
							$('#branchname').show();
							$('#chequeno').show();
							$('#chequeNo').attr('class','mandatory');
						}else{
							$('#bankname').hide().find("input").val("");
							$('#bankName').removeAttr('class');
							$('#branchname').hide().find("input").val("");
							$('#chequeno').hide().find("input").val("");
							$('#chequeNo').removeAttr('class');
						}
					});
					$('#forPayments').change(function(){
				        if(this.checked){
				            $('.gridDisplay').hide();
				        }
				        else{
				        	 $('.green-results-list').html('');
				        	 TransactionChangeRequestHandler.loadProductGridBasedOnBusinesName();
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
						         /* if(parseInt(productQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity'});
								       return;
						          }
						          if(parseInt(bonusQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity'});
								       return;
						          }
						          if(parseInt(bonusQty) + parseInt(productQty) > parseInt(availableQty)) {
						        	  showMessage({title:'Warning', msg:'Please Enter valid Quantity'});
								       return;
						          }
						          if(bonusQty != "" && bonusReason == "") {
						        	  showMessage({title:'Warning', msg:'Please Enter Reason For Bonus.'});
								       return;
						          }	*/
						          var productName = $(this).find('#row1').html();
						          var productCost = $(this).find('#row4').html();
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
				loadSalesReturnProductGridBasedOnBusinesName : function(){
					var businessName = $('#businessName').val();
					 var salesReturnId= $('#salesReturnId').val();
					$('.green-results-list1').html('');
					$.post('changeTransaction.json', 'action=get-sales-return-grid-data&businessName=' +businessName+'&salesReturnId='+salesReturnId, function(obj) {
						var data = obj.result.data;
						if(data !== undefined){
							if(data.length>0) {
								$('.success-msg').hide();
								var count=1;
								for(var x=0;x<data.length;x=x+1) {
									var rowstr ='<div class="ui-content report-content" id="sales-row">';
									rowstr=rowstr+'<div id="sales-return-cr-row-'+count+'" class="report-body" style="width: 697px;height: auto; overflow: hidden;">';
									rowstr=rowstr+'<div id="row1" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].productName+'</div>';
									rowstr=rowstr+'<div id="row2" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
									rowstr=rowstr+'<div id="row6" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text"  id="damaged" class="damaged" size=4px value='+currencyHandler.convertNumberToStringPattern(data[x].damagedQty) +'>' + '</div>';
									rowstr=rowstr+'<div id="row7" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text"  id="resalable" class="resalable" size=4px value='+currencyHandler.convertNumberToStringPattern(data[x].resaleableQty) +'>'+'</div>';
									rowstr=rowstr+'<div id="row4" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+'<input type = "text"  id="returnQty" class="returnQty" style = "size: 4px; border: none;" value='+ currencyHandler.convertNumberToStringPattern(data[x].totalQty) +'>'+'</div>';
									rowstr=rowstr+'<div id="row3" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].cost+'</div>';
									rowstr=rowstr+'<div id="row5" class="report-body-column2 centered" style="height: inherit; width: 85px; word-wrap: break-word;">'+data[x].totalCost+'</div>';
									rowstr=rowstr+'</div></div>';
									$( '#returnQty' ).css( "width", "85px" );
									$('.green-results-list1').append(rowstr);
									if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
											$('#sales-return-cr-row-'+count).each(function(index) {
										        $(this).children().height(100);
										        //$(this).children('#row7').css('height',100);
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
								$( '.report-header-column2' ).css( "width", "100px" );
								$( '.report-body-column2' ).css( "width", "100px" );
								$( '.returnQty' ).css( "width", "100px" );		
						        $('.gridDisplay').show();
							}else{
							$( '#search-results-list' ).css( "width", "830px" );
							$( '.report-header' ).css( "width", "830px" );
					    	$( '.report-body' ).css( "width", "830px" );
							$( '.report-header-column2' ).css( "width", "100px" );
							$( '.report-body-column2' ).css( "width", "100px" );
							$( '.returnQty' ).css( "width", "100px" );		
					        $('.gridDisplay').show();
							}
							$('#ps-exp-col').click(function(){
								   if(PageHandler.expanded){
									   $( '#search-results-list' ).css( "width", "699px" );
									    $( '.report-header' ).css( "width", "699px" );
								    	$( '.report-body' ).css( "width", "699px" );
										$( '.report-header-column2' ).css( "width", "85px" );
										$( '.report-body-column2' ).css( "width", "85px" );
										$( '.returnQty' ).css( "width", "85px" );
								   }else{
									   $( '#search-results-list' ).css( "width", "830px" );
									   $( '.report-header' ).css( "width", "830px" );
								    	$( '.report-body' ).css( "width", "830px" );
										$( '.report-header-column2' ).css( "width", "100px" );
										$( '.report-body-column2' ).css( "width", "100px" );
										$( '.returnQty' ).css( "width", "100px" );
								   }
							   });
					}else {
						$('.green-results-list1').html('<div class="success-msg">No Products Available.</div>');
						$('.gridDisplay').show();
					}
				});
				},
				changedFormValues : function(changedFormFieldsId,changedFormFieldsValue){
			         TransactionChangeRequestHandler.array+=changedFormFieldsId + ":" + changedFormFieldsValue + ",";
				},
				initSalesReturnAddButtons : function() {
					TransactionChangeRequestHandler.salesreturnsStepCount = 0;
					$('#action-clear').click(function() {
						$(TransactionChangeRequestHandler.salesreturnsSteps[TransactionChangeRequestHandler.salesreturnsStepCount]).clearForm();
					});
					$('#button-save').click(function() {
						var thisButton = $(this);
						var paramString = 'action=save-sales-returns';
						PageHandler.expanded=false;
						pageSelctionButton.click();
						$.post('changeTransaction.json', paramString, function(obj) {
							$(this).successMessage({
								container : '.transaction-page-container',
								data : obj.result.message
							});
						});
				});
					$('#button-next').click(function(){
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
						var grandTotalCost =currencyHandler.convertStringPatternToFloat($('#grandTotalCost').val());
						$('div#sales-row').each(function(index, value) {
							var id = $(this).find('div').attr('id');
							var productName = $(this).find('#row1').html();
							var batchNumber = $(this).find('#row2').html();
							var cost = currencyHandler.convertStringPatternToFloat($(this).find('#row3').html());
							var totalCost = currencyHandler.convertStringPatternToFloat($(this).find('#row5').html());
							var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
							var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
							var returnQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
							if(damaged != "" || resalable != ""){
								if(index == 0) {
									listOfObjects +=damaged+'|'+resalable+'|'+cost+'|'+returnQty+'|'+totalCost+'|'+batchNumber+'|'+productName
								} else {
									listOfObjects +=','+damaged+'|'+resalable+'|'+cost+'|'+returnQty+'|'+totalCost+'|'+batchNumber+'|'+productName
								}
							}
						});
						var paramString = 'action=save-sales-return-products&listOfProductObjects=' + listOfObjects +'&businessName=' +businessName +'&invoiceName=' +invoiceName+'&grandTotalCost=' +grandTotalCost+'&formChangedFields='+TransactionChangeRequestHandler.array+'&invoiceNumber='+invoiceNumber+'&description='+crDescription;
						if(grandTotalCost != 0) {
							$.ajax({type : "POST",
								url : 'changeTransaction.json',
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
						$('#error-message').html('You will loose unsaved data.. Clear form?');
						$("#error-message").dialog({	
							resizable : false,
							height : 140,
							title : "<span class='ui-dlg-confirm'>Confirm</span>",
							modal : true,
							buttons : {
								'Ok' : function() {
									$('.task-page-container').html('');
									var container ='.transaction-page-container';
					    			var url = "my-sales/transactions/change-transactions/sales_returns_change_transaction_edit.jsp";
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
          salesReturnLoad:function() {
			$('#productName').change(function() {
				var productName=$("#productName").val()
				var businessName = $("#businessName").val()
				$.post('changeTransaction.json','action=get-product-cost&productNameVal='+productName+'&businessNameVal='+businessName, function(obj) {
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
				  $.each($('.report-body'), function(index, value) {
					  var productCost = currencyHandler.convertStringPatternToFloat($(this).find('#row3').html());
					  var damaged = currencyHandler.convertStringPatternToNumber($(this).find('.damaged').val());
						var resalable = currencyHandler.convertStringPatternToNumber($(this).find('.resalable').val());
						if(damaged == "") {
							damaged = 0;
						}
						if(resalable == "") {
							resalable = 0;
						}
						var returnQty = parseInt(damaged) + parseInt(resalable);
						$(this).find('.returnQty').val(currencyHandler.convertNumberToStringPattern(returnQty));
				      var productQty = currencyHandler.convertStringPatternToNumber($(this).find('.returnQty').val());
				      var totalCost = parseFloat(productCost) *  parseInt(productQty);
				      if(isNaN(totalCost)) {
				       	  totalCost = 0.00;
				       }
				      $(this).find('#row5').html(currencyHandler.convertFloatToStringPattern((totalCost.toFixed(2))));
				      sum += totalCost;
			          $('#grandTotalCost').val(currencyHandler.convertFloatToStringPattern(sum.toFixed(2)));
			          $(this).find('.damaged').val(currencyHandler.convertNumberToStringPattern(damaged));
					  $(this).find('.resalable').val(currencyHandler.convertNumberToStringPattern(resalable));
					  $('#grandTotalCost').change(function() {
							var grandTotalCost = $('#grandTotalCost').val();
							if(grandTotalCost == '0'){
								$('button-next').hide();
							} else {
								$('button-next').show();
							}
						});
				  });
				});
		},
		
		//day book step-by-step js function.
		changedDayBookFormChangedValues : function(changedFormFieldsId,changedFormFieldsValue){
	        TransactionChangeRequestHandler.dayBookArray+=changedFormFieldsId + ":" + changedFormFieldsValue + ",";
		},
		dayBookInitAddButtons : function() {
			TransactionChangeRequestHandler.dayBookStepCount = 0;

			$('#action-clear').click(function() {
								$(TransactionChangeRequestHandler.dayBookSteps[TransactionChangeRequestHandler.dayBookStepCount]).clearForm();});

			$('#button-next').click(
					function() {
						var success = true;
						var resultSuccess =true;
						if(TransactionChangeRequestHandler.dayBookStepCount == 4){
						if(TransactionChangeRequestHandler.validateReading()==false){
							return resultSuccess;
						}
						}
						var paramString = $(TransactionChangeRequestHandler.dayBookSteps[TransactionChangeRequestHandler.dayBookStepCount]).serializeArray();
						var execAllowances=$('#executiveAllowances').val();
						var driverAllowances=$('#driverAllowances').val();
						var vehicleFuelAllowances=$('#vehicleFuelExpenses').val();
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
						var salesExecutive = $('#salesExecutive').val();
						$.post('changeTransaction.json','action=check-day-book&salesExecutive='+salesExecutive,function(obj) {
							var data = obj.result.data;
							/*if(data == "false") {*/
								var sendFormData = $.param(paramString);
								alert(sendFormData);
								$.ajax({
											type : "POST",
											url : 'changeTransaction.json',
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
													var listOfObjects = '';
													$('div#day-book-grid').each(function(index, value) {
														 var id = $(this).find('div').attr('id');
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
											          $.post('changeTransaction.json','action=save-day-book-data&listOfProductObjects=' + listOfObjects+'&dayBookFormChangedValues='+TransactionChangeRequestHandler.dayBookArray,function(obj) {

											          });
													$('#button-prev').show();
													$('.page-buttons').css('margin-left', '150px');
													if (TransactionChangeRequestHandler.dayBookStepCount == 3) {
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
													if(TransactionChangeRequestHandler.dayBookStepCount == 4){
														$('.page-selection').animate( { width:"183px"}, 0,function(){
															$('.page-link-strip').show();
															$('.module-title').show();
														});
														$('.page-container').animate( { width:"702px"}, 0);
													}
													if(TransactionChangeRequestHandler.dayBookStepCount == 5){
														PageHandler.hidePageSelection();
														  $('.page-link-strip').hide();
															$('.module-title').hide();
															$('.page-selection').animate( { width:"55px"}, 0,function(){});
															$('.page-container').animate( { width:"835px"}, 0);
															var thisTheme = PageHandler.theme;
															$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
													}
												} else {
													$('#button-prev').hide();
													$('.page-buttons').css('margin-left', '200px');
												}
											},
										});
							/*} else {
								showMessage({title:'Warning', msg:'Your Day Book Have Been Closed For The Day.'});
				         	  	return;
							}*/
						});
						
					});
	$('#button-prev')
			.click(	function() {
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
							if (TransactionChangeRequestHandler.dayBookStepCount == 4) {
								$('.page-selection').animate( { width:"183px"}, 0,function(){
									$('.page-link-strip').show();
									$('.module-title').show();
								});
								$('.page-container').animate( { width:"702px"}, 0);
							}
							if (TransactionChangeRequestHandler.dayBookStepCount == 3) {
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
				var paramString = 'action=save-daybook';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				var isReturn = $('#isReturn').val();
				$.post('changeTransaction.json', paramString+'&isReturn='+isReturn, function(obj) {
					$(this).successMessage({
						container : '.transaction-page-container',
						data : obj.result.message
					});
				});
			});
			$('#action-cancel').click(function() {
			    $('#error-message').html('You will loose unsaved data.. Clear form?');   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/change-transactions/daybook_change_transaction_edit.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			    return false;
			});
			$('#executiveAllowances').blur(function() {
				var execAllowances=currencyHandler.convertStringPatternToFloat($('#executiveAllowances').val());
				var fomatExecAllowances=currencyHandler.convertFloatToStringPattern(execAllowances.toFixed(2));
				$('#executiveAllowances').val(fomatExecAllowances);
			});
			$('#driverAllowances').blur(function() {
				var driverAllowances=currencyHandler.convertStringPatternToFloat($('#driverAllowances').val());
				var fomatdriverAllowances=currencyHandler.convertFloatToStringPattern(driverAllowances.toFixed(2));
				$('#driverAllowances').val(fomatdriverAllowances);
			});
			$('#vehicleFuelExpenses').blur(function() {
				var vehicleFuelAllowances=currencyHandler.convertStringPatternToFloat($('#vehicleFuelExpenses').val());
				var fomatVehicleFuelAllowances=currencyHandler.convertFloatToStringPattern(vehicleFuelAllowances.toFixed(2));
				$('#vehicleFuelExpenses').val(fomatVehicleFuelAllowances);
			});
			$('#offloadingLoadingCharges').blur(function() {
				var offloadingLoadingCharges=currencyHandler.convertStringPatternToFloat($('#offloadingLoadingCharges').val());
				var fomatOffloadingLoadingCharges=currencyHandler.convertFloatToStringPattern(offloadingLoadingCharges.toFixed(2));
				$('#offloadingLoadingCharges').val(fomatOffloadingLoadingCharges);
			});
			$('#vehicleMaintenanceExpenses').blur(function() {
				var vehicleMaintenanceExpenses=currencyHandler.convertStringPatternToFloat($('#vehicleMaintenanceExpenses').val());
				var fomatVehicleMaintenanceExpenses=currencyHandler.convertFloatToStringPattern(vehicleMaintenanceExpenses.toFixed(2));
				$('#vehicleMaintenanceExpenses').val(fomatVehicleMaintenanceExpenses);
			});
			
			$('#miscellaneousExpenses').blur(function() {
				var miscellaneousExpenses=currencyHandler.convertStringPatternToFloat($('#miscellaneousExpenses').val());
				var fomatMiscellaneousExpenses=currencyHandler.convertFloatToStringPattern(miscellaneousExpenses.toFixed(2));
				$('#miscellaneousExpenses').val(fomatMiscellaneousExpenses);
			});
			
			$('#dealerPartyExpenses').blur(function() {
				var dealerPartyExpenses=currencyHandler.convertStringPatternToFloat($('#dealerPartyExpenses').val());
				var fomatDealerPartyExpenses=currencyHandler.convertFloatToStringPattern(dealerPartyExpenses.toFixed(2));
				$('#dealerPartyExpenses').val(fomatDealerPartyExpenses);
			});
			
			$('#municipalCityCouncil').blur(function() {
				var municipalCityCouncil=currencyHandler.convertStringPatternToFloat($('#municipalCityCouncil').val());
				var fomatMunicipalCityCouncil=currencyHandler.convertFloatToStringPattern(municipalCityCouncil.toFixed(2));
				$('#municipalCityCouncil').val(fomatMunicipalCityCouncil);
			});
			
			$('#amountToFactory').blur(function() {
				var amountToFactory=currencyHandler.convertStringPatternToFloat($('#amountToFactory').val());
				var fomatAmountToFactory=currencyHandler.convertFloatToStringPattern(amountToFactory.toFixed(2));
				$('#amountToFactory').val(fomatAmountToFactory);
			});
			
			$('#amountToBank').blur(function() {
				var amountToBank=currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var fomatAmountToBank=currencyHandler.convertFloatToStringPattern(amountToBank.toFixed(2));
				$('#amountToBank').val(fomatAmountToBank);
			});
		},
		dayBookLoad:function() {
			$('#isReturn').change(function() {
				if($('#isReturn').is(':checked')) {
					$('#isReturn').attr("value", true);
				} else {
					$('#isReturn').attr("value", false);
				}
			});
			$('.green-results-list').html('');
			var dayBookId=$('#dayBookId').val();
			var salesExecutive=$('#salesExecutive').val();
			$.post('changeTransaction.json', 'action=get-day-book-grid-data&dayBookId='+dayBookId+'&salesExecutive='+salesExecutive, function(obj) {
				var data = obj.result.data;
				if(data != undefined){
					if(data.length>0) {
						var count=1;
						for(var x=0;x<data.length;x=x+1) {
							var rowstr='<div class="ui-content report-content" id="day-book-grid">';
							rowstr=rowstr+'<div id="day-book-cr-row-'+count+'" class="report-body" style="width: 830px;height: auto; overflow: hidden;">';
							rowstr=rowstr+'<div id = "row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>';
							rowstr=rowstr+'<div id = "row7" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
							rowstr=rowstr+'<div id = "row2" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].openingStock)+'</div>';
							rowstr=rowstr+'<div id = "row6" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].returnQty)+'</div>';
							rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned productToCustomer" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].productsToCustomer)+'</div>';
							rowstr=rowstr+'<div id="row4" class="report-body-column2 centered productToFactories" style="height: inherit; width: 100px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;" class="productToFactory" id="productsToFactory" size=6px value='+currencyHandler.convertNumberToStringPattern(data[x].productsToFactory)+'>'+'</div>';
							rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].closingStock)+'</div>';
							rowstr=rowstr+'</div></div>';
							$('.green-results-list').append(rowstr);
							if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
									$('#day-book-cr-row-'+count).each(function(index) {
								        $(this).children().height(100);
								        //$(this).children('#row7').css('height',100);
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
							}
					} else {
					$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
				}
			}else {
				$('.green-results-list').html('<div class="success-msg">No Products Available.</div>');
				$('.gridDisplay').show();
			}
		});
			
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
			
			$('#closingBalance').focus(function() {
				$.post('changeTransaction.json','action=get-opening-balance', function(obj) {
					var openingBalance = obj.result.data;
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
					$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceVal.toFixed(2)));
				});
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
				         $(this).find('#row5').html(currencyHandler.convertNumberToStringPattern(closingStock));
				         $(this).find('.productToFactory').val(currencyHandler.convertNumberToStringPattern(productToFactory));
			    });
			});
		},
		validateReading : function(){
			var result = true;
				var startingReadingVal = $('#startReading').val();
				var endingReadingVal = $('#endingReading').val();
				var dlen =$('#driverName').val().length
				var vlen=$('#vehicleNo').val().length;
				if(/^[a-zA-Z\s]+$/.test($('#driverName').val())==false ||$('#driverName').val().length==0||$('#driverName').val().charAt(0)==" "||$('#driverName').val().charAt(dlen -1)==" "){
					$('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#driverName").focus(function(event){
						$('#driverNameValid').empty();
						 $('#driverName_pop').show();
					});
					$("#driverName").blur(function(event){
						 $('#driverName_pop').hide();
						 if(/^[a-zA-Z\s]+$/.test($('#driverName').val())==false ||$('#driverName').val().length==0||$('#driverName').val().charAt(0)==" "||$('#driverName').val().charAt(dlen-1)==" "){
							 $('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#driverName").focus(function(event){
								 $('#driverNameValid').empty();
								 $('#driverName_pop').show();
							 });
							
						 }else{
							 $('#driverNameValid').empty();
						 }
					});
					result=false;

				}
				if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen -1)==" "){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#vehicleNo").focus(function(event){
						$('#vehicleNOValid').empty();
						 $('#vehicleNo_pop').show();
					});
					$("#vehicleNo").blur(function(event){
						 $('#vehicleNo_pop').hide();
						 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen-1)==" "){
							 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#vehicleNo").focus(function(event){
								 $('#vehicleNoValid').empty();
								 $('#vehicleNo_pop').show();
							 });
							
						 }else{
							 $('#vehicleNOValid').empty();
						 }
					});
					result=false;
				}
				if($('#vehicleNo').val().length>20){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $('#vehicleNo_pop').hide();
					$("#vehicleNo").focus(function(event){
						 $('#vehicleNo_pop').hide();
						$('#vehicleNOValid').empty();
						 $('#vehicleNolen_pop').show();
					});
					$("#vehicleNo").blur(function(event){
						 $('#vehicleNolen_pop').hide();
						 if($('#vehicleNo').val().length>20){
							 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#vehicleNo").focus(function(event){
								 $('#vehicleNo_pop').hide();
								 $('#vehicleNoValid').empty();
								 $('#vehicleNolen_pop').show();
							 });
							
						 }else{
							 $('#vehicleNOValid').empty();
						 }
						 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen -1)==" "){
								$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#vehicleNo").focus(function(event){
									 $('#vehicleNolen_pop').hide();
									$('#vehicleNOValid').empty();
									 $('#vehicleNo_pop').show();
								});
								$("#vehicleNo").blur(function(event){
									 $('#vehicleNo_pop').hide();
									
									 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen-1)==" "){
										 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#vehicleNo").focus(function(event){
											 $('#vehicleNolen_pop').hide();
											 $('#vehicleNoValid').empty();
											 $('#vehicleNo_pop').show();
										 });
										
									 }else{
										 $('#vehicleNOValid').empty();
									 }
								});
								result=false;
							}
					});
					result=false;
				}
				var slen=$('#startReading').val().length;
				var elen=$('#startReading').val().length;
				if(/^[0-9,]+$/.test($('#startReading').val()) == false||$('#startReading').val().length==0||$('#startReading').val().charAt(0)==" "||$('#startReading').val().charAt(slen-1)==" "){
					$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#startReading").focus(function(event){
						$('#reading_pop').hide();
						$('#startreadingValid').empty();
						 $('#startreading_pop').show();
					});
					$("#startReading").blur(function(event){
						 $('#startreading_pop').hide();
						 if(/^[0-9,]+$/.test($('#startReading').val()) == false||$('#startReading').val().length==0||$('#startReading').val().charAt(0)==" "||$('#startReading').val().charAt(slen-1)==" "){
							 $('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#startReading").focus(function(event){
								 $('#startreadingValid').empty();
								 $('#startreading_pop').show();
							 });
							
						 }else{
							 $('#readingValid').empty();
						 }
					});
					result=false;
				}
				if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#readingValid").focus(function(event){
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#readingvalid_pop').hide();
						 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#reading_pop').hide();
								 $('#readingValid').empty();
								 $('#readingvalid_pop').show();
							 });
							
						 }else{
							 $('#readingValid').empty();
						 }
					});
					result=false;
				}else{
					 $('#readingValid').empty();
				}
				if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val())||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#readingvalid_pop').hide();	
						$('#readingValid').empty();
						 $('#reading_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#reading_pop').hide();
						 if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val())||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingvalid_pop').hide();
								 $('#readingValid').empty();
							 $('#reading_pop').show();
							 });
						 }
						 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "){
								$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#endingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#readingValid').empty();
									 $('#readingvalid_pop').show();
								});
								$("#endingReading").blur(function(event){
									 $('#readingvalid_pop').hide();
									 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "){
										 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#endingReading").focus(function(event){
											 $('#reading_pop').hide();
											 $('#readingValid').empty();
											 $('#readingvalid_pop').show();
										 });
										
									 }else{
										 $('#readingValid').empty();
									 }
								});
								result=false;
							}
					});
					result=false;
					
				}else{
					// $('#readingValid').empty();
				}
				return result;
		},
		getDayBookCustomerTotalPayable:function() {
			$.post('changeTransaction.json','action=get-customer-total-payable', function(obj) {
				var customerTotalPayable = obj.result.data.presentPayable;
				var customerTotalReceived = obj.result.data.presentPayment;
				var totalCredit = parseFloat(customerTotalPayable) - parseFloat(customerTotalReceived);
				$('#customerTotalPayable').val(currencyHandler.convertFloatToStringPattern(customerTotalPayable));
				$('#customerTotalReceived').val(currencyHandler.convertFloatToStringPattern(customerTotalReceived));
				$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(totalCredit.toFixed(2)));
			});	
		},
		//Journal step-by-step
		changedEditJournalBasicInfoFormValues : function(changedFormFieldsId,changedFormFieldsValue){
	         TransactionChangeRequestHandler.array+=changedFormFieldsId + ":" + changedFormFieldsValue + ",";
		},
		initAddJournalCRButtons : function(){
			TransactionChangeRequestHandler.journalCRStepCount = 0;
			$('#action-clear').click(function() {
				$('#edit-journal-form').clearForm();
			});
			$('#button-save').click(function() {
				if(TransactionChangeRequestHandler.validateJournals()==false){
					return true;
				}
				var thisButton = $(this);
				var paramString = $('#edit-journal-form').serializeArray();
				var amount=$('#amount').val();
				var formatAmount=currencyHandler.convertStringPatternToFloat(amount);
				//access form searialized value and modified
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
				$.post('changeTransaction.json', sendFormData+'&form-changed-value-journal-edit='+TransactionChangeRequestHandler.array+'&businessName='+businessName+'&invoiceNo='+invoiceNo+'&journalType='+journalType+'&amount='+formatAmount+'&invoiceName='+invoiceName+'&description='+description+'&crDescription='+crDescription, function(obj) {
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
				$('#error-message').html('You will loose unsaved data.. Clear form?');
				$("#error-message").dialog({	
					resizable : false,
					height : 140,
					title : "<span class='ui-dlg-confirm'>Confirm</span>",
					modal : true,
					buttons : {
						'Ok' : function() {
							$('.task-page-container').html('');
			    			var container ='.transaction-page-container';
			    			var url = "my-sales/transactions/change-transactions/journal_change_transaction_edit.jsp";
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
	validateJournals : function(){
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
	}
	
  };


