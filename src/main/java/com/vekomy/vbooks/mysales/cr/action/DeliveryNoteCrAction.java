/**
 * com.vekomy.vbooks.mysales.cr.action.DeliveryNoteCrAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: September 26, 2013
 */
package com.vekomy.vbooks.mysales.cr.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.mysales.command.DeliveryNoteViewResult;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteProductCommand;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestDeliveryNoteResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestProductResult;
import com.vekomy.vbooks.mysales.cr.dao.DeliveryNoteCrDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;
/**
 * This action class is responsible to process the Delivery Note CR activity.
 * 
 * @author ankit
 * 
 */
public class DeliveryNoteCrAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(DeliveryNoteCrAction.class);
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
	public IResult process(Object form) {
		try{
			ChangeRequestDeliveryNoteCommand deliveryNoteCommand = null;
			ChangeRequestDeliveryNoteProductCommand deliveryNoteProducts = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			DeliveryNoteCrDao deliveryNoteCrDao = (DeliveryNoteCrDao) getDao();
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			if (form instanceof ChangeRequestDeliveryNoteCommand) {
				deliveryNoteCommand = (ChangeRequestDeliveryNoteCommand) form;
				String action = deliveryNoteCommand.getAction();
				if ("save-deliverynote-info".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					session.setAttribute("deliverynote-info", deliveryNoteCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("save-product-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String listOfObjects = request.getParameter("listOfProductObjects");
					String businessName = request.getParameter("businessName");
					String invoiceName = request.getParameter("invoiceName");
					String invoiceNumber = request.getParameter("invoiceNumber");
					String rowSteps[] = listOfObjects.split("\\?");
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
								deliveryNoteCommand.setBonusQuantity(bonus);
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
						}
					}
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("remove-delivery-note-product-from-session".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					// removing the session data.
					removeDeliveryNoteDataFromSession(session);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}  else if("get-original-delivery-note-data".equals(action)){
					int id = Integer.parseInt(request.getParameter("id"));
					DeliveryNoteViewResult  originalDeliveryNoteResult= deliveryNoteCrDao.getOriginalDeliveryNoteDetails(id, organization);
					
					resultSuccess.setData(originalDeliveryNoteResult);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} 
				else if ("go-for-payments".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//ChangeRequestDeliveryNoteCommand command = new ChangeRequestDeliveryNoteCommand();
					deliveryNoteCommand.setBusinessName(request.getParameter("businessName"));
					deliveryNoteCommand.setInvoiceName(request.getParameter("invoiceName"));
					deliveryNoteCommand.setInvoiceNo(request.getParameter("invoiceNumber"));
					session.setAttribute("go-for-payments", deliveryNoteCommand);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("save-payment-delivery-note".equals(action)) {
					String originalDNInvoiceNumber=request.getParameter("invoiceNumber");
					session = request.getSession(Boolean.TRUE);
					deliveryNoteCrDao.savePayments((ChangeRequestDeliveryNoteCommand)session.getAttribute("go-for-payments"), 
							originalDNInvoiceNumber,(ChangeRequestDeliveryNoteProductCommand)session.getAttribute("delivery-note-product"), 
							organization, userName);
					
					// Firing alerts.
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(getOrganization(), userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_RAISED_MESSAGE));
					
					_logger.info("Firing an alert for sales executive deliveryNote change request for payments");
					
					//Removing data from session.
					removeDataFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				}
				else if ("save-deliverynote".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String originalDNInvoiceNumber=request.getParameter("invoiceNumber");
					deliveryNoteCrDao.saveDeliveryNote((ChangeRequestDeliveryNoteProductCommand) session.getAttribute("delivery-note-product"),
						(List<ChangeRequestDeliveryNoteCommand>) session.getAttribute("product-data"),originalDNInvoiceNumber, userName,organization);
					
					// Firing alerts.
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_RAISED_MESSAGE));
					
					_logger.info("Firing an alert for sales executive deliveryNote change request for payments");
					
					// removing the session data.
					removeDeliveryNoteDataFromSession(session);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("get-existed-grid-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					String businessName = request.getParameter("businessName");
					Integer deliveryNoteId = Integer.valueOf(request.getParameter("deliveryNoteId"));
					Integer salesId = Integer.valueOf(request.getParameter("salesId"));
					List<ChangeRequestProductResult> productResultList = deliveryNoteCrDao.getExistedBusinessNameGridData(salesId,deliveryNoteId,userName, businessName, organization);
					session.setAttribute("grid-data", productResultList);
					
					resultSuccess.setData(productResultList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("change-dn-transaction-request".equals(action)) {
					List<ChangeRequestDeliveryNoteResult> list = deliveryNoteCrDao.getDeliveryNoteResultsOnCriteria(organization, userName);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("approve-delivery-note-cr".equals(action)) {
					Integer deliverNoteCRId = Integer.parseInt(request.getParameter("deliverNoteCRId"));
					String status = request.getParameter("status");
					deliveryNoteCrDao.getApprovedDeliveyNoteCR(deliverNoteCRId, status, organization, userName);
					
					// For Alerts.
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_DELIVERY_NOTE_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = deliveryNoteCrDao.getCreatedBy(deliverNoteCRId, OrganizationUtils.CR_TYPE_DELIVERY_NOTE);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, 
							OrganizationUtils.ALERT_MYSALES_PAGE_DELIVERY_NOTE, description);
					
					_logger.info("Firing an alert for sales executive salesReturn deliveryNote Change Request");
					
					resultSuccess.setMessage(description);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-delivery-note-id".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
		        	Integer deliveryNoteId=Integer.valueOf(request.getParameter("deliveryNoteId"));
					resultSuccess.setData((Integer) deliveryNoteCrDao.getDeliveryNoteBasedOnInvoiceNo(organization,deliveryNoteId, invoiceNumber,userName));
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("search-delivery-note-change-request-dashboard".equals(action)) {
					List<ChangeRequestDeliveryNoteResult> list=deliveryNoteCrDao.getDeliveryNoteCrResults(userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("validate-SE-DN-change-request".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					String isAvailable = deliveryNoteCrDao.validateSEChangeRequest(invoiceNumber, organization);
					
					resultSuccess.setData(isAvailable);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}  else if ("get-delivery-note-creation-time".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer deliveryNoteId = Integer.valueOf(request.getParameter("deliveryNoteId"));
					String createdOn = deliveryNoteCrDao.fetchDeliveryNoteCreationTime(deliveryNoteId, invoiceNumber, organization);
					
					resultSuccess.setData(createdOn);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-delivery-note-history-transaction".equals(action)) {
					List<MySalesHistoryResult> deliveryNoteHistory = deliveryNoteCrDao.getDeliveryNoteTransactionHistory(organization,userName);
					
					resultSuccess.setData(deliveryNoteHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if ("get-delivery-note-invoices-history-transaction".equals(action)) {
		        	String invoiceNumber=request.getParameter("invoiceNumber");
		        	String status=request.getParameter("status");
					List<MySalesInvoicesHistoryResult> deliveryNoteinvoicesHistory = deliveryNoteCrDao.getDeliveryNoteInvoicesTransactionHistory(invoiceNumber,status,organization,userName);
					
					resultSuccess.setData(deliveryNoteinvoicesHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
				//removing data from session on click of cancel button in DN CR edit page
				else if ("remove-delivery-note-cr-session-data".equals(action)) {
					session = request.getSession(Boolean.TRUE);
					//Removing data from session.
					String invoiceNumber=request.getParameter("invoiceNumber");
					if(invoiceNumber.contains("DN")){
						//DN CR product sold
						removeDeliveryNoteDataFromSession(session);
					}else{
						//DN CR payment collection 
						removeDataFromSession(session);
					}
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.REMOVE_DATA_FROM_SESSION_MESSAGE));
				}
			} else if (form instanceof ChangeRequestDeliveryNoteProductCommand) {
				deliveryNoteProducts = (ChangeRequestDeliveryNoteProductCommand) form;
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("delivery-note-product", deliveryNoteProducts);
				
				resultSuccess.setStatus(resultSuccessStatus);
				resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
			} 
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 * @param session - {@link HttpSession}
	 * 
	 */
	private void removeDeliveryNoteDataFromSession(HttpSession session) {
		session.removeAttribute("deliverynote-info");
		session.removeAttribute("delivery-note-product");
		session.removeAttribute("product-data");
	}
	
	/**
	 * This method is responsible to remove data from session.
	 * 
	 * @param session - {@link HttpSession}
	 */
	private void removeDataFromSession(HttpSession session){
		session.removeAttribute("go-for-payments");
		session.removeAttribute("delivery-note-product");
	}
}
