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
			if($('#uName').val() != ''){
				$('#uName').val('');	
			}
			var thisInput = $(this);
			$('#employee-name-suggestions').show();
			assignCustomerHandler.suggestEmployeeNames(thisInput);
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
										htmlStr += '<li><a class="select-employee" style="cursor: pointer;">'
												+ data[loop]
												+ '</a></li>';
									}
									htmlStr += '</div>';
									suggestionsDiv.append(htmlStr);
								} else {
									suggestionsDiv.append('<div id="">'
											+ 'No Sales Executives' + '</div>');
								}
								suggestionsDiv.css('left',
										thisInput.position().left);
								suggestionsDiv.css('top',
										thisInput.position().top + 25);
								suggestionsDiv.show();
								$('.select-employee').click(
										function() {
											$('#localities-search-results-list').empty();
											$('#business-search-results-list').empty();
											thisInput.val($(this).html());
											thisInput.attr('uName', $(this)
													.attr('uName'));
											$('#uName').attr('value',
													$(this).attr('uName'));
											suggestionsDiv.hide();
										});
		});
	},
	searchLocalities:function(){
		var salesExecutive=$('#uName').val();
		if(salesExecutive != ""){
			$.post('assignCustomers.json','action=get-localities',function(obj){
				var data = obj.result.data;
		   		$('#localities-search-results-list').html('');
		        if(data!=''){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					if(alternate) {
						var rowstr = '<a href="#"><div class="green-result-row alternate" id="locality-row" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important;  color:black;">';
					} else {
						rowstr = '<a href="#"><div class="green-result-row" id="locality-row" style="height:23px; border-bottom:1px solid #049de2 !important; background:#7abcff; color:black;">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div class="green-result-col-1" style="padding: 0px 0px 0px 0px;">'+
					'<div class="result-title" id = "locality-val-'+loop+'" style = "font-weight:normal; font-size:13px; padding-top:3px;padding-left:5px;">' + data[loop] + '</div>' +
					'</div>'+
					'</div>'+
					'</a>';
					$('#localities-search-results-list').append(rowstr);
				}
				
				};
				//Adding a scroller for localities box
				$('#localities-search-results-list').mCustomScrollbar({
					scrollButtons:{
						enable:true
					}
				});
				assignCustomerHandler.searchBusinessNames();
			});
		}
	},
	
	searchBusinessNames:function(){
		var businessArray = [];
		var businessNamesArray = [];
		//Method will be called on selecting a locality
		$('#locality-row').live('click',function(){
			$('#localities-search-results-list').find('.selected').each(function(){
				$(this).removeClass('selected');
				var id = $(this).find('.result-title').attr('id');
		        $(this).find('#'+id).css({'font-size':'13px','background':'#7abcff', 'color':'black'});
		    });
			$(this).addClass('selected');
			var id = $(this).find('.result-title').attr('id');
			$(this).find('#'+id).css({'font-size':'13px','color':'#fff','background':'#1E90FF'});
			var locality = $('#'+id).text();
			//Post call to get customers in a locality asynchronously
			$.post('assignCustomers.json','action=get-customers-by-locality&locality='+locality,function(obj){
				var data = obj.result.data;
		   		$('#business-search-results-list').html('');
		        if(data!=''){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					var index= jQuery.inArray(data[loop], businessArray);
					if(index != -1){
						//It is to check whether a business name is selected already
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate" id="business-row" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important;  color:#d4d4d4;">';
						} else {
							rowstr = '<div class="green-result-row" id="business-row" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important;  color:#d4d4d4;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1" style="padding: 0px 0px 0px 0px;">'+
						'<div class="result-title sessionselected" id = "business-val-'+loop+'" style = "font-weight:normal; font-size:13px; padding-top:3px;padding-left:5px;cursor:move;color:#d4d4d4">' + data[loop]  + '</div>' +
						'</div>'+
						'</div>';
						
					}else{
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate" id="business-row" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important;  color:black;">';
						} else {
							rowstr = '<div class="green-result-row" id="business-row" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important;  color:black;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1" style="padding: 0px 0px 0px 0px;">'+
						'<div class="result-title" id = "business-val-'+loop+'" style = "font-weight:normal; font-size:13px; padding-top:3px;padding-left:5px;">' + data[loop]  + '</div>' +
						'</div>'+
						'</div>';
					}
					$('#business-search-results-list').append(rowstr);
				}
				};
				//Adding scroller to the boxes
				$('#business-search-results-list').mCustomScrollbar({
					scrollButtons:{
						enable:true
					}
				});
				var salesExecutive = $('#uName').val();
				$('#business-search-results-list').find('#business-row').each(function(){
			        var id = $(this).find('.result-title').attr('id');
			        var businessName = $('#'+id).text();
			        //To check business name availability
			   	 $.ajax({
					    type: "POST",
						url:'assignCustomers.json', 
						data: 'action=check-availability-for-businessName&businessName='+businessName+'&salesExecutive='+salesExecutive,
						async : false,
						success: function(obj){
							var result=obj.result.data;
							//Condition which proves business name is selected by the same user.
							if(result == 'true'){
								$('#'+id).css("color","#fff");
							    $('#'+id).parents('a').css('cursor', 'default');
							    $('#'+id).parents('.green-result-row').addClass('userselected');
							}else{
								//Method which proves business name is selected by the same user.
								$.ajax({
									type:"POST",
									url:'assignCustomers.json', 
									data: 'action=check-for-selected-businessName&businessName='+businessName,
									async : false,
									success:function(obj){
										var data=obj.result.data;
										if(data == 'true'){
											$('#'+id).css("color","green");
										    $('#'+id).parents('a').css('cursor', 'default');
										    $('#'+id).parents('.green-result-row').addClass('existed');
										    $.post('assignCustomers.json','action=check-for-disabled-salesExecutives&businessName='+businessName,function(obj){
										    	var data=obj.result.data;
										    	if(data == 'false'){
										    		$('#'+id).parents('.green-result-row').addClass('existedAllDisabledEmployees');
										    	}
										    	
										    });
										}
									}
								});
							}
						}
						});
			    });
			});
		});
		//This will be called when a business Name is clicked in  a locality 
		$('#business-row').live('click',function(event){
			//event checking to remove default behaviour of live method.
			if(event.handled === true){
				return false;
			}
			event.handled = true;
			var id = $(this).find('.result-title').attr('id');
			var salesExecutive = $('#uName').val();
			var businessName = $('#'+id).text();
			var index= jQuery.inArray(businessName, businessArray);
			if(index != -1){
				//Indicates a business name is selected locally and to remove from selected businessnames box.
				  businessArray.splice( $.inArray(businessName,businessArray) ,1 );
				  $('#'+id).css("color","black");
				  $('#'+id).css("background","#7abcff");
				  $('#'+id).parents('a').css('cursor', 'pointer');
				  $('#'+id).parents('.green-result-row').removeClass('selected');
				  $('#selected-business-names-list').html('');
				  jQuery.each(businessArray, function() {
					  var formatedBusinessName=this.replace(/\s/g, "");
					  var row='';
			        	row = '<div class="green-result-row" id="row-'+formatedBusinessName+'" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important; color:black; color:black;">';
						row = row + 
						'<div class="result-title-list" id = "list-'+formatedBusinessName+'" style = "font-weight:normal; font-size:13px;background:#7abcff;padding-left:5px; color:black; padding-top:3px;text-decoration:none !important;">' + this  + '</div>' +
						'</div>';
						$('#selected-business-names-list').append(row);
				  });
				  $('#selected-business-names-list').mCustomScrollbar({
						scrollButtons:{
							enable:true
						}
					});
				
			}
			else if(!$(this).hasClass('dbselected') && !$(this).hasClass('userselected')){
				if($(this).hasClass('existed') && ! $(this).hasClass('selected')){
					if($(this).hasClass('existed') && $(this).hasClass('existedAllDisabledEmployees')){
						$('#'+id).parents('a').css('cursor', 'pointer').css('color','green');
				         $('#'+id).parents('.green-result-row').addClass('selected');
				         $('#'+id).parents('.green-result-row').css("font-size","12px").css("background-color","rgb(30, 144, 255)").css("color","rgb(255, 255, 255)");
					}else{
						$('#error-message').html(''+businessName+' is already assigned To Other Sales Executive,Do you want to assign anyway ?');   
						$("#error-message").dialog({
							resizable: false,
							height:140,
							title: "<span class='ui-dlg-confirm'>Confirm</span>",
							modal: true,
							buttons: {
								'Ok' : function() {
								         $('#'+id).parents('a').css('cursor', 'pointer').css('color','green');
								         $('#'+id).parents('.green-result-row').addClass('selected');
								         $('#'+id).parents('.green-result-row').css("font-size","12px").css("background-color","rgb(30, 144, 255)").css("color","rgb(255, 255, 255)");
					    		 	     $(this).dialog('close');
								},
								Cancel: function() {
									$('#'+id).css("color","green");
								    $('#'+id).parents('a').css('cursor', 'default');
								    $('#'+id).parents('.green-result-row').addClass('existed');
									$(this).dialog('close');
								}
							}
						});
					}
				}else if($(this).hasClass('selected')){
					if($(this).hasClass('existed')){
						$('#'+id).css("color","green").css("background","#7abcff").css("font-size","13px");
						$('#'+id).parents('a').css('cursor', 'pointer');
						$('#'+id).parents('.green-result-row').removeClass('selected');
						
						/*$(this).find('#'+id).css("color","green").css("background","#7abcff").css("font-size","13px");
						$(this).find('#'+id).parents('a').css('cursor', 'pointer');
						$(this).find('#'+id).parents('.green-result-row').removeClass('selected');*/
					}else{
						$(this).find('#'+id).css("color","black");
						$(this).find('#'+id).css("background","#7abcff").css("font-size","13px");
						$(this).find('#'+id).parents('a').css('cursor', 'pointer');
						$(this).find('#'+id).parents('.green-result-row').removeClass('selected');
					}
				}else{
					$(this).addClass('selected');
					$(this).find('#'+id).css("font-size","12px").css("background-color","rgb(30, 144, 255)").css("color","rgb(255, 255, 255)");
				}
			}
			//To check for a business name whether selected already or yet to be selected
			else if($(this).hasClass('userselected')){
				$('#error-message').html('Do you want to de-assign customer for this employee?');   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$.post('assignCustomers.json','action=deassign-customer&businessName='+businessName+'&salesExecutive='+salesExecutive,function(obj){
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
				
			}
		});
		
		//Method called on finalizing selected business names to be assigned 
		$('#action-assign-customer').unbind("click").click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(assignCustomerHandler.validateAssignCustomer() ==  false){
				return resultFailure;
			}
			var salesExecutive = $('#uName').val();
			var id = $('#localities-search-results-list').find('.selected').find('.result-title').attr('id');
			var locality = $('#'+id).text();
			$('#business-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        var index= jQuery.inArray(businessName, businessNamesArray);
		        if(index == -1){
		        	businessNamesArray.push(businessName);
		        }
		    });
			var businessName = businessNamesArray.join(',');
			//post call to send all selected business names for a sales executive to assign.
			$.post('assignCustomers.json','action=assign-customers&userName='+salesExecutive+'&businessArray='+businessName+'&locality='+locality,function(obj){
				$(this).successMessage({container:'.employee-page-container', data:obj.result.message});
			});
			//Emptying the array 
			businessNamesArray.length=0;
		});
		//Method called on selecting different businessnames in different localities
		$('#action-assign-multiple-customer').unbind("click").click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			if(assignCustomerHandler.validateAssignCustomer() ==  false){
				return resultFailure;
			}
			var salesExecutive = $('#uName').val();
			var id = $('#localities-search-results-list').find('.selected').find('.result-title').attr('id');
			var locality = $('#'+id).text();
			$('#business-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.result-title').attr('id');
		        var businessName = $('#'+id).text();
		        var index= jQuery.inArray(businessName, businessNamesArray);
		        if(index == -1){
		        	businessNamesArray.push(businessName);
		        }
		        $('#selected-business-names-list').html('');
		        jQuery.each(businessNamesArray, function() {
		        	 var formatedBusinessName=this.replace(/\s/g, "");
					  var row='';
			        	row = '<div class="green-result-row" id="row-'+formatedBusinessName+'" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important; color:black; color:black;">';
						row = row + 
						'<div class="result-title-list" id = "list-'+formatedBusinessName+'" style = "font-weight:normal; font-size:13px;background:#7abcff;padding-left:5px; color:black; padding-top:3px;text-decoration:none !important;">' + this  + '</div>' +
						'</div>';
						$('#selected-business-names-list').append(row);
				  });
		        $('#selected-business-names-list').mCustomScrollbar({
					scrollButtons:{
						enable:true
					}
				});
		       
		    });
			
		});
		//Method called when user tries to deselect it from selected business names box.
		$('.result-title-list').live('click',function(){
				var id=$(this).parent().attr('id');
			    var  businessSelected= $(this).text();
			    businessNamesArray.splice( $.inArray(businessSelected,businessNamesArray) ,1 );
			  $('#selected-business-names-list').html('');
			  jQuery.each(businessNamesArray, function() {
				  var formatedBusinessName=this.replace(/\s/g, "");
				  var row='';
		        	row = '<div class="green-result-row" id="row-'+formatedBusinessName+'" style="height:23px; background:#7abcff;border-bottom:1px solid #049de2 !important; color:black; color:black;">';
					row = row + 
					'<div class="result-title-list" id = "list-'+formatedBusinessName+'" style = "font-weight:normal; font-size:13px;background:#7abcff;padding-left:5px; color:black; padding-top:3px;text-decoration:none !important;">' + this  + '</div>' +
					'</div>';
					$('#selected-business-names-list').append(row);
				  
			  });
			  $('#selected-business-names-list').mCustomScrollbar({
					scrollButtons:{
						enable:true
					}
				});
			  //Method to check for selected business names i an array 
			  $('#business-search-results-list').find('.selected').each(function(){
			        var id = $(this).find('.result-title').attr('id');
			        var businessName = $('#'+id).text();
				    if(businessName == businessSelected){
				    if($(this).hasClass('existed')){
				    	$(this).find('.result-title').css({'font-size':'13px','background':'#7abcff', 'color':'green'});
				    	$(this).removeClass('selected');
				    	
				     }else{
				        		$(this).find('.result-title').css({'font-size':'13px','background':'#7abcff', 'color':'black'}).removeClass('selected');
				        		$(this).removeClass('selected');
				        		
				        	}
				        }
			        
			        
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
					$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('#search-form').clearForm();
								$(this).dialog('close');
							},
					Cancel: function() {
						$(this).dialog('close');
					}
						}
					});
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
				$('.delete-organization').unbind("click").click(function() {
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
			    				$('table').find('input[type=checkbox]').each(function(){
		    		    			  if($(this).is(':checked')){
		    		    				  var idArray=$(this).val();
		    		    				  var index= jQuery.inArray(idArray, arry);
		    		    				  if(index == -1){
		    		    					  arry.push(idArray);
		    		    				  }
		    		    			  }if(arry.length==0){
		    		    				  showMessage({title:'Warning', msg:'No Business Name is selected'});
		    		    				 return;
		    		    			  }
		    		    		  });
			    				if(arry.length>0){
			    					$.post('assignCustomers.json', 'idList='+arry+'&action=delete-employeecustomer&salesExecutive='+executive,
				    						 function(obj) {
				    						$(this).successMessage({
				    							container : '.employee-page-container',
				    							data : obj.result.message
				    						});
				    						arry.length = 0;
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
				/*$('#assign-customers').click(function() {
					$('.employee-page-container').load('employee/assign_customers.jsp');
				});*/
			/*	$('#search-assigned-customers').click(function(){
					$('.employee-page-container').load('employee/search_assigned_customers.jsp');
				});*/
			},
			formatDate:function(inputFormat){
				var str=inputFormat.split(/[" "]/);
				dt=new Date(str[0]);
				return [dt.getDate(),dt.getMonth()+1, dt.getFullYear()].join('-');
			},
				searchonload :function(){
				var thisButton = $(this);
				var paramString = $('#search-form').serialize();
				$('#search-results-list').ajaxLoader();
				$.post('assignCustomers.json', paramString, function(obj) {
					var data = obj.result.data;
			   		$('#search-results-list').html('');
			        if(data!=undefined){
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1){
						var dateFormat=assignCustomerHandler.formatDate(data[loop].createdOn);
						if(data[loop].firstName.length>25){
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate" id="employee-customer-row-'+data[loop].id +'" style="height:70px">';
							} else {
								rowstr = '<div class="green-result-row" id="employee-customer-row-'+data[loop].id +'" style=height:70px;>';
							}
						rowstr = rowstr + '<div class="green-result-col-1" style="margin-top:10px;">'+
						'<div class="result-title">' + data[loop].userName  + '</div>' +
						'<div class="result-body"style="height:auto;">' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-2" style="margin-top:10px;">'+
						'<div class="result-body">' +
						'<span class="property">'+' </span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action" id="green-result-col-action-'+data[loop].id+'" style="height:55px;">' + 
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn btn-view" title="View EmployeeCustomer  Details"></div>'+
						'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
						
				         $('#search-results-list').append(rowstr);
						}else {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate" id="employee-customer-row-'+data[loop].id +'">';
						} else {
							rowstr = '<div class="green-result-row" id="employee-customer-row-'+data[loop].id +'">';
						}
					rowstr = rowstr + '<div class="green-result-col-1" style="margin-top:10px;">'+
					'<div class="result-title">' + data[loop].userName  + '</div>' +
					'<div class="result-body">' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2" style="margin-top:10px;">'+
					'<div class="result-body">' +
					'<span class="property">'+' </span><span class="property-value" style="font: bold 14px arial;">' + dateFormat + '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action" id="green-result-col-action-'+data[loop].id+'">' + 
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn btn-view" title="View EmployeeCustomer  Details"></div>'+
					'<div id="'+data[loop].id+'" date="'+dateFormat+'" salesExecutive="'+data[loop].userName+'" class="ui-btn delete-icon delete-organization" title="delte  EmployeeCustomer  Details"></div>';
					
			         $('#search-results-list').append(rowstr);
						}
						alternate = !alternate;
						
						if(data[loop].isEnabled == 0){
							$("#employee-customer-row-"+ data[loop].id).css('opacity','0.5');
							$("#green-result-col-action-"+ data[loop].id).find(".delete-icon").hide();
							 $("#green-result-col-action-"+ data[loop].id).find(".btn-view").css("pointer-events", "visible");
						}
						
						};
					assignCustomerHandler.initSearchResultButtons();
					$('#search-results-list').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">'+ obj.result.message +'</div></div></div>');
				}
					$.loadAnimation.end();
					setTimeout(function(){
						$('#search-results-list').jScrollPane({showArrows:true});
					},0);
				});
				
			},
};