var ManageUserHandler = {
		expanded: true,
	initPageLinks : function() {
		$('#add-user').pageLink({
			container : '.manage-user-page-container',
			url : 'siteadmin/user/manage_user_list.jsp'
		});
	},
	manageUserSteps:['#manage-user-form','#manage-user-address-form'],
	manageUserUrl:['manageUser.json','manageUser.json','manageUser.json'],
	manageUserStepCount: 0,
	initAddButtons : function() {
		$('#action-add-school').click(function(){
			$('.module-title').hide();
			
			$.post('siteadmin/user/manage_user_create.jsp', '',
		        function(data){
					$('.manage-user-page-container').html(data);     
		        }
	        );
		});
		ManageUserHandler.manageUserStepCount = 0;
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    /*else if (type == 'checkbox' || type == 'radio')
			      this.checked =true;*/
			    else if (tag == 'select')
			      this.selectedIndex = 0;
			  });
			};
		$('#action-clear').click(function() {
		$('form').clear();
		});
		$('#button-next').click(function() {
			var resultSuccess=true;
			var resultFailure=false;
			if(ManageUserHandler.manageUserStepCount == 0){
				if(ManageUserHandler.validateUserStepone() == false){
					return  resultSuccess;
				}
			}else if(ManageUserHandler.manageUserStepCount == 1){
				if(ManageUserHandler.validateUserStepTwo() == false){
					return  resultSuccess;
					 
				}
			}
			var paramString = $(ManageUserHandler.manageUserSteps[ManageUserHandler.manageUserStepCount]).serialize();
			$.ajax({type: "POST",
				url:'manageUser.json', 
				data: paramString,
				success: function(data){
					$('#error-message').html('');
					$('#error-message').hide();
					$(ManageUserHandler.manageUserSteps[ManageUserHandler.manageUserStepCount]).hide();
					$(ManageUserHandler.manageUserSteps[++ManageUserHandler.manageUserStepCount]).show();
					if(ManageUserHandler.manageUserStepCount==ManageUserHandler.manageUserSteps.length) {
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
						$('#button-update-employee').show();
						$.post('siteadmin/user/manage_user_preview.jsp', 'viewType=preview',
						        function(data){
							 $('#manage-user-preview-container').css({'height' : '350px'});
								$('#manage-user-preview-container').html(data);
								$('.table-field').css({"width":"800px"});
								$('.main-table').css({"width":"400px"});
								$('.inner-table').css({"width":"400px"});
								$('.display-boxes-colored').css({"width":"140px"});
								$('.display-boxes').css({"width":"255px"});
								$('#manage-user-preview-container').show();
								ManageUserHandler.expanded=false;
									if (ManageUserHandler.manageUserStepCount==ManageUserHandler.manageUserSteps.length)
									{
								    if(!PageHandler.expanded) {
								    	$('#manage-user-preview-container').css({'height' : '350px'});
										$('#manage-user-preview-container').html(data);
										$('.table-field').css({"width":"800px"});
										$('.main-table').css({"width":"400px"});
										$('.inner-table').css({"width":"400px"});
										$('.display-boxes-colored').css({"width":"140px"});
										$('.display-boxes').css({"width":"255px"});
										$('#manage-user-preview-container').show();
										ManageUserHandler.expanded=false;
								    }
								    else{
								    	$('#manage-user-preview-container').css({'height' : '350px'});
										$('#manage-user-preview-container').html(data);
										$('.table-field').css({"width":"662px"});
										$('.main-table').css({"width":"330px"});
										$('.inner-table').css({"width":"330px"});
										$('.display-boxes-colored').css({"width":"125px"});
										$('.display-boxes').css({"width":"200px"});
										$('#manage-user-preview-container').show();
										ManageUserHandler.expanded=true;
								    }
									}
					});
					}if(ManageUserHandler.manageUserStepCount>0) {
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
			if(ManageUserHandler.manageUserStepCount==ManageUserHandler.manageUserSteps.length) {
				if(!ManageUserHandler.expanded){
					PageHandler.pageSelectionHidden =false;
					PageHandler.hidePageSelection();
					ManageUserHandler.expanded=true;
				}
				$('#button-next').show();
				$('#button-save').hide();
				$('#button-update-employee').hide();
				$('#manage-user-preview-container').html('');
				$('#manage-user-preview-container').hide();
				$('.page-buttons').css('margin-left', '150px');
			}
			$(ManageUserHandler.manageUserSteps[ManageUserHandler.manageUserStepCount]).hide();
			$(ManageUserHandler.manageUserSteps[--ManageUserHandler.manageUserStepCount]).show();
			if(ManageUserHandler.manageUserStepCount>0) {
				$('#button-prev').show();
				$('.page-buttons').css('margin-left', '150px');
			} else {
				$('#button-prev').hide();
				$('.page-buttons').css('margin-left', '240px');
			}
		});
		
		$('#button-save').click(function() {
			var resultSuccess=true;
			var paramString = 'action=save-manage-user';
			$.post('manageUser.json',paramString, function(obj) {
				$(this).successMessage({
						container : '.manage-user-page-container',
						data : obj.result.message
					});
			});
		});
		$('#button-update-employee').click(function() {
			var thisButton = $(this);
			var paramString = 'action=update-user';
			$.post('manageUser.json', paramString,
		        function(obj){
				$(this).successMessage({container:'.manage-user-page-container', data:obj.result.message});
		        }
		    );
		});
		$('#action-search').click(function(){
			ManageUserHandler.search();
			});
		$('#add-user').click(function() {
			$('.manage-user-page-container').load('siteadmin/user/manage_user_list.jsp');
		});
		
		$('#action-cancel').click(function() {
		    $('#error-message').html('You will loose unsaved data.. Clear form?');   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
		    			$('.task-page-container').html('');
		    			var container ='.manage-user-page-container';
		    			var url = "siteadmin/user/manage_user_create.jsp";
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
	},
	search : function(){
		var thisButton = $(this);
		var paramString = $('#user-search-form').serialize();
		$.post('manageUser.json', paramString,
	        function(data){
		    	var data = data.result.data;
				$('#green-results-list').html('');
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
						'<div class="result-title">' + data[loop].username +'</div>' +
						'<div class="result-body">' +
						'<span class="property">User Name</span><span class="property-value">' + data[loop].username +'</span>' +
						'</div>' +
						'<span class="property">Full Name</span><span class="property-value">' + data[loop].fullName +'</span>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">Mobile</span><span class="property-value">' + fmt(data[loop].mobile) + '</span>' +
						'</div>' +'<span class="property">Email</span><span class="property-value" style="font: bold 14px arial;width: 300px;">' + data[loop].employeeEmail + '</span>' +
						'</div>' +
						'<div class="green-result-col-action">'+
						'<div id="'+data[loop].id+'" class="ui-btn edit-icon" title="Edit User" style="margin-top:1px;"></div>' +
						'<div id="'+data[loop].id+'" class="ui-btn btn-view" title="View User Details"style="margin-top:1px;"></div>'+
						'<div id="'+data[loop].id+'" class="ui-btn delete-icon" title="Delete User"></div>' ;
				         $('#green-results-list').append(rowstr);
			};
			 ManageUserHandler.initUserListButtons();
			$('#green-results-list').jScrollPane({showArrows:true});
				} else {
					$('#green-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
				$.loadAnimation.end();
	        }
	    );
	},
	initUserListButtons : function(){
		$('.btn-view').click(function() {
			var id = $(this).attr('id');
			$.post('siteadmin/user/manage_user_profile_view.jsp', 'id='+id,
		        function(data){
				$('#user-delete-container').html(data); 
	        	$('.table-field').css({"width":"800px"});
				$('.main-table').css({"width":"400px"});
				$('.inner-table').css({"width":"400px"});
				$('.display-boxes-colored').css({"width":"140px"});
				$('.display-boxes').css({"width":"255px"});
	        	$("#user-delete-dialog").dialog({
	    			autoOpen: true,
	    			height: 455,
	    			width: 850,
	    			modal: true,
	    			buttons: {
	    			 Close: function() {
	    			      $(this).dialog('close');
	    			
	    		          }
	    			}
	        	});
		        }
	        );
		});
		$('.edit-icon').click(function() {
			var id = $(this).attr('id');
			$.post('siteadmin/user/manage_user_edit.jsp', 'id='+id,
			        function(data){
							$('.manage-user-page-container').html(data);
							$.post('manageUser.json','action=get-organizations', function(data) {
								 orgList = data.result.data;
							});
							$.post('manageUser.json', 'id='+id+'&action=get-assigned-user',
									 function(obj){
								listSelected = obj.result.data;
								var resultArray = [];
								var mergedArray = $.merge( orgList, listSelected );
								for (var i=0; i<mergedArray.length;i++) {
								   var c = mergedArray[i];
								   if ($.inArray(c, orgList)==-1 || $.inArray(c, listSelected)==-1) resultArray.push(c);
								}
								var organizationOptions = $("#organizationType");
								for(var loop=0;loop<listSelected.length;loop=loop+1){
									for(var innerLoop=loop;innerLoop<orgList.length;innerLoop=innerLoop+1){
										if(orgList[innerLoop]==listSelected[loop]){
											organizationOptions.append('<option  multiple="multiple" selected="selected" value="' + orgList[innerLoop]	+ '">' + orgList[innerLoop] + '</option>');
											break;
										}
									};
								};
								$.each(resultArray, function(i) {
									organizationOptions.append('<option multiple="multiple" value="' + resultArray[i]	+ '">' + resultArray[i] + '</option>');
								});
								
							});
			    });
			
			
			
		});
		$('.delete-icon').click(function() {
			var id = $(this).attr('id');
			$.post('siteadmin/user/manage_user_delete.jsp', 'id='+id,
		        function(data){
		        	$('#user-delete-container').html(data); 
		        	$('.table-field').css({"width":"800px"});
					$('.main-table').css({"width":"400px"});
					$('.inner-table').css({"width":"400px"});
					$('.display-boxes-colored').css({"width":"140px"});
					$('.display-boxes').css({"width":"255px"});
		        	$("#user-delete-dialog").dialog({
		    			autoOpen: true,
		    			height: 455,
		    			width: 850,
		    			modal: true,
		    			buttons: {
		    			Delete: function() {
		    				 $.post('manageUser.json', 'id='+id+'&action=delete-user',
		    						 function(obj) {
		    						$(this).successMessage({
		    							container : '.manage-user-page-container',
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
		    				$('#user-delete-container').html('');
		    			}
		    		});
		        }
		    );
		});
	},
	getOrganizations: function() {
		var organizationOptions = $("#organizationType");
		$.post('manageUser.json','action=get-organizations', function(obj) {
			$("#organizationType").html('');
			$.each(obj.result.data, function(i, organizationType) {
				organizationOptions.append('<option multiple="multiple" value="' + organizationType	+ '">' + organizationType + '</option>');
			});
		});
	},
	validateUserStepone :function(){
		var result=true;
		if(/^[a-zA-Z\s]+$/.test($('#fullName').val())==false || ($('#fullName').val()).length == 0){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fnameValid').empty();
				 $('#fname_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#fname_pop').hide();
				 if(/^[a-zA-Z\s]+$/.test($('#fullName').val())==false || ($('#fullName').val()).length == 0){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fnameValid').empty();
							 $('#fname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#fullName').val()).length > 35){
			$('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fnameValid').empty();
				 $('#dbfname_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#dbfname_pop').hide();
				 if(($('#fullName').val()).length > 35){
					 $('#fnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fnameValid').empty();
							 $('#dbfname_pop').show();
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
				$('#unValid').empty();
				 $('#username_pop').show();
			});
			$("#username").blur(function(event){
				 $('#username_pop').empty();
				 if(/^[a-zA-Z0-9\._]+$/.test($('#username').val())==false || ($('#username').val()).length > 20){
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
		var empdline = $('#directLine').val();
		var end = empdline.length;
		if((/^[0-9-+()\s]+$/.test(empdline)==false || empdline.length == 0) || empdline.charAt(0) == " " || empdline.charAt(end - 1) == " "){
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
		return result;
	},
	validateUserStepTwo :function(){
		var result =true;
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
		if($('#organizationType').val() == null){
			$('#organizationTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#organizationType").focus(function(event){
				$('#organizationTypeValid').empty();
				 $('#organizationType_pop').show();
			});
			$("#organizationType").blur(function(event){
				 $('#organizationType_pop').hide();
				 if($('#organizationType').val() == '-1'){
					 $('#organizationTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#organizationType").focus(function(event){
							$('#organizationTypeValid').empty();
							 $('#organizationType_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		return result;
	}
};