<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>


  <div id="employee-add-form-container" title="Create employee">
		<div class="green-title-bar">
			<div class="green-title-bar2">
				<div class="page-icon student-import-icon"></div>
				<div class="page-title import-employee-title"></div>
			</div>
		</div>
	<div class="ui-content form-panel full-content">
		<form id="employee-upload-form" action="employee/employee_import_submit.jsp" method="post" enctype="multipart/form-data">
			<div class="form-row"  style="width: 420px; float:none !important;">
				 <div class="label">Choose file: </div>
				 <div class="input-field">
				 	<input type="file" name="fl_upload" style="width:200px !important;">
				 </div>
			</div>
			<input id="upload" type="submit" value="Upload" class="btn-upload" style="margin-left:65px; margin-top:10px;"/>
		</form>
		<%
		HttpSession importsession = request.getSession();
		if(importsession.getAttribute("failureMsg") != null){
			%>
			
			<div style="height:20px;color:#3FA8FB; margin-top:50px; font:bold 18px arial;text-align: center;word-wrap: break-word;">&nbsp;<% out.println(importsession.getAttribute("failureMsg")); %></div>
			<%
		}
		importsession.removeAttribute("failureMsg");
		%>
	</div>
  </div>
  <script>
  EmployeeHandler.importEmployee();
  </script>