var ValidateChangeTransaction = {
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
			return result;
		},
		
		validateMeterreading : function(meterReading,id){
			//alert("Row Id:"+id);
			var mainId = $('#'+id).parent().attr('id');
			var res = $('#'+id).parent().attr('class');
			$('.col4').change(function(){
			});
			/*var startingReading ;
			var endingReading;*/
			var startingReading = $('#startReading').val();
			var endingReading = $('#endingReading').val();
				var Sno = $('#'+mainId).children('.row1').children('.col1').val();
			/*if(Sno == 1){
				alert("if");
				startingReading = $('#startReading').val();
				endingReading = $('#endingReading').val();
			}else{
				alert("else");
				valueThree = currencyHandler.convertStringPatternToFloat($('#'+mainId).children('.row4').children('.col4').val());
				var dayBookType = "Vehicle Fuel Expenses";
				$.ajax({type: "POST",
					url:'dayBookCr.json', 
					data: 'action=get-starting-reading&dayBookType='+dayBookType,
					async : false,
					success: function(data){
						 existedStartReading = data.result.data;
						 alert(existedStartReading);
						 return existedStartReading;
					},
				});
				 startingReading = existedStartReading;
				 alert("startingReading:"+startingReading);
			}*/
			var check;
			//alert("meterReading:"+meterReading);
			//alert("startingReading:"+startingReading);
			//alert("endingReading"+endingReading);
			if(meterReading < startingReading || meterReading >endingReading  ){
				check = false;
			}else{
				check = true;
			}
			//alert(check);
			return check;
		},
		
		validateReportingManager : function(){
			var result = true;
			if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
				$('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#reportingManager").focus(function(event){
					$('#reportingManagerValid').empty();
					 $('#reportingManager_pop').show();
				});
				$("#reportingManager").blur(function(event){
					 $('#reportingManager_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
						 $('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#reportingManager").focus(function(event){
							 $('#reportingManagerValid').empty();
							 $('#reportingManager_pop').show();
						 });
						
					 }else{
						 $('#reportingManagerValid').empty();
					 }
				});
				result = false;
			}
			return result;
		},
		validateMeterReading : function(existedStartReading){
			var result =true;
			if(/^[0-9]+$/.test($('#meterreading').val()) == false||$('#meterreading').val().length==0||parseInt($('#meterreading').val()) < parseInt(existedStartReading)||parseInt($('#meterreading').val()) == parseInt(existedStartReading)){
				$('#meterReadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#meterreading').focus(function(event){
				$('#meterReadingValid').empty();
				$('#meterReading_pop').show();
				});
			$("#meterreading").blur(function(event){
				 $('#meterReading_pop').hide();
				 if(/^[0-9]+$/.test($('#meterreading').val()) == false||$('#meterreading').val().length==0||parseInt($('#meterreading').val()) < parseInt(existedStartReading)||parseInt($('#meterreading').val()) == parseInt(existedStartReading)){
					 $('#meterReadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#meterreading").focus(function(event){
						 $('#meterReading_pop').show();
						 $('#meterReadingValid').empty();
					 });
					
				 }else{
					 $('#readingValid').empty();
				 }
			});
			result = false;
			}
			return result;
		},
		validateAllowances :function(){
			var result = true;
			end = $('#executiveAllowances').val().length;
			if(/^[0-9.,]+$/.test($('#executiveAllowances').val())==false &&$('#executiveAllowances').val().length>0||$('#executiveAllowances').val().charAt(0)==" "||$('#executiveAllowances').val().charAt(end -1)==" "){
				$('#executiveallValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#executiveAllowances").focus(function(event){
					$('#executiveall_pop').hide();
					$('#executiveallValid').empty();
					 $('#executiveall_pop').show();
				});
			$("#executiveAllowances").blur(function(event){
				 $('#executiveall_pop').hide();
				 if(/^[0-9.,]+$/.test($('#executiveAllowances').val())==false||$('#executiveAllowances').val().charAt(0)==" "||$('#executiveAllowances').val().charAt(end -1)==" "){
					 $('#executiveallValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#executiveAllowances").focus(function(event){
						 $('#executiveall_pop').hide();
						 $('#executiveallValid').empty();
						 $('#executiveall_pop').show();
					 });
					 
				 }
			});
			result=false;
			}
			var startReading = $('#startReading').val();
			var endReading = $('#endingReading').val();
			return result;
		},
		validateReading : function(){
			var existedStartReading;
			var dayBookType = 'Vehicle Fuel Expenses';
			$.ajax({type: "POST",
				url:'dayBookCr.json', 
				data: 'action=get-starting-reading&dayBookType='+dayBookType,
				async : false,
				success: function(data){
					 existedStartReading = data.result.data;
					 return existedStartReading;
				}
			});
			var result = true;
				var startingReadingVal = $('#startReading').val();
				var endingReadingVal = $('#endingReading').val();
				var dlen =$('#driverName').val().length
				var vlen=$('#vehicleNo').val().length;
				if(/^[a-zA-Z\s]+$/.test($('#driverName').val())==false ||$('#driverName').val().length==0||$('#driverName').val().charAt(0)==" "||$('#driverName').val().charAt(dlen -1)==" "){
					$('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#driverName").focus(function(event){
						$('#driverNameValid').empty();
						 $('#driverName_pop').show();
					});
					$("#driverName").blur(function(event){
						 $('#driverName_pop').hide();
						 if(/^[a-zA-Z\s]+$/.test($('#driverName').val())==false ||$('#driverName').val().length==0||$('#driverName').val().charAt(0)==" "||$('#driverName').val().charAt(dlen-1)==" "){
							 $('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#driverName").focus(function(event){
								 $('#driverNameValid').empty();
								 $('#driverName_pop').show();
							 });
							
						 }else{
							 $('#driverNameValid').empty();
						 }
					});
					result=false;

				}
				if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen -1)==" "){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#vehicleNo").focus(function(event){
						$('#vehicleNOValid').empty();
						 $('#vehicleNo_pop').show();
					});
					$("#vehicleNo").blur(function(event){
						 $('#vehicleNo_pop').hide();
						 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen-1)==" "){
							 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#vehicleNo").focus(function(event){
								 $('#vehicleNoValid').empty();
								 $('#vehicleNo_pop').show();
							 });
							
						 }else{
							 $('#vehicleNOValid').empty();
						 }
					});
					result=false;
				}
				if($('#vehicleNo').val().length>20){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $('#vehicleNo_pop').hide();
					$("#vehicleNo").focus(function(event){
						 $('#vehicleNo_pop').hide();
						$('#vehicleNOValid').empty();
						 $('#vehicleNolen_pop').show();
					});
					$("#vehicleNo").blur(function(event){
						 $('#vehicleNolen_pop').hide();
						 if($('#vehicleNo').val().length>20){
							 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#vehicleNo").focus(function(event){
								 $('#vehicleNo_pop').hide();
								 $('#vehicleNoValid').empty();
								 $('#vehicleNolen_pop').show();
							 });
							
						 }else{
							 $('#vehicleNOValid').empty();
						 }
						 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen -1)==" "){
								$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#vehicleNo").focus(function(event){
									 $('#vehicleNolen_pop').hide();
									$('#vehicleNOValid').empty();
									 $('#vehicleNo_pop').show();
								});
								$("#vehicleNo").blur(function(event){
									 $('#vehicleNo_pop').hide();
									
									 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNo').val())==false ||$('#vehicleNo').val().length==0||$('#vehicleNo').val().charAt(0)==" "||$('#vehicleNo').val().charAt(vlen-1)==" "){
										 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#vehicleNo").focus(function(event){
											 $('#vehicleNolen_pop').hide();
											 $('#vehicleNoValid').empty();
											 $('#vehicleNo_pop').show();
										 });
										
									 }else{
										 $('#vehicleNOValid').empty();
									 }
								});
								result=false;
							}
					});
					result=false;
				}
				var slen=$('#startReading').val().length;
				var elen=$('#startReading').val().length;
				if(/^[0-9,.]+$/.test($('#startReading').val()) == false||$('#startReading').val().length==0||$('#startReading').val().charAt(0)==" "||$('#startReading').val().charAt(slen-1)==" "){
					$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#startReading").focus(function(event){
						$('#reading_pop').hide();
						$('#startreadingValid').empty();
						 $('#startreading_pop').show();
					});
					$("#startReading").blur(function(event){
						 $('#startreading_pop').hide();
						 if(/^[0-9,.]+$/.test($('#startReading').val()) == false||$('#startReading').val().length==0||$('#startReading').val().charAt(0)==" "||$('#startReading').val().charAt(slen-1)==" "){
							 $('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#startReading").focus(function(event){
								 $('#startreadingValid').empty();
								 $('#startreading_pop').show();
							 });
							
						 }else{
							 $('#readingValid').empty();
						 }
					});
					result=false;
				}
				if(/^[0-9,.]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#readingValid").focus(function(event){
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#readingvalid_pop').hide();
						 if(/^[0-9,.]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" " ||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#reading_pop').hide();
								 $('#readingValid').empty();
								 $('#readingvalid_pop').show();
							 });
							
						 }else{
							 $('#readingValid').empty();
						 }
					});
					result=false;
				}else{
					 $('#readingValid').empty();
				}
				if(parseInt(currencyHandler.convertStringPatternToFloat($('#startReading').val())) > parseInt(currencyHandler.convertStringPatternToFloat($('#endingReading').val()))||parseInt(currencyHandler.convertStringPatternToFloat($('#startReading').val())) == parseInt(currencyHandler.convertStringPatternToFloat($('#endingReading').val())) ||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#readingvalid_pop').hide();	
						$('#readingValid').empty();
						 $('#reading_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#reading_pop').hide();
						 if(currencyHandler.convertStringPatternToFloat(parseInt($('#startReading').val())) > parseInt(currencyHandler.convertStringPatternToFloat($('#endingReading').val()))||parseInt(currencyHandler.convertStringPatternToFloat($('#startReading').val())) == parseInt(currencyHandler.convertStringPatternToFloat($('#endingReading').val())) ||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingvalid_pop').hide();
								 $('#readingValid').empty();
							 $('#reading_pop').show();
							 });
						 }
						 if(/^[0-9,.]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" " ||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
								$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#endingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#readingValid').empty();
									 $('#readingvalid_pop').show();
								});
								$("#endingReading").blur(function(event){
									 $('#readingvalid_pop').hide();
									 if(/^[0-9,.]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" " ||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
										 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#endingReading").focus(function(event){
											 $('#reading_pop').hide();
											 $('#readingValid').empty();
											 $('#readingvalid_pop').show();
										 });
									 }else{
										 $('#readingValid').empty();
									 }
								});
								result=false;
							}
					});
					result=false;
					
				}else{
					// $('#readingValid').empty();
				}
				return result;
		},
		validateAmounts : function(){
			var result = true;
			var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val());
			var openingBalance =$('#allotStockAdvance').val();
				var totalAllowances = currencyHandler.convertStringPatternToFloat($('#totalAllowances').val());
				var customerTotalReceived = currencyHandler.convertStringPatternToFloat($('#customerTotalReceived').val());
				var amountToBank = currencyHandler.convertStringPatternToFloat($('#amountToBank').val());
				var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val())
				var amounttoFactory = currencyHandler.convertStringPatternToFloat($('#amountToFactory').val())
				var sum = amountToBank + closingBalnce + amounttoFactory;
				var validResult = (currencyHandler.convertStringPatternToFloat(openingBalance)+customerTotalReceived)-(amountToBank+totalAllowances);
				if($('#isReturn').is(':checked')) {
			   if(parseFloat(validResult - amounttoFactory) < 0){
				showMessage({title:'Error', msg:'Amount To Factory Should Not Exceed Closing Balance( '+parseFloat(validResult).toFixed(2)+')'});
					result = false;
				}
				}
			if(/^[0-9-,.]+$/.test($('#amountToFactory').val()) == false && $('#amountToFactory').val().length >0){
				$('#amountToFactoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#amountToFactory").focus(function(event){
					$('#amountToFactory_pop').show();	
					$('#amountToFactoryValid').empty();
				});
				$("#amountToFactory").blur(function(event){
					$('#amountToFactory_pop').hide();	
					if(/^[0-9-,.]+$/.test($('#amountToFactory').val()) == false&&$('#amountToFactory').val().length >0){
						 $('#amountToFactoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#amountToFactory").focus(function(event){
								$('#amountToFactory_pop').show();	
								$('#amountToFactoryValid').empty();
							});
					 }else{
						 $('#amountToFactoryValid').empty();
					 }
				});
				result = false;
			}
			
			return result;
		},
		validateJournals : function(){
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
			if($('#amount').val().length == 0){
				$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#amount").focus(function(event){
					$('#amountValid').empty();
					 $('#amount_pop').show();
				});
				$("#amount").blur(function(event){
					 $('#amount_pop').hide();
					 if(/^[0-9,.]+$/.test($('#amount').val())==false||$('#amount').val().charAt(0)==" "){
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
		},
};
