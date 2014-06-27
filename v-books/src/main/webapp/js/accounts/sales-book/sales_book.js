


var SalesBookHandler = {
		

 initPageLinks: function() {
		$('#sales-book-add').pageLink({container:'.sales-book-page-container', url:'sales-book/sales_book_add.jsp'});
		$('#sales-book-search').pageLink({container:'.sales-book-page-container', url:'sales-book/sales_book_search.jsp'});
		$('#sales-book-import').pageLink({container:'.sales-book-page-container', url:'sales-book/sales_book_import.jsp'});
},	
		
	
	salesBookSteps:['#sales-book-form','#sales-book-product-form'],
	salesBookUrl:['salesBook.json','salesBook.json'],
	salesBookStepCount: 0,
	
initAddButtons: function () {
	SalesBookHandler.salesBookStepCount = 0;
	

	$('#action-clear').click(function() {
		$(SalesBookHandler.salesBookSteps[SalesBookHandler.salesBookStepCount]).clearForm();
	});
	
	$('#button-next').click(function() {
		
		
		if($(SalesBookHandler.salesBookSteps[SalesBookHandler.salesBookStepCount]).validate()==false) return;
		var paramString = $(SalesBookHandler.salesBookSteps[SalesBookHandler.salesBookStepCount]).serialize();
		$.ajax({type: "POST",
			url:'salesBook.json', 
			data: paramString,
			success: function(data){
				$('#error-message').html('');
				$('#error-message').hide();
				$(SalesBookHandler.salesBookSteps[SalesBookHandler.salesBookStepCount]).hide();
				$(SalesBookHandler.salesBookSteps[++SalesBookHandler.salesBookStepCount]).show();
				if(SalesBookHandler.salesBookStepCount==SalesBookHandler.salesBookSteps.length) {
					$('#button-next').hide();
					$('#action-clear').hide();
					$('#button-save').show();
					$('#button-update').show();
					$.post('sales-book/sales_book_view.jsp', 'viewType=preview',
					        function(data){
								$('#sales-book-preview-container').css({'height':'350px'});
								$('#sales-book-preview-container').html(data);
								$('#sales-book-preview-container').show();
					        }
				        );
				}if(SalesBookHandler.salesBookStepCount>0) {
					$('#button-prev').show();
					$('.page-buttons').css('margin-left', '150px');
					
				} else {
					$('#button-prev').hide();
					$('.page-buttons').css('margin-left', '200px');
				}
		},
        error: function(data){
			$('#error-message').html(data.responseText);
			$('#error-message').dialog();
			$('#error-message').show();
		}
		});
	});
	$('#button-prev').click(function() {
		$('#action-clear').show();
		if(SalesBookHandler.salesBookStepCount==SalesBookHandler.salesBookSteps.length) {
			$('#button-next').show();
			$('#button-save').hide();
			$('#button-update').hide();
			$('#sales-book-preview-container').html('');
			$('#sales-book-preview-container').hide();
			$('.page-buttons').css('margin-left', '150px');
		}
		$(SalesBookHandler.salesBookSteps[SalesBookHandler.salesBookStepCount]).hide();
		$(SalesBookHandler.salesBookSteps[--SalesBookHandler.salesBookStepCount]).show();
		if(SalesBookHandler.salesBookStepCount>0) {
			$('#button-prev').show();
			$('.page-buttons').css('margin-left', '150px');
		} else {
			$('#button-prev').hide();
			$('.page-buttons').css('margin-left', '240px');
		}
	});
	$('#button-save').click(function() {
		var thisButton = $(this);
		var paramString = 'action=save-sales-book';
		$.post('salesBook.json', paramString,
	        function(obj){
				$(this).successMessage({container:'.sales-book-page-container', data:obj.result.message});
	        }
        );
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
	    			var container ='.sales-book-page-container';
	    			var url = "sales-book/sales_book_add.jsp";
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
		$('#button-update').click(function() {
			var thisButton = $(this);
			var paramString = $('#department-edit-form').serialize();
			$.post('salesBook.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.sales-book-page-container', data:obj.result.message});
		        }
		    );
		});

	},
	
	initSearchSalesBook : function (role) {
		$('#ps-exp-col').click(function() {
			setTimeout(function() {
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').jScrollPane({showArrows:true});
            }, 1000);
		});
		//button click - cancel
		$('#action-cancel').click(function() {
			
			$('#sales-book-search').click();		
		});
		$('#action-clear').click(function() {
			$('#sales-book-search-form').clearForm();
			$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},300);
		});
		
		//button click - search
		$('#action-sales-book-search').click(function() {
			var thisButton = $(this);
			var paramString = $('#sales-book-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			$.post('salesBook.json', paramString,
		        function(obj){
			    	var data = obj.result.data;
					$('#search-results-list').html('');
					if(data.length>0) {
						var alternate = false;

						for(var loop=0;loop<data.length;loop=loop+1) {
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate">';
							} else {
								rowstr = '<div class="green-result-row">';
							}
							alternate = !alternate;
							rowstr = rowstr + '<div class="green-result-col-1">'+
							'<div class="result-title">' + fmt(data[loop].salesExecutive) + '</div>' +
							'<div class="result-body">' +
							'<span class="property">'+ Msg.SALES_NUMBER +'</span><span class="property-value">' + fmt(data[loop].salesNumber) +'</span>' +
							'<span class="property">'+Msg.CREATED_DATE+' </span><span class="property-value">' + data[loop].createdOn + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-action">' + 
							'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales Book Details"></div>';+
							'</div>' +
							'</div>';
					$('#search-results-list').append(rowstr);
				};
				SalesBookHandler.initSearchResultButtons();
				$('#search-results-list').jScrollPane({showArrows:true});
					} else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
					}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},300);
					
		        }
		    );
		});
		$('#employee-exp-coll').click(function() {
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
	
	initSearchResultButtons : function () {

		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('sales-book/sales_book_profile_view.jsp', 'id='+id,
		        function(data){
				$('#sales-book-view-container').html(data);
					$("#sales-book-view-dialog").dialog('open');
		        }
	        );
		});

		$("#sales-book-view-dialog").dialog({
			autoOpen: false,
			height: 500,
			width: 500,
			modal: true,
			buttons: {
				Close: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				$('#sales-book-view-container').html('');
			}
		});
	}
	
};