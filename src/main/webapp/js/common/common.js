var ResultHandler = {



	init: function(resp) {
		$("#error-dialog").dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				Cancel: function() {
					$(this).dialog('close');
				}				
			}
		});
		$('.anchor').live('mouseenter', function() {
			$(this).css('cursor', 'pointer');
		});
		$('.anchor').live('mouseleave', function() {
			$(this).css('cursor', 'auto');
		});
		$('.ui-btn').live('mouseenter', function() {
			$(this).css('cursor', 'pointer');
		});
		$('.ui-btn').live('mouseleave', function() {
			$(this).css('cursor', 'auto');
		});
		$('.exp-coll').live('mouseenter', function() {
			$(this).css('cursor', 'pointer');
		});
		$('.exp-coll').live('mouseleave', function() {
			$(this).css('cursor', 'auto');
		});
		$('.favorites-bg').live('mouseenter', function() {
			$(this).css('cursor', 'pointer');
		});
		$('.favorites-bg').live('mouseleave', function() {
			$(this).css('cursor', 'auto');
		});
		
		
		$('.exp-coll').live('click', function() {
			if($(this).hasClass('expand-icon')) {
				$(this).removeClass('expand-icon');
				$(this).addClass('collapse-icon');
				$(this).parents('.ui-container').find('.ui-content').animate({ height: 'hide', opacity: 'hide' }, 'medium');
				//$(this).parents('.ui-container').find('.green-results-list').width('830px;');
			} else if($(this).hasClass('collapse-icon')) {
				alert('else');
				$(this).removeClass('collapse-icon');
				$(this).addClass('expand-icon');
				$(this).parents('.ui-container').find('.ui-content').animate({ height: 'show', opacity: 'show' }, 'medium');
				//$(this).parents('.ui-container').find('.green-results-list').width('830px;');
			}
		});
		
		$('#favorite-icon').live('click',function() {
			
			var module = $('#page-container').attr('module');
			var page = $('#page-container').attr('page');
			var pageLink = $(this).attr('pageLink');
			var favTitle = $(this).attr('favTitle');
			
			if(!(pageLink==='' || pageLink==='-1')) {
			var paramString = 'action=save_favorite&module='+module+'&page='+page+'&pageLink='+pageLink+'&favTitle='+favTitle;
			$.post('profile.json', paramString,
			    function(obj){
				});
			} else {
				alert('SORRY-This Page Is Not Recommended As Favorite.');
			}
		});
		
		$('#help-icon').live('click',function() {
			var helpLink = $(this).attr('link');
			
			var width  = 535;
			 var height = 500;
			 var left   = (screen.width  - width)/2;
			 var top    = (screen.height - height)/2;
			 var params = 'width='+width+', height='+height;
			 params += ', top='+top+', left='+left;
			 params += ', directories=no';
			 params += ', location=no';
			 params += ', menubar=no';
			 params += ', resizable=no';
			 params += ', scrollbars=1';
			 params += ', status=no';
			 params += ', toolbar=no';

			 var newwin=window.open(helpLink,'Nirvahak Help', params);
		});
		
		
		DetailPopup.init('#student-preview-popup');
		$('.student-preview').live('mouseenter', function(e) {
			var id = $(this).attr('studentId');
			var name = $(this).html()
			$(this).css('cursor', 'pointer');
			//var x = $(this).position().left;
	        //var y = $(this).position().top;
	        var x = e.pageX - this.offsetLeft;
	    	var y = e.pageY - this.offsetTop;
			DetailPopup.gEnterInterval = setTimeout(function() {
				//$('#student-preview-popup').css({'top':y+20,'left':x+50}).show();
				$('#student-preview-popup').css({'top':y,'left':x}).show();
				$('#student-preview-popup').find('#student-preview-title').html(name+" "+id);
	        }, 500);
		});
		
		$('.student-preview').mousemove(function() {
	        clearTimeout(DetailPopup.gExitInterval);
	    });
		$('.student-preview').mouseleave(function(){
			$(this).css('background', 'none');
			$(this).css('cursor', 'auto');
			$('#student-preview-popup').css('display', 'none');
			DetailPopup.leave('#student-preview-popup');		
		});
	},
	
	process: function(resp) {
		//console.log(resp);
		if(resp.failure) {
			$('#error-dialog').html(resp.fielderrors);
			$('#error-dialog').dialog('open');
		}
	}
};
var currencyHandler = {
convertStringPatternToFloat: function(stringPattern) {
		var floatValue = Number(stringPattern.replace(/[^0-9-\.]+/g,""));
		return floatValue;
},
convertStringMillionPatternToFloat: function(stringPattern) {
	var floatValue = Number(stringPattern.replace(/[^a-zA-Z0-9-\.]+/g,""));
	return floatValue;
},
convertProductCostStrPatternToFloat: function(stringPattern) {
	var integerRegex = /^[\d.]+$/;
	if(integerRegex.test(stringPattern) === false){
		return false;
	}else{
		var floatValue = Number(stringPattern.replace(/[^0-9-\.]+/g,""));
		return floatValue;
	}
	
},
convertFloatToStringPattern: function(floatPattern) {
	var stringValue=floatPattern.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	 return stringValue;
},
convertStringPatternToNumber: function(stringPattern) {
	var number = Number(stringPattern.replace(/[^0-9-]+/g,""));
	return number;
},
convertNumberToStringPattern: function(numberPattern) {
	var stringValue=numberPattern.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	return stringValue;
},
formatStringValue:function(number){
	if(number.length>0){
		return number;
	}else{
		return null;
	}
},
};

var RoleHandler = {
	roles: [],
	modules: [],
	canEditTimetable: false,
	canEditTask: false,
	init: function() {
		for(var loop=0;loop<RoleHandler.roles.length; loop=loop+1) {
			if(RoleHandler.roles[loop]==='ROLE_MANAGEMENT') {
				RoleHandler.canEditTimetable = true;
				RoleHandler.canEditTask = true;
			}
		}
	},
	
	hasRole: function(role) {
		for(var loop=0;loop<RoleHandler.roles.length; loop=loop+1) {
			if(RoleHandler.roles[loop]===role) {
				return true;
			}
		}
		return false;
	}
};

var UserHandler = {
		flag:true,
		initValidateUserName:function(){
			var paramString="action=get-all-employees";
			$.post('organization.json',paramString,
			        function(obj){
				var data=obj.result.data;
			});
		},
		initCheckUsername: function() {
			var resFailure=false;
			var resSuccess=true;
			$("#username").blur(function(event) {
				$('#user_pop').hide();
				var uname=$('#username').val();
				var pwd=$('#password').val();
					var uname=$('#username').val();
					if(uname=="")
						{
						 $('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
						}
						else
							{
							var paramString='username='+uname+'&action=validate-username';
							$.post('organization.json',paramString,
							        function(data){
					            var delay = function() {
					            	UserHandler.AjaxSucceeded(data);
					            	};
					            	setTimeout(delay, 400);
							});
							
							}
				
			});
		},
		
		initCheckUsernameForSchool: function() {
			$("#username").blur(function(event) {
				var uname=$('#username').val();
				 if(/^[a-zA-Z0-9]/.test($('#username').val())==false){
					 return false;
				 }
				//if($('#uName').validate()==false) return;
				$.post('school.json', 'action=validate-username&username=' + uname,
				        function(data){
		        	$('#uValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking username...'>");
		            var delay = function() {
		            	UserHandler.AjaxSucceeded(data);
		            	};
		            	setTimeout(delay, 200);
				});
			});
		},
		
		AjaxSucceeded: function(data1) {
			if (data1.result.data == "y") {
				$('#uValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='Username available!'>");
				$('#unValid').empty();
				$("#username").focus(function(event){
					$('#unValid').empty();
					$('#username_pop').show();
					$('#username_popDuplicate').hide();
				});
				UserHandler.flag=true;
			}
			else if (data1.result.data == "n") {
				$('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Username Unavailable!'>");
				$('#username_popDuplicate').hide();
				$("#username").focus(function(event){
					$('#unValid').empty();
					$('#username_pop').hide();
					 $('#username_popDuplicate').show();
				});
				UserHandler.flag=false;
			}else if (data1.result.data == "v") {
	            $('#uValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='FeeTypeCode available!'>");
	        	$('#description').focus();
	        }  else{
	        	UserHandler.flag=false;
	        	$('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>");
	        	$("#username").focus(function(){
	        		$('#uValid').empty();
	        		$('user_pop').show();
	        	});
	        }
		},
		AjaxFailed: function(result) {
			//alert('select another...');
		},
		
		initCheckPassword: function() {
			$("#password").blur(function(event) {
				var uname=$('#username').val();
				var pwd=$('#password').val();
				 if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val())==false||pwd == uname||pwd.length<3){
					  UserHandler.flag=false;
					 $('#pValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $('#pValid_pop').show();
						var uname=$('#username').val();
				 }else{
					 UserHandler.flag=true;
					 $('#pValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='Password Valid!'>");
				 }
				
			});
		}
};

var PageHandler = {
	theme: "",	
	expanded: true,
	pageSelctionButton: undefined,
	initPageSelection: function() { 
		var thisTheme = PageHandler.theme;
		pageSelctionButton = $('#ps-exp-col');
		pageSelctionButton.mouseenter(function() {
			$(this).css('cursor', 'pointer');
		});
		pageSelctionButton.mouseleave(function() {
			$(this).css('cursor', 'auto');
		});
		pageSelctionButton.click(function() {
			PageHandler.expanded = !PageHandler.expanded;
			if(PageHandler.expanded) {
				//pageSelctionButton.removeClass('ps-exp-col');
				//pageSelctionButton.addClass('ps-exp-exp');
				pageSelctionButton.attr('src', thisTheme+'/button-left.jpg');
				$('.page-selection').animate( { width:"183px"},0,function(){
					$('.page-link-strip').show();
					$('.module-title').show();
				});
				$('.page-container').animate( { width:"702px"},0);
			} else {
				//pageSelctionButton.removeClass('ps-exp-exp');
				//pageSelctionButton.addClass('ps-exp-col');
				pageSelctionButton.attr('src', thisTheme+'/button-right.jpg');
				$('.page-link-strip').hide();
				$('.module-title').hide();
				$('.page-selection').animate( { width:"55px"},0,function(){});
				$('.page-container').animate( { width:"835px"},0);				
			}
		});
		if(PageHandler.expanded) {
			$('.page-link-strip').show();
			$('.module-title').show();
			$('.page-selection').css('width', '183px;');
			//pageSelctionButton.removeClass('ps-exp-col');
			//pageSelctionButton.addClass('ps-exp-exp');
			pageSelctionButton.attr('src', thisTheme+'/button-left.jpg');
		} else {
			$('.page-link-strip').hide();
			$('.module-title').hide();
			$('.page-selection').css('width', '50px;');
			//pageSelctionButton.removeClass('ps-exp-exp');
			//pageSelctionButton.addClass('ps-exp-col');
			pageSelctionButton.attr('src', thisTheme+'/button-right.jpg');
		}
	},
	pageSelectionHidden: false,
	hidePageSelection: function() {
		if(!PageHandler.pageSelectionHidden) {
			$('#ps-exp-col').click();
			//$('#ps-exp-col').hide();
			PageHandler.pageSelectionHidden = true;
		}
	}
	
};

var ModuleHandler = {
	moduleImage2: "",
	init: function() {
		
		var thisImage = ModuleHandler.moduleImage2;
		
		$('.module-link').mouseenter(function() {
			var module = $(this).attr('module');
			
			if(RoleHandler.modules[module]===true) { 
				$("img:first",this).attr('src', thisImage+'images/module/'+$(this).attr('module')+'_2.png');
			}else {
				
				var thisAnchor =$("a:first",this);
				
				if(thisAnchor.hasClass('module-link-na')==false){
					thisAnchor.addClass('module-link-na');
					thisAnchor.click(function(){
						showMessage({title:'Warning', msg:'This module is not accessible by you.'});
						return false;
					});					
				};
			}
		});
		$('.module-link').mouseleave(function() {
			$("img:first",this).attr('src', thisImage+'images/module/'+$(this).attr('module')+'_1.png');
		});
	}
};

/*height adjustment for all search page results...*/
var SearchResultsHandler = {
		init: function(greenResultId) {
			var greenResultCol1 = $('#green-result-row-'+greenResultId).find('.green-result-col-1').outerHeight();
			var greenResultCol2 = $('#green-result-row-'+greenResultId).find('.green-result-col-2').outerHeight();
			var greenResultCol3 = $('#green-result-row-'+greenResultId).find('.green-result-col-3').outerHeight();
			var greenResultCol4 = $('#green-result-row-'+greenResultId).find('.green-result-col-4').outerHeight();
			var greenResultCol5 = $('#green-result-row-'+greenResultId).find('.green-result-col-5').outerHeight();
			if(greenResultCol1 < greenResultCol2) {
				$('#green-result-row-'+greenResultId).find('.green-result-col-1').outerHeight(greenResultCol2);
				$('#green-result-row-'+greenResultId).find('.green-result-col-action').outerHeight(greenResultCol2);
				$('#green-result-row-'+greenResultId).height(greenResultCol2+greenResultCol3+greenResultCol4+greenResultCol5);
			} else {
				$('#green-result-row-'+greenResultId).find('.green-result-col-2').outerHeight(greenResultCol1);
				$('#green-result-row-'+greenResultId).find('.green-result-col-action').outerHeight(greenResultCol1);
				$('#green-result-row-'+greenResultId).height(greenResultCol1+greenResultCol3+greenResultCol4+greenResultCol5);
			}
		}
};

var DetailPopup = {
	gEnterInterval: null,
	gExitInterval: null,
	init: function(popupId) {
		$(popupId).bind('mouseenter', function() {
	        if (DetailPopup.gExitInterval != null) {
	            clearTimeout(DetailPopup.gExitInterval);
	            DetailPopup.gExitInterval = null;
	        }
	    });
	    $(popupId).bind('mouseleave', function() {
	    	DetailPopup.gExitInterval = setTimeout(function() {
	            $(popupId).hide();
	            DetailPopup.gExitInterval = null;
	        }, 400);
	    });
	},
	
	leave: function(popupId) {
		DetailPopup.gExitInterval = setTimeout(function() {
			$(popupId).hide();
			DetailPopup.gExitInterval = null;
        }, 0);
        clearTimeout(DetailPopup.gEnterInterval);
	},
	close: function() {
	    $('.detail-popup').hide();
	}

};

var SystemDefaultsHandler = {
		getAddressTypes : function(){
			var type=$('#addressTypeEdit').val();
			$.post('default.json','action=get-address-types',function(obj){
				var result = obj.result.data;
				var res =new String(result);
				if(result.length>0){
					$('#addressType').empty();
					var select= 'select'
					$('#addressType').append( $('<option></option').text('select').val('-1'));
					for(var loop=0;loop<result.length-1;loop = loop+1){
						$('#addressType').append( $('<option></option').text(result[loop].addressType));
						if(result[loop]==type){
							loop=loop+1;
						}
						$('#addressTypeEdit').append( $('<option></option').text(result[loop].addressType));
						
				};
				var prefix =result[result.length-1].userName;
				if(prefix != undefined){
					$('#orgPrefix').val(prefix+'.');
				}
				
				}
			});
		},
		getPaymentTypes : function(){
			$.post('default.json','action=get-all-payment-types',function(obj){
				var result =obj.result.data;
				if(result.length>0){
					$('#paymentType').empty();
					var select= 'select'
					$('#paymentType').append( $('<option></option').text('select').val('-1'));
					for(var loop=0;loop<result.length;loop = loop+1){
						$('#paymentType').append( $('<option value='+ result[loop] +'></option').text(result[loop]));			
				};
				}
			});
		},
    	getJournalTypes : function(){
    		$.post('default.json','action=get-all-journal-types',function(obj){
				var result =obj.result.data;
				if(result.length>0){
					$('#journalType').empty();
					var select= 'select'
					$('#journalType').append( $('<option></option').text('select').val('-1'));
					for(var loop=0;loop<result.length;loop = loop+1){
						$('#journalType').append( $('<option></option').text(result[loop]));			
				};
				}
			});
    	}
};


var FooterHandler = {
	     init : function() {
	
			$('.build-count').mousemove(function() {
		        clearTimeout(DetailPopup.gExitInterval);
		    });
			$('.build-count').mouseleave(function(){
				DetailPopup.leave('#build-detail');			
			});
	
			DetailPopup.init('#build-detail');
			$('.build-count').mouseenter(function(){
				var x = $(this).position().left;
		        var y = $(this).position().top;
		      //  var data = $(this).data('task');
				DetailPopup.gEnterInterval = setTimeout(function() {
					$('#build-detail').css({'top':y-245,'left':x}).show();
					//$('#build-detail-content').find('#build-info').html($('.buildInfo').show());
		        }, 500);
				
			});
			
	     }	
}

var FeedManager = {
		notificationBubbles : {},
		notificationBubblesCount : 0,
		feeds : new Array(),

		fireNotificationBubble : function() {
			var me = this;
			$.post('employee.json', 'action=get-loggedin-user', function(obj) {
				if(undefined != obj.result.data) {
					var queueName = obj.result.data.userName;
					me.updateNotification(queueName);
					FeedManager.deleteQueueMessages(queueName);
					FeedManager.setNotificationCount(obj);
				}
			});
		},

		readRssFeeds : function() {
			var me = this;
			$.post('employee.json', 'action=rss-feed-lookup', function(obj) {
				var data = obj.result.data;
				if(undefined != data) {
					$('.notificationCount').val(data.length);
					if (undefined == me.notificationBubbles['Notification']) {
						var bubblePos = (me.notificationBubblesCount + 1) * 240 + 'px';
						me.notificationBubbles['Notification'] = new VbooksFeedReader({
							name : 'Notification',
							position : bubblePos
						});
						me.notificationBubblesCount++;
					}
					
					// For bubble alert.
					var containerObj = $('.notification-header').parent();
					me.notificationBubbles['Notification'].showNotification({container: containerObj});
					containerObj.children().addClass('open');
					
					$.each(data, function(i, feedMsg) {
						var content = "";
						content += '<b>Message:</b>' + feedMsg.message;
						content += '<br/><b>Date:</b>' + feedMsg.date;
						var content = content;
						me.notificationBubbles['Notification'].writeContent('Notification', content);
					});	
				}
			});
		},
		
		updateNotification : function(data) {
			var me = this;
			var content = '';
			var rssFeedsForSource = {};
			if (undefined == data || data.length == 0) {
				content += cfg.message;
				return;
			} else {
				if (undefined == rssFeedsForSource[data]) {
					me.readRssFeeds();
				}						
			}
			// default polling to 6 sec.
			var interval = 6000; 
			setInterval(function() {
				$('.notifcation-content').html('');
				me.readRssFeeds();
			}, interval);
		},
		
		// Deleting messages from Message Server for every 5 mins.
		deleteQueueMessages: function(queueName) {
			setInterval(function() {
				$.ajax({
					url : 'queueBrowse',
					data : 'queueName='+queueName,
				});
			}, 300000);
		},
		
		setNotificationCount: function(obj) {
			$('#notifications').click(function() {
				$('.notificationCount').val('');
				var userName = obj.result.data.userName;
				var employeeType = obj.result.data.employeeType;
				var redirectUrl;
				if('SLE' == employeeType) {
					redirectUrl = window.location.pathname+'?module=mysales&page=myAlerts';
				} else {
					redirectUrl = window.location.pathname+'?module=dashboard&page=alerts';
				}
				window.location = redirectUrl;
				
				// Deleting messages from Message Server.
				$.ajax({
					url : 'queueBrowse',
					data : 'queueName='+userName,
				});
				
				$('.notifcation-content').html('');
				FeedManager.readRssFeeds();
			});
		}
	};




$.fn.validate = function() {
	var thisForm = this;
	var success = true;

	/*if(thisForm.hasClass('.notForm')){
		console.log($(this))
	}*/
	
	thisForm.find('.mandatory').each(function() {
		if($(this).val()=='' || ($(this).val()=='-1') &&
				($(this).get(0).tagName=='select'||$(this).get(0).tagName=='SELECT')){
			showMessage({title:'Error', msg:'Red marked fields are mandatory'});
			success = false;
			return;
		}
	});
	if(success==false) return false;
	
	//if(thisForm.find('.constrained').constrained()==false) return false;
	thisForm.find('.constrained').each(function() {
		if($(this).constrained() == false) {
			success = false;
			return;
		}
	});
	//});
	if(success==false) return false;

	/*$('.constrained').each(function() {
		if(thisForm.find('.constrained').constrained()==false) return false;
	});*/
	
	
	return true;
}

/*
 * constraints.fieldLabel: label for constraint
 * constraints.maxSize: maximum size constraint
 */
$.fn.constrained = function() {
	if($(this).attr('constraints')=='') return true;
	try {
	var constraints = jQuery.parseJSON($(this).attr('constraints'));
	}catch(e){
		showMessage({title:'Error', msg:'Invalid constraints for field '+constraints.fieldLabel});
		return false;
	}
	 if(undefined!==constraints.charsNumsNoSpace && /^[a-zA-Z0-9\S]+$/.test($(this).val()) !== true) {
			var msg = $(this).parent().find('.label').html();
			showMessage({title:'Error', msg:'The input field '+constraints.fieldLabel+'Will not Accept Spaces. '});
			return false;
	};
	 if(undefined!==constraints.charsNumsOnly && /^[a-zA-Z0-9]/.test($(this).val()) !== true) {
			var msg = $(this).parent().find('.label').html();
			showMessage({title:'Error', msg:'The input field '+constraints.fieldLabel+' accepts no special characters. '});
			return false;
		};
	if(undefined!==constraints.charsOnly && /^[a-zA-Z\.\s]+$/.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The input field '+constraints.fieldLabel+' accepts Only characters. '});
		return false;
	};
	if(undefined!==constraints.maxSize && constraints.maxSize<$(this).val().length) {
		var msg = $(this).parent().find('.label').html();
		if(undefined!==constraints.fieldMessage){
			showMessage({title:'Error', msg:constraints.fieldMessage});	
		
		} else {
			showMessage({title:'Error', msg:'The size of field '+constraints.fieldLabel+' is more than '+constraints.maxSize});
		};
		return false;
	};
	if(undefined!==constraints.numbersOnly && /^[0-9]+$/.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The input field '+constraints.fieldLabel+' accepts only numbers. '});
		return false;
	};
	if(undefined!==constraints.minSize && constraints.minSize>$(this).val().length) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The size of field '+constraints.fieldLabel+' must be atleast '+constraints.minSize});
		return false;
	};
	if(undefined!==constraints.mustSelect && ($(this).val()<0 || $(this).val()== null)) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The field '+constraints.fieldLabel+' must be selected'});
		return false;
	};
	if(undefined!==constraints.choose && /^[a-zA-Z0-9._-]{3,16}$/.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:constraints.fieldLabel+' must have no special (OR) less than 3 characters.'});
		return false;
	};
	if(undefined!==constraints.floatOnly && /^(?!0\d)\d*(\.\d+)?$/mg.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:constraints.fieldLabel+' must be positive integer.'});
		return false;
	};

/*	if(undefined!==constraints.choose && /^[a-zA-Z0-9._-]{3,16}$/.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		
		if(/^{3,16}$/.test($(this.val())) ! == true) {
			showMessage({title:'Error', msg:constraints.fieldLabel+' must have atleast 3 characters.'});
		}else {
			showMessage({title:'Error', msg:constraints.fieldLabel+' must have no special characters.'});
		}
		return false;
	};
*/

	
	//var objEmail = document.getElementById("emailId");
	//var consEmail = objEmail.value;
     
	if(constraints.residence === 'true' &&  $(this).val() === ''){
		
	}else if(undefined!==constraints.residence && /^[0-9]+$/.test($(this).val()) !== true) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The input field '+constraints.fieldLabel+' accepts only numbers'});
		return false;
	}else if(undefined!==constraints.rminSize && constraints.rminSize>$(this).val().length) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'The size of field '+constraints.fieldLabel+' must be atleast '+constraints.rminSize});
		return false;
	};


	
	
	if(constraints.email === 'true' &&  $(this).val() === ''){
	
	}else if(/*objEmail == null || consEmail == "" ||*/ undefined!==constraints.email && !$(this).ValidateEmailAddr(/*consEmail*/$('#'+$(this).attr('id')).val())) {
		var msg = $(this).parent().find('.label').html();
		showMessage({title:'Error', msg:'Please enter a valid email address for '+constraints.fieldLabel+'<html><br></html> Eg: john@xyz.com , harsh@gmail.co.in'});
		return false;
	};
	
	return true;	
}
$.fn.sumValues = function() {
	var sum = 0; 
	this.each(function() {
		if ( $(this).is(':input') ) {
			var val = $(this).val();
		} else {
			var val = $(this).text();
		}
		sum += parseFloat( ('0' + val).replace(/[^0-9-\.]/g, ''), 10 );
	});
	return sum;
}

$.fn.ValidateEmailAddr = function(str) {
	var at = "@";
	var dot = ".";
	var lat = str.indexOf(at);
	var lstr = str.length;
	var ldot = str.indexOf(dot);
	
	if (str.indexOf(at) == -1) {
		return false;
	} else if (str.indexOf(at) == -1 || str.indexOf(at) == 0 || str.indexOf(at) == lstr) {
		return false;
	} else if (str.indexOf(dot) == -1 || str.indexOf(dot) == 0 || str.indexOf(dot) == lstr) {
		return false;
	} else if (str.indexOf(at, (lat + 1)) != -1) {
		return false;
	} else if (str.substring(lat - 1, lat) == dot || str.substring(lat + 1, lat + 2) == dot) {
		return false;
	} else if (str.indexOf(dot, (lat + 2)) == -1) {
		return false;
	} else if (str.indexOf(" ") != -1) {
		return false;
	} else return true;
}

/*
 * config.container: selector string for page container
 * config.url: page URL
 * config.params: page parameters
 */
$.fn.pageLink = function(config) {
	var thisLink = this;
	thisLink.click(function() {
		thisLink.activePagelink();
			if(config.params===undefined) {
				config.params = '';
			}			
			$.post(config.url, config.params,
		        function(data){
					$(config.container).html(data);     
		        }
	        );
	
	});
}

$.fn.successMessage = function(config) {
	var thisMessage = this;
	$(config.container).html('<div class="success-msg">'+config.data+'</div>');
}
/*yet to be developed...*/
$.fn.ajaxLoader = function() {
	this.loadAnimation({image: {src: THEMES_URL+'images/common/ajax-save-loader.gif'}});	
}
$.fn.ajaxSavingLoader = function() {
	this.loadAnimation({image: {src: THEMES_URL+'images/common/ajax-save-loader.gif'}});	
}

$.fn.activePagelink = function() {
	//if(this.hasClass('page-links-active')) return true;
	$('.page-links-active').removeClass('page-links-active');
	this.addClass('page-links-active');
	if($.loadAnimation.isStarted){
		$.loadAnimation.end();
	}
	//return false;
}

/*
 * config.msg: Message to display
 * config.container: selector string for page container
 * config.url: page URL
 * config.params: page parameters
 */
$.fn.actionCancel = function(config) {
	$(this).click(function() {
		if(undefined===config.msg) {
			config.msg = 'You will loose unsaved data.. Clear form?';
		};
		$('#error-message').html(config.msg);   
		$("#error-message").dialog({
			resizable: false,
			height:140,
			title: "Confirm",
			modal: true,
			buttons: {
				'Ok': function() {
	    			$(this).dialog('close');
					$.post(config.url,config.params, function(data){
						$(config.container).html(data); 
					} );
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
	    return false;
		
	});
}

$.fn.clearForm = function() {
  return this.each(function() {
    var type = this.type, tag = this.tagName.toLowerCase();
    if (tag == 'form')
      return $(':input',this).clearForm();
    if (type == 'text' || type == 'password' || tag == 'textarea')
      this.value = '';
    else if (type == 'checkbox' || type == 'radio')
      this.checked = false;
    else if (tag == 'select')
      this.selectedIndex = -1;
  });
};

function pagecontent(config) {
	var currHeight = $('.page-selection').height();
	if(config.height!==undefined) {
		var diffHeight = config.height - currHeight;
		$('.main-container').height($('.main-container').height()+diffHeight);
		$('.page-selection').height($('.page-selection').height()+diffHeight);
		$('.page-container').height($('.page-container').height()+diffHeight);
		$('.page-content').height($('.page-container').height()); 
	}
}

function isFail(obj) {
	if(obj.result.type=="fail") {
		showMessage({title:'Error', msg:obj.result.message});
		return true;
	}
	return false;
}

/*
 * config.msg: Message to display
 * config.title: Message dialog title
 */
function showMessage(config) {
	if(undefined===config.title) {config.title="Message"}
	$('#error-message').html(config.msg);   
	$("#error-message").dialog({
		resizable: false,
		height:160,
		width: 350,
		title: config.title,
		modal: true,
		buttons: {
			'Ok': function() {
    			$(this).dialog('close');
			}
		}
	});	
}


(function($) { 	

	function callFunction(options, name, self)
	{
		var fn = options[name];
		if ($.isFunction(fn))
		{
			try
			{
				return fn.call(self);
			}
			catch (error)
			{
				if (options.eAlert)
					alert("Error calling ." + name + ": " + error);
				else
					throw error;		
				
				return false;
			} 			
		}
		return true;			
	}
	
	// mask instance (singleton)
	var mask = null;	

	// animated elements
	var animated, conf = null;
	var origIndex = 0;
	
	// global methods
	$.loadAnimation = {		
		
		getVersion: function()
		{
			return [0, 2, 0];	
		},
		
		getMask: function()
		{
			return mask;	
		},
		
		getAnimated: function()
		{
			return animated;	
		},
		
		getConf: function()
		{
			return conf;	
		},		
		
		isStarted: function()
		{
			return mask && mask.is(":visible");	
		},
		
		start: function(target, options)
		{ 
			
			// already started ?
			if (this.isStarted()) return this;

			if (target) {
				animated = target;
				origIndex = animated.eq(0).css("zIndex");
				conf = options;					
			} else {
				target = animated;
				options = conf;
			} 

			if (!target || !target.length) return this;
			
			// setup mask if not already done
			if (!mask)
			{
				mask = $('<div id="'+options.maskId+'"></div>');
				if (options.image.src != '')
					img = $('<img id="'+options.maskId+'_loading_img" />')
							.attr('src',options.image.src)
							.attr('alt',options.image.alt);
			}
			
			// set mask css properties
			mask.css({				
				position: 'absolute', 
				top: target.offset().top, 
				left: target.offset().left,
				width: target.width(),
				height: target.height(),
				display: 'none',
				opacity: 0,					 		
				zIndex: options.zIndex,
				backgroundColor: options.color
			});
			
			// set image css properities			
			img.css({
				zIndex: options.zIndex+1,
				position: 'absolute',
				top: (target.height()/2-options.image.size.height/2),
				left: (target.width()/2-options.image.size.width/2)
			});
			
			// insert image into mask and append mask to body
			mask.html(img);
			$("body").append(mask);

			
			
			// onBeforeLoad
			if (callFunction(options, "onBeforeLoad", this) === false) return this;	
			
			// reveal mask
			if (!this.isStarted())
			{					
				mask.css({opacity: 0, display: 'block'}).fadeTo(options.loadSpeed, options.opacity, function()
				{
					callFunction(options, "onLoad", $.loadAnimation);  						
				});					
			}

			return this;
		}, 
		
		
		end: function()
		{
			
			var self = this;
			
			if (!this.isStarted()) { return self; }   
			
			if (callFunction(conf, "onBeforeClose", self) === false) return self;   
			
			
			mask.fadeOut(conf.closeSpeed, function()
			{          
				animated.css({zIndex: $.browser.msie ? origIndex : null});
				callFunction(conf, "onClose", self);               
			});        
		}
		
	};
	
	// jQuery plugin initialization
	$.prototype.loadAnimation = function(conf)
	{

		// no elements to expose
		if (!this.length) return this;
		
		var options = {
			eAlert: true,

			// mask settings
			maskId: 'exposeMask',
			loadSpeed: 'slow',
			closeSpeed: 'fast',
			
			// css settings
			zIndex: 998,
			opacity: 0.9,
			color: '#f6f6f6',
			
			// image settings
			image: {
				src: '',
				alt: 'Loading...',
				size: {
					width: 100,
					height: 10
				}
			}
		};
		// save critical options
		if (typeof conf == 'string') conf = {color: conf};
		if (typeof conf.image == 'string') conf.image = { src: conf.image, alt: options.image.alt, size: options.image.size };
		if (conf.image.alt == undefined) conf.image.alt = options.image.alt;
		if (conf.image.size == undefined) conf.image.size = options.image.size;
		if (conf.image.size.width == undefined) conf.image.size.width = options.image.size.width;
		if (conf.image.size.height == undefined) conf.image.size.height = options.image.size.height;
		
		
		// extend and overwrite standard options with user config
		$.extend(options, conf);
		
		// start animation	
		$.loadAnimation.start(this, options);
		
		// return jQuery object
		return this;
		
	}; 


})(jQuery);

function fmt(str) {
	if(str===undefined) return '';
	if(str===null) return '';
	if(str==='null') return '';
	
	return str;
}


/*
 * config.pUrl: Url path to send to.
 * config.pTitle: Message title
 * config.pWidth: print Screen width
 * config.pHeight: print Screen height
 */
function getPrint(config) {
	 var width  = '';
	 var height = '';
	 var left   = '';
	 var top    = '';
	 
	 if((config.pWidth !== undefined && config.pWidth !== '') 
			 && (config.pHeight !== undefined && config.pHeight !== '')) {
		 width  = config.pWidth;
		 height = config.pHeight;
		 left   = (screen.width  - width)/2;
		 top    = (screen.height - height)/2;
	 } else {
		 /*default width and height*/
		 width  = 635;
		 height = 600;
		 left   = (screen.width  - width)/2;
		 top    = (screen.height - height)/2;
	 } 
	 
	 
	 var url = config.pUrl;
	 var title = config.pTitle;
	 var params = 'width='+width+', height='+height;
	 params += ', top='+top+', left='+left;
	 params += ', directories=no';
	 params += ', location=no';
	 params += ', menubar=no';
	 params += ', resizable=no';
	 params += ', scrollbars=1';
	 params += ', status=no';
	 params += ', toolbar=no';

	 var newwin=window.open(url,title, params);
	
}