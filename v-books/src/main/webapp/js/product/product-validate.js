var ProductValidationHandler = {
		flagValidate:true,
		validateProduct : function(){
			var result=true;
			var productCategory=$('#productCategoryName').val();
			var categoryEnd=productCategory.length;
			var productBrand=$('#brand').val();
			var brandEnd=productBrand.length;
			var productModel=$('#model').val();
			var modelEnd=productModel.length;
			var productBatchNumber=$('#productBatchNumber').val();
			var productBatchNumberEdit=$('#productBatchNumberEdit').val();
			if(productBatchNumber != undefined){
				var batchNumberEnd=productBatchNumber.length;
			}
			if(productBatchNumberEdit != undefined){
				var batchNumberEndEdit=productBatchNumberEdit.length;
			}
			var costPerQuantity=$('#costPerQuantity').val();
			var regex = /^\d+$/;
			var costQtyParsed=currencyHandler.convertProductCostStrPatternToFloat(($('#costPerQuantity').val()));
			var costEnd=costPerQuantity.length;
			if((/^[a-zA-Z0-9\s]+$/.test(productCategory)==false && productCategory.length > 0) || productCategory.charAt(0) == " " || productCategory.charAt(categoryEnd - 1) == " "){
				$('#productCategoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#productCategoryName").focus(function(event){
					$('#productCategoryValid').empty();
					 $('#productCategory_pop').show();
				});
				$("#productCategoryName").blur(function(event){
					 $('#productCategory_pop').empty();
					    var productCategory=$('#productCategoryName').val();
						var categoryEnd=productCategory.length;
					 if((/^[a-zA-Z0-9\s]+$/.test(productCategory)==false && productCategory.length > 0) || productCategory.charAt(0) == " " || productCategory.charAt(categoryEnd - 1) == " "){
						 $('#productCategoryValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $("#productCategoryName").focus(function(event){
								$('#productCategoryValid').empty();
								 $('#productCategory_pop').show();
							});
					 }else {
					}
				});
				result=false;
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#brand').val()) == false || ($('#brand').val()).length == 0 || productBrand.charAt(0) == " " || productBrand.charAt(brandEnd - 1) == " "){
				$('#productBrandValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#brand").focus(function(event){
					$('#productBrandValid').empty();
					 $('#productBrand_pop').show();
				});
				$("#brand").blur(function(event){
					 $('#productBrand_pop').empty();
					 if(/^[a-zA-Z0-9\s]+$/.test($('#brand').val()) == false || ($('#brand').val()).length == 0 || productBrand.charAt(0) == " " || productBrand.charAt(brandEnd - 1) == " "){
						 $('#productBrandValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#productBrand_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			if(/^[a-zA-Z0-9\s]+$/.test($('#model').val()) == false || ($('#model').val()).length == 0 || productModel.charAt(0) == " " || productModel.charAt(modelEnd - 1) == " "){
				$('#productModelValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#model").focus(function(event){
					$('#productModelValid').empty();
					 $('#productModel_pop').show();
				});
				$("#model").blur(function(event){
					 $('#productModel_pop').empty();
					 if(/^[a-zA-Z0-9\s]+$/.test($('#model').val()) == false || ($('#model').val()).length == 0 || productModel.charAt(0) == " " || productModel.charAt(modelEnd - 1) == " "){
						 $('#productModelValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						 $('#productModel_pop').show();
						 $('#productName_pop').show();
					 }else{
						// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
					 }
				});
				result=false;
			}
			if($('#productBatchNumber').val() != undefined){
				if(/^[a-zA-Z0-9_#\s]+$/.test($('#productBatchNumber').val()) == false || ($('#productBatchNumber').val()).length == 0 || ($('#model').val()).length == 0 || productBatchNumber.charAt(0) == " " || productBatchNumber.charAt(batchNumberEnd - 1) == " "){
					$('#batchNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#productBatchNumber").focus(function(event){
						$('#batchNumberValid').empty();
						 $('#batchNumber_pop').show();
					});
					$("#productBatchNumber").blur(function(event){
						 $('#batchNumber_pop').empty();
						 if(/^[a-zA-Z0-9\s]+$/.test($('#productBatchNumber').val())==false || ($('#productBatchNumber').val()).length == 0 || ($('#model').val()).length == 0 || productBatchNumber.charAt(0) == " " || productBatchNumber.charAt(batchNumberEnd - 1) == " "){
							 $('#batchNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $('#batchNumber_pop').show();
						 }else{
							// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
			}
			if($('#productBatchNumberEdit').val() != undefined){
				if(/^[a-zA-Z0-9_#\s]+$/.test($('#productBatchNumberEdit').val()) == false || ($('#productBatchNumberEdit').val()).length == 0 || productBatchNumberEdit.charAt(0) == " " || productBatchNumberEdit.charAt(batchNumberEndEdit - 1) == " "){
					$('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					$("#productBatchNumberEdit").focus(function(event){
						$('#batchNumberValidEdit').empty();
						 $('#batchNumber_popEdit').show();
					});
					$("#productBatchNumberEdit").blur(function(event){
						 $('#batchNumber_pop').empty();
						 if(/^[a-zA-Z0-9\s]+$/.test($('#productBatchNumberEdit').val())==false || ($('#productBatchNumberEdit').val()).length == 0 ||  productBatchNumberEdit.charAt(0) == " " || productBatchNumberEdit.charAt(batchNumberEndEdit - 1) == " "){
							 $('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							 $('#batchNumber_popEdit').show();
						 }else{
							// $('#cityValid').html("<img src='"+THEMES_URL+"images/available.gif' alt=''>");
						 }
					});
					result=false;
				}
			}
			if(ProductValidationHandler.flagValidate === false){
				$('#productCostIsValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				$("#costPerQuantity").focus(function(event){
					$('#productCostIsValid').empty();
					 $('#productCost_pop').show();
				});
				$("#costPerQuantity").blur(function(event){
					 $('#productCost_pop').hide();
					 $('#productCostIsValid').show();
				});
				result=false;
			}
			return result;
		},
		initAddButtons: function () {
			var regex = /^\d+$/;
			$("#brand").focus(function(event){
				$('#productBrandValid').empty();
				 $('#productBrand_pop').show();
			});
			$('#brand').blur(function (){
				var productBrand=$('#brand').val();
				var brandEnd=productBrand.length;
				$('#productBrand_pop').hide();
				if(/^[a-zA-Z0-9\s]+$/.test($('#brand').val()) == false || productBrand.charAt(0) == " " || productBrand.charAt(brandEnd - 1) == " "){
					$('#productName_pop').show();
					$('#productName1').val('');
					$('#productBrandValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
				}else{
					$('#productName_pop').hide();
					var productModelName = $('#model').val();
					var productBrandName = $(this).val();
			        var productName = (productBrandName) + ' ' + (productModelName);
			        $('#productName1').val('');
			        $('#productName1').val(productName);
			        $('#productNameValid').empty();
					$('#productBrandValid').empty();
				}
		      });
			$('#costPerQuantity').blur(function (){
				var convertedCostQty = currencyHandler.convertProductCostStrPatternToFloat(($('#costPerQuantity').val()));
				var cquantity=$('#costPerQuantity').val();
				var costEnd=cquantity.length;
				if(convertedCostQty=== false){
					ProductValidationHandler.flagValidate = false;
					var commaSaperatedCost=currencyHandler.convertStringMillionPatternToFloat(($('#costPerQuantity').val()));
					var costQty = currencyHandler.convertProductCostStrPatternToFloat(commaSaperatedCost.toString());
					if(costQty === false){
						$('#productCost_pop').hide();
						$('#productCostIsValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					}else{
						 $('#productCost_pop').hide();
							if(costQty.toString().length > 10 || cquantity.charAt(0) == " " || cquantity.charAt(costEnd - 1) == " "){
								$('#productCostIsValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
							}else{
								ProductValidationHandler.flagValidate = true;
								var commaSaperated=currencyHandler.convertFloatToStringPattern(costQty.toFixed(2));
								$('#costPerQuantity').val(commaSaperated);
								$('#productCostIsValid').empty();
							}
					}
				}else{
					 $('#productCost_pop').hide();
					if(convertedCostQty.toString().length > 10 || cquantity.charAt(0) == " " || cquantity.charAt(costEnd - 1) == " "){
						$('#productCostIsValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					}else{
						ProductValidationHandler.flagValidate = true;
						var commaSaperated=currencyHandler.convertFloatToStringPattern(convertedCostQty.toFixed(2));
						$('#costPerQuantity').val(commaSaperated);
						$('#productCostIsValid').empty();
					}
				}
		      });
			$('#costPerQuantity').focus(function(event){
				$('#productCostIsValid').empty();
				 $('#productCost_pop').show();
			});
			$("#model").focus(function(event){
				$('#productCostValid').hide();
				 $('#productModel_pop').show();
			});
			$('#model').blur(function (){
				var productModel=$(this).val();
				var modelEnd=productModel.length;
				$('#productModel_pop').hide();
				if(/^[a-zA-Z0-9\s]+$/.test($(this).val()) == false || productModel.charAt(0) == " " || productModel.charAt(modelEnd - 1) == " "){
					$('#productName_pop').show();
					$('#productName1').val('');
					$('#productModelValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
					/*$(this).focus(function(){
						$('#productModelValid').hide();
				});*/
				}else{
				    $('#productModelValid').empty();
				}
				if(/^[a-zA-Z0-9\s]+$/.test($('#brand').val()) == false || /^[a-zA-Z0-9\s]+$/.test($(this).val()) == false){
				}else{
					$('#productName_pop').hide();
					var productModelName = $(this).val();
					var productBrandName = $("#brand").val();
			        var productName = (productBrandName) + ' ' + ($(this).val());
			        $('#productName1').val('');
			        $('#productName1').val(productName);
			        $('#productNameValid').empty();
				}
		      });
			if($('#productBatchNumber').val() != undefined){
				$('#productBatchNumber').blur(function(){
					$('#batchNumber_pop').hide();
					var batchNumber = $('#productBatchNumber').val();
					if(/^[a-zA-Z0-9_#\s]+$/.test(batchNumber) == false || batchNumber.toString().length === 0){
						$('#batchNumberValid').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						$('#batchNumber_pop').hide();
					}else{
						var validBatchNumber=$('#productBatchNumber').val();
						var validProductName=$('#productName1').val();
						var paramString='productName='+validProductName+'&batchNumber='+validBatchNumber+'&action=validate-productName';
						$.post('product.json',paramString,
						    function(data){
				        	$('#batchNumberValid').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking productName...'> Checking...");
				        	$('#batchNumber_pop').hide();
				               var delay = function() {
				            	ProductHandler.AjaxSucceeded(data);
				            	};
				            	setTimeout(delay, 0);
						});
					}
				});
				$('#productBatchNumber').focus(function(){
					$('#batchNumberValid').empty();
					$('#batchNumber_pop').show();
				});
			}
			if($('#productBatchNumberEdit').val() != undefined){
				$('#productBatchNumberEdit').blur(function(){
					$('#batchNumber_popEdit').hide();
					var batchNumber = $('#productBatchNumberEdit').val();
					if(/^[a-zA-Z0-9_#\s]+$/.test(batchNumber) == false || batchNumber.toString().length === 0){
						$('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/taken.gif' alt=''>");
						$('#batchNumber_popEdit').hide();
					}else{
						var validBatchNumber=$('#productBatchNumberEdit').val();
						var validProductName=$('#productName1').val();
						var id=$('#idPrd').val();
						var paramString='productName='+validProductName+'&batchNumber='+validBatchNumber+'&id='+id+'&action=validate-edit-productName';
						$.post('product.json',paramString,
						    function(data){
							$('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/waiting.gif' alt='Checking productName...'> Checking...");
				        	$('#batchNumber_popEdit').hide();
							var res=data.result.data;
							if(res == 'y'){
								$('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/available.gif' alt='Username available!'>");
							}else{
								 $('#batchNumberValidEdit').html("<img src='"+THEMES_URL+"images/taken.gif' alt='Code already taken!'>ProductName with Batch Number Alreday Exists!");
							}
						});
					}
				});
				$('#productBatchNumberEdit').focus(function(){
					$('#batchNumberValid').empty();
					$('#batchNumber_pop').show();
				});
			}
			
			$('#product-list').click(function(){
				$('.page-selection').animate( { width:"183px"}, 0,function(){});
				$('.page-link-strip').show();
				$('.module-title').show();
				$('.page-container').animate( { width:"702px"}, 0);
				var thisTheme = PageHandler.theme;
				$('.page-selection-expand').append(pageSelctionButton.attr('src', thisTheme+'/button-left.jpg'));
				pageSelctionButton.attr('src', thisTheme+'/button-left.jpg');
		$('.product-page-container').load('product/product_search.jsp');
		  ProductHandler.initSearchProductListOnLoad();
		});
	},
		
};


