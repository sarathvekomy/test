<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currencyFormat = user.getOrganization().getCurrencyFormat();
%>
<div id="product-add-form-container" title="Create Product">
	<div class="ui-content form-panel full-content">
			<form id="product-form" style="height: 280px;">
				 <div class="fieldset-row" style="margin-top: 10px;">
					<div class="fieldset" style="height: 120px;">
					<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CATEGORY_LABEL)%>
							</div>
							<div class="input-field">
								<input name="productCategory" id="productCategoryName">
							</div>
							<span id="productCategoryValid" class="productValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productCategory_pop" class="helppop" style="display: block; " aria-hidden="false;">
                                 <div id="namehelp" class="helpctr" style="float: left;margin-left: 3px; "><p style="display: inline; width:100px;margin-left:5px; "> Product Category can contain letters, numbers and space.</p></div>
                            </div> 
					</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BRAND_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="brand" id="brand">
							</div>
							<span id="productBrandValid" class="productValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productBrand_pop" class="helppop" style="display: block;" aria-hidden="false;">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 8px; width: 110px; font-size:12px;">Product Brand can contain letters, numbers and space.</div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_MODEL_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="model" id="model">
							</div>
							<span id="productModelValid" class="productValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productModel_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 5px; width: 110px;font-size:12px;"><p> Product Model can contain letters, numbers and space.</p></div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%>
							</div>
							<div class="input-field" id="pName">
								<input readonly="readonly" name="productName"id="productName1" style="border: none; background-color:inherit;">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="batchNumber" id="productBatchNumber">
							</div>
							<span id="batchNumberValid" class="productValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="batchNumber_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 5px; width: 110px;font-size:12px;"><p> Product Batch Number can contain letters, numbers and space and special character like _,#.</p></div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_COST_PER_QUANTITY_LABEL)%> (<%=currencyFormat%>)</div>
							<div class="input-field">
								<input class="mandatory" name="costPerQuantity" id="costPerQuantity">
							</div>
							<span id="productCostIsValid" class="productValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productCost_pop" class="helppop" style="display: block;">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 5px; width: 110px;font-size:12px;"><p> Product Cost can contains only positive number and should not exceed 10 digits.</p></div>
                            </div> 
						</div>
						 <div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_DESCRIPTION_LABEL)%></div>
							<div class="input-field">
								<textarea rows="3" cols="40" name="description" id="description"
									style="resize: none;"></textarea>
							</div>
						</div> 
					</div>
						<div id="page-buttons" class="page-buttons">
							<input name="action" value="save-product" type="hidden"
								id="productAction">
					   </div>
				</div> 
				<div class="fieldset-row"></div>
			</form>
	</div>
</div>
<script type="text/javascript">
ProductValidationHandler.initAddButtons();
$(document).ready(function(){
	 $('.helppop').hide();
});
</script>