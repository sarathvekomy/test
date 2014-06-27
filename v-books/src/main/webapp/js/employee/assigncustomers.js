var assignCustomerHandler={
		initPageLinks: function() {
			$('#assign-customers').pageLink({container:'.employee-page-container', url:'employee/assign_customers.jsp'});
			$('#search-assigned-customers').pageLink({container:'.employee-page-container',url:'employee/search_assigned_customers.jsp'})
	},	
	assignCustomereSteps:['#employee-form','#employee-form'],
	assigncustomerUrl:['asignCustomer.json','asignCustomer.json'],
	assigncustomerStepCount: 0,
		
	load: function() {
		$('#uName').click(function(){
			var thisInput = $(this);
			$('#employee-name-suggestions').show();
			assignCustomerHandler.suggestEmployeeNames(thisInput);
		});
		$('#uName').keyup(function(){
			var thisInput = $(this);
			$('#employee-name-suggestions').show();
			assignCustomerHandler.suggestEmployeeNames(thisInput);
		});
		$('#uName').focusout(function() {
			$('#employee-name-suggestions').animate({
				display : 'none'
			}, 100, function() {
				//$('#employee-name-suggestions').hide();
			});
		});
		$("#employee-name-suggestions").click(function(){
			assignCustomerHandler.searchLocalities();
		});
	},
	suggestEmployeeNames : function(thisInput){
			var suggestionsDiv = $('#employee-name-suggestions');
			var val = $('#uName').val();
			$.post('assignCustomers.json','action=get-employee-names&employeeNameVal=' + val,function(obj) {
								$.loadAnimation.end();
								suggestionsDiv.html('');
								var data = obj.result.data;
								if (data.length > 0) {
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
											+ 'No  Names' + '</div>');
								}
								suggestionsDiv.css('left',
										thisInput.position().left);
								suggestionsDiv.css('top',
										thisInput.position().top + 25);
								suggestionsDiv.show();
								$('.select-teacher').click(
										function() {
											$('#localities-search-results-list').empty();
											$('#business-search-results-list').empty();
											$.post('assignCustomers.json','action=remove-session',function(obj){
											
											});
											thisInput.val($(this).html());
											thisInput.attr('uName', $(this)
													.attr('uName'));
											$('#uName').attr('value',
													$(this).attr('uName'));
											suggestionsDiv.hide();
										});
		});
	},
	addColor: function(number) {
		if(number%2 !=0){
			$('#row-'+number).css({'background-color' : 'LightGray'});
		}
		else
		$('#row-'+number).css({'background-color' : 'FloralWhite'});
		
	},
	searchLocalities:function(){
		$.post('assignCustomers.json','action=get-localities',function(obj){
			var data = obj.result.data;
	   		$('#localities-search-results-list').html('');
	        if(data!=''){
			var alternate = false;
			for(var loop=0;loop<data.length;loop=loop+1){
				if(alternate) {
					var rowstr = '<a href="#"><div class="green-result-row alternate" id="locality-row" style="height:23px; background:white; color:black;">';
				} else {
					rowstr = '<a href="#"><div class="green-result-row" id="locality-row" style="height:23px; background:white; color:black;">';
				}
				alternate = !alternate;
				rowstr = rowstr + '<div class="green-result-col-1" style="padding: 0px 0px 0px 3px;">'+
				'<div class="result-title" id = "locality-val-'+loop+'" style = "font-weight:normal; font-size:13px; padding-top:3px;">' + data[loop] + '</div>' +
				'</div>'+
				'</div>'+
				'</a>';
				$('#localities-search-results-list').append(rowstr);
			}
			};
			assignCustomerHandler.searchBusinessNames();
		});
	},
	
	searchBusinessNames:function(){
		$('#locality-row').live('click',function(){
			$('#localities-search-results-list').find('.selected').each(function(){
				$(this).removeClass('selected');
				var id = $(this).find('.result-title').attr('id');
		        $(this).find('#'+id).css("font-size","13px");
		    });
			$(this).addClass('selected');
			var id = $(this).find('.result-title').attr('id');
			$(this).find('#'+id).css("font-size","16px");
			var locality = $('#'+id).text();
			$.post('assignCustomers.json','action=get-customers-by-locality&locality='+locality,function(obj){
				var data = obj.result.data;
		   		$('#business-search-results-list').html('');
		        if(data!=''){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					if(alternate) {
						var rowstr = '<a href="#"><div class="green-result-row alternate" id="business-row" style="height:23px; background:white; color:black;">';
					} else {
						rowstr = '<a href="#"><div class="green-result-row" id="business-row" style="height:23px; background:white;  color:black;">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1" style="padding: 0px 0px 0px 3px;">'+
					'<div class="result-title" id = "business-val-'+loop+'" style = "font-weight:normal; font-size:13px; padding-top:3px;">' + data[loop]  + '</div>' +
					'</div>'+
					'</div>'+
					'</a>';
					$('#business-search-results-list').append(rowstr);
				}
				};
				var salesExecutive = $('#uName').val();
				$('#business-search-results-list').find('#business-row').each(function(){
			        var id = $(this).find('.result-title').attr('id');
			        var businessName = $('#'+id).text();
			        $.post('assignCustomers.json','action=check-availability-for-businessName&businessName='+businessName,function(obj){
						var result=obj.result.data;
						if(result == salesExecutive){
							$('#'+id).css("color","#33AD5C");
						    $('#'+id).parents('a').css('cursor', 'default');
						    $('#'+id).parents('.green-result-row').addClass('userselected');
						}else if(result.length > 0){
							$('#'+id).css("color","#888888");
						    $('#'+id).parents('a').css('cursor', 'default');
						    $('#'+id).parents('.green-result-row').addClass('dbselected');
						}
			        });
			        $.post('assignCustomers.json','action=session-availability-for-businessName&businessNameSession='+businessName+'&locality='+locality,function(obj){
			        	var result=obj.result.data;
						if(result == 'true'){
							 $('#'+id).css("color","blue");
						     $('#'+id).parents('a').css('cursor', 'move');
						     $('#'+id).parents('.green-result-row').addClass('sessionselected');
						}
			        });
			    });
			});
		});
		assignCustomerHandler.initSearchBusinessNameButtons();
	},
	initSearchBusinessNameButtons:function(){
		$('#business-row').live('click',function(){
			//var val=$(this).find('#business-val').text();
			//$(this).css("border-style","dotted").css("border-color","gray");
			//$(this).addClass('selected');
			var id = $(this).find('.result-title').attr('id');
			//$(this).find('#'+id).css("font-size","16px");
			var salesExecutive = $('#uName').val();
			var businessName = $('#'+id).text();
			$.post('assignCustomers.json','action=check-availability-for-businessName&businessName='+businessName,function(obj){
				var result=obj.result.data;
				if(result == salesExecutive){
					$('#error-message').html('Do you want to de-assign customer for this employee?');   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$.post('assignCustomers.json','action=deassign-customer&businessName='+businessName,function(obj){
							    	 var result=obj.result.data;
							    	 $('#'+id).css("color","black");
								     $('#'+id).parents('a').css('cursor', 'pointer');
								     $('#'+id).parents('.green-result-row').removeClass('userselected');
							     });
				    			$(this).dialog('close');
							},
							Cancel: function() {
								$(this).dialog('close');
							}
						}
					});
				    return false;
					/*$('#'+id).css("color","#33AD5C");
				    $('#'+id).parents('a').css('cursor', 'default');
				    $('#'+id).parents('.green-result-row').addClass('userselected');*/
				}else if(result.length > 0){
					//$(this).find('#'+id).attr('disabled','disabled');
					$('#business-search-results-list').find('.selected').each(function(){
						$(this).removeClass('selected');
				        var id = $(this).find('.result-title').attr('id');
				        $(this).find('#'+id).css("font-size","13px").css("color","#888888 ");
				        $(this).find('#'+id).parents('a').css('cursor', 'default');
				        //$(this).find('#'+id).removeTag('a');
				        $(this).find('#'+id).parents('href').remove();
				    });
				}
			});
			
			if(!$(this).hasClass('dbselected') && !$(this).hasClass('sessionselected') && !$(this).hasClass('userselected')){
				$(this).addClass('selected');
				$(this).find('#'+id).css("font-size","16px");
			}else if($(this).hasClass('sessionselected')){
				 $('#'+id).css("color","black");
			     $('#'+id).parents('a').css('cursor', 'pointer');
			     $('#'+id).parents('.green-result-row').removeClass('sessionselected');
			}
		});
		$('#action-assign-customer').click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(assignCustomerHandler.validateAssignCustomer() ==  false){
				return resultFailure;
			}
			var salesExecutive = $('#uName').val();
			var id = $('#localities-search-results-list').find('.selected').find('.result-title').attr('id');
			var locality = $('#'+id).text();
			var businessArray = [];
			$('#business-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        businessArray.push(businessName);
		    });
			$('#business-search-results-list').find('.sessionselected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        businessArray.push(businessName);
		    });
			var businessName = businessArray.join(',');
			$.post('assignCustomers.json','action=assign-customers&userName='+salesExecutive+'&businessArray='+businessName+'&locality='+locality,function(obj){
				var result=obj.result.data;
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
			});
		});
		$('#action-assign-multiple-customer').click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(assignCustomerHandler.validateAssignCustomer() ==  false){
				return resultFailure;
			}
			var salesExecutive = $('#uName').val();
			var id = $('#localities-search-results-list').find('.selected').find('.result-title').attr('id');
			var locality = $('#'+id).text();
			var businessArray = [];
			$('#business-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        businessArray.push(businessName);
		    });
			$('#business-search-results-list').find('.sessionselected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        businessArray.push(businessName);
		    });
			var businessNameString = businessArray.join(',');
			$.post('assignCustomers.json','action=assign-customers-multiple&userName='+salesExecutive+'&locality='+locality+'&businessNameString='+businessNameString,function(obj){
				var result=obj.result.data;
			});
			$('#business-search-results-list').empty();
			$('#localities-search-results-list').find('.selected').each(function(){
				$(this).removeClass('selected');
		        var id = $(this).find('.result-title').attr('id');
		        $(this).find('#'+id).css("font-size","13px");
		    });
		});
	},
	
	validateAssignCustomer:function(){
		var result=true;
		if(($('#uName').val()).length==0){
			$('#employeeNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#uName").focus(function(event){
				$('#employeeNameValid').empty();
				 $('#employeeName_pop').show();
			});
			$("#uName").blur(function(event){
				 $('#employeeName_pop').hide();
				 if(($('#uName').val()).length==0){
					 $('#employeeNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#uName").focus(function(event){
							$('#employeeNameValid').empty();
							 $('#employeeName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		return result;
	},
		initSearchAssignedCustomer : function(role) {
			$('#action-search-assigned-customer').click(function(){
				var thisButton = $(this);
				var paramString = $('#search-form').serialize();
				$('#search-results-list').ajaxLoader();
				$.post('assignCustomers.json', paramString, function(obj) {
					var data = obj.result.data;
			   		$('#search-results-list').html('');
			        if(data!=''){
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1){
						var dateFormat=assignCustomerHandler.formatDate(data[loop].createdOn);
						if(data[loop].firstName.length>25){
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate"style="height:70px">';
							} else {
								rowstr = '<div class="green-result-row"style=height:70px;>';
							}
						rowstr = rowstr + '<div class="green-result-col-1">'+
						'<div class="result-title">' + data[loop].userName  + '</div>' +
						'<div class="result-body"style="height:auto;">' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+' </span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action"style="height:55px;">' + 
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn btn-view" title="View EmployeeCustomer  Details"></div>'+
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
						
				         $('#search-results-list').append(rowstr);
						}else {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate">';
						} else {
							rowstr = '<div class="green-result-row">';
						}
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-title">' + data[loop].userName  + '</div>' +
					'<div class="result-body">' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">'+'</span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'"class="ui-btn btn-view" title="EmployeeCustomer  Details"></div>'+
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
					
			         $('#search-results-list').append(rowstr);
						}
						alternate = !alternate;
						};
					assignCustomerHandler.initSearchResultButtons();
					$('#search-results-list').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
				});
			});
			},
			initSearchResultButtons:function(){
				$('.btn-view').click(function() {
					var id = $(this).attr('id');
					var date = $(this).attr('date');
					var executive=$(this).attr('salesExecutive');
					$.post('employee/assign_customers_view.jsp', 'id='+id+'&date='+date+'&salesExecutive='+executive,
				        function(data){
						$('#employee-view-container').html(data);
							$("#employee-view-dialog").dialog('open');
				        }
			        );
				});
				$('#ps-exp-col').click(function() {
					setTimeout(function() {
						$('#search-results-list').jScrollPaneRemove();
						$('#search-results-list').jScrollPane({showArrows:true});
		            }, 0);
				});
				$('#action-clear').click(function() {
					$('#employee-search-form').clearForm();
					$('.employee-page-container').load('employee/search_assigned_customers.jsp');
				});
				$("#employee-view-dialog").dialog({
					autoOpen: false,
					height: 250,
					width: 550,
					modal: true,
					buttons: {
						Close: function() {
							$(this).dialog('close');
						}
					},
					close: function() {
						$('#employee-view-container').html('');
					}
				});
				$('.delete-organization').click(function() {
					var result=false;
					var arry=[];
					var id = $(this).attr('id');
					var date = $(this).attr('date');
					var executive=$(this).attr('salesExecutive');
					$.post('employee/delete_assign_customers.jsp', 'id='+id+'&date='+date+'&salesExecutive='+executive,
				        function(data){
						$('#employee-delete-container').html(data);
						$("#employee-delete-dialog").dialog({
			    			autoOpen: true,
			    			height: 250,
			    			width: 600,
			    			modal: true,
			    			buttons: {
			    			Delete: function() {
			    				$('.result-row').find('input[type=checkbox]').each(function(){
		    		    			  if($(this).is(':checked')){
		    		    				  var idArray=$(this).val();
		    		    				  arry.push(idArray);
		    		    			  }if(arry.length==0){
		    		    				  showMessage({title:'Warning', msg:'No Business Name is selected'});
		    		    				 return;
		    		    			  }
		    		    		  });
			    				if(arry.length>0){
			    					$.post('assignCustomers.json', 'idList='+arry+'&action=delete-employeecustomer',
				    						 function(obj) {
				    						$(this).successMessage({
				    							container : '.employee-page-container',
				    							data : obj.result.message
				    						});
				    					});
			    				}
			    				 $(this).dialog('close');
			    				},
			    		      Close: function() {
			    			      $(this).dialog('close');
			    		          }
			    			},
			    			close: function() {
			    				$('#employee-delete-container').html('');
			    			}
			    		});
						
				        }
			        );
				});
				$('.edit-icon').click(function(){
					var userName=$(this).attr('userName');
					var id=$(this).attr('id');
					var locality=$(this).attr('locality');
					var businessName=$(this).attr('businessName');
					  $.post('employee/edit_assigned_customers.jsp', 'userName='+userName+'&locality='+locality+'&id='+id,
						        function(data){
								$('.employee-page-container').html(data);
					        }
				        );
				});
				$('#search-assigned-customers').click(function() {
					$('.employee-page-container').load('employee/search_assigned_customers.jsp');
				});
				$('#assign-customers').click(function() {
					$('.employee-page-container').load('employee/assign_customers.jsp');
				});
				$('.select-teacher').click(function() {
							// $('#locality').empty();
							thisInput.val($(this).html());
							thisInput.attr('uName', $(this)
									.attr('uName'));
							$('#uName').attr('value',
									$(this).attr('userName'));
							suggestionsDiv.hide();
							var userName = $('#uName').val();
							$.post('assignCustomers.json','action=get-customer&userName='+userName,function(obj){
								var result=obj.result.data;
								if(result.length>0){
									for ( var loop = 0; loop < result.length; loop = loop + 1) {
										 document.form2.businessName.options[loop]=new Option(result[loop]);
										 } 
									}
								else{
									 showMessage({title:'Warning', msg:'NO Customers In the Selected Employee locality'});
									 $('#locality').empty();
								}
								
							});
						});
			},
			formatDate:function(inputFormat){
				var str=inputFormat.split(/[" "]/);
				dt=new Date(str[0]);
				return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
			},
			getLocalities :function(data){
				var id=$('#id').val();
				$.post('assignCustomers.json','action='+get-localities+'&id='+id,function(data){
					
				})
			},
			getCustomerNames :function(data){
				var action='&action=get-customernames-by-id'
				var paramString =$('#employee-customer-search-form').serialize()+action;
				$.post('assignCustomers.json',paramString,function(obj){
					var data=obj.result.data;
				if(data.length>0){
						for(var loop=0;loop<data.length;loop=loop+1){
							 document.form2.businessName.options[loop]=new Option(data[loop]);
							 $("select option").each(function(){
								$(this).attr("selected","selected");
								   $(this).attr("style","background-color:AliceBlue");
								});
						};
						
				}
				var locality=$('#localities').val();
				assignCustomerHandler.getAllCustomers(locality);
			      }
			);
				
			},
			getAllCustomers : function(locality){
				$.post('assignCustomers.json','action=get-customers-by-locality&locality='+locality,function(obj){
					var result=obj.result.data;
				        if(result.length>0){
							for(var loop=0;loop<result.length;loop=loop+1){
								/*document.form2.businessName.options[loop]= Option(result[loop]);*/
								$('#businessNames').append( $('<option></option').text(result[loop]));
							};
						}
				        var a = new Array();
				        $("select option").each(function(x){
				                test = false;
				                b = a[x] = $(this).val();
				                for (i=0;i<a.length-1;i++){
				                        if (b ==a[i]) test =true;
				                }
				                if (test) $(this).remove();
				        });
				});
				$('#btn-update').click(function(){
					var action='&action=update-assigned-customer';
						var x=$('#businessNames').val();
					var paramString =$('#employee-customer-search-form').serialize()+action;
					$.post('assignCustomers.json',paramString,function(obj){
						$(this).successMessage({
							container : '.employee-page-container',
							data : obj.result.message
						});
					});
				});
				$('#search-assigned-customers').click(function() {
					$('.customer-page-container').load('employee/search_assigned_customers.jsp');
				});
				
			},
			searchonload :function(){
				var thisButton = $(this);
				var paramString = $('#search-form').serialize();
				$.post('assignCustomers.json', paramString, function(obj) {
					var data = obj.result.data;
			   		$('#search-results-list').html('');
			        if(data!=''){
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1){
						var dateFormat=assignCustomerHandler.formatDate(data[loop].createdOn);
						if(data[loop].firstName.length>25){
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate"style="height:70px">';
							} else {
								rowstr = '<div class="green-result-row"style=height:70px;>';
							}
						rowstr = rowstr + '<div class="green-result-col-1">'+
						'<div class="result-title">' + data[loop].userName  + '</div>' +
						'<div class="result-body"style="height:auto;">' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">'+' </span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action"style="height:55px;">' + 
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn btn-view" title="View EmployeeCustomer  Details"></div>'+
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
						
				         $('#search-results-list').append(rowstr);
						}else {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate">';
						} else {
							rowstr = '<div class="green-result-row">';
						}
					rowstr = rowstr + '<div class="green-result-col-1">'+
					'<div class="result-title">' + data[loop].userName  + '</div>' +
					'<div class="result-body">' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">'+' </span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn btn-view" title="View EmployeeCustomer  Details"></div>'+
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
					
			         $('#search-results-list').append(rowstr);
						}
						alternate = !alternate;
						};
					assignCustomerHandler.initSearchResultButtons();
					$('#search-results-list').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
				});
				
			},
};