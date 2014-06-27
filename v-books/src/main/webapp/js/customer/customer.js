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
		$('#add-change-request').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_change_request_add.jsp'
		});
		$('#approve-change-request').pageLink({
			container : '.customer-page-container',
			url : 'customer/approve_customer_change_request.jsp'
		});
		$('#search-change-request-details').pageLink({
			container : '.customer-page-container',
			url : 'customer/customer_change_request_search.jsp'
		});
	},

	customerSteps : [ '#customer-form', '#customers-detail-form' ],
	customerUrl : [ 'customer.json', 'customer.json'],
	customerStepCount : 0,
  initBubble:function(){
	  $('#help-icon-customer').click(function(){
		  var button = $(this);
		  $('#help-icon-customer').CreateBubblePopup({
              innerHtml: 'help!'
  });
		  $.ajax({
	            url : "helloworld.txt",
	            dataType: "text",
	            success : function (data) {
	            	$('#help-icon-customer').ShowBubblePopup({ width: 700,height:455,selectable:true, innerHtml: 'text is changed!' }, false);
	            	$('#help-icon-customer').SetBubblePopupInnerHtml(data, false); 
	            }
	        });
		 /* $.get('images/default/vekomy-logo.png', function(data) {
			  var seconds_to_wait = 2;
				function pause(){
					var timer = setTimeout(function(){
						seconds_to_wait--;
						if(seconds_to_wait > 0){
							pause();
						}else{
							$('#help-icon-customer').SetBubblePopupInnerHtml(data, false); //false -> it shows new innerHtml but doesn't save it, then the script is forced to load everytime the innerHtml... 
						};
					},1000);
				};pause();
		  });*/

		  $('#help-icon-customer').ShowBubblePopup({ width: 700,height:455,selectable:true, innerHtml: 'text is changed!' }, false);
	  });
  },
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
			$('#error-message').html('You will loose entered data.. Clear form?');   
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
			if(CustomerHandler.validateCustomer()==false){
				return resultSuccess;
			}
			}else if (CustomerHandler.customerStepCount == 1) {
				if(CustomerHandler.validateCustomerStepOne()==false){
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
														'margin-left', '150px');

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
			$.post('customer.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
			
		});
		$('#customer-add').click(function() {
			$('.customer-page-container').load('customer/customer_add.jsp');
		});
		$('#button-update').click(function() {
			var thisButton = $(this);
			var paramString = 'action=edit-customer';
			PageHandler.expanded=false;
			pageSelctionButton.click();
			$.post('customer.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.customer-page-container', data:obj.result.message});
		        }
		    );
		});
		$('#customer-search').click(function() {
			$('.customer-page-container').load('customer/customer_search.jsp');
		});
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
		$('#button-prev')
				.click(
						function() {
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
							$(
									CustomerHandler.customerSteps[CustomerHandler.customerStepCount])
									.hide();
							$(
									CustomerHandler.customerSteps[--CustomerHandler.customerStepCount])
									.show();
							if (CustomerHandler.customerStepCount > 0) {
								$('#button-prev').show();
								$('.page-buttons').css('margin-left', '150px');
							} else {
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
validateCustomer : function(){
	var result=true;
	var end=$('#businessName').val().length;
	if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			$('#businessnamevalid_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessName_pop').show();
		});
	
	$("#businessName").blur(function(event){
		 $('#businessName_pop').hide();
		 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
			 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#businessName").focus(function(event){
				 $('#businessnamevalid_pop').hide();
				 $('#businessNameValid').empty();
				 $('#businessName_pop').show();
			 });
			 
		 }else{
			 $('#businessName_pop').hide();
		 }
	});
	result=false;
	}
	if($('#businessName').val().length>200){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			$('#businessName_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessnamelen_pop').show();
		});
		$("#businessName").blur(function(event){
			$('#businessnamelen_pop').hide();
			if($('#businessName').val().length>200){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessNameValid').empty();
					 $('#businessnamelen_pop').show();
				});
			}else{
				$('#businessnamelen_pop').hide();
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessnamelen_pop').hide();
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
				});
			
			$("#businessName").blur(function(event){
				 $('#businessName_pop').hide();
				 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" " ){
					 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#businessName").focus(function(event){
						 $('#businessNameValid').empty();
						 $('#businessName_pop').show();
					 });
					 
				 }else{
					 $('#businessName_pop').hide();
				 }
			});
			result=false;
			}
		});
		result=false;
	}
	var invEnd =$('#invoiceName').val().length;
	if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#inamelen_pop').hide();
			$('#invoiceNameValid').empty();
			 $('#iname_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#iname_pop').hide();
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#iname_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	if($('#invoiceName').val().length>200){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#invoiceNameValid').empty();
			 $('#inamelen_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#inamelen_pop').hide();
			 if($('#invoiceName').val().length>200){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#inamelen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
					$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#invoiceName").focus(function(event){
						$('#inamelen_pop').hide();
						$('#invoiceNameValid').empty();
						 $('#iname_pop').show();
					});
					$("#invoiceName").blur(function(event){
						 $('#iname_pop').hide();
						 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
							 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#invoiceName").focus(function(event){
								 $('#invoiceNameValid').empty();
							 $('#iname_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var crlimit =$('#creditLimit').val().length;
	if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val())==false && $('#creditLimit').val().length>0 || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10 || $('#creditLimit').val().charAt(0)==" "||$('#creditLimit').val().charAt(crlimit -1)==" "){
		$('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#creditLimit").focus(function(event){
			$('#creditlimitlen_pop').hide();
			$('#creditLimitValid').empty();
			 $('#creditlimit_pop').show();
		});
		$("#creditLimit").blur(function(event){
			 $('#creditlimit_pop').hide();
			 if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val())==false&& $('#creditLimit').val().length>0 || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10 ||$('#creditLimit').val().charAt(0)==" "||$('#creditLimit').val().charAt(crlimit -1)==" "){
				 $('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#creditLimit").focus(function(event){
					 $('#creditLimitValid').empty();
					 $('#creditlimit_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	var cname= $('#customerName').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			 $('#cnamelen_pop').hide();
			$('#customerNameValid').empty();
			 $('#cname_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cname_pop').hide();
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
					 $('#cname_pop').show();
				 });
				 
			 }else{
				 $('#customerNameValid').empty();
			 }
		});
		result=false;
	}
	if($('#customerName').val().length>200){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			$('#cname_pop').hide();
			$('#customerNameValid').empty();
			 $('#cnamelen_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cnamelen_pop').hide();
			 if($('#customerName').val().length>200){
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
				 $('#cnamelen_pop').show();
				 });
			 }if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
					$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#customerName").focus(function(event){
						 $('#cnamelen_pop').hide();
						$('#customerNameValid').empty();
						 $('#cname_pop').show();
					});
					$("#customerName").blur(function(event){
						 $('#cname_pop').hide();
							 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(cname -1)==" "){
							 $("#customerName").focus(function(event){
								 $('#customerNameValid').empty();
								 $('#cname_pop').show();
							 });
							 
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var mobile=$('#mobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			 $('#mobilelen_pop').hide();
			$('#mobileValid').empty();
			 $('#mobile_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobile_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobileValid').empty();
					 $('#mobile_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#mobile').val().length>60){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			$('#mobile_pop').hide();
			$('#mobileValid').empty();
			 $('#mobilelen_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobilelen_pop').hide();
			 if($('#mobile').val().length>60){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobile_pop').hide();
					 $('#mobileValid').empty();
				 $('#mobilelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
					$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#mobile").focus(function(event){
						 $('#mobilelen_pop').hide();
						$('#mobileValid').empty();
						 $('#mobile_pop').show();
					});
					$("#mobile").blur(function(event){
						 $('#mobile_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
							 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#mobile").focus(function(event){
								 $('#mobileValid').empty();
								 $('#mobile_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var cod=$('#creditOverdueDays').val().length;
	if(/^[0-9]+$/.test($('#creditOverdueDays').val())==false &&$('#creditOverdueDays').val().length>0||$('#creditOverdueDays').val().charAt(0)==" "||$('#creditOverdueDays').val().charAt(cod -1)==" "){
		$('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#creditOverdueDays").focus(function(event){
			$('#overduesValid').empty();
			 $('#overdues_pop').show();
		});
		$("#creditOverdueDays").blur(function(event){
			 $('#overdues_pop').hide();
			 if(/^[0-9]+$/.test($('#creditOverdueDays').val())==false&&$('#creditOverdueDays').val().length>0||$('#creditOverdueDays').val().charAt(0)==" "||$('#creditOverdueDays').val().charAt(cod -1)==" "){
				 $('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#creditOverdueDays").focus(function(event){
					 $('#overduesValid').empty();
					 $('#overdues_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
		}
	if($('#creditOverdueDays').val().length>3){
		$('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#creditOverdueDays").focus(function(event){
			$('#overdues_pop').hide();	
			$('#overduesValid').empty();
			 $('#overdueslen_pop').show();
		});
		$("#creditOverdueDays").blur(function(event){
			 $('#overdueslen_pop').hide();
			 $('#overduesValid').empty();
			 if($('#creditOverdueDays').val().length>3){
				 $('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#creditOverdueDays").focus(function(event){
					 $('#overduesValid').empty();
				 $('#overdueslen_pop').show();
				 });
			 } if(/^[0-9]+$/.test($('#creditOverdueDays').val())==false&&$('#creditOverdueDays').val().length>0||$('#creditOverdueDays').val().charAt(0)==" "||$('#creditOverdueDays').val().charAt(cod-1)==" "){
				 $('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#creditOverdueDays").focus(function(event){
					 $('#overdueslen_pop').hide();
					 $('#overduesValid').empty();
					 $('#overdues_pop').show();
				 });
				 $("#creditOverdueDays").blur(function(event){
					 $('#overdues_pop').hide();
					 if(/^[0-9]+$/.test($('#creditOverdueDays').val())==false&&$('#creditOverdueDays').val().length>0||$('#creditOverdueDays').val().charAt(0)==" "||$('#creditOverdueDays').val().charAt(cod-1)==" "){
						 $('#overduesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#creditOverdueDays").focus(function(event){
							 $('#overdueslen_pop').hide();
							 $('#overduesValid').empty();
							 $('#overdues_pop').show();
						 });
						
					 }else{
						// $('#cityValid').html("<img
						// src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
			 }
			 else{
				 $('#overduesValid').empty();
			 }
		});
		result=false;
		
}
	var elen= $('#Email').val().length;
	var Email = $('#Email').val();
	if($('#Email').ValidateEmailAddr(Email) == false &&  $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			 $('#emaillen_pop').hide();
			$('#emailValid').empty();
			 $('#email_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#email_pop').hide();
			 if($('#Email').ValidateEmailAddr($('#Email').val()) == false&&  $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#Email').val().length>100){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			$('#email_pop').hide();
			$('#emailValid').empty();
			 $('#emaillen_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#emaillen_pop').hide();
			 if($('#Email').val().length>100){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#email_pop').hide();
					 $('#emailValid').empty();
				 $('#emaillen_pop').show();
				 });
			 }if($('#Email').ValidateEmailAddr(Email) == false&&  $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
					$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#Email").focus(function(event){
						 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
					$("#Email").blur(function(event){
						 $('#email_pop').hide();
						 if($('#Email').ValidateEmailAddr(Email) == false&&  $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
							 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#Email").focus(function(event){
								 $('#emaillen_pop').hide();
								 $('#emailValid').empty();
								 $('#email_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
	}
	return result;
},
validateCustomerStepOne: function(){
	var result=true;
	var dlen =$("#directLine").val().length
	if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#directLineValid').empty();
			 $('#dline_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dline_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#directLineValid').empty();
					 $('#dline_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#directLine').val().length>60){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#dline_pop').hide();
			$('#directLineValid').empty();
			 $('#dlinelen_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dlinelen_pop').hide();
			 if($('#directLine').val().length>60){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#dline_pop').hide();
					 $('#directLineValid').empty();
				 $('#dlinelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
					$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#directLine").focus(function(event){
						 $('#dlinelen_pop').hide();
						$('#directLineValid').empty();
						 $('#dline_pop').show();
					});
					$("#directLine").blur(function(event){
						 $('#dline_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
							 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#directLine").focus(function(event){
								 $('#directLineValid').empty();
								 $('#dline_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var almlen =$('#alternateMobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#altmobileValid').empty();
			 $('#alternate_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternate_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#altmobileValid').empty();
					 $('#alternate_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#alternateMobile').val().length>60){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#alternate_pop').hide();
			$('#altmobileValid').empty();
			 $('#alternatelen_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternatelen_pop').hide();
			 if($('#alternateMobile').val().length>60){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#alternate_pop').hide();
					 $('#altmobileValid').empty();
				 $('#alternatelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
					$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#alternateMobile").focus(function(event){
						 $('#alternatelen_pop').hide();
						$('#altmobileValid').empty();
						 $('#alternate_pop').show();
					});
					$("#alternateMobile").blur(function(event){
						 $('#alternate_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
							 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#alternateMobile").focus(function(event){
								 $('#altmobileValid').empty();
								 $('#alternate_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var reglen=$('#region').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
		$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#region").focus(function(event){
			$('#regionValid').empty();
			 $('#region_pop').show();
		});
		$("#region").blur(function(event){
			 $('#region_pop').hide();
			 if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
				 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#region").focus(function(event){
					 $('#regionValid').empty();
					 $('#region_pop').show();
				 });
				
			 }if($('#region').val().length>60){
					$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#region").focus(function(event){
						$('#region_pop').hide();	
						$('#regionValid').empty();
						 $('#regionlen_pop').show();
					});
					$("#region").blur(function(event){
						 $('#regionlen_pop').hide();
						 if($('#region').val().length>60){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionValid').empty();
							 $('#regionlen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false || ($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionlen_pop').hide();
								 $('#regionValid').empty();
								 $('#region_pop').show();
							 });
							 $("#region").blur(function(event){
								 $('#region_pop').hide();
								 if(/^[0-9]+$/.test($('#region').val())==false||($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
									 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#region").focus(function(event){
										 $('#regionlen_pop').hide();
										 $('#regionValid').empty();
										 $('#region_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#regionValid').empty();
						 }
					});
					result=false;
			 }
		});
		result=false;
	}
	var addlen=$('#addressLine1').val().length ;
	if(($('#addressLine1').val()).length > 0&&$('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1_pop').hide();
			 if(($('#addressLine1').val()).length > 0&&$('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#addressLine1').val().length>200){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1len_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1len_pop').hide();
			 if(($('#addressLine1').val()).length > 0 && $('#addressLin1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1len_pop').show();
					});
			 }else{
			 }
		});
	}
	var add2len=$('#addressLine2').val().length;
	if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
		 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		 $("#addressLine2").focus(function(event){
				$('#address2Valid').empty();
				 $('#addressLine2_pop').show();
			});
		 $("#addressLine2").blur(function(event){
		 $('#addressLine2_pop').hide();
			if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
			 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#addressLine2").focus(function(event){
					$('#address2Valid').empty();
					 $('#addressLine2_pop').show();
				});
		 }else{
		 }
	});
		 result=false;
	 }
	var lmlen=$('#landmark').val().length ;
	if($('#landmark').val().length > 0){
	if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmark_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmark_pop').hide();
			 if($('#landmark').val().length > 0){
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
					 $('#landmark_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		}
		});
		result=false;
	}}
	if($('#landmark').val().length>60){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmarklen_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmarklen_pop').hide();
			 if($('#landmark').val().length>60){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
				 $('#landmarklen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false || ($('#landmark').val()).length > 0||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
					$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#landmark").focus(function(event){
						$('#landmarklen_pop').hide();
						$('#landmarkValid').empty();
						 $('#landmark_pop').show();
					});
					$("#landmark").blur(function(event){
						 $('#landmark_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false || ($('#landmark').val()).length > 0||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
							 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#landmark").focus(function(event){
								 $('#landmarkValid').empty();
							 $('#landmark_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var loclen=$('#locality').val().length ;
	if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#locality_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#locality_pop').hide();
			 if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
					 $('#locality_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}	if($('#locality').val().length>60){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#localitylen_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#localitylen_pop').hide();
			 if($('#locality').val().length>60){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
				 $('#localitylen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
					$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#locality").focus(function(event){
						$('#localitylen_pop').hide();
						$('#localityValid').empty();
						 $('#locality_pop').show();
					});
					$("#locality").blur(function(event){
						 $('#locality_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
							 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#locality").focus(function(event){
								 $('#localityValid').empty();
							 $('#locality_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var citylen=$('#city').val().length;
	if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
		// $('#city_pop').show();
		$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	    // $('#city').focus();
		$("#city").focus(function(event){
			$('#cityValid').empty();
			 $('#city_pop').show();
		});
		$("#city").blur(function(event){
			 $('#city_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
				 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#city").focus(function(event){
					 $('#cityValid').empty();
					 $('#city_pop').show();
				 });
				
			 }if($('#city').val().length>50){
					$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#city").focus(function(event){
						$('#city_pop').hide();	
						$('#cityValid').empty();
						 $('#citylen_pop').show();
					});
					$("#city").blur(function(event){
						 $('#citylen_pop').hide();
						 if($('#city').val().length>60){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#cityValid').empty();
							 $('#citylen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false && $('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#citylen_pop').hide();
								 $('#cityValid').empty();
								 $('#city_pop').show();
							 });
							 $("#city").blur(function(event){
								 $('#city_pop').hide();
								 if(/^[0-9]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
									 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#city").focus(function(event){
										 $('#citylen_pop').hide();
										 $('#cityValid').empty();
										 $('#city_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#cityValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length > 0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
		$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#state").focus(function(event){
			$('#stateValid').empty();
			 $('#state_pop').show();
		});
		$("#state").blur(function(event){
			 $('#state_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length > 0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
				 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#state").focus(function(event){
						$('#stateValid').empty();
						 $('#state_pop').show();
					});
			 }
			 if($('#state').val().length>60){
					$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#state").focus(function(event){
						$('#state_pop').hide();	
						$('#stateValid').empty();
						 $('#statelen_pop').show();
					});
					$("#state").blur(function(event){
						 $('#statelen_pop').hide();
						 if($('#state').val().length>60){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#stateValid').empty();
							 $('#statelen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false &&$('#state').val().length > 0||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#statelen_pop').hide();
								 $('#stateValid').empty();
								 $('#state_pop').show();
							 });
							 $("#state").blur(function(event){
								 $('#state_pop').hide();
								 if(/^[0-9]+$/.test($('#state').val())==false&&$('#state').val().length > 0||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
									 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#state").focus(function(event){
										 $('#statelen_pop').hide();
										 $('#stateValid').empty();
										 $('#state_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#stateValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	var zipcodelen=$('#zipcode').val().length;
	if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false&&$('#zipcode').val().length > 0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" " ){
		// $('#pincode_pop').show();
		$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#zipcode").focus(function(event){
			$('#zipcodeValid').empty();
			 $('#pincode_pop').show();
		});
		$("#zipcode").blur(function(event){
			 $('#pincode_pop').hide();
				if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false&&$('#zipcode').val().length > 0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
				 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#zipcode").focus(function(event){
					 $('#zipcodeValid').empty();
					 $('#pincode_pop').show();
				 });
				
			 }else{
				 // $('#pincodeValid').html("<img
					// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#zipcode').val().length>10){
			$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincode_pop').hide();	
				$('#zipcodeValid').empty();
				 $('#pincodelen_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincodelen_pop').hide();
				 if($('#zipcode').val().length>10){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#zipcodeValid').empty();
					 $('#pincodelen_pop').show();
					 });
				 } if(/^[0-9]+$/.test($('#zipcode').val())==false&&$('#zipcode').val().length > 0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#pincodelen_pop').hide();
						 $('#zipcodeValid').empty();
						 $('#pincode_pop').show();
					 });
					 $("#zipcode").blur(function(event){
						 $('#pincode_pop').hide();
						 if(/^[0-9]+$/.test($('#zipcode').val())==false&&$('#zipcode').val().length > 0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
							 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#zipcode").focus(function(event){
								 $('#pincodelen_pop').hide();
								 $('#zipcodeValid').empty();
								 $('#pincode_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
				 }
				 else{
					 $('#zipcodeValid').empty();
				 }
			});
			result=false;
			
	}
	
	return result;
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
			CustomerHandler.customerSearch();
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
				$('#customer-search-form').clearForm();
    			CustomerHandler.customerSearch();
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
							rowstr = '<div class="customer-search-result"style="height:50px;">';
							rowstr = rowstr + '<div class="green-result-col-1" style="width:350px;">'+
							'<div class="result-title"style="height:auto;overflow:hidden;width:190px;;word-wrap:break-word;">' + data[loop].businessName  + '</div>' +
							'<div class="result-body"style="height:auto">' +
							'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font:12px arial;width:100px;word-wrap:break-word;left:-100px;">' + data[loop].mobile + '</span><br/>' +
							
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2"style="height:auto">'+
							'<div class="result-title"style="height:auto;overflow:hidden;width:190px;;word-wrap:break-word;">' + data[loop].vbCustomer.customerName + '</div>' +
							'<div class="result-body"style="height:auto">' +
							'<span class="property">'+Msg.CUSTOMER_LOCALITY+' </span><span class="property-value"style="height :auto;width:100px;word-wrap:break-word;font:12px ">' +data[loop].locality + '</span>' +
							'</div>' +
							'</div>' +
							'<div class="green-result-col-action"style="height:50px;padding: 0px 8px 0px 0px;">' + 
							'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Customer"style="margin-top:10px;"></div>' + 
							'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Customer Details"style="margin-top:10px;"></div>'+
							'<div id="'+data[loop].id+'" class="ui-btn delete-icon delete-organization" title="Delete Customer"style="margin-top:10px;"></div>';  
							
					         $('#search-results-list').append(rowstr);
				};
				CustomerHandler.initSearchResultButtons();
				$('#search-results-list').jScrollPane({showArrows:true});
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
			}
				$.loadAnimation.end();
				setTimeout(function(){
					$('#search-results-list').jScrollPane({showArrows:true});
				},0);
			});
		},
		initSearchResultButtons : function (){
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('customer/customer_profile_view.jsp', 'id='+id,
		        function(data){
				$('#customer-view-container').html(data);
				$('.table-field').css({"width":"800px"});
				$('.main-table').css({"width":"400px"});
				$('.inner-table').css({"width":"400px"});
				$('.display-boxes-colored').css({"width":"140px"});
				$('.display-boxes').css({"width":"255px"});
				$('#customer-view-dialog').dialog('open');
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
				}
			},
			Close: function() {
				$('#customer-view-container').html('');
			}
		});
		
		
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('customer/customer_edit.jsp', 'id='+id,
		        function(data){
					$('.customer-page-container').html(data);
		        }
	        );
      });
		$('.delete-organization').click(function() {
			var id = $(this).attr('id');
			$.post('customer/customer_profile_delete.jsp', 'id='+id,
		        function(data){
		        	$('#customer-delete-container').html(data);  
		        	$('.table-field').css({"width":"800px"});
					$('.main-table').css({"width":"400px"});
					$('.inner-table').css({"width":"400px"});
					$('.display-boxes-colored').css({"width":"140px"});
					$('.display-boxes').css({"width":"255px"});
		        	$("#customer-delete-dialog").dialog({
		    			autoOpen: true,
		    			height: 480,
		    			width: 850,
		    			modal: true,
		    			buttons: {
		    			Delete: function() {
		    				 $.post('customer.json', 'id='+id+'&action=delete-customer',
		    						 function(obj) {
		    						$(this).successMessage({
		    							container : '.customer-page-container',
		    							data : obj.result.message
		    						});
		    					});
		    				  $(this).dialog('close');
		    				},
		    		      Close: function() {
		    			      $(this).dialog('close');
		    			
		    		          }
		    			},
		    			Close: function() {
		    				$('#customer-delete-container').html('');
		    			}
		    		});
		        }
		    );
		});
			
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
				//$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
			}else
				{
				var businessname=$('#businessName').val();
				var paramString='businessName='+businessname+'&action=validate-businessName';
				$.post('customer.json',paramString,
				        function(data){
		        	//$('#businessNameValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking businessName...'> Checking...");
		            var delay = function() {
		            	CustomerHandler.AjaxSucceeded(data);
		            	};
		            	setTimeout(delay, 0);
				});
				
				}
		});
},

Succeeded: function(data1) {
    if (data1.result.data == "y") {
        $('#bValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='BusinessName available!'> ");
    } else if (data1.result.data == "v") {
        $('#bValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'>");
    	$('#description').focus();
    }
    /* confirming and displaying remainder date is valid or Invalid */
     else{
        $('#bValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>BusinessName Alreday Exists!");
        $('#businessname').focus();
    }
    
},
AjaxSucceeded: function(data1) {
    if (data1.result.data == "y") {
    	 CustomerHandler.flag=true;
      //  $('#businessNameValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='BusinessName available!'>");
    } else if (data1.result.data == "v") {
        $('#businessNameValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> ");
    	$('#description').focus();
    }
    /* confirming and displaying remainder date is valid or Invalid */
     else {
        $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>");
        CustomerHandler.flag=false;
               $("#businessName").focus(function(){
            	   if($('#businessName').val()!=""){
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
}
};
var CustomerCrHandler = {
		initPageLinks : function() {
			$('#add-change-request').pageLink({
				container : '.customer-page-container',
				url : 'customer/customer_change_request_add.jsp'
			});
			$('#search-change-request-details').pageLink({
				container : '.customer-page-container',
				url : 'customer/customer_change_request_search.jsp'
			});
		
		},
		
		crSteps:['#customer-change-request-form','#customers-change-request-detail-form'],
		crUrl : [ 'customerCr.json', 'customerCr.json' ],
		crStepCount : 0,
		
		initAddCrButtons : function(){
			CustomerCrHandler.crStepCount = 0;
			$.fn.clear = function() {
				  return this.each(function() {
				    var type = this.type, tag = this.tagName.toLowerCase();
				    if (this.readOnly) return
				    if (tag == 'form')
				      return $(':input',this).clear();
				    if (type == 'text' || type == 'password' || tag == 'textarea')
				      this.value = '';
				    /*
					 * else if (type == 'checkbox' || type == 'radio')
					 * this.checked =true;
					 */
				    else if (tag == 'select')
				      return;
				  });
				};
			$('#action-clear').click(function() {
				$('#error-message').html('You will loose entered data.. Clear form?');   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('#businessNameValid').empty();
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
							$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).clear();
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
				$('form').clearForm();
			});
			$('#button-cr-next').click(function() {
				
				var success = true;
				$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).find('.mandatory').each(function() {
					
					if($(this).val()=='' || ($(this).val()=='-1') &&
							($(this).get(0).tagName=='select'||$(this).get(0).tagName=='SELECT')){
						// showMessage({title:'Error', msg:'Red marked fields
						// are mandatory'});
						return;
						success = false;
						
					}
				});
				var thisButton = $(this);
				var resultSuccess=true;
				var resultFailure=false;
				if(CustomerCrHandler.crStepCount == 0){
				if(CustomerCrHandler.validateCustomerCr()==false){
					return resultSuccess;
				}
				}else if (CustomerCrHandler.crStepCount == 1) {
					if(CustomerCrHandler.validateCustomerCrStepOne()==false){
						return resultSuccess;
					}
				}
						var paramString = $(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).serialize();
						$.ajax({	type : "POST",
							url : 'customerCr.json',
							data : paramString,
							success : function(obj) {
								var res=obj.result.data;
								var crType=$('#crType').val();
								if(res !=null){
								$('#mobile').val(res.mobile);
								$('#locality').val(res.locality);
								$('#region').val(res.region);
								if(res.landmark = null){
									$('#landmark').val();
								}else{
									$('#landmark').val(res.landmark);
								}
								if(res.alternateContactNumber= null){
									$('#alternateContactNumber').val();
								}else{
									$('#alternateContactNumber').val(res.alternateContactNumber);
								}
								if(res.directLine = null){
									$('#directLine').val();
								}else{
									$('#directLine').val(res.directLine);
								}
								$('#invoiceName').val(res.vbCustomer.invoiceName);
								if(res.city = null){
									$('#city').val();
								}else{
									$('#city').val(res.city);
								}
								if(res.state = null){
									$('#state').val();
								}else{
									$('#state').val(res.state);
								}
								if(res.addressLine1 = null){
									$('#addressLine1').val();
								}else{
									$('#addressLine1').val(res.addressLine1);
								}
								if(res.email = null){
									$('#Email').val();
								}else{
									$('#Email').val(res.email);
								}
								if(res.addressLine2 = null){
									$('#addressLine2').val();
								}else{
									$('#addressLine2').val(res.addressLine2);
								}
								if(res.zipcode = null){
									$('#zipcode').val();
								}else{
									$('#zipcode').val(res.zipcode);
								}
								
								}else if(crType=="true"&&CustomerCrHandler.crStepCount==0){
									 showMessage({title:'Warning', msg:'No User Exists With Given Details'});
								       event.preventDefault();	
								       return;

								}
								$('#error-message').html('');
								$('#error-message').hide();
								$(CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount]).hide();
								$(CustomerCrHandler.crSteps[++CustomerCrHandler.crStepCount]).show();
								if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length) {
									if(!PageHandler.expanded){
										PageHandler.hidePageSelection();// this
																		// is to
																		// enlarge
																		// preview
																		// container
																		// on
																		// loading
																		// page
									}
									else{
										
										PageHandler.pageSelectionHidden =false;
										PageHandler.hidePageSelection();
									}
									$('#button-cr-next').hide();
									$('#action-clear').hide();
									$('#button-cr-save').show();
									$.post('customer/customer_change_request_view.jsp','viewType=preview',function(data) {
										 $('#customer-preview-container').css({'height' : '350px'});
											$('#customer-preview-container').html(data);
											$('.table-field').css({"width":"800px"});
											$('.main-table').css({"width":"400px"});
											$('.inner-table').css({"width":"400px"});
											$('.display-boxes-colored').css({"width":"140px"});
											$('.display-boxes').css({"width":"255px"});
											$('#customer-preview-container').show();
											CustomerCrHandler.expanded=false;
										$('#ps-exp-col').click(function() {
												if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length)
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
													CustomerCrHandler.expanded=false;
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
													CustomerCrHandler.expanded=true;
											    }
												
												}
										   
										});		});
								}
								
								if (CustomerCrHandler.crStepCount > 0) {
									$('#button-cr-prev').show();
									$('.page-buttons').css(
											'margin-left', '150px');

								} else {
									$('#button-cr-prev').hide();
									$('.page-buttons').css(
											'margin-left', '200px');
								}
							},
							error : function(data) {
								$('#error-message').html(data.responseText);
								$('#error-message').dialog();
								$('#error-message').show();
							}
						});
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
							    			var url = "customer/customer_change_request_add.jsp";
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
					
		$('#button-cr-prev')
		.click(
				function() {
					$('#action-clear').show();
					if (CustomerCrHandler.crStepCount == CustomerCrHandler.crSteps.length) {
						if(!CustomerCrHandler.expanded){
							PageHandler.pageSelectionHidden =false;
							PageHandler.hidePageSelection();
							CustomerCrHandler.expanded=true;
						}

						$('#button-cr-next').show();
						$('#button-cr-save').hide();
						$('#button-update').hide();
						$('#customer-preview-container').html('');
						$('#customer-preview-container').hide();
						$('.page-buttons').css('margin-left', '150px');
					}
					$(
							CustomerCrHandler.crSteps[CustomerCrHandler.crStepCount])
							.hide();
					$(
							CustomerCrHandler.crSteps[--CustomerCrHandler.crStepCount])
							.show();
					if (CustomerCrHandler.crStepCount > 0) {
						$('#button-cr-prev').show();
						$('.page-buttons').css('margin-left', '150px');
					} else {
						$('#button-cr-prev').hide();
						$('.page-buttons').css('margin-left', '240px');
					}
				});
		$('#button-cr-save').click(function() {
			var thisButton = $(this);
			var paramString = 'action=save-customer-cr';
			$.post('customerCr.json', paramString, function(obj) {
				$(this).successMessage({
					container : '.customer-page-container',
					data : obj.result.message
				});
			});
		});
		$('#add-change-request').click(function() {
			$('.customer-page-container').load('customer/customer_change_request_add.jsp');
		});
		$('#business-name-suggestions').click(function (){
	        var customerBusinessName = $('#businessName').val();
	        $.post('customerCr.json', 'action=check-customer-cr-type&businessName='+customerBusinessName,
			        function(obj){
	        	    var data = obj.result.data;
	        	    if(data == 0){
	        	    	
	        	    }else{
	        	    	 showMessage({title:'Warning', msg:"Customer Changed request Not Approved Till Now"});
					       event.preventDefault();	
					       return;
	        	    }
	        	    }
		       );
	      }); 
		},
initSearchCrOnLoad: function() {
	var paramString='action=search-cr-onload';
	$.post('customerCr.json', paramString,
	function(obj){
		var data = obj.result.data;
		$('#search-results-list').html('');
				if(data != undefined) {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate"style="height:70px;">';
						} else {
							rowstr = '<div class="green-result-row"style="height:70px;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1">'+
						'<div class="result-title">' + data[loop].businessName  + '</div>' +
						'<div class="result-body">' +
						'<span class="property">'+ Msg.CUSTOMER_CR_TYPE +'</span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].crType +'</span>' +
						
						'</div>' +'<span class="property">'+Msg.CUSTOMER_EMAIL+' </span><span class="property-value">' + fmt(data[loop].email) + '</span>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + fmt(data[loop].mobile) + '</span><br/>' +
						'<span class="property">'+'</span>'+
						'<span class="property">'+Msg.CUSTOMER_ALTERNATE_NUMBER+' </span><span class="property-value">' + fmt(data[loop].alternateMobile) + '</span><br/>' +
						'</div>' +
						'</div>' +						
						'<div class="green-result-col-action"style="height:57px;">' + 
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="Approve Cr Details"></div>';
				
						rowstr = rowstr + '<div id="'+data[loop].id+'" class="ui-btn btn-employee-change-img" title="Change Employee Image"></div>' +
						'</div>' +
						'</div>';
				$('#search-results-list').append(rowstr);
			};
			CustomerCrHandler.initCrSearchResultButtons ();
			$('#search-results-list').jScrollPane({showArrows:true});
				}
				 else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
					}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
			});
},
initSearchCrCustomer : function(role) {
$('#action-search-customer-cr').click(function(){
	var thisButton = $(this);
	var paramString = $('#customer-cr-search-form').serialize();
	$('#search-results-list').ajaxLoader();
	$.post('customerCr.json', paramString, function(obj) {
		var data = obj.result.data;
   		$('#search-results-list').html('');
        if(data!=null){
		var alternate = false;
		for(var loop=0;loop<data.length;loop=loop+1){
			if(alternate) {
				var rowstr = '<div class="green-result-row alternate"style="height:70px;">';
			} else {
				rowstr = '<div class="green-result-row"style="height:70px;">';
			}
			alternate = !alternate;
		rowstr = rowstr + '<div class="green-result-col-1">'+
		'<div class="result-title">' + data[loop].businessName  + '</div>' +
		'<div class="result-body">' +
		'<span class="property">'+ Msg.CUSTOMER_CR_TYPE +'</span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].crType +'</span>' +
		
		'</div>' +'<span class="property">'+Msg.CUSTOMER_EMAIL+' </span><span class="property-value" style="font: bold 14px arial;">' + fmt(data[loop].email) + '</span>' +
		'</div>' +
		'<div class="green-result-col-2">'+
		'<div class="result-body">' +
		'<span class="property">'+Msg.CUSTOMER_MOBILE_NUMBER+' </span><span class="property-value"style="font: bold 14px arial;width: 300px;">' + fmt(data[loop].mobile) + '</span><br/>' +
		'<span class="property">'+'</span>'+
		'<span class="property">'+Msg.CUSTOMER_ALTERNATE_NUMBER+' </span><span class="property-value" style="font: bold 14px arial;">' + fmt(data[loop].alternateMobile) + '</span><br/>' +
    	'</div>' +
		'</div>' +
		'<div class="green-result-col-action"style="height:57px;">' + 
		'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Customer Details"></div>';
         $('#search-results-list').append(rowstr);
		};
		CustomerCrHandler.initCrSearchResultButtons();
		$('#search-results-list').jScrollPane({showArrows:true});
	} else {
		$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
	}
		$.loadAnimation.end();
		setTimeout(function(){
			$('#search-results-list').jScrollPane({showArrows:true});
		},0);
	});
});
},
getAddressTypes : function(){
	$.post('default.json','action=get-cr-address-types',function(obj){
		var result = obj.result.data;
		var res =new String(result);
		if(result.length >0 ){
			for(var loop=0;loop<result.length;loop = loop+1){
				 document.form2.addressType.options[loop]=new Option(result[loop]);
				};
		}
	
		
	});
},
initCrSearchResultButtons : function(){
	$('.btn-view').click(function() {
		var id = $(this).attr('id');
		$.post('customer/customer_change_request_credit.jsp', 'id='+id,
	        function(data){
	        	$('#customer-view-container').html(data);  
	        	$("#customer-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 485,
	    			width: 640,
	    			modal: true,
	    			buttons: {
	    		      Close: function() {
	    			      $(this).dialog('close');
	    		          }
	    			},
	    			close: function() {
	    				$('#customer-view-container').html('');
	    			}
	    		});
	        }
	    );
  });
},

load:function() {
	
	$('#businessName').click(function() {
		var crtype=$('#crType').val();
		var thisInput = $(this);
		if(crtype=="true"){
		$('#business-name-suggestions').show();
		CustomerCrHandler.suggestBusinessName(thisInput);
	}
	});
	$('#businessName').keyup(function() {
		var thisInput = $(this);
		$('#business-name-suggestions').animate({
			display : 'none'
		}, 0, function() {
			$('#business-name-suggestions').hide();
		});
		var thisInput = $(this);
		if(crtype=="true"){
		$('#business-name-suggestions').show();
		CustomerCrHandler.suggestBusinessName(thisInput);
	}
		var businessName = $('#businessName').val();
		$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
			
			$('#customerName').val(obj.result.data);
			
		});
	});
	$('#businessName').focusout(function() {
		$('#business-name-suggestions').animate({
			display : 'none'
		},0, function() {
		});
	});
},
validateCustomerCr:function(){
	var result=true;
	var end=$('#businessName').val().length;
	if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			$('#businessnamelen_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessName_pop').show();
		});
	
	$("#businessName").blur(function(event){
		 $('#businessName_pop').hide();
		 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
			 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#businessName").focus(function(event){
				 $('#businessNameValid').empty();
				 $('#businessName_pop').show();
			 });
			 
		 }else{
			// $('#cityValid').html("<img
			// src='"+THEMES_URL+"images/available.gif' alt=''>");
		 }
	});
	result=false;
	}
	if($('#businessName').val().length>200){
		$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		$("#businessName").focus(function(event){
			 //$('#businessName_pop').hide();
			$('#businessNameValid').empty();
			 $('#businessnamelen_pop').show();
		});
		$("#businessName").blur(function(event){
			$('#businessnamelen_pop').hide();
			if($('#businessName').val().length>200){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessNameValid').empty();
					 $('#businessnamelen_pop').show();
				});
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" "){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessnamelen_pop').hide();
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
				});
			
			$("#businessName").blur(function(event){
				 $('#businessName_pop').hide();
				 if(/^[a-zA-Z0-9\s]+$/.test($('#businessName').val())==false||$('#businessName').val().charAt(0)==" "||$('#businessName').val().charAt(end -1)==" " ){
					 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#businessName").focus(function(event){
						 $('#businessNameValid').empty();
						 $('#businessName_pop').show();
					 });
					 
				 }else{
					// $('#cityValid').html("<img
					// src='"+THEMES_URL+"images/available.gif' alt=''>");
				 }
			});
			result=false;
			}
		});
		result=false;
	}

	var cname= $('#customerName').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			 $('#cnamelen_pop').hide();
			$('#customerNameValid').empty();
			 $('#cname_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cname_pop').hide();
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
					 $('#cname_pop').show();
				 });
				 
			 }else{
				 $('#customerNameValid').empty();
			 }
		});
		result=false;
	}
	if($('#customerName').val().length>200){
		$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#customerName").focus(function(event){
			$('#cname_pop').hide();
			$('#customerNameValid').empty();
			 $('#cnamelen_pop').show();
		});
		$("#customerName").blur(function(event){
			 $('#cnamelen_pop').hide();
			 if($('#customerName').val().length>200){
				 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#customerName").focus(function(event){
					 $('#customerNameValid').empty();
				 $('#cnamelen_pop').show();
				 });
			 }if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0||$('#customerName').val().charAt(0)==" "||$('#customerName').val().charAt(cname -1)==" "){
					$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#customerName").focus(function(event){
						 $('#cnamelen_pop').hide();
						$('#customerNameValid').empty();
						 $('#cname_pop').show();
					});
					$("#customerName").blur(function(event){
						 $('#cname_pop').hide();
							 $('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 if(/^[a-zA-Z\s]+$/.test($('#customerName').val())==false||($('#customerName').val()).length==0){
							 $("#customerName").focus(function(event){
								 $('#customerNameValid').empty();
								 $('#cname_pop').show();
							 });
							 
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	return result;
},
validateCustomerCrStepOne: function(){
	var result=true;
	var invEnd =$('#invoiceName').val().length;
	if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#inamelen_pop').hide();
			$('#invoiceNameValid').empty();
			 $('#iname_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#iname_pop').hide();
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#iname_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	if($('#invoiceName').val().length>200){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#invoiceNameValid').empty();
			 $('#inamelen_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#inamelen_pop').hide();
			 if($('#invoiceName').val().length>200){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#inamelen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
					$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#invoiceName").focus(function(event){
						$('#inamelen_pop').hide();
						$('#invoiceNameValid').empty();
						 $('#iname_pop').show();
					});
					$("#invoiceName").blur(function(event){
						 $('#iname_pop').hide();
						 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false||$('#invoiceName').val().charAt(0)==" "||$('#invoiceName').val().charAt(invEnd -1)==" "){
							 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#invoiceName").focus(function(event){
								 $('#invoiceNameValid').empty();
							 $('#iname_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	
	var mobile=$('#mobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			 $('#mobilelen_pop').hide();
			$('#mobileValid').empty();
			 $('#mobile_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobile_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobileValid').empty();
					 $('#mobile_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#mobile').val().length>60){
		$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#mobile").focus(function(event){
			$('#mobile_pop').hide();
			$('#mobileValid').empty();
			 $('#mobilelen_pop').show();
		});
		$("#mobile").blur(function(event){
			 $('#mobilelen_pop').hide();
			 if($('#mobile').val().length>60){
				 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#mobile").focus(function(event){
					 $('#mobile_pop').hide();
					 $('#mobileValid').empty();
				 $('#mobilelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
					$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#mobile").focus(function(event){
						 $('#mobilelen_pop').hide();
						$('#mobileValid').empty();
						 $('#mobile_pop').show();
					});
					$("#mobile").blur(function(event){
						 $('#mobile_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#mobile').val())==false||($('#mobile').val()).length==0||$('#mobile').val().charAt(0)==" "||$('#mobile').val().charAt(mobile -1)==" "){
							 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#mobile").focus(function(event){
								 $('#mobileValid').empty();
								 $('#mobile_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var elen= $('#Email').val().length;
	var Email = $('#Email').val();
	if($('#Email').ValidateEmailAddr(Email) == false&&  $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			 $('#emaillen_pop').hide();
			$('#emailValid').empty();
			 $('#email_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#email_pop').hide();
			 if($('#Email').ValidateEmailAddr($('#Email').val()) == false&& $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#Email').val().length>100){
		$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#Email").focus(function(event){
			$('#email_pop').hide();
			$('#emailValid').empty();
			 $('#emaillen_pop').show();
		});
		$("#Email").blur(function(event){
			 $('#emaillen_pop').hide();
			 if($('#Email').val().length>100){
				 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#Email").focus(function(event){
					 $('#email_pop').hide();
					 $('#emailValid').empty();
				 $('#emaillen_pop').show();
				 });
			 }if($('#Email').ValidateEmailAddr(Email) == false&& $('#Email').val().length>0 ||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
					$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#Email").focus(function(event){
						 $('#emaillen_pop').hide();
						$('#emailValid').empty();
						 $('#email_pop').show();
					});
					$("#Email").blur(function(event){
						 $('#email_pop').hide();
						 if($('#Email').ValidateEmailAddr(Email) == false&& $('#Email').val().length>0||$('#Email').val().charAt(0)==" "||$('#Email').val().charAt(elen-1)==" "){
							 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#Email").focus(function(event){
								 $('#emaillen_pop').hide();
								 $('#emailValid').empty();
								 $('#email_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
	}
	if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false){
		$('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#invoiceName").focus(function(event){
			$('#inamelen_pop').hide();
			$('#invoiceNameValid').empty();
			 $('#iname_pop').show();
		});
		$("#invoiceName").blur(function(event){
			 $('#iname_pop').hide();
			 if(/^[a-zA-Z0-9.\s]+$/.test($('#invoiceName').val())==false){
				 $('#invoiceNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#invoiceName").focus(function(event){
					 $('#invoiceNameValid').empty();
				 $('#iname_pop').show();
				 });
			 }else{
			 }
		});
		result=false;
	}
	var dlen =$("#directLine").val().length
	if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#directLineValid').empty();
			 $('#dline_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dline_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false && ($('#directLine').val()).length > 0||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#directLineValid').empty();
					 $('#dline_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#directLine').val().length>60){
		$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#directLine").focus(function(event){
			$('#dline_pop').hide();
			$('#directLineValid').empty();
			 $('#dlinelen_pop').show();
		});
		$("#directLine").blur(function(event){
			 $('#dlinelen_pop').hide();
			 if($('#directLine').val().length>60){
				 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#directLine").focus(function(event){
					 $('#dline_pop').hide();
					 $('#directLineValid').empty();
				 $('#dlinelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
					$('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#directLine").focus(function(event){
						 $('#dlinelen_pop').hide();
						$('#directLineValid').empty();
						 $('#dline_pop').show();
					});
					$("#directLine").blur(function(event){
						 $('#dline_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#directLine').val())==false||$('#directLine').val().charAt(0)==" "||$('#directLine').val().charAt(dlen-1)==" "){
							 $('#directLineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#directLine").focus(function(event){
								 $('#directLineValid').empty();
								 $('#dline_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var almlen =$('#alternateMobile').val().length;
	if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#altmobileValid').empty();
			 $('#alternate_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternate_pop').hide();
			 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false && ($('#alternateMobile').val()).length > 0||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#altmobileValid').empty();
					 $('#alternate_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#alternateMobile').val().length>60){
		$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#alternateMobile").focus(function(event){
			$('#alternate_pop').hide();
			$('#altmobileValid').empty();
			 $('#alternatelen_pop').show();
		});
		$("#alternateMobile").blur(function(event){
			 $('#alternatelen_pop').hide();
			 if($('#alternateMobile').val().length>60){
				 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#alternateMobile").focus(function(event){
					 $('#alternate_pop').hide();
					 $('#altmobileValid').empty();
				 $('#alternatelen_pop').show();
				 });
			 }if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
					$('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#alternateMobile").focus(function(event){
						 $('#alternatelen_pop').hide();
						$('#altmobileValid').empty();
						 $('#alternate_pop').show();
					});
					$("#alternateMobile").blur(function(event){
						 $('#alternate_pop').hide();
						 if(/^[0-9-+()\s]+$/.test($('#alternateMobile').val())==false||$('#alternateMobile').val().charAt(0)==" "||$('#alternateMobile').val().charAt(almlen-1)==" "){
							 $('#altmobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#alternateMobile").focus(function(event){
								 $('#altmobileValid').empty();
								 $('#alternate_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var reglen=$('#region').val().length;
	if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
		$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#region").focus(function(event){
			$('#regionValid').empty();
			 $('#region_pop').show();
		});
		$("#region").blur(function(event){
			 $('#region_pop').hide();
			 if(/^[a-zA-Z\s]+$/.test($('#region').val())==false||($('#region').val()).length==0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
				 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#region").focus(function(event){
					 $('#regionValid').empty();
					 $('#region_pop').show();
				 });
				
			 }if($('#region').val().length>60){
					$('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#region").focus(function(event){
						$('#region_pop').hide();	
						$('#regionValid').empty();
						 $('#regionlen_pop').show();
					});
					$("#region").blur(function(event){
						 $('#regionlen_pop').hide();
						 if($('#region').val().length>60){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionValid').empty();
							 $('#regionlen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false || ($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
							 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#region").focus(function(event){
								 $('#regionlen_pop').hide();
								 $('#regionValid').empty();
								 $('#region_pop').show();
							 });
							 $("#region").blur(function(event){
								 $('#region_pop').hide();
								 if(/^[0-9]+$/.test($('#region').val())==false||($('#region').val()).length == 0||$('#region').val().charAt(0)==" "||$('#region').val().charAt(reglen-1)==" "){
									 $('#regionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#region").focus(function(event){
										 $('#regionlen_pop').hide();
										 $('#regionValid').empty();
										 $('#region_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#regionValid').empty();
						 }
					});
					result=false;
			 }
		});
		result=false;
	}
	var addlen=$('#addressLine1').val().length ;
	if($('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1_pop').hide();
			 if($('#addressLine1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}if($('#addressLine1').val().length>200){
		$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#addressLine1").focus(function(event){
			$('#addressLine1Valid').empty();
			 $('#addressLine1len_pop').show();
		});
		$("#addressLine1").blur(function(event){
			 $('#addressLine1len_pop').hide();
			 if($('#addressLin1').val().charAt(0)==" "||$('#addressLine1').val().charAt(addlen-1)==" "){
				 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#addressLine1").focus(function(event){
						$('#addressLine1Valid').empty();
						 $('#addressLine1len_pop').show();
					});
			 }else{
			 }
		});
	}
	var add2len=$('#addressLine2').val().length;
	if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
		 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		 $("#addressLine2").focus(function(event){
				$('#address2Valid').empty();
				 $('#addressLine2_pop').show();
			});
		 $("#addressLine2").blur(function(event){
		 $('#addressLine2_pop').hide();
			if($('#addressLine2').val().length!=0 && $('#addressLine2').val().charAt(add2len -1)==" "){
			 $('#address2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#addressLine2").focus(function(event){
					$('#address2Valid').empty();
					 $('#addressLine2_pop').show();
				});
		 }else{
		 }
	});
		 result=false;
	 }
	var lmlen=$('#landmark').val().length;
	if(($('#landmark').val()).length > 0){
	if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmark_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmark_pop').hide();
			 if(($('#landmark').val()).length > 0){
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
					 $('#landmark_pop').show();
				 });
				
			 }
			 else{
				 $('#landmarkValid').empty();
			 }
			 }
		});
		result=false;
	}else {
		 $('#landmarkValid').empty();
	}
}
	if($('#landmark').val().length>60){
		$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#landmark").focus(function(event){
			$('#landmarkValid').empty();
			 $('#landmarklen_pop').show();
		});
		$("#landmark").blur(function(event){
			 $('#landmarklen_pop').hide();
			 if($('#landmark').val().length>60){
				 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#landmark").focus(function(event){
					 $('#landmarkValid').empty();
				 $('#landmarklen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false ||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
					$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#landmark").focus(function(event){
						$('#landmarklen_pop').hide();
						$('#landmarkValid').empty();
						 $('#landmark_pop').show();
					});
					$("#landmark").blur(function(event){
						 $('#landmark_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false ||$('#landmark').val().charAt(0)==" "||$('#landmark').val().charAt(lmlen-1)==" "){
							 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#landmark").focus(function(event){
								 $('#landmarkValid').empty();
							 $('#landmark_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var loclen=$('#locality').val().length ;
	if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#locality_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#locality_pop').hide();
			 if(/^[a-zA-Z0-9\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length == 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
					 $('#locality_pop').show();
				 });
				
			 }else{
				// $('#cityValid').html("<img
				// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}	if($('#locality').val().length>60){
		$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#locality").focus(function(event){
			$('#localityValid').empty();
			 $('#localitylen_pop').show();
		});
		$("#locality").blur(function(event){
			 $('#localitylen_pop').hide();
			 if($('#locality').val().length>60){
				 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#locality").focus(function(event){
					 $('#localityValid').empty();
				 $('#localitylen_pop').show();
				 });
			 }
			 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
					$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#locality").focus(function(event){
						$('#localitylen_pop').hide();
						$('#localityValid').empty();
						 $('#locality_pop').show();
					});
					$("#locality").blur(function(event){
						 $('#locality_pop').hide();
						 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#locality').val())==false || ($('#locality').val()).length > 0||$('#locality').val().charAt(0)==" "||$('#locality').val().charAt(loclen-1)==" "){
							 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#locality").focus(function(event){
								 $('#localityValid').empty();
							 $('#locality_pop').show();
							 });
						 }else{
						 }
					});
					result=false;
				}
		});
		result=false;
		
	}
	var citylen=$('#city').val().length;
	if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
		// $('#city_pop').show();
		$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	    // $('#city').focus();
		$("#city").focus(function(event){
			$('#cityValid').empty();
			 $('#city_pop').show();
		});
		$("#city").blur(function(event){
			 $('#city_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#city').val())==false&&$('#city').val().length>0 ||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
				 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#city").focus(function(event){
					 $('#cityValid').empty();
					 $('#city_pop').show();
				 });
				
			 }if($('#city').val().length>50){
					$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#city").focus(function(event){
						$('#city_pop').hide();	
						$('#cityValid').empty();
						 $('#citylen_pop').show();
					});
					$("#city").blur(function(event){
						 $('#citylen_pop').hide();
						 if($('#city').val().length>60){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#cityValid').empty();
							 $('#citylen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#citylen_pop').val()) == false&&$('#city').val().length>0 ||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
							 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#city").focus(function(event){
								 $('#citylen_pop').hide();
								 $('#cityValid').empty();
								 $('#city_pop').show();
							 });
							 $("#city").blur(function(event){
								 $('#city_pop').hide();
								 if(/^[0-9]+$/.test($('#city').val())==false&&$('#city').val().length>0||$('#city').val().charAt(0)==" "||$('#city').val().charAt(citylen-1)==" "){
									 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#city").focus(function(event){
										 $('#citylen_pop').hide();
										 $('#cityValid').empty();
										 $('#city_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#cityValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length>0||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
		$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#state").focus(function(event){
			$('#stateValid').empty();
			 $('#state_pop').show();
		});
		$("#state").blur(function(event){
			 $('#state_pop').hide();
			 if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false&&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
				 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#state").focus(function(event){
						$('#stateValid').empty();
						 $('#state_pop').show();
					});
			 }
			 if($('#state').val().length>60){
					$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#state").focus(function(event){
						$('#state_pop').hide();	
						$('#stateValid').empty();
						 $('#statelen_pop').show();
					});
					$("#state").blur(function(event){
						 $('#statelen_pop').hide();
						 if($('#state').val().length>60){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#stateValid').empty();
							 $('#statelen_pop').show();
							 });
						 }  if(/^[a-zA-Z.\s]+$/.test($('#state').val()) == false &&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
							 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#state").focus(function(event){
								 $('#statelen_pop').hide();
								 $('#stateValid').empty();
								 $('#state_pop').show();
							 });
							 $("#state").blur(function(event){
								 $('#state_pop').hide();
								 if(/^[0-9]+$/.test($('#state').val())==false&&$('#state').val().length>0 ||$('#state').val().charAt(0)==" "||$('#state').val().charAt($('#state').val().length -1)==" "){
									 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#state").focus(function(event){
										 $('#statelen_pop').hide();
										 $('#stateValid').empty();
										 $('#state_pop').show();
									 });
									
								 }else{
									// $('#cityValid').html("<img
									// src='"+THEMES_URL+"images/available.gif' alt=''>");
								 }
							});
						 }
						 else{
							 $('#stateValid').empty();
						 }
					});
					result=false;
					
			}
		});
		result=false;
	}
	var zipcodelen=$('#zipcode').val().length;
	if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" " ){
		// $('#pincode_pop').show();
		$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#zipcode").focus(function(event){
			$('#zipcodeValid').empty();
			 $('#pincode_pop').show();
		});
		$("#zipcode").blur(function(event){
			 $('#pincode_pop').hide();
				if(/^[a-zA-Z0-9-\s]+$/.test($('#zipcode').val()) == false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
				 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#zipcode").focus(function(event){
					 $('#zipcodeValid').empty();
					 $('#pincode_pop').show();
				 });
				
			 }else{
				 // $('#pincodeValid').html("<img
					// src='"+THEMES_URL+"images/available.gif' alt=''>");
			 }
		});
		result=false;
	}if($('#zipcode').val().length>10){
			$('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincode_pop').hide();	
				$('#zipcodeValid').empty();
				 $('#pincodelen_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincodelen_pop').hide();
				 if($('#zipcode').val().length>10){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#zipcodeValid').empty();
					 $('#pincodelen_pop').show();
					 });
				 } if(/^[0-9]+$/.test($('#zipcode').val())==false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
					 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
						 $('#pincodelen_pop').hide();
						 $('#zipcodeValid').empty();
						 $('#pincode_pop').show();
					 });
					 $("#zipcode").blur(function(event){
						 $('#pincode_pop').hide();
						 if(/^[0-9]+$/.test($('#zipcode').val())==false &&$('#zipcode').val().length>0||$('#zipcode').val().charAt(0)==" "||$('#zipcode').val().charAt(zipcodelen-1)==" "){
							 $('#zipcodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#zipcode").focus(function(event){
								 $('#pincodelen_pop').hide();
								 $('#zipcodeValid').empty();
								 $('#pincode_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
				 }
				 else{
					 $('#zipcodeValid').empty();
				 }
			});
			result=false;
			
	}
	return result;
},
suggestBusinessName : function(thisInput) {
	var suggestionsDiv = $('#business-name-suggestions');
	var val = $('#businessName').val();
	$.post('customer.json','action=get-business-name&businessNameVal=' + val,function(obj) {
		$.loadAnimation.end();
		suggestionsDiv.html('');
		var data = obj.result.data;
		if (data.length > 0) {
			var htmlStr = '<div>';
			for ( var loop = 0; loop < data.length; loop = loop + 1) {
				htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
						+ data[loop].businessName
						+ '</a></li>';
			}
			htmlStr += '</div>';
			suggestionsDiv.append(htmlStr);
		} else {
			suggestionsDiv.append('<div id="">'
					+ 'No Business Names' + '</div>');
		}
		suggestionsDiv.css('left',thisInput.position().left);
		suggestionsDiv.css('top',thisInput.position().top + 25);
		suggestionsDiv.show();
		$('.select-teacher').click(
				function() {
					thisInput.val($(this).html());
					thisInput.attr('businessName', $(this)
							.attr('businessName'));
					$('#businessName').attr('value',$(this).attr('businessName'));
					suggestionsDiv.hide();
					var businessName = $('#businessName').val();
					$.post('customer.json','action=get-customer-full-name&businessName='+businessName, function(obj){
						$('#customerName').val(obj.result.data);
					
				});
		

				});
	});
	
       },

};
	