var ReportsHandler = {
		initPageLinks : function() {
			$('#product-wise-report').pageLink({
				container : '.report-page-container',
				url : 'mgt-user/productwise_report_show.jsp'
			});
		}	
};

var ProductWiseReportHandler = {
		
		load : function() {
			
			$.post('reports.json','action=get-assigned-organizations', function(obj) {
				var organizationOptions = $("#organization");
				$.each(obj.result.data, function(i, organization) {
					organizationOptions.append('<option value="' + organization
							+ '">' + organization + '</option>');

				});
			});
			
			$('#organization').change(function() {
				var organization = $('#organization').val();
				$.post('reports.json','action=get-all-product-names&organization='+organization, function(obj) {
					var productNameOptions = $("#productName");
					$.each(obj.result.data, function(i, productName) {
						productNameOptions.append('<option value="' + productName
								+ '">' + productName + '</option>');

					});
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
