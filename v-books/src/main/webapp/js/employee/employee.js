/*var UserHandler={
},*/
var EmployeeHandler = {
		
		theme: "",
		expanded: true,
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
			    if (this.readOnly) return
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    else if (tag == 'select')
			      this.selectedIndex = 0;
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
					$(EmployeeHandler.employeeSteps[EmployeeHandler.employeeStepCount]).clear();
					$('#pValid').empty();
					$('#uValid').empty();
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
		if(UserHandler.flag==false){
			return resultSuccess;
		}
		if(EmployeeHandler.employeeStepCount == 0){
			if($("#password").length > 0){
		if(EmployeeHandler.validateEmployee()==false){
			$("#username").val('');
			$("#password").val('');
			$("#uValid").empty();
			$("#pValid").empty();
			return resultSuccess;
		}
			}else {
				if(EmployeeHandler.validateEmployeeUpdate()==false){
					return resultSuccess;
				}
			}
		}else if (EmployeeHandler.employeeStepCount == 1) {
			if(EmployeeHandler.validateEmployeeStepOne()==false){
				return resultSuccess;
			}
		}else {
			if(EmployeeHandler.validateEmployeeStepTwo()==false){
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
							$('.table-field').css({"width":"800px"});
							$('.main-table').css({"width":"400px"});
							$('.inner-table').css({"width":"400px"});
							$('.display-boxes-colored').css({"width":"140px"});
							$('.display-boxes').css({"width":"255px"});
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
	
	$('#employee-search').click(function(){
		$('.employee-page-container').load('employee/employee_search.jsp');
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
		$.post('employee.json', paramString,
	        function(obj){
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
				
	        }
        );
	});
	$('#employee-add').click(function() {
		$('.employee-page-container').load('employee/employee_add.jsp');
	});
	$('#action-cancel').click(function() {
	    $('#error-message').html('You will loose unsaved data.. Cancel form?');   
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
	    $('#error-message').html('You will loose unsaved data.. Cancel form?');   
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
			$.post('employee.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
		        }
		    );
		});
		$('#employee-search').click(function() {
			$('.employee-page-container').load('employee/employee_search.jsp');
		});
		
		$('#button-update-employee').click(function() {
			var thisButton = $(this);
			var paramString ='action=update-employee';
			PageHandler.expanded=false;
			pageSelctionButton.click();
			$.post('employee.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
		        }
		    );
		});

	},
	
	validateEmployee:function(){
		var result=true;
		if(/^[a-zA-Z]+$/.test($('#firstName').val())==false || ($('#firstName').val()).length == 0){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#firstName").focus(function(event){
				$('#fnameValid').empty();
				 $('#fname_pop').show();
			});
			$("#firstName").blur(function(event){
				 $('#fname_pop').hide();
				 if(/^[a-zA-Z]+$/.test($('#firstName').val())==false || ($('#firstName').val()).length == 0){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#firstName").focus(function(event){
							$('#fnameValid').empty();
							 $('#fname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#firstName').val()).length > 35){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#firstName").focus(function(event){
				$('#fnameValid').empty();
				 $('#dbfname_pop').show();
			});
			$("#firstName").blur(function(event){
				 $('#dbfname_pop').hide();
				 if(($('#firstName').val()).length > 35){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#firstName").focus(function(event){
							$('#fnameValid').empty();
							 $('#dbfname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9.@]+$/.test($('#middleName').val())==false && ($('#middleName').val()).length > 0){
			$('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#middleName").focus(function(event){
				$('#mnameValid').empty();
				 $('#mname_pop').show();
			});
			$("#middleName").blur(function(event){
				 $('#mname_pop').hide();
				 if(/^[a-zA-Z0-9.@]+$/.test($('#middleName').val())==false && ($('#middleName').val()).length > 0){
					 $('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#middleName").focus(function(event){
							$('#mnameValid').empty();
							 $('#mname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#middleName').val()).length > 35){
			$('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#middleName").focus(function(event){
				$('#mnameValid').empty();
				 $('#dbmname_pop').show();
			});
			$("#middleName").blur(function(event){
				 $('#dbmname_pop').hide();
				 if(($('#middleName').val()).length > 35){
					 $('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#middleName").focus(function(event){
							$('#mnameValid').empty();
							 $('#dbmname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z]+$/.test($('#lastName').val())==false || ($('#lastName').val()).length == 0){
			$('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#lastName").focus(function(event){
				$('#lnameValid').empty();
				 $('#lname_pop').show();
			});
			$("#lastName").blur(function(event){
				 $('#lname_pop').hide();
				 if(/^[a-zA-Z]+$/.test($('#lastName').val())==false || ($('#lastName').val()).length == 0){
					 $('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#lastName").focus(function(event){
							$('#lnameValid').empty();
							 $('#lname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#lastName').val()).length > 35){
			$('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#lastName").focus(function(event){
				$('#lnameValid').empty();
				 $('#dblname_pop').show();
			});
			$("#lastName").blur(function(event){
				 $('#dblname_pop').hide();
				 if(($('#lastName').val()).length > 35){
					 $('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#lastName").focus(function(event){
							$('#lnameValid').empty();
							 $('#dblname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#employeeEmail').val();
		if($('#employeeEmail').ValidateEmailAddr(Email) == false || ($('#employeeEmail').val()).length == 0){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#employeeEmail").focus(function(event){
				$('#emailValid').empty();
				 $('#email_pop').show();
			});
			$("#employeeEmail").blur(function(event){
				 $('#email_pop').hide();
				 if($('#employeeEmail').ValidateEmailAddr($('#email').val()) == false || ($('#employeeEmail').val()).length == 0){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#employeeEmail").focus(function(event){
							$('#emailValid').empty();
							 $('#email_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#employeeEmail').val()).length > 100){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#employeeEmail").focus(function(event){
				$('#emailValid').empty();
				 $('#dbemail_pop').show();
			});
			$("#employeeEmail").blur(function(event){
				 $('#dbemail_pop').hide();
				 if(($('#employeeEmail').val()).length > 100){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#employeeEmail").focus(function(event){
							$('#emailValid').empty();
							 $('#dbemail_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9\._]+$/.test($('#username').val())==false || ($('#username').val()).length > 20){
			$('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#username").focus(function(event){
				$('#username_popDuplicate').hide();
				$('#unValid').empty();
				 $('#username_pop').show();
			});
			$("#username").blur(function(event){
				 $('#username_pop').empty();
				 if(/^[a-zA-Z0-9\._]+$/.test($('#username').val())==false || ($('#username').val()).length > 20){
					 $('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#username").focus(function(event){
						 $('#username_popDuplicate').hide();
							$('#unValid').empty();
							 $('#username_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#username').val()).length == 0){
			$('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#username").focus(function(event){
				$('#unValid').empty();
				 $('#username_pop').show();
			});
			$("#username").blur(function(event){
				 $('#username_pop').empty();
				 if(($('#username').val()).length == 0){
					 $('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#username").focus(function(event){
							$('#unValid').empty();
							 $('#username_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#password').val()).length == 0){
			$('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#password").focus(function(event){
				$('#pwValid').empty();
				 $('#password_pop').show();
			});
			$("#password").blur(function(event){
				 $('#password_pop').empty();
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
		if($('#employeeType').val() == '-1'){
		$('#employeeTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#employeeType").focus(function(event){
			$('#employeeTypeValid').empty();
			 $('#employeeType_pop').show();
		});
		$("#employeeType").blur(function(event){
			 $('#employeeType_pop').hide();
			 if($('#employeeType').val() == '-1'){
				 $('#employeeTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#employeeType").focus(function(event){
						$('#employeeTypeValid').empty();
						 $('#employeeType_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}
		return result;
	},
	
	validateEmployeeUpdate:function(){
		var result=true;
		
		if(/^[a-zA-Z]+$/.test($('#firstName').val())==false || ($('#firstName').val()).length == 0){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#firstName").focus(function(event){
				$('#fnameValid').empty();
				 $('#fname_pop').show();
			});
			$("#firstName").blur(function(event){
				 $('#fname_pop').hide();
				 if(/^[a-zA-Z]+$/.test($('#firstName').val())==false || ($('#firstName').val()).length == 0){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#firstName").focus(function(event){
							$('#fnameValid').empty();
							 $('#fname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#firstName').val()).length > 35){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#firstName").focus(function(event){
				$('#fnameValid').empty();
				 $('#dbfname_pop').show();
			});
			$("#firstName").blur(function(event){
				 $('#dbfname_pop').hide();
				 if(($('#firstName').val()).length > 35){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#firstName").focus(function(event){
							$('#fnameValid').empty();
							 $('#dbfname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9.@]+$/.test($('#middleName').val())==false && ($('#middleName').val()).length > 0){
			$('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#middleName").focus(function(event){
				$('#mnameValid').empty();
				 $('#mname_pop').show();
			});
			$("#middleName").blur(function(event){
				 $('#mname_pop').hide();
				 if(/^[a-zA-Z0-9.@]+$/.test($('#middleName').val())==false && ($('#middleName').val()).length > 0){
					 $('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#middleName").focus(function(event){
							$('#mnameValid').empty();
							 $('#mname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#middleName').val()).length > 35){
			$('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#middleName").focus(function(event){
				$('#mnameValid').empty();
				 $('#dbmname_pop').show();
			});
			$("#middleName").blur(function(event){
				 $('#dbmname_pop').hide();
				 if(($('#middleName').val()).length > 35){
					 $('#mnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#middleName").focus(function(event){
							$('#mnameValid').empty();
							 $('#dbmname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z]+$/.test($('#lastName').val())==false || ($('#lastName').val()).length == 0){
			$('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#lastName").focus(function(event){
				$('#lnameValid').empty();
				 $('#lname_pop').show();
			});
			$("#lastName").blur(function(event){
				 $('#lname_pop').hide();
				 if(/^[a-zA-Z]+$/.test($('#lastName').val())==false || ($('#lastName').val()).length == 0){
					 $('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#lastName").focus(function(event){
							$('#lnameValid').empty();
							 $('#lname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#lastName').val()).length > 35){
			$('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#lastName").focus(function(event){
				$('#lnameValid').empty();
				 $('#dblname_pop').show();
			});
			$("#lastName").blur(function(event){
				 $('#dblname_pop').hide();
				 if(($('#lastName').val()).length > 35){
					 $('#lnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#lastName").focus(function(event){
							$('#lnameValid').empty();
							 $('#dblname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#employeeEmail').val();
		if($('#employeeEmail').ValidateEmailAddr(Email) == false || ($('#employeeEmail').val()).length == 0){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#employeeEmail").focus(function(event){
				$('#emailValid').empty();
				 $('#email_pop').show();
			});
			$("#employeeEmail").blur(function(event){
				 $('#email_pop').hide();
				 if($('#employeeEmail').ValidateEmailAddr($('#email').val()) == false || ($('#employeeEmail').val()).length == 0){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#employeeEmail").focus(function(event){
							$('#emailValid').empty();
							 $('#email_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#employeeEmail').val()).length > 100){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#employeeEmail").focus(function(event){
				$('#emailValid').empty();
				 $('#dbemail_pop').show();
			});
			$("#employeeEmail").blur(function(event){
				 $('#dbemail_pop').hide();
				 if(($('#employeeEmail').val()).length > 100){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#employeeEmail").focus(function(event){
							$('#emailValid').empty();
							 $('#dbemail_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if($('#employeeType').val() == '-1'){
		$('#employeeTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
		$("#employeeType").focus(function(event){
			$('#employeeTypeValid').empty();
			 $('#employeeType_pop').show();
		});
		$("#employeeType").blur(function(event){
			 $('#employeeType_pop').hide();
			 if($('#employeeType').val() == '-1'){
				 $('#employeeTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				 $("#employeeType").focus(function(event){
						$('#employeeTypeValid').empty();
						 $('#employeeType_pop').show();
					});
			 }else{
			 }
		});
		result=false;
	}
		return result;
	},
	
	validateEmployeeStepOne: function(){
		var result=true;
		var empmobile = $('#mobile').val();
		var end = empmobile.length;
		if(/^[0-9-+()\s]+$/.test(empmobile)==false || empmobile.length == 0 || empmobile.charAt(0) == " " || empmobile.charAt(end - 1) == " "){
			$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#mobile").focus(function(event){
				$('#mobileValid').empty();
				 $('#mobile_pop').show();
			});
			$("#mobile").blur(function(event){
				 $('#mobile_pop').hide();
				 var empmobile = $('#mobile').val();
				 var end = empmobile.length;
				 if(/^[0-9-+()\s]+$/.test(empmobile)==false || empmobile.length == 0 || empmobile.charAt(0) == " " || empmobile.charAt(end - 1) == " "){
					 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#mobile").focus(function(event){
							$('#mobileValid').empty();
							 $('#mobile_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#mobile').val()).length > 60){
			$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#mobile").focus(function(event){
				$('#mobileValid').empty();
				 $('#dbmobile_pop').show();
			});
			$("#mobile").blur(function(event){
				 $('#dbmobile_pop').hide();
				 if(($('#mobile').val()).length > 60){
					 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#mobile").focus(function(event){
							$('#mobileValid').empty();
							 $('#dbmobile_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empdline = $('#directLine').val();
		var end = empdline.length;
		if((/^[0-9-+()\s]+$/.test(empdline)==false && empdline.length > 0) || empdline.charAt(0) == " " || empdline.charAt(end - 1) == " "){
			$('#dlineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#directLine").focus(function(event){
				$('#dlineValid').empty();
				 $('#dline_pop').show();
			});
			$("#directLine").blur(function(event){
				 $('#dline_pop').hide();
				 var empdline = $('#directLine').val();
				 var end = empdline.length;
				 if((/^[0-9-+()\s]+$/.test(empdline)==false && empdline.length > 0) || empdline.charAt(0) == " " || empdline.charAt(end - 1) == " "){
					 $('#dlineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#directLine").focus(function(event){
							$('#dlineValid').empty();
							 $('#dline_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#directLine').val()).length > 60){
			$('#dlineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#directLine").focus(function(event){
				$('#dlineValid').empty();
				 $('#dbdline_pop').show();
			});
			$("#directLine").blur(function(event){
				 $('#dbdline_pop').hide();
				 if(($('#directLine').val()).length > 60){
					 $('#dlineValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#directLine").focus(function(event){
							$('#dlineValid').empty();
							 $('#dbdline_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empalter = $('#alternateMobile').val();
		var end = empalter.length;
		if((/^[0-9-+()\s]+$/.test(empalter)==false && empalter.length > 0) || empalter.charAt(0) == " " || empalter.charAt(end - 1) == " "){
			$('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#alternateMobile").focus(function(event){
				$('#alternateValid').empty();
				 $('#alternate_pop').show();
			});
			$("#alternateMobile").blur(function(event){
				 $('#alternate_pop').hide();
				 var empalter = $('#alternateMobile').val();
				 var end = empalter.length;
				 if((/^[0-9-+()\s]+$/.test(empalter)==false && empalter.length > 0) || empalter.charAt(0) == " " || empalter.charAt(end - 1) == " "){
					 $('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#alternateMobile").focus(function(event){
							$('#alternateValid').empty();
							 $('#alternate_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#alternateMobile').val()).length > 60 ){
			$('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#alternateMobile").focus(function(event){
				$('#alternateValid').empty();
				 $('#dbalternate_pop').show();
			});
			$("#alternateMobile").blur(function(event){
				 $('#dbalternate_pop').hide();
				 if(($('#alternateMobile').val()).length > 60){
					 $('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#alternateMobile").focus(function(event){
							$('#alternateValid').empty();
							 $('#dbalternate_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^(A|B|AB|O)[+-]+$/.test($('#bloodGroup').val())==false || ($('#bloodGroup').val()).length == 0){
			$('#bgroupValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#bloodGroup").focus(function(event){
				$('#bgroupValid').empty();
				 $('#bgroup_pop').show();
			});
			$("#bloodGroup").blur(function(event){
				 $('#bgroup_pop').hide();
				 if(/^(A|B|AB|O)[+-]+$/.test($('#bloodGroup').val())==false || ($('#bloodGroup').val()).length == 0){
					 $('#bgroupValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#bloodGroup").focus(function(event){
							$('#bgroupValid').empty();
							 $('#bgroup_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#bloodGroup').val()).length > 5){
			$('#bgroupValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#bloodGroup").focus(function(event){
				$('#bgroupValid').empty();
				 $('#dbbgroup_pop').show();
			});
			$("#bloodGroup").blur(function(event){
				 $('#dbbgroup_pop').hide();
				 if(($('#bloodGroup').val()).length > 5){
					 $('#bgroupValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#bloodGroup").focus(function(event){
							$('#bgroupValid').empty();
							 $('#dbbgroup_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9]+$/.test($('#passPortNumber').val())==false || ($('#passPortNumber').val()).length == 0){
			$('#passportValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#passPortNumber").focus(function(event){
				$('#passportValid').empty();
				 $('#passport_pop').show();
			});
			$("#passPortNumber").blur(function(event){
				 $('#passport_pop').hide();
				 if(/^[a-zA-Z0-9]+$/.test($('#passPortNumber').val())==false || ($('#passPortNumber').val()).length == 0){
					 $('#passportValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#passPortNumber").focus(function(event){
							$('#passportValid').empty();
							 $('#passport_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#passPortNumber').val()).length > 50){
			$('#passportValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#passPortNumber").focus(function(event){
				$('#passportValid').empty();
				 $('#dbpassport_pop').show();
			});
			$("#passPortNumber").blur(function(event){
				 $('#dbpassport_pop').hide();
				 if(($('#passPortNumber').val()).length > 50){
					 $('#passportValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#passPortNumber").focus(function(event){
							$('#passportValid').empty();
							 $('#dbpassport_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empnation = $('#nationality').val();
		var end = empnation.length;
		if(/^[a-zA-Z\s]+$/.test(empnation)==false || empnation.length == 0 || empnation.charAt(0) == " " || empnation.charAt(end - 1) == " "){
			$('#nationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#nationality").focus(function(event){
				$('#nationValid').empty();
				 $('#nation_pop').show();
			});
			$("#nationality").blur(function(event){
				 $('#nation_pop').hide();
				 var empnation = $('#nationality').val();
				 var end = empnation.length;
				 if(/^[a-zA-Z\s]+$/.test(empnation)==false || empnation.length == 0 || empnation.charAt(0) == " " || empnation.charAt(end - 1) == " "){
					 $('#nationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#nationality").focus(function(event){
							$('#nationValid').empty();
							 $('#nation_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#nationality').val()).length > 20){
			$('#nationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#nationality").focus(function(event){
				$('#nationValid').empty();
				 $('#dbnation_pop').show();
			});
			$("#nationality").blur(function(event){
				 $('#dbnation_pop').hide();
				 if(($('#nationality').val()).length > 20){
					 $('#nationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#nationality").focus(function(event){
							$('#nationValid').empty();
							 $('#dbnation_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		return result;
	},
	
	validateEmployeeStepTwo: function(){
		var result=true;
		var empaddr1 = $('#addressLine1').val();
		var end = empaddr1.length;
		if(empaddr1.length == 0 || empaddr1.charAt(0) == " " || empaddr1.charAt(end - 1) == " "){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#addressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#addressLine1_pop').hide();
				 var empaddr1 = $('#addressLine1').val();
				 var end = empaddr1.length;
				 if(empaddr1.length == 0 || empaddr1.charAt(0) == " " || empaddr1.charAt(end - 1) == " "){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#addressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#addressLine1').val()).length > 200){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#dbaddressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#dbaddressLine1_pop').hide();
				 if(($('#addressLine1').val()).length > 200){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#dbaddressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empaddr2 = $('#addressLine2').val();
		var end = empaddr2.length;
		if(empaddr2.length > 200 || empaddr2.charAt(0) == " " || empaddr2.charAt(end - 1) == " "){
			$('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine2").focus(function(event){
				$('#addressLine2Valid').empty();
				 $('#dbaddressLine2_pop').show();
			});
			$("#addressLine2").blur(function(event){
				 $('#dbaddressLine2_pop').hide();
				 var empaddr2 = $('#addressLine2').val();
				 var end = empaddr2.length;
				 if(empaddr2.length > 200 || empaddr2.charAt(0) == " " || empaddr2.charAt(end - 1) == " "){
					 $('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine2").focus(function(event){
							$('#addressLine2Valid').empty();
							 $('#dbaddressLine2_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false && ($('#landmark').val()).length > 0){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#landmark_pop').show();
			});
			$("#landmark").blur(function(event){
				 $('#landmark_pop').hide();
				 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val())==false && ($('#landmark').val()).length > 0){
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
		var empland = $('#landmark').val();
		var end = empland.length;
		if(empland.length > 60 || empland.charAt(0) == " " || empland.charAt(end - 1) == " "){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#dblandmark_pop').show();
			});
			$("#landmark").blur(function(event){
				 $('#dblandmark_pop').hide();
				 var empland = $('#landmark').val();
				 var end = empland.length;
				 if(empland.length > 60 || empland.charAt(0) == " " || empland.charAt(end - 1) == " "){
					 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#landmark").focus(function(event){
							$('#landmarkValid').empty();
							 $('#dblandmark_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var emploc = $('#locality').val();
		var end = emploc.length;
		if(/^[a-zA-Z0-9\s]+$/.test(emploc)==false || emploc.length == 0 || emploc.charAt(0) == " " || emploc.charAt(end - 1) == " "){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#locality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#locality_pop').hide();
				 var emploc = $('#locality').val();
				 var end = emploc.length;
				 if(/^[a-zA-Z0-9\s]+$/.test(emploc)==false || emploc.length == 0 || emploc.charAt(0) == " " || emploc.charAt(end - 1) == " "){
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
		if(($('#locality').val()).length > 60){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#dblocality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#dblocality_pop').hide();
				 if(($('#locality').val()).length > 60){
					 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#localityValid').empty();
							 $('#dblocality_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empcity = $('#city').val();
		var end = empcity.length;
		if(/^[a-zA-Z\s]+$/.test(empcity)==false || empcity.length == 0 || empcity.charAt(0) == " " || empcity.charAt(end - 1) == " "){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#city_pop').show();
			});
			$("#city").blur(function(event){
				 $('#city_pop').hide();
				 var empcity = $('#city').val();
				 var end = empcity.length;
				 if(/^[a-zA-Z\s]+$/.test(empcity)==false || empcity.length == 0 || empcity.charAt(0) == " " || empcity.charAt(end - 1) == " "){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#city_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#city').val()).length > 50){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#dbcity_pop').show();
			});
			$("#city").blur(function(event){
				 $('#dbcity_pop').hide();
				 if(($('#city').val()).length > 50){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#dbcity_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var empstate = $('#state').val();
		var end = empstate.length;
		if(/^[a-zA-Z\s]+$/.test(empstate) == false || empstate.length == 0 || empstate.charAt(0) == " " || empstate.charAt(end - 1) == " "){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#state_pop').show();
			});
			$("#state").blur(function(event){
				 $('#state_pop').hide();
				 var empstate = $('#state').val();
				 var end = empstate.length;
				 if(/^[a-zA-Z\s]+$/.test(empstate) == false || empstate.length == 0 || empstate.charAt(0) == " " || empstate.charAt(end - 1) == " "){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#state_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		if(($('#state').val()).length > 50){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#dbstate_pop').show();
			});
			$("#state").blur(function(event){
				 $('#dbstate_pop').hide();
				 if(($('#state').val()).length > 50){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#dbstate_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		var empzip = $('#zipcode').val();
		var end = empzip.length;
		if(/^[a-zA-Z0-9-\s]+$/.test(empzip) == false || empzip.length > 9 || empzip.charAt(0) == " " || empzip.charAt(end - 1) == " "){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 var empzip = $('#zipcode').val();
				 var end = empzip.length;
				 if(/^[a-zA-Z0-9-\s]+$/.test(empzip) == false || empzip.length > 9 || empzip.charAt(0) == " " || empzip.charAt(end - 1) == " "){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#zipcode').val()).length == 0 ){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 if(($('#zipcode').val()).length == 0 ){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if($('#addressType').val() == '-1'){
			$('#addressTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressType").focus(function(event){
				$('#addressTypeValid').empty();
				 $('#addressType_pop').show();
			});
			$("#addressType").blur(function(event){
				 $('#addressType_pop').hide();
				 if($('#addressType').val() == '-1'){
					 $('#addressTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressType").focus(function(event){
							$('#addressTypeValid').empty();
							 $('#addressType_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		return result;
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
	/*var thisButton = $(this);*/
	var paramString = $('#employee-search-form').serialize();  
	$.post('employee.json', paramString,
        function(obj){
	    	var data = obj.result.data;
			$('#search-results-list').html('');
			if(data != undefined) {
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1) {
					if(alternate) {
						var rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-title">' + data[loop].firstName +'</div>' +
					'<div class="result-body">' +
					'<span class="property">'+ Msg.EMPLOYEE_USERNAME +'</span><span class="property-value">' + data[loop].username +'</span>' +
					
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">'+Msg.EMPLOYEE_PHONE_LABEL+' </span><span class="property-value">' + fmt(data[loop].mobile) + '</span>' +
					'</div>' +'<span class="property">'+Msg.EMPLOYEE_EMPLOYEE_TYPE_LABEL+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].employeeTypeByString + '</span>' +
					'</div>' +
					'<div class="green-result-col-action">';
					rowstr = rowstr +'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Employee" style="margin-top:1px;"></div>' +
					'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Employee Details"style="margin-top:1px;"></div>'+
					'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Employee"></div>' ;
			         $('#search-results-list').append(rowstr);
		};
		EmployeeHandler.initSearchResultButtons();
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
		$('#ps-exp-col').click(function() {
			setTimeout(function() {
				$('#search-results-list').jScrollPaneRemove();
				$('#search-results-list').jScrollPane({showArrows:true});
            }, 0);
		});
		//button click - cancel
		$('#action-cancel').click(function() {
			
			$('#employee-search').click();		
		});
		$('#action-clear').click(function() {
			$('#employee-search-form').clearForm();
			$('.employee-page-container').load('employee/employee_search.jsp');
			/*$('#search-results-list').html('<tr><td colspan="4">Search Results will be show here</td></tr>');
			setTimeout(function(){
				$('#search-results-list').jScrollPane({showArrows:true});
			},0);*/
		});
		
		//button click - search
		$('#action-search-employee').click(function() {
			var thisButton = $(this);
			var paramString = $('#employee-search-form').serialize();  
			$('#search-results-list').ajaxLoader();
			  //$('form').clearForm();
			$.post('employee.json', paramString,
		        function(obj){
			    	var data = obj.result.data;
					$('#search-results-list').html('');
					if(data != undefined) {
						var alternate = false;

						for(var loop=0;loop<data.length;loop=loop+1) {
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate">';
							} else {
								rowstr = '<div class="green-result-row">';
							}
							alternate = !alternate;
							rowstr = rowstr + '<div class="green-result-col-1">'+
							'<div class="result-title">' + data[loop].firstName +'</div>' +
							'<div class="result-body">' +
							'<span class="property">'+ Msg.EMPLOYEE_USERNAME +'</span><span class="property-value">' + data[loop].username +'</span>' +
							
							'</div>' +
							'</div>' +
							'<div class="green-result-col-2">'+
							'<div class="result-body">' +
							'<span class="property">'+Msg.EMPLOYEE_PHONE_LABEL+' </span><span class="property-value">' + fmt(data[loop].mobile) + '</span>' +
							'</div>' +'<span class="property">'+Msg.EMPLOYEE_EMPLOYEE_TYPE_LABEL+' </span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].employeeTypeByString + '</span>' +
							'</div>' +
							'<div class="green-result-col-action">';
							rowstr = rowstr +'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit Employee" style="margin-top:1px;"></div>' +
							'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View Employee Details"style="margin-top:1px;"></div>'+
							'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete Employee"></div>' ;
					         $('#search-results-list').append(rowstr);
				};
				EmployeeHandler.initSearchResultButtons();
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
		
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('employee/employee_edit.jsp', 'id='+id,
		        function(data){
						$('.employee-page-container').html(data);
		        	
		        });
		});
		$('.delete-icon').click(function() {
			var id = $(this).attr('id');
			$.post('employee/employee_preview_delete.jsp', 'id='+id,
		        function(data){
		        	$('#employee-delete-container').html(data); 
		        	$('.table-field').css({"width":"800px"});
					$('.main-table').css({"width":"400px"});
					$('.inner-table').css({"width":"400px"});
					$('.display-boxes-colored').css({"width":"140px"});
					$('.display-boxes').css({"width":"255px"});
		        	$("#employee-delete-dialog").dialog({
		    			autoOpen: true,
		    			height: 455,
		    			width: 850,
		    			modal: true,
		    			buttons: {
		    			Delete: function() {
		    				 $.post('employee.json', 'id='+id+'&action=delete-employee',
		    						 function(obj) {
		    						$(this).successMessage({
		    							container : '.employee-page-container',
		    							data : obj.result.message
		    						});
		    					});
		    				 $(this).dialog('close');
		    				},
		    		      Close: function() {
		    			      $(this).dialog('close');
		    			
		    		          }
		    			},
		    			close: function() {
		    				$('#employee-delete-container').html('');
		    			}
		    		});
		        }
		    );
		});

		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('employee/employee_profile_view.jsp', 'id='+id,
		        function(data){
				$('#employee-view-container').html(data);
				$('.table-field').css({"width":"800px"});
				$('.main-table').css({"width":"400px"});
				$('.inner-table').css({"width":"400px"});
				$('.display-boxes-colored').css({"width":"140px"});
				$('.display-boxes').css({"width":"255px"});
					$("#employee-view-dialog").dialog('open');
		        }
	        );
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
	
AddEmployeeType :function(){
	$.post('employee.json','action=get-employee-types',function(obj){
		var res=obj.result.data;
		for(loop=0;loop<res.length;loop=loop+1){
			if(res[loop]!='User'&& res[loop] !='Site Administrator'){
				 document.form1.employeeType.options[loop]=new Option(res[loop]);
			}else if(res[loop] =='User'&& res[loop] =='Site Administrator'){
				
			}
		}
		
	})
 
}
	
	

};