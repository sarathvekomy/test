<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>

  <div id="customer-add-form-container" title="Create Customer">
		<div class="green-title-bar">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title"></div>
			</div>
		</div>
	<div class="ui-content form-panel full-content">
		<form id="customer-upload-form"  action="customer/customer_import_submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload" id="fl_upload" style="width:200px !important;">
				 </div>
			</div>
			<input id="upload" type="submit" value="Upload" class="btn-upload" style="margin-left:190px; margin-top:10px;"/>
		</form>
		<%
		HttpSession importsession = request.getSession();
		if(importsession.getAttribute("failureMsg") != null){
			%>
			<div style="height:300px;color:#3FA8FB; margin:25px; overflow-y:scroll; border:1px solid #f6f6f6;text-align: center;word-wrap:break-word;overflow-y:scroll; ">&nbsp;<% out.println(importsession.getAttribute("failureMsg")); %></div>
			<%
		}
		importsession.removeAttribute("failureMsg");
		%>
	</div>
  </div>
  <script>
  CustomerHandler.importCustomer();
  </script>