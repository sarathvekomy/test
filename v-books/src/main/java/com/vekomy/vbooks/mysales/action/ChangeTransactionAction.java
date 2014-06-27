/**
 * com.vekomy.vbooks.mysales.action.ChangeTransactionAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: June 18, 2013
 */
package com.vekomy.vbooks.mysales.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAllowancesCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookAmountCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookBasicInfoCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookProductsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestDayBookVehicleDetailsCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestDeliveryNoteResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestJournalCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestProductResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnCommand;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnResult;
import com.vekomy.vbooks.mysales.command.ChangeRequestSalesReturnsResult;
import com.vekomy.vbooks.mysales.dao.ChangeTransactionDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;
import com.vekomy.vbooks.util.Msg.MsgEnum;

/**
 * This action class is responsible to process the change transaction activity.
 * 
 * @author ankit
 * 
 */
public class ChangeTransactionAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ChangeTransactionAction.class);
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IResult process(Object form) throws Exception {
		//ChangeTransactionCommand changeTransactionCommand = null;
		ChangeRequestDeliveryNoteCommand deliveryNoteCommand = null;
		ChangeRequestDeliveryNoteProductCommand deliveryNoteProducts = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		ChangeTransactionDao changeTransactionDao = (ChangeTransactionDao) getDao();
		if (form instanceof ChangeRequestDeliveryNoteCommand) {
			deliveryNoteCommand = (ChangeRequestDeliveryNoteCommand) form;
			_logger.debug("deliveryNoteCommand: {}", deliveryNoteCommand);
			String action = deliveryNoteCommand.getAction();
			//start of delivery note Action
			if ("save-deliverynote-info".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("deliverynote-info", deliveryNoteCommand);
				resultSuccess.setMessage("Saved Successfully");
			}else if ("save-product-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String listOfObjects = request.getParameter("listOfProductObjects");
				String businessName = request.getParameter("businessName");
				String invoiceName = request.getParameter("invoiceName");
				String invoiceNumber = request.getParameter("invoiceNumber");
				String formChangedValues = request.getParameter("formChangedValues");
				String [] formChangedValuesItems = formChangedValues.split(",");
				String rowSteps[] = listOfObjects.split(",");
				List<ChangeRequestDeliveryNoteCommand> deliveryNoteList = new ArrayList<ChangeRequestDeliveryNoteCommand>();
				for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
					if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
						deliveryNoteCommand = new ChangeRequestDeliveryNoteCommand();
						String rowData[] = rowSteps[rowStep].split("\\|");
						String productQuantity = rowData[0].trim();
						String batchNumber = rowData[1].trim();
						String bonus = rowData[2].trim();
						String bonusReason = rowData[3].trim();
						String productName = rowData[4].trim();
						String productCost = rowData[5].trim();
						if (!StringUtils.isEmpty(productQuantity)) {
							deliveryNoteCommand.setProductQuantity(StringUtil.format(Integer.parseInt(productQuantity)));
						}
						if (!StringUtils.isEmpty(productName)) {
							deliveryNoteCommand.setProductName(productName);
						}
						if (!StringUtils.isEmpty(productCost)) {
							deliveryNoteCommand.setProductCost(StringUtil.floatFormat(Float.parseFloat(productCost)));
						}
						if (!StringUtils.isEmpty(bonus)) {
							deliveryNoteCommand.setBonusQuantity(StringUtil.format(Integer.parseInt(bonus)));
						}
						if (!StringUtils.isEmpty(bonusReason)) {
							deliveryNoteCommand.setBonusReason(bonusReason);
						}
						if (!StringUtils.isEmpty(batchNumber)) {
							deliveryNoteCommand.setBatchNumer(batchNumber);
						}
						deliveryNoteCommand.setBusinessName(businessName);
						deliveryNoteCommand.setInvoiceName(invoiceName);
						deliveryNoteCommand.setInvoiceNo(invoiceNumber);
						deliveryNoteList.add(deliveryNoteCommand);
						session.setAttribute("product-data", deliveryNoteList);
						session.setAttribute("save-form-changed-values", formChangedValuesItems);
						resultSuccess.setMessage("Saved Successfully");
					}
				}
			} else if ("go-for-payments".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				ChangeRequestDeliveryNoteCommand command = new ChangeRequestDeliveryNoteCommand();
				String formChangedValues=request.getParameter("formChangedValues");
				String [] formChangedValuesItems = formChangedValues.split(",");
				command.setBusinessName(request.getParameter("businessName"));
				command.setInvoiceName(request.getParameter("invoiceName"));
				command.setInvoiceNo(request.getParameter("invoiceNumber"));
				session.setAttribute("go-for-payments", command);
				session.setAttribute("form-changed-DN-payment-values", formChangedValuesItems);
				resultSuccess.setMessage("Saved Successfully");
			} else if ("save-payment-delivery-note".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				changeTransactionDao.savePayments((ChangeRequestDeliveryNoteCommand)session.getAttribute("go-for-payments"), 
						(String[])session.getAttribute("form-changed-DN-payment-values"),(ChangeRequestDeliveryNoteProductCommand)session.getAttribute("delivery-note-product"), 
						getOrganization(), getUsername());
				
				// Firing alerts.
				String defaultToRecipient = getOrganization().getSuperUserName();
				AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
						OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_RAISED_MESSAGE));
				
				_logger.info("Firing an alert for sales executive deliveryNote change request for payments");
				resultSuccess.setMessage("Saved Successfully");
				//Deleting data from session.
				removeDataFromSession();
			}
			else if ("save-deliverynote".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				changeTransactionDao.saveDeliveryNote((ChangeRequestDeliveryNoteProductCommand) session.getAttribute("delivery-note-product"),
						(String[])session.getAttribute("save-form-changed-values"),(List<ChangeRequestDeliveryNoteCommand>) session.getAttribute("product-data"), getUsername(),getOrganization());
				
				// Firing alerts.
				String defaultToRecipient = getOrganization().getSuperUserName();
				AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
						OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_RAISED_MESSAGE));
				
				_logger.info("Firing an alert for sales executive deliveryNote change request for payments");
				
				// removing the session data.
				removeDeliveryNoteDataFromSession();
				resultSuccess.setMessage("Saved Successfully");
			} 
			else if ("get-existed-grid-data".equals(action)) {
				session = request.getSession(Boolean.TRUE);
				String businessName = request.getParameter("businessName");
				Integer deliveryNoteId = Integer.valueOf(request.getParameter("deliveryNoteId"));
				Integer salesId = Integer.valueOf(request.getParameter("salesId"));
				List<ChangeRequestProductResult> productResultList = changeTransactionDao.getExistedBusinessNameGridData(salesId,deliveryNoteId,getUsername(), businessName, getOrganization());
				session.setAttribute("grid-data", productResultList);
				resultSuccess.setMessage("Fetched Records Successfully");
				resultSuccess.setData(productResultList);
			} else if ("change-dn-transaction-request".equals(action)) {
				List<ChangeRequestDeliveryNoteResult> list = changeTransactionDao.getDeliveryNoteResultsOnCriteria(getOrganization(), getUsername());
				if (list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			}
			else if ("approve-delivery-note-cr".equals(action)) {
				Integer deliverNoteCRId = Integer.parseInt(request.getParameter("deliverNoteCRId"));
				String status = request.getParameter("status");
				changeTransactionDao.getApprovedDeliveyNoteCR(deliverNoteCRId, status, getOrganization(), getUsername());
				
				// For Alerts.
				String description = null;
				if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
					description = Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_APPROVED_MESSAGE);
				} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
					description = Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_DECLINED_MESSAGE);
				}
				String defaultToRecipient = changeTransactionDao.getCreatedBy(deliverNoteCRId, OrganizationUtils.CR_TYPE_DELIVERY_NOTE);
				AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
						OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, description);
				
				_logger.debug("Firing an alert for sales executive salesReturn deliveryNote Change Request");
				
				resultSuccess.setMessage("Delivery Note Approved Successfully");
			}
			else if ("get-delivery-note-id".equals(action)) {
	        	String invoiceNumber=request.getParameter("invoiceNumber");
				resultSuccess.setData((Integer) changeTransactionDao.getDeliveryNoteBasedOnInvoiceNo(getOrganization(), invoiceNumber,getUsername()));
				resultSuccess.setMessage("Delivery Note Id Based on Invoice No. fetched successfully");
			} 
			 else if ("validate-SE-DN-change-request".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
					resultSuccess.setData((String) changeTransactionDao.validateSEChangeRequest(invoiceNumber, getOrganization()));
					resultSuccess.setMessage("SE with Change Request Validated Successfully.");
				}
			 else if ("get-delivery-note-creation-time".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
		        	Integer deliveryNoteId=Integer.valueOf(request.getParameter("deliveryNoteId"));
					resultSuccess.setData((String) changeTransactionDao.fetchDeliveryNoteCreationTime(deliveryNoteId,invoiceNumber, getOrganization()));
					resultSuccess.setMessage("Fetched Difference between Mins of DeliveryNote creation and Current Time .");
				}
			//end of delivery note dao calls
			else if("change-sales-transaction-request".equals(action)){
				List<ChangeRequestSalesReturnResult> salesReturnResult=changeTransactionDao.getSalesReturnsOnLoad(getUsername(), getOrganization());
				if(salesReturnResult.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(salesReturnResult);
					resultSuccess.setMessage("Search Successfull");
				}
		        } else if ("get-sales-return-grid-data".equals(action)) {
					//session = request.getSession(Boolean.TRUE);
					String businessName = request.getParameter("businessName");
					Integer salesReturnId = Integer.valueOf(request.getParameter("salesReturnId"));
					List<ChangeRequestSalesReturnsResult> salesReturnsResult = changeTransactionDao.getSalesReturnGridData(salesReturnId,getUsername(), businessName, getOrganization());
					if (salesReturnsResult == null) {
						resultSuccess.setMessage("No Records Found");
					} else {
						resultSuccess.setMessage("Fetched Records Successfully");
						resultSuccess.setData(salesReturnsResult);
					}
				} else if ("search-delivery-note-change-request-dashboard".equals(action)) {
					List<ChangeRequestDeliveryNoteResult> list=changeTransactionDao.getDeliveryNoteCrResults(getUsername(), getOrganization());
					if(list.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
				} else if ("save-sales-return-products".equals(action)) {
		        	session = request.getSession(Boolean.TRUE);
					String listOfObjects = request.getParameter("listOfProductObjects");
					String businessName = request.getParameter("businessName");
					String invoiceName = request.getParameter("invoiceName");
					String invoiceNumber = request.getParameter("invoiceNumber");
					String grandTotalCost = request.getParameter("grandTotalCost");
					String description = request.getParameter("description");
					String formChangedValues = request.getParameter("formChangedFields");
					String [] formChangedValuesItems = formChangedValues.split(",");
					String rowSteps[] = listOfObjects.split(",");
					List<ChangeRequestSalesReturnCommand> salesReturnList = new ArrayList<ChangeRequestSalesReturnCommand>();
					ChangeRequestSalesReturnCommand command = null;
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							command = new ChangeRequestSalesReturnCommand();
							String rowData[] = rowSteps[rowStep].split("\\|");
							String damaged = rowData[0].trim();
							String resalable = rowData[1].trim();
							String cost = rowData[2].trim();
							String returnQty = rowData[3].trim();
							String totalCost = rowData[4].trim();
							String batchNumber = rowData[5].trim();
							String productName = rowData[6].trim();
							if(!StringUtils.isEmpty(damaged)){
								command.setDamaged(damaged);
							}
							
							if(!StringUtils.isEmpty(resalable)){
								command.setResalable(resalable);
							}
							
							if(!StringUtils.isEmpty(cost)){
								command.setCost(cost);
							}
							
							if (!StringUtils.isEmpty(returnQty)) {
								command.setTotalQty(returnQty);
							}
							
							if(!StringUtils.isEmpty(totalCost)){
								command.setTotalCost(totalCost);
							}
							
							command.setProductName(productName);
							command.setBatchNumber(batchNumber);
							command.setBusinessName(businessName);
							command.setInvoiceName(invoiceName);
							command.setGrandTotalCost(grandTotalCost);
							command.setInvoiceNo(invoiceNumber);
							command.setCrDescription(description);
							salesReturnList.add(command);
						}
					}
					session.setAttribute("save-sales-return-products", salesReturnList);
					session.setAttribute("save-form-change-request-fields", formChangedValuesItems);
				}  else if ("validate-SE-SR-change-request".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
					resultSuccess.setData((String) changeTransactionDao.validateSESalesReturnChangeRequest(invoiceNumber, getOrganization()));
					resultSuccess.setMessage("SE with Change Request Validated Successfully.");
				} else if ("search-sales-return-change-request-dashboard".equals(action)) {
					List<ChangeRequestSalesReturnResult> list = changeTransactionDao
						.getSalesReturnCrResults(getUsername(),
								getOrganization());
					if (list.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
			   } else if ("save-sales-returns".equals(action)) {
				changeTransactionDao.saveSalesReturns((List<ChangeRequestSalesReturnCommand>) session.getAttribute("save-sales-return-products"),
						(String[]) session.getAttribute("save-form-change-request-fields"), getOrganization(), getUsername());
				resultSuccess.setMessage("Saved Successfully");
				
				// Firing alerts.
				String defaultToRecipient = getOrganization().getSuperUserName();
				AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
						OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN, Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_RAISED_MESSAGE));
				
				_logger.info("Firing an alert for sales executive sales return change request.");
				
				// Removing session data.
				session.removeAttribute("save-sales-return-products");
				session.removeAttribute("save-form-change-request-fields");
		       } else if ("approve-sales-return-cr".equals(action)) {
					Integer salesReturnCRId = Integer.parseInt(request.getParameter("salesReturnCRId"));
					String status = request.getParameter("status");
					changeTransactionDao.getApprovedSalesReturnCR(salesReturnCRId, status, getOrganization(), getDaoName());
					resultSuccess.setMessage("SalesReturn CR Approved Successfully");
					
					// For Alerts.
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_SALES_RETURNS_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = changeTransactionDao.getCreatedBy(salesReturnCRId, OrganizationUtils.CR_TYPE_SALES_RETURNS);
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_SALES_RETURN, description);
					
					_logger.info("Firing an alert for sales executive Sales Return change request.");
				}
		       else if ("get-sales-return-id".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
					resultSuccess.setData((Integer) changeTransactionDao.getSalesReturnBasedOnInvoiceNo(getOrganization(), invoiceNumber,getUsername()));
					resultSuccess.setMessage("Delivery Note Id Based on Invoice No. fetched successfully");
				} 
		       else if ("get-sales-return-creation-time".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
		        	Integer salesReturnId=Integer.valueOf(request.getParameter("salesReturnId"));
					resultSuccess.setData((String) changeTransactionDao.fetchSalesReturnCreationTime(salesReturnId,invoiceNumber, getOrganization()));
					resultSuccess.setMessage("Fetched Difference between Mins of DeliveryNote creation and Current Time .");
				}
				//daybook dao calls
		        else if("change-daybook-transaction-request".equals(action)){
		        	List<ChangeRequestDayBookResult> dayBook = changeTransactionDao.getDayBookChangeTransaction( getOrganization(), getUsername());
					if (dayBook.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(dayBook);
						resultSuccess.setMessage("Search Successfull");
					}
		        } else if ("get-day-book-grid-data".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
		        	String salesExecutive=request.getParameter("salesExecutive");
					session = request.getSession(Boolean.TRUE);
					List<ChangeRequestDayBookResult> dayBookResultList = changeTransactionDao.getGridData(salesExecutive,dayBookId,getUsername() , getOrganization());
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(dayBookResultList);
				} else if ("get-opening-balance".equals(action)) {
					Float openingBalance = changeTransactionDao.getOpeningBalance(getUsername() , getOrganization());
					if (openingBalance == null) {
						if (_logger.isErrorEnabled()) {
							_logger.error("No Data Found.");
						}
						resultSuccess.setMessage("No Data Found.");
						resultSuccess.setData(0.00);
					} else {
						resultSuccess.setData(openingBalance);
						resultSuccess.setMessage("Retrieved Data successfully");
					}
				} else if ("save-day-book-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String dayBookFormChangedValues = request.getParameter("dayBookFormChangedValues");
					String dayBookFormChangedValuesArray[] = dayBookFormChangedValues.split(",");
					String listOfObjects = request.getParameter("listOfProductObjects");
					String rowSteps[] = listOfObjects.split(",");
					List<ChangeRequestDayBookProductsCommand> dayBookList = new ArrayList<ChangeRequestDayBookProductsCommand>();
					for (int rowStep = 0; rowStep < rowSteps.length; rowStep++) {
						if ("" != rowSteps[rowStep] && rowSteps[rowStep].length() > 0) {
							ChangeRequestDayBookProductsCommand dayBookProduct = new ChangeRequestDayBookProductsCommand();
							String rowData[] = rowSteps[rowStep].split("\\|");
							String productName = rowData[0].trim();
							String openingStock = rowData[1].trim();
							String productToCustomer = rowData[2].trim();
							String productToFactory = rowData[3].trim();
							String closingStock = rowData[4].trim();
							String returnQty = rowData[5].trim();
							String batchNumber = rowData[6].trim();
							if (!StringUtils.isEmpty(productName)) {
								dayBookProduct.setProductName(productName);
							}
							if (!StringUtils.isEmpty(openingStock)) {
								dayBookProduct.setOpeningStock(openingStock);
							}
							if (!StringUtils.isEmpty(productToCustomer)) {
								dayBookProduct.setProductsToCustomer(productToCustomer);
							}
							if (!StringUtils.isEmpty(productToFactory)) {
								dayBookProduct.setProductsToFactory(productToFactory);
							}
							if (!StringUtils.isEmpty(closingStock)) {
								dayBookProduct.setClosingStock(closingStock);
							}
							if(!StringUtils.isEmpty(returnQty)){
								dayBookProduct.setReturnQty(returnQty);
							}
							if(!StringUtils.isEmpty(batchNumber)){
								dayBookProduct.setBatchNumber(batchNumber);
							}
							dayBookList.add(dayBookProduct);
							session.setAttribute("daybook-product-data", dayBookList);
							session.setAttribute("daybook-form-changed-data", dayBookFormChangedValuesArray);
							resultSuccess.setMessage("Saved Successfully");
						}
					}
				} else if ("save-daybook".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					changeTransactionDao.saveDayBook((ChangeRequestDayBookBasicInfoCommand)session.getAttribute("daybook-basic-info"), 
							(ChangeRequestDayBookAllowancesCommand)session.getAttribute("daybook-allowances"),(ChangeRequestDayBookAmountCommand) session.getAttribute("daybook-amount"),
							(List<ChangeRequestDayBookProductsCommand>) session.getAttribute("daybook-product-data"),(ChangeRequestDayBookVehicleDetailsCommand)session.getAttribute("vehicle-details"),
							(String[]) session.getAttribute("daybook-form-changed-data"),getOrganization(), getUsername(), request.getParameter("isReturn"));
					
					// Firing alerts.
					String defaultToRecipient = getOrganization().getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DAY_BOOK, Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_RAISED_MESSAGE));
					_logger.info("Firing an alert for sales executive DayBook Change Request");
					
					// Removing data from session.
					removeDayBookFromSession(session);
					resultSuccess.setMessage("Saved successfully");
				} else if ("validate-SE-DB-change-request".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
					resultSuccess.setData((String) changeTransactionDao.validateSEDayBookChangeRequest(dayBookId, getOrganization()));
					resultSuccess.setMessage("SE with Change Request for Day Book Validated Successfully.");
				} else if ("search-day-book-change-request-dashboard".equals(action)) {
					List<ChangeRequestDayBookResult> list=changeTransactionDao.getDayBookCrResults(getUsername(), getOrganization());
					if(list.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
				} else if ("approve-day-book-cr".equals(action)) {
					Integer dayBookCrId = Integer.parseInt(request.getParameter("dayBookCrId"));
					String status = request.getParameter("status");
					changeTransactionDao.getApprovedDayBookCR(dayBookCrId, status, getOrganization(), getUsername());
					resultSuccess.setMessage("Day Book CR Approved Successfully");
					
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DAY_BOOK_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = changeTransactionDao.getCreatedBy(dayBookCrId, OrganizationUtils.CR_TYPE_DAY_BOOK);
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DAY_BOOK, description);
					
					_logger.info("DayBook sales executive change request have been {}", status);
				}
				else if ("get-day-book-creation-time".equals(action)) {
		        	Integer dayBookId=Integer.valueOf(request.getParameter("dayBookId"));
					resultSuccess.setData((String) changeTransactionDao.fetchJournalCreationTime(dayBookId,getOrganization()));
					resultSuccess.setMessage("Fetched Difference between Mins of Journal creation and Current Time .");
				}
				//making journal dao class
		        else if("change-journal-transaction-request".equals(action)){
		        	List<VbJournal> journal = changeTransactionDao.getJournalChangeTransaction( getOrganization(), getUsername());
					if (journal.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(journal);
						resultSuccess.setMessage("Search Successfull");
					}
		        } else if ("validate-SE-Journal-change-request".equals(action)) {
		        	Integer journalId=Integer.valueOf(request.getParameter("journalId"));
		        	String invoiceNumber=request.getParameter("invoiceNumber");
					resultSuccess.setData((String) changeTransactionDao.validateSEJournalChangeRequest(invoiceNumber,journalId, getOrganization()));
					resultSuccess.setMessage("SE with Change Request for Journal Validated Successfully.");
				} else if("edit-journal".equals(action)) {
		        	String formChangedValues=request.getParameter("form-changed-value-journal-edit");
		        	ChangeRequestJournalCommand journalCommand=new ChangeRequestJournalCommand();
		        	journalCommand.setJournalType(request.getParameter("journalType"));
		        	journalCommand.setBusinessName(request.getParameter("businessName"));
		        	journalCommand.setInvoiceNo(request.getParameter("invoiceNo"));
		        	journalCommand.setAmount(request.getParameter("amount"));
		        	journalCommand.setInvoiceName(request.getParameter("invoiceName"));
		        	journalCommand.setDescription(request.getParameter("description"));
		        	journalCommand.setCrDescription(request.getParameter("crDescription"));
		        	String journalFormChangedValuesArray[] = formChangedValues.split(",");
		        	changeTransactionDao.saveJournalCR(journalCommand,journalFormChangedValuesArray,getOrganization(), getUsername());
					resultSuccess.setMessage("Saved successfully.");
					
					// Firing alerts.
					String defaultToRecipient = getOrganization().getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_RAISED_MESSAGE));
					_logger.info("Journal sales executive change request have been raised");
				}
		       else if ("get-all-journals-CR-for-dashboard".equals(action)) {
					List<VbJournalChangeRequest> list=changeTransactionDao.getJournalCrResults(getUsername(), getOrganization());
					if(list.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}
				} else if ("approve-journal-cr".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("journalId"));
					String status = request.getParameter("status");
					Boolean isApproved = changeTransactionDao.approveOrDeclineJournalCR(id, status, getOrganization(), getUsername());
					if(isApproved == Boolean.TRUE) {
						resultSuccess.setMessage("Journal have been approved.");
					} else {
						resultSuccess.setMessage("Journal have been declained.");
					}
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = changeTransactionDao.getCreatedBy(id, OrganizationUtils.CR_TYPE_JOURNAL);
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), getUsername(), defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, description);
					
					_logger.info("Journal sales executive changge request have been {}", status);
				} else if ("search-alert-dashboard".equals(action)) {
					/*List<VbSystemAlertsHistory> list=changeTransactionDao.getAlertsList(getOrganization());
					if(list.isEmpty()) {
						resultSuccess.setMessage("No Search Results Found");
					} else {
						resultSuccess.setData(list);
						resultSuccess.setMessage("Search Successfull");
					}*/
				}
				else if ("get-journal-id".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
					resultSuccess.setData((Integer) changeTransactionDao.getJournalBasedOnInvoiceNo(getOrganization(), invoiceNumber,getUsername()));
					resultSuccess.setMessage("Delivery Note Id Based on Invoice No. fetched successfully");
				} 
				else if ("get-journal-creation-time".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
		        	Integer journalId=Integer.valueOf(request.getParameter("journalId"));
					resultSuccess.setData((String) changeTransactionDao.fetchJournalCreationTime(journalId,invoiceNumber, getOrganization()));
					resultSuccess.setMessage("Fetched Difference between Mins of Journal creation and Current Time .");
				}
			//check Transaction Sales return in Dashboard
			else if ("check-Transaction-Sales-Return".equals(action)) {
			String invoiceNumber=request.getParameter("invoiceNumber");
			resultSuccess.setData((Integer) changeTransactionDao.checkTransactionSalesReturn(invoiceNumber, getOrganization()));
			resultSuccess.setMessage("Checked Whther Transaction Sales Return is Present in dashboard or not.");
			}
			//check Transaction Journal in Dashboard
			else if ("check-Transaction-journal".equals(action)) {
			String invoiceNumber=request.getParameter("invoiceNumber");
			resultSuccess.setData((Integer) changeTransactionDao.checkTransactionJournal(invoiceNumber, getOrganization()));
			resultSuccess.setMessage("Checked Whther Transaction Journal is Present in dashboard or not.");
			}
		}
		else if (form instanceof ChangeRequestDeliveryNoteProductCommand) {
			deliveryNoteProducts = (ChangeRequestDeliveryNoteProductCommand) form;
			session = request.getSession(Boolean.TRUE);
			session.setAttribute("delivery-note-product", deliveryNoteProducts);
			resultSuccess.setMessage("Saved Successfully");
		}else if(form instanceof ChangeRequestDayBookBasicInfoCommand) {
			ChangeRequestDayBookBasicInfoCommand dayBookBasicInfoCommand = (ChangeRequestDayBookBasicInfoCommand) form;
			session.setAttribute("daybook-basic-info", dayBookBasicInfoCommand);
		}
		else if(form instanceof ChangeRequestDayBookAllowancesCommand) {
			ChangeRequestDayBookAllowancesCommand dayBookAllowancesCommand = (ChangeRequestDayBookAllowancesCommand) form;
			session.setAttribute("daybook-allowances", dayBookAllowancesCommand);
		} else if(form instanceof ChangeRequestDayBookAmountCommand) {
			ChangeRequestDayBookAmountCommand dayBookAmountCommand = (ChangeRequestDayBookAmountCommand) form;
			session.setAttribute("daybook-amount", dayBookAmountCommand);
		} else if(form instanceof ChangeRequestDayBookProductsCommand) {
			ChangeRequestDayBookProductsCommand dayBookProductsCommand = (ChangeRequestDayBookProductsCommand) form;
			session.setAttribute("daybook-products", dayBookProductsCommand);
		} else if(form instanceof ChangeRequestDayBookVehicleDetailsCommand){
			ChangeRequestDayBookVehicleDetailsCommand dayBookVehicleDetailsCommand = (ChangeRequestDayBookVehicleDetailsCommand) form;
			session.setAttribute("vehicle-details", dayBookVehicleDetailsCommand);
		}
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}

		return resultSuccess;
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 */
	private void removeDeliveryNoteDataFromSession() {
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("deliverynote-info");
		session.removeAttribute("delivery-note-product");
		session.removeAttribute("product-data");
	}
	
	/**
	 * Removing data from session.
	 * 
	 */
	private void removeDayBookFromSession(HttpSession session) {
		session.removeAttribute("daybook-amount");
		session.removeAttribute("daybook-allowances");
		session.removeAttribute("daybook-product-data");
		session.removeAttribute("vehicle-details");
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 */
	private void removeDataFromSession(){
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("go-for-payments");
		session.removeAttribute("delivery-note-product");
	}

}
