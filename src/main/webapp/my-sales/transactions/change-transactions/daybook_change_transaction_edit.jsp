<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<div id="day-book-add-form-container" title="Day Book">
	<div class="ui-content form-panel full-content">

		<form id="day-book-basic-info-form" style="height: 350px;">
			<div class="add-student-tabs">
				<div class="step-selected" style="width: 100px;">
				    <div class="step-no-select-corner"></div>
					<div class="tabs-title" style="padding-left: 10px;"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%></div>
				</div>
				<div class="step-no-select" style="width: 140px;">
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
				<div class="step-no-select" style="width: 100px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PREVIEW_LABEL)%></div>
				</div>
				<div class="step-no-select-corner"></div>
			</div>
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 30px;">
				    <div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_NO_LABEL)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="dayBookNo"
								id="dayBookNo"  readonly="readonly">
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_SALES_EXECUTIVE)%>
						</div>
						<div class="input-field">
							<input class="mandatory read-only" name="salesExecutive"
								id="salesExecutive"  readonly="readonly">
						</div>
					</div>
					<div class="form-row">
						<div class="input-field">
							<input name="isReturn" id="isReturn" type="checkbox" style="width: 28px !important; border: 0 !important;">
						</div>
						<div class="label" style="float: left; margin-left: -5px"><%=Msg.get(MsgEnum.DAY_BOOK_IS_RETURN)%>
						</div>
					</div>
				</div>
				<div class="separator" style="height: 40px;"></div>
				<div class="fieldset" style="height: 30px;">
					<div class="form-row">
						<div class="label">Start Date</div>
						<div class="input-field">
							<input type="text" id="startDate" class="read-only" readonly="readonly">
						</div>
					</div>
					<div class="form-row" id="presentDate">
						<div class="label"><%=Msg.get(MsgEnum.CURRENT_DATE)%>
						</div>
						<div class="input-field">
							<input name="currentDate" id="currentDate"  class="read-only" readonly="readonly">
						</div>
					</div>  
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OPENING_BALANCE_LABEL)%>
						</div>
						<div class="input-field">
							<input name="allotStockOpeningBalance" id="allotStockAdvance" class="read-only" readonly="readonly" readonly="readonly">
						</div>
					</div>
					<div class="form-row" id="reportingManagerLabel">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REPORTING_MANAGER)%>
					</div>
						<div class="input-field">
							<input class="mandatory" name="reportingManager" id="reportingManager" >
						</div>
						<span id="reportingManagerValid"
								style="float: left; position: absolute; margin-left: 8px; margin-top: 2px"></span>
							<div id="reportingManager_pop" class="helppop"
								style="display: block; float: left; margin-left: 283px; margin-top: -30px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7px;">
									<p>Reporting Manager can Have Only Characters and Spaces
										only</p>
								</div>
							</div>
							<div id="reportingManagerLen_pop" class="helppop"
								style="display: block; float: left; margin-left: 283px; margin-top: -30px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7px;">
									<p>Reporting Manager Length Can not Exceed 35 </p>
								</div>
							</div>
					</div>
				</div>
				<div class="separator" style="height: 100px;"></div>
				<div class="form-row">
					<div class="label" style="margin-top: 50px;"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 140px; margin-top:-30px;" name="dayBookCRRemarks"
						id="dayBookCRRemarks" cols="50" rows="3"></textarea>
				</div>
			  </div>
				<div class="input-field">
					<input name="action" value="daybook-basic-info" type="hidden" id="dayBookAction">
				</div>
				<input type="hidden" name="dayBookNo" id="dayBookNo" >
		</form>
		
		<form name="vehicleDetails" id="day-book-vehicle-details-form"
			style="display: none; height: 350px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
				</div>
				<div class="step-selected">
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
				<div class="step-no-select" style="width: 90px;">
					<div class="step-no-select-corner"></div>
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_PRODUCTS_LABEL)%></div>
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
							<input class="mandatory" style="border: none;" class="read-only" readonly="readonly" name="vehicleNo" id="vehicleNo" >
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
							<input name="driverName" class="read-only" readonly="readonly" id="driverName" class="mandatory" >
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
								<input name="startingReading" style="border: none;" id="startReading"
									class="mandatory" class="read-only" readonly="readonly" >
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
								<input name="endingReading" id="endingReading" class="mandatory" >
								<span id="readingValid"
									style="float: left; position: absolute; margin-left: 145px; margin-top: -20px"></span>
								<div id="reading_pop" class="helppop"
									style="display: block; float: left; margin-left: 130px;"
									aria-hidden="false">
									<div id="namehelp" class="helpctr"
										style="float: left; margin-left: 10px;">
										<p>Ending Reading must greater than Starting Reading and Meter Reading if Exists</p>
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
					<div class="form-row" style="margin-bottom:30px;">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
						<div class="input-field">
							<textarea style="float: left;" name="dayBookVehicleRemarks"
								id="dayBookVehicleRemarks" cols="52" rows="4"></textarea>
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
		
		<form name="day-book-allowances-form" id="day-book-allowances-form"
			style="display: none; height: 360px;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
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
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<a id="executiveAllowancesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_EXECUTIVE_ALLOWANCES)%>
						</div></a>
						<div class="input-field">
							<input name="executiveAllowances" class="allowances read-only" id="executiveAllowances" readonly='readonly'>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="driverAllowancesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_ALLOWANCES)%></div></a>
						<div class="input-field">
							<input name="driverAllowances" class="allowances read-only" id="driverAllowances" readonly='readonly'>
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<a id="vehicleFuelExpensesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_FUEL_EXPENSES)%></div></a>
						<div class="input-field">
							<input name="vehicleFuelExpenses" class="allowances read-only" id="vehicleFuelExpenses" readonly='readonly' >
						</div>
					</div>
					<div class="separator" style="height: 20px;"></div>
					 <div class="form-row">
						<a id="vehicleMaintenanceExpensesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_MAINTENANCE_EXPENSES)%></div></a>
						<div class="input-field">
							<input name="vehicleMaintenanceExpenses" readonly='readonly'
								id="vehicleMaintenanceExpenses" class="allowances read-only" >
						</div>
					</div>
					
					<div class="separator" style="height: 20px;"></div>
					<div class="form-row">
						<a id="offloadingChargesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_OFFLOADING_CHARGES)%></div></a>
						<div class="input-field">
							<input name="offloadingLoadingCharges"
								id="offloadingLoadingCharges" class="allowances read-only" readonly='readonly' >
						</div>
					</div>
				</div>
				<div class="separator" style="height: 120px;"></div>
				<div class="fieldset" style="height: 110px;">
					<div class="form-row">
						<a id="miscellaneousExpensesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_MISCELLANEOUS_EXPENSES)%></div></a>
						<div class="input-field">
							<input name="miscellaneousExpenses" class="allowances read-only" readonly='readonly' id="miscellaneousExpenses" >
						</div>
					</div>
					<div class="separator" style="height: 30px;"></div>
					<div class="form-row">
						<a id="dealerPartyExpensesCRLink" href="#"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DEALER_PARTY_EXPENCES)%></div></a>
						<div class="input-field">
							<input name="dealerPartyExpenses" class="allowances read-only" readonly='readonly' id="dealerPartyExpenses" >
						</div>
					</div>
					<div class="separator" style="height:20px;"></div>
					<div class="form-row">
						<a id="municipalCityCouncilCRLink" href="#"><div class="label" style="cursor:pointer !important;"><%=Msg.get(MsgEnum.DAY_BOOK_MUNICIPAL_CITY_COUNCIL)%></div></a>
						<div class="input-field">
							<input name="municipalCityCouncil" class="allowances read-only" readonly='readonly' id="municipalCityCouncil" >
						</div>
					</div>
					<div class="separator" style="height: 40px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_TOTAL_ALLOWANCES)%></div>
						<div class="input-field">
							<input class="allowances read-only" name="totalAllowances" readonly='readonly'
								id="totalAllowances" readonly='readonly' >
						</div>
					</div>
				</div>
				<div class="form-row" style="margin-top: 130px; float:left; width:100%;">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left;  margin-top:-10px;" name="allowancesCRRemarks"
						id="allowancesCRRemarks" cols="52" rows="3"></textarea>
				</div>
			</div>
			<input type="hidden" name="executiveOriginalAllowances"id="executiveOriginalAllowances" >
			<input type="hidden" name="driverOriginalAllowances" id="driverOriginalAllowances">
			<input type="hidden" name="vehicleFuelOriginalExpenses"  id="vehicleFuelOriginalExpenses">
			<input type="hidden" name="vehicleMaintenanceOriginalExpenses"  id="vehicleMaintenanceOriginalExpenses">
			<input type="hidden" name="offloadingLoadingOriginalCharges" id="offloadingLoadingOriginalCharges">
			<input type="hidden" name="miscellaneousOriginalExpenses"   id="miscellaneousOriginalExpenses">
			<input type="hidden" name="dealerPartyOriginalExpenses"   id="dealerPartyOriginalExpenses">
			<input type="hidden" name="municipalCityOriginalCouncil"   id="municipalCityOriginalCouncil">
			<input type="hidden" name="totalOriginalAllowances"  id="totalOriginalAllowances">
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
				<div class="fieldset" style="height: 120px;">
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_PAYABLE)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalPayable"
								id="customerTotalPayable" readonly='readonly' >
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>

					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_RECEIVED)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalReceived"
								id="customerTotalReceived" readonly='readonly' >
						</div>
					</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CUSTOMER_TOTAL_CREDIT)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="customerTotalCredit"
								id="customerTotalCredit" readonly='readonly' >
						</div>
					</div>
				</div>
				<div class="separator" style="height: 140px;"></div>
				<div class="fieldset" style="height: 120px;">
				<div class="form-row">
						<a href="#" id="amountToBankCRLink"><div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_BANK)%></div></a>
						<div class="input-field">
							<input name="amountToBank" class="mandatory read-only" id="amountToBank" readonly='readonly' >
						</div>
				</div>
				<div class="separator" style="height: 10px;"></div>
				<div class="form-row">
						<div class="label amountToFactory"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_FACTORY)%></div>
						<div class="input-field">
							<input name="amountToFactory" id="amountToFactory" >
						</div>
				</div>
					<div class="separator" style="height: 10px;"></div>
					<div class="form-row">
						<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_CLOSING_BALANCE)%></div>
						<div class="input-field">
							<input class="mandatory read-only" name="closingBalance"
								id="closingBalance" readonly='readonly'>
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
				<div class="separator" style="height: 10px;"></div>
				<div class="form-row" style="margin-top: 20px;">
					<div class="label" style="float: left; margin-left: -287px;"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: -160px;" name="amountsCRRemarks"
						id="amountsCRRemarks" cols="52" rows="3"></textarea>
				</div>
			</div>
		</form>

		<form id="day-book-product-form" style="height: 350px; display: none;">
			<div class="add-student-tabs">
				<div class="step-no-select" style="width: 100px;">
					<div class="tabs-title"><%=Msg.get(MsgEnum.DAY_BOOK_BASIC_INFO_LABEL)%>
					</div>
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
						style="height: 130px; width: 830px; overflow-x: hidden;"></div>
				</div>
				<div class="separator" style="height: 10px;"></div>
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<textarea style="float: left; margin-left: 95px; margin-top:-30px !important;" name="productsCRRemarks"
						id="productsCRRemarks" cols="80" rows="3"></textarea>
				</div>
				</div>
				<div class="fieldset" style="height: 100px;">
					<div class="form-row">
						<div class="input-field">
							<input name="action" value="daybook-products" type="hidden" id="dayBookAction">
							<input name="dayBookId" type="hidden" id="dayBookId">
							<input name="salesBookId"  type="hidden" id="salesBookId">
							<input name="originalReportingManagerName"  type="hidden" id="originalReportingManagerName">
							<input name="originalDayBookCRRemarks"  type="hidden" id="originalDayBookCRRemarks">
							<input name="originalEndingReading"  type="hidden" id="originalEndingReading">
							<input name="originalDayBookVehicleCRRemarks"  type="hidden" id="originalDayBookVehicleCRRemarks">
							<input name="originalDayBookCRAllowancesRemarks"  type="hidden" id="originalDayBookCRAllowancesRemarks">
						    <input name="originalDayBookCRAmountsRemarks"  type="hidden" id="originalDayBookCRAmountsRemarks"> 
						     <input name="originalDayBookCRProductRemarks"  type="hidden" id="originalDayBookCRProductRemarks"> 
						</div>
					</div>
				</div>
		</form>

		<div id="day-book-preview-container"
			style="display: none; overflow-y: scroll;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 200px; margin-top: 75px;">
			<div id="button-prev" class="ui-btn btn-prev" style="display: none">Previous</div>
			<div id="button-next" class="ui-btn btn-next">Next</div>
			<div id="button-save" class="ui-btn btn-save" style="display: none;">Save</div>
			<div id="action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
	</div>
</div>
<div id="cr-executive-allowances-view-dialog" style="display: none;">
	<div id="cr-executive-allowances-view-container"></div>
</div>
<script type="text/javascript">
TransactionChangeRequestHandler.dayBookInitAddButtons();
TransactionChangeRequestHandler.dayBookLoad();
	$(document).ready(function() {
	$('.helppop').hide();
	});
</script>
