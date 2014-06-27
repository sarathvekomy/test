var SalesBookHandler = {
		theme: "",
		expanded: true,
		count: 0,
initPageLinks: function() {
$('#view-sales-returns').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/view-transactions/stock_returns.jsp'
});
$('#allot-stock').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/sales-book/allot_stock.jsp'
});
$('#view-allot-stock').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/view-transactions/sales_view.jsp'
});
$('#view-day-book').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/view-transactions/day_book_search.jsp'
});
$('#view-delivery-note').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/view-transactions/delivery_note_search.jsp'
});
$('#view-journal').pageLink({
	container : '.accounts-page-container',
	url : 'accounts/view-transactions/search_journals.jsp'
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

//common function for displaying search results
displayStockReturnsSearchResults:function(data){
	if(data!=null)  {
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
			
			'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST + ' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].totalCost) + '</span>' +
			'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
			'</div>' +
			'<div class="green-result-col-action">' + 
			'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales Returns"></div>';
			'</div>' +
			'</div>';
	$('#search-results-list').append(rowstr);
};
SalesBookHandler.initSearchStockReturnResultButtons();
$('#search-results-list').jScrollPane({showArrows:true});
	} else {
		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
	}
	$.loadAnimation.end();
	setTimeout(function(){
		$('#search-results-list').jScrollPane({showArrows:true});
	},0);
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
initSearchStockReturnOnLoad: function() {
	var paramString = $('#sales-return-search-form').serialize();
	$.post('salesReturn.json', paramString,
	function(obj){
		var data = obj.result.data;
		$('#search-results-list').html('');
		SalesBookHandler.displayStockReturnsSearchResults(data);
	}
	);
	},
	
	//End of Searchsalesreturn on load of sales return
	
	initSearchStockReturn : function(role) {
		//Stock search button click
		$('#action-search-sales-return').click(function() {
			var thisButton = $(this);
			var paramString = $('#sales-return-search-form').serialize();
			$('#search-results-list').ajaxLoader();
			$.post('salesReturn.json', paramString,
		        function(obj){
			    	var data = obj.result.data;
			    	//for refreshing input fields after search
			    	$('form').clearForm();
					$('#search-results-list').html('');
					SalesBookHandler.displayStockReturnsSearchResults(data);
					
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
							$('#search-results-list')
									.html(
											'<tr><td colspan="4">Search Results will be show here</td></tr>');
							setTimeout(function() {
								$('#search-results-list').jScrollPane({
									showArrows : true
								});
							}, 0);
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
	//End of initSearchStockReturn
	
	initSearchStockReturnResultButtons : function () {
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('accounts/view-transactions/stock_returns_view.jsp', 'id='+id,
		        function(data){
				$('#accounts-view-container').html(data);
					$("#accounts-view-dialog").dialog('open');
		        }
	        );
		});
		$("#accounts-view-dialog").dialog({
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
				$('#accounts-view-container').html('');
			}
		});

	},
	//End of initSearchStockReturnResultButtons for sales return	
	
	
	initSearchSalesOnLoad: function() {
		var paramString='action=search-sales-data-onload';
		$.post('accounts.json', paramString,
		function(obj){
			var data = obj.result.data;
			$('#search-results-list').html('');
			if(data!=null)  {
				var alternate = false;

				for(var loop=0;loop<data.length;loop=loop+1) {
					var value=data[loop].totalCost;
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1" style="width:300px;">'+
					'<div class="result-title">' + data[loop].salesExecutive  + '</div>' +
					'<div class="result-body">' +
					'</div>' +'<span class="property">'+Msg.SALES_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">'+Msg.SALES_OPENING_BALANCE_LABEL +'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].balanceOpening) + '</span>' +
					
					
					'</div>' +'<span class="property">'+Msg.SALES_CLOSING_BALANCE_LABEL +'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].balanceClosing) + '</span>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales"></div>';
					'</div>' +
					'</div>';
			$('#search-results-list').append(rowstr);
		};
		SalesBookHandler.initSearchSalesResultButtons();
		$('#search-results-list').jScrollPane({showArrows:true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
			$.loadAnimation.end();
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},0);
			
		}
		);
		},
	
		//End of initSearchSalesOnLoad for search sales 
initSearchSales: function(role) {
	//Sales search button click
	$('#action-search-sales').click(function() {
		var thisButton = $(this);
		var paramString = $('#sales-search-form').serialize();  
		$('#search-results-list').ajaxLoader();
		$.post('accounts.json', paramString,
	        function(obj){
		    	var data = obj.result.data;
				$('#search-results-list').html('');
				//for refreshing input fields after search
				//$('form').clearForm();
				if(data!=null)  {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						var value=data[loop].totalCost;
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate">';
						} else {
							rowstr = '<div class="green-result-row">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1" style="width:300px;">'+
						'<div class="result-title">' + data[loop].salesExecutive  + '</div>' +
						'<div class="result-body">' +
						'</div>' +'<span class="property">'+Msg.SALES_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+Msg.SALES_OPENING_BALANCE_LABEL +'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].balanceOpening) + '</span>' +
						
						
						'</div>' +'<span class="property">'+Msg.SALES_CLOSING_BALANCE_LABEL +'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].balanceClosing) + '</span>' +
						'</div>' +
						'<div class="green-result-col-action">' + 
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales"></div>';
						'</div>' +
						'</div>';
				$('#search-results-list').append(rowstr);
			};
			SalesBookHandler.initSearchSalesResultButtons();
			$('#search-results-list').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
				$.loadAnimation.end();
				setTimeout(function(){
					$('#search-results-list').jScrollPane({showArrows:true});
				},0);
				
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
	$('#action-clear').click(function() {
		$('form').clearForm();
		SalesBookHandler.initSearchSalesOnLoad();
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
//End of initSearchSales

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
	    $('#error-message').html('You will loose unsaved data.. Clear form?');   
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
				$('#previousClosingBalance').val(parseFloat(obj.result.data).toFixed(2));
			});
			$('#openingBalance').val("");
			$('#advance').val("");
			$('#button-save').show();
			$('#button-update').hide();
		}
		else{
			$.post('accounts.json','action=get-closing-balance&salesExecutiveVal='+salesExecutive, function(obj){
				$('#previousClosingBalance').val(parseFloat(obj.result.data).toFixed(2));
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
								htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
										+ data[loop].salesExecutive
										+ '</a></li>';
							}
							htmlStr += '</div>';
							suggestionsDiv.append(htmlStr);
						} else {
							suggestionsDiv.append('<div id="">'
									+ 'No SalesExecutive Names' + '</div>');
						}
						suggestionsDiv.css('left',
								thisInput.position().left);
						suggestionsDiv.css('top',
								thisInput.position().top + 25);
						suggestionsDiv.show();
						$('.select-teacher').click(function() {
									thisInput.val($(this).html());
									thisInput.attr('salesExecutive', $(this)
											.attr('salesExecutive'));
									$('#salesExecutive').attr('value',
											$(this).attr('salesExecutive'));
									suggestionsDiv.hide();
								});
					});
	$('#sales-executive-name-suggestions').click(function (){
		var salesExecutive = $('#salesExecutive').val();
		var paramString='action=show-hide-buttons';
		$.post('accounts.json', paramString+'&salesExecutive='+salesExecutive,function(obj){
			var data = obj.result.data;
			// For new sales executive.
			if(data == undefined){
				$('#previousClosingBalance').val("0.00");
				$('#closingBalance').val("0.00");
				$('#openingBalance').val("");
				$('#advance').val("0.00");
				$('#button-save').show();
				$('#button-update').hide();
				SalesBookHandler.load();
			}
			//For existing sales executive.
			else{
				$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].closingBalance).toFixed(2)));
				$('#previousClosingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].previousClosingBalance).toFixed(2)));
				$('#openingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].openingBalance).toFixed(2)));
				$('#advance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].advance).toFixed(2)));
				$('#allotmentType').val(data[0].allotmentType);
				$('#products-search-results-list').show();
				var data= obj.result.data;
				SalesBookHandler.loadGrid(data);
				var flag = data[0].flag;
				if(flag == '1') {
					$('#button-save').hide();
					$('#button-update').show();
				} else {click
					$('#button-save').show();
					$('#button-update').hide();
				}
				
			}
		});
	});
	$('#ps-exp-col').click(function() {
	    if(PageHandler.expanded) {
	    	$('.report-header').css( "width", "697px" );
			$('.report-body').css( "width", "697px" );
			$('.report-header-column2').css( "width", "90px" );
			$('.report-body-column2').css( "width", "90px" );
			$('.quantityAlloted').css( "width", "80px" );
			$('.quantityAllotedText').css( "width", "80px" );
			$('#products-search-results-list').css("width", "697px");
			$('.grid-content' ).css( "width", "697px" );
			$('.jScrollPaneContainer').css("width","697px");
		} else {
			$('.report-header').css( "width", "830px" );
			$('.report-body').css( "width", "830px" );
			$('.report-header-column2').css( "width", "100px" );
			$('.report-body-column2').css( "width", "100px" );
			$('.quantityAlloted').css( "width", "90px" );
			$('.quantityAllotedText').css( "width", "90px" );
			$('#products-search-results-list').css("width", "830px");
			$('.grid-content' ).css( "width", "830px" );
			$('.jScrollPaneContainer').css("width","830px");
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
		//$.post('accounts.json', 'action=get-previous-opening-balance&salesExecutive='+salesExecutive, function(obj) {
			//var previousOpeningBal = obj.result.data;
			var advance = currencyHandler.convertStringPatternToFloat($('#advance').val());
	   		var formatAdvance=currencyHandler.convertFloatToStringPattern(advance.toFixed(2));
	   		$('#advance').val(formatAdvance);
	   		var previousClosingBalance = currencyHandler.convertStringPatternToFloat($('#previousClosingBalance').val());
	   		var openingBalanceVal = parseFloat(advance) + parseFloat(previousClosingBalance);
	   		if(isNaN(openingBalanceVal)) {
	   			openingBalanceVal = 0.00;
	   		}
	   		$('#openingBalance').val(currencyHandler.convertFloatToStringPattern(openingBalanceVal.toFixed(2)));
		//});
   	});
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
	$('#stock-returns').click(function(){
	});
	$('#view-sales').click(function(){
	});
	$('.grid-content').live("blur",function(){
		 $.each($('.report-body'), function(index, value) {
			  var availableQtyVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
			  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
	          var quantityAllotted = currencyHandler.convertStringPatternToNumber($(this).find('#quantityAllotted').val());
	          //validating the allotted quantity with available quantity.
	          if(parseInt(quantityAllotted) > parseInt(availableQtyVal)) {
	         	  	showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
	         	  	return;
	           }
	          var sum= parseInt(qtyClosingBalance) + parseInt(quantityAllotted);
	          if(isNaN(sum)) {
	        	  sum = "0";
	          } 
	        $(this).find('#qtyOpeningBalance').val(currencyHandler.convertFloatToStringPattern(sum));
	        $(this).find('#quantityAllotted').val(currencyHandler.convertFloatToStringPattern(quantityAllotted));
		 });
	});
	$('#button-save').click(function() {
		//if($('form').validate() == false) return;
		var resultSuccess=true;
		var resultFailure=false;
		if(SalesBookHandler.validateAllotStock()==false){
			return resultSuccess;
		}else{
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
				$('div#allot-stock-grid').each(function(index, value) {
					  var id = $(this).find('div').attr('id');
					  var availableQtyVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
					  var qtyAllotted = currencyHandler.convertStringPatternToNumber($(this).find('#quantityAllotted').val());
					  if(parseInt(quantityAllotted) > parseInt(availableQtyVal)) {
			         	  	showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
			         	  	return;
			           }
					  var qtyOpeningBalanceVal = currencyHandler.convertStringPatternToFloat($(this).find('#qtyOpeningBalance').val());
					  var remarks = $(this).find('.remarks').val();
					  var productName = $(this).find('#productName').html();
					  var batchNumber = $(this).find('#batchNumber').html();
					  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
					  if(qtyOpeningBalanceVal != 0) {
			        	  if(index == 0){
			        		  listOfObjects +=productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+batchNumber;
			        	  }else{
			        		  listOfObjects +=','+productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+batchNumber;
			        	  }
			          }
				});
				$.post('accounts.json', "action="+action+'&listOfObjs='+listOfObjects+'&salesExecutive='+salesExecutive+'&previousClosingBal='+previousClosingBal+'&advance='+advance+'&openingBal='+openingBal+'&allotmentType='+allotmentType, function(obj){		
						$('.accounts-page-container').load('accounts/sales-book/allot_stock.jsp');
				  });
			} else {
				showMessage({title:'Warning', msg:'Sales executive not available.'});
			    return false;
			}
			
		});
		}
	});
	$('#button-update').click(function() {
		//if($('form').validate() == false) return;
		var resultSuccess=true;
		var resultFailure=false;
		if(SalesBookHandler.validateAllotStock()==false){
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
						  if(parseInt(qtyAllotted) > parseInt(availableQtyVal)) {
				         	  	showMessage({title:'Warning', msg:'Please Enter valid Quantity.'});
				         	  	return false;
				           } else {
				        	  var remarks = $(this).find('.remarks').val();
				 			  var productName = $(this).find('#productName').html();
				 			  var batchNumber = $(this).find('#batchNumber').html();
				 			  var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($(this).find('#qtyClosingBalance').html());
				 			  var availableQuantityVal = currencyHandler.convertStringPatternToNumber($(this).find('#availableQuantity').html());
				 			  var qtyOpeningBalanceVal = $(this).find('#qtyOpeningBalance').val();
				 			  if(qtyOpeningBalanceVal != 0) {
				 	        	  if(index == 0){
				 	        		  listOfObjects +=productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+batchNumber;
				 	        	  }else{
				 	        		  listOfObjects +=','+productName+'|'+remarks+'|'+qtyClosingBalance+'|'+qtyAllotted+'|'+batchNumber;
				 	        	  }
				 	          }
						}
						  
					});
					if(listOfObjects != '') {
						$.post('accounts.json', "action="+action+'&listOfObjs='+listOfObjects+'&salesExecutive='+salesExecutive+'&previousClosingBal='+previousClosingBal+'&advance='+advance+'&openingBal='+openingBal+'&allotmentType='+allotmentType, function(obj){		
							$('.accounts-page-container').load('accounts/sales-book/allot_stock.jsp');
						});
					}
				} else {
					showMessage({title:'Warning', msg:'Sales executive not available.'});
				    return false;
				}
			});
	
		
		}
	});
 },
 
 loadGrid: function(data) {
	 $('#products-search-results-list').html('');
	 if(data.length>0) {
			var alternate = false;
			var productDetailsTable='';
			 productDetailsTable +='<div class="report-header" style="width: 830px; height: 45px;">'+
			'<div class="report-header-column2 centered" style="width: 78px;">' + Msg.ACCOUNTS_PRODUCT_NAME + '</div>' +
			'<div class="report-header-column2 centered" style="width: 78px;">' + Msg.ACCOUNTS_BATCH_NUMBER + '</div>' +
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_AVAILABLE_QUANTITY +'</div>'+
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_PREVIOUS_CLOSING_STOCK +'</div>'+
			'<div class="report-header-column2 centered quantityAlloted" style="width: 78px;">'+ Msg.ACCOUNTS_ALLOTMENT +'</div>'+
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_OPENING_STOCK +'</div>'+			
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_RETURN_QTY +'</div>'+
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_CLOSING_STOCK +'</div>'+
			'<div class="report-header-column2 centered" style="width: 78px;">'+ Msg.ACCOUNTS_REMARKS +'</div>'+
			//'<div class="report-header-column2 centered" style="width: 15px;"></div>'+
			'</div>' +
			'</div>';
			$('#products-search-results-list').append(productDetailsTable);
			$('#products-search-results-list').append('<div class="grid-content" style="height:133px; width: 830px; overflow-y:initial;"></div>'); 
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
			'<div class="report-body" id="row-'+SalesBookHandler.count+'" style="width: 830px; height: auto; overflow: hidden;">'+
			'<div id="productName" class="report-body-column2 centered sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
			'<div id="batchNumber" class="report-body-column2 centered sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">' +  data[loop].batchNumber  + '</div>' +
			'<div id="availableQuantity" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">'+ currencyHandler.convertNumberToStringPattern(data[loop].availableQuantity) +'</div>'+
			'<div id="qtyClosingBalance" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">'+ currencyHandler.convertNumberToStringPattern(data[loop].previousQtyClosingStock) +'</div>'+
			'<div class="report-body-column2 centered quantityAlloted sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">'+
			'<div class="input-field" id="quantity" style="margin-top:-5px;"><input name="quantityAllotted" class="quantityAllotedText" id="quantityAllotted" " style="width:78px; float:left;" value="'+ currencyHandler.convertNumberToStringPattern(data[loop].qtyAllotted) +'"></div></div>'+
			'<div class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">' +  '<input type = "text" id="qtyOpeningBalance"  class="qtyOpeningBalance right-aligned" size=8px style="border: none;margin-top:-3px; " readonly = "readonly" value="'+ currencyHandler.convertNumberToStringPattern(data[loop].qtyOpeningBalance) +'">'  + '</div>' +
			'<div id="returnQty" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">'+ currencyHandler.convertNumberToStringPattern(data[loop].returnQty) +'</div>'+
			'<div id="closingStock" class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 78px; word-wrap: break-word;">'+ currencyHandler.convertNumberToStringPattern(data[loop].closingStock) +'</div>'+
			'<div id="stockRemarks"class="report-body-column2 centered sameHeight" style="height: inherit;width: 78px; word-wrap: break-word;">' +  '<textarea rows="2" cols="2"  style="height: 26px; width: 78px; border: none; resize: none;" id="remarks"  class="remarks">'+data[loop].remarks+'</textarea>' + '</div>' +
			//'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Allotment" style="margin-top:1px;"></div>'
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
		$('.ui-btn').click(function() {
			var id = $(this).attr('id');
			$.post('accounts/sales-book/allot_stock_edit.jsp', 'id='+id,
		        function(data){
					$('#allot-stock-edit-container').html(data);
					$("#allot-stock-edit-dialog").dialog({
		    			autoOpen: true,
		    			height: 560,
		    			width: 650,
		    			modal: true,
		    			buttons: {
		    			Update: function() {
		    				var thisButton = $(this);
		    				var paramString = $('#allot-stock-edit-form').serialize();
		    				 $.post('accounts.json', paramString,
		    						 function(obj) {
		    						$(this).successMessage({
		    							container : '.product-page-container',
		    							data : obj.result.message
		    						});
		    						$("#allot-stock-edit-dialog").dialog('close');
		    					});
		    				},
		    		      Cancel: function() {
		    			      $(this).dialog('close');
		    			
		    		          }
		    			},
		    			close: function() {
		    				$('#product-view-container').html('');
		    			}
		    		});
		        }
	        );
		});
 },
 setOpeningStock: function() {
	 $('#qtyAllotted').change(function() {
			$('#qtyOpeningBalance').val('');
			var qtyAllotted = currencyHandler.convertStringPatternToNumber($('#qtyAllotted').val());
			var qtyClosingBalance = currencyHandler.convertStringPatternToNumber($('#qtyClosingBalance').val());
			var sum = parseInt(qtyAllotted) + parseInt(qtyClosingBalance);
			$('.qtyOpeningBalance').val(sum);
		});
},
 loadGridByDate: function() {
	 $('#createdOn').change(function() {
		 var selectedDate = $('#createdOn').val();
		 var todayDate = $.datepicker.formatDate('dd-mm-yy', new Date());
		 if(selectedDate < todayDate) {
			 var salesExecName = $('#salesExecutive').val();
			 $.post('accounts.json',"action=get-grid-by-date&salesExecName="+salesExecName+'&selectedDate='+selectedDate,function(obj) {
				 var data= obj.result.data;
				 $('#previousClosingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].previousClosingBalance).toFixed(2)));
				 $('#closingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].closingBalance).toFixed(2)));
				 $('#openingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].openingBalance).toFixed(2)));
				 $('#advance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].advance).toFixed(2)));
				 $('#allotmentType').val(data[0].allotmentType);
				 $('#products-search-results-list').show();
				 SalesBookHandler.loadGrid(data);
				 $('#openingBalance').attr('class','read-only');
				 $('#advance').attr('class','read-only');
				 $('#button-save').hide();
				 $('#button-update').hide();
				 $('#action-clear').hide();
				 $('#action-cancel').hide();
				 
			 });
		 } else {
			 var salesExecutive = $('#salesExecutive').val();
			 var paramString='action=show-hide-buttons';
			 $.post('accounts.json', paramString+'&salesExecutive='+salesExecutive,function(obj){
				var data = obj.result.data;
				$('#closingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].closingBalance).toFixed(2)));
				$('#previousClosingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].previousClosingBalance).toFixed(2)));
				$('#openingBalance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].openingBalance).toFixed(2)));
				$('#advance').val(currencyHandler.convertFloatToStringPattern(parseFloat(data[0].advance).toFixed(2)));
				$('#allotmentType').val(data[0].allotmentType);
				$('#products-search-results-list').show();
				SalesBookHandler.loadGrid(data);
				$('#openingBalance').removeAttr('class');
				 $('#advance').removeAttr('class');
				 $('#openingBalance').attr('class','mandatory' );
				 $('#advance').attr('class', 'mandatory');
				$('#button-save').hide();
				$('#button-update').show();
				$('#action-clear').show();
				$('#action-cancel').show();
			 });
		 }
	 });
	 
 },
//End Of Allot Stock
	

initSearchSalesResultButtons : function () {
	$('.btn-view').click(function() {
		var id = $(this).attr('id');
		$.post('accounts/view-transactions/sales_preview.jsp', 'id='+id,
	        function(data){
			$('#accounts-view-container-sales').html(data);
				$("#accounts-view-dialog-sales").dialog('open');
	        }
        );
	});
	$("#accounts-view-dialog-sales").dialog({
		autoOpen: false,
		height: 500,
		width: 1145,
		modal: true,
		buttons: {
			Close: function() {
				$(this).dialog('close');
			}
		},
		close: function() {
			$('#accounts-view-container-sales').html('');
		}
	});

},
//End of initSearchSalesResultButtons for sales returns

displaySearchResults:function(data){
	if(data!=null)  {
		var alternate = false;
		for(var loop=0;loop<data.length;loop=loop+1) {
			var dateFormat=SalesBookHandler.formatDate(data[loop].createdOn);
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
SalesBookHandler.initDefaultResultButtons();
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
	       }, 300);
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
				SalesBookHandler.displaySearchResults(data);
				
	        });
	});
},
initSearchDayBookOnLoad: function() {
	$('#search-results-list').ajaxLoader();
	var paramString = $('#day-book-search-form').serialize();
	$.post('dayBook.json', paramString,
	function(obj){
		var data = obj.result.data;
		$('#search-results-list').html('');
		SalesBookHandler.displaySearchResults(data);
	}
	);
	},
//Function to format date to DD/MM/YYYY
formatDate:function(inputFormat){
	var str=inputFormat.split(/[" "]/);
	dt=new Date(str[0]);
	return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
},
//end of function
initDefaultResultButtons:function(){
	
	$('.btn-view').click(function() {
		var id = $(this).attr('id');
		$.post('accounts/view-transactions/day_book_view.jsp', 'id='+id,
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
//Deliverynote starts
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
    SalesBookHandler.displayDeliveryNoteSearchResults(data);
    
         }
     );
 });
},
initSearchDeliveryNoteOnLoad : function() {
	  var paramString = $('#delivery-note-search-form').serialize();  
	  $('#search-results-list').ajaxLoader();
	  $.post('deliveryNote.json', paramString,
	         function(obj){
	       var data = obj.result.data;
	    $('#search-results-list').html('');
	    //for refreshing input fields after search
	    $('form').clearForm();
	    SalesBookHandler.displayDeliveryNoteSearchResults(data);
	    
	         }
	     );
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
				'</div>'+'<span class="property">'+Msg.DELIVERY_NOTE_BALANCE_LABEL +'</span><span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+':'+'</span>'+'<span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].balance) + '</span>' +
				'</div>' +
				'<div class="green-result-col-action">' + 
				'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Delivery Note"></div>';
				'</div>' +
				'</div>';
		$('#search-results-list').append(rowstr);
	};
	SalesBookHandler.initDeliveryNoteResultButtons();
	   $('#search-results-list').jScrollPane({showArrows:true});
	    } else {
	     $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
	    }
	    $.loadAnimation.end();
	    setTimeout(function(){
	     $('#search-results-list').jScrollPane({showArrows:true});
	    },300);
	    
},
initDeliveryNoteResultButtons : function() {
	  $('.btn-view')
	    .click(
	      function() {
	       var id = $(this).attr('id');
	       $
	         .post(
	           'accounts/view-transactions/delivery_note_view.jsp',
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

	 },
	 checkLength: function(len,number,bonus) {
			if(len>25||bonus>25){
				$('#row-'+number).css({'height' : '30px'});
				if(len>=45||bonus>=45){
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
		checkLengthForDeliveryNote: function(len,number,bonus,batchNo) {
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
		checkLengthForAllotment: function(len,number,batchNo) {
			if(len>20||batchNo>15){
				$('#row-'+number).css({'height' : '30px'});
				$('.invoice-boxes-'+number).css({'height' : 'inherit'});
				if(len>=40||batchNo>25){
					$('#row-'+number).css({'height' : '40px'});
				}
				if(len>=55){
					$('#row-'+number).css({'height' : '60px'});
				}
				if(len>=70){
					$('#row-'+number).css({'height' : '70px'});
				}
					
			}
			
			
		},
		checkLengthForSalesReturns: function(len,number) {
			$('.invoice-boxes-'+number).css({'height' : 'inherit'});
			if(len>45){
				$('#row-'+number).css({'height' : '35px'});
			}
			else
			$('#row-'+number).css({'height' : '40px'});
			
		},
		expandByLength:function(len,number){
			if(len>22){
				$('#row-'+number).css({'height' : '30px'});
				$('.invoice-boxes-'+number).css({'height' : 'inherit'});
			if(len>40){
				$('#row-'+number).css({'height' : '40px;'});
			}
			if(len>60){
				$('#row-'+number).css({'height' : '50px;'});
			}
			}
		},
      checkAllotStockGridLength: function(count,productNameLength,batchNumberLength,remarksLength){
    	  if(productNameLength>15||batchNumberLength>15||remarksLength>15){
				$('#row-'+count).css({'height' : '30px'});
				/*$('.invoice-boxes-'+number).css({'height' : 'inherit'});*/
			
				if(productNameLength>=45||batchNumberLength>=45||remarksLength>28){
					$('#row-'+count).css({'height' : '45px'});
				}
				if(remarksLength>=55){
					$('#row-'+count).css({'height' : '60px'});
				}
				if(remarksLength>=70){
					$('#row-'+count).css({'height' : '70px'});
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
		searchJournalOnLoad:function(){
			 var paramString = $('#journal-search-form').serialize(); 
		    $.post(
		      'journal.json',paramString,
		      function(obj) {
		       var data = obj.result.data;
		       $('#default-results-list').html('');
		       SalesBookHandler.displayJournalSearchResults(data);

		 });

		},
		 searchJournal : function() {
			  $('#action-search-journals').click(function() {
			   var thisButton = $(this);
			   var paramString = $('#journal-search-form').serialize();  
			   $.post('journal.json', paramString,
			          function(obj){
			        var data = obj.result.data;
			        $('#search-results-list').html('');
			        SalesBookHandler.displayJournalSearchResults(data);
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
		        	var dateFormat=SalesBookHandler.formatDate(data[loop].createdOn);
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
		SalesBookHandler.initDefaultResultButtons();
		        $('#search-results-list').jScrollPane({
					showArrows : true
				});
		       } else {
		        $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
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
		           'accounts/view-transactions/journals_view.jsp',
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
		   height : 400,
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
	},
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
};
