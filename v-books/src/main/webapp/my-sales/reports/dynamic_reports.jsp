<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="javax.jws.soap.SOAPBinding.Use"%>
<%@page import="com.vekomy.vbooks.organization.dao.OrganizationDao"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.Msg"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="com.vekomy.vbooks.util.DropDownUtil"%>
<%@page import="com.vekomy.vbooks.util.DateUtils"%>

<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
%>
<div id="reports-add-form-container" title="Sales Wise Report"
	style="overflow: auto;">
	<!--<div class="green-title-bar">
		<div class="green-title-bar">
			<div class="green-title-bar2"></div>
		</div>
	</div>-->
	<div class="ui-content form-panel full-content">
		<form id="dynamic-report-form" style="height: 150px;">
			<div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
					<div class="form-row">
						<div class="label">Types
						</div>
						<div class="input-field">
							<Select name="type" id="type" class="mandatory">
							<option value="select">select</option>
							<option value="Non-Transaction">Dafault Types</option>
							<option value="Transaction types">Transaction Types</option>
							</Select>
						</div>
					</div>
					<div class="fieldset" style="height: 60px;margin-left:350px;margin-top: -30px">
					<div class="form-row"id="transactionTypes" style="display: none;">
						<div class="label">Transaction Types</div>
						<div class="input-field">
							<Select name="transactionType" id="transactionType" class="mandatory">
							<option value="select">select</option>
							<option value="Delivery Note">DN</option>
							<option value="Sales Return">SR</option>
							<option value="Journal">JOU</option>
							<option value="Cash Collections">CC</option>
							</Select>
						</div>
					</div>
					</div>
					<div class="form-row" id="dnInputFields" style="display: none;">
					<div class="label">Report Criteria</div>
					<table id="dnTable"style="width:550px;margin-left:132px;margin-top: -20px"border=1>
					<tr><th>Field Name</th>
					<th style="width:100px"id="inputCheck">Input Field</th>
					<th  style="width:100px" id="outputCheck"> Output Field</th>
					<th  style="width:100px"  colspan=2> Criteria
					</th>
					<tr id="createdOn">
					<td >Start Date</td>
					<td><input type="checkbox" id="startDateCheck"/></td>
					<td><input type="checkbox"id="startDateOutputCheck"/></td>
					  <td class="criteria"><select class="criteriadrop" style="width: 60px" disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					  </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px" readonly="readonly"/></td>
 				 </tr>
 				 <tr id="endDate">
					<td>End Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px" disabled="disabled">
				 	  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					  </select>
				 </td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id ="createdBy">
					<td>Employee Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width:60px" disabled="disabled">
						 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					    </select>
					 </td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="businessName">
					<td>Business Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px" disabled="disabled">
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
                    </select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="locality">
					<td>Business Locality</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="invoiceNo">
					<td>Invoice Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="productName">
					<td>Product Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal"  type="text" style="width:60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="batchNumber">
					<td>Batch Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="productQty">
					<td>Quantity Sold</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr  id="bonusQty">
					<td>Bonus Quantity</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop" style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width:60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="totalPayable">
					<td >Total Payable</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr  id="totalRecieved">
					<td>Total Recieved</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr  id="previousCredit">
					<td>Credit Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr  id="presentAdvance">
					<td>Advance Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
					</table>
					</div>
					<div class="form-row" id="srFields" style="display: none;">
					<div class="label">Input Filters</div>
					<table style="width:550px;margin-left:132px;margin-top: -20px"border=1>
					<tr><th>Field Name</th>
					<th style="width:100px">Input Field</th>
					<th  style="width:100px"> Output Field</th>
					<th>Criteria</th>
					</tr>
					<tr  id="createdOn">
					<td>Start Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					</select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="endDate">
					<td>End Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
				    <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
				 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="createdBy">
					<td>Employee Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="businessName">
					<td>Business Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="locality">
					<td>Business Locality</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="invoiceNo">
					<td>Invoice Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					  </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="productName">
					<td>Product Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="batchNumber">
					<td>Batch Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="damaged">
					<td>Quantity Damaged</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="resalable">
					<td>Resalable Quantity</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
					</table>
					</div>
					<div class="form-row" id="JouFields" style="display: none;">
						<div class="label">Input Filters</div>
					<table style="width:550px;margin-left:132px;margin-top: -20px"border=1>
					<tr><th>Field Name</th>
					<th style="width:100px">Input Field</th>
					<th  style="width:100px"> Output Field</th>
					<th>Criteria</th>
					</tr>
					<tr id="createdOn">
					<td>Start Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="endDate">
					<td>End Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="createdBy">
					<td>Employee Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="businessName">
					<td>Business Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="locality">
					<td>Business Locality</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="invoiceNo">
					<td>Invoice Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="journalType">
					<td>Journal Type</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="description">
					<td>Journal Description</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="amount">
					<td>Journal Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
				 <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
					</table>
					</div>
					<div class="form-row" id="CCFields" style="display: none;">
					<div class="label">Input Filters</div>
					<table style="width:550px;margin-left:132px;margin-top: -20px"border=1>
					<tr><th>Field Name</th>
					<th style="width:100px">Input Field</th>
					<th  style="width:100px"> Output Field</th>
					<th>Criteria</th>
					</tr>
					<tr id="createdOn">
					<td>Start Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop" style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="endDate">
					<td>End Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="createdBy">
					<td>Employee Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="businessName">
					<td>Business Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="locality">
					<td>Business Locality</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="invoiceNo">
					<td>Invoice Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> 
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="totalPayable">
					<td>Total Payable</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="totalRecieved">
					<td>Total Recieved</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="previousCredit">
					<td>Credit Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="advanceAmount">
					<td>Advance Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px" disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
					</table>
					</div>
					<div class="form-row" id="commonFields" style="display: none;">
					<div class="label">Input Filters</div>
						<table style="width:550px;margin-left:132px;margin-top: -20px"border=1>
					<tr><th>Field Name</th>
					<th style="width:100px">Input Field</th>
					<th  style="width:100px"> Output Field</th>
					<th>Criteria</th>
					</tr>
					<tr id="createdOn">
					<td>Start Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop" style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="endDate">
					<td>End Date</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="createdBy">
					<td>Employee Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="businessName">
					<td>Business Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="locality">
					<td>Business Locality</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"><option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="invoiceNo">
					<td>Invoice Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> 
					 <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="totalPayable">
					<td>Total Payable</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="totalRecieved">
					<td>Total Recieved</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="previousCredit">
					<td>Credit Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="advanceAmount">
					<td>Advance Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
				 <td><select class="criteriadrop"style="width: 60px" disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="productName">
					<td>Product Name</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal"  type="text" style="width:60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="batchNumber">
					<td>Batch Number</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="LIKE">LIKE</option>
						  <option value="!=">!=</option>
						  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr  id="productQty">
					<td>Quantity Sold</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr  id="bonusQty">
					<td>Bonus Quantity</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop" style="width: 60px"disabled="disabled">
					  <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option>
					 </select></td>
					  <td><input class="criteriaVal" type="text" style="width:60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="damaged">
					<td>Quantity Damaged</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="resalable">
					<td>Resalable Quantity</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				 <tr id="dayBookType">
					<td>Expenditure Type</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="valueOne">
					<td>Expenditure Amount</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
 				  <tr id="valueThree">
					<td>Expenditure Details</td>
					<td><input type="checkbox"/></td>
					<td><input type="checkbox"/></td>
					 <td><select class="criteriadrop"style="width: 60px"disabled="disabled"> <option value="<"><</option>
					  <option value=">">></option>
					  <option value="==">==</option></select></td>
					  <td><input class="criteriaVal"  type="text" style="width: 60px"readonly="readonly"/></td>
 				 </tr>
					</table>
					</div>
			</div>
			</div>
			<%-- <div class="fieldset-row" style="margin-top: 10px;">
				<div class="fieldset" style="height: 60px;">
				<div class="form-row" id="numRelated" style="display: none;">
						<div class="label">Criteria</div>
						<div class="input-field">
							<Select name="type" id="numCriteria" class="mandatory criteria" style="width:60px">
							<option value="select">select</option>
							<option value=">">></option>
							<option value="<"><</option>
							<option value="==">==</option>
							<option value="!=">!=</option>
							</Select>
						</div>
					</div>
					<div class="form-row" id="textRelated" style="display: none;">
						<div class="label">Criteria</div>
						<div class="input-field">
							<Select name="type" id="textCriteria" class="mandatory criteria"style="width:60px">
							<option value="select">select</option>
							<option value="==">==</option>
							<option value="!=">!=</option>
							<option value="like">LIKE</option>
							</Select>
						</div>
					</div>
					</div>
			</div> --%>
			<!-- <div class="fieldset-row" style="margin-left: 255px;">
				<div class="fieldset" style="height: 60px;">
				<div class="form-row" id="criteriaVal" style="margin-top: -60px;margin-left: 260px;display: none;">
						<div class="input-field">
							<input name="value" id="value" class="mandatory" style="width:90px;">
							</div>
					</div>
					<div id="page-buttons" class="page-buttons">
					<div id="sales-action-save" class="ui-btn btn-save" style="margin-left:314px;margin-top:-62px;display: none;">Save</div>
					</div>
				</div>
				</div> -->
				<div class="form-row">
				<div class="input-field">
					<input id="reportFormat" name="reportFormat" type="hidden">
					<%-- <input id = "organization" name="organization" type="hidden" value = "<%=user.getOrganization()%>"> --%>
				</div>
			</div>
			<div class="form-row">
				<div class="input-field">
					<input id="inputs" name="inputs" type="hidden">
					<%-- <input id = "organization" name="organization" type="hidden" value = "<%=user.getOrganization()%>"> --%>
				</div>
			</div>
			<div class="form-row">
				<div class="input-field">
					<input id="outputs" name="outputs" type="hidden">
					<%-- <input id = "organization" name="organization" type="hidden" value = "<%=user.getOrganization()%>"> --%>
				</div>
			</div>
			<div class="form-row">
				<div class="input-field">
					<input id="criteria" name="criteria" type="hidden">
					<%-- <input id = "organization" name="organization" type="hidden" value = "<%=user.getOrganization()%>"> --%>
				</div>
			</div>
		</form>
		<div style="float: left; width: 10px;"></div>
		<div id="page-buttons" class="page-buttons"
			style="margin-left: 130px; float:left;margin-top:280px; position: relative;">
			<div id="sales-action-show" class="ui-btn btn-show">Show</div>
			<div id="sales-action-pdf" class="ui-btn btn-print">Download PDF</div>
			<div id="sales-action-xls" class="ui-btn btn-print">Download CSV</div>
			<div id="sales-action-clear" class="ui-btn btn-clear">Clear</div>
			<div id="sales-action-cancel" class="ui-btn btn-cancel">Cancel</div>
		</div>
		<!-- <div class="fieldset-row" style="margin-top: 100px;">
			<div id="report-container"></div>
		</div> -->
		<div id="report-show-dialog" style="display: none; overflow-y: hidden;"
			title="Sales Wise Report">
			<div id="report-container"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		$('.helppop').hide();
		dynamicReportsHandler.load();
		$('.label').click(function(){
			 if($(this).hasClass("multiselect-on")){
				 $(this).removeClass("multiselect-on");
			 }else{
				 $(this).addClass("multiselect-on");
			 }
		});
		/*$('.helppop').hide();
		$(document).ready(function() {
			$('.helppop').hide();
			CustomerWiseReportHandler.load();
			CustomerWiseReportHandler.registorReportShowEvents();
			  var dates = $("#startDate,#endDate" ).datepicker({
			        defaultDate: "+1w",
			        dateFormat : 'dd-mm-yy',
			        changeMonth: true,
			        numberOfMonths: 1,
					changeMonth: true,
				    changeYear : true,
					numberOfMonths: 1,
					maxDate: 0,

			        onSelect: function( selectedDate ) {
			        	if($('#reportType').val() != "Weekly"){
			        		 if(this.id == "startDate") {
					        		var option = "minDate",
					    	                instance = $( this ).data( "datepicker" ),
					    	                date = $.datepicker.parseDate(
					    	                    instance.settings.dateFormat ||
					    	                    $.datepicker._defaults.dateFormat,
					    	                    selectedDate, instance.settings );
					    	            dates.not( this ).datepicker( "option", option, date );
					        	} else {
					        			var startDate = $('#startDate').val();
										var endDate = $('#endDate').val();
										var parseStartDate = $.datepicker.parseDate( 'dd-mm-yy', startDate);
										var parseEndDate = $.datepicker.parseDate( 'dd-mm-yy', endDate );
										if(parseStartDate > parseEndDate) {
											$('#startDate').val(selectedDate);
										}
								}
			        	}
			        	
			        	if($('#reportType').val() == "Weekly"){
			        		 if(this.id == "startDate") {
			        			 var startDate = $('#startDate').val();
					        		var parseStartDate = $.datepicker.parseDate( 'dd-mm-yy', startDate);
					        		parseStartDate.setDate(parseStartDate.getDate()+7);
					        		 var date = new Date(parseStartDate),
					        	        mnth = ("0" + (date.getMonth()+1)).slice(-2),
					        	        day  = ("0" + date.getDate()).slice(-2);
					        	    var parsedEndDate = [ day, mnth, date.getFullYear() ].join("-");
					        	    $("#endDate").datepicker( "setDate",  parsedEndDate.toString());
			        		 }else{
			        			 var endDate = $('#endDate').val();
					        		var parseEndDate = $.datepicker.parseDate( 'dd-mm-yy', endDate);
					        		parseEndDate.setDate(parseEndDate.getDate()-7);
					        		 var date = new Date(parseEndDate),
					        	        mnth = ("0" + (date.getMonth()+1)).slice(-2),
					        	        day  = ("0" + date.getDate()).slice(-2);
					        	    var parsedStartDate = [ day, mnth, date.getFullYear() ].join("-");
					        	    $("#startDate").datepicker( "setDate",  parsedStartDate.toString());
			        		 }
			        		
						 }
			        },
			        beforeShow : function(input, inst){
			        	 if($('#reportType').val() == "Monthly"){
							 $(inst.dpDiv).addClass('calendar-off');  
						 }else{
							 $(inst.dpDiv).removeClass('calendar-off');  
						 }
			        },
			        onClose: function(dateText, inst) {
						 if($('#reportType').val() == "Monthly"){
							 var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
							 var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
							 $(this).datepicker('setDate', new Date(year, month, 1));
						 }else{
								 $('#startDate').datepicker({
									 altFormat : "dd-mm-yy",
									 onSelect: function(dateText, inst){
										 var option = "minDate",
					    	                instance = $( this ).data( "datepicker" ),
					    	                date = $.datepicker.parseDate(
					    	                    instance.settings.altFormat ||
					    	                    $.datepicker._defaults.altFormat,
					    	                    selectedDate, instance.settings );
					    	            $( this ).datepicker( "option", option, date );
									  }
								 });
						 }
					 }
			    }); 
		}); */
	});
</script>
<style>
   .divTable
    {
        display: table;
        width:350px;
        background-color:#eee;
        border:1px solid  #666666;
        border-spacing:5px;
        margin-left:60px;
        border-radius:5.5px/*cellspacing:poor IE support for  this*/
       /* border-collapse:separate;*/
    }

    .divRow
    {
       display:table-row;
       width:auto;

    }

    .divCell
    {
        float:left;/*fix for  buggy browsers*/
        display:table-column;
        width:200px;
        background-color:#ccc;

    }
    .coloumn2{
    margin-top:30px;}



</style>