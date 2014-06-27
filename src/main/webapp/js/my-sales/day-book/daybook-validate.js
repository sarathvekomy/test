var ValidateDayBookHandler = {
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
		validateReportingManager : function(){
			var result = true;
			if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
				$('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#reportingManager").focus(function(event){
					$('#reportingManagerValid').empty();
					 $('#reportingManagerLen_pop').hide();
					 $('#reportingManager_pop').show();
				});
				$("#reportingManager").blur(function(event){
					 $('#reportingManager_pop').hide();
					 if(/^[a-zA-Z\s]+$/.test($('#reportingManager').val()) == false || $('#reportingManager').val().length ==0){
						 $('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#reportingManager").focus(function(event){
							 $('#reportingManagerValid').empty();
							 $('#reportingManagerLen_pop').hide();
							 $('#reportingManager_pop').show();
						 });
						
					 }else{
						 $('#reportingManagerValid').empty();
					 }
				});
				result = false;
			}
			if($('#reportingManager').val().length > 35){

				$('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#reportingManager").focus(function(event){
					$('#reportingManagerValid').empty();
					 $('#reportingManager_pop').hide();
					 $('#reportingManagerLen_pop').show();
				});
				$("#reportingManager").blur(function(event){
					 $('#reportingManagerLen_pop').hide();
					 if($('#reportingManager').val().length > 35){
						 $('#reportingManagerValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#reportingManager").focus(function(event){
							 $('#reportingManagerValid').empty();
							 $('#reportingManager_pop').hide();
							 $('#reportingManagerLen_pop').show();
						 });
						
					 }else{
						 $('#reportingManagerValid').empty();
					 }
				});
				result = false;
			
			}
			return result;
		},
		validateReading : function(){
			var dayBookType = 'Vehicle Fuel Expenses';
			$.ajax({type: "POST",
				url:'dayBook.json', 
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
				var elen=$('#endingReading').val().length;
				if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt($('#endingReading').val().length-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#readingValid").focus(function(event){
						$('#readingLen_pop').hide();
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#readingvalid_pop').hide();
							if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt($('#endingReading').val().length-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingLen_pop').hide();
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
				if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val()) ||$('#endingReading').val().length==0||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#readingLen_pop').hide();
						$('#readingvalid_pop').hide();	
						$('#readingValid').empty();
						 $('#reading_pop').show();
					});
					$("#endingReading").blur(function(event){
						 $('#reading_pop').hide();
						 if(parseInt($('#startReading').val()) > parseInt($('#endingReading').val())||parseInt($('#startReading').val()) == parseInt($('#endingReading').val())||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
							 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#endingReading").focus(function(event){
								 $('#readingLen_pop').hide();
								 $('#readingvalid_pop').hide();
								 $('#readingValid').empty();
							 $('#reading_pop').show();
							 });
						 }else{
							 $('#readingValid').empty();
						 }
						 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
								$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#endingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#readingLen_pop').hide();
									$('#readingValid').empty();
									 $('#readingvalid_pop').show();
								});
								$("#endingReading").blur(function(event){
									 $('#readingvalid_pop').hide();
									 if(/^[0-9,]+$/.test($('#endingReading').val()) == false ||$('#endingReading').val().length==0||$('#endingReading').val().charAt(0)==" "||$('#endingReading').val().charAt(elen-1)==" "||parseInt($('#endingReading').val())< parseInt(existedStartReading)){
										 $('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#endingReading").focus(function(event){
											 $('#readingLen_pop').hide();
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
					});
					result=false;
					
				}
				if($('#endingReading').val().length > 6){
					$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#endingReading").focus(function(event){
						$('#reading_pop').hide();
						$('#readingValid').empty();
						 $('#readingvalid_pop').hide();
						 $('#readingLen_pop').show();
					});
					$("#endingReading").blur(function(){
						 $('#readingLen_pop').hide();
						if($('#endingReading').val().length > 6){
							$('#readingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							$("#endingReading").focus(function(event){
								$('#reading_pop').hide();
								$('#readingValid').empty();
								 $('#readingvalid_pop').hide();
								 $('#readingLen_pop').show();
							});
						}
					});
					result =false;
				}
				/*else{
					 $('#readingValid').empty();
				}*/
				return result;
		},
		validateAmounts : function(){
			var result = true;
			var closingBalnce = currencyHandler.convertStringPatternToFloat($('#closingBalance').val());
			var openingBalance =$('#allotStockOpeningBalance').val();
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
			if(amounttoFactory < 0){
				showMessage({title:'Error', msg:'Amount To Factory should have a positive value'});
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
};
var ValidateTempDayBookHandler = {
		validateAmounts : function(){
			var result = true;
			var elen = $('#bankAmount').val().length;
			if(/^[0-9,.]+$/.test($('#bankAmount').val()) == false ||$('#bankAmount').val().length==0||$('#bankAmount').val().charAt(0)==" "||$('#bankAmount').val().charAt(elen-1)==" "){
				$('#amountToBankValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#bankAmount').focus(function(event){
				$('#amountToBankValid').empty();
				$('#factory').html('Haii');
				$('#amountToBank_pop').show();
				});
			$("#bankAmount").blur(function(event){
				 $('#amountToBank_pop').hide();
				 if(/^[0-9,.]+$/.test($('#bankAmount').val()) == false ||$('#bankAmount').val().length==0||$('#bankAmount').val().charAt(0)==" "||$('#bankAmount').val().charAt(elen-1)==" "){
					 $('#amountToBankValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#bankAmount").focus(function(event){
						 $('#factory').html('Haii');
						 $('#amountToBank_pop').show();
						 $('#amountToBankValid').empty();
					 });
					
				 }else{
					 $('#amountToBankValid').empty();
				 }
			});
			result = false;
			}
			if($('#Remarks').val().length == 0){
				$("#remarksValid").html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#Remarks').focus(function(){
					$("#remarksValid").empty();
					$("#remarks_pop").show();
				});
				$('#Remarks').blur(function(){
					$("#remarks_pop").hide();
					if($('#Remarks').val().length == 0){
						$("#remarksValid").html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						$('#Remarks').focus(function(){
							$("#remarksValid").empty();
							$("#remarks_pop").show();
						});
					}
				});
				result = false;
			}
			return result;
		},
		
		validateVehicleDetails : function(){
			var result = true;
				var startingReadingVal = $('#startReading').val();
				var endingReadingVal = $('#endingReading').val();
				var dlen =$('#drivername').val().length
				var vlen=$('#vehicleNo').val().length;
				if(/^[a-zA-Z\s]+$/.test($('#drivername').val())==false ||$('#drivername').val().length==0||$('#drivername').val().charAt(0)==" "||$('#drivername').val().charAt(dlen -1)==" "){
					$('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#drivername").focus(function(event){
						$('#driverNameValid').empty();
						 $('#driverNameLen_pop').hide();
						 $('#driverName_pop').show();
					});
					$("#drivername").blur(function(event){
						 $('#driverName_pop').hide();
						 if(/^[a-zA-Z\s]+$/.test($('#drivername').val())==false ||$('#drivername').val().length==0||$('#drivername').val().charAt(0)==" "||$('#drivername').val().charAt(dlen-1)==" "){
							 $('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#drivername").focus(function(event){
								 $('#driverNameValid').empty();
								 $('#driverNameLen_pop').hide();
								 $('#driverName_pop').show();
							 });
							
						 }else{
							 $('#driverNameValid').empty();
						 }
					});
					result=false;

				}
				if($("#drivername").val().length > 35){
					$('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#drivername").focus(function(event){
						$('#driverNameValid').empty();
						 $('#driverName_pop').hide();
						 $('#driverNameLen_pop').show();
					});
					$("#drivername").blur(function(event){
						 $('#driverNameLen_pop').hide();
						 if($("#drivername").val().length > 35){
							 $('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#drivername").focus(function(event){
								 $('#driverNameValid').empty();
								 $('#driverName_pop').hide();
								 $('#driverNameLen_pop').show();
							 });
							
						 }if(/^[a-zA-Z\s]+$/.test($('#drivername').val())==false ||$('#drivername').val().length==0||$('#drivername').val().charAt(0)==" "||$('#drivername').val().charAt(dlen -1)==" "){
								$('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#drivername").focus(function(event){
									$('#driverNameValid').empty();
									 $('#driverNameLen_pop').hide();
									 $('#driverName_pop').show();
								});
								$("#drivername").blur(function(event){
									 $('#driverName_pop').hide();
									 if(/^[a-zA-Z\s]+$/.test($('#drivername').val())==false ||$('#drivername').val().length==0||$('#drivername').val().charAt(0)==" "||$('#drivername').val().charAt(dlen-1)==" "){
										 $('#driverNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#drivername").focus(function(event){
											 $('#driverNameValid').empty();
											 $('#driverNameLen_pop').hide();
											 $('#driverName_pop').show();
										 });
										
									 }else{
										 $('#driverNameValid').empty();
									 }
								});
								result=false;

							}
						 else{
							 $('#driverNameValid').empty();
						 }
					});
					result=false;
				} 
				if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNumber').val())==false ||$('#vehicleNumber').val().length==0||$('#vehicleNumber').val().charAt(0)==" "||$('#vehicleNumber').val().charAt(vlen -1)==" "){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#vehicleNumber").focus(function(event){
						$('#vehicleNoLen_pop').hide();
						$('#vehicleNOValid').empty();
						 $('#vehicleNo_pop').show();
					});
					$("#vehicleNumber").blur(function(event){
						 $('#vehicleNo_pop').hide();
						 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNumber').val())==false ||$('#vehicleNumber').val().length==0||$('#vehicleNumber').val().charAt(0)==" "||$('#vehicleNumber').val().charAt(vlen-1)==" "){
							 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#vehicleNumber").focus(function(event){
								 $('#vehicleNoLen_pop').hide();
								 $('#vehicleNoValid').empty();
								 $('#vehicleNo_pop').show();
							 });
							
						 }else{
							 $('#vehicleNOValid').empty();
						 }
					});
					
					result=false;
				}
				if($("#vehicleNumber").val().length >12){
					$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#vehicleNumber").focus(function(event){
						$('#vehicleNOValid').empty();
						 $('#vehicleNo_pop').hide();
						 $('#vehicleNoLen_pop').show();
					});
					$("#vehicleNumber").blur(function(event){
						$('#vehicleNOValid').empty();
						$('#vehicleNoLen_pop').hide();
						if($("#vehicleNumber").val().length >12){
							$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							$("#vehicleNumber").focus(function(event){
								$('#vehicleNOValid').empty();
								 $('#vehicleNo_pop').hide();
								 $('#vehicleNoLen_pop').show();
							});
						}
						if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNumber').val())==false ||$('#vehicleNumber').val().length==0||$('#vehicleNumber').val().charAt(0)==" "||$('#vehicleNumber').val().charAt(vlen -1)==" "){
							$('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							$("#vehicleNumber").focus(function(event){
								$('#vehicleNoLen_pop').hide();
								$('#vehicleNOValid').empty();
								 $('#vehicleNo_pop').show();
							});
							$("#vehicleNumber").blur(function(event){
								 $('#vehicleNo_pop').hide();
								 if(/^[a-zA-Z0-9-./\s]+$/.test($('#vehicleNumber').val())==false ||$('#vehicleNumber').val().length==0||$('#vehicleNumber').val().charAt(0)==" "||$('#vehicleNumber').val().charAt(vlen-1)==" "){
									 $('#vehicleNOValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
									 $("#vehicleNumber").focus(function(event){
										 $('#vehicleNoLen_pop').hide();
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
				
				
				var slen=$('#startingReading').val().length;
				var elen=$('#startReading').val().length;
				if(/^[0-9,]+$/.test($('#startingReading').val()) == false||$('#startingReading').val().length==0||$('#startingReading').val().charAt(0)==" "||$('#startingReading').val().charAt(slen-1)==" "){
					$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#startingReading").focus(function(event){
						$('#reading_pop').hide();
						$('#startreadingValid').empty();
						 $('#startreadingLen_pop').hide();
						 $('#startreading_pop').show();
					});
					$("#startingReading").blur(function(event){
						 $('#startreading_pop').hide();
						 if(/^[0-9,]+$/.test($('#startingReading').val()) == false||$('#startingReading').val().length==0||$('#startingReading').val().charAt(0)==" "||$('#startingReading').val().charAt(slen-1)==" "){
							 $('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#startingReading").focus(function(event){
								 $('#startreadingValid').empty();
								 $('#startreadingLen_pop').hide();
								 $('#startreading_pop').show();
							 });
							
						 }else{
							 $('#readingValid').empty();
						 }
					});
					result=false;
				}
				if($('#startingReading').val().length>6){
					$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#startingReading").focus(function(event){
						$('#reading_pop').hide();
						$('#startreadingValid').empty();
						 $('#startreading_pop').hide();
						 $('#startreadingLen_pop').show();
					});
					$("#startingReading").blur(function(event){
						$('#startreadingLen_pop').hide();
							if($('#startingReading').val().length>6){
								$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#startingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#startreadingValid').empty();
									 $('#startreading_pop').hide();
									 $('#startreadingLen_pop').show();
								});
							}
							if(/^[0-9,]+$/.test($('#startingReading').val()) == false||$('#startingReading').val().length==0||$('#startingReading').val().charAt(0)==" "||$('#startingReading').val().charAt(slen-1)==" "){
								$('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
								$("#startingReading").focus(function(event){
									$('#reading_pop').hide();
									$('#startreadingValid').empty();
									 $('#startreadingLen_pop').hide();
									 $('#startreading_pop').show();
								});
								$("#startingReading").blur(function(event){
									 $('#startreading_pop').hide();
									 if(/^[0-9,]+$/.test($('#startingReading').val()) == false||$('#startingReading').val().length==0||$('#startingReading').val().charAt(0)==" "||$('#startingReading').val().charAt(slen-1)==" "){
										 $('#startreadingValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
										 $("#startingReading").focus(function(event){
											 $('#startreadingValid').empty();
											 $('#startreadingLen_pop').hide();
											 $('#startreading_pop').show();
										 });
										
									 }else{
										 $('#readingValid').empty();
									 }
								});
								result=false;
							}
					});
					result=false;
				}
				return result;
		
		},
		validateAmount : function(){
			var result =true;
			if(/^[0-9,.]+$/.test($('#allowanceAmount').val()) == false ||$('#allowanceAmount').val().length==0){
				$('#allowancesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$('#allowanceAmount').focus(function(event){
				$('#allowancesValid').empty();
				$('#allowances_pop').show();
				});
			$("#allowanceAmount").blur(function(event){
				 $('#allowances_pop').hide();
				 if(/^[0-9,.]+$/.test($('#allowanceAmount').val()) == false ||$('#allowanceAmount').val().length==0){
					 $('#allowancesValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#allowanceAmount").focus(function(event){
						 $('#allowances_pop').hide();
						 $('#allowancesValid').empty();
					 });
					
				 }else{
					 $('#readingValid').empty();
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
		validateBusinessName : function(){
			var result = true;
			if(/^[a-zA-Z0-9\s]+$/.test($('#businessname').val())==false||$('#businessname').val().charAt(0)==" "){
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessname").focus(function(event){
					$('#businessnamevalid_pop').hide();
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
				});
			
			$("#businessname").blur(function(event){
				 $('#businessName_pop').hide();
				 if(/^[a-zA-Z0-9\s]+$/.test($('#businessname').val())==false||$('#businessname').val().charAt(0)==" "){
					 $('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#businessname").focus(function(event){
						 $('#businessnamevalid_pop').hide();
						 $('#businessNameValid').empty();
						 $('#businessName_pop').show();
					 });
					 
				 }else{
					 $('#businessNameValid').empty();
					 $('#businessName_pop').hide();
				 }
			});
			result=false;
			}
	return result;
		},
		validateAllowances : function(){
			var result =true;
			if($('#allowancesType').val() == "-1"){
				$('#allowancesTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#allowancesType").focus(function(event){
					if($('#allowancesType').val() == "-1"){
						$('#allowancesTypeValid').empty();
						 $('#allowancesType_pop').show();
					}else{
						$('#allowancesTypeValid').empty();
						 $('#allowancesType_pop').hide();
					}
					 });
				$("#allowancesType").blur(function(event){
					 $('#allowancesType_pop').hide();
						if($('#allowancesType').val() == "-1"){
						 $('#allowancesTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#allowancesType").focus(function(event){
							 $('#allowancesType_pop').hide();
							 $('#allowancesTypeValid').empty();
							 $('#allowancesType_pop').show();
						 });
						 
					 }else{
						 $('#allowancesType_pop').hide();
					 }
				});
				result =false;
			}
			return result;
		},
		validateDayBookType : function(){
			var result = true;
			if($('#dayBookType').val() == "-1"){
				$('#dayBookTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#dayBookType").focus(function(event){
					if($('#dayBookType').val() == "-1"){
						$('#dayBookTypeValid').empty();
						 $('#dayBookType_pop').show();
					}else{
						$('#dayBookTypeValid').empty();
						 $('#dayBookType_pop').hide();
					}
					 });
				$("#dayBookType").blur(function(event){
					 $('#dayBookType_pop').hide();
						if($('#dayBookType').val() == "-1"){
						 $('#dayBookTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#dayBookType").focus(function(event){
							 $('#dayBookType_pop').hide();
							 $('#dayBookTypeValid').empty();
							 $('#dayBookType_pop').show();
						 });
						 
					 }else{
						 $('#dayBookType_pop').hide();
					 }
				});
				result =false;
			}
			return result;
		},	
};