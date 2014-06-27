var ValidateDeliveryNoteHandler = {
		
		validateChequeDetails : function(){
			var bankNameLen = $('#bankName').val().length;
			var branchNameLen = $('#branchName').val().length;
			var presentPaymentLen = $('#presentPayment').val().length;
			var result =true;
			if(/^[a-zA-Z\s]+$/.test($('#bankName').val())==false||$('#bankName').val().length == 0||$('#bankName').val().charAt(0)==" "||$('#bankName').val().charAt(bankNameLen-1)==" "){
				$('#bankNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#bankName").focus(function(event){
					$('#bankNameValid').empty();
					 $('#bankName_pop').show();
				});
				$("#bankName").blur(function(event){
					 $('#bankName_pop').hide();
					 if(/^[a-zA-Z.\s]+$/.test($('#bankName').val())==false||$('#bankName').val().length== 0||$('#bankName').val().charAt(0)==" "||$('#bankName').val().charAt(citylen-1)==" "){
						 $('#bankNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#bankName").focus(function(event){
							 $('#bankNameValid').empty();
							 $('#bankName_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			if(/^[a-zA-Z.\s]+$/.test($('#branchName').val())==false && $('#branchName').val().length> 0||$('#branchName').val().charAt(0)==" "||$('#branchName').val().charAt(branchName-1)==" "){
				$('#branchnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#branchName").focus(function(event){
					$('#branchnameValid').empty();
					 $('#branchname_pop').show();
				});
				$("#branchName").blur(function(event){
					 $('#branchname_pop').hide();
					 if(/^[a-zA-Z.\s]+$/.test($('#branchName').val())==false&&$('#branchName').val().length>0||$('#branchName').val().charAt(0)==" "||$('#branchName').val().charAt(citylen-1)==" "){
						 $('#branchnameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#branchName").focus(function(event){
							 $('#branchnameValid').empty();
							 $('#branchname_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			var bankLocationLen = $('#bankLocation').val().length;
			if(/^[a-zA-Z.\s]+$/.test($('#bankLocation').val())==false|| $('#bankLocation').val().length == 0||$('#bankLocation').val().charAt(0)==" "||$('#bankLocation').val().charAt(bankLocation-1)==" "){
				$('#bankLocationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#bankLocation").focus(function(event){
					$('#bankLocationValid').empty();
					 $('#bankLocation_pop').show();
				});
				$("#bankLocation").blur(function(event){
					 $('#bankLocation_pop').hide();
					 if(/^[a-zA-Z.\s]+$/.test($('#bankLocation').val())==false || $('#bankLocation').val().length ==0||$('#bankLocation').val().charAt(0)==" "||$('#branchName').val().charAt(citylen-1)==" "){
						 $('#bankLocationValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#bankLocation").focus(function(event){
							 $('#bankLocationValid').empty();
							 $('#bankLocation_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			var chequeLen = $('#chequeNo').val().length;
			if(/^[0-9a-zA-Z\s]+$/.test($('#chequeNo').val())==false|| $('#chequeNo').val().length == 0||$('#chequeNo').val().charAt(0)==" "||$('#chequeNo').val().charAt(chequeLen-1)==" "){
				$('#chequeNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#chequeNo").focus(function(event){
					$('#chequeNoValid').empty();
					 $('#chequeNo_pop').show();
				});
				$("#chequeNo").blur(function(event){
					 $('#chequeNo_pop').hide();
					 if(/^[0-9a-zA-Z\s]+$/.test($('#chequeNo').val())==false || $('#chequeNo').val().length ==0||$('#chequeNo').val().charAt(0)==" "||$('#chequeNo').val().charAt(chequeNo-1)==" "){
						 $('#chequeNoValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#chequeNo").focus(function(event){
							 $('#chequeNoValid').empty();
							 $('#chequeNo_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length == 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(bankNameLen-1)==" "){
				$('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#presentPayment").focus(function(event){
					$('#presentPaymentValid').empty();
					 $('#presentPayment_pop').show();
				});
				$("#presentPayment").blur(function(event){
					 $('#presentPayment_pop').hide();
					 if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length== 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(citylen-1)==" "){
						 $('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#presentPayment").focus(function(event){
							 $('#presentPaymenteValid').empty();
							 $('#presentPayment_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			return result;
		},
		validatePaymnetFields : function(){
			var result = true;
			if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length == 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt($('#presentPayment').val().length -1)==" "){
				$('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			    // $('#city').focus();
				$("#presentPayment").focus(function(event){
					$('#presentPaymentValid').empty();
					 $('#presentPayment_pop').show();
				});
				$("#presentPayment").blur(function(event){
					 $('#presentPayment_pop').hide();
					 if(/^[0-9.,\s]+$/.test($('#presentPayment').val())==false||$('#presentPayment').val().length== 0||$('#presentPayment').val().charAt(0)==" "||$('#presentPayment').val().charAt(citylen-1)==" "){
						 $('#presentPaymentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#presentPayment").focus(function(event){
							 $('#presentPaymenteValid').empty();
							 $('#presentPayment_pop').show();
						 });
						
					 }
				});
				result = false;
			}
			if(currencyHandler.convertStringPatternToFloat($('#presentPayment').val()) > 0){
				if($('#paymentType').val() == 'NA'||$('#paymentType').val() == '-1'||$('#paymentType').val() == ''){
					$('#paymentTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#paymentType").focus(function(event){
						$('#paymentTypeValid').empty();
						 $('#paymentType_pop').show();
					});
					$("#paymentType").blur(function(event){
						$('#paymentType_pop').hide();
						if($('#presentPayment').val() > 0){
						if($('#paymentType').val() == 'NA'||$('#paymentType').val() == '-1'||$('#paymentType').val() == ''){
							 $('#paymentTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#paymentType").focus(function(event){
								 $('#paymentTypeValid').empty();
								 $('#paymentType_pop').show();
							 });
						 }
						}else{
							 $('#paymentTypeValid').empty();
						}
					});
					result = false;
				}
			}
			return result;
		},
		validatein :function(){
			var result=true;
			var invEnd=$('#invoiceName').val().length;
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
			return result;
		},
};
var ValidateJournalHandler = {
		validateJournals : function(){
			var result = true;
			if($('#businessName').val().length==0){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#businessName").focus(function(event){
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
				});
				$("#businessName").blur(function(event){
					 $('#businessName_pop').hide();
					 if($('#businessName').val().length==0){
						 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#businessName").focus(function(event){
							 $('#businessNameValid').empty();
							 if($('#businessName').val().length==0){
								 $('#businessName_pop').show();
							 }
							});
					 }else{
						 $('#businessName_pop').hide();
					 }
				});
				result =false; 
			}else{
				 $('#businessName_pop').hide();
			}
			if( $('#journalType').val()=='-1'||$('#journalType').val()==null){
				$('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#journalType").focus(function(event){
					$('#typeValid').empty();
					 $('#Type_pop').show();
				});
				$("#journalType").blur(function(event){
					 $('#Type_pop').hide();
					 if($('#journalType').val() == '-1'||$('#journalType').val()==null){
						 $('#typeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#journalType").focus(function(event){
							 $('#typeValid').empty();
							 if($('#journalType').val() == '-1'){
							 $('#Type_pop').show();
						 }
							});
					 }else{
						 $('#Type_pop').hide();
					 }
				});
				result =false; 
			}
			if($('#description').val()==""){
				$('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#description").focus(function(event){
					$('#descValid').empty();
					 $('#desc_pop').show();
				});
				$("#description").blur(function(event){
					 $('#desc_pop').hide();
					 if($('#description').val().length==0){
						 $('#descValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#description").focus(function(event){
							 $('#descValid').empty();
							 if($('#description').val().length==0){
								 $('#desc_pop').show();
							 }
							});
					 }else{
						 $('#desc_pop').hide();
					 }
				});
				result =false; 
			}
			if($('#amount').val().length == 0 ||currencyHandler.convertStringPatternToFloat($('#amount').val()) <0){
				$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#amount").focus(function(event){
					$('#amountValid').empty();
					 $('#amount_pop').show();
				});
				$("#amount").blur(function(event){
					 $('#amount_pop').hide();
					 if(/^[0-9,.]+$/.test($('#amount').val())==false||$('#amount').val().charAt(0)==" "||currencyHandler.convertStringPatternToFloat($('#amount').val()) <0){
						 $('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#amount").focus(function(event){
							 $('#amountValid').empty();
							 if($('#amount').val().length==0){
									 $('#amount_pop').show();
							 }
							});
					 }else{
						 $('#amount_pop').hide();
					 }
				});
				result =false; 
			}
			if(currencyHandler.convertStringPatternToFloat($('#amount').val())>10000000000000){
				$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#amount").focus(function(event){
					$('#amountValid').empty();
					 $('#amount_len_pop').show();
				});
				$("#amount").blur(function(event){
					 $('#amount_pop').hide();
					 if(currencyHandler.convertStringPatternToFloat($('#amount').val())>10000000000000){
						 $('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#amount").focus(function(event){
							 $('#amountValid').empty();
							 if($('#amount').val().length==0){
									 $('#amount_len_pop').show();
							 }
							});
					 }else{
						 $('#amount_len_pop').hide();
					 }
				});
				result =false;
			}
			return result;
		},	
};
var ValidateCustomerChangerequest = {
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
};