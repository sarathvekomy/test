var ProductCustomerCostHandler = {
		expanded: true,
		count: 0,
	initPageLinks : function() {
		$('#product-add-customer-cost').pageLink({
			container : '.product-page-container',
			url : 'product/product-customer-cost/product_customer_cost_add.jsp'
		});
		$('#product-search-customer-cost').pageLink({
			container : '.product-page-container',
			url : 'product/product-customer-cost/product_customer_cost_search.jsp'
		});
		$('#product-import-customer-cost').pageLink({
			container : '.product-page-container',
			url : 'product/product-customer-cost/product_customer_cost_import.jsp'
		});
	},
	productCustomerCostSteps : [ '#product-customer-cost-add-form'],
	productCustomerCostUrl : [ 'productCustomerCommand.json'],
	productCustomerCostStepCount : 0,
	
	//product customer cost search result onload
	initProductCustomerCostSearchResultOnLoad : function(){
		var thisButton = $(this);
		var paramString = $('#product-customer-cost-search-form').serialize();  
		$('#search-results-list').ajaxLoader();
		$.post('productCustomerCost.json', paramString, function(obj){
		    	var data = obj.result.data;
		    	//for refreshing input fields after search
		    	$('form').clearForm();
				$('#search-results-list').html('');
				if(data == undefined){
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}else{
				    if(data.length > 0) {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						var dateFormat=ProductCustomerCostHandler.formatDate(data[loop].createdDate);
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate">';
						} else {
							rowstr = '<div class="green-result-row">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1">'+
						'<div  class="result-body" style="font: bold 14px arial;width: 300px;">' +data[loop].businessName+ '</div>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+ Msg.PRODUCT_CREATED_DATE_LABEL+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' +dateFormat+ '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action">' + 
						'<div businessName="'+data[loop].businessName+'" class="ui-btn edit-icon" title="Edit Product Customer Cost"></div>' + 
						'<div businessName="'+data[loop].businessName+'"& id="'+data[loop].id+'" class="ui-btn delete-icon delete-organization" title="Delete Product Customer Cost"></div>' + 
						'<div id="'+data[loop].businessName+'" class="ui-btn btn-view" title="View Product Customer Cost Details"></div>';
						'</div>'; 
						$('#search-results-list').append(rowstr);
			};
			ProductCustomerCostHandler.initSearchResultButtons();
			$('#search-results-list').jScrollPane({showArrows:true});
				} 
		      else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
			}
				$.loadAnimation.end();
				setTimeout(function(){
					$('#search-results-list').jScrollPane({showArrows:true});
				},0);
	        });
	},

	initAddButtons: function () {
		$(document).click(function() {
			$('#business-name-suggestions').hide();
		});
		$('#business-name-suggestions').click(function (){
	        var customerBusinessName = $('#businessName').val();
	        $("div#product-row").each(function() {
				  var id = $(this).find('div').attr('id');
				  $(this).find('input').val("");
			});
	        $.post('productCustomerCost.json', 'action=get-product-cost-by-business-name&businessName='+customerBusinessName,
			        function(obj){
	        	    var data = obj.result.data;
	        	    	$("div#product-row").each(function() {
	        	    		for(var loop=0;loop<data.length;loop++){
	        	    			var id = $(this).find('div').attr('id');
	        	    			var productName = $('#'+id).find('#product-name').html();
	        	    			if(productName==data[loop].productName){
	        	                $(this).find('input').val(currencyHandler.convertFloatToStringPattern(data[loop].cost));
	        	    		  }
	        	    		}
	        	    	});
	        	    });
	      }); 
		
	$('#action-clear').click(function() {
         $('#businessName').val("");
         $("div#product-row").each(function() {
			  var id = $(this).find('div').attr('id');
			  $(this).find('input').val("");
		});
	});
	//product customer cost save button
	 $('#button-save-product-customer-cost').click(function() {
		 var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(ProductCustomerCostHandler.validateProductCustomerCost()==false){
				return resultFailure;
			}else{
			$("div#product-row").each(function() {
				  var id = $(this).find('div').attr('id');
				  var customerCost =$(this).find('input').val();
				  if(customerCost != ''){
					  var productName = $('#'+id).find('#product-name').html();
					  var batchNumber = $('#'+id).find('#batch-number').html();
					  var customerCost =  $(this).find('input').val();
					  if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test(customerCost) == false || currencyHandler.convertStringPatternToFloat(($(this).find('#customerCost')).val()).toString().length > 10){
						  $('#button-save-product-customer-cost').disable();
						}else{
					    var paramString = $('form').serializeArray();
    					//access form searialized value and modified
						$.each(paramString, function(i, formData) {
						    if(formData.value === customerCost){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(customerCost);
						    }
						});    
						//convert serialized array to url-encoded string
						var formatCustomerCost=currencyHandler.convertStringPatternToFloat(customerCost);
						var sendFormData = $.param(paramString);
						$.post('productCustomerCost.json', sendFormData+'&productName='+productName+'&batchNumber='+batchNumber+'&cost='+formatCustomerCost,
							        function(obj){
									  $(this).successMessage({
										container : '.product-page-container',
										data : obj.result.message
									  });
							        });
						}
				      }else{
				      }
			  });
		}
		});
	 $('#product-add-customer-cost').click(function() {
			$('.product-page-container').load('product/product-customer-cost/product_customer_cost_add.jsp');
		});
		$('#button-update-product-customer-cost').click(function() {
			if($(ProductCustomerCostHandler.productCustomerCostSteps[ProductCustomerCostHandler.productCustomerCostStepCount]).validate()==false) return;
			$("div#product").each(function() {
				var id =  $(this).find('div').attr('id');
				  var customerCost =  $(this).find('input').val();
				  if(customerCost != ''){
					  var productName = $(this).find('#productName').html();
					  var batchNumber = $(this).find('#batchNumber').html();
					  if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test(customerCost) == false || currencyHandler.convertStringPatternToFloat(($(this).find('#customerCost')).val()).toString().length > 10){
						  $('#button-save-product-customer-cost').disable();
						}else{
					  var paramString = $('form').serializeArray();
					//access form searialized value and modified
						$.each(paramString, function(i, formData) {
						    if(formData.value === customerCost){ 
						    	formData.value = currencyHandler.convertStringPatternToFloat(customerCost);
						    }
						});    
						//convert serialized array to url-encoded string
						var formatCustomerCost=currencyHandler.convertStringPatternToFloat(customerCost);
						var sendFormData = $.param(paramString);
						$.post('productCustomerCost.json', sendFormData+'&productName='+productName+'&batchNumber='+batchNumber+'&cost='+formatCustomerCost,
							        function(obj){
									  $(this).successMessage({
										container : '.product-page-container',
										data : obj.result.message
									  });
							        });
				           }
				}else{
					
				}
			});
			  
		});
		$('#action-cancel-product-customer-cost').click(function() {
		    $('#error-message').html('You will loose unsaved data.. Clear form?');   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$('.task-page-container').html('');
		    			var container ='.product-page-container';
		    			var url = "product/product-customer-cost/product_customer_cost_add.jsp";
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
		
		$('#button-edit-product-customer-cost').click(function() {
			var thisButton = $(this);
			var paramString = $('#product-customer-cost-edit-form').serialize();
			$.post('productCustomerCost.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.product-page-container', data:obj.result.message});
		        }
		    );
		});
		$('#product-search-customer-cost').click(function() {
			$('.product-page-container').load('product/product-customer-cost/product_customer_cost_search.jsp');
		});
	},
	initSearchProduct : function (role) {
		$('#ps-exp-col').click(function() {
			setTimeout(function() {
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').jScrollPane({showArrows:true});
            }, 0);
		});
		
		$('#action-clear').click(function() {
			$('#product-customer-cost-search-form').clearForm();
			$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},0);
		});
		//button click - search
		$('#action-search-product').click(function() {
			var thisButton = $(this);
			var paramString = $('#product-customer-cost-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			$.post('productCustomerCost.json', paramString, function(obj){
			    	var data = obj.result.data;
			    	//for refreshing input fields after search
			    	$('form').clearForm();
					$('#search-results-list').html('');
					if(data == undefined){
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
					}else{
					    if(data.length > 0) {
						var alternate = false;
						for(var loop=0;loop<data.length;loop=loop+1) {
							var dateFormat=ProductCustomerCostHandler.formatDate(data[loop].createdDate);
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate">';
							} else {
								rowstr = '<div class="green-result-row">';
							}
							alternate = !alternate;
							rowstr = rowstr + '<div class="green-result-col-1">'+
							'<div  class="result-body" style="font: bold 14px arial;width: 300px;">' +data[loop].businessName+ '</div>' +
							'</div>' +
							'<div class="green-result-col-2">'+
							'<div class="result-body">' +
							'<span class="property">'+ Msg.PRODUCT_CREATED_DATE_LABEL+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' +dateFormat+ '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-action">' + 
							'<div businessName="'+data[loop].businessName+'" class="ui-btn edit-icon" title="Edit Product Customer Cost"></div>' + 
							'<div businessName="'+data[loop].businessName+'"& id="'+data[loop].id+'" class="ui-btn delete-icon delete-organization" title="Delete Product Customer Cost"></div>' + 
							'<div id="'+data[loop].businessName+'" class="ui-btn btn-view" title="View Product Customer Cost Details"></div>';
							'</div>'; 
							$('#search-results-list').append(rowstr);
				};
				ProductCustomerCostHandler.initSearchResultButtons();
				$('#search-results-list').jScrollPane({showArrows:true});
					} 
			      else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
					}
				}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
		        });
		});
		$('#product-exp-coll').click(function() {
			if($(this).hasClass('expand-icon')) {
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').css('height', '428px');
				$('#search-results-list').jScrollPane({showArrows:true});
			} else if($(this).hasClass('collapse-icon')) {save
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').css('height', '328px');
				$('#search-results-list').jScrollPane({showArrows:true});
			}
		});
	},
	initSearchResultButtons : function () {
		$('.edit-icon').click(function() {
			if($(ProductCustomerCostHandler.productCustomerCostSteps[ProductCustomerCostHandler.productCustomerCostStepCount]).validate()==false) return;
			var businessName = $(this).attr('businessName');
			$.post('product/product-customer-cost/product_customer_cost_edit.jsp', 'businessName='+businessName,
		        function(data){
				$('.product-page-container').html(data);
				
		        });
		});
		$('.btn-view').click(function() {
			$.post('product/product-customer-cost/product_customer_cost_profile_view.jsp', 'name='+$(this).attr('id'),
		        function(data){
				$('#product-customer-cost-view-container').html(data);
				$("#product-customer-cost-view-dialog").dialog({
					autoOpen: true,
					height: 450,
					width: 900,
					modal: true,
					buttons: {
						Close: function() {
							$(this).dialog('close');
						}
					},
					close: function() {
						$('#product-customer-cost-view-container').html('');
					}
				});
		        }
	        );
		});
		
		$('.delete-organization').click(function() {
			var id=$(this).attr('id');
			var businessName=$(this).attr('businessName')
			$.post('product/product-customer-cost/product_customer_cost_delete.jsp', 'businessName='+businessName,
		        function(data){
		        	$('#product-customer-cost-delete-container').html(data);  
		        	$("#product-customer-cost-delete-dialog").dialog({
		    			autoOpen: true,
		    			height: 450,
		    			width: 900,
		    			modal: true,
		    			buttons: {
		    			Delete: function() {
		    				 $.post('productCustomerCost.json', 'businessName='+businessName+'&id='+id+'&action=delete-product-customer-cost',
		    						 function(obj) {
		    						$(this).successMessage({
		    							container : '.product-page-container',
		    							data : obj.result.message
		    						});
		    						$("#product-customer-cost-delete-dialog").dialog('close');
		    					});
		    				},
		    		      Close: function() {
		    			      $(this).dialog('close');
		    			
		    		          }
		    			},
		    			close: function() {
		    				$('#product-customer-cost-delete-container').html('');
		    			}
		    		});
		        }
	        );
		});
		
	},
	formatDate:function(inputFormat){
		var str=inputFormat.split(/[" "]/);
		dt=new Date(str[0]);
		return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
	},
	
	setTableGrid:function(){
		$.post('productCustomerCost.json','action=get-product-list', function(obj) {
			var data = obj.result.data;
			$('#search-results-list').html('');
			if(data.length>0) {
				    var productListTable="";
		             productListTable +='<div class="report-header" style="width: 697px; height: 40px;">'+
		             '<div class="report-header-column2 centered" style="width: 80px;">' + Msg.PRODUCT_SERIAL_NUMBER + '</div>' +
					'<div class="report-header-column2 centered" style="width: 130px;">' + Msg.PRODUCT_PRODUCT_NAME + '</div>' +
					'<div class="report-header-column2 centered" style="width: 130px;">' + Msg.PRODUCT_BATCH_NUMBER	 + '</div>' +
					'<div class="report-header-column2" style="width: 135px;" float: left;>'+ Msg.PRODUCT_COST +' '+'('+Msg.CURRENCY_FORMATE+')</div>'+
					'<div class="report-header-column2 centered" style="width: 130px;">'+  Msg.PRODUCT_CUSTOMER_COST +' '+'('+Msg.CURRENCY_FORMATE+')</div>'+
					'</div>';
		             $('#search-results-list').append(productListTable);
		             $('#search-results-list').append('<div class="grid-content" id="grid-content-id" style="height: 290px; width: 697px; overflow-y:initial;"></div>'); 
		            var count=0;
		             for(var loop=0;loop<data.length;loop=loop+1) {
								var productListRows ='<div id="product-row" class="ui-content report-content">'+
								    '<div class="report-body" id="row-'+ ProductCustomerCostHandler.count +'" style="width: 697px;height: auto; overflow: hidden;">'+
								    '<div class="report-body-column2 centered sameHeight" style="height: inherit; width: 80px; word-wrap: break-word;">' +  data[loop].id  + '</div>' +
									'<div id="product-name" class="report-body-column2 centered sameHeight" style="height: inherit; width: 130px; word-wrap: break-word;">' +  data[loop].productName  + '</div>' +
									'<div id="batch-number" class="report-body-column2 centered sameHeight" style="height: inherit; width: 130px; word-wrap: break-word;">' +  data[loop].batchNumber  + '</div>' +
									'<div class="report-body-column2 right-aligned sameHeight" style="height: inherit; width: 135px; word-wrap: break-word;">'+ currencyHandler.convertFloatToStringPattern(data[loop].costPerQuantity) +'</div>'+
									'<div id="customer-cost" class="report-body-column2 centered sameHeight" style="height: inherit; width: 130px; word-wrap: break-word;"><div id="product-customer-cost" class="input-field"><input name="customerCost" id="customerCost" style="border: none;"><span id="customerCost-'+ count +'" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span></div></div>'+
									'</div>'+
									'</div>';
									$('.grid-content').append(productListRows);
									if((data[loop].productName.length > 80) || (data[loop].batchNumber.length > 80)){
										ProductCustomerCostHandler.checkGridLength($('row-'+ProductCustomerCostHandler.count));
									   }else if((data[loop].productName.length > 50) || (data[loop].batchNumber.length > 50)){
										   ProductCustomerCostHandler.checkGridLength($('row-'+ProductCustomerCostHandler.count));
										}else if((data[loop].productName.length > 30) || (data[loop].batchNumber.length > 30)){
											ProductCustomerCostHandler.checkGridLength($('row-'+ProductCustomerCostHandler.count));
									   }else if((data[loop].productName.length > 15) || (data[loop].batchNumber.length > 15)){
										   ProductCustomerCostHandler.checkGridLength($('row-'+ProductCustomerCostHandler.count));
							           }
							           else{
							        	   ProductCustomerCostHandler.checkGridLength($('row-'+ProductCustomerCostHandler.count));
									   }
									ProductCustomerCostHandler.count=ProductCustomerCostHandler.count+1;
						};
		      $('.grid-content').jScrollPane({showArrows:true});
		      $('#search-results-list').jScrollPane({showArrows:false});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
		});	
		$('.grid-content').live("blur",function(){
			var count=0;
			 $.each($('.report-body'), function(index, value) {
				  var cost = $(this).find('#customerCost').val();
				  var spanId=$(this).find("span").attr("id");
				  if(cost != ''){
				  if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test(cost) == false || currencyHandler.convertStringPatternToFloat(($(this).find('#customerCost')).val()).toString().length > 10){
					  $('#customerCost-'+count).html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					}else{
						var productCost = currencyHandler.convertStringPatternToFloat($(this).find('#customerCost').val());
						var formatCustomerCost=currencyHandler.convertFloatToStringPattern(productCost.toFixed(2));
						$(this).find('#customerCost').val(formatCustomerCost);
						$("#customerCost-"+count).empty();
					}
				  }else{
				  }
				  count++;
			 });
		});
	},
	
	load:function() {
		$('#businessName').click(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			ProductCustomerCostHandler.suggestBusinessName(thisInput);
		});
		$('#businessName').keyup(function() {
			var thisInput = $(this);
			$('#business-name-suggestions').show();
			ProductCustomerCostHandler.suggestBusinessName(thisInput);
		});

		$('#businessName').focusout(function() {
			$('#businessNameValid_pop').hide();
			$('#business-name-suggestions').animate({
				display : 'none'
			}, 0, function() {
			});
		});
	},
	suggestBusinessName : function(thisInput) {
		var suggestionsDiv = $('#business-name-suggestions');
		var val = $('#businessName').val();
		$.post('productCustomerCost.json','action=get-business-name&businessNameVal=' + val,function(obj) {
							$.loadAnimation.end();
							suggestionsDiv.html('');
							var data = obj.result.data;
							if (data != undefined) {
								var htmlStr = '<div>';
								for ( var loop = 0; loop < data.length; loop = loop + 1) {
									htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
											+ data[loop].businessName
											+ '</a></li>';
								}
								htmlStr += '</div>';
								suggestionsDiv.append(htmlStr);
							} else {
								suggestionsDiv.append('<div id="" class="select-teacher">'
										+ 'No Business Names' + '</div>');
							}
							suggestionsDiv.css('left',
									thisInput.position().left);
							suggestionsDiv.css('top',
									thisInput.position().top + 25);
							suggestionsDiv.show();
							$('.select-teacher').click(
									function() {
										$('#businessNameValid').hide();
										$('#businessNameValid_pop').hide();
										thisInput.val($(this).html());
										thisInput.attr('businessName', $(this)
												.attr('businessName'));
										$('#businessName').attr('value',
												$(this).attr('businessName'));
										suggestionsDiv.hide();
									});
						});
	       },
	       checkGridLength: function(group1){
				$('#row-'+ProductCustomerCostHandler.count).each(function(index) {
			        var maxHeight = 0;
			        $(this).children().each(function(index) {
			            if($(this).height() > maxHeight) 
			                maxHeight = $(this).height();
			        });
			        $(this).children().height(maxHeight);
			    });    
			},
	       validateProductCustomerCost : function(){
	    	   var result=true;
	    	   $('#businessNameValid_pop').empty();
	    	   $('#businessNameIncorrect_pop').empty();
	    	   $('#businessNameEmpty_pop').empty();
	    		  if(($('#businessName').val()).length == 0){
		    		   $('#businessNameValid_pop').empty();
		    		   $('#businessNameIncorrect_pop').empty();
		    		   $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    		   $('#businessName').click(function(){
		    			   $('#businessNameValid').empty();
		    			   $('#businessNameEmpty_pop').show();
		    		   });
		    		   return false;
		    	   }
	    		   else{
		    		   $('.select-teacher').each(function(){
		    			   if($(this).html() !== $('#businessName').val()){
		    				   result=false;
		    			   }
		    		   });
		    		   $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		    			   $('#businessName').click(function(){
		    				   $('#businessNameValid').empty();
	    					   $('#businessNameValid').append('<div style="color:red">* Please select a valid Business name</div>');
	    				   });
		    			   return result;
		    	   }
	    	   return result;
	       },
	       addColor: function(number) {
	   		if(number%2 !=0){
	   			$('#row-'+number).css({'background-color' : 'LightGray'});
	   		}
	   		else
	   		$('#row-'+number).css({'background-color' : 'FloralWhite'});
	   		
	   	},
	   	checkLength: function(len,number,description,batchNo) {
			if(len>20||description>20||batchNo>15){
				$('#row-'+number).css({'height' : '30px'});
				$('.invoice-boxes-'+number).css({'height' : 'inherit'});
			
				if(len>=45||description>=45||batchNo>28){
					$('#row-'+number).css({'height' : '45px'});
				}
				if(len>=55||description>=55){
					$('#row-'+number).css({'height' : '60px'});
				}
				if(len>=70||description>=70){
					$('#row-'+number).css({'height' : '70px'});
				}
					
			}
	   	}
};