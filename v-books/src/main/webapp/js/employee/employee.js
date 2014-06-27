/*var UserHandler={
},*/
var EmployeeHandler = {
		
		theme: "",
		expanded: true,
		flag:true,
 initPageLinks: function() {
		$('#employee-add').pageLink({container:'.employee-page-container', url:'employee/employee_add.jsp'});
		$('#employee-search').pageLink({container:'.employee-page-container', url:'employee/employee_search.jsp'});
		$('#employee-import').pageLink({container:'.employee-page-container', url:'employee/employee_import.jsp'});
},	
		
	
	employeeSteps:['#employee-form','#employee-detail-form','#employee-address-form'],
	employeeUrl:['employee.json','employee.json','employee.json'],
	employeeStepCount: 0,
	
initEmployeePageSelection: function(){
	PageHandler.hidePageSelection();
},	
initAddButtons: function () {
		EmployeeHandler.employeeStepCount = 0;
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (this.readOnly) {
			    	return
			    }
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			};
	
			
	$('#action-clear').click(function() {
		$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
		$("#error-message").dialog({
			resizable: false,
			height:140,
			title: "<span class='ui-dlg-confirm'>Confirm</span>",
			modal: true,
			buttons: {
				'Ok' : function() {
					$(EmployeeHandler.employeeSteps[EmployeeHandler.employeeStepCount]).clear();
					$('#pValid').empty();
					$('#uValid').empty();
					$('#unValid').empty();
					$('#pwValid').empty();
					$('#fnameValid').empty();
					$('#mnameValid').empty();
					$('#lnameValid').empty();
					$('#emailValid').empty();
					$('#employeeTypeValid').empty();
					$('#mobileValid').empty();
					$('#dlineValid').empty();
					$('#alternateValid').empty();
					$('#bgroupValid').empty();
					$('#passportValid').empty();
					$('#nationValid').empty();
					$('#addressLine2Valid').empty();
					$('#addressLine1Valid').empty();
					$('#landmarkValid').empty();
					$('#localityValid').empty();
					$('#cityValid').empty();
					$('#stateValid').empty();
					$('#pincodeValid').empty();
					$('#addressTypeValid').empty();
	    			$(this).dialog('close');

				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
	    return false;
	});
	
	$('#button-next').click(function() {
		var uname=$('#username').val();
		var pwd=$('#password').val();
		/*if($('#password').val()!==''&&pwd!==undefined){
			if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val()) == false || pwd == uname || pwd.length < 8 ){
				UserHandler.flag =false;
			}else{
				UserHandler.flag =true;
			}
		}else{
			UserHandler.flag =true;
		}*/
		var thisButton = $(this);
		var resultSuccess=true;
		var resultFailure=false;
		/*if(UserHandler.flag==false){
			return resultSuccess;
		}*/
		if(EmployeeHandler.employeeStepCount == 0){
			if(uname=="")
			{
			 $('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
			}
			else
				{
				var orgPrefix = $('#orgPrefix').val();
				var paramString='username='+orgPrefix+uname+'&action=validate-username';
				var result ;
				$.ajax({type: "POST",
					url:'employee.json', 
					data: paramString,
					async : false,
					success: function(data){
						result = data.result.data;
					},
				/*$.post('employee.json',paramString, function(data){*/
		          
				});
				if(result == 'n'){
					EmployeeHandler.flag = false;
				}else{
					EmployeeHandler.flag = true;
				}
				}
			if(EmployeeHandler.flag==false){
				return resultSuccess;
			}
			if($("#password").length > 0){
		if(ValidateEmployeeHandler.validateEmployee()==false){
			$("#username").val('');
			$("#password").val('');
			$("#uValid").empty();
			$("#pValid").empty();
			return resultSuccess;
		}
			}else {
				if(ValidateEmployeeHandler.validateEmployeeUpdate()==false){
					return resultSuccess;
				}
			}
		}else if (EmployeeHandler.employeeStepCount == 1) {
			if(ValidateEmployeeHandler.validateEmployeeStepOne()==false){
				return resultSuccess;
			}
		}else {
			if(ValidateEmployeeHandler.validateEmployeeStepTwo()==false){
				return resultSuccess;
			}
		}
		var paramString = $(EmployeeHandler.employeeSteps[EmployeeHandler.employeeStepCount]).serialize();
		$.ajax({type: "POST",
			url:'employee.json', 
			data: paramString,
			success: function(data){
				$('#error-message').html('');
				$('#error-message').hide();
				$(EmployeeHandler.employeeSteps[EmployeeHandler.employeeStepCount]).hide();
				$(EmployeeHandler.employeeSteps[++EmployeeHandler.employeeStepCount]).show();
				if(EmployeeHandler.employeeStepCount==EmployeeHandler.employeeSteps.length) {
					if(!PageHandler.expanded){
						PageHandler.hidePageSelection();//this is to enlarge preview container on loading page
					}
					else{
						
						PageHandler.pageSelectionHidden =false;
						PageHandler.hidePageSelection();
					}
					$('#button-next').hide();
					$('#action-clear').hide();
					$('#button-save').show();
					$('#button-update-employee').show();
					$.post('employee/employee_view.jsp', 'viewType=preview',
					        function(data){
						 $('#employee-preview-container').css({'height' : '350px'});
							$('#employee-preview-container').html(data);
							$('.table-field').css({"width":"662px"});
							$('.main-table').css({"width":"330px"});
							$('.inner-table').css({"width":"330px"});
							$('.display-boxes-colored').css({"width":"125px"});
							$('.display-boxes').css({"width":"200px"});
							$('#employee-preview-container').show();
							EmployeeHandler.expanded=false;
							$('#ps-exp-col').click(function() {
								if (EmployeeHandler.employeeStepCount==EmployeeHandler.employeeSteps.length)
								{
							    if(!PageHandler.expanded) {
							    	$('#employee-preview-container').css({'height' : '350px'});
									$('#employee-preview-container').html(data);
									$('.table-field').css({"width":"800px"});
									$('.main-table').css({"width":"400px"});
									$('.inner-table').css({"width":"400px"});
									$('.display-boxes-colored').css({"width":"140px"});
									$('.display-boxes').css({"width":"255px"});
									$('#employee-preview-container').show();
									EmployeeHandler.expanded=false;
							    }
							    else{
							    	$('#employee-preview-container').css({'height' : '350px'});
									$('#employee-preview-container').html(data);
									$('.table-field').css({"width":"662px"});
									$('.main-table').css({"width":"330px"});
									$('.inner-table').css({"width":"330px"});
									$('.display-boxes-colored').css({"width":"125px"});
									$('.display-boxes').css({"width":"200px"});
									$('#employee-preview-container').show();
									EmployeeHandler.expanded=true;
							    }
								}
					        });
				});
				}if(EmployeeHandler.employeeStepCount>0) {
					$('#button-prev').show();
					$('.page-buttons').css('margin-left', '230px');
					
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
	
	$("#username").focus(function(event){
		$('#uValid').empty();
		 $('#username_pop').show();
		 $('#username_popDuplicate').hide()
	});
	$("#username").blur(function(event){
		 $('#username_pop').hide();
		 $('#username_popDuplicate').hide()
	});
	
	$("#password").focus(function(event){
		$('#pValid').empty();
		 $('#password_pop').show();
	});
	$("#password").blur(function(event){
		 $('#password_pop').hide();
	});
	$('#button-prev').click(function() {
		$('#action-clear').show();
		if(EmployeeHandler.employeeStepCount==EmployeeHandler.employeeSteps.length) {
			if(!EmployeeHandler.expanded){
				PageHandler.pageSelectionHidden =false;
				PageHandler.hidePageSelection();
				EmployeeHandler.expanded=true;
			}
			$('#button-next').show();
			$('#button-save').hide();
			$('#button-update-employee').hide();
			$('#employee-preview-container').html('');
			$('#employee-preview-container').hide();
			$('.page-buttons').css('margin-left', '150px');
		}
		$(EmployeeHandler.employeeSteps[EmployeeHandler.employeeStepCount]).hide();
		$(EmployeeHandler.employeeSteps[--EmployeeHandler.employeeStepCount]).show();
		if(EmployeeHandler.employeeStepCount>0) {
			$('#button-prev').show();
			$('#button-update-employee').hide();
			$('.page-buttons').css('margin-left', '150px');
		} else {
			$('#button-prev').hide();
			$('#button-update-employee').hide();
			$('.page-buttons').css('margin-left', '240px');
		}
	});
	$('#button-save').click(function() {
		var thisButton = $(this);
		var paramString = 'action=save-employee';
		PageHandler.expanded=false;
		pageSelctionButton.click();
		$('.page-content').ajaxSavingLoader();
		$.post('employee.json', paramString,
	        function(obj){
			 $.loadAnimation.end();
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
				
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
	    			var container ='.employee-page-container';
	    			var url = "employee/employee_add.jsp";
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
	$('#action-cancel-edit').click(function() {
	    $('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
		$("#error-message").dialog({
			resizable: false,
			height:140,
			title: "<span class='ui-dlg-confirm'>Confirm</span>",
			modal: true,
			buttons: {
				'Ok' : function() {
	    			$(this).dialog('close');
	    			$('.task-page-container').html('');
	    			location.href = "index.jsp?module=employee"

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
			$('.page-content').ajaxSavingLoader();
			$.post('employee.json', paramString,
		        function(obj){
				 $.loadAnimation.end();
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
		        }
		    );
		});

		
		$('#button-update-employee').click(function() {
			var thisButton = $(this);
			var paramString ='action=update-employee';
			PageHandler.expanded=false;
			pageSelctionButton.click();
			$('.page-content').ajaxSavingLoader();
			$.post('employee.json', paramString,
		        function(obj){
				$.loadAnimation.end();
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
		        }
		    );
		});

	},
	importEmployee: function(){
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
initsearchEmployeeOnload :function(){
	var paramString="action=get-disabled-employee-list";
	$.post('employee.json', paramString, function(obj){
    var employeeName = obj.result.data;
	var paramString = $('#employee-search-form').serialize(); 
	$('#search-results-list').ajaxLoader();
	$.post('employee.json', paramString, function(obj){
	    	var data = obj.result.data;
			$('#search-results-list').html('');
			if(data != undefined) {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate" id="employee-row-'+data[loop].id +'">';
						} else {
							rowstr = '<div class="green-result-row" id="employee-row-'+data[loop].id +'">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-column" style="width: 830px;">';
						rowstr = rowstr + '<div class="green-result-col-1" style="width:275px !important;">'+
						'<div class="result-title">' + data[loop].firstName +'</div>' +
						'<div class="result-body">' +
						'<span class="property">'+ Msg.EMPLOYEE_USERNAME +'</span><span class="property-value">' + data[loop].username +'</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-2" style="width:240px !important;">'+
						'<div class="result-body">' +
						'<span class="property">Mobile Number </span><span class="property-value">' + fmt(data[loop].mobile) + '</span>' +
						'</div>' +'<span class="property">'+Msg.EMPLOYEE_EMPLOYEE_TYPE_LABEL+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].employeeTypeByString + '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action" id="green-result-col-action-'+data[loop].id+'" style="width:125px !important;">';
						rowstr = rowstr +'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Employee" style="margin-top:1px;"></div>' +
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Employee Details"style="margin-top:1px;"></div>';
						if('Super Management' == data[loop].loginEmployeeType || 'MGT' == data[loop].loginEmployeeType){
							rowstr = rowstr+'<div id="'+data[loop].id+'" class="login-track btn-login-track" title="View Login Details" style="margin-top:0px; width:16px !important; margin-left:5px !important;"></div>';
						}
						if('Super Management' == data[loop].loginEmployeeType && 'super management' != data[loop].employeeType ){
							rowstr = rowstr+'<div id="'+data[loop].id+'" userName="'+data[loop].username+'" class="reset-password" title="Reset Password" style="margin-top:0px; width:16px !important; margin-left:5px !important;"></div>';
						}
						if('true' == data[loop].showDisable){
							rowstr = rowstr+'<div id="'+data[loop].id+'" align="'+ data[loop].employeeType +'" class="disable-icon enable-disable" title="Disable Employee"></div>';
						}
						rowstr = rowstr+'</div>';
						'</div>';
						$('#search-results-list').append(rowstr);
					
						if(employeeName == undefined){
						}else{
						for(var i=0; i<employeeName.length; i=i+1) {
						if(data[loop].username == employeeName[i].userName){
							//$("#employee-row-"+ data[loop].id).css("width","580px");
							$("#employee-row-"+ data[loop].id).css('opacity','0.5');
							 //$("#green-result-col-action-"+ data[loop].id).find(".edit-icon").css("pointer-events", "none");
							$("#green-result-col-action-"+ data[loop].id).find(".edit-icon").hide();
							 $("#green-result-col-action-"+ data[loop].id).find(".btn-view").css("pointer-events", "visible");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-disable").addClass("enable-icon");
							$("#green-result-col-action-"+ data[loop].id).find(".enable-disable").removeClass("disable-icon");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-icon").attr('title', 'Enable Employee');
						    }else{
						    	 $("#green-result-col-action-"+ data[loop].id).find(".disable-icon").attr('title', 'Disable Employee');
						    }
						}
					}
			};
			EmployeeHandler.initSearchResultButtons();
			$('#search-results-list').jScrollPane({showArrows:true});
			
			} else {
				$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Results Found</div></div></div>');
			}
			$.loadAnimation.end();
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},0);
	  });
        });

},
AjaxSucceeded: function(data1) {
    if (data1.result.data == "y") {
        $('#employeeNumberValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='EmployeeNo available!'> Available!");
    } else if (data1.result.data == "v") {
        $('#employeeNumberValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'> Available!");
    	$('#description').focus();
    }
    /*confirming and displaying remainder date is valid or Invalid*/
     else{
        $('#employeeNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>EmployeeNo Alreday Exists!");
        $('#empno').focus();
    }
    
},
	
	initSearchEmployee : function (role) {
		//Method overridden to enlarge page size on clicking
		$('#ps-exp-col').click(function() {
			setTimeout(function() {
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').jScrollPane({showArrows:true});
            }, 0);
		});
		//button click - cancel
	
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
		$('#action-clear').click(function() {
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
		});
		
		//button click - search
		$('#action-search-employee').click(function() {
		/*	var paramString="action=get-disabled-employee-list";*/
			/*$('#search-results-list').ajaxLoader();*/
			EmployeeHandler.initsearchEmployeeOnload();
		});
	},
	
initSearchResultButtons : function () {
	$('.edit-icon').click(function() {
		var arry =[];
		var id = $(this).attr('id');
		$.post('employee.json','action=get-employee-data&id='+id,function(obj){
			var result = obj.result.data;
			$.post('employee/employee_edit.jsp', 'id='+id, function(data){
							$('.employee-page-container').html(data);
							$("#id").val(id);
							$('#userName').val(result.userName);
							$('#firstName').val(result.firstName);
							$('#middleName').val(result.middleName);
							if(result.gender =='F'){
								$('#female').attr('checked','checked');
							}else{
								$('#male').attr('checked','checked');
							}
							$("#employeeEmail").val(result.employeeEmail);
							$('#employeeType option').each(function(index,option){
								if($(this).val() == result.employeeType){
									arry.push($(this).val());
										$('#employeeType').val(result.employeeType).attr('selected','selected');
								}
							});
							if(arry.length == 0){
								$('#employeeType').append( $('<option></option').text(result.employeeType).attr('selected','selected'));
								$('#bloodGroup').removeClass('mandatory');
								$('#passPortNumber').removeClass('mandatory');
								$('#lastName').removeClass('mandatory');
								//$('#employeeType').val('Super Management').text('Super Management').attr('selected','selected');
							}
							$('#employeeType').val(result.employeeType).attr('selected','selected');
							if(result.employeeType == "SLE"){
								$('#salesExecutive_granted_days').show();
								$('#grantedDays').val(result.grantedDays);
							}else{
								$('#salesExecutive_granted_days').hide();
							}
							$('#lastName').val(result.lastName);
							$('#employeeNumber').val(result.employeeNumber);
							$('#mobile').val(result.mobile);
							$('#directLine').val(result.directLine);
							$('#alternateMobile').val(result.alternateMobile);
							$('#bloodGroup').val(result.bloodGroup);
							$('#passPortNumber').val(result.passPortNumber);
							$('#nationality').val(result.nationality);
							$('#addressLine1').val(result.addressLine1);
							$('#addressLine2').val(result.addressLine2);
							$('#locality').val(result.locality);
							$('#landmark').val(result.landmark);
							$('#city').val(result.city);
							$('#state').val(result.state);
							$('#zipcode').val(result.zipcode);
							var addressTypeSelected=result.addressType;
							$.post('default.json','action=get-address-types',function(obj){
								var addTypes = obj.result.data;
								if(addTypes.length>0){
									$('#addressType').empty();
									var select= 'select'
									for(var loop=0;loop<addTypes.length-1;loop = loop+1){
										if(addTypes[loop].addressType == addressTypeSelected){
											$('#addressTypeEdit').append( $('<option value='+ addTypes[loop].addressType +'></option').text(addTypes[loop].addressType).attr('selected','selected'));
										}else{
											$('#addressTypeEdit').append( $('<option value='+ addTypes[loop].addressType +'></option').text(addTypes[loop].addressType));
										}
										
										
								};
								}
							});
							
							  
			        });
			
		});
		
	});
		$('.enable-icon').click(function(){
			var employeeId = $(this).attr('id'); 
				 var employeeStatusParam='Enabled';
			     $('#employee-list-container').html('Are You Sure Want To Enable This Employee ?');
				  $('#employee-list-dialog').dialog({
						autoOpen: true,
						height: 200,
						width: 500,
						modal: true,
						buttons: {
							Yes: function() {
								 $.post('employee.json', 'id='+employeeId+'&action=modify-employee-status&employeeStatusParam='+employeeStatusParam, 
									function(obj) {
									 $("#employee-row-"+ employeeId).css("width","580px");
									 $("#employee-row-"+ employeeId).css('opacity', '0.5');
									 $("#green-result-col-action-"+ employeeId).find(".edit-icon").css("pointer-events", "visible");
									 $("#green-result-col-action-"+ employeeId).find(".btn-view").css("pointer-events", "visible");
									 $("#green-result-col-action-"+ employeeId).find(".enable-disable").removeClass("enable-icon");
									 $("#green-result-col-action-"+ employeeId).find(".enable-disable").addClass("disable-icon");
									 $("#green-result-col-action-"+ employeeId).find(".disable-icon").attr("title","Disable Employee");
									 EmployeeHandler.initsearchEmployeeOnload();
								 });
								 $("#employee-list-dialog").dialog('close');
							},
				            No: function() {
				            	$('#employee-list-dialog').dialog('close');
					      }
						},
						close: function() {
							$('#employee-list-dialog').dialog('close');
						}
		        });
		});
			 $('.disable-icon').click(function(){
			  var employeeId = $(this).attr('id');
			  var employeeStatusParam='Disabled';
			  var employeetype = $(this).attr('align');
			  if(employeetype == 'sle'){
				  $.post('employee.json', 'action=check-credits-for-sales-executive&id='+employeeId, 
							function(obj) {
					 var data=obj.result.data;
					 if(data == 'true'){
						  $('#employee-list-container').html('Are You Sure Want To Disable This Employee ?');
						  $('#employee-list-dialog').dialog({
								autoOpen: true,
								height: 200,
								width: 500,
								modal: true,
								buttons: {
									Yes: function() {
										 $.post('employee.json', 'id='+employeeId+'&action=modify-employee-status&employeeStatusParam='+employeeStatusParam, function(obj) {
											 $("#employee-row-"+ employeeId).css('opacity', '1');
											 $("#green-result-col-action-"+ employeeId).find(".edit-icon").css("pointer-events", "none");
											 $("#green-result-col-action-"+ employeeId).find(".btn-view").css("pointer-events", "visible");
											 $("#green-result-col-action-"+ employeeId).find(".enable-disable").addClass("enable-icon");
											 $("#green-result-col-action-"+ employeeId).find(".enable-disable").removeClass("disable-icon");
											 $("#green-result-col-action-"+ employeeId).find(".enable-icon").attr("title","Enable Employee");
											 EmployeeHandler.initsearchEmployeeOnload();
										 });
										 $("#employee-list-dialog").dialog('close');
									},
						            No: function() {
						            	$('#employee-list-dialog').dialog('close');
							      }
								},
								close: function() {
									$('#employee-list-dialog').dialog('close');
								}
				           });
					 }else{
						 showMessage({title:'Message', msg:obj.result.message});
					 }
			   });
			  }else{
				  $('#employee-list-container').html('Are You Sure Want To Disable This Employee ?');
				  $('#employee-list-dialog').dialog({
						autoOpen: true,
						height: 200,
						width: 500,
						modal: true,
						buttons: {
							Yes: function() {
								 $.post('employee.json', 'id='+employeeId+'&action=modify-employee-status&employeeStatusParam='+employeeStatusParam, function(obj) {
									 $("#employee-row-"+ employeeId).css('opacity', '1');
									 $("#green-result-col-action-"+ employeeId).find(".edit-icon").css("pointer-events", "none");
									 $("#green-result-col-action-"+ employeeId).find(".btn-view").css("pointer-events", "visible");
									 $("#green-result-col-action-"+ employeeId).find(".enable-disable").addClass("enable-icon");
									 $("#green-result-col-action-"+ employeeId).find(".enable-disable").removeClass("disable-icon");
									 $("#green-result-col-action-"+ employeeId).find(".enable-icon").attr("title","Enable Employee");
									 EmployeeHandler.initsearchEmployeeOnload();
								 });
								 $("#employee-list-dialog").dialog('close');
							},
				            No: function() {
				            	$('#employee-list-dialog').dialog('close');
					      }
						},
						close: function() {
							$('#employee-list-dialog').dialog('close');
						}
		           });
			  }
			
		});

		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('employee.json','action=get-employee-data&id='+id,function(obj){
				var result = obj.result.data;
				$.post('employee/employee_profile_view.jsp', 'id='+id, function(data){
					$('#employee-view-container').html(data);
					$('.table-field').css({"width":"800px"});
					$('.main-table').css({"width":"400px"});
					$('.inner-table').css({"width":"400px"});
					$('.display-boxes-colored').css({"width":"140px"});
					$('.display-boxes').css({"width":"255px"});
						$("#employee-view-dialog").dialog('open');
						$('#userName').text(result.userName);
						$('#fName').text(result.firstName);
						$('#middleName').text(result.middleName);
						$("#lName").text(result.lastName);
						if(result.gender =='F'){
							$('#gender').text('Female');
						}else{
							$('#gender').text('Male');
						}
						$("#email").text(result.employeeEmail);
						$('#eType').text(result.employeeType);
						if(result.employeeType == "SLE"){
							$('#grantedDaysField').css("display","visible");
							$("#grantedDays").text(result.grantedDays);
						}else{
							$('#grantedDaysField').css("display","none");
						}
						$('#employeeNumber').text(result.employeeNumber);
						$('#mobile').text(result.mobile);
						$('#directLine').text(result.directLine);
						$('#alternateMobile').text(result.alternateMobile);
						$('#bloodGroup').text(result.bloodGroup);
						$('#passportNumber').text(result.passPortNumber);
						$('#nationality').text(result.nationality);
						$('#addressLine1').text(result.addressLine1);
						$('#addressLine2').text(result.addressLine2);
						$('#locality').text(result.locality);
						$('#landMark').text(result.landmark);
						$('#city').text(result.city);
						$('#state').text(result.state);
						$('#zipcode').text(result.zipcode);
						$('#addressType').text(result.addressType);
			        }
		        );
			});
		});
		
		$('.login-track').click(function() {
			var id = $(this).attr('id');
			$.post('employee.json', 'action=get-login-details&id='+id, function(obj) {
				var data = obj.result.data;
				$("#employee-login-view-dialog").dialog('open');
				if(data != undefined) {
					var viewLoginTrack="";
					viewLoginTrack +='<div class="report-header" style="width: 780px; height: 30px;">'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 148px;"> '+ Msg.EMPLOYEE_LOGIN_SNO +'</div>' +
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 200px;">'+ Msg.EMPLOYEE_LOGIN_USER_NAME +'</div>'+
					'<div class="report-header-column2 centered report-header-transaction-column2" style="width: 345px;">'+ Msg.EMPLOYEE_LOGIN_LOGIN_TIME +'</div>'+
					'</div>';
		            $('#employee-login-view-container').append(viewLoginTrack);
		            $('#employee-login-view-container').append('<div class="grid-content" style="height:242px;width: 780px; overflow-y:initial;"></div>'); 
					for(var loop=1; loop<=data.length; loop=loop+1) {
						var viewLoginTrackRows ='<div class="ui-content report-content">'+
						'<div class="report-body" style="width: 780px; height: 30px; overflow: hidden; line-height:20px;">'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 148px; word-wrap: break-word;">' +  loop  + '</div>' +
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 200px; word-wrap: break-word;">'+ data[loop-1].userName +'</div>'+
						'<div class="report-body-column2 centered report-body-transaction-column2 sameHeight" style="height: inherit; width: 345px; word-wrap: break-word;">'+ data[loop-1].loginTime +'</div>'+
						'</div>'+
						'</div>';
						$('.grid-content').append(viewLoginTrackRows);
					}
				} else {
					$('#employee-login-view-container').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
				}
			});
		});
		$('.reset-password').click(function(){
			var userName = $(this).attr('userName');
			$('#employee-reset-password-dialog').dialog('open');
			var userDetails = "";
			userDetails += '<div class="fieldset" style="height: 40px;">'+
			'<div class="form-row">'+'<div class ="label">' +Msg.EMPLOYEE_USERNAME+ '</div>'+
			'<div class="input-field" >'+'<input class ="remove-inputfield-style mandatory" name = "userName" value ="'+userName+'" readonly ="readonly" border ="none">'+'</div>'+'</div>';
			'</div>'; 
			userDetails +='<div class="fieldset" style="height: 40px;">'+
			'<div class="form-row">'+'<div class ="label">New Password</div>'+
			'<div class="input-field" >'+'<input id="password" name = "password" type = "password" class="mandatory">'+'</div>'+
			'<span id="pValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px">'+'</span>'+
			'<span id="pwValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px">'+'</span>'+
			'<div id="password_pop" class="helppop" style="display: block;"aria-hidden="false">'+
				'<div id="namehelp" class="helpctr"style="float: left; margin-left: 3px;">'+
					'<p>password should be 8, must contains atleast one alphanumeric, capital, small & special characters.</p>'+
				'</div>'+
			'</div>'+
			'</div>';
			$('.helppop').hide();
			userDetails =userDetails + '<div id="page-buttons" class="page-buttons"	style="margin-left: 240px; margin-top: 75px;" align="center">'+
							'<div id="submit" class="ui-btn btn-search">Submit</div>'+
							'<div id="clear" class="ui-btn btn-clear">Clear</div>'+
							'<div id="cancel" class="ui-btn btn-cancel">Cancel</div>'+'</div>';
			$('#employee-reset-password-container').append(userDetails);
			$('#employee-reset-password-container').append('<div class="grid-content" style="height:242px;width: 780px; overflow-y:initial;"></div>');
			$(document).ready(function() {
				$('.helppop').hide();
			});
		
		$("#submit").click(function(){
			if(EmployeeHandler.validatePassword() != false){
				var password = $('#password').val();
				$.post('employee.json','action=reset-password&userName='+userName+'&password='+password,function(obj){
					if(obj.result.data == "true"){
						showMessage({title:'Message', msg:'<b>Old password and new password should not match</b>'});
					}else{
						$('#employee-reset-password-dialog').dialog('close');
						showMessage({title:'Message', msg:'<b>'+obj.result.message+'</b>'});
					}
					
					
				});
			}
				
			});
			$('#clear').click(function(){
				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('#password').val('');
								$(this).dialog("close");
							},
							Cancel : function(){
								$(this).dialog('close');
							}
						}
					});
			
			});
			$('#cancel').click(function(){
				 $('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('#password').val('');
								$(this).dialog("close");
								$('#employee-reset-password-dialog').dialog('close');
							},
							Cancel : function(){
								$(this).dialog('close');
							}
						}
					});
			
			
			});
		});
		$('#employee-reset-password-dialog').dialog({
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
				$('#employee-reset-password-container').html('');
			}
		});
		
		$("#employee-login-view-dialog").dialog({
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
				$('#employee-login-view-container').html('');
			}
		});
		
		$("#employee-view-dialog").dialog({
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
				$('#employee-view-container').html('');
			}
		});

	
		$('.btn-employee-change-img').click(function() {
			var id = $(this).attr('id');
			$.post('employee/employee_image_upload.jsp', 'id='+id,
		        function(data){
				$('.task-page-container').html(data);
		        }
	        );
		});
		$('.btn-changepassword').click(function() {
	    	var id = $(this).attr('id');
			var username=$(this).attr('username');
			$('#error-message').html('Do you want to reset password?'); 
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "Confirm",
				modal: true,
				buttons: {
					'Ok': function(){
				$(this).dialog('close');
			    $.post('profile.json', 'action=reset_password&changePasswordId='+id+'&userName='+username,function(obj){
			    	var data = obj.result.data;
			    //	$(this).successMessage({container:'.task-page-container', data:'Your New Possword:'+data});
				    $('#error-message').html(data);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "New Password",
						modal: true,
						buttons: {
							'Ok': function() {
				    			$(this).dialog('close');
							    }
						        }
					        });
		                });
			 	     },
			 	
				'Cancel': function() {
					$(this).dialog('close');
				}
			}
		});
	});
		
	},
	validatePassword : function(){
		var result;
		if(($('#password').val()).length == 0){
			$('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#password").focus(function(event){
				$('#pwValid').empty();
				 $('#password_pop').show();
			});
			$("#password").blur(function(event){
				 $('#password_pop').hide();
				 if(($('#password').val()).length == 0){
					 $('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#password").focus(function(event){
							$('#pwValid').empty();
							 $('#password_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val()) == false || $('#password').val() == $('#username').val()){
			$('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#password").focus(function(event){
				$('#pwValid').empty();
				 $('#password_pop').show();
			});
			$("#password").blur(function(event){
				 $('#password_pop').hide();
				 if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val()) == false || $('#password').val() == $('#username').val()){
					 $('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#password").focus(function(event){
							$('#pwValid').empty();
							 $('#password_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		return result;
	},
AddEmployeeType :function(){
	$.post('employee.json','action=get-employee-types',function(obj){
		var res=obj.result.data;
		for(loop=0;loop<=res.length;loop=loop+1){
			document.form1.employeeType.options[loop]=new Option(res[loop]);
		}
	})
 
},
getOrganizationPrefeix : function() {
	/*$.post('employee.json', 'action=get-organization-prefix', function(obj) {
		var prefix = obj.result.data;
		$('#orgPrefix').val(prefix+'.');
	});*/
},

initCheckUsername: function() {
	var resFailure=false;
	var resSuccess=true;
	 $("#username").blur(function(event) {
		$('#user_pop').hide();
		var uname=$('#username').val();
		var orgPrefix = $('#orgPrefix').val();
		var pwd=$('#password').val();
			var uname=$('#username').val();
			if(uname=="")
				{
				 $('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				}
				else
					{
					var paramString='username='+orgPrefix+uname+'&action=validate-username';
					$.post('employee.json',paramString, function(data){
			            var delay = function() {
			            	UserHandler.AjaxSucceeded(data);
			            	};
			               	setTimeout(delay, 400);
					});
					
					}
		
	});
},
	
	

};