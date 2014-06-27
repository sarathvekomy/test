var OrganizationHandler = {
		flag:true,
		prevUserName : 'null',
		prevPrefix : 'null',
	pageLinks : function () {							
		$('#organization-list').pageLink({container:'.organization-page-container', url:'siteadmin/organization_list.jsp'});
		$('#department-create').pageLink({container:'.organization-page-container', url:'siteadmin/department_create.jsp'});		
	},
	organizationSteps:['#organization-form','#organization-superuser-form'],
	organizationUrl:['organization.json','organization.json','organization.json'],
	organizationStepCount: 0,
	initOrganizationListButtons: function() {
		$('#action-add-school').click(function(){
			$('.page-selection').css("width","55px");
			$('.page-container').css("width","830px");
			$('.page-link-strip').hide();
			$('.module-title').hide();
			
			$.post('siteadmin/organization_create.jsp', '',
		        function(data){
					$('.organization-page-container').html(data);     
		        }
	        );
		});
		$('#organization-list').click(function(){
			$('.task-page-container').html('');
			location.href = "index.jsp?module=siteadmin"
			$('.page-selection').css("width","183px");
			$('.page-container').css("width","702px");
			$('.page-link-strip').show();
			$('.module-title').show();
			$('.jScrollPaneContainer').css("width","697px");
			$('div#school-results-list').css("width","697px");
			setTimeout(function(){
				$('#school-results-list').jScrollPane({showArrows:true});
			},0);
		});
		$('.edit-icon').click(function(){
			$('.page-selection').css("width","55px");
			$('.page-container').css("width","830px");
			$('.page-link-strip').hide();
			$('.module-title').hide();
			var organizationId = $(this).attr('id'); 
			$.post('organization.json','action=get-organization-data&id='+organizationId,function(obj){
				var result = obj.result.data;
			$.post('siteadmin/organization_edit.jsp', 'mode=edit&id='+organizationId,
		        function(data){
					$('.organization-page-container').html(data);    
					$("#organizationId").val(organizationId);
					$('#mainBranch').val(result.mainBranch).attr('selected','selected');
					if(result.mainBranch == "Y"){
						$('#mainBranchName').attr('disabled','disabled');
					}else{
						$('#mainBranchName').val(result.main_branch_name);
					}
					$("#organizationCode").val(result.organizationCode);
					$('#name').val(result.name);
					$('#currencyFormat').val(result.currencyFormat);
					$('#branchName').val(result.branchName);
					$("#usernamePrefix").val(result.usernamePrefix);
					$("#addressLine1").val(result.addressLine1);
					$("#addressLine2").val(result.addressLine2);
					$('#locality').val(result.locality);
					$('#landmark').val(result.landmark);
					$("#city").val(result.city);
					$('#state').val(result.state);
					$("#country").val(result.country);
					$("#zipcode").val(result.zipcode);
					$("#phone1").val(result.phone1);
					$("#phone2").val(result.phone2);
					$('#description').val(result.description);
					$('#fullName').val(result.fullName);
					$('#username').val(result.superUserName);
					$("#mobile").val(result.mobile);
					$("#alternateMobile").val(result.alternateMobile);
					$("#email").val(result.email);
					 prevUserName = $("#username").val();
					 prevPrefix = $('#usernamePrefix').val();
		        });
		});
		});
		$('#ps-exp-col').click(function() {
		    if(PageHandler.expanded) {
				$('.jScrollPaneContainer').css("width","695px");
				$('div#school-results-list').css("width","695px");
			} else {
				$('.jScrollPaneContainer').css("width","830px");
				$('div#school-results-list').css("width","820px");
			}
		});
		$('.enable-icon').click(function(){
			var organizationId = $(this).attr('id'); 
			var checkMainBranch = $(this).attr('align'); 
			/*if($("#organization-row-"+ organizationId).hasClass('green-result-col-disabled-1')){*/
				 var OrganizationStatusParam='Enabled';
			     $('#organization-list-container').html('Are You Sure Want To Enable This Organization ?');
				  $("#organization-list-dialog").dialog({
						autoOpen: true,
						height: 200,
						width: 500,
						modal: true,
						buttons: {
							Yes: function() {
								 $.post('organization.json', 'id='+organizationId+'&action=modify-organization-status&organizationStatus='+OrganizationStatusParam+'&mainBranch='+checkMainBranch, function(obj) {
								    var data = obj.result.message;
								    if(data =='Sub Branch cant be Enabled as Main Branch is Disabled.'){
								    	 showMessage({title:'Warning', msg:data});
								    }
									 OrganizationHandler.initsearchOrganizationOnload();
									 $("#organization-list-dialog").dialog('close');
								 });
							},
				            No: function() {
				            	$('#organization-list-dialog').dialog('close');
					      }
						},
						close: function() {
							$('#organization-list-dialog').dialog('close');
						}
		        });
		});
			 $('.disable-icon').click(function(){
			  var organizationId = $(this).attr('id');
			  var checkMainBranch = $(this).attr('align'); 
			  var OrganizationStatusParam='Disabled';
			  $('#organization-list-container').html('Are You Sure Want To Disable This Organization ?');
			  $("#organization-list-dialog").dialog({
					autoOpen: true,
					height: 200,
					width: 500,
					modal: true,
					buttons: {
						Yes: function() {
							 $.post('organization.json', 'id='+organizationId+'&action=modify-organization-status&organizationStatus='+OrganizationStatusParam+'&mainBranch='+checkMainBranch, function(obj) {
								 OrganizationHandler.initsearchOrganizationOnload();
								 $("#organization-list-dialog").dialog('close');
							 });
						},
			            No: function() {
			            	$('#organization-list-dialog').dialog('close');
				      }
					},
					close: function() {
						$('#organization-list-dialog').dialog('close');
					}
	        });
		});
		
		$('.btn-view').click(function() {
			var organizationId = $(this).attr('id'); 
			$.post('organization.json','action=get-organization-data&id='+organizationId,function(obj){
				var result = obj.result.data;
			$.post('siteadmin/organization_view.jsp', 'id='+organizationId, function(data){
				$('#organization-view-container').html(data);
				$("#organizationId").val(organizationId);
				if(result.mainBranch == "N"){
					$('#mainBranchName').text(result.main_branch_name);
				}
				$("#organizationCode").text(result.organizationCode);
				$('#mainBranchVal').html(result.mainBranch);
				$('#mainBranchName').html(result.mainBranchName);
				$('#name').text(result.name);
				$('#currencyFormat').text(result.currencyFormat);
				$('#branchName').text(result.branchName);
				$("#usernamePrefix").text(result.usernamePrefix);
				$("#addressLine1").text(result.addressLine1);
				$("#addressLine2").text(result.addressLine2);
				$('#locality').text(result.locality);
				$('#landMark').text(result.landmark);
				$("#city").text(result.city);
				$('#state').text(result.state);
				$("#countryVal").text(result.country);
				$("#zipcode").text(result.zipcode);
				$("#phone1").text(result.phone1);
				$("#phone2").text(result.phone2);
				$('#description').text(result.description);
				$('#fullName').text(result.fullName);
				$('#username').text(result.superUserName);
				$("#mobile").text(result.mobile);
				$("#alternateMobile").text(result.alternateMobile);
				$("#email").text(result.email);
				$("#organization-view-dialog").dialog({
					autoOpen: true,
					height: 400,
					width: 800,
					modal: true,
					buttons: {
						Close: function() {
							$(this).dialog('close');
						}
					},
					close: function() {
						$('#organization-view-container').html('');
					}
				});
		    });
		});
		});
	},
	initsearchOrganizationOnload :function(){
		var paramString="action=get-disabled-organization-list";
		$.post('organization.json', paramString, function(obj){
			//list getting problem
		var organizationName = obj.result.data;
		var paramString = $('#organization-search-form').serialize();  
		$.post('organization.json', paramString,
	        function(obj){
		    	var data = obj.result.data;
				$('#school-results-list').html('');
				if(data != undefined) {
					var alternate = false;
					for(var loop=0;loop<data.length;loop=loop+1) {
						if( data[loop].email.length > 30){
							if(alternate) {
								var rowstr = '<div class="green-result-row alternate" id="green-result-row-'+data[loop].id+'" style="height:110px;">';
						} else {
							rowstr = '<div class="green-result-row" id="green-result-row-'+data[loop].id+'" style="height:110px;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1" id="organization-row-'+data[loop].id +'" style="width: 575px;">'+
						'<div class="result-title" style="width: 575px;">'+
						'<div style="width:410px; float:left;">'+
						'<span class="property-value" style="font: bold 16px arial;">'+ data[loop].name +'</span>'+
						'</div>' +
						'<div style=" float:left;">'+
						'<span class="property-value" style="font: bold 16px arial;">'+ data[loop].organizationCode +'</span>'+
						'</div>' +
						'</div>' +
						
						'<div class="result-body" style="float:left;">' +
						'<div id="org-col1" style="width: 220px; float: left;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Branch Name</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].branchName +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Address 1</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].addressLine1 +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Locality</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].locality +'</span>' +
						'</div>' +
						'</div>' +
						
						'<div id="org-col2" style="width: 185px; float: left; margin-left: 5px;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">City</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].city +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">State</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].state +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Country</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].country +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Zipcode</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].zipcode +'</span>' +
						'</div>' +
						'</div>' +
						
						
						'<div id="org-col3" style="width: 155px; float: left;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Phone 1</span>' +
						'</div>' +
						'<div style="margin-left: 57px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].phone1 +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Mobile</span>' +
						'</div>' +
						'<div style="margin-left: 57px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].mobile +'</span>' +
						'</div>' +
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Email</span>' +
						'</div>' +
						'<div style="float:right;width:100px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].email +'</span>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action" id="green-result-col-action-'+data[loop].id+'" style="width: 82px; height:95px;padding: 13px 5px 2px 5px">' +
						'<div style="width: 220px;"> '+
						'<div id="'+data[loop].id+'" style="margin-top:25px; padding: 1px;float: left;" class="ui-btn edit-icon" title="Edit Organization"></div>' +
						'<div id="'+data[loop].id+'" style="margin-top:25px; padding: 1px;float: left;" class="ui-btn btn-view" title="View Organization"></div>'+
						'<div id="'+data[loop].id+'" align="'+data[loop].mainBranch+'" style="margin-top:25px; float: left;" class="ui-btn enable-icon" title="Disable Organization"></div>' +
						'</div>' +
				        '</div>';
						$('#school-results-list').append(rowstr);	
						//var organizationName = obj.result.data;
						if(organizationName == undefined){
						}else{
						for(var i=0; i<organizationName.length; i=i+1) {
						if(data[loop].superUserName == organizationName[i].disabledLoginUserName){
							//$("#organization-row-"+ data[loop].id).css("width","580px");
							$("#green-result-row-"+ data[loop].id).css('opacity', '0.5');
							 //("#organization-row-"+ data[loop].id).addClass("green-result-col-disabled-1");
							 //$("#green-result-col-action-"+ data[loop].id).find(".edit-icon").css("pointer-events", "none");
							 $("#green-result-col-action-"+ data[loop].id).find(".edit-icon").hide();
							 $("#green-result-col-action-"+ data[loop].id).find(".btn-view").css("pointer-events", "none");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-disable").removeClass("disable-icon");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-disable").addClass("enable-icon");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-icon").attr('title', 'Enable Organization');
						    }else{
						    	 $("#green-result-col-action-"+ data[loop].id).find(".disable-icon").attr('title', 'Disable Organization');
						    }
						  }
					    }  
						}else{
						if(alternate) {
								var rowstr = '<div class="green-result-row alternate" id="green-result-row-'+data[loop].id+'"  style="height:90px;">';
						} else {
							rowstr = '<div class="green-result-row" id="green-result-row-'+data[loop].id+'"  style="height:90px;">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div class="green-result-col-1" id="organization-row-'+data[loop].id +'"  style="width:575px;">'+
						'<div class="result-title" style="width:575px;">'+
						'<div style="width:410px; float:left;">'+
						'<span class="property-value" style="font: bold 16px arial;">'+ data[loop].name +'</span>'+
						'</div>' +
						'<div style=" float:left;">'+
						'<span class="property-value" style="font: bold 16px arial;">'+ data[loop].organizationCode +'</span>'+
						'</div>' +
						'</div>' +
						
						'<div class="result-body" style="float:left;">' +
						'<div id="org-col1" style="width: 220px; float: left;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Branch Name</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].branchName +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Address 1</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].addressLine1 +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Locality</span>' +
						'</div>' +
						'<div style="margin-left: 87px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].locality +'</span>' +
						'</div>' +
						'</div>' +
						
						
						'<div id="org-col2" style="width: 185px; float: left; margin-left: 5px;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">City</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].city +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">State</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].state +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Country</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].country +'</span>' +
						'</div>' +
						
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Zipcode</span>' +
						'</div>' +
						'<div style="margin-left: 56px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].zipcode +'</span>' +
						'</div>' +
						'</div>' +
						'<div id="org-col3" style="width: 155px; float: left;">'+
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Phone 1</span>' +
						'</div>' +
						'<div style="margin-left: 57px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].phone1 +'</span>' +
						'</div>' +
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Mobile</span>' +
						'</div>' +
						'<div style="margin-left: 57px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].mobile +'</span>' +
						'</div>' +
						'<div style="float: left; position: absolute;">'+
						'<span class="property" style="font: normal 13px arial;">Email</span>' +
						'</div>' +
						'<div style="float:right;width:100px;">'+
						'<span class="property-value" style="word-wrap:break-word;">' + data[loop].email +'</span>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action" id="green-result-col-action-'+data[loop].id+'" style="width: 82px; height:75px;padding: 13px 5px 2px 5px">' +
						'<div style="width: 220px;"> '+
						'<div id="'+data[loop].id+'" style="margin-top:25px; padding: 1px;float: left;" class="ui-btn edit-icon" title="Edit Organization"></div>' +
						'<div id="'+data[loop].id+'" style="margin-top:25px; padding: 1px;float: left;" class="ui-btn btn-view" title="View Organization"></div>'+
						'<div id="'+data[loop].id+'" align="'+data[loop].mainBranch+'" style="margin-top:25px; float: left;" class="ui-btn disable-icon enable-disable" title="Disable Organization"></div>' +
						'</div>' +
				        '</div>';
						$('#school-results-list').append(rowstr);
						//var organizationName = obj.result.data;
						if(organizationName == undefined){
						}else{
						for(var i=0; i<organizationName.length; i=i+1) {
						if(data[loop].superUserName == organizationName[i].disabledLoginUserName){
							$("#green-result-row-"+ data[loop].id).css('opacity', '0.5');
							 //("#organization-row-"+ data[loop].id).addClass("green-result-col-disabled-1");
							 $("#green-result-col-action-"+ data[loop].id).find(".edit-icon").hide();
							 //$("#green-result-col-action-"+ data[loop].id).find(".btn-view").css("pointer-events", "none");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-disable").removeClass("disable-icon");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-disable").addClass("enable-icon");
							 $("#green-result-col-action-"+ data[loop].id).find(".enable-icon").attr('title', 'Enable Organization');
						    }else{
						    	 $("#green-result-col-action-"+ data[loop].id).find(".disable-icon").attr('title', 'Disable Organization');
						    }
						}
					}
				}
			};
			OrganizationHandler.initOrganizationListButtons();
			$('#school-results-list').jScrollPane({showArrows:true});
				} else {
					$('#school-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">No search results found</div></div></div>');
				}
				$.loadAnimation.end();
				setTimeout(function(){
					$('#school-results-list').jScrollPane({showArrows:true});
				},0);
				
	        });
		});
	},
	initSearchOrganization : function (role) {
		$('#action-clear').click(function() {
			//code for conformation message in organization when click on clear button.
			$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$('#country').val('');
						$(this).dialog('close');
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		    return false;
		});
		//button click - search
		$('#action-search-organization').click(function() {
			var thisButton = $(this);
			OrganizationHandler.initsearchOrganizationOnload();
	  });
	},
	initOrgNames:function(orgId){
		$('#mainBranchName').click(function() {
			var thisInput = $(this);
			$('#auto-suggestions').show();
			OrganizationHandler.suggestOrganizationNames(thisInput,orgId);
		});
		$('#mainBranchName').keyup(function() {
			var thisInput = $(this);
			$('#auto-suggestions').show();
			OrganizationHandler.suggestOrganizationNames(thisInput,orgId);
		});
		$('#mainBranchName').focusout(function() {
			$('#auto-suggestions').animate({
				display : 'none'
			},0, function() {
			});
		});
	},
	initOrganizationName : function(){
		var orgId=0;
		$('#mainBranchName').click(function() {
			var thisInput = $(this);
			$('#auto-suggestions').show();
			OrganizationHandler.suggestOrganizationNames(thisInput,orgId);
		});
		$('#mainBranchName').keyup(function() {
			var thisInput = $(this);
			$('#auto-suggestions').show();
			OrganizationHandler.suggestOrganizationNames(thisInput,orgId);
		});
		$('#mainBranchName').focusout(function() {
			$('#auto-suggestions').animate({
				display : 'none'
			},0, function() {
			});
		});
	},
	initAdd:function(){
		$('#button-next').click(function() {
			if(typeof prevPrefix == "undefined"){
				 prevUserName = '';
				 prevPrefix = '';
			}
			var resultSuccess=true;
			if(OrganizationHandler.validateSuperUserName()== false){
				return resultSuccess;
			}
			if(OrganizationHandler.flag == false){
				return resultFailure;
			}
			var resultFailure=false;
			if(OrganizationHandler.organizationStepCount == 0){
				if(OrganizationHandler.validateOrganizationStepOne() == false){
					$("#username").val('');
					$("#password").val('');
					$("#uValid").empty();
					$("#pValid").empty();
					return resultSuccess;
				}
				
				if(OrganizationHandler.checkUserPrefix() == false){
					return resultSuccess;
				}
			}else if(OrganizationHandler.organizationStepCount == 1){
				if(OrganizationHandler.validateOrganizationStepTwo() == false){
					return  resultSuccess;
				}
			}
			var paramString = $(OrganizationHandler.organizationSteps[OrganizationHandler.organizationStepCount]).serialize();
			if($('#mainBranch').val()=='N' && $('#mainBranchName').val() == ''){
				$('#branchMainPop').show();
				return false;
			}
			$.ajax({ type: "POST",
				url:'organization.json', 
				data: paramString,
				success: function(data){
					$('#error-message').html('');
					$('#error-message').hide();
					$(OrganizationHandler.organizationSteps[OrganizationHandler.organizationStepCount]).hide();
					$(OrganizationHandler.organizationSteps[++OrganizationHandler.organizationStepCount]).show();
					if(OrganizationHandler.organizationStepCount == OrganizationHandler.organizationSteps.length) {
						$('#button-next').hide();
						$('#action-clear').hide();
						$('#action-save').show();
						$('#action-update').show();
						$.post('siteadmin/organization_preview.jsp', 'viewType=preview',
						        function(data){
							$('#organization-user-preview-container').css({'height' : '350px'});
								$('#organization-user-preview-container').html(data);
								$('.table-field').css({"width":"800px"});
								$('.main-table').css({"width":"400px"});
								$('.inner-table').css({"width":"400px"});
								$('.display-boxes-colored').css({"width":"140px"});
								$('.display-boxes').css({"width":"255px"});
								$('#organization-user-preview-container').show();
								OrganizationHandler.expanded=false;
										$('#ps-exp-col').click(function() {
											if (OrganizationHandler.organizationStepCount==OrganizationHandler.organizationSteps.length)
											{
								    if(!PageHandler.expanded) {
								    	$('#organization-user-preview-container').css({'height' : '350px'});
										$('#organization-user-preview-container').html(data);
										$('.table-field').css({"width":"800px"});
										$('.main-table').css({"width":"400px"});
										$('.inner-table').css({"width":"400px"});
										$('.display-boxes-colored').css({"width":"140px"});
										$('.display-boxes').css({"width":"255px"});
										$('#organization-user-preview-container').show();
										OrganizationHandler.expanded=false;
								    }
								    else{
								    	$('#organization-user-preview-container').css({'height' : '350px'});
										$('#organization-user-preview-container').html(data);
										$('.table-field').css({"width":"662px"});
										$('.main-table').css({"width":"330px"});
										$('.inner-table').css({"width":"330px"});
										$('.display-boxes-colored').css({"width":"125px"});
										$('.display-boxes').css({"width":"200px"});
										$('#organization-user-preview-container').show();
										OrganizationHandler.expanded=true;
								    }
											}
									});
					});
					}if(OrganizationHandler.organizationStepCount>0) {
						$('#button-prev').show();
						$('.page-buttons').css('margin-left', '150px');
						
					} else {
						$('#button-prev').hide();
						$('.page-buttons').css('margin-left', '200px');
					}
			},
	        error: function(data){
				$('#error-message').html(data.responseText);
				$('#error-message').dialog();
				$('#error-message').show();
			}
			});
			$("#username").change(function(event){
		    	   OrganizationHandler.validateSuperUserName();
			});
		});
		$('#button-prev').click(function() {
			$('#action-clear').show();
			if(OrganizationHandler.organizationStepCount==OrganizationHandler.organizationSteps.length) {
				/*if(!OrganizationHandler.expanded){
					PageHandler.pageSelectionHidden =false;
					PageHandler.hidePageSelection();
					OrganizationHandler.expanded=true;orgId
				}*/
				$('#button-next').show();
				$('#action-save').hide();
				$('#action-update').hide();
				$('#organization-user-preview-container').html('');
				$('#organization-user-preview-container').hide();
				$('.page-buttons').css('margin-left', '150px');
			}
			$(OrganizationHandler.organizationSteps[OrganizationHandler.organizationStepCount]).hide();
			$(OrganizationHandler.organizationSteps[--OrganizationHandler.organizationStepCount]).show();
			if(OrganizationHandler.organizationStepCount>0) {
				$('#button-prev').show();
				$('.page-buttons').css('margin-left', '150px');
			} else {
				$('#button-prev').hide();
				$('.page-buttons').css('margin-left', '240px');
			}
		});
	},
	validateSuperUserName: function() {
		var result=true;
		var superUserName = $("#username").val();
		var paramString='superUser='+superUserName+'&action=validate-organization-super-user-name';
		$.ajax({type: "POST",
			url:'organization.json', 
			data: paramString,
			async : false,
			success :function(data) {
	    if(prevUserName != superUserName){
		    if (data.result.data == "y") {
		    	 $('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>Super Username Alreday Exists!");
			        $('#userName').focus();
			        result =false;
		    }else{
		    	 $('#unValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='Super Username available!'> ");
		    }
	    }else{
	    	$('#unValid').empty();
	    }
			},
	    });
		return result;
	},
	suggestOrganizationNames : function(thisInput,orgId) {
		var suggestionsDiv = $('#auto-suggestions');
		var mainBranchNameVal = $('#mainBranchName').val();
		$.post('organization.json','action=get-all-organization-names&id='+orgId,function(obj) {
			$.loadAnimation.end();
			suggestionsDiv.html('');
			var data = obj.result.data;
			if (data != undefined) {
				var htmlStr = '<div>';
				for ( var loop = 0; loop < data.length; loop = loop + 1) {
					htmlStr += '<li><a class="select-teacher" style="cursor: pointer;">' + data[loop] + '</a></li>';
				}
				htmlStr += '</div>';
				suggestionsDiv.append(htmlStr);
			} else {
				suggestionsDiv.append('<div id="" class="select-teacher">' + 'No Organizations To Select' + '</div>');
			}
			suggestionsDiv.css('left', thisInput.position().left);
			suggestionsDiv.css('top', thisInput.position().top + 25);
			suggestionsDiv.show();
			$('.select-teacher').click(function() {
				thisInput.val($(this).html());
				thisInput.attr('mainBranchName', $(this).attr('mainBranchName'));
				$('#mainBranchName').attr('value', $(this).attr('mainBranchName'));
				suggestionsDiv.hide();
				$('#branchMainPop').hide();
			});
		});
	},
	
	initOrganizationCreate: function() {
		PageHandler.hidePageSelection();
		PageHandler.pageSelectionHidden =true;
		$('#action-cancel').click(function(){
	    	$('#error-message').html(Msg.CANCEL_BUTTON_MESSAGE);   
			$("#error-message").dialog({
				resizable: false,
				height:140,
				title: "<span class='ui-dlg-confirm'>Confirm</span>",
				modal: true,
				buttons: {
					'Ok' : function() {
						$(this).dialog('close');
		    			$('.task-page-container').html('');
		    			location.href = "index.jsp?module=siteadmin"
		    			setTimeout(function(){
							$('#school-results-list').jScrollPane({showArrows:true});
						},0);
						PageHandler.pageSelectionHidden =false;
						PageHandler.hidePageSelection();
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		    return false;
		});
		$('#usernamePrefix').change(function(){
			if(typeof prevPrefix == "undefined"){
				 prevUserName = '';
				 prevPrefix = '';
			}
			OrganizationHandler.checkUserPrefix();
			var prefix = $('#usernamePrefix').val();
			$("#orgPrefix").val(prefix+".");
		});
		$.fn.clear = function() {
			  return this.each(function() {
			    var type = this.type, tag = this.tagName.toLowerCase();
			    if (tag == 'form')
			      return $(':input',this).clear();
			    if($(this).hasClass('read-only'))
			    	return;
			    
			    if (type == 'text' || type == 'password' || tag == 'textarea')
			      this.value = '';
			    /*else if (type == 'checkbox' || type == 'radio')
			      this.checked =true;*/
			    else if (tag == 'select'){
			    	this.selectedIndex = 0;
			    }
			  });
			};
			$('#action-clear').click(function() {
				//code for conformation message in organization when click on clear button.
				$('#error-message').html(Msg.CLEAR_BUTTON_MESSAGE);   
				$("#error-message").dialog({
					resizable: false,
					height:140,
					title: "<span class='ui-dlg-confirm'>Confirm</span>",
					modal: true,
					buttons: {
						'Ok' : function() {
							$(this).dialog('close');
							$(OrganizationHandler.organizationSteps[OrganizationHandler.organizationStepCount]).clear();
							$('#prefixValid').hide();
						},
						Cancel: function() {
							$(this).dialog('close');
						}
					}
				});
			    return false;
			});
		
		$('#mainBranch').click(function(){
			if($('#mainBranch').val() == 'Y'){
				$('#mainBranchName').attr("disabled", "disabled");
			}else{
				$('#mainBranchName').removeAttr("disabled");
			}
		});
		
		$("#organizationCode").keyup(function(event){
			$('#organizationCodeValid').empty();
			 $('#namehelp_pop').show();
			 $('#code_pop').hide();
		});
		$("#organizationCode").blur(function(event){
			 $('#namehelp_pop').hide();
		});
		
		$("#username").focus(function(event){
			$('#uValid').empty();
			 $('#username_pop').show();
		});
		$("#username").blur(function(event){
			 $('#username_pop').hide();
		});
		
		$("#password").focus(function(event){
			$('#pValid').empty();
			 $('#password_pop').show();
		});
		$("#password").blur(function(event){
			 $('#password_pop').hide();
		});
		
		$('#action-clear').click(function(){
			var mainBranchVal = $('#mainBranch').val();
			$('#organization-create-form').clearForm();
			$('#mainBranch').val(mainBranchVal);
			$('#organizationCodeValid').empty();
			$('#organizationNameValid').empty();
			$('#branchNameValid').empty();
			$('#descriptionValid').empty();
			$('#fillNameValid').empty();
			$('#uValid').empty();
			$('#pValid').empty();
			$('#unValid').empty();
			$('#pwValid').empty();
			$('#pincodeValid').empty();
			$('#cityValid').empty();
			$('#stateValid').empty();
			$('#countryValid').empty();
			$('#phone1Valid').empty();
			$('#phone2Valid').empty();
			$('#phone3Valid').empty();
			$('#emailValid').empty();
			$('#landmarkValid').empty();
			$('#localityValid').empty();
			$('#addressLine1Valid').empty();
			$('#addressLine2Valid').empty();
			$('#branchNameValid').empty();
			$('#organizationCodeValid').empty();
		});
		
		
		$('#action-save').click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			var paramString = $('#organization-create-form').serialize();
			$('.page-content').ajaxSavingLoader();
			$.post('organization.json', paramString+'&action=save-organization-details',
		        function(data){
				$.loadAnimation.end();
				$(this).successMessage({
					container : '.organization-page-container',
					data : data.result.message
				});
		        }
	        );
		}); 
		$('#action-update').click(function(){
			var thisButton = $(this);
			var resultSuccess=true;
			var resultFailure=false;
			var id = $('#organizationId').val();  
			$('.page-content').ajaxSavingLoader();
			$.post('organization.json', 'id='+id+'&action=update-organization',
		        function(data){
				$.loadAnimation.end();
				$(this).successMessage({
					container : '.organization-page-container',
					data : data.result.message
				});
		        }
	        );
		});
		
	},
	validateMainBranch:function(){
		var result=true;
		var orgId=0;
			var data=obj.result.data;
			/*for(var i=0;i<data.length;i=i+1){
					arry.push(data[i]);
			}*/
			if($('#mainBranchName').val().length !== 0){
				if(jQuery.inArray( $('#mainBranchName').val(), OrganizationHandler.arry) == -1){
					result=false;
					$('#incorrecthelp').show();
					$('#mainBranchName').focus();
				}
			}
		return result;
	},
	validateOrganizationStepOne:function(){
		var result=true;
		var orgcode = $('#organizationCode').val();
		var end = orgcode.length;
		if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgcode) === false || orgcode.length === 0 || orgcode.charAt(0) == " " || orgcode.charAt(end - 1) == " "){
			$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#organizationCode").focus(function(event){
				$('#organizationCodeValid').empty();
				 $('#namehelp_pop').show();
			});
			$("#organizationCode").blur(function(event){
				 $('#namehelp_pop').hide();
				 var orgcode = $('#organizationCode').val();
				 var end = orgcode.length;
				 if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgcode) === false || orgcode.length === 0 || orgcode.charAt(0) == " " || orgcode.charAt(end - 1) == " "){
					 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#organizationCode").focus(function(event){
							$('#organizationCodeValid').empty();
							 $('#namehelp_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#organizationCode').val()).length > 30){
			$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#organizationCode").focus(function(event){
				$('#organizationCodeValid').empty();
				 $('#codedb_pop').show();
			});
			$("#organizationCode").blur(function(event){
				 $('#codedb_pop').hide();
				 if(($('#organizationCode').val()).length > 30){
					 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#organizationCode").focus(function(event){
							$('#organizationCodeValid').empty();
							 $('#codedb_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var usernamePrefix = $('#usernamePrefix').val();
		if(/^[a-zA-Z0-9.@\s]+$/.test(usernamePrefix) === false || usernamePrefix.length === 0 || usernamePrefix.charAt(0) == " " || usernamePrefix.charAt(end - 1) == " "){
			$('#prefixValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#usernamePrefix").focus(function(event){
				 $('#prefixValid_pop').hide();
				$('#prefixValid').empty();
				 $('#prefix_pop').show();
			});
			$("#usernamePrefix").blur(function(event){
				 $('#prefix_pop').hide();
				 var usernamePrefix = $('#usernamePrefix').val();
				 var end = usernamePrefix.length;
				 if(/^[a-zA-Z0-9.@\s]+$/.test(usernamePrefix) === false || usernamePrefix.length === 0 || usernamePrefix.charAt(0) == " " || usernamePrefix.charAt(end - 1) == " "){
					 $('#prefixValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#usernamePrefix").focus(function(event){
						 $('#prefixValid_pop').hide();
							$('#prefixValid').empty();
							 $('#prefix_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgname = $('#name').val();
		var end = orgname.length;
		if(/^[a-zA-Z0-9-_#@()(?!.*?\.\.)/\s]+$/.test(orgname) === false || orgname.length === 0 || orgname.charAt(0) == " " || orgname.charAt(end - 1) == " "){
			$('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#name").focus(function(event){
				$('#organizationNameValid').empty();
				 $('#organizationName_pop').show();
			});
			$("#name").blur(function(event){
				var orgname = $('#name').val();
				var end = orgname.length;
				 $('#organizationName_pop').hide();
				 if(/^[a-zA-Z0-9-_#@()(?!.*?\.\.)/\s]+$/.test(orgname) === false || orgname.length === 0 || orgname.charAt(0) == " " || orgname.charAt(end - 1) == " "){
					 $('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#name").focus(function(event){
							$('#organizationNameValid').empty();
							 $('#organizationName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#name').val()).length > 400){
			$('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#name").focus(function(event){
				$('#organizationNameValid').empty();
				 $('#dbName_pop').show();
			});
			$("#name").blur(function(event){
				 $('#dbName_pop').hide();
				 if(($('#name').val()).length > 400){
					 $('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#name").focus(function(event){
							$('#organizationNameValid').empty();
							 $('#dbName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgbranch = $('#branchName').val();
		var end = orgbranch.length;
		if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgbranch) === false || orgbranch.length === 0 || orgbranch.charAt(0) == " " || orgbranch.charAt(end - 1) == " " ){
			$('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#branchName").focus(function(event){
				$('#branchNameValid').empty();
				 $('#branchName_pop').show();
			});
			$("#branchName").blur(function(event){
				 $('#branchName_pop').hide();
				 var orgbranch = $('#branchName').val();
				 var end = orgbranch.length;
				 if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgbranch) === false || orgbranch.length === 0 || orgbranch.charAt(0) == " " || orgbranch.charAt(end - 1) == " " ){
					 $('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#branchName").focus(function(event){
							$('#branchNameValid').empty();
							 $('#branchName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		if(($('#branchName').val()).length > 400){
			$('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#branchName").focus(function(event){
				$('#branchNameValid').empty();
				 $('#dbBranchName_pop').show();
			});
			$("#branchName").blur(function(event){
				 $('#dbBranchName_pop').hide();
				 if(($('#branchName').val()).length > 400){
					 $('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#branchName").focus(function(event){
							$('#branchNameValid').empty();
							 $('#dbBranchName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgdesc = $('#description').val();
		var end = orgdesc.length;
		if(orgdesc.length > 200 || orgdesc.charAt(0) == " " || orgdesc.charAt(end - 1) == " "){
			$('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#description").focus(function(event){
				$('#descriptionValid').empty();
				 $('#dbdescription_pop').show();
			});
			$("#description").blur(function(event){
				 $('#dbdescription_pop').hide();
				 var orgdesc = $('#description').val();
				 var end = orgdesc.length;
				 if(orgdesc.length > 200 || orgdesc.charAt(0) == " " || orgdesc.charAt(end - 1) == " "){
					 $('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#description").focus(function(event){
							$('#descriptionValid').empty();
							 $('#dbdescription_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgaddr1 = $('#addressLine1').val();
		var end = orgaddr1.length;
		if(orgaddr1.length === 0 || orgaddr1.charAt(0) == " " || orgaddr1.charAt(end - 1) == " "){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#addressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#addressLine1_pop').hide();
				 var orgaddr1 = $('#addressLine1').val();
				 var end = orgaddr1.length;
				 if(orgaddr1.length === 0 || orgaddr1.charAt(0) == " " || orgaddr1.charAt(end - 1) == " "){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#addressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		if(($('#addressLine1').val()).length > 200){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#dbAddressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#dbAddressLine1_pop').hide();
				 if(($('#addressLine1').val()).length > 200){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#dbAddressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgaddr2 = $('#addressLine2').val();
		var end = orgaddr2.length;
		if(orgaddr2.length > 200 || orgaddr2.charAt(0) == " " || orgaddr2.charAt(end - 1) == " "){
			$('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine2").focus(function(event){
				$('#addressLine2Valid').empty();
				 $('#dbAddressLine2_pop').show();
			});
			$("#addressLine2").blur(function(event){
				 $('#dbAddressLine2_pop').hide();
				 var orgaddr2 = $('#addressLine2').val();
				 var end = orgaddr2.length;
				 if(orgaddr2.length > 200 || orgaddr2.charAt(0) == " " || orgaddr2.charAt(end - 1) == " "){
					 $('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine2").focus(function(event){
							$('#addressLine2Valid').empty();
							 $('#dbAddressLine2_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		
		if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val()) === false && ($('#landmark').val()).length > 0){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#landmark_pop').show();
			});
			$("#landmark").blur(function(event){
				 $('#landmark_pop').hide();
				 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val()) === false && ($('#landmark').val()).length > 0){
					 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#landmark").focus(function(event){
							$('#landmarkValid').empty();
							 $('#landmark_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgland = $('#landmark').val();
		var end = orgland.length;
		if(orgland.length > 60 || orgland.charAt(0) == " " || orgland.charAt(end - 1) == " "){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#dbLandmark_pop').show();
			});
			$("#landmark").blur(function(event){
				 $('#dbLandmark_pop').hide();
				 var orgland = $('#landmark').val();
				 var end = orgland.length;
				 if(orgland.length > 60 || orgland.charAt(0) == " " || orgland.charAt(end - 1) == " "){
					 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#landmark").focus(function(event){
							$('#landmarkValid').empty();
							 $('#dbLandmark_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgloc = $('#locality').val();
		var end = orgloc.length;
		if(orgloc.length === 0 || orgloc.charAt(0) == " " || orgloc.charAt(end - 1) == " "){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#locality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#locality_pop').hide();
				 var orgloc = $('#locality').val();
				 var end = orgloc.length;
				 if(orgloc.length === 0 || orgloc.charAt(0) == " " || orgloc.charAt(end - 1) == " "){
					 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#localityValid').empty();
							 $('#locality_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z$@#%^&*]+$/.test($('#currencyFormat').val()) === false||$('#currencyFormat').val().length === 0){
			$('#currencyValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#currencyFormat").focus(function(event){
				$('#currencyValid').empty();
				 $('#currency_pop').show();
			});
			$("#currencyFormat").blur(function(event){
				 $('#currency_pop').hide();
				 if(/^[a-zA-Z$@#%^&*]+$/.test($('#currencyFormat').val()) === false||$('#currencyFormat').val().length === 0){
					 $('#currencyValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#currencyValid').empty();
							 $('#currency_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		if(($('#locality').val()).length > 60){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#dbLocality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#dbLocality_pop').hide();
				 if(($('#locality').val()).length > 60){
					 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#localityValid').empty();
							 $('#dbLocality_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgcity = $('#city').val();
		var end = orgcity.length;
		if(/^[a-zA-Z.\s]+$/.test(orgcity) === false  || orgcity.length === 0 || orgcity.charAt(0) == " " || orgcity.charAt(end - 1) == " "){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#city_pop').show();
			});
			$("#city").blur(function(event){
				 $('#city_pop').hide();
				 var orgcity = $('#city').val();
				 var end = orgcity.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgcity) === false  || orgcity.length === 0 || orgcity.charAt(0) == " " || orgcity.charAt(end - 1) == " "){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#city_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#city').val()).length > 60){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#dbCity_pop').show();
			});
			$("#city").blur(function(event){
				 $('#dbCity_pop').hide();
				 if(($('#city').val()).length > 60){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#dbCity_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
				var orgstate = $('#state').val();
		var end = orgstate.length;
		if(/^[a-zA-Z.\s]+$/.test(orgstate) === false || orgstate.length === 0 || orgstate.charAt(0) == " " || orgstate.charAt(end - 1) == " " ){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#state_pop').show();
			});
			$("#state").blur(function(event){
				 $('#state_pop').hide();
				 var orgstate = $('#state').val();
				 var end = orgstate.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgstate) == false || orgstate.length == 0 || orgstate.charAt(0) == " " || orgstate.charAt(end - 1) == " " ){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#state_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		if(($('#state').val()).length > 60){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#dbState_pop').show();
			});
			$("#state").blur(function(event){
				 $('#dbState_pop').hide();
				 if(($('#state').val()).length > 60){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#dbState_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		var orgcoun = $('#country').val();
		var end = orgcoun.length;
		if(/^[a-zA-Z.\s]+$/.test(orgcoun) === false || orgcoun.length === 0 || orgcoun.charAt(0) == " " || orgcoun.charAt(end - 1) == " "){
			$('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#country").focus(function(event){
				$('#countryValid').empty();
				 $('#country_pop').show();
			});
			$("#country").blur(function(event){
				 $('#country_pop').hide();
				 var orgcoun = $('#country').val();
				 var end = orgcoun.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgcoun) === false || orgcoun.length === 0 || orgcoun.charAt(0) == " " || orgcoun.charAt(end - 1) == " "){
					 $('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#country").focus(function(event){
							$('#countryValid').empty();
							 $('#country_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#country').val()).length > 50){
			$('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#country").focus(function(event){
				$('#countryValid').empty();
				 $('#dbCountry_pop').show();
			});
			$("#country").blur(function(event){
				 $('#dbCountry_pop').hide();
				 if(($('#country').val()).length > 50){
					 $('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#country").focus(function(event){
							$('#countryValid').empty();
							 $('#dbCountry_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgzip = $('#zipcode').val();
		var end = orgzip.length;
		if(/^[a-zA-Z0-9-\s]+$/.test(orgzip) === false || orgzip.length > 9 || orgzip.charAt(0) == " " || orgzip.charAt(end - 1) == " "){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 var orgzip = $('#zipcode').val();
				 var end = orgzip.length;
				 if(/^[a-zA-Z0-9-\s]+$/.test(orgzip) === false || orgzip.length > 9 || orgzip.charAt(0) == " " || orgzip.charAt(end - 1) == " "){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#zipcode').val()).length === 0 ){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 if(($('#zipcode').val()).length === 0 ){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgph1 = $('#phone1').val();
		var end = orgph1.length;
		if((/^[0-9-+()\s]+$/.test(orgph1) === false || orgph1.length === 0) || orgph1.charAt(0) == " " || orgph1.charAt(end - 1) == " "){
			$('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone1").focus(function(event){
				$('#phone1Valid').empty();
				 $('#phone1_pop').show();
			});
			$("#phone1").blur(function(event){
				 $('#phone1_pop').hide();
				 var orgph1 = $('#phone1').val();
				 var end = orgph1.length;
				 if((/^[0-9-+()\s]+$/.test(orgph1) === false && orgph1.length > 0) || orgph1.charAt(0) == " " || orgph1.charAt(end - 1) == " "){
					 $('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone1").focus(function(event){
							$('#phone1Valid').empty();
							 $('#phone1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#phone1').val()).length > 60){
			$('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone1").focus(function(event){
				$('#phone1Valid').empty();
				 $('#dbPhone1_pop').show();
			});
			$("#phone1").blur(function(event){
				 $('#dbPhone1_pop').hide();
				 if(($('#phone1').val()).length > 60 ){
					 $('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone1").focus(function(event){
							$('#phone1Valid').empty();
							 $('#dbPhone1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgph2 = $('#phone2').val();
		var end = orgph2.length;
		if((/^[0-9-+()\s]+$/.test(orgph2) === false && orgph2.length > 0) || orgph2.charAt(0) == " " || orgph2.charAt(end - 1) == " "){
			$('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone2").focus(function(event){
				$('#phone2Valid').empty();
				 $('#phone2_pop').show();
			});
			$("#phone2").blur(function(event){
				 $('#phone2_pop').hide();
				 var orgph2 = $('#phone2').val();
				 var end = orgph2.length;
				 if((/^[0-9-+()\s]+$/.test(orgph2) === false && orgph2.length > 0) || orgph2.charAt(0) == " " || orgph2.charAt(end - 1) == " "){
					 $('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone2").focus(function(event){
							$('#phone2Valid').empty();
							 $('#phone2_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		if(($('#phone2').val()).length > 60){
			$('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone2").focus(function(event){
				$('#phone2Valid').empty();
				 $('#dbPhone2_pop').show();
			});
			$("#phone2").blur(function(event){
				 $('#dbPhone2_pop').hide();
				 if(($('#phone2').val()).length > 60 ){
					 $('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone2").focus(function(event){
							$('#phone2Valid').empty();
							 $('#dbPhone2_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		/*var orgId=0;
		if($('#mainBranchName').val().length != 0){
			var value=false;
			$.POST('organization.json','action=get-all-organization-names&id='+orgId,function(obj) {
				var data=obj.result.data;
				for(var i=0;i<data.length;i=i+1){
					if(data[i]===$('#mainBranchName').val()){
						alert("dfsdffff");
						value=true;
					}
				}
			}); 
			alert(value);
			result=value;
		}
		alert(result);*/
		return result;
	},
	
	validateOrganizationUpdate : function(){
		var result=true;
		var orgcode = $('#organizationCode').val();
		var end = orgcode.length;
		if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgcode) === false || orgcode.length === 0 || orgcode.charAt(0) == " " || orgcode.charAt(end - 1) == " "){
			$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#organizationCode").focus(function(event){
				$('#organizationCodeValid').empty();
				 $('#namehelp_pop').show();
			});
			$("#organizationCode").blur(function(event){
				 $('#namehelp_pop').hide();
				 var orgcode = $('#organizationCode').val();
				 var end = orgcode.length;
				 if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgcode) === false || orgcode.length === 0 || orgcode.charAt(0) == " " || orgcode.charAt(end - 1) == " "){
					 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#organizationCode").focus(function(event){
							$('#organizationCodeValid').empty();
							 $('#namehelp_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#organizationCode').val()).length > 30){
			$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#organizationCode").focus(function(event){
				$('#organizationCodeValid').empty();
				 $('#codedb_pop').show();
			});
			$("#organizationCode").blur(function(event){
				 $('#codedb_pop').hide();
				 if(($('#organizationCode').val()).length > 30){
					 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#organizationCode").focus(function(event){
							$('#organizationCodeValid').empty();
							 $('#codedb_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgname = $('#name').val();
		var end = orgname.length;
		if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgname) === false || orgname.length === 0 || orgname.charAt(0) == " " || orgname.charAt(end - 1) == " "){
			$('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#name").focus(function(event){
				$('#organizationNameValid').empty();
				 $('#organizationName_pop').show();
			});
			$("#name").blur(function(event){
				var orgname = $('#name').val();
				var end = orgname.length;
				 $('#organizationName_pop').hide();
				 if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgname) === false || orgname.length === 0 || orgname.charAt(0) == " " || orgname.charAt(end - 1) == " "){
					 $('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#name").focus(function(event){
							$('#organizationNameValid').empty();
							 $('#organizationName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#name').val()).length > 400){
			$('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#name").focus(function(event){
				$('#organizationNameValid').empty();
				 $('#dbName_pop').show();
			});
			$("#name").blur(function(event){
				 $('#dbName_pop').hide();
				 if(($('#name').val()).length > 400){
					 $('#organizationNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#name").focus(function(event){
							$('#organizationNameValid').empty();
							 $('#dbName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgbranch = $('#branchName').val();
		var end = orgbranch.length;
		if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgbranch) === false || orgbranch.length === 0 || orgbranch.charAt(0) == " " || orgbranch.charAt(end - 1) == " " ){
			$('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#branchName").focus(function(event){
				$('#branchNameValid').empty();
				 $('#branchName_pop').show();
			});
			$("#branchName").blur(function(event){
				 $('#branchName_pop').hide();
				 var orgbranch = $('#branchName').val();
				 var end = orgbranch.length;
				 if(/^[a-zA-Z0-9-_#@()/\s]+$/.test(orgbranch) === false || orgbranch.length === 0 || orgbranch.charAt(0) == " " || orgbranch.charAt(end - 1) == " " ){
					 $('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#branchName").focus(function(event){
							$('#branchNameValid').empty();
							 $('#branchName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#branchName').val()).length > 400){
			$('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#branchName").focus(function(event){
				$('#branchNameValid').empty();
				 $('#dbBranchName_pop').show();
			});
			$("#branchName").blur(function(event){
				 $('#dbBranchName_pop').hide();
				 if(($('#branchName').val()).length > 400){
					 $('#branchNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#branchName").focus(function(event){
							$('#branchNameValid').empty();
							 $('#dbBranchName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgdesc = $('#description').val();
		var end = orgdesc.length;
		if(orgdesc.length > 200 || orgdesc.charAt(0) == " " || orgdesc.charAt(end - 1) == " "){
			$('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#description").focus(function(event){
				$('#descriptionValid').empty();
				 $('#dbdescription_pop').show();
			});
			$("#description").blur(function(event){
				 $('#dbdescription_pop').hide();
				 var orgdesc = $('#description').val();
				 var end = orgdesc.length;
				 if(orgdesc.length > 200 || orgdesc.charAt(0) == " " || orgdesc.charAt(end - 1) == " "){
					 $('#descriptionValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#description").focus(function(event){
							$('#descriptionValid').empty();
							 $('#dbdescription_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgaddr1 = $('#addressLine1').val();
		var end = orgaddr1.length;
		if(orgaddr1.length === 0 || orgaddr1.charAt(0) == " " || orgaddr1.charAt(end - 1) == " "){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#addressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#addressLine1_pop').hide();
				 var orgaddr1 = $('#addressLine1').val();
				 var end = orgaddr1.length;
				 if(orgaddr1.length === 0 || orgaddr1.charAt(0) == " " || orgaddr1.charAt(end - 1) == " "){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#addressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#addressLine1').val()).length > 200){
			$('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine1").focus(function(event){
				$('#addressLine1Valid').empty();
				 $('#dbAddressLine1_pop').show();
			});
			$("#addressLine1").blur(function(event){
				 $('#dbAddressLine1_pop').pop();
				 if(($('#addressLine1').val()).length > 200){
					 $('#addressLine1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine1").focus(function(event){
							$('#addressLine1Valid').empty();
							 $('#dbAddressLine1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		var orgaddr2 = $('#addressLine2').val();
		var end = orgaddr2.length;
		if(orgaddr2.length > 200 || orgaddr2.charAt(0) == " " || orgaddr2.charAt(end - 1) == " "){
			$('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#addressLine2").focus(function(event){
				$('#addressLine2Valid').empty();
				 $('#dbAddressLine2_pop').show();
			});
			$("#addressLine2").blur(function(event){
				 $('#dbAddressLine2_pop').hide();
				 var orgaddr2 = $('#addressLine2').val();
				 var end = orgaddr2.length;
				 if(orgaddr2.length > 200 || orgaddr2.charAt(0) == " " || orgaddr2.charAt(end - 1) == " "){
					 $('#addressLine2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#addressLine2").focus(function(event){
							$('#addressLine2Valid').empty();
							 $('#dbAddressLine2_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgfname = $('#fullName').val();
		var end = orgfname.length;
		if(/^[a-zA-Z0-9.@\s]+$/.test(orgfname) === false || orgfname.length === 0 || orgfname.charAt(0) == " " || orgfname.charAt(end - 1) == " "){
			$('#fullNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fillNameValid').empty();
				 $('#fullName_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#fullName_pop').hide();
				 var orgfname = $('#fullName').val();
				 var end = orgfname.length;
				 if(/^[a-zA-Z0-9.@\s]+$/.test(orgfname) === false || orgfname.length === 0 || orgfname.charAt(0) == " " || orgfname.charAt(end - 1) == " "){
					 $('#fullNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fillNameValid').empty();
							 $('#fullName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#fullName').val()).length > 100){
			$('#fullNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fillNameValid').empty();
				 $('#dbfname_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#dbfname_pop').hide();
				 if(($('#fullName').val()).length > 100){
					 $('#fillNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fillNameValid').empty();
							 $('#dbfname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val()) === false && ($('#landmark').val()).length > 0){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#landmark_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#landmark_pop').hide();
				 if(/^[a-zA-Z0-9-#.,/\s]+$/.test($('#landmark').val()) === false && ($('#landmark').val()).length > 0){
					 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#landmark").focus(function(event){
							$('#landmarkValid').empty();
							 $('#landmark_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgland = $('#landmark').val();
		var end = orgland.length;
		if(orgland.length > 60 || orgland.charAt(0) == " " || orgland.charAt(end - 1) == " "){
			$('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#landmark").focus(function(event){
				$('#landmarkValid').empty();
				 $('#dbLandmark_pop').show();
			});
			$("#landmark").blur(function(event){
				 $('#dbLandmark_pop').hide();
				 var orgland = $('#landmark').val();
				 var end = orgland.length;
				 if(orgland.length > 60 || orgland.charAt(0) == " " || orgland.charAt(end - 1) == " "){
					 $('#landmarkValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#landmark").focus(function(event){
							$('#landmarkValid').empty();
							 $('#dbLandmark_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgloc = $('#locality').val();
		var end = orgloc.length;
		if(/^[a-zA-Z0-9\s]+$/.test(orgloc) === false || orgloc.length === 0 || orgloc.charAt(0) == " " || orgloc.charAt(end - 1) == " "){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#locality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#locality_pop').hide();
				 var orgloc = $('#locality').val();
				 var end = orgloc.length;
				 if(/^[a-zA-Z0-9\s]+$/.test(orgloc) === false || orgloc.length === 0 || orgloc.charAt(0) == " " || orgloc.charAt(end - 1) == " "){
					 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#localityValid').empty();
							 $('#locality_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#locality').val()).length > 60){
			$('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#locality").focus(function(event){
				$('#localityValid').empty();
				 $('#dbLocality_pop').show();
			});
			$("#locality").blur(function(event){
				 $('#dbLocality_pop').hide();
				 if(($('#locality').val()).length > 60){
					 $('#localityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#locality").focus(function(event){
							$('#localityValid').empty();
							 $('#dbLocality_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgcity = $('#city').val();
		var end = orgcity.length;
		if(/^[a-zA-Z.\s]+$/.test(orgcity) === false  || orgcity.length === 0 || orgcity.charAt(0) == " " || orgcity.charAt(end - 1) == " "){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#city_pop').show();
			});
			$("#city").blur(function(event){
				 $('#city_pop').hide();
				 var orgcity = $('#city').val();
				 var end = orgcity.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgcity) === false  || orgcity.length === 0 || orgcity.charAt(0) == " " || orgcity.charAt(end - 1) == " "){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#city_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#city').val()).length > 60){
			$('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#city").focus(function(event){
				$('#cityValid').empty();
				 $('#dbCity_pop').show();
			});
			$("#city").blur(function(event){
				 $('#dbCity_pop').hide();
				 if(($('#city').val()).length > 60){
					 $('#cityValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#city").focus(function(event){
							$('#cityValid').empty();
							 $('#dbCity_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#email').val();
		if($('#email').ValidateEmailAddr(Email) === false || ($('#email').ValidateEmailAddr(Email)).length === 0){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#email").focus(function(event){
				$('#emailValid').empty();
				 $('#email_pop').show();
			});
			$("#email").blur(function(event){
				 $('#email_pop').hide();
				 if($('#email').ValidateEmailAddr($('#email').val()) === false || ($('#email').ValidateEmailAddr(Email)).length === 0){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#email").focus(function(event){
							$('#emailValid').empty();
							 $('#email_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#email').val();
		if(($('#email').val()).length > 100){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#email").focus(function(event){
				$('#emailValid').empty();
				 $('#dbEmail_pop').show();
			});
			$("#email").blur(function(event){
				 $('#dbEmail_pop').hide();
				 if(($('#email').val()).length > 100){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#email").focus(function(event){
							$('#emailValid').empty();
							 $('#dbEmail_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgstate = $('#state').val();
		var end = orgstate.length;
		if(/^[a-zA-Z.\s]+$/.test(orgstate) === false || orgstate.length === 0 || orgstate.charAt(0) == " " || orgstate.charAt(end - 1) == " " ){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#state_pop').show();
			});
			$("#state").blur(function(event){
				 $('#state_pop').hide();
				 var orgstate = $('#state').val();
				 var end = orgstate.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgstate) === false || orgstate.length === 0 || orgstate.charAt(0) == " " || orgstate.charAt(end - 1) == " " ){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#state_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		if(($('#state').val()).length > 60){
			$('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#state").focus(function(event){
				$('#stateValid').empty();
				 $('#dbState_pop').show();
			});
			$("#state").blur(function(event){
				 $('#dbState_pop').hide();
				 if(($('#state').val()).length > 60){
					 $('#stateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#state").focus(function(event){
							$('#stateValid').empty();
							 $('#dbState_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		var orgcoun = $('#country').val();
		var end = orgcoun.length;
		if(/^[a-zA-Z.\s]+$/.test(orgcoun) === false || orgcoun.length === 0 || orgcoun.charAt(0) == " " || orgcoun.charAt(end - 1) == " "){
			$('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#country").focus(function(event){
				$('#countryValid').empty();
				 $('#country_pop').show();
			});
			$("#country").blur(function(event){
				 $('#country_pop').hide();
				 var orgcoun = $('#country').val();
				 var end = orgcoun.length;
				 if(/^[a-zA-Z.\s]+$/.test(orgcoun) === false || orgcoun.length === 0 || orgcoun.charAt(0) == " " || orgcoun.charAt(end - 1) == " "){
					 $('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#country").focus(function(event){
							$('#countryValid').empty();
							 $('#country_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#country').val()).length > 50){
			$('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#country").focus(function(event){
				$('#countryValid').empty();
				 $('#dbCountry_pop').show();
			});
			$("#country").blur(function(event){
				 $('#dbCountry_pop').hide();
				 if(($('#country').val()).length > 50){
					 $('#countryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#country").focus(function(event){
							$('#countryValid').empty();
							 $('#dbCountry_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgzip = $('#zipcode').val();
		var end = orgzip.length;
		if(/^[a-zA-Z0-9-\s]+$/.test(orgzip) === false || orgzip.length > 9 || orgzip.charAt(0) == " " || orgzip.charAt(end - 1) == " "){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 var orgzip = $('#zipcode').val();
				 var end = orgzip.length;
				 if(/^[a-zA-Z0-9-\s]+$/.test(orgzip) === false || orgzip.length > 9 || orgzip.charAt(0) == " " || orgzip.charAt(end - 1) == " "){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#zipcode').val()).length === 0 ){
			$('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#zipcode").focus(function(event){
				$('#pincodeValid').empty();
				 $('#pincode_pop').show();
			});
			$("#zipcode").blur(function(event){
				 $('#pincode_pop').hide();
				 if(($('#zipcode').val()).length === 0 ){
					 $('#pincodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#zipcode").focus(function(event){
							$('#pincodeValid').empty();
							 $('#pincode_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgph1 = $('#phone1').val();
		var end = orgph1.length;
		if((/^[0-9-+()\s]+$/.test(orgph1) === false && orgph1.length > 0) || orgph1.charAt(0) == " " || orgph1.charAt(end - 1) == " "){
			$('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone1").focus(function(event){
				$('#phone1Valid').empty();
				 $('#phone1_pop').show();
			});
			$("#phone1").blur(function(event){
				 $('#phone1_pop').hide();
				 var orgph1 = $('#phone1').val();
				 var end = orgph1.length;
				 if((/^[0-9-+()\s]+$/.test(orgph1) === false && orgph1.length > 0) || orgph1.charAt(0) == " " || orgph1.charAt(end - 1) == " "){
					 $('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone1").focus(function(event){
							$('#phone1Valid').empty();
							 $('#phone1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#phone1').val()).length > 60){
			$('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone1").focus(function(event){
				$('#phone1Valid').empty();
				 $('#dbPhone1_pop').show();
			});
			$("#phone1").blur(function(event){
				 $('#dbPhone1_pop').hide();
				 if(($('#phone1').val()).length > 60 ){
					 $('#phone1Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone1").focus(function(event){
							$('#phone1Valid').empty();
							 $('#dbPhone1_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgph2 = $('#phone2').val();
		var end = orgph2.length;
		if((/^[0-9-+()\s]+$/.test(orgph2) === false && orgph2.length > 0) || orgph2.charAt(0) == " " || orgph2.charAt(end - 1) == " "){
			$('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone2").focus(function(event){
				$('#phone2Valid').empty();
				 $('#phone2_pop').show();
			});
			$("#phone2").blur(function(event){
				 $('#phone2_pop').hide();
				 var orgph2 = $('#phone2').val();
				 var end = orgph2.length;
				 if((/^[0-9-+()\s]+$/.test(orgph2) === false && orgph2.length > 0) || orgph2.charAt(0) == " " || orgph2.charAt(end - 1) == " "){
					 $('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone2").focus(function(event){
							$('#phone2Valid').empty();
							 $('#phone2_pop').show();
						});
				 }else {
				}
			});
			result=false;
		}
		if(($('#phone2').val()).length > 60){
			$('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#phone2").focus(function(event){
				$('#phone2Valid').empty();
				 $('#dbPhone2_pop').show();
			});
			$("#phone2").blur(function(event){
				 $('#dbPhone2_pop').hide();
				 if(($('#phone2').val()).length > 60 ){
					 $('#phone2Valid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#phone2").focus(function(event){
							$('#phone2Valid').empty();
							 $('#dbPhone2_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var orgph3 = $('#mobile').val();
		var end = orgph3.length;
		
		return result;
	},
	checkUserPrefix : function(){
		var result = true;
			var userNamePrefix = $('#usernamePrefix').val();
			if(typeof prevPrefix != 'undefined'){
			if(prevPrefix != userNamePrefix){
				$.ajax({type: "POST",
					url:'organization.json', 
					data: 'action=check-username-prefix-availability&usernamePrefix='+userNamePrefix,
					async : false,
					success :function(obj) {
					if(obj.result.data == "true"){
						 $('#prefixValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#usernamePrefix").focus(function(event){
								$('#prefixValid').empty();
								 $('#prefix_pop').hide();
								 $('#prefixValid_pop').show();
							
							});
						 $("#usernamePrefix").blur(function(event){
							 $('#prefixValid_pop').hide();
							});
						 result = false;
					}else{
						$('#prefixValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='Super Username available!'> ");
					}
					},
			});
			}
			}
		return result;
	},
	validateOrganizationStepTwo : function(){
		var result = true;
		var orgfname = $('#fullName').val();
		var end = orgfname.length;
		if(/^[a-zA-Z ]+$/.test(orgfname) === false || orgfname.length === 0 || orgfname.charAt(0) == " " || orgfname.charAt(end - 1) == " "){
			$('#fullNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fullNameValid').empty();
				 $('#fullName_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#fullName_pop').hide();
				 var orgfname = $('#fullName').val();
				 var end = orgfname.length;
				 if(/^[a-zA-Z ]+$/.test(orgfname) === false || orgfname.length === 0 || orgfname.charAt(0) == " " || orgfname.charAt(end - 1) == " "){
					 $('#fillNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fillNameValid').empty();
							 $('#fullName_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(($('#fullName').val()).length > 100){
			$('#fillNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#fullName").focus(function(event){
				$('#fillNameValid').empty();
				 $('#dbfname_pop').show();
			});
			$("#fullName").blur(function(event){
				 $('#dbfname_pop').hide();
				 if(($('#fullName').val()).length > 100){
					 $('#fillNameValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#fullName").focus(function(event){
							$('#fillNameValid').empty();
							 $('#dbfname_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#email').val();
		if($('#email').ValidateEmailAddr(Email) === false || ($('#email').ValidateEmailAddr(Email)).length === 0){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#email").focus(function(event){
				$('#emailValid').empty();
				 $('#email_pop').show();
			});
			$("#email").blur(function(event){
				 $('#email_pop').hide();
				 if($('#email').ValidateEmailAddr($('#email').val()) === false || ($('#email').ValidateEmailAddr(Email)).length === 0){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#email").focus(function(event){
							$('#emailValid').empty();
							 $('#email_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		var Email = $('#email').val();
		if(($('#email').val()).length > 100){
			$('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#email").focus(function(event){
				$('#emailValid').empty();
				 $('#dbEmail_pop').show();
			});
			$("#email").blur(function(event){
				 $('#dbEmail_pop').hide();
				 if(($('#email').val()).length > 100){
					 $('#emailValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#email").focus(function(event){
							$('#emailValid').empty();
							 $('#dbEmail_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
var orgph3 = $('#mobile').val();
if((/^[0-9-+()\s]+$/.test(orgph3) === false|| orgph3.length === 0) || orgph3.charAt(0) == " " || orgph3.charAt(end - 1) == " "){
	$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	$("#mobile").focus(function(event){
		$('#mobileValid').empty();
		 $('#mobile_pop').show();
	});
	$("#mobile").blur(function(event){
		 $('#mobile_pop').hide();
		 var orgph3 = $('#mobile').val();
		 var end = orgph3.length;
		 if((/^[0-9-+()\s]+$/.test(orgph3) === false && orgph3.length > 0) || orgph3.charAt(0) == " " || orgph3.charAt(end - 1) == " "){
			 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#mobile").focus(function(event){
					$('#mobileValid').empty();
					 $('#mobile_pop').show();
				});
		 }else{
			
		 }
	});
	result=false;
}
if(($('#mobile').val()).length > 60){
	$('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	$("#mobile").keyup(function(event){
		$('#mobileValid').empty();
		 $('#dbmobile_pop').show();
	});
	$("#mobile").blur(function(event){
		 $('#dbmobile_pop').hide();
		 if(($('#mobile').val()).length > 60){
			 $('#mobileValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#mobile").keyup(function(event){
					$('#mobileValid').empty();
					 $('#dbmobile_pop').show();
				});
		 }else{
			
		 }
	});
	result=false;
}
var empalter = $('#alternateMobile').val();
var end = empalter.length;
if((/^[0-9-+()\s]+$/.test(empalter) === false && empalter.length > 0) || empalter.charAt(0) == " " || empalter.charAt(end - 1) == " "){
	$('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	$("#alternateMobile").focus(function(event){
		$('#alternateValid').empty();
		 $('#alternate_pop').show();
	});
	$("#alternateMobile").blur(function(event){
		 $('#alternate_pop').hide();
		 var empalter = $('#alternateMobile').val();
		 var end = empalter.length;
		 if((/^[0-9-+()\s]+$/.test(empalter) === false && empalter.length > 0) || empalter.charAt(0) == " " || empalter.charAt(end - 1) == " "){
			 $('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#alternateMobile").focus(function(event){
					$('#alternateValid').empty();
					 $('#alternate_pop').show();
				});
		 }else{
		 }
	});
	result=false;
}
if(($('#alternateMobile').val()).length > 60 ){
	$('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
	$("#alternateMobile").focus(function(event){
		$('#alternateValid').empty();
		 $('#dbalternate_pop').show();
	});
	$("#alternateMobile").blur(function(event){
		 $('#dbalternate_pop').hide();
		 if(($('#alternateMobile').val()).length > 60){
			 $('#alternateValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			 $("#alternateMobile").focus(function(event){
					$('#alternateValid').empty();
					 $('#dbalternate_pop').show();
				});
		 }else{
		 }
	});
	result=false;
}		if(($('#username').val()).length === 0){
			$('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#username").focus(function(event){
				$('#unValid').empty();
				 $('#username_pop').show();
			});
			$("#username").blur(function(event){
				 $('#username_pop').hide();
				 if(($('#username').val()).length === 0){
					 $('#uValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#username").focus(function(event){
							$('#unValid').empty();
							 $('#username_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		if(/^[a-zA-Z0-9\._]+$/.test($('#username').val()) ===  false || ($('#username').val()).length > 20){
			$('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#username").focus(function(event){
				$('#unValid').empty();
				 $('#username_pop').show();
			});
			$("#username").blur(function(event){
				 $('#username_pop').empty();
				 if(/^[a-zA-Z0-9\._]+$/.test($('#username').val()) === false || ($('#username').val()).length > 20){
					 $('#unValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#username").focus(function(event){
							$('#unValid').empty();
							 $('#username_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		
		if(($('#password').val()).length === 0){
			$('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#password").focus(function(event){
				$('#pwValid').empty();
				 $('#password_pop').show();
			});
			$("#password").blur(function(event){
				 $('#password_pop').hide();
				 if(($('#password').val()).length === 0){
					 $('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#password").focus(function(event){
							$('#pwValid').empty();
							 $('#password_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
		if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val()) === false || $('#password').val() == $('#username').val()){
			$('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
			$("#password").focus(function(event){
				$('#pwValid').empty();
				 $('#password_pop').show();
			});
			$("#password").blur(function(event){
				 $('#password_pop').hide();
				 if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#password').val()) === false || $('#password').val() == $('#username').val()){
					 $('#pwValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					 $("#password").focus(function(event){
							$('#pwValid').empty();
							 $('#password_pop').show();
						});
				 }else{
				 }
			});
			result=false;
		}
return result;

	},
	initCheckOrganizationCode: function() {
		var resFailure=false;
		var resSuccess=true;
		$("#organizationCode").blur(function(event){
			$('#code_pop').hide();
			var organizationCodeVal=$('#organizationCode').val();
			if(organizationCodeVal=="")
			{
			 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
			}
			else
				{
				var paramString='organizationCode='+organizationCodeVal+'&action=validate-organization-code';
				$.post('organization.json',paramString,
				        function(data){
		        	$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='...'> ...");
		            var delay = function() {
		            	OrganizationHandler.Succeeded(data)
		            	};
		            	setTimeout(delay, 0);
				});
				
				}
		});
},
initCheckOrgCode: function(orgId) {
	var resFailure=false;
	var resSuccess=true;
	$("#organizationCode").blur(function(event){
		$('#code_pop').hide();
		var organizationCodeVal=$('#organizationCode').val();
		if(organizationCodeVal=="")
		{
		 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>" );
		 //$('#organizationCode').focus();
		}
		else
			{
			var paramString='organizationCode='+organizationCodeVal+'&id='+orgId+'&action=validate-organization-code';
			$.post('organization.json',paramString,
			        function(data){
	        	$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='...'> ...");
	            var delay = function() {
	            	OrganizationHandler.Succeeded(data)
	            	};
	            	setTimeout(delay, 0);
			});
			
			}
	});
},
Succeeded: function(data1) {
	if (data1.result.data == "y") {
		 $('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/available.gif' alt='organizationCode available!'>");
		$("#organizationCode").focus(function(event){
			$('#organizationCodeValid').empty();
			$('#namehelp_pop').show();
			$('#code_pop').hide();
		});
		OrganizationHandler.flag = true;
	}else if (data1.result.data == "n") {
		$('#organizationCodeValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>");
		$('#code_pop').hide();
		$("#organizationCode").focus(function(event){
			$('#organizationCodeValid').empty();
			$('#namehelp_pop').hide();
			$('#code_pop').show();
		});
		OrganizationHandler.flag = false;
	}
},
	initDepartmentCreate: function() {
		$('#department-create-form-container').hide();
		$('.btn-go').click(function(){
			$('#department-create-form-container').show();
			$('#department-create-form').clearForm();
			$('#department-list').height(278);
			$('#organizationId').val($('#chooseOrganization').val());
			$('#departmentAction').val('load-department');
			var paramString = $('#department-create-form').serialize();    
			$('#department-list').html('');
			$.post('organization.json', paramString, function(obj){
				var data = obj.result.data;
				if(data.length>0) {
					var alternate=false;
					var rowstr = '';
					for(var loop=0;loop<data.length;loop=loop+1) {
						if(alternate) {
							var rowstr = '<div class="green-result-row alternate">';
						} else {
							rowstr = '<div class="green-result-row">';
						}
						alternate = !alternate;
						rowstr = rowstr + '<div id="row-'+data[loop].id+'">'+
						'<div class="green-result-col-1">'+
						'<div class="result-title" ><span class="property" style="font-size: 13px;">Department Code: </span><span id="dCode-'+data[loop].id+'">' +data[loop].code+ '</span></div>' +
						'<span class="property">Name: </span><span id="dName-'+data[loop].id+'" class="property-value">' +data[loop].name+ '</span>' +							
						'<div class="result-body">' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-2">'+
						'<div class="result-body">' +
						'<span class="property">Description: </span><span id="dDescription-'+data[loop].id+'" class="property-value">' +data[loop].description+ '</span>' +
						'</div>' +
						'</div>' +
						'<div class="green-result-col-action">' + 
						'<div id="department-type-'+data[loop].id+'" class="ui-btn edit-assessment-type-icon" title="Modify Department Type"></div>' +
						'</div>' +
						'</div>' +
						'</div>';
						$('#department-list').append(rowstr);
						$('#department-type-'+data[loop].id).data('editdepartmentType', data[loop]);
					};
					OrganizationHandler.initDepartmentButtons();
					$('#department-list').jScrollPane({showArrows:true});
		        }
			});
			$(this).parents('.ui-container').find('.green-results-list').height('288');
		});
		$('#action-save').click(function(){
			var thisButton = $(this)
			if($('#departmentId').val() === ''){
			$('#departmentAction').val('save-department');
			}else{
			$('#departmentAction').val('update-department');	
			}
			if($('#department-create-form').validate() === false) return;			
			var paramString = $('#department-create-form').serialize();    
			$.post('organization.json', paramString, function(obj){
				var hasDepartmentTypeCode = false; 
				var data = obj.result.data;
			    if((data==null)) {
			    	showMessage({title:'Error', msg:'Department code already exist.'});
			        return;
			       } else {
			    	   hasDepartmentTypeCode = true;
			       }
			    if(hasDepartmentTypeCode === false) return;
			  //  $('#info').hide();
			    if($('#departmentAction').val() !== 'update-department') {
					var alternate = false;
					var rowstr='';
					if(alternate) {
						 rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div id="row-'+data.id+'">' +
					'<div class="green-result-col-1">'+
					'<div class="result-title"><span class="property" style="font-size: 13px;">Department Code: </span><span  id="dCode-'+data.id+'">' +data.code+ '</span></div>' +
					'<span class="property">Name: </span><span id="dName-'+data.id+'" class="property-value">' +data.name+ '</span>' +							
					'<div class="result-body">' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">Description: </span><span id="dDescription-'+data.id+'" class="property-value">' +data.description+ '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="department-type-'+data.id+'" class="ui-btn edit-assessment-type-icon" title="Modify Department Type"></div>' +
					'</div>'+
					'</div>'+
					'</div>';
					$('#department-list').prepend(rowstr);
					$('#department-type-'+data.id).data('editdepartmentType', data);
					 $('#department-create-form').clearForm();
					$('#department-list').jScrollPane({showArrows:true});
		        }else{
					$('#row-'+data.id).html('');
				   	var alternate = false;

					var rowstr='';
					if(alternate) {
						 rowstr = '<div class="green-result-row alternate">';
					} else {
						rowstr = '<div class="green-result-row">';
					}
					alternate = !alternate;
					rowstr = rowstr + '<div id="row-'+data.id+'">' +
					'<div class="green-result-col-1">'+
					'<div class="result-title"><span class="property" style="font-size: 13px;">Department Code: </span><span  id="dCode-'+data.id+'">' +data.code+ '</span></div>' +
					'<span class="property">Name: </span><span id="dName-'+data.id+'" class="property-value">' +data.name+ '</span>' +							
					'<div class="result-body">' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-2">'+
					'<div class="result-body">' +
					'<span class="property">Description: </span><span id="dDescription-'+data.id+'" class="property-value">' +data.description+ '</span>' +
					'</div>' +
					'</div>' +
					'<div class="green-result-col-action">' + 
					'<div id="department-type-'+data.id+'" class="ui-btn edit-assessment-type-icon" title="Modify Department Type"></div>' +
					'</div>'+
					'</div>'+
					'</div>';
					$('#row-'+data.id).html(rowstr);
					$('#department-type-'+data.id).data('editdepartmentType', data);
    				$('#departmentAction').val('save-department');
    				$('#action-save').removeClass('btn-update').addClass('btn-save');
    				$('#department-create-form').clearForm();
    				$('#department-list').jScrollPane({showArrows:true});
		        }
			   
				OrganizationHandler.initDepartmentButtons();
			});
		});
		
	    $('#action-cancel').click(function() {
	    	$('#organization-list').click();
		});
		
		$('#action-clear').click(function(){
			$('#department-create-form').clearForm();
		});
		
	},
	initDepartmentButtons :  function() {
		$('.edit-assessment-type-icon').click(function() {
			var data = $(this).data('editdepartmentType');
			$('#departmentId').val(data.id);
			$('#departmentCode').val(data.code);
			$('#departmentName').val(data.name);
			$('#description').val(data.description);
			$('#departmentAction').val('update-department');
			$('#action-save').removeClass('btn-save').addClass('btn-update');
		});
	}
	
	
}