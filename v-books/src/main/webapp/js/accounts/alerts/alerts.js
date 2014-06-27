var AlertsHandler = {
		initPageLinks : function() {
			$('#system-defined-alert').pageLink({
				container : '.alerts-page-container',
				url : 'accounts/alerts/system_defined_alert_create.jsp'
			});
			$('#user-defined-alert').pageLink({
				container : '.alerts-page-container',
				url : 'accounts/alerts/user_defined_alert_create.jsp'
			});
			$('#view-alert').pageLink({
				container : '.alerts-page-container',
				url : 'accounts/alerts/alert_view.jsp'
			});
			$('#alert-history').pageLink({
				container : '.alerts-page-container',
				url : 'accounts/alerts/alert_history.jsp'
			});
			
		},
		
		assignMuliple :function() {
			if ($('form').validate() == false){
				return;
			}
			var thisButton = $(this);
			var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
			var notification = $('#'+notificationId).text();
			var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
			var group = $('#'+id).text();
			if(notification == 'Emails') {
				var groupDropdown = $('#groups-search-results-list').find('.selected').find('.group-dropdown').val();
				group = group + '/' + groupDropdown;
			}
			
			var usersArray = [];
			$('#users-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.report-body-column2').attr('id');
		        var user = $('#'+id).text();
		        if(notification == 'Emails') {
		        	var userDropdown = $(this).find('.user-dropdown').val();
			        user = user + '/' + userDropdown;
		        }
		        usersArray.push(user);
		    });
			var user = usersArray.join(',');
			$.post('alerts.json','action=fire-alert-for-multiple-group&group='+group+'&usersArray='+usersArray,function(obj){
				var result=obj.result.data;
			});
			$('#users-search-results-list').empty();
			$('#groups-search-results-list').find('.selected').each(function(){
				$(this).removeClass('selected');
		        var id = $(this).find('.report-body-column2').attr('id');
		        $(this).find('#'+id).attr("clicked","false").css("font-size","13px").css("background","");
		    });
		},
		
		loadGrid: function(data) {
			var alertsGridHeader ='';
			alertsGridHeader += '<div class="report-header" style="width:830px; height: 40px;">'+
								'<div class="report-header-column2 centered" style="width:95px;">' + Msg.ALERT_CATEGORY + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_TYPE_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 85px;">' + Msg.ALERT_NAME_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_CRITERIA_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_DESCRIPTION_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 65px;">' + Msg.ALERT_COUNT_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 110px;">' + Msg.ALERT_ENABLE_DISABLE_LABLE + '</div>' +
								'<div class="report-header-column2 centered" style="width: 55px;">'+'</div>' +
								'</div>'
								$('#search-results-list').append(alertsGridHeader);
								$('#search-results-list').append('<div class="grid-content" style="height:270px;width: 830px;overflow:auto"></div>');
			for(var loop=0;loop<data.length;loop=loop+1) {
				var alertsGridData = '<div class="ui-content report-content" id = "view-alert-grid">';
				alertsGridData+='<div class="report-body" style="width:830px; height: auto; overflow: hidden;">';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertCategory+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertType+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 85px; word-wrap: break-word;">'+data[loop].alertName+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertCriteria+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertDescription+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style=" width: 65px; word-wrap: break-word;">'+data[loop].alertCount+'</div>';
				alertsGridData += '<div category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-check report-body-column2 centered sameHeight" style=" width: 110px; word-wrap: break-word;">'+'<input type = "checkbox" class = "status" id = "statusCheck-'+loop+'" style="margin-top:-3px;" size=10px>'+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style=" width: 55px; word-wrap: break-word;">'
			//	alertsGridData +='<div  category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-btn edit-icon"  title="Edit Alerts" style="margin-top:1px;"></div>';
				//alertsGridData +='<div  category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-btn btn-view "  title="View Alerts" style="margin-top:1px;"></div>';
				alertsGridData +='<div category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="delete-icon"  title="Delete Alerts"></div>';
			   '</div>';
				alertsGridData += '</div>';
				alertsGridData += '</div>';
				$('.grid-content').append(alertsGridData);
				var status = data[loop].alertEnableDisable;
				if(status == "true") {
					$('#statusCheck-'+loop).attr('checked',true);
				} else {
					$('#statusCheck-'+loop).attr('checked',false);
				}
			};
			//$('.grid-content').jScrollPane({showArrows:true});
		},
		initSearchResultButtons : function(){
			/*$('.edit-icon').click(function(){
				var id =$(this).attr('id');
				var category = (this).attr('category');
				if(category == "System Alerts"){
					
				}
			});*/
			$('.btn-view ').click(function(){
				var id =$(this).attr('id');
				var category = $(this).attr('category');
				$.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category,
				        function(data){
							$('#alert-view-container').html(data);
							$("#alert-view-dialog").dialog({
				    			autoOpen: true,
				    			height: 400,
				    			width: 650,
				    			modal: true,
				    			buttons: {
				    				Close: function() {
				    			      $(this).dialog('close');
				    			
				    		          }
				    			},
				    		});
				        }
			        );
			});
			$('.delete-icon').click(function(){
				var id = $(this).attr('id');
				var category = $(this).attr('category');
				$.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category,
			        function(data){
						$('#alert-delete-container').html(data);
						$("#alert-delete-dialog").dialog({
			    			autoOpen: true,
			    			height: 300,
			    			width: 650,
			    			modal: true,
			    			buttons: {
			    			Delete: function() {
			    				if(category == "System Alerts"){
			    					$.post('alerts.json','id='+id+'&action=delete-system-alerts',function(obj){
			    						$(this).successMessage({
			    							container : '.alerts-page-container',
			    							data : obj.result.message
			    						});
			    					});
			    					$(this).dialog('close');
			    				}else if(category = "User Defined Alerts"){
                                   $.post('alerts.json','id='+id+'&action=delete-user-alerts',function(obj){
                                	   $(this).successMessage({
   		    							container : '.alerts-page-container',
   		    							data : obj.result.message
   		    						});
                                	  
			    					});
                                   $(this).dialog('close');
			    				}
			    				
			    			},
			    		      Cancel: function() {
			    			      $(this).dialog('close');
			    			
			    		          }
			    			},
			    			close: function() {
			    				$('#alert-delete-container').html('');
			    			}
			    		});
			        }
		        );
			});
		},
		initViewAlertsOnload: function() {
			var paramString = $('#alert-view-form').serialize();
			$.post('alerts.json', paramString, function(obj) {
				var data = obj.result.data;
				$('#search-results-list').html('');
				$('.grid-content').html('');
				if(data != undefined) {
					AlertsHandler.loadGrid(data);
					AlertsHandler.initSearchResultButtons();
				} 
				else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Available</div></div></div>');
				}
			});
		},
		getAlertCategory: function() {
			var alertCategoryOptions = $('#alertCategory');
			$.post('alerts.json', 'action=get-alert-category', function(obj) {
				$('#alertCategory').html('');
				alertCategoryOptions.append('<option value=-1>select</option>');
				$.each(obj.result.data, function(i, alertCategoryType) {
					alertCategoryOptions.append('<option value="' + alertCategoryType	+ '">' + alertCategoryType + '</option>');
				});
			});
			
			$('#alertCategory').change(function() {
				var alertCategoryVal = $('#alertCategory').val();
				var alertTypeOptions = $('#alertType');
				$.post('alerts.json', 'action=get-alert-type-based-on-category&alertCategory=' + alertCategoryVal, function(obj) {
					$('#alertType').html('');
					alertTypeOptions.append('<option value=-1>select</option>');
					$.each(obj.result.data, function(i, alertType) {
						alertTypeOptions.append('<option value="' + alertType	+ '">' + alertType + '</option>');
					});
				}); 
			});
			
			$('#action-search').click(function() {
				var alertCategory = $('#alertCategory').val();
				var alertType = $('#alertType').val();
				$.post('alerts.json', 'action=search-criteria-for-alerts&alertCategory=' +alertCategory+'&alertType='+alertType, function(obj) {
					var data = obj.result.data;
					$('#search-results-list').html('');
					$('.grid-content').html('');
					if(data != undefined) {
						AlertsHandler.loadGrid(data);
					} else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Available</div></div></div>');
					}
				});
				
			});
			//For activating and deactivating the alert.
			$('.ui-check').live('click', function() {
				var id = $(this).attr('id');
				var status = $(this).find('.status').is(':checked');
				if(status == true) {
					showMessage({title:'Warning', msg:'You have activated the alert.'});
				} else {
					showMessage({title:'Warning', msg:'You have deactivated the alert.'});
				}
				var category = $(this).attr('category');
					$.post('alerts.json','action=enable-or-disable-alert&checkBoxId='+id+'&checkBoxStatus='+status+'&category='+category, function(obj) {
						
					});
			});
			
		}
};

var SystemDefinedAlertsHandler = {
		load: function() {
			var alertTypeOptions = $('#alertType');
			$.post('alerts.json','action=get-alert-type', function(obj) {
				$.each(obj.result.data, function(i, alertType) {
					alertTypeOptions.append('<option value="' + alertType	+ '">' + alertType + '</option>');
				});
			});
		},
		configuredNotifications: function() {
			$('#notification-search-results-list').html('');
			var notifications = ['Emails','SMS','In System Alert'];
			for ( var loop = 0; loop < notifications.length; loop++) {
				var rowstr='';
				rowstr+='<div id="alert-notifications-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
				rowstr+='<a href="#"><div id="notificationColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 190px; word-wrap: break-word;">'+notifications[loop]+'</div>'+'</a>'
				rowstr+='</div>';
				$('#notification-search-results-list').append(rowstr);
			}
			$('#alert-notifications-grid').live('click',function(){
				$('#groups-search-results-list').html('');
				$('#users-search-results-list').html('');
				$('#notification-search-results-list').find('.selected').each(function(){
					$(this).removeClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
			        $(this).find('#'+id).css("font-size","13px").css("background","");
			    });
					$(this).addClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
					$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					var notification = $('#'+id).text();
					if(notification == 'Emails') {
						SystemDefinedAlertsHandler.configuredGroupsForEmail();
					} else {
						SystemDefinedAlertsHandler.configuredGroups();
					}
			});
		},
		//for email selection.
		configuredGroupsForEmail:function(){
			$.post('alerts.json','action=get-groups',function(obj){
				var data = obj.result.data;
				$('#groups-search-results-list').html('');
				$('#users-search-results-list').html('');
		        if(data!=undefined){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					var rowstr='';
					rowstr+='<div id="alert-groups-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='<div id="groupsDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight" clicked=false style="height: inherit; width: 55px">'+'<select class="group-dropdown" style=width:70px;><option value = -1>Select</option>'+
					'<option value=to>To</option>'+
					'<option value=cc>Cc</option>'+
					'<option value=bcc>Bcc</option>'+
					
					'</select>'+'</div>';
					rowstr+='</div>';
					rowstr+='</div>';
					$('#groups-search-results-list').append(rowstr);
				}
				};
			});
				SystemDefinedAlertsHandler.getAssociatedUsersForEmail();
		},
		
		configuredGroups:function(){
			$.post('alerts.json','action=get-groups',function(obj){
				var data = obj.result.data;
		   		$('#groups-search-results-list').html('');
		        if(data!=''){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					var rowstr='';
					rowstr+='<div id="alert-groups-grid-no-email" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class="report-body-column2 centered sameHeight" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='</div>';
					$('#groups-search-results-list').append(rowstr);
				}
				};
				SystemDefinedAlertsHandler.getAssociatedUsers();
			});
		},
		
		getAssociatedUsersForEmail:function(){
			$('#alert-groups-grid').live('click',function(){
				$('#users-search-results-list').html('');
				$('#groups-search-results-list').find('.selected').each(function(){
					$(this).removeClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
			        $(this).find('#'+id).css("font-size","13px").css("background","");
			    });
					$(this).addClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
					$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					var group = $('#'+id).text();
					$.post('alerts.json','action=get-associated-users&group='+group,function(obj){
						var data = obj.result.data;
				        if(data!=undefined){
						var alternate = false;
						$('#users-search-results-list').html('');
						for(var loop=0;loop<data.length;loop=loop+1){
							
							var rowstr='';
							rowstr+='<div id="alert-users-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
							rowstr+='<a href="#"><div id="userColumn-'+loop+'" class="report-body-column2 centered sameHeight" clicked=false style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
							rowstr+='<div id="userDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 55px" value="abc">'+'<select  class="user-dropdown" style= width:70px;><option value = -1>Select</option>'+
							'<option value=to>To</option>'+
							'<option value=cc>Cc</option>'+
							'<option value=bcc>Bcc</option>'+
							'</select>'+'</div>';
							rowstr+='</div>';
							rowstr+='</div>';
							$('#users-search-results-list').append(rowstr);
						}
						};
					});
			});
			
			$('#alert-users-grid').die('click').live('click',function(){
				var id = $(this).find('.report-body-column2').attr('id');
				var selectedElem = $(this).find('#'+id).attr('clicked');
				if($(this).hasClass('selected')){
					$(this).removeClass('selected');
					$(this).find('#'+id).attr("clicked","false").css("font-size","13px").css("background","");
			    }else{
					$(this).addClass('selected');
					$(this).find('#'+id).attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
			    }
			});
		},
		
		getAssociatedUsers:function(){
			$('#alert-groups-grid-no-email').live('click',function(){
				$('#groups-search-results-list').find('.selected').each(function(){
					$(this).removeClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
			        $(this).find('#'+id).css("font-size","13px").css("background","");
			    });
					$(this).addClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
					$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					var group = $('#'+id).text();
					$.post('alerts.json','action=get-associated-users&group='+group,function(obj){
						var data = obj.result.data;
				   		$('#users-search-results-list').html('');
				        if(data!=''){
						var alternate = false;
						for(var loop=0;loop<data.length;loop=loop+1){
							var rowstr='';
							rowstr+='<div id="alert-users-grid-no-email" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
							rowstr+='<a href="#"><div id="usersColumn-'+loop+'" class="report-body-column2 centered sameHeight" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
							rowstr+='</div>';
							$('#users-search-results-list').append(rowstr);
						}
						};
					});
			});
			
			$('#alert-users-grid-no-email').die('click').live('click',function(){
					var id = $(this).find('.report-body-column2').attr('id');
					var id = $(this).find('.report-body-column2').attr('id');
					var selectedElem = $(this).find('#'+id).attr('clicked');
					if($(this).hasClass('.selected')){
						$(this).removeClass('selected');
						$(this).find('#'+id).attr("clicked","false").css("font-size","13px").css("background","");
				    }else{
						$(this).addClass('selected');
						$(this).find('#'+id).attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
				    }
				});
		},
		
		initAddButtons : function() {
			$('#user-defined-alert').click(function(){
				$('.alerts-page-container').load( 'accounts/alerts/user_defined_alert_create.jsp');
			})
			$('#action-clear').click(function() {
				$('form').clearForm();
			});
			
			$('#action-cancel').click(function() {
					$('#error-message').html('You will loose unsaved data.. Cancel form?');   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$.post('alerts.json','action=cancel-form', function() {
								});
				    			$('.task-page-container').html('');
				    			var container ='.alerts-page-container';
				    			var url = "accounts/alerts/system_defined_alert_create.jsp";
				    			$(container).load(url);
				    			$(this).dialog('close');

							},
							Cancel: function() {
								$(this).dialog('close');
							}
						}
					});
				    return false;
			});
			
			$('#button-save').click(function(){
				if ($('form').validate() == false){
					return;
				}
				var thisButton = $(this);
				var paramString = $('#system-defined-alerts-form').serialize();
				var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var notification = $('#'+notificationId).text();
				var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var group = $('#'+id).text();
				if(notification == 'Emails') {
					if(id != undefined) {
						var groupDropdown = $('#groups-search-results-list').find('.selected').find('.group-dropdown').val();
						group = group + '/' + groupDropdown;
					}
				}
				
				var usersArray = [];
				$('#users-search-results-list').find('.selected').each(function(){
			        var id = $(this).find('.report-body-column2').attr('id');
			        var user = $('#'+id).text();
			        if(notification == 'Emails') {
			        	var userDropdown = $(this).find('.user-dropdown').val();
				        user = user + '/' + userDropdown;
			        }
			        usersArray.push(user);
			    });
				var user = usersArray.join(',');
				
				if(notification == "") {
					showMessage({title:'Warning', msg:'Please select atleast one notification type.'});
				    return false;
				} else {
					$.post('alerts.json',paramString=paramString+'&usersArray='+usersArray+'&group='+group+'&notificationType='+notification,function(obj){
						$(this).successMessage({
							container : '.alerts-page-container',
							data : obj.result.message
						});
					});
				}
			});
			
			$('#system-defined-alert').click(function() {
				$('.alerts-page-container').load('accounts/alerts/system_defined_alert_create.jsp');
			});
			$('#action-assign').click(function(){
				AlertsHandler.assignMuliple();
			});
			
		}
		
};
var UserDefinedAlertsHandler = {
		userDefinedalertSteps : ['#user-defined-alerts-form','#user-defined-alerts-form-notifications'],
		userDefinedalertUrl : [ 'alerts.json', 'alerts.json'],
		userDefinedalertStepCount : 0,
		initAddButtons : function(){
			$('#action-clear').click(function() {
				$('form').clearForm();
			});
			
			$('#action-cancel').click(function() {
					$('#error-message').html('You will loose unsaved data.. Cancel form?');   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$.post('alerts.json','action=cancel-user-form', function() {
								});
				    			$('.task-page-container').html('');
				    			var container ='.alerts-page-container';
				    			var url = "accounts/alerts/user_defined_alert_create.jsp";
				    			$(container).load(url);
				    			$(this).dialog('close');

							},
							Cancel: function() {
								$(this).dialog('close');
							}
						}
					});
				    return false;
			});
				$('#button-next').click(function(){
					if($('form').validate()==false){
						return;

					}
					if(UserDefinedAlertsHandler.userDefinedalertStepCount == 0){
						var saleTypesArray = [];
						$('#salesTypes-search-results-list').find('.selected').each(function(){
					        var id = $(this).find('.report-body-column2').attr('id');
					        var salesTypeVal = $('#'+id).text();
					        saleTypesArray.push(salesTypeVal);
					    });
						var sales = saleTypesArray.join(',');
						var paramString ;
						 var alertName = $('#alertName').val();
						var alertType = $('#alertType').val();
						var description =$('#alertDescription').val();
						var values = 'alertType='+alertType +'&alertName='+alertName+'&description='+description;
						if($('#alertType').val()=="My Sales"){
						var mySalesId = $('#mysales-search-results-list').find('.selected').find('.report-body-column2').attr('id');
						var mySales = $('#'+mySalesId).text();
						var mySalesTypeId = $('#salesTypes-search-results-list').find('.selected').find('.report-body-column2').attr('id');
						var mySalesType = $('#'+mySalesTypeId).text();
						paramString = values+'&action=user-defined-alerts-form'+'&alertMySalesPage='+mySalesType+'&alertMySales='+mySales+'&salesTypeArray='+saleTypesArray;
						UserDefinedAlertsHandler.previewFunction(paramString);
						
					}else if($('#alertType').val()=="Trending"){
						
						var productPercentage =$('#productPercentage').val();
						var amountPersentage =$('#amountPercentage').val();
						if(productPercentage == ""&& amountPersentage ==""){
							  showMessage({title:'Warning', msg:'Please enter any one of the above'});
							return ;
						}
						paramString ='productPercentage='+productPercentage+'&amountPercentage='+amountPersentage+'&action=user-defined-alerts-form'+'&'+values;
						UserDefinedAlertsHandler.previewFunction(paramString);
					}else if($('#alertType').val()=="Excess Amount"){
						var amount = $('#amount').val();
						paramString = 'amount='+amount+'&action=user-defined-alerts-form'+'&'+values;
						UserDefinedAlertsHandler.previewFunction(paramString);
					}
			}else{
				var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var notification = $('#'+notificationId).text();
				var usersArray = [];
				 var res ;
				$('#users-search-results-list').find('.selected').each(function(){
			        var id = $(this).find('.report-body-column2').attr('id');
			        var user = $('#'+id).text();
			        if(notification == 'Emails') {
			        	var userDropdown = $(this).find('.user-dropdown').val();
				        user = user + '/' + userDropdown;
			        }
			        usersArray.push(user);
			    });
				var user = usersArray.join(',');
				res =user
				
				var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var group = $('#'+id).text();
				 paramString= $(UserDefinedAlertsHandler.userDefinedalertSteps[UserDefinedAlertsHandler.userDefinedalertStepCount]).serialize()+'&group='+group+'&notificationType='+notification+'&usersArray='+res;
				UserDefinedAlertsHandler.previewFunction(paramString);
			}
				
});
			$('#action-assign').click(function(){
				if(UserDefinedAlertsHandler.userDefinedalertStepCount == 1){
					AlertsHandler.assignMuliple();
				}else{
					
				}
				});
			$('#button-prev').click(function() {
				$('#action-clear').show();
				if(UserDefinedAlertsHandler.userDefinedalertStepCount==UserDefinedAlertsHandler.userDefinedalertSteps.length) {
					$("#action-assign").hide();
					$('#button-next').show();
					$('#button-save').hide();
					$('#alerts-preview-container').html('');
					$('#alerts-preview-container').hide();
					$('.page-buttons').css('margin-left', '150px');
				}
				$(UserDefinedAlertsHandler.userDefinedalertSteps[UserDefinedAlertsHandler.userDefinedalertStepCount]).hide();
				$(UserDefinedAlertsHandler.userDefinedalertSteps[--UserDefinedAlertsHandler.userDefinedalertStepCount]).show();
				if(UserDefinedAlertsHandler.userDefinedalertStepCount>0) {
					$('#button-prev').show();
					$('.page-buttons').css('margin-left', '150px');
				} else {
					$('#button-prev').hide();
					$('.page-buttons').css('margin-left', '240px');
				}
			});
			$('#button-save').click(function(){
				var thisButton = $(this);
				var paramString = 'action=save-user-defined-alerts';
				var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var notification = $('#'+notificationId).text();
				var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var group = $('#'+id).text();
				if(notification == 'Emails') {
					if(id != undefined) {
						var groupDropdown = $('#groups-search-results-list').find('.selected').find('.group-dropdown').val();
						group = group + '/' + groupDropdown;
					}
				}
				
				var usersArray = [];
				$('#users-search-results-list').find('.selected').each(function(){
			        var id = $(this).find('.report-body-column2').attr('id');
			        var user = $('#'+id).text();
			        if(notification == 'Emails') {
			        	var userDropdown = $(this).find('.user-dropdown').val();
				        user = user + '/' + userDropdown;
			        }
			        usersArray.push(user);
			    });
				var user = usersArray.join(',');
				$.post('alerts.json',paramString=paramString+'&usersArray='+usersArray+'&group='+group+'&notificationType='+notification,function(obj){
					$(this).successMessage({
						container : '.alerts-page-container',
						data : obj.result.message
					});
				});
			
			});
		},
		previewFunction :function(paramString){
			$.ajax({type: "POST",
				url:'alerts.json', 
				data: paramString,
				success: function(data){
					$('#error-message').html('');
					$('#error-message').hide();
					$(UserDefinedAlertsHandler.userDefinedalertSteps[UserDefinedAlertsHandler.userDefinedalertStepCount]).hide();
					$(UserDefinedAlertsHandler.userDefinedalertSteps[++UserDefinedAlertsHandler.userDefinedalertStepCount]).show();
			if(UserDefinedAlertsHandler.userDefinedalertStepCount == UserDefinedAlertsHandler.userDefinedalertSteps.length) {
				$("#action-assign").hide();
				$('#button-next').hide();
				$('#action-clear').hide();
				$('#button-save').show();
				$.post('accounts/alerts/user_defined_alert_preview.jsp', 'viewType=preview',
				        function(data){
					 $('#alerts-preview-container').css({'height' : '300px'});
						$('#alerts-preview-container').html(data);
						$('.table-field').css({"width":"800px"});
						$('.main-table').css({"width":"400px"});
						$('.inner-table').css({"width":"400px"});
						$('.display-boxes-colored').css({"width":"140px"});
						$('.display-boxes').css({"width":"255px"});
						$('#alerts-preview-container').show();
						UserDefinedAlertsHandler.expanded=false;
							if (UserDefinedAlertsHandler.userDefinedalertStepCount == UserDefinedAlertsHandler.userDefinedalertSteps.length)
							{
								$('#action-assign').hide();
						    if(!PageHandler.expanded) {
						    	$('#alerts-preview-container').css({'height' : '300px'});
								$('#alerts-preview-container').html(data);
								$('.table-field').css({"width":"800px"});
								$('.main-table').css({"width":"400px"});
								$('.inner-table').css({"width":"400px"});
								$('.display-boxes-colored').css({"width":"140px"});
								$('.display-boxes').css({"width":"255px"});
								$('#alerts-preview-container').show();
								UserDefinedAlertsHandler.expanded=false;
						    }
						    else{
						    	$('#alerts-preview-container').css({'height' : '300px'});
								$('#alerts-preview-container').html(data);
								$('.table-field').css({"width":"662px"});
								$('.main-table').css({"width":"330px"});
								$('.inner-table').css({"width":"330px"});
								$('.display-boxes-colored').css({"width":"125px"});
								$('.display-boxes').css({"width":"200px"});
								$('#alerts-preview-container').show();
								UserDefinedAlertsHandler.expanded=true;
						    }
							}
							$('#ps-exp-col').click(function() {
								if (UserDefinedAlertsHandler.userDefinedalertStepCount == UserDefinedAlertsHandler.userDefinedalertSteps.length)
								{
								if(!PageHandler.expanded) {
							    	$('#alerts-preview-container').css({'height' : '300px'});
									$('#alerts-preview-container').html(data);
									$('.table-field').css({"width":"800px"});
									$('.main-table').css({"width":"400px"});
									$('.inner-table').css({"width":"400px"});
									$('.display-boxes-colored').css({"width":"140px"});
									$('.display-boxes').css({"width":"255px"});
									$('#alerts-preview-container').show();
									UserDefinedAlertsHandler.expanded=false;
							    }
							    else{
							    	$('#alerts-preview-container').css({'height' : '350px'});
									$('#alerts-preview-container').html(data);
									$('.table-field').css({"width":"662px"});
									$('.main-table').css({"width":"330px"});
									$('.inner-table').css({"width":"330px"});
									$('.display-boxes-colored').css({"width":"125px"});
									$('.display-boxes').css({"width":"200px"});
									$('#alerts-preview-container').show();
									UserDefinedAlertsHandler.expanded=true;
							    }
								
								}
						   
						});
			});
				
			}
			if(UserDefinedAlertsHandler.userDefinedalertStepCount>0) {
				$('#action-assign').show();
				$('#button-prev').show();
				$('.page-buttons').css('margin-left', '150px');
			} else {
				$('#button-prev').hide();
				$('.page-buttons').css('margin-left', '200px');
			}
				},
		});
		},
		load :function(){
			$('#alertType').change(function(){
				$('#amountPercentage').removeAttr('class','mandatory');
				$('#productPercentage').removeAttr('class','mandatory');
				$('#mysalesTypes').removeAttr('class','mandatory');
				$('#amount').removeAttr('class','mandatory');
				$('#salesTypes-search-results-list').removeAttr('class','mandatory');
				if($('#alertType').val()=="My Sales"){
					//$('#action-assign').show();
					$('#mysalesTypes').attr('class','mandatory');
					$('#mySales-search-result-list').show();
					$('#trendingAp').hide();
					$('#trendingPp').hide();
					$('#excescashRow').hide();	
				}else if($('#alertType').val()== "Trending"){
					$('#mySales-search-result-list').hide();
					$('#salesTypes-search-result-list').hide();
					$('#excescashRow').hide();	
					$('#trendingAp').show();
					$('#trendingPp').show();
					
				}else if($('#alertType').val()=="Excess Amount"){
					$('#mySales-search-result-list').hide();
					$('#amount').attr('class','mandatory');
					$('#salesTypes-search-result-list').hide();
					$('#trendingAp').hide();
					$('#trendingPp').hide();
				$('#excescashRow').show();	
				}else if($('#alertType').val()=="-1"){
					$('#salesTypes-search-result-list').hide();
					$('#mySales-search-result-list').hide();
					$('#trendingAp').hide();
					$('#trendingPp').hide();
					$('#excescashRow').hide();	
				}
			});
		},
		getAlertTypes : function(){
			$.post('alerts.json','action=get-user-alert-types',function(obj){
				var res = obj.result.data;
				if(res.length>0){
					$('#alertType').empty();
					var select= 'select';
					$('#alertType').append( $('<option></option').text('select').val('-1'));
					for(var loop=0;loop<res.length;loop = loop+1){
						$('#alertType').append( $('<option></option').text(res[loop]));
					};
				}
				
			});
			$.post('alerts.json','action=get-mysales-types',function(obj){
				var res = obj.result.data;
				if(res.length>0){
					$('#mysales-search-results-list').html('');
					for ( var loop = 0; loop < res.length; loop++) {
						var rowstr='';
						rowstr+='<div id="alert-mySales-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
						rowstr+='<a href="#"><div id="mySalesColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
						rowstr+='</div>';
						$('#mysales-search-results-list').append(rowstr);
					}
					$('#alert-mySales-grid').die('click').live('click',function(){
						$('#mysales-search-results-list').find('.selected').each(function(){
							$(this).removeClass('selected');
							var id = $(this).find('.report-body-column2').attr('id');
					        $(this).find('#'+id).css("font-size","13px").css("background","");
					    });
							$(this).addClass('selected');
							var id = $(this).find('.report-body-column2').attr('id');
							$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
							var value = $('#'+id).text();
							if(value == 'Approvals') {
								$.post('alerts.json','action=get-approval-types&value='+value,function(obj){
									var res = obj.result.data;
									if(res.length>0){
										$('#salesTypes-search-results-list').empty();
										$('#salesTypes-search-result-list').show();
										$('#salesTypes-search-results-list').addClass("green-results-list");
										for(var loop=0;loop<res.length; loop++){
											var rowstr='';
											rowstr+='<div id="alert-mySalestypes-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
											rowstr+='<a href="#"><div id="mySalesTypesColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
											rowstr+='</div>';
											$('#salesTypes-search-results-list').append(rowstr);
										};
									}
								});
							} else if(value == 'Transaction CR'){
								$.post('alerts.json','action=get-transaction-cr-types&value='+value,function(obj){
									var res = obj.result.data;
									if(res.length>0){
									$('#salesTypes-search-results-list').empty();
									$('#salesTypes-search-result-list').show();
									$('#salesTypes-search-results-list').addClass("green-results-list");
										for(var loop=0;loop<res.length;loop = loop+1){
											var rowstr='';
											rowstr+='<div id="alert-mySalestypes-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
											rowstr+='<a href="#"><div id="mySalesTypesColumn-'+loop+'" class="report-body-column2 centered sameHeight" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
											rowstr+='</div>';
											$('#salesTypes-search-results-list').append(rowstr);
										};
									}
								});
							}
					});
					$('#alert-mySalestypes-grid').die('click').live('click',function(){
						var id = $(this).find('.report-body-column2').attr('id');
						var selectedElem = $(this).find('#'+id).attr('clicked');
						if(selectedElem == "clicked") {
							$(this).removeClass('selected');
							$(this).find('#'+id).attr("clicked","false").css("font-size","13px").css("background","");
						} else {
							$(this).addClass('selected');
							$(this).find('#'+id).attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
						}
						/*
						$('#salesTypes-search-results-list').find('.selected').each(function(){
							$(this).removeClass('selected');
							var id = $(this).find('.report-body-column2').attr('id');
					        $(this).find('#'+id).css("font-size","13px").css("background","");
					    });
							$(this).addClass('selected');
							var id = $(this).find('.report-body-column2').attr('id');
							$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");*/
					});
				}
				
				
			});
		}
};
