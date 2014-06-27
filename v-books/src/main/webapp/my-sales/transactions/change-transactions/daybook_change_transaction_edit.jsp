<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="com.vekomy.vbooks.util.StringUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.mysales.dao.ChangeTransactionDao"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBook"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookProducts"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbDayBookAmount"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBook"%>

<%
User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
VbDayBook vbDayBook = null;
VbDayBookProducts vbDayBookProducts = null;
VbDayBookAmount vbDayBookAmount = null; 
VbSalesBook vbSalesBook=null;
    Float advance= new Float(0);
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) hibernateContext
				.getBean("changeTransactionDao");
		if (changeTransactionDao != null) {
			int dayBookId = Integer.parseInt(request.getParameter("id"));
			 vbDayBook = changeTransactionDao.getDayBook(dayBookId , user.getOrganization(),user.getName());
			Iterator dayBookProductsIterator = vbDayBook.getVbDayBookProductses().iterator();
			if (dayBookProductsIterator.hasNext()) {
				vbDayBookProducts = (VbDayBookProducts) dayBookProductsIterator.next();
			}
			Iterator dayBookAmountsIterator = vbDayBook.getVbDayBookAmounts().iterator();
			if (dayBookAmountsIterator.hasNext()) {
				vbDayBookAmount = (VbDayBookAmount) dayBookAmountsIterator.next();
			}
			if (vbDayBookProducts == null) {
				vbDayBookProducts = new VbDayBookProducts();
			} 
			advance=changeTransactionDao.getAdvance(dayBookId, user.getOrganization());
			vbSalesBook=changeTransactionDao.getSalesExecutiveType(dayBookId, user.getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="day-book-add-form-container" title="Day Book">
	<div class="ui-content form-panel full-content">

		<div id="day-book-add-form-container" title="Day Book">
			<div class="green-title-bar">
				<div class="green-title-bar2"></div>
			</div>
		</div>

		<form id="day-book-basic-info-form" style="height: 160px;">
			<div class="add-student-tabs">
				<div class="step-selected" style="width: 100px;">
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 120px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
				<div class="step-no-select" style="width: 100px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="salesExecutive"
								id="salesExecutive" value=<%=vbDayBook.getSalesExecutive()%> readonly="readonly">
						</div>
					</div>
					<%if("daily".equals(vbSalesBook.getAllotmentType())){ %>
					<%}else{ %>
					<div class="form-row">
						<div class="input-field">
							<input name="isReturn" id="isReturn" type="checkbox" value="<%=vbDayBook.getIsReturn()%>">
						</div>
						<div class="label" style="float: left; margin-left: -5px"><%=Msg.get(MsgEnum.DAY_BOOK_IS_RETURN)%>
						</div>
					</div>
					<%} %>
				</div>
				<div class="separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label">Date</div>
						<div class="input-field">
							<input type="text" value="<%=DateUtils.format(new Date())%>"
								disabled="true">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input name="allotStockOpeningBalance" id="allotStockAdvance" class="read-only" readonly="readonly" value="<%=StringUtil.currencyFormat(advance)%>" readonly="readonly">
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-basic-info" type="hidden"
								id="dayBookAction">
						</div>
					</div>
				</div>
			</div>
		</form>
		
		<!--  -->
		
		<form name="day-book-allowances-form" id="day-book-allowances-form"
			style="display: none; height: 360px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-selected" style="width: 120px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_EXECUTIVE_ALLOWANCES)%>
						</div>
						<div class="input-field">
							<input name="executiveAllowances" id="executiveAllowances" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getExecutiveAllowances())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_ALLOWANCES)%></div>
						<div class="input-field">
							<input name="driverAllowances" id="driverAllowances" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getDriverAllowances())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_FUEL_EXPENSES)%></div>
						<div class="input-field">
							<input name="vehicleFuelExpenses" id="vehicleFuelExpenses" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getVehicleFuelExpenses())%>">
						</div>
					</div>
					<div class="separator" style="height: 20px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OFFLOADING_CHARGES)%></div>
						<div class="input-field">
							<input name="offloadingLoadingCharges"
								id="offloadingLoadingCharges" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getOffloadingLoadingCharges())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES)%></div>
						<div class="input-field">
							<input name="vehicleMaintenanceExpenses"
								id="vehicleMaintenanceExpenses" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getVehicleMaintenanceExpenses())%>">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 120px;"></div>
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_MISCELLANEOUS_EXPENSES)%></div>
						<div class="input-field">
							<input name="miscellaneousExpenses" id="miscellaneousExpenses" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getMiscellaneousExpenses())%>">
						</div>
					</div>
					<div class="separator" style="height: 20px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REASON_TO_MISCELLANEOUS_EXPENSES)%></div>
						<div class="input-field">
							<textarea name="reasonMiscellaneousExpenses" cols="15" rows="3"
								id="reasonMiscellaneousExpenses"><%=StringUtil.format(vbDayBookAmount.getReasonMiscellaneousExpenses())%></textarea>
						</div>
					</div>
					<div class="separator" style="height: 40px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DEALER_PARTY_EXPENCES)%></div>
						<div class="input-field">
							<input name="dealerPartyExpenses" id="dealerPartyExpenses" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getDealerPartyExpenses())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_MUNICIPAL_CITY_COUNCIL)%></div>
						<div class="input-field">
							<input name="municipalCityCouncil" id="municipalCityCouncil" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getMunicipalCityCouncil())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_TOTAL_ALLOWANCES)%></div>
						<div class="input-field">
							<input class="mandatory" name="totalAllowances"
								id="totalAllowances" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDayBookAmount.getTotalAllowances())%>">
						</div>
					</div>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-allowances" type="hidden"
								id="dayBookAction">
						</div>
					</div>
				</div>
			</div>
		</form>
		<!--  -->
		<form name="day-book-amount-form" id="day-book-amount-form"
			style="display: none; height: 260px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-no-select" style="width: 120px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
				<div class="step-selected" style="width: 90px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_PAYABLE)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalPayable"
								id="customerTotalPayable" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDayBookAmount.getCustomerTotalPayable())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_RECEIVED)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalReceived"
								id="customerTotalReceived" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDayBookAmount.getCustomerTotalReceived())%>">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_CREDIT)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalCredit"
								id="customerTotalCredit" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDayBookAmount.getCustomerTotalCredit())%>">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_FACTORY)%></div>
						<div class="input-field">
							<input name="amountToFactory" id="amountToFactory" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getAmountToFactory())%>">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 140px;"></div>
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_BANK)%></div>
						<div class="input-field">
							<input name="amountToBank" id="amountToBank" value="<%=StringUtil.currencyFormat(vbDayBookAmount.getAmountToBank())%>">
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REASON_TO_AMOUNT_TO_BANK)%></div>
						<div class="input-field">
							<textarea name="reasonAmountToBank" id="reasonAmountToBank" rows="3" cols="15"><%=vbDayBookAmount.getReasonAmountToBank()%></textarea>
						</div>
					</div>
					<div class="seperator" style="height: 110px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_BALANCE)%></div>
						<div class="input-field">
							<input class="mandatory" name="closingBalance"
								id="closingBalance" readonly='readonly' value="<%=StringUtil.currencyFormat(vbDayBookAmount.getClosingBalance())%>">
						</div>
					</div>
					<div class="fieldset-row" style="margin-top: 10px;">
						<div class="fieldset" style="height: 100px;">
							<div class="form-row">
								<div class="input-field">
									<input name="action" value="daybook-amount" type="hidden"
										id="dayBookAction">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>

		<form id="day-book-product-form" style="height: 350px; display: none;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-no-select" style="width: 120px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
				</div>
				<div class="step-selected" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 20px;">
				<div class="fieldset" style="height: 200px; width: 699px;">
					<div class="report-header" style="width: 830px; height: 40px;">
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCT_NAME)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_BATCH_NUMBER)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_STOCK)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_RETURN_QUANTITY)%></div>
						<div class="report-header-column2  productToCustomer"style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_CUSTOMER)%></div>
						<div class="report-header-column2  productToFactories"style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_FACTORY)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_STOCK)%></div>
					</div>
					<div id="search-results-list" class="green-results-list"
						style="height: 250px; width: 830px; overflow-x: hidden;"></div>
				</div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-products" type="hidden"
								id="dayBookAction">
							<input name="dayBookId" value="<%=vbDayBook.getId() %>" type="hidden"
								id="dayBookId">
						</div>
					</div>
				</div>
			</div>
		</form>
		<form name="vehicleDetails" id="day-book-vehicle-details-form"
			style="display: none; height: 350px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-no-select" style="width: 120px;">
				<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
				</div>
				<div class="step-selected">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
				</div>
				<div class="step-no-select" style="width: 90px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 80px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_NO)%></div>
						<div class="input-field">
							<input class="mandatory" name="vehicleNo" id="vehicleNo" value="<%=vbDayBook.getVehicleNo()%>">
							<span id="vehicleNOValid"
								style="float: left; position: absolute; margin-left: 150px; margin-top: -15px"></span>
							<div id="vehicleNo_pop" class="helppop"
								style="display: block; float: left; margin-left: 150px; margin-top: -30px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 10px;">
									<p>Vehicle Number can Have Numbers,letters,hyphen(-),dot(.)
										and Spaces only</p>
								</div>
							</div>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_NAME)%></div>
						<div class="input-field">
							<input name="driverName" id="driverName" class="mandatory" value="<%=StringUtil.format(vbDayBook.getDriverName())%>">
						</div>
						<span id="driverNameValid"
							style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="driverName_pop" class="helppop"
							style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Driver Name must Contains Only Characters</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 80px;"></div>
				<div id="dynamic-display">
					<div class="fieldset" style="height: 80px;">
						<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOKS_STARTING_READING)%></div>
							<div class="input-field">
								<input name="startingReading" id="startReading"
									class="mandatory" value="<%=StringUtil.format(vbDayBook.getStartingReading())%>">
							</div>
						</div>
						<span id="startreadingValid"
							style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="startreading_pop" class="helppop"
							style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Starting Reading must Contains Only Numbers</p>
							</div>
						</div>
						<div class="separator" style="height: 10px;"></div>
						<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_ENDING_READING)%></div>
							<div class="input-field">
								<input name="endingReading" id="endingReading" class="mandatory" value="<%=StringUtil.format(vbDayBook.getEndingReading())%>">
								<span id="readingValid"
									style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								<div id="reading_pop" class="helppop"
									style="display: block; float: left; margin-left: 130px;"
									aria-hidden="false">
									<div id="namehelp" class="helpctr"
										style="float: left; margin-left: 10px;">
										<p>Ending Reading must greater than Starting Reading</p>
									</div>
								</div>
								<div id="readingvalid_pop" class="helppop"
									style="display: block; float: left; margin-left: 130px;"
									aria-hidden="false">
									<div id="namehelp" class="helpctr"
										style="float: left; margin-left: 10px;">
										<p>Ending Reading must Contains Only Numbers</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
						<div class="input-field">
							<textarea style="float: left;" name="remarks"
								id="remarks" cols="52" rows="4"><%=vbDayBook.getRemarks() %></textarea>
						</div>
					</div>
				</div>
				<div class="form-row" style="height: 10px; margin-top: 80px; margin-left: -280px;">
						<div class="label"><%=Msg.get(MsgEnum.CHANGE_REQUEST_DESCRIPTION)%></div>
						<div class="input-field">
							<textarea style="float: left;" name="description" id="description" rows="4" cols="52"></textarea>
						</div>
					</div>
			</div>
			 <div class="input-field"  style="height: 10px; margin-top: 30px;">
									<input name="action" value="vehicle-details" type="hidden"
										id="dayBookAction">
			</div> 
		</form>

		<div id="day-book-preview-container"
			style="display: none; overflow-y: scroll;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none"></div>
			<div id="button-next" class="ui-btn btn-next"></div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;"></div>
			<div id="action-clear" class="ui-btn btn-clear"></div>
			<div id="action-cancel" class="ui-btn btn-cancel"></div>
		</div>
	</div>
</div>
<script type="text/javascript">
TransactionChangeRequestHandler.dayBookInitAddButtons();
TransactionChangeRequestHandler.getDayBookCustomerTotalPayable();
TransactionChangeRequestHandler.dayBookLoad();
	UserHandler.initCheckUsername();
	UserHandler.initCheckPassword();
	$(document).ready(function() {
		  $('#day-book-allowances-form').find('input,textarea').live('change paste', function (event) {
		    var name  = this.name,
		        value = this.value,
		        id    = this.id,
		        dObj  = {};
		    dObj[id] = value;
		    /* if(id == 'reasonMiscellaneousExpenses'){
		    	   TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,value);
		    }else{ */
		    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,currencyHandler.convertStringPatternToFloat(value));
		    /* } */
		});
		  $('#day-book-amount-form').find('input,textarea').live('change paste', function (event) {
			    var name  = this.name,
			        value = this.value,
			        id    = this.id,
			        dObj  = {};
			    dObj[id] = value;
			    /* if(id == 'reasonAmountToBank'){
			    	TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,value);
			    }else{ */
			    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,currencyHandler.convertStringPatternToFloat(value));
			   /*  } */
			    });
		  $('#day-book-product-form').find('input').live('change paste', function (event) {
			    var name  = this.name,
			        value = this.value,
			        id    = this.id,
			        dObj  = {};
			    dObj[id] = value;
			    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,currencyHandler.convertStringPatternToFloat(value));
			});
		  $('#day-book-vehicle-details-form').find('input,textarea').live('change paste', function (event) {
			    var name  = this.name,
			        value = this.value,
			        id    = this.id,
			        dObj  = {};
			    dObj[id] = value;
			    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,value);
			   /*  if(id == 'driverName' || id='vehicleNo' || id='remarks'){
			    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,value);
			    }else{
			    TransactionChangeRequestHandler.changedDayBookFormChangedValues(id,currencyHandler.convertStringPatternToFloat(value));
			    } */
			    });
		  $('.helppop').hide();
	});
</script>
