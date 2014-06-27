<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="com.vekomy.vbooks.product.command.ProductResult"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductCustomerCostDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCustomerCostCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProductCustomerCost"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currencyFormat = user.getOrganization().getCurrencyFormat();
    VbProductCustomerCost vbProductCustomerCost=null;
    ProductCustomerCostCommand productCustomerCostCommand=new ProductCustomerCostCommand();
    VbOrganization organization=new VbOrganization();
    List<ProductResult> productList=null;
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		ProductCustomerCostDao productCustomerCostDao = (ProductCustomerCostDao) hibernateContext
				.getBean("productCustomerCostDao");
		if (productCustomerCostDao != null) {
			String businessName=request.getParameter("businessName");
			productCustomerCostCommand.setBusinessName(businessName);
			productList= productCustomerCostDao.getProductCustomerCostByBusinessName(productCustomerCostCommand , user.getOrganization());
			
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
	String bName = productCustomerCostCommand.getBusinessName();
%>
<div id="product-customer-cost-form-container" title="Product Customer Cost">
	<div class="green-title-bar">
		<div class="green-title-bar2">
		<div class="page-icon employee-search-icon"></div>
				<div class="page-title employee-search-title"></div>
		</div>
	</div>
	<div class="ui-content form-panel full-content">
		<form id="product-customer-cost-add-form" style="height: 280px;">
			 <div class="fieldset-row" style="margin-top: 0px;">
			   <div class="fieldset" style="height: 120px;">
				 <div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.PRODUCT_CUSTOMER_NAME_LABEL)%>
					</div>
					<div class="input-field">
						<input value="<%=bName%>" name="businessName" id="businessName" readonly="readonly"/>
					</div>
				  </div> 
				<div class="separator" style="height: 60px;"></div>
		        <div class="form-row"> 
			    <div id="search-results-list" class="green-results-list" style="height: 200px; width: 697px; overflow-x: hidden; overflow-y: scroll;">
				<div class="report-header" style="width: 697px; height:40px;"> 
				<div class="report-header-column2 centered" style="width: 80px;">S.No</div>
				<div class="report-header-column2 centered" style="width: 130px; ">Product Name</div>
				<div class="report-header-column2 centered" style="width: 130px; ">Batch Number</div>
				<div class="report-header-column2 centered" style="width: 130px; ">Product Cost (<%=currencyFormat%>)</div>
				<div class="report-header-column2 centered" style="width: 130px; ">Selling Price (<%=currencyFormat%>)</div>
	           </div>
	           <%
	           int count=0;
	       for(int i=0;i<productList.size();i++){
		   if(productList.get(i).getProductName().length() > 80 || productList.get(i).getBatchNumber().length() > 80){
				 %>
				 <div class="report-body" id="product-customer-cost" style="height: auto; overflow: hidden;">
				    <div id="productNumber">
				                <div class="report-body-column2 centered sameheightcolumns" id="productId " style="width: 80px;height: auto;"><%=productList.get(i).getId()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="productName" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
								<div class="report-body-column2 centered sameheightcolumns" id="batchNumber" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="costPerQuantity" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getCostPerQuantity()%></div>
								<div class="report-body-column2 centered sameheightcolumns" style="height: auto; width: 130px;"> 
					            <div class="input-field">
					            <input  class="mandatory"  name="customerCost" id="customerCost" value="<%=productList.get(i).getCost()%>" style="border: none;">
					            <span id="customerCost-"<%=count++%> style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					            </div>
					            </div>
					</div>			
				 </div>
				 <script type="text/javascript">
				 setTableGrid.equalHeight($(".sameheightcolumns"));
				 </script>
	          <%}else if(productList.get(i).getProductName().length() > 50 || productList.get(i).getBatchNumber().length() > 50){
	           %>
				 <div class="report-body" id="product-customer-cost" style="height: auto; overflow: hidden;">
				    <div id="productNumber">
				                <div class="report-body-column2 centered sameheightcolumns" id="productId " style="width: 80px;height: auto;"><%=productList.get(i).getId()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="productName" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
								<div class="report-body-column2 centered sameheightcolumns" id="batchNumber" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="costPerQuantity" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getCostPerQuantity()%></div>
								<div class="report-body-column2 centered sameheightcolumns" style="height: auto; width: 130px;"> 
					            <div class="input-field">
					            <input  class="mandatory"  name="customerCost" id="customerCost" value="<%=productList.get(i).getCost()%>" style="border: none;">
					            <span id="customerCost-"<%=count++%> style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					            </div>
					            </div>
					</div>			
				 </div>
				 <script type="text/javascript">
				 equalHeight($(".sameheightcolumns"));
				 </script>
				 <%}else if(productList.get(i).getProductName().length() > 30 || productList.get(i).getBatchNumber().length() > 30){
	           %>
				 <div class="report-body" id="product-customer-cost" style="height: auto; overflow: hidden;">
				    <div id="productNumber">
				                <div class="report-body-column2 centered sameheightcolumns" id="productId " style="width: 80px;height: auto;"><%=productList.get(i).getId()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="productName" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
								<div class="report-body-column2 centered sameheightcolumns" id="batchNumber" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
								<div class="report-body-column2 centered sameheightcolumns" id="costPerQuantity" style="height: auto; width: 130px; word-wrap: break-word;"><%=productList.get(i).getCostPerQuantity()%></div>
								<div class="report-body-column2 centered sameheightcolumns" style="height: auto; width: 130px;"> 
					            <div class="input-field">
					            <input  class="mandatory"  name="customerCost" id="customerCost" value="<%=productList.get(i).getCost()%>" style="border: none;">
					            <span id="customerCost-"<%=count++%> style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					            </div>
					            </div>
					</div>			
				 </div>
				 <script type="text/javascript">
				 equalHeight($(".sameheightcolumns"));
				 </script>
				  <%}else{
	              %>
				 <div class="report-body" id="product">
	             <div class="productNumber">
	             <div class="report-body-column2 centered" id="productId " style="width: 80px;height: 20px;"><%=productList.get(i).getId()%></div>
					<div class="report-body-column2 centered" id="productName" style="height: 20px; width: 130px;"><%=productList.get(i).getProductName()%></div>
					<div class="report-body-column2 centered" id="batchNumber" style="height: 20px; width: 130px;"><%=productList.get(i).getBatchNumber()%></div>
					<div class="report-body-column2 centered" id="costPerQuantity" style="height: 20px; width: 130px;"><%=productList.get(i).getCostPerQuantity()%></div>
					<div class="report-body-column2 centered" style="height: 20px; width: 130px;"> 
					<div class="input-field">
					<input  class="mandatory"  name="customerCost" id="customerCost" value="<%=productList.get(i).getCost()%>" style="border: none;">
					<span id="customerCost-"<%=count++%> style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
					</div>
					</div>
		       </div>
	        </div>
	        <%}
	       }
		   %>
				 </div>   
				 <div id="page-buttons" class="page-buttons">
					<input name="action" value="update-product-customer-cost" type="hidden"
						id="productCustomerCostAction">
			   </div>
			</div>
		</div></div>
		</form>
		<div id="product-preview-container" style="display: none;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 20px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next" style="display: none"></div>
			<div id="button-update-product-customer-cost" class="ui-btn btn-update"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel-product-customer-cost" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
    ProductCustomerCostHandler.initAddButtons(); 
	ProductCustomerCostHandler.load();
</script>



























