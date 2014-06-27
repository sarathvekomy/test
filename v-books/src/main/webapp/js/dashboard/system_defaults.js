var SystemDefaultsHandelr ={
		flag :true,
		count:1,
		mode : 'null',
		prevPattern:'null', 
		prevPayVal : 'null',
		prevAddVal : 'null',
		prevJournalVal :'null',
		prevProductCategory :'null',
		initPageLinks :function(){
			$('#default-types').pageLink({
				container : '.dashboard-page-container',
				url : 'dashboard/system-lookups/employee_types.jsp'
			});
		},
		Steps : [ '#employee-types-form'],
		Url : [ 'default.json'],
		StepCount : 0,
		initAddButtons : function(){
			$.fn.clear = function() {
				  return this.each(function() {
				    var type = this.type, tag = this.tagName.toLowerCase();
				    if (this.readOnly) return
				    if (tag == 'form')
				      return $(':input',this).clear();
				    if (type == 'text' || type == 'password' || tag == 'textarea')
				      this.value = '';
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
								$('form').clear();
								$('#valueValid').empty();
								$('#invoiceNoPeriodValid').empty();
								$('#invoiceNoValid').empty();
								$('#descValid').empty();
								$('#typeValid').empty();
								$(this).dialog('close');
							},
					Cancel: function() {
						$(this).dialog('close');
					}
						}
					});
				
			});
			$('#Edit-clear').click(function(){
				typeVal = $('#types').val();
				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('form').clearForm();
								$('#types').val(typeVal).attr('selected','selected');
								$('#valueValid').empty();
								$('#invoiceNoPeriodValid').empty();
								$('#invoiceNoValid').empty();
								$('#descValid').empty();
								$('#typeValid').empty();
								$(this).dialog('close');
							},
					Cancel: function() {
						$(this).dialog('close');
					}
						}
					});
				
			});
			$('#action-cancel').click(function() {
				$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
				$("#error-message").dialog({
									resizable : false,
									height : 140,
									title : "<span class='ui-dlg-confirm'>Confirm</span>",
									modal : true,
									buttons : {
										'Ok' : function() {
											$('.task-page-container').html('');
							    			var container ='.dashboard-page-container';
							    			var url = "dashboard/system-lookups/employee_types.jsp";
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
			$('.btn-save').click(function(){
				description = $('#description').val();
				type = $('#types').val();
				if(type == "Invoice No"){
					if(SystemDefaultsHandelr.validateInvoiceNumFields() == false){
						return false;
					}
					if(flag == false){
						showMessage({title:'Warning', msg:'Invice Number already configured'});
						return;
					}
				}else{
					if(type.toLowerCase() == "payment type"){
						if(SystemDefaultsHandelr.checkForPaymentTypes() == false){
							return false;
						}
						if(SystemDefaultsHandelr.validatePaymentTypes() == false){
						return false;	
						}
					}else if(type.toLowerCase() == "address type"){
						if(SystemDefaultsHandelr.checkForAddressTypes() == false){
							return false;
						}
						if(SystemDefaultsHandelr.validateAddressTypes() == false){
							return false;
						}
					}else if(type.toLowerCase() == "journal type"){
						if(SystemDefaultsHandelr.validatJournalTypes() == false){
							return true;
						}
						if(SystemDefaultsHandelr.validateInvoiceNo() == false){
							return true;
						}
							
					}else if(type.toLowerCase() == "product category"){
						if(SystemDefaultsHandelr.checkForProductCategories() == false){
							return false;
						}
						if(SystemDefaultsHandelr.validateProductCategories() == false){
							return false;
						}
					}
					SystemDefaultsHandelr.validateDefaults();
					var resultSuccess=true;
					var resultFailure=false;
					if(SystemDefaultsHandelr.flag == false){
						return resultSuccess;
					}
					if(SystemDefaultsHandelr.validateDefaults()==false){
						return resultSuccess;
					}
				}
				
				var paramString='action=add-employee-type&value='+value+'&description='+description;
				if(type =='Employee Type'){
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
				}else if(type =='Payment Type'){
					var paymentValue = $('#paymentValue').val();
					var paramString='action=add-payment-type&value='+paymentValue+'&description='+description;
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}else if(type == 'Address Type'){
					var addValue = $('#addressValue').val();
					var paramString='action=add-address-type&value='+addValue+'&description='+description;
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}
				else if(type == 'Journal Type'){
					if(SystemDefaultsHandelr.checkForJournalTypes() == false){
						return false;
					}
					var value = $('#value').val();
					var invoiceNo=$('#invoiceNo').val();
					var paramString='action=add-journals&value='+value+'&description='+description+'&invoiceNo='+invoiceNo;
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}
				else if(type == 'Invoice No'){
					var invoiceValue = $('#invoiceValue').val();
					var invoiceNoPeriod=$('#invoiceNoPeriod').val();
					var paramString='action=add-invoice-period&value='+invoiceValue+'&description='+description+'&invoiceNoPeriod='+invoiceNoPeriod;
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}else if(type == 'Product Category'){
					var productCategory = $('#productValue').val();
					var paramString = 'action=save-product-categories&value='+productCategory+'&description='+description;
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
				}
			});
			 $('.btn-update').click(function(){
				 var resultSuccess = true;
				 var id = $('#idVal').val();
					var value=$('#value').val();
					var desc = $('#description').val();	
					var type =$('#types').val();
					if(SystemDefaultsHandelr.flag == false||SystemDefaultsHandelr.validateDefaults()==false){
						return resultSuccess;
					}else{
						resultSuccess = true;
					}
					if(type == "Employee Type"){
					$.post('default.json','action=update-emp-types&id='+id+'&value='+value+'&description='+desc,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
			       }else if(type == "Address Type"){
			    	   var addValue = $('#addressValue').val();
			    	   if(SystemDefaultsHandelr.validateAddressTypes() == false){
							return false;
						}
			    	   currentAddVal = $('#addressValue').val();
			    	   if(prevAddVal != currentAddVal){
							 if(SystemDefaultsHandelr.checkForAddressTypes() == false){
								return false; 
							 }
						 }
				    $.post('default.json','action=update-add-types&id='+id+'&value='+addValue+'&description='+desc,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
			     }else if(type == "Payment Type"){
			    	 var payVal = $('#paymentValue').val();
			    	 if(SystemDefaultsHandelr.validatePaymentTypes() == false){
							return false;	
						}
			    	   if(prevPayVal != payVal){
							 if(SystemDefaultsHandelr.checkForPaymentTypes()() == false){
								return false; 
							 }
						 }
			    	 $.post('default.json','action=update-pay-types&id='+id+'&value='+payVal+'&description='+desc,function(obj){
							$(this).successMessage({
								container : '.'+$('#cont').attr('class'),
								data : obj.result.message
							});
						}); 
			     }else if(type == "Journal Type"){
			    		if(SystemDefaultsHandelr.validatJournalTypes() == false){
							return true;
						}
			    		 currentPattern = $('#invoiceNo').val();
						 if(prevPattern != currentPattern){
							 if(SystemDefaultsHandelr.validateInvoiceNo() == false){
									return true;
								}
						 }
						
			    	 var invoiceno =$('#invoiceNo').val();
			    	 $.post('default.json','action=update-journal-types&id='+id+'&value='+value+'&description='+desc+'&invoiceNo='+invoiceno,function(obj){
							$(this).successMessage({
								container : '.'+$('#cont').attr('class'),
								data : obj.result.message
							});
						}); 
			     }else if(type == "Invoice No"){
			    	 if(SystemDefaultsHandelr.validateInvoiceNumFields() == false){
							return false;
						}
			    	 var invoiceVal = $('#invoiceValue').val();	
			    	 var period = $('#invoiceNoPeriod').val();
			    	 $.post('default.json','action=update-invoice-numbers&id='+id+'&value='+invoiceVal+'&description='+desc+'&period='+period,function(obj){
							$(this).successMessage({
								container : '.'+$('#cont').attr('class'),
								data : obj.result.message
							});
						}); 
			     }else if(type == "Product Category"){
			    	 var prodCategory = $('#productValue').val();
			    	 if(prevProductCategory != prodCategory){
			    		 if(SystemDefaultsHandelr.checkForProductCategories() == false){
								return false;
							}
			    	 }
			    	
						if(SystemDefaultsHandelr.validateProductCategories() == false){
							return false;
						}
			    	 var description = $('#description').val();
			    	 $.post('default.json','action=update-product-categories&id='+id+'&value='+prodCategory+'&description='+description,function(obj){
			    		 $(this).successMessage({
								container : '.'+$('#cont').attr('class'),
								data : obj.result.message
							});
			    	 });
			     }
				});
			$('.exp-coll-pay').die('click').live('click', function() {
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.pay-bar').css('width','700px');
					$('.pay-row').css('overflow-y','hidden');
					$('.pay-row').css('overflow-x','hidden');
					$('.pay-title').css('margin-left','-660px');
					if($('#pay').length==0){
						$('#pay-search-results-list').css("height","30px");
					}else{
						$('#pay').toggle();
					}
				}else if($(this).hasClass('collapse-icon')){ 
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.pay-bar').css('width','690px');
					$('.pay-row').css('overflow-y','auto');
					$('.pay-row').css('overflow-x','hidden');
					$('.pay-title').css('margin-left','-660px');
					if($('#pay').length==0){
						$('#pay-search-results-list').css("height","90px");
					}else{
						$('#pay').toggle();
					}
				}
			});
			$('.exp-coll-emp').die('click').live('click', function() {
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.emp-bar').css('width','700px');
					$('.emp-row').css('overflow-y','hidden');
					$('.emp-row').css('overflow-x','hidden');
					$('.emp-title').css('margin-left','-660px');
					if($('#emp').length==0){
						$('#emp-search-results-list').css("height","30px");
					}else{
						$('#emp').toggle();
					}
				} else{
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.emp-bar').css('width','690px');
					$('.emp-row').css('overflow-y','hidden');
					$('.emp-title').css('margin-left','-660px');
					if($('#emp').length==0){
						$('#emp-search-results-list').css("height","90px");
					}else{
						$('#emp').toggle();
					}
				}
			});
			$('.exp-coll-add').die('click').live('click', function() {
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.add-bar').css('width','700px');
					$('.add-row').css('overflow-y','hidden');
					$('.add-row').css('overflow-x','hidden');
					$('.add-title').css('margin-left','-660px');
					if($('#add').length==0){
						$('#add-search-results-list').css("height","30px");
					}else{
						$('#add').toggle();
					}
					
				} else {
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.add-bar').css('width','690px');
					$('.add-row').css('overflow-y','auto');
					$('.add-row').css('overflow-x','hidden');
					$('.add-title').css('margin-left','-660px');
					if($('#add').length==0){
						$('#add-search-results-list').css("height","90px");
					}else{
						 
						$('#add').toggle();
					}
				}
			});
			$('.exp-coll-journals').die('click').live('click', function() {
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.journals-bar').css('width','700px');
					$('.journals-row').css('overflow-y','hidden');
					$('.journals-row').css('overflow-x','hidden');
					$('.journals-title').css('margin-left','-660px');
					if($('#journals').length==0){
						$('#journal-search-results-list').css("height","30px");
					}else{
						$('#journals').toggle();
					}
					
				} else {
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.journals-bar').css('width','690px');
					$('.journals-row').css('overflow-y','auto');
					$('.journals-row').css('overflow-x','hidden');
					$('.journals-title').css('margin-left','-660px');
					if($('#journals').length==0){
						$('#journal-search-results-list').css("height","90px");
					}else{
						$('#journals').toggle();
					}
				}
			});
			$('.exp-coll-invoice').die('click').live('click',function(obj){
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.invoice-bar').css('width','700px');
					$('.invoice-row').css('overflow-y','hidden');
					$('.invoice-row').css('overflow-x','hidden');
					$('.invoice-title').css('margin-left','-660px');
					if($('#invoice').length==0){
						$('#invoice-search-results-list').css("height","30px");
					}else{
						$('#invoice').toggle();
					}
					
				} else {
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.invoice-bar').css('width','690px');
					$('.invoice-row').css('overflow-y','auto');
					$('.invoice-row').css('overflow-x','hidden');
					$('.invoice-title').css('margin-left','-660px');
					if($('#invoice').length==0){
						$('#invoice-search-results-list').css("height","90px");
					}else{
						$('#invoice').toggle();
					}
				}
			
			});
			$('.exp-coll-product').die('click').live('click', function() {
				if($(this).hasClass('expand-icon')) {
					$(this).removeClass('expand-icon');
					$(this).addClass('collapse-icon');
					$('.product-bar').css('width','700px');
					$('.product-row').css('overflow-y','hidden');
					$('.product-row').css('overflow-x','hidden');
					$('.product-title').css('margin-left','-660px');
					if($('#product').length==0){
						$('#product-search-results-list').css("height","30px");
					}else{
						$('#product').toggle();
					}
					
				} else {
					$(this).removeClass('collapse-icon');
					$(this).addClass('expand-icon');
					$('.product-bar').css('width','690px');
					$('.product-row').css('overflow-y','auto');
					$('.product-row').css('overflow-x','hidden');
					$('.product-title').css('margin-left','-660px');
					if($('#product').length==0){
						$('#product-search-results-list').css("height","90px");
					}else{
						$('#product').toggle();
					}
				}
			});
			 $('#value').change(function(){
				 SystemDefaultsHandelr.checkForJournalTypes();
			 });
				  /*if(type =='Employee Type'){
						$.post('default.json','action=validate-employee-type&value='+value,function(obj){
							var result = obj.result.data;
							if(result.length>0){
								$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#value").focus(function(event){
									 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
										 $('#journalvalue_pop').hide();
										 $('#payvalue_pop').hide();
										 $('#addvalue_pop').hide();
										 $('#valuesp_pop').hide();
										 $('#value_pop').hide();
										 $('#valueValid').empty();
										 $('#empvalue_pop').show();
									 }
								});
								$("#value").blur(function(event){
									 $('#empvalue_pop').hide();
									 if(/^[a-zA-Z0-9\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(-1)==" "){
										 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#value").focus(function(event){
											 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
												 $('#journalvalue_pop').hide();
												 $('#payvalue_pop').hide();
												 $('#addvalue_pop').hide();
												 $('#valuesp_pop').hide();
												$('#value_pop').hide();
												$('#valueValid').empty();
												 $('#empvalue_pop').show();
											 }
											});
									 }else{
										 $('#empvalue_pop').hide();
									 }
								});
								SystemDefaultsHandelr.flag =false;
							}else{
								SystemDefaultsHandelr.flag =true;
							}
							
						});
				  }*/
					
			 $('#addressValue').change(function(){
				 currentAddVal = $("#addressValue").val();
					 if(prevAddVal != currentAddVal){
						 SystemDefaultsHandelr.checkForAddressTypes();
					 }
			 });
			 $('#paymentValue').change(function(){
				 currentPayVal = $('#paymentValue').val();
				 if(prevPayVal != currentPayVal){
					 SystemDefaultsHandelr.checkForPaymentTypes();
				 }
			 });
			 $('#invoiceNo').change(function(){
				 currentPattern = $('#invoiceNo').val();
				 if(prevPattern != currentPattern){
					 SystemDefaultsHandelr.validateInvoiceNo();
				 }
				 
			 });
			 $('#types').change(function(){
				 $('#valueValid').empty();
				 $('#value').val('');
				 $('#paymentValue').val('');
				 $('#addressValue').val('');
				 $('#invoiceNoValid').empty();
					var type=$('#types').val();
					if(type.toLowerCase() == "journal type"){
						$('#invoicevalue').hide();
						$('#payment').hide();
						$('#address').hide();
						$('#jValue').show();
						$('#value').attr('class','mandatory');
						$('#invoiceNum').show();
						$('#invoiceNo').attr('class','mandatory');
						$('#invoiceNoPeriod').hide();
						$('#invoiceNoPeriodLabel').hide();
					}else if(type.toLowerCase() == "address type"){
						$('#invoiceNoPeriodLabel').hide();
						$('#invoiceNum').hide();
						$('#addressValue').attr('class','mandatory');
						$('#jValue').hide();
						$('#invoicevalue').hide();
						$('#payment').hide();
						$('#address').show();
					}else if(type.toLowerCase() == "payment type"){
						$('#invoiceNoPeriodLabel').hide();
						$('#invoiceNum').hide();
						$('#paymentValue').attr('class','mandatory');
						$('#invoicevalue').hide();
						$('#jValue').hide();
						$('#address').hide();
						$('#payment').show();
					}
					else if(type == "Invoice No") {
						$('#invoiceValue').attr('class','mandatory');
						$('#invoiceNum').hide();
						$('#jValue').hide();
						$('#address').hide();
						$('#payment').hide();
						$('#invoicevalue').show();
						
						if(typeof mode == 'undefined'){
							$.post('default.json','action=Check-Invoice-Number-Existence',function(obj){
								var result = obj.result.data;
								if(result == 'true'){
									showMessage({title:'Warning', msg:'Invice Number already configured'});
									flag = false;
								}else{
									flag = true;
								}
							});
						}
						
						$('#invoiceNum').hide();
						$('#invoiceNo').removeAttr('class');
						$('#invoiceNoPeriod').show();
						$('#invoiceNoPeriodLabel').show();
					}else if(type == "Product Category"){
						$('#invoiceNoPeriodLabel').hide();
						$('#invoiceNum').hide();
						$('#productValue').attr('class','mandatory commonValue');
						$('#jValue').hide();
						$('#invoicevalue').hide();
						$('#payment').hide();
						$('#productCategory').show();
					}
					else {
						$('#invoiceNum').hide();
						$('#invoiceNo').removeAttr('class');
						$('#invoiceNoPeriod').hide();
						$('#invoiceNoPeriodLabel').hide();
					}
				});
			/* $('#default-types').unbind('click').bind('click', function (e){
					$('.dashboard-page-container').load('dashboard/system-lookups/employee_types.jsp');
				});*/
		},
		checkForPaymentTypes : function(){
			var res = true;
		 	var payVal = $('#paymentValue').val();
			$.ajax({type: "POST",
				url:'default.json', 
				data: 'action=validate-payment-type&value='+payVal,
				async : false,
				success :function(obj) {
				var result = obj.result.data;
				if(result.length>0){
					$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#paymentValue").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#addvalue_pop').hide();
							$('#value_pop').hide();
							$('#valuesp_pop').hide();
							$('#valueValid').empty();
							 $('#payvalue_pop').show();
						
					});
					$("#paymentValue").blur(function(event){
						 $('#payvalue_pop').hide();
						 if(/^[a-zA-Z0-9\s]+$/.test($('#paymentValue').val())==false || ($('#paymentValue').val()).length == 0||$('#paymentValue').val().charAt(0)==" "||$('#paymentValue').val().charAt(-1)==" "){
							 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#paymentValue").focus(function(event){
									 $('#journalvalue_pop').hide();
									 $('#empvalue_pop').hide();
									 $('#addvalue_pop').hide();
									$('#value_pop').show();
									$('#valuesp_pop').hide();
									$('#valueValid').empty();
									 $('#payvalue_pop').hide();
								
								});
						 }else{
							 $('#payvalue_pop').hide();
						 }
					});
					result = '';
					res = false;
				}else{
					res = true;
				}
			},
			});
			return res;
		},
		checkForAddressTypes : function(){
			vlen=$('#addressValue').val().length;
			var res = true;
		 	var addressVal = $("#addressValue").val();
			$.ajax({type: "POST",
				url:'default.json', 
				data: 'action=validate-address-type&value='+addressVal,
				async : false,
				success :function(obj) {
				var result = obj.result.data;
				if(result.length>0){
					$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#addressValue").focus(function(event){
							$('#value_pop').hide();
							$('#valuesp_pop').hide();
							$('#valueValid').empty();
							 $('#addvalue_pop').show();
						 
					});
				$("#addressValue").blur(function(event){
					 $('#value_pop').hide();
						 $('#addvalue_pop').hide();
						 if(/^[a-zA-Z0-9\s]+$/.test($('#addressValue').val())==false || ($('#addressValue').val()).length == 0||$('#addressValue').val().charAt(0)==" "||$('#addressValue').val().charAt(vlen-1)==" "){
							 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#addressValue").focus(function(event){
										$('#journalvalue_pop').hide();
										 $('#empvalue_pop').hide();
										 $('#payvalue_pop').hide();
										 $('#addvalue_pop').hide();
										$('#value_pop').hide();
										$('#valuesp_pop').hide();
										$('#valueValid').empty();
										 $('#addvalue_pop').show();
								});
						 }else{
							 $('#addvalue_pop').hide();
						 }
					});
					res = false;
				}else{
					res = true;
				}
				},
			});
			return res;
		},
		checkForJournalTypes : function(){
			var res = true;
			 var value = $('#value').val(); 
			 $.ajax({type: "POST",
					url:'default.json', 
					data: 'action=validate-journal-type&value='+value,
					async : false,
					success :function(obj) {
					var jResult = obj.result.data;
					if(jResult != undefined){
						$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						$("#value").focus(function(event){
							 $('#value_pop').hide();
								$('#valueValid').empty();
								 $('#journalvalue_pop').show();
							
						});
						$("#value").blur(function(event){
							 $('#journalvalue_pop').hide();
							 if(/^[a-zA-Z0-9\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(-1)==" "){
								 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								 $("#value").focus(function(event){
									 $('#value_pop').hide();
										$('#valueValid').empty();
										 $('#journalvalue_pop').show();
									});
							 }else{
								 $('#journalvalue_pop').hide();
							 }
						});
						res = false;
					}else{
						$('#valueValid').empty();
						res = true;
					}
				},
				});
				return res;
		 
		},
		checkForProductCategories : function(){
			vlen=$('#productValue').val().length;
			var res = true;
		 	var productVal = $("#productValue").val();
			$.ajax({type: "POST",
				url:'default.json', 
				data: 'action=check-product-category-existance&value='+productVal,
				async : false,
				success :function(obj) {
				var result = obj.result.data;
				if(result == 'false'){
					$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#productValue").focus(function(event){
							$('#value_pop').hide();
							$('#valuesp_pop').hide();
							$('#valueValid').empty();
							 $('#productvalue_pop').show();
						 
					});
				$("#productValue").blur(function(event){
					 $('#value_pop').hide();
						 $('#productvalue_pop').hide();
						 if(/^[a-zA-Z0-9\s]+$/.test($('#productValue').val())==false || ($('#productValue').val()).length == 0||$('#productValue').val().charAt(0)==" "||$('#productValue').val().charAt(vlen-1)==" "){
							 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#productValue").focus(function(event){
										$('#journalvalue_pop').hide();
										 $('#empvalue_pop').hide();
										 $('#payvalue_pop').hide();
										 $('#addvalue_pop').hide();
										$('#value_pop').hide();
										$('#valuesp_pop').hide();
										$('#valueValid').empty();
										 $('#productvalue_pop').show();
								});
						 }else{
							 $('#addvalue_pop').hide();
						 }
					});
					res = false;
				}else{
					res = true;
				}
				},
			});
			return res;
		},
		validateDefaults : function(){
			var result=true;
			if($('#description').val()!=""||$('#description').val().length>255){
			if($('#description').val().charAt(0)==" "){
				$('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#description").focus(function(event){
						$('#descValid').empty();
						 $('#desc_pop').show();
					});
					$("#description").blur(function(event){
						 $('#desc_pop').hide();
						 if($('#description').val().charAt(0)==" "){
							 $('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#description").focus(function(event){
									$('#descValid').empty();
									 $('#desc_pop').show();
								});
						 }else{
						 }
					});
					result =false; 
			}
			}
			if( $('#types').val()=='-1'||$('#types').val() == null ||$('#types').val() == ""){
				$('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#types").focus(function(event){
					$('#typeValid').empty();
					 $('#Type_pop').show();
				});
				$("#types").blur(function(event){
					 $('#Type_pop').hide();
					 if($('#types').val() == '-1'||$('#types').val()==null){
						 $('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#types").focus(function(event){
								$('#typeValid').empty();
								 $('#Type_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}
			if($('#types').val()=="Journal Type"){}
			return result;
		},
		validateInvoiceNumFields : function(){
			var result = true;
			if($('#invoiceValue').val().length == 0 ||/^[0-9\s]+$/.test($('#invoiceValue').val()) == false){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#invoiceValue").focus(function(event){
					$('#valueValid').empty();
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					$('#valuelen_pop').hide();
					 $('#valuesp_pop').hide();
					 $('#value_pop').hide();
					 $('#invoicevalue_pop').show();
				});
				$("#invoiceValue").blur(function(event){
					 $('#invoicevalue_pop').hide();
					 if(/^[0-9\s]+$/.test($('#invoiceValue').val())==false || ($('#invoiceValue').val()).length == 0){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#invoiceValue").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
							 $('#addvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#valueValid').empty();
								 $('#invoicevalue_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}
			if($('#invoiceNoPeriod').val().length == 0 ||/^[0-9\s]+$/.test($('#invoiceNoPeriod').val()) == false){
				$('#invoiceNoPeriodValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#invoiceNoPeriod").focus(function(event){
					$('#invoiceNoPeriodValid').empty();
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					$('#valuelen_pop').hide();
					 $('#valuesp_pop').hide();
					 $('#value_pop').hide();
					 $('#invoiceNoPeriod_pop').show();
				});
				$("#invoiceNoPeriod").blur(function(event){
					 $('#invoiceNoPeriod_pop').hide();
					 if(/^[0-9\s]+$/.test($('#invoiceNoPeriod').val())==false || ($('#invoiceNoPeriod').val()).length == 0){
						 $('#invoiceNoPeriodValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#invoiceNoPeriod").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
							 $('#addvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#invoiceNoPeriodValid').empty();
								 $('#invoiceNoPeriod_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}
			if($('#description').val()!=""||$('#description').val().length>255){
				if(/^[a-zA-Z\s]+$/.test($('#description').val())==false||$('#description').val().charAt(0)==" "){
					$('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						$("#description").focus(function(event){
							$('#descValid').empty();
							 $('#desc_pop').show();
						});
						$("#description").blur(function(event){
							 $('#desc_pop').hide();
							 if(/^[a-zA-Z\s]+$/.test($('#description').val())==false||$('#description').val().charAt(0)==" "){
								 $('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								 $("#description").focus(function(event){
										$('#descValid').empty();
										 $('#desc_pop').show();
									});
							 }else{
							 }
						});
						result =false; 
				}
				}
			return result;
		},
		validatePaymentTypes : function(){
			var result=true;
			var vlen=$('#paymentValue').val().length;
			if($('#paymentValue').val().length == 0 ||/^[a-zA-Z\s]+$/.test($('#paymentValue').val()) == false||$('#paymentValue').val().charAt(0)==" "||$('#paymentValue').val().charAt($('#paymentValue').val().length-1) == ""){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#paymentValue").focus(function(event){
					 $('#payvalue_pop').hide();
					$('#valueValid').empty();
					 $('#value_pop').show();
				});
				$("#paymentValue").blur(function(event){
					 $('#value_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#paymentValue').val())==false || ($('#paymentValue').val()).length == 0||$('#paymentValue').val().charAt(0)==" "||$('#paymentValue').val().charAt($('#paymentValue').val().length-1) == ""){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#paymentValue").focus(function(event){
							 $('#payvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#valueValid').empty();
								 $('#value_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}if($('#paymentValue').val().length>30){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#paymentValue").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					 $('#value_pop').hide();
					$('#valueValid').empty();
					 $('#valuelen_pop').show();
				});
				$("#paymentValue").blur(function(event){
					 $('#valuelen_pop').hide();
					 if($('#paymentValue').val().length>30){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#paymentValue").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
							 $('#addvalue_pop').hide();
							 $('#value_pop').hide();
								$('#valueValid').empty();
								 $('#valuelen_pop').show();
							});
					 }	
					 else{
					 }
				});
				result =false;
			}
			return result;
		},
		validateAddressTypes : function(){
			var result=true;
			var vlen=$('#addressValue').val().length;
			if($('#addressValue').val().length == 0 ||/^[a-zA-Z\s]+$/.test($('#addressValue').val()) == false||$('#addressValue').val().charAt(0)==" "||$('#addressValue').val().charAt($('#addressValue').val().length - 1) == ""){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#addressValue").focus(function(event){
					 $('#addvalue_pop').hide();
					$('#valueValid').empty();
					 $('#value_pop').show();
				});
				 $('#addressValue').blur(function(){
					 $('#value_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#addressValue').val())==false || ($('#addressValue').val()).length == 0||$('#addressValue').val().charAt(0)==" "||$('#addressValue').val().charAt($('#addressValue').val().length - 1) == ""){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#addressValue").focus(function(event){
							 $('#addvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#valueValid').empty();
								 $('#value_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}if($('#addressValue').val().length>30){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#addressValue").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					 $('#value_pop').hide();
					$('#valueValid').empty();
					 $('#valuelen_pop').show();
				});
				$("#addressValue").blur(function(event){
					 $('#valuelen_pop').hide();
					 if($('#addressValue').val().length>30){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#addressValue").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
							 $('#addvalue_pop').hide();
							 $('#value_pop').hide();
								$('#valueValid').empty();
								 $('#valuelen_pop').show();
							});
					 }	
					 else{
					 }
				});
				result =false;
			}
		return result;
		},
		validatJournalTypes : function(){
			var result=true;
			var vlen=$('#value').val().length;
			if($('#value').val().length == 0 ||/^[a-zA-Z\s]+$/.test($('#value').val()) == false||$('#value').val().charAt(0)==" "||$('#value').val().charAt(vlen-1) == ""){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#value").focus(function(event){
					$('#journalvalue_pop').hide();
					$('#valueValid').empty();
					 $('#value_pop').show();
				});
				 $('#value').blur(function(){
					 $('#value_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(vlen-1) == ""){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#value").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#valueValid').empty();
								 $('#value_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}if($('#value').val().length>30){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#value").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#value_pop').hide();
					$('#valueValid').empty();
					 $('#valuelen_pop').show();
				});
				$("#value").blur(function(event){
					 $('#valuelen_pop').hide();
					 if($('#value').val().length>30){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#value").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#value_pop').hide();
								$('#valueValid').empty();
								 $('#valuelen_pop').show();
							});
					 }	
					 else{
					 }
				});
				result =false;
			}

			vlen=$('#invoiceNo').val().length;
			if($('#invoiceNo').val().length == 0||/^[a-zA-Z]+$/.test($('#invoiceNo').val())==false||$('#invoiceNo').val().charAt(0)==" "||$('#invoiceNo').val().charAt(vlen-1)==" "){
				$('#invoiceNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#invoiceNo").focus(function(event){
					$('#invoiceNoValid').empty();
					 $('#invoiceNo_pop').show();
				});
				$("#invoiceNo").blur(function(event){
					 $('#invoiceNo_pop').hide();
					 if($('#invoiceNo').val().length == 0||/^[a-zA-Z]+$/.test($('#invoiceNo').val())==false||$('#invoiceNo').val().charAt(0)==" "||$('#invoiceNo').val().charAt(vlen-1)==" "){
						 $('#invoiceNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#invoiceNo").focus(function(event){
								$('#invoiceNoValid').empty();
								 $('#invoiceNo_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}
		
		return result;
		
		},
		validateInvoiceNo : function(){
			var invoiceNo = $('#invoiceNo').val();
			var result =true;
			$.ajax({type : "POST",
				url : 'default.json',
				data : 'action=check-invoiceno-availability&invoiceNo='+invoiceNo,
				async : false,
				success : function(obj) {
				if(obj.result.data == "true"){
					$('#invoiceNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#invoiceNo").focus(function(event){
						$('#invoiceNoValid').empty();
						 $('#invoiceNo_pop').hide();
						 $('#invoiceNoValid_pop').show();
					});
					$("#invoiceNo").blur(function(event){
						 $('#invoiceNoValid_pop').hide();
					});
					result = false;
				}
				},
			});
			return result;
		},
		validateProductCategories : function(){
			var result=true;
			var vlen=$('#productValue').val().length;
			if($('#productValue').val().length == 0 ||/^[a-zA-Z\s]+$/.test($('#productValue').val()) == false||$('#productValue').val().charAt(0)==" "||$('#productValue').val().charAt($('#productValue').val().length - 1) == ""){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#productValue").focus(function(event){
					 $('#addvalue_pop').hide();
					$('#valueValid').empty();
					 $('#value_pop').show();
				});
				 $('#productValue').blur(function(){
					 $('#value_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#productValue').val())==false || ($('#productValue').val()).length == 0||$('#productValue').val().charAt(0)==" "||$('#productValue').val().charAt($('#productValue').val().length - 1) == ""){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#productValue").focus(function(event){
							 $('#addvalue_pop').hide();
							 $('#valuesp_pop').hide();
							 $('#valuelen_pop').hide();
								$('#valueValid').empty();
								 $('#value_pop').show();
							});
					 }else{
					 }
				});
				result =false; 
			}if($('#productValue').val().length>30){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#productValue").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					 $('#value_pop').hide();
					$('#valueValid').empty();
					 $('#valuelen_pop').show();
				});
				$("#productValue").blur(function(event){
					 $('#valuelen_pop').hide();
					 if($('#productValue').val().length>30){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#productValue").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
							 $('#addvalue_pop').hide();
							 $('#value_pop').hide();
								$('#valueValid').empty();
								 $('#valuelen_pop').show();
							});
					 }	
					 else{
					 }
				});
				result =false;
			}
		return result;
		
		},
		getAllExistedTypes: function(){
			$.post('default.json','action=get-all-payment-types',function(obj){
				var result = obj.result.data;
				if(typeof result != "undefined"){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="pay-col green-result-col-1">'+
					'<div class="pay-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;overflow-y:hidden;overflow-x:hidden">'+
					'<div class="pay-bar green-title-bar" style="width:695px;background:#b4d8f2">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-pay expand-icon" id="defaults-exp-coll"style="float:left;margin-left:663px;">'+
					'<div class="pay-title  result-title" style="height:40px;width:690px;float:left;margin-left:-655px;line-height:27px;">'+Msg.DELIVERY_NOTE_PAYMENT_TYPES+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="pay">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style="float:left;margin-top:8px; margin-left:25px;">'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right; margin-right:5px;">'+
						'<div idval="'+result[loop]+'" class="pay-edit  edit-icon" title="Edit Payment Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop]+'" class="pay-delete  delete-icon delete-organization" title="Delete Payment Types"style="margin-top:-21px;"></div>'+
						'</div>';
						$('#pay-search-results-list').html('');
						$('#pay-search-results-list').append(rowstr);
					};
					SystemDefaultsHandelr.load();
				SystemDefaultsHandelr.initSearchResultButtons();
		     } else{
		    	 $('#pay-search-results-list').html('');
				    $('#pay-search-results-list').append('<div class="pay-col green-result-col-1"><div class="pay-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="pay-bar green-title-bar" style="width:690px;background:#b4d8f2 "><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-pay expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;"><div class="pay-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Payment Types</div></div></div>');
					SystemDefaultsHandelr.load();
			     }
		          
			});
			
			//$.post('default.json','action=get-all-employee-types',function(obj){
//				var employeeTypes = obj.result.data;
//				if(employeeTypes.length >0 ){
//					var rowstr ='<div class=table-field" style="height:auto;">'+
//					'<div class="emp-col green-result-col-1">'+
//					'<div class="emp-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;overflow-y:auto;overflow-x:hidden; ">'+
//					'<div class="emp-bar green-title-bar" style="width:675px;background:#b4d8f2">'+
//					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
//					'<div class="exp-coll-emp expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
//					'<div class="emp-title result-title" style="width:690px;float:left;margin-left:-665px;line-height:27px; ">'+Msg.EMPLOYEE_EMPLOYEE_TYPES+'</div>'+
//					'</div>'+
//					'</div>'+
//					'<div>'+
//					'</div>'+
//					'</div>'+
//					'<div class="test" id="emp">';
//					for(var loop=0;loop<employeeTypes.length;loop = loop+1){
//						rowstr = rowstr	+
//						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
//						'<div style="float:left;margin-top:8px; margin-left:25px;">'+
//						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+employeeTypes[loop]+'</span>'+
//						'</div>'+
//						'<div>' + 
//						'</div>'+
//						'</div>'+
//						'<div style="float:right; margin-right:5px;">'+
//						'<div idval="'+employeeTypes[loop]+'" class="edit-emp  edit-icon" title="Edit Payment Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
//						'<div idval="'+employeeTypes[loop]+'" class="delete-emp  delete-icon delete-organization" title="Delete Payment Types"style="margin-top:-21px;"></div>'+
//						'</div>';
//						$('#emp-search-results-list').html('');
//						$('#emp-search-results-list').append(rowstr);
//						
//					};
//					SystemDefaultsHandelr.empload();
//				SystemDefaultsHandelr.initemployeeButtons();
//				}
//				else{
//					$('#emp-search-results-list').html('');
//					$('#emp-search-results-list').append('<div class="emp-col green-result-col-1"><div class="emp-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="emp-bar green-title-bar" style="width:690px;background:#b4d8f2"><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-emp expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;"><div class="emp-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Employee Types</div></div></div>');
//					SystemDefaultsHandelr.empload();
//			     }
//				
//			});
			$.post('default.json','action=get-all-address-types',function(obj){
				 result = obj.result.data;
				if(result.length > 1){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="add-col green-result-col-1">'+
					'<div class="add-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="add-bar green-title-bar" style="width:675px;background:#b4d8f2 ">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-add expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="add-title  result-title" style="width:690px;float:left;margin-left:-665px;line-height:27px;">'+Msg.ADDRESS_TYPE_LABEL+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="add">';
					for(var loop=0;loop<result.length-1;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style="float:left;margin-top:8px; margin-left:25px;">'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop].addressType+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right; margin-right:5px;">'+
						'<div idval="'+result[loop].addressType+'" class="edit-add  edit-icon" title="Edit Address Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop].addressType+'" class="delete-add  delete-icon delete-organization" title="Delete Address Types"style="margin-top:-21px;"></div>'+
						'</div>';
						$('#add-search-results-list').html('');
						$('#add-search-results-list').append(rowstr);
						
					};
					SystemDefaultsHandelr.addload();
				SystemDefaultsHandelr.initSearchaddressButtons();
				}else{
					$('#add-search-results-list').html('');
					$('#add-search-results-list').append('<div class="add-col green-result-col-1"><div class="add-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px; "><div class="add-bar green-title-bar" style="width:690px;background:#b4d8f2; "><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-add expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="add-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Address Types</div></div></div>');
					SystemDefaultsHandelr.addload();
			     }
			});
			$.post('default.json','action=get-all-journal-types',function(obj){
				var result = obj.result.data;
				if(typeof result != "undefined"){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="journals-col green-result-col-1">'+
					'<div class="journals-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="journals-bar green-title-bar" style="width:675px;background:#b4d8f2  ">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-journals expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="journals-title  result-title" style="width:690px;float:left;margin-left:-665px;line-height:27px;">'+Msg.JOURNAL_TYPES+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="journals">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style="float:left;margin-top:8px; margin-left:25px;">'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right; margin-right:5px;">'+
						'<div idval="'+result[loop]+'" class="edit-journals  edit-icon" title="Edit journal Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop]+'" class="delete-journals  delete-icon delete-organization" title="Delete journal Types"style="margin-top:-21px;"></div>'+
						'</div>';
						$('#journal-search-results-list').html('');
						$('#journal-search-results-list').append(rowstr);
						
					};
					SystemDefaultsHandelr.journalsload();
				SystemDefaultsHandelr.initSearchjournalsButtons();
				}else{
					$('#journal-search-results-list').html('');
					$('#journal-search-results-list').append('<div class="journals-col green-result-col-1"><div class="journals-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="journals-bar green-title-bar" style="width:690px; background:#b4d8f2 "><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-journals expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="journals-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Journal Types</div></div></div>');
					SystemDefaultsHandelr.journalsload();
			     }
			});
			$.post('default.json','action=get-existed-invoice-numbers',function(obj){
				result = obj.result.data;
				if(typeof result != "undefined"){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="invoice-col green-result-col-1">'+
					'<div class="invoice-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="invoice-bar green-title-bar" style="width:675px;background:#b4d8f2  ">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-invoice expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="invoice-title  result-title" style="width:690px;float:left;margin-left:-665px;line-height:27px;">Invoice Numbers</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="invoice">';
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style="margin-top:8px; margin-left:25px;">'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+'<span class="property">Period:</span>'+result.period+' </span>'+
						'<div class="green-result-col-1">'+
						'<div class="result-body">' +
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+'<span class="property">Start Value:</span>'+result.value+'</span>'+
						'</div>'+
						'</div>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right; margin-right:5px;">'+
						'<div idval="'+result.id+'" startVal ="'+result.value+'" period="'+result.period+'" description ="'+result.description+'" class="edit-invoice  edit-icon" title="Edit Invoice Numbers" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result.id+'" class="delete-invoice  delete-icon delete-organization" title="Delete Invoice Numbers"style="margin-top:-21px;"></div>'+
						'</div>';
						$('#invoice-search-results-list').html('');
						$('#invoice-search-results-list').append(rowstr);
						
					SystemDefaultsHandelr.invoiceNoload();
				SystemDefaultsHandelr.initSearchInvoiceButtons();
				
				}else {
					$('#invoice-search-results-list').html('');
					$('#invoice-search-results-list').append('<div class="invoice-col green-result-col-1"><div class="invoice-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="invoice-bar green-title-bar" style="width:690px; background:#b4d8f2 "><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-invoice expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="invoice-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Invoice Types</div></div></div>');
					SystemDefaultsHandelr.invoiceNoload();
				}
			});
			$.post('default.json','action=get-all-product-categories',function(obj){

				result = obj.result.data;
				if(result != ""){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="product-col green-result-col-1">'+
					'<div class="product-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="product-bar green-title-bar" style="width:675px;background:#b4d8f2  ">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-product expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="product-title  result-title" style="width:690px;float:left;margin-left:-665px;line-height:27px;">Product Categories</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="productCategories">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style="float:left;margin-top:8px; margin-left:25px;">'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop].value+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right; margin-right:5px;">'+
						'<div idval="'+result[loop].id+'" prodCategory = "'+result[loop].value+'" description = "'+result[loop].description+'"class="edit-prodCategory  edit-icon" title="Edit Product Categories" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop].id+'" class="delete-prodCategory  delete-icon delete-organization" title="Delete Product Categories"style="margin-top:-21px;"></div>'+
						'</div>';
						$('#product-search-results-list').html('');
						$('#product-search-results-list').append(rowstr);
						
					};
						
						
					SystemDefaultsHandelr.productsOnload();
				SystemDefaultsHandelr.initSearchProductButtons();
				}else {
					$('#product-search-results-list').html('');
					$('#product-search-results-list').append('<div class="product-col green-result-col-1"><div class="product-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="product-bar green-title-bar" style="width:690px; background:#b4d8f2 "><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-product expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="product-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px;line-height:27px;">Product Categories</div></div></div>');
					SystemDefaultsHandelr.productsOnload();
				}
			
			});
		},
		load : function(){
			$('#defaults-exp-coll.exp-coll-pay').removeClass('expand-icon');
			$('#defaults-exp-coll.exp-coll-pay').addClass('collapse-icon');
			$('.pay-bar').css('width','700px');
			$('.pay-row').css('overflow-y','hidden');
			$('.pay-row').css('overflow-x','hidden');
			$('.pay-title').css('margin-left','-660px');
			if($('#pay').length==0){
				$('#pay-search-results-list').css("height","30px");
			}else{
				$('#pay').toggle();
			}
		
		},
		addload :function(){
			$('#defaults-exp-coll.exp-coll-add').removeClass('expand-icon');
			$('#defaults-exp-coll.exp-coll-add').addClass('collapse-icon');
			$('.add-bar').css('width','700px');
			$('.add-row').css('overflow-y','hidden');
			$('.add-row').css('overflow-x','hidden');
			$('.add-title').css('margin-left','-660px');
			if($('#add').length==0){
				$('#add-search-results-list').css("height","30px");
			}else{
				$('#add').toggle();
			}
		},
		empload : function(){
			$('#defaults-exp-coll.exp-coll-emp').removeClass('expand-icon');
					$('#defaults-exp-coll.exp-coll-emp').addClass('collapse-icon');
					$('.emp-bar').css('width','700px');
					$('.emp-row').css('overflow-y','hidden');
					$('.emp-row').css('overflow-x','hidden');
					$('.emp-title').css('margin-left','-660px');
					if($('#emp').length==0){
						$('#emp-search-results-list').css("height","30px");
					}else{
						$('#emp').toggle();
					}
		},
		journalsload :function(){
			$('#defaults-exp-coll.exp-coll-journals').removeClass('expand-icon');
			$('#defaults-exp-coll.exp-coll-journals').addClass('collapse-icon');
			$('.journals-bar').css('width','700px');
			$('.journals-row').css('overflow-y','hidden');
			$('.journals-row').css('overflow-x','hidden');
			$('.journals-title').css('margin-left','-660px');
			if($('#journals').length==0){
				$('#journal-search-results-list').css("height","30px");
			}else{
				$('#journals').toggle();
			}
		},
		invoiceNoload : function(){
			$('#defaults-exp-coll.exp-coll-invoice').removeClass('expand-icon');
			$('#defaults-exp-coll.exp-coll-invoice').addClass('collapse-icon');
			$('.invoice-bar').css('width','700px');
			$('.invoice-row').css('overflow-y','hidden');
			$('.invoice-row').css('overflow-x','hidden');
			$('.invoice-title').css('margin-left','-660px');
			if($('#invoice').length==0){
				$('#invoice-search-results-list').css("height","30px");
			}else{
				$('#invoice').toggle();
			}
		},
		productsOnload : function(){
			$('#defaults-exp-coll.exp-coll-product').removeClass('expand-icon');
			$('#defaults-exp-coll.exp-coll-product').addClass('collapse-icon');
			$('.product-bar').css('width','700px');
			$('.product-row').css('overflow-y','hidden');
			$('.product-row').css('overflow-x','hidden');
			$('.product-title').css('margin-left','-660px');
			if($('#product').length==0){
				$('#product-search-results-list').css("height","30px");
			}else{
				$('#product').toggle();
			}
		
		},
		initSearchResultButtons : function(){
		$('.pay-edit').click(function(){
			var id = $(this).attr('idval');
			var value=$(this).attr('idval');
			var type='Payment Type';
			$.post('default.json','action=get-pay-id&value='+value,function(obj){
				var res = obj.result.data;
				if(res.length>0){
					$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+res+'&type='+type,
					        function(data){
								$('.'+$('#cont').attr('class')).html(data);
								prevPayVal = $('#paymentValue').val();
					        });
				}
			});
		});
		$('.pay-delete').click(function(){
			var description = $(this).attr('idval');
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
										$.post('default.json','action=delete-pay-types&description='+description,function(obj){
											$(this).successMessage({
												container : '.'+$('#cont').attr('class'),
												data : obj.result.message
											});
										});
									},
									Cancel : function() {
										$(this).dialog('close');
									}
								}
							});
			return false;
		});
	    },
	    initSearchaddressButtons : function(){
			$('.edit-add').click(function(){
				var value=$(this).attr('idval');
				$.post('default.json','action=get-add-id&value='+value,function(obj){
					var res = obj.result.data;
					if(res.length>0){
						var type='Address Type';
						$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+res+'&type='+type,
						        function(data){
									$('.'+$('#cont').attr('class')).html(data);
									prevAddVal = $('#addressValue').val();
						        });
					}
				});
				
			});
			$('.delete-add').click(function(){
				var description=$(this).attr('idval');
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
											$.post('default.json','action=delete-add-types&description='+description,function(obj){
												$(this).successMessage({
													container : '.'+$('#cont').attr('class'),
													data : obj.result.message
												});
											});
										},
										Cancel : function() {
											$(this).dialog('close');
										}
									}
								});
				return false;
			});
		},
		initSearchInvoiceButtons : function(){
			$('.edit-invoice').click(function(){
				mode = 'edit';
				var id = $(this).attr('idval');
				var period = $(this).attr('period');
				var value = $(this).attr('startVal');
				var description = $(this).attr('description');
						var type='Invoice No';
						$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+id+'&type='+type+'&value='+value+'&period='+period+'&description='+description,
						        function(data){
									$('.'+$('#cont').attr('class')).html(data);
						        });
			});	
			$('.delete-invoice').click(function(){
				var id  = $(this).attr('idval');
				$('#error-message').html('Are you sure you want to Delete?');
				$("#error-message").dialog({
									resizable : false,
									height : 140,
									title : "<span class='ui-dlg-confirm'>Confirm</span>",
									modal : true,
									buttons : {
										'Ok' : function() {
											$(this).dialog('close');
											$.post('default.json','action=delete-invoice-numbers&id='+id,function(obj){
												$(this).successMessage({
													container : '.'+$('#cont').attr('class'),
													data : obj.result.message
												});
											});
										},
										Cancel : function() {
											$(this).dialog('close');
										}
									}
								});
			});
		},
		initemployeeButtons : function(){
			$('.edit-emp').click(function(){
				var description=$(this).attr('idval');
				$.post('default.json','action=get-emp-id&description='+description,function(obj){
					var res = obj.result.data;
					if(res.length >0){
					var type='EmployeeType';
					$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+res+'&type='+type,
					        function(data){
								$('.'+$('#cont').attr('class')).html(data);
					        });
					}
				});
				
			});
			$('.delete-emp').click(function(){
				var description=$(this).attr('idval');
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
											$.post('default.json','action=delete-emp-types&description='+description,function(obj){
												$(this).successMessage({
													container : '.'+$('#cont').attr('class'),
													data : obj.result.message
												});
											});
										},
										Cancel : function() {
											$(this).dialog('close');
										}
									}
								});
				return false;
			});
		},
		initSearchjournalsButtons :function(){
			$('.edit-journals').click(function(){
				var value=$(this).attr('idval');
				$.post('default.json','action=get-journal-id&value='+value,function(obj){
					var res = obj.result.data;
					if(res.length >0){
					var type='Journal Type';
					$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+res+'&type='+type,
					        function(data){
								$('.'+$('#cont').attr('class')).html(data);
								prevPattern = $('#invoiceNo').val();
					        });
					}
				});
			});
			$('.delete-journals').click(function(){
				var value=$(this).attr('idval');
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
											$.post('default.json','action=delete-journal-types&value='+value,function(obj){
												$(this).successMessage({
													container : '.'+$('#cont').attr('class'),
													data : obj.result.message
												});
											});
										},
										Cancel : function() {
											$(this).dialog('close');
										}
									}
								});
				return false;
			});
		},
		initSearchProductButtons : function(){
			$('.edit-prodCategory').click(function(){
				mode = 'edit';
				var id = $(this).attr('idval');
				prevProductCategory = $(this).attr('prodCategory');
				var prodCategory = $(this).attr('prodCategory');
				var description = $(this).attr('description');
				var type = 'Product Category';
						var type='Product Category';
						$.post('dashboard/system-lookups/edit-default-types.jsp', 'id='+id+'&type='+type+'&value='+prodCategory+'&description='+description,
						        function(data){
									$('.'+$('#cont').attr('class')).html(data);
						        });
			});
			$('.delete-prodCategory').click(function(){
				var value=$(this).attr('idval');
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
											$.post('default.json','action=delete-product-categories&id='+value,function(obj){
												$(this).successMessage({
													container : '.'+$('#cont').attr('class'),
													data : obj.result.message
												});
											});
										},
										Cancel : function() {
											$(this).dialog('close');
										}
									}
								});
				return false;
			});
		
		}
		
}