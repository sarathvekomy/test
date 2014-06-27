var MyAlertsHandler = {
		pageNumber: 1,
		numberOfPages: 1,
		count: 1,
		result:false,
		initPageLinks : function() {
			$('#my-alerts').pageLink({
				container : '.my-alerts-page-container',
				url : 'my-sales/alerts/my_alert_history.jsp'
			});
		},
		initMyAlertHistoryOnload: function() {
			$.post('alerts.json', 'action=get-myalerts-records-count', function(obj){
				var data1 = obj.result.data;
				var numberOfPages=(data1/50)+0.5;
				MyAlertsHandler.numberOfPages=Math.round(numberOfPages);
			$.post('alerts.json', 'action=get-my-alert-history&pageNumber='+MyAlertsHandler.pageNumber, function(obj) {
				data = obj.result.data;
				$('#search-results-list').html('');
				if(undefined != data) {
					var alertsGridHeader ='';
					alertsGridHeader += '<div class="report-header" style="width:848px; height: 30px;">'+
					'<div class="report-header-column2 centered" style="width:50px;">' + Msg.ALERT_SERIAL_NO + '</div>' +
					'<div class="report-header-column2 centered alertName" style="width: 150px;">' + Msg.ALERT_NAME_LABEL + '</div>' +
					'<div class="report-header-column2 centered description" style="width: 300px;">' + Msg.ALERT_DESCRIPTION_LABEL + '</div>' +
					'<div class="report-header-column2 centered from" style="width: 100px;">' + Msg.ALERT_CREATED_USER + '</div>' +
					'<div class="report-header-column2 centered from" style="width: 150px;">' + Msg.ALERT_CREATED_DATE_LABEL + '</div>' +
					'</div>'
					$('#search-results-list').append(alertsGridHeader);
					$('#search-results-list').append('<div class="grid-content" style="height:390px; width:848px;"></div>');
					for(var loop=1;loop<=data.length;loop=loop+1) {
						var alertsGridData = '<div class="ui-content report-content" id = "alert-history-grid">';
						alertsGridData+='<div class="report-body" style="width:848px; height: 40px; overflow: hidden;">';
						alertsGridData += '<div class="report-body-column2 centered sameHeight" style="width: 50px;height: 40px; word-wrap: break-word;">'+ MyAlertsHandler.count +'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight alertName" style="width: 150px;height: 40px; word-wrap: break-word;">'+data[loop-1].alertName+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight description" style="width: 300px;height: 40px; word-wrap: break-word;">'+data[loop-1].description+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight from" style="width: 100px;height: 40px; word-wrap: break-word;">'+data[loop-1].createdBy+'</div>';
						alertsGridData += '<div class="report-body-column2 centered sameHeight createdOn" style="width: 150px;height: 40px; word-wrap: break-word;">'+data[loop-1].createdOn+'</div>';
						alertsGridData += '</div>';
						alertsGridData += '</div>';
						$('.grid-content').append(alertsGridData);
						MyAlertsHandler.count=MyAlertsHandler.count+1;
					};
					var mysalesAlertsPages="";
					mysalesAlertsPages +='<div class="report-header" style="width: 698px; height: 30px;">';
		             if(MyAlertsHandler.pageNumber==1){
		            	 mysalesAlertsPages +='<div firstPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px; pointer-events: none;"  title="Click First"></div>';
		             }else{
		            	 mysalesAlertsPages +='<div firstPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  first-btn" style= "margin-left: 235px; width: 20px; border-left: none; padding-right: 20px;"  title="Click First"></div>'; 
		             }
		             if(MyAlertsHandler.pageNumber==1){
		            	 mysalesAlertsPages +='<div previousPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none; pointer-events: none;"  title="Click Previous"></div>';	 
		             }else{
		            	 mysalesAlertsPages +='<div previousPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  previous-btn" style="margin-left: 20px; width: 20px; border-left: none;"  title="Click Previous"></div>';
		             }
		             mysalesAlertsPages += '<div id="current-page-number" class="input-field"><input name="pageNumber" id="pageNumber" value="'+ MyAlertsHandler.pageNumber +'" style="width: 30px; margin-left: 40px; padding-left: 20px;"> of '+ MyAlertsHandler.numberOfPages +'</div>';
		            if(MyAlertsHandler.pageNumber==MyAlertsHandler.numberOfPages){
		            	mysalesAlertsPages +='<div nextPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Next"></div>';
		            }else{
		            	mysalesAlertsPages +='<div nextPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  next-btn" style="margin-left: 10px; width: 20px;  border-left: none"  title="Click Next"></div>';
		            }
		            if(MyAlertsHandler.pageNumber==MyAlertsHandler.numberOfPages){
		            	mysalesAlertsPages +='<div lastPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none; pointer-events: none;"  title="Click Last"></div>';
		            }else{
		            	mysalesAlertsPages +='<div lastPageNumber="'+MyAlertsHandler.pageNumber+'" class="report-header-column2  last-btn" style="margin-left: 20px; width: 20px;  border-left: none"  title="Click Last"></div>';
		            }
		            mysalesAlertsPages +='</div>';
		            $('#search-results-list').append(mysalesAlertsPages);
					$('.grid-content').jScrollPane({showArrows:true});
				} else {
					$('#search-results-list').append('<div class="green-result-row"><div class="green-result-col-1"><div class="result-title">' + obj.result.message + '</div></div></div>');
				}
				MyAlertsHandler.initGridActionButtons();
			});
		});
			/*$('#ps-exp-col').click(function() {
		    if(PageHandler.expanded) {
		    	$('#search-results-list').css("width","848px");
		    	$( '.report-header' ).css( "width", "848px" );
		    	$( '.report-body' ).css( "width", "848px" );
		    	$( '.alertName' ).css( "width", "150px" );
		    	$( '.description' ).css( "width", "300px" );
				$( '.createdBy' ).css( "width", "100px" );
				$( '.jScrollPaneContainer' ).css( "width", "848px" );
		    	
			} else {
				$('#search-results-list').css("width","830px");
				$( '.report-header' ).css( "width", "830px" );
				$( '.report-body' ).css( "width", "830px" );
				$( '.alertName' ).css( "width", "170px" );
		    	$( '.description' ).css( "width", "320px" );
				$( '.createdBy' ).css( "width", "120px" );
				$( '.jScrollPaneContainer' ).css( "width", "830px" );
			}
			setTimeout(function() {
				$('#search-results-list').jScrollPane({
					showArrows : true
				});
			}, 0);
		});*/
	},
	initGridActionButtons:function(){
		$('.last-btn').click(function() {
			$.post('alerts.json', 'action=get-my-alert-history&pageNumber='+MyAlertsHandler.pageNumber, function(obj) {
				data = obj.result.data;
				var numberOfPages=(data/50)+0.5;
				MyAlertsHandler.pageNumber=Math.round(numberOfPages);
				MyAlertsHandler.result=true;
				MyAlertsHandler.initMyAlertHistoryOnload();
				});
			});
		$('.first-btn').click(function() {
				var pageNumber = $(this).attr('firstPageNumber');
				var firstPageNumber=parseInt(pageNumber-(pageNumber-1));
				MyAlertsHandler.pageNumber=firstPageNumber;
				MyAlertsHandler.result=true;
				MyAlertsHandler.initMyAlertHistoryOnload();
			});
		$('.next-btn').click(function(){
			var pageNumber = $(this).attr('nextPageNumber');
			var nextPageNumber=parseInt(pageNumber)+1;
			MyAlertsHandler.pageNumber=nextPageNumber;
			MyAlertsHandler.result=true;
			MyAlertsHandler.initMyAlertHistoryOnload();
		});
		$('.previous-btn').click(function(){
			var pageNumber = $(this).attr('previousPageNumber');
			var prevPageNumber=pageNumber-1;
			MyAlertsHandler.pageNumber=prevPageNumber;
			MyAlertsHandler.result=true;
			MyAlertsHandler.initMyAlertHistoryOnload();
		});
		$('#pageNumber').live("keypress", function(e) {
	        if (e.keyCode == 13) {
	            var currentPageNumber=$('#pageNumber').val();
	            if(currentPageNumber > MyAlertsHandler.numberOfPages || currentPageNumber < 1 ){
	            	 showMessage({title:'Warning', msg:"please enter page number between"+ " "+ 1 +" "+ "and" + " "+ MyAlertsHandler.numberOfPages +"."});
	            	 $('#pageNumber').val(1); 
	            	 event.preventDefault();
				       return;
	            }else{
	            	MyAlertsHandler.pageNumber=currentPageNumber;
	            	MyAlertsHandler.initMyAlertHistoryOnload();
	            }
	        }
	  });
	}
};