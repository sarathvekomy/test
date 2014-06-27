var ProductHandler = {
		expanded: true,
		pageNumber: 1,
		numberOfPages: 1,
		theme: "",
		count: 0,
		result:false,
	initPageLinks : function() {
		$('#product-list').pageLink({
			container : '.product-page-container',
			url : 'product/product_search.jsp'
		});
		$('#product-import').pageLink({
			container : '.product-page-container',
			url : 'product/product_import.jsp'
		});
			$('#product-transaction').pageLink({
				container : '.product-page-container',
				url : 'product/product_transaction.jsp'
			});
		
	},
	showTransactionsOnLoad:function(){
		if(PageHandler.expanded){
			$('.page-link-strip').hide();
			$('.module-title').hide();
			$('.page-selection').animate( { width:"55px"}, 0,function(){});
			$('.page-container').animate( { width:"835px"}, 0);
			var thisTheme = PageHandler.theme;
			$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-right.jpg'));
			pageSelctionButton.attr('src', thisTheme+'/button-right.jpg');
			$('.jScrollPaneContainer').css("width","830px");
			$('#search-results-list').css("width","830px");
		}
		
		$.post('product.json', 'action=get-records-count', function(obj){
			var data1 = obj.result.data;
			var numberOfPages=(data1/100)+0.5;
			ProductHandler.numberOfPages=Math.round(numberOfPages);
			var productName=$('#productName').val();
			var batchNumber=$('#batchNumber').val();
			if($('#type').val()=='Select'){
				var transactionType='';
			}else{
				var transactionType=$('#type').val();
			}
			var date=$('#date').val();
		$.post('product.json', 'action=show-product-history&pageNumber='+ProductHandler.pageNumber+'&productName='+productName+'&batchNumber='+batchNumber+'&transactionType='+transactionType+'&createdOn='+date, function(obj){
	    	var data = obj.result.data;
	    	$('#search-results-list').html('');
			if(data == undefined){
	    		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No product transaction found</div></div></div>');
	    	}
			if(data.length>0) {
				    var productTransactionTable="";
		             productTransactionTable +='<div class="report-header" style="width: 830px; height: 30px;">'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 124px;">' + Msg.PRODUCT_PRODUCT_NAME + '</div>' +
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+ Msg.PRODUCT_BATCH_NUMBER +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+  Msg.PRODUCT_SALES_EXECUTIVE +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+  Msg.PRODUCT_CREATED_DATE_LABEL +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+ Msg.PRODUCT_QUANTITY +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px; line-height:12px; ">'+ Msg.PRODUCT_QUANTITY_TYPE +'</div>'+
					'</div>';
		             $('#search-results-list').append(productTransactionTable);
		             $('#search-results-list').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
						for(var loop=0;loop<data.length;loop=loop+1) {
							var dateFormat=ProductHandler.formatDate(data[loop].createdOn);
							if(data[loop].salesExecutive == undefined){
								 var productTransactionTableRows ='<div class="ui-content report-content">'+
								 '<div class="report-body" id="row-'+ProductHandler.count+'" style="width: 830px;height: auto; overflow : hidden;">'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].batchNumber +'</div>'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].createdBy +'</div>'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ dateFormat +'</div>'+
									'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word; text-align: right;">'+ currencyHandler.convertNumberToStringPattern(data[loop].quantity) +'</div>'+
									'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].quantityType +'</div>'+
									'</div>'+
									'</div>';
									$('.grid-content').append(productTransactionTableRows);
									if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
										ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
										   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
											ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
										   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
							           }
							           else{
							        	   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }
									ProductHandler.count=ProductHandler.count+1;
							}else{
								var productTransactionTableRows ='<div class="ui-content report-content">'+
								 '<div class="report-body" id="row-'+ProductHandler.count+'" style="width: 830px;height: auto; overflow: hidden;">'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].batchNumber +'</div>'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].salesExecutive+'</div>'+
									'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+  dateFormat +'</div>'+
									'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word; text-align: right;">'+ currencyHandler.convertNumberToStringPattern(data[loop].quantity) +'</div>'+
									'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].quantityType +'</div>'+
									'</div>'+
									'</div>';
									$('.grid-content').append(productTransactionTableRows);
									if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
										ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
										   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
											ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
										   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
							           }
							           else{
							        	   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
									   }
									ProductHandler.count=ProductHandler.count+1;
							}
						};
						 var productTransactionAction="";
			             productTransactionAction +='<div class="report-header" style="width: 830px; height: 30px;">';
			             if(ProductHandler.pageNumber==1){
			             productTransactionAction +='<div firstPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px; pointer-events: none;"  title="Click First"></div>';
			             }else{
			            	 productTransactionAction +='<div firstPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px;"  title="Click First"></div>'; 
			             }
			             if(ProductHandler.pageNumber==1){
			            	 productTransactionAction +='<div previousPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none; pointer-events: none;"  title="Click Previous"></div>';	 
			             }else{
			            	 productTransactionAction +='<div previousPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none;"  title="Click Previous"></div>';
			             }
			             productTransactionAction += '<div id="current-page-number" class="input-field"><input name="pageNumber" id="pageNumber" value="'+ ProductHandler.pageNumber +'" style="width: 30px; margin-left: 40px; padding-left: 20px;"> of '+ ProductHandler.numberOfPages +'</div>';
			            if(ProductHandler.pageNumber==ProductHandler.numberOfPages){
			            	 productTransactionAction +='<div nextPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Next"></div>';
			            }else{
			            	 productTransactionAction +='<div nextPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none"  title="Click Next"></div>';
			            }
			            if(ProductHandler.pageNumber==ProductHandler.numberOfPages){
			             productTransactionAction +='<div lastPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Last"></div>';
			            }else{
			            	productTransactionAction +='<div lastPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none"  title="Click Last"></div>';
			            }
			             productTransactionAction +='</div>';
			            
			             $('#search-results-list').append(productTransactionAction);
			            $('.grid-content').jScrollPane({showArrows:true});
						//$('#search-results-list').jScrollPane({showArrows:false});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No product transaction found</div></div></div>');
			}
			ProductHandler.initGridActionButtons();
		  });
		});
	},
	
	showTransactions:function(){
		$('#action-search-product-transactions').click(function(){
			if(ProductHandler.result !== true){
				ProductHandler.pageNumber=1;
			}
			var paramString=$('#product-transaction-search-form').serialize();
			var productName=$('#productName').val();
			var batchNumber=$('#batchNumber').val();
			if($('#type').val()=='Select'){
				var transactionType='';
			}else{
				var transactionType=$('#type').val();
			}
			var date=$('#date').val();
			$.post('product.json', 'action=get-records-count-for-criteria&productName='+productName+'&batchNumber='+batchNumber+'&transactionType='+transactionType+'&createdOn='+date, function(obj){
				var data1 = obj.result.data;
				var numberOfPages=(data1/100)+0.5;
				ProductHandler.numberOfPages=Math.round(numberOfPages);
			$.post('product.json', 'action=show-product-history&pageNumber='+ProductHandler.pageNumber+'&productName='+productName+'&batchNumber='+batchNumber+'&transactionType='+transactionType+'&createdOn='+date, function(obj){
				var data = obj.result.data;
		    	$('#search-results-list').html('');
				if(data == undefined){
		    		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No product transaction found</div></div></div>');
		    	}
				if(data.length>0) {
					    var productTransactionTable="";
			             productTransactionTable +='<div class="report-header" style="width: 830px; height: 30px;">'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 124px;">' + Msg.PRODUCT_PRODUCT_NAME + '</div>' +
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+ Msg.PRODUCT_BATCH_NUMBER +'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+  Msg.PRODUCT_SALES_EXECUTIVE +'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+  Msg.PRODUCT_CREATED_DATE_LABEL +'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px;">'+ Msg.PRODUCT_QUANTITY +'</div>'+
						'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 125px; line-height:12px; ">'+ Msg.PRODUCT_QUANTITY_TYPE +'</div>'+
						'</div>';
			             $('#search-results-list').append(productTransactionTable);
			             $('#search-results-list').append('<div class="grid-content" style="height:242px;width: 830px; overflow-y:initial;"></div>'); 
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=ProductHandler.formatDate(data[loop].createdOn);
								if(data[loop].salesExecutive == undefined){
									 var productTransactionTableRows ='<div class="ui-content report-content">'+
									 '<div class="report-body" id="row-'+ProductHandler.count+'" style="width: 830px;height: auto; overflow : hidden;">'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].batchNumber +'</div>'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].createdBy +'</div>'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ dateFormat +'</div>'+
										'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word; text-align: right;">'+ currencyHandler.convertNumberToStringPattern(data[loop].quantity) +'</div>'+
										'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].quantityType +'</div>'+
										'</div>'+
										'</div>';
										$('.grid-content').append(productTransactionTableRows);
										if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
											ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
											   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
											}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
												ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
											   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
								           }
								           else{
								        	   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }
										ProductHandler.count=ProductHandler.count+1;
								}else{
									var productTransactionTableRows ='<div class="ui-content report-content">'+
									 '<div class="report-body" id="row-'+ProductHandler.count+'" style="width: 830px;height: auto; overflow: hidden;">'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].batchNumber +'</div>'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].salesExecutive+'</div>'+
										'<div class="report-body-column2 centered report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+  dateFormat +'</div>'+
										'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;text-align: right;">'+ currencyHandler.convertNumberToStringPattern(data[loop].quantity) +'</div>'+
										'<div class="report-body-column2 right-aligned report-body-transaction-column2 " style="height: inherit; width: 125px; word-wrap: break-word;">'+ data[loop].quantityType +'</div>'+
										'</div>'+
										'</div>';
										$('.grid-content').append(productTransactionTableRows);
										if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
											ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
											   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
											}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
												ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
											   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
								           }
								           else{
								        	   ProductHandler.checkGridLength($('row-'+ProductHandler.count));
										   }
										ProductHandler.count=ProductHandler.count+1;
								}
							};
							 var productTransactionAction="";
				             productTransactionAction +='<div class="report-header" style="width: 830px; height: 30px;">';
				             if(ProductHandler.pageNumber==1){
				             productTransactionAction +='<div firstPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px; pointer-events: none;"  title="Click First"></div>';
				             }else{
				            	 productTransactionAction +='<div firstPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px;"  title="Click First"></div>'; 
				             }
				             if(ProductHandler.pageNumber==1){
				            	 productTransactionAction +='<div previousPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none; pointer-events: none;"  title="Click Previous"></div>';	 
				             }else{
				            	 productTransactionAction +='<div previousPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none;"  title="Click Previous"></div>';
				             }
				             productTransactionAction += '<div id="current-page-number" class="input-field"><input name="pageNumber" id="pageNumber" value="'+ ProductHandler.pageNumber +'" style="width: 30px; margin-left: 40px; padding-left: 20px;"> of '+ ProductHandler.numberOfPages +'</div>';
				            if(ProductHandler.pageNumber==ProductHandler.numberOfPages){
				            	 productTransactionAction +='<div nextPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Next"></div>';
				            }else{
				            	 productTransactionAction +='<div nextPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none"  title="Click Next"></div>';
				            }
				            if(ProductHandler.pageNumber==ProductHandler.numberOfPages){
				             productTransactionAction +='<div lastPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Last"></div>';
				            }else{
				            	productTransactionAction +='<div lastPageNumber="'+ProductHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none"  title="Click Last"></div>';
				            }
				             productTransactionAction +='</div>';
				            
				             $('#search-results-list').append(productTransactionAction);
				            $('.grid-content').jScrollPane({showArrows:true});
							//$('#search-results-list').jScrollPane({showArrows:false});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No product transaction found</div></div></div>');
				}
				ProductHandler.initGridActionButtons();
			});
			
			});
			ProductHandler.result=false;
		});
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    /*
				 * else if (type == 'checkbox' || type == 'radio') this.checked
				 * =true;
				 */
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			};
		$('#action-transaction-clear').click(function(){
			 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('form').clear();
							$(this).dialog("close");
						},
						Cancel : function(){
							$(this).dialog('close');
						}
					}
				});
			$('#action-search-product-list').click();
		});
	},
	initGridActionButtons:function(){
		$('.last-btn').click(function() {
				$.post('product.json', 'action=get-records-count',
						 function(obj){
					var data = obj.result.data;
					var numberOfPages=(data/100)+0.5;
					ProductHandler.pageNumber=Math.round(numberOfPages);
					ProductHandler.result=true;
					$("#action-search-product-transactions").trigger("click");
				});
			});
		$('.first-btn').click(function() {
				var pageNumber = $(this).attr('firstPageNumber');
				var firstPageNumber=parseInt(pageNumber-(pageNumber-1));
				ProductHandler.pageNumber=firstPageNumber;
				ProductHandler.result=true;
				//$('#action-add-product-transaction').click();
				$("#action-search-product-transactions").trigger("click");
			});
		$('.next-btn').click(function(){
			var pageNumber = $(this).attr('nextPageNumber');
			var nextPageNumber=parseInt(pageNumber)+1;
			ProductHandler.pageNumber=nextPageNumber;
			ProductHandler.result=true;
			$("#action-search-product-transactions").trigger("click");
			
		});
		$('.previous-btn').click(function(){
			var pageNumber = $(this).attr('previousPageNumber');
			var prevPageNumber=pageNumber-1;
			ProductHandler.pageNumber=prevPageNumber;
			ProductHandler.result=true;
			$("#action-search-product-transactions").trigger("click");
		});
		$('#pageNumber').live("keypress", function(e) {
	        if (e.keyCode == 13) {
	            var currentPageNumber=$('#pageNumber').val();
	            if(currentPageNumber > ProductHandler.numberOfPages || currentPageNumber < 1 ){
	            	showMessage({title:'Warning', msg:"please enter page number between"+ " "+ 1 +" "+ "and" + " "+ ProductHandler.numberOfPages +"."});
	            	$('#pageNumber').val(1); 
	            	event.preventDefault();
				       return;
	            }else{
	            ProductHandler.pageNumber=currentPageNumber;
	            $("#action-search-product-transactions").trigger("click");
	            }
	        }
	  });
		},
		
	showPageSelection: function(){
		$('.page-selection').animate( { width:"183px"}, 0,function(){
			$('.page-link-strip').show();
			$('.module-title').show();
		});
		$('.page-container').animate( { width:"702px"}, 0);
	},
	importproduct: function(){
		var thisButton = $(this);
		$('#product-upload-form').submit(function(event) {
			   var file = $('#fileProduct').val();       

			   if ( ! file) {
			       showMessage({title:'Warning', msg:'Please choose a file'});
			       event.preventDefault();
			       return;
			   } 

			   if ( ! file.match(/\.(xls)$/)) {
			       showMessage({title:'Warning', msg:'please choose Excel files only!'});
			       event.preventDefault();
			   }

			});
		$('#product-arrived-quantity-upload-form').submit(function(event) {
			   var file = $('#fileArrivedQuantity').val();       

			   if ( ! file) {
			       showMessage({title:'Warning', msg:'Please choose a file'});
			       event.preventDefault();
			       return;
			   } 

			   if ( ! file.match(/\.(xls)$/)) {
			       showMessage({title:'Warning', msg:'please choose Excel files only!'});
			       event.preventDefault();
			   }

			});
		
	},
		
		
	initFunctionButtons: function(){
		$('#action-add-product').click(function() {
			$.post('product/product_add.jsp', '',
			        function(data){
						$('#product-add-container').html(data);
						$("#product-add-dialog").dialog({
			    			autoOpen: true,
			    			height: 460,
			    			width: 650,
			    			modal: true,
			    			buttons: {
			    			Save: function() {
			    				var thisButton = $(this);
			    				var resultSuccess=true;
			    				var resultFailure=false;
			    				var validBatchNumber=$('#productBatchNumber').val();
			    				var validProductName=$('#productName1').val();
			    				var paramString='productName='+validProductName+'&batchNumber='+validBatchNumber+'&action=validate-productName';
			    				$.post('product.json',paramString,
			    					    function(data){
			    			    var exists=data.result.data;
			    				if(ProductValidationHandler.validateProduct() === false){
			    					return resultSuccess;
			    				}else if(exists == "n"){
			    					return resultSuccess;
			    				}else{
			    					var paramString = $('#product-form').serializeArray();
			    					var productCost=$('#costPerQuantity').val();
			    					//access form searialized value and modified
									$.each(paramString, function(i, formData) {
									    if(formData.value === productCost){ 
									    	formData.value = currencyHandler.convertStringPatternToFloat(productCost);
									    }
									});    
									//convert serialized array to url-encoded string
									var sendFormData = $.param(paramString);
				    				$.post('product.json', sendFormData,
				    						 function(obj) {
				    					 ProductHandler.initSearchProductListOnLoad();
				    					});
				    				$("#product-add-dialog").dialog('close');
			    				    }
			    				});
			    			},
			    		      Cancel: function() {
									$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
									$("#error-message").dialog(
													{
														resizable : false,
														height : 140,
														title : "<span class='ui-dlg-confirm'>Confirm</span>",
														modal : true,
														buttons : {
															'Ok' : function() {
																$('#product-add-dialog').dialog("close");
												    			$(this).dialog('close');
															},
															Cancel : function() {
																$(this).dialog('close');
															}
														}
													});
									return false;
								},
			    			 Clear: function() {
			    				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			    					$("#error-message").dialog({
			    						resizable: false,
			    						height:140,
			    						title: "<span class='ui-dlg-confirm'>Confirm</span>",
			    						modal: true,
			    						buttons: {
			    							'Ok' : function() {
			    								$('form').clearForm();
			    								$('.productValid').empty();
			    								$(this).dialog("close");
			    							},
			    							Cancel : function(){
			    								$(this).dialog('close');
			    							}
			    						}
			    					});
			    			  }, 
			    			},
			    			close: function() {
			    				$('#product-add-container').html('');
			    			}
			    		});
			        });
		      });
		$('#action-add-arrived-quantity').click(function() {
			$.post('product/product_arrived_quantity.jsp', '',
			        function(data){
				/*var productList;
				 $('.report-body').each(function(){
					 var maxHeight;
						var productLength=$(this).find('#productName').height();
						var batchNumberLength=$(this).find('#batchNumber').height();
						if(productLength > batchNumberLength){
							maxHeight=productLength;
						}else{
							maxHeight=batchNumberLength;
						}
						$(this).find('.sameheightcolumns').height(maxHeight);
						productList+=$(this);
					});*/
						$('#product-add-arrived-quantity-container').html(data);
						$("#product-add-arrived-quantity-dialog").dialog({
			    			autoOpen: true,
			    			height: 600,
			    			width: 700,
			    			modal: true,
			    			buttons: {
			    			Save: function() {
			    				var thisButton = $(this);
			    				var listOfObjects = '';
			    				var sum = 0;
			    				var result;
								$('div#arrived-product').each(function(index, value) {
							    	  var id = $(this).find('div').attr('id');
							    	  var productName = $(this).find('#productName').html();
		    						  var batchNumber = $(this).find('#batchNumber').html();
							    	  var quantityArrived = currencyHandler.convertStringPatternToNumber($(this).find('#qunatityArrived').val());
							    	  if(/^([0-9]+,?)+$/.test(quantityArrived) == false || currencyHandler.convertStringPatternToNumber(($(this).find('#qunatityArrived')).val()).toString().length > 10 || quantityArrived < 0){
										  result = false;
										}
							    	  else{
							    		  if(quantityArrived != "") {
								        	  if(index == 0){
								        		  listOfObjects +=productName+'|'+batchNumber+'|'+quantityArrived;
								        	  }else{
								        		  listOfObjects +=','+productName+'|'+batchNumber+'|'+quantityArrived;
								        	  }
								          }
							    	  }
							         
							    });
								if(result != false){
									$.post('product.json','action=add-arrived-quantity&listOfProductObjects=' + listOfObjects,function(obj) {
							    		ProductHandler.initSearchProductListOnLoad();
							    	});
									$("#product-add-arrived-quantity-dialog").dialog('close');
								}
								 $(this).dialog('close');
			    				},
			    		      Cancel: function() {
									$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
									$("#error-message").dialog(
													{
														resizable : false,
														height : 140,
														title : "<span class='ui-dlg-confirm'>Confirm</span>",
														modal : true,
														buttons : {
															'Ok' : function() {
																$('#product-add-arrived-quantity-dialog').dialog("close");
												    			$(this).dialog('close');
															},
															Cancel : function() {
																$(this).dialog('close');
															}
														}
													});
									return false;
								},
			    			 Clear: function() {
			    				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			    					$("#error-message").dialog({
			    						resizable: false,
			    						height:140,
			    						title: "<span class='ui-dlg-confirm'>Confirm</span>",
			    						modal: true,
			    						buttons: {
			    							'Ok' : function() {
			   			    				 $("div#arrived-product").each(function() {
			   			    					  var id = $(this).find('div').attr('id');
			   			    					  $(this).find('#qunatityArrived').val("0");
			   			    				});
			    								$('form').clearForm();
			    								$(this).dialog("close");
			    							},
			    							Cancel : function(){
			    								$(this).dialog('close');
			    							}
			    						}
			    					});
				    		  },
			    			},
			    			close: function() {
			    				$('#product-add-arrived-quantity-container').html('');
			    			}
			    		});
			        }
		        );
		});
		$('#action-add-damaged-quantity').click(function() {
			$.post('product/product_damaged_quantity.jsp', '',
			        function(data){
						$('#product-add-damaged-quantity-container').html(data);
						$("#product-add-damaged-quantity-dialog").dialog({
			    			autoOpen: true,
			    			height: 600,
			    			width: 700,
			    			modal: true,
			    			buttons: {
			    			Save: function() {
			    				var thisButton = $(this);
			    				var listOfObjects = '';
			    				var sum = 0;
			    				var result;
			    				var count=0;
			    				$('.error-damaged-quantity').empty();
								$('div#damaged-product').each(function(index, value) {
							    	  var id = $(this).find('div').attr('id');
							    	  var productName = $(this).find('#productName').html();
		    						  var batchNumber = $(this).find('#batchNumber').html();
		    						  var available=$(this).find('#available').html();
							    	  var damagedQuantity =  currencyHandler.convertStringPatternToNumber($(this).find('#damagedQuantity').val());
							    	  if(/^([0-9]+,?)+$/.test(damagedQuantity) && available >= damagedQuantity){
							    		  if(damagedQuantity != "") {
								        	  if(index == 0){
								        		  listOfObjects +=productName+'|'+batchNumber+'|'+damagedQuantity;
								        	  }else{
								        		  listOfObjects +=','+productName+'|'+batchNumber+'|'+damagedQuantity;
								        	  }
								          }
							    	  }else{
							    		  result=false;
							    		  $('#damagedQuantity-'+count).html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							    		  $('#damagedQuantity-'+count).addClass('error');
							    	  }
							    	  count++;
							    });
								if( result == false){
									$('.input-field').click(function(event){
										 var count=0;
										 var damagedQuantity= $(this).find('#damagedQuantity').val();
										 var Id=$(this).find('.error-damaged-quantity').attr('id');
										/* $('#'+Id).empty();
										 if($('#'+Id).hasClass('error')){
											 if(/^([0-9]+,?)+$/.test(damagedQuantity) == false){
												 $('#'+Id).html("Only numeric values are accepted").css({'background':'#ccc','color':'#000000', 'margin-left': '35px','margin-top': '-10px','position': 'absolute','width':'120px','z-index': '99999'});
											 }else{
												 $('#'+Id).html("Damaged Quantity cannot be more than Available quanity").css({'background':'#ccc','color':'#000000', 'margin-left': '35px','margin-top': '-10px','position': 'absolute','width':'120px','z-index': '99999'});
												 
											 }
										 }*/
									});
								}else{
									$.post('product.json','action=add-damaged-quantity&listOfProductObjects=' + listOfObjects,function(obj) {
							    		ProductHandler.initSearchProductListOnLoad();
							    	});
			    				$("#product-add-damaged-quantity-dialog").dialog('close');
								}
			    				},
			    		      Cancel: function() {
									$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
									$("#error-message").dialog(
													{
														resizable : false,
														height : 140,
														title : "<span class='ui-dlg-confirm'>Confirm</span>",
														modal : true,
														buttons : {
															'Ok' : function() {
																$('#product-add-damaged-quantity-dialog').dialog("close");
												    			$(this).dialog('close');
															},
															Cancel : function() {
																$(this).dialog('close');
															}
														}
													});
									return false;
								},
			    			 Clear: function() {
			    				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			    					$("#error-message").dialog({
			    						resizable: false,
			    						height:140,
			    						title: "<span class='ui-dlg-confirm'>Confirm</span>",
			    						modal: true,
			    						buttons: {
			    							'Ok' : function() {
			    								 $("div#damaged-product").each(function() {
			   			    					  var id = $(this).find('div').attr('id');
			   			    					  $(this).find('#damagedQuantity').val("0");
			   			    				});
			    								$('form').clearForm();
			    								$(this).dialog("close");
			    							},
			    							Cancel : function(){
			    								$(this).dialog('close');
			    							}
			    						}
			    					});
				    		  },
			    			},
			    			close: function() {
			    				$('#product-add-damaged-quantity-container').html('');
			    			}
			    		});
			        }
		        );
		});
		$('.report-body').live("blur",function(){
			 var count=0;
			 $.each($('.productNumber'), function(index, value) {
				  var arrivedQuantity = $(this).find('#qunatityArrived').val();
				  var spanId=$(this).find("span").attr("id");
				  var arrived=currencyHandler.convertStringPatternToNumber($(this).find('#qunatityArrived').val());
				  if(arrivedQuantity != 0){
				  if(/^([0-9]+,?)+$/.test(arrived) == false || currencyHandler.convertStringPatternToNumber(($(this).find('#qunatityArrived')).val()).toString().length > 10){
					  $('#arrivedQuantity-'+count).html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>")
					}else{
						var formatArrivedQuantity=currencyHandler.convertNumberToStringPattern(arrivedQuantity);
						$(this).find('#qunatityArrived').val(formatArrivedQuantity);
						$("#arrivedQuantity-"+count).empty();
					}
				  }else{
				  }
				  count++;
			 });
		});
		$('#product-list').click(function(){
			window.location.reload(true);
		});
		
	},
	AjaxSucceeded: function(data1) {
	    if (data1.result.data == "y") {
	        $('#batchNumberValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='ProductName available!'> ");
	    } else if (data1.result.data == "v") {
	        $('#batchNumberValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> ");
	    }
	    /*confirming and displaying remainder date is valid or Invalid*/
	     else{
	        $('#batchNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>ProductName with Batch Number Alreday Exists!");
	         
	     }
	},
	
	//Function to format date to DD/MM/YYYY
	formatDate:function(inputFormat){
		var str=inputFormat.split(/[" "]/);
		dt=new Date(str[0]);
		return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
	},
	//end of function
	
	initSearchProductListOnLoad : function () {
		var paramString = 'action=search-product';  
		//$('#search-results-list').ajaxLoader();
		$.post('product.json', paramString,
		        function(obj){
			    	var data = obj.result.data;
			    	ProductHandler.displaySearchData(data);
		        }
		    );
		$('#ps-exp-col').click(function() {
			//ProductHandler.expanded = !ProductHandler.expanded;
		    if(PageHandler.expanded) {
		    	$( '#productCategory' ).css( "width", "138px" );
		    	$( '.report-header' ).css( "width", "697px" );
		    	$( '.report-body' ).css( "width", "697px" );
		    	$( '.product-category-header' ).css( "width", "135px" );
		    	$( '.product-name-header' ).css( "width", "110px" );
		    	$( '.batch-number-header' ).css( "width", "110px" );
		    	$( '.product-cost-header' ).css( "width", "105px" );
		    	$( '.product-description-header' ).css( "width", "100px" );
		    	$( '.product-category-body' ).css( "width", "136px" );
		    	$( '.product-name-body' ).css( "width", "110px" );
		    	$( '.batch-number-body' ).css( "width", "110px" );
		    	$( '.product-cost-body' ).css( "width", "105px" );
		    	$( '.product-description-body' ).css( "width", "100px" );
				$('.jScrollPaneContainer').css("width","697px");
				$('#search-results-list').css("width","697px");
				$('.grid-content').css("width","697px");
				$('.first-btn').css("margin-left","190px");
				$('.first-btn').css("width","15px");
				$('.previous-btn').css("margin-left","10px");
				$('.previous-btn').css("width","15px");
				$('.next-btn').css("width","15px");
				$('.next-btn').css("margin-left","10px");
				$('.last-btn').css("width","15px");
				$('.transaction-header').css("width","85px");
				$('.transaction-row').css("width","85px");
				$('.product-action').css("width","70px");
				$( '.report-header-transaction-column2' ).css( "width", "105px" );
				$( '.report-body-transaction-column2' ).css( "width", "105px" );
			} else {
				$('.green-result-col-1').css("width","820px");
				$( '#productCategory' ).css( "width", "138px" );
				$( '.product-category-body' ).css( "width", "138px" );
				$('.grid-content').css("width","830px");
				$('.jScrollPaneContainer').css("width","830px");
				$( '.report-header' ).css( "width", "830px" );
		    	$( '.report-body' ).css( "width", "830px" );
				$( '.report-header-column2' ).css( "width", "137px" );
				$( '.product-name-header' ).css( "text-align", "center" );
		    	$( '.batch-number-header' ).css( "text-align", "center" );
		    	$( '.product-cost-header' ).css( "text-align", "center" );
		    	$( '.product-description-header' ).css( "text-align", "center" );
				$( '.report-body-column2' ).css( "width", "137px" );
				$('.first-btn').css("margin-left","235px");
				$('.first-btn').css("width","20px");
				$('.previous-btn').css("margin-left","20px");
				$('.previous-btn').css("width","20px");
				$('.next-btn').css("width","20px");
				$('.last-btn').css("width","20px");
				$('.last-btn').css("margin-left","20px");
				$('.transaction-header').css("width","105px");
				$('.transaction-row').css("width","105px");
				$('.product-action').css("width","70px");
				$( '.report-header-transaction-column2' ).css( "width", "125px" );
				$( '.report-body-transaction-column2' ).css( "width", "125px" );
			}
			setTimeout(function() {
				//$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').jScrollPane({
					showArrows : true
				});
			}, 0);
		});
	},
   initSearchResultButtons : function () {
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('product/product_edit.jsp', 'id='+id,
		        function(data){
				//$('#product-edit-form').clearForm();
					$('#product-edit-container').html(data);
					$("#product-edit-dialog").dialog({
		    			autoOpen: true,
		    			height: 460,
		    			width: 650,
		    			modal: true,
		    			buttons: {
		    			Update: function() {
		    			
		    				var resultSuccess=true;
		    				var resultFailure=false;
		    				/*if(ProductHandler.validateProduct() == false){
		    					return resultSuccess;
		    				}else{*/
		    				if(ProductValidationHandler.validateProduct()==false){
		    					return resultSuccess;
		    				}
		    				var validBatchNumber=$('#productBatchNumberEdit').val();
		    				var validProductName=$('#productName1').val();
		    				var paramString='productName='+validProductName+'&batchNumber='+validBatchNumber+'&id='+id+'&action=validate-edit-productName';
		    				$.post('product.json',paramString,
		    					    function(data){
		    					 var exists=data.result.data;
				    				if(exists == "n"){
				    					return resultSuccess;
				    				}else{
				    					$(this).dialog('close');
				    					var paramString = $('#product-edit-form').serializeArray();
				    					var productCost=$('#costPerQuantity').val();
				    					//access form searialized value and modified
										$.each(paramString, function(i, formData) {
										    if(formData.value === productCost){ 
										    	formData.value = currencyHandler.convertStringPatternToFloat(productCost);
										    }
										});    
										//convert serialized array to url-encoded string
										var sendFormData = $.param(paramString);
										$('.page-content').ajaxSavingLoader();
					    				 $.post('product.json',sendFormData,function(obj) {
					    					 $.loadAnimation.end();
					    						$(this).successMessage({
					    							container : '.product-page-container',
					    							data : obj.result.message
					    						});
					    					});
					    				 $("#product-edit-dialog").dialog('close');
				    				}
				    				
		    				});
		    				},
		    			    Cancel: function() {
								$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
								$("#error-message").dialog(
												{
													resizable : false,
													height : 140,
													title : "<span class='ui-dlg-confirm'>Confirm</span>",
													modal : true,
													buttons : {
														'Ok' : function() {
															$('#product-edit-dialog').dialog("close");
											    			$(this).dialog('close');
														},
														Cancel : function() {
															$(this).dialog('close');
														}
													}
												});
								return false;
							},
		    			 Clear: function() {
		    				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
		    					$("#error-message").dialog({
		    						resizable: false,
		    						height:140,
		    						title: "<span class='ui-dlg-confirm'>Confirm</span>",
		    						modal: true,
		    						buttons: {
		    							'Ok' : function() {
		    								$('form').clearForm();
		    								$('.helppop').hide();
		    								$('#batchNumber_pop').hide();
		    								$(this).dialog("close");
		    							},
		    							Cancel : function(){
		    								$(this).dialog('close');
		    							}
		    						}
		    					});
			    		  },
		    			},
		    			close: function() {
		    				$('#product-edit-container').html('');
		    			}
		    		});
		        }
	        );
		});
		$("#brand").focus(function(event){
			$('#productBrandValid').empty();
			 $('#productBrand_pop').show();
		});
		$('#brand').blur(function (){
			var productBrand=$(this).val();
			var brandEnd=productBrand.length;
			$('#productBrand_pop').hide();
			if(/^[a-zA-Z0-9\s]+$/.test($(this).val()) == false || productBrand.charAt(0) == " " || productBrand.charAt(brandEnd - 1) == " "){
				$('#productName_pop').show();
				$('#productName1').val('');
				$('#productBrandValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			}else{
				$('#productBrandValid').empty();
			}
	      });
		$('#costPerQuantity').blur(function (){
			var cquantity=$(this).val();
			if(cquantity != null){
			var cost=currencyHandler.convertStringPatternToFloat(cquantity);
			var digitLength=cost.toString().length;
			if(digitLength > 10){
				$('#productCostValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			}else{
				var formatCost=currencyHandler.convertFloatToStringPattern(cost.toFixed(2));
				$(this).val(formatCost);
				$('#productCostValid').empty();
			}
			}
	      });
		$("#model").focus(function(event){
			$('#productCostValid').hide();
			 $('#productModel_pop').show();
		});
		$('#model').blur(function(){
			var productModel=$(this).val();
			var modelEnd=productModel.length;
			$('#productModel_pop').hide();
			if(/^[a-zA-Z0-9\s]+$/.test($(this).val()) == false || productModel.charAt(0) == " " || productModel.charAt(modelEnd - 1) == " "){
				$('#productName_pop').show();
				$('#productName1').val('');
				$('#productModelValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$(this).focus(function(){
					$('#productModelValid').hide();
			});
			}else{
			    $('#productModelValid').empty();
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#brand').val()) == false || /^[a-zA-Z0-9\s]+$/.test($(this).val()) == false){
			}else{
				$('#productName_pop').hide();
				/*var productModelName = $(this).val();*/
				var productBrandName = $("#brandEdit").val();
		        var productName = (productBrandName) + ' ' + ($(this).val());
		        $('#productName1').val('');
		        $('#productName1').val(productName);
		        $('#productNameValid').empty();
			}
	      });
		$('#productBatchNumber').change(function (){
			if(/^[a-zA-Z0-9_#\s]+$/.test($(this).val()) == false){
			}else{
			var validBatchNumber=$(this).val();
			var validProductName=$('#productName1').val();
			if(validBatchNumber == '')
			{
			 $('#batchNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>Enter Batch Number" );
			 $(this).focus();
			}
			else{
				var paramString='productName='+validProductName+'&batchNumber='+validBatchNumber+'&action=validate-productName';
				$.post('product.json',paramString,
				    function(data){
		        	$('#batchNumberValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking productName...'> Checking...");
		               var delay = function() {
		            	ProductHandler.AjaxSucceeded(data);
		            	};
		            	setTimeout(delay, 0);
				});
			}
		}
	});
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('product/product_profile_view.jsp', 'id='+id,
		        function(data){
				$('#product-view-container').html(data);
				$("#product-view-dialog").dialog({
					autoOpen: true,
					height: 380,
					width: 600,
					modal: true,
					buttons: {
						Close: function() {
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
		$('.enable-icon').click(function() {
			var id = $(this).attr('id');
			 var productStatusParam='Enabled';
			$('#product-view-container').html('Are You Sure Want To Enable This Product ?');
			
			  $('#product-view-dialog').dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						Yes: function() {
							 $.post('product.json', 'action=modify-product-status&productStatusParam='+productStatusParam+'&id='+id, 
										function(obj) {
								 var list=obj.result.data;
								 ProductHandler.displaySearchData(list);
							 });
							 $("#product-view-dialog").dialog('close');
						},
			            No: function() {
			            	$('#product-view-dialog').dialog('close');
				      }
					},
					close: function() {
						$('#product-view-dialog').dialog('close');
					}
	        });
			
		});
		
		 $('.disable-icon').click(function(){
			 var id = $(this).attr('id');
			 var productStatusParam='Disabled';
			 $.post('product.json', 'action=check-products-in-warehouse&id='+id, 
						function(obj) {
				 var data=obj.result.data;
				 if(data =='true'){
					 $('#product-view-container').html('Are You Sure Want To Disable This Product ?');
					 $('#product-view-dialog').dialog({
							autoOpen: true,
							height: 200,
							width: 500,
							modal: true,
							buttons: {
								Yes: function() {
									$.post('product.json', 'action=modify-product-status&productStatusParam='+productStatusParam+'&id='+id, 
											function(obj) {
										 var list=obj.result.data;
										 ProductHandler.displaySearchData(list);
								     });
									 $("#product-view-dialog").dialog('close');
								},
					            No: function() {
					            	$('#product-view-dialog').dialog('close');
						      }
							},
							close: function() {
								$('#product-view-dialog').dialog('close');
							}
					 });
				 }else{
						showMessage({title:'Message', msg:'Cannot Disable, Product is available in Warehouse'});
				 }
		    });
		 });
	},
	displaySearchData : function(data){
		$('#search-results-list').ajaxLoader();
		$('#search-results-list').html('');
		if(data != undefined) {
			var alternate = false;
			var productTable='';
			 productTable +='<div class="report-header" style="width: 697px; height: 30px; text-align:center; ">'+
			 '<div class="report-header-column2 product-category-header" style="width: 135px; float: left;">' + Msg.PRODUCT_CATEGORY + '</div>' +
			 '<div class="report-header-column2 product-name-header" style="width: 110px; float: left;">' + Msg.PRODUCT_PRODUCT_NAME + '</div>' +
			'<div class="report-header-column2 batch-number-header" style="width: 110px; float: left;">'+ Msg.PRODUCT_BATCH_NUMBER +'</div>'+
			'<div class="report-header-column2 product-cost-header" style="width: 105px; float: left; line-height:12px;">'+ Msg.PRODUCT_COST +' '+'('+Msg.CURRENCY_FORMATE+')</div>'+
			'<div class="report-header-column2 product-description-header" style="width: 100px; float: left;">'+ Msg.PRODUCT_DESCRIPTION +'</div>'+
			'<div class="report-header-column2 product-action" style="width: 70px; float: left;"></div>'+
			'</div>';
			 $('#search-results-list').append(productTable);
			 $('#search-results-list').append('<div class="grid-content" style="height:270px;width: 697px;overflow-y:initial;"></div>'); 
			 for(var loop=0;loop<data.length;loop=loop+1) {
					 var productTableRows ='<div class="ui-content report-content">';
						productTableRows+='<div class="report-body row-'+ProductHandler.count+'" id="row-'+data[loop].id+'" style="width: 697px; height: auto; overflow: hidden;">';
						if(data[loop].productCategory==undefined){
							productTableRows+='<div id="productCategory" class="report-body-column2 product-category-body centered " style="height: inherit;  width: 136px; word-wrap: break-word;">'+''+'</div>';
						}
						else{
							productTableRows+='<div id="productCategory" class="report-body-column2 product-category-body centered " style="height: inherit;  width: 136px; word-wrap: break-word;">' +  data[loop].productCategory  + '</div>';
						}
						productTableRows+='<div id="productName" class="report-body-column2 product-name-body centered " style="height: inherit;  width: 110px; word-wrap: break-word;">' +  data[loop].productName  + '</div>';
						productTableRows+='<div id="batchNumber" class="report-body-column2 batch-number-body centered " style="height: inherit; width: 110px; word-wrap: break-word;">'+ data[loop].batchNumber +'</div>';
						productTableRows+='<div class="report-body-column2 product-cost-body right-aligned " style="height: inherit; width: 105px; word-wrap: break-word; text-align: right;">'+ data[loop].costPerQuantity +'</div>';
						productTableRows+='<div class="report-body-column2 product-description-body centered " style="height: inherit; width: 100px; word-wrap: break-word;">'+ data[loop].description +'</div>';
						productTableRows+='<div class="report-body-column2 centered product-action " id="action-'+data[loop].id+'" style="height: inherit; width: 70px; word-wrap: break-word;">';
						productTableRows+='<div id="'+data[loop].id+'" class="ui-btn edit-icon"  title="Edit Product" style="margin-top:1px;"></div>';
						productTableRows+='<div id="'+data[loop].id+'" class="btn-view centered"  title="View Product Details"></div>';
						productTableRows+='<div id="'+data[loop].id+'" class="disable-icon enable-disable"  title="Disable Product"></div>';
						productTableRows+='</div>';
						productTableRows+='</div>';
						productTableRows+='</div>';
					    $('.grid-content').append(productTableRows);
					    if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
					    	ProductHandler.checkGridLength($('.row-'+ProductHandler.count));
						   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
							   ProductHandler.checkGridLength($('.row-'+ProductHandler.count));
							}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
								ProductHandler.checkGridLength($('.row-'+ProductHandler.count));
							}else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
							   ProductHandler.checkGridLength($('.row-'+ProductHandler.count));
				           }
				           else{
				        	   ProductHandler.checkGridLength($('.row-'+ProductHandler.count));
						   }
					    ProductHandler.count=ProductHandler.count+1;
					    if(data[loop].state == 'Disabled'){
					    	 $("#row-"+data[loop].id).css('opacity', '0.5');
					    	 $("#action-"+data[loop].id).find(".edit-icon").hide();
							 $("#action-"+data[loop].id).find(".enable-disable").addClass("enable-icon");
							 $("#action-"+data[loop].id).find(".enable-disable").removeClass("disable-icon");
							 $("#action-"+data[loop].id).find(".enable-icon").attr("title","Enable Product");
					    }else{
					    	 $("#action-"+data[loop].id).find(".disable-icon").attr("title","Disable Product");
					    }
	        };
	        
	        $('.grid-content').jScrollPane({showArrows:true});
	        ProductHandler.initSearchResultButtons();
		} else {
			$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1" style="width:690px;"><div class="result-title">No search results found</div></div></div>');
		}
		$.loadAnimation.end();
	},
	initSearchProductList : function(){
		//button click - search
		//button click - search
		$('#action-search-product-list').click(function() {
			var paramString = $('#product-search-form').serialize(); 
			$('#search-results-list').ajaxLoader();
			$.post('product.json', paramString,
			        function(obj){
				    	var data = obj.result.data;
				    	ProductHandler.displaySearchData(data);
			        }
			    );
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
							$('#product-search-form').clearForm();
							$(this).dialog("close");
						},
						Cancel : function(){
							$(this).dialog('close');
						}
					}
				});
		});
	},
	checkGridLength: function(group1){
		$('.row-'+ProductHandler.count).each(function(index) {
	        var maxHeight = 0;
	        $(this).children().each(function(index) {
	            if($(this).height() > maxHeight) 
	                maxHeight = $(this).height();
	        });
	        //alert(maxHeight);
	        $(this).children().height(maxHeight);
	        //$(this).children().css('margin-top',maxHeight/2+'px');
	    });    
	},
	equalHeight: function(group2){
			   tallest = 0;
			   group2.each(function() {
			      thisHeight = $(this).height();
			     // alert(thisHeight);
			      if(thisHeight > tallest) {
			         tallest = thisHeight;
			      }
			   });
			   group2.height(tallest);
	},
};