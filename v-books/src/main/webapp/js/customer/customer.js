var CustomerHandler = {
		flag:true,
		theme: "",
		expanded: true,
	initPageLinks : function() {
		$('#customer-add').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_add.jsp'
		});
		$('#customer-search').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_search.jsp'
		});
		$('#customer-import').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_import.jsp'
		});
		$('#customer-upload').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_upload.jsp'
		});
	},

	customerSteps : [ '#customer-form', '#customers-detail-form' ],
	customerUrl : [ 'customer.json', 'customer.json'],
	customerStepCount : 0,
	initAddButtons : function() {
		CustomerHandler.customerStepCount = 0;
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
		$('#action-clear').click(function(){
			$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$('#invoiceNameValid').empty();
						$('#creditLimitValid').empty();
						$('#customerNameValid').empty();
					    $('#mobileValid').empty();
						$('#overduesValid').empty();
						$('#emailValid').empty();
						$('#directLineValid').empty();
						$('#altmobileValid').empty();
						$('#regionValid').empty();
						$('#addressLine1Valid').empty();
						$('#localityValid').empty();
						$('#cityValid').empty();
					    $('#stateValid').empty();
						$('#zipcodeValid').empty();
						$('#businessNameValid').empty();
						$(CustomerHandler.customerSteps[CustomerHandler.customerStepCount]).clear();
		    			$(this).dialog('close');

					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		    return false;
		});
		// clear button for customer credit form
		$('#action-clear-customer-credit').click(function() {
			$('#error-message').html('You will loose entered data.. Clear form?');   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$('form').clearForm();
		    			$(this).dialog('close');

					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		    return false;
		});
		$('#creditLimit').blur(function(){
			var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
			var formatCreditLimit=currencyHandler.convertFloatToStringPattern(creditLimit.toFixed(2));
			$('#creditLimit').val(formatCreditLimit);
			
		});

		$('#button-next').click(function() {
			var success = true;
			$(CustomerHandler.customerSteps[CustomerHandler.customerStepCount]).find('.mandatory').each(function() {
				
				if($(this).val()=='' || ($(this).val()=='-1') &&
						($(this).get(0).tagName=='select'||$(this).get(0).tagName=='SELECT')){
					return;
					success = false;
					
				}
			});
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(CustomerHandler.flag==false){
				return resultFailure;
			}
			if(CustomerHandler.customerStepCount == 0){
			if(CustomerValidationHandler.validateCustomer()==false){
				return resultSuccess;
			}
			}else if (CustomerHandler.customerStepCount == 1) {
				if(CustomerValidationHandler.validateCustomerStepOne()==false){
					return resultSuccess;
				}
			}
		
							var paramString = $(CustomerHandler.customerSteps[CustomerHandler.customerStepCount]).serializeArray();
							var creditLimit=$('#creditLimit').val();
							$.each(paramString, function(i, formData) {
								if(formData.value === creditLimit){ 
							    	formData.value = currencyHandler.convertStringPatternToFloat(creditLimit);
							    }
							    if(formData.value==0){
							    	formData.value=null;
							    }
							});    
							var sendFormData=$.param(paramString);
							$.ajax({	type : "POST",
										url : 'customer.json',
										data : sendFormData,
										success : function(data) {
											$('#error-message').html('');
											$('#error-message').hide();
											$(CustomerHandler.customerSteps[CustomerHandler.customerStepCount]).hide();
											$(CustomerHandler.customerSteps[++CustomerHandler.customerStepCount]).show();
											if (CustomerHandler.customerStepCount == CustomerHandler.customerSteps.length) {
												if(!PageHandler.expanded){
													PageHandler.hidePageSelection();
												}
												else{
													PageHandler.pageSelectionHidden =false;
													PageHandler.hidePageSelection();
												}
												$('#button-next').hide();
												$('#action-clear').hide();
												$('#button-save').show();
												$('#button-assign-credit').show();
												$('#button-update').show();
												$.post('customer/customer_view.jsp','viewType=preview',function(data) {
														 $('#customer-preview-container').css({'height' : '350px'});
															$('#customer-preview-container').html(data);
															$('.table-field').css({"width":"800px"});
															$('.main-table').css({"width":"400px"});
															$('.inner-table').css({"width":"400px"});
															$('.display-boxes-colored').css({"width":"140px"});
															$('.display-boxes').css({"width":"255px"});
															$('#customer-preview-container').show();
															CustomerHandler.expanded=false;
														$('#ps-exp-col').click(function() {
																if (CustomerHandler.customerStepCount == CustomerHandler.customerSteps.length)
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
																	CustomerHandler.expanded=false;
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
																	CustomerHandler.expanded=true;
															    }
																
																}
														   
														});
														
													});
												
											}
											if (CustomerHandler.customerStepCount > 0) {
												$('#button-prev').show();
												$('.page-buttons').css(
														'margin-left', '240px');

											} else {
												$('#button-prev').hide();
												$('.page-buttons').css(
														'margin-left', '200px');
											}
										},
										error : function(data) {
											$('#error-message').html(
													data.responseText);
											$('#error-message').dialog();
											$('#error-message').show();
										}
									});
						});
		$('#button-save').click(function() {
			var thisButton = $(this);
			var paramString = 'action=save-customer';
			PageHandler.expanded=false;
			pageSelctionButton.click();
			$('.page-content').ajaxSavingLoader();
			$.post('customer.json', paramString, function(obj) {
				$.loadAnimation.end();
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
			
		});
		/*$('#customer-add').click(function() {
			$('.customer-page-container').load('customer/customer_add.jsp');
		});*/
		$('#button-update').click(function() {
			var thisButton = $(this);
			var paramString = 'action=edit-customer';
			PageHandler.expanded=false;
			pageSelctionButton.click();
			$('.page-content').ajaxSavingLoader();
			$.post('customer.json', paramString, function(obj){
				$.loadAnimation.end();
				$(this).successMessage({container:'.customer-page-container', data:obj.result.message});
		    });
		});
	/*	$('#customer-search').click(function() {
			$('.customer-page-container').load('customer/customer_search.jsp');
		});*/
		$('#action-display-customers').click(function() {
			var thisButton = $(this);
			var paramString = 'action=display-customers';
			$.post('customer.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
		});
		// customer credit save button
		$('#button-save-customer-credit').click(function() {
			if ($('form').validate() == false)
				return;
			var thisButton = $(this);
			var paramString = $('form').serialize();
			$.post('customer.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
		});
		// customer Assign credit save button
		$('#button-assign-credit').click(function() {
			var action="save-customer-assign-credit";
			var container ='.customer-page-container';
			var url = "customer/customer_credit_add.jsp?action="+action;
			$(container).load(url);
		});
		$('#button-prev').click(function() {
							$('#action-clear').show();
							if (CustomerHandler.customerStepCount == CustomerHandler.customerSteps.length) {
								if(!CustomerHandler.expanded){
									PageHandler.pageSelectionHidden =false;
									PageHandler.hidePageSelection();
									CustomerHandler.expanded=true;
								}
								$('#button-next').show();
								$('#button-save').hide();
								$('#button-update').hide();
								$('#customer-preview-container').html('');
								$('#customer-preview-container').hide();
								$('.page-buttons').css('margin-left', '150px');
							}
							$(CustomerHandler.customerSteps[CustomerHandler.customerStepCount]).hide();
							$(CustomerHandler.customerSteps[--CustomerHandler.customerStepCount]).show();
							if (CustomerHandler.customerStepCount > 0) {
								$('#button-prev').show();
								$('.page-buttons').css('margin-left', '150px');
							} else{
								$('#button-prev').hide();
								$('.page-buttons').css('margin-left', '240px');
							}
						});
		$('#action-cancel').click(function() {
							$('#error-message').html('You will loose unsaved data.. Cancel form?');
							$("#error-message").dialog(
											{
												resizable : false,
												height : 140,
												title : "<span class='ui-dlg-confirm'>Confirm</span>",
												modal : true,
												buttons : {
													'Ok' : function() {
														$('.task-page-container').html('');
										    			var container ='.customer-page-container';
										    			var url = "customer/customer_add.jsp";
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
		// cancel button for edit functionality
		$('#action-cancel-edit').click(function() {
			$('#error-message').html('You will loose unsaved data.. Cancel form?');
			$("#error-message").dialog(
							{
								resizable : false,
								height : 140,
								title : "<span class='ui-dlg-confirm'>Confirm</span>",
								modal : true,
								buttons : {
									'Ok' : function() {
										$('.task-page-container').html('');
										location.href = "index.jsp?module=customer"
									},
									Cancel : function() {
										$(this).dialog('close');
									}
								}
							});
			return false;
		});
		$('#button-update-customer-credit').click(function() {
			var thisButton = $(this);
			var paramString = $('#customer-credit-form').serialize();
			$.post('customer.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
		});
	$('#button-edit-customer').click(function() {
		var thisButton = $(this);
		var paramString = $('#customer-edit-form').serialize();
		$.post('customer.json', paramString,
	        function(obj){
			$(this).successMessage({container:'.customer-page-container', data:obj.result.message});
	        }
	    );
	});

},
initSearchCustomer : function(role) {
		// Customer credit search button click
		$('#action-search-customer-credit').click(function() {
			var thisButton = $(this);
			var paramString = $('#customer-credit-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			$.post('customer.json', paramString,
		        function(obj){
			    	var data = obj.result.data;
					$('#search-results-list').html('');
					if(data!= null) {
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
					
							'<span class="property">'+Msg.CUSTOMER_ID +': '+' </span><span class="property-value">' + data[loop].customerId + '</span>' +
							'<span class="property">'+Msg.CUSTOMER_CREDIT_LIMIT +': '+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].creditLimit.toFixed(2)) + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2">'+
							'<div class="result-body">' +
							'<span class="property">'+Msg.CUSTOMER_CREDIT_OVER_DUE_DAYS +': '+' </span><span class="property-value">' + data[loop].creditOverdueDays + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-action">' + 
							'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Modify Customer Credit"></div>' + 
							'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Customer Credit Details"></div>';
					
							rowstr = rowstr + '<div id="'+data[loop].id+'" class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
							'</div>' +
							'</div>';
					$('#search-results-list').append(rowstr);
				};
				CustomerHandler.initSearchCreditResultButtons ();
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
	},
		// end of search
		initSearchCustomer : function(role) {
			//CustomerHandler.customerSearch();
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
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('#customer-search-form').clearForm();
							$(this).dialog('close');
						},
				Cancel: function() {
					$(this).dialog('close');
				}
					}
				});
			});
			$('#action-search-customer').click(function() {
				CustomerHandler.customerSearch();
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
		customerSearch :function(){
			var thisButton = $(this);
			var paramString = $('#customer-search-form').serialize();
			$('#search-results-list').ajaxLoader();
			  //$('form').clearForm();
			$.post('customer.json', paramString, function(obj) {
				var data = obj.result.data;
		   		$('#search-results-list').html('');
	            if(data!=null){
	    		var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
							rowstr = '<div class="customer-search-result" id="row-'+data[loop].id+'" style="height:50px;">';
							rowstr = rowstr + '<div class="green-result-col-1" style="width:250px;">'+
							'<div class="result-title" style="height:auto;overflow:hidden;width:190px;;word-wrap:break-word;">' + data[loop].businessName  + '</div>' +
							'<div class="result-body" style="height:auto">' +
							'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font:12px arial;width:100px;word-wrap:break-word;left:-100px;">' + data[loop].mobile + '</span><br/>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2"style="height:auto">'+
							'<div class="result-title"style="height:auto;overflow:hidden;width:190px;;word-wrap:break-word;">' + data[loop].vbCustomer.customerName + '</div>' +
							'<div class="result-body"style="height:auto">' +
							'<span class="property">'+Msg.CUSTOMER_LOCALITY+' </span><span class="property-value"style="height :auto;width:100px;word-wrap:break-word;font:12px ">' +data[loop].locality + '</span>' +
							'</div>' +
							'</div>' +
							
							'<div class="green-result-col-action" id="action-'+data[loop].id+'" style="height:50px;padding: 0px 8px 0px 0px; width:100px !important;">' + 
							'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Customer"style="margin-top:10px;"></div>' + 
							'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Customer Details" style="margin-top:10px;"></div>'+
							'<div id="'+data[loop].id+'" class="ui-btn disable-icon enable-disable" title="Disable Customer" style="margin-top:10px;"></div>'+
							'<div id="'+data[loop].id+'" class="ui-btn btn-view-docs" title="View Customer Documents" style="margin-top:10px;"></div>'+
							'</div>'+
							'</div>';
							
					         $('#search-results-list').append(rowstr);
					         if(data[loop].customerState == 'Disabled'){
						    	 $("#row-"+data[loop].id).css('opacity', '0.5');
						    	 $("#action-"+data[loop].id).find(".edit-icon").hide();
								 $("#action-"+data[loop].id).find(".btn-view").css("pointer-events", "visible");
								 $("#action-"+data[loop].id).find(".enable-disable").addClass("enable-icon");
								 $("#action-"+data[loop].id).find(".enable-disable").removeClass("disable-icon");
								 $("#action-"+data[loop].id).find(".enable-icon").attr("title","Enable Customer");
						    }else{
						    	$("#action-"+data[loop].id).find(".disable-icon").attr("title","Disable Customer");
						    }
				};
				CustomerHandler.initSearchResultButtons();
				$('#search-results-list').jScrollPane({showArrows:true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
			}
				$.loadAnimation.end();
				setTimeout(function(){
					$('#search-results-list').jScrollPane({showArrows:true});
				},0);
			});
		},
		initSearchResultButtons : function (){
			
		$('.btn-view-docs').click(function() {
			var id = $(this).attr('id');
			$.post('customer.json','action=get-customer-documents&id='+id, function(obj) {
				var data = obj.result.data;
				$("#customer-documents-dialog").dialog('open');
				if(data != undefined) {
					var custDocs="";
					custDocs +='<div class="report-header" style="width: 780px; height: 30px;">'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 148px;"> S.No</div>' +
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 400px;">File Name</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 50px;"></div>'+
					'</div>';
		            $('#customer-documents-container').append(custDocs);
		            $('#customer-documents-container').append('<div class="grid-content" style="height:242px;width: 780px; overflow-y:initial;"></div>'); 
					for(var loop=1; loop<=data.length; loop=loop+1) {
						var fileName = data[loop-1].filePath;
						var custDocsRows ='<div class="ui-content report-content">'+
						'<div class="report-body" style="width: 780px; height: 30px; overflow: hidden; line-height:20px;">'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 148px; word-wrap: break-word;">' +  loop  + '</div>' +
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 400px; word-wrap: break-word;">'+ data[loop-1].fileName+'</div>'+
						'<a href="downloadServlet?filePath='+fileName+'"  class="btn-doc-view" title="View Document" style="height: inherit; width: 20px; margin-top:10px; margin-left:50px;" ></a>'+
						'<a id="'+fileName+'" onclick="CustomerHandler.documentDelete(\'' +fileName+'\');"class="btn-doc-delete" title="Delete Document" style="height: inherit; width: 20px; margin-top:10px;"></a>'+
						'</div>'+
						'</div>';
						$('.grid-content').append(custDocsRows);
					}
				} else {
					$('#customer-documents-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
				}
			});
		});
		$("#customer-documents-dialog").dialog({
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
				$('#customer-documents-container').html('');
			}
		});
		
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('customer.json','action=get-customer-details&id='+id,function(obj){
			$.post('customer/customer_profile_view.jsp', 'id='+id, function(data){
				var result = obj.result.data;
				$('#customer-view-container').html(data);
				$('.table-field').css({"width":"800px"});
				$('.main-table').css({"width":"400px"});
				$('.inner-table').css({"width":"400px"});
				$('.display-boxes-colored').css({"width":"140px"});
				$('.display-boxes').css({"width":"255px"});
				$('#customer-view-dialog').dialog('open');
				//$('#id').val(id);
				CustomerHandler.displayRecords(result);
		        });
			});
		});
		$('#customer-view-dialog').dialog({
			autoOpen: false,
			height: 480,
			width: 850,
			modal: true,
			buttons: {
				Close: function() {
					$(this).dialog('close');
					$('#customer-view-container').html('')
				}
			},
		});
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('customer.json','action=get-customer-details&id='+id,function(obj){
				var result = obj.result.data;
				$.post('customer/customer_edit.jsp', 'id='+id,  function(data){
					$('.customer-page-container').html(data);
					$('#id').val(id);
					$('#businessName').val(result.businessName);
					$('#invoiceName').val(result.invoiceName);
					$('#customerName').val(result.customerName);
					$('#region').val(result.region);
					$('#locality').val(result.locality);
					if(result.gender == 'M'){
						$('#male').attr('checked','checked');
					}else{
						$('#female').attr('checked','checked');
					}
					$('#mobile').val(result.mobile);
					if(result.email != "null"){
						$('#Email').val(result.email);
					}
					if(result.creditLimit != "null"){
						$('#creditLimit').val(result.creditLimit);
					}
					if(result.creditOverdueDays != "null"){
						$('#creditOverdueDays').val(result.creditOverdueDays);
					}
					if(result.directLine != "null"){
						$('#directLine').val(result.directLine);
					}
					if(result.alternateMobile != "null"){
						$('#alternateMobile').val(result.alternateMobile);
					}
					if(result.addressLine1 != "null"){
						$('#addressLine1').val(result.addressLine1);
					}
					if(result.addressLine2 != "null"){
						$('#addressLine2').val(result.addressLine2);
					}
					if(result.landmark != "null"){
						$('#landmark').val(result.landmark);
					}
					if(result.state != "null"){
						$('#state').val(result.state);
					}
					if(result.city != "null"){
						$('#city').val(result.city);
					}
					if(result.zipcode != "null"){
						$('#zipcode').val(result.zipcode);
					}
					
				   });
			});
			
      });
		$('.enable-icon').click(function() {
			var id = $(this).attr('id');
			 var customerStatusParam='Enabled';
			$('#customer-enable-disable-view-container').html('Are You Sure Want To Enable This Customer ?');
			
			  $('#customer-enable-disable-view-dialog').dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						Yes: function() {
							 $.post('customer.json', 'action=modify-customer-status&customerStatusParam='+customerStatusParam+'&id='+id, 
										function(obj) {
								 var data=obj.result.data;
									 $("#row-"+id).css('opacity', '1');
								     $("#action-"+id).find(".edit-icon").css("pointer-events", "visible");
									 $("#action-"+id).find(".btn-view").css("pointer-events", "visible");
									 $("#action-"+id).find(".enable-disable").removeClass("enable-icon");
									 $("#action-"+id).find(".enable-disable").addClass("disable-icon");
									 $("#action-"+id).find(".disable-icon").attr("title","Disable Customer");
									 CustomerHandler.customerSearch();
							 });
							 $("#customer-enable-disable-view-dialog").dialog('close');
						},
			            No: function() {
			            	$('#customer-enable-disable-view-dialog').dialog('close');
				      }
					},
					close: function() {
						$('#customer-enable-disable-view-dialog').dialog('close');
					}
	        });
			
		});
		
		 $('.disable-icon').click(function(){
			 var id = $(this).attr('id');
			 var customerStatusParam='Disabled';
			 $.post('customer.json', 'action=check-customer-credits&id='+id, 
						function(obj) {
				 var data=obj.result.data;
				 if(data == 'true'){
					 $('#customer-enable-disable-view-container').html('Are You Sure Want To Disable This Customer ?');
					 $('#customer-enable-disable-view-dialog').dialog({
							autoOpen: true,
							height: 200,
							width: 500,
							modal: true,
							buttons: {
								Yes: function() {
									 $.post('customer.json', 'id='+id+'&action=modify-customer-status&customerStatusParam='+customerStatusParam, function(obj) {
										 $("#row-"+id).css('opacity', '0.5');
									     $("#action-"+id).find(".edit-icon").css("pointer-events", "none");
										 $("#action-"+id).find(".btn-view").css("pointer-events", "visible");
										 $("#action-"+id).find(".enable-disable").addClass("enable-icon");
										 $("#action-"+id).find(".enable-disable").removeClass("disable-icon");
										 $("#action-"+id).find(".enable-icon").attr("title","Enable Customer");
										 CustomerHandler.customerSearch();
									 });
									 $("#customer-enable-disable-view-dialog").dialog('close');
								},
					            No: function() {
					            	$('#customer-enable-disable-view-dialog').dialog('close');
						      }
							},
							close: function() {
								$('#customer-enable-disable-view-dialog').dialog('close');
							}
				  });
				 }else{
					showMessage({title:'Message', msg:'Can not Disable,This Customer has Credits'});
				 }
			   });
	       });
		},
		
		// Delete customer docs.
		documentDelete:function(id){var filePath = id;
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
										$.post('customer.json','action=delete-customer-document&filePath='+filePath, function(obj) {
											$("#customer-documents-dialog").dialog('close');
											showMessage({title:'Message', msg:'<b>File deleted successfully.</b>'});
										});
									},
									Cancel : function() {
										$(this).dialog('close');
									}
								}
							});
			
			
		},
		
		displayRecords : function(result){
			$('#bName').text(result.businessName);
			$('#iName').text(result.invoiceName);
			$('#cName').text(result.customerName);
			$('#region').text(result.region);
			$('#loc').text(result.locality);
			if(result.gender == 'M'){
				$('#gender').text('Male');
			}else{
				$('#gender').text('Female');
			}
			$('#mobile').text(result.mobile);
			if(result.email != "null"){
				$('#mail').text(result.email);
			}
			if(result.creditLimit != "null"){
				$('#creditLimit').text(result.creditLimit);
			}
			if(result.creditOverdueDays != "null"){
				$('#creditOverdueDays').text(result.creditOverdueDays);
			}
			if(result.directLine != "null"){
				$('#directLine').text(result.directLine);
			}
			if(result.alternateMobile != "null"){
				$('#alternateNumber').text(result.alternateMobile);
			}
			if(result.addressLine1 != "null"){
				$('#address1').text(result.addressLine1);
			}
			if(result.addressLine2 != "null"){
				$('#address2').text(result.addressLine2);
			}
			if(result.landmark != "null"){
				$('#landMark').text(result.landmark);
			}
			if(result.state != "null"){
				$('#state').text(result.state);
			}
			if(result.city != "null"){
				$('#city').text(result.city);
			}
			if(result.zipcode != "null"){
				$('#zipcode').text(result.zipcode);
			}
		},
	initSearchCreditResultButtons : function () {
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('customer/customer_credit_edit.jsp', 'id='+id,
		        function(data){
					$('.customer-page-container').html(data);
		        }
	        );
		});
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('customer/customer_credit_view.jsp', 'id='+id,
		        function(data){
				$('#customer-credit-view-container').html(data);
				$("#customer-credit-view-dialog").dialog('open');
		        }
	        );
		});
		$("#customer-credit-view-dialog").dialog({
			autoOpen: false,
			height: 650,
			width: 800,
			modal: true,
			buttons: {
				Close: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				$('#customer-credit-view-container').html('');
			}
		});

	},
	initCheckEditBusinessName :function(){
		$("#businessName").focus(function(event){
			$('#businessNameValid').empty();
		});
	
		$("#businessName").change(function(event){
			if($('#businessName').val()==''){
				//$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
			}else
				{
				var businessname=$('#businessName').val();
				var blen=$('#businessName').val().length;
				var paramString='businessName='+businessname+'&action=validate-businessName';
				if($('#businessName').val().charAt(blen -1)!==" "){
				$.post('customer.json',paramString,
				        function(data){
		        	//$('#businessNameValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking businessName...'> Checking...");
		            var delay = function() {
		            	CustomerHandler.AjaxSucceeded(data);
		            	};
		            	setTimeout(delay,0);
				});
				}
				
				}
		});
	},
	initCheckBusinessName: function() {
		$("#businessName").focus(function(event){
			$('#businessNameValid').empty();
		});
	
		$("#businessName").blur(function(event){
			if($('#businessName').val()==''){
			}else
				{
				var businessname=$('#businessName').val();
				var paramString='businessName='+businessname+'&action=validate-businessName';
				$.post('customer.json',paramString,
				        function(data){
		            var delay = function() {
		            	CustomerHandler.AjaxSucceeded(data);
		            	};
		            	setTimeout(delay, 0);
				});
				
				}
		});
},
/*Succeeded: function(data1) {
    if (data1.result.data == "y") {
        $('#bValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='BusinessName available!'> ");
    } else if (data1.result.data == "v") {
        $('#bValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'>");
    	$('#description').focus();
    }
     confirming and displaying remainder date is valid or Invalid 
     else{
        $('#bValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>BusinessName Alreday Exists!");
        $('#businessname').focus();
    }
    
},*/
AjaxSucceeded: function(data1) {
    if (data1.result.data == "true") {
    	 CustomerHandler.flag=true;
    	 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> ");
    } /* else if (data1.result.data == "v") {
        $('#businessNameValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> ");
    	$('#description').focus();
    }*/ else {
        $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>");
        CustomerHandler.flag=false;
               $("#businessName").focus(function() {
            	   if($('#businessName').val()!="") {
            	   $('#businessName_pop').hide();
               	 $('#businessnamevalid_pop').show();
               	 $('#businessNameValid').empty();
            	   }
               });
        $("#businessName").blur(function(event){
        	var end=$('#businessName').val().length;
        	 $('#businessnamevalid_pop').hide();
        });
    }
    
},
importCustomer: function(){
	$('form').submit(function(event) {
		   var file = $('input[type=file]').val();       

		   if ( ! file) {
		       showMessage({title:'Warning', msg:'Please choose a file'});
		       event.preventDefault();
		       return;
		   } 

		   if ( ! file.match(/\.(xls)$/)) {
		       showMessage({title:'Warning', msg:'please choose .xls files only!'});
		       event.preventDefault();
		   }

		});
},

manageUploadOperations: function() {
	$('#businessName').click(function() {
		var thisInput = $(this);
		$('#business-name-suggestions').show();
		CustomerHandler.suggestBusinessName(thisInput);
	});
	$('#businessName').keyup(function() {
		var thisInput = $(this);
		$('#business-name-suggestions').show();
		CustomerHandler.suggestBusinessName(thisInput);
	});
	$('#businessName').focusout(function() {
		$('#business-name-suggestions').animate({
			display : 'none'
		}, 1000, function() {
			$('#business-name-suggestions').hide();
		});
	});
	$('#businessName').change(function() {
		if($('#businessName').val().length > 0) {
			CustomerValidationHandler.validateBusinessName();
		}
	});
	$('.btn-upload').click(function() {
		if($('#businessName').val().length > 0) {
			if(CustomerValidationHandler.validateBusinessName()== false){
				return false;
			}
		}
		  var file = $('input[type=file]').val();       

		  if($('#businessName').val().length == 0){
				showMessage({title:'Message', msg:'Please Select business Name'});
				 return false;
			}
				
		   if ( ! file) {
		       showMessage({title:'Warning', msg:'Please choose a file'});
		       return false;
		   } 
		   if(file && $('#businessName').val().length>0 ){
				$('.customer-page-container').load('customer/customer_upload.jsp');
			}
		
	});
}, 

suggestBusinessName : function(thisInput) {
	var suggestionsDiv = $('#business-name-suggestions');
	var val = $('#businessName').val();
	$.post('customer.json','action=get-business-name&businessNameVal=' + val,function(obj) {
		$.loadAnimation.end();
		suggestionsDiv.html('');
		var data = obj.result.data;
		if (data != undefined) {
			var customerId;
			var htmlStr = '<div>';
			for ( var loop = 0; loop < data.length; loop = loop + 1) {
				customerId = data[loop].id;
				htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">' + data[loop].businessName + '</a></li>';
			}
			htmlStr += '</div>';
			suggestionsDiv.append(htmlStr);
			$.post('customer.json', 'action=get-organization-name&id='+customerId, function(obj) {
				$('#organizationId').val(obj.result.data);
			})
		} else {
			suggestionsDiv.append('<div id="">No Business Names</div>');
		}
		suggestionsDiv.css('left',thisInput.position().left);
		suggestionsDiv.css('top',thisInput.position().top + 25);
		suggestionsDiv.show();
		$('.select-teacher').click(function() {
			thisInput.val($(this).html());
			thisInput.attr('businessName', $(this).attr('businessName'));
			$('#businessName').attr('value',$(this).attr('businessName'));
			$('#businessNameValid').empty();
			 $('#businessName_pop').hide();
			suggestionsDiv.hide();
			var businessName = $('#businessName').val();
			$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
				$('#customerName').val(obj.result.data.customerName);
			});
		});
	});
  },

};

