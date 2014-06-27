<%@page import="com.vekomy.vbooks.spring.action.BaseAction"%>
<%@page import="com.vekomy.vbooks.product.dao.ProductDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbProduct"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Formatter"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%
	List<VbProduct> productList=null;
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ProductDao productDao = (ProductDao) hibernateContext
				.getBean("productDao");
		if (productDao != null) {
			productList = productDao.getProductList(user.getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div class="fieldset-row" style="margin-top: 10px;padding-left: 40px;">
  <div class="fieldset" style="height: 10px;">
	<div class="form-row" style="margin-bottom: 10px;">
<div class="label" align="center"><%=Msg.get(MsgEnum.PRODUCT_ARRIVED_QUANTITY_DATE)%>
</div>
<div class="input-field" id="quantityArrived" align="left">
<%= new SimpleDateFormat("dd-MM-yyyy").format(new Date())%>
</div>
</div>
</div>
</div>
<div id="product-arrived-quantity-form-container" title="Save Arrived Quantity" align="center" style="padding-top: 50px;">
	 <form id="addArrivedQuantity">
	 <div class="report-header" style="width: 420px; height: 30px;"> 
				<div class="report-header-column2 centered" style="width: 120px;">Product Name</div>
				<div class="report-header-column2 centered" style="width: 120px;">Batch Number</div>
				<div class="report-header-column2 centered" style="width: 130px;">Damaged Quantity</div>
	 </div> 
	<%
	int count=0;
	   for(int i=0;i<productList.size();i++){
		   if(productList.get(i).getProductName().length() > 80 || productList.get(i).getBatchNumber().length() > 80){
	 %>
	 <div class="report-body" id="damaged-product" style="width: 420px; height: 170px; overflow: hidden;">
	    <div class="productNumber">
					<div class="report-body-column2 centered sameheightcolumns" id="productName" style="height: 170px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
					<div class="report-body-column2 centered sameheightcolumns" id="batchNumber" style="height: 170px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
					<div class="report-body-column2 centered sameheightcolumns" id="available" hidden="true" style="height: 170px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getAvailableQuantity()%></div>
					<div class="report-body-column2 centered sameheightcolumns" style="height: 170px; width: 90px;"> 
					</div>
					<div class="report-body-column2 centered" style="height: 170px; width: 130px;"> 
					<div class="input-field">
								<input name="damagedQuantity" id="damagedQuantity"  class="right-aligned" value="<%=new Integer(0) %>" style="height: 16px; width: 100px;  border:1px solid #049fff;">
							    <span id="damagedQuantity-<%=count++%>" style="float: left;  position: absolute; margin-left: 5px; margin-top: 5px"></span>
					</div>
					</div>
		</div>			
	 </div>
	 <!-- <script type="text/javascript">
	 ProductHandler.equalHeight(equalHeight($(".sameheightcolumns"));
	 </script> -->
	    <%
		   }else if(productList.get(i).getProductName().length() > 50 || productList.get(i).getBatchNumber().length() > 50){
		%>
	 <div class="report-body" id="damaged-product" style="width: 420px; height: 120px; overflow: hidden;">
	    <div class="productNumber">
					<div class="report-body-column2 centered sameheight" id="productName" style="height: 120px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
					<div class="report-body-column2 centered sameheight" id="batchNumber" style="height: 120px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
					<div class="report-body-column2 centered sameheight" id="available" hidden="true" style="height: 120px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getAvailableQuantity()%></div>
					<div class="report-body-column2 centered sameheight" style="height: auto; width: 90px;"> 
					</div>
					<div class="report-body-column2 centered" style="height: 120px; width: 130px;"> 
					<div class="input-field">
								<input name="damagedQuantity" id="damagedQuantity"  class="right-aligned" value="<%=new Integer(0) %>" style="height: 16px; width: 100px;  border:1px solid #049fff;">
							    <span id="damagedQuantity-<%=count++%>" style="float: left;  position: absolute; margin-left: 5px; margin-top: 5px"></span>
					</div>
					</div>
		</div>			
	 </div>
	 <!-- <script type="text/javascript">
	 ProductHandler.equalHeight($(".sameheight"));
	 </script> -->
	 <% 	
	   }else if(productList.get(i).getProductName().length() > 30 || productList.get(i).getBatchNumber().length() > 30){
	%>
	 <div class="report-body" id="damaged-product" style="width: 420px; height: 70px; overflow: hidden;">
	    <div class="productNumber">
					<div class="report-body-column2 centered sameheight" id="productName" style="height: 70px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
					<div class="report-body-column2 centered sameheight" id="batchNumber" style="height: 70px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
					<div class="report-body-column2 centered sameheight" id="available" hidden="true" style="height: 70px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getAvailableQuantity()%></div>
					<div class="report-body-column2 centered" style="height: 70px; width: 130px;"> 
					<div class="input-field">
								<input name="damagedQuantity" id="damagedQuantity"  class="right-aligned" value="<%=new Integer(0) %>" style="height: 16px; width: 100px;  border:1px solid #049fff;">
							    <span id="damagedQuantity-<%=count++%>" style="float: left;  position: absolute; margin-left: 5px; margin-top: 5px"></span>
					</div>
					</div>
		</div>			
	 </div>
	 <!-- <script type="text/javascript">
	 ProductHandler.equalHeight($(".sameheight"));
	 </script> -->
	<% 
	 }else {
	%>
	<div class="report-body" id="damaged-product" style="width: 420px; height: 30px;">
	   <div class="productNumber">
					<div class="report-body-column2 centered" id="productName" style="height: 21px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getProductName() %></div>
					<div class="report-body-column2 centered" id="batchNumber" style="height: 21px; width: 120px; word-wrap: break-word;"><%=productList.get(i).getBatchNumber()%></div>
					<div class="report-body-column2 centered sameheight" id="available" hidden="true" style="height: auto; width: 120px; word-wrap: break-word;"><%=productList.get(i).getAvailableQuantity()%></div>
					<div class="report-body-column2 centered" style="height: 21px; width: 130px;"> 
					<div class="input-field">
								<input name="damagedQuantity" id="damagedQuantity"  class="right-aligned" value="<%=new Integer(0) %>" style="height: 16px; width: 100px;  border:1px solid #049fff;">
							    <span id="damagedQuantity-<%=count++%>" class="error-damaged-quantity" style="float: left;  position: absolute; margin-left: 5px; margin-top: 5px; "></span>
					
					</div>
					</div>
					</div>
	 </div>
	<%  
	}
	   }
	%>
	<div class="loading"></div>
	<input name="action" value="add-damaged-quantity" type="hidden"
						id="productAction">
	 </form>
	</div>