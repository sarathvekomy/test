<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

  <div id="product-add-form-container" title="Create Product">
		<div class="green-title-bar">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title"></div>
			</div>
		</div>
	<div class="ui-content form-panel full-content">
		<form id="product-upload-form" action="product/product_import_submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload">
				 </div>
			</div>
			<input id="upload" type="submit" value="" class="btn-upload">
		</form>
	</div>
  </div>
  
  <script>
  $(document).ready(function() {
	  ProductHandler.showPageSelection();
	});
  ProductHandler.importproduct();
  </script>