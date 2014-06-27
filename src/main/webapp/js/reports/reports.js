var ReportsHandler = {
		initPageLinks : function() {
			$('#customer-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/customer-category/customerwise_report_show.jsp'
			});
			$('#product-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/product-category/productwise_report_show.jsp'
			});

			$('#sales-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/saleswise_report_show.jsp'
			});
			$('#sales-return-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/salesreturnwise_report_show.jsp'
			});
			$('#sales-executive-expenditure-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/sales_executive_expenditure_wise_report_show.jsp'
			});
			$('#sales-executive-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/sales_executive_wise_reports.jsp'
			});
			//adding page links for new reports
			$('#factory-product-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/product-category/factory_product_wise_report_show.jsp'
			});
			$('#product-sales-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/product-category/product_wise_sales_report_show.jsp'
			});
			$('#sales-executive-customer-wise-sales-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/SLE_customer_wise_sales_show.jsp'
			});
		    $('#sales-executive-sales-wise-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/sales-category/SLE_sales_wise_report.jsp'
			});
			$('#product-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/product-category/product_report_wise_show.jsp'
			});
			$('#customer-product-sales-report').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/customer-category/customer_product_sales_show.jsp'
			});
			$('#product-report-sales-executive-wise').pageLink({
				container : '.report-page-container',
				url : 'my-sales/reports/product-category/product_report_sle_show.jsp'
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
/*	validateCustomerName: function() {
		var result = true;
		var customerName = $('#customerName').val();
		$.ajax({type : "POST",
			url : 'reports.json',
			data : 'action=check-customer-name-availability&customerName='+customerName,
			async : false,
			success : function(obj) {
			var data = obj.result.data;
			if(data != "true") {
				$('#customerNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#customerName").focus(function(event){
					$('#customerNameValid').empty();
					 $('#cname_pop').show();
			});
			$("#customerName").blur(function(event){
				 $('#cname_pop').hide();
			});
			result = false;
			}
		},
		});
		return result;
},*/
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
		var salesExecutiveVal = $('#salesExecutive').val();
		var businessNameVal = $('#businessName').val();
		if (businessNameVal == undefined) {
			businessNameVal = 'null';
		}
		$.post('reports.json','action=get-all-sales-executives&salesExecutiveVal=' + salesExecutiveVal +'&businessNameVal=' +businessNameVal,function(obj) {
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
	},
	suggestSalesExecutiveBasedOnCustomerName : function(thisInput) {
		var suggestionsDiv = $('#salesexecutive-name-suggestions');
		var salesExecutiveVal = $('#salesExecutive').val();
		var customerNameVal = $('#customerName').val();
		if (customerNameVal == undefined) {
			customerNameVal = 'null';
		}
		$.post('reports.json','action=get-all-sales-executives-basedon-customername&salesExecutiveVal=' + salesExecutiveVal +'&customerNameVal=' +customerNameVal,function(obj) {
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
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
			
			//When autocomplete changes a value, it fires a autocompletechange event, not the change event
			$('#businessName').change(function() {
				if($(this).val().length > 0) {
					//ReportsHandler.validateBusinessName();
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
			$('#action-show').live('click', function(cfg) {
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
		    			title: 'Customer Wise Report',
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
			
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/customer-category/customerwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/customer-category/customerwise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#action-pdf').live('click', function(cfg) {
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
			$('#action-xls').live('click', function(cfg) {
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
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		registorReportShowEvents : function(cfg) {
			$('#product-action-show').live('click', function(cfg) {
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
		    			title: 'Product Wise Report',
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/product-category/productwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');

						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
									var url = "my-sales/reports/product-category/productwise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#product-action-pdf').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-wise-report-form').attr('action','productWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
					
				});
			$('#product-action-xls').live('click', function(cfg) {
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
					//ReportsHandler.validateSalesExecutive();
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
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/saleswise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/saleswise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sales-action-pdf').live('click', function(cfg) {
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
			$('#sales-action-xls').live('click', function(cfg) {
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
					//ReportsHandler.validateSalesExecutive();
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
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
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
			
			$('#sales-return-action-pdf').live('click', function(cfg) {
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
			$('#sales-return-action-xls').live('click', function(cfg) {
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
			
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/salesreturnwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/salesreturnwise_report_show.jsp";
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
					//ReportsHandler.validateSalesExecutive();
				}
			});
			
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
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
			
			$('#sales-expenditure-action-pdf').live('click', function(cfg) {
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
			$('#sales-expenditure-action-xls').live('click', function(cfg) {
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
			
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/sales_executive_expenditure_wise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/sales_executive_expenditure_wise_report_show.jsp";
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
					//ReportsHandler.validateSalesExecutive();
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
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
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
				  if (!$('#sales-executive-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-executive-wise-report-form').serialize();
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/sales_executive_wise_reports.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/sales_executive_wise_reports.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sales-action-pdf').live('click', function(cfg) {
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
				  if (!$('#sales-executive-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-executive-wise-report-form').attr('action','salesExecutiveSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-executive-wise-report-form').submit();
					
				});
			$('#sales-action-xls').live('click', function(cfg) {
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
					$('#sales-executive-wise-report-form').attr('action','salesExecutiveSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-executive-wise-report-form').submit();
				});
			
		}
		
};
var FactoryProductWiseReportHandler = {
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
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
			
		},
		registorReportShowEvents : function(cfg) {
			$('#factory-product-action-show').live('click', function(cfg) {
				  if (!$('#factory-product-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#factory-product-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('factoryProductWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			        
			        $("#report-show-dialog").dialog({
		    			autoOpen: true,
		    			title: 'Factory Product Wise Report',
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/product-category/factory_product_wise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');

						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/product-category/factory_product_wise_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#factory-product-action-pdf').live('click', function(cfg) {
				  if (!$('#factory-product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#factory-product-wise-report-form').attr('action','factoryProductWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#factory-product-wise-report-form').submit();
					
				});
			$('#factory-product-action-xls').live('click', function(cfg) {
				  if (!$('#factory-product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#factory-product-wise-report-form').attr('action','factoryProductWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#factory-product-wise-report-form').submit();
				});
			
		}
};
var ProductSalesWiseReportHandler = {
		
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
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#product-sales-action-show').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-wise-sales-report-form').validate()) {
				        return;
				      }
				var paramString = $('#product-wise-sales-report-form').serialize();
				var reportFormat = 'html';
				$.post('productSalesWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/product-category/product_wise_sales_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/product-category/product_wise_sales_report_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#product-sales-action-pdf').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-wise-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-wise-sales-report-form').attr('action','productSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-sales-report-form').submit();
					
				});
			$('#product-sales-action-xls').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-wise-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#product-wise-sales-report-form').attr('action','productSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-sales-report-form').submit();
				});
			
		}
	};
var SLECustomerWiseSalesReportHandler = {
		
		load : function() {
			$('#salesExecutive').click(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutiveBasedOnCustomerName(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				ReportsHandler.suggestSalesExecutiveBasedOnCustomerName(thisInput);
			});

			$('#salesExecutive').focusout(function() {
				$('#salesexecutive-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#salesexecutive-name-suggestions').hide();
				});
			});
			//When autocomplete changes a value, it fires a autocompletechange event, not the change event
			$('#salesExecutive').change(function() {
				if($(this).val().length > 0) {
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SLECustomerWiseSalesReportHandler.suggestCustomerNameBasedOnSalesExecutive(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				SLECustomerWiseSalesReportHandler.suggestCustomerNameBasedOnSalesExecutive(thisInput);
			});

			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			//When autocomplete changes a value, it fires a autocompletechange event, not the change event
			$('#businessName').change(function() {
				if($(this).val().length > 0) {
					//ReportsHandler.validateBusinessName();
				}
			});
			
			$('#region').click(function() {
				var thisInput = $(this);
				$('#region-name-suggestions').show();
				SLECustomerWiseSalesReportHandler.suggestRegionNameBasedOnCustomerNameAndSalesExecutiveName(thisInput);
			});
			$('#region').keyup(function() {
				var thisInput = $(this);
				$('#region-name-suggestions').show();
				SLECustomerWiseSalesReportHandler.suggestRegionNameBasedOnCustomerNameAndSalesExecutiveName(thisInput);
			});

			$('#region').focusout(function() {
				$('#region-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#region-name-suggestions').hide();
				});
			});
			//When autocomplete changes a value, it fires a autocompletechange event, not the change event
			$('#region').change(function() {
				if($(this).val().length > 0) {
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		suggestCustomerNameBasedOnSalesExecutive : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var salesExecutiveVal = $('#salesExecutive').val();
			var businessNameVal = $('#businessName').val();
			$.post('reports.json','action=get-all-customer-names-based-sales-executive&salesExecutiveVal=' + salesExecutiveVal +'&businessNameVal=' +businessNameVal, function(obj) {
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
		
		suggestRegionNameBasedOnCustomerNameAndSalesExecutiveName : function(thisInput) {
			var suggestionsDiv = $('#region-name-suggestions');
			var salesExecutiveVal = $('#salesExecutive').val();
			var businessNameVal = $('#businessName').val();
			var regionNameVal = $('#region').val();
			$.post('reports.json','action=get-all-regions-based-customer-salesexecutive-name&businessNameVal=' + businessNameVal +'&regionNameVal=' +regionNameVal +'&salesExecutiveVal=' +salesExecutiveVal, function(obj) {
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
											+ 'No Region Names' + '</div>');
								}
								suggestionsDiv.css('left',thisInput.position().left);
								suggestionsDiv.css('top',thisInput.position().top + 25);
								suggestionsDiv.show();
								$('.select-teacher').click(
										function() {
											thisInput.val($(this).html());
											thisInput.attr('region', $(this)
													.attr('region'));
											$('#region').attr('value',
													$(this).attr('region'));
											suggestionsDiv.hide();
											var regionName = $('#region').val();
										$.post('reports.json','action=get-customer-region-name&customerName='+customerName, function(obj) {
											 var customerRegionName=obj.result.data;					
												$('#region').attr('value',customerRegionName);
											});
										});
							});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#sle-customer-sales-action-show').live('click', function(cfg) {
				/*if($('#salesExecutive').val().length == 0 && $('#customerName').val().length == 0 && $('#region').val().length == 0){
			    alert("Select any one of them : Customer Name,Sales Executive,Region");
				}else{*/
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
					 if (!$('#sle-customer-wise-sales-report-form').validate()) {
					        return;
					      }
				var paramString = $('#sle-customer-wise-sales-report-form').serialize();
				var reportFormat = 'html';
				$.post('sleCustomerSalesWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
				//}
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/SLE_customer_wise_sales_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/SLE_customer_wise_sales_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sle-customer-sales-action-pdf').live('click', function(cfg) {
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
				  if (!$('#sle-customer-wise-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sle-customer-wise-sales-report-form').attr('action','sleCustomerSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sle-customer-wise-sales-report-form').submit();
					
				});
			$('#sle-customer-sales-action-xls').live('click', function(cfg) {
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
				  if (!$('#product-wise-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sle-customer-wise-sales-report-form').attr('action','sleCustomerSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sle-customer-wise-sales-report-form').submit();
				});
			
		}
	};

var SalesExecutiveSalesWiseReportHandler = {
		
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
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#sle-sales-action-show').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sle-sales-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sle-sales-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('sleSalesWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/sales-category/SLE_sales_wise_report.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/sales-category/SLE_sales_wise_report.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#sle-sales-action-pdf').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sle-sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sle-sales-wise-report-form').attr('action','sleSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sle-sales-wise-report-form').submit();
					
				});
			$('#sle-sales-action-xls').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#sle-sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sle-sales-wise-report-form').attr('action','sleSalesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sle-sales-wise-report-form').submit();
				});
			
		}
	};
	

var ProductReportWiseReportHandler = {
		
		load : function() {
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#product-report-action-show').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#product-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('productReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/product-category/product_report_wise_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/product-category/product_report_wise_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#product-report-action-pdf').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-wise-report-form').attr('action','productReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
					
				});
			$('#product-report-action-xls').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#product-wise-report-form').attr('action','productReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
				});
		}
	};

var CustomerProductSalesReportHandler = {
		
		load : function() {
			$('#businessName').click(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				CustomerProductSalesReportHandler.suggestCustomerName(thisInput);
			});
			$('#businessName').keyup(function() {
				var thisInput = $(this);
				$('#business-name-suggestions').show();
				CustomerProductSalesReportHandler.suggestCustomerName(thisInput);
			});

			$('#businessName').focusout(function() {
				$('#business-name-suggestions').animate({
					display : 'none'
				}, 1000, function() {
					$('#business-name-suggestions').hide();
				});
			});
			//When autocomplete changes a value, it fires a autocompletechange event, not the change event
			$('#businessName').change(function() {
				if($(this).val().length > 0) {
					//ReportsHandler.validateBusinessName();
				}
			});
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		suggestCustomerName : function(thisInput) {
			var suggestionsDiv = $('#business-name-suggestions');
			var businessNameVal = $('#businessName').val();
			$.post('reports.json','action=get-all-business-names&businessNameVal=' +businessNameVal, function(obj) {
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
											+ 'No Customer Business Names' + '</div>');
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
											var customerName = $('#businessName').val();
										});
							});
		},
		
		
		registorReportShowEvents : function(cfg) {
			$('#customer-product-sales-action-show').live('click', function(cfg) {
					if($('#businessName').val().length > 0) {
						if(ReportsHandler.validateBusinessName()== false){
							alert("false");
							return;
						}
					}
					 if (!$('#customer-product-sales-report-form').validate()) {
					        return;
					      }
				var paramString = $('#customer-product-sales-report-form').serialize();
				var reportFormat = 'html';
				$.post('customerProductSalesReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/customer-category/customer_product_sales_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/customer-category/customer_product_sales_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#customer-product-sales-action-pdf').live('click', function(cfg) {
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#customer-product-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#customer-product-sales-report-form').attr('action','customerProductSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#customer-product-sales-report-form').submit();
					
				});
			$('#customer-product-sales-action-xls').live('click', function(cfg) {
				if($('#businessName').val().length > 0) {
					if(ReportsHandler.validateBusinessName()== false){
						return;
					}
				}
				  if (!$('#customer-product-sales-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#customer-product-sales-report-form').attr('action','customerProductSalesReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#customer-product-sales-report-form').submit();
				});
			
		}
	};

var ProductReportSLEWiseReportHandler = {
		
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
			$('#reportType').change(function() {
				var reportTypeVal = $('#reportType').val();
				if(reportTypeVal != "select") {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
		},
		
		registorReportShowEvents : function(cfg) {
			$('#product-report-sle-action-show').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-report-sle-report-form').validate()) {
				        return;
				      }
				var paramString = $('#product-report-sle-report-form').serialize();
				var reportFormat = 'html';
				$.post('productReportSLE.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
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
			$('.btn-clear').live('click', function(cfg) {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.report-page-container';
			    			var url = "my-sales/reports/product-category/product_report_sle_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click',function(cfg) {
						$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									var container ='.report-page-container';
					    			var url = "my-sales/reports/product-category/product_report_sle_show.jsp";
					    			$(container).load(url);
					    			$(this).dialog('close');
								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#product-report-sle-action-pdf').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-report-sle-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-report-sle-report-form').attr('action','productReportSLE.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-report-sle-report-form').submit();
					
				});
			$('#product-report-sle-action-xls').live('click', function(cfg) {
				if($('#salesExecutive').val().length > 0) {
					if(ReportsHandler.validateSalesExecutive()== false){
						return;
						event.preventDefault();
					}
				}
				  if (!$('#product-report-sle-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#product-report-sle-report-form').attr('action','productReportSLE.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-report-sle-report-form').submit();
				});
			
		}
	};
