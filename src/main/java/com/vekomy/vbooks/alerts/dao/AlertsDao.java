/**
 * com.vekomy.vbooks.alerts.dao.AlertsDao.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 10, 2013
 */
package com.vekomy.vbooks.alerts.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.alerts.command.AlertsCommand;
import com.vekomy.vbooks.alerts.command.AlertsHistoryResult;
import com.vekomy.vbooks.alerts.command.AlertsResult;
import com.vekomy.vbooks.alerts.command.MyAlertResult;
import com.vekomy.vbooks.alerts.command.TrendingAlertResult;
import com.vekomy.vbooks.alerts.command.ViewAlertResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.BaseDao;
import com.vekomy.vbooks.hibernate.model.VbAlertCategory;
import com.vekomy.vbooks.hibernate.model.VbAlertType;
import com.vekomy.vbooks.hibernate.model.VbAlertTypeMySales;
import com.vekomy.vbooks.hibernate.model.VbAlertTypeMySalesPage;
import com.vekomy.vbooks.hibernate.model.VbEmployee;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbExcessCash;
import com.vekomy.vbooks.hibernate.model.VbInsystemAlertNotifications;
import com.vekomy.vbooks.hibernate.model.VbLogin;
import com.vekomy.vbooks.hibernate.model.VbMySales;
import com.vekomy.vbooks.hibernate.model.VbNotifications;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.hibernate.model.VbSalesBook;
import com.vekomy.vbooks.hibernate.model.VbSalesBookProducts;
import com.vekomy.vbooks.hibernate.model.VbSystemAlerts;
import com.vekomy.vbooks.hibernate.model.VbSystemAlertsHistory;
import com.vekomy.vbooks.hibernate.model.VbSystemAlertsNotifications;
import com.vekomy.vbooks.hibernate.model.VbTrending;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlerts;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlertsHistory;
import com.vekomy.vbooks.hibernate.model.VbUserDefinedAlertsNotifications;
import com.vekomy.vbooks.util.DateUtils;
import com.vekomy.vbooks.util.ENotification;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Swarupa
 *
 */
public class AlertsDao extends BaseDao {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(AlertsDao.class);
	/**
	 * Integer variable holds myAlertsPageSize.
	 */
	private static Integer myAlertsPageSize = 50;
	/**
	 * Integer variable holds alertHistoryPageSize.
	 */
	private static Integer alertHistoryPageSize = 25;

	/**
	 * This method is responsible for persisting the alert into
	 * {@link VbSystemAlertsHistory}.
	 * 
	 * @param defaultUser - {@link String}
	 * @param alertType - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public void saveSystemAlertsHistory(String defaultUser, String alertType, String userName,
			VbOrganization organization) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
			VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
			if (vbAlertType != null) {
				List<VbSystemAlerts> systemAlertList = getVbSystemAlerts(session, vbAlertType, organization);
				if(!(systemAlertList.isEmpty())) {
					// For configured alerts
					VbSystemAlertsHistory systemAlertsHistory = null;
					for (VbSystemAlerts vbSystemAlerts : systemAlertList) {
						systemAlertsHistory = new VbSystemAlertsHistory();
						systemAlertsHistory.setCreatedOn(date);
						systemAlertsHistory.setCreatedBy(userName);
						systemAlertsHistory.setModifiedOn(date);
						systemAlertsHistory.setVbSystemAlerts(vbSystemAlerts);
						
						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbSystemAlertsHistory");
						}
						session.save(systemAlertsHistory);
					}
				} else {
					// For default recipients.
					VbSystemAlerts systemAlerts = new VbSystemAlerts();
					systemAlerts.setActiveInactive(Boolean.TRUE);
					systemAlerts.setAlertName(alertType);
					systemAlerts.setCreatedBy(userName);
					systemAlerts.setCreatedOn(new Date());
					systemAlerts.setModifiedOn(new Date());
					systemAlerts.setVbAlertType(vbAlertType);
					systemAlerts.setVbOrganization(organization);
						
					if(_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSystemAlerts");
					}
					session.save(systemAlerts);
						
					// Persisting VbSystemAlertsNotifications.
					VbSystemAlertsNotifications alertsNotifications = null;
					VbNotifications vbNotifications = null;
					VbEmployee employee = null;
					VbRole vbRole = null;
					String employeeType = null;
					String roleType = null;
					for (ENotification notificationType : ENotification.values()) {
						alertsNotifications = new VbSystemAlertsNotifications();
						alertsNotifications.setVbSystemAlerts(systemAlerts);
						vbNotifications = getVbNotifications(session, notificationType.name());
						alertsNotifications.setVbNotifications(vbNotifications);
						employee = (VbEmployee) session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("username", defaultUser))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						alertsNotifications.setVbEmployee(employee);
						employeeType = employee.getEmployeeType();
						if("SLE".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_SALESEXECUTIVE_VALUE;
						} else if ("MGT".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_MANAGEMENT_VALUE;
						} else if ("ACC".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_ACOUNTANT_VALUE;
						} else {
							roleType = OrganizationUtils.ROLE_SUPER_MANAGEMENT;
						}
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description", roleType))
								.uniqueResult();
						alertsNotifications.setVbRole(vbRole);
						if(ENotification.Emails.name().equals(notificationType)) {
							alertsNotifications.setMailsTo(Boolean.TRUE);
							alertsNotifications.setMailsCc(Boolean.FALSE);
							alertsNotifications.setMailsBcc(Boolean.FALSE);
						}
							
						if(_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbSystemAlertsNotifications");
						}
						session.save(alertsNotifications);
					}
						
					// Persisting VbSystemAlertsHistory.
					VbSystemAlertsHistory systemAlertsHistory = new VbSystemAlertsHistory();
					systemAlertsHistory.setCreatedOn(date);
					systemAlertsHistory.setCreatedBy(userName);
					systemAlertsHistory.setModifiedOn(date);
					systemAlertsHistory.setVbSystemAlerts(systemAlerts);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbSystemAlertsHistory");
					}
					session.save(systemAlertsHistory);
				}
			}
		txn.commit();
	} catch (HibernateException exception) {
		if(txn != null) {
			txn.rollback();
		}
		String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
		throw new DataAccessException(errorMsg);
	} finally {
		if(session != null) {
			session.close();
		}
	}
}

	/**
	 * This method is responsible for persisting
	 * {@link VbUserDefinedAlertsHistory} into DB.
	 * 
	 * @param defaultUser - {@link String}
	 * @param alertType - {@link String}
	 * @param alertMysales - {@link String}
	 * @param alertMysalesPage - {@link String}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public void saveUserDefinedAlertsHistory(String defaultUser,
			String alertType, String alertMysales, String alertMysalesPage,
			String userName, VbOrganization organization)
			throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
			VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
			if (vbAlertType != null) {
				List<VbUserDefinedAlerts> vbUserDefinedAlertsList = getVbUserDefinedAlerts(session, vbAlertType, organization);
				if (!(vbUserDefinedAlertsList.isEmpty())) {
					// For configured alerts
					for (VbUserDefinedAlerts vbUserDefinedAlerts : vbUserDefinedAlertsList) {
						VbUserDefinedAlertsHistory vbUserDefinedAlertsHistory = new VbUserDefinedAlertsHistory();
						vbUserDefinedAlertsHistory.setCreatedBy(userName);
						vbUserDefinedAlertsHistory.setCreatedOn(date);
						vbUserDefinedAlertsHistory.setModifiedOn(date);
						vbUserDefinedAlertsHistory.setVbUserDefinedAlerts(vbUserDefinedAlerts);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbUserDefinedAlertsHistory");
						}
						session.save(vbUserDefinedAlertsHistory);
					}
				} else {
					// For default recipients.
					VbUserDefinedAlerts userDefinedAlerts = new VbUserDefinedAlerts();
					userDefinedAlerts.setActiveInactive(Boolean.TRUE);
					userDefinedAlerts.setAlertName(alertType);
					userDefinedAlerts.setCreatedBy(userName);
					userDefinedAlerts.setCreatedOn(new Date());
					userDefinedAlerts.setModifiedOn(new Date());
					userDefinedAlerts.setVbAlertType(vbAlertType);
					userDefinedAlerts.setVbOrganization(organization);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbUserDefinedAlerts");
					}
					session.save(userDefinedAlerts);

					// Persisting VbUserDefinedAlertsNotifications.
					VbUserDefinedAlertsNotifications alertsNotifications = null;
					VbNotifications vbNotifications = null;
					VbEmployee employee = null;
					VbRole vbRole = null;
					String employeeType = null;
					String roleType = null;
					for (ENotification notificationType : ENotification.values()) {
						alertsNotifications = new VbUserDefinedAlertsNotifications();
						alertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);
						vbNotifications = getVbNotifications(session, notificationType.name());
						alertsNotifications.setVbNotifications(vbNotifications);		
						employee = (VbEmployee) session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("username", defaultUser))
								.add(Restrictions.eq("vbOrganization", organization))
								.uniqueResult();
						alertsNotifications.setVbEmployee(employee);
						employeeType = employee.getEmployeeType();
						if ("SLE".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_SALESEXECUTIVE_VALUE;
						} else if ("MGT".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_MANAGEMENT_VALUE;
						} else if ("ACC".equalsIgnoreCase(employeeType)) {
							roleType = OrganizationUtils.ROLE_ACOUNTANT_VALUE;
						} else {
							roleType = OrganizationUtils.ROLE_SUPER_MANAGEMENT;
						}
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description", roleType))
								.uniqueResult();
						alertsNotifications.setVbRole(vbRole);
						if (ENotification.Emails.name().equals(notificationType)) {
							alertsNotifications.setMailsTo(Boolean.TRUE);
							alertsNotifications.setMailsCc(Boolean.FALSE);
							alertsNotifications.setMailsBcc(Boolean.FALSE);
						}

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbSystemAlertsNotifications");
						}
						session.save(alertsNotifications);
					}

					// Persisting VbUserDefinedAlertsHistory.
					VbUserDefinedAlertsHistory vbUserDefinedAlertsHistory = new VbUserDefinedAlertsHistory();
					vbUserDefinedAlertsHistory.setCreatedBy(userName);
					vbUserDefinedAlertsHistory.setCreatedOn(date);
					vbUserDefinedAlertsHistory.setModifiedOn(date);
					vbUserDefinedAlertsHistory.setVbUserDefinedAlerts(userDefinedAlerts);

					if (_logger.isDebugEnabled()) {
						_logger.debug("Persisting VbUserDefinedAlertsHistory");
					}
					session.save(vbUserDefinedAlertsHistory);
				}
			}
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to get the mailid's for {@link VbSystemAlerts}
	 * 
	 * @param alertType - {@link String}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return alertsEmailResultList - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getConfiguredSystemAlerts(String alertType,
			String notificationType, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
		if(vbAlertType != null) {
			VbNotifications vbNotifications = getVbNotifications(session, notificationType);
			if(vbNotifications != null) {
				List<VbSystemAlerts> vbSystemAlertsList = getVbSystemAlerts(session, vbAlertType, organization);
				if(!vbSystemAlertsList.isEmpty()) {
					AlertsResult alertsResult = null;
					List<AlertsResult> alertsEmailResultList = new ArrayList<AlertsResult>();
					List<VbSystemAlertsNotifications> systemAlertsNotifications = null;
					for (VbSystemAlerts vbSystemAlerts : vbSystemAlertsList) {
						systemAlertsNotifications = session.createCriteria(VbSystemAlertsNotifications.class)
								.add(Restrictions.eq("vbNotifications", vbNotifications))
								.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
								.list();
						for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotifications) {
							alertsResult = new AlertsResult();
							if(ENotification.Emails.name().equals(notificationType)) {
								alertsResult.setEmailId(vbSystemAlertsNotifications.getVbEmployee().getEmployeeEmail());
								if (vbSystemAlertsNotifications.getMailsCc() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC);
								} else if (vbSystemAlertsNotifications.getMailsBcc() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC);
								} else if (vbSystemAlertsNotifications.getMailsTo() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO);
								}
							} else if (ENotification.In_System_Alert.name().equals(notificationType)) {
								alertsResult.setUserName(vbSystemAlertsNotifications.getVbEmployee().getUsername());
							} else if (ENotification.SMS.name().equals(notificationType)) {
								// TODO: Needs to implement later.
							}
							
							alertsEmailResultList.add(alertsResult);
						}
					}
					session.close();

					if (_logger.isDebugEnabled()) {
						_logger.debug("{} mailids have been configured for alertType: {}", alertsEmailResultList.size(), alertType);
					}
					return alertsEmailResultList;
				} else {
					session.close();
					String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
					
					throw new DataAccessException(errorMsg);
				}
			} else {
				session.close();
				String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
				
				throw new DataAccessException(errorMsg);
			}
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get the mailid's for {@link VbUserDefinedAlerts}.
	 * 
	 * @param alertType - {@link String}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return alertsEmailResultList - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getConfiguredUserDefinedAlerts(String alertType,
			String notificationType, VbOrganization organization)
			throws DataAccessException {
		Session session = this.getSession();
		VbAlertCategory alertCategory = getAlertCategoryForUserDefinedAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, alertCategory);
		if(vbAlertType != null) {
			VbNotifications vbNotifications = getVbNotifications(session, notificationType);
			if(vbNotifications != null) {
				List<VbUserDefinedAlerts> vbUserDefinedAlertsList = getVbUserDefinedAlerts(session, vbAlertType, organization);
				if(!(vbUserDefinedAlertsList.isEmpty())) {
					List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotifications = null;
					AlertsResult alertsResult = null;
					List<AlertsResult> alertsEmailResultList = new ArrayList<AlertsResult>();
					for (VbUserDefinedAlerts vbUserDefinedAlerts : vbUserDefinedAlertsList) {
						userDefinedAlertsNotifications = session.createCriteria(VbUserDefinedAlertsNotifications.class)
								.add(Restrictions.eq("vbNotifications", vbNotifications))
								.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
								.list();
						
						for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userDefinedAlertsNotifications) {
							alertsResult = new AlertsResult();
							if(OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
								alertsResult.setEmailId(vbUserDefinedAlertsNotifications.getVbEmployee().getEmployeeEmail());
								if (vbUserDefinedAlertsNotifications.getMailsCc() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC);
								} else if (vbUserDefinedAlertsNotifications.getMailsBcc() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC);
								} else if (vbUserDefinedAlertsNotifications.getMailsTo() == Boolean.TRUE) {
									alertsResult.setMailRecipientType(OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO);
								}
							} else if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_IN_SYSTEM_ALERT.equalsIgnoreCase(notificationType)) {
								alertsResult.setUserName(vbUserDefinedAlertsNotifications.getVbEmployee().getUsername());
							} else if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_SMS.equalsIgnoreCase(notificationType)) {
								// TODO: Needs to implement later.
							}
							
							alertsEmailResultList.add(alertsResult);
						}
					}
					session.close();

					if (_logger.isDebugEnabled()) {
						_logger.debug("{} mailids have been configured for alertType: {}", alertsEmailResultList.size(), alertType);
					}
					return alertsEmailResultList;
				} else {
					session.close();
					String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
					
					throw new DataAccessException(errorMsg);
				}
			} else {
				session.close();
				String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
				
				if(_logger.isWarnEnabled()) {
					_logger.warn(errorMsg);
				}
				throw new DataAccessException(errorMsg);
			}
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get all the mobile nos based on alert type.
	 * 
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return mobileNoList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMobileNoByAlertTypeForSystemAlerts(String alertType,
			VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
		if(vbAlertType != null) {
			List<String> mobileNoList = null;
			List<VbSystemAlerts> vbSystemAlertsList = getVbSystemAlerts(session, vbAlertType, organization);
			if(!(vbSystemAlertsList.isEmpty())) {
				VbNotifications vbNotifications = getVbNotifications(session, ENotification.SMS.name());
				mobileNoList = new ArrayList<String>();
				VbEmployee vbEmployee = null;
				String mobileNo = null;
				for (VbSystemAlerts vbSystemAlerts : vbSystemAlertsList) {
					List<VbSystemAlertsNotifications> systemAlertsNotifications = session
							.createCriteria(VbSystemAlertsNotifications.class)
							.add(Restrictions.eq("vbNotifications", vbNotifications))
							.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts)).list();
					for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotifications) {
						vbEmployee = vbSystemAlertsNotifications.getVbEmployee();
						mobileNo = (String) session.createCriteria(VbEmployeeDetail.class)
								.createAlias("vbEmployee", "employee")
								.add(Restrictions.eq("vbEmployee", vbEmployee))
								.add(Restrictions.eq("employee.vbOrganization", organization))
								.uniqueResult();
						mobileNoList.add(mobileNo);
					}
				}
				session.close();

				if (_logger.isDebugEnabled()) {
					_logger.debug("{} mobile nos are available for alert type: {}", mobileNoList.size(), alertType);
				}
			} else {
				session.close();
				String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
				
				throw new DataAccessException(errorMsg);
			}
			return mobileNoList;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get the mail id associated with particular user.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return emailId - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	public String getEmailIDByName(String userName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String emailId = (String) session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("employeeEmail"))
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		session.close();
		
		if(emailId != null) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} associated with user {}", emailId, userName);
			}
			return emailId;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is used to get the alert type list.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return alertTypeList - {@link List}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertType() throws DataAccessException {
		Session session = this.getSession();
		List<String> alertTypeList = session.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", getAlertCategoryForSystemAlerts(session)))
				.list();
		session.close();
		
		if(!(alertTypeList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} alert types have been configured.", alertTypeList.size());
			}
			return alertTypeList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg); 
		}
	}

	/**
	 * This method is responsible to get the list of configured groups.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return groupList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getConfiguredGroups(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<String> groupList = session.createCriteria(VbRole.class)
				.setProjection(Projections.property("description"))
				.add(Restrictions.ne("description", "User"))
				.add(Restrictions.ne("description", "Site Administrator"))
				.add(Restrictions.ne("description", "Group Head")).list();
		session.close();
		
		if(!(groupList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} groups have been configured.", groupList.size());
			}
			return groupList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg); 
		}
	}

	/**
	 * This method is responsible to get the associated users under particular group.
	 * 
	 * @param group - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return userList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAssociatedUsers(String group, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		String empType = OrganizationUtils.getEmployeeTypeByRole(group);
		List<String> userNamesList = session.createCriteria(VbEmployee.class)
				.setProjection(Projections.property("username"))
				.add(Restrictions.eq("employeeType", empType))
				.add(Restrictions.eq("vbOrganization", organization))
				.list();
		VbLogin login = null;
		List<String> enabledUserNamesList = new ArrayList<String>();
		for (String userName : userNamesList) {
			login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("vbOrganization", organization))
					.add(Restrictions.eq("enabled", OrganizationUtils.LOGIN_ENABLED))
					.uniqueResult();
			if(login != null) {
				enabledUserNamesList.add(userName);
			}
		}
		session.close();
		
		if(!enabledUserNamesList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} users have been found under {}", enabledUserNamesList.size(), group);
			}
			return enabledUserNamesList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This Method is responsible To save SystemDefined Alerts - {@link VbSystemAlerts}.
	 * 
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param notificationType - {@link String}
	 * @param group - {@link String}
	 * @param userNames - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void saveSystemDefinedAlert(AlertsCommand alertsCommand,	String notificationType, String group, String userNames,
			VbOrganization organization, String createdBy) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			String groupDropdown = null;
			Date date = new Date();
			// Persisting VbSystemAlerts.
			VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
			VbAlertType alertType = getVbAlertType(session,	alertsCommand.getAlertType(), alertCategory);
			VbSystemAlerts systemAlerts = new VbSystemAlerts();
			systemAlerts.setAlertName(alertsCommand.getAlertName());
			systemAlerts.setActiveInactive(Boolean.TRUE);
			systemAlerts.setCreatedBy(createdBy);
			systemAlerts.setCreatedOn(date);
			systemAlerts.setModifiedOn(date);
			systemAlerts.setDescription(alertsCommand.getDescription());
			systemAlerts.setVbAlertType(alertType);
			systemAlerts.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSystemAlerts");
			}
			session.save(systemAlerts);
			
			//Persisting VbSystemAlerts Notifications.
			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			List<VbEmployee> vbEmployeeList = null;
			VbSystemAlertsNotifications systemAlertsNotifications = null;
			VbRole vbRole = null;
			String empType = null;
			//Iterating for every notification type
			for (String notification : notificationList) {
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				//For Specified Groups.
				if (group != "") {
					String[] groupReceipient = group.split("/");
					String groupName = groupReceipient[0];
					if (ENotification.Emails.name().equals(notificationType)) {
						groupDropdown = groupReceipient[1];
					}

					vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupName))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupName);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization)).list();
					//no selected users(for all users).
					if (userNames.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupDropdown)) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupDropdown)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupDropdown)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications");
							}
							session.save(systemAlertsNotifications);
						}
					} else { //for specified users.
						String[] userNamesArray = userNames.split(",");
						List<String> userNamesList = Arrays.asList(userNamesArray);
						String[] userNameReceipient = null;
						for (String userName : userNamesList) {
							userNameReceipient = userName.split("/");

							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization", organization)).uniqueResult();
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);
							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupDropdown)) {
										systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupDropdown)) {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.TRUE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications");
							}
							session.save(systemAlertsNotifications);
						}
					}
				} else { //no selected groups(for all groups associated with all users).
					List<String> configuredGroups = getConfiguredGroups(organization);
					for (String configuredGroup : configuredGroups) {
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description",	configuredGroup)).uniqueResult();
						empType = OrganizationUtils.getEmployeeTypeByRole(configuredGroup);
						vbEmployeeList = session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("employeeType", empType))
								.add(Restrictions.eq("vbOrganization", organization)).list();
						for (VbEmployee vbEmployee : vbEmployeeList) {
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								systemAlertsNotifications.setMailsTo(Boolean.TRUE);
								systemAlertsNotifications.setMailsCc(Boolean.FALSE);
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications");
							}
							session.save(systemAlertsNotifications);
						}
					}
				}
			}
			txn.commit();
		} catch(HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMessage = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMessage);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to get the alert category for system alerts.
	 * 
	 * @param session - {@link Session}
	 * @return alertCategory - {@link VbAlertCategory}
	 */
	private VbAlertCategory getAlertCategoryForSystemAlerts(Session session) {
		
		VbAlertCategory alertCategory = (VbAlertCategory) session.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory", OrganizationUtils.SYSTEM_ALERT))
				.uniqueResult();

		return alertCategory;
	}

	/**
	 * This method is responsible to get the alert category for user defined alerts.
	 * 
	 * @param session - {@link Session}
	 * @return alertCategory - {@link VbAlertCategory}
	 */ 
	private VbAlertCategory getAlertCategoryForUserDefinedAlerts(Session session) {
		
		VbAlertCategory alertCategory = (VbAlertCategory) session.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory", OrganizationUtils.USER_DEFINED_ALERT))
				.uniqueResult();

		return alertCategory;
	}

	/**
	 * This method is responsible to get the alert type.
	 * 
	 * @param session - {@link Session}
	 * @param alertType - {@link String}
	 * @param alertCategory - {@link VbAlertCategory}
	 * @return vbAlertType - {@link VbAlertType}
	 */
	private VbAlertType getVbAlertType(Session session, String alertType, VbAlertCategory alertCategory) {
		VbAlertType vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
				.add(Restrictions.eq("alertType", alertType))
				.add(Restrictions.eq("vbAlertCategory", alertCategory))
				.uniqueResult();
		Hibernate.initialize(vbAlertType);
		return vbAlertType;
	}
	
	/**
	 * This Method is To Retrieve SalesPageTypes.
	 * 
	 * @param value - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return approvalTypeList - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMysalesTypes(String value, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbAlertTypeMySales alertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
				.add(Restrictions.eq("alertMySales", value).ignoreCase())
				.uniqueResult();
		List<String> approvalTypeList = null;
		if (alertTypeMySales != null) {
			approvalTypeList = session.createCriteria(VbAlertTypeMySalesPage.class)
					.setProjection(Projections.property("alertMySalesPage"))
					.add(Restrictions.eq("vbAlertTypeMySales", alertTypeMySales))
					.list();
			session.close();

			if (_logger.isDebugEnabled()) {
				_logger.debug("{} approvals are configured for mysales type: {}", approvalTypeList.size(), value);
			}
			return approvalTypeList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		}

	}

	/**
	 * This Method is To Retrieve All The Existed UserDefined AlertTypes.
	 * 
	 * @return userDefinedAlertsList - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUserDefinedAlerts() throws DataAccessException {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
		List<String> userDefinedAlertsList = session.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
				.list();
		session.close();
		if(!(userDefinedAlertsList.isEmpty())) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Configured alert types are {}", userDefinedAlertsList);
			}
			return userDefinedAlertsList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		}
		
	}
	
	/**
	 * This Method Is To retrieve all Existed mysalestypes.
	 * 
	 * @return mySalesTypesList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getMySalesTypes() throws DataAccessException {
		Session session = this.getSession();
		List<String> mySalesTypesList = session.createCriteria(VbAlertTypeMySales.class)
				.setProjection(Projections.property("alertMySales"))
				.list();
		session.close();
		
		if(!(mySalesTypesList.isEmpty())) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} alert types have been cinfigured under mysales.", mySalesTypesList.size());
			}
			return mySalesTypesList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		}
		
	}

	/**
	 * This Method is to Retrieve SystemAlerts.
	 * 
	 * @param session - {@link Session}
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return systemAlerts - {@link VbSystemAlerts}
	 */ 
	@SuppressWarnings("unchecked")
	private List<VbSystemAlerts> getVbSystemAlerts(Session session,	VbAlertType alertType, VbOrganization organization) {
		List<VbSystemAlerts> systemAlerts = session.createCriteria(VbSystemAlerts.class)
				.add(Restrictions.eq("vbAlertType", alertType))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("activeInactive", Boolean.TRUE))
				.list();

		return systemAlerts;
	}

	/**
	 * This method id responsible to get {@link VbNotifications} instance.
	 * 
	 * @param session - {@link Session}
	 * @param notificationType - {@link String}
	 * @return notifications - {@link VbNotifications}
	 * 
	 */
	private VbNotifications getVbNotifications(Session session, String notificationType) {
		VbNotifications notifications = (VbNotifications) session.createCriteria(VbNotifications.class)
				.add(Restrictions.eq("notificationType", notificationType))
				.uniqueResult();

		return notifications;
	}

	/**
	 * This method is responsible to get {@link VbUserDefinedAlerts} instance.
	 * 
	 * @param session - {@link Session}
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return userDefinedAlerts - {@link VbUserDefinedAlerts}
	 * 
	 */
	@SuppressWarnings("unchecked")
	private List<VbUserDefinedAlerts> getVbUserDefinedAlerts(Session session, VbAlertType alertType, VbOrganization organization) {
		List<VbUserDefinedAlerts> userDefinedAlerts = (List<VbUserDefinedAlerts>) session.createCriteria(VbUserDefinedAlerts.class)
				.add(Restrictions.eq("vbAlertType", alertType))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("activeInactive", Boolean.TRUE))
				.list();

		return userDefinedAlerts;
	}

	/**
	 * This Method is To Assign Multiple NotificationTypes For Mutiple groups
	 * 
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param multiGroupMap - {@link HashMap}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void saveSystemDefinedAlertForMultipleGroups(AlertsCommand alertsCommand, HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization, String createdBy) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			// Persisting VbSystemAlerts.
			VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
			VbAlertType alertType = getVbAlertType(session,	alertsCommand.getAlertType(), alertCategory);
			VbSystemAlerts systemAlerts = new VbSystemAlerts();
			systemAlerts.setAlertName(alertsCommand.getAlertName());
			systemAlerts.setActiveInactive(Boolean.TRUE);
			systemAlerts.setCreatedBy(createdBy);
			systemAlerts.setCreatedOn(date);
			systemAlerts.setModifiedOn(date);
			systemAlerts.setDescription(alertsCommand.getDescription());
			systemAlerts.setVbAlertType(alertType);
			systemAlerts.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSystemAlerts: {}", systemAlerts);
			}
			session.save(systemAlerts);
			//Persisting VbSystemAlertsNotifications.
			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			String group = null;
			String groupUsers = null;
			String[] groupUser = null;
			String[] groupNames = null;
			List<String> userGroup = null;
			String groupReceipient = null;
			String groupRecipients [] = null;
			String empType = null;
			VbSystemAlertsNotifications systemAlertsNotifications = null;
			List<VbEmployee> vbEmployeeList = null;
			//Iterating for every notification type
			for (String notification : notificationList) {
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
					group = entry.getKey();
					groupUsers = entry.getValue();
					groupNames = group.split(",");
					userGroup = Arrays.asList(groupNames);
					for(String groupName : userGroup){
						groupRecipients = groupName.split("/");
					
					VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupRecipients[0]))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupRecipients[0]);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();

					if (ENotification.Emails.name().equals(notificationType)) {
						groupReceipient = groupRecipients[1];
					}
					groupUser = groupUsers.split(",");
					List<String> groupUserList = Arrays.asList(groupUser);
					//no selected users(for all users).
					if (groupUsers.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications");
							}
							session.save(systemAlertsNotifications);
						}
					} else { //for specific users.
						String[] userNameReceipient = null;
						for (String userName : groupUserList) {
							userNameReceipient = userName.split("/");

							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization", organization)).uniqueResult();
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);
							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
										systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.TRUE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications For Multiple USers");
							}
							session.save(systemAlertsNotifications);
						} // closing 3rd for
					} // closing else

				} // closing 2nd for

			} // closing 1st for

			txn.commit();
		} 
		}catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			 throw new DataAccessException(errorMsg);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This Method is To Configure User defined Alerts.
	 * 
	 * @param notificationType - {@link String}
	 * @param group - {@link String}
	 * @param userNames - {@link String}
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param userDefinedAlerts - {@link VbUserDefinedAlertsNotifications}
	 * @param vbOrganization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param salesTypes - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void saveUserDefinedAlerts(String notificationType, String group,
			String userNames, AlertsCommand alertsCommand,
			VbUserDefinedAlertsNotifications userDefinedAlerts,
			VbOrganization vbOrganization, String userName, String salesTypes) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			String groupDropdown = null;
			Date date = new Date();
			VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications = null;
			VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
			String alertType = alertsCommand.getAlertType();
			VbAlertTypeMySales vbAlertTypeMySales =null;
			VbAlertTypeMySalesPage vbAlertTypeMySalesPage = null;
			List<VbAlertTypeMySalesPage> vbAlertTypeMySalesPages = null;
			VbAlertType vbAlertType =getVbAlertType(session, alertType, vbAlertCategory);
			
			//Persisting VbUserDefinedAlerts.
			VbUserDefinedAlerts  vbUserDefinedAlerts =new VbUserDefinedAlerts();
			vbUserDefinedAlerts.setAlertName(alertsCommand.getAlertName());
			vbUserDefinedAlerts.setDescription(alertsCommand.getDescription());
			vbUserDefinedAlerts.setCreatedBy(userName);
			vbUserDefinedAlerts.setCreatedOn(date);
			vbUserDefinedAlerts.setModifiedBy(userName);
			vbUserDefinedAlerts.setVbAlertType(vbAlertType);
			vbUserDefinedAlerts.setModifiedOn(date);
			vbUserDefinedAlerts.setActiveInactive(Boolean.TRUE);
			vbUserDefinedAlerts.setVbOrganization(vbOrganization);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbUserDefinedAlerts");
			}
			session.save(vbUserDefinedAlerts);
			
			//checking for mysales.
			if(alertType.equals("My Sales")) {
				VbMySales vbMySales =null;
				//particular mysales type is selected.
				if(salesTypes != null) {
					String[] salesTypesListArray =salesTypes.split(",");
					List<String> salesTypeList=	Arrays.asList(salesTypesListArray);
					for (String salesType : salesTypeList) {
						vbMySales = new VbMySales();
						String salesTypesValue = alertsCommand.getAlertMySales();
						
						vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
								.add(Restrictions.eq("alertMySales",salesTypesValue))
								.uniqueResult();
						
						vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
								.add(Restrictions.eq("alertMySalesPage", salesType))
								.add(Restrictions.eq("vbAlertTypeMySales",	vbAlertTypeMySales))
								.uniqueResult();
						
						vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
						
						vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
								.add(Restrictions.eq("alertType", alertType))
								.uniqueResult();
	
						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);
						
						if(_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbMySales");
						}
						session.save(vbMySales);
					}
				} else { //if no mysales type is selected.
					String salesTypesValue = alertsCommand.getAlertMySales();
					vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
							.add(Restrictions.eq("alertMySales", salesTypesValue))
							.uniqueResult();
					vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
							.add(Restrictions.eq("alertType", alertType))
							.uniqueResult();
					vbAlertTypeMySalesPages = (List<VbAlertTypeMySalesPage>) session.createCriteria(VbAlertTypeMySalesPage.class)
							.add(Restrictions.eq("vbAlertTypeMySales",	vbAlertTypeMySales))
							.list();
					for (VbAlertTypeMySalesPage alertTypeMySalesPage : vbAlertTypeMySalesPages) {
						vbMySales = new VbMySales();
						vbMySales.setVbAlertTypeMySalesPage(alertTypeMySalesPage);
						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);
							
						if(_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbMySales");
						}
						session.save(vbMySales);
					}
				}
					
		} else if(alertType.equals("Trending")) {
				VbTrending vbTrending = new VbTrending();//checking for Trending.
				vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
				vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
				vbTrending.setDescription(alertsCommand.getDescription());
				vbTrending.setVbUserDefinedAlerts(vbUserDefinedAlerts);
					
				if(_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbTrending");
				}
				session.save(vbTrending);
			} else if (alertType.equals("Excess Amount")) {
				VbExcessCash vbExcessCash = new VbExcessCash();//checking for ExcessAmount.
				vbExcessCash.setAmount(alertsCommand.getAmount());
				vbExcessCash.setDescription(alertsCommand.getDescription());
				vbExcessCash.setVbUserDefinedAlerts(vbUserDefinedAlerts);
					
				if(_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbExcessCash");
				}
				session.save(vbExcessCash);
			}
		String[] notifications = notificationType.split(",");
		List<String> notificationList = Arrays.asList(notifications);
		VbNotifications vbNotifications = null;
		List<VbEmployee> vbEmployeeList = null;
		VbRole vbRole = null;
		String empType = null;
		//Iterating for every notification type
			for (String notification : notificationList) {
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				// For Specified Groups.
				if (group != "") {
					String[] groupReceipient = group.split("/");
					String groupName = groupReceipient[0];
					if (ENotification.Emails.name().equals(notificationType)) {
						groupDropdown = groupReceipient[1];
					}

					vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupName))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupName);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", vbOrganization))
							.list();
					// no selected users(for all users).
					if (userNames.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							vbUserDefinedAlertsNotifications.setVbRole(vbRole);
							vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(vbUserDefinedAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupDropdown)) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupDropdown)) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupDropdown)) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserDefinedAlertsNotifications");
							}
							session.save(vbUserDefinedAlertsNotifications);
						}
					} else {
						String[] userNamesArray = userNames.split(",");
						List<String> userNamesList = Arrays.asList(userNamesArray);
						String[] userNameReceipient = null;
						for (String userName1 : userNamesList) {
							userNameReceipient = userName1.split("/");
							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization", vbOrganization)).uniqueResult();
							vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							vbUserDefinedAlertsNotifications.setVbRole(vbRole);
							vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(vbUserDefinedAlerts);
							if (ENotification.Emails.name().equalsIgnoreCase(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equals(groupDropdown)) {
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equals(groupDropdown)) {
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserDefinedAlertsNotifications");
							}
							session.save(vbUserDefinedAlertsNotifications);
						}
					}
				} else { // no selected groups(for all groups associated with
							// all users).
					List<String> configuredGroups = getConfiguredGroups(vbOrganization);
					for (String configuredGroup : configuredGroups) {
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description",	configuredGroup)).uniqueResult();
						empType = OrganizationUtils.getEmployeeTypeByRole(configuredGroup);
						vbEmployeeList = session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("employeeType", empType))
								.add(Restrictions.eq("vbOrganization", vbOrganization)).list();
						for (VbEmployee vbEmployee : vbEmployeeList) {
							vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							vbUserDefinedAlertsNotifications.setVbRole(vbRole);
							vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(vbUserDefinedAlerts);

							if (ENotification.Emails.name().equalsIgnoreCase(notificationType)) {
								vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
								vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserDefinedAlertsNotifications");
							}
							session.save(vbUserDefinedAlertsNotifications);
						}
					}
				}
			}
			txn.commit();
		} catch(HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String errorMessage = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
				
			throw new DataAccessException(errorMessage);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}

	/**
	 * This Method is To Assign Multiple Notifications for multiple groups in case of 
	 * UserDefined Alerts 
	 * 
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param multiGroupMap - {@link HashMap}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @param salesType 
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void saveUserDefinedAlertForMultipleGroups(AlertsCommand alertsCommand, HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization,
			String createdBy, String salesTypes) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			txn = session.beginTransaction();
			Date date = new Date();
			String alertType = alertsCommand.getAlertType();
			VbAlertTypeMySales vbAlertTypeMySales = null;
			VbAlertTypeMySalesPage vbAlertTypeMySalesPage = null;
			List<VbAlertTypeMySalesPage> vbAlertTypeMySalesPages = null;
			// Persisting VbUserDefinedAlerts.
			VbAlertCategory alertCategory = getAlertCategoryForUserDefinedAlerts(session);
			VbAlertType vbAlertType = getVbAlertType(session, alertsCommand.getAlertType(), alertCategory);
			VbUserDefinedAlerts vbUserDefinedAlerts = new VbUserDefinedAlerts();
			vbUserDefinedAlerts.setAlertName(alertsCommand.getAlertName());
			vbUserDefinedAlerts.setActiveInactive(Boolean.TRUE);
			vbUserDefinedAlerts.setCreatedBy(createdBy);
			vbUserDefinedAlerts.setCreatedOn(date);
			vbUserDefinedAlerts.setModifiedOn(date);
			vbUserDefinedAlerts.setDescription(alertsCommand.getDescription());
			vbUserDefinedAlerts.setVbAlertType(vbAlertType);
			vbUserDefinedAlerts.setVbOrganization(organization);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting vbUserDefinedAlerts");
			}
			session.save(vbUserDefinedAlerts);
			
			// checking for mysales.
			if (alertType.equals("My Sales")) {
				VbMySales vbMySales = null;
				// particular mysales type is selected.
				if (salesTypes != null) {
					String[] salesTypesListArray = salesTypes.split(",");
					List<String> salesTypeList = Arrays.asList(salesTypesListArray);
					for (String salesType : salesTypeList) {
						vbMySales = new VbMySales();
						String salesTypesvlaue = alertsCommand.getAlertMySales();
						
						vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
								.add(Restrictions.eq("alertMySales", salesTypesvlaue))
								.uniqueResult();
						
						vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
								.add(Restrictions.eq("alertMySalesPage", salesType))
								.add(Restrictions.eq("vbAlertTypeMySales",	vbAlertTypeMySales))
								.uniqueResult();
						
						vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
						
						vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
								.add(Restrictions.eq("alertType", alertType))
								.uniqueResult();

						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbMySales: {}", vbMySales);
						}
						session.save(vbMySales);
					}
				} else { // if no mysales type is selected.
					String salesTypesvlaue = alertsCommand.getAlertMySales();
					
					vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
							.add(Restrictions.eq("alertMySales", salesTypesvlaue))
							.uniqueResult();
					
					vbAlertType = (VbAlertType) session.createCriteria(VbAlertType.class)
							.add(Restrictions.eq("alertType", alertType))
							.uniqueResult();
					
					vbAlertTypeMySalesPages = (List<VbAlertTypeMySalesPage>) session.createCriteria(VbAlertTypeMySalesPage.class)
							.add(Restrictions.eq("vbAlertTypeMySales",	vbAlertTypeMySales))
							.list();
					
					for (VbAlertTypeMySalesPage alertTypeMySalesPage : vbAlertTypeMySalesPages) {
						vbMySales = new VbMySales();
						vbMySales.setVbAlertTypeMySalesPage(alertTypeMySalesPage);
						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbUserDefinedAlerts(vbUserDefinedAlerts);

						if (_logger.isDebugEnabled()) {
							_logger.debug("Persisting VbMySales: {}", vbMySales);
						}
						session.save(vbMySales);
					}
				}

			} else if (alertType.equals("Trending")) { // checking for Trending.
				VbTrending vbTrending = new VbTrending();
				vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
				vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
				vbTrending.setDescription(alertsCommand.getDescription());
				vbTrending.setVbUserDefinedAlerts(vbUserDefinedAlerts);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbTrending: {}", vbTrending);
				}

				session.save(vbTrending);
			} else if (alertType.equals("Excess Amount")) { // checking for ExcessAmount.
				VbExcessCash vbExcessCash = new VbExcessCash();
				vbExcessCash.setAmount(alertsCommand.getAmount());
				vbExcessCash.setDescription(alertsCommand.getDescription());
				vbExcessCash.setVbUserDefinedAlerts(vbUserDefinedAlerts);
				
				if (_logger.isDebugEnabled()) {
					_logger.debug("Persisting VbExcessCash: {}", vbExcessCash);
				}
				session.save(vbExcessCash);
			}

			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			String group = null;
			String groupUsers = null;
			String[] groupUser = null;
			String[] groupNames = null;
			List<String> userGroup = null;
			String groupReceipient = null;
			String[] groupRecipients = null; 
			String empType = null;
			VbUserDefinedAlertsNotifications userDefinedAlertsNotifications = null;
			List<VbEmployee> vbEmployeeList = null;
			// Iterating for each notification type.
			for (String notification : notificationList) {

				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
					group = entry.getKey();
					groupUsers = entry.getValue();
					groupNames = group.split(",");
					userGroup = Arrays.asList(groupNames);
					for(String groupName : userGroup){
						groupRecipients = groupName.split("/");
					
					VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupRecipients[0]))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupRecipients[0]);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();

					if (ENotification.Emails.name().equals(notificationType)) {
						groupReceipient = groupRecipients[1];
					}
					groupUser = groupUsers.split(",");
					
					List<String> groupUserList = Arrays.asList(groupUser);
					/*group = entry.getKey();
					groupUsers = entry.getValue();
					groupNames = group.split("/");
					userGroup = groupNames[0];
					VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", userGroup))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(userGroup);
					vbEmployeeList = session
							.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();

					if (ENotification.Emails.name().equals(notificationType)) {
						groupReceipient = groupNames[1];
					}
					groupUser = groupUsers.split(",");
					List<String> groupUserList = Arrays.asList(groupUser);*/
					// no selected users(for all users).
					if (groupUsers.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							userDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							userDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							userDefinedAlertsNotifications.setVbRole(vbRole);
							userDefinedAlertsNotifications.setVbUserDefinedAlerts(vbUserDefinedAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserDefinedAlertsNotifications");
							}
							session.save(userDefinedAlertsNotifications);
						}
					} else { // for specific users.
						String[] userNameReceipient = null;
						for (String userName : groupUserList) {
							userNameReceipient = userName.split("/");

							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization",	organization)).uniqueResult();
							userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							userDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							userDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							userDefinedAlertsNotifications.setVbRole(vbRole);
							userDefinedAlertsNotifications.setVbUserDefinedAlerts(vbUserDefinedAlerts);
							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserdDefinedAlertsNotifications For Multiple USers");
							}
							session.save(userDefinedAlertsNotifications);
						} // closing 3rd for
					} // closing else

				} // closing 2nd for

			} // closing 1st for
			}
			txn.commit();
		} catch (HibernateException exception) {
			if (txn != null) {
				txn.rollback();
			}
			String errorMsg = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * This method is responsible to get the configured alert categories.
	 * 
	 * @return alertCategoryList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertCategory() throws DataAccessException {
		Session session = this.getSession();
		List<String> alertCategoryList = session.createCriteria(VbAlertCategory.class)
				.setProjection(Projections.property("alertCategory"))
				.list();
		session.close();
		
		if(!alertCategoryList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} alert categories have been configured.",	alertCategoryList.size());
			}
			return alertCategoryList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);

			throw new DataAccessException(errorMsg);
		}
		
	}

	/**
	 * This method is responsible to get the alert types based on the {@link VbAlertCategory}.
	 * 
	 * @param alertCategory - {@link String}
	 * @return alertTypeList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAlertType(String alertCategory) throws DataAccessException {
		Session session = this.getSession();
		VbAlertCategory vbAlertCategory = (VbAlertCategory) session.createCriteria(VbAlertCategory.class)
				.add(Restrictions.eq("alertCategory", alertCategory))
				.uniqueResult();
		List<String> alertTypeList = session.createCriteria(VbAlertType.class)
				.setProjection(Projections.property("alertType"))
				.add(Restrictions.eq("vbAlertCategory", vbAlertCategory))
				.list();
		session.close();
		
		if(!alertTypeList.isEmpty()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("{} alert types have been Configured for alertCategory {}", alertTypeList.size(), alertCategory);
			}
			return alertTypeList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg); 
		}
	}

	/**
	 * This method is responsible to get all the configured alerts.
	 * 
	 * @return alertResultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<ViewAlertResult> getAllAlerts(String alertCategory, String alertType, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		ViewAlertResult alertResult = null;
		List<ViewAlertResult> alertResultList = new ArrayList<ViewAlertResult>();
		Integer count = new Integer(0);
		List<VbSystemAlerts> systemAlertsList = null;
		List<VbUserDefinedAlerts> userDefinedAlertsList = null;
		
		//Fetching System defined alerts.
		if(alertCategory == null && alertType == null) {
			systemAlertsList = session.createCriteria(VbSystemAlerts.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
		} else if(alertCategory != null && alertType.equals("-1")) {
			systemAlertsList = session.createQuery("FROM VbSystemAlerts vb WHERE vb.vbAlertType.vbAlertCategory.alertCategory = :alertCategory AND vb.vbOrganization = :organization")
					.setParameter("alertCategory", alertCategory)
					.setParameter("organization", organization)
					.list();
		} else {
			systemAlertsList = session.createQuery("FROM VbSystemAlerts vb WHERE vb.vbAlertType.vbAlertCategory.alertCategory = :alertCategory AND vb.vbAlertType.alertType = :alertType AND vb.vbOrganization = :organization")
					.setParameter("alertCategory", alertCategory)
					.setParameter("alertType", alertType)
					.setParameter("organization", organization)
					.list();
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Configured system defined alerts list size is: {}", systemAlertsList.size());
		}
		String description = null;
		for (VbSystemAlerts vbSystemAlerts : systemAlertsList) {
			alertResult = new ViewAlertResult();
			alertResult.setId(vbSystemAlerts.getId());
			alertResult.setAlertCategory(vbSystemAlerts.getVbAlertType().getVbAlertCategory().getAlertCategory());
			alertResult.setAlertName(vbSystemAlerts.getAlertName());
			alertResult.setAlertType(vbSystemAlerts.getVbAlertType().getAlertType());
			description = vbSystemAlerts.getDescription();
			if(description == null) {
				description = "";
			}
			alertResult.setAlertDescription(description);
			alertResult.setAlertEnableDisable(vbSystemAlerts.getActiveInactive());
			alertResult.setAlertCriteria("NA");
			count = (Integer) session.createQuery("SELECT COUNT(*) FROM VbSystemAlertsHistory vb WHERE vb.vbSystemAlerts = :vbSystemAlerts GROUP BY vb.vbSystemAlerts")
					.setParameter("vbSystemAlerts", vbSystemAlerts)
					.uniqueResult();
			if(count == null) {
				count = 0;
			}
			alertResult.setAlertCount(count);
			alertResultList.add(alertResult);
		}
		
		//Fetching user defined alerts.
		if(alertCategory == null && alertType == null) {
			userDefinedAlertsList = session.createCriteria(VbUserDefinedAlerts.class)
					.add(Restrictions.eq("vbOrganization", organization))
					.list();
		} else if(alertCategory != null && alertType.equals("-1")) {
			userDefinedAlertsList = session.createQuery("FROM VbUserDefinedAlerts vb WHERE vb.vbAlertType.vbAlertCategory.alertCategory = :alertCategory AND vb.vbOrganization = :organization")
					.setParameter("alertCategory", alertCategory)
					.setParameter("organization", organization)
					.list();
		} else {
			userDefinedAlertsList = session.createQuery("FROM VbUserDefinedAlerts vb WHERE vb.vbAlertType.vbAlertCategory.alertCategory = :alertCategory AND vb.vbAlertType.alertType = :alertType AND vb.vbOrganization = :organization")
					.setParameter("alertCategory", alertCategory)
					.setParameter("alertType", alertType)
					.setParameter("organization", organization)
					.list();
		}
		
		if(_logger.isDebugEnabled()) {
			_logger.debug("Configured user defined alerts list size is: {}", userDefinedAlertsList.size());
		}
		
		for (VbUserDefinedAlerts vbUserDefinedAlerts : userDefinedAlertsList) {
			alertResult = new ViewAlertResult();
			String alertCriteria = null;
			String alertTypeVal = vbUserDefinedAlerts.getVbAlertType().getAlertType();
			
			if(alertTypeVal.equals("Trending")) {
				Long amountPersentage = (long) 0;
				Long productPersentage = (long) 0;
				VbTrending vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.uniqueResult();
				if(vbTrending != null){
					 amountPersentage = vbTrending.getAmountPersentage();
					 productPersentage = vbTrending.getProductPercentage();
				}
				alertCriteria = "amount% "+ amountPersentage + ", product% " + productPersentage;
			} else if(alertTypeVal.equals("Excess Amount")) {
				Float excessAmount = (Float) session.createCriteria(VbExcessCash.class)
						.setProjection(Projections.property("amount"))
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.uniqueResult();
				alertCriteria = "Excess Amount :" +excessAmount;
			} else {
				List<VbMySales> vbMySales = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.list();
				for (VbMySales mySales : vbMySales) {
					alertCriteria = "Mysales:" +mySales.getVbAlertTypeMySales().getAlertMySales();
				}
				
			}
			alertResult.setId(vbUserDefinedAlerts.getId());
			alertResult.setAlertCategory(vbUserDefinedAlerts.getVbAlertType().getVbAlertCategory().getAlertCategory());
			alertResult.setAlertName(vbUserDefinedAlerts.getAlertName());
			alertResult.setAlertType(alertTypeVal);
			alertResult.setAlertDescription(vbUserDefinedAlerts.getDescription());
			alertResult.setAlertEnableDisable(vbUserDefinedAlerts.getActiveInactive());
			alertResult.setAlertCriteria(alertCriteria);
			count = (Integer) session.createQuery("SELECT COUNT(*) FROM VbUserDefinedAlertsHistory vb WHERE vb.vbUserDefinedAlerts = :vbUserDefinedAlerts GROUP BY vb.vbUserDefinedAlerts")
					.setParameter("vbUserDefinedAlerts", vbUserDefinedAlerts)
					.uniqueResult();
					
			if(count == null) {
				count = 0;
			}
			alertResult.setAlertCount(count);
			alertResultList.add(alertResult);
		}
		session.close();
		if(!(alertResultList.isEmpty())) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("Configured alerts list size is: {}", alertResultList.size());
			}
			return alertResultList;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		
	}
	
	

	/**
	 * This method is responsible to get {@link VbTrending} instance.
	 * 
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return vbTrending - {@link VbTrending}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public VbTrending getVbTrending(String alertType, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		VbTrending vbTrending = null;
		VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
		if(vbAlertCategory != null) {
			VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
			if(vbAlertType != null) {
				VbUserDefinedAlerts vbUserDefinedAlerts = getVbUserDefinedAlerts(session, vbAlertType, organization).get(0);
				if(vbUserDefinedAlerts != null) {
					vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
							.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
							.uniqueResult();
				}
			}
		}
		session.close();
		
		if(vbTrending != null) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbTrending: {}", vbTrending);
			}
			return vbTrending;
		} else {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to get today's and yesterday's soldQty from
	 * {@link VbSalesBookProducts} based on SalesExecutive and
	 * {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return amountTrendingResult - {@link TrendingAlertResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public TrendingAlertResult getProductTrendingAlertResult(VbOrganization organization) throws DataAccessException{
		
		Session session = this.getSession();
		Date date = new Date();
		
		// For Yesterday's SoldQty.
		VbSalesBook yesterdaySalesBook = getVbSalesBook(session, organization, DateUtils.getYestersDayDate(date));
		Integer yesterdaySoldQty = (Integer) session.createCriteria(VbSalesBookProducts.class)
				.createAlias("vbSalesBook", "salesBook")
				.setProjection(Projections.sum("qtySold"))
				.add(Restrictions.eq("vbSalesBook", yesterdaySalesBook))
				.add(Restrictions.eq("salesBook.vbOrganization", organization))
				.uniqueResult();
		
		
		// For Today's SoldQty.
		VbSalesBook todaySalesBook = getVbSalesBook(session, organization, date);
		Integer todaySoldQty = (Integer) session.createCriteria(VbSalesBookProducts.class)
				.createAlias("vbSalesBook", "salesBook")
				.setProjection(Projections.sum("qtySold"))
				.add(Restrictions.eq("vbSalesBook", todaySalesBook))
				.add(Restrictions.eq("salesBook.vbOrganization", organization))
				.uniqueResult();
		
		session.close();
		if(yesterdaySoldQty != null && todaySoldQty != null) {
			TrendingAlertResult productTrendingResult = new TrendingAlertResult();
			productTrendingResult.setYesterdaySoldQty(yesterdaySoldQty);
			productTrendingResult.setTodaySoldQty(todaySoldQty);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("TrendingAlertResult: {}", productTrendingResult);
			}
			return productTrendingResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		
	}
	
	/**
	 * This method is responsible to get today's and yesterday's closing balance
	 * based on SalesExecutive and {@link VbOrganization}.
	 * 
	 * @param organization - {@link VbOrganization}
	 * @return amountTrendingResult - {@link TrendingAlertResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	public TrendingAlertResult getAmountTrendingAlertResult(VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Date date = new Date();
		
		// For Yesterday's Amount.
		Float yesterdaysAmount = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("closingBalance"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdOn", DateUtils.getYestersDayDate(date)))
				.uniqueResult();
		
		// For Today's Amount.
		Float todaysAmount = (Float) session.createCriteria(VbSalesBook.class)
				.setProjection(Projections.property("closingBalance"))
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdOn", date))
				.uniqueResult();
		session.close();
		
		if(yesterdaysAmount != null && todaysAmount != null) {
			TrendingAlertResult amountTrendingResult = new TrendingAlertResult();
			amountTrendingResult.setYesterdaysAmount(yesterdaysAmount);
			amountTrendingResult.setTodaysAmount(todaysAmount);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("TrendingAlertResult: {}", amountTrendingResult);
			}
			return amountTrendingResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to get {@link VbSalesBook} based on {@link Date}.
	 * 
	 * @param session - {@link Session}
	 * @param organization - {@link VbOrganization}
	 * @param createdOn - {@link Date}
	 * @return vbSalesBook - {@link VbSalesBook}
	 * 
	 */
	private VbSalesBook getVbSalesBook(Session session, VbOrganization organization, Date createdOn) {
		VbSalesBook vbSalesBook = (VbSalesBook) session.createCriteria(VbSalesBook.class)
				.add(Restrictions.eq("vbOrganization", organization))
				.add(Restrictions.eq("createdOn", createdOn))
				.uniqueResult();
		
		return vbSalesBook;
	}
	/**
	 * This method is responsible to get the system alerts data when clicked on the edit icon.
	 * 
	 * @param id - {@link Integer}
	 * @return alertsResult - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public AlertsResult getSystemAlertsData(Integer id) throws DataAccessException {
		Session session =this.getSession();
		VbSystemAlerts vbSystemAlerts = (VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		if(vbSystemAlerts != null) {
			AlertsResult alertsResult = new AlertsResult();
			alertsResult.setAlertName(vbSystemAlerts.getAlertName());
			alertsResult.setDescription(vbSystemAlerts.getDescription());
			alertsResult.setAlertType(vbSystemAlerts.getVbAlertType().getAlertType());
			List<VbSystemAlertsNotifications> systemAlertsNotificationsList = session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();

			StringBuffer userNames = null;
			StringBuffer notifications = null;
			StringBuffer roles = null;
			String userName = null;
			String notification = null;
			String role = null;
			for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotificationsList) {
				userName = vbSystemAlertsNotifications.getVbEmployee().getUsername();
				notification = vbSystemAlertsNotifications.getVbNotifications().getNotificationType();
				role = vbSystemAlertsNotifications.getVbRole().getDescription();
				if (userNames == null) {
					userNames = new StringBuffer(userName);
				} else {
					List<String> userNameList = Arrays.asList(userNames.toString().split(","));
					if(!userNameList.contains(userName)) {
						userNames = userNames.append(",").append(userName);
					}
				}
				if(notifications == null){
					notifications = new StringBuffer(notification);
				} else {
					List<String> notificationsList = Arrays.asList(notifications.toString().split(","));
					if(!notificationsList.contains(notification)) {
						notifications = notifications.append(",").append(notification);
					}
				}
				if(roles == null){
					roles = new StringBuffer(role); 
				} else {
					List<String> roleList = Arrays.asList(roles.toString().split(","));
					if(!roleList.contains(role)) {
						roles = roles.append(",").append(role);
					}
				}
			}
			alertsResult.setUserName(userNames.toString());
			alertsResult.setNotificationType(notifications.toString());
			alertsResult.setRole(roles.toString());
			
			session.close();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbSystemAlerts: {}", vbSystemAlerts);
			}
			return alertsResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to get the system alerts data when clicked on the edit icon.
	 * 
	 * @param id - {@link Integer}
	 * @return alertsResult - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public AlertsResult getUserAlertsData(Integer id) throws DataAccessException{
		Session session =this.getSession();
		VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class,id);
		
		if(vbUserDefinedAlerts != null) {
			AlertsResult alertsResult = new AlertsResult();
			alertsResult.setAlertName(vbUserDefinedAlerts.getAlertName());
			alertsResult.setDescription(vbUserDefinedAlerts.getDescription());
			alertsResult.setAlertType(vbUserDefinedAlerts.getVbAlertType().getAlertType());
			
			List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotificationsList =(List<VbUserDefinedAlertsNotifications>) session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
					.list();
			StringBuffer userNames = null;
			StringBuffer notifications = null;
			StringBuffer roles = null;
			String userName = null;
			String notification = null;
			String role = null;
			
			for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userDefinedAlertsNotificationsList) {
				userName = vbUserDefinedAlertsNotifications.getVbEmployee().getUsername();
				notification = vbUserDefinedAlertsNotifications.getVbNotifications().getNotificationType();
				role = vbUserDefinedAlertsNotifications.getVbRole().getDescription();
				if (userNames == null) {
					userNames = new StringBuffer(userName);
				} else {
					List<String> userNameList = Arrays.asList(userNames.toString().split(","));
					if(!userNameList.contains(userName)) {
						userNames = userNames.append(",").append(userName);
					}
				}
				if(notifications == null){
					notifications = new StringBuffer(notification);
				}else{
					List<String> notificationsList = Arrays.asList(notifications.toString().split(","));
					if(!notificationsList.contains(notification)) {
						notifications = notifications.append(",").append(notification);
					}
				}
				if(roles == null){
					roles = new StringBuffer(role); 
				}else{
					List<String> roleList = Arrays.asList(roles.toString().split(","));
					if(!roleList.contains(role)) {
						roles = roles.append(",").append(role);
					}
				}
			}
			alertsResult.setUserName(userNames.toString());
			alertsResult.setNotificationType(notifications.toString());
			alertsResult.setRole(roles.toString());
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("VbUserDefinedAlerts: {}", vbUserDefinedAlerts);
			}
			session.close();
			return alertsResult;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		
	}

	/**
	 * This method is responsible to delete systemalertsdata.
	 * 
	 * @param id - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException} 
	 */
	@SuppressWarnings("unchecked")
	public void deleteSystemAlerts(Integer id) throws DataAccessException {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		List<VbSystemAlertsNotifications> systemAlertsNotificationsList = null;
		VbSystemAlerts vbSystemAlerts = (VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		if (vbSystemAlerts != null) {
			systemAlertsNotificationsList = (List<VbSystemAlertsNotifications>) session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();
			for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotificationsList) {
				if (vbSystemAlertsNotifications != null) {
					session.delete(vbSystemAlertsNotifications);
				}
			}

			session.delete(vbSystemAlerts);
			transaction.commit();
			session.close();
		} else {
			transaction.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		_logger.info("{} {}", vbSystemAlerts.getAlertName(), Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
	}
	
	/**
	 * This method is responsible for removing userdefined alerts data.
	 * 
	 * @param id - {@link Integer}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void deleteUserAlerts(Integer id) throws DataAccessException {
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotifications = null;
		VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		if (vbUserDefinedAlerts != null) {
			userDefinedAlertsNotifications = (List<VbUserDefinedAlertsNotifications>) session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
					.list();
			for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userDefinedAlertsNotifications) {
				if (vbUserDefinedAlertsNotifications != null) {
					session.delete(vbUserDefinedAlertsNotifications);
				}
			}
			session.delete(vbUserDefinedAlerts);
			transaction.commit();
			session.close();
		} else {
			transaction.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.DELETE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		_logger.info("{} {}", vbUserDefinedAlerts.getAlertName(), Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
	}

	/**
	 * This method is responsible for enabling or disabling the alert.
	 * 
	 * @param id - {@link Integer}
	 * @param status - {@link Boolean}
	 * @param category - {@link String}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	public void enableOrDisableAlert(Integer id, Boolean status, String category) throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		if(OrganizationUtils.SYSTEM_ALERT.equalsIgnoreCase(category)) {
			VbSystemAlerts vbSystemAlerts = (VbSystemAlerts) session.get(VbSystemAlerts.class, id);
			if(vbSystemAlerts != null) {
				vbSystemAlerts.setActiveInactive(status);
				
				if (_logger.isInfoEnabled()) {
					_logger.info("{} is {} ", vbSystemAlerts.getAlertName(), Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				}
				session.update(vbSystemAlerts);
				txn.commit();
				session.close();
			} else {
				txn.rollback();
				session.close();
				String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				
				throw new DataAccessException(errorMsg);
			}
		} else if(OrganizationUtils.USER_DEFINED_ALERT.equalsIgnoreCase(category)){
			VbUserDefinedAlerts vbUserDefinedAlerts = (VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
			if(vbUserDefinedAlerts != null) {
				vbUserDefinedAlerts.setActiveInactive(status);
				
				if (_logger.isInfoEnabled()) {
					_logger.info("{} is {} ", vbUserDefinedAlerts.getAlertName(), Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				}
				session.update(vbUserDefinedAlerts);
				txn.commit();
				session.close();
			} else {
				String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
				txn.rollback();
				session.close();
				
				throw new DataAccessException(errorMsg);
			}
		}
	}

	/**
	 * This method is responsible for get all configured active {@link VbOrganization}.
	 * 
	 * @return activeOrganizationList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<VbOrganization> getAllActiveOrganizations() throws DataAccessException {
		Session session = this.getSession();
		List<VbOrganization> organizationsList = session.createCriteria(VbOrganization.class)
				.add(Restrictions.ne("description", "Organization Group"))
				.list();
		List<VbOrganization> activeOrganizationList = new ArrayList<VbOrganization>();
		VbLogin login = null;
		for (VbOrganization vbOrganization : organizationsList) {
			login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("vbOrganization", vbOrganization))
					.add(Restrictions.eq("username", vbOrganization.getSuperUserName()))
					.uniqueResult();
			if(OrganizationUtils.LOGIN_ENABLED == login.getEnabled()) {
				activeOrganizationList.add(vbOrganization);
			}
		}
		session.close();
		if(!organizationsList.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} organizations are in active state.", activeOrganizationList.size());
			}
			return activeOrganizationList;
		} else {
			throw new DataAccessException(Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE));
		}
	}

	/**
	 *This method is to retrieve system alerts data based on the given id
	 * 
	 * @param id {@link Integer}
	 * @return alertsResults - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getSystemAlertsDataByid(Integer id) throws DataAccessException {
		Session session =this.getSession();
		VbSystemAlerts vbSystemAlerts =(VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		if(vbSystemAlerts!=null){
			List<VbSystemAlertsNotifications> systemAlertsNotificationsList = (List<VbSystemAlertsNotifications>) session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();
			AlertsResult alertsResult = null;
			ArrayList<AlertsResult> alertsResults =new ArrayList<AlertsResult>();
			for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotificationsList) {
				alertsResult = new AlertsResult();
				alertsResult.setAlertName(vbSystemAlerts.getAlertName());
				alertsResult.setAlertType(vbSystemAlerts.getVbAlertType().getAlertType());
				alertsResult.setDescription(vbSystemAlerts.getDescription());
				alertsResult.setNotificationType(vbSystemAlertsNotifications.getVbNotifications().getNotificationType());
				alertsResult.setRole(vbSystemAlertsNotifications.getVbRole().getDescription());
				alertsResult.setUserName(vbSystemAlertsNotifications.getVbEmployee().getUsername());
				alertsResult.setMailsBcc(vbSystemAlertsNotifications.getMailsBcc());
				alertsResult.setMailsCc(vbSystemAlertsNotifications.getMailsCc());
				alertsResult.setMailsTo(vbSystemAlertsNotifications.getMailsTo());
				alertsResults.add(alertsResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("{}", alertsResults);
			}
			return alertsResults;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get the alerts history.
	 * 
	 * @param alertCategory - {@link String}
	 * @param pageNumber - {@link Integer}
	 * @param alertType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return alertsHistoryResultsList - {@link AlertsHistoryResult}
	 * @throws ParseException  - {@link ParseException}
	 * @throws DataAccessException  - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsHistoryResult> getAlertsHistory(Integer pageNumber,String alertCategory,
			String alertType, String alertName, VbOrganization organization,
			String strDate, String eDate) throws ParseException,
			DataAccessException {
		Session session = this.getSession();
		List<AlertsHistoryResult> alertsHistoryResultsList = new ArrayList<AlertsHistoryResult>();
		AlertsHistoryResult alertsHistoryResult = null;
		List<VbSystemAlertsHistory> systemAlertsHistory = null;
		List<VbUserDefinedAlertsHistory> userDefinedAlertsHistory = null;
		Integer count = new Integer(1);
		Date startDate = null;
		Date endDate = null;
		if(!strDate.isEmpty()) {
			startDate = DateUtils.getStartTimeStamp(DateUtils.parse(strDate));
		}
		if(!eDate.isEmpty()) {
			endDate = DateUtils.getEndTimeStamp(DateUtils.parse(eDate));
		}
		Boolean addAlertCategory = Boolean.FALSE;
		Boolean addAlertType = Boolean.FALSE;
		Boolean addAlertName = Boolean.FALSE;
		Boolean addDate = Boolean.FALSE;
		Boolean addStartDate = Boolean.FALSE;
		Boolean addEndDate  = Boolean.FALSE;
		StringBuffer queryString = new StringBuffer("FROM VbSystemAlertsHistory vb WHERE vb.vbSystemAlerts.vbOrganization = :organization");
		StringBuffer queryString1 = new StringBuffer("FROM VbUserDefinedAlertsHistory vb WHERE vb.vbUserDefinedAlerts.vbOrganization = :organization");
		if(alertCategory != null && !(alertCategory.equals("-1"))) {
			queryString.append(" AND vb.vbSystemAlerts.vbAlertType.vbAlertCategory.alertCategory = :alertCategory");
			queryString1.append(" AND vb.vbUserDefinedAlerts.vbAlertType.vbAlertCategory.alertCategory = :alertCategory");
			addAlertCategory = Boolean.TRUE;
		}
		if(alertType != null && !(alertType.equals("-1"))) {
			queryString.append(" AND vb.vbSystemAlerts.vbAlertType.alertType = :alertType");
			queryString1.append(" AND vb.vbUserDefinedAlerts.vbAlertType.alertType = :alertType");
			addAlertType = Boolean.TRUE;
		}
		if(alertName != null && !alertName.isEmpty()) {
			queryString.append(" AND vb.vbSystemAlerts.alertName = :alertName");
			queryString1.append(" AND vb.vbUserDefinedAlerts.alertName = :alertName");
			addAlertName = Boolean.TRUE;
		}
		if(startDate != null && endDate != null) {
			queryString.append(" AND vb.createdOn BETWEEN :startDate AND :endDate");
			queryString1.append(" AND vb.createdOn BETWEEN :startDate AND :endDate");
			addDate = Boolean.TRUE;
		}
		if(startDate != null && endDate == null) {
			queryString.append(" AND vb.createdOn = :startDate");
			queryString1.append(" AND vb.createdOn = :startDate");
			addStartDate = Boolean.TRUE;
		}
		if(startDate == null && endDate != null) {
			queryString.append(" AND vb.createdOn = :endDate");
			queryString1.append(" AND vb.createdOn = :endDate");
			addEndDate = Boolean.TRUE;
		}
		Query query = session.createQuery(queryString.toString());
		Query query1 = session.createQuery(queryString1.toString());
		query.setParameter("organization", organization);
		query1.setParameter("organization", organization);
		if(addAlertCategory) {
			query.setParameter("alertCategory", alertCategory);
			query1.setParameter("alertCategory", alertCategory);
		}
		if(addAlertType) {
			query.setParameter("alertType", alertType);
			query1.setParameter("alertType", alertType);
		}
		if(addAlertName) {
			query.setParameter("alertName", alertName);
			query1.setParameter("alertName", alertName);
		}
		if(addDate) {
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query1.setParameter("startDate", startDate);
			query1.setParameter("endDate", endDate);
		}
		if(addStartDate){
			query.setParameter("startDate", startDate);
			query1.setParameter("startDate", startDate);
		}
		if(addEndDate) {
			query.setParameter("endDate", endDate);
			query1.setParameter("endDate", endDate);
		}
		query.setFirstResult(alertHistoryPageSize*(pageNumber-1));
		query.setMaxResults(alertHistoryPageSize);		
		systemAlertsHistory = query.list();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Configured system alerts list size is: {}", systemAlertsHistory.size());
		}
		query1.setFirstResult(alertHistoryPageSize*(pageNumber-1));
		query1.setMaxResults(alertHistoryPageSize);
		userDefinedAlertsHistory = query1.list();
		if(_logger.isDebugEnabled()) {
			_logger.debug("Configured user defined alerts list size is: {}", userDefinedAlertsHistory.size());
		}
		//For System Defined Alerts.
		String description = null;
		for (VbSystemAlertsHistory vbSystemAlertsHistory : systemAlertsHistory) {
			alertsHistoryResult = new AlertsHistoryResult();
			alertsHistoryResult.setAlertName(vbSystemAlertsHistory.getVbSystemAlerts().getAlertName());
			description = vbSystemAlertsHistory.getVbSystemAlerts().getDescription();
			if(description == null) {
				description = " ";
			}
			alertsHistoryResult.setDescription(description);
			alertsHistoryResult.setCreatedUser(vbSystemAlertsHistory.getCreatedBy());
			alertsHistoryResult.setId(count++);
			alertsHistoryResult.setCreatedOn(DateUtils.formatDateWithTimestamp(vbSystemAlertsHistory.getCreatedOn()));
			alertsHistoryResultsList.add(alertsHistoryResult);
		}
		//For User Defined Alerts.
		for (VbUserDefinedAlertsHistory vbUserDefinedAlertsHistory : userDefinedAlertsHistory) {
			alertsHistoryResult = new AlertsHistoryResult();
			//String alertCriteria = null;
			//String alertTypeVal = vbUserDefinedAlertsHistory.getVbUserDefinedAlerts().getVbAlertType().getAlertType();
			VbUserDefinedAlerts vbUserDefinedAlerts = vbUserDefinedAlertsHistory.getVbUserDefinedAlerts();
			alertsHistoryResult.setId(count++);
			alertsHistoryResult.setCreatedUser(vbUserDefinedAlertsHistory.getCreatedBy());
			alertsHistoryResult.setAlertName(vbUserDefinedAlerts.getAlertName());
			alertsHistoryResult.setCreatedOn(DateUtils.formatDateWithTimestamp(vbUserDefinedAlertsHistory.getCreatedOn()));
			String userDefinedDescription = null;
			if(vbUserDefinedAlerts.getDescription() == null){
				userDefinedDescription=" ";
			}else{
				userDefinedDescription=vbUserDefinedAlerts.getDescription();
			}
			alertsHistoryResult.setDescription(userDefinedDescription);
			//Fetching alert criteria based on alert type.
			/*if(alertTypeVal.equals("Trending")) {
				VbTrending vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.uniqueResult();
				Long amountPersentage = vbTrending.getAmountPersentage();
				Long productPersentage = vbTrending.getProductPercentage();
				alertCriteria = "amount% "+ amountPersentage + ", product% " + productPersentage;
			} else if(alertTypeVal.equals("Excess Amount")) {
				Float excessAmount = (Float) session.createCriteria(VbExcessCash.class)
						.setProjection(Projections.property("amount"))
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.uniqueResult();
				alertCriteria = "Excess Amount :" +excessAmount;
			} else {
				List<VbMySales> vbMySales = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
						.list();
				for (VbMySales mySales : vbMySales) {
					alertCriteria = "Mysales:" +mySales.getVbAlertTypeMySales().getAlertMySales();
				}
			}
			alertsHistoryResult.setAlertCriteria(alertCriteria);*/
			alertsHistoryResultsList.add(alertsHistoryResult);
		}
		
		session.close();
		if(alertsHistoryResultsList.isEmpty()) {
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		} else {
			if(_logger.isDebugEnabled()) {
				_logger.debug("Alerts History List Size Is : {}", alertsHistoryResultsList.size());
			}
			//return alertsHistoryResultsList.subList(0, 50);
			return alertsHistoryResultsList;
		}
		
	}

	/**
	 * This Method is Responsible for updating Systemdefined Alerts 
	 *  
	 * @param id - {@link Integer}
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param notificationType - {@link String}
	 * @param group - {@link String}
	 * @param userNames - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateSystemDefinedAlert(Integer id, AlertsCommand alertsCommand,
			String notificationType, String group, String userNames,
			VbOrganization organization, String createdBy) throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType vbAlertType = getVbAlertType(session, alertsCommand.getAlertType(), alertCategory);
		VbSystemAlerts vbSystemAlerts = (VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		// Updating VbSystemAlerts.
		if(vbSystemAlerts != null) {
			vbSystemAlerts.setAlertName(alertsCommand.getAlertName());
			vbSystemAlerts.setDescription(alertsCommand.getDescription());
			vbSystemAlerts.setVbAlertType(vbAlertType);
			
			session.update(vbSystemAlerts);
			
			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			List<VbEmployee> vbEmployeeList = null;
			VbRole vbRole = null;
			String empType = null;
			String[] userNamesArray = userNames.split(",");
			List<String> userNamesList = Arrays.asList(userNamesArray);
			String [] groupArray = group.split(",");
			List<String> groupsList = Arrays.asList(groupArray);
			String[] groupReceipient = null;
			VbSystemAlertsNotifications vbSystemAlertsNotifications = null;
			List<VbSystemAlertsNotifications> vbSystemAlertsNotificationsList = session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts", vbSystemAlerts))
					.list();
			if(!vbSystemAlertsNotificationsList.isEmpty()) {
				for (VbSystemAlertsNotifications systemAlertsNotifications : vbSystemAlertsNotificationsList) {
					session.delete(systemAlertsNotifications);
				}
			}
			//Iterating for every notification type
			for (String notification : notificationList) {
				if (!group.isEmpty()) {
					//	String [] groupNameRecipient = null;
					for(String groupValue: groupsList){
						groupReceipient = groupValue.split("/");
				
				 vbRole = (VbRole) session.createCriteria(VbRole.class)
						.add(Restrictions.eq("description", groupReceipient[0])).uniqueResult();
				
				 empType = OrganizationUtils.getEmployeeTypeByRole(groupReceipient[0]);
				
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				//For Specified Groups.
					//no selected users(for all users).
					if (userNames.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							vbSystemAlertsNotifications = new VbSystemAlertsNotifications();
							vbSystemAlertsNotifications.setVbEmployee(vbEmployee);
							vbSystemAlertsNotifications.setVbNotifications(vbNotifications);
							vbSystemAlertsNotifications.setVbRole(vbRole);
							vbSystemAlertsNotifications.setVbSystemAlerts(vbSystemAlerts);
		
							if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient[1])) {
									vbSystemAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient[1])) {
									vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsCc(Boolean.TRUE);
									vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient[1])) {
									vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemDefinedAlertsNotifications");
							}
							session.save(vbSystemAlertsNotifications);
						}
					}else { //for specific users.
						String[] userNameReceipient = null;
						for (String userName1 : userNamesList) {
							userNameReceipient = userName1.split("/");
		
							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username",userNameReceipient[0]))
									.add(Restrictions.eq("employeeType",empType))
									.add(Restrictions.eq("vbOrganization",organization))
									.uniqueResult();
							if(vbEmployee != null){
								vbSystemAlertsNotifications = new VbSystemAlertsNotifications();
								
								vbSystemAlertsNotifications.setVbEmployee(vbEmployee);
								vbSystemAlertsNotifications.setVbNotifications(vbNotifications);
								vbSystemAlertsNotifications.setVbRole(vbRole);
								vbSystemAlertsNotifications.setVbSystemAlerts(vbSystemAlerts);
								
								if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
										vbSystemAlertsNotifications.setMailsTo(Boolean.TRUE);
										vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
										vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
										vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbSystemAlertsNotifications.setMailsCc(Boolean.TRUE);
										vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
										vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
										vbSystemAlertsNotifications.setMailsBcc(Boolean.TRUE);
									} else {
										if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient[1])) {
											vbSystemAlertsNotifications.setMailsBcc(Boolean.TRUE);
											vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
											vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
										} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient[1])) {
											vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
											vbSystemAlertsNotifications.setMailsTo(Boolean.FALSE);
											vbSystemAlertsNotifications.setMailsCc(Boolean.TRUE);
										} else {
											vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
											vbSystemAlertsNotifications.setMailsTo(Boolean.TRUE);
											vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
										}
									}
								}
			
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting VbSystemAlertsNotifications");
								}
								session.save(vbSystemAlertsNotifications);
							}
						}
					}
				} 
			}
			else {
					List<String> configuredGroups = getConfiguredGroups(organization);
					for (String configuredGroup : configuredGroups) {
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description", configuredGroup))
								.uniqueResult();
						empType = OrganizationUtils.getEmployeeTypeByRole(configuredGroup);
						vbEmployeeList = session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("employeeType", empType))
								.add(Restrictions.eq("vbOrganization", organization)).list();
						for (VbEmployee vbEmployee : vbEmployeeList) {
							vbSystemAlertsNotifications = new VbSystemAlertsNotifications();
								vbSystemAlertsNotifications.setVbEmployee(vbEmployee);
								vbSystemAlertsNotifications.setVbNotifications(vbNotifications);
								vbSystemAlertsNotifications.setVbRole(vbRole);
								vbSystemAlertsNotifications.setVbSystemAlerts(vbSystemAlerts);

								if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
									vbSystemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									vbSystemAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbSystemAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting VbUserAlertsNotifications");
								}
								session.save(vbSystemAlertsNotifications);
						}
					}
				}
			}
			txn.commit();
			session.close();
		} else {
			txn.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
}

	/**
	 * This Method is to update systemdefined alert for multiple groups.
	 * 
	 * @param id - {@link Integer}
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param multiGroupMap - {@link HashMap}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param createdBy - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateSystemDefinedAlertForMultipleGroups(Integer id, AlertsCommand alertsCommand, HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization, String createdBy) throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbAlertCategory alertCategory = getAlertCategoryForSystemAlerts(session);
		VbAlertType alertType = getVbAlertType(session,
				alertsCommand.getAlertType(), alertCategory);
		VbSystemAlerts systemAlerts = null;
		systemAlerts = (VbSystemAlerts) session.get(VbSystemAlerts.class, id);
		// Updating VbSystemAlerts.
		if(systemAlerts != null){
			systemAlerts.setAlertName(alertsCommand.getAlertName());
			systemAlerts.setDescription(alertsCommand.getDescription());
			systemAlerts.setVbAlertType(alertType);

			if (_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbSystemAlerts");
			}
			session.update(systemAlerts);
			
			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			VbSystemAlertsNotifications systemAlertsNotifications = null;
			String group = null;
			String groupUsers = null;
			String[] groupUser = null;
			String[] groupNames = null;
			List<String> userGroup = null;
			String groupReceipient = null;
			String [] groupRecipients = null;
			String empType = null;
			List<VbEmployee> vbEmployeeList = null;
			//Deliting the notifications before updating.
			List<VbSystemAlertsNotifications>	systemAlertsNotificationsList = session.createCriteria(VbSystemAlertsNotifications.class)
					.add(Restrictions.eq("vbSystemAlerts",systemAlerts ))
					.list();
			if(!systemAlertsNotificationsList.isEmpty()) {
				for (VbSystemAlertsNotifications vbSystemAlertsNotifications : systemAlertsNotificationsList) {
					session.delete(vbSystemAlertsNotifications);
				}
			}
			//Saving the system alert notification for multiple groups. 
			for (String notification : notificationList) {
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
					group = entry.getKey();
					groupUsers = entry.getValue();
					groupNames = group.split(",");
					userGroup = Arrays.asList(groupNames);
					for(String groupName : userGroup){
						groupRecipients = groupName.split("/");
					
					VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupRecipients[0]))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupRecipients[0]);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();

					if (ENotification.Emails.equals(notificationType)) {
						groupReceipient = groupRecipients[1];
					}
					groupUser = groupUsers.split(",");
					
					List<String> groupUserList = Arrays.asList(groupUser);
					if (groupUsers.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							systemAlertsNotifications = new VbSystemAlertsNotifications();
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug(
										"Persisting VbSystemAlertsNotifications For Multiple Users");
							}
							session.save(systemAlertsNotifications);
							
					}
					} else {
						String[] userNameReceipient = null;
						for (String userName : groupUserList) {
							userNameReceipient = userName.split("/");

							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();
							systemAlertsNotifications = new VbSystemAlertsNotifications(); 
							systemAlertsNotifications.setVbEmployee(vbEmployee);
							systemAlertsNotifications.setVbNotifications(vbNotifications);
							systemAlertsNotifications.setVbRole(vbRole);
							systemAlertsNotifications.setVbSystemAlerts(systemAlerts);
							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.TRUE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC
										.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									systemAlertsNotifications.setMailsTo(Boolean.FALSE);
									systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
										systemAlertsNotifications.setMailsBcc(Boolean.TRUE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.FALSE);
										systemAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										systemAlertsNotifications.setMailsBcc(Boolean.FALSE);
										systemAlertsNotifications.setMailsTo(Boolean.TRUE);
										systemAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbSystemAlertsNotifications For Multiple Users");
							}
							session.save(systemAlertsNotifications);
					} 
				}
			}
		}
			}
			txn.commit();
			session.close();
		} else {
			txn.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
		
	}

	/**
	 * This Method is to retrieve {@link VbUserDefinedAlerts} data based on given id
	 * 
	 * @param id - {@link Integer}
	 * @return alertsResults - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */ 
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getUserAlertsDataById(Integer id) throws DataAccessException {
		Session session =this.getSession();
		ArrayList<AlertsResult> alertsResults =new ArrayList<AlertsResult>();
		List<VbUserDefinedAlertsNotifications> vbUserDefinedAlertsNotifications =null;
		VbUserDefinedAlerts vbUserDefinedAlerts =(VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		if(vbUserDefinedAlerts!=null){
			vbUserDefinedAlertsNotifications = (List<VbUserDefinedAlertsNotifications>) session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts", vbUserDefinedAlerts))
					.list();
			AlertsResult alertsResult = null;
			for (VbUserDefinedAlertsNotifications userDefinedAlertsNotifications : vbUserDefinedAlertsNotifications) {
				alertsResult = new AlertsResult();
				alertsResult.setAlertName(vbUserDefinedAlerts.getAlertName());
				alertsResult.setAlertType(vbUserDefinedAlerts.getVbAlertType().getAlertType());
				alertsResult.setDescription(vbUserDefinedAlerts.getDescription());
				alertsResult.setNotificationType(userDefinedAlertsNotifications.getVbNotifications().getNotificationType());
				alertsResult.setRole(userDefinedAlertsNotifications.getVbRole().getDescription());
				alertsResult.setUserName(userDefinedAlertsNotifications.getVbEmployee().getUsername());
				alertsResult.setMailsBcc(userDefinedAlertsNotifications.getMailsBcc());
				alertsResult.setMailsCc(userDefinedAlertsNotifications.getMailsCc());
				alertsResult.setMailsTo(userDefinedAlertsNotifications.getMailsTo());
				alertsResults.add(alertsResult);
			}
			session.close();
			
			if (_logger.isDebugEnabled()) {
				_logger.debug(": {}", alertsResults);
			}
			return alertsResults;
			
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}
	
	/**
	 * This method is responsible to persist {@link VbInsystemAlertNotifications}.
	 * 
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param alertType - {@link String}
	 * @param description - {@link String}
	 * @param toRecipient - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */		
	public void saveInSystemAlertNotification(String userName,
			VbOrganization organization, String alertType, String description,
			String toRecipient) throws DataAccessException {
		Session session = null;
		Transaction txn = null;
		try {
			session = this.getSession();
			VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
					.add(Restrictions.eq("username", toRecipient))
					.add(Restrictions.eq("vbOrganization", organization))
					.uniqueResult();
			txn = session.beginTransaction();
			VbInsystemAlertNotifications vbInsystemAlertNotification = new VbInsystemAlertNotifications();
			vbInsystemAlertNotification.setAlertType(alertType);
			vbInsystemAlertNotification.setCreatedBy(userName);
			vbInsystemAlertNotification.setCreatedOn(new Date());
			vbInsystemAlertNotification.setModifiedOn(new Date());
			vbInsystemAlertNotification.setDescription(description);
			vbInsystemAlertNotification.setFlag(new Integer(1));
			vbInsystemAlertNotification.setVbLogin(login);
			vbInsystemAlertNotification.setVbOrganization(organization);
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("Persisting VbInsystemAlertNotifications: {}", vbInsystemAlertNotification);
			}
			session.save(vbInsystemAlertNotification);
			txn.commit();
		} catch (HibernateException exception) {
			if(txn != null) {
				txn.rollback();
			}
			String message = Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE);
			
			throw new DataAccessException(message);
		} finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * This method is responsible to get all active notifications from {@link VbInsystemAlertNotifications}.
	 *  
	 * @param pageNumber - {@link Integer}
	 * @param userName - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @return resultList - {@link List}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<MyAlertResult> getMyAlerts(Integer pageNumber,String userName, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		Date date = new Date();
		VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
				.add(Restrictions.eq("username", userName))
				.add(Restrictions.eq("vbOrganization", organization))
				.uniqueResult();
		
		Criteria criteria = session.createCriteria(VbInsystemAlertNotifications.class);
		criteria.add(Restrictions.eq("vbLogin", login));
		criteria.add(Restrictions.eq("vbOrganization", organization));
		criteria.add(Restrictions.between("createdOn", DateUtils.getBeforeTwoDays(date), date));
		criteria.setFirstResult(myAlertsPageSize*(pageNumber-1));
		criteria.setMaxResults(myAlertsPageSize);		
		criteria.addOrder(Order.desc("createdOn"));
		List<VbInsystemAlertNotifications> notificationList =criteria.list();
		
		if(!notificationList.isEmpty()) {
			List<MyAlertResult> resultList = new ArrayList<MyAlertResult>();
			MyAlertResult result = null;
			for (VbInsystemAlertNotifications notifications : notificationList) {
				// Populating result object.
				result = new MyAlertResult();
				result.setAlertName(notifications.getAlertType());
				result.setCreatedBy(notifications.getCreatedBy());
				result.setCreatedOn(DateUtils.formatDateWithTimestamp(notifications.getCreatedOn()));
				result.setDescription(notifications.getDescription());
				resultList.add(result);
			}
			session.close();
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("{} records have been found");
			}
			return resultList;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is responsible to get the user defined alert data based on the id.
	 * 
	 * @param id - {@link Integer}
	 * @return alertsResults - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getUserDefinedAlertsData(Integer id) throws DataAccessException {
		Session session = this.getSession();
		AlertsResult alertsResult = null;
		AlertsResult alertsResult2 = null;
		ArrayList<AlertsResult> alertsResults =new ArrayList<AlertsResult>();
		VbUserDefinedAlerts userDefinedAlerts = null;
		VbNotifications notifications = null;
		String notificationType = null;
		String alertType  = null;
		List<VbUserDefinedAlertsNotifications> userDefinedAlertsData =(List<VbUserDefinedAlertsNotifications>) session.createCriteria(VbUserDefinedAlertsNotifications.class)
				.createAlias("vbUserDefinedAlerts", "userAlerts")
				.add(Restrictions.eq("userAlerts.id",id))
				.list();
		for (VbUserDefinedAlertsNotifications userDefinedAlertsNotifications : userDefinedAlertsData) {
			alertsResult = new AlertsResult();
			alertType = userDefinedAlertsNotifications.getVbUserDefinedAlerts().getVbAlertType().getAlertType();
			userDefinedAlerts =  userDefinedAlertsNotifications.getVbUserDefinedAlerts();
			notifications = userDefinedAlertsNotifications.getVbNotifications();
			notificationType = notifications.getNotificationType();
			alertsResult.setAlertName(userDefinedAlerts.getAlertName());
			alertsResult.setDescription(userDefinedAlerts.getDescription());
			alertsResult.setAlertType(userDefinedAlerts.getVbAlertType().getAlertType());
			alertsResult.setNotificationType(notificationType);
			if(notificationType.equals("Emails")){
				alertsResult.setMailsBcc(userDefinedAlertsNotifications.getMailsBcc());
				alertsResult.setMailsCc(userDefinedAlertsNotifications.getMailsCc());
				alertsResult.setMailsTo(userDefinedAlertsNotifications.getMailsTo());
			}
			alertsResult.setRole(userDefinedAlertsNotifications.getVbRole().getDescription());
			alertsResult.setUserName(userDefinedAlertsNotifications.getVbEmployee().getUsername());
			if(alertType.equals("Trending")){
				VbTrending trending = (VbTrending) session.createCriteria(VbTrending.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts))
						.uniqueResult();
				if(trending != null){
					alertsResult.setProductPercentage(trending.getProductPercentage());
					alertsResult.setAmountPersentage(trending.getAmountPersentage());
				}
				alertsResults.add(alertsResult);
			} else if(alertType.equals("Excess Amount")) {
				VbExcessCash excessCash = (VbExcessCash) session.createCriteria(VbExcessCash.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts))
						.uniqueResult();
				if(excessCash != null){
					alertsResult.setAmount(excessCash.getAmount());
				}
				alertsResults.add(alertsResult);
			} else if(alertType.equals("My Sales")) {
				List<VbMySales> vbMySales = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts))
						.list();
				for (VbMySales mySales : vbMySales) {
					alertsResult2 = new AlertsResult();
					alertsResult2.setAlertName(userDefinedAlerts.getAlertName());
					alertsResult2.setDescription(userDefinedAlerts.getDescription());
					alertsResult2.setAlertType(userDefinedAlerts.getVbAlertType().getAlertType());
					alertsResult2.setNotificationType(notifications.getNotificationType());
					alertsResult2.setSalesType(mySales.getVbAlertTypeMySales().getAlertMySales());
					alertsResult2.setSalesPage(mySales.getVbAlertTypeMySalesPage().getAlertMySalesPage());
					alertsResult2.setRole(userDefinedAlertsNotifications.getVbRole().getDescription());
					alertsResult2.setUserName(userDefinedAlertsNotifications.getVbEmployee().getUsername());
					alertsResult2.setMailsBcc(userDefinedAlertsNotifications.getMailsBcc());
					alertsResult2.setMailsCc(userDefinedAlertsNotifications.getMailsCc());
					alertsResult2.setMailsTo(userDefinedAlertsNotifications.getMailsTo());
					alertsResults.add(alertsResult2);
				}
			}
		}
		session.close();
		
		if(!userDefinedAlertsData.isEmpty()) {
			
			if(_logger.isDebugEnabled()) {
				_logger.debug("List of userdefined alerts associated with id {} is {}:", id, alertsResults.size());
			}
			return alertsResults;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This Method is responsible for updating user defined alerts for particular record
	 * 
	 * @param id - {@link Integer}
	 * @param notificationType - {@link String}
	 * @param group - {@link String}
	 * @param userNames - {@link String}
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param userDefinedNotifications - VbUserDefinedAlertsNotifications
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param salesTypes - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateUserDefinedAlerts(Integer id, String notificationType,String group, String userNames,
			AlertsCommand alertsCommand,VbUserDefinedAlertsNotifications uerDefinedNotifications,
			VbOrganization organization, String userName, String salesTypes) throws DataAccessException {
		
		Session session = this.getSession();
		Transaction transaction = session.beginTransaction();
		VbUserDefinedAlerts userDefinedAlerts = (VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		VbTrending vbTrending = null;
		VbExcessCash vbExcessCash = null;
		//Updating VbUserDefinedAlerts.
		if (userDefinedAlerts != null) {
			userDefinedAlerts.setAlertName(alertsCommand.getAlertName());
			userDefinedAlerts.setDescription(alertsCommand.getDescription());
			String alertType = alertsCommand.getAlertType();
			String previousAlertType = userDefinedAlerts.getVbAlertType().getAlertType();
			VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
			VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
			userDefinedAlerts.setVbAlertType(vbAlertType);
			session.update(userDefinedAlerts);
			if (OrganizationUtils.ALERT_TYPE_TRENDING.equals(previousAlertType)) {
				if(previousAlertType .equals(alertType)){
					 vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.uniqueResult();
					if (vbTrending != null) {
						vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
						vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
						vbTrending.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.update(vbTrending);
				}
				}else{
					 vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
								.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
								.uniqueResult();
					session.delete(vbTrending);
				}
			}else if(OrganizationUtils.ALERT_TYPE_TRENDING.equals(alertType)){
				vbTrending = new VbTrending();
				vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
				vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
				vbTrending.setVbUserDefinedAlerts(userDefinedAlerts);
				session.save(vbTrending);
			}
			
				
			if (OrganizationUtils.ALERT_TYPE_EXCESS_AMOUNT.equals(previousAlertType)) {
				if(previousAlertType .equals(alertType)){
					 vbExcessCash = (VbExcessCash) session.createCriteria(VbExcessCash.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts)).uniqueResult();
					if (vbExcessCash != null) {
						vbExcessCash.setAmount(alertsCommand.getAmount());
						vbExcessCash.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.update(vbExcessCash);
					}
				}else{
					vbExcessCash = (VbExcessCash) session.createCriteria(VbExcessCash.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts)).uniqueResult();
					session.delete(vbExcessCash);
				}
			}else if(OrganizationUtils.ALERT_TYPE_EXCESS_AMOUNT.equals(alertType)){
					vbExcessCash = new VbExcessCash();
					vbExcessCash.setAmount(alertsCommand.getAmount());
					vbExcessCash.setVbUserDefinedAlerts(userDefinedAlerts);
					session.save(vbExcessCash);
			}
			if(OrganizationUtils.ALERT_TYPE_MY_SALES.equals(previousAlertType)){
				if(previousAlertType.equals(alertType)){
					String[] salesTypesListArray = salesTypes.split(",");
					List<String> salesTypeList = Arrays.asList(salesTypesListArray);
					VbMySales vbMySales = null;
					List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.list();
					for (VbMySales mySales : vbMySalesList) {
						session.delete(mySales);
					}
					if (salesTypes != null) {
						VbAlertTypeMySales vbAlertTypeMySales = null;
						for (String salesType : salesTypeList) {
							VbAlertTypeMySalesPage vbAlertTypeMySalesPage = new VbAlertTypeMySalesPage();
							String salesTypesvlaue = alertsCommand.getAlertMySales();
							vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
									.createAlias("vbAlertType", "vbAlertType")
									.add(Restrictions.eq("vbAlertType.id", vbAlertType.getId()))
									.add(Restrictions.eq("alertMySales", salesTypesvlaue))
									.uniqueResult();
							vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
									.add(Restrictions.eq("alertMySalesPage", salesType))
									.createAlias("vbAlertTypeMySales", "vbAlertTypeMySales")
									.add(Restrictions.eq("vbAlertTypeMySales.id", vbAlertTypeMySales.getId()))
									.uniqueResult();
							vbMySales = (VbMySales) session.createCriteria(VbMySales.class)
									.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
									.add(Restrictions.eq("vbAlertTypeMySalesPage", vbAlertTypeMySalesPage))
									.uniqueResult();
							vbMySales = new VbMySales();
							vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
							vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
							vbMySales.setVbUserDefinedAlerts(userDefinedAlerts);
							
							session.save(vbMySales);
						}
					}
				
				}else{
					List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.list();
					for (VbMySales mySales : vbMySalesList) {
						session.delete(mySales);
					}
				}
			}else if (OrganizationUtils.ALERT_TYPE_MY_SALES.equals(alertType)) {
				String[] salesTypesListArray = salesTypes.split(",");
				List<String> salesTypeList = Arrays.asList(salesTypesListArray);
				VbMySales vbMySales = null;
				List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
						.list();
				for (VbMySales mySales : vbMySalesList) {
					session.delete(mySales);
				}
				if (salesTypes != null) {
					VbAlertTypeMySales vbAlertTypeMySales = null;
					for (String salesType : salesTypeList) {
						VbAlertTypeMySalesPage vbAlertTypeMySalesPage = new VbAlertTypeMySalesPage();
						String salesTypesvlaue = alertsCommand.getAlertMySales();
						vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
								.createAlias("vbAlertType", "vbAlertType")
								.add(Restrictions.eq("vbAlertType.id", vbAlertType.getId()))
								.add(Restrictions.eq("alertMySales", salesTypesvlaue))
								.uniqueResult();
						vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
								.add(Restrictions.eq("alertMySalesPage", salesType))
								.createAlias("vbAlertTypeMySales", "vbAlertTypeMySales")
								.add(Restrictions.eq("vbAlertTypeMySales.id", vbAlertTypeMySales.getId()))
								.uniqueResult();
						vbMySales = (VbMySales) session.createCriteria(VbMySales.class)
								.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
								.add(Restrictions.eq("vbAlertTypeMySalesPage", vbAlertTypeMySalesPage))
								.uniqueResult();
						vbMySales = new VbMySales();
						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
						vbMySales.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.save(vbMySales);
					}
				}
			}
			
			String[] notifications = notificationType.split(",");
			List<String> notificationList = Arrays.asList(notifications);
			VbNotifications vbNotifications = null;
			List<VbEmployee> vbEmployeeList = null;
			VbRole vbRole = null;
			String empType = null;
			String[] userNamesArray = userNames.split(",");
			List<String> userNamesList = Arrays.asList(userNamesArray);
			String [] groupArray = group.split(",");
			VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications = null;
			List<String> groupsList = Arrays.asList(groupArray);
			String[] groupReceipient = null;
			
			//deleting the user defined alert notifications. 
			List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotificationList = session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
					.list();
			if (!userDefinedAlertsNotificationList.isEmpty()) {
				for (VbUserDefinedAlertsNotifications vbUserAlertsNotifications : userDefinedAlertsNotificationList) {
					session.delete(vbUserAlertsNotifications);
				}
			}

			
			// Iterating for every notification type and saving into VbUserDefinedAlertsNotifications. 
			for (String notification : notificationList) {
				if (!group.isEmpty()) {
					//	String [] groupNameRecipient = null;
					for(String groupValue: groupsList){
						groupReceipient = groupValue.split("/");
				
				 vbRole = (VbRole) session.createCriteria(VbRole.class)
						.add(Restrictions.eq("description", groupReceipient[0])).uniqueResult();
				
				 empType = OrganizationUtils.getEmployeeTypeByRole(groupReceipient[0]);
				
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				//For Specified Groups.
					//no selected users(for all users).
					if (userNames.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							vbUserDefinedAlertsNotifications.setVbRole(vbRole);
							vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);
		
							if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient[1])) {
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Persisting VbUserDefinedAlertsNotifications");
							}
							session.save(vbUserDefinedAlertsNotifications);
						}
					}else { //for specific users.
						String[] userNameReceipient = null;
						for (String userName1 : userNamesList) {
							userNameReceipient = userName1.split("/");
		
							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username",userNameReceipient[0]))
									.add(Restrictions.eq("employeeType",empType))
									.add(Restrictions.eq("vbOrganization",organization))
									.uniqueResult();
							if(vbEmployee != null){
								vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
								
								vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
								vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
								vbUserDefinedAlertsNotifications.setVbRole(vbRole);
								vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);
								
								if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
										vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
										vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
									} else {
										if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient[1])) {
											vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
											vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
											vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
										} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient[1])) {
											vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
											vbUserDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
											vbUserDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
										} else {
											vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
											vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
											vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
										}
									}
								}
			
								if (_logger.isDebugEnabled()) {
									_logger.debug("Persisting VbUserDefinedAlertsNotifications");
								}
								session.save(vbUserDefinedAlertsNotifications);
							}
						}
					}
				} 
			}
			else {
					/*List<VbUserDefinedAlertsNotifications> userAlertsNotificationsList = session.createCriteria(VbUserDefinedAlertsNotifications.class)
							.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts))
							.list();
					if(!userAlertsNotificationsList.isEmpty()) {
						for (VbUserDefinedAlertsNotifications vbUserAlertsNotifications : userAlertsNotificationsList) {
							 session.delete(vbUserAlertsNotifications);
						}
					}*/
					List<String> configuredGroups = getConfiguredGroups(organization);
					for (String configuredGroup : configuredGroups) {
						vbRole = (VbRole) session.createCriteria(VbRole.class)
								.add(Restrictions.eq("description", configuredGroup))
								.uniqueResult();
						empType = OrganizationUtils.getEmployeeTypeByRole(configuredGroup);
						vbEmployeeList = session.createCriteria(VbEmployee.class)
								.add(Restrictions.eq("employeeType", empType))
								.add(Restrictions.eq("vbOrganization", organization)).list();
						for (VbEmployee vbEmployee : vbEmployeeList) {
								vbUserDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
								vbUserDefinedAlertsNotifications.setVbEmployee(vbEmployee);
								vbUserDefinedAlertsNotifications.setVbNotifications(vbNotifications);
								vbUserDefinedAlertsNotifications.setVbRole(vbRole);
								vbUserDefinedAlertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);

								if (OrganizationUtils.ALERT_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(notificationType)) {
									vbUserDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									vbUserDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									vbUserDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
								if (_logger.isDebugEnabled()) {
									_logger.debug("Updating VbUserAlertsNotifications");
								}
								session.save(vbUserDefinedAlertsNotifications);
						}
					}
				}
			}
			transaction.commit();
			session.close();
		} else {
			transaction.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}

	/**
	 * This method is used to update the userdefined alerts for multiple groups.
	 * 
	 * @param id - {@link Integer}
	 * @param alertsCommand - {@link AlertsCommand}
	 * @param multiGroupMap - {@link HashMap}
	 * @param notificationType - {@link String}
	 * @param organization - {@link VbOrganization}
	 * @param userName - {@link String}
	 * @param salesTypes - {@link String}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public void updateUserDefinedAlertForMultipleGroups(Integer id,	AlertsCommand alertsCommand,
			HashMap<String, String> multiGroupMap,
			String notificationType, VbOrganization organization,
			String userName, String salesTypes) throws DataAccessException {
		Session session = this.getSession();
		Transaction txn = session.beginTransaction();
		VbTrending vbTrending = null;
		VbExcessCash vbExcessCash = null;
		String[] groupUser = null;
		String[] groupNames = null;
		List<String> userGroup = null;
		String groupReceipient = null;
		String groupRecipients[] = null;
		List<VbEmployee> vbEmployeeList = null;
		String[] notifications = notificationType.split(",");
		VbUserDefinedAlertsNotifications userDefinedAlertsNotifications = null;
		List<String> notificationList = Arrays.asList(notifications);
		VbUserDefinedAlerts userDefinedAlerts = (VbUserDefinedAlerts) session.get(VbUserDefinedAlerts.class, id);
		if (userDefinedAlerts != null) {
			userDefinedAlerts.setAlertName(alertsCommand.getAlertName());
			userDefinedAlerts.setDescription(alertsCommand.getDescription());
			String alertType = alertsCommand.getAlertType();
			String previousAlertType = userDefinedAlerts.getVbAlertType().getAlertType();
			VbAlertCategory vbAlertCategory = getAlertCategoryForUserDefinedAlerts(session);
			VbAlertType vbAlertType = getVbAlertType(session, alertType, vbAlertCategory);
			userDefinedAlerts.setVbAlertType(vbAlertType);
			session.update(userDefinedAlerts);
			if (OrganizationUtils.ALERT_TYPE_TRENDING.equals(previousAlertType)) {
				if(previousAlertType .equals(alertType)){
					 vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.uniqueResult();
					if (vbTrending != null) {
						vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
						vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
						vbTrending.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.update(vbTrending);
				}
				}else{
					 vbTrending = (VbTrending) session.createCriteria(VbTrending.class)
								.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
								.uniqueResult();
					session.delete(vbTrending);
				}
			}else if(OrganizationUtils.ALERT_TYPE_TRENDING.equals(alertType)){
				vbTrending = new VbTrending();
				vbTrending.setAmountPersentage(alertsCommand.getAmountPercentage());
				vbTrending.setProductPercentage(alertsCommand.getProductPercentage());
				vbTrending.setVbUserDefinedAlerts(userDefinedAlerts);
				session.save(vbTrending);
			}
			
				
			if (OrganizationUtils.ALERT_TYPE_EXCESS_AMOUNT.equals(previousAlertType)) {
				if(previousAlertType .equals(alertType)){
					 vbExcessCash = (VbExcessCash) session.createCriteria(VbExcessCash.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts)).uniqueResult();
					if (vbExcessCash != null) {
						vbExcessCash.setAmount(alertsCommand.getAmount());
						vbExcessCash.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.update(vbExcessCash);
					}
				}else{
					vbExcessCash = (VbExcessCash) session.createCriteria(VbExcessCash.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts)).uniqueResult();
					session.delete(vbExcessCash);
				}
			}else if(OrganizationUtils.ALERT_TYPE_EXCESS_AMOUNT.equals(alertType)){
					vbExcessCash = new VbExcessCash();
					vbExcessCash.setAmount(alertsCommand.getAmount());
					vbExcessCash.setVbUserDefinedAlerts(userDefinedAlerts);
					session.save(vbExcessCash);
			}
			if(OrganizationUtils.ALERT_TYPE_MY_SALES.equals(previousAlertType)){
				if(previousAlertType.equals(alertType)){
					String[] salesTypesListArray = salesTypes.split(",");
					List<String> salesTypeList = Arrays.asList(salesTypesListArray);
					VbMySales vbMySales = null;
					List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.list();
					for (VbMySales mySales : vbMySalesList) {
						session.delete(mySales);
					}
					if (salesTypes != null) {
						VbAlertTypeMySales vbAlertTypeMySales = null;
						for (String salesType : salesTypeList) {
							VbAlertTypeMySalesPage vbAlertTypeMySalesPage = new VbAlertTypeMySalesPage();
							String salesTypesvlaue = alertsCommand.getAlertMySales();
							vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
									.createAlias("vbAlertType", "vbAlertType")
									.add(Restrictions.eq("vbAlertType.id", vbAlertType.getId()))
									.add(Restrictions.eq("alertMySales", salesTypesvlaue))
									.uniqueResult();
							vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
									.add(Restrictions.eq("alertMySalesPage", salesType))
									.createAlias("vbAlertTypeMySales", "vbAlertTypeMySales")
									.add(Restrictions.eq("vbAlertTypeMySales.id", vbAlertTypeMySales.getId()))
									.uniqueResult();
							vbMySales = (VbMySales) session.createCriteria(VbMySales.class)
									.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
									.add(Restrictions.eq("vbAlertTypeMySalesPage", vbAlertTypeMySalesPage))
									.uniqueResult();
							vbMySales = new VbMySales();
							vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
							vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
							vbMySales.setVbUserDefinedAlerts(userDefinedAlerts);
							
							session.save(vbMySales);
						}
					}
				
				}else{
					List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
							.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
							.list();
					for (VbMySales mySales : vbMySalesList) {
						session.delete(mySales);
					}
				}
			}else if (OrganizationUtils.ALERT_TYPE_MY_SALES.equals(alertType)) {
				String[] salesTypesListArray = salesTypes.split(",");
				List<String> salesTypeList = Arrays.asList(salesTypesListArray);
				VbMySales vbMySales = null;
				List<VbMySales> vbMySalesList = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
						.list();
				for (VbMySales mySales : vbMySalesList) {
					session.delete(mySales);
				}
				if (salesTypes != null) {
					VbAlertTypeMySales vbAlertTypeMySales = null;
					for (String salesType : salesTypeList) {
						VbAlertTypeMySalesPage vbAlertTypeMySalesPage = new VbAlertTypeMySalesPage();
						String salesTypesvlaue = alertsCommand.getAlertMySales();
						vbAlertTypeMySales = (VbAlertTypeMySales) session.createCriteria(VbAlertTypeMySales.class)
								.createAlias("vbAlertType", "vbAlertType")
								.add(Restrictions.eq("vbAlertType.id", vbAlertType.getId()))
								.add(Restrictions.eq("alertMySales", salesTypesvlaue))
								.uniqueResult();
						vbAlertTypeMySalesPage = (VbAlertTypeMySalesPage) session.createCriteria(VbAlertTypeMySalesPage.class)
								.add(Restrictions.eq("alertMySalesPage", salesType))
								.createAlias("vbAlertTypeMySales", "vbAlertTypeMySales")
								.add(Restrictions.eq("vbAlertTypeMySales.id", vbAlertTypeMySales.getId()))
								.uniqueResult();
						vbMySales = (VbMySales) session.createCriteria(VbMySales.class)
								.add(Restrictions.eq("vbUserDefinedAlerts",	userDefinedAlerts))
								.add(Restrictions.eq("vbAlertTypeMySalesPage", vbAlertTypeMySalesPage))
								.uniqueResult();
						vbMySales = new VbMySales();
						vbMySales.setVbAlertTypeMySales(vbAlertTypeMySales);
						vbMySales.setVbAlertTypeMySalesPage(vbAlertTypeMySalesPage);
						vbMySales.setVbUserDefinedAlerts(userDefinedAlerts);
						
						session.save(vbMySales);
					}
				}
			}
			//deleting the user defined alerts notification
			List<VbUserDefinedAlertsNotifications> userDefinedAlertsNotificationList = session.createCriteria(VbUserDefinedAlertsNotifications.class)
					.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts)).list();
			if (!userDefinedAlertsNotificationList.isEmpty()) {
				for (VbUserDefinedAlertsNotifications vbUserAlertsNotifications : userDefinedAlertsNotificationList) {
					session.delete(vbUserAlertsNotifications);
				}
			}
			VbNotifications vbNotifications = null;
			String empType = null;
			String group = null;
			String groupUsers = null;
			//Iterating the notification and saving to database. 
			for (String notification : notificationList) {
				vbNotifications = (VbNotifications) session.createCriteria(VbNotifications.class)
						.add(Restrictions.eq("notificationType", notification))
						.uniqueResult();
				vbEmployeeList = session.createCriteria(VbEmployee.class)
						.add(Restrictions.eq("employeeType", empType))
						.add(Restrictions.eq("vbOrganization", organization))
						.list();
				for (Map.Entry<String, String> entry : multiGroupMap.entrySet()) {
					group = entry.getKey();
					groupUsers = entry.getValue();
					groupNames = group.split(",");
					userGroup = Arrays.asList(groupNames);
					for(String groupName : userGroup){
						groupRecipients = groupName.split("/");
					
					VbRole vbRole = (VbRole) session.createCriteria(VbRole.class)
							.add(Restrictions.eq("description", groupRecipients[0]))
							.uniqueResult();
					empType = OrganizationUtils.getEmployeeTypeByRole(groupRecipients[0]);
					vbEmployeeList = session.createCriteria(VbEmployee.class)
							.add(Restrictions.eq("employeeType", empType))
							.add(Restrictions.eq("vbOrganization", organization))
							.list();

					if (ENotification.Emails.name().equals(notificationType)) {
						groupReceipient = groupRecipients[1];
					}
					groupUser = groupUsers.split(",");
					
					List<String> groupUserList = Arrays.asList(groupUser);
					// No users selected(for all users).
					if (groupUsers.isEmpty()) {
						for (VbEmployee vbEmployee : vbEmployeeList) {
							userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();
							userDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							userDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							userDefinedAlertsNotifications.setVbRole(vbRole);
							userDefinedAlertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
								}
							}
							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbUserDefinedAlertsNotifications");
							}
							session.save(userDefinedAlertsNotifications);
						}
					} else { // For specific group,user
						String[] userNameReceipient = null;
						for (String username : groupUserList) {
							userNameReceipient = username.split("/");
							VbEmployee vbEmployee = (VbEmployee) session.createCriteria(VbEmployee.class)
									.add(Restrictions.eq("username", userNameReceipient[0]))
									.add(Restrictions.eq("vbOrganization", organization))
									.uniqueResult();
							userDefinedAlertsNotifications = new VbUserDefinedAlertsNotifications();

							userDefinedAlertsNotifications.setVbEmployee(vbEmployee);
							userDefinedAlertsNotifications.setVbNotifications(vbNotifications);
							userDefinedAlertsNotifications.setVbRole(vbRole);
							userDefinedAlertsNotifications.setVbUserDefinedAlerts(userDefinedAlerts);

							if (ENotification.Emails.name().equals(notificationType)) {
								if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_TO.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
								} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(userNameReceipient[1])) {
									userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
								} else {
									if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_BCC.equalsIgnoreCase(groupReceipient)) {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.TRUE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									} else if (OrganizationUtils.EMAIL_RECEIPIENT_TYPE_CC.equalsIgnoreCase(groupReceipient)) {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.TRUE);
									} else {
										userDefinedAlertsNotifications.setMailsBcc(Boolean.FALSE);
										userDefinedAlertsNotifications.setMailsTo(Boolean.TRUE);
										userDefinedAlertsNotifications.setMailsCc(Boolean.FALSE);
									}
								}
							}

							if (_logger.isDebugEnabled()) {
								_logger.debug("Updating VbUserDefinedAlertsNotifications");
							}
							session.save(userDefinedAlertsNotifications);
						} // closing 3rd for
					} // closing else
				}
			} // closing 2nd for
			}
			txn.commit();
			session.close();
		} else {
			txn.rollback();
			session.close();
			String errorMsg = Msg.get(MsgEnum.UPDATE_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	} 

	/**
	 * This method is responsible to get the usernames associated with alerts.
	 * 
	 * @param id - {@link Integer}
	 * @param organization - {@link VbOrganization}
	 * @return userNamesList - {@link AlertsResult}
	 * @throws DataAccessException - {@link DataAccessException}
	 */
	@SuppressWarnings("unchecked")
	public List<AlertsResult> getUserNamesForThisAlert(Integer id, VbOrganization organization) throws DataAccessException {
		Session session = this.getSession();
		List<AlertsResult> userNamesList = new ArrayList<AlertsResult>();
		AlertsResult alertsResult = null;
		String notificationType = null;
		List<VbUserDefinedAlertsNotifications> userNames = (List<VbUserDefinedAlertsNotifications>) 
				session.createCriteria(VbUserDefinedAlertsNotifications.class)
				.createAlias("vbUserDefinedAlerts", "userAlerts")
				.add(Restrictions.eq("userAlerts.id",id))
				.list();
		for (VbUserDefinedAlertsNotifications vbUserDefinedAlertsNotifications : userNames) {
			 String alertType = vbUserDefinedAlertsNotifications.getVbUserDefinedAlerts().getVbAlertType().getAlertType();
			 VbUserDefinedAlerts userDefinedAlerts = vbUserDefinedAlertsNotifications.getVbUserDefinedAlerts();
			if(alertType.equals("My Sales")){
				List<VbMySales> vbMySales = (List<VbMySales>) session.createCriteria(VbMySales.class)
						.add(Restrictions.eq("vbUserDefinedAlerts", userDefinedAlerts))
						.list();
				AlertsResult alertsResult2 = null;
				for (VbMySales mySales : vbMySales) {
					alertsResult2 = new AlertsResult();
					alertsResult2.setUserName(vbUserDefinedAlertsNotifications.getVbEmployee().getUsername());
					alertsResult2.setRole(vbUserDefinedAlertsNotifications.getVbRole().getDescription());
					notificationType = vbUserDefinedAlertsNotifications.getVbNotifications().getNotificationType();
					alertsResult2.setNotificationType(notificationType);
					alertsResult2.setMailsBcc(vbUserDefinedAlertsNotifications.getMailsBcc());
					alertsResult2.setMailsCc(vbUserDefinedAlertsNotifications.getMailsCc());
					alertsResult2.setMailsTo(vbUserDefinedAlertsNotifications.getMailsTo());
					alertsResult2.setSalesType(mySales.getVbAlertTypeMySales().getAlertMySales());
					alertsResult2.setSalesPage(mySales.getVbAlertTypeMySalesPage().getAlertMySalesPage());
					
					userNamesList.add(alertsResult2);
				}
			}else{
				 alertsResult = new AlertsResult();
				alertsResult.setUserName(vbUserDefinedAlertsNotifications.getVbEmployee().getUsername());
				alertsResult.setRole(vbUserDefinedAlertsNotifications.getVbRole().getDescription());
				notificationType = vbUserDefinedAlertsNotifications.getVbNotifications().getNotificationType();
				alertsResult.setNotificationType(notificationType);
				alertsResult.setMailsBcc(vbUserDefinedAlertsNotifications.getMailsBcc());
				alertsResult.setMailsCc(vbUserDefinedAlertsNotifications.getMailsCc());
				alertsResult.setMailsTo(vbUserDefinedAlertsNotifications.getMailsTo());
				userNamesList.add(alertsResult);
			}
		}
		session.close();
		if(!userNames.isEmpty()) {
			if(_logger.isDebugEnabled()) {
				_logger.debug("Usernames associated with alert are {}:", userNamesList);
			}
			return userNamesList;
		} else {
			session.close();
			String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
			
			throw new DataAccessException(errorMsg);
		}
	}
/** This method is responsible for getting count of records from system alerts history and user defined history for Alert History.
 *  
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return countMysalesAlertsRecords - {@link Integer}
 * @throws ParseException - {@link ParseException}
 */
public Integer getAlertsHistoryRecordsCount(String alertCategory, String alertType, String alertName,
		VbOrganization organization, String strDate, String eDate) throws ParseException {
	Session session = this.getSession();
	Date startDate = null;
	Date endDate = null;
	if(!strDate.isEmpty()) {
		startDate = DateUtils.getStartTimeStamp(DateUtils.parse(strDate));
	}
	if(!eDate.isEmpty()) {
		endDate = DateUtils.getEndTimeStamp(DateUtils.parse(eDate));
	}
	Boolean addAlertCategory = Boolean.FALSE;
	Boolean addAlertType = Boolean.FALSE;
	Boolean addAlertName = Boolean.FALSE;
	Boolean addDate = Boolean.FALSE;
	Boolean addStartDate = Boolean.FALSE;
	Boolean addEndDate  = Boolean.FALSE;
	Query query = null;
	Query query1 = null;
	StringBuffer queryString = new StringBuffer("FROM VbSystemAlertsHistory vb WHERE vb.vbSystemAlerts.vbOrganization = :organization");
	StringBuffer queryString1 = new StringBuffer("FROM VbUserDefinedAlertsHistory vb WHERE vb.vbUserDefinedAlerts.vbOrganization = :organization");
	if(alertCategory != null && !(alertCategory.equals("-1"))) {
		queryString.append(" AND vb.vbSystemAlerts.vbAlertType.vbAlertCategory.alertCategory = :alertCategory");
		queryString1.append(" AND vb.vbUserDefinedAlerts.vbAlertType.vbAlertCategory.alertCategory = :alertCategory");
		addAlertCategory = Boolean.TRUE;
	}
	if(alertType != null && !(alertType.equals("-1"))) {
		queryString.append(" AND vb.vbSystemAlerts.vbAlertType.alertType = :alertType");
		queryString1.append(" AND vb.vbUserDefinedAlerts.vbAlertType.alertType = :alertType");
		addAlertType = Boolean.TRUE;
	}
	if(alertName != null && !alertName.isEmpty()) {
		queryString.append(" AND vb.vbSystemAlerts.alertName = :alertName");
		queryString1.append(" AND vb.vbUserDefinedAlerts.alertName = :alertName");
		addAlertName = Boolean.TRUE;
	}
	if(startDate != null && endDate != null) {
		queryString.append(" AND vb.createdOn BETWEEN :startDate AND :endDate");
		queryString1.append(" AND vb.createdOn BETWEEN :startDate AND :endDate");
		addDate = Boolean.TRUE;
	}
	if(startDate != null && endDate == null) {
		queryString.append(" AND vb.createdOn = :startDate");
		queryString1.append(" AND vb.createdOn = :startDate");
		addStartDate = Boolean.TRUE;
	}
	if(startDate == null && endDate != null) {
		queryString.append(" AND vb.createdOn = :endDate");
		queryString1.append(" AND vb.createdOn = :endDate");
		addEndDate = Boolean.TRUE;
	}
	query = session.createQuery(queryString.toString());
	query1 = session.createQuery(queryString1.toString());
	query.setParameter("organization", organization);
	query1.setParameter("organization", organization);
	if(addAlertCategory) {
		query.setParameter("alertCategory", alertCategory);
		query1.setParameter("alertCategory", alertCategory);
	}
	if(addAlertType) {
		query.setParameter("alertType", alertType);
		query1.setParameter("alertType", alertType);
	}
	if(addAlertName) {
		query.setParameter("alertName", alertName);
		query1.setParameter("alertName", alertName);
	}
	if(addDate) {
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query1.setParameter("startDate", startDate);
		query1.setParameter("endDate", endDate);
	}
	if(addStartDate){
		query.setParameter("startDate", startDate);
		query1.setParameter("startDate", startDate);
	}
	if(addEndDate) {
		query.setParameter("endDate", endDate);
		query1.setParameter("endDate", endDate);
	}
	Integer systemAlertsHistoryCount = query.list().size();
	if(_logger.isDebugEnabled()) {
		_logger.debug("Configured system alerts list size is: {}", systemAlertsHistoryCount);
	}
	Integer userDefinedAlertsHistoryCount = query1.list().size();
	session.close();
	
	if(_logger.isDebugEnabled()) {
		_logger.debug("Configured user defined alerts list size is: {}", userDefinedAlertsHistoryCount);
	}
	Integer totalRecordsCount = systemAlertsHistoryCount + userDefinedAlertsHistoryCount;
	
	if (_logger.isDebugEnabled()) {
		_logger.debug("totalRecordsCount: {}", totalRecordsCount);
	}
	return totalRecordsCount;
}
/** This method is responsible for getting count of records from in system notification for mysales alerts.
 *  
 * @param organization - {@link VbOrganization}
 * @param userName - {@link String}
 * @return countMysalesAlertsRecords - {@link Integer}
 * @throws DataAccessException 
 */
public Integer getRecordsCount(VbOrganization organization,String userName) throws DataAccessException {
	Session session = this.getSession();
	Date date = new Date();
	VbLogin login = (VbLogin) session.createCriteria(VbLogin.class)
			.add(Restrictions.eq("username", userName))
			.add(Restrictions.eq("vbOrganization", organization))
			.uniqueResult();
	Integer countMysalesAlertsRecords = session.createCriteria(VbInsystemAlertNotifications.class)
			.add(Restrictions.eq("vbLogin", login))
	        .add(Restrictions.eq("vbOrganization", organization))
	        .add(Restrictions.between("createdOn", DateUtils.getBeforeTwoDays(date), date))
			.list().size();
	session.close();
	
	if(countMysalesAlertsRecords != null){
		if (_logger.isDebugEnabled()) {
			_logger.debug("countMysalesAlertsRecords: {}", countMysalesAlertsRecords);
		}
		return countMysalesAlertsRecords;
	} else {
		String errorMsg = Msg.get(MsgEnum.RESULT_FAILURE_MESSAGE);
		
		throw new DataAccessException(errorMsg);
	}
}

}
