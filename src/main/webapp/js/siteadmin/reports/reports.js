var SiteAdminReportsHandler = {
		initPageLinks : function() {
			$('#product-wise-report').pageLink({
				container : '.manage-user-page-container',
				url : 'siteadmin/reports/productwise_report_show.jsp'
			});
		}	
};
var AdminProductWiseReportHandler = {
		
		load : function() {
			
			$.post('reports.json','action=get-all-organizations', function(obj) {
				var organizationOptions = $("#organization");
				$.each(obj.result.data, function(i, organization) {
					organizationOptions.append('<option value="' + organization
							+ '">' + organization + '</option>');

				});
			});
			
			$('#organization').change(function() {
				$("#productName").children('option:not(:first)').remove();
				var organization = $('#organization').val();
				$.post('reports.json','action=get-all-product-names&organization='+organization, function(obj) {
					var productNameOptions = $("#productName");
					productNameOptions.empty();
					productNameOptions.append('<option value="ALL">ALL</option>');
					$.each(obj.result.data, function(i, productName) {
						productNameOptions.append('<option value="' + productName
								+ '">' + productName + '</option>');

					});
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
			$('#action-show').live('click', function(cfg) {
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
							var container ='.manage-user-page-container';
			    			var url = "siteadmin/reports/productwise_report_show.jsp";
			    			$(container).load(url);
			    			$(this).dialog('close');
						},
						'Cancel' : function() {
							$(this).dialog('close');
						}
					}
				});
			});

			$('.btn-cancel').live('click', function(cfg) {
				$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);
				$("#error-message").dialog({
					resizable : false,
					height : 140,
					title : "Confirm",
					modal : true,
					buttons : {
						'Ok' : function() {
							var container ='.manage-user-page-container';
			    			var url = "siteadmin/reports/productwise_report_show.jsp";
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
