/**
 * com.vekomy.vbooks.mysales.cr.action.JournalCrAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: September 26, 2013
 */
package com.vekomy.vbooks.mysales.cr.action;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.manager.AlertManager;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalChangeRequest;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesJournalHistoryResult;
import com.vekomy.vbooks.mysales.cr.command.ChangeRequestJournalCommand;
import com.vekomy.vbooks.mysales.cr.dao.JournalCrDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * This action class is responsible to process the Journal CR activity.
 * 
 * @author ankit
 * 
 */

public class JournalCrAction extends BaseAction {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalCrAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) {
		try {
			ChangeRequestJournalCommand journalCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			JournalCrDao journalCrDao = (JournalCrDao) getDao();
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
			if (form instanceof ChangeRequestJournalCommand) {
				journalCommand = (ChangeRequestJournalCommand) form;
				String action = journalCommand.getAction();
				if ("change-journal-transaction-request".equals(action)) {
					List<VbJournal> journal = journalCrDao.getJournalChangeTransaction(organization, userName);
					
					resultSuccess.setData(journal);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("validate-SE-Journal-change-request".equals(action)) {
					Integer journalId = Integer.valueOf(request.getParameter("journalId"));
					String invoiceNumber = request.getParameter("invoiceNumber");
					String data = journalCrDao.validateSEJournalChangeRequest(invoiceNumber, journalId, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
					
				}else if("get-original-journal-data".equals(action)){
					int id = Integer.parseInt(request.getParameter("id"));
					VbJournal originalJournal= journalCrDao.getJournalById(id, organization, userName);
					
					resultSuccess.setData(originalJournal);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("save-journal-change-request".equals(action)) {
					ChangeRequestJournalCommand journalCRCommand = new ChangeRequestJournalCommand();
					journalCRCommand.setJournalType(request.getParameter("journalType"));
					journalCRCommand.setBusinessName(request.getParameter("businessName"));
					journalCRCommand.setInvoiceNo(request.getParameter("invoiceNo"));
					journalCRCommand.setAmount(request.getParameter("amount"));
					journalCRCommand.setInvoiceName(request.getParameter("invoiceName"));
					journalCRCommand.setDescription(request.getParameter("description"));
					journalCRCommand.setCrDescription(request.getParameter("crDescription"));
					journalCrDao.saveJournalCR(journalCRCommand, organization, userName);

					// Firing alerts.
					String defaultToRecipient = organization.getSuperUserName();
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES,
									OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL,
									Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_RAISED_MESSAGE));
					_logger.info("Journal sales executive change request have been raised");
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if ("get-all-journals-CR-for-dashboard".equals(action)) {
					List<VbJournalChangeRequest> list = journalCrDao.getJournalCrResults(userName, organization);
					
					resultSuccess.setData(list);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("approve-journal-cr".equals(action)) {
					Integer id = Integer.parseInt(request.getParameter("journalId"));
					String status = request.getParameter("status");
					journalCrDao.approveOrDeclineJournalCR(id, status, organization, userName);
					
					// Firing Alerts.
					String description = null;
					if (OrganizationUtils.STATUS_APPROVED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_APPROVED_MESSAGE);
					} else if (OrganizationUtils.STATUS_DECLINED.equalsIgnoreCase(status)) {
						description = Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_CHANGE_REQUEST_DECLINED_MESSAGE);
					}
					String defaultToRecipient = journalCrDao.getCreatedBy(id, OrganizationUtils.CR_TYPE_JOURNAL);
					AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, OrganizationUtils.ALERT_TYPE_MY_SALES,
									OrganizationUtils.ALERT_MYSALES_TYPE_TRANSACTION_CR, OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, description);
					_logger.info("Journal sales executive changge request have been {}", status);
					
					resultSuccess.setMessage(description);
					resultSuccess.setStatus(resultSuccessStatus);
				} else if ("get-journal-status".equals(action)) {
					Integer journalId = Integer.valueOf(request.getParameter("id"));
					VbJournal journal = journalCrDao.getJournalStatus(organization, journalId, userName);
					
					resultSuccess.setData(journal);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-journal-id".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer journalId = Integer.valueOf(request.getParameter("journalId"));
					Integer data = journalCrDao.getJournalBasedOnInvoiceNo(organization, invoiceNumber, journalId, userName);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-journal-creation-time".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer journalId = Integer.valueOf(request.getParameter("journalId"));
					String data = journalCrDao.fetchJournalCreationTime(journalId, invoiceNumber, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-journal-id-data".equals(action)) {
					Integer journalId = Integer.valueOf(request.getParameter("journalId"));
					VbJournal vbJournal = journalCrDao.getJournalById(journalId, organization, userName);
					
					resultSuccess.setData(vbJournal);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				}  else if ("check-Transaction-journal".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					Integer data = journalCrDao.checkTransactionJournal(invoiceNumber, organization);
					
					resultSuccess.setData(data);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-configured-journal-types-invoice-pattern".equals(action)) {
					List<MySalesJournalHistoryResult> configuredJournalTypes = journalCrDao.getconfiguredJournalTypesCRWithInvoicePattern(organization, userName);
					
					resultSuccess.setData(configuredJournalTypes);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-specific-journal-type-count".equals(action)) {
					String journalType = request.getParameter("journalTxnType");
					String invoicePatern = request.getParameter("invoicePattern");
					List<MySalesHistoryResult> journalHistory = journalCrDao.getJournalTransactionHistory(journalType, invoicePatern, organization, userName);
					
					resultSuccess.setData(journalHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-journal-invoices-history-transaction".equals(action)) {
					String invoiceNumber = request.getParameter("invoiceNumber");
					String status = request.getParameter("status");
					List<MySalesInvoicesHistoryResult> journalInvoicesHistory = journalCrDao.getJournalInvoicesTransactionHistory(invoiceNumber, status, organization, userName);
					
					resultSuccess.setData(journalInvoicesHistory);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				}
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		} catch (ParseException exception) {
			ResultError resultError = getResultError(exception.getMessage());

			if (_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}

}
