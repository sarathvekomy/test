<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.security.LoginDao"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<script type="text/javascript" src="js/dashboard/dashboard.js"></script>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	String userName = "";
	String studentName = "";
	if(null!=user.getVbEmployee()) {
		userName = user.getVbEmployee().getFirstName()+" "+user.getVbEmployee().getLastName();
	}
%>					
          <div id="profile-container1" >
			<div class="profile-div">
				<!--<div class="profile-name">
					<%=userName %>
				</div>-->
				
				<div class="clearfloat"></div>
				<div class="filler" style="height: 30px"> </div>
						<div id="change-password-container" class="ui-content profile-container" >
							<div class="profile-title" class="ui-profile-header">
								<div style="float: left;">Change Password</div><div class="exp-coll expand-icon "></div>
							</div>
					   <div class="ui-content profile-content" id="images-container" style="width: 658px;height: 210px;">
					   <div style="width: 660px;height:220px;overflow: auto;position: absolute;">
							
						<form id="change-password-form">
						<div class="fieldset-row" style="height: 60px; margin-top: 20px;">
							    <div class="form-row">
									<div class="label">Old Password: </div>
									 <div class="input-field"><input type="password" name="oldPassWord" id="oldPassWord" autocomplete="off"></div>
								</div>
							
								<div class="separator" style="height: 60px;"></div>
       							<div class="fieldset-row" style="height: 60px;">
								<div class="fieldset" style="height: 40px;">
									<div class="form-row">
											 <div class="label">New Password: </div>
											 <div class="input-field"><input type="password" name="newPassWord" id="newPassWord" ></div>
									</div>
									<div class="form-row">
											 <div class="label">Confirm PassWord: </div>
											 <div class="input-field"><input type="password" name="confirmPassWord" id="confirmPassWord" ></div>
									</div>

										     <div class="form-row">
											<input name="action" value="change_password" type="hidden" id="changePasswordAction">
										    </div>							
            					  </div>
								</div>
						 </div>
						 
						  <%
				    ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
				            .getSession().getServletContext());
					LoginDao loginDao = (LoginDao) hibernateContext.getBean("loginDao");
					///loginDao.getOldPassword(user.getName());
					
    						%>
							</form>	
							<div id="error-message"></div>
								<div id="search-buttons" class="search-buttons" style="width:265px !important; margin-top:50px;">
									<div id="action-save" class="ui-btn btn-save" style="width: 65px;">Save</div>
									<div id="action-clear" class="ui-btn btn-clear" style="width: 65px;">Clear</div>
									<div id="action-cancel" class="ui-btn btn-cancel" style="width: 65px;">Cancel</div>
								</div>
								<center><span id="messageId" style="color:red"></span></center>
						</div>
						</div>
						</div>
						 </div>
						 </div>
			 
			<script type="text/javascript">
				$(document).ready(function() {
					ProfileHandler.load();
					ProfileHandler.initProfileMain();
					ProfileHandler.initChangePassword('<%=user.getName() %>');
					
				});
				
			</script>

