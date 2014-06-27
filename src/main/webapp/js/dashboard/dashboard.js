var DashbookHandler = {
		// For Alerts.
		initAlertOnLoad: function(){
			var paramString='action=search-alert-dashboard';
			$.post('changeTransaction.json', paramString,
			function(obj){
				var data = obj.result.data;
				$('#search-alerts-results-list').html('');
						if(data != undefined) {
							var alternate = false;
							for(var loop=0;loop<data.length;loop=loop+1) {
								if(alternate) {
									var rowstr = '<a href="#"><div class="green-result-row alternate search-row-results" id="alert-row"  align="'+data[loop].id+'" style="height: 80px; width: 210px;">';
								} else {
									rowstr = '<a href="#"><div id="alert-row" class="green-result-row search-row-results" align="'+data[loop].id+'"  style="height: 80px; width: 210px;">';
								}
								alternate = !alternate;
								rowstr = rowstr + '<div class="green-result-col-1 search-row-results" style="width: 210px;">'+
								'<div class="result-body">' +
								'<div class="result-title">' + data[loop].alertType + '</div>' +
								'<span class="property">'+Msg.ALERT_NAME_LABEL +':'+' </span><span class="property-value">' + data[loop].alertName + '</span>' +
								'</div>' +
								'</div>' +
								'<div class="green-result-col-2">'+
								'<div class="result-body">' +
								'</div>' +'<span class="property">'+Msg.ALERT_DESCRIPTION_LABEL +':'+' </span><span class="property-value">' + data[loop].description + '</span>' +
								'</div>' +
								'</a>';
						$('#search-alerts-results-list').append(rowstr);
					};
					$('#search-alerts-results-list').jScrollPane({showArrows:true});
						}
						 else {
								$('#search-alerts-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No Alerts Configured</div></div></div>');
							  }
							$.loadAnimation.end();
					});
			$('#ps-exp-col').click(function() {
			    if(PageHandler.expanded) {
			    	$('.system-alerts').css( "width", "250px" );
			    	$('.search-row-results').css( "width", "245px" );
			    	$('.jScrollPaneContainer').css("width","245px");
				} else {
					$('.system-alerts').css( "width", "210px" );
					$('.search-row-results').css( "width", "210px" );
			    	$('.jScrollPaneContainer').css("width","210px");
				}
				setTimeout(function() {
					$('#customer-change-request-results-list').jScrollPane({
						showArrows : true
					});
				}, 0);
			});
		},	
};

var ProfileHandler = {
		load: function() {
		pagecontent({full: true, height: '468px'});
		$('.main-container').height('468px');
		$('#droppable1').height('468px');
		$('#droppable2').height('468px');
        } ,			
		
		initProfileMain: function() {
		$('.profile-theme-name').click(function(){
			var thisTheme = $(this).attr('name');
			var paramString = 'action=change_theme&theme='+thisTheme;
			$.post('profile.json', paramString,
			    function(obj){
				});
		});
	},
	initChangePassword : function (userName) {
			$('#action-save').click(function() {
			var p1=($('#newPassWord')).val();
			var p2=($('#confirmPassWord')).val();
			var p3=($('#oldPassWord')).val();
			    if(!p1.length>0 && !p2.length>0){
				     $('#change-password-form').clearForm();
				     $('#messageId').html('Passwords should Not be empty');
				}else if(p1===p3 ||p2==p3){
					$('#change-password-form').clearForm();
				    $('#messageId').html('Old password and new Password should Not be same');
				}else if(p1 != p2){
					$('#change-password-form').clearForm();
		          	$('#messageId').html('Passwords didnot match');
		       }else if(p1===userName){
		          $('#change-password-form').clearForm();
		          $('#messageId').html('Username and password should not be same');
		       }else if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test(p2)==false || /^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test(p1)==false){
		    	   $('#messageId').html('password should be 8, must contains atleast one alphanumeric, capital, small & special characters.');
		       }
		       else{
		          var thisButton = $(this)
					var paramString = $('#change-password-form').serialize();
					$.post('profile.json', paramString,
				        function(obj){
				            var result=obj.result.message;
				             if(result==='Failed'){
				               $('#messageId').html('Old password is incorrect');
				             }else{
							    $('.profile-div').html('<div class="success-msg">Saved Succesfully</div>')
							 }
				        }
			        );
		      }
			});
			$('#action-cancel').click(function() {
				
				 $('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
					$("#error-message").dialog({
						resizable: false,
						height:140,
						title: 'Confirm',
						modal: true,
						buttons: {
							'Ok' : function() {
								document.location.href = 'index.jsp';
				    			$(this).dialog('close');
							},
							Cancel: function() {
								$(this).dialog('close');
							}
						}
					});
				
				//$('#change-password-form').clearForm('');
			});
			
			$('#action-clear').click(function() {
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title:'Confirm',
					modal: true,
					buttons: {
						'Ok' : function() {
							$('#change-password-form').clearForm();
							$('#messageId').html('');
			    			$(this).dialog('close');
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			});
	}
	
};
var HelpHandler = {
		load: function() {
			pagecontent({full: true, height: '700px'});
			$('.ui-content').height($('.page-container').height());
			$('.ui-content').jScrollPane({showArrows:true});
			
			$('#ps-exp-col').click(function() {
				setTimeout(function() {
					$('.ui-content').jScrollPaneRemove()
					$('.ui-content').jScrollPane({showArrows:true});
	            }, 0);
			});

			
			
		}
};

var DragAndDropHandler = {
		
		initDragAndDrop: function() {
			
			$( ".draggable" ).draggable();
			$( "#droppable1" ).droppable();
			$( "#droppable2" ).droppable({
			});
		}
};