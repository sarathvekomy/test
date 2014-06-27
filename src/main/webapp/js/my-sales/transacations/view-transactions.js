var ViewTransactionsHandler = {
		initPageLinks : function() {
			$('#view-transaction').pageLink({
				container : '.transaction-page-container',
				url : 'my-sales/transactions/view-transactions/delivery_note_default_view.jsp'
			});
			$('#view-day-book').pageLink({
				container : '.transaction-page-container',
				url : 'my-sales/transactions/view-transactions/day_book_search.jsp'
			});
			$('#search-journal').pageLink({
				container : '.transaction-page-container',
				url : 'my-sales/transactions/view-transactions/search_journals.jsp'
			});
			$('#view-sales-returns').pageLink({
				container : '.transaction-page-container',
				url : 'my-sales/transactions/view-transactions/sales_returns_view.jsp'
			});
		},
};
var DeliveryNoteViewHandler = {
		count: 1,
		//Function to display results on load
		  initSearchDeliveryNoteOnLoad : function() {
			  var paramString = $('#delivery-note-search-form').serialize(); 
			  $('#search-results-list').ajaxLoader();
		    $.post(
		      'deliveryNote.json',paramString,
		      function(obj) {
		       $('#default-results-list').html('');
		       DeliveryNoteViewHandler.displayDeliveryNoteSearchResults(obj);

		 });
		  },
		 displayDeliveryNoteSearchResults:function(obj){
			 var data = obj.result.data;
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
						'<div class="result-title">' + data[loop].invoiceNo + '</div>' +
						'<span class="property">Business Name:'+' </span><span class="property-value">' + data[loop].businessName + '</span>' +
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
			        $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+obj.result.message+'</div></div></div>');
			       }
			       $.loadAnimation.end();
			       setTimeout(function() {
			       }, 0);

			  $('#action-clear').click(function() {
				  $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('form').clearForm();
								$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
								setTimeout(function() {
									$('#search-results-list').jScrollPane({
										showArrows : true
									});
								}, 0);
								$(this).dialog('close');
							},
					Cancel: function() {
						$(this).dialog('close');
					}
						}
					});
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
		     $('#search-results-list').html('');
		     //for refreshing input fields after search
		     $('form').clearForm();
		     DeliveryNoteViewHandler.displayDeliveryNoteSearchResults(obj);
		   });
		  });
		 },

		 initDefaultResultButtons : function() {
		  $('.btn-view')
		    .click(
		      function() {
		       var id = $(this).attr('id');
		       $.post('deliveryNote.json','action=get-delivery-note-for-view&id='+id,function(obj){
		    	   var data=obj.result.data;
		    	   var deliveryNote='<div class="outline" style="margin-left: 10px;">'+
		    		'<div class="first-row" style="float:none !important;">'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Invoice No</span>'+
		    				'</div>'+
		    				'<div class="number">'+
		    					'<span class="property-value">'+
		    					'<a id="previous-delivery-note-invoice-number" href="#" class="'+ data[0].invoiceNo +'" align="'+ id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
		    					 data[0].invoiceNo
		    					 +'</a>'
		    					 +'</span>'+
		    				'</div>'+
		    			'</div>'+
		    			'<div class="right-align">'+
		    					'<div class="number-lable" style="margin-left:-18px;">'+
		    					'<span class="span-label">Date</span>'+
		    				'</div>'+
		    				'<div class="number" style="margin-left:110px;">'+
		    					'<span class="property-value">'+data[0].createdDate+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    		'</div>'+
		    		'<div class="first-row" style="width:960px;float:none !important;">'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Business Name</span>'+
		    				'</div>'+
		    				'<div class="number">'+
		    					'<span class="property-value">'+data[0].businessName+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Invoice Name</span>'+
		    				'</div>'+
		    				'<div class="number">'+
		    					'<span class="property-value">'+data[0].invoiceName+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    		'</div>';
		    	   if(data[0].product != undefined){
		    			 deliveryNote=deliveryNote+ '<div>'+
				    		'<div>';
		    			
		    		 deliveryNote= deliveryNote + 
		    				
		    			'<table  border="1" width="100%">'+
		    		      '<tr><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Product Cost ('+Msg.CURRENCY_FORMATE+')</td><td>Product Quantity</td><td>Bonus Quantity</td><td>Bonus Reason</td>'+
		    		     '<td>Total Cost ('+Msg.CURRENCY_FORMATE+')</td></tr>';
		    					var count=0;
		    					var alternateProduct = false;
		    						for(var i=0;i<data.length;i++)
		    					{
		    							
		    					count++;
		    					if(alternateProduct){
		    						deliveryNote= deliveryNote +
			    					'<tr style="background:#eaeaea;">';
		    					}else{
		    						deliveryNote= deliveryNote +
			    					'<tr style="background:#bababa;">';
		    					}
		    					alternateProduct= !alternateProduct;
		    					deliveryNote= deliveryNote +'<td>'+count+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+data[i].product+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+data[i].batchNumber+'</td>'+
		    	    		      '<td align="right">'+data[i].costProduct+'</td>'+
		    	    		      '<td align="right">'+data[i].qtyProduct+'</td>'+
		    	    		      '<td align="right">'+data[i].qtyBonus+'</td>'+
		    	    		      '<td style="word-wrap:break-word;">'+data[i].bonusReason+'</td>'+
		    	    		      '<td align="right">'+data[i].totalCostProduct+'</td>'+
		    	    		    '</tr>';
		    					}
		    						  
		    			deliveryNote=deliveryNote+ '</table></div>'+
		    		'</div>';
		    					
		    					}
		    		
		    			if (data != undefined) {
		    	
		    				deliveryNote= deliveryNote +'<div class="paymentset-row" style="margin-top: 10px;">'+
		    			'<div class="paymentset" style="height: 150px;">'+
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label" style="width: 90px;margin-left:2px;">Payment Type</div>'+
		    					'<div class="input-field" style="margin-left: 7px;">'+
		    						'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].paymentType+'</span>'+
		    					'</div>'+
		    				'</div>';
		    		
		    		if(! data[0].bankName == ""){
		    			deliveryNote= deliveryNote +'<div style="float:left; width:400px;">'+
		    			'<div  style="width: 90px;margin-left:2px; float:left; margin-right:10px; font-size:12px; color:#808080;font-weight: bold;">Bank Name</div>'+
    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].bankName+'</span>'+
    					'</div>'+
		    			'<div style="float:left; width:400px;">'+
    					'<div  style="width: 90px;margin-left:2px; float:left; margin-right:10px;font-size:12px; color:#808080;font-weight: bold;">Branch Name</div>'+
    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].branchName+'</span>'+
    					'</div>'+
    					'<div style="float:left; width:400px;">'+    					
    					'<div  style="width: 90px;margin-left:2px; float:left; margin-right:10px;font-size:12px; color:#808080;font-weight: bold;">Cheque No</div>'+
    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].chequeNo+'</span>'+
    					'</div>'+
    					'<div style="float:left; width:400px;">'+    					
    					'<div  style="width: 90px;margin-left:2px; float:left; margin-right:10px;font-size:12px; color:#808080;font-weight: bold;">Bank Location</div>'+
    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].bankLocation+'</span>'+
    					'</div>';
    				
		    		}
		    		       
		    				deliveryNote= deliveryNote +'<div class="separator" style="height: 80px; width: 300px;"></div>'+
		    				'<div class="form-row" style="margin-left: 2px; width: 280px;">'+
		    					'<div class="payment-span-label" style="width: 105px;">Sales Executive</div>'+
		    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].salesExecutive+'</span>'+
		    				'</div>'+ 
		    				
		    				'<div class="separator" style="height: 10px; width: 60px;"></div>'+
		    				'<div class="form-row" style="width: 300px; margin-left: 240px; margin-top: -40px;">'+
		    					'<div class="payment-span-label" style="margin-left: 40px; width: 80px;">Customer</div>'+
		    					'<span style="color: #1C8CF5; font: 12px arial;">'+data[0].businessName+'</span>'+
		    						
		    				'</div>'+
		    			'</div>'+
		    			
		    			'</div>'+
		    			'<div class="paymentset" style="float:right !important;">'+
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Present Payable</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].presentPayable+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Present Advance</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].presentAdvance+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Previous Credit</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].previousCredit+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Total Payable</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].totalPayable+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Present Payment</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].presentPayment+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Balance</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+data[0].balance+'</span>'+
		    					'</div>'+
		    				'</div>'+
		    				
		    				'<div style="float: left;margin-right:10px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in '+Msg.CURRENCY_FORMATE+'</i></span></div>'+
		    			'</div>'+
		    		'</div>';
		    		
		    			}
		    	
		    	'</div>';

		    			
		    			 $('#delivery-note-view-container').html(deliveryNote);
				         $("#delivery-note-view-dialog").dialog('open');
		    	   
		    	   
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
		  //------------------------------
		   //click of current delivery_note_view_page invoiceNumber display previous DN with details before CR of that delivery_note
		    var $anchor = $('.outline').find('a#previous-delivery-note-invoice-number');
		    $anchor.live('click',function(){
		    	var invoiceNumber=$(this).attr('class');
		    	var deliveryNoteId=$(this).attr('align');
		    	$.post('deliveryNote.json','action=get-previous-delivery-note-id&invoiceNumber='+ invoiceNumber +'&deliveryNoteId='+deliveryNoteId,
		    			function(obj){
      					var previousDeliveryNoteId = obj.result.data;
      					if(previousDeliveryNoteId != 0){
   						$.post('my-sales/transactions/view-transactions/delivery_note_results_view.jsp','id=' + previousDeliveryNoteId,
   						           function(data) {
   						            $('#delivery-note-previous-view-container-'+DeliveryNoteViewHandler.count).html(data);
   						            var dialogOpts = {
	        						     		   height : 550,
	        						     		   width : 1020,
   						            		buttons : {
	        						     		    Close : function() {
	        						     		     $(this).dialog('close');
	        						     		    }
	        						     		 },
   						            	};
   						            $("#delivery-note-previous-view-dialog-"+DeliveryNoteViewHandler.count).dialog(dialogOpts);
   						 });
   				}else{
   					$('.outline').find('a#previous-delivery-note-invoice-number').css('font-weight','normal');
   					$('.outline').find('a#previous-delivery-note-invoice-number').css('color','#00f');
   				}
      				DeliveryNoteViewHandler.count=DeliveryNoteViewHandler.count+1;	
      	    });
		   }); 
		    //id from jsp page
		    $('#change-request-dn-Invoice-number').live('click',function(){
		       var invoiceNumber=$(this).attr('class');
		    	var deliveryNoteId=$(this).attr('align');
		    	$.post('deliveryNote.json','action=get-previous-delivery-note-id&invoiceNumber='+ invoiceNumber +'&deliveryNoteId='+deliveryNoteId,
		    			function(obj){
     					var previousDeliveryNoteId = obj.result.data;
     					if(previousDeliveryNoteId != 0){
  						$.post('my-sales/transactions/view-transactions/delivery_note_results_view.jsp','id=' + previousDeliveryNoteId,
  						           function(data) {
  						            $('#delivery-note-previous-view-container-'+ DeliveryNoteViewHandler.count).html(data);
  						            var dialogOpts = {
	        						     		   height : 550,
	        						     		   width : 1020,
  						            		buttons : {
	        						     		    Close : function() {
	        						     		     $(this).dialog('close');
	        						     		    }
	        						     	  },
  						            	};
  						            $("#delivery-note-previous-view-dialog-"+ DeliveryNoteViewHandler.count).dialog(dialogOpts);
  						 });
  				}else{
  					$('#change-request-dn-Invoice-number').css('font-weight','normal');
  					$('#change-request-dn-Invoice-number').css('color','#00f');
  				}
     					DeliveryNoteViewHandler.count=DeliveryNoteViewHandler.count+1;
     	      });
		    });
		 }
		   //---------------------------------
		};
var DayBookViewHandler = {
		count: 1,
		displaySearchResults : function(data){
			if(data!=null)  {
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1) {
					var dateFormat=DayBookViewHandler.formatDate(data[loop].createdOn);
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate" >';
					} else {
						rowstr = '<div class="green-result-row" >';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-body">' +
					'<div class="result-title">'+  data[loop].invoiceNo +'</div>' +
					'</div>'+
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
				$('#search-results-list').jScrollPane({showArrows : true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+'No Records Found'+'</div></div></div>');
			}
			$.loadAnimation.end();
			 setTimeout(function() {
			    $('#search-results-list').jScrollPane({showArrows : true});
			 }, 0);
			 $('#action-clear')
				.click(
						function() {
							$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
							$("#error-message").dialog({
								resizable: false,
								height:140,
								title: "<span class='ui-dlg-confirm'>Confirm</span>",
								modal: true,
								buttons: {
									'Ok' : function() {
										$('#createdOn').clearForm();
										$('#search-results-list')
												.html(
														'<tr><td colspan="4">Search Results will be show here</td></tr>');
										setTimeout(function() {
											$('#search-results-list').jScrollPane({
												showArrows : true
											});
										}, 0);
										$(this).dialog('close');
									},
							Cancel: function() {
								$(this).dialog('close');
							}
								}
							});
						});
		},
		initSearchDayBookOnLoad: function() {
			$("#executive-allowances-view-dialog").dialog({
				autoOpen: false,
				height: 455,
				width: 850,
				modal: true,
				buttons: {
					Close: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$('#executive-allowances-view-container').html('');
				}
			});
			$('#search-results-list').ajaxLoader();
			var paramString = $('#day-book-search-form').serialize();
			$.post('dayBook.json', paramString,function(obj){
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
				var dayBookType = 'Deposit Amount';
				var id = $(this).attr('id');
				var paramString='action=get-day-book-for-view&id='+id;
				$.post('dayBook.json',paramString,function(obj){
					var data=obj.result.data;
					var dayBookInvoice = '<div class="dayBookInvoice">';
					dayBookInvoice = dayBookInvoice+'<div class="outline" style="width: 820px; margin-left: 10px;">'+
					
					'<div class="first-row" style="width: 820px;float:none !important;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Day Book No</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+
					'<a id="previous-day-book-number" href="#" class="'+ data[0].dayBookNo +'" align="'+ id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
					 data[0].dayBookNo
					 +'</a>'
					+'</span>'+
					'</div>'+
					'</div>';
					if(data[0].reportingManager != 0){
						dayBookInvoice = dayBookInvoice+'<div class="right-align" style="float:right">'+
						'<div class="number-lable">'+
						'<span class="span-label"  style="width:160px !important;">Reporting Manager</span>'+
						'</div>'+
						'<div class="number">'+
						'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].reportingManager+'</span>'+
						'</div>'+
						'</div>';
					}
					dayBookInvoice = dayBookInvoice+'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Sales Executive</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].salesExecutive+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Start Date</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].startDate+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="right-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Created Date</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].createdDate+'</span>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<div class="first-row" style="width:820px;border-bottom: none;float:none !important;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Opening Balance</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].openingBalance+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="right-align" class="totalExpenses">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;" ><a href="#" onclick="DayBookViewHandler.calling('+id+');" id="totalExpenses" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">Total Expenses</a></span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].totalExpenses+'</span>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<div class="all-expenses-'+id+'" style="display:none; line-height:26px; " title="Expenses">'+
					'<div style="float:left; width:255px;color:#808080;">Driver Allowances</div>'+
					'<div style="color:#1C8CF5;">'+data[0].driverAllowances+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Executive Allowances</div>'+
					'<div style="color:#1C8CF5;">'+data[0].executiveAllowances+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Vehicle Fuel Expenses</div>'+
					'<div style="color:#1C8CF5;">'+data[0].vehicleFuelExpenses+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Vehicle Maintainance Expenses</div>'+
					'<div style="color:#1C8CF5;">'+data[0].vehicleMaintenanceExpenses+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Offloading Loading Charges</div>'+
					'<div style="color:#1C8CF5;">'+data[0].offloadingLoadingCharges+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Dealer Party Expenses</div>'+
					'<div style="color:#1C8CF5;">'+data[0].dealerPartyExpenses+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Municipality Council</div>'+
					'<div style="color:#1C8CF5;">'+data[0].municipalCityCouncil+'</div>'+
					'<div style="float:left; width:255px;color:#808080;">Miscellaneous Expenses</div>'+
					'<div style="color:#1C8CF5;">'+data[0].miscellaneousExpenses+'</div>'+
					'</div>'+
					'<div class="first-row" style="width:820px;height: auto;margin-bottom:20px;float:none !important;">'+
					'</div>'+
					'<div class="first-row" style="width:820px;float:none !important;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Total Payable</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].totalPayable+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="center-align">'+
					'<div class="number-lable" >'+
					'<span class="span-label" style="width:160px !important;">Total recieved</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].totalRecieved+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="right-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Balance</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].balance+'</span>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<div class="first-row" style="width:820px;margin-bottom:15px;border-bottom:none;float:none !important;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px;"><a href="#" onclick="DayBookViewHandler.getAmountToBankData('+id+');" id="amounToBank" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">Amount To Bank</a></span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+ currencyHandler.convertFloatToStringPattern(data[0].amountToBank)+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="amount-to-bank-'+id+'"style="display:none; line-height:26px;" title="Deposited Amounts">';
					if(data[0].depositedAmounts != null){
						for(var loop = 0;loop<data[0].depositedAmounts.length ; loop=loop+1){
							var amountToBank=currencyHandler.convertStringPatternToFloat(data[0].depositedAmounts[loop]);
							var bankAmount=currencyHandler.convertFloatToStringPattern(amountToBank.toFixed(2));
							dayBookInvoice=	dayBookInvoice+
							'<div style="float:left; width:255px;color:#808080;">Amount To Bank</div>'+
							'<div style="color:#1C8CF5;">'+bankAmount+'</div>';
						};
					}else{
						var amount=currencyHandler.convertStringPatternToFloat(data[0].amountToBank);
						var bankAmount=currencyHandler.convertFloatToStringPattern(amount.toFixed(2));
						dayBookInvoice=	dayBookInvoice+
						'<div style="float:left; width:255px;color:#808080;">Amount To Bank</div>'+
						'<div style="color:#1C8CF5;">'+amount+'</div>';
					}
					
					dayBookInvoice=	dayBookInvoice+'</div>';
					if(data[0].reportingManager != 0){
					dayBookInvoice=	dayBookInvoice+'<div class="center-align" >'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Amount To Factory</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].amountToFactory+'</span>'+
					'</div>'+
					'</div>';
					}
					dayBookInvoice=	dayBookInvoice+'<div class="right-align">'+
					'<div class="number-lable">'+
					'<span class="span-label"  style="width:160px !important;">Closing Balance</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].closingBalance+'</span>'+
					'</div>'+
					'</div>'+
					'</div>';
					
					if(data != undefined){
						dayBookInvoice = dayBookInvoice+ '<div>'+
			    		'<div>';
	 			
					dayBookInvoice = dayBookInvoice+ 
	 				
	 			'<table  border="1" width="100%">'+
	 		      '<tr><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Opening Stock</td><td>Products To Customer</td><td>Products To Factory</td><td>Return Quantity</td>'+
	 		     '<td>Closing Stock</td></tr>';
	 					var count=0;
	 					var alternateProduct = false;
	 						for(var i=0;i<data.length;i++)
	 					{
	 					count++;
	 					if(alternateProduct){
	 						dayBookInvoice = dayBookInvoice+
		    					'<tr style="background:#eaeaea;">';
	 					}else{
	 						dayBookInvoice = dayBookInvoice+
		    					'<tr style="background:#bababa;">';
	 					}
	 					alternateProduct= !alternateProduct;
	 					dayBookInvoice = dayBookInvoice+'<td>'+count+'</td>'+
	 	    		    '<td style="word-wrap:break-word;">'+data[i].product+'</td>'+
	 	    		    '<td style="word-wrap:break-word;">'+data[i].batchNumber+'</td>'+
	 	    		     '<td align="right">'+data[i].openingStock+'</td>'+
	 	    		      '<td align="right">'+data[i].productsToCustomer+'</td>'+
	 	    		      '<td align="right">'+data[i].productsToFactory+'</td>'+
	 	    		      '<td align="right">'+data[i].returnQty+'</td>'+
	 	    		      '<td align="right">'+data[i].closingStock+'</td>'+
	 	    		    '</tr>';
	 					}
	 						  
	 						dayBookInvoice = dayBookInvoice+'</table></div>'+
	 		'</div>';
				}
					
					if(data != undefined){ 
						dayBookInvoice = dayBookInvoice+'<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px; line-height:20px;">'+
						
						'<div class="left-align">'+
										
						'<div class="first-row" style="width:820px;border-bottom:none;height: auto;margin-bottom:50px; line-height:20px;">'+
						'<div style="width:820px; float:left; padding-bottom:15px;">'+
							'<div class="left-align">'+
							
								'<div class="number-lable">'+
									'<span class="span-label"  style="width:160px !important;"">Basic Info Remarks</span>'+
								'</div>'+
								'<div class="number" style="margin-left: 163px;">'+
									'<div class="input-field-preview">'+data[0].basicInfoRemarks+'</div>'+
								'</div></div>'+
							
							'</div>'+
							
							'<div style="width:820px; float:left; padding-bottom:15px;">'+
							'<div class="left-align">'+
								'<div class="number-lable" style="width:160px !important;">'+
									'<span class="span-label" style="width:160px !important;">Vehicle Details Remarks</span>'+
								'</div>'+
								'<div class="number" style="margin-left: 163px;">'+
									'<div class="input-field-preview">'+data[0].vehicleDetailRemarks+'</div>'+
								'</div>'+
							'</div>'+
							'</div>'+
						'<div style="width:820px; float:left; padding-bottom:15px;">'+
							'<div class="left-align">'+
								'<div class="number-lable" style="width:160px !important;">'+
									'<span class="span-label" style="width:150px !important;">Allowances Remarks</span>'+
								'</div>'+
								'<div class="number" style="margin-left: 163px;">'+
									'<div class="input-field-preview">'+data[0].allowancesRemarks+'</div>'+
								'</div>'+
							'</div>'+
							'</div>'+
						'<div style="width:820px; float:left; padding-bottom:15px;">'+
							'<div class="left-align">'+
								'<div class="number-lable"  style="width:160px !important;">'+
									'<span class="span-label">Amount Remarks</span>'+
								'</div>'+
								'<div class="number" style="margin-left: 163px;">'+
									'<div class="input-field-preview">'+data[0].amountRemarks+'</div>'+
								'</div>'+
							'</div>'+
							'</div>'+
						'<div style="width:820px; float:left; padding-bottom:15px;">'+
							'<div class="left-align">'+
								'<div class="number-lable"  style="width:160px !important;">'+
									'<span class="span-label">Product Remarks</span>'+
								'</div>'+
								'<div class="number" style="margin-left: 163px;">'+
									'<div class="input-field-preview">'+data[0].productsRemarks+'</div>'
								'</div>'+
								'</div>'+
							'</div>';
							
						
					
						dayBookInvoice = dayBookInvoice+'<div class="first-row" style="width:820px;border-bottom:none; margin-left:-163px; margin-top:15px;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Vehicle Number</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].vehicleNo+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="right-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Starting Reading</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].startingReading+'</span>'+
					'</div>'+
					'</div>'+				
					'</div>'+
					'<div class="first-row" style="width:820px;border-bottom:none; margin-left:-163px;">'+
					'<div class="left-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Driver Name</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].driverName+'</span>'+
					'</div>'+
					'</div>'+
					'<div class="right-align">'+
					'<div class="number-lable">'+
					'<span class="span-label" style="width:160px !important;">Ending Reading</span>'+
					'</div>'+
					'<div class="number">'+
					'<span class="property-value" Style="padding-left: 10px;padding-top: 3px;">: '+data[0].endingReading+'</span>'+
					'</div>'+
					'</div>'+
					'<div style="float: right;margin-right: 100px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in '+Msg.CURRENCY_FORMATE+'</i></span></div>'+
					'</div>';
					
					
					
					}
					//'</div>';
					$('#day-book-view-container').html(dayBookInvoice);
						$("#day-book-view-dialog").dialog('open');
				});
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
			 //------------------------------
			   //click of current day_book_view_page dayBookNumber display previous DayBook with details before CR of that day_book
			    var $anchor = $('.outline').find('a#previous-day-book-number');
			    $anchor.live('click',function(){
			    	var dayBookNumber=$(this).attr('class');
			    	var dayBookId=$(this).attr('align');
			    	$.post('dayBook.json','action=get-previous-day-book-id&dayBookNumber='+ dayBookNumber +'&dayBookId='+dayBookId,
			    			function(obj){
	      					var previousDayBookId = obj.result.data;
	      					if(previousDayBookId != 0){
	   						$.post('my-sales/transactions/change-transactions/day_book_change_transaction_results_view.jsp','id=' + previousDayBookId,
	   						           function(data) {
	   						            $('#day-book-previous-view-container-'+DayBookViewHandler.count).html(data);
	   						            var dialogOpts = {
		        						     		   height : 550,
		        						     		   width : 900,
	   						            		buttons : {
		        						     		    Close : function() {
		        						     		     $(this).dialog('close');
		        						     		    }
		        						     		 },
	   						            	};
	   						            $("#day-book-previous-view-dialog-"+DayBookViewHandler.count).dialog(dialogOpts);
	   						 });
	   				}else{
	   					$('.outline').find('a#previous-day-book-number').css('font-weight','normal');
	   					$('.outline').find('a#previous-day-book-number').css('color','#00f');
	   				}
	      					DayBookViewHandler.count=DayBookViewHandler.count+1;	
	      	    });
			   }); 
			    //id from jsp page
			    $('#change-request-daybook-number').live('click',function(){
			       var dayBookNumber=$(this).attr('class');
			    	var dayBookId=$(this).attr('align');
			    	$.post('dayBook.json','action=get-previous-day-book-id&dayBookNumber='+ dayBookNumber +'&dayBookId='+dayBookId,
			    			function(obj){
	     					var previousDayBookId = obj.result.data;
	     					if(previousDayBookId != 0){
	  						$.post('my-sales/transactions/change-transactions/day_book_change_transaction_results_view.jsp','id=' + previousDayBookId,
	  						           function(data) {
	  						            $('#day-book-previous-view-container-'+ DayBookViewHandler.count).html(data);
	  						            var dialogOpts = {
		        						     		   height : 550,
		        						     		   width : 1020,
	  						            		buttons : {
		        						     		    Close : function() {
		        						     		     $(this).dialog('close');
		        						     		    }
		        						     	  },
	  						            	};
	  						            $("#day-book-previous-view-dialog-"+ DayBookViewHandler.count).dialog(dialogOpts);
	  						 });
	  				}else{
	  					$('#change-request-day-book-number').css('font-weight','normal');
	  					$('#change-request-day-book-number').css('color','#00f');
	  				}
	     					DayBookViewHandler.count=DayBookViewHandler.count+1;
	     	      });
			    });
		},
			 
		calling:function(id) {
			$('.all-expenses-'+id).dialog({
				height: 400,
				width: 400,
				modal:true,
				buttons: {
					Close: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$(this).dialog('close');
				}
			});
		},
		/*getAmountToBankData : function(){

			$("#executive-allowances-view-dialog").dialog({
				height: 400,
				width: 400,
				modal:true,
				buttons: {
					Close: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$(this).dialog('close');
				}
			});
			
			DayBookViewHandler.trackAmountData(dayBookType);
		
		},*/
		getAmountToBankData : function(id){
			$('.amount-to-bank-'+id).dialog({
				height: 400,
				width: 400,
				modal:true,
				buttons: {
					Close: function() {
						$(this).dialog('close');
					}
				},
				close: function() {
					$(this).dialog('close');
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
var SalesReturnViewHandler = {
		count: 1,
		//common function for displaying search results
		displaySearchResults:function(obj){
			var data=obj.result.data;
			if (data!=undefined) {
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1) {
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate" style="height:46px !important;">';
					} else {
						rowstr = '<div class="green-result-row" style="height:46px !important;">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-body">' +
					'<div class="result-title">' + data[loop].invoiceNo + '</div>' +
					'<div class="result-body">' +
					'<span class="property">Business Name:'+' </span><span class="property-value">' + data[loop].businessName + '</span>' +
					'</div>'+
					/*'<div class="result-body">' +
					'<span class="property">'+Msg.SALES_RETURNS_CREATED_DATE +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
					'</div>'+*/
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					//'<div class="result-body">' +
					'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST +' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + currencyHandler.convertFloatToStringPattern(data[loop].total) + '</span>'+
					'<div class="result-body">' +
					'<span class="property">'+Msg.SALES_RETURNS_CREATED_DATE +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
					'</div>'+
					//'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
					//'</div>' +
					'<div class="green-result-col-action" style="height:34px !important;float:right;margin-right:-118px;margin-top:-35px;">' + 
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Sales Returns"></div>';
					'</div>' +
					'</div>';
					$('#search-results-list').append(rowstr);
				};
				SalesReturnViewHandler.initSearchSalesReturnResultButtons();
				$('#search-results-list').jScrollPane({showArrows:true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
			}
			$.loadAnimation.end();
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},0);
			 $('#action-clear')
				.click(
						function() {
							$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
							$("#error-message").dialog({
								resizable: false,
								height:140,
								title: "<span class='ui-dlg-confirm'>Confirm</span>",
								modal: true,
								buttons: {
									'Ok' : function() {
										$('form').clearForm();
										$('#search-results-list')
												.html(
														'<tr><td colspan="4">Search Results will be show here</td></tr>');
										setTimeout(function() {
											$('#search-results-list').jScrollPane({
												showArrows : true
											});
										}, 300);
										$(this).dialog('close');
									},
							Cancel: function() {
								$(this).dialog('close');
							}
								}
							});
						});
		},
		//View sales return On page load
		initSearchSalesReturnOnLoad: function() {
			var paramString = $('#sales-return-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			$.post('salesReturn.json', paramString, function(obj){
				$('#search-results-list').html('');
				SalesReturnViewHandler.displaySearchResults(obj);
			});
		},
			//End of search sales returns on load function
			
		
			initSearchSalesReturn : function(role) {
				//Stock search button click
				$('#action-search-sales-return').click(function() {
					var thisButton = $(this);
					var paramString = $('#sales-return-search-form').serialize();  
					$('#search-results-list').ajaxLoader();
					$.post('salesReturn.json', paramString, function(obj){
							$('#search-results-list').html('');
							//for refreshing input fields after search
							$('form').clearForm();
							SalesReturnViewHandler.displaySearchResults(obj);
					});
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
									$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
									$("#error-message").dialog({
										resizable: false,
										height:140,
										title: "<span class='ui-dlg-confirm'>Confirm</span>",
										modal: true,
										buttons: {
											'Ok' : function() {
												$('#sales-return-search-form').clearForm();
												$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
												setTimeout(function() {
													$('#search-results-list').jScrollPane({
														showArrows : true
													});
												}, 300);
												$(this).dialog('close');
											},
									Cancel: function() {
										$(this).dialog('close');
									}
										}
									});
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
					$.post('salesReturn.json', 'action=get-sales-return-for-view&id='+id,
					        function(obj){
						var data=obj.result.data;
						var salesReturnInvoice = '<div class="salesReturnInvoice">';
						salesReturnInvoice = salesReturnInvoice+'<div class="outline" style="margin-left:0px;">'+
						'<div class="first-row" style="float:none !important;">'+
						'<div class="left-align">'+
						'<div class="number-lable">'+
						'<span class="span-label">Invoice No</span>'+
						'</div>'+
						'<div class="number">'+
    					'<span class="property-value">'+
    					'<a id="previous-sales-return-invoice-number" href="#" class="'+ data[0].invoiceNo +'" align="'+ id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
    					 data[0].invoiceNo
    					 +'</a>'
    					 +'</span>'+
    				     '</div>'+
						'</div>'+
						'<div class="right-align">'+
						'<div class="number-lable" style="margin-left:-18px;">'+
						'<span class="span-label">Date</span>'+
						'</div>'+
						'<div class="number" style="margin-left:110px;">'+
						'<div class="property-value">: '+data[0].createdDate+'</div>'+
						'</div>'+
						'</div>'+
						'</div>'+
						
						'<div class="first-row" style="width:960px;float:none !important;">'+
						'<div class="left-align">'+
						'<div class="number-lable">'+
						'<span class="span-label">Business Name</span>'+
						'</div>'+
						'<div class="number">'+
						'<div class="property-value">: '+data[0].businessName+'</div>'+
						'</div>'+
						'</div>'+
						'<div class="left-align">'+
						'<div class="number-lable">'+
						'<span class="span-label">Invoice Name</span>'+
						'</div>'+
						'<div class="number">'+
						'<div class="property-value">: '+data[0].invoiceName+'</div>'+
						'</div>'+
						'</div>'+
						'</div>'+
						'<div class="right-align" style="margin-right:16px;">'+
						'<div class="number-lable">'+
						'<span class="span-label">Status</span>'+
						'</div>'+
						'<div class="number">'+
						'<div class="property-value">: '+data[0].status+'</div>'+
						'</div>'+
						'</div>';
						if (data != undefined) {
							
							salesReturnInvoice = salesReturnInvoice+ '<div>'+
				    		'<div>';
							if(data[0].status =='approved'){
								salesReturnInvoice = salesReturnInvoice+
				    				
				    			'<table  border="1" width="100%">'+
				    		      '<tr><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Unit Cost ('+Msg.CURRENCY_FORMATE+')</td><td>Damaged</td><td>Resalable</td>'+
				    		     '<td>Damaged Cost ('+Msg.CURRENCY_FORMATE+')</td><td>Resalable Cost ('+Msg.CURRENCY_FORMATE+')</td><td>Total quantity</td><td>Total Cost ('+Msg.CURRENCY_FORMATE+')</td></tr>';
							}else{
								salesReturnInvoice = salesReturnInvoice+ 
				    				
				    			'<table  border="1" width="100%">'+
				    		      '<tr><td>S.No</td><td>Product Name</td><td>Batch No</td><td>Damaged</td><td>Resalable</td><td>Total quantity</td>'+
				    		     '</tr>';
								
							}
							var count=0;
							var total=0;
							var alternateProduct = false;
							for(var i=0;i<data.length;i++){
								count++;
								var productName=data[i].product;
							var cost=data[i].totalCost;
							total=total+cost;
							if(data[i].status =='approved'){
								if(alternateProduct){
									salesReturnInvoice= salesReturnInvoice +
			    					'<tr style="background:#eaeaea;">';
		    					}else{
		    						salesReturnInvoice= salesReturnInvoice +
			    					'<tr style="background:#bababa;">';
		    					}
		    					alternateProduct= !alternateProduct;
		    					salesReturnInvoice= salesReturnInvoice +'<td>'+count+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+productName+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+data[i].batchNumber+'</td>'+
		    	    		    '<td align="right">'+data[i].productCost+'</td>'+
		    	    		     '<td align="right">'+data[i].damaged+'</td>'+
		    	    		      '<td align="right">'+data[i].resalable+'</td>'+
		    	    		      '<td align="right">'+data[i].damageCost+'</td>'+
		    	    		      '<td align="right">'+data[i].resaleCost+'</td>'+
		    	    		      '<td align="right">'+data[i].totalQty+'</td>'+
		    	    		      '<td align="right">'+data[i].productTotalCost+'</td>'+
		    	    		    '</tr>';
								
							}else{
								
								if(alternateProduct){
									salesReturnInvoice= salesReturnInvoice +
			    					'<tr style="background:#eaeaea;">';
		    					}else{
		    						salesReturnInvoice= salesReturnInvoice +
			    					'<tr style="background:#bababa;">';
		    					}
		    					alternateProduct= !alternateProduct;
		    					salesReturnInvoice= salesReturnInvoice +'<td>'+count+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+productName+'</td>'+
		    	    		    '<td style="word-wrap:break-word;">'+data[i].batchNumber+'</td>'+
		    	    		     '<td align="right">'+data[i].damaged+'</td>'+
		    	    		      '<td align="right">'+data[i].resalable+'</td>'+
		    	    		      '<td align="right">'+data[i].totalQty+'</td>'+
		    	    		    '</tr>';
								
							}
				    							
							}		
				    						  
				    					salesReturnInvoice=salesReturnInvoice+ '</table></div>'+
				    		'</div>';
						}						
						salesReturnInvoice = salesReturnInvoice+'<div class="first-row" style="width:799px;border-bottom:none;height: auto; word-wrap:break-word; min-height:30px;margin-bottom:15px;">'+
						'<div class="left-align">'+
						'<div class="number-lable">'+
							'<span class="span-label">Remarks</span>'+
						'</div>'+
						'<div class="number" style="">'+
							'<span style="color: #1C8CF5;font:12px arial;">'+data[0].remarks+'</span>'+
						'</div>'+
					'</div>'+
				'</div>' +
						
						
						'<div class="paymentset-row" style="margin-top: 10px;">'+
						'<div class="paymentset" style="height:90px;float:left; width:100% !important;">'+
						
						
						
						
						'<div class="form-row" style="margin-left: 2px; width: 300px;">'+
						'<span class="payment-span-label" style="width:100px;">Sales Executive</span>'+
						'<div style="color: #1C8CF5; font: 12px arial;">'+data[0].salesExecutive+'</div>'+
						'<div style="float: right;margin-right:-545px;margin-bottom: 2px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in  '+Msg.CURRENCY_FORMATE+'</i></span></div>'+
						'</div>';
						if(data[0].status =='approved'){
							salesReturnInvoice = salesReturnInvoice+
							'<div class="paymentset" style="margin-left: 18px;height:100px; margin-top:-25px; float:right !important;">'+
							'<div class="form-row" style="margin-left:-10px !important;">'+
							'<span class="payment-span-label">Total</span>'+
							'<div class="input-field"><div class="appostaphie"></div>'+
							'<div id="total" class="span-payment" style="float:left !important;">: '+data[0].productsGrandTotal+'</div>'+
							'</div>'+
							'</div>'+
							'</div>';
						}else{
							$('.inner-table').css('width','571px !important');
						}
						
						
						
						
						salesReturnInvoice = salesReturnInvoice+'<div class="separator" style="height: 10px; width: 60px;"></div>'+
						'<div class="form-row" style="width: 300px; ">'+
						'<span class="payment-span-label" style="margin-left: 40px; width: 80px;">Customer</span>'+
						'<div style="color: #1C8CF5; font: 12px arial;">'+data[0].businessName+'</div>';
						'</div>'+
						'</div>'+
					    '</div>';
						
						$('#sales-return-view-container').html(salesReturnInvoice);
					//This is the view Representation for sales return view .
						$("#sales-return-view-dialog").dialog('open');
					        });
				});
				$("#sales-return-view-dialog").dialog({
					autoOpen: false,
					overflow: 'hidden',
					height: 500,
					width: 1010,
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
				 //------------------------------
				   //click of current sales_return_view_page invoiceNumber display previous SR with details before CR of that sales_return
				   var $anchor = $('.outline').find('a#previous-sales-return-invoice-number');
				    $anchor.live('click',function(){
				    	var invoiceNumber=$(this).attr('class');
				    	var salesReturnId=$(this).attr('align');
				    	$.post('salesReturn.json','action=get-previous-sales-return-id&invoiceNumber='+ invoiceNumber +'&salesReturnId='+salesReturnId,
				    			function(obj){
		      					var previousSalesReturnId = obj.result.data;
		      					if(previousSalesReturnId != 0){
		   						$.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp','id=' + previousSalesReturnId,
		   						           function(data) {
		   						            $('#sales-return-previous-view-container-'+SalesReturnViewHandler.count).html(data);
		   						            var dialogOpts = {
			        						     		   height : 550,
			        						     		   width : 1020,
		   						            		buttons : {
			        						     		    Close : function() {
			        						     		     $(this).dialog('close');
			        						     		    }
			        						     		 },
		   						            	};
		   						            $("#sales-return-previous-view-dialog-"+SalesReturnViewHandler.count).dialog(dialogOpts);
		   						 });
		   				}else{
		   					$('.outline').find('a#previous-sales-return-invoice-number').css('font-weight','normal');
		   					$('.outline').find('a#previous-sales-return-invoice-number').css('color','#00f');
		   				}
		      					SalesReturnViewHandler.count=SalesReturnViewHandler.count+1;	
		      	    });
				   }); 
				    //id from jsp page
				    $('#change-request-sr-Invoice-number').live('click',function(){
				       var invoiceNumber=$(this).attr('class');
				    	var salesReturnId=$(this).attr('align');
				    	$.post('salesReturn.json','action=get-previous-sales-return-id&invoiceNumber='+ invoiceNumber +'&salesReturnId='+salesReturnId,
				    			function(obj){
		     					var previousSalesReturnId = obj.result.data;
		     					if(previousSalesReturnId != 0){
		  						$.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp','id=' + previousSalesReturnId,
		  						           function(data) {
		  						            $('#sales-return-previous-view-container-'+ SalesReturnViewHandler.count).html(data);
		  						            var dialogOpts = {
			        						     		   height : 550,
			        						     		   width : 1020,
		  						            		buttons : {
			        						     		    Close : function() {
			        						     		     $(this).dialog('close');
			        						     		    }
			        						     	  },
		  						            	};
		  						            $("#sales-return-previous-view-dialog-"+ SalesReturnViewHandler.count).dialog(dialogOpts);
		  						 });
		  				}else{
		  					$('#change-request-sr-Invoice-number').css('font-weight','normal');
		  					$('#change-request-sr-Invoice-number').css('color','#00f');
		  				}
		     					SalesReturnViewHandler.count=SalesReturnViewHandler.count+1;
		     	      });
				    });
				   //---------------------------------
			},
			//End of initSearchSalesReturnResultButtons
			checkLength:function(count){
				$('#row-'+count).each(function(index) {
			        var maxHeight = 0;
			        $(this).children().each(function(index) {
			            if($(this).height() > maxHeight) 
			                maxHeight = $(this).height();
			        });
			        $(this).children().height(maxHeight);
			        //$(this).children().css('margin-top',maxHeight/2+'px');
			    });    
			}
		
		
};
var JournalViewHandler = {
		count: 1,
		searchJournalOnLoad:function(){
			 var paramString = $('#journal-search-form').serialize(); 
		    $.post(
		      'journal.json',paramString,
		      function(obj) {
		       $('#default-results-list').html('');
		       JournalViewHandler.displayJournalSearchResults(obj);

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
			        $('#search-results-list').html('');
			        JournalViewHandler.displayJournalSearchResults(obj);
			     //for refreshing input fields after search
			     $('#businessName').clear();
			     $('#createdOn').clear();
			     

			   });
			  });
			 },
		displayJournalSearchResults:function(obj){
			var data = obj.result.data;
			if (data!=null) {
		        var alternate = false;
		        for(var loop=0;loop<data.length;loop=loop+1) {
		        	var dateFormat=DayBookViewHandler.formatDate(data[loop].createdOn);
		        	var amnt=data[loop].amount;
		        	var parsedAmount=parseFloat(Math.round(amnt * 100) / 100).toFixed(2);
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate"  style="height : 53px !important">';
					} else {
						rowstr = '<div class="green-result-row" style="height : 53px !important">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-body">' +
					'<div class ="result-title">'+ data[loop].invoiceNo +'</div>'+
					'<span class="property">Business Name:'+' </span><span class="property-value">' + data[loop].businessName + '</span>' +
					'<div class="result-body">' +
					'<span class="property">'+Msg.JOURNAL_TYPE +':'+' </span><span class="property-value">' + data[loop].journalType + '</span>' +
					'</div>'+
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +'<span class="property">'+Msg.JOURNAL_CREATED_DATE+':'+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + dateFormat + '</span>' +
					'</div>' +'<span class="property">'+Msg.JOURNAL_AMOUNT +' '+'<span class="property-value">'+'('+Msg.CURRENCY_FORMATE+')'+'</span>'+':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span>' +
					'</div>' +
					'<div class="green-result-col-action"  style="height :41px !important">' + 
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Journal"></div>';
					'</div>' +
					'</div>';
			$('#search-results-list').append(rowstr);
		};
		JournalViewHandler.initDefaultResultButtons();
		        $('#search-results-list').jScrollPane({
					showArrows : true
				});
		       } else {
		        $('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+obj.result.message+'</div></div></div>');
		       }
		       $.loadAnimation.end();
		       setTimeout(function() {
		       }, 0);

		  $('#action-clear')
			.click(
					function() {
						$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
						$("#error-message").dialog({
							resizable: false,
							height:140,
							title: "<span class='ui-dlg-confirm'>Confirm</span>",
							modal: true,
							buttons: {
								'Ok' : function() {
									$('form').clearForm();
									$('#search-results-list')
											.html(
													'<tr><td colspan="4">Search Results will be show here</td></tr>');
									setTimeout(function() {
										$('#search-results-list').jScrollPane({
											showArrows : true
										});
									}, 0);
									$(this).dialog('close');
								},
						Cancel: function() {
							$(this).dialog('close');
						}
							}
						});
					});
			    
		},
		initDefaultResultButtons:function(){
			$('.btn-view').click(function() {
		       var id = $(this).attr('id');
		       $.post('journal.json','action=get-journal-for-view&id='+id,function(obj){
		    	   var data=obj.result.data;
		    	   var journal= '<div class="outline" style="margin-left: 10px;">'+
		    		'<div class="first-row">'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Invoice No</span>'+
		    				'</div>'+
		    				'<div class="number">'+
	    					'<span class="property-value">'+
	    					'<a id="previous-journal-invoice-number" href="#" class="'+ data.invoiceNo +'" align="'+ id +'" style="color:#000; font-weight:bold; outline:none;text-decoration:none !important;">'+
	    					 data.invoiceNo
	    					 +'</a>'
	    					 +'</span>'+
	    				     '</div>'+
		    			'</div>'+
		    			'<div class="right-align">'+
		    					'<div class="number-lable" style="margin-left:-18px;">'+
		    					'<span class="span-label">Date</span>'+
		    				'</div>'+
		    				'<div class="number" style="margin-left:110px;">'+
		    					'<span class="property-value">'+DayBookViewHandler.formatDate(data.createdOn)+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    		'</div>'+
		    		'<div class="first-row" style="width:960px;">'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Business Name</span>'+
		    				'</div>'+
		    				'<div class="number">'+
		    					'<span class="property-value">'+data.businessName+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable">'+
		    					'<span class="span-label">Invoice Name</span>'+
		    				'</div>'+
		    				'<div class="number">'+
		    					'<span class="property-value">'+data.invoiceName+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    			'<div class="right-align" style="margin-right:16px;">'+
	    				'<div class="number-lable">'+
	    					'<span class="span-label">Status</span>'+
	    				'</div>'+
	    				'<div class="number">'+
	    					'<span class="property-value">'+data.status+'</span>'+
	    				'</div>'+
	    			'</div>'+
		    			'<div class="left-align">'+
		    				'<div class="number-lable" style="width: 120px;">'+
		    					'<span class="span-label">Reason</span>'+
		    				'</div>'+
		    				'<div class="number" style="margin-left: 110px;">'+
		    					'<div class="input-field-preview" style="width: 620px;">'+
		    								'<span class="property-value">'+data.description+'</span>'+
		    							'</div>'+
		    				'</div>'+
		    			'</div>'+
		    		'</div>'+
		    		'<div class="paymentset-row" style="margin-top: 10px;">'+
		    			'<div class="paymentset" style="height: 120px;">'+
		    				'<div class="separator" style="height: 30px; width: 260px;"></div>'+
		    				'<div class="form-row" style="margin-left: 2px; width: 280px;">'+
		    					'<div class="payment-span-label" style="width: 125px;">Sales Executive</div>'+
		    					'<span style="color: #1C8CF5; font: 12px arial;">'+data.createdBy+'</span>'+
		    				'</div>'+ 
		    				'<div class="separator" style="height: 10px; width: 60px;"></div>'+
		    				'<div class="form-row" style="width: 300px; margin-left: 240px; margin-top: -40px;">'+
		    					'<div class="payment-span-label" style="margin-left: 40px; width: 80px;">Customer</div>'+
		    					'<span style="color: #1C8CF5; font: 12px arial;">'+data.businessName+'</span>'+
		    				'</div>'+
		    			'</div>'+
		    			'<div class="separator" style="height: 100px; width: 400px;"></div>'+
		    			
		    			'<div class="paymentset" style="height: 120px;">'+
		    				'<div class="form-row">'+
		    					'<div class="payment-span-label">Amount</div>'+
		    					'<div class="input-field"><span class="appostaphie"></span>'+
		    						'<span class="span-payment">'+currencyHandler.convertFloatToStringPattern(data.amount)+'</span>'+
		    					'</div>'+
		    				'</div></div>'+
		    				'<div style="float:right;margin-right:120px;"><span style="color:red;"><b>*</b></span><span style="color: gray;"><i>All the amounts are in '+Msg.CURRENCY_FORMATE+'</i></span></div>'+
		    		'</div>'+
		    	'</div>';
		    	   $('#journals-view-container').html(journal);
		            $("#journals-view-dialog").dialog('open');
		    	   
		    	   
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
		  //------------------------------
		   //click of current sales_return_view_page invoiceNumber display previous SR with details before CR of that sales_return
		   var $anchor = $('.outline').find('a#previous-journal-invoice-number');
		    $anchor.live('click',function(){
		    	var invoiceNumber=$(this).attr('class');
		    	var journalId=$(this).attr('align');
		    	$.post('journal.json','action=get-previous-journal-id&invoiceNumber='+ invoiceNumber +'&journalId='+journalId,
		    			function(obj){
	 					var previousJournalId = obj.result.data;
	 					if(previousJournalId != 0){
							$.post('my-sales/transactions/view-transactions/journals_view.jsp','id=' + previousJournalId,
							           function(data) {
							            $('#previous-journals-view-container-'+ JournalViewHandler.count).html(data);
							            var dialogOpts = {
	       						     		   height : 400,
	       						     		   width : 1020,
							            		buttons : {
	       						     		    Close : function() {
	       						     		     $(this).dialog('close');
	       						     		    }
	       						     		 },
							            	};
							            $("#previous-journals-view-dialog-"+ JournalViewHandler.count).dialog(dialogOpts);
							 });
					}else{
						$('.outline').find('a#previous-journal-invoice-number').css('font-weight','normal');
						$('.outline').find('a#previous-journal-invoice-number').css('color','#00f');
					}
	 					JournalViewHandler.count=JournalViewHandler.count+1;	
	 	    });
		   }); 
		    //id from jsp page
		    $('#change-request-journal-Invoice-number').live('click',function(){
		       var invoiceNumber=$(this).attr('class');
		    	var journalId=$(this).attr('align');
		    	$.post('journal.json','action=get-previous-journal-id&invoiceNumber='+ invoiceNumber +'&journalId='+journalId,
		    			function(obj){
						var previousJournalId = obj.result.data;
						if(previousJournalId != 0){
							$.post('my-sales/transactions/view-transactions/journals_view.jsp','id=' + previousJournalId,
							           function(data) {
							            $('#previous-journals-view-container-'+ JournalViewHandler.count).html(data);
							            var dialogOpts = {
	       						     		   height : 400,
	       						     		   width : 1020,
							            		buttons : {
	       						     		    Close : function() {
	       						     		     $(this).dialog('close');
	       						     		    }
	       						     	  },
							            	};
							            $("#previous-journals-view-dialog-"+ JournalViewHandler.count).dialog(dialogOpts);
							 });
					}else{
						$('#change-request-journal-Invoice-number').css('font-weight','normal');
						$('#change-request-journal-Invoice-number').css('color','#00f');
					}
						JournalViewHandler.count=JournalViewHandler.count+1;
		      });
		    });
		 }
};