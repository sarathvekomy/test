/**
 * com.vekomy.vbooks.mysales.action.JournalAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.action;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.JournalCommand;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesJournalHistoryResult;
import com.vekomy.vbooks.mysales.dao.JournalDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to process the {@link VbJournal} requests.
 * 
 * @author Sudhakar
 * 
 */
public class JournalAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalAction.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form)  {
		try {
			ResultSuccess resultSuccess = new ResultSuccess();
			String resultStatusSuccess = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			JournalDao journalDao = (JournalDao) getDao();
			if(form instanceof JournalCommand) {
				String userName = getUsername();
				VbOrganization organization = getOrganization();
				JournalCommand command = (JournalCommand) form;
				String action = command.getAction();
				if("generate-invoice-no".equals(action)) {
					String journalType = request.getParameter("journalType");
					String generatedInvoiceNo = journalDao.generateInvoiceNo(organization, journalType);
					
					resultSuccess.setData(generatedInvoiceNo);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("add-journal".equals(action)) {
					journalDao.saveJournal(command, organization, userName);
					
					//For Alerts.
					String defaultToRecipient = journalDao.getCreatedBy(userName, organization);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS, 
							OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_RAISED_MESSAGE));
					_logger.info("Firing an alert for Journal request");
					
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("approve-journal".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("journalId"));
					String status = request.getParameter("status");
					Float amount=Float.parseFloat(request.getParameter("amount"));
					Boolean isApproved = journalDao.approveOrDeclineJournal(id,amount, status, organization, userName);
					
					//For Alerts.
					String description = null;
					if(OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_DECLINED_MESSAGE);
					}
					String defaultToRecipient = journalDao.getCreatedBy(id);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient,
							OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS,
							OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, description);
					
					_logger.info("Firing an alert for Journal request");
					
					resultSuccess.setStatus(resultStatusSuccess);
					if(isApproved == Boolean.TRUE) {
						resultSuccess.setMessage(Msg.get(MsgEnum.APPROVE_JOURNAL_MESSAGE));
					} else {
						resultSuccess.setMessage(Msg.get(MsgEnum.DECLINE_JOURNAL_MESSAGE));
					}
				} else if ("is-daybook-closed".equals(action)) {
					Boolean isDayBookClosed = journalDao.isDayBookClosed(userName, organization);
					
					resultSuccess.setData(isDayBookClosed);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-business-name".equals(action)) {
					String businessName = request.getParameter("businessNameVal");
					List<String> businessNameList = journalDao.getBusinessName(businessName, organization, userName);
					
					resultSuccess.setData(businessNameList);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-invoice-name-for-journal".equals(action)) {
					String businessName = request.getParameter("businessName");
					String invoiceName = journalDao.getInvoiceName(businessName, organization);
					
					resultSuccess.setData(invoiceName);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-all-journals-for-dashboard".equals(action)) {
					List<VbJournal> journals = journalDao.getAllJournals(organization);
					
					resultSuccess.setData(journals);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("search-journals".equals(action)){
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("journalType", new SearchFilterData("journalType", command.getJournalType(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("businessName", new SearchFilterData("businessName", command.getBusinessName(), SearchFilterData.TYPE_STRING_STR));
					hashMap.put("createdOn", new SearchFilterData("createdOn",command.getCreatedOn(), SearchFilterData.TYPE_DATE_STR));
					List<VbJournal> list = journalDao.getJournal(command, userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-journal-for-view".equals(action)){
					VbJournal vbJournal = journalDao.getJournalById(command.getId(), organization, userName);
					
					resultSuccess.setData(vbJournal);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-previous-journal-id".equals(action)) {
		        	String invoiceNumber = request.getParameter("invoiceNumber");
		        	Integer journalId = Integer.valueOf(request.getParameter("journalId"));
		        	Integer previousJournalId = 
		        			(Integer) journalDao.getPreviousJournalInvoiceNumber(journalId, invoiceNumber, organization, userName);
		        	
					resultSuccess.setData(previousJournalId);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-configured-journal-types-invoice-pattern".equals(action)) {
					List<MySalesJournalHistoryResult> configuredJournalTypes = 
							journalDao.getconfiguredJournalTypesWithInvoicePattern(organization, userName);
					
					resultSuccess.setData(configuredJournalTypes);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-specific-journal-type-count".equals(action)) {
					String journalType = request.getParameter("journalTxnType");
					String invoicePatern = request.getParameter("invoicePattern");
					List<MySalesHistoryResult> journalHistory = 
							journalDao.getJournalTransactionHistory(journalType, invoicePatern, organization, userName);
					
					resultSuccess.setData(journalHistory);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-approval-journal-invoices-history-transaction".equals(action)) {
		        	String journalType = request.getParameter("journalType");
		        	String status = request.getParameter("status");
					List<MySalesInvoicesHistoryResult> journalInvoicesHistory = 
							journalDao.getJournalInvoicesTransactionHistory(journalType, status, organization, userName);
					
					resultSuccess.setData(journalInvoicesHistory);
					resultSuccess.setStatus(resultStatusSuccess);
					resultSuccess.setMessage(resultSuccessMessage);
				}
			}
			
			if(_logger.isDebugEnabled()) {
				_logger.info("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
}
