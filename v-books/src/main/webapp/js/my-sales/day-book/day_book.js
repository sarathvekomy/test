var DayBookHandler = {
		theme: "",
		expanded: true,
		initPageLinks : function() {
			$('#add-day-book').pageLink({
				container : '.day-book-page-container',
				url : 'my-sales/day-book/day_book_add.jsp'
			});
		},
		checkLength:function(len,number){
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
		dayBookSteps : [  '#day-book-basic-info-form' , '#day-book-allowances-form', '#day-book-amount-form','#day-book-product-form','#day-book-vehicle-details-form' ],
		dayBookUrl : [ 'dayBook.json' , 'dayBook.json', 'dayBook.json','dayBook.json' ,'dayBook.json'],
		dayBookStepCount : 0,
		initAddButtons : function() {
			DayBookHandler.dayBookStepCount = 0;

			$('#action-clear').click(function() {
								$(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).clearForm();});

			$('#button-next').click(
					function() {
						var success = true;
						var resultSuccess =true;
						if(DayBookHandler.dayBookStepCount == 1||DayBookHandler.dayBookStepCount == 2){
							if ($(DayBookHandler.dayBookSteps[DayBookHandler.dayBookStepCount]).validate() == false)
								return;
							}
						if(DayBookHandler.dayBookStepCount == 4){
						if(DayBookHandler.validateReading()==false){
							return resultSuccess;
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
							if(formData.name ==='reasonAmountToBank'||formData.name === 'reasonMiscellaneousExpenses'||formData.name === 'remarks'){ 
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
						
						//convert serialized array to url-encoded string
						/*var sendFormData=$.param(paramString);
						alert(sendFormData);
						var serializedData = sendFormData.replace(/[^&]+=\0?(?:&|$)/g, '')
						alert(serializedData);*/
						/*/
						&?      # Optional ampersand
						[^=&]+  # 1 or more characters except = or &
						=       # Equals sign
						(&|$)   # Either another ampersand or end of string/line
						/g      # Global search*/
						// serializedData = 'age=99'  <- Removes all empty values
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
												if (DayBookHandler.dayBookStepCount > 0) {
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
											          $.post('dayBook.json','action=save-day-book-data&listOfProductObjects=' + listOfObjects,function(obj) {

											          });
													$('#button-prev').show();
													$('.page-buttons').css('margin-left', '150px');
													if (DayBookHandler.dayBookStepCount == 3) {
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
													if(DayBookHandler.dayBookStepCount == 4){
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
	$('#button-prev')
			.click(	function() {
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
							if (DayBookHandler.dayBookStepCount == 4) {
								$('.page-selection').animate( { width:"183px"}, 0,function(){
									$('.page-link-strip').show();
									$('.module-title').show();
								});
								$('.page-container').animate( { width:"702px"}, 0);
							}
							if (DayBookHandler.dayBookStepCount == 3) {
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
							$('.page-buttons').css('margin-left', '240px');
						}
					});
			$('#button-save').click(function() {
				var thisButton = $(this);
				var paramString = 'action=save-daybook';
				PageHandler.expanded=false;
				pageSelctionButton.click();
				var isReturn = $('#isReturn').val();
				$.post('dayBook.json', paramString+'&isReturn='+isReturn, function(obj) {
					$(this).successMessage({
						container : '.day-book-page-container',
						data : obj.result.message
					});
				});
			});
			$('#add-day-book').click(function() {
				$('.day-book-page-container').load('my-sales/day-book/day_book_add.jsp');
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
			    			var container ='.day-book-page-container';
			    			var url = "my-sales/day-book/day_book_add.jsp";
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
			$('#vehicleMeterReading').blur(function() {
				var vehicleMeterReading=currencyHandler.convertStringPatternToFloat($('#vehicleMeterReading').val());
				var fomatvehicleMeterReading=currencyHandler.convertFloatToStringPattern(vehicleMeterReading.toFixed(2));
				$('#vehicleMeterReading').val(fomatvehicleMeterReading);
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
		load:function() {
			$.post('dayBook.json','action=get-allot-stock-opening-balance', function(obj) {
				var allotStockOpeningBalVal = currencyHandler.convertStringPatternToFloat(obj.result.data);
				var fomatAllotStockOpeningBal=currencyHandler.convertFloatToStringPattern(allotStockOpeningBalVal.toFixed(2));
				$('#allotStockOpeningBalance').val(fomatAllotStockOpeningBal);
			});
			$.post('dayBook.json', 'action=lookup-allotment-type', function(obj) {
				var isDaily = obj.result.data;
				if(isDaily == 'true') {
					$('#isReturn').attr('checked', 'checked');
					$('#isReturn').attr("value", true);
					$('#isReturn').attr("disabled", true);
				} else {
					$('#isReturn').removeAttr("disabled");
					$('#isReturn').attr("value", false);
				}
			});
			
			$('#isReturn').change(function() {
				if($('#isReturn').is(':checked')) {
					$('#isReturn').attr("value", true);
				} else {
					$('#isReturn').attr("value", false);
				}
			});
			
			$('.green-results-list').html('');
			$.post('dayBook.json', 'action=get-grid-data', function(obj) {
				var data = obj.result.data;
				if(data != undefined){
					if(data.length>0) {
						var count=1;
						for(var x=0;x<data.length;x=x+1) {
							var rowstr='<div class="ui-content report-content" id="day-book-grid">';
							rowstr=rowstr+'<div id="day-book-row-'+count+'" class="report-body" style="width: 830px;height: auto; overflow: hidden;">';
							rowstr=rowstr+'<div id = "row1" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].productName+'</div>';
							rowstr=rowstr+'<div id = "row7" class="report-body-column2 centered" style="height: inherit; width: 100px; word-wrap: break-word;">'+data[x].batchNumber+'</div>';
							rowstr=rowstr+'<div id = "row2" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].openingStock)+'</div>';
							rowstr=rowstr+'<div id = "row6" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].returnQty)+'</div>';
							rowstr=rowstr+'<div id="row3" class="report-body-column2 right-aligned productToCustomer" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].productsToCustomer)+'</div>';
							rowstr=rowstr+'<div id="row4" class="report-body-column2 centered productToFactories" style="height: inherit; width: 100px; word-wrap: break-word;">'+'<input type = "text" style="margin-top:-3px;" class="productToFactory" size=6px>'+'</div>';
							rowstr=rowstr+'<div id="row5" class="report-body-column2 right-aligned" style="height: inherit; width: 100px; word-wrap: break-word;">'+currencyHandler.convertNumberToStringPattern(data[x].closingStock)+'</div>';
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
		validateAllowances :function(){
			var result = true;
			end = $('#executiveAllowances').val().length;
			if(/^[0-9.,]+$/.test($('#executiveAllowances').val())==false||$('#executiveAllowances').val().charAt(0)==" "||$('#executiveAllowances').val().charAt(end -1)==" "){
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
			return result;
			
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
		getCustomerTotalPayable:function() {
			$.post('dayBook.json','action=get-customer-total-payable', function(obj) {
				var customerTotalPayable = obj.result.data.presentPayable;
				var customerTotalReceived = obj.result.data.presentPayment;
				var totalCredit = parseFloat(customerTotalPayable) - parseFloat(customerTotalReceived);
				$('#customerTotalPayable').val(currencyHandler.convertFloatToStringPattern(customerTotalPayable));
				$('#customerTotalReceived').val(currencyHandler.convertFloatToStringPattern(customerTotalReceived));
				$('#customerTotalCredit').val(currencyHandler.convertFloatToStringPattern(totalCredit.toFixed(2)));
			});	
		}
	};