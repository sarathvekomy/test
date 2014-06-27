<%@page import="com.vekomy.vbooks.util.DateUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%
	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<div id="day-book-add-form-container" title="Day Book">
	<div class="ui-content form-panel full-content">
		<div id="day-book-add-form-container" title="Day Book"></div>
		<form id="day-book-basic-info-form" style="height: 395px;">
			<div class="add-student-tabs">
				<div class="step-selected" style="width: 100px;">
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
				<div class="step-no-select" style="width: 100px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="salesExecutive" id="salesExecutive" value=<%=user.getName()%> readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input name="allotStockOpeningBalance" id="allotStockOpeningBalance" class="read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="input-field" style="width: 35px !important;">
							<input name="isReturn" id="isReturn" type="checkbox" style="width: 28px !important; border: 0 !important;"/>
						</div>
						<div class="label" style="float: left; margin-left: -5px; margin-top: 4px;"><%=Msg.get(MsgEnum.DAY_BOOK_IS_RETURN)%></div>
					</div>
				</div>
				<div class="separator" style="height: 100px;"></div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label">Start Date</div>
						<div class="input-field">
								<input type="text" id="startDate" class="read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="fieldset" style="height: 30px;">
						<div class="form-row" id="presentDate">
							<div class="label"><%=Msg.get(MsgEnum.CURRENT_DATE)%></div>
							<div class="input-field">
								<input name="currentDate" id="currentDate" value="<%=DateUtils.format(new Date())%>" class="read-only" readonly="readonly"/>
							</div>
						</div>
						<div class="separator" style="height: 10px;"></div>
						<div class="form-row" id="manager">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REPORTING_MANAGER)%></div>
							<div class="input-field">
								<input name="reportingManagerName" id="reportingManager"/>
							</div>
							<span id="reportingManagerValid" style="float: left; position: absolute; margin-left: 8px; margin-top: 2px"></span>
							<div id="reportingManager_pop" class="helppop" style="display: block; float: left; margin-left: 283px; margin-top: -30px;" aria-hidden="false">
								<div id="namehelp" class="helpctr" style="float: left; margin-left: 7px;">
									<p>Reporting Manager can Have Only Characters and Spaces only</p>
								</div>
							</div>
							<div id="reportingManagerLen_pop" class="helppop" style="display: block; float: left; margin-left: 283px; margin-top: -30px;" aria-hidden="false">
								<div id="namehelp" class="helpctr" style="float: left; margin-left: 7px;">
									<p>Reporting Manager Length Can not Exceed 35 </p>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="separator" style="height: 120px;"></div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 60px;" name="dayBookRemarks" id="remarks" cols="70" rows="3"></textarea>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-basic-info" type="hidden" id="dayBookAction"/>
						</div>
					</div>
				</div>
			</div>
		</form>

		<form name="vehicleDetails" id="day-book-vehicle-details-form"
			style="display: none; height: 395px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-selected">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_NO)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="vehicleNo" id="vehicleNo" readonly="readonly"/>
							 <span id="vehicleNOValid" style="float: left; position: absolute; margin-left: 150px; margin-top: -15px"></span>
							<div id="vehicleNo_pop" class="helppop" style="display: block; float: left; margin-left: 150px; margin-top: -30px;" aria-hidden="false">
								<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
									<p>Vehicle Number can Have Numbers,letters,hyphen(-),dot(.) and Spaces only</p>
								</div>
							</div>
							<div id="vehicleNolen_pop" class="helppop" style="display: block; float: left; margin-left: 150px; margin-top: -30px;" aria-hidden="false">
								<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
									<p><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_NO_LENGTH)%></p>
								</div>
							</div>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_NAME)%></div>
						<div class="input-field">
							<input name="driverName" id="driverName" class="mandatory read-only" readonly="readonly"/>
						</div>
						<span id="driverNameValid" style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="driverName_pop" class="helppop" style="display: block; float: left; margin-left: 280px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
								<p>Driver Name must Contains Only Characters</p>
							</div>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 120px;"></div>
				<div id="dynamic-display">
					<div class="fieldset" style="height: 120px;">
						<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOKS_STARTING_READING)%></div>
							<div class="input-field">
								<input name="startingReading" id="startReading" readonly="readonly" class="mandatory read-only"/>
							</div>
						</div>
						<span id="startreadingValid" style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="startreading_pop" class="helppop" style="display: block; float: left; margin-left: 280px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
								<p>Starting Reading must Contains Only Numbers</p>
							</div>
						</div>
						<div class="separator" style="height: 10px;"></div>
						<div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_ENDING_READING)%></div>
							<div class="input-field">
								<input name="endingReading" id="endingReading" class="mandatory"/>
								<span id="readingValid" style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								<div id="reading_pop" class="helppop" style="display: block; float: left; margin-left: 130px;" aria-hidden="false">
									<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
										<p>Ending Reading must greater than Starting Reading and Meter Reading if Exists</p>
									</div>
								</div>
								<div id="readingvalid_pop" class="helppop" style="display: block; float: left; margin-left: 130px;" aria-hidden="false">
									<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
										<p>Ending Reading must Contains Only Numbers</p>
									</div>
								</div>
								<div id="readingLen_pop" class="helppop" style="display: block; float: left; margin-left: 130px;" aria-hidden="false">
									<div id="namehelp" class="helpctr" style="float: left; margin-left: 10px;">
										<p>Ending Reading Length can Not Exceed 6</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="form-row" style="width: 279px" !important>
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
						<div class="input-field">
							<textarea style="float: left; margin-left: 60px;" name="remarks" id="remarks" cols="70" rows="4"></textarea>
						</div>
					</div>
					<div class="fieldset-row" style="margin-top: 10px;">
						<div class="fieldset" style="height: 100px;">
							<div class="form-row">
								<div class="input-field">
									<input name="action" value="vehicle-details" type="hidden" id="dayBookAction">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<form name="day-book-allowances-form" id="day-book-allowances-form"
			style="display: none; height: 395px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>

			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 200px;">
					<div class="form-row">
						<a id="executiveAllowancesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_EXECUTIVE_ALLOWANCES)%></div>
						</a>
						<div class="input-field">
							<input name="executiveAllowances" id="executiveAllowances" class="allowances read-only" readonly="readonly"/>
						</div>
						<span id="executiveallValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="executiveall_pop" class="helppop" style="display: block; margin-top: -5px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; margin-top: 2px;">
								<p><%=Msg.get(MsgEnum.DAY_BOOK_EXECUTIVE_ALLOWANCES_POP)%></p>
							</div>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="driverAllowancesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_ALLOWANCES)%></div>
						</a>
						<div class="input-field">
							<input name="driverAllowances" id="driverAllowances" class="allowances read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="vehicleFuelExpensesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_FUEL_EXPENSES)%></div>
						</a>
						<div class="input-field">
							<input name="vehicleFuelExpenses" id="vehicleFuelExpenses" class="allowances read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 20px;"></div>
					<div class="form-row">
						<a id="municipalCityCouncilLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_MUNICIPAL_CITY_COUNCIL)%></div>
						</a>
						<div class="input-field">
							<input name="municipalCityCouncil" id="municipalCityCouncil" class="allowances read-only" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="separator" style="height: 200px;"></div>
				<div class="fieldset" style="height: 200px;">
					<div class="form-row">
						<a id="vehicleMaintenanceExpensesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES)%></div>
						</a>
						<div class="input-field">
							<input name="vehicleMaintenanceExpenses" id="vehicleMaintenanceExpenses" class="allowances read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="offloadingChargesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OFFLOADING_CHARGES)%></div>
						</a>
						<div class="input-field">
							<input name="offloadingLoadingCharges" id="offloadingLoadingCharges" class="allowances read-only" readonly="readonly">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="miscellaneousExpensesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_MISCELLANEOUS_EXPENSES)%></div>
						</a>
						<div class="input-field">
							<input name="miscellaneousExpenses" id="miscellaneousExpenses" class="allowances read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="dealerPartyExpensesLink" href="#">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DEALER_PARTY_EXPENCES)%></div>
						</a>
						<div class="input-field">
							<input name="dealerPartyExpenses" id="dealerPartyExpenses" class="allowances read-only" readonly="readonly"/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row" style="margin-top: 10px;">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_TOTAL_ALLOWANCES)%></div>
						<div class="input-field">
							<input name="totalAllowances" id="totalAllowances" class="mandatory totalAllowances read-only" readonly="readonly"/>
						</div>
					</div>
				</div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 60px;" name="allowancesRemarks" id="remarks" cols="70" rows="3"></textarea>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 50px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-allowances" type="hidden" id="dayBookAction">
						</div>
					</div>
				</div>
			</div>
		</form>
		<form name="day-book-amount-form" id="day-book-amount-form"
			style="display: none; height: 395px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_PAYABLE)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalPayable" id="customerTotalPayable" readonly='readonly'/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_RECEIVED)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalReceived" id="customerTotalReceived" readonly='readonly'/>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_CREDIT)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalCredit" id="customerTotalCredit" readonly='readonly'/>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 100px;"></div>
				<div class="fieldset" style="height: 110px;">
					<div class="form-row">
						<a href="#" id="amountToBankLink">
							<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_BANK)%></div>
						</a>
						<div class="input-field">
							<input name="amountToBank" id="amountToBank" class="read-only" readonly="readonly"/>
						</div>
						<span id="amountTobankValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="amountTobank_pop" class="helppop" style="display: block; margin-top: -5px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; margin-top: 2px;">
								<p><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_BANK_POP)%></p>
							</div>
						</div>
						<div id="sumb_pop" class="helppop" style="display: block; margin-top: -5px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; margin-top: 2px;">
								<p><%=Msg.get(MsgEnum.DAY_BOOK_SUM_POP)%></p>
							</div>
						</div>
					</div>
					<div class="seperator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label amountToFactory"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_FACTORY)%></div>
						<div class="input-field">
							<input name="amountToFactory" id="amountToFactory"/>
						</div>
						<span id="amountToFactoryValid" style="float: left; position: absolute; margin-left: 5px; margin-top: 5px"></span>
						<div id="amountToFactory_pop" class="helppop" style="display: block; margin-top: -5px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; margin-top: 2px;">
								<p><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_FACTORY_POP)%></p>
							</div>
						</div>
						<div id="sum_pop" class="helppop" style="display: block; margin-top: -5px;" aria-hidden="false">
							<div id="namehelp" class="helpctr" style="float: left; margin-left: 3px; margin-top: 2px;">
								<p><%=Msg.get(MsgEnum.DAY_BOOK_SUM_POP)%></p>
							</div>
						</div>
					</div>
					<div class="seperator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_BALANCE)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="closingBalance"id="closingBalance" readonly='readonly' style="border: none"/>
						</div>
					</div>
					<div class="fieldset-row" style="margin-top: 10px;">
						<div class="fieldset" style="height: 100px;">
							<div class="form-row">
								<div class="input-field">
									<input name="action" value="daybook-amount" type="hidden" id="dayBookAction">
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="separator" style="height: 10px;"></div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 60px;" name="amountsRemarks" id="remarks" cols="70" rows="3"></textarea>
				</div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
					<div class="fieldset" style="height: 100px;">
						<div class="form-row">
							<div class="input-field">
								<input name="action" value="vehicle-details" type="hidden" id="dayBookAction">
							</div>
						</div>
					</div>
				</div>
		</form>

		<form id="day-book-product-form" style="height: 395px; display: none;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DYA_BOOK_VEHICLE_DETAILS)%></div>
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
				<div class="step-no-select" style="width: 90px;">
					<div class="step-selected-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 20px;">
				<div class="fieldset" style="height: 200px; width: 699px;">
					<div class="report-header"
						style="width: 830px; height: 30px; line-height: 12px;">
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCT_NAME)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_BATCH_NUMBER)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_STOCK)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_RETURN_QUANTITY)%></div>
						<div class="report-header-column2  productToCustomer" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_CUSTOMER)%></div>
						<div class="report-header-column2  productToFactories" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_TO_FACTORY)%></div>
						<div class="report-header-column2 centered" style="width: 100px;"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_STOCK)%></div>
					</div>
					<div id="search-results-list" class="green-results-list" style="height: 150px; width: 830px; overflow-x: hidden;"></div>
				</div>
				<div class="separator" style="height: 10px;"></div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 60px;" name="productsRemarks" id="productsRemarks" cols="72" rows="3"></textarea>
				</div>
			</div>
			<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-products" type="hidden" id="dayBookAction">
						</div>
					</div>
				</div>
		</form>
		<div id="day-book-preview-container" style="display: none; overflow-y: scroll;"></div>
		<div id="page-buttons" class="page-buttons" style="margin-left: 200px; margin-top: 10px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none">Previous</div>
			<div id="button-next" class="ui-btn btn-next">Next</div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;">Save</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>
</div>

<div id="executive-allowances-view-dialog" style="display: none;">
	<div id="executive-allowances-view-container"></div>
</div>
<div id ="amount-to-bank-dialog" style="display: none;">
	<div id="amount-to-bank-container"></div>
</div>
<div id="driver-allowances-view-dialog" style="display: none;" title="Driver Allowances Details">
	<div id="driver-allowances-view-container"></div>
</div>
<script type="text/javascript">
	DayBookHandler.initAddButtons();
	DayBookHandler.load();
	$(document).ready(function() {
		$('#isReturn').change(function(){
			if($('#isReturn').is(':checked')) {
				$('#manager').show();
				$('#reportingManager').attr('class','mandatory');
				$('#amountToFactory').show();
				$('.amountToFactory').show();
			}else{
				$('#manager').hide();
				$('#reportingManager').removeAttr('class','mandatory');
				$('#amountToFactory').hide();
				$('.amountToFactory').hide();
			}
		});
		$('.helppop').hide();
		$('#manager').hide();
	});
</script>