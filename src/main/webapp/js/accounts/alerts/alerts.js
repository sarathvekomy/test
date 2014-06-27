var AlertsHandler = {
		expanded: true,
		 existeduserList :'null',
		 edit : false,
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
			$('#my-alerts').pageLink({
				container : '.alerts-page-container',
				url : 'accounts/alerts/my_alert_history.jsp'
			});
			$('#user-defined-alert').click(function(){
				edit = 'undefined';
				UserDefinedAlertsHandler.userDefinedalertStepCount = 0
				//$('.alerts-page-container').load('accounts/alerts/user_defined_alert_create.jsp');
			});
			
		},
		assignMuliple : function() {
			if ($('form').validate() == false){
				return;
			}	
			var thisButton = $(this);
			var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
			var notification = $('#'+notificationId).text();
			var groupArray = [];
			$('#groups-search-results-list').find('.selected').each(function(){
		        var id = $(this).find('.report-body-column2').attr('id');
		        var group = $('#'+id).text();
		        if(notification == 'Emails') {
		        	var groupDropdown = $(this).find('.group-dropdown').val();
			        group = group + '/' + groupDropdown;
		        }
		        groupArray.push(group);
		    });
			var usersArray = [];
			$('#users-search-results-list').find('.selected').each(function(){
				var id = $(this).find('.report-body-column2').attr('id');
		        var user = $('#'+id).text();
		        if(notification == 'Emails') {
		        	 var user = $(this).find('.userColoumn1').text();
		        	var userDropdown = $(this).find('.user-dropdown').val();
			        user = user + '/' + userDropdown;
		        }else{
		        	var user =$(this).find('.userColoumnnoemail1').text()
		        }
		        usersArray.push(user);
		    });
			var user = usersArray.join(',');
			$.post('alerts.json','action=fire-alert-for-multiple-group&group='+groupArray+'&usersArray='+usersArray,function(obj){
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
			alertsGridHeader += '<div class="report-header" style="width:865px; height: 30px;">'+
								'<div class="report-header-column2 centered" style="width:100px;">' + Msg.ALERT_CATEGORY + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_TYPE_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_NAME_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_CRITERIA_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px;">' + Msg.ALERT_DESCRIPTION_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 100px; line-height:12px;">' + Msg.ALERT_COUNT_LABEL + '</div>' +
								'<div class="report-header-column2 centered" style="width: 110px;">' + Msg.ALERT_ENABLE_DISABLE_LABLE + '</div>' +
								'<div class="report-header-column2 centered" style="width: 55px;">'+'</div>' +
								'</div>'
								$('#search-results-list').append(alertsGridHeader);
								$('#search-results-list').append('<div class="grid-content" style="height:270px;width: 865px;overflow:auto"></div>');
			for(var loop=0;loop<data.length;loop=loop+1) {
				var alertsGridData = '<div class="ui-content report-content" id = "view-alert-grid">';
				alertsGridData+='<div class="report-body" style="width:865px; height: auto; overflow: hidden;">';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 101px; word-wrap: break-word;">'+data[loop].alertCategory+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertType+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertName+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertCriteria+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 100px; word-wrap: break-word;">'+data[loop].alertDescription+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style=" width: 100px; word-wrap: break-word;">'+data[loop].alertCount+'</div>';
				alertsGridData += '<div category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-check report-body-column2 centered sameHeight" style=" width: 110px; word-wrap: break-word;">'+'<input type = "checkbox" class = "status" id = "statusCheck-'+loop+'" style="margin-top:-3px;" size=10px>'+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style=" width: 66px; word-wrap: break-word;">'
				alertsGridData +='<div  category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-btn edit-icon"  title="Edit Alerts" style="margin-top:1px;"></div>';
				alertsGridData +='<div  category="'+data[loop].alertCategory+'" id="'+data[loop].id+'" class="ui-btn btn-view "  title="View Alerts" style="float:left;"></div>';
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
			
			
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.report-header').css( "width", "810px" );
					$('.report-body').css( "width", "810px" );
					$('.report-header-column2').css( "width", "90px" );
					$('.report-body-column2').css( "width", "90px" );
					$('#search-results-list').css("width", "697px");
					$('.grid-content' ).css( "width", "810px" );
					$('.jScrollPaneContainer').css("width","810px");
				} else {
					$('.report-header').css( "width", "899px" );
					$('.report-body').css( "width", "899px" );
					$('.report-header-column2').css( "width", "100px" );
					$('.report-body-column2').css( "width", "100px" );
					$('#search-results-list').css("width", "830px");
					$('.grid-content' ).css( "width", "899px" );
					$('.jScrollPaneContainer').css("width","899px");
				}
				
				setTimeout(function() {
					$('#sales-search-results-list').jScrollPaneRemove();
					$('#sales-search-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},
		initSearchResultButtons : function(){
			$('.edit-icon').click(function(){
				UserDefinedAlertsHandler.userDefinedalertStepCount = 0;
				var id = $(this).attr('id');
				var editId = $(this).attr('id');
				var category = $(this).attr('category');
				if(category == "System Alerts") {
					PageHandler.pageSelectionHidden = false;
					PageHandler.hidePageSelection();
					AlertsHandler.expanded = true;
					var configuredValuesList;
					//Getting the already configured values.
					$.ajax({type: "POST",
						url:'alerts.json', 
						data: 'action=get-system-alerts-data&id='+id+'&category='+category,
						async : false,
						success: function(obj){
							configuredValuesList = obj.result.data;
						},
					});
					var configuredAlertsList;
					$.ajax({type: "POST",
						url:'alerts.json', 
						data: 'action=get-system-alerts-data&id='+id+'&category='+category,
						async : false,
						success: function(obj){
							configuredAlertsList = obj.result.data;
						},
					});
					
					$.post('accounts/alerts/system_defined_alert_create.jsp','id='+id+'&category='+category,function(data) {
						$('.alerts-page-container').html(data);
						$('#button-save').hide();
						$('#button-update').show();
						$('#button-update').click(function() {
							if ($('form').validate() == false){
								return;
							}
							var thisButton = $(this);
							var paramString = $('#system-defined-alerts-form').serialize();
							var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
							var notification = $('#'+notificationId).text();
							/*var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
							var group = $('#'+id).text();
							if(notification == 'Emails') {
								if(id != undefined) {
									var groupDropdown = $('#groups-search-results-list').find('.selected').find('.group-dropdown').val();
									group = group + '/' + groupDropdown;
								}
							}*/
							var groupArray = [];
							$('#groups-search-results-list').find('.selected').each(function(){
						        var id = $(this).find('.report-body-column2').attr('id');
						        var group = $('#'+id).text();
						        if(notification == 'Emails') {
						        	var groupDropdown = $(this).find('.group-dropdown').val();
							        group = group + '/' + groupDropdown;
						        }
						        groupArray.push(group);
						    });
							
							var usersArray = [];
							$('#users-search-results-list').find('.selected').each(function(){
								var id = $(this).find('.report-body-column2').attr('id');
						        var user = $('#'+id).text();
						        if(notification == 'Emails') {
						        	 var user = $(this).find('.userColoumn1').text();
						        	var userDropdown = $(this).find('.user-dropdown').val();
							        user = user + '/' + userDropdown;
						        }else{
						        	var user =$(this).find('.userColoumnnoemail1').text()
						        }
						        usersArray.push(user);
						    });
							var user = usersArray.join(',');
							
							if(notification == "") {
								showMessage({title:'Warning', msg:'Please select atleast one notification type.'});
							    return false;
							}else{
									$.post('alerts.json',paramString=paramString+'&usersArray='+usersArray+'&group='+groupArray+'&notificationType='+notification+'&editaction=update-system-defined-alerts'+'&id='+editId,function(obj){
										$(this).successMessage({
											container : '.alerts-page-container',
											data : obj.result.message
										});
									});
							}
						});
						
						//Checking the notification type and getting the groups based on notification.
						if(configuredValuesList[0].notificationType == 'Emails') {
							AlertsHandler.getGroupsForEdit();
						} else {
							AlertsHandler.getGroupsNoMailForEdit();
						}
						//Setting the alert name and type.
						$('#alertType').val(configuredValuesList[0].alertType);
						$('#alertName').val(configuredValuesList[0].alertName);
						$('#alertDescription').val(configuredValuesList[0].description);
						
						//highlighting the selected values.
						for(var loop = 0; loop < configuredValuesList.length; loop++) {
							if(configuredValuesList[0].notificationType == 'Emails') {
								if(loop > 0 ){
									if(configuredValuesList[loop-1].role != configuredValuesList[loop].role){
										SystemDefinedAlertsHandler.commonForEmail(configuredValuesList[loop].role);
									}	
								}else{
									SystemDefinedAlertsHandler.commonForEmail(configuredValuesList[loop].role);
								}
								$('.groupEmailGrid').live('click',function(){
									$('#groups-search-results-list').find('.selected').each(function(){
										$(this).removeClass('selected');
										var id = $(this).find('.report-body-column2').attr('id');
								        $(this).find('#'+id).css("font-size","13px").css("background","");
								    });
										$(this).addClass('selected');
										var id = $(this).find('.report-body-column2').attr('id');
										$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
										var group = $('#'+id).text();
										AlertsHandler.getUsersForEmailForEdit(group);
										for(var loop = 0; loop < configuredAlertsList.length; loop++) {
											$('.userColoumn1').each(function(){
												if($(this).text() == configuredAlertsList[loop].userName){
													$(this).parent().parent().addClass('selected');
													   $(this).css("font-size","13px").css("background","#aaaaaa");
													   var mainId =  $(this).parent().parent().attr('id');
														var id = $(this).parent().parent().children().children().attr('id');
														if(configuredValuesList[loop].mailsTo == "true"){
															 $('#'+mainId).find('select.user-dropdown').val('to');
														}else if(configuredValuesList[loop].mailsCc == "true"){
															 $('#'+mainId).find('select.user-dropdown').val('cc');
														}else if(configuredValuesList[loop].mailsBcc == "true"){
															 $('#'+mainId).find('select.user-dropdown').val('bcc');
														}
												}
											});
										};
								});
								$('.userGrid').live('click',function(){
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
								
								//Highlighting the notification type.
								 $.each($('.notificationColumn'), function(index, value) {
									   if( $(this).text() == configuredValuesList[loop].notificationType){
										   $(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
									   }
								   });
								$('.groupEmailcolumn1').each(function(){
									if($(this).text() == configuredValuesList[loop].role ) {
										   $(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
										   var mainId =  $(this).parent().parent().attr('id');
											var id = $(this).parent().parent().children().children().attr('id');
											if(configuredValuesList[loop].mailsTo == "true"){
												 $('#'+mainId).find('select.group-dropdown').val('to');
											}else if(configuredValuesList[loop].mailsCc == "true"){
												 $('#'+mainId).find('select.group-dropdown').val('cc');
											}else if(configuredValuesList[loop].mailsBcc == "true"){
												 $('#'+mainId).find('select.group-dropdown').val('bcc');
											}
									}
								});
								$('.userColoumn1').each(function(){
									if($(this).text() == configuredValuesList[loop].userName) {
										 var mainId =  $(this).parent().parent().attr('id');
											var id = $(this).parent().parent().children().children().attr('id');
											if(configuredValuesList[loop].mailsTo == "true"){
												 $('#'+mainId).find('select.user-dropdown').val('to');
											}else if(configuredValuesList[loop].mailsCc == "true"){
												 $('#'+mainId).find('select.user-dropdown').val('cc');
											}else if(configuredValuesList[loop].mailsBcc == "true"){
												 $('#'+mainId).find('select.user-dropdown').val('bcc');
											}
										 $(this).parent().parent().addClass('selected');
										 var testCss = $(this).parent().parent().css("font-size","13px").css("background","#aaaaaa");
									}
								});
							} else {
								if(loop > 0 ){
									if(configuredValuesList[loop-1].role != configuredValuesList[loop].role){
										SystemDefinedAlertsHandler.commonForNomail(configuredValuesList[loop].role);
									}	
								}else{
									SystemDefinedAlertsHandler.commonForNomail(configuredValuesList[loop].role);
								}
								$('.groupsGrid').live('click',function(){
									$('#groups-search-results-list').find('.selected').each(function(){
										$(this).removeClass('selected');
										var id = $(this).find('.report-body-column2').attr('id');
								        $(this).find('#'+id).css("font-size","13px").css("background","");
								    });
										$(this).addClass('selected');
										var id = $(this).find('.report-body-column2').attr('id');
										$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
										var group = $('#'+id).text();
									AlertsHandler.getUsersForEdit(group);
										$.each(configuredAlertsList,function(index){
											$('.userColoumnnoemail1').each(function(){
										if($(this).text() == configuredAlertsList[index].userName){
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									});
										});
								});
								$('#alert-users-grid-no-email').live('click',function(){
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
								$('.notificationColumn').each(function(){
									if($(this).text() == configuredValuesList[loop].notificationType ){
										$(this).parent().parent().addClass('selected');
										$('.notificationColumn').css('width','190px !important');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
									}
								});
								$('.groupscolumn').each(function(){
									if($(this).text() == configuredValuesList[loop].role ){
										$(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
										   return;
									}
								});
								$('.userColoumnnoemail1').each(function(){
									if($(this).text() == configuredValuesList[loop].userName){
										 $(this).parent().parent().addClass('selected');
										 var testCss = $(this).parent().parent().css("font-size","13px").css("background","#aaaaaa");
									}
								});
							}
							

							//Highlighting the all the existed values on click of notification.
							
							$('#alert-notifications-grid').live('click',function(){
								$('#users-search-results-list').html('');
								var id = $(this).find('.report-body-column2').attr('id');
								$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
								var notification = $('#'+id).text();
								if(notification == "Emails"){
									AlertsHandler.getGroupsForEdit();
								}else{
									AlertsHandler.getGroupsNoMailForEdit();
								}
								var notificationType;
								var role;
								$.each(configuredAlertsList,function(index){
								if(notification == configuredAlertsList[index].notificationType){
									if(notification == "Emails"){
										if(index == 0){
											SystemDefinedAlertsHandler.commonForEmail(configuredAlertsList[index].role);
										}else{
											if(configuredAlertsList[index -1 ].role != configuredAlertsList[index].role){
												SystemDefinedAlertsHandler.commonForEmail(configuredAlertsList[index].role);
											}
										}
										$('.groupEmailcolumn1').each(function(){
											if($(this).text() == configuredAlertsList[index].role ){
												var mainId =  $(this).parent().parent().attr('id');
												var id = $(this).parent().parent().children().children().attr('id');
												if(configuredAlertsList[index].mailsTo == "true"){
													 $('#'+mainId).find('select.group-dropdown').val('to');
												}else if(configuredAlertsList[index].mailsCc == "true"){
													 $('#'+mainId).find('select.group-dropdown').val('cc');
												}else if(configuredAlertsList[index].mailsBcc == "true"){
													 $('#'+mainId).find('select.group-dropdown').val('bcc');
												}
												   $(this).parent().parent().addClass('selected');
												   $(this).css("font-size","13px").css("background","#aaaaaa");
											}
										});
												$('.userColoumn1').each(function(){
														if($(this).text() == configuredAlertsList[index].userName){
																var mainId =  $(this).parent().parent().attr('id');
																var id = $(this).parent().parent().children().children().attr('id');
																$(this).parent().parent().addClass('selected');
																   $(this).css("font-size","13px").css("background","#aaaaaa");
																	if(configuredAlertsList[index].mailsTo == "true"){
																		 $('#'+mainId).find('select.user-dropdown').val('to');
																	}else if(configuredAlertsList[index].mailsCc == "true"){
																		 $('#'+mainId).find('select.user-dropdown').val('cc');
																	}else if(configuredAlertsList[index].mailsBcc == "true"){
																		 $('#'+mainId).find('select.user-dropdown').val('bcc');
																	}
																 
																 $(this).parent().parent().addClass('selected');
																 var testCss = $(this).parent().parent().find('.userColoumn1').css("font-size","13px").css("background","#aaaaaa");
														}
												});
										
									}else{
										var role;
										if(notification == configuredAlertsList[index].notificationType){
											if(index == 0){
												SystemDefinedAlertsHandler.commonForNomail(configuredAlertsList[index].role);
											}else {
												if(configuredAlertsList[index -1 ].role != configuredAlertsList[index].role){
													SystemDefinedAlertsHandler.commonForNomail(configuredAlertsList[index].role);
												}
											}
										}
										$('.groupscolumn').each(function(){
											if($(this).text() == configuredAlertsList[index].role ){
												role = configuredAlertsList[index].role;
												$(this).parent().parent().addClass('selected');
												   $(this).css("font-size","13px").css("background","#aaaaaa");
											}
										});
										$.each(configuredAlertsList,function(index){
												$('.userColoumnnoemail1').each(function(){
											if($(this).text() == configuredAlertsList[index].userName){
												 $(this).parent().parent().addClass('selected');
												 var testCss = $(this).parent().parent().css("font-size","13px").css("background","#aaaaaa");
											}
										});
												$('.userColoumnnoemail1').live('click',function(){
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
											});
										
									}
								}
							});
							});
						};
						
					});
				}else if(category == "User Defined Alerts") {
					var uId = $(this).attr('id');
					$.ajax({type: "POST",
						url:'alerts.json', 
						data: 'action=get-list-users-for-this-record&id='+uId,
						async : false,
						success: function(data){
						existeduserList = data.result.data;
					},
					});
					var existedrecord;
					$.ajax({type: "POST",
						url:'alerts.json', 
						data: 'action=get-user-alerts-data&id='+id,
						async : false,
						success: function(data){
							existedrecord = data.result.data;
						},
					});
					$.post('accounts/alerts/edit_user_defined_alerts.jsp','id='+uId+'&category='+category,function(data){
						edit = true;
						$('.alerts-page-container').html(data);
						$('#button-save').hide();
						$('#button-update').click(function(){
							var thisButton = $(this);
							var paramString = 'action=update-user-defined-alerts';
							var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
							var notification = $('#'+notificationId).text();
							/*var id = $('#groups-search-results-list').find('.selected').find('.report-body-column2').attr('id');
							var group = $('#'+id).text();*/
							var groupArray = [];
							$('#groups-search-results-list').find('.selected').each(function(){
						        var id = $(this).find('.report-body-column2').attr('id');
						        var group = $('#'+id).text();
						        if(notification == 'Emails') {
						        	var groupDropdown = $(this).find('.group-dropdown').val();
							        group = group + '/' + groupDropdown;
						        }
						        groupArray.push(group);
						    });
							var usersArray = [];
							$('#users-search-results-list').find('.selected').each(function(){
						        var id = $(this).find('.report-body-column2').attr('id');
						        if(notification == 'Emails') {
						        	 var user = $(this).find('.userColoumn1').text();
						        	var userDropdown = $(this).find('.user-dropdown').val();
							        user = user + '/' + userDropdown;
						        }else{
						        	var user =$(this).find('.userColoumnnoemail1').text()
						        }
						        usersArray.push(user);
						    });
							$.post('alerts.json',paramString=paramString+'&usersArray='+usersArray+'&groupArray='+groupArray+'&notificationType='+notification+'&id='+uId,function(obj){
								$(this).successMessage({
									container : '.alerts-page-container',
									data : obj.result.message
								});
							});
						
						});
						if(existedrecord.length >0){
							$.ajax({type: "POST",
								url:'alerts.json', 
								data: 'action=get-user-alert-types',
								async :false,
								success: function(obj){
								var res = obj.result.data;
								if(res.length>0){
									$('#alertType').empty();
									var select= 'select';
									$('#alertType').append( $('<option></option').text('select').val('-1'));
									for(var loop=0;loop<res.length;loop = loop+1){
										$('#alertType').append( $('<option></option').text(res[loop]));
									};
								}
								},
							});
							$('#alertType option').each(function(){
								if(existedrecord[0].alertType == $(this).attr('value')){
									$('#alertType option[value="'+$(this).attr('value')+'"]').remove();
									$("#alertType option[value=-1]").text(existedrecord[0].alertType);
									$("#alertType option[value=-1]").val(existedrecord[0].alertType);
								}
							});
							$('#alertName').val(existedrecord[0].alertName);
							$('#alertDescription').val(existedrecord[0].description);
						if(existedrecord[0].notificationType == "Emails"){
							AlertsHandler.getGroupsForEdit();
						}else{
							AlertsHandler.getGroupsNoMailForEdit();
						}
						if( existedrecord[0].salesType == "Approvals"){
							AlertsHandler.getApprovalsForEdit(existedrecord[0].salesType);
						}else{
							AlertsHandler.getTransactionTypesForEdit(existedrecord[0].salesType);
						}
						for(var loop = 0; loop<existedrecord.length;loop = loop+1){
						if(existedrecord[loop].alertType == "Trending"){
							$('#trendingAp').show();
							$('#trendingPp').show();
							$('#amountPercentage').val(existedrecord[0].amountPersentage);
							$('#productPercentage').val(existedrecord[0].productPercentage);
						}else if(existedrecord[loop].alertType == "Excess Amount"){
							$('#amount').attr('class','mandatory');
							$('#excescashRow').show();	
							$('#amount').val(currencyHandler.convertStringPatternToFloat(existedrecord[0].amount));
						}else if(existedrecord[loop].alertType == "My Sales"){
							$('#mysalesTypes').attr('class','mandatory');
							$('#mySales-search-result-list').show();
							if( existedrecord[loop].salesType == "Approvals"){
								 $('.salesType').each(function(){
										if($(this).text() == existedrecord[loop].salesType){
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									});
								 $('.salesPage').each(function(){
									if($(this).text() == existedrecord[loop].salesPage ) {
										$(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
									}
								 });
							}else{
								var res;
								 $('.salesType').each(function(){
										if($(this).text() == existedrecord[loop].salesType){
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									});
								 $('.transaction').each(function(){
										if($(this).text() == existedrecord[loop].salesPage ) {
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									 });
							}
						}
						//Checking for the existing notification is Emails or not.
						if(existedrecord[0].notificationType == 'Emails') {
							if(loop > 0 ){
								if(existedrecord[loop-1].role != existedrecord[loop].role){
									SystemDefinedAlertsHandler.commonForEmail(existedrecord[loop].role);
								}	
							}else{
								SystemDefinedAlertsHandler.commonForEmail(existedrecord[loop].role);
							}
							$('.groupEmailGrid').die('click').live('click',function(){
								$('#groups-search-results-list').find('.selected').each(function(){
									$(this).removeClass('selected');
									var id = $(this).find('.report-body-column2').attr('id');
							        $(this).find('#'+id).css("font-size","13px").css("background","");
							    });
									$(this).addClass('selected');
									var id = $(this).find('.report-body-column2').attr('id');
									$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
									var group = $('#'+id).text();
									
									AlertsHandler.getUsersForEmailForEdit(group);
									for(var loop = 0; loop < existeduserList.length; loop++) {
										$('.userColoumn1').each(function(){
											if($(this).text() ==  existedrecord[loop].userName){
												var mainId =  $(this).parent().parent().attr('id');
												var resId = $('#'+mainId).find('.userColoumn2').attr("id");
												  $(this).parent().parent().addClass('selected');
												   $(this).css("font-size","13px").css("background","#aaaaaa");
												   
													if(existedrecord[loop].mailsTo == "true"){
														 $('#'+resId).find('select.user-dropdown').val('to');
													}else if(existedrecord[loop].mailsCc == "true"){
														 $('#'+resId).find('select.user-dropdown').val('cc');
													}else if(existedrecord[loop].mailsBcc == "true"){
														 $('#'+resId).find('select.user-dropdown').val('bcc');
													}
												}
										});
										};
							});
							$('.userGrid').live('click',function(){
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
							//Iterating the values and highlighting the existed values.
							$('.notificationColumn').each(function(){
								if($(this).text() == existedrecord[loop].notificationType ){
									$('.notificationColumn').css('width','190px !important');
									$(this).parent().parent().addClass('selected');
									   $(this).css("font-size","13px").css("background","#aaaaaa");
								}
							});
							$('.groupEmailcolumn1').each(function(){
								if($(this).text() == existedrecord[loop].role ){
									var mainId =  $(this).parent().parent().attr('id');
									var resId = $('#'+mainId).find('.groupEmailcolumn2').attr("id");
									  $(this).parent().parent().addClass('selected');
									   $(this).css("font-size","13px").css("background","#aaaaaa");
									   
										if(existedrecord[loop].mailsTo == "true"){
											 $('#'+resId).find('select.group-dropdown').val('to');
										}else if(existedrecord[loop].mailsCc == "true"){
											 $('#'+resId).find('select.group-dropdown').val('cc');
										}else if(existedrecord[loop].mailsBcc == "true"){
											 $('#'+resId).find('select.group-dropdown').val('bcc');
										}
								}
							});
							$('.userColoumn1').each(function(){
								if($(this).text() ==  existedrecord[loop].userName){
									var mainId =  $(this).parent().parent().attr('id');
									var resId = $('#'+mainId).find('.userColoumn2').attr("id");
									  $(this).parent().parent().addClass('selected');
									   $(this).css("font-size","13px").css("background","#aaaaaa");
									   
										if(existedrecord[loop].mailsTo == "true"){
											 $('#'+resId).find('select.user-dropdown').val('to');
										}else if(existedrecord[loop].mailsCc == "true"){
											 $('#'+resId).find('select.user-dropdown').val('cc');
										}else if(existedrecord[loop].mailsBcc == "true"){
											 $('#'+resId).find('select.user-dropdown').val('bcc');
										}
									}
							});
						}else{
							if(loop > 0 ){
								if(existedrecord[loop-1].role != existedrecord[loop].role){
									SystemDefinedAlertsHandler.commonForNomail(existedrecord[loop].role);
								}	
							}else{
								SystemDefinedAlertsHandler.commonForNomail(existedrecord[loop].role);
							}
							$('.groupsGrid').live('click',function(){
								var id = $(this).find('.report-body-column2').attr('id');
								$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
								var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
								var notification = $('#'+notificationId).text();
								$('#groups-search-results-list').find('.selected').each(function(){
									$(this).removeClass('selected');
									var id = $(this).find('.report-body-column2').attr('id');
							        $(this).find('#'+id).css("font-size","13px").css("background","");
							    });
									$(this).addClass('selected');
									var id = $(this).find('.report-body-column2').attr('id');
									$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
									var group = $('#'+id).text();
								AlertsHandler.getUsersForEdit(group);
								if(notification == existedrecord[0].notificationType ){
									$.each(existeduserList,function(index){
										$('.userColoumnnoemail1').each(function(){
									if($(this).text() == existeduserList[index].userName){
										$(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
									}
								});
									});
								}
							});
							$('#alert-users-grid-no-email').live('click',function(){
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
							$('.notificationColumn').each(function(){
								if($(this).text() == existedrecord[loop].notificationType ){
									$(this).parent().parent().addClass('selected');
									$('.notificationColumn').css('width','190px !important');
									   $(this).css("font-size","13px").css("background","#aaaaaa");
								}
							});
							$('.groupscolumn').each(function(){
								if($(this).text() == existedrecord[loop].role ){
									$(this).parent().parent().addClass('selected');
									   $(this).css("font-size","13px").css("background","#aaaaaa");
									   return;
								}
							});
							$('.userColoumnnoemail1').each(function(){
								if($(this).text() == existedrecord[loop].userName){
									 $(this).parent().parent().addClass('selected');
									 var testCss = $(this).parent().parent().css("font-size","13px").css("background","#aaaaaa");
									//return;
								}
							});
						}
						$('#alert-notifications-grid').live('click',function(){
							$('#users-search-results-list').html('');
							var id = $(this).find('.report-body-column2').attr('id');
							$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
							var notification = $('#'+id).text();
							if(notification == "Emails"){
								AlertsHandler.getGroupsForEdit();
							}else{
								AlertsHandler.getGroupsNoMailForEdit();
							}
							var notificationType;
							var role;
							$.each(existeduserList,function(index){
							if(notification == existeduserList[index].notificationType){
								if(notification == "Emails"){
									if(index == 0){
										SystemDefinedAlertsHandler.commonForEmail(existeduserList[index].role);
									}else{
										if(existeduserList[index -1 ].role != existeduserList[index].role){
											SystemDefinedAlertsHandler.commonForEmail(existeduserList[index].role);
										}
									}
									$('.groupEmailcolumn1').each(function(){
										if($(this).text() ==  existeduserList[index].role ){
											var mainId =  $(this).parent().parent().attr('id');
											var resId = $('#'+mainId).find('.groupEmailcolumn2').attr("id");
											  $(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
											   if(existeduserList[index].mailsTo == "true"){
													 $('#'+resId).find('select.group-dropdown').val('to');
												}else if(existeduserList[index].mailsCc == "true"){
													 $('#'+resId).find('select.group-dropdown').val('cc');
													
												}else if(existeduserList[index].mailsBcc == "true"){
													 $('#'+resId).find('select.group-dropdown').val('bcc');
												}
										}
									});
									
									$('.userColoumn1').each(function(){
										if($(this).text() ==  existeduserList[index].userName){
											var mainId =  $(this).parent().parent().attr('id');
											var resId = $('#'+mainId).find('.userColoumn2').attr("id");
											  $(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
											   if(existeduserList[index].mailsTo == "true"){
													 $('#'+resId).find('select.user-dropdown').val('to');
												}else if(existeduserList[index].mailsCc == "true"){
													 $('#'+resId).find('select.user-dropdown').val('cc');
													
												}else if(existeduserList[index].mailsBcc == "true"){
													 $('#'+resId).find('select.user-dropdown').val('bcc');
												}
										}
									});
								}else{
									var role;
									if(notification == existeduserList[index].notificationType){
										if(index == 0){
											SystemDefinedAlertsHandler.commonForNomail(existeduserList[index].role);
										}else{
											if(existeduserList[index -1 ].role != existeduserList[index].role){
												SystemDefinedAlertsHandler.commonForNomail(existeduserList[index].role);
											}
										}
									}
									$('.groupscolumn').each(function(){
										if($(this).text() == existeduserList[index].role ){
											role = existeduserList[index].role;
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									});
									$.each(existeduserList,function(index){
											$('.userColoumnnoemail1').each(function(){
												if($(this).text() ==  existeduserList[index].userName){
													 $(this).parent().parent().addClass('selected');
													 var testCss = $(this).parent().parent().css("font-size","13px").css("background","#aaaaaa");
												}
									});
											$('.userColoumnnoemail1').live('click',function(){
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
										});
									
								}
							}
						});
						});
					};
					$('#alert-mySales-grid').die('click').live('click',function(){
						 var salesType;
							var id = $(this).find('.report-body-column2').attr('id');
							$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					         salesType = $('#'+id).text();
					         
					       $('#mysales-search-results-list').find('.selected').each(function(){
								$(this).removeClass('selected');
								var id = $(this).find('.report-body-column2').attr('id');
								$(this).find('#'+id).css("font-size","13px").css("background","");
					    });
							$(this).addClass('selected');
							var id = $(this).find('.report-body-column2').attr('id');
							$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
						if(salesType == "Approvals"){
							AlertsHandler.getApprovalsForEdit(salesType);
						}else{
							AlertsHandler.getTransactionTypesForEdit(salesType);
						}
						if(existeduserList[0].salesType == "Approvals"){
							$.each(existeduserList,function(index){
								$('.salesPage').each(function(){
										if($(this).text() == existeduserList[index].salesPage ) {
											$(this).parent().parent().addClass('selected');
											   $(this).css("font-size","13px").css("background","#aaaaaa");
										}
									 });
							});
						}else{
							$.each(existeduserList,function(index){
							$('.transaction').each(function(){
									if($(this).text() == existeduserList[index].salesPage ) {
										$(this).parent().parent().addClass('selected');
										   $(this).css("font-size","13px").css("background","#aaaaaa");
									}
								 });
							});
						}
					});
					
					}
					});
				}
			});
			$('.btn-view ').click(function(){
				var id =$(this).attr('id');
				var category = $(this).attr('category');
				var res;
				if(category == "System Alerts"){
					$.post('alerts.json','id='+id+'&action=get-system-alert-data-by-id',function(obj){
						 res = obj.result.data;
						 $.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category, function(data){
								$('#alert-view-container').html(data);
								$("#alert-view-dialog").dialog({
					    			autoOpen: true,
					    			height: 300,
					    			width: 650,
					    			modal: true,
					    			buttons : {
					    				Close :function() {
					    					$(this).dialog('close');
					    				}
					    			},
							 });
							 $('.alertNameVal').text(res.alertName);
						     $('.alertCategoryVal').text(res.alertCategory);
							 $('.descriptionVal').text(res.description);
							 $('.notificationTypeVal').text(res.notificationType);
							 $('.roleVal').text(res.role);
							 $(".userNameVal").text(res.userName);
							});
					});
				}else if(category == "User Defined Alerts"){
					$.post('alerts.json','id='+id+'&action=get-user-alert-data-by-id',function(obj){
						 res = obj.result.data;
						 $.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category, function(data){
								$('#alert-view-container').html(data);
								$("#alert-view-dialog").dialog({
					    			autoOpen: true,
					    			height: 300,
					    			width: 650,
					    			modal: true,
					    			buttons: {
					    				close: function() {
						    				$(this).dialog('close');
						    			}
						    		},
						    		
							 });
							 $('.alertNameVal').text(res.alertName);
							 $('.alertCategoryVal').text(res.alertCategory);
							 $('.descriptionVal').text(res.description);
							 $('.notificationTypeVal').text(res.notificationType);
							 $('.roleVal').text(res.role);
							 $('.userNameVal').text(res.userName);
							});
					});
				}
			});
			$('.delete-icon').click(function(){
				var id = $(this).attr('id');
				var category = $(this).attr('category');
				var res;
				if(category == "System Alerts"){
					$.post('alerts.json','id='+id+'&action=get-system-alert-data-by-id',function(obj){
						 res = obj.result.data;
						 $.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category, function(data){
								$('#alert-delete-container').html(data);
								$("#alert-delete-dialog").dialog({
					    			autoOpen: true,
					    			height: 300,
					    			width: 650,
					    			modal: true,
					    			buttons: {
						    			Delete: function() {
						    					$.post('alerts.json','id='+id+'&action=delete-system-alerts',function(obj){
						    						$(this).successMessage({
						    							container : '.alerts-page-container',
						    							data : obj.result.message
						    						});
						    					});
						    					$(this).dialog('close');
						    				
						    			},
						    		      Cancel: function() {
						    			      $(this).dialog('close');
						    			
						    		          }
						    			},
						    			close: function() {
						    				$('#alert-delete-container').html('');
						    			}
							 });
								$('.alertNameVal').text(res.alertName);
								 $('.alertCategoryVal').text(res.alertCategory);
								 $('.descriptionVal').text(res.description);
								 $('.notificationTypeVal').text(res.notificationType);
								 $('.roleVal').text(res.role);
								 $('.userNameVal').text(res.userName);
							});
					});
				}else if(category == "User Defined Alerts"){
					$.post('alerts.json','id='+id+'&action=get-user-alert-data-by-id',function(obj){
						 res = obj.result.data;
						 $.post('accounts/alerts/alerts_delete.jsp', 'id='+id+'&category='+category, function(data){
								$('#alert-delete-container').html(data);
								$("#alert-delete-dialog").dialog({
					    			autoOpen: true,
					    			height: 300,
					    			width: 650,
					    			modal: true,
					    			buttons: {
						    			Delete: function() {
			                                   $.post('alerts.json','id='+id+'&action=delete-user-alerts',function(obj){
			                                	   $(this).successMessage({
			   		    							container : '.alerts-page-container',
			   		    							data : obj.result.message
			   		    						});
			                                	  
						    					});
			                                   $(this).dialog('close');
						    				
						    			},
						    		      Cancel: function() {
						    			      $(this).dialog('close');
						    			
						    		          }
						    			},
						    			close: function() {
						    				$('#alert-delete-container').html('');
						    			}
							 });
								$('.alertNameVal').text(res.alertName);
								 $('.alertCategoryVal').text(res.alertCategory);
								 $('.descriptionVal').text(res.description);
								 $('.notificationTypeVal').text(res.notificationType);
								 $('.roleVal').text(res.role);
								 $('.userNameVal').text(res.userName);
							});
					});
				}
			});
		},
		//for edit system Alerts
		selectExistedGroupsForEmail: function(res){
			for(var loop=0; loop<res.length ; loop = loop+1){
				$.each($('#groups-search-results-list'),function(index,value){
					$.each($('.groupEmailcolumn1'), function(index, value) {
						 if($(this).text() == res[loop].role){
								var id= $(this).attr('id');
							$('#'+id).addClass('selected');
							$('#'+id).css("font-size","13px").css("background","#aaaaaa");
						if(res[loop].mailsTo == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.group-dropdown').val('to');
						}else if(res[loop].mailsCc == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.group-dropdown').val('cc');
						}else if(res[loop].mailsBcc == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.group-dropdown').val('bcc');
						}
					   }
					 });
				});
				
			}
			 
		},
		selectExistedGroups : function(res){
			for(var loop=0; loop<res.length ; loop = loop+1){
				$.each($('#groups-search-results-list'),function(index,value){
			$.each($('.groupscolumn'), function(index, value) {
				if($(this).text() == res[loop].role){
					var id= $(this).attr('id');
				$('#'+id).addClass('selected');
				$('#'+id).css("font-size","13px").css("background","#aaaaaa");
			   }
			 });
				});
			}
		},
		selectExistedUsers : function(res){
			for(var loop=0; loop<res.length ; loop = loop+1){
		 $.each($('.userColoumnnoemail1'),function(index,value){
				if($(this).text() == res[loop].userName){
					$(this).parent().parent().addClass('selected');
					var id= $(this).attr('id');
					$('#'+id).css("font-size","13px").css("background","#aaaaaa");
				}
			});
			}
		},
		getSelectedUsersForEmail : function(res){
			for(var loop=0; loop<res.length ; loop = loop+1){
				$.each($('#users-search-results-list'),function(index,value){
					$.each($('.userColoumn1'), function(index, value) {
						 if($(this).text() == res[loop].userName){
								var id= $(this).attr('id');
							$('#'+id).addClass('selected');
							$('#'+id).css("font-size","13px").css("background","#aaaaaa");
						if(res[loop].mailsTo == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.user-dropdown').val('to');
						}else if(res[loop].mailsCc == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.user-dropdown').val('cc');
						}else if(res[loop].mailsBcc == "true"){
							var resid = $(this).parent().parent().attr('id');
							$('#'+resid).find('.user-dropdown').val('bcc');
						}
					   }
					 });
				});
				
			}
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
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('form').clear();
							$(this).dialog('close');
						},
				Cancel: function() {
					$(this).dialog('close');
				}
					}
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
			
		},
		getGroupsForEdit : function(){
			$.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-groups',
				async :false,
				success: function(obj){
				var data = obj.result.data;
				$('#groups-search-results-list').html('');
		        if(data!=undefined){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1) {
					var rowstr='';
					rowstr+='<div id="alert-groups-grid-'+loop+'" class="report-body groupEmailGrid" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupEmailcolumn1" style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='<div id="groupsDropdownColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupEmailcolumn2" clicked=false style="height: inherit; width: 55px">'+'<select class="group-dropdown" style=width:70px;><option value = -1>Select</option>'+
					'<option value=to>To</option>'+
					'<option value=cc>Cc</option>'+
					'<option value=bcc>Bcc</option>'+
					
					'</select>'+'</div>';
					rowstr+='</div>';
					rowstr+='</div>';
					$('#groups-search-results-list').append(rowstr);
				}
			};
			},
		});
			$('.groupEmailGrid').click(function(){
				$('#groups-search-results-list').find('.selected').each(function(){
					$(this).removeClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
			        $(this).find('#'+id).css("font-size","13px").css("background","");
			    });
					$(this).addClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
					$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					var group = $('#'+id).text();
					AlertsHandler.getUsersForEmailForEdit(group);
			});
	},
	getApprovalsForEdit : function(salesType){
		 $.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-approval-types&value='+salesType,
				async :false,
				success: function(obj){
			var res = obj.result.data;
			if(res.length>0){
				$('#salesTypes-search-results-list').empty();
				$('#salesTypes-search-result-list').show();
				$('#salesTypes-search-results-list').addClass("green-results-list");
				for(var loop=0;loop<res.length; loop++){
					var rowstr='';
					rowstr+='<div id="alert-mySalestypes-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="mySalesTypesColumn-'+loop+'" class="report-body-column2 centered sameHeight salesPage" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
					rowstr+='</div>';
					$('#salesTypes-search-results-list').append(rowstr);
				};
			}
		},
		});
	},
	getTransactionTypesForEdit : function(salesType){
		 $.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-transaction-cr-types&value='+salesType,
				async :false,
				success: function(obj){
			 res = obj.result.data;
			if(res.length>0){
			$('#salesTypes-search-results-list').empty();
			$('#salesTypes-search-result-list').show();
			$('#salesTypes-search-results-list').addClass("green-results-list");
				for(var loop=0;loop<res.length;loop = loop+1){
					var rowstr='';
					rowstr+='<div id="alert-mySalestypes-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="mySalesTypesColumn-'+loop+'" class="report-body-column2 centered sameHeight transaction" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
					rowstr+='</div>';
					$('#salesTypes-search-results-list').append(rowstr);
				};
			}
			},
		});
	},
	getGroupsNoMailForEdit : function(){
		$.ajax({type: "POST",
			url:'alerts.json', 
			data: 'action=get-groups',
			async :false,
			success: function(obj){
			var data = obj.result.data;
	   		$('#groups-search-results-list').html('');
	        if(data!=''){
			var alternate = false;
			for(var loop=0;loop<data.length;loop=loop+1){
				var rowstr='';
				rowstr+='<div id="alert-groups-grid-no-email-'+loop+'" class="report-body groupsGrid" style="width: 200px; height: auto; overflow: hidden;">';
				rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupscolumn" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
				rowstr+='</div>';
				$('#groups-search-results-list').append(rowstr);
			}
			};
		},
		});
		$('.groupsGrid').click(function(){
			$('#groups-search-results-list').find('.selected').each(function(){
				$(this).removeClass('selected');
				var id = $(this).find('.report-body-column2').attr('id');
		        $(this).find('#'+id).css("font-size","13px").css("background","");
		    });
				$(this).addClass('selected');
				var id = $(this).find('.report-body-column2').attr('id');
				$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
				var group = $('#'+id).text();
				AlertsHandler.getUsersForEdit(group);
		});
	},
	getUsersForEmailForEdit : function(role){
		 $.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-associated-users&group='+role,
				async :false,
				success: function(obj){
				var data = obj.result.data;
		        if(data!=undefined){
				var alternate = false;
				$('#users-search-results-list').html('');
				for(var loop=0;loop<data.length;loop=loop+1){
					var rowstr='';
					rowstr+='<div id="alert-users-grid-'+loop+'" class="report-body userGrid" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="userColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn1" clicked=false style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='<div id="userDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn2" style="height: inherit; width: 55px" value="abc">'+'<select  class="user-dropdown" style= width:70px;><option value = -1>Select</option>'+
					'<option value=to>To</option>'+
					'<option value=cc>Cc</option>'+
					'<option value=bcc>Bcc</option>'+
					'</select>'+'</div>';
					rowstr+='</div>';
					rowstr+='</div>';
					$('#users-search-results-list').append(rowstr);
				};
				}else{
					$('#users-search-results-list').html('');
				}
			},
			});
		 $('.userGrid').click(function(){
				if($(this).hasClass('selected')){
					$(this).find('.report-body user').removeClass('selected');
					$(this).attr("clicked","false").css("font-size","13px").css("background","");
			    }else{
					$(this).find('.report-body user').addClass('selected');
					$(this).find('.userColoumn1').attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
			    }
			});
	},
	getUsersForEdit :function(role){
		 $.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-associated-users&group='+role,
				async :false,
				success: function(obj){
			var data = obj.result.data;
	   		$('#users-search-results-list').html('');
	        if(data!=''){
			var alternate = false;
			for(var loop=0;loop<data.length;loop=loop+1){
				var rowstr='';
				rowstr+='<div id="alert-users-grid-no-email" class="user report-body" style="width: 200px; height: auto; overflow: hidden;">';
				rowstr+='<a href="#"><div id="usersColumn-'+loop+'" class="user report-body-column2 centered sameHeight userColoumnnoemail1" clicked=true style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
				rowstr+='</div>';
				$('#users-search-results-list').append(rowstr);
			}
			};
		},
		});
		 $('#alert-users-grid-no-email').die('click').live('click',function(){
			 var id = $(this).find('.report-body-column2').attr('id');
				var thisClass = $('#'+id).attr("class");
				if($(this).hasClass('selected')){
					$(this).removeClass('selected')
					$(this).attr("clicked","false").css("font-size","13px").css("background","");
					$(this).find('.userColoumnnoemail1').css("font-size","13px").css("background","");
			    }else{
					$(this).addClass('selected');
					$(this).attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
			    }
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
			var notifications = ['Emails','SMS','In_System_Alert'];
			for ( var loop = 0; loop < notifications.length; loop++) {
				var rowstr='';
				rowstr+='<div id="alert-notifications-grid" class="report-body notification" style="width: 200px; height: auto; overflow: hidden;">';
				rowstr+='<a href="#"><div id="notificationColumn-'+loop+'" class="report-body-column2 centered sameHeight notificationColumn" style="height: inherit; width: 190px; word-wrap: break-word;">'+notifications[loop]+'</div>'+'</a>'
				rowstr+='</div>';
				$('#notification-search-results-list').append(rowstr);
			}
			$('#alert-notifications-grid').die('click').live('click',function(){
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
		// For Edit System Alerts
		EditconfiguredNotifications: function() {
			$('#notification-search-results-list').html('');
			var notifications = ['Emails','SMS','In_System_Alert'];
			for ( var loop = 0; loop < notifications.length; loop++) {
				var rowstr='';
				rowstr+='<div id="alert-notifications-grid-edit" class="report-body notification" style="width: 200px; height: auto; overflow: hidden;">';
				rowstr+='<a href="#"><div id="notificationColumn-'+loop+'" class="report-body-column2 centered sameHeight notificationColumn" style="height: inherit; width: 190px; word-wrap: break-word;">'+notifications[loop]+'</div>'+'</a>'
				rowstr+='</div>';
				$('#notification-search-results-list').append(rowstr);
			}
			$('#alert-notifications-grid-edit').die('click').live('click',function(){
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
				for(var loop=0;loop<data.length;loop=loop+1) {
					var rowstr='';
					rowstr+='<div id="alert-groups-grid-'+loop+'" class="report-body groupEmailGrid" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupEmailcolumn1" style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='<div id="groupsDropdownColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupEmailcolumn2" clicked=false style="height: inherit; width: 55px">'+'<select class="group-dropdown" style=width:70px;><option value = -1>Select</option>'+
					'<option value=to>To</option>'+
					'<option value=cc>Cc</option>'+
					'<option value=bcc>Bcc</option>'+
					
					'</select>'+'</div>';
					rowstr+='</div>';
					rowstr+='</div>';
					$('#groups-search-results-list').append(rowstr);
				}
			};
			SystemDefinedAlertsHandler.getAssociatedUsersForEmail();
		});
	},
		
		configuredGroups:function(){
			$.post('alerts.json','action=get-groups',function(obj){
				var data = obj.result.data;
		   		$('#groups-search-results-list').html('');
		        if(data!=''){
				var alternate = false;
				for(var loop=0;loop<data.length;loop=loop+1){
					var rowstr='';
					rowstr+='<div id="alert-groups-grid-no-email-'+loop+'" class="report-body groupsGrid" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="groupsColumn-'+loop+'" class=" report-body-column2 centered sameHeight groupscolumn" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
					rowstr+='</div>';
					$('#groups-search-results-list').append(rowstr);
				}
				};
				SystemDefinedAlertsHandler.getAssociatedUsers();
			});
		},
		getAssociatedUsersForEmail:function(){
			$('.groupEmailGrid').click(function(){
				$('#groups-search-results-list').find('.selected').each(function(){
					$(this).removeClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
			        $(this).find('#'+id).css("font-size","13px").css("background","");
			    });
					$(this).addClass('selected');
					var id = $(this).find('.report-body-column2').attr('id');
					$(this).find('#'+id).css("font-size","13px").css("background","#aaaaaa");
					var group = $('#'+id).text();
					$('#users-search-results-list').html('');
					$.post('alerts.json','action=get-associated-users&group='+group,function(obj){
						var data = obj.result.data;
				        if(data!=undefined){
						var alternate = false;
						for(var loop=0;loop<data.length;loop=loop+1){
							var rowstr='';
							rowstr+='<div id="alert-users-grid" class="report-body userGrid" style="width: 200px; height: auto; overflow: hidden;">';
							rowstr+='<a href="#"><div id="userColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn1" clicked=false style="height: inherit; width: 110px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
							rowstr+='<div id ="userDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn2" style="height: inherit; width: 55px" value="abc">'+'<select  class="user-dropdown" style= width:70px;><option value = -1>Select</option>'+
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
			 $('.userGrid').die('click').live('click',function(){
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
			$('.groupsGrid').click(function(){
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
							rowstr+='<div id="alert-users-grid-no-email" class="user report-body" style="width: 200px; height: auto; overflow: hidden;">';
							rowstr+='<a href="#"><div id="usersColumn-'+loop+'" class="user report-body-column2 centered sameHeight userColoumnnoemail1" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
							rowstr+='</div>';
							$('#users-search-results-list').append(rowstr);
						}
						};
					});
			});
			
			$('#alert-users-grid-no-email').die('click').live('click',function(){
				var id = $(this).find('.report-body-column2').attr('id');
				var selectedElem = $(this).find('#'+id).attr('clicked');
				if($(this).hasClass('selected')){
					var thisId = $(this).find('.report-body-column2').attr("id")
					$('#'+thisId).find('.report-body-column2').removeClass('selected');
					$(this).removeClass('selected');
					$('#'+thisId).find('.report-body-column2').attr("clicked","false").css("font-size","13px").css("background","");
					$(this).attr("clicked","false").css("font-size","13px").css("background","");
			    }else{
					$('#'+thisId).find('.report-body-column2').addClass('selected');
					$(this).addClass('selected');
			    	$('#'+thisId).find('.report-body-column2').attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
			    	$(this).attr("clicked","clicked").css("font-size","13px").css("background","#aaaaaa");
			    }
				});
		},
		getUsersNoMail : function(){
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
						rowstr+='<div id="alert-users-grid-no-email" class="report-body user" style="width: 200px; height: auto; overflow: hidden;">';
						rowstr+='<a href="#"><div id="usersColumn-'+loop+'" class=" user report-body-column2 centered sameHeight" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+data[loop]+'</div>'+'</a>'
						rowstr+='</div>';
						$('#users-search-results-list').append(rowstr);
					};
					}
				});
		},
		commonForEmail: function(group){
			/*var groups = [];
			var finalArray =groups.push(group);
			alert(finalArray)*/
			$.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-associated-users&group='+group,
				async : false,
				success: function(data){
						var result = data.result.data;
				        if(result!=undefined){
						var alternate = false;
						if($('.userColoumn1').length > 0){
							var loopValue = $('.userColoumn1').length +1;
							for(var loop=loopValue;loop<result.length+loopValue;loop=loop+1){
								var rowstr='';
								rowstr+='<div id="alert-users-grid-'+loop+'" class="report-body userGrid" style="width: 200px; height: auto; overflow: hidden;">';
								rowstr+='<a href="#"><div id="userColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn1" clicked=false style="height: inherit; width: 110px; word-wrap: break-word;">'+result[loop-loopValue]+'</div>'+'</a>'
								rowstr+='<div id="userDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn2" style="height: inherit; width: 55px" value="abc">'+'<select  class="user-dropdown" style= width:70px;><option value = -1>Select</option>'+
								'<option value=to>To</option>'+
								'<option value=cc>Cc</option>'+
								'<option value=bcc>Bcc</option>'+
								'</select>'+'</div>';
								rowstr+='</div>';
								rowstr+='</div>';
								$('#users-search-results-list').append(rowstr);
							}
						}else{
							for(var loop=0;loop<result.length;loop=loop+1){
								var rowstr='';
								rowstr+='<div id="alert-users-grid-'+loop+'" class="report-body userGrid" style="width: 200px; height: auto; overflow: hidden;">';
								rowstr+='<a href="#"><div id="userColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn1" clicked=false style="height: inherit; width: 110px; word-wrap: break-word;">'+result[loop]+'</div>'+'</a>'
								rowstr+='<div id="userDropdownColumn-'+loop+'" class="report-body-column2 centered sameHeight userColoumn2" style="height: inherit; width: 55px" value="abc">'+'<select  class="user-dropdown" style= width:70px;><option value = -1>Select</option>'+
								'<option value=to>To</option>'+
								'<option value=cc>Cc</option>'+
								'<option value=bcc>Bcc</option>'+
								'</select>'+'</div>';
								rowstr+='</div>';
								rowstr+='</div>';
								$('#users-search-results-list').append(rowstr);
							}
						}
						
						};
					},
					});
			$('.userGrid').die('click').live('click',function(){
				var id = $(this).find('.report-body-column2').attr('id');
				var thisClass = $('#'+id).attr("class");
				var selectedElem = $(this).find('#'+id).attr('clicked');
				var thisId = $(this).attr("id");
				if($(this).hasClass('selected')){
					$('.'+thisClass).parent().parent().removeClass('selected');
					$(this).removeClass('selected')
					$(this).find('.userColoumn1').css("font-size","13px").css("background","");
					$(this).css("font-size","13px").css("background","");
					evt.preventDefault();
			    }else{
			    	$('.'+thisClass).parent().parent().addClass('selected');
			    	$(this).addClass('selected')
					$(this).find('.userColoumn1').css("font-size","13px").css("background","#aaaaaa");
			    	$('#'+id).find('.userColoumn1').css("font-size","13px").css("background","#aaaaaa");
			    	evt.preventDefault();
			    }
			});
		
		},
		commonForNomail : function(group){
			$.ajax({type: "POST",
				url:'alerts.json', 
				data: 'action=get-associated-users&group='+group,
				async : false,
				success: function(data){
				var result = data.result.data;
		        if(result!=''){
				var alternate = false;
				for(var loop=0;loop<result.length;loop=loop+1){
					var rowstr='';
					rowstr+='<div id="alert-users-grid-no-email" class="report-body user" style="width: 200px; height: auto; overflow: hidden;">';
					rowstr+='<a href="#"><div id="usersColumn-'+loop+'" class=" user report-body-column2 centered sameHeight userColoumnnoemail1" clicked=false style="height: inherit; width: 200px; word-wrap: break-word;">'+result[loop]+'</div>'+'</a>'
					rowstr+='</div>';
					$('#users-search-results-list').append(rowstr);
				};
				}
			},
			});
			$('#alert-users-grid-no-email').die('click').live('click',function(){
				var selectedElem = $(this).find('#'+id).attr('clicked');
				var id = $(this).find('.report-body-column2').attr('id');
				var thisClass = $('#'+id).attr("class");
				if($(this).hasClass('selected')){
					$('.'+thisClass).parent().parent().removeClass('selected');
					$(this).removeClass('selected')
					$(this).find('.userColoumnnoemail1').css("font-size","13px").css("background","");
					$(this).css("font-size","13px").css("background","");
					evt.preventDefault();
					
			    }else{
			    	$('.'+thisClass).parent().parent().addClass('selected');
			    	$(this).addClass('selected')
					$(this).find('.userColoumnnoemail1').css("font-size","13px").css("background","#aaaaaa");
			    	$('#'+id).find('.userColoumnnoemail1').css("font-size","13px").css("background","#aaaaaa");
			    	evt.preventDefault();
			    }
				});
		},
		
		initAddButtons : function() {
			$('#user-defined-alert').click(function(){
				UserDefinedAlertsHandler.userDefinedalertStepCount =0;
			})
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
			$('#action-clear').click(function() {
				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: "<span class='ui-dlg-confirm'>Confirm</span>",
						modal: true,
						buttons: {
							'Ok' : function() {
								$('form').clear();
								$(this).dialog('close');
							},
					Cancel: function() {
						$(this).dialog('close');
					}
						}
					});
				
			});
			
			$('#action-cancel').click(function() {
					$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
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
				if(SystemDefinedAlertsHandler.validateAlerts() == false){
					return false;
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
			
			$('#action-assign').click(function(){
				AlertsHandler.assignMuliple();
			});
			
		},
validateAlerts : function(){
	var formid = $('form').attr('id');
	var result = true;
	$('#'+formid+" :input").each(function(index,elm){
		if(elm.name == "description" && elm.className == "mandatory"){
			if($('#alertDescription').val()==""){
				$('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#alertDescription").focus(function(event){
					$('#descriptionValid').empty();
					 $('#desc_pop').show();
				});
				$("#alertDescription").blur(function(event){
					 $('#desc_pop').hide();
					 if($('#alertDescription').val().length==0){
						 $('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#alertDescription").focus(function(event){
							 $('#descriptionValid').empty();
							 if($('#alertDescription').val().length==0){
								 $('#desc_pop').show();
							 }
							});
					 }else{
						 $('#desc_pop').hide();
					 }
				});
				result =false; 
			}
		
		}
		if(elm.name == "alertName"){
			var alertName = $('#alertName').val();
			var end = $('#alertName').val().length;
			if(/^[a-zA-Z0-9\s]+$/.test($('#alertName').val()) == false){
				$('#alertNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#alertName").focus(function(event){
					$('#alertNameValid').empty();
					 $('#alertName_pop').show();
				});
			
			$("#alertName").blur(function(event){
				 $('#alertName_pop').hide();
				 if(/^[a-zA-Z0-9\s]+$/.test($('#alertName').val())==false){
					 $('#alertNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#alertName").focus(function(event){
						 $('#alertNameValid').empty();
						 $('#alertName_pop').show();
					 });
					 
				 }else{
					 $('#alertName_pop').hide();
				 }
			});
			result=false;
			}
			if($('#alertName').val().charAt(0) == " "||$('#alertName').val().charAt(end -1) == " "){

				$('#alertNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
				$("#alertName").focus(function(event){
					$('#alertNameValid').empty();
					 $('#alertName_pop').hide();
					 $('#alertNamespaces_pop').show();
				});
			
			$("#alertName").blur(function(event){
				 $('#alertNamespaces_pop').hide();
				 if($('#alertName').val().charAt(end -1)==" "){
					 $('#alertNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#alertName").focus(function(event){
						 $('#alertNameValid').empty();
						 $('#alertName_pop').hide();
						 $('#alertNamespaces_pop').show();
					 });
					 
				 }else{
					 $('#alertName_pop').hide();
				 }
			});
			result=false;
			
			}
		}
		if(elm.name == "alertType"){
			if($('#alertType').val() == "-1" || $('#alertType').val() == null){

			$('#alertTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
			$("#alertType").focus(function(event){
				if($('#alertType').val() == "-1"){
					$('#alertTypeValid').empty();
					 $('#alertType_pop').show();
				}else{
					$('#alertTypeValid').empty();
					 $('#alertType_pop').hide();
				}
				 });
			$("#alertType").blur(function(event){
				 $('#alertType_pop').hide();
					if($('#alertType').val() == "-1" ||$('#alertType').val() == null){
					 $('#alertTypeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#alertType").focus(function(event){
						 $('#alertType_pop').hide();
						 $('#alertTypeValid').empty();
						 $('#alertType_pop').show();
					 });
					 
				 }else{
					 $('#alertType_pop').hide();
				 }
			});
			result =false;
		
		}
		}
		if($('#alertType').val() == "Trending"){
			if(elm.name == "productPercentage" && $('#productPercentage').val().length >0){
				var productVal = $('#productPercentage').val().length
				if(/^[0-9.]+$/.test($('#productPercentage').val())==false||($('#productPercentage').val()).length==0||$('#productPercentage').val().charAt(0)==" "||$('#productPercentage').val().charAt(productVal -1)==" "||$('#productPercentage').val()>100){
					$('#productPercentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#productPercentage").focus(function(event){
						$('#productPercentValid').empty();
						 $('#product_pop').show();
					});
					$("#productPercentage").blur(function(event){
						 $('#product_pop').hide();
						 if(/^[0-9.]+$/.test($('#productPercentage').val())==false||($('#productPercentage').val()).length==0||$('#productPercentage').val().charAt(0)==" "||$('#productPercentage').val().charAt(productVal -1)==" "||$('#productPercentage').val()>100){
							 $('#productPercentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#productPercentage").focus(function(event){
								 $('#productPercentValid').empty();
								 $('#product_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
			}
		}
			if(elm.name == "amountPersentage" && $('#amountPercentage').val().length >0){
				var amtVal = $('#amountPercentage').val().length
				if(/^[0-9.]+$/.test($('#amountPercentage').val())==false||($('#amountPercentage').val()).length==0||$('#amountPercentage').val().charAt(0)==" "||$('#amountPercentage').val().charAt(amtVal -1)==" "||$('#amountPercentage').val()>100){
					$('#amountPercentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#amountPercentage").focus(function(event){
						$('#amountPercentValid').empty();
						 $('#amountP_pop').show();
					});
					$("#amountPercentage").blur(function(event){
						 $('#amountP_pop').hide();
						 if(/^[0-9.]+$/.test($('#amountPercentage').val())==false||($('#amountPercentage').val()).length==0||$('#amountPercentage').val().charAt(0)==" "||$('#amountPercentage').val().charAt(amtVal -1)==" "||$('#amountPercentage').val()>100){
							 $('#amountPercentValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#amountPercentage").focus(function(event){
								 $('#amountPercentValid').empty();
								 $('#amountP_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
			}
		}	
		}
		if($('#alertType').val() == "Excess Amount"){
			if(elm.name == "amount"){
				var amtVal = $('#amount').val().length
				if(/^[0-9]+$/.test($('#amount').val())==false||($('#amount').val()).length==0||$('#amount').val().charAt(0)==" "||$('#amountPercentage').val().charAt(amtVal -1)==" "){
					$('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#amount").focus(function(event){
						$('#amountValid').empty();
						 $('#amount_pop').show();
					});
					$("#amount").blur(function(event){
						 $('#amount_pop').hide();
						 if(/^[0-9.]+$/.test($('#amount').val())==false||($('#amount').val()).length==0||$('#amount').val().charAt(0)==" "||$('#amount').val().charAt(amtVal -1)==" "){
							 $('#amountValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $("#amount").focus(function(event){
								 $('#amountValid').empty();
								 $('#amount_pop').show();
							 });
							
						 }else{
							// $('#cityValid').html("<img
							// src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
			}
		}	
		}
	});
	return result;
},
		
};
var UserDefinedAlertsHandler = {
		userDefinedalertSteps : ['#user-defined-alerts-form','#user-defined-alerts-form-notifications'],
		userDefinedalertUrl : [ 'alerts.json', 'alerts.json'],
		userDefinedalertStepCount : 0,
		initAddButtons : function(){
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
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
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
					},
				});
				$(this).dialog('close');
				
			});
			
			$('#action-cancel').click(function() {
					$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
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
					if(UserDefinedAlertsHandler.userDefinedalertStepCount != UserDefinedAlertsHandler.userDefinedalertSteps.length) {
						$('#button-update').hide();
					}
					if(UserDefinedAlertsHandler.userDefinedalertStepCount == 0){
						if(SystemDefinedAlertsHandler.validateAlerts() == false){
							return false;
						}
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
						if(mySales == ""){
							showMessage({title:'Warning', msg:'Please select atleast one My Sales type.'});
						    return false;
						}
						paramString = values+'&action=user-defined-alerts-form'+'&alertMySalesPage='+mySalesType+'&alertMySales='+mySales+'&salesTypeArray='+saleTypesArray;
						UserDefinedAlertsHandler.previewFunction(paramString);
						
					}else if($('#alertType').val()=="Trending"){
						PageHandler.pageSelectionHidden =false;
						PageHandler.hidePageSelection();
						SystemDefinedAlertsHandler.expanded=true;
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
					}else if(UserDefinedAlertsHandler.userDefinedalertStepCount == 1){
						PageHandler.pageSelectionHidden =true;
						PageHandler.hidePageSelection();
						SystemDefinedAlertsHandler.expanded=true;
					}
			}else{
				var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
				var notification = $('#'+notificationId).text();
				if(UserDefinedAlertsHandler.userDefinedalertStepCount == 1){
				if(notification == "") {
					showMessage({title:'Warning', msg:'Please select atleast one notification type.'});
				    return false;
				}
				}
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
				var groupArray = [];
				$('#groups-search-results-list').find('.selected').each(function(){
			        var id = $(this).find('.report-body-column2').attr('id');
			        var group = $('#'+id).text();
			        if(notification == 'Emails') {
			        	var groupDropdown = $(this).find('.group-dropdown').val();
				        group = group + '/' + groupDropdown;
			        }
			        groupArray.push(group);
			    });
				 paramString= $(UserDefinedAlertsHandler.userDefinedalertSteps[UserDefinedAlertsHandler.userDefinedalertStepCount]).serialize()+'&group='+groupArray+'&notificationType='+notification+'&usersArray='+res;
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
					$('#button-update').hide();
					$('#alerts-preview-container').html('');
					$('#alerts-preview-container').hide();
					$('.page-buttons').css('margin-left', '150px');
				}
				$(UserDefinedAlertsHandler.userDefinedalertSteps[UserDefinedAlertsHandler.userDefinedalertStepCount]).hide();
				$(UserDefinedAlertsHandler.userDefinedalertSteps[--UserDefinedAlertsHandler.userDefinedalertStepCount]).show();
				if(UserDefinedAlertsHandler.userDefinedalertStepCount == 0){
					$("#action-assign").hide();
				}
				if(UserDefinedAlertsHandler.userDefinedalertStepCount == 1){
					$("#action-assign").show();
				}
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
			/*	var notificationId = $('#notification-search-results-list').find('.selected').find('.report-body-column2').attr('id');
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
				var user = usersArray.join(',');*/
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
				if(typeof edit == 'undefined' || edit == 'undefined'){
					$('#button-save').show();
				}else if(edit == true){
					$('#button-update').show();
				}
				$("#action-assign").hide();
				$('#button-next').hide();
				$('#action-clear').hide();
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
							}else{
								$('#button-update').hide();
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
		
		validateDescription : function(){
			var result = true;
			if($('#alertDescription').val()==""){
				$('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#alertDescription").focus(function(event){
					$('#descriptionValid').empty();
					 $('#desc_pop').show();
				});
				$("#alertDescription").blur(function(event){
					 $('#desc_pop').hide();
					 if($('#alertDescription').val().length==0){
						 $('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#alertDescription").focus(function(event){
							 $('#descriptionValid').empty();
							 if($('#alertDescription').val().length==0){
								 $('#desc_pop').show();
							 }
							});
					 }else{
						 $('#desc_pop').hide();
					 }
				});
				result =false; 
			}
			return result;
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
			$.post('alerts.json','action=get-mysales-types',function(obj){
				var res = obj.result.data;
				if(res.length>0){
					$('#mysales-search-results-list').html('');
					for ( var loop = 0; loop < res.length; loop++) {
						var rowstr='';
						rowstr+='<div id="alert-mySales-grid" class="report-body" style="width: 200px; height: auto; overflow: hidden;">';
						rowstr+='<a href="#"><div id="mySalesColumn-'+loop+'" class="report-body-column2 centered sameHeight salesType" style="height: inherit; width: 190px; word-wrap: break-word;">'+res[loop]+'</div>'+'</a>'
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
					});
				}
				
				
			});
		}
};

var AlertsHistoryHandler = {
		myAlertsPageNumber: 1,
		myAlertNumberOfPages: 1,
		myAlertcount: 1,
		alertHistoryPageNumber: 1,
		alertHistoryNumberOfPages: 1,
		alertHistorycount: 1,
		result:false,
		initAlertHistoryOnload: function() {
			var paramString = $('#alert-history-form').serialize();
			var startDate = $('#startDate').val();
			var endDate = $('#endDate').val();
			$.post('alerts.json', 'action=get-alertHistory-records-count&startDate='+startDate+'&endDate='+endDate, function(obj){
				var data1 = obj.result.data;
				var numberOfPages=(data1/50)+0.5;
				AlertsHistoryHandler.myAlertNumberOfPages=Math.round(numberOfPages);
			$.post('alerts.json', paramString+'&startDate='+startDate+'&endDate='+endDate+'&pageNumber='+AlertsHistoryHandler.alertHistoryPageNumber, function(obj) {
				var data = obj.result.data;
				$('#search-results-list').html('');
				$('.grid-content').html('');
				if(data != undefined) {
					AlertsHistoryHandler.loadAlertsHistoryGrid(data);
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Available</div></div></div>');
				}
				AlertsHistoryHandler.initAlertHistoryGridActionButtons();
			   });
			});
			$('#action-search-history').click(function() {
				var alertCategory = $('#alertCategory').val();
				var alertType = $('#alertType').val();
				var startDate = $('#startDate').val();
				var endDate = $('#endDate').val();
				var alertName = $('#alertName').val();
				$.post('alerts.json', 'action=get-alertHistory-records-count&alertCategory='+alertCategory+'&alertType='+alertType+'&alertName='+alertName+'&startDate='+startDate+'&endDate='+endDate, function(obj){
					var data1 = obj.result.data;
					var numberOfPages=(data1/50)+0.5;
					AlertsHistoryHandler.myAlertNumberOfPages=Math.round(numberOfPages);
					$.post('alerts.json', 'action=search-criteria-for-alerts-history&alertCategory=' +alertCategory+'&alertType='+alertType+'&alertName='+alertName+'&startDate='+startDate+'&endDate='+endDate+'&pageNumber='+Math.round(numberOfPages), function(obj) {
					var data = obj.result.data;
					$('#search-results-list').html('');
					$('.grid-content').html('');
					if(data != undefined) {
						AlertsHistoryHandler.loadAlertsHistoryGrid(data);
					} else {
						$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Available</div></div></div>');
					}
					AlertsHistoryHandler.initAlertHistoryGridActionButtons();
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
			$('#history-clear').click(function(){
			$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$('form').clear();
							$('#startDate').val($.datepicker.formatDate('dd-mm-yy', new Date()));
							$('#endDate').val($.datepicker.formatDate('dd-mm-yy', new Date()));
							$(this).dialog('close');
						},
				Cancel: function() {
					$(this).dialog('close');
				}
					}
				});
			});
		},
		
		// Grid for alerts history.
		loadAlertsHistoryGrid: function(data) {
			var numberOfPages=(data.length/50)+0.5;
			AlertsHistoryHandler.myAlertNumberOfPages=Math.round(numberOfPages);
			var alertsGridHeader ='';
			alertsGridHeader += '<div class="report-header" style="width:848px; height: 30px;">'+
			'<div class="report-header-column2 centered" style="width:50px;">' + Msg.ALERT_SERIAL_NO + '</div>' +
			'<div class="report-header-column2 centered alertHistoryalertName" style="width: 150px;">' + Msg.ALERT_NAME_LABEL + '</div>' +
			'<div class="report-header-column2 centered alertHistorydescription" style="width: 250px;">' + Msg.ALERT_DESCRIPTION_LABEL + '</div>' +
			'<div class="report-header-column2 centered alertHistorycreatedBy" style="width: 150px;">' + Msg.ALERT_CREATED_USER + '</div>' +
			'<div class="report-header-column2 centered alertHistorycreatedOn" style="width: 150px;">' + Msg.ALERT_CREATED_DATE_LABEL + '</div>' +
			'</div>';
			$('#search-results-list').append(alertsGridHeader);
			$('#search-results-list').append('<div class="grid-content" style="height:200px;width: 848px;"></div>');
			for(var loop=1;loop-1<data.length;loop=loop+1) {
				var alertsGridData = '<div class="ui-content report-content" id = "alert-history-grid">';
				alertsGridData+='<div class="report-body" style="width:848px; height: auto; overflow: hidden;">';
				/*alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 150px; word-wrap: break-word;">'+data[loop].id+'</div>';*/
				alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 50px; word-wrap: break-word;">'+loop+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight alertHistoryalertName" style="width: 150px; word-wrap: break-word;">'+data[loop-1].alertName+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight alertHistorydescription" style="width: 250px; word-wrap: break-word;">'+data[loop-1].description+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight alertHistorycreatedBy" style="width: 150px; word-wrap: break-word;">'+data[loop-1].createdUser+'</div>';
				alertsGridData += '<div class="report-body-column2 centered sameHeight alertHistorycreatedOn" style="width: 150px; word-wrap: break-word;">'+data[loop-1].createdOn+'</div>';
				alertsGridData += '</div>';
				alertsGridData += '</div>';
				$('.grid-content').append(alertsGridData);
				AlertsHistoryHandler.alertHistorycount=AlertsHistoryHandler.alertHistorycount+1;
			};
			var myAlertHistoryPages="";
			myAlertHistoryPages +='<div class="report-header" style="width: 698px; height: 30px;">';
             if(AlertsHistoryHandler.alertHistoryPageNumber==1){
            	 myAlertHistoryPages +='<div firstPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px; pointer-events: none;"  title="Click First"></div>';
             }else{
            	 myAlertHistoryPages +='<div firstPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px;"  title="Click First"></div>'; 
             }
             if(AlertsHistoryHandler.alertHistoryPageNumber==1){
            	 myAlertHistoryPages +='<div previousPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none; pointer-events: none;"  title="Click Previous"></div>';	 
             }else{
            	 myAlertHistoryPages +='<div previousPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none;"  title="Click Previous"></div>';
             }
             myAlertHistoryPages += '<div id="current-page-number" class="input-field"><input name="pageNumber" id="pageNumber" value="'+ AlertsHistoryHandler.alertHistoryPageNumber +'" style="width: 30px; margin-left: 40px; padding-left: 20px;"> of '+ AlertsHistoryHandler.alertHistoryNumberOfPages +'</div>';
            if(AlertsHistoryHandler.alertHistoryPageNumber==AlertsHistoryHandler.alertHistoryNumberOfPages){
            	myAlertHistoryPages +='<div nextPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Next"></div>';
            }else{
            	myAlertHistoryPages +='<div nextPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none"  title="Click Next"></div>';
            }
            if(AlertsHistoryHandler.alertHistoryPageNumber==AlertsHistoryHandler.alertHistoryNumberOfPages){
            	myAlertHistoryPages +='<div lastPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Last"></div>';
            }else{
            	myAlertHistoryPages +='<div lastPageNumber="'+AlertsHistoryHandler.alertHistoryPageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none"  title="Click Last"></div>';
            }
            myAlertHistoryPages +='</div>';
            $('#search-results-list').append(myAlertHistoryPages);
			$('.grid-content').jScrollPane({showArrows:true});
			//AlertsHistoryHandler.MyAlertsPageHandler();
		},
		
		initAlertHistoryGridActionButtons:function(){
			$('.last-btn').click(function() {
				var alertCategory = $('#alertCategory').val();
				var alertType = $('#alertType').val();
				var startDate = $('#startDate').val();
				var endDate = $('#endDate').val();
				var alertName = $('#alertName').val();
				$.post('alerts.json', 'action=get-alertHistory-records-count&alertCategory='+alertCategory+'&alertType='+alertType+'&alertName='+alertName+'&startDate='+startDate+'&endDate='+endDate, function(obj){
					data = obj.result.data;
					var numberOfPages=(data/50)+0.5;
					AlertsHistoryHandler.alertHistoryPageNumber=Math.round(numberOfPages);
					AlertsHistoryHandler.result=true;
					AlertsHistoryHandler.initMyAlertHistoryOnload();
					});
				});
			$('.first-btn').click(function() {
					var pageNumber = $(this).attr('firstPageNumber');
					var firstPageNumber=parseInt(pageNumber-(pageNumber-1));
					AlertsHistoryHandler.alertHistoryPageNumber=firstPageNumber;
					AlertsHistoryHandler.result=true;
					AlertsHistoryHandler.initMyAlertHistoryOnload();
				});
			
			$('.next-btn').click(function(){
				var pageNumber = $(this).attr('nextPageNumber');
				var nextPageNumber=parseInt(pageNumber)+1;
				AlertsHistoryHandler.alertHistoryPageNumber=nextPageNumber;
				AlertsHistoryHandler.result=true;
				AlertsHistoryHandler.initMyAlertHistoryOnload();
				AlertsHistoryHandler.initMyAlertHistoryOnload((pageNumber*50)+1);
			});
			$('.previous-btn').click(function(){
				var firstRecordCount = $(this).attr('firstRecordCount');
				var pageNumber = $(this).attr('previousPageNumber');
				var prevPageNumber= pageNumber-1;
				AlertsHistoryHandler.alertHistoryPageNumber=prevPageNumber;
				AlertsHistoryHandler.result=true;
				var previousPageCount=(prevPageNumber*50)-49;
				AlertsHistoryHandler.initMyAlertHistoryOnload(previousPageCount);
			});
			/*$('.next-btn').click(function(){
				var pageNumber = $(this).attr('nextPageNumber');
				var nextPageNumber=parseInt(pageNumber)+1;
				AlertsHistoryHandler.alertHistoryPageNumber=nextPageNumber;
				AlertsHistoryHandler.result=true;
				AlertsHistoryHandler.initMyAlertHistoryOnload();
			});
			$('.previous-btn').click(function(){
				alert('click');
				var pageNumber = $(this).attr('previousPageNumber');
				alert(pageNumber)
				var prevPageNumber=pageNumber-1;
				AlertsHistoryHandler.alertHistoryPageNumber=prevPageNumber;
				AlertsHistoryHandler.result=true;
				AlertsHistoryHandler.initMyAlertHistoryOnload();
			});*/
			$('#pageNumber').live("keypress", function(e) {
		        if (e.keyCode == 13) {
		            var currentPageNumber=$('#pageNumber').val();
		            if(currentPageNumber > AlertsHistoryHandler.alertHistoryNumberOfPages || currentPageNumber < 1 ){
		            	 showMessage({title:'Warning', msg:"please enter page number between"+ " "+ 1 +" "+ "and" + " "+ AlertsHistoryHandler.alertHistoryNumberOfPages +"."});
		            	 $('#pageNumber').val(1); 
		            	 evt.preventDefault();
					       return;
		            }else{
		            	AlertsHistoryHandler.alertHistoryPageNumber=currentPageNumber;
		            	AlertsHistoryHandler.initMyAlertHistoryOnload();
		            }
		        }
		  });
		},
		// Grid for my alerts.
		initMyAlertHistoryOnload: function(previousPageCount) {
			$.post('alerts.json', 'action=get-myalerts-records-count', function(obj){
				var data1 = obj.result.data;
				var numberOfPages=(data1/50)+0.5;
				AlertsHistoryHandler.myAlertNumberOfPages=Math.round(numberOfPages);
			$.post('alerts.json', 'action=get-my-alert-history&pageNumber='+AlertsHistoryHandler.myAlertsPageNumber, function(obj) {
				data = obj.result.data;
				var firstRecordCount;
				$('#search-results-list').html('');
				if(undefined != data) {
					var alertsGridHeader ='';
					alertsGridHeader += '<div class="report-header" style="width:848px; height: 30px;">'+
					'<div class="report-header-column2 centered" style="width:50px;">' + Msg.ALERT_SERIAL_NO + '</div>' +
					'<div class="report-header-column2 centered alertName" style="width: 150px;">' + Msg.ALERT_NAME_LABEL + '</div>' +
					'<div class="report-header-column2 centered description" style="width: 300px;">' + Msg.ALERT_DESCRIPTION_LABEL + '</div>' +
					'<div class="report-header-column2 centered createdBy" style="width: 100px;">' + Msg.ALERT_CREATED_USER + '</div>' +
					'<div class="report-header-column2 centered createdOn" style="width: 150px;">' + Msg.ALERT_CREATED_DATE_LABEL + '</div>' +
					'</div>'
					$('#search-results-list').append(alertsGridHeader);
					$('#search-results-list').append('<div class="grid-content" style="height:390px; width:848px;"></div>');
					for(var loop=1;loop<=data.length;loop=loop+1) {
						var alertsGridData = '<div class="ui-content report-content" id = "alert-history-grid">';
						alertsGridData+='<div class="report-body" style="width:848px; height: 40px; overflow: hidden;">';
						if(previousPageCount == undefined){
						alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 50px;height: 40px; word-wrap: break-word;">'+ loop +'</div>';
						}else{
							alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 50px;height: 40px; word-wrap: break-word;">'+ previousPageCount +'</div>';
						}
						alertsGridData += '<div class="report-body-column2 centered sameHeight alertName" style="width: 150px; height: 40px; word-wrap: break-word;">'+data[loop-1].alertName+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight description" style="width: 300px; height: 40px; word-wrap: break-word;">'+data[loop-1].description+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight createdBy" style="width: 100px; height: 40px; word-wrap: break-word;">'+data[loop-1].createdBy+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight createdOn" style="width: 150px; height: 40px; word-wrap: break-word;">'+data[loop-1].createdOn+'</div>';
						alertsGridData += '</div>';
						alertsGridData += '</div>';
						$('.grid-content').append(alertsGridData);
						if(previousPageCount == undefined){
						AlertsHistoryHandler.myAlertcount=AlertsHistoryHandler.myAlertcount+1;
						}else{
							previousPageCount=previousPageCount+1;
						}
						if(loop == 1){
							 firstRecordCount=AlertsHistoryHandler.myAlertcount;
						}
					};
					var mysalesAlertsPages="";
					mysalesAlertsPages +='<div class="report-header" style="width: 830px; height: 30px;">';
		             if(AlertsHistoryHandler.myAlertsPageNumber==1){
		            	 mysalesAlertsPages +='<div firstPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px; pointer-events: none;"  title="Click First"></div>';
		             }else{
		            	 mysalesAlertsPages +='<div firstPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px;"  title="Click First"></div>'; 
		             }
		             if(AlertsHistoryHandler.myAlertsPageNumber==1){
		            	 mysalesAlertsPages +='<div previousPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none; pointer-events: none;"  title="Click Previous"></div>';	 
		             }else{
		            	 mysalesAlertsPages +='<div previousPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" firstRecordCount="'+ firstRecordCount +'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none;"  title="Click Previous"></div>';
		             }
		             mysalesAlertsPages += '<div id="current-page-number" class="input-field"><input name="pageNumber" id="pageNumber" value="'+ AlertsHistoryHandler.myAlertsPageNumber +'" style="width: 30px; margin-left: 40px; padding-left: 20px;"> of '+ AlertsHistoryHandler.myAlertNumberOfPages +'</div>';
		            if(AlertsHistoryHandler.myAlertsPageNumber==AlertsHistoryHandler.myAlertNumberOfPages){
		            	mysalesAlertsPages +='<div nextPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Next"></div>';
		            }else{
		            	mysalesAlertsPages +='<div nextPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none"  title="Click Next"></div>';
		            }
		            if(AlertsHistoryHandler.myAlertsPageNumber==AlertsHistoryHandler.myAlertNumberOfPages){
		            	mysalesAlertsPages +='<div lastPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Last"></div>';
		            }else{
		            	mysalesAlertsPages +='<div lastPageNumber="'+AlertsHistoryHandler.myAlertsPageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none"  title="Click Last"></div>';
		            }
		            mysalesAlertsPages +='</div>';
		            $('#search-results-list').append(mysalesAlertsPages);
					$('.grid-content').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">' + obj.result.message + '</div></div></div>');
				}
				AlertsHistoryHandler.initGridActionButtons();
			});
		});
			//AlertsHistoryHandler.MyAlertsPageHandler();
	},
	initGridActionButtons:function(){
		$('#my-alerts').click(function(){
			var pageNumber = 0;
			var nextPageNumber=parseInt(pageNumber)+1;
			AlertsHistoryHandler.myAlertsPageNumber=nextPageNumber;
			AlertsHistoryHandler.result=true;
			AlertsHistoryHandler.initMyAlertHistoryOnload((pageNumber*50)+1);
		
		});
		$('.last-btn').click(function() {
			$.post('alerts.json', 'action=get-myalerts-records-count', function(obj) {
				data = obj.result.data;
				var numberOfPages=(data/50)+0.5;
				AlertsHistoryHandler.myAlertsPageNumber=Math.round(numberOfPages);
				AlertsHistoryHandler.result=true;
				AlertsHistoryHandler.initMyAlertHistoryOnload();
				});
			});
		$('.first-btn').click(function() {
				var pageNumber = $(this).attr('firstPageNumber');
				var firstPageNumber=parseInt(pageNumber-(pageNumber-1));
				AlertsHistoryHandler.myAlertsPageNumber=firstPageNumber;
				AlertsHistoryHandler.result=true;
				AlertsHistoryHandler.myAlertcount=1;
				AlertsHistoryHandler.initMyAlertHistoryOnload();
			});
		$('.next-btn').click(function(){
			var pageNumber = $(this).attr('nextPageNumber');
			var nextPageNumber=parseInt(pageNumber)+1;
			AlertsHistoryHandler.myAlertsPageNumber=nextPageNumber;
			AlertsHistoryHandler.result=true;
			AlertsHistoryHandler.initMyAlertHistoryOnload((pageNumber*50)+1);
		});
		$('.previous-btn').click(function(){
			var firstRecordCount = $(this).attr('firstRecordCount');
			var pageNumber = $(this).attr('previousPageNumber');
			var prevPageNumber= pageNumber-1;
			AlertsHistoryHandler.myAlertsPageNumber=prevPageNumber;
			AlertsHistoryHandler.result=true;
			var previousPageCount=(prevPageNumber*50)-49
			AlertsHistoryHandler.initMyAlertHistoryOnload(previousPageCount);
		});
		$('#pageNumber').live("keypress", function(e) {
	        if (e.keyCode == 13) {
	            var currentPageNumber=$('#pageNumber').val();
	            if(currentPageNumber > AlertsHistoryHandler.myAlertNumberOfPages || currentPageNumber < 1 ){
	            	 showMessage({title:'Warning', msg:"please enter page number between"+ " "+ 1 +" "+ "and" + " "+ AlertsHistoryHandler.myAlertNumberOfPages +"."});
	            	 $('#pageNumber').val(1); 
	            	 evt.preventDefault();
				       return;
	            }else{
	            	AlertsHistoryHandler.myAlertsPageNumber=currentPageNumber;
	            	AlertsHistoryHandler.initMyAlertHistoryOnload();
	            }
	        }
	  });
	},
	/*MyAlertsPageHandler: function(){
		$('#ps-exp-col').click(function() {
		    if(PageHandler.expanded) {
		    	//alert("expand");
		    	$('#search-results-list').css("width","698px");
		    	$( '.report-header' ).css( "width", "698px" );
		    	$( '.report-body' ).css( "width", "698px" );
		    	$( '.alertName' ).css( "width", "150px" );
		    	$( '.description' ).css( "width", "350px" );
		    	$('.grid-content').css( "width", "698px" );
				$( '.createdBy' ).css( "width", "100px" );
				$( '.alertHistoryalertName' ).css( "width", "150px" );
		    	$( '.alertHistorydescription' ).css( "width", "150px" );
				$( '.alertHistorycreatedBy' ).css( "width", "150px" );
				$( '.jScrollPaneContainer' ).css( "width", "698px" );
		    	
			} else {
				//alert("compress");
				$('#search-results-list').css("width","830px");
				$( '.report-header' ).css( "width", "830px" );
				$( '.report-body' ).css( "width", "830px" );
				$( '.alertName' ).css( "width", "170px" );
		    	$( '.description' ).css( "width", "370px" );
		    	$('.grid-content').css( "width", "830px" );
				$( '.createdBy' ).css( "width", "120px" );
				$( '.jScrollPaneContainer' ).css( "width", "830px" );
				$( '.alertHistoryalertName' ).css( "width", "200px" );
		    	$( '.alertHistorydescription' ).css( "width", "200px" );
				$( '.alertHistorycreatedBy' ).css( "width", "200px" );
				
			}
			setTimeout(function() {
				$('#search-results-list').jScrollPane({
					showArrows : true
				});
			}, 0);
		});
	}*/
};