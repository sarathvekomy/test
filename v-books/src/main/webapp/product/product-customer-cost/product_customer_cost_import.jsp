<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

  <div id="product-add-form-container" title="Create Product">
		<div class="green-title-bar">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title"></div>
			</div>
		</div>
	<div class="ui-content form-panel full-content">
		<form id="product-customer-cost-upload-form" action="product/product-customer-cost/product-customer-cost-import-submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload" id="productCostImp">
				 </div>
			</div>
			<input id="upload" type="submit" value="Upload" class="btn-upload">
		</form>
		<%
		HttpSession importsession = request.getSession();
		if(importsession.getAttribute("failureMsg") != null){
			%>
			
			<div style="height:20px;color:#3FA8FB; margin-top:50px; font:bold 18px arial;text-align: center;word-wrap:break-word;">&nbsp;<% out.println(importsession.getAttribute("failureMsg")); %></div>
			<%
		}
		importsession.removeAttribute("failureMsg");
		%>
	</div>
  </div>
  
  <script>
  $(document).ready(function() {
	  //ProductHandler.showPageSelection();
	  ProductCustomerCostHandler.importProductCost();
	});
 
  </script>