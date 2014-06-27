/**
 * com.vekomy.vbooks.mysales.dao.JournalDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 4, 2013
 */
package com.vekomy.vbooks.mysales.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbCustomer;
import com.vekomy.vbooks.hibernate.model.VbCustomerAdvanceInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditInfo;
import com.vekomy.vbooks.hibernate.model.VbCustomerCreditTransaction;
import com.vekomy.vbooks.hibernate.model.VbCustomerDebitTransaction;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbInvoiceNoPeriod;
import com.vekomy.vbooks.hibernate.model.VbJournal;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.mysales.command.JournalCommand;
import com.vekomy.vbooks.mysales.command.MySalesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesInvoicesHistoryResult;
import com.vekomy.vbooks.mysales.command.MySalesJournalHistoryResult;
import com.vekomy.vbooks.util.CRStatus;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotificationTypes;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.StringUtil;

/**
 * This dao class is responsible to perform CURD operations on {@link VbJournal}.
 * 
 * @author Sudhakar
 *
 */
public class JournalDao extends MySalesBaseDao {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(JournalDao.class);
	/**
	 * String variable holds DAILY_SALES_EXECUTIVE.
	 */
	private static final String DAILY_SALES_EXECUTIVE = "Daily";
	
	/**
	 * This method is responsible to persist {@link VbJournal} into database.
	 * 
	 * @param command - {@link JournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void saveJournal(JournalCommand command, VbOrganization organization, String userName) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			Date date = new Date();
			String businessName = command.getBusinessName();
			String invoiceNo = command.getInvoiceNo();
			VbSalesBook salesBook = getVbSalesBook(session, userName, organization);
			txn = session.beginTransaction();
			
			// Populating and Persisting VbJournal
			VbJournal vbJournalinstance = new VbJournal();
			vbJournalinstance.setAmount(command.getAmount());
			vbJournalinstance.setBusinessName(businessName);
			vbJournalinstance.setInvoiceName(command.getInvoiceName());
			vbJournalinstance.setCreatedBy(userName);
			vbJournalinstance.setCreatedOn(date);
			vbJournalinstance.setModifiedOn(date);
			vbJournalinstance.setDescription(command.getDescription());
			vbJournalinstance.setFlag(new Integer(1));
			vbJournalinstance.setInvoiceNo(invoiceNo);
			vbJournalinstance.setStatus(CRStatus.PENDING.name());
			vbJournalinstance.setJournalType(command.getJournalType());
			vbJournalinstance.setVbOrganization(organization);
			vbJournalinstance.setVbSalesBook(salesBook);		
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbJournal: {}", vbJournalinstance);
			}
			session.save(vbJournalinstance);
			
			// For Android Application.
			saveSystemNotificationForAndroid(session, userName, userName, organization, 
					ENotificationTypes.JOURNAL.name(), CRStatus.PENDING.name(), invoiceNo, businessName);
			txn.commit();
		} catch(HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to generate invoiceNo based on {@link VbOrganization} and journal type.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param journalType - {@link String}
	 * @return generatedInvoiceNo - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 * 
	 */
	public String generateInvoiceNo(VbOrganization organization, String journalType) throws DataAccessException {
		Session session = this.getSession();
		String seperator = OrganizationUtils.SEPERATOR;
		String formattedDate = DateUtils.format2(new Date());
		StringBuffer generatedInvoiceNo = new StringBuffer();
		String invoiceNumberPattern = (String) session.createCriteria(VbJournalTypes.class)
				.setProjection(Projections.property("invoiceNo"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("journalType", journalType))
				.uniqueResult();
		if(invoiceNumberPattern != null) {
			Query query = session.createQuery("SELECT vb.invoiceNo FROM VbJournal vb WHERE vb.createdOn " +
					"IN(SELECT MAX(vbj.createdOn) FROM VbJournal vbj WHERE vbj.vbOrganization = :vbOrganization AND vbj.invoiceNo LIKE :invoiceNo)")
					.setParameter("vbOrganization", organization)
					.setParameter("invoiceNo", "%"+invoiceNumberPattern+"%");
			String latestInvoiceNo = getSingleResultOrNull(query);
			
			VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session.createCriteria(VbInvoiceNoPeriod.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_JOURNAL))
					.uniqueResult();
			
			if(latestInvoiceNo == null) {
				if(invoiceNoPeriod != null) {
					generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append(invoiceNoPeriod.getStartValue());
				} else {
					generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append("0001");
				}
			} else {
				String[] previousInvoiceNo = latestInvoiceNo.split("#");
				String[] latestInvoiceNoArray = previousInvoiceNo[0].split(seperator);
				latestInvoiceNo = latestInvoiceNoArray[3];
				Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
				String invoiceNumber = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
				generatedInvoiceNo.append(organization.getOrganizationCode()).append(seperator).append(invoiceNumberPattern).append(seperator).append(formattedDate).append(seperator).append(invoiceNumber);
			}
			session.close();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Generated invoiceNo: {}", generatedInvoiceNo.toString());
			}
			return generatedInvoiceNo.toString();
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is used to get the invoiceName based on the businessName from {@link VbCustomer} .
	 * 
	 * @param businessName - {@link String}
	 * @return invoiceName - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getInvoiceName(String businessName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String invoiceName = (String) session.createCriteria(VbCustomer.class)
				.setProjection(Projections.property("invoiceName"))
				.add(Expression.eq("businessName", businessName))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();

		if (invoiceName != null) {
			if (_logger.isInfoEnabled()) {
				_logger.info("Invoice Name associated with businessName {} is: {}",businessName, invoiceName);
			}
			return invoiceName;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}

	}
	
	/**
	 * This method is responsible to get all the un approved {@link VbJournal} based on {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return journalsList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getAllJournals(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<VbJournal> journalsList = session.createCriteria(VbJournal.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("status", CRStatus.PENDING.name()))
				.list();
		session.close();
		
		if(!journalsList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} journals available for the organization: {}", journalsList.size(), organization.getName());
			}
			return journalsList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to approve the raised {@link VbJournal} requests means
	 * updating {@link VbCustomerCreditInfo} if the business name have any credit
	 * or saving the amount in {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param command - {@link JournalCommand}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return isApproved - {@link Boolean}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public Boolean approveOrDeclineJournal(Integer id, Float approvedAmount,
			String status, VbOrganization organization, String userName)
			throws DataAccessException {
		Boolean isApproved = Boolean.FALSE;
		Date date = new Date();
		Session session = this.getSession();
		VbJournal journal = (VbJournal) session.get(VbJournal.class, id);
		if(journal != null) {
			Transaction txn = null;
			try {
				txn = session.beginTransaction();
				String businessName = journal.getBusinessName();
				journal.setModifiedBy(userName);
				journal.setModifiedOn(date);
				String salesExecutive = journal.getVbSalesBook().getSalesExecutive();
				VbSalesBook vbSalesBook = getVbSalesBook(session, salesExecutive, organization);
				if(vbSalesBook == null) {
					vbSalesBook = getVbSalesBookNoFlag(session, salesExecutive, organization);
				}
				if(CRStatus.APPROVED.name().equalsIgnoreCase(status)) {
					Query query = null;
					VbCustomerCreditInfo creditInfo = null;
					VbCustomerDebitTransaction customerDebitTxn=null;
					while (approvedAmount != 0) {
						// Updating previous credit of the businessName.
						query = session.createQuery("FROM VbCustomerCreditInfo vb WHERE vb.createdOn IN ("
										+ "SELECT MIN(vbc.createdOn) FROM VbCustomerCreditInfo vbc WHERE "
										+ "vbc.businessName = :businessName AND vbc.vbOrganization = :vbOrganization AND vbc.due > :due)")
										.setParameter("businessName", businessName)
										.setParameter("vbOrganization", organization)
										.setParameter("due", new Float("0"));
						creditInfo = getSingleResultOrNull(query);
						customerDebitTxn=new VbCustomerDebitTransaction();
						if(creditInfo != null) {
							creditInfo.setModifiedBy(userName);
							creditInfo.setModifiedOn(date);
							String existingInvoiceNo = creditInfo.getDebitTo();
							if(existingInvoiceNo == null) {
								creditInfo.setDebitTo(journal.getInvoiceNo());
							} else {
								creditInfo.setDebitTo(existingInvoiceNo.concat(",").concat(journal.getInvoiceNo()));
							}
							Float due = creditInfo.getDue();
							if(approvedAmount < due) {
								creditInfo.setDue(due - approvedAmount);
								customerDebitTxn.setAmount(approvedAmount);
								approvedAmount = new Float(0);
							} else {
								creditInfo.setDue(new Float(0));
								customerDebitTxn.setAmount(due);
								approvedAmount = approvedAmount - due;
							}
							
							if(_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerCreditInfo: {}", creditInfo);
							}
							session.update(creditInfo);
							
							//persisting Customer debit Txn
							customerDebitTxn.setBusinessName(businessName);
							customerDebitTxn.setFlag(new Integer(1));
							customerDebitTxn.setCreditFrom(creditInfo.getInvoiceNo());
							customerDebitTxn.setDebitTo(journal.getInvoiceNo());
							customerDebitTxn.setCreatedBy(userName);
							customerDebitTxn.setCreatedOn(new Date());
							customerDebitTxn.setModifiedOn(new Date());
							customerDebitTxn.setVbOrganization(organization);
							
							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbCustomerDebitTransaction: {}", customerDebitTxn);
							}
							session.save(customerDebitTxn);
						} else {
							// Saving the remaining balance as advance for the businessName.
							saveCustomerAdvanceInfo(session, businessName, journal.getInvoiceNo(), organization, approvedAmount, userName);
							approvedAmount = new Float(0);
						}
					}
					journal.setStatus(CRStatus.APPROVED.name());
					isApproved = Boolean.TRUE;
				} else {
					journal.setStatus(CRStatus.DECLINE.name());
					isApproved = Boolean.FALSE;
				}
				
				if(_logger.isDebugEnabled()) {
					_logger.debug("Updating VbJournal: {}", journal);
				}
				session.update(journal);
				
				// For Android Application.
				saveSystemNotificationForAndroid(session, userName, salesExecutive, organization, 
							ENotificationTypes.JOURNAL.name(), status, journal.getInvoiceNo(), businessName);
				txn.commit();
				return isApproved;
			} catch(HibernateException exception) {
				if (txn != null) {
					txn.rollback();
				}
				throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
			} finally {
				if (session != null) {
					session.close();
				}
			}
		} else {
			if (session != null) {
				session.close();
			}
			throw new DataAccessException(Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to persist {@link VbCustomerAdvanceInfo}, if
	 * there is any balance available after crediting all the
	 * {@link VbCustomerCreditInfo} based on businessName and
	 * {@link VbOrganization} or persisting advance directly.
	 * 
	 * @param session - {@link Session}
	 * @param businessName - {@link String}
	 * @param invoiceNo - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param balance - {@link Float}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	private void saveCustomerAdvanceInfo(Session session, String businessName,
			String invoiceNo, VbOrganization organization, Float balance, String userName) throws DataAccessException {
		try {
			VbCustomerAdvanceInfo advanceInfo = new VbCustomerAdvanceInfo();
			VbCustomerCreditTransaction customerCreditTxn=new VbCustomerCreditTransaction();
			Date date = new Date();
			if (balance < 0) {
				advanceInfo.setAdvance(-(balance));
				advanceInfo.setBalance(-(balance));
				customerCreditTxn.setAmount(-(balance));
			} else {
				advanceInfo.setAdvance(balance);
				advanceInfo.setBalance(balance);
				customerCreditTxn.setAmount(balance);
			}
			String advanceInfoInvoiceNumber = generateCustomerAdvanceInfoInvoiceNumber(session, organization);
			advanceInfo.setBusinessName(businessName);
			advanceInfo.setInvoiceNo(advanceInfoInvoiceNumber);
			advanceInfo.setCreatedBy(userName);
			advanceInfo.setCreatedOn(date);
			advanceInfo.setModifiedOn(date);
			advanceInfo.setCreditFrom(invoiceNo);
			advanceInfo.setVbOrganization(organization);
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerAdvanceInfo: {}", advanceInfo);
			}
			session.save(advanceInfo);
			
			//persisting Customer Credits Txn for New Advance info for extra amount.
			customerCreditTxn.setBusinessName(businessName);
			customerCreditTxn.setCreditFrom(invoiceNo);
			customerCreditTxn.setDebitTo(advanceInfoInvoiceNumber);
			customerCreditTxn.setFlag(new Integer(1));
			customerCreditTxn.setCreatedBy(userName);
			customerCreditTxn.setCreatedOn(date);
			customerCreditTxn.setModifiedOn(date);
			customerCreditTxn.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbCustomerCreditTransaction: {}", customerCreditTxn);
			}
			session.save(customerCreditTxn);
		} catch(HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
		}
	}
	/**
	 * This method is responsible to retrieve all the {@link VbJournal} from
	 * DB on criteria.
	 * 
	 * @param JournalCommand - {@link command}
	 * @param username - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return journalList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbJournal> getJournal(JournalCommand command, String username,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Integer grantedDays = 0;
		//for display of search Txn of SE get granted_days from vb_employee table for specific SE
		grantedDays = (Integer)session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("grantedDays"))
				.add(Expression.eq("username", username))
				.add(Expression.eq("employeeType", "SLE"))
				.add(Expression.eq("vbOrganization", organization))
				.uniqueResult();
		if(grantedDays == null){
			grantedDays = new Integer(0);
		}
		
		Criteria criteria = session.createCriteria(VbJournal.class);
		VbSalesBook salesBook = getSalesBookForSearch(session, organization, username);
		if(salesBook != null){
			criteria.add(Restrictions.eq("createdBy",username));
			if(DAILY_SALES_EXECUTIVE.equalsIgnoreCase(salesBook.getAllotmentType())){
				criteria.add(Restrictions.ge("createdOn", DateUtils.getBeforeTwoDays(new Date(),grantedDays)));
			}else{
				Date createdDate = salesBook.getCreatedOn();
				List<VbSalesBook> salesBookList  = session.createCriteria(VbSalesBook.class)
						    .add(Restrictions.between("createdOn", createdDate, DateUtils.getEndTimeStamp(new Date())))
						    .list();
				criteria.add(Restrictions.in("vbSalesBook",salesBookList));
			}
		}
		if(command != null) {
			String journalType = command.getJournalType();
			String businessName = command.getBusinessName();
			Date createdOn = command.getCreatedOn();
			if(StringUtils.isNotBlank(journalType)){
				criteria.add(Restrictions.like("journalType",journalType, MatchMode.START).ignoreCase());
			}
			if (StringUtils.isNotBlank(businessName)) {
				criteria.add(Restrictions.like("businessName", businessName, MatchMode.START).ignoreCase());
			}
			if (createdOn != null) {
				criteria.add(Restrictions.between("createdOn", DateUtils.getStartTimeStamp(createdOn), DateUtils.getEndTimeStamp(createdOn)));
			}
		}
		criteria.add(Expression.eq("flag", new Integer(1)));
		criteria.addOrder(Order.desc("createdOn"));
		criteria.add(Restrictions.eq("vbOrganization", organization));
		List<VbJournal> journalList=criteria.list();
		session.close();
		
		if(!journalList.isEmpty()) {
			if(_logger.isInfoEnabled()) {
				_logger.info("{} records have been found.", journalList.size());
			}
			return journalList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to get {@link VbSalesBook} based on
	 * {@link VbOrganization} and userName.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return salesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getSalesBookForSearch(Session session, VbOrganization organization, String userName) {
		Query query = session.createQuery(
						"FROM VbSalesBook vb WHERE  (vb.createdOn,vb.cycleId) IN " +
						"(SELECT MIN(vbs.createdOn),MAX(vbs.cycleId) FROM VbSalesBook vbs WHERE " +
						"vbs.salesExecutive = :salesExecutiveName AND vb.vbOrganization = :vbOrganization )")
				.setParameter("vbOrganization", organization)
				.setParameter("salesExecutiveName", userName);
		VbSalesBook salesBook = getSingleResultOrNull(query);
		return salesBook;
	}
	
	/**
	 * This method is responsible to get {@link VbJournal} based on
	 * journal id and userName.
	 * 
	 * @param journalId - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return vbJournal - {@link VbJournal}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbJournal getJournalById(Integer journalId,
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		VbJournal vbJournal = (VbJournal)session.get(VbJournal.class, journalId);
		session.close();
		
		if(vbJournal != null) {
			vbJournal.setCreatedBy(getEmployeeFullName(vbJournal.getCreatedBy(),organization));
			vbJournal.setAmount(Float.parseFloat(StringUtil.floatFormat(vbJournal.getAmount())));
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("VbJournal: {}", vbJournal);
			}
			return vbJournal;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to get createdBy of {@link VbEmployee}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public String getCreatedBy(String userName, VbOrganization organization) throws DataAccessException {
		String createdBy = null;
		VbEmployee employee = getSalesExecutiveFullName(userName, organization);
		if(employee != null) {
			createdBy = employee.getCreatedBy();
			
			if(_logger.isInfoEnabled()) {
				_logger.info("Journal created by: {}", createdBy);
			}
			return createdBy;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to get createdBy of {@link VbJournal}.
	 * 
	 * @param id - {@link Integer}
	 * @return createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public String getCreatedBy(Integer id) throws DataAccessException {
		String createdBy = null;
		Session session = this.getSession();
		VbJournal journal = (VbJournal) session.get(VbJournal.class, id);
		session.close();
		
		if(journal != null) {
			createdBy = journal.getCreatedBy();
			
			if(_logger.isInfoEnabled()) {
				_logger.info("Journal created by: {}", createdBy);
			}
			return createdBy;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to generate invoice no for {@link VbCustomerAdvanceInfo}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return generatedInvoiceNo - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public String generateCustomerAdvanceInfoInvoiceNumber(Session session,
			VbOrganization organization) throws DataAccessException {
		try {
			String formattedDate = DateUtils.format2(new Date());
			Query query = session.createQuery(
							"SELECT vb.invoiceNo FROM VbCustomerAdvanceInfo vb WHERE vb.createdOn IN " +
							"(SELECT MAX(vbdn.createdOn) FROM VbCustomerAdvanceInfo vbdn "
							+ "WHERE vbdn.vbOrganization = :vbOrganization AND vbdn.invoiceNo LIKE :invoiceNo)")
					.setParameter("vbOrganization", organization)
					.setParameter("invoiceNo", "%AD%");
			String latestInvoiceNo = getSingleResultOrNull(query);
			VbInvoiceNoPeriod invoiceNoPeriod = (VbInvoiceNoPeriod) session
					.createCriteria(VbInvoiceNoPeriod.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("txnType", OrganizationUtils.TXN_TYPE_ADVANCE))
					.uniqueResult();
			StringBuffer generatedInvoiceNo = new StringBuffer();
			if (latestInvoiceNo == null) {
				if (invoiceNoPeriod != null) {
					generatedInvoiceNo
							.append(organization.getOrganizationCode())
							.append(OrganizationUtils.SEPERATOR)
							.append(OrganizationUtils.TXN_TYPE_ADVANCE)
							.append(OrganizationUtils.SEPERATOR)
							.append(formattedDate)
							.append(OrganizationUtils.SEPERATOR)
							.append(invoiceNoPeriod.getStartValue());
				} else {
					generatedInvoiceNo
							.append(organization.getOrganizationCode())
							.append(OrganizationUtils.SEPERATOR)
							.append(OrganizationUtils.TXN_TYPE_ADVANCE)
							.append(OrganizationUtils.SEPERATOR)
							.append(formattedDate)
							.append(OrganizationUtils.SEPERATOR).append("0001");
				}
			} else {
				String[] previousInvoiceNo = latestInvoiceNo.split("#");
				String[] latestInvoiceNoArray = previousInvoiceNo[0].split("/");
				latestInvoiceNo = latestInvoiceNoArray[3];
				Integer invoiceNo = Integer.parseInt(latestInvoiceNo);
				String newInvoiceNo = resetInvoiceNo(session, invoiceNoPeriod, organization, invoiceNo);
				generatedInvoiceNo.append(organization.getOrganizationCode())
						.append(OrganizationUtils.SEPERATOR)
						.append(OrganizationUtils.TXN_TYPE_ADVANCE)
						.append(OrganizationUtils.SEPERATOR)
						.append(formattedDate)
						.append(OrganizationUtils.SEPERATOR)
						.append(newInvoiceNo);
			}

			if (_logger.isDebugEnabled()) {
				_logger.debug("InvoiceNo generated for customer advance is: {}", generatedInvoiceNo);
			}
			return generatedInvoiceNo.toString();
		} catch (HibernateException exception) {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible for get previous journal based on current invoiceNumber,flag=0,and previousJournal
	 *  
	 * @param journalId - {@link Integer}
	 * @param invoiceNumber - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param username - {@link String}
	 * @return salesReturnMaxId - {@link Integer}
	 */
	public Integer getPreviousJournalInvoiceNumber(Integer journalId,
			String invoiceNumber, VbOrganization organization, String username){
		Session session = this.getSession();
		Query query = session.createQuery("SELECT MAX(vbj.id) FROM VbJournal vbj where vbj.invoiceNo LIKE :invoiceNo AND " +
				"vbj.flag= :flag AND vbj.id < :journalId AND vbj.vbOrganization = :vbOrganization")
				.setParameter("invoiceNo", invoiceNumber)
				.setParameter("flag", new Integer(0))
				.setParameter("journalId", journalId)
				.setParameter("vbOrganization", organization);
		Integer journalMaxId = getSingleResultOrNull(query);
		session.close();
		
		if(journalMaxId == null){
			journalMaxId = new Integer(0);
		}
		
		return journalMaxId;
	}
	
	/**
	 * This method is responsible for getting Approved,Declined,Pending count of
	 * Journal.
	 * 
	 * @param journalTxnType -{@link String}
	 * @param invoicePattern -{@link String}
	 * @param organization -{@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults -{@link MySalesHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesHistoryResult> getJournalTransactionHistory(
			String journalTxnType, String invoicePattern,
			VbOrganization organization, String userName)
			throws DataAccessException {
		Session session = this.getSession();
		//Approved, Decline, Pending status count of journal
		List<Object[]> resultList = session.createCriteria(VbJournal.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("invoiceNo"))
						.add(Projections.property("status"))
						.add(Projections.rowCount())
						.add(Projections.groupProperty("status")))
					.add(Restrictions.like("invoiceNo", invoicePattern, MatchMode.ANYWHERE))
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
		session.close();
		if (!resultList.isEmpty()) {
			MySalesHistoryResult historyResult = null;
			List<MySalesHistoryResult> historyResults = new ArrayList<MySalesHistoryResult>();
			for (Object[] objects : resultList) {
				historyResult = new MySalesHistoryResult();
				String journalStatus = (String) objects[1];
				Integer count = (Integer) objects[2];
				if (CRStatus.APPROVED.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setApprovedDNCount(count);
				} else if (CRStatus.DECLINE.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setDeclinedDNCount(count);
				} else if (CRStatus.PENDING.name().equals(journalStatus)) {
					historyResult.setDeliveryNoteTransactionType(invoicePattern);
					historyResult.setDeliveryNoteTxnStatus(journalStatus);
					historyResult.setPendingDNCount(count);
				}
				historyResults.add(historyResult);
			} 
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResults.size());
			}
			return historyResults;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
	
	/**
	 * This method is responsible to fetch invoices from {@link VbJournal} based on status and invoiceNumber 
	 * 
	 * @param journalType - {@link String}
	 * @param status - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return invoiceHistoryResults - {@link MySalesInvoicesHistoryResult}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesInvoicesHistoryResult> getJournalInvoicesTransactionHistory(
			String journalType, String status, VbOrganization organization,
			String userName) throws DataAccessException {
		Session session = this.getSession();
		List<Object[]> resultList = session.createCriteria(VbJournal.class)
				.setProjection(Projections.projectionList()
						.add(Projections.property("id"))
						.add(Projections.property("invoiceNo"))
						.add(Projections.property("status"))
						.add(Projections.property("createdBy"))
						.add(Projections.property("createdOn"))
						.add(Projections.property("modifiedBy"))
						.add(Projections.property("modifiedOn")))
				.add(Restrictions.like("invoiceNo", journalType, MatchMode.ANYWHERE))
				.add(Restrictions.eq("status", status))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		session.close();
		
		if (!resultList.isEmpty()) {
			Integer journalId = null;
			String journalInvNo = null;
			String JOURNALstatus = null;
			String createdBy = null;
			Date createdOn = null;
			String modifiedBy = null;
			Date modifiedOn = null;
			MySalesInvoicesHistoryResult invoiceHistoryResult = null;
			List<MySalesInvoicesHistoryResult> invoiceHistoryResults = new ArrayList<MySalesInvoicesHistoryResult>();
			for (Object[] objects : resultList) {
				journalId = (Integer) objects[0];
				journalInvNo = (String) objects[1];
				JOURNALstatus = (String) objects[2];
				createdBy = (String) objects[3];
				createdOn = (Date) objects[4];
				modifiedBy = (String) objects[5];
				modifiedOn = (Date) objects[6];
				
				invoiceHistoryResult = new MySalesInvoicesHistoryResult();
				invoiceHistoryResult.setInvoiceNumber(journalInvNo);
				invoiceHistoryResult.setRequestedBy(createdBy);
				invoiceHistoryResult.setRequestedDate(createdOn);
				invoiceHistoryResult.setModifiedBy(modifiedBy);
				invoiceHistoryResult.setModifiedDate(modifiedOn);
				invoiceHistoryResult.setStatus(JOURNALstatus);
				invoiceHistoryResult.setId(journalId);

				invoiceHistoryResults.add(invoiceHistoryResult);
			} 
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", invoiceHistoryResults.size());
			}
			return invoiceHistoryResults;
		} else{
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 * This method is responsible for fetching all Txn for Journal based on
	 * Journal Type and respective invoice pattern.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @return historyResults - {@link MySalesJournalHistoryResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MySalesJournalHistoryResult> getconfiguredJournalTypesWithInvoicePattern(
			VbOrganization organization, String userName)
			throws DataAccessException {
		// Approved, Decline, Pending status count of journal
		Session session = this.getSession();
		List<VbJournalTypes> journalTypeList = (List<VbJournalTypes>) session.createCriteria(VbJournalTypes.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdBy", userName))
				.list();
		if (!journalTypeList.isEmpty()) {
			String journalType = null;
			Integer totalCount = null;
			List<MySalesJournalHistoryResult> historyResults = new ArrayList<MySalesJournalHistoryResult>();
			for (VbJournalTypes journalsType : journalTypeList) {
				List<Object[]> resultList = session.createCriteria(VbJournal.class)
						.setProjection(Projections.projectionList()
								.add(Projections.property("journalType"))
								.add(Projections.rowCount())
								.add(Projections.groupProperty("journalType")))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				if(!resultList.isEmpty()) {
					MySalesJournalHistoryResult historyResult = null;
					for (Object[] objects : resultList) {
						historyResult = new MySalesJournalHistoryResult();
						journalType = (String) objects[0];
						totalCount = (Integer) objects[1];
						if (journalsType.getJournalType().equalsIgnoreCase(journalType)) {
							historyResult.setJournalTransactionType(journalType);
							historyResult.setJournalInvoicePattern(journalsType.getInvoiceNo());
							historyResult.setTotalJournalTypeCount(totalCount);
							historyResults.add(historyResult);
						}
					}
				} else {
					// for those journal type which are configured but not made any txn
					MySalesJournalHistoryResult historyResult = new MySalesJournalHistoryResult();
					historyResult.setJournalTransactionType(journalsType.getJournalType());
					historyResult.setJournalInvoicePattern(journalsType.getInvoiceNo());
					historyResult.setTotalJournalTypeCount(new Integer(0));
					historyResults.add(historyResult);
				}
			}
			session.close();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records found.", historyResults.size());
			}
			return historyResults;
		} else {
			session.close();
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}
}