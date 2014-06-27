<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProduct"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ListIterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currencyFormat = user.getOrganization().getCurrencyFormat();
	VbProduct vbProduct = null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		ProductDao productDao = (ProductDao) hibernateContext.getBean("productDao");
		if (productDao != null) {
			int id = Integer.parseInt(request.getParameter("id"));
			vbProduct = productDao.getProduct(id , user.getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="product-add-form-container" title="Create Product">
	<div class="ui-content form-panel full-content">
			<form id="product-edit-form" style="height: 280px;">
				 <div class="fieldset-row" style="margin-top: 10px;">
					<div class="fieldset" style="height: 120px;">
					<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CATEGORY_LABEL)%>
							</div>
							<%
							if(vbProduct.getProductCategory()==null){
							%>
							<div class="input-field">
								<input name="productCategory" id="productCategoryName" value="">
							</div>
							<span id="productCategoryValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productCategory_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Product Category can contain letters, numbers and space.</p></div>
                            </div> 
							<%
							}else{
							%>
							<div class="input-field">
								<input name="productCategory" id="productCategoryName" value="<%=vbProduct.getProductCategory()%>">
							</div>
							<span id="productCategoryValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productCategory_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p style="display: inline;"> Product Category can contain letters, numbers and space.</p></div>
                            </div> 
							<%
							}
							%>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BRAND_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="brand" id="brand" value="<%=vbProduct.getBrand()%>">
							</div>
							<span id="productBrandValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productBrand_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;">Product Brand can contain letters, numbers and space.</div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_MODEL_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="model" id="model" value="<%=vbProduct.getModel()%>">
							</div>
							<span id="productModelValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productModel_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p> Product Model can contain letters, numbers and space.</p></div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%>
							</div>
							<div class="input-field" id="pName">
								<input readonly="readonly" name="productName"id="productName1" style="border: none; background-color:inherit;" value="<%=vbProduct.getProductName()%>">
							</div>
							<span id="productNameValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						    <div id="productName_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p>Please Enter Valid Product Brand And Model</p></div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%>
							</div>
							<div class="input-field">
								<input class="mandatory" name="batchNumber" id="productBatchNumber" value="<%=vbProduct.getBatchNumber()%>">
							</div>
							<span id="batchNumberValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="batchNumber_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p> Product Batch Number can contain letters, numbers and space and special character like _,#.</p></div>
                            </div> 
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_COST_PER_QUANTITY_LABEL)%> (<%=currencyFormat%>)</div>
							<div class="input-field">
								<input class="mandatory" name="costPerQuantity" id="costPerQuantity" value="<%=StringUtil.currencyFormat(vbProduct.getCostPerQuantity())%>">
							</div>
							<span id="productCostValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
							 <div id="productCost_pop" class="helppop" style="display: block;" aria-hidden="false">
                                 <div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; width: 300px;"><p> Product Cost can contains only number and should not exceed 10 digits.</p></div>
                            </div> 
						</div>
						 <div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.PRODUCT_DESCRIPTION_LABEL)%></div>
							<div class="input-field">
								<textarea rows="3" cols="40" name="description" id="description"
									style="resize: none;"><%=vbProduct.getDescription()%></textarea>
							</div>
						</div> 
						<input name="action" value="edit-product" type="hidden" id="productAction">
			            <input name="productId" value="<%=vbProduct.getId()%>" type="hidden" id="productId">
				</div> 
			</div>
				<div class="fieldset-row"></div>
			</form>
	</div>
</div>
<script type="text/javascript">
ProductHandler.initAddButtons();
$('.helppop').hide();
</script>

