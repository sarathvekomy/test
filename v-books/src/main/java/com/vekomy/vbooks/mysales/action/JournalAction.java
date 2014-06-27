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
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.mysales.command.JournalCommand;
import com.vekomy.vbooks.mysales.dao.JournalDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

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
	
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IResult process(Object form) throws Exception {
		ResultSuccess resultSuccess = new ResultSuccess();
		JournalDao journalDao = (JournalDao) getDao();
		if(form instanceof JournalCommand) {
			String userName = getUsername();
			VbOrganization organization = getOrganization();
			JournalCommand command = (JournalCommand) form;
			String action = command.getAction();
			if("generate-invoice-no".equals(action)) {
				String journalType = request.getParameter("journalType");
				String generatedInvoiceNo = journalDao.generateInvoiceNo(organization, journalType);
				if(generatedInvoiceNo == null) {
					resultSuccess.setMessage("Not able to generate invoiceNO");
				} else {
					resultSuccess.setData(generatedInvoiceNo);
					resultSuccess.setMessage("Fetched successfully.");
				}
			} else if("add-journal".equals(action)) {
				journalDao.saveJournal(command, organization, userName);
				
				//For Alerts.
				String defaultToRecipient = journalDao.getCreatedBy(userName, organization);
				AlertManager.getInstance().fireUserDefinedAlert(organization, userName, defaultToRecipient, 
						OrganizationUtils.ALERT_TYPE_MY_SALES, OrganizationUtils.ALERT_MYSALES_TYPE_APPROVALS, 
						OrganizationUtils.ALERT_MYSALES_PAGE_JOURNAL, Msg.get(MsgEnum.ALERT_TYPE_JOURNAL_RAISED_MESSAGE));
				_logger.info("Firing an alert for Journal request");
				
				resultSuccess.setMessage("Saved successfully.");
			} else if ("approve-journal".equals(action)) {
				Integer id = Integer.parseInt(request.getParameter("journalId"));
				String status = request.getParameter("status");
				Boolean isApproved = journalDao.approveOrDeclineJournal(id, status, organization, userName);
				
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
				
				if(isApproved == Boolean.TRUE) {
					resultSuccess.setMessage("Journal have been approved.");
				} else {
					resultSuccess.setMessage("Journal have been declained.");
				}
			} else if ("is-daybook-closed".equals(action)) {
				Boolean isDayBookClosed = journalDao.isDayBookClosed(userName, organization);
				resultSuccess.setData(isDayBookClosed);
				resultSuccess.setMessage("Fetched successfully.");
			} else if("get-business-name".equals(action)) {
				String businessName = request.getParameter("businessNameVal");
				List<String> businessNameList = journalDao.getBusinessName(businessName, organization, userName);
				if (businessNameList.isEmpty()) {
					resultSuccess.setMessage("No Records Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(businessNameList);
				}
			} else if("get-invoice-name-for-journal".equals(action)) {
				String businessName = request.getParameter("businessName");
				String invoiceName = journalDao.getInvoiceName(businessName, organization);
				if (invoiceName == null) {
					resultSuccess.setMessage("No Invoice Name Found");
				} else {
					resultSuccess.setMessage("Fetched Records Successfully");
					resultSuccess.setData(invoiceName);
				}
				
			} else if ("get-all-journals-for-dashboard".equals(action)) {
				List<VbJournal> journals = journalDao.getAllJournals(organization);
				if (journals.isEmpty()) {
					resultSuccess.setMessage("Records not found.");
				} else {
					resultSuccess.setData(journals);
					resultSuccess.setMessage("Fetched successfully.");
				}
			} else if("search-journals".equals(action)){
				HashMap hashMap = new HashMap();
				hashMap.put("journalType", new SearchFilterData("journalType", command.getJournalType(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("businessName", new SearchFilterData("businessName", command.getBusinessName(),SearchFilterData.TYPE_STRING_STR));
				hashMap.put("createdOn", new SearchFilterData("createdOn",command.getCreatedOn(),SearchFilterData.TYPE_DATE_STR));
				List<VbJournal> list=journalDao.getJournal(command, userName, organization);
				if(list.isEmpty()) {
					resultSuccess.setMessage("No Search Results Found");
				} else {
					resultSuccess.setData(list);
					resultSuccess.setMessage("Search Successfull");
				}
			}
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("ResultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}

}
