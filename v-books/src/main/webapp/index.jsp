<%@page import="com.vekomy.vbooks.hibernate.model.VbUserSetting"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.vekomy.vbooks.spring.page.Module"%>
<%@page import="com.vekomy.vbooks.spring.page.Page"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.vekomy.vbooks.spring.page.ModuleBar"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="com.vekomy.vbooks.spring.page.SessionPage"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbOrganization"%>
<%@page import="com.vekomy.vbooks.security.LoginDao"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbEmployee"%>
<%@page import="java.util.ArrayList"%><html>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	ApplicationContext hibernateContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	LoginDao loginDao = (LoginDao) hibernateContext.getBean("loginDao");
	
	if(loginDao.isFirstTimeLogin(user.getName())){
		response.sendRedirect(request.getContextPath()+"/change_password.jsp");
	}else{
		session.setAttribute("firstTimeLogin","false");
	}
	
	String theme = request.getParameter("theme");
	if(theme!=null && !"".equals(theme)) {
		user.setTheme(theme);
	}
	
	
	if(user.getRoles()==null) {
		user.prepareRoles();
	}
	Iterator roleIterator = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
	boolean showAll = false;
	boolean siteAdmin = false;
	boolean salesExecutive = false;
	boolean groupHead = false;
	StringBuilder role = new StringBuilder();
	boolean organizationLoaded = false;
	while(roleIterator.hasNext()) {
		GrantedAuthority authority = (GrantedAuthority) roleIterator.next();
		role.append("'");
		role.append(authority.getAuthority());
		role.append("'");
		if(roleIterator.hasNext()) {
			role.append(",");
		}
		if(authority.getAuthority().equals("ROLE_SITEADMIN")) {
			siteAdmin = true;
		}
		if(authority.getAuthority().equals("ROLE_SALESEXECUTIVE")){
			salesExecutive = true;
		}
		
		if(authority.getAuthority().equals("ROLE_GROUPHEAD")) {
			groupHead = true;
		}
	}
	
	if(user.getOrganization()==null) {
		VbOrganization vbOrganization = loginDao.getEmployeeOrganization(user.getName());
		VbEmployee vbEmployee = loginDao.getEmployee(user.getName());
		if(vbEmployee!=null) {
			user.setOrganization(vbOrganization);
			organizationLoaded = true;
			user.setVbEmployee(vbEmployee);
		}
	} else {
		organizationLoaded = true;
	}
%>
<head>
	<title>V-Books</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="robots" content="index, follow"/>
	<meta name="keywords" content="Nirvahak, Organization, Management, TSB, Corporation"/>
	<meta name="description" content="TSB Corporation! - Skool Manager version 1.0"/>
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<script type="text/javascript" src="js/lib/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="js/lib/jquery-ui-1.8.10.custom.min.js"></script>
	<script type="text/javascript" src="js/common/common.js"></script>
		
	<script type="text/javascript" src="js/lib/jScrollPane.js"></script>
	<script type="text/javascript" src="js/lib/jscharts.js"></script>
	<script type="text/javascript" src="js/lib/jquery.datePicker.js"></script>
	<script type="text/javascript" src="js/lib/date.js"></script>
	
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL %>css/datePicker.css" type="text/css"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL %>css/ui-lightness/jquery-ui-1.8.10.custom.css" type="text/css"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL %>css/jScrollPane.css" type="text/css"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL %>css/site.css" type="text/css"/>
	<link rel="stylesheet" href="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getThemeCSS(user)%>" type="text/css"/>
	
	
</head>
<body>
	<div style="position: relative; top: -10px;">
    <!--TSB HEADER START-->
    <script type="text/javascript">
    	RoleHandler.roles = [<%=role%>];
    	RoleHandler.modules = <%=OrganizationUtils.getModules()%>;
    	RoleHandler.init(); 
    	PageHandler.theme = '<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"")%>';
    	ModuleHandler.moduleImage2 = '<%=OrganizationUtils.THEMES_URL%>';
    	<%
    	if(request.getParameter("module")!=null && !session.getAttribute("firstTimeLogin").equals("true")&& !user.getRoles().contains("ROLE_SITEADMIN")){
        }%>
    	var THEMES_URL='<%=OrganizationUtils.THEMES_URL%>';
	</script>  
   	<%-- <div class="logo-container"><img  src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"nirvahak-logo-1.png")%>" alt="logo" border="0" style="padding-top:20px;"/></div> --%>
    <div class="header">
        <div class="header-container">
            
            <div class="header-left">
            	<a href="index.jsp"><div class="header-logo"><img  src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"vekomy-logo.png")%>" alt="logo" border="0"/></div></a>
            	<%-- <div class="header-caption"><img  src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"caption.png")%>" alt=" "/></div> --%>
            </div>
            <div class="header-middle">
            <%if(user.getOrganization()!=null) {%>
                	<div><span id="header-organization-name" style="color: #049; font:bold 15px Arial;"><%=user.getOrganization().getName() %>,&nbsp;</span></div>
                	<div><span id="header-organization-branch" style="color: #049; font:bold 15px Arial;"><%=user.getOrganization().getBranchName() %></span></div> 
                	<%}%>
            </div>
            <div class="header-right">
                <div class="header-right-top">
                	<span id="header-login-name"><%=user.getName() %></span>&nbsp;&nbsp;<a href="logout">Logout</a></div>
                <div class="header-right-bottom">
                    <div class="top-header-links">
                        <ul>
                            <li id="nirvahak-home" ><a href="index.jsp">Home</a></li>
                            <li><a href="index.jsp?page=profile">Profile</a></li>
                            <!-- <li><a href="index.jsp?page=help">Help</a></li> -->
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    <div class="filler"></div>
    
<%
	String moduleStr = request.getParameter("module");
   if(moduleStr==null){
	   moduleStr="dashBoard";
   }
	String pageStr = request.getParameter("page");
	SessionPage sessionPage = null; 
	if(session.getAttribute("selectedPage")==null) {
		sessionPage = new SessionPage();
		session.setAttribute("selectedPage", sessionPage);
	} else {
		sessionPage = (SessionPage)session.getAttribute("selectedPage"); 
	}
	

	WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
	ModuleBar moduleBar = (ModuleBar) applicationContext.getBean("moduleBar");
	
	Module module = null;
	//if(module==null && siteAdmin){
	if(siteAdmin && !organizationLoaded){
		module = moduleBar.getModule(request, "siteadmin");
	} else if(groupHead) {
		module = moduleBar.getModule(request, "grouphead");
	} else if(salesExecutive) {
		module = moduleBar.getModule(request, "mysales");
	}else if(siteAdmin && organizationLoaded){
		module = moduleBar.getModule(request, moduleStr);
	} else if(!"siteadmin".equals(moduleStr)){
		//Do not show siteadmin module for other users.
		module = moduleBar.getModule(request, moduleStr);
	} 
	String pageUrl = null;
	String pageLinksUrl = null;
	String moduleLabel = "";
	if(moduleStr.equalsIgnoreCase("dashBoard")&&module==null){
		module=moduleBar.getDefaulModule();
	}
	if(module!=null) {
		if(pageStr==null || "".equals(pageStr)) {
			pageUrl = module.getDefaulPage().getUrl();
			pageStr = module.getDefaulPage().getName();
			pageLinksUrl = module.getDefaulPage().getLinksUrl();
		} else {
			Page page2 = module.getPage(request, pageStr); 
			if(page2!=null) {
				pageUrl = page2.getUrl();
				pageLinksUrl = page2.getLinksUrl();
			}
		}
		moduleLabel = module.getLabel();
	} else {
		moduleLabel = "DashBoard";
	}
	
	if("profile".equals(pageStr)) {
	    moduleLabel = "Profile";
	    pageUrl="dashboard/profile_main.jsp";
	} 
	if("help".equals(pageStr)) { 
	    moduleLabel = "Help";
	    pageUrl="dashboard/help_main.jsp";
	}
	if(pageUrl == null) {
		pageUrl = "";
	}
	if(pageLinksUrl == null) {
		pageLinksUrl = "";
	}
	
%>        
    <div class="tsb-container">
        <!--START OF MODULEBAR-->
        
        <%
        Module module2 = null;
        if(!salesExecutive && !groupHead) {
        	if(!siteAdmin || (siteAdmin && organizationLoaded)){ %>
        <div class="module-bar">
            <ul>
              	<%
       				Iterator iterator = moduleBar.getModuleKeyset().iterator();
       				String moduleKey = null;
       				while(iterator.hasNext()) {
       					moduleKey = (String)iterator.next();
       					//do not show siteadmin module in Module bar
       					if(!moduleKey.equals("siteadmin")&&!moduleKey.equals("dashboard") && !moduleKey.equals("organizationSettings") && !salesExecutive && !moduleKey.equals("grouphead")) {
       							module2 = (Module)moduleBar.getModule(request, moduleKey);
              	%>
              	<%-- <%=OrganizationUtils.THEMES_URL%> --%>
				<li>
					<span class="module-link" module="<%=module2.getName() %>">
					<a href="index.jsp?module=<%=moduleKey %>"><img src="images/module/<%=module2.getName() %>_1.png"  border="0" width="80" height="70"/></a>
					</span>
					<%if(iterator.hasNext()) {
						%>
						<!--<img src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"module-seperator.png")%>"  border="0" class="module-seperator"/>-->
					<%} %>
				</li>
              	<%
              			} 
       				}
       				
              	%>
			</ul>
        </div>
        <%}
	} else if(salesExecutive){
			// Hiding all modules for Sales executives except my-sales.
			module2 = (Module)moduleBar.getModule(request, "mysales");
	} else if(groupHead) {
		module2 = (Module)moduleBar.getModule(request, "grouphead");
	}
   %>
        <!--END OF MODULEBAR-->
        
        <div class="filler"></div>

        <!--START OF MAIN CONTENT-->
        <div class="main-container">
            <!--START OF LEFT CONTAINER-->
            <div class="page-selection" id="droppable1">
            	<div class="module-title-bar">
					<div class="module-title"><%=moduleLabel %></div>
					<div class="page-selection-expand">
						<img id="ps-exp-col" src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"button-left.jpg")%>"/>
					</div>
				</div>
				<!--<div class="clearfloat"></div>
				<div class="page-selection-seperator"></div>-->
                <div class="left-nav">
                    <ul>
                    	<%
                    		if(module!=null) {
                    				Iterator iterator2 = module.getPageKeyset().iterator();
                    				Page modulePage = null;
                    				String pageKey = null;
                    				while(iterator2.hasNext()) {
                    					pageKey = (String)iterator2.next();
                    					modulePage = (Page)module.getPage(request, pageKey);
                    	%>
	                        <li>
	                        	<a href="index.jsp?module=<%=module.getName() %>&page=<%=pageKey %>">
	                        		<div class="page-link">
										<div class="page-link-icon"><img src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getModuleImageUrl(user,modulePage.getIcon())%>" border="0"/></div>
										<div class="page-link-strip">
											<div class="page-link-text"><%=modulePage.getLabel() %></div>
										</div>
									</div>
								</a>
	                        </li>
                    	<%
                    				}
                    		}
                    		
                    	%>
                        <!--<li><div style="width:175px;height:50px;background:red;"><div class="icon" style="width:50px;height:50px;background:green;float:left;"></div><div style="width:125px;height:40px;background:yellow;margin-top:5px;margin-bottom:5px;float:left;"></div></div></li>
                   --></ul>
                </div>
            </div>
            <!--END OF LEFT CONTAINER-->
            
            <div class="page-filler"></div>
            
            <!--START OF RIGHT CONTAINER-->
            <div class="page-container" id="page-container" module=<%=request.getParameter("module")%> page=<%=pageStr%>>
				<%
				if(!pageLinksUrl.equals("")) { %>
				<div class="page-links">
                    <jsp:include page='<%=pageLinksUrl%>'></jsp:include>
                </div>
                <div class="pagelinks-filler">&nbsp;</div>
                <div class="page-content-filler">&nbsp;</div>
                <%} %>
                <div class="page-content" id="droppable2">
                	<%if(!pageUrl.equals("")) { %>
                    <jsp:include page='<%=pageUrl %>'></jsp:include>
                    <%} else {%>
                    <jsp:include page='dashboard/dashboard_main.jsp'>
                    	<jsp:param value="<%=showAll%>" name="showAll"/>
                    </jsp:include>
                    <%}%>
                </div>					          
            </div>
            <!--END OF RIGHT CONTAINER-->
            
        </div>
        <!--END OF MAIN CONTENT-->
        
        <div class="filler"> </div>
        
        <!--START OF TSB FOOTER-->
        <div class="footer">
        	<div class="footer-left"></div>
            <!--<div class="footer-left"><img src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"futter-left-img.png")%>" /></div>-->
            <div class="footer-middle">
            	<div class="clearfloat"></div>
            	<div class="tittle">&copy; Vekomy Technologies Pvt.Ltd 2011-2012. All rights reserved</div>
            	<div class="clearfloat"></div>
            	<div class="footer-middle-footer-div">
            		<div class="copyright footer-middle-footer-div-data">Copyright</div>
            		<div class="terms-and-conditions footer-middle-footer-div-data">Terms and Conditions</div>
            		<div class="contact-us footer-middle-footer-div-data">Contact Us</div>
            		<div class="build-count footer-middle-footer-div-data">Build: 1.0.0</div>            	
            	</div>
 					 <div class="detail-popup" id="build-detail" style="display: none;">
						    <div class="popup-bottom-pointer1"></div>
						    <div class="popup-drop-shadow"></div>
						    <div class="popup-container" id="build-detail-content">
						    	<div class="popup-head" style="padding-bottom: 5px;">
						    		<label class="popup-heading">Build Detail</label>
						    		<div onclick="DetailPopup.close()" class="popup-close"></div>
						    	</div>				
						    	<div id="build-content" style="margin-top: 60px;">		        
				                 Build: 1.0.4
				                 <div class="filler"></div>
			                     Last Modified Date:08/05/2013
			                     <div class="filler"></div>
			                     � Vekomy Technologies Pvt.Ltd 2013-2014. All rights reserved
			                     </div>
							 </div>
					</div>	
            	

            </div>
            <!--<div class="footer-right"><img src="<%=OrganizationUtils.THEMES_URL+OrganizationUtils.getImageUrl(user,"futter-rig-img.png")%>" /></div>-->
            <div class="footer-right"></div>
        </div>
        
        
        <!--END OF TSB FOOTER--><!--
        
		<div class="detail-popup" id="corporate-preview-popup" style="display: none;">
		    <div class="popup-top-pointer"></div>
		    <div class="popup-drop-shadow"></div>
		    <div class="popup-container">
		    	<div class="popup-head">
		    		<label class="popup-heading" id="corporate-preview-title"></label>
		    		<div onclick="DetailPopup.close()" class="popup-close">CLOSE</div>
		    	</div>						        
		        <div id="corporate-preview-popup-content">
		        </div>
		    </div>
			
		</div>
    -->
    
    </div>
</div> 
<jsp:include page='msg.jsp'></jsp:include> 

    <script type="text/javascript">
    $(document).ready(function() {
    	ModuleHandler.init();
    	PageHandler.initPageSelection();
    	FooterHandler.init()
    	var footerMiddle = $('.footer-middle').outerHeight();
    	$('.footer-left').outerHeight(footerMiddle);
    	$('.footer-right').outerHeight(footerMiddle);
    	//$('.footer-middle-footer-div').css('margin-top',footerMiddle);    	
    }); 
	</script>  
	
</body>
</html>
