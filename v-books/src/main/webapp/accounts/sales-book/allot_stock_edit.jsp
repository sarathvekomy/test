<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.vekomy.vbooks.accounts.dao.SalesBookDao"%>
<%@page import="com.vekomy.vbooks.product.command.ProductCommand"%>
<%@page import="com.vekomy.vbooks.hibernate.model.VbSalesBookProducts"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="com.vekomy.vbooks.security.User"%>
<%@page import="com.vekomy.vbooks.util.Msg.MsgEnum"%>
<%@page import="com.vekomy.vbooks.util.OrganizationUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.vekomy.vbooks.util.*"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.ListIterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%
	User user= (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String currencyFormat = user.getOrganization().getCurrencyFormat();
	VbSalesBookProducts salesBookProducts = null;
	String productName = null;
	String batchNumber = null;
	Integer availableQty = new Integer(0);
	try {
		ApplicationContext hibernateContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		SalesBookDao salesBookDao = (SalesBookDao) hibernateContext.getBean("accountsSalesBookDao");
		if (salesBookDao != null) {
			int id = Integer.parseInt(request.getParameter("id"));
			salesBookProducts = salesBookDao.getSalesBookProduct(id , user.getOrganization());
			productName = salesBookProducts.getProductName();
			batchNumber = salesBookProducts.getBatchNumber();
			availableQty = salesBookDao.getAvailableQty(productName, batchNumber, user.getOrganization());
		}
	} catch (Exception exx) {
		exx.printStackTrace();
	}
%>
<div id="allot-stock-edit-form-container" title="Edit AllotStock">
	<div class="ui-content form-panel full-content">
			<form id="allot-stock-edit-form" style="height: 280px;">
				 <div class="fieldset-row" style="margin-top: 10px;">
					<div class="fieldset" style="height: 120px;">
					<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_PRODUCT_NAME_LABEL)%>
							</div>
							<div class="input-field">
								<input name="productName" id="productName" readonly="readonly" style="border: none; background-color:inherit;" value="<%=productName%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_BATCH_NUMBER_LABEL)%>
							</div>
							<div class="input-field">
								<input name="batchNumber" id="batchNumber" readonly="readonly" style="border: none; background-color:inherit;" value="<%=batchNumber%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_PREVIOUS_CLOSING_STOCK_LABEL)%>
							</div>
							<div class="input-field">
								<input name="qtyClosingBalance" id="qtyClosingBalance" readonly="readonly" style="border: none; background-color:inherit;" value="<%=salesBookProducts.getQtyClosingBalance()%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_CLOSING_STOCK_LABEL)%>
							</div>
							<div class="input-field">
								<input name="closingStock"id="closingStock"  readonly="readonly" style="border: none; background-color:inherit;" value="<%=salesBookProducts.getQtyClosingBalance()%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_RETURN_QTY)%>
							</div>
							<div class="input-field">
								<input name="returnQty" id="returnQty" readonly="readonly" style="border: none; background-color:inherit;" value="<%=salesBookProducts.getQtyToFactory()%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_AVAILABLE_QUANTITY_LABEL)%></div>
							<div class="input-field">
								<input name="availableQuantity" id="availableQuantity" readonly="readonly" style="border: none; background-color:inherit;" value="<%=availableQty%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_ALLOTMENT_LABEL)%></div>
							<div class="input-field">
								<input name="qtyAllotted" id="qtyAllotted" value="<%=salesBookProducts.getQtyAllotted()%>">
							</div>
						</div>
						<div class="form-row" style="margin-bottom: 10px;">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_OPENING_STOCK_LABEL)%></div>
							<div class="input-field">
								<input class="qtyOpeningBalance" name="qtyOpeningBalance" id="qtyOpeningBalance" readonly="readonly" style="border: none; background-color:inherit;" value="<%=salesBookProducts.getQtyOpeningBalance()%>">
							</div>
						</div>
						 <div class="form-row">
							<div class="label"><%=Msg.get(MsgEnum.ACCOUNTS_REMARKS_LABEL)%></div>
							<div class="input-field">
							<%if(salesBookProducts.getRemarks() == null) { %>
							<textarea rows="3" cols="40" name="remarks" id="remarks"
									style="resize: none;"><%=""%></textarea>
							<%} else {%>
								<textarea rows="3" cols="40" name="remarks" id="remarks"
									style="resize: none;"><%=salesBookProducts.getRemarks()%></textarea>
									<%} %>
							</div>
						</div> 
						<input name="action" value="edit-allot-stock" type="hidden" id="allotStockAction">
			            <input name="id" value="<%=salesBookProducts.getId()%>" type="hidden" id="salesBookProductId">
				</div> 
			</div>
				<div class="fieldset-row"></div>
			</form>
	</div>
</div>
<script type="text/javascript">
SalesBookHandler.editAllotStock();
SalesBookHandler.setOpeningStock();
$('.helppop').hide();
</script>

