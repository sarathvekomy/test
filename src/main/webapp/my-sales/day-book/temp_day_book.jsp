<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<div class="ui-content form-panel form-panel-border"
	style="height: 170px;">
	<form name="tempDayBookForm" id="tempDayBookForm">
		<div class="fieldset-row" style="height: 40px; margin-top: 10px;">
			<div class="fieldset" style="height: 40px;">
				<div class="form-row">
					<div class="label"><%=Msg.get(MsgEnum.DEFAULT_TYPES)%></div>
					<div class="input-field">
						<select name="dayBookType" id="dayBookType" class="mandatory">
							<option value="-1">Select</option>
							<%
								for (String dayBookType : DropDownUtil.getDropDown(
										DropDownUtil.DAY_BOOK_TYPE).keySet()) {
							%>
							<option value="<%=dayBookType%>"><%=DropDownUtil.getDropDown(DropDownUtil.DAY_BOOK_TYPE,dayBookType)%></option>
							<%
								}
							%>
						</select>
					</div>
					<span id="dayBookTypeValid"
								style="float: left; position: absolute; margin-left: 9px; margin-top: 2px"></span>
							<div id="dayBookType_pop" class="helppop"
								style="display: block; float: left; margin-left: 288px; margin-top: -16px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7x;">
									<p>You must Select atleast One Type</p>
								</div>
							</div>
				</div>
			</div>
			<div class="separator" style="height: 40px;"></div>
			<div class="fieldset" style="height: 40px;">
				<div class="form-row" id="allowanceType">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_LABEL)%></div>
					<div class="input-field">
						<select name="allowanceType" id="allowancesType" class="allowanceType">
							<option value="-1">Select</option>
							<%
								for (String allowanceType : DropDownUtil.getDropDown(
										DropDownUtil.ALLOWANCE_TYPES).keySet()) {
							%>
							<option value="<%=allowanceType%>"><%=DropDownUtil.getDropDown(
						DropDownUtil.ALLOWANCE_TYPES, allowanceType)%></option>
							<%
								}
							%>
						</select>
					</div>
					<span id="allowancesTypeValid"
								style="float: left; position: absolute; margin-left: 9px; margin-top: 2px"></span>
							<div id="allowancesType_pop" class="helppop"
								style="display: block; float: left; margin-left: 288px; margin-top: -16px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7x;">
									<p>You must Select atleast One Type</p>
								</div>
							</div>
				</div>
			</div>
		</div>
		<div class="fieldset-row" style="height: 80px; margin-top: 10px;">
			<div class="fieldset" style="height: 120px;">
				<div class="form-row" id="vehicleNo">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_VEHICLE_NO)%></div>
					<div class="input-field">
						<input name="vehicleNo" id="vehicleNumber">
					</div>
					<span id="vehicleNOValid"
								style="float: left; position: absolute; margin-left: 9px; margin-top: 2px"></span>
							<div id="vehicleNo_pop" class="helppop"
								style="display: block; float: left; margin-left: 288px; margin-top: -16px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7x;">
									<p>Vehicle Number can Have Numbers,letters,hyphen(-),dot(.)
										and Spaces only</p>
								</div>
							</div>
							<div id="vehicleNoLen_pop" class="helppop"
								style="display: block; float: left; margin-left: 288px; margin-top: -16px;"
								aria-hidden="false">
								<div id="namehelp" class="helpctr"
									style="float: left; margin-left: 7x;">
									<p>Vehicle Number Can not Exceed 12 Characters</p>
								</div>
							</div>
				</div>
				<div class="form-row" id="driverName">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_DRIVER_NAME)%></div>
					<div class="input-field">
						<input name="driverName" id="drivername">
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
						<div id="driverNameLen_pop" class="helppop"
							style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Driver Name Length Can Not Exceed 35</p>
							</div>
						</div>
						
				</div>
				<div class="form-row" id="amountToBank">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_AMOUNT_TO_BANK)%></div>
					<div class="input-field">
						<input name="amountToBank" id="bankAmount" class="amount">
					</div>
					<span id="amountToBankValid"style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="amountToBank_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Amount To bank Can Contains Only Numbers</p>
							</div>
						</div>
						<div id="amountToBankValid_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Amount To bank Can Contains Only Numbers</p>
							</div>
						</div>
				</div>
				<div class="form-row" id="allowancesAmount">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_ALLOWANCES_AMOUNT)%></div>
					<div class="input-field">
						<input name="allowancesAmount" id="allowanceAmount" class="amount">
					</div>
					<span id="allowancesValid"style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="allowances_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Allowance Amount must Contains Only Numbers</p>
							</div>
						</div>
				</div>
			</div>
			<div class="separator" style="height: 80px;"></div>
			<div class="fieldset" style="height: 80px;">
			<div class="form-row" id="businessName">
					<div class="label"><%=Msg.get(MsgEnum.DELIVERY_NOTE_BUSINESS_NAME_LABEL)%></div>
					<div class="input-field">
						<input name="businessName" id="businessname">
					</div>
					<div id="business-name-suggestions" class="business-name-suggestions"></div>
					<span id="businessNameValid"
						style="float: left; position: absolute; margin-left:5px; margin-top:5px"></span>
					<div id="businessName_pop" class="helppop" style="display: block;"
						aria-hidden="false">
						<div id="namehelp" class="helpctr"
							style="float: left; margin-left: 3px;">
							<p><%=Msg.get(MsgEnum.VALIDATE_CUSTOMER_BUSINESS_NAME) %></p>
						</div>
					</div>
				</div>
				<div class="form-row" id="meterReading">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_METER_READING_LABEL)%></div>
					<div class="input-field">
						<input name="meterReading" id="meterreading">
					</div>
					<span id="meterReadingValid"style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="meterReading_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Meter reading can contain only numbers and should be greater than startreading or meterreading,if exists</p>
							</div>
						</div>
				</div>
				<div class="form-row" id="remarks">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_REMARKS)%></div>
					<div class="input-field">
						<textarea style="float: left;" name="remarks" id="Remarks" class="amountRemarks"
							cols="30" rows="6"></textarea>
					</div>
					<span id="remarksValid"style="float: left; position: absolute; margin-left: 87px; margin-top: 5px"></span>
						<div id="remarks_pop" class="helppop"style="display: block; float: left; margin-left: 363px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 8px;">
								<p>Please Provide Account/Cheque Details</p>
							</div>
						</div>
				</div>
				<div class="form-row" id="startReading">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOKS_STARTING_READING)%></div>
					<div class="input-field">
						<input name="startingReading" id="startingReading">
					</div>
					<span id="startreadingValid"style="float: left; position: absolute; margin-left: 10px; margin-top: 5px"></span>
						<div id="startreading_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Starting Reading must Contains Only Numbers</p>
							</div>
						</div>
						<div id="startreadingLen_pop" class="helppop"style="display: block; float: left; margin-left: 280px;"
							aria-hidden="false">
							<div id="namehelp" class="helpctr"
								style="float: left; margin-left: 10px;">
								<p>Starting Reading Can not Exceed 6 Numbers</p>
							</div>
						</div>
				</div>
				<%-- <div class="form-row" id="endingReading">
					<div class="label"><%=Msg.get(MsgEnum.DAY_BOOK_ENDING_READING)%></div>
					<div class="input-field">
						<input name="endingReading" id="endingReading">
					</div>
				</div> --%>
				<div class="form-row">
					<div class="input-field">
						<input name="action" value="temp-day-book-save" type="hidden">
					</div>
				</div>
				<div class="seperator" style="height: 40px;"></div>
			</div>
		</div>
	</form>
<div id="page-buttons " class="page-buttons test">
		<div id="button-save" class="ui-btn btn-save">Save</div>
			<div id="button-update" class="ui-btn btn-update">Update</div>
		<div id="action-clear" class="ui-btn btn-clear">Clear</div>
		<div id="action-cancel" class="ui-btn btn-cancel">Cancel</div>
	</div>
</div>
<div id="search-results-container2" class="ui-container search-results-container">
	<div class="ui-content">
		<div id="search-results-list" class="green-results-list" style="height: 282px; float:left; width:100%;">
		  </div>
			<div class="green-footer-bar">
		</div>
	</div>
</div>
<div id="employee-view-dialog" style="display: none;"
	title="Employee Details">
	<div id="employee-view-container"></div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$('.helppop').hide();
	$('.test').hide();
	$('#button-update').hide();
	//$('#search-results-container2').hide();
});
	 TempDayBookHandler.load();
	$('#vehicleNo').hide();
	$('#driverName').hide();
	$('#remarks').hide();
	$('#allowanceType').hide();
	$('#amountToBank').hide();
	$('#startReading').hide();
	$('#allowancesAmount').hide();
	$('#businessName').hide();
	$('#meterReading').hide();
</script>