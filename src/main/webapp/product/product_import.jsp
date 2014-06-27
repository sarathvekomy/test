<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

  <div id="product-add-form-container" title="Create Product">
		<div class="green-title-bar">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title" style="padding-top:5px;padding-left:10px;font-size:16px !important;"> Product</div>
			</div>
		</div>
	<div class="ui-content form-panel full-content">
		<form id="product-upload-form" action="product/product_import_submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px; float:none !important;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload" id="fileProduct" style="width:200px;">
				 </div>
			</div>
			<input id="upload" type="submit" value="Upload" class="btn-upload" style="margin-left:65px; margin-top:10px;"/>
		</form>
		<%
		HttpSession importsession = request.getSession();
		if(importsession.getAttribute("failureMsg") != null){
			%>
			
			<div style="height:60px;color:#3FA8FB; margin:25px; overflow-y:scroll; border:1px solid #f6f6f6; font:bold 18px arial;text-align: center;word-wrap:break-word;">&nbsp;<% out.println(importsession.getAttribute("failureMsg")); %></div>
			<%
		}
		importsession.removeAttribute("failureMsg");
		%>
		<div class="green-title-bar" style="margin-top:20px; margin-bottom:10px;">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title" style="padding-top:5px;padding-left:10px; font-size:16px !important;"> Arrived Quantity</div>
			</div>
		</div>
		<form id="product-arrived-quantity-upload-form" action="product/product_arrived_quantity_import_submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px;float:none !important;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload" id="fileArrivedQuantity" style="width:200px;">
				 </div>
			</div>
			<input id="upload" type="submit" value="Upload" class="btn-upload" style="margin-left:65px; margin-top:10px;"/>
		</form>
		<%
		HttpSession importsession1 = request.getSession();
		if(importsession1.getAttribute("fail") != null){
			%>
			
			<div style="height:60px;color:#3FA8FB;font:bold 18px arial;margin:25px; overflow-y:scroll; border:1px solid #f6f6f6;text-align: center;word-wrap:break-word;">&nbsp;<% out.println(importsession1.getAttribute("fail")); %></div>
			<%
		}
		importsession1.removeAttribute("fail");
		%>
	</div>
  </div>
  <script>
  $(document).ready(function() {
	  ProductHandler.showPageSelection();
	});
  ProductHandler.importproduct();
  </script>