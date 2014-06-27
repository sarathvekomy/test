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
				  if (!$('#customer-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#customer-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('customerWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			      
				});
				
				//$('#customer-wise-report-form').clearForm();
				
				
			});
			
			$('.btn-clear').live('click', function(cfg) {
				$('#customer-wise-report-form').clearForm();
				$('#report-container').html('').clearForm();
			});

			$('.btn-cancel').live(
					'click',
					function(cfg) {
						$('#error-message').html(
								Msg.YOU_WILLLOOSE_YOUR_UNSAVED_DATA_LABEL);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									$(this).dialog('close');
									$('.report-page-container').html('');
									$('#report-switch').click();
//									$('.button-action-add').show();
//									$('.page-screen-title').html('');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#action-pdf').live('click', function(cfg) {
				  if (!$('#customer-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#customer-wise-report-form').attr('action','customerWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#customer-wise-report-form').submit();
					
				});
			$('#action-xls').live('click', function(cfg) {
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
			$('#action-show').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#product-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('productWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			      
				});
				
				//$('#customer-wise-report-form').clearForm();
				
				
			});
			
			$('.btn-clear').live('click', function(cfg) {
				$('#product-wise-report-form').clearForm();
				$('#report-container').html('').clearForm();
			});

			$('.btn-cancel').live(
					'click',
					function(cfg) {
						$('#error-message').html(
								Msg.YOU_WILLLOOSE_YOUR_UNSAVED_DATA_LABEL);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									$(this).dialog('close');
									$('.report-page-container').html('');
									$('#report-switch').click();
//									$('.button-action-add').show();
//									$('.page-screen-title').html('');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#action-pdf').live('click', function(cfg) {
				  if (!$('#product-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#product-wise-report-form').attr('action','productWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#product-wise-report-form').submit();
					
				});
			$('#action-xls').live('click', function(cfg) {
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
				SalesWiseReportHandler.suggestSalesExecutive(thisInput);
			});
			$('#salesExecutive').keyup(function() {
				var thisInput = $(this);
				$('#salesexecutive-name-suggestions').show();
				SalesWiseReportHandler.suggestSalesExecutive(thisInput);
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
				if(reportTypeVal != -1) {
					$('#endDate').attr("disabled",true);
				} else {
					$('#endDate').removeAttr("disabled");
				}
			});
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
											+ 'No Business Names' + '</div>');
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
		registorReportShowEvents : function(cfg) {
			$('#action-show').live('click', function(cfg) {
				  if (!$('#sales-wise-report-form').validate()) {
				        return;
				      }
				var paramString = $('#sales-wise-report-form').serialize();
				var reportFormat = 'html';
				$.post('salesWiseReport.json', paramString+'&reportFmt='+reportFormat, function(obj) {	
					$('#report-container').html('');
			        $('#report-container').html(obj);
			      
				});
				
			});
			
			$('.btn-clear').live('click', function(cfg) {
				$('#sales-wise-report-form').clearForm();
				$('#report-container').html('').clearForm();
			});

			$('.btn-cancel').live(
					'click',
					function(cfg) {
						$('#error-message').html(
								Msg.YOU_WILLLOOSE_YOUR_UNSAVED_DATA_LABEL);
						$("#error-message").dialog({
							resizable : false,
							height : 140,
							title : "Confirm",
							modal : true,
							buttons : {
								'Ok' : function() {
									$(this).dialog('close');
									$('.report-page-container').html('');
									$('#report-switch').click();
//									$('.button-action-add').show();
//									$('.page-screen-title').html('');

								},
								'Cancel' : function() {
									$(this).dialog('close');
								}
							}
						});
					});
			
			$('#action-pdf').live('click', function(cfg) {
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'pdf';
					$('#sales-wise-report-form').attr('action','salesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
					
				});
			$('#action-xls').live('click', function(cfg) {
				  if (!$('#sales-wise-report-form').validate()) {
					  return;
				  }
				  var reportFormat = 'csv';
					$('#sales-wise-report-form').attr('action','salesWiseReport.json');
					$('#reportFormat').attr('value',reportFormat);
					$('#sales-wise-report-form').submit();
				});
			
		}
};