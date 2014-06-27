var ValidateEmployeeHandler = {
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
			
			if($('#lastName').hasClass('mandatory')){
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
			}
			
			//validation for granted_days field when employee type will be Sales Executive
			if($('#employeeType').val()=="SLE"){
			var grantedDays = $('#grantedDays').val();
			var grantedDaysEnd = grantedDays.length;
			if(/^[0-9\s]+$/.test(grantedDays)==false || grantedDays.length == 0 || grantedDays.charAt(0) == " " || grantedDays.charAt(grantedDaysEnd - 1) == " "){
				$('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayValid').empty();
					 $('#txnViewDayValid_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#txnViewDayValid_pop').hide();
					 var grantedDays = $('#grantedDays').val();
					 var grantedDaysEnd = grantedDays.length;
					 if(/^[0-9\s]+$/.test(grantedDays)==false || grantedDays.length == 0 || grantedDays.charAt(0) == " " || grantedDays.charAt(grantedDaysEnd - 1) == " "){
						 $('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayValid').empty();
								 $('#txnViewDayValid_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
			if(($('#grantedDays').val()).length > 3){
				$('#txnViewDayLengthValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayLengthValid').empty();
					 $('#dbtxnViewDayValid_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#dbtxnViewDayValid_pop').hide();
					 if(($('#grantedDays').val()).length > 3){
						 $('#txnViewDayLengthValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayLengthValid').empty();
								 $('#dbtxnViewDayValid_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
			//validate max and min days for SE Granted_Days
			var grantedMaxMinDays = $('#grantedDays').val();
			if(grantedMaxMinDays > 365 || grantedMaxMinDays < 1 ){
				$('#txnViewDayMaxMinDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayMaxMinDaysValid').empty();
					 $('#dbtxnMaxMinDays_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#dbtxnMaxMinDays_pop').hide();
					 if(grantedMaxMinDays > 365 || grantedMaxMinDays < 1){
						 $('#txnViewDayMaxMinDaysValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayMaxMinDaysValid').empty();
								 $('#dbtxnMaxMinDays_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
		}//if employee is sales executive
			/*------------------------------*/
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
			if($('#lastName').hasClass('mandatory')){
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
				
				if($('#lastName').hasClass('mandatory')){
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
				}
			}
			
			//validation for granted_days field when employee type will be Sales Executive
			if($('#employeeType').val()=="SLE"){
			var grantedDays = $('#grantedDays').val();
			var grantedDaysEnd = grantedDays.length;
			if(/^[0-9\s]+$/.test(grantedDays)==false || grantedDays.length == 0 || grantedDays.charAt(0) == " " || grantedDays.charAt(grantedDaysEnd - 1) == " "){
				$('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayValid').empty();
					 $('#txnViewDayValid_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#txnViewDayValid_pop').hide();
					 var grantedDays = $('#grantedDays').val();
					 var grantedDaysEnd = grantedDays.length;
					 if(/^[0-9\s]+$/.test(grantedDays)==false || grantedDays.length == 0 || grantedDays.charAt(0) == " " || grantedDays.charAt(grantedDaysEnd - 1) == " "){
						 $('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayValid').empty();
								 $('#txnViewDayValid_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
			if(($('#grantedDays').val()).length > 3){
				$('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayValid').empty();
					 $('#dbtxnViewDayValid_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#dbtxnViewDayValid_pop').hide();
					 if(($('#grantedDays').val()).length > 3){
						 $('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayValid').empty();
								 $('#dbtxnViewDayValid_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
			//validate max and min days for SE Granted_Days
			var grantedMaxMinDays = $('#grantedDays').val();
			if(grantedMaxMinDays > 365 || grantedMaxMinDays < 1 ){
				$('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#grantedDays").focus(function(event){
					$('#txnViewDayValid').empty();
					 $('#dbtxnMaxMinDays_pop').show();
				});
				$("#grantedDays").blur(function(event){
					 $('#dbtxnMaxMinDays_pop').hide();
					 if(grantedMaxMinDays > 365 || grantedMaxMinDays < 1){
						 $('#txnViewDayValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#grantedDays").focus(function(event){
								$('#txnViewDayValid').empty();
								 $('#dbtxnMaxMinDays_pop').show();
							});
					 }else{
					 }
				});
				result=false;
			}
			
		}//if employee is sales executive
			/*------------------------------*/
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
			if($('#bloodGroup').hasClass('mandatory')){
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
			}
			if($('#passPortNumber').hasClass('mandatory')){
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
			var addressTypeId ;
			if($('#addressTypeEdit').length == 0){
				 addressTypeId = 'addressType';
			}else{
				 addressTypeId  ='addressTypeEdit';
			}
			if($('#'+addressTypeId).val() == '-1' ||$('#'+addressTypeId).val() == null){
				$('#addressTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#'+addressTypeId).focus(function(event){
					$('#addressTypeValid').empty();
					 $('#addressType_pop').show();
				});
				$('#'+addressTypeId).blur(function(event){
					 $('#addressType_pop').hide();
					 if($('#'+addressTypeId).val() == '-1'||$('#'+addressTypeId).val() == null){
						 $('#addressTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#'+addressTypeId).focus(function(event){
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
};