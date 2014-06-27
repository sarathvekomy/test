var SalesBookHandler = {
		theme: "",
		expanded: true,
		count: 0,
initPageLinks: function() {
$('#allot-stock').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/sales-book/allot_stock.jsp'
});
},
initAllotStockSelection: function() {
	PageHandler.hidePageSelection();
},
showPageSelection:function(){
	PageHandler.expanded = PageHandler.expanded;
	if(!PageHandler.expanded) {
		$('#ps-exp-col').click();
		$('.page-selection').animate( { width:"183px"}, 0,function(){
			$('.page-link-strip').show();
			$('.module-title').show();
		});
		$('.page-container').animate( { width:"702px"}, 0);
		$('div#search-results-list').css({width:"698px"});
	}else {
		$('.page-selection').animate( { width:"183px"}, 0,function(){
			$('.page-link-strip').show();
			$('.module-title').show();
		});
		$('.page-container').animate( { width:"702px"}, 0);
		$('div#search-results-list').css({width:"698px"});
	}
},
//Allot Stock Search Result
initSearchSalesExecutiveOnLoad: function() {
	$('#action-clear').click(function() {
		var createdOnVal = $('#createdOn').val();
		var salesExecutiveVal = $('#salesExecutive').val();
		$('#allot-stock-form').clearForm();
		$('#createdOn').val(createdOnVal);
		$('#salesExecutive').val(salesExecutiveVal);
		SalesBookHandler.loadClear(salesExecutiveVal);
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
					$('.task-page-container').html('');
	    			var container ='.accounts-page-container';
	    			var url = "accounts/sales-book/allot_stock.jsp";
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
},
// Getting the grid data for new sales executive.
load:function() {
		$.post('accounts.json','action=get-product-details',function(obj) {
			$('#products-search-results-list').show();
			var data= obj.result.data;
			SalesBookHandler.loadGrid(data);
		});
},

loadClear:function(salesExecutiveVal){
	$(".product_row").each(function() {
		  var id = $(this).find('div').attr('id');
		  $(this).find('input').val(0);
	});
	var salesExecutive = salesExecutiveVal;
	var paramString='action=show-hide-buttons';
	$.post('accounts.json', paramString+'&salesExecutive='+salesExecutive,function(obj){
		var data = obj.result.data;
		if(data.length == 1){
			$.post('accounts.json','action=get-closing-balance&salesExecutiveVal='+salesExecutive, function(obj){
				if('fail' == obj.result.status) {
					$('#previousClosingBalance').val("0.00");
				} else {
					$('#previousClosingBalance').val(parseFloat(obj.result.data).toFixed(2));
				}
			});
			$('#openingBalance').val("");
			$('#advance').val("");
			$('#button-save').show();
			$('#button-update').hide();
		} else{
			$.post('accounts.json','action=get-closing-balance&salesExecutiveVal='+salesExecutive, function(obj){
				if('fail' == obj.result.status) {
					$('#previousClosingBalance').val("0.00");
				} else {
					$('#previousClosingBalance').val(parseFloat(obj.result.data).toFixed(2));
				}
			});
			$('#openingBalance').val(parseFloat(data[0].openingBalance).toFixed(2));
			$('#advance').val(parseFloat(data[0].advance).toFixed(2));
			for(var loop=1;loop<data.length;loop++) {
				var productName = data[loop].productName;
				$(".product_row").each(function() {
					var id = $(this).find('div').attr('id');
					var productNameGrid = $('#'+id).find('#productName').html();
					if(productName == productNameGrid){
						 $(this).find('input').val(data[loop].qtyAllotted);
					}
				});
			}
			$('#button-save').hide();
			$('#button-update').show();
		}
	});
	SalesBookHandler.load();
},
initSalesExecutiveName : function(){
	$('#salesExecutive').click(function() {
		var thisInput = $(this);
		$('#sales-executive-name-suggestions').show();
		SalesBookHandler.suggestSalesExecutiveName(thisInput);
	});
	$('#salesExecutive').keyup(function() {
		var thisInput = $(this);
		$('#sales-executive-name-suggestions').show();
		SalesBookHandler.suggestSalesExecutiveName(thisInput);
	});
	$('#salesExecutive').focusout(function() {
		$('#sales-executive-name-suggestions').animate({
			display : 'none'
		}, 1000, function() {
			$('#sales-executive-name-suggestions').hide();
		});
	});
	$('.btn-clear').live('click', function(cfg) {
		var createdOn = $('#createdOn').val();
		 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
		$("#error-message").dialog({
			resizable: false,
			height:140,
			title: "<span class='ui-dlg-confirm'>Confirm</span>",
			modal: true,
			buttons: {
				'Ok' : function() {
					$('form').clear();
					$('#createdOn').val(createdOn);
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#createdOn').val(createdOn);
	});

	$('.btn-cancel').click(function() {
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
					    			var container ='.accounts-page-container';
					    			var url = "accounts/sales-book/allot_stock.jsp";
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
},
suggestSalesExecutiveName : function(thisInput) {
	var suggestionsDiv = $('#sales-executive-name-suggestions');
	var val = $('#salesExecutive').val();
	$.post('accounts.json','action=search-sales-data&salesExecutiveNameVal=' + val,function(obj) {
		$.loadAnimation.end();
		suggestionsDiv.html('');
		var data = obj.result.data;
		if (data != undefined) {
			var htmlStr = '<div>';
			for ( var loop = 0; loop < data.length; loop = loop + 1) {
				htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">' + data[loop].salesExecutive + '</a></li>';
			}
			htmlStr += '</div>';
			suggestionsDiv.append(htmlStr);
		} else {
			suggestionsDiv.append('<div id="">' + 'No SalesExecutive Names' + '</div>'); 
		}
		suggestionsDiv.css('left', thisInput.position().left);
		suggestionsDiv.css('top', thisInput.position().top + 25);
		suggestionsDiv.show();
		$('.select-teacher').click(function() {
			thisInput.val($(this).html());
			thisInput.attr('salesExecutive', $(this).attr('salesExecutive'));
			$('#salesExecutive').attr('value', $(this).attr('salesExecutive'));
			suggestionsDiv.hide();
		});
	});
	$('#sales-executive-name-suggestions').click(function (){
		var salesExecutive = $('#salesExecutive').val();
		if(salesExecutive != '') {
			var paramString='action=show-hide-buttons';
			$.post('accounts.json', paramString+'&salesExecutive='+salesExecutive,function(obj){
				var data = obj.result.data;
				// For new sales executive.
				if(data == undefined){
					$('#previousClosingBalance').val("0.00");
					$('#closingBalance').val("0.00");
					$('#openingBalance').val("");
					$('#advance').val("");
					$('#button-save').show();
					$('#button-update').hide();
					SalesBookHandler.load();
				}
				//For existing sales executive.
				else{
					$('#previousClosingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].previousClosingBalance).toFixed(2)));
					$('#openingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].openingBalance).toFixed(2)));
					$('#advance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].advance).toFixed(2)));
					$('#allotmentType').val(data[0].allotmentType);
					$('#products-search-results-list').show();
					var data= obj.result.data;
					SalesBookHandler.loadGrid(data);
					var flag = data[0].flag;
					if(flag == '1') {
						if('Non-Daily' == data[0].allotmentType) {
							$('#button-save').hide();
							$('#button-update').hide();
							$('#action-clear').hide();
							$('#action-cancel').hide();
							$('.edit-icon').hide();
						} else {
							$('#button-save').hide();
							$('#button-update').show();
							$('.edit-icon').show();
						}
					} else {
						$('#button-save').show();
						$('#button-update').hide();
						$('.edit-icon').hide();
					}
				}
			});
		} 
	});
	$('#ps-exp-col').click(function() {
	    if(PageHandler.expanded) {
	    	$('.report-header').css( "width", "810px" );
			$('.report-body').css( "width", "810px" );
			$('.report-header-column2').css( "width", "90px" );
			$('.report-body-column2').css( "width", "90px" );
			$('.quantityAlloted').css( "width", "80px" );
			$('.quantityAllotedText').css( "width", "80px" );
			$('#products-search-results-list').css("width", "699px");
			$('.grid-content' ).css( "width", "1045px" );
			$('.jScrollPaneContainer').css("width","810px");
		} else {
			$('.report-header').css( "width", "1044px" );
			$('.report-body').css( "width", "1045px" );
			$('.report-header-column2').css( "width", "100px" );
			$('.report-body-column2').css( "width", "100px" );
			$('.quantityAlloted').css( "width", "90px" );
			$('.quantityAllotedText').css( "width", "90px" );
			$('#products-search-results-list').css("width", "830px");
			$('.grid-content' ).css( "width", "1045px" );
			$('.jScrollPaneContainer').css("width","1045px");
		}
		setTimeout(function() {
			$('#sales-search-results-list').jScrollPaneRemove();
			$('#sales-search-results-list').jScrollPane({
				showArrows : true
			});
		}, 0);
	});
	
	$('#advance').change(function(){
		var salesExecutive = $('#salesExecutive').val();
		var advance = currencyHandler.convertStringPatternToFloat($('#advance').val());
	   	var formatAdvance=currencyHandler.convertFloatToStringPattern(advance.toFixed(2));
	   	if(advance < parseFloat(10000000000000)){
	   		$('#advance').val(formatAdvance);
	   	}
	   
	   	var previousClosingBalance = currencyHandler.convertStringPatternToFloat($('#previousClosingBalance').val());
	   	var openingBalanceVal = parseFloat(advance) + parseFloat(previousClosingBalance);
	   	if(isNaN(openingBalanceVal)) {
	   	 openingBalanceVal = 0.00;
	   	}
	   	$('#openingBalance').val(currencyHandler.convertFloatToStringPattern(openingBalanceVal.toFixed(2)));
   	});
	$('#advance').change(function(){
		SalesBookHandler.validateAdvance();
	})
	
	$('#allot-stock').click(function(){
		  PageHandler.hidePageSelection();
		  $('.page-link-strip').hide();
			$('.module-title').hide();
			$('.page-selection').animate( { width:"55px"}, 0,function(){});
			$('.page-container').animate( { width:"835px"}, 0);
			var thisTheme = PageHandler.theme;
			$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			//SalesBookHandler.initProductGrid();
	});
	
	$('.grid-content').live("blur",function(){
		 $.each($('.report-body'), function(index, value) {
			  var availableQtyVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
			  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
	          var quantityAllotted = currencyHandler.convertStringPatternToNumber($(this).find('#quantityAllotted').val());
	          var productName = $(this).find('#productName').html();
	          var batchNumber = $(this).find('#batchNumber').html();
	          var salesExecutive = $('#salesExecutive').val();
	          var thisId = $(this);
	          
	          //validating the allotted quantity with available quantity.
	          if(parseFloat(quantityAllotted) < 0 || parseInt(quantityAllotted) > parseInt(availableQtyVal)) {
	         	  	showMessage({title:'Error', msg:'Please Enter valid Quantity.'});
	         	  	return;
	           }
	          $.post('accounts.json','action=get-existing-allotment&productName='+productName+'&batchNumber='+batchNumber+'&salesExecutive='+salesExecutive ,function(obj) {
	        	  var existingAllotment;
	        	  if('fail' == obj.result.status) {
	        		existingAllotment = "0";
	        	  } else {
	        		  if(isNaN(obj.result.data)) {
	        			  existingAllotment = "0";
	        		  } else {
	        			  existingAllotment = parseInt(obj.result.data);
					}
				  }
	        	  if(parseInt(existingAllotment) != quantityAllotted) {
	        		  var sum= parseInt(qtyClosingBalance) + parseInt(quantityAllotted) + parseInt(existingAllotment);
			          if(isNaN(sum)) {
			        	  sum = "0";
			          } 
			        $(thisId).find('#qtyOpeningBalance').val(currencyHandler.convertFloatToStringPattern(sum));
			        existingAllotment = "0";
	        	  }
	          });
		 });
	});
	
	$('#button-save').click(function() {
		 $('.page-content').ajaxSavingLoader();
		if(SalesBookHandler.validateAdvance() == false){
			 $.loadAnimation.end();
			return false;
		}
		var thisButton = $(this);
		var listOfObjects = '';
		var salesExecutive = $('#salesExecutive').val();
		$.post('accounts.json', 'action=is-sales-executive-available&salesExecutive='+salesExecutive, function(obj) {
			var isAvailable = obj.result.data;
			if(isAvailable == 'true') {
				var previousClosingBal = currencyHandler.convertStringPatternToFloat($('#previousClosingBalance').val());
				var advance = currencyHandler.convertStringPatternToFloat($('#advance').val());
				var openingBal = currencyHandler.convertStringPatternToFloat($('#openingBalance').val());
				var action = $('#accountsAction').val();
				var allotmentType = $('#allotmentType').val();
				 allotmentVal = 0;
				 openingStock = 0;
				$('div#allot-stock-grid').each(function(index, value) {
					
					  var id = $(this).find('div').attr('id');
					  var availableQtyVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
					  var qtyAllotted = currencyHandler.convertStringPatternToNumber($(this).find('#quantityAllotted').val());
					  var qtyOpeningStock = currencyHandler.convertStringPatternToNumber($(this).find('#qtyOpeningBalance').val());
					  openingStock = Number(openingStock) + Number(qtyOpeningStock);
					  allotmentVal = Number(allotmentVal) + Number(qtyAllotted);
					  if(parseFloat(qtyAllotted) < 0 || parseFloat(qtyAllotted) > parseFloat(availableQtyVal)) {
			         	  	showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
			         	  	$.loadAnimation.end();
			         	  	return false;
			           } else {
			        	   var qtyOpeningBalanceVal = currencyHandler.convertStringPatternToFloat($(this).find('#qtyOpeningBalance').val());
							  var remarks = encodeURIComponent($(this).find('.remarks').val());
							  var productName = $(this).find('#productName').html();
							  var batchNumber = $(this).find('#batchNumber').html();
							  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
							  if(qtyOpeningBalanceVal != 0) {
					        	  if(index == 0){
					        		  listOfObjects +=productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+qtyOpeningBalanceVal+'|'+batchNumber;
					        	  }else{
					        		  listOfObjects +='?'+productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+qtyOpeningBalanceVal+'|'+batchNumber;
					        	  }
					          } 
							  return allotmentVal;
			           }
					  
				});
				 if(allotmentVal == 0 && openingStock == 0){
					 $.loadAnimation.end();
					  showMessage({title:'Warning', msg:'Please Allot Products'});
					 return;
				  }
				 if(listOfObjects != "") {
					 $.post('accounts.json', "action="+action+'&listOfObjs='+listOfObjects+'&salesExecutive='+salesExecutive+'&previousClosingBal='+previousClosingBal+'&advance='+advance+'&openingBal='+openingBal+'&allotmentType='+allotmentType, function(obj){
						 $.loadAnimation.end();
							$('.accounts-page-container').load('accounts/sales-book/allot_stock.jsp');
					  });
				 }
			} else {
				 $.loadAnimation.end();
				showMessage({title:'Warning', msg:'Sales executive not available.'});
			    return false;
			}
			
		});
	});
	
	$('#button-update').click(function() {
		//if($('form').validate() == false) return;
		 $('.page-content').ajaxSavingLoader();
		var resultSuccess=true;
		var resultFailure=false;
		if(SalesBookHandler.validateAllotStock()==false){
			 $.loadAnimation.end();
			return resultSuccess;
		}else{
			var salesExecutive = $('#salesExecutive').val();
			$.post('accounts.json', 'action=is-sales-executive-available&salesExecutive='+salesExecutive, function(obj) {
				var isAvailable = obj.result.data;
				if(isAvailable == 'true') {
					var thisButton = $(this);
					var listOfObjects = '';
					var previousClosingBal = currencyHandler.convertStringPatternToFloat($('#previousClosingBalance').val());
					var advance = currencyHandler.convertStringPatternToFloat($('#advance').val());
					var openingBal = currencyHandler.convertStringPatternToFloat($('#openingBalance').val());
					var action = $('#accountsAction').val();
					var allotmentType = $('#allotmentType').val();
					$('div#allot-stock-grid').each(function(index, value) {
						  var id = $(this).find('div').attr('id');
						  var availableQtyVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
						  var qtyAllotted = currencyHandler.convertStringPatternToNumber($(this).find('#quantityAllotted').val());
						  if(parseFloat(qtyAllotted) < 0 || parseInt(qtyAllotted) > parseInt(availableQtyVal)) {
				         	  	showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
				         	  	$.loadAnimation.end();
				         	  	return false;
				           } else {
				        	  var remarks = encodeURIComponent($(this).find('.remarks').val());
				 			  var productName = $(this).find('#productName').html();
				 			  var batchNumber = $(this).find('#batchNumber').html();
				 			  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
				 			  var availableQuantityVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
				 			  var qtyOpeningBalanceVal = currencyHandler.convertStringPatternToNumber($(this).find('#qtyOpeningBalance').val());
				 			  if(qtyOpeningBalanceVal != 0) {
				 	        	  if(index == 0){
				 	        		  listOfObjects +=productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+qtyOpeningBalanceVal+'|'+batchNumber;
				 	        	  }else{
				 	        		  listOfObjects +='?'+productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+qtyOpeningBalanceVal+'|'+batchNumber;
				 	        	  }
				 	          }
						}
						  
					});
					if(listOfObjects != '') {
						$.post('accounts.json', "action="+action+'&listOfObjs='+listOfObjects+'&salesExecutive='+salesExecutive+'&previousClosingBal='+previousClosingBal+'&advance='+advance+'&openingBal='+openingBal+'&allotmentType='+allotmentType, function(obj){
							 $.loadAnimation.end();
							$('.accounts-page-container').load('accounts/sales-book/allot_stock.jsp');
						});
					}
				} else {
					 $.loadAnimation.end();
					showMessage({title:'Warning', msg:'Sales executive not available.'});
				    return false;
				}
			});
	
		
		}
	});
 },
 
 loadGrid: function(data) {
	 $('#products-search-results-list').html('');
	 if(data != undefined) {
			var alternate = false;
			var productDetailsTable='';
			 productDetailsTable +='<div class="report-header" style="width: 1033px; line-height:12px;">'+
			'<div class="report-header-column2 centered" style="width: 125px;">' + Msg.ACCOUNTS_PRODUCT_NAME + '</div>' +
			'<div class="report-header-column2 centered" style="width: 125px;">' + Msg.ACCOUNTS_BATCH_NUMBER + '</div>' +
			'<div class="report-header-column2 centered" style="width: 125px;">'+ Msg.ACCOUNTS_PREVIOUS_CLOSING_STOCK +'</div>'+
			'<div class="report-header-column2 centered" style="width: 125px;">'+ Msg.ACCOUNTS_AVAILABLE_QUANTITY +'</div>'+
			'<div class="report-header-column2 centered quantityAlloted" style="width: 125px;">'+ Msg.ACCOUNTS_ALLOTMENT +'</div>'+
			'<div class="report-header-column2 centered" style="width: 125px;">'+ Msg.ACCOUNTS_OPENING_STOCK +'</div>'+
			'<div class="report-header-column2 centered" style="width: 125px;">'+ Msg.ACCOUNTS_REMARKS +'</div>'+
			'<div class="report-header-column2 centered" style="width: 15px;"></div>'+
			'</div>' +
			'</div>';
			$('#products-search-results-list').append(productDetailsTable);
			$('#products-search-results-list').append('<div class="grid-content" style="height:133px; width: 1034px; overflow-y:initial;"></div>'); 
		   var count=1;
			for(var loop=0;loop<data.length;loop=loop+1){
			if(data[loop].qtyAllotted == undefined) {
				data[loop].qtyAllotted="";
			}
			if(data[loop].remarks == undefined) {
				data[loop].remarks="";
			}
			var productDetailsTableRows = '';
			if(alternate) {
				productDetailsTableRows += '<div id="product_result_row" class="product_row alternate">';
			} else {
				productDetailsTableRows += '<div id="product_result_row" class="product_row">';
			}
			alternate != alternate;
			var productDetailsTableRows = productDetailsTableRows+'<div id="allot-stock-grid" class="ui-content report-content">'+
			'<div class="report-body" id="row-'+SalesBookHandler.count+'" style="width: 1034px; height: auto; overflow: hidden; line-height:20px;">'+
			'<div id="productName" class="report-body-column2 centered sameHeight" style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
			'<div id="batchNumber" class="report-body-column2 centered sameHeight" style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].batchNumber  + '</div>' +
			'<div id="qtyClosingBalance" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 125px; word-wrap: break-word; text-align:right !important;">'+ currencyHandler.convertNumberToStringPattern(data[loop].previousQtyClosingStock) +'</div>'+
			'<div id="availableQuantity" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 125px; word-wrap: break-word; text-align:right !important;">'+ currencyHandler.convertNumberToStringPattern(data[loop].availableQuantity) +'</div>'+
			'<div class="report-body-column2 centered quantityAlloted sameHeight" style="height: inherit; width: 125px; word-wrap: break-word;">'+
			'<div class="input-field" id="quantity" style="margin-top:-5px;"><input name="quantityAllotted" class="quantityAllotedText" id="quantityAllotted" " style="width:125px; float:left; text-align:right !important;" value="'+ currencyHandler.convertNumberToStringPattern(data[loop].qtyAllotted) +'"></div></div>'+
			'<div class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 125px; word-wrap: break-word;">' +  '<input type = "text" id="qtyOpeningBalance"  class="qtyOpeningBalance right-aligned" size=6px style="border: none;margin-top:-3px; text-align:right !important;" readonly = "readonly" value="'+ currencyHandler.convertNumberToStringPattern(data[loop].qtyOpeningBalance) +'">'  + '</div>' +
			'<div id="stockRemarks"class="report-body-column2 centered sameHeight" style="height: inherit;width: 125px; border-right:1px solid #9BCCF2; word-wrap: break-word;">' +  '<textarea rows="2" cols="2"  style="height: 26px; width: 105px;border:none; resize: none;" id="remarks"  class="remarks">'+data[loop].remarks+'</textarea>' + '</div>' +
			'<div id="'+data[loop].id+'" class="edit-icon editAllotStock" title="Edit Allotment" style="margin-top:1px; display:none; float:right; margin-right:35px;"></div>' +
			'</div>'+
			'</div>';
			$('.grid-content').append(productDetailsTableRows);
			if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
				SalesBookHandler.checkGridLength($('row-'+SalesBookHandler.count));
		   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
			   SalesBookHandler.checkGridLength($('row-'+SalesBookHandler.count));
			}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
				SalesBookHandler.checkGridLength($('row-'+SalesBookHandler.count));
		   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
			   SalesBookHandler.checkGridLength($('row-'+SalesBookHandler.count));
           }
           else{
        	   SalesBookHandler.checkGridLength($('row-'+SalesBookHandler.count));
		   }
			SalesBookHandler.count=SalesBookHandler.count+1;
			}
		 $('.grid-content').jScrollPane({showArrows:true});
		 SalesBookHandler.editAllotStock();
		}
		else {
			$('#products-search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Products Available</div></div></div>');
		}
 },
 editAllotStock: function() {
		$('.editAllotStock').click(function() {
			var id = $(this).attr('id');
			if("undefined" != id) {
				$.post('accounts/sales-book/allot_stock_edit.jsp', 'id='+id, function(data){
						$('#allot-stock-edit-container').html(data);
						$("#allot-stock-edit-dialog").dialog({
			    			autoOpen: true,
			    			height: 560,
			    			width: 650,
			    			modal: true,
			    			buttons: {
			    			Update: function() {
			    				var thisButton = $(this);
			    				var qtyAllotted = $('.qtyAllotted').val();
			    				var qtyOpeningBalance = $('.qtyOpeningBal').val();
			    				var remarks = encodeURIComponent($(this).find('.remarks').val());
			    				 $.post('accounts.json', 'action=edit-allot-stock&id='+id+'&qtyAllotted='+qtyAllotted+'&qtyOpeningBalance='+qtyOpeningBalance+'&remarks='+remarks , function(obj) {
			    						$(".allot-stock-edit-dialog").dialog('close');
			    						window.location.reload();
			    					});
			    				},
			    		      Cancel: function() {
			    			      $(this).dialog('close');
			    			
			    		          }
			    			},
			    			close: function() {
			    				$('#allot-stock-edit-container').html('');
			    			}
			    		});
			        });
			}
		});
		/*
		$('#allot-stock').click(function() {
			$('.accounts-page-container').load('accounts/sales-book/allot_stock.jsp');
		});*/
 },
 setOpeningStock: function() {
	 $('.qtyAllotted').change(function() {
		 var id = $('#salesBookProductId').val();
		 $.post('accounts.json', 'action=get-recent-allotment&id='+id, function(obj) {
			 var previousAllotment = obj.result.data;
			 var currentAllotment = $('input#qtyAllotted').val();
			 var qtyOpeningBalance = $('input#qtyOpeningBal').val();
			 var sum;
			 var allotmentDiff;
			 if(parseInt(currentAllotment) < parseInt(previousAllotment)) {
				 allotmentDiff = parseInt(previousAllotment) - parseInt(currentAllotment);
				 sum = parseInt(qtyOpeningBalance) - allotmentDiff;
			 } else {
				 allotmentDiff = parseInt(currentAllotment) - parseInt(previousAllotment);
				 sum = parseInt(qtyOpeningBalance) + allotmentDiff;
			 }
			$('.qtyOpeningBal').val(sum);
		});
	});
 },
//End Of Allot Stock
	
validateAdvance : function(){
	var result = true;
	var advance =  currencyHandler.convertStringPatternToFloat($('#advance').val());
	if(advance > parseFloat(10000000000000) ||advance < 0){
		$('#advanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$('#advance').focus(function(){
			$('#advanceValid').empty();
			$('#advance_pop').show();
		});
		$('#advance').blur(function(){
			var advance =  currencyHandler.convertStringPatternToFloat($('#advance').val());
			$('#advance_pop').hide();
			if(advance > parseFloat(10000000000000)||advance < 0){
				$('#advanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#advance').focus(function(){
					$('#advanceValid').empty();
					$('#advance_pop').show();
				});
			}else{
				$('#advanceValid').empty();
			}
		});
		result = false;
	}
	return result;
},
		validateAllotStock : function(){
			var result=true;
			if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#advance').val()) == false || currencyHandler.convertStringPatternToFloat(($('#advance').val())).toString().length > 10){
				$('#advanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#advance").focus(function(event){
					$('#advanceValid').empty();
					 $('#advance_pop').show();
				});
				$("#advance").blur(function(event){
					 $('#advance_pop').empty();
					 if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#advance').val()) == false || currencyHandler.convertStringPatternToFloat(($('#advance').val())).toString().length > 10){
						 $('#advanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#advance_pop').show();
					 }else{
						 $('#advanceValid').empty();
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#openingBalance').val()) == false || currencyHandler.convertStringPatternToFloat(($('#openingBalance').val())).toString().length > 10){
				$('#openingBalanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#openingBalance").focus(function(event){
					$('#openingBalanceValid').empty();
					 $('#openingBalance_pop').show();
				});
				$("#openingBalance").blur(function(event){
					 $('#openingBalance_pop').empty();
					 if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#openingBalance').val()) == false || currencyHandler.convertStringPatternToFloat(($('#openingBalance').val())).toString().length > 10){
						 $('#openingBalanceValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#openingBalance_pop').show();
					 }else{
						 $('#openingBalanceValid').empty();
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			return result;
		},
		checkGridLength: function(group1){
			$('#row-'+SalesBookHandler.count).each(function(index) {
		        var maxHeight = 0;
		        $(this).children().each(function(index) {
		            if($(this).height() > maxHeight) 
		                maxHeight = $(this).height();
		        });
		        $(this).children().height(maxHeight);
		    });    
		},
};
