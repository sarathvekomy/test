 
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
 <%
User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
String currencyFormat = user.getOrganization().getCurrencyFormat();
String advance = "Adv";
String advancePaid = "Cr Paid";
%> 
<script type="text/javascript">
 var Msg = {
		 	'DATE': '<%=Msg.get(MsgEnum.DATE_LABEL)%>' ,
			//Employee
		    'EMPLOYEE_USERNAME': '<%=Msg.get(MsgEnum.EMPLOYEE_USERNAME)%>' ,
		    'EMPLOYEE_EMPLOYEE_TYPE_LABEL': '<%=Msg.get(MsgEnum.EMPLOYEE_EMPLOYEE_TYPE_LABEL)%>' ,
		    'EMPLOYEE_PHONE_LABEL': '<%=Msg.get(MsgEnum.EMPLOYEE_PHONE_LABEL)%>' ,
		    'EMPLOYEE_MOTHER_NAME_LABEL': '<%=Msg.get(MsgEnum.EMPLOYEE_MOTHER_NAME_LABEL)%>' ,
			'EMPLOYEE_FATHER_NAME_LABEL': '<%=Msg.get(MsgEnum.EMPLOYEE_FATHER_NAME_LABEL)%>' ,
			'EMPLOYEE_EMPLOYEE_TYPES':'<%=Msg.get(MsgEnum.EMPLOYEE_EMPLOYEE_TYPES) %>',
			'ADDRESS_TYPE_LABEL':'<%=Msg.get(MsgEnum.ADDRESS_TYPE_LABEL) %>',
			'EMPLOYEE_LOGIN_SNO' : '<%=Msg.get(MsgEnum.EMPLOYEE_LOGIN_VIEW_SNO) %>',
			'EMPLOYEE_LOGIN_USER_NAME' : '<%=Msg.get(MsgEnum.EMPLOYEE_LOGIN_VIEW_USER_NAME) %>',
			'EMPLOYEE_LOGIN_LOGIN_TIME' : '<%=Msg.get(MsgEnum.EMPLOYEE_LOGIN_VIEW_LOGIN_TIME) %>',
			// Customer
			'CUSTOMER_ID' : '<%=Msg.get(MsgEnum.CUSTOMER_ID)%>',
			'CUSTOMER_CREDIT_OVER_DUE_DAYS' : '<%=Msg.get(MsgEnum.CREDIT_OVERDUE_DAYS)%>',
			'CUSTOMER_CREDIT_LIMIT' : '<%=Msg.get(MsgEnum.CREDIT_LIMIT)%>',
			'CREDIT_ID' : '<%=Msg.get(MsgEnum.CREDIT_ID)%>',
			'CUSTOMER_MOBILE_NUMBER' :'<%=Msg.get(MsgEnum.CUSTOMER_DETAILS_MOBILE)%>',
			'CUSTOMER_REGION' : '<%=Msg.get(MsgEnum.CUSTOMER_REGION)%>',
			'CUSTOMER_ALTERNATE_NUMBER':'<%=Msg.get(MsgEnum.CUSTOMER_DETAILS_ALTERNATE_MOBILE)%>',
			'CUSTOMER_EMAIL': '<%=Msg.get(MsgEnum.CUSTOMER_DETAILS_EMAIL)%>',
			'CUSTOMER_NAME_LABEL' : '<%=Msg.get(MsgEnum.CUSTOMER_NAME)%>',
			
			'CUSTOMER_LOCALITY' : '<%=Msg.get(MsgEnum.CUSTOMER_DETAILS_LOCALITY)%>',
			'CUSTOMER_CREDIT_LIMIT':'<%=Msg.get(MsgEnum.CREDIT_LIMIT)%>',
			'CUSTOMER_OVERDUE_DAYS':'<%=Msg.get(MsgEnum.CREDIT_OVERDUE_DAYS)%>',
			'CUSTOMER_CR_TYPE' :'<%=Msg.get(MsgEnum.CUSTOMER_CHANGE_REQUEST_TYPE)%>',
			'CUSTOMER_BUSINESS_NAME' :'<%=Msg.get(MsgEnum.CUSTOMER_BUSINESS_NAME)%>',
			'CUSTOMER_INVOICE_NAME' :'<%=Msg.get(MsgEnum.CUSTOMER_INVOICE_NAME)%>',
			
			// Product
			'PRODUCT_DESCRIPTION' : '<%=Msg.get(MsgEnum.PRODUCT_DESCRIPTION_LABEL)%>',
			'PRODUCT_BATCH_NUMBER' : '<%=Msg.get(MsgEnum.PRODUCT_BATCH_NUMBER_LABEL)%>',
			'PRODUCT_COST_PER_QUANTITY' : '<%=Msg.get(MsgEnum.PRODUCT_COST_PER_QUANTITY_LABEL)%>',
			'PRODUCT_AVAILABLE_QUANTITY' : '<%=Msg.get(MsgEnum.PRODUCT_AVAILABLE_QUANTITY_LABEL)%>',
			'PRODUCT_QUANTITY_ARRIVED' : '<%=Msg.get(MsgEnum.PRODUCT_ARRIVED_QUANTITY_LABEL)%>',
			'PRODUCT_QUANTITY_AT_WAREHOUSE' : '<%=Msg.get(MsgEnum.PRODUCT_WAREHOUSE_QUANTITY_LABEL)%>',
			'PRODUCT_TOTAL_QUANTITY' : '<%=Msg.get(MsgEnum.PRODUCT_TOTAL_QUANTITY_LABEL)%>',
			'PRODUCT_CUSTOMER_NAME' : '<%=Msg.get(MsgEnum.PRODUCT_CUSTOMER_NAME_LABEL)%>',
			'PRODUCT_PRODUCT_NAME' : '<%=Msg.get(MsgEnum.PRODUCT_PRODUCT_NAME_LABEL)%>',
			'PRODUCT_COST' : '<%=Msg.get(MsgEnum.PRODUCT_COST_LABEL)%>',
			'PRODUCT_ASSIGNED_BY' : '<%=Msg.get(MsgEnum.PRODUCT_ASSIGNED_BY_LABEL)%>',
			'PRODUCT_QUANTITY' : '<%=Msg.get(MsgEnum.PRODUCT_QUANTITY_LABEL)%>',
			'PRODUCT_QUANTITY_TYPE' : '<%=Msg.get(MsgEnum.PRODUCT_QUANTITY_TYPE_LABEL)%>',
			'PRODUCT_SALES_EXECUTIVE' : '<%=Msg.get(MsgEnum.PRODUCT_SALES_EXECUTIVE_LABEL)%>',
			'PRODUCT_CUSTOMER_COST' : '<%=Msg.get(MsgEnum.PRODUCT_CUSTOMER_COST)%>',
			'PRODUCT_SERIAL_NUMBER' : '<%=Msg.get(MsgEnum.PRODUCT_SERIAL_NUMBER_LABEL)%>',
			'PRODUCT_CREATED_DATE_LABEL' : '<%=Msg.get(MsgEnum.PRODUCT_CREATED_DATE_LABEL)%>',
			'PRODUCT_CATEGORY' : '<%=Msg.get(MsgEnum.PRODUCT_CATEGORY_LABEL)%>',
			
			// Sales
			'SALES_NUMBER' : '<%=Msg.get(MsgEnum.SALES_BOOK_SALES_NUMBER_LABLE)%>',
			'CREATED_DATE' : '<%=Msg.get(MsgEnum.SALES_BOOK_CREATED_DATE_LABLE)%>',
			//Sales in Accounts
			'SALES_EXECUTIVE_NAME_LABEL': '<%=Msg.get(MsgEnum.SALES_EXECUTIVE_NAME_LABEL)%>',
			'SALES_CREATED_DATE_LABEL': '<%=Msg.get(MsgEnum.SALES_CREATED_DATE_LABEL)%>',
			'SALES_OPENING_BALANCE_LABEL':'<%=Msg.get(MsgEnum.SALES_OPENING_BALANCE_LABEL)%>',
			'SALES_CLOSING_BALANCE_LABEL':'<%=Msg.get(MsgEnum.SALES_CLOSING_BALANCE_LABEL)%>',
			//
			'SALES_RETURNS_PRODUCT_NAME': '<%=Msg.get(MsgEnum.SALES_RETURNS_PRODUCT_NAME_LABEL)%>',
			'SALES_RETURN_QUANTITY':'<%=Msg.get(MsgEnum.SALES_RETURNS_RETURN_QUANTITY)%>',
			'SALES_RETURNS_COST':'<%=Msg.get(MsgEnum.SALES_RETURNS_COST)%>',
			'SALES_RETURNS_TOTAL_COST':'<%=Msg.get(MsgEnum.SALES_RETURNS_TOTAL_COST)%>',
			'SALES_RETURNS_EXECUTIVE_NAME':'<%=Msg.get(MsgEnum.SALES_RETURNS_EXECUTIVE_NAME)%>',
			'SALES_RETURNS_CREATED_DATE' :'<%=Msg.get(MsgEnum.SALES_RETURNS_CREATED_DATE)%>',
			'STOCK_RETURNS_BUSINESS_NAME_LABEL':'<%=Msg.get(MsgEnum.STOCK_RETURNS_BUSINESS_NAME_LABEL)%>',
			'SALES_RETURNS_IS_APPROVED' :'<%=Msg.get(MsgEnum.SALES_RETURNS_IS_APPROVED)%>',
			//Delivery Note
			'DELIVERY_NOTE_BUSINESS_NAME_LABEL':'<%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%>',
			'DELIVERY_NOTE_INVOICE_NAME_LABEL':'<%=Msg.get(MsgEnum.DELIVERY_NOTE_INVOICE_NAME_LABEL)%>',
			'DELIVERY_NOTE_CREATED_DATE_LABEL':'<%=Msg.get(MsgEnum.DELIVERY_NOTE_CREATED_DATE_LABEL)%>',
			'DELIVERY_NOTE_BALANCE_LABEL':'<%=Msg.get(MsgEnum.DELIVERY_NOTE_BALANCE_LABEL)%>',
			//Accounts
			'ACCOUNTS_SALES_EXECUTIVE' : '<%=Msg.get(MsgEnum.ACCOUNTS_SALES_EXECUTIVE_LABEL)%>',
			'ACCOUNTS_OPENING_BALANCE' : '<%=Msg.get(MsgEnum.ACCOUNTS_OPENING_BALANCE_LABEL)%>',
			'ACCOUNTS_ADVANCE' : '<%=Msg.get(MsgEnum.ACCOUNTS_ADVANCE_LABEL)%>',
			'ACCOUNTS_PRODUCT_NAME' : '<%=Msg.get(MsgEnum.ACCOUNTS_PRODUCT_NAME_LABEL)%>',
			'ACCOUNTS_BATCH_NUMBER' : '<%=Msg.get(MsgEnum.ACCOUNTS_BATCH_NUMBER_LABEL)%>',
			'ACCOUNTS_ALLOTMENT' : '<%=Msg.get(MsgEnum.ACCOUNTS_ALLOTMENT_LABEL)%>',
			'ACCOUNTS_PREVIOUS_CLOSING_STOCK' : '<%=Msg.get(MsgEnum.ACCOUNTS_PREVIOUS_CLOSING_STOCK_LABEL)%>',
			'ACCOUNTS_CLOSING_STOCK' : '<%=Msg.get(MsgEnum.ACCOUNTS_CLOSING_STOCK_LABEL)%>',
			'ACCOUNTS_OPENING_STOCK' : '<%=Msg.get(MsgEnum.ACCOUNTS_OPENING_STOCK_LABEL)%>',
			'ACCOUNTS_AVAILABLE_QUANTITY' : '<%=Msg.get(MsgEnum.ACCOUNTS_AVAILABLE_QUANTITY_LABEL)%>',
			'ACCOUNTS_REMARKS' : '<%=Msg.get(MsgEnum.ACCOUNTS_REMARKS_LABEL)%>',
			'ACCOUNTS_RETURN_QTY' : '<%=Msg.get(MsgEnum.ACCOUNTS_RETURN_QTY)%>',
			'ACCOUNTS_CLOSING_STOCK' : '<%=Msg.get(MsgEnum.ACCOUNTS_CLOSING_STOCK_LABEL)%>',
			//Day Book
			'DAY_BOOK_CREATED_DATE_LABEL' :'<%=Msg.get(MsgEnum.DAY_BOOK_CREATED_DATE_LABEL)%>',
			'DAY_BOOK_SALES_EXECUTIVE' :'<%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%>',
			'DAY_BOOK_NUMBER' :'<%=Msg.get(MsgEnum.DAY_BOOK_NO_LABEL)%>',
			'SERIAL_NUMBER_LABEL' :'<%=Msg.get(MsgEnum.SERIAL_NUMBER_LABEL)%>',
			'DAY_BOOK_AMOUNT_LABEL' :'<%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%>',
			'DAY_BOOK_REMARKS' :'<%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%>',
			'DAY_BOOK_BUSINESS_NAME' :'<%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%>',
			'DAY_BOOK_METER_READING' :'<%=Msg.get(MsgEnum.DAY_BOOK_METER_READING_LABEL)%>',
			 // Currency Format
			'CURRENCY_FORMATE' : '<%=currencyFormat%>', 
			 // Currency Format
			'ADVANCE_AMOUNT' : '<%=advance%>', 
			// DayBook Advance Paid
			'DayBook_ADVANCE_AMOUNT' : '<%=advancePaid%>', 
			//Organization
			'DEFAULT_TYPES':'<%=Msg.get(MsgEnum.DEFAULT_TYPES)%>',
			'DELIVERY_NOTE_PAYMENT_TYPES':'<%=Msg.get(MsgEnum.DELIVERY_NOTE_PAYMENT_TYPES)%>',
			//Journals
			'JOURNAL_TYPES': '<%=Msg.get(MsgEnum.JOURNAL_TYPES)%>',
			'JOURNAL_TYPE':'<%=Msg.get(MsgEnum.JOURNAL_TYPE)%>',
			'JOURNAL_CREATED_DATE':'<%=Msg.get(MsgEnum.JOURNAL_CREATED_DATE)%>',
			'JOURNAL_AMOUNT':'<%=Msg.get(MsgEnum.JOURNAL_AMOUNT)%>',
			 'INVOICE_NO':'<%=Msg.get(MsgEnum.JOURNAL_INVOICE_NO)%>',
			
			//Alerts
			'ALERT_NAME_LABEL':'<%=Msg.get(MsgEnum.ALERT_NAME_LABEL)%>',
			'ALERT_TYPE_LABEL':'<%=Msg.get(MsgEnum.ALERT_TYPE_LABEL)%>',
			'ALERT_DESCRIPTION_LABEL':'<%=Msg.get(MsgEnum.ALERT_DESCRIPTION_LABEL)%>',
			'ALERT_CATEGORY':'<%=Msg.get(MsgEnum.ALERT_CATEGORY)%>',
			'ALERT_CRITERIA_LABEL':'<%=Msg.get(MsgEnum.ALERT_CRITERIA_LABEL)%>',
			'ALERT_COUNT_LABEL':'<%=Msg.get(MsgEnum.ALERT_COUNT_LABEL)%>',
			'ALERT_ENABLE_DISABLE_LABLE':'<%=Msg.get(MsgEnum.ALERT_ENABLE_DISABLE_LABLE)%>',
			'ALERT_CREATED_USER':'<%=Msg.get(MsgEnum.ALERT_CREATED_USER)%>',
			'ALERT_SERIAL_NO':'<%=Msg.get(MsgEnum.ALERT_SERIAL_NO)%>',
			'ALERT_CREATED_DATE_LABEL':'<%=Msg.get(MsgEnum.ALERT_CREATED_DATE_LABEL)%>',
			//Common
			'CANCEL_BUTTON_MESSAGE': '<%=Msg.get(MsgEnum.CANCEL_BUTTON_MESSAGE)%>',
			'CLEAR_BUTTON_MESSAGE': '<%=Msg.get(MsgEnum.CLEAR_BUTTON_MESSAGE)%>',
     }
 
 </script>