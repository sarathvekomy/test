<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
	
</head>
<body onload='document.f.j_username.focus();'>
	<div style="position: relative; top: 0px;">
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
    
    <div class="filler"></div>
    
    <div class="tsb-container">
        <!--START OF MAIN CONTENT-->
        <div class="main-container">
        	<div style="margin:200px auto;width: 350px;">
				<div class="dashboard-page-container">
					<div id="login-page" class="ui-container login-page">
						<div class="widget-title"><div style="float: left;">Please Login</div></div>
						<div id="login-page-content" class="ui-content widget-content">
							<div style="height:20px;color:red;font:bold 13px arial;text-align: center;">&nbsp;${SPRING_SECURITY_LAST_EXCEPTION.message}</div>
							<form id="login-form" name='f' action='<%=request.getContextPath() %>/j_spring_security_check' method='POST' autocomplete="off">
									<div class="fieldset" style="height: 60px;">
										<div class="form-row">
											<div class="label">Username: </div>
											<div class="input-field">
												<input type='text' name='j_username' autocomplete="off" autofocus/>
											</div>
										</div>
										<div class="form-row">
											<div class="label">Password: </div>
											<div class="input-field">
												<input type='password' name='j_password' autocomplete="off"/>
											</div>
										</div>
							  		</div>
							  	<div style="float:right;width: 185px; ">
							  		<input name="submit" type="submit" value="Login" style="width:65px; padding:4px; margin-right:10px; background:#59caf5; color:#fff; border:1px solid #fff; border-radius:5px; cursor:pointer;"/>
							  		<input name="reset" type="reset" value="Reset" style="width:65px;padding:4px;background:#59caf5; color:#fff;border-radius:5px;border:1px solid #fff;cursor:pointer;"/>
							  	</div>
							  	<div class="clearfloat"></div>							  	
						  	</form>
						</div>
					</div>					
		  		</div>
		  		<div class="clearfloat"></div>
		  	</div>
        </div>
    </div>
</div>    
</body>
</html>