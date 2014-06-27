var DayBookHandler = {
		theme: "",
		flag : true,
		check : true,
		expanded: true,
		initPageLinks : function() {
			$('#add-day-book').pageLink({
				container : '.day-book-page-container',
				url : 'my-sales/day-book/day_book_add.jsp'
			});
			$('#temp-day-book').pageLink({
				container : '.day-book-page-container',
				url : 'my-sales/day-book/temp_day_book.jsp'
			});
		},
		getAmountToBankData : function(){
				var dayBookType = "Deposit Amount";

				titleVal = dayBookType+" Details";
				$.post('dayBook.json', 'action=get-allowances-details&dayBookType='+dayBookType, function(obj) {
					var data = obj.result.data;
					var d = $("#amount-to-bank-dialog");
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
						$('#amount-to-bank-container').html('');
						$('#amount-to-bank-container').append(allowancesTrack);
			            $('#amount-to-bank-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
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
						$('#amount-to-bank-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
					}
				});
			return titleVal;
			
				//DayBookHandler.trackAllowanceData(dayBookType);
		},
		validateMeterreading : function(meterReading,id){
			var mainId = $('#'+id).parent().attr('id');
			var res = $('#'+id).parent().attr('class');
			$('.col4').change(function(){
				
				
			});
			
			var startingReading ;
			var endingReading;
				var Sno = $('#'+mainId).children('.row1').children('.col1').val();
			if(Sno == 1){
				startingReading = $('#startReading').val();
				endingReading = $('#endingReading').val();
			}else{
				valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
				var dayBookType = "Vehicle Fuel Expenses";
				$.ajax({type: "POST",
					url:'dayBook.json', 
					data: 'action=get-starting-reading&dayBookType='+dayBookType,
					async : false,
					success: function(data){
						 existedStartReading = data.result.data;
						 return existedStartReading;
					},
				});
				 startingReading = existedStartReading;
			}
			var check;
			if(meterReading < startingReading || meterReading >endingReading  ){
				check = false;
			}else{
				check = true;
			}
			return check;
		},
		dayBookSteps : [  '#day-book-basic-info-form','#day-book-vehicle-details-form' , '#day-book-allowances-form', '#day-book-amount-form','#day-book-product-form' ],
		dayBookUrl : [ 'dayBook.json' , 'dayBook.json', 'dayBook.json','dayBook.json' ,'dayBook.json'],
		dayBookStepCount : 0,
		initAddButtons : function() {
			DayBookHandler.dayBookStepCount = 0;


			$('#button-next').click(function() {
				if( DayBookHandler.checkVehicleDetails() == false){
					showMessage({title:'Error', msg:'Please Configure Vehicle Details In Cash Day Book'});
					return false;
				}
					
					var success = true;
					var resultSuccess =true;
					if(DayBookHandler.dayBookStepCount == 0){
						if($('#reportingManager').is(":visible")){
							if(ValidateDayBookHandler.validateReportingManager() == false){
					    		return true;
					    	}
						}
					}
					if(DayBookHandler.dayBookStepCount == 2){
						if(ValidateDayBookHandler.validateAllowances()== false){
							return resultSuccess;
						}
						}
					if(DayBookHandler.dayBookStepCount == 3){
						if(ValidateDayBookHandler.validateAmounts() == false){
							return true;
						}
					}
					
					if(DayBookHandler.dayBookStepCount == 1){
					if(ValidateDayBookHandler.validateReading()==false){
						return resultSuccess;
					}
					}
					if(DayBookHandler.dayBookStepCount ==4){
						$('div#day-book-grid').each(function(index, value) {
							var productName = $(this).find('#row1').html();
				        	  var openingStock = currencyHandler.convertStringPatternToNumber($(this).find('#row2').html());
				        	  var productToCustomer = currencyHandler.convertStringPatternToNumber($(this).find('#row3').html());
				        	  var productToFactory = currencyHandler.convertStringPatternToNumber($(this).find('.productToFactory').val());
				        	  var closingStock =parseInt(openingStock) - (parseInt(productToCustomer) + parseInt(productToFactory)) ;
				        	  if(productToFactory < 0){
				        		   showMessage({title:'Error', msg:'Products to factory should have a positive value'});
									flag = false;
									event.preventDefault();
									return false;
								}
				        	  if(closingStock < 0){
									$(this).find('#row5').html(currencyHandler.convertNumberToStringPattern(openingStock));
									showMessage({title:'Error', msg:'Products to factory should not be greater than closing stock'});
									flag = false;
									event.preventDefault();
									return false;
								}else{
									flag = true;
								}
							});
							if(flag == false){
								return false;
							}
							 
					}
					
					var paramString = $(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).serializeArray();
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
					var startReading=$('#startReading').val();
					var endingReading=$('#endingReading').val();
					$.post('dayBook.json','action=get-opening-balance', function(obj) {
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
					
					var closingBalance=$('#closingBalance').val();
					var allotStockOpeningBalance=$('#allotStockOpeningBalance').val();
					
					//access form serialized value and modified
					$.each(paramString, function(i, formData) {
						 if(formData.name ==='reasonAmountToBank'||formData.name === 'reasonMiscellaneousExpenses'||formData.name === 'remarks'||formData.name == 'dayBookRemarks'
								||formData.name =='allowancesRemarks'||formData.name == 'amountsRemarks'||formData.name =='productsRemarks'){ 
							 
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
					    if(formData.value === vehicleMeterReading){ 
					    	formData.value = currencyHandler.convertStringPatternToFloat(vehicleMeterReading);
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
					    if(formData.value === allotStockOpeningBalance){ 
					    	formData.value = currencyHandler.convertStringPatternToFloat(allotStockOpeningBalance);
					    }
					});    
					
					// TODO: Needs to enable later.
					/*var salesExecutive = $('#salesExecutive').val();
					$.post('dayBook.json','action=check-day-book&salesExecutive='+salesExecutive,function(obj) {
						var data = obj.result.data;
						if(data == "false") {*/
							$.ajax({
										type : "POST",
										url : 'dayBook.json',
										data : paramString,
										success : function(data) {
											$('#error-message').html('');
											$('#error-message').hide();
											$(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).hide();
											$(DayBookHandler.dayBookSteps[++DayBookHandler.dayBookStepCount]).show();
											
											if (DayBookHandler.dayBookStepCount > 0) {
												var isDaily = $('#isReturn').val();
												var listOfObjects = '';
												var productsRemarks =  currencyHandler.formatStringValue($('#productsRemarks').val());
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
												        		  listOfObjects +='?'+productName+'|'+openingStock+'|'+productToCustomer+'|'+productToFactory+'|'+closingStock+'|'+returnQty+'|'+batchNumber;
												        	  }
											    });
												$.ajax({type: "POST",
													url:'dayBook.json', 
													data: 'action=save-day-book-data&productsRemarks='+productsRemarks+'&listOfProductObjects='+listOfObjects,
													async : false,
													success :function(obj) {
														if (DayBookHandler.dayBookStepCount == 4) {
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
																			DayBookHandler.expanded=false;
																	   }else{
																		   $( '#search-results-list' ).css( "width", "830px" );
																		   $( '.report-header' ).css( "width", "830px" );
																	    	$( '.report-body' ).css( "width", "830px" );
																			$( '.report-header-column2' ).css( "width", "100px" );
																			$( '.report-body-column2' ).css( "width", "100px" );
																			$( '.productToCustomer' ).css( "width", "100px" );
																			$( '.productToFatcory' ).css( "width", "1000px" );
																			DayBookHandler.expanded=true;
																	   }
																   });
														}
													},
										          });
												$('#button-prev').show();
												$('.page-buttons').css('margin-left', '225px');
												if(DayBookHandler.dayBookStepCount == 3){
													$('.page-selection').animate( { width:"183px"}, 0,function(){
														$('.page-link-strip').show();
														$('.module-title').show();
													});
													$('.page-container').animate( { width:"702px"}, 0);
												}
												if(DayBookHandler.dayBookStepCount == 5){
													PageHandler.hidePageSelection();
													  $('.page-link-strip').hide();
														$('.module-title').hide();
														$('.page-selection').animate( { width:"55px"}, 0,function(){});
														$('.page-container').animate( { width:"835px"}, 0);
														var thisTheme = PageHandler.theme;
														$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
												}
												if (DayBookHandler.dayBookStepCount == DayBookHandler.dayBookSteps.length) {
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
													$.post('my-sales/day-book/day_book_preview.jsp',
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
														DayBookHandler.expanded=false;
														$('#ps-exp-col').click(function() {
														if(DayBookHandler.dayBookStepCount == DayBookHandler.dayBookSteps.length){
															if(!PageHandler.expanded) {
														    	$('#day-book-preview-container').css({'height' : '350px'});
																$('#day-book-preview-container').html(data);
																$('.table-field').css({"width":"800px"});
																$('.main-table').css({"width":"400px"});
																$('.inner-table').css({"width":"400px"});
																$('.display-boxes-colored').css({"width":"140px"});
																$('.display-boxes').css({"width":"255px"});
																$('#day-book-preview-container').show();
																DayBookHandler.expanded=false;
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
																DayBookHandler.expanded=true;
														    }
														}
														});
													});
												}
												
												
											} else {
												$('#button-prev').hide();
												$('.page-buttons').css('margin-left', '200px');
											}
										},
										/*error : function(data) {
											$('#error-message').html(data.responseText);
											$('#error-message').dialog();
											$('#error-message').show();
										}*/
									});
						/*} else {
							showMessage({title:'Warning', msg:'Your Day Book Have Been Closed For The Day.'});
			         	  	return;
						}*/
					//});
						
						
					});
				$('#button-prev').click(function() {
					//DayBookHandler.getReportingManagerName();
						$('#action-clear').show();
						if (DayBookHandler.dayBookStepCount == DayBookHandler.dayBookSteps.length) {
							$('#button-next').show();
							$('#button-save').hide();
							$('#button-update').hide();
							$('#day-book-preview-container').html('');
							$('#day-book-preview-container').hide();
							$('.page-buttons').css('margin-left', '150px');
						}
						$(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).hide();
						$(DayBookHandler.dayBookSteps[--DayBookHandler.dayBookStepCount]).show();
						if (DayBookHandler.dayBookStepCount > 0) {
							$('#button-prev').show();
							$('.page-buttons').css('margin-left', '150px');
							if (DayBookHandler.dayBookStepCount == 3) {
								$('.page-selection').animate( { width:"183px"}, 0,function(){
									$('.page-link-strip').show();
									$('.module-title').show();
								});
								$('.page-container').animate( { width:"702px"}, 0);
							}
							if (DayBookHandler.dayBookStepCount == 4) {
								PageHandler.hidePageSelection();
								  $('.page-link-strip').hide();
									$('.module-title').hide();
									$('.page-selection').animate( { width:"55px"}, 0,function(){});
									$('.page-container').animate( { width:"835px"}, 0);
									var thisTheme = PageHandler.theme;
									$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
							}
							if (DayBookHandler.dayBookStepCount == 2) {
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
							$('.page-buttons').css('margin-left', '215px');
						}
					});
			$('#button-save').click(function() {
				var thisButton = $(this);
				var paramString = 'action=save-daybook';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				var isReturn = $('#isReturn').val();
				$('.page-content').ajaxSavingLoader();
				$.post('dayBook.json', paramString+'&isReturn='+isReturn, function(obj) {
					 $.loadAnimation.end();
					$(this).successMessage({
						container : '.day-book-page-container',
						data : obj.result.message
					});
				});
			});
			/*$('#add-day-book').click(function() {
				if( DayBookHandler.checkVehicleDetails() == false){
					showMessage({title:'Error', msg:'Please Configure Vehicle Details In Cash Day Book'});
					return false;
				}
				$('.day-book-page-container').load('my-sales/day-book/day_book_add.jsp');
			});*/
			
			$('#action-clear').click(function() {
				var salesExecutiveValue = $('#salesExecutive').val();
				var allotStockOpeningBalanceValue = $('#allotStockOpeningBalance').val();
				var startDateValue = $('#startDate').val();
				var vehicleNoValue = $('#vehicleNo').val();
				var driverNameValue = $('#driverName').val();
				var startReadingValue = $('#startReading').val();
				var executiveAllowancesValue = $('#executiveAllowances').val();
				var driverAllowancesValue = $('#driverAllowances').val();
				var vehicleFuelExpensesValue = $('#vehicleFuelExpenses').val();
				var municipalCityCouncilValue = $('#municipalCityCouncil').val();
				var vehicleMaintenanceExpensesValue = $('#vehicleMaintenanceExpenses').val();
				var offloadingLoadingChargesValue = $('#offloadingLoadingCharges').val();
				var miscellaneousExpensesValue = $('#miscellaneousExpenses').val();
				var dealerPartyExpensesValue = $('#dealerPartyExpenses').val();
				var totalAllowancesValue = $('#totalAllowances').val();
				var customerTotalPayableValue = $('#customerTotalPayable').val();
				var customerTotalReceivedValue = $('#customerTotalReceived').val();
				var customerTotalCreditValue = $('#customerTotalCredit').val();
				var amountToBank = $('#amountToBank').val();
				//calculating the closingbalance.
				var firstValue = parseFloat(currencyHandler.convertStringPatternToFloat(allotStockOpeningBalanceValue)) + parseFloat(currencyHandler.convertStringPatternToFloat(customerTotalReceivedValue));
				var secondValue = parseFloat(currencyHandler.convertStringPatternToFloat(totalAllowancesValue)) + parseFloat(currencyHandler.convertStringPatternToFloat(amountToBank));
				var closingBalanceValue = firstValue - secondValue;
				if(isNaN(closingBalanceValue)) {
					closingBalanceValue = 0.00;
				}
				
				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {

								$(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).clearForm();
								
								$('#salesExecutive').val(salesExecutiveValue);
								$('#allotStockOpeningBalance').val(allotStockOpeningBalanceValue);
								$('#startDate').val(startDateValue);
								$('#vehicleNo').val(vehicleNoValue);
								$('#driverName').val(driverNameValue);
								$('#startReading').val(startReadingValue);
								$('#executiveAllowances').val(executiveAllowancesValue);
								$('#driverAllowances').val(driverAllowancesValue);
								$('#vehicleFuelExpenses').val(vehicleFuelExpensesValue);
								$('#municipalCityCouncil').val(municipalCityCouncilValue);
								$('#vehicleMaintenanceExpenses').val(vehicleMaintenanceExpensesValue);
								$('#offloadingLoadingCharges').val(offloadingLoadingChargesValue);
								$('#miscellaneousExpenses').val(miscellaneousExpensesValue);
								$('#dealerPartyExpenses').val(dealerPartyExpensesValue);
								$('#totalAllowances').val(totalAllowancesValue);
								$('#customerTotalPayable').val(customerTotalPayableValue);
								$('#customerTotalReceived').val(customerTotalReceivedValue);
								$('#customerTotalCredit').val(customerTotalCreditValue);
								$('#amountToBank').val(amountToBank);
								$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceValue.toFixed(2)));
				    			$(this).dialog('close');
							},
							Cancel: function() {
								$(this).dialog('close');
							}
						}
					});
				    return false;
				
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
							$.post('dayBook.json', 'action=remove-day-book-session-data', function(obj) {
							$('.task-page-container').html('');
			    			var container ='.day-book-page-container';
			    			var url = "my-sales/day-book/day_book_add.jsp";
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
				var openingBalance =$('#allotStockOpeningBalance').val();
				var totalAllowances = currencyHandler.convertStringPatternToFloat($('#totalAllowances').val());
				var customerTotalReceived = currencyHandler.convertStringPatternToFloat($('#customerTotalReceived').val());
				var amountToBank = currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val())
				var amounttoFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val())
				var sum = amountToBank + closingBalnce + amounttoFactory;
				var validResult = (currencyHandler.convertStringPatternToFloat(openingBalance)+customerTotalReceived)-(amountToBank+totalAllowances);
				if($('#isReturn').is(':checked')) {
					if(parseFloat(validResult - amounttoFactory) < 0 ){
						showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
					result = false;
				}
					if(amountToFactory < 0){
						showMessage({title:'Error', msg:'Amount To Factory should have a positive value'});
						result = false;
					}
				}
			});
		},
		getReportingManagerName : function(){
			$.post('dayBook.json', 'action=lookup-allotment-type', function(obj) {
				var isDaily = obj.result.data;
				if(isDaily == "true"){
					$('#manager').show();
					$('#reportingManager').attr('class','mandatory');
				}else {
					$('#amountToFactory').hide();
					$('.amountToFactory').hide();
					$('#isReturn').change(function(){
						if($('#isReturn').is(':checked')) {
							$('#manager').show();
							$('#reportingManager').attr('class','mandatory');
							$('#amountToFactory').show();
							$('.amountToFactory').show();
						}else{
							$('#manager').hide();
							$('#reportingManager').removeAttr('class','mandatory');
							$('#amountToFactory').hide();
							$('.amountToFactory').hide();
						}
					});
				}
				
			});
		},
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		getStartDate : function(){
			$.ajax({
				type : "POST",
				url : 'dayBook.json',
				data : 'action=get-start-date',
				async : false,
				success : function(obj) {
			var createdDate = obj.result.data;
			$('#startDate').val(createdDate);
			},
			});
		},
		
		calculateClosingBalance : function() {
			var openingBalance = currencyHandler.convertStringPatternToFloat($('#allotStockOpeningBalance').val());
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
				if(amountToFactory < 0){
					showMessage({title:'Error', msg:'Amount To Factory should have a positive value'});
					result = false;
				}else if(value1 - (amountToFactory+ parseFloat(amountToBank+totalAllowances)) < 0){
					showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
				}else{
				$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceVal.toFixed(2)));
				}
			} else {
				$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(closingBalanceVal.toFixed(2)));
			}
		},
		
		load:function() {
			var titleVal ;
			$.post('dayBook.json','action=get-allot-stock-opening-balance', function(obj) {
				var allotStockOpeningBalVal = currencyHandler.convertStringPatternToFloat(obj.result.data);
				var fomatAllotStockOpeningBal=currencyHandler.convertFloatToStringPattern(allotStockOpeningBalVal.toFixed(2));
				$('#allotStockOpeningBalance').val(fomatAllotStockOpeningBal);
			});
			$.post('dayBook.json', 'action=lookup-allotment-type', function(obj) {
				var isDaily = obj.result.data;
				if(isDaily == 'true') {
					$('#presentDate').hide();
					$('#isReturn').attr('checked', 'checked');
					$('#isReturn').attr("value", true);
					$('#isReturn').attr("disabled", true);
				} else {
					$('#presentDate').show();
					$('#isReturn').removeAttr("disabled");
					$('#isReturn').attr("value", false);
				}
			});
			
			//Populating the allowances.
			DayBookHandler.populateAllowances();
			
			$('#isReturn').change(function() {
				if($('#isReturn').is(':checked')) {
					$('#isReturn').attr("value", true);
				} else {
					$('#isReturn').attr("value", false);
				}
			});
			
			$.post('dayBook.json', 'action=get-grid-data', function(obj) {
				$('.green-results-list').html('');
				var data = obj.result.data;
				if(data != undefined){
					if(data.length>0) {
						var count=1;
						for(var x=0;x<data.length;x=x+1) {
							var rowstr='<div class="ui-content report-content" id="day-book-grid">';
							rowstr=rowstr+'<div id="day-book-row-'+count+'" class="report-body" style="width: 830px;height: auto; overflow: hidden;">';
							rowstr=rowstr+'<div id = "row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>';
							rowstr=rowstr+'<div id = "row7" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
							rowstr=rowstr+'<div id = "row2" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;text-align:right !important;">'+currencyHandler.convertNumberToStringPattern(data[x].openingStock)+'</div>';
							rowstr=rowstr+'<div id = "row6" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;text-align:right !important;">'+currencyHandler.convertNumberToStringPattern(data[x].returnQty)+'</div>';
							rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned productToCustomer" style="height: inherit; width: 100px; word-wrap: break-word;text-align:right !important;">'+currencyHandler.convertNumberToStringPattern(data[x].productsToCustomer)+'</div>';
							rowstr=rowstr+'<div id="row4" class="report-body-column2 centered productToFactories" style="height: inherit; width: 100px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;text-align:right !important;" class="productToFactory" size=4px readonly="readonly">'+'</div>';
							rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;text-align:right !important;">'+currencyHandler.convertNumberToStringPattern(data[x].closingStock)+'</div>';
							rowstr=rowstr+'</div></div>';
							$('.green-results-list').append(rowstr);
							if((data[x].productName.length > 80) || (data[x].batchNumber.length > 80)){
								$('#day-book-row-'+count).each(function(index) {
							        $(this).children().height(100);
							        //$(this).children('#row2').css('padding-top', 50);
							    });    
							   }else if((data[x].productName.length > 50) || (data[x].batchNumber.length > 50)){
										$('#day-book-row-'+count).each(function(index) {
									        $(this).children().height(68);
									    });    
								}else if((data[x].productName.length > 30) || (data[x].batchNumber.length > 30)){
									$('#day-book-row-'+count).each(function(index) {
								        $(this).children().height(55);
								    });   
							   }else if((data[x].productName.length > 15) || (data[x].batchNumber.length > 15)){
								   $('#day-book-row-'+count).each(function(index) {
								        $(this).children().height(50);
								    });   
					           }
					           else{
					        	   $('#day-book-row-'+count).each(function(index) {
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
			
			$('#amountToFactory').change(function() {
				DayBookHandler.calculateClosingBalance();
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
		        	  if(productToFactory < 0){
		        		  showMessage({title:'Error', msg:'Products to factory should have a positive value'});
		        		  return false;
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
			
			$('#executiveAllowancesLink').click(function() {
				var dayBookType = "Executive Allowances";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#driverAllowancesLink').click(function(){
				var dayBookType = "Driver Allowances";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#vehicleFuelExpensesLink').click(function(){
				var dayBookType = "Vehicle Fuel Expenses";
				titleVal = dayBookType+" Details";
				var d = $("#executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				$.post('dayBook.json','action=get-allowances-details&dayBookType='+dayBookType,function(obj){
					var result = obj.result.data;
					$("#executive-allowances-view-dialog").dialog('open');
					if(result.length != 0){
						var allowancesTrack="";
						allowancesTrack +='<div class="report-header" style="width: 730px; height: 30px;">'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 200px;">'+ Msg.DAY_BOOK_METER_READING+'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
						'</div>';
			            $('#executive-allowances-view-container').append(allowancesTrack);
			            $('#executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 730px; overflow-y:initial;"></div>'); 
						for(var loop=1; loop<=result.length; loop=loop+1) {
							 var vehicleExpenses = currencyHandler.convertStringPatternToFloat(result[loop-1].vehicleFuelExpenses);
							var allowancesTrackRows ='<div class="ui-content report-content">'+
							'<div id='+ loop+' class="report-body" style="width: 730px; height: 30px; overflow: hidden; line-height:20px;">'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row1"  style="height: inherit; width: 100px; word-wrap: break-word;">' + '<input class = "col1 read-only" type = "text" style="width:100px;" readonly="readonly" value='+ loop+ '></div>' +
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern(vehicleExpenses.toFixed(2))+'></div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ result[loop-1].createdOn +'</div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="height: inherit; width: 200px; word-wrap: break-word;">'+'<input class = "col4 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+ result[loop-1].vehicleMeterReading+'></div>'+
							'<a id="'+result[loop-1].id+'" onclick="TempDayBookHandler.allowanceEdit(\'' +result[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px;"></a>'+
							'<a id="'+result[loop-1].id+'" onclick="TempDayBookHandler.allowanceUpdate(\'' +result[loop-1].id+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px; display:none;"></a>'+
							'</div>'+
							'</div>';
							$('.grid-content').append(allowancesTrackRows);
						}
					}
					 else {
						 $('#executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
						}
				});
			
			});
			$('#offloadingChargesLink').click(function(){
				var dayBookType = "Offloading Charges";
				titleVal = dayBookType+" Details";
				var d = $("#executive-allowances-view-dialog");
				d.dialog('option', 'title', titleVal);
				d.dialog('open');
				$.post('dayBook.json','action=get-allowances-details&dayBookType='+dayBookType, function(obj){
					var result = obj.result.data;
					$("#executive-allowances-view-dialog").dialog('open');
					if(result.length != 0){
						var allowancesTrack="";
						allowancesTrack +='<div class="report-header" style="width: 830px; height: 30px;">'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 100px;">' +Msg.SERIAL_NUMBER_LABEL+ '</div>' +
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DAY_BOOK_AMOUNT_LABEL+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 150px;">' +Msg.DATE+ '</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 300px;">'+ Msg.DAY_BOOK_BUSINESS_NAME+'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
						'</div>';
			            $('#executive-allowances-view-container').append(allowancesTrack);
			            $('#executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
						for(var loop=1; loop<=result.length; loop=loop+1) {
							var offloadingCharges = currencyHandler.convertStringPatternToFloat(result[loop-1].offLoadingCharges );
							var allowancesTrackRows ='<div class="ui-content report-content">'+
							'<div id='+loop+' class="report-body row1" style="width: 830px; height: 30px; overflow: hidden; line-height:20px;">'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 100px; word-wrap: break-word;">' +  loop  + '</div>' +
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style="height: inherit; width: 150px; word-wrap: break-word;">'+'<input class = "col2 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern( offloadingCharges.toFixed(2))+'></div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 150px; word-wrap: break-word;">'+ result[loop-1].createdOn +'</div>'+
							'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="height: inherit; width: 300px; word-wrap: break-word;">'+'<input class = "col4 read-only" type = "text" style="width:100px;" readonly="readonly" value ='+result[loop-1].businessName+'></div>'+
							'<a id="'+result[loop-1].id+'" onclick="TempDayBookHandler.allowanceEdit(\'' +result[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px;"></a>'+
							'<a id="'+result[loop-1].id+'" onclick="TempDayBookHandler.allowanceUpdate(\'' +result[loop-1].id+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:1px; float:right; margin-right:35px; display:none;"></a>'+
							'</div>'+
							'</div>';
							$('.grid-content').append(allowancesTrackRows);
						}
					}
					 else {
						 $('#executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
						}
				});
			});
			$('#vehicleMaintenanceExpensesLink').click(function(){
				var dayBookType = "Vehicle Maintenance Expenses";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#municipalCityCouncilLink').click(function(){
				var dayBookType = "Municipal City Council";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#miscellaneousExpensesLink').click(function(){
				var dayBookType = "Miscellaneous Expenses";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#dealerPartyExpensesLink').click(function(){
				var dayBookType = "Dealer Party Expenses";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$('#amountToBankLink').click(function(){
				var dayBookType = "Deposit Amount";
				DayBookHandler.trackAllowanceData(dayBookType);
			});
			$("#executive-allowances-view-dialog").dialog({
				autoOpen: false,
				height: 455,
				width: 850,
				title :titleVal,
				modal: true,
				buttons: {
					Close: function() {
						//Populating the allowances.
						DayBookHandler.populateAllowances();
						
						$(this).dialog('close');
					}
				},
				close: function() {
					$('#executive-allowances-view-container').html('');
				}
			});
			$("#amount-to-bank-dialog").dialog({
				autoOpen: false,
				height: 455,
				width: 850,
				title :titleVal,
				modal: true,
				buttons: {
					Close: function() {
						//Populating the allowances.
						DayBookHandler.populateAllowances();
						
						$(this).dialog('close');
					}
				},
				close: function() {
					$('#executive-allowances-view-container').html('');
				}
			});
		},
		
		trackAllowanceData : function(dayBookType){
			titleVal = dayBookType+" Details";
			$.post('dayBook.json', 'action=get-allowances-details&dayBookType='+dayBookType, function(obj) {
				var data = obj.result.data;
				var d = $("#executive-allowances-view-dialog");
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
		            $('#executive-allowances-view-container').append(allowancesTrack);
		            $('#executive-allowances-view-container').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
					for(var loop=1; loop<=data.length; loop=loop+1) {
						if( data[loop-1].remarks == "null" || data[loop-1].remarks == "0" ){
							 data[loop-1].remarks ="";
						}
						var allowancesAmount =currencyHandler.convertStringPatternToFloat( data[loop-1].executiveAllowances);
						var allowancesTrackRows ='<div class="ui-content report-content">'+
						'<div id='+loop+' class="report-body" style="width: 830px; height: 30px; overflow: hidden; line-height:20px;">'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style=" width: 100px; word-wrap:line-height:20px;break-word;">' +  loop  + '</div>' +
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row2" style=" width: 150px; line-height:20px;word-wrap: break-word;">'+'<input class = "col2 read-only" id =" amount " type = "text" style="width:100px;margin-top:2px;" readonly="readonly" value ='+currencyHandler.convertFloatToStringPattern(allowancesAmount.toFixed(2))+'></div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="width: 150px; line-height:20px;word-wrap: break-word;">'+ data[loop-1].createdOn +'</div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight row4" style="width: 301px;border-right: 1px solid #9BCCF2; word-wrap: break-word;" id="remarksVal">'+'<textarea rows="1" cols="2"  class = "col4 read-only" style="min-height: 15px;margin-top:-6px; overflow-y:auto ;width: 285px; resize: none;" readonly="readonly">'+data[loop-1].remarks+'</textarea></div>'+
						'<a id="'+data[loop-1].id+'" onclick="TempDayBookHandler.allowanceEdit(\'' +data[loop-1].id+'\');" class="edit-icon row5" title="Edit Vehicle Expenses" style="margin-top:10px; float:right; margin-right:35px;"></a>'+
						'<a id="'+data[loop-1].id+'" onclick="TempDayBookHandler.allowanceUpdate(\'' +data[loop-1].id+'\', \'' +dayBookType+'\');" class="btn-update1 row6" title="Update Vehicle Expenses" style="margin-top:9px; float:right; margin-right:35px; display:none;"></a>'+
						'</div>'+
						'</div>';
						$('.grid-content').append(allowancesTrackRows);
					}
				} else {
					$('#executive-allowances-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Records Found</div></div></div>');
				}
			});
		return titleVal;
		},
		populateAllowances : function() {
			$.post('dayBook.json', 'action=get-allowances', function(obj) {
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
							DayBookHandler.calculateClosingBalance();
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
		
		/*validateAllowances :function(){
			var result = true;
			end = $('#executiveAllowances').val().length;
			if(/^[0-9.,]+$/.test($('#executiveAllowances').val())==false &&$('#executiveAllowances').val().length>0||$('#executiveAllowances').val().charAt(0)==" "||$('#executiveAllowances').val().charAt(end -1)==" "){
				$('#executiveallValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#executiveAllowances").focus(function(event){
					$('#executiveall_pop').hide();
					$('#executiveallValid').empty();
					 $('#executiveall_pop').show();
				});
			$("#executiveAllowances").blur(function(event){
				 $('#executiveall_pop').hide();
				 if(/^[0-9.,]+$/.test($('#executiveAllowances').val())==false||$('#executiveAllowances').val().charAt(0)==" "||$('#executiveAllowances').val().charAt(end -1)==" "){
					 $('#executiveallValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#executiveAllowances").focus(function(event){
						 $('#executiveall_pop').hide();
						 $('#executiveallValid').empty();
						 $('#executiveall_pop').show();
					 });
					 
				 }
			});
			result=false;
			}
			var startReading = $('#startReading').val();
			var endReading = $('#endingReading').val();
			return result;
			
		},
		validateReportingManager : function(){
			var result = true;
			if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
				$('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#reportingManager").focus(function(event){
					$('#reportingManagerValid').empty();
					 $('#reportingManagerLen_pop').hide();
					 $('#reportingManager_pop').show();
				});
				$("#reportingManager").blur(function(event){
					 $('#reportingManager_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
						 $('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#reportingManager").focus(function(event){
							 $('#reportingManagerValid').empty();
							 $('#reportingManagerLen_pop').hide();
							 $('#reportingManager_pop').show();
						 });
						
					 }else{
						 $('#reportingManagerValid').empty();
					 }
				});
				result = false;
			}
			if($('#reportingManager').val().length > 35){

				$('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#reportingManager").focus(function(event){
					$('#reportingManagerValid').empty();
					 $('#reportingManager_pop').hide();
					 $('#reportingManagerLen_pop').show();
				});
				$("#reportingManager").blur(function(event){
					 $('#reportingManagerLen_pop').hide();
					 if($('#reportingManager').val().length > 35){
						 $('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#reportingManager").focus(function(event){
							 $('#reportingManagerValid').empty();
							 $('#reportingManager_pop').hide();
							 $('#reportingManagerLen_pop').show();
						 });
						
					 }else{
						 $('#reportingManagerValid').empty();
					 }
				});
				result = false;
			
			}
			return result;
		},
		validateReading : function(){
			var dayBookType = 'Vehicle Fuel Expenses';
			$.ajax({type: "POST",
				url:'dayBook.json', 
				data: 'action=get-starting-reading&dayBookType='+dayBookType,
				async : false,
				success: function(data){
					 existedStartReading = data.result.data;
					 return existedStartReading;
				}
				
			});
			var result = true;
				var startingReadingVal = $('#startReading').val();
				var endingReadingVal = $('#endingReading').val();
				var dlen =$('#driverName').val().length
				var elen=$('#endingReading').val().length;
				if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt($('#endingReading').val().length-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#readingValid").focus(function(event){
						$('#readingLen_pop').hide();
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#readingvalid_pop').hide();
							if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt($('#endingReading').val().length-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingLen_pop').hide();
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
				if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val()) ||$('#endingReading').val().length==0||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#readingLen_pop').hide();
						$('#readingvalid_pop').hide();	
						$('#readingValid').empty();
						 $('#reading_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#reading_pop').hide();
						 if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val())||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingLen_pop').hide();
								 $('#readingvalid_pop').hide();
								 $('#readingValid').empty();
							 $('#reading_pop').show();
							 });
						 }else{
							 $('#readingValid').empty();
						 }
						 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
								$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#endingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#readingLen_pop').hide();
									$('#readingValid').empty();
									 $('#readingvalid_pop').show();
								});
								$("#endingReading").blur(function(event){
									 $('#readingvalid_pop').hide();
									 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
										 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#endingReading").focus(function(event){
											 $('#readingLen_pop').hide();
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
					});
					result=false;
					
				}
				if($('#endingReading').val().length > 6){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').hide();
						 $('#readingLen_pop').show();
					});
					$("#endingReading").blur(function(){
						 $('#readingLen_pop').hide();
						if($('#endingReading').val().length > 6){
							$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							$("#endingReading").focus(function(event){
								$('#reading_pop').hide();
								$('#readingValid').empty();
								 $('#readingvalid_pop').hide();
								 $('#readingLen_pop').show();
							});
						}
					});
					result =false;
				}
				else{
					 $('#readingValid').empty();
				}
				return result;
		},
		validateAmounts : function(){
			var result = true;
			var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val());
			var openingBalance =$('#allotStockOpeningBalance').val();
				var totalAllowances = currencyHandler.convertStringPatternToFloat($('#totalAllowances').val());
				var customerTotalReceived = currencyHandler.convertStringPatternToFloat($('#customerTotalReceived').val());
				var amountToBank = currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val())
				var amounttoFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val())
				var sum = amountToBank + closingBalnce + amounttoFactory;
				var validResult = (currencyHandler.convertStringPatternToFloat(openingBalance)+customerTotalReceived)-(amountToBank+totalAllowances);
				if($('#isReturn').is(':checked')) {
			if(parseFloat(validResult - amounttoFactory) < 0){
				showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
					
					result = false;
				}
				}
			if(/^[0-9-,.]+$/.test($('#amountToFactory').val()) == false && $('#amountToFactory').val().length >0){
				$('#amountToFactoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#amountToFactory").focus(function(event){
					$('#amountToFactory_pop').show();	
					$('#amountToFactoryValid').empty();
				});
				$("#amountToFactory").blur(function(event){
					$('#amountToFactory_pop').hide();	
					if(/^[0-9-,.]+$/.test($('#amountToFactory').val()) == false&&$('#amountToFactory').val().length >0){
						 $('#amountToFactoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#amountToFactory").focus(function(event){
								$('#amountToFactory_pop').show();	
								$('#amountToFactoryValid').empty();
							});
						
					 }else{
						 $('#amountToFactoryValid').empty();
					 }
				});
				result = false;
			}
			
			return result;
			
		},*/
		getCustomerTotalPayable:function() {
			$.post('dayBook.json','action=get-customer-total-payable', function(obj) {
				var customerTotalPayable = obj.result.data.presentPayable;
				var customerTotalReceived = obj.result.data.presentPayment;
				var totalCredit = parseFloat(customerTotalPayable) - parseFloat(customerTotalReceived);
				if(totalCredit < 0.0){
					var advanceBalance=Math.abs(totalCredit).toFixed(2);
					$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(advanceBalance)+' '+'('+Msg.DayBook_ADVANCE_AMOUNT+')');
				}else{
					$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(totalCredit.toFixed(2)));
				}
				$('#customerTotalPayable').val(currencyHandler.convertFloatToStringPattern(customerTotalPayable));
				$('#customerTotalReceived').val(currencyHandler.convertFloatToStringPattern(customerTotalReceived));
			});	
		},
		checkVehicleDetails : function(){
			$.ajax({type: "POST",
				url:'dayBook.json', 
				data: 'action=check-vehicle-details-availability',
				async : false,
				success: function(data){
					 result = data.result.data;
					 if(result =="false"){
							flag = false;
						}else {
							flag = true;
						}
				},
			});
			return flag;
		},
		
	};
var TempDayBookHandler = {
		prevVal :0,
		flag :true,
		check :true,
	load: function() {
		var dayBookType = $('#dayBookType').val();
		if(dayBookType == '-1'){
			$('.test').show();
		//	$("#amount").hide();
			$('.test').css('margin-left','200px');
			$('.test').css('margin-top','250px');
			$('#search-results-container2').hide();
		}
		$('#dayBookType').change(function() {
			$('#search-results-container2').hide();
			$('#driverNameValid').empty();
			$('#vehicleNOValid').empty();
			$('#dayBookTypeValid').empty();	
			$('#amountToBankValid').empty();
			$('#startreadingValid').empty();
			var dayBookType = $('#dayBookType').val();
			 if(dayBookType == "Deposit Amount") {
				 $.ajax({type: "POST",
						url:'dayBook.json', 
						data: 'action=check-available-deposit-amount',
						async : false,
						success: function(obj){
						amount = obj.result.data;
					amount = obj.result.data;
					$('#bankAmount').blur(function(){
						var bankAmount = $('#bankAmount').val();
						if( currencyHandler.convertStringPatternToFloat(bankAmount) > parseFloat(amount)){
							check = false;
							if(amount != 0.0){
								showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
								return check;
							}else{
								showMessage({title:'Error', msg:'No Cash Available'});
								return check;
							}
						}else{
							check = true;
						}
					});
						}
				});
				 $('#search-results-list').html('');
				 TempDayBookHandler.checkVehicleDetailsExistance();
				 $('#button-save').show();
					$('#button-update').hide();
				TempDayBookHandler.getExistedAmounts();
				 $('#search-results-list').html('');
				 $('#search-results-container2').show();
					$('.test').show();
					$('.test').css('margin-left','200px');
					$('.test').css('margin-top','5px');
				$('#amountToBank').show();
				$('#meterReading').hide();
				$('#meterreading').removeAttr('class','mandatory');
				$('#bankAmount').attr('class','mandatory');
				$('#remarks').show();
				$('#Remarks').attr('class','mandatory');
				$('#vehicleNo').hide();
				$('#driverName').hide();
				$('#allowanceType').hide();
				$('#startReading').hide();
				$('#allowancesAmount').hide();
				$('#businessName').hide();
			} else if(dayBookType == "Vehicle Details") {
				$.post('dayBook.json', 'action=check-vehicle-details-availability', function(obj) {
					var result = obj.result.data;
					if(result =="false"){
						flag = true;
					}else{
						showMessage({title:'Error', msg:'Vehicle Details Already Configured'});
						flag = false;
					}
				});
				$('#meterReading').hide();
				$('#search-results-list').html('');
				$('#button-save').show();
				$('#button-update').hide();
				TempDayBookHandler.getExistedVehiicleDetails();
				$('#search-results-container2').show();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','-10px');
				$('#amountToBank').hide();
				$('#bankAmount').removeAttr('class','mandatory');
				$('#remarks').hide();
				$('#Remarks').removeAttr('class','mandatory');
				$('#vehicleNo').show();
				$('#vehicleNumber').attr('class','mandatory');
				$('#driverName').show();
				$('#drivername').attr('class','mandatory');
				$('#allowanceType').hide();
				$('#startReading').show();
				$('#startingReading').attr('class','mandatory');
				$('#allowancesAmount').hide();
				$('#businessName').hide();
			} else if(dayBookType == "Allowances") {
				$('#search-results-list').html('');
				 TempDayBookHandler.checkVehicleDetailsExistance();
				$('#button-save').show();
				$('#button-update').hide();
				$('#search-results-container2').hide();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','250px');
				$('#allowancesAmount').hide();
				$('#amount').hide();
				$('#remarks').hide();
				$('#Remarks').removeAttr('class','mandatory');
				$('#vehicleNo').hide();
				$('#driverName').hide();
				$('#allowanceType').show();
				$('#allowancesType').attr('class','mandatory');
				$('#startReading').hide();
				$('#amountToBank').hide();
				$('#bankAmount').removeAttr('class','mandatory');
				$('#startingReading').removeAttr('class','mandatory');
				$('#drivername').removeAttr('class','mandatory');
				$('#vehicleNumber').removeAttr('class','mandatory');
				$('#businessName').hide();
			} else {
				 TempDayBookHandler.checkVehicleDetailsExistance();
				$('#button-save').show();
				$('#button-update').hide();
				$('#search-results-container2').show();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','250px');
				$('#vehicleNo').hide();
				$('#driverName').hide();
				$('#remarks').hide();
				$('#Remarks').removeAttr('class','mandatory');
				$('#allowanceType').hide();
				$('#amountToBank').hide();
				$('#bankAmount').removeAttr('class','mandatory');
				('#startingReading').removeAttr('class','mandatory');
				$('#drivername').removeAttr('class','mandatory');
				$('#vehicleNumber').removeAttr('class','mandatory');
				$('#allowanceAmount').removeAttr('class','mandatory');
				$('#startReading').hide();
				$('#allowancesAmount').hide();
				$('#businessName').hide();
			}
		});
		$('#allowanceAmount').change(function(){
			var allowancesAmount =currencyHandler.convertStringPatternToFloat($('#allowanceAmount').val());
			$("#allowanceAmount").val(currencyHandler.convertFloatToStringPattern(allowancesAmount.toFixed(2)));
		});
		$("#bankAmount").blur(function(){
			var amountToBank=currencyHandler.convertStringPatternToFloat($('#bankAmount').val());
			var bankAmount=currencyHandler.convertFloatToStringPattern(amountToBank.toFixed(2));
			$('#bankAmount').val(bankAmount);
		});
		$('#allowanceType').change(function() {
			$('#businessname').removeAttr('readonly','readonly');
			 $.ajax({type: "POST",
					url:'dayBook.json', 
					data: 'action=check-available-deposit-amount',
					async : false,
					success: function(obj){
				amount = obj.result.data;
				$('#allowanceAmount').blur(function(){
					var bankAmount = $('#allowanceAmount').val();
					if( currencyHandler.convertStringPatternToFloat(bankAmount) > parseFloat(amount)){
						check = false;
						if(amount != 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return check;
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
							return check;
						}
					}else{
						check = true;
						return check;
					}
				});
					}
			});
			$('#allowanceAmount').val("");
			$('#Remarks').val("");
			$("#remarksValid").empty();
			$('#allowancesValid').empty();
			$('#meterReadingValid').empty();
			$('#businessNameValid').empty();
			$('#dayBookTypeValid').empty();
			var allowancesType = $('#allowancesType').val();
			if(allowancesType == '-1'){
			$('.test').show();
			$('.test').css('margin-left','200px');
			$('.test').css('margin-top','250px');
			$('#search-results-container2').hide();
			}
			if(allowancesType == "Offloading Charges") {
				$('#search-results-container2').hide();
				$('#search-results-list').html('');
				$('#button-save').show();
				$('#button-update').hide();
				TempDayBookHandler.getOffloadingCharges();
				$('#search-results-container2').show();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','-10px');
				$('#allowancesAmount').show();
				$('#businessName').show();
				$('#businessname').attr('class','mandatory');
				$('#businessname').val("");
				$('#allowanceAmount').attr('class','mandatory');
				$('#meterReading').hide();
				$('#meterreading').removeAttr('class','mandatory');
				$('#remarks').hide();
			} else if(allowancesType == "Vehicle Fuel Expenses") {
				$.post('dayBook.json','action=get-starting-reading&dayBookType='+dayBookType,function(obj){
					 existedStartReading = obj.result.data;
				});
				$('#search-results-list').html('');
				$('#button-save').show();
				$('#button-update').hide();
				TempDayBookHandler.getExistedVehicleFuelExpenses();
				$('#search-results-container2').show();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','-10px');
				$('#meterReading').show();
				$('#meterreading').attr('class','mandatory');
				$('#allowancesAmount').show();
				$('#allowanceAmount').attr('class','mandatory');
				$('#remarks').hide();
				$('#businessName').hide();
			} else {
				$('#search-results-container2').hide();
				$('#search-results-list').html('');
				$('#button-save').show();
				$('#button-update').hide();
				$('#search-results-container2').show();
				$('.test').show();
				$('.test').css('margin-left','200px');
				$('.test').css('margin-top','-10px');
				TempDayBookHandler.getExisedExpensesData();
				$('#allowancesAmount').show();
				$('#allowanceAmount').attr('class','mandatory');
				$('#businessName').hide();
				$('#meterReading').hide();
				$('#meterreading').removeAttr('class','mandatory');
				$('#remarks').show();
			}
		});
		
		$('.btn-save').click(function() {
			$('.page-content').ajaxSavingLoader();
			var dayBookType = $('#dayBookType').val();
			var allowancesType = $('#allowancesType').val();
			if(ValidateTempDayBookHandler.validateDayBookType() == false){
				 $.loadAnimation.end();
				return true;
			}else if(dayBookType == "Deposit Amount") {
				//TempDayBookHandler.checkAmountAvailability();
				if(flag == false){
					 $.loadAnimation.end();
					showMessage({title:'Error', msg:'Please Configure Vehicle Details'});
					return false;
				}
				if(ValidateTempDayBookHandler.validateAmounts()==false){
					 $.loadAnimation.end();
					return true;
				}
				if(check == false){
					if(amount != 0.0){
						 $.loadAnimation.end();
						showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
						return false;
					}else{
						 $.loadAnimation.end();
						showMessage({title:'Error', msg:'No Cash Available'});
						return false;
					}
				}
			}else if(dayBookType == "Vehicle Details") {
				if(flag == false){
					 $.loadAnimation.end();
					showMessage({title:'Error', msg:'Vehicle Details Already Configured'});
					return false;
				}else if(ValidateTempDayBookHandler.validateVehicleDetails()==false){
					 $.loadAnimation.end();
					return true;
				}
			}else if(dayBookType == "Allowances"){
				//TempDayBookHandler.checkAmountAvailability();
				if(flag == false){
					 $.loadAnimation.end();
					showMessage({title:'Error', msg:'Please Configure Vehicle Details'});
					return false;
				}
				if(ValidateTempDayBookHandler.validateAllowances() == false){
					 $.loadAnimation.end();
					return false;
				}
					if(allowancesType != '-1'&& allowancesType =="Vehicle Fuel Expenses"){
						if(ValidateTempDayBookHandler.validateAmount() == false){
							 $.loadAnimation.end();
							if(ValidateTempDayBookHandler.validateMeterReading(existedStartReading) == false){
								 $.loadAnimation.end();
								return true;
							}
						}else{
							if(ValidateTempDayBookHandler.validateMeterReading(existedStartReading) == false){
								 $.loadAnimation.end();
								return true;
							}
						}
					if(check == false){
						if(amount != 0.0){
							 $.loadAnimation.end();
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return false;
						}else{
							 $.loadAnimation.end();
							showMessage({title:'Error', msg:'No Cash Available'});
							return false;
						}
					}
					
					var dayBookType ="Vehicle Details";
				}
				if(allowancesType != '-1'&& allowancesType =="Offloading Charges"){
					if(ValidateTempDayBookHandler.validateAmount() == false){
						if(ValidateTempDayBookHandler.validateBusinessName() == false){
							 $.loadAnimation.end();
							return true;
						}
					}else{
						if(ValidateTempDayBookHandler.validateBusinessName() == false){
							 $.loadAnimation.end();
							return true;
						}
					}
					if(TempDayBookHandler.checkOffloadingChargesAvailability() == false){
						 $.loadAnimation.end();
						$('#tempDayBookForm').clear();
						showMessage({title:'Error', msg:'Please Add DeliveryNote'});
						return true;
					}
					if(check == false){
						 $.loadAnimation.end();
						if(amount != 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return false;
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
							return false;
						}
					}
				}else{
					if(ValidateTempDayBookHandler.validateAmount() == false){
						 $.loadAnimation.end();
						return true;
					}
					if(check == false){
						 $.loadAnimation.end();
						if(amount != 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return false;
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
							return false;
						}
					}
					
				}
			}
			var paramString = $('#tempDayBookForm').serializeArray();
			var amountToBank = $('#bankAmount').val();
			var allowancesAmount= $('#allowanceAmount').val();
			$.each(paramString, function(i, formData) {
				if(formData.value === amountToBank){ 
			    	formData.value = currencyHandler.convertStringPatternToFloat(amountToBank);
			    }
				if(formData.value === allowancesAmount ){
					formData.value = currencyHandler.convertStringPatternToFloat(allowancesAmount);

				}
			   /* if(formData.value==0){
			    	formData.value=null;
			    }*/
			}); 
			$.post('dayBook.json', paramString, function(obj) {
				 $.loadAnimation.end();
				$(this).successMessage({
					container : '.day-book-page-container',
					data : obj.result.message
				});
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
						    			var container ='.day-book-page-container';	
						    			var url = "my-sales/day-book/temp_day_book.jsp";
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
			$('#action-clear').click(function() {
				
			$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$('#tempDayBookForm').clear();
						$(this).dialog('close');
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		    return false;
		});
		$('#businessname').click(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			TempDayBookHandler.suggestBusinessName(thisInput);
		});
		$('#businessname').keyup(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			TempDayBookHandler.suggestBusinessName(thisInput);
		});
		$('#businessname').focusout(function() {
			$('#business-name-suggestions').animate({
				display : 'none'
			}, 1000, function() {
				$('#business-name-suggestions').hide();
			});
		});
		
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (this.readOnly) return
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			};
	},
	checkAmountAvailability : function(id,valueOne){
		var mainId = $('#'+id).parent().attr('id');
		var check;
		var currentValue ;
		prevVal = valueOne;
		var amountToFactory = $('#amountToFactory').val();
		$('#'+mainId).children('.row2').children('.col2').change(function(){
			 currentValue = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row2').children('.col2').val());
		
		$.ajax({type: "POST",
			url:'dayBook.json', 
			data: 'action=check-available-deposit-amount',
			async : false,
			success: function(data){
				amount = data.result.data;
					//var allowanceAmount = $('#'+mainId).children('.row2').children('.col2').val();
				if($('#closingBalance').val() == 0){
					if( currentValue > Number(prevVal)){
						if(amount > 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return check;
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
							return check;
						}
					}
				}else{
					if(currentValue > Number(amount)+Number(prevVal)-Number(amountToFactory)){
						var resVal = Number(amount)+Number(prevVal)-Number(amountToFactory);
						if(resVal > 0.0){
							showMessage({title:'Error', msg:'Only '+resVal+' of Cash Available'});
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
						}
					}
				}
		 }
			});
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
			endingReading =$('#'+nextMainId).children('.row4').children('.col4').val();
			if(endingReading == undefined){
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
	checkOffloadingChargesAvailability : function(){
		var businessName = $('#businessname').val();
		var res;
		$.ajax({type: "POST",
			url:'dayBook.json', 
			data: 'action=is-offloading-charge-available&businessName='+businessName,
			async : false,
			success: function(data){
				 result = data.result.data;
				if(result == "false"){
					res = false;
				}else{
					res = true;
				}
			}
		});
		return res;
	},
	checkVehicleDetailsExistance : function(){
		var res = true;
		 $.post('dayBook.json', 'action=check-vehicle-details-availability', function(obj) {
			var result = obj.result.data;
			if(result =="false"){
				showMessage({title:'Error', msg:'Please Configure Vehicle Details'});
				res = false;
				flag = false;
			}else{
				res = true;
				flag = true;
			}
		});
		 return res;
	},
	getExistedVehicleFuelExpenses : function(){
	$.post('dayBook.json','action=get-existed-vehicle-fuel-expenses',function(obj){
		var data = obj.result.data;
		if(data != undefined){
			var alternate = false;

			for(var loop=0;loop<data.length;loop=loop+1) {
				if(alternate) {
					var rowstr = '<div class="green-result-row alternate">';
				} else {
					rowstr = '<div class="green-result-row">';
				}
				var allowanceAmount = currencyHandler.convertStringPatternToFloat(data[loop].allowancesAmount);
				alternate = !alternate;
				rowstr = rowstr + '<div class="green-result-col-1">'+
				'<div class="result-body">' +
		
				'<span class="property">Amount: '+' </span><span class="property-value">' +currencyHandler.convertFloatToStringPattern(allowanceAmount.toFixed(2)) + '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-2">'+
				'<div class="result-body">' +
				'<span class="property">Meter Reading: '+' </span><span class="property-value">' + data[loop].vehicleMeterReading + '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-action">' + 
				'<div id="'+data[loop].id+'" allowancesAmount="'+data[loop].allowancesAmount+'" class="edit-vehicle ui-btn edit-icon" meterReading="'+data[loop].vehicleMeterReading+'" title="Edit Vehicle Details"></div>' + 
				'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Vehicle Details"></div>';
		
				rowstr = rowstr + '<div id="'+data[loop].id+'"   class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
				'</div>' +
				'</div>';
				if(loop == 0){
					$('#search-results-list').html('');
					$('#search-results-list').append(rowstr);
				}else{
					$('#search-results-list').append(rowstr);
				}
	};
	TempDayBookHandler.initVehicleFuelExpensesButtons ();
	$('#search-results-list').jScrollPane({showArrows:true});
		}else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
		}
	});
},
getOffloadingCharges : function(){
	var dayBookType = $('#allowancesType').val();
	$.post('dayBook.json','action=get-allowances-details&dayBookType='+dayBookType,function(obj){
		var data = obj.result.data;
		 if ( typeof data != 'undefined') {
			var alternate = false;
			for(var loop=0;loop<data.length;loop=loop+1) {
				if(alternate) {
					var rowstr = '<div class="green-result-row alternate">';
				} else {
					rowstr = '<div class="green-result-row">';
				}
				var offloadingCharges = currencyHandler.convertStringPatternToFloat(data[loop].offLoadingCharges);
				alternate = !alternate;
				rowstr = rowstr + '<div class="green-result-col-1">'+
				'<div class="result-body">' +
		
				'<span class="property">Amount: '+' </span><span class="property-value">' +currencyHandler.convertFloatToStringPattern(offloadingCharges.toFixed(2)) +  '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-2">'+
				'<div class="result-body">' +
				'<span class="property">Business Name: '+' </span><span class="property-value">' + data[loop].businessName + '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-action">' + 
				'<div id="'+data[loop].id+'" offloadingCharges="'+data[loop].offLoadingCharges+'" class="edit-vehicle ui-btn edit-icon" businessName="'+data[loop].businessName+'" title="Edit Vehicle Details"></div>' + 
				'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Vehicle Details"></div>';
		
				rowstr = rowstr + '<div id="'+data[loop].id+'"   class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
				'</div>' +
				'</div>';
				if(loop == 0){
					$('#search-results-list').html('');
					$('#search-results-list').append(rowstr);
				}else{
					$('#search-results-list').append(rowstr);
				}
	};
	TempDayBookHandler.initOffloadingChrgesButtons ();
	$('#search-results-list').jScrollPane({showArrows:true});
		}else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
		}
	});
},
initOffloadingChrgesButtons : function(){
	var idVal;
	$('.edit-icon').click(function(){
		idVal = $(this).attr('id');
		$('#search-results-list').html('');
		$('#button-save').hide();
		$('#button-update').show();
		var offloadingCharges = $(this).attr("offloadingCharges");
		var businessName = $(this).attr('businessName');
		$('#businessname').attr('readonly','readonly');
		$('#businessname').val(businessName);
		$('#allowanceAmount').val(offloadingCharges);
	});
	$('#button-update').click(function(){
		$('.page-content').ajaxSavingLoader();
		if(ValidateTempDayBookHandler.validateAmount() == false){
			if(ValidateTempDayBookHandler.validateBusinessName() == false){
				$.loadAnimation.end();
				return true;
			}
		}else{
			if(ValidateTempDayBookHandler.validateBusinessName() == false){
				$.loadAnimation.end();
				return true;
			}
		}
		$.ajax({type: "POST",
			url:'dayBook.json', 
			data: 'action=check-available-deposit-amount',
			async : false,
			success: function(obj){
			amount = obj.result.data;
				var allowanceAmount = $('#allowanceAmount').val();
				if( currencyHandler.convertStringPatternToFloat(allowanceAmount) > parseFloat(amount)){
					$.loadAnimation.end();
					check = false;
					if(amount != 0.0){
						showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
						return check;
					}else{
						showMessage({title:'Error', msg:'No Cash Available'});
						return check;
					}
				}else{
					check = true;
				}
			},
		});
		if(check == false){
			$.loadAnimation.end();
			if(amount > 0.0){
				showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
				return false;
			}else{
				showMessage({title:'Error', msg:'No Cash Available'});
				return false;
			}
		}else{
			 var allowanceAmount =currencyHandler.convertStringPatternToFloat($('#allowanceAmount').val());
				var businessName = $('#businessname').val();
				$.post('dayBook.json','action=update-offloading-charges&id='+idVal+'&allowancesAmount='+allowanceAmount+'&businessName='+businessName,function(obj){
					$(this).successMessage({
						container : '.day-book-page-container',
						data : obj.result.message
					});
				});
		}
		
	});
	$('.delete-icon').click(function(){
		var id = $(this).attr("id");
		$('#error-message').html('Are you sure you want to Delete?');
		$("#error-message").dialog(
						{
							resizable : false,
							height : 140,
							title : "<span class='ui-dlg-confirm'>Confirm</span>",
							modal : true,
							buttons : {
								'Ok' : function() {
									$(this).dialog('close');
									$.post('dayBook.json','action=delete-offloading-charges&id='+id,function(obj){
										$(this).successMessage({
											container : '.day-book-page-container',
											data : obj.result.message
										});
									});
								},
								Cancel : function() {
									$(this).dialog('close');
								}
							}
						});
		return false;
	});
},
getExisedExpensesData : function(){

	var dayBookType = $('#allowancesType').val();
	$.post('dayBook.json','action=get-expenses-details&dayBookType='+dayBookType,function(obj){
		var data = obj.result.data;
		 if ( typeof data != 'undefined') {
			var alternate = false;
			for(var loop=0;loop<data.length;loop=loop+1) {
				if(alternate) {
					var rowstr = '<div class="green-result-row alternate">';
				} else {
					rowstr = '<div class="green-result-row">';
				}
				var executiveAllowances =  currencyHandler.convertStringPatternToFloat(data[loop].executiveAllowances);
				if( data[loop].remarks == "null"||data[loop].remarks == 0){
					 data[loop].remarks = "";
				}
				alternate = !alternate;
				rowstr = rowstr + '<div class="green-result-col-1">'+
				'<div class="result-body">' +
		
				'<span class="property">Amount: '+' </span><span class="property-value">' +currencyHandler.convertFloatToStringPattern(executiveAllowances.toFixed(2)) + '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-2">'+
				'<div class="result-body">' +
				'<span class="property">Remarks: '+' </span><span class="property-value">' + data[loop].remarks + '</span>' +
				'</div>' +
				'</div>' +
				'<div class="green-result-col-action">' + 
				'<div id="'+data[loop].id+'" executiveAllowances ="'+data[loop].executiveAllowances+'" class="edit-vehicle ui-btn edit-icon" remarks="'+data[loop].remarks+'" title="Edit Vehicle Details"></div>' + 
				'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Vehicle Details"></div>';
		
				rowstr = rowstr + '<div id="'+data[loop].id+'"   class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
				'</div>' +
				'</div>';
				if(loop == 0){
					$('#search-results-list').html('');
					$('#search-results-list').append(rowstr);
				}else{
					$('#search-results-list').append(rowstr);
				}
	};
	TempDayBookHandler.initExpensesButtons ();
	$('#search-results-list').jScrollPane({showArrows:true});
		}else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
		}
	});

},
initExpensesButtons : function(){

	var idVal;
	var amount;
	$('.edit-icon').click(function(){
		$("#remarksValid").empty();
		$('#allowancesValid').empty();
		$('#meterReadingValid').empty();
		$('#businessNameValid').empty();
		$('#dayBookTypeValid').empty();
		$('#amountToBankValid').empty();
		idVal = $(this).attr('id');
		$('#search-results-list').html('');
		$('#button-save').hide();
		$('#button-update').show();
		 amount = $(this).attr("executiveAllowances");
		var remarks = $(this).attr('remarks');
		if(remarks != 0){
			$('#Remarks').val(remarks);
		}
		
		$('#allowanceAmount').val(amount);
	});
	$('#button-update').click(function(){
		$('.page-content').ajaxSavingLoader();
		var dayBookType = $('#allowancesType').val();
		$.ajax({type: "POST",
			url:'dayBook.json', 
			data: 'action=check-available-deposit-amount',
			async : false,
			success: function(obj){
			amount = obj.result.data;
				var allowanceAmount = $('#allowanceAmount').val();
				if( currencyHandler.convertStringPatternToFloat(allowanceAmount) > parseFloat(amount)){
					$.loadAnimation.end();
					check = false;
					if(amount > 0.0){
						showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
						return check;
					}else{
						showMessage({title:'Error', msg:'No Cash Available'});
						return check;
					}
				}else{
					check = true;
				}
			},
		});
		if(check == false){
			$.loadAnimation.end();
			if(amount > 0.0){
				showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
				return check;
			}else{
				showMessage({title:'Error', msg:'No Cash Available'});
				return check;
			}
		}else{
			var dayBookType = $('#allowancesType').val();
			var allowanceAmount = currencyHandler.convertStringPatternToFloat($('#allowanceAmount').val());
			var remarksData = $('#Remarks').val();
			$.post('dayBook.json','action=update-expenses-data&id='+idVal+'&allowancesAmount='+allowanceAmount+'&remarks='+remarksData+'&dayBookType='+dayBookType,function(obj){
				$.loadAnimation.end();
				$(this).successMessage({
					container : '.day-book-page-container',
					data : obj.result.message
				});
			});
		
		}
		});
	$('.delete-icon').click(function(){
		var dayBookType = $('#allowancesType').val();
		var id = $(this).attr("id");
		$('#error-message').html('Are you sure you want to Delete?');
		$("#error-message").dialog(
						{
							resizable : false,
							height : 140,
							title : "<span class='ui-dlg-confirm'>Confirm</span>",
							modal : true,
							buttons : {
								'Ok' : function() {
									$(this).dialog('close');
									$.post('dayBook.json','action=delete-expenses-data&id='+id+'&dayBookType='+dayBookType,function(obj){
										$(this).successMessage({
											container : '.day-book-page-container',
											data : obj.result.message
										});
									});
								},
								Cancel : function() {
									$(this).dialog('close');
								}
							}
						});
		return false;
	});

},
	suggestBusinessName : function(thisInput) {
		var suggestionsDiv = $('#business-name-suggestions');
		var val = $('#businessname').val();
		$.post('dayBook.json','action=get-business-name&businessNameVal=' + val,function(obj) {
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
										thisInput.val($(this).html());
										thisInput.attr('businessname', $(this).attr('businessname'));
										var businessname = $(this).attr('businessname');
										$('#businessname').attr('value', businessname);
										suggestionsDiv.hide();
									});
						});
	},
	initVehicleFuelExpensesButtons : function(){
		var prevReading;
		var prevAmt ;
		var idVal;
		var test;
		$('.edit-icon').click(function(){
			prevReading = $(this).attr('meterReading');
			$("#remarksValid").empty();
			$('#allowancesValid').empty();
			$('#meterReadingValid').empty();
			$('#businessNameValid').empty();
			$('#dayBookTypeValid').empty();
			$('#amountToBankValid').empty();
			idVal = $(this).attr('id');	
			$('#search-results-list').html('');
			$('#button-save').hide();
			$('#button-update').show();
			prevAmt = $(this).attr("allowancesAmount");
			$('#meterreading').val(prevReading);
			$('#allowanceAmount').val(prevAmt);
		});
		$('#button-update').click(function(){
			$('.page-content').ajaxSavingLoader();
			currentmeterReading = $('#meterreading').val();
			currentAmount = $('#allowanceAmount').val();
			if(prevReading != currentmeterReading){
				if(ValidateTempDayBookHandler.validateMeterReading(0) == false){
					 $.loadAnimation.end();
					return true;
				}
			}
			if(prevAmt != currentAmount ){
				$.ajax({type: "POST",
					url:'dayBook.json', 
					data: 'action=check-available-deposit-amount',
					async : false,
					success: function(obj){
					amount = obj.result.data;
						var allowanceAmount = $('#allowanceAmount').val();
						if( currencyHandler.convertStringPatternToFloat(allowanceAmount) > parseFloat(amount)){
							 $.loadAnimation.end();
							check = false;
							if(amount > 0.0){
								showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
								return check;
							}else{
								showMessage({title:'Error', msg:'No Cash Available'});
								return check;
							}
						}else{
							check = true;
						}
					},
				});
				if(check == false){
					 $.loadAnimation.end();
					if(amount > 0.0){
						showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
						return check;
					}else{
						showMessage({title:'Error', msg:'No Cash Available'});
						return check;
					}
				}else{
					var allowanceAmount =currencyHandler.convertStringPatternToFloat($('#allowanceAmount').val());
					var meterReading = $('#meterreading').val();
					TempDayBookHandler.updateVehicleFuelExpenses(allowanceAmount,meterReading,idVal);
				}
			}else{
				var allowanceAmount =currencyHandler.convertStringPatternToFloat($('#allowanceAmount').val());
				var meterReading = $('#meterreading').val();
				TempDayBookHandler.updateVehicleFuelExpenses(allowanceAmount,meterReading,idVal);
			}
			});
		$('.delete-icon').click(function(){
			idVal = $(this).attr('id');
			$('#error-message').html('Are you sure you want to Delete?');
			$("#error-message").dialog(
							{
								resizable : false,
								height : 140,
								title : "<span class='ui-dlg-confirm'>Confirm</span>",
								modal : true,
								buttons : {
									'Ok' : function() {
										$(this).dialog('close');
										$.post('dayBook.json','action=delete-vehicle-fuel-expenses&id='+idVal,function(obj){
											$(this).successMessage({
												container : '.day-book-page-container',
												data : obj.result.message
											});
										});
									},
									Cancel : function() {
										$(this).dialog('close');
									}
								}
							});
		});
		
	},
	updateVehicleFuelExpenses:function(allowanceAmount,meterReading,idVal){
		$.post('dayBook.json','action=update-vehicle-fuel-expenses&id='+idVal+'&allowancesAmount='+allowanceAmount+'&meterReading='+meterReading,function(obj){
			 $.loadAnimation.end();
			$(this).successMessage({
				container : '.day-book-page-container',
				data : obj.result.message
			});
		});
	
	},
	getExistedVehiicleDetails : function(){
		$.post('dayBook.json','action=get-existed-vehicle-details',function(obj){
			var data = obj.result.data;
			if(data != undefined|| data == 'true'){
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
			
					'<span class="property">Driver Name: '+' </span><span class="property-value">' + data[loop].driverName + '</span>' +
					'<span class="property"style="margin-left:76px;">Vehicle No: '+' </span><span class="property-value">' +data[loop].vehicleNo + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">Starting Reading: '+' </span><span class="property-value">' + data[loop].startingReading + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" driverName="'+data[loop].driverName+'" class="edit-vehicle ui-btn edit-icon"vehicleNo="'+data[loop].vehicleNo+'" StartReading = "'+data[loop].startingReading +'" title="Edit Vehicle Details"></div>' + 
					'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Vehicle Details"></div>';
			
					rowstr = rowstr + '<div id="'+data[loop].id+'"   class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
					'</div>' +
					'</div>';
					if(loop == 0){
						$('#search-results-list').html('');
						$('#search-results-list').append(rowstr);
					}else{
						$('#search-results-list').append(rowstr);
					}
		};
		TempDayBookHandler.initSearchResultButtons ();
		$('#search-results-list').jScrollPane({showArrows:true});
			}else {
					$('#search-results-list').html('');
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
		});
	},
	getExistedAmounts : function(){
		$.post('dayBook.json','action=get-deposited-amounts',function(obj){
			var data = obj.result.data;
			if(data.length>0){
				var alternate = false;

				for(var loop=0;loop<data.length;loop=loop+1) {
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
					var bankAmount = currencyHandler.convertStringPatternToFloat(data[loop].amountToBank);
					if( data[loop].remarks == "null" || data[loop].remarks == 0 ){
						 data[loop].remarks = "";
					}
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-body">' +
			
					'<span class="property">Amount To Bank: '+' </span><span class="property-value">' +currencyHandler.convertFloatToStringPattern(bankAmount.toFixed(2) )+ '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">Remarks: '+' </span><span class="property-value">' +data[loop].remarks + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" amountToBank="'+data[loop].amountToBank+'" class="ui-btn edit-icon"remarks="'+data[loop].remarks+'" title="Edit Vehicle Details"></div>' + 
					'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Vehicle Details"></div>';
			
					rowstr = rowstr + '<div id="'+data[loop].id+'"   class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
					'</div>' +
					'</div>';
					if(loop == 0){
						$('#search-results-list').html('');
						$('#search-results-list').append(rowstr);
					}else{
						$('#search-results-list').append(rowstr);
					}
			
		};
		TempDayBookHandler.initAmountResultButtons ();
		$('#search-results-list').jScrollPane({showArrows:true});
			}else {
				$('#search-results-list').html('');
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
					
			}
		});
	},
	initSearchResultButtons : function(){
		var idVal;
		$('.edit-icon').click(function(){
			$('#search-results-list').html('');
			$('#button-save').hide();
			$('#button-update').show();
			 idVal= $(this).attr("id");
			$("#drivername").val($(this).attr('driverName'));
			$('#vehicleNumber').val($(this).attr('vehicleNo'));
			$('#startingReading').val($(this).attr('StartReading'));
			
		});
		$('.delete-icon').click(function(){
			var id = $(this).attr("id");
			$('#error-message').html('Are you sure you want to Delete?');
			$("#error-message").dialog(
							{
								resizable : false,
								height : 140,
								title : "<span class='ui-dlg-confirm'>Confirm</span>",
								modal : true,
								buttons : {
									'Ok' : function() {
										$(this).dialog('close');
										$.post('dayBook.json','action=delete-vehicle-details&id='+id,function(obj){
											$(this).successMessage({
												container : '.day-book-page-container',
												data : obj.result.message
											});
										});
									},
									Cancel : function() {
										$(this).dialog('close');
									}
								}
							});
			return false;
		});
		
		$('#button-update').click(function(){
			$('.page-content').ajaxSavingLoader();
			if(ValidateTempDayBookHandler.validateVehicleDetails()==false){
				 $.loadAnimation.end();
				return true;
			}
			var driverName = $("#drivername").val();
			var startingReading = $('#startingReading').val();
			var vehicleNo = $('#vehicleNumber').val();
			$.post('dayBook.json','action=update-vehicle-details&driverName='+driverName+'&startingReading='+startingReading+'&vehicleNo='+vehicleNo+'&id='+idVal,function(obj){
				 $.loadAnimation.end();
				$(this).successMessage({
					container : '.day-book-page-container',
					data : obj.result.message
				});
			});
		});
	},
	
	//Allowances edit related functionality.
	allowanceEdit : function(id) {
		var mainId = $('#'+id).parent().attr('id');
		$('#'+mainId).children('.row2').children('.col2').removeAttr('readonly');
		
		if($('#executive-allowances-view-dialog').dialog("option", "title") != "Offloading Charges Details"){
			$('#'+mainId).children('.row4').children('.col4').removeClass('read-only');
			$('#'+mainId).children('.row4').children('.col4').removeAttr('readonly');
		}
		$('#'+mainId).children('.row2').children('.col2').removeClass('read-only');
		$('#'+mainId).children('.row5').hide();
		$('#'+mainId).children('.row6').show();
		var valueOne = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row2').children('.col2').val());
		$('#'+mainId).children('.row2').children('.col2').val(currencyHandler.convertFloatToStringPattern(valueOne.toFixed(2)));
		TempDayBookHandler.checkAmountAvailability(id,valueOne);
	},
	
	//Allowances update related functionality.
	allowanceUpdate : function(id, dayBookType) {
		var mainId = $('#'+id).parent().attr('id');
		var valueOne = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row2').children('.col2').val());
		var valueThree;
		var amountToFactory =  $('#amountToFactory').val();
		if ("Vehicle Fuel Expenses" == dayBookType) {
			valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
		} else {
			valueThree = $('#'+mainId).children('.row4').children('.col4').val();
		}
		if($('#executive-allowances-view-dialog').dialog("option", "title") == "Vehicle Fuel Expenses Details"){
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
				if(TempDayBookHandler.validateMeterreading(valueThree,id) == false){
						showMessage({title:'Message', msg:'Meter Reading should greater than '+ startingReading +'  And should be less than  '+ endingReading +''});
						event.preventDefault();
						return false;
				}
			}
		$.ajax({type: "POST",
			url:'dayBook.json', 
			data: 'action=check-available-deposit-amount',
			async : false,
			success: function(data){
				amount = data.result.data;
				if($('#closingBalance').val() == 0){
					if( valueOne > Number(prevVal)){
						if(amount > 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
						}
					}else{
						TempDayBookHandler.updateAllowances(id,valueOne,valueThree);
					}
				}else{
					if(valueOne >Number(amount)+Number(prevVal)-Number(amountToFactory)){
						var resVal = Number(amount)+Number(prevVal)-Number(amountToFactory);
						if(resVal > 0.0){
							showMessage({title:'Error', msg:'Only '+resVal+' of Cash Available'});
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
						}
					}else{
						TempDayBookHandler.updateAllowances(id,valueOne,valueThree);
					}
				}
		 }
			});
		/*if($('#executive-allowances-view-dialog').dialog("option", "title") == "Vehicle Fuel Expenses Details"){
			if(TempDayBookHandler.validateMeterreading(valueThree,id) == false){
				showMessage({title:'Message', msg:'Meter Reading should greater than starting reading And should be less than Ending Reading'});
				even.preventDefault();
				return ;
			}
		}*/
		
	},
	updateAllowances : function(id,valueOne,valueThree){
		var mainId = $('#'+id).parent().attr('id');
		$.post('dayBook.json', 'action=edit-allowance&id='+id+'&valueOne='+valueOne+'&valueThree='+valueThree, function(obj) {
			$('#'+mainId).children('.row2').children('.col2').attr('readonly', 'readonly');
			$('#'+mainId).children('.row4').children('.col4').attr('readonly', 'readonly');
			$('#'+mainId).children('.row2').children('.col2').addClass('read-only');
			$('#'+mainId).children('.row4').children('.col4').addClass('read-only');
			$('#'+mainId).children('.row5').show();
			$('#'+mainId).children('.row6').hide();
		});
	},
	
	initAmountResultButtons : function(){
		var idVal;
		$('.edit-icon').click(function(){
			$("#remarksValid").empty();
			$('#allowancesValid').empty();
			$('#meterReadingValid').empty();
			$('#businessNameValid').empty();
			$('#dayBookTypeValid').empty();
			$('#amountToBankValid').empty();
			$('#search-results-list').html('');
			$('#button-save').hide();
			$('#button-update').show();
			 idVal= $(this).attr("id");
			$("#bankAmount").val($(this).attr('amountToBank'));
			$('#Remarks').val($(this).attr('remarks'));
			
		});
		$('.delete-icon').click(function(){
			var id = $(this).attr("id");
			$('#error-message').html('Are you sure you want to Delete?');
			$("#error-message").dialog({
				resizable : false,
				height : 140,
				title : "<span class='ui-dlg-confirm'>Confirm</span>",
				modal : true,
				buttons : {
					'Ok' : function() {
						      $(this).dialog('close');
							  $.post('dayBook.json','action=delete-amount-details&id='+id,function(obj){
							  $(this).successMessage({
									container : '.day-book-page-container',
									data : obj.result.message
								});
						      });
							},
					Cancel : function() {
					     	$(this).dialog('close');
						}
					 }
			    });
			return false;
		
		});
		$('#button-update').click(function(){
			$('.page-content').ajaxSavingLoader();
			if(ValidateTempDayBookHandler.validateAmounts()==false){
				 $.loadAnimation.end();
				return true;
			}
			$.post('dayBook.json', 'action=check-available-deposit-amount', function(obj) {
				amount = obj.result.data;
					var allowanceAmount = $('#allowanceAmount').val();
					if( currencyHandler.convertStringPatternToFloat(allowanceAmount) > parseFloat(amount)){
						 $.loadAnimation.end();
						check = false;
						if(amount > 0.0){
							showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
							return check;
						}else{
							showMessage({title:'Error', msg:'No Cash Available'});
							return check;
						}
					}else{
						check = true;
					}
			});
			if(check == false){
				 $.loadAnimation.end();
				if(amount > 0.0){
					showMessage({title:'Error', msg:'Only '+parseFloat(amount).toFixed(2)+' of Cash Available'});
					return false;
				}else{
					showMessage({title:'Error', msg:'No Cash Available'});
					return false;
				}
			}else{
			 var amountToBank =currencyHandler.convertStringPatternToFloat($("#bankAmount").val());
				var remarks = $('#Remarks').val();
				$.post('dayBook.json','action=update-amounts&bankAmount='+amountToBank+'&remarks='+remarks+'&id='+idVal,function(obj){
					 $.loadAnimation.end();
					$(this).successMessage({
						container : '.day-book-page-container',
						data : obj.result.message
					});
					
				});
			}
			});

	},
	
}