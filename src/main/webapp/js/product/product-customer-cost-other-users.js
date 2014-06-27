var ProductCustomerCostOtherUserHandler = {
		
		
		initPageLinks : function() {
			$('#product-search-customer-cost').pageLink({
				container : '.product-page-container',
				url : 'product/product-customer-cost/product_customer_cost_search_for_others.jsp'
			});
		},
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
							var dateFormat=ProductCustomerCostOtherUserHandler.formatDate(data[loop].createdDate);
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
							'<div id="'+data[loop].businessName+'" class="ui-btn btn-view" title="View Product Customer Cost Details"></div>'+
							
							'</div>'; 
							$('#search-results-list').append(rowstr);
				};
				ProductCustomerCostOtherUserHandler.initSearchResultButtons();
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
								'<div businessName="'+data[loop].businessName+'"& id="'+data[loop].id+'" class="ui-btn delete-icon delete-organization" title="Delete Product Customer Cost"></div>' + 
								'</div>'; 
								$('#search-results-list').append(rowstr);
					};
					ProductCustomerCostOtherUserHandler.initSearchResultButtons();
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
			    				 $('#error-message').html('You will loose entered data.. Clear form?');   
			    					$("#error-message").dialog({
			    						resizable: false,
			    						height:140,
			    						title: "<span class='ui-dlg-confirm'>Confirm</span>",
			    						modal: true,
			    						buttons: {
			    							'Ok' : function() {
			    								$('form').clearForm();
			    								$(this).dialog("close");
			    							},
			    							Cancel : function(){
			    								$(this).dialog('close');
			    							}
			    						}
			    					});
				    		  
							}
						},
						close: function() {
							$('#product-customer-cost-view-container').html('');
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
		
};