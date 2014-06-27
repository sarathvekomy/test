var ReportsHandler = {
		initPageLinks : function() {
			$('#customer-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/customerwise_report_show.jsp'
			});
			$('#product-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/productwise_report_show.jsp'
			});

			$('#sales-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/saleswise_report_show.jsp'
			});
			$('#sales-return-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/salesreturnwise_report_show.jsp'
			});
			$('#sales-executive-expenditure-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales_executive_expenditure_wise_report_show.jsp'
			});
			$('#sales-executive-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales_executive_wise_reports.jsp'
			});
			$('#dynamic-reports').pageLink({
				container : '.dynamic-report-page-container',
				url :'my-sales/reports/dynamic_reports.jsp'
			});
		}, 
		validateBusinessName: function() {
			var result = true;
			var businessName = $('#businessName').val();
			$.ajax({type : "POST",
				url : 'reports.json',
				data : 'action=check-business-name-availability&businessName='+businessName,
				async : false,
				success : function(obj) {
				var data = obj.result.data;
				if(data != "true") {
					$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
					$("#businessName").focus(function(event){
						$('#businessNameValid').empty();
						 $('#businessName_pop').show();
				});
				$("#businessName").blur(function(event){
					 $('#businessName_pop').hide();
				});
				result = false;
				}
			},
			});
			return result;
	},
	validateBusinessNameBasedOnSalesExecutive: function() {
		var result = true;
		var businessName = $('#businessName').val();
		var salesExecutive = $('#salesExecutive').val();
		$.ajax({type : "POST",
			url : 'reports.json',
			data : 'action=check-business-name-availability-based-on-sales-executive&businessName='+businessName+'&salesExecutive='+salesExecutive,
			async : false,
			success : function(obj) {
			var data = obj.result.data;
			if(data != "true") {
				$('#businessNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#businessName").focus(function(event){
					$('#businessNameValid').empty();
					 $('#businessName_pop').show();
			});
			$("#businessName").blur(function(event){
				 $('#businessName_pop').hide();
			});
			result = false;
			}
		},
		});
		return result;
	},
	validateSalesExecutive: function() {
		var result = true;
		var salesExecutive = $('#salesExecutive').val();
		$.ajax({type : "POST",
			url : 'reports.json',
			data : 'action=check-sales-executive-availability&salesExecutive='+salesExecutive,
			async : false,
			success : function(obj) {
			var data = obj.result.data;
			if(data != "true") {
				$('#salesExecutiveValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#salesExecutive").focus(function(event){
					$('#salesExecutiveValid').empty();
					 $('#salesExecutive_pop').show();
			});
			$("#salesExecutive").blur(function(event){
				 $('#salesExecutive_pop').hide();
			});
			result = false;
			}
		},
		});
		return result;
	},
	suggestSalesExecutive : function(thisInput) {
		var suggestionsDiv = $('#salesexecutive-name-suggestions');
		var val = $('#salesExecutive').val();
		$.post('reports.json','action=get-all-sales-executives&salesExecutiveVal=' + val,function(obj) {
							$.loadAnimation.end();
							suggestionsDiv.html('');
							var data = obj.result.data;
							if (data != undefined) {
								var htmlStr = '<div>';
								for ( var loop = 0; loop < data.length; loop = loop + 1) {
									htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
											+ data[loop]
											+ '</a></li>';
								}
								htmlStr += '</div>';
								suggestionsDiv.append(htmlStr);
							} else {
								suggestionsDiv.append('<div id="">'
										+ 'No Sales Executives' + '</div>');
							}
							suggestionsDiv.css('left',thisInput.position().left);
							suggestionsDiv.css('top',thisInput.position().top + 25);
							suggestionsDiv.show();
							$('.select-teacher').click(
									function() {
										thisInput.val($(this).html());
										thisInput.attr('salesExecutive', $(this)
												.attr('salesExecutive'));
										$('#salesExecutive').attr('value',
												$(this).attr('salesExecutive'));
										suggestionsDiv.hide();
									});
						});
	}
};

var CustomerWiseReportHandler = {
		load : function() {

			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				CustomerWiseReportHandler.suggestBusinessName(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				CustomerWiseReportHandler.suggestBusinessName(thisInput);
			});

			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
			
			$('#businessName').change(function() {
				if($('#businessName').val().length > 0) {
					ReportsHandler.validateBusinessName();
				}
			});
		},
		
		suggestBusinessName : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var val = $('#businessName').val();
			$.post('reports.json','action=get-all-business-names&businessNameVal=' + val,function(obj) {
								$.loadAnimation.end();
								suggestionsDiv.html('');
								var data = obj.result.data;
								if (data != undefined) {
									var htmlStr = '<div>';
									for ( var loop = 0; loop < data.length; loop = loop + 1) {
										htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
												+ data[loop]
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
											$('#businessName').attr('value',
													$(this).attr('businessName'));
											suggestionsDiv.hide();
											var businessName = $('#businessName').val();
											$.post('reports.json','action=get-customer-product-name&businessName='+businessName, function(obj) {
												var productNameOptions = $("#productName");
												$.each(obj.result.data, function(i, productName) {
													productNameOptions.append('<option value="' + productName
															+ '">' + productName + '</option>');

												});
											});
										});
							});
		},
		registorReportShowEvents : function(cfg) {
			$('#action-show').click(function(cfg) {
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#customer-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#customer-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('customerWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			        
					$("#report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 950,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
				});
				
			});
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
			
			$('.btn-clear').click(function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/customerwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/customerwise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#action-pdf').click(function(cfg) {
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#customer-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#customer-wise-report-form').attr('action','customerWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#customer-wise-report-form').submit();
					
				});
			$('#action-xls').click(function(cfg) {
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#customer-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#customer-wise-report-form').attr('action','customerWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#customer-wise-report-form').submit();
				});
			
		}
};


var ProductWiseReportHandler = {
		
		load : function() {
			$.post('reports.json','action=get-all-product-names', function(obj) {
				var productNameOptions = $("#productName");
				$.each(obj.result.data, function(i, productName) {
					productNameOptions.append('<option value="' + productName
							+ '">' + productName + '</option>');

				});
			});
			
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		registorReportShowEvents : function(cfg) {
			$('#product-action-show').click(function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#product-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('productWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			        
			        $("#report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 850,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
			      
				});
				
				//$('#customer-wise-report-form').clearForm();
				
				
			});
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
			$('.btn-clear').click( function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/productwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');

						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/productwise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#product-action-pdf').click(function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-wise-report-form').attr('action','productWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
					
				});
			$('#product-action-xls').click(function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#product-wise-report-form').attr('action','productWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
				});
			
		}
};

var SalesWiseReportHandler = {
		
		load : function() {
			$('#salesExecutive').click(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});

			$('#salesExecutive').focusout(function() {
				$('#salesexecutive-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#salesexecutive-name-suggestions').hide();
				});
			});
			$('#salesExecutive').change(function() {
				if($('#salesExecutive').val().length > 0) {
					$('#business-name-suggestions').empty();
					$('#businessName').val('');
					ReportsHandler.validateSalesExecutive();
				}
			});
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			$('#businessName').change(function() {
				if($('#businessName').val().length > 0) {
					ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		suggestBusinessNameBasedSalesExecutive : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var salesExecutiveVal = $('#salesExecutive').val();
			var businessNameVal = $('#businessName').val();
			$.post('reports.json','action=get-all-business-names-based-sales-executive&salesExecutiveVal=' + salesExecutiveVal +'&businessNameVal=' +businessNameVal, function(obj) {
								$.loadAnimation.end();
								suggestionsDiv.html('');
								var data = obj.result.data;
								if (data != undefined) {
									var htmlStr = '<div>';
									for ( var loop = 0; loop < data.length; loop = loop + 1) {
										htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
												+ data[loop]
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
											$('#businessName').attr('value',
													$(this).attr('businessName'));
											suggestionsDiv.hide();
										});
							});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#sales-action-show').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('salesWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			        
			        $("#report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 950,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
			      
				});
				
			});
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
			$('.btn-clear').click(function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/saleswise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/saleswise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sales-action-pdf').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-wise-report-form').attr('action','salesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
					
				});
			$('#sales-action-xls').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sales-wise-report-form').attr('action','salesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
				});
			
		}
	}

var SalesReturnWiseReportHandler = {
		load: function() {
			$('#salesExecutive').click(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});

			$('#salesExecutive').focusout(function() {
				$('#salesexecutive-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#salesexecutive-name-suggestions').hide();
				});
			});
			$('#salesExecutive').change(function() {
				if($('#salesExecutive').val().length > 0) {
					$('#business-name-suggestions').empty();
					$('#businessName').val('');
					ReportsHandler.validateSalesExecutive();
				}
			});
			
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesReturnWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesReturnWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			$('#businessName').change(function() {
				if($('#businessName').val().length > 0) {
					ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
			$('#productName').change(function() {
				var businessNameVal = $('#businessName').val();
				var productNameVal = $('#productName').val();
				$.post('reports.json', 'action=get-batch-number-associated-product&productNameVal='+productNameVal+'&businessNameVal='+businessNameVal, function(obj) {
					var batchNumberOptions = $('#batchNumber');
					$('#batchNumber').empty();
					batchNumberOptions.append('<option value="ALL"> ALL </option>');
					$.each(obj.result.data, function(i, batchNumber) {
						batchNumberOptions.append('<option value="'+batchNumber+'">' + batchNumber + '</option>');
					});
				});
			});
			$('#resalableOperator').change(function() {
				var resalableOperatorVal = $('#resalableOperator').val();
				if(resalableOperatorVal != "-1") {
					$('#resalableQty').val("");
					$('#resalableQty').show();
					$('#resalableQty').addClass('mandatory');
				} else {
					$('#resalableQty').hide();
					$('#resalableQty').removeClass('mandatory');
				}
			});
			$('#damagedOperator').change(function() {
				var damagedOperatorVal = $('#damagedOperator').val();
				if(damagedOperatorVal != "-1") {
					$('#damagedQty').val("");
					$('#damagedQty').show();
					$('#damagedQty').addClass('mandatory');
				} else {
					$('#damagedQty').hide();
					$('#damagedQty').removeClass('mandatory');
				}
			});
		},
		suggestBusinessNameBasedSalesExecutive : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var salesExecutiveVal = $('#salesExecutive').val();
			var businessNameVal = $('#businessName').val();
			$.post('reports.json','action=get-all-business-names-based-sales-executive&salesExecutiveVal=' + salesExecutiveVal +'&businessNameVal=' +businessNameVal, function(obj) {
								$.loadAnimation.end();
								suggestionsDiv.html('');
								var data = obj.result.data;
								if (data != undefined) {
									var htmlStr = '<div>';
									for ( var loop = 0; loop < data.length; loop = loop + 1) {
										htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
												+ data[loop]
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
											$('#businessName').attr('value',
													$(this).attr('businessName'));
											suggestionsDiv.hide();
											var businessName = $('#businessName').val();
											$.post('reports.json','action=get-customer-product-name&businessName='+businessName, function(obj) {
												var productNameOptions = $('#productName');
												$('#productName').empty();
												productNameOptions.append('<option value="ALL"> ALL </option>');
												$.each(obj.result.data, function(i,productName) {
													productNameOptions.append('<option value="' + productName
															+ '">' + productName + '</option>');
												});
											});
										});
							});
		},
		
		registorReportShowEvents: function() {
			$('#sales-return-action-show').click(function() {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-return-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-return-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('salesReturnReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#sales-return-report-container').html('');
			        $('#sales-return-report-container').html(obj);
			        
			        $("#sales-return-wise-report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 950,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
			      
				});
			});
			
			$('#sales-return-action-pdf').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				 if($('#businessName').val().length > 0) {
						if(ReportsHandler.validateBusinessName()== false){
							return;
						}
					}
				  if (!$('#sales-return-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-return-wise-report-form').attr('action','salesReturnReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-return-wise-report-form').submit();
					
				});
			$('#sales-return-action-xls').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-return-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sales-return-wise-report-form').attr('action','salesReturnReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-return-wise-report-form').submit();
				});
			
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
			
			$('.btn-clear').click(function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/salesreturnwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/salesreturnwise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
		}
};

var SalesExecutiveExpenditureWiseReportHandler = {
		load: function() {
			$('#salesExecutive').click(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});

			$('#salesExecutive').focusout(function() {
				$('#salesexecutive-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#salesexecutive-name-suggestions').hide();
				});
			});
			$('#salesExecutive').change(function() {
				if($('#salesExecutive').val().length > 0) {
					$('#business-name-suggestions').empty();
					$('#businessName').val('');
					ReportsHandler.validateSalesExecutive();
				}
			});
			
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		registorReportShowEvents: function() {
			$('#sales-expenditure-action-show').click(function() {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sales-executive-expenditure-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-executive-expenditure-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('salesExecutiveExpenditureReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#sales-executive-expenditure-wise-report-container').html('');
			        $('#sales-executive-expenditure-wise-report-container').html(obj);
			        
			        $("#sales-executive-expenditure-wise-report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 750,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
			      
				});
			});
			
			$('#sales-expenditure-action-pdf').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sales-executive-expenditure-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-executive-expenditure-wise-report-form').attr('action','salesExecutiveExpenditureReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-executive-expenditure-wise-report-form').submit();
					
				});
			$('#sales-expenditure-action-xls').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sales-executive-expenditure-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sales-executive-expenditure-wise-report-form').attr('action','salesExecutiveExpenditureReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-executive-expenditure-wise-report-form').submit();
				});
			
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
			
			$('.btn-clear').click(function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales_executive_expenditure_wise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales_executive_expenditure_wise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
		}
};

var SalesExecutiveWiseReprtHandler = {
		
		load : function() {
			$('#salesExecutive').click(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutive(thisInput);
			});

			$('#salesExecutive').focusout(function() {
				$('#salesexecutive-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#salesexecutive-name-suggestions').hide();
				});
			});
			$('#salesExecutive').change(function() {
				if($('#salesExecutive').val().length > 0) {
					$('#business-name-suggestions').empty();
					$('#businessName').val('');
					ReportsHandler.validateSalesExecutive();
				}
			});
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SalesWiseReportHandler.suggestBusinessNameBasedSalesExecutive(thisInput);
			});
			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			$('#businessName').change(function() {
				if($('#businessName').val().length > 0) {
					ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		suggestBusinessNameBasedSalesExecutive : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var salesExecutiveVal = $('#salesExecutive').val();
			var businessNameVal = $('#businessName').val();
			$.post('reports.json','action=get-all-business-names-based-sales-executive&salesExecutiveVal=' + salesExecutiveVal +'&businessNameVal=' +businessNameVal, function(obj) {
								$.loadAnimation.end();
								suggestionsDiv.html('');
								var data = obj.result.data;
								if (data != undefined) {
									var htmlStr = '<div>';
									for ( var loop = 0; loop < data.length; loop = loop + 1) {
										htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">'
												+ data[loop]
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
											$('#businessName').attr('value',
													$(this).attr('businessName'));
											suggestionsDiv.hide();
										});
							});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#sales-action-show').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('salesExecutiveSalesReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			        
			        $("#report-show-dialog").dialog({
		    			autoOpen: true,
		    			height: 'auto',
		    			width: 950,
		    			modal: true,
		    			buttons : {
							Close : function() {
								$(this).dialog('close');
							}
						}
		    		});
			      
				});
				
			});
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
			$('.btn-clear').click(function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales_executive_wise_reports.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').click(function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales_executive_wise_reports.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sales-action-pdf').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()=== false){
						return;
						event.preventDefault();
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()=== false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-wise-report-form').attr('action','salesExecutiveSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
					
				});
			$('#sales-action-xls').click(function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()===false){
						event.preventDefault();
						return;
					}
				}
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()===false){
						return;
					}
				}
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sales-wise-report-form').attr('action','salesExecutiveSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
				});
			
		}
		
};
var dynamicReportsHandler = {
		load : function(){
			var map = {};
			var crArray =[];
			var inputArray = [];
			var outputArray = [];
			
			$('#type').change(function(){
				if($('#type').val() == "Transaction types"){
					$('#transactionTypes').show();
				}else {
					$('#transactionTypes').hide();
					$('#commonFields').show();
					$('#dnInputFields').hide();
					$('#CCFields').hide();
					$('#srFields').hide();
					$('#JouFields').hide();
				}
			});
			$('#fieldType').click(function(){
				if($('#fieldType').val() == "Products"){
					
				}
				dynamicReportsHandler.getDnData();
			});
			$('#transactionType').change(function(){
					$('#numRelated').hide();
					$('#textRelated').hide();
					$('#criteriaVal').hide();
				if($('#transactionType').val()=="Delivery Note"){
					$('#CCFields').hide();
					$('#JouFields').hide();
					$('#srFields').hide();
					$('#dnInputFields').show();
					$('#commonFields').hide();
				}else if($('#transactionType').val() == "Sales Return"){
					$('#dnInputFields').hide();
					$('#CCFields').hide();
					$('#JouFields').hide();
					$('#srFields').show();
					$('#commonFields').hide();
				}else if($('#transactionType').val() == "Cash Collections"){
					$('#dnInputFields').hide();
					$('#JouFields').hide();
					$('#srFields').hide();
					$('#CCFields').show();
					$('#commonFields').hide();
				}else if($('#transactionType').val() == "Journal"){
					$('#dnInputFields').hide();
					$('#CCFields').hide();
					$('#srFields').hide();
					$('#JouFields').show();
					$('#commonFields').hide();
				}
			});
			/*$('.Fields').click(function(){
				alert("dfg")
				var id = $(this).attr('id');
				if($('#'+id).val() == "businessName"||$('#'+id).val() == "invoiceName"||$('#'+id).val() =="productName"||$('#'+id).val()=="paymentType"
					||$('#'+id).val()=="journalType"||$('#'+id).val()=="description"||$('#'+id).val() == "dayBookNo"||$('#'+id).val() == "invoiceNo"||$('#'+id).val() == "batchNumber"){
					alert('fd')
					$('#textRelated').show();
					$('#numRelated').hide();
				}else{
					$('#textRelated').hide();
					$('#numRelated').show();
				}
			});*/
			$('.criteria').change(function(){
				$('#criteriaVal').show();
				var index = jQuery.inArray($('#dn_input_Fields').val(),crArray);
				if(index == -1){
					$('.btn-save').show();
				}else{
				}
			});
			$("table tr td:nth-child(2)").each(function (i,row) {
				$(row).find('input:checkbox').click(function(){
					if($(row).find('input:checkbox').is(":checked") == true){
						$(row).find('input:checked').closest('tr').find('.criteriadrop').removeAttr("disabled");
						$(row).find('input:checked').closest('tr').find('.criteriaVal').removeAttr('readonly');
					}else{
						$(row).find('input:checked').closest('tr').find('.criteriadrop').addAttr("disabled",'disabled');
						$(row).find('input:checked').closest('tr').find('.criteriaVal').addAttr('readonly','readonly');
					}
				});
			});
			$('#sales-action-show').click(function(){
				var transactionType = $('#transactionType').val();
				var reportFormat = 'html';
				//if($('#transactionType').val() == "Delivery Note"){
					$("table tr td:nth-child(2)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 inputArray.push(inputValues)
						 }
						
					});
					$("table tr td:nth-child(3)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 outputArray.push(inputValues)
						 }
					});
					 $("table tr td:nth-child(2)").each(function (i,row) {
						 var criteria =$(row).find('input:checked').closest('tr').find('.criteriadrop').val();
						 var criteriaVal = $(row).find('input:checked').closest('tr').find('.criteriaVal').val();
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 
						 if(criteria != undefined && criteriaVal != undefined){
							 map[inputValues] = criteria+","+criteriaVal;
						 }
					});
					 $.post('dynamicReports.json','action=display-dynamic-reprots&transactionType='+transactionType+'&inputs='+inputArray+'&outputs='+outputArray+'&criteria='+JSON.stringify(map)+'&reportFormat='+reportFormat,function(obj){
							$('#report-container').html('');
					        $('#report-container').html(obj);
					        
					        $("#report-show-dialog").dialog({
				    			autoOpen: true,
				    			height: 'auto',
				    			width: 950,
				    			modal: true,
				    			buttons : {
									Close : function() {
										$(this).dialog('close');
									}
								}
				    		});
					 });
						inputArray.length=0;
						outputArray.length = 0;
						crArray.length=0;
				//}
			});
			$('#sales-action-pdf').click(function(cfg) {
				var transactionType = $('#transactionType').val();
				//if($('#transactionType').val() == "Delivery Note"){
					$("table tr td:nth-child(2)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 inputArray.push(inputValues)
						 }
						
					});
					$("table tr td:nth-child(3)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 outputArray.push(inputValues)
						 }
					});
					 $("table tr td:nth-child(2)").each(function (i,row) {
						 var criteria =$(row).find('input:checked').closest('tr').find('.criteriadrop').val();
						 var criteriaVal = $(row).find('input:checked').closest('tr').find('.criteriaVal').val();
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 
						 if(criteria != undefined && criteriaVal != undefined){
							 map[inputValues] = criteria+","+criteriaVal;
						 }
					});
				  var reportFormat = 'pdf';
				  $('#dynamic-report-form').attr('action','dynamicReports.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#transactionType').attr('value',transactionType);
					$('#inputs').attr('value',inputArray);
					$('#outputs').attr('value',outputArray);
					$('#criteria').attr('value',JSON.stringify(map));
					$('#dynamic-report-form').submit();
					
				});
			$('#sales-action-xls').click(function(cfg) {
				alert("xls")
				$("table tr td:nth-child(2)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 inputArray.push(inputValues)
						 }
						
					});
					$("table tr td:nth-child(3)").each(function (i,row) {
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 if(inputValues != undefined&&inputValues != ""){
							 outputArray.push(inputValues)
						 }
					});
					 $("table tr td:nth-child(2)").each(function (i,row) {
						 var criteria =$(row).find('input:checked').closest('tr').find('.criteriadrop').val();
						 var criteriaVal = $(row).find('input:checked').closest('tr').find('.criteriaVal').val();
						 var inputValues = $(row).find('input:checked').closest('tr').attr("id");
						 
						 if(criteria != undefined && criteriaVal != undefined){
							 map[inputValues] = criteria+","+criteriaVal;
						 }
					});
				/*
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }*/
				  var reportFormat = 'csv';
				 $('#dynamic-report-form').attr('action','dynamicReports.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#transactionType').attr('value',transactionType);
					$('#inputs').attr('value',inputArray);
					$('#outputs').attr('value',outputArray);
					$('#criteria').attr('value',JSON.stringify(map));
					$('#dynamic-report-form').submit();
				});
		},
		getDnData : function(){
			if($('#fieldType').val() == "Products"){
				$('#productval').show();
			}if($('#fieldType').val() == "Customers"){
				$('#bizVal').show();
			}
		
			$.post('reports.json','action=get-dn-data',function(obj){
				var result = obj.result.data;
				if($('#fieldType').val() == "Products"){
					$.each(result, function(i) {
						$('#productName').append('<option value="' + result[i].productName
								+ '">' + result[i].productName + '</option>');
					});
				}
				if($('#fieldType').val() == "Customers"){
					$.each(result, function(i) {
						$('#businessName').append('<option value="' + result[i].businessName
								+ '">' + result[i].businessName + '</option>');
					});
				}
				
			});
		},
	
};
