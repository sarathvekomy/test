<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>V-Books</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="robots" content="index, follow"/>
	<meta name="keywords" content="Nirvahak, Organization, Management, TSB, Corporation"/>
	<meta name="description" content="TSB Corporation! - Skool Manager version 1.0"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL%>css/site.css" type="text/css"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL%>css/default/default.css" type="text/css"/>
	<script type="text/javascript" src="js/lib/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/lib/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="js/common/common.js"></script>
</head>
<body onload='document.f.j_username.focus();'>
	<div style="position: relative; top: -10px;">
    <!--TSB HEADER START-->
    <div class="header">
        <div class="header-container">
            
            <div class="header-left">
            	<a href="index.jsp"><div class="header-logo"><img  src="<%=OrganizationUtils.THEMES_URL%>images/default/vekomy-logo.png" alt="logo" border="0"/></div></a>
            	<%-- <div class="header-caption"><img  src="<%=OrganizationUtils.THEMES_URL%>images/default/caption.png" alt="A Complete Organization Management Solution"/></div> --%>
            </div>
            <div class="header-right">
                <div class="header-right-top">
                </div>
                <div class="header-right-bottom">
                    <div class="top-header-links">                
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@page import="com.vekomy.vbooks.security.User"%>
	<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
	<%@page import="org.springframework.context.ApplicationContext"%>
    <%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
    <%@page import="com.vekomy.vbooks.security.LoginDao"%>
    <div class="filler" style="height:128px;"></div>
    <span style="color:#fff; margin-left:494px;">Login Success,As this your first time login please change your password</span>
    <%
    User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request
            .getSession().getServletContext());
	LoginDao loginDao = (LoginDao) hibernateContext.getBean("loginDao");
	loginDao.isFirstTimeLogin(user.getName());
	String password=null;
	String failure=null;
	if(request.getParameter("newPassword")!=null && request.getParameter("confirmPassword")!=null){
	 password=request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))?request.getParameter("newPassword"):"";
	}
	loginDao.comparePasswords(user.getName(),request.getParameter("oldPassword"));
	
	session.setAttribute("firstTimeLogin","true");
	if(request.getParameter("oldPassword")!=null && password!=null){
		if(password.length()<=0 ){
			failure="Passwords  are Empty or Mismatch";	
			password=null;
		}else if(password.equals(request.getParameter("oldPassword"))){
			failure="Old password and new password should not be same";	
			password=null;
		}else if(user.getName().equals(password)){
			failure="Username and password should not match";	
			password=null;
			
		}else if(!loginDao.comparePasswords(user.getName(),request.getParameter("oldPassword"))){
			failure="Old password incorrect";	
			password=null;
		}
	}
	if(password!=null){
		loginDao.changePassword(password,user.getName());
		response.sendRedirect(request.getContextPath()+"/index.jsp");
		
	}else{
    %>
    <div class="tsb-container">
        <!--START OF MAIN CONTENT-->
        <div class="main-container">
        	<div style="margin: 0 auto;width: 200px;">
				<div class="dashboard-page-container">
					<div id="login-page" class="ui-container login-page">
						<div class="widget-title" style="width:348px"><div style="float: left;color:yellow;width:348px">Change Password</div></div>
						<div id="login-page-content" class="ui-content widget-content" style="height:150px;width:358px;">
							<div style="height:20px;color:red;font:bold 14px arial;text-align: center;">&nbsp;<%if(failure!=null) out.print(failure); %></div>
							<form id="change-password-form" action="change_password.jsp" method='POST'>
									<div class="fieldset" style="height: 100px;">
									   <div class="form-row">
											<div class="label">Old Password:</div>
											<div class="input-field">
												<input type='password' name="oldPassword" autocomplete="off" autofocus="autofocus"/>
											</div>
										</div>
										<div class="form-row">
											<div class="label">New Password:</div>
											<div class="input-field">
												<input class = "password" type='password' name="newPassword" id="newPassword" autocomplete="off"/>
											</div>
											<span id="pValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
											<span id="pwValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
												<div id="password_pop" class="helppop" style="display: block;" aria-hidden="false">
													<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px;">
														<p>password should be 8, must contains atleast one alphanumeric, capital, small & special characters.</p>
													</div>
												</div>
										</div>
										<div class="form-row">
											<div class="label">Confirm Password:</div>
											<div class="input-field">
												<input type='password' name="confirmPassword" />
											</div>
											
										</div>
							  		</div>
							  	<div style="margin: 0 auto;width: 260px;">
							  		<input name="submit" id="action-save"  type="submit" value="Save" style="width:80px;"/>
							  		<input name="reset" type="reset" value="Reset" style="width:80px;"/>
							  		<input name="cancel" id="action-cancel"type="button" value="Cancel" style="width:80px;"/>
							  	</div>
							  	<div class="clearfloat"></div>	
							  	<input name="action" value="change_password" type="hidden" id="changePasswordAction">						  	
						  	</form>
						  	<%} %>
						</div>
					</div>					
		  		</div>
		  		  <script type="text/javascript">
		  			$('.helppop').hide();
		  		 	$("#newPassword").blur(function(){
					 if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#newPassword').val()) == false){
						 $('#pwValid').html("<img src='images/taken.gif' alt=''>");
						 $("#newPassword").focus();
						 $("#newPassword").click(function(){
							 $("#password_pop").show();
							 $('#pwValid').empty();
						 });
						 $("#newPassword").blur(function(){ 
							 $("#password_pop").hide();
							 if(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_@#-$%^&+=]).*$/.test($('#newPassword').val()) == false){
								 $('#pwValid').html("<img src='images/taken.gif' alt=''>");
								 $("#newPassword").focus();
								 $("#newPassword").click(function(){
									 $("#password_pop").show();
									 $('#pwValid').empty();
								 });
							 }
						 });
					 }else{
					 }
		  		 	});
		  		 	$("#action-cancel").click(function(){
		  		 		document.location.href = 'login.jsp';
		  		 	});
				  </script> 
		  		<div class="clearfloat"></div>
		  	</div>
        </div>
    </div>
  
   </div>  
    
</body>
</html>
