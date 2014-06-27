var DashbookHandler = {
		flag :true,
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
			$('#action-clear').click(function(){
				$('#employee-search-form').clearForm();
			});
			$('#action-cancel').click(function() {
				$('#error-message').html('You will loose unsaved data.. Clear form?');
				$("#error-message").dialog(
								{
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
				value = $('#value').val();
				description = $('#description').val();
				type = $('#types').val();
				DashbookHandler.validateDefaults();
				var resultSuccess=true;
				var resultFailure=false;
				if(DashbookHandler.flag == false){
					return resultSuccess;
				}
				if(DashbookHandler.validateDefaults()==false){
					return resultSuccess;
				}
				var paramString='action=add-employee-type&value='+value+'&description='+description
				if(type =='Employee Type'){
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
				}else if(type =='Payment Type'){
					var paramString='action=add-payment-type&value='+value+'&description='+description
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}else if(type == 'Address Type'){
					var paramString='action=add-address-type&value='+value+'&description='+description
					$.post('default.json',paramString,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
						
					});
				}
				else if(type == 'Journal Type'){
					var invoiceNo=$('#invoiceNo').val();
					var paramString='action=add-journals&value='+value+'&description='+description+'&invoiceNo='+invoiceNo
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
					if(DashbookHandler.flag == false||DashbookHandler.validateDefaults()==false){
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
				    $.post('default.json','action=update-add-types&id='+id+'&value='+value+'&description='+desc,function(obj){
						$(this).successMessage({
							container : '.'+$('#cont').attr('class'),
							data : obj.result.message
						});
					});
			     }else if(type == "Payment Type"){
			    	 $.post('default.json','action=update-pay-types&id='+id+'&value='+value+'&description='+desc,function(obj){
							$(this).successMessage({
								container : '.'+$('#cont').attr('class'),
								data : obj.result.message
							});
						}); 
			     }else if(type == "Journal Type"){
			    	 var invoiceno =$('#invoiceNo').val();
			    	 $.post('default.json','action=update-journal-types&id='+id+'&value='+value+'&description='+desc+'&invoiceNo='+invoiceno,function(obj){
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
			 $('#value').blur(function(){
					type = $('#types').val();
					value = $('#value').val();
					vlen = $('#value').val().length;
					if($('#value').val().charAt(0)==" "||$('#value').val().charAt(vlen-1)==" "){
						 $('#journalvalue_pop').hide();
						 $('#empvalue_pop').hide();
						 $('#payvalue_pop').hide();
						 $('#value_pop').hide();
						 $('#valuelen_pop').hide();
				 $('#valuesp_pop').show();
				 DashbookHandler.flag =false;
			 }else{
				 $('#valuesp_pop').hide();
				 DashbookHandler.flag =true;
			 
					if(type == 'Address Type'){
						$.post('default.json','action=validate-address-type&value='+value,function(obj){
							var result = obj.result.data;
							if(result.length>0){
								$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#value").focus(function(event){
									if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
										$('#journalvalue_pop').hide();
										 $('#empvalue_pop').hide();
										 $('#payvalue_pop').hide();
										 $('#addvalue_pop').hide();
										$('#value_pop').hide();
										$('#valuesp_pop').hide();
										$('#valueValid').empty();
										 $('#addvalue_pop').show();
									}
									 
								});
								$("#value").blur(function(event){
									 $('#addvalue_pop').hide();
									 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(vlen-1)==" "){
										 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#value").focus(function(event){
											 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
													$('#journalvalue_pop').hide();
													 $('#empvalue_pop').hide();
													 $('#payvalue_pop').hide();
													 $('#addvalue_pop').hide();
													$('#value_pop').hide();
													$('#valuesp_pop').hide();
													$('#valueValid').empty();
													 $('#addvalue_pop').show();
												}
											});
									 }else{
										 $('#addvalue_pop').hide();
									 }
								});
								DashbookHandler.flag =false;
							}else{
								DashbookHandler.flag =true;
							}
							
						});
					}else if(type =='Payment Type'){
						$.post('default.json','action=validate-payment-type&value='+value,function(obj){
							var result = obj.result.data;
							if(result.length>0){
								$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#value").focus(function(event){
									 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
										 $('#journalvalue_pop').hide();
										 $('#empvalue_pop').hide();
										 $('#addvalue_pop').hide();
										$('#value_pop').hide();
										$('#valuesp_pop').hide();
										$('#valueValid').empty();
										 $('#payvalue_pop').show();
									 }
									
								});
								$("#value").blur(function(event){
									 $('#payvalue_pop').hide();
									 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(-1)==" "){
										 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#value").focus(function(event){
											 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
												 $('#journalvalue_pop').hide();
												 $('#empvalue_pop').hide();
												 $('#addvalue_pop').hide();
												$('#value_pop').hide();
												$('#valuesp_pop').hide();
												$('#valueValid').empty();
												 $('#payvalue_pop').show();
											 }
											
											});
									 }else{
										 $('#payvalue_pop').hide();
									 }
								});
								DashbookHandler.flag =false;
							}else{
								DashbookHandler.flag =true;
							}
							
						});
					}else if(type =='Employee Type'){
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
									 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(-1)==" "){
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
								DashbookHandler.flag =false;
							}else{
								DashbookHandler.flag =true;
							}
							
						});
					}else if(type =='Journal Type'){
						$.post('default.json','action=validate-journal-type&value='+value,function(obj){
							var result = obj.result.data;
							if(result.length>0){
								$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#value").focus(function(event){
									if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
										 $('#empvalue_pop').hide();
										 $('#payvalue_pop').hide();
										 $('#addvalue_pop').hide();
										 $('#valuesp_pop').hide();
										$('#value_pop').hide();
										$('#valueValid').empty();
										 $('#journalvalue_pop').show();
									}
									
								});
								$("#value").blur(function(event){
									 $('#journalvalue_pop').hide();
									 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0||$('#value').val().charAt(0)==" "||$('#value').val().charAt(-1)==" "){
										 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#value").focus(function(event){
											 if($('#value').val().charAt(0)!=" "||$('#value').val().charAt(vlen-1)!=" "){
												 $('#empvalue_pop').hide();
												 $('#payvalue_pop').hide();
												 $('#addvalue_pop').hide();
												 $('#valuesp_pop').hide();
												$('#value_pop').hide();
												$('#valueValid').empty();
												 $('#journalvalue_pop').show();
											}
											});
									 }else{
										 $('#journalvalue_pop').hide();
									 }
								});
								DashbookHandler.flag =false;
							}else{
								DashbookHandler.flag =true;
							}
							
						});
					}
			 }
				});
			 $('#types').change(function(){
					var type=$('#types').val();
					if(type == "Journal Type"){
						$('#invoiceNum').show();
						$('#invoiceNo').attr('class','mandatory');
					}else{
						$('#invoiceNum').hide();
						$('#invoiceNo').removeAttr('class');
					}
				});
			 $('#default-types').unbind('click').bind('click', function (e){
					$('.dashboard-page-container').load('dashboard/system-lookups/employee_types.jsp');
				});
		},
		validateDefaults : function(){
			var result=true;
			var vlen=$('#value').val().length;
			if($('#value').val().length == 0 ||/^[a-zA-Z\s]+$/.test($('#value').val()) == false){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#value").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
					$('#valuelen_pop').hide();
					 $('#valuesp_pop').hide();
					$('#valueValid').empty();
					 $('#value_pop').show();
				});
				$("#value").blur(function(event){
					 $('#value_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#value').val())==false || ($('#value').val()).length == 0){
						 $('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#value").focus(function(event){
							 $('#journalvalue_pop').hide();
							 $('#empvalue_pop').hide();
							 $('#payvalue_pop').hide();
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
			}if($('#value').val().length>30){
				$('#valueValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#value").focus(function(event){
					 $('#journalvalue_pop').hide();
					 $('#empvalue_pop').hide();
					 $('#payvalue_pop').hide();
					 $('#addvalue_pop').hide();
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
			if( $('#types').val()=='-1'||$('#types').val()==null){
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
			if($('#types').val()=="Journal Type"){
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
			}
			return result;
		},
		getAllExistedTypes: function(){
			$.post('default.json','action=get-all-payment-types',function(obj){
				var result = obj.result.data;
				if(result.length >0 ){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="pay-col green-result-col-1">'+
					'<div class="pay-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;overflow-y:hidden;overflow-x:hidden">'+
					'<div class="pay-bar green-title-bar" style="width:695px;">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-pay expand-icon" id="defaults-exp-coll"style="float:left;margin-left:663px;">'+
					'<div class="pay-title  result-title" style="height:40px;width:690px;float:left;margin-left:-655px">'+Msg.DELIVERY_NOTE_PAYMENT_TYPES+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="pay">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style=float:left;margin-top:10px;>'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right;">'+
						'<div idval="'+result[loop]+'" class="pay-edit  edit-icon" title="Edit Payment Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop]+'" class="pay-delete  delete-icon delete-organization" title="Delete Payment Types"style="margin-top:-20px;"></div>'+
						'</div>';
						$('#pay-search-results-list').html('');
						$('#pay-search-results-list').append(rowstr);
					};
					DashbookHandler.load();
				DashbookHandler.initSearchResultButtons();
		     } else{
		    	 $('#pay-search-results-list').html('');
				    $('#pay-search-results-list').append('<div class="pay-col green-result-col-1"><div class="pay-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="pay-bar green-title-bar" style="width:690px;"><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-pay expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;"><div class="pay-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px">Payment Types</div></div></div>');
					DashbookHandler.load();
			     }
		          
			});
			
			$.post('default.json','action=get-all-employee-types',function(obj){
				var employeeTypes = obj.result.data;
				if(employeeTypes.length >0 ){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="emp-col green-result-col-1">'+
					'<div class="emp-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;overflow-y:auto;overflow-x:hidden">'+
					'<div class="emp-bar green-title-bar" style="width:675px;">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-emp expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="emp-title result-title" style="width:690px;float:left;margin-left:-665px">'+Msg.EMPLOYEE_EMPLOYEE_TYPES+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="emp">';
					for(var loop=0;loop<employeeTypes.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style=float:left;margin-top:10px;>'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+employeeTypes[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right;">'+
						'<div idval="'+employeeTypes[loop]+'" class="edit-emp  edit-icon" title="Edit Payment Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+employeeTypes[loop]+'" class="delete-emp  delete-icon delete-organization" title="Delete Payment Types"style="margin-top:-20px;"></div>'+
						'</div>';
						$('#emp-search-results-list').html('');
						$('#emp-search-results-list').append(rowstr);
						
					};
					DashbookHandler.empload();
				DashbookHandler.initemployeeButtons();
				}
				else{
					$('#emp-search-results-list').html('');
					$('#emp-search-results-list').append('<div class="emp-col green-result-col-1"><div class="emp-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="emp-bar green-title-bar" style="width:690px;"><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-emp expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;"><div class="emp-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px">Employee Types</div></div></div>');
					DashbookHandler.empload();
			     }
				
			});
			$.post('default.json','action=get-all-address-types',function(obj){
				var result = obj.result.data;
				if(result.length >0 ){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="add-col green-result-col-1">'+
					'<div class="add-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="add-bar green-title-bar" style="width:675px;">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-add expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="add-title  result-title" style="width:690px;float:left;margin-left:-665px">'+Msg.ADDRESS_TYPE_LABEL+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="add">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style=float:left;margin-top:10px;>'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right;">'+
						'<div idval="'+result[loop]+'" class="edit-add  edit-icon" title="Edit Address Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop]+'" class="delete-add  delete-icon delete-organization" title="Delete Address Types"style="margin-top:-20px;"></div>'+
						'</div>';
						$('#add-search-results-list').html('');
						$('#add-search-results-list').append(rowstr);
						
					};
					DashbookHandler.addload();
				DashbookHandler.initSearchaddressButtons();
				}else{
					$('#add-search-results-list').html('');
					$('#add-search-results-list').append('<div class="add-col green-result-col-1"><div class="add-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="add-bar green-title-bar" style="width:690px;"><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-add expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="add-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px">Address Types</div></div></div>');
					DashbookHandler.addload();
			     }
			});
			$.post('default.json','action=get-all-journal-types',function(obj){
				var result = obj.result.data;
				if(result.length >0 ){
					var rowstr ='<div class=table-field" style="height:auto;">'+
					'<div class="journals-col green-result-col-1">'+
					'<div class="journals-row green-result-row alternate" style="color: #FFFFFF;height:auto;width:690px;">'+
					'<div class="journals-bar green-title-bar" style="width:675px;">'+
					'<div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;">'+
					'<div class="exp-coll-journals expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;">'+
					'<div class="journals-title  result-title" style="width:690px;float:left;margin-left:-665px">'+Msg.JOURNAL_TYPES+'</div>'+
					'</div>'+
					'</div>'+
					'<div>'+
					'</div>'+
					'</div>'+
					'<div class="test" id="journals">';
					for(var loop=0;loop<result.length;loop = loop+1){
						rowstr = rowstr	+
						'<div style=" background: none repeat scroll 0 0  #D2E6F5;  border-bottom: 1px solid #9BCCF2; float: left; height:30px;width:690px;">'+
						'<div style=float:left;margin-top:10px;>'+
						'<span style="color:#1C8CF5;font: 13px arial;top:20px;margin-top:-40px;">'+result[loop]+'</span>'+
						'</div>'+
						'<div>' + 
						'</div>'+
						'</div>'+
						'<div style="float:right;">'+
						'<div idval="'+result[loop]+'" class="edit-journals  edit-icon" title="Edit journal Types" style="float:left;margin-left:630px;margin-top:-20px;"></div>' +
						'<div idval="'+result[loop]+'" class="delete-journals  delete-icon delete-organization" title="Delete journal Types"style="margin-top:-20px;"></div>'+
						'</div>';
						$('#journal-search-results-list').html('');
						$('#journal-search-results-list').append(rowstr);
						
					};
					DashbookHandler.journalsload();
				DashbookHandler.initSearchjournalsButtons();
				}else{
					$('#journal-search-results-list').html('');
					$('#journal-search-results-list').append('<div class="journals-col green-result-col-1"><div class="journals-row green-result-row alternate" style="color: #FFFFFF;height:80px;width:690px;"><div class="journals-bar green-title-bar" style="width:690px;"><div style="background: url("images/title-bg.png") no-repeat scroll 0 0 transparent; height: 30px;padding-left: 20px;"><div class="exp-coll-journals expand-icon" id="defaults-exp-coll" style="float:left;margin-left:663px;""><div class="journals-title  result-title" style="height:40px;width:690px;float:left;margin-left:-665px">Journal Types</div></div></div>');
					DashbookHandler.journalsload();
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
		addColor: function(number) {
			if(number%2 !=0){
				$('#row-'+number).css({'background-color' : 'LightGray'});
			}
			else{
				$('#row-'+number).css({'background-color' : 'FloralWhite'});
			}
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
		
		//Change Request Dashboard for Customer,Sales Return,SE Transaction CR(Delivery Note,Sales Return,Day Book,Journal)
		initSearchCustomerChangeRequestOnLoad: function(){
			var paramString='action=search-cr-onload';
			$.post('customerCr.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#customer-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="change-request-row" align="'+data[loop].id+'" style="height: 80px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="change-request-row" class="green-result-row search-row-results" align="'+data[loop].id+'"  style="height: 80px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1 search-row-results" style="width: 210px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].createdBy + '</div>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_BUSINESS_NAME +':'+' </span><span class="property-value" id="customerBusinessName">' + data[loop].businessName + '</span><br/>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_LOCALITY+':'+' </span><span class="property-value">' + data[loop].locality + '</span><br/>';
								rowstr +='<span class="property">'+Msg.CUSTOMER_CR_TYPE +':'+' </span><span class="property-value">' + data[loop].crType + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#customer-change-request-results-list').append(rowstr);
					};
					$('#customer-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#customer-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.customer-change-request').css( "width", "250px" );
			    	$('.sales-return-approval').css( "width", "250px" );
			    	$('.journal-approval').css( "width", "245px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.customer-change-request').css( "width", "210px" );
			    	$('.sales-return-approval').css( "width", "205px" );
			    	$('.journal-approval').css( "width", "200px" );
			    	$('.search-row-results').css( "width", "200px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				}
				setTimeout(function() {
					$('#customer-change-request-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},	
		initSalesReturnOnLoad: function(){
			var paramString='action=search-sales-return-dashboard';
			$.post('salesReturn.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-sales-returns-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="sales-return-row"  align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 60px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="sales-return-row" class="green-result-row search-row-results" align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 60px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr = rowstr +'<div class="green-result-col-1 search-row-results" style="width: 340px;">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].createdBy + '</div>' +
								'<span class="property">'+Msg.CUSTOMER_BUSINESS_NAME+':'+' </span><span class="property-value">' + data[loop].businessName + '</span><br/>' +
								'<span class="property">'+Msg.SALES_RETURNS_CREATED_DATE+':'+' </span><span class="property-value">' + data[loop].date + '</span><br/>' +
								'<span class="property">'+Msg.CUSTOMER_INVOICE_NAME +':'+' </span><span class="property-value">' + data[loop].invoiceName + '</span>' +
								'</div>' +
								'</div>'+
								'</a>';
						$('#search-sales-returns-results-list').append(rowstr);
					};
					$('#search-sales-returns-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-sales-returns-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Sales Return Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
		initJournalsOnLoad: function(){
			var paramString='action=get-all-journals-for-dashboard';
			$.post('journal.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-journal-add-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashbookHandler.formatDate(data[loop].createdOn);
								var amount=data[loop].amount;
								if(amount.indexOf(",1") !== -1){
								var formatAmount=amount.replace(",1","");
								var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}else{
									var formatAmount=amount.replace(",0","");
									var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="journal-add-row" align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'" style="height: 70px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="journal-add-row" class="green-result-row search-row-results" align="'+data[loop].id+'" comp="'+data[loop].invoiceNo+'"  style="height: 70px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1 search-row-results" style="width: 210px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].businessName + '</div>';
								rowstr +='<span class="property">'+Msg.JOURNAL_TYPE+':'+' </span><span class="property-value">' + data[loop].journalType + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_CREATED_DATE +':'+' </span><span class="property-value">' + dateFormat + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_AMOUNT +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#search-journal-add-results-list').append(rowstr);
					};
					$('#search-journal-add-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-journal-add-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Journal Available.</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
		initDeliveryNoteCROnLoad: function(){
			var paramString='action=search-delivery-note-change-request-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-delivery-note-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="delivery-note-change-request-row"  align="'+data[loop].id+'" style="height: 70px;">';
								} else {
									rowstr = '<a href="#"><div id="delivery-note-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 70px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].businessName + '</div>' +
								'<span class="property">'+Msg.DELIVERY_NOTE_INVOICE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].invoiceName + '</span>' +
								'</div>' +
								'</div>' +
								'<div class="green-result-col-2">'+
								'<div class="result-body">' +'<span class="property">'+Msg.DELIVERY_NOTE_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + data[loop].date + '</span>' +
								'</div>' +'<span class="property">'+Msg.DELIVERY_NOTE_BALANCE_LABEL +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].balance) + '</span>' +
								'</div>' +
								'</a>';
						$('#search-delivery-note-results-list').append(rowstr);
					};
					$('#search-delivery-note-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-delivery-note-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Delivery Note CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.delivery-note-change-request').css( "width", "250px" );
			    	$('.sales-return-change-request').css( "width", "250px" );
			    	$('.day-book-change-request').css( "width", "245px" );
			    	$('.journal-change-request').css( "width", "245px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.delivery-note-change-request').css( "width", "210px" );
			    	$('.sales-return-change-request').css( "width", "205px" );
			    	$('.day-book-change-request').css( "width", "200px" );
			    	$('.journal-change-request').css( "width", "210px" );
			    	$('.search-row-results').css( "width", "200px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				}
				setTimeout(function() {
					$('#customer-change-request-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},	
		initSalesReturnCROnLoad: function(){
			var paramString='action=search-sales-return-change-request-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-sales-return-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="sales-return-change-request-row"  align="'+data[loop].id+'" style="height: 70px;">';
								} else {
									rowstr = '<a href="#"><div id="sales-return-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 70px; width: 340px;">';
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
								'<span class="property">'+Msg.SALES_RETURNS_TOTAL_COST +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(data[loop].total) + '</span>' +
								'</div>' +'<span class="property">'+Msg.SALES_EXECUTIVE_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].createdBy + '</span>' +
								'</div>' +
								'</a>';
						$('#search-sales-return-change-request-results-list').append(rowstr);
					};
					$('#search-sales-return-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-sales-return-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Sales Return CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
		//Function to format date to DD/MM/YYYY
		formatDate:function(inputFormat){
			var str=inputFormat.split(/[" "]/);
			dt=new Date(str[0]);
			return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
		},
		initDayBookCROnLoad: function(){
			var paramString='action=search-day-book-change-request-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-day-book-change-request-results-list').html('');
						if(data != undefined ) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashbookHandler.formatDate(data[loop].createdOn);
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="day-book-change-request-row"  align="'+data[loop].id+'" style="height: 50px;">';
								} else {
									rowstr = '<a href="#"><div id="day-book-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 50px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1">'+
								'<div class="result-body">' +
					   			'<div class="result-title">'+ data[loop].salesExecutive + '</div>' +
					   			'<div class="result-body">' +'<span class="property">'+Msg.DAY_BOOK_CREATED_DATE_LABEL +':'+' </span><span class="property-value">' + dateFormat + '</span>' +
					   			'</div>' +
					   			'</div>' +
					   			'</div>' +
								'</a>';
						$('#search-day-book-change-request-results-list').append(rowstr);
					};
					$('#search-day-book-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-day-book-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Day Book CR Available</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
		initJournalsCROnLoad: function(){
			var paramString='action=get-all-journals-CR-for-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-journal-change-request-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								var dateFormat=DashbookHandler.formatDate(data[loop].createdOn);
								var amount=data[loop].amount;
								if(amount.indexOf(",1") !== -1){
								var formatAmount=amount.replace(",1","");
								var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}else{
									var formatAmount=amount.replace(",0","");
									var parsedAmount=parseFloat(Math.round(formatAmount * 100) / 100).toFixed(2);
								}
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate" id="journal-change-request-row" align="'+data[loop].id+'" style="height: 70px;">';
								} else {
									rowstr = '<a href="#"><div id="journal-change-request-row" class="green-result-row" align="'+data[loop].id+'"  style="height: 70px; width: 340px;">';
								}
								alternate = !alternate;
								rowstr +='<div class="green-result-col-1" style="width: 340px;">';
								rowstr +='<div class="result-body">';
								rowstr +='<div id="results" class="result-title">' + data[loop].businessName + '</div>';
								rowstr +='<span class="property">'+Msg.JOURNAL_TYPE+':'+' </span><span class="property-value">' + data[loop].journalType + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_CREATED_DATE +':'+' </span><span class="property-value">' + dateFormat + '</span><br/>';
								rowstr +='<span class="property">'+Msg.JOURNAL_AMOUNT +':'+' </span><span class="property-value">' + currencyHandler.convertFloatToStringPattern(parsedAmount) + '</span>';
								rowstr +='</div>'; 
								rowstr +='</div>';
								rowstr +='</a>';
						$('#search-journal-change-request-results-list').append(rowstr);
					};
					$('#search-journal-change-request-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-journal-change-request-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Journal CR Available.</div></div></div>');
							  }
							$.loadAnimation.end();
					});
		},	
		// For Alerts.
		initAlertOnLoad: function(){
			var paramString='action=search-alert-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-alerts-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="alert-row"  align="'+data[loop].id+'" style="height: 80px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="alert-row" class="green-result-row search-row-results" align="'+data[loop].id+'"  style="height: 80px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1 search-row-results" style="width: 210px;">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].alertType + '</div>' +
								'<span class="property">'+Msg.ALERT_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].alertName + '</span>' +
								'</div>' +
								'</div>' +
								'<div class="green-result-col-2">'+
								'<div class="result-body">' +
								'</div>' +'<span class="property">'+Msg.ALERT_DESCRIPTION_LABEL +':'+' </span><span class="property-value">' + data[loop].description + '</span>' +
								'</div>' +
								'</a>';
						$('#search-alerts-results-list').append(rowstr);
					};
					$('#search-alerts-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-alerts-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Configured</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.system-alerts').css( "width", "250px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.system-alerts').css( "width", "210px" );
					$('.search-row-results').css( "width", "210px" );
			    	$('.jScrollPaneContainer').css("width","210px");
				}
				setTimeout(function() {
					$('#customer-change-request-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},	
		
		validateCustomerChangeRequestCredits: function(){
			var result=true;
			if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10){
				$('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#creditLimit").focus(function(event){
					$('#creditLimitValid').empty();
					 $('#creditLimit_pop').show();
				});
				$("#creditLimit").blur(function(event){
					 $('#creditLimit_pop').empty();
					 if(/^\$?\d+((,\d{3})+)?(\.\d+)?$/.test($('#creditLimit').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditLimit').val())).toString().length > 10){
						 $('#creditLimitValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#creditLimit_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			if(/^[0-9]+$/.test($('#creditOverdueDays').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditOverdueDays').val())).toString().length > 3){
				$('#creditOverdueDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#creditOverdueDays").focus(function(event){
					$('#creditOverdueDaysValid').empty();
					 $('#creditOverdueDays_pop').show();
				});
				$("#creditOverdueDays").blur(function(event){
					 $('#creditOverdueDays_pop').empty();
					 if(/^[0-9]+$/.test($('#creditOverdueDays').val()) == false || currencyHandler.convertStringPatternToFloat(($('#creditOverdueDays').val())).toString().length > 3){
						 $('#creditOverdueDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#creditOverdueDays_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			return result;
		},
		
};
$('#journal-add-row').live('click',function(){
	var id = $(this).attr('align');
	var invoiceNumber=$(this).attr('comp');
	$.post('changeTransaction.json', 'action=check-Transaction-journal'+'&invoiceNumber='+invoiceNumber,
	        function(data){
		var existsId=data.result.data;
		//if Pending transaction journal is present in Transaction CR for Aproval journal based on invoiceNo.
	    if(existsId != 0){
	    	 $('#check-transaction-journal-view-container').html('There is an Transaction CR related to the Invoice.');
			  $("#check-transaction-journal-view-dialog").dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						//if click open Transaction journal CR dialog should open.
						Open: function() {
							$.post('my-sales/transactions/change-transactions/journal_dashboard_cr_view.jsp', 'id='+existsId,
							        function(data){
							        	$('#journal-dashboard-cr-view-container').html(data);  
							        	$("#journal-dashboard-cr-view-dialog").dialog({
							    			autoOpen: true,
							    			height: 400,
							    			width: 1040,
							    			modal: true,
							    			buttons: {
							    				//for txn journal CR clicked respective Approval journal dialog should opened.
							    			Approved: function() {
							    				var statusParameter='Approved';
							    				$.post('changeTransaction.json', 'journalId='+existsId+'&action=approve-journal-cr'+'&status='+statusParameter,
							    						 function(obj) {
							    					    DashbookHandler.initJournalsCROnLoad();
							    					    $.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp', 'id='+id,
							    				    	        function(data){
							    				    	        	$('#journal-dashboard-view-container').html(data);  
							    				    	        	$("#journal-dashboard-view-dialog").dialog({
							    				    	    			autoOpen: true,
							    				    	    			height: 400,
							    				    	    			width: 1040,
							    				    	    			modal: true,
							    				    	    			buttons: {
							    				    	    			Approved: function() {
							    				    	    				var statusParameter='Approved';
							    				    	    				$.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
							    				    	    						 function(obj) {
							    				    	    					    DashbookHandler.initJournalsOnLoad();
							    				    	    						$("#journal-dashboard-view-dialog").dialog('close');
							    				    	    					});
							    				    	    				},
							    				    	    		      Decline: function() {
							    				    	    		    	  var statusParameter='Decline';
							    				    	    		    	  $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
							    				    		    						 function(obj) {
							    				    		    					    DashbookHandler.initJournalsOnLoad();
							    				    		    						$("#journal-dashboard-view-dialog").dialog('close');
							    				    		    					});
							    				    	    		          }
							    				    	    			},
							    				    	    			close: function() {
							    				    	    				$('#journal-dashboard-view-container').html('');
							    				    	    			}
							    				    	    		});
							    				    	        });
							    						$("#journal-dashboard-cr-view-dialog").dialog('close');
							    					});
							    				},
							    				//for txn journal CR  decline clicked respective Approval journal dialog should opened.
							    		      Decline: function() {
							    		    	  var statusParameter='Decline';
							    		    	  $.post('changeTransaction.json', 'journalId='+existsId+'&action=approve-journal-cr'+'&status='+statusParameter,
								    						 function(obj) {
								    					    DashbookHandler.initJournalsCROnLoad();
								    					    $.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp', 'id='+id,
								    				    	        function(data){
								    				    	        	$('#journal-dashboard-view-container').html(data);  
								    				    	        	$("#journal-dashboard-view-dialog").dialog({
								    				    	    			autoOpen: true,
								    				    	    			height: 400,
								    				    	    			width: 1040,
								    				    	    			modal: true,
								    				    	    			buttons: {
								    				    	    			Approved: function() {
								    				    	    				var statusParameter='Approved';
								    				    	    				$.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
								    				    	    						 function(obj) {
								    				    	    					    DashbookHandler.initJournalsOnLoad();
								    				    	    						$("#journal-dashboard-view-dialog").dialog('close');
								    				    	    					});
								    				    	    				},
								    				    	    		      Decline: function() {
								    				    	    		    	  var statusParameter='Decline';
								    				    	    		    	  $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
								    				    		    						 function(obj) {
								    				    		    					    DashbookHandler.initJournalsOnLoad();
								    				    		    						$("#journal-dashboard-view-dialog").dialog('close');
								    				    		    					});
								    				    	    		          }
								    				    	    			},
								    				    	    			close: function() {
								    				    	    				$('#journal-dashboard-view-container').html('');
								    				    	    			}
								    				    	    		});
								    				    	        });
								    						$("#journal-dashboard-cr-view-dialog").dialog('close');
								    					});
							    		          }
							    			},
							    			close: function() {
							    				$('#journal-dashboard-cr-view-container').html('');
							    			}
							    		});
							        }
							    );
							$("#check-transaction-journal-view-dialog").dialog('close');
						},
						//if cancel redirect to Approval dashboard page.
			            Cancel: function() {
			            	$('.dashboard-page-container').load('dashboard/approvals/dashboard_main_approvals_view.jsp');
							$("#check-transaction-journal-view-dialog").dialog('close');
				      }
					},
					close: function() {
						$(this).dialog('close');
					}
	          });
			//if not, display Approval journal and Approved and declined
	    }else{
	    	$.post('my-sales/transactions/view-transactions/journal_dashboard_view.jsp', 'id='+id,
	    	        function(data){
	    	        	$('#journal-dashboard-view-container').html(data);  
	    	        	$("#journal-dashboard-view-dialog").dialog({
	    	    			autoOpen: true,
	    	    			height: 400,
	    	    			width: 1040,
	    	    			modal: true,
	    	    			buttons: {
	    	    			Approved: function() {
	    	    				var statusParameter='Approved';
	    	    				$.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
	    	    						 function(obj) {
	    	    					    DashbookHandler.initJournalsOnLoad();
	    	    						$("#journal-dashboard-view-dialog").dialog('close');
	    	    					});
	    	    				},
	    	    		      Decline: function() {
	    	    		    	  var statusParameter='Decline';
	    	    		    	  $.post('journal.json', 'journalId='+id+'&action=approve-journal'+'&status='+statusParameter,
	    		    						 function(obj) {
	    		    					    DashbookHandler.initJournalsOnLoad();
	    		    						$("#journal-dashboard-view-dialog").dialog('close');
	    		    					});
	    	    		          }
	    	    			},
	    	    			close: function() {
	    	    				$('#journal-dashboard-view-container').html('');
	    	    			}
	    	    		});
	    	        });
	       }
	  });
	 });
$('#journal-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/journal_dashboard_cr_view.jsp', 'id='+id,
	        function(data){
	        	$('#journal-dashboard-cr-view-container').html(data);  
	        	$("#journal-dashboard-cr-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 450,
	    			width: 1040,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var statusParameter='Approved';
	    				$.post('changeTransaction.json', 'journalId='+id+'&action=approve-journal-cr'+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initJournalsCROnLoad();
	    						$("#journal-dashboard-cr-view-dialog").dialog('close');
	    					});
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('changeTransaction.json', 'journalId='+id+'&action=approve-journal-cr'+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initJournalsCROnLoad();
		    						$("#journal-dashboard-cr-view-dialog").dialog('close');
		    					});
	    		          }
	    			},
	    			close: function() {
	    				$('#journal-dashboard-cr-view-container').html('');
	    			}
	    		});
	        	$('#change-request-journal-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		$.post('changeTransaction.json', 'action=get-journal-id'+'&invoiceNumber='+invoiceNumber,
	        				function(obj){
	        					var journalId = obj.result.data;
	        					if(journalId != 0){
	        						 $.post('my-sales/transactions/view-transactions/journals_view.jsp','id=' + journalId,
	        						           function(data) {
	        						            $('#journal-container').html(data);
	        						            var dialogOpts = {
	  	        						     		   height : 360,
	  	        						     		   width : 1020,
	        						            		buttons : {
	    	        						     		    Close : function() {
	    	        						     		     $(this).dialog('close');
	    	        						     		    }
	    	        						     		 },
	        						            	};
	        						            $("#journal-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        	});
	        });
	 });
$('#sales-return-row').live('click',function(){
	var id = $(this).attr('align');
	var invoiceNumber = $(this).attr('comp');
	$.post('changeTransaction.json', 'action=check-Transaction-Sales-Return'+'&invoiceNumber='+invoiceNumber,
	        function(data){
		var existsId=data.result.data;
		//if Pending transaction sales return is present in Transaction CR for Aproval sales return based on invoiceNo.
	    if(existsId != 0){
	    	  $('#check-transaction-sales-return-view-container').html('There is an Transaction CR related to the Invoice.');
			  $("#check-transaction-sales-return-view-dialog").dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						//if click open Transaction Sales Return CR dialog should open.
						Open: function() {
							$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_results_view.jsp', 'id='+existsId,
							        function(data){
							        	$('#sales-return-change-request-dashboard-view-container').html(data);  
							        	$("#sales-return-change-request-dashboard-view-dialog").dialog({
							    			autoOpen: true,
							    			height: 485,
							    			width: 1000,
							    			modal: true,
							    			buttons: {
							    				//for txn Sales return CR clicked respective Approval SR dialog should opened.
							    			Approved: function() {
							    				var thisButton = $(this);
							    				var statusParameter='Approved';
							    				$.post('changeTransaction.json', 'salesReturnCRId='+existsId+'&action=approve-sales-return-cr'+'&status='+statusParameter,
							    						 function(obj) {
							    					    DashbookHandler.initSalesReturnCROnLoad();
							    					$.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp', 'id='+id,
							    					        function(data){
							    					        	$('#sales-return-dashboard-view-container').html(data);  
							    					        	$("#sales-return-dashboard-view-dialog").dialog({
							    					    			autoOpen: true,
							    					    			height: 485,
							    					    			width: 1000,
							    					    			modal: true,
							    					    			buttons: {
							    					    			Approved: function() {
							    					    				var statusParameter='Approved';
							    					    				$.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
							    					    						 function(obj) {
							    					    					    DashbookHandler.initSalesReturnOnLoad();
							    					    						$("#sales-return-dashboard-view-dialog").dialog('close');
							    					    					});
							    					    				},
							    					    		      Decline: function() {
							    					    		    	  var statusParameter='Decline';
							    					    		    	  $.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
							    						    						 function(obj) {
							    						    					    DashbookHandler.initSalesReturnOnLoad();
							    						    						$("#sales-return-dashboard-view-dialog").dialog('close');
							    						    					});
							    					    		          }
							    					    			},
							    					    			close: function() {
							    					    				$('#sales-return-dashboard-view-container').html('');
							    					    			}
							    					    		});
							    					        });
							    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
							    					});
							    				},
							    				//if decline click Approval SR dialog should opened.
							    		      Decline: function() {
							    		    	  var statusParameter='Decline';
							    		    	  $.post('changeTransaction.json', 'salesReturnCRId='+existsId+'&action=approve-sales-return-cr'+'&status='+statusParameter,
								    						 function(obj) {
								    					    DashbookHandler.initSalesReturnCROnLoad();
								    					    $.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp', 'id='+id,
									    					        function(data){
									    					        	$('#sales-return-dashboard-view-container').html(data);  
									    					        	$("#sales-return-dashboard-view-dialog").dialog({
									    					    			autoOpen: true,
									    					    			height: 485,
									    					    			width: 1000,
									    					    			modal: true,
									    					    			buttons: {
									    					    			Approved: function() {
									    					    				var statusParameter='Approved';
									    					    				$.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
									    					    						 function(obj) {
									    					    					    DashbookHandler.initSalesReturnOnLoad();
									    					    						$("#sales-return-dashboard-view-dialog").dialog('close');
									    					    					});
									    					    				},
									    					    		      Decline: function() {
									    					    		    	  var statusParameter='Decline';
									    					    		    	  $.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
									    						    						 function(obj) {
									    						    					    DashbookHandler.initSalesReturnOnLoad();
									    						    						$("#sales-return-dashboard-view-dialog").dialog('close');
									    						    					});
									    					    		          }
									    					    			},
									    					    			close: function() {
									    					    				$('#sales-return-dashboard-view-container').html('');
									    					    			}
									    					    		});
									    					        });
								    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
								    					});
							    			      //$(this).dialog('close');
							    		          }
							    			},
							    			close: function() {
							    				$('#sales-return-change-request-dashboard-view-container').html('');
							    			}
							    		});
							        });
							$("#check-transaction-sales-return-view-dialog").dialog('close');
						},
						//if cancel redirect to Approval dashboard page.
			            Cancel: function() {
			            	$('.dashboard-page-container').load('dashboard/approvals/dashboard_main_approvals_view.jsp');
							$("#check-transaction-sales-return-view-dialog").dialog('close');
				      }
					},
					close: function() {
						$(this).dialog('close');
					}
	          });
	    }else{
	    	//if not, display Approval Sales return and Approved and declined
	$.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp', 'id='+id,
	        function(data){
	        	$('#sales-return-dashboard-view-container').html(data);  
	        	$("#sales-return-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 485,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var statusParameter='Approved';
	    				$.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initSalesReturnOnLoad();
	    						$("#sales-return-dashboard-view-dialog").dialog('close');
	    					});
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('salesReturn.json', 'salesReturnId='+id+'&action=sales-return-approval'+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initSalesReturnOnLoad();
		    						$("#sales-return-dashboard-view-dialog").dialog('close');
		    					});
	    		          }
	    			},
	    			close: function() {
	    				$('#sales-return-dashboard-view-container').html('');
	    			}
	    		});
	        });
	    }
	  });
	 });
$('#change-request-row').live('click',function(){
	var id = $(this).attr('align');
	var businessName=$('#customerBusinessName').html();
	$.post('customer/customer_change_request_dashboard_approved.jsp', 'id='+id+'&businessName='+businessName,
	        function(data){
	        	$('#customer-change-request-view-container').html(data);  
	        	$("#customer-change-request-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 485,
	    			width: 700,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var thisButton = $(this);
	    				var resultSuccess=true;
	    				var resultFailure=false;
	    				if(DashbookHandler.validateCustomerChangeRequestCredits()==false){
	    					return resultSuccess;
	    				}else{
	    				var creditLimit=currencyHandler.convertStringPatternToFloat($('#creditLimit').val());
	    				var creditOverdueDays=$('#creditOverdueDays').val();
	    				var statusParameter='Approved';
	    				$.post('customerCr.json', 'id='+id+'&action=approve-customer-cr'+'&creditLimit='+creditLimit+'&creditOverdueDays='+creditOverdueDays+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initSearchCustomerChangeRequestOnLoad();
	    						$("#customer-change-request-view-dialog").dialog('close');
	    					});
	    				   }
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('customerCr.json', 'id='+id+'&action=approve-customer-cr'+'&creditLimit='+creditLimit+'&creditOverdueDays='+creditOverdueDays+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initSearchCustomerChangeRequestOnLoad();
		    						$("#customer-change-request-view-dialog").dialog('close');
		    					});
	    			     // $(this).dialog('close');
	    		          }
	    			},
	    			close: function() {
	    				$('#customer-change-request-view-container').html('');
	    			}
	    		});
	        }
	    );
});
$('#delivery-note-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/delivery_note_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#delivery-note-dashboard-view-container').html(data);  
	        	$("#delivery-note-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 550,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var thisButton = $(this);
	    				var statusParameter='Approved';
	    				 $.post('changeTransaction.json', 'deliverNoteCRId='+id+'&action=approve-delivery-note-cr'+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initDeliveryNoteCROnLoad();
	    						$("#delivery-note-dashboard-view-dialog").dialog('close');
	    					});
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('changeTransaction.json', 'deliverNoteCRId='+id+'&action=approve-delivery-note-cr'+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initDeliveryNoteCROnLoad();
		    						$("#delivery-note-dashboard-view-dialog").dialog('close');
		    					});
	    			     // $(this).dialog('close');
	    		          }
	    			},
	    			close: function() {
	    				$('#delivery-note-dashboard-view-container').html('');
	    			}
	    		});
	        	$('#change-request-dn-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		$.post('changeTransaction.json', 'action=get-delivery-note-id'+'&invoiceNumber='+invoiceNumber,
	        				function(obj){
	        					var deliveryNoteId = obj.result.data;
	        					if(deliveryNoteId != 0){
	        						 $.post('my-sales/transactions/view-transactions/delivery_note_results_view.jsp','id=' + deliveryNoteId,
	        						           function(data) {
	        						            $('#delivery-note-container').html(data);
	        						            var dialogOpts = {
	  	        						     		   height : 500,
	  	        						     		   width : 1020,
	        						            		buttons : {
	    	        						     		    Close : function() {
	    	        						     		     $(this).dialog('close');
	    	        						     		    }
	    	        						     		 },
	        						            	};
	        						            $("#delivery-note-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        	});
	    });
});

$('#sales-return-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/sales_return_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#sales-return-change-request-dashboard-view-container').html(data);  
	        	$("#sales-return-change-request-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 420,
	    			width: 1000,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var thisButton = $(this);
	    				var statusParameter='Approved';
	    				$.post('changeTransaction.json', 'salesReturnCRId='+id+'&action=approve-sales-return-cr'+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initSalesReturnCROnLoad();
	    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
	    					});
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('changeTransaction.json', 'salesReturnCRId='+id+'&action=approve-sales-return-cr'+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initSalesReturnCROnLoad();
		    						$("#sales-return-change-request-dashboard-view-dialog").dialog('close');
		    					});
	    		          }
	    			},
	    			close: function() {
	    				$('#sales-return-change-request-dashboard-view-container').html('');
	    			}
	    		});
	        	$('#change-request-salesreturn-Invoice-number').click(function(){
	        		var invoiceNumber=$(this).attr('class');
	        		$.post('changeTransaction.json', 'action=get-sales-return-id'+'&invoiceNumber='+invoiceNumber,
	        				function(obj){
	        					var salesReturnId = obj.result.data;
	        					if(salesReturnId != 0){
	        						 $.post('my-sales/transactions/view-transactions/sales_returns_preview.jsp','id=' + salesReturnId,
	        						           function(data) {
	        						            $('#sales-return-container').html(data);
	        						            var dialogOpts = {
		  	        						     		   height : 420,
		  	        						     		   width : 1030,
		        						            		buttons : {
		    	        						     		    Close : function() {
		    	        						     		     $(this).dialog('close');
		    	        						     		    }
		    	        						     		 },
		        						            	};
		        						            $("#sales-return-dialog").dialog(dialogOpts);
	        						 });
	        				}
	        		});
	        	});
	        });
});

$('#day-book-change-request-row').live('click',function(){
	var id = $(this).attr('align');
	$.post('my-sales/transactions/change-transactions/day_book_change_transaction_results_view.jsp', 'id='+id,
	        function(data){
	        	$('#day-book-dashboard-view-container').html(data);  
	        	$("#day-book-dashboard-view-dialog").dialog({
	    			autoOpen: true,
	    			height: 550,
	    			width: 900,
	    			modal: true,
	    			buttons: {
	    			Approved: function() {
	    				var thisButton = $(this);
	    				var statusParameter='Approved';
	    				$.post('changeTransaction.json', 'dayBookCrId='+id+'&action=approve-day-book-cr'+'&status='+statusParameter,
	    						 function(obj) {
	    					    DashbookHandler.initDayBookCROnLoad();
	    						$("#day-book-dashboard-view-dialog").dialog('close');
	    					});
	    				},
	    		      Decline: function() {
	    		    	  var statusParameter='Decline';
	    		    	  $.post('changeTransaction.json', 'dayBookCrId='+id+'&action=approve-day-book-cr'+'&status='+statusParameter,
		    						 function(obj) {
		    					    DashbookHandler.initDayBookCROnLoad();
		    						$("#day-book-dashboard-view-dialog").dialog('close');
		    					});
	    		          }
	    			},
	    			close: function() {
	    				$('#day-book-dashboard-view-container').html('');
	    			}
	    		});
	        }
	    );
});
var ProfileHandler = {
		load: function() {
		pagecontent({full: true, height: '700px'});
		$('.main-container').height('700px');
		$('#droppable1').height('700px');
		$('#droppable2').height('700px');
        } ,			
		
		initProfileMain: function() {
		$('.profile-theme-name').click(function(){
			var thisTheme = $(this).attr('name');
			var paramString = 'action=change_theme&theme='+thisTheme;
			$.post('profile.json', paramString,
			    function(obj){
				});
		});
		
/*		$('#left').click(function(){
			  $("#theme-content").animate({"left": "+=50px"}, 1000);
			//$("#theme-content").animate({width:"55px",right: ""}, 1000);
			});
		$('#right').click(function(){
			  $("#theme-content").animate({"left": "-=50px"}, 1000);
			  //$("#theme-content").animate({width:"55px"}, 1000);
			});
*/
		
	},
	initChangeStaffPassword : function (userName) {
			$('#action-save').click(function() {
			var p1=($('#newPassWord')).val();
			var p2=($('#confirmPassWord')).val();
			var p3=($('#oldPassWord')).val();
			    if(!p1.length>0 && !p2.length>0){
				     $('#change-password-form').clearForm();
				     $('#messageId').html('Passwords should Not be empty');
				}else if(p1===p3 ||p2==p3){
					$('#change-password-form').clearForm();
				    $('#messageId').html('Old password and new Password should Not be same');
				}else if(p1 != p2){
					$('#change-password-form').clearForm();
		          	$('#messageId').html('Passwords are Not matched');
		       }else if(p1===userName){
		          $('#change-password-form').clearForm();
		          $('#messageId').html('Username and password should not be same');
		       }else{
		          var thisButton = $(this)
					var paramString = $('#change-password-form').serialize();
					$.post('profile.json', paramString,
				        function(obj){
				            var result=obj.result.message;
				             if(result==='Failed'){
				               $('#messageId').html('Old password is incorrect');
				             }else{
							    $('.profile-div').html('<div class="success-msg">Saved Succesfully</div>')
							 }
				        }
			        );
		      }
			});
			$('#action-cancel').click(function() {
				$('#change-password-form').html('');
			});
			
			$('#action-clear').click(function() {
				$('#change-password-form').clearForm();
				$('#search-results-list').html('<div class="green-result-row">Search Results will be show here</div>');
			});
	}
	
};

var HelpHandler = {
		
		load: function() {
			pagecontent({full: true, height: '700px'});
			$('.ui-content').height($('.page-container').height());
			$('.ui-content').jScrollPane({showArrows:true});
			
			$('#ps-exp-col').click(function() {
				setTimeout(function() {
					$('.ui-content').jScrollPaneRemove()
					$('.ui-content').jScrollPane({showArrows:true});
	            }, 0);
			});

			
			
		}
};

var DragAndDropHandler = {
		
		initDragAndDrop: function() {
			
			$( ".draggable" ).draggable();
			$( "#droppable1" ).droppable();
			$( "#droppable2" ).droppable({
			});
		}
};